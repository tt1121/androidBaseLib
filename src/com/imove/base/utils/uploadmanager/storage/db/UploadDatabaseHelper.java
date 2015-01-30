package com.imove.base.utils.uploadmanager.storage.db;

import java.util.HashSet;

import android.content.Context;

import com.imove.base.utils.db.BasicSQLiteHelper;
import com.imove.base.utils.db.IDatabaseDao;
import com.imove.base.utils.downloadmanager.DownloadConfiguration;
import com.imove.base.utils.uploadmanager.UploadConfiguration;

/**
 * @author 李理
 * @date 2013年11月18日
 */
public class UploadDatabaseHelper extends BasicSQLiteHelper {
	public static final String TAG = "DownloadDatabaseHelper";
	private static final String DATABASE_NAME = "upload.db";
	private static final int DATABASE_VERSION = UploadConfiguration.UPLOAD_DB_VERSION;
	
	private static UploadDatabaseHelper sInstance;
	
	public synchronized static UploadDatabaseHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new UploadDatabaseHelper(context);
		}
		return sInstance;
	}
	
	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public UploadDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	protected HashSet<Class<? extends IDatabaseDao>> getTableDaoClass() {
		HashSet<Class<? extends IDatabaseDao>> set = new HashSet<Class<? extends IDatabaseDao>>();
		set.add(UploadTaskDao.class);
		return set;
	}
}
