package com.pranjals.nsit.jobtracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Pranjal Verma on 3/22/2016.
 */
public class CustomerAddActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add);

        Button add = (Button)findViewById(R.id.customerAdd_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = (EditText)findViewById(R.id.customerAdd_name);
                String customer_name = editText.getText().toString();
                editText = (EditText)findViewById(R.id.customerAdd_mobile);
                String customer_mobile = editText.getText().toString();
                editText = (EditText)findViewById(R.id.customerAdd_email);
                String customer_email = editText.getText().toString();
                editText = (EditText)findViewById(R.id.customerAdd_address);
                String customer_address = editText.getText().toString();
                Customer customer = new Customer(customer_name,customer_mobile,customer_email,customer_address);
                DBHelper.getInstance(CustomerAddActivity.this).addCustomer(customer);
                finish();

            }
        });
    }
}
