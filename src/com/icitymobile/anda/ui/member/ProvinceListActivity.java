package com.icitymobile.anda.ui.member;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.hualong.framework.log.Logger;
import com.hualong.framework.widget.LibToast;
import com.icitymobile.anda.R;
import com.icitymobile.anda.bean.AndaResult;
import com.icitymobile.anda.bean.PCItem;
import com.icitymobile.anda.service.PCBServiceCenter;
import com.icitymobile.anda.ui.BaseActivity;
import com.icitymobile.anda.view.AndaProcessDialog;



/**
 * 省份列表
 * @author sherry
 */
public class ProvinceListActivity extends BaseActivity {

	ListView mListView;
	PCAdapter mAdapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.member_register_pcblist);
		setTitle(R.string.member_register_province);
		mAdapter = new PCAdapter(this);
		mListView = (ListView)findViewById(R.id.pcb_list);
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				try {
					//send province ID
					PCItem item = (PCItem)parent.getItemAtPosition(position);
					Intent intent = new Intent (ProvinceListActivity.this,CityListActivity.class);
					intent.putExtra("provinceID", item.getCityId());
					startActivityForResult(intent,1);
				} catch (Exception e) {
					Logger.e("", "", e);
				}
			}
		});
		new ProvinceTask().execute();
	}
	
	/**
	 * get city item
	 * @param requestCode, resultCode,data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode){
		case 1:
			if(resultCode == RESULT_OK){
				PCItem cityObj = (PCItem)data.getSerializableExtra("city");
				Intent intent = new Intent();
				intent.putExtra("city",cityObj);
				setResult(RESULT_OK,intent);
				finish();
			}
		}
	}
	/**
	 * 获取省份列表
	 * @author Sherry
	 */
	class ProvinceTask extends AsyncTask<Integer, Void, AndaResult<List<PCItem>>> {
		 

		@Override
		protected AndaResult<List<PCItem>> doInBackground(Integer... params) {
			try {
				return PCBServiceCenter.getProvinceCity("0");
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

