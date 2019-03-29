package com.theone.using.activity.main;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.theone.using.R;
import com.theone.using.amap.AMapUtil;
import com.theone.using.amap.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivityFrag extends Fragment implements LocationSource,
        AMapLocationListener, AMap.OnMapClickListener {

    private static final String TAG = "MainActivityFrag";
    private RelativeLayout searchLayout;
    private RelativeLayout moreParkLayout;
    private  RelativeLayout seeDetailsLayout;
    private TextView mPoiName;
    private TextView mPoiAddress;
    private View view;
    private Button getToRoutebtn;

    private MapView mapView;
    private AMap aMap;

    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private Marker parkPointMarker;

    //逆地理编码
    private GeocodeSearch geocoderSearch;

    //搜索回传
    private TextView locationtx;
    private boolean isFromSearch = false;
    String locationTitle = "";
    String locationSnippet = "";

    //显示周围信息
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private Marker detailMarker;//当前点击显示细节的点
    private Marker mlastMarker;//上一个被点击到的点
    private PoiSearch poiSearch;
    private MyPoiOverlay poiOverlay;// poi图层
    private List<PoiItem> poiItems;// poi数据
    // private PoiItem mPoi;
    private LatLonPoint routeBegin, routeEnd;
    private int[] markers = {R.drawable.poi_marker_1,
            R.drawable.poi_marker_2,
            R.drawable.poi_marker_3,
            R.drawable.poi_marker_4,
            R.drawable.poi_marker_5,
            R.drawable.poi_marker_6,
            R.drawable.poi_marker_7,
            R.drawable.poi_marker_8,
            R.drawable.poi_marker_9,
            R.drawable.poi_marker_10
    };
    MarkerOptions markerOptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(TAG + "------onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_frag, null);
        System.out.println(TAG + "------onCreateView");
        this.view = view;
        mapView = (MapView) (view.findViewById(R.id.map));
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        initMap();
        iniView();
        return view;
    }

    private void iniView() {
        searchLayout = (RelativeLayout) view.findViewById(R.id.main_search_layout);
        moreParkLayout = (RelativeLayout) view.findViewById(R.id.layout_more_park);
        MyOnClickListener l = new MyOnClickListener();
        searchLayout.setOnClickListener(l);
        moreParkLayout.setOnClickListener(l);
        mPoiName = (TextView) view.findViewById(R.id.main_location_title);
        mPoiAddress = (TextView) view.findViewById(R.id.main_location_snippet);
        locationtx = (TextView) view.findViewById(R.id.tx_location);
        getToRoutebtn = (Button) view.findViewById(R.id.btn_go);
        getToRoutebtn.setOnClickListener(l);
        seeDetailsLayout= (RelativeLayout) view.findViewById(R.id.layout_see_details);
        seeDetailsLayout.setOnClickListener(l);
    }

    /**
     * 初始化AMap对象
     */
    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.pic_my_location_point2));// 设置小蓝点的图标
        // myLocationStyle.anchor(0.5f,0.5f);
        //myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        // myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细

        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        aMap.setOnMarkerClickListener(new MyOnMarkerClickListener());
        //设置移动监听
        aMap.setOnCameraChangeListener(new MyOnCameraChangeListener());
        //地图点击事件
        aMap.setOnMapClickListener(this);
        //设置加载成功监听
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                //aMap.moveCamera(CameraUpdateFactory.zoomTo(20));//设置放大级别
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.140094, 112.990538), 20));
            }
        });

        // aMap.setMyLocationType()
        geocoderSearch = new GeocodeSearch(this.getActivity());
        geocoderSearch.setOnGeocodeSearchListener(new MyOnGeocodeSearchListener());
    }


    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {
        parkPointMarker = aMap.addMarker(new MarkerOptions()
                .title("")
                .snippet("在这附近停车")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.pic_park_location))
                .draggable(true));
        // parkPointMarker.setRotateAngle(90);// 设置marker旋转90度

        int width = this.getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int height = this.getActivity().getWindowManager().getDefaultDisplay().getHeight();

        parkPointMarker.setPositionByPixels(width / 2, height / 3 - 50);
        parkPointMarker.showInfoWindow();// 设置默认显示一个infowinfow
    }

    @Override
    public void onStart() {
        super.onStart();
        addMarkersToMap();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        System.out.println(TAG + "------onResume");
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mapView.onPause();
        deactivate();
        System.out.println(TAG + "------onPause");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        System.out.println(TAG + "------onStop");
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        System.out.println(TAG + "------onDestroyView");
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        System.out.println(TAG + "------onDestroy");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记
            case 0:
                Bundle b = null; //data为B中回传的Intent

                Double locationLongitude;
                Double locationLatitude;
                try {
                    b = data.getExtras();
                    locationTitle = b.getString("locationTitle");
                    locationSnippet = b.getString("locationSnippet");
                    locationLongitude = b.getDouble("locationLongitude");
                    locationLatitude = b.getDouble("locationLatitude");
                    //   System.out.println(""+locationLongitude+" "+locationLatitude);
                    isFromSearch = true;
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationLatitude, locationLongitude), 20));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

    }

    //定位信息**********************************************************************//

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                routeBegin = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getActivity());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    //点击事件**********************************************************************//
    @Override
    public void onMapClick(LatLng latLng) {
        if (mlastMarker != null) {
            resetlastmarker();
        }
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.main_search_layout) {
                //SearchActivity.actionStart(view.getContext());
                Intent intentForSearch = new Intent(view.getContext(), SearchActivity.class);
                MainActivityFrag.this.startActivityForResult(intentForSearch, 0);
            }

            if (id == R.id.layout_more_park) {
                MoreParkActivity.actionStart(view.getContext());

            }

            if (id == R.id.btn_go) {
                if (routeBegin == null || routeEnd == null) {
                    ToastUtil.show(MainActivityFrag.this.getActivity(), R.string.no_result);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putDouble("routeBeginLatitude", routeBegin.getLatitude());
                bundle.putDouble("routeBeginLongitude", routeBegin.getLongitude());
                bundle.putDouble("routeEndLatitude", routeEnd.getLatitude());
                bundle.putDouble("routeEndLongitude", routeEnd.getLongitude());

                RouteActivity.actionStart(view.getContext(), bundle);
            }

            if (id==R.id.layout_see_details){
                ParkDetailsActivity.actionStart(view.getContext(),new Bundle());
            }
        }
    }

    //移动地图*******************************************************************************//
    private class MyOnCameraChangeListener implements AMap.OnCameraChangeListener {

        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
            parkPointMarker.hideInfoWindow();
        }

        @Override
        public void onCameraChangeFinish(CameraPosition cameraPosition) {
            LatLng target = parkPointMarker.getPosition();
            //得到地理位置信息
            if (isFromSearch) {
                parkPointMarker.setTitle(locationTitle);
                locationtx.setText(locationSnippet);
                parkPointMarker.showInfoWindow();
                isFromSearch = false;
            } else {
                getAddress(AMapUtil.convertToLatLonPoint(target));
            }
            doSearchQuery(target);//搜索周边信息
        }
    }


    //位置信息*****************************************************************************//

