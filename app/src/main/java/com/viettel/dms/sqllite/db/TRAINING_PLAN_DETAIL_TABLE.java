/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;
import android.database.Cursor;
import android.text.TextUtils;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.view.GSNPPTrainingPlanDTO;
import com.viettel.dms.dto.view.GSNPPTrainingPlanDTO.GSNPPTrainingPlanIem;
import com.viettel.dms.dto.view.GsnppTrainingResultAccReportDTO;
import com.viettel.dms.dto.view.GsnppTrainingResultAccReportDTO.GsnppTrainingResultAccReportItem;
import com.viettel.dms.dto.view.TBHVTrainingPlanDTO;
import com.viettel.dms.dto.view.TBHVTrainingPlanDTO.TBHVTrainingPlanItem;
import com.viettel.dms.dto.view.TBHVTrainingPlanHistoryAccDTO;
import com.viettel.dms.dto.view.TBHVTrainingPlanHistoryAccDTO.TBHVHistoryPlanTrainingItem;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;

/**
 * TRAINING PLAN DETAIL TABLE
 * TRAINING_PLAN_DETAIL_TABLE.java
 * @version: 1.0 
 * @since:  08:26:35 20 Jan 2014
 */
public class TRAINING_PLAN_DETAIL_TABLE extends ABSTRACT_TABLE {

	// id bang
	public static final String ID = "ID";
	// ma training plan detail id
	public static final String TRAINING_PLAN_ID = "TRAINING_PLAN_ID";
	// staff id
	public static final String STAFF_ID = "STAFF_ID";
	// training date
	public static final String TRAINING_DATE = "TRAINING_DATE";
	// Trang thai cua lich huan luyen: 0: Tao moi; 1: Da huan luyen; 2: Huy bo;
	public static final String STATUS = "STATUS";
	// note info
	public static final String NOTE = "NOTE";
	// score
	public static final String SCORE = "SCORE";
	// create date
	public static final String CREATE_DATE = "CREATE_DATE";
	// update date
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// amount plan
	public static final String AMOUNT_PLAN = "AMOUNT_PLAN";
	// amount
	public static final String AMOUNT = "AMOUNT";
	// num customer plan
	public static final String NUM_CUSTOMER_PLAN = "NUM_CUSTOMER_PLAN";
	// num customer order
	public static final String NUM_CUSTOMER_ORDER = "NUM_CUSTOMER_ORDER";
	// num customer ir
	public static final String NUM_CUSTOMER_IR = "NUM_CUSTOMER_IR";
	// num customer or
	public static final String NUM_CUSTOMER_OR = "NUM_CUSTOMER_OR";
	// num customer new
	public static final String NUM_CUSTOMER_NEW = "NUM_CUSTOMER_NEW";
	// num customer on
	public static final String NUM_CUSTOMER_ON = "NUM_CUSTOMER_ON";
	// shop id
	public static final String SHOP_ID = "SHOP_ID";

	private static final String TRAINING_PLAN_DETAIL_TABLE = "TRAINING_PLAN_DETAIL_TABLE";

