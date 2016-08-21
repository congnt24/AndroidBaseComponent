package com.congnt.androidcustomview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.congnt.androidbasecomponent.annotation.Activity;

@Activity(layoutId = R.layout.activity_main2)
public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
}
