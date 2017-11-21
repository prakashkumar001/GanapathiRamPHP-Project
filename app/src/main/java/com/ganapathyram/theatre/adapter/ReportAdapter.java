package com.ganapathyram.theatre.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.database.UserSession;
import com.ganapathyram.theatre.model.Parking;
import com.ganapathyram.theatre.parking.ParkingDashboard;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import static com.ganapathyram.theatre.helper.Helper.getHelper;

/**
 * Created by Prakash on 11/21/2017.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {


    List<UserSession> list = new ArrayList<>();

    GlobalClass global;
    ImageLoader loader;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sno,timein,timeout;


        public MyViewHolder(View view) {
            super(view);

            sno = (TextView) view.findViewById(R.id.sno);
            timein = (TextView) view.findViewById(R.id.timein);
            timeout = (TextView) view.findViewById(R.id.timeout);

        }
    }


    public ReportAdapter(Context context) {

        this.context=context;
        list=getHelper().getSessionList();
        global=new GlobalClass();

    }

    @Override
    public ReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_items, parent, false);


        return new ReportAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ReportAdapter.MyViewHolder holder, final int position) {


        holder.sno.setText(String.valueOf(list.get(position).getId()));
        holder.timein.setText(list.get(position).getStartTime());
        holder.timeout.setText(list.get(position).getEndtime());





    }

    @Override
    public int getItemCount() {
        return list.size();
    }




}