	public TRAINING_PLAN_DETAIL_TABLE(SQLiteDatabase mDB) {
		this.tableName = TRAINING_PLAN_DETAIL_TABLE;
		// this.columns = new String[] { STAFF_ID, STAFF_NAME, STAFF_CODE,
		// PARENT_STAFF_ID, SHOP_ID, MONTH_AMOUNT_PLAN, MONTH_AMOUNT,
		// MONTH_SCORE, DAY_AMOUNT_PLAN, DAY_AMOUNT, DAY_SCORE,
		// MONTH_SKU_PLAN, MONTH_SKU, FOCUS1_AMOUNT_PLAN, FOCUS1_AMOUNT,
		// FOCUS2_AMOUNT_PLAN, FOCUS2_AMOUNT, CREATE_DATE, UPDATE_DATE };
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
	 * getAccTrainResultReportDTO
	 * 
	 * @author: TamPQ
	 * @return: AccTrainResultReportDTO
	 * @throws:
	 */
	public GsnppTrainingResultAccReportDTO getAccTrainResultReportDTO(long staffId, String shopId) {
		GsnppTrainingResultAccReportDTO dto = new GsnppTrainingResultAccReportDTO();
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);
		ArrayList<String> params = new ArrayList<String>();
		StringBuffer var1 = new StringBuffer();
		var1.append("SELECT STAFF.STAFF_ID, ");
		var1.append("       STAFF.STAFF_CODE, ");
		var1.append("       STAFF.STAFF_NAME, ");
		var1.append("       STAFF.SALE_TYPE_CODE, ");
		var1.append("       TRAINING_PLAN_DETAIL.TRAINING_PLAN_DETAIL_ID, ");
		var1.append("       TRAINING_PLAN_DETAIL.TRAINING_DATE, ");
		var1.append("       TRAINING_PLAN_DETAIL.AMOUNT_PLAN, ");
		var1.append("       TRAINING_PLAN_DETAIL.AMOUNT, ");
		var1.append("       TRAINING_PLAN_DETAIL.NUM_CUSTOMER_PLAN, ");
		var1.append("       TRAINING_PLAN_DETAIL.NUM_CUSTOMER_ORDER, ");
		var1.append("       TRAINING_PLAN_DETAIL.NUM_CUSTOMER_NEW, ");
		var1.append("       TRAINING_PLAN_DETAIL.NUM_CUSTOMER_ON, ");
		var1.append("       TRAINING_PLAN_DETAIL.NUM_CUSTOMER_OR, ");
		var1.append("       TRAINING_PLAN_DETAIL.SCORE, ");
		var1.append("       TRAINING_PLAN_DETAIL.SHOP_ID ");
		var1.append("FROM   TRAINING_PLAN, ");
		var1.append("       TRAINING_PLAN_DETAIL, ");
		var1.append("       STAFF ");
		var1.append("WHERE  TRAINING_PLAN.STAFF_ID = ? ");
		params.add(""+staffId);
		var1.append("       AND TRAINING_PLAN.TRAINING_PLAN_ID = TRAINING_PLAN_DETAIL.TRAINING_PLAN_ID ");
		var1.append("       AND TRAINING_PLAN_DETAIL.STAFF_ID = STAFF.STAFF_ID ");
		var1.append("       AND TRAINING_PLAN_DETAIL.STATUS IN ( 0,1 ) ");
		var1.append("       AND STAFF.STATUS = 1 ");
		var1.append("       AND TRAINING_PLAN_DETAIL.SHOP_ID = ? ");
		params.add(shopId);
		var1.append("       AND TRAINING_PLAN.STATUS = 1 ");
		var1.append("       AND DATE(TRAINING_PLAN.MONTH, 'start of month') = DATE( ?, 'localtime','start of month') ");
		params.add(date_now);
		var1.append("       AND DATE(TRAINING_PLAN_DETAIL.TRAINING_DATE) >= DATE(?, 'localtime','start of month') ");
		params.add(date_now);
		var1.append("       AND DATE(TRAINING_PLAN_DETAIL.TRAINING_DATE) <= DATE(?) ");
		params.add(date_now);
		var1.append("ORDER  BY TRAINING_PLAN_DETAIL.TRAINING_DATE DESC ");

		Cursor c = null;
		try {
			c = this.rawQueries(var1.toString(), params);
			if (c != null && c.moveToFirst()) {
				do {
					GsnppTrainingResultAccReportItem i = dto.newAccTrainResultReportItem();
					i.initData(c);

					dto.listResult.add(i);
					dto.amountMonth += i.amountMonth;
					dto.amount += i.amount;
					dto.numCustomerPlan += i.numCustomerPlan;
					dto.numCustomerOrder += i.numCustomerOrder;
					dto.numCustomerNew += i.numCustomerNew;
					dto.numCustomerOn += i.numCustomerIr;
					dto.numCustomerOr += i.numCustomerOr;
					dto.score += i.score;
				} while (c.moveToNext());
			}

		} catch (Exception ex) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}

