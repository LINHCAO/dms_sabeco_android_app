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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * Utility class for managing external storage.
 * 
 */
@SuppressLint("SdCardPath")
public class ExternalStorage {
	
	@SuppressWarnings("unused")
	private static final String TAG = "ExternalStorage";
	
	// Convention for external storage path used by Android 2.2.
	private static final String EXT_STORAGE_ROOT_PREFIX = "/Android/data/";
	private static final String EXT_STORAGE_ROOT_SUFFIX = "/files/";

	private static final String INT_STORAGE_DATA = "/data/data/";
	private static final String INT_STORAGE_DATABASE = "/databases/";
	
	private static StringBuilder sStoragePath = new StringBuilder();

	//"/mnt/sdcard/";
	public static String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";

	//external storage directory
	public static String EXTERNAL_CACHE_DIR = GlobalInfo.getInstance().getAppContext().getExternalCacheDir().getAbsolutePath() + "/";
	
	/**
	 * Likely places where we could find an external SD card mounted.
	 * This list was determined empirically, by looking at various devices.
	 * It is not known to be complete.
	 */ 
	private static final String ALTERNATE_SDCARD_MOUNTS[] = {
		"/emmc",				// Internal storage on Droid Incredible, Nook Color/CyanogenMod, some other devices
		"/sdcard/ext_sd",		// Newer (2011) HTC devices (Flyer, Rezound)
		"/sdcard-ext",			// Some Motorola devices (RAZR)
		"/sdcard/sd",			// Older Samsung Galaxy S (Captivate)
		"/sdcard/sdcard"		// Archos tablets
	};
	
	//thu muc DMS ngoai SDCard
	public static final String DMS_SABECO_FOLDER = SDCARD_PATH + "DMSSABECO/";
	//Thu muc chua anh chup hinh
	public static final String VNM_TAKEN_PHOTO_FOLDER 	= "TAKEN_PHOTOS";
	//thu muc file buffer dong bo du lieu
	public static final String VNM_SYNDATA_FOLDER 		= "SYN_DATA";
	//thu muc database trong cache VNM
	public static final String VNM_DATABASE_FOLDER = EXTERNAL_CACHE_DIR + "DATABASE";
	//folder va file path lib dong
	public static final String SYSTEM_LIB_PATH = "/system/usr"; 
	public static final String ICU_LIB_NAME = "icudt46l.dat";
	public static final String ICU_LIB_DIR = "icu";
	public static final String DMS_NATIVE_PATH = DMS_SABECO_FOLDER + "DMSLib";// thu muc chua file lib
	

	/**
	 * Return a File object containing the folder to use for storing
	 * data on the external SD card.  Falls back to the internal
	 * application cache if SD is not available or writable.
	 * 
	 * @param context
	 * @param dirName name of sub-folder within the SD card root.
	 * @return
	 */
	public static File getSDCacheDir( Context context, String dirName ) {
		File cacheDir = null;
		
		// Check to see if SD Card is mounted and read/write accessible
		if ( android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState()) ) {
			// Get the directory on the SD card to store content
			// Attempt to use getExternalFilesDir() if we are on Android 2.2 or newer
			// Data stored in this location will auto-delete with app uninstall
			Method getExternalFilesDirMethod = null;
			try {
				getExternalFilesDirMethod = Context.class.getMethod( "getExternalFilesDir", String.class );
				cacheDir = (File) getExternalFilesDirMethod.invoke( context, dirName );
			} catch (NoSuchMethodException e) {
				// Android 2.1 and earlier - use old APIs
				cacheDir = buildCacheDirPath( context, android.os.Environment.getExternalStorageDirectory(), dirName );
			} catch (IllegalArgumentException e) {
				cacheDir = buildCacheDirPath( context, android.os.Environment.getExternalStorageDirectory(), dirName );
			} catch (IllegalAccessException e) {
				cacheDir = buildCacheDirPath( context, android.os.Environment.getExternalStorageDirectory(), dirName );
			} catch (InvocationTargetException e) {
				cacheDir = buildCacheDirPath( context, android.os.Environment.getExternalStorageDirectory(), dirName );
			}
		}
		
		if ( cacheDir == null ) {
			// Attempting to find the default external storage was a failure.
			// Look for another suitable external filesystem where we can store our crap
			for ( int i = 0; i < ALTERNATE_SDCARD_MOUNTS.length; i++ ) {
				File alternateDir = new File( ALTERNATE_SDCARD_MOUNTS[i] );
				if ( alternateDir.exists() && alternateDir.isDirectory() && 
						alternateDir.canRead() && alternateDir.canWrite() ) {
					cacheDir = buildCacheDirPath( context, alternateDir, dirName );
					break;
				}	
			}
		}

