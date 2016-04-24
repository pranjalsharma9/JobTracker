package com.pranjals.nsit.jobtracker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

import java.util.ArrayList;

public class CustomerViewActivity extends AppCompatActivity {

    private long customerIdTobeViewed;
    private String[] colValues;
    private String[] colNames;
    public static final String START_WITH_ID = "_idOfCustomerToView";
    private ArrayList<String> extraCols;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        customerIdTobeViewed = getIntent().getLongExtra(START_WITH_ID, 1);
        LinearLayout container = (LinearLayout)findViewById(R.id.customerView_container);
        LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        extraCols = DBHelper.getInstance(CustomerViewActivity.this).getExtraCols(1);

        TextView name = (TextView)findViewById(R.id.customerView_name);
        TextView mobile = (TextView)findViewById(R.id.customerView_mobile);
        TextView email = (TextView)findViewById(R.id.customerView_email);
        TextView address = (TextView)findViewById(R.id.customerView_address);
        ImageView image = (ImageView)findViewById(R.id.iv_customerView);
        colValues = new String[DBHelper.DEF_CUSTOMER_COLS + extraCols.size()-1];

        Cursor c = getContentResolver().query(DBContentProvider.CUSTOMER_URI, null, "_id = " + customerIdTobeViewed, null, null);
        if(c!=null && c.moveToFirst()){
            for(int i=0;i<colValues.length;i++) {
                colValues[i] = c.getString(i+2);
            }
            byte[] bb = c.getBlob(c.getColumnIndex("image"));
            image.setImageBitmap(BitmapFactory.decodeByteArray(bb, 0, bb.length));

            c.close();
        }



        colNames = c.getColumnNames();

        name.setText(colValues[0]);
        mobile.setText(colValues[1]);
        email.setText(colValues[2]);
        address.setText(colValues[3]);

        for(int i=0;i<extraCols.size();i++){
            View viewToAdd = inflater.inflate(R.layout.customer_view_dynamic_row,null);
            TextView tv = (TextView)viewToAdd.findViewById(R.id.customerViewDynamic_tv);
            tv.setId(i);
            tv.setText(colValues[i+4]);
            container.addView(viewToAdd);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_view,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.edit_customer:
                Intent intent = new Intent(CustomerViewActivity.this,CustomerEditActivity.class);
                intent.putExtra("customerId", customerIdTobeViewed);
                startActivityForResult(intent, 15);
            break;

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==15&&resultCode==RESULT_OK){

            //refreshUi
            TextView name = (TextView)findViewById(R.id.customerView_name);
            TextView mobile = (TextView)findViewById(R.id.customerView_mobile);
            TextView email = (TextView)findViewById(R.id.customerView_email);
            TextView address = (TextView)findViewById(R.id.customerView_address);
            ImageView image = (ImageView)findViewById(R.id.iv_customerView);


            Cursor c = getContentResolver().query(DBContentProvider.CUSTOMER_URI, null, "_id = "+ customerIdTobeViewed, null, null);
            if(c!=null&&c.moveToFirst()){
////
               name.setText(c.getString(2));
                mobile.setText(c.getString(3));
               email.setText(c.getString(4));
                address.setText(c.getString(5));

                for(int i=0;i<extraCols.size();i++)
                {
                    ((TextView)findViewById(i)).setText(c.getString(i+6));
                }

                byte[] bb = c.getBlob(c.getColumnIndex("image"));
                image.setImageBitmap(BitmapFactory.decodeByteArray(bb, 0, bb.length));

                c.close();
//
          }


        }
    }


}
