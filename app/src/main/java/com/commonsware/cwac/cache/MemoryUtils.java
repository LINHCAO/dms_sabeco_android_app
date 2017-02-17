/***
	Copyright (c) 2008-2009 CommonsWare, LLC
	
	Licensed under the Apache License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may obtain
	a copy of the License at
		http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package com.commonsware.cwac.cache;

import java.io.File;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;

import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;


/**
 * Quan ly dung luong bo nho external and internal
 * 
 * @author: PhucNT
 * @version: 1.1
 * @since: Oct 26, 2011
 */
public class MemoryUtils {
	private static final int TYPE_CACHE_EXTERNAL = 0;
	private static final int TYPE_CACHE_INTERNAL = 1;
	private static final float ALLOWED_MEMORY_SIZE = 20;
	private static String FODER_CACHE = "DMSSABECO";
	private static String IMAGE_CACHE = "IMAGE_CACHE";
	// private CacheCleanTask cacheCleanCache = new CacheCleanTask();
	public File cacheRootPath = null;
	public long rootCacheSize = 0;
	private static MemoryUtils instance = null;

	public static MemoryUtils getInstance() {
		if (instance == null) {
			instance = new MemoryUtils();
			instance.initialize();
		}
		return instance;
	}

	public static void setNullInstance() {
		instance = null;
	}

	private void initialize() {
		// Config custom message
		cacheRootPath = initCacheRoot();
		rootCacheSize = getFileSize(cacheRootPath);
	}

	/**
	 * init Cache root
	 * 
	 * @author: PhucNT6
	 * @return: void
	 * @param: file
	 * @throws:
	 */
	public File initCacheRoot() {
		File cacheRoot = null;
		float memAvailExternal = megabytesAvailable(new File(Environment
				.getExternalStorageDirectory().getPath()));
		VTLog.e("PhucNT4", "dung luong external available "
				+ memAvailExternal);
		float memAvailInternal = megabytesAvailable(new File(Environment
				.getDataDirectory().getPath()));
		VTLog.e("PhucNT4", "dung luong internal available "
				+ memAvailInternal);
		File rootExternal = getImageCachePath(TYPE_CACHE_EXTERNAL);
		File rootInternal = getImageCachePath(TYPE_CACHE_INTERNAL);
		if (isSdPresent() && (memAvailExternal > ALLOWED_MEMORY_SIZE)) {
			cacheRoot = rootExternal;
		} else if (memAvailInternal > ALLOWED_MEMORY_SIZE) {
			cacheRoot = rootInternal;
			if (rootExternal != null && rootExternal.exists())
				new CacheCleanTask().execute(rootExternal);
		} else {
			cacheRoot = null;
			if (rootExternal != null && rootExternal.exists())
				new CacheCleanTask().execute(rootExternal);
			if (rootInternal != null && rootInternal.exists())
				new CacheCleanTask().execute(rootInternal);
		}
		if (cacheRoot != null)
			VTLog.e("PhucNT4", "thu muc cache root " + cacheRoot.getPath());

		return cacheRoot;
	}

	/**
	 * Check dung luong bo nho free
	 * 
	 * @author: PhucNT6
	 * @return: void
	 * @param: file
	 * @throws:
	 */
	public static float megabytesAvailable(File f) {
		StatFs stat = new StatFs(f.getPath());
		long bytesAvailable = (long) stat.getBlockSize()
				* (long) stat.getAvailableBlocks();
		return bytesAvailable / (1024.f * 1024.f);
	}

	public static boolean isSdPresent() {

		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);

	}

	/**
	 * Tao file duong dan thu muc cach image
	 * 
	 * @author: PhucNT6
	 * @param: isExternal lay thu muc duong dan trong the nho hay la lay trong
	 *         bo nho trong
	 * @return: void
	 * @throws:
	 */
	protected static File initKunKunImageCacheFolder(int typeCache) {
		String extStorageDirectory = null;
		File folderRoot = null;
		try {
			if (typeCache == TYPE_CACHE_EXTERNAL)
				extStorageDirectory = Environment.getExternalStorageDirectory()
						.toString();
			if (typeCache == TYPE_CACHE_INTERNAL)
				extStorageDirectory = GlobalInfo.getInstance().getAppContext()
						.getCacheDir().toString();
			folderRoot = new File(extStorageDirectory, FODER_CACHE);
			if (!folderRoot.exists()) {
				if (folderRoot.mkdir()) {
				} else {
					folderRoot = null;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return folderRoot;
	}

	/**
	 * Tao file duong dan thu muc cach image cho nhung anh nho
	 * 
	 * @author: PhucNT6
	 * @return: void
	 * @throws:
	 */
	private File getImageCachePath(int typeCache) {
		File cache = initKunKunImageCacheFolder(typeCache);
		if (cache == null)
			return null;
		String server_revision = "version_1";//GlobalInfo.getInstance().getPackageName();
		File fileCacheToDisk = new File(cache.getAbsolutePath(), IMAGE_CACHE
				+ "_" + server_revision);
		
		if (!fileCacheToDisk.exists()) {
			deleteDir(new File(cache.getAbsolutePath()));
			if (fileCacheToDisk.mkdir()) {
			} else {
				fileCacheToDisk = null;
			}
		}
		return fileCacheToDisk;
	}

	public void deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				if (children[i].contains(IMAGE_CACHE))	{	
					new CacheCleanTask().execute(new File(dir,children[i]));
					(new File(dir,children[i])).delete();
				}
				
			}
		}
	}

	/**
	 * Get size of folder root
	 * 
	 * @author: PhucNT6
	 * @return: void
	 * @throws:
	 */
	public long getFileSize(File folder) {

		if (folder == null)
			return 0;
		VTLog.e("PhucNT4", "Folder: " + folder.getName());
		long foldersize = 0;

		File[] filelist = folder.listFiles();
		for (int i = 0; i < filelist.length; i++) {
			if (filelist[i].isDirectory()) {
				foldersize += getFileSize(filelist[i]);
			} else {

				foldersize += filelist[i].length();
			}
		}
		return foldersize;
	}

	/**
	 * 
	 * Xoa cache
	 * 
	 * @author: DoanDM
	 * @return
	 * @return: CacheCleanTask
	 * @throws:
	 */
	public CacheCleanTask getCacheCleanTask() {
		return new CacheCleanTask();
	}

	public class CacheCleanTask extends AsyncTask<File, Void, Void> {

		@Override
		protected Void doInBackground(File... files) {
			try {
				walkDir(files[0]);
			} catch (Throwable t) {
				VTLog.e("PhucNT4", "Exception cleaning cache", t);
			}
			return null;
		}

		void walkDir(File dir) {
			if (dir.isDirectory()) {
				String[] children = dir.list();

				for (int i = 0; i < children.length; i++) {
					walkDir(new File(dir, children[i]));
				}
			} else {
				dir.delete();
			}
		}

	}

}
