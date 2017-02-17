/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.Vector;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.dto.db.VisitPlanDTO;
import com.viettel.dms.util.ServerLogger;
import com.viettel.utils.VTLog;

/**
 * Thong tin lo trinh ban hang
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class VISIT_PLAN_TABLE extends ABSTRACT_TABLE {
	// ID bảng
	public static final String VISIT_PLAN_ID = "VISIT_PLAN_ID";
	// ID tuyến
	public static final String ROUTING_ID = "ROUTING_ID";
	// id NPP
	public static final String SHOP_ID = "SHOP_ID";
	// ID NVBH
	public static final String STAFF_ID = "STAFF_ID";
	// tu ngay
	public static final String FROM_DATE = "FROM_DATE";
	// den ngay
	public static final String TO_DATE = "TO_DATE";
	// 0: ngung hoat dong, 1: hoat dong
	public static final String STATUS = "STATUS";
	// ngay tao
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";

	public static final String TABLE_VISIT_PLAN = "VISIT_PLAN";

	public VISIT_PLAN_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_VISIT_PLAN;
		this.columns = new String[] { VISIT_PLAN_ID, ROUTING_ID, SHOP_ID, STAFF_ID, FROM_DATE, TO_DATE, STATUS,
				UPDATE_DATE, CREATE_DATE, CREATE_USER, UPDATE_USER, SYN_STATE };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;

	}

	/**
	 * Them 1 dong xuong db
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	@Override
	public long insert(AbstractTableDTO dto) {
		ContentValues value = initDataRow((VisitPlanDTO) dto);
		return insert(null, value);
	}

	/**
	 * 
	 * them 1 dong xuong CSDL
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	public long insert(VisitPlanDTO dto) {
		ContentValues value = initDataRow(dto);
		return insert(null, value);
	}

	/**
	 * Update 1 dong xuong db
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	@Override
	public long update(AbstractTableDTO dto) {
		VisitPlanDTO disDTO = (VisitPlanDTO) dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.visitPlanId };
		return update(value, VISIT_PLAN_ID + " = ?", params);
	}

	/**
	 * Xoa 1 dong cua CSDL
	 * 
	 * @author: TruongHN
	 * @param id
	 * @return: int
	 * @throws:
	 */
	public int delete(String code) {
		String[] params = { code };
		return delete(VISIT_PLAN_ID + " = ?", params);
	}

	public long delete(AbstractTableDTO dto) {
		VisitPlanDTO cusDTO = (VisitPlanDTO) dto;
		String[] params = { String.valueOf(cusDTO.visitPlanId) };
		return delete(VISIT_PLAN_ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: TruongHN
	 * @param id
	 * @return: DisplayPrdogrameLvDTO
	 * @throws:
	 */
	public VisitPlanDTO getRowById(String id) {
		VisitPlanDTO dto = null;
		Cursor c = null;
		try {
			String[] params = { id };
			c = query(VISIT_PLAN_ID + " = ?", params, null, null, null);
		} catch (Exception ex) {
			c = null;
		}
		if (c != null) {
			if (c.moveToFirst()) {
				dto = initLogDTOFromCursor(c);
			}
		}
		if (c != null) {
			c.close();
		}
		return dto;
	}


	private VisitPlanDTO initLogDTOFromCursor(Cursor c) {
		VisitPlanDTO vsPlanDTO = new VisitPlanDTO();
		return vsPlanDTO;
	}

	/**
	 * 
	 * lay tat ca cac dong cua CSDL
	 * 
	 * @author: HieuNH
	 * @return
	 * @return: Vector<VisitPlanDTO>
	 * @throws:
	 */
	public Vector<VisitPlanDTO> getAllRow() {
		Vector<VisitPlanDTO> v = new Vector<VisitPlanDTO>();
		Cursor c = null;
		try {
			c = query(null, null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			VisitPlanDTO VisitPlanDTO;
			if (c.moveToFirst()) {
				do {
					VisitPlanDTO = initLogDTOFromCursor(c);
					v.addElement(VisitPlanDTO);
				} while (c.moveToNext());
			}
			c.close();
		}
		return v;

	}

	private ContentValues initDataRow(VisitPlanDTO dto) {
		ContentValues editedValues = new ContentValues();
		return editedValues;
	}

	/**
	 * Cap nhat last order khi tao don hang
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param visitPlanDTO
	 * @return: void
	 * @throws:
	 */

	public long updateLastOrder(VisitPlanDTO visitPlanDTO, String lastOrder) {
		ContentValues value = initDataRowUpdateFromOrder(visitPlanDTO, lastOrder);
		StringBuilder whereClause = new StringBuilder();
		ArrayList<String> whereParams = new ArrayList<String>();

		// whereClause.append(ACTIVE);
		whereClause.append(" = ?");
		whereClause.append(" AND ");
		whereClause.append(SHOP_ID);
		whereClause.append(" = ?");
		whereClause.append(" AND ");
		whereClause.append(STAFF_ID);
		whereClause.append(" = ?");
		whereClause.append(" AND ");
		// whereClause.append(CUSTOMER_ID);
		whereClause.append(" = ?");
		whereClause.append(" AND date(START_DATE) <= date(?) ");
		whereClause.append(" AND date(END_DATE) >= date(?)");

		whereParams.add(String.valueOf(visitPlanDTO.active));
		whereParams.add(String.valueOf(visitPlanDTO.shopId));
		whereParams.add(String.valueOf(visitPlanDTO.staffId));
		whereParams.add(String.valueOf(visitPlanDTO.customerId));
		whereParams.add(String.valueOf(visitPlanDTO.lastOrder));
		whereParams.add(String.valueOf(visitPlanDTO.lastOrder));

		long tmp = update(value, whereClause.toString(), whereParams.toArray(new String[whereParams.size()]));
		if(tmp <= 0){
			ServerLogger.sendLog("updateLastOrder", "Khong cap nhat dong visit_plan nao \n"
					+ value.toString() + "\n"
					+ whereClause.toString() + "\n"
					+ whereParams.toString()
					, TabletActionLogDTO.LOG_CLIENT);
		}
		return tmp;
	}

	/**
	 * Tao gia tri cap nhat
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param visitPlanDTO
	 * @return
	 * @return: ContentValues
	 * @throws:
	 */

	private ContentValues initDataRowUpdateFromOrder(VisitPlanDTO visitPlanDTO, String lastOrder) {
		ContentValues editedValues = new ContentValues();

		// editedValues.put(LAST_ORDER, lastOrder);

		return editedValues;
	}
}
