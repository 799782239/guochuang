package com.slidemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.RelativeLayout;

public class LayoutRelative extends RelativeLayout {

	private GestureDetector mGestureDetector;
	private boolean bLockScrollX = false;
	private boolean bTouchIntercept = false;

	private OnScrollListener mOnScrollListenerCallback = null;

	public LayoutRelative(Context context) {
		this(context, null);
	}

	public LayoutRelative(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LayoutRelative(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mGestureDetector = new GestureDetector(getContext(),
				new LayoutGestureListener());
	}

	/**
	 * ���ù��������ӿ�
	 * 
	 * @param l
	 */
	public void setOnScrollListener(OnScrollListener l) {
		mOnScrollListenerCallback = l;
	}

	/*
	 * Progress: 1. ����dispatchTouchEvent������Ϣ���ݸ�GestureDetector; 2. ����������onDown ��
	 * onScroll��������; 3. ��onDown�У�Ĭ�϶�ˮƽ�����������; 4. ��onScroll�У��ж�e1��e2��ˮƽ�����봹ֱ�������:
	 * a. �����ֱ���������������¹������ҷ���false������ǰ���Ʋ������أ� b.
	 * ���ˮƽ���������������ҹ������ҷ���true������ǰ������Ҫ���أ� 5.
	 * ����onInterceptTouchEvent��������Ʒ���Ϊtrue����onInterceptTouchEventҲ����true�� 6.
	 * ���Ҫ����������Ϣ������Ҫ����onTouchEvent��������ͼ���������������������������Ϣ�� 7.
	 * ����Լ��������ˮƽ�������ȥ��(������ǰ�û������ҹ���)��
	 * 
	 * ---------- ---------------------- ------>| onDown | | | | ---------- |
	 * dispatchTouchEvent | <---- ------ false: ���¹��� | | | ------------ /
	 * ---------------------- ------>| onScroll | ---- | ------------ \ | ------
	 * true : ���ҹ��� | intercept = true ---------------- |----------------------|
	 * onTouchEvent | | ---------------- ------------------------- | | |
	 * onInterceptTouchEvent | | | -------------------------
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		bTouchIntercept = mGestureDetector.onTouchEvent(ev);

		if (MotionEvent.ACTION_UP == ev.getAction() && !bLockScrollX) {
			if (mOnScrollListenerCallback != null) {
				mOnScrollListenerCallback.doOnRelease();
			}
		}

		return super.dispatchTouchEvent(ev);
	}

	// viewgroup.onInterceptTouchEvent
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		super.onInterceptTouchEvent(ev);
		return bTouchIntercept;
	}

	// view.onTouchEvent
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		bLockScrollX = false;
		return super.onTouchEvent(event);
	}

	// ���Ƽ���
	public class LayoutGestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			bLockScrollX = true;
			return super.onDown(e);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (!bLockScrollX) {
				if (mOnScrollListenerCallback != null) {
					mOnScrollListenerCallback.doOnScroll(e1, e2, distanceX,
							distanceY);
				}
			}

			if (Math.abs(e1.getY() - e2.getY()) > Math.abs(e1.getX()
					- e2.getX())) {
				return false;
			} else {
				return true;
			}
		}
	}

	public interface OnScrollListener {
		void doOnScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				float distanceY);

		void doOnRelease();
	}
}
