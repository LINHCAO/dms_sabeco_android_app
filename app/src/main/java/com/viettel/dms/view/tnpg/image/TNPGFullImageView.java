/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tnpg.image;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.commonsware.cwac.cache.BigPhotoCache;
import com.commonsware.cwac.cache.CacheBase.DiskCachePolicy;
import com.commonsware.cwac.cache.SimpleWebImageCache;
import com.commonsware.cwac.thumbnail.ThumbnailAdapter;
import com.commonsware.cwac.thumbnail.ThumbnailBus;
import com.commonsware.cwac.thumbnail.ThumbnailMessage;
import com.viettel.dms.adapter.ThumbnailImageAdapter;
import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.download.ExternalStorage;
import com.viettel.dms.dto.photo.PhotoDTO;
import com.viettel.dms.dto.view.AlbumDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.global.ServerPath;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.MenuItem;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.sale.image.PopupShowPosition;
import com.viettel.image.zoom.ImageViewTouch;
import com.viettel.image.zoom.PagerAdapter;
import com.viettel.image.zoom.ZoomViewPaper;
import com.viettel.image.zoom.ZoomViewPaper.OnPageChangeListener;
import com.viettel.map.OverlayViewItemObj;
import com.viettel.map.OverlayViewLayer;
import com.viettel.map.OverlayViewOptions;
import com.viettel.map.view.MarkerMapView;
import com.viettel.maps.MapView;
import com.viettel.maps.base.LatLng;
import com.viettel.maps.controls.BaseControl;
import com.viettel.maps.controls.ZoomControl;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * Man hinh xem chi tiet anh (Role TNPG)
 * 
 * @author: QuangVT
 * @version: 1.0
 * @since: 1.0
 */
