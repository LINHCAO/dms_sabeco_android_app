/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.view.CustomerAttendProgramListDTO;
import com.viettel.dms.dto.view.CustomerAttendProgramListItem;
import com.viettel.dms.dto.view.ListProductQuantityJoin;
import com.viettel.dms.dto.view.SaleSupportProgramDetailModel;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * Thong tin CT HTBH
 * 
 * @author: dungnt19
 * @version: 1.0
 * @since: 1.0
 */
public class PRO_INFO_TABLE extends ABSTRACT_TABLE {
	// id chuong trinh khuyen mai
	public static final String PRO_INFO_ID = "PRO_INFO_ID";
	// ma chuong trinh
	public static final String PRO_INFO_CODE = "PRO_INFO_CODE";
	// ten chuong trinh
	public static final String PRO_INFO_NAME = "PRO_INFO_NAME";
	// Loai chuong trinh: 1: HT01, 2: HT02, 3: HT03, 4: HT04, 5: HT05
	public static final String TYPE = "TYPE";
	// tu ngay
	public static final String FROM_DATE = "FROM_DATE";
	// den ngay
	public static final String TO_DATE = "TO_DATE";
	// Trang thai: 0: du thao; 1: hoat dong; 2: tam ngung
	public static final String STATUS = "STATUS";
	// Phan loai chuong trinh, map qua bang PRO_CATEGORY
	public static final String PRO_CATEGORY_ID = "PRO_CATEGORY_ID";
	// Loai khach hang: 1: NPP C1, 2: C2, 3: C3, 4: Quan, 5: Tiem tap hoa, 6:
	// Khac
	public static final String CUSTOMER_TYPE = "CUSTOMER_TYPE";
	// Id chuong trinh goc
	public static final String PARENT_PRO_INFO_ID = "PARENT_PRO_INFO_ID";
	// Loai nguon ngan sach: 1: Tong cong ty, 2: Thuong mai
	public static final String BUDGET_TYPE = "BUDGET_TYPE";
	// mo ta
	public static final String CONTENT = "CONTENT";
	// Loai chu ky: 1: Thang, 2: Quy, 3: Thoi gian CT, 4: Tuy chinh
	public static final String PERIOD_TYPE = "PERIOD_TYPE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// id NPP
	public static final String SHOP_ID = "SHOP_ID";

	private static final String TABLE_NAME = "PRO_INFO_TABLE";

