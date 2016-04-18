package com.pranjals.nsit.jobtracker.BuildDB;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.pranjals.nsit.jobtracker.BuildDB.BuildDBFragment;
import com.pranjals.nsit.jobtracker.BuildDB.BuildDBFragmentCustomer;
import com.pranjals.nsit.jobtracker.BuildDB.BuildDBFragmentEmployee;
import com.pranjals.nsit.jobtracker.BuildDB.BuildDBFragmentFinish;
import com.pranjals.nsit.jobtracker.BuildDB.BuildDBFragmentOrder;
import com.pranjals.nsit.jobtracker.CustomFields;
import com.pranjals.nsit.jobtracker.DBHelper;
import com.pranjals.nsit.jobtracker.MainActivity;
import com.pranjals.nsit.jobtracker.R;

public class BuildDBActivity extends FragmentActivity {

    private static final int NUM_PAGES = 5;

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private PopupWindow currentPopupWindow;
    private CustomFields customFields = new CustomFields();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_db);
        //setting the view pager
        viewPager = (ViewPager) findViewById(R.id.builddb_pager);
        pagerAdapter = new BuildDBPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (currentPopupWindow != null) currentPopupWindow.dismiss();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //opens a popup window with the images of datatype options as ImageButtons
    public void onAddFieldButtonTapped(View view){
        LayoutInflater layoutInflater = getLayoutInflater();
        final View popupView = layoutInflater.inflate(R.layout.popupwindow_add_field, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        currentPopupWindow = popupWindow;
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        float offsetPosition = 48f*(getResources().getDisplayMetrics().density);
        popupWindow.showAsDropDown(view, (int) (-offsetPosition), (int)(-offsetPosition/2));
    }

    //when a button from the above popup window is tapped this function creates a new input field in
    //the current fragment of the viewPager
    public void onNewFieldDatatypeSelected(View view){
        //to get the datatype tapped
        String datatype;

        //getting the index of the current fragment of the viewPager
        int currentFragment = viewPager.getCurrentItem();

        //to get the fragment's viewGroup from the switch case below
        ViewGroup currentFragmentViewGroup;

        //inflating the view to be inserted in the viewGroup above
        LayoutInflater layoutInflater = (LayoutInflater) getLayoutInflater();
        View customFieldBox = layoutInflater.inflate(R.layout.custom_field_box, null);

        //getting the view's subview that will contain the datatype image... set in the second
        //switch below
        ImageView customFieldBoxDatatypeImage = (ImageView) customFieldBox.findViewById(R.id.custom_field_box_datatype_image);
        int customFieldBoxDatabaseImageResource;
        switch (currentFragment){
            case 1: currentFragmentViewGroup = (ViewGroup) findViewById(R.id.builddb_order_view_group);
                break;
            case 2: currentFragmentViewGroup = (ViewGroup) findViewById(R.id.builddb_customer_view_group);
                break;
            case 3: currentFragmentViewGroup = (ViewGroup) findViewById(R.id.builddb_employee_view_group);
                break;
            default: currentFragmentViewGroup = (ViewGroup) findViewById(R.id.builddb_order_view_group);
                break;
        }

        switch (view.getId()){
            case R.id.datatype_choice_text : datatype = "TEXT";
                customFieldBoxDatabaseImageResource = R.drawable.ic_datatype_text_48dp;
                break;
            case R.id.datatype_choice_numeric : datatype = "NUMERIC";
                customFieldBoxDatabaseImageResource = R.drawable.ic_datatype_numeric_48dp;
                break;
            case R.id.datatype_choice_date : datatype = "DATE";
                customFieldBoxDatabaseImageResource = R.drawable.ic_datatype_date_48dp;
                break;
            default: datatype = "TEXT";
                customFieldBoxDatabaseImageResource = R.drawable.ic_datatype_text_48dp;
                break;
        }
        //setting the datatype image in the view's ImageView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            customFieldBoxDatatypeImage.setImageDrawable(getResources().getDrawable(customFieldBoxDatabaseImageResource, getApplicationContext().getTheme()));
        } else {
            customFieldBoxDatatypeImage.setImageDrawable(getResources().getDrawable(customFieldBoxDatabaseImageResource));
        }
        customFieldBoxDatatypeImage.setTag(datatype);

        //adding the new view created to the current fragment's viewGroup
        currentFragmentViewGroup.addView(customFieldBox, currentFragmentViewGroup.getChildCount() - 1);

        //dismiss the popupWindow
        currentPopupWindow.dismiss();
    }

    //Saving the data entered in the customFields object on destruction of fragments or when the
    //finish button is pressed
    public void saveCustomFields(int callingFragment){
        ViewGroup currentFragmentViewGroup;
        View childField;
        CustomFields.FieldElement fieldElement;
        switch (callingFragment){
            case 1: currentFragmentViewGroup = (ViewGroup) findViewById(R.id.builddb_order_view_group);
                break;
            case 2: currentFragmentViewGroup = (ViewGroup) findViewById(R.id.builddb_customer_view_group);
                break;
            case 3: currentFragmentViewGroup = (ViewGroup) findViewById(R.id.builddb_employee_view_group);
                break;
            default: currentFragmentViewGroup = (ViewGroup) findViewById(R.id.builddb_order_view_group);
                break;
        }
        //if this function is called outside of the onSaveInstanceState() of the fragment
        //we check whether the onSaveInstanceState() has already been called
        if(currentFragmentViewGroup != null) {
            customFields.refreshList(callingFragment);
            for (int i = 2; i < currentFragmentViewGroup.getChildCount() - 1; i++) {
                childField = currentFragmentViewGroup.getChildAt(i);
                fieldElement = new CustomFields.FieldElement(((ViewGroup) childField).getChildAt(0).getTag().toString(),
                        ((EditText) ((ViewGroup) childField).getChildAt(1)).getText().toString());
                customFields.addNewFieldElement(fieldElement, callingFragment);
            }
        }
    }

    //on recreation of fragments, restoring the data that was entered and saved in the customFields object
    public ViewGroup loadCustomFields(int callingFragment, ViewGroup fragmentRootViewGroup){
        ViewGroup currentFragmentViewGroup = (ViewGroup)fragmentRootViewGroup.getChildAt(0);
        for(int i = 0; i < customFields.getListLength(callingFragment); i++){
            CustomFields.FieldElement fieldElement = customFields.returnFieldElement(i, callingFragment);
            LayoutInflater layoutInflater = (LayoutInflater) getLayoutInflater();
            View customFieldBox = layoutInflater.inflate(R.layout.custom_field_box, null);
            ImageView customFieldBoxDatatypeImage = (ImageView) customFieldBox.findViewById(R.id.custom_field_box_datatype_image);
            int customFieldBoxDatabaseImageResource;
            switch (fieldElement.datatype){
                case "TEXT" : customFieldBoxDatabaseImageResource = R.drawable.ic_datatype_text_48dp;
                    break;
                case "NUMERIC" : customFieldBoxDatabaseImageResource = R.drawable.ic_datatype_numeric_48dp;
                    break;
                case "DATE" : customFieldBoxDatabaseImageResource = R.drawable.ic_datatype_date_48dp;
                    break;
                default: customFieldBoxDatabaseImageResource = R.drawable.ic_text_format_black_48dp;
                    break;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                customFieldBoxDatatypeImage.setImageDrawable(getResources().getDrawable(customFieldBoxDatabaseImageResource, getApplicationContext().getTheme()));
            } else {
                customFieldBoxDatatypeImage.setImageDrawable(getResources().getDrawable(customFieldBoxDatabaseImageResource));
            }
            customFieldBoxDatatypeImage.setTag(fieldElement.datatype);

            EditText editText = (EditText) ((ViewGroup) customFieldBox).getChildAt(1);
            editText.setText(fieldElement.fieldName);

            //adding the new view created to the current fragment's viewGroup
            currentFragmentViewGroup.addView(customFieldBox, i + 2);
        }
        return fragmentRootViewGroup;
    }

    //Overriding so that the user is not able to exit the viewPager
    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            //super.onBackPressed();
        }
        else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    private class BuildDBPagerAdapter extends FragmentStatePagerAdapter {
        public BuildDBPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch(position){
                case 0: fragment = new BuildDBFragment();
                    break;
                case 1: fragment = new BuildDBFragmentOrder();
                    break;
                case 2: fragment = new BuildDBFragmentCustomer();
                    break;
                case 3: fragment = new BuildDBFragmentEmployee();
                    break;
                case 4: fragment = new BuildDBFragmentFinish();
                    break;
                default: fragment = null;
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    //from the last fragment
    public void onFinishButtonTapped(View view) {
        saveCustomFields(1);
        saveCustomFields(2);
        saveCustomFields(3);
        String createOrderTableString, createCustomerTableString, createEmployeeTableString;
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < customFields.getListLength(1); i++){
            stringBuilder.append(", " + customFields.returnFieldElement(i,1).fieldName.replace(" ", "_") + " " + customFields.returnFieldElement(i,1).datatype);
        }
        createOrderTableString = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        for(int i = 0; i < customFields.getListLength(2); i++){
            stringBuilder.append(", " + customFields.returnFieldElement(i,2).fieldName.replace(" ", "_") + " " + customFields.returnFieldElement(i,2).datatype);
        }
        createCustomerTableString =  stringBuilder.toString();
        stringBuilder = new StringBuilder();
        for(int i = 0; i < customFields.getListLength(3); i++){
            stringBuilder.append(", " + customFields.returnFieldElement(i,3).fieldName.replace(" ", "_") + " " + customFields.returnFieldElement(i,3).datatype);
        }
        createEmployeeTableString = stringBuilder.toString();
        DBHelper.getInstance(this).createTables(createOrderTableString, createCustomerTableString, createEmployeeTableString);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstTime", false);
        editor.commit();
        finish();
    }

    //when the cross button next to an added custom field is tapped
    public void onRemoveCustomFieldButtonTapped(View view){
        View customFieldBoxView = (View) view.getParent();
        ViewGroup customFieldBoxListView = (ViewGroup) customFieldBoxView.getParent();
        customFieldBoxListView.removeView(customFieldBoxView);
    }

}
