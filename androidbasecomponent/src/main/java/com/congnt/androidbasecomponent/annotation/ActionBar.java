package com.congnt.androidbasecomponent.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by congn on 8/20/2016.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //on class level
public @interface ActionBar {
    enum ActionbarType {
        MATERIAL_SEARCH, FLOATING_SEARCH
    }
    ActionbarType actionbarType() default ActionbarType.MATERIAL_SEARCH;
    String leftText() default "";
    String centerText() default "";
    String rightText() default "";
    int leftDrawableId() default 0;
    int rightDrawableId() default 0;
}
