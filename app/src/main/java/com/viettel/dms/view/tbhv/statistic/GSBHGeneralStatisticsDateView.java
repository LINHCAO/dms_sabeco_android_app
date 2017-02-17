/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv.statistic;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.viettel.dms.constants.FragmentMenuContanst;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.view.GSTGeneralStatisticsViewDTO;
import com.viettel.dms.dto.view.GeneralStatisticsViewDTO.GeneralStatisticsInfo;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.sale.statistic.NVBHGeneralStatisticsView;
import com.viettel.sabeco.R;

/**
 * View for GST date report 
 * GSTGeneralStatisticsDateView.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  19:30:35 5 Dec 2013
 */
public class GSBHGeneralStatisticsDateView extends BaseFragment implements OnEventControlListener, VinamilkTableListener {
	//TAG of fragment
	public static final String TAG = GSBHGeneralStatisticsDateView.class.getName();
	//parent Activity
	private GlobalBaseActivity parent;
	private static final int ACTION_SHOW_DETAIL_GSBH_WITH_STAFF = 1;
	private static final int ACTION_SHOW_DETAIL_GSBH_WITH_NPP = 2;
	private static final int ACTION_SHOW_DETAIL_TNPG = 3;
	
	//BEGIN DTO
	private GSTGeneralStatisticsViewDTO dto = null;  
	//END DTO
	//BEGIN USER INFO
	private String userId;
	private String shopId;
	//END USER INFO
	
	//BEGIN DECLARE
	private LinearLayout llHeaderProgressInfo;
	private DMSTableView tbGsbhStatistics;
	private DMSTableView tbTnPGStatistics;
	//END DECLARE
	
	//BEGIN NEW INSTANCE
	public static GSBHGeneralStatisticsDateView newInstance(Bundle b) {
		GSBHGeneralStatisticsDateView instance = new GSBHGeneralStatisticsDateView();
		instance.setArguments(b);
		return instance;
	}

	public static GSBHGeneralStatisticsDateView getInstance(Bundle b) {
		return newInstance(b);
	}
	//END NEW INSTANCE
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_gst_general_month_report_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		
		initUserInfo();
		initView(v);
		initHeaderText();
		initHeaderMenu();
		requestData();
		return v;
	}
	
	/**
	 * initUserInfo from other View send to this
	 * @author: duongdt3
	 * @since: 19:31:05 5 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void initUserInfo() {
		userId =  GlobalInfo.getInstance().getProfile().getUserData().id + "";
		shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
	}
	public GSBHGeneralStatisticsDateView() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get views from layout
	 * @author: duongdt3
	 * @since: 19:31:19 5 Dec 2013
	 * @return: void
	 * @throws:  
	 * @param view
	 */
	private void initView(View view) {
		llHeaderProgressInfo = (LinearLayout) view.findViewById(R.id.llHeaderProgressInfo);
		//an header tien trinh thang
		llHeaderProgressInfo.setVisibility(View.GONE);
		tbGsbhStatistics = (DMSTableView) view.findViewById(R.id.tbGsbhStatistics);
		tbGsbhStatistics.setListener(this);
		//HeaderTableInfo[] gsbhHeaderInfo = GSBHGeneralStatisticsDateRow.getTableHeaderGSBH(parent);
		GSBHGeneralStatisticsDateRow gsbhHeaderInfo = new GSBHGeneralStatisticsDateRow(parent, this, false, -1, -1);
		gsbhHeaderInfo.renderHeaderGSBH();
		initHeaderTable(tbGsbhStatistics, gsbhHeaderInfo);
		
		tbTnPGStatistics = (DMSTableView) view.findViewById(R.id.tbTnPGStatistics);
		tbTnPGStatistics.setListener(this);
		GSBHGeneralStatisticsDateRow gsbhHeaderInfo1 = new GSBHGeneralStatisticsDateRow(parent, this, false, -1, -1);
		gsbhHeaderInfo1.renderHeaderTNPG();
		initHeaderTable(tbTnPGStatistics, gsbhHeaderInfo1);
	}
	
	
	/**
	 * initHeaderText
	 * @author: duongdt3
	 * @since: 19:31:44 5 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void initHeaderText() {
		String title = StringUtil.getString(R.string.TEXT_TITLE_REPORT_PROGRESS_DATE) + " " + StringUtil.getBoldText(DateUtils.getCurrentDate());
		Spanned titleBold = StringUtil.getHTMLText(title);
		setTitleHeaderView(titleBold);
	}
	
	
	/**
	 * init menu header
	 * @author: duongdt3
	 * @since: 19:31:50 5 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void initHeaderMenu() {
		// enable menu bar
		enableMenuBar(this, FragmentMenuContanst.GST_REPORT_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_GSBH_GENERAL_STATISTICS_DATE);
	}
	
	
	/**
	 * request data on first time
	 * @author: duongdt3
	 * @since: 19:31:55 5 Dec 2013
	 * @return: void
	 * @throws:
	 */
