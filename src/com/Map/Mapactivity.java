package com.Map;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.Chat.ChatActivity;
import com.FriendData.FriendInfo;
import com.FriendData.LocalFriendInfoXMLToList;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.notification.R;
import com.slidemenu.LayoutRelative;
import com.slidemenu.LayoutRelative.OnScrollListener;

@SuppressLint("ShowToast")
public class Mapactivity extends ActionBarActivity implements
		OnGestureListener, OnTouchListener {
	// ��λ���
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	BitmapDescriptor mCurrentMarker;
	private double mCurrentLantitude;
	private double mCurrentLongitude;

	// ��ͼ���
	MapView mMapView;
	BaiduMap mBaiduMap;
	private Marker mMarkerA;
	private TextView textState;
	private InfoWindow minfoWindow;

	// UI���
	private ImageButton First, Second, Third;
	private ImageView right_Top, right_buttom;
	OnCheckedChangeListener radioButtonListener;
	Button requestLocButton;
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ

	private List<String> data = null;// ����Դ
	LocalFriendInfoXMLToList localFriendInfoXMLToList = new LocalFriendInfoXMLToList();
	List<FriendInfo> InfoList = new ArrayList<FriendInfo>();

	// sildmenu
	private static final String TAG = "ChrisSlideMenu";
	private RelativeLayout mainLayout;
	private RelativeLayout leftLayout;
	private RelativeLayout rightLayout;
	private LayoutRelative layoutSlideMenu;
	private ListView mListMore;

	private ImageView ivMore;
	private ImageView ivSettings;
	private ImageView ivTopButton;
	private GestureDetector mGestureDetector;

	private static final int SPEED = 30;
	private boolean bIsScrolling = false;
	private View mClickedView = null;

	private String sender = null;
	private String reciver;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Intent intent=getIntent();
		// sender=intent.getStringExtra("user");
		sender = "yanqi";
		setContentView(R.layout.map);
		initView();
		mapactivity();

	}

	private void mapactivity() {
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);

		// ��ͼ��ʼ��
		mMapView = (MapView) findViewById(R.id.bmapView);
		First = (ImageButton) findViewById(R.id.first);
		Second = (ImageButton) findViewById(R.id.Second);
		Third = (ImageButton) findViewById(R.id.Third);
		mMapView.showZoomControls(false);

		mBaiduMap = mMapView.getMap();
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(16);
		mBaiduMap.animateMapStatus(mapStatusUpdate);
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
	}

	public void Mark(double arg, double arg1) {
		LatLng point = new LatLng(arg, arg1);
		// ����Markerͼ��
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.mark1);
		// ����MarkerOption�������ڵ�ͼ�����Marker
		OverlayOptions option1 = new MarkerOptions().position(point).icon(
				bitmap);
		mBaiduMap.addOverlay(option1);
		// �ڵ�ͼ�����Marker������ʾ
		// ����InfoWindowչʾ��view
	}

	private void initView() {
		mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
		leftLayout = (RelativeLayout) findViewById(R.id.leftLayout);
		rightLayout = (RelativeLayout) findViewById(R.id.rightLayout);

		mainLayout.setOnTouchListener(this);
		leftLayout.setOnTouchListener(this);
		rightLayout.setOnTouchListener(this);

		layoutSlideMenu = (LayoutRelative) findViewById(R.id.layoutSlideMenu);
		layoutSlideMenu.setOnScrollListener(new OnScrollListener() {
			@Override
			public void doOnScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				onScroll(distanceX);
			}

			@Override
			public void doOnRelease() {
				onRelease();
			}
		});

		ivMore = (ImageView) findViewById(R.id.ivMore);
		ivSettings = (ImageView) findViewById(R.id.ivSettings);
		ivTopButton = (ImageView) findViewById(R.id.top_button);
		ivTopButton.bringToFront();
		ivTopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LatLng ll = new LatLng(mCurrentLantitude, mCurrentLongitude);
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		});
		ivMore.setOnTouchListener(this);
		ivSettings.setOnTouchListener(this);

		right_Top = (ImageView) findViewById(R.id.text);
		right_Top.setClickable(true);
		right_buttom = (ImageView) findViewById(R.id.text2);
		right_buttom.setClickable(true);

		mListMore = (ListView) findViewById(R.id.listView1);
		setListMore s = new setListMore();
		s.execute("");

		mGestureDetector = new GestureDetector(getApplicationContext(), this);
		mGestureDetector.setIsLongpressEnabled(false);

		resizeLayout();
		right_Top.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setLocal sLocal = new setLocal();
				sLocal.execute("");
			}
		});
		right_buttom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setState state = new setState();
				state.execute("");
			}
		});
	}

	/*
	 * ʹ��leftMargin��rightMargin��ֹlayout����ѹ���� Math.abs(leftMargin - rightMargin)
	 * = layout.width
	 */
	private void resizeLayout() {
		DisplayMetrics dm = getResources().getDisplayMetrics();

		// �̶� main layout, ��ֹ�����Ҽ�ѹ����
		RelativeLayout.LayoutParams lp = (LayoutParams) mainLayout
				.getLayoutParams();
		lp.width = dm.widthPixels;
		mainLayout.setLayoutParams(lp);

		// ����layout������main layout���
		lp = (LayoutParams) leftLayout.getLayoutParams();
		lp.leftMargin = -lp.width;
		leftLayout.setLayoutParams(lp);
		Log.d(TAG, "left l.margin = " + lp.leftMargin);

		// ����layout������main layout�ұ�
		lp = (LayoutParams) rightLayout.getLayoutParams();
		lp.leftMargin = dm.widthPixels;
		lp.rightMargin = -lp.width;
		rightLayout.setLayoutParams(lp);
		Log.d(TAG, "right l.margin = " + lp.leftMargin);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			RelativeLayout.LayoutParams lp = (LayoutParams) mainLayout
					.getLayoutParams();

			if (lp.leftMargin != 0) {
				if (lp.leftMargin > 0) {
					new SlideMenu().execute(leftLayout.getLayoutParams().width,
							-SPEED);
				} else if (lp.leftMargin < 0) {
					new SlideMenu().execute(
							rightLayout.getLayoutParams().width, SPEED);
				}
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void rollLayout(int margin) {
		RelativeLayout.LayoutParams lp = (LayoutParams) mainLayout
				.getLayoutParams();
		lp.leftMargin += margin;
		lp.rightMargin -= margin;
		mainLayout.setLayoutParams(lp);
		lp = (LayoutParams) leftLayout.getLayoutParams();
		lp.leftMargin += margin;
		leftLayout.setLayoutParams(lp);
		lp = (LayoutParams) rightLayout.getLayoutParams();
		lp.leftMargin += margin;
		lp.rightMargin -= margin;
		rightLayout.setLayoutParams(lp);
	}

	private void onScroll(float distanceX) {
		bIsScrolling = true;
		RelativeLayout.LayoutParams lp = (LayoutParams) mainLayout
				.getLayoutParams();
	}

	private void onRelease() {
		RelativeLayout.LayoutParams lp = (LayoutParams) mainLayout
				.getLayoutParams();
		if (lp.leftMargin < 0) { // ����
			/*
			 * ���ƴ����ҵ������һ�룬���Զ�չ��,�����Զ�����ȥ
			 */
			if (Math.abs(lp.leftMargin) > rightLayout.getLayoutParams().width / 2) {
				new SlideMenu().execute(rightLayout.getLayoutParams().width
						- Math.abs(lp.leftMargin), -SPEED);
			} else {
				new SlideMenu().execute(Math.abs(lp.leftMargin), SPEED);
			}
		} else if (lp.leftMargin > 0) {
			/*
			 * ���ƴ����󵼺����һ�룬���Զ�չ��,�����Զ�����ȥ
			 */
			if (Math.abs(lp.leftMargin) > leftLayout.getLayoutParams().width / 2) {
				new SlideMenu().execute(leftLayout.getLayoutParams().width
						- Math.abs(lp.leftMargin), SPEED);
			} else {
				new SlideMenu().execute(Math.abs(lp.leftMargin), -SPEED);
			}
		}
	}

	// //////////////////////////// onTouch ///////////////////////////////
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		mClickedView = v;

		if (MotionEvent.ACTION_UP == event.getAction() && bIsScrolling) {
			onRelease();
		}

		return mGestureDetector.onTouchEvent(event);
	}

	// ///////////////// GestureDetector Override Begin ///////////////////
	@Override
	public boolean onDown(MotionEvent e) {

		bIsScrolling = false;
		// mScroll = 0;
		// iLimited = 0;
		RelativeLayout.LayoutParams lp = (LayoutParams) mainLayout
				.getLayoutParams();
		if (lp.leftMargin > 0) {
			// iLimited = 1;
		} else if (lp.leftMargin < 0) {
			// iLimited = -1;
		}

		return true;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		onScroll(distanceX);
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		/*
		 * ��������£�mainLayout��leftMarginΪ0, ����/�Ҳ˵�Ϊ���У���ʱ�Ͳ�Ϊ0����Ҫ�ж�
		 */
		if (mClickedView != null) {
			RelativeLayout.LayoutParams lp = (LayoutParams) mainLayout
					.getLayoutParams();

			if (mClickedView == ivMore) {
				Log.d(TAG, "[onSingleTapUp] ivMore clicked! leftMargin = "
						+ lp.leftMargin);

				if (lp.leftMargin == 0) {
					new SlideMenu().execute(leftLayout.getLayoutParams().width,
							SPEED);
				} else {
					new SlideMenu().execute(leftLayout.getLayoutParams().width,
							-SPEED);
				}
			} else if (mClickedView == ivSettings) {
				Log.d(TAG, "[onSingleTapUp] ivSettings clicked! leftMargin = "
						+ lp.leftMargin);

				if (lp.leftMargin == 0) {
					new SlideMenu().execute(
							rightLayout.getLayoutParams().width, -SPEED);
				} else {
					new SlideMenu().execute(
							rightLayout.getLayoutParams().width, SPEED);
				}
			} else if (mClickedView == mainLayout) {
				Log.d(TAG, "[onSingleTapUp] mainLayout clicked!");
			}
		}
		return true;
	}

	// ///////////////// GestureDetector Override End ///////////////////

	/**
	 * 
	 * @author ��
	 * 
	 *         ���Ҳ˵�����
	 * 
	 *         params[0]: �������� params[1]: �����ٶ�,������
	 */
	public class SlideMenu extends AsyncTask<Integer, Integer, Void> {
		@Override
		protected Void doInBackground(Integer... params) {
			if (params.length != 2) {
				Log.e(TAG, "error, params must have 2!");
			}

			int times = params[0] / Math.abs(params[1]);
			if (params[0] % Math.abs(params[1]) != 0) {
				times++;
			}

			for (int i = 0; i < times; i++) {
				this.publishProgress(params[0], params[1], i + 1);
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			if (values.length != 3) {
				Log.e(TAG, "error, values must have 3!");
			}

			int distance = Math.abs(values[1]) * values[2];
			int delta = values[0] - distance;

			int leftMargin = 0;
			if (values[1] < 0) { // ����
				leftMargin = (delta > 0 ? values[1]
						: -(Math.abs(values[1]) - Math.abs(delta)));
			} else {
				leftMargin = (delta > 0 ? values[1]
						: (Math.abs(values[1]) - Math.abs(delta)));
			}

			rollLayout(leftMargin);
		}
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
			mCurrentLongitude = location.getLongitude();
			mCurrentLantitude = location.getLatitude();
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

	/**
	 * @author yQ ˢ�º����б�
	 */
	public class setListMore extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			data = mapdata.getDataSource();
			InfoList = localFriendInfoXMLToList.getFriendInfoList();

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			mListMore
					.setAdapter(new ArrayAdapter<String>(
							getApplicationContext(), R.layout.items,
							R.id.tv_item, data));
			mListMore.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					// double addr, addr2;
					// addr = InfoList.get(arg2).getFriend_Local_latitude();
					// addr2 = InfoList.get(arg2).getFriend_Local_longitude();
					// LatLng cenpt = new LatLng(addr2, addr);
					// MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
					// .newLatLng(cenpt);
					// mBaiduMap.setMapStatus(mapStatusUpdate);
					reciver = InfoList.get(arg2).getFriend_Name();
					Intent intent = new Intent(Mapactivity.this,
							ChatActivity.class);
					intent.putExtra("sender", sender);
					intent.putExtra("reciver", "wangjialu");
					startActivity(intent);

				}
			});
		}
	}

	/**
	 * @author yQ ����ˢ��״̬ǽ
	 * 
	 */
	public class setState extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			InfoList = localFriendInfoXMLToList.getFriendInfoList();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			mBaiduMap.clear();
			textState = new TextView(getApplicationContext());
			textState.setBackgroundResource(R.drawable.popup);
			// OnInfoWindowClickListener listener = null;
			// if (marker == mMarkerA) {
			textState.setText("��һ����");
			// ����������ʾ��InfoWindow�������
			double addr, addr2;
			for (int i = 0; i < InfoList.size(); i++) {
				addr = InfoList.get(i).getFriend_Local_latitude();
				addr2 = InfoList.get(i).getFriend_Local_longitude();
				LatLng pt = new LatLng(addr2, addr);

				// ����InfoWindow , ���� view�� �������꣬ y ��ƫ����
				InfoWindow mInfoWindow = new InfoWindow(textState, pt, -47);
				// ��ʾInfoWindow
				mBaiduMap.showInfoWindow(mInfoWindow);
			}
		}
	}

	public class setLocal extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			InfoList = localFriendInfoXMLToList.getFriendInfoList();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			mBaiduMap.clear();
			double addr, addr2;
			for (int i = 0; i < InfoList.size(); i++) {
				addr = InfoList.get(i).getFriend_Local_latitude();
				addr2 = InfoList.get(i).getFriend_Local_longitude();
				Mark(addr2, addr);
			}

		}

	}
}
