package com.example.nice.util;

import java.io.File;

/**
 * Created by sreay on 14-8-18.
 */
public class PathManager {
	//自定义相机存储路径（图片经过剪裁后的图片，生成640*640）
	public static File getCropPhotoPath() {
		File photoFile = new File(getCropPhotoDir().getAbsolutePath() + System.currentTimeMillis() + ".jpeg");
		return photoFile;
	}

	//存储剪裁后的图片的文件夹
	public static File getCropPhotoDir() {
		String path = FileUtil.getRootPath() + "/nimei/crop/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}
}
