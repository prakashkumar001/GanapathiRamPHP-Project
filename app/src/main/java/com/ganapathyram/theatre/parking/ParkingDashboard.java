package com.ganapathyram.theatre.parking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.citizen.sdk.ESCPOSConst;
import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.activities.DashBoard;
import com.ganapathyram.theatre.activities.Home;
import com.ganapathyram.theatre.activities.Login;
import com.ganapathyram.theatre.adapter.ParkingAdapter;
import com.ganapathyram.theatre.bluetooth.DeviceListActivity;
import com.ganapathyram.theatre.bluetooth.printer.WoosimCmd;
import com.ganapathyram.theatre.bluetooth.printer.WoosimImage;
import com.ganapathyram.theatre.bluetooth.utils.ESCPOSDriver;
import com.ganapathyram.theatre.bluetooth.utils.PrinterCommands;
import com.ganapathyram.theatre.bluetooth.utils.Utils;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.database.Product;
import com.ganapathyram.theatre.database.UserSession;
import com.ganapathyram.theatre.database.Wifi_BluetoothAddress;
import com.ganapathyram.theatre.model.Parking;
import com.ganapathyram.theatre.utils.TableBuilder;
import com.ganapathyram.theatre.utils.WSUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.ganapathyram.theatre.common.GlobalClass.bluetoothStatus;
import static com.ganapathyram.theatre.helper.Helper.getHelper;

/**
 * Created by Prakash on 9/21/2017.
 */

public class ParkingDashboard extends AppCompatActivity implements Runnable{
    public RecyclerView parkinglist;
    ArrayList<Parking> list;
    ParkingAdapter adapter;
    GlobalClass global;
    double gst_amount;
    String gstvalue;

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
        global=(GlobalClass)getApplicationContext();
        //for bluetooth
        /*if(bluetoothStatus!=null)
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



        }*/


        if(getHelper().getAddress().getBluetoothAddress()!=null)
        {
            bluetoothStatus=getHelper().getAddress().getBluetoothAddress();

                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                mBluetoothDevice = mBluetoothAdapter
                        .getRemoteDevice(bluetoothStatus);
                Thread mBlutoothConnectThread = new Thread(this);
                mBlutoothConnectThread.start();



        }else
        {

            bluetoothStatus=null;

        }




        parkinglist = (RecyclerView) findViewById(R.id.parking);
        list=new ArrayList<>();
        list.add(new Parking("BIKE",R.mipmap.bike_icon));
        list.add(new Parking("CAR",R.mipmap.car_icon));
        list.add(new Parking("AUTO",R.mipmap.auto_icon));
        list.add(new Parking("HEAVY",R.mipmap.truck_icon));
        list.add(new Parking("COMPLEMENTRY",R.mipmap.cycle_icon));


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

