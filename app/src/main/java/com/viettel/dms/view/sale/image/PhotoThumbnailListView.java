/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.image;

import java.util.ArrayList;

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
import android.widget.Button;
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
import com.viettel.dms.dto.db.MediaItemDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.photo.PhotoDTO;
import com.viettel.dms.dto.view.AlbumDTO;
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
 * Man hinh chua cac anh trong 1 album cua mot khach hang
 * 
 * @author: QuangVT 
 */ 
public class PhotoThumbnailListView extends BaseFragment implements OnItemClickListener, OnClickListener {
	//TAG
	public static final String TAG = PhotoThumbnailListView.class.getName();
	//PARENT
	private GlobalBaseActivity parent; // parent 
	// CONSTANT
	// action xem vi tri
	private static final int ACTION_POSITION = 1;	
	// so luong load more 
	private static final int NUM_LOAD_MORE = 20;	
	// id ImageView hien hinh anh
	public static final int[] IMAGE_IDS = { R.id.imgAlbumImage }; 
	private ThumbnailAdapter thumbs = null;	// Adapter Thumbnail
	private ThumbnailBus 	 bus;			// BUS thumbnail
	// danh sach Thumbnail
	PhotoThumbnailListDto photoThumbnailListDto = new PhotoThumbnailListDto(); 
	private int numTopLoaded = 0;			// so luong da load
	private boolean isAbleGetMore = true;	// Loadmore?
	// CONTROL
	private GridView gvImageView; // GV danh sach hinh anh
	private Button   btTakePhoto; // button chup hinh
	private TextView tvHoTenKH;	  // tv Ho ten KH	
	private TextView tvMaKH;	  // tv Ma Kh
	// INFO
	private int totalImage;		// so luong hinh anh  
	private int albumType = 0; 	// loai album 
	private String shopId; 		// shop ID
	
	// Prefix of title
	private String prefix = "";

	// Cho biet tu man hinh nao di vao:
	// 0 - tu thong tin khach hang, 4- man hinh hinh anh
	// muc dich set title cho dung
	private int type;	
		
	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param data
	 * @return
	 * @return: ListAlbumUserView
	 * @throws:
	 */ 
	public static PhotoThumbnailListView getInstance(Bundle data) {
		PhotoThumbnailListView instance = new PhotoThumbnailListView();
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
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_photo_thumbnail_user_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);

		Bundle data = (Bundle) getArguments();
		String customerId = data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String customerCode = data.getString(IntentConstants.INTENT_CUSTOMER_CODE);
		String customerName = data.getString(IntentConstants.INTENT_CUSTOMER_NAME);
		type   = data.getInt(IntentConstants.INTENT_TYPE, 0);
		shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		if (StringUtil.isNullOrEmpty(shopId)) {
			shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
		}
		AlbumDTO albumDto = (AlbumDTO) data.getSerializable(IntentConstants.INTENT_ALBUM_INFO);
		albumType = albumDto.getAlbumType();

		// Lay doi tuong
		gvImageView = (GridView) view.findViewById(R.id.gvImageView);
		gvImageView.setOnItemClickListener(this);

		btTakePhoto = (Button) view.findViewById(R.id.btTakePhoto);
		btTakePhoto.setOnClickListener(this);
		
		if (albumType == MediaItemDTO.TYPE_LOCATION_CLOSED) {
			btTakePhoto.setVisibility(View.GONE);
		}
		ListAlbumUserView albumFragment = (ListAlbumUserView) this.getFragmentManager().findFragmentByTag(
				ListAlbumUserView.TAG);
		if (albumFragment == null) {
			btTakePhoto.setVisibility(View.GONE);
		} 
		tvMaKH = (TextView) view.findViewById(R.id.tvMaKH);
		if(!StringUtil.isNullOrEmpty(customerCode)) {
			customerCode = customerCode.substring(0, 3);
		}
		tvMaKH.setText(customerCode);
		tvHoTenKH = (TextView) view.findViewById(R.id.tvHoTenKH);
		tvHoTenKH.setText(customerName);

		if (photoThumbnailListDto.isFirstInit) {
			gvImageView.setAdapter(thumbs);
			thumbs.notifyDataSetChanged();
		} else {
			photoThumbnailListDto.setCustomerId(customerId);
			photoThumbnailListDto.setCustomerName(customerName);
			photoThumbnailListDto.setAlbumInfo(albumDto.clone2());
			photoThumbnailListDto.isFirstInit = true;
			initData();
			getListAlbumOfUser();
			totalImage = photoThumbnailListDto.getAlbumInfo().getNumImage();
		}
		
		updateTitle(totalImage);

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
	 * Request upload photo
	 * 
	 * @author: QuangVT
	 * @param customerName
	 * @return: void
	 * @throws:
	 */
	private void updateTitle(int total) {
		SpannableObject spanObject = new SpannableObject();
		prefix = getParentPrefixTitle() + "-A.";
		spanObject.addSpan(prefix);
		spanObject.addSpan(Constants.STR_SPACE+ photoThumbnailListDto.getAlbumInfo().getAlbumTitle());
		spanObject.addSpan(" (");
		spanObject.addSpan(String.valueOf(total), ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);
		spanObject.addSpan(")");
		setTitleHeaderView(spanObject); 
	}

