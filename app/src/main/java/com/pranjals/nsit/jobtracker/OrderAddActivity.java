package com.pranjals.nsit.jobtracker;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

import java.util.ArrayList;

public class OrderAddActivity extends AppCompatActivity {

    private  ArrayList<String> extraCols;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_add);

         extraCols = DBHelper.getInstance(OrderAddActivity.this).getExtraOrderCols();
        Button add = (Button) findViewById(R.id.orderAdd_button);

        LinearLayout container = (LinearLayout)findViewById(R.id.orderAdd_container);
        LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(int i=0;i<extraCols.size();i++) {

            View viewToAdd = inflater.inflate(R.layout.order_add_dynamic_row, null);
            EditText et = (EditText)viewToAdd.findViewById(R.id.orderAdd_dynamic_et);
            et.setId(i);
            et.setHint(extraCols.get(i));
            container.addView(viewToAdd);
        }


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.orderAdd_name);
                String name = editText.getText().toString();
                editText = (EditText) findViewById(R.id.orderAdd_cid);
                long cid = Integer.parseInt(editText.getText().toString());
                editText = (EditText) findViewById(R.id.orderAdd_eid);
                long eid = Integer.parseInt(editText.getText().toString());
                editText = (EditText) findViewById(R.id.orderAdd_doo);
                String doo = editText.getText().toString();
                editText = (EditText) findViewById(R.id.orderAdd_doc);
                String doc = editText.getText().toString();

                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("cid", cid);
                values.put("eid",eid);
                values.put("doo", doo);
                values.put("doc", doc);
                for(int i=0;i<extraCols.size();i++)
                    values.put(extraCols.get(i),((EditText)findViewById(i)).getText().toString());

                getContentResolver().insert(DBContentProvider.ORDER_URI,values);
                finish();
            }
        });
    }
}
