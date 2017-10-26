package com.ganapathyram.theatre.common;

import android.app.Application;
import android.content.Context;

import com.ganapathyram.theatre.model.Categories;
import com.ganapathyram.theatre.model.Product;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;

/**
 * Created by Prakash on 9/19/2017.
 */

public class GlobalClass extends Application{

    public static String UserId="";
    public static ArrayList<Product> cartList=new ArrayList<>();
    public static String BadgeCount="0";
    public static ArrayList<Categories> categoryList=new ArrayList<>();
    public static String bluetoothStatus=null;
    public static String ApiBaseUrl="http://192.168.0.105:8080/services/";

    public void onCreate() {



        super.onCreate();

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