//	private void initData() {
//		requestData();
//	}
	
	/**
	 * Get data from DB
	 * @author: duongdt3
	 * @since: 19:32:00 5 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void requestData() {
		this.parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_STAFF_ID, userId);
		data.putString(IntentConstants.INTENT_SHOP_ID, shopId);
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_GENERAL_STATISTICS_DATE_REPORT_INFO;
		TBHVController.getInstance().handleViewEvent(e);
	}
	
	/**
	 * Render layout from data
	 * @author: duongdt3
	 * @since: 19:32:10 5 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		if (dto != null) {
			tbGsbhStatistics.clearAllData();
			tbTnPGStatistics.clearAllData();

			// render for GSBH
			List<DMSTableRow> listRowGSBH = new ArrayList<DMSTableRow>();

			if (dto.listGsbhInfo != null) {
				GeneralStatisticsInfo infoTotal = new GeneralStatisticsInfo();

				for (int i = 0; i < dto.listGsbhInfo.size(); i++) {
					GeneralStatisticsInfo infoGSBH = dto.listGsbhInfo.get(i);
					// cal total
					infoTotal.calTotal(infoGSBH);

					GSBHGeneralStatisticsDateRow row = new GSBHGeneralStatisticsDateRow(parent, this, false, ACTION_SHOW_DETAIL_GSBH_WITH_STAFF, ACTION_SHOW_DETAIL_GSBH_WITH_NPP);
					row.render(infoGSBH);
					// add to list row
					listRowGSBH.add(row);
					tbGsbhStatistics.addRow(row);
				}

				// add total row
				if (listRowGSBH.size() > 0) {
					GSBHGeneralStatisticsDateRow row = new GSBHGeneralStatisticsDateRow(parent, this, true, ACTION_SHOW_DETAIL_GSBH_WITH_STAFF, ACTION_SHOW_DETAIL_GSBH_WITH_NPP);
					row.render(infoTotal);
					listRowGSBH.add(row);
					tbGsbhStatistics.addRow(row);
				}
			}

			// render for TNPG
			List<DMSTableRow> listRowTNPG = new ArrayList<DMSTableRow>();

			if (dto.listTNPGInfo != null) {
				GeneralStatisticsInfo infoTotal = new GeneralStatisticsInfo();

				for (int i = 0; i < dto.listTNPGInfo.size(); i++) {
					GeneralStatisticsInfo infoTNPG = dto.listTNPGInfo.get(i);
					// cal total
					infoTotal.calTotal(infoTNPG);

					GSBHGeneralStatisticsDateRow row = new GSBHGeneralStatisticsDateRow(parent, this, false, ACTION_SHOW_DETAIL_TNPG, ACTION_SHOW_DETAIL_TNPG);
					row.render(infoTNPG);
					// add to list row
					listRowTNPG.add(row);
					tbTnPGStatistics.addRow(row);
				}

				// add total row
				if (listRowTNPG.size() > 0) {
					GSBHGeneralStatisticsDateRow row = new GSBHGeneralStatisticsDateRow(parent, this, true, ACTION_SHOW_DETAIL_TNPG, ACTION_SHOW_DETAIL_TNPG);
					row.render(infoTotal);
					listRowTNPG.add(row);
					tbTnPGStatistics.addRow(row);
				}
			}
		}
	}
		
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		//close Progress Dialog
		this.parent.closeProgressDialog();	
		
		switch (modelEvent.getActionEvent().action) {
			case ActionEventConstant.GET_GENERAL_STATISTICS_DATE_REPORT_INFO:
    			//get data
    			dto = (GSTGeneralStatisticsViewDTO) modelEvent.getModelData();
    			renderLayout();
    			requestInsertLogKPI(HashMapKPI.GSBH_BAOCAOTONGHOPTIENDONGAY, modelEvent.getActionEvent().startTimeFromBoot);
			break;
			default:
				super.handleModelViewEvent(modelEvent);
				break;
		}
	}
	
	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		//close Progress Dialog
		this.parent.closeProgressDialog();
		//show Error Message
		//this.parent.showDialog(StringUtil.getString(R.string.MESSAGE_ERROR_HAPPEN));
		switch (modelEvent.getActionEvent().action) {
			default:
				super.handleErrorModelViewEvent(modelEvent);
				break;
		}
	}
	
	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
			default:
				super.onEvent(eventType, control, data);
				break;
		}
	}
	
	//table paging
	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		if(data instanceof GeneralStatisticsInfo){
			GeneralStatisticsInfo info = (GeneralStatisticsInfo) data;
			ActionEvent e = new ActionEvent();
			e.sender = this;
			Bundle param = new Bundle();
			param.putInt(IntentConstants.INTENT_TYPE_REQUEST, NVBHGeneralStatisticsView.TYPE_REQUEST_GST_DATE_DETAIL);
			param.putString(IntentConstants.INTENT_STAFF_ID, info.tvObjectId); 
			param.putString(IntentConstants.INTENT_SHOP_ID, info.tvShopId);
			//send shop of him to view detail
			param.putString(IntentConstants.INTENT_PARENT_STAFF_ID, userId);
			
			e.viewData = param;
			switch (action) {
        		case ACTION_SHOW_DETAIL_TNPG:
        			e.action = ActionEventConstant.GO_TO_GENERAL_STATISTICS_DATE_DETAIL_TNPG;
        			break;
        		case ACTION_SHOW_DETAIL_GSBH_WITH_STAFF:
        			e.action = ActionEventConstant.GO_TO_GENERAL_STATISTICS_DATE_DETAIL_GSBH;
        			break;
        		case ACTION_SHOW_DETAIL_GSBH_WITH_NPP:
        			e.action = ActionEventConstant.GO_TO_GENERAL_STATISTICS_DATE_DETAIL_GSBH;
        			break;
        		default:
        			break;
			}
			TBHVController.getInstance().handleSwitchFragment(e);
		}
		
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				//Re request data
				requestData();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
}
