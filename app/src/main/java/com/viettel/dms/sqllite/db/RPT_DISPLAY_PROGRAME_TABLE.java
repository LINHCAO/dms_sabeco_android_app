/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.DisplayProgrameLvDTO;
import com.viettel.dms.dto.view.DisProComProgReportDTO;
import com.viettel.dms.dto.view.DisProComProgReportDTO.DisProComProgReportItem;
import com.viettel.dms.dto.view.DisProComProgReportDTO.DisProComProgReportItemResult;
import com.viettel.dms.dto.view.DisplayPresentProductInfo;
import com.viettel.dms.dto.view.DisplayProgramLevelForProgramDTO;
import com.viettel.dms.dto.view.GSNPPInfoDTO;
import com.viettel.dms.dto.view.ProgReportProDispDetailSaleDTO;
import com.viettel.dms.dto.view.ProgReportProDispDetailSaleRowDTO;
import com.viettel.dms.dto.view.TBHVDisProComProgReportDTO;
import com.viettel.dms.dto.view.TBHVDisProComProgReportItem;
import com.viettel.dms.dto.view.TBHVDisProComProgReportItemResult;
import com.viettel.dms.dto.view.TBHVDisProComProgReportNPPDTO;
import com.viettel.dms.dto.view.TBHVProgReportProDispDetailSaleDTO;
import com.viettel.dms.dto.view.TBHVProgReportProDispDetailSaleRowDTO;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * RPT DISPLAY PROGRAME TABLE
 * RPT_DISPLAY_PROGRAME_TABLE.java
 * @version: 1.0 
 * @since:  08:26:54 20 Jan 2014
 */
public class RPT_DISPLAY_PROGRAME_TABLE extends ABSTRACT_TABLE {

	// id cua bang
	public static final String DISPLAY_PROGRAME_DETAIL_ID = "DISPLAY_PROGRAME_DETAIL_ID";

	public static final String ID = "ID";
	public static final String SHOP_ID = "SHOP_ID";
	public static final String STAFF_ID = "STAFF_ID";
	public static final String PARENT_STAFF_ID = "PARENT_STAFF_ID";
	public static final String STAFF_CODE = "STAFF_CODE";
	public static final String STAFF_NAME = "STAFF_NAME";
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	public static final String CUSTOMER_CODE = "CUSTOMER_CODE";
	public static final String CUSTOMER_NAME = "CUSTOMER_NAME";
	public static final String CUSTOMER_ADDRESS = "CUSTOMER_ADDRESS";
	public static final String DISPLAY_PROGRAME_ID = "DISPLAY_PROGRAME_ID";
	public static final String DISPLAY_PROGRAME_CODE = "DISPLAY_PROGRAME_CODE";
	public static final String DP_SHORT_CODE = "DP_SHORT_CODE";
	public static final String DISPLAY_PROGRAME_NAME = "DISPLAY_PROGRAME_NAME";
	public static final String DP_SHORT_NAME = "DP_SHORT_NAME";
	public static final String DISPLAY_PROGRAME_LEVEL = "DISPLAY_PROGRAME_LEVEL";
	public static final String AMOUNT_PLAN = "AMOUNT_PLAN";
	public static final String AMOUNT = "AMOUNT";
	public static final String AMOUNT_FINO = "AMOUNT_FINO";
	public static final String AMOUNT_OTHER = "AMOUNT_OTHER";
	public static final String FINO_PERCENTAGE = "FINO_PERCENTAGE";
	public static final String RESULT = "RESULT";
	public static final String MONTH = "MONTH";
	public static final String UPDATE_DATE = "UPDATE_DATE";
	public static final String SYN_STATE = "SYN_STATE";
	public static final String FROM_DATE = "FROM_DATE";
	public static final String TO_DATE = "TO_DATE";

	public RPT_DISPLAY_PROGRAME_TABLE(SQLiteDatabase _mDB) {
		this.mDB = _mDB;
	}

