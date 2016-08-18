package com.congnt.androidbase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.congnt.androidbasecomponent.annotation.ActivityAnnotation;
import com.congnt.androidbasecomponent.annotation.FullScreen;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        CreateFileFromAssets.getInstance().initialize(this).CreateFileFromAssets(CreateFileFromAssets.FILE, "data");
        getAnnotation();
    }


    public void getAnnotation() {
        Class<ActivityAnnotation> obj = ActivityAnnotation.class;
//        if (obj.isAnnotationPresent(FullScreen.class)) {
//            Annotation annotation = obj.getAnnotation(FullScreen.class);
//            FullScreen testerInfo = (FullScreen) annotation;
//            System.out.printf("%Value :%s", testerInfo.value());
//            System.out.printf("%nTags :");
//        }


        // Process @Test
        for (Field field : obj.getDeclaredFields()) {

            // if method is annotated with @Test
            if (field.isAnnotationPresent(FullScreen.class)) {
                Annotation annotation = field.getAnnotation(FullScreen.class);
                FullScreen fullScreen = (FullScreen) annotation;
                Log.d("AAAA", "AAAAAAAAAAAAAAAAAAAA AAAAAAA VALUE = "+fullScreen.value());
            }

        }
    }
}
