package com.pranjals.nsit.jobtracker;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Pranjal Verma on 3/24/2016.
 */
public class MySimpleAdapterCustomer extends SimpleCursorAdapter {

    public MySimpleAdapterCustomer(Context context,int layout,Cursor c,String[] from,int[] to){
        super(context,layout,c,from,to);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return super.newView(context, cursor, parent);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
    }
}
