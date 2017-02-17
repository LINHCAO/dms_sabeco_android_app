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

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.view.DisplayPresentProductInfo;
import com.viettel.dms.dto.view.VoteDisplayPresentProductViewDTO;
import com.viettel.dms.dto.view.VoteDisplayProductDTO;
import com.viettel.dms.util.DateUtils;
import com.viettel.utils.VTLog;

/**
 * Mo ta muc dich cua lop (interface)
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class DISPLAY_CUSTOMER_MAP_TABLE extends ABSTRACT_TABLE {
	// id bang
	public static final String DISPLAY_CUSTOMER_MAP_ID = "DISPLAY_CUSTOMER_MAP_ID";
	// id bang nv phat trien cttb
	public static final String DISPLAY_STAFF_MAP_ID = "DISPLAY_STAFF_MAP_ID";
	// id muc cua cttb
	public static final String DISPLAY_PROGRAM_LEVEL_ID = "DISPLAY_PROGRAM_LEVEL_ID";
	// id kh tham gia cttb
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	// trang thai
	public static final String STATUS = "STATUS";
	// tu ngay
	public static final String FROM_DATE = "FROM_DATE";
	// den ngay
	public static final String TO_DATE = "TO_DATE";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// NGUOI TAO
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";

	private static final String TABLE_DISPLAY_CUSTOMER_MAP = "DISPLAY_CUSTOMER_MAP";

	public DISPLAY_CUSTOMER_MAP_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_DISPLAY_CUSTOMER_MAP;
		this.columns = new String[] { DISPLAY_CUSTOMER_MAP_ID, DISPLAY_STAFF_MAP_ID, DISPLAY_PROGRAM_LEVEL_ID,
				CUSTOMER_ID, STATUS, FROM_DATE, TO_DATE, CREATE_DATE, CREATE_USER, UPDATE_USER, UPDATE_DATE, SYN_STATE };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;

	}

	/**
	 * Lay danh sach chuong trinh trung bay cua khach hang
	 * @author: quangvt1
	 * @since: 14:10:31 09-05-2014
	 * @return: VoteDisplayPresentProductViewDTO
	 * @throws:  
	 * @return
	 */
	public VoteDisplayPresentProductViewDTO getVoteDisplayPresentProductView(String idStaff, String idCustomer) {
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		VoteDisplayPresentProductViewDTO result = new VoteDisplayPresentProductViewDTO();
		ArrayList<String> params = new ArrayList<String>();
		
		StringBuffer  sql = new StringBuffer();
		sql.append("SELECT PRO.PRO_INFO_CODE, ");
		sql.append("       PRO.PRO_INFO_ID, ");
		sql.append("       PRO.PRO_INFO_NAME, ");
		sql.append("       PRO_MAP.LEVEL_NUMBER ");
		sql.append("FROM   PRO_INFO PRO, ");
		sql.append("       PRO_CUS_MAP PRO_MAP ");
		sql.append("WHERE  1 = 1 ");
		sql.append("       AND PRO.PRO_INFO_ID = PRO_MAP.PRO_INFO_ID ");
		sql.append("       AND PRO.STATUS = 1 ");
		sql.append("       AND PRO.TYPE = 5 ");
		sql.append("       AND substr(PRO.FROM_DATE,1,10) <= ? ");
		params.add(date_now); 
		sql.append("       AND ( substr(PRO.TO_DATE,1,10) IS NULL ");
		sql.append("             OR substr(PRO.TO_DATE,1,10) >= ? ) ");
		params.add(date_now);
		sql.append("       AND PRO_MAP.OBJECT_ID = ? ");
		params.add(idCustomer); 
//		sql.append("       AND NOT EXISTS (SELECT * ");
//		sql.append("                       FROM   ACTION_LOG AC ");
//		sql.append("                       WHERE  substr(AC.START_TIME,1,10) = ? ");
//		params.add(date_now);
//		sql.append("                              AND AC.OBJECT_TYPE = 2 "); // cham trung bay
//		sql.append("                              AND AC.CUSTOMER_ID = ? ");
//		params.add(idCustomer); 
//		sql.append("                              AND AC.OBJECT_ID = PRO.PRO_INFO_ID ");
//		sql.append("                              AND AC.STAFF_ID = ? ) ");
		sql.append("       AND NOT EXISTS ( select pro_info_id ");
		sql.append("						from pro_display_program pdp");
		sql.append("						where customer_id = ? ");
		params.add(idCustomer); 
		sql.append("							  and substr(create_date,1,10) = ?");
		params.add(date_now); 
		sql.append("							  and pdp.pro_info_id = PRO.PRO_INFO_ID ) ");
		
		Cursor c = null;
		try {
			c = rawQueries(sql.toString(), params);

			if (c != null) {

				if (c.moveToFirst()) {
					do {
						DisplayPresentProductInfo program = new DisplayPresentProductInfo();
						program.initDisplayPresentProductInfo(c);
						result.listDisplayProgrameInfo.add(program);
					} while (c.moveToNext());
				}
			}

		} catch (Exception e) {
			VTLog.i("error", e.toString());
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
	 * get list display program product with customerId
	 * 
	 * @author: HaiTC3
	 * @param data
	 * @return
	 * @return: List<DisplayPresentProductInfo>
	 * @throws:
	 */
	public List<DisplayPresentProductInfo> getListDisplayProgramProduct(Bundle data) {
		String customerId = data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID);
		StringBuffer  queryGetListProductForOrder = new StringBuffer();
		queryGetListProductForOrder.append("SELECT DP.DISPLAY_PROGRAM_CODE AS DISPLAY_PROGRAM_CODE, ");
		queryGetListProductForOrder.append("       DP.DISPLAY_PROGRAM_NAME AS DISPLAY_PROGRAM_NAME, ");
		queryGetListProductForOrder.append("       DPL.LEVEL_CODE          AS LEVEL_CODE, ");
		queryGetListProductForOrder.append("       DP.DISPLAY_PROGRAM_ID   AS DISPLAY_PROGRAM_ID ");
		queryGetListProductForOrder.append("FROM   DISPLAY_CUSTOMER_MAP DCM, ");
		queryGetListProductForOrder.append("       DISPLAY_PROGRAM_LEVEL AS DPL, ");
		queryGetListProductForOrder.append("       DISPLAY_STAFF_MAP DSM, ");
		queryGetListProductForOrder.append("       DISPLAY_PROGRAM AS DP ");
		queryGetListProductForOrder.append("WHERE  DCM.DISPLAY_PROGRAM_LEVEL_ID = DPL.DISPLAY_PROGRAM_LEVEL_ID ");
		queryGetListProductForOrder.append("       AND DP.DISPLAY_PROGRAM_ID = DPL.DISPLAY_PROGRAM_ID ");
		queryGetListProductForOrder.append("       AND DCM.CUSTOMER_ID = ? ");
		queryGetListProductForOrder.append("       AND Date(DCM.FROM_DATE) <= (SELECT Date('now', 'localtime')) ");
		queryGetListProductForOrder.append("       AND Ifnull(Date(DCM.TO_DATE) >= (SELECT Date('now', 'localtime')), 1) ");
		queryGetListProductForOrder.append("       AND DCM.STATUS = 1 ");
		queryGetListProductForOrder.append("       AND DCM.DISPLAY_STAFF_MAP_ID = DSM.DISPLAY_STAFF_MAP_ID ");
		queryGetListProductForOrder.append("       AND DSM.STATUS = 1 ");
		queryGetListProductForOrder.append("       AND DSM.DISPLAY_PROGRAM_ID = DP.DISPLAY_PROGRAM_ID ");
		queryGetListProductForOrder.append("       AND DSM.STAFF_ID = ? ");
		queryGetListProductForOrder.append("       AND DP.STATUS = 1 ");
		queryGetListProductForOrder.append("       AND Date(DP.FROM_DATE) <= (SELECT Date('NOW', 'localtime')) ");
		queryGetListProductForOrder.append("       AND Ifnull(( Date(DP.TO_DATE) ) >= (SELECT Date('NOW', 'localtime')), 1) ");
		queryGetListProductForOrder.append("       AND DPL.STATUS = 1 ");
		queryGetListProductForOrder.append("       AND DP.DISPLAY_PROGRAM_ID NOT IN (SELECT OBJECT_ID AS DISPLAY_PROGRAM_ID ");
		queryGetListProductForOrder.append("                                         FROM   ACTION_LOG ");
		queryGetListProductForOrder.append("                                         WHERE  OBJECT_TYPE = 2 ");
		queryGetListProductForOrder.append("                                                AND Date(START_TIME) <= ");
		queryGetListProductForOrder.append("                                                    Date('NOW', 'localtime') ");
		queryGetListProductForOrder.append("                                                AND Ifnull(Date(END_TIME) >= ");
		queryGetListProductForOrder.append("                                                           Date('NOW', ");
		queryGetListProductForOrder.append("                                                           'localtime'), 1) ");
		queryGetListProductForOrder.append("                                                AND CUSTOMER_ID = ? ");
		queryGetListProductForOrder.append("                                                AND STAFF_ID = ?) ");
		queryGetListProductForOrder.append("ORDER  BY DISPLAY_PROGRAM_CODE ASC ");
		

		String[] params = new String[] { customerId, staffId, customerId, staffId };

		List<DisplayPresentProductInfo> listResult = new ArrayList<DisplayPresentProductInfo>();
		Cursor c = null;
		try {
			c = rawQuery(queryGetListProductForOrder.toString(), params);

			if (c != null) {

				if (c.moveToFirst()) {
					do {
						DisplayPresentProductInfo orderJoinTableDTO = new DisplayPresentProductInfo();
						orderJoinTableDTO.initDisplayPresentProductInfo(c);
						listResult.add(orderJoinTableDTO);
					} while (c.moveToNext());
				}
			}

		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		return listResult;
	}

	/**
	 * Lay danh sach san pham cham trung bay cua 1 chuong trinh
	 * @author: hoanpd1
	 * @since: 14:31:11 22-08-2014
	 * @return: DisplayPresentProductInfo
	 * @throws:  
	 * @param programId
	 * @return
	 */
	public DisplayPresentProductInfo getListVoteDisplayProduct(String programId) {
		ArrayList<String> params = new ArrayList<String>();
		StringBuffer  var1 = new StringBuffer();
		var1.append(" SELECT P.product_id         AS PRODUCT_ID, ");
		var1.append("        P.product_code       AS PRODUCT_CODE, ");
		var1.append("        P.product_name       AS PRODUCT_NAME, ");
		var1.append("        psd.display_quantity AS QUANTITY ");
		var1.append(" FROM   pro_structure ps, ");
		var1.append("        pro_structure_detail psd, ");
		var1.append("        product P ");
		var1.append(" WHERE  1 = 1 ");
		var1.append("        AND ps.pro_info_id = ? ");
		params.add(programId);
		var1.append("        AND psd.type = 1 ");
		var1.append("        AND ps.pro_structure_id = psd.pro_structure_id ");
		var1.append("        AND P.product_id = psd.product_id ");
		var1.append(" UNION ALL ");
		var1.append(" SELECT '-10'                 AS PRODUCT_ID, "); // lay dong tong nen se khong co product => dat product_id =-10 để check render dong tong luc render layout
		var1.append("        'zzzzzzzzzzzzzzzz'    AS PRODUCT_CODE, ");// dat product_code ='zzzzzzzzzzzzzzzzzz' de tien viec sau khi order by dong tong nam cuoi cung
		var1.append("       CASE WHEN ps.type = 1 THEN 'TOTAL_PRODUCT' ELSE 'TOTAL_NOT' end PRODUCT_NAME, "); // dua vao type de biet loai cua dong tong 
		var1.append("       psd.display_quantity QUANTITY ");
		var1.append(" FROM   pro_structure ps, ");
		var1.append("       pro_structure_detail psd ");
		var1.append(" WHERE  1 = 1 ");
		var1.append("       AND ps.pro_info_id = ? ");
		params.add(programId);
		var1.append("       AND psd.type = 2 ");
		var1.append("       AND ps.pro_structure_id = psd.pro_structure_id ");
		var1.append(" ORDER  BY PRODUCT_CODE, ");
		var1.append("          PRODUCT_NAME ");

		DisplayPresentProductInfo result = new DisplayPresentProductInfo();
		Cursor c = null; 
		try { 
			c = rawQueries(var1.toString(), params); 
			if (c != null) { 
				if (c.moveToFirst()) {
					do {
						VoteDisplayProductDTO displayProduct = new VoteDisplayProductDTO();
						displayProduct.initVoteDisplayProduct(c);
						result.listProductDisplay.add(displayProduct);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				} 
			} catch (Exception e) { 
				VTLog.i("error", e.toString());
			}

		}
		return result;
	}
 
	@Override
	protected long insert(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.lib.sqllite.db.ABSTRACT_TABLE#update(com.viettel
	 * .vinamilk.dto.db.AbstractTableDTO)
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
	 * com.viettel.vinamilk.lib.sqllite.db.ABSTRACT_TABLE#delete(com.viettel
	 * .vinamilk.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long delete(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

}
