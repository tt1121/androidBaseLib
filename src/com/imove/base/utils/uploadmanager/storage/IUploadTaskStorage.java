package com.imove.base.utils.uploadmanager.storage;

import java.util.List;

import android.content.Context;

/**
 * [下载任务存储]<br/>
 * 
 * @author 李理
 * @date 2013年11月20日
 */
public interface IUploadTaskStorage {

	void init(Context context, String storageName);
	
	List<UploadBean> queryAllTask();
	
	List<UploadBean> queryAllTask(int state);
	
	List<UploadBean> queryExcludeStateAllTask(int state);
	
	List<UploadBean> queryAllTask(String owner);
	
	UploadBean queryTask(String uploadId);
	
	boolean updateTask(UploadBean uploadBean);
	
	boolean addTask(UploadBean uploadBean);
	
	boolean addTasks(List<UploadBean> list);
	
	boolean deleteTask(String uploadId);
	
	boolean deleteAllTask();
}

