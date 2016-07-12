package com.ta.demo.heytaxi;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class TaxiMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int REQUEST_LOCATION=2;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(mGoogleApiClient==null){
            mGoogleApiClient=new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMapInfomation();
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_LOCATION);
        }else{
            setupMyLocation();
        }


    }

    private void setupMyLocation() {
        //noinspection MissingPermission
        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                LocationManager locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                String provider="gps";
//                //noinspection MissingPermission
//                Location location=locationManager.getLastKnownLocation(provider);
                Criteria criteria=new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                String provider=locationManager.getBestProvider(criteria,true);
                //noinspection MissingPermission
                Location location=locationManager.getLastKnownLocation(provider);
                if(location !=null){
                    Log.i("LOCATION",location.getLatitude()+"/"+location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(),location.getLongitude())
                            ,19));
                }
                return true;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case REQUEST_LOCATION:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //noinspection MissingPermission
                    setupMyLocation();
                }else{

                }
                break;
        }

    }

    private void setMapInfomation() {

        // 設定地圖類型
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // 地圖上顯示建築物
        // 注意：zoom的設定要 >=17才會顯示建築物
        mMap.setBuildingsEnabled(true);
        // Google地圖使用者操作界面功能設定
        UiSettings ui = mMap.getUiSettings();
        // 開啟/關閉縮放鈕
        ui.setZoomControlsEnabled(true);
        // 開啟/關閉地圖捲動手勢
        ui.setScrollGesturesEnabled(true);
        // 開啟/關閉地圖縮放手勢
        ui.setZoomGesturesEnabled(true);
        // 開啟/關閉地圖傾斜手勢
        ui.setTiltGesturesEnabled(true);
        // 開啟/關閉地圖旋轉手勢
        ui.setRotateGesturesEnabled(true);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //noinspection MissingPermission
        Location location=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        ///setMapInfomation();
        if(location !=null){
            Log.i("LOCATION",location.getLatitude()+"/"+location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(),location.getLongitude())
                    ,19));
            LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("My Home"));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}
