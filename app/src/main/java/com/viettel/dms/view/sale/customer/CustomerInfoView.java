/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.view.sale.customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.TNPGController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.MediaItemDTO;
import com.viettel.dms.dto.db.SaleOrderDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.CustomerInfoDTO;
import com.viettel.dms.dto.view.CustomerProgrameDTO;
import com.viettel.dms.dto.view.SaleOrderCustomerDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.global.ServerPath;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.sale.image.FullImageView;
import com.viettel.dms.view.supervisor.training.TrainingListView;
import com.viettel.dms.view.tnpg.customer.CustomerListViewPG;
import com.viettel.map.dto.LatLng;
import com.viettel.sabeco.R;
import com.viettel.utils.ImageValidatorTakingPhoto;
import com.viettel.utils.VTLog;

import java.io.File;
import java.util.Calendar;
import java.util.Vector;

/**
 * Thong tin co ban khach hang
 * 
 * @author : BangHN since : 9:48:52 AM version :
 */
public class CustomerInfoView extends BaseFragment implements OnEventControlListener, VinamilkTableListener {
	public static final String TAG = CustomerInfoView.class.getName();

	public static final int UPDATE_LOCATION_ACTION = 1;
	public static final int END_UPDATE_ACTION = 2;
	public static final int FEEDBACK_ACTION = 3;
	public static final int GIVE_PROMOTION_ACTION = 4;
	public static final int CHECK_POINT_ACTION = 5;
	private static final int ACTION_DELETE_AVATAR = 6;
	public static final int ACTION_TAKE_PHOTO= 8;
	public static final int MENU_REVIEW = 10;
	public static final int MENU_LIST_FEEDBACKS = 11;
	public static final int MENU_MAP = 12;
	public static final int MENU_IMAGE = 13;
	public static final int MENU_INFO_DETAIL = 14;
	public static final int MENU_UPDATE_LOCAION = 15;
	public static final int MENU_CUSTOMER_SALE = 16; 
	
	LinearLayout llContent;// layout chua toan bo customer info
	LinearLayout llProgrameDisplay;// title chuong trinh khach hang dang tham
	LinearLayout llSaleInfo	;// latout doanh so							// gia
	TextView tvCustomerName;// ten khach hang
	TextView tvCustomerAddress;// dia chi khach hang
	TextView tvCustomerPhone;// phone
	TextView tvCustomerMobile;// mobile phone
	TextView tvCustomerType;// loai cua hang
	TextView tvCustomerContact;// nguoi lien he
	TextView tvSKU;// sku
	TextView tvOrderInMonth;// so lan dat trong thang
	TextView tvAverageSales;// binh quan 2 thang truoc
	TextView tvSalesInMonth;// doanh so trong thang
	TextView tvSalesLast3Month1;// doanh so trong 3 thang
	TextView tvSalesLast3Month2;// doanh so trong 3 thang
	TextView tvSalesLast3Month3;// doanh so trong 3 thang
	TextView tvLastSalesGroup;// title 5 don hang gan nhat
	TextView tvPrograme;// title CTTB
	TextView tvAmountInfo;
	ImageView ivAvatar;

	// doanh so gan day nhat
	private DMSTableView tbLastSalesInfo;
	//chuong trinh khach hang tham gia
	private DMSTableView tbCusDisplayProgram;
	// parent
	GlobalBaseActivity parent;
	CustomerInfoDTO customerInfoDTO = new CustomerInfoDTO();
	CustomerUpdateAvatar updateAvatar;

	// lan dau init view
	private boolean isFirstInitView = true;
	String customerId;// id khach hang
	int currentPage = 1;// trang hien tai
	int numTop = 5;// so luong moi trang
	private String thumbURLFrom;
	private String mediaItemID;
	private File takenPhoto;
	private boolean hasAvatar;
	private String URL;
	private String sender;
	private String tagFrom;
	private boolean isNewAvatar; 
	private final int TAB_INDEX = 1;

	// bien dung de kiem tra co phai di tu man hinh tao don hang hay khong
	public boolean isFromCreateOrder = false;

