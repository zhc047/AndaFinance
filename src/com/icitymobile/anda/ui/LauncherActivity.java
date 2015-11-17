package com.icitymobile.anda.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.hualong.framework.log.Logger;
import com.icitymobile.anda.R;

/**
 * 启动页面
 * @author robot
 */
public class LauncherActivity extends Activity {
	private final String TAG = this.getClass().getSimpleName();
	CountDownTimer countDown;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_launcher);
		
		countDown = new CountDownTimer(3000, 100) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				
			}
			
			/** 倒计时结束后在这里实现activity跳转  */
			@Override
			public void onFinish() {
				jump();
			}
		};
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		try {
			countDown.start();
		} catch (Exception e) {
			Logger.e(TAG, e.getMessage(),e);
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		try {
			countDown.cancel();
		} catch (Exception e) {
			Logger.e(TAG, e.getMessage(),e);
		}
	}
	
	private void jump() {
		Intent intent = new Intent();
		intent.setClass(LauncherActivity.this, MainActivity.class);
		startActivity(intent);
		 
		//销毁自身防止用户返回
		finish();
	}
}