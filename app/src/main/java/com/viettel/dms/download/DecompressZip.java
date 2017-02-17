/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/**
 * ZipDownloader
 * 
 * A simple app to demonstrate downloading and unpacking a .zip file
 * as a background task.
 * 
 * Copyright (c) 2011 Michael J. Portuesi (http://www.jotabout.com)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.viettel.dms.download;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;

import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * Unpack ZIP file
 * 
 * Stolen from:
 * http://www.jondev.net/articles/Unzipping_Files_with_Android_(Programmatically)
 *  
 * fixed to:
 * 1) ensure cleanup of resources
 * 2) decompress individual files transactionally
 * 3) buffered I/O to make it work 10x faster
 * 
 * IO Exceptions are rethrown as RuntimeException (not checked).
 */
public class DecompressZip {
	private static final int BUFFER_SIZE=8192;

	private String _zipFile;
	private String _location;
	private byte[] _buffer;

	/**
	 * Constructor.
	 * 
	 * @param zipFile		Fully-qualified path to .zip file
	 * @param location		Fully-qualified path to folder where files should be written.
	 * 						Path must have a trailing slash.
	 */
	public DecompressZip(String zipFile, String location) {
		_zipFile = zipFile;
		_location = location;
		_buffer = new byte[BUFFER_SIZE];
		dirChecker(_location);
	}

	public void unzip(Context context) {
		FileInputStream fin = null;
		ZipInputStream zin = null;
		OutputStream fout = null;
		
		File outputDir = new File(_location);
		File tmp = null;
		
		try {
			fin = new FileInputStream(_zipFile);
			zin = new ZipInputStream(fin);
			ZipEntry ze = null;
			while ((ze = zin.getNextEntry()) != null) {
				VTLog.d("Decompress", "Unzipping " + ze.getName());

				if (ze.isDirectory()) {
					dirChecker(ze.getName());
				} else {
					tmp = new File(outputDir.getAbsolutePath() + "/"+ze.getName());
					fout = new BufferedOutputStream(new FileOutputStream(tmp));
					DownloadFile.copyStream( zin, fout, _buffer, BUFFER_SIZE );
					zin.closeEntry();
					fout.close();
					fout = null;
					tmp = null; 
				}
			}
			zin.close();
			zin = null;
		} catch (IOException e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			ServerLogger.sendLog("UnZip Database", e.getMessage() + "\n" + e.toString(),
					false, TabletActionLogDTO.LOG_EXCEPTION);
			throw new RuntimeException(e);
		}catch (Exception e) {
			ServerLogger.sendLog("UnZip Database", e.getMessage() + "\n" + e.toString(),
					false, TabletActionLogDTO.LOG_EXCEPTION);
			throw new RuntimeException(e);
		}finally {
			if ( tmp != null  ) { try { tmp.delete();     } catch (Exception ignore) {;} }
			if ( fout != null ) { try { fout.close(); 	  } catch (Exception ignore) {;} }
			if ( zin != null  ) { try { zin.closeEntry(); } catch (Exception ignore) {;} }
			if ( fin != null  ) { try { fin.close(); 	  } catch (Exception ignore) {;} }
		}
	}

	private void dirChecker(String dir) {
		File f = new File(_location + dir);

		if (!f.isDirectory()) {
			f.mkdirs();
		}
	}
}
