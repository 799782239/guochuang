package com.example.android_mapceshi;

import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import android.R.string;
import android.support.v7.app.ActionBarActivity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends ActionBarActivity {

	// ��λ���
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;

	MapView mMapView;
	BaiduMap mBaiduMap;

	// UI���
	OnCheckedChangeListener radioButtonListener;
	Button requestLocButton;
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ

	private Marker mMarkerA;

	private ListView listView;
	private ArrayAdapter<String> adapter;
	private List<String> data = null;// ����Դ

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());

		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		setContentView(R.layout.activity_main);
		// �б�
		data =MyData.getDataSource();
		adapter = new ArrayAdapter<String>(MainActivity.this,
				android.R.layout.simple_expandable_list_item_1, data);
		listView = (ListView) this.findViewById(R.id.listView1);
		listView.setAdapter(adapter);
		// ��ť
		requestLocButton = (Button) findViewById(R.id.button1);
		mCurrentMode = LocationMode.NORMAL;
		requestLocButton.setText("��ͨ");
		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				switch (mCurrentMode) {
				case NORMAL:
					requestLocButton.setText("����");
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case COMPASS:
					requestLocButton.setText("��ͨ");
					mCurrentMode = LocationMode.NORMAL;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case FOLLOWING:
					requestLocButton.setText("����");
					mCurrentMode = LocationMode.COMPASS;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				}
			}
		};
		requestLocButton.setOnClickListener(btnClickListener);

		RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
		radioButtonListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.defaulticon) {
					// ����null�򣬻ָ�Ĭ��ͼ��
					mCurrentMarker = null;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, null));
				}
				if (checkedId == R.id.customicon) {
					// �޸�Ϊ�Զ���marker
					mCurrentMarker = BitmapDescriptorFactory
							.fromResource(R.drawable.icon_geo);
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
				}
			}
		};
		group.setOnCheckedChangeListener(radioButtonListener);

		// ��ͼ��ʼ��
		mMapView = (MapView) findViewById(R.id.bmapView);

		mBaiduMap = mMapView.getMap();

		// ������λͼ��
		mBaiduMap.setMyLocationEnabled(true);
		// ��λ��ʼ��
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(1000);

		mLocClient.setLocOption(option);
		mLocClient.getLocOption();
		mLocClient.start();
		double addr, addr2;
		// 39.97147, 116.357916
		addr = 39.97147;
		addr2 = 116.357916;
		Mark(addr, addr2);
		addr2 = 116.355585;
		addr = 39.967043;
		Mark(addr, addr2);
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub
				Button button = new Button(getApplicationContext());
				button.setBackgroundResource(R.drawable.popup);
				OnInfoWindowClickListener listener = null;
				if (marker == mMarkerA) {
					button.setText("�û���");
					// ����������ʾ��InfoWindow�������
					LatLng pt = marker.getPosition();
					// ����InfoWindow , ���� view�� �������꣬ y ��ƫ����
					InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);
					// ��ʾInfoWindow
					mBaiduMap.showInfoWindow(mInfoWindow);
				}
				return true;
			}
		});

	}

	public void Mark(double arg, double arg1) {
		LatLng point = new LatLng(arg, arg1);
		// ����Markerͼ��
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.mark1);
		// ����MarkerOption�������ڵ�ͼ�����Marker
		OverlayOptions option1 = new MarkerOptions().position(point).icon(
				bitmap);
		mMarkerA = (Marker) (mBaiduMap.addOverlay(option1));
		// �ڵ�ͼ�����Marker������ʾ
		// ����InfoWindowչʾ��view
	}

	/**
	 * ��λSDK��������
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);

			}

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// �˳�ʱ���ٶ�λ
		mLocClient.stop();
		// �رն�λͼ��
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

}
