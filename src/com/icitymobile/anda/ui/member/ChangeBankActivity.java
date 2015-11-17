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
import com.icitymobile.anda.ui.member.MemberDetailActivity.GetInfoTask;
import com.icitymobile.anda.util.Const;
import com.icitymobile.anda.view.AndaProcessDialog;

public class ChangeBankActivity extends BaseActivity {

	private User user;
	
	EditText mEtDebitCard;       //银行卡
	EditText mEtCardholder;   	 //持卡人
	EditText mEtReservedPhone;	 //开户手机
	TextView mTvBank;     		 //银行
	TextView mTvBankLabel;
	Button mBtnFinished;         //完成
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.member_register_bank);
		setTitle(R.string.member_register_bank);
						
		initView();
		initData();
	}
	
	// 初始化页面要素
	private void initView() {

		mEtDebitCard = (EditText)findViewById(R.id.register_dibit_card);
		mEtCardholder = (EditText)findViewById(R.id.register_cardholder);
		mEtReservedPhone = (EditText)findViewById(R.id.register_reserved_phone);
		
		mTvBank = (TextView)findViewById(R.id.register_bank_arrow);
		mTvBankLabel = (TextView)findViewById(R.id.register_bank);
	
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
				String bank = mTvBankLabel.getText().toString().trim();
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
				new EditInfoTask(user).execute();	
			}
		});		
	}

	// 初始化页面数据
	private void initData() {
		try {
			if(CacheCenter.getCurrentUser() != null) {				
				user = CacheCenter.getCurrentUser();
				if(!StringKit.isEmpty(user.getDebitCardNumber())) {
					mEtDebitCard.setText(user.getDebitCardNumber());
				}
				if(!StringKit.isEmpty(user.getCardHolder())) {
					mEtCardholder.setText(user.getCardHolder());
				}
				if(!StringKit.isEmpty(user.getReservedPhone())) {
					mEtReservedPhone.setText(user.getReservedPhone());
				}
				if(!StringKit.isEmpty(user.getCardBank())) {
					mTvBankLabel.setText(user.getCardBank());
				}
			}
		} catch (Exception e) {
			Logger.e("", "", e);
		}
		
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
				mTvBankLabel.setText(bankObj.getBank());
				user.setCardBank(bankObj.getBank());
			}
		}
	}
	
	/**
	 * 修改会员资料
	 * @author Sherry
	 */
	class EditInfoTask extends AsyncTask<Void, Void, AndaResult<Void>> {
		
		User user;
		AndaProcessDialog mAndaDialog;
		
		public EditInfoTask(User user) {
			this.user = user;
			mAndaDialog = new AndaProcessDialog(ChangeBankActivity.this);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mAndaDialog.show();
		}

		
		@Override
		protected AndaResult<Void> doInBackground(Void... params) {
			try {
				return MemberServiceCenter.userEdit(user);
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
				CacheCenter.cacheCurrentUser(user);
		        finish();
			} else {
				LibToast.show("资料更新失败");
			}
		}
	}

}
