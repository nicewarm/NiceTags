package com.example.nice.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import com.example.nice.NiceWarm;

import java.io.*;


/**
 * 文件工具类
 */
public final class FileUtil {
	//获得应用的存储目录
	public static String getRootPath() {
		String rootPath;
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
		if (sdCardExist) {
			rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();//获取跟目录
		} else {
			rootPath = NiceWarm.getInstance().getApplicationContext().getFilesDir().getAbsolutePath();
		}
		return rootPath;
	}

	public static boolean hasSDCard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	// ------------------------------------文件的相关方法--------------------------------------------

	/**
	 * 将数据写入一个文件
	 *
	 * @param destFilePath 要创建的文件的路径
	 * @param data         待写入的文件数据
	 * @param startPos     起始偏移量
	 * @param length       要写入的数据长度
	 * @return 成功写入文件返回true, 失败返回false
	 */
	public static boolean writeFile(String destFilePath, byte[] data, int startPos, int length) {
		try {
			if (!createFile(destFilePath)) {
				return false;
			}
			FileOutputStream fos = new FileOutputStream(destFilePath);
			fos.write(data, startPos, length);
			fos.flush();
			if (null != fos) {
				fos.close();
				fos = null;
			}
			return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 从一个输入流里写文件
	 *
	 * @param destFilePath 要创建的文件的路径
	 * @param in           要读取的输入流
	 * @return 写入成功返回true, 写入失败返回false
	 */
	public static boolean writeFile(String destFilePath, InputStream in) {
		try {
			if (!createFile(destFilePath)) {
				return false;
			}
			FileOutputStream fos = new FileOutputStream(destFilePath);
			int readCount = 0;
			int len = 1024;
			byte[] buffer = new byte[len];
			while ((readCount = in.read(buffer)) != -1) {
				fos.write(buffer, 0, readCount);
			}
			fos.flush();
			if (null != fos) {
				fos.close();
				fos = null;
			}
			if (null != in) {
				in.close();
				in = null;
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static boolean appendFile(String filename, byte[] data, int datapos, int datalength) {
		try {
			createFile(filename);
			RandomAccessFile rf = new RandomAccessFile(filename, "rw");
			rf.seek(rf.length());
			rf.write(data, datapos, datalength);
			if (rf != null) {
				rf.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 读取文件，返回以byte数组形式的数据
	 *
	 * @param filePath 要读取的文件路径名
	 * @return
	 */
	public static byte[] readFile(String filePath) {
		try {
			if (isFileExist(filePath)) {
				FileInputStream fi = new FileInputStream(filePath);
				return readInputStream(fi);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从一个数量流里读取数据,返回以byte数组形式的数据。
	 * </br></br>
	 * 需要注意的是，如果这个方法用在从本地文件读取数据时，一般不会遇到问题，但如果是用于网络操作，就经常会遇到一些麻烦(available()方法的问题)。所以如果是网络流不应该使用这个方法。
	 *
	 * @param in 要读取的输入流
	 * @return
	 * @throws java.io.IOException
	 */
	public static byte[] readInputStream(InputStream in) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			byte[] b = new byte[in.available()];
			int length = 0;
			while ((length = in.read(b)) != -1) {
				os.write(b, 0, length);
			}

			b = os.toByteArray();

			in.close();
			in = null;

			os.close();
			os = null;

			return b;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读取网络流
	 *
	 * @param in
	 * @return
	 */
	public static byte[] readNetWorkInputStream(InputStream in) {
		ByteArrayOutputStream os = null;
		try {
			os = new ByteArrayOutputStream();

			int readCount = 0;
			int len = 1024;
			byte[] buffer = new byte[len];
			while ((readCount = in.read(buffer)) != -1) {
				os.write(buffer, 0, readCount);
			}

			in.close();
			in = null;

			return os.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				os = null;
			}
		}
		return null;
	}

	/**
	 * 将一个文件拷贝到另外一个地方
	 *
	 * @param sourceFile    源文件地址
	 * @param destFile      目的地址
	 * @param shouldOverlay 是否覆盖
	 * @return
	 */
	public static boolean copyFiles(String sourceFile, String destFile, boolean shouldOverlay) {
		try {
			if (shouldOverlay) {
				deleteFile(destFile);
			}
			FileInputStream fi = new FileInputStream(sourceFile);
			writeFile(destFile, fi);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断文件是否存在
	 *
	 * @param filePath 路径名
	 * @return
	 */
	public static boolean isFileExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	/**
	 * 创建一个文件，创建成功返回true
	 *
	 * @param filePath
	 * @return
	 */
	public static boolean createFile(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}

				return file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 删除一个文件
	 *
	 * @param filePath 要删除的文件路径名
	 * @return true if this file was deleted, false otherwise
	 */
	public static boolean deleteFile(String filePath) {
		try {
			File file = new File(filePath);
			if (file.exists()) {
				return file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除 directoryPath目录下的所有文件，包括删除删除文件夹
	 *
	 * @param
	 */
	public static void deleteDirectory(File dir) {
		if (dir.isDirectory()) {
			File[] listFiles = dir.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				deleteDirectory(listFiles[i]);
			}
		}
		dir.delete();
	}

	/**
	 * 获取文件夹大小
	 *
	 * @param file File实例
	 * @return long 单位为M
	 * @throws Exception
	 */
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				size = size + getFolderSize(fileList[i]);
			} else {
				size = size + fileList[i].length();
			}
		}
		return size / 1048576;
	}

	/**
	 * 字符串转流
	 *
	 * @param str
	 * @return
	 */
	public static InputStream String2InputStream(String str) {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}

	/**
	 * 流转字符串
	 *
	 * @param is
	 * @return
	 */
	public static String inputStream2String(InputStream is) {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";

		try {
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	//批量更改文件后缀
	public static void reNameSuffix(File dir, String oldSuffix, String newSuffix) {
		if (dir.isDirectory()) {
			File[] listFiles = dir.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				reNameSuffix(listFiles[i], oldSuffix, newSuffix);
			}
		} else {
			dir.renameTo(new File(dir.getPath().replace(oldSuffix, newSuffix)));
		}
	}

	public static void writeImage(Bitmap bitmap, String destPath, int quality) {
		try {
			FileUtil.deleteFile(destPath);
			if (FileUtil.createFile(destPath)) {
				FileOutputStream out = new FileOutputStream(destPath);
				if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
					out.flush();
					out.close();
					out = null;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 清理sdcard上面时间早于time的缓存图片
	 *
	 * @param time
	 */
	public static void clear(long time, String path) {
		if (hasSDCard()) {
			return;
		}
		clearFile(time, System.currentTimeMillis(), path);
	}

	/**
	 * @param time
	 * @param currentTime
	 * @param path
	 */
	private static void clearFile(long time, long currentTime, String path) {
		if (TextUtils.isEmpty(path)) {
			return;
		}
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (file.isFile()) {
			// 删除早于time的文件
			if (file.lastModified() < time) {
				file.delete();
			}
			// 清理由于系统时间不准确可能引入的脏文件
			else if (file.lastModified() > currentTime + 1000 * 60 * 60 * 24) {
				file.delete();
			} else {
				//todo
			}
		} else {
			File[] files = file.listFiles();
			if (files != null) {
				for (File file2 : files) {
					clearFile(time, currentTime, file2.getAbsolutePath());
				}
			}
		}
	}
}
