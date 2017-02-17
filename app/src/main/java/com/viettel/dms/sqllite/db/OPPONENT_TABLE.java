/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.ProductCompetitorListDTO;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * @author dungdq3
 *
 */
public class OPPONENT_TABLE extends ABSTRACT_TABLE {

	// id doi thu
	public static final String OPPONENT_ID = "OPPONENT_ID";
	// ma doi thu
	public static final String OPPONENT_CODE = "OPPONENT_CODE";
	// ten doi thu
	public static final String OPPONENT_NAME = "OPPONENT_NAME";
	
	// ma san pham doi thu
	public static final String PRODUCT_CODE = "PRODUCT_CODE";
	// ten san pham doi thu
	public static final String PRODUCT_NAME = "PRODUCT_NAME";
	//trang thai
	public static final String STATUS = "STATUS";
	// đơn vị lẽ
	public static final String UOM1 = "UOM1";
	// đơn vị package
	public static final String UOM2 = "UOM2";
	// quy cach
	public static final String CONVFACT = "CONVFACT";
	
	public static final String OPPONENT_TABLE = "OPPONENT";

	/* (non-Javadoc)
	 * @see com.viettel.dms.sqllite.db.ABSTRACT_TABLE#insert(com.viettel.dms.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long insert(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.viettel.dms.sqllite.db.ABSTRACT_TABLE#update(com.viettel.dms.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.viettel.dms.sqllite.db.ABSTRACT_TABLE#delete(com.viettel.dms.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long delete(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public OPPONENT_TABLE(SQLiteDatabase mDB) {
		this.tableName = OPPONENT_TABLE;

		this.columns = new String[] { OPPONENT_ID, OPPONENT_CODE, OPPONENT_NAME, PRODUCT_CODE, PRODUCT_NAME, STATUS,
				UOM1, UOM2, CONVFACT};
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;
	}

	/**
	* Lay danh sach cac san pham doi thu
	* @author: dungdq3
	* @return: ProductCompetitorListDTO
	*/
	public ProductCompetitorListDTO getInformationCompetitor(String staffId, String cusId, String date) { 
		Cursor cursor = null;
		ProductCompetitorListDTO productCompetitor = new ProductCompetitorListDTO();
		StringBuffer sql = new StringBuffer();
		ArrayList<String> params = new ArrayList<String>(); 
		 
		sql.append("SELECT * ");
		sql.append("FROM   (SELECT OP.OPPONENT_ID AS OPID, ");
		sql.append("               OP.OPPONENT_CODE, ");
		sql.append("               OP.OPPONENT_NAME ");
		sql.append("        FROM   OPPONENT OP ");
		sql.append("        WHERE  STATUS = 1) ");
		
		// Cac san pham cua doi thu
		sql.append("       INNER JOIN (SELECT OPP.OPPONENT_ID AS OPPID, ");
		sql.append("                          OPP.OP_PRODUCT_ID, ");
		sql.append("                          OPP.CONVFACT, ");
		sql.append("                          OPP.PRODUCT_CODE, ");
		sql.append("                          OPP.PRODUCT_NAME, ");
		sql.append("                          OPP.UOM1, ");
		sql.append("                          OPP.UOM2        AS UOM2 ");
		sql.append("                   FROM   OP_PRODUCT OPP ");
		sql.append("                   WHERE  OPP.STATUS = 1) ");
		sql.append("               ON OPID = OPPID ");
		sql.append("       LEFT JOIN (SELECT AP_PARAM_CODE, ");
		sql.append("                         AP_PARAM_NAME ");
		sql.append("                  FROM   AP_PARAM ");
		sql.append("                  WHERE  TYPE IN ( 'UOM1', 'UOM2' ) AND STATUS =1) ");
		sql.append("              ON UOM2 = AP_PARAM_CODE ");
		
		// Left join de lay thong tin, so luong san pham cua doi thu
		if(!StringUtil.isNullOrEmpty(cusId)){
			sql.append("       LEFT JOIN (SELECT OP1.OP_PRODUCT_ID 		AS SALE_PRODUCT_ID, ");
			sql.append("                         OP1.QUANTITY 			AS QUANTITY, ");
			sql.append("                         OP1.OP_SALE_VOLUME_ID 	AS OP_SALE_VOLUME_ID ");
			sql.append("                  FROM   OP_SALE_VOLUME OP1 ");
			sql.append("                  WHERE  substr(OP1.SALE_DATE,0,11) = ? ");
			params.add(date);
			sql.append("                         AND OP1.CUSTOMER_ID = ? ");
			params.add(cusId);
//			sql.append("                         AND OP1.STAFF_ID = ? ");
//			params.add(staffId);
			sql.append("                         AND OP1.TYPE = 0  "); // type : 1 - BSG, 0 - doi thu
			sql.append("                         AND NOT EXISTS  ");
			sql.append(" 						   ( SELECT OP2.OP_PRODUCT_ID ");
			sql.append("                             FROM   OP_SALE_VOLUME OP2 ");
			sql.append("                             WHERE  OP1.OP_SALE_VOLUME_ID <> OP2.OP_SALE_VOLUME_ID ");
			sql.append("                                    AND substr(OP1.SALE_DATE,0,11) = ");
			sql.append("                                        substr(OP2.SALE_DATE,0,11) ");
			sql.append("                                    AND OP1.CUSTOMER_ID = OP2.CUSTOMER_ID ");
			sql.append("                                    AND OP1.TYPE = OP2.TYPE ");
			sql.append("                                    AND OP2.STATUS = 1 ");
			sql.append("                                    AND OP1.OP_PRODUCT_ID = OP2.OP_PRODUCT_ID ");
			sql.append("                                    AND ( OP1.SYN_STATE IS NULL ");
			sql.append("                                          AND OP2.SYN_STATE IS NOT NULL )) ");
			sql.append("                         AND OP1.STATUS = 1 ) ");
			sql.append("              ON OP_PRODUCT_ID = SALE_PRODUCT_ID ");

			sql.append("       LEFT JOIN (SELECT OP1.OP_PRODUCT_ID AS PRICE_PRODUCT_ID, ");
			sql.append("                         OP1.PRICE 			AS PRICE, ");
			sql.append("                         OP1.OP_PRICE_ID 	AS OP_PRICE_ID ");
			sql.append("                  FROM   OP_PRICE OP1 ");
			sql.append("                  WHERE  substr(OP1.SALE_DATE,0,11) = ? ");
			params.add(date);
			sql.append("                         AND OP1.CUSTOMER_ID = ? ");
			params.add(cusId);
//			sql.append("                         AND OP1.STAFF_ID = ? ");
//			params.add(staffId);
			sql.append("                         AND OP1.TYPE = 0  "); // type : 1 - BSG, 0 - doi thu
			sql.append("                         AND NOT EXISTS  ");
			sql.append(" 						   ( SELECT OP2.OP_PRODUCT_ID ");
			sql.append("                             FROM   OP_PRICE OP2 ");
			sql.append("                             WHERE  OP1.OP_PRICE_ID <> OP2.OP_PRICE_ID ");
			sql.append("                                    AND substr(OP1.SALE_DATE,0,11) = ");
			sql.append("                                        substr(OP2.SALE_DATE,0,11) ");
			sql.append("                                    AND OP1.CUSTOMER_ID = OP2.CUSTOMER_ID ");
			sql.append("                                    AND OP1.TYPE = OP2.TYPE ");
			sql.append("                                    AND OP2.STATUS = 1 ");
			sql.append("                                    AND OP1.OP_PRODUCT_ID = OP2.OP_PRODUCT_ID ");
			sql.append("                                    AND ( OP1.SYN_STATE IS NULL ");
			sql.append("                                          AND OP2.SYN_STATE IS NOT NULL )) ");
			sql.append("                         AND OP1.STATUS = 1 ) ");
			sql.append("              ON OP_PRODUCT_ID = PRICE_PRODUCT_ID ");
		}
		
		// Order
		sql.append("ORDER  BY OPPONENT_NAME, ");
		sql.append("          PRODUCT_NAME");
		
		try {
			cursor = rawQueries(sql.toString(), params);

			if (cursor != null) {
				productCompetitor.initFromCursor(cursor);
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (Exception ex) {
				}
			} else {
			}
		}
		return productCompetitor;
	}
	
