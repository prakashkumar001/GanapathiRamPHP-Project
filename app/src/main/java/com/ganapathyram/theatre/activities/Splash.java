package com.ganapathyram.theatre.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.utils.GifImageView;


/**
 * Created by Prakash on 9/14/2017.
 */

public class Splash extends AppCompatActivity {
    final int SPLASH_DISPLAY_TIME = 2000;
    GifImageView gifImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

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
}
