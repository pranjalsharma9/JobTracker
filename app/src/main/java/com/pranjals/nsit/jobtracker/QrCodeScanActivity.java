package com.pranjals.nsit.jobtracker;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

/**
 * Created by Pranjal Verma on 3/24/2016.
 */
public class QrCodeScanActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);

        Button scan = (Button)findViewById(R.id.button_scanQr);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator intentIntegrator = new IntentIntegrator(QrCodeScanActivity.this);
                intentIntegrator.initiateScan();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(scanResult != null){
            String scanned_orderId = scanResult.getContents();
            String projection[] = {"_id","name", "doo", "doc", "cid"};
            String selection = "_id = '"+scanned_orderId+"'";
            Cursor cursor = getContentResolver().query(DBContentProvider.ORDER_URI, projection, selection, null, null);
            String orderDoo="none",orderName="none",orderDoc="none",orderCid="none";

            if( cursor != null && cursor.moveToFirst() ){
                orderName = cursor.getString(cursor.getColumnIndex("name"));
                orderDoo = cursor.getString(cursor.getColumnIndex("doo"));
                orderDoc = cursor.getString(cursor.getColumnIndex("doc"));
                orderCid = cursor.getString(cursor.getColumnIndex("cid"));
                cursor.close();
            }
            TextView tv = (TextView)findViewById(R.id.textView_scanned_orderName);
            tv.setText(orderName);
            tv = (TextView)findViewById(R.id.textView_scanned_dateOfOrder);
            tv.setText(orderDoo);
            tv = (TextView)findViewById(R.id.textView_scanned_dateOfComp);
            tv.setText(orderDoc);
            tv = (TextView)findViewById(R.id.textView_scanned_custId);
            tv.setText(orderCid);
        }
        else{

            Toast.makeText(QrCodeScanActivity.this,"Sorry! Something got Wrong",Toast.LENGTH_SHORT).show();
        }
    }
}
