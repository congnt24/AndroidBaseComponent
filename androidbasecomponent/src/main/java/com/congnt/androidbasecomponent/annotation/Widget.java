package com.congnt.androidbasecomponent.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by congn on 8/19/2016.
 */
@Inherited
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE) //on class level
public @interface Widget {
    int value() default 0;
}
