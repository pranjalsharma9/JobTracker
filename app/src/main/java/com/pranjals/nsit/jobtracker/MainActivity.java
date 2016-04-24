package com.pranjals.nsit.jobtracker;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pranjals.nsit.jobtracker.BuildDB.BuildDBActivity;
import com.pranjals.nsit.jobtracker.GoogleSignIn.SignInActivity;
import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private final String isFirstTime = "isFirstTime";
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ArrayList<Order> orders;
    OrderRecyclerView orderAdapter;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    private GoogleApiClient apiClient;
    private int BUILDB_INTENT = 3,SIGN_IN = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_navigation_drawer);


         sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);;
        if(!(sharedPreferences.contains(isFirstTime))){
            Intent intent = new Intent(this, BuildDBActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, BUILDB_INTENT);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
        apiClient =  new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();


        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.fab_scan);

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
                    case R.id.drawer_signIn_activity:
                        intent = new Intent(MainActivity.this, SignInActivity.class);
                        startActivityForResult(intent,SIGN_IN);

                    default:
                        break;
                }
                return true;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.initiateScan();

            }
        });

        orders = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.OrderRecyclerView_homescreen);
        orderAdapter = new OrderRecyclerView(orders);
        orderAdapter.setOnItemClickListener(new OrderRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, long _id) {
                startOrderViewActivity(_id);

            }
        });
        recyclerView.setAdapter(orderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(sharedPreferences.contains(isFirstTime)) {
           // Log.e("sdsdsddss","sdsdsds");
           // orders = new ArrayList<>();
            String projection[] = {"_id", "name", "doo", "doc", "cid", "eid", "curStage", "totalStages"};
            Cursor c = getContentResolver().query(DBContentProvider.ORDER_URI, projection, null, null, "date(doc)");
            if (c.moveToFirst()) {
                do {
                    String _id = c.getString(c.getColumnIndex("_id"));
                    String name = c.getString(c.getColumnIndex("name"));
                    String doo = DBHelper.getDDMMYYYY(c.getString(c.getColumnIndex("doo")));
                    String doc = DBHelper.getDDMMYYYY(c.getString(c.getColumnIndex("doc")));
                    String cid = c.getString(c.getColumnIndex("cid"));
                    String eid = c.getString(c.getColumnIndex("eid"));
                    int curStage = c.getInt(c.getColumnIndex("curStage"));
                    int totalStages = c.getInt(c.getColumnIndex("totalStages"));
                    orders.add(new Order(Long.parseLong(_id), name, Long.parseLong(cid), Long.parseLong(eid), doo, doc, curStage, totalStages));
                } while (c.moveToNext());
            }

        }

    }

    private void startOrderViewActivity(Long _id){
        Intent intent = new Intent(this, OrderViewActivity.class);
        intent.putExtra(OrderViewActivity.START_WITH_ID,_id);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_main, menu);
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
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(scanResult!=null&&IntentIntegrator.REQUEST_CODE==requestCode&&resultCode==RESULT_OK){
                 long scanned_orderId;
            try{ scanned_orderId= Long.parseLong(scanResult.getContents());
                Intent intent = new Intent(MainActivity.this,OrderViewActivity.class);
                intent.putExtra(OrderViewActivity.START_WITH_ID,scanned_orderId);
                startActivity(intent);}
            catch (Exception e){
                Toast.makeText(MainActivity.this,"Wrong Scanned id",Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode==0&&resultCode==RESULT_OK){
            orders = new ArrayList<>();
            String projection[] = {"_id","name", "doo", "doc", "cid", "eid", "curStage", "totalStages"};

            Cursor c = getContentResolver().query(DBContentProvider.ORDER_URI,projection,null,null,"date(doc)");
            if (c.moveToFirst()) {
                do {
                    String _id = c.getString(c.getColumnIndex("_id"));
                    String name = c.getString(c.getColumnIndex("name"));
                    String doo = DBHelper.getDDMMYYYY(c.getString(c.getColumnIndex("doo")));
                    String doc = DBHelper.getDDMMYYYY(c.getString(c.getColumnIndex("doc")));
                    String cid = c.getString(c.getColumnIndex("cid"));
                    String eid = c.getString(c.getColumnIndex("eid"));
                    int curStage = c.getInt(c.getColumnIndex("curStage"));
                    int totalStages = c.getInt(c.getColumnIndex("totalStages"));
                    orders.add(new Order(Long.parseLong(_id), name, Long.parseLong(cid), Long.parseLong(eid), doo, doc, curStage, totalStages));
                } while(c.moveToNext());
            }

            orderAdapter = new OrderRecyclerView(orders);
            recyclerView.swapAdapter(orderAdapter, false);

        }

        if(requestCode==BUILDB_INTENT&&resultCode==RESULT_OK){
//            Log.e("ghgjhgj","yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
            Intent signInIntent = new Intent(MainActivity.this,SignInActivity.class);
            startActivityForResult(signInIntent,SIGN_IN);
        }

        if(requestCode==SIGN_IN&&resultCode==RESULT_OK){
                setSignInMenu(sharedPreferences.contains("ownerName"));
                String ownerName = sharedPreferences.getString("ownerName","You are not logged in");
                String ownerEmail = sharedPreferences.getString("ownerEmail", "Sign in from below");
                setNavigationMenuHeader(ownerName, ownerEmail);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public boolean chechIfSignedIn(){

        if(sharedPreferences.contains("ownerName")){
            return true;
        }

        return false;

//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(apiClient);
//
//        if(opr.isDone()){
//            GoogleSignInResult result = opr.get();
//            if(result.isSuccess()){
//                GoogleSignInAccount acct = result.getSignInAccount();
//                setNavigationMenuHeader(acct.getDisplayName(), acct.getEmail());
//                return true;
//            }
//        }
//
//        else{
//            setNavigationMenuHeader("You are not logged in","Sign in from below");
//        }
//
//        return false;
    }


    public void setSignInMenu(boolean isSignedIn){
        if(isSignedIn){
            MenuItem item = navigationView.getMenu().getItem(5);
            item.setTitle("Sign out");
        }
        else
        {
            MenuItem item = navigationView.getMenu().getItem(5);
            item.setTitle("Sign in");
        }
    }

    public void setNavigationMenuHeader(String ownerName,String ownerEmail){
        View headerView = navigationView.getHeaderView(0);
        TextView ownerNametv = (TextView)headerView.findViewById(R.id.owner_name);
        TextView ownerEmailtv = (TextView)headerView.findViewById(R.id.owner_email);
        ownerNametv.setText(ownerName);
        ownerEmailtv.setText(ownerEmail);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        //checks whether user has signed in and sets the
        // title of menu in navigation drawer
        setSignInMenu(chechIfSignedIn());
        setNavigationMenuHeader(sharedPreferences.getString("ownerName","You are not logged in"),sharedPreferences.getString("ownerEmail", "Sign in from below"));


    }
}
