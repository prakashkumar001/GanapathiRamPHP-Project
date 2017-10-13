package com.ganapathyram.theatre.bluetooth.printer;

import android.os.Handler;
import android.util.Log;

import java.util.Arrays;
import java.util.LinkedList;

public class WoosimProtocolMode {
    private static final int BLOCK_SIZE = 8000;
    private static final boolean f9D = false;
    private static final byte ENQ = (byte) 5;
    private static final byte EOF = (byte) -63;
    private static final byte EOT = (byte) 4;
    private static final byte ETX = (byte) 3;
    public static final int MESSAGE_PROTOCOL_NO_RESPONSE = 202;
    public static final int MESSAGE_PROTOCOL_PRINT = 201;
    public static final int MESSAGE_PROTOCOL_TIMEOUT = 200;
    private static final byte SOF = (byte) -64;
    private static final String TAG = "WoosimProtocolMode";
    private static int mCurrentEOT;
    private static int mCurrentETX;
    private static LinkedList<byte[]> mList;
    private static byte[] mProtocolBuffer;
    private static int mProtocolBufferIndex;
    private static boolean mWaiting;
    private final Handler mHandler;

    public WoosimProtocolMode(Handler handler) {
        this.mHandler = handler;
        mWaiting = f9D;
        mProtocolBuffer = new byte[BLOCK_SIZE];
        mProtocolBufferIndex = 0;
        mCurrentETX = 0;
        mCurrentEOT = 0;
        mList = new LinkedList();
    }

    public void write(byte[] out) {
        if (mWaiting) {
            appendData(out);
            return;
        }
        mWaiting = true;
        this.mHandler.obtainMessage(MESSAGE_PROTOCOL_PRINT, encodeDataFrame(out)).sendToTarget();
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(MESSAGE_PROTOCOL_TIMEOUT, 4, mCurrentEOT), 1000);
    }

    public void invokeTimeout(int type, int id, int[] IDs) {
        if (mWaiting) {
            switch (type) {
                case 3:
                    if (mCurrentETX == id) {
                        sendENQFrame();
                        return;
                    }
                    return;
                case 4:
                    if (mCurrentEOT == id) {
                        sendENQFrame();
                        return;
                    }
                    return;
                case 5:
                    if (mCurrentEOT == IDs[0] && mCurrentETX == IDs[1]) {
                        clear();
                        this.mHandler.obtainMessage(MESSAGE_PROTOCOL_NO_RESPONSE).sendToTarget();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public void processACK() {
        processETX();
    }

    public void processNAK() {
        processETX();
    }

    public void processEOT() {
        mCurrentEOT++;
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(MESSAGE_PROTOCOL_TIMEOUT, 3, mCurrentETX), 10000);
    }

    public void processETX() {
        mCurrentETX++;
        if (mCurrentETX != mCurrentEOT) {
            mCurrentEOT = mCurrentETX;
        }
        if (mProtocolBufferIndex == 0) {
            mWaiting = f9D;
            return;
        }
        byte[] node = (byte[]) mList.poll();
        if (node == null) {
            byte[] data = Arrays.copyOf(mProtocolBuffer, mProtocolBufferIndex);
            mProtocolBufferIndex = 0;
            mWaiting = f9D;
            write(data);
            return;
        }
        mWaiting = f9D;
        write(node);
    }

    private void appendData(byte[] data) {
        if (data.length > BLOCK_SIZE) {
            Log.e(TAG, "Data block is too big to send in protocol mode. size:" + data.length);
            return;
        }
        if (data.length > 8000 - mProtocolBufferIndex) {
            mList.add(Arrays.copyOf(mProtocolBuffer, mProtocolBufferIndex));
            Arrays.fill(mProtocolBuffer, (byte) 0);
            mProtocolBufferIndex = 0;
        }
        System.arraycopy(data, 0, mProtocolBuffer, mProtocolBufferIndex, data.length);
        mProtocolBufferIndex += data.length;
    }

    private byte[] encodeDataFrame(byte[] data) {
        byte[] length = String.format("%04d", new Object[]{Integer.valueOf(data.length)}).getBytes();
        int j = 0;
        byte evenChecksum = (byte) 0;
        byte oddChecksum = (byte) 0;
        byte[] tmp = new byte[(data.length * 2)];
        int i = 0;
        while (i < data.length) {
            if ((i & 1) == 0) {
                evenChecksum = (byte) (data[i] ^ evenChecksum);
            } else {
                oddChecksum = (byte) (data[i] ^ oddChecksum);
            }
            int i2;
            if (data[i] == SOF || data[i] == EOF || data[i] == (byte) 125) {
                i2 = j + 1;
                tmp[j] = (byte) 125;
                j = i2 + 1;
                tmp[i2] = (byte) (data[i] ^ 32);
            } else {
                i2 = j + 1;
                tmp[j] = data[i];
                j = i2;
            }
            i++;
        }
        byte[] frameData = new byte[(j + 11)];
        frameData[0] = (byte) 0;
        frameData[1] = SOF;
        frameData[2] = WoosimService.FRAME_DATA;
        frameData[3] = (byte) 49;
        System.arraycopy(length, 0, frameData, 4, 4);
        System.arraycopy(tmp, 0, frameData, 8, j);
        frameData[j + 8] = evenChecksum;
        frameData[j + 9] = oddChecksum;
        frameData[j + 10] = EOF;
        return frameData;
    }

    private void sendENQFrame() {
        byte[] ENQframe = new byte[4];
        ENQframe[1] = SOF;
        ENQframe[2] = ENQ;
        ENQframe[3] = EOF;
        this.mHandler.obtainMessage(MESSAGE_PROTOCOL_PRINT, ENQframe).sendToTarget();
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(MESSAGE_PROTOCOL_TIMEOUT, 5, 0, new int[]{mCurrentEOT, mCurrentETX}), 500);
    }

    private void clear() {
        mList.clear();
        Arrays.fill(mProtocolBuffer, (byte) 0);
        mProtocolBufferIndex = 0;
        mCurrentETX = 0;
        mCurrentEOT = 0;
        mWaiting = f9D;
    }
}
