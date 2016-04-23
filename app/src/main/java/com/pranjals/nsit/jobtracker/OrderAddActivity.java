package com.pranjals.nsit.jobtracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

import java.util.ArrayList;
import java.util.Calendar;

public class OrderAddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private String stageId = "1";
    private StageSpinnerAdapter spinnerAdapter;

    private ArrayList<String> extraCols;
    private ArrayList<String> extraColDataTypes;

    private EditText editTextSelectedForDateInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_add);

        extraCols = DBHelper.getInstance(OrderAddActivity.this).getExtraOrderCols(0);
        extraColDataTypes = DBHelper.getInstance(OrderAddActivity.this).getExtraOrderColDataTypes(0);

        Button add = (Button) findViewById(R.id.orderAdd_button);
        ImageButton addStage = (ImageButton)findViewById(R.id.orderAdd_imageButton);

        Spinner spinner = (Spinner)findViewById(R.id.addOrderStage_spinner);

        String projection[] = {"_id","type"};
        Cursor stageCursor = getContentResolver().query(DBContentProvider.STAGE_URI,projection,null,null,null);
        spinnerAdapter = new StageSpinnerAdapter(OrderAddActivity.this,stageCursor,0);

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
            EditText et;
            switch(extraColDataTypes.get(i)){
                case "TEXT" :
                    et = (EditText) inflater.inflate(R.layout.order_add_dynamic_row, null);
                    et.setInputType(InputType.TYPE_CLASS_TEXT);
                    et.setId(i);
                    et.setHint(extraCols.get(i));
                    break;
                case "NUME" :
                    et = (EditText) inflater.inflate(R.layout.order_add_dynamic_row, null);
                    et.setInputType(InputType.TYPE_CLASS_NUMBER);
                    et.setId(i);
                    et.setHint(extraCols.get(i));
                    break;
                case "DATE" :
                    et = (EditText) inflater.inflate(R.layout.order_add_date_dynamic_row, null);
                    et.setId(i);
                    et.setHint(extraCols.get(i));
                    break;
                default :
                    et = (EditText) inflater.inflate(R.layout.order_add_dynamic_row, null);
                    et.setInputType(InputType.TYPE_CLASS_TEXT);
                    et.setId(i);
                    et.setHint(extraCols.get(i));
                    break;
            }
            container.addView(et);
        }


        addStage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OrderAddActivity.this,StageAddActivity.class);
                startActivityForResult(intent, 2);
            }
        });

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
                String doo = DBHelper.getYYYYMMDD(editText.getText().toString());
                editText = (EditText) findViewById(R.id.orderAdd_doc);
                String doc = DBHelper.getYYYYMMDD(editText.getText().toString());

                ContentValues values = new ContentValues();

                    values.put("name", name);
                    values.put("cid", cid);
                    values.put("eid",eid);
                    values.put("doo", doo);
                    values.put("doc", doc);
                    values.put("stageId", stageId);
                    values.put("curStage", 0);


                for(int i=0;i<extraCols.size();i++)
                    values.put(extraCols.get(i) + extraColDataTypes.get(i),((EditText)findViewById(i)).getText().toString());


                getContentResolver().insert(DBContentProvider.ORDER_URI, values);
                // Toast.makeText(OrderAddActivity.this, "Added stage id :" + stageId, Toast.LENGTH_SHORT).show();
                Intent intent= getIntent();
                setResult(RESULT_OK,intent);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==2 && resultCode ==RESULT_OK){
            String projection[] = {"_id","type"};
            stageId = data.getExtras().getString("stageId");
            spinnerAdapter.changeCursor( getContentResolver().query(DBContentProvider.STAGE_URI,projection,null,null,null));
            spinnerAdapter.notifyDataSetChanged();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        String dayString = Integer.toString(day);
        if(day < 10){dayString = "0" + dayString;}
        String monthString = Integer.toString(month + 1);
        if((month + 1) < 10){monthString = "0" + monthString;}
        String date = dayString + "-" + monthString + "-" + Integer.toString(year);
        editTextSelectedForDateInput.setText(date);
    }

    public void onAddDateClicked (View view){
        editTextSelectedForDateInput = ((EditText) view);
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(), "date picker");
    }

}
