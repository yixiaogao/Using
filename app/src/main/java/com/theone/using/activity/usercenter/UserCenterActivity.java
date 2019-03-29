package com.theone.using.activity.usercenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.theone.using.R;
import com.theone.using.activity.user.LoginActivity;
import com.theone.using.common.ActivityCollector;
import com.theone.using.common.BaseActivity;
import com.theone.using.common.TitleLayout;

public class UserCenterActivity extends BaseActivity {
    private TitleLayout titleLayout;
    Button exitUserbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        init();
    }

    void init() {
        titleLayout = (TitleLayout) findViewById(R.id.title);
        titleLayout.getBackView().setImageResource(R.drawable.pic_back);
        titleLayout.getTitletx().setText("用户中心");
        MyOnClickListener l=new MyOnClickListener();
        titleLayout.getBackView().setOnClickListener(l);
        exitUserbtn= (Button) findViewById(R.id.btn_user_exit);
        exitUserbtn.setOnClickListener(l);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, UserCenterActivity.class);
        context.startActivity(intent);
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id==R.id.title_iv){
                UserCenterActivity.this.finish();
            }
            if (id==R.id.btn_user_exit){
                ActivityCollector.finishAll();
                LoginActivity.actionStart(UserCenterActivity.this);

            }
        }
    }
}
