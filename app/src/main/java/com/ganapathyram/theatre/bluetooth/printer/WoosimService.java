package com.ganapathyram.theatre.bluetooth.printer;

import android.os.Handler;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class WoosimService {
    public static final byte ACK = (byte) 6;
    private static final byte CR = (byte) 13;
    private static final boolean f10D = false;
    public static final int ENCRYPTION_BASEKEY = 89;
    private static final byte EOF = (byte) -63;
    public static final byte EOT = (byte) 4;
    private static final byte ESC = (byte) 27;
    public static final byte ETX = (byte) 3;
    public static final byte FRAME_DATA = (byte) 68;
    public static final byte FRAME_STATUS = (byte) 83;
    private static final byte FS = (byte) 28;
    public static final int KEY_INDEX = 120;
    private static final byte LF = (byte) 10;
    public static final int MESSAGE_PRINTER = 100;
    public static final int MSR = 2;
    public static final byte NAK = (byte) 21;
    private static final byte NUL = (byte) 0;
    private static final byte SOF = (byte) -64;
    private static final String TAG = "WoosimService";
    public static final int UNPRESCRIBED = 0;
    public static int mTTFStringWidth;
    private final Handler mHandler;
    private ParsingThread mParsingTread = null;
    private Queue mRcvQueue = null;

    private class ParsingThread extends Thread {
        public void run() {
            while (!WoosimService.this.mRcvQueue.isEmpty()) {
                byte token = WoosimService.this.mRcvQueue.getByte();
                switch (token) {
                    case (byte) -64:
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        decodeFrameBlock();
                        break;
                    case (byte) 0:
                        break;
                    case (byte) 2:
                        try {
                            Thread.sleep(700);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        decodeMSRBlock();
                        break;
                    case (byte) 27:
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e22) {
                            e22.printStackTrace();
                        }
                        byte b1 = WoosimService.this.mRcvQueue.getByte();
                        byte b2 = WoosimService.this.mRcvQueue.getByte();
                        if (b1 != (byte) 77 || b2 != (byte) 49) {
                            if (b1 != (byte) 77 || (b2 != (byte) 88 && b2 != (byte) 89)) {
                                if (b1 != (byte) 77 || b2 != (byte) 120) {
                                    if (b1 == (byte) 103 && b2 == (byte) 78) {
                                        WoosimService.mTTFStringWidth = ((WoosimService.this.mRcvQueue.getByte() & 255) << 8) | (WoosimService.this.mRcvQueue.getByte() & 255);
                                        break;
                                    }
                                }
                                WoosimService.this.mHandler.obtainMessage(100, WoosimService.KEY_INDEX, WoosimService.this.mRcvQueue.getByte()).sendToTarget();
                                break;
                            }
                            byte b = WoosimService.this.mRcvQueue.getByte();
                            int result = -1;
                            if (b == (byte) 48) {
                                result = 1;
                            } else if (b == (byte) 57) {
                                result = 0;
                            }
                            if (result < 0) {
                                break;
                            }
                            WoosimService.this.mHandler.obtainMessage(100, 89, result).sendToTarget();
                            break;
                        }
                        WoosimService.this.mHandler.obtainMessage(100, 2, 0).sendToTarget();
                        break;

                    default:
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e222) {
                            e222.printStackTrace();
                        }
                        int dataLength = WoosimService.this.mRcvQueue.remainData();
                        ByteBuffer data = ByteBuffer.allocate(dataLength + 1);
                        data.put(token);
                        data.put(WoosimService.this.mRcvQueue.getBytes(dataLength));
                        WoosimService.this.mHandler.obtainMessage(100, 0, dataLength, data).sendToTarget();
                        WoosimService.this.mRcvQueue.reset();
                        break;
                }
            }
            synchronized (WoosimService.this) {
                WoosimService.this.mParsingTread = null;
                WoosimService.this.mRcvQueue.reset();
            }
        }

        private void decodeMSRBlock() {
            if (WoosimService.this.mRcvQueue.remainData() >= 3) {
                int MSRtype = WoosimService.this.mRcvQueue.getByte();
                WoosimService.this.mRcvQueue.skipBytes(2);
                int i = 0;
                byte[] tmp = new byte[110];
                byte[][] track = new byte[3][];
                while (!WoosimService.this.mRcvQueue.isEmpty()) {
                    byte token = WoosimService.this.mRcvQueue.getByte();
                    switch (token) {
                        case (byte) 3:
                            if (i == 37) {
                                track[1] = new byte[i];
                                System.arraycopy(tmp, 0, track[1], 0, i);
                            } else if (i == 104) {
                                track[2] = new byte[i];
                                System.arraycopy(tmp, 0, track[2], 0, i);
                            }
                            WoosimService.this.mHandler.obtainMessage(100, 2, MSRtype, track).sendToTarget();
                            break;
                        case (byte) 10:
                            return;
                        case WoosimCmd.CT_ISO8859_15 /*13*/:
                            break;
                        case (byte) 28:
                            if (i != 0) {
                                if (i != 76) {
                                    if (i != 37) {
                                        if (i != 104) {
                                            break;
                                        }
                                        track[2] = new byte[i];
                                        System.arraycopy(tmp, 0, track[2], 0, i);
                                    } else {
                                        track[1] = new byte[i];
                                        System.arraycopy(tmp, 0, track[1], 0, i);
                                    }
                                } else {
                                    track[0] = new byte[i];
                                    System.arraycopy(tmp, 0, track[0], 0, i);
                                }
                                i = 0;
                                break;
                            }
                            break;
                        default:
                            int i2 = i + 1;
                            tmp[i] = token;
                            if (i2 <= 104) {
                                i = i2;
                                break;
                            }
                            return;
                    }
                }
            }
        }

        private void decodeFrameBlock() {
            while (!WoosimService.this.mRcvQueue.isEmpty()) {
                switch (WoosimService.this.mRcvQueue.getByte()) {
                    case (byte) -63:
                    case WoosimCmd.CT_ISO8859_15 /*13*/:
                        break;
                    case (byte) 3:
                        WoosimService.this.mHandler.obtainMessage(100, 3, WoosimService.this.mRcvQueue.getByte(), Integer.valueOf(0)).sendToTarget();
                        break;
                    case (byte) 4:
                        WoosimService.this.mHandler.obtainMessage(100, 4, 0).sendToTarget();
                        break;
                    case (byte) 6:
                        WoosimService.this.mHandler.obtainMessage(100, 6, 0).sendToTarget();
                        break;
                    case (byte) 10:
                        return;
                    case WoosimCmd.CT_WIN1255 /*21*/:
                        WoosimService.this.mHandler.obtainMessage(100, 21, 0).sendToTarget();
                        break;

                    case (byte) 83:
                        WoosimService.this.mHandler.obtainMessage(100, 83, 0, WoosimService.this.mRcvQueue.getBytes(2)).sendToTarget();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private class Queue {
        private byte[] mmBuffer;
        private int mmFront = 0;
        private final int mmQsize;
        private int mmRear = 0;

        public Queue(int size) {
            this.mmBuffer = new byte[size];
            Arrays.fill(this.mmBuffer, WoosimService.NUL);
            this.mmQsize = size;
        }

        public boolean isEmpty() {
            if (this.mmFront == this.mmRear) {
                return true;
            }
            return WoosimService.f10D;
        }

        public int remainData() {
            return ((this.mmRear - this.mmFront) + this.mmQsize) % this.mmQsize;
        }

        private int remainSpace() {
            return this.mmQsize - remainData();
        }

        public synchronized boolean add(byte[] data, int length) {
            boolean z = WoosimService.f10D;
            synchronized (this) {
                if (length < remainSpace()) {
                    if (length > this.mmQsize - this.mmRear) {
                        int space = this.mmQsize - this.mmRear;
                        System.arraycopy(data, 0, this.mmBuffer, this.mmRear, space);
                        System.arraycopy(data, space, this.mmBuffer, 0, length - space);
                    } else {
                        System.arraycopy(data, 0, this.mmBuffer, this.mmRear, length);
                    }
                    this.mmRear = (this.mmRear + length) % this.mmQsize;
                    z = true;
                }
            }
            return z;
        }

        public synchronized byte getByte() {
            byte b;
            b = this.mmBuffer[this.mmFront];
            this.mmBuffer[this.mmFront] = WoosimService.NUL;
            this.mmFront++;
            this.mmFront %= this.mmQsize;
            return b;
        }

        public synchronized byte[] getBytes(int length) {
            byte[] bArr;
            if (remainData() < length) {
                bArr = null;
            } else {
                bArr = new byte[length];
                if (length > this.mmQsize - this.mmFront) {
                    System.arraycopy(this.mmBuffer, this.mmFront, bArr, 0, this.mmQsize - this.mmFront);
                    System.arraycopy(this.mmBuffer, 0, bArr, this.mmQsize - this.mmFront, length - (this.mmQsize - this.mmFront));
                    Arrays.fill(this.mmBuffer, this.mmFront, this.mmQsize, WoosimService.NUL);
                    Arrays.fill(this.mmBuffer, 0, length - (this.mmQsize - this.mmFront), WoosimService.NUL);
                } else {
                    System.arraycopy(this.mmBuffer, this.mmFront, bArr, 0, length);
                    Arrays.fill(this.mmBuffer, this.mmFront, this.mmFront + length, WoosimService.NUL);
                }
                this.mmFront = (this.mmFront + length) % this.mmQsize;
            }
            return bArr;
        }

        public synchronized boolean skipBytes(int n) {
            boolean z = WoosimService.f10D;
            synchronized (this) {
                if (remainData() >= n) {
                    if (n > this.mmQsize - this.mmFront) {
                        Arrays.fill(this.mmBuffer, this.mmFront, this.mmQsize, WoosimService.NUL);
                        Arrays.fill(this.mmBuffer, 0, n - (this.mmQsize - this.mmFront), WoosimService.NUL);
                    } else {
                        Arrays.fill(this.mmBuffer, this.mmFront, this.mmFront + n, WoosimService.NUL);
                    }
                    this.mmFront = (this.mmFront + n) % this.mmQsize;
                    z = true;
                }
            }
            return z;
        }

        public synchronized void reset() {
            Arrays.fill(this.mmBuffer, WoosimService.NUL);
            this.mmFront = 0;
            this.mmRear = 0;
        }
    }

    public WoosimService(Handler handler) {
        this.mHandler = handler;
    }

    public void processRcvData(byte[] data, int length) {
        if (this.mRcvQueue == null) {
            this.mRcvQueue = new Queue(1024);
        }
        this.mRcvQueue.add(data, length);
        if (this.mParsingTread == null) {
            this.mParsingTread = new ParsingThread();
            this.mParsingTread.start();
        }
    }

    public void clearRcvBuffer() {
        if (this.mRcvQueue != null) {
            this.mRcvQueue.reset();
        }
    }
}
