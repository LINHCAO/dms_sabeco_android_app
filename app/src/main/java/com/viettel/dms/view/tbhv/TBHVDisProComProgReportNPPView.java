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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.view.TBHVDisProComProgReportItem;
import com.viettel.dms.dto.view.TBHVDisProComProgReportNPPDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
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
 * 01-04-01. Tiến độ CTTB theo NPP ngay (TBHV)
 * 
 * @author: HoanPD1
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVDisProComProgReportNPPView extends BaseFragment implements OnEventControlListener,
		VinamilkTableListener, OnItemSelectedListener {

	private static final int ACTION_DATE = 1;// Bao cao nagy
	private static final int ACTION_ACC = 2; // Bao cao luy ke thang
	private static final int ACTION_CTTB = 3;// Chuong trinh TB
	private static final int ACTION_PSDS = 4;// khach hang chua PSDS
	private static final int ACTION_MHTT = 5;// Bao cao mat hang trong tam

	private GlobalBaseActivity parent;
	private VinamilkTableView tbList;
	private TBHVDisProComProgReportNPPDTO dto;
	// sp gsnpp 
	Spinner spCodeGSNPP;
	// sp display programe code
	Spinner spCodeDisPro;
	// ty le chuan
	TextView tvStandardRate;
	// code NPP
	TextView tvCodeNPP;
	// linner layout level
	LinearLayout llLevel;
	// last item selected prev screen
	TBHVDisProComProgReportItem lastObjectSelected;
	// check is selected GSNPP in prev screen
	boolean isSelectedGSNPP = false;
	// flag check load the first
	boolean isLoadedData = false;
	// check has item all
	boolean isHasItemAll = false;
	// flag currnect selected gsnpp
	int currentSelectedGSNPP = 0;
	// flag current selected display programe
	int currentSelectedDisPro = 0;
	// layout for header on VNM table
	LinearLayout llContentHeader;
	// percent not psds alow
	int progressNotPSDSAlow = 0;
	TextView tvTotal;

	public static final String TAG = TBHVDisProComProgReportNPPView.class.getName();

	public static TBHVDisProComProgReportNPPView getInstance(Bundle data) {
		TBHVDisProComProgReportNPPView f = new TBHVDisProComProgReportNPPView();
		// Supply index input as an argument.
		f.setArguments(data);
		return f;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (GlobalBaseActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_tbhv_staff_dis_pro_com_prog_report, container,
				false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		this.setTitleForScreen();
		// enable menu bar
		this.initMenuItem();

		this.initView(v);
		// lay du lieu truyen qua
		Bundle bundle = getArguments();
		if (bundle.getSerializable(IntentConstants.INTENT_TBHV_DISPROCOM_PROG_REPORT_ITEM) != null) {
			this.lastObjectSelected = (TBHVDisProComProgReportItem) bundle
					.getSerializable(IntentConstants.INTENT_TBHV_DISPROCOM_PROG_REPORT_ITEM);
		}
		if (bundle.getSerializable(IntentConstants.INTENT_PERCENT_PROGRESS) != null) {
			this.progressNotPSDSAlow = bundle.getInt(IntentConstants.INTENT_PERCENT_PROGRESS);
			tvStandardRate.setText(Integer.toString(progressNotPSDSAlow)+StringUtil.getString(R.string.TEXT_PERCENT));
		}
		this.isSelectedGSNPP = bundle.getBoolean(IntentConstants.INTENT_IS_OR);
		if (this.lastObjectSelected != null) {	
			tvCodeNPP.setText(this.lastObjectSelected.codeNPP);
			if (!this.isLoadedData) {
				getListGSNPPAndListCTTBAndListNVBH();
			}
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
		if (this.isLoadedData) {
			this.llContentHeader.setVisibility(View.VISIBLE);
			this.renderListGSNPP();
			this.renderListCTTBOfNPP();
			this.renderLayout();
		}
		super.onResume();
	}

	/**
	 * 
	 * init view control for screen
	 * 
	 * @param v
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 31, 2012
	 */
	public void initView(View v) {
		llContentHeader = (LinearLayout) v.findViewById(R.id.llContentHeader);
		llContentHeader.setVisibility(View.INVISIBLE);
		tvCodeNPP = (TextView) v.findViewById(R.id.tvCodeNPP);
		tvStandardRate = (TextView) v.findViewById(R.id.tvStandardRate);
		tvTotal = (TextView) v.findViewById(R.id.tvTotal);
		spCodeDisPro = (Spinner) v.findViewById(R.id.spCodeDisPro);
		spCodeDisPro.setOnItemSelectedListener(this);
		spCodeGSNPP = (Spinner) v.findViewById(R.id.spCodeGSNPP);
		spCodeGSNPP.setOnItemSelectedListener(this);
		llLevel = (LinearLayout) v.findViewById(R.id.llLevel);

		tbList = (VinamilkTableView) v.findViewById(R.id.tbCusList);
		tbList.setListener(this);
	}

	/**
	 * 
	 * init menu item
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 31, 2012
	 */
	public void initMenuItem() {
		enableMenuBar(this);
		addMenuItem(getString(R.string.TEXT_HEADER_MENU_PSDS), R.drawable.icon_order, ACTION_PSDS);
		addMenuItem(getString(R.string.TEXT_HEADER_MENU_CTTB), R.drawable.icon_reminders, ACTION_CTTB);
		addMenuItem(getString(R.string.TEXT_HEADER_MENU_MHTT), R.drawable.icon_list_star, ACTION_MHTT);
		addMenuItem(getString(R.string.TEXT_HEADER_MENU_REPORT_MONTH), R.drawable.icon_accumulated, ACTION_ACC);
		addMenuItem(getString(R.string.TEXT_HEADER_MENU_REPORT_DATE), R.drawable.icon_calendar, ACTION_DATE);
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
		obj.addSpan(getString(R.string.TITLE_VIEW_TBHV_NPP_DIS_PRO_COM_PROG_REPORT) + Constants.STR_SPACE,
				ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
		obj.addSpan(DateUtils.defaultDateFormat.format(new Date()), ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		setTitleHeaderView(obj);
	}

	/**
	 * get data
	 * 
	 * @author: HoanPD1
	 * @return: void
	 * @throws:
	 */
	private void getListGSNPPAndListCTTBAndListNVBH() {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));

		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		if (this.isLoadedData) {
			b.putString(
					IntentConstants.INTENT_TBHV_CTTB_CODE,
					this.dto.listDisplayProgrameInfo.get(this.spCodeDisPro.getSelectedItemPosition()).displayProgramCode);

			if (this.isHasItemAll) {
				if (this.currentSelectedGSNPP != 0) {
					b.putString(IntentConstants.INTENT_STAFF_ID, String.valueOf(this.dto.listGSNPPInfo
							.get(this.spCodeGSNPP.getSelectedItemPosition() - 1).staffId));
				}
			} else {
				b.putString(IntentConstants.INTENT_STAFF_ID,
						String.valueOf(this.dto.listGSNPPInfo.get(this.spCodeGSNPP.getSelectedItemPosition()).staffId));
			}

		} else {
			b.putString(IntentConstants.INTENT_TBHV_CTTB_CODE, this.lastObjectSelected.programShortCode);
			if (this.isSelectedGSNPP) {
				b.putString(IntentConstants.INTENT_STAFF_ID, this.lastObjectSelected.idGSNPP);
			}
		}
		b.putBoolean(IntentConstants.INTENT_IS_OR, !this.isLoadedData);
		b.putString(IntentConstants.INTENT_SHOP_ID, this.lastObjectSelected.idNPP);
		e.viewData = b;
		e.action = ActionEventConstant.GET_TBHV_NPP_DIS_PRO_COM_PROG_REPORT;
		e.sender = this;
		TBHVController.getInstance().handleViewEvent(e);
	}

	/**
	 * 
	 * render list GSNPP
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 31, 2012
	 */
	public void renderListGSNPP() {
		ArrayList<String> listParams = new ArrayList<String>();
		if (this.dto.listGSNPPInfo.size() > 1) {
			isHasItemAll = true;
			listParams.add(StringUtil.getString(R.string.ALL));
		}
		int index = 0;
		for (int i = 0, size = this.dto.listGSNPPInfo.size(); i < size; i++) {
			if (this.dto.listGSNPPInfo.get(i).staffCode.equals(this.lastObjectSelected.codeGSNPP)) {
				index = i;
			}
			 listParams.add(this.dto.listGSNPPInfo.get(i).staffCode + " - " +
			 this.dto.listGSNPPInfo.get(i).staffName);
//			listParams.add(this.lastObjectSelected.codeNPP + " - " + this.dto.listGSNPPInfo.get(i).staffName);
//			listParams.add(this.lastObjectSelected.codeGSNPP + " - " + this.lastObjectSelected.nameGSNPP);
		}
		String[] arrPromotion = listParams.toArray(new String[listParams.size()]);
		SpinnerAdapter adapterLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrPromotion);
		this.spCodeGSNPP.setAdapter(adapterLine);
		if (this.isSelectedGSNPP) {
			if (isHasItemAll) {
				this.currentSelectedGSNPP = index + 1;
				this.spCodeGSNPP.setSelection(index + 1);
			} else {
				this.currentSelectedGSNPP = index;
				this.spCodeGSNPP.setSelection(index);
			}
			// set for current to don't request two time
		} else {
			this.spCodeGSNPP.setSelection(this.currentSelectedGSNPP);
		}
	}

	/**
	 * 
	 * render list CTTB of NPP
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 31, 2012
	 */
	public void renderListCTTBOfNPP() {
		String[] arrPromotion = new String[this.dto.listDisplayProgrameInfo.size()];
		int index = 0;
		for (int i = 0, size = this.dto.listDisplayProgrameInfo.size(); i < size; i++) {
			if (this.lastObjectSelected.programShortCode
					.equals(this.dto.listDisplayProgrameInfo.get(i).displayProgramCode)) {
				index = i;
			}
			arrPromotion[i] = this.dto.listDisplayProgrameInfo.get(i).displayProgramCode;
		}
		SpinnerAdapter adapterLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrPromotion);
		this.currentSelectedDisPro = index;
		// this.isFirstLoadProduct = true;
		this.spCodeDisPro.setAdapter(adapterLine);
		this.spCodeDisPro.setSelection(index);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_TBHV_NPP_DIS_PRO_COM_PROG_REPORT:
			parent.closeProgressDialog();
			TBHVDisProComProgReportNPPDTO tmp = (TBHVDisProComProgReportNPPDTO) modelEvent.getModelData();
			if (!isLoadedData) {
				dto = tmp;
			} else {
				dto.arrLevelCode = tmp.arrLevelCode;
				dto.arrList = tmp.arrList;
				dto.arrResultTotal = tmp.arrResultTotal;
				dto.dtoResultTotal = tmp.dtoResultTotal;
			}
			if (dto != null) {
				// cap nhat lai danh sach bang cach gom cac level cua cung mot
				// nvbh lai voi nhau
				this.updateListItemInData();

				this.llContentHeader.setVisibility(View.VISIBLE);
				if (!isLoadedData) {
					this.renderListGSNPP();
					this.renderListCTTBOfNPP();
				}
				renderLayout();
				this.isLoadedData = true;
			}
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
					if (newItem.idNPP.equals(idNPP) && newItem.idGSNPP.equals(idGSNPP)) {
						for (int k = 0, size3 = newItem.arrLevelCode.size(); k < size3; k++) {
							newItem.arrLevelCode.get(k).resultNumber += item.arrLevelCode.get(k).resultNumber;
							newItem.arrLevelCode.get(k).joinNumber += item.arrLevelCode.get(k).joinNumber;
						}
						newItem.itemResultTotal.joinNumber += item.itemResultTotal.joinNumber;
						newItem.itemResultTotal.resultNumber += item.itemResultTotal.resultNumber;
					}
				}
			}
		}

		this.dto.arrList = newList;
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_TBHV_NPP_DIS_PRO_COM_PROG_REPORT:
			parent.closeProgressDialog();
			break;
		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	}

	/**
	 * renderLayout
	 * 
	 * @author: HoanPD1
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {

		// TODO Auto-generated method stub
		llLevel.removeAllViews();
		// ren level
		String[] listCharTitle = new String[] { "A", "B", "C", "D", "E", "F",
				"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "W",
				"V", "Y", "Z" };
		int widthTVLevel = dto.arrLevelCode.size() <= 5 ? 380 / (dto.arrLevelCode
				.size() + 1) : 380 / 6;
		for (int i = 0; i < dto.arrLevelCode.size(); i++) {
			DisProComProgReportLevelTextView tv = new DisProComProgReportLevelTextView(parent);
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

		List<TableRow> listRows = new ArrayList<TableRow>();
		for (int i = 0, s = dto.arrList.size(); i < s; i++) {
			TBHVDisProComProgReportRow row = new TBHVDisProComProgReportRow(parent, false,
					TBHVDisProComProgReportRow.OBJECT_TYPE_USE_NPP);
			row.renderRowForCTTBNPP(dto.arrList.get(i), dto.arrLevelCode, this.progressNotPSDSAlow,widthTVLevel);
			row.setListener(this);
			listRows.add(row);
		}

		TBHVDisProComProgReportRow row = new TBHVDisProComProgReportRow(parent, true,
				TBHVDisProComProgReportRow.OBJECT_TYPE_USE_NPP);
		row.renderSum(dto, this.progressNotPSDSAlow,widthTVLevel);
		listRows.add(row);
		tbList.addContent(listRows);
	}

	@Override
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
				e.viewData = new Bundle();
				TBHVController.getInstance().handleSwitchFragment(e);

				break;
			default:
				break;
			}
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.listener.VinamilkTableListener#
	 * handleVinamilkTableRowEvent(int, android.view.View, java.lang.Object)
	 */
	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		if (action == TBHVDisProComProgReportRow.ACTION_CLICK_NPP_CODE) {
			this.gotoTBHVProgReportDispDetailSale((TBHVDisProComProgReportItem) data);
		}
	}

	/**
	 * 
	 * display screen cttb of nvbh
	 * 
	 * @param item
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 31, 2012
	 */
	public void gotoTBHVProgReportDispDetailSale(TBHVDisProComProgReportItem item) {
		Bundle bundle = new Bundle();
		// ma npp
		bundle.putString(IntentConstants.INTENT_SHOP_CODE, this.lastObjectSelected.codeNPP);
		bundle.putString(IntentConstants.INTENT_SHOP_NAME, this.lastObjectSelected.nameNPP);
		// ma gsnpp
		bundle.putString(IntentConstants.INTENT_STAFF_OWNER_CODE, item.codeGSNPP);
		bundle.putString(IntentConstants.INTENT_STAFF_OWNER_NAME, item.nameGSNPP);

		// if (this.isHasItemAll) {
		// bundle.putString(IntentConstants.INTENT_STAFF_OWNER_CODE,
		// this.dto.listGSNPPInfo.get(this.spCodeGSNPP
		// .getSelectedItemPosition() - 1).staffCode);
		// bundle.putString(IntentConstants.INTENT_STAFF_OWNER_NAME,
		// this.dto.listGSNPPInfo.get(this.spCodeGSNPP
		// .getSelectedItemPosition() - 1).staffName);
		// } else {
		// bundle.putString(IntentConstants.INTENT_STAFF_OWNER_CODE,
		// this.dto.listGSNPPInfo.get(this.spCodeGSNPP
		// .getSelectedItemPosition()).staffCode);
		// bundle.putString(IntentConstants.INTENT_STAFF_OWNER_NAME,
		// this.dto.listGSNPPInfo.get(this.spCodeGSNPP
		// .getSelectedItemPosition()).staffName);
		// }

		// ma nvbh
		bundle.putString(IntentConstants.INTENT_STAFF_CODE, item.codeNPP);
		// ten nvbh
		bundle.putString(IntentConstants.INTENT_STAFF_NAME, item.nameNPP);
		// id nvbh
		bundle.putString(IntentConstants.INTENT_STAFF_ID, item.idNPP);
		// danh sach chuong trinh trung bay cua npp
		bundle.putSerializable(IntentConstants.INTENT_TBHV_LIST_CTTB_NPP, this.dto.listDisplayProgrameInfo);
		// chuong trinh trung bay hien tai dang selected
		bundle.putInt(IntentConstants.INTENT_TBHV_CURRENT_CTTB_NPP_SELECTED, spCodeDisPro.getSelectedItemPosition());
		ActionEvent actionEvent = new ActionEvent();
		actionEvent.viewData = bundle;
		actionEvent.action = ActionEventConstant.GET_TBHV_PROG_REPOST_PRO_DISP_DETAIL_SALE;
		actionEvent.sender = this;
		TBHVController.getInstance().handleSwitchFragment(actionEvent);
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				getListGSNPPAndListCTTBAndListNVBH();
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
		// TODO Auto-generated method stub
		if (arg0 == this.spCodeGSNPP) {
			if (this.currentSelectedGSNPP != arg2) {
				this.currentSelectedGSNPP = arg2;
				this.getListGSNPPAndListCTTBAndListNVBH();
			}
		} else if (arg0 == this.spCodeDisPro) {
			if (this.currentSelectedDisPro != arg2) {
				this.currentSelectedDisPro = arg2;
//				this.tvStandardRate.setText(this.dto.listDisplayProgrameInfo.get(currentSelectedDisPro).displayProgramName);
				this.getListGSNPPAndListCTTBAndListNVBH();
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
