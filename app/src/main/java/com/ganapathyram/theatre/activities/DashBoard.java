package com.ganapathyram.theatre.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.adapter.CartAdapter;
import com.ganapathyram.theatre.adapter.ProductListAdapter;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.model.Product;

import java.util.ArrayList;

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

                //finish();
                global.cartList.clear();
                DashBoard.adapter.notifyDataSetChanged();
                DashBoard.cartcount.setText("0");
            }
        });
        final int columns = getResources().getInteger(R.integer.grid_column);

        CartAdapter adapter=new CartAdapter(DashBoard.this,dialog);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), columns);
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
}
