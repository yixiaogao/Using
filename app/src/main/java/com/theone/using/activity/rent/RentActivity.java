package com.theone.using.activity.rent;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.theone.using.R;
import com.theone.using.common.BaseActivity;
import com.theone.using.common.TitleLayout;

public class RentActivity extends BaseActivity {
    private TitleLayout titleLayout;
    Button chooseLocationbtn,datebeginbtn,dateendbtn,timebeginbtn,timeendbtn;
    ImageView rentpicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);
        init();
    }

    void init() {
        titleLayout = (TitleLayout) findViewById(R.id.title);
        titleLayout.getBackView().setImageResource(R.drawable.pic_back);
        titleLayout.getTitletx().setText("车位出租");
        MyOnClickListener l=new MyOnClickListener();
        titleLayout.getBackView().setOnClickListener(l);
        chooseLocationbtn= (Button) findViewById(R.id.btn_choose_location);
        datebeginbtn= (Button) findViewById(R.id.btn_date_begin);
        dateendbtn= (Button) findViewById(R.id.btn_date_end);
        timebeginbtn= (Button) findViewById(R.id.btn_time_begin);
        timeendbtn= (Button) findViewById(R.id.btn_time_end);
        chooseLocationbtn.setOnClickListener(l);
        datebeginbtn.setOnClickListener(l);
        dateendbtn.setOnClickListener(l);
        timebeginbtn.setOnClickListener(l);
        timeendbtn.setOnClickListener(l);
        rentpicture= (ImageView) findViewById(R.id.iv_rent_picture);
        rentpicture.setOnClickListener(l);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, RentActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) { //resultCode为回传的标记
            case 1:
                Bundle b = null; //data为B中回传的Intent
                try {
                    b = data.getExtras();
                    String str=b.getString("markertitle");
                    TextView textView= (TextView) findViewById(R.id.tx_rent_location);
                    textView.setText(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 0:
                System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
                if (data != null) {
                    Uri uri = data.getData();
                    //通过uri获取图片路径
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String picPath = cursor.getString(column_index);
                    System.out.println(picPath);

                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(picPath, options);
                    //  options.inSampleSize = BitmapUtil.calculateInSampleSize(options, 1280, 720);
                    options.inJustDecodeBounds = false;
                    Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);
                    rentpicture.setImageBitmap(bitmap);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id==R.id.title_iv){
                RentActivity.this.finish();
            }
            if (id==R.id.iv_rent_picture){
                // 激活系统图库，选择一张图片
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                RentActivity.this.startActivityForResult(intent, 0);
            }
            if (id==R.id.btn_choose_location){
                Intent intent=new Intent(RentActivity.this,ChooseLocationActivity.class);
                RentActivity.this.startActivityForResult(intent,1);
            }
            if (id==R.id.btn_date_begin){
                new DatePickerDialog(RentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String datebeginstr=year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日";
                        datebeginbtn.setText(datebeginstr);
                    }
                },2016,7,29).show();
            }
            if (id==R.id.btn_date_end){
                new DatePickerDialog(RentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dateendstr=year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日";
                        dateendbtn.setText(dateendstr);
                    }
                },2016,7,29).show();
            }
            if (id==R.id.btn_time_begin){
                new TimePickerDialog(RentActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                       String  timebeginstr=hourOfDay+"时"+minute+"分";
                        timebeginbtn.setText(timebeginstr);
                    }
                },7,0,true).show();
            }
            if (id==R.id.btn_time_end){
                new TimePickerDialog(RentActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String  timeendstr=hourOfDay+"时"+minute+"分";
                        timeendbtn.setText(timeendstr);
                    }
                },18,0,true).show();
            }

        }
    }
}
