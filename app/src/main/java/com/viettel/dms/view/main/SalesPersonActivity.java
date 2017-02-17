/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.ActionBar.LayoutParams;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.control.MenuItemDTO;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.MediaItemDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.dto.view.SaleSupportProgramModel;
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
import com.viettel.dms.view.sale.customer.CustomerListView;
import com.viettel.dms.view.sale.depot.ProductListView;
import com.viettel.dms.view.sale.image.ImageListView;
import com.viettel.dms.view.sale.order.FindProductAddOrderListView;
import com.viettel.dms.view.sale.order.OrderView;
import com.viettel.dms.view.sale.order.RemainProductView;
import com.viettel.dms.view.sale.promotion.PromotionProgramView;
import com.viettel.dms.view.sale.salestatistics.SaleStatisticsInDayPreSalesView;
import com.viettel.dms.view.sale.statistic.NVBHGeneralStatisticsView;
import com.viettel.sabeco.R;
import com.viettel.utils.ImageValidatorTakingPhoto;
import com.viettel.utils.VTLog;

/**
 * Activity chinh: Chua cac man hinh cua nhan vien ban hang
 * 
 * @author : BangHN since : 4:41:25 PM version :
 */
public class SalesPersonActivity extends GlobalBaseActivity implements
		OnClickListener, OnItemClickListener {
	public static final int ACTION_AGRRE_BACK = 9;
	// tu choi back
	public static final int ACTION_CANCEL_BACK = 10;
	public static final int CONFIRM_EXIT_APP_OK = 11;
	public static final int CONFIRM_EXIT_APP_CANCEL = 12;
	// confirm show camera
	public static final int CONFIRM_SHOW_CAMERA_WHEN_CLICK_CLOSE_DOOR = 13;
	// dong y thoat man hinh dat hang cancle thoat man hinh dat hang
	public static final int CONFIRM_EXIT_ORDER_VIEW_OK = 14;

	public static final int CONFIRM_EXIT_ORDER_VIEW_CANCEL = 15;
	// dong y ket thuc ghe tham
	private static final int ACTION_FINISH_VISIT_CUS_OK = 16;
	// cancel ket thuc ghe tham
	private static final int ACTION_FINISH_VISIT_CUS_CANCEL = 17;

	boolean isShowMenu = false;
	boolean isTakePhotoFromMenuCloseCustomer = false;
	boolean isSaveActionLogAndCloseCustomerAfterTakePhoto = false;
	ImageView iconLeft;
	TextView tvTitleMenu;
	LinearLayout llMenu;// layout header icon

	ArrayList<MenuItemDTO> listMenu;
	ListView lvMenu;
	MenuAdapter menuAdapter;
	boolean isShowTextInMenu = false;

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

		llShowHideMenu.setOnClickListener(this);
		iconLeft = (ImageView) llShowHideMenu.findViewById(R.id.ivLeftIcon);
		tvTitleMenu = (TextView) findViewById(R.id.tvTitleMenu);
		llMenu = (LinearLayout) findViewById(R.id.llMenu);
		lvMenu = (ListView) findViewById(R.id.lvMenu);
		initMenu();
		showHideMenuText(isShowMenu);

		// TamPQ : xu ly trang thai ghe tham neu thoat dot ngot ( force close)
		checkVisitFromActionLog();
		requestGetAllOrderFailForWarning();
		
		//[Quang] lay danh sach cac chuong trinh can nhap san luong ban
		getAllProgrameNeedTypeQuantity();
		
		// check va gui toan bo log dang con ton dong
		TransactionProcessManager.getInstance().startChecking(
				TransactionProcessManager.SYNC_NORMAL);

		VTLog.i("TamPQ", "activity onCreate");
	}

	private void getAllProgrameNeedTypeQuantity() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.action = ActionEventConstant.ACTION_GET_ALL_PROGRAME_NEED_TYPE_QUANTITY;
		SaleController.getInstance().handleViewEvent(e);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		VTLog.i("TamPQ", "activity onStart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		VTLog.i("TamPQ", "activity onResume");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		VTLog.i("TamPQ", "activity onSaveInstanceState");
	}

	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		super.onPostResume();
		VTLog.i("TamPQ", "activity onPostResume");
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		VTLog.i("TamPQ", "activity onPostCreate");
	}

	/**
	 * Khoi tao menu chuong trinh
	 * 
	 * @author : BangHN since : 4:41:56 PM
	 */
	private void initMenu() {
		listMenu = new ArrayList<MenuItemDTO>();
		MenuItemDTO item1 = new MenuItemDTO(
				StringUtil.getString(R.string.TEXT_MENU_REPORT),
				R.drawable.menu_report_icon);
		MenuItemDTO item2 = new MenuItemDTO(
				StringUtil.getString(R.string.TEXT_MENU_SALES),
				R.drawable.menu_customer_icon);
		MenuItemDTO item4 = new MenuItemDTO(
				StringUtil.getString(R.string.TEXT_PICTURE),
				R.drawable.menu_picture_icon);
		MenuItemDTO item5 = new MenuItemDTO(
				StringUtil.getString(R.string.TEXT_LIST_PRODUCT),
				R.drawable.icon_product_list);
		 MenuItemDTO item6 = new MenuItemDTO(StringUtil.getString(R.string.TEXT_GET_INFORMATION_SALES),
				R.drawable.icon_cost);
		 MenuItemDTO item3 = new MenuItemDTO(
		 		StringUtil.getString(R.string.TEXT_HTTM),
		 		R.drawable.menu_promotion_icon);
		// MenuItemDTO item7 = new
		// MenuItemDTO(StringUtil.getString(R.string.TEXT_DEBT),
		// R.drawable.icon_table_money);
		listMenu.add(item1);
		listMenu.add(item2);
		listMenu.add(item4);
		listMenu.add(item5);
		listMenu.add(item6);
//		listMenu.add(item3);
		// if
		// (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType
		// == UserDTO.TYPE_VALSALES) {
		// listMenu.add(item7);
		// }

		menuAdapter = new MenuAdapter(this, 0, listMenu);
		lvMenu.setAdapter(menuAdapter);
		lvMenu.setOnItemClickListener(this);
		listMenu.get(1).setSelected(true);
		lvMenu.setSelection(1);
		gotoCustomerList();
		menuAdapter.notifyDataSetChanged();
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
		v.add(String.valueOf(GlobalInfo.getInstance().getProfile()
				.getUserData().id));

		e.viewData = v;
		e.action = ActionEventConstant.CHECK_VISIT_FROM_ACTION_LOG;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
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
			case 0:// thong ke
				fg = (BaseFragment) fm
						.findFragmentByTag(NVBHGeneralStatisticsView.TAG);
				if (fg != null && fg.isVisible()) {
					isOk = true;
				}
				break;
			case 1:// ds khach hang
				fg = (BaseFragment) fm.findFragmentByTag(CustomerListView.TAG);
				if (fg != null && fg.isVisible()) {
					isOk = true;
				}
				break;
			case 2:// ho tro thuong mai
				fg = (BaseFragment) fm
						.findFragmentByTag(PromotionProgramView.TAG);
				if (fg != null && fg.isVisible()) {
					isOk = true;
				}
				break;
			case 3:// ds hinh anh
				fg = (BaseFragment) fm.findFragmentByTag(ImageListView.TAG);
				if (fg != null && fg.isVisible()) {
					isOk = true;
				}
				break;
			case 4:// ds san pham
				fg = (BaseFragment) fm.findFragmentByTag(ProductListView.TAG);
				if (fg != null && fg.isVisible()) {
					isOk = true;
				}
				break;
			case 5:// ds don tong ngay
				fg = (BaseFragment) fm
						.findFragmentByTag(SaleStatisticsInDayPreSalesView.TAG);
				if (fg != null && fg.isVisible()) {
					isOk = true;
				}
				break;
			default:
				break;
			}
		}

		return isOk;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (!isCheckConfirmOrderView(arg2)) {
			processClickOnMenu(arg2);
		}
	}

	/**
	 * Kiem tra co phai dang o man hinh dat hang hay khong?
	 * 
	 * @author: TruongHN
	 * @return: boolean
	 * @throws:
	 */
	private boolean isCheckConfirmOrderView(int index) {
		boolean isConfirm = false;
		if (RemainProductView.TAG.equals(GlobalInfo.getInstance().getCurrentTag())
				|| FindProductAddOrderListView.TAG.equals(GlobalInfo.getInstance().getCurrentTag())) {
			// kiem tra co thay doi du lieu ko
			BaseFragment view = (BaseFragment) getFragmentManager()
					.findFragmentByTag(GlobalInfo.getInstance().getCurrentTag());
			if (view != null) {
				// String changeData = orderFragment.checkChangeData();
				// if (!StringUtil.isNullOrEmpty(changeData)) {
				// hien thi thong bao
				GlobalUtil
						.showDialogConfirm(
								this,
								StringUtil
										.getString(R.string.TEXT_CONFIRM_BACK_FIRST_LAYOUT)
										+ " " + view.VIEW_NAME + "?",
								StringUtil.getString(R.string.TEXT_AGREE),
								CONFIRM_EXIT_ORDER_VIEW_OK, StringUtil
										.getString(R.string.TEXT_DENY),
								CONFIRM_EXIT_ORDER_VIEW_CANCEL, index);
				isConfirm = true;
				// }
			}
		} else if (OrderView.TAG.equals(GlobalInfo.getInstance().getCurrentTag())) {
			OrderView view = (OrderView) getFragmentManager()
					.findFragmentByTag(
							GlobalInfo.getInstance().getCurrentTag());
			if (view != null) {
				if (view.isCanEditOrder()) {
					GlobalUtil
							.showDialogConfirm(
									this,
									StringUtil
											.getString(R.string.TEXT_CONFIRM_BACK_FIRST_LAYOUT)
											+ " " + view.VIEW_NAME + "?",
									StringUtil
											.getString(R.string.TEXT_AGREE),
									ACTION_AGRRE_BACK, StringUtil
											.getString(R.string.TEXT_DENY),
									ACTION_CANCEL_BACK, null);
					isConfirm = true;
				}
			}
		}
		return isConfirm;
	}
	
	/**
	 * Thuc thi su kien onClick tren menu
	 * 
	 * @author: TruongHN
	 * @param index
	 * @return: void
	 * @throws:
	 */
	private void processClickOnMenu(int index) {
		if (!isValidateMenu(index)) {
			// neu khong phai la module hien tai
			for (int i = 0; i < listMenu.size(); i++) {
				if (i == index)
					listMenu.get(i).setSelected(true);
				else
					listMenu.get(i).setSelected(false);
			}
			menuAdapter.notifyDataSetChanged();

			// TamPQ: CR0075: neu dang la kh ngoai tuyen thi tu dong update
			// action_log ghe tham khi thoat man hinh dat hang
			ActionLogDTO actionLog = GlobalInfo.getInstance().getProfile()
					.getActionLogVisitCustomer();
			if (actionLog != null && actionLog.isOr == 1) {
				requestUpdateActionLog("0", null, null, this);
			}
			showDetails(index);
		} else {
			// neu la module hien tai thi khong lam j ca
		}
	}

	/**
	 * Show cac manhinh detail
	 * 
	 * @author : BangHN since : 4:42:12 PM
	 */
	void showDetails(int index) {

		GlobalUtil.forceHideKeyboard(this);
		switch (index) {
		case 0:
			gotoGeneralStatistics();
			break;
		case 1: {// ban hang
			gotoCustomerList();
			break;
		}
		case 2:// hinh anh
			gotoImageList();
			break;
		case 3:// danh sach san pham
			gotoProductList();
			break;
		case 4:// don tong
//			gotoSaleStatisticsProductList();
			// thu thap du lieu thi truong
			goToCollectOpponentSale(index+1);
			break;
//		case 5:// cong no
//			gotoCustomerDebtList();
//			break;
		case 5: // chuong trinh ho tro khuyen mai
			gotoPromotion();
			break;
		default:
			break;
		}

	}

	/**
	 * Di toi màn hình cập nhật sản lượng bán đối thủ
	 * 
	 * @author: dungdq3
	 * @param index 
	 * @since: 10:48:04 AM Mar 6, 2014
	 * @return: void
	 * @throws:  
	 */
	private void goToCollectOpponentSale(int index) {
		// TODO Auto-generated method stub
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, String.valueOf(index));
		
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = b; 
		e.action = ActionEventConstant.GOTO_COLLECT_OPPONENT_SALE;
		SaleController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Toi danh sach khach hang
	 * 
	 * @author tampq
	 */
	private void gotoCustomerList() {
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "02");
		
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = b; 
		e.action = ActionEventConstant.GET_CUSTOMER_LIST;
		SaleController.getInstance().handleSwitchFragment(e);

		// ActionEvent e = new ActionEvent();
		// e.sender = this;
		// Bundle bunde = new Bundle();
		// bunde.putString(IntentConstants.INTENT_CUSTOMER_ID, "5");
		// e.viewData = bunde;
		// e.action = ActionEventConstant.GO_TO_CUSTOMER_INFO;
		// UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * di toi man hinh danh sach cong no khach hang
	 * 
	 * @author: BangHN
	 * @return: void
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
//	private void gotoCustomerDebtList() {
//		ActionEvent e = new ActionEvent();
//		e.sender = this;
//		e.viewData = new Bundle();
//		e.action = ActionEventConstant.GOTO_CUSTOMER_DEBT_LIST;
//		SaleController.getInstance().handleSwitchFragment(e);
//	}

	/**
	 * di toi man hinh thong ke
	 * 
	 * @author : BangHN since : 10:59:39 AM
	 */
	private void gotoGeneralStatistics() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle param = new Bundle();
		e.viewData = param;
		e.action = ActionEventConstant.GO_TO_GENERAL_STATISTICS;
		SaleController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * di toi fragment danh sach chuong trinh khuyen mai
	 * 
	 * @author: SoaN
	 * @return: void
	 * @throws:
	 */
	private void gotoPromotion() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = new Bundle();
		e.action = ActionEventConstant.GO_TO_PROMOTION_PROGRAM;
		SaleController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * di toi fragment display image list
	 * 
	 * @author: HoanPD1
	 * @return: void
	 * @throws:
	 */
	private void gotoImageList() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = new Bundle();
		e.action = ActionEventConstant.GET_LIST_IMAGE_CUS;
		SaleController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * 
	 * display product list
	 * 
	 * @author: HaiTC3
	 * @since: 11:46:55 AM | Jun 13, 2012
	 * @return: void
	 * @throws:
	 */
	private void gotoProductList() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = new Bundle();
		e.action = ActionEventConstant.GO_TO_PRODUCT_LIST;
		UserController.getInstance().handleSwitchFragment(e);
	}

