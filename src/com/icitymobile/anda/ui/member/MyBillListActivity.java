package com.icitymobile.anda.ui.member;

import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.hualong.framework.log.Logger;
import com.hualong.framework.widget.LibToast;
import com.icitymobile.anda.R;
import com.icitymobile.anda.bean.AndaResult;
import com.icitymobile.anda.bean.BillItem;
import com.icitymobile.anda.bean.User;
import com.icitymobile.anda.cache.CacheCenter;
import com.icitymobile.anda.service.BonusServiceCenter;
import com.icitymobile.anda.ui.BaseActivity;
import com.icitymobile.anda.ui.member.BillDetail;

/**
 * 我的账单列表
 * @author sherry
 */
public class MyBillListActivity extends BaseActivity {

	SwipeRefreshLayout mSwipeRefreshLayout;
	ListView mListView;
	BillAdapter mAdapter;
	TextView mTvTotalAmount;
	TextView mTvYearlyAmount;
	User user;
	String user_token;
	
	int currentPage = 1;
	//加载更多
	View footer;
	Button footerButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.member_my_bill);
		setTitle(R.string.member_page_bill);
		
		mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.member_swipe_container);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_red_inactive, R.color.btn_red_active);
		mListView = (ListView)findViewById(R.id.bill_list);
		mTvTotalAmount = (TextView)findViewById(R.id.total_amount_number);
		mTvYearlyAmount = (TextView)findViewById(R.id.yearly_amount_number);
		
		footer=View.inflate(getBaseContext(),R.layout.list_footer,null);
		//LayoutInflater.from(this).inflate(resource, root);
		footerButton = (Button)footer.findViewById(R.id.list_footer_more_btn);
		footerButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//获取下一页数据
				new GetBillListTask(currentPage+1).execute();
			}
		});
		mListView.addFooterView(footer);
		
		mAdapter = new BillAdapter(this);
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				try {
					//send bill item
					BillItem bill = (BillItem)parent.getItemAtPosition(position);
					Intent intent = new Intent (MyBillListActivity.this, BillDetail.class);
					intent.putExtra("bill",bill);
					startActivity(intent);

				} catch (Exception e) {
					Logger.e("", "", e);
				}
			}
		});
		
		
		new GetTotalAmountTask(currentPage).execute();
		
		//下拉刷新
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				new GetBillListTask(currentPage).execute();
			}
		});
	}
	

	/**
	 * 获取总奖金数
	 * @author Sherry
	 */
	class GetTotalAmountTask extends AsyncTask<Integer, Void, AndaResult<BillItem>> {
		ListView mListView;
		int page;
		
		public GetTotalAmountTask( int page) {
			this.page = page;
		}
		@Override
		protected AndaResult<BillItem> doInBackground(Integer... params) {
			try {
				return BonusServiceCenter.getTotalAmount(CacheCenter.getCurrentUser().getToken());
			} catch (Exception e) {
				Logger.e("", "", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(AndaResult<BillItem> result) {
			super.onPostExecute(result);
			if(result != null && result.isSucceed()) {
				mTvTotalAmount.setText(result.getData().getTotalAmount());
				mTvYearlyAmount.setText(result.getData().getCurrentYearAmount());
				new GetBillListTask(page).execute();
			} else {
				LibToast.show("数据获取失败");
			}
		}
	}

	/**
	 * 获取账单列表
	 * @author Sherry
	 */
	class GetBillListTask extends AsyncTask<Integer, Void, AndaResult<List<BillItem>>> {
		int page = 1;
		
		public GetBillListTask(int page) {
			this.page = page;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mSwipeRefreshLayout.setRefreshing(true);
			enableFooter(false);
		}
		@Override
		protected AndaResult<List<BillItem>> doInBackground(Integer... params) {
			try {
				return BonusServiceCenter.getBillList(page,CacheCenter.getCurrentUser().getToken());
			} catch (Exception e) {
				Logger.e("", "", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(AndaResult<List<BillItem>> result) {
			super.onPostExecute(result);
			mSwipeRefreshLayout.setRefreshing(false);
			enableFooter(true);
			if(result != null  && result.isSucceed()) {
				currentPage = page;
				//加载第一页数据时先清空
				if(page == 1) {
					mAdapter.clear();
				}
				mAdapter.addAll(result.getData());
				mAdapter.notifyDataSetChanged();

			} else {
				LibToast.show("数据获取失败");
			}
		}
	}
	
	private void enableFooter(boolean flag) {
		if(flag) {
			footerButton.setText(getString(R.string.footer_more));
			footerButton.setEnabled(true);
		} else {
			footerButton.setText(getString(R.string.footer_loading));
			footerButton.setEnabled(false);
		}
	}
}
