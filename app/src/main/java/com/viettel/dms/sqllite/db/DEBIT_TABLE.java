/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.DebitDTO;
import com.viettel.dms.dto.view.CusDebitDetailDTO;
import com.viettel.dms.dto.view.CustomerDebtDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.StringUtil;
import com.viettel.utils.VTLog;

/**
 * Bang cong no
 * 
 * @author: BangHN
 * @version: 1.0
 * @since: 1.0
 */
public class DEBIT_TABLE extends ABSTRACT_TABLE {
	// id
	public static final String DEBIT_ID = "DEBIT_ID";
	//
	public static final String OBJECT_ID = "OBJECT_ID";
	//
	public static final String OBJECT_TYPE = "OBJECT_TYPE";
	// tong no
	public static final String TOTAL_AMOUNT = "TOTAL_AMOUNT";
	// tong no da tra
	public static final String TOTAL_PAY = "TOTAL_PAY";
	// tong no
	public static final String TOTAL_DEBIT = "TOTAL_DEBIT";
	// gia tri lon nhat cho phep no
	public static final String MAX_DEBIT_AMOUNT = "MAX_DEBIT_AMOUNT";
	// thoi han no toi da
	public static final String MAX_DEBIT_DATE = "MAX_DEBIT_DATE";

	// ten bang
	public static final String TABLE_NAME = "DEBIT";

