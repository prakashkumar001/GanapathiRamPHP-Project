package com.ganapathyram.theatre.parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.activities.DashBoard;
import com.ganapathyram.theatre.activities.Home;
import com.ganapathyram.theatre.adapter.ParkingAdapter;
import com.ganapathyram.theatre.adapter.ProductListAdapter;
import com.ganapathyram.theatre.model.Parking;

import java.util.ArrayList;

/**
 * Created by Prakash on 9/21/2017.
 */

public class ParkingDashboard extends AppCompatActivity {
    public RecyclerView parkinglist;
    ArrayList<Parking> list;
    ParkingAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_dashboard);
        parkinglist = (RecyclerView) findViewById(R.id.parking);
        list=new ArrayList<>();
        list.add(new Parking("BIKE PARKING",R.mipmap.bike_icon));
        list.add(new Parking("CAR PARKING",R.mipmap.car_icon));
        list.add(new Parking("AUTO PARKING",R.mipmap.auto_icon));
        list.add(new Parking("HEAVY VEHICLE PARKING",R.mipmap.truck_icon));
        list.add(new Parking("CYCLE PARKING",R.mipmap.cycle_icon));



        adapter=new ParkingAdapter(ParkingDashboard.this,list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
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
}

