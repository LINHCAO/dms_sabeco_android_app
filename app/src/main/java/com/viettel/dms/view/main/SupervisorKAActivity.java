/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.main;

import android.app.ActionBar.LayoutParams;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
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

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.AdminController;
import com.viettel.dms.dto.control.MenuItemDTO;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.admin.report.ReportView;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for supervisor KA
 *
 * @author : Tuanlt1989
 */
public class SupervisorKAActivity extends GlobalBaseActivity implements
		OnClickListener, OnItemClickListener {

	boolean isShowMenu = false;
	ImageView iconLeft;
	TextView tvTitleMenu;
	LinearLayout llMenu;// layout header icon

	ArrayList<MenuItemDTO> listMenu;
	ListView lvMenu;
	MenuAdapter menuAdapter;
	boolean isShowTextInMenu = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		if (GlobalInfo.getInstance().isDebugMode) {
			setTitleName(GlobalInfo.getInstance().getProfile().getUserData().userCode);
		} else {
			setTitleName(GlobalInfo.getInstance().getProfile().getUserData().userCode);
		}
		llMenuUpdate.setVisibility(View.VISIBLE);
		llMenuGPS.setVisibility(View.GONE);
		llShowHideMenu.setOnClickListener(this);
		llShowHideMenu.setClickable(true);
		iconLeft = (ImageView) llShowHideMenu.findViewById(R.id.ivLeftIcon);
		tvTitleMenu = (TextView) findViewById(R.id.tvTitleMenu);
		llMenu = (LinearLayout) findViewById(R.id.llMenu);
		llMenu.setOnClickListener(this);
		lvMenu = (ListView) findViewById(R.id.lvMenu);
		initMenu();
		showHideMenuText(isShowMenu);
	}

	/**
	 * Init menu
	 */
	private void initMenu() {
		listMenu = new ArrayList<>();
		MenuItemDTO item1 = new MenuItemDTO(
				StringUtil.getString(R.string.TEXT_MENU_REPORT),
				R.drawable.menu_report_icon);
		MenuItemDTO item2 = new MenuItemDTO(
				StringUtil.getString(R.string.TEXT_MANAGE),
				R.drawable.menu_customer_icon);
		MenuItemDTO item3 = new MenuItemDTO(
				StringUtil.getString(R.string.TEXT_NOTIFY),
				R.drawable.icon_customer_list);
		MenuItemDTO item4 = new MenuItemDTO(
				StringUtil.getString(R.string.TEXT_NOTIFY),
				R.drawable.menu_picture_icon);
		listMenu.add(item1);
		listMenu.add(item2);
		listMenu.add(item3);
		listMenu.add(item4);
		menuAdapter = new MenuAdapter(this, 0, listMenu);
		lvMenu.setAdapter(menuAdapter);
		lvMenu.setOnItemClickListener(this);
		listMenu.get(0).setSelected(true);
		lvMenu.setSelection(0);
		menuAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		if (v == llShowHideMenu || v == ivLogo) {
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
			case 0:// Bao cao
				fg = (BaseFragment) fm.findFragmentByTag(ReportView.TAG);
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
		processClickOnMenu(arg2);
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
		}
	}

	/**
	 * Show cac man hinh detail
	 * 
	 * @author : BangHN since : 4:42:12 PM
	 */
	void showDetails(int index) {
		GlobalUtil.forceHideKeyboard(this);
		switch (index) {
			case 0: //Report view
				gotoReportView();
				break;
			case 1: {
				break;
			}
			case 2:
				break;
		default:
			break;
		}

	}

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
					GlobalUtil.dip2Pixel(200), LayoutParams.MATCH_PARENT);
			RelativeLayout.LayoutParams llMenuParam = new RelativeLayout.LayoutParams(
					GlobalUtil.dip2Pixel(200), LayoutParams.WRAP_CONTENT);
			lvMenu.setLayoutParams(layoutParams);
			llMenu.setLayoutParams(llMenuParam);
			iconLeft.setVisibility(View.VISIBLE);
			tvTitleMenu.setVisibility(View.VISIBLE);
		} else {
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					GlobalUtil.dip2Pixel(48), LayoutParams.MATCH_PARENT);
			RelativeLayout.LayoutParams llMenuParam = new RelativeLayout.LayoutParams(
					GlobalUtil.dip2Pixel(50), LayoutParams.WRAP_CONTENT);
			lvMenu.setLayoutParams(layoutParams);
			llMenu.setLayoutParams(llMenuParam);
			iconLeft.setVisibility(View.GONE);
			tvTitleMenu.setVisibility(View.GONE);

		}
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
				cell = new MenuItemRow(SupervisorKAActivity.this, row);
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
	public void handleModelViewEvent(ModelEvent modelEvent) {
		super.handleModelViewEvent(modelEvent);
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		super.handleErrorModelViewEvent(modelEvent);
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
	 * Go to report view
	 */
	private void gotoReportView() {
		Bundle b = new Bundle();
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = b;
		e.action = ActionEventConstant.GO_TO_REPORT_VIEW;
		AdminController.getInstance().handleSwitchFragment(e);
	}
}
