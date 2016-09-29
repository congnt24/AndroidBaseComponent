package com.congnt.androidbasecomponent.data;

import android.Manifest;
import android.content.Context;

import com.congnt.androidbasecomponent.utility.PermissionUtil;
import com.karumi.dexter.listener.PermissionGrantedResponse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Decompress {
    /**
     * Unzip a zip file to a folder
     *
     * @param zipFile:         Filename
     * @param targetDirectory: Folder path
     */
    public static void unzip(Context context, final File zipFile, final File targetDirectory, boolean permitRequestPermission) {
        if (PermissionUtil.getInstance(context).checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            unzip(zipFile, targetDirectory);
        }else{
            if (permitRequestPermission) {
                PermissionUtil.getInstance(context).requestPermission(new PermissionUtil.PermissionListenerGranted() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        unzip(zipFile, targetDirectory);
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    private static void unzip(File zipFile, File targetDirectory) {
        try {
            ZipInputStream zis = new ZipInputStream(
                    new BufferedInputStream(new FileInputStream(zipFile)));
            try {
                ZipEntry ze;
                int count;
                byte[] buffer = new byte[8192];
                while ((ze = zis.getNextEntry()) != null) {
                    File file = new File(targetDirectory, ze.getName());
                    File dir = ze.isDirectory() ? file : file.getParentFile();
                    if (!dir.isDirectory() && !dir.mkdirs())
                        throw new FileNotFoundException("Failed to ensure directory: " +
                                dir.getAbsolutePath());
                    if (ze.isDirectory())
                        continue;
                    FileOutputStream fout = new FileOutputStream(file);
                    try {
                        while ((count = zis.read(buffer)) != -1)
                            fout.write(buffer, 0, count);
                    } finally {
                        fout.close();
                    }
                }
                //Delete file zip after decompress
                zipFile.delete();
            } finally {
                zis.close();
            }
        } catch (IOException e) {
        }
    }

}
