package com.makerx.galalock.receiver;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.makerx.galalock.activity.LockActivity;
import com.makerx.galalock.app.GalaLockApplication;

public class LockScreenReceiver extends BroadcastReceiver {
	public static boolean wasScreenOn = true;
	private static String TAG = "LockScreenReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

			Log.d(TAG, " ACTION_SCREEN_OFF ");

			wasScreenOn = false;
			if (!GalaLockApplication.getInstance().getLockStat()) {
				Intent intent11 = new Intent(context, LockActivity.class);
				GalaLockApplication.getInstance().setLockStat(true);
				Log.d(TAG, "SetLockStat true ");
				intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent11.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

				context.startActivity(intent11);
			}

		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

			Log.d(TAG, " ACTION_SCREEN_ON ");

			wasScreenOn = true;

		} else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			KeyguardManager.KeyguardLock k1;
			KeyguardManager km = (KeyguardManager) context
					.getSystemService(context.KEYGUARD_SERVICE);
			k1 = km.newKeyguardLock("IN");
			k1.disableKeyguard();

			Log.d(TAG, " ACTION_BOOT_COMPLETED ");

			Intent intent11 = new Intent(context, LockActivity.class);
			GalaLockApplication.getInstance().setLockStat(true);
			Log.d(TAG, "SetLockStat true ");
			intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent11.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			context.startActivity(intent11);

		}

	}

}
