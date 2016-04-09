package com.pranjals.nsit.jobtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pranjals.nsit.jobtracker.BuildDB.BuildDBActivity;

public class MainActivity extends AppCompatActivity {

    private final String isFirstTime = "isFirstTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
