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
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.download.ExternalStorage;
import com.viettel.dms.dto.photo.PhotoDTO;
import com.viettel.dms.dto.view.AlbumDTO;
import com.viettel.dms.dto.view.PhotoThumbnailListDto;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
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
 * Man hinh chua cac album cua mot khach hang
 * 
 * @author: SoaN
 * @version: 1.0
 * @since: Aug 1, 2012
 */

public class PhotoThumbnailListProgrameView extends BaseFragment implements OnItemClickListener, OnClickListener {
	private static final int ACTION_POSITION = 1;
	private static final int NUM_LOAD_MORE = 20;

	public static final int[] IMAGE_IDS = { R.id.imgAlbumImage };
	public static final String TAG = PhotoThumbnailListProgrameView.class.getName();

	private ThumbnailAdapter thumbs = null;
	private ThumbnailBus bus;
	PhotoThumbnailListDto photoThumbnailListDto = new PhotoThumbnailListDto();
	private int numTopLoaded = 0;
	private boolean isAbleGetMore = true;

	private GlobalBaseActivity parent; // parent
	private GridView gvImageView;
	TextView tvProgrameCode;
	TextView tvFromDateToDate;
	TextView tvMaTenKH;
	//private int totalImage;
	String fromDate="";
	String toDate="";
	String fromDateForRequest="";
	String toDateForRequest="";
	String programeCode="";
	private String shopId;

	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param data
	 * @return
	 * @return: ListAlbumUserView
	 * @throws:
	 */

	public static PhotoThumbnailListProgrameView getInstance(Bundle data) {
		PhotoThumbnailListProgrameView instance = new PhotoThumbnailListProgrameView();
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
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_photo_thumbnail_programe_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);