		// Attempt to create folder on external storage if it does not exist
		if ( cacheDir != null && !cacheDir.exists() ) {
			if ( !cacheDir.mkdirs() ) {
				cacheDir = null;		// Failed to create folder
			}
		}

		// Fall back on internal cache as a last resort
		if ( cacheDir == null ) {
			cacheDir = new File( context.getCacheDir() + File.separator + dirName );
			cacheDir.mkdirs();
		}
		
		return cacheDir;		
	}
	
	/**
	 * Clear files from SD cache.
	 * 
	 * @param context
	 */
	public static void clearSDCache( Context context, String dirName ) {
		File cacheDir = getSDCacheDir( context, dirName );
		File[] files = cacheDir.listFiles();
		for (File f : files) {
			f.delete();
		}
		cacheDir.delete();
	}

	
	/**
	 * get file database .
	 * 
	 * @param context
	 */
	public static File getDatabasePath( Context context ) {
		StringBuilder str = new StringBuilder();
		str.append(INT_STORAGE_DATA);
		str.append(context.getPackageName());
		str.append(INT_STORAGE_DATABASE );
		//str.append(Constants.DATABASE_NAME);
		File f = new File(str.toString());
		return f;
	}
	/**
	 * Use older Android APIs to put data in the same relative directory location
	 * as the 2.2 API.
	 * 
	 * When device upgrades to 2.2, data will auto-delete with app uninstall.
	 * 
	 * @param mountPoint
	 * @return
	 */
	private static File buildCacheDirPath( Context context, File mountPoint, String dirName ) {
		sStoragePath.setLength(0);
		sStoragePath.append(EXT_STORAGE_ROOT_PREFIX);
		sStoragePath.append(context.getPackageName());
		sStoragePath.append(EXT_STORAGE_ROOT_SUFFIX );
		sStoragePath.append(dirName);
		return new File( mountPoint, sStoragePath.toString());
	}
	
	/**
	 * PhucNT
	 * get thư mục đương dẫn chưa file db download
	 * @return
	 */
	public static File getFileDBPath( Context context) {
		// if (MemoryUtils.isSdPresent()){
		// return new File(SDCARD_PATH );
		// }else {
		// return getDatabasePath(context);
		// }
		File path = new File(VNM_DATABASE_FOLDER);
		if (!path.exists()) {
			path.mkdir();
		}
		return path;
	}
	
	/**
	 * PhucNT
	 * get thư mục đương dẫn chưa nhung file hinh cap upload
	 * @return
	 */
	public static File getTakenPhotoPath( Context context) {
		final File root = new File(DMS_SABECO_FOLDER);
		if (!root.exists()) {
			root.mkdir();
		}
			final File path = new File(DMS_SABECO_FOLDER+VNM_TAKEN_PHOTO_FOLDER);
			if (!path.exists()) {
				path.mkdir();
			}
		return path;
		
	}

	/**
	 * 
	*  create path syndata on sdcard
	*  @author: HaiTC3
	*  @return
	*  @return: File
	*  @throws:
	 */
	public static File getPathSynData() {
		final File root = new File(DMS_SABECO_FOLDER);
		if (!root.exists()) {
			root.mkdir();
		}
		final File path = new File(DMS_SABECO_FOLDER + VNM_SYNDATA_FOLDER);
		if (!path.exists()) {
			path.mkdir();
		}
		return path;
	}
	
	/**
	 * Lay dung luong vung nho ngoai
	 * @author: banghn
	 * @return
	 */
	public static float megabytesAvailableOnDisk() {
		File externalDisk = Environment.getExternalStorageDirectory();
	    StatFs stat = new StatFs(externalDisk.getPath());
	    long bytesAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();
	    return bytesAvailable / (1024.f * 1024.f);
	}
	/**
	 * check .so file in Internal Storage
	 * @author: duongdt3
	 * @since: 11:11:28 2 Dec 2014
	 * @return: boolean
	 * @throws:  
	 * @return
	 */
	public static synchronized boolean checkNativeLib(Context con) {
		
		VTLog.d("checkNativeLib", "start");
		boolean isHaveNativeLib = false;
		
		File[] listFileSo = null;
		try {
			File filesInternal = con.getFilesDir();
			FileFilter fileFilter = new FileFilter() {
				
				@Override
				public boolean accept(File f) {
					return f.isFile() && f.getName().endsWith(".so");
				}
			};
			
			//check internal files have .so file
			listFileSo = filesInternal.listFiles(fileFilter);
			isHaveNativeLib = (listFileSo != null && listFileSo.length > 0);
			
			//if not have in internal, copy to it
			if (!isHaveNativeLib) {
				//check DMSLib in sdCard, copy libs file
				copySDCardLib2Internal(con);
				isHaveNativeLib = true;
			}
			
			//load lib
			if (isHaveNativeLib) {
				String urlInternal = filesInternal.getAbsolutePath();
				FileInputStream fisLibs = null;
				InputStreamReader in = null;
				BufferedReader br = null;
				try {
					//read list so file and order
					fisLibs = con.openFileInput("libs.txt");
					in = new InputStreamReader(fisLibs);
					br = new BufferedReader(in);
					String str = null;
					//check have file libs.txt
					isHaveNativeLib = false;
					//load so files
					while ((str = br.readLine()) != null) {
						String fileInternalPath = urlInternal + "/" + str.trim();
						File fSo = new File(fileInternalPath);
						if (fSo.exists()) {
							isHaveNativeLib = true;
							//load lib
							System.load(fileInternalPath);
						} else{
							throw new FileNotFoundException("not found " + str.trim());
						}
					}
				} catch (Throwable e) {
					isHaveNativeLib = false;
					VTLog.e("checkNativeLib", VNMTraceUnexceptionLog.getReportFromThrowable(e));
				} finally{
					if (br != null) {
						br.close();
					}
					if (in != null) {
						in.close();
					}
					if (fisLibs != null) {
						fisLibs.close();
					}
				}
			}
		} catch (Exception e) {
			isHaveNativeLib = false;
			ServerLogger.sendLog("checkNativeLib", VNMTraceUnexceptionLog.getReportFromThrowable(e), false, TabletActionLogDTO.LOG_EXCEPTION);
		}
		
		VTLog.d("checkNativeLib", "end " + isHaveNativeLib);
		return isHaveNativeLib;
	}
	/**
	 * copy lib from sdcard to internal
	 * @author: duongdt3
	 * @since: 14:01:01 3 Dec 2014
	 * @return: void
	 * @throws:  
	 * @param con
	 * @throws Exception
	 */
	private static synchronized void copySDCardLib2Internal(Context con) throws Exception{
		VTLog.d("copySDCardLib2Internal", "start");
		List<String> listFileImport = new ArrayList<String>();
		//copy tất cả thư viện .so vào Internal
		File filePath = new File(ExternalStorage.DMS_NATIVE_PATH);
		if (filePath.exists()) {
			FileFilter fileFilter = new FileFilter() {
				
				@Override
				public boolean accept(File f) {
					return f.isFile() && (f.getName().endsWith(".so") || f.getName().equals("libs.txt"));
				}
			};
			
			try {
				File[] listFile = filePath.listFiles(fileFilter);
				if (listFile != null && listFile.length > 0) {
					String urlInternal = con.getFilesDir().getAbsolutePath();
					for (File file : listFile) {
						//chép file vào internal
						if (file != null && file.isFile()) {
							String fileInternalPath = urlInternal + "/" + file.getName();
							//add, help delete file when error
							listFileImport.add(fileInternalPath);
							FileOutputStream fileInternal = con.openFileOutput(file.getName(), Context.MODE_PRIVATE);
							ExternalStorage.copyFile(new FileInputStream(file.getAbsolutePath()), fileInternal);
						}
					}
					VTLog.d("copySDCardLib2Internal", "end");
				} else{
					throw new FileNotFoundException();
				}
			} catch (Exception e) {
				//delele files when error
				for (String fPath : listFileImport) {
					File f = null;
					try {
						f = new File(fPath);
						if (f.exists()) {
							f.delete();
						}
					} catch (Exception e2) {
						VTLog.e("copySDCardLib2Internal delete when error", VNMTraceUnexceptionLog.getReportFromThrowable(e2));
					}
				}
				VTLog.e("copySDCardLib2Internal", VNMTraceUnexceptionLog.getReportFromThrowable(e));
				throw e;
			}
		} else{
			throw new FileNotFoundException();
		}
	}
	/**
	 * copy file
	 * @author: duongdt3
	 * @since: 10:36:27 2 Dec 2014
	 * @return: void
	 * @throws:  
	 * @param inSrc
	 * @param inDest
	 * @throws Exception
	 */
	public static void copyFile(FileInputStream inSrc, FileOutputStream inDest) throws Exception
	{
	    FileChannel inChannel = null;
	    FileChannel outChannel = null;
	    try
	    {
	    	inChannel = inSrc.getChannel();
	    	outChannel = inDest.getChannel();
	        inChannel.transferTo(0, inChannel.size(), outChannel);
	    }
	    finally
	    {
	    	if (inSrc != null) {
				inSrc.close();
			}
	    	if (inDest != null) {
	    		inDest.close();
			}
	        if (inChannel != null){
	            inChannel.close();
	        }
	        if (outChannel != null){
	        	outChannel.close();
	        }
	    }
	}
	/**
	 * check and load Icu lib
	 * @author: duongdt3
	 * @since: 09:26:34 13 Dec 2014
	 * @return: boolean
	 * @throws:  
	 * @param con
	 * @return
	 */
	public static boolean checkIcuLibVaild(Context con){
		boolean isHaveNativeLib = false;
		try {
			boolean systemICUFileExists = ExternalStorage.checkIcuLibInDir(ExternalStorage.SYSTEM_LIB_PATH);
			String icuRootPath = null;
			if (systemICUFileExists) {
				icuRootPath = ExternalStorage.SYSTEM_LIB_PATH;
			} else {
				//check exists icu file
				File appFilesDirectory = con.getFilesDir();
				boolean internalICUFileExists = ExternalStorage.checkIcuLibInDir(appFilesDirectory.getAbsolutePath());
				//icu internal exsits
				if (internalICUFileExists) {
					icuRootPath = appFilesDirectory.getAbsolutePath();
				} else{
					try {
						ExternalStorage.loadIcuLib(ExternalStorage.DMS_NATIVE_PATH, appFilesDirectory);
						icuRootPath = appFilesDirectory.getAbsolutePath();
					} catch (Exception e) {
						VTLog.e("load icu lub fail",VNMTraceUnexceptionLog.getReportFromThrowable(e));
					}
				}
			}
			
			if (icuRootPath != null) {
				// set thêm ICU path
				SQLiteDatabase.setICURoot(icuRootPath);
				isHaveNativeLib = true;
			} else {
				isHaveNativeLib = false;
			}
		} catch (Throwable e) {
			isHaveNativeLib = false;
			String log = ExternalStorage.DMS_NATIVE_PATH + "\r\n" + VNMTraceUnexceptionLog.getReportFromThrowable(e);
			VTLog.e("setICURoot fail", log);
			ServerLogger.sendLog("checkIcuLibVaild, setICURoot fail", log, false, TabletActionLogDTO.LOG_EXCEPTION);
		}
		return isHaveNativeLib;
	}
	/**
	 * check icu lib exists
	 * @author: duongdt3
	 * @since: 09:26:45 13 Dec 2014
	 * @return: boolean
	 * @throws:  
	 * @param dirPath
	 * @return
	 */
	public static boolean checkIcuLibInDir(String dirPath){
		boolean isICUFileExists = new File(dirPath + "/" + ICU_LIB_DIR + "/" + ICU_LIB_NAME).exists();
		return isICUFileExists;
	}
	/**
	 * load icu from external -> internal
	 * @author: duongdt3
	 * @since: 09:27:02 13 Dec 2014
	 * @return: void
	 * @throws:  
	 * @param srcPath
	 * @param descFile
	 * @throws Exception
	 */
	public static void loadIcuLib(String srcPath, File descFile) throws Exception{
		String fileSrcPath = srcPath + "/" + ICU_LIB_DIR + "/" + ICU_LIB_NAME;
		File icuFileExternal = new File(fileSrcPath);
		File icuDirInternal = null;
		if (icuFileExternal.exists()) {
			try {
				icuDirInternal = new File(descFile, ICU_LIB_DIR);
				if(!icuDirInternal.exists()){
					icuDirInternal.mkdirs();
				}
				File icuDataFileInternal = new File(icuDirInternal, ICU_LIB_NAME);
				FileOutputStream outIcuFileInternal = new FileOutputStream(icuDataFileInternal); 
				ExternalStorage.copyFile(new FileInputStream(icuFileExternal.getAbsolutePath()), outIcuFileInternal);
			} catch (Exception e) {
				try {
					if (icuDirInternal != null) {
						delete(icuDirInternal);
					}
				} catch (Exception e2) {
				}
				throw e;
			}
		} else{
			throw new FileNotFoundException("loadIcuLib: " + fileSrcPath + " not found");
		}
	}
	/**
	 * 
	 * @author: 
	 * @since: 15:10:37 01-04-2015
	 * @return: void
	 * @throws:  
	 * @param file
	 * @throws Exception
	 */
	public static void delete(File file) throws Exception {

		if (file.isDirectory()) {
			// directory is empty, then delete it
			if (file.list().length == 0) {
				file.delete();
			} else {

				// list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);

					// recursive delete
					delete(fileDelete);
				}

				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
				}
			}

		} else {
			// if file, then delete it
			file.delete();
		}
	}
	/**
	 * deleteNativeLibInternal
	 * @author: 
	 * @since: 15:46:29 06-04-2015
	 * @return: void
	 * @throws:  
	 * @param con
	 */
	private static void deleteNativeLibInternal(Context con) {
		try {
			File dir = con.getFilesDir();
			FileFilter fileFilter = new FileFilter() {
				
				@Override
				public boolean accept(File f) {
					return (f.isFile() && (f.getName().endsWith(".so") || f.getName().equals("libs.txt")) 
							|| (f.isDirectory() && ICU_LIB_DIR.equals(f.getName())));
				}
			};
			
			File[] listFile = dir.listFiles(fileFilter);
			for (File file : listFile) {
				delete(file);
			}
		} catch (Exception e) {
		}
	}
	/**
	 * delete native lib dir
	 * @author: duongdt3
	 * @since: 08:48:23 12 Dec 2014
	 * @return: void
	 * @throws:
	 */
	public static void deleteNativeLibPath(Context con) {
		try {
			delete(getNativeLibPath());
			deleteNativeLibInternal(con);
		} catch (Exception e) {
		}
	}
	/**
	 * get native path in SDCard
	 * @author: duongdt3
	 * @since: 14:01:45 3 Dec 2014
	 * @return: File
	 * @throws:  
	 * @return
	 */
	public static File getNativeLibPath() {
		File path = new File(DMS_NATIVE_PATH);
		if (!path.exists()) {
			path.mkdirs();
		}
		return path;
	}
	/**
	 * download native lib 
	 * @author: duongdt3
	 * @since: 14:01:16 3 Dec 2014
	 * @return: void
	 * @throws:  
	 * @param urlDownload
	 * @param con
	 * @throws Exception
	 */
	public static void downloadNativeLib(String urlDownload, Context con) throws Exception {
		VTLog.d("downloadNativeLib", "start");
		// xoa file db hien tai
		File zipDir = ExternalStorage.getNativeLibPath();
		// File path to store .zip file before unzipping
		File zipFile = new File(zipDir.getPath() + "/temp.zip");
		
		try {
			DownloadFile.downloadWithURLConnection(urlDownload, zipFile, zipDir);
			DownloadFile.unzipFile(con, zipFile, zipDir);
			copySDCardLib2Internal(con);
			VTLog.d("downloadNativeLib", "end");
		} catch (Exception e) {
			VTLog.e("downloadNativeLib", e.getMessage());
			throw e;
		} finally {
			try {
				if (zipFile.exists()) {
					zipFile.delete();
				}
			} catch (Exception e2) {
			}
		}
	}
	
	/**
	 * 
	 * get thư mục đương dẫn chưa file db download
	 * @return
	 */
	public static File getDirDBPath( Context context) {
		File path = new File(VNM_DATABASE_FOLDER);
		if (!path.exists()) {
			path.mkdir();
		}
		return path;
	}
	
	/**
	 * Lay danh sach file 
	 * @author: yennth16
	 * @since: 07:48:15 04-02-2015
	 * @return: ArrayList<File>
	 * @throws:  
	 * @param dir
	 * @return
	 */
	public static String getListFileName(File dir){
			ArrayList<String> allFile = new ArrayList<String>();
			String fileName = "";
	        File[] files = dir.listFiles();
	        for (File file : files) {
	            if(file.isFile()){
	            	allFile.add(file.getName()+",");
	            }else if(file.isDirectory()){
	            	getListFileName(file.getAbsoluteFile()); 
	            }
	        }
	        fileName = allFile.toString();
	        return fileName;
	}

	/**
	 * Get file apk sau khi download
	 *
	 * @author ThanhNN
	 * @param path
	 * @throws Exception
	 */
	public static String downloadApk(String path) throws Exception {
		String tempPath = null;
		File tempDir = new File(ExternalStorage.DMS_SABECO_FOLDER + "APP/");
		if (!tempDir.exists()) {
			tempDir.mkdirs();
		}
		File tempApk = new File(tempDir.getPath() + "/" + System.currentTimeMillis() + "_dms.apk");
		DownloadFile.downloadWithURLConnection(path, tempApk, tempDir);
		tempPath = tempApk.getAbsolutePath();
		return tempPath;
	}
}
