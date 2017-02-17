/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

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
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.download.ExternalStorage;
import com.viettel.dms.dto.db.DisplayProgrameDTO;
import com.viettel.dms.dto.db.MediaItemDTO;
import com.viettel.dms.dto.photo.PhotoDTO;
import com.viettel.dms.dto.view.AlbumDTO;
import com.viettel.dms.dto.view.PhotoThumbnailListDto;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
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
 * 
 *  Danh sach hinh anh trong 1 album
 *  @author: Nguyen Thanh Dung
 *  @version: 1.1
 *  @since: 1.0
 */
public class SupervisorPhotoThumbnailListUserView extends BaseFragment implements OnItemClickListener, OnClickListener {
	private static final int ACTION_POSITION = 1;
	private static final int NUM_LOAD_MORE = 20;

	public static final int[] IMAGE_IDS = { R.id.imgAlbumImage };
	public static final String TAG = SupervisorPhotoThumbnailListUserView.class.getName();

	private ThumbnailAdapter thumbs = null;
	private ThumbnailBus bus;
	PhotoThumbnailListDto model = new PhotoThumbnailListDto();
	private int numTopLoaded = 0;
	private boolean isAbleGetMore = true;

	private GlobalBaseActivity parent; // parent
	private GridView gvImageView;
	Button btTakePhoto;
	
