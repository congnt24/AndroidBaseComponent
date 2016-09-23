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
@Target(ElementType.TYPE) //on class level
public @interface Activity {
    enum ActionBarType {
        ACTIONBAR_NONE, ACTIONBAR_CUSTOM
    }
    enum AnimationType {
        ANIM_NONE, ANIM_BOTTOM_TO_TOP, ANIM_TOP_TO_BOTTOM, ANIM_RIGHT_TO_LEFT, ANIM_LEFT_TO_RIGHT
    }
    boolean fullscreen() default false;
    AnimationType transitionAnim() default AnimationType.ANIM_NONE;
    ActionBarType actionbarType() default ActionBarType.ACTIONBAR_NONE;
    int mainLayoutId() default 0;
    boolean enableSearch() default false;
}
