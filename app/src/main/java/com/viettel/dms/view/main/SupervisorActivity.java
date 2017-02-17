/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.control.MenuItemDTO;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.MediaItemDTO;
import com.viettel.dms.dto.db.ShopDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.GeneralStatisticsTNPGViewDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.TransactionProcessManager;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.sale.customer.CustomerInfoView;
import com.viettel.dms.view.sale.order.FindProductAddOrderListView;
import com.viettel.dms.view.sale.order.RemainProductView;
import com.viettel.dms.view.supervisor.CustomerSaleList;
import com.viettel.dms.view.supervisor.FollowProblemView;
import com.viettel.dms.view.supervisor.GSBHRouteSupervisionView;
import com.viettel.dms.view.supervisor.ManagerEquipmentView;
import com.viettel.dms.view.supervisor.ReportProgressDateView;
import com.viettel.dms.view.supervisor.SuperVisorPromotionProgramView;
import com.viettel.sabeco.R;
import com.viettel.utils.ImageValidatorTakingPhoto;
import com.viettel.utils.VTLog;

/**
 * Activity chinh: cua nhan vien giam sat nha phan phoi
 * 
 * @author : BangHN since : 1.0
 */
public class SupervisorActivity extends GlobalBaseActivity implements OnClickListener, OnItemClickListener {
	public static final int CONFIRM_EXIT_APP_OK = 11;
	public static final int CONFIRM_EXIT_APP_CANCEL = 12;
	private static final int ACTION_FINISH_VISIT_CUS_OK = 13;
	private static final int ACTION_FINISH_VISIT_CUS_CANCEL = 14;
	// confirm show camera
	public static final int CONFIRM_SHOW_CAMERA_WHEN_CLICK_CLOSE_DOOR = 15;
	boolean isShowMenu = false;
	ImageView iconLeft;
	TextView tvTitleMenu;
	LinearLayout llMenu;// layout header icon

	ArrayList<MenuItemDTO> listMenu;
	ListView lvMenu;
	MenuAdapter menuAdapter;
	boolean isShowTextInMenu = false;
	boolean isTakePhotoFromMenuCloseCustomer = false;
	boolean isSaveActionLogAndCloseCustomerAfterTakePhoto = false;
	// dialog hien thi danh sach NPP
	private AlertDialog alertListShopDialog;
	// action sau khi chon NPP quan ly
	private final int ACTION_SELECTED_SHOP_MANAGE = 106;
	public static final String DMS_SHOP_ID = "com.viettel.dms.shopId";
	public static final String DMS_SHOP_CODE = "com.viettel.dms.shopCode";
	public static final String DMS_SHOP_MANAGED = "com.viettel.dms.shopManaged";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		if (GlobalInfo.getInstance().isDebugMode) {
			setTitleName(GlobalInfo.getInstance().getProfile().getUserData().userCode);
		} else {
			setTitleName(GlobalInfo.getInstance().getProfile().getUserData().userCode);
		}
		llMenuUpdate.setVisibility(View.VISIBLE);
		llMenuGPS.setVisibility(View.VISIBLE);

		if(GlobalInfo.getInstance().getProfile().getUserData().shopManaged != null) {
			String shopSelectedStr = "NPP: " + GlobalInfo.getInstance().getProfile().getUserData().shopManaged.shopName;
			SpannableString spannableString = new SpannableString(shopSelectedStr);
			spannableString.setSpan(new UnderlineSpan(), 0, shopSelectedStr.length(), 0);
			setShopSelected(spannableString);
		}

		llShowHideMenu.setOnClickListener(this);
		iconLeft = (ImageView) llShowHideMenu.findViewById(R.id.ivLeftIcon);
		tvTitleMenu = (TextView) findViewById(R.id.tvTitleMenu);
		llMenu = (LinearLayout) findViewById(R.id.llMenu);
		lvMenu = (ListView) findViewById(R.id.lvMenu);

