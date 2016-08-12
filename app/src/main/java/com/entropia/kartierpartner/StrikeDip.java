package com.entropia.kartierpartner;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class StrikeDip extends AppCompatActivity implements SensorEventListener {

    private ImageView image;

    private float currentDegree = 0f;

    private SensorManager mSensorManager;

    TextView tvHeading;
    Integer selection;
    DBHelper mydb;

    double lat = 0;
    double lon = 0;
    float accuracy = 0;
    float dip = 0;
    float roll = 0;
    float dipAngle = 0;
    String name1;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strike_dip);
        
        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener mlocListener = new MyLocationListenerBound();

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(StrikeDip.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(StrikeDip.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                showMessageOKCancel(getString(R.string.allowLocation),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(StrikeDip.this,
                                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(StrikeDip.this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 1, 1, mlocListener);

        Bundle b = getIntent().getExtras();
        if (b!= null){
            selection = (Integer)b.getInt("idToSearch");
        }

        mydb = new DBHelper(this);
        Cursor cursor1 = mydb.getData(selection);
        cursor1.moveToFirst();
        name1 = cursor1.getString(cursor1.getColumnIndex(DBHelper.KP_COLUMN_NAME));


        image = (ImageView) findViewById(R.id.compass_customview);
        tvHeading = (TextView) findViewById(R.id.showAngle);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    public class MyLocationListenerBound implements LocationListener
    {
        @Override
        public void onLocationChanged(Location loc) {
            lat = loc.getLatitude();
            lon = loc.getLongitude();
            accuracy = loc.getAccuracy();

            NumberFormat formatter = new DecimalFormat("#0.000");
            TextView textView = (TextView) findViewById(R.id.gpsPositionBound);
            textView.setText(getString(R.string.current_location) + " \n" +
                    getString(R.string.lat) + " = " + formatter.format(lat) +"\n"+
                    getString(R.string.lon) + " = " + formatter.format(lon) +"\n"+
                    getString(R.string.acc) + " = " + accuracy +" m");
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);
        float pitch = Math.round(event.values[1]);
        float roll = Math.round(event.values[2]);
        if(roll<=0){
            dip = degree+90;
            dipAngle = Math.abs(roll);
            if(dip>360){
                dip = dip-360;
                dipAngle = Math.abs(roll);
            }
        }
        else{
            dip = degree-90;
            dipAngle = Math.abs(roll);
            if(dip<0){
                dip = 360-dip;
                dipAngle = Math.abs(roll);
            }
            if(dip>360){
                dip = 270+90-(dip-360);
                dipAngle = Math.abs(roll);
            }
        }
        String orientationString = getString(R.string.strike) +": " +Float.toString(degree) +"\n" + getString(R.string.dipDir) +": " +Float.toString(dip) + "\n" +getString(R.string.dip) +": " +Float.toString(roll) +"\n" +getString(R.string.pitch) +": " +Float.toString(pitch) ;
        tvHeading.setText(orientationString);
        if(pitch>-4&&pitch<4){
            tvHeading.setTextColor(Color.parseColor("#FF24551F"));
        }
        else{
            tvHeading.setTextColor(Color.RED);
        }
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        ra.setDuration(210);
        ra.setFillAfter(true);
        image.startAnimation(ra);
        currentDegree = -degree;
        float dipAngle = roll+3;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

    public void saveBoundary (View view) {
        if(0==lat){
            Toast.makeText(this, R.string.noGps, Toast.LENGTH_SHORT).show();
        }
        else {
            mydb = new DBHelper(this);
            mydb.insertMeasurement(name1, "NONE", lon, lat, dip, dipAngle, accuracy, "StrikeDip");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(StrikeDip.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}