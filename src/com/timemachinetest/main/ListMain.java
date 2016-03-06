package com.timemachinetest.main;

import android.app.Activity;
import android.os.Bundle;

import com.example.timemachinetest.R;
import com.timemachinetest.timeshaft.view.XListView.IXListViewListener;

public class ListMain extends Activity implements IXListViewListener {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.indexlist);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

}
