/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteStatement;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.SaleOrderDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.ListOrderMngDTO;
import com.viettel.dms.dto.view.NoSuccessSaleOrderDto;
import com.viettel.dms.dto.view.NoSuccessSaleOrderDto.NoSuccessSaleOrderItem;
import com.viettel.dms.dto.view.SaleInMonthDTO;
import com.viettel.dms.dto.view.SaleOrderCustomerDTO;
import com.viettel.dms.dto.view.SaleOrderViewDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.sale.order.ListOrderView;
import com.viettel.utils.VTLog;

/**
 * Thong tin don hang
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class SALE_ORDER_TABLE extends ABSTRACT_TABLE {
	// don hang presale
	public static final String ORDER_TYPE_PRESALE = "IN";
	public static final String ORDER_TYPE_VANSALE = "SO";
	// id don dat hang
	public static final String SALE_ORDER_ID = "SALE_ORDER_ID";
	// ma NPP
	public static final String SHOP_ID = "SHOP_ID";
	// Loai don hang: IN: pre->kh; CM: KH->Pre;SO Van->KH; CO KH->Van, TT-> don hang tra thuong
	public static final String ORDER_TYPE = "ORDER_TYPE";
	// ma don hang
	public static final String ORDER_NUMBER = "ORDER_NUMBER"; 
	// 1: don hang ban nhung chua tra, 0: don hang ban da thuc hien tra lai, 2 don tra hàng
	public static final String TYPE = "TYPE"; //
	// 2: don hang tao tren tablet chua yeu cau xac nhan, 3: don hang tao tren tablet yeu cau xac nhan, 
	// 0: don hang chua duyet,1: don hang da duyet, 4 huy do qua ngay khong phe duyet
	public static final String APPROVED = "APPROVED"; //
	// so hoa don
//	public static final String INVOICE_NUMBER = "INVOICE_NUMBER";
	// ngay lap don hang
	public static final String ORDER_DATE = "ORDER_DATE";
	// id khach hang
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	// ma nhan vien
	public static final String STAFF_ID = "STAFF_ID";
	// id NVGH
	public static final String DELIVERY_ID = "DELIVERY_ID";
	// Xe giao hang
	public static final String CAR_ID = "CAR_ID";
	// thue suat
	public static final String VAT = "VAT";
	// Id NVTT: nhan vien thu tien
	public static final String CASHIER_ID = "CASHIER_ID";
	// so tien don hang chua tinh khuyen mai
	public static final String AMOUNT = "AMOUNT";
	// so tien khuyen mai
	public static final String DISCOUNT = "DISCOUNT";
	// so tien don hang sau khi tinh khuyen mai
	public static final String TOTAL = "TOTAL";
	// 1: don hang tren web; 2: don hang tao ra tren table;
	public static final String ORDER_SOURCE = "ORDER_SOURCE";
	// mo ta
	public static final String DESCRIPTION = "DESCRIPTION";
	// id don hang bi tra
	public static final String FROM_SALE_ORDER_ID = "FROM_SALE_ORDER_ID";
	// tong trong luong don hang
	public static final String TOTAL_WEIGHT = "TOTAL_WEIGHT";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// 1: trong tuyen, 0: ngoai tuyen
	public static final String IS_VISIT_PLAN = "IS_VISIT_PLAN";
	// muc do khan
	public static final String PRIORITY = "PRIORITY";
	// ngay dat don hang mong muon
	public static final String DELIVERY_DATE = "DELIVERY_DATE";
	// ngay in
	public static final String TIME_PRINT = "TIME_PRINT";
	// ma user huy dung cho xoa don hang tablet: neu xoa tu dong code = AUTO_DEL
	public static final String DESTROY_CODE = "DESTROY_CODE";
	// tong so luong detail cua don hang
	public static final String TOTAL_DETAIL = "TOTAL_DETAIL"; 
	// ma NPP
	public static final String SHOP_CODE = "SHOP_CODE";
//	// ma CTKM dang Docmt
//	public static final String PROGRAM_CODE = "PROGRAM_CODE";
//	// so tien chiet khau %
//	public static final String DISCOUNT_PERCENT = "DISCOUNT_PERCENT";
//	// so tien KM
//	public static final String DISCOUNT_AMOUNT = "DISCOUNT_AMOUNT";
//	// so tien chiet khau toi da
//	public static final String MAX_AMOUNT_FREE = "MAX_AMOUNT_FREE";

	// public static final String SYN_OPERATOR = "SYN_OPERATOR";
	public static final String TABLE_NAME = "SALE_ORDER";
	public static final String TOTAL_QUANTITY = "TOTAL_QUANTITY";

	public SALE_ORDER_TABLE(SQLiteDatabase mDB) {
		tableName = TABLE_NAME;
		this.columns = new String[] { SALE_ORDER_ID, SHOP_ID, ORDER_TYPE,
				ORDER_NUMBER,TYPE, APPROVED, ORDER_DATE, CUSTOMER_ID,
				STAFF_ID, DELIVERY_ID,CAR_ID, VAT,CASHIER_ID, AMOUNT, DISCOUNT, TOTAL,ORDER_SOURCE,
				DESCRIPTION,FROM_SALE_ORDER_ID,TOTAL_WEIGHT, CREATE_USER, UPDATE_USER, CREATE_DATE, UPDATE_DATE,
				IS_VISIT_PLAN,PRIORITY,DELIVERY_DATE, TIME_PRINT,DESTROY_CODE, TOTAL_DETAIL, SHOP_CODE, TOTAL_QUANTITY, SYN_STATE };

		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;
	}

	/**
	 * xoa data
	 * 
	 * @author: DoanDM
	 * @return: void
	 * @throws:
	 */
	public void clearTable() {
		execSQL(sqlDelete);
	}

	/**
	 * drop database trong data
	 * 
	 * @author: BangHN
	 * @return: void
	 * @throws:
	 */
	public void dropDatabase() {
		try {
			GlobalInfo.getInstance().getAppContext()
					.deleteDatabase(Constants.DATABASE_NAME);
			VTLog.i("DBAdapter", "Drop database success.......");
		} catch (Exception e) {
			VTLog.i("DBAdapter", "Error drop database : " + e.toString());
		}
	}

	public long getCount() {
		SQLiteStatement statement = compileStatement(sqlGetCountQuerry);
		long count = statement.simpleQueryForLong();
		return count;
	}

	/**
	 * thay doi 1 dong cua CSDL
	 * 
	 * @author: HieuNH
	 * @param dto
	 * @return: int
	 * @throws:
	 */
	public long update(SaleOrderDTO dto) {
		ContentValues value = initDataRow(dto);
		String[] params = { "" + dto.saleOrderId };
		return update(value, SALE_ORDER_ID + " = ? ", params);
	}

	/**
	 * cap nhat don hang da chuyen CSDL
	 * 
	 * @author: PhucNT
	 * @param dto
	 * @return: int
	 * @throws:
	 */
	public long updateSentOrder(SaleOrderDTO dto) {
		ContentValues value = new ContentValues();

		dto.updateDate = DateUtils.now();
		// dto.state = 1;
		dto.updateUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
//		dto.importCode = "";

//		value.put(IS_SEND, dto.isSend);
		value.put(UPDATE_DATE, dto.updateDate);
		// value.put(STATE, dto.state );
//		value.put(IMPORT_CODE, dto.importCode);

		String[] params = { "" + dto.saleOrderId };
		return update(value, SALE_ORDER_ID + " = ? ", params);
	}

	public long update(AbstractTableDTO dto) {
		SaleOrderDTO saleDTO = (SaleOrderDTO) dto;
		ContentValues value = initDataRowForEdit(saleDTO, false);
		String[] params = { "" + saleDTO.saleOrderId, "" + saleDTO.synState };
		return update(value, SALE_ORDER_ID + " = ? AND " + SYN_STATE + " = ?",
				params);
	}

	public int updateAfterCommit(AbstractTableDTO dto, boolean isCommited) {
		SaleOrderDTO saleDTO = (SaleOrderDTO) dto;
		ContentValues value = initDataRowForEdit(saleDTO, isCommited);
		String[] params = { "" + saleDTO.saleOrderId };
		return update(value, SALE_ORDER_ID + " = ? AND " + SYN_STATE + " = 0 ",
				params);
	}

	public long delete(AbstractTableDTO dto) {
		SaleOrderDTO saleDTO = (SaleOrderDTO) dto;
		String[] params = { "" + saleDTO.saleOrderId, "" + saleDTO.synState };
		return delete(SALE_ORDER_ID + " = ? AND " + SYN_STATE + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: DoanDM replaced
	 * @param id
	 * @return: SaleOrderDTO
	 * @throws:
	 */
	public SaleOrderDTO getSaleById(long id) {
		SaleOrderDTO SaleOrderDTO = null;
		Cursor c = null;
		try {
			String[] params = { "" + id };
			c = query(SALE_ORDER_ID + " = ?", params, null, null, null);

			if (c != null) {
				if (c.moveToFirst()) {
					SaleOrderDTO = initSaleOrderDTOFromCursor(c);
				}
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			}
		}
		return SaleOrderDTO;
	}

	private SaleOrderDTO initSaleOrderDTOFromCursor(Cursor c) {
		SaleOrderDTO saleOrderDTO = new SaleOrderDTO();

		saleOrderDTO.amount = c.getLong(c.getColumnIndex(AMOUNT));
		saleOrderDTO.createDate = DateUtils.parseDateFromSqlLite(c.getString(c
				.getColumnIndex(CREATE_DATE)));
		saleOrderDTO.createUser = c.getString(c.getColumnIndex(CREATE_USER));
		saleOrderDTO.customerId = c.getLong(c.getColumnIndex(CUSTOMER_ID));
		saleOrderDTO.deliveryId = c
				.getString(c.getColumnIndex(DELIVERY_ID));
		saleOrderDTO.discount = c.getLong(c.getColumnIndex(DISCOUNT));
//		saleOrderDTO.invoiceNumber = c.getString(c
//				.getColumnIndex(INVOICE_NUMBER));
		saleOrderDTO.isVisitPlan = c.getInt(c.getColumnIndex(IS_VISIT_PLAN));
		saleOrderDTO.orderDate = DateUtils.parseDateFromSqlLite(c.getString(c
				.getColumnIndex(ORDER_DATE)));
		// SaleOrderDTO.userName = c.getString(c
		// .getColumnIndex(USER_NAME));
		saleOrderDTO.orderNumber = c.getString(c.getColumnIndex(ORDER_NUMBER));
		saleOrderDTO.orderType = c.getString(c.getColumnIndex(ORDER_TYPE));
		saleOrderDTO.saleOrderId = c.getLong(c.getColumnIndex(SALE_ORDER_ID));
		saleOrderDTO.shopId = c.getInt(c.getColumnIndex(SHOP_ID));
		saleOrderDTO.staffId = c.getInt(c.getColumnIndex(STAFF_ID));
		saleOrderDTO.approved = c.getInt(c.getColumnIndex(APPROVED));
		saleOrderDTO.totalWeight = c.getDouble(c.getColumnIndex(TOTAL_WEIGHT));
		// saleOrderDTO.importCode = c.getString(c.getColumnIndex(IMPORT_CODE));
		saleOrderDTO.priority = c.getLong(c.getColumnIndex(PRIORITY));
		saleOrderDTO.deliveryDate = DateUtils.parseDateFromSqlLite(c
				.getString(c.getColumnIndex(DELIVERY_DATE)));
		// ---
		// saleOrderDTO.SYN_OPERATOR =
		// c.getString(c.getColumnIndex(SYN_OPERATOR));
//		saleOrderDTO.synStatus = c.getInt(c.getColumnIndex(SYN_STATUS));
		saleOrderDTO.total = c.getLong(c.getColumnIndex(TOTAL));
		saleOrderDTO.updateDate = DateUtils.parseDateFromSqlLite(c.getString(c
				.getColumnIndex(UPDATE_DATE)));
		saleOrderDTO.updateUser = c.getString(c.getColumnIndex(UPDATE_USER));
		saleOrderDTO.synState = c.getInt(c.getColumnIndex(SYN_STATE));
		saleOrderDTO.orderSource = c.getInt(c.getColumnIndex(ORDER_SOURCE));
		saleOrderDTO.totalDetail =  c.getInt(c.getColumnIndex(TOTAL_DETAIL));
		saleOrderDTO.shopCode =  c.getString(c.getColumnIndex(SHOP_CODE));
		saleOrderDTO.totalQuantity =  c.getLong(c.getColumnIndex(TOTAL_QUANTITY));
		
		return saleOrderDTO;
	}

	/**
	 * Init value sale_order_table
	 * 
	 * @author : BangHN since : 3:35:36 PM
	 */
	private ContentValues initDataRow(SaleOrderDTO dto) {
		ContentValues editedValues = new ContentValues();

		// not null field
		if (dto.saleOrderId > 0) {
			editedValues.put(SALE_ORDER_ID, dto.saleOrderId);
		}
		if (dto.shopId > 0) {
			editedValues.put(SHOP_ID, dto.shopId);
		}
		if (!StringUtil.isNullOrEmpty(dto.orderNumber)) {
			editedValues.put(ORDER_NUMBER, dto.orderNumber);
		}

		if (!StringUtil.isNullOrEmpty(dto.orderDate)) {
			editedValues.put(ORDER_DATE, dto.orderDate);
		}
		if (dto.customerId > 0) {
			editedValues.put(CUSTOMER_ID, dto.customerId);
		}

		if (dto.staffId > 0) {
			editedValues.put(STAFF_ID, dto.staffId);
		}
		if (!StringUtil.isNullOrEmpty(dto.deliveryId)) {
			editedValues.put(DELIVERY_ID, dto.deliveryId);
		}
//		if (dto.amount > 0) {
			editedValues.put(AMOUNT, dto.amount);
//		}
//		if (dto.total > 0) {
			editedValues.put(TOTAL, dto.total);
//		}
		if (!StringUtil.isNullOrEmpty(dto.createUser)) {
			editedValues.put(CREATE_USER, dto.createUser);
		}

		if (!StringUtil.isNullOrEmpty(dto.createDate))
			editedValues.put(CREATE_DATE, dto.createDate);

		// can null field
		if (dto.orderType != null) {
			editedValues.put(ORDER_TYPE, dto.orderType);
		} else {
			editedValues.put(ORDER_TYPE, "");
		}

//		if (dto.invoiceNumber != null) {
//			editedValues.put(INVOICE_NUMBER, dto.invoiceNumber);
//		} else {
//			editedValues.put(INVOICE_NUMBER, "");
//		}

		if (dto.updateUser != null) {
			editedValues.put(UPDATE_USER, dto.updateUser);
		} else {
			editedValues.put(UPDATE_USER, "");
		}

		if (dto.updateDate != null) {
			editedValues.put(UPDATE_DATE, dto.updateDate);
		} else {
			editedValues.put(UPDATE_DATE, "");
		}

		if (dto.deliveryDate != null) {
			editedValues.put(DELIVERY_DATE, dto.deliveryDate);
		} else {
			editedValues.put(DELIVERY_DATE, "");
		}

		if (dto.priority >= 0) {
			editedValues.put(PRIORITY, dto.priority);
		}

		editedValues.put(DISCOUNT, dto.discount);
		editedValues.put(TYPE, dto.type);
		editedValues.put(IS_VISIT_PLAN, dto.isVisitPlan);
		editedValues.put(ORDER_SOURCE, dto.orderSource);
		editedValues.put(APPROVED, dto.approved);
		editedValues.put(SYN_STATE, dto.synState);
		editedValues.put(TOTAL_WEIGHT, dto.totalWeight);
		editedValues.put(TOTAL_DETAIL, dto.totalDetail);
		editedValues.put(SHOP_CODE, dto.shopCode);
		editedValues.put(TOTAL_QUANTITY, dto.totalQuantity);
		return editedValues;
	}

	private ContentValues initDataRowForEdit(SaleOrderDTO dto,
			boolean isCommited) {
		ContentValues editedValues = new ContentValues();

		if (!StringUtil.isNullOrEmpty(dto.deliveryId)) {
			editedValues.put(DELIVERY_ID, dto.deliveryId);
		}
		if (dto.amount > 0)
			editedValues.put(AMOUNT, dto.amount);
		if (dto.total > 0)
			editedValues.put(TOTAL, dto.total);

		if (dto.invoiceNumber != null) {
//			editedValues.put(INVOICE_NUMBER, dto.invoiceNumber);
		}

		if (dto.updateUser != null) {
			editedValues.put(UPDATE_USER, dto.updateUser);
		}

		if (dto.updateDate != null) {
			editedValues.put(UPDATE_DATE, dto.updateDate);
		} else {
			editedValues.put(UPDATE_DATE, "");
		}
		if (dto.discount > 0)
			editedValues.put(DISCOUNT, dto.discount);
//		if (dto.state > 0)
//			editedValues.put(STATE, dto.state);
		if (dto.isVisitPlan > 0)
			editedValues.put(IS_VISIT_PLAN, dto.isVisitPlan);
//		if (dto.isSend > 0)
//			editedValues.put(IS_SEND, dto.isSend);
//		if (dto.synStatus > 0)
//			editedValues.put(SYN_STATUS, dto.synStatus);
		if (isCommited) {
			editedValues.put(SYN_STATE, TRANSFERED_STATUS);
		} else
			editedValues.put(SYN_STATE, dto.synState);
		
		return editedValues;
	}

	/**
	 * 
	*  lay san luong 2 thang
	*  @author: YenNTH
	*  @param customerId
	*  @param shopId
	*  @return
	*  @throws Exception
	*  @return: long
	*  @throws:
	 */
	public long getAverageSalesInMonth2(String customerId,
			String shopId) throws Exception {
		long averageSale = 0;
		Cursor cursor = null;
		String dateFirstMonth = DateUtils.getFirstDateOfNumberPreviousMonthWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT,0);

		List<String> params = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT SUM(sim.quantity) AS quantity ");
		sql.append("FROM   rpt_sale_in_month sim ");
		sql.append("WHERE  sim.customer_id = ? ");
		params.add(customerId);
		sql.append("       AND sim.shop_id = ? ");
		params.add(shopId);
		sql.append("       AND sim.status = 1 ");
		sql.append("       AND substr(sim.MONTH,0,11) = ?");
		params.add(dateFirstMonth);
		
		try {
			String[] arrParam = new String[params.size()];
			for (int i = 0; i < params.size(); i++) {
				arrParam[i] = params.get(i);
			}
			cursor = rawQuery(sql.toString(), arrParam);

			if (cursor != null) {
				if (cursor.moveToFirst()) {
					averageSale = cursor.getLong(cursor
							.getColumnIndex("quantity"));
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
		return averageSale;
	}

	/**
	 * Lay thong tin doanh so 3 thang gan day
	 * 
	 * @author : BangHN since : 1.0
	 */
	public ArrayList<SaleInMonthDTO> getAverageSalesIn3MonthAgo(
			String customerId) throws Exception {
		StringBuilder var1 = new StringBuilder();
		List<String> params = new ArrayList<String>();
		Cursor cursor = null;
		String dateNow= DateUtils.now();
		ArrayList<SaleInMonthDTO> saleIn3Month = new ArrayList<SaleInMonthDTO>();
		String dateLastOneLastMonth = DateUtils.getLastDateOfNumberPreviousMonthWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT, -1);
		String dateFirstThreeLastMonth = DateUtils.getFirstDateOfNumberPreviousMonthWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT,-3);

		var1.append("SELECT SUM(sim.quantity)         AS quantity, ");
		var1.append("       Strftime('%m', sim.month) AS month ");
		var1.append("FROM   rpt_sale_in_month sim ");
		var1.append("WHERE  sim.customer_id = ? ");
		params.add(customerId);
		var1.append("       AND sim.quantity > 0 ");
		var1.append("       AND sim.status = 1 ");
		var1.append(" and substr(sim.MONTH,0,11) <= ?");
		params.add(dateLastOneLastMonth);
		var1.append(" and substr(sim.MONTH,0,11) >= ?");
		params.add(dateFirstThreeLastMonth);
		var1.append("GROUP  BY month ");
		

		try {
			String[] arrParam = new String[params.size()];
			for (int i = 0; i < params.size(); i++) {
				arrParam[i] = params.get(i);
			}
			cursor = rawQuery(var1.toString(), arrParam);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					do {
						SaleInMonthDTO dto = new SaleInMonthDTO();
						dto.parseSaleInMonth(cursor);
						saleIn3Month.add(dto);
					} while (cursor.moveToNext());
				}
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
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
		return saleIn3Month;
	}

	/**
	 * Lay so mat hang khac nhau ban trong thang cho khach hang (sku)
	 * 
	 * @author : BangHN since : 1.0
	 * @throws Exception
	 */
	public int getSKUOfCustomerInMonth(String customerId, String shopId) throws Exception {
		int sku = 0;
		Cursor cursor = null;
		List<String> params = new ArrayList<String>();
		String dateFirstMonth = DateUtils.getFirstDateOfNumberPreviousMonthWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT,0);
		
		StringBuffer  sql = new StringBuffer();
		sql.append("SELECT Count (DISTINCT product_id) sku ");
		sql.append("FROM   rpt_sale_in_month sim ");
		sql.append("WHERE  sim.customer_id = ? and sim.shop_id = ? and sim.amount > 0");
		sql.append("	AND substr(sim.month,0,11) >= ?");
		
		params.add(customerId);
		params.add(shopId);
		params.add(dateFirstMonth);
		try {
			String[] arrParam = new String[params.size()];
			for (int i = 0; i < params.size(); i++) {
				arrParam[i] = params.get(i);
			}
			cursor = rawQuery(sql.toString(), arrParam);

			if (cursor != null) {
				if (cursor.moveToFirst()) {
					sku = cursor.getInt(cursor.getColumnIndex("sku"));
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
		return sku;
	}

	/**
	 * lay so don hang cua khach hang dat trong thang
	 * 
	 * @author : BangHN since : 1.0
	 * @throws Exception
	 */
	public int getNumberOrdersInMonth(String customerId) throws Exception {
		int numOrders = 0;
		Cursor cursor = null;
		List<String> params = new ArrayList<String>();
		String dateNow = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		String dateFirstMonth = DateUtils.getFirstDateOfNumberPreviousMonthWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT,0);
		
		StringBuffer  sql = new StringBuffer();
		sql.append("SELECT Count(1) AS COUNT ");
		sql.append("FROM   (SELECT substr(s.order_date,0,11), ");
		sql.append("               Sum(CASE ");
		sql.append("                     WHEN s.order_type = 'CM' THEN -s.total ");
		sql.append("                     WHEN s.order_type = 'CO' THEN -s.total ");
		sql.append("                     ELSE s.total ");
		sql.append("                   end) AS tong ");
		sql.append("        FROM   sale_order s ");
		sql.append("        WHERE  s.customer_id = ? ");
		sql.append("               AND s.shop_id = ? ");
		sql.append("               AND ( ( s.[order_type] != 'CM' ");
		sql.append("                       AND s.[order_type] != 'CO' ");
		sql.append("                       AND s.[approved] = 1 ");
		sql.append("                       AND substr(s.order_date,0,11) < ? ");
		sql.append("                       AND substr(s.order_date,0,11) >= ?) ");
		sql.append("                      OR ( s.[order_type] != 'CM' ");
		sql.append("                           AND s.[order_type] != 'CO' ");
		sql.append("                           AND s.[approved] != 2 ");
		sql.append("                           AND substr(s.order_date,0,11) = ?) ) ");
		sql.append("        GROUP  BY substr(s.order_date,0,11)) ");
		sql.append("WHERE  tong > 0 ");
		
		String shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
		params.add(customerId);
		params.add(shopId);
		params.add(dateNow);
		params.add(dateFirstMonth);
		params.add(dateNow);
		try {
			String[] arrParam = new String[params.size()];
			for (int i = 0; i < params.size(); i++) {
				arrParam[i] = params.get(i);
			}
			cursor = rawQuery(sql.toString(), arrParam);

			if (cursor != null) {
				if (cursor.moveToFirst()) {
					numOrders = cursor.getInt(cursor.getColumnIndex("COUNT"));
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
		return numOrders;
	}

	/**
	 * 
	 * lay danh sach don dat hang trong ngay.
	 * @author: thuattq
	 * @return : List<SaleOrderViewDTO>
	 * @throws :
	 */
	public ListOrderMngDTO getSalesOrderInDate(String oderDate, String customer_code, String customer_name, int status, String staffId, String staffOwnerId, String shop_id,
			boolean bApproved, int page) throws Exception {

		ListOrderMngDTO listSaleOrderViewDTO = new ListOrderMngDTO();
		StringBuilder sql = new StringBuilder();
		Cursor cursor = null;
		List<String> params = new ArrayList<String>();

		ArrayList<String> strings = new ArrayList<String>();
		StringBuilder stringBuilder = new StringBuilder();
		if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSNPP) {
			if(StringUtil.isNullOrEmpty(staffId)) {
				STAFF_TABLE staff_table = new STAFF_TABLE(mDB);
				strings = staff_table.getStaffRecursiveReverse(staffId);
			}
		} else if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSTINH) {
			if(StringUtil.isNullOrEmpty(staffId)) {
				STAFF_TABLE staff_table = new STAFF_TABLE(mDB);
				if(!StringUtil.isNullOrEmpty(staffOwnerId)) {
					strings = staff_table.getStaffRecursiveReverseByGSNPP(staffOwnerId);
				} else {
					strings =  staff_table.getStaffGroupTBHV(String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
//					strings = staff_table.getStaffRecursiveReverseTBHV(String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
				}
			}
		}
		if(strings.size() > 0) {
			for (String s:
				 strings) {
				if(stringBuilder.length() > 0) {
					stringBuilder.append(",");
				}
				stringBuilder.append(s);
			}
		}
		
		sql.append("SELECT DISTINCT Strftime('%d/%m/%Y %H:%M:%S', SO.order_date) AS ORDER_DATE, ");
		sql.append("                SO.create_date                             AS CREATE_DATE, ");
		sql.append("                CT.customer_code                          AS CUSTOMER_CODE, ");
		sql.append("                CT.short_code                          AS SHORT_CODE, ");
		sql.append("                CT.customer_id                            AS CUSTOMER_ID, ");
		sql.append("                CT.customer_name                          AS CUSTOMER_NAME, ");
		sql.append("                CT.channel_type_id                       AS CUSTOMER_TYPE_ID, ");
		sql.append("                CT.shop_id                                AS SHOP_ID, ");
		sql.append("                SO.total                                  AS TOTAL, ");
		sql.append("                SO.priority                               AS PRIORITY, ");
		sql.append("                SO.approved                               AS APPROVED, ");
		sql.append("                SO.is_visit_plan                          AS IS_VISIT_PLAN, ");
		sql.append("                SO.order_number                           AS ORDER_NUMBER, ");
		sql.append("                CT.address                                AS STREET, ");
		sql.append("                S.staff_code                                AS STAFF_CODE, ");
		sql.append("                S.staff_name                                AS STAFF_NAME, ");
		sql.append("                SO.order_type                             AS ORDER_TYPE, ");
		sql.append("                SO.sale_order_id                          AS SALE_ORDER_ID, ");
		sql.append("                SO.description                            AS DESCRIPTION, ");
		sql.append("                SO.syn_state                              AS SYN_STATE, ");
		sql.append("                SO.staff_id                               AS STAFF_ID, ");
		sql.append("                SO.create_user                            AS CREATE_USER, ");
		sql.append("                SO.destroy_code                           AS DESTROY_CODE, ");
		sql.append("                ( CASE ");
		sql.append("                    WHEN SO.approved = 2 THEN 0 ");
		sql.append("                    WHEN SO.approved = 0 THEN 1 ");
		sql.append("                    WHEN SO.approved = 1 THEN 2 ");
		sql.append("                    WHEN (SO.approved = 3 and so.[DESTROY_CODE] like '%AUTO_DEL%') THEN 3 ");
		sql.append("                  END )                                   AS TMP, ");
		sql.append("                  AP.AP_PARAM_CODE ");
		sql.append("FROM   sale_order SO ");
		sql.append("       INNER JOIN customer CT ON SO.customer_id = CT.customer_id ");
		sql.append("       INNER JOIN staff S ON SO.staff_id = S.staff_id ");
		sql.append("       LEFT JOIN AP_PARAM AP ON SO.PRIORITY = AP.AP_PARAM_ID ");
		sql.append("WHERE  1 = 1 ");
		if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES) {
			sql.append("       AND SO.staff_id = ? ");
			sql.append("       AND SO.shop_id = ? ");
			params.add(staffId.toLowerCase());
			params.add(shop_id.toLowerCase());
		} else if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSNPP) {
			if(!StringUtil.isNullOrEmpty(staffId)) {
				sql.append("       AND SO.staff_id = ? ");
				sql.append("       AND SO.shop_id = ? ");
				params.add(staffId.toLowerCase());
				params.add(shop_id.toLowerCase());
			} else {
				sql.append("       AND SO.staff_id in ( ");
				sql.append(stringBuilder.toString());
				sql.append("       ) ");
				sql.append("       AND SO.shop_id = ? ");
				params.add(shop_id.toLowerCase());
			}
		} else if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSTINH) {
			if(!StringUtil.isNullOrEmpty(staffId)) {
				sql.append("       AND SO.staff_id = ? ");
				params.add(staffId.toLowerCase());
			} else {
				sql.append("       AND SO.staff_id in ( ");
				sql.append(stringBuilder.toString());
				sql.append("       ) ");
			}
		}
		sql.append("       AND AP.STATUS = 1 ");
//		sql.append("       AND Ifnull(Date(SO.order_date) >= Date('NOW', 'localtime', '-60 day'), 0) ");

		// tim kiem theo ma KH
		if (!StringUtil.isNullOrEmpty(customer_code)) {
			customer_code = StringUtil.escapeSqlString(customer_code);
			customer_code = DatabaseUtils.sqlEscapeString("%" + customer_code + "%");
			customer_code = customer_code.substring(1, customer_code.length()-1);
			sql.append("	and upper(substr(CT.CUSTOMER_CODE,1,3)) like upper(?) escape '^' ");
			params.add(customer_code);

		}
		// tim kiem theo ten/dia chi
		if (!StringUtil.isNullOrEmpty(customer_name)) {
			customer_name = StringUtil.getEngStringFromUnicodeString(customer_name);
			customer_name = StringUtil.escapeSqlString(customer_name);
			customer_name = DatabaseUtils.sqlEscapeString("%" + customer_name + "%");
			customer_name = customer_name.substring(1, customer_name.length()-1);
			sql.append("	and ((upper(CT.NAME_TEXT) like upper(?) escape '^')");
			params.add(customer_name);
			sql.append(" or (upper(STREET) like upper(?) escape '^') ");
			params.add(customer_name);
			sql.append(" or (upper(ADDRESS) like upper(?) escape '^')) ");
			params.add(customer_name);
		}
		if(!StringUtil.isNullOrEmpty(oderDate)){
			sql.append(" AND substr(SO.ORDER_DATE,0,11) = substr(?,0,11) ");
			params.add(oderDate);
		}
		if(status>=0){
			if(status==ListOrderView.STATUS_SUCCESS){// thanh cong
				sql.append(" AND SO.APPROVED = 1 AND SO.SYN_STATE = 2 ");
			}else if (status==ListOrderView.STATUS_OVERDATE) {//qua ngay khong duyet
				sql.append(" AND SO.APPROVED = 3 AND SO.SYN_STATE = 2 AND DESTROY_CODE like 'AUTO_DEL' ");
			}else if (status==ListOrderView.STATUS_DENY) {// tu choi
				sql.append(" AND SO.APPROVED = 2 AND SO.SYN_STATE = 2 ");
			}else if (status==ListOrderView.STATUS_WAITING_PROCESS) {// cho xu ly
				sql.append(" AND SO.APPROVED = 0 AND SO.SYN_STATE = 2  ");
			}else if (status== ListOrderView.STATUS_NOT_SEND) {// cho gui
				sql.append(" AND SO.SYN_STATE = 0 OR SO.SYN_STATE = 1");
			}
		}	
		sql.append(" ORDER BY SO.ORDER_DATE DESC, TMP ASC, SO.ORDER_NUMBER DESC  ");
		Cursor cTotal =null;
		try {
			String sqlTotal = " select count(*) as total_row from ("
					+ sql.toString() + ")";
			// lay tong so
			cTotal = rawQuery(sqlTotal, params.toArray(new String[params.size()]));
			if (cTotal != null) {
				cTotal.moveToFirst();
				listSaleOrderViewDTO.total = cTotal.getInt(0);
			} else {
				listSaleOrderViewDTO.total = 0;
			}
		} catch (Exception ex) {
			ServerLogger.sendLog("getSalesOrderInDate", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
		} finally {
			if (cTotal != null) {
				try {
					cTotal.close();
				} catch (Exception ex) {
				}
			}
		}
		try {
			sql.append(" limit ? offset ? ");
			params.add(String.valueOf(Constants.NUM_ITEM_PER_PAGE));
			params.add(String.valueOf(page * Constants.NUM_ITEM_PER_PAGE));
			cursor = rawQuery(sql.toString(), params.toArray(new String[params.size()]));
			if (cursor != null) {
				listSaleOrderViewDTO.listDTO = SaleOrderViewDTO.initListDataFromCursor(cursor);
			}

		} catch (Exception ex) {
			ServerLogger.sendLog("getSalesOrderInDate", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (Exception ex) {
				}
			}
		}
		return listSaleOrderViewDTO;
	}
	
	/**
	 * 
	*  Lay ngay dat hang cuoi cung
	*  @author: Nguyen Thanh Dung
	*  @param dto
	*  @return
	*  @return: String
	*  @throws:
	 */
	public String getPreviousLastOrderDate(SaleOrderViewDTO dto) {
		StringBuilder sqlTotal = new StringBuilder();
		String dateNow= DateUtils.now();
		sqlTotal.append("select order_date as ORDER_DATE from sale_order so ");
		sqlTotal.append(" where customer_id = ?");
		sqlTotal.append(" and create_user = ?");
		
		sqlTotal.append(" AND IFNULL( DATE(SO.ORDER_DATE) = DATE(?), 0) ");
		sqlTotal.append(" and syn_state != 3 and syn_state != 4 ");
		sqlTotal.append(" order by order_date desc ");
		
		ArrayList<String> params = new ArrayList<String>();
		params.add(String.valueOf(dto.getCustomerId()));
		params.add(dto.saleOrder.createUser);
		params.add(dateNow);
		Cursor cursor = null;
		String result = "";
		ArrayList<String> listOrderDate = new ArrayList<String>();
		try {
			// lay tong so
			cursor = rawQuery(sqlTotal.toString(),params.toArray(new String[params.size()]));
			if (cursor != null) {
				while(cursor.moveToNext()) {
					String orderDate=cursor.getString(cursor.getColumnIndexOrThrow(SALE_ORDER_TABLE.ORDER_DATE));
					listOrderDate.add(orderDate);
				}
			}
		} catch (Exception ex) {
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (Exception ex) {
				}
			}
		}
		
		if(listOrderDate.size() == 1) {//Co 1 don hang cuoi cung
			result = "";
		} else if (listOrderDate.size() >= 2) {
			if(dto.saleOrder.orderDate.equals(listOrderDate.get(0))) {
				result = listOrderDate.get(1);
			} else {
				result = dto.saleOrder.orderDate;
			}
		}
		
		return result;
	}

	/**
	 * 
	 * Lay so don hang chua chuyen
	 * @author: thuattq
	 * @return : List<SaleOrderViewDTO>
	 * @throws :
	 */
	public int getTotalSalesOrderNotSend(String staffId, String shop_id){
		int count = 0;
		StringBuilder sql = new StringBuilder();
		Cursor cursor = null;
		List<String> params = new ArrayList<String>();
		String dateNow = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);

		sql.append(" SELECT count(*) as Total ");
		sql.append(" FROM SALE_ORDER SO ");
		sql.append(" WHERE SO.STAFF_ID = ? ");
		sql.append(" 	AND SO.SHOP_ID = ? ");
		sql.append(" 	AND SO.APPROVED = 2 ");
		sql.append("  AND IFNULL(substr(SO.ORDER_DATE,0,11) >= ?, 0)");
		params.add(staffId);
		params.add(shop_id);
		params.add(dateNow);

		try {
			String[] arrParam = new String[params.size()];
			for (int i = 0; i < params.size(); i++) {
				arrParam[i] = params.get(i);
			}
			cursor = rawQuery(sql.toString(), arrParam);
			if (cursor != null) {
				cursor.moveToFirst();
				count = cursor.getInt(cursor.getColumnIndexOrThrow("Total"));
			}

		} catch (Exception ex) {
			ServerLogger.sendLog("getTotalSalesOrderNotSend", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (Exception ex) {
				}
			} else {

			}
		}
		return count;
	}

	@Override
	protected long insert(AbstractTableDTO dto) {
		ContentValues value = initDataRow((SaleOrderDTO) dto);
		return insert(null, value);
	}

	/**
	 * Lay tong so don hang gan day cua khach hang
	 * 
	 * @author : BangHN since : 3:41:04 PM
	 * @throws Exception
	 */
	public int getCountLastSaleOrders(String customerId, String shopId,
			String staffId) throws Exception {
		Cursor cursor = null;
		int count = 0;
		StringBuilder sql = new StringBuilder();
		List<String> params = new ArrayList<String>();
		sql.append(" select count((select count(product_id)");
		sql.append(" from sales_order_detail sod where sod.sale_order_id = s.sale_order_id)) as count");
		sql.append(" from sales_order s");
		sql.append(" WHERE   s.customer_id = ?");
		sql.append(" and   s.shop_id = ?");
		sql.append(" and   s.staff_id = ?");
		params.add(customerId);
		params.add(shopId);
		params.add(staffId);
		try {
			String[] arrParam = new String[params.size()];
			for (int i = 0; i < params.size(); i++) {
				arrParam[i] = params.get(i);
			}
			cursor = rawQuery(sql.toString(), arrParam);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					count = cursor.getInt(cursor.getColumnIndex("count"));
				}
			}
			return count;
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
	}

	/**
	 * Lay nhung don hang gan day cua khach hang
	 * 
	 * @author : BangHN since : 1:50:02 PM
	 * @throws Exception
	 */
	public ArrayList<SaleOrderCustomerDTO> getLastSaleOrders(String customerId,
			String shopId, int page, int numTop) throws Exception {
		Cursor cursor = null;
		ArrayList<SaleOrderCustomerDTO> result = new ArrayList<SaleOrderCustomerDTO>();
		
		List<String> params = new ArrayList<String>();
		String dateNow = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		
		StringBuffer  sql = new StringBuffer();
		sql.append("SELECT s.sale_order_id, ");
		sql.append("       Strftime('%d-%m-%Y %H:%M:%S', s.order_date) AS ORDER_DATE, ");
		sql.append("       s.order_number, ");
		sql.append("       s.total, ");
		sql.append("       (SELECT Count(DISTINCT product_id) ");
		sql.append("        FROM   sale_order_detail sod ");
		sql.append("        WHERE  sod.sale_order_id = s.sale_order_id ");
		sql.append("               AND sod.is_free_item = 0)           AS SKU ");
		sql.append("FROM   sale_order s ");
		sql.append("WHERE  s.customer_id = ? ");
		sql.append("       AND s.shop_id = ? ");
		sql.append("       AND s.type = 1 ");
		sql.append("       AND s.total > 0 ");
//		sql.append("       AND ( ( s.[order_type] != 'CM' ");
//		sql.append("               AND s.[order_type] != 'CO' ");
		sql.append("       AND ( (  ");
		sql.append("               s.[approved] = 1 ");
		sql.append("               AND substr(s.order_date,0,11) < ?) ");
//		sql.append("              OR ( s.[order_type] != 'CM' ");
//		sql.append("                   AND s.[order_type] != 'CO' ");
		sql.append("              OR ( ");
		sql.append("                   s.[approved] in (0, 1) ");
		sql.append("                   AND substr(s.order_date,0,11) = ?) ) ");
		sql.append("ORDER  BY s.order_date DESC, ");
		sql.append("          s.order_number DESC ");
		sql.append("LIMIT  ?, ? ");
		
		params.add(customerId);
		params.add(shopId);
		params.add(dateNow);
		params.add(dateNow);
		params.add("" + (page - 1) * numTop);
		params.add("" + numTop);
		try {
			String[] arrParam = new String[params.size()];
			for (int i = 0; i < params.size(); i++) {
				arrParam[i] = params.get(i);
			}
			cursor = rawQuery(sql.toString(), arrParam);

			if (cursor != null) {
				if (cursor.moveToFirst()) {
					do {
						SaleOrderCustomerDTO saleOrder = SaleOrderCustomerDTO
								.initOrderFromCursor(cursor);
						if (saleOrder != null)
							result.add(saleOrder);
					} while (cursor.moveToNext());
				}
			}
			return result;
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
	}

	/**
	 * Cap nhat trang thai dong bo don hang
	 * 
	 * @author: TruongHN
	 * @return: int
	 * @throws:
	 */
	public long updateState(String saleOrderId, int synState) {
		ContentValues value = new ContentValues();
		value.put(SYN_STATE, synState);
		String[] params = { "" + saleOrderId };
		return update(value, SALE_ORDER_ID + " = ? ", params);
	}

	/**
	 * Lay ds don hang chua send thanh cong
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public NoSuccessSaleOrderDto getNoSuccessOrderList(ArrayList<String> idList) {
		NoSuccessSaleOrderDto dto = null;
		Cursor c = null;

		StringBuffer sql = new StringBuffer();
		sql.append("select so.*, ct.customer_name as CUSTOMER_NAME, substr(ct.customer_code, 0, 4) as CUSTOMER_CODE from sales_order so, customer ct");
		sql.append("	where so.sale_order_id in (");

		if (idList.size() > 1) {
			for (int i = 0; i < idList.size() - 1; i++) {
				sql.append("?,");
			}
		}
		sql.append("?)");
		sql.append("		and so.customer_id = ct.customer_id");
		sql.append("		and ct.status = 1");

		try {
			c = rawQueries(sql.toString(), idList);
			if (c != null) {
				dto = new NoSuccessSaleOrderDto();
				if (c.moveToFirst()) {
					do {
						NoSuccessSaleOrderItem item = dto
								.newNoSuccessSaleOrderItem();
						item.initNoSuccessSaleOrderItem(c);
						dto.itemList.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return dto;
	}
	
	/**
	*  Lay ds don hang loi: bao gom don hang chua goi, don hang loi va don hang tra ve
	*  @author: TruongHN
	*  @param staffId
	*  @return: NoSuccessSaleOrderDto
	*  @throws:
	 */
	public NoSuccessSaleOrderDto getAllOrderFail(long staffId) {
		NoSuccessSaleOrderDto dto = null;

		StringBuffer  var1 = new StringBuffer();
		var1.append("SELECT DISTINCT so.sale_order_id               AS SALE_ORDER_ID, ");
		var1.append("                so.order_number                AS ORDER_NUMBER, ");
		var1.append("                so.order_date                  AS ORDER_DATE, ");
		var1.append("                so.total                       AS TOTAL, ");
		var1.append("                so.syn_state                   AS SYN_STATE, ");
		var1.append("                so.description                 AS DESCRIPTION, ");
		var1.append("                ct.customer_name               AS CUSTOMER_NAME, ");
		var1.append("                so.approved                    AS APPROVED, ");
		var1.append("                Substr(ct.customer_code, 0, 4) AS CUSTOMER_CODE ");
		var1.append("FROM   sale_order so, ");
		var1.append("       customer ct ");
		var1.append("WHERE  so.staff_id = ? ");
		var1.append("       AND ( so.sale_order_id IN (SELECT table_id ");
		var1.append("                                  FROM   log_table ");
		var1.append("                                  WHERE  table_type = 3 ");
		var1.append("                                         AND state IN ( 0, 1, 3, 4 ) ");
		var1.append("                                         AND Date(create_date) >= ");
		var1.append("                                             Date('now', 'localtime')) ) ");
		var1.append("       AND so.customer_id = ct.customer_id ");
		var1.append("UNION ");
		var1.append("SELECT DISTINCT so.sale_order_id               AS SALE_ORDER_ID, ");
		var1.append("                so.order_number                AS ORDER_NUMBER, ");
		var1.append("                so.order_date                  AS ORDER_DATE, ");
		var1.append("                so.total                       AS TOTAL, ");
		var1.append("                so.syn_state                   AS SYN_STATE, ");
		var1.append("                so.description                 AS DESCRIPTION, ");
		var1.append("                ct.customer_name               AS CUSTOMER_NAME, ");
		var1.append("                so.approved                    AS APPROVED, ");
		var1.append("                Substr(ct.customer_code, 0, 4) AS CUSTOMER_CODE ");
		var1.append("FROM   sale_order so, ");
		var1.append("       customer ct ");
		var1.append("WHERE  so.staff_id = ? ");
		var1.append("       AND (( so.syn_state = 2 ");
		var1.append("       	AND so.approved = 2 ");
		var1.append("           AND Date(so.create_date) = Date('now', 'localtime') )) ");
		var1.append("       AND so.customer_id = ct.customer_id ");
		String staff = String.valueOf(staffId);
		String[] params = {staff, staff};
		Cursor c = null;
		try {
			c = rawQuery(var1.toString(), params);
			if (c != null){
				dto = new NoSuccessSaleOrderDto();
				if (c.moveToFirst()) {
					do {
						NoSuccessSaleOrderItem item = dto.newNoSuccessSaleOrderItem();
						item.initNoSuccessSaleOrderItem(c);
						dto.itemList.add(item);
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
				// TODO: handle exception
				VTLog.i("getAllOrderFail", e.getMessage());
			}
		}
		return dto;
	}
	/**
	*  Lay so don hang tra ve tu NPP trong ngay
	*  @author: TruongHN
	*  @return: int
	*  @throws:
	 */
	public int getOrderReturnNPPInDay(long staffId) {
		int count = 0;
		StringBuffer  var1 = new StringBuffer();
		var1.append("SELECT Count(*) AS count ");
		var1.append("FROM   sale_order ");
		var1.append("WHERE  staff_id = ? ");
		var1.append("       AND syn_state = 2 ");
		var1.append("       AND approved = 2 ");
		var1.append("       AND Date(create_date) = Date('now', 'localtime') ");
		
		String[] params = {String.valueOf(staffId)};
		Cursor cursor = null;
		try {
			cursor = rawQuery(var1.toString(), params);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					count = cursor.getInt(cursor.getColumnIndex("count"));
				}
			}
		} catch (Exception ex) {
		} finally {
			try {
				if (cursor != null) {
					cursor.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
				VTLog.i("getOrderReturnNPPInDay", e.getMessage());
			}
		}
		return count;
	}

	/**
	 * delete cac don hang truoc 3 thang
	 *
	 * @author banghn
	 * @return
	 */
	public long deleteOldSaleOrder() {
		long success = -1;
		try {
			String startOfMonth = DateUtils
					.getFirstDateOfNumberPreviousMonthWithFormat(
							DateUtils.DATE_FORMAT_SQL_DEFAULT, -2);
			String[] params = {startOfMonth};

			//mDB.beginTransaction();
			//xoa don hang giu lai trong 3 thang hien tai
			StringBuffer sqlDel = new StringBuffer();
			sqlDel.append("  ?  > substr(ORDER_DATE,0,11) ");
			delete(sqlDel.toString(), params);
			success = 1;
			//mDB.setTransactionSuccessful();
		} catch (Throwable e) {
			e.printStackTrace();
			success = -1;
		} finally {
//			if (mDB != null && mDB.inTransaction()) {
//				mDB.endTransaction();
//			}
			SQLUtils.getInstance().isProcessingTrans = false;
		}
		return success;
	}

}
