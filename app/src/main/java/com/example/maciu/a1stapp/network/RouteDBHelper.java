package com.example.maciu.a1stapp.network;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.maciu.a1stapp.object.Route;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maciu on 06.07.2017.
 */
public class RouteDBHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "entry";
    private static final String KEY_ID = "id";
    private static final String COLUMN_NAME_NAME = "name";
    private static final String COLUMN_NAME_THUMBID = "thumbid";
    private static final String COLUMN_NAME_LOCATION_LONGITUDE = "location_long";
    private static final String COLUMN_NAME_LOCATION_LATITUDE = "location_lat";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " +
            RouteDBHelper.TABLE_NAME + " (" +
            RouteDBHelper.KEY_ID + " INTEGER PRIMARY KEY," +
            RouteDBHelper.COLUMN_NAME_NAME + " TEXT," +
            RouteDBHelper.COLUMN_NAME_THUMBID + " INTEGER," +
            RouteDBHelper.COLUMN_NAME_LOCATION_LATITUDE + " DOUBLE," +
            RouteDBHelper.COLUMN_NAME_LOCATION_LONGITUDE + " DOUBLE)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + RouteDBHelper.TABLE_NAME;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Route.db";

    public RouteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    public void createDB() {
        this.getWritableDatabase().execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public void dropTable() {
        this.getWritableDatabase().execSQL(SQL_DELETE_ENTRIES);
    }

    public void createEntries(Route routes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_NAME, routes.getName());
        values.put(COLUMN_NAME_THUMBID, routes.getThumbId());
        values.put(COLUMN_NAME_LOCATION_LATITUDE, routes.getLocation().latitude);
        values.put(COLUMN_NAME_LOCATION_LONGITUDE, routes.getLocation().longitude);
        try {
            db.insertOrThrow(TABLE_NAME, null, values);
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }

    }

    public List<Route> getEntries() {
        List<Route> routes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Route route = new Route();
                route.setName(c.getString(c.getColumnIndex(COLUMN_NAME_NAME)));
                route.setThumbId(c.getInt(c.getColumnIndex(COLUMN_NAME_THUMBID)));
                route.setLatLng(new LatLng(c.getDouble(c.getColumnIndex(COLUMN_NAME_LOCATION_LATITUDE)), c.getDouble(c.getColumnIndex(COLUMN_NAME_LOCATION_LONGITUDE))));
                routes.add(route);
            } while (c.moveToNext());
        }
        c.close();
        return routes;
    }
}
