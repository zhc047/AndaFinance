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
import com.icitymobile.anda.service.MemberServiceCenter;
import com.icitymobile.anda.ui.BaseActivity;
import com.icitymobile.anda.util.Const;
import com.icitymobile.anda.view.AndaProcessDialog;

public class ResetPasswordPhoneActivity extends BaseActivity{
	EditText mEtPhone;	//手机号
	Button mBtnNext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.member_reset_password_phone);
		setTitle(R.string.member_forgot_password);
				
		initView();
		
	}
	
	// 初始化页面要素
	private void initView() {
		mEtPhone= (EditText)findViewById(R.id.reset_password_phone);
		mBtnNext = (Button)findViewById(R.id.reset_password_next);
		//提交用户注册手机号
        mBtnNext.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
				String phone = mEtPhone.getText().toString().trim();
				if(StringKit.isEmpty(phone)) {
					LibToast.show(R.string.member_hint_phone);
					return;
				}
				new CheckPhoneTask(phone).execute();

        	}
        });
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
				finish();
			}
		}
	}
	
	/**
	 * 手机号码验证
	 * @author sherry
	 */
	class CheckPhoneTask extends AsyncTask<Void, Void, AndaResult<Void>> {
		
		String phone;
		AndaProcessDialog mAndaDialog;
		
		public CheckPhoneTask(String phone) {
			this.phone = phone;		
			mAndaDialog = new AndaProcessDialog(ResetPasswordPhoneActivity.this);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mAndaDialog.show();
		}

		@Override
		protected AndaResult<Void> doInBackground(Void... params) {
			try {
				return MemberServiceCenter.checkPhone(phone);
			} catch (Exception e) {
				Logger.e("", "", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(AndaResult<Void> result) {
			super.onPostExecute(result);
			mAndaDialog.dismiss();
			if(result != null) {
				//手机号被注册
				if(result.isSucceed()) {
					Intent intent = new Intent(getApplicationContext(), RePasswordConfirmPhoneActivity.class);
					intent.putExtra("phone_number", phone);
					startActivityForResult(intent,Const.FINISH_ACTIVITY);	
				} else {
					LibToast.show(R.string.member_phone_not_registed);
				}
			} else {
				LibToast.show(R.string.member_register_network);
			}
		}
	}
}
