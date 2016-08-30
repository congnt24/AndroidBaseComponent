package com.congnt.androidbasecomponent.view.searchview;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by congn_000 on 8/24/2016.
 */
public class DimensionUtil {
    public static float dpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static float pxToDp(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float spToPx(Context context, float sp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }
}
