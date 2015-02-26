package com.example.nice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.nice.customview.HGAlertDlg;
import com.example.nice.customview.HGTagPickerView;
import com.example.nice.customview.HGTipsDlg;
import com.example.nice.model.TagInfoModel;
import com.example.nice.tagview.TagInfo;
import com.example.nice.tagview.TagView;
import com.example.nice.tagview.TagViewLeft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sreay on 14-9-22.
 */
public class ActivityAddTags extends Activity implements View.OnClickListener,
		View.OnTouchListener, TagView.TagViewListener, HGAlertDlg.HGAlertDlgClickListener,
		HGTagPickerView.HGTagPickerViewListener {
	private ImageView headLeft;
	private TextView headTitle;
	private TextView headRight;
	private ImageView image;
	private FrameLayout tagsContainer;

	private float positionX = 0;
	private float positionY = 0;
	private int width;
	private int height;
	private List<TagView> tagViews = new ArrayList<TagView>();
	private List<TagInfo> tagInfos = new ArrayList<TagInfo>();


	private HGAlertDlg dlg;
	private HGTagPickerView tagPickerView;
	private HGTipsDlg tipsDlg;
	private int tagsCount = 0;
	private List<String> base = Arrays.asList("杰克船长", "威尔·特纳|", "伊丽莎白·斯旺", "巴博萨", "吉布斯", "提阿·达尔玛", "巨型章鱼", "北海巨妖");
	private String content = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_add_tags);
		getViews();
		initViews();
		setListeners();
	}

	protected void getViews() {
		headLeft = (ImageView) findViewById(R.id.tv_head_left);
		headTitle = (TextView) findViewById(R.id.tv_head_title);
		headRight = (TextView) findViewById(R.id.tv_head_right);
		headRight.setVisibility(View.VISIBLE);
		image = (ImageView) findViewById(R.id.image);
		tagsContainer = (FrameLayout) findViewById(R.id.tagsContainer);
	}

	protected void initViews() {
		headTitle.setText("添加标签");
		headRight.setText("完成");
		image.setImageResource(R.drawable.test);
	}

	protected void setListeners() {
		headLeft.setOnClickListener(this);
		headRight.setOnClickListener(this);
		image.setOnClickListener(this);
		image.setOnTouchListener(this);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		this.width = image.getMeasuredWidth();
		this.height = image.getMeasuredHeight();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_head_left:
			this.finish();
			break;
		case R.id.tv_head_right:
			goToEditFootprints();
			break;
		case R.id.image: {
			if (tagsCount >= 5) {
				if (!HGTipsDlg.hasDlg(this)) {
					tipsDlg = HGTipsDlg.showDlg("最多添加5个标签~", this);
				}
				return;
			}
			if (tagPickerView == null) {
				tagPickerView = HGTagPickerView.showDlg(base, this, this);
			}
		}
		break;
		}
	}

	private void goToEditFootprints() {
		List<TagInfoModel> tagInfos = new ArrayList<TagInfoModel>();
		for (TagView tagView : tagViews) {
			TagInfoModel infoModel = new TagInfoModel();
			infoModel.tag_name = tagView.getData().bname;
			infoModel.x = (tagView.getData().leftMargin * 1.0f) / (width * 1.0f);
			infoModel.y = (tagView.getData().topMargin * 1.0f) / (height * 1.0f);
			tagInfos.add(infoModel);
		}
		//todo
		//在应用程序中需要将这些tag的信息上传到服务器，此处为了展示方便将其存储在本地
		LocalStatic.addTagInfos(tagInfos);
		Intent intent = new Intent(this, ActivityTagsShow.class);
		startActivity(intent);
		this.finish();
	}

	private void addTag() {
		tagsCount++;
		TagInfo tagInfo = new TagInfo();
		tagInfo.bid = 2L;
		tagInfo.bname = content;
		tagInfo.direct = TagInfo.Direction.Left;
		tagInfo.pic_x = 50;
		tagInfo.pic_y = 50;
		tagInfo.type = TagInfo.Type.CustomPoint;
		tagInfo.leftMargin = (int) positionX;
		tagInfo.topMargin = (int) positionY;
		TagView tagView = new TagViewLeft(this, null);
		tagView.setData(tagInfo);
		tagView.setTagViewListener(this);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		params.leftMargin = tagInfo.leftMargin;
		params.topMargin = tagInfo.topMargin;
		tagViews.add(tagView);
		tagsContainer.addView(tagView, params);
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		int action = motionEvent.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			positionX = motionEvent.getX();
			positionY = motionEvent.getY();
		}
		return false;
	}

	private View destView;

	@Override
	public void onTagViewClicked(View view, TagInfo info) {
		destView = view;
		if (null == dlg) {
			dlg = HGAlertDlg.showDlg("确定删除该标签?", null, this, this);
		}
	}

	@Override
	public void onBackPressed() {
		if (tipsDlg != null) {
			tipsDlg.onBackPressed(this);
			tipsDlg = null;
			return;
		}
		if (dlg != null) {
			dlg.onBackPressed(this);
			dlg = null;
			return;
		}
		if (tagPickerView != null) {
			tagPickerView.onBackPressed(this);
			tagPickerView = null;
			return;
		}
		super.onBackPressed();
	}

	private void removeTag() {
		tagsCount--;
		tagViews.remove(destView);
		tagsContainer.removeView(destView);
	}

	@Override
	public void onAlertDlgClicked(boolean isConfirm) {
		if (isConfirm) {
			removeTag();
		}
		dlg = null;
	}

	@Override
	public void onTagPickerViewClicked(String content, boolean isConfirm) {
		if (isConfirm) {
			this.content = content;
			addTag();
		}
		tagPickerView = null;
	}
}
