/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TableRow;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.view.TBHVRouteSupervisionDTO;
import com.viettel.dms.dto.view.TBHVRouteSupervisionDTO.THBVRouteSupervisionItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Giam sat lo trinh
 * 
 * @author : TamPQ
 * @version: 1.1
 * @since : 1.0
 */
public class TBHVRouteSupervisionView extends BaseFragment implements VinamilkTableListener, OnEventControlListener {

	private GlobalBaseActivity parent; // activity
	private VinamilkTableView tbList; // list table
	private Button btViewAll;// view all button
	private TBHVRouteSupervisionDTO dto;// dto
	private TBHVRouteSupervisionDTO wrongDto;// wrong nvgsnpp dto
	@SuppressWarnings("unused")
	private boolean isViewAllVisible;// is ViewAll button Visible
	private AlertDialog alertDialog;

	// show tab giam sat lo trinh
	private static final int MENU_STAFF_TAB = 0;
	// sho tab xem vi tri
	private static final int MENU_STAFF_POSITION = 1;
	private static final int MENU_ATTENDANCE = 2;
	private static final int MENU_PLAN_PROCESS = 3;
	public static final String TAG = TBHVRouteSupervisionView.class.getName();

	public static TBHVRouteSupervisionView newInstance() {
		TBHVRouteSupervisionView f = new TBHVRouteSupervisionView();
		Bundle args = new Bundle();
		f.setArguments(args);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_tbhv_route_supervision_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_TBHV_ROUTE_SUPERVISION_VIEW));

		tbList = (VinamilkTableView) view.findViewById(R.id.tbCusList);
		tbList.setListener(this);
		btViewAll = (Button) view.findViewById(R.id.btViewAll);
		btViewAll.setOnClickListener(this);
		btViewAll.setVisibility(View.GONE);

		initMenuActionBar();

		//Tam thoi chua thuc thi gi
		//parent.showDialog("Chức năng lộ trình sẽ được cập nhật vào phiên bản sắp tới.");
		tbList.setNoContentText(StringUtil.getString(R.string.TEXT_TBHV_ROUTE_NEXT_UPDATE));
		tbList.showNoContentRow();
		
		/*if (dto != null) {
			if (!isViewAllVisible) {
				btViewAll.setVisibility(View.GONE);
				renderLayout(dto);
			} else {
				renderLayout(wrongDto);
				btViewAll.setVisibility(View.VISIBLE);
			}

		} else {
			getListRouteSupervision();
		}*/

		return v;
	}

	/**
	 * Init menu action bar
	 * 
	 * @author : TamPQ since : 2:53:53 PM
	 */
	private void initMenuActionBar() {
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_STAFF_GOING_ONLINE), R.drawable.icon_task, MENU_PLAN_PROCESS);
		addMenuItem(StringUtil.getString(R.string.TEXT_STAFF_TIMEKEEPING), R.drawable.icon_clock, MENU_ATTENDANCE);
		addMenuItem(StringUtil.getString(R.string.TEXT_VIEW_POSITION), R.drawable.icon_map, MENU_STAFF_POSITION);
		//addMenuItem(StringUtil.getString(R.string.TEXT_MONITORING), R.drawable.menu_customer_icon, MENU_STAFF_TAB);
		setMenuItemFocus(4);
	}

	/**
	 * getListRouteSupervision
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	@SuppressWarnings("unused")
	private void getListRouteSupervision() {
		parent.showLoadingDialog();
		Date d = new Date(System.currentTimeMillis());
		Calendar.getInstance().setTime(d);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		Bundle bundle = new Bundle();
		bundle.putLong(IntentConstants.INTENT_STAFF_ID, GlobalInfo.getInstance().getProfile().getUserData().id);
		bundle.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		bundle.putInt(IntentConstants.INTENT_DAY_OF_WEEK, day);
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.TBHV_ROUTE_SUPERVISION;
		e.sender = this;
		e.viewData = bundle;
		TBHVController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.TBHV_ROUTE_SUPERVISION:
			dto = (TBHVRouteSupervisionDTO) modelEvent.getModelData();
			wrongDto = new TBHVRouteSupervisionDTO();
			for (int i = 0, s = dto.itemGSNPPList.size(); i < s; i++) {
				THBVRouteSupervisionItem item = dto.itemGSNPPList.get(i);
				if (item.numWrongPlan > 0) {
					wrongDto.itemGSNPPList.add(item);
				}
			}
			if (wrongDto.itemGSNPPList.size() > 0) {
				renderLayout(wrongDto);
				isViewAllVisible = true;
				btViewAll.setVisibility(View.VISIBLE);
			} else {
				renderLayout(dto);
				isViewAllVisible = false;
				btViewAll.setVisibility(View.GONE);
			}
			parent.closeProgressDialog();
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		super.handleErrorModelViewEvent(modelEvent);
	}

	/**
	 * renderLayout
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void renderLayout(TBHVRouteSupervisionDTO sDto) {
		tbList.getListRowView().clear();
		List<TableRow> listRows = new ArrayList<TableRow>();
		if (sDto == null || sDto.itemGSNPPList == null || sDto.itemGSNPPList.size() == 0) {
			TBHVRouteSupervisionRow row = new TBHVRouteSupervisionRow(parent, this);
			row.renderNoResult();
			listRows.add(row);
		} else {
			for (int i = 0, s = sDto.itemGSNPPList.size(); i < s; i++) {
				TBHVRouteSupervisionRow row = new TBHVRouteSupervisionRow(parent, this);
				row.render(sDto.itemGSNPPList.get(i));
				listRows.add(row);
			}
		}
		tbList.addContent(listRows);
	}

	/**
	 * showDialog
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void showDialog(THBVRouteSupervisionItem dto, int type) {
		if (alertDialog != null && alertDialog.isShowing()) {

		} else {
			Builder build = new AlertDialog.Builder(parent, R.style.CustomDialogTheme);

			TBHVRouteSaleListView view = new TBHVRouteSaleListView(parent, dto, this, type);
			build.setView(view.viewLayout);
			alertDialog = build.create();
			Window window = alertDialog.getWindow();
			window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
			window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
			alertDialog.show();
		}
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		case TBHVRouteSaleListView.ACTION_CLOSE_DIALOG:
			if (alertDialog != null) {
				alertDialog.dismiss();
			}
			break;
		case MENU_STAFF_TAB:
			break;
		case MENU_STAFF_POSITION: {
			Bundle b = new Bundle();
			b.putSerializable(IntentConstants.INTENT_TBHV_ROUTE_SUPERVISION, dto);
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.TBHV_SUPERVISE_GSNPP_POSITION;
			e.sender = this;
			e.viewData = b;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		}
		case MENU_PLAN_PROCESS: {
			Bundle b = new Bundle();
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.TBHV_VISIT_CUS_NOTIFICATION;
			e.sender = this;
			e.viewData = b;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		}
		case MENU_ATTENDANCE: {
			Bundle b = new Bundle();
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.TBHV_ATTENDANCE;
			e.sender = this;
			e.viewData = b;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {

	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		if (action == ActionEventConstant.TBHV_ROUTE_SUPERVISION_MAP) {
			THBVRouteSupervisionItem item = (THBVRouteSupervisionItem) data;
			Bundle b = new Bundle();
			b.putSerializable(IntentConstants.INTENT_STAFF_DTO, item);
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.TBHV_ROUTE_SUPERVISION_MAP;
			e.sender = this;
			e.viewData = b;
			TBHVController.getInstance().handleSwitchFragment(e);
		} else if (action == ActionEventConstant.TBHV_ROUTE_SUPERVISION_TRAINING_CUS) {
			THBVRouteSupervisionItem item = (THBVRouteSupervisionItem) data;
			if (item.gsnppRealSeq.size() > 0) {
				showDialog(item, ActionEventConstant.TBHV_ROUTE_SUPERVISION_TRAINING_CUS);
			}
		} else if (action == ActionEventConstant.TBHV_ROUTE_SUPERVISION_VISITED_CUS) {
			THBVRouteSupervisionItem item = (THBVRouteSupervisionItem) data;
			if (item.numGsnppVisitedStore > 0 || item.numNvbhVisitedStore > 0) {
				showDialog(item, ActionEventConstant.TBHV_ROUTE_SUPERVISION_VISITED_CUS);
			}
		} else if (action == ActionEventConstant.TBHV_ROUTE_SUPERVISION_WRONG_CUS) {
			THBVRouteSupervisionItem item = (THBVRouteSupervisionItem) data;
			if (item.numWrongPlan > 0) {
				showDialog(item, ActionEventConstant.TBHV_ROUTE_SUPERVISION_WRONG_CUS);
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btViewAll) {
			renderLayout(dto);
			isViewAllVisible = false;
			btViewAll.setVisibility(View.GONE);
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				// cau request du lieu man hinh
				//getListRouteSupervision();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

}
