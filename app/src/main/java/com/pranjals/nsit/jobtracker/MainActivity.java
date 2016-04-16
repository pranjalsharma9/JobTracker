package com.pranjals.nsit.jobtracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pranjals.nsit.jobtracker.BuildDB.BuildDBActivity;
import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String isFirstTime = "isFirstTime";
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ArrayList<Order> orders;
    OrderRecyclerView orderAdapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_navigation_drawer);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);;
        if(!(sharedPreferences.contains(isFirstTime))){
            Intent intent = new Intent(this, BuildDBActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


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

        TextView countOrder = (TextView)navigationView.getMenu().getItem(0).getActionView();
        countOrder.setText("12");
        TextView countCustomer = (TextView)navigationView.getMenu().getItem(1).getActionView();
        countCustomer.setText("5");
        TextView countEmployee = (TextView)navigationView.getMenu().getItem(2).getActionView();
        countEmployee.setText("10");


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.drawer_add_order:
                        //drawerLayout.closeDrawer(GravityCompat.START);
                        intent = new Intent(MainActivity.this, OrderAddActivity.class);
                        startActivityForResult(intent, 0);
                        break;
                    case R.id.drawer_add_customer:
                        //drawerLayout.closeDrawer(GravityCompat.START);
                        intent = new Intent(MainActivity.this, CustomerAddActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.drawer_add_employee:
                        //drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(MainActivity.this, "COMING SOON", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.drawer_orders:
                        //drawerLayout.closeDrawer(GravityCompat.START);
                        intent = new Intent(MainActivity.this, OrderListActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.drawer_customers:
                        //drawerLayout.closeDrawer(GravityCompat.START);
                        intent = new Intent(MainActivity.this, CustomerListActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.drawer_employees:
                        //drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(MainActivity.this, "COMING SOON", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.drawer_setting_activity:
                        //drawerLayout.closeDrawer(GravityCompat.START);
//                    intent = new Intent(HomeScreen.this,CustomerListActivity.class);
//                    startActivity(intent);
                        Toast.makeText(MainActivity.this, "COMING SOON", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }
                return true;
            }
        });

        if(sharedPreferences.contains(isFirstTime)) {
            orders = new ArrayList<>();
            String projection[] = {"_id", "name", "doo", "doc", "cid", "eid"};
            Cursor c = getContentResolver().query(DBContentProvider.ORDER_URI, projection, null, null, null);
            if (c.moveToFirst()) {
                do {
                    String _id = c.getString(c.getColumnIndex("_id"));
                    String name = c.getString(c.getColumnIndex("name"));
                    String doo = c.getString(c.getColumnIndex("doo"));
                    String doc = c.getString(c.getColumnIndex("doc"));
                    String cid = c.getString(c.getColumnIndex("cid"));
                    String eid = c.getString(c.getColumnIndex("eid"));
                    orders.add(new Order(Long.parseLong(_id), name, Long.parseLong(cid), Long.parseLong(eid), doo, doc));
                } while (c.moveToNext());
            }

            recyclerView = (RecyclerView) findViewById(R.id.OrderRecyclerView_homescreen);
            orderAdapter = new OrderRecyclerView(orders);
            orderAdapter.setOnItemClickListener(new OrderRecyclerView.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    startOrderViewActivity(orders.get(position).get_id());

                }
            });
            recyclerView.setAdapter(orderAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

    }

    private void startOrderViewActivity(Long _id){
        Intent intent = new Intent(this, OrderViewActivity.class);
        intent.putExtra(OrderViewActivity.START_WITH_ID,_id);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0&&resultCode==RESULT_OK){
            orders = new ArrayList<>();
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

            orderAdapter = new OrderRecyclerView(orders);
            recyclerView.swapAdapter(orderAdapter,false);

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
