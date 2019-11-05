package com.manish.firstnote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class Splash_Activity extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences=getSharedPreferences("FIRENOTESDATA", Context.MODE_PRIVATE);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //here we need to check if status is true then we are directly open to homescreen othwerwise login activity
                if((sharedPreferences.getBoolean("LOGINSTATUS",false))){
                    Intent intent=new Intent(Splash_Activity.this,MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent=new Intent(Splash_Activity.this,LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        },3000);
    }
}
