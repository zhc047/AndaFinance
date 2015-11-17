package com.icitymobile.anda.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.icitymobile.anda.R;

/**
 * 基类
 * @author robot
 */
public class BaseActivity extends Activity {

	private final String TAG = this.getClass().getSimpleName();
	
	private ImageButton mBtnLeft;
	private ImageButton mBtnRight;
	private TextView mTvText;
	
	// 暂无内容
	private TextView mTvNoData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setContentView(int layoutResID) {
		setContentView(View.inflate(this, layoutResID, null));
	}

	@Override
	public void setContentView(View view) {
		setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		View layout = View.inflate(this, R.layout.activity_base, null);
		mBtnLeft = (ImageButton) layout.findViewById(R.id.header_btn_back);
		mBtnRight = (ImageButton) layout.findViewById(R.id.header_btn_settings);
		mTvText = (TextView) layout.findViewById(R.id.header_title);
		mTvNoData = (TextView) layout.findViewById(R.id.base_nodata);
		
		FrameLayout container = (FrameLayout) layout.findViewById(R.id.base_container);
		container.addView(view, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		mBtnLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		super.setContentView(layout, params);
	}

	public void setTitle(int strId) {
		setTitle(getString(strId));
	}
	
	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitle(String title) {
		if(mTvText != null) {
			mTvText.setText(title);
		}
	}
	
	/**
	 * 获取左侧按钮
	 * @return
	 */
	public ImageButton getBtnLeft() {
		return mBtnLeft;
	}
	
	/**
	 * 获取右侧按钮
	 * @return
	 */
	public ImageButton getBtnRight() {
		return mBtnRight;
	}
	
	/**
	 * 打开、关闭“暂无数据”
	 * @param visiable
	 */
	public void showNoDataView(boolean visiable) {
		if(visiable) {
			mTvNoData.setVisibility(View.VISIBLE);
		} else {
			mTvNoData.setVisibility(View.GONE);
		}
	}
}
