package com.congnt.androidbasecomponent.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by congn_000 on 8/18/2016.
 */

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //on class level
public @interface FullScreen {
    int value();
}
