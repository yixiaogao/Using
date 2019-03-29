package com.theone.using.activity.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.theone.using.R;
import com.theone.using.activity.main.MainActivity;
import com.theone.using.common.BaseActivity;
import com.theone.using.common.TitleLayout;

public class LoginByVcodeActivity extends BaseActivity {
    private TitleLayout titleLayout;
    private EditText userMobiletx;
    private EditText vcodetx;
    private Button getVcodebtn;
    private TextView toPasswordLogintx;
    private Button toRegisterbtn;
    private Button loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_vcode);
        init();
    }

    void init() {
        titleLayout = (TitleLayout) findViewById(R.id.title);
        titleLayout.getBackView().setImageResource(R.drawable.pic_back);
        titleLayout.getTitletx().setText("验证码登录");
        MyOnClickListener l = new MyOnClickListener();
        //titleLayout.getBackView().setOnClickListener(l);
        titleLayout.getBackView().setVisibility(View.INVISIBLE);

        userMobiletx = (EditText) findViewById(R.id.tx_usermobile);
        vcodetx = (EditText) findViewById(R.id.tx_vcode);

        getVcodebtn = (Button) findViewById(R.id.btn_get_vcode);
        toPasswordLogintx = (TextView) findViewById(R.id.tx_to_password_login);
        toRegisterbtn = (Button) findViewById(R.id.btn_to_register);
        loginbtn = (Button) findViewById(R.id.btn_login);

        toPasswordLogintx.setOnClickListener(l);
        toRegisterbtn.setOnClickListener(l);
        loginbtn.setOnClickListener(l);
        getVcodebtn.setOnClickListener(l);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LoginByVcodeActivity.class);
        context.startActivity(intent);
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
//            if (id == R.id.title_iv) {
//                LoginByVcodeActivity.this.finish();
//            }
            if (id == R.id.btn_get_vcode) {
                System.out.println("获得验证码");
            }
            if (id == R.id.tx_to_password_login) {
                LoginByVcodeActivity.this.finish();
                LoginActivity.actionStart(LoginByVcodeActivity.this);
            }
            if (id == R.id.btn_to_register) {
                LoginByVcodeActivity.this.finish();
                RegisterActivity.actionStart(LoginByVcodeActivity.this);
            }
            if (id == R.id.btn_login) {
                LoginByVcodeActivity.this.finish();
                MainActivity.actionStart(LoginByVcodeActivity.this);
            }
        }
    }
}