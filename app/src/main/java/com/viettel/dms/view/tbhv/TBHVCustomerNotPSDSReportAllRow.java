/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 * row tong cong cua man hinh khach hang chua PSDS cua TBHV
 * 
 * TBHVCustomerNotPSDSReportAllRow.java
 * @author: QuangVT
 * @version: 1.0 
 * @since:  4:49:08 PM Dec 16, 2013
 */
public class TBHVCustomerNotPSDSReportAllRow extends TableRow {
	private View view;
		
	private TextView tvTotal; 
	private TextView tvNumCusVisit;
	private TextView tvNumCusPSDS;
	private TextView tvNumCusNotPSDS;
	
	/**
	 * Ham khoi tao
	 * @param context
	 */
	public TBHVCustomerNotPSDSReportAllRow(Context context) {
		super(context);		
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_tbhv_custommer_not_psds_report_all_row, this);
		
		tvTotal 		= (TextView) view.findViewById(R.id.tvTotal);
		tvNumCusVisit   = (TextView) view.findViewById(R.id.tvNumCusVisit);
		tvNumCusPSDS    = (TextView) view.findViewById(R.id.tvNumCusPSDS);
		tvNumCusNotPSDS = (TextView) view.findViewById(R.id.tvNumCusNotPSDS); 
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
	public void render(int numCusVisit, int numCusPSDS, int numCusNotPSDS) {	
		final String strNumCusVisit   = StringUtil.parseAmountMoney(numCusVisit);
		final String strNumCusPSDS    = StringUtil.parseAmountMoney(numCusPSDS);
		final String strNumCusNotPSDS = StringUtil.parseAmountMoney(numCusNotPSDS);
		
		tvTotal.setText(StringUtil.getString(R.string.TEXT_HEADER_TABLE_SUM)); 
		tvNumCusVisit.setText(strNumCusVisit); 
		tvNumCusPSDS.setText(strNumCusPSDS); 
		tvNumCusNotPSDS.setText(strNumCusNotPSDS); 
	}
}
