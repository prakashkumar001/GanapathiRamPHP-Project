package com.ganapathyram.theatre.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.ganapathyram.theatre.model.Report;
import com.ganapathyram.theatre.parking.ParkingDashboard;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import static com.ganapathyram.theatre.helper.Helper.getHelper;

/**
 * Created by Prakash on 11/21/2017.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {


    List<Report> list = new ArrayList<>();

    GlobalClass global;
    ImageLoader loader;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sno,txn_date,txn_count,amount;


        public MyViewHolder(View view) {
            super(view);

            sno = (TextView) view.findViewById(R.id.sno);
            txn_date = (TextView) view.findViewById(R.id.txn_date);
            txn_count = (TextView) view.findViewById(R.id.txn_count);
            amount = (TextView) view.findViewById(R.id.amount);

        }
    }


    public ReportAdapter(Context context, List<Report> list) {

        this.context=context;
        this.list=list;
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


        holder.sno.setText(String.valueOf(position+1));
        holder.txn_date.setText(list.get(position).txnDate);
        holder.txn_count.setText(list.get(position).txnCount);
        holder.amount.setText(list.get(position).amount);

        if(position%2 == 0)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#FEF7E5"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }




}