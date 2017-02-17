/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv.statistic;

import android.content.Context;
import android.graphics.Color;
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
 * Row for GT Month report
 * GSTGeneralStatisticsMonthRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  19:33:12 5 Dec 2013
 */
public class GSBHGeneralStatisticsMonthRow extends DMSTableRow  implements OnClickListener {
	private int ACTION_SHOW_DETAIL = 1;
	
	private boolean isTotalRow = false;
	private TextView tvStaffCode;
	private TextView tvStaffName;
	private TextView tvKeHoach;
	private TextView tvThucHien;
	private TextView tvConLai;
	private TextView tvTienDo;
	private TextView text;
	private GeneralStatisticsInfo info;
	
//	public static HeaderTableInfo[] getTableHeaderGSBH(Context con){
//		return getTableHeader(con, StringUtil.getString(R.string.TEXT_LABLE_NPP), StringUtil.getString(R.string.TEXT_HEADER_INFO_GSBH_REPORT));
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
//			GSBHGeneralStatisticsMonthRow row = new GSBHGeneralStatisticsMonthRow(con);
//			TABLE_HEADER = new HeaderTableInfo[]{
//				 new HeaderTableInfo( row.tvStaffCode.getText().toString(), row.tvStaffCode.getLayoutParams().width)
//				,new HeaderTableInfo( row.tvStaffName.getText().toString(), row.tvStaffName.getLayoutParams().width)
//				,new HeaderTableInfo ( StringUtil.getString(R.string.TEXT_TITLE_REPORT_QUANTITY_SALE),0, new HeaderTableInfo[]{
//                            				 new HeaderTableInfo( row.tvKeHoach.getText().toString(), row.tvKeHoach.getLayoutParams().width)
//                            				,new HeaderTableInfo( row.tvThucHien.getText().toString(), row.tvThucHien.getLayoutParams().width)
//                            				,new HeaderTableInfo( row.tvConLai.getText().toString(), row.tvConLai.getLayoutParams().width)
//                            				 ,new HeaderTableInfo( row.tvTienDo.getText().toString(), row.tvTienDo.getLayoutParams().width)
//                    				})
//			};
//		}
//		
//		//change objectCode and objectName
//		TABLE_HEADER[0].text = objectCode;
//		TABLE_HEADER[1].text = objectName;
//		return TABLE_HEADER;
//	}
//
//	private static HeaderTableInfo[] TABLE_HEADER_TNPG = null;
//	public static HeaderTableInfo[] getTableHeaderTNPG(Context con){
//		if (TABLE_HEADER_TNPG == null) {
//			GSBHGeneralStatisticsMonthRow row = new GSBHGeneralStatisticsMonthRow(con);
//			TABLE_HEADER_TNPG = new HeaderTableInfo[]{
//				 new HeaderTableInfo( StringUtil.getString(R.string.TEXT_HEADER_TABLE_STAFF_CODE), row.tvStaffCode.getLayoutParams().width)
//				,new HeaderTableInfo( StringUtil.getString(R.string.TEXT_HEADER_INFO_TNPG_REPORT), row.tvStaffName.getLayoutParams().width)
//                ,new HeaderTableInfo( row.tvKeHoach.getText().toString(), row.tvKeHoach.getLayoutParams().width)
//                ,new HeaderTableInfo( row.tvThucHien.getText().toString(), row.tvThucHien.getLayoutParams().width)
//                ,new HeaderTableInfo( row.tvConLai.getText().toString(), row.tvConLai.getLayoutParams().width)
//                ,new HeaderTableInfo( row.tvTienDo.getText().toString(), row.tvTienDo.getLayoutParams().width)
//			};
//		}
//		
//		return TABLE_HEADER_TNPG;
//	}
	
	public GSBHGeneralStatisticsMonthRow(Context context, VinamilkTableListener listener, boolean isTotalRow, int actionShowDetail) {
		super(context, R.layout.layout_gst_general_month_report_row);
		this.isTotalRow = isTotalRow;
		this.ACTION_SHOW_DETAIL = actionShowDetail;
		setListener(listener);
		tvStaffCode = (TextView) this.findViewById(R.id.tvStaffCode);
		tvStaffName = (TextView) this.findViewById(R.id.tvStaffName);
		tvKeHoach = (TextView) this.findViewById(R.id.tvKeHoach);
		tvThucHien = (TextView) this.findViewById(R.id.tvThucHien);
		tvConLai = (TextView) this.findViewById(R.id.tvConLai);
		tvTienDo = (TextView) this.findViewById(R.id.tvTienDo);
		
		int color = ImageUtil.getColor(R.color.COLOR_REMAIN);
		tvConLai.setBackgroundColor(color);
		text = (TextView) this.findViewById(R.id.text);
		//set onclick event
		tvStaffName.setOnClickListener(this);
		tvStaffCode.setOnClickListener(this);
		this.setOnClickListener(this);
	}
	
	public void render(GeneralStatisticsInfo info){
		this.info = info;
		if (isTotalRow) {
			showRowSum("Tổng", tvStaffCode, tvStaffName);
		}else{
			//case normal row
			tvStaffCode.setText(info.tvObjectCode);
			tvStaffName.setText(info.tvObjectName);
		}
		
		String strKeHoach = StringUtil.parseAmountMoney(info.tvKeHoachThang);
		String strThucHien = StringUtil.parseAmountMoney(info.tvThucHienThang);
		String strConLai = StringUtil.parseAmountMoney(info.tvConLaiThang);
		String strTienDoThang = StringUtil.parsePercent(info.tvTienDoThang);
		
		tvKeHoach.setText(strKeHoach);
		tvThucHien.setText(strThucHien);
		tvConLai.setText(strConLai);
		tvTienDo.setText(strTienDoThang);
		
		// check hightlight
		if (info.isHightLight) {
			tvTienDo.setTextColor(Color.RED);
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
	public void renderHeaderGSBHMonth() {
			tvStaffCode.setText(StringUtil.getString(R.string.TEXT_HEADER_TABLE_STAFF_CODE));
			tvStaffName.setText(StringUtil.getString(R.string.TEXT_HEADER_TABLE_NVBH));
			text.setText(StringUtil.getString(R.string.TEXT_TITLE_REPORT_QUANTITY_SALE));
	}
	@Override
	public void onClick(View v) {
		// case other views
		if (v == tvStaffName || v == tvStaffCode) {
			if (listener != null) {
				listener.handleVinamilkTableRowEvent(ACTION_SHOW_DETAIL, v, info);
			}
		}
	}

	
}
