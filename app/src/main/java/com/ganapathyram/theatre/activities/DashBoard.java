package com.ganapathyram.theatre.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.citizen.sdk.ESCPOSConst;
import com.citizen.sdk.ESCPOSPrinter;
import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.adapter.CartAdapter;
import com.ganapathyram.theatre.adapter.ProductListAdapter;
import com.ganapathyram.theatre.bluetooth.utils.ESCPOSDriver;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.database.Categories;
import com.ganapathyram.theatre.database.LocalSalesError;
import com.ganapathyram.theatre.database.UserSession;
import com.ganapathyram.theatre.database.Wifi_BluetoothAddress;
import com.ganapathyram.theatre.model.PrintError;
import com.ganapathyram.theatre.model.Product;
import com.ganapathyram.theatre.model.ProductAvailable;
import com.ganapathyram.theatre.utils.InternetPermissions;
import com.ganapathyram.theatre.utils.TableBuilder;
import com.ganapathyram.theatre.utils.WSUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.ganapathyram.theatre.helper.Helper.getHelper;

/**
 * Created by Prakash on 9/19/2017.
 */

public class DashBoard extends AppCompatActivity {
    public static RecyclerView productListView;
    public static ProductListAdapter adapter;
    public static List<com.ganapathyram.theatre.database.Product> productList;
    public static TextView cartcount,totalprice,subtotal;
    RelativeLayout cartRelativeLayout;
    public static  RecyclerView cartview;
    RadioGroup radioGroup;
    int indexpos=-1;
    GlobalClass global;
    double gst_amount;
    String gstvalue;
    String subtotals;
    List<Categories> categoriesList;
    LinearLayout layout;
    CartAdapter cartadapter;
    EditText seatno;
    PrintError printError;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        global=(GlobalClass)getApplicationContext();
        productListView = (RecyclerView) findViewById(R.id.productList);
        cartcount=(TextView)findViewById(R.id.cartcount);
        cartRelativeLayout=(RelativeLayout)findViewById(R.id.cartRelativeLayout);
        radioGroup= (RadioGroup) findViewById(R.id.rg_header);
        layout=(LinearLayout)findViewById(R.id.layout);
        RadioGroup.LayoutParams rprms;


