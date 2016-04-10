package com.pranjals.nsit.jobtracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pranjals.nsit.jobtracker.BuildDB.BuildDBActivity;
import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

public class MainActivity extends AppCompatActivity {

    private final String isFirstTime = "isFirstTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContentValues values = new ContentValues();
        values.put("type","polish");
        values.put("names","stage1;stage2;stage3");
        values.put("total",3);
        getContentResolver().insert(DBContentProvider.STAGE_URI,values);
        values.put("type","paint");
        values.put("names","stage1;stage2");
        values.put("total",2);
        getContentResolver().insert(DBContentProvider.STAGE_URI,values);
        values.put("type","garnish");
        values.put("names","stage1");
        values.put("total",1);
        getContentResolver().insert(DBContentProvider.STAGE_URI,values);
        values.put("type","assembly");
        values.put("names","stage1;stage2;stage3");
        values.put("total",3);
        getContentResolver().insert(DBContentProvider.STAGE_URI,values);

        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        if(!(sharedPreferences.contains(isFirstTime))){
            Intent intent = new Intent(this, BuildDBActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstTime", false);
            editor.commit();
        }




    }

    public void onButtonTapped(View view){
        int activityToStart = view.getId();
        Intent intent;
        switch(activityToStart){
            case R.id.orderAddButton : intent = new Intent(this, OrderAddActivity.class);
                break;
            case R.id.orderViewButton : intent = new Intent(this, OrderViewActivity.class);
                break;
            case R.id.orderListButton : intent = new Intent(this, OrderListActivity.class);
                break;
            case R.id.customerAddButton : intent = new Intent(this, CustomerAddActivity.class);
                break;
            case R.id.customerViewButton : intent = new Intent(this, CustomerViewActivity.class);
                break;
            case R.id.customerListButton : intent = new Intent(this, CustomerListActivity.class);
                break;
           /* case R.id.addEmployeeButton : intent = new Intent(this, AddEmployeeActivity.class);
                break;
            case R.id.viewEmployeeButton : intent = new Intent(this, ViewEmployeeActivity.class);
                break;
            case R.id.listEmployeeButton : intent = new Intent(this, ListEmployeeActivity.class);
                break;*/
            case R.id.scanQrcodeButton : intent = new Intent(this, QrCodeScanActivity.class);
                break;
            default : intent = new Intent();
        }
        startActivity(intent);
    }

}