//    /**
//     * 响应地理编码
//     */
//    public void getLatlon(final String name) {
//
//        GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
//        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
//    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        //showDialog();
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

    private class MyOnGeocodeSearchListener implements GeocodeSearch.OnGeocodeSearchListener {

        /**
         * 逆地理编码回调
         */
        @Override
        public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
            //dismissDialog();
            if (rCode == 1000) {
                if (result != null && result.getRegeocodeAddress() != null
                        && result.getRegeocodeAddress().getFormatAddress() != null) {
                    //String addressName = result.getRegeocodeAddress().getFormatAddress()+ "附近";
                    String addressName = result.getRegeocodeAddress().getPois().get(0).getSnippet();
                    //移动到搜索到的Camera
//                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                            AMapUtil.convertToLatLng(latLonPoint), 15));
                    // regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
                    //parkPointMarker.setSnippet(addressName);
                    locationtx.setText(addressName);
                    parkPointMarker.setTitle(result.getRegeocodeAddress().getPois().get(0).getTitle());
                } else {
                    ToastUtil.show(MainActivityFrag.this.getActivity(), R.string.no_result);
                }
            } else {
                ToastUtil.showerror(MainActivityFrag.this.getActivity(), rCode);
            }
            parkPointMarker.showInfoWindow();
        }

        /**
         * 地理编码回调
         */
        @Override
        public void onGeocodeSearched(GeocodeResult result, int rCode) {
        }
    }


    //周边信息*****************************************************************************//

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(LatLng latLng) {
        System.out.println("开始进行poi搜索");
        currentPage = 0;
        query = new PoiSearch.Query("停车", "", "长沙");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        if (latLng != null) {
            poiSearch = new PoiSearch(view.getContext(), query);
            poiSearch.setOnPoiSearchListener(new MyOnPoiSearchListener());
            poiSearch.setBound(new PoiSearch.SearchBound(AMapUtil.convertToLatLonPoint(latLng), 5000, true));//设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }

    private class MyOnMarkerClickListener implements AMap.OnMarkerClickListener {
        @Override
        public boolean onMarkerClick(Marker marker) {
            System.out.println(marker.getObject());
            if (marker.getObject() != null) {
                try {
                    PoiItem mCurrentPoi = (PoiItem) marker.getObject();
                    if (mlastMarker == null) {
                        mlastMarker = marker;
                    } else {
                        // 将之前被点击的marker置为原来的状态
                        resetlastmarker();
                        mlastMarker = marker;
                    }
                    detailMarker = marker;
                    detailMarker.setIcon(BitmapDescriptorFactory
                            .fromBitmap(BitmapFactory.decodeResource(
                                    getResources(),
                                    R.drawable.poi_marker_pressed)));

                    routeEnd = mCurrentPoi.getLatLonPoint();
                    setPoiItemDisplayContent(mCurrentPoi);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                 //resetlastmarker();
                routeEnd = AMapUtil.convertToLatLonPoint(marker.getPosition());
                setPoiItemDisplayContent(marker);
                //whetherToShowDetailInfo(false);

            }
            return true;
        }
    }

    //监听位置信息回调
    private class MyOnPoiSearchListener implements PoiSearch.OnPoiSearchListener {
        @Override
        public void onPoiSearched(PoiResult result, int rcode) {
            if (rcode == 1000) {
                if (result != null && result.getQuery() != null) {// 搜索poi的结果
                    if (result.getQuery().equals(query)) {// 是否是同一条
                        poiResult = result;
                        poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始

                            if (poiItems != null && poiItems.size() > 0) {
                                setPoiItemDisplayContent(poiItems.get(0));
                                routeEnd = poiItems.get(0).getLatLonPoint();
                                //并还原点击marker样式
                                if (mlastMarker != null) {
                                    resetlastmarker();
                                }
                            //清理之前搜索结果的marker
                            if (poiOverlay != null) {
                                // System.out.println("清理之前搜索结果的marker");
                                poiOverlay.removeFromMap();
                            }
//                            aMap.clear();
                            poiOverlay = new MyPoiOverlay(aMap, poiItems);
                            poiOverlay.addToMap();
//                            poiOverlay.zoomToSpan();


                        } else {
                            ToastUtil.show(MainActivityFrag.this.getActivity(),
                                    R.string.no_result);
                        }
                    }
                } else {
                    ToastUtil
                            .show(MainActivityFrag.this.getActivity(), R.string.no_result);
                }
            }
        }

        @Override
        public void onPoiItemSearched(PoiItem poiItem, int i) {

        }
    }


    // 将之前被点击的marker置为原来的状态
    private void resetlastmarker() {
        int index = poiOverlay.getPoiIndex(mlastMarker);
        if (index < 10) {
            mlastMarker.setIcon(BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(
                            getResources(),
                            markers[index])));
        } else {
            mlastMarker.setIcon(BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(getResources(), R.drawable.marker_other_highlight)));
        }
        mlastMarker = null;

    }

    //设置推荐信息
    private void setPoiItemDisplayContent(final PoiItem mCurrentPoi) {
        mPoiName.setText(mCurrentPoi.getTitle());
        mPoiAddress.setText(mCurrentPoi.getSnippet());
    }
    //设置推荐信息
    private void setPoiItemDisplayContent(final Marker mCurrentPoi) {
        mPoiName.setText(mCurrentPoi.getTitle());
        mPoiAddress.setText(mCurrentPoi.getSnippet());
    }


    /**
     * 自定义PoiOverlay
     */

    private class MyPoiOverlay {
        private AMap mamap;
        private List<PoiItem> mPois;
        private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();

        public MyPoiOverlay(AMap amap, List<PoiItem> pois) {
            mamap = amap;
            mPois = pois;
        }

        /**
         * 添加Marker到地图中。
         *
         * @since V2.1.0
         */
        public void addToMap() {
            if (markerOptions == null) {
                markerOptions = new MarkerOptions().position(
                        new LatLng(28.139396, 112.98916))
                        .title("铁道学院第二综合楼前坪").snippet("铁道学院第二综合楼前坪")
                        .icon(BitmapDescriptorFactory.fromBitmap(
                                BitmapFactory.decodeResource(getResources(), R.drawable.poi_marker_1_blue)));
                mamap.addMarker(markerOptions);
            }
            int count = mPois.size();
            if (count > 6) {
                count = 6;
            }
            for (int i = 0; i < count; i++) {
                Marker marker = mamap.addMarker(getMarkerOptions(i));
                System.out.println(marker.getTitle()+" "+ marker.getSnippet());
                PoiItem item = mPois.get(i);
                marker.setObject(item);
                mPoiMarks.add(marker);
            }
        }


        /**
         * 去掉PoiOverlay上所有的Marker。
         *
         * @since V2.1.0
         */
        public void removeFromMap() {
            for (Marker mark : mPoiMarks) {
                mark.remove();
            }
            mPoiMarks.clear();
        }

        /**
         * 移动镜头到当前的视角。
         *
         * @since V2.1.0
         */
        public void zoomToSpan() {
            if (mPois != null && mPois.size() > 0) {
                if (mamap == null)
                    return;
                LatLngBounds bounds = getLatLngBounds();
                mamap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        }

        private LatLngBounds getLatLngBounds() {
            LatLngBounds.Builder b = LatLngBounds.builder();
            for (int i = 0; i < mPois.size(); i++) {
                b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
                        mPois.get(i).getLatLonPoint().getLongitude()));
            }
            return b.build();
        }

        private MarkerOptions getMarkerOptions(int index) {
            return new MarkerOptions()
                    .position(
                            new LatLng(mPois.get(index).getLatLonPoint()
                                    .getLatitude(), mPois.get(index)
                                    .getLatLonPoint().getLongitude()))
                    .title(getTitle(index)).snippet(getSnippet(index))
                    .icon(getBitmapDescriptor(index));
        }

        protected String getTitle(int index) {
            return mPois.get(index).getTitle();
        }

        protected String getSnippet(int index) {
            return mPois.get(index).getSnippet();
        }

        /**
         * 从marker中得到poi在list的位置。
         *
         * @param marker 一个标记的对象。
         * @return 返回该marker对应的poi在list的位置。
         * @since V2.1.0
         */
        public int getPoiIndex(Marker marker) {
            for (int i = 0; i < mPoiMarks.size(); i++) {
                if (mPoiMarks.get(i).equals(marker)) {
                    return i;
                }
            }
            return -1;
        }

        /**
         * 返回第index的poi的信息。
         *
         * @param index 第几个poi。
         * @return poi的信息。poi对象详见搜索服务模块的基础核心包（com.amap.api.services.core）中的类 <strong><a href="../../../../../../Search/com/amap/api/services/core/PoiItem.html" title="com.amap.api.services.core中的类">PoiItem</a></strong>。
         * @since V2.1.0
         */
        public PoiItem getPoiItem(int index) {
            if (index < 0 || index >= mPois.size()) {
                return null;
            }
            return mPois.get(index);
        }

        protected BitmapDescriptor getBitmapDescriptor(int arg0) {
            if (arg0 < 10) {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), markers[arg0]));
                return icon;
            } else {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), R.drawable.marker_other_highlight));
                return icon;
            }
        }
    }
}