    public void printCheck(final Parking parking)
    {
        Thread t = new Thread() {
            public void run() {
                try {
                    ESCPOSDriver escposDriver = new ESCPOSDriver();
                    OutputStream os = mBluetoothSocket
                            .getOutputStream();
                    outputStream=new BufferedOutputStream(os);
                    printImage(R.drawable.printer_logo);

                   /// String dateTime[] = getDateTime().split(" ");
                   // printText(leftRightAlign(dateTime[0], dateTime[1]));
                    // printCustom(dateTime[0]+"   "+dateTime[1],0,1);
                    printCustom("GANAPATHIRAM THEATRE",0,1);
                    printCustom("LB Road, Adyar,Ch-20,Ph: 044 2441 7424",0,1);
                    escposDriver.printLineAlignCenter(outputStream,getDateTime());

                    // printCustom("GST.no : 33AAJFGO516A1Z7",0,1);
                    printNewLine();

          /*          ArrayList<com.ganapathyram.theatre.database.Product> snacks=new ArrayList<>();
                    ArrayList<com.ganapathyram.theatre.database.Product> beverages=new ArrayList<>();
                    for(int k=0;k<global.cartList.size();k++)
                    {

                        com.ganapathyram.theatre.database.Product product=global.cartList.get(k);
                        if(product.categoryUid.equalsIgnoreCase("snacks"))
                        {

                            snacks.add(product);
                        }else if(product.categoryUid.equalsIgnoreCase("beverage"))
                        {
                            beverages.add(product);
                        }
                    }


                    // posPtr.printText( "- Sample Print 1 -\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_2HEIGHT );
                    // posPtr.printText( "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
                    TableBuilder data=new TableBuilder();
                    data.addRow("Qty.","Item","Price","Total");
                    data.addRow("-----", "----", "-----","-----");

                    if(snacks.size()>0)
                    {
                        data.addRow("Snacks");
                        for(int i=0;i<snacks.size();i++)
                        {
                            com.ganapathyram.theatre.database.Product product=snacks.get(i);

                            data.addRow(String.valueOf(product.getQuantity()),product.getProductName(),product.getPrice(),product.getTotalprice());

                        }
                        double sub=subtotal(snacks);
                        gst_amount = (sub * 18) / 100;

                        double gst=gst_amount/2;
                        gstvalue=String.format("%.2f", gst);


                        escposDriver.printLineAlignCenter(outputStream,data.toString());

                        escposDriver.printLineAlignCenter(outputStream,"-----------------------------------"+"\n" );
                        escposDriver.printLineAlignCenter(outputStream,"SUB TOTAL "+sub+gst_amount+"\n" );

                        escposDriver.printLineAlignCenter(outputStream,"CGST "+gst+ "\n" );
                        escposDriver.printLineAlignCenter(outputStream,"SGST "+gst+ "\n" );
                        escposDriver.printLineAlignCenter(outputStream,"------------"+"\n" );
                        outputStream.write(ESCPOSDriver.LINE_FEED);
                    }



                    if(beverages.size()>0)
                    {
                        TableBuilder data2=new TableBuilder();
                        data2.addRow("Beverages");
                        for(int i=0;i<beverages.size();i++)
                        {
                            com.ganapathyram.theatre.database.Product product=beverages.get(i);
                            data2.addRow(String.valueOf(product.getQuantity()),product.getProductName(),product.getPrice(),product.getTotalprice());

                        }
                        double sub=subtotal(beverages);
                        gst_amount = (sub * 20) / 100;

                        double gst=gst_amount/2;
                        gstvalue=String.format("%.2f", gst);


                        escposDriver.printLineAlignCenter(outputStream,data2.toString());

                        escposDriver.printLineAlignCenter(outputStream,"-----------------------------------"+"\n" );
                        escposDriver.printLineAlignCenter(outputStream,"SUB TOTAL "+sub+gst_amount+"\n" );

                        escposDriver.printLineAlignCenter(outputStream,"CGST "+gst+ "\n" );
                        escposDriver.printLineAlignCenter(outputStream,"SGST "+gst+ "\n" );
                        escposDriver.printLineAlignCenter(outputStream,"------------"+"\n" );
                        outputStream.write(ESCPOSDriver.LINE_FEED);


                    }*/





                    printCustom(parking.name+" "+parking.chargesToBePaid,2,1);
                    printCustom(new String(new char[32]).replace("\0", "."),0,1);
                    printNewLine();
                    printCustom("Thank you for coming",0,1);
                    printUnicode();
                    printNewLine();
                    outputStream.write(escposDriver.PAPER_CUT);
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
            Toast.makeText(ParkingDashboard.this, "Bluetooth not connected", 2000).show();
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

                        getParkingStatus(name);
                       // printCheck(R.drawable.bike_bw,name);
                    /*else  if(name.equalsIgnoreCase("CAR PARKING"))
                    {
                        getParkingStatus(name);
                        printCheck(R.drawable.car_bw,name);

                    }else  if(name.equalsIgnoreCase("AUTO PARKING"))
                    {
                        getParkingStatus(name);
                        printCheck(R.drawable.auto_bw,name);
                    }else  if(name.equalsIgnoreCase("HEAVY PARKING"))
                    {
                        printCheck(R.drawable.bus_bw,name);
                    }else  if(name.equalsIgnoreCase("CYCLE PARKING"))
                    {
                        printCheck(R.drawable.bike_bw,name);
                    }*/


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
                    Toast.makeText(ParkingDashboard.this, "Bluetooth Cancelled", 2000).show();
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

            if(getHelper().getAddress()==null)
            {
                Wifi_BluetoothAddress address=new Wifi_BluetoothAddress();
                address.setBluetoothAddress(mBluetoothDevice.getAddress());
                getHelper().getDaoSession().insert(address);

            }else
            {
                Wifi_BluetoothAddress address=new Wifi_BluetoothAddress();
                address.setId(Long.parseLong("1"));
                address.setBluetoothAddress(mBluetoothDevice.getAddress());
                getHelper().getDaoSession().update(address);

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





    //printphoto
    public void printImage(int drawable) {

       /* BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), drawable, options);
        if (bmp == null) {
            Log.e(TAG, "resource decoding is failed");
            return;
        }
        byte[] data = WoosimImage.printBitmap(300, 0, 200, 200, bmp);


        bmp.recycle();

        sendData(WoosimCmd.setPageMode());
        sendData(data);
        sendData(WoosimCmd.PM_setStdMode());*/

        printPhoto(R.drawable.gr_new);

    }
    //print photo
    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    img);
            if(bmp!=null){
                byte[] command = Utils.decodeBitmap(bmp);
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
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
    public void getParkingStatus(final String parkingType) {
        class ParkingServer extends AsyncTask<String, String, String> {
            ProgressDialog dialog;
            String response = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(ParkingDashboard.this);
                dialog.setMessage(getString(R.string.loading));
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(String[] params) {
                try {


                    String requestURL = global.ApiBaseUrl + "parking/confirm";
                    WSUtils utils = new WSUtils();
                    JSONObject user = new JSONObject();
                    user.put("userId", getHelper().getSession().getUserId());
                    if(parkingType.equalsIgnoreCase("CAR"))
                    {
                        user.put("parkingType", "car");

                    }else if(parkingType.equalsIgnoreCase("BIKE"))
                    {
                        user.put("parkingType", "bike");

                    }
                        else  if(parkingType.equalsIgnoreCase("AUTO"))
                    {
                        user.put("parkingType", "auto");

                    }else  if(parkingType.equalsIgnoreCase("HEAVY"))
                    {
                        user.put("parkingType", "hmv");

                    }else  if(parkingType.equalsIgnoreCase("COMPLEMENTRY"))
                    {
                        user.put("parkingType", "complimentary");

                    }
                    user.put("venueId", "gprtheatre");
                    user.put("sessionId",getHelper().getSession().getSessionId());





                    response = utils.responsedetailsfromserver(requestURL, user.toString());
                    System.out.println("SERVER REPLIED:" + response);
                    //{"status":"success","message":"Registration Successful","result":[],"statusCode":200}
                    // {"status":"success","message":"Logged in Successfully","result":{"statusCode":4},"statusCode":200}
                } catch (Exception ex) {
                    Log.i("ERROR", "ERROR" + ex.toString());
                }

                return response;
            }


            @Override
            protected void onPostExecute(String o) {

                Parking parking;

                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

                if (o == null ) {

                }else {

                    try {
                        JSONObject object=new JSONObject(o);
                        JSONObject payload=object.getJSONObject("payload");

                        String startTime=payload.getString("startTime");
                        String chargesToBePaid=payload.getString("chargesToBePaid");
                        String venueId=payload.getString("venueId");
                        parking=new Parking(parkingType,startTime,chargesToBePaid,venueId);

                        printCheck(parking);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            }
        }
        new ParkingServer().execute();
    }

    public double subtotal(List<Product> data)
    {
        double subTotal=0.0;
        for(com.ganapathyram.theatre.database.Product product:data)
        {
            subTotal=subTotal+Double.parseDouble(product.totalprice);
        }

        return subTotal;
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy hh:mm:aa");
        Date date = new Date();
        return dateFormat.format(date);
    }

    void LogoutDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ParkingDashboard.this);

        // set title
        alertDialogBuilder.setTitle("Alert");

        // set dialog message
        alertDialogBuilder
                .setMessage("Are you sure want to exit ?")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity

                        logout();


                        dialog.dismiss();
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    void logout()
    {
        class LogOutServer extends AsyncTask<String, String, String> {
            ProgressDialog dialog;
            String response = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(ParkingDashboard.this);
                dialog.setMessage(getString(R.string.loading));
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(String[] params) {
                try {


                    String requestURL = global.ApiBaseUrl + "user/logout";
                    WSUtils utils = new WSUtils();

                    // JSONObject object = new JSONObject();
                    JSONObject user = new JSONObject();
                    user.put("userId", getHelper().getSession().getUserId());
                    user.put("sessionId", getHelper().getSession().getSessionId());

               /* object.put("user", user);
                object.put("venueId", "gprtheatre");

*/
                    response = utils.responsedetailsfromserver(requestURL, user.toString());

                    System.out.println("SERVER REPLIED:" + response);
                    //{"status":"success","message":"Registration Successful","result":[],"statusCode":200}
                    // {"status":"success","message":"Logged in Successfully","result":{"statusCode":4},"statusCode":200}
                } catch (Exception ex) {
                    Log.i("ERROR", "ERROR" + ex.toString());
                }

                return response;
            }


            @Override
            protected void onPostExecute(String o) {

                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

                if (o == null ) {

                }else {
                    try {
                        JSONObject object = new JSONObject(o);

                        String payload=object.getString("payload");
                        if(payload.equalsIgnoreCase("success"))
                        {
                            getHelper().getDaoSession().deleteAll(UserSession.class);

                            Intent i=new Intent(ParkingDashboard.this,Login.class);
                            startActivity(i);
                            ActivityCompat.finishAffinity(ParkingDashboard.this);

                        }




                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
        }
        new LogOutServer().execute();
    }

}

