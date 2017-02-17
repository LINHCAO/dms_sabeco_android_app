/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.viettel.dms.controller.AbstractController;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.sabeco.R;

/**
 * Lop dung chung cho tat ca cac AlertDialog
 * 
 * @author: dungdq3
 * @version: 1.0
 * @since: 1.0
 */
public class AbstractAlertDialog extends AlertDialog implements
		OnTouchListener, android.view.View.OnClickListener,
		DialogInterface.OnShowListener {

	private final Context con;
	protected final GlobalBaseActivity parent;
	protected BaseFragment listener;
	private final View v;
	private TextView tvTitleAbstractAlert;
	private ViewStub viewStub;

	/**
	 * @param context
	 */
	private AbstractAlertDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		con = context;
		parent = (GlobalBaseActivity) con;
		LayoutInflater li = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = li.inflate(R.layout.abstract_alertdialog, null);
		setView(v);
		setCanceledOnTouchOutside(false);
		Window window = getWindow();
		window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		window.setGravity(Gravity.CENTER);
	}

	public AbstractAlertDialog(Context context, BaseFragment base,
			CharSequence title) {
		this(context);
		setOnShowListener(this);
		tvTitleAbstractAlert = (TextView) v.findViewById(R.id.tvTitleAbstractAlert);
		tvTitleAbstractAlert.setTypeface(null, Typeface.BOLD);
		tvTitleAbstractAlert.setOnTouchListener(this);
		viewStub = (ViewStub) v.findViewById(R.id.vStub);
		v.setOnClickListener(this);
		listener = base;
		setTitle(title);
	}
	 
	/**
	 * Set title
	 * 
	 * @author: dungdq3
	 * @param: int action
	 * @param: Bundle b
	 * @return: void
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 * @date: Jan 9, 2014
	 */
	public void setTitle(CharSequence title){
		tvTitleAbstractAlert.setText(title);
	}

	/**
	 * Chuyen fragment
	 * 
	 * @author: dungdq3
	 * @param: int action
	 * @param: Bundle b
	 * @return: void
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 * @date: Jan 9, 2014
	 */
	protected void switchFragment(int action, Bundle b, AbstractController abController) {
		// TODO Auto-generated method stub
		ActionEvent e = new ActionEvent();
		e.action = action;
		e.sender = listener;
		e.viewData = (b == null ? new Bundle() : b);
		abController.handleSwitchFragment(e);
	}

	/**
	 * Set view cho viewStub
	 * 
	 * @author: dungdq3
	 * @param: Tham số của hàm
	 * @return: void
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 * @date: Jan 9, 2014
	 */
	protected View setViewLayout(int layoutResource) {
		// TODO Auto-generated method stub
		viewStub.setLayoutResource(layoutResource);
		return viewStub.inflate();
	}

	/**
	 * Tao su kien cho control tuong ung,
	 * 
	 * @author: dungdq3
	 * @param: int action (hanh dong duoc lay trong ActionEventConstant)
	 * @param: View control (control goi su kien nay)
	 * @param: Object data (du lieu de truyen sang listener)
	 * @return: void
	 * @throws:
	 * @date: Jan 9, 2014
	 */
	protected void setEventFromAlert(int action, View control, Object data) {
		// TODO Auto-generated method stub
		listener.onEvent(action, control, data);
	}

	/**
	 * show dialog by alert
	 * 
	 * @author: dungdq3
	 * @param: CharSequence title
	 * @param: String textOK
	 * @param: int actionOK
	 * @param: String textCancel
	 * @param: int actionCancel
	 * @param: Object data
	 * @return: void
	 * @date: Jan 9, 2014
	 */
	protected void showDialogFromAlert(CharSequence title, String textOK, int actionOK, String textCancel, int actionCancel, Object data) {
		// TODO Auto-generated method stub
		GlobalUtil.showDialogConfirm(listener, parent, title, textOK, actionOK, textCancel, actionCancel, data);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		boolean onTouch = false;
		if (v == tvTitleAbstractAlert
				&& event.getAction() == MotionEvent.ACTION_DOWN) {
			Drawable[] arrDrawable = tvTitleAbstractAlert.getCompoundDrawables().clone();
			Drawable drawable = arrDrawable[2]; // get right Drawable;
			int x = (int) event.getX();
			if (x > tvTitleAbstractAlert.getWidth()
					- tvTitleAbstractAlert.getPaddingRight()
					- drawable.getIntrinsicWidth()) { // neu vi tri cham nam
													  // trong drawable thi
				forceHideKeyBoardForDialog();
				dismiss(); // se dong dialog
				onTouch = true;
			}
		} else if (v == this.v) {
			forceHideKeyBoardForDialog();
		} else if (v == tvTitleAbstractAlert) {
			forceHideKeyBoardForDialog();
		}
		return onTouch;
	}

	/**
	 * @author: dungdq3
	 * @since: 1:46:34 PM Mar 6, 2014
	 * @return: void
	 * @param: action
	 * @param: bundle
	 * @param: abController
	 * @throws:
	 */
	protected void setHandleViewEvent(int action, Bundle bundle, AbstractController abController) {
		// TODO Auto-generated method stub
		ActionEvent e = new ActionEvent();
		e.action = action;
		e.sender = listener;
		e.viewData = (bundle == null ? new Bundle() : bundle);
		abController.handleViewEvent(e);
	}

	/**
	 * An keyboard
	 * 
	 * @author: dungdq3
	 * @since: 4:56:28 PM Mar 18, 2014
	 * @return: void
	 * @throws:
	 */
	public void forceHideKeyBoardForDialog() {
		// TODO Auto-generated method stub
		GlobalUtil.forceHideKeyboardInput(parent, v);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		forceHideKeyBoardForDialog();
	}

	@Override
	public void onShow(DialogInterface dialog) {
		// TODO Auto-generated method stub
	}
}
