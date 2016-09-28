package com.congnt.androidbasecomponent.utility;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipboardManagerUtil {

    public static void setText(Context context, CharSequence text) {
        android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (APILevel.require(11)) {
            ClipboardManager cm = (ClipboardManager) clipboardManager;
            ClipData clip = ClipData.newPlainText("Clipboard", text);
            cm.setPrimaryClip(clip);
        } else {
            clipboardManager.setText(text);
        }
    }

    public static boolean hasText(Context context) {
        android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (APILevel.require(11)) {
            ClipboardManager cm = (ClipboardManager) clipboardManager;
            ClipDescription description = cm.getPrimaryClipDescription();
            ClipData clipData = cm.getPrimaryClip();
            return clipData != null
                    && description != null
                    && (description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN));
        } else {
            return clipboardManager.hasText();
        }
    }

    public static CharSequence getText(Context context) {
        android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (APILevel.require(11)) {
            ClipboardManager cm = (ClipboardManager) clipboardManager;
            ClipDescription description = cm.getPrimaryClipDescription();
            ClipData clipData = cm.getPrimaryClip();
            if (clipData != null
                    && description != null
                    && description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                return clipData.getItemAt(0).getText();
            else
                return null;
        } else {
            return clipboardManager.getText();
        }
    }
}