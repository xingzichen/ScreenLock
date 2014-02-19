package com.makerx.galalock.activity.view;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.os.Handler;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makerx.galalock.R;
public class DigitalClock extends RelativeLayout {

	private Calendar mCalendar;
	private String mFormat;
	private TextView mTimeDisplay;
	private AmPm mAmPm;
	private ContentObserver mFormatChangeObserver;
	private Context mContext;
	/* called by system on minute ticks */
	private final Handler mHandler = new Handler();
	private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
				mCalendar = Calendar.getInstance();
			} else if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
				mAmPm.reloadStringResource();
			}
			// Post a runnable to avoid blocking the broadcast.
			mHandler.post(new Runnable() {
				public void run() {
					updateTime();
				}
			});
		}
	};

	static class AmPm {
		private TextView mAmPm;
		private String mAmString, mPmString;

		AmPm(View parent, Typeface tf) {
			mAmPm = (TextView) parent.findViewById(R.id.am_pm);
			if (tf != null) {
				mAmPm.setTypeface(tf);
			}

			String[] ampm = new DateFormatSymbols().getAmPmStrings();
			mAmString = ampm[0];
			mPmString = ampm[1];
		}

		void setShowAmPm(boolean show) {
			mAmPm.setVisibility(show ? View.VISIBLE : View.GONE);
		}

		void setIsMorning(boolean isMorning) {
			mAmPm.setText(isMorning ? mAmString : mPmString);
		}

		void reloadStringResource() {
			String[] ampm = new DateFormatSymbols().getAmPmStrings();
			mAmString = ampm[0];
			mPmString = ampm[1];
		}
	}

	private class FormatChangeObserver extends ContentObserver {
		public FormatChangeObserver() {
			super(new Handler());
		}

		public void onChange(boolean selfChange) {
			setDateFormat();
			updateTime();
		}
	}

	public DigitalClock(Context context) {
		this(context, null);
		mContext = context;
	}

	public DigitalClock(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	protected void onFinishInflate() {
		super.onFinishInflate();

		mTimeDisplay = (TextView) findViewById(R.id.timeDisplay);
		// mTimeDisplay.setTypeface(Typeface.createFromFile("/system/fonts/SegoeWP.ttf"));
		// mAmPm = new AmPm(this, Typeface.createFromFile("/system/fonts/DroidSansFallback.ttf"));
		mAmPm = new AmPm(this, null);
		mCalendar = Calendar.getInstance();

		setDateFormat();
	}

	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		/* monitor time ticks, time changed, timezone */
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_TICK);
		filter.addAction(Intent.ACTION_TIME_CHANGED);
		filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
		filter.addAction(Intent.ACTION_LOCALE_CHANGED);
		mContext.registerReceiver(mIntentReceiver, filter);

		/* monitor 12/24-hour display preference */
		mFormatChangeObserver = new FormatChangeObserver();
		mContext.getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, mFormatChangeObserver);

		updateTime();
	}

	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();

		mContext.unregisterReceiver(mIntentReceiver);
		mContext.getContentResolver().unregisterContentObserver(mFormatChangeObserver);
	}

	public void updateTime() {
		mCalendar = Calendar.getInstance();
		setDateFormat();
		mAmPm.reloadStringResource();

		CharSequence newTime = new SimpleDateFormat(mFormat).format(mCalendar.getTime());
		mTimeDisplay.setText(newTime);
		mAmPm.setIsMorning(mCalendar.get(Calendar.AM_PM) == 0);
	}

	private void setDateFormat() {
		boolean is24Format = android.text.format.DateFormat.is24HourFormat(getContext());
		// int fommatStringId = is24Format ? R.string.twenty_four_hour_time_format : R.string.twelve_hour_time_format;

		//		String format = getContext().getString(fommatStringId);
		String format = "HH:mm";
		mAmPm.setShowAmPm(!is24Format);
		int a = -1;
		boolean quoted = false;
		for (int i = 0; i < format.length(); i++) {
			char c = format.charAt(i);
			if (c == '\'') {
				quoted = !quoted;
			}
			if (!quoted && c == 'a') {
				a = i;
				break;
			}
		}
		if (a == 0) {
			format = format.substring(1);
		} else if (a > 0) {
			format = format.substring(0, a - 1) + format.substring(a + 1);
		}
		format = format.trim();
		mFormat = format;
	}
}