	//ma khach hang
	private TextView tvCustomerCode;
	private String stCustomerCode = ""; 
	//Ten khach hang
	private TextView tvCustomerName;
	private String stCustomerName = ""; 
//	TextView tvNumImageInfo;
	private int totalImage;
	private String stStaffName;
	private String fromDate = ""; //tu ngay	
	private String toDate = ""; //den ngay
	private String toDateForRequest = ""; //tu ngay request
	private String fromDateForRequest = ""; //den ngay request
	int albumType = 0;
	private String shopId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.main.BaseFragment#onCreateView(android.view
	 * .LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_supervisor_album_detail_user_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);

		Bundle data = (Bundle) getArguments();
		String customerId = data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		stCustomerCode = data.getString(IntentConstants.INTENT_CUSTOMER_CODE);
		stCustomerName = data.getString(IntentConstants.INTENT_CUSTOMER_NAME);
		stStaffName = data.getString(IntentConstants.INTENT_CREATE_USER);
		fromDate = data.getString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE);
		toDate = data.getString(IntentConstants.INTENT_FIND_ORDER_TO_DATE);
		shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		AlbumDTO albumDto = (AlbumDTO) data.getSerializable(IntentConstants.INTENT_ALBUM_INFO);
		
		albumType = albumDto.getAlbumType();

		// Lay doi tuong
		gvImageView = (GridView) view.findViewById(R.id.gvImageView);
		gvImageView.setOnItemClickListener(this);

		tvCustomerCode = (TextView) view.findViewById(R.id.tvCustomerCode);
		tvCustomerCode.setText(stCustomerCode.subSequence(0, 3));
		tvCustomerName = (TextView) view.findViewById(R.id.tvCustomerName);
		tvCustomerName.setText(stCustomerName);
		btTakePhoto = (Button) view.findViewById(R.id.btTakePhoto);
		btTakePhoto.setOnClickListener(this);
		
		if (albumType == MediaItemDTO.TYPE_LOCATION_CLOSED) {
			btTakePhoto.setVisibility(View.GONE);
		}

		if (model.isFirstInit) {
			gvImageView.setAdapter(thumbs);
			thumbs.notifyDataSetChanged();
		} else {
			model.setCustomerId(customerId);
			model.setCustomerName(stCustomerName);
			model.setAlbumInfo(albumDto.clone2());
			model.isFirstInit = true;
			getListAlbumOfUser();
			totalImage = model.getAlbumInfo().getNumImage();
		}
		updateTitle(totalImage);

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
	private void updateTitle(int total) {
		SpannableObject spanObject = new SpannableObject();
		spanObject.addSpan(getString(R.string.TITLE_VIEW_GSNPP_ALBUM_DETAIL_USER));
//		spanObject.addSpan(Constants.STR_SPACE + model.getCustomerName(), ImageUtil.getColor(R.color.BLACK),
//				android.graphics.Typeface.BOLD);
		spanObject.addSpan(Constants.STR_SPACE + model.getAlbumInfo().getAlbumTitle(),
				ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
//		tvNumImageInfo.setText(spanObject.getSpan());
		spanObject.addSpan(" ("+String.valueOf(total)+")", ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);
		setTitleHeaderView(spanObject);

//		spanObject = new SpannableObject();
//		spanObject.addSpan(String.valueOf(total), ImageUtil.getColor(R.color.RED), android.graphics.Typeface.BOLD);
//		spanObject.addSpan(Constants.STR_SPACE + model.getAlbumInfo().getAlbumTitle(),
//				ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);
//		tvNumImageInfo.setText(spanObject.getSpan());
//		spanObject.addSpan(String.valueOf(total), ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			parent = (GlobalBaseActivity) activity;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

	}

	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param data
	 * @return
	 * @return: ListAlbumUserView
	 * @throws:
	 */

	public static SupervisorPhotoThumbnailListUserView getInstance(Bundle data) {
		// if (instance == null) {
		SupervisorPhotoThumbnailListUserView instance = new SupervisorPhotoThumbnailListUserView();
		instance.setArguments(data);
		// }

		return instance;
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
		if (v == btTakePhoto) {

			SupervisorListAlbumUserView orderFragment = (SupervisorListAlbumUserView) this.getFragmentManager().findFragmentByTag(
					SupervisorListAlbumUserView.TAG);
			if (orderFragment != null) {
				DisplayProgrameDTO dpTP = new DisplayProgrameDTO();
				dpTP.displayProgrameId = model.getAlbumInfo().getDisplayProgrameId();
				dpTP.displayProgrameCode = model.getAlbumInfo().getAlbumTitle();
				orderFragment.dpTakePhoto = dpTP;
				orderFragment.takePhoto(model.getAlbumInfo().getAlbumType());
			}

		} else {
			super.onClick(v);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(IntentConstants.INTENT_ALBUM_INFO, model.getAlbumInfo());
		bundle.putInt(IntentConstants.INTENT_ALBUM_INDEX_IMAGE, arg2);

		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = bundle;
		e.action = ActionEventConstant.ACTION_LOAD_IMAGE_FULL;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	private void getListAlbumOfUser() {
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, model.getCustomerId());
		bundle.putString(IntentConstants.INTENT_SHOP_ID, shopId);
		bundle.putInt(IntentConstants.INTENT_ALBUM_TYPE, model.getAlbumInfo().getAlbumType());
		bundle.putInt(IntentConstants.INTENT_MAX_IMAGE_PER_PAGE, NUM_LOAD_MORE);
		bundle.putInt(IntentConstants.INTENT_PAGE, numTopLoaded / NUM_LOAD_MORE);

		if (albumType == 4) {
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
			
			bundle.putString(IntentConstants.INTENT_DISPLAY_PROGRAM_CODE, model.getAlbumInfo().getAlbumTitle());

			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.GO_TO_ALBUM_DETAIL_PROGRAME;
			e.sender = this;
			e.viewData = bundle;
			e.tag = 0;
			SuperviorController.getInstance().handleViewEvent(e);
		} else {
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.GO_TO_ALBUM_DETAIL_USER;
			e.sender = this;
			e.viewData = bundle;
			e.tag = 0;
			SuperviorController.getInstance().handleViewEvent(e);
		}
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.GO_TO_ALBUM_DETAIL_PROGRAME:
		case ActionEventConstant.GO_TO_ALBUM_DETAIL_USER: {
			PhotoThumbnailListDto tempModel = (PhotoThumbnailListDto) modelEvent.getModelData();
			if (modelEvent.getActionEvent().tag == 0) {
				model.getAlbumInfo().getListPhoto().clear();
				model.getAlbumInfo().getListPhoto().addAll(tempModel.getAlbumInfo().getListPhoto());

				try {
					bus = new ThumbnailBus();

					thumbs = new ThumbnailAdapter(parent,
							new ImageAdapter(parent, model.getAlbumInfo().getListPhoto()),
							new SimpleWebImageCache<ThumbnailBus, ThumbnailMessage>(null, null, 101, bus), IMAGE_IDS);
					gvImageView.setAdapter(thumbs);
					thumbs.notifyDataSetChanged();
				} catch (Exception e1) {
					// TODO: handle exception
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

				model.getAlbumInfo().getListPhoto().addAll(tempModel.getAlbumInfo().getListPhoto());

				Bundle bundle = new Bundle();
				bundle.putSerializable(IntentConstants.INTENT_ALBUM_INFO, model.getAlbumInfo());
				// sendBroadcast(ActionEventConstant.GET_RESULT_MORE_PHOTOS,
				// bundle);

				SupervisorFullImageView orderFragment = (SupervisorFullImageView) getActivity().getFragmentManager().findFragmentByTag(
						SupervisorFullImageView.TAG);
				if (orderFragment != null) {
					orderFragment.receiveBroadcast(ActionEventConstant.GET_RESULT_MORE_PHOTOS, bundle);
				}
				thumbs.notifyDataSetChanged();
			}
			requestInsertLogKPI(HashMapKPI.GSNPP_DANHSACHHINHANHTHEOALBUM, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		}
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
		// TODO Auto-generated method stub

	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		super.handleErrorModelViewEvent(modelEvent);
	}

	public class ImageAdapter extends BaseAdapter {

		private ArrayList<PhotoDTO> list = new ArrayList<PhotoDTO>();

		public ImageAdapter(Context c, ArrayList<PhotoDTO> list) {
			this.list = list;
			//bmDefault = BitmapFactory.decodeResource(c.getResources(), R.drawable.album_default);
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
				LayoutInflater layout = (LayoutInflater) ( parent)
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = layout.inflate(R.layout.layout_supervisor_album_detail_user, null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) row.findViewById(R.id.imgAlbumImage);
				holder.titleAlbum = (TextView) row.findViewById(R.id.tvAlbumName);
				holder.createUser = (TextView) row.findViewById(R.id.tvCreateUser);
				
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
//			holder.createUser.setText(list.get(position).staffName);
			holder.createUser.setText(stStaffName);
			

			if (position == numTopLoaded - 1) {
				getMorePhoto();
			}

			return row;
		}
	}

	/**
	 * Lay chuoi ngay thang nam yyyy-MM-dd HH:mm:ss tu dd/MM/yyyy HH:mm
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
			bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, model.getCustomerId());
			bundle.putString(IntentConstants.INTENT_SHOP_ID, shopId);
			bundle.putInt(IntentConstants.INTENT_ALBUM_TYPE, model.getAlbumInfo().getAlbumType());
			bundle.putInt(IntentConstants.INTENT_MAX_IMAGE_PER_PAGE, NUM_LOAD_MORE);
			bundle.putInt(IntentConstants.INTENT_PAGE, numTopLoaded / NUM_LOAD_MORE);
			
			if (albumType == 4) {
				if (!StringUtil.isNullOrEmpty(fromDateForRequest)) {
					bundle.putString(IntentConstants.INTENT_FIND_ORDER_TO_DATE,
							fromDateForRequest);
				}
				if (!StringUtil.isNullOrEmpty(toDateForRequest)) {
					bundle.putString(IntentConstants.INTENT_FIND_ORDER_TO_DATE,
							toDateForRequest);
				}

				bundle.putString(IntentConstants.INTENT_DISPLAY_PROGRAM_CODE,
						model.getAlbumInfo().getAlbumTitle());

				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.GO_TO_ALBUM_DETAIL_PROGRAME;
				e.sender = this;
				e.viewData = bundle;
				e.tag = 1;
				SuperviorController.getInstance().handleViewEvent(e);
			} else {

				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.GO_TO_ALBUM_DETAIL_USER;
				e.sender = this;
				e.viewData = bundle;
				e.tag = 1;
				SuperviorController.getInstance().handleViewEvent(e);
			}
		}
	}

	public static class ViewHolder {
		public TextView createUser;
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
