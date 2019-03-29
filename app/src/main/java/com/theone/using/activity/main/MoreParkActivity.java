package com.theone.using.activity.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.theone.using.R;
import com.theone.using.common.BaseActivity;
import com.theone.using.common.TitleLayout;

import java.util.ArrayList;
import java.util.List;

public class MoreParkActivity extends BaseActivity {
    private TitleLayout titleLayout;
    private ListView listView;
    private List<SearchItem> listviewData = new ArrayList<SearchItem>();
    private Button button1, button2, button3, button4;
    private SearchItemAdpter searchItemAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_park);
        init();
    }

    void init() {
        titleLayout = (TitleLayout) findViewById(R.id.title);
        titleLayout.getBackView().setImageResource(R.drawable.pic_back);
        titleLayout.getTitletx().setText("附近更多车位");
        MyOnClickListener l = new MyOnClickListener();
        titleLayout.getBackView().setOnClickListener(l);

        listView = (ListView) findViewById(R.id.list_more_park);
        searchItemAdpter = new SearchItemAdpter(this, R.layout.search_item, listviewData);
        initData1();
        listView.setAdapter(searchItemAdpter);
        button1 = (Button) findViewById(R.id.btn_type_all);
        button2 = (Button) findViewById(R.id.btn_type_frommap);
        button3 = (Button) findViewById(R.id.btn_type_share);
        button4 = (Button) findViewById(R.id.btn_type_rent);
        button1.setOnClickListener(l);
        button2.setOnClickListener(l);
        button3.setOnClickListener(l);
        button4.setOnClickListener(l);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParkDetailsActivity.actionStart(MoreParkActivity.this,new Bundle());
                MoreParkActivity.this.finish();
            }
        });

    }

    private void initData1() {
        listviewData.clear();
        String str1[] = {"韶山南路68号中南大学铁道学院内","芙蓉南路1段181通泰·梅岭苑","南路一段181号","芙蓉南路120号附近","芙蓉南路1段181通泰·梅岭苑"," 竹塘西路30号附近","铁道学院第二综合楼前坪"};
        String str2[] = {"中南大学铁道学院停车场","通泰·梅岭苑地下停车场(北入口)","通泰·梅岭苑停车场","停车场(芙蓉南路)","通泰·梅岭苑停车场","停车场(竹塘西路)","铁道学院第二综合楼前坪"};
        for (int i = 0; i < str1.length; i++) {
            listviewData.add(new SearchItem(R.drawable.pic_search_history, str1[i], str2[i]));
        }
        searchItemAdpter.notifyDataSetChanged();
    }

    private void initData2() {
        listviewData.clear();
        String str1[] = {"韶山南路68号中南大学铁道学院内","芙蓉南路1段181通泰·梅岭苑","南路一段181号","芙蓉南路120号附近","芙蓉南路1段181通泰·梅岭苑"," 竹塘西路30号附近"};
        String str2[] = {"中南大学铁道学院停车场","通泰·梅岭苑地下停车场(北入口)","通泰·梅岭苑停车场","停车场(芙蓉南路)","通泰·梅岭苑停车场","停车场(竹塘西路)"};
        for (int i = 0; i < str1.length; i++) {
            listviewData.add(new SearchItem(R.drawable.pic_search_history, str1[i], str2[i]));
        }
        searchItemAdpter.notifyDataSetChanged();
    }

    private void initData3() {
        listviewData.clear();
        String str1[] = {"铁道学院第二综合楼前坪"};
        String str2[] = {"铁道学院第二综合楼前坪"};

        for (int i = 0; i < str1.length; i++) {
            listviewData.add(new SearchItem(R.drawable.pic_search_history, str1[i], str2[i]));
        }
        searchItemAdpter.notifyDataSetChanged();
    }

    private void initData4() {
        listviewData.clear();
        searchItemAdpter.notifyDataSetChanged();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MoreParkActivity.class);
        context.startActivity(intent);
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.title_iv) {
                MoreParkActivity.this.finish();
            }
            if (id == R.id.btn_type_all) {
                initData1();
            }
            if (id == R.id.btn_type_frommap) {
                initData2();
            }
            if (id == R.id.btn_type_share) {
                initData3();
            }
            if (id == R.id.btn_type_rent) {
                initData4();
            }
        }
    }
}
