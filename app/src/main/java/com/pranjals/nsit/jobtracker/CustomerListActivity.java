package com.pranjals.nsit.jobtracker;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

import java.util.ArrayList;

public class CustomerListActivity extends AppCompatActivity {

    public static final int CALL_CUSTOMER_LIST_FOR_EDIT_TEXT = 18;
    private String sortOrder;
    private PopupWindow openedPopupWindow;
    ArrayList<Customer> customers;
    RecyclerView recyclerView;
    CustomerRecyclerView adapter;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        sortOrder = null;

        intent = getIntent();

        customers = new ArrayList<>();

        /*String projection[] = {"_id", "name", "mobile", "email", "address"};
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
        }*/

        recyclerView = (RecyclerView)findViewById(R.id.CustomerRecyclerView);
        adapter = new CustomerRecyclerView(customers);
        refreshListOnSort(sortOrder);
        adapter.setOnItemClickListener(new CustomerRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, long _id, String name) {
                if(intent.hasExtra("CallingForCustomerNameEditText")){
                    returnToOrderAddActivity(_id, name);
                }
                else{
                    startCustomerViewActivity(_id);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void startCustomerViewActivity(Long _id){
        Intent newIntent = new Intent(this, CustomerViewActivity.class);
        newIntent.putExtra(CustomerViewActivity.START_WITH_ID, _id);
        startActivity(newIntent);
        finish();
    }

    private void returnToOrderAddActivity(Long _id, String name){
        intent.putExtra("cid", _id);
        intent.putExtra("customerName", name);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            /*case R.id.search_view:


                LayoutInflater layoutInflater =
                        (LayoutInflater)getBaseContext()
                                .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popuowindow_searchview, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //popupWindow.setElevation(10);
                Toolbar searchBar = (Toolbar)popupView.findViewById(R.id.search_toolbar);
                searchBar.inflateMenu(R.menu.search_popup_menu);
                Menu menu = searchBar.getMenu();
                MenuItem searchItem = menu.findItem(R.id.action_search);
                final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
                MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        popupWindow.dismiss();
                        return true;
                    }
                });


                searchItem.expandActionView();
                searchView.requestFocus();
                //searchView.

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });

                float offsetPosition = 280f*(getResources().getDisplayMetrics().density);
                float offsetPositiony = 30f*(getResources().getDisplayMetrics().density);

                popupWindow.showAsDropDown(findViewById(R.id.search_view),(int)(-offsetPosition),(int)(-offsetPositiony));

               //to dim the background as  popup window is opened
                View container = (View) popupWindow.getContentView().getParent();
                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
                p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                p.dimAmount = 0.5f;
                wm.updateViewLayout(container, p);




                break;*/
            case android.R.id.home : finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume(){
        super.onResume();
        // refreshListOnSort(sortOrder);
    }

    public void onSortByButtonClicked(View view){
        LayoutInflater layoutInflater = getLayoutInflater();
        final View popupView = layoutInflater.inflate(R.layout.popup_window_sort_customer_by, null);
        if(sortOrder != null){
            String tag;
            if(sortOrder.contains(" DESC")){
                tag = sortOrder.substring(0, sortOrder.length() - 5);
                ((ImageView) ((ViewGroup)(((ViewGroup) popupView).findViewWithTag(tag))).getChildAt(1)).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_expand_more_black_18dp));
            }
            else{
                tag = sortOrder;
                ((ImageView) ((ViewGroup)(((ViewGroup) popupView).findViewWithTag(tag))).getChildAt(1)).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_expand_less_black_18dp));
            }
        }
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        float offsetPosition = 18f*(getResources().getDisplayMetrics().density);
        popupWindow.showAsDropDown(view, 0, (int) (-offsetPosition));
        openedPopupWindow = popupWindow;
    }

    public void onSortBySelected(View view){
        String sortBy = view.getTag().toString();
        if(sortOrder == sortBy) {
            sortOrder = (sortBy + " DESC");
        }
        else if(sortOrder == (sortBy + " DESC")) {
            sortOrder = sortBy.substring(0, sortBy.length() - 5);
        }
        else {
            sortOrder = sortBy;
        }

        refreshListOnSort(sortOrder);

        openedPopupWindow.dismiss();
    }

    public void refreshListOnSort(String sortOrder){

        customers = new ArrayList<>();

        String projection[] = {"_id", "name", "mobile", "email", "address"};
        Cursor c = getContentResolver().query(DBContentProvider.CUSTOMER_URI,projection,null,null,sortOrder);
        if (c.moveToFirst()) {
            do {
                String _id = c.getString(c.getColumnIndex("_id"));
                String name = c.getString(c.getColumnIndex("name"));
                String mobile = c.getString(c.getColumnIndex("mobile"));
                String email = c.getString(c.getColumnIndex("email"));
                String address = c.getString(c.getColumnIndex("address"));
                customers.add(new Customer(Long.parseLong(_id), name, mobile, email, address));
            } while (c.moveToNext());
        }

        adapter = new CustomerRecyclerView(customers);
        recyclerView.swapAdapter(adapter, true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_main, menu);

        MenuItem searchItem = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
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
                customers = new ArrayList<Customer>();
                String selection = "name like '%" + query + "%'";
                String projection[] = {"_id", "name", "mobile", "email", "address"};
                Cursor c = getContentResolver().query(DBContentProvider.CUSTOMER_URI,projection,selection,null,null);
                if (c!=null&&c.moveToFirst()) {
                    do {
                        String _id = c.getString(c.getColumnIndex("_id"));
                        String name = c.getString(c.getColumnIndex("name"));
                        String mobile = c.getString(c.getColumnIndex("mobile"));
                        String email = c.getString(c.getColumnIndex("email"));
                        String address = c.getString(c.getColumnIndex("address"));
                        customers.add(new Customer(Long.parseLong(_id), name, mobile, email, address));
                    } while (c.moveToNext());
                        byte[] bb = c.getBlob(c.getColumnIndex("image"));
                        Bitmap pic = BitmapFactory.decodeByteArray(bb, 0, bb.length);
                        customers.add(new Customer(Long.parseLong(_id), name, mobile, email, address,pic));
                    } while(c.moveToNext());
                }

                CustomerRecyclerView cadapter = new CustomerRecyclerView(customers);
                recyclerView.swapAdapter(cadapter, true);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                customers = new ArrayList<Customer>();
                String selection = "name like '%" + newText + "%'";
                String projection[] = {"_id", "name", "mobile", "email", "address"};
                Cursor c = getContentResolver().query(DBContentProvider.CUSTOMER_URI,projection,selection,null,null);
                if (c.moveToFirst()) {
                    do {
                        String _id = c.getString(c.getColumnIndex("_id"));
                        String name = c.getString(c.getColumnIndex("name"));
                        String mobile = c.getString(c.getColumnIndex("mobile"));
                        String email = c.getString(c.getColumnIndex("email"));
                        String address = c.getString(c.getColumnIndex("address"));
                        customers.add(new Customer(Long.parseLong(_id), name, mobile, email, address));
                    } while (c.moveToNext());
                }

                CustomerRecyclerView cadapter = new CustomerRecyclerView(customers);
                recyclerView.swapAdapter(cadapter, true);

                return false;
            }
        });


        return true;
    }

}