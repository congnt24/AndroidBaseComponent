package com.congnt.androidbasecomponent.Awesome;

import android.content.Context;

import com.congnt.androidbasecomponent.data.CreateFileFromAssets;
import com.congnt.androidbasecomponent.data.Decompress;

import java.io.File;

/**
 * Created by congn_000 on 8/22/2016.
 */
public class AwesomeData {

    public static void executeDecompress(Context context, File zipFile, File targetDirectory){
        Decompress.unzipPermission(context, zipFile, targetDirectory);
    }

    public static void executeFileFromAssets(Context context,int fileOrFolder, String path){
        CreateFileFromAssets.createFileFromAssets(context, fileOrFolder, path);
    }
}
