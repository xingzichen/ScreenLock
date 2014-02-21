package com.makerx.galalock.activity.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {

	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	private static final String DEFAULT_LAUNCHER_PACKAGE = "default launcher package";
	private static final String SCREEN_LOCK_STATE = "screen lock state";

	public SharePreferenceUtil(Context context, String file) {
		sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		editor = sp.edit();
	}
	
	
	public void setLauncherPackageName(String packageName) {
		editor.putString(DEFAULT_LAUNCHER_PACKAGE, packageName);
		editor.commit();
	}
	
	public String getLauncherPackageName(){
		return sp.getString(DEFAULT_LAUNCHER_PACKAGE,"");
	}
	
	public void setScreenLockState(boolean state) {
		editor.putBoolean(SCREEN_LOCK_STATE, state);
		editor.commit();
	}
	
	public boolean getScreenLockState(){
		return sp.getBoolean(SCREEN_LOCK_STATE, false);
	}

}
