package com.imove.base.utils.downloadmanager.storage.db;

import java.util.HashSet;

import android.content.Context;

import com.imove.base.utils.db.BasicSQLiteHelper;
import com.imove.base.utils.db.IDatabaseDao;
import com.imove.base.utils.downloadmanager.DownloadConfiguration;

/**
 * @author 李理
 * @date 2013年11月18日
 */
public class DownloadDatabaseHelper extends BasicSQLiteHelper {
	public static final String TAG = "DownloadDatabaseHelper";
	private static final String DATABASE_NAME = "download.db";
	private static final int DATABASE_VERSION = DownloadConfiguration.DOWNLOAD_DB_VERSION;
	
	private static DownloadDatabaseHelper sInstance;
	
	public synchronized static DownloadDatabaseHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DownloadDatabaseHelper(context);
		}
		return sInstance;
	}
	
	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public DownloadDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	protected HashSet<Class<? extends IDatabaseDao>> getTableDaoClass() {
		HashSet<Class<? extends IDatabaseDao>> set = new HashSet<Class<? extends IDatabaseDao>>();
		set.add(DownloadTaskDao.class);
		return set;
	}
}
