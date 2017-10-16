package com.ganapathyram.theatre.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.citizen.sdk.ESCPOSConst;
import com.citizen.sdk.ESCPOSPrinter;
import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.adapter.CartAdapter;
import com.ganapathyram.theatre.adapter.ProductListAdapter;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.model.Product;
import com.ganapathyram.theatre.model.ProductAvailable;
import com.ganapathyram.theatre.utils.TableBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prakash on 9/19/2017.
 */

public class DashBoard extends AppCompatActivity {
    public static RecyclerView productListView;
    public static ProductListAdapter adapter;
    public static ArrayList<Product> productList;
    public static TextView cartcount,totalprice;
    RelativeLayout cartRelativeLayout;
    public static  RecyclerView cartview;
    int indexpos=-1;
    GlobalClass global;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        global=new GlobalClass();
        productListView = (RecyclerView) findViewById(R.id.productList);
        cartcount=(TextView)findViewById(R.id.cartcount);
        cartRelativeLayout=(RelativeLayout)findViewById(R.id.cartRelativeLayout);

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

        totalprice.setText(rupee+" "+String.valueOf(adapter.totalvalue()));


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
            Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.print_icon23);
            // Character set
            posPtr.setEncoding( "ISO-8859-1" );		// Latin-1
            //posPtr.setEncoding( "Shift_JIS" );	// Japanese 日本語を印字する場合は、この行を有効にしてください.

            // Start Transaction ( Batch )
            posPtr.transactionPrint( ESCPOSConst.CMP_TP_TRANSACTION );
            posPtr.printBitmap(bitmap, ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, 100 | 100);

            // Print Text
            posPtr.printText( "Ganapathy Ram Theatre" + "\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
            posPtr.printText( "Adyar ,Chennai-600096" + "\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
            posPtr.printText( "Phone : 044-425962886" + "\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

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
