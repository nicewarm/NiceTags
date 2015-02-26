package com.example.nice;

import com.example.nice.model.TagInfoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sreay on 14-11-25.
 */
public class LocalStatic {
	public static ArrayList<TagInfoModel> tagInfoModels = new ArrayList<TagInfoModel>();
	public static String path;

	public static void addTagInfos(List<TagInfoModel> infoModels) {
		tagInfoModels.clear();
		tagInfoModels.addAll(infoModels);
	}
}
