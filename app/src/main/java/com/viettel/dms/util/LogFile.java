/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.viettel.dms.download.ExternalStorage;
import com.viettel.utils.VTLog;

/**
 * Mo ta chuc nang class
 * 
 * @author BANGHN
 * @version 1.0
 */
public class LogFile {

	public static void logToFile(String logContent) {
		File logFile = new File(ExternalStorage.SDCARD_PATH + "/VTLogFile.txt");
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			}
		}
		try {
			// BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
					true));
			buf.append(logContent);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
	}
}
