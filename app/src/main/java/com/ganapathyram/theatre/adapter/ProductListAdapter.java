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
import com.ganapathyram.theatre.model.Product;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Prakash on 9/19/2017.
 */

public class ProductListAdapter  extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {


    ArrayList<Product> products = new ArrayList<>();

    GlobalClass global;
    ImageLoader loader;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView offerprice, productname, sellerprice;
        public ImageView product_image,addtocart;
        public TextView add;

        public MyViewHolder(View view) {
            super(view);

            product_image = (ImageView) view.findViewById(R.id.image);
            addtocart = (ImageView) view.findViewById(R.id.addtocart);
            productname = (TextView) view.findViewById(R.id.productname);

        }
    }


    public ProductListAdapter(Context context, ArrayList<Product> productlist) {


        loader = ImageLoader.getInstance();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_items, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.placeholder) // resource or drawable
                .showImageForEmptyUri(R.mipmap.placeholder) // resource or drawable
                .showImageOnFail(R.mipmap.placeholder) // resource or drawable
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(100)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .build();










    }

    @Override
    public int getItemCount() {
        return 10;
    }




}
