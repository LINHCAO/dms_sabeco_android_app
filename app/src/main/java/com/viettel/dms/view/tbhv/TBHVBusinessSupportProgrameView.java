/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.constants.TableDefineContanst;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.view.TBHVBusinessSupportPrograme;
import com.viettel.dms.dto.view.TBHVBusinessSupportProgrameDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.TBHVActivity;
import com.viettel.sabeco.R;

/**
 * 
 *  Ds chuong trinh KM ma TBHV quan ly
 *  @author: Nguyen Thanh Dung
 *  @version: 1.1
 *  @since: 1.0
 */
public class TBHVBusinessSupportProgrameView extends BaseFragment implements
		OnClickListener, OnEventControlListener, VinamilkTableListener {

	public static final String TAG = TBHVBusinessSupportProgrameView.class.getName();

	public static final int ACTION_PROMOTION = 0;
	public static final int ACTION_DISPLAY = 1;

	private VinamilkTableView tbPromotionList;
	private TBHVActivity parent;

	TextView tvPromotionCode; //ma chuong trinh khuyen mai
	TextView tvPromotionName; //ten chuong trinh khuyen mai
	TextView tvPromotionFromDate; //ngay bat dau
	TextView tvPromotionToDate; //ngay ket thuc
	TextView tvPromotionDescription; //mo ta chuong trinh khuyen mai
	private boolean isFirstTime;//Kiem tra lan dau vao man hinh
	private boolean checkSendRequest = true;//check da gui request lay ds hay chua khi refresh lai MH thi ko goi nua
	TBHVBusinessSupportProgrameDTO promotionData = new TBHVBusinessSupportProgrameDTO(); //dto view
	
	/**
	 * 
	*  method get instance
	*  @author: Nguyen Thanh Dung
	*  @return
	*  @return: TBHVPromotionProgramView
	*  @throws:
	 */
	public static TBHVBusinessSupportProgrameView getInstance() {
		TBHVBusinessSupportProgrameView instance = new TBHVBusinessSupportProgrameView();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		instance.setArguments(args);

		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (TBHVActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		isFirstTime = true;
		ViewGroup v = (ViewGroup) inflater.inflate(
				R.layout.layout_tbhv_business_support_programe_list, null);
		View view = super.onCreateView(inflater, v, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_TBHV_LIST_BUSINESS_SUPPORT_PROGRAME));

		tbPromotionList = (VinamilkTableView) view
				.findViewById(R.id.tbPromotionList);
		tbPromotionList.setListener(this);

		// menu bar
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_CTTB),R.drawable.menu_manage_icon, ACTION_DISPLAY);
		addMenuItem(StringUtil.getString(R.string.TEXT_CTKM),R.drawable.menu_promotion_icon, ACTION_PROMOTION);
//		setMenuItemFocus(2);
//
		// request get list promotion
		if (checkSendRequest) {
			getPromotionProgramList();
		}
		return view;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Fragment#onResume()
	 */
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
	 * 
	*  Lay danh sach san pham chuong trinh khuyen mai
	*  @author: Nguyen Thanh Dung
	*  @return: void
	*  @throws:
	 */
	private void getPromotionProgramList() {
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_TBHV_BUSINESS_SUPPORT_PROGRAME_INFO;
		TBHVController.getInstance().handleViewEvent(e);

	}
	
	/**
	 * 
	*  render du lieu cho man hinh
	*  @author: Nguyen Thanh Dung
	*  @param model
	*  @return: void
	*  @throws:
	 */
	private void renderLayout(TBHVBusinessSupportProgrameDTO model) {
		int pos = (tbPromotionList.getPagingControl().getCurrentPage() - 1) * Constants.NUM_ITEM_PER_PAGE + 1;
		List<TableRow> listRows = new ArrayList<TableRow>();
		for (int i = 0, n = model.getListPrograme().size(); i < n; i++) {
			TBHVBusinessSupportProgrameRow row = new TBHVBusinessSupportProgrameRow(parent, null);

			row.setClickable(true);
			row.setOnClickListener(this);
			row.setTag(Integer.valueOf(pos));
			row.renderLayout(pos, model.getListPrograme().get(i));
			row.setListener(this);
			pos++;

			listRows.add(row);
		}
		
		if (isFirstTime) {
			tbPromotionList.getHeaderView().addColumns(
				TableDefineContanst.TBHV_BUSINESS_SUPPORT_TABLE_WIDTHS,
				TableDefineContanst.TBHV_BUSINESS_SUPPORT_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK),ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			tbPromotionList.setNumItemsPage(Constants.NUM_ITEM_PER_PAGE);
			isFirstTime = false;
		}
		if (tbPromotionList.getPagingControl().totalPage < 0)
			tbPromotionList.setTotalSize(model.getListPrograme().size());
		tbPromotionList.addContent(listRows);
	}


	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.GET_TBHV_BUSINESS_SUPPORT_PROGRAME_INFO: {
			promotionData.getListPrograme().clear();
			Bundle data = (Bundle) modelEvent.getModelData();
			
			int numCTKM = data.getInt(StringUtil.getString(R.string.TEXT_CTKM));
			int numCTTB = data.getInt(StringUtil.getString(R.string.TEXT_HEADER_MENU_CTTB));
			
			//Chuong trinh KM
			TBHVBusinessSupportPrograme programe = new TBHVBusinessSupportPrograme();
			programe.setName(StringUtil.getString(R.string.TEXT_CTKM));
			programe.setCount(numCTKM);
			programe.setAction(ActionEventConstant.GO_TO_TBHV_PROMOTION_PROGRAM);
			promotionData.getListPrograme().add(programe);
			
			
			//Chuong trinh TB
			programe = new TBHVBusinessSupportPrograme();
			programe.setName(StringUtil.getString(R.string.TEXT_HEADER_MENU_CTTB));
			programe.setCount(numCTTB);
			programe.setAction(ActionEventConstant.GO_TO_TBHV_SUPPORT_SALE);
			promotionData.getListPrograme().add(programe);
			
			renderLayout(promotionData);
			break;
		}
		default:
			break;
		}
		parent.closeProgressDialog();
		super.handleModelViewEvent(modelEvent);
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		super.handleErrorModelViewEvent(modelEvent);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		if (eventType == ACTION_DISPLAY) {
			gotoDisplayProgramView();
		} else if (eventType == ACTION_PROMOTION) {
			gotoPromotionProgramView();
		}
	}
	
	/**
	 * 
	*  chuyen den man hinh danh sach trung bay
	*  @author: Nguyen Thanh Dung
	*  @return: void
	*  @throws:
	 */
	private void gotoDisplayProgramView() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = new Bundle();
		e.action = ActionEventConstant.GO_TO_TBHV_SUPPORT_SALE;
		TBHVController.getInstance().handleSwitchFragment(e);
	}
	
	/**
	 * 
	 *  chuyen den man hinh danh sach trung bay
	 *  @author: Nguyen Thanh Dung
	 *  @return: void
	 *  @throws:
	 */
	private void gotoPromotionProgramView() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = new Bundle();
		e.action = ActionEventConstant.GO_TO_TBHV_PROMOTION_PROGRAM;
		TBHVController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {

		if (action == ActionEventConstant.GO_TO_TBHV_SUPPORT_SALE) {
			gotoDisplayProgramView();
		} else if (action == ActionEventConstant.GO_TO_TBHV_PROMOTION_PROGRAM) {
			gotoPromotionProgramView();
		}

	}
	
	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if(this.isVisible()){
				getPromotionProgramList();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
}
