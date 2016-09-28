package com.congnt.androidbasecomponent.utility;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * PackageUtil helps to handle methods related to package.
 *
 * @author Leonardo Taehwan Kim
 */
public class PackageUtil {

    public static final String FACEBOOK = "com.facebook.katana";
    public static final String TWITTER = "com.twitter.android";
    public static final String GOOGLE_PLUS = "com.google.android.apps.plus";
    public static final String GMAP = "com.google.android.apps.maps";
    public static final String YOUTUBE = "com.google.android.apps.youtube";
    public static final String GMAIL = "com.google.android.gm";
    public static final String INBOX = "com.google.android.apps.inbox";
    public static final String MAIL = "android.mail";
    public static final String PINTEREST = "com.pinterest";
    public static final String TUMBLR = "com.tumblr";
    public static final String FANCY = "com.thefancy.app";
    public static final String FLIPBOARD = "flipboard.app";
    public static final MessageFormat MARKET_APP_URL_TEMPLATE = new MessageFormat("market://details?id={0}", Locale.ENGLISH);
    public static final MessageFormat PLAYSTORE_APP_URL_TEMPLATE = new MessageFormat("https://play.google.com/store/apps/details?id={0}", Locale.ENGLISH);


    //PACKAGE
    public static boolean isIntentAvailable(Intent intent, Context context) {
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER).size() > 0;
    }

    public static boolean canMakePhoneCall(boolean paramBoolean, Context paramContext) {
        return (isIntentAvailable(new Intent(Intent.ACTION_DIAL), paramContext)) && ((!paramBoolean) || (paramContext.getPackageManager().hasSystemFeature("android.hardware.telephony")));
    }

    public static ResolveInfo getResolveInfo(Context context, String packageName, String intentCategory) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent();
        intent.setPackage(packageName);
        intent.addCategory(intentCategory);
        return packageManager.resolveActivity(intent, PackageManager.GET_RESOLVED_FILTER);
    }

    public static boolean isInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void openPlayStore(Context context) {
        openPlayStore(context, context.getPackageName());
    }

    public static void openPlayStore(Context context, String packageName) {
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_APP_URL_TEMPLATE.format(new String[]{packageName})));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (ActivityNotFoundException anfe) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLAYSTORE_APP_URL_TEMPLATE.format(new String[]{packageName})));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } finally {
            if (intent != null)
                context.startActivity(intent);
        }
    }
}