package com.theone.using.activity.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.theone.using.R;
import com.theone.using.common.BaseActivity;
import com.theone.using.common.TitleLayout;

public class SettingsActivity extends BaseActivity {
    TitleLayout titleLayout;
    RelativeLayout aboutbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }
    void init(){
        titleLayout= (TitleLayout) findViewById(R.id.title);
        titleLayout.getBackView().setImageResource(R.drawable.pic_back);
        titleLayout.getTitletx().setText("设置");
        aboutbtn= (RelativeLayout) findViewById(R.id.btn_about);

        MyOnClickListener l=new MyOnClickListener();
        aboutbtn.setOnClickListener(l);
        titleLayout.getBackView().setOnClickListener(l);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    private class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            int id=v.getId();
            if(id==R.id.btn_about){
                AboutActivity.actionStart(SettingsActivity.this);
            }
            if (id==R.id.title_iv){
                SettingsActivity.this.finish();
            }
        }
    }
}
