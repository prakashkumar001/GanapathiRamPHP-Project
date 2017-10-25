package com.ganapathyram.theatre.parking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.citizen.sdk.ESCPOSConst;
import com.citizen.sdk.ESCPOSPrinter;
import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.Test;
import com.ganapathyram.theatre.activities.DashBoard;
import com.ganapathyram.theatre.activities.Home;
import com.ganapathyram.theatre.adapter.ParkingAdapter;
import com.ganapathyram.theatre.adapter.ProductListAdapter;
import com.ganapathyram.theatre.bluetooth.DeviceListActivity;
import com.ganapathyram.theatre.bluetooth.printer.WoosimCmd;
import com.ganapathyram.theatre.bluetooth.printer.WoosimImage;
import com.ganapathyram.theatre.bluetooth.printer.WoosimProtocolMode;
import com.ganapathyram.theatre.bluetooth.utils.ESCPOSDriver;
import com.ganapathyram.theatre.bluetooth.utils.PrinterCommands;
import com.ganapathyram.theatre.bluetooth.utils.Utils;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.model.Parking;
import com.ganapathyram.theatre.model.Product;
import com.ganapathyram.theatre.utils.TableBuilder;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

import static com.ganapathyram.theatre.common.GlobalClass.bluetoothStatus;

/**
 * Created by Prakash on 9/21/2017.
 */

public class ParkingDashboard extends AppCompatActivity implements Runnable{
    public RecyclerView parkinglist;
    ArrayList<Parking> list;
    ParkingAdapter adapter;


