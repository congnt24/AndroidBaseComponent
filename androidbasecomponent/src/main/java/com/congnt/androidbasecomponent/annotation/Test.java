package com.congnt.androidbasecomponent.annotation;

import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by congn_000 on 8/18/2016.
 */
public class Test {

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
