package com.theone.using.activity.payment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.theone.using.R;
import com.theone.using.common.BaseActivity;

public class PaymentActivity extends BaseActivity {

    ImageView backiv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        init();
    }

    private void init() {
        backiv= (ImageView) findViewById(R.id.iv_payment_back);
        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentActivity.this.finish();
            }
        });
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PaymentActivity.class);
        context.startActivity(intent);
    }
}