    //for bluetooth
    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    Button mScan, mPrint, mDisc;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    public BufferedOutputStream outputStream;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_dashboard);

        //for bluetooth
        if(bluetoothStatus!=null)
        {
            if(isBluetoothConnected())
            {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                mBluetoothDevice = mBluetoothAdapter
                        .getRemoteDevice(bluetoothStatus);
                Thread mBlutoothConnectThread = new Thread(this);
                mBlutoothConnectThread.start();


            }else
            {

                bluetoothStatus=null;

            }



        }




        parkinglist = (RecyclerView) findViewById(R.id.parking);
        list=new ArrayList<>();
        list.add(new Parking("BIKE PARKING",R.mipmap.bike_icon));
        list.add(new Parking("CAR PARKING",R.mipmap.car_icon));
        list.add(new Parking("AUTO PARKING",R.mipmap.auto_icon));
        list.add(new Parking("HEAVY PARKING",R.mipmap.truck_icon));
        list.add(new Parking("CYCLE PARKING",R.mipmap.cycle_icon));


        final int columns = getResources().getInteger(R.integer.grid_column);

        adapter=new ParkingAdapter(ParkingDashboard.this,list);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),columns);
        parkinglist.setLayoutManager(layoutManager);
        parkinglist.setItemAnimator(new DefaultItemAnimator());
        parkinglist.setAdapter(adapter);
        parkinglist.setNestedScrollingEnabled(false);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(ParkingDashboard.this, Home.class);
        startActivity(i);
        finish();
    }

    public void printCheck(final int drawable, final String type)
    {
        Thread t = new Thread() {
            public void run() {
                try {
                    ESCPOSDriver escposDriver = new ESCPOSDriver();
                    OutputStream os = mBluetoothSocket
                            .getOutputStream();
                    outputStream=new BufferedOutputStream(os);
                    printImage(R.drawable.printer_logo);
                    String dateTime[] = getDateTime();
                    printText(leftRightAlign(dateTime[0], dateTime[1]));
                    printNewLine();
                    // printCustom(dateTime[0]+"   "+dateTime[1],0,1);
                    printCustom("Ganapathi Theatre",0,1);
                    printCustom("101, Lattice Bridge Road, Adyar, Baktavatsalm Nagar, Chennai, Tamil Nadu 600020",0,1);
                    printCustom("Phone: 044 2441 7424",0,1);

                    //printImageVehicle(drawable,type);
                    printCustom(type,0,1);






                    printCustom(new String(new char[32]).replace("\0", "."),0,1);




                    printNewLine();
                    printCustom("Thank you for coming & we look",0,1);
                    printCustom("forward to serve you again",0,1);
                    printUnicode();
                    printNewLine();
                    printNewLine();
                    outputStream.write(escposDriver.PAPER_CUT);
                    printNewLine();
                    printNewLine();


                    outputStream.flush();
                } catch (Exception e) {
                    Log.e("Main", "Exe ", e);
                }
            }
        };
        t.start();
    }

    public void onItemClick(int position) {



        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(ParkingDashboard.this, "Message1", 2000).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,
                        REQUEST_ENABLE_BT);
            } else {

                if(bluetoothStatus!=null)
                {

                    String name=list.get(position).name;
                    if(name.equalsIgnoreCase("BIKE PARKING"))
                    {
                        printCheck(R.drawable.bike_bw,name);

                    }else  if(name.equalsIgnoreCase("CAR PARKING"))
                    {
                        printCheck(R.drawable.car_bw,name);

                    }else  if(name.equalsIgnoreCase("AUTO PARKING"))
                    {
                        printCheck(R.drawable.auto_bw,name);
                    }else  if(name.equalsIgnoreCase("HEAVY PARKING"))
                    {
                        printCheck(R.drawable.bus_bw,name);
                    }else  if(name.equalsIgnoreCase("CYCLE PARKING"))
                    {
                        printCheck(R.drawable.bike_bw,name);
                    }


                }else
                {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(ParkingDashboard.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent,
                            REQUEST_CONNECT_DEVICE);
                }

            }
        }

        }


    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    bluetoothStatus=mDeviceAddress;
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, true);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(ParkingDashboard.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(ParkingDashboard.this, "Message", 2000).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(mBluetoothConnectProgressDialog!=null)
            {
                mBluetoothConnectProgressDialog.dismiss();
            }
            //
            //bluetoothStatus="Connected";
            Toast.makeText(ParkingDashboard.this, "DeviceConnected", 5000).show();
        }
    };
    //print custom
    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B,0x21,0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text
        try {
            switch (size){
                case 0:
                    outputStream.write(cc);
                    break;
                case 1:
                    outputStream.write(bb);
                    break;
                case 2:
                    outputStream.write(bb2);
                    break;
                case 3:
                    outputStream.write(bb3);
                    break;
            }

            switch (align){
                case 0:
                    //left align
                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            outputStream.write(msg.getBytes());
            outputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //print unicode
    public void printUnicode(){
        try {
            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(Utils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //print new line
    private void printNewLine() {
        try {
            outputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //print text
    private void printText(String msg) {
        try {
            // Print normal text
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print byte[]
    private void printText(byte[] msg) {
        try {
            // Print normal text


            // outputStream.write(PrinterCommands.SELECT_BIT_IMAGE_MODE);
            outputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String leftRightAlign(String str1, String str2) {
        String ans = str1 +str2;
        if(ans.length() <31){
            int n = (31 - str1.length() + str2.length());
            ans = str1 + new String(new char[n]).replace("\0", " ") + str2;
        }
        return ans;
    }


    private String[] getDateTime() {
        final Calendar c = Calendar.getInstance();
        String dateTime [] = new String[2];
        dateTime[0] = c.get(Calendar.DAY_OF_MONTH) +"/"+ c.get(Calendar.MONTH) +"/"+ c.get(Calendar.YEAR);
        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) +":"+ c.get(Calendar.MINUTE);
        return dateTime;
    }


    //printphoto
    public void printImage(int drawable) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), drawable, options);
        if (bmp == null) {
            Log.e(TAG, "resource decoding is failed");
            return;
        }
        byte[] data = WoosimImage.printBitmap(250, 20, 300, 300, bmp);


        bmp.recycle();

        sendData(WoosimCmd.setPageMode());
        sendData(data);
        sendData(WoosimCmd.PM_setStdMode());
    }



    private void sendData(byte[] data) {

        try {


            outputStream.write(data);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public  boolean isBluetoothConnected() {

        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()
                && mBluetoothAdapter.STATE_CONNECTED==2;
    }



    void printImageVehicle(int drawable,String type)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), drawable, options);
        if (bmp == null) {
            Log.e(TAG, "resource decoding is failed");
            return;
        }
        byte[] data = WoosimImage.printBitmap(250, 20, 200, 200, bmp);


        bmp.recycle();

        sendData(WoosimCmd.setPageMode());
        sendData(data);
        sendData(WoosimCmd.PM_setStdMode());
    }

}

