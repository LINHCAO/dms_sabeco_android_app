/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.view.DisplayPresentProductInfo;
import com.viettel.dms.dto.view.TBHVProgReportProDispDetailSaleDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.MenuItem;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * man hinh :01-04-02. Tiến độ CTTB chi tiết NVBH ngày (TBHV)
 * 
 * @author: HoanPD1
 * @version: 1.1
 * @since: 1.0
 */
public class TBHVProgReportProDispDetailSaleView extends BaseFragment implements OnEventControlListener,
		VinamilkTableListener, OnClickListener, OnItemSelectedListener {

	public static final String TAG = TBHVProgReportProDispDetailSaleView.class.getName();
	private static final int ACTION_DATE = 1;// Bao cao nagy
	private static final int ACTION_ACC = 2; // Bao cao luy ke thang
	private static final int ACTION_CTTB = 3;// Chuong trinh TB
	private static final int ACTION_PSDS = 4;// khach hang chua PSDS
	private static final int ACTION_MHTT = 5;// Bao cao mat hang trong tam

	TextView tvGSNPPCode;// ma GSNPP
	TextView tvNVBHCode;// ma nhan vien ban hang
	Button btViewAll;// hien thi tat ca
	private Spinner spinnerCTTB;// spinnerLine

	private VinamilkTableView tbList;
	GlobalBaseActivity parent;
	// data
	TBHVProgReportProDispDetailSaleDTO dto;

//	private String stCodeNPP; // ma NPP
//	private String stNameNPP; // ten NPP
//	private String stCodeGSNPP; // ma GSNPP
	private String stNameGSNPP; // ten GSNPP
	private String stStaffCode; // ma nhan vien ban hang
	private String stStaffName; // ten nhan vien ban hang
	private long staffId; // id nhan vien ban hang
	private boolean isFirstTime;// kiem tra lan dau tien vao man hinh
	private boolean checkAll; // kiem tra xem tat ca
	private boolean checkSendRequest = true;// check da gui request lay ds hay
											// chua khi refresh lai MH thi ko
											// goi nua
	private int currentPage = -1;// ten trang
	private int currentSpinerSelectedItem = -1;
	private ArrayList<DisplayPresentProductInfo> disPreProInfo;
	private String[] displayProgramCodeList;

	public static TBHVProgReportProDispDetailSaleView getInstance(Bundle data) {
		TBHVProgReportProDispDetailSaleView f = new TBHVProgReportProDispDetailSaleView();
		// Supply index input as an argument.
		f.setArguments(data);
		return f;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (GlobalBaseActivity) getActivity();

		// lay du lieu truyen qua
		Bundle bundle = getArguments();
//		stCodeNPP = bundle.getString(IntentConstants.INTENT_SHOP_CODE);
//		stNameNPP = bundle.getString(IntentConstants.INTENT_SHOP_NAME);
//		stCodeGSNPP = bundle.getString(IntentConstants.INTENT_STAFF_OWNER_CODE);
		stNameGSNPP = bundle.getString(IntentConstants.INTENT_STAFF_OWNER_NAME);
		stStaffCode = bundle.getString(IntentConstants.INTENT_STAFF_CODE);
		stStaffName = bundle.getString(IntentConstants.INTENT_STAFF_NAME);
		staffId = Long.parseLong(bundle.getString(IntentConstants.INTENT_STAFF_ID));

		disPreProInfo = (ArrayList<DisplayPresentProductInfo>) bundle
				.getSerializable(IntentConstants.INTENT_TBHV_LIST_CTTB_NPP);
		currentSpinerSelectedItem = bundle.getInt(IntentConstants.INTENT_TBHV_CURRENT_CTTB_NPP_SELECTED);

		displayProgramCodeList = new String[disPreProInfo.size()];
		for (int i = 0; i < disPreProInfo.size(); i++) {
			displayProgramCodeList[i] = disPreProInfo.get(i).displayProgramCode;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		isFirstTime = true;
		checkAll = false;
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_tbhv_prog_report_pro_disp_detail_sale, container,
				false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		this.setTitleForScreen();
		initMenuActionBar();

		tvGSNPPCode = (TextView) v.findViewById(R.id.tvGSNPPCode);
		tvNVBHCode = (TextView) v.findViewById(R.id.tvNVBHCode);
		btViewAll = (Button) v.findViewById(R.id.btViewAll);
		btViewAll.setOnClickListener(this);
		tbList = (VinamilkTableView) v.findViewById(R.id.tbList);
		tbList.setListener(this);
		spinnerCTTB = (Spinner) v.findViewById(R.id.spinnerPath);
		SpinnerAdapter adapterCTTB = new SpinnerAdapter(parent, R.layout.simple_spinner_item, displayProgramCodeList);
		spinnerCTTB.setAdapter(adapterCTTB);
		spinnerCTTB.setOnItemSelectedListener(this);
		spinnerCTTB.setSelection(currentSpinerSelectedItem);

		// tvGSNPPCode.setText(stCodeGSNPP + " - " + stNameGSNPP);
		tvGSNPPCode.setText(stNameGSNPP);
		tvNVBHCode.setText(stStaffCode +"-"+ stStaffName);
		if (dto != null && currentPage > 0) {
			renderLayout();
			tbList.getPagingControl().setCurrentPage(currentPage);
		} else {
			getAccList(disPreProInfo.get(currentSpinerSelectedItem).displayProgramCode, 0, 0);
		}

		return v;
	}

	/**
	 * set title for screen
	 * 
	 * @author: HoanPD1
	 * @return: void
	 * @throws:
	 */
	private void setTitleForScreen() {
		// TODO Auto-generated method stub
		SpannableObject obj = new SpannableObject();
		obj.addSpan(getString(R.string.TITLE_VIEW_TBHV_PROG_REPOST_PRO_DISP_DETAIL_SALE) + " ",
				ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
		obj.addSpan(DateUtils.defaultDateFormat.format(new Date()), ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		setTitleHeaderView(obj);
	}

	/**
	 * thiet lap menubar
	 * 
	 * @author: HoanPD1
	 * @return: void
	 * @throws:
	 */
	private void initMenuActionBar() {
		// TODO Auto-generated method stub
		enableMenuBar(this);
		addMenuItem(getString(R.string.TEXT_HEADER_MENU_PSDS), R.drawable.icon_order, ACTION_PSDS);
		addMenuItem(getString(R.string.TEXT_HEADER_MENU_CTTB), R.drawable.icon_reminders, ACTION_CTTB);
		addMenuItem(getString(R.string.TEXT_HEADER_MENU_MHTT), R.drawable.icon_list_star, ACTION_MHTT);
		addMenuItem(getString(R.string.TEXT_HEADER_MENU_REPORT_MONTH), R.drawable.icon_accumulated, ACTION_ACC);
		addMenuItem(getString(R.string.TEXT_HEADER_MENU_REPORT_DATE), R.drawable.icon_calendar, ACTION_DATE);
		setMenuItemFocus(2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (checkSendRequest) {
		} else {
			// layout danh sach khach hang khong dat
			renderLayout();
			// layout hien thi tat ca khach hang
			checkSendRequest = true;
		}
	}

	/**
	 * 
	 * thiet lap list row
	 * 
	 * @author: HoanPD1
	 * @param checkAll
	 *            //kiem tra co phai request hien thi tat ca hay khong?
	 * @return: void
	 * @throws:
	 */
	private void getAccList(String disProCode, int checkAll, int page) {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		bundle.putLong(IntentConstants.INTENT_STAFF_ID, staffId);
		bundle.putInt(IntentConstants.INTENT_PAGE, page);
		bundle.putString(IntentConstants.INTENT_DISPLAY_PROGRAM_CODE, disProCode);
		// bundle.putString(IntentConstants.INTENT_DISPLAY_PROGRAM_LEVEL,
		// stDisplayProgrameLevel);
		bundle.putInt(IntentConstants.INTENT_ID, checkAll);
		ActionEvent e = new ActionEvent();
		e.viewData = bundle;
		e.action = ActionEventConstant.GET_TBHV_PROG_REPOST_PRO_DISP_DETAIL_SALE;
		e.sender = this;
		TBHVController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.GET_TBHV_PROG_REPOST_PRO_DISP_DETAIL_SALE:
			dto = (TBHVProgReportProDispDetailSaleDTO) modelEvent.getModelData();
			if (!checkAll && dto.listItem.size() <= 0) {
				checkAll = true;
				getAccList(disPreProInfo.get(currentSpinerSelectedItem).displayProgramCode, 1, 0);
			} else {
				renderLayout();
			}
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
		parent.closeProgressDialog();
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		super.handleErrorModelViewEvent(modelEvent);
	}

	/**
	 * 
	 * Layout cho man hinh
	 * 
	 * @author: HoanPD1
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		// paging

		if (currentPage <= 0) {
			tbList.setTotalSize(dto.total);
			currentPage = tbList.getPagingControl().getCurrentPage();
		} else {
			if (checkSendRequest) {
				checkSendRequest = true;
				tbList.setTotalSize(dto.total);
			}
			tbList.getPagingControl().setCurrentPage(currentPage);
		}

		List<TableRow> listRows = new ArrayList<TableRow>();
		for (int i = 0; i < dto.listItem.size(); i++) {
			TBHVProgReportProDispDetailSaleRow row = new TBHVProgReportProDispDetailSaleRow(parent, false);
			row.render(dto.listItem.get(i));
			row.setListener(this);
			listRows.add(row);
		}

		if (checkAll == false) {
			// TBHVProgReportProDispDetailSaleRow row = new
			// TBHVProgReportProDispDetailSaleRow(
			// parent, true);
			// row.renderSum(dto.remainSaleTotal);
			// listRows.add(row);
			btViewAll.setVisibility(View.VISIBLE);
		} else {
			if (dto.listItem.size() == 0) {
				TBHVProgReportProDispDetailSaleRow row = new TBHVProgReportProDispDetailSaleRow(parent, false);
				row.renderRowNull(StringUtil.getString(R.string.TEXT_NOTIFY_CUSTOMER_NULL));
				listRows.add(row);
			}
			btViewAll.setVisibility(View.GONE);
		}

		if (isFirstTime) {
			tbList.setNumItemsPage(Constants.NUM_ITEM_PER_PAGE);
			isFirstTime = true;
		}
		tbList.addContent(listRows);

	}

	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		if (control instanceof MenuItem) {
			switch (eventType) {
			case ACTION_DATE: {
				ActionEvent e = new ActionEvent();
				e.sender = this;
				e.viewData = new Bundle();
				e.action = ActionEventConstant.ACTION_TBHV_REPORT_PROGRESS_DATE;
				TBHVController.getInstance().handleSwitchFragment(e);
				break;
			}
			case ACTION_ACC: {
				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.GOTO_TBHV_REPORT_PROGRESS_MONTH;
				e.sender = this;
				TBHVController.getInstance().handleSwitchFragment(e);
				break;
			}
			case ACTION_MHTT: {
				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.GOTO_TBHV_REPORT_PROGRESS_SALES_FORCUS;
				e.sender = this;
				TBHVController.getInstance().handleSwitchFragment(e);
				break;
			}
			case ACTION_CTTB: {
				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.GET_TBHV_DIS_PRO_COM_PROG_REPORT;
				e.sender = this;
				TBHVController.getInstance().handleSwitchFragment(e);
				break;
			}
			case ACTION_PSDS:
				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.GO_TO_TBHV_REPORT_CUSTOMER_NOT_PSDS;
				e.sender = this;
				TBHVController.getInstance().handleSwitchFragment(e);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		if (control == tbList) {
			currentPage = tbList.getPagingControl().getCurrentPage();
			getAccList(disPreProInfo.get(currentSpinerSelectedItem).displayProgramCode, 1, currentPage);
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {

	}

	public void onClick(View v) {
		super.onClick(v);
		if (v == btViewAll) {
			if (checkSendRequest) {
				checkAll = true;
				getAccList(disPreProInfo.get(currentSpinerSelectedItem).displayProgramCode, 1, 0);
			}
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				checkAll = false;
				currentPage = -1;
				getAccList(disPreProInfo.get(currentSpinerSelectedItem).displayProgramCode, 0, 0);
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android
	 * .widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == spinnerCTTB) {
			if (currentSpinerSelectedItem != spinnerCTTB.getSelectedItemPosition()) {
				currentSpinerSelectedItem = spinnerCTTB.getSelectedItemPosition();
				checkAll = false;
				getAccList(disPreProInfo.get(currentSpinerSelectedItem).displayProgramCode, 0, 0);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android
	 * .widget.AdapterView)
	 */
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
