package com.ganapathyram.theatre.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.adapter.ReportAdapter;
import com.ganapathyram.theatre.database.UserSession;
import com.ganapathyram.theatre.parking.ParkingDashboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ganapathyram.theatre.helper.Helper.getHelper;

/**
 * Created by Prakash on 11/21/2017.
 */

public class Reports extends AppCompatActivity {
    ImageView logout;
    RecyclerView list;
    ReportAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports);
        logout=(ImageView)findViewById(R.id.logout);
        list=(RecyclerView)findViewById(R.id.report_list);


        adapter=new ReportAdapter(Reports.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        list.setLayoutManager(layoutManager);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(adapter);
        list.setNestedScrollingEnabled(false);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LogoutDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(Reports.this, Home.class);
        startActivity(i);
        finish();
    }
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy hh:mm:aa");
        Date date = new Date();
        return dateFormat.format(date);
    }
    void LogoutDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Reports.this);

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

                        if(getHelper().getSession().getEndtime()==null)
                        {
                            UserSession session=getHelper().getSession();
                            session.setEndtime(getDateTime());
                            getHelper().getDaoSession().update(session);


                        }

                        Intent i=new Intent(Reports.this,Login.class);
                        startActivity(i);
                        ActivityCompat.finishAffinity(Reports.this);

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
}
