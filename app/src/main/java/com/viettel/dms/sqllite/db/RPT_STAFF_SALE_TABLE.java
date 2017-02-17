/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import net.sqlcipher.database.SQLiteDatabase;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.ShopDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.AccSaleProgReportDTO;
import com.viettel.dms.dto.view.AccSaleProgReportDTO.AccSaleProgReportItem;
import com.viettel.dms.dto.view.CabinetStaffDTO;
import com.viettel.dms.dto.view.HistoryItemDTO;
import com.viettel.dms.dto.view.ListStaffDTO;
import com.viettel.dms.dto.view.ManagerEquipmentDTO;
import com.viettel.dms.dto.view.ProgressDateDetailReportDTO;
import com.viettel.dms.dto.view.ProgressDateReportDTO;
import com.viettel.dms.dto.view.ProgressReportSalesFocusDTO;
import com.viettel.dms.dto.view.ProgressReportSalesFocusDTO.InfoProgressEmployeeDTO;
import com.viettel.dms.dto.view.ProgressReportSalesFocusDTO.ReportProductFocusItem;
import com.viettel.dms.dto.view.ReportFocusProductItem;
import com.viettel.dms.dto.view.ReportProgressMonthCellDTO;
import com.viettel.dms.dto.view.ReportProgressMonthViewDTO;
import com.viettel.dms.dto.view.ReportSalesFocusEmployeeInfo;
import com.viettel.dms.dto.view.TBHVCustomerNotPSDSReportDTO;
import com.viettel.dms.dto.view.TBHVManagerEquipmentDTO;
import com.viettel.dms.dto.view.TBHVProgressDateReportDTO;
import com.viettel.dms.dto.view.TBHVProgressReportSalesFocusViewDTO;
import com.viettel.dms.dto.view.VisitDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * 
 * table summary all report for gsnpp & tbhv
 * 
 * @author: HaiTC3
 * @version: 1.1
 * @since: 1.0
 */
public class RPT_STAFF_SALE_TABLE extends ABSTRACT_TABLE {

	// id khach hang
	public static final String STAFF_ID = "STAFF_ID";
	public static final String STAFF_NAME = "STAFF_NAME";
	public static final String STAFF_CODE = "STAFF_CODE";
	public static final String PARENT_STAFF_ID = "PARENT_STAFF_ID";
	public static final String SHOP_ID = "SHOP_ID";
	// doanh so ke hoach thang
	public static final String MONTH_AMOUNT_PLAN = "MONTH_AMOUNT_PLAN";
	// doanh so thuc hien trong thang
	public static final String MONTH_AMOUNT = "MONTH_AMOUNT";
	// diem trong thang
	public static final String MONTH_SCORE = "MONTH_SCORE";
	// doanh so ke hoach ngay
	public static final String DAY_AMOUNT_PLAN = "DAY_AMOUNT_PLAN";
	// doanh so thuc hien trong ngay
	public static final String DAY_AMOUNT = "DAY_AMOUNT";
	// diem trong ngay
	public static final String DAY_SCORE = "DAY_SCORE";
	// sku ke hoach thang
	public static final String MONTH_SKU_PLAN = "MONTH_SKU_PLAN";
	// sku thuc hien trong than
	public static final String MONTH_SKU = "MONTH_SKU";
	// doanh so ke hoach thang mat hang trong tam 1
	public static final String FOCUS1_AMOUNT_PLAN = "FOCUS1_AMOUNT_PLAN";
	// doanh so dat dc trong thang mat hang trong tam 1
	public static final String FOCUS1_AMOUNT = "FOCUS1_AMOUNT";
	// doanh so ke hoach thang mat hang trong tam 2
	public static final String FOCUS2_AMOUNT_PLAN = "FOCUS2_AMOUNT_PLAN";
	// doanh so dat dc trong thang mat hang trong tam 2
	public static final String FOCUS2_AMOUNT = "FOCUS2_AMOUNT";
	public static final String CREATE_DATE = "CREATE_DATE";
	public static final String UPDATE_DATE = "UPDATE_DATE";
	public static final String RPT_STAFF_SALE_TABLE = "RPT_STAFF_SALE";

	public RPT_STAFF_SALE_TABLE() {
		this.tableName = RPT_STAFF_SALE_TABLE;
		this.columns = new String[] { STAFF_ID, STAFF_NAME, STAFF_CODE, PARENT_STAFF_ID, SHOP_ID, MONTH_AMOUNT_PLAN,
				MONTH_AMOUNT, MONTH_SCORE, DAY_AMOUNT_PLAN, DAY_AMOUNT, DAY_SCORE, MONTH_SKU_PLAN, MONTH_SKU,
				FOCUS1_AMOUNT_PLAN, FOCUS1_AMOUNT, FOCUS2_AMOUNT_PLAN, FOCUS2_AMOUNT, CREATE_DATE, UPDATE_DATE };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = SQLUtils.getInstance().getmDB();
	}

