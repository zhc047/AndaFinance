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
import com.icitymobile.anda.bean.BankItem;
import com.icitymobile.anda.service.PCBServiceCenter;
import com.icitymobile.anda.ui.BaseActivity;


/**
 * 银行列表
 * @author sherry
 */
public class BankListActivity extends BaseActivity {

	ListView mListView;
	PCAdapter mAdapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.member_register_pcblist);
		setTitle(R.string.member_register_choose_bank);
		mAdapter = null;
		mListView = (ListView)findViewById(R.id.pcb_list);
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				try {
					//send bank item
					BankItem item = (BankItem)parent.getItemAtPosition(position);
					Intent intent = new Intent ();
					intent.putExtra("bank",item);
					setResult(RESULT_OK, intent);
					finish();
				} catch (Exception e) {
					Logger.e("", "", e);
				}
			}
		});

		new BankTask(mListView).execute();
	}
	

	/**
	 * 获取银行列表
	 * @author Sherry
	 */
	class BankTask extends AsyncTask<Integer, Void, AndaResult<List<BankItem>>> {
		private ListView mListView;
		
		public BankTask(ListView mListView) {
			this.mListView = mListView;
		}
		@Override
		protected AndaResult<List<BankItem>> doInBackground(Integer... params) {
			try {
				return PCBServiceCenter.getBank();
			} catch (Exception e) {
				Logger.e("", "", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(AndaResult<List<BankItem>> result) {
			super.onPostExecute(result);
			if(result != null && result.getData().size() != 0 && result.isSucceed()) {
				BankAdapter adapter = new BankAdapter (BankListActivity.this,R.layout.pcb_list_item,result.getData());
				mListView.setAdapter(adapter);
			} else {
				LibToast.show("数据获取失败");
			}
		}
	}
}
