package com.timemachinetest.main;

import com.example.timemachinetest.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Welcome extends Activity implements OnViewChangeListener {

	private ScrollLayout mScrollLayout;// ViewGroup
	private LinearLayout pointLayout;
	private int count;
	private ImageView[] imgs;
	private int currentItem;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		initView();// 布局文件初始化
	}

	private void initView() {
		mScrollLayout = (ScrollLayout) findViewById(R.id.ScrollLayout);// 实例化ViewGroup
		pointLayout = (LinearLayout) findViewById(R.id.llayout);
		count = mScrollLayout.getChildCount();// 取得Group的数目
		mScrollLayout.setActivity(this);
		imgs = new ImageView[count];
		for (int i = 0; i < count; i++) {
			imgs[i] = (ImageView) pointLayout.getChildAt(i);
			imgs[i].setEnabled(true);
			imgs[i].setTag(i);
		}
		currentItem = 0;
		imgs[currentItem].setEnabled(false);
		mScrollLayout.SetOnViewChangeListener(this);
	}

	@Override
	public void OnViewChange(int position) {
		// TODO Auto-generated method stub
		setCurrentPoint(position);
	}

	private void setCurrentPoint(int position) {
		if (position < 0 || position > count - 1 || currentItem == position) {// 越界或位置不变时
			return;
		}
		imgs[currentItem].setEnabled(true);// 设置控件可用
		imgs[position].setEnabled(false);// 设置控件不可用
		currentItem = position;
	}

}
