/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.image;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonsware.cwac.cache.SimpleWebImageCache;
import com.commonsware.cwac.thumbnail.ThumbnailAdapter;
import com.commonsware.cwac.thumbnail.ThumbnailBus;
import com.commonsware.cwac.thumbnail.ThumbnailMessage;
import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.download.ExternalStorage;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.photo.PhotoDTO;
import com.viettel.dms.dto.view.PhotoThumbnailListDto;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.global.ServerPath;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * Man hinh chua cac anh cua mot khach hang che do search
 * 
 * PhotoThumbnailListViewForSearch.java
 * @author: QuangVT
 * @version: 1.0 
 * @since:  5:18:46 PM Dec 12, 2013
 */
public class PhotoThumbnailListViewForSearch extends BaseFragment implements OnItemClickListener, OnClickListener {
	/*----------------DEFAULT----------------*/
	//---Tag
	public static final String TAG = PhotoThumbnailListViewForSearch.class.getName();
	//---Parent
	private GlobalBaseActivity parent;
	
	/*----------------CONSTANT----------------*/ 
	// So luong load more
	private static final int NUM_LOAD_MORE   = 20;					
	// id ImageView hien hinh anh
	public static final int[] IMAGE_IDS = { R.id.imgAlbumImage };
	private static final int TAB_INDEX = 1;	

	/*----------------DTO----------------*/ 
	//---DTO thong tin album
	private PhotoThumbnailListDto photoThumbnailListDto = new PhotoThumbnailListDto();
	
	/*----------------ALBUM----------------*/ 
	private ThumbnailAdapter thumbs = null;		// Adapter
	private ThumbnailBus bus;					// Bus
	private int numTopLoaded = 0;				// So luong load top
	private boolean isAbleGetMore = true;		// Albe get more?
	
	/*----------------GRIDVIEW----------------*/ 
	private GridView gvImageView;
	
	/*----------------TIME IMAGE----------------*/ 
	private String strFromDate = Constants.STR_BLANK;
	private String strToDate   = Constants.STR_BLANK;
	private String strFromDateForRequest = Constants.STR_BLANK;
	private String strToDateForRequest   = Constants.STR_BLANK;

	/*----------------TITLES----------------*/  
	private TextView tvFromDateToDate;	// Khoang thoi gian
	private TextView tvMaTenKH;			// Thong tin khach hang
	
	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param data
	 * @return
	 * @return: ListAlbumUserView
	 * @throws:
	 */

	public static PhotoThumbnailListViewForSearch getInstance(Bundle data) {
		PhotoThumbnailListViewForSearch instance = new PhotoThumbnailListViewForSearch();
		instance.setArguments(data);
		return instance;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			parent = (GlobalBaseActivity) activity;
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_photo_thumbnail_view_for_search, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);

		Bundle data = (Bundle) getArguments(); 
		String customerId   = data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String customerCode = data.getString(IntentConstants.INTENT_CUSTOMER_CODE);
		String customerName = data.getString(IntentConstants.INTENT_CUSTOMER_NAME);
		
