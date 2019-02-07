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

import static com.ganapathyram.theatre.helper.Helper.getHelper;

/**
 * Created by Prakash on 9/19/2017.
 */

public class GlobalClass extends Application{

    public static String UserId="";
    public static String UserName="";
    public static String deFaultBaseUrl="";//http://192.168.0.114
    public static ArrayList<com.ganapathyram.theatre.database.Product> cartList=new ArrayList<>();
    public static String BadgeCount="0";
    public static String bluetoothStatus=null;
   // public static String ApiBaseUrl=":8080/services/";    //192.168.1.110
    public static String ApiImageUrl="/theatre/snacks/";
    public static String ApiBaseUrl="/theatre/";


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
