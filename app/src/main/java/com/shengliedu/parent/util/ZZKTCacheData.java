package com.shengliedu.parent.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

/**
 * Created by FussenYu on 2016/4/20.
 */
public class ZZKTCacheData {
	public static final int homework_list = 8;
	public static final int logindata = 0;
	public static final int recent_subject_list = 8;
	private static final String CACHE_DATA_PATH_ROOT = Environment.getExternalStorageDirectory()
			+ "/zzkt/";
	private static final String CACHE_DATA_PATH = Environment.getExternalStorageDirectory()
			+ "/zzkt/filecache";

	/**
	 * 获取文件名
	 * 
	 * @param uid用户的id
	 * @param id文件唯一标示
	 * @param type:数据类型
	 * @return
	 */
	public static String getFileNameById(String type, String uid, String studentId) {
		File file_root = new File(CACHE_DATA_PATH_ROOT);
		if (!file_root.exists())
			file_root.mkdirs();
		File file = new File(CACHE_DATA_PATH);
		if (!file.exists())
			file.mkdirs();
		String fileName = "cachedata_" + uid + "_" + type + "_" + studentId
				+ ".dat";
		return fileName;
	}

	/**
	 * 存储缓存文件列表
	 */
	public static void saveRecentSubList(String type,String uid, String soleId,
			List<?> list) {
		String fileName = CACHE_DATA_PATH + File.separator
				+ getFileNameById(type, uid, soleId);
		File file = new File(fileName);
		try {
			if (!file.exists())
				file.createNewFile();
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(fileName));
			oos.writeObject(list);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取缓存文件列表
	 */

	public static List<Object> getRecentSubList(String type,String uid,
			String studentId) {
		List<Object> resultList = new ArrayList<Object>();
		String fileName = CACHE_DATA_PATH + File.separator
				+ getFileNameById(type, uid, studentId);
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					fileName));
			ArrayList<Object> list_ext = (ArrayList<Object>) ois
					.readObject();

			for (Object obj : list_ext) {
				Object bean = obj;
				if (bean != null) {
					resultList.add(bean);
				}
			}
			ois.close();
		} catch (Exception e) {
			return resultList;
		}
		return resultList;
	}
}