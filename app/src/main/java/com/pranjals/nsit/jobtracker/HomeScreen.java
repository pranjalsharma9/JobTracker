package com.pranjals.nsit.jobtracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

import java.util.ArrayList;

/**
 * Created by Pranjal Verma on 4/15/2016.
 */
public class HomeScreen extends AppCompatActivity {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<Order> orders = new ArrayList<>();
        final ArrayList<Order> ordersFinal = orders;


        setContentView(R.layout.home_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

         drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
         navigationView = (NavigationView)findViewById(R.id.navigation_view);

        ActionBarDrawerToggle barDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_closed){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.setDrawerListener(barDrawerToggle);
        barDrawerToggle.syncState();





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

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.OrderRecyclerView_homescreen);
        OrderRecyclerView orderAdapter = new OrderRecyclerView(orders);
       orderAdapter.setOnItemClickListener(new OrderRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startOrderViewActivity(ordersFinal.get(position).get_id());

            }
        });
        recyclerView.setAdapter(orderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);

    }
    private void startOrderViewActivity(Long _id){
        Intent intent = new Intent(this, OrderViewActivity.class);
        intent.putExtra(OrderViewActivity.START_WITH_ID,_id);
        startActivity(intent);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        public void selectItem(int position){
            drawerLayout.closeDrawer(menu);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            drawerLayout.closeDrawer();
            
        }
        return super.onOptionsItemSelected(item);
    }
}
