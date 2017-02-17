/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv.statistic;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.viettel.dms.dto.view.GeneralStatisticsViewDTO.GeneralStatisticsInfo;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * Row for GST Date report 
 * GSTGeneralStatisticsDateRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  19:30:00 5 Dec 2013
 */
public class GSBHGeneralStatisticsDateRow extends DMSTableRow  implements OnClickListener {
	private int ACTION_SHOW_DETAIL_WITH_STAFF = 1;
	private int ACTION_SHOW_DETAIL_WITH_NPP = 2;
	
	private boolean isTotalRow = false;
	private TextView tvStaffCode;
	private TextView tvStaffName;
	private TextView tvKeHoach;
	private TextView tvThucHien;
	private TextView tvConLai;
	TextView text;

	
//	public static HeaderTableInfo[] getTableHeaderGSBH(Context con) {
//		return getTableHeader(con, StringUtil.getString(R.string.TEXT_LABLE_NPP), StringUtil.getString(R.string.TEXT_HEADER_GSBH_REPORT));
//	}
//	
//	public static HeaderTableInfo[] getTableHeaderDetailInfoGSBH(Context con) {
//		return getTableHeader(con, StringUtil.getString(R.string.TEXT_HEADER_TABLE_STAFF_CODE), StringUtil.getString(R.string.TEXT_HEADER_TABLE_NVBH));
//	}
//	
//	//header table of this row
//	private static HeaderTableInfo[] TABLE_HEADER = null;
//	private static HeaderTableInfo[] getTableHeader(Context con, String objectCode, String objectName){
//		if (TABLE_HEADER == null) {
//			GSBHGeneralStatisticsDateRow row = new GSBHGeneralStatisticsDateRow(con);
//			row.tvStaffCode.setText("");
//			row.tvStaffName.setText("");
//			TABLE_HEADER = new HeaderTableInfo[]{
//				 new HeaderTableInfo( row.tvStaffCode.getText().toString(), row.tvStaffCode.getLayoutParams().width)
//				,new HeaderTableInfo( row.tvStaffName.getText().toString(), row.tvStaffName.getLayoutParams().width)
//				,new HeaderTableInfo ( StringUtil.getString(R.string.TEXT_TITLE_REPORT_QUANTITY_SALE),0, new HeaderTableInfo[]{
//                				 new HeaderTableInfo( row.tvKeHoach.getText().toString(), row.tvKeHoach.getLayoutParams().width)
//                				,new HeaderTableInfo( row.tvThucHien.getText().toString(), row.tvThucHien.getLayoutParams().width)
//                				,new HeaderTableInfo( row.tvConLai.getText().toString(), row.tvConLai.getLayoutParams().width)
//				 })
//				 
//			};
//		}
//		//change objectCode and objectName
//		TABLE_HEADER[0].text = objectCode;
//		TABLE_HEADER[1].text = objectName;
//		return TABLE_HEADER;
//	}
//
//	private static HeaderTableInfo[] TABLE_HEADER_TNPG = null;
//	public static HeaderTableInfo[] getTableHeaderTNPG(Context con){
//		if (TABLE_HEADER_TNPG == null) {
//			GSBHGeneralStatisticsDateRow row = new GSBHGeneralStatisticsDateRow(con);
//			row.tvStaffCode.setText(StringUtil.getString(R.string.TEXT_HEADER_TABLE_STAFF_CODE));
//			row.tvStaffName.setText( StringUtil.getString(R.string.TEXT_HEADER_TNPG_REPORT));
//			TABLE_HEADER_TNPG = new HeaderTableInfo[]{
//				 new HeaderTableInfo( row.tvStaffCode.getText().toString(), row.tvStaffCode.getLayoutParams().width)
//				,new HeaderTableInfo( row.tvStaffName.getText().toString(), row.tvStaffName.getLayoutParams().width)
//                ,new HeaderTableInfo( row.tvKeHoach.getText().toString(), row.tvKeHoach.getLayoutParams().width)
//                ,new HeaderTableInfo( row.tvThucHien.getText().toString(), row.tvThucHien.getLayoutParams().width)
//                ,new HeaderTableInfo( row.tvConLai.getText().toString(), row.tvConLai.getLayoutParams().width)				 
//			};
//		}
//		return TABLE_HEADER_TNPG;
//	}
	
