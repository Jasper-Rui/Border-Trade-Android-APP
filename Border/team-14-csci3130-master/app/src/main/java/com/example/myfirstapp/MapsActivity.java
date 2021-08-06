package com.example.myfirstapp;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Serializable escolas;
    private ProgressDialog dialog;
    private Circle mCircle;
    private Marker mMarker;

    LocationManager manager;
    LatLng currentLocation;

    //test inside
    double mLatitude = 44.6363;
    double mLongitude = -63.5692;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000,
                5, listener);

        mapFragment.getMapAsync(this);
    }

    private void drawMarkerWithCircle(LatLng position) {
        double radiusInMeters = 300.0;  // increase decrease this distancce as per your requirements
        int strokeColor = 0xffff0000; //red outline
        int shadeColor = 0x44ff0000; //opaque red fill

        CircleOptions circleOptions = new CircleOptions()
                .center(position)
                .radius(radiusInMeters)
                .fillColor(shadeColor)
                .strokeColor(strokeColor)
                .strokeWidth(8);
        mCircle = googleMap.addCircle(circleOptions);

       /* MarkerOptions markerOptions = new MarkerOptions().position(position);
        mMarker = googleMap.addMarker(markerOptions);*/
    }


    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(22.9809425, 72.6051794);
       /* googleMap.addMarker(new MarkerOptions().position(sydney));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        // Changing map type
//        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Showing / hiding your current location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);

        // Enable / Disable zooming controls
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // Enable / Disable my location button
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Enable / Disable Compass icon
        googleMap.getUiSettings().setCompassEnabled(true);

        // Enable / Disable Rotate gesture
        googleMap.getUiSettings().setRotateGesturesEnabled(true);

        // Enable / Disable zooming functionality
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        // Bundle extra = getIntent().getBundleExtra("extra");
        //ArrayList<Escolas> objects = (ArrayList<Escolas>) extra.getSerializable("array");

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude, mLongitude), 14));

        drawMarkerWithCircle(new LatLng(mLatitude, mLongitude));

      /*  try {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude, mLongitude), 14));

            LatLng latLng = new LatLng(mLatitude, mLongitude);
            drawMarkerWithCircle(latLng);


            googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    float[] distance = new float[2];

                        *//*
                        Location.distanceBetween( mMarker.getPosition().latitude, mMarker.getPosition().longitude,
                                mCircle.getCenter().latitude, mCircle.getCenter().longitude, distance);
                                *//*

                    Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                            mCircle.getCenter().latitude, mCircle.getCenter().longitude, distance);

                    float radius = Float.parseFloat(mCircle.getRadius() + "");
                    float distanceInMeter = Float.parseFloat(distance[0] + "");
                    if (distanceInMeter > radius) {
                        Toast.makeText(getBaseContext(), "You are Outside of the circle, Distance from center: " + distance[0] + " Radius: " + mCircle.getRadius(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Your are Inside the circle, Distance from center: " + distance[0] + " Radius: " + mCircle.getRadius(), Toast.LENGTH_LONG).show();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

            float[] distance = new float[2];
            Location.distanceBetween(currentLocation.latitude, currentLocation.longitude,
                    mCircle.getCenter().latitude, mCircle.getCenter().longitude, distance);

            float radius = Float.parseFloat(mCircle.getRadius() + "");
            float distanceInMeter = Float.parseFloat(distance[0] + "");
            if (distanceInMeter > radius) {
                Toast.makeText(getBaseContext(), "You are Outside of the circle, Distance from center: " + distance[0] + " Radius: " + mCircle.getRadius(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "Your are Inside the circle, Distance from center: " + distance[0] + " Radius: " + mCircle.getRadius(), Toast.LENGTH_LONG).show();
            }

            if (googleMap != null) {
               /* if (isFirstLaunch) {
                    MarkerOptions mOptions = new MarkerOptions().position(currentLocation);
                    myMarker = mMap.addMarker(mOptions);
                    isFirstLaunch = false;
                } else {
                    myMarker.setPosition(currentLocation);
                }*/
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16.0f));
            }
        }


    };

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
