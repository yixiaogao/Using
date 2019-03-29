package com.theone.using.activity.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.theone.using.R;
import com.theone.using.common.BaseActivity;
import com.theone.using.common.TitleLayout;

import java.text.SimpleDateFormat;

public class ShareDetailsActivity extends BaseActivity {
    private TitleLayout titleLayout;
    private Intent intent;
    private TextView shareLocationtx;
    private TextView shareTimetx;
    private Button confrimSharebtn;
    private ImageView shareImg;
    private String picPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_details);
        init();
    }

    void init() {
        titleLayout = (TitleLayout) findViewById(R.id.title);
        titleLayout.getBackView().setImageResource(R.drawable.pic_back);
        titleLayout.getTitletx().setText("分享内容");
        MyOnClickListener l = new MyOnClickListener();
        titleLayout.getBackView().setOnClickListener(l);
        shareLocationtx = (TextView) findViewById(R.id.tx_share_location);
        shareTimetx = (TextView) findViewById(R.id.tx_share_time);

        confrimSharebtn = (Button) findViewById(R.id.btn_comfirm_share);
        shareImg = (ImageView) findViewById(R.id.iv_share_img);
        shareImg.setOnClickListener(l);
        confrimSharebtn.setOnClickListener(l);
        shareTimetx.setText(getSystemTime());
        try {
            intent = getIntent();
            Bundle bundle = intent.getExtras();
            shareLocationtx.setText(bundle.getString("markertitle", "分享地址"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String getSystemTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        return date;

    }

    public static void actionStart(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ShareDetailsActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri uri = data.getData();
            //通过uri获取图片路径
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            picPath = cursor.getString(column_index);
            System.out.println(picPath);

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picPath, options);
            //  options.inSampleSize = BitmapUtil.calculateInSampleSize(options, 1280, 720);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);
            shareImg.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.title_iv) {
                ShareDetailsActivity.this.finish();
            }
            if (id == R.id.iv_share_img) {
                // 激活系统图库，选择一张图片
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
            if (id == R.id.btn_comfirm_share) {
                System.out.println("分享成功");
                ShareDetailsActivity.this.finish();
            }
        }
    }


}

