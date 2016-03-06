package com.timemachinetest.timeshaft.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

public class XListView extends ListView implements OnScrollListener{

	public XListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public XListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public interface IXListViewListener {
		public void onRefresh();

		public void onLoadMore();
	}

}
