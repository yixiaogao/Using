package com.theone.using.activity.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.theone.using.R;
import com.theone.using.activity.main.MainActivity;
import com.theone.using.common.BaseActivity;
import com.theone.using.common.MyTextUtils;
import com.theone.using.common.SMSUtil;
import com.theone.using.common.TitleLayout;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static cn.smssdk.framework.utils.R.getStringRes;

public class RegisterActivity extends BaseActivity {
    private TitleLayout titleLayout;
    private EditText userMobiletx;
    private EditText userPsdtx;
    private EditText vcodetx;
    private Button getVcodebtn;
    private Button registerbtn;
    private Button toLoginbtn;
    private Context context;    //activity context
    private MyHandler myHandler;    //线程消息传递
    private ProgressDialog progressDialog;  //信息提示对话框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    void init() {
        titleLayout = (TitleLayout) findViewById(R.id.title);
        titleLayout.getBackView().setImageResource(R.drawable.pic_back);
        titleLayout.getTitletx().setText("注册");
        MyOnClickListener l = new MyOnClickListener();
        //titleLayout.getBackView().setOnClickListener(l);
        titleLayout.getBackView().setVisibility(View.INVISIBLE);

        userMobiletx = (EditText) findViewById(R.id.tx_usermobile);
        userPsdtx = (EditText) findViewById(R.id.tx_userpsd);
        vcodetx = (EditText) findViewById(R.id.tx_vcode);

        getVcodebtn = (Button) findViewById(R.id.btn_get_vcode);
        registerbtn = (Button) findViewById(R.id.btn_register);
        toLoginbtn = (Button) findViewById(R.id.btn_to_login);

        getVcodebtn.setOnClickListener(l);
        registerbtn.setOnClickListener(l);
        toLoginbtn.setOnClickListener(l);

        context = getApplication();
        initSMS();
    }

    public void initSMS() {
        myHandler = new MyHandler();
        //初始化SDK
        SMSSDK.initSDK(context, SMSUtil.APPKEY, SMSUtil.APPSECRET);
        Log.i("TAG", "initSMSSDK");
        //注册消息事件
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                myHandler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.title_iv) {
                RegisterActivity.this.finish();
            }
            if (id == R.id.btn_get_vcode) {
                String et_mobileNoStr = userMobiletx.getText().toString().trim();
                if (TextUtils.isEmpty(et_mobileNoStr)) {
                    Toast.makeText(context, "电话不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!MyTextUtils.isInteger(et_mobileNoStr)) {
                    Toast.makeText(context, "电话号码输入有误", Toast.LENGTH_LONG).show();
                    return;
                }
                Log.i("TAG", "SMSSDK.getVerificationCode");

                MyCount mc = new MyCount(30000, 1000);//计时三十秒，每秒刷新
                mc.start();
                getVcodebtn.setClickable(false);//按钮设置为不可编辑

                //开启线程阻塞ui
                progressDialog = ProgressDialog.show(RegisterActivity.this, "", "正在提交请稍后......");

                //发送获得验证码请求
                new GetVerificationCodeThread(et_mobileNoStr).start();
                // SMSSDK.getVerificationCode("86", et_mobileNoStr);
            }

            //点击注册按钮
            if (id == R.id.btn_register) {
                String vcodestr = vcodetx.getText().toString().trim();
                String userMobilestr = userMobiletx.getText().toString().trim();

                if (TextUtils.isEmpty(vcodestr)) {
                    Toast.makeText(context, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!MyTextUtils.isInteger(userMobiletx.getText().toString())
                        || !MyTextUtils.isInteger(userMobiletx.getText().toString())) {
                    Toast.makeText(context, "电话号码或输入有误", Toast.LENGTH_LONG).show();
                    return;
                }

                //开启线程阻塞ui
                progressDialog = ProgressDialog.show(RegisterActivity.this, "", "正请稍后......");
                //提交验证码
                new SubmitVerificationCodeThread(userMobilestr, vcodestr).start();
                //SMSSDK.submitVerificationCode("86", et_mobileNoStr2, confirmInputStr);

            }
            if (id == R.id.btn_to_login) {
                RegisterActivity.this.finish();
                LoginActivity.actionStart(RegisterActivity.this);
            }


        }
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("result/event：", "result= " + result + " and event= " + event);

            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    Toast.makeText(context, "验证成功", Toast.LENGTH_SHORT).show();
                    //验证成功到下一个界面
                    System.out.println("验证成功");
                    RegisterActivity.this.finish();
                    MainActivity.actionStart(RegisterActivity.this);

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    Toast.makeText(context, "验证码正在发送", Toast.LENGTH_SHORT).show();
                }

            } else if (result == SMSSDK.RESULT_ERROR) {
                Toast.makeText(context, "电话号码或验证码发生错误", Toast.LENGTH_SHORT).show();
            } else {
                ((Throwable) data).printStackTrace();
                int resId = getStringRes(context, "smssdk_network_error");
                Toast.makeText(context, "发生错误，请检查网络", Toast.LENGTH_SHORT).show();
                if (resId > 0) {
                    Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    class MyCount extends android.os.CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long second = millisUntilFinished / 1000;
            getVcodebtn.setText(second + "秒");
            Log.i("PDA", millisUntilFinished / 1000 + "");

        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            getVcodebtn.setClickable(true);
            getVcodebtn.setText("重新发送");
        }

    }

    //提交验证码线程
    class SubmitVerificationCodeThread extends Thread {

        public String phoneNum;
        public String confirmNum;

        SubmitVerificationCodeThread() {
        }


        SubmitVerificationCodeThread(String phoneNum, String confirmNum) {
            this.confirmNum = confirmNum;
            this.phoneNum = phoneNum;
        }

        public void run() {
            Log.i("提交信息：", phoneNum + "+" + confirmNum);
            SMSSDK.submitVerificationCode("86", phoneNum, confirmNum);
        }
    }


    //发送验证码线程
    class GetVerificationCodeThread extends Thread {

        public String et_mobileNoStr;

        GetVerificationCodeThread() {
        }


        GetVerificationCodeThread(String et_mobileNoStr) {
            this.et_mobileNoStr = et_mobileNoStr;
        }

        public void run() {
            SMSSDK.getVerificationCode("86", et_mobileNoStr);
        }
    }


}
