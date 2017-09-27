package com.ganapathyram.theatre.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.parking.ParkingDashboard;

/**
 * Created by Prakash on 9/25/2017.
 */

public class Home extends AppCompatActivity {
    ImageView parking,order_food;
    int backPressedCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        parking=(ImageView)findViewById(R.id.parking);
        order_food=(ImageView)findViewById(R.id.orderfood);

        parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(Home.this, ParkingDashboard.class);
                startActivity(i);
                finish();
            }
        });


        order_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Home.this, DashBoard.class);
                startActivity(i);
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {


        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            if (backPressedCount == 1) {
                ActivityCompat.finishAffinity(Home.this);
            } else {
                backPressedCount++;

                new Thread() {
                    @Override
                    public void run() {
                        //super.run();
                        try {
                            sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            backPressedCount = 0;
                        }
                    }
                }.start();
            }
        } else {
            super.onBackPressed();
        }


    }
}