	/**
	 * Lay danh sach hinh anh cua User
	 * 
	 * @author: QuangVT
	 * @param customerName
	 * @return: void
	 * @throws:
	 */
	private void getListAlbumOfUser() {
		Bundle bundle = new Bundle();
		bundle.putInt(IntentConstants.INTENT_TYPE, type);
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, photoThumbnailListDto.getCustomerId());
		bundle.putString(IntentConstants.INTENT_SHOP_ID, shopId);
		bundle.putInt(IntentConstants.INTENT_ALBUM_TYPE, photoThumbnailListDto.getAlbumInfo().getAlbumType());
		bundle.putInt(IntentConstants.INTENT_MAX_IMAGE_PER_PAGE, NUM_LOAD_MORE);
		bundle.putInt(IntentConstants.INTENT_PAGE, numTopLoaded / NUM_LOAD_MORE);
		bundle.putBoolean(IntentConstants.INTENT_IS_GET_NUM_TOTAL_ITEM, true);
 
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GO_TO_ALBUM_DETAIL_USER;
		e.sender = this;
		e.viewData = bundle;
		e.tag = 0;
		SaleController.getInstance().handleViewEvent(e); 
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action) { 
		case ActionEventConstant.GO_TO_ALBUM_DETAIL_USER: {
			PhotoThumbnailListDto tempModel = (PhotoThumbnailListDto) modelEvent.getModelData();
			if (modelEvent.getActionEvent().tag == 0) {
				photoThumbnailListDto.getAlbumInfo().getListPhoto().clear();
				photoThumbnailListDto.getAlbumInfo().getListPhoto().addAll(tempModel.getAlbumInfo().getListPhoto());
				photoThumbnailListDto.getAlbumInfo().setNumImage(tempModel.getAlbumInfo().getNumImage());
				totalImage = tempModel.getAlbumInfo().getNumImage();
				updateTitle(totalImage);
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
			} else { // load more
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
				requestInsertLogKPI(HashMapKPI.NVBH_DANHSACHHINHANHTHEOALBUM, modelEvent.getActionEvent().startTimeFromBoot);
			}else if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSNPP) {
				requestInsertLogKPI(HashMapKPI.GSNPP_DANHSACHHINHANHTHEOALBUM, modelEvent.getActionEvent().startTimeFromBoot);
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
		case ACTION_POSITION: { 
			break;
		} 
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btTakePhoto) { 
			ListAlbumUserView albumFragment = (ListAlbumUserView) this.getFragmentManager().findFragmentByTag(
					ListAlbumUserView.TAG);
			if (albumFragment != null) {
				albumFragment.takePhoto(photoThumbnailListDto.getAlbumInfo().getAlbumType());
			} 
		} else {
			super.onClick(v);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Bundle bundle = new Bundle();
		bundle.putInt(IntentConstants.INTENT_SENDER, FullImageView.FROM_THUMNAIL_LIST_VIEW);
		bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentPrefixTitle());
		bundle.putInt(IntentConstants.INTENT_TYPE, type);
		bundle.putInt(IntentConstants.INTENT_ALBUM_INDEX_IMAGE, arg2);
		bundle.putSerializable(IntentConstants.INTENT_ALBUM_INFO, photoThumbnailListDto.getAlbumInfo());
		bundle.putString(IntentConstants.INTENT_FROM, PhotoThumbnailListView.TAG);
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, photoThumbnailListDto.getCustomerId());

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
	 * 
	 * Lay chuoi ngay thang nam yyyy-MM-dd HH:mm:ss tu dd/MM/yyyy HH:mm
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param strDate
	 * @param strTime
	 * @return
	 * @return: String
	 * @throws:
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
	 * Lay them hinh anh
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return: void
	 * @throws:
	 */ 
	public void getMorePhoto() {
		if (isAbleGetMore) { 
			Bundle bundle = new Bundle();
			bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, photoThumbnailListDto.getCustomerId());
			bundle.putString(IntentConstants.INTENT_SHOP_ID, shopId);
			bundle.putInt(IntentConstants.INTENT_ALBUM_TYPE, photoThumbnailListDto.getAlbumInfo().getAlbumType());
			bundle.putInt(IntentConstants.INTENT_MAX_IMAGE_PER_PAGE, NUM_LOAD_MORE);
			bundle.putInt(IntentConstants.INTENT_PAGE, numTopLoaded / NUM_LOAD_MORE);
			bundle.putBoolean(IntentConstants.INTENT_IS_GET_NUM_TOTAL_ITEM, false);
 
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.GO_TO_ALBUM_DETAIL_USER;
			e.sender = this;
			e.viewData = bundle;
			e.tag = 1;
			SaleController.getInstance().handleViewEvent(e); 
		}
	}

	/**
	 * Class Holder giup cai thien performance
	 * 
	 * @author quangvt1
	 */
	public static class ViewHolder {
		public ImageView imageView;
		public TextView titleAlbum;
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		// TODO Auto-generated method stub
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW: {
			if (this.isVisible()) {
				// cau request du lieu man hinh
				numTopLoaded = 0;
				isAbleGetMore = true;
				getListAlbumOfUser();
				
				ListAlbumUserView albumFragment = (ListAlbumUserView) this.getFragmentManager().findFragmentByTag(ListAlbumUserView.TAG);
				if (albumFragment != null) {
					albumFragment.getListAlbumOfUser(true);
				} 
			}
			break;
		}
		case ActionEventConstant.GET_MORE_PHOTOS: {
			getMorePhoto();
			break;
		}
		case ActionEventConstant.UPDATE_TAKEN_PHOTO: {
			numTopLoaded = 0;
			isAbleGetMore = true;
			getListAlbumOfUser();
			totalImage++;
			updateTitle(totalImage);
			break;
		}
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (thumbs != null) {
			thumbs.recycleAllBitmaps();
		}
		super.onDestroy();
	}
}
