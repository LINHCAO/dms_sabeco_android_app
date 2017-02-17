/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.order;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.view.ListRemainProductDTO;
import com.viettel.dms.dto.view.RemainProductViewDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.locating.PositionManager;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.SalesPersonActivity;
import com.viettel.dms.view.sale.customer.CustomerListView;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * Kiem ton tai KH 
 * RemainProductView.java
 * @author: dungnt19
 * @version: 1.0 
 * @since:  19:51:20 12-12-2013
 */
public class RemainProductView extends BaseFragment implements OnClickListener,
		OnEventControlListener, VinamilkTableListener {
	public static final String TAG = RemainProductView.class.getName();
	private static final int ACTION_SAVE_REMAIN_SUCCESS = 1;
	private static final int ACTION_SAVE_REMAIN_FAIL = 2;

	private static final int ACTION_FROM_ORDER_VIEW = 1;
//	// dong y back
//	public static final int ACTION_AGRRE_BACK = 7;
//	// tu choi back
//	public static final int ACTION_CANCEL_BACK = 8;

	ListRemainProductDTO listMngDTO = new ListRemainProductDTO();
	DMSTableView tbOrder;
	RemainProductView instance;
	String customerId;
	String customerCode;
	String customerName;
	String customerAddress;
	int cusTypeId;
	String is_or;
	Button btSave;

	private GlobalBaseActivity parent;
	// danh sach nhung mat hang duoc danh dau check se dua qua man hinh dat hang
	ArrayList<RemainProductViewDTO> listSelectedProduct = new ArrayList<RemainProductViewDTO>();
	// danh sach nhung mat hang can cap nhat len server vao bang
	// customer_stock_history
	ArrayList<RemainProductViewDTO> listUpdatedProduct = new ArrayList<RemainProductViewDTO>();
	private int actionFromView = 0;
	private String startTime;
	private double lat;
	private double lng;
	public int[] REMAIN_PRODUCT_TABLE_WIDTHS = { 50, 110, 445, 110, 160, 60 };

	public static RemainProductView newInstance(Bundle bundle) {
		RemainProductView instance = new RemainProductView();
		instance.setArguments(bundle);
		instance.VIEW_NAME = StringUtil.getString(R.string.TEXT_CHECK_REMAIN_PRODUCT);

		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(
				R.layout.layout_remain_product, null);
		View view = super.onCreateView(inflater, v, savedInstanceState);
		customerId = getArguments().getString(
				IntentConstants.INTENT_CUSTOMER_ID);
		customerCode = getArguments().getString(
				IntentConstants.INTENT_CUSTOMER_CODE);
		customerName = getArguments().getString(
				IntentConstants.INTENT_CUSTOMER_NAME);
		customerAddress = getArguments().getString(
				IntentConstants.INTENT_CUSTOMER_ADDRESS);
		cusTypeId = getArguments().getInt(
				IntentConstants.INTENT_CUSTOMER_TYPE_ID);
		is_or = getArguments().getString(IntentConstants.INTENT_IS_OR)
				.toString();
		initView(v);

		setTitleHeaderView(StringUtil
				.getString(R.string.TEXT_HEADER_TITLE_REMAIN_PRODUCT_VIEW));
		this.startTime = DateUtils.now();

		if (actionFromView == 0) {
			getListOrder(customerId, false);
		}

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (actionFromView == 0) {
		} else {
			renderLayout();
		}
	}

	private void initView(View v) {
		btSave = (Button) v.findViewById(R.id.btSave);
		btSave.setOnClickListener(this);

		tbOrder = (DMSTableView) v.findViewById(R.id.tbOrder);
		tbOrder.setListener(this);
		initHeaderTable(tbOrder, new RemainProductRow(parent, this));

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			parent = (SalesPersonActivity) activity;
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

	}

	/**
	 * lay danh sach hang ma khach hang do' da dat cua nhan vien do' cua shop
	 * do' trong vong 2 thang truoc (hang do co trong CTKM hay kg, co trong
	 * CTTTam hay kg, co so luong ton trong kho cua nha phan phoi <= 0 hay kg,
	 * sort theo mat hang trong tam, mat hang khuyen mai, ma mat hang
	 * 
	 */

	private void getListOrder(String customerId, boolean isGotCount) {
		if(!parent.isShowProgressDialog()){
			parent.showProgressDialog(StringUtil.getString(R.string.loading));
		}

		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_CUSTOMER_ID, /* "131105" */
		customerId);
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance()
				.getProfile().getUserData().shopId);
		data.putString(
				IntentConstants.INTENT_STAFF_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile()
						.getUserData().id));
		data.putBoolean(IntentConstants.INTENT_IS_GOT_COUNT, isGotCount);
		data.putInt(IntentConstants.INTENT_CUSTOMER_TYPE_ID, cusTypeId);

		data.putString(IntentConstants.INTENT_PAGE, "");

		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_REMAIN_PRODUCT;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * Layout du lieu
	 * @author: dungnt19
	 * @since: 19:52:40 12-12-2013
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		GlobalUtil.setEnableButton(btSave, true);
		int indexPage = tbOrder.getPagingControl().getCurrentPage() - 1;
		int pos = 1 + Constants.NUM_ITEM_PER_PAGE
				* (tbOrder.getPagingControl().getCurrentPage() - 1);
		tbOrder.clearAllData();
		for (int i = indexPage * Constants.NUM_ITEM_PER_PAGE; i < (indexPage + 1)
				* Constants.NUM_ITEM_PER_PAGE
				&& i < listMngDTO.listDTO.size(); i++) {
			RemainProductViewDTO dto = listMngDTO.listDTO.get(i);
			RemainProductRow row = new RemainProductRow(parent, null);

			row.setClickable(true);
			row.setTag(Integer.valueOf(pos));
			if (listSelectedProduct.contains(dto)) {
				row.renderLayout(pos, listSelectedProduct
						.get(listSelectedProduct.indexOf(dto)));
			} else {
				row.renderLayout(pos, dto);
			}
			// khi quay tro lai thi khong duoc edit so luong ton
			if (actionFromView == ACTION_FROM_ORDER_VIEW)
				row.edRemainQuanlity.setEnabled(false);

			row.setOnClickListener(this);
			pos++;
			tbOrder.addRow(row);

			if (tbOrder.getListChildRow().size() == Constants.NUM_ITEM_PER_PAGE)
				break;

		}

		if (tbOrder.getPagingControl().totalPage < 0)
			tbOrder.setTotalSize(listMngDTO.total, 1);

	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent actionEvent = modelEvent.getActionEvent();
		switch (actionEvent.action) {
		case ActionEventConstant.GET_REMAIN_PRODUCT:
			listMngDTO = new ListRemainProductDTO();
			
			ListRemainProductDTO listResult = (ListRemainProductDTO) modelEvent
					.getModelData();
			int pos = 1 + Constants.NUM_ITEM_PER_PAGE
					* (tbOrder.getPagingControl().getCurrentPage() - 1);

			for (int i = 0; i < listResult.listDTO.size(); i++) {
				listResult.listDTO.get(i).stt = pos;
				pos++;
				listMngDTO.listDTO.add(listResult.listDTO.get(i));
			}
			listMngDTO.total = listResult.total;
			// sortListByProperty(listMngDTO.listDTO);
			if (listMngDTO.listDTO.size() > 0) {
				renderLayout();
				parent.closeProgressDialog();
				// TamPQ: them luong dinh vi
				processWaitingPosition();
			} else {
				gotoCreateOrder(listSelectedProduct, false);
				parent.closeProgressDialog();
			}
			requestInsertLogKPI(HashMapKPI.NVBH_KIEMHANGTON, actionEvent.startTimeFromBoot);
			break;

		case ActionEventConstant.SAVE_NUMBER_REMAIN_PRODUCT:
			insertActionLog();
			parent.closeProgressDialog();
			parent.removeMenuCloseCustomer();
			gotoCreateOrder(listSelectedProduct, false);
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		ActionEvent actionEvent = modelEvent.getActionEvent();
		parent.closeProgressDialog();
		switch (actionEvent.action) {
		case ActionEventConstant.SAVE_NUMBER_REMAIN_PRODUCT:
			GlobalUtil.setEnableButton(btSave, true);
			parent.removeMenuCloseCustomer();
		case ActionEventConstant.ACTION_GET_CURRENT_DATE_TIME_SERVER:

			GlobalUtil.setEnableButton(btSave, true);
			super.handleErrorModelViewEvent(modelEvent);
			break;
		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}

	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub
		if (control == tbOrder) {
			savePageDTO(tbOrder.getPagingControl().getOldPage());
			renderLayout();
		}
	}

	/**
	 * Dinh vi vi tri
	 * @author: dungnt19
	 * @since: 19:53:11 12-12-2013
	 * @return: void
	 * @throws:
	 */
	public void processWaitingPosition() {
		lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
				.getLatitude();
		lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
				.getLongtitude();
	}

	/**
	 * Insert action log sau khi kiem ton
	 * @author: dungnt19
	 * @since: 19:53:23 12-12-2013
	 * @return: void
	 * @throws:
	 */
	private void insertActionLog() {
		try {
			ActionLogDTO action = new ActionLogDTO();
			action.staffId = GlobalInfo.getInstance().getProfile()
					.getUserData().id;
			action.lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
					.getLatitude();
			action.lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
					.getLongtitude();
			action.aCustomer.customerId = Long.parseLong(customerId);
			action.objectId = customerId;
			action.objectType = "3";
			action.startTime = this.startTime;
			action.endTime = DateUtils.now();
			action.isOr = Integer.parseInt(is_or);
			parent.requestInsertActionLog(action);
		} catch (NumberFormatException e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
	}

	@Override
	public void onClick(View v) {
		// String dateTimePattern =
		// "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";
		// Pattern pattern = Pattern.compile(dateTimePattern);
		if (v == btSave) {
			// goToScreen();
			parent.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (tbOrder.getListChildRow().size() > 0) {
						GlobalUtil.forceHideKeyboard(parent);
					}
				}
			});

			if (actionFromView == 0) {
				GlobalUtil
						.showDialogConfirm(
								this,
								parent,
								StringUtil.getString(R.string.TEXT_CONFIRM_REMAIN_PRODUCT_VIEW),
								StringUtil.getString(R.string.TEXT_AGREE),
								ACTION_SAVE_REMAIN_SUCCESS, StringUtil
										.getString(R.string.TEXT_BUTTON_CLOSE),
								ACTION_SAVE_REMAIN_FAIL, null);
			} else {
				savePageDTO(tbOrder.getPagingControl().getCurrentPage());
				// chuyen qua man hinh dat hang
				gotoCreateOrder(listSelectedProduct, false);
			}
		} else {
			super.onClick(v);
		}

	}

	/**
	 * Di toi MH Dat hang
	 * @author: dungnt19
	 * @since: 19:54:12 12-12-2013
	 * @return: void
	 * @throws:  
	 * @param listCheck
	 * @param hasData
	 */
	private void gotoCreateOrder(ArrayList<RemainProductViewDTO> listCheck,
			boolean hasData) {
		if (!hasData) {
			CustomerListView frag = (CustomerListView) parent
					.getFragmentManager().findFragmentByTag(
							CustomerListView.TAG);
			if (frag != null) {
				frag.isBackFromPopStack = true;
			}
			GlobalUtil.popBackStack(this.getActivity());
		}
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);
		bundle.putString(IntentConstants.INTENT_CUSTOMER_NAME, customerName);
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ADDRESS, customerAddress);
		bundle.putString(IntentConstants.INTENT_CUSTOMER_CODE, customerCode);
		bundle.putString(IntentConstants.INTENT_ORDER_ID, "0");
		bundle.putString(IntentConstants.INTENT_IS_OR, is_or);
		bundle.putInt(IntentConstants.INTENT_CUSTOMER_TYPE_ID, cusTypeId);
		bundle.putSerializable(IntentConstants.INTENT_SUGGEST_ORDER_LIST,
				listSelectedProduct);
		bundle.putBoolean(IntentConstants.INTENT_HAS_REMAIN_PRODUCT, hasData);
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_ORDER_VIEW;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Luu kiem ton 
	 * @author: dungnt19
	 * @since: 19:54:25 12-12-2013
	 * @return: void
	 * @throws:
	 */
	private void saveRemainProduct() {
		actionFromView = ACTION_FROM_ORDER_VIEW;
		savePageDTO(tbOrder.getPagingControl().getCurrentPage());

		// cap nhat du lieu xuong db va server
		if (listUpdatedProduct.size() > 0) {
			parent.showProgressDialog(StringUtil.getString(R.string.loading), false);
			ActionEvent e = new ActionEvent();
			e.viewData = listUpdatedProduct;
			e.sender = this;
			e.action = ActionEventConstant.SAVE_NUMBER_REMAIN_PRODUCT;
			e.isNeedCheckTimeServer = true;
			SaleController.getInstance().handleViewEvent(e);
		} else {
			gotoCreateOrder(listSelectedProduct, false);
		}

	}

	/**
	 * Luu du lieu de khi chuyen qua cac trang van giu gia tri da nhap
	 * @author: dungnt19
	 * @since: 19:55:58 12-12-2013
	 * @return: void
	 * @throws:  
	 * @param page
	 */
	private void savePageDTO(int page) {
		int position = Constants.NUM_ITEM_PER_PAGE * (page - 1);
		for (int i = 0; i < tbOrder.getListChildRow().size(); i++) {
			RemainProductRow row = (RemainProductRow) tbOrder.getListChildRow().get(i);
			RemainProductViewDTO sale = listMngDTO.listDTO.get(position + i);

			sale.CUSTOMER_ID = Long.parseLong(customerId);
			sale.STAFF_ID = GlobalInfo.getInstance().getProfile().getUserData().id;

			if (!StringUtil.isNullOrEmpty(row.edRemainQuanlity.getText()
					.toString())) {
				sale.quantity = GlobalUtil.calRealOrder(row.edRemainQuanlity.getText().toString(), sale.convfact);
				sale.remainNumber = row.edRemainQuanlity.getText().toString();
				if (!listUpdatedProduct.contains(sale))
					listUpdatedProduct.add(sale);
			}
			if (row.cbCheck.isChecked()) {
				sale.isCheck = true;
				if (!listSelectedProduct.contains(sale))
					listSelectedProduct.add(sale);
			}
		}
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		if (control instanceof RemainProductRow) {
			if (eventType == RemainProductRow.ACTION_CLICK_SHOW_PROMOTION_DETAIL) {
				RemainProductViewDTO dto = (RemainProductViewDTO) data;
				requestGetPromotionDetail(dto.promotionProgramCode);
			}

		} else {

			switch (eventType) {
			case ACTION_SAVE_REMAIN_SUCCESS:
				saveRemainProduct();
				break;
			case ACTION_SAVE_REMAIN_FAIL:
				GlobalUtil.setEnableButton(btSave, true);
				break;
//			case ACTION_AGRRE_BACK: 
//				GlobalUtil.popBackStack(getActivity());
//				break;
			default:
				break;
			}
			super.onEvent(eventType, control, data);

		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
	}

	@Override
	public void receiveBroadcast(int action, Bundle bundle) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				listSelectedProduct = new ArrayList<RemainProductViewDTO>();
				listUpdatedProduct = new ArrayList<RemainProductViewDTO>();
				getListOrder(customerId, true);
			}
			break;
		case ActionEventConstant.BROADCAST_ORDER_VIEW:
			actionFromView = ACTION_FROM_ORDER_VIEW;
			break;
		default:
			super.receiveBroadcast(action, bundle);
			break;
		}

	}
}
