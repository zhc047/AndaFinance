package com.icitymobile.anda.ui.member;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.icitymobile.anda.service.AmsServiceCenter;
import com.icitymobile.anda.service.MemberServiceCenter;
import com.icitymobile.anda.ui.BaseActivity;
import com.icitymobile.anda.util.Const;
import com.icitymobile.anda.view.AndaProcessDialog;

public class ConfirmOldPhoneActivity extends BaseActivity{
	EditText mEtOldPhone;	//原手机号
	EditText mEtCode;		//验证码
	Button mBtnGetCode; 	//获取验证码
	Button mBtnNext;
	private User user;
	private String oldCode;	//旧验证码
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.member_change_phone);
		setTitle(R.string.member_confirm_old_phone);
				
		initView();
		initData();
	}
	
	// 初始化页面要素
	private void initView() {
		mEtOldPhone = (EditText)findViewById(R.id.phone_number);
		mEtCode = (EditText)findViewById(R.id.phone_code);
		mBtnGetCode = (Button)findViewById(R.id.get_code);
		mBtnNext= (Button)findViewById(R.id.member_next);
		enableCaptchaButton(true);
		
		// 获取验证码
		mBtnGetCode.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				//new CaptchaTask(user.getPhone()).execute();
				new CaptchaTask(mEtOldPhone.getText().toString().trim()).execute();
			}
		});
		
		//传送验证码至下一个class
        mBtnNext.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
				String oldPhone = mEtOldPhone.getText().toString().trim();
				if(StringKit.isEmpty(oldPhone)) {
					LibToast.show(R.string.member_hint_password_current);
					return;
				}
				oldCode = mEtCode.getText().toString().trim();
				if(StringKit.isEmpty(oldCode)) {
					LibToast.show(R.string.member_hint_code);
					return;
				}
				new CheckCodeTask(oldPhone,oldCode).execute();
        	}
        });
	}
	
	
	// 初始化页面数据
	private void initData() {
		try {
			if(CacheCenter.getCurrentUser() != null) {
				user = CacheCenter.getCurrentUser();				
				mEtOldPhone.setText(user.getPhone());
				//mEtOldPhone.setEnabled(false);
			}
		} catch (Exception e) {
			Logger.e("", "", e);
		}
		
	}
	
	/**
	 * 启用、禁用验证码按钮
	 * @param enable
	 */
	private void enableCaptchaButton(boolean enable) {
		if(enable) {
			mBtnGetCode.setEnabled(true);
			mBtnGetCode.setTextColor(getResources().getColor(R.color.red));
		} else {
			mBtnGetCode.setEnabled(false);
			mBtnGetCode.setTextColor(getResources().getColor(R.color.btn_white_disable));
		}
	}
	
	/**
	 * finish this class
	 * @param requestCode, resultCode,data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode){

		case Const.FINISH_ACTIVITY:
			if(resultCode ==RESULT_OK){
				this.finish();
			}
		}
	}
	
	/**
	 * 发送验证码
	 * @author Sherry
	 */
	class CaptchaTask extends AsyncTask<Void, Void, AndaResult<Void>> {
		String phone;
		AndaProcessDialog mAndaDialog;
		
		public CaptchaTask(String phone) {
			this.phone = phone;
			mAndaDialog = new AndaProcessDialog(ConfirmOldPhoneActivity.this);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mAndaDialog.show();
			enableCaptchaButton(false);
		}

		@Override
		protected AndaResult<Void> doInBackground(Void... params) {
			try {
				return AmsServiceCenter.getMessageCode(phone);
			} catch (Exception e) {
				Logger.e("", "", e);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(AndaResult<Void> result) {
			super.onPostExecute(result);
			mAndaDialog.dismiss();
			if(result != null)
				if( result.isSucceed()) {
					LibToast.show(R.string.member_code_sent);
					//启动倒计时
					new CountDownTimer(120000,1000) {
					
						@Override
						public void onTick(long millisUntilFinished) {
							String leaveSecond = "" + Math.round(millisUntilFinished/1000f);
							mBtnGetCode.setText(leaveSecond);
						}
					
						@Override
						public void onFinish() {
							mBtnGetCode.setText(R.string.member_captcha);
							enableCaptchaButton(true);
						}
					}.start();
				}
				else if(StringKit.isNotEmpty(result.getMessage())){
					LibToast.show(result.getMessage());
					
			} else {
				enableCaptchaButton(true);
				LibToast.show(R.string.member_code_sent_failure);
			}
		}
	}
	
	/**
	 * 验证验证码
	 * @author Sherry
	 */
	class CheckCodeTask extends AsyncTask<Void, Void, AndaResult<Void>> {
		String phone;
		String code;
		AndaProcessDialog mAndaDialog;
		
		public CheckCodeTask(String phone, String code) {
			this.phone = phone;
			this.code = code;
			mAndaDialog = new AndaProcessDialog(ConfirmOldPhoneActivity.this);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mAndaDialog.show();
		}

		@Override
		protected AndaResult<Void> doInBackground(Void... params) {
			try {
				return MemberServiceCenter.checkMessageCode(phone, code);
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
				if(result.isSucceed()) {
					Intent intent = new Intent(ConfirmOldPhoneActivity.this, UpdatePhoneActivity.class);
					intent.putExtra("OldCode", oldCode);
					startActivityForResult(intent,Const.FINISH_ACTIVITY);
				} else if(StringKit.isNotEmpty(result.getMessage())) {
					LibToast.show(result.getMessage());
				} else {
					LibToast.show(R.string.member_code_failure);
				}
			} else {
				LibToast.show(R.string.member_register_network);
			}
		
	    }
	}
}
