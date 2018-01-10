package com.ganapathyram.theatre.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.database.IpSettings;
import com.ganapathyram.theatre.database.UserSession;
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



        /*if(getHelper().getLogin()!=null)
        {
            global.UserId=String.valueOf(getHelper().getLogin().pin);
        }
*/
          new Handler().postDelayed(new Runnable() {
            public void run() {

                Splash.this.finish();
                overridePendingTransition(R.anim.fadeinact,
                        R.anim.fadeoutact);

                if(getHelper().getSession()==null)
                {
                    Intent mainIntent = new Intent(
                            Splash.this,
                            Login.class);

                    Splash.this.startActivity(mainIntent);

                }else if(getHelper().getSession()!=null)
                {
                    UserSession session=getHelper().getSession();
                    if(session.getSessionId().equalsIgnoreCase(" "))
                    {
                        Intent mainIntent = new Intent(
                                Splash.this,
                                Login.class);

                        Splash.this.startActivity(mainIntent);

                    }else
                    {
                        Intent mainIntent = new Intent(
                                Splash.this,
                                Home.class);

                        Splash.this.startActivity(mainIntent);

                    }

                }





            }
        }, SPLASH_DISPLAY_TIME);
    }

    @Override
    public void isInternetOn(String message) {

    }


    @Override
    protected void onResume() {
        super.onResume();

        if(getHelper().getIpAddress()==null)
        {
            IpSettings ipSettings=new IpSettings();
            ipSettings.setId(Long.parseLong("1"));
            ipSettings.setBaseIpAdress("192.168.1.114");
            getHelper().getDaoSession().insertOrReplace(ipSettings);

            global.deFaultBaseUrl="http://"+getHelper().getIpAddress().getBaseIpAdress();
        }else
        {
            global.deFaultBaseUrl="http://"+getHelper().getIpAddress().getBaseIpAdress();
        }
    }
}
