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
		initView();// �����ļ���ʼ��
	}

	private void initView() {
		mScrollLayout = (ScrollLayout) findViewById(R.id.ScrollLayout);// ʵ����ViewGroup
		pointLayout = (LinearLayout) findViewById(R.id.llayout);
		count = mScrollLayout.getChildCount();// ȡ��Group����Ŀ
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
		if (position < 0 || position > count - 1 || currentItem == position) {// Խ���λ�ò���ʱ
			return;
		}
		imgs[currentItem].setEnabled(true);// ���ÿؼ�����
		imgs[position].setEnabled(false);// ���ÿؼ�������
		currentItem = position;
	}

}
