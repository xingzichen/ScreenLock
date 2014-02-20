package com.makerx.galalock.bean;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

public class HomeAppInfo {
	public String name;
	public String packageName;
	public String label;
	public Drawable icon;

	public HomeAppInfo(ResolveInfo rInfo, PackageManager pm) {
		name = rInfo.activityInfo.name;
		packageName = rInfo.activityInfo.packageName;
		label = rInfo.loadLabel(pm).toString();
		icon = rInfo.loadIcon(pm);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return label;
	}
	
}
