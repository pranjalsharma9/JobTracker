package com.pranjals.nsit.jobtracker.ViewTables;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pranjals.nsit.jobtracker.R;

public class TableListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_list);
    }

    public void onViewTableButtonTapped(View view){
        Intent intent = new Intent(this, ViewTableActivity.class);
        switch(view.getId()){
            case R.id.view_order_table_button :
                intent.putExtra("tablesToView", "order");
                break;
            case R.id.view_customer_table_button :
                intent.putExtra("tablesToView", "customer");
                break;
            case R.id.view_employee_table_button :
                intent.putExtra("tablesToView", "employee");
                break;
            case R.id.view_stage_table_button :
                intent.putExtra("tablesToView", "stage");
                break;
            case R.id.view_order_customer_table_button :
                intent.putExtra("tablesToView", "orderCustomer");
                break;
            case R.id.view_order_employee_table_button :
                intent.putExtra("tablesToView", "orderEmployee");
                break;
            case R.id.view_order_stage_table_button :
                intent.putExtra("tablesToView", "orderStage");
                break;
            default :
                intent.putExtra("tablesToView", "order");
                break;
        }
        startActivity(intent);
    }

}

