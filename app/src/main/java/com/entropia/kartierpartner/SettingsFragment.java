package com.entropia.kartierpartner;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;

public class SettingsFragment extends Fragment {
    DBHelper mydb;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    public SettingsFragment() {
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnexport = (Button) getActivity().findViewById(R.id.btnexport);
        Button btnmail = (Button) getActivity().findViewById(R.id.btnmail);
        Button btnreset = (Button) getActivity().findViewById(R.id.btnreset);

        //Save to sdCard
        btnexport.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                int hasWritePermission = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        showMessageOKCancel(getString(R.string.allowWrite),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(getActivity(),
                                                new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                });
                        return;
                    }
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    return;
                }

                try {
                    mydb = new DBHelper(getActivity());
                    Cursor c = mydb.getExport();
                    int rowcount = 0;
                    int colcount = 0;
                    File sdCardDir = new File(Environment.getExternalStorageDirectory() + "/kartierpartner");
                    if (!sdCardDir.exists()) {
                        sdCardDir.mkdirs();
                    }
                    String filename = "kartierpartner_db_export.csv";
                    File saveFile = new File(sdCardDir, filename);

                    FileWriter fw = new FileWriter(saveFile);

                    BufferedWriter bw = new BufferedWriter(fw);
                    rowcount = c.getCount();
                    colcount = c.getColumnCount();
                    if (rowcount > 0) {
                        c.moveToFirst();

                        for (int i = 0; i < colcount; i++) {
                            if (i != colcount - 1) {

                                bw.write(c.getColumnName(i) + ",");

                            } else {

                                bw.write(c.getColumnName(i));

                            }
                        }
                        bw.newLine();

                        for (int i = 0; i < rowcount; i++) {
                            c.moveToPosition(i);

                            for (int j = 0; j < colcount; j++) {
                                if (j != colcount - 1)
                                    bw.write(c.getString(j) + ",");
                                else
                                    bw.write(c.getString(j));
                            }
                            bw.newLine();
                        }
                        bw.flush();
                        Toast.makeText(getActivity(), getString(R.string.exportSuccess), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), getString(R.string.errorExport), Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();

                } finally {

                }
            }
        });

        // Send via Email

        btnmail.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                int hasWritePermission = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        showMessageOKCancel(getString(R.string.allowWrite),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(getActivity(),
                                                new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                });
                        return;
                    }
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    return;
                }

                try {
                    mydb = new DBHelper(getActivity());
                    Cursor c = mydb.getExport();
                    int rowcount = 0;
                    int colcount = 0;
                    File sdCardDir = new File(Environment.getExternalStorageDirectory() + "/kartierpartner");
                    if (!sdCardDir.exists()) {
                        sdCardDir.mkdirs();
                    }
                    String filename = "kartierpartner_db_export.csv";
                    File saveFile = new File(sdCardDir, filename);

                    FileWriter fw = new FileWriter(saveFile);

                    BufferedWriter bw = new BufferedWriter(fw);
                    rowcount = c.getCount();
                    colcount = c.getColumnCount();
                    if (rowcount > 0) {
                        c.moveToFirst();

                        for (int i = 0; i < colcount; i++) {
                            if (i != colcount - 1) {

                                bw.write(c.getColumnName(i) + ",");

                            } else {

                                bw.write(c.getColumnName(i));

                            }
                        }
                        bw.newLine();

                        for (int i = 0; i < rowcount; i++) {
                            c.moveToPosition(i);

                            for (int j = 0; j < colcount; j++) {
                                if (j != colcount - 1)
                                    bw.write(c.getString(j) + ",");
                                else
                                    bw.write(c.getString(j));
                            }
                            bw.newLine();
                        }
                        bw.flush();
                    }
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), getString(R.string.errorExport), Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();

                } finally {

                }
                String filename = "kartierpartner_db_export.csv";
                File sdCardDir = new File(Environment.getExternalStorageDirectory() + "/kartierpartner");
                File filelocation = new File(sdCardDir, filename);
                Uri path = Uri.fromFile(filelocation);
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("vnd.android.cursor.dir/email");
                emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.emailSubject));
                startActivity(Intent.createChooser(emailIntent, getString(R.string.emailChooser)));
            }
        });

        btnreset.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                AlertDialog diaBox = AskOption();
                diaBox.show();
            }
        });
    }

        private AlertDialog AskOption()
        {
            AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.deleteProject))
                    .setMessage(getString(R.string.deleteProjectText))

                    .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            getActivity().deleteDatabase("field.db");
                            dialog.dismiss();
                        }

                    })



                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    })
                    .create();
            return myQuittingDialogBox;

        }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    }
