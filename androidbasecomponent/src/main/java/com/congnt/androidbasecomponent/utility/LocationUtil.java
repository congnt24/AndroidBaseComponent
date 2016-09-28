package com.congnt.androidbasecomponent.utility;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by congnt24 on 27/09/2016.
 */

public class LocationUtil {
    public static boolean isGpsEnable(Context context) {
        return isGpsEnable(context, null);
    }

    public static boolean isGpsEnable(Context context, LocationManager location) {
        if (location == null)
            location = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return location.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isGpsOnlyMode(Context context) {
        LocationManager location = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean bool1 = location.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean bool2 = location.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return (bool1) && (!bool2);
    }

    public static boolean isGpsOrWirelessOnlyMode(Context context) {
        LocationManager location = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean bool1 = location.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean bool2 = location.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return ((bool1) && (!bool2)) || ((!bool1) && (bool2));
    }

    public static List<Address> getAddress(Context context, Location location) {
        Geocoder geo = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return addresses;
        }
    }

    public static String getAddress(List<Address> addresses, int levelDetail) {
        int maxLine = addresses.get(0).getMaxAddressLineIndex();
        String str = "";
        levelDetail = levelDetail < maxLine ? levelDetail : maxLine;
        for (int i = 0; i < levelDetail; i++) {
            str += addresses.get(0).getAddressLine(i);
            if (i < levelDetail - 1) str += ", ";
        }
        return str;
    }
}
