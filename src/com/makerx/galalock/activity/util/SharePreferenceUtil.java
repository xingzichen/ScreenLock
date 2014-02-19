package com.makerx.galalock.activity.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {

	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	private static final String DEFAULT_LAUNCHER_PACKAGE = "default launcher package";

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

}
