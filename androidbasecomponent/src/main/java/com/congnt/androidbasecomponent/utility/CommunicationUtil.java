package com.congnt.androidbasecomponent.utility;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by congnt24 on 27/09/2016.
 */

public class CommunicationUtil {

    private static final String INTENT_TYPE_TEXT = "text/plain";
    private static int MAX_SMS_MESSAGE_LENGTH = 160;

    public static void sendSmsAuto(String phonenumber, String message) {
        SmsManager manager = SmsManager.getDefault();
        int length = message.length();
        if (length > MAX_SMS_MESSAGE_LENGTH) {
            ArrayList<String> messagelist = manager.divideMessage(message);
            manager.sendMultipartTextMessage(phonenumber, null, messagelist, null, null);
        } else {
            manager.sendTextMessage(phonenumber, null, message, null, null);
        }
    }

    public static void sendSms(Context context, String phoneNumber, String message) {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", phoneNumber);
        smsIntent.putExtra("sms_body", message);
        context.startActivity(Intent.createChooser(smsIntent, "SMS:"));
    }

    public static void callTo(Context context, String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        context.startService(callIntent);
    }

    public static void dialTo(Context context, String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        context.startService(callIntent);
    }

    //Email

    /**
     * Direct open email app
     *
     * @param context
     * @param subject
     * @param text
     */
    public static void openMailApp1(Context context, String dialogTitle, String subject, String text) {
        ResolveInfo resolveInfo = null;
        if (PackageUtil.isInstalled(context, PackageUtil.GMAIL)) {
            resolveInfo = PackageUtil.getResolveInfo(context, PackageUtil.GMAIL, Intent.CATEGORY_APP_EMAIL);
        } else if (PackageUtil.isInstalled(context, PackageUtil.INBOX)) {
            resolveInfo = PackageUtil.getResolveInfo(context, PackageUtil.INBOX, Intent.CATEGORY_APP_EMAIL);
        } else if (PackageUtil.isInstalled(context, PackageUtil.MAIL)) {
            resolveInfo = PackageUtil.getResolveInfo(context, PackageUtil.MAIL, Intent.CATEGORY_APP_EMAIL);
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType(INTENT_TYPE_TEXT);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        if (resolveInfo != null) {
            intent.setComponent(new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name));
        } else {
            if (dialogTitle.isEmpty()) {
                dialogTitle = "Email: ";
            }
            context.startActivity(Intent.createChooser(intent, dialogTitle));
        }
    }

    /**
     * List all email app in your device
     *
     * @param context
     * @param dialogTitle
     * @param subject
     * @param text
     * @deprecated using
     */
    public static void openMailApp(Context context, String dialogTitle, String subject, String text) {
        List<Intent> targetShareIntents = new ArrayList<>();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        List<ResolveInfo> listResolveInfos = context.getPackageManager().queryIntentActivities(shareIntent, 0);
        if (!listResolveInfos.isEmpty()) {
            for (ResolveInfo resInfo : listResolveInfos) {
                String packageName = resInfo.activityInfo.packageName;
                if (isPackageOfMailApp(packageName)) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    intent.putExtra(Intent.EXTRA_TEXT, text);
                    intent.setPackage(packageName);
                    targetShareIntents.add(intent);
                }
            }
            if (!targetShareIntents.isEmpty()) {
                Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), dialogTitle);
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                context.startActivity(chooserIntent);
            }
        }
    }

    private static boolean isPackageOfMailApp(String packageName) {
        return packageName.contains("com.google.android.gm") || packageName.contains("android.mail");
    }

    public static void openMailApp(Context context, String dialogTitle, String email, String subject, String text) {
        Intent intentMail = new Intent(Intent.ACTION_SENDTO);
        intentMail.setData(Uri.parse(email));
        intentMail.putExtra(Intent.EXTRA_SUBJECT, subject);
        intentMail.putExtra(Intent.EXTRA_TEXT, text);
        try {
            context.startActivity(Intent.createChooser(intentMail, dialogTitle));
        } catch (ActivityNotFoundException ex) {
        }
    }

    public static void shareAll(Context context, String dialogTitle, String content) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setType(INTENT_TYPE_TEXT);
        context.startActivity(Intent.createChooser(intent, dialogTitle));
    }
}
