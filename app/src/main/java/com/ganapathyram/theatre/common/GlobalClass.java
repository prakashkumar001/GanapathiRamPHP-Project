package com.ganapathyram.theatre.common;

import android.app.Application;
import android.content.Context;

import com.ganapathyram.theatre.database.DaoMaster;
import com.ganapathyram.theatre.database.DaoSession;
import com.ganapathyram.theatre.helper.Helper;
import com.ganapathyram.theatre.model.Product;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;

/**
 * Created by Prakash on 9/19/2017.
 */

public class GlobalClass extends Application{

    public static String UserId="";
    public static ArrayList<com.ganapathyram.theatre.database.Product> cartList=new ArrayList<>();
    public static String BadgeCount="0";
    public static String bluetoothStatus=null;
    public static String ApiBaseUrl="http://192.168.1.5:8080/services/";
    public static String ApiImageUrl="http://192.168.1.5:8080/images/";
    Database db;
    public DaoSession daoSession;
    public void onCreate() {



        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "grcinemas_db");

        // db = helper.getWritableDb();
        db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        new Helper(daoSession, this);

        initImageLoader(getApplicationContext());

    }
    public static void initImageLoader(Context context) {



        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)

                .threadPriority(Thread.NORM_PRIORITY - 2)

                .denyCacheImageMultipleSizesInMemory()

                .discCacheFileNameGenerator(new Md5FileNameGenerator())

                .tasksProcessingOrder(QueueProcessingType.LIFO)

                .build();



        ImageLoader.getInstance().init(config);

    }

}
