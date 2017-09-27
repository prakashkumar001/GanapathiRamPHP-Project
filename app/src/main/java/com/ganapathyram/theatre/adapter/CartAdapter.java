package com.ganapathyram.theatre.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.activities.DashBoard;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.model.Parking;
import com.ganapathyram.theatre.model.Product;
import com.ganapathyram.theatre.model.ProductAvailable;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prakash on 9/21/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {




    GlobalClass global;
    ImageLoader loader;
    Context context;
    String rupee;
    Dialog cartdialog;
    int indexpos=-1;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,price;
        public ImageView icon,remove;


        public MyViewHolder(View view) {
            super(view);

            icon = (ImageView) view.findViewById(R.id.product_image);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.amount);
            remove = (ImageView) view.findViewById(R.id.removeicon);

        }
    }


    public CartAdapter(Context context,Dialog cartdialog) {
        this.context=context;
        this.cartdialog=cartdialog;
        rupee = context.getResources().getString(R.string.Rupee_symbol);


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

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.remove.setImageResource(R.mipmap.remove_select);
                showRemoveDialog(position);


            }
        });





    }

    @Override
    public int getItemCount() {
        return global.cartList.size();
    }


    public double totalvalue()
    {
        double totalValue=0.0;
        for(int i=0;i<global.cartList.size();i++)
        {
            String price=global.cartList.get(i).getProductprice();

            double value= Double.parseDouble(price) * global.cartList.get(i).getQuantity();
            totalValue=totalValue + value;

        }

        return totalValue;
    }


    public void showRemoveDialog(final int position) {

        // custom dialog
        final Dialog dialog = new Dialog(context, R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.remove_alert);
        dialog.getWindow().setGravity(Gravity.CENTER);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button remove=(Button)dialog.findViewById(R.id.remove);
        final ImageView close=(ImageView)dialog.findViewById(R.id.iv_close);
        TextView productcount=(TextView)dialog.findViewById(R.id.productcount);


        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.show();
        dialog.getWindow().setLayout((8 * width) / 10, (8 * height) / 10);




        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                CartAdapter adapter=new CartAdapter(context,cartdialog);
                DashBoard.cartview.setAdapter(adapter);
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Product product=global.cartList.get(position);
               String id= product.getProductid();

                ProductAvailable productAvailable=containsProduct(DashBoard.productList,id);
                if(productAvailable.isProductAvailable)
                {
                    product.setSelected(false);
                    DashBoard.productList.set(indexpos,product);
                    DashBoard.adapter.notifyDataSetChanged();
                }*/
                global.cartList.remove(position);



                dialog.dismiss();
                notifyDataSetChanged();
                CartAdapter adapter=new CartAdapter(context,cartdialog);
                DashBoard.cartview.setAdapter(adapter);

                ProductListAdapter adapter1=new ProductListAdapter(context,DashBoard.productList);
                DashBoard.productListView.setAdapter(adapter1);


                DashBoard.totalprice.setText(rupee+" "+String.valueOf(totalvalue()));

                if(totalvalue()==0.0)
                {
                    cartdialog.dismiss();

                }
                DashBoard.cartcount.setText(String.valueOf(global.cartList.size()));



            }
        });




        dialog.show();



    }

    ProductAvailable containsProduct(List<Product> list, String productid) {
        for (Product item : list) {
            if (item.productid.equals(productid)) {

                indexpos=list.indexOf(item);
                return new ProductAvailable(true,indexpos);

            }
        }

        return new ProductAvailable(false,-1);
    }

}
