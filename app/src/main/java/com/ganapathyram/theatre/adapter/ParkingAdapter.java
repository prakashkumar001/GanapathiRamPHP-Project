package com.ganapathyram.theatre.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.bluetooth.utils.ESCPOSDriver;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.model.Parking;
import com.ganapathyram.theatre.model.Product;
import com.ganapathyram.theatre.parking.ParkingDashboard;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Prakash on 9/21/2017.
 */

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.MyViewHolder> {


    ArrayList<Parking> list = new ArrayList<>();

    GlobalClass global;
    ImageLoader loader;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView icon;


        public MyViewHolder(View view) {
            super(view);

            icon = (ImageView) view.findViewById(R.id.icon);
            title = (TextView) view.findViewById(R.id.title);

        }
    }


    public ParkingAdapter(Context context, ArrayList<Parking> parkinglist) {

        this.context=context;
        list=parkinglist;
        global=new GlobalClass();

    }

    @Override
    public ParkingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.parking_items, parent, false);


        return new ParkingAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ParkingAdapter.MyViewHolder holder, final int position) {


        holder.title.setText(list.get(position).name);
        holder.icon.setImageResource(list.get(position).icon);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParkingDashboard) context).onItemClick(position);
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }




}
