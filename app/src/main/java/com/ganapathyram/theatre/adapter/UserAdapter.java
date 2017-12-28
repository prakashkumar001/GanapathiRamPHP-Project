package com.ganapathyram.theatre.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.activities.AdminDashboard;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.model.Users;
import com.ganapathyram.theatre.utils.WSUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Creative IT Works on 28-Dec-17.
 */

public class UserAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<Users> userList;
    Context context;
    public  UserAdapter(Context context,ArrayList<Users> userList)
    {
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.userList=userList;
        this.context=context;
    }
    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView( int i, View view, ViewGroup viewGroup) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.spinner_item, null);
        holder.textview=(TextView) rowView.findViewById(R.id.textview);

        holder.textview.setText(userList.get(i).name);


        return rowView;
    }

    public class Holder
    {
        TextView textview;

    }



}
