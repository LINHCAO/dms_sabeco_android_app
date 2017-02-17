/**
 * Copyright 2015 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.view.main;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.sabeco.R;

/**
 * View hien thi danh sach ung dung khong cho phep cai dat
 * @author: yennth16
 * @version: 1.0
 * @since: 18:56:07 25-12-2014
 */
public class BlackListAppView extends ScrollView implements OnClickListener {
	private GlobalBaseActivity parent;
	public View viewLayout;
	private VinamilkTableView tbApp;
	private int actionSelectedApp = -1;
	private TextView tvTitleApp;
	private ImageView ivIconMenu;

	public BlackListAppView(Context context, int actionSelectedApp) {
		super(context);
		this.actionSelectedApp = actionSelectedApp;
		parent = (GlobalBaseActivity) context;
		LayoutInflater inflater = parent.getLayoutInflater();
		viewLayout = inflater
				.inflate(R.layout.layout_black_list_app_view, null);
		tbApp = (VinamilkTableView) viewLayout.findViewById(R.id.tbApp);
		tvTitleApp = (TextView) viewLayout.findViewById(R.id.tvTitleApp);
		ivIconMenu = (ImageView) viewLayout.findViewById(R.id.ivIconMenu);
		ivIconMenu.setOnClickListener(this);
		layoutTableHeader();
	}

	/**
	 * render nhung row ung dung can xoa
	 * @author: Tuanlt11
	 * @param listApp
	 * @return: void
	 * @throws:
	 */
	public void renderLayout(final List<ApplicationInfo> listApp) {
		tbApp.clearAllData();
		tvTitleApp.setText(StringUtil.getString(R.string.TEXT_BLACK_LIST));
		int pos = 1;
		if (listApp != null && listApp.size() > 0) {
			for (int i = 0, s = listApp.size(); i < s; i++) {
				BlackListAppRow row = new BlackListAppRow(parent,
						actionSelectedApp);
				row.renderLayout(pos, listApp.get(i));
				pos++;
				tbApp.addRow(row);
			}
		}
	}
	
	
	public void layoutTableHeader() {
		// customer list table
		int[] CUSTOMER_LIST_TABLE_WIDTHS = { 48, 272};
		String[] CUSTOMER_LIST_TABLE_TITLES = { "", "Ứng dụng"};
		tbApp.getHeaderView().addColumns(CUSTOMER_LIST_TABLE_WIDTHS,
				CUSTOMER_LIST_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK),
				ImageUtil.getColor(R.color.TABLE_HEADER_BG));
	}

	@Override
	public void onClick(View v) {
		if (v == ivIconMenu) {
			parent.onEvent(ActionEventConstant.ACTION_REFRESH_APP_UNINSTALL,
					ivIconMenu, null);
		}
	}
}