	public static CustomerInfoView newInstance(Bundle data) {
		CustomerInfoView f = new CustomerInfoView();
		f.setArguments(data);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (GlobalBaseActivity) getActivity();
		isNewAvatar=false;
		Bundle args = getArguments();
		if (args != null) {
			customerId = args.getString(IntentConstants.INTENT_CUSTOMER_ID);
			sender = args.getString(IntentConstants.INTENT_SENDER);
			if(!StringUtil.isNullOrEmpty(args.getString(IntentConstants.INTENT_FROM))){
				if(args.getString(IntentConstants.INTENT_FROM).equalsIgnoreCase(FullImageView.TAG)){
					thumbURLFrom=args.getString(IntentConstants.INTENT_URL);
					mediaItemID=args.getString(IntentConstants.INTENT_MEDIA_ITEM_ID);
				}
			}
			tagFrom=args.getString(IntentConstants.INTENT_SENDER, Constants.STR_BLANK);
			VTLog.i("Customer", "Customer code : " + customerId);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_customer_info, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		llContent = (LinearLayout) v.findViewById(R.id.llContent);
		llContent.setVisibility(View.GONE);
		llProgrameDisplay = (LinearLayout) v.findViewById(R.id.llProgrameDisplay);
		tbLastSalesInfo = (DMSTableView) v.findViewById(R.id.tbLastSalesInfo);
		llSaleInfo = (LinearLayout) v.findViewById(R.id.llSaleInfo);
		tbCusDisplayProgram = (DMSTableView) v.findViewById(R.id.tbCusDisplayProgram);
		tbLastSalesInfo.setListener(this);
		initViewControl(v);
		// Title NVBH
		int role=GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType;
		 if(role==UserDTO.TYPE_TNPG){
			llSaleInfo.setVisibility(View.GONE);
			tbLastSalesInfo.setVisibility(View.GONE);
			tvLastSalesGroup.setVisibility(View.GONE);
			tvAmountInfo.setText(StringUtil.getString(R.string.TEXT_YIELDS_DETAIL_TITLE));
		} 
		
		setTitleHeaderView(getFullTitleString());

		// intiMenuIcon(v);
		enableMenuBar(this);
		initMenuActionBar();
		ivAvatar.setImageResource(R.drawable.icon_app);

		// TamPQ
		ActionLogDTO actionLog = GlobalInfo.getInstance().getProfile().getActionLogVisitCustomer();
		if (actionLog != null && actionLog.isVisited()) {
			parent.endVisitCustomerBar();
		}

		return v;
	}

	/**
	 * Khoi tao cac control layout
	 * 
	 * @author : BangHN since : 9:35:46 AM
	 */
	private void initViewControl(View v) {
		tvCustomerName = (TextView) v.findViewById(R.id.tvCustomerName);
		tvCustomerAddress = (TextView) v.findViewById(R.id.tvCustomerAddress);
		tvCustomerPhone = (TextView) v.findViewById(R.id.tvCustomerPhone);
		tvCustomerMobile = (TextView) v.findViewById(R.id.tvCustomerMobile);
		tvCustomerType = (TextView) v.findViewById(R.id.tvCustomerType);
		tvCustomerContact = (TextView) v.findViewById(R.id.tvCustomerContact);
		tvOrderInMonth = (TextView) v.findViewById(R.id.tvOrderInMonth);
//		tvSKU = (TextView) v.findViewById(R.id.tvSKU);
		tvSalesInMonth = (TextView) v.findViewById(R.id.tvSalesInMonth);
		tvAverageSales = (TextView) v.findViewById(R.id.tvAverageSales);
		tvSalesLast3Month1 = (TextView) v.findViewById(R.id.tvSalesLast3Month1);
		tvSalesLast3Month2 = (TextView) v.findViewById(R.id.tvSalesLast3Month2);
		tvSalesLast3Month3 = (TextView) v.findViewById(R.id.tvSalesLast3Month3);
		tvLastSalesGroup = (TextView) v.findViewById(R.id.tvLastSalesGroup);
		tvPrograme = (TextView) v.findViewById(R.id.tvPrograme);
		tvAmountInfo=(TextView) v.findViewById(R.id.tvAmountInfo);
		ivAvatar= (ImageView) v.findViewById(R.id.ivAvatar);
		ivAvatar.setOnClickListener(this);
		if (isFirstInitView) {
			parent.showLoadingDialog();
			getCustomerInfo(customerId);
			// getLastSaleOrder();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==ivAvatar){
			if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES){
				createPopup();
			}
		}
	}

	/**
	* khoi tao popup Update avatar
	* @author: dungdq3
	* @return: void
	* @date: Jan 2, 2014
	*/
	private void createPopup() {
		// TODO Auto-generated method stub
		if(updateAvatar==null){
			updateAvatar= new CustomerUpdateAvatar(parent, this, customerInfoDTO, hasAvatar);
		}else{
			updateAvatar.setHideMenuDelete(hasAvatar);
		}
		updateAvatar.show();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (isFirstInitView) {
		} else {
			if(!StringUtil.isNullOrEmpty(customerInfoDTO.getCustomer().getURL())){
				ImageUtil.getImageFromURL(customerInfoDTO.getCustomer().getURL(), parent, ivAvatar);
				hasAvatar=true;
			}
			renderData(customerInfoDTO);
			renderDataLastSaleOrder();
		}
	}

	/**
	 * Init menu action bar
	 * 
	 * @author : BangHN since : 2:53:53 PM
	 */
	private void initMenuActionBar() {
		addMenuItem(StringUtil.getString(R.string.TEXT_POSITION), R.drawable.icon_map, MENU_MAP);
		addMenuItem(StringUtil.getString(R.string.TEXT_PICTURE), R.drawable.menu_picture_icon, MENU_IMAGE);
		addMenuItem(StringUtil.getString(R.string.TEXT_FEEDBACK), R.drawable.icon_note, MENU_LIST_FEEDBACKS);
		addMenuItem(StringUtil.getString(R.string.TEXT_INFO), R.drawable.icon_detail, MENU_INFO_DETAIL, View.INVISIBLE);
		setMenuItemFocus(4);
	}

