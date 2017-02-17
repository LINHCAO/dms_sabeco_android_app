/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.database.Cursor;
import android.net.ParseException;

import com.viettel.dms.sqllite.db.DISPLAY_PROGRAME_TABLE;
import com.viettel.dms.sqllite.db.RPT_DISPLAY_PROGRAME_TABLE;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * DTO cua man hinh bao cao chuong trinh trung bay
 * 
 * @author hieunq1
 * 
 */
public class DisProComProgReportDTO {
	// danh sach item
	public ArrayList<DisProComProgReportItem> arrList;
	// danh sach level code
	public ArrayList<DisplayProgramLevelForProgramDTO> arrayLevel = new ArrayList<DisplayProgramLevelForProgramDTO>();
	// tong ket qua
	public ArrayList<DisProComProgReportItemResult> arrResultTotal;
	public DisProComProgReportItemResult dtoResultTotal;
	// max level display
	public int maxLevelDisPlay = 0;

	public DisProComProgReportDTO() {
		arrList = new ArrayList<DisProComProgReportDTO.DisProComProgReportItem>();
		arrayLevel = new ArrayList<DisplayProgramLevelForProgramDTO>();
		// arrLevelCode = new ArrayList<String>();
		arrResultTotal = new ArrayList<DisProComProgReportItemResult>();
		dtoResultTotal = new DisProComProgReportItemResult();
	}

	/**
	 * new DisProComProgReportItemResult
	 * 
	 * @return
	 */
	public DisProComProgReportItemResult newDisProComProgReportItemResult() {
		return new DisProComProgReportItemResult();
	}

	/**
	 * add item
	 * 
	 * @param c
	 */
	public void addItem(Cursor c) {
		arrList.add(new DisProComProgReportItem(c));
	}

	/**
	 * new DisProComProgReportItem
	 * 
	 * @return
	 */
	public DisProComProgReportItem newDisProComProgReportItem() {
		return new DisProComProgReportItem();
	}

	/**
	 * DisProComProgReportItemResult
	 * 
	 * @author hieunq1
	 * 
	 */
	public class DisProComProgReportItemResult {
		public int resultNumber;
		public int joinNumber;
		public String leveLCode;
	}

	/**
	 * DisProComProgReportItem
	 * 
	 * @author hieunq1
	 * 
	 */
	public class DisProComProgReportItem {
		public String programId;
		public String staffId;
		public String staffCode;
		public String staffName;
		public String programCodeShort;
		public String programNameShort;
		public String programCode;
		public String programName;
		// ma Level
		public String programLevel;
		public String dateFromTo;
		public String from_date;
		public String to_date;
		public ArrayList<DisProComProgReportItemResult> arrLevelCode;
		public DisProComProgReportItemResult itemResultTotal;

		public DisProComProgReportItem() {
			arrLevelCode = new ArrayList<DisProComProgReportItemResult>();
			itemResultTotal = new DisProComProgReportItemResult();
			programCodeShort = "";
			programNameShort = "";
			programCode = "";
			programName = "";
			programLevel = "";
			dateFromTo = "";
		}

		public DisProComProgReportItem(Cursor c) {

			programCodeShort = c.getString(c
					.getColumnIndex(DISPLAY_PROGRAME_TABLE.DP_SHORT_CODE));
			programNameShort = c.getString(c
					.getColumnIndex(DISPLAY_PROGRAME_TABLE.DP_SHORT_NAME));
			programCode = c
					.getString(c
							.getColumnIndex(DISPLAY_PROGRAME_TABLE.DISPLAY_PROGRAM_CODE));
			programName = c
					.getString(c
							.getColumnIndex(DISPLAY_PROGRAME_TABLE.DISPLAY_PROGRAM_NAME));
			programLevel = c
					.getString(c
							.getColumnIndex(RPT_DISPLAY_PROGRAME_TABLE.DISPLAY_PROGRAME_LEVEL));
			from_date = c.getString(c
					.getColumnIndex(RPT_DISPLAY_PROGRAME_TABLE.FROM_DATE));
			to_date = c.getString(c
					.getColumnIndex(RPT_DISPLAY_PROGRAME_TABLE.TO_DATE));
		}

		/**
		 * new DisProComProgReportItemResult
		 * 
		 * @return
		 */
		public DisProComProgReportItemResult NewDisProComProgReportItemResult() {
			return new DisProComProgReportItemResult();
		}

