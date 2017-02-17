/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

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
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.1
 */
public class TBHVListImageView extends BaseFragment implements OnEventControlListener, VinamilkTableListener {
	public static final String TAG = TBHVListImageView.class.getName();
	// MH nay khong can phan trang vi so luong it, 
	// nhung de phan trang 20 cung chap nhan dc
	public static final int NUM_ITEM_PER_PAGE = 20;

	//private static final int MENU_IMAGE = 1;
	// table ds
	private VinamilkTableView tbCusList;
	private GlobalBaseActivity parent;

	boolean isReload = false;
	// thong tin du lieu cua view
	private TBHVListImageDTOView dataView = new TBHVListImageDTOView();

	public static TBHVListImageView getInstance(Bundle viewData) {
		TBHVListImageView f = new TBHVListImageView();
		f.setArguments(viewData);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) activity;
	}

	/**
	 * 
	 * khoi tao control
	 * 
	 * @author: HoanPD1
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return: v
	 * @throws:
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_list_image_of_tbhv, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(getString(R.string.TITLE_TBHV_LIST_IMAGE));
//		enableMenuBar(this);
//		initMenuActionBar();
		
		tbCusList = (VinamilkTableView) v.findViewById(R.id.tbCusList);
		tbCusList.setListener(this);

		layoutTableHeader(v);

		if (dataView != null && dataView.listData.size() > 0) {
			renderLayout();
		} else {
			getListImageOfTBHV(1);
		}

		return v;

	}


	/**
	 * header list
	 * 
	 * @author: HoanPD1
	 * @param v
	 * @return: void
	 * @throws:
	 */
	private void layoutTableHeader(View v) {
		String[] LIST_CUSTOMER_TABLE_WIDTHS = { StringUtil.getString(R.string.TEXT_HEADER_TABLE_NPP),
				StringUtil.getString(R.string.TEXT_HEADER_TABLE_GSNPP),
				StringUtil.getString(R.string.TEXT_NUMBER_PHOTO) };
		int[] LIST_CUSTOMER_TABLE_TITLES = { 200, 545, 200 };
		tbCusList.getHeaderView().addColumns(LIST_CUSTOMER_TABLE_TITLES, LIST_CUSTOMER_TABLE_WIDTHS,
				ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG));
	}

	/**
	 * get danh sach hinh anh cua TBHV
	 * 
	 * @author: TruongHN
	 * @throws:
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
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		// TODO Auto-generated method stub
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
	 * @author: TruongHN
	 * @return: void
	 * @throws:
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
