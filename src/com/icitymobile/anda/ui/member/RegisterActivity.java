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


/**
 * 注册首页
 * @author Sherry
 */
public class RegisterActivity extends BaseActivity {

	EditText mEtPhone;
	Button mBtnNext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.member_register_phone);
		setTitle(R.string.member_register);
		

		//初始化页面
		initView();
	}
	
	private void initView() {
		mEtPhone = (EditText)findViewById(R.id.register_phone);
		mBtnNext = (Button)findViewById(R.id.register_next);
		
		
		//注册
		mBtnNext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phone = mEtPhone.getText().toString();
				if(StringKit.isEmpty(phone)) {
					mEtPhone.setError(getString(R.string.member_hint_phone));
					return;
				}
				new CheckPhoneTask(phone).execute();
				
				
			}
		});
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
	 * 手机号码验证
	 * @author sherry
	 */
	class CheckPhoneTask extends AsyncTask<Void, Void, AndaResult<Void>> {
		
		String phone;
		AndaProcessDialog mAndaDialog;
		
		public CheckPhoneTask(String phone) {
			this.phone = phone;		
			mAndaDialog = new AndaProcessDialog(RegisterActivity.this);
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
				//手机号未被注册
				if(!result.isSucceed()) {
					Intent intent = new Intent(getApplicationContext(), RegisterDetailActivity.class);
					intent.putExtra("phone_number", phone);
					startActivityForResult(intent,2);	

				} else {
					LibToast.show(R.string.member_phone_registed);
				}
			} else {
				LibToast.show(R.string.member_register_network);
			}
		}
	}
}

