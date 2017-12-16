package com.ganapathyram.theatre.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.adapter.ReportAdapter;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.database.UserSession;
import com.ganapathyram.theatre.fragments.ParkingReports;
import com.ganapathyram.theatre.fragments.SnacksReports;
import com.ganapathyram.theatre.model.Report;
import com.ganapathyram.theatre.parking.ParkingDashboard;
import com.ganapathyram.theatre.utils.WSUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.ganapathyram.theatre.helper.Helper.getHelper;

/**
 * Created by Prakash on 11/21/2017.
 */

public class Reports extends AppCompatActivity {

    GlobalClass global;
    RadioGroup radioGroup;
    RadioButton snacks, parking;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports);
        global=(GlobalClass)getApplicationContext();
        radioGroup=(RadioGroup) findViewById(R.id.radiogroup);
        snacks = (RadioButton) findViewById(R.id.snacks);
        parking = (RadioButton) findViewById(R.id.parking);
        SnacksReports fragment = new SnacksReports();
        loadFragments(fragment);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.snacks) {
                    SnacksReports fragment = new SnacksReports();
                    loadFragments(fragment);

                } else if (checkedId == R.id.parking) {
                    ParkingReports fragment = new ParkingReports();
                    loadFragments(fragment);

                }
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

                        logout();


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

    void logout()
    {
        class LogOutServer extends AsyncTask<String, String, String> {
            ProgressDialog dialog;
            String response = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(Reports.this);
                dialog.setMessage(getString(R.string.loading));
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(String[] params) {
                try {


                    String requestURL = global.ApiBaseUrl + "user/logout";
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

                if (o != null || !o.equalsIgnoreCase("null")) {
                    try {
                        JSONObject object = new JSONObject(o);

                        String payload=object.getString("payload");
                        if(payload.equalsIgnoreCase("success"))
                        {
                            getHelper().getDaoSession().deleteAll(UserSession.class);

                            Intent i=new Intent(Reports.this,Login.class);
                            startActivity(i);
                            ActivityCompat.finishAffinity(Reports.this);

                        }




                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
        }
        new LogOutServer().execute();
    }

    public void loadFragments(Fragment fragment) {

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

    }

}
