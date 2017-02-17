/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.viettel.dms.dto.control.SpinnerItemDTO;
import com.viettel.dms.dto.view.TBHVRouteSupervisionDTO;
import com.viettel.dms.dto.view.TBHVRouteSupervisionDTO.THBVRouteSupervisionItem;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpinerRoute;
import com.viettel.dms.view.control.VNMSpinnerTextAdapter;
import com.viettel.sabeco.R;

/**
 * 
 * header MH theo doi vi tri role GST
 * 
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVGsnppNvbhPositionHeader extends LinearLayout{
	public CheckBox cbGsnpp;// check box gsnpp
	public SpinerRoute spinnerStaffCode;// ma gsnpp
	public CheckBox cbTttt;// check box tttt
	// spiner ds Gsnpp
	public VNMSpinnerTextAdapter adapterGsnpp;
	// luu chi so nhan vien dang duoc chon
	public int selectedSpinnerIndex = -1;
	Context mContext;
	public TBHVGsnppNvbhPositionHeader(Context context) {
		super(context);
		mContext= context;
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.layout_gst_follow_position, this);
		cbGsnpp = (CheckBox) view.findViewById(R.id.cbGsnpp);
		spinnerStaffCode = (SpinerRoute) view.findViewById(R.id.spinnerStaffCode);
		spinnerStaffCode.setMinimumWidth(300);
		cbTttt = (CheckBox) view.findViewById(R.id.cbTttt);
		
	}

	/**
	 * 
	 * render layout
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	public void updateView(TBHVRouteSupervisionDTO gsnppList){
		Vector<SpinnerItemDTO> listGsnppItem = new Vector<SpinnerItemDTO>();
		THBVRouteSupervisionItem item = gsnppList.newTHBVRouteSupervisionItem();
		SpinnerItemDTO spinnerItem;
		spinnerItem = new SpinnerItemDTO();
		spinnerItem.name = StringUtil.getString(R.string.TEXT_POSITION_GSNPP);
		listGsnppItem.add(spinnerItem);
		for (int i = 0, size = gsnppList.itemGSNPPList.size(); i < size; i++) {
			item = gsnppList.itemGSNPPList.get(i);
			spinnerItem = new SpinnerItemDTO();
			spinnerItem.name = item.staffCodeGS + " - " + item.staffNameGS;
			if (item.lat <= 0 || item.lng <= 0) {
				spinnerItem.content = StringUtil.getString(R.string.TEXT_CANT_LOCATE_YOUR_POSITION);
			}
			listGsnppItem.add(spinnerItem);
		}
		adapterGsnpp = new VNMSpinnerTextAdapter(mContext.getApplicationContext(), R.layout.simple_spinner_item, listGsnppItem);
		adapterGsnpp.setHint(StringUtil.getString(R.string.TEXT_POSITION_GSNPP));
		spinnerStaffCode.setAdapter(adapterGsnpp);
		selectedSpinnerIndex = 0;
		spinnerStaffCode.setSelection(selectedSpinnerIndex);
		adapterGsnpp.notifyDataSetChanged();
		
	}

}
