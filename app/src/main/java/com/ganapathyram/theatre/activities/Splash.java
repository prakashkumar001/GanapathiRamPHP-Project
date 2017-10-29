package com.ganapathyram.theatre.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.interfaces.NetworkConnection;
import com.ganapathyram.theatre.utils.GifImageView;
import com.ganapathyram.theatre.utils.InternetPermissions;

import static com.ganapathyram.theatre.helper.Helper.getHelper;


/**
 * Created by Prakash on 9/14/2017.
 */

public class Splash extends AppCompatActivity implements NetworkConnection {
    final int SPLASH_DISPLAY_TIME = 2000;
    GifImageView gifImageView;
    GlobalClass global;
    LinearLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        global=(GlobalClass)getApplicationContext();
        layout=(LinearLayout)findViewById(R.id.layout);



        if(getHelper().getLogin()!=null)
        {
            global.UserId=String.valueOf(getHelper().getLogin().pin);
        }

          new Handler().postDelayed(new Runnable() {
            public void run() {

                Splash.this.finish();
                overridePendingTransition(R.anim.fadeinact,
                        R.anim.fadeoutact);

                Intent mainIntent = new Intent(
                        Splash.this,
                        Login.class);

                Splash.this.startActivity(mainIntent);




            }
        }, SPLASH_DISPLAY_TIME);
    }

    @Override
    public void isInternetOn(String message) {

    }
}