	public RPT_STAFF_SALE_TABLE(SQLiteDatabase mDB) {
		this.tableName = RPT_STAFF_SALE_TABLE;
		this.columns = new String[] { STAFF_ID, STAFF_NAME, STAFF_CODE, PARENT_STAFF_ID, SHOP_ID, MONTH_AMOUNT_PLAN,
				MONTH_AMOUNT, MONTH_SCORE, DAY_AMOUNT_PLAN, DAY_AMOUNT, DAY_SCORE, MONTH_SKU_PLAN, MONTH_SKU,
				FOCUS1_AMOUNT_PLAN, FOCUS1_AMOUNT, FOCUS2_AMOUNT_PLAN, FOCUS2_AMOUNT, CREATE_DATE, UPDATE_DATE };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;
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
	 * 
	 * lay thong tin bao cao tien do ban mat hang trong tam
	 * 
	 * @author: HoanPD1
	 * @param staffIdGS
	 * @return
	 * @return: ProgressReportSalesFocusDTO
	 * @throws:
	 */
	public ProgressReportSalesFocusDTO getProgReportSalesFocus(Bundle data) {
		ProgressReportSalesFocusDTO dto = new ProgressReportSalesFocusDTO();
		Cursor c = null;
		long staffId = data.getLong(IntentConstants.INTENT_STAFF_ID);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		StringBuffer sqlQuery = new StringBuffer();
		try {
			sqlQuery.append(" SELECT *");
			sqlQuery.append(" FROM rpt_staff_sale");
			sqlQuery.append(" left join staff s on rpt_staff_sale.staff_id = s.staff_id");
			sqlQuery.append(" WHERE rpt_staff_sale.PARENT_STAFF_ID = ?");
			sqlQuery.append(" and s.status = 1 ");
			sqlQuery.append(" and s.shop_id = ? ");
			sqlQuery.append(" and date(rpt_staff_sale.CREATE_DATE, 'start of month') = date('now', 'localtime','start of month')");
			sqlQuery.append(" ORDER BY rpt_staff_sale.STAFF_CODE");
			c = this.rawQuery(sqlQuery.toString(), new String[] { String.valueOf(staffId), shopId });
			for (int i = 1; i < Integer.MAX_VALUE; i++) {
				int idx = c.getColumnIndex("FOCUS" + i + "_AMOUNT");
				if (idx >= 0) {
					dto.arrMMTTText.add(String.valueOf(i));
					ReportProductFocusItem fItem = dto.newReportProductFocusItem();
					dto.arrRptFocusItemTotal.add(fItem);
				} else {
					break;
				}
			}
			if (c.moveToFirst()) {
				do {
					InfoProgressEmployeeDTO item = dto.newInfoProgressEmployeeDTO();
					item.staffId = c.getInt(c.getColumnIndex("STAFF_ID"));
					item.staffCode = c.getString(c.getColumnIndex("STAFF_CODE"));
					item.staffName = c.getString(c.getColumnIndex("STAFF_NAME"));
					item.staffPhone = c.getString(c.getColumnIndex(STAFF_TABLE.PHONE));
					item.staffMobile = c.getString(c.getColumnIndex(STAFF_TABLE.MOBILE_PHONE));

					for (int i = 0, size = dto.arrMMTTText.size(); i < size; i++) {
						String number = dto.arrMMTTText.get(i);
						ReportProductFocusItem fItemTT = dto.arrRptFocusItemTotal.get(i);
						ReportProductFocusItem fItem = dto.newReportProductFocusItem();
						fItem.planMoney = c.getDouble(c.getColumnIndex("FOCUS" + number + "_AMOUNT_PLAN"));
						fItem.planMoney = Math.round(fItem.planMoney / 1000.0);
						fItem.soldMoney = c.getDouble(c.getColumnIndex("FOCUS" + number + "_AMOUNT"));
						fItem.soldMoney = Math.round(fItem.soldMoney / 1000.0);
						fItem.leftMoney = fItem.planMoney > fItem.soldMoney ? fItem.planMoney - fItem.soldMoney : 0;
						fItem.soldPercent = (int) (fItem.planMoney > 0 ? fItem.soldMoney * 100 / fItem.planMoney
								: 100.0);
						fItemTT.planMoney += fItem.planMoney;
						fItemTT.soldMoney += fItem.soldMoney;
						fItemTT.leftMoney += fItem.leftMoney;
						item.arrRptFocusItem.add(fItem);
					}

					dto.addItem(item);
				} while (c.moveToNext());
			}
			dto.PlanDay = new SALE_DAYS_TABLE(mDB).getPlanWorkingDaysOfMonth(new Date());
			dto.PastDay = new EXCEPTION_DAY_TABLE(mDB).getWorkingDaysOfMonth();
			dto.Progress = (int) (dto.PlanDay > 0 ? (double) dto.PastDay * 100 / (double) dto.PlanDay : 0);
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
	 * get DTO for AccSaleProgReport
	 * 
	 * @param staffId
	 * @return
	 */
	public AccSaleProgReportDTO getAccSaleProgReportDTO(long staffId, String shopId) {
		AccSaleProgReportDTO dto = new AccSaleProgReportDTO();
		Cursor c = null;
		try {
			StringBuffer sqlQuery = new StringBuffer();
			List<String> params = new ArrayList<String>();
			params.add(String.valueOf(staffId));
			params.add(shopId);
			sqlQuery.append("SELECT s.staff_id          STAFF_ID, ");
			sqlQuery.append("       s.staff_code        STAFF_CODE, ");
			sqlQuery.append("       s.staff_name        STAFF_NAME, ");
			sqlQuery.append("       rpt_staff_sale.month_amount_plan MONTH_AMOUNT_PLAN, ");
			sqlQuery.append("       rpt_staff_sale.month_amount      MONTH_AMOUNT, ");
			sqlQuery.append("       rpt_staff_sale.month_sku_plan    MONTH_SKU_PLAN, ");
			sqlQuery.append("       rpt_staff_sale.month_sku         MONTH_SKU, ");
			sqlQuery.append("       rpt_staff_sale.month_score       MONTH_SCORE, ");
			sqlQuery.append("       s.mobilephone                MOBILE ");
			sqlQuery.append("FROM   rpt_staff_sale ");
			sqlQuery.append("       LEFT JOIN staff s");
			sqlQuery.append("              ON rpt_staff_sale.staff_id = s.staff_id ");
			sqlQuery.append("WHERE  rpt_staff_sale.parent_staff_id = ? ");
			// sqlQuery.append("       AND s.status > 0 ");
			sqlQuery.append("       AND s.shop_id = ? ");
			sqlQuery.append("       AND Date(rpt_staff_sale.create_date, 'start of month') = ");
			sqlQuery.append("           Date('now', 'localtime','start of month') ");
			sqlQuery.append("ORDER  BY rpt_staff_sale.staff_code ");

			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));

			if (c.moveToFirst()) {
				do {
					AccSaleProgReportItem item = dto.newAccSaleProgReportItem();
					item.parseDataFromCursor(c);
					dto.addItem(item);
				} while (c.moveToNext());
			}
			dto.monthSalePlan = new SALE_DAYS_TABLE(mDB).getPlanWorkingDaysOfMonth(new Date());
			dto.soldSalePlan = new EXCEPTION_DAY_TABLE(mDB).getWorkingDaysOfMonth();
			dto.perSalePlan = (int) (dto.monthSalePlan > 0 ? (double) dto.soldSalePlan * 100
					/ (double) dto.monthSalePlan : 100);
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

	public double getMinScoreNotification() {
		StringBuffer sqlQuery = new StringBuffer();
		double minScoreNotification = 2.5;
		sqlQuery.append("select  ap_param_code from ap_param ap where type like 'MIN_SCORE_NOTIFICATION' AND ap.status =1 ");
		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString(), null);

			if (c != null && c.moveToFirst()) {
				do {
					if (c.getColumnIndex("ap_param_code") > -1) {
						minScoreNotification = c.getDouble(c.getColumnIndex("ap_param_code"));
					}
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return minScoreNotification;
	}

	/**
	 * HieuNH Lay danh sach bao cao ngay cua NVGS
	 * 
	 * @param staffOwnerId
	 *            : id cua NVGS
	 * @return
	 */
	public ProgressDateReportDTO getProgressDateReport(long staffOwnerId, long shopId) {
		ProgressDateReportDTO dto = new ProgressDateReportDTO();
		dto.minScoreNotification = getMinScoreNotification();
		String date_now = DateUtils
				.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		List<String> params = new ArrayList<String>();
		params.add(String.valueOf(staffOwnerId));
		params.add(String.valueOf(shopId));
		params.add(String.valueOf(date_now));

		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT s.staff_name        NVBH, ");
		sqlQuery.append("       s.staff_code        MA_NVBH, ");
		sqlQuery.append("       s.mobilephone       MOBILE, ");
		sqlQuery.append("       s.staff_id          ID, ");
		sqlQuery.append("       rss.day_amount_plan DOANH_SO_KH_TUYEN,");
//		sqlQuery.append("       rss.MONTH_AMOUNT_PLAN MONTH_AMOUNT_PLAN, ");
//		sqlQuery.append("       rss.MONTH_AMOUNT MONTH_AMOUNT, ");
		sqlQuery.append("       rss.DAY_PLAN DAY_PLAN, ");
		sqlQuery.append("       rss.day_amount      DOANH_SO_TH, ");
		// sqlQuery.append("       CASE ");
		// sqlQuery.append("         WHEN ( rss.day_amount_plan > rss.day_amount ) THEN ( ");
		// sqlQuery.append("         rss.day_amount_plan - rss.day_amount ) ");
		// sqlQuery.append("         ELSE 0 ");
		// sqlQuery.append("       end                 AS DOANH_SO_CL, ");
		sqlQuery.append("       rss.day_score       DIEM, ");
		sqlQuery.append("       day_cust_order_plan DON_HANG_KH, ");
		sqlQuery.append("       day_cust_order      DON_HANG_TH ");
		sqlQuery.append("FROM   rpt_staff_sale rss ");
		sqlQuery.append("       LEFT JOIN staff s ");
		sqlQuery.append("              ON rss.staff_id = s.staff_id ");
		sqlQuery.append("WHERE  rss.parent_staff_id = ? ");
		sqlQuery.append("       AND s.status = 1 ");
		sqlQuery.append("       AND s.shop_id = ? ");
		sqlQuery.append("       AND Date(rss.create_date, 'start of month') = ");
		sqlQuery.append("           Date(?, 'start of month') ");
		sqlQuery.append("ORDER  BY rss.staff_code ");

		Cursor c = null;
//		int soldDayInMonth = new EXCEPTION_DAY_TABLE(mDB).getWorkingDaysOfMonth();
//		int numDaySalePlan = new SALE_DAYS_TABLE(mDB).getPlanWorkingDaysOfMonth(new Date());
//		dto.remainDayInMonth = numDaySalePlan - soldDayInMonth;
//
//		// Neu ngay hom nay
//		if (!new EXCEPTION_DAY_TABLE(mDB).checkExceptionDay(dateNow)) {
//			dto.remainDayInMonth++;
//		}
		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));

			if (c != null && c.moveToFirst()) {
				do {
					dto.addItem(c);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
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
	 * HieuNH Lay danh sach bao cao ngay cua NVGS
	 *            : id cua NVGS
	 * @return
	 */
	public ProgressDateDetailReportDTO getProgressDateDetailReport(long staffId) {
		ProgressDateDetailReportDTO dto = new ProgressDateDetailReportDTO();
		StringBuffer sqlQuery = new StringBuffer();
		List<String> params = new ArrayList<String>();
		params.add("" + staffId);
		sqlQuery.append("SELECT C.housenumber    SoNha, ");
		sqlQuery.append("       C.street         Duong, ");
		sqlQuery.append("       C.address        address, ");
		sqlQuery.append("       substr(C.customer_code, 1, 3)  MA_KH, ");
		sqlQuery.append("       C.customer_name  KH, ");
		sqlQuery.append("       RSRD.amount_plan MUC_TIEU, ");
		sqlQuery.append("       RSRD.amount      THUC_HIEN, ");
		sqlQuery.append("       RSRD.score       DIEM, ");
		sqlQuery.append("       RSRD.is_new      IS_MOI, ");
		sqlQuery.append("       RSRD.is_on       IS_ON, ");
		sqlQuery.append("       RSRD.is_or       IS_NT ");
		sqlQuery.append("FROM   rpt_sale_result_detail RSRD, ");
		sqlQuery.append("       customer C ");
		sqlQuery.append("WHERE  RSRD.customer_id = C.customer_id ");
		sqlQuery.append("       AND RSRD.staff_id = ? ");
		sqlQuery.append("       AND Date(RSRD.sale_date) = ");
		sqlQuery.append("           Date('now', 'localtime') ");
		sqlQuery.append("       order by C.short_code ");
		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));

			if (c != null && c.moveToFirst()) {
				do {
					dto.addItem(c);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
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
	 * HieuNH Lay danh sach bao cao ngay cua TBHV
	 * 
	 * @param staffOwnerId
	 *            : id cua TBHV
	 * @return
	 */
	public TBHVProgressDateReportDTO getTBHVProgressDateDetailReport(String shopId, String staffOwnerId) {
		// SHOP_TABLE shopTB = new SHOP_TABLE(this.mDB);
		// ArrayList<String> listShopId = shopTB.getShopRecursive(shopId);
		// String strListShop = TextUtils.join(",", listShopId);
		TBHVProgressDateReportDTO dto = new TBHVProgressDateReportDTO();
		StringBuffer sqlQuery = new StringBuffer();
		List<String> params = new ArrayList<String>();
		params.add(shopId);

		sqlQuery.append("SELECT st.staff_name      TEN_NPP, ");
		sqlQuery.append("       rss.staff_name       TEN_GSNPP, ");
		sqlQuery.append("       rss.day_plan DOANH_SO_KH, ");
		sqlQuery.append("       rss.day_amount      DOANH_SO_TH, ");
		sqlQuery.append("       rss.day_sku_plan    SKU_KE_HOACH, ");
		sqlQuery.append("       rss.day_sku         SKU_THUC_HIEN ");
		sqlQuery.append("FROM   rpt_staff_sale rss, ");
		sqlQuery.append("       staff stNVBH, ");
		sqlQuery.append("       staff st ");
		sqlQuery.append("WHERE  1 = 1 ");
		sqlQuery.append("       AND rss.shop_id = ? ");
		sqlQuery.append("       AND stNVBH.staff_id = rss.staff_id ");
		sqlQuery.append("       AND stNVBH.STAFF_OWNER_ID = st.staff_id ");
		sqlQuery.append("       AND st.status = 1 AND stNVBH.status = 1 ");
		sqlQuery.append("       AND Date(rss.create_date, 'start of month') = ");
		sqlQuery.append("           Date(?, 'start of month') ");
		params.add(DateUtils.now());
		if (!StringUtil.isNullOrEmpty(staffOwnerId)) {
			params.add(staffOwnerId);
			sqlQuery.append(" AND st.staff_id = ?");
		}
		sqlQuery.append("ORDER  BY ten_npp, ");
		sqlQuery.append("          ten_gsnpp ");

		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));

			if (c != null && c.moveToFirst()) {
				do {
					dto.addItem(c);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
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
	 * HieuNH Lay bao ca ngay cua TBHV
	 * 
	 * @param shopId
	 *            : id cua TBHV
	 * @return
	 */
	public TBHVProgressDateReportDTO getTBHVProgressDateReport(String shopId) {
		TBHVProgressDateReportDTO dto = new TBHVProgressDateReportDTO();

		SHOP_TABLE shopTB = new SHOP_TABLE(this.mDB);
		ArrayList<String> listShopId = shopTB.getShopRecursiveReverse(shopId);
		String strListShop = TextUtils.join(",", listShopId);

		StringBuffer sqlQuery = new StringBuffer();
		List<String> params = new ArrayList<String>();
		// params.add("" + shopId);
		params.add(String.valueOf(UserDTO.TYPE_GSNPP));

		sqlQuery.append("SELECT sh.shop_name             TEN_NPP, ");
		sqlQuery.append("       sh.shop_code             MA_NPP, ");
		sqlQuery.append("       st1.staff_name           TEN_GSNPP, ");
		sqlQuery.append("       st1.staff_code           MA_GSNPP, ");
		sqlQuery.append("       Sum(rss.day_plan) AS DOANH_SO_KH, ");
		sqlQuery.append("       Sum(rss.day_amount)      AS DOANH_SO_TH, ");
		sqlQuery.append("       Sum(rss.day_sku_plan)    SKU_KE_HOACH_SUM, ");
		sqlQuery.append("       Count(st.staff_id)       SKU_KE_HOACH_COUNT, ");
		sqlQuery.append("       Sum(rss.day_sku)         SKU_THUC_HIEN_AMOUNT, ");
		sqlQuery.append("       Count(st.staff_owner_id) SKU_THUC_HIEN_COUNT ");
		sqlQuery.append("FROM   rpt_staff_sale rss, ");
		sqlQuery.append("       shop sh, ");
		sqlQuery.append("       staff st, ");
		sqlQuery.append("       staff st1 ");
		sqlQuery.append("WHERE  rss.shop_id = sh.shop_id ");
		sqlQuery.append("       AND sh.parent_shop_id IN ( ");
		sqlQuery.append(strListShop + " ) ");
		sqlQuery.append("       AND st.staff_id = rss.staff_id ");
		sqlQuery.append("       AND st.staff_owner_id = st1.staff_id ");
		sqlQuery.append("       AND st1.staff_id IN (SELECT s.staff_id ");
		sqlQuery.append("                            FROM   staff s, ");
		sqlQuery.append("                                   channel_type c ");
		sqlQuery.append("                            WHERE  s.staff_type_id = c.channel_type_id ");
		sqlQuery.append("                                   AND c.object_type = ? ");
		sqlQuery.append("                                   AND c.type = 2) ");
		sqlQuery.append("       AND st.status = 1 ");
		sqlQuery.append("       AND st1.status = 1 ");
		sqlQuery.append("       AND Date(rss.create_date, 'start of month') = ");
		sqlQuery.append("           Date(?, 'start of month') ");
		params.add(DateUtils.now());
		sqlQuery.append("GROUP  BY sh.shop_name, ");
		sqlQuery.append("          st1.staff_name ");
		sqlQuery.append("ORDER  BY ma_npp, ");
		sqlQuery.append("          ten_gsnpp ");
		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));

			if (c != null && c.moveToFirst()) {
				do {
					dto.addItem(c);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
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
	 * Lay danh sach not PSDS thuoc TBHV
	 * 
	 * @author: QuangVT
	 * @since: 5:13:47 PM Dec 16, 2013
	 * @return: TBHVCustomerNotPSDSReportDTO
	 * @throws:  
	 * @param shopId
	 * @return
	 */
	public TBHVCustomerNotPSDSReportDTO getTBHVNotPSDSReport(String staff_owner_id) {
		TBHVCustomerNotPSDSReportDTO dto = new TBHVCustomerNotPSDSReportDTO();
		String dateFirstMonth = DateUtils.getFirstDateOfNumberPreviousMonthWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT,0);
		String dateNow=DateUtils
				.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		ArrayList<String> params = new ArrayList<String>();  
		StringBuffer  sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT STAFF_ID, ");
		sqlQuery.append("       COUNT(*)              AS VISIT_NUMBER_IN_MONTH, ");
		sqlQuery.append("       SUM(NUM_CUS_NOT_PSDS) AS NUM_CUS_NOT_PSDS, ");
		sqlQuery.append("       ID_NPP                AS SHOP_ID, ");
		sqlQuery.append("       CODE_NPP              AS SHOP_CODE, ");
		sqlQuery.append("       NAME_GSBH             AS STAFF_NAME, ");
		sqlQuery.append("       ID_GSBH               AS STAFF_ID, ");
		sqlQuery.append("       CODE_GSBH             AS STAFF_CODE ");
		sqlQuery.append("FROM   (SELECT RT_CUS.STAFF_ID       STAFF_ID, ");
		sqlQuery.append("               RT_CUS.ID_NPP         AS ID_NPP, ");
		sqlQuery.append("               RT_CUS.CODE_NPP       AS CODE_NPP, ");
		sqlQuery.append("               RT_CUS.STAFF_OWNER_ID STAFF_OWNER_ID, ");
		sqlQuery.append("               STAFF_GS.STAFF_ID     ID_GSBH, ");
		sqlQuery.append("               STAFF_GS.STAFF_NAME   NAME_GSBH, ");
		sqlQuery.append("               STAFF_GS.STAFF_CODE   CODE_GSBH, ");
		sqlQuery.append("               (SELECT CASE ");
		sqlQuery.append("                         WHEN ( ");
		sqlQuery.append("                                ( IFNULL(DATE (ST_CUS.LAST_APPROVE_ORDER) < ?, 1) ");
		params.add(dateFirstMonth);
		sqlQuery.append("                                      AND IFNULL(substr(ST_CUS.LAST_ORDER,1,10) < ?, ");
		params.add(dateNow);
		sqlQuery.append("                                          1) ) ) THEN 1 ");
		sqlQuery.append("                         ELSE 0 ");
		sqlQuery.append("                       END)          AS NUM_CUS_NOT_PSDS ");
		sqlQuery.append("        FROM   (SELECT IFNULL(CU.ADDRESS, '') ADDRESS, ");
		sqlQuery.append("                       CU.SHORT_CODE          CUSTOMER_SHORT_CODE, ");
		sqlQuery.append("                       A.START_DATE           RT_CUS_START_DATE, ");
		sqlQuery.append("                       A.END_DATE             RT_CUS_END_DATE, ");
		sqlQuery.append("                       B.STAFF_ID             STAFF_ID, ");
		sqlQuery.append("                       CU.CUSTOMER_ID, ");
		sqlQuery.append("                       SH.SHOP_ID             ID_NPP, ");
		sqlQuery.append("                       SH.SHOP_CODE           AS CODE_NPP, ");
		sqlQuery.append("                       S.STAFF_OWNER_ID       AS STAFF_OWNER_ID ");
		sqlQuery.append("                FROM   (SELECT * ");
		sqlQuery.append("                        FROM   ROUTING_CUSTOMER ");
		sqlQuery.append("                        WHERE  1 = 1 ");
		sqlQuery.append("                               AND STATUS = 1 ");
		sqlQuery.append("                               AND ( MONDAY ");
		sqlQuery.append("                                      OR TUESDAY ");
		sqlQuery.append("                                      OR WEDNESDAY ");
		sqlQuery.append("                                      OR THURSDAY ");
		sqlQuery.append("                                      OR FRIDAY ");
		sqlQuery.append("                                      OR SATURDAY ");
		sqlQuery.append("                                      OR SUNDAY ) = 1 ");
		sqlQuery.append("                               AND substr(START_DATE,1,10) <= ? ");
		params.add(dateNow);
		sqlQuery.append("                               AND IFNULL(substr(END_DATE,1,10) >= ?, 1 ");
		params.add(dateNow);
		sqlQuery.append("                                   )) A, ");
		sqlQuery.append("                       VISIT_PLAN B, ");
		sqlQuery.append("                       ROUTING C, ");
		sqlQuery.append("                       STAFF S, ");
		sqlQuery.append("                       SHOP SH, ");
		sqlQuery.append("                       CUSTOMER CU ");
		sqlQuery.append("                WHERE  1 = 1 ");
		sqlQuery.append("                       AND B.ROUTING_ID = A.ROUTING_ID ");
		sqlQuery.append("                       AND C.ROUTING_ID = A.ROUTING_ID ");
		sqlQuery.append("                       AND B.STAFF_ID = S.STAFF_ID ");
		sqlQuery.append("                       AND A.CUSTOMER_ID = CU.CUSTOMER_ID ");
		sqlQuery.append("                       AND S.SHOP_ID = SH.SHOP_ID ");
		sqlQuery.append("                       AND SH.STATUS = 1 ");
		sqlQuery.append("                       AND B.STATUS in (0, 1) ");
		sqlQuery.append("                       AND S.STATUS = 1 ");
		sqlQuery.append("                       AND C.STATUS = 1 ");
		sqlQuery.append("                       AND CU.STATUS = 1 ");
		sqlQuery.append("                       AND ? >= substr(B.FROM_DATE,1,10) ");
		params.add(dateNow);
		sqlQuery.append("                       AND IFNULL(? <= substr(B.TO_DATE,1,10), 1) ");
		params.add(dateNow);
		sqlQuery.append("                       AND B.STAFF_ID IN (SELECT STAFF_ID AS ID_NVBH ");
		sqlQuery.append("                                          FROM   STAFF ");
		sqlQuery.append("                                          WHERE  STAFF_OWNER_ID IN ");
		sqlQuery.append("                                                 (SELECT ");
		sqlQuery.append("                                                 STAFF_ID AS ID_GSBH ");
		sqlQuery.append("                                                                    FROM   STAFF ");
		sqlQuery.append("                                                                    WHERE  1 = 1 ");
//		sqlQuery.append("                                                                           AND ");
//		sqlQuery.append("                                                 STAFF_OWNER_ID = ");
//		sqlQuery.append("                                                 ? ");
//		params.add(staff_owner_id);
		sqlQuery.append("												AND STAFF_ID in(	");
		sqlQuery.append("	    											SELECT	");
		sqlQuery.append("	        											sgd.STAFF_ID	");
		sqlQuery.append("	    											FROM	");
		sqlQuery.append("	        											staff_group_detail sgd	");
		sqlQuery.append("	    											WHERE	");
		sqlQuery.append("	        											sgd.STAFF_GROUP_ID IN       (	");
		sqlQuery.append("	            											SELECT	");
		sqlQuery.append("	                											sg1.staff_group_id	");
		sqlQuery.append("	            											FROM	");
		sqlQuery.append("	                											staff_group sg1	");
		sqlQuery.append("	            											WHERE	");
		sqlQuery.append("	                											sg1.STAFF_ID = ?	");
		params.add(staff_owner_id);
		sqlQuery.append("	                											AND sg1.status = 1	");
		sqlQuery.append("	                											AND sg1.GROUP_LEVEL = 3	");
		sqlQuery.append("	                											AND sg1.GROUP_TYPE = 4	");
		sqlQuery.append("	        									))	");
		sqlQuery.append("                                                                           AND ");
		sqlQuery.append("                                                 STATUS = 1 ");
		sqlQuery.append("                                                                           AND ");
		sqlQuery.append("                                                 STAFF_TYPE_ID IN ");
		sqlQuery.append("                                                 (SELECT ");
		sqlQuery.append("                                                 CHANNEL_TYPE_ID ");
		sqlQuery.append("                                                 FROM ");
		sqlQuery.append("                                                 CHANNEL_TYPE ");
		sqlQuery.append("                                                 WHERE ");
		sqlQuery.append("                                                 TYPE = 2 ");
		sqlQuery.append("                                                 AND OBJECT_TYPE ");
		sqlQuery.append("                                                     IN ( 5 )) ");
		sqlQuery.append("                                                                           ) ");
		sqlQuery.append("                                                 AND STAFF_TYPE_ID IN ");
		sqlQuery.append("                                                     (SELECT ");
		sqlQuery.append("                                                     CHANNEL_TYPE_ID ");
		sqlQuery.append("                                                                       FROM ");
		sqlQuery.append("                                                     CHANNEL_TYPE ");
		sqlQuery.append("                                                                       WHERE ");
		sqlQuery.append("                                                     TYPE = 2 ");
		sqlQuery.append("                                                     AND ");
		sqlQuery.append("                                                     OBJECT_TYPE ");
		sqlQuery.append("                                                     IN ( ");
		sqlQuery.append("                                                     1 )) ");
		sqlQuery.append("                                                     AND STATUS = 1 ");
		sqlQuery.append("                                         )) RT_CUS ");
		sqlQuery.append("               LEFT JOIN STAFF_CUSTOMER ST_CUS ");
		sqlQuery.append("                      ON ( RT_CUS.STAFF_ID = ST_CUS.STAFF_ID ");
		sqlQuery.append("                           AND RT_CUS.CUSTOMER_ID = ST_CUS.CUSTOMER_ID ) ");
		sqlQuery.append("               LEFT JOIN STAFF STAFF_GS ");
		sqlQuery.append("                      ON STAFF_GS.STAFF_ID = RT_CUS.STAFF_OWNER_ID ");
		sqlQuery.append("        WHERE  1 = 1) ");
		sqlQuery.append(" GROUP  BY CODE_NPP, NAME_GSBH "); 

		Cursor c = null;
		try {
			c = this.rawQueries(sqlQuery.toString(), params);

			if (c != null && c.moveToFirst()) {
				do {
					dto.addItem(c);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("[Quang]", ex.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) { 
				VTLog.i("[Quang]", e.toString());
			}
		}
		return dto;
	}

	/**
	 *  lay danh sach cac lan ghe tham khach hang chua PSDS cua NVBH
	 * @author duongdt3
	 * @param staffId
	 *            : id NVBH
	 * @param customerIddddd
	 *            : id Khach Hang
	 * @return
	 */
	public VisitDTO getListVisitNotPSDS(long staffId, long customerId) {
		VisitDTO dto = new VisitDTO();
		ArrayList<String> params = new ArrayList<String>();
			
		StringBuffer sqlObject = new StringBuffer();
		/*sqlQuery.append("SELECT Strftime('%d/%m/%Y : %H:%M', al.start_time) THOI_GIAN, ");
		sqlQuery.append("       al.object_type                              TRANG_THAI ");
		sqlQuery.append("FROM   action_log al ");
		sqlQuery.append("WHERE  ( al.object_type = 0 ");
		sqlQuery.append("          OR al.object_type = 1 ) ");
		sqlQuery.append("       AND staff_id NOT IN (SELECT staff_id ");
		sqlQuery.append("                            FROM   action_log ");
		sqlQuery.append("                            WHERE  customer_id = ? ");
		sqlQuery.append("                                   AND Date(al.start_time) = Date(start_time) ");
		sqlQuery.append("                                   AND object_type = 4) ");
		sqlQuery.append("       AND al.staff_id = ? ");
		sqlQuery.append("       AND al.customer_id = ? ");
		sqlQuery.append("       AND Date(al.start_time) >= (SELECT ");
		sqlQuery.append("           Date('now', 'localtime','start of month')) ");*/
		sqlObject.append("	SELECT al.THOI_GIAN THOI_GIAN,  ");
		sqlObject.append("				   (CASE  ");
		sqlObject.append("					 WHEN alOrder.object_type IS NOT NULL ");
		sqlObject.append("					 THEN 2                 ");
		sqlObject.append("					 ELSE al.object_type  ");
		sqlObject.append("				END)       TRANG_THAI ");
		sqlObject.append("			FROM   (SELECT Strftime('%d/%m/%Y %H:%M', start_time) THOI_GIAN, *  FROM action_log  ");
		sqlObject.append("					   WHERE 1=1   ");
		sqlObject.append("					   AND DATE(start_time) >= DATE('now', 'localtime','start of month') ");
		sqlObject.append("					   AND object_type IN (0,1) ");
		sqlObject.append("					   AND staff_id = ?  ");
		//ADD PARAM
		params.add("" + staffId);
		sqlObject.append("					   AND customer_id = ? ");
		//ADD PARAM
		params.add("" + customerId);
		sqlObject.append("			   ) al ");
		sqlObject.append("		LEFT JOIN  ");
		sqlObject.append("			 (SELECT * FROM action_log  ");
		sqlObject.append("					 WHERE 1=1 ");
		sqlObject.append("					 AND object_type IN (4)                    ");
		sqlObject.append("					 AND staff_id = ?  ");
		//ADD PARAM
		params.add("" + staffId);
		sqlObject.append("					 AND customer_id = ? ");
		//ADD PARAM
		params.add("" + customerId);
		sqlObject.append("					 GROUP BY customer_id, staff_id, Date(start_time) ");
		sqlObject.append("			  ) alOrder  ");
		sqlObject.append("		ON Date(al.start_time) = Date(alOrder.start_time) ");
		sqlObject.append("			WHERE  1=1  ");
		sqlObject.append("			ORDER BY THOI_GIAN ASC ");
		
		Cursor c = null;
		try {
			c = this.rawQueries(sqlObject.toString(), params);
			if (c != null && c.moveToFirst()) {
				do {
					dto.addItem(c);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
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
	 * HieuNH lay danh sach thiet bi
	 * 
	 * @param staffIdGS
	 *            : id NVBH
	 * @param customerId
	 *            : id Khach Hang
	 * @return
	 */
	public ManagerEquipmentDTO getListEquipment(long staffOwnerId) {
		ManagerEquipmentDTO dto = new ManagerEquipmentDTO();
		List<String> params = new ArrayList<String>();
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT distinct s.staff_id            id, ");
		sqlQuery.append("       s.staff_code          maNVBH, ");
		sqlQuery.append("       s.staff_name          nvbh, ");
		sqlQuery.append("       Count(re.customer_id) soThietBi, ");
		sqlQuery.append("       Sum(re.result)        dat ");
		sqlQuery.append("FROM   rpt_equipment re ");
		sqlQuery.append("       JOIN staff s ");
		sqlQuery.append("              ON re.staff_id = s.staff_id ");
		sqlQuery.append("WHERE  re.parent_staff_id = ? and s.shop_id = ?");
		params.add("" + staffOwnerId);
		params.add(GlobalInfo.getInstance().getProfile().getUserData().shopId);
		sqlQuery.append("       AND Strftime('%m', re.month, 'localtime') = ");
		sqlQuery.append("           Strftime('%m', 'now', 'localtime') ");
		sqlQuery.append("GROUP  BY s.staff_id ");
		sqlQuery.append("ORDER  BY manvbh, ");
		sqlQuery.append("          nvbh ");
		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				do {
					dto.addItem(c);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
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
	 * 
	 * Lay ds thiet bi theo NVBH cua TBHV
	 * 
	 * @author: Nguyen Huu Hieu
	 * @param nvbhShopId
	 * @return
	 * @return: ManagerEquipmentDTO
	 * @throws:
	 */
	public TBHVManagerEquipmentDTO getListEquipmentTBHV(long parentShopId) {
		TBHVManagerEquipmentDTO dto = new TBHVManagerEquipmentDTO();
		List<String> params = new ArrayList<String>();
		params.add("" + parentShopId);

		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT ab.shop_id id, ab.shop_code maNVBH, ab.shop_name nvbh,");
		sqlQuery.append(" ab.total soThietBi,  ab.totalpass,");
		sqlQuery.append(" (ab.total - case when ab.totalpass is null then 0 else ab.totalpass end) AS khongDat");
		sqlQuery.append(" FROM");
		sqlQuery.append(" ((SELECT sh.shop_id, sh.shop_name, sh.shop_code, dt.customer_id, COUNT (dt.customer_id) AS total");
		sqlQuery.append(" FROM display_tools dt, staff s, customer c, product p, shop sh");
		sqlQuery.append(" WHERE dt.staff_id = s.staff_id AND dt.shop_id = sh.shop_id AND sh.parent_shop_id = ?");
		sqlQuery.append(" AND dt.customer_id = c.customer_id AND c.status = 1 AND s.status > 0");
		sqlQuery.append(" AND dt.product_id = p.product_id AND p.status = 1");
		sqlQuery.append(" AND p.category_code = 'Z'");
		sqlQuery.append(" AND strftime('%m', dt.in_month) = (select strftime('%m', date('now', 'localtime')))");
		sqlQuery.append(" GROUP BY dt.shop_id) a");
		sqlQuery.append(" left outer join");
		sqlQuery.append(" (SELECT a.shop_id, COUNT (*) AS totalpass");
		sqlQuery.append(" FROM");
		sqlQuery.append(" (SELECT dt.shop_id, dt.customer_id, dt.amount, SUM (sim.amount)");
		sqlQuery.append(" FROM display_tools dt,  display_tools_product dtp, sale_in_month sim");
		sqlQuery.append(" WHERE 1 = 1");
		sqlQuery.append(" AND dt.product_id = dtp.tool_id");
		sqlQuery.append(" AND strftime('%m', dt.in_month) = (select strftime('%m', date('now', 'localtime')))");
		sqlQuery.append(" AND sim.product_id = dtp.product_id");
		sqlQuery.append(" AND sim.customer_id = dt.customer_id");
		sqlQuery.append(" AND strftime('%m', sim.month) = (select strftime('%m', date('now', 'localtime')))");
		sqlQuery.append(" GROUP BY dt.shop_id, dt.customer_id, dt.amount HAVING SUM (sim.amount) >= dt.amount) a");
		sqlQuery.append(" GROUP BY a.shop_id) b on a.shop_id = b.shop_id) ab");
		sqlQuery.append(" order by khongDat desc,  soThietBi desc, maNVBH, nvbh");
		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				do {
					dto.addItem(c);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
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
	 * 
	 * Lay ds thiet bi theo NVBH cua NPP
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param shopId
	 * @return
	 * @return: ManagerEquipmentDTO
	 * @throws:
	 */
	public TBHVManagerEquipmentDTO getListEquipment(String shopId) {
		TBHVManagerEquipmentDTO dto = new TBHVManagerEquipmentDTO();
		List<String> params = new ArrayList<String>();
		params.add(shopId);

		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT ab.staff_id id, ab.staff_code maNVBH, ab.name nvbh,");
		sqlQuery.append(" ab.total soThietBi,  ab.totalpass,");
		sqlQuery.append(" (ab.total - case when ab.totalpass is null then 0 else ab.totalpass end) AS khongDat");
		sqlQuery.append(" FROM");
		sqlQuery.append(" ((SELECT dt.staff_id, s.name, s.staff_code, dt.customer_id, COUNT (dt.customer_id) AS total");
		sqlQuery.append(" FROM display_tools dt, staff s, customer c, product p");
		// sqlQuery.append(" WHERE dt.staff_id = s.staff_id AND s.staff_owner_id = ?");
		sqlQuery.append(" WHERE dt.staff_id = s.staff_id AND dt.shop_id = ?");
		sqlQuery.append(" AND dt.customer_id = c.customer_id AND c.status = 1");
		sqlQuery.append(" AND dt.product_id = p.product_id AND p.status = 1");
		sqlQuery.append(" AND p.category_code = 'Z'");
		sqlQuery.append(" AND strftime('%m', dt.in_month) = (select strftime('%m', date('now', 'localtime')))");
		sqlQuery.append(" GROUP BY dt.staff_id) a");
		sqlQuery.append(" left outer join");
		sqlQuery.append(" (SELECT a.staff_id, COUNT (*) AS totalpass");
		sqlQuery.append(" FROM");
		sqlQuery.append(" (SELECT dt.staff_id, dt.customer_id, dt.amount, SUM (sim.amount)");
		sqlQuery.append(" FROM display_tools dt,  display_tools_product dtp, sale_in_month sim");
		sqlQuery.append(" WHERE 1 = 1");
		sqlQuery.append(" AND dt.product_id = dtp.tool_id");
		sqlQuery.append(" AND strftime('%m', dt.in_month) = (select strftime('%m', date('now', 'localtime')))");
		sqlQuery.append(" AND sim.product_id = dtp.product_id");
		sqlQuery.append(" AND sim.customer_id = dt.customer_id");
		sqlQuery.append(" AND strftime('%m', sim.month) = (select strftime('%m', date('now', 'localtime')))");
		sqlQuery.append(" GROUP BY dt.staff_id, dt.customer_id, dt.amount HAVING SUM (sim.amount) >= dt.amount) a");
		sqlQuery.append(" GROUP BY a.staff_id) b on a.staff_id = b.staff_id) ab");
		sqlQuery.append(" order by khongDat desc,  soThietBi desc, maNVBH, nvbh");
		// Cursor c = this.rawQuery(sqlQuery.toString(),params.toArray(new
		// String[params.size()]));
		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				do {
					dto.addItem(c);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
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
	 * HieuNH lay count danh sach tu nhan vien
	 * 
	 * @param staffId
	 *            : id NVBH
	 * @param customerId
	 *            : id Khach Hang
	 * @return
	 */
	public int getCountCabinetStaff(long shopId, long staffId, int isAll) {
		int count = 0;
		List<String> params = new ArrayList<String>();
		params.add("" + staffId);
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT c.customer_id                       id, ");
		sqlQuery.append("       c.customer_code                     MA_KH, ");
		sqlQuery.append("       c.customer_name                     TEN_KH, ");
		sqlQuery.append("       c.housenumber                       SO_NHA, ");
		sqlQuery.append("       c.street                            DUONG, ");
		sqlQuery.append("       Round(re.amount_plan / 1000.0)      KE_HOACH, ");
		sqlQuery.append("       Round(re.amount / 1000.0)           THUC_HIEN, ");
		sqlQuery.append("       ( Round(re.amount_plan / 1000.0) - CASE ");
		sqlQuery.append("                                       WHEN re.amount IS NULL THEN 0 ");
		sqlQuery.append("                                       ELSE Round(re.amount / 1000.0) ");
		sqlQuery.append("                                     END ) CON_LAI, ");
		sqlQuery.append("       re.result                           DAT ");
		sqlQuery.append("FROM   customer c, ");
		sqlQuery.append("       rpt_equipment re ");
		sqlQuery.append("WHERE  re.staff_id = ? ");
		sqlQuery.append("       AND re.customer_id = c.customer_id ");
		sqlQuery.append("       AND Strftime('%m', re.month, 'localtime') = Strftime('%m', 'now', 'localtime') ");
		if (isAll == 0) {// khong lay tat ca
			sqlQuery.append(" AND CON_LAI > 0");
		}
		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				count = c.getCount();
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		return count;

	}

	/**
	 * HieuNH lay danh sach tu nhan vien
	 * 
	 * @param staffId
	 *            : id NVBH
	 * @param customerId
	 *            : id Khach Hang
	 * @return
	 */
	public CabinetStaffDTO getCabinetStaff(long shopId, long staffId, int isAll, String page) {
		CabinetStaffDTO dto = new CabinetStaffDTO();
		List<String> params = new ArrayList<String>();
		params.add("" + staffId);

		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT c.customer_id                       id, ");
		sqlQuery.append("       c.customer_code                     MA_KH, ");
		sqlQuery.append("       c.customer_name                     TEN_KH, ");
		sqlQuery.append("       c.housenumber                       SO_NHA, ");
		sqlQuery.append("       c.street                            DUONG, ");
		sqlQuery.append("       pr.product_name                     EQUIP_NAME, ");
		sqlQuery.append("       Round(re.amount_plan / 1000.0)      KE_HOACH, ");
		sqlQuery.append("       Round(re.amount / 1000.0)           THUC_HIEN, ");
		sqlQuery.append("       ( Round(re.amount_plan / 1000.0) - CASE ");
		sqlQuery.append("                                       WHEN re.amount IS NULL THEN 0 ");
		sqlQuery.append("                                       ELSE Round(re.amount / 1000.0) ");
		sqlQuery.append("                                     END ) CON_LAI, ");
		sqlQuery.append("       re.result                           DAT ");
		sqlQuery.append("FROM   customer c, ");
		sqlQuery.append("       rpt_equipment re, product pr ");
		sqlQuery.append("WHERE  re.staff_id = ? ");
		sqlQuery.append("       AND re.customer_id = c.customer_id ");
		sqlQuery.append("       AND re.equip_product_id = pr.product_id ");
		sqlQuery.append("       AND Strftime('%m', re.month, 'localtime') = Strftime('%m', 'now', 'localtime') ");
		if (isAll == 0) {// khong lay tat ca
			sqlQuery.append(" AND re.result = 0");
		}
		sqlQuery.append(" ORDER  BY con_lai DESC, ");
		sqlQuery.append("          ke_hoach DESC, ");
		sqlQuery.append("          thuc_hien DESC, ");
		sqlQuery.append("          ma_kh, ");
		sqlQuery.append("          ten_kh ");
		sqlQuery.append(page);
		Cursor c = null;

		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				do {
					dto.addItem(c);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
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
	 * HieuNH lay danh sach NPP cua TBHV
	 * 
	 * @param parentShopId
	 * 
	 * @return
	 */
	public ArrayList<ShopDTO> getListNPP(String shopId) {
		SHOP_TABLE shopTB = new SHOP_TABLE(this.mDB);
		ArrayList<String> listShopId = shopTB.getShopRecursiveReverse(shopId);
		String strListShop = TextUtils.join(",", listShopId);

		ArrayList<ShopDTO> listShop = new ArrayList<ShopDTO>();
		ArrayList<String> params = new ArrayList<String>();
		// params.add("" + parentShopId);
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT distinct s.shop_id   SHOP_ID, ");
		sqlQuery.append("       s.shop_code SHOP_CODE, ");
		sqlQuery.append("       s.shop_name SHOP_NAME ");
		sqlQuery.append("FROM   shop s ");
		sqlQuery.append("WHERE  s.parent_shop_id IN (");
		sqlQuery.append(strListShop);
		sqlQuery.append(")");
		sqlQuery.append(" AND s.shop_id in (select distinct shop_id from rpt_staff_sale) ");
		sqlQuery.append("ORDER  BY shop_code, ");
		sqlQuery.append("          shop_name ");

		// params.add(strListShop);
		Cursor c = null;
		try {
			c = this.rawQueries(sqlQuery.toString(), params);
			if (c != null && c.moveToFirst()) {
				do {
					ShopDTO item = new ShopDTO();
					item.parseDataFromCusor(c);
					listShop.add(item);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		return listShop;
	}

	/**
	 * HieuNH lay danh sach NVGS cua NPP
	 * 
	 * @param shopId
	 * 
	 * @return
	 */
	public ListStaffDTO getListNVGSOfTBHVReportPSDS(String shopId) {
		ListStaffDTO dto = new ListStaffDTO();
		List<String> params = new ArrayList<String>();
		params.add(shopId);
		StringBuffer sqlQuery = new StringBuffer();

		// SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
//		ArrayList<String> shopList = shopTable.getShopRecursiveReverse(shopId);
//		String shopStr = TextUtils.join(",", shopList

		sqlQuery.append("SELECT GS.STAFF_ID          AS STAFF_ID, ");
		sqlQuery.append("       GS.STAFF_CODE        AS STAFF_CODE, ");
		sqlQuery.append("       GS.STAFF_NAME        AS STAFF_NAME, ");
		sqlQuery.append("       SH.SHOP_ID                  AS NVBH_SHOP_ID, ");
		sqlQuery.append("       SH.SHOP_CODE          AS NVBH_SHOP_CODE, ");
		sqlQuery.append("       COUNT(NVBH.STAFF_ID) AS NUM_NVBH ");
		sqlQuery.append("FROM   STAFF NVBH, ");
		sqlQuery.append("       CHANNEL_TYPE CH, ");
		sqlQuery.append("       SHOP SH, ");
		sqlQuery.append("       STAFF GS, ");
		sqlQuery.append("       CHANNEL_TYPE GS_CH ");
		sqlQuery.append("WHERE  NVBH.STAFF_TYPE_ID = CH.CHANNEL_TYPE_ID ");
		sqlQuery.append("       AND CH.TYPE = 2 ");
		sqlQuery.append("       AND CH.OBJECT_TYPE IN ( 1, 2 ) ");
		sqlQuery.append("       AND NVBH.SHOP_ID = SH.SHOP_ID ");
		sqlQuery.append("       AND NVBH.STATUS = 1 ");
		sqlQuery.append("       AND SH.STATUS = 1 ");
		// sqlQuery.append("       AND NVBH.SHOP_ID IN ( " + shopStr + " ) ");
		sqlQuery.append("       AND NVBH.SHOP_ID = ? ");
		sqlQuery.append("       AND NVBH.STAFF_OWNER_ID IS NOT NULL ");
		sqlQuery.append("       AND NVBH.STAFF_OWNER_ID = GS.STAFF_ID ");
		sqlQuery.append("       AND GS.STAFF_TYPE_ID = GS_CH.CHANNEL_TYPE_ID ");
		sqlQuery.append("       AND GS_CH.TYPE = 2 ");
		sqlQuery.append("       AND GS_CH.OBJECT_TYPE = ? ");
		sqlQuery.append("       AND GS.STATUS = 1 ");
		sqlQuery.append("GROUP  BY SH.SHOP_ID, ");
		sqlQuery.append("          NVBH.STAFF_OWNER_ID ");
		sqlQuery.append("ORDER  BY SH.SHOP_CODE, ");
		sqlQuery.append("          GS.STAFF_NAME ");

		// sqlQuery.append("SELECT st.staff_name STAFF_NAME, ");
		// sqlQuery.append("       st.staff_id STAFF_ID, ");
		// sqlQuery.append("       st.staff_code STAFF_CODE ");
		// sqlQuery.append("FROM   (SELECT DISTINCT s1.staff_name, ");
		// sqlQuery.append("                        s1.staff_id, ");
		// sqlQuery.append("                        s1.staff_code ");
		// sqlQuery.append("        FROM   visit_plan vp, ");
		// sqlQuery.append("               staff s, ");
		// sqlQuery.append("               staff s1, ");
		// sqlQuery.append("               (SELECT shop_id, ");
		// sqlQuery.append("                       shop_name, ");
		// sqlQuery.append("                       shop_code ");
		// sqlQuery.append("                FROM   shop ");
		// if (!StringUtil.isNullOrEmpty(shopId)) {
		// sqlQuery.append(" WHERE shop_id = ?");
		// params.add("" + shopId);
		// }
		// sqlQuery.append("       ) sh WHERE  s.status > 0 ");
		// sqlQuery.append("               AND vp.staff_id = s.staff_id ");
		// sqlQuery.append("               AND s.staff_owner_id = s1.staff_id ");
		// sqlQuery.append("               AND s1.shop_id = sh.shop_id ");
		// sqlQuery.append("               AND s1.staff_id IN (SELECT s.staff_id ");
		// sqlQuery.append("                                   FROM   staff s, ");
		// sqlQuery.append("                                          channel_type c ");
		// sqlQuery.append("                                   WHERE  s.staff_type_id = c.channel_type_id ");
		// sqlQuery.append("                                          AND c.object_type = ? ");
		// sqlQuery.append("                                          AND c.type = 2) ");
		// sqlQuery.append("               AND ( (SELECT Date('now', 'localtime')) > Date(vp.from_date) ");
		// sqlQuery.append("                     AND ( vp.to_date IS NULL ");
		// sqlQuery.append("                            OR (SELECT Date('now', 'localtime')) < ");
		// sqlQuery.append("                               Date(vp.to_date) ) ) ");
		// sqlQuery.append("               AND s.status > 0) st ");

		params.add("" + UserDTO.TYPE_GSNPP);
		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				if (c.getCount() > 1) {
					dto.addItemAll();
				}
				do {
					dto.parrseStaffFromCursor(c);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
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
	 * HieuNH lay danh sach NVGS cua NPP
	 * 
	 * @param shopId
	 * 
	 * @return
	 */
	public ListStaffDTO getListNVGSOfTBHVReportDate(String shopId) {
		ListStaffDTO dto = new ListStaffDTO();
		List<String> params = new ArrayList<String>();
		params.add(String.valueOf(UserDTO.TYPE_GSNPP));
		StringBuffer sqlQuery = new StringBuffer();

		sqlQuery.append("SELECT distinct st1.staff_name STAFF_NAME, ");
		sqlQuery.append("       st1.staff_code, ");
		sqlQuery.append("       st1.staff_id ");
		sqlQuery.append("FROM   rpt_staff_sale rss, ");
		sqlQuery.append("       staff st, ");
		sqlQuery.append("       staff st1 ");
		sqlQuery.append("WHERE  st.staff_id = rss.staff_id ");
		sqlQuery.append("       AND st.staff_owner_id = st1.staff_id ");
		sqlQuery.append("       AND st1.staff_id IN (SELECT s.staff_id ");
		sqlQuery.append("                            FROM   staff s, ");
		sqlQuery.append("                                   channel_type c ");
		sqlQuery.append("                            WHERE  s.staff_type_id = c.channel_type_id ");
		sqlQuery.append("                                   AND c.object_type = ? ");
		sqlQuery.append("                                   AND c.type = 2) ");
		if (!StringUtil.isNullOrEmpty(shopId)) {
			sqlQuery.append(" AND rss.shop_id = ?");
			params.add(String.valueOf(shopId));
		}
		sqlQuery.append("       AND Date(rss.create_date, 'start of month') = ");
		sqlQuery.append("           Date('now', 'localtime','start of month') ");
		sqlQuery.append("GROUP  BY st1.staff_code, st1.staff_name ");

		// sqlQuery.append("SELECT st1.STAFF_NAME STAFF_NAME, st1.STAFF_CODE, st1.STAFF_ID");
		// sqlQuery.append(" FROM RPT_STAFF_SALE rss, STAFF st, staff st1");
		// sqlQuery.append(" WHERE st.STAFF_ID = rss.staff_id AND st.staff_owner_id = st1.staff_id AND st1.role_type = 2 AND st.STATUS > 0 AND st1.STATUS > 0");
		// if (!StringUtil.isNullOrEmpty(shopId)) {
		// sqlQuery.append(" AND rss.shop_id = ?");
		// params.add("" + shopId);
		// }
		// sqlQuery.append(" AND date(rss.CREATE_DATE, 'start of month') = date('now', 'start of month', 'localtime') GROUP BY st1.STAFF_NAME");

		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				if (c.getCount() > 1) {
					dto.addItemAll();
				}
				do {
					dto.parrseStaffFromCursor(c);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
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
	 * trungnt56 lay danh sach NVBH thuoc quyen quan ly cua NVBH
	 *
	 * @param staffId
	 *            : id NVGS
	 * @return
	 */
	public ListStaffDTO getListStaffForGSBHOrderCode(long staffId, boolean isHaveAll) {
		STAFF_TABLE staff_table = new STAFF_TABLE(mDB);
		ArrayList<String> strings = new ArrayList<>();
		if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSTINH){
			strings = staff_table.getStaffGroupTBHV(String.valueOf(staffId));
		}else {
			strings = staff_table.getStaffRecursiveReverseTBHV(String.valueOf(staffId));
		}
		StringBuffer lstStaffId = new StringBuffer();
		for (String s:
			 strings) {
			if(lstStaffId.length() > 0) {
				lstStaffId.append(",");
			}
			lstStaffId.append(s);
		}
		ListStaffDTO dto = new ListStaffDTO();
		List<String> params = new ArrayList<String>();
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("select st.STAFF_ID as STAFF_ID, st.STAFF_NAME as STAFF_NAME, st.STAFF_CODE as STAFF_CODE from STAFF st where st.STAFF_ID in (");
		sqlQuery.append(lstStaffId);
		sqlQuery.append(") and st.STATUS = 1");
		sqlQuery.append(" and st.staff_type_id in (select channel_type_id from channel_type where status = 1 and type = 2 and object_type in (1,5,11)) ");
		sqlQuery.append(" order by st.STAFF_CODE");
		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				if (isHaveAll && c.getCount() > 1) {
					dto.addItemAll();
				}
				do {
					dto.addItem(c);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
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
	 * HieuNH lay danh sach NVBH thuoc quyen quan ly cua NVGS
	 * 
	 * @param shopId
	 * @param staffOwnerId
	 *            : id NVGS
	 * @return
	 */
	public ListStaffDTO getListNVBHOrderCode(long shopId, String staffOwnerId, boolean isHaveAll) {
		ListStaffDTO dto = new ListStaffDTO();
		List<String> params = new ArrayList<String>();
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("select st.STAFF_ID as STAFF_ID, st.STAFF_NAME as STAFF_NAME, st.STAFF_CODE as STAFF_CODE from STAFF st where st.SHOP_ID = ? and st.STATUS = 1");
		params.add(String.valueOf(shopId));
		sqlQuery.append(" and st.staff_type_id in (select channel_type_id from channel_type where status = 1 and type = 2 and object_type in (1,5)) ");
		if (!StringUtil.isNullOrEmpty(staffOwnerId)) {
			sqlQuery.append(" and st.STAFF_OWNER_ID = ?");
			params.add(staffOwnerId);
		}
		sqlQuery.append(" order by st.STAFF_CODE");
		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				if (isHaveAll && c.getCount() > 1) {
					dto.addItemAll();
				}
				do {
					dto.addItem(c);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
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
	 * HieuNH lay danh sach lich su mua hang cua NVBH ban cho Khach hang
	 * 
	 * @param nvbhShopId
	 * @param staffOwnerId
	 *            : id NVGS
	 * @return
	 */
	public Vector<HistoryItemDTO> getListHistory(String staffId, String customerId, String productId) {
		Vector<HistoryItemDTO> dto = new Vector<HistoryItemDTO>();
		HistoryItemDTO item;
		List<String> params = new ArrayList<String>();
		StringBuffer sqlQuery = new StringBuffer();

		sqlQuery.append("SELECT P.product_code        SKU, ");
		sqlQuery.append("       p.product_id, ");
		sqlQuery.append("       FB.feedback_id, ");
		sqlQuery.append("       Ifnull(SIM.amount, 0) DOANH_SO ");
		sqlQuery.append("FROM   feedback FB, ");
		sqlQuery.append("       feedback_detail FBD ");
		sqlQuery.append("       LEFT JOIN (SELECT Sum(RSIM.amount) AMOUNT, ");
		sqlQuery.append("                         RSIM.product_id ");
		sqlQuery.append("                  FROM   rpt_sale_in_month RSIM ");
		sqlQuery.append("                  WHERE  1 = 1 and RSIM.status = 1 ");
		if (!StringUtil.isNullOrEmpty(staffId)) {
			sqlQuery.append("                         AND RSIM.staff_id = ? ");
			params.add(staffId);
		}
		if (!StringUtil.isNullOrEmpty(customerId)) {
			sqlQuery.append("                  AND RSIM.customer_id = ? ");
			params.add(customerId);
		}

		sqlQuery.append("                         AND Date(RSIM.month, 'start of month') = (SELECT ");
		sqlQuery.append("                             Date('now', 'localtime','start of month')) ");
		sqlQuery.append("                  GROUP  BY RSIM.product_id) SIM ");
		sqlQuery.append("              ON FBD.product_id = SIM.product_id, ");
		sqlQuery.append("       product P ");
		sqlQuery.append("WHERE  FB.feedback_id = FBD.feedback_id ");
		sqlQuery.append("       AND FBD.product_id = P.product_id ");
		sqlQuery.append("       AND FB.type = 9 ");
		if (!StringUtil.isNullOrEmpty(staffId)) {
			sqlQuery.append("       AND FB.staff_id = ? ");
			params.add(staffId);
		}
		if (!StringUtil.isNullOrEmpty(customerId)) {
			sqlQuery.append("       AND FB.customer_id = ? ");
			params.add(customerId);
		}
		sqlQuery.append("GROUP  BY sku ");
		sqlQuery.append("ORDER  BY doanh_so DESC ");

		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				do {
					item = new HistoryItemDTO();
					item.initDateWithCursor(c);
					dto.addElement(item);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
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
	 * 
	 * get report progress in month of TBHV
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: ReportProgressMonthViewDTO
	 * @throws:
	 */
	public ReportProgressMonthViewDTO getReportProgressInMonthOfTBHV(Bundle data) {
		SHOP_TABLE shopTB = new SHOP_TABLE(this.mDB);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		ArrayList<String> listShopId = shopTB.getShopRecursiveReverse(shopId);
		String strListShop = TextUtils.join(",", listShopId);
		ReportProgressMonthViewDTO result = new ReportProgressMonthViewDTO();

		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT sp.shop_id                                     STAFF_OWNER_ID, ");
		sqlQuery.append("       sp.shop_name                                   STAFF_OWNER_NAME, ");
		sqlQuery.append("       sp.shop_code                                   STAFF_OWNER_CODE, ");
		sqlQuery.append("       st.staff_id                                    STAFF_ID, ");
		sqlQuery.append("       st.staff_name                                  STAFF_NAME, ");
		sqlQuery.append("       st.staff_code                                  STAFF_CODE, ");
		sqlQuery.append("       Sum(rss.month_amount_plan)                     MONTH_AMOUNT_PLAN, ");
		sqlQuery.append("       Sum(rss.month_amount)                          MONTH_AMOUNT, ");
		sqlQuery.append("       ( Sum (rss.month_sku) / Count (rss.staff_id) ) MONTH_SKU, ");
		sqlQuery.append("       Ifnull(NEW_SS.PLAN, 0)                         MONTH_SKU_PLAN, ");
		sqlQuery.append("       Ifnull(CASE ");
		sqlQuery.append("                WHEN ( Sum(rss.month_amount_plan) = 0 ");
		sqlQuery.append("                       AND Sum(rss.month_amount) > 0 ) THEN 1 ");
		sqlQuery.append("                ELSE Sum(rss.month_amount) / Sum(rss.month_amount_plan) ");
		sqlQuery.append("              END, 0)                                 PERCENT ");
		sqlQuery.append("FROM   shop sp, ");
		sqlQuery.append("       staff st, ");
		sqlQuery.append("       rpt_staff_sale rss ");
		sqlQuery.append("       LEFT JOIN (SELECT PLAN, ");
		sqlQuery.append("                         object_id ");
		sqlQuery.append("                  FROM   rpt_sale_summary ");
		sqlQuery.append("                  WHERE  object_type = 1 ");
		sqlQuery.append("                         AND code = 'SKU_CT_NPP' ");
		sqlQuery.append("                         AND view_type = 2 ");
		sqlQuery.append("                         AND type = 2 ");
		sqlQuery.append("                         AND Ifnull(Date(sale_date) = ");
		sqlQuery.append("                                    Date('NOW', 'localtime','START OF MONTH'), ");
		sqlQuery.append("                             1) ");
		sqlQuery.append("                         AND status = 1) NEW_SS ");
		sqlQuery.append("              ON rss.shop_id = NEW_SS.object_id ");
		sqlQuery.append("WHERE  1 = 1 ");
		sqlQuery.append("       AND rss.shop_id = sp.shop_id ");
		// sqlQuery.append("       AND st.status = 1 ");
		sqlQuery.append("       AND sp.parent_shop_id IN ( ");
		sqlQuery.append(strListShop + ") ");
		sqlQuery.append("       AND st.staff_id = rss.parent_staff_id ");
		sqlQuery.append("       AND Ifnull(Date(rss.create_date, 'start of month') = ");
		sqlQuery.append("                      Date('now', 'localtime','start of month'), 1) ");
		sqlQuery.append("GROUP  BY sp.shop_id, ");
		sqlQuery.append("          sp.shop_name, ");
		sqlQuery.append("          sp.shop_code, ");
		sqlQuery.append("          st.staff_id, ");
		sqlQuery.append("          st.staff_name, ");
		sqlQuery.append("          st.staff_code ");
		sqlQuery.append("ORDER  BY staff_owner_code, ");
		sqlQuery.append("          staff_name ");

		Cursor c = null;
		// lay d/s shop theo dinh dang de truyen vao cau SQL
		String[] param = {};
		long totalAmountPlan = 0;
		long totalAmountDone = 0;
		try {
			c = this.rawQuery(sqlQuery.toString(), param);
			if (c != null && c.moveToFirst()) {
				do {
					ReportProgressMonthCellDTO item = new ReportProgressMonthCellDTO();
					item.initReportProgressMonthObject(c);
					totalAmountPlan += item.amountPlan;
					totalAmountDone += item.amountDone;
					result.listReportProgessMonthDTO.add(item);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		// row total
		result.totalReportObject.amountPlan = totalAmountPlan;
		result.totalReportObject.amountDone = totalAmountDone;
		result.totalReportObject.amountRemain = (totalAmountPlan - totalAmountDone) >= 0 ? (totalAmountPlan - totalAmountDone)
				: 0;
		if (totalAmountDone <= 0) {
			result.totalReportObject.progressAmountDone = 0;
		} else {
			result.totalReportObject.progressAmountDone = (int) (totalAmountDone * 100 / (totalAmountPlan <= 0 ? totalAmountDone
					: totalAmountPlan));
		}

		// number day plan sale in month
		try {
			result.numDaySalePlan = new SALE_DAYS_TABLE(mDB).getPlanWorkingDaysOfMonth(new Date());
			// number day sold in month
			result.numDaySoldPlan = new EXCEPTION_DAY_TABLE(mDB).getWorkingDaysOfMonth();
			// percent
			if (result.numDaySoldPlan <= 0) {
				result.progessSold = 0;
			} else {
				result.progessSold = (int) (result.numDaySoldPlan * 100 / (result.numDaySalePlan <= 0 ? result.numDaySoldPlan
						: result.numDaySalePlan));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		

		return result;
	}

	/**
	 * 
	 * get report progress in month of TBHV detail NPP
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: ReportProgressMonthViewDTO
	 * @throws:
	 */
	public ReportProgressMonthViewDTO getReportProgressInMonthOfTBHVDetailNPP(Bundle data) {
		String staffOwenerId = "";
		String shopId = "";
		if (data != null) {
			if (data.getString(IntentConstants.INTENT_STAFF_OWNER_ID) != null) {
				staffOwenerId = data.getString(IntentConstants.INTENT_STAFF_OWNER_ID);
			}
			if (data.getString(IntentConstants.INTENT_SHOP_ID) != null) {
				shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
			}
		}
		ReportProgressMonthViewDTO result = new ReportProgressMonthViewDTO();

		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT DISTINCT RPT_SS.staff_id          STAFF_ID, ");
		sqlQuery.append("                RPT_SS.newName        STAFF_NAME, ");
		sqlQuery.append("                RPT_SS.staff_code        STAFF_CODE, ");
		sqlQuery.append("                ST.staff_id              STAFF_OWNER_ID, ");
		sqlQuery.append("                ST.staff_name                  STAFF_OWNER_NAME, ");
		sqlQuery.append("                ST.staff_code            STAFF_OWNER_CODE, ");
		sqlQuery.append("                RPT_SS.month_amount_plan MONTH_AMOUNT_PLAN, ");
		sqlQuery.append("                RPT_SS.month_amount      MONTH_AMOUNT, ");
		sqlQuery.append("                RPT_SS.month_sku_plan    MONTH_SKU_PLAN, ");
		sqlQuery.append("                RPT_SS.month_sku         MONTH_SKU, ");
		sqlQuery.append("       ifnull(case  when (RPT_SS.month_amount_plan = 0 and RPT_SS.month_amount > 0) then 1 else RPT_SS.month_amount / RPT_SS.month_amount_plan end, 0) PERCENT ");
		sqlQuery.append("FROM   (select rss.*, st.staff_name newName from rpt_staff_sale rss , staff st where rss.staff_id = st.staff_id ) RPT_SS, ");
		sqlQuery.append("       staff ST ");
		sqlQuery.append("WHERE  RPT_SS.shop_id = ? ");
		// sqlQuery.append("       AND ST.status = 1 ");
		sqlQuery.append("       AND ST.staff_id = RPT_SS.parent_staff_id ");
		if (!StringUtil.isNullOrEmpty(staffOwenerId)) {
			sqlQuery.append("       AND RPT_SS.parent_staff_id = ? ");
		}
		sqlQuery.append("       AND ( Date(RPT_SS.create_date) IS NULL ");
		sqlQuery.append("              OR ( Date(RPT_SS.create_date, 'start of month') = ");
		sqlQuery.append("                       Date('now', 'localtime','start of month') ) ) ");
		sqlQuery.append("ORDER  BY staff_owner_name, ");
		// sqlQuery.append("ORDER  BY PERCENT, staff_owner_name, ");
		sqlQuery.append("          staff_name ");

		Cursor c = null;
		String[] paramsList = new String[] {};
		ArrayList<String> listParams = new ArrayList<String>();
		if (!StringUtil.isNullOrEmpty(staffOwenerId)) {
			listParams.add(shopId);
			listParams.add(staffOwenerId);
		} else {
			listParams.add(shopId);
		}
		paramsList = listParams.toArray(new String[listParams.size()]);

		long totalAmountPlan = 0;
		long totalAmountDone = 0;
		try {
			c = this.rawQuery(sqlQuery.toString(), paramsList);
			if (c != null && c.moveToFirst()) {
				do {
					ReportProgressMonthCellDTO item = new ReportProgressMonthCellDTO();
					item.initReportProgressMonthObject(c);
					totalAmountPlan += item.amountPlan;
					totalAmountDone += item.amountDone;
					result.listReportProgessMonthDTO.add(item);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		// row total
		result.totalReportObject.amountPlan = totalAmountPlan;
		result.totalReportObject.amountDone = totalAmountDone;
		result.totalReportObject.amountRemain = (totalAmountPlan - totalAmountDone) >= 0 ? (totalAmountPlan - totalAmountDone)
				: 0;
		if (totalAmountDone <= 0) {
			result.totalReportObject.progressAmountDone = 0;
		} else {
			result.totalReportObject.progressAmountDone = (int) (totalAmountDone * 100 / (totalAmountPlan <= 0 ? totalAmountDone
					: totalAmountPlan));
		}

		return result;
	}

	/**
	 * 
	 * exec query sql get report progress sales focus info
	 * 
	 * @author: HaiTC3
	 * @param data
	 * @return
	 * @return: TBHVReportProgressSalesFocusView
	 * @throws:
	 */
	public TBHVProgressReportSalesFocusViewDTO getReportProgressSalesFocusInfo(Bundle data) {
		TBHVProgressReportSalesFocusViewDTO result = new TBHVProgressReportSalesFocusViewDTO();
		Cursor c = null;
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		SHOP_TABLE shopTB = new SHOP_TABLE(this.mDB);
		ArrayList<String> listParentShopId = shopTB.getShopRecursive(shopId);
		ArrayList<String> listChildShopId = shopTB.getShopRecursiveReverse(shopId);
		String strListParentShop = TextUtils.join(",", listParentShopId);
		String strListChildShop = TextUtils.join(",", listChildShopId);
		try {
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append("SELECT DISTINCT S.shop_id                      STAFF_OWNER_ID, ");
			sqlQuery.append("                S.shop_name                    STAFF_OWNER_NAME, ");
			sqlQuery.append("                S.shop_code                    STAFF_OWNER_CODE, ");
			sqlQuery.append("                st.staff_id                    STAFF_ID, ");
			sqlQuery.append("                st.staff_name                        STAFF_NAME, ");
			sqlQuery.append("                st.staff_code                  STAFF_CODE, ");
			sqlQuery.append("                Sum(RPT_SS.focus1_amount_plan) FOCUS1_AMOUNT_PLAN, ");
			sqlQuery.append("                Sum(RPT_SS.focus1_amount)      FOCUS1_AMOUNT, ");
			sqlQuery.append("                Sum(RPT_SS.focus2_amount_plan) FOCUS2_AMOUNT_PLAN, ");
			sqlQuery.append("                Sum(RPT_SS.focus2_amount)      FOCUS2_AMOUNT, ");
			sqlQuery.append("                ifnull(case  when (Sum(RPT_SS.focus1_amount_plan) = 0 and Sum(RPT_SS.focus1_amount) > 0) then 1 else Sum(RPT_SS.focus1_amount) / Sum(RPT_SS.focus1_amount_plan) end, 0) PERCENT1, ");
			sqlQuery.append("                ifnull(case  when (Sum(RPT_SS.focus2_amount_plan) = 0 and Sum(RPT_SS.focus2_amount) > 0) then 1 else Sum(RPT_SS.focus2_amount) / Sum(RPT_SS.focus2_amount_plan) end, 0) PERCENT2 ");
			sqlQuery.append("FROM   shop S, ");
			sqlQuery.append("       rpt_staff_sale RPT_SS, ");
			sqlQuery.append("       staff ST ");
			sqlQuery.append("WHERE  S.shop_id = RPT_SS.shop_id ");
			sqlQuery.append("       AND ST.status = 1 ");
			sqlQuery.append("       AND (S.parent_shop_id IN ( ");
			sqlQuery.append(strListParentShop + " ) OR S.parent_shop_id IN ( ");
			sqlQuery.append(strListChildShop + " ))");
			sqlQuery.append("       AND ST.staff_id = rpt_ss.parent_staff_id ");
			sqlQuery.append("       AND ( Date(RPT_SS.create_date) IS NULL ");
			sqlQuery.append("              OR ( Date(RPT_SS.create_date, 'start of month') = ");
			sqlQuery.append("                       Date('now', 'localtime','start of month') ) ) ");
			sqlQuery.append("GROUP  BY S.shop_id, ");
			sqlQuery.append("          S.shop_name, ");
			sqlQuery.append("          S.shop_code, ");
			sqlQuery.append("          st.staff_id, ");
			sqlQuery.append("          st.staff_name, ");
			sqlQuery.append("          st.staff_code ");
			sqlQuery.append("ORDER  BY STAFF_OWNER_CODE, ");
			sqlQuery.append("          STAFF_NAME ,PERCENT1, PERCENT2 ");

			String[] param = new String[] {};

			c = this.rawQuery(sqlQuery.toString(), param);

			if (c != null && c.moveToFirst()) {
				boolean isCreateArrMMTTT = false;

				do {
					if (!isCreateArrMMTTT) {
						for (int i = 1; i < Integer.MAX_VALUE; i++) {
							int idx = c.getColumnIndex("FOCUS" + i + "_AMOUNT");
							if (idx >= 0) {
								result.arrMMTTText.add(String.valueOf(i));
								result.objectReportTotal.listFocusProductItem.add(new ReportFocusProductItem());
							} else {
								break;
							}
						}
						isCreateArrMMTTT = true;
					}
					ReportSalesFocusEmployeeInfo item = new ReportSalesFocusEmployeeInfo();

					item.parseDataFromCursor(c);

					for (int i = 0; i < result.arrMMTTText.size(); i++) {
						String number = result.arrMMTTText.get(i);
						ReportFocusProductItem fItem = new ReportFocusProductItem();
						fItem.parseDataFromCursor(c, number);
						fItem.remain = fItem.amountPlan > fItem.amount ? fItem.amountPlan - fItem.amount : (long) 0;
						fItem.progress = (int) (fItem.amountPlan > 0 ? fItem.amount * 100 / fItem.amountPlan : 100.0);
						result.objectReportTotal.listFocusProductItem.get(i).amountPlan += fItem.amountPlan;
						result.objectReportTotal.listFocusProductItem.get(i).amount += fItem.amount;
						item.listFocusProductItem.add(fItem);
					}
					result.listFocusInfoRow.add(item);
				} while (c.moveToNext());
			}

			// update total row
			for (int i = 0, size = result.objectReportTotal.listFocusProductItem.size(); i < size; i++) {
				ReportFocusProductItem TMP = result.objectReportTotal.listFocusProductItem.get(i);
				TMP.remain = TMP.amountPlan > TMP.amount ? TMP.amountPlan - TMP.amount : 0;
				TMP.progress = (int) (TMP.amountPlan > 0 ? TMP.amount * 100 / TMP.amountPlan : 100.0);
			}

			// get plan date, plan date done, progress percent sold
			result.numPlanDate = new SALE_DAYS_TABLE(mDB).getPlanWorkingDaysOfMonth(new Date());
			result.numPlanDateDone = new EXCEPTION_DAY_TABLE(mDB).getWorkingDaysOfMonth();
			result.progressSales = (int) (result.numPlanDate > 0 ? result.numPlanDateDone * 100 / result.numPlanDate
					: 0);
		} catch (Exception e) {
			result = null;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		return result;
	}

	/**
	 * 
	 * get report progress sales focus detail info from DB
	 * 
	 * @author: HaiTC3
	 * @param data
	 * @return
	 * @return: TBHVProgressReportSalesFocusViewDTO
	 * @throws:
	 */
	public TBHVProgressReportSalesFocusViewDTO getReportProgressSalesFocusDetailInfo(Bundle data) {
		String staffOwnerId = "";
		String shopId = "";
		if (data != null) {
			if (data.getString(IntentConstants.INTENT_STAFF_OWNER_ID) != null) {
				staffOwnerId = data.getString(IntentConstants.INTENT_STAFF_OWNER_ID);
			}
			if (data.getString(IntentConstants.INTENT_SHOP_ID) != null) {
				shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
			}
		}

		TBHVProgressReportSalesFocusViewDTO result = new TBHVProgressReportSalesFocusViewDTO();
		Cursor c = null;
		try {
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append("SELECT DISTINCT ST.staff_id STAFF_OWNER_ID, ");
			sqlQuery.append("                ST.staff_name     STAFF_OWNER_NAME, ");
			sqlQuery.append("                RPT_SS.* ,");
			sqlQuery.append("                ifnull(case  when (RPT_SS.FOCUS1_AMOUNT_PLAN = 0 and RPT_SS.FOCUS1_AMOUNT > 0) then 1 else RPT_SS.FOCUS1_AMOUNT / RPT_SS.FOCUS1_AMOUNT_PLAN end, 0) PERCENT1, ");
			sqlQuery.append("                ifnull(case  when (RPT_SS.FOCUS2_AMOUNT_PLAN = 0 and RPT_SS.FOCUS2_AMOUNT > 0) then 1 else RPT_SS.FOCUS2_AMOUNT / RPT_SS.FOCUS2_AMOUNT_PLAN end, 0) PERCENT2 ");
			sqlQuery.append("FROM   staff ST, ");
			sqlQuery.append("       (select rss.*, st.staff_name NEWNAME from rpt_staff_sale rss , staff st where rss.staff_id = st.staff_id and st.status = 1) RPT_SS ");
			sqlQuery.append("WHERE  RPT_SS.shop_id = ? ");
			sqlQuery.append("       AND ST.status = 1 ");
			sqlQuery.append("       AND ST.staff_id = RPT_SS.parent_staff_id ");
			sqlQuery.append("       AND ( Date(RPT_SS.create_date) IS NULL ");
			sqlQuery.append("              OR ( Date(RPT_SS.create_date, 'start of month') = ");
			sqlQuery.append("                       Date('now', 'localtime','start of month') ) ) ");
			if (!StringUtil.isNullOrEmpty(staffOwnerId)) {
				sqlQuery.append("       AND RPT_SS.parent_staff_id = ? ");
			}

			sqlQuery.append("ORDER  BY staff_owner_name, ");
			sqlQuery.append("          RPT_SS.staff_name, PERCENT1, PERCENT2 ");

			String[] paramsList = new String[] {};
			ArrayList<String> listParams = new ArrayList<String>();
			if (!StringUtil.isNullOrEmpty(staffOwnerId)) {
				listParams.add(shopId);
				listParams.add(staffOwnerId);
			} else {
				listParams.add(shopId);
			}
			paramsList = listParams.toArray(new String[listParams.size()]);

			c = this.rawQuery(sqlQuery.toString(), paramsList);

			if (c != null && c.moveToFirst()) {
				boolean isCreateArrMMTTT = false;

				do {
					if (!isCreateArrMMTTT) {
						for (int i = 1; i < Integer.MAX_VALUE; i++) {
							int idx = c.getColumnIndex("FOCUS" + i + "_AMOUNT");
							if (idx >= 0) {
								result.arrMMTTText.add(String.valueOf(i));
								result.objectReportTotal.listFocusProductItem.add(new ReportFocusProductItem());
							} else {
								break;
							}
						}
						isCreateArrMMTTT = true;
					}
					ReportSalesFocusEmployeeInfo item = new ReportSalesFocusEmployeeInfo();

					item.parseDataFromCursor(c);
					if (c.getColumnIndex("NEWNAME") >= 0) {
						String newName = c.getString(c.getColumnIndex("NEWNAME"));
						if (!StringUtil.isNullOrEmpty(newName)) {
							item.staffName = newName;
						}
					}

					for (int i = 0; i < result.arrMMTTText.size(); i++) {
						String number = result.arrMMTTText.get(i);
						ReportFocusProductItem fItem = new ReportFocusProductItem();
						fItem.parseDataFromCursor(c, number);
						fItem.remain = fItem.amountPlan > fItem.amount ? fItem.amountPlan - fItem.amount : (long) 0;
						fItem.progress = (int) (fItem.amountPlan > 0 ? (double) fItem.amount * 100
								/ (double) fItem.amountPlan : 100.0);
						result.objectReportTotal.listFocusProductItem.get(i).amountPlan += fItem.amountPlan;
						result.objectReportTotal.listFocusProductItem.get(i).amount += fItem.amount;
						item.listFocusProductItem.add(fItem);
					}
					result.listFocusInfoRow.add(item);
				} while (c.moveToNext());
			}

			// update total row
			for (int i = 0, size = result.objectReportTotal.listFocusProductItem.size(); i < size; i++) {
				ReportFocusProductItem TMP = result.objectReportTotal.listFocusProductItem.get(i);
				TMP.remain = TMP.amountPlan > TMP.amount ? TMP.amountPlan - TMP.amount : 0;
				TMP.progress = (int) (TMP.amountPlan > 0 ? (double) TMP.amount * 100 / (double) TMP.amountPlan : 100.0);
			}

		} catch (Exception e) {
			result = null;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		return result;
	}

}
