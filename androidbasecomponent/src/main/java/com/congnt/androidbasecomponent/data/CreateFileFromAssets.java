package com.congnt.androidbasecomponent.data;

import android.Manifest;
import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

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

public class CreateFileFromAssets {
    public static final int FILE = 1;
    public static final int FOLDER = 2;

    /**
     * handle func extract file from assets to app dir
     *
     * @param fileOrFolder: Extract file or folder
     * @param path:         name of file or folder
     */
    public static void createFileFromAssets(final Context context, final int fileOrFolder, final String path) {
        Dexter.initialize(context);
        Dexter.checkPermission(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                switch (fileOrFolder) {
                    case FILE:
                        createOneFile(context, path);
                        break;
                    case FOLDER:
                        createFileFromPath(context, path);
                        break;
                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(context, "Permission was Denied", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * Create all file from a folder in assets folder to app dir folder
     * CreateFileFromAssets.getInstance().initialize(context).CreateFileFromPath("data");
     *
     * @param path: folder name in assets folder
     * @return this
     */
    private static void createFileFromPath(Context context, String path) {
        AssetManager assetManager = context.getAssets();
        File dir = new File(context.getFilesDir().getPath() + "/" + path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String[] listFile = new String[0];
        try {
            listFile = assetManager.list(path);
            for (int i = 0; i < listFile.length; i++) {
                createOneFile(context, path + "/" + listFile[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create A file from assets folder to app dir
     * CreateFileFromAssets.getInstance().initialize(context).CreateOneFile("data0.zip");
     *
     * @param fileName: file name in assets folder
     * @return this
     */
    private static void createOneFile(Context context, String fileName) {
        String path = context.getFilesDir().getPath() + "/" + fileName;
        File file = new File(path);
        if (file.exists()) {
            return;
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        AssetManager assetManager = context.getAssets();
        try {
            inputStream = assetManager.open(fileName);
            file.createNewFile();
            int read = 0;
            outputStream = new FileOutputStream(file);
            byte[] offer = new byte[1024];
            while ((read = inputStream.read(offer)) != -1) {
                outputStream.write(offer, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

}
