/*
 * 版    权： 深圳市爱猫新媒体数据科技有限公司
 * 创建人: 李理
 * 创建时间: 2014年8月12日
 */
package com.imove.base;

import android.app.Application;
import android.content.Context;

import com.imove.base.utils.JacksonUtils;
import com.imove.base.utils.Log;
import com.imove.base.utils.ThreadPoolManagerQuick;
import com.imove.base.utils.executor.ThreadPoolManager;

/**
 * @author 李理 
 */
public class IMoveBaseApplication extends Application {

	private final String TAG = IMoveBaseApplication.class.getSimpleName();
	
	private static Context sContext;

	@Override
	public void onCreate() {
		super.onCreate();
		sContext = this;
		ThreadPoolManager.initThreadPoolManager(this);
		ThreadPoolManagerQuick.execute(new Runnable() {
			@Override
			public void run() {
				long t1 = System.currentTimeMillis();
				JacksonUtils.shareJacksonUtils();	
				long t2 = System.currentTimeMillis();
				Log.v(TAG, "JacksonUtils 耗时： " + (t2-t1));
			}
		});
	}
	
	public static Context getContext() {
		return sContext;
	}
}
