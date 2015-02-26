package com.example.nice.customview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.nice.R;

public class HGTipsDlg extends FrameLayout implements View.OnClickListener {
	private TextView title;
	private boolean cancelAble = true;
	private FrameLayout rootView;

	private OnClickConfirmListener listener;

	public interface OnClickConfirmListener {
		public void onClickListener();
	}

	public HGTipsDlg(Context context) {
		super(context);
	}

	public static HGTipsDlg showDlg(String titleStr, Activity activity, boolean cancelAble) {
		HGTipsDlg dlg = new HGTipsDlg(titleStr, activity, cancelAble);
		dlg.show();
		return dlg;
	}

	public static HGTipsDlg showDlg(String titleStr, Activity activity) {
		return showDlg(titleStr, activity, true);
	}

	public static HGTipsDlg showDlg(String titleStr, Activity activity, OnClickConfirmListener listener) {
		HGTipsDlg dlg = new HGTipsDlg(titleStr, activity, true, listener);
		dlg.show();
		return dlg;
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
	public HGTipsDlg(String titleStr, Activity activity, boolean cancelAble) {
		super(activity);
		LayoutInflater li = LayoutInflater.from(activity);
		li.inflate(R.layout.view_tips_dlg, this, true);
		title = (TextView) findViewById(R.id.title);

		findViewById(R.id.bnConfirm).setOnClickListener(this);

		this.cancelAble = cancelAble;
		title.setText(titleStr);
		setVisibility(View.GONE);

		rootView = getRootView(activity);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
		setId(R.id.view_tips_dlg);
	}

	@SuppressWarnings("deprecation")
	public HGTipsDlg(String titleStr, Activity activity, boolean cancelAble, OnClickConfirmListener listener) {
		super(activity);
		LayoutInflater li = LayoutInflater.from(activity);
		li.inflate(R.layout.view_tips_dlg, this, true);
		title = (TextView) findViewById(R.id.title);

		findViewById(R.id.bnConfirm).setOnClickListener(this);

		this.cancelAble = cancelAble;
		title.setText(titleStr);
		setVisibility(View.GONE);

		rootView = getRootView(activity);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
		setId(R.id.view_tips_dlg);
		this.listener = listener;
	}

	public static boolean onBackPressed(Activity activity) {
		HGTipsDlg dlg = getDlgView(activity);
		if (null != dlg && dlg.isShowing()) {
			if (dlg.cancelAble) {
				dlg.dismiss();
			}
			return true;
		}
		return false;
	}

	public static boolean hasDlg(Activity activity) {
		HGTipsDlg dlg = getDlgView(activity);
		return dlg != null;
	}

	public static boolean isShowing(Activity activity) {
		HGTipsDlg dlg = getDlgView(activity);
		if (null != dlg && dlg.isShowing()) {
			return true;
		}
		return false;
	}

	public static HGTipsDlg getDlgView(Activity activity) {
		return (HGTipsDlg) getRootView(activity).findViewById(R.id.view_tips_dlg);
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

	@Override
	public void onClick(View v) {
		dismiss();
		if (listener != null) {
			listener.onClickListener();
		}
	}
}
