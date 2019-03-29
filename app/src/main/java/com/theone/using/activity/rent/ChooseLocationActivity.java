package com.theone.using.activity.rent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.theone.using.R;
import com.theone.using.activity.share.ShareDetailsActivity;
import com.theone.using.amap.AMapUtil;
import com.theone.using.common.BaseActivity;
import com.theone.using.common.TitleLayout;
import com.theone.using.amap.ToastUtil;

public class ChooseLocationActivity extends BaseActivity implements LocationSource,
        AMapLocationListener {
    private TitleLayout titleLayout;
    private MapView mapView;
    private AMap aMap;
    private Marker shareMarker;
    private Button chooseLocationbtn;
    Intent intent;

    //定位
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private ProgressDialog progDialog = null;

    //逆地理编码
    private GeocodeSearch geocoderSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        initView();
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        initMap();
    }

    void initView() {
        titleLayout = (TitleLayout) findViewById(R.id.title);
        titleLayout.getBackView().setImageResource(R.drawable.pic_back);
        titleLayout.getTitletx().setText("分享车位");
        MyOnClickListener l = new MyOnClickListener();
        titleLayout.getBackView().setOnClickListener(l);
        chooseLocationbtn = (Button) findViewById(R.id.btn_choose_location);

        chooseLocationbtn.setOnClickListener(l);
        progDialog = new ProgressDialog(this);
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new MyOnGeocodeSearchListener());

        try {
            intent = getIntent();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker2));// 设置小蓝点的图标
        //myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        // myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 没有颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点

        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细

        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // aMap.setMyLocationType()
        aMap.moveCamera(CameraUpdateFactory.zoomTo(20));//设置放大级别
        aMap.setOnCameraChangeListener(new MyOnCameraChangeListener());
        //设置加载成功监听
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.140094, 112.990538), 20));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        addMarkersToMap();// 往地图上添加marker
    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {
        shareMarker = aMap.addMarker(new MarkerOptions()
                .title("")
                .snippet("出租车位")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.pic_park_location))
                .draggable(true));
        // shareMarker.setRotateAngle(90);// 设置marker旋转90度

        int width = getWindowManager().getDefaultDisplay().getWidth();
        int height = getWindowManager().getDefaultDisplay().getHeight();

        shareMarker.setPositionByPixels(width / 2, height / 3 + 150);
        shareMarker.showInfoWindow();// 设置默认显示一个infowinfow

    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ChooseLocationActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);

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
//                    String addressName = result.getRegeocodeAddress().getFormatAddress();
                    String addressName = result.getRegeocodeAddress().getPois().get(0).getSnippet();
                    //移动到搜索到的Camera
//                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                            AMapUtil.convertToLatLng(latLonPoint), 15));
                    // regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
                    shareMarker.setTitle(addressName);
                    //ToastUtil.show(ShareActivity.this, addressName);
                } else {
                    ToastUtil.show(ChooseLocationActivity.this, R.string.no_result);
                }
            } else {
                ToastUtil.showerror(ChooseLocationActivity.this, rCode);
            }
            shareMarker.showInfoWindow();
        }


        @Override
        public void onGeocodeSearched(GeocodeResult result, int rCode) {
        }
    }

    private class MyOnCameraChangeListener implements AMap.OnCameraChangeListener {
        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
            shareMarker.hideInfoWindow();
        }

        @Override
        public void onCameraChangeFinish(CameraPosition cameraPosition) {
            LatLng target = shareMarker.getPosition();
            getAddress(AMapUtil.convertToLatLonPoint(target));
        }
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.title_iv) {
                ChooseLocationActivity.this.finish();
            }
            if (id == R.id.btn_choose_location) {
                Bundle bundle = new Bundle();
                bundle.putString("markertitle", shareMarker.getTitle());
                intent.putExtras(bundle);
                ChooseLocationActivity.this.setResult(0, intent);
                ChooseLocationActivity.this.finish();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    /**
     * 显示进度条对话框
     */
    public void showDialog() {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在获取地址");
        progDialog.show();
    }

    /**
     * 隐藏进度条对话框
     */
    public void dismissDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 响应地理编码
     */
    public void getLatlon(final String name) {
        // showDialog();
        GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        //showDialog();
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }
}