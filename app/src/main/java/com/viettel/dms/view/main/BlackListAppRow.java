/**
 * Copyright 2015 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.view.main;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;

/**
 * Dong hien thi thong tin item cua ung dung
 * @author: yennth16
 * @version: 1.0 
 * @since:  18:56:48 25-12-2014
 */
public class BlackListAppRow extends TableRow implements OnClickListener {
	private int actionSelectedApp = -1;
	//private Context context;
	private View view;
	private ImageView imgIcon;
	private TextView tvAppName;
	private TextView tvPackageName;
	TableRow row;
	private ApplicationInfo itemApp;
	//su kien click row
	private OnEventControlListener listener;
	GlobalBaseActivity parent;
	
	public BlackListAppRow(Context context, int actionSelectedApp) {
		super(context);
		parent = (GlobalBaseActivity)context;
		this.actionSelectedApp = actionSelectedApp;
		listener = (OnEventControlListener) context;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_black_list_app_row, this);
		row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);
		imgIcon = (ImageView)findViewById(R.id.imgAppIcon);
		tvAppName = (TextView) view.findViewById(R.id.tvAppName);
		tvPackageName = (TextView) view.findViewById(R.id.tvPackageName);
		
	}

	
	 /**
	 * render layout
	 * @author: Tuanlt11
	 * @param pos
	 * @param item
	 * @return: void
	 * @throws:
	*/
	public void renderLayout(int pos, ApplicationInfo item) {
		this.itemApp = item;
		imgIcon.setImageDrawable(item.loadIcon(parent.packageManager));
		tvAppName.setText(item.loadLabel(parent.packageManager));
		tvPackageName.setText("( " + itemApp.packageName + " )");
	}

	@Override
	public void onClick(View v) {
		if(v == row && listener != null && (itemApp != null)){
			listener.onEvent(actionSelectedApp, null, itemApp);
		}
	}
}