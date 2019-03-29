package com.theone.using.activity.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.theone.using.R;
import com.theone.using.common.BaseActivity;
import com.theone.using.common.TitleLayout;

public class MsgDetailsActivity extends BaseActivity {
    private TitleLayout titleLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_details);
        init();
    }

    void init() {
        titleLayout = (TitleLayout) findViewById(R.id.title);
        titleLayout.getBackView().setImageResource(R.drawable.pic_back);
        titleLayout.getTitletx().setText("消息内容");
        MyOnClickListener l=new MyOnClickListener();
        titleLayout.getBackView().setOnClickListener(l);

    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MsgDetailsActivity.class);
        context.startActivity(intent);
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id==R.id.title_iv){
                MsgDetailsActivity.this.finish();
            }
        }
    }
}
