package com.entropia.kartierpartner;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class MeasureBoundary extends AppCompatActivity implements SensorEventListener {

    // define the display assembly compass picture
    private ImageView image;

    // record the compass picture angle turned
    private float currentDegree = 0f;

    // device sensor manager
    private SensorManager mSensorManager;

    TextView tvHeading;
    ArrayList<Integer> selection = new ArrayList<Integer>();
    DBHelper mydb;

    double lat = 0;
    double lon = 0;
    float accuracy = 0;
    float degree;
    String name1;
    String name2;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_boundary);

        /* Use the LocationManager class to obtain GPS locations */
        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener mlocListener = new MyLocationListenerBound();
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MeasureBoundary.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MeasureBoundary.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                showMessageOKCancel(getString(R.string.allowLocation),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MeasureBoundary.this,
                                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(MeasureBoundary.this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 1, 1, mlocListener);

        Bundle b = getIntent().getExtras();
        if (b!= null){
           selection = b.getIntegerArrayList("array_list");
            }

        mydb = new DBHelper(this);
        Cursor cursor1 = mydb.getData(selection.get(0));
        cursor1.moveToFirst();
        name1 = cursor1.getString(cursor1.getColumnIndex(DBHelper.KP_COLUMN_NAME));

        Cursor cursor2 = mydb.getData(selection.get(1));
        cursor2.moveToFirst();
        name2 = cursor2.getString(cursor2.getColumnIndex(DBHelper.KP_COLUMN_NAME));

        TextView formation1 = (TextView) findViewById(R.id.formation1);
        formation1.setText(name1);
        TextView formation2 = (TextView) findViewById(R.id.formation2);
        formation2.setText(name2);


        image = (ImageView) findViewById(R.id.compass_customview);
        // TextView that will tell the user what degree is he heading
        tvHeading = (TextView) findViewById(R.id.showAngle);

        // initialize your android device sensor capabilities
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
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);

        tvHeading.setText("Heading: " + Float.toString(degree));

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;
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
            mydb.insertMeasurement(name1, name2, lon, lat, 360-currentDegree*(-1), 0, accuracy, "MeasureBoundary");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MeasureBoundary.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}