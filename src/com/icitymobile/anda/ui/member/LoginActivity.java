package com.icitymobile.anda.ui.member;

import com.hualong.framework.kit.StringKit;
import com.hualong.framework.log.Logger;
import com.hualong.framework.widget.LibToast;
import com.icitymobile.anda.R;
import com.icitymobile.anda.R.layout;
import com.icitymobile.anda.bean.AndaResult;
import com.icitymobile.anda.bean.User;
import com.icitymobile.anda.cache.CacheCenter;
import com.icitymobile.anda.service.MemberServiceCenter;
import com.icitymobile.anda.ui.BaseActivity;
import com.icitymobile.anda.view.AndaProcessDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * 登录页面
 * @author Sherry
 */
public class LoginActivity extends BaseActivity {

	EditText mEtUsername;
	EditText mEtPassword;
	Button mBtnSubmit;
	Button mBtnRegister;
	private ImageButton resetPasswordBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.member_login);
		setTitle(R.string.member_page_login);
		

		//初始化页面
		initView();
	}
	
	private void initView() {
		mEtUsername = (EditText)findViewById(R.id.login_username);
		mEtPassword = (EditText)findViewById(R.id.login_password);
		mBtnSubmit = (Button)findViewById(R.id.login_submit);
		mBtnRegister = (Button)findViewById(R.id.register_start);
		//设置忘记密码按钮
		resetPasswordBtn = (ImageButton)findViewById(R.id.login_forgot_password);
		resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), ResetPasswordPhoneActivity.class));
			}
		});
		
		
		//提交
		mBtnSubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String username = mEtUsername.getText().toString();
				if(StringKit.isEmpty(username)) {
					mEtUsername.setError(getString(R.string.member_hint_username));
					return;
				}
				
				String password = mEtPassword.getText().toString();
				if(StringKit.isEmpty(password)) {
					mEtPassword.setError(getString(R.string.member_hint_password));
					return;
				}
				//发起登录请求
				new LoginTask(username, password).execute();
			}
		});
		
		//登录
		mBtnRegister.setOnClickListener(new View.OnClickListener() {
			

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
			}
		});
	}

	/**
	 * 会员登录
	 * @author Sherry
	 */
	class LoginTask extends AsyncTask<Void, Void, AndaResult<User>> {
		
		String username,password;
		AndaProcessDialog mAndaDialog;
		
		public LoginTask(String username, String password) {
			this.username = username;
			this.password = password;
			
			mAndaDialog = new AndaProcessDialog(LoginActivity.this);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mAndaDialog.show();
		}

		@Override
		protected AndaResult<User> doInBackground(Void... params) {
			try {
				return MemberServiceCenter.login(username, password);
			} catch (Exception e) {
				Logger.e("", "", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(AndaResult<User> result) {
			super.onPostExecute(result);
			mAndaDialog.dismiss();
			if(result != null) {
				if(result.isSucceed()) {
					LibToast.show(R.string.member_login_success);
					//登录成功
					//缓存用户资料
					CacheCenter.cacheCurrentUser(result.getData());
					//关闭登录页面
					LoginActivity.this.finish();
				} else {
					LibToast.show(R.string.member_login_error_account);
				}
			} else {
				LibToast.show(R.string.member_login_error_network);
			}
		}
	}
}

