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

public class OrderListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ArrayList<Order> orders = new ArrayList<>();
        final ArrayList<Order> ordersFinal = orders;

        String projection[] = {"_id","name", "doo", "doc", "cid","eid"};
        Cursor c = getContentResolver().query(DBContentProvider.ORDER_URI,projection,null,null,null);
        if (c.moveToFirst()) {
            do {
                String _id = c.getString(c.getColumnIndex("_id"));
                String name = c.getString(c.getColumnIndex("name"));
                String doo = c.getString(c.getColumnIndex("doo"));
                String doc = c.getString(c.getColumnIndex("doc"));
                String cid = c.getString(c.getColumnIndex("cid"));
                String eid = c.getString(c.getColumnIndex("eid"));
                orders.add(new Order(Long.parseLong(_id),name,Long.parseLong(cid),Long.parseLong(eid),doo,doc));
            } while(c.moveToNext());
        }

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.OrderRecyclerView);
        OrderRecyclerView adapter = new OrderRecyclerView(orders);
        adapter.setOnItemClickListener(new OrderRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startOrderViewActivity(ordersFinal.get(position).get_id());

            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void startOrderViewActivity(Long _id){
        Intent intent = new Intent(this, OrderViewActivity.class);
        intent.putExtra(OrderViewActivity.START_WITH_ID,_id);
        startActivity(intent);
    }

}