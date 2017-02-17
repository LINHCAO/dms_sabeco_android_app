/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

import android.database.Cursor;

import com.viettel.dms.sqllite.db.DISPLAY_PROGRAME_TABLE;
import com.viettel.dms.sqllite.db.RPT_DISPLAY_PROGRAME_TABLE;
import com.viettel.dms.sqllite.db.SHOP_TABLE;
import com.viettel.dms.sqllite.db.STAFF_TABLE;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * Mo ta muc dich cua lop (interface)
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class TBHVDisProComProgReportItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// id nha phan phoi
	public String idNPP;
	// id giam sat nha phan phoi
	public String idGSNPP;
	// id chuong trinh Trung bày
	public String programId;
	// code nha phan phoi
	public String codeNPP;
	// ma giam sat nha phan phoi
	public String codeGSNPP;
	// ma CTTB
	public String programCode;
	// ma short CTTB
	public String programShortCode;
	// ma Level
	public String programLevel;
	// ten nha phan phoi
	public String nameNPP;
	// ten giam sat nha phan phoi
	public String nameGSNPP;
	// ten CTTB
	public String programName;
	// ten short CTTB
	public String programShortName;
	// thoi gian
	public String dateFromTo;
	// so chua phat sinh doanh so
	public String resultNumber;
	// so tham gia
	public String joinNumber;
	public ArrayList<TBHVDisProComProgReportItemResult> arrLevelCode;
	public TBHVDisProComProgReportItemResult itemResultTotal;

	public TBHVDisProComProgReportItem() {
		arrLevelCode = new ArrayList<TBHVDisProComProgReportItemResult>();
		itemResultTotal = new TBHVDisProComProgReportItemResult();
		nameNPP = "";
		nameGSNPP = "";
		programCode = "";
		programName = "";
		dateFromTo = "";
	}

	public TBHVDisProComProgReportItem(Cursor c) {
		idNPP = c.getString(c.getColumnIndex(SHOP_TABLE.SHOP_ID));
		codeNPP = c.getString(c.getColumnIndex(SHOP_TABLE.SHOP_CODE));
		nameNPP = c.getString(c.getColumnIndex(SHOP_TABLE.SHOP_NAME));
		idGSNPP = c.getString(c.getColumnIndex(STAFF_TABLE.STAFF_ID));
		codeGSNPP = c.getString(c.getColumnIndex(STAFF_TABLE.STAFF_CODE));
		nameGSNPP = c.getString(c.getColumnIndex(STAFF_TABLE.STAFF_NAME));
		programCode = c.getString(c
				.getColumnIndex(DISPLAY_PROGRAME_TABLE.DISPLAY_PROGRAM_CODE));
		programName = c.getString(c
				.getColumnIndex(DISPLAY_PROGRAME_TABLE.DISPLAY_PROGRAM_NAME));
		programLevel = c
				.getString(c
						.getColumnIndex(RPT_DISPLAY_PROGRAME_TABLE.DISPLAY_PROGRAME_LEVEL));
	}

	/**
	 * new DisProComProgReportItemResult
	 * 
	 * @return
	 */
	public TBHVDisProComProgReportItemResult newDisProComProgReportItemResult() {
		return new TBHVDisProComProgReportItemResult();
	}

	/**
	 * 
	 * init data with cursor
	 * 
	 * @param c
	 * @param dto
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 30, 2012
	 */
	public void initWithCursor(Cursor c, TBHVDisProComProgReportDTO dto) {
		idNPP = c.getString(c.getColumnIndex("NPP_ID"));
		codeNPP = c.getString(c.getColumnIndex("NPP_CODE"));
		nameNPP = c.getString(c.getColumnIndex("NPP_NAME"));
		idGSNPP = c.getString(c.getColumnIndex("GSNPP_ID"));
		codeGSNPP = c.getString(c.getColumnIndex("GSNPP_CODE"));
		nameGSNPP = c.getString(c.getColumnIndex("GSNPP_NAME"));
		
		programShortCode = c.getString(c.getColumnIndex("DP_CODE"));
		programShortName = c.getString(c.getColumnIndex("DP_NAME"));
		programLevel = c.getString(c.getColumnIndex("DP_LEVEL"));
		int joinNumber = c.getInt(c.getColumnIndex("NUM_JOIN"));
		int resultNumber = c.getInt(c.getColumnIndex("NUM_NON_COMPLETE"));
		try {

			for (int i = 0; i < dto.arrLevelCode.size(); i++) {
				arrLevelCode.add(newDisProComProgReportItemResult());
				if (programLevel.equals(dto.arrLevelCode.get(i))) {
					arrLevelCode.get(i).joinNumber = joinNumber;
					arrLevelCode.get(i).resultNumber = resultNumber;
					TBHVDisProComProgReportItemResult rs = dto.arrResultTotal
							.get(i);
					TBHVDisProComProgReportItemResult rsi = arrLevelCode.get(i);
					rs.joinNumber += rsi.joinNumber;
					rs.resultNumber += rsi.resultNumber;
					itemResultTotal.joinNumber += rsi.joinNumber;
					itemResultTotal.resultNumber += rsi.resultNumber;
				}
			}
			//
			dto.dtoResultTotal.joinNumber += itemResultTotal.joinNumber;
			dto.dtoResultTotal.resultNumber += itemResultTotal.resultNumber;

		} catch (Exception e) {
			dateFromTo = "";
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
	}
	
	/**
	 * 
	*  init data with cursor for SQL of NPP
	*  @param c
	*  @param dto
	*  @return: void
	*  @throws:
	*  @author: HaiTC3
	*  @date: Oct 31, 2012
	 */
	public void initWithCursorForNPP(Cursor c, TBHVDisProComProgReportNPPDTO dto) {
		
		idNPP = c.getString(c.getColumnIndex(STAFF_TABLE.STAFF_ID));
		codeNPP = c.getString(c.getColumnIndex(STAFF_TABLE.STAFF_CODE));
		nameNPP = c.getString(c.getColumnIndex(STAFF_TABLE.STAFF_NAME));
		
		idGSNPP = c.getString(c.getColumnIndex("STAFF_OWNER_ID"));
		codeGSNPP = c.getString(c.getColumnIndex("STAFF_OWNER_CODE"));
		nameGSNPP = c.getString(c.getColumnIndex("STAFF_OWNER_NAME"));
		
		
		
		programShortCode = c.getString(c.getColumnIndex("DP_CODE"));
		programShortName = c.getString(c.getColumnIndex("DP_NAME"));
		programLevel = c.getString(c.getColumnIndex("DP_LEVEL"));
		
		int joinNumber = c.getInt(c.getColumnIndex("NUM_JOIN"));
		int resultNumber = c.getInt(c.getColumnIndex("NUM_NON_COMPLETE"));
		try {
			
			for (int i = 0; i < dto.arrLevelCode.size(); i++) {
				arrLevelCode.add(newDisProComProgReportItemResult());
				if (programLevel.equals(dto.arrLevelCode.get(i))) {
					arrLevelCode.get(i).joinNumber = joinNumber;
					arrLevelCode.get(i).resultNumber = resultNumber;
					TBHVDisProComProgReportItemResult rs = dto.arrResultTotal
							.get(i);
					TBHVDisProComProgReportItemResult rsi = arrLevelCode.get(i);
					rs.joinNumber += rsi.joinNumber;
					rs.resultNumber += rsi.resultNumber;
					itemResultTotal.joinNumber += rsi.joinNumber;
					itemResultTotal.resultNumber += rsi.resultNumber;
				}
			}
			//
			dto.dtoResultTotal.joinNumber += itemResultTotal.joinNumber;
			dto.dtoResultTotal.resultNumber += itemResultTotal.resultNumber;
			
		} catch (Exception e) {
			dateFromTo = "";
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
	}
}
