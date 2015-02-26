package com.example.nice.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.nice.R;

import java.util.List;

public class HGTagPickerView extends FrameLayout implements View.OnClickListener,
		AdapterView.OnItemClickListener {
	private EditText editText;
	private ListView listView;
	private TagAdapter tagAdapter;

	private boolean cancelAble = true;
	private HGTagPickerViewListener l;
	private FrameLayout rootView;

	private List<String> tags = null;

	private String content = "时尚流";

	private boolean[] selected;

	public HGTagPickerView(Context context) {
		super(context);
	}

	public static HGTagPickerView showDlg(List<String> tags, Activity activity, HGTagPickerViewListener l, boolean cancelAble) {
		HGTagPickerView dlg = new HGTagPickerView(tags, activity, l, cancelAble);
		dlg.show();
		return dlg;
	}

	public static HGTagPickerView showDlg(List<String> tags, Activity activity, HGTagPickerViewListener l) {
		return showDlg(tags, activity, l, true);
	}

	public void setPositiveBnText(String text) {
		((TextView) findViewById(R.id.bnConfirm)).setText(text);
	}

	public void setNegativeBnText(String text) {
		((TextView) findViewById(R.id.bnCancel)).setText(text);
	}

	@SuppressWarnings("deprecation")
	public HGTagPickerView(List<String> tags, Activity activity, HGTagPickerViewListener l, boolean cancelAble) {
		super(activity);
		this.tags = tags;
		if(tags != null && tags.size() != 0){
			selected = new boolean[tags.size()];
			selected[0] = true;
			content = tags.get(0);
		}
		LayoutInflater li = LayoutInflater.from(activity);
		li.inflate(R.layout.tag_picker_view, this, true);
		editText = (EditText) findViewById(R.id.editText);
		listView = (ListView) findViewById(R.id.myList);
		listView.setOnItemClickListener(this);
		findViewById(R.id.bnConfirm).setOnClickListener(this);
		findViewById(R.id.bnCancel).setOnClickListener(this);
		tagAdapter = new TagAdapter();
		listView.setAdapter(tagAdapter);
		this.cancelAble = cancelAble;
		setVisibility(View.GONE);
		rootView = getRootView(activity);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
		setId(R.id.view_tag_picker_dlg);
		this.l = l;
	}

	public boolean onBackPressed(Activity activity) {
		HGTagPickerView dlg = getDlgView(activity);
		if (null != dlg && dlg.isShowing()) {
			if (dlg.cancelAble) {
				dlg.dismiss();
				dlg.l.onTagPickerViewClicked(content,false);
			}
			return true;
		}
		return false;
	}

	public static boolean hasDlg(Activity activity) {
		HGTagPickerView dlg = getDlgView(activity);
		return dlg != null;
	}

	public static boolean isShowing(Activity activity) {
		HGTagPickerView dlg = getDlgView(activity);
		if (null != dlg && dlg.isShowing()) {
			return true;
		}
		return false;
	}

	public static HGTagPickerView getDlgView(Activity activity) {
		return (HGTagPickerView) getRootView(activity).findViewById(R.id.view_tag_picker_dlg);
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
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		for(int j=0;j<selected.length;j++){
			if(j == i){
				selected[j] = true;
			}else{
				selected[j] = false;
			}
		}
		ContentItemView destContentItemView = (ContentItemView) view;
		content = destContentItemView.contentView.getText().toString().trim();
		tagAdapter.notifyDataSetChanged();
	}

	public interface HGTagPickerViewListener {
		void onTagPickerViewClicked(String content, boolean isConfirm);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if(!TextUtils.isEmpty(editText.getText().toString().trim())){
			content = editText.getText().toString().trim();
		}
		l.onTagPickerViewClicked(content,v.getId() == R.id.bnConfirm);
	}

	class TagAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			if (tags == null) {
				return 0;
			}
			return tags.size();
		}

		@Override
		public Object getItem(int i) {
			return null;
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			ContentItemView contentItemView = null;
			if (view == null) {
				contentItemView = new ContentItemView(HGTagPickerView.this.getContext());
			} else {
				contentItemView = (ContentItemView) view;
			}
			contentItemView.setContent(tags.get(i));
			if(selected != null && selected.length != 0){
				contentItemView.setContentSelected(selected[i]);
			}
			return contentItemView;
		}
	}

	class ContentItemView extends FrameLayout{
		public TextView contentView;

		public ContentItemView(Context context) {
			super(context);
			init(context);
		}

		private void init(Context context){
			LayoutInflater inflater = LayoutInflater.from(context);
			inflater.inflate(R.layout.view_content_item,this,true);
			contentView = (TextView)findViewById(R.id.content);
		}

		public void setContent(String content){
			if(!TextUtils.isEmpty(content)){
				contentView.setText(content);
			}
		}

		public void setContentSelected(boolean selected){
			if(selected){
				contentView.setTextColor(getResources().getColor(R.color.white));
			}else{
				contentView.setTextColor(Color.parseColor("#7a7a7a"));
			}
		}
	}
}
