/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.FragmentMenuContanst;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.LogDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.ListOrderMngDTO;
import com.viettel.dms.dto.view.ListStaffDTO;
import com.viettel.dms.dto.view.OrderViewDTO;
import com.viettel.dms.dto.view.SaleOrderViewDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.SalesPersonActivity;
import com.viettel.dms.view.main.SupervisorActivity;
import com.viettel.dms.view.main.TBHVActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * Man hinh danh sach don hang
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class ListOrderView extends BaseFragment implements OnClickListener, OnItemSelectedListener,
		OnEventControlListener, VinamilkTableListener, OnTouchListener {
	public static final String TAG = ListOrderView.class.getName();
	public static final int MAP_ACTION = 0;
	public static final int NOTE_ACTION = 1;
	public static final int DETAIL_ACTION = 2;
	public static final int LIST_ACTION = 3;
	public static final int REFRESH_ACTION = 4;

	// tat ca
	public static final int STATUS_ALL = -1;
	// thanh cong
	public static final int STATUS_SUCCESS = 0;
	// dang cho xu ly
	public static final int STATUS_WAITING_PROCESS = 1;
	// qua ngay chua duyet
	public static final int STATUS_OVERDATE = 2;
	// tu choi
	public static final int STATUS_DENY = 3;
	//Cho gui
	public static final int STATUS_NOT_SEND = 4;
	private static final int DATE_FROM_CONTROL = 1;
	// Xoa don hang thanh cong
	private static final int ACTION_DELETE_ORDER_SUCCESS = 1;
	// xoa don hang khong thanh cong
	private static final int ACTION_DELETE_ORDER_FAIL = 2;

	ListOrderMngDTO listMngDTO = new ListOrderMngDTO();
	DMSTableView tbOrder;
	Button btSearch;

	VNMEditTextClearable edMKH;
	VNMEditTextClearable edTKH;
	VNMEditTextClearable edTN;
	Spinner spinnerState;
	Spinner spinnerNVBH;
	Spinner spinnerGSNPP;
	TextView tvKH;
	TextView tvNVBH;
	TextView tvGSNPP;
	// nhung gia tri can search
	String mkh;
	String tkh;
	String createDate;
	TextView tvNotChecked;
	ArrayList<SaleOrderViewDTO> listSelectedOrder = new ArrayList<SaleOrderViewDTO>();
	private String[] arrStateChoose = new String[] { StringUtil.getString(R.string.ALL),
			StringUtil.getString(R.string.TEXT_ORDER_NOT_SEND), StringUtil.getString(R.string.TEXT_WAITING_PROCESS),
			StringUtil.getString(R.string.TEXT_SUCCESS), StringUtil.getString(R.string.TEXT_DENY),
			StringUtil.getString(R.string.TEXT_OVERDATE) };
	private int selectedState = -1;
	private GlobalBaseActivity parent;

	private int mDay;
	private int mMonth;
	private int mYear;
	private int currentCalender;
	private boolean isFirstLoad = true;
	private boolean getListLog = false;

	// danh sach nhan vien ban hang
	private ListStaffDTO dtoListStaff;
	// danh sach gsnpp
	private ListStaffDTO dtoListGSNPP;
	// list ds nhan vien o spiner
	private String[] arrNVBHChoose;
	// list ds gsnpp o spiner
	private String[] arrGSNPPChoose;
	int currentNVBH = -1;// KH dang chon
	int currentGSNPP = -1;// GSNPP dang chon
	private String textStaffId = "";
	private String textGSNPPId = "";

	public static ListOrderView newInstance(int index) {
		ListOrderView f = new ListOrderView();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", index);
		f.setArguments(args);

		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.layout_list_oder, null);
		View view = super.onCreateView(inflater, v, savedInstanceState);
		// menu bar
		if(parent instanceof SalesPersonActivity) {
			enableMenuBar(this, FragmentMenuContanst.NVBH_CUSTOMER_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_NVBH_LIST_ORDER);
		} else if(parent instanceof SupervisorActivity) {
			enableMenuBar(this, FragmentMenuContanst.GSNPP_STAFF_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_GSNPP_INVOICE_LIST);
		} else if(parent instanceof TBHVActivity) {
			enableMenuBar(this, FragmentMenuContanst.GSBH_STAFF_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_GSBH_INVOICE_LIST);
		}
		initView(v);
		if(parent instanceof SalesPersonActivity) {
			setTitleHeaderView(StringUtil.getString(R.string.TITLE_MODULE_LIST_ORDER));
		} else if(parent instanceof SupervisorActivity || parent instanceof TBHVActivity) {
			setTitleHeaderView(StringUtil.getString(R.string.TITLE_MODULE_GSNPP_LIST_ORDER));
		}
		if (isFirstLoad) {
			getListLog = true;
			if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES) {
				createDate = DateUtils.getCurrentDate();
				this.edTN.setText(createDate);
				getListOrder();
			} else if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSNPP) {
				getListSaleStaff(GlobalInfo.getInstance().getProfile().getUserData().id);
			} else if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSTINH) {
				getListGSNPPByGSBH(GlobalInfo.getInstance().getProfile().getUserData().id);
			}
		}
		return view;
	}

	/**
	 * request lay danh sach nhan vien ban hang trong shop
	 *
	 * @author banghn
	 */
	private void getListGSNPPByGSBH(long staffId) {
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_STAFF_OWNER_ID,
				String.valueOf(staffId));
		b.putBoolean(IntentConstants.INTENT_IS_ALL, true);
		e.viewData = b;
		e.action = ActionEventConstant.GET_LIST_GSNPP_BY_GSBH;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	/**
	 * request lay danh sach nhan vien ban hang trong shop
	 *
	 * @author banghn
	 */
	private void getListSaleStaff(long staffId) {
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_SHOP_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId));
		b.putString(IntentConstants.INTENT_STAFF_OWNER_ID,
				String.valueOf(staffId));
		b.putBoolean(IntentConstants.INTENT_ORDER, true);
		b.putBoolean(IntentConstants.INTENT_IS_ALL, true);
		e.viewData = b;
		e.action = ActionEventConstant.GET_LIST_NVBH;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	/**
	 * request lay danh sach nhan vien ban hang trong shop cho GSBH
	 *
	 * @author banghn
	 */
	private void getListSaleStaff() {
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_STAFF_OWNER_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
		b.putString(IntentConstants.INTENT_STAFF_ID, textGSNPPId);
		b.putBoolean(IntentConstants.INTENT_IS_ALL, true);
		e.viewData = b;
		e.action = ActionEventConstant.GET_LIST_NVBH_BY_GSBH;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	/**
	 * khoi tao view list GSNPP
	 *
	 * @author banghn
	 */
	private void initSpGSNPP() {
		arrGSNPPChoose = new String[dtoListGSNPP.arrList.size()];
		int i=0;
		for (ListStaffDTO.StaffItem staffItem: dtoListGSNPP.arrList) {
			if (!StringUtil.isNullOrEmpty(staffItem.code)) {
				arrGSNPPChoose[i] = staffItem.code + "-" + staffItem.name;
			} else {
				arrGSNPPChoose[i] = staffItem.name;
			}
			i++;
		}
		SpinnerAdapter adapterGSNPP = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrGSNPPChoose);
		spinnerGSNPP.setAdapter(adapterGSNPP);
		currentGSNPP = spinnerGSNPP.getSelectedItemPosition();
		if(currentGSNPP > 0 && currentGSNPP < dtoListGSNPP.arrList.size()) {
			textGSNPPId = dtoListGSNPP.arrList.get(spinnerGSNPP.getSelectedItemPosition()).id;
		} else {
			textGSNPPId = "";
		}
	}

	/**
	 * khoi tao view list NVBH
	 *
	 * @author banghn
	 */
	private void initSpNVBH() {
		arrNVBHChoose = new String[dtoListStaff.arrList.size()];
		int i=0;
		for (ListStaffDTO.StaffItem staffItem: dtoListStaff.arrList) {
			if (!StringUtil.isNullOrEmpty(staffItem.code)) {
				arrNVBHChoose[i] = staffItem.code + "-" + staffItem.name;
			} else {
				arrNVBHChoose[i] = staffItem.name;
			}
			i++;
		}
		SpinnerAdapter adapterNVBH = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrNVBHChoose);
		spinnerNVBH.setAdapter(adapterNVBH);
		currentNVBH = spinnerNVBH.getSelectedItemPosition();
		if(currentNVBH > 0 && currentNVBH < dtoListStaff.arrList.size()) {
			textStaffId = dtoListStaff.arrList.get(spinnerNVBH.getSelectedItemPosition()).id;
		} else {
			textStaffId = "";
		}
	}


	@Override
	public void onResume() {
		
		super.onResume();
		if (isFirstLoad) {
		} else {
			// renderLayout();
			if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSNPP) {
				initSpNVBH();
			} else if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES) {
				getListOrder();
			}
			else if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSTINH) {
				initSpGSNPP();
				initSpNVBH();
			}
		}
	}

	private void initView(View v) {
		this.edMKH = (VNMEditTextClearable) v.findViewById(R.id.edMKH);
		GlobalUtil.setEditTextMaxLength(edMKH, Constants.MAX_LENGTH_CUSTOMER_CODE);
		this.edTKH = (VNMEditTextClearable) v.findViewById(R.id.edTKH);
		GlobalUtil.setEditTextMaxLength(edTKH, Constants.MAX_LENGTH_CUSTOMER_NAME);
		this.edTN = (VNMEditTextClearable) v.findViewById(R.id.edTN);
		this.edTN.setOnTouchListener(this);
		this.edTN.setIsHandleDefault(false);
		this.spinnerState = (Spinner) v.findViewById(R.id.spinnerState);

		this.spinnerGSNPP = (Spinner) v.findViewById(R.id.spGSNPP);
		this.spinnerNVBH = (Spinner) v.findViewById(R.id.spNVBH);
		this.tvKH = (TextView) v.findViewById(R.id.tvKH);
		this.tvNVBH = (TextView) v.findViewById(R.id.tvNVBH);
		this.tvGSNPP = (TextView) v.findViewById(R.id.tvGSNPP);

		SpinnerAdapter adapterState = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrStateChoose);
		this.spinnerState.setAdapter(adapterState);

		if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES) {
			tvKH.setVisibility(View.VISIBLE);
			edMKH.setVisibility(View.VISIBLE);
			edTKH.setVisibility(View.VISIBLE);

			tvNVBH.setVisibility(View.GONE);
			spinnerNVBH.setVisibility(View.GONE);
			tvGSNPP.setVisibility(View.GONE);
			spinnerGSNPP.setVisibility(View.GONE);
		} else if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSNPP) {
			tvKH.setVisibility(View.GONE);
			edMKH.setVisibility(View.GONE);
			edTKH.setVisibility(View.GONE);

			tvNVBH.setVisibility(View.VISIBLE);
			spinnerNVBH.setVisibility(View.VISIBLE);
			tvGSNPP.setVisibility(View.GONE);
			spinnerGSNPP.setVisibility(View.GONE);
			spinnerNVBH.setOnItemSelectedListener(this);

			spinnerState.setSelection(2);
			this.selectedState = STATUS_WAITING_PROCESS;
		} else if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSTINH) {
			tvKH.setVisibility(View.GONE);
			edMKH.setVisibility(View.GONE);
			edTKH.setVisibility(View.GONE);

			tvNVBH.setVisibility(View.VISIBLE);
			spinnerNVBH.setVisibility(View.VISIBLE);
			tvGSNPP.setVisibility(View.VISIBLE);
			spinnerGSNPP.setVisibility(View.VISIBLE);
			spinnerNVBH.setOnItemSelectedListener(this);
			spinnerGSNPP.setOnItemSelectedListener(this);

			spinnerState.setSelection(2);
			this.selectedState = STATUS_WAITING_PROCESS;
		}
		this.spinnerState.setOnItemSelectedListener(this);

		tvNotChecked = (TextView) v.findViewById(R.id.tvNotChecked);
		tbOrder = (DMSTableView) v.findViewById(R.id.tbOrder);
		tbOrder.setListener(this);
		btSearch = (Button) v.findViewById(R.id.btSearch);
		btSearch.setOnClickListener(this);
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

	private void renderLayout() {
		if (listMngDTO != null) {
			tbOrder.clearAllDataAndHeader();
			initHeaderTable(tbOrder, new OrderMngRow(parent, this));
			int pos = 1 + Constants.NUM_ITEM_PER_PAGE * (tbOrder.getPagingControl().getCurrentPage() - 1);
			if (listMngDTO.getListDTO().size() == 0) {
				tbOrder.showNoContentRow();
			}else{
//				tbOrder.hideNoContentRow();
				for (final SaleOrderViewDTO dto : listMngDTO.getListDTO()) {
					OrderMngRow row = new OrderMngRow(parent, this);
					row.setClickable(true);
					row.setTag(Integer.valueOf(pos));
					if (listSelectedOrder.contains(dto)) {
						row.renderLayout(pos, listSelectedOrder.get(listSelectedOrder.indexOf(dto)));
					} else {
						row.renderLayout(pos, dto);
					}
					pos++;
					tbOrder.addRow(row);
				}
			}
			// kiem tra neu ko du lieu thi add row

			// totalPage < 0 when delete at last page
			if (tbOrder.getPagingControl().totalPage < 0)
				tbOrder.setTotalSize(listMngDTO.total, 1);
		}
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent actionEvent = modelEvent.getActionEvent();
		switch (actionEvent.action) {
		case ActionEventConstant.GET_LIST_GSNPP_BY_GSBH:
			dtoListGSNPP = (ListStaffDTO) modelEvent.getModelData();
			if (dtoListGSNPP != null) {
				initSpGSNPP();
				getListSaleStaff();
			}
			break;
		case ActionEventConstant.GET_LIST_NVBH:
		case ActionEventConstant.GET_LIST_NVBH_BY_GSBH:
			dtoListStaff = (ListStaffDTO) modelEvent.getModelData();
			if (dtoListStaff != null) {
				//dtoListStaff.addItemAll();
				initSpNVBH();
				onClick(btSearch);
			}
			break;
		case ActionEventConstant.GET_SALE_ORDER:
			getListLog = false;
			ListOrderMngDTO list = (ListOrderMngDTO) modelEvent.getModelData();
			if (list != null) {
				if (this.listMngDTO == null) {
					this.listMngDTO = new ListOrderMngDTO();
				} else {
					this.listMngDTO.getListDTO().clear();
				}

				for (SaleOrderViewDTO item : list.getListDTO()) {
					this.listMngDTO.getListDTO().add(item);
				}
				this.listMngDTO.total = list.total;
				this.listMngDTO.totalIsSend = list.totalIsSend;
				renderLayout();

				// Update icon order status
				// ArrayList<LogDTO> listLog = list.getListLog();
				if (parent != null) {
					if(parent instanceof SalesPersonActivity) {
						parent.sendBroadcaseNotifyOrder(list.notifyDTO);
					}

					parent.closeProgressDialog();
				}
				if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES) {
					tvNotChecked.setText(StringUtil.getStringAndReplace(R.string.TEXT_X_ORDER_NOT_SEND, ""
							+ listMngDTO.totalIsSend));
				}
			}
			requestInsertLogKPI(HashMapKPI.NVBH_DANHSACHDONHANG, modelEvent.getActionEvent().startTimeFromBoot);

			break;
		case ActionEventConstant.DELETE_SALE_ORDER:
			VTLog.e("PhucNT4", " thoi gian sau khi delete " + DateUtils.now());
			// Remove don hang khoi ds don hang
			SaleOrderViewDTO dto = (SaleOrderViewDTO) actionEvent.viewData;
			for (int i = 0; i < listMngDTO.getListDTO().size(); i++) {
				if (dto.saleOrder.saleOrderId == listMngDTO.getListDTO().get(i).saleOrder.saleOrderId) {
					listMngDTO.getListDTO().remove(i);
					break;
				}
			}

			// Sent broad cast de cap nhat icon notify don hang loi
			Bundle b = new Bundle();
			// b.putInt(IntentConstants.INTENT_DATA, count);
			b.putLong(IntentConstants.INTENT_SALE_ORDER_ID, dto.saleOrder.saleOrderId);
			((GlobalBaseActivity) GlobalInfo.getInstance().getActivityContext()).sendBroadcast(
					ActionEventConstant.NOTIFY_DELETE_SALE_ORDER, b);

			// xoa don hang cuoi cung cua trang cuoi cung
			// reset lai trang truoc do
			if ((tbOrder.getPagingControl().getCurrentPage() > 1 && tbOrder.getPagingControl().getCurrentPage() == tbOrder
					.getPagingControl().totalPage) && (listMngDTO.listDTO.size() == 0)) {

				tbOrder.getPagingControl().setCurrentPage(tbOrder.getPagingControl().getCurrentPage() - 1);

				tbOrder.getPagingControl().totalPage = -1;
			} else {
				// Old current page
				int currentPage = tbOrder.getPagingControl().getCurrentPage();

				// Calculate total page & set current page = 1: old code
				tbOrder.setTotalSize(listMngDTO.total - 1, currentPage);
				if (currentPage <= tbOrder.getPagingControl().totalPage) {
					tbOrder.getPagingControl().setCurrentPage(currentPage);
				} else {
					tbOrder.getPagingControl().setCurrentPage(tbOrder.getPagingControl().totalPage);
				}
			}

			// renderLayout();
			getListOrder();

			parent.closeProgressDialog();
			tvNotChecked.setText(StringUtil.getStringAndReplace(R.string.TEXT_X_ORDER_NOT_SEND, ""
					+ (listMngDTO.totalIsSend - 1)));
			// parent.showToastMessage("Xóa đơn hàng thành công");
			parent.showDialog(StringUtil.getString(R.string.TEXT_DELETE_ORDER_SUCCESS));
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
		case ActionEventConstant.GET_SALE_ORDER:
			parent.showDialog(StringUtil.getString(R.string.MESSAGE_ERROR_HAPPEN));
			break;
		case ActionEventConstant.DELETE_SALE_ORDER:
			parent.showDialog(StringUtil.getString(R.string.TEXT_DELETE_ORDER_FAIL));
			break;
		case ActionEventConstant.ACTION_GET_CURRENT_DATE_TIME_SERVER:
			if (parent != null) {
				parent.closeProgressDialog();
			}
			// GlobalUtil.setEnableButton(btTransfer, true);
			super.handleErrorModelViewEvent(modelEvent);
			break;
		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}

	}

	@Override
	public void onClick(View v) {
		
		if (v == btSearch) {
			GlobalUtil.forceHideKeyboard(parent);
			tbOrder.getPagingControl().totalPage = -1;
			updateSearchValue();
			getListOrder();
		}
	}

	/**
	 * update gia tri search
	 * 
	 * @author: PhucNT
	 * @return: void
	 * @throws:
	 */
	private void updateSearchValue() {
		
		mkh = edMKH.getText().toString().trim();
		tkh = edTKH.getText().toString().trim();
		createDate = edTN.getText().toString().trim();
		tbOrder.getPagingControl().setCurrentPage(1);
	}

	/**
	 * reset gia tri search
	 * 
	 * @author: PhucNT
	 * @return: void
	 * @throws:
	 */
	private void resetSearchValue() {
		edMKH.getText().clear();
		edTKH.getText().clear();
		createDate = DateUtils.getCurrentDate();
		this.edTN.setText(createDate);
		spinnerState.setSelection(0);
		this.selectedState = STATUS_ALL;
		updateSearchValue();
	}

	/**
	 * updateDisplay
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public void updateDate(int day, int month, int year) {
		this.mDay = day;
		this.mMonth = month;
		this.mYear = year;

		String sDay = String.valueOf(mDay);
		String sMonth = String.valueOf(mMonth + 1);
		if (mDay < 10) {
			sDay = "0" + sDay;
		}
		if (mMonth + 1 < 10) {
			sMonth = "0" + sMonth;
		}

		if (currentCalender == DATE_FROM_CONTROL) {
			edTN.setTextColor(ImageUtil.getColor(R.color.BLACK));
			edTN.setText(new StringBuilder()
					// Month is 0 based so add 1
					.append(sDay).append("/").append(sMonth).append("/").append(mYear).append(" "));
		}

	}

	/*private ArrayList<SaleOrderDTO> convertToListSaleOrder(ArrayList<SaleOrderViewDTO> listSelectedOrder) {
		
		ArrayList<SaleOrderDTO> listSaleOder = new ArrayList<SaleOrderDTO>();
		for (int i = 0; i < listSelectedOrder.size(); i++) {
			SaleOrderDTO sale = listSelectedOrder.get(i).saleOrder;
			// sale.saleOrderId = listSelectedOrder.get(i).getSaleOrderId();
			// sale.isSend = 1;
			listSaleOder.add(sale);
		}
		return listSaleOder;
	}*/

	private void getListOrder() {
		// parent.showProgressDialog(getString(R.string.loading));
		String dateTimePattern = StringUtil.getString(R.string.TEXT_DATE_TIME_PATTERN);
		Pattern pattern = Pattern.compile(dateTimePattern);
		Bundle viewData = new Bundle();

		if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES) {
			viewData.putString(IntentConstants.INTENT_FIND_ORDER_STAFFID,
					String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
		} else if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSNPP ||
				GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSTINH) {
			viewData.putString(IntentConstants.INTENT_FIND_ORDER_STAFFID,
					textStaffId);
		}
		if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSTINH) {
			viewData.putString(IntentConstants.INTENT_FIND_ORDER_STAFFOWNERID,
					textGSNPPId);
		}
		viewData.putString(IntentConstants.INTENT_FIND_ORDER_SHOP_ID, GlobalInfo.getInstance().getProfile()
				.getUserData().shopId);
		// ngay tao
		if (!StringUtil.isNullOrEmpty(createDate)) {
			String strTN = createDate;
			Matcher matcher = pattern.matcher(strTN);
			if (!matcher.matches()) {
				parent.showDialog(StringUtil.getString(R.string.TEXT_DATE_TIME_PATTERN));
				return;
			} else {
				try {
					Date tn = StringUtil.stringToDate(strTN, "");
					String strFindTN = StringUtil.dateToString(tn, "yyyy-MM-dd");
					viewData.putString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE, strFindTN);
				} catch (Exception ex) {
					parent.showDialog(StringUtil.getString(R.string.TEXT_START_DATE_SEARCH_SYNTAX_ERROR));
					return;
				}
			}
		}

		if (!StringUtil.isNullOrEmpty(createDate)) {
			try {
				Date dn = StringUtil.stringToDate(createDate, "");
				String strFindDN = StringUtil.dateToString(dn, "yyyy-MM-dd");
				viewData.putString(IntentConstants.INTENT_FIND_ORDER_TO_DATE, strFindDN);
			} catch (Exception ex) {
				parent.showDialog(StringUtil.getString(R.string.TEXT_END_DATE_SEARCH_SYNTAX_ERROR));
				return;
			}
		}

		if (!StringUtil.isNullOrEmpty(mkh)) {
			viewData.putString(IntentConstants.INTENT_FIND_ORDER_CUSTOMER_CODE, this.mkh);
		}

		if (!StringUtil.isNullOrEmpty(tkh)) {
			viewData.putString(IntentConstants.INTENT_FIND_ORDER_CUSTOMER_NAME, tkh);
		}

		viewData.putInt(IntentConstants.INTENT_FIND_ORDER_STATUS, this.selectedState);

		int page=tbOrder.getPagingControl().getCurrentPage() - 1;
		
