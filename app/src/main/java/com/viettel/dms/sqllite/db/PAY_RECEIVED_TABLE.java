/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.PayReceivedDTO;

/**
 * Bang cong no
 * 
 * @author: BangHN
 * @version: 1.0
 * @since: 1.0
 */
public class PAY_RECEIVED_TABLE extends ABSTRACT_TABLE {
	// id
	public static final String PAY_RECEIVED_ID = "PAY_RECEIVED_ID";
	//
	public static final String PAY_RECEIVED_NUMBER = "PAY_RECEIVED_NUMBER";
	//
	public static final String AMOUNT = "AMOUNT";
	//
	public static final String PAYMENT_TYPE = "PAYMENT_TYPE";
	//
	public static final String SHOP_ID = "SHOP_ID";
	//
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	//
	public static final String RECEIPT_TYPE = "RECEIPT_TYPE";
	//
	public static final String STAFF_ID = "STAFF_ID";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";

	// ten bang
	public static final String TABLE_PAY_RECEIVED = "PAY_RECEIVED";

	public PAY_RECEIVED_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_PAY_RECEIVED;
		this.columns = new String[] { PAY_RECEIVED_ID, PAY_RECEIVED_NUMBER, AMOUNT, PAYMENT_TYPE, SHOP_ID, CUSTOMER_ID,
				RECEIPT_TYPE };
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
		ContentValues value = initDataRow((PayReceivedDTO) dto);
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
	public long insert(PayReceivedDTO dto) {
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
		PayReceivedDTO disDTO = (PayReceivedDTO) dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.payReceivedID };
		return update(value, PAY_RECEIVED_ID + " = ?", params);
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
		return delete(PAY_RECEIVED_ID + " = ?", params);
	}

	public long delete(AbstractTableDTO dto) {
		PayReceivedDTO paramDTO = (PayReceivedDTO) dto;
		String[] params = { String.valueOf(paramDTO.payReceivedID) };
		return delete(PAY_RECEIVED_ID + " = ?", params);
	}

	private ContentValues initDataRow(PayReceivedDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(PAY_RECEIVED_ID, dto.payReceivedID);
		editedValues.put(PAY_RECEIVED_NUMBER, dto.payReceivedNumber);
		editedValues.put(AMOUNT, dto.amount);
		editedValues.put(PAYMENT_TYPE, dto.paymentType);
		editedValues.put(STAFF_ID, dto.staffId);
		editedValues.put(SHOP_ID, dto.shopId);
		editedValues.put(CUSTOMER_ID, dto.customerId);
		editedValues.put(RECEIPT_TYPE, dto.receiptType);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(CREATE_DATE, dto.createDate);
		return editedValues;
	}

}
