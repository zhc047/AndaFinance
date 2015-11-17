package com.icitymobile.anda.ui.member;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.hualong.framework.kit.StringKit;
import com.hualong.framework.log.Logger;
import com.hualong.framework.widget.LibToast;
import com.icitymobile.anda.R;
import com.icitymobile.anda.bean.AndaResult;
import com.icitymobile.anda.bean.PCItem;
import com.icitymobile.anda.bean.User;
import com.icitymobile.anda.service.AmsServiceCenter;
import com.icitymobile.anda.service.MemberServiceCenter;
import com.icitymobile.anda.ui.BaseActivity;
import com.icitymobile.anda.ui.WebBrowserActivity;
import com.icitymobile.anda.ui.member.RegisterDetailActivity.CaptchaTask;
import com.icitymobile.anda.ui.member.RegisterDetailActivity.CheckCodeTask;
import com.icitymobile.anda.ui.member.RegisterDetailActivity.CheckUsernameTask;
import com.icitymobile.anda.util.Const;
import com.icitymobile.anda.view.AndaProcessDialog;

public class ResetPasswordActivity extends BaseActivity{
	
	private String phone;
	private User user;
	
	EditText mEtCode;       //验证码
	EditText mEtPassword;	//密码
	EditText mEtRePassword;	//重复密码
	
	Button mBtnGetCode;	    //获取验证码
	Button mBtnFinish;        //下一步
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.member_reset_password);
		setTitle(R.string.member_reset_password);
		Intent intent = getIntent();
		phone = intent.getStringExtra("phone_number").trim();
	
		initView();
		user = new User();
	}
	
	// 初始化页面要素
	private void initView() {

		mEtCode = (EditText)findViewById(R.id.reset_password_code);

		mEtPassword = (EditText)findViewById(R.id.new_password);
		mEtRePassword = (EditText)findViewById(R.id.confirm_password);		
		mBtnGetCode = (Button)findViewById(R.id.get_code);
		mBtnFinish = (Button)findViewById(R.id.reset_password_finish);
		enableCaptchaButton(true);
		
        
		// 获取验证码
		mBtnGetCode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				new CaptchaTask(phone).execute();
			}
		});
		
		// 提交用户信息
		mBtnFinish.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				// 验证码
				String code = mEtCode.getText().toString().trim();
				if(StringKit.isEmpty(code)) {
					LibToast.show(R.string.member_hint_code);
					return;
				}					
	
				// 密码
				String password = mEtPassword.getText().toString().trim();
				if(StringKit.isEmpty(password)) {
					LibToast.show(R.string.member_hint_password);
					return;
				}
				// 重复密码
				String rePassword = mEtRePassword.getText().toString().trim();
				if(StringKit.isEmpty(rePassword)||!rePassword.equals(password)) {
					LibToast.show(R.string.member_hint_rePassword);
					return;
				}	

				user.setPassword(password);
				user.setCode(code);
			
				new CheckCodeTask(phone, code,password).execute();	
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
			mAndaDialog = new AndaProcessDialog(ResetPasswordActivity.this);
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
		String password;
		AndaProcessDialog mAndaDialog;
		
		public CheckCodeTask(String phone, String code, String password) {
			this.phone = phone;
			this.code = code;
			this.password = password;
			mAndaDialog = new AndaProcessDialog(ResetPasswordActivity.this);
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
					new ResetPasswordTask(phone, code, password).execute();
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
	 * 验证用户名
	 * @author Sherry
	 */
	class ResetPasswordTask extends AsyncTask<Void, Void, AndaResult<Void>> {
		String phone;
		String code;
		String password;
		AndaProcessDialog mAndaDialog;
		
		public ResetPasswordTask(String phone, String code, String password) {
			this.phone = phone;
			this.code = code;
			this.password = password;
			mAndaDialog = new AndaProcessDialog(ResetPasswordActivity.this);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mAndaDialog.show();
		}

		@Override
		protected AndaResult<Void> doInBackground(Void... params) {
			try {
				return MemberServiceCenter.resetPassword(phone, code, password);
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
					setResult(RESULT_OK);
					finish();
				} else if(StringKit.isNotEmpty(result.getMessage())) {
					LibToast.show(result.getMessage());
				} else {
					LibToast.show(R.string.member_username_failure);
				}
			} else {
				LibToast.show(R.string.member_register_network);
			}
		
	    }
	}
}
