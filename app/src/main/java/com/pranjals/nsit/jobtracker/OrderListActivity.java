package com.pranjals.nsit.jobtracker;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

import java.util.ArrayList;

public class OrderListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ArrayList<Order> orders = new ArrayList<>();

        String projection[] = {"_id","name", "doo", "doc", "cid","eid"};
        Cursor c = getContentResolver().query(DBContentProvider.ORDER_URI,projection,null,null,null);
        if (c.moveToFirst()) {
            do {
                String name = c.getString(c.getColumnIndex("name"));
                String doo = c.getString(c.getColumnIndex("doo"));
                String doc = c.getString(c.getColumnIndex("doc"));
                String cid = c.getString(c.getColumnIndex("cid"));
                String eid = c.getString(c.getColumnIndex("eid"));
                orders.add(new Order(name,Long.parseLong(cid),Long.parseLong(eid),doo,doc));
            } while(c.moveToNext());
        }

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.OrderRecyclerView);
        OrderRecyclerView adapter = new OrderRecyclerView(orders);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }


}