/**
 * Copyright 2015 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.util;

import java.util.concurrent.ThreadPoolExecutor;

import android.os.AsyncTask;

import com.viettel.utils.VTLog;

/**
 * Thuc hien mot so ultil kiem tra asynctask
 * @author banghn
 * @version 1.0
 */
public class AsyncTaskUtil {

	private static final int NUM_THREAD_ALLOW_SEND = 3;

	public static String getThreadInfo(){
		StringBuffer log = new StringBuffer();
		try {
			//log.append(" Total thread: " + getNumAsyncTaskTotal());
			log.append("Thread active: " + getNumAsyncTaskActive());
			log.append("\n Thread waiting: " + getNumAsyncTaskWait());
			//log.append("\n Thread completed: " + getNumAsyncTaskComplete());
			//log.append("\n SQL in transaction: " + SQLUtils.getInstance().isInTransaction());
		} catch (Throwable e) {
			//neu loi thi loi os, khong can xu ly gi them neu ko lay duoc
			VTLog.w("", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return log.toString();
	}
	
	// Lấy số lượng AsyncTask đang thực thi (tham khảo)
	public static int getNumAsyncTaskActive() {
		int result = 0;
		try {
			ThreadPoolExecutor executor = getThreadPoolExecutor();
			if (executor != null) {
				result = executor.getActiveCount();
			}
		} catch (Throwable ex) {
			// neu loi thi loi os, khong can xu ly gi them neu ko lay duoc
			VTLog.w("", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
		return result;
	}

	// Lấy số lượng AsyncTask đang chờ trong queue (tham khảo)
	public static int getNumAsyncTaskWait() {
		int result = 0;
		try {
			ThreadPoolExecutor executor = getThreadPoolExecutor();
			if (executor != null && executor.getQueue() != null) {
				result = executor.getQueue().size();
			}
		} catch (Throwable ex) {
			// neu loi thi loi os, khong can xu ly gi them neu ko lay duoc
			VTLog.w("", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
		return result;
	}

	// Lấy số lượng AsyncTask đã hoàn thành(tham khảo)
	public static long getNumAsyncTaskComplete() {
		long result = 0;
		try {
			ThreadPoolExecutor executor = getThreadPoolExecutor();
			if (executor != null) {
				result = executor.getCompletedTaskCount();
			}
		} catch (Throwable ex) {
			// neu loi thi loi os, khong can xu ly gi them neu ko lay duoc
			VTLog.w("", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
		return result;
	}

	// Lấy tổng số lượng AsyncTask đã được queue(tham khảo)
	public static long getNumAsyncTaskTotal(){
		long result = 0;
		try {
			ThreadPoolExecutor executor = getThreadPoolExecutor();
			if (executor != null) {
				result = executor.getTaskCount();
			}
		} catch (Throwable ex) {
			// neu loi thi loi os, khong can xu ly gi them neu ko lay duoc
			VTLog.w("", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
		return result;
	}

	public static ThreadPoolExecutor getThreadPoolExecutor() throws Throwable{
		return AsyncTask.THREAD_POOL_EXECUTOR instanceof ThreadPoolExecutor ? ((ThreadPoolExecutor) AsyncTask.THREAD_POOL_EXECUTOR)
				: null;
	}

	/**
	 * Check allow do task
	 * @author: duongdt3
	 * @since: 08:41:45 10 Nov 2014
	 * @return: boolean
	 * @throws:
	 * @return
	 */
	public static boolean checkAllowDoTask() {
		return (getNumAsyncTaskActive() <= NUM_THREAD_ALLOW_SEND);
	}
}
