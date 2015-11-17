package com.icitymobile.anda.ui.member;

import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.hualong.framework.log.Logger;
import com.hualong.framework.widget.LibToast;
import com.icitymobile.anda.R;
import com.icitymobile.anda.bean.AndaResult;
import com.icitymobile.anda.bean.PCItem;
import com.icitymobile.anda.service.PCBServiceCenter;
import com.icitymobile.anda.ui.BaseActivity;
import com.icitymobile.anda.ui.member.ProvinceListActivity.ProvinceTask;

/**
 * 城市列表
 * @author sherry
 */
public class CityListActivity extends BaseActivity {

	ListView mListView;
	PCAdapter mAdapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.member_register_pcblist);
		setTitle(R.string.member_register_city);
		mAdapter = new PCAdapter(this);
		mListView = (ListView)findViewById(R.id.pcb_list);
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				try {
					//send city item
					PCItem item = (PCItem)parent.getItemAtPosition(position);
					Intent intent = new Intent ();
					intent.putExtra("city",item);
					setResult(RESULT_OK, intent);
					finish();
				} catch (Exception e) {
					Logger.e("", "", e);
				}
			}
		});
		Intent intent = getIntent();
		String provinceId = intent.getStringExtra("provinceID");
		new CityTask( provinceId).execute();
	}
	

	/**
	 * 获取城市列表
	 * @author Sherry
	 */
	class CityTask extends AsyncTask<Integer, Void, AndaResult<List<PCItem>>> {
		private String provinceId;
		
		public CityTask(String provinceId) {
			this.provinceId = provinceId;
		}
		@Override
		protected AndaResult<List<PCItem>> doInBackground(Integer... params) {
			try {
				return PCBServiceCenter.getProvinceCity(provinceId);
			} catch (Exception e) {
				Logger.e("", "", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(AndaResult<List<PCItem>> result) {
			super.onPostExecute(result);
			if(result != null && result.getData().size() != 0 && result.isSucceed()) {
				mAdapter.addAll(result.getData());
				mAdapter.notifyDataSetChanged();
			} else {
				LibToast.show("数据获取失败");
			}
		}
	}
}

