package com.icitymobile.anda.ui.member;

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

public class ChangePasswordActivity extends BaseActivity{
	EditText mEtOldPassword;	//原密码
	EditText mEtNewPassword;	//新密码
	EditText mEtConfirmPassword;//重复密码
	Button mBtnFinish;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.member_change_password);
		setTitle(R.string.member_page_password);
				
		initView();
	}
	
	// 初始化页面要素
	private void initView() {
		mEtOldPassword = (EditText)findViewById(R.id.password_current);
		mEtNewPassword = (EditText)findViewById(R.id.password_new);
		mEtConfirmPassword = (EditText)findViewById(R.id.password_confirm);
		mBtnFinish = (Button)findViewById(R.id.member_finish);
		//提交用户密码
        mBtnFinish.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
				String oldPassword = mEtOldPassword.getText().toString().trim();
				if(StringKit.isEmpty(oldPassword)) {
					LibToast.show(R.string.member_hint_password_current);
					return;
				}
				String newPassword = mEtNewPassword.getText().toString().trim();
				if(StringKit.isEmpty(newPassword)) {
					LibToast.show(R.string.member_hint_password_new);
					return;
				}
				String confirmPassword = mEtConfirmPassword.getText().toString().trim();
				if(StringKit.isEmpty(confirmPassword)||!confirmPassword.equals(newPassword)) {
					LibToast.show(R.string.member_hint_password_confirm);
					return;
				}
				if(CacheCenter.getCurrentUser() != null) {				
					user = CacheCenter.getCurrentUser();
				}
			    String user_token = user.getToken();
				new ChangePasswordTask(user_token, oldPassword, newPassword).execute();
	
        	}
        });
	}
	
	
	/**
	 * 修改会员资料
	 * @author Sherry
	 */
	class ChangePasswordTask extends AsyncTask<Void, Void, AndaResult<Void>> {
		
		String user_token;
		String oldPassword;
		String newPassword;
		AndaProcessDialog mAndaDialog;
		
		public ChangePasswordTask(String user_token, String oldPassword, String newPassword) {
			this.user_token = user_token;
			this.oldPassword = oldPassword;
			this.newPassword = newPassword;
			mAndaDialog = new AndaProcessDialog(ChangePasswordActivity.this);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mAndaDialog.show();
		}

		
		@Override
		protected AndaResult<Void> doInBackground(Void... params) {
			try {
				return MemberServiceCenter.changePassword(user_token, oldPassword, newPassword);
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
				user.setPassword(newPassword);
				CacheCenter.cacheCurrentUser(user);
				finish();
				//setResult(RESULT_OK);
			} else {
				LibToast.show("资料更新失败");
			}
		}
	}
}
