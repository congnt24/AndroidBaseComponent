package com.congnt.androidbasecomponent.utility;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

/**
 * Created by congnt24 on 24/09/2016.
 */

public class PermissionUtil implements PermissionListener, MultiplePermissionsListener {
    public static PermissionUtil instance;
    private Context context;

    public PermissionUtil(Context context) {
        Dexter.initialize(context);
        this.context = context;
    }

    public static PermissionUtil getInstance(Context context) {
        if (instance == null) {
            instance = new PermissionUtil(context);
        }
        return instance;
    }

    public boolean checkPermission(String permission) {
        if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public boolean checkMultiPermission(String... permissions) {
        for (String str : permissions) {
            if (ActivityCompat.checkSelfPermission(context, str) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    public void requestPermission(String permission) {
        Dexter.checkPermission(this, permission);
    }

    public void requestPermission(PermissionListener listener, String permission) {
        Dexter.checkPermission(listener, permission);
    }

    public void requestPermission(final PermissionListenerGranted listener, String permission) {
        Dexter.checkPermission(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                listener.onPermissionGranted(response);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                onPermissionDenied(response);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                onPermissionRationaleShouldBeShown(permission, token);
            }
        }, permission);
    }

    public void requestPermissions(String... permissions) {
        Dexter.checkPermissions(this, permissions);
    }

    public void requestPermissions(final MultiPermissionListenerGranted listener, String... permissions) {
        Dexter.checkPermissions(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                listener.onPermissionGranted(report);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }, permissions);
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {

    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        Toast.makeText(context, "Permission" + response.getPermissionName() + " was Denied", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
        token.continuePermissionRequest();
    }

    @Override
    public void onPermissionsChecked(MultiplePermissionsReport report) {

    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
        token.continuePermissionRequest();
    }

    public interface PermissionListenerGranted {
        void onPermissionGranted(PermissionGrantedResponse response);
    }

    public interface MultiPermissionListenerGranted {
        void onPermissionGranted(MultiplePermissionsReport response);
    }
}
