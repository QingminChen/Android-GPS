package com.certusnet.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.certusnet.dao.LocationPoint;
import com.certusnet.db.DBManager;
import com.certusnet.gdmap_gps_orbit.R;

/**
 * AMapV2地图中简单介绍显示定位小蓝点
 */
public class LocationSourceActivity extends RoboActivity implements LocationSource,AMapLocationListener{
	private AMap aMap;
	//private MapView mapView;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private Marker marker;// 定位雷达小图标,测试更改的效果
	private List<LocationPoint> locationPointList = new ArrayList<LocationPoint>();
	private List<LocationPoint> locationPointListTest = new ArrayList<LocationPoint>();
	private List<LocationPoint> locationPointListCur = new ArrayList<LocationPoint>();
	
	@InjectView(R.id.map)
	MapView mapView;
	
	@Inject
	DBManager dbManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationsource_activity);
		mapView.onCreate(savedInstanceState);
		init();
		
		testData();

	}

	public  void testData(){
		LocationPoint locationPoint = new LocationPoint();
		locationPoint.setLatitude("31.289664");
		locationPoint.setLongitude("121.510228");
		locationPointList.add(locationPoint);
		
		locationPoint = new LocationPoint();
		locationPoint.setLatitude("31.282182");
		locationPoint.setLongitude("121.50631");
		locationPointList.add(locationPoint);
		
		locationPoint = new LocationPoint();
		locationPoint.setLatitude("31.274879");
		locationPoint.setLongitude("121.501481");
		locationPointList.add(locationPoint);
		
		locationPoint = new LocationPoint();
		locationPoint.setLatitude("31.268641");
		locationPoint.setLongitude("121.494488");
		locationPointList.add(locationPoint);
		
		locationPoint = new LocationPoint();
		locationPoint.setLatitude("31.259276");
		locationPoint.setLongitude("121.488675");
		locationPointList.add(locationPoint);
		
		locationPoint = new LocationPoint();
		locationPoint.setLatitude("31.251571");
		locationPoint.setLongitude("121.476368");
		locationPointList.add(locationPoint);
		
		locationPoint = new LocationPoint();
		locationPoint.setLatitude("31.24988");
		locationPoint.setLongitude("121.4579");
		locationPointList.add(locationPoint);
		
		locationPoint = new LocationPoint();
		locationPoint.setLatitude("31.254701");
		locationPoint.setLongitude("121.441192");
		locationPointList.add(locationPoint);
		
		locationPoint = new LocationPoint();
		locationPoint.setLatitude("31.246385");
		locationPoint.setLongitude("121.429829");
		locationPointList.add(locationPoint);
		
		locationPoint = new LocationPoint();
		locationPoint.setLatitude("31.238283");
		locationPoint.setLongitude("121.417206");
		locationPointList.add(locationPoint);
		
		locationPoint = new LocationPoint();
		locationPoint.setLatitude("31.241817");
		locationPoint.setLongitude("121.411453");
		locationPointList.add(locationPoint);
		locationPointListTest = locationPointList;
	}
	/**
	 * 初始化
	 */
	
	private int runCount = 0;
	//private int runIntervalCount = 0;
	private Long preLocationTime = 0L;
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		
	}

	static Timer timer;
	/**以下产生的是测试数据**/
	private class TimerTest03 {
	    
	    public TimerTest03(){
	    	if(timer == null){
	    		timer = new Timer();
	    		timer.scheduleAtFixedRate(new TimerTaskTest03(),0,3000);
	    	}
	    }
	}

	private class TimerTaskTest03 extends TimerTask{

	    @Override
	    public void run() {
//	    	Log.i("abc",Thread.currentThread().getName()+":"+String.valueOf(System.currentTimeMillis()));
	    	runCount = runCount+1;
	    	if(runCount<12){
	    		if(runCount!=1){
	    			if((System.currentTimeMillis()-preLocationTime)>=2000){
				          locationPointListTest.get(runCount-1).setMarkFlag("1");
			        }
	    		}else{
	    			locationPointListTest.get(runCount-1).setMarkFlag("0");
	    		}
    		    //DBManagerOld.insertLocation(locationPointListTest.get(runCount-1));
	    		dbManager.create(locationPointListTest.get(runCount-1));
			    reFreshMap();
		        preLocationTime = System.currentTimeMillis();
	    	}
	    }
	}
	/**测试数据执行代码我完毕**/
	
	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point1));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point2));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point3));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point4));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point5));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point6));
		marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).icons(giflist).period(50));
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(0.1f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setMyLocationRotateAngle(180);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		//设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种 
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
	}

	
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
		deleteAll();
	}

	private void deleteAll(){
		List<LocationPoint> locationPointListTemp = new ArrayList<LocationPoint>();
		locationPointListTemp = dbManager.queryForAll(LocationPoint.class);
		for(int i=0;i<locationPointListTemp.size();i++){
			dbManager.remove(locationPointListTemp.get(i));
		}
	}
	
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * 此方法已经废弃
	 */
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/**
	 * 定位成功后回调函数
	 */
	private Double latitudeTemp =  0.0;
	
	private Double longitudeTemp = 0.0;
	
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			Log.i("abc2",String.valueOf(System.currentTimeMillis()));
			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
			