	/**
	* Lay danh sach cac san pham doi thu
	* @author: dungdq3
	* @return: ProductCompetitorListDTO
	*/
	public ProductCompetitorListDTO getInformationCompetitorPG() {
		// TODO Auto-generated method stub
		Cursor cursor = null;
		ProductCompetitorListDTO productCompetitor = new ProductCompetitorListDTO();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * ");
		sql.append(" FROM (select op.opponent_id as OPID, op.OPPONENT_CODE, op.OPPONENT_NAME from opponent op where status = 1) ");
		sql.append(" inner join ");
		sql.append(" (select opp.opponent_id as OPPID, opp.OP_PRODUCT_ID, opp.CONVFACT, opp.PRODUCT_CODE, opp.PRODUCT_NAME, opp.UOM1, opp.UOM2 as UOM2 ");
		sql.append(" from op_product opp ");
		sql.append(" where opp.status=1) ");
		
		sql.append(" on OPID=OPPID ");
		sql.append(" left join	");
		sql.append(" (select AP_PARAM_CODE, AP_PARAM_NAME from AP_PARAM where type in ('UOM1','UOM2')  and status =1)	");
		sql.append(" on UOM2= AP_PARAM_CODE ");
		//sql.append(" group by OPPID");
		sql.append(" order by OPPONENT_NAME, PRODUCT_NAME;	");
		
		try {
			cursor = rawQuery(sql.toString(), null);

			if (cursor != null) {
				productCompetitor.initFromCursor(cursor);
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (Exception ex) {
				}
			} else {
			}
		}
		return productCompetitor;
	}
	