//	/**
//	 * 
//	 * display sale statistics product list
//	 * 
//	 * @return: void
//	 * @throws:
//	 * @author: HaiTC3
//	 * @date: Oct 19, 2012
//	 */
//	private void gotoSaleStatisticsProductList() {
//		ActionEvent e = new ActionEvent();
//		e.sender = this;
//		e.viewData = new Bundle();
//		e.action = ActionEventConstant.GO_TO_SALE_STATISTICS_PRODUCT_VIEW_IN_DAY;
//		UserController.getInstance().handleSwitchFragment(e);
//	}

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
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					GlobalUtil.dip2Pixel(200), LayoutParams.FILL_PARENT);
			RelativeLayout.LayoutParams llMenuParam = new RelativeLayout.LayoutParams(
					GlobalUtil.dip2Pixel(200), LayoutParams.WRAP_CONTENT);
			lvMenu.setLayoutParams(layoutParams);
			llMenu.setLayoutParams(llMenuParam);
			iconLeft.setVisibility(View.VISIBLE);
			tvTitleMenu.setVisibility(View.VISIBLE);
		} else {
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					GlobalUtil.dip2Pixel(48), LayoutParams.FILL_PARENT);
			RelativeLayout.LayoutParams llMenuParam = new RelativeLayout.LayoutParams(
					GlobalUtil.dip2Pixel(50), LayoutParams.WRAP_CONTENT);
			lvMenu.setLayoutParams(layoutParams);
			llMenu.setLayoutParams(llMenuParam);
			iconLeft.setVisibility(View.GONE);
			tvTitleMenu.setVisibility(View.GONE);

		}
	}

	/**
	 * TamPQ
	 */
	public void endVisitCustomer() {
		// ket thuc ghe tham
		requestUpdateActionLog("0", null, null, this);
		gotoCustomerList();
		GlobalInfo.getInstance().setPositionCustomerVisiting(null);
	}

	/**
	 * adapter list menu fragment
	 * 
	 * @author : BangHN since : 1:55:59 PM version :
	 */
	class MenuAdapter extends ArrayAdapter<MenuItemDTO> {
		List<MenuItemDTO> modelMenu;
		Context mContext;

		public MenuAdapter(Context context, int textViewResourceId,
				List<MenuItemDTO> listMenu) {
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
				LayoutInflater vi = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = vi.inflate(R.layout.layout_fragment_menu_item, null);
				cell = new MenuItemRow(SalesPersonActivity.this, row);
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
			GlobalUtil.showDialogConfirm(this,
					StringUtil.getString(R.string.TEXT_CONFIRM_EXIT), "Đồng ý",
					CONFIRM_EXIT_APP_OK, "Hủy bỏ", CONFIRM_EXIT_APP_CANCEL,
					null);
		} else if (fm.getBackStackEntryCount() > 1) {
			int handleBack = -1;
			if (RemainProductView.TAG.equals(GlobalInfo.getInstance()
					.getCurrentTag())) {
				BaseFragment view = (BaseFragment) getFragmentManager()
						.findFragmentByTag(
								GlobalInfo.getInstance().getCurrentTag());
				if (view != null) {
					handleBack = 0;
					GlobalUtil
							.showDialogConfirm(
									this,
									StringUtil
											.getString(R.string.TEXT_CONFIRM_BACK_FIRST_LAYOUT)
											+ " " + view.VIEW_NAME + "?",
									StringUtil.getString(R.string.TEXT_AGREE),
									ACTION_AGRRE_BACK, StringUtil
											.getString(R.string.TEXT_DENY),
									ACTION_CANCEL_BACK, null);
					// if (handleBack == OrderView.ACTION_CANCEL_BACK_DEFAULT) {
					// // cancel back
					// }
				}

				// } else if
				// (RemainProductView.TAG.equals(GlobalInfo.getInstance().getCurrentTag()))
				// {
				// // cancel back
				// handleBack = 0;
				// RemainProductView orderView = (RemainProductView)
				// getFragmentManager().findFragmentByTag(RemainProductView.TAG);
				// if (orderView != null) {
				// handleBack = orderView.onBackPressed();
				// if (handleBack == OrderView.ACTION_CANCEL_BACK_DEFAULT) {
				// // cancel back
				// }
				// }
				// }
				// else if
				// (NoteListView.TAG.equals(GlobalInfo.getInstance().getCurrentTag()))
				// {
				// NoteListView noteView = (NoteListView)
				// getFragmentManager().findFragmentByTag(NoteListView.TAG);
				// if (noteView != null) {
				// handleBack = noteView.onBackPressed();
				// }
			} else if (OrderView.TAG.equals(GlobalInfo.getInstance()
					.getCurrentTag())) {
				OrderView view = (OrderView) getFragmentManager()
						.findFragmentByTag(
								GlobalInfo.getInstance().getCurrentTag());
				if (view != null) {
					if (view.isCanEditOrder()) {
						handleBack = 0;
						GlobalUtil
								.showDialogConfirm(
										this,
										StringUtil
												.getString(R.string.TEXT_CONFIRM_BACK_FIRST_LAYOUT)
												+ " " + view.VIEW_NAME + "?",
										StringUtil
												.getString(R.string.TEXT_AGREE),
										ACTION_AGRRE_BACK, StringUtil
												.getString(R.string.TEXT_DENY),
										ACTION_CANCEL_BACK, null);
					}
				}
			}
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
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.CHECK_VISIT_FROM_ACTION_LOG:
			ActionLogDTO action = (ActionLogDTO) modelEvent.getModelData();
			if (action != null) {
				GlobalInfo.getInstance().getProfile().setVisitingCustomer(true);
				GlobalInfo.getInstance().getProfile()
						.setActionLogVisitCustomer(action);
				initMenuVisitCustomerForNVBH(action.aCustomer.shortCode,
						action.aCustomer.customerName, action.objectType,
						action.isOr, action.haveAction);
			}
			break;
		case ActionEventConstant.UPDATE_ACTION_LOG:
			GlobalInfo.getInstance().getProfile().setVisitingCustomer(false);
			GlobalInfo.getInstance().getProfile()
					.setActionLogVisitCustomer((ActionLogDTO) e.viewData);
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
		case ActionEventConstant.ACTION_GET_ALL_PROGRAME_NEED_TYPE_QUANTITY: {
			listProgrameTypeQuantity = (SaleSupportProgramModel) modelEvent.getModelData();
			if(listProgrameTypeQuantity != null && listProgrameTypeQuantity.listPrograme.size() > 0){
				showWarningInputQuantity(true);
			}else{
				showWarningInputQuantity(false); 
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
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
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
		case CONFIRM_EXIT_APP_OK:
			// ket thuc ghe tham
			// requestUpdateActionLog("0", null, null, this);
//			sendBroadcast(ActionEventConstant.ACTION_FINISH_APP, new Bundle());
			TransactionProcessManager.getInstance().cancelTimer();
			//xu ly thoat ung dung
			processExitApp();
//			finish();
			break;
		case CONFIRM_EXIT_APP_CANCEL:
			break;
		case CONFIRM_SHOW_CAMERA_WHEN_CLICK_CLOSE_DOOR:
			// show camera
			this.isTakePhotoFromMenuCloseCustomer = true;
			this.takenPhoto = GlobalUtil.takePhoto(this,
					GlobalBaseActivity.RQ_TAKE_PHOTO);
			break;
		case CONFIRM_EXIT_ORDER_VIEW_OK:
			int index = Integer.valueOf(data.toString());
			processClickOnMenu(index);
			break;
		case CONFIRM_EXIT_ORDER_VIEW_CANCEL:
			break;
		case ACTION_FINISH_VISIT_CUS_OK: {
			endVisitCustomer();
			break;
		}
		case ACTION_FINISH_VISIT_CUS_CANCEL: {
			break;
		}
		case ACTION_AGRRE_BACK: {
			// dong y back tro lai
			RemainProductView remainFragment = (RemainProductView) this
					.getFragmentManager().findFragmentByTag(
							RemainProductView.TAG);
			if (remainFragment != null) {
				remainFragment.receiveBroadcast(
						ActionEventConstant.BROADCAST_ORDER_VIEW, null);
			}
			GlobalUtil.popBackStack(this);

			break;
		}
		case ACTION_CANCEL_BACK: {
			// khong dong y back
			break;
		}
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	@Override
	public void receiveBroadcast(int action, Bundle bundle) {
		switch (action) {
		case ActionEventConstant.NOTIFY_MENU:
			int index = bundle.getInt(IntentConstants.INTENT_INDEX);
			if (index >= 0) {
				for (int i = 0; i < listMenu.size(); i++) {
					listMenu.get(i).setSelected(false);
				}
				listMenu.get(index).setSelected(true);
				lvMenu.setSelection(index);
				menuAdapter.notifyDataSetChanged();
				showDetails(index);
			}
			break;
		default:
			super.receiveBroadcast(action, bundle);
			break;
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
		gotoCustomerList();
		endVisitCustomerBar();
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
							// imageFile = takingFile;
							// GlobalInfo.getInstance().setBitmapData(validator.getBitmap());
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
	 * update image to db and to server
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	private void updateTakenPhoto() {
		this.showProgressDialog(StringUtil.getString(R.string.loading));
		Vector<String> viewData = new Vector<String>();

//		viewData.add(IntentConstants.INTENT_STAFF_ID);
//		viewData.add(Integer.toString(GlobalInfo.getInstance().getProfile()
//				.getUserData().id));
//
//		viewData.add(IntentConstants.INTENT_SHOP_ID);
//		if (!StringUtil.isNullOrEmpty(GlobalInfo.getInstance().getProfile()
//				.getUserData().shopId)) {
//			viewData.add(GlobalInfo.getInstance().getProfile().getUserData().shopId);
//		} else {
//			viewData.add("1");
//		}

		if (this.takenPhoto != null) {
			viewData.add(IntentConstants.INTENT_FILE_NAME);
			viewData.add(this.takenPhoto.getName());

			viewData.add(IntentConstants.INTENT_TAKEN_PHOTO);
			viewData.add(this.takenPhoto.getAbsolutePath());
		}

		MediaItemDTO dto = new MediaItemDTO();

		try {
			dto.objectId = Long.parseLong(GlobalInfo.getInstance().getProfile()
					.getActionLogVisitCustomer().aCustomer.getCustomerId());
			dto.objectType = 0;
			dto.mediaType = 0;// loai hinh anh , 1 loai video
			dto.url = this.takenPhoto.getAbsolutePath();
			dto.thumbUrl = this.takenPhoto.getAbsolutePath();
			dto.createDate = DateUtils.now();
			dto.createUser = GlobalInfo.getInstance().getProfile()
					.getUserData().userCode;
			dto.lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
					.getLatitude();
			dto.lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
					.getLongtitude();
			dto.type = 1;
			dto.status = 1;
			dto.fileSize = this.takenPhoto.length();
			dto.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
			if (!StringUtil.isNullOrEmpty(GlobalInfo.getInstance().getProfile()
					.getUserData().shopId)) {
				dto.shopId = Integer.valueOf(GlobalInfo.getInstance()
						.getProfile().getUserData().shopId);
			} else {
				dto.shopId = 1;
			}

			viewData.add(IntentConstants.INTENT_OBJECT_ID);
			viewData.add(GlobalInfo.getInstance().getProfile()
					.getActionLogVisitCustomer().aCustomer.getCustomerId());
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
			viewData.add(GlobalInfo.getInstance().getProfile()
					.getActionLogVisitCustomer().aCustomer.getCustomerCode());
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
}
