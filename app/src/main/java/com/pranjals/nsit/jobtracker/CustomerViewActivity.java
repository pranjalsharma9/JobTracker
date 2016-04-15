package com.pranjals.nsit.jobtracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Pranjal Verma on 3/22/2016.
 */
public class CustomerViewActivity extends AppCompatActivity{

    public final static String START_WITH_ID = "_idOfCustomerToView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view);
    }
}
