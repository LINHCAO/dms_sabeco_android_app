/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

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
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.dto.db.PromotionProgrameDTO;
import com.viettel.dms.dto.view.PromotionProgrameModel;
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
import com.viettel.dms.view.main.SupervisorActivity;
import com.viettel.sabeco.R;

/**
 * MH chuong trinh khuyen mai GSNPP
 * 
 * @author: YenNTH
 * @version: 1.0
 */

public class SuperVisorPromotionProgramView extends BaseFragment implements
		OnClickListener, OnEventControlListener, VinamilkTableListener {

	public static final String TAG = SuperVisorPromotionProgramView.class.getName();

	public static final int ACTION_PROMOTION = 0;
	public static final int ACTION_DISPLAY = 1;
	public static final int ACTION_PRODUCT_LIST = 2;
	private VinamilkTableView tbPromotionList;
	private SupervisorActivity parent;
	TextView tvPromotionCode; //ma chuong trinh khuyen mai
	TextView tvPromotionName; //ten chuong trinh khuyen mai
	TextView tvPromotionFromDate; //ngay bat dau
	TextView tvPromotionToDate; //ngay ket thuc
	TextView tvPromotionDescription; //mo ta chuong trinh khuyen mai
	private boolean isFirstTime;
	private boolean checkSendRequest = true;
	PromotionProgrameModel promotionData; //dto view
	private boolean checkLoadMore;

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
	public static SuperVisorPromotionProgramView getInstance() {
		SuperVisorPromotionProgramView instance = new SuperVisorPromotionProgramView();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		instance.setArguments(args);

		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (SupervisorActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		isFirstTime = true;
		checkLoadMore = false;
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.layout_supervisor_promotion_list, null);
		View view = super.onCreateView(inflater, v, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_SUPERVISOR_LIST_PROMOTION));
		tbPromotionList = (VinamilkTableView) view.findViewById(R.id.tbPromotionList);
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
		int indexPage = 0;
		if (checkLoadMore == true) {
			indexPage = (tbPromotionList.getPagingControl().getCurrentPage() - 1);
		}
		String page = " limit " + (indexPage * Constants.NUM_ITEM_PER_PAGE)+ "," + Constants.NUM_ITEM_PER_PAGE;
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		data.putString(IntentConstants.INTENT_PAGE, page);
		data.putBoolean(IntentConstants.INTENT_CHECK_PAGGING, checkLoadMore);
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		e.viewData = data;
		if (checkLoadMore == false) {
			e.tag = 11;
		}
		e.sender = this;
		e.action = ActionEventConstant.GET_LIST_PROMOTION_PROGRAME;
		SuperviorController.getInstance().handleViewEvent(e);

	}
	
	/**
	 * 
	*  render du lieu cho man hinh
	*  @author: YenNTH
	*  @param model
	*  @return: void
	*  @throws:
	 */
	private void renderLayout(PromotionProgrameModel model) {
		int pos = (tbPromotionList.getPagingControl().getCurrentPage() - 1) * Constants.NUM_ITEM_PER_PAGE + 1;
		List<TableRow> listRows = new ArrayList<TableRow>();
		if (model.getModelData().size() > 0) {
			for (int i = 0, n = model.getModelData().size(); i < n; i++) {
				SuperVisorPromotionProgrameRow row = new SuperVisorPromotionProgrameRow(parent, null);
				row.setClickable(true);
				row.setOnClickListener(this);
				row.setTag(Integer.valueOf(pos));
				row.renderLayout(pos, model.getModelData().get(i));
				row.setListener(this);
				pos++;
				listRows.add(row);
			}
		} else {
			SuperVisorPromotionProgrameRow row = new SuperVisorPromotionProgrameRow(parent, null);
			row.setClickable(true);
			row.setOnClickListener(this);
			row.renderLayoutNoResult();
			listRows.add(row);
		}
		if (isFirstTime) {
			tbPromotionList.getHeaderView().addColumns(
				TableDefineContanst.SUPER_PROMOTION_TABLE_WIDTHS,
				TableDefineContanst.SUPER_PROMOTION_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK),ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			tbPromotionList.setNumItemsPage(Constants.NUM_ITEM_PER_PAGE);
			isFirstTime = false;
		}
		if (tbPromotionList.getPagingControl().totalPage < 0)
			tbPromotionList.setTotalSize(model.getTotal());
		tbPromotionList.addContent(listRows);
	}


	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.GET_LIST_PROMOTION_PROGRAME: {
			if (event.tag == 11) {
				tbPromotionList.getPagingControl().totalPage = -1;
				tbPromotionList.getPagingControl().setCurrentPage(1);
			}
			promotionData = (PromotionProgrameModel) modelEvent.getModelData();
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
		case ActionEventConstant.GET_LIST_PROMOTION_PROGRAME: {
			if (event.tag == 11) {
				promotionData = new PromotionProgrameModel();
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
		} else if (eventType == ACTION_PRODUCT_LIST) {
			gotoProductListView();
		}
	}
	
	/**
	 * 
	*  Chuyen toi ds san pham
	*  @author: YenNTH
	*  @return: void
	*  @throws:
	 */
	private void gotoProductListView() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = new Bundle();
		e.action = ActionEventConstant.GO_TO_PRODUCT_LIST;
		SuperviorController.getInstance().handleSwitchFragment(e);
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