//		String page = " limit " + ((tbOrder.getPagingControl().getCurrentPage() - 1) * Constants.NUM_ITEM_PER_PAGE)
//				+ "," + Constants.NUM_ITEM_PER_PAGE;

		viewData.putInt(IntentConstants.INTENT_PAGE, page);
		// can hien thi icon notify don hang loi
		viewData.putBoolean(IntentConstants.INTENT_GET_LIST_LOG, getListLog);

		parent.showProgressDialog(getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		e.viewData = viewData;
		e.sender = this;
		e.action = ActionEventConstant.GET_SALE_ORDER;

		SaleController.getInstance().handleViewEvent(e);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == this.spinnerState) {
			switch (arg2) {
			case 0:
				this.selectedState = STATUS_ALL;
				break;
			case 1:
				this.selectedState = STATUS_NOT_SEND;
				break;
			case 2:
				this.selectedState = STATUS_WAITING_PROCESS;
				break;
			case 3:
				this.selectedState = STATUS_SUCCESS;
				break;
			case 4:
				this.selectedState = STATUS_DENY;
				break;
			case 5:
				this.selectedState = STATUS_OVERDATE;
				break;
			default:
				this.selectedState = STATUS_ALL;
				break;
			}
			onClick(btSearch);
		} else if (arg0 == spinnerNVBH) {
			if (currentNVBH != spinnerNVBH.getSelectedItemPosition()) {
				currentNVBH = spinnerNVBH.getSelectedItemPosition();
				textStaffId = dtoListStaff.arrList.get(spinnerNVBH.getSelectedItemPosition()).id;
				onClick(btSearch);
			}
		} else if (arg0 == spinnerGSNPP) {
			if (currentGSNPP != spinnerGSNPP.getSelectedItemPosition()) {
				currentGSNPP = spinnerGSNPP.getSelectedItemPosition();
				textGSNPPId = dtoListGSNPP.arrList.get(spinnerGSNPP.getSelectedItemPosition()).id;
				getListSaleStaff();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.listener.VinamilkTableListener#loadMore(int,
	 * android.view.View, java.lang.Object)
	 */
	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		
		if (control == tbOrder) {
			getListOrder();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
    		case ACTION_DELETE_ORDER_SUCCESS:
    			deleteOrder((SaleOrderViewDTO) data);
    			break;
    		case ACTION_DELETE_ORDER_FAIL:
    			break;
    		default:
    			super.onEvent(eventType, control, data);
    			break;
		}
	}

	private void deleteOrder(SaleOrderViewDTO dto) {
		
		if (dto != null) {
			parent.showProgressDialog(getString(R.string.loading), false);
			ActionEvent e = new ActionEvent();
			e.viewData = dto;
			e.sender = this;
			e.action = ActionEventConstant.DELETE_SALE_ORDER;
			SaleController.getInstance().handleViewEvent(e);
		}
	}

	/**
	 * di toi man hinh chi tiet Khach hang
	 * 
	 * @author : PhucNT since : 10:59:39 AM
	 */
	public void gotoCustomerInfo(long customerId) {
		isFirstLoad = false;
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bunde = new Bundle();
		bunde.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "02-03");
		bunde.putString(IntentConstants.INTENT_CUSTOMER_ID, String.valueOf(customerId));
		e.viewData = bunde;
		e.action = ActionEventConstant.GO_TO_CUSTOMER_INFO;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Den man hinh tao don hang
	 * 
	 * @author: TruongHN
	 * @param approved 
	 * @return: void
	 * @throws:
	 */
	private void gotoViewOrder(String orderId, int approved, CustomerDTO cus) {
		isFirstLoad = false;
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_ORDER_ID, orderId);
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, String.valueOf(cus.customerId));
		bundle.putString(IntentConstants.INTENT_CUSTOMER_CODE, cus.customerCode);
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ADDRESS, cus.getStreet());
		bundle.putString(IntentConstants.INTENT_CUSTOMER_NAME, cus.getCustomerName());
		bundle.putInt(IntentConstants.INTENT_CUSTOMER_TYPE_ID, cus.getCustomerTypeId());
		bundle.putInt(IntentConstants.INTENT_FIND_ORDER_BAPPROVED, approved);
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_ORDER_VIEW;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.listener.VinamilkTableListener#handleRowEvent
	 * (android.view.View, java.lang.Object)
	 */
	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		
		switch (action) {
		case OrderMngRow.ACTION_CLICK_MKH:
			SaleOrderViewDTO item = (SaleOrderViewDTO) data;
			gotoCustomerInfo(item.getCustomerId());
			break;
		case OrderMngRow.ACTION_CLICK_SDH:
			// vao man hinh so don hang , dang cheat code
			SaleOrderViewDTO item1 = (SaleOrderViewDTO) data;
			gotoViewOrder(String.valueOf(item1.getSaleOrderId()), item1.saleOrder.approved, item1.customer);
			break;
		case OrderMngRow.ACTION_CLICK_DELETE:
			GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.TEXT_CONFIRM_DELETE_ORDER),
				StringUtil.getString(R.string.TEXT_HAVE), ACTION_DELETE_ORDER_SUCCESS,
				StringUtil.getString(R.string.TEXT_NO), ACTION_DELETE_ORDER_FAIL, data);
			break;
		case OrderMngRow.ACTION_CLICK_SELECT:
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void receiveBroadcast(int action, Bundle bundle) {
		

		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				// cau request du lieu man hinh
				tbOrder.getPagingControl().totalPage = -1;
				resetSearchValue();
				// TruongHN - khi dong bo du lieu ve thi can lay lai ds log de
				// hien thi notify icon
				getListLog = true;
				getListOrder();
			}
			break;
		case ActionEventConstant.BROADCAST_UPDATE_EDIT_ORDER:
			OrderViewDTO orderDTO = (OrderViewDTO) bundle.getSerializable(IntentConstants.INTENT_UPDATE_EDITED_ORDER);
			if (orderDTO != null) {
				for (int i = 0; i < listMngDTO.getListDTO().size(); i++) {
					SaleOrderViewDTO dto = listMngDTO.getListDTO().get(i);
					if (orderDTO.orderInfo.saleOrderId == dto.saleOrder.saleOrderId) {
						// dto.saleOrder.isSend = orderDTO.orderInfo.isSend;
						dto.saleOrder.total = orderDTO.orderInfo.total;
						dto.setDESCRIPTION("");
						break;
					}
				}

				renderLayout();
			}
			break;
		case ActionEventConstant.NOTIFY_LIST_ORDER_STATE: {
			ArrayList<LogDTO> list = (ArrayList<LogDTO>) bundle.getSerializable(IntentConstants.INTENT_DATA);

			if (list != null && listMngDTO != null) {
				for (SaleOrderViewDTO dto : listMngDTO.getListDTO()) {
					for (LogDTO logDto : list) {
						if (dto.saleOrder.saleOrderId == Long.valueOf(logDto.tableId)) {
							dto.saleOrder.synState = Integer.parseInt(logDto.state);
						}
					}
				}
			}

			renderLayout();
			break;
		}
		default:
			super.receiveBroadcast(action, bundle);
			break;
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		
		if (v == edTN) {
			if (!edTN.onTouchEvent(arg1)) {
				currentCalender = DATE_FROM_CONTROL;
				parent.fragmentTag = ListOrderView.TAG;
				parent.showDatePickerWithDate(GlobalBaseActivity.DATE_DIALOG_ID, edTN.getText().toString(), true);
			}
		}
		return true;
	}
}
