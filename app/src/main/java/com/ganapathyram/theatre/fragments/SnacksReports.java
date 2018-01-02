package com.ganapathyram.theatre.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.activities.Login;
import com.ganapathyram.theatre.activities.Reports;
import com.ganapathyram.theatre.adapter.ReportAdapter;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.database.UserSession;
import com.ganapathyram.theatre.model.Report;
import com.ganapathyram.theatre.utils.WSUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.ganapathyram.theatre.helper.Helper.getHelper;

/**
 * Created by Creative IT Works on 12-Dec-17.
 */

public class SnacksReports extends Fragment {
    GlobalClass global;
    RecyclerView list;
    ReportAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_fragment, container, false);
        list=(RecyclerView)view.findViewById(R.id.report_list);
        global=new GlobalClass();
        getTranscation();
        return view;
    }
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy hh:mm:ss aa");
        Date date = new Date();
        return dateFormat.format(date);
    }
   


    void getTranscation()
    {
        class getTranscationfromServer extends AsyncTask<String, String, String> {
            ProgressDialog dialog;
            String response = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(getActivity());
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

                   /* String[] dates=getDateTime().split(" ");
                    String[] dmy=dates[0].split("/");
                    String month=dmy[1];
                    String year=dmy[2];

                    user.put("startDate", "01/"+month+"/"+year+ " 12:00:00 AM");
                    // user.put("startDate", "01/"+month+"/"+year+ " 12:00:00 AM");
                    user.put("endDate", getDateTime());
                    user.put("sessionId",getHelper().getSession().getSessionId());*/
                    user.put("venueUid", "gprtheatre");


                    response = utils.responsedetailsfromserver(requestURL, user.toString());

                    System.out.println("SERVER REPLIED:" + response);
                    dialog.dismiss();
                    //{"status":"success","message":"Registration Successful","result":[],"statusCode":200}
                    // {"status":"success","message":"Logged in Successfully","result":{"statusCode":4},"statusCode":200}
                } catch (Exception ex) {
                    Log.i("ERROR", "ERROR" + ex.toString());
                }

                return response;
            }


            @Override
            protected void onPostExecute(String o) {



                if (o != null || !o.equalsIgnoreCase("null")) {

                    ArrayList<Report> reportArrayList=new ArrayList<>();
                    ArrayList<String> sessions=new ArrayList<>();
                    try {
                        JSONObject object = new JSONObject(o);

                        JSONArray sessionList=object.getJSONArray("sessionList");
                        for(int i=0;i<sessionList.length();i++) {

                            JSONObject session=new JSONObject();
                            String sessionId=session.getString("sessionId");
                            sessions.add(sessionId);

                        }


                        for(int j=0;j<sessions.size();j++)
                        {
                            JSONArray payload=object.getJSONArray(sessions.get(j));

                            String headerName="SESSION";
                            int counts=j+1;
                            reportArrayList.add(new Report("","","","",String.valueOf(headerName+" "+counts)));
                            for(int i=0;i<payload.length();i++)
                            {
                                JSONObject ob=payload.getJSONObject(i);
                                String txnDate=ob.getString("txnDate");
                                String txnCount=ob.getString("txnCount");
                                String amount=ob.getString("txnAmt");
                               // String dateStr=ob.getString("dateStr");
                                reportArrayList.add(new Report(txnDate,txnCount,amount,txnDate,"false"));

                            }
                        }



                        adapter=new ReportAdapter(getActivity(),reportArrayList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
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
