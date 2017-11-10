package com.ganapathyram.theatre.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ganapathyram.theatre.MainActivity;
import com.ganapathyram.theatre.R;
import com.ganapathyram.theatre.common.GlobalClass;
import com.ganapathyram.theatre.utils.InternetPermissions;
import com.ganapathyram.theatre.utils.WSUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.R.attr.data;
import static com.ganapathyram.theatre.helper.Helper.getHelper;


/**
 * Created by Prakash on 9/14/2017.
 */

public class Login extends AppCompatActivity {

    Button bOne, bTwo, bThree, bFour, bFive, bSix, bSeven, bEight, bNine, bZero, submit;
    ImageView iv_delete;
    TextView one, two, three, four;
    Button login;
    String pinNumber;
    GlobalClass global;
    LinearLayout layout;
    Spinner select_class;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        global = (GlobalClass) getApplicationContext();
        initialize();


    }

    @Override
    protected void onResume() {
        super.onResume();

       /* if(!new InternetPermissions(Login.this).isInternetOn())
        {
            Snackbar.make(layout, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            }).show();

        }*/
    }

    private void initialize() {
        one = (TextView) findViewById(R.id.num_one);
        two = (TextView) findViewById(R.id.num_two);
        three = (TextView) findViewById(R.id.num_three);
        four = (TextView) findViewById(R.id.num_four);


        bOne = (Button) findViewById(R.id.btn_one);
        bTwo = (Button) findViewById(R.id.btn_two);
        bThree = (Button) findViewById(R.id.btn_three);
        bFour = (Button) findViewById(R.id.btn_four);
        bFive = (Button) findViewById(R.id.btn_five);
        bSix = (Button) findViewById(R.id.btn_six);
        bSeven = (Button) findViewById(R.id.btn_seven);
        bEight = (Button) findViewById(R.id.btn_eight);
        bNine = (Button) findViewById(R.id.btn_nine);
        bZero = (Button) findViewById(R.id.btn_zero);

        login = (Button) findViewById(R.id.login);
        layout=(LinearLayout)findViewById(R.id.layout);

        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        select_class=(Spinner)findViewById(R.id.select_class);

        TextView marque = (TextView) this.findViewById(R.id.marquee_text);
        marque.setSelected(true);

        ArrayList<String> classes=new ArrayList<>();
        classes.add("First Class");
        classes.add("Balcony");
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(Login.this,android.R.layout.simple_list_item_1,classes);
        select_class.setAdapter(adapter);




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!one.getText().toString().equals("*") && !two.getText().toString().equals("*")
                        && !three.getText().toString().equals("*") && !four.getText().toString().equals("*")
                        ) {
                    String otp = one.getText().toString() + two.getText().toString() + three.getText().toString() +
                            four.getText().toString();


                    Login(otp);

                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter Correct PIN", Toast.LENGTH_SHORT).show();
                }
            }
        });


        bOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (one.getText().toString().equals("*")) {
                    one.setText("1");
                } else if (two.getText().toString().equals("*")) {
                    two.setText("1");
                } else if (three.getText().toString().equals("*")) {
                    three.setText("1");
                } else if (four.getText().toString().equals("*")) {
                    four.setText("1");
                }

                one.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                three.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                four.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


            }
        });
        bTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (one.getText().toString().equals("*")) {
                    one.setText("2");
                } else if (two.getText().toString().equals("*")) {
                    two.setText("2");
                } else if (three.getText().toString().equals("*")) {
                    three.setText("2");
                } else if (four.getText().toString().equals("*")) {
                    four.setText("2");
                }

                one.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                three.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                four.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


            }
        });
        bThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (one.getText().toString().equals("*")) {
                    one.setText("3");
                } else if (two.getText().toString().equals("*")) {
                    two.setText("3");
                } else if (three.getText().toString().equals("*")) {
                    three.setText("3");
                } else if (four.getText().toString().equals("*")) {
                    four.setText("3");
                }

                one.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                three.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                four.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            }
        });
        bFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (one.getText().toString().equals("*")) {
                    one.setText("4");
                } else if (two.getText().toString().equals("*")) {
                    two.setText("4");
                } else if (three.getText().toString().equals("*")) {
                    three.setText("4");
                } else if (four.getText().toString().equals("*")) {
                    four.setText("4");
                }
                one.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                three.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                four.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            }
        });

        bFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (one.getText().toString().equals("*")) {
                    one.setText("5");
                } else if (two.getText().toString().equals("*")) {
                    two.setText("5");
                } else if (three.getText().toString().equals("*")) {
                    three.setText("5");
                } else if (four.getText().toString().equals("*")) {
                    four.setText("5");
                }
                one.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                three.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                four.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


            }
        });

        bSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (one.getText().toString().equals("*")) {
                    one.setText("6");
                } else if (two.getText().toString().equals("*")) {
                    two.setText("6");
                } else if (three.getText().toString().equals("*")) {
                    three.setText("6");
                } else if (four.getText().toString().equals("*")) {
                    four.setText("6");
                }
                one.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                three.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                four.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            }
        });

        bSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (one.getText().toString().equals("*")) {
                    one.setText("7");
                } else if (two.getText().toString().equals("*")) {
                    two.setText("7");
                } else if (three.getText().toString().equals("*")) {
                    three.setText("7");
                } else if (four.getText().toString().equals("*")) {
                    four.setText("7");
                }

                one.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                three.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                four.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


            }
        });

        bEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (one.getText().toString().equals("*")) {
                    one.setText("8");
                } else if (two.getText().toString().equals("*")) {
                    two.setText("8");
                } else if (three.getText().toString().equals("*")) {
                    three.setText("8");
                } else if (four.getText().toString().equals("*")) {
                    four.setText("8");
                }
                one.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                three.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                four.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


            }
        });

        bNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (one.getText().toString().equals("*")) {
                    one.setText("9");
                } else if (two.getText().toString().equals("*")) {
                    two.setText("9");
                } else if (three.getText().toString().equals("*")) {
                    three.setText("9");
                } else if (four.getText().toString().equals("*")) {
                    four.setText("9");
                }

                one.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                three.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                four.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


            }
        });

        bZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (one.getText().toString().equals("*")) {
                    one.setText("0");
                } else if (two.getText().toString().equals("*")) {
                    two.setText("0");
                } else if (three.getText().toString().equals("*")) {
                    three.setText("0");
                } else if (four.getText().toString().equals("*")) {
                    four.setText("0");
                }

                one.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                two.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                three.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                four.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


            }
        });

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!four.getText().toString().equals("*")) {
                    four.setText("*");
                } else if (!three.getText().toString().equals("*")) {
                    three.setText("*");
                } else if (!two.getText().toString().equals("*")) {
                    two.setText("*");
                } else if (!one.getText().toString().equals("*")) {
                    one.setText("*");
                }

                one.setTextColor(getResources().getColor(android.R.color.darker_gray));
                two.setTextColor(getResources().getColor(android.R.color.darker_gray));
                three.setTextColor(getResources().getColor(android.R.color.darker_gray));
                four.setTextColor(getResources().getColor(android.R.color.darker_gray));

            }
        });


    }


    public void Login(final String pinNumber) {
        class LoginServer extends AsyncTask<String, String, String> {
            ProgressDialog dialog;
            String response = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(Login.this);
                dialog.setMessage(getString(R.string.loading));
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(String[] params) {
                try {


                    String requestURL = global.ApiBaseUrl + "login";
                    WSUtils utils = new WSUtils();

                    JSONObject object = new JSONObject();
                    JSONObject user = new JSONObject();
                    user.put("userId", pinNumber);
                    user.put("password", pinNumber);

                    object.put("user", user);
                    object.put("venueId", "gprtheatre");


                    response = utils.responsedetailsfromserver(requestURL, object.toString());

                    System.out.println("SERVER REPLIED:" + response);
                    //{"status":"success","message":"Registration Successful","result":[],"statusCode":200}
                    // {"status":"success","message":"Logged in Successfully","result":{"statusCode":4},"statusCode":200}
                } catch (Exception ex) {
                    Log.i("ERROR", "ERROR" + ex.toString());
                }

                return response;
            }


            @Override
            protected void onPostExecute(String o) {

                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

                if (o != null || !o.equalsIgnoreCase("null")) {
                    try {
                        JSONObject object = new JSONObject(o);
                        String payload = object.getString("payload");

                        if (payload.equalsIgnoreCase("success")) {

                            com.ganapathyram.theatre.database.Login login=new  com.ganapathyram.theatre.database.Login();
                            login.pin=Long.parseLong(pinNumber);
                            login.status=payload;
                            getHelper().getDaoSession().insertOrReplace(login);
                            global.UserId=pinNumber;

                            Intent intent = new Intent(
                                    Login.this,
                                    Home.class);

                            Login.this.startActivity(intent);


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }
        }
        new LoginServer().execute();
    }


}
