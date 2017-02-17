/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.controller;

/**
 *  Controller giao tiep cua PG
 *  @author: BangHN
 *  @version: 1.1
 *  @since: 1.0
 */
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.model.PGModel;
import com.viettel.dms.model.SaleModel;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.sale.customer.CustomerFeedBackListView;
import com.viettel.dms.view.tnpg.customer.CompetitorSalePG;
import com.viettel.dms.view.tnpg.customer.CustomerListViewPG;
import com.viettel.dms.view.tnpg.customer.CustomerRouteViewPG;
import com.viettel.dms.view.tnpg.followproblem.TTTTFollowProblemView;
import com.viettel.dms.view.tnpg.image.TNPGFullImageView;
import com.viettel.dms.view.tnpg.image.TNPGListAlbumUserView;
import com.viettel.dms.view.tnpg.image.TNPGPhotoThumbnailListView;
import com.viettel.dms.view.tnpg.statistic.TNPGGeneralStatisticView;
import com.viettel.viettellib.network.http.HTTPRequest;

/**
 * Controller for PG
 * PGController.java
 * @version: 1.0 
 * @since:  10:05:18 8 Jan 2014
 */
public class TNPGController extends AbstractController {

	static TNPGController instance;

	protected TNPGController() {
	}

	public static TNPGController getInstance() {
		if (instance == null) {
			instance = new TNPGController();
		}
		return instance;
	}

	/**
	 * Xu ly cac request tu view
	 * @author: BangHN
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void handleViewEvent(final ActionEvent e) {
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {
				//HTTPRequest request = null;
				//GlobalBaseActivity base = null;
				//if (e.sender instanceof Activity) {
				//	base = (GlobalBaseActivity) e.sender;
				//} else if (e.sender instanceof Fragment) {
				//	base = (GlobalBaseActivity) ((Fragment) e.sender).getActivity();
				//}
				switch (e.action) {
				case ActionEventConstant.GET_LIST_PG_FOR_TAKE_ATTENDANCE: {
					PGModel.getInstance().getListPGForTakeAttendance(e);
					break;
				}
				case ActionEventConstant.GET_LIST_TYPE_PROBLEM_GST_TTTT:
					PGModel.getInstance().getListTypeProblemTTTT(e);
					break;
				case ActionEventConstant.GET_INFORMATION_COMPETITOR:
					PGModel.getInstance().getInformationCompetitor(e);
					break; 
				case ActionEventConstant.SAVE_PG_TAKE_ATTENDANCE: {
					PGModel.getInstance().savePGTakeAttendance(e);
					break;
				}
				case ActionEventConstant.GO_TO_ALBUM_DETAIL_USER:
					PGModel.getInstance().getAlbumDetailUser(e);
					break;
				case ActionEventConstant.GO_TO_LIST_ALBUM_USER:
					PGModel.getInstance().getListAlbumUser(e);
					break;
				case ActionEventConstant.SAVE_REMAIN_COMPETITOR:
					PGModel.getInstance().saveRemainCompetitor(e);
					break;
				case ActionEventConstant.SAVE_SALE_COMPETITOR:
					PGModel.getInstance().saveSaleCompetitor(e);
					break;
				case ActionEventConstant.CHECK_REMAINED_COMPETITOR:
					PGModel.getInstance().checkRemainedCompetitor(e);
					break;
				case ActionEventConstant.CHECK_SALED_COMPETITOR:
					PGModel.getInstance().checkSaledCompetitor(e);
					break;
				case ActionEventConstant.CHECK_VISIT_FROM_ACTION_LOG:
					SaleModel.getInstance().checkVisitFromActionLog(e);
					break;
				case ActionEventConstant.CHECK_TNPG_HAVE_ACTION_FROM_ACTION_LOG:
					PGModel.getInstance().checkTNPGHaveActionFromActionLog(e);
					break;
				default:// test
					break;
				}
				return null;
			}
		};
		task.execute();
	}

	/**
	 * Xu ly du lieu tra ve tu model
	 * @author: BangHN
	 * @param modelEvent
	 * @return: void
	 * @throws:
	 */
	public void handleModelEvent(final ModelEvent modelEvent) {
		if (modelEvent.getModelCode() == ErrorConstants.ERROR_CODE_SUCCESS) {
			final ActionEvent e = modelEvent.getActionEvent();
			HTTPRequest request = e.request;
			if (e.sender != null && (request == null || (request != null && request.isAlive()))) {
				if (e.sender instanceof GlobalBaseActivity) {
					final GlobalBaseActivity sender = (GlobalBaseActivity) e.sender;
					if (sender.isFinished)
						return;
					sender.runOnUiThread(new Runnable() {
						public void run() {
							// TODO Auto-generated method stub
							sender.handleModelViewEvent(modelEvent);
						}
					});
				} else if (e.sender instanceof BaseFragment) {
					final BaseFragment sender = (BaseFragment) e.sender;
					if (sender.getActivity() == null || sender.isFinished) {
						return;
					}
					sender.getActivity().runOnUiThread(new Runnable() {
						public void run() {
							sender.handleModelViewEvent(modelEvent);
						}
					});
				}
			} else {
				modelEvent.setIsSendLog(false);
				handleErrorModelEvent(modelEvent);
			}
		} else {
			handleErrorModelEvent(modelEvent);
		}
	}

