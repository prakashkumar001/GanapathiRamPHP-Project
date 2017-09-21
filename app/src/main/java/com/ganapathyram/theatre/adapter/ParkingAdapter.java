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
import com.ganapathyram.theatre.model.Parking;
import com.ganapathyram.theatre.model.Product;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Prakash on 9/21/2017.
 */

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.MyViewHolder> {


    ArrayList<Parking> list = new ArrayList<>();

    GlobalClass global;
    ImageLoader loader;

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


        list=parkinglist;

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





    }

    @Override
    public int getItemCount() {
        return list.size();
    }




}
