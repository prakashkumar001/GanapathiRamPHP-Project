package com.ganapathyram.theatre.adapter;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.activities.DashBoard;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.model.Product;
import com.ganapathyram.theatre.model.ProductAvailable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prakash on 9/19/2017.
 */

public class ProductListAdapter  extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {


    List<com.ganapathyram.theatre.database.Product> products = new ArrayList<>();

    GlobalClass global;
    Context context;
ImageLoader loader;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView price, productname;
        public ImageView product_image;
        public TextView quantity;
        public ImageView plus,minus;
        LinearLayout productbg;



        public MyViewHolder(View view) {
            super(view);

            product_image = (ImageView) view.findViewById(R.id.product_image);
            //addtocart = (ImageView) view.findViewById(R.id.addtocart);
            productname = (TextView) view.findViewById(R.id.productname);
            price = (TextView) view.findViewById(R.id.price);
            quantity= (TextView) view.findViewById(R.id.quantity);
            plus = (ImageView) view.findViewById(R.id.plus);
            minus = (ImageView) view.findViewById(R.id.minus);
            productbg= (LinearLayout) view.findViewById(R.id.productbg);


        }
    }


    public ProductListAdapter(Context context, List<com.ganapathyram.theatre.database.Product> productlist) {

        global=new GlobalClass();
        this.context=context;
        this.products=productlist;
        loader=ImageLoader.getInstance();

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
                .showImageOnLoading(R.drawable.app_icon) // resource or drawable
                .showImageForEmptyUri(R.drawable.app_icon) // resource or drawable
                .showImageOnFail(R.drawable.app_icon) // resource or drawable
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(100)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .build();


        final String ruppee=context.getResources().getString(R.string.Rupee_symbol);
        holder.productname.setText(products.get(position).getProductName());
        holder.price.setText(ruppee+" "+products.get(position).getTotalprice());
       // holder.product_image.setImageResource(products.get(position).getProductimage());
        //holder.quantity.setText(products.get(position).quantity);
        try
        {
            loader.displayImage(global.ApiImageUrl+products.get(position).getProductimage(),holder.product_image,options);

        }catch (Exception e)
        {

        }




        if(global.cartList.size()>0)
        {
           /* for(int i=0;i<global.cartList.size();i++)
            {
                if(global.cartList.get(i).getProductid().equalsIgnoreCase(products.get(position).getProductid()))
                {
                    products.get(i).setSelected(true);
                   // holder.addtocart.setImageResource(R.mipmap.add_buy_select);
                    holder.productbg.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                    holder.quantity.setText(String.valueOf(global.cartList.get(i).getQuantity()));
                    holder.price.setText(global.cartList.get(i).getTotalprice());
                }
            }*/

            ProductAvailable productAvailable=containsProduct(global.cartList,products.get(position).getProductId());
            if(productAvailable.isProductAvailable)
            {
                products.get(position).setIsSelected(true);
                // holder.addtocart.setImageResource(R.mipmap.add_buy_select);
                holder.productbg.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                holder.quantity.setText(String.valueOf(global.cartList.get(productAvailable.indexofProduct).getQuantity()));
                holder.price.setText(ruppee+" "+global.cartList.get(productAvailable.indexofProduct).getTotalprice());
            }else
            {
                products.get(position).setIsSelected(false);
                holder.productbg.setBackgroundColor(context.getResources().getColor(android.R.color.white));
                holder.quantity.setText("1");
                holder.price.setText(ruppee+" "+products.get(position).getTotalprice());






            }



        }else {
            products.get(position).setIsSelected(false);
           // holder.productbg.setBackgroundColor(context.getResources().getColor(android.R.color.white));

        }


       /* holder.addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProductAvailable productAvailable=containsProduct(global.cartList,products.get(position).productid);
                if(productAvailable.isProductAvailable)
                {
                    Toast.makeText(context,"Already Added Please increase the quantity",Toast.LENGTH_SHORT).show();
                }else
                {
                    global.cartList.add(products.get(position));
                    products.get(position).setSelected(true);
                   holder.addtocart.setImageResource(R.mipmap.add_buy_select);
                   holder.productbg.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                   // notifyDataSetChanged();

                }

                int count=global.cartList.size();
                String value= String.valueOf(count);
                global.BadgeCount=value;
                AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(context,R.animator.flip);
                set.setTarget(DashBoard.cartcount);
                DashBoard.cartcount.setText(global.BadgeCount);
                set.start();

            }
        });*/


       /* holder.productbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                ProductAvailable productAvailable=containsProduct(global.cartList,products.get(position).productid);
                if(productAvailable.isProductAvailable)
                {
                    //Toast.makeText(context,"Already Added Please increase the quantity",Toast.LENGTH_SHORT).show();
                }else
                {


                    global.cartList.add(products.get(position));
                    products.get(position).setSelected(true);
                    //holder.addtocart.setImageResource(R.mipmap.add_buy_select);
                    holder.productbg.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                    // notifyDataSetChanged();

                }

                int count=global.cartList.size();
                String value= String.valueOf(count);
                global.BadgeCount=value;
                AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(context,R.animator.flip);
                set.setTarget(DashBoard.cartcount);
                DashBoard.cartcount.setText(global.BadgeCount);
                set.start();


            }
        });*/

        holder.productbg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                ProductAvailable productAvailable=containsProduct(global.cartList,products.get(position).getProductId());

                if(productAvailable.isProductAvailable)
                {

                    global.cartList.remove(productAvailable.indexofProduct);
                    products.get(position).setIsSelected(false);
                    products.get(position).setQuantity(1);
                    products.get(position).setTotalprice(products.get(position).getPrice());
                    holder.quantity.setText(String.valueOf(products.get(position).getQuantity()));
                    holder.price.setText(ruppee+" "+String.valueOf(products.get(position).getPrice()));
                    double gst_amount = (Double.parseDouble(products.get(position).getPrice()) * Double.parseDouble(products.get(position).taxAmount)) / 100;
                     products.get(position).setTaxAmount(String.format("%.2f", gst_amount));
                    holder.productbg.setBackgroundColor(context.getResources().getColor(android.R.color.white));

                    int count=global.cartList.size();
                    String value= String.valueOf(count);
                    global.BadgeCount=value;
                    AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(context,R.animator.flip);
                    set.setTarget(DashBoard.cartcount);
                    DashBoard.cartcount.setText(global.BadgeCount);
                    set.start();                   // holder.productbg.setBackgroundColor(context.getResources().getColor(android.R.color.white));
                   // notifyDataSetChanged();

                }else
                {

                }
                return true;
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProductAvailable productAvailable=containsProduct(global.cartList,products.get(position).getProductId());
                if(productAvailable.isProductAvailable)
                {
                    int index=productAvailable.indexofProduct;
                    double b=0.0;
                    int values = Integer.parseInt(holder.quantity.getText().toString());
                    values = values + 1;
                    global.cartList.get(index).setQuantity(values);

                    holder.quantity.setText(String.valueOf(global.cartList.get(index).getQuantity()));
                    b = global.cartList.get(index).getQuantity() * Double.parseDouble(global.cartList.get(index).getPrice());




                    global.cartList.get(index).setTotalprice(String.valueOf(b));

                    double gst_amount = (Double.parseDouble(global.cartList.get(index).getTotalprice()) * Double.parseDouble( global.cartList.get(index).taxPercent)) / 100;
                    //double gst_amount = ((Double.parseDouble( global.cartList.get(index).getTotalprice()) ) * Double.parseDouble( global.cartList.get(index).taxPercent)) / 100;
                    global.cartList.get(index).setTaxAmount(String.format("%.2f", gst_amount));


                    holder.price.setText(ruppee + String.valueOf(b));


                }else
                {
                    global.cartList.add(products.get(position));
                    products.get(position).setIsSelected(true);
                    //holder.addtocart.setImageResource(R.mipmap.add_buy_select);
                    holder.productbg.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));

                    //Toast.makeText(context,"Please Select the Product",Toast.LENGTH_SHORT).show();
                    int count=global.cartList.size();
                    String value= String.valueOf(count);
                    global.BadgeCount=value;
                    AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(context,R.animator.flip);
                    set.setTarget(DashBoard.cartcount);
                    DashBoard.cartcount.setText(global.BadgeCount);
                    set.start();

                }
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProductAvailable productAvailable=containsProduct(global.cartList,products.get(position).getProductId());
                if(productAvailable.isProductAvailable)
                {
                    int index=productAvailable.indexofProduct;

                    double b;

                    int values = Integer.parseInt(holder.quantity.getText().toString());
                    if (values == 1) {

                    } else {
                        values = values - 1;
                    }

                    global.cartList.get(index).setQuantity(values);

                    holder.quantity.setText(String.valueOf(global.cartList.get(index).getQuantity()));
                    b = global.cartList.get(index).getQuantity() * Double.parseDouble(global.cartList.get(index).getPrice());



                    global.cartList.get(index).setTotalprice(String.valueOf(b));
                    double gst_amount = (Double.parseDouble(global.cartList.get(index).getTotalprice()) * Double.parseDouble( global.cartList.get(index).taxPercent)) / 100;
                    global.cartList.get(index).setTaxAmount(String.format("%.2f", gst_amount));


                    holder.price.setText(ruppee + String.valueOf(b));



                }else
                {
                    Toast.makeText(context,"Please Select the Product",Toast.LENGTH_SHORT).show();

                }
            }
        });



        if(products.get(position).getIsSelected())
        {
            holder.productbg.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));


        }else
        {
            holder.productbg.setBackgroundColor(context.getResources().getColor(android.R.color.white));


        }





    }

    @Override
    public int getItemCount() {
        return products.size();
    }



    ProductAvailable containsProduct(List<com.ganapathyram.theatre.database.Product> list, Long productid) {
        for (com.ganapathyram.theatre.database.Product item : list) {
            if (item.productId.equals(productid)) {

                int index=list.indexOf(item);
               return new ProductAvailable(true,index);

            }
        }

       return new ProductAvailable(false,-1);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
