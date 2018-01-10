package com.ganapathyram.theatre.helper;

import android.content.Context;


import com.ganapathyram.theatre.database.Categories;
import com.ganapathyram.theatre.database.DaoSession;
import com.ganapathyram.theatre.database.IpSettings;
import com.ganapathyram.theatre.database.IpSettingsDao;
import com.ganapathyram.theatre.database.Login;
import com.ganapathyram.theatre.database.LoginDao;
import com.ganapathyram.theatre.database.ParkingDao;
import com.ganapathyram.theatre.database.Product;
import com.ganapathyram.theatre.database.ProductDao;
import com.ganapathyram.theatre.database.UserList;
import com.ganapathyram.theatre.database.UserListDao;
import com.ganapathyram.theatre.database.UserSession;
import com.ganapathyram.theatre.database.UserSessionDao;
import com.ganapathyram.theatre.database.Wifi_BluetoothAddress;
import com.ganapathyram.theatre.database.Wifi_BluetoothAddressDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Creative IT Works on 10-Aug-17.
 */

public class Helper {
    private final DaoSession daoSession;

    Context context;


    public Helper(DaoSession daoSession, Context context) {
        this.daoSession = daoSession;
        this.context = context;
        helper = this;
    }

    public static Helper getHelper() {
        return helper;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public static Helper helper;



    //getMenuItems
    public List<Categories> getCategoryItems() {

        QueryBuilder<Categories> qb = daoSession.queryBuilder(Categories.class);
        return qb.list();

    }

    //getMenuItems
    public List<Product> getProductItems(String categoryId) {

        QueryBuilder<Product> qb = daoSession.queryBuilder(Product.class);
        qb.where(ProductDao.Properties.CategoryUid.eq(categoryId));

        return qb.list();

    }

    //getAddress
    public Wifi_BluetoothAddress getAddress() {

        QueryBuilder<Wifi_BluetoothAddress> qb = daoSession.queryBuilder(Wifi_BluetoothAddress.class);



        return  qb.unique();


    }

    //getAddress
    public UserSession getSession() {

        QueryBuilder<UserSession> qb = daoSession.queryBuilder(UserSession.class);
        qb.orderDesc(UserSessionDao.Properties.Id).limit(1);


        return  qb.unique();


    }


    public UserList getuserName(String userId) {

        QueryBuilder<UserList> qb = daoSession.queryBuilder(UserList.class);
        qb.where(UserListDao.Properties.UserId.eq(userId));
        qb.limit(1);


        return  qb.unique();


    }

    public Login getLogin() {

        QueryBuilder<Login> qb = daoSession.queryBuilder(Login.class);
        qb.limit(1);


        return  qb.unique();


    }



    //getAddress
    public List<UserSession> getSessionList() {

        QueryBuilder<UserSession> qb = daoSession.queryBuilder(UserSession.class);
        qb.orderDesc(UserSessionDao.Properties.Id);
        qb.where(UserSessionDao.Properties.StartTime.ge(getDateTime()));
        return  qb.list();


    }

    public IpSettings getIpAddress() {

        QueryBuilder<IpSettings> qb = daoSession.queryBuilder(IpSettings.class);
        qb.limit(1);
        return  qb.unique();


    }


    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy hh:mm:aa");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