		Bundle data = (Bundle) getArguments();
		String customerId = data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String customerCode = data.getString(IntentConstants.INTENT_CUSTOMER_CODE);
		String customerName = data.getString(IntentConstants.INTENT_CUSTOMER_NAME);
		shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		if (StringUtil.isNullOrEmpty(shopId)) {
			shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
		}
		fromDate = data.getString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE);
		toDate = data.getString(IntentConstants.INTENT_FIND_ORDER_TO_DATE);
		AlbumDTO albumDto = (AlbumDTO) data.getSerializable(IntentConstants.INTENT_ALBUM_INFO);
		programeCode = albumDto.getAlbumTitle();
		if (StringUtil.getString(R.string.ALL).equals(programeCode)) {
			programeCode = "";
		}

		// Lay doi tuong
		gvImageView = (GridView) view.findViewById(R.id.gvImageView);
		gvImageView.setOnItemClickListener(this);

		tvProgrameCode = (TextView) view.findViewById(R.id.tvProgrameCode);
		tvFromDateToDate = (TextView) view.findViewById(R.id.tvFromDateToDate);
		tvMaTenKH = (TextView) view.findViewById(R.id.tvMaTenKH);
		tvProgrameCode.setText(albumDto.getAlbumTitle());
		if (!StringUtil.isNullOrEmpty(fromDate) || !StringUtil.isNullOrEmpty(toDate))
		tvFromDateToDate.setText(fromDate + " - " + toDate);
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
			photoThumbnailListDto.setAlbumInfo(albumDto.clone2());
			photoThumbnailListDto.isFirstInit = true;
			getListAlbumOfPrograme();
			//totalImage = photoThumbnailListDto.getAlbumInfo().getNumImage();
		}
		int type = data.getInt(IntentConstants.INTENT_TYPE, 0);
		updateTitle(type, customerCode, customerName);

		return v;
	}

	/**
	 * Request upload photo
	 * 
	 * @author: PhucNT
	 * @param customerName
	 * @return: void
	 * @throws:
	 */
	private void updateTitle(int type, String customerCode, String customerName) {
		SpannableObject spanObject = new SpannableObject();
		if (type ==4) {
			spanObject.addSpan(getString(R.string.TITLE_VIEW_ALBUM_DETAIL_PROGRAME_FROM_ALBUM));
		} else {
			spanObject.addSpan(getString(R.string.TITLE_VIEW_ALBUM_DETAIL_PROGRAME_FROM_CUSTOMER));
		}
		spanObject.addSpan(" ");
		spanObject.addSpan(customerCode + " - " + customerName, ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		setTitleHeaderView(spanObject);

//		spanObject = new SpannableObject();
//		spanObject.addSpan(String.valueOf(total), ImageUtil.getColor(R.color.RED), android.graphics.Typeface.BOLD);
//		spanObject.addSpan(" " + photoThumbnailListDto.getAlbumInfo().getAlbumTitle(),
//				ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);
//		tvNumImageInfo.setText(spanObject.getSpan());
	}

	/**
	 * get List Album Of User
	 * 
	 * @author: PhucNT - thanhnn
	 * @param customerName
	 * @return: void
	 * @throws:
	 */
	private void getListAlbumOfPrograme() {
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, photoThumbnailListDto.getCustomerId());
		bundle.putString(IntentConstants.INTENT_SHOP_ID, shopId);
		bundle.putInt(IntentConstants.INTENT_ALBUM_TYPE, photoThumbnailListDto.getAlbumInfo().getAlbumType());
		bundle.putInt(IntentConstants.INTENT_MAX_IMAGE_PER_PAGE, NUM_LOAD_MORE);
		bundle.putInt(IntentConstants.INTENT_PAGE, numTopLoaded / NUM_LOAD_MORE);
		
		//ngay thang va chuong trinh
		String dateTimePattern = StringUtil
				.getString(R.string.TEXT_DATE_TIME_PATTERN);
		Pattern pattern = Pattern.compile(dateTimePattern);
		if (!StringUtil.isNullOrEmpty(fromDate)) {
			String strTN = fromDate.trim();

			Matcher matcher = pattern.matcher(strTN);
			if (!matcher.matches()) {
				parent.showDialog(StringUtil
						.getString(R.string.TEXT_START_DATE_SEARCH_SYNTAX_ERROR));
				return;
			} else {
				try {
					Date tn = StringUtil.stringToDate(strTN, "");
					String strFindTN = StringUtil.dateToString(tn,
							"yyyy-MM-dd");
					fromDateForRequest = strFindTN;
					bundle.putString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE, fromDateForRequest);
				} catch (Exception ex) {
					parent.showDialog(StringUtil
							.getString(R.string.TEXT_START_DATE_SEARCH_SYNTAX_ERROR));
					return;
				}
			}
		} 
		if (!StringUtil.isNullOrEmpty(toDate)) {
			String strDN = toDate.trim();
			
			Matcher matcher = pattern.matcher(strDN);
			if (!matcher.matches()) {
				parent.showDialog(StringUtil
						.getString(R.string.TEXT_START_DATE_SEARCH_SYNTAX_ERROR));
				return;
			} else {
				try {
					Date dn = StringUtil.stringToDate(strDN, "");
					String strFindDN = StringUtil.dateToString(dn,
							"yyyy-MM-dd");
					toDateForRequest = strFindDN;
					bundle.putString(IntentConstants.INTENT_FIND_ORDER_TO_DATE, toDateForRequest);
				} catch (Exception ex) {
					parent.showDialog(StringUtil
							.getString(R.string.TEXT_START_DATE_SEARCH_SYNTAX_ERROR));
					return;
				}
			}
		} 
		
		bundle.putString(IntentConstants.INTENT_DISPLAY_PROGRAM_CODE, programeCode);

		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GO_TO_ALBUM_DETAIL_PROGRAME;
		e.sender = this;
		e.viewData = bundle;
		e.tag = 0;
		SaleController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.GO_TO_ALBUM_DETAIL_PROGRAME: {
			PhotoThumbnailListDto tempModel = (PhotoThumbnailListDto) modelEvent.getModelData();
			if (modelEvent.getActionEvent().tag == 0) {
				photoThumbnailListDto.getAlbumInfo().getListPhoto().clear();
				photoThumbnailListDto.getAlbumInfo().getListPhoto().addAll(tempModel.getAlbumInfo().getListPhoto());

				try {
					bus = new ThumbnailBus();

					thumbs = new ThumbnailAdapter(parent, new ImageAdapter(parent, photoThumbnailListDto.getAlbumInfo()
							.getListPhoto()), new SimpleWebImageCache<ThumbnailBus, ThumbnailMessage>(null, null, 101,
							bus), IMAGE_IDS);
					gvImageView.setAdapter(thumbs);
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
				// sendBroadcast(ActionEventConstant.GET_RESULT_MORE_PHOTOS,
				// bundle);

				FullImageView orderFragment = (FullImageView) getActivity().getFragmentManager().findFragmentByTag(
						FullImageView.TAG);
				if (orderFragment != null) {
					orderFragment.receiveBroadcast(ActionEventConstant.GET_RESULT_MORE_PHOTOS, bundle);
				}
				thumbs.notifyDataSetChanged();
			}

			break;
		}
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
		// TODO Auto-generated method stub

	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		setMenuItemFocus(eventType);
		switch (eventType) {
		case ACTION_POSITION: {

			// gotoCustomerRouteView();
			break;
		}

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
		bundle.putSerializable(IntentConstants.INTENT_ALBUM_INFO, photoThumbnailListDto.getAlbumInfo());
		bundle.putInt(IntentConstants.INTENT_ALBUM_INDEX_IMAGE, arg2);
		bundle.putString(IntentConstants.INTENT_FROM, PhotoThumbnailListProgrameView.TAG);

		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = bundle;
		e.action = ActionEventConstant.ACTION_LOAD_IMAGE_FULL;
		UserController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		super.handleErrorModelViewEvent(modelEvent);
	}

	public class ImageAdapter extends BaseAdapter {

		private ArrayList<PhotoDTO> list = new ArrayList<PhotoDTO>();

		// private Bitmap bitmap = null;
		// private Bitmap bmDefault =null;
		public ImageAdapter(Context c, ArrayList<PhotoDTO> list) {
			this.list = list;
			// bmDefault = BitmapFactory.decodeResource(c.getResources(),
			// R.drawable.album_default);
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

			// if (!StringUtil.isNullOrEmpty(list.get(position).thumbUrl) && new
			// File(list.get(position).thumbUrl).exists()) {
			// boolean decoded = false;
			// if (holder.imageView.getTag()instanceof Boolean)
			// decoded = ((Boolean)holder.imageView.getTag()).booleanValue();
			// if (!decoded){
			//
			// int maxDimension =
			// Math.max((int)getResources().getDimension(R.dimen.width_image_thumb),
			// (int)getResources().getDimension(R.dimen.height_image_thumb));
			// try {
			// bitmap =
			// ImageUtil.readImageFromSDCard(list.get(position).thumbUrl,
			// maxDimension);
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			// }
			// holder.imageView.setTag(new Boolean(true));
			// }
			// if(bitmap!= null)
			// holder.imageView.setImageBitmap(bitmap);
			// }
			// else
			if (!StringUtil.isNullOrEmpty(list.get(position).thumbUrl)) {
				if (list.get(position).thumbUrl.contains(ExternalStorage.SDCARD_PATH))
					holder.imageView.setTag(list.get(position).thumbUrl);
				else
					holder.imageView.setTag(ServerPath.IMAGE_PATH + list.get(position).thumbUrl);
				// holder.imageView.setTag(list.get(position).thumbUrl);
			}

			// holder.titleAlbum.setText(getDateTimeStringFromDateAndTime(list.get(position).createdTime));
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
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return: void
	 * @throws:
	 */

	public void getMorePhoto() {
		if (isAbleGetMore) {
//			numTopLoaded += NUM_LOAD_MORE;

			Bundle bundle = new Bundle();
			bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, photoThumbnailListDto.getCustomerId());
			bundle.putString(IntentConstants.INTENT_SHOP_ID, shopId);
			bundle.putInt(IntentConstants.INTENT_ALBUM_TYPE, photoThumbnailListDto.getAlbumInfo().getAlbumType());
			bundle.putInt(IntentConstants.INTENT_MAX_IMAGE_PER_PAGE, NUM_LOAD_MORE);
			bundle.putInt(IntentConstants.INTENT_PAGE, numTopLoaded / NUM_LOAD_MORE);
			
			if (!StringUtil.isNullOrEmpty(fromDateForRequest)) {
				bundle.putString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE, fromDateForRequest);
			} 
			if (!StringUtil.isNullOrEmpty(toDateForRequest)) {
				bundle.putString(IntentConstants.INTENT_FIND_ORDER_TO_DATE, toDateForRequest);
			} 
			
			bundle.putString(IntentConstants.INTENT_DISPLAY_PROGRAM_CODE, programeCode);

			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.GO_TO_ALBUM_DETAIL_PROGRAME;
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
				// cau request du lieu man hinh
				numTopLoaded = 0;
				isAbleGetMore = true;
				getListAlbumOfPrograme();
			}
			break;
		}
		case ActionEventConstant.GET_MORE_PHOTOS: {
			getMorePhoto();
			break;
		}
		case ActionEventConstant.UPDATE_TAKEN_PHOTO: {
			getListAlbumOfPrograme();
//			totalImage++;
//			updateTitle(totalImage);
			break;
		}
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.main.BaseFragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.main.BaseFragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (thumbs != null) {
			thumbs.recycleAllBitmaps();
		}
		super.onDestroy();
	}
}
