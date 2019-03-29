package com.theone.using.activity.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.theone.using.R;
import com.theone.using.common.BaseActivity;
import com.theone.using.common.TitleLayout;

public class AboutActivity extends BaseActivity {
    private TitleLayout titleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init();
    }

    void init() {
        titleLayout = (TitleLayout) findViewById(R.id.title);
        titleLayout.getBackView().setImageResource(R.drawable.pic_back);
        titleLayout.getBackView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.this.finish();
            }
        });
        titleLayout.getTitletx().setText("关于");
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }
}
