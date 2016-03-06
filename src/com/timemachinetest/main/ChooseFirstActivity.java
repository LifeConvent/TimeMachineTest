package com.timemachinetest.main;

import com.example.timemachinetest.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

public class ChooseFirstActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//首次登陆的界面选择
		super.onCreate(savedInstanceState);
		setContentView(R.layout.empty_page);
		if (getNum()) {
			Log.v("start", "0");
			Intent intent = new Intent(ChooseFirstActivity.this,Welcome.class);//指定信息
			startActivity(intent);//系统通过antion跳转到对应的Activity
			finish();
		} else {
			Intent intent = new Intent(ChooseFirstActivity.this,ListMain.class);
			startActivity(intent);
			finish();
		}
	}

	@SuppressLint("CommitPrefEdits")
	public boolean getNum() {//首次进入时初始化为0，之后随操作递增
		SharedPreferences sharedata = getSharedPreferences("open_num", 0); // 实例化SharedPreferences对象
		int num = sharedata.getInt("int_num", 0);// 使用getInt方法取得num的值，0为默认值
		Editor editor = sharedata.edit(); // 实例化sharedata.edit对象
		editor.putInt("int_num", ++num);// 使用putInt方法保存数据
		editor.commit();// 提交当前数据
		if (--num == 0)
			return true;
		else
			return false;
	}
}
