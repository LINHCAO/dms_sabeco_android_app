/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv.image;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.view.ImageInfoShopDTO;
import com.viettel.dms.dto.view.TBHVListImageDTOView;
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
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Danh sach hinh anh cua TBHV
 * 
 * TBHVListImageView.java
 * @author: QuangVT
 * @version: 1.0 
 * @since:  3:48:14 PM Dec 6, 2013
 */
public class TBHVListImageView extends BaseFragment implements OnEventControlListener, VinamilkTableListener {
	/*---------------DEFAULT---------------*/
	//--- Tag
	public static final String TAG = TBHVListImageView.class.getName();
	//--- Parent
	private GlobalBaseActivity parent;
	
	/*---------------TABLE---------------*/
	//---- Table danh sach khach hang
	private VinamilkTableView tbCusList;
	//---- So luong item trong 1 trang
	public static final int NUM_ITEM_PER_PAGE = 20;
	
	/*---------------DTO---------------*/
	// thong tin du lieu cua view
	private TBHVListImageDTOView dataView = new TBHVListImageDTOView();
	 
	/*---------------VARIALES---------------*/
	//--- Co reload khong
	boolean isReload = false;

	/**
	 * Lay the hien cua doi tuong
	 * 
	 * @author: QuangVT
	 * @since: 3:48:31 PM Dec 6, 2013
	 * @return: TBHVListImageView
	 * @throws:  
	 * @param viewData
	 * @return
	 */
	public static TBHVListImageView getInstance(Bundle viewData) {
		TBHVListImageView f = new TBHVListImageView();
		f.setArguments(viewData);
		return f;
	}

	/*
	 * (non-Javadoc)
	 * @see com.viettel.dms.view.main.BaseFragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) activity;
	}
	 
	/*
	 * (non-Javadoc)
	 * @see com.viettel.dms.view.main.BaseFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_list_image_of_tbhv, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(getString(R.string.TITLE_TBHV_LIST_IMAGE)); 
		
		// Binding control
		tbCusList = (VinamilkTableView) v.findViewById(R.id.tbCusList);
		tbCusList.setListener(this);

		// Khoi tao table
		layoutTableHeader(v);

		// Load data
		if (dataView != null && dataView.listData.size() > 0) {
			renderLayout();
		} else {
			getListImageOfTBHV(1);
		}

		return v;

	}

	/**
	 * Khoi tao table
	 * 
	 * @author: QuangVT
	 * @since: 3:49:00 PM Dec 6, 2013
	 * @return: void
	 * @throws:  
	 * @param v
	 */
	private void layoutTableHeader(View v) {
		final int numColumn = 3; 
		String[] LIST_CUSTOMER_TABLE_TITLES = new String[numColumn];
		int[] LIST_CUSTOMER_TABLE_WIDTHS    = new int[numColumn];
		
		LIST_CUSTOMER_TABLE_TITLES[0] = StringUtil.getString(R.string.TEXT_HEADER_TABLE_NPP);
		LIST_CUSTOMER_TABLE_WIDTHS[0] = 200;
		
		LIST_CUSTOMER_TABLE_TITLES[1] = StringUtil.getString(R.string.TEXT_HEADER_TABLE_GSNPP);
		LIST_CUSTOMER_TABLE_WIDTHS[1] = 545;
		
		LIST_CUSTOMER_TABLE_TITLES[2] = StringUtil.getString(R.string.TEXT_NUMBER_PHOTO);
		LIST_CUSTOMER_TABLE_WIDTHS[2] = 200; 
		 
		tbCusList.getHeaderView().addColumns(LIST_CUSTOMER_TABLE_WIDTHS, LIST_CUSTOMER_TABLE_TITLES,
				ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG));
	}
 
	/**
	 * Lay danh sach hinh anh cua TBHV
	 * 
	 * @author: QuangVT
	 * @since: 3:49:23 PM Dec 6, 2013
	 * @return: void
	 * @throws:  
	 * @param currentPage
	 */
	private void getListImageOfTBHV(int currentPage) {
		parent.showLoadingDialog();
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putInt(IntentConstants.INTENT_PAGE, (currentPage - 1) * NUM_ITEM_PER_PAGE);
		data.putInt(IntentConstants.INTENT_NUMTOP, NUM_ITEM_PER_PAGE);
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);

		e.viewData = data;
		e.action = ActionEventConstant.GET_LIST_IMAGE_OF_TBHV;
		e.sender = this;
		TBHVController.getInstance().handleViewEvent(e);
	}
 
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_LIST_IMAGE_OF_TBHV:
			TBHVListImageDTOView tempDto = (TBHVListImageDTOView) modelEvent.getModelData();
			if (tempDto != null) {
				dataView.totalItems = tempDto.totalItems;
				dataView.listData.clear();
				for (int i = 0, size = tempDto.listData.size(); i < size; i++) {
					dataView.listData.add(tempDto.listData.get(i));
				}
			}
			renderLayout();
			parent.closeProgressDialog();
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	} 
	 
	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		super.handleErrorModelViewEvent(modelEvent);
	}
 
	/**
	 * render layout for list customer - image view
	 * 
	 * @author: QuangVT
	 * @since: 3:49:53 PM Dec 6, 2013
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		int pos = 1 + Constants.NUM_ITEM_PER_PAGE * (tbCusList.getPagingControl().getCurrentPage() - 1);
		List<TableRow> listRows = new ArrayList<TableRow>();
		if (dataView.listData.size() > 0) {
			for (int i = 0, s = dataView.listData.size(); i < s; i++) {
				TBHVListImageRow row = new TBHVListImageRow(parent, null);
				row.renderLayout(pos, dataView.listData.get(i));
				row.setListener(this);
				pos++;
				listRows.add(row);
			}
		} else {
			TBHVListImageRow row = new TBHVListImageRow(parent, null);
			row.setClickable(true);
			row.setOnClickListener(this);
			row.renderLayoutNoResult();
			listRows.add(row);
		}
		tbCusList.addContent(listRows);
//		if (tbCusList.getPagingControl().totalPage < 0)
//			tbCusList.setTotalSize(dataView.totalItems);
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
		if (control == tbCusList) {
			getListImageOfTBHV(tbCusList.getPagingControl().getCurrentPage());
		}
	}
 
	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		switch (action) {
		case TBHVListImageRow.ACTION_VIEW_DETAIL_NPP: {

			isReload = true;
			gotoListImageAtNPP((ImageInfoShopDTO) data);
			break;
		}
		default:
			break;
		}
	} 
	
	/**
	 * Toi man ds hinh anh cua 1 NPP
	 * 
	 * @author: QuangVT
	 * @since: 3:50:21 PM Dec 6, 2013
	 * @return: void
	 * @throws:  
	 * @param item
	 */
	private void gotoListImageAtNPP(ImageInfoShopDTO item) {
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_STAFF_ID, item.supervisorId);
		bundle.putString(IntentConstants.INTENT_STAFF_NAME, item.supervisorName);
		bundle.putString(IntentConstants.INTENT_SHOP_ID, item.shopId);
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_GSNPP_IMAGE_LIST;
		SuperviorController.getInstance().handleSwitchFragment(e);
	} 
	 
	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				// cau request du lieu man hinh
				tbCusList.getPagingControl().totalPage = -1;
				getListImageOfTBHV(1);
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

}
