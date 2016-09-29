package com.congnt.androidbasecomponent.utility;

import android.Manifest;
import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by congnt24 on 29/09/2016.
 */

public class FileUtil {

    /**
     * Creates the specified directory, along with all parent paths if necessary
     *
     * @param directory directory to be created
     */
    public static void mkdirs(File directory) throws IOException {
        if (directory.exists()) {
            // file exists and *is* a directory
            if (directory.isDirectory()) {
                return;
            }
            // file exists, but is not a directory - delete it
            if (!directory.delete()) {
                throw new IOException("Cannot Delete");
            }
        }
        // doesn't exist. Create one
        if (!directory.mkdirs() && !directory.isDirectory()) {
            throw new IOException("Cannot make dir");
        }
    }

    /**
     * Renames the source file to the target file. If the target file exists, then we attempt to
     * delete it. If the delete or the rename operation fails, then we raise an exception
     *
     * @param source the source file
     * @param target the new 'name' for the source file
     * @throws IOException
     */
    public static void rename(File source, File target) {
        // delete the target first - but ignore the result
        target.delete();
        if (source.renameTo(target)) {
            return;
        }
    }

    /**
     * It'll get all Files in a parentDir and return it
     * You need to check Permission using PermissionUtil to avoid crash
     *
     * @return List<File> or null if not have permission or parentDir doesn't exist
     */
    public static List<File> getListFile(File parentDir) {
        return getListFile(parentDir, "");
    }

    /**
     * It'll get all Files (with end == engWith) in a parentDir and return it
     * You need to check Permission using PermissionUtil to avoid crash
     *
     * @param endWith ".mp3", ".jpg"...
     * @return List<File> or null if not have permission or parentDir doesn't exist
     */
    public static List<File> getListFile(File parentDir, String endWith) {
        if (!parentDir.exists()) return null;
        List<File> listFile = new ArrayList<>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                listFile.addAll(getListFile(file, endWith));
            } else {
                if (file.getName().endsWith(endWith)) listFile.add(file);
            }
        }
        return listFile;
    }

}