	public PRO_INFO_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_NAME;
		this.columns = new String[] { PRO_INFO_ID, PRO_INFO_CODE,
				PRO_INFO_NAME, TYPE, FROM_DATE, TO_DATE, STATUS,
				PRO_CATEGORY_ID, CUSTOMER_TYPE, PARENT_PRO_INFO_ID,
				BUDGET_TYPE, CONTENT, PERIOD_TYPE, CREATE_USER, UPDATE_USER,
				CREATE_DATE, UPDATE_DATE, SHOP_ID, SYN_STATE };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.dms.sqllite.db.ABSTRACT_TABLE#insert(com.viettel.dms.dto.
	 * db.AbstractTableDTO)
	 */
	@Override
	protected long insert(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.dms.sqllite.db.ABSTRACT_TABLE#update(com.viettel.dms.dto.
	 * db.AbstractTableDTO)
	 */
	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.dms.sqllite.db.ABSTRACT_TABLE#delete(com.viettel.dms.dto.
	 * db.AbstractTableDTO)
	 */
	@Override
	protected long delete(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Lấy thông tin chi tiết chương trình HTBH
	 * 
	 * @author: hoanpd1
	 * @since: 07:45:08 25-07-2014
	 * @return: SaleSupportProgramDetailModel
	 * @throws:  
	 * @param programId
	 * @return
	 */
	public SaleSupportProgramDetailModel getSaleSupportProgramDetail(long programId) {
		SaleSupportProgramDetailModel model = new SaleSupportProgramDetailModel();

		ArrayList<String> params = new ArrayList<String>();
		StringBuffer  sql = new StringBuffer();
		sql.append("SELECT Group_concat(ct.type) CUSTOMER_TYPE, ");
		sql.append("       Group_concat(ct.customer_type_name) CUSTOMER_TYPE_NAME, ");
		sql.append("       PI.pro_info_id PRO_INFO_ID, ");
		sql.append("       PI.pro_info_code PRO_INFO_CODE, ");
		sql.append("       PI.pro_info_name PRO_INFO_NAME, ");
		sql.append("       strftime('%d/%m/%Y', PI.FROM_DATE) AS FROM_DATE, ");
		sql.append("       strftime('%d/%m/%Y', PI.TO_DATE) AS TO_DATE, ");
		sql.append("       PI.pro_category_id PRO_CATEGORY_ID, ");
		sql.append("       PI.budget_type BUDGET_TYPE, ");
		sql.append("       PI.content CONTENT ");
		sql.append("FROM   pro_info pi, ");
		sql.append("       (SELECT PIC.pro_info_id      PRO_INFO_ID, ");
		sql.append("               pic.type             type, ");
		sql.append("               ct.channel_type_name CUSTOMER_TYPE_NAME ");
		sql.append("        FROM   pro_info_cus pic, ");
		sql.append("               channel_type ct ");
		sql.append("        WHERE  1 = 1 ");
		sql.append("               AND pic.[type] = ct.[object_type] ");
		sql.append("               AND ct.[type] = 3 ");
		sql.append("               AND ct.[status] = 1 ");
		sql.append("               AND pic.[pro_info_id] = ? ");
		params.add(programId + "");
		sql.append("        ORDER  BY CUSTOMER_TYPE_NAME ) ct ");
		sql.append("WHERE  1 = 1 ");
		sql.append("       AND pi.[pro_info_id] = ct.pro_info_id ");
		sql.append("ORDER  BY ct.customer_type_name ASC");

		Cursor c = null;
		try {
			c = rawQueries(sql.toString(), params);
			if (c != null) {
				if (c.moveToFirst()) {
					long parentProCateId = 0 ;
					if(c.getColumnIndex("PRO_CATEGORY_ID") > -1){
						parentProCateId = c.getLong(c.getColumnIndex("PRO_CATEGORY_ID"));
					}
					String nameCategory = nameProCategory(parentProCateId);
					model.initDataFromCursor(c, nameCategory); 
				}
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}

		return model;
	}
	
	/**
	 * Lay ten loai chuong trinh trong man hinh thong tin chuong trinh
	 * 
	 * @author: hoanpd1
	 * @since: 07:44:28 25-07-2014
	 * @return: String
	 * @throws:  
	 * @param parentProCateId
	 * @return
	 */
	public String nameProCategory(long parentProCateId) {
		StringBuilder nameCategory = new StringBuilder();
		Cursor c = null;
		ArrayList<String> params = new ArrayList<String>();
		StringBuffer  sql = new StringBuffer();
		sql.append("SELECT pc.[pro_category_name], ");
		sql.append("       pc.pro_category_id, ");
		sql.append("       pc.parent_pro_category_id ");
		sql.append("FROM   pro_category pc ");
		sql.append("WHERE  1 = 1 ");
		sql.append("       AND pc.type IN ( 1, 2, 3 ) ");
		sql.append("       AND pc.status = 1 ");
		sql.append("       AND pc.[pro_category_id] =? ");
		params.add(""+parentProCateId);
		try {
			c = rawQueries(sql.toString(), params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						long parProCateId = c.getLong(c.getColumnIndex("PARENT_PRO_CATEGORY_ID"));
						String categoryname = c.getString(c.getColumnIndex("PRO_CATEGORY_NAME"));
						if (!StringUtil.isNullOrEmpty(categoryname) && !"null".equals(categoryname)) {
							if(parProCateId==0 ||"null".equals(parProCateId)){
								nameCategory.append(categoryname);
							}else {
								nameCategory.append( " - " +categoryname);
							}
						}
						String s = nameProCategory(parProCateId);
						nameCategory.insert(0, s);
						
					} while (c.moveToNext());
				}
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
		
		return nameCategory.toString();
	}
	

	/**
	 * @author: dungnt19
	 * @since: 1.0
	 * @return: int
	 * @throws:
	 * @param programId
	 * @return
	 */
	public int getSaleSupportProgramLevel(long programId) {
		int result = 0;

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT PS.TOTAL_LEVEL ");
		sql.append(" FROM	PRO_INFO PI, PRO_STRUCTURE PS ");
		sql.append(" WHERE 1 = 1 ");
		sql.append("       AND PI.PRO_INFO_ID = ? ");
		sql.append("       AND PI.STATUS = 1 ");
		sql.append("       AND PI.PRO_INFO_ID = PS.PRO_INFO_ID ");

		Cursor c = null;
		String[] params = { programId + "" };
		try {
			c = rawQuery(sql.toString(), params);
			if (c != null) {
				if (c.moveToFirst()) {
					String strTotalLevel = c.getString(c.getColumnIndex("TOTAL_LEVEL"));

					// Khong biet truong hop khong co muc thi se luu null hay
					// ghi 0
					// Nen kiem tra truong hop ghi la null
					// Neu khong co muc thi gia tri result = 0
					if (strTotalLevel == null
							|| "null".equals(strTotalLevel.toLowerCase())) {
						result = 0;
					} else {
						try {
							result = Integer.parseInt(strTotalLevel);
						} catch (Exception e) {
							result = 0;
						}
					}
				}
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}
		return result;
	}

	/**
	 * Danh sach khach hang tham gia CT HTBH
	 * 
	 * @author: hoanpd1
	 * @since: 16:33:45 28-07-2014
	 * @return: CustomerAttendProgramListDTO
	 * @throws:  
	 * @param bundle
	 * @return
	 */
	public CustomerAttendProgramListDTO getCustomerListAttendProgram(Bundle bundle) {
		CustomerAttendProgramListDTO result = new CustomerAttendProgramListDTO();
		// -- ma chuong trinh
		long proInfoId = bundle.getLong(IntentConstants.INTENT_ID);
		// -- trang thai
		String status = bundle.getString(IntentConstants.INTENT_STATUS);
		// -- muc
		String level = bundle.getString(IntentConstants.INTENT_LEVEL);
		// -- du lieu search
		String name = bundle.getString(IntentConstants.INTENT_NAME);
		// -- Co lay so trang khong?
		boolean isGetTotalPage = bundle.getBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE);
		// -- Lay page nao?
		int page = bundle.getInt(IntentConstants.INTENT_PAGE);
		String date_now = DateUtils .getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		
		long staffId = bundle.getLong(IntentConstants.INTENT_STAFF_ID);
		long shopId = bundle.getLong(IntentConstants.INTENT_SHOP_ID);
		StringBuilder totalPageSql = new StringBuilder();
		ArrayList<String> paramsObject = new ArrayList<String>();
		StringBuffer  sqlObject = new StringBuffer();
		sqlObject.append("SELECT CT.customer_id AS CUSTOMER_ID, ");
		sqlObject.append("       CT.short_code AS SHORT_CODE, ");
		sqlObject.append("       CT.customer_name AS CUSTOMER_NAME, ");
		sqlObject.append("       CT.housenumber AS HOUSENUMBER, ");
		sqlObject.append("       CT.street AS STREET, ");
		sqlObject.append("       CT.address AS ADDRESS1, ");
		sqlObject.append("       CT.name_text, ");
		sqlObject.append("       CT.phone, ");
		sqlObject.append("       CT.mobiphone, ");
		sqlObject.append("       A.area_name AS AREA_NAME, ");
		sqlObject.append("       PCM.level_number AS LEVEL_NUMBER, ");
		sqlObject.append("       LV.level_name AS LEVEL_NAME, ");
		sqlObject.append("       PCM.joining_date AS JOINING_DATE, ");
		sqlObject.append("       PCM.pro_cus_map_id AS PRO_CUS_MAP_ID, ");
		sqlObject.append("       PCH.type AS TYPE, ");
		sqlObject.append("       ( CASE ");
		sqlObject.append("           WHEN( (SELECT Count(*) ");
		sqlObject.append("                  FROM   pro_cus_map_detail PCMD ");
		sqlObject.append("                  WHERE  PCMD.pro_cus_map_id = PCM.pro_cus_map_id ");
		sqlObject.append("                         AND PCMD.quantity > 0) > 0 ) THEN 1 ");
		sqlObject.append("           ELSE 0 ");
		sqlObject.append("         end ) AS NUM_JOIN ");
		sqlObject.append("FROM   pro_cus_map PCM, ");
		sqlObject.append("       pro_cus_history PCH ");
		sqlObject.append("       LEFT JOIN (SELECT DISTINCT( psd.[level_name] ) LEVEL_NAME, ");
		sqlObject.append("                                 psd.level_number     LEVEL_NUMBER ");
		sqlObject.append("                  FROM   pro_structure ps, ");
		sqlObject.append("                         pro_structure_detail psd ");
		sqlObject.append("                  WHERE  ps.[pro_info_id] = ? ");
		paramsObject.add(""+proInfoId );
		sqlObject.append("                         AND ps.[pro_structure_id] = psd.[pro_structure_id] ");
		sqlObject.append("                  ORDER  BY psd.[level_number]) LV ");
		sqlObject.append("              ON PCM.[level_number] = LV.level_number, ");
		sqlObject.append("       area A, ");
		sqlObject.append("       (SELECT c.[customer_id], ");
		sqlObject.append("               c.customer_code, ");
		sqlObject.append("               c.short_code, ");
		sqlObject.append("               c.customer_name, ");
		sqlObject.append("               c.area_id, ");
		sqlObject.append("               C.housenumber, ");
		sqlObject.append("               C.street, ");
		sqlObject.append("               C.address, ");
		sqlObject.append("               C.name_text, ");
		sqlObject.append("               C.phone, ");
		sqlObject.append("               C.mobiphone ");
		sqlObject.append("        FROM   visit_plan vp, ");
		sqlObject.append("               customer c, ");
		sqlObject.append("               routing rt, ");
		sqlObject.append("               shop sh, "); 
		sqlObject.append("               (SELECT routing_id, ");
		sqlObject.append("                       customer_id  ");
		sqlObject.append("                FROM  routing_customer ");
		sqlObject.append("                WHERE ( substr(end_date,1,10) >= ? ");
		paramsObject.add(date_now);
		sqlObject.append("                        OR Date(end_date) IS NULL) ");
		sqlObject.append("                      AND substr(start_date,1,10) <= ? ");
		paramsObject.add(date_now);
		sqlObject.append("                      AND ( ( week1 IS NULL ");
		sqlObject.append("                          AND week2 IS NULL ");
		sqlObject.append("                          AND week3 IS NULL ");
		sqlObject.append("                          AND week4 IS NULL) ");
		sqlObject.append("                          OR ( ( ( Cast((Julianday(?) - Julianday(start_date)) /7 AS INTEGER)  ");
		paramsObject.add(date_now);
		sqlObject.append("                               + ( CASE WHEN ( ( Julianday(?) - Julianday(start_date)) % 7 > 0) ");
		paramsObject.add(date_now);
		sqlObject.append("                                   AND ( Cast((CASE WHEN Strftime('%w',?) = '0' THEN 7  ");
		paramsObject.add(date_now);
		sqlObject.append("                                                    ELSE Strftime('%w', ?) END) AS INTEGER)  ");
		paramsObject.add(date_now);
		sqlObject.append("                                       < Cast((CASE WHEN Strftime('%w', start_date) = '0' THEN 7  ");
		sqlObject.append("                                                    ELSE Strftime('%w',start_date) END) AS INTEGER)) THEN 1 ELSE 0 END) ");
		sqlObject.append("                             ) % 4 + 1) = 1 AND week1 = 1) ");
		sqlObject.append("                          OR ( ( ( Cast((Julianday(?) - Julianday(start_date)) / 7 AS INTEGER)  ");
		paramsObject.add(date_now);
		sqlObject.append("                               + ( CASE WHEN ( ( Julianday(?) - Julianday(start_date)) % 7 > 0) ");
		paramsObject.add(date_now);
		sqlObject.append("                                   AND ( Cast((CASE WHEN Strftime('%w',?) = '0' THEN 7  ");
		paramsObject.add(date_now);
		sqlObject.append("                                                    ELSE Strftime('%w',?) END) AS INTEGER)  ");
		paramsObject.add(date_now);
		sqlObject.append("                                       < Cast((CASE WHEN Strftime('%w', start_date) = '0' THEN 7  ");
		sqlObject.append("                                                    ELSE Strftime('%w', start_date) END) AS INTEGER)) THEN 1 ELSE 0 END) ");
		sqlObject.append("                             ) % 4 + 1) = 2 AND week2 = 1) ");
		sqlObject.append("                          OR ( ( ( Cast((Julianday(?) - Julianday(start_date)) / 7 AS INTEGER)  ");
		paramsObject.add(date_now);
		sqlObject.append("                               + ( CASE WHEN ( ( Julianday(?) - Julianday(start_date)) % 7 > 0) ");
		paramsObject.add(date_now);
		sqlObject.append("                                   AND ( Cast((CASE WHEN Strftime('%w',?) = '0' THEN 7  ");
		paramsObject.add(date_now);
		sqlObject.append("                                                    ELSE Strftime('%w',?) END) AS INTEGER)  ");
		paramsObject.add(date_now);
		sqlObject.append("                                       < Cast((CASE WHEN Strftime('%w',start_date) = '0' THEN 7  ");
		sqlObject.append("                                                    ELSE Strftime('%w', start_date) END) AS INTEGER)) THEN 1 ELSE 0 END) ");
		sqlObject.append("                             ) % 4 + 1) = 3 AND week3 = 1) ");
		sqlObject.append("                          OR ( ( ( Cast((Julianday(?) - Julianday(start_date)) / 7 AS INTEGER)  ");
		paramsObject.add(date_now);
		sqlObject.append("                               + ( CASE WHEN ( ( Julianday(?) - Julianday(start_date)) % 7 > 0) ");
		paramsObject.add(date_now);
		sqlObject.append("                                   AND ( Cast((CASE WHEN Strftime('%w', ?) = '0' THEN 7  ");
		paramsObject.add(date_now);
		sqlObject.append("                                                    ELSE Strftime('%w', ?) END) AS INTEGER)  ");
		paramsObject.add(date_now);
		sqlObject.append("                                       < Cast((CASE WHEN Strftime('%w', start_date) = '0' THEN 7  ");
		sqlObject.append("                                                    ELSE Strftime('%w', start_date) END) AS INTEGER)) THEN 1 ELSE 0 END)  ");    
		sqlObject.append("                             ) % 4 + 1) = 4 AND week4 = 1))) RC     "); 
		sqlObject.append("        WHERE  1 = 1 ");
//		sqlObject.append("               AND vp.status = 1 ");
		sqlObject.append("               AND c.status = 1 ");
		sqlObject.append("       	     AND substr(vp.FROM_DATE,1,10) <= ? ");
		paramsObject.add(date_now);
		sqlObject.append("               AND (substr(VP.TO_DATE,1,10) >= ? OR substr(VP.TO_DATE,1,10) IS NULL) ");
		paramsObject.add(date_now);
		sqlObject.append("               AND rt.status = 1 ");
		sqlObject.append("               AND vp.staff_id = ? ");
		paramsObject.add(""+staffId);
		sqlObject.append("               AND sh.shop_id = ? ");
		paramsObject.add(""+shopId);
		sqlObject.append("               AND sh.shop_id = c.shop_id ");
		sqlObject.append("               AND sh.shop_id = rt.shop_id ");
		sqlObject.append("               AND sh.shop_id = vp.shop_id ");
		sqlObject.append("               AND vp.routing_id = rt.routing_id ");
		sqlObject.append("               AND rt.routing_id = rc.routing_id ");
		sqlObject.append("               AND rc.customer_id = c.customer_id)CT ");
		sqlObject.append("WHERE  1 = 1 ");
		sqlObject.append("       AND PCM.pro_info_id = ? ");
		paramsObject.add(""+proInfoId );
		sqlObject.append("       AND PCM.object_type = 1 ");
		sqlObject.append("       AND PCM.object_id = CT.customer_id ");
		sqlObject.append("       AND PCM.pro_cus_map_id = PCH.pro_cus_map_id ");
		sqlObject.append("       AND A.area_id = CT.area_id ");
		sqlObject.append("       AND PCH.to_date IS NULL ");
		if (!"-1".equals(status)) {
			sqlObject.append("	      AND PCH.TYPE = ?	");
			paramsObject.add(status);
		}
		if (!"0".equals(level)) {
			sqlObject.append("	      AND PCM.LEVEL_NUMBER = ?	");
			paramsObject.add(level);
		}
		if (!StringUtil.isNullOrEmpty(name)) {
			name = StringUtil.getEngStringFromUnicodeString(name);
			name = StringUtil.escapeSqlString(name);
			name = DatabaseUtils.sqlEscapeString("%" + name + "%");
			name = name.substring(1, name.length()-1);
			// tim theo truong ten va dia chi
			sqlObject.append("	and (upper(CT.NAME_TEXT) like upper(?) escape '^' ");
			paramsObject.add(name);
			// tim theo dt co dinh
			sqlObject.append("	or upper(CT.PHONE) like upper(?) escape '^' ");
			paramsObject.add(name);
			// tim theo ma khach hang
			sqlObject.append("	or upper(CT.SHORT_CODE) like upper(?) escape '^' ");
			paramsObject.add(name);
			// tim theo so dtdd
			sqlObject.append("	or upper(CT.MOBIPHONE) like upper(?) escape '^' )");
			paramsObject.add(name);
		} 

		Cursor cTotalRow = null;
		if (isGetTotalPage) {
			totalPageSql.append("select count(*) as TOTAL_ROW from ("
					+ sqlObject + ")");
			try {
				cTotalRow = rawQueries(totalPageSql.toString(), paramsObject);
				if (cTotalRow != null) {
					if (cTotalRow.moveToFirst()) {
						result.totalCustomer = cTotalRow.getInt(0);
					}
				}
			} catch (Exception ex) {
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			} finally {
				try {
					if (cTotalRow != null) {
						cTotalRow.close();
					}
				} catch (Exception e) {
				}
			}
		}

		sqlObject.append("	ORDER BY CT.SHORT_CODE ");
		sqlObject.append(" limit "
				+ Integer.toString(Constants.NUM_ITEM_PER_PAGE));
		sqlObject.append(" offset "
				+ Integer.toString((page - 1) * Constants.NUM_ITEM_PER_PAGE));

		Cursor c = null;
		try {
			c = rawQueries(sqlObject.toString(), paramsObject);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						CustomerAttendProgramListItem item = new CustomerAttendProgramListItem();
						item.initDataFromCursor(c);

						result.cusList.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}

		return result;
	}
	
	/**
	 * Lấy ds KH tham gia CT HTBH
	 * 
	 * @author: dungnt19
	 * @since: 1.0
	 * @return: CustomerAttendProgramListDTO
	 * @throws:
	 * @param bundle
	 * @return
	 * @edit : quangvt1
	 */
	public CustomerAttendProgramListDTO getCustomerListProgramDone(Bundle bundle) {
		CustomerAttendProgramListDTO result = new CustomerAttendProgramListDTO();
		// -- ma chuong trinh
		long proInfoId = bundle.getLong(IntentConstants.INTENT_ID);
		// -- trang thai
		String status = bundle.getString(IntentConstants.INTENT_STATUS);
		// -- muc
		String level = bundle.getString(IntentConstants.INTENT_LEVEL);
		// -- du lieu search
		String name = bundle.getString(IntentConstants.INTENT_NAME);
		// -- Co lay so trang khong?
		boolean isGetTotalPage = bundle.getBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE);
		// -- Lay page nao?
		int page = bundle.getInt(IntentConstants.INTENT_PAGE);
		
		StringBuilder totalPageSql = new StringBuilder();
		ArrayList<String> paramsObject = new ArrayList<String>();
		StringBuffer sqlObject = new StringBuffer();
		sqlObject.append("SELECT C.CUSTOMER_ID, ");
		sqlObject.append("       C.SHORT_CODE, ");
		sqlObject.append("       C.CUSTOMER_NAME, ");
		sqlObject.append("       C.HOUSENUMBER, ");
		sqlObject.append("       C.STREET, ");
		sqlObject.append("       C.ADDRESS, ");
		sqlObject.append("       A.AREA_NAME AS AREA_NAME, ");
		sqlObject.append("       PCM.LEVEL_NUMBER AS LEVEL_NUMBER, ");
		sqlObject.append("       PCM.JOINING_DATE AS JOINING_DATE, ");
		sqlObject.append("       PCM.PRO_CUS_MAP_ID, ");
		sqlObject.append("       PCH.TYPE, ");
		
		// So luong sanh pham dang ki tham gia
		sqlObject.append("       ( CASE ");
		sqlObject.append("           WHEN( (SELECT COUNT(*) ");
		sqlObject.append("                  FROM   PRO_CUS_MAP_DETAIL PCMD ");
		sqlObject.append("                  WHERE  PCMD.PRO_CUS_MAP_ID = PCM.PRO_CUS_MAP_ID ");
		sqlObject.append("                         AND PCMD.QUANTITY > 0) > 0 ) THEN 1 ");
		sqlObject.append("           ELSE 0 ");
		sqlObject.append("         END ) AS NUM_JOIN ");
		
		sqlObject.append("FROM   PRO_CUS_MAP PCM, ");
		sqlObject.append("       PRO_CUS_HISTORY PCH, ");
		sqlObject.append("       CUSTOMER C, ");
		sqlObject.append("       AREA A ");
		sqlObject.append("WHERE  1 = 1 ");
		sqlObject.append("       AND PCM.PRO_INFO_ID = ? ");
		paramsObject.add(proInfoId + "");
		sqlObject.append("       AND PCM.OBJECT_TYPE = 1 ");
		sqlObject.append("       AND PCM.OBJECT_ID = C.CUSTOMER_ID ");
		sqlObject.append("       AND PCM.PRO_CUS_MAP_ID = PCH.PRO_CUS_MAP_ID ");
		sqlObject.append("       AND A.AREA_ID = C.AREA_ID ");
		sqlObject.append("       AND PCH.TO_DATE IS NULL ");
		
		if (!"-1".equals(status)) {
			sqlObject.append("	      AND PCH.TYPE = ?	");
			paramsObject.add(status);
		}
		if (!"0".equals(level)) {
			sqlObject.append("	      AND PCM.LEVEL_NUMBER = ?	");
			paramsObject.add(level);
		}
		if (!StringUtil.isNullOrEmpty(name)) {
			name = StringUtil.getEngStringFromUnicodeString(name);
			name = StringUtil.escapeSqlString(name);
			name = DatabaseUtils.sqlEscapeString("%" + name + "%");
			name = name.substring(1, name.length()-1);
			// tim theo truong ten va dia chi
			sqlObject.append("	and (upper(C.NAME_TEXT) like upper(?) escape '^' ");
			paramsObject.add(name);
			// tim theo dt co dinh
			sqlObject.append("	or upper(C.PHONE) like upper(?) escape '^' ");
			paramsObject.add(name);
			// tim theo ma khach hang
			sqlObject.append("	or upper(C.SHORT_CODE) like upper(?) escape '^' ");
			paramsObject.add(name);
			// tim theo so dtdd
			sqlObject.append("	or upper(C.MOBIPHONE) like upper(?) escape '^' )");
			paramsObject.add(name);
		} 
		
		Cursor cTotalRow = null;
		if (isGetTotalPage) {
			totalPageSql.append("select count(*) as TOTAL_ROW from ("
					+ sqlObject + ")");
			try {
				cTotalRow = rawQueries(totalPageSql.toString(), paramsObject);
				if (cTotalRow != null) {
					if (cTotalRow.moveToFirst()) {
						result.totalCustomer = cTotalRow.getInt(0);
					}
				}
			} catch (Exception ex) {
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			} finally {
				try {
					if (cTotalRow != null) {
						cTotalRow.close();
					}
				} catch (Exception e) {
				}
			}
		}
		
		sqlObject.append("	ORDER BY C.SHORT_CODE ");
		sqlObject.append(" limit "
				+ Integer.toString(Constants.NUM_ITEM_PER_PAGE));
		sqlObject.append(" offset "
				+ Integer.toString((page - 1) * Constants.NUM_ITEM_PER_PAGE));
		
		Cursor c = null;
		try {
			c = rawQueries(sqlObject.toString(), paramsObject);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						CustomerAttendProgramListItem item = new CustomerAttendProgramListItem();
						item.initDataFromCursor(c);
						
						result.cusList.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}
		
		return result;
	}

	/**
	 * Lay danh sach khach hang can add chuong trinh HTBh
	 * 
	 * @author: hoanpd1
	 * @since: 1.0
	 * @return: CustomerAttendProgramListDTO
	 * @throws:
	 * @param bundle
	 * @return
	 */
	public CustomerAttendProgramListDTO getAddCustomerList(Bundle bundle) {
		Cursor c = null;
		CustomerAttendProgramListDTO result = new CustomerAttendProgramListDTO();
		// -- ma chuong trinh
		long proInfoId = bundle.getLong(IntentConstants.INTENT_PROGRAM_ID);
		// -- trang thai
		String visitPlan = bundle.getString(IntentConstants.INTENT_VISIT_PLAN);
		// -- muc
		//String level = bundle.getString(IntentConstants.INTENT_LEVEL);
		// -- du lieu search
		String strSearch = bundle.getString(IntentConstants.INTENT_DATA);
		// -- ma nhan vien
		long staffId = bundle.getLong(IntentConstants.INTENT_STAFF_ID);
		// -- shopid
		String shopId = bundle.getString(IntentConstants.INTENT_SHOP_ID);
		// -- Co lay so trang khong?
		boolean isGetTotalPage = bundle.getBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE);
		// -- Lay page nao?
		int page = bundle.getInt(IntentConstants.INTENT_PAGE);
		String date_now = DateUtils
				.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		StringBuilder totalPageSql = new StringBuilder();
		ArrayList<String> params = new ArrayList<String>();
		StringBuffer  sql = new StringBuffer();
        sql.append(" SELECT CT.customer_id         AS CUSTOMER_ID,  ");
        sql.append("       CT.short_code           AS SHORT_CODE,  ");
        sql.append("       CT.customer_name        AS CUSTOMER_NAME,       ");
        sql.append("       S.shop_code             AS SHOP_CODE,  ");
        sql.append("       CT.name_text            AS NAME_TEXT1,  ");
        sql.append("       CT.ADDRESS1              AS ADDRESS1,   ");
        sql.append("       CT.ADDRESS 			   AS ADDRESS, ");
        sql.append("       (CASE WHEN RTC.monday = 1 THEN 'T2' ELSE '' END   ");
        sql.append("        || CASE WHEN RTC.tuesday = 1 THEN ',T3' ELSE '' END   ");
        sql.append("        || CASE WHEN RTC.wednesday = 1 THEN ',T4' ELSE '' END   ");
        sql.append("        || CASE WHEN RTC.thursday = 1 THEN ',T5' ELSE '' END   ");
        sql.append("        || CASE WHEN RTC.friday = 1 THEN ',T6' ELSE '' END   ");
        sql.append("        || CASE WHEN RTC.saturday = 1 THEN ',T7' ELSE '' END   ");
        sql.append("        || CASE WHEN RTC.sunday = 1 THEN ',CN' ELSE '' END ) AS CUS_PLAN  ");
        sql.append(" FROM  visit_plan VP,  ");
        sql.append("       routing RT,  ");
        sql.append("       (SELECT routing_id,  ");
        sql.append("               customer_id AS CUID,  ");
        sql.append("               status, ");
        sql.append("               monday,  ");
        sql.append("               tuesday,  ");
        sql.append("               wednesday,  ");
        sql.append("               thursday,  ");
        sql.append("               friday,  ");
        sql.append("               saturday, "); 
        sql.append("               sunday,  ");
        sql.append("               SEQ2, ");
        sql.append("               SEQ3, ");
        sql.append("               SEQ4, ");
        sql.append("               SEQ5, ");
        sql.append("               SEQ6, ");
        sql.append("               SEQ7, ");
        sql.append("               SEQ8 ");
        sql.append("        FROM   routing_customer  ");
        sql.append("        WHERE  ( substr(end_date,1,10) >= ?  ");
        params.add(date_now);
        sql.append("                  OR Date(end_date) IS NULL )  ");
        sql.append("               AND substr(start_date,1,10) <= ? ) RTC, ");
        params.add(date_now);
//        sql.append("               AND ( ( week1 IS NULL  ");
//        sql.append("                       AND week2 IS NULL  ");
//        sql.append("                       AND week3 IS NULL  ");
//        sql.append("                       AND week4 IS NULL )  ");
//        sql.append("                      OR ( ( ( Cast(( Julianday(?) - Julianday(start_date) ) /7 AS INTEGER)  ");
//        params.add(date_now);
//        sql.append("                                 + (CASE WHEN ( ( Julianday(?) - Julianday(start_date) ) % 7 > 0 )  ");
//        params.add(date_now);
//        sql.append("                                          AND ( Cast(( CASE  WHEN Strftime('%w',?) = '0' THEN 7  ");
//        params.add(date_now);
//        sql.append("                                                             ELSE Strftime('%w', ? ) END ) AS INTEGER )  ");
//        params.add(date_now);
//        sql.append("                                              < Cast(( CASE WHEN Strftime('%w', start_date) = '0' THEN 7  ");
//        sql.append("                                                            ELSE Strftime('%w',start_date) END ) AS INTEGER) ) THEN 1  ");
//        sql.append("                                 ELSE 0 END ) ) % 4 + 1 ) = 1 AND week1 = 1 )  ");
//        sql.append("                      OR ( ( ( Cast(( Julianday(?) - Julianday(start_date) ) / 7 AS INTEGER)  ");
//        params.add(date_now);
//        sql.append("                                 + (CASE WHEN ( ( Julianday(?) - Julianday(start_date) ) % 7 > 0 )  ");
//        params.add(date_now);
//        sql.append("                                          AND ( Cast(( CASE WHEN Strftime('%w',? ) = '0' THEN 7  ");
//        params.add(date_now);
//        sql.append("                                                            ELSE  Strftime('%w',?) END ) AS INTEGER )  ");
//        params.add(date_now);
//        sql.append("                                              < Cast(( CASE WHEN Strftime('%w', start_date) = '0' THEN 7  ");
//        sql.append("                                                            ELSE Strftime('%w', start_date) END ) AS INTEGER) ) THEN 1  ");
//        sql.append("                                 ELSE 0 END ) ) % 4 + 1 ) = 2 AND week2 = 1 )  ");
//        sql.append("                      OR ( ( ( Cast(( Julianday(?) - Julianday(start_date) ) / 7 AS INTEGER)  ");
//        params.add(date_now);
//        sql.append("                                 + (CASE WHEN ( ( Julianday(?) - Julianday(start_date) ) % 7 > 0 )  ");
//        params.add(date_now);
//        sql.append("                                         AND ( Cast(( CASE WHEN Strftime('%w',?) = '0' THEN 7  ");
//        params.add(date_now);
//        sql.append("                                                        ELSE Strftime('%w',? ) END ) AS INTEGER )  ");
//        params.add(date_now);
//        sql.append("                                             < Cast(( CASE WHEN Strftime('%w',start_date) = '0' THEN 7  ");
//        sql.append("                                                        ELSE Strftime('%w', start_date) END ) AS INTEGER) ) THEN 1  ");
//        sql.append("                                 ELSE 0 END ) ) % 4 + 1 ) = 3 AND week3 = 1 )  ");
//        sql.append("                     OR ( ( ( Cast(( Julianday(?) - Julianday(start_date) ) / 7 AS INTEGER)  ");
//        params.add(date_now);
//        sql.append("                                 + (CASE WHEN ( ( Julianday(?) - Julianday(start_date) ) % 7 > 0 )  ");
//        params.add(date_now);
//        sql.append("                                      AND ( Cast(( CASE WHEN Strftime('%w', ? ) = '0' THEN 7  ");
//        params.add(date_now);
//        sql.append("                                                        ELSE Strftime('%w', ? ) END ) AS INTEGER )  ");
//        params.add(date_now);
//        sql.append("                                          < Cast(( CASE WHEN Strftime('%w', start_date) = '0' THEN 7  ");
//        sql.append("                                                        ELSE Strftime('%w', start_date) END ) AS INTEGER) ) THEN 1  ");
//        sql.append("                                 ELSE 0 END ) ) % 4 + 1 ) = 4 AND week4 = 1 ) )) RTC , ");
        sql.append("       (Select c.customer_id,     ");   
        sql.append("               c.short_code,      ");          
        sql.append("               c.customer_name,   ");
        sql.append("               c.shop_id,                ");
        sql.append("               c.name_text, ");
        sql.append("               (ifnull(C.housenumber,'') ||' '|| ifnull(C.street,'') ||', '||ifnull(A.AREA_NAME,'') ) AS ADDRESS1 , ");
        sql.append("               c.MOBIPHONE, ");
        sql.append("               c.PHONE, ");
        sql.append("               ( CASE WHEN C.ADDRESS IS NOT NULL THEN C.ADDRESS ");
        sql.append("		                  ELSE ( ifnull(C.housenumber,'') ||' '|| ifnull(C.street,''))  ");    
        sql.append("                END ) AS ADDRESS ");
        sql.append("        from  CUSTOMER C, ");
        sql.append("              CHANNEL_TYPE CT,      ");
        sql.append("              PRO_INFO PI,      ");
        sql.append("              PRO_INFO_CUS PIC, ");
        sql.append("              AREA A ");
        sql.append("        where c.[CHANNEL_TYPE_ID] = ct.[CHANNEL_TYPE_ID] ");
        sql.append("              and A.AREA_ID = C.AREA_ID     ");  
        sql.append("              and PIC.[PRO_INFO_ID] = PI.[PRO_INFO_ID]     ");  
        sql.append("              and ct.type =3  ");
        sql.append("              and A.status =1  ");
        sql.append("              and PI.PRO_INFO_ID = ?      ");   
        params.add(""+proInfoId);       
        sql.append("              and ct.object_type = PIC.type     "); 
        sql.append("              and c.customer_id not in (SELECT C.CUSTOMER_ID  "); 
        sql.append("                      					FROM   PRO_CUS_MAP PCM, "); 
        sql.append("                          					   PRO_CUS_HISTORY PCH, "); 
        sql.append("                          					   CUSTOMER C, "); 
        sql.append("                                               AREA A  "); 
        sql.append("                                        WHERE  1 = 1         "); 
        sql.append("                          					   AND PCM.PRO_INFO_ID = ?   ");
        params.add(""+proInfoId); 
        sql.append("                          					   AND PCM.OBJECT_TYPE = 1        "); 
        sql.append("                          					   AND PCM.OBJECT_ID = C.CUSTOMER_ID    ");     
        sql.append("                          					   AND PCM.PRO_CUS_MAP_ID = PCH.PRO_CUS_MAP_ID     ");    
        sql.append("                          					   AND A.AREA_ID = C.AREA_ID        "); 
        sql.append("                          					   AND PCH.TO_DATE IS NULL ) "); 
        sql.append("              and c.status =1       ");
        sql.append("              and ct.status =1) CT,  ");
        sql.append("       shop S  ");
        sql.append(" WHERE  VP.routing_id = RT.routing_id  ");
        sql.append("       AND s.status = 1  ");
        sql.append("       AND rt.status = 1  ");
        sql.append("       AND RTC.routing_id = RT.routing_id  ");
        sql.append("       AND RTC.cuid = CT.customer_id  ");
        sql.append("       AND substr(VP.from_date,1,10) <= ?  ");
        params.add(date_now);
        sql.append("       AND RT.type = 0  ");
        sql.append("       AND S.shop_id = CT.shop_id  ");
        sql.append("       AND CT.shop_id = ?  ");
        params.add(shopId);
        sql.append("       AND ( substr(VP.to_date,1,10) >= ?  ");
        params.add(date_now);
        sql.append("              OR Date(VP.to_date) IS NULL )  ");
        sql.append("       AND VP.staff_id = ?  ");
        params.add(""+staffId);
		if (!StringUtil.isNullOrEmpty(visitPlan)) {
			sql.append("	and (upper(CUS_PLAN) like upper(?) )");
			params.add("%" + visitPlan + "%");
		}
		if (!StringUtil.isNullOrEmpty(strSearch)) {
			strSearch = StringUtil.getEngStringFromUnicodeString(strSearch);
			strSearch = StringUtil.escapeSqlString(strSearch);
			strSearch = DatabaseUtils.sqlEscapeString("%" + strSearch + "%"); 
			strSearch = strSearch.substring(1, strSearch.length()-1);
			// tim theo truong ten va dia chi
			sql.append("	and (upper(CT.NAME_TEXT) like upper(?) escape '^' ");
			params.add(strSearch);
			// tim theo dt co dinh
			sql.append("	or upper(CT.PHONE) like upper(?) escape '^' ");
			params.add(strSearch);
			// tim theo ma khach hang
			sql.append("	or upper(CT.SHORT_CODE) like upper(?) escape '^' ");
			params.add(strSearch);
			// tim theo so dtdd
			sql.append("	or upper(CT.MOBIPHONE) like upper(?) escape '^' )");
			params.add(strSearch);
		} 
		
        sql.append(" GROUP  BY CUSTOMER_ID ");
		Cursor cTotalRow = null;
		if (isGetTotalPage) {
			totalPageSql.append("select count(*) as TOTAL_ROW from ("
					+ sql + ")");
			try {
				cTotalRow = rawQueries(totalPageSql.toString(), params);
				if (cTotalRow != null) {
					if (cTotalRow.moveToFirst()) {
						result.totalCustomer = cTotalRow.getInt(0);
					}
				}
			} catch (Exception ex) {
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			} finally {
				try {
					if (cTotalRow != null) {
						cTotalRow.close();
					}
				} catch (Exception e) {
				}
			}
		}

        sql.append(" ORDER  BY SHORT_CODE,    ");        
        sql.append("           CUSTOMER_NAME ");
		sql.append(" limit "
				+ Integer.toString(Constants.NUM_ITEM_PER_PAGE));
		sql.append(" offset "
				+ Integer.toString((page - 1) * Constants.NUM_ITEM_PER_PAGE));
		try {
			c = rawQueries(sql.toString(), params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						CustomerAttendProgramListItem item = new CustomerAttendProgramListItem();
						item.initDataFromCursor(c);

						result.cusList.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}

		return result;
	}

	/**
	 * Lay danh sach san pham de dang ki san luong tham gia
	 * @author: quangvt1
	 * @since: 17:04:33 20-05-2014
	 * @return: ListProductQuantityJoin
	 * @throws:  
	 * @param customer_id
	 * @param level
	 * @return
	 */
	public ListProductQuantityJoin getListProductForJoin(long pro_info_id,
			long customer_id, String level) { 
		ListProductQuantityJoin result = new ListProductQuantityJoin(); 
		ArrayList<String> params = new ArrayList<String>();	 
		StringBuffer  var1 = new StringBuffer();
		var1.append("SELECT P.PRODUCT_ID, ");
		var1.append("       P.PRODUCT_CODE, ");
		var1.append("       P.PRODUCT_NAME, ");
		var1.append("       PCMD.QUANTITY, ");
		var1.append("       PCM.PRO_CUS_MAP_ID, ");
		var1.append("       PCMD.PRO_CUS_MAP_DETAIL_ID ");
		var1.append("FROM   PRO_STRUCTURE PS, ");
		var1.append("       PRO_STRUCTURE_DETAIL PSD, ");
		var1.append("       PRODUCT P, ");
		var1.append("       PRO_CUS_MAP PCM ");
		var1.append("       LEFT JOIN PRO_CUS_MAP_DETAIL PCMD ");
		var1.append("              ON PCMD.PRO_CUS_MAP_ID = PCM.PRO_CUS_MAP_ID ");
		var1.append("                 AND PCMD.PRODUCT_ID = P.PRODUCT_ID AND PCM.LEVEL_NUMBER = ?");
		params.add(level);
		var1.append("WHERE  1 = 1 ");
		var1.append("       AND PS.PRO_INFO_ID = ? ");
		params.add(pro_info_id + "");
		var1.append("       AND PS.PRO_STRUCTURE_ID = PSD.PRO_STRUCTURE_ID ");
		var1.append("       AND PSD.PRODUCT_ID = P.PRODUCT_ID ");
		var1.append("       AND PCM.PRO_INFO_ID = PS.PRO_INFO_ID ");
		var1.append("       AND PCM.OBJECT_ID = ? ");
		var1.append("       AND P.STATUS = 1 ");
		params.add(customer_id + ""); 
		var1.append("       AND PSD.LEVEL_NUMBER = ? ");
		params.add(level);  
		 
		Cursor c = null; 
		try {
			c = rawQueries(var1.toString(), params);
			if (c != null) {
				 result.initFromCursor(c);
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex)); 
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}
		
		return result;
	}

	public boolean updateLevelOfCustomerJoinHTBH(String pro_cus_map_id,
			int newLevel) { 
		return false;
	}
}