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

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.adapter.AdminReportAdapter;
import com.ganapathyram.theatre.adapter.ReportAdapter;
import com.ganapathyram.theatre.adapter.UserAdapter;
import com.ganapathyram.theatre.common.GlobalClass;
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
    Spinner userList,transcationType;
    String user="";
    String type="snacks";
    ArrayList<Users> userLists;
    List<Report> snacks_list;
    List<Report> parking_list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_panel);
        transcations = (RecyclerView) findViewById(R.id.transcations);
        userList = (Spinner) findViewById(R.id.selectusers);
        transcationType = (Spinner) findViewById(R.id.transcationType);

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


                    String requestURL = GlobalClass.ApiBaseUrl + "user/details";
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

                userLists=new ArrayList<>();
                try{

                    JSONObject object=new JSONObject(o);
                    JSONArray array=object.getJSONArray("payload");
                    for(int i=0;i<array.length();i++)
                    {
                        JSONObject object1=array.getJSONObject(i);
                        String id=object1.getString("userId");
                        String userName=object1.getString("userName");
                        userLists.add(new Users(id,userName));

                    }
                    userLists.add(0,new Users("all","All Users"));
                    UserAdapter adapter=new UserAdapter(AdminDashboard.this,userLists);
                    userList.setAdapter(adapter);


                    userList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            Users users=userLists.get(i);
                            snacks_list=new ArrayList<>();
                            parking_list=new ArrayList<>();
                            getTransactions(users);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                    //getTransactions();

                }catch (Exception e)
                {

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

    public void getTransactions(final Users users) {
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


                    String requestURL = GlobalClass.ApiBaseUrl + "transaction/details";
                    WSUtils utils = new WSUtils();
                    // JSONObject object = new JSONObject();
                    JSONObject user = new JSONObject();

                    if(users.id.equalsIgnoreCase("all"))
                    {

                    }else
                    {
                        user.put("userId", users.id);

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


                try {
                    JSONObject result=new JSONObject(o);
                    JSONObject payload=result.getJSONObject("payload");
                    JSONArray parkingArray=payload.getJSONArray("Parking");
                    JSONArray snacks=payload.getJSONArray("Snacks & Beverages");

                    for(int i=0;i<parkingArray.length();i++)
                    {
                        JSONObject data=parkingArray.getJSONObject(i);
                        String txnDate=data.getString("txnDate");
                        String txnCount=data.getString("txnCount");
                        String amount=data.getString("amount");
                        String dateStr=data.getString("dateStr");


                        parking_list.add(new Report(txnDate,txnCount,amount,dateStr,"false"));
                    }


                    for(int k=0;k<snacks.length();k++)
                    {
                        JSONObject data=snacks.getJSONObject(k);
                        String txnDate=data.getString("txnDate");
                        String txnCount=data.getString("txnCount");
                        String amount=data.getString("amount");
                        String dateStr=data.getString("dateStr");

                        snacks_list.add(new Report(txnDate,txnCount,amount,dateStr,"false"));


                    }


                    ArrayList<String> types=new ArrayList<>();
                    types.add("Snacks");
                    types.add("Parking");
                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(AdminDashboard.this,android.R.layout.simple_list_item_1,types);
                    transcationType.setAdapter(adapter);




transcationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(i==0)
        {
            AdminReportAdapter adapter=new AdminReportAdapter(AdminDashboard.this,snacks_list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AdminDashboard.this);
            transcations.setLayoutManager(layoutManager);
            transcations.setItemAnimator(new DefaultItemAnimator());
            transcations.setAdapter(adapter);
            transcations.setNestedScrollingEnabled(false);


        }else if(i==1)
        {
            AdminReportAdapter  adapter=new AdminReportAdapter(AdminDashboard.this,parking_list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AdminDashboard.this);
            transcations.setLayoutManager(layoutManager);
            transcations.setItemAnimator(new DefaultItemAnimator());
            transcations.setAdapter(adapter);
            transcations.setNestedScrollingEnabled(false);


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
        new TranscationListServer().execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(AdminDashboard.this,Home.class);
        startActivity(i);
        ActivityCompat.finishAffinity(AdminDashboard.this);
    }
}