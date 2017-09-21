package com.ganapathyram.theatre.parking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.model.Parking;

import java.util.ArrayList;

/**
 * Created by Prakash on 9/21/2017.
 */

public class ParkingDashboard extends AppCompatActivity {
    public RecyclerView parkinglist;
    ArrayList<Parking> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_dashboard);
        parkinglist = (RecyclerView) findViewById(R.id.parking);
        list=new ArrayList<>();
        list.add(new Parking("CAR PARKING",R.mipmap.car_placeholder));
        list.add(new Parking("BIKE PARKING",R.mipmap.bike_placeholder));
    }
}

