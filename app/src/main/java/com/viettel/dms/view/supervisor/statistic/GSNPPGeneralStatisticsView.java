/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor.statistic;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.FragmentMenuContanst;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.dto.view.GSBHGeneralStatisticsViewDTO;
import com.viettel.dms.dto.view.GSBHGeneralStatisticsViewDTO.ShopItemDTO;
import com.viettel.dms.dto.view.GSBHGeneralStatisticsViewDTO.StaffItemDTO;
import com.viettel.dms.dto.view.GSBHGeneralStatisticsViewDTO.StaffsOfShopInfo;
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
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.sale.statistic.NVBHGeneralStatisticsView;
import com.viettel.dms.view.tbhv.statistic.GSBHGeneralStatisticsDateRow;
import com.viettel.dms.view.tbhv.statistic.GSBHGeneralStatisticsMonthRow;
import com.viettel.sabeco.R;

/**
 * Báo cáo doanh số trong ngày và trong tháng của GSBH
 * GeneralStatisticsSupervisorView.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  10:34:59 22 Nov 2013
 */
public class GSNPPGeneralStatisticsView extends BaseFragment implements OnEventControlListener, VinamilkTableListener, OnItemSelectedListener {
	//TAG of fragment
	public static final String TAG = GSNPPGeneralStatisticsView.class.getName();
	//parent Activity
	private GlobalBaseActivity parent;
			
	//BEGIN USER INFO
	private String userId;
	private String shopId;
	private String parrentStaffId;
	private int typeRequest = NVBHGeneralStatisticsView.TYPE_REQUEST_GSBH;
	
	//for GST view detail
	private List<StaffsOfShopInfo> listStaffInShop;
	private String userIdSelected;
	private String shopIdSelected;
	//END USER INFO
	
	//BEGIN REPORT INFO
	private float progressSold = 0;
	//END REPORT INFO
	
	//BEGIN PAGING
	//Num row in page
	private static final int NUM_ITEM_PER_PAGE = Constants.NUM_ITEM_PER_PAGE;
	//END PAGING
	
	private static final int ACTION_STAFF_CODE = 1;
	private static final int ACTION_SHOW_DETAIL_INFO = 2;
	private static final int ACTION_SHOW_DETAIL_DATE_INFO = 3;
	private static final int ACTION_SHOW_DETAIL_MONTH_INFO = 4;
	
	//BEGIN DECLARE
	private TextView tvNumDaySalePlan;
	private TextView tvNumDaySold;
	private TextView tvSoldPercent;
	private DMSTableView tbStatistics;
	private LinearLayout llHeaderGSBH;
	
	//for GST view detail
	private LinearLayout llHeaderGST;
	private Spinner spinnerNPP;
	private Spinner spinnerGSBH;
	
	private List<ShopItemDTO> arrShop = new ArrayList<ShopItemDTO>();
	List<StaffItemDTO> arrStaff = new ArrayList<StaffItemDTO>();
	//END DECLARE
	
	//BEGIN NEW INSTANCE
	public static GSNPPGeneralStatisticsView newInstance(Bundle b) {
		GSNPPGeneralStatisticsView instance = new GSNPPGeneralStatisticsView();
		instance.setArguments(b);
		return instance;
	}

	public static GSNPPGeneralStatisticsView getInstance(Bundle b) {
		return newInstance(b);
	}
	//END NEW INSTANCE
	
	//BEGIN DTO
	private GSBHGeneralStatisticsViewDTO dto = null;  
	//END DTO		
	private int currentIndexNPP = -1;
	private int currentIndexGSBH = -1;
	private String userIdSelectedFirst;
	private String shopIdSelectedFirst;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_gsbh_general_report_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		
		initUserInfo();
		initView(v);
		initHeaderText();
		initHeaderMenu();
		initData();
		
