package com.ganapathyram.theatre.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ganapathyram.theatre.R;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        productListView = (RecyclerView) findViewById(R.id.productList);

        productList=new ArrayList<>();




        adapter = new ProductListAdapter(getApplicationContext(), productList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        productListView.setLayoutManager(layoutManager);
        productListView.setItemAnimator(new DefaultItemAnimator());
        productListView.setAdapter(adapter);
        productListView.setNestedScrollingEnabled(false);
        adapter.notifyDataSetChanged();

    }
}
