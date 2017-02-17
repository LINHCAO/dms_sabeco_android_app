/**
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.view.sale.promotion;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.FragmentMenuContanst;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.db.ProInfoDTO;
import com.viettel.dms.dto.view.ComboBoxDisplayProgrameItemDTO;
import com.viettel.dms.dto.view.CustomerAttendProgramListDTO;
import com.viettel.dms.dto.view.CustomerAttendProgramListItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.AbstractAlertDialog;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.sabeco.R;

/**
 * Them khach hang tham gia chuong trinh HTBH
 * 
 * @author: HoanPD1
 * @version: 1.0
 * @since: 1.0
 */
public class CustomerAddSupportSalesView extends AbstractAlertDialog implements
		OnClickListener, OnItemSelectedListener, VinamilkTableListener {
	// action tim kiem
	public static int actionGetCustomer;
	// action chon row
	public static int actionChooseCustomer;
	// action button chon
	public static int actionChoose;
	// // view layout
	// public View viewLayout;
	// listtener
	protected OnEventControlListener listener;
	// list row
	private VinamilkTableView tbCusList;
	// ma khach hang
	private VNMEditTextClearable edCustomer;
	// spinner tuyen
	private Spinner spinnerLine;
	// Linear level
	private LinearLayout llLevel;
	// spinner muc tham gia
	private Spinner spinnerLevel;
	// button tim kiem
	private Button btSearch;
	// Button Chon
	private Button btChoose;
	// Button dong
	private Button btClose;
	// Checkbox header
	CheckBox cbChooseAll;
	// trang hien tai dang duoc chon tren table
	private int currentPage;
	// co lay tong so trang ko?!
	public boolean isGetTotalPage;
	private int selectLine = 0;
	private int selectLevel = 0;
	// danh sach muc tham gia
	List<ComboBoxDisplayProgrameItemDTO> listLevel = new ArrayList<ComboBoxDisplayProgrameItemDTO>();
	// DTO cua page hien tai
	CustomerAttendProgramListDTO dtoCurrentPage;
	// DTO danh sach khach hang de luu
	CustomerAttendProgramListDTO dtoSave;
	List<ArrayList<CustomerAttendProgramListItem>> listCusSelected;
	// danh sach khach hang da tham gia
	ArrayList<CustomerAttendProgramListItem> cusList = new ArrayList<CustomerAttendProgramListItem>();
	// danh sach khach hang da tham gia
	ArrayList<CustomerAttendProgramListItem> cusListCho = new ArrayList<CustomerAttendProgramListItem>();
	// item khach hang tham gia
	CustomerAttendProgramListItem dtoItem;
	// chuong trinh tham gia
	private ProInfoDTO saleSupportDTO;

	public CustomerAddSupportSalesView(Context context, BaseFragment base,
			CharSequence title, ArrayList<CustomerAttendProgramListItem> listCus) {
		super(context, base, title);
		View v = setViewLayout(R.layout.layout_customer_add_support_sales_view);
		Bundle bundle = FragmentMenuContanst.NVBH_PROGRAM_MENU_COMMAND_HAVE_DONE
				.getDataInfo();
		saleSupportDTO = (ProInfoDTO) bundle
				.getSerializable(IntentConstants.INTENT_PROMOTION);
		initView(v);
		if (dtoCurrentPage == null) {
			isGetTotalPage = true;
			if (listCus == null) {
				getProgrameLevel();
			} else {
				cusList = listCus;
			}
		} else {
			renderLayout();
		}
	}

	/**
	 * thiet lap control
	 * 
	 * @author: hoanpd1
	 * @since: 1.0
	 * @return: void
	 * @throws:
	 * @param v
	 */
	private void initView(View viewLayout) {
		dtoSave = new CustomerAttendProgramListDTO();
		edCustomer = (VNMEditTextClearable) viewLayout
				.findViewById(R.id.edCustomer);
		spinnerLine = (Spinner) viewLayout.findViewById(R.id.spLine);
		spinnerLine.setOnItemSelectedListener(this);
		llLevel = (LinearLayout) viewLayout.findViewById(R.id.llLevel);
		spinnerLevel = (Spinner) viewLayout.findViewById(R.id.spLevelJoin);
		spinnerLevel.setOnItemSelectedListener(this);
		cbChooseAll = (CheckBox) viewLayout.findViewById(R.id.cbChooseAll);
		cbChooseAll.setOnClickListener(this);
		// cbChooseAll.setOnCheckedChangeListener(this);
		btSearch = (Button) viewLayout.findViewById(R.id.btSearch);
		btSearch.setOnClickListener(this);
		btChoose = (Button) viewLayout.findViewById(R.id.btChoose);
		btChoose.setOnClickListener(this);
		btClose = (Button) viewLayout.findViewById(R.id.btClose);
		btClose.setOnClickListener(this);
		tbCusList = (VinamilkTableView) viewLayout.findViewById(R.id.tbCusList);
		tbCusList.setListener(this);
		SpinnerAdapter adapterLine = new SpinnerAdapter(parent,
				R.layout.simple_spinner_item, Constants.ARRAY_LINE_CHOOSE);
		spinnerLine.setAdapter(adapterLine);
		if (dtoCurrentPage != null && currentPage != -1) {
			spinnerLine.setSelection(selectLine);
		} else {
			spinnerLine.setSelection(Constants.ARRAY_LINE_CHOOSE.length - 1);
			selectLine = spinnerLine.getSelectedItemPosition();
		}
	}

	/**
	 * Lay muc cua CT HTBH
	 * 
	 * @author: HoanPD
	 * @since: 1.0
	 * @return: void
	 * @throws:
	 */
	private void getProgrameLevel() {
		ActionEvent e = new ActionEvent();
		Bundle bundle = new Bundle();
		bundle.putLong(IntentConstants.INTENT_ID, saleSupportDTO.PRO_INFO_ID);
		e.viewData = bundle;
		e.action = ActionEventConstant.GET_LIST_LEVEL_ATTEND;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * Lay danh sach khach hang chua tham gia chuong trinh khi chon button them
	 * khach hang
	 * 
	 * @author: hoanpd1
	 * @since: 11:25:44 16-05-2014
	 * @return: void
	 * @throws:
	 * @param i
	 * @param j
	 */
	public void getAddCustomerList(int page, boolean isGetTotalPage) {
		// Show dialog loading...
		if (!parent.isShowProgressDialog()) {
			parent.showLoadingDialog();
		}
		this.currentPage = page;
		Bundle bundle = new Bundle();
		bundle.putInt(IntentConstants.INTENT_PAGE, page); // Load 1 lan het luon
		bundle.putBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE, isGetTotalPage);
		bundle.putLong(IntentConstants.INTENT_STAFF_ID, GlobalInfo.getInstance()
				.getProfile().getUserData().id);
		bundle.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo
				.getInstance().getProfile().getUserData().shopId);
		// chuong trinh
		bundle.putLong(IntentConstants.INTENT_PROGRAM_ID,
				saleSupportDTO.PRO_INFO_ID);
		// chon tuyen
		String plan = DateUtils
				.getVisitPlan(Constants.ARRAY_LINE_CHOOSE[selectLine]);
		bundle.putString(IntentConstants.INTENT_VISIT_PLAN, plan);
		// -- Muc (Level)
		if (listLevel.size() > 0) {
			bundle.putString(IntentConstants.INTENT_LEVEL,
					listLevel.get(selectLevel).value);
		} else {
			bundle.putString(IntentConstants.INTENT_LEVEL, "0");
		}
		// Lay chuoi search
		String strSearch = edCustomer.getText().toString().trim();
		bundle.putString(IntentConstants.INTENT_DATA, strSearch);

		setHandleViewEvent(
				ActionEventConstant.GET_LIST_CUSTOMER_ADD_SUPPORT_SALES,
				bundle, SaleController.getInstance());
	}

	/**
	 * Render cac muc cua chuong trinh
	 * 
	 * @author: hoanpd1
	 * @since: 09:55:18 25-07-2014
	 * @return: void
	 * @throws:  
	 * @param listLevel1
	 */
	void renderDataLevel(List<ComboBoxDisplayProgrameItemDTO> listLevel1) {
		if (listLevel1.size() <= 1) {
			llLevel.setVisibility(View.INVISIBLE);
		} else {
			ArrayList<String> levelArray = new ArrayList<String>();
			ComboBoxDisplayProgrameItemDTO arg0 = new ComboBoxDisplayProgrameItemDTO();
			for (int i = 0; i < listLevel1.size(); i++) {
				arg0 = new ComboBoxDisplayProgrameItemDTO();
				arg0.value = (i + 1) + "";
				String level = listLevel1.get(i).name;
				arg0.name = level;
				levelArray.add(level);

				listLevel.add(arg0);
			}

			String[] levelArr = levelArray.toArray(new String[levelArray.size()]);
			SpinnerAdapter adapterLevel = new SpinnerAdapter(parent, R.layout.simple_spinner_item, levelArr);
			spinnerLevel.setAdapter(adapterLevel);
		}
	}
	
	/**
	 * 
	 * render Layout
	 * 
	 * @author: HoanPD1
	 * @param dto
	 * @param cusItem
	 * @return: void
	 * @throws:
	 */
	public void renderLayout() {
		if (isGetTotalPage) {
			tbCusList.setTotalSize(dtoCurrentPage.totalCustomer);
			initListSelectedForPage(tbCusList.getPagingControl().totalPage);
			isGetTotalPage = false;
		}

		if (currentPage > 0) {
			tbCusList.getPagingControl().setCurrentPage(currentPage);
		} else {
			currentPage = tbCusList.getPagingControl().getCurrentPage();
		}

		// Kiem tra co check all hay khong
		testCheckAll();

		int pos = 1 + Constants.NUM_ITEM_PER_PAGE * (currentPage - 1);
		List<TableRow> listRows = new ArrayList<TableRow>();
		if (dtoCurrentPage.cusList.size() > 0) {
			for (CustomerAttendProgramListItem dtoItem : dtoCurrentPage.cusList) {
				dtoItem.isCheck = isCheckItem(dtoItem, currentPage);
				CustomerAddSupportSalesRow row = new CustomerAddSupportSalesRow(
						parent, this);
				row.renderLayout(pos, dtoItem);

				pos++;
				listRows.add(row);
			}
		} else {
			tbCusList.showNoContentRowWithString(StringUtil
					.getString(R.string.TEXT_NO_CONTENT_SEARCH));
		}
		tbCusList.addContent(listRows);
		parent.closeProgressDialog();
		forceHideKeyBoardForDialog();
	}

	/**
	 * Kiem tra co check all hay khong
	 * 
	 * @author: vtit
	 * @since: 5:07:15 PM May 26, 2014
	 * @return: void
	 * @throws:
	 */
	private void testCheckAll() {
		int index = currentPage - 1;
		if (index >= 0 && index < listCusSelected.size()) {
			// Kiem tra neu so luong select == voi so luong trong page
			// thi cho checkall
			if (listCusSelected.get(index).size() == dtoCurrentPage.cusList
					.size() && dtoCurrentPage.cusList.size() > 0) {
				cbChooseAll.setChecked(true);
			} else {
				cbChooseAll.setChecked(false);
			}
		}
	}

	/**
	 * Kiem tra 1 item trong trang hien tai co duoc check hay khong
	 * 
	 * @author: vtit
	 * @since: 4:57:01 PM May 26, 2014
	 * @return: boolean
	 * @throws:
	 * @param dtoItem2
	 * @param currentPage2
	 * @return
	 */
	private boolean isCheckItem(CustomerAttendProgramListItem dtoItem,
			int currentPage) {
		boolean checkItem = false;
		int index = currentPage - 1;
		if (index < listCusSelected.size() && index >= 0) {
			ArrayList<CustomerAttendProgramListItem> list = listCusSelected
					.get(index);
			for (CustomerAttendProgramListItem item : list) {
				if (item.customerId == dtoItem.customerId) {
					checkItem = true;
				}
			}
		}
		return checkItem;
	}

	/**
	 * Khoi tao danh sach khach hang duoc chon cho tung trang
	 * 
	 * @author: vtit
	 * @since: 4:45:52 PM May 26, 2014
	 * @return: void
	 * @throws:
	 * @param totalPage
	 */
	private void initListSelectedForPage(int totalPage) {
		if (listCusSelected == null) {
			listCusSelected = new ArrayList<ArrayList<CustomerAttendProgramListItem>>();
		} else {
			listCusSelected.clear();
		}

		for (int i = 0; i < totalPage; i++) {
			listCusSelected.add(new ArrayList<CustomerAttendProgramListItem>());
		}
	}

	/**
	 * 
	 * dung phan trang
	 * 
	 * @author: HoanPD1
	 * @param size
	 * @return: void
	 * @throws:
	 */
	public void setTotalSize(int size) {
		tbCusList.setTotalSize(size);
	}

	@Override
	public void onClick(View v) {
		if (v == btSearch) {
			isGetTotalPage = true;
			getAddCustomerList(1, isGetTotalPage);
		} else if (v == btClose) {
			dismiss();
		} else if (v == btChoose) {
			getListCustomerForSave();
			if (dtoSave.cusList.size() != 0) {
				saveCustomerAttend();
				dismiss();
			} else {
				parent.showDialog(StringUtil
						.getString(R.string.TEXT_NOTICE_NO_CHOOSE_CUSTOMER));
			}
		} else if (v == cbChooseAll) { // Header Checkbox
			if (cbChooseAll.isChecked()) {
				addAllItemCurrentPage(dtoCurrentPage);
			} else {
				int index = currentPage - 1;
				if (index < listCusSelected.size() && index >= 0) {
					listCusSelected.get(index).clear();
				}
			}

			renderLayout();
		}
	}

	/**
	 * Lay danh sach khach hang duoc chon de them
	 * 
	 * @author: vtit
	 * @since: 5:33:27 PM May 26, 2014
	 * @return: void
	 * @throws:
	 */
	private void getListCustomerForSave() {
		if (dtoSave == null) {
			dtoSave = new CustomerAttendProgramListDTO();
		} else {
			dtoSave.cusList.clear();
		}

		for (ArrayList<CustomerAttendProgramListItem> list : listCusSelected) {
			dtoSave.cusList.addAll(list);
		}
	}

	/**
	 * CHon luu khach hang tham gia chuong trinh HTBH
	 * 
	 * @author: hoanpd1
	 * @since: 1.0
	 * @return: void
	 * @throws:
	 */
	private void saveCustomerAttend() {
		parent.showLoadingDialog();
		Bundle data = new Bundle();
		dtoSave.programID = saleSupportDTO.PRO_INFO_ID;
		dtoSave.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
		dtoSave.staffCode = GlobalInfo.getInstance().getProfile().getUserData().userCode;
		dtoSave.day = DateUtils.now();
		dtoSave.type = 0;

		if (listLevel.size() > 0) {
			dtoSave.level = listLevel.get(selectLevel).value;
		} else {
			dtoSave.level = "0";
		}
		data.putSerializable(IntentConstants.INTENT_CUSTOMER_OBJECT, dtoSave);
		setHandleViewEvent(ActionEventConstant.SAVE_CHOOSE_CUSTOMER_ATTEND,
				data, SaleController.getInstance());

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (arg0 == spinnerLine
				&& selectLine != spinnerLine.getSelectedItemPosition()) {
			selectLine = spinnerLine.getSelectedItemPosition();
			spinnerLevel.getSelectedItemPosition();
			isGetTotalPage = true;
			getAddCustomerList(1, isGetTotalPage);
		} else if (arg0 == spinnerLevel
				&& selectLevel != spinnerLevel.getSelectedItemPosition()) {
			selectLevel = spinnerLevel.getSelectedItemPosition();
			spinnerLine.getSelectedItemPosition();
			// isGetTotalPage = true;
			// getAddCustomerList(1, isGetTotalPage);
		}

	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		if (control == tbCusList) {
			currentPage = tbCusList.getPagingControl().getCurrentPage();
			isGetTotalPage = false;
			getAddCustomerList(currentPage, false);
		}

	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		// TODO Auto-generated method stub
		switch (action) {
		case ActionEventConstant.ACTION_CHOOSE_CUSTOMER: {
			CustomerAttendProgramListItem item = (CustomerAttendProgramListItem) data;
			CheckBox check = (CheckBox) control;
			if (check.isChecked() && isCheckItem(item, currentPage) == false) {
				addCheckItem(item);
				testCheckAll();
			}
			if (!check.isChecked()) {
				removeCheckItem(item);
				cbChooseAll.setChecked(false);
			}
			break;
		}
		default:
			break;
		}
	}

	/**
	 * Uncheck 1 item
	 * 
	 * @author: Quangvt
	 * @since: 5:09:18 PM May 26, 2014
	 * @return: void
	 * @throws:
	 * @param item
	 */
	private void removeCheckItem(CustomerAttendProgramListItem item) {
		int index = currentPage - 1;
		if (index < listCusSelected.size() && index >= 0) {
			ArrayList<CustomerAttendProgramListItem> list = listCusSelected
					.get(index);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).customerId == item.customerId) {
					list.remove(i);
					return;
				}
			}
		}
	}

	/**
	 * Check 1 item
	 * 
	 * @author: vtit
	 * @since: 5:09:33 PM May 26, 2014
	 * @return: void
	 * @throws:
	 * @param item
	 */
	private void addCheckItem(CustomerAttendProgramListItem item) {
		int index = currentPage - 1;
		if (index < listCusSelected.size() && index >= 0) {
			ArrayList<CustomerAttendProgramListItem> list = listCusSelected
					.get(index);
			list.add(item);
		}
	} 

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * set danh sach khach hàng c2
	 * @author: hoanpd1
	 * @since: 15:48:42 14-11-2014
	 * @return: void
	 * @throws:  
	 * @param cusC2
	 */
	public void setCustomerC2(CustomerAttendProgramListDTO cusC2) {
		this.dtoCurrentPage = cusC2;
		renderLayout();
		// hide ban phim
		GlobalUtil.forceHideKeyboard(parent);
	}

	/**
	 * add tat ca item của trang hienj tại
	 * @author: hoanpd1
	 * @since: 15:49:13 14-11-2014
	 * @return: void
	 * @throws:  
	 * @param dto
	 */
	private void addAllItemCurrentPage(CustomerAttendProgramListDTO dto) {
		int index = currentPage - 1;
		if (dto == null || dto.cusList == null || dto.cusList.size() <= 0) {
			return;
		}

		if (index < listCusSelected.size() && index >= 0) {
			listCusSelected.get(index).clear();
			listCusSelected.get(index).addAll(dto.cusList);
		}
	}
}
