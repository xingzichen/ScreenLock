package com.makerx.galalock.activity;

import com.makerx.galalock.app.GalaLockApplication;

import android.app.Activity;
import android.os.Bundle;

public class SwitchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		boolean lock = GalaLockApplication.getInstance().getLockStat();
		if (lock) {
			finish();
		}else {
		}
	}
}
