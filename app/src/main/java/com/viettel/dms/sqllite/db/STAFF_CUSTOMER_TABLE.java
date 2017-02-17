/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.StaffCustomerDTO;
import com.viettel.dms.util.StringUtil;
import com.viettel.utils.VTLog;

/**
 * Mo ta muc dich cua class
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
public class STAFF_CUSTOMER_TABLE extends ABSTRACT_TABLE {
	// STAFF_CUSTOMER_ID
	public static final String STAFF_CUSTOMER_ID = "STAFF_CUSTOMER_ID";
	// id nhan vien
	public static final String STAFF_ID = "STAFF_ID";
	// customer id nhan vien
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	// Ngày cuối cùng có đơn hàng
	public static final String LAST_ORDER = "LAST_ORDER";
	// Ngày cuối cùng có đơn hàng được duyệt
	public static final String LAST_APPROVE_ORDER = "LAST_APPROVE_ORDER";
	// id nhan vien
	public static final String DAY_PLAN = "DAY_PLAN";
	// Ngày có cập nhật doanh số mục tiêu của khách hàng.
	public static final String DAY_PLAN_DATE = "DAY_PLAN_DATE";
	// DS kế hoạch trung bình của khách hàng.
	public static final String DAY_PLAN_AVG = "DAY_PLAN_AVG";
	// Ngày có cập nhật doanh số mục tiêu trung bình.
	public static final String DAY_PLAN_AVG_DATE = "DAY_PLAN_AVG_DATE";
	// Ngày cho phép đặt hàng từ xa.
	public static final String EXCEPTION_ORDER_DATE = "EXCEPTION_ORDER_DATE";

	public static final String TABLE_NAME = "STAFF_CUSTOMER";

	public STAFF_CUSTOMER_TABLE() {
		this.tableName = TABLE_NAME;
		this.columns = new String[] { STAFF_CUSTOMER_ID, STAFF_ID, CUSTOMER_ID, LAST_ORDER, LAST_APPROVE_ORDER,
				DAY_PLAN, DAY_PLAN_DATE, DAY_PLAN_AVG, DAY_PLAN_AVG_DATE, EXCEPTION_ORDER_DATE, SYN_STATE };

		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = SQLUtils.getInstance().getmDB();
	}

	public STAFF_CUSTOMER_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_NAME;
		this.columns = new String[] { STAFF_CUSTOMER_ID, STAFF_ID, CUSTOMER_ID, LAST_ORDER, LAST_APPROVE_ORDER,
				DAY_PLAN, DAY_PLAN_DATE, DAY_PLAN_AVG, DAY_PLAN_AVG_DATE, EXCEPTION_ORDER_DATE, SYN_STATE };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
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
	* Lay thong tin mot row du lieu STAFF-CUSTOMER
	* @author: BangHN
	* @param staffId
	* @param customerId
	* @return
	* @return: StaffCustomerDTO
	* @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	public StaffCustomerDTO getRowStaffCustomer(String staffId, String customerId){
		StaffCustomerDTO dto = null;
		Cursor c = null;
		try {
			String query = "SELECT * FROM STAFF_CUSTOMER WHERE STAFF_ID = ? AND CUSTOMER_ID = ? ORDER BY SYN_STATE DESC ";
			String[] params = {staffId, customerId};
			c = rawQuery(query, params);
			if (c != null) {
				if (c.moveToFirst()) {
					dto = initFromCusor(c);
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
		return dto;
	}

	
	private ContentValues initDataRow(StaffCustomerDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(STAFF_CUSTOMER_ID, dto.staffCustomerId);
		editedValues.put(CUSTOMER_ID, dto.customerId);
		editedValues.put(STAFF_ID, dto.staffId);
		editedValues.put(EXCEPTION_ORDER_DATE, dto.exceptionOrderDate);
		return editedValues;
	}
	
	
	/**
	* Insert mot record staff customer, (add truong exception orderdate)
	* @author: BangHN
	* @param dto
	* @return
	* @return: long
	* @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	public long insertExceptionOrderDate(StaffCustomerDTO dto){
		ContentValues value = initDataRow(dto);
		return insert(null, value);
	}
	
	
	/**
	 * update ExceptionOrderDate
	 * 
	 * @author: TamPQ
	 * @param dto
	 * @return
	 * @return: long
	 * @throws:
	 */
	public long updateExceptionOrderDate(StaffCustomerDTO dto) {
		try {
			ContentValues value = new ContentValues();
			value.put(EXCEPTION_ORDER_DATE, dto.exceptionOrderDate);
			String[] params = { String.valueOf(dto.staffCustomerId) };
			return update(value, STAFF_CUSTOMER_ID + " = ?", params);
		} catch (Exception e) {
			return -1;
		}
	}
	/**
	 * Cap nhat lastOrder khi tao moi don hang
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	public long updateLastOrder(AbstractTableDTO dto) {
		StaffCustomerDTO lastOrder = (StaffCustomerDTO) dto;
		ContentValues value = initDataStaffCustomerUpdate(lastOrder);
		String[] params = { String.valueOf(lastOrder.customerId), String.valueOf(lastOrder.staffId) };
		return update(value, CUSTOMER_ID + " = ? AND " + STAFF_ID +  " = ? ", params);
	}
	
	/**
	*  Insert or update staff customer
	*  @author: TruongHN
	*  @param dto
	*  @return: long
	*  @throws:
	 */
	public long insertOrUpdate(StaffCustomerDTO dto){
		long res = -1;
		// kiem tra ton tai du lieu hay khong
		StaffCustomerDTO staffCus = getRowStaffCustomer(String.valueOf(dto.staffId),String.valueOf(dto.customerId));
		if (staffCus != null){
			// update
			dto.staffCustomerId = staffCus.staffCustomerId;
			res = updateLastOrder(dto);
		}else{
			TABLE_ID tableId = new TABLE_ID(mDB);
			long staffCustomerId = tableId.getMaxId(STAFF_CUSTOMER_TABLE.TABLE_NAME, dto.staffId);
			dto.staffCustomerId = staffCustomerId ;
			ContentValues value = initRowForInsertStaffCustomer(dto);
			res = insert(null, value);
		}
		return res;
	}
	
	private ContentValues initRowForInsertStaffCustomer(StaffCustomerDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(STAFF_CUSTOMER_ID, dto.staffCustomerId);
		editedValues.put(CUSTOMER_ID, dto.customerId);
		editedValues.put(STAFF_ID, dto.staffId);
		if(!StringUtil.isNullOrEmpty(dto.lastOrder)){
			editedValues.put(LAST_ORDER, dto.lastOrder);
		}
		if(!StringUtil.isNullOrEmpty(dto.exceptionOrderDate)){
			editedValues.put(EXCEPTION_ORDER_DATE, dto.exceptionOrderDate);
		}
		if(!StringUtil.isNullOrEmpty(dto.lastApproveOrder)) {
			editedValues.put(LAST_APPROVE_ORDER, dto.lastApproveOrder);
		}
		editedValues.put(SYN_STATE, dto.synState);
		return editedValues;
	}
	/**
	 * Cap nhat lastOrder khi tao moi don hang
	 * @author: TruongHN
	 * @param dto
	 * @return: ContentValues
	 * @throws:
	 */
	public ContentValues initDataStaffCustomerUpdate(StaffCustomerDTO dto) {
		ContentValues editedValues = new ContentValues();
		if(!StringUtil.isNullOrEmpty(dto.lastOrder)){
			editedValues.put(LAST_ORDER, dto.lastOrder);
		}
		if(!StringUtil.isNullOrEmpty(dto.lastApproveOrder)){
			editedValues.put(LAST_APPROVE_ORDER, dto.lastApproveOrder);
		}
		if(!StringUtil.isNullOrEmpty(dto.exceptionOrderDate)){
			editedValues.put(EXCEPTION_ORDER_DATE, dto.exceptionOrderDate);
		}else if(StringUtil.isNullOrEmpty(dto.lastOrder) 
				&& StringUtil.isNullOrEmpty(dto.exceptionOrderDate)){
			editedValues.put(EXCEPTION_ORDER_DATE, "");
		}
		return editedValues;
	}
	
	
	/**
	* Init du lieu staff-customer
	* @author: BangHN
	* @param c: du lieu sau khi query
	* @return
	* @return: StaffCustomerDTO
	* @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	public StaffCustomerDTO initFromCusor(Cursor c){
		StaffCustomerDTO dto = new StaffCustomerDTO();
		dto.staffCustomerId = (c.getLong(c.getColumnIndex(STAFF_CUSTOMER_ID)));
		dto.staffId = (c.getInt(c.getColumnIndex(STAFF_ID)));
		dto.customerId = (c.getLong(c.getColumnIndex(CUSTOMER_ID)));
		dto.exceptionOrderDate = (c.getString(c.getColumnIndex(EXCEPTION_ORDER_DATE)));
		//...
		return dto;
	}
}
