package com.congnt.androidbasecomponent.utility;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by congn_000 on 9/20/2016.
 */

public class ImageUtil {
    public static String filePath;

    /**
     * Save bitmap to file
     *
     * @param bitmap
     * @param folder   Parent Dir
     * @param fileName
     * @return
     */
    public static void createFileFromBitmap(final Context context, final Bitmap bitmap, final File folder, final String fileName) {
        if (PermissionUtil.getInstance(context).checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            filePath = createBitmapFile(bitmap, folder, fileName);
        }
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

    public static Bitmap scaleBitmap(Context ctx, Bitmap source, float scale) {
        int w = (int) (scale * source.getWidth());
        int h = (int) (scale * source.getHeight());
        Bitmap photo = Bitmap.createScaledBitmap(source, w, h, true);
        return photo;
    }

    public Bitmap rotate(Bitmap bm, int rotateAngle) {
        if (rotateAngle % 360 == 0) {
            return bm;
        }
        Matrix mtx = new Matrix();
        mtx.postRotate(rotateAngle);
        bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), mtx, true);
        return bm;
    }
}
