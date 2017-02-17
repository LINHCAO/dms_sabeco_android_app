/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.commonsware.cwac.cache.BigPhotoCache;
import com.commonsware.cwac.cache.CacheBase.DiskCachePolicy;
import com.commonsware.cwac.cache.SimpleWebImageCache;
import com.commonsware.cwac.thumbnail.ThumbnailAdapter;
import com.commonsware.cwac.thumbnail.ThumbnailBus;
import com.commonsware.cwac.thumbnail.ThumbnailMessage;
import com.viettel.dms.adapter.ThumbnailImageAdapter;
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
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.MenuItem;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.sale.image.FullImageView;
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
 * Man hinh xem chi tiet anh
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class SupervisorFullImageView extends BaseFragment implements OnTouchListener, DiskCachePolicy {
	public static final String TAG = SupervisorFullImageView.class.getName();
	private static final int MAX_IMAGE_CACHE_ON_MEMORY = 6;
	private static final int SHOW_POSITION = 0;
	// public ImageViewTouch image;
	ThumbnailBus thumbBus = new ThumbnailBus();
	// cahe hinh anh
	private BigPhotoCache cache;
	private ArrayList<PhotoDTO> listPhotosDTO = new ArrayList<PhotoDTO>();
	public ImageViewTouch imageTouch;
	private ZoomViewPaper myPager;
	private MyPagerAdapter adapter;
	// danh sach index nhung hinh anh duoc hien thi de cho recycle bitmaps
	private ArrayList<Integer> arraySelectedIndex = new ArrayList<Integer>();
	protected int currentSelected;
	// text view hien thi so thu tu cua hinh dang xem
	// private TextView tvNumberImage;
	// thanh bar o tren dau trong man hinh
	// private RelativeLayout headerBar;
	private RelativeLayout rootView;
	// header thong tin hinh anh
	// private ImageHeader ihImageHeader;
	private GlobalBaseActivity parent;
	AlbumDTO albumDTO;
	private AlertDialog alertPromotionDetail;
	private Gallery gallery;
	private ThumbnailAdapter thumnailThumbs;
	private ThumbnailBus bus;
	private boolean isThumnailClick = false;// co bien nay de thumbnail adapter
											// ko notify nhieu lan, se bi sai
											// lech hinh

	public static SupervisorFullImageView newInstance(Bundle bundle) {
		SupervisorFullImageView f = new SupervisorFullImageView();
		f.setArguments(bundle);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) activity;
		albumDTO = (AlbumDTO) getArguments().getSerializable(IntentConstants.INTENT_ALBUM_INFO);
		listPhotosDTO = albumDTO.getListPhoto();
		currentSelected = getArguments().getInt(IntentConstants.INTENT_ALBUM_INDEX_IMAGE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.view_image_full, null);
		View view = super.onCreateView(inflater, v, savedInstanceState);

		// Setup header
		rootView = (RelativeLayout) view.findViewById(R.id.lnViewImage);
		// gallery = (HorizontalListView)
		// view.findViewById(R.id.glCategoryView);

		enableMenuBar(this);
		addMenuItem(R.drawable.icon_map, SHOW_POSITION);

		Display currentDisplay = ((WindowManager) parent.getBaseContext().getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
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
				// VTLog.e("PhucNT4","onPageSelected "+arg0
				// +"vi tri tham so 1"+arg1+" tham so 2"+arg2);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// VTLog.e("PhucNT4","onPageScrollStateChanged "+arg0);
			}
		});

		initThumbnailGallery(view);
		return view;

	}

	/**
	 * init Thumbnail Gallery
	 * 
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void initThumbnailGallery(View view) {
		llGallery = (LinearLayout) view.findViewById(R.id.llGallery);
		llGallery.setVisibility(View.INVISIBLE);
		gallery = (Gallery) view.findViewById(R.id.gallery);
		int[] IMAGE_IDS = { R.id.thumbnail_item };
		thumnailThumbs = new ThumbnailAdapter(parent, new ThumbnailImageAdapter(parent, SupervisorFullImageView.this,
				listPhotosDTO), new SimpleWebImageCache<ThumbnailBus, ThumbnailMessage>(null, null, 101, bus),
				IMAGE_IDS);
		gallery.setAdapter(thumnailThumbs);
		gallery.setSelection(currentSelected);
		gallery.setCallbackDuringFling(false);
		thumnailThumbs.notifyDataSetChanged();
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				VTLog.e("TamPQ", "click vao image position : " + arg2);
				isThumnailClick = true;
				currentSelected = arg2;
				myPager.setCurrentItem(currentSelected);
				adapter.notifyDataSetChanged();
			}
		});
	}

	/**
	 * process Thumbnail View
	 * 
	 * @author: TamPQ
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

	// xu ly nhan ket qua tra ve khi load hinh anh
	private ThumbnailBus.Receiver<ThumbnailMessage> onCache = new ThumbnailBus.Receiver<ThumbnailMessage>() {
		public void onReceive(final ThumbnailMessage message) {
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					VTLog.e("PhucNT4", "Status image  " + message.status + "message.position)" + message.position);

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
							BitmapDrawable bd = (BitmapDrawable) cache.get(getUrlPath(listPhotosDTO
									.get(message.position)));
							image.setImageBitmapReset(bd.getBitmap(), true);
							checkRecylceBitmap(message.position);
						} catch (Exception e) {
							VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
						}
						break;
					}
					// adapter.notifyDataSetChanged();
				}

				/**
				 * cu hashmap tang len toi max size thi thuc hien recycle mot
				 * nua bitmap de giai vong vung nho
				 * 
				 * @author: PhucNT
				 * @return: void
				 * @throws:
				 */
				private void checkRecylceBitmap(int position) {
					if (!arraySelectedIndex.contains(Integer.valueOf(position))) {
						arraySelectedIndex.add(Integer.valueOf(position));
					}
					VTLog.e("BigPhotoCache", "hash map size size  " + cache.getHashmapSize());
					if (cache != null && cache.getHashmapSize() >= MAX_IMAGE_CACHE_ON_MEMORY) {
						VTLog.e("BigPhotoCache", "size  " + arraySelectedIndex.size());
						recycleHalfViews(message.position);
					}

				}
			});
		}
	};
	private boolean isChangedIndex;
	private LinearLayout llGallery;
	private MapView mapView;

	// private HorizontalListView gallery;

	/**
	 * recylce nhung view ma naen tai ma cach xa voi voi view hien tai nhat
	 * 
	 * @author: PhucNT
	 * @return: void
	 * @throws:
	 */
	private void recycleHalfViews(int position) {
		int midSize = 1;// (int) arraySelectedIndex.size() / 2;
		for (int i = 0; i < midSize; i++) {
			int indexRecylce = findMaxInArray(arraySelectedIndex);
			String key = getUrlPath(listPhotosDTO.get(arraySelectedIndex.get(indexRecylce).intValue()));
			cache.recyleOneBitmap((BitmapDrawable) cache.get(key));
			cache.remove(key);
			VTLog.e("BigPhotoCache", "index recycle  " + arraySelectedIndex.get(indexRecylce).intValue());
			arraySelectedIndex.remove(indexRecylce);

		}
		// cache.getSoftHashMap().clear();
		// imageAdapter.recycleHalf();
		// System.gc();

	}

	/**
	 * tim index cua view ma nam cach xa voi view hien tai nhat de recycle
	 * 
	 * @author: PhucNT
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
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return indexMax;
	}

	/**
	 * update image info
	 * 
	 * @author: PhucNT
	 * @return: void
	 * @throws:
	 */
	protected void updateSelectedInfoImage(int position) {
		currentSelected = position;
		isChangedIndex = true;
		String time = DateUtils.parseDateFromSqlLite(listPhotosDTO.get(currentSelected).createdTime);
		String staffCode = listPhotosDTO.get(currentSelected).staffCode;
		String staffName = listPhotosDTO.get(currentSelected).staffName;
		String createUser = listPhotosDTO.get(currentSelected).createUser;

		// intit title :TITLE_VIEW_IMAGE
		SpannableObject spanObject = new SpannableObject();
		spanObject.addSpan(StringUtil.getString(R.string.TITLE_VIEW_GSNPP_IMAGE_FULL));
		StringBuilder title = new StringBuilder();

		if (!StringUtil.isNullOrEmpty(staffCode)) {
			title.append(" (").append(staffCode).append(" - ").append(staffName)
					.append(StringUtil.getString(R.string.TEXT_TIME_TAKE_PHOTO)).append(time).append(")");
		} else {
			title.append(" (").append(createUser).append(StringUtil.getString(R.string.TEXT_TIME_TAKE_PHOTO))
					.append(time).append(")");
		}

		spanObject.addSpan(title.toString(), ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);

		setTitleHeaderView(spanObject);
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
			// View row = ((ZoomViewPaper) collection).getChildAt(position);
			// if (row == null) {
			LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);

			int resId = R.layout.item_view_image_full;

			View row = inflater.inflate(resId, null);
			VTLog.e("PhucNT4", "instantiateItem " + position);

			imageTouch = (ImageViewTouch) row.findViewById(R.id.imageView1);
			imageTouch.setFragment(SupervisorFullImageView.this);
			imageTouch.setOnTouchListener(SupervisorFullImageView.this);
			Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.image_loading);
			imageTouch.setImageBitmapReset(bm, true);
			if (!StringUtil.isNullOrEmpty(listPhotosDTO.get(position).fullUrl)) {

				// listPhotosDTO.get(position).fullUrl = ServerPath.AVATAR_PATH
				// + listPhotosDTO.get(position).fullUrl;
				listPhotosDTO.get(position).fullUrl = listPhotosDTO.get(position).fullUrl;
				notifyLoadImage(listPhotosDTO.get(position), position, imageTouch);
			}

			((ZoomViewPaper) collection).addView(row);

			// get more
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
		// if (image.getScale()==1)
		// ((ZoomViewPager)v).onTouchEvent(event);
		// return false;
	}

	public void getMorePhotos() {
		// ListAlbumDetailUserView.getInstance(new Bundle()).getMorePhoto();
		Bundle bundle = new Bundle();
		sendBroadcast(ActionEventConstant.GET_MORE_PHOTOS, bundle);

		SupervisorPhotoThumbnailListUserView orderFragment = (SupervisorPhotoThumbnailListUserView) getActivity()
				.getFragmentManager().findFragmentByTag(SupervisorPhotoThumbnailListUserView.TAG);
		if (orderFragment != null) {
			orderFragment.receiveBroadcast(ActionEventConstant.GET_MORE_PHOTOS, null);
		}
	}

	public void notifyLoadImage(PhotoDTO imgDTO, int position, ImageViewTouch image) {
		// if (!StringUtil.isNullOrEmpty(imgDTO.thumbUrl) && new
		// File(imgDTO.thumbUrl).exists()) {
		// Bitmap bitmap= null;
		// int maxDimension = Math.max(750,550);
		// try {
		// bitmap = ImageUtil.readImageFromSDCard(imgDTO.thumbUrl ,
		// maxDimension);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		// }
		//
		// if(bitmap!=null)
		// image.setImageBitmapReset(bitmap,true);
		// }
		if (cache != null && imgDTO != null) {
			if (cache.get(imgDTO.fullUrl) == null) {

				ThumbnailMessage msg = cache.getBus().createMessage(this.toString());

				msg.position = position;
				msg.setImageTouchView(image);
				msg.setUrl(getUrlPath(imgDTO));
				try {
					cache.notify(msg.getUrl(), msg);
				} catch (Throwable t) {
					VTLog.e("PhucNT4", "Exception trying to fetch image", t);
				}

			} else {
				BitmapDrawable bd = (BitmapDrawable) cache.get(listPhotosDTO.get(position).fullUrl);
				image.setImageBitmapReset(bd.getBitmap(), true);
			}
		}
	}

	public String getUrlPath(PhotoDTO dto) {
		if (!StringUtil.isNullOrEmpty(dto.fullUrl)) {
			if (dto.fullUrl.contains(ExternalStorage.SDCARD_PATH))
				return dto.fullUrl;
			else
				return ServerPath.IMAGE_PATH + dto.fullUrl;
		}
		return "";
	}

	// @Override
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

	@Override
	public void onEvent(int eventType, View control, Object data) {
		if (control instanceof MenuItem) {
			if (eventType == SHOW_POSITION) {
				showPositionImage();
			}
		} else if (eventType == FullImageView.ACTION_GET_MORE_PHOTO) {
			getMorePhotos();
		} else {
			super.onEvent(eventType, control, data);
		}
	}

	/**
	 * 
	 * show thong tin chi tiet chuong trinh khuy
	 * 
	 * @param program
	 * @return: void
	 * @throws:
	 */
	public void showPositionImage() {

		if (alertPromotionDetail == null || isChangedIndex) {
			isChangedIndex = false;
			Builder build = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
			PopupShowPosition popup = new PopupShowPosition(getActivity());
			popup.setFragment(this);
//			m_loMain = ihImageHeader.getLayoutMap();

			PhotoDTO dto = listPhotosDTO.get(currentSelected);
			initMap(popup.rlMap, dto.lat, dto.lng);

			build.setView(popup);
			alertPromotionDetail = build.create();
			Window window = alertPromotionDetail.getWindow();
			window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
			window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
		}
		alertPromotionDetail.show();
	}

	/**
	 * Mo ta muc dich cua ham
	 * @author: TamPQ
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
		m_vBackground.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		rlMap.addView(m_vBackground);

		mapView = new MapView(parent);
		mapView.setMapKey(GlobalInfo.VIETTEL_MAP_KEY);
		mapView.setZoomControlEnabled(true);
		mapView.setMapTypeControlEnabled(false);
		// mapView.setMapEventListener(this);
		rlMap.addView(mapView);

		ZoomControl zoomControl = (ZoomControl) mapView.getControl(BaseControl.TYPE_CONTROL_ZOOM);
		if (zoomControl != null) {
			mapView.removeView(zoomControl);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
					RelativeLayout.LayoutParams.FILL_PARENT);
			zoomControl.setLayoutParams(lp);
			zoomControl.setGravity(Gravity.LEFT | Gravity.BOTTOM);
			rlMap.addView(zoomControl);
		}

		LatLng myLocation = new LatLng(lat, lng);
		mapView.moveTo(myLocation);
		MarkerMapView maker = new MarkerMapView(getActivity(), R.drawable.icon_location);
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
			albumDTO = (AlbumDTO) bundle.getSerializable(IntentConstants.INTENT_ALBUM_INFO);
			listPhotosDTO = albumDTO.getListPhoto();
			adapter.notifyDataSetChanged();
			thumnailThumbs.notifyDataSetChanged();
		}
	}

	@Override
	public boolean eject(File cachedFile) {
		return true;
	}

	@Override
	public void closePopup() {
		alertPromotionDetail.dismiss();
	}
}