		return v;
	}
	
	private void initUserInfo() {
		Bundle param = getArguments();
		if (param != null) {
			typeRequest  = param.getInt(IntentConstants.INTENT_TYPE_REQUEST, NVBHGeneralStatisticsView.TYPE_REQUEST_GSBH);
			switch (typeRequest) {
    			//case GSBH login
    			case NVBHGeneralStatisticsView.TYPE_REQUEST_GSBH: {
    				userId =  String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id) ;
    				shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
    				parrentStaffId = Constants.STR_BLANK;
    			}
    				break;
    			default:{
    				//case GST login, view detail report
    				userIdSelectedFirst =  param.getString(IntentConstants.INTENT_STAFF_ID,  Constants.STR_BLANK);
    				//first staff id GSBH get from GST
    				shopIdSelectedFirst = param.getString(IntentConstants.INTENT_SHOP_ID, Constants.STR_BLANK);
    				
    				userIdSelected = userIdSelectedFirst;
    				shopIdSelected = shopIdSelectedFirst;
    				//mac dinh lay tat ca shop, staff_owner_id
    				userId = Constants.STR_BLANK;
    				shopId = Constants.STR_BLANK;
    				parrentStaffId = param.getString(IntentConstants.INTENT_PARENT_STAFF_ID);
    			}
    				break;
    		}	
		}
	}
	
	private void initView(View view) {
		llHeaderGSBH = (LinearLayout) view.findViewById(R.id.llHeaderGSBH);
		tvNumDaySalePlan = (TextView) view.findViewById(R.id.tvNumDaySalePlan);
		tvNumDaySold = (TextView) view.findViewById(R.id.tvNumDaySold);
		//header danh rieng cho month
		LinearLayout llMonthDetail = (LinearLayout) view.findViewById(R.id.llMonthDetail);
		
		//neu la detail cua GST thi se lay tv tvSoldPercent o header panel llHeaderGST
		if (typeRequest == NVBHGeneralStatisticsView.TYPE_REQUEST_GST_MONTH_DETAIL) {
			tvSoldPercent = (TextView) view.findViewById(R.id.tvSoldPercentDetailMonth);
		}else{
			llMonthDetail.setVisibility(View.GONE);
			tvSoldPercent = (TextView) view.findViewById(R.id.tvSoldPercent);
		}
		
		tbStatistics = (DMSTableView) view.findViewById(R.id.tbStatistics);
		//Mỗi loại request có 1 header + row riêng
		switch (typeRequest) {
    		case NVBHGeneralStatisticsView.TYPE_REQUEST_GSBH: {
    			initHeaderTable(tbStatistics, new GSNPPGeneralStatisticsRow(parent, this, false, ACTION_STAFF_CODE, ACTION_SHOW_DETAIL_INFO));
    		}
    		break;
    		case NVBHGeneralStatisticsView.TYPE_REQUEST_GST_DATE_DETAIL: {
    			GSBHGeneralStatisticsDateRow gsbhHeaderInfo = new GSBHGeneralStatisticsDateRow(parent, this, false, -1, -1);
    			gsbhHeaderInfo.renderHeaderGSBHDateDetail();
    			initHeaderTable(tbStatistics, gsbhHeaderInfo);
    		}
    			break;
    		case NVBHGeneralStatisticsView.TYPE_REQUEST_GST_MONTH_DETAIL:{
    			GSBHGeneralStatisticsMonthRow gsbhHeaderInfo = new GSBHGeneralStatisticsMonthRow(parent, this, false, -1);
    			gsbhHeaderInfo.renderHeaderGSBHMonth();
    			initHeaderTable(tbStatistics, gsbhHeaderInfo);
    		}
    			break;
    		default:
    			break;
		}
		
		//info for GST
		llHeaderGST = (LinearLayout) view.findViewById(R.id.llHeaderGST);
		spinnerNPP = (Spinner) view.findViewById(R.id.spinnerNPP);
		spinnerGSBH = (Spinner) view.findViewById(R.id.spinnerGSBH);
		
		//Hide, show right header info with role TYPE_REQUEST_GSBH | TYPE_REQUEST_GST
		switch (typeRequest) {
    		case NVBHGeneralStatisticsView.TYPE_REQUEST_GSBH: {
    			llHeaderGST.setVisibility(View.GONE);
    		}
    			break;
    		case NVBHGeneralStatisticsView.TYPE_REQUEST_GST_MONTH_DETAIL:
    		case NVBHGeneralStatisticsView.TYPE_REQUEST_GST_DATE_DETAIL: {
    			spinnerNPP.setOnItemSelectedListener(this);
    			spinnerGSBH.setOnItemSelectedListener(this);
    			llHeaderGSBH.setVisibility(View.GONE);
    		}
    			break;
		}
	}
	
	private void initHeaderText() {
		String title = "";
		//thêm ngày tháng năm vào title
		String strBold = StringUtil.getBoldText( DateUtils.getCurrentDate() );
		switch (typeRequest) {
    		case NVBHGeneralStatisticsView.TYPE_REQUEST_GSBH: {
    			title = StringUtil.getString(R.string.TEXT_TITLE_REPORT_PROGRESS_DATE) + " "+ strBold ;
    		}
    			break;
    		case NVBHGeneralStatisticsView.TYPE_REQUEST_GST_DATE_DETAIL:{
    			title = StringUtil.getString(R.string.TEXT_TITLE_REPORT_PROGRESS_DATE_GST_DETAIL_GSBH)+ " " + strBold ;
    		}
    			break;
    		case NVBHGeneralStatisticsView.TYPE_REQUEST_GST_MONTH_DETAIL:{
    			title = StringUtil.getString(R.string.TEXT_TITLE_REPORT_PROGRESS_DATE_GST_DETAIL_MONTH_GSBH)+ " " + strBold ;
    		}
    			break;
    		default:
    			break;
		}
		
		Spanned span = StringUtil.getHTMLText(title);
		setTitleHeaderView(span);
	}
	
	private void initHeaderMenu() {
		switch (typeRequest) {
    		case NVBHGeneralStatisticsView.TYPE_REQUEST_GSBH: {
    			// enable menu bar for GSBH
    			enableMenuBar(this, FragmentMenuContanst.GSBH_REPORT_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_GSNPP_GENERAL_STATISTICS);
    		}
    			break;
    		case NVBHGeneralStatisticsView.TYPE_REQUEST_GST_DATE_DETAIL: {
    			// enable menu bar for GST date view
    			enableMenuBar(this, FragmentMenuContanst.GST_REPORT_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_GSBH_GENERAL_STATISTICS_DATE);
    		}
    			break;
    		case NVBHGeneralStatisticsView.TYPE_REQUEST_GST_MONTH_DETAIL: {
    			// enable menu bar for GST month view
    			enableMenuBar(this, FragmentMenuContanst.GST_REPORT_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_GSBH_GENERAL_STATISTICS_MONTH);
    		}
    			break;
    		default:
    			break;
		}
	}
	/**
	 * Check when load list GSBH for GST detail view
	 * @author: duongdt3
	 * @since: 14:09:43 20 Dec 2013
	 * @return: boolean
	 * @throws:  
	 * @return
	 */
	boolean checkLoadListGsbhForGstDetailView(){
		boolean isLoadListGSBH = false;
		switch (typeRequest) {
    		case NVBHGeneralStatisticsView.TYPE_REQUEST_GSBH: {
    			// GSBH ko load list GSBH render Spinner
    			isLoadListGSBH = false;
    		}
    			break;
    		case NVBHGeneralStatisticsView.TYPE_REQUEST_GST_DATE_DETAIL:
    		case NVBHGeneralStatisticsView.TYPE_REQUEST_GST_MONTH_DETAIL: {
    			isLoadListGSBH = (listStaffInShop == null);
    		}
    			break;
    		default:
    			break;
		}
		return isLoadListGSBH;
	}
	/**
	 * request data on first time
	 * @author: duongdt3
	 * @since: 15:13:14 12 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void initData() {
		//khong render
		progressSold = 0;
		listStaffInShop = null;
		
		boolean isLoadListGSBH = checkLoadListGsbhForGstDetailView();
		
		requestData(isLoadListGSBH);
	}
	
	/**
	 * get data from DB
	 * @author: duongdt3
	 * @since: 14:04:22 27 Dec 2013
	 * @return: void
	 * @throws:  
	 * @param isLoadListGsbhForGSTView
	 */
	private void requestData(boolean isLoadListGsbhForGSTView) {
		//show dialog loading...
		this.parent.showProgressDialog(StringUtil.getString(R.string.loading));
		//create request
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		String uID = typeRequest == NVBHGeneralStatisticsView.TYPE_REQUEST_GSBH ? userId : userIdSelected;
		String sID = typeRequest == NVBHGeneralStatisticsView.TYPE_REQUEST_GSBH ? shopId : shopIdSelected;
		data.putString(IntentConstants.INTENT_STAFF_ID, uID);
		data.putString(IntentConstants.INTENT_SHOP_ID, sID);
		data.putString(IntentConstants.INTENT_PARENT_STAFF_ID, parrentStaffId);
		data.putInt(IntentConstants.INTENT_NUM_ITEM_IN_PAGE, NUM_ITEM_PER_PAGE);
		//neu can load listStaffInShop, khi listStaffInShop = null moi load
		data.putBoolean(IntentConstants.INTENT_IS_LOAD_STAFF_LIST, isLoadListGsbhForGSTView);
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_GENERAL_STATISTICS_REPORT_INFO_FOR_SUPERVIOR;
		//send request
		SaleController.getInstance().handleViewEvent(e);
	}
	
	/**
	 * render progress info
	 * @author: duongdt3
	 * @since: 15:13:36 12 Dec 2013
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
	
	private void renderLayout() {
		tbStatistics.clearAllData();
		if (dto != null) {
			//RENDER HEADER INFO PANEL
			if(dto.progressSoldInfo != null){
				//render progress
				renderProgress(dto.progressSoldInfo);
				progressSold = dto.progressSoldInfo.progressSold;
			}
			
			//list staff in shop for GST request
			if(dto.listStaffInShop != null){
				listStaffInShop = dto.listStaffInShop;
				//render progress
				renderSpinnerListStaffInShop();
			}
			
			//RENDER TABLE
			List<DMSTableRow> listRow = new ArrayList<DMSTableRow>();
			
			if(dto.listInfo != null){
				GeneralStatisticsInfo infoTotal = new GeneralStatisticsInfo();
				
				int stt = StringUtil.getSTT(1, NUM_ITEM_PER_PAGE);
				for (int i = 0; i < dto.listInfo.size(); i++) {
					GeneralStatisticsInfo info = dto.listInfo.get(i);
					info.tvSTT = stt;
					info.checkHightLightByProgress(progressSold);
					//cal total
					infoTotal.calTotal(info);
					
					//Mỗi loại request có 1 header + row riêng
					switch (typeRequest) {
    					case NVBHGeneralStatisticsView.TYPE_REQUEST_GSBH: {
    						GSNPPGeneralStatisticsRow row = new GSNPPGeneralStatisticsRow(parent, this, false, ACTION_STAFF_CODE, ACTION_SHOW_DETAIL_INFO);
    						row.render(info);
    						// add to list row
    						listRow.add(row);
    						tbStatistics.addRow(row);
    					}
    						break;
    					case NVBHGeneralStatisticsView.TYPE_REQUEST_GST_DATE_DETAIL: {
    						GSBHGeneralStatisticsDateRow row = new GSBHGeneralStatisticsDateRow(parent, this, false, ACTION_SHOW_DETAIL_DATE_INFO, ACTION_SHOW_DETAIL_DATE_INFO);
    						row.render(info);
    						// add to list row
    						listRow.add(row);
    						tbStatistics.addRow(row);
    					}
    						break;
    					case NVBHGeneralStatisticsView.TYPE_REQUEST_GST_MONTH_DETAIL: {
    						GSBHGeneralStatisticsMonthRow row = new GSBHGeneralStatisticsMonthRow(parent, this, false, ACTION_SHOW_DETAIL_MONTH_INFO);
    						row.render(info);
    						// add to list row
    						listRow.add(row);
    						tbStatistics.addRow(row);
    					}
    						break;
    					default:
    						break;
					}
					//tang stt
					stt ++;
				}
				
				//add total row
				if (listRow.size() > 0) {
					// Mỗi loại request có 1 header + row total riêng
					switch (typeRequest) {
    					case NVBHGeneralStatisticsView.TYPE_REQUEST_GSBH: {
    						GSNPPGeneralStatisticsRow row = new GSNPPGeneralStatisticsRow(parent, this, true, ACTION_STAFF_CODE, ACTION_SHOW_DETAIL_INFO);
    						infoTotal.checkHightLightByProgress(progressSold);
    						row.render(infoTotal);
    						listRow.add(row);
    						tbStatistics.addRow(row);
    					}
    						break;
    					case NVBHGeneralStatisticsView.TYPE_REQUEST_GST_DATE_DETAIL: {
    						GSBHGeneralStatisticsDateRow row = new GSBHGeneralStatisticsDateRow(parent, this, true, ACTION_SHOW_DETAIL_DATE_INFO, ACTION_SHOW_DETAIL_DATE_INFO);
    						row.render(infoTotal);
    						// add to list row
    						listRow.add(row);
    						tbStatistics.addRow(row);
    					}
    						break;
    					case NVBHGeneralStatisticsView.TYPE_REQUEST_GST_MONTH_DETAIL: {
    						GSBHGeneralStatisticsMonthRow row = new GSBHGeneralStatisticsMonthRow(parent, this, true, ACTION_SHOW_DETAIL_MONTH_INFO);
    						infoTotal.checkHightLightByProgress(progressSold);
    						row.render(infoTotal);
    						// add to list row
    						listRow.add(row);
    						tbStatistics.addRow(row);
    					}
    						break;
    
    					default:
    						break;
					}
				}
			}
		}
	}
	
	/**
	 * Render cho spinner NPP lan dau load du lieu
	 * @author: duongdt3
	 * @since: 16:24:56 20 Dec 2013
	 * @return: void
	 * @throws:
	 */
	void renderFirstSpinnerNPP(){
		arrShop = StaffsOfShopInfo.getShopArray(listStaffInShop , userIdSelected);
		List<String> listShopString = new ArrayList<String>();
		int indexSelected = -1;
		for (int i= 0; i< arrShop.size(); i++) {
			ShopItemDTO shopItem = arrShop.get(i); 
			//neu chua duoc thay doi && giong voi shop id minh chon ben man hinh GST
			if(indexSelected == -1 && shopIdSelected.equals(shopItem.shopId)){
				indexSelected = i;
			}
			listShopString.add(shopItem.toString());
		}

		String[] arrString = {};
		arrString = listShopString.toArray(arrString);
		SpinnerAdapter adapterShop = new SpinnerAdapter(parent, R.layout.simple_spinner_item , arrString);
		spinnerNPP.setAdapter(adapterShop);
		
		//sau khi set adapter xong -> select 1 NPP
		
		//neu la lan dau tien, phai chon NPP cho dung
		// tim thay 1 NPP giong voi yeu cau GST
		if (indexSelected >= 0) {
			currentIndexNPP = indexSelected;
			// chon dung NPP
			spinnerNPP.setSelection(indexSelected);
		}
		
		adapterShop.notifyDataSetChanged();
	}
	
	/**
	 * Render cho spinner GSBH lan dau tien  
	 * @author: duongdt3
	 * @since: 16:25:56 20 Dec 2013
	 * @return: void
	 * @throws:
	 */
	void renderFirstSpinnerGSBH(){
		//lay dang chon hien taik
		int indexSpinnerCurrentSelected = spinnerNPP.getSelectedItemPosition();
		if (indexSpinnerCurrentSelected >= 0 ) {
			arrStaff = StaffsOfShopInfo.getStaffArray(listStaffInShop, shopIdSelected);
			List<String> listStaffString = new ArrayList<String>();
			
			int indexGSBHSelected = -1;
			for (int i=0; i< arrStaff.size();i++) {
				StaffItemDTO staffItem = arrStaff.get(i);
				//get index of staff GSBH 
				if (indexGSBHSelected == -1 && userIdSelected.equals(staffItem.staffId)) {
					indexGSBHSelected = i;
				}
				listStaffString.add(staffItem.toString());
			}
			
			String[] arrStaffStr = {};
			arrStaffStr = listStaffString.toArray(arrStaffStr);
			
			currentIndexGSBH = indexGSBHSelected;
			
			SpinnerAdapter adapterStaff = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrStaffStr);
			spinnerGSBH.setAdapter(adapterStaff);
			
			//set pos
			if (indexGSBHSelected >= 0) {
				spinnerGSBH.setSelection(indexGSBHSelected);
			}
			adapterStaff.notifyDataSetChanged();
		}
	}
	
	/**
	 * render spinner staff + spinner npp	
	 * @author: duongdt3
	 * @since: 17:36:03 29 Nov 2013
	 * @return: void
	 * @throws:  
	 */
	private void renderSpinnerListStaffInShop() {
		// --------------begin rendering spinner NPP--------------
		// Spinner DS NPP
		renderFirstSpinnerNPP();
		renderFirstSpinnerGSBH();
		// --------------end rendering spinner NPP--------------
	}

	//success request
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		//close Progress Dialog
		this.parent.closeProgressDialog();
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.GET_GENERAL_STATISTICS_REPORT_INFO_FOR_SUPERVIOR:
			dto = (GSBHGeneralStatisticsViewDTO) modelEvent.getModelData();
			renderLayout();
			switch (typeRequest) {
			case NVBHGeneralStatisticsView.TYPE_REQUEST_GSBH: {
				requestInsertLogKPI(HashMapKPI.GSNPP_BAOCAOTIENDONGAY, modelEvent.getActionEvent().startTimeFromBoot);
			}
				break;
			case NVBHGeneralStatisticsView.TYPE_REQUEST_GST_DATE_DETAIL: {
				requestInsertLogKPI(HashMapKPI.GSBH_BAOCAOCHITIETTIENDONGAYTHEOGSNPP, modelEvent.getActionEvent().startTimeFromBoot);
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
	
	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		//close Progress Dialog
		this.parent.closeProgressDialog();
		//show Error Message
		this.parent.showDialog(StringUtil.getString(R.string.MESSAGE_ERROR_HAPPEN));
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
		if (data instanceof GeneralStatisticsInfo ) {
			GeneralStatisticsInfo info = (GeneralStatisticsInfo) data;
			switch (action) {
    			case ACTION_STAFF_CODE:{
    				ActionEvent e = new ActionEvent();
    				//click on staff code
    				//show info of staff
    				Bundle b = new Bundle();
    				b.putString(IntentConstants.INTENT_STAFF_CODE, info.tvObjectCode);
    				b.putString(IntentConstants.INTENT_STAFF_ID, String.valueOf(info.tvObjectId));
    				b.putString(IntentConstants.INTENT_STAFF_PHONE, String.valueOf(info.tvStaffMobile));
    				b.putString(IntentConstants.INTENT_STAFF_NAME, String.valueOf(info.tvObjectName));
    				e = new ActionEvent();
    				e.action = ActionEventConstant.STAFF_INFORMATION;
    				e.sender = this;
    				e.viewData = b;
    				SuperviorController.getInstance().handleSwitchFragment(e);
    			}
    				break;
    			case ACTION_SHOW_DETAIL_MONTH_INFO:
    				//show month detail view NVBH of GST
    			case ACTION_SHOW_DETAIL_DATE_INFO:
    				//show date detail view NVBH of GST
    			case ACTION_SHOW_DETAIL_INFO:{
    				//click search image
    				//show detail view NVBH of GSBH
    				ActionEvent e = new ActionEvent();
    				e.sender = this;
    				Bundle param = new Bundle();
    				param.putInt(IntentConstants.INTENT_TYPE_REQUEST, typeRequest);
    				param.putString(IntentConstants.INTENT_STAFF_ID, info.tvObjectId); 
    				param.putString(IntentConstants.INTENT_SHOP_ID, info.tvShopId);
    				param.putString(IntentConstants.INTENT_STAFF_NAME, info.tvObjectName);
    				e.viewData = param;
    				e.action = ActionEventConstant.GO_TO_GENERAL_STATISTICS_DETAIL;
    				SuperviorController.getInstance().handleSwitchFragment(e);
    			}
    				break;
    			default:
    				break;
			}
		}
	}
	
	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				//reset combobox to First - First
				userIdSelected = userIdSelectedFirst;
				shopIdSelected = shopIdSelectedFirst;
				
				//Re request data
				initData();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
	
	/**
	 * Render list GSNPP from 1 NPP
	 * @author: duongdt3
	 * @since: 14:01:24 27 Dec 2013
	 * @return: void
	 * @throws:  
	 * @param currentPos
	 */
	void renderSpinnerGsnppFromNPP(int currentPos){
		shopIdSelected = arrShop.get(currentPos).shopId;
		currentIndexNPP = currentPos;
		//get list 
		arrStaff = StaffsOfShopInfo.getStaffArray(listStaffInShop, shopIdSelected);
		List<String> listStaffString = new ArrayList<String>();
		int indexSelected = -1;
		for (int i = 0; i < arrStaff.size(); i++) {
			StaffItemDTO staffItem = arrStaff.get(i);
			//neu chua duoc thay doi && giong voi staff id dang duoc chon
			if(userIdSelected.equals(staffItem.staffId)){
				indexSelected = i;
			}
			listStaffString.add(staffItem.toString());
		}

		String[] arrStaffStr = {};
		arrStaffStr = listStaffString.toArray(arrStaffStr);
		SpinnerAdapter adapterStaff = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrStaffStr);
		spinnerGSBH.setAdapter(adapterStaff);
		
		if (indexSelected >= 0) {
			currentIndexGSBH = indexSelected;
			// chon dung NPP
			spinnerGSBH.setSelection(indexSelected);
		}
		
		requestData(false);
	}
	
	/**
	 * Render list NPP from 1 GSNPP
	 * @author: duongdt3
	 * @since: 14:01:42 27 Dec 2013
	 * @return: void
	 * @throws:  
	 * @param currentPos
	 */
	void renderSpinnerNppFromGSNPP(int currentPos){
		//get STAFF_ID of selected GSNPP 
		userIdSelected = arrStaff.get(currentPos).staffId;
		currentIndexGSBH = currentPos;
		//get list shop of GSNPP
		arrShop = StaffsOfShopInfo.getShopArray(listStaffInShop, userIdSelected);
		
		//render spinner NPP
		List<String> listShopString = new ArrayList<String>();
		int indexSelected = -1;
		for (int i= 0; i< arrShop.size(); i++) {
			ShopItemDTO shopItem = arrShop.get(i); 
			//neu chua duoc thay doi && giong voi shop id minh chon ben man hinh GST
			if(shopIdSelected.equals(shopItem.shopId)){
				indexSelected = i;
			}
			listShopString.add(shopItem.toString());
		}

		String[] arrString = {};
		arrString = listShopString.toArray(arrString);
		SpinnerAdapter adapterShop = new SpinnerAdapter(parent, R.layout.simple_spinner_item , arrString);
		spinnerNPP.setAdapter(adapterShop);
		
		// tim thay 1 NPP cu, da select truoc do, 
		//tranh spinner NPP request danh sach GSNPP them lan nua
		if (indexSelected >= 0) {
			currentIndexNPP = indexSelected;
			// chon dung NPP
			spinnerNPP.setSelection(indexSelected);
		}
		/*else{
			//khong tim thay item lan truoc select
			if (arrString.length > 0) {
				currentIndexNPP = 0;
				// chon dung NPP
				spinnerNPP.setSelection(0);
				shopIdSelected = arrString[0];
			}else{
				currentIndexNPP = -1;
				shopIdSelected = "";
			}
		}*/
		//VTLog.e("renderSpinnerNppFromGSNPP",currentIndexNPP + " " +shopIdSelected);
		// request du lieu cho GSNPP + NPP dang chon
		requestData(false);
	}
	@Override
	public void onItemSelected(AdapterView<?> spinner, View v, int currentPos, long id) {
		
		int indexNPP = spinnerNPP.getSelectedItemPosition();
		int indexGSBH = spinnerGSBH.getSelectedItemPosition();
		// Load Spinner DS GSBH cua NPP
		if (spinner == spinnerNPP && currentIndexNPP != indexNPP) {
			//VTLog.e("onItemSelected", "renderSpinnerGsnppFromNPP npp: " + indexNPP  + " " + arrShop.get(indexNPP).shopName + " gsnpp: " + indexGSBH + " " + arrStaff.get(indexGSBH).staffName);
			renderSpinnerGsnppFromNPP(currentPos);
		} else if (spinner == spinnerGSBH && currentIndexGSBH != indexGSBH ) {
			//VTLog.e("onItemSelected", "renderSpinnerNppFromGSNPP npp: " + indexNPP + " gsnpp: " + indexGSBH);
			renderSpinnerNppFromGSNPP(currentPos);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
