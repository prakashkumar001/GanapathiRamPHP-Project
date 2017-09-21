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
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Prakash on 9/21/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {




    GlobalClass global;
    ImageLoader loader;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,price;
        public ImageView icon;


        public MyViewHolder(View view) {
            super(view);

            icon = (ImageView) view.findViewById(R.id.product_image);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.amount);

        }
    }


    public CartAdapter(Context context) {
        this.context=context;


    }

    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);


        return new CartAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CartAdapter.MyViewHolder holder, final int position) {


        holder.title.setText(global.cartList.get(position).getProductname());
        holder.icon.setImageResource(global.cartList.get(position).getProductimage());
        holder.price.setText(global.cartList.get(position).getTotalprice());





    }

    @Override
    public int getItemCount() {
        return global.cartList.size();
    }




}
