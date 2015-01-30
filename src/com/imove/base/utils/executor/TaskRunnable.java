package com.imove.base.utils.executor;

/**
 * @author 李理
 * @date 2013-7-19
 */
public abstract class TaskRunnable implements Runnable {

	protected String taskId;
	
	public void setTaskId(String id) {
		taskId = id;
	}
	
	public String getTaskId() {
		if (taskId == null) {
			return toString();
		}
		return taskId;
	}
}

