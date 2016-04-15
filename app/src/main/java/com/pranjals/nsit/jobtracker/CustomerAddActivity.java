package com.pranjals.nsit.jobtracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

import java.util.ArrayList;

public class CustomerAddActivity extends AppCompatActivity {

    private ArrayList<String> extraCols;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add);

        extraCols = DBHelper.getInstance(CustomerAddActivity.this).getExtraOrderCols(1);
        Button add = (Button) findViewById(R.id.customerAdd_button);

        LinearLayout container = (LinearLayout) findViewById(R.id.customerAdd_container);
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < extraCols.size(); i++) {

            View viewToAdd = inflater.inflate(R.layout.customer_add_dynamic_row, null);
            EditText et = (EditText) viewToAdd.findViewById(R.id.customerAdd_dynamic_et);
            et.setId(i);
            et.setHint(extraCols.get(i));
            container.addView(viewToAdd);
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

                ContentValues values = new ContentValues();

                values.put("name", name);
                values.put("mobile", mobile);
                values.put("email", email);
                values.put("address", address);

                for (int i = 0; i < extraCols.size(); i++)
                    values.put(extraCols.get(i), ((EditText) findViewById(i)).getText().toString());

                getContentResolver().insert(DBContentProvider.CUSTOMER_URI, values);
                finish();
            }
        });
    }
}