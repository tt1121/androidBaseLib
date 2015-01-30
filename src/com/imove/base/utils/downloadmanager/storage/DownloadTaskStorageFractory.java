package com.imove.base.utils.downloadmanager.storage;

import android.content.Context;

import com.imove.base.utils.downloadmanager.storage.db.DownloadTaskDaoStorage;

/**
 * [任务存储器工厂]<br/>
 * 
 * @author 李理
 * @date 2013年11月21日
 */
public class DownloadTaskStorageFractory {
	
	public static final int STORAGE_TYPE_NONE = 0;
	public static final int STORAGE_TYPE_DB_FILE_MANAGER = 1;
	
	public static IDownloadTaskStorage createDownloadStorage(Context context, 
			int type) {
		IDownloadTaskStorage storage = null;
		switch (type) {
		case STORAGE_TYPE_DB_FILE_MANAGER:
			storage = new DownloadTaskDaoStorage();
			break;
		default:
			break;
		}
		return storage;
	}
}

