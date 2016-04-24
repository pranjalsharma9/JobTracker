package com.pranjals.nsit.jobtracker;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.ViewGroup.LayoutParams;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.pranjals.nsit.jobtracker.contentprovider.DBContentProvider;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Pranjal Verma on 4/17/2016.
 */
public class CustomerEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private long customerId;
    private ArrayList<String> extraCols;
    private ArrayList<String> extraColDataTypes;
    private EditText editTextSelectedForDateInput;
    private int REQUEST_CAMERA=13,REQUEST_GALLERY=14;
    private ImageView imageToEdit;
    private Bitmap thumbnail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        imageToEdit = (ImageView)findViewById(R.id.image_customerEdit);
        FloatingActionButton takeImage = (FloatingActionButton)findViewById(R.id.fab_takeCustomerPic);

        extraCols = DBHelper.getInstance(CustomerEditActivity.this).getExtraCols(1);
        extraColDataTypes = DBHelper.getInstance(CustomerEditActivity.this).getExtraOrderColDataTypes(1);

        LinearLayout container = (LinearLayout) findViewById(R.id.container_customerEdit);
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        customerId = getIntent().getLongExtra("customerId",0);
        String[] value = new String[DBHelper.DEF_CUSTOMER_COLS + extraCols.size()];

        if(customerId!=0){
            Cursor c = getContentResolver().query(DBContentProvider.CUSTOMER_URI,null,"_id = "+customerId,null,null);
            if(c!=null && c.moveToFirst()){
                for(int i=2;i<=value.length;i++) { // 0 is _id and 1 is blob image
                    value[i - 2] = c.getString(i);
                    //Log.v("values", c.getString(i));
                }
            }
            byte[] bb = c.getBlob(c.getColumnIndex("image"));
            imageToEdit.setImageBitmap(BitmapFactory.decodeByteArray(bb, 0, bb.length));
            c.close();
        }


        EditText et = (EditText)findViewById(R.id.et_customerEdit_name);
        et.setText(value[0]);
        et = (EditText)findViewById(R.id.et_customerEdit_mobile);
        et.setText(value[1]);
        et = (EditText)findViewById(R.id.et_customerEdit_email);
        et.setText(value[2]);
        et = (EditText)findViewById(R.id.et_customerEdit_address);
        et.setText(value[3]);


        for (int i = 0; i < extraCols.size(); i++) {
            switch(extraColDataTypes.get(i)){
                case "TEXT" :
                    et = (EditText) inflater.inflate(R.layout.order_add_dynamic_row, null);
                    et.setInputType(InputType.TYPE_CLASS_TEXT);
                    et.setId(i);
                    et.setText(value[i + DBHelper.DEF_CUSTOMER_COLS - 1]);
                    break;
                case "NUME" :
                    et = (EditText) inflater.inflate(R.layout.order_add_dynamic_row, null);
                    et.setInputType(InputType.TYPE_CLASS_NUMBER);
                    et.setId(i);
                    et.setText(value[i + DBHelper.DEF_CUSTOMER_COLS - 1]);
                    break;
                case "DATE" :
                    et = (EditText) inflater.inflate(R.layout.order_add_date_dynamic_row, null);
                    et.setId(i);
                    et.setText(value[i + DBHelper.DEF_CUSTOMER_COLS - 1]);
                    break;
                default :
                    et = (EditText) inflater.inflate(R.layout.order_add_dynamic_row, null);
                    et.setInputType(InputType.TYPE_CLASS_TEXT);
                    et.setId(i);
                    et.setText(value[i + DBHelper.DEF_CUSTOMER_COLS - 1]);
                    break;
            }
            container.addView(et);
        }




        takeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("dssds","pppppppppppppppppppppppppppp");
                //open pop up window
                LayoutInflater layoutInflater =
                        (LayoutInflater)getBaseContext()
                                .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup_camera_action, null);

                final PopupWindow popupWindow = new PopupWindow(
                        popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,true);

                Button camera = (Button)popupView.findViewById(R.id.bt_popup_takePhoto);
                Button gallery = (Button)popupView.findViewById(R.id.bt_popup_chooseGallery);

                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra("crop", "true");
                        cameraIntent.putExtra("aspectX", 4);
                        cameraIntent.putExtra("aspectY", 3);
                        cameraIntent.putExtra("scale", true);
                        cameraIntent.putExtra("outputX", 200);
                        cameraIntent.putExtra("outputY", 200);
                        startActivityForResult(cameraIntent,REQUEST_CAMERA);

                    }
                });


                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        popupWindow.dismiss();
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_PICK);
                        intent.putExtra("crop", "true");
                        intent.putExtra("aspectX", 4);
                        intent.putExtra("aspectY", 3);
                        intent.putExtra("scale", true);
                        intent.putExtra("outputX", 200);
                        intent.putExtra("outputY", 200);
                        intent.putExtra("return-data", true);
                        startActivityForResult(Intent.createChooser(intent,"Complete action using"),REQUEST_GALLERY);

                    }
                });

                float offsetPosition = 100f*(getResources().getDisplayMetrics().density);
                float offsetPositiony = 150f*(getResources().getDisplayMetrics().density);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupWindow.showAsDropDown(imageToEdit, (int) (offsetPosition), (int) (-offsetPositiony));

                //to dim the background as  popup window is opened

                View container = (View) popupWindow.getContentView().getParent();
                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
                p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                p.dimAmount = 0.5f;
                wm.updateViewLayout(container, p);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_done,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.done_edit_customer:
                String name = ((EditText)findViewById(R.id.et_customerEdit_name)).getText().toString();
                String mobile = ((EditText)findViewById(R.id.et_customerEdit_mobile)).getText().toString();
                String address = ((EditText)findViewById(R.id.et_customerEdit_address)).getText().toString();
                String email = ((EditText)findViewById(R.id.et_customerEdit_email)).getText().toString();


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thumbnail = ((BitmapDrawable)imageToEdit.getDrawable()).getBitmap();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitMapData = stream.toByteArray();


                ContentValues cvalues = new ContentValues();
                cvalues.put("name",name);
                cvalues.put("mobile", mobile);
                cvalues.put("email", email);
                cvalues.put("address", address);
                cvalues.put("image", bitMapData);


                for(int i=0;i<extraCols.size();i++){
                    String toAdd = ((EditText)findViewById(i)).getText().toString();
                    cvalues.put(extraCols.get(i) + extraColDataTypes.get(i),toAdd);
                }

                getContentResolver().update(DBContentProvider.CUSTOMER_URI, cvalues, "_id = " + customerId, null);
                //Log.e("dssdsdssdds", customerId + "");

                setResult(RESULT_OK);
                finish();
                break;


        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK&&data!=null&&data.getExtras()!=null){

            if(requestCode==REQUEST_CAMERA){

                thumbnail = (data.getExtras()).getParcelable("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                byte[] bitMapData = bytes.toByteArray();
                imageToEdit.setImageBitmap(BitmapFactory.decodeByteArray(bitMapData, 0, bitMapData.length));



            }
            else if(requestCode==REQUEST_GALLERY){

//                Uri selectedImageUri = data.getData();
//                String[] projection = { MediaStore.MediaColumns.DATA };
//                Cursor cursor =getContentResolver().query(selectedImageUri, projection, null, null, null);
//                cursor.moveToFirst();
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//                String selectedImagePath = cursor.getString(column_index);
//                cursor.close();
//                Bitmap bm = (BitmapFactory.decodeFile(selectedImagePath));
                Bitmap bm = (data.getExtras()).getParcelable("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG,100, bytes);
                byte[] bitMapData = bytes.toByteArray();
                imageToEdit.setImageBitmap(BitmapFactory.decodeByteArray(bitMapData, 0, bitMapData.length));


            }

        }
        else
            Toast.makeText(CustomerEditActivity.this,"Sorry! Something went wrong.Try using another application",Toast.LENGTH_SHORT).show();
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        String dayString = Integer.toString(day);
        if(day < 10){dayString = "0" + dayString;}
        String monthString = Integer.toString(month + 1);
        if((month + 1) < 10){monthString = "0" + monthString;}
        String date = dayString + "-" + monthString + "-" + Integer.toString(year);
        editTextSelectedForDateInput.setText(date);
    }

    public void onAddDateClicked (View view){
        editTextSelectedForDateInput = ((EditText) view);
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(), "date picker");
    }
}

