package com.pranjals.nsit.jobtracker;

import android.os.Bundle;
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

import java.util.ArrayList;

/**
 * Created by Pranjal Verma on 4/15/2016.
 */
public class HomeScreen extends AppCompatActivity {

    RecyclerView menu;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

         drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
         menu = (RecyclerView)findViewById(R.id.list_slidermenu);
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
        ArrayList<String> menuItems = new ArrayList<>();
        menuItems.add("Orders");
        menuItems.add("Customers");
        menuItems.add("Employees");
        menuItems.add("Settings");
        NavDrawerListAdapter adapter = new NavDrawerListAdapter(menuItems,"Pranjal Verma","pranjal.verma80@gmail.com");
        adapter.setClickListener(new NavDrawerListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {

                    case 1:
                        Toast.makeText(HomeScreen.this, "Order List Activity launched", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(menu);
                        break;
                    case 2:
                        Toast.makeText(HomeScreen.this, "Customer List Activity launched", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(menu);
                        break;
                    case 3:
                        Toast.makeText(HomeScreen.this, "Employer List Activity launched", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(menu);
                        break;
                    case 4:
                        Toast.makeText(HomeScreen.this, "Settings Activity launched", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(menu);
                        break;
                    default:
                        Toast.makeText(HomeScreen.this, "Touched item unrecogonizable", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        menu.setAdapter(adapter);
        menu.setLayoutManager(new LinearLayoutManager(this));





//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);

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
            drawerLayout.openDrawer(menu);
        }
        return super.onOptionsItemSelected(item);
    }
}
