package com.pranjals.nsit.jobtracker;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

/**
 * Created by Pranjal Verma on 3/22/2016.
 */
public class CustomerListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter adapter;
    private LoaderManager loaderManager;
    private CursorLoader cursorLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        loaderManager = getLoaderManager();
        Log.e("luck","jskjdjskdjksjkdsdsdsdssdsssdsssss");
        //the names of the columns that are to be mapped
        String[] bindFrom = {"name", "mobile", "email", "address"};
        ListView listView = (ListView)findViewById(R.id.customer_ListView);
        //the ids of the view where the columns are to be mapped
        int[] bindTo = {R.id.customerCard_name, R.id.customerCard_mobile, R.id.customerCard_email, R.id.customerCard_address};
        adapter = new SimpleCursorAdapter(this,R.layout.customer_card,null,bindFrom,bindTo,0);

        listView.setAdapter(adapter);

        loaderManager.initLoader(1, null, this);



    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {"_id","name", "mobile", "email", "address"};
        cursorLoader = new CursorLoader(CustomerListActivity.this, DBContentProvider.CUSTOMER_URI,projection,null,null,null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
         adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);

    }
}