		strFromDate = data.getString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE);
		strToDate   = data.getString(IntentConstants.INTENT_FIND_ORDER_TO_DATE);
		 
		// Tao thoi gian request
		createTimeRequest();

		// Binding control
		initView(view);  
		
		if (!StringUtil.isNullOrEmpty(strFromDate) || !StringUtil.isNullOrEmpty(strToDate)){
			final String date = strFromDate + " - " + strToDate;
			tvFromDateToDate.setText(date);
		}
		if(!StringUtil.isNullOrEmpty(customerCode)) {
			customerCode = customerCode.substring(0, 3);
		}
		tvMaTenKH.setText(customerCode + " - " + customerName);

		if (photoThumbnailListDto.isFirstInit) { 
			gvImageView.setAdapter(thumbs);
			thumbs.notifyDataSetChanged();
		} else {
			photoThumbnailListDto.setCustomerId(customerId);
			photoThumbnailListDto.setCustomerName(customerName); 
			photoThumbnailListDto.isFirstInit = true;
			initData();
			getListAlbumOfUser();
		}
		
		int type = data.getInt(IntentConstants.INTENT_TYPE, 0);
		updateTitle(type, customerCode, customerName);

		return v;
	}

	/** 
	 * Khoi tao Album cho man hinh
	 * 
	 * @author: QuangVT1
	 * @return: void
	 * @throws:
	 */
	private void initData() {   
		bus = new ThumbnailBus();

		thumbs = new ThumbnailAdapter(parent, new ImageAdapter(parent, photoThumbnailListDto.getAlbumInfo()
				.getListPhoto()), new SimpleWebImageCache<ThumbnailBus, ThumbnailMessage>(null, null, 101,
				bus), IMAGE_IDS);
		gvImageView.setAdapter(thumbs);
	}
	/**
	 * Binding control
	 * 
	 * @author: QuangVT
	 * @since: 5:46:16 PM Dec 12, 2013
	 * @return: void
	 * @throws:  
	 * @param view
	 * @param data 
	 */
	private void initView(ViewGroup view) {  
		gvImageView = (GridView) view.findViewById(R.id.gvImageView);
		gvImageView.setOnItemClickListener(this);
		
		tvFromDateToDate = (TextView) view.findViewById(R.id.tvFromDateToDate);
		tvMaTenKH 		 = (TextView) view.findViewById(R.id.tvMaTenKH);  
	}

	/**
	 * Tao thoi gian request
	 * 
	 * @author: QuangVT
	 * @since: 5:42:23 PM Dec 12, 2013
	 * @return: void
	 * @throws:
	 */
	private void createTimeRequest() {
		final String dateTimePattern = StringUtil.getString(R.string.TEXT_DATE_TIME_PATTERN);
		final Pattern pattern = Pattern.compile(dateTimePattern);
		
		if (!StringUtil.isNullOrEmpty(strFromDate)) {
			String strTN = strFromDate.trim();

			Matcher matcher = pattern.matcher(strTN);
			if (matcher.matches()) { 
				try {
					Date tn = StringUtil.stringToDate(strTN, "");
					String strFindTN = StringUtil.dateToString(tn, "yyyy-MM-dd");
					strFromDateForRequest = strFindTN;
				} catch (Exception ex) { 
				} 
			}
		} 
		if (!StringUtil.isNullOrEmpty(strToDate)) {
			String strDN = strToDate.trim();
			
			Matcher matcher = pattern.matcher(strDN);
			if (matcher.matches()) { 
				try {
					Date dn = StringUtil.stringToDate(strDN, "");
					String strFindDN = StringUtil.dateToString(dn, "yyyy-MM-dd");
					strToDateForRequest = strFindDN;
				} catch (Exception ex) { 
				}
			}
		} 
	} 
	
	/**
	 * Set title cho man hinh
	 * 
	 * @author: QuangVT
	 * @since: 5:56:30 PM Dec 12, 2013
	 * @return: void
	 * @throws:  
	 * @param type
	 * @param customerCode
	 * @param customerName
	 */
	private void updateTitle(int type, String customerCode, String customerName) {
		SpannableObject spanObject = new SpannableObject();
		String prefix = getParentPrefixTitle() + "-A. " + getTitleString();
		spanObject.addSpan(prefix);
		spanObject.addSpan(" ");
		spanObject.addSpan(customerCode + " - " + customerName, ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		setTitleHeaderView(spanObject); 
	} 
	
	/**
	 *  Get List Album Of User
	 * 
	 * @author: QuangVT
	 * @since: 5:57:30 PM Dec 12, 2013
	 * @return: void
	 * @throws:
	 */
	private void getListAlbumOfUser() {
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, photoThumbnailListDto.getCustomerId());
		bundle.putInt(IntentConstants.INTENT_MAX_IMAGE_PER_PAGE, NUM_LOAD_MORE);
		bundle.putInt(IntentConstants.INTENT_PAGE, numTopLoaded / NUM_LOAD_MORE);
		
		 
		if (!StringUtil.isNullOrEmpty(strFromDateForRequest)) { 
			bundle.putString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE, strFromDateForRequest);
		} 
		
		if (!StringUtil.isNullOrEmpty(strToDateForRequest)) {
			bundle.putString(IntentConstants.INTENT_FIND_ORDER_TO_DATE, strToDateForRequest);
		} 

		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GO_TO_ALBUM_DETAIL_FOR_SEARCH;
		e.sender = this;
		e.viewData = bundle;
		e.tag = 0;
		SaleController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.GO_TO_ALBUM_DETAIL_FOR_SEARCH: {
			PhotoThumbnailListDto tempModel = (PhotoThumbnailListDto) modelEvent.getModelData();
			if (modelEvent.getActionEvent().tag == 0) {
				photoThumbnailListDto.getAlbumInfo().getListPhoto().clear();
				photoThumbnailListDto.getAlbumInfo().getListPhoto().addAll(tempModel.getAlbumInfo().getListPhoto());

				try { 
					//gvImageView.setAdapter(thumbs);
					thumbs.notifyDataSetChanged();
				} catch (Exception e1) {
				}

				int size = tempModel.getAlbumInfo().getListPhoto().size();
				numTopLoaded = size;
				if (size < NUM_LOAD_MORE) {
					isAbleGetMore = false;
				}
			} else {
				if (tempModel.getAlbumInfo().getListPhoto().size() < NUM_LOAD_MORE) {
					isAbleGetMore = false;
				}
				int size = tempModel.getAlbumInfo().getListPhoto().size();
				numTopLoaded += size;

				photoThumbnailListDto.getAlbumInfo().getListPhoto().addAll(tempModel.getAlbumInfo().getListPhoto());

				Bundle bundle = new Bundle();
				bundle.putSerializable(IntentConstants.INTENT_ALBUM_INFO, photoThumbnailListDto.getAlbumInfo());

				FullImageView orderFragment = (FullImageView) getActivity().getFragmentManager().findFragmentByTag(
						FullImageView.TAG);
				if (orderFragment != null) {
					orderFragment.receiveBroadcast(ActionEventConstant.GET_RESULT_MORE_PHOTOS, bundle);
				}
				//gvImageView.setAdapter(thumbs);
				thumbs.notifyDataSetChanged();
			}
			if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_VALSALES
					|| GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES) {
				requestInsertLogKPI(HashMapKPI.NVBH_DANHSACHHINHANHTIMKIEM, modelEvent.getActionEvent().startTimeFromBoot);
			}else if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSNPP) {
				requestInsertLogKPI(HashMapKPI.GSNPP_DANHSACHHINHANHTIMKIEM, modelEvent.getActionEvent().startTimeFromBoot);
			}
			break;
		}
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		setMenuItemFocus(eventType);
		switch (eventType) {
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Bundle bundle = new Bundle();
		bundle.putInt(IntentConstants.INTENT_SENDER, FullImageView.FROM_THUMNAIL_LIST_VIEW_FOR_SEARCH);
		bundle.putInt(IntentConstants.INTENT_TYPE, 5);
		bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentPrefixTitle());
		bundle.putSerializable(IntentConstants.INTENT_ALBUM_INFO, photoThumbnailListDto.getAlbumInfo());
		bundle.putInt(IntentConstants.INTENT_ALBUM_INDEX_IMAGE, arg2);
		bundle.putString(IntentConstants.INTENT_FROM, PhotoThumbnailListViewForSearch.TAG);

		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = bundle;
		e.action = ActionEventConstant.ACTION_LOAD_IMAGE_FULL;
		UserController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		super.handleErrorModelViewEvent(modelEvent);
	}

	public class ImageAdapter extends BaseAdapter {
		private ArrayList<PhotoDTO> list = new ArrayList<PhotoDTO>();
 
		public ImageAdapter(Context c, ArrayList<PhotoDTO> list) {
			this.list = list; 
		}

		@Override
		public int getCount() {
			if (list != null) {
				return list.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup pt) {
			View row = convertView;
			ViewHolder holder = null;

			if (convertView == null) {
				LayoutInflater layout = (LayoutInflater) ((GlobalBaseActivity) parent)
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = layout.inflate(R.layout.layout_album_detail_user, null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) row.findViewById(R.id.imgAlbumImage);
				holder.titleAlbum = (TextView) row.findViewById(R.id.tvAlbumName);
				row.setTag(holder);
			} else {
				holder = (ViewHolder) row.getTag();
			}

			holder.imageView.setImageResource(R.drawable.album_default);
 
			if (!StringUtil.isNullOrEmpty(list.get(position).thumbUrl)) {
				if (list.get(position).thumbUrl.contains(ExternalStorage.SDCARD_PATH))
					holder.imageView.setTag(list.get(position).thumbUrl);
				else
					holder.imageView.setTag(ServerPath.IMAGE_PATH + list.get(position).thumbUrl);
			}

			holder.titleAlbum.setText(list.get(position).createdTime);

			if (position == numTopLoaded - 1) {
				getMorePhoto();
			}

			return row;
		}
	} 
	
	/**
	 * Lay chuoi ngay thang nam yyyy-MM-dd HH:mm:ss tu dd/MM/yyyy HH:mm
	 * 
	 * @author: QuangVT
	 * @since: 6:10:41 PM Dec 12, 2013
	 * @return: String
	 * @throws:  
	 * @param strDateTime
	 * @return
	 */
	public static String getDateTimeStringFromDateAndTime(String strDateTime) {
		String[] timeArray = strDateTime.split(" ");
		String strDate = timeArray[0];
		String strTime = timeArray[1];

		String[] days = strDate.split("-");
		StringBuilder sbDateTime = new StringBuilder();

		if (days.length >= 3) {
			sbDateTime.append(days[2]);
			sbDateTime.append("/");
			sbDateTime.append(days[1]);
			sbDateTime.append("/");
			sbDateTime.append(days[0]);

			sbDateTime.append(" - ");
		}

		strTime = strTime.substring(0, strTime.lastIndexOf(":"));
		sbDateTime.append(strTime);

		return sbDateTime.toString().trim();
	} 

	/**
	 * Load them hinh anh
	 * 
	 * @author: QuangVT
	 * @since: 6:11:11 PM Dec 12, 2013
	 * @return: void
	 * @throws:
	 */
	public void getMorePhoto() {
		if (isAbleGetMore) { 
			Bundle bundle = new Bundle();
			bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, photoThumbnailListDto.getCustomerId());
			bundle.putInt(IntentConstants.INTENT_MAX_IMAGE_PER_PAGE, NUM_LOAD_MORE);
			bundle.putInt(IntentConstants.INTENT_PAGE, numTopLoaded / NUM_LOAD_MORE);
			
			if (!StringUtil.isNullOrEmpty(strFromDateForRequest)) {
				bundle.putString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE, strFromDateForRequest);
			} 
			if (!StringUtil.isNullOrEmpty(strToDateForRequest)) {
				bundle.putString(IntentConstants.INTENT_FIND_ORDER_TO_DATE, strToDateForRequest);
			} 
		 
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.GO_TO_ALBUM_DETAIL_FOR_SEARCH;
			e.sender = this;
			e.viewData = bundle;
			e.tag = 1;
			SaleController.getInstance().handleViewEvent(e);
		}
	}

	public static class ViewHolder {
		public ImageView imageView;
		public TextView titleAlbum;
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW: {
			if (this.isVisible()) {
				numTopLoaded = 0;
				isAbleGetMore = true;
				getListAlbumOfUser();
			}
			break;
		}
		case ActionEventConstant.GET_MORE_PHOTOS: {
			getMorePhoto();
			break;
		}
		case ActionEventConstant.UPDATE_TAKEN_PHOTO: {
			getListAlbumOfUser(); 
			break;
		}
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	} 
 
	@Override
	public void onDestroyView() {
		super.onDestroyView();

	}
	 
	@Override
	public void onDestroy() {
		if (thumbs != null) {
			thumbs.recycleAllBitmaps();
		}
		super.onDestroy();
	}
	
	@Override
	protected int getTabIndex() { 
		return TAB_INDEX;
	}
	
	@Override
	protected String getTitleString() {  
		return StringUtil.getString(R.string.TITLE_VIEW_ALBUM_DETAIL_PROGRAME_FROM_ALBUM_DEFAULT);
	}
}