		llShopSelected.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showDialogListNPP();
			}
		});

		initMenu();
		showHideMenuText(isShowMenu);
		gotoReportProgressDate();// bao cao theo ngay

		// TamPQ : xu ly trang thai ghe tham neu thoat dot ngot ( force close)
		checkVisitFromActionLog();

		// check va gui toan bo log dang con ton dong
		TransactionProcessManager.getInstance().startChecking(TransactionProcessManager.SYNC_NORMAL);
	}

	/**
	 * Hien thi danh sach NPP can quan ly
	 *
	 * @author: BangHN
	 * @return: void
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	private void showDialogListNPP() {
		showLoadingDialog();
		AlertDialog.Builder build = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
		ListShopManagedView view = new ListShopManagedView(this, GlobalInfo.getInstance().getProfile().getUserData().listShop, ACTION_SELECTED_SHOP_MANAGE);
		build.setView(view.viewLayout);
		alertListShopDialog = build.create();
		Window window = alertListShopDialog.getWindow();
		window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
		window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER);
		if (!alertListShopDialog.isShowing() && !isFinishing()) {
			alertListShopDialog.show();
		}
		closeProgressDialog();
	}

	/**
	 * kiem tra duoi DB lan login truoc co dang ghe tham khach hang nao ko
	 * 
	 * @author TamPQ
	 */
	private void checkVisitFromActionLog() {
		ActionEvent e = new ActionEvent();
		Vector<String> v = new Vector<String>();
		v.add(IntentConstants.INTENT_STAFF_ID);
		v.add(String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));

		e.viewData = v;
		e.action = ActionEventConstant.CHECK_VISIT_FROM_ACTION_LOG;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * Khoi tao menu chuong trinh
	 * 
	 * @author : BangHN since : 4:41:56 PM
	 */
	private void initMenu() {
		listMenu = new ArrayList<MenuItemDTO>();
		MenuItemDTO item1 = new MenuItemDTO(StringUtil.getString(R.string.TEXT_PROGRESS_SALE),
				R.drawable.menu_report_icon);
		MenuItemDTO item2 = new MenuItemDTO(StringUtil.getString(R.string.TEXT_ROUTE_MANAGE),
				R.drawable.menu_customer_icon);
		MenuItemDTO item3 = new MenuItemDTO(StringUtil.getString(R.string.TEXT_GET_INFORMATION),
				R.drawable.menu_manage_icon);
//		MenuItemDTO item4 = new MenuItemDTO(StringUtil.getString(R.string.TEXT_TRAINING_SALEMAN),
//				R.drawable.icon_training);
		MenuItemDTO item5 = new MenuItemDTO(StringUtil.getString(R.string.TEXT_CATEGORY), R.drawable.icon_category);
		MenuItemDTO item6 = new MenuItemDTO(StringUtil.getString(R.string.TEXT_PROBLEMS_MANAGE),
				R.drawable.menu_problem_icon);
		MenuItemDTO item7 = new MenuItemDTO(StringUtil.getString(R.string.TEXT_CUSTOMER_LIST),
				R.drawable.icon_customer_list);
		MenuItemDTO item8 = new MenuItemDTO(StringUtil.getString(R.string.TEXT_TRAINING),
				R.drawable.icon_menu_training);
		listMenu.add(item1);
		listMenu.add(item2);
		listMenu.add(item3);
//		listMenu.add(item4);
		listMenu.add(item5);
		listMenu.add(item6);
		listMenu.add(item7);
		listMenu.add(item8);

		menuAdapter = new MenuAdapter(this, 0, listMenu);
		lvMenu.setAdapter(menuAdapter);
		lvMenu.setOnItemClickListener(this);
		listMenu.get(0).setSelected(true);
		lvMenu.setSelection(0);
		menuAdapter.notifyDataSetChanged();
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
			case ActionEventConstant.CHECK_VISIT_FROM_ACTION_LOG:
				ActionLogDTO action = (ActionLogDTO) modelEvent.getModelData();
				if (action != null) {
//					GlobalInfo.getInstance().getProfile().setActionLogVisitCustomer(action);
//					requestUpdateActionLog("0", null, null, this);
					GlobalInfo.getInstance().getProfile().setVisitingCustomer(true);
					GlobalInfo.getInstance().getProfile()
							.setActionLogVisitCustomer(action);
					initMenuVisitCustomerForNVBH(action.aCustomer.shortCode,
							action.aCustomer.customerName, action.objectType,
							action.isOr, action.haveAction);
				}
				break;
			case ActionEventConstant.UPLOAD_PHOTO_TO_SERVER: {
				if (this.isSaveActionLogAndCloseCustomerAfterTakePhoto) {
					this.isSaveActionLogAndCloseCustomerAfterTakePhoto = false;
					this.closeProgressDialog();
				} else {
					super.handleModelViewEvent(modelEvent);
				}
				break;
			}
			default:
				super.handleModelViewEvent(modelEvent);
				break;
		}
	}
	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
			case ActionEventConstant.UPLOAD_PHOTO_TO_SERVER: {
				if (this.isSaveActionLogAndCloseCustomerAfterTakePhoto) {
					this.isSaveActionLogAndCloseCustomerAfterTakePhoto = false;
					this.closeProgressDialog();
				} else {
					super.handleErrorModelViewEvent(modelEvent);
				}
				break;
			}
			default:
				super.handleErrorModelViewEvent(modelEvent);
				break;
		}
	}
	@Override
	public void onClick(View v) {
		if (v == llShowHideMenu) {
			isShowMenu = !isShowMenu;

			if (!isShowMenu) {
				iconLeft.setVisibility(View.GONE);
				tvTitleMenu.setVisibility(View.GONE);
			} else {
				iconLeft.setVisibility(View.VISIBLE);
				tvTitleMenu.setVisibility(View.VISIBLE);
			}
			showHideMenuText(isShowMenu);
		} else {
			super.onClick(v);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (isValidateMenu(arg2))
			return;
//		// TamPQ: giai phong TrainingReviewStaff de phan biet luong Hinh anh WW
//		// voi DS Hinh anh
//		setTrainingReviewStaffBundle(null);
//		// TamPQ: xu ly ket thuc danh gia NVBH
//		if (arg2 != 3) {// chuyen sang module ko phai la Huyen luyen
//			if (llMenubar.getChildCount() > 0) { // co hien thi ten KH tren menu
//				ActionLogDTO actionLog = GlobalInfo.getInstance().getProfile().getActionLogVisitCustomer();
//				if (actionLog != null && !StringUtil.isNullOrEmpty(actionLog.startTime)
//						&& StringUtil.isNullOrEmpty(actionLog.endTime)) {
//					String textConfirm = Constants.STR_BLANK;
//					if(!StringUtil.isNullOrEmpty(actionLog.aCustomer.customerCode)) {
//						textConfirm = "Có thực hiện kết thúc đánh giá khách hàng "
//							+ actionLog.aCustomer.customerName + " - "
//							+ actionLog.aCustomer.customerCode.substring(0, 3) + " hiện tại không?";
//
//					}else{
//						textConfirm = "Có thực hiện kết thúc đánh giá khách hàng "
//								+ actionLog.aCustomer.customerName
//								+ " hiện tại không?";
//					}
//					GlobalUtil.showDialogConfirmCanBackAndTouchOutSide(this, textConfirm, StringUtil.getString(R.string.TEXT_AGREE),
//							ACTION_FINISH_VISIT_CUS_OK, StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), ACTION_FINISH_VISIT_CUS_CANCEL, arg2, true, true);
//				} else {
//					notifyChangeSelection(arg2);
//					showDetails(arg2);
//					endVisitCustomerBar();
//				}
//			} else {
//				notifyChangeSelection(arg2);
//				showDetails(arg2);
//				endVisitCustomerBar();
//			}
//		} else {
		notifyChangeSelection(arg2);
		showDetails(arg2);
//		}
	}

	/**
	 * Thay doi selection
	 * 
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void notifyChangeSelection(int index) {
		for (int i = 0; i < listMenu.size(); i++) {
			if (i == index)
				listMenu.get(i).setSelected(true);
			else
				listMenu.get(i).setSelected(false);
		}
		menuAdapter.notifyDataSetChanged();
	}

	/**
	 * Show cac manhinh detail
	 * 
	 * @author : BangHN since : 4:42:12 PM
	 */
	void showDetails(int index) {
		switch (index) {
		case 0:
			gotoReportProgressDate();// bao cao theo ngay
			break;
		case 1:
			gotoSaleRoadMapSupervior();
			break;
		case 2:
			gotoCustomerListOfC2();
			break;
		case 3:
			gotoSuperPromotionProgrameView();
			break;
		case 4:
			gotoFollowProblemView();
			break;
		case 5:
			gotoCustomerSaleList();
			break;
		case 6:
			gotoTraining();
			break;
		default:
			break;
		}
	}

	/**
	 * Hien thi man hinh danh sach khuyen mai
	 * 
	 * @author: ThanhNN8
	 * @return: void
	 * @throws:
	 */
	private void gotoSuperPromotionProgrameView() {
		// TODO Auto-generated method stub
		ActionEvent e = new ActionEvent();
		e.sender = this;
//		e.action = ActionEventConstant.GET_LIST_PROMOTION_PROGRAME;
		e.action = ActionEventConstant.GO_TO_PRODUCT_LIST;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Hien thi man hinh theo doi khac phuc cua gsnpp
	 * 
	 * @author: ThanhNN8
	 * @return: void
	 * @throws:
	 */
	private void gotoFollowProblemView() {
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GO_TO_FOLLOW_LIST_PROBLEM;
		e.sender = this;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	@SuppressWarnings("unused")
	private void gotoReportTrainResult() {
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GSNPP_TRAINING_PLAN;
		e.sender = this;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	private void gotoSaleRoadMapSupervior() {
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GET_GSNPP_ROUTE_SUPERVISION;
		e.sender = this;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	private void gotoCustomerSaleList() {
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GO_TO_CUSTOMER_SALE_LIST;
		e.sender = this;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Đến màn hình huấn luyện
	 */
	private void gotoTraining() {
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GO_TO_TRAINING_LIST;
		e.sender = this;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}


	/**
	 * validate khi nhan menu chinh
	 * 
	 * @author : BangHN since : 1.0
	 */
	private boolean isValidateMenu(int index) {
		boolean isOk = false;
		FragmentManager fm = getFragmentManager();
		BaseFragment fg;
		if (listMenu.get(index).isSelected()) {
			switch (index) {
			case 0:
				fg = (BaseFragment) fm.findFragmentByTag(ReportProgressDateView.TAG);
				if (fg != null && fg.isVisible()) {
					isOk = true;
				}
				break;
			case 1:
				fg = (BaseFragment) fm.findFragmentByTag(GSBHRouteSupervisionView.TAG);
				if (fg != null && fg.isVisible()) {
					isOk = true;
				}
				break;
			case 2:
				fg = (BaseFragment) fm.findFragmentByTag(ManagerEquipmentView.TAG);
				if (fg != null && fg.isVisible()) {
					isOk = true;
				}
				break;
//			case 3:
//				fg = (BaseFragment) fm.findFragmentByTag(GSNPPTrainingPlanView.TAG);
//				if (fg != null && fg.isVisible()) {
//					isOk = true;
//				}
//				break;
			case 3:
				fg = (BaseFragment) fm.findFragmentByTag(SuperVisorPromotionProgramView.TAG);
				if (fg != null && fg.isVisible()) {
					isOk = true;
				}
				break;
			case 4:
				fg = (BaseFragment) fm.findFragmentByTag(FollowProblemView.TAG);
				if (fg != null && fg.isVisible()) {
					isOk = true;
				}
				break;
			default:// 5
				fg = (BaseFragment) fm.findFragmentByTag(CustomerSaleList.TAG);
				if (fg != null && fg.isVisible()) {
					isOk = true;
				}
				break;
			}
		}
		return isOk;
	}

//	private void gotoManagerEquipment() {
//		ActionEvent e = new ActionEvent();
//		e.sender = this;
//		e.viewData = new Bundle();
//		e.action = ActionEventConstant.ACTION_MANAGER_EQUIPMENT;
//		SuperviorController.getInstance().handleSwitchFragment(e);
//	}
	
	/**
	 * Di toi MH ds KH c2
	 * 
	 * @author : TamPQ since : 10:59:39 AM
	 */
	public void gotoCustomerListOfC2() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bunde = new Bundle();
		e.viewData = bunde;
		e.action = ActionEventConstant.GO_TO_CUSTOMER_LIST_C2;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	private void gotoReportProgressDate() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = new Bundle();
		e.action = ActionEventConstant.GO_TO_GENERAL_STATISTICS;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Show full menu ben trai
	 * 
	 * @author : BangHN since : 4:42:25 PM
	 */
	public void showFullMenu(boolean isShowFull) {
		isShowTextInMenu = isShowFull;
		menuAdapter.notifyDataSetChanged();
	}

	/**
	 * An hien full menu ben trai
	 * 
	 * @author : BangHN since : 4:42:55 PM
	 */
	private void showHideMenuText(boolean isShow) {
		showFullMenu(isShow);
		if (isShow) {
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(GlobalUtil.dip2Pixel(200),
					LayoutParams.FILL_PARENT);
			RelativeLayout.LayoutParams llMenuParam = new RelativeLayout.LayoutParams(GlobalUtil.dip2Pixel(200),
					LayoutParams.WRAP_CONTENT);
			lvMenu.setLayoutParams(layoutParams);
			llMenu.setLayoutParams(llMenuParam);
			iconLeft.setVisibility(View.VISIBLE);
			tvTitleMenu.setVisibility(View.VISIBLE);
		} else {
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(GlobalUtil.dip2Pixel(48),
					LayoutParams.FILL_PARENT);
			RelativeLayout.LayoutParams llMenuParam = new RelativeLayout.LayoutParams(GlobalUtil.dip2Pixel(50),
					LayoutParams.WRAP_CONTENT);
			lvMenu.setLayoutParams(layoutParams);
			llMenu.setLayoutParams(llMenuParam);
			iconLeft.setVisibility(View.GONE);
			tvTitleMenu.setVisibility(View.GONE);

		}
	}

	/**
	 * adapter list menu fragment
	 * 
	 * @author : BangHN since : 1:55:59 PM version :
	 */
	class MenuAdapter extends ArrayAdapter<MenuItemDTO> {
		List<MenuItemDTO> modelMenu;
		Context mContext;

		public MenuAdapter(Context context, int textViewResourceId, List<MenuItemDTO> listMenu) {
			super(context, textViewResourceId, listMenu);
			modelMenu = listMenu;
			mContext = context;
		}

		@Override
		public MenuItemDTO getItem(int position) {
			return listMenu.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			VTLog.i("GET VIEW", "" + position);
			MenuItemRow cell = null;
			if (row == null) {
				LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = vi.inflate(R.layout.layout_fragment_menu_item, null);
				cell = new MenuItemRow(SupervisorActivity.this, row);
				row.setTag(cell);
			} else {
				cell = (MenuItemRow) row.getTag();
			}
			cell.populateFrom(getItem(position));
			if (isShowTextInMenu) {
				cell.tvText.setVisibility(View.VISIBLE);
			} else {
				cell.tvText.setVisibility(View.GONE);
			}
			return row;
		}
	}

	@Override
	public void onBackPressed() {
		FragmentManager fm = getFragmentManager();
		if (fm.getBackStackEntryCount() == 1) {
			// finish();
			GlobalUtil.showDialogConfirm(this, StringUtil.getString(R.string.TEXT_CONFIRM_EXIT), "Đồng ý",
					CONFIRM_EXIT_APP_OK, "Hủy bỏ", CONFIRM_EXIT_APP_CANCEL, null);
		} else if (fm.getBackStackEntryCount() > 1) {
			int handleBack = -1;
			// dinh nghia cac luong xu ly su kien back dac biet
			if (handleBack == -1) {
				GlobalInfo.getInstance().popCurrentTag();
				super.onBackPressed();
			}
		} else {
			GlobalInfo.getInstance().popCurrentTag();
			super.onBackPressed();
		}
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
//		case MENU_FINISH_VISIT:
//			endVitCustomer();
//			break;
		case MENU_FINISH_VISIT:
				ActionLogDTO action = GlobalInfo.getInstance().getProfile()
						.getActionLogVisitCustomer();
				if (action != null && StringUtil.isNullOrEmpty(action.endTime)) {
					// kiem tra neu vao lai dung khach hang do thi khong
					// insertlog
					SpannableObject textConfirmed = new SpannableObject();
					textConfirmed.addSpan(StringUtil
									.getString(R.string.TEXT_ALREADY_VISIT_CUSTOMER),
							ImageUtil.getColor(R.color.WHITE),
							android.graphics.Typeface.NORMAL);
					textConfirmed.addSpan(
							" " + action.aCustomer.customerCode,
							ImageUtil.getColor(R.color.WHITE),
							android.graphics.Typeface.BOLD);
					textConfirmed.addSpan(" - ", ImageUtil.getColor(R.color.WHITE),
							android.graphics.Typeface.BOLD);
					textConfirmed.addSpan(action.aCustomer.customerName,
							ImageUtil.getColor(R.color.WHITE),
							android.graphics.Typeface.BOLD);
					textConfirmed.addSpan(" trong ",
							ImageUtil.getColor(R.color.WHITE),
							android.graphics.Typeface.NORMAL);
					textConfirmed.addSpan(DateUtils.getVisitTime(action.startTime),
							ImageUtil.getColor(R.color.WHITE),
							android.graphics.Typeface.BOLD);
					textConfirmed.addSpan(StringUtil
									.getString(R.string.TEXT_ASK_END_VISIT_CUSTOMER),
							ImageUtil.getColor(R.color.WHITE),
							android.graphics.Typeface.NORMAL);

					GlobalUtil.showDialogConfirmCanBackAndTouchOutSide(this,
							textConfirmed.getSpan(),
							StringUtil.getString(R.string.TEXT_AGREE),
							ACTION_FINISH_VISIT_CUS_OK,
							StringUtil.getString(R.string.TEXT_BUTTON_CLOSE),
							ACTION_FINISH_VISIT_CUS_CANCEL, null, true, true);
				}
				break;
		case MENU_FINISH_VISIT_CLOSE:
			this.showPopupConfirmCustomerClose();
			break;
		case CONFIRM_SHOW_CAMERA_WHEN_CLICK_CLOSE_DOOR:
			// show camera
			this.isTakePhotoFromMenuCloseCustomer = true;
			this.takenPhoto = GlobalUtil.takePhoto(this,GlobalBaseActivity.RQ_TAKE_PHOTO);
			break;
		case CONFIRM_EXIT_APP_OK:
			ActionLogDTO log = GlobalInfo.getInstance().getProfile()
					.getActionLogVisitCustomer();
			if (log != null && StringUtil.isNullOrEmpty(log.endTime) && log.isOr == 1) {
				endVitCustomer();
			}
//			sendBroadcast(ActionEventConstant.ACTION_FINISH_APP, new Bundle());
			TransactionProcessManager.getInstance().cancelTimer();
			//xu ly thoat ung dung
			processExitApp();
//			finish();
			break;
		case CONFIRM_EXIT_APP_CANCEL:
			break;
		case ACTION_FINISH_VISIT_CUS_OK:
//			requestUpdateActionLog("0", null, null, this);
//			int arg2 = (Integer) data;
//			notifyChangeSelection(arg2);
//			showDetails(arg2);
			endVisitCustomer();
			break;
		case ACTION_FINISH_VISIT_CUS_CANCEL:
			break;
		case ACTION_SELECTED_SHOP_MANAGE:
			ShopDTO shop = (ShopDTO) data;
			alertListShopDialog.cancel();
			if(shop.shopId != GlobalInfo.getInstance().getProfile().getUserData().shopManaged.shopId) {
				GlobalInfo.getInstance().getProfile().getUserData().shopManaged = shop;
				GlobalInfo.getInstance().getProfile().getUserData().shopId = "" + GlobalInfo.getInstance().getProfile().getUserData().shopManaged.shopId;
				GlobalInfo.getInstance().getProfile().getUserData().shopCode = GlobalInfo.getInstance().getProfile().getUserData().shopManaged.shopCode;
				saveUserInfo(GlobalInfo.getInstance().getProfile().getUserData());
				String shopSelectedStr = "NPP: " + GlobalInfo.getInstance().getProfile().getUserData().shopManaged.shopName;
				SpannableString spannableString = new SpannableString(shopSelectedStr);
				spannableString.setSpan(new UnderlineSpan(), 0, shopSelectedStr.length(), 0);
				setShopSelected(spannableString);
				gotoReportProgressDate();
				for (int i = 0; i < listMenu.size(); i++) {
					if (i == 0)
						listMenu.get(i).setSelected(true);
					else
						listMenu.get(i).setSelected(false);
				}
				menuAdapter.notifyDataSetChanged();
			}
			break;
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}
	/**
	 * Ket thuc ghe tham
	 */
	public void endVisitCustomer() {
		// ket thuc ghe tham
		requestUpdateActionLog("0", null, null, this);
		gotoTraining();
		GlobalInfo.getInstance().setPositionCustomerVisiting(null);
	}
	/**
	 *
	 * show popup before show camera when click button "dong cua"
	 *
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void showPopupConfirmCustomerClose() {
		GlobalUtil
				.showDialogConfirm(
						this,
						getString(R.string.TEXT_NOTIFY_DISPLAY_CAMERA_CLICK_BUTTON_CUSTOMER_CLOSE_THE_DOOR),
						"OK", CONFIRM_SHOW_CAMERA_WHEN_CLICK_CLOSE_DOOR, "", 0,
						null);
	}
	/**
	 * Luu thong tin dang nhap
	 *
	 * @author : BangHN since : 9:01:36 AM
	 */
	private void saveUserInfo(UserDTO userDTO) {
		GlobalInfo.getInstance().getProfile().setUserData(userDTO);
		// luu lai profile de auto login lan sau
		// SharedPreferences sharedPreferences =
		// getSharedPreferences(LoginView.PREFS_PRIVATE, Context.MODE_PRIVATE);
		SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
		SharedPreferences.Editor prefsPrivateEditor = sharedPreferences.edit();

		if (!StringUtil.isNullOrEmpty(userDTO.shopId)) {
			prefsPrivateEditor.putString(DMS_SHOP_ID, userDTO.shopId);
		}
		prefsPrivateEditor.putString(DMS_SHOP_MANAGED, new Gson().toJson(userDTO.shopManaged));
		prefsPrivateEditor.putString(DMS_SHOP_CODE, userDTO.shopCode);

		prefsPrivateEditor.commit();
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void endVitCustomer() {
		requestUpdateActionLog("0", null, null, this);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			String filePath = "";
			switch (requestCode) {
				case RQ_TAKE_PHOTO: {
					if (this.isTakePhotoFromMenuCloseCustomer) {
						this.isTakePhotoFromMenuCloseCustomer = false;
						this.isSaveActionLogAndCloseCustomerAfterTakePhoto = true;
						if (resultCode == RESULT_OK && takenPhoto != null) {
							filePath = takenPhoto.getAbsolutePath();
							ImageValidatorTakingPhoto validator = new ImageValidatorTakingPhoto(
									this, filePath, Constants.MAX_FULL_IMAGE_HEIGHT);
							validator.setDataIntent(data);
							if (validator.execute()) {
								this.saveCustomerCloseDoorAndGotoCustomerList();
								updateTakenPhoto();
							}
						}
					} else {
						super.onActivityResult(requestCode, resultCode, data);
					}
					break;
				}
				default:
					super.onActivityResult(requestCode, resultCode, data);
					break;

			}
		} catch (Throwable ex) {
			ServerLogger.sendLog("ActivityState", "GlobalBaseActivity : "
							+ VNMTraceUnexceptionLog.getReportFromThrowable(ex),
					TabletActionLogDTO.LOG_EXCEPTION);
		}
	}
	/**
	 *
	 * thuc hien request luu action log - hien thi man hinh danh sach khach hang
	 * va thiet lap ket thuc ghe tham khach hang hien tai
	 *
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	private void saveCustomerCloseDoorAndGotoCustomerList() {
		requestUpdateActionLog("1", null, null, this);
		// ket thuc ghe tham
		GlobalInfo.getInstance().getProfile().setVisitingCustomer(false);
		gotoTraining();
		endVisitCustomerBar();
	}
	/**
	 *
	 * update image to db and to server
	 *
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	private void updateTakenPhoto() {
		this.showProgressDialog(StringUtil.getString(R.string.loading));
		Vector<String> viewData = new Vector<String>();
		if (this.takenPhoto != null) {
			viewData.add(IntentConstants.INTENT_FILE_NAME);
			viewData.add(this.takenPhoto.getName());
			viewData.add(IntentConstants.INTENT_TAKEN_PHOTO);
			viewData.add(this.takenPhoto.getAbsolutePath());
		}
		MediaItemDTO dto = new MediaItemDTO();
		try {
			dto.objectId = Long.parseLong(GlobalInfo.getInstance().getProfile().getActionLogVisitCustomer().aCustomer.getCustomerId());
			dto.objectType = 0;
			dto.mediaType = 0;// loai hinh anh , 1 loai video
			dto.url = this.takenPhoto.getAbsolutePath();
			dto.thumbUrl = this.takenPhoto.getAbsolutePath();
			dto.createDate = DateUtils.now();
			dto.createUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
			dto.lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
			dto.lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
			dto.type = 1;
			dto.status = 1;
			dto.fileSize = this.takenPhoto.length();
			dto.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
			if (!StringUtil.isNullOrEmpty(GlobalInfo.getInstance().getProfile().getUserData().shopId)) {
				dto.shopId = Integer.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId);
			} else {
				dto.shopId = 1;
			}
			viewData.add(IntentConstants.INTENT_OBJECT_ID);
			viewData.add(GlobalInfo.getInstance().getProfile().getActionLogVisitCustomer().aCustomer.getCustomerId());
			viewData.add(IntentConstants.INTENT_OBJECT_TYPE_IMAGE);
			viewData.add(String.valueOf(dto.objectType));
			viewData.add(IntentConstants.INTENT_MEDIA_TYPE);
			viewData.add(String.valueOf(dto.mediaType));
			viewData.add(IntentConstants.INTENT_URL);
			viewData.add(String.valueOf(dto.url));
			viewData.add(IntentConstants.INTENT_THUMB_URL);
			viewData.add(String.valueOf(dto.thumbUrl));
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
			viewData.add(GlobalInfo.getInstance().getProfile().getActionLogVisitCustomer().aCustomer.getCustomerCode());
			viewData.add(IntentConstants.INTENT_STATUS);
			viewData.add("1");
			viewData.add(IntentConstants.INTENT_TYPE);
			viewData.add("1");
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
}