		/**
		 * 
		 * khoi tao du lieu cho tung CTTB, tinh toan lai so KH tham gia va so
		 * khach hang dat
		 * 
		 * @author: HaiTC3
		 * @param c
		 * @param dto
		 * @return: void
		 * @throws:
		 * @since: Feb 19, 2013
		 */
		public void initWithCursor(Cursor c, DisProComProgReportDTO dto) {
			SimpleDateFormat sfs = DateUtils.defaultSqlDateFormat;
			SimpleDateFormat sfd = DateUtils.defaultDateFormat;
			if (c.getColumnIndex("STAFF_ID") >= 0) {
				staffId = c.getString(c.getColumnIndex("STAFF_ID"));
			}
			if (c.getColumnIndex("STAFF_CODE") >= 0) {
				staffCode = c.getString(c.getColumnIndex("STAFF_CODE"));
			}
			if (c.getColumnIndex("STAFF_NAME") >= 0) {
				staffName = c.getString(c.getColumnIndex("STAFF_NAME"));
			}
			if (c.getColumnIndex("DP_ID") >= 0) {
				programId = c.getString(c.getColumnIndex("DP_ID"));
			}
			if (c.getColumnIndex("SHORT_CODE") >= 0) {
				programCodeShort = c.getString(c.getColumnIndex("SHORT_CODE"));
			} else if (c.getColumnIndex("STAFF_CODE") >= 0) {
				programCodeShort = c.getString(c.getColumnIndex("STAFF_CODE"));
			}
			if (c.getColumnIndex("SHORT_NAME") >= 0) {
				programNameShort = c.getString(c.getColumnIndex("SHORT_NAME"));
			} else if (c.getColumnIndex("STAFF_NAME") >= 0) {
				programNameShort = c.getString(c.getColumnIndex("STAFF_NAME"));
			}
			if (c.getColumnIndex("LEVEL") >= 0) {
				programLevel = c.getString(c.getColumnIndex("LEVEL"));
			}
			if (c.getColumnIndex("FROM_DATE") >= 0) {
				from_date = c.getString(c.getColumnIndex("FROM_DATE"));
			}
			if (c.getColumnIndex("TO_DATE") >= 0) {
				to_date = c.getString(c.getColumnIndex("TO_DATE"));
			}
			try {
				try {
					if (!StringUtil.isNullOrEmpty(from_date)) {
						dateFromTo = sfd.format(sfs.parse(from_date));
					}
					dateFromTo += "-";
					if (!StringUtil.isNullOrEmpty(to_date)) {
						dateFromTo += sfd.format(sfs.parse(to_date));
					}
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
				}
			} catch (ParseException e) {
				dateFromTo = "";
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			}
			//
			String joinNumber = c.getString(c.getColumnIndex("NUM_JOIN"));
			String resultNumber = c.getString(c
					.getColumnIndex("NUM_NON_COMPLETE"));

			String[] arrProgramLevel = programLevel.split(",");
			String[] arrJoinNumber = joinNumber.split(",");
			String[] arrResultNumber = resultNumber.split(",");

			try {
				// tao d/s level tmp va lay dung display program tuong ung voi
				// programId
				DisplayProgramLevelForProgramDTO currentDPLevel = new DisplayProgramLevelForProgramDTO();
				for (int i = 0, size = dto.arrayLevel.size(); i < size; i++) {
					if (String.valueOf(dto.arrayLevel.get(i).displayProgramId)
							.equals(programId)) {
						currentDPLevel = dto.arrayLevel.get(i);
					}
				}
				for (int i = 0; i < dto.maxLevelDisPlay; i++) {
					arrLevelCode.add(newDisProComProgReportItemResult());
				}

				// cap nhat du lieu cho tung level tmp
				for (int i = 0, size = currentDPLevel.listDisProLevel.size(); i < size; i++) {

					for (int j = 0, size2 = arrProgramLevel.length; j < size2; j++) {
						if (arrProgramLevel[j]
								.equals(currentDPLevel.listDisProLevel.get(i).levelCode)) {
							arrLevelCode.get(i).joinNumber = Integer
									.parseInt(arrJoinNumber[j]);
							arrLevelCode.get(i).resultNumber = Integer
									.parseInt(arrResultNumber[j]);
							DisProComProgReportItemResult rs = dto.arrResultTotal
									.get(i);
							DisProComProgReportItemResult rsi = arrLevelCode
									.get(i);
							rs.joinNumber += rsi.joinNumber;
							rs.resultNumber += rsi.resultNumber;
							// dung tinh cho cot tong
							itemResultTotal.joinNumber += rsi.joinNumber;
							itemResultTotal.resultNumber += rsi.resultNumber;
						}
					}
				}
				dto.dtoResultTotal.joinNumber += itemResultTotal.joinNumber;
				dto.dtoResultTotal.resultNumber += itemResultTotal.resultNumber;

			} catch (Exception e) {
				dateFromTo = "";
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			}
		}
	}

}
