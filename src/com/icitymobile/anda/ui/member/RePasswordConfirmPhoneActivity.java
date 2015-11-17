package com.icitymobile.anda.ui.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.hualong.framework.log.Logger;
import com.icitymobile.anda.R;
import com.icitymobile.anda.cache.CacheCenter;
import com.icitymobile.anda.ui.BaseActivity;
import com.icitymobile.anda.util.Const;

public class RePasswordConfirmPhoneActivity extends BaseActivity{
	EditText mEtPhone;	//手机号
	Button mBtnReset;
	private String phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.member_reset_password_confirm_phone);
		setTitle(R.string.member_reset_password_confirm_phone);
				
		initView();
		initData();
		
	}
	
	// 初始化页面要素
	private void initView() {
		mEtPhone= (EditText)findViewById(R.id.reset_password_confirm_phone);
		mBtnReset = (Button)findViewById(R.id.reset_password);

        mBtnReset.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		Intent intent = new Intent(RePasswordConfirmPhoneActivity.this, ResetPasswordActivity.class);
        		intent.putExtra("phone_number",phone);
        		startActivityForResult(intent, Const.FINISH_ACTIVITY);
        	}
        });
	}

	
	
	// 初始化页面数据
	private void initData() {		
		Intent intent = getIntent();
		phone = intent.getStringExtra("phone_number");
		mEtPhone.setText(phone);
		mEtPhone.setEnabled(false);

	}
	
	/**
	 * finish this activity
	 * @param requestCode, resultCode,data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode){
		case Const.FINISH_ACTIVITY:
			if(resultCode == RESULT_OK){
				setResult(RESULT_OK);
				finish();
			}
		}
	}
}
