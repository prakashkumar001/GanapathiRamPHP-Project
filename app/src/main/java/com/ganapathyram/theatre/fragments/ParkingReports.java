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
import android.widget.TextView;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.activities.Login;
import com.ganapathyram.theatre.activities.Reports;
import com.ganapathyram.theatre.adapter.ReportAdapter;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.database.UserSession;
import com.ganapathyram.theatre.model.Report;
import com.ganapathyram.theatre.utils.WSUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;

import static com.ganapathyram.theatre.helper.Helper.getHelper;

/**
 * Created by Creative IT Works on 12-Dec-17.
 */

public class ParkingReports extends Fragment {
    GlobalClass global;
    RecyclerView list;
    ReportAdapter adapter;
    ArrayList<Report> reportArrayList;
    TextView totalsales,parkingTypes;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_fragment, container, false);
        list=(RecyclerView)view.findViewById(R.id.report_list);
        totalsales=(TextView) view.findViewById(R.id.totalsales);
        parkingTypes=(TextView) view.findViewById(R.id.timeout);
        parkingTypes.setText("Type");
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


                    String requestURL = global.ApiBaseUrl + "parking/transactions/";
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
                    user.put("sessionId",getHelper().getSession().getSessionId());
               */     user.put("venueUid", "gprtheatre");


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


                dialog.dismiss();
                if (o != null || !o.equalsIgnoreCase("null")) {


                    try {
                        reportArrayList = new ArrayList<>();
                        ArrayList<String> sessionListKeys = new ArrayList<>();
                        JSONObject object = new JSONObject(o);

                        JSONObject payload = object.getJSONObject("payload");

                        Iterator<String> keys = payload.keys();

                        while (keys.hasNext()) {
                            String key = keys.next();
                            sessionListKeys.add(key);
                        }


                        for (int j = 0; j < sessionListKeys.size(); j++) {
                            JSONArray array = payload.getJSONArray(sessionListKeys.get(j));

                            String headerName = "SESSION";
                            int counts = j + 1;
                            reportArrayList.add(new Report("","", "", "0.0", "", String.valueOf(headerName + " " + counts),"parking",null));
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject ob = array.getJSONObject(i);
                                String txnDate = ob.getString("txnDate");
                                String amount = ob.getString("txnAmt");
                                String parkingType = ob.getString("txnType");
                                // String dateStr=ob.getString("dateStr");
                                reportArrayList.add(new Report(String.valueOf(i+1),txnDate, parkingType, amount, txnDate, "false","parking",null));

                            }
                        }


                        adapter = new ReportAdapter(getActivity(), reportArrayList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        list.setLayoutManager(layoutManager);
                        list.setItemAnimator(new DefaultItemAnimator());
                        list.setAdapter(adapter);
                        list.setNestedScrollingEnabled(false);


                        // toMap(object);
                        totalsales.setText(String.format("%.2f",getTotalSales()));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }
        }
        new getTranscationfromServer().execute();
    }

    public double getTotalSales()
    {
        double totalValue=0.0;
        for(int i=0;i<reportArrayList.size();i++)
        {
            totalValue=totalValue+Double.parseDouble(reportArrayList.get(i).amount);
        }

        return totalValue;
    }
}
