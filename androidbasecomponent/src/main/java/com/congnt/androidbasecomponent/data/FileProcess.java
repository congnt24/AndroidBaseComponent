package com.congnt.androidbasecomponent.data;

import android.Manifest;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.congnt.androidbasecomponent.utility.LogUtil;
import com.congnt.androidbasecomponent.utility.PermissionUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by congn_000 on 9/20/2016.
 */

public class FileProcess {
    public static String filePath;
    /**
     * Save bitmap to file
     * @param bitmap
     * @param folder Parent Dir
     * @param fileName
     * @return
     */
    public static void createFileFromBitmap(final Context context, final Bitmap bitmap, final File folder, final String fileName){
        PermissionUtil.getInstance(context).requestPermission(new PermissionUtil.PermissionListenerGranted() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                filePath = createBitmapFile(bitmap, folder, fileName);
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private static String createBitmapFile(Bitmap bmp, File dest, String fileName) {
        String result = null;
        File f = new File(dest, fileName);
        if (f.exists()) {
            return f.getAbsolutePath();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your
            // Bitmap
            // instance
            // PNG is a loss less format, the compression factor (100) is ignored
            result = f.getAbsolutePath();
        } catch (Exception e) {
            LogUtil.e("Error save bitmap", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                LogUtil.e("Error close stream", e);
            }
        }
        return result;
    }
}