	@Override
	protected long insert(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected long delete(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Hoan get DTO for DisProComProgReport
	 * 
	 * @param staffId
	 * @return
	 */
	public DisProComProgReportDTO getDisProComProReportDTO(long staffId,
			String shopId) {
		Cursor c = null;
		DisProComProgReportDTO dto = new DisProComProgReportDTO();
		try {
			// /lay danh sach level cua cttb
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append("SELECT SHORT_CODE, ");
			sqlQuery.append("       SHORT_NAME, ");
			sqlQuery.append("       DP_ID, ");
			sqlQuery.append("       STAFF_ID, ");
			sqlQuery.append("       FROM_DATE, ");
			sqlQuery.append("       TO_DATE, ");
			sqlQuery.append("       Group_concat(level)            LEVEL, ");
			sqlQuery.append("       Group_concat (num_join)        NUM_JOIN, ");
			sqlQuery.append("       Group_concat(num_non_complete) NUM_NON_COMPLETE ");
			sqlQuery.append("FROM   (SELECT RDP.display_programe_id   AS DP_ID, ");
			sqlQuery.append("               RDP.display_programe_code         AS SHORT_CODE, ");
			sqlQuery.append("               RDP.display_programe_name         AS SHORT_NAME, ");
			sqlQuery.append("               s.staff_id                        AS STAFF_ID, ");
			sqlQuery.append("               RDP.from_date FROM_DATE, ");
			sqlQuery.append("               RDP.to_date TO_DATE, ");
			sqlQuery.append("               RDP.display_programe_level LEVEL, ");
			sqlQuery.append("               Count(RDP.customer_id)       NUM_JOIN, ");
			sqlQuery.append("               Sum(CASE RDP.result ");
			sqlQuery.append("                     WHEN 1 THEN 0 ");
			sqlQuery.append("                     ELSE 1 ");
			sqlQuery.append("                   end)                  NUM_NON_COMPLETE ");
			sqlQuery.append("        FROM   rpt_display_program RDP, display_program_level DPL ");
			sqlQuery.append("               LEFT JOIN staff s");
			sqlQuery.append("                      ON RDP.staff_id = s.staff_id ");
			sqlQuery.append("        WHERE  Date(from_date) <= Date('now', 'localtime') ");
			sqlQuery.append("               AND ifnull(Date('now', 'localtime') <= Date(to_date), 1) ");
			sqlQuery.append("               AND Date(RDP.month, 'start of month') = ");
			sqlQuery.append("                   Date('now', 'localtime','start of month') ");
			sqlQuery.append("               AND DPL.level_code = RDP.display_programe_level AND DPL.status = 1 ");
			sqlQuery.append("               AND DPL.display_program_id = RDP.display_programe_id ");
			sqlQuery.append("               AND RDP.parent_staff_id = ? ");
			sqlQuery.append("               AND RDP.shop_id = ? ");
			sqlQuery.append("        GROUP  BY RDP.display_programe_id, ");
			sqlQuery.append("                  RDP.display_programe_code, ");
			sqlQuery.append("                  RDP.display_programe_name, ");
			sqlQuery.append("                  RDP.display_programe_level ");
			sqlQuery.append("        ORDER  BY level) ");
			sqlQuery.append("GROUP  BY short_code ");

			String[] params = new String[] { "" + staffId, shopId };
			c = this.rawQuery(sqlQuery.toString(), params);
			dto.arrayLevel = getListDisplayProgramLevel(this
					.getListDisplayProgramHaveRPTDisplayProgramTable(shopId,
							String.valueOf(staffId)));
			int maxLevel = 0;
			for (int i = 0, size = dto.arrayLevel.size(); i < size; i++) {
				if (maxLevel < dto.arrayLevel.get(i).maxDisProLevel) {
					maxLevel = dto.arrayLevel.get(i).maxDisProLevel;
				}
			}
			// set max level display
			dto.maxLevelDisPlay = maxLevel;

			for (int i = 0; i < maxLevel; i++) {
				DisProComProgReportItemResult rs = dto
						.newDisProComProgReportItemResult();
				dto.arrResultTotal.add(rs);
			}
			if (c != null && c.moveToFirst()) {
				do {
					DisProComProgReportItem item = dto
							.newDisProComProgReportItem();
					item.initWithCursor(c, dto);
					dto.arrList.add(item);
				} while (c.moveToNext());
			}
		} catch (Exception e) {
			dto = null;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return dto;
		//
	}

	/**
	 * 
	 * lay danh sach id cac chuong trinh trung bay duoc tong hop trong bang rpt
	 * display program
	 * 
	 * @author: HaiTC3
	 * @param shopId
	 * @param staffId
	 * @return
	 * @return: String
	 * @throws:
	 * @since: Feb 22, 2013
	 */
	public String getListDisplayProgramHaveRPTDisplayProgramTable(
			String shopId, String staffId) {
		String listDis = "";
		Cursor c = null;
		try {
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append("SELECT DISTINCT RDP.display_programe_id DISPLAY_PROGRAME_ID ");
			sqlQuery.append("FROM   rpt_display_program RDP, ");
			sqlQuery.append("       display_program_level DPL ");
			sqlQuery.append("       LEFT JOIN staff s ");
			sqlQuery.append("              ON RDP.staff_id = s.staff_id ");
			sqlQuery.append("WHERE  Date(from_date) <= Date('now', 'localtime') ");
			sqlQuery.append("       AND ifnull(Date('now', 'localtime') <= Date(to_date), 1)  ");
			sqlQuery.append("       AND Date(RDP.month, 'start of month') = ");
			sqlQuery.append("           Date('now', 'localtime','start of month') ");
			sqlQuery.append("       AND s.status > 0 ");
			sqlQuery.append("       AND DPL.level_code = RDP.display_programe_level ");
			sqlQuery.append("       AND DPL.status = 1 ");
			sqlQuery.append("       AND DPL.display_program_id = RDP.display_programe_id ");
			sqlQuery.append("       AND RDP.parent_staff_id = ? ");
			sqlQuery.append("       AND RDP.shop_id = ? ");
			sqlQuery.append("GROUP  BY RDP.display_programe_id ");

			String[] params = new String[] { staffId, shopId };
			c = this.rawQuery(sqlQuery.toString(), params);

			if (c != null) {
				if (c.moveToFirst()) {
					do {
						if (c.getColumnIndex("DISPLAY_PROGRAME_ID") >= 0) {
							listDis += c.getString(c
									.getColumnIndex("DISPLAY_PROGRAME_ID"));
							listDis += ",";
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			listDis = "";
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		if (!StringUtil.isNullOrEmpty(listDis)) {
			listDis = listDis.substring(0, listDis.length() - 1);
		}
		return listDis;

	}

	/**
	 * Hoan2 get DTO for TBHVDisProComProgReport 01-04. Báo cáo tiến độ chung
	 * CTTB ngày (TBHV)
	 * @author: HoanPD1
	 * @return
	 */
	public TBHVDisProComProgReportDTO getTBHVDisProComProReportDTO(Bundle data) {

		Cursor c = null;
		String dateNow=DateUtils.now();
		// Cursor ctotal = null;
		String displayProgrameCode = data
				.getString(IntentConstants.INTENT_DISPLAY_PROGRAME_CODE);
		String parentShopId = data
				.getString(IntentConstants.INTENT_PARENT_SHOP_ID);
		// String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);

		// String page = data.getString(IntentConstants.INTENT_PAGE);
		// boolean isGetCountTotal = false; //
		// data.getBoolean(IntentConstants.INTENT_IS_GET_NUM_TOTAL_ITEM);

		TBHVDisProComProgReportDTO dto = new TBHVDisProComProgReportDTO();
		if (StringUtil.isNullOrEmpty(displayProgrameCode)) {
			// lay danh sach chuong trinh
			dto.listDisplayProgrameInfo = this
					.getListDisplayProgrameForTBHV(parentShopId);
			if (dto.listDisplayProgrameInfo.size() > 0) {
				displayProgrameCode = dto.listDisplayProgrameInfo.get(0).displayProgramCode;
			}
		}
		if (!StringUtil.isNullOrEmpty(displayProgrameCode)) {
			try {
				// /lay danh sach level cua cttb
				dto.arrLevelCode = getTBHVArrayLevelCodeVNM(displayProgrameCode);

				StringBuffer sqlQuery = new StringBuffer();
				sqlQuery.append("SELECT DISTINCT S.shop_id                    NPP_ID, ");
				sqlQuery.append("                S.shop_code                  NPP_CODE, ");
				sqlQuery.append("                S.shop_name                  NPP_NAME, ");
				sqlQuery.append("                ST.staff_id                  GSNPP_ID, ");
				sqlQuery.append("                ST.staff_code                GSNPP_CODE, ");
				sqlQuery.append("                ST.staff_name                GSNPP_NAME, ");
				sqlQuery.append("                RPT_DP.display_programe_code DP_CODE, ");
				sqlQuery.append("                RPT_DP.display_programe_name DP_NAME, ");
				sqlQuery.append("                RPT_DP.display_programe_level DP_LEVEL, ");
				sqlQuery.append("                Count(RPT_DP.AMOUNT = 0)       NUM_JOIN, ");
				sqlQuery.append("                Sum(CASE RPT_DP.amount ");
				sqlQuery.append("                      WHEN 0 THEN 1 ");
				sqlQuery.append("                      ELSE 0 ");
				sqlQuery.append("                    end)                     NUM_NON_COMPLETE ");
				sqlQuery.append("FROM   (SELECT * ");
				sqlQuery.append("        FROM   staff, ");
				sqlQuery.append("               channel_type ");
				sqlQuery.append("        WHERE  staff.staff_type_id = channel_type.channel_type_id ");
				sqlQuery.append("               AND channel_type.object_type = 5 ");
				sqlQuery.append("               AND staff.status = 1) ST, ");
				sqlQuery.append("       shop S, ");
				sqlQuery.append("       rpt_display_program RPT_DP ");
				sqlQuery.append("WHERE  1 = 1 ");
				sqlQuery.append("       AND RPT_DP.shop_id = S.shop_id ");
				sqlQuery.append("       AND S.parent_shop_id = ? ");
				// sqlQuery.append("       AND S.shop_id = ? ");
				sqlQuery.append("       AND ST.staff_id = RPT_DP.parent_staff_id ");
				sqlQuery.append("       AND RPT_DP.display_programe_code = ?  ");
				sqlQuery.append("       AND Date(RPT_DP.from_date) <= Date(?) ");
				sqlQuery.append("       AND ( Date(RPT_DP.to_date) >= Date(?) ");
				sqlQuery.append("              OR Date(RPT_DP.to_date) IS NULL ) ");
				sqlQuery.append("       AND Date(RPT_DP.month, 'start of month') = ");
				sqlQuery.append("           Date(?, 'start of month' ");
				sqlQuery.append("           ) ");
				sqlQuery.append("GROUP  BY S.shop_id, ");
				sqlQuery.append("          S.shop_code, ");
				sqlQuery.append("          S.shop_name, ");
				sqlQuery.append("          ST.staff_id, ");
				sqlQuery.append("          ST.staff_code, ");
				sqlQuery.append("          ST.staff_name, ");
				sqlQuery.append("          RPT_DP.display_programe_code, ");
				sqlQuery.append("          RPT_DP.display_programe_name, ");
				sqlQuery.append("          rpt_dp.display_programe_level ");
				sqlQuery.append("ORDER  BY npp_code, ");
				sqlQuery.append("          gsnpp_name, ");
				sqlQuery.append("          dp_code, ");
				sqlQuery.append("          dp_name ");

				// String sqlQueryGetTotal =
				// " select count(*) as total_row from ("
				// + sqlQuery.toString() + ") ";

				String[] param = new String[] { parentShopId,
						displayProgrameCode, dateNow, dateNow, dateNow };

				// get total
				// int total = 0;
				// if (isGetCountTotal) {
				// ctotal = rawQuery(sqlQueryGetTotal, param);
				// if (ctotal != null) {
				// ctotal.moveToFirst();
				// total = ctotal.getInt(0);
				// dto.totalItem = total;
				// }
				// }

				c = this.rawQuery(sqlQuery.toString(), param);
				// get liver ABCD
				for (int i = 0; i < dto.arrLevelCode.size(); i++) {
					TBHVDisProComProgReportItemResult rs = dto
							.newDisProComProgReportItemResult();
					dto.arrResultTotal.add(rs);
				}
				if (c != null && c.moveToFirst()) {
					do {
						TBHVDisProComProgReportItem item = dto
								.newDisProComProgReportItem();
						item.initWithCursor(c, dto);
						dto.arrList.add(item);
					} while (c.moveToNext());
				}
			} catch (Exception e) {
				dto = null;
			} finally {
				try {
					// if (ctotal != null) {
					// ctotal.close();
					// }
					if (c != null) {
						c.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			// <HaiTC> update for feedback CR0073
			// number day sold in month
			int numDaySoldPlan;
			try {
				numDaySoldPlan = new EXCEPTION_DAY_TABLE(mDB)
						.getWorkingDaysOfMonth();
				float percentDefaultInDay = new AP_PARAM_TABLE(mDB)
				.getPercentDefaultInDay();
        		// tinh gioi han tien do chuan cho phep cua khach hang psds
        		if (numDaySoldPlan <= 0) {
        			dto.progressStandarPercent = 0;
        		} else {
        			// dto.progressStandarPercent = (int) (100 - (numDaySoldPlan *
        			// percentDefaultInDay));
        			dto.progressStandarPercent = (int) percentDefaultInDay;
        		}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			}
			
			// </HaiTC>
		}

		return dto;
	}

	/**
	 * 
	 * get list display programe for tbhv
	 * @return
	 * @return: ArrayList<DisplayPresentProductInfo>
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 30, 2012
	 */
	public ArrayList<DisplayPresentProductInfo> getListDisplayProgrameForTBHV(
			String parentShopId) {
		ArrayList<DisplayPresentProductInfo> result = new ArrayList<DisplayPresentProductInfo>();
		SHOP_TABLE shopTB = new SHOP_TABLE(this.mDB);
		ArrayList<String> listParentShopId = shopTB
				.getShopRecursive(parentShopId);
		ArrayList<String> listShopId = shopTB
				.getShopRecursiveReverse(parentShopId);
		String strListParentShop = TextUtils.join(",", listParentShopId);
		String strListShop = TextUtils.join(",", listShopId);
		String dateNow= DateUtils.now();
		Cursor c = null;
		try {
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append("SELECT DISTINCT RPT_DP.display_programe_code AS DISPLAY_PROGRAM_CODE, ");
			sqlQuery.append("                RPT_DP.display_programe_name AS DISPLAY_PROGRAM_NAME  ");
			sqlQuery.append("FROM   rpt_display_program RPT_DP ");
			sqlQuery.append("WHERE  1 = 1 ");
			sqlQuery.append("       AND RPT_DP.shop_id IN ( ");
			sqlQuery.append(strListParentShop + ", " + strListShop + ") ");
			sqlQuery.append("       AND Date(RPT_DP.from_date) <= Date(?) ");
			sqlQuery.append("       AND Ifnull(Date(RPT_DP.to_date) >= Date(?), 1) ");
			sqlQuery.append("       AND Date(RPT_DP.month, 'start of month') = ");
			sqlQuery.append("           Date(?, 'start of month' ");
			sqlQuery.append("           ) ");
			sqlQuery.append("ORDER  BY RPT_DP.display_programe_code ");

			String params[] = new String[] {dateNow, dateNow, dateNow};
			c = this.rawQuery(sqlQuery.toString(), params);
			if (c.moveToFirst()) {
				do {
					DisplayPresentProductInfo item = new DisplayPresentProductInfo();
					item.initDisplayPresentProductInfo(c);
					result.add(item);
				} while (c.moveToNext());
			}
		} catch (Exception e) {
			result = null;
			// TODO: handle exception
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return result;
	}

	/**
	 * 
	 * get list display programe for NPP
	 * 
	 * @param shopId
	 * @return
	 * @return: ArrayList<DisplayPresentProductInfo>
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 31, 2012
	 */
	public ArrayList<DisplayPresentProductInfo> getListDisplayProgrameForNPP(
			String shopId) {
		ArrayList<DisplayPresentProductInfo> result = new ArrayList<DisplayPresentProductInfo>();
		Cursor c = null;
		try {
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append("SELECT DISTINCT display_programe_code AS DISPLAY_PROGRAM_CODE, ");
			sqlQuery.append("                display_programe_name AS DISPLAY_PROGRAM_NAME ");
			sqlQuery.append("FROM   rpt_display_program ");
			sqlQuery.append("WHERE  1 = 1 ");
			sqlQuery.append("       AND shop_id = ? --id NPP chon ");
			sqlQuery.append("       AND ( from_date > Trunc(sysdate) ");
			sqlQuery.append("              OR Nvl(to_date, sysdate) >= Trunc(sysdate) ) ");
			sqlQuery.append("ORDER  BY display_programe_code");

			String params[] = new String[] { shopId };
			c = this.rawQuery(sqlQuery.toString(), params);
			if (c.moveToFirst()) {
				do {
					DisplayPresentProductInfo item = new DisplayPresentProductInfo();
					item.initDisplayPresentProductInfo(c);
					result.add(item);
				} while (c.moveToNext());
			}
		} catch (Exception e) {
			result = null;
			// TODO: handle exception
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return result;
	}

	/**
	 * 
	 * get list GSNPP of NPP
	 * 
	 * @param shopId
	 * @return
	 * @return: ArrayList<GSNPPInfoDTO>
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 31, 2012
	 */
	public ArrayList<GSNPPInfoDTO> getListGSNPPOfNPP(String shopId) {
		ArrayList<GSNPPInfoDTO> result = new ArrayList<GSNPPInfoDTO>();
		Cursor c = null;
		try {
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append("SELECT DISTINCT ST.staff_id   STAFF_ID, ");
			sqlQuery.append("                ST.staff_code STAFF_CODE, ");
			sqlQuery.append("                ST.staff_name STAFF_NAME ");
			sqlQuery.append("FROM   rpt_display_program RPT_DP, ");
			sqlQuery.append("       staff ST, ");
			sqlQuery.append("       staff ST2 ");
			sqlQuery.append("WHERE  1 = 1 ");
			sqlQuery.append("       AND RPT_DP.shop_id = ? ");
			sqlQuery.append("       AND ST.staff_id = RPT_DP.parent_staff_id ");
			sqlQuery.append("       AND ST.status = 1 ");
			sqlQuery.append("       AND ST2.staff_id = RPT_DP.staff_id ");
			sqlQuery.append("       AND ST2.status = 1 ");
			sqlQuery.append("       AND Date(RPT_DP.from_date) <= Date('now', 'localtime') ");
			sqlQuery.append("       AND Ifnull(Date(RPT_DP.to_date) >= Date('now', 'localtime'), 1) ");
			sqlQuery.append("       AND Date(RPT_DP.month, 'start of month') = ");
			sqlQuery.append("           Date('now', 'localtime','start of month' ");
			sqlQuery.append("           ) ");
			sqlQuery.append("GROUP  BY ST.staff_id, ");
			sqlQuery.append("          ST.staff_code, ");
			sqlQuery.append("          ST.staff_name ");
			sqlQuery.append("ORDER  BY ST.staff_code ");

			String params[] = new String[] { shopId };
			c = this.rawQuery(sqlQuery.toString(), params);
			if (c.moveToFirst()) {
				do {
					GSNPPInfoDTO item = new GSNPPInfoDTO();
					item.initObjectWithCursor(c);
					result.add(item);
				} while (c.moveToNext());
			}
		} catch (Exception e) {
			result = null;
			// TODO: handle exception
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return result;
	}

	/**
	 * 
	 * lay d/s chuong trinh trung bay, trong moi chuong trinh trung bay co 1 d/s
	 * cac level tuong ung
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: ArrayList<DisplayProgramLevelForProgramDTO>
	 * @throws:
	 * @since: Feb 19, 2013
	 */
	public ArrayList<DisplayProgramLevelForProgramDTO> getListDisplayProgramLevel(
			String listDisProIdShow) {
		ArrayList<DisplayProgramLevelForProgramDTO> listResult = new ArrayList<DisplayProgramLevelForProgramDTO>();
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT DPL.display_program_id, ");
		sqlQuery.append("       DPL.level_code, ");
		sqlQuery.append("       DPL.amount ");
		sqlQuery.append("FROM   display_program_level DPL ");
		sqlQuery.append("WHERE  status = 1 ");
		sqlQuery.append(" AND DPL.display_program_id IN ( ");
		sqlQuery.append(listDisProIdShow + ") ");
		sqlQuery.append("GROUP  BY DPL.display_program_id, ");
		sqlQuery.append("          DPL.level_code, ");
		sqlQuery.append("          DPL.amount ");
		sqlQuery.append("ORDER  BY DPL.display_program_id, ");
		sqlQuery.append("          DPL.amount DESC ");

		String[] params = new String[] {};
		Cursor c = this.rawQuery(sqlQuery.toString(), params);
		ArrayList<DisplayProgrameLvDTO> listLevelNotFormat = new ArrayList<DisplayProgrameLvDTO>();
		try {
			if (c.moveToFirst()) {
				do {
					DisplayProgrameLvDTO item = new DisplayProgrameLvDTO();
					item.parseWithCursor(c);
					listLevelNotFormat.add(item);
				} while (c.moveToNext());
			}

			// tao danh sach gom cac chuong trinh trung bay va moi chuong trinh
			// co 1
			// d/s cac level khac nhau
			DisplayProgramLevelForProgramDTO newItemDPLevel = new DisplayProgramLevelForProgramDTO();
			for (int i = 0, size = listLevelNotFormat.size(); i < size; i++) {
				if (i != 0
						&& newItemDPLevel.displayProgramId != listLevelNotFormat.get(i).dislayProgramId) {
					newItemDPLevel.maxDisProLevel = newItemDPLevel.listDisProLevel.size();
					listResult.add(newItemDPLevel);
					newItemDPLevel = new DisplayProgramLevelForProgramDTO();
				}
				newItemDPLevel.displayProgramId = listLevelNotFormat.get(i).dislayProgramId;
				newItemDPLevel.listDisProLevel.add(listLevelNotFormat.get(i));
			}
			// bo xung item cuoi cung vao d/s
			newItemDPLevel.maxDisProLevel = newItemDPLevel.listDisProLevel.size();
			listResult.add(newItemDPLevel);
		} catch (Exception e) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return listResult;
	}

	/**
	 * Lấy Level cho man hinh :01-04. Báo cáo tiến độ chung CTTB ngày (TBHV)
	 * @author: HoanPD1
	 * @return
	 */
	public ArrayList<String> getTBHVArrayLevelCodeVNM(String displayProgrameCode) {
		ArrayList<String> ret = new ArrayList<String>();
		Cursor c = null;
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT DISTINCT rdp.display_programe_level ");
		sqlQuery.append("FROM   rpt_display_program rdp, ");
		sqlQuery.append("       display_program_level dpl ");
		sqlQuery.append("WHERE  1 = 1 ");
		sqlQuery.append("       AND rdp.display_programe_code = ? ");
		sqlQuery.append("       AND rdp.display_programe_level = dpl.level_code ");
		sqlQuery.append("GROUP  BY rdp.display_programe_level ");
		sqlQuery.append("ORDER  BY rdp.display_programe_level ");

		String params[] = new String[] { displayProgrameCode };
		c = this.rawQuery(sqlQuery.toString(), params);
		try {
			if (c.moveToFirst()) {
				do {
					ret.add(c.getString(c.getColumnIndex("DISPLAY_PROGRAME_LEVEL")));
				} while (c.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return ret;
	}

	/**
	 * get DTO for report display programe of staff
	 * 
	 * @param staffId
	 * @param proId
	 * @return
	 */
	public DisProComProgReportDTO getStaffDisProComProReportDTO(long staffId,
																long shopId, int proId) {
		Cursor c = null;
		DisProComProgReportDTO dto = new DisProComProgReportDTO();
		try {
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append("SELECT STAFF_ID, ");
			sqlQuery.append("       STAFF_CODE, ");
			sqlQuery.append("       STAFF_NAME, ");
			sqlQuery.append("       display_programe_id DP_ID, ");
			sqlQuery.append("       Group_concat(level)            LEVEL, ");
			sqlQuery.append("       Group_concat (num_join)        NUM_JOIN, ");
			sqlQuery.append("       Group_concat(num_non_complete) NUM_NON_COMPLETE ");
			sqlQuery.append("FROM   (SELECT RDP.staff_id STAFF_ID, ");
			sqlQuery.append("               RDP.staff_code STAFF_CODE, ");
			sqlQuery.append("               RDP.staff_name STAFF_NAME, ");
			sqlQuery.append("               rdp.display_programe_id display_programe_id, ");
			sqlQuery.append("               RDP.display_programe_level LEVEL, ");
			sqlQuery.append("               Count(RDP.customer_id)       NUM_JOIN, ");
			sqlQuery.append("               Sum(CASE RDP.result ");
			sqlQuery.append("                     WHEN 1 THEN 0 ");
			sqlQuery.append("                     ELSE 1 ");
			sqlQuery.append("                   end)                  NUM_NON_COMPLETE ");
			sqlQuery.append("        FROM   rpt_display_program RDP ");
			sqlQuery.append("               LEFT JOIN staff s");
			sqlQuery.append("                      ON RDP.staff_id = s.staff_id ");
			sqlQuery.append("        WHERE  Date(from_date) <= Date('now', 'localtime') ");
			sqlQuery.append("               AND ifnull(Date('now', 'localtime') <= Date(to_date),1) ");
			sqlQuery.append("               AND Date(RDP.month, 'start of month') = ");
			sqlQuery.append("                   Date('now', 'localtime','start of month') ");
			sqlQuery.append("               AND rdp.parent_staff_id = ? ");
			sqlQuery.append("               AND rdp.shop_id = ? ");
			sqlQuery.append("               AND rdp.display_programe_id = ? ");
			sqlQuery.append("        GROUP  BY RDP.staff_id, ");
			sqlQuery.append("                  RDP.staff_code, ");
			sqlQuery.append("                  RDP.staff_name, ");
			sqlQuery.append("                  RDP.display_programe_level ");
			sqlQuery.append("        ORDER  BY RDP.staff_code, RDP.staff_name) ");
			sqlQuery.append("GROUP  BY staff_code ");

			String[] params = new String[] { String.valueOf(staffId),
					String.valueOf(shopId), String.valueOf(proId) };
			c = this.rawQuery(sqlQuery.toString(), params);
			dto.arrayLevel = getListDisplayProgramLevel(this
					.getListDisplayProgramHaveRPTDisplayProgramTable(
							String.valueOf(shopId), String.valueOf(staffId)));
			int maxLevel = 0;
			for (int i = 0, size = dto.arrayLevel.size(); i < size; i++) {
				if (maxLevel < dto.arrayLevel.get(i).maxDisProLevel) {
					maxLevel = dto.arrayLevel.get(i).maxDisProLevel;
				}
			}
			// set max level display
			dto.maxLevelDisPlay = maxLevel;

			for (int i = 0; i < maxLevel; i++) {
				DisProComProgReportItemResult rs = dto
						.newDisProComProgReportItemResult();
				dto.arrResultTotal.add(rs);
			}
			if (c != null && c.moveToFirst()) {
				do {
					DisProComProgReportItem item = dto
							.newDisProComProgReportItem();
					item.initWithCursor(c, dto);
					dto.arrList.add(item);
				} while (c.moveToNext());
			}
			// return dto;
		} catch (Exception e) {
			dto = null;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return dto;

	}

	/**
	 * lay du lieu man hinh : 01-07.Tiến độ CTTB theo NPP ngay (TBHV)
	 * @author: HoanPD1
	 * @return
	 */

	public TBHVDisProComProgReportNPPDTO getTBHVDisProComProReportNPP(
			Bundle data) {
		Cursor c = null;
		TBHVDisProComProgReportNPPDTO dto = new TBHVDisProComProgReportNPPDTO();
		try {
			String strGSNPPId = Constants.STR_BLANK;
			String strCTTBCode = Constants.STR_BLANK;
			String strShopId = Constants.STR_BLANK;
			boolean isLoadListGSNPPAndListDisPro = false;
			if (data.getString(IntentConstants.INTENT_STAFF_ID) != null) {
				strGSNPPId = data.getString(IntentConstants.INTENT_STAFF_ID);
			}
			if (data.getString(IntentConstants.INTENT_TBHV_CTTB_CODE) != null) {
				strCTTBCode = data
						.getString(IntentConstants.INTENT_TBHV_CTTB_CODE);
			}
			if (data.getString(IntentConstants.INTENT_SHOP_ID) != null) {
				strShopId = data.getString(IntentConstants.INTENT_SHOP_ID);
			}
			isLoadListGSNPPAndListDisPro = data
					.getBoolean(IntentConstants.INTENT_IS_OR);

			// /lay danh sach level cua cttb
			dto.arrLevelCode = getTBHVArrayLevelCodeVNM(strCTTBCode);

			if (isLoadListGSNPPAndListDisPro) {
				// GET LIST GSNPP
				dto.listGSNPPInfo = this.getListGSNPPOfNPP(strShopId);

				// get list CTTB
				dto.listDisplayProgrameInfo = this
						.getListDisplayProgrameForNPP(strShopId);
			}

			ArrayList<String> listParams = new ArrayList<String>();
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append("SELECT DISTINCT RPT_DP.staff_id              STAFF_ID, ");
			sqlQuery.append("                RPT_DP.staff_code            STAFF_CODE, ");
			sqlQuery.append("                RPT_DP.staff_name            STAFF_NAME, ");
			sqlQuery.append("                ST.staff_id                  STAFF_OWNER_ID, ");
			sqlQuery.append("                ST.staff_code                STAFF_OWNER_CODE, ");
			sqlQuery.append("                ST.staff_name                STAFF_OWNER_NAME, ");
			sqlQuery.append("                RPT_DP.display_programe_code DP_CODE, ");
			sqlQuery.append("                RPT_DP.display_programe_name DP_NAME, ");
			sqlQuery.append("                RPT_DP.display_programe_level DP_LEVEL, ");
			sqlQuery.append("                Count(RPT_DP.customer_id)    NUM_JOIN, ");
			sqlQuery.append("                Sum(CASE RPT_DP.amount ");
			sqlQuery.append("                      WHEN 0 THEN 1 ");
			sqlQuery.append("                      ELSE 0 ");
			sqlQuery.append("                    end)                     NUM_NON_COMPLETE ");
			sqlQuery.append("FROM   rpt_display_program RPT_DP, ");
			sqlQuery.append("       staff ST ");
			sqlQuery.append("WHERE  1 = 1 ");
			sqlQuery.append("       AND RPT_DP.shop_id = ? ");
			sqlQuery.append("       AND ST.staff_id = RPT_DP.parent_staff_id ");
			sqlQuery.append("       AND ST.status = 1 ");

			listParams.add(strShopId);

			if (!StringUtil.isNullOrEmpty(strGSNPPId)) {
				sqlQuery.append("       AND RPT_DP.parent_staff_id = ? ");
				listParams.add(strGSNPPId);
			}
			// if (!StringUtil.isNullOrEmpty(strGSNPPId)) {
			// sqlQuery.append("        AND RPT_DP.display_programe_code = ? ");
			// listParams.add(strCTTBCode);
			// }

			sqlQuery.append("       AND RPT_DP.display_programe_code = ? ");
			listParams.add(strCTTBCode);

			sqlQuery.append("       AND Date(RPT_DP.from_date) <= Date('now', 'localtime') ");
			sqlQuery.append("       AND Ifnull(Date(RPT_DP.to_date) >= Date('now', 'localtime'), 1) ");
			sqlQuery.append("       AND Date(RPT_DP.month, 'start of month') = ");
			sqlQuery.append("           Date('now', 'localtime','start of month' ");
			sqlQuery.append("           ) ");
			sqlQuery.append("GROUP  BY RPT_DP.staff_id, ");
			sqlQuery.append("          RPT_DP.staff_code, ");
			sqlQuery.append("          RPT_DP.staff_name, ");
			sqlQuery.append("          ST.staff_id, ");
			sqlQuery.append("          ST.staff_code, ");
			sqlQuery.append("          ST.staff_name, ");
			sqlQuery.append("          RPT_DP.display_programe_code, ");
			sqlQuery.append("          RPT_DP.display_programe_name, ");
			sqlQuery.append("          RPT_DP.display_programe_level ");
			sqlQuery.append("ORDER  BY RPT_DP.staff_code, ");
			sqlQuery.append("          RPT_DP.staff_name ");

			String[] params = listParams.toArray(new String[listParams.size()]);

			c = this.rawQuery(sqlQuery.toString(), params);
			// get liver ABCD
			for (int i = 0; i < dto.arrLevelCode.size(); i++) {
				TBHVDisProComProgReportItemResult rs = new TBHVDisProComProgReportItemResult();
				dto.arrResultTotal.add(rs);
			}
			if (c != null && c.moveToFirst()) {
				do {
					TBHVDisProComProgReportItem item = new TBHVDisProComProgReportItem();
					item.initWithCursorForNPP(c, dto);
					dto.arrList.add(item);
				} while (c.moveToNext());
			}
			// return dto;
		} catch (Exception e) {
			dto = null;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return dto;

	}

	public ProgReportProDispDetailSaleDTO getProgReportProDispDetailSaleDTO(
			long staffId, String displayProgrameCode, int checkAll, String page) {
		ProgReportProDispDetailSaleDTO dto = new ProgReportProDispDetailSaleDTO();
		String sqlQuery = "select RDP.CUSTOMER_CODE, RDP.CUSTOMER_NAME, RDP.CUSTOMER_ADDRESS, RDP.AMOUNT_PLAN, RDP.AMOUNT_REMAIN, RDP.DISPLAY_PROGRAME_LEVEL, RDP.AMOUNT, RDP.RESULT"
				+ " from RPT_DISPLAY_PROGRAM as RDP"
				+ " where RDP.STAFF_ID = ?"
				+ " and RDP.DISPLAY_PROGRAME_CODE = ?"
				+ " and date(RDP.MONTH, 'start of month') = date('now', 'localtime','start of month')";
		if (checkAll == 0) {
			sqlQuery += " and (RDP.RESULT = 0 OR RDP.RESULT is null)";
		}
		sqlQuery += " group by RDP.CUSTOMER_CODE, RDP.CUSTOMER_NAME";
		Cursor c = null;
		Cursor cTmp = null;
		try {
			ArrayList<ProgReportProDispDetailSaleRowDTO> arrayList = new ArrayList<ProgReportProDispDetailSaleRowDTO>();
			long totalRemain = Long.valueOf(0);
			String[] param = new String[] { String.valueOf(staffId),
					displayProgrameCode };
			String getCountProductList = " SELECT COUNT(*) AS TOTAL_ROW FROM ("
					+ sqlQuery + ") ";

			// tinh tong so item
			if (checkAll == 1) {
				try {
					cTmp = rawQuery(getCountProductList, param);
					int total = 0;
					if (cTmp != null) {
						cTmp.moveToFirst();
						total = cTmp.getInt(0);
						dto.totalItem = total;
					}
				} catch (Exception e) {
					VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
				} finally {
					try {
						if (checkAll == 1 && cTmp != null) {
							cTmp.close();
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

				c = this.rawQuery(sqlQuery + page, param);
			} else {
				c = this.rawQuery(sqlQuery, param);
			}

			if (c.moveToFirst()) {
				do {
					ProgReportProDispDetailSaleRowDTO detailDto = this.initForDetailSaleFromCursor(c);
					totalRemain += Math.round(detailDto.RemainSale / 1000.0);
					arrayList.add(detailDto);
				} while (c.moveToNext());
				dto.listItem = arrayList;
				dto.RemainSaleTotal = totalRemain;
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			dto = null;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return dto;
	}

	/**
	 * Khoi tao du lieu cho man hinh thong ke chi tiet
	 * 
	 * @author: ThanhNN8
	 * @param c
	 * @return
	 * @return: ProgReportProDispDetailSaleRowDTO
	 * @throws:
	 */
	private ProgReportProDispDetailSaleRowDTO initForDetailSaleFromCursor(
			Cursor c) {
		// TODO Auto-generated method stub
		ProgReportProDispDetailSaleRowDTO detailDto = new ProgReportProDispDetailSaleRowDTO();
		if (c.getColumnIndex("CUSTOMER_CODE") >= 0) {
			detailDto.customerCode = c.getString(
					c.getColumnIndex("CUSTOMER_CODE")).substring(0, 3);
		} else {
			detailDto.customerCode = "";
		}
		if (c.getColumnIndex("CUSTOMER_NAME") >= 0) {
			detailDto.customerName = c.getString(c
					.getColumnIndex("CUSTOMER_NAME"));
		} else {
			detailDto.customerName = "";
		}
		if (c.getColumnIndex("CUSTOMER_ADDRESS") >= 0) {
			detailDto.CustomerAddress = c.getString(c
					.getColumnIndex("CUSTOMER_ADDRESS"));
		} else {
			detailDto.CustomerAddress = "";
		}
		if (c.getColumnIndex("DISPLAY_PROGRAME_LEVEL") >= 0) {
			detailDto.level = c.getString(c
					.getColumnIndex("DISPLAY_PROGRAME_LEVEL"));
		} else {
			detailDto.level = "";
		}
		if (c.getColumnIndex("AMOUNT_PLAN") >= 0
				&& c.getColumnIndex("AMOUNT") >= 0
				&& c.getColumnIndex("RESULT") >= 0) {
			// int result = c.getInt(c.getColumnIndex("RESULT"));
			detailDto.amountPlan = c.getLong(c.getColumnIndex("AMOUNT_PLAN"));
			// long amount = c.getLong(c.getColumnIndex("AMOUNT"));
			// if (result == 1) {
			detailDto.RemainSale = c.getLong(c.getColumnIndex("AMOUNT_REMAIN"));
			if (detailDto.RemainSale < 0) {
				detailDto.RemainSale = 0;
			}
		} else {
			detailDto.RemainSale = Long.valueOf(0);
		}
		if (c.getColumnIndex("RESULT") >= 0) {
			detailDto.result = c.getInt(c.getColumnIndex("RESULT"));
		} else {
			detailDto.result = 0;
		}
		return detailDto;
	}

	/**
	 * lay du lieu man hinh : 01-08.Tiến độ CTTB chi tiết NVBH ngay (TBHV)
	 * @author: HoanPD1
	 * @param staffId
	 * @param checkAll
	 * @return
	 */
	public TBHVProgReportProDispDetailSaleDTO getTBHVProgReportProDispDetailSaleDTO(
			long staffId, String displayProgrameCode,
			String displayProgrameLevel, int checkAll, int page) {
		TBHVProgReportProDispDetailSaleDTO dto = new TBHVProgReportProDispDetailSaleDTO();
		// String getTotalCount = "";
		StringBuffer getTotalCount = new StringBuffer();
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT RDP.customer_code, ");
		sqlQuery.append("       RDP.customer_name, ");
		sqlQuery.append("       RDP.customer_address, ");
		sqlQuery.append("       CT.housenumber  || ' ' || ");
		sqlQuery.append("       street housenumber, ");
		sqlQuery.append("       RDP.display_programe_level DISPLAY_PROGRAME_LEVEL, ");
		sqlQuery.append("       RDP.display_programe_code, ");
		sqlQuery.append("       RDP.amount_plan, ");
		sqlQuery.append("       RDP.amount_remain, ");
		sqlQuery.append("       RDP.amount, ");
		sqlQuery.append("       RDP.result ");
		sqlQuery.append("FROM   rpt_display_program RDP, customer CT ");
		sqlQuery.append("WHERE  RDP.display_programe_code = ? ");
		sqlQuery.append("       AND RDP.staff_id = ? ");
		sqlQuery.append("       AND RDP.customer_id = CT.customer_id ");
		sqlQuery.append("       AND Date(RDP.month, 'start of month') = ");
		sqlQuery.append("           Date('now', 'localtime','start of month') ");
		if (checkAll == 0) {
			sqlQuery.append("       AND RDP.amount = 0 ");
		}
		sqlQuery.append("GROUP  BY RDP.customer_code, ");
		sqlQuery.append("          RDP.customer_name ");
		sqlQuery.append("ORDER  BY RDP.customer_code, ");
		sqlQuery.append("          RDP.customer_name ");

		if (checkAll == 1) {

			getTotalCount.append("  select count(*) as TOTAL_ROW from ("
					+ sqlQuery.toString() + ")");
			// sqlQuery.append(" limit " + Constants.NUM_ITEM_PER_PAGE
			// + " offset " + page);

			sqlQuery.append(" limit "
					+ Integer.toString(Constants.NUM_ITEM_PER_PAGE));
			sqlQuery.append(" offset "
					+ Integer
							.toString((page - 1) * Constants.NUM_ITEM_PER_PAGE));

		}

		ArrayList<TBHVProgReportProDispDetailSaleRowDTO> arrayList = new ArrayList<TBHVProgReportProDispDetailSaleRowDTO>();
		Cursor c = null;
		try {
			int totalRemain = Integer.valueOf(0);
			c = this.rawQuery(sqlQuery.toString(), new String[] {
					displayProgrameCode, "" + staffId });
			if (c.moveToFirst()) {
				do {
					TBHVProgReportProDispDetailSaleRowDTO detailDto = this.initTBHVForDetailSaleFromCursor(c);
					totalRemain += Math.round(detailDto.remainSale / 1000.0);
					arrayList.add(detailDto);
				} while (c.moveToNext());
				dto.listItem = arrayList;
				dto.remainSaleTotal = totalRemain;
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			dto = null;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		Cursor cTotalRow = null;
		try {
			if (checkAll == 1) {
				cTotalRow = rawQuery(getTotalCount.toString(), new String[] {
						displayProgrameCode, "" + staffId });
				if (cTotalRow != null) {
					if (cTotalRow.moveToFirst()) {
						dto.total = cTotalRow.getInt(0);
					}
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			dto = null;
		} finally {
			try {
				if (cTotalRow != null) {
					cTotalRow.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return dto;
	}

	/**
	 * Khoi tao du lieu cho man hinh thong ke chi tiet
	 * 
	 * @author: HoanPD1
	 * @param c
	 * @return
	 * @return: ProgReportProDispDetailSaleRowDTO
	 * @throws:
	 */
	private TBHVProgReportProDispDetailSaleRowDTO initTBHVForDetailSaleFromCursor(
			Cursor c) {
		// TODO Auto-generated method stub
		TBHVProgReportProDispDetailSaleRowDTO detailDto = new TBHVProgReportProDispDetailSaleRowDTO();
		if (c.getColumnIndex("CUSTOMER_CODE") >= 0) {
			detailDto.customerCode = c.getString(
					c.getColumnIndex("CUSTOMER_CODE")).substring(0, 3);
		} else {
			detailDto.customerCode = "";
		}
		if (c.getColumnIndex("CUSTOMER_NAME") >= 0) {
			detailDto.customerName = c.getString(c
					.getColumnIndex("CUSTOMER_NAME"));
		} else {
			detailDto.customerName = "";
		}
		if (c.getColumnIndex("CUSTOMER_ADDRESS") >= 0) {
			detailDto.customerAddress = c.getString(c
					.getColumnIndex("CUSTOMER_ADDRESS"));
			if (StringUtil.isNullOrEmpty(detailDto.customerAddress)) {
				detailDto.customerAddress = c.getString(c
						.getColumnIndex("housenumber"));
			}
		} else {
			detailDto.customerAddress = "";
		}
		if (c.getColumnIndex("DISPLAY_PROGRAME_LEVEL") >= 0) {
			detailDto.displayProgLevel = c.getString(c
					.getColumnIndex("DISPLAY_PROGRAME_LEVEL"));
		} else {
			detailDto.displayProgLevel = "";
		}
		if (c.getColumnIndex("AMOUNT_PLAN") >= 0) {
			detailDto.amountPlan = c.getLong(c.getColumnIndex("AMOUNT_PLAN"));
		} else {
			detailDto.amountPlan = 0;
		}
		if (c.getColumnIndex("AMOUNT_REMAIN") >= 0) {
			detailDto.remainSale = c.getLong(c.getColumnIndex("AMOUNT_REMAIN"));
		} else {
			detailDto.remainSale = 0;
		}
		if (c.getColumnIndex("AMOUNT") >= 0) {
			detailDto.amount = c.getLong(c.getColumnIndex("AMOUNT"));
		} else {
			detailDto.amount = 0;
		}
		if (c.getColumnIndex("RESULT") >= 0) {
			detailDto.result = c.getInt(c.getColumnIndex("RESULT"));
		} else {
			detailDto.result = 0;
		}
		return detailDto;
	}

}
