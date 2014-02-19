package com.makerx.galalock.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.makerx.galalock.R;
import com.makerx.galalock.activity.util.LockPatternUtils;
import com.makerx.galalock.activity.view.LockPatternView;
import com.makerx.galalock.activity.view.LockPatternView.Cell;
import com.makerx.galalock.activity.view.LockPatternView.DisplayMode;
import com.makerx.galalock.activity.view.LockPatternView.OnPatternListener;
import com.makerx.galalock.app.GalaLockApplication;

public class LockActivity extends Activity {

	private String TAG = "LockActivity";

	private LockPatternView mLockPatternView;
	private LockPatternUtils mLockPatternUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		boolean lock = GalaLockApplication.getInstance().getLockStat();
		if (lock) {

		} else {
			// 启动默认launcher
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

		GalaLockApplication.getInstance().setLockStat(true);
		Log.d(TAG, "SetLockStat true ");

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
						Toast.makeText(LockActivity.this, "password error",
								Toast.LENGTH_LONG).show();
					} else {
						// result = -1
						mLockPatternView.clearPattern();
						Toast.makeText(LockActivity.this,
								"password has not been set", Toast.LENGTH_LONG)
								.show();
					}

				} else {
					Toast.makeText(LockActivity.this, "unlocked",
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
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
			return true;
		else
			return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "SetLockStat false ");
		GalaLockApplication.getInstance().setLockStat(false);
	}
}
