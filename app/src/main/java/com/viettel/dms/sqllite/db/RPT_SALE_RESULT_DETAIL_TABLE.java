/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.view.GSNPPTrainingResultDayReportDTO;
import com.viettel.dms.dto.view.GSNPPTrainingResultDayReportDTO.GSNPPTrainingResultReportDayItem;
import com.viettel.dms.dto.view.TBHVTrainingPlanDayResultReportDTO;
import com.viettel.dms.dto.view.TBHVTrainingPlanDayResultReportDTO.TBHVTrainingPlanDayResultReportItem;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * RPT SALE RESULT DETAIL TABLE
 * RPT_SALE_RESULT_DETAIL_TABLE.java
 * @version: 1.0 
 * @since:  08:23:19 20 Jan 2014
 */
public class RPT_SALE_RESULT_DETAIL_TABLE extends ABSTRACT_TABLE {

	public RPT_SALE_RESULT_DETAIL_TABLE(SQLiteDatabase mDB) {
		this.mDB = mDB;
	}

	@Override
	protected long insert(AbstractTableDTO dto) {
		return 0;
	}

	@Override
	protected long update(AbstractTableDTO dto) {
		return 0;
	}

	@Override
	protected long delete(AbstractTableDTO dto) {
		return 0;
	}

	/**
	 * get Gsnpp Training Result Day Report
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public GSNPPTrainingResultDayReportDTO getGsnppTrainingResultDayReport(long staffTrainId, long shopId) {
		GSNPPTrainingResultDayReportDTO dto = new GSNPPTrainingResultDayReportDTO();
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);
		StringBuffer var1 = new StringBuffer();
		var1.append("SELECT * ");
		var1.append("FROM   (SELECT RPT_SALE_RESULT_DETAIL.*, ");
		var1.append("               CUSTOMER.CUSTOMER_CODE, ");
		var1.append("               CUSTOMER.CUSTOMER_ID, ");
		var1.append("               CUSTOMER.CUSTOMER_NAME, ");
		var1.append("               CUSTOMER.HOUSENUMBER, ");
		var1.append("               CUSTOMER.LAT, ");
		var1.append("               CUSTOMER.LNG, ");
		var1.append("               CUSTOMER.STREET, ");
		var1.append("               CUSTOMER.CHANNEL_TYPE_ID, ");
		var1.append("               RPT_SALE_RESULT_DETAIL.SCORE AS SCORE, ");
		var1.append("               TRAINING_PLAN_DETAIL.SCORE  AS TPD_SCORE");
		var1.append("        FROM   RPT_SALE_RESULT_DETAIL, ");
		var1.append("               CUSTOMER, ");
		var1.append("               TRAINING_PLAN_DETAIL ");
		var1.append("        WHERE  RPT_SALE_RESULT_DETAIL.TRAINING_PLAN_DETAIL_ID = ? ");
		var1.append("               AND RPT_SALE_RESULT_DETAIL.SHOP_ID = ? ");
		var1.append("               AND RPT_SALE_RESULT_DETAIL.TRAINING_PLAN_DETAIL_ID = TRAINING_PLAN_DETAIL.TRAINING_PLAN_DETAIL_ID ");
		var1.append("               AND TRAINING_PLAN_DETAIL.STATUS IN ( 0,1 ) ");
		var1.append("               AND RPT_SALE_RESULT_DETAIL.CUSTOMER_ID = CUSTOMER.CUSTOMER_ID ");
		var1.append("               AND CUSTOMER.STATUS = 1) CUS_LIST ");
		var1.append("       LEFT JOIN (SELECT ACTION_LOG.ACTION_LOG_ID   AS AL_ID, ");
		var1.append("                         ACTION_LOG.STAFF_ID        AS AL_STAFF_ID, ");
		var1.append("                         ACTION_LOG.CUSTOMER_ID     AS AL_CUSTOMER_ID_1, ");
		var1.append("                         ACTION_LOG.LAT             AS AL_LAT, ");
		var1.append("                         ACTION_LOG.LNG             AS AL_LNG, ");
		var1.append("                         MIN(ACTION_LOG.START_TIME) AS AL_START_TIME, ");
		var1.append("                         ACTION_LOG.END_TIME        AS AL_END_TIME ");
		var1.append("                  FROM   ACTION_LOG, ");
		var1.append("                         STAFF, ");
		var1.append("                         CHANNEL_TYPE ");
		var1.append("                  WHERE  ACTION_LOG.STAFF_ID = STAFF.STAFF_ID ");
		var1.append("                         AND STAFF.STAFF_TYPE_ID = CHANNEL_TYPE.CHANNEL_TYPE_ID ");
		var1.append("                         AND CHANNEL_TYPE.OBJECT_TYPE = 5 ");
		var1.append("                         AND CHANNEL_TYPE.TYPE = 2 ");
		var1.append("                         AND STAFF.STATUS = 1 ");
		var1.append("                         AND DATE(?) = DATE(ACTION_LOG.START_TIME) ");
		var1.append("                  GROUP  BY CUSTOMER_ID) AL ");
		var1.append("              ON CUS_LIST.CUSTOMER_ID = AL.AL_CUSTOMER_ID_1 ");
		var1.append("ORDER  BY AMOUNT_PLAN DESC, ");
		var1.append("          AMOUNT DESC ");

		Cursor c = null;
		try {
			c = this.rawQuery(var1.toString(), new String[] { String.valueOf(staffTrainId), "" + shopId, date_now });
			if (c != null && c.moveToFirst()) {
				int stt = 0;
				do {
					stt++;
					GSNPPTrainingResultReportDayItem i = dto.newStaffTrainResultReportItem();
					i.initFromCursor(c, dto, stt);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			if (c != null) {
				c.close();
			}
		}
		
		StringBuffer scoreSql = new StringBuffer();
		scoreSql.append("  SELECT * FROM TRAINING_PLAN_DETAIL WHERE TRAINING_PLAN_DETAIL_ID = ? ");
		Cursor cScore = null;
		try {
			cScore = rawQuery(scoreSql.toString(), new String[] { String.valueOf(staffTrainId) });
			if (cScore != null) {
				if (cScore.moveToFirst()) {
					if (dto != null) {
						dto.score = cScore.getDouble(cScore.getColumnIndex("SCORE"));
					}
				}
			}
		} catch (Exception e) {
		} finally {
			if (cScore != null) {
				cScore.close();
			}
		}
		
		Cursor cDistance = null;
		String getShopDistance = " select DISTANCE_TRAINING from SHOP where SHOP_ID = ?";
		try {
			cDistance = rawQuery(getShopDistance, new String[] { String.valueOf(shopId) });
			if (cDistance != null) {
				if (cDistance.moveToFirst()) {
					if (dto != null) {
						dto.distance = cDistance.getDouble(0);
					}
				}
			}
		} catch (Exception e) {
		} finally {
			if (cDistance != null) {
				cDistance.close();
			}
		}

		return dto;
	}

	/**
	 * get Day Training Supervision
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public TBHVTrainingPlanDayResultReportDTO getDayTrainingSupervision(long staffTrainId, long shopId) {
		TBHVTrainingPlanDayResultReportDTO dto = new TBHVTrainingPlanDayResultReportDTO();
		StringBuffer var1 = new StringBuffer();
		var1.append("SELECT RPT_SALE_RESULT_DETAIL.*, ");
		var1.append("       CUSTOMER.CUSTOMER_CODE, ");
		var1.append("       SUBSTR(CUSTOMER.CUSTOMER_CODE, 1, 3) AS CUSCODE, ");
		var1.append("       CUSTOMER.CUSTOMER_ID, ");
		var1.append("       CUSTOMER.CUSTOMER_NAME, ");
		var1.append("       CUSTOMER.HOUSENUMBER, ");
		var1.append("       CUSTOMER.LAT, ");
		var1.append("       CUSTOMER.LNG, ");
		var1.append("       CUSTOMER.STREET ");
		var1.append("FROM   RPT_SALE_RESULT_DETAIL, ");
		var1.append("       CUSTOMER, ");
		var1.append("       TRAINING_PLAN, ");
		var1.append("       TRAINING_PLAN_DETAIL ");
		var1.append("WHERE  TRAINING_PLAN_DETAIL.TRAINING_PLAN_DETAIL_ID = ? ");
		var1.append("       AND TRAINING_PLAN_DETAIL.TRAINING_PLAN_ID = ");
		var1.append("           TRAINING_PLAN.TRAINING_PLAN_ID ");
		var1.append("       AND TRAINING_PLAN.STATUS = 1 ");
		var1.append("       AND TRAINING_PLAN_DETAIL.STATUS IN ( 0,1 ) ");
		var1.append("       AND RPT_SALE_RESULT_DETAIL.CUSTOMER_ID = CUSTOMER.CUSTOMER_ID ");
		var1.append("       AND RPT_SALE_RESULT_DETAIL.TRAINING_PLAN_DETAIL_ID = ");
		var1.append("           TRAINING_PLAN_DETAIL.TRAINING_PLAN_DETAIL_ID ");
		var1.append("       AND CUSTOMER.STATUS = 1 ");
		var1.append("ORDER  BY CUSCODE ASC ");
		Cursor c = null;
		try {
			c = this.rawQuery(var1.toString(), new String[] { String.valueOf(staffTrainId) });
			if (c != null && c.moveToFirst()) {
				int stt = 0;
				do {
					stt++;
					TBHVTrainingPlanDayResultReportItem i = dto.newStaffTrainResultReportItem();
					i.initFromCursor(c, dto, stt);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
		} finally {
			if (c != null) {
				c.close();
			}
		}
		StringBuffer scoreSql = new StringBuffer();
		scoreSql.append("  SELECT * FROM TRAINING_PLAN_DETAIL WHERE TRAINING_PLAN_DETAIL_ID = ? ");
		Cursor cScore = null;
		try {
			cScore = rawQuery(scoreSql.toString(), new String[] { String.valueOf(staffTrainId) });
			if (cScore != null) {
				if (cScore.moveToFirst()) {
					if (dto != null) {
						dto.score = cScore.getDouble(cScore.getColumnIndex("SCORE"));
					}
				}
			}
		} catch (Exception e) {
		} finally {
			if (cScore != null) {
				cScore.close();
			}
		}
		return dto;
	}

}
