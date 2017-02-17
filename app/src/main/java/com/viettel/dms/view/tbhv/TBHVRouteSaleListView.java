/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.TBHVRouteSaleListDTO;
import com.viettel.dms.dto.view.TBHVRouteSaleListDTO.TBHVRouteSaleListItem;
import com.viettel.dms.dto.view.TBHVRouteSupervisionDTO.STATUS;
import com.viettel.dms.dto.view.TBHVRouteSupervisionDTO.THBVRouteSupervisionItem;
import com.viettel.dms.dto.view.TBHVRouteSupervisionDTO.THBVRouteSupervisionItem.Customer;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.main.TBHVActivity;
import com.viettel.sabeco.R;

/**
 * TBHV Route Sale List View
 * TBHVRouteSaleListView.java
 * @version: 1.0 
 * @since:  08:30:40 20 Jan 2014
 */
public class TBHVRouteSaleListView extends ScrollView implements OnClickListener {

	public static final int ACTION_VIEW_ALL = 333;
	public static final int ACTION_CLOSE_DIALOG = 444;

	private TBHVActivity parent;
	public View viewLayout;
	private TextView tvGSNPP;
	private TextView tvNVBH;
	private Button btClose;
	private Button btXemTatCa;
	private THBVRouteSupervisionItem aRouteSupervisionItem;
	private TBHVRouteSaleListDTO dto;
	protected OnEventControlListener listener;
	private VinamilkTableView tbRouteSaleList;

	public TBHVRouteSaleListView(Context context, THBVRouteSupervisionItem aRouteSupervisionItem,
			OnEventControlListener listener, int type) {
		super(context);
		parent = (TBHVActivity) context;
		this.listener = listener;
		LayoutInflater inflater = this.parent.getLayoutInflater();
		viewLayout = inflater.inflate(R.layout.layout_tbhv_route_sale_list_view, null);
		tvGSNPP = (TextView) viewLayout.findViewById(R.id.tvGSNPP);
		tvNVBH = (TextView) viewLayout.findViewById(R.id.tvNVBH);

		tbRouteSaleList = (VinamilkTableView) viewLayout.findViewById(R.id.tbSaleList);
		btClose = (Button) viewLayout.findViewById(R.id.btClose);
		btClose.setOnClickListener(this);
		btXemTatCa = (Button) viewLayout.findViewById(R.id.btXemTatCa);
		btXemTatCa.setOnClickListener(this);
		this.aRouteSupervisionItem = aRouteSupervisionItem;

		initData(aRouteSupervisionItem, type);
		renderLayout(dto);
	}

