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

public class ReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<Report> list = new ArrayList<>();

    GlobalClass global;
    ImageLoader loader;
    Context context;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;


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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=null;
        if (viewType == TYPE_HEADER) {
             itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.report_header, parent, false);

            return new ReportAdapter.HeaderViewHolder(itemView);
        } else if (viewType == TYPE_ITEM) {
             itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.report_items, parent, false);

            return new ReportAdapter.MyViewHolder(itemView);
        }

        throw new RuntimeException("No match for " + viewType + ".");


    }



    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Report report = list.get(position);

        if(holder instanceof HeaderViewHolder){
            ((HeaderViewHolder) holder).headerTitle.setText("");
                }else if(holder instanceof MyViewHolder){
            ((MyViewHolder) holder).sno.setText(String.valueOf(position+1));
            ((MyViewHolder) holder).txn_date.setText(report.txnDate);
            ((MyViewHolder) holder).txn_count.setText(report.txnCount);
            ((MyViewHolder) holder).amount.setText(report.amount);
        }

       /* holder.sno.setText(String.valueOf(position+1));
        holder.txn_date.setText(list.get(position).txnDate);
        holder.txn_count.setText(list.get(position).txnCount);
        holder.amount.setText(list.get(position).amount);
*/
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

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }
    private boolean isPositionHeader(int position) {
        if(list.get(position).header.equalsIgnoreCase("false"))
        {
            return false ;
        }else
        {
            return true;
        }

    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        public TextView headerTitle;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerTitle = (TextView)itemView.findViewById(R.id.header);
        }
    }
}