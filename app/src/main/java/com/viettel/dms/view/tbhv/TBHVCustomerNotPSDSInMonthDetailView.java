/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.FragmentMenuContanst;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.db.ShopDTO;
import com.viettel.dms.dto.view.CustomerNotPsdsInMonthReportViewDTO;
import com.viettel.dms.dto.view.CustomerNotPsdsInMonthReportViewDTO.CustomerNotPsdsInMonthReportItem;
import com.viettel.dms.dto.view.ListStaffDTO;
import com.viettel.dms.dto.view.ListStaffDTO.StaffItem;
import com.viettel.dms.dto.view.TBHVNoPSDSDetailDTO;
import com.viettel.dms.dto.view.VisitDTO;
import com.viettel.dms.dto.view.VisitDTO.VisitItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.supervisor.VisitRow;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * Hiển thị danh sách chưa PSDS của TBHV khi xem chi tiet
 * CustomerNotPSDSInMonthGSBHView.java
 * @author: QuangVT 
 */
public class TBHVCustomerNotPSDSInMonthDetailView extends BaseFragment implements
		OnItemSelectedListener, OnEventControlListener, OnClickListener,
		VinamilkTableListener {
	
	//-------------- Default --------------//
	// Tag(Phan biet vs fragment khac)
	public static final String TAG = TBHVCustomerNotPSDSInMonthDetailView.class.getName();
	// Parent(Activity)
	private GlobalBaseActivity parent; 
	// Vao man hinh lan dau tien hay kg?
	private boolean isFirst = true;
	
	//-------------- Constant --------------//
	// So luong item tren 1 trang
	private static final int NUM_ITEM_IN_PAGE = Constants.NUM_ITEM_PER_PAGE;
	// Su kien click 1 dong tren table
	private static final int ACTION_ROW_CLICK = 1; 

	//-------------- User Info --------------//
	// Id user
	private long userId;
	// Id shop
	private String shopId;
	
	//-------------- DTO --------------//
	// DTO danh sach KH chua PDSD
	private CustomerNotPsdsInMonthReportViewDTO dtoNoPSDS;
	// DTO danh sach KH chua PDSD
	private TBHVNoPSDSDetailDTO dtoListNPPAndChild;
	// DTO cac lan ghe tham 1 KH
	private VisitDTO dtoVisit;  
	// Danh sach NPP hien tai cua combobox
	private List<ShopDTO> dtoListNPP;
	// Danh sach GSBH hien tai cua combobox
	private ListStaffDTO dtoListGSBH;
	// Danh sach NVBH hien tai cua combobox
	private ListStaffDTO dtoListNVBH;
	
	//-------------- Table --------------//
	// Table danh sach KH chua PSDS
	private VinamilkTableView tbCustomer;
	// Table cac lan ghe tham
	private VinamilkTableView tbVisited;
	// Page hien tai cua bang tbCustomer
	private int currentPage = -1;
	
	//-------------- Spinner --------------//
	// Combobox tuyent
	private Spinner spNPP;
	// Combobox tuyent
	private Spinner spGSBH;
	// Combobox tuyent
	private Spinner spNVBH;   
	// Index Select Spinner NPP
	private int indexSelectNPP = 0;
	// Index Select Spinner GSBH
	private int indexSelectGSBH = 0;
	// Index Select Spinner NVBH
	private int indexSelectNVBH = 0;
	// danh sach NPP
	private String[] arrNPPChoose = null;
	// danh sach GSBH
	private String[] arrGSBHChoose = null;
	// danh sach NVBH
	private String[] arrNVBHChoose = null; 
	
	//-------------- Adapter --------------//
	private SpinnerAdapter adapterNVBH;
	private SpinnerAdapter adapterGSBH;
	private SpinnerAdapter adapterNPP;
	
	//-------------- TextView --------------//
	// Tv Cac lan ghe tham KH
	private TextView tvVisited;
	// Tv thong bao cac lan ghe tham KH(khi chua ghe tham KH)
	private TextView tvMessageVisited;
	// ScrollView
	private ScrollView mScrollView;
	
	//-------------- VARIABLES --------------// 
	private String shopIdNPP; // shopId cua NPP duoc chon
	private String staffOwnerId; // id cua GSBH duoc chon
	/**
	 * Lay the hien cua doi tuong
	 * 
	 * @author quangvt1
	 * @param b
	 * @return GSBHCustomerNotPSDSInMonthView
	 */
	public static TBHVCustomerNotPSDSInMonthDetailView getInstance(Bundle b) {
		TBHVCustomerNotPSDSInMonthDetailView instance = new TBHVCustomerNotPSDSInMonthDetailView();
		instance.setArguments(b);
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_tbhv_customer_not_psds_in_month_detail, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		
		Bundle data = getArguments(); 
		shopIdNPP    = data.getString(IntentConstants.INTENT_SHOP_ID);
		staffOwnerId = data.getString(IntentConstants.INTENT_STAFF_OWNER_ID);
		// Lay thong tin khach hang tu Profile
		initUserInfo();
		// Khoi tao Title Header
		final String title = StringUtil.getString(R.string.TEXT_CUSTOMER_NOT_PSDS_IN_MONTH_DETAIL_FOR_GST);
		setTitleHeaderView(title);
		
		// Khoi tao menu
		initHeadermenu();
		
		// Binding control va khoi tao
		initView(v); 
		
		if (isFirst) {
			isFirst = false; 
			// Lay danh sach NPP, GSBH, NVBH
			getListShopSupervisorAndStaff();
		} else { 
			//renderLayout();
		}
		
		return v;
	}

	/**
	 * Lay danh sach NPP, GSBH, NVBH
	 * 
	 * @author: QuangVT
	 * @since: 6:07:51 PM Dec 17, 2013
	 * @return: void
	 * @throws:
	 */
	private void getListShopSupervisorAndStaff() {
		// Show dialog loading...
		parent.showLoadingDialog();
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();  
		b.putString(IntentConstants.INTENT_STAFF_ID, String.valueOf(userId)); 
		e.viewData = b;
		e.action = ActionEventConstant.GET_LIST_SHOP_SUPERVISOR_STAFF;
		e.sender = this;
		TBHVController.getInstance().handleViewEvent(e);
	}

	/**
	 * Tao adapter cho Spinner Staff
	 * 
	 * @author quangvt1
	 */
	private void initSpNVBH() {
		arrNVBHChoose = new String[dtoListNVBH.arrList.size()];
		int i=0;
		for (StaffItem staffItem: dtoListNVBH.arrList) {
			if (!StringUtil.isNullOrEmpty(staffItem.code)) {
				arrNVBHChoose[i] = staffItem.code + "-" + staffItem.name;
			} else {
				arrNVBHChoose[i] = staffItem.name;
			}
			i++;
		}
		indexSelectNVBH = 0;
		adapterNVBH = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrNVBHChoose); 
		spNVBH.setAdapter(adapterNVBH);
		spNVBH.setSelection(0, false);
	}
	
	/**
	 * Tao adapter cho Spinner GSBH
	 * 
	 * @author quangvt1
	 */
	private void initSpGSBH() {
		arrGSBHChoose = new String[dtoListGSBH.arrList.size()];
		int i=0;
		for (StaffItem staffItem: dtoListGSBH.arrList) {
			if (!StringUtil.isNullOrEmpty(staffItem.code)) {
				arrGSBHChoose[i] = staffItem.code + "-" + staffItem.name;
			} else {
				arrGSBHChoose[i] = staffItem.name;
			}
			i++;
		} 
		indexSelectGSBH = 0;
		adapterGSBH = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrGSBHChoose); 
		spGSBH.setAdapter(adapterGSBH);
		spGSBH.setSelection(0, false);
	}
	
	/**
	 * Tao adapter cho Spinner NPP
	 * 
	 * @author quangvt1
	 */
	private void initSpNPP() {
		arrNPPChoose = new String[dtoListNPP.size()];
		int i=0;
		for (ShopDTO shop: dtoListNPP) {
			if(shop.shopName.equals(Constants.STR_BLANK)||StringUtil.isNullOrEmpty(shop.shopName)){
				arrNPPChoose[i] = shop.shopCode; 
			}else {
				arrNPPChoose[i] = shop.shopCode+" - "+ shop.shopName; 
			}
			i++;
		} 
		indexSelectNPP = 0;
		adapterNPP = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrNPPChoose); 
		spNPP.setAdapter(adapterNPP);
		spNPP.setSelection(0, false);
	} 
	 
	/**
	 * Khoi tao menu
	 * 
	 * @author quangvt1
	 */
	private void initHeadermenu() {
		enableMenuBar(this, FragmentMenuContanst.GST_REPORT_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_GSBH_REPORT_CUSTOMER_NOT_PSDS);
	}

	/**
	 * Binđing control va khoi tao view
	 * 
	 * @author quangvt1
	 * @param view
	 */
	private void initView(View view) {
		indexSelectNVBH = 0;
		indexSelectNPP  = 0;
		indexSelectGSBH = 0;
		
		spNPP = (Spinner) view.findViewById(R.id.spNPP);
		spNPP.setOnItemSelectedListener(this); 
		
		spGSBH = (Spinner) view.findViewById(R.id.spGSBH); 
		spGSBH.setOnItemSelectedListener(this); 
		
		spNVBH = (Spinner) view.findViewById(R.id.spNVBH);
		spNVBH.setOnItemSelectedListener(this);  
		
		tvVisited = (TextView) view.findViewById(R.id.tvCacLanGheTham);
		tvVisited.setVisibility(View.INVISIBLE);
		
		tvMessageVisited = (TextView) view.findViewById(R.id.tvMessageCacLanGheTham);
		tvMessageVisited.setVisibility(View.GONE);

		mScrollView = (ScrollView) view.findViewById(R.id.mScrollView);
		
		tbCustomer = (VinamilkTableView) view.findViewById(R.id.tbCustomer);
		tbCustomer.setNumItemsPage(NUM_ITEM_IN_PAGE);
		tbCustomer.getHeaderView().addHeader(TBHVCustomerNotPSDSInMonthDetailRow.getTableHeader(parent));
		tbCustomer.setListener(this);

		tbVisited = (VinamilkTableView) view.findViewById(R.id.tbCacLanGheTham);
		tbVisited.getHeaderView().addHeader(VisitRow.getTableHeader(parent));
		tbVisited.setListener(this);
		tbVisited.setVisibility(View.INVISIBLE);
	}  

	/**
	 * Set thong tin User tu Profile
	 * 
	 * @author quangvt1
	 * @return void
	 */
	private void initUserInfo() {
		userId = GlobalInfo.getInstance().getProfile().getUserData().id;
		shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
	}

	/**
	 *  Lay danh sach bao cao trong ngay
	 *  
	 *  @author quangvt1
	 *  @param page
	 *  @return void
	 */
	private void getReport(int page) {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_SHOP_ID, shopId );
		b.putString(IntentConstants.INTENT_STAFF_ID, userId + "");
		b.putInt(IntentConstants.INTENT_NUM_ITEM_IN_PAGE, NUM_ITEM_IN_PAGE);
		if (page > 0) {
			b.putInt(IntentConstants.INTENT_PAGE, page);
			currentPage = page;
		}else{
			currentPage = 1;
		} 
		if (arrNVBHChoose != null && indexSelectNVBH < arrNVBHChoose.length) {
			// Lay danh sach staffId 
			String staffId = getListStaffIdForReport();
			b.putString(IntentConstants.INTENT_STAFF_ID, staffId);
		}
		e.viewData = b;
		e.action = ActionEventConstant.GET_REPORT_CUSTOMER_NOT_PSDS_IN_MONTH;
		e.sender = this;
		TBHVController.getInstance().handleViewEvent(e);
	}

	/**
	 * Lay danh sach Id nhan vien cho bao cao
	 * 
	 * @author quangvt1
	 * @return
	 */
	private String getListStaffIdForReport() {
		String staffId = ""; 
		if(indexSelectNVBH == 0){
			ArrayList<String> arrStaffNoAll = new ArrayList<String>();
			for (int i = 1; i < dtoListNVBH.arrList.size(); i++) {
				String id = dtoListNVBH.arrList.get(i).id;
				arrStaffNoAll.add(id);
			} 
			staffId = TextUtils.join(",", arrStaffNoAll);
		}else{
			staffId = dtoListNVBH.arrList.get(indexSelectNVBH).id;
		} 
		return staffId;
	}

	/**
	 * Scroll man hinh toi danh sach ghe tham
	 * 
	 * @author: QuangVT1
	 * @since: 3:10:55 PM Jan 6, 2014
	 * @return: void
	 * @throws:
	 */
	private void scrollToListVisited() {
		final int x = tbCustomer.getLeft();
    	final int y = tbCustomer.getBottom();
		setScrollToPosition(x, y);
	}
	
	/**
	 * Scroll man hinh toi 1 vi tri
	 * 
	 * @author quangvt1 
	 * @param x
	 * @param y
	 */
	private void setScrollToPosition(final int x, final int y) {
		mScrollView.post(new Runnable() { 
	        public void run() {  
	             mScrollView.scrollTo(x, y);
	        } 
		});
	}
	
	/**
	 * Render danh sach khach hang
	 * 
	 * @author quangvt1
	 * @return void
	 */
	private void renderLayout() {
		if (dtoNoPSDS.totalList >= 0) {
			tbCustomer.setTotalSize(dtoNoPSDS.totalList);	
			tbVisited.invalidate();
		}
		tbCustomer.getPagingControl().setCurrentPage(currentPage);
		
		tvMessageVisited.setVisibility(View.GONE);
		tbVisited.setVisibility(View.GONE);
		tvVisited.setVisibility(View.INVISIBLE);
		
		final int currPage = tbCustomer.getPagingControl().getCurrentPage();
		int pos = 1 + NUM_ITEM_IN_PAGE * ( currPage - 1);
		List<TableRow> listRows = new ArrayList<TableRow>();
		for (int i = 0, s = dtoNoPSDS.arrList.size(); i < s; i++, pos++) {
			TBHVCustomerNotPSDSInMonthDetailRow row = new TBHVCustomerNotPSDSInMonthDetailRow(parent, this, ACTION_ROW_CLICK);
			row.render(pos, dtoNoPSDS.arrList.get(i)); 
			listRows.add(row);
		}

		tbCustomer.addContent(listRows);
		this.parent.closeProgressDialog();
		
		// Khi scroll xuong duoi, chuyen trang thi scroll len tren
		setScrollToPosition(0, 0);
	}

	/**
	 * Reset background cac row
	 * 
	 * @author quangvt1
	 * @return void
	 */
	private void resetBackgroundRows() {
		List<TableRow> list = tbCustomer.getListRowView();
		TBHVCustomerNotPSDSInMonthDetailRow row;
		//doi background thanh binh thuong cho tat ca dong
		for (int i = 0, size = list.size(); i < size; i++) {
			row = (TBHVCustomerNotPSDSInMonthDetailRow) list.get(i);
			row.changeBackground(false);
		}		
	}

	/**
	 * Tao view danh sach cac lan vieng tham khach hang cua NVBH
	 * 
	 * @author quangvt1
	 * @return void
	 */
	private void renderLayoutVisit() {
		tvVisited.setVisibility(View.VISIBLE);
		tvMessageVisited.setVisibility(View.GONE);
		tbVisited.setVisibility(View.VISIBLE);
		if (dtoVisit != null) {
			List<TableRow> listRows = new ArrayList<TableRow>();
			for (int i = 0, s = dtoVisit.arrList.size(); i < s; i++) {
				VisitItem item = dtoVisit.arrList.get(i);
				item.stt = "" + (i + 1);
				VisitRow row = new VisitRow(parent);
				row.render(item);
				listRows.add(row);
			}
			tbVisited.addContent(listRows);
		}
		
		scrollToListVisited();
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_REPORT_CUSTOMER_NOT_PSDS_IN_MONTH:
			dtoNoPSDS = (CustomerNotPsdsInMonthReportViewDTO) modelEvent.getModelData();
			renderLayout();
			requestInsertLogKPI(HashMapKPI.GSBH_BAOCAOCHITIETKHCHUAPSDS, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		case ActionEventConstant.GET_LIST_VISIT:
			dtoVisit = (VisitDTO) modelEvent.getModelData();
			renderLayoutVisit();
			break; 
		case ActionEventConstant.GET_LIST_SHOP_SUPERVISOR_STAFF:
			dtoListNPPAndChild = (TBHVNoPSDSDetailDTO) modelEvent.getModelData();
			if (dtoListNPPAndChild != null) { 
				initSpinner();  
				parent.closeProgressDialog();
				
				// Lay danh sach khach hang khong PDSD
				final int page = 0;
				getReport(page); 
			}
			
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	/**
	 * Render cac spinner NPP, GSBH va NVBH
	 * 
	 * @author: QuangVT
	 * @since: 2:48:20 PM Dec 18, 2013
	 * @return: void
	 * @throws:
	 */
	private void initSpinner() {  
		Map<String, String> listStaffOwnerId = new HashMap<String, String>();
		Map<String, String> listShopId = new HashMap<String, String>();
		dtoListNVBH = new ListStaffDTO();
		for (StaffItem item : dtoListNPPAndChild.listNVBH.arrList) {
			if (shopIdNPP.equals(item.nvbhShopId)) { 
				listStaffOwnerId.put(item.staffOwnerId, item.staffOwnerId);
			}
			if (staffOwnerId.equals(item.staffOwnerId)) { 
				listShopId.put(item.nvbhShopId, item.nvbhShopId);
			}
			if(shopIdNPP.equals(item.nvbhShopId) && staffOwnerId.equals(item.staffOwnerId)){
				dtoListNVBH.arrList.add(item);
			}
		}
		
		initListGSBHFromMap(listStaffOwnerId); 
		initListNPPFromMap(listShopId);
		addItemAllNVBH();
		
		initSpNPP();
		initSpGSBH();
		initSpNVBH();
		 
		selectComboGSBH(staffOwnerId);
		selectComboNPP(Long.parseLong(shopIdNPP));
	} 

	/**
	 * Update danh sach GSBH khi select NPP
	 * 
	 * @author: QuangVT
	 * @since: 4:25:54 PM Dec 18, 2013
	 * @return: void
	 * @throws:  
	 * @param shopId : shopId cua NPP
	 */
	private void updateListGSBHAndNVBH(String shopId) {  
		String staffOwnerId = Constants.STR_BLANK;
		if(dtoListGSBH != null && indexSelectGSBH >= 0 && indexSelectGSBH < dtoListGSBH.arrList.size()){
			staffOwnerId = dtoListGSBH.arrList.get(indexSelectGSBH).id;
		}
		
		// Truong hop tat ca
		if (Long.parseLong(shopId) < 0) {
			dtoListNVBH = new ListStaffDTO();
			dtoListNVBH.arrList = cloneArrayStaff(dtoListNPPAndChild.listNVBH.arrList);
			dtoListNVBH.arrList.remove(0); // remove item all
			
			dtoListGSBH = new ListStaffDTO();
			dtoListGSBH.arrList = cloneArrayStaff(dtoListNPPAndChild.listGSBH.arrList);
		} else {

			dtoListNVBH = new ListStaffDTO();
			Map<String, String> listStaffOwnerId = new HashMap<String, String>();
			for (StaffItem item : dtoListNPPAndChild.listNVBH.arrList) {
				if (shopId.equals(item.nvbhShopId)) {
					dtoListNVBH.arrList.add(item);
					listStaffOwnerId.put(item.staffOwnerId, item.staffOwnerId);
				}
			}

			initListGSBHFromMap(listStaffOwnerId); 
		}
		
		// Combobox GSBH dang chon tat ca
		if (StringUtil.isNullOrEmpty(staffOwnerId)) {
			// thi khong can loc lai NVBH
		}else{
			// Can loc lai NBBH theo GSBH
			for (int i = 0, s = dtoListNVBH.arrList.size(); i < s; i++) {
				if(!dtoListNVBH.arrList.get(i).staffOwnerId.equals(staffOwnerId)){
					dtoListNVBH.arrList.remove(i);
					i--;
					s--;
				}
			}
		}
		 
		addItemAllNVBH();
	}

	/**
	 * Them item Tat ca vao list NVBH
	 * 
	 * @author: QuangVT1
	 * @since: 3:23:55 PM Jan 10, 2014
	 * @return: void
	 * @throws:
	 */
	private void addItemAllNVBH() {
		dtoListNVBH.arrList.add(0, dtoListNPPAndChild.listNVBH.arrList.get(0));
	}

	/**
	 * Tao danh sach GSBH tu map staffOwnerId
	 * 
	 * @author: QuangVT
	 * @since: 4:23:48 PM Dec 27, 2013
	 * @return: void
	 * @throws:  
	 * @param listStaffOwnerId
	 */
	private void initListGSBHFromMap(Map<String, String> listStaffOwnerId) {
		dtoListGSBH = new ListStaffDTO();
		for (StaffItem item : dtoListNPPAndChild.listGSBH.arrList) {
			if (listStaffOwnerId.containsKey(item.id)) {
				dtoListGSBH.arrList.add(item);
			}
		}  
		dtoListGSBH.arrList.add(0, dtoListNPPAndChild.listGSBH.arrList.get(0));
	}
	
	/**
	 * Clone array list
	 * 
	 * @author: QuangVT
	 * @since: 5:59:56 PM Dec 26, 2013
	 * @return: ArrayList<StaffItem>
	 * @throws:  
	 * @param arrList
	 * @return
	 */
	private ArrayList<StaffItem> cloneArrayStaff(ArrayList<StaffItem> arrList) {
		ArrayList<StaffItem> list = new ArrayList<ListStaffDTO.StaffItem>();
		for (StaffItem staffItem : arrList) {
			list.add(staffItem);
		}
		return list;
	}

	/**
	 * Update danh sach NPP khi select GSBH
	 * 
	 * @author: QuangVT
	 * @since: 4:25:54 PM Dec 18, 2013
	 * @return: void
	 * @throws:  
	 */
	private void updateListNPPAndNVBH(String staffId) {
		final String shopId = dtoListNPP.get(indexSelectNPP).shopId + "";
		
		// Truong hop tat ca
		if (StringUtil.isNullOrEmpty(staffId)) {
			dtoListNVBH = new ListStaffDTO();
			dtoListNVBH.arrList = cloneArrayStaff(dtoListNPPAndChild.listNVBH.arrList);
			dtoListNVBH.arrList.remove(0); // remove item all
			
			dtoListNPP = dtoListNPPAndChild.listNPP; 
		}else{
			dtoListNVBH = new ListStaffDTO();
			Map<String, String> listStaffOwnerId = new HashMap<String, String>();
			for (StaffItem item : dtoListNPPAndChild.listNVBH.arrList) {
				if(staffId.equals(item.staffOwnerId)){
					dtoListNVBH.arrList.add(item);
					listStaffOwnerId.put(item.nvbhShopId, item.nvbhShopId);
				}
			}
			
			initListNPPFromMap(listStaffOwnerId); 
		} 
		
		// Combobox NPP dang chon tat ca
		if (Long.parseLong(shopId) < 0) {
			// thi khong can loc lai NVBH
		} else {
			// Can loc lai NVBH theo NPP
			for (int i = 0, s = dtoListNVBH.arrList.size(); i < s; i++) {
				if (!dtoListNVBH.arrList.get(i).nvbhShopId.equals(shopId)) {
					dtoListNVBH.arrList.remove(i);
					i--;
					s--;
				}
			}
		}
				
		addItemAllNVBH();
	}

	private void initListNPPFromMap(Map<String, String> listStaffOwnerId) {
		dtoListNPP = new ArrayList<ShopDTO>();
		for (ShopDTO shop : dtoListNPPAndChild.listNPP) {
			if(listStaffOwnerId.containsKey(shop.shopId + "")){
				dtoListNPP.add(shop);
			}
		}
		dtoListNPP.add(0, dtoListNPPAndChild.listNPP.get(0)); 
	} 
	 

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		super.handleErrorModelViewEvent(modelEvent);
		parent.closeProgressDialog();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			parent = (GlobalBaseActivity) activity;
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		if (control == tbCustomer) {
			int page = tbCustomer.getPagingControl().getCurrentPage();
			getReport(page); 
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
    	if(action == ACTION_ROW_CLICK){
    		CustomerNotPsdsInMonthReportItem item = (CustomerNotPsdsInMonthReportItem) data;
    		final String cusCode = item.customerCode;
    		final String cusName = item.customerName; 
    		final String strVisit = StringUtil.getString(R.string.TEXT_OF_VISITS_) + " (" + cusCode + " - " + cusName + ")";
    		tvVisited.setText(strVisit);
    		tvVisited.setVisibility(View.VISIBLE);
    		
    		// Reset Background
    		resetBackgroundRows();
    		
    		//sau do doi lai background cho dong duoc chon
    		TBHVCustomerNotPSDSInMonthDetailRow row = (TBHVCustomerNotPSDSInMonthDetailRow) control; 
    		row.changeBackground(true);
    		
    		tbCustomer.requestFocus();
    				
    		if (item.visitNumberInMonth > 0) {
    			getListVisited(item);
    		} else {
    			tvVisited.setVisibility(View.VISIBLE);
    			tbVisited.setVisibility(View.GONE);
    			tvMessageVisited.setVisibility(View.VISIBLE);
    			
    			scrollToListVisited();
    		}
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
	 
	/**
	 * request lay danh sach cac lan ghe tham khach hang cua NVBH
	 * 
	 * @author: QuangVT
	 * @since: 4:09:47 PM Dec 19, 2013
	 * @return: void
	 * @throws:  
	 * @param item
	 */
	private void getListVisited(CustomerNotPsdsInMonthReportItem item) {
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_STAFF_ID, item.staffId);
		b.putString(IntentConstants.INTENT_CUSTOMER_ID, item.customerId);
		e.viewData = b;
		e.action = ActionEventConstant.GET_LIST_VISIT;
		e.sender = this;
		TBHVController.getInstance().handleViewEvent(e);
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				if (!isFirst) { 
					// Lay danh sach NPP, GSBH, NVBH
					getListShopSupervisorAndStaff();
				} 
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {  
		if (parent == spNPP) {
			int currSelectNPP = spNPP.getSelectedItemPosition();
			if (currSelectNPP != indexSelectNPP) {
				indexSelectNPP = currSelectNPP;
				updateSpinnerWhenChangeNPP();

				// Lay danh sach khach hang khong PDSD
				final int page = 0;
				getReport(page);
			}
		} else if (parent == spGSBH) {
			int currSelectGSBH = spGSBH.getSelectedItemPosition();
			if (currSelectGSBH != indexSelectGSBH) {
				indexSelectGSBH = currSelectGSBH;
				updateSpinnerWhenChangeGSBH();

				// Lay danh sach khach hang khong PDSD
				final int page = 0;
				getReport(page);
			}
		} else if (parent == spNVBH) {
			int currSelectNVBH = spNVBH.getSelectedItemPosition();
			if (currSelectNVBH != indexSelectNVBH) {
				indexSelectNVBH = currSelectNVBH;

				// Lay danh sach khach hang khong PDSD
				final int page = 0;
				getReport(page);
			}
		}
	}

	/**
	 * Update spinner khi nguoi dung chon NPP
	 * 
	 * @author: QuangVT
	 * @since: 3:11:10 PM Dec 18, 2013
	 * @return: void
	 * @throws:
	 */
	private void updateSpinnerWhenChangeNPP() {
		final String staffOwnerId = dtoListGSBH.arrList.get(indexSelectGSBH).id;
//		final int numGSBHOld = dtoListGSBH.arrList.size();
		updateListGSBHAndNVBH(dtoListNPP.get(indexSelectNPP).shopId + "");
		initSpNVBH();
		initSpGSBH();
		
		if (!StringUtil.isNullOrEmpty(staffOwnerId)) {
			selectComboGSBH(staffOwnerId);
		}
	}

	/**
	 * Tim GS theo staffId roi select GS do
	 * 
	 * @author: QuangVT
	 * @since: 5:52:20 PM Dec 27, 2013
	 * @return: void
	 * @throws:  
	 * @param staffOwnerId
	 */
	private void selectComboGSBH(final String staffOwnerId) {
		for (int i = 0, s = dtoListGSBH.arrList.size(); i < s; i++) {
			if(dtoListGSBH.arrList.get(i).id.equals(staffOwnerId)){
				indexSelectGSBH = i;
				spGSBH.setSelection(i, false); 
				break;
			}
		}
	}

	/**
	 *  Update spinner khi nguoi dung chon GSBH
	 * 
	 * @author: QuangVT
	 * @since: 3:11:13 PM Dec 18, 2013
	 * @return: void
	 * @throws:
	 */
	private void updateSpinnerWhenChangeGSBH() {
		final long shopId = dtoListNPP.get(indexSelectNPP).shopId;
		updateListNPPAndNVBH(dtoListGSBH.arrList.get(indexSelectGSBH).id);
		initSpNPP();
		initSpNVBH();
		
		if (shopId >= 0) {
			selectComboNPP(shopId);
		}
	}

	/**
	 * Tim NPP theo shopId roi select NPP do
	 * 
	 * @author: QuangVT
	 * @since: 5:51:30 PM Dec 27, 2013
	 * @return: void
	 * @throws:  
	 * @param shopId
	 */
	private void selectComboNPP(final long shopId) {
		for (int i = 0, s = dtoListNPP.size(); i < s; i++) {
			if(dtoListNPP.get(i).shopId  == shopId){
				indexSelectNPP = i;
				spNPP.setSelection(i, false); 
				break;
			}
		}
	} 

	@Override
	public void onNothingSelected(AdapterView<?> arg0) { 
	} 
}
