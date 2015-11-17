package com.icitymobile.anda;

import com.hualong.framework.LibApplication;
import com.hualong.framework.LibConfig;

public class MyApplication extends LibApplication {

	@Override
	public void onCreate() {
		super.onCreate();
		
		LibConfig.setDebug(true);
	}

}
