package com.pranjals.nsit.jobtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pranjal on 12-03-2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private Context callingActivity;
    private static DBHelper dbHelper;

    private DBHelper(Context context) {
        super(context, "DB.db", null, 1);
    }

    //Function to return a reference to the dbHelper
    public static synchronized DBHelper getInstance(Context context) {
        if(dbHelper == null){
            dbHelper = new DBHelper(context);
        }
        dbHelper.callingActivity = context;
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getOrders() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("select orders._id, orders.name, orders.doo, orders.doc, customers.name from orders, customers"
                +" where (orders.cid = customers._id)", null);
    }

    public void addOrder(Order order){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", order.getName());
        contentValues.put("cid", order.getCid());
        contentValues.put("eid", order.getEid());
        contentValues.put("doo", order.getDoo());
        contentValues.put("doc", order.getDoc());
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert("orders", null, contentValues);
    }

    public void addCustomer(Customer customer){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", customer.getName());
        contentValues.put("mobile", customer.getMobile());
        contentValues.put("email", customer.getEmail());
        contentValues.put("address", customer.getAddress());
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert("customers", null, contentValues);
    }

    public void createTables(String createOrderTableString, String createCustomerTableString, String createEmployeeTableString){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("CREATE TABLE orders (_id INTEGER PRIMARY KEY, name TEXT, cid INTEGER, eid INTEGER, doo TEXT, doc TEXT, curStage INTEGER, stageId INTEGER"
                    + createOrderTableString + ")");
        db.execSQL("CREATE TABLE customers (_id INTEGER PRIMARY KEY, name TEXT, mobile TEXT, email TEXT"
                + createCustomerTableString + ")");
        db.execSQL("CREATE TABLE employees (_id INTEGER PRIMARY KEY, name TEXT, mobile TEXT"
                + createEmployeeTableString + ")");
        db.execSQL("CREATE TABLE stages (_id INTEGER PRIMARY KEY, type TEXT, total INTEGER, names TEXT)");

        /*Code to test the execution of instructions above!!!!
        Cursor c = db.rawQuery("SELECT * FROM orders", null);
        StringBuilder result = new StringBuilder();
        String[] result0 = c.getColumnNames();
        int i = 0;
        for(int j = 0; j < result0.length; j++){
            result.append(result0[j]);
        }
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String[] temp = new String[c.getColumnCount()];
            for (i = 0; i < temp.length; i++) {
                temp[i] = c.getString(i);
            }
            for(int j = 0; j < result0.length; j++){
                result.append(result0[j]);
            }
        }
        Log.v("DBHelper", result.toString());
        Log.v("DBHelper", "I created the tables!");*/
    }
}