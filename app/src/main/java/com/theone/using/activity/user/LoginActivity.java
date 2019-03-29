package com.theone.using.activity.user;

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

public class LoginActivity extends BaseActivity {
    private TitleLayout titleLayout;

    private EditText userMobiletx;
    private EditText userPsdtx;
    private TextView toVcodeLogintx;
    private Button toRegisterbtn;
    private Button loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    void init() {
        titleLayout = (TitleLayout) findViewById(R.id.title);
        titleLayout.getBackView().setImageResource(R.drawable.pic_back);
        titleLayout.getTitletx().setText("登录");
        MyOnClickListener l = new MyOnClickListener();
        // titleLayout.getBackView().setOnClickListener(l);
        titleLayout.getBackView().setVisibility(View.INVISIBLE);

        userMobiletx = (EditText) findViewById(R.id.tx_usermobile);
        userPsdtx = (EditText) findViewById(R.id.tx_userpsd);

        toVcodeLogintx = (TextView) findViewById(R.id.tx_to_vcode_login);
        toRegisterbtn = (Button) findViewById(R.id.btn_to_register);
        loginbtn = (Button) findViewById(R.id.btn_login);

        toVcodeLogintx.setOnClickListener(l);
        toRegisterbtn.setOnClickListener(l);
        loginbtn.setOnClickListener(l);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
//            if (id == R.id.title_iv) {
//               LoginActivity.this.finish();
//            }
            if (id == R.id.tx_to_vcode_login) {
                LoginActivity.this.finish();
                LoginByVcodeActivity.actionStart(LoginActivity.this);
            }
            if (id == R.id.btn_to_register) {
                LoginActivity.this.finish();
                RegisterActivity.actionStart(LoginActivity.this);
            }
            if (id == R.id.btn_login) {
                LoginActivity.this.finish();
                MainActivity.actionStart(LoginActivity.this);
            }
        }
    }


}
