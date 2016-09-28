package com.congnt.androidbasecomponent.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.congnt.androidbasecomponent.Awesome.AwesomeFragment;
import com.congnt.androidbasecomponent.R;
import com.congnt.androidbasecomponent.utility.LocationUtil;
import com.congnt.androidbasecomponent.utility.NetworkUtil;
import com.congnt.androidbasecomponent.utility.PermissionUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class MapFragment extends AwesomeFragment implements OnMapReadyCallback, LocationListener {
    private GoogleMap mMap;
    private LocationManager locationManager;
    private String provider;
    private TextView tv_address;
    private SupportMapFragment mapFragment;
    private Location location;
    private String[] locationPermission = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private Context context;

    public MapFragment() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map;
    }

    @Override
    protected void initAll(View rootView) {
        context = getActivity();
        tv_address = (TextView) rootView.findViewById(R.id.uLocationAddress);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Check GPS
        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (PermissionUtil.getInstance(context).checkMultiPermission(locationPermission)) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setScrollGesturesEnabled(false);
            provider = locationManager.getBestProvider(new Criteria(), true);
            location = locationManager.getLastKnownLocation(provider);
            if (location == null) {
                if (NetworkUtil.isNetworkConnected(context)) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }
            if (location != null) {
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(provider, 1000, 1, this);
        }
    }

    public void movingCamera(Location location) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                .zoom(16)                   // Sets the zoom
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void updateLocation(final Location location) {
        new AsyncTask<Void, Void, List<Address>>() {
            @Override
            protected List<Address> doInBackground(Void... params) {
                return LocationUtil.getAddress(context, location);
            }

            @Override
            protected void onPostExecute(List<Address> addresses) {
                if (addresses.isEmpty()) {
                    tv_address.setText("Waiting for location");
                } else {
                    tv_address.setText(LocationUtil.getAddress(addresses, 4));
                    movingCamera(location);
                }
            }
        }.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (PermissionUtil.getInstance(context).checkMultiPermission(locationPermission)) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        updateLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
