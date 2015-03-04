package com.certusnet;

import java.util.ArrayList;

import android.app.Activity;

import com.airshiplay.mobile.application.MobileApplication;
import com.airshiplay.mobile.util.ScreenUtil;
import com.airshiplay.mobile.util.TelephoneUtil;
import com.certusnet.client.FWClient;
import com.certusnet.client.FWConfig;
import com.certusnet.common.util.CrashHandler;
import com.certusnet.download.DownloadMgr;
import com.certusnet.mobile.statistics.CrashExceptionHandler;


public class LocationApplication extends MobileApplication {

	public static final String GUIDE = null;
	private static LocationApplication instance;
	

	public static LocationApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		ScreenUtil.init(getApplicationContext());
		FWClient.createInstance(getApplicationContext());
		DownloadMgr.getInstance(getApplicationContext());

	}


}
