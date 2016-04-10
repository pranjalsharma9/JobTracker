package com.pranjals.nsit.jobtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

import java.util.ArrayList;

public class OrderAddActivity extends AppCompatActivity {

    private String stageId = "1";

    private  ArrayList<String> extraCols;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_add);

         extraCols = DBHelper.getInstance(OrderAddActivity.this).getExtraOrderCols();
        Button add = (Button) findViewById(R.id.orderAdd_button);
        Spinner spinner = (Spinner)findViewById(R.id.addOrderStage_spinner);

        String projection[] = {"_id","type"};
        Cursor stageCursor = getContentResolver().query(DBContentProvider.STAGE_URI,projection,null,null,null);
        StageSpinnerAdapter spinnerAdapter = new StageSpinnerAdapter(OrderAddActivity.this,stageCursor,0);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                stageId = position + 1 +"";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                    values.put("stageId", stageId);


                for(int i=0;i<extraCols.size();i++)
                    values.put(extraCols.get(i),((EditText)findViewById(i)).getText().toString());

                getContentResolver().insert(DBContentProvider.ORDER_URI, values);
                finish();
            }
        });
    }


    public class StageSpinnerAdapter extends CursorAdapter{

        public StageSpinnerAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.spinner_stages_add,parent,false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TextView tv = (TextView)view.findViewById(R.id.spinnerStageAdd_tv);
            String stage = cursor.getString(cursor.getColumnIndex("type"));
               tv.setText(stage);

        }
    }


}
