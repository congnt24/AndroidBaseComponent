package com.congnt.androidbasecomponent.network;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.congnt.androidbasecomponent.utility.PermissionUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Downloader extends AsyncTask<String, Void, String> {
    Context context;

    public Downloader(Context context) {
        this.context = context;
    }

    static String download(Context mcontext, String urls) {
        String fileName = Uri.parse(urls).getLastPathSegment();
        if (new File(mcontext.getFilesDir() + "/" + fileName).exists()) {
            return fileName;
        }
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urls);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                int fileLength = connection.getContentLength();
                inputStream = connection.getInputStream();
                outputStream = mcontext.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = inputStream.read(data)) > 0) {
                    total += count;
                    if (fileLength > 0)
                        outputStream.write(data, 0, count);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
            if (connection != null)
                connection.disconnect();
        }
        return fileName;
    }

    @Override
    protected void onPreExecute() {
        PermissionUtil.getInstance(context).requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected String doInBackground(String... params) {
        return download(context, params[0]);
    }
}
