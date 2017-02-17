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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.zip.GZIPInputStream;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.FloatMath;
import android.widget.ImageView;

import com.commonsware.cwac.bus.AbstractBus;
import com.commonsware.cwac.task.AsyncTaskEx;
import com.commonsware.cwac.thumbnail.ThumbnailMessage;
import com.viettel.dms.constants.Constants;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * Simple Web Image Cache
 * SimpleWebImageCache.java
 * @version: 1.0 
 * @since:  08:20:07 20 Jan 2014
 * @param <B>
 * @param <M>
 */
@SuppressLint("FloatMath")
@SuppressWarnings("rawtypes")
public class SimpleWebImageCache<B extends AbstractBus, M> extends
		CacheBase<String, Drawable> {
	//10MB
	private static final long MAX_SIZE_IMAGE_CACHE = 10 * 1024 * 1024;
	// thoi gian de reload lai hinh anh
	protected static final long DELAY_TIME_TO_RELOAD_IMAGE = 2000;

	// thoi gian moi lan retry 3s
	public static final long TIME_DELAY_RETRY_LOAD_IMAGE = 1000;
	// so lan toi da retry
	public static final int MAX_NUM_TIMES_RETRY = 3;
	
	// megabyte
	// private static final String TAG="SimpleWebImageCache";
	protected B bus = null;
	// tham so policy de xoa cache
	private DiskCachePolicy mPolicy;
	// kich thuoc byte cua folder cache
	private long rootCacheSize;
	// so lan toi da de retry mot hinh anh
	protected int maxNumRetry = 0;
	// bien can de retry
	private boolean isRetry;

	static public File buildCachedImagePath(File cacheRoot, String url)
			throws Exception {
		return (new File(cacheRoot, md5(url)));
	}

	/**
	 * lay name image tu link url
	 * 
	 * @author: PhucNT
	 * @return: void
	 * @throws:
	 */
	static public String getNameImage(String s) throws Exception {
		if (s != null) {
			String[] token = s.split("/");
			if (token.length > 0)
				return token[token.length - 1];
		}
		return "";
	}

	static public String md5(String s) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");

		md.update(s.getBytes());

		byte digest[] = md.digest();
		StringBuffer result = new StringBuffer();

		for (int i = 0; i < digest.length; i++) {
			result.append(Integer.toHexString(0xFF & digest[i]));
		}

		return (result.toString());
	}

	public SimpleWebImageCache(File cacheRoot,
			AsyncCache.DiskCachePolicy policy, int maxSize, B bus) {
		super(cacheRoot, policy, maxSize);
		initCacheRoot(cacheRoot, policy);
		this.bus = bus;
	}

	/**
	 * set co su dung cache hinh anh hay khong
	 * 
	 * @author: PhucNT
	 * @return: void
	 * @throws:
	 */
	public void setUserCacheRoot(boolean isUse) {
		if (!isUse) {
			cacheRoot = null;
			setCacheRoot(cacheRoot);
		}
	}

	/**
	 * set retry de lay hinh anh trong mot time delay maxNumRetry so lan toi da
	 * de lay hinh anh
	 * 
	 * @author: PhucNT
	 * @return: void
	 * @throws:
	 */

	public void setRetryLoadImage(boolean isRetry) {
		if (isRetry)
			maxNumRetry = MAX_NUM_TIMES_RETRY;
	}

	/**
	 * Tao duong dan thu muc chua anh cache va xoa thu muc neu so luong file
	 * vuot qua so luong cho phep
	 * 
	 * @author: KunKun
	 * @return: void
	 * @throws:
	 */
	private void initCacheRoot(File cacheRoot, AsyncCache.DiskCachePolicy policy) {
		// TODO Auto-generated method stub
		// khoi tao quyen de xoa file
		mPolicy = new DiskCachePolicy() {
			@Override
			public boolean eject(File cachedFile) {
				// TODO Auto-generated method stub
				return true;
			}
		};
		cacheRoot = MemoryUtils.getInstance().cacheRootPath;
		setCacheRoot(cacheRoot);
		rootCacheSize = MemoryUtils.getInstance().rootCacheSize;
	}

	@Override
	public int getStatus(String key) {
		int result = super.getStatus(key);

		if (result == CACHE_NONE && getCacheRoot() != null) {
			try {
				File cache = buildCachedImagePath(key);
				if (cache.exists()) {
					result = CACHE_DISK;
				}
				File fielLocal = new File(key);
				if (fielLocal.exists()){
					result = CACHE_PATH_DISK;
				}
			} catch (Throwable t) {
				// VTLog.e(TAG, "Exception getting cache status", t);
			}
		}

		return (result);
	}

	public File buildCachedImagePath(String url) throws Exception {
		if (getCacheRoot() == null) {
			return (null);
		}

		return (buildCachedImagePath(getCacheRoot(), url));
	}

	@SuppressWarnings("unchecked")
	public void notify(String key, ThumbnailMessage message) throws Exception {
		int status = getStatus(key);
		if (status == CACHE_PATH_DISK) {
			new LoadImageTask()
			.execute(message, key,new File(key));
		}
		else if (status == CACHE_NONE) {
			VTLog.e("ImageCache", "-----------CACHE_NONECACHE_NONECACHE_NONE");
			new FetchImageTask().execute(message, key,
					buildCachedImagePath(key));
		} else if (status == CACHE_DISK) {
			VTLog.e("ImageCache",
					"-----------CACHE_DISKCACHE_DISKCACHE_DISKCACHE_DISK");
			new LoadImageTask()
					.execute(message, key, buildCachedImagePath(key));
		} else {
			VTLog.e("ImageCache", "-----------MEMORYMEMORYMEMORYMEMORY");
			bus.send(message);
		}
	}

	public B getBus() {
		return (bus);
	}

	class FetchImageTask extends AsyncTaskEx<Object, Object, Void> {
		@SuppressWarnings({ "unchecked" })
		@Override
		protected Void doInBackground(Object... params) {
			String url = params[1].toString();
			File cache = (File) params[2];

			URLConnection connection = null;
			InputStream stream = null;

			int countRetry = 0;
			ThumbnailMessage message = (ThumbnailMessage) params[0];
			do {
				countRetry++;
				isRetry = false;
				try {
					VTLog.i("ImageCache", "---------> countRetry: "
							+ countRetry + " - url :" + url);
					connection = new URL(url).openConnection();

					String contentEncoding = connection.getContentEncoding();
					if (contentEncoding != null
							&& contentEncoding.equals("gzip")) {
						stream = new GZIPInputStream(
								connection.getInputStream());
					} else {
						stream = connection.getInputStream();
					}
					contentEncoding = null;

					ByteArrayOutputStream out = new ByteArrayOutputStream();
					int read;
					byte[] b = new byte[4096];

					while ((read = stream.read(b)) != -1) {
						out.write(b, 0, read);
					}
					out.flush();
					out.close();

					byte[] raw = out.toByteArray();

					BitmapFactory.Options resample = new BitmapFactory.Options();
					resample.inJustDecodeBounds = false;
					resample.inSampleSize = computeSampleSize(raw,
							Constants.MAX_THUMB_IMAGE_WIDTH, Constants.MAX_THUMB_IMAGE_HEIGHT);
					Bitmap bitmap = BitmapFactory.decodeByteArray(raw, 0,
							raw.length, resample);

					// if (!isIconCategory(getNameImage(url))) {
					// ByteArrayOutputStream baos = new ByteArrayOutputStream();
					// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					// raw = baos.toByteArray();
					// }
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					boolean success=bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					if (success)
						raw = baos.toByteArray();
					else
						VTLog.i("ImageCache", " compress bitmap fail ");
					
					put(url, new BitmapDrawable(bitmap));

					message = (ThumbnailMessage) params[0];

					if (message != null) {
						bus.send(message);

					}

					if (cache != null && success) {
						FileOutputStream file = new FileOutputStream(cache);
						// bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
						// file);
						file.write(raw);
						VTLog.i("ImageCache", " write on disk successful");
						file.flush();
						file.close();
						checkCleanCache(cache);
					}
					bitmap = null;

				} catch (OutOfMemoryError out) {
					VTLog.i("MEMORY", " SIMPLE WEB IMAGE CACHE");
					SimpleWebImageCache.this.cache.clear();
					System.gc();
					System.runFinalization();
					System.gc();
				} catch (FileNotFoundException e) {
					isRetry = false;
					
//					isRetry = true;
//					VTLog.i("ImageCache",
//							" FileNotFoundException url = " +url+" path "+ cache.getAbsolutePath());
//					// neu cache khong ton tai thi khong retry
//					try {
//						@SuppressWarnings("resource")
//						FileOutputStream file = new FileOutputStream(cache);
//					} catch (FileNotFoundException e2) {
//						// TODO Auto-generated catch block
//						isRetry = false;
//					}

//					try {
// 						Thread.sleep(TIME_DELAY_RETRY_LOAD_IMAGE);
//					} catch (InterruptedException e1) {
//						VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e1));
//					}
					VTLog.i("ImageCache", " FileNotFoundException isRetry = " + e.toString());
				} catch (IOException e) {

					VTLog.i("ImageCache",
							" FileNotFoundException isRetry = " + isRetry);

				} catch (Exception e) {
					VTLog
							.i("ImageCache", " Exception isRetry = " + isRetry);

				} catch (Throwable t) {

					message = (ThumbnailMessage) params[0];

					if (message != null) {
						bus.send(message);
					}
					VTLog
							.i("ImageCache", " Throwable isRetry = " + isRetry);
				} finally {
					if (stream != null) {
						try {
							stream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
						}
					}
				}
			} while (isRetry && countRetry <= maxNumRetry);
			return (null);
		}


		@Override
		protected void onProgressUpdate(Object... values) {
			// TODO Auto-generated method stub
			// super.onProgressUpdate(values);
			ThumbnailMessage message = (ThumbnailMessage) values[0];

			if (message != null) {
				ImageView image = (ImageView) message.getImageView();
				Bitmap bm = (Bitmap) values[1];
				if (bm != null)
					image.setImageBitmap(bm);
			}
		}
	}

	public class LoadImageTask extends AsyncTaskEx<Object, Void, Void> {
		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(Object... params) {
			String url = params[1].toString();
			File cache = (File) params[2];
			ThumbnailMessage message = (ThumbnailMessage) params[0];
			try {
				BitmapFactory.Options resample = new BitmapFactory.Options();
				resample.inJustDecodeBounds = false;
				resample.inSampleSize = computeSampleSizeFromFile(cache,
						Constants.MAX_THUMB_IMAGE_WIDTH, Constants.MAX_THUMB_IMAGE_HEIGHT);
				Bitmap bmp = BitmapFactory.decodeFile(cache.getAbsolutePath(),resample);
				if (bmp != null) {
					VTLog.i("ImageCache", " LoadImageTask  " + url);
					put(url, new BitmapDrawable(bmp));

					if (params[0] != null) {
						bus.send(params[0]);
					}
				}else {
					// neu hinh khong co trong sdcard load lai tu tren mang
					VTLog.i("ImageCache", " load lai ");
					new FetchImageTask().execute(message, url,
							buildCachedImagePath(url));
				}
				bmp = null;
			} catch (Throwable t) {
				 VTLog.e("PhucNT4", "Exception downloading image", t);
			}

			return (null);
		}
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

		if (bitmap != null) {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect1 = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			// final Rect rect2 = new Rect(0, 0, bitmap.getWidth() + 4, bitmap
			// .getHeight() + 4);
			// final RectF rectF = new RectF(rect2);
			// final float roundPx = 0;

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			// paint.setStrokeWidth(1);
			canvas.drawColor(Color.WHITE);
			// paint.setStyle(Paint.Style.STROKE);
			// canvas.drawRect(rectF, paint);

			// phuc paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect1, paint);

			return output;
		} else {
			return bitmap;
		}
	}

	/**
	 * tinh sample size tu chuoi byte 
	 * 
	 * @author: PhucNT
	 * @return
	 * @return: int
	 * @throws:
	 */
	public int computeSampleSize(byte[] raw, int maxWidth, int maxHeight) {
		// TODO Auto-generated method stub
		if (maxWidth == 0 || maxHeight == 0)
			return 1;
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(raw, 0, raw.length, bmpFactoryOptions);
		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
				/ (float) maxHeight);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
				/ (float) maxWidth);
		// If both of the ratios are greater than 1,
		// one of the sides of the image is greater than the screen
		bmpFactoryOptions.inSampleSize = 1;
		if (heightRatio > 1 && widthRatio > 1) {
			if (heightRatio > widthRatio) {
				// Height ratio is larger, scale according to it
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				// Width ratio is larger, scale according to it
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}

		VTLog.e("BigPhotoCache", "bmpFactoryOptions.inSampleSize "
				+ bmpFactoryOptions.inSampleSize + "   " + maxWidth + "   "
				+ maxHeight);
	
		return bmpFactoryOptions.inSampleSize;
	}

	/**
	 * tinh sample size tu file
	 * 
	 * @author: PhucNT
	 * @return
	 * @return: int
	 * @throws:
	 */
	public static int computeSampleSizeFromFile(File file, int maxWidth, int maxHeight) {
		// TODO Auto-generated method stub
		if (maxWidth == 0 || maxHeight == 0)
			return 1;
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getAbsolutePath(), bmpFactoryOptions);
		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
				/ (float) maxHeight);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
				/ (float) maxWidth);
		// If both of the ratios are greater than 1,
		// one of the sides of the image is greater than the screen
		bmpFactoryOptions.inSampleSize = 1;
		if (heightRatio > 1 && widthRatio > 1) {
			if (heightRatio > widthRatio) {
				// Height ratio is larger, scale according to it
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				// Width ratio is larger, scale according to it
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}
		VTLog.e("BigPhotoCache", "bmpFactoryOptions.inSampleSize "
				+ bmpFactoryOptions.inSampleSize);

		return bmpFactoryOptions.inSampleSize;
	}

	
	/**
	 * Xoa nhung hinh anh nho neu vuot qua so luong cho phep
	 * 
	 * @author: PhucNT6
	 * @return: void
	 * @throws:
	 */
	protected void checkCleanCache(File file) {
		// TODO Auto-generated method stub
		if (cacheRoot != null) {
			rootCacheSize += file.length();
			MemoryUtils.getInstance().rootCacheSize = rootCacheSize;
			VTLog.e("ImageCache", "kich thuoc vung nho cache "
					+ (rootCacheSize) + " mb");
			if (rootCacheSize > MAX_SIZE_IMAGE_CACHE) {
				rootCacheSize = 0;
				MemoryUtils.getInstance().rootCacheSize = 0;
				new CacheCleanTask().execute(mPolicy);
			}
		}
	}

}
