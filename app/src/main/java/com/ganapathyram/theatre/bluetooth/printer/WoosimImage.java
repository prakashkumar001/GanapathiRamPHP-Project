package com.ganapathyram.theatre.bluetooth.printer;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

public class WoosimImage {
    private static final byte CAN = (byte) 24;
    private static final int DITHERING_FLOYD_STEINBERG = 2;
    private static final int DITHERING_NO = 0;
    private static final int DITHERING_RANDOM_THRESHOLD = 1;
    private static final byte ESC = (byte) 27;
    private static final byte GS = (byte) 29;
    private static final int MAX_RLE_LENGTH = 62;
    private static final String TAG = "WoosimImage";
    private static final byte[] cmd_ESCFF = new byte[]{ESC, (byte) 12};
    private static double[] threshold = new double[]{0.25d, 0.26d, 0.27d, 0.28d, 0.29d, 0.3d, 0.31d, 0.32d, 0.33d, 0.34d, 0.35d, 0.36d, 0.37d, 0.38d, 0.39d, 0.4d, 0.41d, 0.42d, 0.43d, 0.44d, 0.45d, 0.46d, 0.47d, 0.48d, 0.49d, 0.5d, 0.51d, 0.52d, 0.53d, 0.54d, 0.55d, 0.56d, 0.57d, 0.58d, 0.59d, 0.6d, 0.61d, 0.62d, 0.63d, 0.64d, 0.65d, 0.66d, 0.67d, 0.68d, 0.69d};

    public static byte[] printStoredImage(int id) {
        if (id < 1 || id > 60) {
            Log.e(TAG, "Invalid stored image number: " + id);
            return null;
        }
        return new byte[]{ESC, (byte) 102, (byte) (id - 1), (byte) 12};
    }

    public static byte[] printBitmap(int x, int y, int width, int height, Bitmap bmp) {
        return printImage(x, y, width, height, bmp, false, 0);
    }

    public static byte[] printCompressedBitmap(int x, int y, int width, int height, Bitmap bmp) {
        return printImage(x, y, width, height, bmp, true, 0);
    }

    public static byte[] printColorBitmap(int x, int y, int width, int height, Bitmap bmp) {
        return printImage(x, y, width, height, bmp, true, 2);
    }

