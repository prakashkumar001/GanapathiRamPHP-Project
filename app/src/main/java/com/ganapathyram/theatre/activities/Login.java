package com.ganapathyram.theatre.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ganapathyram.theatre.MainActivity;
import com.ganapathyram.theatre.R;


/**
 * Created by Prakash on 9/14/2017.
 */

public class Login extends AppCompatActivity {

    Button bOne, bTwo, bThree, bFour, bFive, bSix, bSeven, bEight, bNine, bZero, submit;
    ImageView iv_delete;
    TextView one,two,three,four;
    Button login;
    String pinNumber;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initialize();



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

        login=(Button)findViewById(R.id.login);

        iv_delete = (ImageView) findViewById(R.id.iv_delete);

        TextView marque = (TextView) this.findViewById(R.id.marquee_text);
        marque.setSelected(true);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!one.getText().toString().equals("*") && !two.getText().toString().equals("*")
                        && !three.getText().toString().equals("*") && !four.getText().toString().equals("*")
                        ) {
                    String otp = one.getText().toString() + two.getText().toString() + three.getText().toString() +
                            four.getText().toString() ;

                    Intent intent = new Intent(
                            Login.this,
                            Home.class);

                    Login.this.startActivity(intent);

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


}
