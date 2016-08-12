package com.entropia.kartierpartner;

/**
 * Created by Entropia on 07.09.2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    // Formations and User Settings -- TABLE "formations"
    public static final String KP_DATABASE_NAME = "field.db";
    public static final String KP_TABLE_NAME = "formations";
    public static final String KP_COLUMN_ID = "f_id";
    public static final String KP_COLUMN_NAME = "name";
    public static final String KP_COLUMN_COUNT = "count";

    //Results from Fieldtrip -- TABLE "measurements"
    public static final String MEASURE_TABLE_NAME = "measurements";
    public static final String MEASURE_COLUMN_ID = "m_id";
    public static final String MEASURE_COLUMN_FORMATION1 = "formation1";
    public static final String MEASURE_COLUMN_FORMATION2 = "formation2";
    public static final String MEASURE_COLUMN_LON = "lon";
    public static final String MEASURE_COLUMN_LAT = "lat";
    public static final String MEASURE_COLUMN_DIR = "dir";
    public static final String MEASURE_COLUMN_DIP = "dip";
    public static final String MEASURE_COLUMN_ACC = "acc";
    public static final String MEASURE_COLUMN_COMMENT = "comment";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, KP_DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table formations " +
                        "(f_id integer primary key, name text, count integer)"
        );
        db.execSQL(
                "create table measurements " +
                        "(m_id integer primary key, formation1 text, formation2 text, lon double, lat double, dir float, dip float, acc float, comment text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS formations");
        db.execSQL("DROP TABLE IF EXISTS measurements");
        onCreate(db);
    }

    public boolean insertFormation(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        db.insert("formations", null, contentValues);
        return true;
    }

    public boolean insertMeasurement(String formation1, String formation2, double lon, double lat, double dir, double dip, double acc, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("formation1", formation1);
        contentValues.put("formation2", formation2);
        contentValues.put("lon", lon);
        contentValues.put("lat", lat);
        contentValues.put("dir", dir);
        contentValues.put("dip", dip);
        contentValues.put("acc", acc);
        contentValues.put("comment", comment);
        db.insert("measurements", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from formations where f_id=" + id + "", null);
        return res;
    }

    public Cursor getExport(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from measurements", null);
        return res;
    }
    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, KP_TABLE_NAME);
        return numRows;
    }

    public boolean updateFormation(Integer id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        db.update("formations", contentValues, "f_id = ? ", new String[]{Integer.toString(id)});
        return true;
    }


    public Integer deleteFormation(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("formations",
                "f_id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<String> getAllFormations() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from formations", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(KP_COLUMN_NAME)));

            res.moveToNext();
        }
        return array_list;
    }

    public int numberOfMeasurements() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, MEASURE_TABLE_NAME);
        return numRows;
    }

    public ArrayList<Integer> getAllFormations2() {
        ArrayList<Integer> array_list2 = new ArrayList<Integer>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from formations", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list2.add(res.getInt(res.getColumnIndex(KP_COLUMN_ID)));
            res.moveToNext();
        }
        return array_list2;
    }
    public ArrayList<String> getAllMeasurements() {
        ArrayList<String> measurements = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from measurements", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            measurements.add(res.getString(res.getColumnIndex(MEASURE_COLUMN_FORMATION1)));

            res.moveToNext();
        }
            return measurements;
    }

    public ArrayList<String> getFormationMeasurements(String formation) {
        ArrayList<String> measurements = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from measurements where comment = '" + formation + "'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            measurements.add(res.getString(res.getColumnIndex(MEASURE_COLUMN_FORMATION1)));

            res.moveToNext();
        }
        return measurements;
    }

    public ArrayList<String> getFormationMeasurements2(String formation) {
        ArrayList<String> measurements2 = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from measurements where comment = '" + formation + "'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            measurements2.add(res.getString(res.getColumnIndex(MEASURE_COLUMN_FORMATION2)));

            res.moveToNext();
        }
        return measurements2;
    }

    public ArrayList<Double> getAllMeasurementsLat(String formation) {
        ArrayList<Double> measurementsLat = new ArrayList<Double>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from measurements where comment = '" + formation + "'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            measurementsLat.add(res.getDouble(res.getColumnIndex(MEASURE_COLUMN_LAT)));

            res.moveToNext();
        }
        return measurementsLat;
    }

    public ArrayList<Double> getAllMeasurementsLon(String formation) {
        ArrayList<Double> measurementsLon = new ArrayList<Double>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from measurements where comment = '" + formation + "'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            measurementsLon.add(res.getDouble(res.getColumnIndex(MEASURE_COLUMN_LON)));

            res.moveToNext();
        }
        return measurementsLon;
    }

    public ArrayList<Double> getAllMeasurementsDir(String formation) {
        ArrayList<Double> measurementsDir = new ArrayList<Double>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from measurements where comment = '" + formation + "'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            measurementsDir.add(res.getDouble(res.getColumnIndex(MEASURE_COLUMN_DIR)));

            res.moveToNext();
        }
        return measurementsDir;
    }

    public ArrayList<Double> getAllMeasurementsAcc(String formation) {
        ArrayList<Double> measurementsAcc = new ArrayList<Double>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from measurements where comment = '" + formation + "'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            measurementsAcc.add(res.getDouble(res.getColumnIndex(MEASURE_COLUMN_ACC)));

            res.moveToNext();
        }
        return measurementsAcc;
    }

    public ArrayList<Double> getAllMeasurementsDip(String formation) {
        ArrayList<Double> measurementsDip = new ArrayList<Double>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from measurements where comment = '" + formation + "'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            measurementsDip.add(res.getDouble(res.getColumnIndex(MEASURE_COLUMN_DIP)));

            res.moveToNext();
        }
        return measurementsDip;
    }
}