package com.theone.using.activity.main;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.theone.using.R;
import com.theone.using.activity.history.HistoryItem;
import com.theone.using.activity.history.HistoryItemAdpter;
import com.theone.using.common.BaseActivity;
import com.theone.using.common.TitleLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ParkDetailsActivity extends BaseActivity {
    private TitleLayout titleLayout;
    private Intent intent;
    //
//    private TextView shareLocationtx;
//    private TextView shareTimetx;
    private EditText commenttx;
    CommentListViewAdapter commentListViewAdapter;
    private ListView commentListView;
    private List<CommentItem> listviewData = new ArrayList<CommentItem>();
    private Button sendCommentbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_details);
        init();
    }

    void init() {
        titleLayout = (TitleLayout) findViewById(R.id.title);
        titleLayout.getBackView().setImageResource(R.drawable.pic_back);
        titleLayout.getTitletx().setText("分享详情");
        MyOnClickListener l = new MyOnClickListener();
        titleLayout.getBackView().setOnClickListener(l);
//        shareLocationtx = (TextView) findViewById(R.id.tx_share_location);
//        shareTimetx = (TextView) findViewById(R.id.tx_share_time);

        commenttx = (EditText) findViewById(R.id.tx_comment_edit);

        commentListView = (ListView) findViewById(R.id.lv_user_mark);
        initListData();
        commentListViewAdapter = new CommentListViewAdapter(this, R.layout.comment_content_item, listviewData);
        commentListView.setAdapter(commentListViewAdapter);
        setListViewHeightBasedOnChildren(commentListView);

        sendCommentbtn = (Button) findViewById(R.id.btn_send_comment);
        sendCommentbtn.setOnClickListener(l);

        try {
            intent = getIntent();
            Bundle bundle = intent.getExtras();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListData() {
        CommentItem commentItem = new CommentItem("18973993737用户", "早上车比较多，中午有很多车位");
        CommentItem commentItem2 = new CommentItem("13574853020用户", "13574853020用户使用了该条信息，有效");
        listviewData.add(commentItem);
        listviewData.add(commentItem2);
    }


    public static void actionStart(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ParkDetailsActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.title_iv) {
                ParkDetailsActivity.this.finish();
            }

            if (id == R.id.btn_send_comment) {
                listviewData.add(new CommentItem("13574853020用户", commenttx.getText().toString()));
                commentListViewAdapter.notifyDataSetChanged();
                commenttx.setText("");
                setListViewHeightBasedOnChildren(commentListView);
            }
        }
    }

    /**
     * 动态设置ListView的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}

