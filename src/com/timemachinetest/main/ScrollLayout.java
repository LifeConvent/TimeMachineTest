package com.timemachinetest.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

@SuppressLint("NewApi")
public class ScrollLayout extends ViewGroup {

	private static final String TAG = "ScrollLayout";

	private VelocityTracker mVelocityTracker; // 判断甩动手势，记录当前屏幕的下标，取值范围是：0~getChildCount()-1

	private static final int SNAP_VELOCITY = 600; // 定义最大可滑动距离

	private Scroller mScroller; // 滑动控制,速度追踪器，主要是为了通过当前滑动速度来判断当前滑动是否为fling（快速滑动）

	private int mCurScreen;

	private int mDefaultScreen = 0;// touch状态 0表示静止，1表示滑动

	private float mLastMotionX;// 上次的位置

	private Activity activity = null;

	private OnViewChangeListener mOnViewChangeListener;// OnViewChangeListener接口

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public ScrollLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		this.activity = (Activity) context;
	}

	public ScrollLayout(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public ScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public ScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	@SuppressWarnings("unused")
	private void init(Context context) {
		mCurScreen = mDefaultScreen;// 当前屏幕 默认屏幕？？？
		mScroller = new Scroller(context);
	}

	protected void onLayout(boolean changed, int l, int t, int r, int b) {// 自行指定排列的方向
		// TODO Auto-generated method stub
		if (changed) {
			int childLeft = 0;
			final int childCount = getChildCount();// 返回在组中的编号
			for (int i = 0; i < childCount; i++) {// 横向的布局
				final View childView = getChildAt(i);// 从组中取出指定位置的view
				if (childView.getVisibility() != View.GONE) {// 不可见的属性
					final int childWidth = childView.getMeasuredWidth();
					childView.layout(childLeft, 0, childLeft + childWidth,
							childView.getMeasuredHeight());
					childLeft += childWidth;
				}
			}
		}
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = MeasureSpec.getSize(widthMeasureSpec);// 低16位specSize
		final int count = getChildCount();// 屏幕范围取值
		for (int i = 0; i < count; i++) {// 计算好每页的大小规格
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		scrollTo(mCurScreen * width, 0);// 平滑滚动到页面指定位置,x轴页数*页宽，y轴上的移动距离为零
	}

	// 根据当前位置滑动到指定的相应界面
	public void snapToDestination() {
		final int screenWidth = getWidth();
		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;// 取整得目标视图
		snapToScreen(destScreen);
	}

	// snapToScreen 方法描述：滑动到到第whichScreen（从0开始）个界面，有过渡效果
	public void snapToScreen(int whichScreen) {
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));// 滑动的范围限制
																				// 0~getChildCount()-1
		if (getScrollX() != (whichScreen * getWidth())) {// ----------------------?????
			final int delta = whichScreen * getWidth() - getScrollX();// 需要移动的距离
			mScroller.startScroll(getScrollX(), 0, delta, 0,
					Math.abs(delta) * 2);
			mCurScreen = whichScreen;// 当前屏幕
			invalidate();// 请求重绘View树，即draw()过程，只会绘制调用者本身
			if (mOnViewChangeListener != null) {
				mOnViewChangeListener.OnViewChange(mCurScreen);// 调用接口方法
			}
		}
	}

	public void computeScroll() {// 主要功能是计算拖动的位移量、更新背景、设置要显示的屏幕,重写computeScroll()的原因
		// 调用startScroll()是不会有滚动效果的，只有在computeScroll()获取滚动情况，做出滚动的响应,computeScroll在父控件执
		// 行drawChild时，会调用这个方法
		if (mScroller.computeScrollOffset()) {// Call this when you want to know
												// the new location. If it
												// returns true, the animation
												// is not yet finished.
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());// Returns the
																	// current X
																	// offset in
																	// the
																	// scroll
			postInvalidate();// 线程中界面刷新
		}
	}

	@SuppressWarnings("static-access")
	@SuppressLint({ "ClickableViewAccessibility", "Recycle" })
	public boolean onTouchEvent(MotionEvent event) {// MontionEvent整型定值常量
		final int action = event.getAction();// 返回屏幕操作情况的索引
		final float x = event.getX();
		switch (action) {
		case MotionEvent.ACTION_DOWN:// 屏幕被按下
			Log.i("", "onTouchEvent ACTION_DOMN");
			if (mVelocityTracker == null) {// 记录当前甩动手势
				mVelocityTracker = mVelocityTracker.obtain();
				mVelocityTracker.addMovement(event);// 将Motion
													// event加入到VelocityTracker类实例中
			}
			if (!mScroller.isFinished()) {// 判断滚动是否结束
				mScroller.abortAnimation();// 终止滚动
			}
			mLastMotionX = x;// 重置上次触发动作的位置为x
			break;
		case MotionEvent.ACTION_MOVE:// 在屏幕中拖动
			int deltaX = (int) (mLastMotionX - x);// 测算移动距离转为目标值
			if (IsCanMove(deltaX)) {
				if (mVelocityTracker != null) {// 有甩动手势试
					mVelocityTracker.addMovement(event);// 将Motion
														// event加入到VelocityTracker类实例中
				}
				mLastMotionX = x;
				scrollBy(deltaX, 0);// 移动位置
			}
			break;

		case MotionEvent.ACTION_UP:
			int velocityX = 0;// x方向上的滑动速度
			if (mVelocityTracker != null) {// 甩动手势不为空
				mVelocityTracker.addMovement(event);
				mVelocityTracker.computeCurrentVelocity(1000);// 初始化速率的单位为1000
				velocityX = (int) mVelocityTracker.getXVelocity();// x方向上的速度
			}
			if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
				Log.e(TAG, "snap left");
				snapToScreen(mCurScreen - 1);// 左滑编号减一
			}
			if (velocityX < -SNAP_VELOCITY && mCurScreen < getChildCount() - 1) {
				Log.e(TAG, "snap right");
				snapToScreen(mCurScreen + 1);// 右滑编号加一
			} else if (mCurScreen == 6 || velocityX < -SNAP_VELOCITY + 400) {
				Intent intent = new Intent(activity, ListMain.class);
				activity.startActivity(intent);
				activity.finish();
				snapToScreen(mCurScreen + 1);
			} else {
				snapToDestination();
			}
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			break;
		}
		return true;
	}

	private boolean IsCanMove(int deltaX) {// 判断是否可以移动
		if (getScrollX() <= 0 && deltaX < 0) {// 获取目标值小于零
			return false;
		}
		if (getScrollX() >= (getChildCount() - 1) * getWidth() && deltaX > 0) {// 所求越界
			return false;
		}
		return true;
	}

	public void SetOnViewChangeListener(OnViewChangeListener listener) {// 设置监听事件对于ViewChange
		mOnViewChangeListener = listener;
	}

}
