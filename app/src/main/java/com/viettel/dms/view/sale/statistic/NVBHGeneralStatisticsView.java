/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.statistic;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.FragmentMenuContanst;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.view.GeneralStatisticsViewDTO;
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
import com.viettel.sabeco.R;

/**
 * Báo cáo doanh số trong ngày và trong tháng của NVBH
 * GeneralStatisticsSaleView.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  10:34:59 22 Nov 2013
 */
public class NVBHGeneralStatisticsView extends BaseFragment implements OnEventControlListener, VinamilkTableListener {
	//TAG of fragment
	public static final String TAG = NVBHGeneralStatisticsView.class.getName();
	//parent Activity
	private GlobalBaseActivity parent;
	//type request = request from
	private static final int TYPE_REQUEST_NVBH = 1;
	public static final int TYPE_REQUEST_GSBH = 2;
	public static final int TYPE_REQUEST_TNPG = 3;
	public static final int TYPE_REQUEST_GST_DATE_DETAIL = 4;
	public static final int TYPE_REQUEST_GST_MONTH_DETAIL = 5;
	//BEGIN USER INFO
	private String userId;
	private String shopId;
	//END USER INFO
	
	//BEGIN REPORT INFO
	private float progressSold = 0;
	//END REPORT INFO
	
	//BEGIN PAGING
	//Num row in page
	private static final int NUM_ITEM_PER_PAGE = Constants.NUM_ITEM_PER_PAGE;
	//END PAGING
	
	//BEGIN DECLARE
	private TextView tvNumDaySalePlan;
	private TextView tvNumDaySold;
	private TextView tvSoldPercent;
	private DMSTableView tbStatistics;
	//END DECLARE
	
	//BEGIN NEW INSTANCE
	public static NVBHGeneralStatisticsView newInstance(Bundle b) {
		NVBHGeneralStatisticsView instance = new NVBHGeneralStatisticsView();
		instance.setArguments(b);
		return instance;
	}

	public static NVBHGeneralStatisticsView getInstance(Bundle b) {
		return newInstance(b);
	}
	//END NEW INSTANCE
	
	//BEGIN DTO
	private GeneralStatisticsViewDTO dto = null;  
	//END DTO
	private int typeRequest = TYPE_REQUEST_NVBH;
	private String userName = "";
		
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_nvbh_general_report_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		
		initUserInfo();
		initView(v);
		initHeaderText();
		initHeaderMenu();
		initData();
		
