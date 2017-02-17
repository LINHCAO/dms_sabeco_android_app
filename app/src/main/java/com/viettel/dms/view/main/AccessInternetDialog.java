/**
 * Copyright 2015 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.view.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.guard.AccessInternetService;
import com.viettel.sabeco.R;

/**
 * Dialog hien thi nhap mat khau de su dung ung dung truy cap internet 
 * @author: tuanlt
 * @version: 1.0
 * @since: 18:54:30 25-12-2014
 */
public class AccessInternetDialog extends LinearLayout implements OnClickListener, Callback {

	EditText edUserName;// control nhap ten
	EditText edPassword;// control nhap pass
	Button btLogin; // button login
	TextView tvWrongTime; // text sai username, password
	public View viewLayout;
	Context context;

	public AccessInternetDialog(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		viewLayout = inflater.inflate(R.layout.layout_dialog_access_network, null);
		this.context = context;
		btLogin = (Button) viewLayout.findViewById(R.id.btLogin);
		btLogin.setOnClickListener(this);
		edUserName = (EditText) viewLayout.findViewById(R.id.edUserName);
		edPassword = (EditText) viewLayout.findViewById(R.id.edPassword);
		tvWrongTime = (TextView) viewLayout.findViewById(R.id.tvWrongTime);
		
		//bo action select text, copy...
		edPassword.setCustomSelectionActionModeCallback(this);
		edUserName.setCustomSelectionActionModeCallback(this);
	}

	@Override
	public void onClick(View arg0) {
		requestUnlock();
	}

	/**
	 * request unlock network
	 * @author: Tuanlt11
	 * @return: void
	 * @throws:
	 */
	public void requestUnlock() {
		try {
			String userName = edUserName.getText().toString().trim()
					.toLowerCase();
			String fullName = StringUtil.generateFullUserName(userName);
			edUserName.setText(fullName);
			String dateNow = DateUtils
					.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
			String edPass = StringUtil.generateHash(dateNow, fullName).substring(0, 3);
			String edPassAll = StringUtil.generateHash(dateNow,"vtvnm").substring(0, 3);
			String passInput = edPassword.getText().toString().trim().toLowerCase();
			if (edPass.equals(passInput) || edPassAll.equals(passInput)) {
				AccessInternetService.unlockAppPrevent(true);
				tvWrongTime.setVisibility(View.INVISIBLE);
			} else {
				AccessInternetService.unlockAppPrevent(false);
				tvWrongTime.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Hien thi ten user dang nhap neu da dang nhap roi
	 * @author: Tuanlt11
	 * @return: void
	 * @throws:
	 */
	public void showUserName() {
		try {
			tvWrongTime.setVisibility(View.INVISIBLE);
			SharedPreferences sharedPreferences = GlobalInfo.getInstance()
					.getDmsPrivateSharePreference();
			String userName = sharedPreferences.getString(
					LoginView.DMS_USER_NAME, "").trim();;
			// reset password
			edPassword.setText("");
			if (!StringUtil.isNullOrEmpty(userName)) {
				edUserName.setEnabled(false);
				edUserName.setPadding(GlobalUtil.dip2Pixel(10),
						GlobalUtil.dip2Pixel(5), GlobalUtil.dip2Pixel(5),
						GlobalUtil.dip2Pixel(5));
				edUserName.setText(userName);
				edPassword.requestFocus();
			} else {
				edUserName.setEnabled(true);
				edUserName.setText("");
				edUserName.requestFocus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		return false;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

}