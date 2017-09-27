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


    ArrayList<Product> products = new ArrayList<>();

    GlobalClass global;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView price, productname;
        public ImageView product_image,addtocart;
        public TextView quantity;
        public ImageView plus,minus;
        LinearLayout productbg;



        public MyViewHolder(View view) {
            super(view);

            product_image = (ImageView) view.findViewById(R.id.product_image);
            addtocart = (ImageView) view.findViewById(R.id.addtocart);
            productname = (TextView) view.findViewById(R.id.productname);
            price = (TextView) view.findViewById(R.id.price);
            quantity= (TextView) view.findViewById(R.id.quantity);
            plus = (ImageView) view.findViewById(R.id.plus);
            minus = (ImageView) view.findViewById(R.id.minus);
            productbg= (LinearLayout) view.findViewById(R.id.productbg);


        }
    }


    public ProductListAdapter(Context context, ArrayList<Product> productlist) {

        global=new GlobalClass();
        this.context=context;
        this.products=productlist;





    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_items, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final String ruppee=context.getResources().getString(R.string.Rupee_symbol);
        holder.productname.setText(products.get(position).productname);
        holder.price.setText(ruppee+" "+products.get(position).productprice);
        holder.product_image.setImageResource(products.get(position).productimage);
        holder.productbg.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        //holder.quantity.setText(products.get(position).quantity);


       /* if(global.cartList.size()>0)
        {
            for(int i=0;i<global.cartList.size();i++)
            {
                if(global.cartList.get(i).getProductid().equalsIgnoreCase(products.get(position).getProductid()))
                {
                    products.get(i).setSelected(true);
                   holder.addtocart.setImageResource(R.mipmap.add_buy_select);
                   holder.productbg.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                    holder.quantity.setText(String.valueOf(global.cartList.get(i).getQuantity()));
                    holder.price.setText(global.cartList.get(i).getTotalprice());
                }
            }



        }
*/


        if(global.cartList.size()>0)
        {
            for(int i=0;i<global.cartList.size();i++)
            {
                if(global.cartList.get(i).getProductid().equalsIgnoreCase(products.get(position).getProductid()))
                {
                    products.get(i).setSelected(true);
                    holder.addtocart.setImageResource(R.mipmap.add_buy_select);
                    holder.productbg.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                    holder.quantity.setText(String.valueOf(global.cartList.get(i).getQuantity()));
                    holder.price.setText(global.cartList.get(i).getTotalprice());
                }
            }


        }


        holder.addtocart.setOnClickListener(new View.OnClickListener() {
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
        });


        holder.addtocart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if(containsProduct(global.cartList,products.get(position).productid).isProductAvailable)
                {

                    global.cartList.remove(products.get(position));
                    products.get(position).setSelected(false);
                    products.get(position).setQuantity(1);
                    products.get(position).getProductprice();
                    holder.quantity.setText(String.valueOf(products.get(position).getQuantity()));
                    holder.price.setText(String.valueOf(products.get(position).getProductprice()));
                    holder.addtocart.setImageResource(R.mipmap.add_buy_unselect);
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

                ProductAvailable productAvailable=containsProduct(global.cartList,products.get(position).productid);
                if(productAvailable.isProductAvailable)
                {
                    int index=productAvailable.indexofProduct;
                    double b;
                    int values = Integer.parseInt(holder.quantity.getText().toString());
                    values = values + 1;
                    global.cartList.get(index).setQuantity(values);

                    holder.quantity.setText(String.valueOf(global.cartList.get(index).getQuantity()));
                    b = global.cartList.get(index).getQuantity() * Double.parseDouble(global.cartList.get(index).getProductprice());
                    global.cartList.get(index).setTotalprice(String.valueOf(b));

                    holder.price.setText(ruppee + String.valueOf(b));


                }else
                {
                    Toast.makeText(context,"Please Select the Product",Toast.LENGTH_SHORT).show();

                }
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProductAvailable productAvailable=containsProduct(global.cartList,products.get(position).productid);
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
                    b = global.cartList.get(index).getQuantity() * Double.parseDouble(global.cartList.get(index).getProductprice());
                    global.cartList.get(index).setTotalprice(String.valueOf(b));
                    holder.price.setText(ruppee + String.valueOf(b));



                }else
                {
                    Toast.makeText(context,"Please Select the Product",Toast.LENGTH_SHORT).show();

                }
            }
        });



        if(products.get(position).isSelected())
        {
            holder.addtocart.setImageResource(R.mipmap.add_buy_select);
            holder.productbg.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));


        }else
        {
            holder.addtocart.setImageResource(R.mipmap.add_buy_unselect);
            holder.productbg.setBackgroundColor(context.getResources().getColor(android.R.color.white));


        }





    }

    @Override
    public int getItemCount() {
        return products.size();
    }



    ProductAvailable containsProduct(List<Product> list, String productid) {
        for (Product item : list) {
            if (item.productid.equals(productid)) {

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