//			runCount = runCount+1;
//			LocationPoint locationPoint =new LocationPoint();
//			if(runCount!=1){
//				    if((latitudeTemp !=aLocation.getLatitude())||(longitudeTemp!=aLocation.getLongitude())){
//				    	if((System.currentTimeMillis()-preLocationTime)>=1000){
//							locationPoint.setMarkFlag("1");
//				        }else{
//				        	locationPoint.setMarkFlag("0");
//				        }
//				    }else{
//				        locationPoint.setMarkFlag("0");
//				    }
//			}else{
//				locationPoint.setMarkFlag("0");
//				latitudeTemp = aLocation.getLatitude();
//				longitudeTemp = aLocation.getLongitude();
//			}
//			locationPoint.setLatitude(String.valueOf(aLocation.getLatitude()));
//			locationPoint.setLongitude(String.valueOf(aLocation.getLongitude()));
//			dbManager.create(locationPoint);
//			preLocationTime = System.currentTimeMillis();
			
			marker.setPosition(new LatLng(aLocation.getLatitude(), aLocation.getLongitude()));// 定位雷达小图标
			float bearing = aMap.getCameraPosition().bearing;
			aMap.setMyLocationRotateAngle(bearing);// 设置小蓝点旋转角度
			new TimerTest03();
			//reFreshMap();
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork,3, 1, this);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	@SuppressWarnings("unchecked")
	private LocationPoint getLocationPointMax(){
		List<LocationPoint> locationPointListTemp =  new ArrayList<LocationPoint>();
		locationPointListTemp = dbManager.queryForAll(LocationPoint.class);
		int idTemp = 0;
		for(int i=0;i<locationPointListTemp.size();i++){
			if(locationPointListTemp.get(i).getId()>=idTemp){
				idTemp = locationPointListTemp.get(i).getId();
			}
		}
		return (LocationPoint) dbManager.queryForEq(LocationPoint.class, "id",idTemp).get(0);
	}

	private void reFreshMap(){
		marker.setPosition(new LatLng(Double.parseDouble(getLocationPointMax().getLatitude()),Double.parseDouble(getLocationPointMax().getLongitude())));// 定位雷达小图标
		float bearing = aMap.getCameraPosition().bearing;
		aMap.setMyLocationRotateAngle(bearing);// 设置小蓝点旋转角度
		
		
		final PolylineOptions  polyLineOptions = new PolylineOptions();
		locationPointListCur = dbManager.queryForAll(LocationPoint.class);
 	    List<LatLng> latLngList = new ArrayList<LatLng>();
 	    for(int i=0;i<locationPointListCur.size();i++){
 	    	LatLng latlng = new LatLng(Double.parseDouble(locationPointListCur.get(i).getLatitude()), Double.parseDouble(locationPointListCur.get(i).getLongitude()));
 	    	latLngList.add(latlng);
 	    	if(locationPointListCur.get(i).getMarkFlag().equalsIgnoreCase("1")){
 	    		MarkerOptions markerOptions = new MarkerOptions();
 	 			markerOptions.position(new LatLng(Double.parseDouble(locationPointListCur.get(i).getLatitude()),Double.parseDouble(locationPointListCur.get(i).getLongitude())));
 	 			markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location));
 	 			Marker marker = aMap.addMarker(markerOptions);
 	    	}
 	    }
		polyLineOptions.addAll(latLngList);
		polyLineOptions.color(Color.BLACK);
		aMap.addPolyline(polyLineOptions);
	}

	private int countBack = 0;
	private Long firstBackDownTime = 0L;
	private Long secondBackDownTime = 0L;
	@Override
	public void onBackPressed() {
		countBack = countBack+1;
		if(countBack==1){
			firstBackDownTime = System.currentTimeMillis();
			Toast.makeText(getBaseContext(), "再按一次退出应用", Toast.LENGTH_SHORT).show();
		}else if(countBack==2){
			secondBackDownTime = System.currentTimeMillis();
			if((secondBackDownTime-firstBackDownTime)>3000){
				       countBack = 1;
				       firstBackDownTime =System.currentTimeMillis();
				       secondBackDownTime = 0L;
				       Toast.makeText(getBaseContext(), "再按一次退出应用", Toast.LENGTH_SHORT).show();
			}else{
				       this.finish();
				       deleteAll();
			}
		}else{
			countBack = 0;
		    firstBackDownTime =0L;
		    secondBackDownTime = 0L;
		}
	}
}