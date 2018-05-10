package com.gaoshoubang.widget.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * viewpage 和listview 相互冲突 将父view 传递到viewpage 里面 使用父类的方法
 * parent.requestDisallowInterceptTouchEvent(true); 当
 * requestDisallowInterceptTouchEvent 如果为true的时候 表示:父view 不拦截子view的touch 事件
 * 这个方法只是改变flag
 */
public class DecoratorViewPager extends ViewPager {
	private ViewGroup parent;
	/**
	 * 最新一次的手指触摸位置Y
	 */
	private float mLastMotionY;
	/**
	 * 最新一次的手指触摸位置X
	 */
	private float mLastMotionX;


	public DecoratorViewPager(Context context) {
		super(context);
	}

	public DecoratorViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setNestedpParent(ViewGroup parent) {
		this.parent = parent;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastMotionY = (int) ev.getY();
				mLastMotionX = (int) ev.getX();
				break;
			case MotionEvent.ACTION_MOVE:
				// 若移动的X比Y大,则运行mGestureDetector事件
				if (Math.abs(ev.getX() - mLastMotionX) > Math.abs(ev.getY() - mLastMotionY)) {
					if (parent != null) {
						parent.requestDisallowInterceptTouchEvent(true);
					}
				}
				break;
		}
		return super.onInterceptTouchEvent(ev);
	}

}
