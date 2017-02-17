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
package com.commonsware.cwac.thumbnail;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.commonsware.cwac.adapter.AdapterWrapper;
import com.commonsware.cwac.cache.SimpleWebImageCache;
import com.viettel.dms.constants.Constants;
import com.viettel.dms.util.StringUtil;
import com.viettel.utils.VTLog;

/**
 * Thumbnail Adapter
 * ThumbnailAdapter.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  08:22:26 20 Jan 2014
 */
public class ThumbnailAdapter extends AdapterWrapper implements
		OnScrollListener {
	private static final String TAG = "ThumbnailAdapter";
	private int[] imageIds;
	private SimpleWebImageCache<ThumbnailBus, ThumbnailMessage> cache = null;
	private Activity host = null;
	@SuppressWarnings("unused")
	private boolean mBusy;
	private ListView listView=null;
	
	// Bitmap bdRounded;

	/**
	 * Constructor wrapping a supplied ListAdapter
	 */
	public ThumbnailAdapter(Activity host, ListAdapter wrapped,
			SimpleWebImageCache<ThumbnailBus, ThumbnailMessage> cache,
			int[] imageIds) {
		super(wrapped);

		this.host = host;
		this.imageIds = imageIds;
		this.cache = cache;
		cache.getBus().register(getBusKey(), onCache);

	}
	/**
	 * Constructor wrapping a supplied ListAdapter, add list view
	 */
	public ThumbnailAdapter(Activity host,ListView list, ListAdapter wrapped,
			SimpleWebImageCache<ThumbnailBus, ThumbnailMessage> cache,
			int[] imageIds) {
		super(wrapped);

		this.host = host;
		this.imageIds = imageIds;
		this.cache = cache;
		this.listView = list;
		if (listView!=null)
			listView.setOnScrollListener(this);
		cache.getBus().register(getBusKey(), onCache);

	}
	public void setUserCacheRoot(boolean isUse) {				
		cache.setUserCacheRoot(isUse);
	}
	public void setRetryLoadImage(boolean isRetry) {
		cache.setRetryLoadImage(isRetry);
	}
	public void close() {
		cache.getBus().unregister(onCache);
	}

	public void clearCache(String key) {
		cache.remove(key);
		
	}
	public void recycleAllBitmaps() {
		cache.recycleAllBitmaps(true);
	}

	/**
	 * Get a View that displays the data at the specified position in the data
	 * set. In this case, if we are at the end of the list and we are still in
	 * append mode, we ask for a pending view and return it, plus kick off the
	 * background task to append more data to the wrapped adapter.
	 * 
	 * @param position
	 *            Position of the item whose data we want
	 * @param convertView
	 *            View to recycle, if not null
	 * @param parent
	 *            ViewGroup containing the returned View
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View result = super.getView(position, convertView, parent);
		//if (!mBusy)
		processView(result); 
		return (result);
	}

	public void processView(View row) {
		if (row != null) {
			for (int imageId : imageIds) {
				ImageView image = (ImageView) row.findViewById(imageId);
				VTLog.i("ImageCache", " processView  " +image );
				if (image != null)
					VTLog.i("ImageCache", " image.getTag" + image.getTag());
				String url = Constants.STR_BLANK;
				if (image != null && image.getTag() != null) {
					url = image.getTag().toString();
				}
				if (image != null && !StringUtil.isNullOrEmpty(url)) {
					ThumbnailMessage msg = cache.getBus().createMessage(getBusKey());  
					msg.setImageView(image); 
					VTLog.i("ImageCache"," image.getWidth() "+image.getMeasuredWidth());
					msg.setUrl(url);

					try {
						cache.notify(msg.getUrl(), msg);
					} catch (Throwable t) {
						VTLog.e(TAG, "Exception trying to fetch image"+t);
					}

				}
			}
		}
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_IDLE:
			mBusy = false;

			int first = view.getFirstVisiblePosition();
			int count = view.getChildCount();
			VTLog.e("KunKun", "SCROLL_STATE_IDLE " + first);
			for (int i = 0; i < count; i++) {
				processView(view.getChildAt(i));
			}
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			mBusy = true;
			VTLog.e("KunKun", "SCROLL_STATE_TOUCH_SCROLL ");
			break;
		case OnScrollListener.SCROLL_STATE_FLING:
			mBusy = true;
			VTLog.e("KunKun", "SCROLL_STATE_FLING ");
			break;
		}
	}

	private String getBusKey() {
		return (toString());
	}

	private ThumbnailBus.Receiver<ThumbnailMessage> onCache = new ThumbnailBus.Receiver<ThumbnailMessage>() {
		public void onReceive(final ThumbnailMessage message) {
			final ImageView image = message.getImageView();

			/**
			 * @author: HaiTC
			 * @since: June 28, 2011
			 * @Description: lay progressbar khi load hinh anh
			 */
			// final ProgressBar pbLoading = message.getPbLoading();
			// final TextView tvNotifyNonImage = message.getTvNotifyNonImage();
			//VTLog.e("BigPhotoCache", "dang recylcling  " + cache.isRecycling );
			if (cache.isRecycling){
				//VTLog.e("BigPhotoCache", "dang recylcling  " );
				return;
			}
			host.runOnUiThread(new Runnable() {
				public void run() {
					
					if (image.getTag() != null
							&& image.getTag().toString()
									.equals(message.getUrl())) {
						
						BitmapDrawable bd = (BitmapDrawable) cache.get(message
								.getUrl());
						VTLog.i("ImageCache", " onReceive " +message.getUrl()+" bd " +bd);
						try {
							if (bd != null ) {	
								
								image.setImageDrawable(bd);
							}
						} catch (RuntimeException e) {
							// TODO Auto-generated catch block
							VTLog.e("BigPhotoCache", "try catch recyle bitmap " );
						}catch (Throwable t) {
							// TODO Auto-generated catch block
							VTLog.e("BigPhotoCache", "try catch recyle bitmap " );
						}
					}
				}
			});
		}
	};

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

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect1, paint);

			return output;
		} else {
			return bitmap;
		}
	}

}
