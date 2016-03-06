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
		//�״ε�½�Ľ���ѡ��
		super.onCreate(savedInstanceState);
		setContentView(R.layout.empty_page);
		if (getNum()) {
			Log.v("start", "0");
			Intent intent = new Intent(ChooseFirstActivity.this,Welcome.class);//ָ����Ϣ
			startActivity(intent);//ϵͳͨ��antion��ת����Ӧ��Activity
			finish();
		} else {
			Intent intent = new Intent(ChooseFirstActivity.this,ListMain.class);
			startActivity(intent);
			finish();
		}
	}

	@SuppressLint("CommitPrefEdits")
	public boolean getNum() {//�״ν���ʱ��ʼ��Ϊ0��֮�����������
		SharedPreferences sharedata = getSharedPreferences("open_num", 0); // ʵ����SharedPreferences����
		int num = sharedata.getInt("int_num", 0);// ʹ��getInt����ȡ��num��ֵ��0ΪĬ��ֵ
		Editor editor = sharedata.edit(); // ʵ����sharedata.edit����
		editor.putInt("int_num", ++num);// ʹ��putInt������������
		editor.commit();// �ύ��ǰ����
		if (--num == 0)
			return true;
		else
			return false;
	}
}