    private static byte[] printImage(int x, int y, int width, int height, Bitmap bmp, boolean compressing, int ditherType) {
        if (width <= 0) {
            width = bmp.getWidth();
        }
        if (height <= 0) {
            height = bmp.getHeight();
        }
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(1024);
        byte xl = (byte) (x & 255);
        byte xh = (byte) ((x >> 8) & 255);
        byte yl = (byte) (y & 255);
        byte yh = (byte) ((y >> 8) & 255);
        byte wl = (byte) (width & 255);
        byte wh = (byte) ((width >> 8) & 255);
        byte hl = (byte) (height & 255);
        byte hh = (byte) ((height >> 8) & 255);
        byte[] cmd_ESCW = new byte[]{ESC, (byte) 87, xl, xh, yl, yh, wl, wh, hl, hh};
        byteStream.write(cmd_ESCW, 0, cmd_ESCW.length);
        byte[] img = convertBMPtoX4image(bmp, ditherType);
        int bmpW = bmp.getWidth();
        int bmpH = bmp.getHeight();
        int imgRowBytes = (bmpW / 8) + (bmpW % 8 == 0 ? 0 : 1);
        int page = 0;
        byte[] cmd_ESCX3;
        byte[] compressedImg;
        byte[] cmd_ESCX4;
        while (page < bmpH / 255) {
            if (compressing) {
                 cmd_ESCX3 = new byte[]{ESC, (byte) 88, (byte) 51, (byte) imgRowBytes, (byte) -1};
                 compressedImg = convertImageX4toX3(img, (imgRowBytes * 255) * page, imgRowBytes * 255);
                byteStream.write(cmd_ESCX3, 0, cmd_ESCX3.length);
                byteStream.write(compressedImg, 0, compressedImg.length);
            } else {
               cmd_ESCX4 = new byte[]{ESC, (byte) 88, (byte) 52, (byte) imgRowBytes, (byte) -1};
                byteStream.write(cmd_ESCX4, 0, cmd_ESCX4.length);
                byteStream.write(img, (imgRowBytes * 255) * page, imgRowBytes * 255);
            }
            int offset = (page + 1) * 255;
            byte ol = (byte) (offset & 255);
            byte oh = (byte) ((offset >> 8) & 255);
            byte[] cmd_ESCO = new byte[6];
            cmd_ESCO[0] = ESC;
            cmd_ESCO[1] = (byte) 79;
            cmd_ESCO[4] = ol;
            cmd_ESCO[5] = oh;
            byteStream.write(cmd_ESCO, 0, cmd_ESCO.length);
            page++;
        }
        if (bmpH % 255 != 0) {
            if (compressing) {
                cmd_ESCX3 = new byte[]{ESC, (byte) 88, (byte) 51, (byte) imgRowBytes, (byte) (bmpH % 255)};
                compressedImg = convertImageX4toX3(img, (imgRowBytes * 255) * page, (bmpH % 255) * imgRowBytes);
                byteStream.write(cmd_ESCX3, 0, cmd_ESCX3.length);
                byteStream.write(compressedImg, 0, compressedImg.length);
            } else {
                cmd_ESCX4 = new byte[]{ESC, (byte) 88, (byte) 52, (byte) imgRowBytes, (byte) (bmpH % 255)};
                byteStream.write(cmd_ESCX4, 0, cmd_ESCX4.length);
                byteStream.write(img, (imgRowBytes * 255) * page, (bmpH % 255) * imgRowBytes);
            }
        }
        byteStream.write(cmd_ESCFF, 0, cmd_ESCFF.length);

        return byteStream.toByteArray();
    }

