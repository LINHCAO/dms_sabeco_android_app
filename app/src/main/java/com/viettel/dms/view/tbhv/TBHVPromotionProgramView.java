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
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.constants.TableDefineContanst;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.db.PromotionProgrameDTO;
import com.viettel.dms.dto.view.TBHVPromotionProgrameDTO;
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
public class TBHVPromotionProgramView extends BaseFragment implements
		OnClickListener, OnEventControlListener, VinamilkTableListener {

	public static final String TAG = TBHVPromotionProgramView.class.getName();

	public static final int ACTION_PROMOTION = 0;
	public static final int ACTION_DISPLAY = 1;
	public static final int ACTION_PRODUCT_LIST = 2;

	private VinamilkTableView tbPromotionList;
	private TBHVActivity parent;

	//ma chuong trinh khuyen mai
	TextView tvPromotionCode; 
	//ten chuong trinh khuyen mai
	TextView tvPromotionName; 
	//ngay bat dau
	TextView tvPromotionFromDate; 
	//ngay ket thuc
	TextView tvPromotionToDate;
	//mo ta chuong trinh khuyen mai
	TextView tvPromotionDescription;
	//Kiem tra lan dau vao man hinh
	private boolean isFirstTime;
	//check da gui request lay ds hay chua khi refresh lai MH thi ko goi nua
	private boolean checkSendRequest = true;
	 //dto view
	TBHVPromotionProgrameDTO promotionData;
	//Check load ds lan dau tien hay la load more de dem tong so row
	private boolean checkLoadMore;

	/**
	 * 
	*  method get instance
	*  @author: Nguyen Thanh Dung
	*  @return
	*  @return: TBHVPromotionProgramView
	*  @throws:
	 */
	public static TBHVPromotionProgramView getInstance() {
		TBHVPromotionProgramView instance = new TBHVPromotionProgramView();
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
		checkLoadMore = false;
		ViewGroup v = (ViewGroup) inflater.inflate(
				R.layout.layout_tbhv_promotion_list, null);
		View view = super.onCreateView(inflater, v, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_TBHV_LIST_PROMOTION));

		tbPromotionList = (VinamilkTableView) view
				.findViewById(R.id.tbPromotionList);
		tbPromotionList.setListener(this);

		// menu bar
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_PRODUCT),R.drawable.icon_product_list, ACTION_PRODUCT_LIST);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_CTHTBH),R.drawable.menu_manage_icon, ACTION_DISPLAY);
		addMenuItem(StringUtil.getString(R.string.TEXT_CTKM),R.drawable.menu_promotion_icon, ACTION_PROMOTION);
		setMenuItemFocus(3);

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
		int indexPage = 0;
		if (checkLoadMore) {
			indexPage = (tbPromotionList.getPagingControl().getCurrentPage() - 1);
		}

		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		data.putInt(IntentConstants.INTENT_PAGE, indexPage);
		data.putBoolean(IntentConstants.INTENT_CHECK_PAGGING, checkLoadMore);
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		e.viewData = data;
		if (!checkLoadMore) {
			e.tag = 11;
		}
		e.sender = this;
		e.action = ActionEventConstant.GET_TBHV_LIST_PROMOTION_PROGRAME;
		TBHVController.getInstance().handleViewEvent(e);

	}
	
	/**
	*  render du lieu cho man hinh
	*  @author: Nguyen Thanh Dung
	*  @param model
	*  @return: void
	*  @throws:
	 */
	private void renderLayout(TBHVPromotionProgrameDTO model) {
		int pos = (tbPromotionList.getPagingControl().getCurrentPage() - 1) * Constants.NUM_ITEM_PER_PAGE + 1;
		if (model.getModelData().size() > 0) {
			tbPromotionList.clearAllData();
			for (PromotionProgrameDTO dto: model.getModelData()) {
				TBHVPromotionProgrameRow row = new TBHVPromotionProgrameRow(parent, this);
				row.setClickable(true);
				row.setTag(Integer.valueOf(pos));
				row.renderLayout(pos, dto);
				pos++;
				tbPromotionList.addRow(row);
			}
		}else{
			tbPromotionList.showNoContentRowWithString(StringUtil.getString(R.string.TEXT_CTKM_INFO_NORESULT));
		}
		
		if (isFirstTime) {
			tbPromotionList.getHeaderView().addColumns(
				TableDefineContanst.TBHV_PROMOTION_TABLE_WIDTHS,
				TableDefineContanst.TBHV_PROMOTION_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK),ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			tbPromotionList.setNumItemsPage(Constants.NUM_ITEM_PER_PAGE);
			isFirstTime = false;
		}
		if (tbPromotionList.getPagingControl().totalPage < 0)
			tbPromotionList.setTotalSize(model.getTotal());
	}


	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.GET_TBHV_LIST_PROMOTION_PROGRAME: {
			if (event.tag == 11) {
				tbPromotionList.getPagingControl().totalPage = -1;
				tbPromotionList.getPagingControl().setCurrentPage(1);
			}
			promotionData = (TBHVPromotionProgrameDTO) modelEvent.getModelData();
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
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.GET_TBHV_LIST_PROMOTION_PROGRAME: {
			if (event.tag == 11) {
				promotionData = new TBHVPromotionProgrameDTO();
				List<PromotionProgrameDTO> modelData = new ArrayList<PromotionProgrameDTO>();
				promotionData.setModelData(modelData);
				promotionData.setTotal(0);
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
		if (eventType == ACTION_DISPLAY) {
			gotoSupportSaleView();
		}else if (eventType == ACTION_PRODUCT_LIST) {
			gotoProductList();
		}
	}

	/**
	 * 
	 * Toi man hinh ds sp TBHV
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return: void
	 * @throws:
	 */
	private void gotoProductList() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = new Bundle();
		e.action = ActionEventConstant.GO_TO_TBHV_PRODUCT_LIST;
		TBHVController.getInstance().handleSwitchFragment(e);
	}
	/**
	 * 
	*  chuyen den man hinh danh sach trung bay
	*  @author: Nguyen Thanh Dung
	*  @return: void
	*  @throws:
	 */
	private void gotoSupportSaleView() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = new Bundle();
		e.action = ActionEventConstant.GO_TO_TBHV_SUPPORT_SALE;
		TBHVController.getInstance().handleSwitchFragment(e);
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
			showPromotionDetailView((PromotionProgrameDTO) data);
		}

	}
	
	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if(this.isVisible()){
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
