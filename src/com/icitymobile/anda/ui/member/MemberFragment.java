package com.icitymobile.anda.ui.member;

import com.hualong.framework.log.Logger;
import com.hualong.framework.widget.LibToast;
import com.icitymobile.anda.R;
import com.icitymobile.anda.bean.AndaResult;
import com.icitymobile.anda.bean.User;
import com.icitymobile.anda.cache.CacheCenter;
import com.icitymobile.anda.service.MemberServiceCenter;
import com.icitymobile.anda.ui.MainActivity;
import com.icitymobile.anda.ui.member.MemberDetailActivity.EditInfoTask;
import com.icitymobile.anda.util.Const;
import com.icitymobile.anda.view.AndaDialog;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;



/**
 * 会员中心
 * @author Sherry
 */
public class MemberFragment extends Fragment {
	
	final int REQUEST_MODIFY = 1;
	private Button mBtnMemberProfile;
	private Button mBtnMemberPassword;
	private Button mBtnMemberPhone;
	private Button mBtnMemberCard;
	private Button mBtnMemberBill;
	private Button mBtnMmeberLogout;
	private TextView mTvUsername;
	private TextView mTvPhone;
	
	public static Fragment newInstance() {
		MemberFragment fragment = new MemberFragment();
		return fragment;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.member_center, null);
	
		mBtnMemberProfile = (Button)view.findViewById(R.id.member_profile);	
		mBtnMemberProfile.setOnClickListener(onClick);
		mBtnMemberPassword = (Button)view.findViewById(R.id.member_change_password);	
		mBtnMemberPassword.setOnClickListener(onClick);
		mBtnMemberPhone = (Button)view.findViewById(R.id.member_phone);	
		mBtnMemberPhone.setOnClickListener(onClick);
		mBtnMemberCard = (Button)view.findViewById(R.id.member_debit_card);	
		mBtnMemberCard.setOnClickListener(onClick);
		mBtnMemberBill = (Button)view.findViewById(R.id.member_bill);
		mBtnMemberBill.setOnClickListener(onClick);
		mBtnMmeberLogout = (Button)view.findViewById(R.id.member_logout);
		mBtnMmeberLogout.setOnClickListener(onClick);
		mTvUsername = (TextView)view.findViewById(R.id.member_profile_arrow);
		mTvPhone = (TextView)view.findViewById(R.id.member_phone_arrow);
		new GetInfoTask().execute();
		initData();
		return view;
	}
	
	// 初始化页面数据
	private void initData() {
		try {
			if(CacheCenter.getCurrentUser() != null) {
				User user = CacheCenter.getCurrentUser();
				mTvPhone.setText(user.getPhone());
				mTvUsername.setText(user.getUsername());			
			}
		} catch (Exception e) {
			Logger.e("", "", e);
		}
	}
	
	OnClickListener onClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.member_profile: //会员资料
				startActivity(new Intent(getActivity(), MemberDetailActivity.class));
				break;
			case R.id.member_change_password: //修改密码
				startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
				break;
			case R.id.member_phone: //手机号码
				startActivity(new Intent(getActivity(), ConfirmOldPhoneActivity.class));
				break;
			case R.id.member_debit_card: //银行卡
				startActivity(new Intent(getActivity(), ChangeBankActivity.class));
				break;
			case R.id.member_bill: //我的账单
				startActivity(new Intent(getActivity(), MyBillListActivity.class));
				break;
			case R.id.member_logout://退出登录
				new AndaDialog(getActivity()).setMessage(R.string.dialog_message_logout).setOnSubmitListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//清除缓存
						CacheCenter.removeCurrentUser();
						//选中Tab1
						((MainActivity)getActivity()).setDefaultTab();
						//退出登录
						startActivity(new Intent(getActivity(), LoginActivity.class));
					}
				}).show();
				break;
			default:
				break;
			}
		}
	};
	
	
	@Override
	public void onResume() {
		super.onResume();
		initData();
	}

	/**
	 * 会员登录，得到会员信息
	 * @author Sherry
	 */
	class GetInfoTask extends AsyncTask<Void, Void, AndaResult<User>> {
		
		@Override
		protected AndaResult<User> doInBackground(Void... params) {
			try {
				return MemberServiceCenter.getUserInfo(CacheCenter.getCurrentUser().getToken());
			} catch (Exception e) {
				Logger.e("", "", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(AndaResult<User> result) {
			super.onPostExecute(result);
			if(result != null) {
				//更新缓存
				CacheCenter.cacheCurrentUser(result.getData());
				//更新界面
				initData();
			} else {
				LibToast.show("资料更新失败");
			}
		}
	}
}
