package com.imove.base.utils.http;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.imove.base.utils.FileUtil;
import com.imove.base.utils.Log;
import com.imove.base.utils.NetWorkUtils;
import com.imove.base.utils.http.UploadRequest.UploadFileBean;

/**
 * @author 李理
 * @date 2013-6-28
 */
public class UploadUtil {

	private static final String TAG = "UploadUtil";

	private static final int CONNECTION_TIMEOUT = 40 * 1000;

	/**
	 * 调用错误
	 */
	public static final int STATE_CALL_ERROR = -1;

	/**
	 * 崩溃异常
	 */
	public static final int STATE_EXCEPTION = 0;

	/**
	 * 请求成功
	 */
	public static final int STATE_SUC = 1;

	/**
	 * 请求超时
	 */
	public static final int STATE_TIME_OUT = 2;

	/**
	 * 网络不可用
	 */
	public static final int STATE_NETWORD_UNSEARCHABLE = 3;
	
	public static final String BOUNDARY = "******";

	public static void startUploadFile(Context context, UploadRequest request) {
		if (context == null || request == null) {
			return;
		}
		Thread thread = new UploadThread(context, request);
		thread.start();
	}

	static class UploadThread extends Thread {

		Context context;
		UploadRequest request;

		UploadThread(Context context, UploadRequest request) {
			this.request = request;
			this.context = context;
		}

		public void run() {
			OnRequestListener moreListener = request.getOnRequestListener();
			String url = request.getUrl();
			List<UploadFileBean> uploadList = request.getUploadFileList();
			Map<String, String> getParams = request.getUriParam();
			long timeOut = request.getTimeout();
			boolean isMulti = request.isMulti();
			Map<String, String> httpHead = request.getHttpHead();
			if (httpHead == null) {
				httpHead = new HashMap<String, String>();
				httpHead.put("Content-Type", "application/octet-stream");
			}
			if (timeOut == -1) {
				timeOut = CONNECTION_TIMEOUT;
			}
			
			int state = STATE_SUC;
			Object result = null;
			if (!NetWorkUtils.isNetworkAvailable(context)) {
				Log.e(TAG, "connection - 网络不可用，抛弃请求");
				state = STATE_NETWORD_UNSEARCHABLE;
				if (moreListener != null) {
					moreListener.onResponse(url, state, result, 0, request, null);
				}
				return;
			}

			long t1 = System.currentTimeMillis();

			Map<String, String> resultMap = null;
			try {
				Object obj = request.getPostData();
				if (obj != null && obj instanceof String){
					resultMap = UploadUtil.uploadFile(url, (String)obj,uploadList, httpHead, getParams, isMulti, (int)timeOut);

				}else {
					resultMap = UploadUtil.uploadFile(url, uploadList, httpHead, getParams, isMulti, (int)timeOut);

				}
				
					result = resultMap.get(WebUtils.MAP_KEY_RESULT);
				
			} catch (HttpConnectionResultException e) {
				// Http Result不为200
				result = e.getMessage();
				state = Integer.parseInt(result.toString());

			} catch (IOException e) {
				result = "connection error";
				state = STATE_TIME_OUT;
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				result = e.getMessage() + "";
				state = STATE_EXCEPTION;
			}
			
			long t2 = System.currentTimeMillis();
			long time = t2 - t1;
			request.setRequestTime(time);
			Log.i(TAG, "【End】time:" + ((time)/1000.0f) + " - state:" + state + " - " +  url);

			if (result == null) {
				state = STATE_EXCEPTION;
			}

			Log.i(TAG, "连接结束：" + url + " - state:" + state);
			Log.i(TAG, "result: " + result);
			if (state == STATE_SUC) {
				IDataParser parser = request.getParser();
				if (parser != null) {
					result = parser.parseData(result.toString());
				}
			}

			if (moreListener != null) {
				moreListener.onResponse(url, state, result, request.getRequestType(), request, resultMap);
			}
		}
	}