    private static byte[] convertBMPtoX4image(Bitmap bmpOrg, int ditherType) {
        Bitmap bmp;
        if (ditherType > 1) {
            bmp = convertGrayscale(bmpOrg);
        } else {
            bmp = bmpOrg;
        }
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[(height * width)];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int imgRowBytes = (width / 8) + (width % 8 == 0 ? 0 : 1);
        byte[] img = new byte[(imgRowBytes * height)];
        Arrays.fill(img, (byte) 0);
        Random rand = new Random();
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                int byteCount = w / 8;
                int bitCount = w % 8;
                int currIndex = (h * width) + w;
                int currPixel = pixels[currIndex];
                int destIndex = (h * imgRowBytes) + byteCount;
                switch (ditherType) {
                    case 0:
                        if ((Color.red(currPixel) + Color.green(currPixel)) + Color.blue(currPixel) < 702 && currPixel != 0) {
                            img[destIndex] = (byte) (img[destIndex] | (1 << (7 - bitCount)));
                            break;
                        }
                    case 1:
                        if (((double) ((((((float) Color.red(currPixel)) * 0.21f) + (((float) Color.green(currPixel)) * 0.71f)) + (((float) Color.blue(currPixel)) * 0.07f)) / 255.0f)) <= threshold[rand.nextInt(threshold.length)] && currPixel != 0) {
                            img[destIndex] = (byte) (img[destIndex] | (1 << (7 - bitCount)));
                            break;
                        }
                    case 2:
                        int i;
                        int roundColor = Color.blue(currPixel) < 128 ? 0 : 255;
                        int errorAmount = Color.blue(currPixel) - roundColor;
                        if (roundColor == 0 && currPixel != 0) {
                            img[destIndex] = (byte) (img[destIndex] | (1 << (7 - bitCount)));
                        }
                        if (w + 1 < width && pixels[currIndex + 1] != 0) {
                            i = currIndex + 1;
                            pixels[i] = pixels[i] + ((errorAmount * 7) >> 4);
                        }
                        if (h + 1 != height) {
                            if (w > 0 && pixels[(currIndex + width) - 1] != 0) {
                                i = (currIndex + width) - 1;
                                pixels[i] = pixels[i] + ((errorAmount * 3) >> 4);
                            }
                            if (pixels[currIndex + width] != 0) {
                                i = currIndex + width;
                                pixels[i] = pixels[i] + ((errorAmount * 5) >> 4);
                            }
                            if (w + 1 < width && pixels[(currIndex + width) + 1] != 0) {
                                i = (currIndex + width) + 1;
                                pixels[i] = pixels[i] + ((errorAmount * 1) >> 4);
                                break;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return img;
    }

    private static Bitmap convertGrayscale(Bitmap bmpOriginal) {
        int height = bmpOriginal.getHeight();
        int width = bmpOriginal.getWidth();
        Rect rect = new Rect(0, 0, width, height);
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0f);
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        c.drawBitmap(bmpOriginal, null, rect, paint);
        return bmpGrayscale;
    }

    private static byte[] convertImageX4toX3(byte[] imgX4, int offset, int length) {
        ByteArrayOutputStream imgX3 = new ByteArrayOutputStream(1024);
        byte front = imgX4[offset];
        int sameByteCount = 0;
        int diffByteCount = 1;
        for (int i = 0; i < length; i++) {
            byte b = imgX4[offset + i];
            if (front == b) {
                sameByteCount++;
                if (sameByteCount >= 3 && diffByteCount > 1) {
                    imgX3.write((diffByteCount + 128) - 1);
                    imgX3.write(imgX4, ((offset + i) - diffByteCount) - 1, diffByteCount - 1);
                    diffByteCount = 1;
                }
                if (sameByteCount > MAX_RLE_LENGTH) {
                    imgX3.write(254);
                    imgX3.write(front);
                    sameByteCount = 1;
                }
            } else {
                diffByteCount++;
                if (sameByteCount >= 3) {
                    imgX3.write(sameByteCount + 192);
                    imgX3.write(front);
                    diffByteCount--;
                } else if (sameByteCount == 2) {
                    diffByteCount++;
                }
                if (diffByteCount > MAX_RLE_LENGTH) {
                    imgX3.write(190);
                    imgX3.write(imgX4, ((offset + i) - diffByteCount) + 1, MAX_RLE_LENGTH);
                    diffByteCount -= 62;
                }
                sameByteCount = 1;
            }
            front = b;
        }
        if (sameByteCount >= 3) {
            imgX3.write(sameByteCount + 192);
            imgX3.write(front);
        } else if (sameByteCount == 2) {
            imgX3.write(130);
            imgX3.write(imgX4, (offset + length) - 2, 2);
        } else {
            imgX3.write(diffByteCount + 128);
            imgX3.write(imgX4, (offset + length) - diffByteCount, diffByteCount);
        }
        return imgX3.toByteArray();
    }

    public static byte[] fastPrintBitmap(int x, int y, int width, int height, Bitmap bmp) {
        byte[] cmd_ESCW;
        byte[] cmd_ESCX4;
        if (width <= 0) {
            width = bmp.getWidth();
        }
        if (height <= 0) {
            height = bmp.getHeight();
        }
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(1024);
        byte xl = (byte) (x & 255);
        byte xh = (byte) ((x >> 8) & 255);
        byte yl = (byte) (y & 255);
        byte yh = (byte) ((y >> 8) & 255);
        byte wl = (byte) (width & 255);
        byte wh = (byte) ((width >> 8) & 255);
        byte[] img = convertBMPtoX4image(bmp, 0);
        int bmpW = bmp.getWidth();
        int bmpH = bmp.getHeight();
        int imgRowBytes = (bmpW / 8) + (bmpW % 8 == 0 ? 0 : 1);
        boolean startBand = true;
        if (bmpH > height) {
            bmpH = height;
        }
        byteStream.write(24);
        int page = 0;
        while (page < bmpH / 255) {
           cmd_ESCW = new byte[10];
            cmd_ESCW[0] = ESC;
            cmd_ESCW[1] = (byte) 87;
            cmd_ESCW[2] = xl;
            cmd_ESCW[3] = xh;
            cmd_ESCW[6] = wl;
            cmd_ESCW[7] = wh;
            cmd_ESCW[8] = (byte) -1;
            if (startBand) {
                cmd_ESCW[4] = yl;
                cmd_ESCW[5] = yh;
                startBand = false;
            }
            byteStream.write(cmd_ESCW, 0, cmd_ESCW.length);
            cmd_ESCX4 = new byte[]{ESC, (byte) 88, (byte) 52, (byte) imgRowBytes, (byte) -1};
            byteStream.write(cmd_ESCX4, 0, cmd_ESCX4.length);
            byteStream.write(img, (imgRowBytes * 255) * page, imgRowBytes * 255);
            byteStream.write(cmd_ESCFF, 0, cmd_ESCFF.length);
            byteStream.write(24);
            page++;
        }
        if (bmpH % 255 != 0) {
            cmd_ESCW = new byte[10];
            cmd_ESCW[0] = ESC;
            cmd_ESCW[1] = (byte) 87;
            cmd_ESCW[2] = xl;
            cmd_ESCW[3] = xh;
            cmd_ESCW[6] = wl;
            cmd_ESCW[7] = wh;
            cmd_ESCW[8] = (byte) (bmpH % 255);
            if (startBand) {
                cmd_ESCW[4] = yl;
                cmd_ESCW[5] = yh;
            }
            byteStream.write(cmd_ESCW, 0, cmd_ESCW.length);
            cmd_ESCX4 = new byte[]{ESC, (byte) 88, (byte) 52, (byte) imgRowBytes, (byte) (bmpH % 255)};
            byteStream.write(cmd_ESCX4, 0, cmd_ESCX4.length);
            byteStream.write(img, (imgRowBytes * 255) * page, (bmpH % 255) * imgRowBytes);
            byteStream.write(cmd_ESCFF, 0, cmd_ESCFF.length);
            byteStream.write(24);
        }
        return byteStream.toByteArray();
    }

    public static byte[] drawBitmap(int x, int y, Bitmap bmp) {
        return drawImage(x, y, bmp, 0);
    }

    public static byte[] drawColorBitmap(int x, int y, Bitmap bmp) {
        return drawImage(x, y, bmp, 2);
    }

    private static byte[] drawImage(int x, int y, Bitmap bmp, int ditherType) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int imgRowBytes = (width / 8) + (width % 8 == 0 ? 0 : 1);
        byte xl = (byte) (x & 255);
        byte xh = (byte) ((x >> 8) & 255);
        byte yl = (byte) (y & 255);
        byte yh = (byte) ((y >> 8) & 255);
        byte wl = (byte) (width & 255);
        byte wh = (byte) ((width >> 8) & 255);
        byte hl = (byte) (height & 255);
        byte hh = (byte) ((height >> 8) & 255);
        byte[] cmd_ESCW = new byte[]{ESC, (byte) 87, xl, xh, yl, yh, wl, wh, hl, hh};
        byte[] cmd_ESCX4 = new byte[]{ESC, (byte) 88, (byte) 52, (byte) imgRowBytes, (byte) height};
        byte[] img = convertBMPtoX4image(bmp, ditherType);
        ByteBuffer buffer = ByteBuffer.allocate((cmd_ESCW.length + cmd_ESCX4.length) + img.length);
        buffer.put(cmd_ESCW);
        buffer.put(cmd_ESCX4);
        buffer.put(img);
        return buffer.array();
    }

    public static byte[] drawBox(int x, int y, int width, int height, int thickness) {
        if (width > 0 || height > 0) {
            byte xl = (byte) (x & 255);
            byte xh = (byte) ((x >> 8) & 255);
            byte yl = (byte) (y & 255);
            byte yh = (byte) ((y >> 8) & 255);
            byte wl = (byte) (width & 255);
            byte wh = (byte) ((width >> 8) & 255);
            byte hl = (byte) (height & 255);
            byte hh = (byte) ((height >> 8) & 255);
            return new byte[]{ESC, (byte) 79, xl, xh, yl, yh, GS, (byte) 105, wl, wh, hl, hh, (byte) thickness};
        }
        Log.e(TAG, "Invalid parameters on width and/or height.");
        return null;
    }

    public static byte[] drawLine(int x1, int y1, int x2, int y2, int thickness) {
        if (x1 < 0 || y1 < 0 || x2 < 0 || y2 < 0 || thickness <= 0) {
            Log.e(TAG, "Invalid parameter.");
            return null;
        }
        if (thickness > 255) {
            thickness = 255;
        }
        byte x1l = (byte) (x1 & 255);
        byte x1h = (byte) ((x1 >> 8) & 255);
        byte y1l = (byte) (y1 & 255);
        byte y1h = (byte) ((y1 >> 8) & 255);
        byte x2l = (byte) (x2 & 255);
        byte x2h = (byte) ((x2 >> 8) & 255);
        byte y2l = (byte) (y2 & 255);
        byte y2h = (byte) ((y2 >> 8) & 255);
        byte thick = (byte) (thickness & 255);
        return new byte[]{ESC, (byte) 103, (byte) 49, x1l, x1h, y1l, y1h, x2l, x2h, y2l, y2h, thick};
    }

    public static byte[] drawEllipse(int x, int y, int radiusW, int radiusH, int thickness) {
        if (x < 0 || y < 0 || radiusW <= 0 || radiusH <= 0 || thickness <= 0) {
            Log.e(TAG, "Invalid parameter.");
            return null;
        }
        if (thickness > 255) {
            thickness = 255;
        }
        byte xl = (byte) (x & 255);
        byte xh = (byte) ((x >> 8) & 255);
        byte yl = (byte) (y & 255);
        byte yh = (byte) ((y >> 8) & 255);
        byte wl = (byte) (radiusW & 255);
        byte wh = (byte) ((radiusW >> 8) & 255);
        byte hl = (byte) (radiusH & 255);
        byte hh = (byte) ((radiusH >> 8) & 255);
        byte thick = (byte) (thickness & 255);
        return new byte[]{ESC, (byte) 103, (byte) 50, xl, xh, yl, yh, wl, wh, hl, hh, thick};
    }

    public static byte[] printARGBbitmap(int x, int y, int width, int height, Bitmap bmp) {
        Bitmap opaqueBmp = removeAlphaValue(bmp);
        byte[] result = printRGBbitmap(x, y, width, height, opaqueBmp);
        opaqueBmp.recycle();
        return result;
    }

    public static byte[] printRGBbitmap(int x, int y, int width, int height, Bitmap bmp) {
        return printBitmap(x, y, width, height, bmp);
    }

    public static byte[] bmp2PrintableImage(int x, int y, int width, int height, Bitmap bmp) {
        return printRGBbitmap(x, y, width, height, bmp);
    }

    private static Bitmap removeAlphaValue(Bitmap bmp) {
        Bitmap clone = bmp.copy(bmp.getConfig(), true);
        int w = clone.getWidth();
        int h = clone.getHeight();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (clone.getPixel(i, j) == 0) {
                    clone.setPixel(i, j, -1);
                }
            }
        }
        return clone;
    }

    public static byte[] putARGBbitmap(int x, int y, Bitmap bmp) {
        Bitmap opaqueBmp = removeAlphaValue(bmp);
        byte[] result = putRGBbitmap(x, y, opaqueBmp);
        opaqueBmp.recycle();
        return result;
    }

    public static byte[] putRGBbitmap(int x, int y, Bitmap bmp) {
        return drawBitmap(x, y, bmp);
    }

    public static byte[] fastPrintARGBbitmap(int x, int y, int width, int height, Bitmap bmp) {
        Bitmap opaqueBmp = removeAlphaValue(bmp);
        byte[] result = fastPrintRGBbitmap(x, y, width, height, opaqueBmp);
        opaqueBmp.recycle();
        return result;
    }

    public static byte[] fastPrintRGBbitmap(int x, int y, int width, int height, Bitmap bmp) {
        return fastPrintBitmap(x, y, width, height, bmp);
    }
}
