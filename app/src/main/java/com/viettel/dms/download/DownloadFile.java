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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;

import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.network.http.HTTPClient;

/**
 * Utility methods for downloading files.
 * 
 * @author portuesi
 *
 */
public class DownloadFile {
	
    public static final int BUFFER_SIZE = 8 * 1024;
    public static final int CONNECT_TIMEOUT = 480000;//16phut
    public static final int READ_TIMEOUT = 480000;//16phut
	private static long fileSize = 0;

	/**
	 * Download a file from a URL somewhere.  The download is atomic;
	 * that is, it downloads to inta temporary file, then renames it to
	 * the requested file name only if the download successfully
	 * completes.
	 * 
	 * Returns TRUE if download succeeds, FALSE otherwise.
	 * 
	 * @param url				Source URL
	 * @param output			Path to output file
	 * @param tmpDir			Place to put file download in progress
	 */
	public static void download( String url, File output, File tmpDir ) {
		InputStream is = null;
		OutputStream os = null;
		File tmp = null;
		try {
			VTLog.i("DownloadDB", "Downloading url :" + url);
			tmp = File.createTempFile( "download", ".tmp", tmpDir );
			is = new URL(url).openStream();
			os = new BufferedOutputStream( new FileOutputStream( tmp ) );
			copyStream( is, os );
			tmp.renameTo( output );
			tmp = null;
		} catch ( IOException e ) {		
			VTLog.e("DownloadDB", "Loi download file db "+e.toString());
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			throw new RuntimeException( e );
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			throw new RuntimeException( e );
		}finally {
			if ( tmp != null ) { try { tmp.delete(); tmp = null; } catch (Exception ignore) {;} }
			if ( is != null  ) { try { is.close();   is = null;  } catch (Exception ignore) {;} }
			if ( os != null  ) { try { os.close();   os = null;  } catch (Exception ignore) {;} }
		}
	}
	
	
	/**
	 * Download file with urlConnection
	 * @author : BangHN
	 * since : 1.0
	 */
	public static void downloadWithURLConnection( String url, File output, File tmpDir ) {
		BufferedOutputStream os = null;
		BufferedInputStream is = null;
		File tmp = null;
		try {
			VTLog.i("Download ZIPFile", "Downloading url :" + url);
			
			tmp = File.createTempFile( "download", ".tmp", tmpDir );
			URL urlDownload = new URL(url);
			URLConnection cn = urlDownload.openConnection();
			cn.addRequestProperty("session", HTTPClient.sessionID);
			cn.setConnectTimeout(CONNECT_TIMEOUT);
			cn.setReadTimeout(READ_TIMEOUT);
			cn.connect();
			is = new BufferedInputStream(cn.getInputStream());
			os = new BufferedOutputStream( new FileOutputStream( tmp ) );
			//cập nhật dung lượng tập tin request
			fileSize = cn.getContentLength();
			//vẫn có trường hợp không có ContentLength
			if (fileSize < 0) {
				//mặc định = 4 MB
				fileSize = 4 * 1024 * 1024;
			}
			copyStream( is, os );
			tmp.renameTo( output );
			tmp = null;
		} catch ( IOException e ) {		
			ServerLogger.sendLog("Download ZIPFile", e.getMessage() + "\n" + e.toString() + "\n" + url,
					false, TabletActionLogDTO.LOG_EXCEPTION);
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			throw new RuntimeException( e );
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			ServerLogger.sendLog("Download ZIPFile", e.getMessage() + "\n" + e.toString() + "\n" + url,
					false, TabletActionLogDTO.LOG_EXCEPTION);
			throw new RuntimeException( e );
		}finally {
			if ( tmp != null ) { try { tmp.delete(); tmp = null; } catch (Exception ignore) {;} }
			if ( is != null  ) { try { is.close();   is = null;  } catch (Exception ignore) {;} }
			if ( os != null  ) { try { os.close();   os = null;  } catch (Exception ignore) {;} }
		}
	}
	
	
	/**
	 * Download file su dung httpclient request
	 * @param url
	 * @param output
	 * @param tmpDir
	 */
	public static void downloadWithHTTPClient( String url, File output, File tmpDir ){
		File tmp = null;
		HttpClient httpclient = new DefaultHttpClient();
		BufferedOutputStream os = null;
		BufferedInputStream is = null;
		try {
			VTLog.i("DownloadDB", "Downloading url :" + url);
			
			tmp = File.createTempFile("download", ".tmp", tmpDir);
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				is = new BufferedInputStream(entity.getContent());
				os = new BufferedOutputStream(
						new FileOutputStream(tmp));

				copyStream(is, os );
				tmp.renameTo( output );
				tmp = null;
				httpclient.getConnectionManager().shutdown();
			}
		} catch ( IOException e ) {		
			VTLog.e("DownloadDB", "Loi download file db "+e.toString());
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			throw new RuntimeException( e );
		} catch (Exception e) {
			VTLog.e("DownloadDB", "Loi download file db "+e.toString());
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			throw new RuntimeException( e );
		}finally {
			if ( tmp != null ) { try { tmp.delete(); tmp = null; } catch (Exception ignore) {;} }
			if ( is != null  ) { try { is.close();   is = null;  } catch (Exception ignore) {;} }
			if ( os != null  ) { try { os.close();   os = null;  } catch (Exception ignore) {;} }
		}
	}
	
	
	
	/**
	 * Copy from one stream to another.  Throws IOException in the event of error
	 * (for example, SD card is full)
	 * 
	 * @param is		Input stream.
	 * @param os		Output stream.
	 */
	public static void copyStream( InputStream is, OutputStream os ) throws IOException {
		byte[] buffer = new byte[ BUFFER_SIZE ];
		copyStream( is, os, buffer, BUFFER_SIZE );
    }
	
	public static void copyStream( BufferedInputStream is, BufferedOutputStream os ) throws IOException {
		byte[] buffer = new byte[ BUFFER_SIZE ];
		copyStream( is, os, buffer, BUFFER_SIZE );
	}

	/**
	 * Copy from one stream to another.  Throws IOException in the event of error
	 * (for example, SD card is full)
	 * 
	 * @param is			Input stream.
	 * @param os			Output stream.
	 * @param buffer		Temporary buffer to use for copy.
	 * @param bufferSize	Size of temporary buffer, in bytes.
	 */
	public static void copyStream( BufferedInputStream is, BufferedOutputStream os,
								      byte[] buffer, int bufferSize ) throws IOException {
		int sizeDownloaded = 0;
		int percent = 0;
		try {
			for (;;) {
				int count = is.read( buffer, 0, bufferSize );
				if (count >= 0) {
					sizeDownloaded += count;
					percent = (int) ((float) (sizeDownloaded) / (float) (fileSize) * 100);
					VTLog.d("copyStream", "current count: " + count + " sizeDownloaded: "
							+ sizeDownloaded + " fileSize: " + fileSize + " percent: " + percent);
					if (percent > 98) {
						percent = 98;
					}
					try {
						Context ct = GlobalInfo.getInstance().getActivityContext();
						if (ct != null && ct instanceof GlobalBaseActivity) {
							((GlobalBaseActivity) ct).updateProgressPercentDialog(percent);
						}
					} catch(Exception e){
						VTLog.e("copyStream", "fail", e);
					}
				}
				if ( count == -1 ) { break; }
				os.write(buffer, 0, count);
			}
			os.flush();
		} catch ( IOException e ) {
			throw e;
		}
	}
	public static void copyStream( InputStream is, OutputStream os,
			byte[] buffer, int bufferSize ) throws IOException {
		try {
			for (;;) {
				int count = is.read( buffer, 0, bufferSize );
				if ( count == -1 ) { break; }
				os.write( buffer, 0, count );
			}
			os.flush();
		} catch ( IOException e ) {
			throw e;
		}
	}
	
	//////////////////////////////////////////////////////////////////////////
	// Zip Extraction
	//////////////////////////////////////////////////////////////////////////

	/**
	 * Unpack .zip file.
	 * 
	 * @param zipFile
	 * @param destination
	 */
	public static void unzipFile(Context context, File zipFile, File destination ) {
		DecompressZip decomp = new DecompressZip( zipFile.getPath(), 
				 destination.getPath() + File.separator );
		decomp.unzip(context);
		
		//copySqlFileFromAnotherDir(context);
		
	}
	
	
