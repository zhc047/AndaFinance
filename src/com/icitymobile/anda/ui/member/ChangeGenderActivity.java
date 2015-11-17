package com.icitymobile.anda.ui.member;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hualong.framework.kit.StringKit;
import com.hualong.framework.log.Logger;
import com.hualong.framework.widget.LibToast;
import com.icitymobile.anda.R;
import com.icitymobile.anda.bean.AndaResult;
import com.icitymobile.anda.bean.User;
import com.icitymobile.anda.cache.CacheCenter;
import com.icitymobile.anda.service.MemberServiceCenter;
import com.icitymobile.anda.ui.BaseActivity;
import com.icitymobile.anda.view.AndaProcessDialog;

public class ChangeGenderActivity extends BaseActivity{
	
	TextView mTvGender;     //性别	
	Button mBtnFinish;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.member_change_gender);
		setTitle(R.string.member_detail_change_gender);
				
		initView();
		
		initData();
	}
	
	// 初始化页面要素
	private void initView() {
		mTvGender = (TextView)findViewById(R.id.member_gender_arrow);
		mBtnFinish = (Button)findViewById(R.id.member_finish);
		//choose gender
		mTvGender.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				final String[] items = getResources().getStringArray(R.array.gender_array_key);
				new AlertDialog.Builder(ChangeGenderActivity.this)
					.setItems(items, new DialogInterface.OnClickListener() {
							
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String key = items[which];
						mTvGender.setText(key);
					}
				}).show();
			}
		});
						
		//提交用户性别
        mBtnFinish.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
				String gender = mTvGender.getText().toString().trim();
				if(StringKit.isEmpty(gender)) {
					LibToast.show(R.string.member_hint_gender);
					return;
				}
				if(gender.equals("女")){
					user.setGender("0");
				}else if (gender.equals("男")){
					user.setGender("1");
				}
				Intent intent = new Intent();
				intent.putExtra("User", user);
				setResult(RESULT_OK, intent);
				finish();
        	}
        });
	}
	
	// 初始化页面数据
	private void initData() {
		try {
			if(CacheCenter.getCurrentUser() != null) {
				user = CacheCenter.getCurrentUser();
				mTvGender.setText(user.getGender());
			}
		} catch (Exception e) {
			Logger.e("", "", e);
		}
		
	}
}
