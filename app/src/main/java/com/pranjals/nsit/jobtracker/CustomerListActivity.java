package com.pranjals.nsit.jobtracker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

import java.util.ArrayList;

public class CustomerListActivity extends AppCompatActivity {

        View searchView;
        RecyclerView recyclerView;
        CustomerRecyclerView adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        ArrayList<Customer> customers = new ArrayList<>();
        final ArrayList<Customer> customersFinal = customers;

        String projection[] = {"_id", "name", "mobile", "email", "address","image"};
        Cursor c = getContentResolver().query(DBContentProvider.CUSTOMER_URI,projection,null,null,null);
        if (c.moveToFirst()) {
            do {
                String _id = c.getString(c.getColumnIndex("_id"));
                String name = c.getString(c.getColumnIndex("name"));
                String mobile = c.getString(c.getColumnIndex("mobile"));
                String email = c.getString(c.getColumnIndex("email"));
                String address = c.getString(c.getColumnIndex("address"));
                byte[] bb = c.getBlob(c.getColumnIndex("image"));
                Bitmap pic = BitmapFactory.decodeByteArray(bb, 0, bb.length);
                customers.add(new Customer(Long.parseLong(_id), name, mobile, email, address,pic));
            } while(c.moveToNext());
        }

        recyclerView = (RecyclerView)findViewById(R.id.CustomerRecyclerView);
         adapter = new CustomerRecyclerView(customers);
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
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_main, menu);

        MenuItem searchItem = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView)MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search customers");
            MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
                        @Override
                        public boolean onMenuItemActionExpand(MenuItem item) {
                            return true;
                        }

                        @Override
                        public boolean onMenuItemActionCollapse(MenuItem item) {

                            recyclerView.swapAdapter(adapter,true);

                            return true;
                        }
                    }
            );

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                String projection[] = {"_id", "name", "mobile", "email", "address","image"};
                String selection = "name = '"+query+"'";
                ArrayList<Customer> customers = new ArrayList<>();
                Cursor c = getContentResolver().query(DBContentProvider.CUSTOMER_URI,projection,selection,null,null);
                if (c!=null&&c.moveToFirst()) {
                    do {
                        String _id = c.getString(c.getColumnIndex("_id"));
                        String name = c.getString(c.getColumnIndex("name"));
                        String mobile = c.getString(c.getColumnIndex("mobile"));
                        String email = c.getString(c.getColumnIndex("email"));
                        String address = c.getString(c.getColumnIndex("address"));
                        byte[] bb = c.getBlob(c.getColumnIndex("image"));
                        Bitmap pic = BitmapFactory.decodeByteArray(bb, 0, bb.length);
                        customers.add(new Customer(Long.parseLong(_id), name, mobile, email, address,pic));
                    } while(c.moveToNext());
                }

                 CustomerRecyclerView sadapter = new CustomerRecyclerView(customers);
                recyclerView.swapAdapter(sadapter,true);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        switch (item.getItemId()){
//
//            case R.id.search_view:
//
//
//                LayoutInflater layoutInflater =
//                        (LayoutInflater)getBaseContext()
//                                .getSystemService(LAYOUT_INFLATER_SERVICE);
//                View popupView = layoutInflater.inflate(R.layout.popuowindow_searchview, null);
//                final PopupWindow popupWindow = new PopupWindow(
//                        popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
//                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                //popupWindow.setElevation(10);
//                Toolbar searchBar = (Toolbar)popupView.findViewById(R.id.search_toolbar);
//                searchBar.inflateMenu(R.menu.search_popup_menu);
//                Menu menu = searchBar.getMenu();
//                MenuItem searchItem = menu.findItem(R.id.action_search);
//                final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//                MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
//                    @Override
//                    public boolean onMenuItemActionExpand(MenuItem item) {
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onMenuItemActionCollapse(MenuItem item) {
//                        popupWindow.dismiss();
//                        return true;
//                    }
//                });
//
//
//                searchItem.expandActionView();
//                searchView.requestFocus();
//                //searchView.
//
//                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                    @Override
//                    public boolean onQueryTextSubmit(String query) {
//
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onQueryTextChange(String newText) {
//                        return false;
//                    }
//                });
//
//                float offsetPosition = 280f*(getResources().getDisplayMetrics().density);
//                float offsetPositiony = 30f*(getResources().getDisplayMetrics().density);
//
//                popupWindow.showAsDropDown(findViewById(R.id.search_view),(int)(-offsetPosition),(int)(-offsetPositiony));
//
//               //to dim the background as  popup window is opened
//                View container = (View) popupWindow.getContentView().getParent();
//                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//                WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
//                p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//                p.dimAmount = 0.5f;
//                wm.updateViewLayout(container, p);
//
//
//
//
//                break;

//        }

        return super.onOptionsItemSelected(item);
    }



}