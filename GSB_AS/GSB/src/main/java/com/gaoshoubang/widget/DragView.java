package com.gaoshoubang.widget;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.gaoshoubang.util.DisplayUtil;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * 首页可拖动的view
 *
 * @author Cisco
 */
public class DragView extends View {
	private Context context;

	private int screenWidth;
	private int screenHeight;
	private int lastX;
	private int lastY;
	private int left = 0, top = 0, right = 0, bottom = 0;
	private float viewX = -1;
	private float viewY = -1;

	private OnDrayViewClick onDrayViewClick;
	private int x = 0;
	private int y = 0;
	private SharedPreferences sp;

	private String saveXKey = "viewX";
	private String saveYKey = "viewY";
	private boolean isAnimator = true;

	private AnimatorSet animatorSet;
	private int time = 1000;

	public DragView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public DragView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		init();
	}

	private void init() {
		sp = context.getSharedPreferences("dragViewXY", Context.MODE_PRIVATE);
		screenWidth = DisplayUtil.getScreenWidth((Activity) context);
		screenHeight = DisplayUtil.getScreenHeight((Activity) context) - DisplayUtil.dip2px(context, 70);
		dragViewAnimator();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setDragView();
	}

	// 设置保存的位置
	private void setDragView() {
		RelativeLayout.LayoutParams viewLp = (RelativeLayout.LayoutParams) getLayoutParams();
		int spViewX = sp.getInt(saveXKey, -1);
		int spViewY = sp.getInt(saveYKey, -1);
		if (spViewX == -1) {
			if (viewX != -1 || viewY != -1) {
				viewLp.setMargins((int) viewX, (int) viewY, 0, 0);
			}
			else {
				viewLp.setMargins(30, screenHeight / 5 * 4, 0, 0);
			}
			requestLayout();
		}
		else {
			viewLp.setMargins(spViewX, spViewY, 0, 0);
			requestLayout();
		}
	}

	// 动画
	private void dragViewAnimator() {
		animatorSet = new AnimatorSet();
		animatorSet.playTogether(ObjectAnimator.ofFloat(this, "translationX", 0, 10, -10, 5, -5, 0));
		animatorSet.setDuration(1000);
		dragViewHandler.sendEmptyMessage(0);
	}

	Handler dragViewHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (animatorSet == null) {
				return;
			}
			animatorSet.start();
			dragViewHandler.sendMessageDelayed(new Message(), time);
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		getParent().requestDisallowInterceptTouchEvent(true);
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				x = (int) event.getRawX();
				y = (int) event.getRawY();
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				break;

			case MotionEvent.ACTION_MOVE:
				int dx = (int) event.getRawX() - lastX;
				int dy = (int) event.getRawY() - lastY;

				left = getLeft() + dx;
				top = getTop() + dy;
				right = getRight() + dx;
				bottom = getBottom() + dy;
				if (left < 0) {
					left = 0;
					right = left + getWidth();
				}
				if (right > screenWidth) {
					right = screenWidth;
					left = right - getWidth();
				}
				if (top < 0) {
					top = 0;
					bottom = top + getHeight();
				}
				if (bottom > screenHeight) {
					bottom = screenHeight;
					top = bottom - getHeight();
				}

				layout(left, top, right, bottom);
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();

				viewX = getX();
				viewY = getY();
				break;

			case MotionEvent.ACTION_UP: // 手指离开屏幕的一瞬间
				SharedPreferences.Editor editor = sp.edit();
				editor.putInt(saveXKey, (int) viewX);
				editor.putInt(saveYKey, (int) viewY);
				editor.commit();
				RelativeLayout.LayoutParams viewLp = (RelativeLayout.LayoutParams) getLayoutParams();
				viewLp.setMargins((int) viewX, (int) viewY, 0, 0);
				requestLayout();
				if (Math.abs(x - event.getRawX()) < 6 && Math.abs(y - event.getRawY()) < 6) {
					onDrayViewClick.onClick();
				}
				break;
		}
		return true;
	}

	public void setSaveKey(String saveXKey, String saveYKey) {
		this.saveXKey = saveXKey;
		this.saveYKey = saveYKey;
	}

	public void setViewXY(float x, float y) {
		this.viewX = x;
		this.viewY = y;
	}

	public boolean isAnimator() {
		return isAnimator;
	}

	public void setViewAnimator(boolean isAnimator) {
		if (!isAnimator) {
			dragViewHandler.removeMessages(0);
			this.clearAnimation();
		}
		this.isAnimator = isAnimator;
	}

	public void setOnDrayViewClick(OnDrayViewClick onDrayViewClick) {
		this.onDrayViewClick = onDrayViewClick;
	}

	public interface OnDrayViewClick {
		void onClick();
	}
}
