package com.ganapathyram.theatre.bluetooth.printer;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class WoosimCmd {
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_RIGHT = 2;
    public static final int CT_ARABIC_FARSI = 42;
    public static final int CT_ARABIC_FORMS_B = 43;
    public static final int CT_AZERBAIJANI = 24;
    public static final int CT_CP437 = 0;
    public static final int CT_CP720 = 40;
    public static final int CT_CP737 = 8;
    public static final int CT_CP775 = 11;
    public static final int CT_CP850 = 2;
    public static final int CT_CP852 = 6;
    public static final int CT_CP855 = 16;
    public static final int CT_CP857 = 7;
    public static final int CT_CP858 = 15;
    public static final int CT_CP860 = 3;
    public static final int CT_CP862 = 10;
    public static final int CT_CP863 = 4;
    public static final int CT_CP865 = 5;
    public static final int CT_CP866 = 9;
    public static final int CT_DBCS = 255;
    public static final int CT_HINDI_DEVANAGARI = 50;
    public static final int CT_IRAN_SYSTEM = 41;
    public static final int CT_ISO8859_15 = 13;
    public static final int CT_KATAKANA = 1;
    public static final int CT_POLISH = 12;
    public static final int CT_WIN1250 = 18;
    public static final int CT_WIN1251 = 17;
    public static final int CT_WIN1252 = 14;
    public static final int CT_WIN1253 = 19;
    public static final int CT_WIN1254 = 20;
    public static final int CT_WIN1255 = 21;
    public static final int CT_WIN1256 = 41;
    public static final int CT_WIN1257 = 23;
    public static final int CT_WIN1258 = 22;
    public static final int CT_WIN874 = 30;
    public static final int CUT_FULL = 0;
    public static final int CUT_PARTIAL = 1;
    private static final byte ESC = (byte) 27;
    public static final int FONT_LARGE = 0;
    public static final int FONT_MEDIUM = 1;
    public static final int FONT_SMALL = 2;
    private static final byte FS = (byte) 28;
    private static final byte GS = (byte) 29;
    public static final int MCU_ARM = 2;
    public static final int MCU_M16C = 1;
    public static final int MCU_RX = 3;
    private static final String TAG = "WoosimCmd";

    public static byte[] initPrinter() {
        return new byte[]{ESC, (byte) 64};
    }

    public static byte[] printData() {
        return new byte[]{(byte) 10};
    }

    public static byte[] printDotFeed(int n) {
        return new byte[]{ESC, (byte) 74, (byte) n};
    }

    public static byte[] printLineFeed(int n) {
        return new byte[]{ESC, (byte) 100, (byte) n};
    }

    public static byte[] setPageMode() {
        return new byte[]{ESC, (byte) 76};
    }

    public static byte[] cutPaper(int mode) {
        if (mode < 0 || mode > 1) {
            Log.e(TAG, "Invalid paper cutting mode: " + mode);
            return null;
        }
        return new byte[]{GS, (byte) 86, (byte) mode};
    }

    public static byte[] setPositionFromMark(int distance) {
        byte nl = (byte) (distance & 255);
        byte nh = (byte) ((distance >> 8) & 255);
        return new byte[]{ESC, (byte) 80, nl, nh};
    }

    public static byte[] feedToMark() {
        return new byte[]{ESC, (byte) 122, ESC, (byte) 121};
    }

    public static byte[] openCashDrawer() {
        byte[] cmd = new byte[5];
        cmd[0] = ESC;
        cmd[1] = (byte) 112;
        cmd[3] = (byte) 50;
        cmd[4] = (byte) 50;
        return cmd;
    }

    public static byte[] queryStatus() {
        return new byte[]{ESC, (byte) 118};
    }

    public static byte[] setMacro(byte[] cmd) {
        byte[] macroCmd = new byte[(cmd.length + 7)];
        macroCmd[0] = ESC;
        macroCmd[1] = FS;
        macroCmd[2] = (byte) 43;
        macroCmd[3] = (byte) 1;
        macroCmd[4] = (byte) 0;
        macroCmd[5] = (byte) (cmd.length & 255);
        macroCmd[6] = (byte) ((cmd.length >> 8) & 255);
        System.arraycopy(cmd, 0, macroCmd, 7, cmd.length);
        return macroCmd;
    }

    public static byte[] resetMacro() {
        byte[] cmd = new byte[7];
        cmd[0] = ESC;
        cmd[1] = FS;
        cmd[2] = (byte) 43;
        cmd[3] = (byte) 1;
        return cmd;
    }

    public static byte[] queryModelName() {
        return new byte[]{ESC, (byte) 89, (byte) -1};
    }

    public static byte[] queryDeviceVersion() {
        byte[] cmd = new byte[3];
        cmd[0] = ESC;
        cmd[2] = (byte) 1;
        return cmd;
    }

    public static byte[] MSR_1stTrackMode() {
        return new byte[]{ESC, (byte) 77, (byte) 67};
    }

    public static byte[] MSR_2ndTrackMode() {
        return new byte[]{ESC, (byte) 77, WoosimService.FRAME_DATA};
    }

    public static byte[] MSR_3rdTrackMode() {
        return new byte[]{ESC, (byte) 77, (byte) 71};
    }

    public static byte[] MSR_doubleTrackMode() {
        return new byte[]{ESC, (byte) 77, (byte) 69};
    }

    public static byte[] MSR_tripleTrackMode() {
        return new byte[]{ESC, (byte) 77, (byte) 70};
    }

    public static byte[] MSR_exit() {
        return new byte[]{(byte) 4};
    }

    public static byte[] SMSR_enter() {
        return new byte[]{ESC, (byte) 77, (byte) 67};
    }

    public static byte[] SMSR_exit() {
        return new byte[]{ESC, (byte) 77, (byte) 88};
    }

    public static byte[] SMSR_writeData(byte[] data, int length) {
        byte nl = (byte) (length & 255);
        byte nh = (byte) ((length >> 8) & 255);
        byte[] cmd = new byte[(length + 5)];
        cmd[0] = ESC;
        cmd[1] = (byte) 77;
        cmd[2] = WoosimService.FRAME_STATUS;
        cmd[3] = nh;
        cmd[4] = nl;
        System.arraycopy(data, 0, cmd, 5, length);
        return cmd;
    }

    public static byte[] SCR_enter() {
        return new byte[]{ESC, (byte) 78};
    }

    public static byte[] SCR_exit() {
        return new byte[]{(byte) 126, (byte) 4, (byte) 126};
    }

    public static byte[] SCR_enterPaymentMode() {
        byte[] cmd = new byte[3];
        cmd[0] = ESC;
        cmd[1] = (byte) 108;
        return cmd;
    }

    public static byte[] SCR_checkIntegrity() {
        return new byte[]{ESC, (byte) 108, (byte) 1};
    }

    public static byte[] SCR_enterKeyDownloadMode() {
        return new byte[]{ESC, (byte) 108, (byte) 2};
    }

    public static byte[] SCR_checkReaderStatus() {
        return new byte[]{ESC, (byte) 108, (byte) 3};
    }

    public static byte[] SCR_checkKeyValidation() {
        return new byte[]{ESC, (byte) 108, (byte) 4};
    }

    public static byte[] setCodeTable(int mcu, int codeTable, int size) {
        int n1 = codeTable;
        int n2 = size;
        if (mcu < 1 || mcu > 3) {
            Log.e(TAG, "Invalid MCU type: " + mcu);
            return null;
        } else if ((codeTable < 0 || codeTable > 50) && codeTable != 255) {
            Log.e(TAG, "Invalid code table: " + codeTable);
            return null;
        } else {
            if (size < 0 || size > 2) {
                Log.i(TAG, "Invalid font size (" + size + ") Use default font size");
                n2 = 0;
            }
            if (mcu == 1 || mcu == 2) {
                if (codeTable <= 3 || codeTable == 13 || codeTable == 12 || codeTable == 255) {
                    if (codeTable == 13) {
                        n1 = 4;
                    }
                    if (codeTable == 12) {
                        n1 = 5;
                    }
                    if (size == 2) {
                        Log.i(TAG, "Change to valid font size for specific MCU");
                        n2 = 1;
                    }
                } else {
                    Log.e(TAG, "Invalid code table for specific MCU: " + n1);
                    return null;
                }
            }
            return new byte[]{ESC, (byte) 116, (byte) n1, ESC, (byte) 33, (byte) n2};
        }
    }

    public static byte[] setTextStyle(boolean bold, boolean underline, boolean reverse, int extWidth, int extHeight) {
        int i;
        int i2 = 1;
        int n1 = extWidth - 1;
        int n2 = extHeight - 1;
        if (n1 < 0) {
            n1 = 0;
        }
        if (n1 > 7) {
            n1 = 7;
        }
        if (n2 < 0) {
            n2 = 0;
        }
        if (n2 > 7) {
            n2 = 7;
        }
        byte extension = (byte) ((n1 & 15) | ((n2 << 4) & 240));
        byte[] cmd = new byte[12];
        cmd[0] = ESC;
        cmd[1] = (byte) 69;
        if (bold) {
            i = 1;
        } else {
            i = 0;
        }
        cmd[2] = (byte) i;
        cmd[3] = ESC;
        cmd[4] = (byte) 45;
        if (underline) {
            i = 1;
        } else {
            i = 0;
        }
        cmd[5] = (byte) i;
        cmd[6] = GS;
        cmd[7] = (byte) 66;
        if (!reverse) {
            i2 = 0;
        }
        cmd[8] = (byte) i2;
        cmd[9] = GS;
        cmd[10] = (byte) 33;
        cmd[11] = extension;
        return cmd;
    }

    public static byte[] setCharacterSpace(int n) {
        return new byte[]{ESC, (byte) 32, (byte) n};
    }

    public static byte[] setLineSpace(int n) {
        return new byte[]{ESC, (byte) 51, (byte) n};
    }

    public static byte[] resetLineSpace() {
        return new byte[]{ESC, (byte) 50};
    }

    public static byte[] setUpsideDown(boolean mode) {
        int n;
        if (mode) {
            n = 1;
        } else {
            n = 0;
        }
        return new byte[]{ESC, (byte) 123, (byte) n};
    }

    public static byte[] setTextAlign(int align) {
        return new byte[]{ESC, (byte) 97, (byte) align};
    }

    public static byte[] moveAbsPosition(int distance) {
        byte nl = (byte) (distance & 255);
        byte nh = (byte) ((distance >> 8) & 255);
        return new byte[]{ESC, (byte) 36, nl, nh};
    }

    public static byte[] moveRelPosition(int distance) {
        byte nl = (byte) (distance & 255);
        byte nh = (byte) ((distance >> 8) & 255);
        return new byte[]{ESC, (byte) 92, nl, nh};
    }

    public static byte[] setTabPosition(int[] pos) {
        int count = pos.length;
        if (count > 32) {
            count = 32;
        }
        byte[] cmd = new byte[(count + 3)];
        int i = 0 + 1;
        cmd[0] = ESC;
        int i2 = i + 1;
        cmd[i] = WoosimService.FRAME_DATA;
        int j = 0;
        i = i2;
        while (j < count) {
            i2 = i + 1;
            cmd[i] = (byte) pos[j];
            j++;
            i = i2;
        }
        cmd[i] = (byte) 0;
        return cmd;
    }

    public static byte[] setLeftMargin(int margin) {
        byte nl = (byte) (margin & 255);
        byte nh = (byte) ((margin >> 8) & 255);
        return new byte[]{GS, (byte) 76, nl, nh};
    }

    public static byte[] setPrintingWidth(int width) {
        byte nl = (byte) (width & 255);
        byte nh = (byte) ((width >> 8) & 255);
        return new byte[]{GS, (byte) 87, nl, nh};
    }

    public static byte[] selectTTF(String filename) {
        byte[] cmd = new byte[]{ESC, (byte) 103, (byte) 70};
        ByteBuffer buffer = ByteBuffer.allocate((cmd.length + filename.getBytes().length) + 1);
        buffer.put(cmd);
        buffer.put(filename.getBytes());
        buffer.put((byte) 0);
        return buffer.array();
    }

    public static byte[] getTTFcode(int width, int height, String string) {
        byte[] cmd = new byte[]{ESC, (byte) 103, (byte) 85};
        byte[] bstr = null;
        try {
            bstr = string.getBytes("UTF-16BE");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ByteBuffer buffer = ByteBuffer.allocate((cmd.length + bstr.length) + 4);
        buffer.put(cmd);
        if (width > 255) {
            buffer.put((byte) -1);
        } else {
            buffer.put((byte) width);
        }
        if (height > 255) {
            buffer.put((byte) -1);
        } else {
            buffer.put((byte) height);
        }
        buffer.put(bstr);
        buffer.put((byte) 0);
        buffer.put((byte) 0);
        return buffer.array();
    }

    public static byte[] getTTFcodeArabic(int width, int height, String string) {
        byte[] cmd = new byte[]{ESC, (byte) 103, (byte) 85};
        byte[] arabic = null;
        ByteBuffer buffer = ByteBuffer.allocate((cmd.length + arabic.length) + 4);
        buffer.put(cmd);
        if (width > 255) {
            buffer.put((byte) -1);
        } else {
            buffer.put((byte) width);
        }
        if (height > 255) {
            buffer.put((byte) -1);
        } else {
            buffer.put((byte) height);
        }
        buffer.put(arabic);
        buffer.put((byte) 0);
        buffer.put((byte) 0);
        return buffer.array();
    }

    public static byte[] queryTTFWidth(int width, int height, String string) {
        byte[] cmd = new byte[]{ESC, (byte) 103, (byte) 78, (byte) 85};
        byte[] bstr = null;
        try {
            bstr = string.getBytes("UTF-16BE");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ByteBuffer buffer = ByteBuffer.allocate((cmd.length + bstr.length) + 4);
        buffer.put(cmd);
        if (width > 255) {
            buffer.put((byte) -1);
        } else {
            buffer.put((byte) width);
        }
        if (height > 255) {
            buffer.put((byte) -1);
        } else {
            buffer.put((byte) height);
        }
        buffer.put(bstr);
        buffer.put((byte) 0);
        buffer.put((byte) 0);
        return buffer.array();
    }

    public static byte[] PM_setStdMode() {
        return new byte[]{ESC, WoosimService.FRAME_STATUS};
    }

    public static byte[] PM_printData() {
        return new byte[]{ESC, (byte) 12};
    }

    public static byte[] PM_printStdMode() {
        return new byte[]{(byte) 12};
    }

    public static byte[] PM_deleteData() {
        return new byte[]{(byte) 24};
    }

    public static byte[] PM_setArea(int x, int y, int width, int height) {
        if (width <= 0 || height <= 0) {
            Log.e(TAG, "Invalid area");
            return null;
        }
        byte xl = (byte) (x & 255);
        byte xh = (byte) ((x >> 8) & 255);
        byte yl = (byte) (y & 255);
        byte yh = (byte) ((y >> 8) & 255);
        byte wl = (byte) (width & 255);
        byte wh = (byte) ((width >> 8) & 255);
        byte hl = (byte) (height & 255);
        byte hh = (byte) ((height >> 8) & 255);
        return new byte[]{ESC, (byte) 87, xl, xh, yl, yh, wl, wh, hl, hh};
    }

    public static byte[] PM_setDirection(int direction) {
        if (direction < 0 || direction > 3) {
            Log.e(TAG, "Invalid direction");
            return null;
        }
        return new byte[]{ESC, (byte) 84, (byte) direction};
    }

    public static byte[] PM_setPosition(int x, int y) {
        byte xl = (byte) (x & 255);
        byte xh = (byte) ((x >> 8) & 255);
        byte yl = (byte) (y & 255);
        byte yh = (byte) ((y >> 8) & 255);
        return new byte[]{ESC, (byte) 79, xl, xh, yl, yh};
    }

    public static byte[] PM_moveAbsVertical(int distance) {
        byte nl = (byte) (distance & 255);
        byte nh = (byte) ((distance >> 8) & 255);
        return new byte[]{GS, (byte) 36, nl, nh};
    }

    public static byte[] PM_moveRelVertical(int distance) {
        byte nl = (byte) (distance & 255);
        byte nh = (byte) ((distance >> 8) & 255);
        return new byte[]{GS, (byte) 92, nl, nh};
    }


}
