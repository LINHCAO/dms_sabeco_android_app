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
import android.widget.TableRow;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.view.TBHVProgressDateReportDTO;
import com.viettel.dms.dto.view.TBHVProgressDateReportDTO.TBHVProgressDateReportItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.TBHVActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;
/**
 * 
 * Man hinh bao cao ngay cua TBHV
 * @author: HieuNH6
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVReportProgressDateView extends BaseFragment implements
		OnEventControlListener, OnClickListener, VinamilkTableListener {

	private static final int ACTION_MENU_DATE = 5;//bao cao ngay
	private static final int ACTION_MENU_MONTH = 4;//luy ke thang
	private static final int ACTION_MENU_CTTB = 3;//chuong trinh trung bay
	private static final int ACTION_MENU_PSDS = 2;//khach hang chua psds
	private static final int ACTION_MENU_MHTT = 1;//mat hang trong tam
	private static final int ACTION_MNV_CLICK = 6;//row clicked
	// main activity
	public static final String TAG = TBHVReportProgressDateView.class.getName();
	private TBHVActivity parent;		
	private VinamilkTableView tbList;//hien thi table	
	private TBHVProgressDateReportDTO dto;//dto cua table tbList
	private boolean isFirst = true;

	public static TBHVReportProgressDateView getInstance(Bundle b) {
		TBHVReportProgressDateView instance = new TBHVReportProgressDateView();
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
		//Debug.startMethodTracing("Vinamilk");
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_tbhv_report_progress_date, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(initTitle());

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
//		Debug.stopMethodTracing();
		if (isFirst) {
			getReport();
		}
		//Debug.stopMethodTracing();
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
		title.addSpan(getString(R.string.TITLE_TBHV_REPORT_PROGRESS_DAY),
				ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.NORMAL);
		title.addSpan(dateTime, ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		return title;
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
	 * HieuNH lay danh sach bao cao trong ngay
	 */
	private void getReport() {
		this.parent.showProgressDialog(getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_SHOP_ID,	 String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId));
//		b.putString(IntentConstants.INTENT_STAFF_OWNER_ID, "125");
		e.viewData = b;
		e.action = ActionEventConstant.GET_TBHV_REPORT_PROGRESS_DATE;
		e.sender = this;
		TBHVController.getInstance().handleViewEvent(e);
	}

	/**
	 * HieuNH tao view sau khi request xong
	 */

	private void renderLayout() {
		List<TableRow> listRows = new ArrayList<TableRow>();
		TBHVProgressDateReportRow row;
		Double keHoach = 0.0, thucHien = 0.0, conLai = 0.0;
		for (int i = 0, s = dto.arrList.size(); i < s; i++) {
			row = new TBHVProgressDateReportRow(parent, ACTION_MNV_CLICK, this, 0);
			row.render(dto.arrList.get(i));
			listRows.add(row);
			keHoach += dto.arrList.get(i).keHoach;
			thucHien += dto.arrList.get(i).thucHien;
		}
		conLai = keHoach - thucHien;
		if(conLai < 0){
			conLai = 0.0;
		}
		TBHVProgressDateReportAllRow row1 = new TBHVProgressDateReportAllRow(
				parent);
		row1.render(StringUtil.getString(R.string.TEXT_HEADER_TABLE_SUM),
				keHoach, thucHien, conLai);
		listRows.add(row1);
		tbList.addContent(listRows);
		this.parent.closeProgressDialog();
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_TBHV_REPORT_PROGRESS_DATE:
			dto = (TBHVProgressDateReportDTO) modelEvent.getModelData();
			renderLayout();
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
		case ACTION_MNV_CLICK:
			TBHVProgressDateReportItem item = (TBHVProgressDateReportItem) data;
			Bundle b = new Bundle();
			b.putString(IntentConstants.INTENT_SHOP_NAME, item.tenNPP);			
			b.putString(IntentConstants.INTENT_STAFF_NAME, item.tenGSNPP);			
			e.action = ActionEventConstant.GO_TO_TBHV_REPORT_PROGRESS_DATE_DETAIL;
			e.sender = this;
			e.viewData = b;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;		
		case ACTION_MENU_MONTH:
			e.action = ActionEventConstant.GOTO_TBHV_REPORT_PROGRESS_MONTH;
			e.sender = this;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		case ACTION_MENU_CTTB: {
			e.action = ActionEventConstant.GET_TBHV_DIS_PRO_COM_PROG_REPORT;
//			e.action = ActionEventConstant.GET_TBHV_PROG_REPOST_PRO_DISP_DETAIL_SALE;
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
				getReport();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

}
