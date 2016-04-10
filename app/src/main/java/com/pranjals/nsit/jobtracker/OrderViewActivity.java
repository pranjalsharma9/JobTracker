package com.pranjals.nsit.jobtracker;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

import java.util.ArrayList;

public class OrderViewActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int totalStages;
    private int currentStage;

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

        for(int i=0;i<extraCols.size();i++){
            View viewToAdd = inflater.inflate(R.layout.order_view_dynamic_row,null);
            TextView tv = (TextView)viewToAdd.findViewById(R.id.orderViewDynamic_tv);
            tv.setText(colValues[i+7]);
            container.addView(viewToAdd);
        }

        //Setting the progress bar
        progressBar = (ProgressBar) findViewById(R.id.view_order_progress_bar);
        totalStages = stageCursor.getInt(stageCursor.getColumnIndex("total"));
        String[] stageNames = stageCursor.getString(stageCursor.getColumnIndex("names")).split(";");
        currentStage = Integer.parseInt(colValues[5]);
        progressBar.setMax(totalStages);
        progressBar.setProgress(currentStage);

        ViewGroup stepperViewGroup = (ViewGroup) findViewById(R.id.stepper_view_group);
        LayoutInflater layoutInflater = getLayoutInflater();

        ViewGroup stepperItem;
        TextView textView;

        for(int i = 0; i < currentStage - 1; i++){
            stepperItem = (ViewGroup) layoutInflater.inflate(R.layout.stepper_item, null);
            stepperItem.getChildAt(0).setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stepper_circle_done));
            ((TextView) stepperItem.getChildAt(1)).setText(stageNames[i]);
        }

        stepperItem = (ViewGroup) layoutInflater.inflate(R.layout.stepper_item, null);
        textView = (TextView) stepperItem.getChildAt(0);
        textView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stepper_circle));
        textView.setText((currentStage - 1));
        ((TextView) stepperItem.getChildAt(1)).setText(stageNames[currentStage - 1]);

        for(int i = currentStage; i < totalStages; i++){
            stepperItem = (ViewGroup) layoutInflater.inflate(R.layout.stepper_item, null);
            textView = (TextView) stepperItem.getChildAt(0);
            textView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stepper_circle_pending));
            textView.setText((currentStage - 1));
            ((TextView) stepperItem.getChildAt(1)).setText(stageNames[i]);
        }
    }

}
