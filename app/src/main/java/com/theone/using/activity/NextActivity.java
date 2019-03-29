package com.theone.using.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.theone.using.R;
import com.theone.using.common.BaseActivity;

public class NextActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, NextActivity.class);
        context.startActivity(intent);
    }
}
