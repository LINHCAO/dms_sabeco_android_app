/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TableRow;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.db.ShopDTO;
import com.viettel.dms.dto.view.ListStaffDTO;
import com.viettel.dms.dto.view.ListStaffDTO.StaffItem;
import com.viettel.dms.dto.view.TBHVProgressDateReportDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.TBHVActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;
/**
 * 
 *  man hinh chi tiet bao cao ngay cua TBHV
 *  @author: Nguyen Huu Hieu
 *  @version: 1.1
 *  @since: 1.0
 */
public class TBHVReportProgressDateDetailView extends BaseFragment implements
		OnItemSelectedListener, OnEventControlListener, OnClickListener,
		VinamilkTableListener {

	private static final int ACTION_MENU_DATE = 5;//bao cao ngay
	private static final int ACTION_MENU_MONTH = 4;//luy ke thang
	private static final int ACTION_MENU_CTTB = 3;//chuong trinh trung bay
	private static final int ACTION_MENU_PSDS = 2;//khach hang chua psds
	private static final int ACTION_MENU_MHTT = 1;//mat hang trong tam
	private static final int ACTION_MNV_CLICK = 6;//row clicked
	// main activity
	public static final String TAG = TBHVReportProgressDateDetailView.class
			.getName();
	private TBHVActivity parent;	
	private VinamilkTableView tbList;//table hien thi
	private Spinner spMaNPP;
	private String[] arrMaNPP;
	private ArrayList<ShopDTO> dtoNPP;
	private Spinner spGiamSatNPP;
	private String[] arrGiamSatNPP;
	private ListStaffDTO dtoGiamSatNPP;
	private int indexSpMaNPP = 0, indexSpGiamSatNPP = 0;
	private TBHVProgressDateReportDTO dto;//dto cua tbList
	private boolean isFirst = true;
	private String tenNPP;	
	private String tenGSNPP;	

	public static TBHVReportProgressDateDetailView getInstance(Bundle b) {
		TBHVReportProgressDateDetailView instance = new TBHVReportProgressDateDetailView();
		instance.setArguments(b);
		return instance;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_tbhv_report_progress_date_detail, container,
				false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(initTitle());

		Bundle data = (Bundle) getArguments();
		tenNPP = data.getString(IntentConstants.INTENT_SHOP_NAME);		
		tenGSNPP = data.getString(IntentConstants.INTENT_STAFF_NAME);
		
		initView(view);
		if (isFirst) {
			getListNPP();
		}
		return v;
	}
	
	/**
	 * init title view
	 * 
	 * @author : HieuNH
	 */
	private SpannableObject initTitle() {		
		String dateTime = new SimpleDateFormat(" dd/MM/yyyy").format(new Date());

		SpannableObject title = new SpannableObject();
		title.addSpan(getString(R.string.TITLE_TBHV_REPORT_PROGRESS_DAY_DETAIL),
				ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.NORMAL);
		title.addSpan(dateTime, ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		return title;
	}

	private void initView(View view) {
		// enable menu bar
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_PSDS),
				R.drawable.icon_order, ACTION_MENU_PSDS);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_CTTB),
				R.drawable.icon_reminders, ACTION_MENU_CTTB);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_MHTT),
				R.drawable.icon_list_star, ACTION_MENU_MHTT);
		addMenuItem(
				StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_MONTH),
				R.drawable.icon_accumulated, ACTION_MENU_MONTH);
		addMenuItem(
				StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_DATE),
				R.drawable.icon_calendar, ACTION_MENU_DATE);
		setMenuItemFocus(5);
		
		tbList = (VinamilkTableView) view.findViewById(R.id.tbCusList);
		tbList.setListener(this);
		spMaNPP = (Spinner) view.findViewById(R.id.spMaNPP);
		spMaNPP.setOnItemSelectedListener(this);
		spGiamSatNPP = (Spinner) view.findViewById(R.id.spGiamSatNPP);
		spGiamSatNPP.setOnItemSelectedListener(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (!isFirst) {
			renderLayout();
		}
		super.onResume();
	}

	/**
	 * HieuNH lay danh sach NVGS cua NPP
	 */
	private void getListNVGS() {
		if(!this.parent.isShowProgressDialog()){
			this.parent.showProgressDialog(getString(R.string.loading));
		}
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		if(dtoNPP.size() > indexSpMaNPP){
		b.putString(IntentConstants.INTENT_SHOP_ID,
				"" + dtoNPP.get(indexSpMaNPP).shopId);
		}else{
			b.putString(IntentConstants.INTENT_SHOP_ID,	"");
		}
		e.viewData = b;
		e.action = ActionEventConstant.GET_LIST_NVGS_OF_DATE;
		e.sender = this;
		TBHVController.getInstance().handleViewEvent(e);
	}

	/**
	 * HieuNH khoi tao view list NVGS
	 */
	private void initSpGiamSatNPP() {		
		arrGiamSatNPP = new String[dtoGiamSatNPP.arrList.size()];
		if (dtoGiamSatNPP.arrList.size() > 1) {
			arrGiamSatNPP[0] = "Tất cả";
			for (int i = 1, size = dtoGiamSatNPP.arrList.size(); i < size; i++) {
				arrGiamSatNPP[i] = dtoGiamSatNPP.arrList.get(i).code + " - " + dtoGiamSatNPP.arrList.get(i).name;
				if(tenGSNPP.equals(dtoGiamSatNPP.arrList.get(i).name)){
					indexSpGiamSatNPP = i;
					tenGSNPP = "";//chi khoi tao combobox 1 lan dau tien thoi khi di tu man hinh bao cao ngay qua chi tiet
				}
			}
		} else {
			for (int i = 0, size = dtoGiamSatNPP.arrList.size(); i < size; i++) {
				arrGiamSatNPP[i] = dtoGiamSatNPP.arrList.get(i).code + " - " + dtoGiamSatNPP.arrList.get(i).name;
			}
		}
		SpinnerAdapter adapterGiamSatNPP = new SpinnerAdapter(parent,
				R.layout.simple_spinner_item, arrGiamSatNPP);
		spGiamSatNPP.setAdapter(adapterGiamSatNPP);
		spGiamSatNPP.setSelection(indexSpGiamSatNPP);
	}

	/**
	 * HieuNH lay danh sach NPP cua TBHV
	 */
	private void getListNPP() {
		if(!this.parent.isShowProgressDialog()){
			this.parent.showProgressDialog(getString(R.string.loading));
		}
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_SHOP_ID, String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId));
//		b.putString(IntentConstants.INTENT_PARENT_SHOP_ID, "125");
		e.viewData = b;
		e.action = ActionEventConstant.GET_LIST_NPP;
		e.sender = this;
		TBHVController.getInstance().handleViewEvent(e);
	}

	/**
	 * HieuNH khoi tao view list NVGS
	 */
	private void initSpNPP() {				
		arrMaNPP = new String[dtoNPP.size()];
		for (int i = 0, size = dtoNPP.size(); i < size; i++) {
			arrMaNPP[i] = dtoNPP.get(i).shopCode;// + " - " + dtoNPP.arrList.get(i).name;
			if(tenNPP.equals(dtoNPP.get(i).shopName)){
				indexSpMaNPP = i;
				tenNPP = "";//chi khoi tao 1 lan dau tien
			}
		}
		SpinnerAdapter adapterNPP = new SpinnerAdapter(parent,
				R.layout.simple_spinner_item, arrMaNPP);
		spMaNPP.setAdapter(adapterNPP);
		spMaNPP.setSelection(indexSpMaNPP);
	}

	/**
	 * HieuNH lay danh sach bao cao trong ngay
	 */
	private void getReport() {
		if(!this.parent.isShowProgressDialog()){
			this.parent.showProgressDialog(getString(R.string.loading));
		}
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		if (dtoGiamSatNPP.arrList.size() > 0) {
			b.putString(
					IntentConstants.INTENT_STAFF_OWNER_ID,
					((StaffItem) dtoGiamSatNPP.arrList.get(indexSpGiamSatNPP)).id);
		} else {
			b.putString(IntentConstants.INTENT_STAFF_OWNER_ID, "");
		}
		if(dtoNPP.size() > indexSpMaNPP){
		b.putString(IntentConstants.INTENT_SHOP_ID,
				"" + dtoNPP.get(indexSpMaNPP).shopId);
		}else{
			b.putString(IntentConstants.INTENT_SHOP_ID,	"");
		}
		e.viewData = b;
		e.action = ActionEventConstant.GET_TBHV_REPORT_PROGRESS_DATE_DETAIL;
		e.sender = this;
		TBHVController.getInstance().handleViewEvent(e);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		if (arg0 == spMaNPP && arg0.getSelectedItemPosition() != indexSpMaNPP) {
			indexSpMaNPP = arg0.getSelectedItemPosition();
			indexSpGiamSatNPP = 0;
			getListNVGS();
		} else if (arg0 == spGiamSatNPP
				&& arg0.getSelectedItemPosition() != indexSpGiamSatNPP) {
			indexSpGiamSatNPP = arg0.getSelectedItemPosition();
			getReport();
		}
	}

	/**
	 * HieuNH tao view sau khi request xong
	 */

	private void renderLayout() {
		List<TableRow> listRows = new ArrayList<TableRow>();
		TBHVProgressDateReportRow row;
		Double keHoach = 0.0, thucHien = 0.0, conLai = 0.0;
		for (int i = 0, s = dto.arrList.size(); i < s; i++) {
			row = new TBHVProgressDateReportRow(parent, ACTION_MNV_CLICK, this, 1);			
			row.render(dto.arrList.get(i));
			listRows.add(row);
			keHoach += dto.arrList.get(i).keHoach;
			thucHien += dto.arrList.get(i).thucHien;
		}
		conLai = keHoach - thucHien > 0 ? keHoach - thucHien : 0;
		TBHVProgressDateReportAllRow row1 = new TBHVProgressDateReportAllRow(parent);
		row1.render("Tổng cộng", keHoach, thucHien, conLai);
		listRows.add(row1);
		tbList.addContent(listRows);
		this.parent.closeProgressDialog();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_TBHV_REPORT_PROGRESS_DATE_DETAIL:
			dto = (TBHVProgressDateReportDTO) modelEvent.getModelData();
			renderLayout();
			break;
		case ActionEventConstant.GET_LIST_NPP:
			dtoNPP = (ArrayList<ShopDTO>) modelEvent.getModelData();
			initSpNPP();
			getListNVGS();
			break;
		case ActionEventConstant.GET_LIST_NVGS_OF_DATE:
			dtoGiamSatNPP = (ListStaffDTO) modelEvent.getModelData();
			initSpGiamSatNPP();
			getReport();
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}
	
	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		super.handleErrorModelViewEvent(modelEvent);
		parent.closeProgressDialog();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			parent = (TBHVActivity) activity;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		// TODO Auto-generated method stub

	}

	/**
	 * xu ly cac action
	 */

	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub

		ActionEvent e = new ActionEvent();
		switch (eventType) {
		case ACTION_MENU_DATE: 			
			e.sender = this;			
			e.action = ActionEventConstant.ACTION_TBHV_REPORT_PROGRESS_DATE;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		case ACTION_MENU_MONTH:
			e.action = ActionEventConstant.GOTO_TBHV_REPORT_PROGRESS_MONTH;
			e.sender = this;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		case ACTION_MENU_CTTB: {
			e.action = ActionEventConstant.GET_TBHV_DIS_PRO_COM_PROG_REPORT;
			e.sender = this;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		}
		case ACTION_MENU_PSDS: {
			e.action = ActionEventConstant.GO_TO_TBHV_REPORT_CUSTOMER_NOT_PSDS;
			e.sender = this;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		}
		case ACTION_MENU_MHTT:
			e.sender = this;
			e.viewData = new Bundle();
			e.action = ActionEventConstant.GOTO_TBHV_REPORT_PROGRESS_SALES_FORCUS;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		default:
			break;
		}

	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				getListNPP();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
