/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.util;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;

import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.utils.VTLog;

/**
 *  Ghi log loi uncaughtException len server
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class VNMTraceUnexceptionLog  implements UncaughtExceptionHandler, Runnable {

	private Thread.UncaughtExceptionHandler unExceptionHandler;
	//private Activity app = null;

	public VNMTraceUnexceptionLog(Activity app) {
		this.unExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		//this.app = app;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uncaughtException(Thread paramThread, Throwable e) {
		// TODO Auto-generated method stub
		StackTraceElement[] arr = e.getStackTrace();
		String report = e.toString()+"\n\n";
		report += "--------- Stack trace ---------\n\n";
		for (int i=0; i<arr.length; i++)
		{
			report += "    "+arr[i].toString()+"\n";
		}
		report += "-------------------------------\n\n";

		// If the exception was thrown in a background thread inside
		// AsyncTask, then the actual exception can be found with getCause
		report += "--------- Cause ---------\n\n";
		Throwable cause = e.getCause();
		if(cause != null) {
			report += cause.toString() + "\n\n";
			arr = cause.getStackTrace();
			for (int i=0; i<arr.length; i++)
			{
				report += "    "+arr[i].toString()+"\n";
			}
		}
		report += "-------------------------------\n\n";
		
		if (!GlobalInfo.getInstance().isSendLogException){
			//clear cac thong tin trong ctrinh
//			GlobalUtil.getInstance().clearAllData();
			// send log len server
			ServerLogger.sendLog(GlobalInfo.getInstance().getCurrentTag(), report, TabletActionLogDTO.LOG_FORCE_CLOSE);
			GlobalInfo.getInstance().isSendLogException = true;
		}
		VTLog.logToFileInReleaseMode("uncaughtException " + GlobalInfo.getInstance().getCurrentTag(), report);
		unExceptionHandler.uncaughtException(paramThread, e);
	}

	/**
	 * Get report msg throwable
	 * @author: BANGHN
	 * @param e
	 * @return
	 */
	public static String getReportFromThrowable(Throwable e){
		StackTraceElement[] arr = e.getStackTrace();
		String report = e.toString()+"\n\n";
		report += "--------- Stack trace ---------\n\n";
		for (int i=0; i<arr.length; i++)
		{
			report += "    "+arr[i].toString()+"\n";
		}
		// If the exception was thrown in a background thread inside
		// AsyncTask, then the actual exception can be found with getCause
		report += "--------- Cause ---------\n\n";
		Throwable cause = e.getCause();
		if(cause != null) {
			report += cause.toString() + "\n\n";
			arr = cause.getStackTrace();
			for (int i=0; i<arr.length; i++)
			{
				report += "    "+arr[i].toString()+"\n";
			}
		}
		return report;
	}
}
