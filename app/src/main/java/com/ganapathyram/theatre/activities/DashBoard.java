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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.adapter.CartAdapter;
import com.ganapathyram.theatre.adapter.ProductListAdapter;
import com.ganapathyram.theatre.model.Product;

import java.util.ArrayList;

/**
 * Created by Prakash on 9/19/2017.
 */

public class DashBoard extends AppCompatActivity {
    public  RecyclerView productListView;
    public ProductListAdapter adapter;
    ArrayList<Product> productList;
    public static TextView cartcount;
    RelativeLayout cartRelativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
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

        productList.add(new Product("1","Pop Corn","45.00",R.mipmap.placeholder,1,"45.00"));
        productList.add(new Product("2","Veg Puff","15.00",R.mipmap.veg_puff,1,"15.00"));
        productList.add(new Product("3","Chicken Puff","25.00",R.mipmap.chicken_samosa,1,"25.00"));
        productList.add(new Product("4","Chicken Samosa","30.00",R.mipmap.veg_puff,1,"30.00"));
        productList.add(new Product("5","Egg Puff","26.00",R.mipmap.egg_puff,1,"26.00"));
        productList.add(new Product("1","Pop Corn","45.00",R.mipmap.placeholder,1,"45.00"));
        productList.add(new Product("2","Veg Puff","15.00",R.mipmap.veg_puff,1,"15.00"));
        productList.add(new Product("3","Chicken Puff","25.00",R.mipmap.chicken_samosa,1,"25.00"));
        productList.add(new Product("4","Chicken Samosa","30.00",R.mipmap.veg_puff,1,"30.00"));
        productList.add(new Product("5","Egg Puff","26.00",R.mipmap.egg_puff,1,"26.00"));




        adapter = new ProductListAdapter(getApplicationContext(), productList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
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
        dialog.setContentView(R.layout.cartpage);
        dialog.getWindow().setGravity(Gravity.CENTER);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.show();
        dialog.getWindow().setLayout((8 * width) / 10, (8 * height) / 10);

        RecyclerView cartview=(RecyclerView)dialog.findViewById(R.id.cartlist) ;
        CartAdapter adapter=new CartAdapter(DashBoard.this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        cartview.setLayoutManager(layoutManager);
        cartview.setItemAnimator(new DefaultItemAnimator());
        cartview.setAdapter(adapter);
        cartview.setNestedScrollingEnabled(false);
        cartview.setAdapter(adapter);



        dialog.show();



}
}
