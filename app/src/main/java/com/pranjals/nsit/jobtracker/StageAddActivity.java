package com.pranjals.nsit.jobtracker;

import android.content.ContentValues;
import android.content.Intent;
import android.net.LinkAddress;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

/**
 * Created by Pranjal Verma on 4/10/2016.
 */
public class StageAddActivity extends FragmentActivity {

    private View firstFragViewGroup;
    private View secondFragViewGroup;
    private int totalStages;
    private  ViewPager viewPager;
    private ViewPager.OnPageChangeListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_add);
         viewPager = (ViewPager)findViewById(R.id.viewPager_stageAdd);
        MyPageAdpter myPageAdpter = new MyPageAdpter(getSupportFragmentManager());
        viewPager.setAdapter(myPageAdpter);


         listener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if(position==0){
//                    getTotalStagesEntered();
//                }

            }

            @Override
            public void onPageSelected(int position) {

                if(position==0){
                 getTotalStagesEntered();
                 LinearLayout linearLayout = (LinearLayout)secondFragViewGroup.findViewById(R.id.stageFragSecond_container);
                    linearLayout.removeAllViews();
                }
                if(position==1) {
                    getTotalStagesEntered();
                    createDynamicViews();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        viewPager.addOnPageChangeListener(listener);





    }

    public void setFirstFragmentViewGroup(View viewGroup){
        firstFragViewGroup = viewGroup;
        //Log.e("1111111111111111","ssds");
        EditText et = (EditText)firstFragViewGroup.findViewById(R.id.stagFrag_totalStages);
        Button next = (Button)firstFragViewGroup.findViewById(R.id.stageAdd_first_next_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1,true);
            }
        });
        et.setText("0");


    }

    public void setSecodFragmentViewGroup(View viewGroup){

        secondFragViewGroup = viewGroup;
        listener.onPageSelected(0);
        //Log.e("2222222222222222222","ss");
        Button button = (Button)secondFragViewGroup.findViewById(R.id.button_addStageFinal);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String names="";
                for(int i=0;i<totalStages;i++){
                    EditText et = (EditText)secondFragViewGroup.findViewById(i);
                    names +=et.getText().toString()+";";
                }
                EditText et =(EditText)firstFragViewGroup.findViewById(R.id.stageFrag_type);
                String type = et.getText().toString();
                ContentValues values = new ContentValues();
                values.put("names", names);
                values.put("type", type);
                values.put("total", totalStages);
               Uri uri = getContentResolver().insert(DBContentProvider.STAGE_URI, values);
              //  Toast.makeText(StageAddActivity.this, "STAGE ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                String id = uri.getLastPathSegment();
              //  Log.e("lsmlmdlsmlsmlmsdlmds",id);
                intent.putExtra("stageId",id);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    public void createDynamicViews(){

        //Log.e("dsssdsd","ppppppppppppppppppppppppppppppppppppppppppp");

        LinearLayout container = (LinearLayout)secondFragViewGroup.findViewById(R.id.stageFragSecond_container);
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        for(int i=0;i<totalStages;i++){

            //Log.e("dsssdsd","ppppppppppppppppppppppppppppppppppppppppppp");
            View toAdd = inflater.inflate(R.layout.stage_add_each_stage_row,null);
            EditText et = (EditText)toAdd.findViewById(R.id.addStage_dynamic_et);
            et.setHint("Stage "+(i+1));
            et.setId(i);
            container.addView(toAdd);
        }


    }

    public void getTotalStagesEntered(){

        EditText et = (EditText)firstFragViewGroup.findViewById(R.id.stagFrag_totalStages);
         totalStages = Integer.parseInt(et.getText().toString());
   //     Log.e("hdshdjshdjshdkjshkdj",et.getText().toString());
    }

    public String getStageType(){

        EditText et = (EditText)secondFragViewGroup.findViewById(R.id.stageFrag_type);
        return et.getText().toString();

    }

    public static class MyPageAdpter extends FragmentPagerAdapter{
        private static int NUM_PAGES = 2;

        public MyPageAdpter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0 : return new StageAddFragment_First();
                case 1 : return new StageAddFragment_Second();
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
