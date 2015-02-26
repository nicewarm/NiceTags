package com.example.nice.customview;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.nice.R;

public class HGAlertDlg extends FrameLayout implements View.OnClickListener {
	private TextView textMessage;
	private TextView title;
	private View dividerLine;
	private boolean cancelAble = true;
	private HGAlertDlgClickListener l;
	private FrameLayout rootView;

	public HGAlertDlg(Context context) {
		super(context);
	}

	public static HGAlertDlg showDlg(String titleStr, String message, Activity activity, HGAlertDlgClickListener l, boolean cancelAble) {
		if (!TextUtils.isEmpty(message)) {
			message = message.replace("\\n", "\n");
		}
		HGAlertDlg dlg = new HGAlertDlg(titleStr, message, activity, l, cancelAble);
		dlg.show();
		return dlg;
	}

	public static HGAlertDlg showDlg(String titleStr, String message, Activity activity, HGAlertDlgClickListener l) {
		return showDlg(titleStr, message, activity, l, true);
	}

	public void setPositiveBnText(String text) {
		((TextView) findViewById(R.id.bnConfirm)).setText(text);
	}

	public void setNegativeBnText(String text) {
		((TextView) findViewById(R.id.bnCancel)).setText(text);
	}

	public void setBackgroundColor(int color) {
		findViewById(R.id.alertDlgRoot).setBackgroundColor(color);
	}

	@SuppressWarnings("deprecation")
	public HGAlertDlg(String titleStr, String message, Activity activity, HGAlertDlgClickListener l, boolean cancelAble) {
		super(activity);
		LayoutInflater li = LayoutInflater.from(activity);
		li.inflate(R.layout.view_alert_dlg, this, true);
		title = (TextView) findViewById(R.id.title);
		dividerLine = findViewById(R.id.dividerLine);
		textMessage = (TextView) findViewById(R.id.textMessage);

		findViewById(R.id.bnConfirm).setOnClickListener(this);
		findViewById(R.id.bnCancel).setOnClickListener(this);

		this.cancelAble = cancelAble;
		textMessage.setText(message);
		title.setText(titleStr);
		setVisibility(View.GONE);

		if (TextUtils.isEmpty(titleStr)) {
			title.setVisibility(View.GONE);
			dividerLine.setVisibility(View.GONE);
		}

		if (TextUtils.isEmpty(message)) {
			textMessage.setVisibility(View.GONE);
			dividerLine.setVisibility(View.GONE);
		}

		rootView = getRootView(activity);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
		setId(R.id.view_alert_dlg);
		this.l = l;
	}

	public static boolean onBackPressed(Activity activity) {
		HGAlertDlg dlg = getDlgView(activity);
		if (null != dlg && dlg.isShowing()) {
			if (dlg.cancelAble) {
				dlg.dismiss();
				dlg.l.onAlertDlgClicked(false);
			}
			return true;
		}
		return false;
	}

	public static boolean hasDlg(Activity activity) {
		HGAlertDlg dlg = getDlgView(activity);
		return dlg != null;
	}

	public static boolean isShowing(Activity activity) {
		HGAlertDlg dlg = getDlgView(activity);
		if (null != dlg && dlg.isShowing()) {
			return true;
		}
		return false;
	}

	public static HGAlertDlg getDlgView(Activity activity) {
		return (HGAlertDlg) getRootView(activity).findViewById(R.id.view_alert_dlg);
	}

	private static FrameLayout getRootView(Activity activity) {
		return (FrameLayout) activity.findViewById(R.id.rootView);
	}

	public boolean isShowing() {
		return getVisibility() == View.VISIBLE;
	}

	public void show() {
		if (getParent() != null) {
			return;
		}
		rootView.addView(this);
		setVisibility(View.VISIBLE);
	}

	public void dismiss() {
		if (getParent() == null) {
			return;
		}
		setVisibility(View.GONE);
		rootView.removeView(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	public interface HGAlertDlgClickListener {
		void onAlertDlgClicked(boolean isConfirm);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		l.onAlertDlgClicked(v.getId() == R.id.bnConfirm);
	}
}
