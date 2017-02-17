/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.PaymentDetailDTO;

/**
 * Bang cong no
 * 
 * @author: BangHN
 * @version: 1.0
 * @since: 1.0
 */
public class PAYMENT_DETAIL_TABLE extends ABSTRACT_TABLE {
	// id
	public static final String PAYMENT_DETAIL_ID = "PAYMENT_DETAIL_ID";
	//
	public static final String DEBIT_DETAIL_ID = "DEBIT_DETAIL_ID";
	//
	public static final String PAY_RECEIVED_ID = "PAY_RECEIVED_ID";
	//
	public static final String AMOUNT = "AMOUNT";
	//
	public static final String PAYMENT_TYPE = "PAYMENT_TYPE";
	//
	public static final String STATUS = "STATUS";
	//
	public static final String STAFF_ID = "STAFF_ID";
	//
	public static final String PAY_DATE = "PAY_DATE";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";

	// ten bang
	public static final String TABLE_PAYMENT_DETAIL = "PAYMENT_DETAIL";

	public PAYMENT_DETAIL_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_PAYMENT_DETAIL;
		this.columns = new String[] { PAYMENT_DETAIL_ID, DEBIT_DETAIL_ID, PAY_RECEIVED_ID, AMOUNT, PAYMENT_TYPE,
				STATUS, PAY_DATE };
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
		ContentValues value = initDataRow((PaymentDetailDTO) dto);
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
	public long insert(PaymentDetailDTO dto) {
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
		PaymentDetailDTO disDTO = (PaymentDetailDTO) dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.paymentDetailID };
		return update(value, PAYMENT_DETAIL_ID + " = ?", params);
	}

	/**
	 * Xoa 1 dong cua CSDL
	 * 
	 * @author: TruongHN
	 * @param id
	 * @return: int
	 * @throws:
	 */
	public int delete(String id) {
		String[] params = { id };
		return delete(PAYMENT_DETAIL_ID + " = ?", params);
	}

	public long delete(AbstractTableDTO dto) {
		PaymentDetailDTO paramDTO = (PaymentDetailDTO) dto;
		String[] params = { String.valueOf(paramDTO.paymentDetailID) };
		return delete(PAYMENT_DETAIL_ID + " = ?", params);
	}

	private ContentValues initDataRow(PaymentDetailDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(PAYMENT_DETAIL_ID, dto.paymentDetailID);
		editedValues.put(DEBIT_DETAIL_ID, dto.debitDetailID);
		editedValues.put(PAY_RECEIVED_ID, dto.payReceivedID);
		editedValues.put(AMOUNT, dto.amount);
		editedValues.put(PAYMENT_TYPE, dto.paymentType);
		editedValues.put(STATUS, dto.status);
		editedValues.put(STAFF_ID, dto.staffId);
		editedValues.put(PAY_DATE, dto.payDate);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(CREATE_DATE, dto.createDate);
		return editedValues;
	}

}
