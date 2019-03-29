package com.theone.using.activity.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.theone.using.R;
import com.theone.using.common.BaseActivity;
import com.theone.using.common.TitleLayout;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends BaseActivity {
    TitleLayout titleLayout;
    private ListView listView;
    private List<MessageItem> listviewData = new ArrayList<MessageItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        init();
    }

    void init() {
        titleLayout = (TitleLayout) findViewById(R.id.title);
        titleLayout.getBackView().setImageResource(R.drawable.pic_back);
        titleLayout.getTitletx().setText("我的消息");
        MyOnClickListener l = new MyOnClickListener();
        titleLayout.getBackView().setOnClickListener(l);

        listView = (ListView) findViewById(R.id.listview);
        initListData();
        listView.setAdapter(new MessageItemAdpter(this, R.layout.message_item, listviewData));

    }

    private void initListData() {
        MessageItem messageItem = new MessageItem("您获得了一条消息。", "您好\n我是优停助手小优，在接下来的日子里我将帮助你使用优停\n很高兴认识你，希望我们相处愉快");
        listviewData.add(messageItem);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MessageActivity.class);
        context.startActivity(intent);
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.title_iv) {
                MessageActivity.this.finish();
            }
        }
    }

}
