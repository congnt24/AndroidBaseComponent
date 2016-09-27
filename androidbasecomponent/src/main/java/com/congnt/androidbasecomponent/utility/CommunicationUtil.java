package com.congnt.androidbasecomponent.utility;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;

import java.util.ArrayList;


/**
 * Created by congnt24 on 27/09/2016.
 */

public class CommunicationUtil {

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
}