	public static Map<String, String> uploadFile(String url, List<UploadFileBean> fileList, Map<String, String> httpHead,
			Map<String, String> getParamMap, boolean isMulti, int timeOut) throws IOException {

		HttpURLConnection conn = null;
		OutputStream out = null;
		BufferedInputStream fileInputStream = null;
		String rsp = null;

		url = WebUtils.buildRequestUrl(url, getParamMap, true);

		Log.i(TAG, "uploadFile: " + url);
		try {
			conn = WebUtils.getConnection(new URL(url), WebUtils.METHOD_POST, httpHead, timeOut);
			conn.setDoOutput(true);
			out = conn.getOutputStream();
			if (out != null) {
				writeFileOutputStream(out, fileList, isMulti);
			}

			int responseCode = conn.getResponseCode();

			Log.i(TAG, "connection - responseCode: " + responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = conn.getInputStream();
				String charset = WebUtils.getResponseCharset(conn.getContentType());
				rsp = WebUtils.getStreamAsString(inputStream, charset);

				Map<String, String> resultMap = new HashMap<String, String>();
				resultMap.put(WebUtils.MAP_KEY_RESULT, rsp);
				WebUtils.getHttpHeadMap(conn, resultMap);	
				
				Log.v(TAG, "结果(" + charset + ")：" + rsp);
				return resultMap;
			} else {
				throw new HttpConnectionResultException(responseCode);
			}

		} catch (IOException e) {
			Log.e(TAG, "connection - IOException:" + e.getLocalizedMessage());
			throw e;
		} finally {
			if (out != null) {
				out.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
			if (fileInputStream != null) {
				fileInputStream.close();
			}
		}
	}
	
	private static void writeFileOutputStream(OutputStream outputStream,
			List<UploadFileBean> fileList, boolean isMulti) throws IOException {
		
		if (fileList == null) {
			return;
		}
		
		String end = "\r\n";  
	    String twoHyphens = "--";  
	    
	    
	    DataOutputStream out = new DataOutputStream(outputStream);
		for(UploadFileBean bean : fileList) {
			String filePath = bean.getFilePath();
			Map<String, String> headMap = bean.getHeadMap();
			if (isMulti) {
				String name = bean.getName();
				String fileName = FileUtil.getFileName(filePath);
				if (name == null) {
					name = fileName;
				}
				
				out.writeBytes(twoHyphens + BOUNDARY + end); 
				out.writeBytes("Content-Disposition: form-data; " +
						"name=\"" + name + "\"; " +
						"filename=\"" + fileName + "\"" + 
						end);  
				if (headMap != null) {
					Iterator<String> it = headMap.keySet().iterator();
					while(it.hasNext()) {
						String key = it.next();
						String value = headMap.get(key);
						
						out.writeBytes(key);
						out.writeBytes(":");
						out.writeBytes(value);
						out.writeBytes(end);
					}
				}
				out.writeBytes(end); 
			}
			
			BufferedInputStream fileInputStream = null;
			File file = new File(filePath);
			try {
				FileInputStream inputStream = new FileInputStream(file);
				fileInputStream = new BufferedInputStream(inputStream);
				byte[] buffer = new byte[1024];
				int readLen = 0;
				while ((readLen = fileInputStream.read(buffer, 0, buffer.length)) != -1) {
					out.write(buffer);
				}
				
				if (isMulti) {
					out.writeBytes(end);  
					out.writeBytes(twoHyphens + BOUNDARY + twoHyphens + end);  
				}
			}finally {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			}
			
			fileInputStream = null;
		}
	}
	
	public static Map<String, String> uploadFile(String url, String postParam, List<UploadFileBean> fileList, Map<String, String> httpHead,
			Map<String, String> getParamMap, boolean isMulti, int timeOut) throws IOException {

		HttpURLConnection conn = null;
		OutputStream out = null;
		BufferedInputStream fileInputStream = null;
		String rsp = null;

		url = WebUtils.buildRequestUrl(url, getParamMap, true);

		Log.i(TAG, "uploadFile: " + url);
		try {
			conn = WebUtils.getConnection(new URL(url), WebUtils.METHOD_POST, httpHead, timeOut);
			conn.setDoOutput(true);
			out = conn.getOutputStream();
			String post = postParam;
//			out.write(post.getBytes());
			if (out != null) {
//				writeFileOutputStream(out, fileList, true);
				writeFileOutputStream(out, post, fileList);
			}

			int responseCode = conn.getResponseCode();

			Log.i(TAG, "connection - responseCode: " + responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = conn.getInputStream();
				String charset = WebUtils.getResponseCharset(conn.getContentType());
				rsp = WebUtils.getStreamAsString(inputStream, charset);

				Map<String, String> resultMap = new HashMap<String, String>();
				resultMap.put(WebUtils.MAP_KEY_RESULT, rsp);
				WebUtils.getHttpHeadMap(conn, resultMap);	
				
				Log.v(TAG, "结果(" + charset + ")：" + rsp);
				return resultMap;
			} else {
				throw new HttpConnectionResultException(responseCode);
			}

		} catch (IOException e) {
			Log.e(TAG, "connection - IOException:" + e.getLocalizedMessage());
			throw e;
		} finally {
			if (out != null) {
				out.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
			if (fileInputStream != null) {
				fileInputStream.close();
			}
		}
	}
	
	
	/**
	 * @param skipBytes
	 *            跳过的字节数
	 */
	private static void writeFileOutputStream(OutputStream outputStream, String postParm, List<UploadFileBean> fileList)
			throws IOException {

		if (fileList == null) {
			return;
		}

		String end = "\r\n";
		String twoHyphens = "--";
		StringBuffer sb = new StringBuffer();
		DataOutputStream out = new DataOutputStream(outputStream);
		
		out.writeBytes(twoHyphens + BOUNDARY + end);
		out.writeBytes("Content-Disposition: form-data; " + "name=\"" + "data" + "\"" + end + end);
		out.writeBytes(postParm );
		for (UploadFileBean bean : fileList) {
			String filePath = bean.getFilePath();
			Map<String, String> headMap = bean.getHeadMap();
//			if (isMulti) {
				String name = bean.getName();
				String fileName = FileUtil.getFileName(filePath);
				if (name == null) {
					name = fileName;
				}
				out.writeBytes(twoHyphens + BOUNDARY + end);
				out.writeBytes("Content-Disposition: form-data; " + "name=\"" + "file" + "\"; " + "filename=\"" + fileName + "\"" + end);
				
				sb.append(twoHyphens + BOUNDARY + end);
				sb.append("Content-Disposition: form-data; " + "name=\"" + "file" + "\"; " + "filename=\"" + fileName + "\"" + end);

				if (headMap != null) {
					Iterator<String> it = headMap.keySet().iterator();
					while (it.hasNext()) {
						String key = it.next();
						String value = headMap.get(key);

						out.writeBytes(key);
						out.writeBytes(":");
						out.writeBytes(value);
						out.writeBytes(end);
						
						sb.append(key);
						sb.append(":");
						sb.append(value);
						sb.append(end);
					}
				}
				out.writeBytes(end);
				sb.append(end);
//			}
			
			BufferedInputStream fileInputStream = null;
			File file = new File(filePath);
			try {
				FileInputStream inputStream = new FileInputStream(file);
				fileInputStream = new BufferedInputStream(inputStream);
				byte[] buffer = new byte[1024];
				int readLen = 0;


				// ----------------
				while ((readLen = fileInputStream.read(buffer, 0, buffer.length)) != -1) {
					out.write(buffer, 0, readLen);
				}
//				String data = "data";
//				out.write(data.getBytes());
					out.writeBytes(end);
					out.writeBytes(twoHyphens + BOUNDARY + twoHyphens + end);
			} finally {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			}

			fileInputStream = null;
		}
		
	}

}
