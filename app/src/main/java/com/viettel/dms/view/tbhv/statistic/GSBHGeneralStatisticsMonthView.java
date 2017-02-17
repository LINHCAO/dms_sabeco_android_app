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
import android.widget.TextView;

import com.viettel.dms.constants.FragmentMenuContanst;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.view.GSTGeneralStatisticsViewDTO;
import com.viettel.dms.dto.view.GeneralStatisticsViewDTO.GeneralStatisticsInfo;
import com.viettel.dms.dto.view.ProgressSoldViewDTO;
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
 * View for GST month report 
 * GSTGeneralStatisticsDateView.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  19:30:35 5 Dec 2013
 */
public class GSBHGeneralStatisticsMonthView extends BaseFragment implements OnEventControlListener, VinamilkTableListener {
	//TAG of fragment
	public static final String TAG = GSBHGeneralStatisticsMonthView.class.getName();
	//parent Activity
	private GlobalBaseActivity parent;
	//BEGIN DTO
	private GSTGeneralStatisticsViewDTO dto = null;  
	//END DTO
	//BEGIN USER INFO
	private String userId;
	private String shopId;
	//END USER INFO
	
	//BEGIN DECLARE
	private TextView tvNumDaySalePlan;
	private TextView tvNumDaySold;
	private TextView tvSoldPercent;
	private DMSTableView tbGsbhStatistics;
	private DMSTableView tbTnPGStatistics;
	private float progressSold = 0;
	private static final int ACTION_SHOW_DETAIL_GSBH = 1;
	private static final int ACTION_SHOW_DETAIL_TNPG = 2;
	//END DECLARE
	
	//BEGIN NEW INSTANCE
	public static GSBHGeneralStatisticsMonthView newInstance(Bundle b) {
		GSBHGeneralStatisticsMonthView instance = new GSBHGeneralStatisticsMonthView();
		instance.setArguments(b);
		return instance;
	}

	public static GSBHGeneralStatisticsMonthView getInstance(Bundle b) {
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
		initData();
		return v;
	}
	
	private void initUserInfo() {
		userId =  GlobalInfo.getInstance().getProfile().getUserData().id + "";
		shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
	}
	
	private void initView(View view) {
		tvNumDaySalePlan = (TextView) view.findViewById(R.id.tvNumDaySalePlan);
		tvNumDaySold = (TextView) view.findViewById(R.id.tvNumDaySold);
		tvSoldPercent = (TextView) view.findViewById(R.id.tvSoldPercent);
		tbGsbhStatistics = (DMSTableView) view.findViewById(R.id.tbGsbhStatistics);
		tbTnPGStatistics = (DMSTableView) view.findViewById(R.id.tbTnPGStatistics);
		tbGsbhStatistics.setListener(this);
		GSBHGeneralStatisticsMonthRow gsbhHeaderInfo = new GSBHGeneralStatisticsMonthRow(parent, this, false, -1);
		gsbhHeaderInfo.renderHeaderGSBH();
		initHeaderTable(tbGsbhStatistics, gsbhHeaderInfo);
		
		tbTnPGStatistics = (DMSTableView) view.findViewById(R.id.tbTnPGStatistics);
		tbTnPGStatistics.setListener(this);
		GSBHGeneralStatisticsMonthRow gsbhHeaderInfo1 = new GSBHGeneralStatisticsMonthRow(parent, this, false, -1);
		gsbhHeaderInfo1.renderHeaderTNPG();
		initHeaderTable(tbTnPGStatistics, gsbhHeaderInfo1);
	}
		
	
	private void initHeaderText() {
		String title = StringUtil.getString(R.string.TEXT_TITLE_REPORT_PROGRESS_MONTH_GST) + " " + StringUtil.getBoldText(DateUtils.getCurrentDate());
		Spanned titleBold = StringUtil.getHTMLText(title);
		setTitleHeaderView(titleBold);
	}
	
	private void initHeaderMenu() {
		// enable menu bar
		enableMenuBar(this, FragmentMenuContanst.GST_REPORT_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_GSBH_GENERAL_STATISTICS_MONTH);
	}

	/**
	 * request data on first time 
	 * @author: duongdt3
	 * @since: 15:08:43 12 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void initData() {
		requestData();
	}
	
	private void requestData() {
		//show dialog loading... 
		this.parent.showProgressDialog(StringUtil.getString(R.string.loading));
		//create request
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_STAFF_ID, userId);
		data.putString(IntentConstants.INTENT_SHOP_ID, shopId);
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_GENERAL_STATISTICS_MONTH_REPORT_INFO;
		//send request
		TBHVController.getInstance().handleViewEvent(e);
	}

	/**
	 * render progress info
	 * @author: duongdt3
	 * @since: 15:08:59 12 Dec 2013
	 * @return: void
	 * @throws:  
	 * @param info
	 */
	private void renderProgress(ProgressSoldViewDTO info) {
		if (info != null) {
			String strNumberDayPlan = info.numberDayPlan + "";
			tvNumDaySalePlan.setText(strNumberDayPlan);

			String strNumDaySold = info.numberDaySold + "";
			tvNumDaySold.setText(strNumDaySold);

			String strSoldPercent = StringUtil.parseProgressSold(info.progressSold);
			tvSoldPercent.setText(strSoldPercent);
		}
	}
	
