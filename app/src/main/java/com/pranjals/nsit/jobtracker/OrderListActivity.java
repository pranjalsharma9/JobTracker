package com.pranjals.nsit.jobtracker;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

public class OrderListActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private SimpleCursorAdapter adapter;
    private LoaderManager loaderManager;
    private CursorLoader cursorLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        loaderManager = getLoaderManager();

        //the names of the columns that are to be mapped
        String[] bindFrom = {"name", "doo", "doc", "cid"};

        //the ids of the view where the columns are to be mapped
        int[] bindTo = {R.id.orderCard_name, R.id.orderCard_doo, R.id.orderCard_doc, R.id.orderCard_cid};

        adapter = new SimpleCursorAdapter(this, R.layout.order_card, null, bindFrom, bindTo, 0);
        setListAdapter(adapter);
        loaderManager.initLoader(1, null, this);
    }

    //link the cursorLoader with the DBContentProvider
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection= {"_id", "name", "doo", "doc", "cid"};
        cursorLoader = new CursorLoader(OrderListActivity.this, DBContentProvider.ORDER_URI,projection, null, null, null);
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