//	public static void copySqlFileFromAnotherDir(Context context) {
//		// TODO Auto-generated method stub
//		// File (or directory) to be moved
//		
//		File src = new File("/mnt/sdcard/demo.db");
//		VTLog.e("PhucNT4",
//				"file database exist " + src.exists() + " name "
//						+ src.getName());
//		// Destination directory
//		String database = ExternalStorage.getDatabasePath(context).getAbsolutePath()+"/"+Constants.DATABASE_NAME;
//		File dst = new File(database);
//		VTLog.e("PhucNT4", "folder database exist " + dst.exists());
//		
//		try {
//			copy(src, dst);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
//		}
//	}

	// Copies src file to dst file.
	// If the dst file does not exist, it is created
	public static void copy(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	/**
	 * Giai nen file ma hoa voi password
	 * @author: ThangNV
	 * @since: 09:12:59 12-09-2014
	 * @return: void
	 * @throws:  
	 * @param source
	 * @param destination
	 * @param password
	 */
	public static void unzipFileWithPass(String source, String destination, String password) {
		try {
		    ZipFile zipFile = new ZipFile(source);
		    if (zipFile.isEncrypted()) {
		        zipFile.setPassword(password);
		    }
		    zipFile.extractAll(destination);
		} catch (ZipException e) {
			VTLog.printStackTrace(e);
		}
	}
	
	/**
	 * Nén 1 file với pass
	 * @author: duongdt3
	 * @since: 09:16:05 29 Jan 2015
	 * @return: void
	 * @throws:  
	 * @param filePath địa chỉ file cần nén
	 * @param zipPath địa chỉ file zip
	 * @param passZip mật khẩu nén (nếu null hoặc empty là không có pass) 
	 * @throws Exception
	 */
	public static void zipFileWithPass(String filePath, String zipPath, String passZip) throws Exception{
		zipFileWithPass(Arrays.asList(filePath), zipPath, passZip);
	}
	
	/**
	 * Nén nhiều file với pass 
	 * @author: duongdt3
	 * @since: 09:11:00 29 Jan 2015
	 * @return: void
	 * @throws:  
	 * @param filePaths danh sách địa chỉ file cần nén
	 * @param zipPath địa chỉ file zip
	 * @param passZip mật khẩu nén (nếu null hoặc empty là không có pass) 
	 * @throws Exception
	 */
	public static void zipFileWithPass(List<String> filePaths, String zipPath, String passZip) throws Exception{
		try {
            // Initiate ZipFile object with the path/name of the zip file.
            ZipFile zipFile = new ZipFile(zipPath);

            // Build the list of files to be added in the array list
            // Objects of type File have to be added to the ArrayList
            ArrayList<File> filesToAdd = new ArrayList<File>();
            for (String fPath : filePaths) {
            	File f = new File(fPath);
            	if (!f.exists()) {
					throw new FileNotFoundException();
				}
            	filesToAdd.add(f);
			}

            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); 

            // Set the compression level
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); 

            if (!StringUtil.isNullOrEmpty(passZip)) {
            	// Set the encryption flag to true
            	// If this is set to false, then the rest of encryption properties are ignored
            	parameters.setEncryptFiles(true);
            	
            	// Set the encryption method to Standard Zip Encryption
            	parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            	
            	// Set password
            	parameters.setPassword(passZip);
			}
            zipFile.addFiles(filesToAdd, parameters);
        } catch (Exception e){
        	VTLog.e("", VNMTraceUnexceptionLog.getReportFromThrowable(e));
            throw e;
        }
	}
}
