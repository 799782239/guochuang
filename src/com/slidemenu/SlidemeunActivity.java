package com.slidemenu;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.example.notification.R;
import com.slidemenu.LayoutRelative.OnScrollListener;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class SlidemeunActivity extends Activity implements OnGestureListener,
		OnTouchListener, OnItemClickListener {

	private static final String TAG = "ChrisSlideMenu";
	private RelativeLayout mainLayout;
	private RelativeLayout leftLayout;
	private RelativeLayout rightLayout;
	private LayoutRelative layoutSlideMenu;
	private ListView mListMore;

	private ImageView ivMore;
	private ImageView ivSettings;
	private GestureDetector mGestureDetector;

	private static final int SPEED = 30;
	private boolean bIsScrolling = false;
	// private int iLimited = 0;
	// private int mScroll = 0;
	private View mClickedView = null;
	MapView mMapView;
	BaiduMap mBaiduMap;

	private String title[] = { "�����Ͷ���", "ͬ����������", "�༭�ҵ�����", "������", "��������",
			"��ʡ����", "��������", "�汾����", "�������", "���ֶһ�", "��ƷӦ��", "��������", "�˳���ǰ�ʺ�",
			"�˳�1", "�˳�2", "�˳�3", "�˳�4" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map);
		initView();
		mMapView = (MapView) findViewById(R.id.bmapView);

		mBaiduMap = mMapView.getMap();
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
		ivMore.setOnTouchListener(this);
		ivSettings.setOnTouchListener(this);

		mListMore = (ListView) findViewById(R.id.listView1);
		mListMore.setAdapter(new ArrayAdapter<String>(this, R.layout.items,
				R.id.tv_item, title));
		mListMore.setOnItemClickListener(this);

		mGestureDetector = new GestureDetector(getApplicationContext(), this);
		mGestureDetector.setIsLongpressEnabled(false);

		resizeLayout();
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
		// mScroll += distanceX; // ����Ϊ��
		// Log.d(TAG, "mScroll = " + mScroll + ", distanceX = " + distanceX);

		RelativeLayout.LayoutParams lp = (LayoutParams) mainLayout
				.getLayoutParams();
		// RelativeLayout.LayoutParams lpLeft = (LayoutParams)
		// leftLayout.getLayoutParams();
		// RelativeLayout.LayoutParams lpRight = (LayoutParams)
		// rightLayout.getLayoutParams();
		Log.d(TAG, "lp.leftMargin = " + lp.leftMargin);

		// int distance = 0;
		// if(mScroll > 0){ // �����ƶ�
		// if(lp.leftMargin <= 0){ // ���ҵ����˵�
		// if(iLimited > 0){
		// return;
		// }
		// distance = lpRight.width - Math.abs(lp.leftMargin);
		// }else if(lp.leftMargin > 0){ // �ر��󵼺��˵�
		// distance = lp.leftMargin;
		// }
		// if(mScroll >= distance){
		// mScroll = distance;
		// }
		// }else if(mScroll < 0){ // �����ƶ�
		// if(lp.leftMargin >= 0){ // ���󵼺��˵�
		// if(iLimited < 0){
		// return;
		// }
		// distance = lpLeft.width - Math.abs(lp.leftMargin);
		// }else if(lp.leftMargin < 0){ // �ر��ҵ����˵�
		// distance = Math.abs(lp.leftMargin);
		// }
		// if(mScroll <= -distance){
		// mScroll = -distance;
		// }
		// }

		// Log.d(TAG, "mScroll = " + mScroll);
		// if(mScroll != 0){
		// rollLayout(-mScroll);
		// }
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

	// /////////////////// ListView.onItemClick ///////////////////////
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Toast.makeText(this, title[arg2], Toast.LENGTH_SHORT).show();
	}

	// //////////////////////////// onTouch ///////////////////////////////
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
	 * @author cheng.yang
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
}
