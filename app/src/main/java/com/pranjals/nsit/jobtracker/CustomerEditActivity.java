package com.pranjals.nsit.jobtracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

import java.util.ArrayList;

/**
 * Created by Pranjal Verma on 4/17/2016.
 */
public class CustomerEditActivity extends AppCompatActivity {

    private long customerId;
    private ArrayList<String> extraCols;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        extraCols = DBHelper.getInstance(CustomerEditActivity.this).getExtraOrderCols(1);

        LinearLayout container = (LinearLayout) findViewById(R.id.container_customerEdit);
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        customerId = getIntent().getLongExtra("customerId",0);
        String[] value = new String[DBHelper.DEF_CUSTOMER_COLS+extraCols.size()];

        if(customerId!=0){
            Cursor c = getContentResolver().query(DBContentProvider.CUSTOMER_URI,null,"_id = "+customerId,null,null);
            if(c!=null && c.moveToFirst()){
                for(int i=0;i<value.length;i++)
                    value[i] = c.getString(i+1);
            }
        }

        EditText et = (EditText)findViewById(R.id.et_customerEdit_name);
        et.setText(value[0]);
        et = (EditText)findViewById(R.id.et_customerEdit_mobile);
        et.setText(value[1]);
        et = (EditText)findViewById(R.id.et_customerEdit_email);
        et.setText(value[2]);
        et = (EditText)findViewById(R.id.et_customerEdit_address);
        et.setText(value[3]);

        for (int i = 0; i < extraCols.size(); i++) {

            View viewToAdd = inflater.inflate(R.layout.customer_add_dynamic_row, null);
            EditText det = (EditText) viewToAdd.findViewById(R.id.customerAdd_dynamic_et);
            det.setId(i);
            det.setText(value[4+i]);
            container.addView(viewToAdd);
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_done,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
        case R.id.done_edit_customer:
                            String name = ((EditText)findViewById(R.id.et_customerEdit_name)).getText().toString();
                            String mobile = ((EditText)findViewById(R.id.et_customerEdit_mobile)).getText().toString();
                            String address = ((EditText)findViewById(R.id.et_customerEdit_address)).getText().toString();
                            String email = ((EditText)findViewById(R.id.et_customerEdit_email)).getText().toString();
            ContentValues cvalues = new ContentValues();
            cvalues.put("name",name);
            cvalues.put("mobile", mobile);
            cvalues.put("email", email);
            cvalues.put("address", address);

                             for(int i=0;i<extraCols.size();i++){
                                 String toAdd = ((EditText)findViewById(i)).getText().toString();
                                 cvalues.put(extraCols.get(i),toAdd);
                             }

                            getContentResolver().update(DBContentProvider.CUSTOMER_URI,cvalues,"_id = "+customerId,null);
                            setResult(RESULT_OK);
                            finish();
                            break;


        }
        return super.onOptionsItemSelected(item);
    }
}
