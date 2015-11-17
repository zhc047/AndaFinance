package com.icitymobile.anda.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.hualong.framework.widget.LibToast;
import com.icitymobile.anda.R;
import com.icitymobile.anda.cache.CacheCenter;
import com.icitymobile.anda.ui.member.LoginActivity;
import com.icitymobile.anda.ui.member.MemberFragment;
import com.icitymobile.anda.ui.product.ProductFragment;
import com.icitymobile.anda.ui.settings.SettingsFragment;
import com.icitymobile.anda.util.FragmentHelper;

/**
 * 主页面
 * 
 * @author robot
 */
public class MainActivity extends FragmentActivity {

	FragmentHelper mFragmentHelper;

	View currentButton;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.activity_main);

		initView();

		// 默认选中Tab1
		setDefaultTab();
	}

	private void initView() {
		mFragmentHelper = new FragmentHelper(this, getSupportFragmentManager(),
				R.id.main_container);
	}

	public void tabClick(View tab) {
		Intent intent = null;
		String id = null;
		switch (tab.getId()) {
		case R.id.main_tab_1: // 产品
			id = ProductFragment.class.getName();
			intent = new Intent(getApplicationContext(), ProductFragment.class);
			break;
		case R.id.main_tab_2: // 订单
			//id = NewsListFragment.class.getName();
			//intent = new Intent(getApplicationContext(), NewsListFragment.class);
			break;
		case R.id.main_tab_3: // 会员
			if(CacheCenter.getCurrentUser() == null) {
				startActivity(new Intent(MainActivity.this, LoginActivity.class));
			} else {
				id = MemberFragment.class.getName();
				intent = new Intent(getApplicationContext(), MemberFragment.class);
			}
			break;
		case R.id.main_tab_4: // 设置
			id = SettingsFragment.class.getName();
			intent = new Intent(getApplicationContext(), SettingsFragment.class);
			break;
		default:
			break;
		}
		if (intent != null) {
			mFragmentHelper.switchFragment(id, intent);
			setButton(tab);
		}
	}

	/**
	 * 更新Tab按钮状态
	 * 
	 * @param v
	 */
	private void setButton(View v) {
		if (currentButton != null && currentButton.getId() != v.getId()) {
			currentButton.setEnabled(true);
		}
		v.setEnabled(false);
		currentButton = v;
	}

	/**
	 * 设置默认Tab
	 */
	public void setDefaultTab() {
		tabClick(findViewById(R.id.main_tab_1));
	}

	private long exitTime = 0;

	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			LibToast.show("再按一次退出程序");
			exitTime = System.currentTimeMillis();
		} else {
			finish();
			System.exit(0);
			super.onBackPressed();
		}
	}
}
