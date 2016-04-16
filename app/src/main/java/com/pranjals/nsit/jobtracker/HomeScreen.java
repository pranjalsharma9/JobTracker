package com.pranjals.nsit.jobtracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

import java.util.ArrayList;

/**
 * Created by Pranjal Verma on 4/15/2016.
 */
public class HomeScreen extends AppCompatActivity {

    //RecyclerView menu;
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
//        ArrayList<String> menuItems = new ArrayList<>();
//        menuItems.add("Orders");
//        menuItems.add("Customers");
//        menuItems.add("Employees");
//        menuItems.add("Add Order");
//        menuItems.add("Add Customer");
//        menuItems.add("Add Employer");
//        menuItems.add("Settings");
//        NavDrawerListAdapter adapter = new NavDrawerListAdapter(menuItems,"Pranjal Verma","pranjal.verma80@gmail.com");
//        adapter.setClickListener(new NavDrawerListAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                switch (position) {
//
//                    case 1:
//                        Toast.makeText(HomeScreen.this, "Order List Activity launched", Toast.LENGTH_SHORT).show();
//                        drawerLayout.closeDrawer(menu);
//                        break;
//                    case 2:
//                        Toast.makeText(HomeScreen.this, "Customer List Activity launched", Toast.LENGTH_SHORT).show();
//                        drawerLayout.closeDrawer(menu);
//                        break;
//                    case 3:
//                        Toast.makeText(HomeScreen.this, "Employer List Activity launched", Toast.LENGTH_SHORT).show();
//                        drawerLayout.closeDrawer(menu);
//                        break;
//                    case 4:
//                        Toast.makeText(HomeScreen.this, "Add Order Activity launched", Toast.LENGTH_SHORT).show();
//                        drawerLayout.closeDrawer(menu);
//                        break;
//                    case 5:
//                        Toast.makeText(HomeScreen.this, "Add Customer Activity launched", Toast.LENGTH_SHORT).show();
//                        drawerLayout.closeDrawer(menu);
//                        break;
//                    case 6:
//                        Toast.makeText(HomeScreen.this, "Add Employee Activity launched", Toast.LENGTH_SHORT).show();
//                        drawerLayout.closeDrawer(menu);
//                        break;
//                    case 7:
//                        Toast.makeText(HomeScreen.this, "Settings Activity launched", Toast.LENGTH_SHORT).show();
//                        drawerLayout.closeDrawer(menu);
//                        break;
//
//                    default:
//                        Toast.makeText(HomeScreen.this, "Touched item unrecogonizable", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        });
//        menu.setAdapter(adapter);
//        menu.setLayoutManager(new LinearLayoutManager(this));

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
            switch(item.getItemId()){
                case R.id.drawer_add_order :
                        //drawerLayout.closeDrawer(GravityCompat.START);
                         intent = new Intent(HomeScreen.this,OrderAddActivity.class);
                        startActivity(intent);
                        break;
                case R.id.drawer_add_customer :
                    //drawerLayout.closeDrawer(GravityCompat.START);
                     intent = new Intent(HomeScreen.this,CustomerAddActivity.class);
                    startActivity(intent);
                    break;
                case R.id.drawer_add_employee :
                    //drawerLayout.closeDrawer(GravityCompat.START);
                     Toast.makeText(HomeScreen.this, "COMING SOON", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.drawer_orders :
                    //drawerLayout.closeDrawer(GravityCompat.START);
                    intent = new Intent(HomeScreen.this,OrderListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.drawer_customers :
                    //drawerLayout.closeDrawer(GravityCompat.START);
                    intent = new Intent(HomeScreen.this,CustomerListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.drawer_employees :
                    //drawerLayout.closeDrawer(GravityCompat.START);
                    Toast.makeText(HomeScreen.this, "COMING SOON", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.drawer_setting_activity :
                    //drawerLayout.closeDrawer(GravityCompat.START);
//                    intent = new Intent(HomeScreen.this,CustomerListActivity.class);
//                    startActivity(intent);
                    Toast.makeText(HomeScreen.this, "COMING SOON", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
            return true;
        }
    });

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




    }
    private void startOrderViewActivity(Long _id){
        Intent intent = new Intent(this, OrderViewActivity.class);
        intent.putExtra(OrderViewActivity.START_WITH_ID,_id);
        startActivity(intent);
    }

//    private class DrawerItemClickListener implements ListView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            selectItem(position);
//        }
//
//        public void selectItem(int position){
//            drawerLayout.closeDrawer(menu);
//        }
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
}