        if(getHelper().getCategoryItems()!=null)
        {
            categoriesList=new ArrayList<>();
            categoriesList=getHelper().getCategoryItems();

            for(int i = 0; i< categoriesList.size(); i++){
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(categoriesList.get(i).categoryName);
                radioButton.setTextSize(16);
                radioButton.setPadding(5,0,0,5);
                radioButton.setChecked(i==0);
                radioButton.setId(i);
                radioButton.setGravity(Gravity.CENTER);
                radioButton.setTextColor(getResources().getColorStateList(R.color.rbtn_textcolor_selector));
                radioButton.setButtonDrawable(null);
                radioButton.setBackgroundResource(R.drawable.radio_selector_circle);
                rprms= new RadioGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
                radioGroup.addView(radioButton, rprms);



            }

            Products(categoriesList.get(0).categoryUid);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                    int id=i;



                        Products(categoriesList.get(id).categoryUid);
                   /* }else
                    {
                        productList=getHelper().getProductItems(categoriesList.get(id).categoryUid);
                        adapter = new ProductListAdapter(getApplicationContext(), productList);
                        final int columns = getResources().getInteger(R.integer.grid_column);

                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),columns);
                        productListView.setLayoutManager(layoutManager);
                        productListView.setItemAnimator(new DefaultItemAnimator());
                        productListView.setAdapter(adapter);
                        productListView.setNestedScrollingEnabled(false);
                        adapter.notifyDataSetChanged();

                    }
*/

                }
            });
        }






        cartRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent i=new Intent(DashBoard.this,CartPage.class);
                startActivity(i);*/
                showOrderDialog();

            }
        });






        if(global.cartList.size()>0)
        {
            cartcount.setText(String.valueOf(global.cartList.size()));
        }





    }

    public void showOrderDialog() {

        // custom dialog
        final Dialog dialog = new Dialog(DashBoard.this, R.style.ThemeDialogCustom);


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.cartpage);
        dialog.getWindow().setGravity(Gravity.CENTER);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        String rupee = getResources().getString(R.string.Rupee_symbol);

        dialog.show();
        //dialog.getWindow().setLayout((8 * width) / 10, (8 * height) / 10);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

         cartview=(RecyclerView)dialog.findViewById(R.id.cartlist) ;
         totalprice=(TextView) dialog.findViewById(R.id.total_price) ;
        seatno=(EditText) dialog.findViewById(R.id.seatno) ;
        subtotal=(TextView) dialog.findViewById(R.id.sub_total) ;
        seatno.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        seatno.setText("");
        ImageView iv_close=(ImageView)dialog.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //adapter = new ProductListAdapter(getApplicationContext(), productList);
               // productListView.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
                dialog.dismiss();
                adapter.notifyDataSetChanged();
            }
        });

        ImageView payment=(ImageView)dialog.findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //wifi();

                Checkout(dialog,seatno.getText().toString());


            }
        });
        final int columns = getResources().getInteger(R.integer.grid_column);

         cartadapter=new CartAdapter(DashBoard.this,dialog);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        cartview.setLayoutManager(layoutManager);
        cartview.setItemAnimator(new DefaultItemAnimator());
        cartview.setNestedScrollingEnabled(false);
        cartview.setAdapter(cartadapter);

        subtotal.setText(rupee+" "+String.format("%.2f",totalvalue()));
        subtotals=String.format("%.2f",totalvalue());

        // gst_amount = ( adapter.totalvalue() * 18 ) / 100;

        double total=totalvalue()+totalTaxAmount();
        double roundofftotal=Math.round(total);
        totalprice.setText(String.format("%.2f",roundofftotal));



        dialog.show();



}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(DashBoard.this, Home.class);
        startActivity(i);
        finish();
    }

    ProductAvailable containsProduct(List<Product> list, String productid) {
        for (Product item : list) {
            if (item.productid.equals(productid)) {

                indexpos=list.indexOf(item);
                return new ProductAvailable(true,indexpos);

            }
        }

        return new ProductAvailable(false,-1);
    }
    public void usbPrinter(PrintError printError)
    {



        String  address=getHelper().getAddress().getWifiAddress();


        // Constructor
        ESCPOSPrinter posPtr = new ESCPOSPrinter();

        // Set context
        posPtr.setContext( DashBoard.this );

        // Get Address
       // UsbDevice usbDevice = null;												// null (Automatic detection)
        //
        // Connect
        int result = posPtr.connect( ESCPOSConst.CMP_PORT_WiFi, address );		// Android 3.1 ( API Level 12 ) or later
        if ( ESCPOSConst.CMP_SUCCESS == result )
        {
            Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.printer_logo);
            // Character set
            posPtr.setEncoding( "ISO-8859-1" );		// Latin-1
            //posPtr.setEncoding( "Shift_JIS" );	// Japanese 日本語を印字する場合は、この行を有効にしてください.

            // Start Transaction ( Batch )
            posPtr.transactionPrint( ESCPOSConst.CMP_TP_TRANSACTION );
            /*ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);
            byte[] b = byteArrayBitmapStream.toByteArray();
            posPtr.printBitmap(b,150,150,);*/

           // posPtr.printBitmap(bitmap,100,200);
            posPtr.printBitmap(bitmap,
                    150,
                    ESCPOSConst.CMP_ALIGNMENT_CENTER);

          /*  double gst=gst_amount/2;
             gstvalue=String.format("%.2f", gst);
*/

            // Print Text 33AAJFGO516A1Z7
            posPtr.printText( "Ganapathy Ram Theatre" + "\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
            posPtr.printText( "101, LB Road, Adyar Chennai, 600020" + "\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
            posPtr.printText( "Phone: 044 2441 7424" + "\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

            posPtr.printText( "GST.no : 33AAJFGO516A1Z7" + "\n", ESCPOSConst.CMP_ALIGNMENT_LEFT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
            posPtr.printText( "Bill.no: "+printError.orderId + "\n", ESCPOSConst.CMP_ALIGNMENT_LEFT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
            posPtr.printText( leftRightAlign("Name  :"+getHelper().getLogin().getUserName(),getDateTime()+"\n") , ESCPOSConst.CMP_ALIGNMENT_LEFT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

            if(!printError.seatno.equalsIgnoreCase(""))
            {
                posPtr.printText( "Seat No: "+printError.seatno + "\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_BOLD, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

            }

            ArrayList<com.ganapathyram.theatre.database.Product> snacks=new ArrayList<>();
            ArrayList<com.ganapathyram.theatre.database.Product> beverages=new ArrayList<>();
            ArrayList<com.ganapathyram.theatre.database.Product> water=new ArrayList<>();
            for(int k=0;k<global.cartList.size();k++)
            {

                com.ganapathyram.theatre.database.Product product=global.cartList.get(k);
                if(product.categoryUid.equalsIgnoreCase("snacks") || product.categoryUid.equalsIgnoreCase("combo"))
                {

                    snacks.add(product);
                }else if(product.categoryUid.equalsIgnoreCase("beverage"))
                {
                    beverages.add(product);
                }else if(product.categoryUid.equalsIgnoreCase("water"))
                {
                    water.add(product);
                }
            }


            // posPtr.printText( "- Sample Print 1 -\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_2HEIGHT );
            // posPtr.printText( "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

            if(snacks.size()>0)
            {
                TableBuilder data=new TableBuilder();
                data.addRow("Snacks");
                data.addRow("S.no.","Item","Qty.","Price","Total");
                data.addRow("-----", "----", "-----","-----","-----");

                for(int i=0;i<snacks.size();i++)
                {
                    com.ganapathyram.theatre.database.Product product=snacks.get(i);

                    data.addRow(String.valueOf(i+1),product.getProductName(),String.valueOf(product.getQuantity()),product.getPrice(),product.getTotalprice());
                }
                double sub=subtotal(snacks);
                double tax=taxsubTotal(snacks);
                String subtotal=String.valueOf(sub+tax);

               double gst_amount = (sub * Double.parseDouble(snacks.get(0).getTaxPercent())) / 100;

                String gstpercent=String.format("%.2f",Double.parseDouble(snacks.get(0).getTaxPercent())/2);

                double gst=gst_amount/2;
                String gstvalue=String.format("%.2f", gst);


                posPtr.printText(data.toString(),ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

                posPtr.printText("-----------------------------------"+"\n",ESCPOSConst.CMP_ALIGNMENT_CENTER,ESCPOSConst.CMP_FNT_DEFAULT,ESCPOSConst.CMP_TXT_1WIDTH| ESCPOSConst.CMP_TXT_1HEIGHT );

                posPtr.printText("CGST "+gstpercent+"% "+gstvalue+ "\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
                posPtr.printText("SGST "+gstpercent+"% "+gstvalue+ "\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
               
				posPtr.printText("TOTAL "+String.format("%.2f",Double.parseDouble(subtotal))+"\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

				
				posPtr.printText("------------"+"\n",ESCPOSConst.CMP_ALIGNMENT_CENTER,ESCPOSConst.CMP_FNT_DEFAULT,ESCPOSConst.CMP_TXT_1WIDTH| ESCPOSConst.CMP_TXT_1HEIGHT );
                posPtr.writeData(ESCPOSDriver.LINE_FEED);
            }



            if(beverages.size()>0)
            {
                TableBuilder data2=new TableBuilder();
                data2.addRow("Beverages");
                data2.addRow("S.no.","Item","Qty.","Price","Total");
                data2.addRow("-----", "----", "-----","-----","-----");

                for(int i=0;i<beverages.size();i++)
                {
                    com.ganapathyram.theatre.database.Product product=beverages.get(i);
                    data2.addRow(String.valueOf(i+1),product.getProductName(),String.valueOf(product.getQuantity()),product.getPrice(),product.getTotalprice());

                }
                double sub=subtotal(beverages);

                double taxs=taxsubTotal(beverages);
                String subtotal2=String.valueOf(sub+taxs);

               // String subtotal2=String.valueOf(sub);
                double gst_amount = (sub * Double.parseDouble(beverages.get(0).getTaxPercent())) / 100;

                double gst=gst_amount/2;
               String gstvalue2=String.format("%.2f", gst);

                String gstpercent=String.format("%.2f",Double.parseDouble(beverages.get(0).getTaxPercent())/2);
                posPtr.printText(data2.toString(),ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

                posPtr.printText("-----------------------------------"+"\n",ESCPOSConst.CMP_ALIGNMENT_CENTER,ESCPOSConst.CMP_FNT_DEFAULT,ESCPOSConst.CMP_TXT_1WIDTH| ESCPOSConst.CMP_TXT_1HEIGHT );

                posPtr.printText("CGST "+gstpercent+"% "+gstvalue2+ "\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
                posPtr.printText("SGST "+gstpercent+"% "+gstvalue2+ "\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
                posPtr.printText("TOTAL "+String.format("%.2f",Double.parseDouble(subtotal2))+"\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

				
				posPtr.printText("------------"+"\n",ESCPOSConst.CMP_ALIGNMENT_CENTER,ESCPOSConst.CMP_FNT_DEFAULT,ESCPOSConst.CMP_TXT_1WIDTH| ESCPOSConst.CMP_TXT_1HEIGHT );
                posPtr.writeData(ESCPOSDriver.LINE_FEED);
            }




            if(water.size()>0)
            {
                TableBuilder data3=new TableBuilder();
                data3.addRow("Water");
                data3.addRow("S.no.","Item","Qty.","Price","Total");
                data3.addRow("-----", "----", "-----","-----","-----");

                for(int i=0;i<water.size();i++)
                {
                    com.ganapathyram.theatre.database.Product product=water.get(i);
                    data3.addRow(String.valueOf(i+1),product.getProductName(),String.valueOf(product.getQuantity()),product.getPrice(),product.getTotalprice());

                }
                double sub=subtotal(water);

                double taxs=taxsubTotal(water);
                String subtotal2=String.valueOf(sub+taxs);

                // String subtotal2=String.valueOf(sub);
                double gst_amount = (sub * Double.parseDouble(water.get(0).getTaxPercent())) / 100;

                double gst=gst_amount/2;
                String gstvalue2=String.format("%.2f", gst);

                String gstpercent=String.format("%.2f",Double.parseDouble(water.get(0).getTaxPercent())/2);
                posPtr.printText(data3.toString(),ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

                posPtr.printText("-----------------------------------"+"\n",ESCPOSConst.CMP_ALIGNMENT_CENTER,ESCPOSConst.CMP_FNT_DEFAULT,ESCPOSConst.CMP_TXT_1WIDTH| ESCPOSConst.CMP_TXT_1HEIGHT );

                posPtr.printText("CGST "+gstpercent+"% "+gstvalue2+ "\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
                posPtr.printText("SGST "+gstpercent+"% "+gstvalue2+ "\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
                posPtr.printText("TOTAL "+String.format("%.2f",Double.parseDouble(subtotal2))+"\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );


                posPtr.printText("------------"+"\n",ESCPOSConst.CMP_ALIGNMENT_CENTER,ESCPOSConst.CMP_FNT_DEFAULT,ESCPOSConst.CMP_TXT_1WIDTH| ESCPOSConst.CMP_TXT_1HEIGHT );
                posPtr.writeData(ESCPOSDriver.LINE_FEED);
            }












            double grand=totalvalue()+totalTaxAmount();
            double grTotal=Math.round(grand);

            posPtr.printText("GRAND TOTAL "+String.format("%.2f",grTotal)+"\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );




            posPtr.printText("Thank you for coming"+"\n",ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
            // Partial Cut with Pre-Feed
            posPtr.cutPaper( ESCPOSConst.CMP_CUT_PARTIAL_PREFEED );

            // End Transaction ( Batch )
            result = posPtr.transactionPrint( ESCPOSConst.CMP_TP_NORMAL );




            if(!printError.seatno.equalsIgnoreCase(""))
            {
                printError.result=result;
                printError.seatno=seatno.getText().toString();
                printError.escposPrinter=posPtr;
            customerCopy(printError);
            }else
            {
                // Disconnect
                posPtr.disconnect();

                printError.progressdialog.dismiss();

                global.cartList.clear();


                printError.orderdialog.dismiss();
                cartcount.setText("0");

                Intent i=new Intent(DashBoard.this,DashBoard.class);
                overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                startActivity(i);
                finish();
            }





            if ( ESCPOSConst.CMP_SUCCESS != result )
            {
                // Transaction Error
                Toast.makeText( DashBoard.this, "Transaction Error : " + Integer.toString( result ), Toast.LENGTH_LONG ).show();
            }
        }
        else
        {

            reprintDialog(printError);
           // printError.progressdialog.dismiss();
           // printError.orderdialog.dismiss();
            // Connect Error
           // Toast.makeText( DashBoard.this, "Connect or Printer Error : " + Integer.toString( result ), Toast.LENGTH_LONG ).show();

            copydatabasetosd();
        }






    }



    public void Products(final String CateroryId) {
        class ProductFromServer extends AsyncTask<String, String, String> {
            ProgressDialog dialog;
            String response = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(DashBoard.this);
                dialog.setMessage(getString(R.string.loading));
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(String[] params) {
                try {


                    //String requestURL = global.deFaultBaseUrl+global.ApiBaseUrl + "product/details/"+CateroryId+"/"+getHelper().getSession().getUserId();
                    String requestURL = global.deFaultBaseUrl+global.ApiBaseUrl + "product_list.php?id="+CateroryId;

                    Log.e("URL","URL"+requestURL);
                    WSUtils utils = new WSUtils();



                    response = utils.getResultFromHttpRequest(requestURL,"GET", new HashMap<String, String>());

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
                productList=new ArrayList<>();
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

                if (o == null ) {

                }else {

                    try {
                        JSONObject object=new JSONObject(o);
                        JSONArray array=object.getJSONArray("payload");
                        for(int i=0;i<array.length();i++)
                        {
                            JSONObject data=array.getJSONObject(i);
                            String productId=data.getString("productId");
                            String productUid=data.getString("productUid");
                            String productName=data.getString("productName");
                            String categoryUid=data.getString("categoryUid");
                            String price=data.getString("price");
                            String description=data.getString("description");
                            String taxPercent=data.getString("taxPercent");
                            String active=data.getString("active");
                            String imageName=data.getString("imageName");

                            double gst_amount = (Double.parseDouble(price) * Double.parseDouble(taxPercent)) / 100;


                            com.ganapathyram.theatre.database.Product product=new com.ganapathyram.theatre.database.Product();
                            product.productId=Long.parseLong(productId);
                            product.productUid=productUid;
                            product.productName=productName;
                            product.categoryUid=categoryUid;
                            product.price=price;
                            product.quantity=1;
                            product.totalprice=price;
                            product.description=description;
                            product.taxPercent=taxPercent;
                            product.productimage=imageName;
                            product.taxAmount=String.format("%.2f", gst_amount);
                            product.active=active;

                            productList.add(product);
                            getHelper().getDaoSession().insertOrReplace(product);

                        }








                        adapter = new ProductListAdapter(getApplicationContext(), productList);
                        final int columns = getResources().getInteger(R.integer.grid_column);

                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),columns);
                        productListView.setLayoutManager(layoutManager);
                        productListView.setItemAnimator(new DefaultItemAnimator());
                        productListView.setAdapter(adapter);
                        productListView.setNestedScrollingEnabled(false);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }
        }
        new ProductFromServer().execute();
    }

    public double subtotal(List<com.ganapathyram.theatre.database.Product> data)
    {
        double subTotal=0.0;
        double total=0.0;
        for(com.ganapathyram.theatre.database.Product product:data)
        {
            subTotal=subTotal+Double.parseDouble(product.getTotalprice());
            //total=total+subTotal+Double.parseDouble(product.getTaxAmount());
        }

        return subTotal;
    }

    public double taxsubTotal(List<com.ganapathyram.theatre.database.Product> data)
    {
        double taxsubTotal=0.0;
        for(com.ganapathyram.theatre.database.Product product:data)
        {
            taxsubTotal=taxsubTotal+Double.parseDouble(product.getTaxAmount());
            //total=total+subTotal+Double.parseDouble(product.getTaxAmount());
        }

        return taxsubTotal;
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*if(!new InternetPermissions(DashBoard.this).isInternetOn())
        {
            Snackbar.make(layout, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            }).show();

        }*/
    }

    public void Checkout(final Dialog orderdialog,final String seatno) {
        class CheckOutService extends AsyncTask<String, String, String> {
            ProgressDialog dialog;
            String response = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(DashBoard.this);
                dialog.setMessage(getString(R.string.loading));
                dialog.setCancelable(false);
                dialog.show();


            }

            @Override
            protected String doInBackground(String[] params) {
                try {


                    String requestURL = global.deFaultBaseUrl+global.ApiBaseUrl + "checkout.php";
                    WSUtils utils = new WSUtils();
                    Log.e("URLCheckout","URLCheckout"+requestURL);

                    JSONObject object;

                    double totaltaxamount=totalTaxAmount();
                            double orderamount=totalvalue();
                    double totalamount=orderamount+totaltaxamount;
                    JSONArray cartList=new JSONArray();
                    for(int i=0;i<global.cartList.size();i++)
                    {
                        com.ganapathyram.theatre.database.Product product=global.cartList.get(i);
                        try {
                            object=new JSONObject();
                            object.put("type",product.categoryUid);
                            object.put("productUid",product.productUid);
                            object.put("quantity",product.quantity);
                            object.put("unitPrice",Double.parseDouble(product.price));
                            object.put("taxPercent",Double.parseDouble(product.taxPercent));
                            object.put("taxAmt",Double.parseDouble(product.taxAmount));
                            object.put("totalAmt",Double.parseDouble(product.totalprice));

                            cartList.put(object);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    JSONArray arraydetails=new JSONArray();

                    JSONObject result=new JSONObject();
                    result.put("userId",getHelper().getSession().getUserId());
                    result.put("sessionId",getHelper().getSession().getSessionId());
                    result.put("cartItems",cartList);
                    result.put("totalTaxAmt",totaltaxamount);
                    result.put("orderAmt",orderamount);
                    result.put("seatNo",seatno);
                    result.put("totalCartAmt",Math.round(totalamount));
                    result.put("venueUid","gprtheatre");
                    result.put("classId",getHelper().getAddress().getSnack_floor());

                    arraydetails.put(result);


                    Log.e("Request","Request"+arraydetails.toString());

                    //usbPrinter();




                    response = utils.responsedetailsfromserver(requestURL, arraydetails.toString());

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


                    if (o == null ) {

                    }else {

                    try {
                        JSONObject object=new JSONObject(o);
                        JSONArray result=object.getJSONArray("payload");
                        JSONObject object1=result.getJSONObject(0);
                        String orderStatus=object1.getString("orderStatus");
                        String orderUId=object1.getString("orderUId");

                        if(orderStatus.equalsIgnoreCase("COMPLETED") ) {
                            printError=new PrintError(dialog,orderdialog,orderUId,seatno);
                            usbPrinter(printError);
                        }
                    } catch (JSONException e) {
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(o);
                            String payload=jsonObject.getString("payload");
                            if(payload.equalsIgnoreCase("null") || payload==null)
                            {
                                JSONObject object=jsonObject.getJSONObject("tags");
                                JSONArray array=object.getJSONArray("shortageDetails");
                                String lowstock="";
                                for(int k=0;k<array.length();k++)
                                {
                                    JSONObject ob=array.getJSONObject(k);
                                    String name=ob.getString("productUid");

                                    lowstock=lowstock+name+",";

                                }


                                lowStockDialog(lowstock);


                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        e.printStackTrace();
                    }


                }


            }
        }
        new CheckOutService().execute();
    }
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy hh:mm:aa");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String leftRightAlign(String str1, String str2) {
        String ans = str1 +str2;
        if(ans.length() <50){
            int n = (50 - str1.length() + str2.length());
            ans = str1 + new String(new char[n]).replace("\0", " ") + str2;
        }
        return ans;
    }

    public double totalvalue()
    {
        double totalValue=0.0;
        for(int i=0;i<global.cartList.size();i++)
        {
            String price=global.cartList.get(i).getPrice();

            double value= Double.parseDouble(price) * global.cartList.get(i).getQuantity();
            totalValue=totalValue + value;

        }

        return totalValue;
    }

    public double totalTaxAmount()
    {
        double totalValue=0.0;
        for(int i=0;i<global.cartList.size();i++)
        {
            String price=global.cartList.get(i).getTaxAmount();

            double value= Double.parseDouble(price);
            totalValue=totalValue + value;

        }

        return totalValue;
    }


    void copydatabasetosd()
    {
        try {
            File sd = Environment.getExternalStorageDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/data/" + getPackageName() + "/databases/grcinemas_db";
                String backupDBPath = "grcinemas_db.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }







       public void lowStockDialog(String message)
       {
           String msg=removeLastChar(message);
           AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                   DashBoard.this);

           // set title
           alertDialogBuilder.setTitle("Your have Low Stock");

           // set dialog message
           alertDialogBuilder
                   .setMessage("Please decrease the products of "+msg)
                   .setCancelable(false)
                   .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog,int id) {
                           // if this button is clicked, close
                           // current activity
                           dialog.dismiss();
                       }
                   });


           // create alert dialog
           AlertDialog alertDialog = alertDialogBuilder.create();

           // show it
           alertDialog.show();

       }

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }


    void reprintDialog(final PrintError printError) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                DashBoard.this);

        // set title
        alertDialogBuilder.setTitle("Alert");

        // set dialog message
        alertDialogBuilder
                .setMessage("Are you sure want to Reprint Receipt ?")
                .setCancelable(false)
                .setPositiveButton("Reprint", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.dismiss();
                        usbPrinter(printError);



                    }
                })

        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                global.cartList.clear();


                cartcount.setText("0");

                printError.progressdialog.dismiss();
                printError.orderdialog.dismiss();
                dialogInterface.dismiss();
                Intent intent=new Intent(DashBoard.this,DashBoard.class);
                overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                startActivity(intent);
                finish();

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
            dialog = new ProgressDialog(DashBoard.this);
            dialog.setMessage(getString(R.string.loading));
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String[] params) {
            try {


                String requestURL = global.deFaultBaseUrl+global.ApiBaseUrl + "user/logout";
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

                        Intent i=new Intent(DashBoard.this,Login.class);
                        startActivity(i);
                        ActivityCompat.finishAffinity(DashBoard.this);

                    }




                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }
    }
        new LogOutServer().execute();
}


    public void customerCopy(PrintError printError)
    {




            Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.printer_logo);
            // Character set
        printError.escposPrinter.setEncoding( "ISO-8859-1" );		// Latin-1

        printError.escposPrinter.transactionPrint( ESCPOSConst.CMP_TP_TRANSACTION );

        printError.escposPrinter.printBitmap(bitmap,
                    150,
                    ESCPOSConst.CMP_ALIGNMENT_CENTER);

        printError.escposPrinter.printText( "Customer Bill" + "\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

        printError.escposPrinter.printText( "Ganapathy Ram Theatre" + "\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
        printError.escposPrinter.printText( "101, LB Road, Adyar Chennai, 600020" + "\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
        printError.escposPrinter.printText( "Phone: 044 2441 7424" + "\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

        printError.escposPrinter.printText( "GST.no : 33AAJFGO516A1Z7" + "\n", ESCPOSConst.CMP_ALIGNMENT_LEFT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
        printError.escposPrinter.printText( "Bill.no: "+printError.orderId + "\n", ESCPOSConst.CMP_ALIGNMENT_LEFT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
        printError.escposPrinter.printText( leftRightAlign("Name  :"+getHelper().getLogin().getUserName(),getDateTime()+"\n") , ESCPOSConst.CMP_ALIGNMENT_LEFT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

        if(!printError.seatno.equalsIgnoreCase(""))
        {
            printError.escposPrinter.printText( "Seat No: "+printError.seatno + "\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_BOLD, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

        }
        ArrayList<com.ganapathyram.theatre.database.Product> snacks=new ArrayList<>();
            ArrayList<com.ganapathyram.theatre.database.Product> beverages=new ArrayList<>();
            ArrayList<com.ganapathyram.theatre.database.Product> water=new ArrayList<>();
            for(int k=0;k<global.cartList.size();k++)
            {

                com.ganapathyram.theatre.database.Product product=global.cartList.get(k);
                if(product.categoryUid.equalsIgnoreCase("snacks") || product.categoryUid.equalsIgnoreCase("combo"))
                {

                    snacks.add(product);
                }else if(product.categoryUid.equalsIgnoreCase("beverage"))
                {
                    beverages.add(product);
                }else if(product.categoryUid.equalsIgnoreCase("water"))
                {
                    water.add(product);
                }
            }



            if(snacks.size()>0)
            {
                TableBuilder data=new TableBuilder();
                data.addRow("Snacks");
                data.addRow("S.no.","Item","Qty.","Price","Total");
                data.addRow("-----", "----", "-----","-----","-----");

                for(int i=0;i<snacks.size();i++)
                {
                    com.ganapathyram.theatre.database.Product product=snacks.get(i);

                    data.addRow(String.valueOf(i+1),product.getProductName(),String.valueOf(product.getQuantity()),product.getPrice(),product.getTotalprice());
                }
                double sub=subtotal(snacks);
                double tax=taxsubTotal(snacks);
                String subtotal=String.valueOf(sub+tax);

                double gst_amount = (sub * Double.parseDouble(snacks.get(0).getTaxPercent())) / 100;

                String gstpercent=String.format("%.2f",Double.parseDouble(snacks.get(0).getTaxPercent())/2);

                double gst=gst_amount/2;
                String gstvalue=String.format("%.2f", gst);


                printError.escposPrinter.printText(data.toString(),ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

                printError.escposPrinter.printText("-----------------------------------"+"\n",ESCPOSConst.CMP_ALIGNMENT_CENTER,ESCPOSConst.CMP_FNT_DEFAULT,ESCPOSConst.CMP_TXT_1WIDTH| ESCPOSConst.CMP_TXT_1HEIGHT );

                printError.escposPrinter.printText("CGST "+gstpercent+"% "+gstvalue+ "\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
                printError.escposPrinter.printText("SGST "+gstpercent+"% "+gstvalue+ "\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

                printError.escposPrinter.printText("TOTAL "+String.format("%.2f",Double.parseDouble(subtotal))+"\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );


                printError.escposPrinter.printText("------------"+"\n",ESCPOSConst.CMP_ALIGNMENT_CENTER,ESCPOSConst.CMP_FNT_DEFAULT,ESCPOSConst.CMP_TXT_1WIDTH| ESCPOSConst.CMP_TXT_1HEIGHT );
                printError.escposPrinter.writeData(ESCPOSDriver.LINE_FEED);
            }



            if(beverages.size()>0)
            {
                TableBuilder data2=new TableBuilder();
                data2.addRow("Beverages");
                data2.addRow("S.no.","Item","Qty.","Price","Total");
                data2.addRow("-----", "----", "-----","-----","-----");

                for(int i=0;i<beverages.size();i++)
                {
                    com.ganapathyram.theatre.database.Product product=beverages.get(i);
                    data2.addRow(String.valueOf(i+1),product.getProductName(),String.valueOf(product.getQuantity()),product.getPrice(),product.getTotalprice());

                }
                double sub=subtotal(beverages);

                double taxs=taxsubTotal(beverages);
                String subtotal2=String.valueOf(sub+taxs);

                // String subtotal2=String.valueOf(sub);
                double gst_amount = (sub * Double.parseDouble(beverages.get(0).getTaxPercent())) / 100;

                double gst=gst_amount/2;
                String gstvalue2=String.format("%.2f", gst);

                String gstpercent=String.format("%.2f",Double.parseDouble(beverages.get(0).getTaxPercent())/2);
                printError.escposPrinter.printText(data2.toString(),ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

                printError.escposPrinter.printText("-----------------------------------"+"\n",ESCPOSConst.CMP_ALIGNMENT_CENTER,ESCPOSConst.CMP_FNT_DEFAULT,ESCPOSConst.CMP_TXT_1WIDTH| ESCPOSConst.CMP_TXT_1HEIGHT );

                printError.escposPrinter.printText("CGST "+gstpercent+"% "+gstvalue2+ "\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
                printError.escposPrinter.printText("SGST "+gstpercent+"% "+gstvalue2+ "\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
                printError.escposPrinter.printText("TOTAL "+String.format("%.2f",Double.parseDouble(subtotal2))+"\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );


                printError.escposPrinter.printText("------------"+"\n",ESCPOSConst.CMP_ALIGNMENT_CENTER,ESCPOSConst.CMP_FNT_DEFAULT,ESCPOSConst.CMP_TXT_1WIDTH| ESCPOSConst.CMP_TXT_1HEIGHT );
                printError.escposPrinter.writeData(ESCPOSDriver.LINE_FEED);
            }




            if(water.size()>0)
            {
                TableBuilder data3=new TableBuilder();
                data3.addRow("Water");
                data3.addRow("S.no.","Item","Qty.","Price","Total");
                data3.addRow("-----", "----", "-----","-----","-----");

                for(int i=0;i<water.size();i++)
                {
                    com.ganapathyram.theatre.database.Product product=water.get(i);
                    data3.addRow(String.valueOf(i+1),product.getProductName(),String.valueOf(product.getQuantity()),product.getPrice(),product.getTotalprice());

                }
                double sub=subtotal(water);

                double taxs=taxsubTotal(water);
                String subtotal2=String.valueOf(sub+taxs);

                // String subtotal2=String.valueOf(sub);
                double gst_amount = (sub * Double.parseDouble(water.get(0).getTaxPercent())) / 100;

                double gst=gst_amount/2;
                String gstvalue2=String.format("%.2f", gst);

                String gstpercent=String.format("%.2f",Double.parseDouble(water.get(0).getTaxPercent())/2);
                printError.escposPrinter.printText(data3.toString(),ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

                printError.escposPrinter.printText("-----------------------------------"+"\n",ESCPOSConst.CMP_ALIGNMENT_CENTER,ESCPOSConst.CMP_FNT_DEFAULT,ESCPOSConst.CMP_TXT_1WIDTH| ESCPOSConst.CMP_TXT_1HEIGHT );

                printError.escposPrinter.printText("CGST "+gstpercent+"% "+gstvalue2+ "\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
                printError.escposPrinter.printText("SGST "+gstpercent+"% "+gstvalue2+ "\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
                printError.escposPrinter.printText("TOTAL "+String.format("%.2f",Double.parseDouble(subtotal2))+"\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );


                printError.escposPrinter.printText("------------"+"\n",ESCPOSConst.CMP_ALIGNMENT_CENTER,ESCPOSConst.CMP_FNT_DEFAULT,ESCPOSConst.CMP_TXT_1WIDTH| ESCPOSConst.CMP_TXT_1HEIGHT );
                printError.escposPrinter.writeData(ESCPOSDriver.LINE_FEED);
            }




            double grand=totalvalue()+totalTaxAmount();
            double grTotal=Math.round(grand);

        printError.escposPrinter.printText("GRAND TOTAL "+String.format("%.2f",grTotal)+"\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );




        printError.escposPrinter.printText("Thank you for coming"+"\n",ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
            // Partial Cut with Pre-Feed
        printError.escposPrinter.cutPaper( ESCPOSConst.CMP_CUT_PARTIAL_PREFEED );

            // End Transaction ( Batch )
            printError.result = printError.escposPrinter.transactionPrint( ESCPOSConst.CMP_TP_NORMAL );

            // Disconnect
        printError.escposPrinter.disconnect();

        printError.progressdialog.dismiss();


            if ( ESCPOSConst.CMP_SUCCESS != printError.result )
            {
                // Transaction Error
              //  Toast.makeText( DashBoard.this, "Transaction Error : " + Integer.toString( printError.result ), Toast.LENGTH_LONG ).show();

                reprintDialog(printError);
            }






        global.cartList.clear();


        printError.orderdialog.dismiss();
        cartcount.setText("0");

        Intent i=new Intent(DashBoard.this,DashBoard.class);
        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
        startActivity(i);
        finish();


    }




}