	@Override
	public void handleSwitchActivity(ActionEvent e) {
	}

	@Override
	public void handleSwitchFragment(ActionEvent e) {
		Activity base = null;
		if (e.sender instanceof Activity) {
			base = (Activity) e.sender;
		} else if (e.sender instanceof Fragment) {
			base = ((Fragment) e.sender).getActivity();
		}
		if (base != null)
			GlobalUtil.forceHideKeyboard(base);

		FragmentManager fm = base.getFragmentManager();
		switch (e.action) {
    		case ActionEventConstant.GO_TO_GENERAL_STATISTICS: {
    			TNPGGeneralStatisticView genSta = TNPGGeneralStatisticView.getInstance((Bundle)e.viewData);
    			switchFragment(fm, genSta, TNPGGeneralStatisticView.TAG, true);
    			break;
    		}
    		case ActionEventConstant.GO_TO_CUSTOMER_LIST_PG: {
    			removeAllInBackStack(fm);
    			CustomerListViewPG genSta = CustomerListViewPG.newInstance();
    			switchFragment(fm, genSta, CustomerListViewPG.TAG, true);
    			break;
    		}
    		case ActionEventConstant.GO_TO_CUSTOMER_ROUTE_PG: {
    			CustomerRouteViewPG genSta =  CustomerRouteViewPG.getInstance();
    			switchFragment(fm, genSta, CustomerRouteViewPG.TAG, true);
    			break;
    		}
    		case ActionEventConstant.GO_TO_TTTT_FOLLOW_LIST_PROBLEM: {
    			TTTTFollowProblemView genSta = TTTTFollowProblemView.newInstance();
    			switchFragment(fm, genSta, TTTTFollowProblemView.TAG, true);
    			break;
    		}
    		case ActionEventConstant.GO_TO_CHECK_REMAIN_COMPETITOR: {
    			CompetitorSalePG genSta = CompetitorSalePG.getInstance((Bundle) e.viewData);
    			switchFragment(fm, genSta, CompetitorSalePG.TAG, false);
    			break;
    		}
    		case ActionEventConstant.GET_LIST_CUS_FEED_BACK: { 
    			Bundle b = (Bundle) e.viewData;
    			CustomerFeedBackListView cusFbFrag = CustomerFeedBackListView.newInstance(b);
    			switchFragment(fm, cusFbFrag, CustomerFeedBackListView.TAG, false);
    			break;
    		} 
    		case ActionEventConstant.GO_TO_ALBUM_DETAIL_USER: {
    			Bundle data = (Bundle) e.viewData;
    			TNPGPhotoThumbnailListView listAlbum = TNPGPhotoThumbnailListView.getInstance(data);
    			switchFragment(fm, listAlbum, TNPGPhotoThumbnailListView.TAG, false);
    			break;
    		}
    		case ActionEventConstant.GO_TO_LIST_ALBUM_USER: {
    			Bundle data = (Bundle) e.viewData;
    			TNPGListAlbumUserView listAlbum = TNPGListAlbumUserView.getInstance(data);
    			switchFragment(fm, listAlbum, TNPGListAlbumUserView.TAG, false);
    			break;
    		}
    		case ActionEventConstant.ACTION_LOAD_IMAGE_FULL: {
    			TNPGFullImageView genSta = TNPGFullImageView.newInstance((Bundle) e.viewData);
    			switchFragment(fm, genSta, TNPGFullImageView.TAG, false);
    			break;
    		} 
		}
	}

}
