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

	private VelocityTracker mVelocityTracker; // �ж�˦�����ƣ���¼��ǰ��Ļ���±꣬ȡֵ��Χ�ǣ�0~getChildCount()-1

	private static final int SNAP_VELOCITY = 600; // �������ɻ�������

	private Scroller mScroller; // ��������,�ٶ�׷��������Ҫ��Ϊ��ͨ����ǰ�����ٶ����жϵ�ǰ�����Ƿ�Ϊfling�����ٻ�����

	private int mCurScreen;

	private int mDefaultScreen = 0;// touch״̬ 0��ʾ��ֹ��1��ʾ����

	private float mLastMotionX;// �ϴε�λ��

	private Activity activity = null;

	private OnViewChangeListener mOnViewChangeListener;// OnViewChangeListener�ӿ�

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
		mCurScreen = mDefaultScreen;// ��ǰ��Ļ Ĭ����Ļ������
		mScroller = new Scroller(context);
	}

	protected void onLayout(boolean changed, int l, int t, int r, int b) {// ����ָ�����еķ���
		// TODO Auto-generated method stub
		if (changed) {
			int childLeft = 0;
			final int childCount = getChildCount();// ���������еı��
			for (int i = 0; i < childCount; i++) {// ����Ĳ���
				final View childView = getChildAt(i);// ������ȡ��ָ��λ�õ�view
				if (childView.getVisibility() != View.GONE) {// ���ɼ�������
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
		final int width = MeasureSpec.getSize(widthMeasureSpec);// ��16λspecSize
		final int count = getChildCount();// ��Ļ��Χȡֵ
		for (int i = 0; i < count; i++) {// �����ÿҳ�Ĵ�С���
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		scrollTo(mCurScreen * width, 0);// ƽ��������ҳ��ָ��λ��,x��ҳ��*ҳ��y���ϵ��ƶ�����Ϊ��
	}

	// ���ݵ�ǰλ�û�����ָ������Ӧ����
	public void snapToDestination() {
		final int screenWidth = getWidth();
		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;// ȡ����Ŀ����ͼ
		snapToScreen(destScreen);
	}

	// snapToScreen ��������������������whichScreen����0��ʼ�������棬�й���Ч��
	public void snapToScreen(int whichScreen) {
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));// �����ķ�Χ����
																				// 0~getChildCount()-1
		if (getScrollX() != (whichScreen * getWidth())) {// ----------------------?????
			final int delta = whichScreen * getWidth() - getScrollX();// ��Ҫ�ƶ��ľ���
			mScroller.startScroll(getScrollX(), 0, delta, 0,
					Math.abs(delta) * 2);
			mCurScreen = whichScreen;// ��ǰ��Ļ
			invalidate();// �����ػ�View������draw()���̣�ֻ����Ƶ����߱���
			if (mOnViewChangeListener != null) {
				mOnViewChangeListener.OnViewChange(mCurScreen);// ���ýӿڷ���
			}
		}
	}

	public void computeScroll() {// ��Ҫ�����Ǽ����϶���λ���������±���������Ҫ��ʾ����Ļ,��дcomputeScroll()��ԭ��
		// ����startScroll()�ǲ����й���Ч���ģ�ֻ����computeScroll()��ȡ���������������������Ӧ,computeScroll�ڸ��ؼ�ִ
		// ��drawChildʱ��������������
		if (mScroller.computeScrollOffset()) {// Call this when you want to know
												// the new location. If it
												// returns true, the animation
												// is not yet finished.
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());// Returns the
																	// current X
																	// offset in
																	// the
																	// scroll
			postInvalidate();// �߳��н���ˢ��
		}
	}

	@SuppressWarnings("static-access")
	@SuppressLint({ "ClickableViewAccessibility", "Recycle" })
	public boolean onTouchEvent(MotionEvent event) {// MontionEvent���Ͷ�ֵ����
		final int action = event.getAction();// ������Ļ�������������
		final float x = event.getX();
		switch (action) {
		case MotionEvent.ACTION_DOWN:// ��Ļ������
			Log.i("", "onTouchEvent ACTION_DOMN");
			if (mVelocityTracker == null) {// ��¼��ǰ˦������
				mVelocityTracker = mVelocityTracker.obtain();
				mVelocityTracker.addMovement(event);// ��Motion
													// event���뵽VelocityTracker��ʵ����
			}
			if (!mScroller.isFinished()) {// �жϹ����Ƿ����
				mScroller.abortAnimation();// ��ֹ����
			}
			mLastMotionX = x;// �����ϴδ���������λ��Ϊx
			break;
		case MotionEvent.ACTION_MOVE:// ����Ļ���϶�
			int deltaX = (int) (mLastMotionX - x);// �����ƶ�����תΪĿ��ֵ
			if (IsCanMove(deltaX)) {
				if (mVelocityTracker != null) {// ��˦��������
					mVelocityTracker.addMovement(event);// ��Motion
														// event���뵽VelocityTracker��ʵ����
				}
				mLastMotionX = x;
				scrollBy(deltaX, 0);// �ƶ�λ��
			}
			break;

		case MotionEvent.ACTION_UP:
			int velocityX = 0;// x�����ϵĻ����ٶ�
			if (mVelocityTracker != null) {// ˦�����Ʋ�Ϊ��
				mVelocityTracker.addMovement(event);
				mVelocityTracker.computeCurrentVelocity(1000);// ��ʼ�����ʵĵ�λΪ1000
				velocityX = (int) mVelocityTracker.getXVelocity();// x�����ϵ��ٶ�
			}
			if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
				Log.e(TAG, "snap left");
				snapToScreen(mCurScreen - 1);// �󻬱�ż�һ
			}
			if (velocityX < -SNAP_VELOCITY && mCurScreen < getChildCount() - 1) {
				Log.e(TAG, "snap right");
				snapToScreen(mCurScreen + 1);// �һ���ż�һ
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

	private boolean IsCanMove(int deltaX) {// �ж��Ƿ�����ƶ�
		if (getScrollX() <= 0 && deltaX < 0) {// ��ȡĿ��ֵС����
			return false;
		}
		if (getScrollX() >= (getChildCount() - 1) * getWidth() && deltaX > 0) {// ����Խ��
			return false;
		}
		return true;
	}

	public void SetOnViewChangeListener(OnViewChangeListener listener) {// ���ü����¼�����ViewChange
		mOnViewChangeListener = listener;
	}

}