	public DEBIT_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_NAME;
		this.columns = new String[] { DEBIT_ID, OBJECT_ID, OBJECT_TYPE, TOTAL_AMOUNT, TOTAL_PAY, TOTAL_DEBIT,
				MAX_DEBIT_AMOUNT, MAX_DEBIT_DATE };
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
		ContentValues value = initDataRow((DebitDTO) dto);
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
	public long insert(DebitDTO dto) {
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
		DebitDTO disDTO = (DebitDTO) dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.id };
		return update(value, DEBIT_ID + " = ?", params);
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
		return delete(DEBIT_ID + " = ?", params);
	}

	public long delete(AbstractTableDTO dto) {
		DebitDTO paramDTO = (DebitDTO) dto;
		String[] params = { String.valueOf(paramDTO.id) };
		return delete(DEBIT_ID + " = ?", params);
	}

	private ContentValues initDataRow(DebitDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(DEBIT_ID, dto.id);
		editedValues.put(OBJECT_ID, dto.objectID);
		editedValues.put(OBJECT_TYPE, dto.objectType);
		editedValues.put(TOTAL_AMOUNT, dto.totalAmount);
		editedValues.put(TOTAL_PAY, dto.totalPay);
		editedValues.put(TOTAL_DEBIT, dto.totalDebit);
		editedValues.put(MAX_DEBIT_AMOUNT, dto.maxDebitAmount);
		editedValues.put(MAX_DEBIT_DATE, dto.maxDebitDate);
		return editedValues;
	}

	/**
	 * Lay danh sach cong no khach hang
	 * 
	 * @author: BangHN
	 * @param customerCode
	 *            : search theo customer code
	 * @param nameAddress
	 *            : search theo name, dia chi
	 * @return
	 * @throws Exception
	 * @return: ArrayList<CustomerDebtDTO>
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	public ArrayList<CustomerDebtDTO> getCustomerDebt(String customerCode, String nameAddress) throws Exception {
		ArrayList<CustomerDebtDTO> listData = null;
		List<String> params = new ArrayList<String>();
		String staffId = "" + GlobalInfo.getInstance().getProfile().getUserData().id;
		String shopId  = "" + GlobalInfo.getInstance().getProfile().getUserData().shopId;
		
		StringBuffer  sql = new StringBuffer();
		sql.append("SELECT c.[short_code], ");
		sql.append("       c.[customer_id], ");
		sql.append("       c.[customer_code], ");
		sql.append("       c.[customer_name], ");
		sql.append("       c.[address], ");
		sql.append("       c.[housenumber] ");
		sql.append("       || ' ' ");
		sql.append("       || c.[street]                                                 AS STREET_HOUSE, ");
		sql.append("       Sum(dd.amount)                                                AS AMOUNT, ");
		sql.append("       Sum(dd.total)                                                 AS TOTAL, ");
		sql.append("       Sum(dd.total_pay)                                             AS ");
		sql.append("       TOTAL_PAY, ");
		sql.append("       Sum(dd.remain)                                                AS REMAIN, ");
		sql.append("       d.object_id, ");
		sql.append("       d.debit_id, ");
		sql.append("       dd.[debit_detail_id], ");
		sql.append("       Julianday(Date('now', 'localtime')) - Julianday(Date(Min(dd.create_date ))) AS ");
		sql.append("       REMAIN_DAY ");
		sql.append("FROM   debit_detail dd ");
		sql.append("       JOIN debit d ");
		sql.append("         ON d.debit_id = dd.debit_id ");
		sql.append("       JOIN customer c ");
		sql.append("         ON d.object_id = c.customer_id ");
		sql.append("WHERE  1 = 1 ");
		sql.append("       AND c.status = 1 ");
		sql.append("       AND c.shop_id = ? ");
		params.add(shopId);
		
		sql.append("       AND c.customer_id IN (SELECT DISTINCT rc.customer_id ");
		sql.append("                             FROM   visit_plan vp, ");
		sql.append("                                    routing_customer rc ");
		sql.append("                             WHERE  vp.routing_id = rc.routing_id ");
		sql.append("                                    AND vp.status = 1 ");
		sql.append("                                    AND rc.status = 1 ");
		sql.append("                                    AND vp.staff_id = ? ");
		params.add(staffId);
		
		sql.append("                                    AND ( Date(vp.to_date) >= ");
		sql.append("                                          Date('NOW', 'localtime') ");
		sql.append("                                           OR vp.to_date IS NULL )) ");
		sql.append("       AND d.object_type = 3 ");
		sql.append("       AND dd.remain > 0 ");
		
		if (!StringUtil.isNullOrEmpty(customerCode)) {
			sql.append("       AND Upper(c.[customer_code]) LIKE Upper(?) ");
			params.add("%" + customerCode.trim() + "%");
		}
		// customer name search
		if (!StringUtil.isNullOrEmpty(nameAddress)) {
			nameAddress = StringUtil.escapeSqlString(nameAddress);
			nameAddress = DatabaseUtils.sqlEscapeString("%" + nameAddress.trim() + "%");
			nameAddress = nameAddress.substring(1, nameAddress.length()-1);
			sql.append("	and ((upper(c.[name_text]) like upper(?) escape '^')");
			params.add(nameAddress);
			sql.append(" or (upper(STREET_HOUSE) like upper(?) escape '^') ");
			params.add(nameAddress);
			sql.append(" or (upper(c.[address]) like upper(?) escape '^'))");
			params.add(nameAddress);
		}
		
		
		sql.append("       AND Date(dd.create_date) >= Date('NOW', 'localtime', 'start of month', ");
		sql.append("                                   '-1 month') ");
		sql.append("GROUP  BY d.debit_id ");
		sql.append("ORDER  BY c.[short_code] ");

		Cursor cursor = null;
		try {
			String[] arrParam = new String[params.size()];
			for (int i = 0; i < params.size(); i++) {
				arrParam[i] = params.get(i);
			}
			cursor = rawQuery(sql.toString(), arrParam);

			if (cursor != null) {
				listData = new ArrayList<CustomerDebtDTO>();
				if (cursor.moveToFirst()) {
					do {
						CustomerDebtDTO debtCustomer = new CustomerDebtDTO();
						debtCustomer.initCustomerDebtFromCursor(cursor);
						if (debtCustomer != null)
							listData.add(debtCustomer);
					} while (cursor.moveToNext());
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (Exception ex) {
				}
			} else {

			}
		}
		return listData;
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @param cusDebitDetailDto
	 * @return: voidvoid
	 * @throws:
	 */
	public int updateDebt(CusDebitDetailDTO cusDebitDetailDto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(TOTAL_PAY, cusDebitDetailDto.totalPay);
		editedValues.put(TOTAL_DEBIT, cusDebitDetailDto.totalDebit);
		String[] params = { "" + cusDebitDetailDto.debitId };
		return update(editedValues, DEBIT_ID + " = ?", params);
	}

	/**
	 * 
	 * Kiem tra cong no cua KH co' ton tai hay ko?
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param customerId
	 * @return
	 * @throws Exception
	 * @return: boolean
	 * @throws:
	 */
	public long checkDebitExist(String customerId) throws Exception {
		long result = -1;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT debit_id from debit where object_id = ? and object_type = 3 order by syn_state desc limit 1");

		Cursor cursor = null;
		try {
			String[] arrParam = new String[] { customerId };
			cursor = rawQuery(sql.toString(), arrParam);

			if (cursor != null) {
				if (cursor.moveToNext()) {
					result = cursor.getLong(0);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (Exception ex) {
				}
			} else {

			}
		}
		return result;
	}

	/**
	 * 
	 * Tang cong no cua 1 KH
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param dto
	 * @return
	 * @return: boolean
	 * @throws:
	 */
	public boolean increaseDebit(AbstractTableDTO dto) {
		DebitDTO temp = (DebitDTO) dto;

		DebitDTO disDTO = new DebitDTO();
		disDTO.objectID = temp.objectID;
		disDTO.objectType = temp.objectType;
		disDTO.totalAmount = temp.totalAmount;
		disDTO.totalDebit = temp.totalDebit;

		StringBuilder sqlObject = new StringBuilder();
		sqlObject.append("UPDATE DEBIT SET ");
		sqlObject.append("TOTAL_AMOUNT = TOTAL_AMOUNT + ");
		sqlObject.append(disDTO.totalAmount);

		sqlObject.append(",TOTAL_DEBIT = TOTAL_DEBIT + ");
		sqlObject.append(disDTO.totalDebit);
		sqlObject.append(" WHERE ");
		sqlObject.append(" OBJECT_ID = ");
		sqlObject.append(disDTO.objectID);
		sqlObject.append(" AND OBJECT_TYPE = ");
		sqlObject.append(disDTO.objectType);

		try {
			execSQL(sqlObject.toString());
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
		}

		return true;
	}

}
