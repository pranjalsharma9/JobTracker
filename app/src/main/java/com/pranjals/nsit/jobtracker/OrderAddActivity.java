package com.pranjals.nsit.jobtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OrderAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_add);

        Button add = (Button) findViewById(R.id.orderAdd_button);
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
                Order order = new Order(name, cid, eid, doo, doc);
                DBHelper.getInstance(OrderAddActivity.this).addOrder(order);
                finish();
            }
        });
    }
}
