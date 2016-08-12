package com.entropia.kartierpartner;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GpsLogging extends AppCompatActivity
{
    double lat = 0;
    double lon = 0;
    float accuracy = 0;
    DBHelper mydb;
    Button mButton;
    Integer selection;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_logging);

        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener mlocListener = new MyLocationListener();

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(GpsLogging.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(GpsLogging.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                showMessageOKCancel(getString(R.string.allowLocation),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(GpsLogging.this,
                                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(GpsLogging.this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 1, 1, mlocListener);
        mydb = new DBHelper(this);
        mButton = (Button) findViewById(R.id.gpsButton);

        Bundle b = getIntent().getExtras();
        if (b!= null){
            selection = (Integer)b.getInt("idToSearch");
        }

        mydb = new DBHelper(this);
        Cursor cursor1 = mydb.getData(selection);
        cursor1.moveToFirst();
        String name1 = cursor1.getString(cursor1.getColumnIndex(DBHelper.KP_COLUMN_NAME));
        TextView textView2 = (TextView) findViewById(R.id.nameOfFormation);
        textView2.setText(name1);

    }

    /* Class My Location Listener */
    public class MyLocationListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location loc) {
            lat = loc.getLatitude();
            lon = loc.getLongitude();
            accuracy = loc.getAccuracy();

            TextView textView = (TextView) findViewById(R.id.gpsPosition);
            textView.setText(getString(R.string.current_location) + " \n" +
                    getString(R.string.lat) + " = " + lat +"\n"+
                    getString(R.string.lon) + " = " + lon +"\n"+
                    getString(R.string.acc) + " = " + accuracy +" m");
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
        }
    }
    public void saveGps (View view) {
        Bundle b = getIntent().getExtras();
        if (b!= null){
            selection = (Integer)b.getInt("idToSearch");
        }
        if(0==lat){
            Toast.makeText(this, R.string.noGps, Toast.LENGTH_SHORT).show();
        }
        else {
            mydb = new DBHelper(this);
            Cursor cursor1 = mydb.getData(selection);
            cursor1.moveToFirst();
            String formation = cursor1.getString(cursor1.getColumnIndex(DBHelper.KP_COLUMN_NAME));
            mydb.insertMeasurement(formation, "NONE", lon, lat, 0, 0, accuracy, "SimpleLogging");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(GpsLogging.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}