	/**
	 * Chuong trinh khach hang dang tham gia
	 * 
	 * @author : BangHN since : 1.0
	 */
	private void renderCustomerDisplayPrograme() {
		if (customerInfoDTO != null && customerInfoDTO.getListCustomerPrograme() != null) {
			int size = customerInfoDTO.getListCustomerPrograme().size();
			tbCusDisplayProgram.clearAllData();
			if (size > 0) {
//				int[] DP_CUTOMER_TABLE_WIDTHS = { 100, 250, 100, 100, 195, 195 };
//				String[] DP_CUTOMER_TABLE_TITLES = {StringUtil.getString(R.string.TEXT_LABLE_PROGRAM_CODE), 
//						StringUtil.getString(R.string.TEXT_CTKM_DETAIL_PROMOTION_NAME), 
//						StringUtil.getString(R.string.TEXT_CTTB_DEPART), 
//						StringUtil.getString(R.string.TEXT_HEADER_LIMIT), 
//						StringUtil.getString(R.string.QUOTA), 
//						StringUtil.getString(R.string.TEXT_TB_CUSTOMER_SALES_REMAIN)};
//				tbCusDisplayProgram.getHeaderView().removeAllColumns();
//				tbCusDisplayProgram.getHeaderView().addColumns(DP_CUTOMER_TABLE_WIDTHS,
//						DP_CUTOMER_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK),
//						ImageUtil.getColor(R.color.TABLE_HEADER_BG));
				initHeaderTable(tbCusDisplayProgram, new CustomerProgrameDisplayRow(parent, this));
//				tbCusDisplayProgram.hideNoContentRow();
				// ds chuong trinh trung bay
				for (CustomerProgrameDTO customerProgrameDTO : customerInfoDTO.getListCustomerPrograme()) {
					CustomerProgrameDisplayRow row = new CustomerProgrameDisplayRow(parent, null);
					row.updateData(customerProgrameDTO);
					tbCusDisplayProgram.addRow(row);
				}
				llProgrameDisplay.setVisibility(View.VISIBLE);
				tvPrograme.setVisibility(View.VISIBLE);
			} else {
				tbCusDisplayProgram.showNoContentRow();
				llProgrameDisplay.setVisibility(View.GONE);
				tvPrograme.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * render data don hang gan day cua khach hang
	 * 
	 * @author : BangHN since : 1.0
	 */
	private void renderDataLastSaleOrder() {
		if (customerInfoDTO == null || customerInfoDTO.listOrderCustomer == null) {
			tvLastSalesGroup.setVisibility(View.GONE);
			return;
		}
//		tbLastSalesInfo.getHeaderView().removeAllColumns();
		if (customerInfoDTO.listOrderCustomer.size() > 0) {
//			tvLastSalesGroup.setText(StringUtil.getString(R.string.TEXT_LIST) + customerInfoDTO.listOrderCustomer.size() 
//					+ StringUtil.getString(R.string.TEXT_ORDERS_RECENTLY));
//			tvLastSalesGroup.setVisibility(View.VISIBLE);
//			int[] LAST_ORDER_CUSTOMER_TABLE_WIDTHS = {
//				TableDefineContanst.COLUMN_WIDTH_CUS_LAST_ORDER_STT, TableDefineContanst.COLUMN_WIDTH_CUS_LAST_ORDER_CODE,
//				TableDefineContanst.COLUMN_WIDTH_CUS_LAST_ORDER_DATE, TableDefineContanst.COLUMN_WIDTH_CUS_LAST_ORDER_SKU,
//				TableDefineContanst.COLUMN_WIDTH_CUS_LAST_ORDER_MONEY };
//			String[] LAST_ORDER_CUSTOMER_TABLE_TITLES = {
//					TableDefineContanst.COLUMN_NAME_CUS_SALE_STT, TableDefineContanst.COLUMN_NAME_CUS_LAST_ORDER_CODE,
//					TableDefineContanst.COLUMN_NAME_CUS_LAST_ORDER_DATE, TableDefineContanst.COLUMN_NAME_CUS_LAST_ORDER_SKU,
//					TableDefineContanst.COLUMN_NAME_CUS_LAST_ORDER_MONEY };
//			
//			if(GlobalInfo.getInstance().getIsShowPrice() == 0) {
//				LAST_ORDER_CUSTOMER_TABLE_WIDTHS = new int[]{
//						TableDefineContanst.COLUMN_WIDTH_CUS_LAST_ORDER_STT, TableDefineContanst.COLUMN_WIDTH_CUS_LAST_ORDER_CODE + TableDefineContanst.COLUMN_WIDTH_CUS_LAST_ORDER_MONEY,
//						TableDefineContanst.COLUMN_WIDTH_CUS_LAST_ORDER_DATE, TableDefineContanst.COLUMN_WIDTH_CUS_LAST_ORDER_SKU };
//				LAST_ORDER_CUSTOMER_TABLE_TITLES = new String[] {
//						TableDefineContanst.COLUMN_NAME_CUS_SALE_STT, TableDefineContanst.COLUMN_NAME_CUS_LAST_ORDER_CODE,
//						TableDefineContanst.COLUMN_NAME_CUS_LAST_ORDER_DATE, TableDefineContanst.COLUMN_NAME_CUS_LAST_ORDER_SKU};	
//			}
//		
//			tbLastSalesInfo.getHeaderView().addColumns(LAST_ORDER_CUSTOMER_TABLE_WIDTHS,
//					LAST_ORDER_CUSTOMER_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK),
//					ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			initHeaderTable(tbLastSalesInfo, new CustomerLastOrderRow(parent, this));
			int pos=1;
			tbLastSalesInfo.clearAllData();
			for (SaleOrderCustomerDTO saleOrderCustomerDTO: customerInfoDTO.listOrderCustomer) {
				CustomerLastOrderRow row = new CustomerLastOrderRow(parent, this);
				row.setClickable(true);
				row.setDataRow(saleOrderCustomerDTO, pos);
				tbLastSalesInfo.addRow(row);
				pos++;
			}
		} else {
			tvLastSalesGroup.setVisibility(View.GONE);
		}
	}

	/**
	* Di toi chi tiet don hang
	* @author: BangHN
	* @return: void
	* @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	private void gotoOrderDetail(SaleOrderDTO dto){
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentPrefixTitle());
		bundle.putString(IntentConstants.INTENT_ORDER_ID, "" + dto.saleOrderId);
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID,
				String.valueOf(customerId));
		bundle.putString(IntentConstants.INTENT_CUSTOMER_CODE, customerInfoDTO.getCustomer().customerCode);
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ADDRESS, customerInfoDTO.getCustomer().address);
		bundle.putString(IntentConstants.INTENT_CUSTOMER_NAME, customerInfoDTO.getCustomer().getCustomerName());
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_ORDER_VIEW;
		UserController.getInstance().handleSwitchFragment(e);
	}
	
	
	/**
	 * Lay thong tin cua khach hang
	 * 
	 * @author : BangHN since : 1.0
	 */
	private void getCustomerInfo(String customerId) {
		ActionEvent e = new ActionEvent();
		Vector<String> v = new Vector<String>();
		v.add(IntentConstants.INTENT_CUSTOMER_ID);
		v.add(customerId);// 129158
		v.add(IntentConstants.INTENT_PAGE);
		v.add("" + currentPage);
		v.add(IntentConstants.INTENT_NUMTOP);
		v.add("" + numTop);
		
		e.viewData = v;
		e.sender = this;
		e.action = ActionEventConstant.GET_CUSTOMER_BASE_INFO;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * gotoFeedBackList
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void gotoFeedBackList() {
		if (customerInfoDTO.getCustomer() == null) {
			return;
		}
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentPrefixTitle());
		bundle.putSerializable(IntentConstants.INTENT_CUSTOMER, customerInfoDTO.getCustomer());
		bundle.putString(IntentConstants.INTENT_SENDER, sender);
		
		e.viewData = bundle;
		e.action = ActionEventConstant.GET_LIST_CUS_FEED_BACK;
		SaleController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * di toi man hinh cap nhat vi tri khach hang
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void gotoCustomerLocation() {
		if (customerInfoDTO.getCustomer() == null) {
			return;
		}

		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentPrefixTitle());
		bundle.putSerializable(IntentConstants.INTENT_CUSTOMER, customerInfoDTO.getCustomer());
		bundle.putString(IntentConstants.INTENT_SENDER, tagFrom);
		e.viewData = bundle;
		e.action = ActionEventConstant.GOTO_CUSTOMER_LOCATION;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Render du lieu len view
	 * 
	 * @author : BangHN since : 9:29:23 AM
	 */
	private void renderData(CustomerInfoDTO viewData) {
		if (viewData != null && viewData.getCustomer() != null) {
			if(!StringUtil.isNullOrEmpty(viewData.getCustomer().getCustomerCode())) {
				tvCustomerName.setText(viewData.getCustomer().getCustomerCode().substring(0, 3) + " - "
						+ viewData.getCustomer().getCustomerName());
			}else{
				tvCustomerName.setText(viewData.getCustomer().getCustomerName());
			}
			tvCustomerAddress.setText(viewData.getCustomer().address);
			tvCustomerPhone.setText(viewData.getCustomer().getPhone());
			tvCustomerMobile.setText(viewData.getCustomer().getMobilephone());
			if (viewData.getCustomerType() != null) {
				tvCustomerType.setText(viewData.getCustomerType().channelTypeCode + " - "
						+ viewData.getCustomerType().channelTypeName);
			} else {
				tvCustomerType.setText("");
			}
			tvCustomerContact.setText(viewData.getCustomer().getContactPerson());
			tvOrderInMonth.setText("" + viewData.saleOrdersInMonth);
//			tvSKU.setText("" + viewData.sku);

			tvSalesInMonth.setText(""+viewData.amountInMonth);
			tvAverageSales.setText(StringUtil
					.parseAmountMoney((viewData.amountInOneMonthAgo + viewData.amountInTwoMonthAgo) / 2));
			Calendar cal = Calendar.getInstance();
			int month = cal.get(Calendar.MONTH);
			
			int oneMonthAgo = (month - 0 + 12) % 12; if(oneMonthAgo == 0) oneMonthAgo = 12;
			int twoMonthAgo = (month - 1 + 12) % 12; if(twoMonthAgo == 0) twoMonthAgo = 12;
			int threeMonthAgo = (month - 2 + 12) % 12; if(threeMonthAgo == 0) threeMonthAgo = 12;
			
			String month1 = getStringAmountMonth(oneMonthAgo , viewData.amountInOneMonthAgo);
			if(oneMonthAgo <10){
				tvSalesLast3Month1.setText(Constants.STR_SPACE + month1);
			}else {
				tvSalesLast3Month1.setText(month1);
			}
			String month2 = getStringAmountMonth(twoMonthAgo , viewData.amountInTwoMonthAgo);
			if(twoMonthAgo <10){
				tvSalesLast3Month2 .setText(Constants.STR_SPACE + month2);
			}else {
				tvSalesLast3Month2 .setText(month2);
			}
			
			String month3 = getStringAmountMonth(threeMonthAgo , viewData.amountInThreeMonthAgo);
			if(threeMonthAgo <10){
				tvSalesLast3Month3 .setText(Constants.STR_SPACE + month3);
			}else {
				tvSalesLast3Month3 .setText(month3);
			}
			
			//tvCustomerSellInDay.setText(viewData.getCustomer().getCustomer_name());
			renderCustomerDisplayPrograme();
			renderDataLastSaleOrder();
			// hien thi noi dung
			llContent.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * get amount string + label month
	 * @author: duongdt3
	 * @since: 15:01:41 27 Dec 2013
	 * @return: String
	 * @throws:  
	 * @param month
	 * @param amount
	 * @return
	 */
	String getStringAmountMonth(int month, long amount){
		String result = "";
		//nếu < 10 thêm khoảng trống
		String strMonth = month < 10 ? " " + month : String.valueOf(month);
		result  = StringUtil.getString(R.string.TEXT_MONTH) + " " + strMonth + ": \t" + StringUtil.parseAmountMoney(amount); 
		return result;
	}
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.GET_CUSTOMER_BASE_INFO:
			customerInfoDTO = (CustomerInfoDTO) modelEvent.getModelData();
			renderData(customerInfoDTO);
			if(!StringUtil.isNullOrEmpty(customerInfoDTO.getCustomer().getURL())){
				if(customerInfoDTO.getCustomer().getURL().contains("/images/")){
					customerInfoDTO.getCustomer().setURL(ServerPath.IMAGE_PATH+customerInfoDTO.getCustomer().getURL());
				}
				ImageUtil.getImageFromURL(customerInfoDTO.getCustomer().getURL(), parent, ivAvatar);
				hasAvatar=true;
			}
			if(StringUtil.isNullOrEmpty(customerInfoDTO.getCustomer().getCustomerAvatarID())){
				getMaxCustomerAvatarID();
			}
			if(!StringUtil.isNullOrEmpty(thumbURLFrom)){
				isNewAvatar=false;
				if(!hasAvatar){
					hasAvatar=true;
					// insert new row
					insertAvatarAlbum();
				}else{
					// update avatar
					updateAvatar();
				}
			}
			
			isFirstInitView = false;
			parent.closeProgressDialog();
			
			if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_VALSALES
					|| GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES) {
				requestInsertLogKPI(HashMapKPI.NVBH_CHITIETKHACHHANG, modelEvent.getActionEvent().startTimeFromBoot);
			} else if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSNPP) {
				requestInsertLogKPI(HashMapKPI.GSNPP_CHITIETKHACHHANG, modelEvent.getActionEvent().startTimeFromBoot);
			}else if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_TNPG) {
				requestInsertLogKPI(HashMapKPI.TTTT_CHITIETKHACHHANG, modelEvent.getActionEvent().startTimeFromBoot);
			}
			break;
		case ActionEventConstant.UPLOAD_PHOTO_TO_SERVER:
			parent.closeProgressDialog();
			mediaItemID=Constants.STR_BLANK+modelEvent.getModelData();
			isNewAvatar=true;
			if(!hasAvatar){
				hasAvatar=true;
				// insert new row
				insertAvatar();
			}else{
				// update avatar
				updateAvatar();
			}
			break;
		case ActionEventConstant.UPDATE_AVATAR:
		case ActionEventConstant.INSERT_NEW_AVATAR_ALBUM:
		case ActionEventConstant.INSERT_NEW_AVATAR:
			if(isNewAvatar){
				ImageUtil.getImageFromURL(takenPhoto.getAbsolutePath(), parent, ivAvatar);
			}else{
				String thumbnailURL=ServerPath.IMAGE_PATH+thumbURLFrom;
				ImageUtil.getImageFromURL(thumbnailURL, parent, ivAvatar);
			}
			break;
		case ActionEventConstant.GET_MAX_CUSTOMER_AVATAR_ID:
			customerInfoDTO.getCustomer().setCustomerAvatarID(modelEvent.getModelData().toString());
			break;
		default:
			break;
		}
	}	

	/**
	* Them mới hình đại diện khách hang
	* @author: dungdq3
	* @return: void
	* @date: Jan 6, 2014
	*/
	private void insertAvatar() {
		// TODO Auto-generated method stub
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_MEDIA_ITEM_ID, mediaItemID);
		data.putString(IntentConstants.INTENT_CUSTOMER_CODE, GlobalInfo.getInstance().getProfile().getUserData().userCode);
		if(StringUtil.isNullOrEmpty(URL)){
			data.putString(IntentConstants.INTENT_THUMB_URL, thumbURLFrom);
		}else if(StringUtil.isNullOrEmpty(thumbURLFrom)){
			data.putString(IntentConstants.INTENT_THUMB_URL, URL);
		}
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		data.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);
		data.putString(IntentConstants.INTENT_STATUS, "1");
		data.putString(IntentConstants.INTENT_STAFF_ID, String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
		data.putBoolean(IntentConstants.INTENT_KEY, isNewAvatar);
		
		ActionEvent event = new ActionEvent();
		event.action = ActionEventConstant.INSERT_NEW_AVATAR;
		event.sender = this;
		event.viewData = data;
		SaleController.getInstance().handleViewEvent(event);
	}
	