	/**
	 * Lay danh sach cac san pham cua BSG
	 * @author: dungdq3
	 * @return: ProductCompetitorListDTO
	 */
	public ProductCompetitorListDTO getInformationBSG(String staffId, String cusId, String date) { 
		Cursor cursor = null;
		ProductCompetitorListDTO product = new ProductCompetitorListDTO();
		StringBuffer sql = new StringBuffer();
		ArrayList<String> params = new ArrayList<String>(); 
		
		sql.append("SELECT * ");
		sql.append("FROM   "); 
		
		// Cac san pham cua BSG 
		sql.append("   (SELECT PRO.PRODUCT_ID AS OP_PRODUCT_ID, ");
		sql.append("               PRO.PRODUCT_CODE, ");
		sql.append("               PRO.CAT_ID, ");
		sql.append("               PRO.PRODUCT_NAME, ");
		sql.append("       		   ( CASE ");
		sql.append("         		WHEN PRO.SORT_ORDER IS NULL THEN 'z' ");
		sql.append("         		ELSE SORT_ORDER ");
		sql.append("         		END )        AS SORT_ORDER, ");
		sql.append("               PRO.UOM1, ");
		sql.append("               PRO.UOM2       AS UOM2 ");
		sql.append("        FROM   PRODUCT PRO ");
		sql.append("        WHERE  PRO.STATUS = 1 AND PRO.CONVFACT > 1 ) P ");
		sql.append("       JOIN (SELECT PRODUCT_INFO_ID ");
		sql.append("             FROM   PRODUCT_INFO ");
		sql.append("             WHERE  TYPE = 1 ");
		sql.append("                    AND OBJECT_TYPE = 0 ");
		sql.append("                    AND STATUS = 1) PI ");
		sql.append("         ON P.CAT_ID = PI.PRODUCT_INFO_ID ");
		sql.append("       LEFT JOIN (SELECT AP_PARAM_CODE, ");
		sql.append("                         AP_PARAM_NAME ");
		sql.append("                  FROM   AP_PARAM ");
		sql.append("                  WHERE  TYPE IN ( 'UOM1', 'UOM2' ) AND STATUS =1 ) ");
		sql.append("              ON UOM2 = AP_PARAM_CODE ");
		
		// Left join de lay thong tin, so luong san pham cua doi thu
		if(!StringUtil.isNullOrEmpty(cusId)){
			sql.append("       LEFT JOIN (SELECT OP1.OP_PRODUCT_ID 		     AS OP_SALE_PRODUCT_ID, ");
			sql.append("                         OP1.QUANTITY 			     AS QUANTITY, ");
			sql.append("                         OP1.ACTION_TYPE 		     AS ACTION_TYPE, ");
			sql.append("                         OP1.OP_SALE_VOLUME_ID 	     AS OP_SALE_VOLUME_ID, ");
			sql.append("                         OP1.UPDATE_USER             AS UPDATE_USER, ");
			sql.append("                         OP1.STAFF_ID	             AS STAFF_ID ");
			sql.append("                  FROM   OP_SALE_VOLUME OP1 ");
			sql.append("                  WHERE  substr(OP1.SALE_DATE,1,10) = substr(?,1,10) ");
			params.add(date);
			sql.append("                         AND OP1.CUSTOMER_ID = ? ");
			params.add(cusId);
//			sql.append("                         AND STAFF_ID = ? ");
//			params.add(staffId);
			sql.append("                         AND OP1.TYPE = 1  "); // type : 1 - BSG, 0- doi thu
			sql.append("                         AND NOT EXISTS  ");
			sql.append(" 						   ( SELECT OP2.OP_PRODUCT_ID ");
			sql.append("                             FROM   OP_SALE_VOLUME OP2 ");
			sql.append("                             WHERE  OP1.OP_SALE_VOLUME_ID <> OP2.OP_SALE_VOLUME_ID ");
			sql.append("                                    AND DATE(OP1.SALE_DATE) = ");
			sql.append("                                        DATE(OP2.SALE_DATE) ");
			sql.append("                                    AND OP1.CUSTOMER_ID = OP2.CUSTOMER_ID ");
			sql.append("                                    AND OP1.TYPE = OP2.TYPE ");
			sql.append("                                    AND OP2.STATUS = 1 ");
			sql.append("                                    AND OP1.OP_PRODUCT_ID = OP2.OP_PRODUCT_ID ");
			sql.append("                                    AND ( OP1.SYN_STATE IS NULL ");
			sql.append("                                          AND OP2.SYN_STATE IS NOT NULL )) ");
			sql.append("                         AND OP1.STATUS = 1 ) ");
			sql.append("              ON OP_PRODUCT_ID = OP_SALE_PRODUCT_ID ");
		}
		
		// Order
		sql.append("ORDER  BY SORT_ORDER, PRODUCT_CODE, ");
		sql.append("          PRODUCT_NAME ");
		
		try {
			cursor = rawQueries(sql.toString(), params);
			
			if (cursor != null) {
				product.initFromCursorForInfoBSG(cursor);
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (Exception ex) {
				}
			} else {
			}
		}
		return product;
	}
}
