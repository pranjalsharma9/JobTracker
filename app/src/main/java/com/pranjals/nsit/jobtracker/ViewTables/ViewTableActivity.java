package com.pranjals.nsit.jobtracker.ViewTables;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.pranjals.nsit.jobtracker.DBHelper;
import com.pranjals.nsit.jobtracker.R;

public class ViewTableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_table);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.my_table);
        LayoutInflater layoutInflater = getLayoutInflater();
        Intent intent = getIntent();
        Cursor c;
        String stringToSwap = "";
        int whenToSwap = 0;
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        Boolean twoTables;
        switch(intent.getStringExtra("tablesToView")){
            case "order" :
                c = DBHelper.getInstance(this).getReadableDatabase().rawQuery("SELECT name , doo , doc FROM orders", null);
                twoTables = false;
                toolbar.setTitle("Order Table");
                break;
            case "customer" :
                c = DBHelper.getInstance(this).getReadableDatabase().rawQuery("SELECT name , mobile , email , address FROM customers", null);
                twoTables = false;
                toolbar.setTitle("Customer Table");
                break;
            case "employee" :
                c = DBHelper.getInstance(this).getReadableDatabase().rawQuery("SELECT name , mobile FROM employees", null);
                twoTables = false;
                toolbar.setTitle("Employee Table");
                break;
            case "stage" :
                c = DBHelper.getInstance(this).getReadableDatabase().rawQuery("SELECT type , total , names FROM stages", null);
                twoTables = false;
                toolbar.setTitle("Stage Table");
                break;
            case "orderCustomer" :
                c = DBHelper.getInstance(this).getReadableDatabase().rawQuery("SELECT orders.name , orders.doo , orders.doc , customers.name , customers.email , customers.mobile , customers.address FROM orders , customers WHERE orders.cid = customers._id", null);
                twoTables = true;
                stringToSwap = "customers.";
                whenToSwap = 3;
                toolbar.setTitle("Order-Customer Table");
                break;
            case "orderEmployee" :
                c = DBHelper.getInstance(this).getReadableDatabase().rawQuery("SELECT orders.name , orders.doo , orders.doc , employees.name , employees.mobile FROM orders , employees WHERE orders.eid = employees._id", null);
                twoTables = true;
                stringToSwap = "employees.";
                whenToSwap = 3;
                toolbar.setTitle("Order-Employee Table");
                break;
            case "orderStage" :
                c = DBHelper.getInstance(this).getReadableDatabase().rawQuery("SELECT orders.name , orders.doo , orders.doc , orders.curStage , orders.totalStages , stages.type , stages.names FROM orders , stages WHERE orders.stageID = stages._id", null);
                twoTables = true;
                stringToSwap = "stages.";
                whenToSwap = 5;
                toolbar.setTitle("Order-Stage Table");
                break;
            default :
                c = DBHelper.getInstance(this).getReadableDatabase().rawQuery("SELECT name , doo , doc FROM orders", null);
                twoTables = false;
                toolbar.setTitle("Order Table");
                break;
        }
        if(twoTables){
            if (c.moveToFirst()) {
                String toAppend = "orders.";
                TableRow titleRow = new TableRow(this);
                for(int j = 0; j < c.getColumnCount(); j++){
                    if(j == whenToSwap){
                        toAppend = stringToSwap;
                    }
                    if(!c.getColumnName(j).equals("image")){
                        TextView textView = (TextView) layoutInflater.inflate(R.layout.table_text_view, null);
                        textView.setText(toAppend + c.getColumnName(j));
                        titleRow.addView(textView);
                    }
                }
                tableLayout.addView(titleRow);
                do {
                    TableRow tableRow = new TableRow(this);
                    for(int i = 0; i < c.getColumnCount(); i++){
                        if(!c.getColumnName(i).equals("image")){
                            TextView textView = (TextView) layoutInflater.inflate(R.layout.table_text_view, null);
                            textView.setText(c.getString(i));
                            tableRow.addView(textView);
                        }
                    }
                    tableLayout.addView(tableRow);
                } while(c.moveToNext());
            }
        }
        else{
            if (c.moveToFirst()) {
                TableRow titleRow = new TableRow(this);
                for(int j = 0; j < c.getColumnCount(); j++){
                    if(!c.getColumnName(j).equals("image")){
                        TextView textView = (TextView) layoutInflater.inflate(R.layout.table_text_view, null);
                        textView.setText(c.getColumnName(j));
                        titleRow.addView(textView);
                    }
                }
                tableLayout.addView(titleRow);
                do {
                    TableRow tableRow = new TableRow(this);
                    for(int i = 0; i < c.getColumnCount(); i++){
                        if(!c.getColumnName(i).equals("image")){
                            TextView textView = (TextView) layoutInflater.inflate(R.layout.table_text_view, null);
                            textView.setText(c.getString(i));
                            tableRow.addView(textView);
                        }
                    }
                    tableLayout.addView(tableRow);
                } while(c.moveToNext());
            }
        }
        c.close();
    }
}
