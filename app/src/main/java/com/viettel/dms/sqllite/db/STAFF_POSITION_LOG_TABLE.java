/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.ShopParamDTO;
import com.viettel.dms.dto.db.StaffPositionLogDTO;
import com.viettel.dms.dto.db.WorkLogDTO;
import com.viettel.dms.dto.view.TBHVAttendanceDTO;
import com.viettel.dms.dto.view.TBHVAttendanceDTO.NvbhLog;
import com.viettel.dms.dto.view.TBHVAttendanceDTO.TVBHAttendanceItem;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.map.dto.GeomDTO;
import com.viettel.map.dto.LatLng;
import com.viettel.utils.VTLog;

/**
 * Table ghi vi tri NVBH
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class STAFF_POSITION_LOG_TABLE extends ABSTRACT_TABLE {
	// id
	public static final String STAFF_POSITION_ID = "STAFF_POSITION_LOG_ID";
	// staff_id
	public static final String STAFF_ID = "STAFF_ID";
	// toa do lat
	public static final String LAT = "LAT";
	// toa do long
	public static final String LNG = "LNG";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// do chinh xac
	public static final String ACCURACY = "ACCURACY";
	// STATUS
	public static final String STATUS = "STATUS";

	public static final String STAFF_POSITION_LOG_TABLE = "STAFF_POSITION_LOG";

	public STAFF_POSITION_LOG_TABLE(SQLiteDatabase mDB) {
		this.tableName = STAFF_POSITION_LOG_TABLE;
		this.columns = new String[] { STAFF_POSITION_ID, STAFF_ID, LAT, LNG, CREATE_DATE, STATUS };
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
	 * lay thong tin bao cao cham cong ngay cua TBHV
	 * 
	 * @param data
	 * @return
	 * @return: ArrayList<ReportTakeAttendOfDateDTO>
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 22, 2013
	 */
	public TBHVAttendanceDTO getTBHVAttendance(Bundle data) {
		TBHVAttendanceDTO dto = new TBHVAttendanceDTO();
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);
		String staffID= data.getString(IntentConstants.INTENT_STAFF_ID);
		ArrayList<String> params = new ArrayList<String>();
		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
		ArrayList<String> shopList = shopTable.getShopRecursiveReverse(shopId);
		String shopStr = TextUtils.join(",", shopList);

		StringBuffer shopParamSql = new StringBuffer();
		shopParamSql.append("SELECT * FROM SHOP_PARAM WHERE SHOP_ID = ? AND TYPE LIKE '%CC%' AND STATUS = 1");
		Cursor cScore = null;
		try {
			cScore = rawQuery(shopParamSql.toString(), new String[] { String.valueOf(shopId) });
			if (cScore != null) {
				if (cScore.moveToFirst()) {
					do {
						if (dto != null) {
							ShopParamDTO item = new ShopParamDTO();
							item.initObjectWithCursor(cScore);
							dto.listParam.put(item.type, item);
						}
					} while (cScore.moveToNext());
				}
			}
		} catch (Exception e) {
		} finally {
			if (cScore != null) {
				cScore.close();
			}
		}

		StringBuffer var1 = new StringBuffer();
		var1.append("SELECT GS.STAFF_ID          AS GS_STAFF_ID, ");
		var1.append("       GS.STAFF_CODE        AS GS_STAFF_CODE, ");
		var1.append("       GS.STAFF_NAME        AS GS_STAFF_NAME, ");
		var1.append("       SH.SHOP_ID			 AS NVBH_SHOP_ID, ");
		var1.append("       SH.SHOP_CODE		 AS NVBH_SHOP_CODE, ");
		var1.append("		SH.LAT AS LAT, ");
		var1.append("		SH.LNG AS LNG, ");
		var1.append("       COUNT(NVBH.STAFF_ID) AS NUM_NVBH ");
		var1.append("FROM   STAFF NVBH, ");
		var1.append("       CHANNEL_TYPE CH, ");
		var1.append("       SHOP SH, ");
		var1.append("       STAFF GS, ");
		var1.append("       CHANNEL_TYPE GS_CH ");
		var1.append("WHERE  NVBH.STAFF_TYPE_ID = CH.CHANNEL_TYPE_ID ");
		var1.append("       AND CH.TYPE = 2 ");
		var1.append("       AND CH.OBJECT_TYPE IN ( 1, 2 ) ");
		var1.append("       AND NVBH.SHOP_ID = SH.SHOP_ID ");
		var1.append("       AND NVBH.STATUS = 1 ");
		var1.append("       AND SH.STATUS = 1 ");
		var1.append("       AND NVBH.SHOP_ID IN ( " + shopStr + " ) ");
		var1.append("       AND NVBH.STAFF_OWNER_ID IS NOT NULL ");
		var1.append("		AND GS.STAFF_ID in(	");
		var1.append("	    	SELECT	");
		var1.append("	        	sgd.STAFF_ID	");
		var1.append("	    	FROM	");
		var1.append("	        	staff_group_detail sgd	");
		var1.append("	    	WHERE	");
		var1.append("	        	sgd.STAFF_GROUP_ID IN       (	");
		var1.append("	            	SELECT	");
		var1.append("	                	sg1.staff_group_id	");
		var1.append("	            	FROM	");
		var1.append("	                	staff_group sg1	");
		var1.append("	            	WHERE	");
		var1.append("	                	sg1.STAFF_ID = ?	");
		var1.append("	                	AND sg1.status = 1	");
		var1.append("	                	AND sg1.GROUP_LEVEL = 3	");
		var1.append("	                	AND sg1.GROUP_TYPE = 4	");
		var1.append("	        ))	");
