/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.image;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.viettel.dms.view.main.BaseFragment;
import com.viettel.map.FragmentMapView;
import com.viettel.sabeco.R;

/**
 * Show mot pop up chi dia chi dia diem
 * 
 * @author: PhucNT
 * @version: 1.0
 * @since: Aug 5, 2012
 */
public class PopupShowPosition extends RelativeLayout implements OnClickListener {
	// thoi gian hien thi toi da
	//private final int DISPLAY_TIME_DEFAULT = 5000;

	private Button btClose;
	//private boolean isDisplay = false;
	// chieu cao cua view
	//private int height = 0;
	BaseFragment listener;
	// final Handler handler = new Handler() {
	// public void handleMessage(Message msg) {
	// listener.visibleHeader();
	//
	// }
	// };

	// private TextView tvUser;

	public RelativeLayout rlMap;

	/**
	 * @param context
	 */
	public PopupShowPosition(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.layout_popup_show_position, this);
		initView(view);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public PopupShowPosition(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.layout_image_tool_bar, this);
		initView(view);
	}

	public void setFragment(FragmentMapView image) {
		listener = image;
	}

	/**
	 * Khoi tao view
	 * 
	 * @author: PhucNT
	 * @param view
	 * @return: void
	 * @throws:
	 */
	private void initView(View view) {
		// tvTime = (TextView) view.findViewById(R.id.tvTime);
		btClose = (Button) view.findViewById(R.id.btClose);
		btClose.setOnClickListener(this);
		rlMap = (RelativeLayout) view.findViewById(R.id.rlMap);
	}

	public RelativeLayout getLayoutMap() {
		return rlMap;
	}

	/**
	 * Hien thi trong mot thoi gian cho truoc
	 * 
	 * @author: PhucNT
	 * @return: void
	 * @throws:
	 */
	public void goneView() {
		// TODO Auto-generated method stub
		setVisibility(View.GONE);
		// handler.removeMessages(0);
	}

	/**
	 * Hien thi trong mot thoi gian cho truoc
	 * 
	 * @author: PhucNT
	 * @return: void
	 * @throws:
	 */
	public void visibleInTime() {
		// TODO Auto-generated method stub
		// handler.removeMessages(0);
		// handler.sendEmptyMessageDelayed(0, DISPLAY_TIME_DEFAULT);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0 == btClose && listener != null)
			listener.closePopup();
	}

	public void setFragment(BaseFragment image) {
		listener = image;
	}

}
