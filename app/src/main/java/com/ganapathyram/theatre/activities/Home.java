package com.ganapathyram.theatre.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.database.Categories;
import com.ganapathyram.theatre.model.Dashboard;
import com.ganapathyram.theatre.parking.ParkingDashboard;
import com.ganapathyram.theatre.utils.WSUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ganapathyram.theatre.helper.Helper.getHelper;

/**
 * Created by Prakash on 9/25/2017.
 */

public class Home extends AppCompatActivity {
    LinearLayout order_food,parking;
    int backPressedCount = 0;
    GlobalClass global;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        global=(GlobalClass)getApplicationContext();
        parking=(LinearLayout)findViewById(R.id.parking);
        order_food=(LinearLayout)findViewById(R.id.orderfood);


       // getDashboard();

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

                getCategoryList();

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

    public void getDashboard() {
        class getDashboardServer extends AsyncTask<String, String, String> {
            ProgressDialog dialog;
            String response = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(Home.this);
                dialog.setMessage(getString(R.string.loading));
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(String[] params) {
                try {


                    String requestURL = global.ApiBaseUrl + "modules";
                    WSUtils utils = new WSUtils();



                    response = utils.getResultFromHttpRequest(requestURL, "POST",new HashMap<String, String>());

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

                ArrayList<Dashboard> list;

                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

                if (o != null || !o.equalsIgnoreCase("null")) {
                    try {
                        JSONObject object = new JSONObject(o);
                        JSONArray array=object.getJSONArray("payload");

                        list=new ArrayList<>();
                        for(int i=0;i<array.length();i++)
                        {
                            JSONObject data=array.getJSONObject(i);
                            String moduleName=data.getString("moduleName");
                            String moduleUid=data.getString("moduleUid");
                            String active=data.getString("active");

                            list.add(new Dashboard(moduleName,moduleUid,active));

                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }
        }
        new getDashboardServer().execute();
    }


    public void getCategoryList() {
        class CategoryServer extends AsyncTask<String, String, String> {
            ProgressDialog dialog;
            String response = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(Home.this);
                dialog.setMessage(getString(R.string.loading));
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(String[] params) {
                try {


                    String requestURL = global.ApiBaseUrl + "product/categories";
                    WSUtils utils = new WSUtils();



                    response = utils.getResultFromHttpRequest(requestURL, "GET",new HashMap<String, String>());

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
                        JSONArray array=object.getJSONArray("payload");

                        for(int i=0;i<array.length();i++)
                        {
                            JSONObject data=array.getJSONObject(i);
                            String categoryId=data.getString("categoryId");
                            String categoryName=data.getString("categoryName");
                            String categoryUid=data.getString("categoryUid");
                            String active=data.getString("active");

                            Categories categories=new Categories();
                            categories.categoryId=(Long.parseLong(categoryId));
                            categories.categoryName=categoryName;
                            categories.categoryUid=categoryUid;
                            categories.active=active;

                            getHelper().getDaoSession().insertOrReplace(categories);

                        }

                        Intent i=new Intent(Home.this, DashBoard.class);
                        startActivity(i);
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }
        }
        new CategoryServer().execute();
    }
}