		return v;
	}
	
	/**
	 * Get user info
	 * @author: duongdt3
	 * @since: 10:58:01 16 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void initUserInfo() {
		Bundle param = getArguments();
		if (param!=null) {
			typeRequest  = param.getInt(IntentConstants.INTENT_TYPE_REQUEST, TYPE_REQUEST_NVBH);
			
			switch (typeRequest) {
				//Sale man login
    			case TYPE_REQUEST_NVBH: {
    				userId =  GlobalInfo.getInstance().getProfile().getUserData().id + "";
    				shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
    			}
    				break;
    				
    			default:{
    				//other role login, view detail report
    				userId =  param.getString(IntentConstants.INTENT_STAFF_ID);
    				shopId = param.getString(IntentConstants.INTENT_SHOP_ID);
    				userName  =  param.getString(IntentConstants.INTENT_STAFF_NAME);
    			}
    				break;
			}
		}
	}
	
	
	private LinearLayout llHeaderForNVBH;
	private LinearLayout llHeaderForGSBH;
	private TextView tvSaleDate;
	private TextView tvStaffName;
		
	/**
	 * Get view  from layout
	 * @author: duongdt3
	 * @since: 10:58:17 16 Dec 2013
	 * @return: void
	 * @throws:  
	 * @param view
	 */
	private void initView(View view) {
		//for NVBH
		llHeaderForNVBH = (LinearLayout) view.findViewById(R.id.llHeaderForNVBH);
		tvNumDaySalePlan = (TextView) view.findViewById(R.id.tvNumDaySalePlan);
		tvNumDaySold = (TextView) view.findViewById(R.id.tvNumDaySold);
		tvSoldPercent = (TextView) view.findViewById(R.id.tvSoldPercent);
		
		//header info for GST detail month view
		LinearLayout llGSTMonthDetail = (LinearLayout) view.findViewById(R.id.llGSTMonthDetail);
		llGSTMonthDetail.setVisibility(View.GONE);
		
		//for GSBH
		llHeaderForGSBH = (LinearLayout) view.findViewById(R.id.llHeaderForGSBH);
		tvSaleDate = (TextView) view.findViewById(R.id.tvSaleDate);
		tvStaffName = (TextView) view.findViewById(R.id.tvStaffName);
		
		tbStatistics = (DMSTableView) view.findViewById(R.id.tbStatistics);
		//Moi role co 1 loai Row va header khac nhau
		switch (typeRequest) {
    		case TYPE_REQUEST_NVBH:
    		case TYPE_REQUEST_GSBH:{
    			initHeaderTable(tbStatistics,  new NVBHGeneralStatisticsRow(parent, this, false, true));
    			}
    			break;
    		case TYPE_REQUEST_GST_DATE_DETAIL:{
    			initHeaderTable(tbStatistics, new NVBHGeneralStatisticsDateDetailRow(parent, this));
    		}
    			break;
    		case TYPE_REQUEST_GST_MONTH_DETAIL:{
    			initHeaderTable(tbStatistics, new NVBHGeneralStatisticsMonthDetailRow(parent, this));
    		}
    			break;
    		default:
    			break;
		}
		
//		tbStatistics.getHeaderView().addHeader(headerInfo);
		//set no content text for table
		tbStatistics.showNoContentRow();
		
		//hide and show right info header
		switch (typeRequest) {
    		case TYPE_REQUEST_NVBH:{
    			llHeaderForNVBH.setVisibility(View.VISIBLE);
    			llHeaderForGSBH.setVisibility(View.GONE);
    		}
    		break;
    		//hiện chi tiến độ
    		case TYPE_REQUEST_GST_MONTH_DETAIL:{
    			llGSTMonthDetail.setVisibility(View.VISIBLE);
    			tvSoldPercent = (TextView) view.findViewById(R.id.tvSoldPercentGSTMonthDetail);
    		}
    		//sau đó đổ xuống ẩn hiện menu header info cho đúng
    		case TYPE_REQUEST_GSBH:
    		case TYPE_REQUEST_GST_DATE_DETAIL:{
    			llHeaderForNVBH.setVisibility(View.GONE);
    			llHeaderForGSBH.setVisibility(View.VISIBLE);
    		}
    			break;
    		default:
    			break;
		}	
	}
	
	/**
	 * Set header text
	 * @author: duongdt3
	 * @since: 10:58:30 16 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void initHeaderText() {
		String title = "";
		Spanned span = null;
		switch (typeRequest) {
    		case TYPE_REQUEST_NVBH: {
    			title = StringUtil.getString(R.string.TEXT_HEADER_TITLE_GENERAL_STATISTICS);
    		}
    			break;
    		case TYPE_REQUEST_GSBH: {
    			// thêm ngày tháng năm vào title
    			String strBold = StringUtil.getBoldText(DateUtils.getCurrentDate());
    			title = StringUtil.getString(R.string.TEXT_TITLE_REPORT_PROGRESS_DATE) + " " + strBold;
    		}
    			break;
    		case TYPE_REQUEST_GST_DATE_DETAIL: {
    			// thêm ngày tháng năm vào title
    			String strBold = StringUtil.getBoldText(DateUtils.getCurrentDate());
    			title = StringUtil.getString(R.string.TEXT_TITLE_REPORT_PROGRESS_DATE_GST_DETAIL_NVBH) + " " + strBold;
    		}
    			break;
    		case TYPE_REQUEST_GST_MONTH_DETAIL: {
    			// thêm ngày tháng năm vào title
    			String strBold = StringUtil.getBoldText(DateUtils.getCurrentDate());
    			title = StringUtil.getString(R.string.TEXT_TITLE_REPORT_PROGRESS_DATE_GST_DETAIL_MONTH_NVBH) + " " + strBold;
    		}
    			break;
    		default:
    			break;
		}
		span = StringUtil.getHTMLText(title);
		setTitleHeaderView(span);

	}

	/**
	 * Set header menu
	 * @author: duongdt3
	 * @since: 10:59:48 16 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void initHeaderMenu() {
		switch (typeRequest) {
    		case TYPE_REQUEST_NVBH: {
    			// enable menu bar
    			enableMenuBar(this, FragmentMenuContanst.NVBH_REPORT_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_NVBH_GENERAL_STATISTICS);
    		}
    			break;
    		case TYPE_REQUEST_GSBH: {
    			// enable menu bar for GSBH
    			enableMenuBar(this, FragmentMenuContanst.GSBH_REPORT_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_GSNPP_GENERAL_STATISTICS);
    		}
    			break;
    		case TYPE_REQUEST_GST_DATE_DETAIL: {
    			enableMenuBar(this, FragmentMenuContanst.GST_REPORT_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_GSBH_GENERAL_STATISTICS_DATE);
    		}
    			break;
    		case TYPE_REQUEST_GST_MONTH_DETAIL: {
    			enableMenuBar(this, FragmentMenuContanst.GST_REPORT_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_GSBH_GENERAL_STATISTICS_MONTH);
    		}
    			break;
    		default:
    			break;
		}
	}
	
	/**
	 * request data on first time
	 * @author: duongdt3
	 * @since: 10:59:57 16 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void initData() {
		//show info in header
		switch (typeRequest) {
    		case TYPE_REQUEST_NVBH: {
    			// info get in request data (progress sold)
    			tvNumDaySalePlan.setText("0");
    			tvNumDaySold.setText("0");
    			tvSoldPercent.setText("0%");
    		}
    			break;
    
    		default: {
    
    			String currentDate = DateUtils.getDayOfWeek() + ", " + DateUtils.getCurrentDate();
    			// thêm ngày tháng năm, staff name in info header
    			tvSaleDate.setText(currentDate);
    			tvStaffName.setText(userName);
    		}
    			break;
		}
		requestData();
	}
	
	/**
	 * request data from DB
	 * @author: duongdt3
	 * @since: 11:00:03 16 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void requestData() {
		//show dialog loading... 
		this.parent.showProgressDialog(StringUtil.getString(R.string.loading));
		
		//create request
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_STAFF_ID, userId);
		data.putString(IntentConstants.INTENT_SHOP_ID, shopId);
		data.putInt(IntentConstants.INTENT_NUM_ITEM_IN_PAGE, NUM_ITEM_PER_PAGE);
		
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_GENERAL_STATISTICS_REPORT_INFO_FOR_SALEMAN;
		//send request
		SaleController.getInstance().handleViewEvent(e);
	}
	
	/**
	 * render progress info header
	 * @author: duongdt3
	 * @since: 11:00:14 16 Dec 2013
	 * @return: void
	 * @throws:  
	 * @param info
	 */
	private void renderProgress(ProgressSoldViewDTO info){
		if (info != null) {
			String strNumberDayPlan = info.numberDayPlan + "";
			tvNumDaySalePlan.setText(strNumberDayPlan);
			
			String strNumDaySold = info.numberDaySold + "";
			tvNumDaySold.setText(strNumDaySold);
			
			String strSoldPercent = StringUtil.parseProgressSold(info.progressSold);
			tvSoldPercent.setText(strSoldPercent);
		}
	}
	
	/**
	 * render table layout 
	 * @author: duongdt3
	 * @since: 11:00:20 16 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		if (dto != null) {
			tbStatistics.clearAllData();
			if(dto.progressSoldInfo != null){
				//render progress
				renderProgress(dto.progressSoldInfo);
				progressSold = dto.progressSoldInfo.progressSold;
			}
			
			List<DMSTableRow> listRow = new ArrayList<DMSTableRow>();
			boolean isColorRemainColumn = typeRequest != TYPE_REQUEST_NVBH; 
			if(dto.listInfo != null){
				GeneralStatisticsInfo infoTotal = new GeneralStatisticsInfo();
				
				int stt = StringUtil.getSTT(1, NUM_ITEM_PER_PAGE);
				for (int i = 0; i < dto.listInfo.size(); i++) {
					GeneralStatisticsInfo info = dto.listInfo.get(i);
					info.tvSTT = stt;
					info.checkHightLightByProgress(progressSold);
					//cal total
					infoTotal.calTotal(info);
					
					//Moi role co 1 loai Row va header khac nhau
					switch (typeRequest) {
    					case TYPE_REQUEST_GST_DATE_DETAIL: {
    						NVBHGeneralStatisticsDateDetailRow row = new NVBHGeneralStatisticsDateDetailRow(parent, this);
    						row.render(info);
    						// add to list row
    						listRow.add(row);
    						tbStatistics.addRow(row);
    					}
    						break;
    					case TYPE_REQUEST_GST_MONTH_DETAIL: {
    						NVBHGeneralStatisticsMonthDetailRow row = new NVBHGeneralStatisticsMonthDetailRow(parent, this);
    						row.render(info);
    						// add to list row
    						listRow.add(row);
    						tbStatistics.addRow(row);
    					}
    						break;
    					default: {
    						// Cac truong hop khac dung NVBHGeneralStatisticsRow
    						NVBHGeneralStatisticsRow row = new NVBHGeneralStatisticsRow(parent, this, isColorRemainColumn, false);
    						row.render(info);
    						// add to list row
    						listRow.add(row);
    						tbStatistics.addRow(row);
    					}
    						break;
    				}
					//tang stt
					stt ++;
				}
				
				//add total row
				if (listRow.size() > 0) {
					//Role GST coi chi tiet khong co tong trong ngay
					switch (typeRequest) {
    					case TYPE_REQUEST_GST_DATE_DETAIL:
    					case TYPE_REQUEST_GST_MONTH_DETAIL:
    						break;

    					default: {
    						NVBHGeneralStatisticsRow row = new NVBHGeneralStatisticsRow(parent, this, isColorRemainColumn, true);
    						infoTotal.checkHightLightByProgress(progressSold);
    						row.render(infoTotal);
    						tbStatistics.addRow(row);
    					}
    						break;
					}
				}
			}
		}
	}
		
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		//close Progress Dialog
		this.parent.closeProgressDialog();
		
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.GET_GENERAL_STATISTICS_REPORT_INFO_FOR_SALEMAN:
			dto = (GeneralStatisticsViewDTO) modelEvent.getModelData();
			renderLayout();
			switch (typeRequest) {
			case NVBHGeneralStatisticsView.TYPE_REQUEST_GST_DATE_DETAIL: {
				requestInsertLogKPI(HashMapKPI.GSBH_BAOCAOCHITIETTIENDONGAYTHEONVBH, modelEvent.getActionEvent().startTimeFromBoot);
			}
				break;
			case NVBHGeneralStatisticsView.TYPE_REQUEST_GST_MONTH_DETAIL: {
				requestInsertLogKPI(HashMapKPI.GSBH_BAOCAOCHITIETTIENDOLUYKETHEOGSNPP, modelEvent.getActionEvent().startTimeFromBoot);
			}
				break;
			default:
				break;
			}
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}
	
	
	//error request
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