	public GSBHGeneralStatisticsDateRow(Context context, VinamilkTableListener listener, 
			boolean isTotalRow, int actionShowDetailWithStaff, int actionShowDetailWithNPP) {
		super(context, R.layout.layout_gst_general_date_report_row);
		setListener(listener);
		this.isTotalRow = isTotalRow;
		this.ACTION_SHOW_DETAIL_WITH_STAFF = actionShowDetailWithStaff;
		this.ACTION_SHOW_DETAIL_WITH_NPP = actionShowDetailWithNPP;
		tvStaffCode = (TextView) this.findViewById(R.id.tvStaffCode);
		tvStaffName = (TextView) this.findViewById(R.id.tvStaffName);
		tvKeHoach = (TextView) this.findViewById(R.id.tvKeHoach);
		tvThucHien = (TextView) this.findViewById(R.id.tvThucHien);
		tvConLai = (TextView) this.findViewById(R.id.tvConLai);
		int color = ImageUtil.getColor(R.color.COLOR_REMAIN);
		text = (TextView) this.findViewById(R.id.text);
		tvConLai.setBackgroundColor(color);
		
		//set onclick event
		this.setOnClickListener(this);
		tvStaffCode.setOnClickListener(this);
		tvStaffName.setOnClickListener(this);
	}
	
	private GeneralStatisticsInfo info;
	public void render(GeneralStatisticsInfo info){
		this.info = info;
		if (isTotalRow) {
			showRowSum("Tổng", tvStaffCode, tvStaffName);
		}else{
			//case normal row
			tvStaffCode.setText(info.tvObjectCode);
			tvStaffName.setText(info.tvObjectName);
		}
		
		String strKeHoach = StringUtil.parseAmountMoney(info.tvKeHoach);
		String strThucHien = StringUtil.parseAmountMoney(info.tvThucHien);
		String strConLai = StringUtil.parseAmountMoney(info.tvConLai);

		tvKeHoach.setText(strKeHoach);
		tvThucHien.setText(strThucHien);
		tvConLai.setText(strConLai);	
	}

	@Override
	public void onClick(View v) {
		// case other views
		if (listener != null) {
			if (v == tvStaffCode) {
				listener.handleVinamilkTableRowEvent(ACTION_SHOW_DETAIL_WITH_NPP, v, info);
			} else if (v == tvStaffName) {
				listener.handleVinamilkTableRowEvent(ACTION_SHOW_DETAIL_WITH_STAFF, v, info);
			}
		}
	}

	/**
	 * @author: yennth16
	 * @since: 16:39:09 23-04-2015
	 * @return: void
	 * @throws:  
	 */
	public void renderHeaderGSBH() {
			tvStaffCode.setText(StringUtil.getString(R.string.TEXT_LABLE_NPP));
			tvStaffName.setText(StringUtil.getString(R.string.TEXT_HEADER_GSBH_REPORT));
			text.setText(StringUtil.getString(R.string.TEXT_TITLE_REPORT_QUANTITY_SALE));
	}
	/**
	 * @author: yennth16
	 * @since: 16:39:09 23-04-2015
	 * @return: void
	 * @throws:  
	 */
	public void renderHeaderTNPG() {
			tvStaffCode.setText(StringUtil.getString(R.string.TEXT_HEADER_TABLE_STAFF_CODE));
			tvStaffName.setText(StringUtil.getString(R.string.TEXT_HEADER_TNPG_REPORT));
			text.setText(StringUtil.getString(R.string.TEXT_SALES_2));
	}
	/**
	 * @author: yennth16
	 * @since: 16:39:09 23-04-2015
	 * @return: void
	 * @throws:  
	 */
	public void renderHeaderGSBHDateDetail() {
			tvStaffCode.setText(StringUtil.getString(R.string.TEXT_HEADER_TABLE_STAFF_CODE));
			tvStaffName.setText(StringUtil.getString(R.string.TEXT_HEADER_TABLE_NVBH));
			text.setText(StringUtil.getString(R.string.TEXT_TITLE_REPORT_QUANTITY_SALE));
	}

	}
