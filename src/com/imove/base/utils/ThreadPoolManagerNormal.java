package com.imove.base.utils;

import com.imove.base.utils.executor.Executor;
import com.imove.base.utils.executor.ThreadPoolManager;

import android.content.Context;

/**
 * @author 李理
 * @date 2013-1-11
 */
public class ThreadPoolManagerNormal {
	
	public static void init(Context context) {
		ThreadPoolManager.getInstance(ThreadPoolManager.TYPE_NORMAL).init(context);
	}
	 
	public static void execute(Runnable runnable) {
		ThreadPoolManager.getInstance(ThreadPoolManager.TYPE_NORMAL).execute(runnable);
	}
	
	public static void clearTask(){ 
		ThreadPoolManager.getInstance(ThreadPoolManager.TYPE_NORMAL).clearTask();
	}
	
	public static Executor getExecutor() {
		return ThreadPoolManager.getInstance(ThreadPoolManager.TYPE_NORMAL).getExecutor();
	}
	
    public static int getActiveCount() {
    	return ThreadPoolManager.getInstance(ThreadPoolManager.TYPE_NORMAL).getActiveCount();
    }
    
    public static long getTaskCount() {
    	return ThreadPoolManager.getInstance(ThreadPoolManager.TYPE_NORMAL).getTaskCount();
    }
    
    public static int getCorePoolSize() {
    	return ThreadPoolManager.getInstance(ThreadPoolManager.TYPE_NORMAL).getCorePoolSize();
    }
    
    public static long getCompletedTaskCount() {
    	return ThreadPoolManager.getInstance(ThreadPoolManager.TYPE_NORMAL).getCompletedTaskCount();
    }
    
    public static boolean removeTask(String taskId) {
    	return ThreadPoolManager.getInstance(ThreadPoolManager.TYPE_NORMAL).removeTask(taskId);
    }
}
