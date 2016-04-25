package com.pranjals.nsit.jobtracker;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

import java.util.ArrayList;

public class OrderViewActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int totalStages;
    private int currentStage;
    private float stepperViewGroupHeight;
    private float doneButtonHeight;
    private float stepperMinHeight;
    private float progressBarGroupHeight;
    private boolean isStepperOpen;
    private boolean isFirstLayoutLoad;
    private long orderIdTobeViewed;
    private String[] colValues;
    private String[] colNames;
    public static final String START_WITH_ID = "_idOfOrderToView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        orderIdTobeViewed = getIntent().getLongExtra(START_WITH_ID, 1);
        stepperMinHeight = 64f*(getResources().getDisplayMetrics().density);
        doneButtonHeight = 40f*(getResources().getDisplayMetrics().density);
        LinearLayout container = (LinearLayout)findViewById(R.id.orderView_container);
        LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        isFirstLayoutLoad = true;

        //Remove for testing
        ArrayList<String> extraCols = DBHelper.getInstance(OrderViewActivity.this).getExtraCols(0);

        /*Just for testing, Temporary code
        ArrayList<String> extraCols = new ArrayList<>();
        extraCols.add("ExtraField1");
        extraCols.add("ExtraField2");
        extraCols.add("ExtraField3");
        extraCols.add("ExtraField4");
        //testing code ends*/

        TextView name = (TextView)findViewById(R.id.orderView_name);
        TextView cid = (TextView)findViewById(R.id.orderView_cid);
        TextView eid = (TextView)findViewById(R.id.orderView_eid);
        TextView doo = (TextView)findViewById(R.id.orderView_doo);
        TextView doc = (TextView)findViewById(R.id.orderView_doc);

        colValues = new String[DBHelper.DEF_ORDER_COLS + extraCols.size()];

        //Remove for testing
        Cursor c = getContentResolver().query(DBContentProvider.ORDER_URI, null, "_id = "+ orderIdTobeViewed, null, null);

        if(c!=null && c.moveToFirst()){
            for(int i=1;i<=colValues.length;i++)
                colValues[i-1] = c.getString(i);
            c.close();
        }

        colNames = c.getColumnNames();
        //Remove comments when tested

        /*For testing purpose only, Remove when tested
        for(int i=0;i<colValues.length;i++)
            colValues[i] = "Column Value : " + i;
        //testing code ends*/

        name.setText(colValues[0]);
        cid.setText(colValues[1]);
        eid.setText(colValues[2]);
        doo.setText(colValues[3]);
        doc.setText(colValues[4]);

        //Remove for testing purposes, Remove comments when testing is complete
        String stageId = colValues[6];
        Cursor stageCursor = getContentResolver().query(DBContentProvider.STAGE_URI,null,"_id = ?",new String[]{stageId},null);

        for(int i=0;i<extraCols.size();i++){
            View viewToAdd = inflater.inflate(R.layout.order_view_dynamic_row,null);
            TextView tv = (TextView)viewToAdd.findViewById(R.id.orderViewDynamic_tv);
            tv.setText(colValues[i+DBHelper.DEF_ORDER_COLS]);
            container.addView(viewToAdd);
        }

        //Setting the progress bar
        progressBar = (ProgressBar) findViewById(R.id.view_order_progress_bar);

        //Remove for testing purposes, Remove comments when done
        String[] stageNames;

        if(stageCursor!=null&&stageCursor.moveToFirst()) {

            totalStages = stageCursor.getInt(stageCursor.getColumnIndex("total"));
            stageNames = stageCursor.getString(stageCursor.getColumnIndex("names")).split(";");
            currentStage = Integer.parseInt(colValues[5]);
            stageCursor.close();
        }
        else
        {   stageNames= new String[0];
             finish();
        }

        /*For Testing Only!!!!
        totalStages = 7;
        currentStage = 4;
        String[] stageNames = "Stage1;Stage2;Stage3;Stage4;Stage5;Stage6;Stage7".split(";");
        */


        progressBar.setMax(totalStages);
        progressBar.setProgress(currentStage);

        ViewGroup stepperViewGroup = (ViewGroup) findViewById(R.id.stepper_view_group);
        LayoutInflater layoutInflater = getLayoutInflater();

        ViewGroup stepperItem = null;
        TextView textView;

        RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                (int) doneButtonHeight);
        buttonLayoutParams.addRule(RelativeLayout.BELOW, R.id.stepper_item_stage_name);
        buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        buttonLayoutParams.setMargins(12, 12, 12, 12);

        ViewGroup stepperCircleLineContainer;
        ViewGroup.LayoutParams layoutParams;

        for(int i = 0; i < currentStage; i++){
            stepperItem = (ViewGroup) layoutInflater.inflate(R.layout.stepper_item, null);
            stepperItem.findViewById(R.id.stepper_circle_container).setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stepper_circle_done));
            ((TextView) stepperItem.getChildAt(1)).setText(stageNames[i]);
            stepperCircleLineContainer = (ViewGroup) stepperItem.findViewById(R.id.stepper_circle_line_container);
            layoutParams = stepperCircleLineContainer.getLayoutParams();
            layoutParams.height = 0;
            stepperCircleLineContainer.setLayoutParams(layoutParams);
            stepperItem.measure(0, 0);
            if(stepperItem.getMeasuredHeight() < stepperMinHeight){
                layoutParams.height = (int) stepperMinHeight;
                stepperCircleLineContainer.setLayoutParams(layoutParams);
            } else {
                layoutParams.height = stepperItem.getMeasuredHeight();
                stepperCircleLineContainer.setLayoutParams(layoutParams);
            }
            stepperViewGroup.addView(stepperItem);
        }

        if(currentStage < totalStages) {
            stepperItem = (ViewGroup) layoutInflater.inflate(R.layout.stepper_item, null);
            Button stepperDoneButton = (Button) layoutInflater.inflate(R.layout.stepper_done_button, null);
            stepperItem.addView(stepperDoneButton, buttonLayoutParams);
            textView = (TextView) stepperItem.findViewById(R.id.stepper_circle_container);
            textView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stepper_circle));
            textView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/RobotoTTF/Roboto-Regular.ttf"));
            textView.setText(Integer.toString(currentStage + 1));
            ((TextView) stepperItem.getChildAt(1)).setText(stageNames[currentStage]);
            stepperCircleLineContainer = (ViewGroup) stepperItem.findViewById(R.id.stepper_circle_line_container);
            layoutParams = stepperCircleLineContainer.getLayoutParams();
            layoutParams.height = 0;
            stepperCircleLineContainer.setLayoutParams(layoutParams);
            stepperItem.measure(0, 0);
            if(stepperItem.getMeasuredHeight() < stepperMinHeight){
                layoutParams.height = (int) stepperMinHeight;
                stepperCircleLineContainer.setLayoutParams(layoutParams);
            } else {
                layoutParams.height = stepperItem.getMeasuredHeight();
                stepperCircleLineContainer.setLayoutParams(layoutParams);
            }
            stepperViewGroup.addView(stepperItem);
        }

        for(int i = currentStage + 1; i < totalStages; i++){
            stepperItem = (ViewGroup) layoutInflater.inflate(R.layout.stepper_item, null);
            textView = (TextView) stepperItem.findViewById(R.id.stepper_circle_container);
            textView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stepper_circle_pending));
            textView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/RobotoTTF/Roboto-Regular.ttf"));
            textView.setText(Integer.toString(i + 1));
            ((TextView) stepperItem.getChildAt(1)).setText(stageNames[i]);
            stepperCircleLineContainer = (ViewGroup) stepperItem.findViewById(R.id.stepper_circle_line_container);
            layoutParams = stepperCircleLineContainer.getLayoutParams();
            layoutParams.height = 0;
            stepperCircleLineContainer.setLayoutParams(layoutParams);
            stepperItem.measure(0, 0);
            if(stepperItem.getMeasuredHeight() < stepperMinHeight){
                layoutParams.height = (int) stepperMinHeight;
                stepperCircleLineContainer.setLayoutParams(layoutParams);
            } else {
                layoutParams.height = stepperItem.getMeasuredHeight();
                stepperCircleLineContainer.setLayoutParams(layoutParams);
            }
            stepperViewGroup.addView(stepperItem);
        }

        if(stepperItem != null) stepperItem.findViewById(R.id.stepper_line).setVisibility(View.GONE);

        //To hide the stepperViewGroup
        final View orderProgressGroup = findViewById(R.id.order_progress_group);
        ViewTreeObserver vto = orderProgressGroup.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //getting stepperViewGroup's height on first load, and hiding it initially.
                View stepperViewGroup = findViewById(R.id.stepper_view_group);
                if(isFirstLayoutLoad) {
                    stepperViewGroupHeight = stepperViewGroup.getHeight();
                    progressBarGroupHeight = findViewById(R.id.progress_bar_container).getHeight();

                    View fillUpView = findViewById(R.id.orderView_fill_up_view);
                    ViewGroup.LayoutParams fillUpLayoutParams = fillUpView.getLayoutParams();
                    fillUpLayoutParams.height = (int) progressBarGroupHeight;
                    fillUpView.setLayoutParams(fillUpLayoutParams);

                    isFirstLayoutLoad = false;

                    ViewGroup.LayoutParams orderProgressGroupLayoutParams = findViewById(R.id.order_progress_group).getLayoutParams();
                    orderProgressGroupLayoutParams.height = (int) progressBarGroupHeight;
                    findViewById(R.id.order_progress_group).setLayoutParams(orderProgressGroupLayoutParams);

                    /*adjusting the steppers' vertical 1dp thick lines to suit the height of the stepper.
                    for (int i = 0; i < totalStages; i++) {
                        View stepperItem = ((ViewGroup) stepperViewGroup).getChildAt(i);
                        ViewGroup.LayoutParams layoutParams;
                        if (stepperItem.getHeight() > stepperMinHeight) {
                            layoutParams = stepperItem.findViewById(R.id.stepper_circle_line_container).getLayoutParams();
                            layoutParams.height = stepperItem.getHeight();
                            stepperItem.findViewById(R.id.stepper_circle_line_container).setLayoutParams(layoutParams);
                        } else {
                            layoutParams = stepperItem.findViewById(R.id.stepper_circle_line_container).getLayoutParams();
                            layoutParams.height = (int) stepperMinHeight;
                            stepperItem.findViewById(R.id.stepper_circle_line_container).setLayoutParams(layoutParams);
                        }
                    }*/
                }

                ViewTreeObserver viewTreeObserver = orderProgressGroup.getViewTreeObserver();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this);
                } else {
                    viewTreeObserver.removeGlobalOnLayoutListener(this);
                }
            }

        });

        isStepperOpen = false;

    }

    public void onToggleButtonTapped(View button){
        AnimatorSet hideShow = new AnimatorSet();
        View view = findViewById(R.id.order_progress_group);
        final View finalView = view;
        ObjectAnimator hideProgressBarAnimation, dropOrderViewOtherDetails, rotateButtonAnimation;
        ValueAnimator heightAnimation;
        if(!isStepperOpen) {
            hideProgressBarAnimation = ObjectAnimator.ofFloat(view,
                    "translationY",
                    0,
                    -progressBarGroupHeight);

            dropOrderViewOtherDetails = ObjectAnimator.ofFloat(findViewById(R.id.orderView_other_details_layout),
                    "translationY",
                    0,
                    -progressBarGroupHeight);

            rotateButtonAnimation = ObjectAnimator.ofFloat(button, "rotation", 0f, 180f);

            heightAnimation = ValueAnimator.ofInt((int) progressBarGroupHeight, (int) (progressBarGroupHeight + stepperViewGroupHeight));

            isStepperOpen = true;
        }
        else {
            hideProgressBarAnimation = ObjectAnimator.ofFloat(view,
                    "translationY",
                    -progressBarGroupHeight,
                    0);

            dropOrderViewOtherDetails = ObjectAnimator.ofFloat(findViewById(R.id.orderView_other_details_layout),
                    "translationY",
                    -progressBarGroupHeight,
                    0);

            rotateButtonAnimation = ObjectAnimator.ofFloat(button, "rotation", 180f, 0f);

            heightAnimation = ValueAnimator.ofInt(view.getMeasuredHeight(), (int) progressBarGroupHeight);

            isStepperOpen = false;
        }

        heightAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = finalView.getLayoutParams();
                layoutParams.height = val;
                finalView.setLayoutParams(layoutParams);
            }
        });
        heightAnimation.setDuration(500);
        hideProgressBarAnimation.setDuration(500);
        dropOrderViewOtherDetails.setDuration(500);
        rotateButtonAnimation.setDuration(500);
        hideShow.play(hideProgressBarAnimation).with(dropOrderViewOtherDetails);
        hideShow.play(rotateButtonAnimation).with(hideProgressBarAnimation);
        hideShow.play(heightAnimation).with(hideProgressBarAnimation);
        hideShow.start();
    }

    public void onStepperDoneButtonTapped(View view){
        View stepperViewGroup = findViewById(R.id.stepper_view_group);
        View currentStepperItem = ((ViewGroup) stepperViewGroup).getChildAt(currentStage);
        ((ViewGroup) currentStepperItem).removeView(view);
        TextView currentStepperCircle = (TextView) currentStepperItem.findViewById(R.id.stepper_circle_container);
        ViewGroup.LayoutParams layoutParams;
        layoutParams = currentStepperItem.findViewById(R.id.stepper_circle_line_container).getLayoutParams();
        layoutParams.height = 0;
        currentStepperItem.findViewById(R.id.stepper_circle_line_container).setLayoutParams(layoutParams);
        currentStepperItem.measure(0, 0);
        if(currentStepperItem.getMeasuredHeight() >= stepperMinHeight){
            layoutParams.height = currentStepperItem.getMeasuredHeight();
            currentStepperItem.findViewById(R.id.stepper_circle_line_container).setLayoutParams(layoutParams);
        }
        else{
            layoutParams.height = (int) stepperMinHeight;
            currentStepperItem.findViewById(R.id.stepper_circle_line_container).setLayoutParams(layoutParams);
        }
        currentStepperCircle.setText("");
        currentStepperCircle.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stepper_circle_done));

        //for the new currentStage
        currentStage++;
        if(currentStage < totalStages) {
            currentStepperItem = ((ViewGroup) stepperViewGroup).getChildAt(currentStage);
            RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    (int) doneButtonHeight);
            buttonLayoutParams.addRule(RelativeLayout.BELOW, R.id.stepper_item_stage_name);
            buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            buttonLayoutParams.setMargins(12, 12, 12, 12);
            Button stepperDoneButton = (Button) getLayoutInflater().inflate(R.layout.stepper_done_button, null);
            ((ViewGroup) currentStepperItem).addView(stepperDoneButton, buttonLayoutParams);
            currentStepperItem.findViewById(R.id.stepper_circle_container).setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stepper_circle));
            layoutParams = currentStepperItem.findViewById(R.id.stepper_circle_line_container).getLayoutParams();
            layoutParams.height = 0;
            currentStepperItem.findViewById(R.id.stepper_circle_line_container).setLayoutParams(layoutParams);
            currentStepperItem.measure(0, 0);
            if (currentStepperItem.getMeasuredHeight() >= stepperMinHeight) {
                layoutParams.height = currentStepperItem.getMeasuredHeight();
                currentStepperItem.findViewById(R.id.stepper_circle_line_container).setLayoutParams(layoutParams);
            }
            else{
                layoutParams.height = (int) stepperMinHeight;
                currentStepperItem.findViewById(R.id.stepper_circle_line_container).setLayoutParams(layoutParams);
            }
        }

        stepperViewGroup.measure(0, 0);
        stepperViewGroupHeight = stepperViewGroup.getMeasuredHeight();

        final View orderProgressGroup = findViewById(R.id.order_progress_group);
        orderProgressGroup.measure(0, 0);
        if(orderProgressGroup.getHeight() != orderProgressGroup.getMeasuredHeight()){
            ValueAnimator heightAnimation = ValueAnimator.ofInt((int) orderProgressGroup.getHeight(), (int) orderProgressGroup.getMeasuredHeight());
            heightAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = orderProgressGroup.getLayoutParams();
                    layoutParams.height = val;
                    orderProgressGroup.setLayoutParams(layoutParams);
                }
            });
            heightAnimation.setDuration(300);
            heightAnimation.start();
        }

        progressBar.setProgress(currentStage);

        //update in database!
        colValues[5] = Integer.toString(currentStage);
        ContentValues contentValues = new ContentValues();
        for(int i = 0; i < colValues.length; i++){
            contentValues.put(colNames[i+1], colValues[i]);
        }
        getContentResolver().update(DBContentProvider.ORDER_URI, contentValues, "_id = " + orderIdTobeViewed, null);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home : finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