	/**
	 * Them mới hình đại diện khách hang
	 * @author: dungdq3
	 * @return: void
	 * @date: Jan 6, 2014
	 */
	private void insertAvatarAlbum() {
		// TODO Auto-generated method stub
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_MEDIA_ITEM_ID, mediaItemID);
		data.putString(IntentConstants.INTENT_CUSTOMER_CODE, GlobalInfo.getInstance().getProfile().getUserData().userCode);
		if(StringUtil.isNullOrEmpty(URL)){
			data.putString(IntentConstants.INTENT_THUMB_URL, thumbURLFrom);
		}else if(StringUtil.isNullOrEmpty(thumbURLFrom)){
			data.putString(IntentConstants.INTENT_THUMB_URL, URL);
		}
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		data.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);
		data.putString(IntentConstants.INTENT_STATUS, "1");
		data.putString(IntentConstants.INTENT_STAFF_ID, String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
		data.putBoolean(IntentConstants.INTENT_KEY, isNewAvatar);
		
		ActionEvent event = new ActionEvent();
		event.action = ActionEventConstant.INSERT_NEW_AVATAR_ALBUM;
		event.sender = this;
		event.viewData = data;
		SaleController.getInstance().handleViewEvent(event);
	}

	/**
	* Cập nhật hình đại diện khách hang
	* @author: dungdq3
	* @return: void
	* @date: Jan 6, 2014
	*/
	private void updateAvatar() {
		// TODO Auto-generated method stub
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_MEDIA_ITEM_ID, mediaItemID);
		data.putString(IntentConstants.INTENT_CUSTOMER_CODE, GlobalInfo.getInstance().getProfile().getUserData().userCode);
		data.putString(IntentConstants.INTENT_CUSTOMER_AVATAR_ID, customerInfoDTO.getCustomer().getCustomerAvatarID());
		if(StringUtil.isNullOrEmpty(URL)){
			data.putString(IntentConstants.INTENT_THUMB_URL, thumbURLFrom);
		}else if(StringUtil.isNullOrEmpty(thumbURLFrom)){
			data.putString(IntentConstants.INTENT_THUMB_URL, URL);
		}
		data.putString(IntentConstants.INTENT_STAFF_ID, String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
		data.putBoolean(IntentConstants.INTENT_KEY, isNewAvatar);
		
		ActionEvent event = new ActionEvent();
		event.action = ActionEventConstant.UPDATE_AVATAR;
		event.sender = this;
		event.viewData = data;
		SaleController.getInstance().handleViewEvent(event);
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		super.handleErrorModelViewEvent(modelEvent);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
//		case MENU_REVIEW:
//			GlobalUtil.popBackStack(this.getActivity());
//			gotoReviewsStaffView();
//			break;
		case ACTION_DELETE_AVATAR:
			ivAvatar.setImageResource(R.drawable.icon_app);
			hasAvatar=false;
			updateAvatar.dismiss();
			deleteAvatar();
			break;
		case MENU_LIST_FEEDBACKS: {
			CustomerListView frag = (CustomerListView) this.getActivity().getFragmentManager()
					.findFragmentByTag(CustomerListView.TAG);
			if (frag != null) {
				frag.isBackFromPopStack = true;
			}
			CustomerListViewPG fragPG = (CustomerListViewPG) this.getActivity().getFragmentManager()
					.findFragmentByTag(CustomerListViewPG.TAG);
			if (fragPG != null) {
				fragPG.isBackFromPopStack = true;
			}
			TrainingListView fragTrain = (TrainingListView) this.getActivity().getFragmentManager()
					.findFragmentByTag(TrainingListView.TAG);
			if (fragTrain != null) {
				fragTrain.isBackFromPopStack = true;
				fragTrain.isBackFromPopStackNot = true;
			}
			GlobalUtil.popBackStack(this.getActivity());
			gotoFeedBackList();
		}
			break;
		case MENU_MAP: {
			CustomerListView frag = (CustomerListView) this.getActivity().getFragmentManager()
					.findFragmentByTag(CustomerListView.TAG);
			if (frag != null) {
				frag.isBackFromPopStack = true;
			}
			CustomerListViewPG fragPG = (CustomerListViewPG) this.getActivity().getFragmentManager()
					.findFragmentByTag(CustomerListViewPG.TAG);
			if (fragPG != null) {
				fragPG.isBackFromPopStack = true;
			}
			TrainingListView fragTrain = (TrainingListView) this.getActivity().getFragmentManager()
					.findFragmentByTag(TrainingListView.TAG);
			if (fragTrain != null) {
				fragTrain.isBackFromPopStack = true;
				fragTrain.isBackFromPopStackNot = true;
			}
			GlobalUtil.popBackStack(this.getActivity());
			gotoCustomerLocation();
		}
			break;
		case MENU_IMAGE: {
			CustomerListView frag = (CustomerListView) this.getActivity().getFragmentManager()
					.findFragmentByTag(CustomerListView.TAG);
			if (frag != null) {
				frag.isBackFromPopStack = true;
			}
			CustomerListViewPG fragPG = (CustomerListViewPG) this.getActivity().getFragmentManager()
					.findFragmentByTag(CustomerListViewPG.TAG);
			if (fragPG != null) {
				fragPG.isBackFromPopStack = true;
			}
			TrainingListView fragTrain = (TrainingListView) this.getActivity().getFragmentManager()
					.findFragmentByTag(TrainingListView.TAG);
			if (fragTrain != null) {
				fragTrain.isBackFromPopStack = true;
				fragTrain.isBackFromPopStackNot = true;
			}
			if (customerInfoDTO.getCustomer() == null) {
				break;
			}
			GlobalUtil.popBackStack(this.getActivity());
			gotoListAlbumUser(customerInfoDTO.getCustomer());
		}
			break;
		case ActionEventConstant.ACTION_TAKE_PHOTO:
			takePhoto();
			break;
		default:
			break;
		}

	}
	
	/**
	* Xoa avatar tren server
	* @author: dungdq3
	* @return: void
	* @date: Jan 4, 2014
	*/
	private void deleteAvatar() {
		// TODO Auto-generated method stub
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		data.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);
		data.putString(IntentConstants.INTENT_MEDIA_ITEM_ID, mediaItemID);
		data.putString(IntentConstants.INTENT_STATUS, "0");
		data.putString(IntentConstants.INTENT_CUSTOMER_CODE, GlobalInfo.getInstance().getProfile().getUserData().userCode);
		data.putString(IntentConstants.INTENT_CUSTOMER_AVATAR_ID, customerInfoDTO.getCustomer().getCustomerAvatarID());
		data.putString(IntentConstants.INTENT_STAFF_ID, String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
		
		ActionEvent event = new ActionEvent();
		event.action = ActionEventConstant.DELETE_AVATAR;
		event.sender = this;
		event.viewData = data;
		SaleController.getInstance().handleViewEvent(event);
	}

	/**
	 * Chụp hình để làm hình đại diện khách hang
	 * 
	 * @author: dungdq3
	 * @return: void
	 * @date: Jan 3, 2014
	 */
	private void takePhoto() {
		// TODO Auto-generated method stub
		LatLng cusLatLng = new LatLng(customerInfoDTO.getCustomer().lat,
				customerInfoDTO.getCustomer().lng);
		LatLng myLatLng = new LatLng(GlobalInfo.getInstance().getProfile()
				.getMyGPSInfo().getLatitude(), GlobalInfo.getInstance()
				.getProfile().getMyGPSInfo().getLongtitude());
		//neu hoat dong che do khong ket noi thi ko check k.c chup anh
		if(GlobalInfo.getInstance().getStateConnectionMode() == Constants.CONNECTION_OFFLINE){
			takenPhoto = GlobalUtil.takePhoto(this, GlobalBaseActivity.RQ_TAKE_PHOTO);
		}else if ((myLatLng.lat > 0 && myLatLng.lng > 0 && cusLatLng.lat > 0
				&& cusLatLng.lng > 0)
			 && GlobalUtil.getDistanceBetween(myLatLng, cusLatLng) <= customerInfoDTO.getCustomer().shopDistance) {
			takenPhoto = GlobalUtil.takePhoto(this, GlobalBaseActivity.RQ_TAKE_PHOTO);
		} else {
			String mess = StringUtil
					.getString(R.string.TEXT_TAKE_PHOTO_TOO_FAR_FROM_SHOP_1)
					+ " "
					+ customerInfoDTO.getCustomer().customerCode
					.substring(0, 3)
					+ " - "
					+ customerInfoDTO.getCustomer().customerName
					+ " "
					+ StringUtil
							.getString(R.string.TEXT_TAKE_PHOTO_TOO_FAR_FROM_SHOP_2);

			parent.showDialog(mess);
		}
	}
	
	/**
	* Lay gia tri lon nhat
	* @author: dungdq3
	* @param: Tham số của hàm
	* @return: void
	* @throws: Ngoại lệ do hàm đưa ra (nếu có)
	* @date: Jan 6, 2014
	*/
	private void getMaxCustomerAvatarID() {
		// TODO Auto-generated method stub
		Bundle data = new Bundle();
		ActionEvent event = new ActionEvent();
		event.action = ActionEventConstant.GET_MAX_CUSTOMER_AVATAR_ID;
		event.sender = this;
		event.viewData = data;
		SaleController.getInstance().handleViewEvent(event);
	}

	/**
	 * Di toi man hinh danh gia nhan vien Danh cho module gs npp
	 * 
	 * @author banghn
	 */
	public void gotoReviewsStaffView() {
		Bundle data = new Bundle();
		ActionEvent event = new ActionEvent();
		event.action = ActionEventConstant.GO_TO_REVIEWS_STAFF_VIEW;
		event.sender = this;
		event.viewData = data;
		SuperviorController.getInstance().handleSwitchFragment(event);
	}

	/**
	 * Toi man hinh ds album cua kh
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param customer
	 * @return: void
	 * @throws:
	 */

	private void gotoListAlbumUser(CustomerDTO customer) {
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentPrefixTitle());
		bundle.putSerializable(IntentConstants.INTENT_CUSTOMER, customer);
		bundle.putString(IntentConstants.INTENT_SENDER, tagFrom);
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_LIST_ALBUM_USER;

		int typeUser = GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType;
		if(typeUser == UserDTO.TYPE_VALSALES || typeUser == UserDTO.TYPE_PRESALES){
			SaleController.getInstance().handleSwitchFragment(e); 
		}else if(typeUser == UserDTO.TYPE_TNPG){
			TNPGController.getInstance().handleSwitchFragment(e);
		}else if(typeUser == UserDTO.TYPE_GSNPP){
			SuperviorController.getInstance().handleSwitchFragment(e); 
		}else{
			SaleController.getInstance().handleSwitchFragment(e); 
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		if (control == tbLastSalesInfo) {
			currentPage = tbLastSalesInfo.getPagingControl().getCurrentPage();
			// getLastSaleOrder();
		}

	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		// TODO Auto-generated method stub
		switch (action) {
		case ActionEventConstant.GO_TO_ORDER_VIEW:
			SaleOrderDTO saleOrderDTO=(SaleOrderDTO) data;
			gotoOrderDetail(saleOrderDTO);
			break;

		default:
			break;
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible() && !StringUtil.isNullOrEmpty(customerId)) {
				thumbURLFrom=Constants.STR_BLANK;
				parent.showLoadingDialog();
				getCustomerInfo(customerId);
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		String filePath = "";
		switch (requestCode) {
		case GlobalBaseActivity.RQ_TAKE_PHOTO:
			VTLog.i("TamPQ", "end camera");
			if (resultCode == Activity.RESULT_OK) {
				filePath = takenPhoto.getAbsolutePath();
				ImageValidatorTakingPhoto validator = new ImageValidatorTakingPhoto(parent, filePath,
						Constants.MAX_FULL_IMAGE_HEIGHT);
				validator.setDataIntent(data);
				if (validator.execute()) {
					VTLog.e("TakePhoto", ".................result taking photo : OK");
					updateTakenPhoto();
				}
			}
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}
	
	/**
	 * Cap nhat hinh anh
	 * 
	 * @author quangvt1
	 * @return void
	 */
	public void updateTakenPhoto() { 
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		Vector<String> viewData = new Vector<String>();

//		viewData.add(IntentConstants.INTENT_STAFF_ID);
//		viewData.add(Integer.toString(GlobalInfo.getInstance().getProfile().getUserData().id));
//
//		viewData.add(IntentConstants.INTENT_SHOP_ID);
//		String shopIdForUpPhoto = GlobalInfo.getInstance().getProfile().getUserData().shopIdProfile;
//		if (!StringUtil.isNullOrEmpty(shopIdForUpPhoto)) {
//			viewData.add(shopIdForUpPhoto);
//		}

		if (takenPhoto != null) {
			viewData.add(IntentConstants.INTENT_FILE_NAME);
			viewData.add(takenPhoto.getName());
			viewData.add(IntentConstants.INTENT_TAKEN_PHOTO);
			viewData.add(takenPhoto.getAbsolutePath());
		}

		// tao doi tuong mediaItem de insert row du lieu
		MediaItemDTO dto = new MediaItemDTO();

		try {
			dto.objectId = Long.parseLong(customerInfoDTO.getCustomer().getCustomerId());
			dto.objectType = MediaItemDTO.TYPE_LOCATION_IMAGE;
			dto.mediaType = 0;// loai hinh anh , 1 loai video
			dto.url = takenPhoto.getAbsolutePath();
			dto.thumbUrl = takenPhoto.getAbsolutePath();
			dto.createDate = DateUtils.now();
			dto.createUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
			dto.lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
			dto.lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
			dto.type = 1;
			dto.status = 1;
			if (!StringUtil.isNullOrEmpty(GlobalInfo.getInstance().getProfile().getUserData().shopId)) {
				dto.shopId = Integer.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId);
			}

			dto.fileSize = takenPhoto.length();
			dto.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
			viewData.add(IntentConstants.INTENT_OBJECT_ID);
			viewData.add(customerInfoDTO.getCustomer().getCustomerId());
			viewData.add(IntentConstants.INTENT_OBJECT_TYPE_IMAGE);
			viewData.add(String.valueOf(dto.objectType));
			viewData.add(IntentConstants.INTENT_MEDIA_TYPE);
			viewData.add(String.valueOf(dto.mediaType));
			viewData.add(IntentConstants.INTENT_URL);
			viewData.add(String.valueOf(dto.url));
			viewData.add(IntentConstants.INTENT_THUMB_URL);
			viewData.add(String.valueOf(dto.thumbUrl));
			URL=dto.thumbUrl;
			viewData.add(IntentConstants.INTENT_CREATE_DATE);
			viewData.add(String.valueOf(dto.createDate));
			viewData.add(IntentConstants.INTENT_CREATE_USER);
			viewData.add(String.valueOf(dto.createUser));
			viewData.add(IntentConstants.INTENT_LAT);
			viewData.add(String.valueOf(dto.lat));
			viewData.add(IntentConstants.INTENT_LNG);
			viewData.add(String.valueOf(dto.lng));
			viewData.add(IntentConstants.INTENT_FILE_SIZE);
			viewData.add(String.valueOf(dto.fileSize)); 
			viewData.add(IntentConstants.INTENT_CUSTOMER_CODE);
			viewData.add(customerInfoDTO.getCustomer().customerCode);
			viewData.add(IntentConstants.INTENT_STATUS);
			viewData.add("1");
			viewData.add(IntentConstants.INTENT_TYPE);
			viewData.add("1");
			viewData.add(IntentConstants.INTENT_CUSTOMER_AVATAR_ID);
			viewData.add(customerInfoDTO.getCustomer().getCustomerAvatarID());
		} catch (NumberFormatException e1) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e1));
		}

		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.UPLOAD_PHOTO_TO_SERVER;
		e.viewData = viewData;
		e.userData = dto;
		e.sender = this;
		UserController.getInstance().handleViewEvent(e);
	}
	
	@Override
	protected int getTabIndex() { 
		return TAB_INDEX;
	}
	
	@Override
	protected String getTitleString() {  
		String title = Constants.STR_BLANK;
		
		int role = GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType;
		if (role == UserDTO.TYPE_PRESALES || role == UserDTO.TYPE_VALSALES) {
			title = StringUtil.getString(R.string.TITLE_VIEW_CUSTOMER_INFO_DEFAULT);
		} else if (role == UserDTO.TYPE_TNPG) {
			title = StringUtil.getString(R.string.TITLE_VIEW_CUSTOMER_INFO_DEFAULT);
		} else if (role == UserDTO.TYPE_GSNPP) {
			title = StringUtil.getString(R.string.TITLE_VIEW_CUSTOMER_INFO_DEFAULT);
 		} else {
 			title = StringUtil.getString(R.string.TITLE_VIEW_GSNPP_CUSTOMER_INFO);
		}
				
		return title;
	}
}