package com.example.coivd_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {






    SharedPreferences sharedpreferences;

    String userType;



    boolean isLoggedIn;


    LottieAnimationView lottie_splash;
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splassh_activity);



        lottie_splash=findViewById( R.id.lottie_splash );
        text=findViewById( R.id.text );

        Animation myanim= AnimationUtils.loadAnimation( this,R.anim.mytransition );
        text.startAnimation(myanim);
        //lottie_splash.startAnimation(myanim);


        //to check current user types
        sharedpreferences= getSharedPreferences("user_details", MODE_PRIVATE);

        isLoggedIn=sharedpreferences.getBoolean("isLoggedIn",false);




        Thread timer= new Thread(){
            public void run(){
                try{
                    sleep( 3000 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    //to check current user types
                    userType=sharedpreferences.getString("usertype","");


//  Toast.makeText(SplashActivity.this,userType+"",Toast.LENGTH_SHORT).show();

                    //if user login then navigation process
                    if(isLoggedIn){



                        startActivity(new Intent(SplashActivity.this, Nav_menu.class));
                        finish();




                    } else {

                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                        //   Toast.makeText(SplashActivity.this,"User not Login !",Toast.LENGTH_SHORT).show();


                    }



                }
            }
        };
        timer.start();
    }
}