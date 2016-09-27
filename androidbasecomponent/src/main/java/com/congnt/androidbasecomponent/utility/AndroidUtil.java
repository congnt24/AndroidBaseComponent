package com.congnt.androidbasecomponent.utility;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by congnt24 on 27/09/2016.
 */

public class AndroidUtil {

    public static boolean isIntentAvailable(Intent intent, Context context) {
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER).size() > 0;
    }

    public static boolean canMakePhoneCall(boolean paramBoolean, Context paramContext) {
        return (isIntentAvailable(new Intent(Intent.ACTION_DIAL), paramContext)) && ((!paramBoolean) || (paramContext.getPackageManager().hasSystemFeature("android.hardware.telephony")));
    }

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(View view) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1 && view.hasFocus()) {
            view.clearFocus();
        }
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }
}
