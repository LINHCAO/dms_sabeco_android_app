/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.controller;

import android.app.Activity;
import android.app.Fragment;

import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.model.SynDataModelService;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.network.http.HTTPRequest;

/**
 *  Controller xu ly dong bo
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class SynDataController extends AbstractController {

	static SynDataController instance;

	protected SynDataController() {
	}

	public static SynDataController getInstance() {
		if (instance == null) {
			instance = new SynDataController();
		}
		return instance;
	}

	@Override
	public void handleViewEvent(final ActionEvent e) {
		try {
			HTTPRequest request = null;
			GlobalBaseActivity base = null;
			if (e.sender instanceof Activity) {
				base = (GlobalBaseActivity) e.sender;
			} else if (e.sender instanceof Fragment) {
				base = (GlobalBaseActivity) ((Fragment) e.sender).getActivity();
			}
			
			switch (e.action) {
			// tien hanh synchronizer data.
			case ActionEventConstant.ACTION_SYN_SYNDATA_MAX:
			case ActionEventConstant.ACTION_SYN_SYNDATA:
				request = SynDataModelService.getInstance().requestSynData(e);
				break;
			case ActionEventConstant.ACTION_GET_LINK_SQL_FILE:
				
				request = SynDataModelService.getInstance().requestGetLinkSqlFile(e);
				
				break;
			default:
				break;
			}
			if (request != null && base != null) {
				base.addProcessingRequest(request, e.isBlockRequest);
				e.request = request;
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
	}

	@Override
	public void handleModelEvent(final ModelEvent modelEvent) {
		if (modelEvent.getModelCode() == ErrorConstants.ERROR_CODE_SUCCESS) {
			final ActionEvent e = modelEvent.getActionEvent();
			HTTPRequest request = e.request;
			if (e.sender != null && (request == null || (request != null && request.isAlive()))) {
				if (e.sender instanceof GlobalBaseActivity) {
					final GlobalBaseActivity sender = (GlobalBaseActivity) e.sender;
					sender.runOnUiThread(new Runnable() {
						public void run() {
							// TODO Auto-generated method stub
							sender.handleModelViewEvent(modelEvent);
						}
					});
				} else if (e.sender instanceof BaseFragment) {
					final BaseFragment sender = (BaseFragment) e.sender;
					sender.getActivity().runOnUiThread(new Runnable() {
						public void run() {
							// TODO Auto-generated method stub
							sender.handleModelViewEvent(modelEvent);
						}
					});
				}
			} else {
				modelEvent.setIsSendLog(false);
				handleErrorModelEvent(modelEvent);
			}
		}
	}

	@Override
	public void handleErrorModelEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		super.handleErrorModelEvent(modelEvent);
	}

	@Override
	public void handleSwitchFragment(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleSwitchActivity(ActionEvent e) {

	}
}
