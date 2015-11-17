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
import com.icitymobile.anda.util.Const;
import com.icitymobile.anda.view.AndaProcessDialog;


/**
 * 会员资料注册
 * @author Sherry
 */

public class RegisterDetailActivity extends BaseActivity {
	
	private String phone;
	private User user;
	private CheckBox agreementCheckbox;//同意条款
	private TextView serviceContract;//服务条款
	
	EditText mEtCode;       //验证码
	EditText mEtUserName;   //用户名
	EditText mEtPassword;	//密码
	EditText mEtRePassword;	//重复密码
	EditText mEtRealName;	//真实姓名
	EditText mEtIDCard;	    //身份证
	TextView mTvGender;     //性别
	TextView mTvCity;       //选择城市
	Button mBtnGetCode;	    //获取验证码
	Button mBtnNext;        //下一步
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.member_register_detail);
		setTitle(R.string.member_register_detail);
		Intent intent = getIntent();
		phone = intent.getStringExtra("phone_number").trim();
	
		initView();
		user = new User();
	}
	
	// 初始化页面要素
	private void initView() {

		mEtCode = (EditText)findViewById(R.id.register_code);
		mEtUserName = (EditText)findViewById(R.id.register_username);
		mEtPassword = (EditText)findViewById(R.id.register_password);
		mEtRePassword = (EditText)findViewById(R.id.register_rePassword);		
		mEtRealName = (EditText)findViewById(R.id.register_name);
		mEtIDCard = (EditText)findViewById(R.id.register_idCard);
		mTvGender = (TextView)findViewById(R.id.register_gender_arrow);
		mTvCity = (TextView)findViewById(R.id.register_city_arrow);
		mBtnGetCode = (Button)findViewById(R.id.get_code);
		mBtnNext = (Button)findViewById(R.id.register_next);
		agreementCheckbox = (CheckBox)findViewById(R.id.register_agree_checkbox);
		serviceContract= (TextView)findViewById(R.id.register_service_contract);
		enableCaptchaButton(true);
		
		// agree contract
		serviceContract.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegisterDetailActivity.this, WebBrowserActivity.class);
				intent.putExtra(Const.EXTRA_WEBVIEW_URL, "http://jzsz.icitymobile.com:8787/jzsz/data/iCityJZSZAbout/privacy.html");
				intent.putExtra(Const.EXTRA_WEBVIEW_TITLE, "条款");
                startActivity(intent);
				
			}
		});
		//choose gender
		mTvGender.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String[] items = getResources().getStringArray(R.array.gender_array_key);
				new AlertDialog.Builder(RegisterDetailActivity.this)
				.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String key = items[which];
						mTvGender.setText(key);
					}
				}).show();
			}
		});
		
		
		//choose province then city
        mTvCity.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(getApplicationContext(), ProvinceListActivity.class),Const.DATA);
			}
        });
        
		// 获取验证码
		mBtnGetCode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				new CaptchaTask(phone).execute();
			}
		});
		
		// 提交用户信息
		mBtnNext.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				// 验证码
				String code = mEtCode.getText().toString().trim();
				if(StringKit.isEmpty(code)) {
					LibToast.show(R.string.member_hint_code);
					return;
				}					
				// 用户名
				String username = mEtUserName.getText().toString().trim();
				if(StringKit.isEmpty(username)) {
					LibToast.show(R.string.member_hint_username);
					return;
				}
				if(username.trim().length() < 6 || username.trim().length() > 40){
					LibToast.show(R.string.username_length_error);
					return;
				}
				String rexp = "^[a-zA-Z0-9_'.-]+$";
				Pattern pat = Pattern.compile(rexp);
				Matcher mat = pat.matcher(username);
				if(!mat.find()){
					LibToast.show(R.string.invalid_username_error);
					return;
				}
				String rexp2 = "^[0-9]+$";
				pat = Pattern.compile(rexp2);
				mat = pat.matcher(username);
				if(mat.find()){
					LibToast.show(R.string.username_number_error);
					return;
				}
				String rexp3 = "[.]{2,}";
				pat = Pattern.compile(rexp3);
				mat = pat.matcher(username);
				if(mat.find()){
					LibToast.show(R.string.username_dots_error);
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
				// 真实姓名
				String realName = mEtRealName.getText().toString().trim();
				if(StringKit.isEmpty(realName)) {
					LibToast.show(R.string.member_hint_realName);
					return;
				}
				// 身份证号
				String idCard = mEtIDCard.getText().toString().trim();
				if(StringKit.isEmpty(idCard)) {
					LibToast.show(R.string.member_hint_IDCard);
					return;
				}
				// 性别
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
				// 城市
				String city = mTvCity.getText().toString().trim();
				if(StringKit.isEmpty(gender)) {
					LibToast.show(R.string.member_hint_gender);
					return;
				}
				// 服务协议
				if(!agreementCheckbox.isChecked()){
					LibToast.show(R.string.member_agree_contract);
					return;
				}

				user.setIDCardNumber(idCard);
				user.setName(realName);
				user.setPassword(password);
				user.setUsername(username);
				user.setCode(code);
				user.setPhone(phone);
			
				new CheckCodeTask(phone, code, username).execute();	
			}
		});		
	}

	/**
	 * get city id, finish this class
	 * @param requestCode, resultCode,data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode){
		case Const.DATA:
			if(resultCode == RESULT_OK){
				PCItem cityObj = (PCItem)data.getSerializableExtra("city");

				mTvCity.setText(cityObj.getCity());
				user.setCity(cityObj.getCityId());
			}
			break;
		case Const.FINISH_ACTIVITY:
			if(resultCode ==RESULT_OK){
				this.finish();
			}
			break;
		default:
			break;
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
	 * 发送验证码
	 * @author Sherry
	 */
	class CaptchaTask extends AsyncTask<Void, Void, AndaResult<Void>> {
		String phone;
		AndaProcessDialog mAndaDialog;
		
		public CaptchaTask(String phone) {
			this.phone = phone;
			mAndaDialog = new AndaProcessDialog(RegisterDetailActivity.this);
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
		String username;
		AndaProcessDialog mAndaDialog;
		
		public CheckCodeTask(String phone, String code, String username) {
			this.phone = phone;
			this.code = code;
			this.username = username;
			mAndaDialog = new AndaProcessDialog(RegisterDetailActivity.this);
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
					new CheckUsernameTask(username).execute();
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
	class CheckUsernameTask extends AsyncTask<Void, Void, AndaResult<Void>> {
		String username;
		AndaProcessDialog mAndaDialog;
		
		public CheckUsernameTask(String username) {
			this.username = username;
			mAndaDialog = new AndaProcessDialog(RegisterDetailActivity.this);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mAndaDialog.show();
		}

		@Override
		protected AndaResult<Void> doInBackground(Void... params) {
			try {
				return MemberServiceCenter.checkUsername(username);
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
					Intent intent = new Intent(getApplicationContext(), RegisterBankActivity.class);
					intent.putExtra("user",user);
					startActivityForResult(intent,Const.FINISH_ACTIVITY);
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