	/**
	 * init Data
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void initData(THBVRouteSupervisionItem aRouteSupervisionItem, int type) {
		dto = new TBHVRouteSaleListDTO();
		dto.nameGSNPP = aRouteSupervisionItem.staffNameGS;
		dto.nameNVBH = aRouteSupervisionItem.staffNameNVBH;
		dto.arrList.clear();
		if (type == ActionEventConstant.TBHV_ROUTE_SUPERVISION_TRAINING_CUS) {
			TBHVRouteSaleListItem dialogItem = dto.newTBHVRouteSaleLstItem();
			Customer cus = aRouteSupervisionItem.gsnppRealSeq.get(aRouteSupervisionItem.gsnppRealSeq.size() - 1);
			dialogItem.khachHang = cus.codeName;
			dialogItem.diaChi = cus.address;
			dialogItem.thuTuNVBH = aRouteSupervisionItem.getRealSequence(
					aRouteSupervisionItem.gsnppRealSeq.get(aRouteSupervisionItem.gsnppRealSeq.size() - 1).cusId,
					aRouteSupervisionItem.nvbhRealSeq);
			dialogItem.thuTuGSNPP = aRouteSupervisionItem.gsnppRealSeq.size();
			if (dialogItem.thuTuNVBH > 0) {
				dialogItem.thoiGianNVBH = aRouteSupervisionItem.nvbhRealSeq.get(dialogItem.thuTuNVBH - 1).visit_start_time;
				dialogItem.isNVBHVisiting = aRouteSupervisionItem.getCusFromRealSequence(dialogItem.thuTuNVBH - 1,
						aRouteSupervisionItem.nvbhRealSeq).isVisting;
			}
			dialogItem.thoiGianGSNPP = aRouteSupervisionItem.gsnppRealSeq.get(dialogItem.thuTuGSNPP - 1).visit_start_time;

			dialogItem.isNPPVisiting = true;

			if (cus.status == STATUS.WRONG_PLAN || cus.status == STATUS.GSNPP_VISITED_BUT_NVBH) {
				dialogItem.isWrongPlan = true;
			}

			dto.arrList.add(dialogItem);
		} else if (type == ActionEventConstant.TBHV_ROUTE_SUPERVISION_WRONG_CUS) {
			for (int i = 0; i < aRouteSupervisionItem.nvbhRealSeq.size(); i++) {
				Customer nvbhCus = aRouteSupervisionItem.nvbhRealSeq.get(i);
				Customer gsnppCus = getCusInList(nvbhCus, aRouteSupervisionItem.gsnppRealSeq);
				if (nvbhCus.status == STATUS.NVBH_VISITED_BUT_GSNPP
						|| (gsnppCus != null && gsnppCus.status == STATUS.WRONG_PLAN)) {
					TBHVRouteSaleListItem dialogItem = dto.newTBHVRouteSaleLstItem();
					dialogItem.intWrongCus(nvbhCus, gsnppCus);
					dto.arrList.add(dialogItem);
				}
			}
			for (int i = 0; i < aRouteSupervisionItem.gsnppRealSeq.size(); i++) {
				Customer gsnppCus = aRouteSupervisionItem.gsnppRealSeq.get(i);
				if (gsnppCus.status == STATUS.GSNPP_VISITED_BUT_NVBH) {
					TBHVRouteSaleListItem dialogItem = dto.newTBHVRouteSaleLstItem();
					dialogItem.intWrongCus(null, gsnppCus);
					dto.arrList.add(dialogItem);
				}
			}
		} else if (type == ActionEventConstant.TBHV_ROUTE_SUPERVISION_VISITED_CUS) {
			initDataViewAll(aRouteSupervisionItem);
			btXemTatCa.setVisibility(View.GONE);
		}

	}

	/**
	 * Mo ta muc dich cua ham
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void initDataViewAll(THBVRouteSupervisionItem aRouteSupervisionItem) {
		dto.arrList.clear();
		for (int i = 0; i < aRouteSupervisionItem.nvbhRealSeq.size(); i++) {
			Customer nvbhCus = aRouteSupervisionItem.nvbhRealSeq.get(i);
			Customer gsnppCus = getCusInList(nvbhCus, aRouteSupervisionItem.gsnppRealSeq);
			TBHVRouteSaleListItem dialogItem = dto.newTBHVRouteSaleLstItem();
			dialogItem.intWrongCus(nvbhCus, gsnppCus);
			dto.arrList.add(dialogItem);
		}

		for (int i = 0; i < aRouteSupervisionItem.gsnppRealSeq.size(); i++) {
			Customer gsnppCus = aRouteSupervisionItem.gsnppRealSeq.get(i);
			if (gsnppCus.status == STATUS.GSNPP_VISITED_BUT_NVBH) {
				TBHVRouteSaleListItem dialogItem = dto.newTBHVRouteSaleLstItem();
				dialogItem.intWrongCus(null, gsnppCus);
				dto.arrList.add(dialogItem);
			}
		}
	}

	private void renderLayout(TBHVRouteSaleListDTO dto) {
		if (dto != null) {
			tvGSNPP.setText(dto.nameGSNPP);
			tvNVBH.setText(dto.nameNVBH);
			List<TableRow> listRows = new ArrayList<TableRow>();
			if (dto.arrList.size() > 0) {
				for (int i = 0, s = dto.arrList.size(); i < s; i++) {
					TBHVRouteSaleListRow row = new TBHVRouteSaleListRow(parent);
					row.renderLayout(dto.arrList.get(i));
					listRows.add(row);
				}
				tbRouteSaleList.addContent(listRows);
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btClose) {
			listener.onEvent(ACTION_CLOSE_DIALOG, this, dto);
		} else if (v == btXemTatCa) {
			initDataViewAll(aRouteSupervisionItem);
			renderLayout(dto);
			btXemTatCa.setVisibility(View.GONE);
		}
	}

	/**
	 * getCusInList
	 * 
	 * @author: TamPQ
	 * @return: ArrayList<String>
	 * @throws:
	 */
	public Customer getCusInList(Customer cus, ArrayList<Customer> list) {
		Customer cust = null;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).cusId == cus.cusId) {
				return list.get(i);
			}
		}
		return cust;
	}

}
