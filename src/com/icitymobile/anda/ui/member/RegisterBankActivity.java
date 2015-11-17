package com.icitymobile.anda.ui.member;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hualong.framework.kit.StringKit;
import com.hualong.framework.log.Logger;
import com.hualong.framework.widget.LibToast;
import com.icitymobile.anda.R;
import com.icitymobile.anda.bean.AndaResult;
import com.icitymobile.anda.bean.BankItem;
import com.icitymobile.anda.bean.User;
import com.icitymobile.anda.cache.CacheCenter;
import com.icitymobile.anda.service.MemberServiceCenter;
import com.icitymobile.anda.ui.BaseActivity;
import com.icitymobile.anda.util.Const;
import com.icitymobile.anda.view.AndaProcessDialog;
/**
 * 会员注册银行
 * @author Sherry
 */
public class RegisterBankActivity extends BaseActivity{

	private User user;
	
	EditText mEtDebitCard;       //银行卡
	EditText mEtCardholder;   	 //持卡人
	EditText mEtReservedPhone;	 //开户手机
	TextView mTvBank;     		 //银行
	Button mBtnFinished;         //完成
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.member_register_bank);
		setTitle(R.string.member_register_bank);
						
		initView();
	}
	
	// 初始化页面要素
	private void initView() {

		mEtDebitCard = (EditText)findViewById(R.id.register_dibit_card);
		mEtCardholder = (EditText)findViewById(R.id.register_cardholder);
		mEtReservedPhone = (EditText)findViewById(R.id.register_reserved_phone);
		
		mTvBank = (TextView)findViewById(R.id.register_bank_arrow);
	
		mBtnFinished = (Button)findViewById(R.id.register_finished);
		Intent intent = getIntent();
		user = (User)intent.getSerializableExtra("user");
	
		//choose bank
        mTvBank.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		startActivityForResult(new Intent(getApplicationContext(), BankListActivity.class),Const.DATA);
        	}
        });

        //设置右按钮跳过
      	getBtnRight().setImageResource(R.drawable.skip);
      	getBtnRight().setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			new UserRegisterTask(user).execute();	
   			}
   			
   		});
      	getBtnRight().setVisibility(View.VISIBLE);
      	
		// 提交用户信息
		mBtnFinished.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 借记卡号
				String debitCard = mEtDebitCard.getText().toString().trim();
				if(StringKit.isEmpty(debitCard)) {
					LibToast.show(R.string.member_hint_debit);
					return;
				}	
				// 开户行
				String bank = mTvBank.getText().toString().trim();
				if(StringKit.isEmpty(bank)) {
					LibToast.show(R.string.member_hint_bank);
					return;
				}
				// 持卡人姓名
				String cardholder = mEtCardholder.getText().toString().trim();
				if(StringKit.isEmpty(cardholder)) {
					LibToast.show(R.string.member_hint_cardholder);
					return;
				}
				// 预留手机号码
				String reservedPhone = mEtReservedPhone.getText().toString().trim();
				if(StringKit.isEmpty(reservedPhone)) {
					LibToast.show(R.string.member_hint_reserved_phone);
					return;
				}
				
		
				user.setDebitCardNumber(debitCard);
				user.setCardHolder(cardholder);
				user.setReservedPhone(reservedPhone);
				new UserRegisterTask(user).execute();	
			}
		});		
	}

	/**
	 * get bank id
	 * @param requestCode, resultCode,data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode){
		case Const.DATA:
			if(resultCode == RESULT_OK){
				BankItem bankObj = (BankItem)data.getSerializableExtra("bank");
				mTvBank.setText(bankObj.getBank());
				user.setCardBank(bankObj.getBank());;
			}
		}
	}
	

	
	/**
	 * 用户注册
	 * @author Sherry
	 */
	class UserRegisterTask extends AsyncTask<Void, Void, AndaResult<User>> {
		User user;
		AndaProcessDialog mAndaDialog;
		
		public UserRegisterTask(User user) {
			this.user = user;
			mAndaDialog = new AndaProcessDialog(RegisterBankActivity.this);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mAndaDialog.show();
		}

		@Override
		protected AndaResult<User> doInBackground(Void... params) {
			try {
				return MemberServiceCenter.userRegister(user);
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
					//缓存用户资料
					result.getData().setToken(result.getToken());
					CacheCenter.cacheCurrentUser(result.getData());
					LibToast.show(R.string.member_register_success);
					Intent intent = new Intent();
					setResult(RESULT_OK,intent);
					finish();	
				} else if(StringKit.isNotEmpty(result.getMessage())) {
					LibToast.show(result.getMessage());
				} else {
					LibToast.show(R.string.member_register_failure);
				}
			} else {
				LibToast.show(R.string.member_register_network);
			}
		
	    }
	}

}
