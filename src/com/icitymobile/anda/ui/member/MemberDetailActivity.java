package com.icitymobile.anda.ui.member;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.hualong.framework.log.Logger;
import com.hualong.framework.widget.LibToast;
import com.icitymobile.anda.R;
import com.icitymobile.anda.bean.AndaResult;
import com.icitymobile.anda.bean.PCItem;
import com.icitymobile.anda.bean.User;
import com.icitymobile.anda.cache.CacheCenter;
import com.icitymobile.anda.service.MemberServiceCenter;
import com.icitymobile.anda.ui.BaseActivity;
import com.icitymobile.anda.ui.MainActivity;
import com.icitymobile.anda.view.AndaDialog;
import com.icitymobile.anda.view.AndaProcessDialog;

/**
 * 会员资料
 * @author Sherry
 */

public class MemberDetailActivity extends BaseActivity {
	/*TODO*/
    SwipeRefreshLayout mSwipeRefreshLayout;
	
	final int REQUEST_MODIFY = 1;
	final int MODIFY_CITY = 2;
	private User user;
	TextView mTvName;	//姓名
	TextView mTvGender; //性别
	TextView mTvCity;	//城市
	TextView mTvIDcard;	//身份证
	
	private boolean first = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.member_detail);
		setTitle(R.string.member_page_profile);
				
		initView();
		
		initData();


	}
	
	// 初始化页面要素
	private void initView() {
		mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.member_swipe_container);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_red_inactive, R.color.btn_red_active);
		mTvName = (TextView)findViewById(R.id.member_name);
		mTvGender = (TextView)findViewById(R.id.member_gender);
		mTvCity = (TextView)findViewById(R.id.member_city);
		mTvIDcard = (TextView)findViewById(R.id.member_IDcard);
		mTvName.setOnClickListener(onClick);
		mTvIDcard.setOnClickListener(onClick);
		mTvGender.setOnClickListener(onClick);
		mTvCity.setOnClickListener(onClick);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				new GetInfoTask().execute();
			}
		});
	}
	
	// 初始化页面数据
	private void initData() {
		try {
			if(CacheCenter.getCurrentUser() != null) {				
				user = CacheCenter.getCurrentUser();
				mTvName.setText(user.getName());
				mTvGender.setText(user.getGender());
				mTvCity.setText(user.getCity());
				mTvIDcard.setText(user.getIDCardNumber());
			}
		} catch (Exception e) {
			Logger.e("", "", e);
		}
		
	}
	
	OnClickListener onClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.member_name: //姓名
				startActivityForResult(new Intent(getApplicationContext(), ChangeNameActivity.class), REQUEST_MODIFY);
				break;
			case R.id.member_IDcard: //身份证
				startActivityForResult(new Intent(getApplicationContext(), ChangeIDcardActivity.class), REQUEST_MODIFY);
				break;
			case R.id.member_gender: //性别
				startActivityForResult(new Intent(getApplicationContext(), ChangeGenderActivity.class), REQUEST_MODIFY);
				break;
			case R.id.member_city: //城市
				startActivityForResult(new Intent(getApplicationContext(), ProvinceListActivity.class), MODIFY_CITY);
				break;
			default:
				break;
			}
		}
	};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//个人修改完成后，自动刷新个人资料
		if(requestCode == REQUEST_MODIFY) {
			if(resultCode == RESULT_OK) {
				user = (User)data.getSerializableExtra("User");
				new EditInfoTask(user).execute();
				
				
			}
		}else if (requestCode == MODIFY_CITY){
			if(resultCode == RESULT_OK){
				PCItem cityObj = (PCItem)data.getSerializableExtra("city");
				mTvCity.setText(cityObj.getCity());
				user.setCity(cityObj.getCityId());
				new EditInfoTask(user).execute();
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
			mAndaDialog = new AndaProcessDialog(MemberDetailActivity.this);
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
				new GetInfoTask().execute();
				//setResult(RESULT_OK);
			} else {
				LibToast.show("资料更新失败");
			}
		}
	}

	/**
	 * 会员登录，刷新会员资料
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
			mSwipeRefreshLayout.setRefreshing(false);
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
