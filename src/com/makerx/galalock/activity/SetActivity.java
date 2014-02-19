package com.makerx.galalock.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.makerx.galalock.R;
import com.makerx.galalock.activity.util.LockPatternUtils;
import com.makerx.galalock.activity.view.LockPatternView;
import com.makerx.galalock.activity.view.LockPatternView.Cell;
import com.makerx.galalock.activity.view.LockPatternView.OnPatternListener;
import com.makerx.galalock.app.GalaLockApplication;
import com.makerx.galalock.bean.HomeAppInfo;

public class SetActivity extends Activity {

	private LockPatternView mLockPatternView;
	private LockPatternUtils mLockPatternUtils;

	private Button mBtnHomeSetting;
	private Button mBtnHomeChoose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);

		mLockPatternUtils = GalaLockApplication.getInstance()
				.getLockPatternUtils();

		mLockPatternView = (LockPatternView) findViewById(R.id.lpv_set);

		mBtnHomeSetting = (Button) findViewById(R.id.btn_home_setting);
		mBtnHomeChoose = (Button) findViewById(R.id.btn_home_choose);

		mBtnHomeSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent homeIntent = new Intent(Intent.ACTION_MAIN);
				homeIntent.addCategory(Intent.CATEGORY_HOME);
				homeIntent.addCategory(Intent.CATEGORY_DEFAULT);
				Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
				chooserIntent.putExtra(Intent.EXTRA_INTENT, homeIntent);
				SetActivity.this.startActivity(chooserIntent);

			}
		});

		mBtnHomeChoose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builderSingle = new AlertDialog.Builder(
						SetActivity.this);
				builderSingle.setIcon(R.drawable.ic_launcher);
				builderSingle.setTitle("Select One Name:-");
				final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
						SetActivity.this,
						android.R.layout.select_dialog_singlechoice);
				Log.d("ABC", ""+queryHomeApp().size());
				for(HomeAppInfo info : queryHomeApp()){
					arrayAdapter.add(info.label);
				}
				builderSingle.setNegativeButton("cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});

				builderSingle.setAdapter(arrayAdapter,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String strName = arrayAdapter.getItem(which);
								AlertDialog.Builder builderInner = new AlertDialog.Builder(
										SetActivity.this);
								builderInner.setMessage(strName);
								builderInner.setTitle("Your Selected Item is");
								builderInner.setPositiveButton("Ok",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										});
								builderInner.show();
							}
						});
				builderSingle.show();
			}
		});

		mLockPatternView.setOnPatternListener(new OnPatternListener() {

			@Override
			public void onPatternStart() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPatternDetected(List<Cell> pattern) {
				// TODO Auto-generated method stub
				mLockPatternUtils.saveLockPattern(pattern);
				Toast.makeText(SetActivity.this, "password saved",
						Toast.LENGTH_LONG).show();
				finish();
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
