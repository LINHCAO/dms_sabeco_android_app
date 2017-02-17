/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.DebitDTO;
import com.viettel.dms.dto.db.DebitDetailDTO;
import com.viettel.dms.dto.view.CusDebitDetailDTO;
import com.viettel.dms.dto.view.CusDebitDetailDTO.CusDebitDetailItem;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;

/**
 * Bang chi tiet cong no
 * 
 * @author: BangHN
 * @version: 1.0
 * @since: 1.0
 */
public class DEBIT_DETAIL_TABLE extends ABSTRACT_TABLE {
	// id
	public static final String DEBIT_ID = "DEBIT_ID";
	public static final String DEBIT_DETAIL_ID = "DEBIT_DETAIL_ID";
	//
	public static final String FROM_OBJECT_ID = "FROM_OBJECT_ID";
	//
	public static final String AMOUNT = "AMOUNT";
	//
	public static final String DISCOUNT = "DISCOUNT";
	//
	public static final String TOTAL = "TOTAL";
	//
	public static final String TOTAL_PAY = "TOTAL_PAY";
	//
	public static final String REMAIN = "REMAIN";
	//
	public static final String FROM_OBJECT_NUMBER = "FROM_OBJECT_NUMBER";
	//
	public static final String INVOICE_NUMBER = "INVOICE_NUMBER";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	//
	public static final String TABLE_NAME = "DEBIT_DETAIL";

	public DEBIT_DETAIL_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_NAME;
		this.columns = new String[] { DEBIT_ID, DEBIT_DETAIL_ID, FROM_OBJECT_ID, AMOUNT, DISCOUNT, TOTAL, TOTAL_PAY,
				REMAIN, FROM_OBJECT_NUMBER, INVOICE_NUMBER, CREATE_DATE, UPDATE_DATE, CREATE_USER, UPDATE_USER };
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
		ContentValues value = initDataRow((DebitDetailDTO) dto);
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
	public long insert(DebitDetailDTO dto) {
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
		DebitDetailDTO disDTO = (DebitDetailDTO) dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.debitID };
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

	private ContentValues initDataRow(DebitDetailDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(DEBIT_ID, dto.debitID);
		editedValues.put(DEBIT_DETAIL_ID, dto.debitDetailID);
		editedValues.put(FROM_OBJECT_ID, dto.fromObjectID);
		editedValues.put(FROM_OBJECT_NUMBER, dto.fromObjectNumber);
		editedValues.put(AMOUNT, dto.amount);
		editedValues.put(DISCOUNT, dto.discount);
		editedValues.put(TOTAL, dto.total);
		editedValues.put(TOTAL_PAY, dto.totalPay);
		editedValues.put(REMAIN, dto.remain);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(CREATE_DATE, dto.createDate);
		return editedValues;
	}

	/**
	 * ghi no chi tiet
	 * 
	 * @author: TamPQ
	 * @param cusId
	 * @return
	 * @return: CusDebitDetailDTOvoid
	 * @throws:
	 */
	public CusDebitDetailDTO requestDebitDetail(long debitId) {
		CusDebitDetailDTO debitDetail = null;
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);
		StringBuffer var1 = new StringBuffer();
		var1.append("SELECT * ");
		var1.append("FROM   (SELECT DD.DEBIT_DETAIL_ID, ");
		var1.append("               SO.ORDER_NUMBER, ");
		var1.append("               STRFTIME('%d/%m/%Y', SO.ORDER_DATE) AS ORDER_DATE, ");
		var1.append("               SO.ORDER_DATE                       AS ORDER_DATE_2, ");
		var1.append("               DD.TOTAL, ");
		var1.append("               DD.TOTAL_PAY, ");
		var1.append("               DD.REMAIN ");
		var1.append("        FROM   DEBIT_DETAIL DD, ");
		var1.append("               DEBIT DB, ");
		var1.append("               SALE_ORDER SO ");
		var1.append("        WHERE  DD.DEBIT_ID = DB.DEBIT_ID ");
		var1.append("               AND DD.REMAIN >= 0 ");
		var1.append("               AND SO.SALE_ORDER_ID = DD.FROM_OBJECT_ID ");
		var1.append("               AND SO.SYN_STATE = 2 ");
		var1.append("               AND DATE(DD.CREATE_DATE) >= DATE('" + date_now
				+ "','localtime', 'start of month', '-1 MONTH') ");
		var1.append("               AND DD.DEBIT_ID = ?) D_D ");
		var1.append("       LEFT JOIN (SELECT PAY_RECEIVED_NUMBER, ");
		var1.append("                         DEBIT_DETAIL_ID  AS DEBIT_DETAIL_ID_1, ");
		var1.append("                         STRFTIME('%d/%m/%Y', MAX(PD.PAY_DATE)) AS PAY_DATE ");
		var1.append("                  FROM   PAYMENT_DETAIL PD, ");
		var1.append("                         PAY_RECEIVED PR ");
		var1.append("                  WHERE  PD.PAY_RECEIVED_ID = PR.PAY_RECEIVED_ID ");
		var1.append("                  GROUP  BY PD.DEBIT_DETAIL_ID) P_R ");
		var1.append("              ON D_D.DEBIT_DETAIL_ID = P_R.DEBIT_DETAIL_ID_1 ");
		var1.append("ORDER  BY ORDER_DATE_2 DESC, ");
		var1.append("          REMAIN DESC ");

		String[] params = { "" + debitId };
		Cursor c = null;
		try {
			c = rawQuery(var1.toString(), params);
			if (c != null) {
				debitDetail = new CusDebitDetailDTO();
				if (c.moveToFirst()) {
					do {
						CusDebitDetailItem item = debitDetail.newCusDebDetailItem();
						item.initData(c);
						debitDetail.arrList.add(item);
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

		StringBuffer var2 = new StringBuffer();
		var2.append("SELECT * FROM DEBIT WHERE DEBIT_ID = ? ");
		String[] params2 = { "" + debitId };
		Cursor c2 = null;
		try {
			c2 = rawQuery(var2.toString(), params2);
			if (c2.moveToFirst()) {
				debitDetail.totalPay = c2.getLong(c2.getColumnIndex("TOTAL_PAY"));
				debitDetail.totalDebit = c2.getLong(c2.getColumnIndex("TOTAL_DEBIT"));
				debitDetail.debitId = c2.getLong(c2.getColumnIndex("DEBIT_ID"));
			}
		} catch (Exception e) {
		} finally {
			try {
				if (c2 != null) {
					c2.close();
				}
			} catch (Exception e) {
			}
		}

		return debitDetail;
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @param cusDebitDetailItem
	 * @return: voidvoid
	 * @throws:
	 */
	public int updateDebt(CusDebitDetailItem cusDebitDetailItem) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(TOTAL_PAY, cusDebitDetailItem.paidAmount);
		editedValues.put(REMAIN, cusDebitDetailItem.remainingAmount);
		editedValues.put(UPDATE_DATE, DateUtils.now());
		editedValues.put(UPDATE_USER, "" + GlobalInfo.getInstance().getProfile().getUserData().userName);
		String[] params = { "" + cusDebitDetailItem.debitDetailId };
		return update(editedValues, DEBIT_DETAIL_ID + " = ?", params);

	}

}