//		var1.append("       AND GS.STAFF_OWNER_ID = ? ");
		String[] strParams= new String[]{staffID};
		var1.append("       AND NVBH.STAFF_OWNER_ID = GS.STAFF_ID ");
		var1.append("       AND GS.STAFF_TYPE_ID = GS_CH.CHANNEL_TYPE_ID ");
		var1.append("       AND GS_CH.TYPE = 2 ");
		var1.append("       AND GS_CH.OBJECT_TYPE = 5 ");
		var1.append("       AND GS.STATUS = 1 ");
		var1.append("GROUP  BY SH.SHOP_ID, ");
		var1.append("          NVBH.STAFF_OWNER_ID ");
		var1.append("ORDER  BY SH.SHOP_CODE, ");
		var1.append("          GS.STAFF_NAME ");

		Cursor c = null;
		try {
			c = rawQuery(var1.toString(), strParams);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						TVBHAttendanceItem item = dto.newTVBHAttendanceItem();
						item.initObjectWithCursor(c);
						dto.arrGsnppList.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception e2) {
				}
			}
		}
		
		if(dto.listParam!=null && dto.listParam.size()>=2){
		
			String CC_START = dto.listParam.get("CC_START").code;
			String CC_END = dto.listParam.get("CC_END").code;
			CC_START=DateUtils.convertDateOneFromFormatToAnotherFormat(CC_START, DateUtils.DATE_FORMAT_HOUR_MINUTE, DateUtils.DATE_FORMAT_HOUR_MINUTE);
			CC_END=DateUtils.convertDateOneFromFormatToAnotherFormat(CC_END, DateUtils.DATE_FORMAT_HOUR_MINUTE, DateUtils.DATE_FORMAT_HOUR_MINUTE);
			
			// String CC_DISTANCE = dto.listParam.get("CC_DISTANCE").code;
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT STAFF_ID, ");
			sql.append("       NPP_STAFF_ID, ");
			sql.append("       SHOP_ID, ");
			sql.append("       SHOP_CODE, ");
			sql.append("       SHOP_LAT, ");
			sql.append("       SHOP_LNG ");
//			sql.append("       ,GROUP_CONCAT(CASE ");
//			sql.append("                      WHEN TIME(STRFTIME('%H:%M', CREATE_DATE)) >= ");
//			sql.append("                           TIME(STRFTIME('%H:%M', ?)) ");
//			params.add(CC_START);
//			sql.append("                           AND TIME(STRFTIME('%H:%M', CREATE_DATE)) <= TIME( ");
//			sql.append("                               STRFTIME('%H:%M', ?)) THEN LAT ");
//			params.add(CC_END);
//			sql.append("                    END) AS LAT_LIST, ");
//			sql.append("       GROUP_CONCAT(CASE ");
//			sql.append("                      WHEN TIME(STRFTIME('%H:%M', CREATE_DATE)) >= ");
//			sql.append("                           TIME(STRFTIME('%H:%M', ?)) ");
//			params.add(CC_START);
//			sql.append("                           AND TIME(STRFTIME('%H:%M', CREATE_DATE)) <= TIME( ");
//			sql.append("                               STRFTIME('%H:%M', ?)) THEN LNG ");
//			params.add(CC_END);
//			sql.append("                    END) AS LNG_LIST, ");
//			sql.append("       GROUP_CONCAT(CASE ");
//			sql.append("                      WHEN TIME(STRFTIME('%H:%M', CREATE_DATE)) >= ");
//			sql.append("                           TIME(STRFTIME('%H:%M', ?)) ");
//			params.add(CC_END);
//			sql.append("                           AND TIME(STRFTIME('%H:%M', CREATE_DATE)) <= ");
//			sql.append("                               TIME(STRFTIME('%H:%M', ?)) THEN LAT ");
//			params.add(date_now);
//			sql.append("                    END) AS LAT_LIST_815, ");
//			sql.append("       GROUP_CONCAT(CASE ");
//			sql.append("                      WHEN TIME(STRFTIME('%H:%M', CREATE_DATE)) >= ");
//			sql.append("                           TIME(STRFTIME('%H:%M', ?)) ");
//			params.add(CC_END);
//			sql.append("                           AND TIME(STRFTIME('%H:%M', CREATE_DATE)) <= ");
//			sql.append("                               TIME(STRFTIME('%H:%M', ?)) THEN LNG ");
//			params.add(date_now);
//			sql.append("                    END) AS LNG_LIST_815 ");
			sql.append("FROM   (SELECT ST.STAFF_ID, ");
			sql.append("               (CASE WHEN CH.OBJECT_TYPE = 5 THEN ST.staff_id ");//gs
			sql.append("                  ELSE ST.staff_owner_id  END ) AS NPP_STAFF_ID, ");//nvbh
			sql.append("               SHOP.SHOP_ID, ");
			sql.append("               SHOP.SHOP_CODE, ");
			sql.append("               SHOP.LAT          AS SHOP_LAT, ");
			sql.append("               SHOP.LNG          AS SHOP_LNG, ");
			sql.append("               LOG.CREATE_DATE AS CREATE_DATE, ");
			sql.append("               LOG.LAT AS LAT, ");
			sql.append("               LOG.LNG AS LNG");
			sql.append("        FROM   STAFF ST, ");
			sql.append("               STAFF_POSITION_LOG LOG, ");
			sql.append("               SHOP, ");
			sql.append("               CHANNEL_TYPE CH ");
			sql.append("        WHERE  ST.STATUS = 1 ");
			sql.append("               AND ST.STAFF_ID = LOG.STAFF_ID ");
			sql.append("               AND DATE(LOG.CREATE_DATE) = DATE(?) ");
			params.add(date_now);
			sql.append("               AND ST.STAFF_TYPE_ID = CH.CHANNEL_TYPE_ID ");
			sql.append("               AND CH.OBJECT_TYPE IN ( 1, 2, 5) ");
			sql.append("               AND CH.TYPE = 2 ");
			sql.append("               AND ST.SHOP_ID = SHOP.SHOP_ID) NVBH_LOG ");
			sql.append("GROUP  BY STAFF_ID ");
	
			Cursor c2 = null;
			try {
				c2 = rawQueries(sql.toString(), params);
				if (c2 != null) {
					if (c2.moveToFirst()) {
						do {
							NvbhLog item = dto.newNvbhLog();
							item.initCursor(c2, "");
							dto.arrNvbhList.add(item);
						} while (c2.moveToNext());
					}
				}
			} catch (Exception e) {
				try {
					throw e;
				} catch (Exception e1) {
					VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e1));
				}
			} finally {
				if (c2 != null) {
					try {
						c2.close();
					} catch (Exception e2) {
					}
				}
			}
		}
		ArrayList<NvbhLog> arrNvbhList = new ArrayList<NvbhLog>();
		if(dto.arrNvbhList.size() > 0) {
			for (NvbhLog nvbh : dto.arrNvbhList) {
				SHOP_PARAM_TABLE shopParamTable = new SHOP_PARAM_TABLE(mDB);
				List<ShopParamDTO> listShopParamDTO = shopParamTable.getListParamForTakeAttendance("" + nvbh.nvbhShopId);
				String ccStart = "";
				String ccEnd = "";
				String ccDistance = "";
				if (listShopParamDTO.size() >= 3) {
					ccStart = listShopParamDTO.get(0).code;
					ccEnd = listShopParamDTO.get(1).code;
					ccDistance = listShopParamDTO.get(2).code;
				}
				StringBuffer sql = new StringBuffer();
				ArrayList<String> paramNVBH = new ArrayList<String>();
				sql.append("SELECT STAFF_ID, ");
				sql.append("       NPP_STAFF_ID, ");
				sql.append("       SHOP_ID, ");
				sql.append("       SHOP_CODE, ");
				sql.append("       SHOP_LAT, ");
				sql.append("       SHOP_LNG, ");
				sql.append("       GROUP_CONCAT(CASE ");
				sql.append("                      WHEN TIME(STRFTIME('%H:%M', CREATE_DATE)) >= ");
				sql.append("                           TIME(STRFTIME('%H:%M', ?)) ");
				paramNVBH.add(ccStart);
				sql.append("                           AND TIME(STRFTIME('%H:%M', CREATE_DATE)) <= TIME( ");
				sql.append("                               STRFTIME('%H:%M', ?)) THEN LAT ");
				paramNVBH.add(ccEnd);
				sql.append("                    END) AS LAT_LIST, ");
				sql.append("       GROUP_CONCAT(CASE ");
				sql.append("                      WHEN TIME(STRFTIME('%H:%M', CREATE_DATE)) >= ");
				sql.append("                           TIME(STRFTIME('%H:%M', ?)) ");
				paramNVBH.add(ccStart);
				sql.append("                           AND TIME(STRFTIME('%H:%M', CREATE_DATE)) <= TIME( ");
				sql.append("                               STRFTIME('%H:%M', ?)) THEN LNG ");
				paramNVBH.add(ccEnd);
				sql.append("                    END) AS LNG_LIST, ");
				sql.append("       GROUP_CONCAT(CASE ");
				sql.append("                      WHEN TIME(STRFTIME('%H:%M', CREATE_DATE)) >= ");
				sql.append("                           TIME(STRFTIME('%H:%M', ?)) ");
				paramNVBH.add(ccEnd);
				sql.append("                           AND TIME(STRFTIME('%H:%M', CREATE_DATE)) <= ");
				sql.append("                               TIME(STRFTIME('%H:%M', ?)) THEN LAT ");
				paramNVBH.add(date_now);
				sql.append("                    END) AS LAT_LIST_815, ");
				sql.append("       GROUP_CONCAT(CASE ");
				sql.append("                      WHEN TIME(STRFTIME('%H:%M', CREATE_DATE)) >= ");
				sql.append("                           TIME(STRFTIME('%H:%M', ?)) ");
				paramNVBH.add(ccEnd);
				sql.append("                           AND TIME(STRFTIME('%H:%M', CREATE_DATE)) <= ");
				sql.append("                               TIME(STRFTIME('%H:%M', ?)) THEN LNG ");
				paramNVBH.add(date_now);
				sql.append("                    END) AS LNG_LIST_815 ");
				sql.append("FROM   (SELECT ST.STAFF_ID, ");
				sql.append("               (CASE WHEN CH.OBJECT_TYPE = 5 THEN ST.staff_id ");//gs
				sql.append("                  ELSE ST.staff_owner_id  END ) AS NPP_STAFF_ID, ");//nvbh
				sql.append("               SHOP.SHOP_ID, ");
				sql.append("               SHOP.SHOP_CODE, ");
				sql.append("               SHOP.LAT          AS SHOP_LAT, ");
				sql.append("               SHOP.LNG          AS SHOP_LNG, ");
				sql.append("               LOG.CREATE_DATE AS CREATE_DATE, ");
				sql.append("               LOG.LAT AS LAT, ");
				sql.append("               LOG.LNG AS LNG");
				sql.append("        FROM   STAFF ST, ");
				sql.append("               STAFF_POSITION_LOG LOG, ");
				sql.append("               SHOP, ");
				sql.append("               CHANNEL_TYPE CH ");
				sql.append("        WHERE  ST.STATUS = 1 ");
				sql.append("               AND ST.STAFF_ID = LOG.STAFF_ID ");
				sql.append("               AND DATE(LOG.CREATE_DATE) = DATE(?) ");
				paramNVBH.add(date_now);
				sql.append("               AND ST.STAFF_TYPE_ID = CH.CHANNEL_TYPE_ID ");
				sql.append("               AND CH.OBJECT_TYPE IN ( 1, 2, 5) ");
				sql.append("               AND CH.TYPE = 2 ");
				sql.append("               AND LOG.STAFF_ID = ? ");
				paramNVBH.add("" + nvbh.nvbhStaffId);
				sql.append("               AND ST.SHOP_ID = SHOP.SHOP_ID) NVBH_LOG ");
				sql.append("GROUP  BY STAFF_ID ");

				Cursor c2 = null;
				try {
					c2 = rawQueries(sql.toString(), paramNVBH);
					if (c2 != null) {
						if (c2.moveToFirst()) {
							do {
								NvbhLog item = dto.newNvbhLog();
								item.initCursor(c2, ccDistance);
								nvbh.latLngList = item.latLngList;
								nvbh.latLngList815 = item.latLngList815;
								nvbh.isValidArrival = item.isValidArrival;
								nvbh.isLateArrival = item.isLateArrival;
								arrNvbhList.add(nvbh);
							} while (c2.moveToNext());
						}
					}
				} catch (Exception e) {
					try {
						throw e;
					} catch (Exception e1) {
						VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e1));
					}
				} finally {
					if (c2 != null) {
						try {
							c2.close();
						} catch (Exception e2) {
						}
					}
				}
			}
			dto.arrNvbhList.clear();
			dto.arrNvbhList = new ArrayList<>();
			dto.arrNvbhList.addAll(arrNvbhList);
		}

		return dto;
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @param staffId
	 * @return
	 * @return: StaffDTOvoid
	 * @throws:
	 */
	public GeomDTO getPosition(long staffId) {
		GeomDTO geom = new GeomDTO();
		StringBuffer sql = new StringBuffer();
		ArrayList<String> paArrayList = new ArrayList<String>();
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);
		sql.append("SELECT STAFF_ID, LAT, LNG, MAX(CREATE_DATE) ");
		sql.append(" 	FROM STAFF_POSITION_LOG ");
		sql.append("	WHERE STAFF_ID = ?  ");
		paArrayList.add("" + staffId);
		sql.append("		AND DATE(CREATE_DATE) = DATE(?) ");
		sql.append("	GROUP BY STAFF_ID ");
		paArrayList.add(date_now);

		Cursor c = null;
		try {
			c = rawQueries(sql.toString(), paArrayList);
			if (c != null) {
				if (c.moveToFirst()) {
					geom.lat = c.getDouble(c.getColumnIndex("LAT"));
					geom.lng = c.getDouble(c.getColumnIndex("LNG"));
				}
			}
		} catch (Exception e) {
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception e2) {
				}
			}
		}
		return geom;
	}
	
	/**
	 * delete old staff_position_log
	 * @author: yennth16
	 * @since: 10:25:58 15-04-2015
	 * @return: long
	 * @throws:  
	 * @return
	 */
	public long deleteOldPositionLog(){
		long result = -1;
		try {
			//mDB.beginTransaction();
			String dayBefore = DateUtils.getDateOfNumberPreviousDateWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT, -1);
			StringBuffer sqlDel = new StringBuffer();
			sqlDel.append("  substr(create_date,0,11) < ? ");
			String[] params = {dayBefore};
			delete(sqlDel.toString(), params);
			result = 1;
			//mDB.setTransactionSuccessful();
		} catch (Throwable e) {
			e.printStackTrace();
			result = -1;
		} finally {
//			if (mDB != null && mDB.inTransaction()) {
//				mDB.endTransaction();
//			}
			SQLUtils.getInstance().isProcessingTrans = false;
		}
		return result;
	}

	/**
	 * Lay log position gan day nhat dong bo len server
	 * @author: 
	 * @return
	 */
	public StaffPositionLogDTO getLastPosition(){
		StaffPositionLogDTO lastLogPosition = null;
		String dateNow= DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		String[] params = {dateNow, GlobalInfo.getInstance().getProfile().getUserData().id + ""};
		StringBuffer  sql = new StringBuffer();
		sql.append("SELECT STAFF_POSITION_LOG_ID 			AS STAFF_POSITION_LOG_ID, "); 
		sql.append("       STAFF_ID 						AS STAFF_ID, "); 
		sql.append("       STRFTIME('%H:%M', CREATE_DATE) 	AS CREATE_DATE, ");  
		sql.append("       LAT 								AS LAT, "); 
		sql.append("       LNG 								AS LNG, "); 
		sql.append("       ACCURACY 						AS ACCURACY "); 
		sql.append("FROM   STAFF_POSITION_LOG ");
		sql.append("WHERE substr(CREATE_DATE,0,11) >= ? ");
		sql.append("AND STAFF_ID = ? ");
		sql.append("ORDER BY CREATE_DATE DESC ");
		sql.append("LIMIT 1");
		
		Cursor c = null;
		try {
			c = rawQuery(sql.toString(), params);
			if (c != null) {
				if (c.moveToFirst()) {
					lastLogPosition = (new StaffPositionLogDTO()).initFromCursor(c);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
		return lastLogPosition;
	}

	/**
	 * Lay log position gan day nhat dong bo len server
	 * @author:
	 * @return
	 */
	public StaffPositionLogDTO getAttendancePosition(){
		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
		SHOP_PARAM_TABLE apParamTable = new SHOP_PARAM_TABLE(mDB);
		StaffPositionLogDTO lastLogPosition = null;
		List<ShopParamDTO> listShopParamDTO = apParamTable.getListParamForTakeAttendance(GlobalInfo.getInstance().getProfile().getUserData().shopId);
		String dateNow= DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		String ccStart = "";
		String ccEnd = "";
		String ccDistance = "";
		if(listShopParamDTO.size() >= 3) {
			ccStart = dateNow + ' ' + listShopParamDTO.get(0).code;
			ccEnd = dateNow + ' ' + listShopParamDTO.get(1).code;
			ccDistance = listShopParamDTO.get(2).code;
		}
		String[] params = {ccStart, ccEnd, GlobalInfo.getInstance().getProfile().getUserData().id + ""};
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT STAFF_POSITION_LOG_ID 			AS STAFF_POSITION_LOG_ID, ");
		sql.append("       STAFF_ID 						AS STAFF_ID, ");
		sql.append("       STRFTIME('%H:%M', CREATE_DATE) 	AS CREATE_DATE, ");
		sql.append("       CREATE_DATE 						AS CREATE_DATE_TEMP, ");
		sql.append("       LAT 								AS LAT, ");
		sql.append("       LNG 								AS LNG, ");
		sql.append("       ACCURACY 						AS ACCURACY ");
		sql.append("FROM   STAFF_POSITION_LOG ");
		sql.append("WHERE CREATE_DATE >= ?  ");
		sql.append("AND CREATE_DATE <= ? ");
		sql.append("AND STAFF_ID = ? ");
		sql.append("ORDER BY CREATE_DATE ");

		Cursor c = null;
		LatLng shopPosition = shopTable.getPositionOfShop(GlobalInfo.getInstance().getProfile().getUserData().shopId);
//		ShopParamDTO shopParamDTO = apParamTable.getDistanceParamForTakeAttendance(GlobalInfo.getInstance().getProfile().getUserData().shopId);
//		if(shopParamDTO != null) {
			if (shopPosition.lat > 0 && shopPosition.lng > 0 && !StringUtil.isNullOrEmpty(ccDistance)) {
				try {
					c = rawQuery(sql.toString(), params);
					if (c != null) {
						if (c.moveToFirst()) {
							do {
								StaffPositionLogDTO staffPositionLogDTO = (new StaffPositionLogDTO()).initFromCursor(c);
								double cusDistance = -1;
								if (staffPositionLogDTO.lat > 0 && staffPositionLogDTO.lng > 0) {
									cusDistance = GlobalUtil.getDistanceBetween(new LatLng(staffPositionLogDTO.lat, staffPositionLogDTO.lng), shopPosition);
									if (Double.valueOf(ccDistance).compareTo(cusDistance) >= 0) {
										lastLogPosition = staffPositionLogDTO;
										lastLogPosition.workLogDTO = new WorkLogDTO();
										lastLogPosition.workLogDTO.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
										if (!StringUtil.isNullOrEmpty(GlobalInfo.getInstance().getProfile().getUserData().shopId)) {
											lastLogPosition.workLogDTO.shopId = Long.parseLong(GlobalInfo.getInstance().getProfile().getUserData().shopId);
										}
										lastLogPosition.workLogDTO.staffLat = staffPositionLogDTO.lat;
										lastLogPosition.workLogDTO.staffLng = staffPositionLogDTO.lng;
										lastLogPosition.workLogDTO.shopLat = shopPosition.lat;
										lastLogPosition.workLogDTO.shopLng = shopPosition.lng;
										lastLogPosition.workLogDTO.workDate = staffPositionLogDTO.createDateTemp;
										lastLogPosition.workLogDTO.createDate = DateUtils.now();
										lastLogPosition.workLogDTO.distance = cusDistance;

										break;
									}
								}
							} while (c.moveToNext());
						}
					}
				} catch (Exception e) {
					VTLog.printStackTrace(e);
				} finally {
					if (c != null) {
						try {
							c.close();
						} catch (Exception e2) {
							// TODO: handle exception
						}
					}
				}
			}
//		}
		return lastLogPosition;
	}
}
