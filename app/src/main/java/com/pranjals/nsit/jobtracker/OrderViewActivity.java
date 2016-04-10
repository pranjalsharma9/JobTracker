package com.pranjals.nsit.jobtracker;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

import java.util.ArrayList;

public class OrderViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);
        String OrderIdTobeViewed = "1";
        LinearLayout container = (LinearLayout)findViewById(R.id.orderView_container);
        LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ArrayList<String> extraCols = DBHelper.getInstance(OrderViewActivity.this).getExtraOrderCols();

        TextView name = (TextView)findViewById(R.id.orderView_name);
        TextView cid = (TextView)findViewById(R.id.orderView_cid);
        TextView eid = (TextView)findViewById(R.id.orderView_eid);
        TextView doo = (TextView)findViewById(R.id.orderView_doo);
        TextView doc = (TextView)findViewById(R.id.orderView_doc);
        TextView stageType = (TextView)findViewById(R.id.orderView_stageType);

        String[] colValues = new String[7 + extraCols.size()];


        Cursor c = getContentResolver().query(DBContentProvider.ORDER_URI, null, "_id = "+OrderIdTobeViewed, null, null);
        if(c!=null && c.moveToFirst()){
            for(int i=1;i<=colValues.length;i++)
                colValues[i-1] = c.getString(i);
            c.close();
        }

        name.setText(colValues[0]);
        cid.setText(colValues[1]);
        eid.setText(colValues[2]);
        doo.setText(colValues[3]);
        doc.setText(colValues[4]);
        String stageId = colValues[6];
        Cursor stageCursor = getContentResolver().query(DBContentProvider.STAGE_URI,null,"_id = "+stageId,null,null);
        if(stageCursor.moveToFirst()) {
            stageType.setText(stageCursor.getString(stageCursor.getColumnIndex("type")));
            stageCursor.close();
        }
        for(int i=0;i<extraCols.size();i++){
            View viewToAdd = inflater.inflate(R.layout.order_view_dynamic_row,null);
            TextView tv = (TextView)viewToAdd.findViewById(R.id.orderViewDynamic_tv);
            tv.setText(colValues[i+7]);
            container.addView(viewToAdd);
        }

    }
}