	private void renderLayout() {
		tbGsbhStatistics.clearAllData();
		tbTnPGStatistics.clearAllData();
		if (dto != null) {
			
			if(dto.progressSoldInfo != null){
				//render progress
				renderProgress(dto.progressSoldInfo);
				progressSold = dto.progressSoldInfo.progressSold;
			}
			
			//render for GSBH
			List<DMSTableRow> listRowGSBH = new ArrayList<DMSTableRow>();
			
			if(dto.listGsbhInfo != null){
				GeneralStatisticsInfo infoTotal = new GeneralStatisticsInfo();
				
				for (int i = 0; i < dto.listGsbhInfo.size(); i++) {
					GeneralStatisticsInfo infoGSBH = dto.listGsbhInfo.get(i);
					infoGSBH.checkHightLightByProgress(progressSold);
					//cal total
					infoTotal.calTotal(infoGSBH);
					
					GSBHGeneralStatisticsMonthRow row = new GSBHGeneralStatisticsMonthRow(parent, this, false, ACTION_SHOW_DETAIL_GSBH);
					row.render(infoGSBH);
					//add to list row
					listRowGSBH.add(row);
					tbGsbhStatistics.addRow(row);
				}
				
				//add total row
				if (listRowGSBH.size() > 0) {
					GSBHGeneralStatisticsMonthRow row = new GSBHGeneralStatisticsMonthRow(parent, this, true, ACTION_SHOW_DETAIL_GSBH);
					infoTotal.checkHightLightByProgress(progressSold);
					row.render(infoTotal);
					listRowGSBH.add(row);
					tbGsbhStatistics.addRow(row);
				}
			}
			
			//render for TNPG
			List<DMSTableRow> listRowTNPG = new ArrayList<DMSTableRow>();
			
			if(dto.listTNPGInfo != null){
				GeneralStatisticsInfo infoTotal = new GeneralStatisticsInfo();
				
				for (int i = 0; i < dto.listTNPGInfo.size(); i++) {
					GeneralStatisticsInfo infoTNPG = dto.listTNPGInfo.get(i);
					infoTNPG.checkHightLightByProgress(progressSold);
					//cal total
					infoTotal.calTotal(infoTNPG);
					
					GSBHGeneralStatisticsMonthRow row = new GSBHGeneralStatisticsMonthRow(parent, this, false, ACTION_SHOW_DETAIL_TNPG);
					row.render(infoTNPG);
					//add to list row
					listRowTNPG.add(row);
					tbTnPGStatistics.addRow(row);
				}
				
				//add total row
				if (listRowTNPG.size() > 0) {
					GSBHGeneralStatisticsMonthRow row = new GSBHGeneralStatisticsMonthRow(parent, this, true, ACTION_SHOW_DETAIL_TNPG);
					infoTotal.checkHightLightByProgress(progressSold);
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
    		case ActionEventConstant.GET_GENERAL_STATISTICS_MONTH_REPORT_INFO:
    			//get data
    			dto = (GSTGeneralStatisticsViewDTO) modelEvent.getModelData();
    			renderLayout();
    			requestInsertLogKPI(HashMapKPI.GSBH_BAOCAOTONGHOPTIENDOLUYKE, modelEvent.getActionEvent().startTimeFromBoot);
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
	
	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		
	}
	
	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		if(data instanceof GeneralStatisticsInfo){
			GeneralStatisticsInfo info = (GeneralStatisticsInfo) data;
			ActionEvent e = new ActionEvent();
			e.sender = this;
			Bundle param = new Bundle();
			param.putInt(IntentConstants.INTENT_TYPE_REQUEST, NVBHGeneralStatisticsView.TYPE_REQUEST_GST_MONTH_DETAIL);
			param.putString(IntentConstants.INTENT_STAFF_ID, info.tvObjectId); 
			param.putString(IntentConstants.INTENT_SHOP_ID, info.tvShopId);
			//send shop of him to view detail
			param.putString(IntentConstants.INTENT_PARENT_STAFF_ID, userId);
			e.viewData = param;
			switch (action) {
        		case ACTION_SHOW_DETAIL_TNPG:
        			e.action = ActionEventConstant.GO_TO_GENERAL_STATISTICS_MONTH_DETAIL_TNPG;
        			break;
        		case ACTION_SHOW_DETAIL_GSBH:
        			e.action = ActionEventConstant.GO_TO_GENERAL_STATISTICS_MONTH_DETAIL_GSBH;
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
				initData();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
}
