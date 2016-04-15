package com.pranjals.nsit.jobtracker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

import java.util.ArrayList;

public class CustomerListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        ArrayList<Customer> customers = new ArrayList<>();
        final ArrayList<Customer> customersFinal = customers;

        String projection[] = {"_id", "name", "mobile", "email", "address"};
        Cursor c = getContentResolver().query(DBContentProvider.CUSTOMER_URI,projection,null,null,null);
        if (c.moveToFirst()) {
            do {
                String _id = c.getString(c.getColumnIndex("_id"));
                String name = c.getString(c.getColumnIndex("name"));
                String mobile = c.getString(c.getColumnIndex("mobile"));
                String email = c.getString(c.getColumnIndex("email"));
                String address = c.getString(c.getColumnIndex("address"));
                customers.add(new Customer(Long.parseLong(_id), name, mobile, email, address));
            } while(c.moveToNext());
        }

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.CustomerRecyclerView);
        CustomerRecyclerView adapter = new CustomerRecyclerView(customers);
        adapter.setOnItemClickListener(new CustomerRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startCustomerViewActivity(customersFinal.get(position).get_id());

            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void startCustomerViewActivity(Long _id){
        Intent intent = new Intent(this, CustomerViewActivity.class);
        intent.putExtra(CustomerViewActivity.START_WITH_ID,_id);
        startActivity(intent);
    }

}