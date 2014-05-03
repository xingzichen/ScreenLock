package com.makerx.galalock.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.makerx.galalock.R;
import com.makerx.galalock.activity.util.LockPatternUtils;
import com.makerx.galalock.activity.view.LockPatternView;
import com.makerx.galalock.activity.view.LockPatternView.Cell;
import com.makerx.galalock.activity.view.LockPatternView.DisplayMode;
import com.makerx.galalock.activity.view.LockPatternView.OnPatternListener;
import com.makerx.galalock.app.GalaLockApplication;

public class ScreenLockActivity extends Activity {

	private String TAG = "LockActivity";

	private LockPatternView mLockPatternView;
	private LockPatternUtils mLockPatternUtils;
	
	private static boolean mLockStat = false;

	public static boolean isLockStat() {
		return mLockStat;
	}

	public static void setLockStat(boolean mLockStat) {
		ScreenLockActivity.mLockStat = mLockStat;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD); 
		boolean lock = ScreenLockActivity.isLockStat();
		if (lock) {
			Log.d(TAG, "in oncreate if true");
		} else {
			// 启动默认launcher
			Log.d(TAG, "in oncreate if false");
			Intent homeIntent = new Intent("android.intent.action.MAIN");
			homeIntent.addCategory("android.intent.category.HOME");
			homeIntent.addCategory("android.intent.category.DEFAULT");
			homeIntent.setPackage(GalaLockApplication.getInstance()
					.getSharePreferenceUtil().getLauncherPackageName());
			this.startActivity(homeIntent);

			finish();
			return;
		}

		setContentView(R.layout.activity_lock);

		mLockPatternUtils = GalaLockApplication.getInstance()
				.getLockPatternUtils();

		mLockPatternView = (LockPatternView) findViewById(R.id.lpv_lock);

		mLockPatternView.setOnPatternListener(new OnPatternListener() {

			@Override
			public void onPatternStart() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPatternDetected(List<Cell> pattern) {
				// TODO Auto-generated method stub
				int result = mLockPatternUtils.checkPattern(pattern);
				if (result != 1) {
					if (result == 0) {
						mLockPatternView.setDisplayMode(DisplayMode.Wrong);
						Toast.makeText(ScreenLockActivity.this, "password error",
								Toast.LENGTH_LONG).show();
					} else {
						// result = -1
						mLockPatternView.clearPattern();
						Toast.makeText(ScreenLockActivity.this,
								"password has not been set", Toast.LENGTH_LONG)
								.show();
					}

				} else {
					Toast.makeText(ScreenLockActivity.this, "unlocked",
							Toast.LENGTH_LONG).show();
					finish();
				}
			}

			@Override
			public void onPatternCleared() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPatternCellAdded(List<Cell> pattern) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(event.getKeyCode()){
		case KeyEvent.KEYCODE_APP_SWITCH:
		case KeyEvent.KEYCODE_HOME:
		case KeyEvent.KEYCODE_BACK:
			return true;
		default:
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ScreenLockActivity.setLockStat(false);
		Log.d(TAG, "SetLockStat false ");
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ScreenLockActivity.setLockStat(true);
		Log.d(TAG, "SetLockStat true ");
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	
}