public class TNPGFullImageView extends BaseFragment implements DiskCachePolicy,
		OnTouchListener {
	// TAG
	public static final String TAG = TNPGFullImageView.class.getName();
	// PARENT
	private GlobalBaseActivity parent;
	// CONSTANT
	private static final int MAX_IMAGE_CACHE_ON_MEMORY = 6;
	private static final int SHOW_POSITION = 0;
	public static final int ACTION_GET_MORE_PHOTO = 1;
	// HINH ANH
	private BigPhotoCache cache; // cahe hinh anh
	public boolean isZooming; // co zoom khong
	// danh sach hinh anh
	public ArrayList<PhotoDTO> listPhotosDTO = new ArrayList<PhotoDTO>(); 
	public ImageViewTouch imageTouch; // hinh anh khi touch an/hien gallery
	private ZoomViewPaper myPager; // pager hien hinh anh
	private MyPagerAdapter adapter; // Adapter cho pager
	// danh sach index nhung hinh anh duoc hien thi de cho recycle bitmaps
	private ArrayList<Integer> arraySelectedIndex = new ArrayList<Integer>(); 
	protected int currentSelected; // index lua chon hinh anh
	private RelativeLayout rootView; // View cha
	private AlbumDTO albumDTO; // Thong tin album hinh anh
	private boolean isChangedIndex; // co su thay doi lua chon hinh anh khong
	// DIALOG
	private AlertDialog alertPromotionDetail; // popup chi tiet chuong trinh
												// khuyen mai
	private MapView mapView; // Mapview hien vi tri
	// THUMNAIL
	private ThumbnailAdapter thumnailThumbs; // adapter
	private ThumbnailBus bus; // Bus cho thumnail
	private boolean isThumnailClick = false; // co bien nay de thumbnail adapter
	// GALLERY
	private LinearLayout llGallery; // Linear chua Gallery(de an hien)
	private Gallery gallery; // gallery hinh anh

	public static TNPGFullImageView newInstance(Bundle bundle) {
		TNPGFullImageView f = new TNPGFullImageView();
		f.setArguments(bundle);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) activity;
		albumDTO = (AlbumDTO) getArguments().getSerializable(
				IntentConstants.INTENT_ALBUM_INFO);
		listPhotosDTO = albumDTO.getListPhoto();
		currentSelected = getArguments().getInt(
				IntentConstants.INTENT_ALBUM_INDEX_IMAGE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.view_image_full,
				null);
		View view = super.onCreateView(inflater, v, savedInstanceState);

		// Setup header
		rootView = (RelativeLayout) view.findViewById(R.id.lnViewImage);
		initHeaderView(rootView, savedInstanceState);

		enableMenuBar(this);
		addMenuItem(R.drawable.icon_map, SHOW_POSITION);

		Display currentDisplay = ((WindowManager) parent.getBaseContext()
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int dw = currentDisplay.getWidth();
		int dh = currentDisplay.getHeight();

		updateSelectedInfoImage(currentSelected);

		bus = new ThumbnailBus();
		cache = new BigPhotoCache(null, this, MAX_IMAGE_CACHE_ON_MEMORY, bus);
		cache.setWidthHeight(dw, dh);
		cache.getBus().register(this.toString(), onCache);

		adapter = new MyPagerAdapter();
		myPager = (ZoomViewPaper) view.findViewById(R.id.myfivepanelpager);
		myPager.setFragment(this);
		myPager.setAdapter(adapter);
		myPager.setCurrentItem(currentSelected);
		myPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				updateSelectedInfoImage(arg0);
				if (isThumnailClick) {
					isThumnailClick = false;
				} else {
					currentSelected = arg0;
					gallery.setSelection(currentSelected);
					thumnailThumbs.notifyDataSetChanged();
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		initThumbnailGallery(view);
		return view;

	}

	/**
	 * Khoi tao Thumbnail Gallery
	 * 
	 * @author: QuangVT
	 * @return: voidvoid
	 * @throws:
	 */
	private void initThumbnailGallery(View view) {
		llGallery = (LinearLayout) view.findViewById(R.id.llGallery);
		llGallery.setVisibility(View.INVISIBLE);
		gallery = (Gallery) view.findViewById(R.id.gallery);
		int[] IMAGE_IDS = { R.id.thumbnail_item };
		thumnailThumbs = new ThumbnailAdapter(parent,
				new ThumbnailImageAdapter(parent, TNPGFullImageView.this,
						listPhotosDTO),
				new SimpleWebImageCache<ThumbnailBus, ThumbnailMessage>(null,
						null, 101, bus), IMAGE_IDS);
		gallery.setAdapter(thumnailThumbs);
		gallery.setSelection(currentSelected);
		gallery.setCallbackDuringFling(false);
		thumnailThumbs.notifyDataSetChanged();
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				isThumnailClick = true;
				currentSelected = arg2;
				myPager.setCurrentItem(currentSelected);
				adapter.notifyDataSetChanged();
			}
		});
	}

	/**
	 * Ạn/Hien Gallery
	 * 
	 * @author: QuangVT
	 * @return: voidvoid
	 * @throws:
	 */
	private void processThumbnailView() {
		if (llGallery.getVisibility() == View.INVISIBLE) {
			llGallery.setVisibility(View.VISIBLE);
		} else if (llGallery.getVisibility() == View.VISIBLE) {
			llGallery.setVisibility(View.INVISIBLE);
		}
	}

	private ThumbnailBus.Receiver<ThumbnailMessage> onCache = new ThumbnailBus.Receiver<ThumbnailMessage>() {
		public void onReceive(final ThumbnailMessage message) {
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					VTLog.e("PhucNT4", "Status image  " + message.status
							+ "message.position)" + message.position);

					switch (message.status) {
					case ThumbnailMessage.STATUS_ERR:
					case ThumbnailMessage.STATUS_NOT_FOUND:

						break;
					case ThumbnailMessage.STATUS_CANCEL:
						break;
					case ThumbnailMessage.STATUS_NONE:
					case ThumbnailMessage.STATUS_SUCCEED:
						try {
							ImageViewTouch image = message.getImageTouchView();
							String key = getUrlPath(listPhotosDTO
									.get(message.position));
							BitmapDrawable bd = (BitmapDrawable) cache.get(key);
							image.setImageBitmapReset(bd.getBitmap(), true);
							checkRecylceBitmap(message.position);
						} catch (Exception e) {
							VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog
									.getReportFromThrowable(e));
						}
						break;
					}
				}

				/**
				 * cu hashmap tang len toi max size thi thuc hien recycle mot
				 * nua bitmap de giai vong vung nho
				 * 
				 * @author: QuangVT
				 * @return: void
				 * @throws:
				 */
				private void checkRecylceBitmap(int position) {
					if (!arraySelectedIndex.contains(Integer.valueOf(position))) {
						arraySelectedIndex.add(Integer.valueOf(position));
					}
					VTLog.e("BigPhotoCache",
							"hash map size size  " + cache.getHashmapSize());
					if (cache != null
							&& cache.getHashmapSize() >= MAX_IMAGE_CACHE_ON_MEMORY) {
						VTLog.e("BigPhotoCache",
								"size  " + arraySelectedIndex.size());
						recycleHalfViews(message.position);
					}

				}
			});
		}
	};

	/**
	 * recylce nhung view ma naen tai ma cach xa voi voi view hien tai nhat
	 * 
	 * @author: QuangVT
	 * @return: void
	 * @throws:
	 */
	private void recycleHalfViews(int position) {
		int midSize = 1;// (int) arraySelectedIndex.size() / 2;
		for (int i = 0; i < midSize; i++) {
			int indexRecylce = findMaxInArray(arraySelectedIndex);
			String key = getUrlPath(listPhotosDTO.get(arraySelectedIndex.get(
					indexRecylce).intValue()));
			cache.recyleOneBitmap((BitmapDrawable) cache.get(key));
			cache.remove(key);
			VTLog.e("BigPhotoCache", "index recycle  "
					+ arraySelectedIndex.get(indexRecylce).intValue());
			arraySelectedIndex.remove(indexRecylce);

		}
	}

	/**
	 * tim index cua view ma nam cach xa voi view hien tai nhat de recycle
	 * 
	 * @author: QuangVT
	 * @return: void
	 * @throws:
	 */
	private int findMaxInArray(ArrayList<Integer> arr) {
		int max = 0;
		int indexMax = 0;
		try {
			int lastValue = arr.get(arr.size() - 1);
			for (int i = 0; i < arr.size(); i++) {
				int distance = Math.abs(arr.get(i).intValue() - lastValue);
				if (distance > max) {
					max = distance;
					indexMax = i;
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog",
					VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return indexMax;
	}

	/**
	 * Cap nhat thong tin hinh anh sau khi select
	 * 
	 * @author quangvt1
	 * @param position
	 */
	protected void updateSelectedInfoImage(int position) {
		currentSelected = position;
		isChangedIndex = true;
		PhotoDTO item = listPhotosDTO.get(currentSelected);
		String time = DateUtils.parseDateFromSqlLite(item.createdTime);

		String prefix = getParentPrefixTitle() + "-B. ";
		String title = Constants.STR_BLANK;
		title = prefix
				+ StringUtil
						.getString(R.string.TITLE_VIEW_IMAGE_FULL_TNPG_DEFAULT)
				+ " (" + item.staffCode + " - " + item.staffName
				+ getString(R.string.TEXT_TIME_TAKE_PHOTO) + time + ")";
		SpannableString span = new SpannableString(title);
		span.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), StringUtil
				.getString(R.string.TITLE_VIEW_IMAGE_FULL_TNPG).length(), title
				.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		setTitleHeaderView(span);
	}

	/**
	 * tao thanh tool bar zoom hinh anh, va bao xau
	 * 
	 * @author: QuangVT
	 * @param header
	 * @return: void
	 * @throws:
	 */
	private void initHeaderView(View header, Bundle savedInstanceState) {

	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void onDestroyView() {
		if (cache != null) {
			GlobalUtil.nullViewDrawablesRecursive(rootView);
			alertPromotionDetail = null;
			cache.recycleAllBitmaps(true);
			cache.getBus().unregister(onCache);
			cache.stopDownload();
			cache = null;
		}
		super.onDestroyView();
	}

	private class MyPagerAdapter extends PagerAdapter {

		public int getCount() {
			return listPhotosDTO.size();
		}

		public Object instantiateItem(View collection, int position) {

			LayoutInflater inflater = (LayoutInflater) collection.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			int resId = R.layout.item_view_image_full;

			View row = inflater.inflate(resId, null);
			VTLog.e("PhucNT4", "instantiateItem " + position);

			imageTouch = (ImageViewTouch) row.findViewById(R.id.imageView1);
			imageTouch.setFragment(TNPGFullImageView.this);
			imageTouch.setOnTouchListener(TNPGFullImageView.this);
			Bitmap bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.image_loading);
			imageTouch.setImageBitmapReset(bm, true);
			if (!StringUtil.isNullOrEmpty(listPhotosDTO.get(position).fullUrl)) {
				listPhotosDTO.get(position).fullUrl = listPhotosDTO
						.get(position).fullUrl;
				notifyLoadImage(listPhotosDTO.get(position), position,
						imageTouch);
			}

			((ZoomViewPaper) collection).addView(row);

			if (position == listPhotosDTO.size() - 3) {
				getMorePhotos();
			}

			return row;

		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ZoomViewPaper) arg0).removeView((View) arg2);
			VTLog.e("BigPhotoCache", "  destroyItem destroyItem  " + arg1);
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View) arg1);

		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

	}

	public void performZoom() {

	}

	/**
	 * Load them hinh anh
	 * 
	 * @author quangvt1
	 * @return void
	 */
	public void getMorePhotos() {
		Bundle bundle = new Bundle();
//		sendBroadcast(ActionEventConstant.GET_MORE_PHOTOS, bundle);
		TNPGPhotoThumbnailListView fragment = null;
		fragment = (TNPGPhotoThumbnailListView) getActivity().getFragmentManager().findFragmentByTag(TNPGPhotoThumbnailListView.TAG);
		if (fragment != null) {
			fragment.receiveBroadcast(ActionEventConstant.GET_MORE_PHOTOS,
					bundle);
		}
	}

	/**
	 * Notify Load Image
	 * 
	 * @author quangvt1
	 * @param imgDTO
	 * @param position
	 * @param image
	 */
	public void notifyLoadImage(PhotoDTO imgDTO, int position,
			ImageViewTouch image) {
		if (cache != null && imgDTO != null) {
			if (cache.get(imgDTO.fullUrl) == null) {
				ThumbnailMessage msg = cache.getBus().createMessage(
						this.toString());
				msg.position = position;
				msg.setImageTouchView(image);
				msg.setUrl(getUrlPath(imgDTO));
				try {
					cache.notify(msg.getUrl(), msg);
				} catch (Throwable t) {
					VTLog.e("PhucNT4", "Exception trying to fetch image", t);
				}

			} else {
				BitmapDrawable bd = (BitmapDrawable) cache.get(listPhotosDTO
						.get(position).fullUrl);
				image.setImageBitmapReset(bd.getBitmap(), true);
			}
		}
	}

	/**
	 * 
	 * notify Load Thumbnail Image
	 * 
	 * @author quangvt1
	 * @param program
	 * @return: void
	 * @throws:
	 */
	public void notifyLoadThumbnailImage(PhotoDTO imgDTO, int position,
			ImageView image) {
		if (cache.get(imgDTO.thumbUrl) == null) {
			ThumbnailMessage msg = cache.getBus()
					.createMessage(this.toString());
			msg.position = position;
			msg.setImageView(image);
			msg.setUrl(getUrlPath(imgDTO));
			try {
				cache.notify(msg.getUrl(), msg);
			} catch (Throwable t) {
				VTLog.e("PhucNT4", "Exception trying to fetch image", t);
			}
		} else {
			BitmapDrawable bd = (BitmapDrawable) cache.get(listPhotosDTO
					.get(position).thumbUrl);
			image.setImageBitmap(bd.getBitmap());
		}
	}

	/**
	 * Lay link hinh tu DTO
	 * 
	 * @author quangvt1
	 * @param dto
	 * @return
	 */
	public String getUrlPath(PhotoDTO dto) {
		if (dto != null && !StringUtil.isNullOrEmpty(dto.fullUrl)) {
			if (dto.fullUrl.contains(ExternalStorage.SDCARD_PATH)) {
				return dto.fullUrl;
			} else {
				return ServerPath.IMAGE_PATH + dto.fullUrl;
			}
		}
		return "";
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		if (control instanceof MenuItem) {
			if (eventType == SHOW_POSITION) {
				showPositionImage();
			}
		} else if (eventType == ACTION_GET_MORE_PHOTO) {
			getMorePhotos();
		} else {
			super.onEvent(eventType, control, data);
		}
	}

	/**
	 * show thong tin chi tiet chuong trinh khuyn mai
	 * 
	 * @author quangvt1
	 * @param program
	 * @return: void
	 * @throws:
	 */
	public void showPositionImage() {
		if (alertPromotionDetail == null || isChangedIndex) {
			isChangedIndex = false;
			Builder build = new AlertDialog.Builder(getActivity(),
					R.style.CustomDialogTheme);
			PopupShowPosition popUpView = new PopupShowPosition(getActivity());
			popUpView.setFragment(this);

			PhotoDTO dto = listPhotosDTO.get(currentSelected);
			initMap(popUpView.rlMap, dto.lat, dto.lng);

			build.setView(popUpView);
			alertPromotionDetail = build.create();
			Window window = alertPromotionDetail.getWindow();
			window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255,
					255, 255)));
			window.setLayout(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
		}
		alertPromotionDetail.show();
	}

	/**
	 * Khoi tao map
	 * 
	 * @author: QuangVT
	 * @param rlMap
	 * @param lat
	 * @param lng
	 * @return: voidvoid
	 * @throws:
	 */
	@SuppressWarnings("unchecked")
	private void initMap(RelativeLayout rlMap, double lat, double lng) {
		View m_vBackground = new View(parent);
		m_vBackground.setBackgroundColor(Color.rgb(237, 234, 226));
		m_vBackground.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		rlMap.addView(m_vBackground);

		mapView = new MapView(parent);
		mapView.setMapKey(GlobalInfo.VIETTEL_MAP_KEY);
		mapView.setZoomControlEnabled(true);
		mapView.setMapTypeControlEnabled(false);
		rlMap.addView(mapView);

		ZoomControl zoomControl = (ZoomControl) mapView
				.getControl(BaseControl.TYPE_CONTROL_ZOOM);
		if (zoomControl != null) {
			mapView.removeView(zoomControl);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.FILL_PARENT,
					RelativeLayout.LayoutParams.FILL_PARENT);
			zoomControl.setLayoutParams(lp);
			zoomControl.setGravity(Gravity.LEFT | Gravity.BOTTOM);
			rlMap.addView(zoomControl);
		}

		LatLng myLocation = new LatLng(lat, lng);
		mapView.moveTo(myLocation);
		MarkerMapView maker = new MarkerMapView(getActivity(),
				R.drawable.icon_location);
		OverlayViewOptions opts = new OverlayViewOptions();
		opts.position(new com.viettel.maps.base.LatLng(lat, lng));
		opts.drawMode(OverlayViewItemObj.DRAW_CENTER);
		OverlayViewLayer myPosLayer = new OverlayViewLayer();
		mapView.addLayer(myPosLayer);
		myPosLayer.addItemObj(maker, opts);
	}

	@Override
	public void receiveBroadcast(int action, Bundle bundle) {
		if (action == ActionEventConstant.GET_RESULT_MORE_PHOTOS) {
			albumDTO = (AlbumDTO) bundle
					.getSerializable(IntentConstants.INTENT_ALBUM_INFO);
			listPhotosDTO = albumDTO.getListPhoto();
			adapter.notifyDataSetChanged();
			thumnailThumbs.notifyDataSetChanged();
		}
	}

	@Override
	public boolean eject(File cachedFile) {
		return true;
	}

	public void closePopup() {
		alertPromotionDetail.dismiss();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		long time1 = 0, time2 = 0;
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			return false; // This is important, if you return TRUE the
		case MotionEvent.ACTION_DOWN:
			time1 = System.currentTimeMillis();
			break;
		case MotionEvent.ACTION_UP:
			time2 = System.currentTimeMillis();
			if ((time2 - time1) >= 1000) {
				processThumbnailView();
			}
			break;
		}

		return false;
	}
}
