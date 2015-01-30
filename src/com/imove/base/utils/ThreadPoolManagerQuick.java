package com.imove.base.utils;

import com.imove.base.utils.executor.Executor;
import com.imove.base.utils.executor.ThreadPoolManager;

import android.content.Context;

/**
 * @author 李理
 * @date 2013-1-11
 */
public class ThreadPoolManagerQuick {
	
	public static void init(Context context) {
		ThreadPoolManager.getInstance(ThreadPoolManager.TYPE_QUICK).init(context);
	}
	
	public static void execute(Runnable runnable) {
		ThreadPoolManager.getInstance(ThreadPoolManager.TYPE_QUICK).execute(runnable);
	}
	
	public static void clearTask(){ 
		ThreadPoolManager.getInstance(ThreadPoolManager.TYPE_QUICK).clearTask();
	}
	
	public static Executor getExecutor() {
		return ThreadPoolManager.getInstance(ThreadPoolManager.TYPE_QUICK).getExecutor();
	}
	
    public static int getActiveCount() {
    	return ThreadPoolManager.getInstance(ThreadPoolManager.TYPE_QUICK).getActiveCount();
    }
    
    public static long getTaskCount() {
    	return ThreadPoolManager.getInstance(ThreadPoolManager.TYPE_QUICK).getTaskCount();
    }
    
    public static int getCorePoolSize() {
    	return ThreadPoolManager.getInstance(ThreadPoolManager.TYPE_QUICK).getCorePoolSize();
    }
    
    public static long getCompletedTaskCount() {
    	return ThreadPoolManager.getInstance(ThreadPoolManager.TYPE_QUICK).getCompletedTaskCount();
    }
    
    public static boolean removeTask(String taskId) {
    	return ThreadPoolManager.getInstance(ThreadPoolManager.TYPE_QUICK).removeTask(taskId);
    }
}
