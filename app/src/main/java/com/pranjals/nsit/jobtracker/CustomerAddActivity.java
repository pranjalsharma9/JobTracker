package com.pranjals.nsit.jobtracker;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class CustomerAddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ArrayList<String> extraCols;
    private ArrayList<String> extraColDataTypes;

    private EditText editTextSelectedForDateInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add);

        extraCols = DBHelper.getInstance(CustomerAddActivity.this).getExtraCols(1);
        extraColDataTypes = DBHelper.getInstance(CustomerAddActivity.this).getExtraOrderColDataTypes(1);

        Button add = (Button) findViewById(R.id.customerAdd_button);

        LinearLayout container = (LinearLayout) findViewById(R.id.customerAdd_container);
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < extraCols.size(); i++) {
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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.customerAdd_name);
                String name = editText.getText().toString();
                editText = (EditText) findViewById(R.id.customerAdd_mobile);
                String mobile = editText.getText().toString();
                editText = (EditText) findViewById(R.id.customerAdd_email);
                String email = editText.getText().toString();
                editText = (EditText) findViewById(R.id.customerAdd_address);
                String address = editText.getText().toString();

                Resources res = getResources();
                Drawable drawable = res.getDrawable(R.drawable.slide2);
                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitMapData = stream.toByteArray();


                ContentValues values = new ContentValues();

                values.put("name", name);
                values.put("mobile", mobile);
                values.put("email", email);
                values.put("address", address);
                values.put("image",bitMapData);

                for (int i = 0; i < extraCols.size(); i++) {
                    values.put(extraCols.get(i) + extraColDataTypes.get(i), ((EditText) findViewById(i)).getText().toString());
                    //Log.v("values", ((EditText) findViewById(i)).getText().toString());
                }
                getContentResolver().insert(DBContentProvider.CUSTOMER_URI, values);
                finish();
            }
        });
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