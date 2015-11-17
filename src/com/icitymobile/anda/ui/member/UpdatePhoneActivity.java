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
import com.icitymobile.anda.view.AndaProcessDialog;

public class UpdatePhoneActivity extends BaseActivity{
	EditText mEtNewPhone;	//新手机号
	EditText mEtCode;		//验证码
	Button mBtnGetCode; 	//获取验证码
	Button mBtnFinish;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.member_change_phone);
		setTitle(R.string.member_update_phone);
				
		initView();
	}
	
	// 初始化页面要素
	private void initView() {
		mEtNewPhone = (EditText)findViewById(R.id.phone_number);
		mEtCode = (EditText)findViewById(R.id.phone_code);
		mBtnGetCode = (Button)findViewById(R.id.get_code);
		mBtnFinish= (Button)findViewById(R.id.member_next);
		enableCaptchaButton(true);
		
		// 获取验证码
		mBtnGetCode.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				new CaptchaTask(mEtNewPhone.getText().toString().trim()).execute();
			}
		});
		
		//提交用户新手机号码
        mBtnFinish.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
				String newPhone = mEtNewPhone.getText().toString().trim();
				if(StringKit.isEmpty(newPhone)) {
					LibToast.show(R.string.member_hint_password_current);
					return;
				}
			
				String newCode = mEtCode.getText().toString().trim();
				if(StringKit.isEmpty(newCode)) {
					LibToast.show(R.string.member_hint_code);
					return;
				}
				if(CacheCenter.getCurrentUser() != null) {				
					user = CacheCenter.getCurrentUser();
				}
			    String user_token = user.getToken();
			    String oldPhone = user.getPhone();
			    Intent intent = getIntent();
			    String oldCode = intent.getStringExtra("OldCode");
			    
				new CheckCodeTask(user_token, oldPhone, newPhone,oldCode,newCode).execute();
	
        	}
        });
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
	 * 发送验证码
	 * @author Sherry
	 */
	class CaptchaTask extends AsyncTask<Void, Void, AndaResult<Void>> {
		String phone;
		AndaProcessDialog mAndaDialog;
		
		public CaptchaTask(String phone) {
			this.phone = phone;
			mAndaDialog = new AndaProcessDialog(UpdatePhoneActivity.this);
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
		String user_token;
		String oldPhone;
		String newPhone;
		String oldCode;
		String newCode;
		
		AndaProcessDialog mAndaDialog;
		
		public CheckCodeTask(String user_token, String oldPhone, String newPhone, String oldCode, String newCode) {
			this.newPhone = newPhone;
			this.newCode = newCode;
			this.oldPhone = oldPhone;
			this.newPhone = newPhone;
			this.user_token = user_token;
			mAndaDialog = new AndaProcessDialog(UpdatePhoneActivity.this);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mAndaDialog.show();
		}

		@Override
		protected AndaResult<Void> doInBackground(Void... params) {
			try {
				return MemberServiceCenter.checkMessageCode(newPhone, newCode);
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
					new ChangePhoneTask(user_token, oldPhone, newPhone, oldCode, newCode).execute();
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
	/**
	 * 修改会员资料
	 * @author Sherry
	 */
	class ChangePhoneTask extends AsyncTask<Void, Void, AndaResult<Void>> {
		
		String user_token;
		String oldPhone;
		String newPhone;
		String oldCode;
		String newCode;
		AndaProcessDialog mAndaDialog;
		
		public ChangePhoneTask(String user_token, String oldPhone, String newPhone, String oldCode, String newCode) {
			this.user_token = user_token;
			this.oldPhone = oldPhone;
			this.newPhone = newPhone;
			this.oldCode = oldCode;
			this.newCode = newCode;
			mAndaDialog = new AndaProcessDialog(UpdatePhoneActivity.this);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mAndaDialog.show();
		}

		
		@Override
		protected AndaResult<Void> doInBackground(Void... params) {
			try {
				return MemberServiceCenter.changePhone(user_token, oldPhone, oldCode,newPhone,newCode);
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
				LibToast.show(result.getMessage());
				//更新缓存
				user.setPhone(newPhone);
				CacheCenter.cacheCurrentUser(user);
				setResult(RESULT_OK);
				finish();
				//setResult(RESULT_OK);
			} else {
				LibToast.show("资料更新失败");
			}
		}
	}
}
