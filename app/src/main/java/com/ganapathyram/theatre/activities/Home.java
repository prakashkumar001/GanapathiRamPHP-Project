package com.ganapathyram.theatre.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.database.Categories;
import com.ganapathyram.theatre.database.UserSession;
import com.ganapathyram.theatre.database.Wifi_BluetoothAddress;
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
    LinearLayout order_food,parking,reports,admin;
    int backPressedCount = 0;
    GlobalClass global;
    ImageView logout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        global=(GlobalClass)getApplicationContext();
        parking=(LinearLayout)findViewById(R.id.parking);
        order_food=(LinearLayout)findViewById(R.id.orderfood);
        reports=(LinearLayout)findViewById(R.id.reports);
        admin=(LinearLayout)findViewById(R.id.admin);
        logout=(ImageView) findViewById(R.id.logout);
       // getDashboard();

        if(getHelper().getLogin().loginType.equalsIgnoreCase("user"))
        {
            admin.setVisibility(View.GONE);
        }else if(getHelper().getLogin().loginType.equalsIgnoreCase("admin"))
        {
            admin.setVisibility(View.VISIBLE);
        }
        getCategoryList();
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

               showDialogClass();

            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(Home.this, AdminDashboard.class);
                startActivity(i);
                finish();

            }
        });

        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Home.this, Reports.class);
                startActivity(i);
                finish();
            }
        });

        logout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                LogoutDialog();
                return false;
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

                if (o == null ) {

                }else {
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

                if (o == null ) {

                }else {

                    if(response.equalsIgnoreCase("401"))
                    {
                        getHelper().getDaoSession().deleteAll(UserSession.class);
                        Intent i=new Intent(Home.this,Login.class);
                        startActivity(i);
                        ActivityCompat.finishAffinity(Home.this);


                    }else
                    {
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



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }






                }


            }
        }
        new CategoryServer().execute();
    }

    public void showDialogClass()
    {

        // custom dialog
        final Dialog dialog = new Dialog(Home.this, R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bookingfloor);
        dialog.getWindow().setGravity(Gravity.CENTER);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.show();
        dialog.getWindow().setLayout((8 * width) / 10, (8 * height) / 10);

        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radiogroup);
        RadioButton firstclass = (RadioButton) dialog.findViewById(R.id.firstclass);
        RadioButton balcony = (RadioButton) dialog.findViewById(R.id.balcony);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch(i)
                {
                    case R.id.firstclass:
                        // TODO Something
                        if(getHelper().getAddress()!=null)
                        {

                            Wifi_BluetoothAddress address=getHelper().getAddress();


                                address.setSnack_floor("firstclass");
                                address.setWifiAddress("192.168.1.13");


                            getHelper().getDaoSession().update(address);


                        }else
                        {
                            Wifi_BluetoothAddress address=new Wifi_BluetoothAddress();
                            address.setId(Long.parseLong("1"));

                                address.setSnack_floor("firstclass");
                                address.setWifiAddress("192.168.1.13");


                            getHelper().getDaoSession().insert(address);
                        }

                        Intent intent=new Intent(Home.this, DashBoard.class);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();
                        break;
                    case R.id.balcony:
                        // TODO Something
                        if(getHelper().getAddress()!=null)
                        {

                            Wifi_BluetoothAddress address=getHelper().getAddress();


                            address.setSnack_floor("balcony");
                            address.setWifiAddress("192.168.1.14");


                            getHelper().getDaoSession().update(address);


                        }else
                        {
                            Wifi_BluetoothAddress address=new Wifi_BluetoothAddress();
                            address.setId(Long.parseLong("1"));

                            address.setSnack_floor("balcony");
                            address.setWifiAddress("192.168.1.14");


                            getHelper().getDaoSession().insert(address);
                        }

                        Intent intents=new Intent(Home.this, DashBoard.class);
                        startActivity(intents);
                        finish();
                        dialog.dismiss();
                        break;

                }
            }
        });

        dialog.show();



    }
    void LogoutDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Home.this);

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
                dialog = new ProgressDialog(Home.this);
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

                if (o == null ) {

                }else {

                    try {
                        JSONObject object = new JSONObject(o);

                        String payload=object.getString("payload");
                        if(payload.equalsIgnoreCase("success"))
                        {
                            getHelper().getDaoSession().deleteAll(UserSession.class);

                            Intent i=new Intent(Home.this,Login.class);
                            startActivity(i);
                            ActivityCompat.finishAffinity(Home.this);

                        }




                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
        }
        new LogOutServer().execute();
    }
}