		return dto;
	}

	/**
	 * getPlanTrainResultReportDTO
	 * 
	 * @author: TamPQ
	 * @return: PlanTrainResultReportDTO
	 * @throws:
	 */
		GSNPPTrainingPlanDTO dto = new GSNPPTrainingPlanDTO();
		public GSNPPTrainingPlanDTO getGsnppTrainingPlan(long staffId, String shopId) {
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);
		ArrayList<String> param = new ArrayList<String>();
		StringBuffer var1 = new StringBuffer();
		var1.append("SELECT STAFF.STAFF_ID, ");
		var1.append("       STAFF.STAFF_CODE, ");
		var1.append("       STAFF.STAFF_NAME, ");
		var1.append("       STAFF.SHOP_ID, ");
		var1.append("       STAFF.SALE_TYPE_CODE, ");
		var1.append("       SHOP.SHOP_NAME, ");
		var1.append("       SHOP.SHOP_CODE, ");
		var1.append("       TRAINING_PLAN_DETAIL.TRAINING_PLAN_DETAIL_ID, ");
		var1.append("       TRAINING_PLAN_DETAIL.TRAINING_DATE, ");
		var1.append("       TRAINING_PLAN_DETAIL.SCORE ");
		var1.append("FROM   TRAINING_PLAN, ");
		var1.append("       TRAINING_PLAN_DETAIL, ");
		var1.append("       STAFF, ");
		var1.append("       SHOP ");
		var1.append("WHERE  TRAINING_PLAN.STAFF_ID = ? ");
		param.add("" + staffId);
		if (shopId != null) {
			var1.append("       AND TRAINING_PLAN_DETAIL.SHOP_ID = ? ");
			param.add(shopId);
		}
		var1.append("       AND TRAINING_PLAN.TRAINING_PLAN_ID = TRAINING_PLAN_DETAIL.TRAINING_PLAN_ID ");
		var1.append("       AND TRAINING_PLAN_DETAIL.STAFF_ID = STAFF.STAFF_ID ");
		var1.append("       AND TRAINING_PLAN_DETAIL.STATUS = 1 ");
		var1.append("       AND TRAINING_PLAN.STATUS = 1 ");
		var1.append("       AND STAFF.STATUS = 1 ");
		var1.append("       AND SHOP.SHOP_ID = STAFF.SHOP_ID ");
		var1.append("       AND DATE(TRAINING_PLAN.MONTH, 'start of month') = DATE('" + date_now
				+ "','start of month') ");
		var1.append("       AND DATE(TRAINING_PLAN_DETAIL.TRAINING_DATE, 'start of month') = DATE('" + date_now
				+ "','start of month') ");

		Cursor c = null;
		try {
			c = this.rawQueries(var1.toString(), param);
			if (c != null && c.moveToFirst()) {
				do {
					GSNPPTrainingPlanIem i = dto.newPlanTrainResultReportItem();
					i.initFromCursor(c);
					dto.listResult.put(i.dateString, i);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
		} finally {
			if (c != null) {
				c.close();
			}
		}

		return dto;
	}

	/**
	 * get Gsnpp Training Plan
	 * 
	 * @author: TamPQ
	 * @return: TBHVGsnppTrainingPlanDTO
	 * @throws:
	 */
	public TBHVTrainingPlanDTO getTbhvTrainingPlan(long staffId, long shopId) {
		TBHVTrainingPlanDTO dto = new TBHVTrainingPlanDTO();
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);

		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
		ArrayList<String> shopList = shopTable.getShopRecursiveReverse(String.valueOf(shopId));
		String shopStr = TextUtils.join(",", shopList);

		// lay ds gsnpp thuoc quyen quan ly
		ArrayList<String> param2 = new ArrayList<String>();
		StringBuffer var2 = new StringBuffer();
		var2.append("SELECT * ");
		var2.append("FROM   (");
		var2.append("SELECT GS.STAFF_ID          AS STAFF_ID, ");
		var2.append("       GS.STAFF_CODE        AS STAFF_CODE, ");
		var2.append("       GS.STAFF_NAME        AS STAFF_NAME, ");
		var2.append("       SH.SHOP_ID			 AS NVBH_SHOP_ID, ");
		var2.append("       SH.SHOP_CODE		 AS NVBH_SHOP_CODE, ");
		var2.append("       COUNT(NVBH.STAFF_ID) AS NUM_NVBH ");
		var2.append("FROM   STAFF NVBH, ");
		var2.append("       CHANNEL_TYPE CH, ");
		var2.append("       SHOP SH, ");
		var2.append("       STAFF GS, ");
		var2.append("       CHANNEL_TYPE GS_CH ");
		var2.append("WHERE  NVBH.STAFF_TYPE_ID = CH.CHANNEL_TYPE_ID ");
		var2.append("       AND CH.TYPE = 2 ");
		var2.append("       AND CH.OBJECT_TYPE IN ( 1, 2 ) ");
		var2.append("       AND NVBH.SHOP_ID = SH.SHOP_ID ");
		var2.append("       AND NVBH.STATUS = 1 ");
		var2.append("       AND SH.STATUS = 1 ");
		var2.append("       AND NVBH.SHOP_ID IN ( " + shopStr + " ) ");
		var2.append("       AND NVBH.STAFF_OWNER_ID IS NOT NULL ");
		var2.append("       AND NVBH.STAFF_OWNER_ID = GS.STAFF_ID ");
		var2.append("       AND GS.STAFF_TYPE_ID = GS_CH.CHANNEL_TYPE_ID ");
		var2.append("       AND GS_CH.TYPE = 2 ");
		var2.append("       AND GS_CH.OBJECT_TYPE = 5 ");
		var2.append("       AND GS.STATUS = 1 ");
		var2.append("GROUP  BY SH.SHOP_ID, ");
		// var2.append("GROUP  BY ");
		var2.append("          NVBH.STAFF_OWNER_ID ");
		var2.append("ORDER  BY SH.SHOP_CODE, ");
		// var2.append("ORDER  BY STAFF_CODE, ");
		var2.append("          GS.STAFF_NAME ");
		var2.append("		) GSNPP ");
		var2.append("       LEFT JOIN (SELECT STAFF.STAFF_ID                     AS NVBH_STAFF_ID, ");
		var2.append("                         TRAINING_PLAN_DETAIL.TRAINING_DATE AS NVBH_TRAINING_DATE, ");
		var2.append("                         STAFF.STAFF_OWNER_ID, ");
		var2.append("                         STAFF.SHOP_ID ");
		var2.append("                  FROM   TRAINING_PLAN, ");
		var2.append("                         TRAINING_PLAN_DETAIL, ");
		var2.append("                         STAFF, ");
		var2.append("                         CHANNEL_TYPE CH ");
		var2.append("                  WHERE  1 = 1 ");
		var2.append("                         AND TRAINING_PLAN.TRAINING_PLAN_ID = TRAINING_PLAN_DETAIL.TRAINING_PLAN_ID ");
		var2.append("                         AND TRAINING_PLAN_DETAIL.STAFF_ID = STAFF.STAFF_ID ");
		var2.append("                         AND TRAINING_PLAN_DETAIL.STATUS = 1 ");
		var2.append("                         AND TRAINING_PLAN.STATUS = 1 ");
		var2.append("                         AND STAFF.STATUS = 1 ");
		var2.append("                         AND DATE(TRAINING_PLAN.MONTH, 'start of month') = DATE('" + date_now
				+ "','start of month') ");
		var2.append("                         AND DATE(TRAINING_PLAN_DETAIL.TRAINING_DATE) = DATE(?) ");
		param2.add(date_now);
		var2.append("                         AND STAFF.STAFF_TYPE_ID = CH.CHANNEL_TYPE_ID ");
		var2.append("                         AND CH.OBJECT_TYPE IN (1,2) ");
		var2.append("                         AND CH.TYPE = 2) NVBH_TRAIN ");
		var2.append("              ON (GSNPP.STAFF_ID = NVBH_TRAIN.STAFF_OWNER_ID AND GSNPP.NVBH_SHOP_ID = NVBH_TRAIN.SHOP_ID) ");
		// var2.append("              ON GSNPP.STAFF_ID = NVBH_TRAIN.STAFF_OWNER_ID ");

		Cursor cr = null;
		try {
			cr = this.rawQueries(var2.toString(), param2);
			if (cr != null && cr.moveToFirst()) {
				do {
					dto.spinnerStaffList.addItem(cr);
				} while (cr.moveToNext());
			}
		} catch (Exception ex) {
			try {
				throw ex;
			} catch (Exception e) {
			}
		} finally {
			if (cr != null) {
				cr.close();
			}
		}

		ArrayList<String> param = new ArrayList<String>();
		StringBuffer var1 = new StringBuffer();
		var1.append("SELECT * ");
		var1.append("FROM   (SELECT TPD.TRAINING_PLAN_ID, ");
		var1.append("               TPD.TRAINING_PLAN_ID AS TRAINING_PLAN_DETAIL_ID, ");
		var1.append("               TPD.STAFF_ID         AS NVBH_STAFF_ID, ");
		var1.append("               TPD.TRAINING_DATE ");
		var1.append("        FROM   TRAINING_PLAN_DETAIL TPD, ");
		var1.append("               STAFF, ");
		var1.append("               CHANNEL_TYPE CH ");
		var1.append("        WHERE  DATE(TPD.TRAINING_DATE) >= DATE('" + date_now + "', 'localtime','start of month') ");
		var1.append("               AND TPD.AREA_MANAGER_ID = ? ");
		param.add("" + staffId);
		var1.append("               AND TPD.STATUS = 1 ");
		var1.append("               AND TPD.STAFF_ID = STAFF.STAFF_ID ");
		var1.append("               AND STAFF.STATUS = 1 ");
		var1.append("               AND STAFF.STAFF_TYPE_ID = CH.CHANNEL_TYPE_ID ");
		var1.append("               AND CH.TYPE = 2 ");
		var1.append("               AND CH.OBJECT_TYPE IN ( 1, 2 )) TPDETAIL ");
		var1.append("       JOIN (SELECT TRAINING_PLAN.TRAINING_PLAN_ID, ");
		var1.append("                    TRAINING_PLAN.STAFF_ID AS GSNPP_STAFF_ID, ");
		var1.append("                    STAFF.STAFF_CODE       AS GSNPP_STAFF_CODE, ");
		var1.append("                    STAFF.STAFF_NAME       AS GSNPP_STAFF_NAME ");
		var1.append("             FROM   TRAINING_PLAN, ");
		var1.append("                    STAFF, ");
		var1.append("                    CHANNEL_TYPE CH ");
		var1.append("             WHERE  TRAINING_PLAN.STAFF_ID = STAFF.STAFF_ID ");
		var1.append("                    AND STAFF.STATUS = 1 ");
		var1.append("                    AND STAFF.STAFF_TYPE_ID = CH.CHANNEL_TYPE_ID ");
		var1.append("                    AND CH.TYPE = 2 ");
		var1.append("                    AND CH.OBJECT_TYPE = 5 ");
		var1.append("                    AND TRAINING_PLAN.STATUS = 1 ");
		var1.append("                    AND DATE(TRAINING_PLAN.MONTH) >= DATE('" + date_now
				+ "', 'localtime','start of month'))TPLAN ");
		var1.append("         ON TPDETAIL.TRAINING_PLAN_ID = TPLAN.TRAINING_PLAN_ID ");

		Cursor c = null;
		try {
			c = this.rawQueries(var1.toString(), param);
			if (c != null && c.moveToFirst()) {
				do {
					TBHVTrainingPlanItem i = dto.newTBHVGsnppTrainingPlanItem();
					i.initFromCursor(c);
					dto.tbhvTrainingPlan.put(i.dateString, i);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
		} finally {
			if (c != null) {
				c.close();
			}
		}

		for (int i = 0; i < dto.spinnerStaffList.arrList.size(); i++) {
			if (dto.spinnerStaffList.arrList.get(i).nvbhShopId != null) {
				GSNPPTrainingPlanDTO trainingPlan = getGsnppTrainingPlan(
						Integer.valueOf(dto.spinnerStaffList.arrList.get(i).id), dto.spinnerStaffList.arrList.get(i).nvbhShopId);
				if(trainingPlan.listResult.size() > 0) {
					dto.trainingPlanOfGsnppDto = trainingPlan;
					dto.spinnerItemSelected = i;
					break;	
				}
				
			}
		}
		return dto;
	}

	/**
	 * getHistoryPlanTraining
	 * 
	 * @author: TamPQ
	 * @return: TBHVHistoryPlanTrainingDTO
	 * @throws:
	 */
	public TBHVTrainingPlanHistoryAccDTO getPlanTrainingHistoryAcc(long staffId, long gsnppStaffId, String shopId) {
		TBHVTrainingPlanHistoryAccDTO dto = new TBHVTrainingPlanHistoryAccDTO();
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);
		ArrayList<String> param = new ArrayList<String>();
		StringBuffer var1 = new StringBuffer();
		var1.append("SELECT PLAN.*, ");
		var1.append("       SHOP.SHOP_CODE AS NVBH_SHOP_CODE, ");
		var1.append("       SHOP.SHOP_NAME AS NVBH_SHOP_NAME ");
		var1.append("FROM   ((SELECT tpd.*, ");
		var1.append("               STAFF.STAFF_CODE AS NVBH_STAFF_CODE, ");
		var1.append("               STAFF.STAFF_NAME AS NVBH_STAFF_NAME ");
		var1.append("        FROM   TRAINING_PLAN_DETAIL tpd, ");
		var1.append("               STAFF, ");
		var1.append("               CHANNEL_TYPE ch ");
		var1.append("        WHERE  DATE(tpd.TRAINING_DATE) >= DATE('" + date_now + "','start of month') ");
		 var1.append("               AND DATE(tpd.TRAINING_DATE) <= DATE(?) ");
		 param.add(date_now);

		if (gsnppStaffId == 0) {
			var1.append("               AND tpd.AREA_MANAGER_ID = ? ");
			param.add("" + staffId);
		}

		var1.append("               AND tpd.STATUS IN ( 0,1 ) ");
		var1.append("               AND tpd.STAFF_ID = STAFF.STAFF_ID ");
		var1.append("               AND STAFF.STATUS = 1 ");
		var1.append("               AND STAFF.STAFF_TYPE_ID = ch.CHANNEL_TYPE_ID ");
		var1.append("               AND ch.OBJECT_TYPE IN (1,2) ");
		var1.append("               AND ch.TYPE = 2) tpdetail ");
		var1.append("        JOIN (SELECT TRAINING_PLAN.TRAINING_PLAN_ID AS TRAINING_PLAN_ID, ");
		var1.append("                     TRAINING_PLAN.STAFF_ID         AS GSNPP_STAFF_ID, ");
		var1.append("                     STAFF.STAFF_CODE               AS GSNPP_STAFF_CODE, ");
		var1.append("                     STAFF.STAFF_NAME               AS GSNPP_STAFF_NAME ");
		var1.append("              FROM   TRAINING_PLAN, ");
		var1.append("                     STAFF, ");
		var1.append("                     CHANNEL_TYPE ch ");
		var1.append("              WHERE  TRAINING_PLAN.STAFF_ID = STAFF.STAFF_ID ");
		var1.append("                     AND STAFF.STATUS = 1 ");
		var1.append("                     AND TRAINING_PLAN.STATUS = 1 ");
		var1.append("                     AND DATE(TRAINING_PLAN.MONTH) >= ");
		var1.append("                         DATE('" + date_now + "','start of month') ");
		var1.append("                     AND STAFF.STAFF_TYPE_ID = ch.CHANNEL_TYPE_ID ");
		var1.append("                     AND ch.TYPE = 2 ");
		var1.append("                     AND ch.OBJECT_TYPE = 5)tplan ");
		var1.append("          ON tpdetail.TRAINING_PLAN_ID = tplan.TRAINING_PLAN_ID) PLAN, ");
		var1.append("       SHOP ");
		var1.append("WHERE  PLAN.SHOP_ID = SHOP.SHOP_ID ");
		if (!StringUtil.isNullOrEmpty(shopId)) {
			var1.append("       AND PLAN.SHOP_ID = ? ");
			param.add(shopId);
		}

		if (gsnppStaffId != 0) {
			var1.append("			    AND GSNPP_STAFF_ID = ? ");
			param.add("" + gsnppStaffId);
		}

		var1.append("ORDER  BY TRAINING_DATE ASC ");

		Cursor c = null;
		try {
			c = this.rawQueries(var1.toString(), param);

			if (c != null && c.moveToFirst()) {
				do {
					TBHVHistoryPlanTrainingItem i = dto.newTBHVHistoryPlanTrainingItem();
					i.initFromCursor(c, dto);
				} while (c.moveToNext());
			}

		} catch (Exception ex) {
		} finally {
			if (c != null) {
				c.close();

			}
		}

		return dto;
	}

}
