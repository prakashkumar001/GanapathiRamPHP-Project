package com.ganapathyram.theatre.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.adapter.AdminReportAdapter;
import com.ganapathyram.theatre.adapter.UserAdapter;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.database.UserList;
import com.ganapathyram.theatre.model.Report;
import com.ganapathyram.theatre.model.Users;
import com.ganapathyram.theatre.utils.WSUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.ganapathyram.theatre.helper.Helper.getHelper;

/**
 * Created by Creative IT Works on 28-Dec-17.
 */

public class AdminDashboard extends AppCompatActivity {
    RecyclerView transcations;
    Spinner userList, transcationType, show_time;
    String userId = "all";
    String showType = "all";
    String type = "snacks";
    ArrayList<Users> userLists;
    List<Report> snacks_list;
    List<Report> parking_list;
    TextView transcation_date, totalsales;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_panel);
        transcations = (RecyclerView) findViewById(R.id.transcations);
        userList = (Spinner) findViewById(R.id.selectusers);
        show_time = (Spinner) findViewById(R.id.show_time);
        transcationType = (Spinner) findViewById(R.id.transcationType);
        transcation_date = (TextView) findViewById(R.id.transcation_date);
        totalsales = (TextView) findViewById(R.id.totalsales);
        transcation_date.setText(getCurrentDateTime());

        GetUserList();


    }

    public void GetUserList() {
        class UserListServer extends AsyncTask<String, String, String> {
            ProgressDialog dialog;
            String response = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(AdminDashboard.this);
                dialog.setMessage(getString(R.string.loading));
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(String[] params) {
                try {


                    String requestURL = GlobalClass.deFaultBaseUrl+GlobalClass.ApiBaseUrl + "user/details";
                    WSUtils utils = new WSUtils();


                    response = utils.getResultFromHttpRequest(requestURL, "GET", new HashMap<String, String>());

                    System.out.println("SERVER REPLIED:" + response);

                } catch (Exception ex) {
                    Log.i("ERROR", "ERROR" + ex.toString());
                }

                return response;
            }


            @Override
            protected void onPostExecute(String o) {

                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                if (o == null) {

                } else {
                    userLists = new ArrayList<>();
                    try {

                        JSONObject object = new JSONObject(o);
                        JSONArray array = object.getJSONArray("payload");
                        // getHelper().getDaoSession().insertOrReplace(new Users("all","All Users"));

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object1 = array.getJSONObject(i);
                            String id = object1.getString("userId");
                            String userName = object1.getString("userName");
                            userLists.add(new Users(id, userName));

                            UserList userList = new UserList();
                            userList.setUserId(id);
                            userList.setUserName(userName);
                            getHelper().getDaoSession().insertOrReplace(userList);

                        }
                        userLists.add(0, new Users("all", "All"));
                        UserAdapter adapter = new UserAdapter(AdminDashboard.this, userLists);
                        userList.setAdapter(adapter);


                        userList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                Users users = userLists.get(i);
                                userId = users.id;
                                snacks_list = new ArrayList<>();
                                parking_list = new ArrayList<>();
                                 getShowTransactions(userId,showType);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });


                        final ArrayList<String> showtimes = new ArrayList<>();
                        showtimes.add("All");
                        showtimes.add("Morning");
                        showtimes.add("Noon");
                        showtimes.add("Evening");
                        showtimes.add("Night");
                        ArrayAdapter<String> showadapter = new ArrayAdapter<String>(AdminDashboard.this, R.layout.spinner_item,R.id.textview, showtimes);
                        show_time.setAdapter(showadapter);


                        show_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                showType = showtimes.get(i);
                                    getShowTransactions(userId, showType);




                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });


                        //getTransactions();

                    } catch (Exception e) {

                    }


                }
            }


        }
        new UserListServer().execute();
    }


    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy hh:mm:ss aa");
        Date date = new Date();
        return dateFormat.format(date);
    }




    public void getShowTransactions(final String usersid, final String showType) {
        class TranscationListServer extends AsyncTask<String, String, String> {
            ProgressDialog dialog;
            String response = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(AdminDashboard.this);
                dialog.setMessage(getString(R.string.loading));
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(String[] params) {
                try {


                    String requestURL = GlobalClass.deFaultBaseUrl+ GlobalClass.ApiBaseUrl + "transaction/details";
                    WSUtils utils = new WSUtils();
                    // JSONObject object = new JSONObject();
                    JSONObject user = new JSONObject();

                    if (usersid.equalsIgnoreCase("all")) {

                    } else {
                        user.put("userId", usersid);

                    }


                    if (showType.equalsIgnoreCase("all")) {

                    } else if(showType.equalsIgnoreCase("Morning")) {
                        user.put("showType", "morning");

                    }else if(showType.equalsIgnoreCase("Noon")) {
                        user.put("showType", "noon");

                    }else if(showType.equalsIgnoreCase("Evening")) {
                        user.put("showType", "evening");

                    }else if(showType.equalsIgnoreCase("Night")) {
                        user.put("showType", "night");

                    }

                    user.put("venueUid", "gprtheatre");

                    response = utils.responsedetailsfromserver(requestURL, user.toString());

                    System.out.println("SERVER REPLIED:" + response);

                } catch (Exception ex) {
                    Log.i("ERROR", "ERROR" + ex.toString());
                }

                return response;
            }


            @Override
            protected void onPostExecute(String o) {

                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

                if (o == null) {

                } else {
                    snacks_list = new ArrayList<>();
                    parking_list = new ArrayList<>();
                    try {
                        JSONObject result = new JSONObject(o);
                        JSONObject payload = result.getJSONObject("payload");
                        final JSONArray parkingArray = payload.getJSONArray("Parking");
                        JSONArray snacks = payload.getJSONArray("Snacks & Beverages");

                        for (int i = 0; i < parkingArray.length(); i++) {
                            JSONObject data = parkingArray.getJSONObject(i);
                            String txnDate = data.getString("txnDate");
                            String txnCount = data.getString("txnCount");
                            String amount = data.getString("amount");
                            String dateStr = data.getString("dateStr");
                            String userId = data.getString("userId");

                            parking_list.add(new Report(String.valueOf(i + 1), txnDate, txnCount, amount, dateStr, "false", "parking", userId));
                        }


                        for (int k = 0; k < snacks.length(); k++) {
                            JSONObject data = snacks.getJSONObject(k);
                            String txnDate = data.getString("txnDate");
                            String txnCount = data.getString("txnCount");
                            String amount = data.getString("amount");
                            String dateStr = data.getString("dateStr");
                            String userId = data.getString("userId");

                            snacks_list.add(new Report(String.valueOf(k + 1), txnDate, txnCount, amount, dateStr, "false", "snacks", userId));


                        }

                        ArrayList<String> types = new ArrayList<>();
                        types.add("Snacks");
                        types.add("Parking");
                        ArrayAdapter<String> foodadapter = new ArrayAdapter<String>(AdminDashboard.this, R.layout.spinner_item,R.id.textview, types);
                        transcationType.setAdapter(foodadapter);


                        transcationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (i == 0) {

                                    type = "snacks";

                                    AdminReportAdapter adapter = new AdminReportAdapter(AdminDashboard.this, snacks_list);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AdminDashboard.this);
                                    transcations.setLayoutManager(layoutManager);
                                    transcations.setItemAnimator(new DefaultItemAnimator());
                                    transcations.setAdapter(adapter);
                                    transcations.setNestedScrollingEnabled(false);
                                    totalsales.setText(String.format("%.2f", getTotalSales(snacks_list)));

                                } else if (i == 1) {
                                    type = "parking";


                                    AdminReportAdapter adapter = new AdminReportAdapter(AdminDashboard.this, parking_list);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AdminDashboard.this);
                                    transcations.setLayoutManager(layoutManager);
                                    transcations.setItemAnimator(new DefaultItemAnimator());
                                    transcations.setAdapter(adapter);
                                    transcations.setNestedScrollingEnabled(false);
                                    totalsales.setText(String.format("%.2f", getTotalSales(parking_list)));

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }


        }
        new TranscationListServer().execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AdminDashboard.this, Home.class);
        startActivity(i);
        ActivityCompat.finishAffinity(AdminDashboard.this);
    }

    public String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public double getTotalSales(List<Report> list) {
        double totalValue = 0.0;
        for (int i = 0; i < list.size(); i++) {
            totalValue = totalValue + Double.parseDouble(list.get(i).amount);
        }

        return totalValue;
    }
}
