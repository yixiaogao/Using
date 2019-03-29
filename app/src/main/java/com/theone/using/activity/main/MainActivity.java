package com.theone.using.activity.main;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.theone.using.R;
import com.theone.using.activity.NextActivity;
import com.theone.using.activity.history.HistoryActivity;
import com.theone.using.activity.message.MessageActivity;
import com.theone.using.activity.payment.PaymentActivity;
import com.theone.using.activity.rent.RentActivity;
import com.theone.using.activity.settings.SettingsActivity;
import com.theone.using.activity.share.ShareActivity;
import com.theone.using.activity.usercenter.UserCenterActivity;
import com.theone.using.common.ActivityCollector;
import com.theone.using.common.BaseActivity;
import com.theone.using.common.TitleLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private ListView lv;
    //private String[] strList;
    private List<LeftListItem> leftList = new ArrayList<LeftListItem>();
    private DrawerLayout mDrawerLayout = null;
    private TitleLayout titleLayout = null;
    private FragmentManager fragmentManager = getFragmentManager();
    private LinearLayout leftRoot = null;
    private RelativeLayout usercenterLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComp();
        initData();
        initFragment();
    }

    private void initFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, new MainActivityFrag()).commit();
    }

    private void initData() {
        // TODO Auto-generated method stub
        titleLayout.getBackView().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        //strList = getResources().getStringArray(R.array.items);
        initListData();
        usercenterLayout= (RelativeLayout) findViewById(R.id.layout_user_center);
        usercenterLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UserCenterActivity.actionStart(MainActivity.this);
            }
        });
        lv.setAdapter(new ListViewAdapter(this,
                R.layout.main_side_listview_item, leftList));
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                lv.setItemChecked(position, true);
                mDrawerLayout.closeDrawer(leftRoot);
                switch (position) {
                    case 0:
                        ShareActivity.actionStart(MainActivity.this);
                        break;
                    case 1:
                        RentActivity.actionStart(MainActivity.this);
                        break;
                    case 2:
                        PaymentActivity.actionStart(MainActivity.this);
                        break;
                    case 3:
                        HistoryActivity.actionStart(MainActivity.this);
                        break;
                    case 4:
                        MessageActivity.actionStart(MainActivity.this);
                        break;
                    case 5:
                        SettingsActivity.actionStart(MainActivity.this);
                        break;
                    case 6:
                        ActivityCollector.finishAll();
                        break;
                    default:
                        NextActivity.actionStart(MainActivity.this);
                        break;
                }

            }
        });
    }

    private void initComp() {
        // TODO Auto-generated method stub
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        titleLayout = (TitleLayout) findViewById(R.id.title);
        lv = (ListView) findViewById(R.id.left_drawer);
        leftRoot = (LinearLayout) findViewById(R.id.left_root);
    }

    private void initListData() {
        String[] strList = getResources().getStringArray(R.array.listviewitemstr);
        int[] picList = {R.drawable.pic_list_share,
                R.drawable.pic_list_rent,
                R.drawable.pic_list_payment,
                R.drawable.pic_list_history,
                R.drawable.pic_list_msg,
                R.drawable.pic_list_settings,
                R.drawable.pic_list_about
        };

        for (int i = 0; i < picList.length; i++) {
            leftList.add(new LeftListItem(picList[i], strList[i]));
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }


    private long exitTime = 0;

    /**
     * 捕捉返回事件按钮
     *
     * 因为此 Activity 继承 TabActivity 用 onKeyDown 无响应，所以改用 dispatchKeyEvent
     * 一般的 Activity 用 onKeyDown 就可以了
     */

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                this.exitApp();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 退出程序
     */
    private void exitApp() {
        // 判断2次点击事件时间
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

}