package com.icitymobile.anda.ui.member;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class ChangeIDcardActivity extends BaseActivity {
	EditText mEtID;		//身份证
	Button mBtnFinish;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.member_modify_name_id);
		setTitle(R.string.member_detail_change_id);
				
		initView();
		
		initData();
	}
	
	// 初始化页面要素
	private void initView() {
		mEtID = (EditText)findViewById(R.id.member_name_id);
		mBtnFinish = (Button)findViewById(R.id.member_modify_name_id_finished);
		//提交用户身份证
        mBtnFinish.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
				String idCard = mEtID.getText().toString().trim();
				if(StringKit.isEmpty(idCard)) {
					LibToast.show(R.string.member_hint_IDCard);
					return;
				}
				user.setIDCardNumber(idCard);
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
				mEtID.setText(user.getIDCardNumber());
			}
		} catch (Exception e) {
			Logger.e("", "", e);
		}
		
	}
}
