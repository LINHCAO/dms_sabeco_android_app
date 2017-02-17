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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.constants.TableDefineContanst;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.view.TBHVManagerEquipmentDTO;
import com.viettel.dms.dto.view.TBHVManagerEquipmentDTO.TBHVManagerEquipmentItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.TBHVActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 *  Man hinh quan ly thiet bi chi tiet theo NVBH cua NPP dung o MH TBHVManagerEquipmentDetailView
 *  @author: Nguyen Thanh Dung
 *  @version: 1.1
 *  @since: 1.0
 */
public class TBHVManagerEquipmentDetailView extends BaseFragment implements
		OnItemSelectedListener, OnEventControlListener, OnClickListener,
		VinamilkTableListener {

	private static final int ACTION_ROW_CLICK = 1;
	// main activity
	public static final String TAG = TBHVManagerEquipmentDetailView.class.getName();
	private TBHVActivity parent;
	private VinamilkTableView tbEquipment;//Table ds thiet bi
	private TBHVManagerEquipmentDTO dto;//du lieu MH quan ly thiet bi cua shop
	private TextView tvMaTenNPP;
	
	private String shopId;//id cua shop duoc chon
	private String shopCode;//code cua shop duoc chon
	private String shopName;//ten shop

	private boolean isFirst = true;// vao man hinh lan dau tien hay kg?

	public static TBHVManagerEquipmentDetailView getInstance(Bundle b) {
		TBHVManagerEquipmentDetailView instance = new TBHVManagerEquipmentDetailView();
		instance.setArguments(b);
		return instance;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_tbhv_manager_equipment_detail, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		Bundle data = (Bundle)getArguments();
		shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		shopCode = data.getString(IntentConstants.INTENT_SHOP_CODE);
		shopName = data.getString(IntentConstants.INTENT_SHOP_NAME);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_HEADER_TBHV_MANAGER_EQUIPMENT_VIEW));
		tvMaTenNPP = (TextView) view.findViewById(R.id.tvMaTenNPP);
		tvMaTenNPP.setText(shopCode + "-" + shopName);
		initView(v);
		if (isFirst) {			
			getDanhSachThietBi();
		}
		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (!isFirst) {
			renderLayout();
		}
		super.onResume();
	}

	/**
	 * 
	*  Khoi tao view
	*  @author: Nguyen Thanh Dung
	*  @param view
	*  @return: void
	*  @throws:
	 */
	private void initView(View view) {
		tbEquipment = (VinamilkTableView) view.findViewById(R.id.tbEquipment);
		tbEquipment.getHeaderView().addColumns(
				TableDefineContanst.EQUIPMENT_DETAIL_TABLE_WIDTHS,
				TableDefineContanst.EQUIPMENT_DETAIL_TABLE_TITLES,
				ImageUtil.getColor(R.color.BLACK),
				ImageUtil.getColor(R.color.TABLE_HEADER_BG));
		tbEquipment.setListener(this);
	}

	/**
	 * 
	*  Lay ds thiet bi theo NVBH cua NPP
	*  @author: Nguyen Thanh Dung
	*  @return: void
	*  @throws:
	 */
	private void getDanhSachThietBi() {
		this.parent.showProgressDialog(getString(R.string.loading)); 
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_SHOP_ID, shopId);
		e.viewData = b;
		e.action = ActionEventConstant.GET_TBHV_LIST_EQUIPMENT;
		e.sender = this;
		TBHVController.getInstance().handleViewEvent(e);
	}

	/**
	 * 
	*  Cap nhat lai layout
	*  @author: Nguyen Thanh Dung
	*  @return: void
	*  @throws:
	 */
	private void renderLayout() {
		TBHVManagerEquipmentItem item;
		TBHVManagerEquipmentDetailRow row;
		List<TableRow> listRows = new ArrayList<TableRow>();
		int soThietBi = 0, khongDat = 0;
		for (int i = 0, s = dto.arrList.size(); i < s; i++) {
			item = (TBHVManagerEquipmentItem) dto.arrList.get(i);
			row = new TBHVManagerEquipmentDetailRow(parent, ACTION_ROW_CLICK);
			row.setClickable(true);
			row.setTag(Integer.valueOf(i));
//			if (indexItemEquipmentSelected == i) {
//				row.render(item, true);
//			} else {
				row.render(item);
//			}
			row.setListener(this);
			soThietBi += item.soThietBi;
			khongDat += item.khongDat;
			listRows.add(row);
		}
		TBHVManagerEquipmentDetailAllRow rowTongCong = new TBHVManagerEquipmentDetailAllRow(parent);
		rowTongCong.render(soThietBi, khongDat);
		listRows.add(rowTongCong);
		tbEquipment.addContent(listRows);
		this.parent.closeProgressDialog();
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_TBHV_LIST_EQUIPMENT:
			dto = (TBHVManagerEquipmentDTO) modelEvent.getModelData();
			renderLayout();
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.viettel.vinamilk.view.main.BaseFragment#handleErrorModelViewEvent(com.viettel.vinamilk.global.ModelEvent)
	 */
	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_TBHV_LIST_EQUIPMENT:
			this.parent.closeProgressDialog();
			break;
		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			parent = (TBHVActivity) activity;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		if (eventType == ACTION_ROW_CLICK) { // 1 row clicked
			isFirst = false;
			goToTrackingCabinetStaffView((TBHVManagerEquipmentItem) data);
		}
	}

	/**
	 * 
	*  Toi man hinh quan ly thiet bi cua NVBH
	*  @author: Nguyen Thanh Dung
	*  @param data
	*  @return: void
	*  @throws:
	 */
	private void goToTrackingCabinetStaffView(TBHVManagerEquipmentItem data) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_STAFF_ID, data.id);
		b.putString(IntentConstants.INTENT_STAFF_CODE, data.maNVBH);
		b.putString(IntentConstants.INTENT_STAFF_NAME, data.nvbh);
		b.putString(IntentConstants.INTENT_SHOP_ID, shopId);
		b.putString(IntentConstants.INTENT_SHOP_CODE, shopCode);
		b.putString(IntentConstants.INTENT_SHOP_NAME, shopName);
		boolean isShowViewAll = true;
		if(data.soThietBi == data.khongDat){
			isShowViewAll = false;
		}
		b.putBoolean(IntentConstants.INTENT_IS_VIEW_ALL, isShowViewAll);
		e.viewData = b;
		e.action = ActionEventConstant.GO_TO_TBHV_TRACKING_CABINET_STAFF;
		TBHVController.getInstance().handleSwitchFragment(e);
	}
	
	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
	      switch (action) {
	      case ActionEventConstant.NOTIFY_REFRESH_VIEW:
	            if(this.isVisible()){
	            	getDanhSachThietBi();
	            }
	            break;
	      default:
	            super.receiveBroadcast(action, extras);
	            break;
	      }
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub

	}

}
