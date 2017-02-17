/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.viettel.dms.dto.view.TBHVCustomerNotPSDSReportDTO.TBHVCustomerNotPSDSReportItem;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * Row dung cho man hinh khach hang chua PSDS cua TBHV
 * 
 * TBHVCustomerNotPSDSReportRow.java
 * @author: QuangVT
 * @version: 1.0 
 * @since:  1:34:36 PM Dec 16, 2013
 */
public class TBHVCustomerNotPSDSReportRow extends DMSTableRow implements OnClickListener {
	
	/*-----------------ACTION CONSTANT-----------------*/
	//--- Action khi click ma GS
	public int ACTION_NPP_CLICK = 0;
	public int ACTION_GSBH_CLICK = 1;
	
	/*-----------------COLUMNS-----------------*/
	private TextView tvNPP;			// cot ma nha phan phoi
	private TextView tvGSBH;		// cot ten Giam sat 
	private TextView tvTotalCus;	// cot Tong so khach hang ghe tham trong thang
	private TextView tvPSDSInMonth;	// cot so KH PSDS trong thang
	private TextView tvNotPSDS; 	// cot so KH chua PSDS trong thang
	
	/*-----------------COLUMNS-----------------*/
	//--- View parent of 

	/*-----------------LISTENER-----------------*/
	//--- Listener when click row
	public OnEventControlListener listener;
	
	/*-----------------ITEMS-----------------*/
	//--- Item for render
	private TBHVCustomerNotPSDSReportItem item;

	/**
	 * Ham khoi tao
	 * 
	 * @param context
	 * @param actionMNVClick
	 * @param listener
	 */
	public TBHVCustomerNotPSDSReportRow(Context context, VinamilkTableListener lis, int nppClick, int gsbhClick, OnEventControlListener listener) {
		super(context, R.layout.layout_tbhv_custommer_not_psds_report_row);
		setListener(lis);
		ACTION_NPP_CLICK  = nppClick;
		ACTION_GSBH_CLICK = gsbhClick;
		this.listener = listener;
		this.setOnClickListener(this);
		tvNPP  = (TextView) this.findViewById(R.id.tvNPP);
		tvNPP.setOnClickListener(this);
		tvGSBH = (TextView) this.findViewById(R.id.tvGSBH);
		tvGSBH.setOnClickListener(this);
		tvTotalCus    = (TextView) this.findViewById(R.id.tvTotalCustomer);
		tvPSDSInMonth = (TextView) this.findViewById(R.id.tvPSDSInMonth);
		tvNotPSDS     = (TextView) this.findViewById(R.id.tvNoPSDS); 
	}

	/**
	 * Render du lieu
	 * 
	 * @author: QuangVT
	 * @since: 1:40:08 PM Dec 16, 2013
	 * @return: void
	 * @throws:  
	 * @param item
	 */
	public void render(TBHVCustomerNotPSDSReportItem item) {
		// Save item
		this.item = item;
		
		//Get info fom item
		final String strNPP  = item.NPPCode;
		final String strGSBH = item.GSBHName;
		final String strNumCusVisit   = StringUtil.parseAmountMoney(item.numCusVisit);
		final String strNumCusPSDS    = StringUtil.parseAmountMoney(item.numCusPSDS);
		final String strNumCusNotPSDS = StringUtil.parseAmountMoney(item.numCusNotPSDS);
		
		// Render 
		tvNPP.setText(strNPP);
		tvGSBH.setText(strGSBH);
		tvTotalCus.setText(strNumCusVisit);
		tvPSDSInMonth.setText(strNumCusPSDS);
		tvNotPSDS.setText(strNumCusNotPSDS);
	}
	/**
	 * Render du lieu
	 * 
	 * @author: QuangVT
	 * @since: 4:51:22 PM Dec 19, 2013
	 * @return: void
	 * @throws:  
	 * @param numCusVisit
	 * @param numCusPSDS
	 * @param numCusNotPSDS
	 */
	public void renderTotal(int numCusVisit, int numCusPSDS, int numCusNotPSDS) {
		showRowSum(StringUtil.getString(R.string.TEXT_HEADER_TABLE_SUM), tvNPP,tvGSBH);
		final String strNumCusVisit   = StringUtil.parseAmountMoney(numCusVisit);
		final String strNumCusPSDS    = StringUtil.parseAmountMoney(numCusPSDS);
		final String strNumCusNotPSDS = StringUtil.parseAmountMoney(numCusNotPSDS);
		
		tvTotalCus.setText(strNumCusVisit); 
		tvPSDSInMonth.setText(strNumCusPSDS); 
		tvNotPSDS.setText(strNumCusNotPSDS); 
	}

	@Override
	public void onClick(View v) { 
		if (listener != null) {
			if(v == tvNPP){
				listener.onEvent(ACTION_NPP_CLICK, v, item);
			}else if(v == tvGSBH){
				listener.onEvent(ACTION_GSBH_CLICK, v, item);
			} 
		}
	}
}
