package com.entropia.kartierpartner;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class OverviewFragment extends Fragment implements OnMapReadyCallback {
    private SupportMapFragment fragment;
    private GoogleMap map;

    public static OverviewFragment newInstance() {
        return new OverviewFragment();
    }

    public OverviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager mapFragment = getChildFragmentManager();
        fragment = (SupportMapFragment) mapFragment.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            mapFragment.beginTransaction().replace(R.id.map, fragment).commit();
        }
    }


        @Override
    public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            int h;
            int i;
            double sLat;
            double sLon;
            double sDir;
            String sName1;
            String sName2;
            double sAcc;
            double sDip;
            LatLng markerPosition;
            DBHelper mydb;
            mydb = new DBHelper(getActivity());
            int test = mydb.numberOfMeasurements();
            if(test>0) {
                ArrayList<String> types = new ArrayList<String>();
                types.add("SimpleLogging");
                types.add("MeasureBoundary");
                types.add("StrikeDip");
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (h = 0; h < types.size(); h++) {
                    mydb = new DBHelper(getActivity());
                    ArrayList measurements = mydb.getFormationMeasurements(types.get(h));
                    ArrayList measurements2 = mydb.getFormationMeasurements2(types.get(h));
                    ArrayList measurementsLat = mydb.getAllMeasurementsLat(types.get(h));
                    ArrayList measurementsLon = mydb.getAllMeasurementsLon(types.get(h));
                    ArrayList measurementsDir = mydb.getAllMeasurementsDir(types.get(h));
                    ArrayList measurementsDip = mydb.getAllMeasurementsDip(types.get(h));
                    ArrayList measurementsAcc = mydb.getAllMeasurementsAcc(types.get(h));

                    for (i = 0; i < measurements.size(); i++) {
                        //Get position of what the DB is and get the latitude and longitude
                        sLat = (Double) measurementsLat.get(i);
                        sLon = (Double) measurementsLon.get(i);
                        sDir = (Double) measurementsDir.get(i);
                        sName1 = (String) measurements.get(i);
                        sName2 = (String) measurements2.get(i);
                        sDip = (Double) measurementsDip.get(i);
                        sAcc = (Double) measurementsAcc.get(i);
                        Double sStrike;

                        markerPosition = new LatLng(sLat, sLon);
                        builder.include(markerPosition);
                        if (0 == h) {
                            map.addMarker(new MarkerOptions().position(markerPosition).title((String) measurements.get(i)).snippet(getString(R.string.lat) +": " + sLat + "\n" +getString(R.string.lon)+ ": " + sLon +"\n" +getString(R.string.acc) + ": " +sAcc + " m").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        } else {
                            if (1 == h) {
                                map.addMarker(new MarkerOptions().position(markerPosition).title(getString(R.string.boundary)).snippet(getString(R.string.formationLeft) + ": " +sName1 + "\n" +getString(R.string.formationRight) +": " +sName2 +"\n" +getString(R.string.lat) +": " +sLat + "\n" +getString(R.string.lon) +": " +sLon +"\n" +getString(R.string.strike) +": " +sDir +"°\n" +getString(R.string.acc) +": " +sAcc +" m").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_boundary)).rotation((float) sDir));
                            } else {
                                if(sDir<90){
                                    sStrike = 360-sDir;
                                }
                                else {
                                    sStrike = (Double) sDir - 90;
                                }
                                map.addMarker(new MarkerOptions().position(markerPosition).title((String) measurements.get(i)).snippet(getString(R.string.lat) +": " + sLat + "\n" +getString(R.string.lon)+ ": " + sLon +"\n" +getString(R.string.strike) +": " +sStrike  +"\n" +getString(R.string.dipDir) +": " +sDir  +"\n" +getString(R.string.dip) +": " +sDip +"\n" +getString(R.string.acc) + ": " +sAcc + " m").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_strike_dip)).rotation((float) sDir));
                            }
                        }
                    }
                }
                final LatLngBounds bounds = builder.build();
                map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30));
                    }
                });
            }


            map.setMyLocationEnabled(true);
            map.getUiSettings().setMapToolbarEnabled(false);

            map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    Context context = getActivity();

                    LinearLayout info = new LinearLayout(context);
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(context);
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(context);
                    snippet.setTextColor(Color.GRAY);
                    snippet.setText(marker.getSnippet());

                    info.addView(title);
                    info.addView(snippet);

                    return info;
                }
            });
        }

    @Override
    public void onResume() {
        super.onResume();
        if (map == null) {
            fragment.getMapAsync(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}