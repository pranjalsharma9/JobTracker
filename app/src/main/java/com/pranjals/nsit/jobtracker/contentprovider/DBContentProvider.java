package com.pranjals.nsit.jobtracker.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Switch;

import com.pranjals.nsit.jobtracker.DBHelper;

/**
 * Created by Pranjal on 15-03-2016.
 * the DBContentProvider just reads from the database defined in DBHelper and provides with Cursors
 * loaded with data, or it simply updates the database.
 */
public class DBContentProvider extends ContentProvider {

    private DBHelper dbHelper;

    //writing constant strings to represent the URI of this ContentProvider
    private static final String AUTHORITY = "com.pranjals.nsit.jobtracker.contentprovider";
    private static final String ORDER_TABLE = "order";
    private static final String CUSTOMER_TABLE = "customer";
    public static final Uri ORDER_URI = Uri.parse("content://" + AUTHORITY + "/" + ORDER_TABLE);
    public static final Uri CUSTOMER_URI = Uri.parse("content://" + AUTHORITY + "/" + CUSTOMER_TABLE);
    private static final UriMatcher dbURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        dbURIMatcher.addURI(AUTHORITY, ORDER_TABLE, 1); //Code 1 for the first URI
        dbURIMatcher.addURI(AUTHORITY,CUSTOMER_TABLE,2);

    }

    @Override
    public boolean onCreate() {
        dbHelper = DBHelper.getInstance(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //Cursor cursor = DBHelper.getInstance(getContext()).getReadableDatabase().query("orders", projection, selection, selectionArgs, null, null, sortOrder);
        //cursor.setNotificationUri(getContext().getContentResolver(), uri);
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (dbURIMatcher.match(uri)){

            case 1 :  queryBuilder.setTables("orders");
                      break;

            case 2 : queryBuilder.setTables("customers");
                     break;

            default : break;
        }

        Cursor cursor = queryBuilder.query(sqLiteDatabase,projection,selection,selectionArgs,null,null,sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id;
        switch(dbURIMatcher.match(uri)){
           case 1 :
                id = DBHelper.getInstance(getContext()).getWritableDatabase().insert("orders", null, values);
               getContext().getContentResolver().notifyChange(uri, null);
               return Uri.parse(ORDER_TABLE + "/" + id);
            case 2 :
                 id = DBHelper.getInstance(getContext()).getWritableDatabase().insert("customers", null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(CUSTOMER_TABLE + "/" + id);

            default:
               return Uri.parse(ORDER_TABLE + "/" + 0);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = DBHelper.getInstance(getContext()).getWritableDatabase().delete("order", selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated = DBHelper.getInstance(getContext()).getWritableDatabase().update("order", values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
