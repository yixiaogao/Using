package com.theone.using.activity.history;

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

public class HistoryActivity extends BaseActivity {
    private TitleLayout titleLayout;

    private ListView listView;
    private List<HistoryItem> listviewData = new ArrayList<HistoryItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        init();
    }

    void init() {
        titleLayout = (TitleLayout) findViewById(R.id.title);
        titleLayout.getBackView().setImageResource(R.drawable.pic_back);
        titleLayout.getTitletx().setText("历史记录");
        MyOnClickListener l=new MyOnClickListener();
        titleLayout.getBackView().setOnClickListener(l);

        listView= (ListView) findViewById(R.id.listview);
        initListData();
        listView.setAdapter(new HistoryItemAdpter(this,R.layout.history_item,listviewData));

    }

    private void initListData() {
        HistoryItem historyItem=new HistoryItem("上次停车","停车成功\n韶山南路68号\n消耗了1U豆");
        HistoryItem historyItem2=new HistoryItem("上次分享","铁道学院韶山南路22号");
        listviewData.add(historyItem);
        listviewData.add(historyItem2);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, HistoryActivity.class);
        context.startActivity(intent);
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id==R.id.title_iv){
                HistoryActivity.this.finish();
            }
        }
    }
}
