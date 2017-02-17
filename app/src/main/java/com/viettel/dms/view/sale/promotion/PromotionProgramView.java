/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.promotion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.ProInfoDTO;
import com.viettel.dms.dto.view.SaleSupportProgramModel;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.SalesPersonActivity;
import com.viettel.sabeco.R;

/**
 * 
 * MH Danh sach chuong trinh ho tro ban hang
 * 
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.0
 */

public class PromotionProgramView extends BaseFragment implements
		OnClickListener, OnEventControlListener, VinamilkTableListener {

	public static final String TAG = PromotionProgramView.class.getName();
	private DMSTableView tbPromotionList;
	private SalesPersonActivity parent;
	TextView tvPromotionCode; //ma chuong trinh khuyen mai
	TextView tvPromotionName; //ten chuong trinh khuyen mai
	TextView tvPromotionFromDate; //ngay bat dau
	TextView tvPromotionToDate; //ngay ket thuc
	TextView tvPromotionDescription; //mo ta chuong trinh khuyen mai
	private boolean checkSendRequest = true; //bien kiem tra cho biet co goi request xuong db lay du lieu ko?
	SaleSupportProgramModel promotionData = new SaleSupportProgramModel(); //dto view
	private boolean checkLoadMore; //bien kiem tra viec load them (phan trang)
	private int indexPage;

	/**
	 * 
	 * method get instance
	 * 
	 * @author: YenNTH
	 * @param index
	 * @return
	 * @return: FindProductAddOrderListView
	 * @throws:
	 */
	public static PromotionProgramView getInstance() {
		PromotionProgramView instance = new PromotionProgramView();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		instance.setArguments(args);

		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (SalesPersonActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		checkLoadMore = false;
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.layout_promotion_list, null);
		View view = super.onCreateView(inflater, v, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_LIST_PROMOTION));
		tbPromotionList = (DMSTableView) view.findViewById(R.id.tbPromotionList);
		tbPromotionList.setListener(this);
//		layoutHeaderTable(); 
		initHeaderTable(tbPromotionList, new PromotionProgrameRow(parent, this));
		// request get list promotion
		if (checkSendRequest) {
			getPromotionProgramList();
		}
		return view;
	}
	

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (checkSendRequest) {
		} else {
			// layout ds sp ban
			renderLayout(promotionData);
			// layout ds sp khuyen mai
			checkSendRequest = true;
		}
	}

	/**
	 * Lay danh sach san pham chuong trinh khuyen mai
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void getPromotionProgramList() {
		if (checkLoadMore == true) {
			indexPage = (tbPromotionList.getPagingControl().getCurrentPage() - 1);
		}
		String page = " limit " + (indexPage * Constants.NUM_ITEM_PER_PAGE) + "," + Constants.NUM_ITEM_PER_PAGE;
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		data.putString(IntentConstants.INTENT_PAGE, page);
		data.putBoolean(IntentConstants.INTENT_CHECK_PAGGING, checkLoadMore);
		parent.showProgressDialog(getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		e.viewData = data;
		if (checkLoadMore == false) {
			e.tag = 11;
		}
		e.sender = this;
		e.action = ActionEventConstant.GET_LIST_PROMOTION_PROGRAME;
		SaleController.getInstance().handleViewEvent(e);

	}
	
	/**
	 * 
	 * render du lieu cho view
	 * 
	 * @author: YenNTH
	 * @param model
	 * @return: void
	 * @throws:
	 */
	private void renderLayout(SaleSupportProgramModel model) {
		int pos = (tbPromotionList.getPagingControl().getCurrentPage() - 1) * Constants.NUM_ITEM_PER_PAGE + 1;
//		List<DMSTableRow> listRows = new ArrayList<DMSTableRow>();
		tbPromotionList.clearAllData();
		if (model.listPrograme.size() > 0) {
			for (int i = 0, n = model.listPrograme.size(); i < n; i++) {
				PromotionProgrameRow row = new PromotionProgrameRow(parent, this);
				row.setClickable(true);
				row.setOnClickListener(this);
				row.setTag(Integer.valueOf(pos));
				row.renderLayout(pos, model.listPrograme.get(i));
				row.setListener(this);
				pos++;

//				listRows.add(row);
				tbPromotionList.addRow(row);
			}
		} else {
			PromotionProgrameRow row = new PromotionProgrameRow(parent, this);

			row.setClickable(true);
			row.setOnClickListener(this);
			row.renderLayoutNoResult();

//			listRows.add(row);
			tbPromotionList.addRow(row);
		}  
		if (tbPromotionList.getPagingControl().totalPage < 0)
			tbPromotionList.setTotalSize(model.total, 1);
		tbPromotionList.getPagingControl().setCurrentPage(indexPage + 1);
//		tbPromotionList.addContent(listRows);
	}

	/**
	 * 
	 * @author: quangvt1
	 * @since: 15:19:11 16-05-2014
	 * @return: void
	 * @throws:
	 */
