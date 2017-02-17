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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.view.TBHVDisProComProgReportDTO;
import com.viettel.dms.dto.view.TBHVDisProComProgReportItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
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
import com.viettel.dms.view.supervisor.DisProComProgReportLevelTextView;
import com.viettel.sabeco.R;

/**
 * Man hinh: 01-04. Bao cao chung CTTB ngay (TBHV)
 * 
 * @author: HoanPD1
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVDisProComProgReportView extends BaseFragment implements
		OnEventControlListener, VinamilkTableListener, OnItemSelectedListener {

	private static final int ACTION_DATE = 1;// Bao cao nagy
	private static final int ACTION_ACC = 2; // Bao cao luy ke thang
	private static final int ACTION_CTTB = 3;// Chuong trinh TB
	private static final int ACTION_PSDS = 4;// khach hang chua PSDS
	private static final int ACTION_MHTT = 5;// Bao cao mat hang trong tam

	private GlobalBaseActivity parent;
	private VinamilkTableView tbList;

	// data
	private TBHVDisProComProgReportDTO dto;
	// layout level
	private LinearLayout llLevel;
	// boolean isReload = false;
	boolean isReload = false;
	// so trang
	private int currentPage = -1;

	// combobox list dis pro code
	Spinner spDisProCode;
	// display programe name
	TextView tvDisProName;
	// current promotion selected
	int currentSelected = 0;
	// flag load first data
	boolean isLoadData = false;
	// number item on page of table
	public static final int LIMIT_ROW_PER_PAGE = 10;
	// change display programe
	boolean isRefreshRequest = false;
	// layout for header on table
	LinearLayout llContentHeader;
	// text view progress standar alow not PSDS
	TextView tvProgressStandar;
	TextView tvTotal;
	// content
	HorizontalScrollView hsContentInfo;
	// header info
	LinearLayout llHeaderInfo;
	// notify if null
	TextView tvNotify;

	// tag for fragment
	public static final String TAG = TBHVDisProComProgReportView.class
			.getName();

	public static TBHVDisProComProgReportView getInstance() {
		TBHVDisProComProgReportView f = new TBHVDisProComProgReportView();
		Bundle args = new Bundle();
		f.setArguments(args);
		return f;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (GlobalBaseActivity) getActivity();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_tbhv_dis_pro_com_prog_report, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);

		this.setTitleForScreen();
		this.initmenuItem();

		llLevel = (LinearLayout) view.findViewById(R.id.layoutLevel);
		this.initView(v);

		if (dto == null || !this.isLoadData) {
			getListListReport(Constants.STR_BLANK, 0, true);
		}
		return v;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.main.BaseFragment#onResume()
	 */
	@Override
	public void onResume() {
		if (this.isLoadData && dto != null) {
			this.llContentHeader.setVisibility(View.VISIBLE);
			// update list dis pro
			if (dto.listDisplayProgrameInfo != null
					&& dto.listDisplayProgrameInfo.size() > 0) {
				this.renderDisplayProgramList();
				this.spDisProCode.setSelection(this.currentSelected);
				this.renderDisplayProgrameInfo();
			}
			// update current page
			tbList.getPagingControl().setCurrentPage(currentPage);
			// update list row
			renderLayout();
		}
		super.onResume();
	}

	/**
	 * 
	 * init views for screen
	 * 
	 * @param v
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 30, 2012
	 */
	public void initView(View v) {
		llContentHeader = (LinearLayout) v.findViewById(R.id.llContentHeader);
		llContentHeader.setVisibility(View.INVISIBLE);
		spDisProCode = (Spinner) v.findViewById(R.id.spDisProCode);
		spDisProCode.setOnItemSelectedListener(this);
		tvDisProName = (TextView) v.findViewById(R.id.tvDisProName);
		tvProgressStandar = (TextView) v.findViewById(R.id.tvProgressStandar);
		tvTotal = (TextView) v.findViewById(R.id.tvTotal);
		tbList = (VinamilkTableView) v.findViewById(R.id.tbCusList);
		tbList.setListener(this);
		tbList.setNumItemsPage(LIMIT_ROW_PER_PAGE);
		hsContentInfo = (HorizontalScrollView) v.findViewById(R.id.hsContentInfo);
		llHeaderInfo = (LinearLayout) v.findViewById(R.id.llHeaderInfo);
		tvNotify = (TextView) v.findViewById(R.id.tvNotify);
	}

	/**
	 * 
	 * init menu item for screen
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 30, 2012
	 */
	public void initmenuItem() {
		// enable menu bar
		enableMenuBar(this);
		addMenuItem(getString(R.string.TEXT_HEADER_MENU_PSDS),
				R.drawable.icon_order, ACTION_PSDS);
		addMenuItem(getString(R.string.TEXT_HEADER_MENU_CTTB),
				R.drawable.icon_reminders, ACTION_CTTB);
		addMenuItem(getString(R.string.TEXT_HEADER_MENU_MHTT),
				R.drawable.icon_list_star, ACTION_MHTT);
		addMenuItem(getString(R.string.TEXT_HEADER_MENU_REPORT_MONTH),
				R.drawable.icon_accumulated, ACTION_ACC);
		addMenuItem(getString(R.string.TEXT_HEADER_MENU_REPORT_DATE),
				R.drawable.icon_calendar, ACTION_DATE);
		setMenuItemFocus(2);
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
		obj.addSpan(getString(R.string.TITLE_VIEW_TBHV_DIS_PRO_COM_PROG_REPORT)
				+ Constants.STR_SPACE, ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.NORMAL);
		obj.addSpan(DateUtils.defaultDateFormat.format(new Date()),
				ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		setTitleHeaderView(obj);
	}

	/**
	 * 
	 * get list report with display programe code
	 * 
	 * @param disProCode
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 30, 2012
	 */
	private void getListListReport(String disProCode, int pageNumber,
			boolean isGetCountTotal) {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		if (pageNumber < 0) {
			pageNumber = 0;
		}
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(
				IntentConstants.INTENT_PARENT_SHOP_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile()
						.getUserData().shopId));
		if (!StringUtil.isNullOrEmpty(disProCode)) {
			b.putString(IntentConstants.INTENT_DISPLAY_PROGRAME_CODE,
					disProCode);
		}
		// String page = " limit " + (pageNumber * LIMIT_ROW_PER_PAGE) + ","
		// + LIMIT_ROW_PER_PAGE;
		// b.putString(IntentConstants.INTENT_PAGE, page);
		b.putBoolean(IntentConstants.INTENT_IS_GET_NUM_TOTAL_ITEM,
				isGetCountTotal);

		e.viewData = b;
		e.action = ActionEventConstant.GET_TBHV_DIS_PRO_COM_PROG_REPORT;
		e.sender = this;
		TBHVController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_TBHV_DIS_PRO_COM_PROG_REPORT:
			TBHVDisProComProgReportDTO TMP = (TBHVDisProComProgReportDTO) modelEvent
					.getModelData();
			if (!this.isLoadData) {
				dto = (TBHVDisProComProgReportDTO) modelEvent.getModelData();
			} else {
				dto.arrLevelCode = TMP.arrLevelCode;
				dto.arrList = TMP.arrList;
				dto.arrResultTotal = TMP.arrResultTotal;
				dto.dtoResultTotal = TMP.dtoResultTotal;
				dto.totalItem = TMP.totalItem;
				dto.totalCustomer = TMP.totalCustomer;
				dto.progressStandarPercent = TMP.progressStandarPercent;
			}

			if (dto != null) {
				if (dto.arrLevelCode == null || dto.arrLevelCode.size() == 0) {
					hsContentInfo.setVisibility(View.GONE);
					tvNotify.setVisibility(View.VISIBLE);
				} else {
					hsContentInfo.setVisibility(View.VISIBLE);
					llHeaderInfo.setVisibility(View.VISIBLE);
					// cap nhat lai danh sach bang cach gom nhom cac level cua
					// gsnpp
					// thuoc npp lai voi nhau
					this.updateListItemInData();
					llContentHeader.setVisibility(View.VISIBLE);

					if (dto != null && !this.isLoadData) {
						this.isLoadData = true;
						this.renderDisplayProgramList();
					}
					if (dto.arrList.size() > 0) {
						tbList.getPagingControl().setVisibility(View.VISIBLE);
						if (tbList.getPagingControl().totalPage < 0
								|| isRefreshRequest) {
							tbList.setTotalSize(dto.totalItem);
							tbList.getPagingControl().setCurrentPage(1);
						}
					} else {
						tbList.getPagingControl().setVisibility(View.GONE);
					}
					if (isRefreshRequest) {
						isRefreshRequest = false;
					}
					renderLayout();
				}
			}
			parent.closeProgressDialog();
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}

	}

	/**
	 * 
	 * update list item in data
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 2, 2012
	 */
	public void updateListItemInData() {
		String idNPP = Constants.STR_BLANK;
		String idGSNPP = Constants.STR_BLANK;
		ArrayList<TBHVDisProComProgReportItem> newList = new ArrayList<TBHVDisProComProgReportItem>();
		for (int i = 0, size = dto.arrList.size(); i < size; i++) {
			// them item moi
			TBHVDisProComProgReportItem item = dto.arrList.get(i);
			if (!item.idNPP.equals(idNPP) || !item.idGSNPP.equals(idGSNPP)) {
				newList.add(item);
				idNPP = item.idNPP;
				idGSNPP = item.idGSNPP;
			}
			// cong don level cua item vao
			else {
				for (int j = 0, sizeListNew = newList.size(); j < sizeListNew; j++) {
					TBHVDisProComProgReportItem newItem = newList.get(j);
					if (newItem.idNPP.equals(idNPP)
							&& newItem.idGSNPP.equals(idGSNPP)) {
						for (int k = 0, size3 = newItem.arrLevelCode.size(); k < size3; k++) {
							newItem.arrLevelCode.get(k).resultNumber += item.arrLevelCode
									.get(k).resultNumber;
							newItem.arrLevelCode.get(k).joinNumber += item.arrLevelCode
									.get(k).joinNumber;
						}
						newItem.itemResultTotal.joinNumber += item.itemResultTotal.joinNumber;
						newItem.itemResultTotal.resultNumber += item.itemResultTotal.resultNumber;
					}
				}
			}
		}

		this.dto.arrList = newList;
	}

	/**
	 * 
	 * render list display programe list
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 30, 2012
	 */
	private void renderDisplayProgramList() {
		String[] arrPromotion = new String[this.dto.listDisplayProgrameInfo
				.size()];
		for (int i = 0, size = this.dto.listDisplayProgrameInfo.size(); i < size; i++) {
			arrPromotion[i] = this.dto.listDisplayProgrameInfo.get(i).displayProgramCode;
		}
		SpinnerAdapter adapterLine = new SpinnerAdapter(parent,
				R.layout.simple_spinner_item, arrPromotion);
		// this.isFirstLoadProduct = true;
		this.spDisProCode.setAdapter(adapterLine);
		this.renderDisplayProgrameInfo();
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		super.handleErrorModelViewEvent(modelEvent);
		parent.closeProgressDialog();
	}

	/**
	 * Render du lieu
	 * 
	 * @author: HoanPD1
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		// TODO Auto-generated method stub
		llLevel.removeAllViews();
		tvProgressStandar.setText(String.valueOf(dto.progressStandarPercent)
				+ StringUtil.getString(R.string.TEXT_PERCENT));
		// ren level
		String[] listCharTitle = new String[] { "A", "B", "C", "D", "E", "F",
				"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "W",
				"V", "Y", "Z" };

		int widthTVLevel = dto.arrLevelCode.size() <= 5 ? 380 / (dto.arrLevelCode
				.size() + 1) : 380 / 6;
		for (int i = 0; i < dto.arrLevelCode.size(); i++) {
			DisProComProgReportLevelTextView tv = new DisProComProgReportLevelTextView(
					parent);
			if (i == 0) {
				tv.setWithForTextViewLevelTBHV(widthTVLevel, true);
			} else {
				tv.setWithForTextViewLevelTBHV(widthTVLevel, false);
			}
			tv.render(listCharTitle[i]);
			llLevel.addView(tv);
		}
		TableRow.LayoutParams param = new TableRow.LayoutParams();
		param.width = GlobalUtil.dip2Pixel(widthTVLevel + 10);
		param.height = LayoutParams.FILL_PARENT;
		param.setMargins(GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(2),
				GlobalUtil.dip2Pixel(0), GlobalUtil.dip2Pixel(0));
		tvTotal.setLayoutParams(param);

		// for (int i = 0; i < dto.arrLevelCode.size(); i++) {
		// DisProComProgReportLevelTextView tv = new
		// DisProComProgReportLevelTextView(parent);
		// // tv.render(dto.arrLevelCode.get(i));
		// tv.render(listCharTitle[i]);
		// llLevel.addView(tv);
		// }
		List<TableRow> listRows = new ArrayList<TableRow>();
		for (int i = 0, s = dto.arrList.size(); i < s; i++) {
			TBHVDisProComProgReportRow row = new TBHVDisProComProgReportRow(
					parent, false,
					TBHVDisProComProgReportRow.OBJECT_TYPE_USE_TBHV);
			row.render(dto.arrList.get(i), dto.arrLevelCode,
					dto.progressStandarPercent, widthTVLevel);
			// pos++;
			row.setListener(this);
			listRows.add(row);
		}

		TBHVDisProComProgReportRow row = new TBHVDisProComProgReportRow(parent,
				true, TBHVDisProComProgReportRow.OBJECT_TYPE_USE_TBHV);
		row.renderSum(dto, dto.progressStandarPercent, widthTVLevel);
		listRows.add(row);
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
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				if (this.dto != null
						&& this.dto.listDisplayProgrameInfo != null
						&& this.dto.listDisplayProgrameInfo.size() > currentSelected) {
					isRefreshRequest = true;
					getListListReport(
							this.dto.listDisplayProgrameInfo
									.get(currentSelected).displayProgramCode,
							0, true);
				} else {
					isRefreshRequest = true;
					getListListReport(Constants.STR_BLANK, 0, true);
				}
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	/**
	 * handle event for table get more
	 */
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub
		if (control == tbList) {
			currentPage = tbList.getPagingControl().getCurrentPage();
			getListListReport(
					this.dto.listDisplayProgrameInfo.get(currentSelected).displayProgramCode,
					currentPage - 1, false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.listener.VinamilkTableListener#
	 * handleVinamilkTableRowEvent(int, android.view.View, java.lang.Object)
	 */
	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		if (action == TBHVDisProComProgReportRow.ACTION_CLICK_NPP_CODE) {
			TBHVDisProComProgReportItem item = (TBHVDisProComProgReportItem) data;
			this.gotoTBHVNPPDisProProgressReport(item, false);
		} else if (action == TBHVDisProComProgReportRow.ACTION_CLICK_GSNPP_NAME) {
			TBHVDisProComProgReportItem item = (TBHVDisProComProgReportItem) data;
			this.gotoTBHVNPPDisProProgressReport(item, true);
		}

	}

	/**
	 * 
	 * go to tbhv npp display programe progress report view
	 * 
	 * @param item
	 * @param strGsnppCode
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 30, 2012
	 */
	public void gotoTBHVNPPDisProProgressReport(
			TBHVDisProComProgReportItem item, boolean isSelected) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(
				IntentConstants.INTENT_TBHV_DISPROCOM_PROG_REPORT_ITEM, item);
		bundle.putBoolean(IntentConstants.INTENT_IS_OR, isSelected);
		bundle.putInt(IntentConstants.INTENT_PERCENT_PROGRESS,
				this.dto.progressStandarPercent);

		ActionEvent actionEvent = new ActionEvent();
		actionEvent.viewData = bundle;
		actionEvent.action = ActionEventConstant.GET_TBHV_NPP_DIS_PRO_COM_PROG_REPORT;
		actionEvent.sender = this;
		TBHVController.getInstance().handleSwitchFragment(actionEvent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android
	 * .widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (currentSelected != arg2) {
			currentSelected = arg2;
			this.renderDisplayProgrameInfo();
			isRefreshRequest = true;
			this.currentPage = 0;
			// get list vote display product
			if (this.dto.listDisplayProgrameInfo != null
					&& this.dto.listDisplayProgrameInfo.size() > this.currentSelected) {
				this.getListListReport(
						this.dto.listDisplayProgrameInfo.get(currentSelected).displayProgramCode,
						this.currentPage, true);
			}
		}
	}

	/**
	 * 
	 * render display programe info
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 30, 2012
	 */
	public void renderDisplayProgrameInfo() {
		if (this.dto.listDisplayProgrameInfo != null
				&& this.dto.listDisplayProgrameInfo.size() > this.currentSelected) {
			tvDisProName.setText(this.dto.listDisplayProgrameInfo
					.get(currentSelected).displayProgramName);
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
