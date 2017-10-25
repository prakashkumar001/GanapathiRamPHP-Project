package com.ganapathyram.theatre.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ganapathyram.theatre.bluetooth.printer.WoosimImage;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.model.Product;
import com.ganapathyram.theatre.model.ProductAvailable;
import com.ganapathyram.theatre.utils.TableBuilder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prakash on 9/19/2017.
 */

public class DashBoard extends AppCompatActivity {
    public static RecyclerView productListView;
    public static ProductListAdapter adapter;
    public static ArrayList<Product> productList;
    public static TextView cartcount,totalprice,subtotal;
    RelativeLayout cartRelativeLayout;
    public static  RecyclerView cartview;
    RadioGroup radioGroup;
    int indexpos=-1;
    GlobalClass global;
    double gst_amount;
    String gstvalue;
    String subtotals;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        global=new GlobalClass();
        productListView = (RecyclerView) findViewById(R.id.productList);
        cartcount=(TextView)findViewById(R.id.cartcount);
        cartRelativeLayout=(RelativeLayout)findViewById(R.id.cartRelativeLayout);
        radioGroup= (RadioGroup) findViewById(R.id.rg_header);

        RadioGroup.LayoutParams rprms;

        for(int i=0;i<global.categoryList.size();i++){
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(global.categoryList.get(i).categoryName);
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

        cartRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent i=new Intent(DashBoard.this,CartPage.class);
                startActivity(i);*/
                showOrderDialog();

            }
        });



        productList=new ArrayList<>();

        productList.add(new Product("1","Pop Corn","45.00",R.mipmap.placeholder,1,"45.00",false));
        productList.add(new Product("2","Veg Puff","15.00",R.mipmap.veg_puff,1,"15.00",false));
        productList.add(new Product("3","Chicken Puff","25.00",R.mipmap.chicken_samosa,1,"25.00",false));
        productList.add(new Product("4","Chicken Samosa","30.00",R.mipmap.veg_puff,1,"30.00",false));
        productList.add(new Product("5","Egg Puff","26.00",R.mipmap.egg_puff,1,"26.00",false));
        productList.add(new Product("6","Pop Corn","45.00",R.mipmap.placeholder,1,"45.00",false));
        productList.add(new Product("7","Veg Puff","15.00",R.mipmap.veg_puff,1,"15.00",false));
        productList.add(new Product("8","Chicken Puff","25.00",R.mipmap.chicken_samosa,1,"25.00",false));
        productList.add(new Product("9","Chicken Samosa","30.00",R.mipmap.veg_puff,1,"30.00",false));
        productList.add(new Product("10","Egg Puff","26.00",R.mipmap.egg_puff,1,"26.00",false));




        adapter = new ProductListAdapter(getApplicationContext(), productList);
        final int columns = getResources().getInteger(R.integer.grid_column);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),columns);
        productListView.setLayoutManager(layoutManager);
        productListView.setItemAnimator(new DefaultItemAnimator());
        productListView.setAdapter(adapter);
        productListView.setNestedScrollingEnabled(false);
        adapter.notifyDataSetChanged();

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

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.show();
        dialog.getWindow().setLayout((8 * width) / 10, (8 * height) / 10);

         cartview=(RecyclerView)dialog.findViewById(R.id.cartlist) ;
         totalprice=(TextView) dialog.findViewById(R.id.total_price) ;
        subtotal=(TextView) dialog.findViewById(R.id.sub_total) ;
        ImageView iv_close=(ImageView)dialog.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //adapter = new ProductListAdapter(getApplicationContext(), productList);
               // productListView.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        ImageView payment=(ImageView)dialog.findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"Payment Successfull",Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                usbPrinter();
                //finish();
                global.cartList.clear();
                adapter=new ProductListAdapter(DashBoard.this,productList);
                productListView.setAdapter(adapter);
                cartcount.setText("0");
            }
        });
        final int columns = getResources().getInteger(R.integer.grid_column);

        CartAdapter adapter=new CartAdapter(DashBoard.this,dialog);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        cartview.setLayoutManager(layoutManager);
        cartview.setItemAnimator(new DefaultItemAnimator());
        cartview.setAdapter(adapter);
        cartview.setNestedScrollingEnabled(false);
        cartview.setAdapter(adapter);

        subtotal.setText(rupee+" "+String.valueOf(adapter.totalvalue()));
        subtotals=String.valueOf(adapter.totalvalue());

         gst_amount = ( adapter.totalvalue() * 18 ) / 100;

        double total=adapter.totalvalue()+gst_amount;
        totalprice.setText(String.valueOf(total));



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
    public void usbPrinter()
    {
        // Constructor
        ESCPOSPrinter posPtr = new ESCPOSPrinter();

        // Set context
        posPtr.setContext( DashBoard.this );

        // Get Address
        UsbDevice usbDevice = null;												// null (Automatic detection)
        //
        // Connect
        int result = posPtr.connect( ESCPOSConst.CMP_PORT_USB, usbDevice );		// Android 3.1 ( API Level 12 ) or later
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

            posPtr.printBitmap(bitmap,300,100);

            double gst=gst_amount/2;
             gstvalue=String.format("%.2f", gst);


            // Print Text
            posPtr.printText( "Ganapathy Ram Theatre" + "\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
            posPtr.printText( "101, Lattice Bridge Road, Adyar, Baktavatsalm Nagar, Chennai, Tamil Nadu 600020" + "\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
            posPtr.printText( "Phone: 044 2441 7424" + "\n\n\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

            // posPtr.printText( "- Sample Print 1 -\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_2HEIGHT );
            // posPtr.printText( "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
            TableBuilder data=new TableBuilder();
            data.addRow("Qty.","Item","Price","Total");
            data.addRow("-----", "----", "-----","-----");
            for(int i=0;i<GlobalClass.cartList.size();i++)
            {
                Product product=GlobalClass.cartList.get(i);
                data.addRow(String.valueOf(product.getQuantity()),product.getProductname(),product.getProductprice(),product.getTotalprice());

            }

            posPtr.printText(data.toString(),ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

            posPtr.printText("-----------------------------------"+"\n",ESCPOSConst.CMP_ALIGNMENT_CENTER,ESCPOSConst.CMP_FNT_DEFAULT,ESCPOSConst.CMP_TXT_1WIDTH| ESCPOSConst.CMP_TXT_1HEIGHT );
            posPtr.printText("SUB TOTAL "+subtotals+"\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

            posPtr.printText("CGST "+gstvalue+ "\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
            posPtr.printText("SGST "+gstvalue+ "\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
            posPtr.printText("------------"+"\n",ESCPOSConst.CMP_ALIGNMENT_CENTER,ESCPOSConst.CMP_FNT_DEFAULT,ESCPOSConst.CMP_TXT_1WIDTH| ESCPOSConst.CMP_TXT_1HEIGHT );

            posPtr.printText("TOTAL "+totalprice.getText().toString()+"\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );




            posPtr.printText("Thank you for coming & we look"+"\n",ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
            posPtr.printText("forward to serve you again",ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );




            /*// Print QRcode
            posPtr.printQRCode( "http://www.citizen-systems.co.jp/", 6, ESCPOSConst.CMP_QRCODE_EC_LEVEL_L, ESCPOSConst.CMP_ALIGNMENT_RIGHT );
*/
            // Partial Cut with Pre-Feed
            posPtr.cutPaper( ESCPOSConst.CMP_CUT_PARTIAL_PREFEED );

            // End Transaction ( Batch )
            result = posPtr.transactionPrint( ESCPOSConst.CMP_TP_NORMAL );

            // Disconnect
            posPtr.disconnect();

            if ( ESCPOSConst.CMP_SUCCESS != result )
            {
                // Transaction Error
                Toast.makeText( DashBoard.this, "Transaction Error : " + Integer.toString( result ), Toast.LENGTH_LONG ).show();
            }
        }
        else
        {
            // Connect Error
            Toast.makeText( DashBoard.this, "Connect or Printer Error : " + Integer.toString( result ), Toast.LENGTH_LONG ).show();
        }
    }
}
