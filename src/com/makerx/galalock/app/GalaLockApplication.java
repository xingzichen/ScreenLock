package com.makerx.galalock.app;

import java.util.ArrayList;
import java.util.List;

import com.makerx.galalock.activity.util.LockPatternUtils;
import com.makerx.galalock.activity.util.SharePreferenceUtil;
import com.makerx.galalock.bean.HomeAppInfo;
import com.makerx.galalock.service.ScreenLockService;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

public class GalaLockApplication extends Application {

	private static String TAG = "GalaLockApplication";
	
	private static GalaLockApplication mInstance;
	private LockPatternUtils mLockPatternUtils;
	private SharePreferenceUtil mSpUtil;
	
	public final static String SP_FILE_NAME = "GALA_LOCK_SP";

//	private boolean mLockStat = false;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mSpUtil = new SharePreferenceUtil(this, SP_FILE_NAME);
		mInstance = this;
		mLockPatternUtils = new LockPatternUtils(this);
		startService(new Intent(this, ScreenLockService.class));
		if (mSpUtil.getLauncherPackageName().equals("")) {
			setDefaultHomeApp();
		}
	}

	public synchronized static GalaLockApplication getInstance() {
		return mInstance;
	}

	public synchronized LockPatternUtils getLockPatternUtils() {
		return mLockPatternUtils;
	}

//	public synchronized void setLockStat(boolean stat) {
//		mLockStat = stat;
//		return;
//	}
//
//	public synchronized boolean getLockStat() {
//		return mLockStat;
//	}

	public synchronized SharePreferenceUtil getSharePreferenceUtil() {
		if (null == mSpUtil)
			mSpUtil = new SharePreferenceUtil(this, SP_FILE_NAME);
		return mSpUtil;
	}

	private void setDefaultHomeApp() {
		for (HomeAppInfo info : queryHomeApp()) {
			if (!info.packageName.equals(this.getPackageName())) {
				mSpUtil.setLauncherPackageName(info.packageName);
				Log.d(TAG, "Set Default HomeApp "+info.packageName);
			}
		}
	}

	private List<HomeAppInfo> queryHomeApp() {
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_HOME);
		mainIntent.addCategory(Intent.CATEGORY_DEFAULT);
		PackageManager pm = getPackageManager();
		List<ResolveInfo> rList = pm.queryIntentActivities(mainIntent, 0);
		List<HomeAppInfo> homeList = new ArrayList<HomeAppInfo>();

		for (ResolveInfo r : rList) {
			homeList.add(new HomeAppInfo(r, pm));
		}
		return homeList;
	}
}