//	private void layoutHeaderTable() {
//		tbPromotionList.getHeaderView().addColumns(
//			TableDefineContanst.PROMOTION_TABLE_WIDTHS,
//			TableDefineContanst.PROMOTION_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK),ImageUtil.getColor(R.color.TABLE_HEADER_BG));
//		tbPromotionList.setNumItemsPage(Constants.NUM_ITEM_PER_PAGE);
//	}

	/**
	 * updateDisplay
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public void updateBirtday(int mDay, int mMonth, int Year) {
		String sDay = String.valueOf(mDay);
		String sMonth = String.valueOf(mMonth + 1);
		if (mDay < 10) {
			sDay = "0" + sDay;
		}
		if (mMonth + 1 < 10) {
			sMonth = "0" + sMonth;
		}

	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.GET_LIST_PROMOTION_PROGRAME: {
			checkSendRequest = false;
			SaleSupportProgramModel modelData = (SaleSupportProgramModel) modelEvent.getModelData();
			if (event.tag == 11) {
				tbPromotionList.getPagingControl().totalPage = -1;
				tbPromotionList.getPagingControl().setCurrentPage(1);
				promotionData.total = modelData.total;
			}
			promotionData.listPrograme.clear();
			promotionData.listPrograme.addAll(modelData.listPrograme);
			renderLayout(promotionData);
			requestInsertLogKPI(HashMapKPI.NVBH_DANHSACHCHUONGTRINHHOTROBANHANG, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		}
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
		parent.closeProgressDialog();
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.GET_LIST_PROMOTION_PROGRAME: {
			if (event.tag == 11) {
				promotionData = new SaleSupportProgramModel();
				renderLayout(promotionData);
			}
			break;
		}
		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
	}
	
	/**
	*  hien thi man hinh danh sach trung bay
	*  @author: ThanhNN8
	*  @return: void
	*  @throws:
	*/
	@SuppressWarnings("unused")
	private void gotoDisplayProgramView() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = new Bundle();
		e.action = ActionEventConstant.GO_TO_DISPLAY_PROGRAM;
		UserController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		checkLoadMore = true;
		getPromotionProgramList();
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) { 
		if (action == ActionEventConstant.GO_TO_PROMOTION_PROGRAM_DETAIL) {
			checkSendRequest = false;
			gotoPromotionProgrameDetail((ProInfoDTO) data);
		}else if (action == ActionEventConstant.GO_TO_PROMOTION_PROGRAM_DONE) {
			checkSendRequest = false;
			gotoPromotionProgrameDone((ProInfoDTO) data);
		}

	}
	
	/**
	 * Di toi man hinh thuc hien cua CT HTBH
	 * @author: quangvt1
	 * @since: 12:27:47 13-06-2014
	 * @return: void
	 * @throws:  
	 * @param data
	 */
	private void gotoPromotionProgrameDone(ProInfoDTO data) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putSerializable(IntentConstants.INTENT_PROMOTION, data); 
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_PROMOTION_PROGRAM_DONE;
		SaleController.getInstance().handleSwitchFragment(e);
	}
	
	/**
	 * Di toi man hinh thong tin chi tiet cua CT HTBH
	 * @author: quangvt1
	 * @since: 12:28:16 13-06-2014
	 * @return: void
	 * @throws:  
	 * @param data
	 */
	private void gotoPromotionProgrameDetail(ProInfoDTO data) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putSerializable(IntentConstants.INTENT_PROMOTION, data);
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_PROMOTION_PROGRAM_DETAIL;
		SaleController.getInstance().handleSwitchFragment(e);
	}
	
	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if(this.isVisible()){
				indexPage = 0;
				checkLoadMore = false;
				getPromotionProgramList();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
}
