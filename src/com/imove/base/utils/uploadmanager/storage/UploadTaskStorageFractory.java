package com.imove.base.utils.uploadmanager.storage;

import android.content.Context;

import com.imove.base.utils.uploadmanager.storage.db.UploadTaskDaoStorage;

/**
 * [任务存储器工厂]<br/>
 * 
 * @author 李理
 * @date 2013年11月21日
 */
public class UploadTaskStorageFractory {
	
	public static final int STORAGE_TYPE_NONE = 0;
	public static final int STORAGE_TYPE_DB_FILE_MANAGER = 1;
	
	public static IUploadTaskStorage createUploadStorage(Context context, 
			int type) {
		IUploadTaskStorage storage = null;
		switch (type) {
		case STORAGE_TYPE_DB_FILE_MANAGER:
			storage = new UploadTaskDaoStorage();
			break;
		default:
			break;
		}
		return storage;
	}
}

