package com.theone.using.activity.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.theone.using.R;
import com.theone.using.amap.AMapUtil;
import com.theone.using.amap.ToastUtil;
import com.theone.using.common.BaseActivity;
import com.theone.using.common.TitleLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements Inputtips.InputtipsListener, PoiSearch.OnPoiSearchListener {
    private TitleLayout titleLayout;
    private ListView listView;
    private List<SearchItem> listviewHistoryData = new ArrayList<SearchItem>();
    private List<SearchItem> listviewSearchData = new ArrayList<SearchItem>();
    private AutoCompleteTextView searchtx;
    private List<SearchItem> searchedListItems;
    private Intent intent;
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private List<PoiItem> poiItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        intent = getIntent();
        init();
    }

    void init() {
        titleLayout = (TitleLayout) findViewById(R.id.title);
        titleLayout.getBackView().setImageResource(R.drawable.pic_back);
        titleLayout.getTitletx().setText("选择停车地点");
        MyOnClickListener l = new MyOnClickListener();
        titleLayout.getBackView().setOnClickListener(l);

        searchtx = (AutoCompleteTextView) findViewById(R.id.tx_search_edit);

        listView = (ListView) findViewById(R.id.listview);
        initHistoryData();

        listView.setAdapter(new SearchItemAdpter(this, R.layout.search_item, listviewHistoryData));

        searchtx.addTextChangedListener(new EditChangedListener());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (searchtx.getText().toString().equals("")) {
                    SearchItem searchItem = listviewHistoryData.get(position);
                    searchtx.setText(searchItem.getSearchPlaceName());
                } else {
                    try {
                        PoiItem poiItem = poiItems.get(position);
                        String locationTitle = poiItem.getTitle();
                        String locationSnippet = poiItem.getSnippet();
                        System.out.println("locationTitle:" + locationTitle + "  " + locationSnippet);
                        Double locationLatitude = poiItem.getLatLonPoint().getLatitude();
                        Double locationLongitude = poiItem.getLatLonPoint().getLongitude();

                        Bundle bundle = new Bundle();
                        bundle.putString("locationTitle", locationTitle);
                        bundle.putString("locationSnippet", locationSnippet);
                        bundle.putDouble("locationLongitude", locationLongitude);
                        bundle.putDouble("locationLatitude", locationLatitude);
                        intent.putExtras(bundle);
                        //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                        setResult(0, intent);
                        finish();
                    } catch (Exception e) {
                        SearchItem searchItem = listviewHistoryData.get(position);
                        searchtx.setText(searchItem.getSearchAddress());
                    }
                }
            }
        });
    }

    private void initHistoryData() {
        listviewHistoryData.clear();
        SearchItem searchItem = new SearchItem(R.drawable.pic_search_history, "韶山南路铁道学院68号", "中南大学铁道学院");
        SearchItem searchItem2 = new SearchItem(R.drawable.pic_search_history, "1号线", "铁道学院(地铁站)");
        listviewHistoryData.add(searchItem);
        listviewHistoryData.add(searchItem2);
    }

    private void initSearchData() {
        listviewSearchData.clear();
        SearchItem searchItem = new SearchItem(R.drawable.pic_search_place, "家乐福", "韶山南路161号");
        SearchItem searchItem2 = new SearchItem(R.drawable.pic_search_place, "家乐福停车场", "韶山南路161号");
        listviewSearchData.add(searchItem);
        listviewSearchData.add(searchItem2);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

//    public static void actionStartForResule(Context context) {
//        Intent intent = new Intent(context, SearchActivity.class);
//        ((Activity)context).startActivityForResult();
//    }

    //输入补全重载
    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == 1000) {// 正确返回
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.route_inputs, listString);
            searchtx.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();

            if (searchtx.getText().toString().equals("")) {
                initHistoryData();
                listView.setAdapter(new SearchItemAdpter(SearchActivity.this, R.layout.search_item, listviewHistoryData));
            }

        } else {
            ToastUtil.showerror(this, rCode);
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(String keyWord) {
        //showProgressDialog();// 显示进度框
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", "长沙");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    //查询地图位置重载方法
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        //dissmissProgressDialog();// 隐藏对话框
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        searchedListItems = new ArrayList<SearchItem>();
                        for (int i = 0; i < poiItems.size(); i++) {
                            searchedListItems.add(new SearchItem(R.drawable.pic_search_place, poiItems.get(i).getSnippet(), poiItems.get(i).getTitle()));
                        }
                        listView.setAdapter(new SearchItemAdpter(SearchActivity.this, R.layout.search_item, searchedListItems));
//                        aMap.clear();// 清理之前的图标
//                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
//                        poiOverlay.removeFromMap();
//                        poiOverlay.addToMap();
//                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        ToastUtil.show(this,
                                R.string.no_result);
                    }
                }
            } else {
                ToastUtil.show(this,
                        R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this, rCode);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtil.show(this, infomation);

    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.title_iv) {
                SearchActivity.this.finish();
            }
        }
    }


    class EditChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (searchedListItems != null) {
                searchedListItems.clear();
            }
            String newText = s.toString().trim();

            //输入为空时显示历史记录
            if (newText.equals("")) {
                initHistoryData();
                listView.setAdapter(new SearchItemAdpter(SearchActivity.this, R.layout.search_item, listviewHistoryData));
            }

            if (!AMapUtil.IsEmptyOrNullString(newText)) {
                InputtipsQuery inputquery = new InputtipsQuery(newText, "长沙");
                Inputtips inputTips = new Inputtips(SearchActivity.this, inputquery);
                inputTips.setInputtipsListener(SearchActivity.this);
                inputTips.requestInputtipsAsyn();
            }

            doSearchQuery(newText);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
