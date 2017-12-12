package com.ganapathyram.theatre.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.adapter.ReportAdapter;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.database.UserSession;
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
    ImageView logout;
    RecyclerView list;
    ReportAdapter adapter;
    GlobalClass global;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports);
        global=(GlobalClass)getApplicationContext();
        logout=(ImageView)findViewById(R.id.logout);
        list=(RecyclerView)findViewById(R.id.report_list);
        getTranscation();


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
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy hh:mm:ss aa");
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



    void getTranscation()
    {
        class getTranscationfromServer extends AsyncTask<String, String, String> {
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


                    String requestURL = global.ApiBaseUrl + "product/transactions/";
                    WSUtils utils = new WSUtils();

                    // JSONObject object = new JSONObject();
                    JSONObject user = new JSONObject();
                    user.put("userId", getHelper().getSession().getUserId());

                    String[] dates=getDateTime().split(" ");
                    String[] dmy=dates[0].split("/");
                    String month=dmy[1];
                    String year=dmy[2];

                    user.put("startDate", "01/"+month+"/"+year+ " 12:00:00 AM");
                   // user.put("startDate", "01/"+month+"/"+year+ " 12:00:00 AM");
                    user.put("endDate", getDateTime());
                    user.put("sessionId",getHelper().getSession().getSessionId());
                    user.put("venueUid", "gprtheatre");


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

                    ArrayList<Report> reportArrayList=new ArrayList<>();
                    try {
                        JSONObject object = new JSONObject(o);

                        JSONArray payload=object.getJSONArray("payload");

                        for(int i=0;i<payload.length();i++)
                        {
                            JSONObject ob=payload.getJSONObject(i);
                            String txnDate=ob.getString("txnDate");
                            String txnCount=ob.getString("txnCount");
                            String amount=ob.getString("amount");
                            String dateStr=ob.getString("dateStr");
                            reportArrayList.add(new Report(txnDate,txnCount,amount,dateStr));

                        }

                        adapter=new ReportAdapter(Reports.this,reportArrayList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        list.setLayoutManager(layoutManager);
                        list.setItemAnimator(new DefaultItemAnimator());
                        list.setAdapter(adapter);
                        list.setNestedScrollingEnabled(false);




                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
        }
        new getTranscationfromServer().execute();
    }
}
