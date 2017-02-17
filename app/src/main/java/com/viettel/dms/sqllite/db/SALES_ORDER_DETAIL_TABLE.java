/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.text.TextUtils;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.SaleOrderDetailDTO;
import com.viettel.dms.dto.db.StockTotalDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.dto.view.AutoCompleteFindProductSaleOrderDetailViewDTO;
import com.viettel.dms.dto.view.CategoryCodeDTO;
import com.viettel.dms.dto.view.FindProductSaleOrderDetailViewDTO;
import com.viettel.dms.dto.view.ListFindProductSaleOrderDetailViewDTO;
import com.viettel.dms.dto.view.ListOrderViewDTO;
import com.viettel.dms.dto.view.ListRemainProductDTO;
import com.viettel.dms.dto.view.OrderDetailViewDTO;
import com.viettel.dms.dto.view.PGOrderViewDTO;
import com.viettel.dms.dto.view.ProgrameForProductDTO;
import com.viettel.dms.dto.view.RemainProductViewDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.MeasuringTime;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * Thong tin chi tiet don hang
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class SALES_ORDER_DETAIL_TABLE extends ABSTRACT_TABLE {
	// id chi tiet don hang
	public static final String SALE_ORDER_DETAIL_ID = "SALE_ORDER_DETAIL_ID";
	// ma san pham
	public static final String PRODUCT_ID = "PRODUCT_ID";
	// so luong
	public static final String QUANTITY = "QUANTITY";
	// gia lich su
	public static final String PRICE_ID = "PRICE_ID";
	// % khuyen mai
	public static final String DISCOUNT_PERCENT = "DISCOUNT_PERCENT";
	// so tien khuyen mai
	public static final String DISCOUNT_AMOUNT = "DISCOUNT_AMOUNT";
	// 0: hang ban, 1: hang KM
	public static final String IS_FREE_ITEM = "IS_FREE_ITEM";
	// ma chuong trinh khuyen mai
	public static final String PROGRAM_CODE = "PROGRAM_CODE";
	// loai khuyen mai hay chuong trinh trung bay: 0 - CTKM tinh tu dong, 1 -
	// CTKM tinh manual, 2 - CTTB, 3: doi, 4: huy, 5: tra
	public static final String PROGRAM_TYPE = "PROGRAM_TYPE";
	// so tien
	public static final String AMOUNT = "AMOUNT";
	// id don dat hang
	public static final String SALE_ORDER_ID = "SALE_ORDER_ID";
	// gia ban
	public static final String PRICE = "PRICE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// ngay dat
	public static final String ORDER_DATE = "ORDER_DATE";
	// so luong KM max cua mat hang
	public static final String MAX_QUANTITY_FREE = "MAX_QUANTITY_FREE";
	// id NPP
	public static final String SHOP_ID = "SHOP_ID";
	// id nhan vien
	public static final String STAFF_ID = "STAFF_ID";
	// Thue VAT
	public static final String VAT = "VAT";
	// Trong luong
	public static final String TOTAL_WEIGHT = "TOTAL_WEIGHT";
	// ID hoa don VAT
	public static final String INVOICE_ID = "INVOICE_ID";
	// gia chua co VAT
	public static final String PRICE_NOT_VAT = "PRICE_NOT_VAT";
	// so tien KM toi da
	public static final String MAX_AMOUNT_FREE = "MAX_AMOUNT_FREE";
	//lưu loại KM (ví dụ ZV01, 02…)
	public static final String PROGRAME_TYPE_CODE = "PROGRAME_TYPE_CODE";

	public static final String TABLE_NAME = "SALE_ORDER_DETAIL";

	/**
	 * 
	 * tao va mo mot CSDL
	 * 
	 * @author: HieuNH
	 * @return
	 * @return: SQLiteUtil
	 * @throws:
	 */
	public SALES_ORDER_DETAIL_TABLE(SQLiteDatabase mDB) {
		this.columns = new String[] { SALE_ORDER_DETAIL_ID, PRODUCT_ID,
				QUANTITY, PRICE_ID, DISCOUNT_PERCENT, DISCOUNT_AMOUNT,
				IS_FREE_ITEM, PROGRAM_CODE, PROGRAM_TYPE, AMOUNT,
				SALE_ORDER_ID, PRICE, CREATE_USER, UPDATE_USER, CREATE_DATE,
				UPDATE_DATE, ORDER_DATE, MAX_QUANTITY_FREE, SHOP_ID, STAFF_ID,
				VAT, TOTAL_WEIGHT, INVOICE_ID, PRICE_NOT_VAT, MAX_AMOUNT_FREE, PROGRAME_TYPE_CODE,
				SYN_STATE };
		this.tableName = TABLE_NAME;
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;
	}

	@Override
	protected long insert(AbstractTableDTO dto) {
		ContentValues value = initDataRow((SaleOrderDetailDTO) dto);
		return insert(null, value);
	}

	/**
	 * Thay doi 1 dong cua CSDL
	 * 
	 * @author: TruongHN
	 * @return: int
	 * @throws:
	 */
	public int update(SaleOrderDetailDTO dto) {
		ContentValues value = initDataRow(dto);
		String[] params = { "" + dto.salesOrderDetailId, "" + dto.synState };
		return update(value, SALE_ORDER_DETAIL_ID + " = ? AND " + SYN_STATE
				+ " = ? ", params);
	}

	public long update(AbstractTableDTO dto) {
		SaleOrderDetailDTO saleOrder = (SaleOrderDetailDTO) dto;
		ContentValues value = initDataRow(saleOrder);
		String[] params = { "" + saleOrder.salesOrderDetailId,
				"" + saleOrder.synState };
		return update(value, SALE_ORDER_DETAIL_ID + " = ? AND " + SYN_STATE
				+ " = ? ", params);
	}

	public int updateAfterCommit(AbstractTableDTO dto, boolean isCommited) {
		SaleOrderDetailDTO saleOrder = (SaleOrderDetailDTO) dto;
		ContentValues value = initDataRowForEdit(saleOrder, isCommited);
		String[] params = { "" + saleOrder.salesOrderDetailId };
		return update(value, SALE_ORDER_DETAIL_ID + " = ? AND " + SYN_STATE
				+ " = 0 ", params);
	}

	public long delete(AbstractTableDTO dto) {
		SaleOrderDetailDTO saleDTO = (SaleOrderDetailDTO) dto;
		String[] params = { "" + saleDTO.salesOrderDetailId,
				"" + saleDTO.synState };
		return delete(SALE_ORDER_DETAIL_ID + " = ? AND " + SYN_STATE + " = ? ",
				params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: DoanDM replaced
	 * @param id
	 * @return: DisplayPrdogrameLvDTO
	 * @throws:
	 */
	public SaleOrderDetailDTO getRowById(String id) {
		SaleOrderDetailDTO DisplayPrdogrameLvDTO = null;
		Cursor c = null;
		try {
			String[] params = { id };
			c = query(SALE_ORDER_DETAIL_ID + " = ?", params, null, null, null);
		} catch (Exception ex) {
			c = null;
		}
		if (c != null) {
			if (c.moveToFirst()) {
				DisplayPrdogrameLvDTO = initLogDTOFromCursor(c);
			}
		}
		if (c != null) {
			c.close();
		}
		return DisplayPrdogrameLvDTO;
	}

	/**
	 * 
	 * get Detail of SaleOrder by saleOrderById.
	 * 
	 * @author: thuattq
	 * @param saleOrderID
	 * @return
	 * @throws Exception
	 * @return: List<SaleOrderDetailDTO>
	 * @throws:
	 */
	public List<SaleOrderDetailDTO> getAllDetailOfSaleOrder(long saleOrderID)
			throws Exception {
		List<SaleOrderDetailDTO> listDetail = new ArrayList<SaleOrderDetailDTO>();
		Cursor c = null;

		try {
			// String[] params = { Long.toString(saleOrderID) };
			ArrayList<String> params = new ArrayList<String>();
			params.add(String.valueOf(saleOrderID));
			c = query(SALE_ORDER_ID + " = ?",
					params.toArray(new String[params.size()]), null, null, null);

			if (c != null) {
				if (c.moveToFirst()) {
					do {
						SaleOrderDetailDTO item = initLogDTOFromCursor(c);
						if (item != null) {
							listDetail.add(item);
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception ex) {

				}
			}
		}

		return listDetail;
	}

	private SaleOrderDetailDTO initLogDTOFromCursor(Cursor c) {
		SaleOrderDetailDTO dpDetailDTO = new SaleOrderDetailDTO();

		dpDetailDTO.salesOrderDetailId = (c.getLong(c
				.getColumnIndex(SALE_ORDER_DETAIL_ID)));
		dpDetailDTO.productId = (c.getInt(c.getColumnIndex(PRODUCT_ID)));
		dpDetailDTO.quantity = (c.getInt(c.getColumnIndex(QUANTITY)));
		dpDetailDTO.priceId = (c.getLong(c.getColumnIndex(PRICE_ID)));
		dpDetailDTO.discountPercentage = (c.getFloat(c
				.getColumnIndex(DISCOUNT_PERCENT)));
		dpDetailDTO.discountAmount = (c.getLong(c
				.getColumnIndex(DISCOUNT_AMOUNT)));

		dpDetailDTO.isFreeItem = (c.getInt(c.getColumnIndex(IS_FREE_ITEM)));
		dpDetailDTO.programeCode = (c.getString(c.getColumnIndex(PROGRAM_CODE)));
		dpDetailDTO.amount = (c.getInt(c.getColumnIndex(AMOUNT)));
		dpDetailDTO.salesOrderId = (c.getLong(c.getColumnIndex(SALE_ORDER_ID)));
		dpDetailDTO.price = (c.getLong(c.getColumnIndex(PRICE)));

		dpDetailDTO.createUser = (c.getString(c.getColumnIndex(CREATE_USER)));
		dpDetailDTO.updateUser = (c.getString(c.getColumnIndex(UPDATE_USER)));
		dpDetailDTO.createDate = (c.getString(c.getColumnIndex(CREATE_DATE)));
		dpDetailDTO.updateDate = (c.getString(c.getColumnIndex(UPDATE_DATE)));
		dpDetailDTO.orderDate = (c.getString(c.getColumnIndex(ORDER_DATE)));
		dpDetailDTO.synState = (c.getInt(c.getColumnIndex(SYN_STATE)));
		dpDetailDTO.maxQuantityFree = (c.getInt(c.getColumnIndex(MAX_QUANTITY_FREE)));
		dpDetailDTO.programeTypeCode = c.getString(c.getColumnIndex(PROGRAME_TYPE_CODE));

		return dpDetailDTO;
	}

	/**
	 * lay tat ca cac dong cua CSDL
	 * 
	 * @author: HieuNH
	 * @return
	 * @return: Vector<DisplayPrdogrameLvDTO>
	 * @throws:
	 */
	public Vector<SaleOrderDetailDTO> getAllRow() {
		Vector<SaleOrderDetailDTO> v = new Vector<SaleOrderDetailDTO>();
		Cursor c = null;
		try {
			c = query(null, null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			SaleOrderDetailDTO DisplayPrdogrameLvDTO;
			if (c.moveToFirst()) {
				do {
					DisplayPrdogrameLvDTO = initLogDTOFromCursor(c);
					v.addElement(DisplayPrdogrameLvDTO);
				} while (c.moveToNext());
			}
			c.close();
		}
		return v;

	}

	private ContentValues initDataRow(SaleOrderDetailDTO dto) {
		ContentValues editedValues = new ContentValues();

		editedValues.put(SALE_ORDER_DETAIL_ID, dto.salesOrderDetailId);
		if (dto.productId > 0) {
			editedValues.put(PRODUCT_ID, dto.productId);
		} else {
			String temp = null;
			editedValues.put(PRODUCT_ID, temp);
		}
		editedValues.put(QUANTITY, dto.quantity);
		editedValues.put(PRICE_ID, dto.priceId);
		editedValues.put(DISCOUNT_PERCENT, dto.discountPercentage);
		editedValues.put(DISCOUNT_AMOUNT, dto.discountAmount);

		editedValues.put(IS_FREE_ITEM, dto.isFreeItem);
		editedValues.put(PROGRAM_CODE, dto.programeCode);
		if (dto.isFreeItem == 1 && dto.programeType >= 0) {
			editedValues.put(PROGRAM_TYPE, dto.programeType);
		}
		editedValues.put(AMOUNT, dto.amount);
		editedValues.put(SALE_ORDER_ID, dto.salesOrderId);
		editedValues.put(PRICE, dto.price);

		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(UPDATE_USER, dto.updateUser);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(UPDATE_DATE, dto.updateDate);
		editedValues.put(ORDER_DATE, dto.orderDate);
		editedValues.put(MAX_QUANTITY_FREE, dto.maxQuantityFree);
		editedValues.put(SYN_STATE, dto.synState);
		editedValues.put(SHOP_ID, dto.shopId);
		editedValues.put(STAFF_ID, dto.staffId);
		editedValues.put(VAT, dto.vat);
		editedValues.put(PRICE_NOT_VAT, dto.priceNotVat);
		editedValues.put(TOTAL_WEIGHT, dto.totalWeight);
		editedValues.put(MAX_AMOUNT_FREE, dto.maxAmountFree);
		editedValues.put(PROGRAME_TYPE_CODE, dto.programeTypeCode);

		return editedValues;
	}

	public ContentValues initDataRowForEdit(SaleOrderDetailDTO dto,
			boolean isCommited) {
		ContentValues editedValues = new ContentValues();

		editedValues.put(SALE_ORDER_DETAIL_ID, dto.salesOrderDetailId);
		editedValues.put(PRODUCT_ID, dto.productId);
		editedValues.put(QUANTITY, dto.quantity);
		editedValues.put(PRICE_ID, dto.priceId);
		editedValues.put(DISCOUNT_PERCENT, dto.discountPercentage);
		editedValues.put(DISCOUNT_AMOUNT, dto.discountAmount);

		editedValues.put(IS_FREE_ITEM, dto.isFreeItem);
		editedValues.put(PROGRAM_CODE, dto.programeCode);
		editedValues.put(AMOUNT, dto.amount);
		editedValues.put(SALE_ORDER_ID, dto.salesOrderId);
		editedValues.put(PRICE, dto.price);

		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(UPDATE_USER, dto.updateUser);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(UPDATE_DATE, dto.updateDate);
		editedValues.put(ORDER_DATE, dto.orderDate);
		editedValues.put(SHOP_ID, dto.shopId);
		editedValues.put(STAFF_ID, dto.staffId);
		editedValues.put(MAX_QUANTITY_FREE, dto.maxQuantityFree);
		editedValues.put(VAT, dto.vat);
		editedValues.put(PRICE_NOT_VAT, dto.priceNotVat);
		editedValues.put(TOTAL_WEIGHT, dto.totalWeight);
		editedValues.put(MAX_AMOUNT_FREE, dto.maxAmountFree);
		editedValues.put(PROGRAME_TYPE_CODE, dto.programeTypeCode);
		if (isCommited)
			editedValues.put(SYN_STATE, TRANSFERED_STATUS);
		else
			editedValues.put(SYN_STATE, dto.synState);
		return editedValues;
	}

	/**
	 * 
	 * Xoa cac chi tiet dat hang cu~
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param orderID
	 * @return
	 * @return: int
	 * @throws:
	 */
	public int deleteAllDetailOfOrder(long orderID) {
		// String[] params = { "" + orderID };
		ArrayList<String> params = new ArrayList<String>();
		params.add(String.valueOf(orderID));

		return delete(SALE_ORDER_ID + " = ? ",
				params.toArray(new String[params.size()]));
	}

	/**
	 * 
	 * get init cac textview de thuc hien autoComplete
	 * 
	 * @param data
	 * @return
	 * @return: AutoCompleteFindProductSaleOrderDetailViewDTO
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 16, 2013
	 */
	public AutoCompleteFindProductSaleOrderDetailViewDTO getInitListProductAddToOrderView(
			Bundle data) {
		String ext = data.getString(IntentConstants.INTENT_PAGE);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		/**
		 * SQL lay danh sach product code
		 */
		StringBuffer sql = new StringBuffer();
		// version 2
		sql.append("SELECT DISTINCT p.product_code      PRODUCT_CODE, ");
		sql.append("                p.product_name      PRODUCT_NAME, ");
		sql.append("                p.product_name_text PRODUCT_NAME_TEXT ");
		sql.append("FROM   (SELECT p.product_id   product_id, ");
		sql.append("               p.product_code product_code, ");
		sql.append("               p.product_name product_name, ");
		sql.append("               p.name_text    product_name_text, ");
		sql.append("     		  ( CASE ");
		sql.append("         		WHEN p.SORT_ORDER IS NULL THEN 'z' ");
		sql.append("         		ELSE SORT_ORDER ");
		sql.append("         		END )        AS SORT_ORDER, ");
		sql.append("               p.cat_id       cat_id ");
		sql.append("        FROM   product p ");
		sql.append("        WHERE  p.status = 1 ");
		sql.append("        	   AND p.convfact > 1 ) p "); // thung/ket
		sql.append("       LEFT JOIN (SELECT quantity, ");
		sql.append("                          product_id ");
		sql.append("                   FROM   stock_total ");
		sql.append("                   WHERE  object_type = 1 ");
		sql.append("                          AND object_id = ?) st ");
		sql.append("               ON st.product_id = p.product_id ");
		sql.append("       ,product_info pi ");
		sql.append("WHERE  1 = 1 ");
		sql.append("       AND pi.status = 1 ");
		sql.append("       AND pi.type = 1 ");
		sql.append("       AND pi.product_info_id = p.cat_id ");
		sql.append("               AND pi.OBJECT_TYPE = 0 ");
//		sql.append("       AND Ifnull(PI.product_info_code != 'Z', 1) AND Ifnull(PI.product_info_code != 'X', 1) ");
		sql.append("ORDER  BY SORT_ORDER, product_code ASC");
		// khoi tao tham so sql lay danh sach
		String[] paramsList = new String[] {};
		ArrayList<String> listParams = new ArrayList<String>();
		listParams.add(shopId);
		// listParams.add(shopId);

		paramsList = listParams.toArray(new String[listParams.size()]);
		AutoCompleteFindProductSaleOrderDetailViewDTO listResult = new AutoCompleteFindProductSaleOrderDetailViewDTO();
		Cursor c = null;
		try {
			c = rawQuery(sql.toString() + ext,
					paramsList);
			if (c != null) {

				if (c.moveToFirst()) {
					do {
						listResult.init(c);
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
	 * 
	 * get list product to add order
	 * 
	 * @author: HaiTC3
	 * @param data
	 * @return
	 * @return: ListFindProductSaleOrderDetailViewDTO
	 * @throws:
	 */
	public ListFindProductSaleOrderDetailViewDTO getListProductAddToOrderView(
			Bundle data) {
		MeasuringTime.getStartTimeParser();
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);
		String ext = data.getString(IntentConstants.INTENT_PAGE);
		String productCode = data
				.getString(IntentConstants.INTENT_PRODUCT_CODE);
		String productName = data
				.getString(IntentConstants.INTENT_PRODUCT_NAME);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		SHOP_TABLE shopTB = new SHOP_TABLE(this.mDB);
		ArrayList<String> listShopId = shopTB.getShopRecursive(shopId);
		String strListShop = TextUtils.join(",", listShopId);

		String staffId = String.valueOf(GlobalInfo.getInstance().getProfile()
				.getUserData().id);
		String customerTypeId = data
				.getString(IntentConstants.INTENT_CUSTOMER_TYPE_ID);
		//String saleTypeCode = data
		//		.getString(IntentConstants.INTENT_SALE_TYPE_CODE);
		String customerId = data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String listProductNotIn = data
				.getString(IntentConstants.INTENT_LIST_PRODUCT_NOT_IN);
		//int staffType = data.getInt(IntentConstants.INTENT_STAFF_TYPE);
		String orderType = data.getString(IntentConstants.INTENT_ORDER_TYPE);
		boolean isGetCount = data
				.getBoolean(IntentConstants.INTENT_IS_GET_NUM_TOTAL_ITEM);

		boolean hasProductCode = !StringUtil.isNullOrEmpty(productCode);
		boolean hasProductName = !StringUtil.isNullOrEmpty(productName);
		boolean hasListProductNotIn = !StringUtil
				.isNullOrEmpty(listProductNotIn);
		if (!StringUtil.isNullOrEmpty(productCode)) {
			productCode = StringUtil.escapeSqlString(productCode);
			productCode = DatabaseUtils
					.sqlEscapeString("%" + productCode + "%");
			productCode = productCode.substring(1, productCode.length() - 1);
		}
		if (!StringUtil.isNullOrEmpty(productName)) {
			productName = StringUtil.getEngStringFromUnicodeString(productName);
			productName = StringUtil.escapeSqlString(productName);
			productName = DatabaseUtils
					.sqlEscapeString("%" + productName + "%");
			productName = productName.substring(1, productName.length() - 1);
		}

		/**
		 * SQL lay danh sach san pham
		 */
		// khoi tao tham so sql lay danh sach
		String[] paramsList = new String[] {};
		ArrayList<String> listParams = new ArrayList<String>();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT p.product_id   AS PRODUCT_ID, ");
		sql.append("                pr_.price_id   AS PRICE_ID, ");
		sql.append("                pr_.vat      AS VAT, ");
		sql.append("                pr_.PRICE_NOT_VAT      AS PRICE_NOT_VAT, ");
		//Neu cau hinh hien thi gia thi hien thi suggested price
		if(GlobalInfo.getInstance().getIsShowPrice() == 1) {
			sql.append("                spr_.price      AS PRICE, ");
		} else {
			sql.append("                pr_.price      AS PRICE, ");
		}
		sql.append("                p.GROSS_WEIGHT     AS GROSS_WEIGHT, ");
		sql.append("                p.convfact     AS CONVFACT, ");
		sql.append("                p.uom2     AS UOM2, ");
		sql.append("                p.SORT_ORDER     AS SORT_ORDER, ");
		sql.append("                ST.quantity    AS QUANTITY, ");
		sql.append("                ST.available_quantity    AS AVAILABLE_QUANTITY, ");

		//BangHN: kiem tra co stock total hay ko de order
		sql.append("                (CASE WHEN ST.quantity IS NULL THEN 0 ");
		sql.append("                	ELSE 1 ");
		sql.append("               	 END ) AS HAVE_STOCK_TOTAL, ");
		
		sql.append("                p.product_name AS PRODUCT_NAME, ");
		sql.append("                p.product_code AS PRODUCT_CODE, ");
		sql.append("                1              AS DISPLAY_PROGRAME_CODE ");
		sql.append("                ,NEW_PROMOTION.COKHAIBAO1       AS COKHAIBAO1 ");
		sql.append("                ,NEW_PROMOTION.COKHAIBAO2       AS COKHAIBAO2 ");
		sql.append("                ,NEW_PROMOTION.COKHAIBAO3       AS COKHAIBAO3 ");
		sql.append("                ,NEW_PROMOTION.COKHAIBAO4       AS COKHAIBAO4 ");
		sql.append("                ,NEW_PROMOTION.COKHAIBAO5       AS COKHAIBAO5 ");
		sql.append("                ,NEW_PROMOTION.COKHAIBAO6       AS COKHAIBAO6 ");
		sql.append("                ,NEW_PROMOTION.COKHAIBAO7       AS COKHAIBAO7 ");
		sql.append("                ,NEW_PROMOTION.COKHAIBAO8       AS COKHAIBAO8 ");
		sql.append("                ,NEW_PROMOTION.COKHAIBAO9       AS COKHAIBAO9 ");
		sql.append("                ,NEW_PROMOTION.COKHAIBAO10       AS COKHAIBAO10 ");
		sql.append("                ,NEW_PROMOTION.code       AS CODE ");

		// check bang product & product info
		sql.append(" FROM   (SELECT p.PRODUCT_CODE       product_code, ");
		sql.append("               p.PRODUCT_ID       product_id, ");
		sql.append("               p.PRODUCT_NAME       product_name, ");
		sql.append("               p.NAME_TEXT  product_name_text, ");
		sql.append("               p.convfact  convfact, ");
		sql.append("               p.GROSS_WEIGHT  GROSS_WEIGHT, ");
		sql.append("      		   ( CASE ");
		sql.append("         		WHEN p.SORT_ORDER IS NULL THEN 'z' ");
		sql.append("         		ELSE SORT_ORDER ");
		sql.append("         		END )        AS SORT_ORDER, ");
		sql.append("               ap.ap_param_name  uom2, ");
		sql.append("               p.cat_id cat_id ");
		sql.append("        FROM   PRODUCT p, ap_param ap, ");
		sql.append("               PRODUCT_INFO pi ");
		sql.append("        WHERE  p.STATUS = 1 ");
		sql.append("        	   AND p.convfact > 1 "); // thung/ket
		sql.append("               AND pi.STATUS = 1 ");
		sql.append("               AND pi.TYPE = 1 ");
		sql.append("               AND p.uom2 = ap.ap_param_code ");
		sql.append("               AND ap.status = 1 ");
		sql.append("               AND ap.type = 'UOM2' ");
		sql.append("               AND pi.PRODUCT_INFO_ID = p.CAT_ID ");
		sql.append("               AND pi.OBJECT_TYPE = 0 ");
		sql.append("               ) AS P ");
		// check bang stock total
		if(orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_PRESALE)) {
			sql.append("       LEFT ");
		}
		
		sql.append("       JOIN (SELECT quantity, ");
		sql.append("                          product_id, ");
		sql.append("                          available_quantity ");
		sql.append("                   FROM   stock_total WHERE ");
		
		// default: preSales
		if(orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_PRESALE)) {
			sql.append("                   object_type = 1 ");
		} else {
			sql.append("                   object_type = 2 ");
		}
		
		sql.append("                          AND object_id = ?) st ");
		if(orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_PRESALE)) {
			listParams.add(shopId);
		} else {
			listParams.add(staffId);
		}
		sql.append("               ON st.product_id = p.product_id ");

		// check bang price
		sql.append("       LEFT JOIN (SELECT product_id, price_id, vat, price_not_vat, price ");
		sql.append("                   FROM   price pr ");
		sql.append("                   WHERE   pr.status = 1 AND pr.type = 2 "); //gia npp ban cho kh
		sql.append("                          AND Ifnull(Date(pr.from_date) <= Date (?), 0) ");
		sql.append("                          AND  Ifnull(Date(pr.to_date) >= Date(?), 1) ) AS pr_ ");
		sql.append("               ON p.product_id = pr_.product_id ");
		listParams.add(date_now);
		listParams.add(date_now);
		sql.append("       LEFT JOIN (SELECT product_id, price ");
		sql.append("                   FROM   suggested_price spr ");
		sql.append("                   WHERE   spr.status = 1 ");
		sql.append("                           AND shop_id = ? AND customer_id = ? ");
		sql.append("                           order by datetime(create_date) desc ");
		sql.append("				) AS spr_ ON p.product_id = spr_.product_id ");
		listParams.add(shopId);
		listParams.add(customerId);
		// Chuong trinh khuyen mai
		listParams.add(customerTypeId);
		listParams.add(customerId);
		listParams.add(customerId);
		listParams.add(customerId);
		listParams.add(customerId);
		listParams.add(customerId);
		sql.append(" LEFT JOIN ( SELECT distinct pp.promotion_program_code code, pp.promotion_program_id, ");
		sql.append("                ppd.product_id product_id, ");
		sql.append("                Ifnull(TTTT1.num_condition, 0) COKHAIBAO1, ");
		sql.append("                Ifnull(TTTT2.num_condition, 0) COKHAIBAO2, ");
		sql.append("                Ifnull(TTTT3.num_condition, 0) COKHAIBAO3, ");
		sql.append("                Ifnull(TTTT4.num_condition, 0) COKHAIBAO4, ");
		sql.append("                Ifnull(TTTT5.num_condition, 0) COKHAIBAO5, ");
		sql.append("                Ifnull(TTTT6.num_condition, 0) COKHAIBAO6, ");
		sql.append("                Ifnull(TTTT7.num_condition, 0) COKHAIBAO7, ");
		sql.append("                Ifnull(TTTT8.num_condition, 0) COKHAIBAO8, ");
		sql.append("                Ifnull(TTTT9.num_condition, 0) COKHAIBAO9, ");
		sql.append("                Ifnull(TTTT10.num_condition, 0) COKHAIBAO10 ");
		sql.append("FROM   promotion_program pp ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id, ");
		sql.append("                         Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca ");
		sql.append("                  WHERE  pca.object_type = 2 ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT1 ");
		sql.append("              ON TTTT1.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id ");
		sql.append("                         , ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         promotion_cust_attr_detail pcad ");
		sql.append("                  WHERE  1 = 1 ");
		sql.append("                         AND pca.promotion_cust_attr_id = ");
		sql.append("                             pcad.promotion_cust_attr_id ");
		sql.append("                         AND pca.object_type = 2 ");
		sql.append("                         AND pcad.object_id = ? ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND pcad.status = 1 ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT2 ");
		sql.append("              ON TTTT2.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id       promotion_program_id, ");
		sql.append("                         Count (promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca ");
		sql.append("                  WHERE  pca.object_type = 3 ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                  GROUP  BY promotion_program_id) TTTT3 ");
		sql.append("              ON TTTT3.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id ");
		sql.append("                         , ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         promotion_cust_attr_detail pcad, ");
		sql.append("                         sale_level_cat slc, ");
		sql.append("                         customer_cat_level ccl ");
		sql.append("                  WHERE  1 = 1 ");
		sql.append("                         AND pca.promotion_cust_attr_id = ");
		sql.append("                             pcad.promotion_cust_attr_id ");
		sql.append("                         AND pcad.object_id = slc.sale_level_cat_id ");
		sql.append("                         AND slc.sale_level_cat_id = ccl.sale_level_cat_id ");
		sql.append("                         AND pca.object_type = 3 ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND pcad.status = 1 ");
		sql.append("                         AND slc.status = 1 ");
		sql.append("                         AND ccl.customer_id = ? ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT4 ");
		sql.append("              ON TTTT4.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id       promotion_program_id, ");
		sql.append("                         Count (promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         attribute AT ");
		sql.append("                  WHERE  pca.object_type = 1 ");
		sql.append("                         AND pca.object_id = AT.attribute_id ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND AT.status = 1 ");
		sql.append("                         AND AT.value_type IN ( 1, 2, 3 ) ");
		sql.append("                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT5 ");
		sql.append("              ON TTTT5.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id ");
		sql.append("                         , ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         attribute AT, ");
		sql.append("                         attribute_value atv ");
		sql.append("                  WHERE  pca.object_type = 1 ");
		sql.append("                         AND pca.object_id = AT.attribute_id ");
		sql.append("                         AND AT.attribute_id = atv.attribute_id ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND AT.status = 1 ");
		sql.append("                         AND atv.status = 1 ");
		sql.append("                         AND AT.value_type IN ( 1, 2, 3 ) ");
		sql.append("                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                         AND atv.table_name = 'CUSTOMER' ");
		sql.append("                         AND atv.table_id = ? ");
		sql.append("                         AND ( CASE ");
		sql.append("                                 WHEN AT.value_type = 2 THEN Ifnull( ");
		sql.append("                                 cast(atv.value as integer) >= pca.from_value, ");
		sql.append("                                                             1) ");
		sql.append("                                                             AND ");
		sql.append("                                 Ifnull(cast(atv.value as integer) <= pca.to_value, 1) ");
		sql.append("                                 WHEN AT.value_type = 1 THEN ");
		sql.append("                                 trim(lower(atv.value)) = trim(lower(pca.from_value)) ");
		sql.append("                                 WHEN AT.value_type = 3 THEN atv.value is not null and Ifnull( ");
		sql.append("                                 Date(atv.value) >= Date(pca.from_value), ");
		sql.append("                                                             1) ");
		sql.append("                                                             AND ");
		sql.append("                                 Ifnull(Date(atv.value) <= Date(pca.to_value), ");
		sql.append("                                 1) ");
		sql.append("                               END ) ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT6 ");
		sql.append("              ON TTTT6.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id ");
		sql.append("                         , ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         attribute AT ");
		sql.append("                  WHERE  pca.object_type = 1 ");
		sql.append("                         AND pca.object_id = AT.attribute_id ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND AT.status = 1 ");
		sql.append("                         AND AT.value_type = 4 ");
		sql.append("                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT7 ");
		sql.append("              ON TTTT7.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id, ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         attribute AT, ");
		sql.append("                         attribute_value atv, ");
		sql.append("                         attribute_detail atd, ");
		sql.append("                         attribute_value_detail atvd, ");
		sql.append("                         promotion_cust_attr_detail pcad ");
		sql.append("                  WHERE  pca.object_type = 1 ");
		sql.append("                         AND pca.object_id = AT.attribute_id ");
		sql.append("                         AND AT.attribute_id = atv.attribute_id ");
		sql.append("                         AND AT.attribute_id = atd.attribute_id ");
		sql.append("                         AND atv.attribute_value_id = atvd.attribute_value_id ");
		sql.append("                         AND atd.attribute_detail_id = atvd.attribute_detail_id ");
		sql.append("                         AND atd.attribute_detail_id = pcad.object_id ");
		sql.append("                         AND pca.promotion_cust_attr_id = ");
		sql.append("                             pcad.promotion_cust_attr_id ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND AT.status = 1 ");
		sql.append("                         AND atv.status = 1 ");
		sql.append("                         AND atd.status = 1 ");
		sql.append("                         AND atvd.status = 1 ");
		sql.append("                         AND pcad.status = 1 ");
		sql.append("                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                         AND AT.value_type = 4 ");
		sql.append("                         AND atv.table_name = 'CUSTOMER' ");
		sql.append("                         AND atv.table_id = ? ");
		sql.append("                  GROUP  BY pca.promotion_program_id)TTTT8 ");
		sql.append("              ON TTTT8.promotion_program_id = pp.promotion_program_id ");

		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id, ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         attribute AT ");
		sql.append("                  WHERE  pca.object_type = 1 ");
		sql.append("                         AND pca.object_id = AT.attribute_id ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND AT.status = 1 ");
		sql.append("                         AND AT.value_type = 5 ");
		sql.append("                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT9 ");
		sql.append("              ON TTTT9.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id, ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         attribute AT, ");
		sql.append("                         attribute_value atv, ");
		sql.append("                         attribute_detail atd, ");
		sql.append("                         attribute_value_detail atvd, ");
		sql.append("                         promotion_cust_attr_detail pcad ");
		sql.append("                  WHERE  pca.object_type = 1 ");
		sql.append("                         AND pca.object_id = AT.attribute_id ");
		sql.append("                         AND AT.attribute_id = atv.attribute_id ");
		sql.append("                         AND AT.attribute_id = atd.attribute_id ");
		sql.append("                         AND atv.attribute_value_id = atvd.attribute_value_id ");
		sql.append("                         AND atd.attribute_detail_id = atvd.attribute_detail_id ");
		sql.append("                         AND atd.attribute_detail_id = pcad.object_id ");
		sql.append("                         AND pca.promotion_cust_attr_id = ");
		sql.append("                             pcad.promotion_cust_attr_id ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND AT.status = 1 ");
		sql.append("                         AND atv.status = 1 ");
		sql.append("                         AND atd.status = 1 ");
		sql.append("                         AND atvd.status = 1 ");
		sql.append("                         AND pcad.status = 1 ");
		sql.append("                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                         AND AT.value_type = 5 ");
		sql.append("                         AND atv.table_name = 'CUSTOMER' ");
		sql.append("                         AND atv.table_id = ? ");
		sql.append("                  GROUP  BY pca.promotion_program_id)TTTT10 ");
		sql.append("              ON TTTT10.promotion_program_id = pp.promotion_program_id, ");
		sql.append("       promotion_program_detail ppd, ");
		sql.append("       promotion_shop_map psm ");
		sql.append("       LEFT JOIN promotion_customer_map pcm ");
		sql.append("              ON pcm.promotion_shop_map_id = psm.promotion_shop_map_id ");
		sql.append("WHERE  1 = 1 ");
		sql.append("       AND psm.promotion_program_id = pp.promotion_program_id ");
		sql.append("       AND pp.promotion_program_id = ppd.promotion_program_id ");
		sql.append("       AND psm.shop_id IN ( ");
		sql.append(strListShop + " )");
		sql.append("       AND pp.status = 1 ");
		sql.append("       AND psm.status = 1 ");
		sql.append("       AND Date(pp.from_date) <= Date('now', 'localtime') ");
		sql.append("       AND Ifnull (Date(pp.to_date) >= Date('now', 'localtime'), 1) ");
		sql.append("       AND ( CASE ");
		sql.append("               WHEN (SELECT Count(*) ");
		sql.append("                     FROM   promotion_customer_map pcmm ");
		sql.append("                     WHERE  pcmm.promotion_shop_map_id = ");
		sql.append("                            psm.promotion_shop_map_id) = 0 ");
		sql.append("                 THEN ");
		sql.append("               1 ");
		sql.append("               ELSE pcm.customer_id = ? ");
		sql.append("                    AND pcm.status = 1 ");
		sql.append("             END ) ) NEW_PROMOTION on NEW_PROMOTION.product_id = p.product_id ");
		sql.append("WHERE  1 = 1 ");

		if (hasListProductNotIn) {
			sql.append(" AND p.product_code NOT IN ( " + listProductNotIn + ")");
		}
		if (hasProductCode) {
			sql.append("AND upper(p.product_code) LIKE upper(?) escape '^'");
			listParams.add(productCode);
		}
		if (hasProductName) {
			sql.append("AND upper(p.product_name_text) LIKE upper(?) escape '^' ");
			listParams.add(productName);
		}
		sql.append("ORDER BY SORT_ORDER, product_code ASC, product_name ASC");

		//lay tong so luong dong
		String[] paramsTotal = new String[] {};
		ArrayList<String> totalParams = new ArrayList<String>();

		StringBuffer sql2 = new StringBuffer();
		sql2.append("SELECT DISTINCT p.product_id AS PRODUCT_ID , st.quantity, st.available_quantity ");

		// check bang product & product info
		sql2.append(" FROM   (SELECT p.PRODUCT_CODE       product_code, ");
		sql2.append("               p.PRODUCT_ID       product_id, ");
		sql2.append("               p.PRODUCT_NAME       product_name, ");
		sql2.append("               p.NAME_TEXT  product_name_text, ");
		sql2.append("               p.convfact  convfact, ");
		sql2.append("               ap.ap_param_name  uom2, ");
		sql2.append("               p.cat_id cat_id ");
		sql2.append("        FROM   PRODUCT p, ap_param ap, ");
		sql2.append("               PRODUCT_INFO pi ");
		sql2.append("        WHERE  p.STATUS = 1 ");
		sql2.append("        AND p.convfact > 1 "); // thung/ket
		sql2.append("        AND ap.status = 1 ");
		sql2.append("        AND ap.ap_param_code = p.uom2 ");
		sql2.append("        AND ap.type = 'UOM2' ");
		sql2.append("               AND pi.STATUS = 1 ");
		sql2.append("               AND pi.TYPE = 1 ");
		sql2.append("               AND pi.PRODUCT_INFO_ID = p.CAT_ID ");
		sql2.append("               AND pi.OBJECT_TYPE = 0 ");
		sql2.append("               ) AS P ");
		//hien tai stock-total dang lay theo left join nen ko can
		//check dk stock-total
		if(orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_PRESALE)) {
			sql2.append("       LEFT ");
		}
		sql2.append("       JOIN (SELECT quantity, AVAILABLE_QUANTITY, ");
		sql2.append("                          product_id ");
		sql2.append("                   FROM   stock_total ");
		sql2.append("                   WHERE   ");

		// default: preSales
		if(orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_PRESALE)) {
			sql2.append("                   object_type = 1 ");
		} else {
			sql2.append("                   object_type = 2 ");
		}
		
		sql2.append("                          AND object_id = ?) st ");
		if(orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_PRESALE)) {
			totalParams.add(shopId);
		} else {
			totalParams.add(staffId);
		}
		sql2.append("               ON st.product_id = p.product_id ");
		sql2.append("       LEFT JOIN (SELECT product_id, price_id, vat, price_not_vat, price ");
		sql2.append("                   FROM   price pr ");
		sql2.append("                   WHERE   pr.status = 1 AND pr.type = 2 "); //gia npp ban cho kh
		sql2.append("                          AND Ifnull(Date(pr.from_date) <= Date (?), 0) ");
		sql2.append("                          AND  Ifnull(Date(pr.to_date) >= Date(?), 1) ) AS pr_ ");
		sql2.append("               ON p.product_id = pr_.product_id ");
		totalParams.add(date_now);
		totalParams.add(date_now);
		sql2.append("       LEFT JOIN (SELECT product_id, price ");
		sql2.append("                   FROM   suggested_price spr ");
		sql2.append("                   WHERE   spr.status = 1 ");
		sql2.append("                           AND shop_id = ? AND customer_id = ? ");
		sql2.append("                           order by datetime(create_date) desc limit 1 ");
		sql2.append("				) AS spr_ ON p.product_id = spr_.product_id ");
		totalParams.add(shopId);
		totalParams.add(customerId);
		sql2.append("WHERE  1 = 1 ");
		if (hasListProductNotIn) {
			sql2.append(" AND p.product_code NOT IN ( " + listProductNotIn
					+ ")");
		}
		if (hasProductCode) {
			sql2.append("AND upper(p.product_code) LIKE upper(?) escape '^' ");
			totalParams.add(productCode);
		}
		if (hasProductName) {
			sql2.append("AND upper(p.product_name_text) LIKE upper(?) escape '^' ");
			totalParams.add(productName);
		}
		String getCountProductList = " select count(*) as total_row from ("
				+ sql2.toString() + ") ";

		// bo xung tham so lay d/s san pham
		paramsList = listParams.toArray(new String[listParams.size()]);

		// bo xung tham so cho sql lay tong so luong
		paramsTotal = totalParams.toArray(new String[totalParams.size()]);
		
		ListFindProductSaleOrderDetailViewDTO listResult = new ListFindProductSaleOrderDetailViewDTO();

		Cursor cTmp = null;
		try {
			// get total row first
			int total = 0;
			if (isGetCount) {
				cTmp = rawQuery(getCountProductList, paramsTotal);
				if (cTmp != null) {
					cTmp.moveToFirst();
					total = cTmp.getInt(0);
					listResult.totalObject = total;
				}
			}
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try {
				if (isGetCount && cTmp != null) {
					cTmp.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
			// end
		Cursor c = null;
		try{
			c = rawQuery(sql.toString() + ext,
					paramsList);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						FindProductSaleOrderDetailViewDTO orderJoinTableDTO = new FindProductSaleOrderDetailViewDTO();
						orderJoinTableDTO
								.initSaleOrderDetailObjectFromGetProductStatement(c);
						listResult.listObject.add(orderJoinTableDTO);
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
		MeasuringTime.getEndTimeParser();
		return listResult;
	}

	/**
	 * 
	 * get list nganh hang, nganh hang con
	 * 
	 * @author: HieuNH
	 * @return
	 * @return: ListProductDTO
	 * @throws:
	 */
	public CategoryCodeDTO getListCategoryCodeProduct() {

		StringBuilder getCategoryCodeList = new StringBuilder();
		getCategoryCodeList
				.append("select distinct P.category_code from PRODUCT as P where P.status = 1");

		StringBuilder queryGetSubCategoryList = new StringBuilder();
		queryGetSubCategoryList
				.append("select distinct P.subcategory_id from PRODUCT as P where P.status = 1");

		CategoryCodeDTO listResult = new CategoryCodeDTO();
		Cursor cCode = null;
		// get total row first
		String[] arrParam = new String[] {};
		try {

			cCode = rawQuery(getCategoryCodeList + "", arrParam);
			listResult.categoryCode.addElement("Chá»�n ngÃ nh hÃ ng");
			if (cCode != null) {
				if (cCode.moveToFirst()) {
					do {
						if (cCode.getColumnIndex("CATEGORY_CODE") >= 0) {
							listResult.categoryCode.addElement(cCode.getString(cCode.getColumnIndex("CATEGORY_CODE")));
						}
					} while (cCode.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try {
				if (cCode != null) {
					cCode.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		// end
		Cursor cCategory = null;
		try {
			cCategory = rawQuery(queryGetSubCategoryList + "", arrParam);
			listResult.subCategoryCode.addElement("Chá»�n ngÃ nh con");
			if (cCategory != null) {
				if (cCategory.moveToFirst()) {
					do {
						if (cCategory.getColumnIndex("SUBCATEGORY_ID") >= 0) {
							listResult.subCategoryCode.addElement(cCategory.getString(cCategory.getColumnIndex("SUBCATEGORY_ID")));
						}
					} while (cCategory.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try {
				if (cCategory != null) {
					cCategory.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return listResult;
	}

	/**
	 * 
	 * get Remain Product
	 * 
	 * @author: HieuNH
	 * @param data
	 * @return
	 * @return: ListProductDTO
	 * @throws:
	 */
	public ListRemainProductDTO getRemainProduct(Bundle data) {
		String customerId = data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		String page = data.getString(IntentConstants.INTENT_PAGE);
		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID);
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		String dateFirstMonthLast = DateUtils.getFirstDateOfNumberPreviousMonthWithFormat(DateUtils.DATE_FORMAT_SQL_MONTH_DEFAULT,-2);
		ArrayList<String> params = new ArrayList<String>();
		StringBuffer  sqlQuery = new StringBuffer();
		sqlQuery.append(" SELECT p.product_id          AS PRODUCT_ID, ");
		sqlQuery.append("        p.product_code        AS PRODUCT_CODE, ");
		sqlQuery.append("        p.product_name        AS PRODUCT_NAME, ");
		sqlQuery.append("        p.convfact            AS CONVFACT, ");
		sqlQuery.append("       ( CASE ");
		sqlQuery.append("         WHEN p.SORT_ORDER IS NULL THEN 'z' ");
		sqlQuery.append("         ELSE SORT_ORDER ");
		sqlQuery.append("         END )        AS SORT_ORDER, ");
		sqlQuery.append("        p.gross_weight        AS GROSS_WEIGHT, ");
		sqlQuery.append("        ap.ap_param_name      AS UNIT, ");
		sqlQuery.append("        pri.price             AS PRICE, ");
		if(GlobalInfo.getInstance().getIsShowPrice() == 1) { 
			sqlQuery.append("    spri.price           AS PRICE,  "); 
		} else { 
			sqlQuery.append("    pri.price            AS PRICE,   "); 
		}
		sqlQuery.append("        pri.price_not_vat     AS PRICE_NOT_VAT, ");
		sqlQuery.append("        pri.price_id          AS PRICE_ID, ");
		sqlQuery.append("        pri.vat               AS VAT, ");
		sqlQuery.append("        st.available_quantity AS QUANTITY_REMAIN ");
		sqlQuery.append(" FROM sale_order_detail sod, ");
		sqlQuery.append("      ap_param ap, ");
		sqlQuery.append("      product p ");
		sqlQuery.append("  LEFT JOIN ( SELECT product_id, ");
		sqlQuery.append("                     price_id, ");
		sqlQuery.append("                     vat, ");
		sqlQuery.append("                     price_not_vat, ");
		sqlQuery.append("                     price ");
		sqlQuery.append("              FROM price pr ");
		sqlQuery.append("              WHERE pr.status = 1 ");
		sqlQuery.append("                    AND pr.type = 2 ");
		sqlQuery.append("                    AND Ifnull(substr(pr.from_date,1,10) <= ?, 0) ");
		params.add(date_now);
		sqlQuery.append("                    AND Ifnull(substr(pr.to_date,1,10) >= ?, 1) ) AS pri  ON p.product_id = pri.product_id ");
		params.add(date_now);
		sqlQuery.append("  LEFT JOIN ( SELECT available_quantity, ");
		sqlQuery.append("                     product_id, ");
		sqlQuery.append("                     object_id ");
		sqlQuery.append("              FROM stock_total ");
		sqlQuery.append("              WHERE object_type = 1 ");
		sqlQuery.append("                    AND object_id = ?) AS st ON st.product_id = p.product_id ");
		params.add(shopId);
		sqlQuery.append("  LEFT JOIN ( SELECT product_id, ");
		sqlQuery.append("                     price ");
		sqlQuery.append("              FROM suggested_price spr ");
		sqlQuery.append("              WHERE spr.status = 1 ");
		sqlQuery.append("                    AND shop_id = ? ");
		params.add(shopId); 
		sqlQuery.append("                    AND customer_id = ? ");
		params.add(customerId);
		sqlQuery.append("              ORDER BY datetime(create_date) DESC) AS spri ON p.product_id = spri.product_id");
		sqlQuery.append(" WHERE 1=1 ");
		sqlQuery.append("  		AND p.status =1 ");
		sqlQuery.append("  		AND ap.status =1 ");
		sqlQuery.append("  		AND p.convfact > 1 ");
		sqlQuery.append("  		AND p.uom2 = ap.ap_param_code ");
		sqlQuery.append("  		AND p.product_id = sod.product_id ");
		sqlQuery.append("  		AND sod.is_free_item = 0 ");
		sqlQuery.append("  		AND sod.sale_order_id IN ");
		sqlQuery.append("    							( SELECT SO.sale_order_id AS SALE_ORDER_ID ");
		sqlQuery.append("     							FROM sale_order SO ");
		sqlQuery.append("     							WHERE SO.approved = 1 ");
		sqlQuery.append("       							  AND SO.type = 1 ");
		sqlQuery.append("       							  AND SO.staff_id = ? ");
		params.add(staffId);
		sqlQuery.append("       							  AND SO.customer_id = ? ");
		params.add(customerId);
		sqlQuery.append("                                     AND Ifnull(substr(SO.order_date,1,7) >= ?, 0)) ");
		params.add(dateFirstMonthLast);
		sqlQuery.append(" GROUP BY sod.product_id, ");
		sqlQuery.append("          p.convfact, ");
		sqlQuery.append("          p.product_name, ");
		sqlQuery.append("          p.product_code ");
		sqlQuery.append(" ORDER BY p.SORT_ORDER, p.product_code, ");
		sqlQuery.append("          p.product_name");
		
		
		
		ListRemainProductDTO listResult = null;
		Cursor c = null;
		try {
			c = rawQuery(sqlQuery.append(page).toString(), params.toArray(new String[params.size()]));
 
			if (c != null) {
				listResult = new ListRemainProductDTO();
				if (c.moveToFirst()) {

					RemainProductViewDTO productInfo;
					do {
						productInfo = new RemainProductViewDTO();
						productInfo.initDataFromCursor(c);

						listResult.listDTO.add(productInfo);

					} while (c.moveToNext());
					listResult.total = listResult.listDTO.size();
				}

			}

//			if (listResult.total != 0) {
//				tinhluongGoiY(data, listResult.listDTO, numVisitRemain);
//			}

		} catch (Exception e) {
			ServerLogger.sendLog("method getRemainProduct error: ", e.getStackTrace().toString() + ", " + e.getMessage(), TabletActionLogDTO.LOG_CLIENT);
			VTLog.i("error", e.toString());
			listResult = null;
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
	 * 
	 * Khoi tao doi tuong chi tiet Don hang da.ng KM
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param c
	 * @return
	 * @return: OrderDetailViewDTO
	 * @throws:
	 */
	private OrderDetailViewDTO initPromotionProductFromCursor(Cursor c) {
		SaleOrderDetailDTO saleOrderDTO = new SaleOrderDetailDTO();
		OrderDetailViewDTO detailViewDTO = new OrderDetailViewDTO();

		saleOrderDTO.productId = c.getInt(c.getColumnIndex(PRODUCT_ID));
		saleOrderDTO.price = c.getLong(c.getColumnIndex(PRICE));
		saleOrderDTO.priceId = c.getLong(c.getColumnIndex(PRICE_ID));
		saleOrderDTO.quantity = c.getInt(c.getColumnIndex("ACTUAL"));
		saleOrderDTO.maxQuantityFree = c.getInt(c
				.getColumnIndex(MAX_QUANTITY_FREE));
		saleOrderDTO.amount = c.getLong(c.getColumnIndex(DISCOUNT_AMOUNT));
		saleOrderDTO.discountAmount = c.getLong(c
				.getColumnIndex(DISCOUNT_AMOUNT));
		saleOrderDTO.discountPercentage = c.getFloat(c
				.getColumnIndex(DISCOUNT_PERCENT));
		saleOrderDTO.maxAmountFree = c.getLong(c
				.getColumnIndex(MAX_AMOUNT_FREE));
		saleOrderDTO.programeCode = c.getString(c.getColumnIndex(PROGRAM_CODE));
		saleOrderDTO.programeType = c.getInt(c.getColumnIndex(PROGRAM_TYPE));
		saleOrderDTO.isFreeItem = c.getInt(c.getColumnIndex("IS_FREE_ITEM"));
		saleOrderDTO.priceNotVat = c.getLong(c.getColumnIndex(PRICE_NOT_VAT));
		saleOrderDTO.totalWeight = c.getDouble(c.getColumnIndex(TOTAL_WEIGHT));
		saleOrderDTO.vat = c.getDouble(c.getColumnIndex("VAT"));
		detailViewDTO.grossWeight = c.getDouble(c
				.getColumnIndex("GROSS_WEIGHT"));
		detailViewDTO.productCode = c.getString(c
				.getColumnIndex(PRODUCT_TABLE.PRODUCT_CODE));
		detailViewDTO.productName = c.getString(c
				.getColumnIndex(PRODUCT_TABLE.PRODUCT_NAME));
		detailViewDTO.uom2 = StringUtil.getStringFromSQliteCursor(c, "UOM2");
		if (detailViewDTO.productName == null
				|| detailViewDTO.productName.length() == 0) {
			detailViewDTO.productName = StringUtil
					.getString(R.string.TEXT_KM_CK_GET);
		}
		detailViewDTO.stock = c.getLong(c.getColumnIndex(STOCK_TOTAL_TABLE.AVAILABLE_QUANTITY));
		detailViewDTO.quantity = String.valueOf(saleOrderDTO.quantity);
		saleOrderDTO.programeTypeCode = c.getString(c.getColumnIndex(SALES_ORDER_DETAIL_TABLE.PROGRAME_TYPE_CODE));
		
		//Get type of promotion for render
		if(saleOrderDTO.maxQuantityFree > 0) {
			detailViewDTO.type = OrderDetailViewDTO.FREE_PRODUCT;
		} else if (saleOrderDTO.discountPercentage > 0) {
			detailViewDTO.type = OrderDetailViewDTO.FREE_PERCENT;
		} else if (saleOrderDTO.maxAmountFree > 0) {
			detailViewDTO.type = OrderDetailViewDTO.FREE_PRICE;
		}

		detailViewDTO.orderDetailDTO = saleOrderDTO;
		return detailViewDTO;
	}

	/**
	 * 
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param c
	 * @return: OrderDetailViewDTO
	 * @throws:
	 */
	private OrderDetailViewDTO initSaleOrderDetailObjectFromGetProductStatement(
			Cursor c) {
		SaleOrderDetailDTO saleOrderDTO = new SaleOrderDetailDTO();
		OrderDetailViewDTO detailViewDTO = new OrderDetailViewDTO();

		saleOrderDTO.productId = c.getInt(c.getColumnIndex(PRODUCT_ID));
		saleOrderDTO.price = c.getLong(c.getColumnIndex(PRICE));
		detailViewDTO.suggestedPrice = String.valueOf(saleOrderDTO.price);
		saleOrderDTO.priceId = c.getLong(c.getColumnIndex(PRICE_ID));
		saleOrderDTO.quantity = c.getInt(c.getColumnIndex("ACTUAL"));
		saleOrderDTO.maxQuantityFree = c.getInt(c
				.getColumnIndex(MAX_QUANTITY_FREE));
		saleOrderDTO.amount = c.getLong(c.getColumnIndex(AMOUNT));
		saleOrderDTO.isFreeItem = c.getInt(c.getColumnIndex(IS_FREE_ITEM));

		saleOrderDTO.programeCode = c.getString(c.getColumnIndex(PROGRAM_CODE));
		saleOrderDTO.programeType = c.getInt(c.getColumnIndex(PROGRAM_TYPE));
		saleOrderDTO.synState = ABSTRACT_TABLE.CREATED_STATUS;
		saleOrderDTO.priceNotVat = c.getLong(c.getColumnIndex(PRICE_NOT_VAT));
		saleOrderDTO.totalWeight = c.getDouble(c.getColumnIndex(TOTAL_WEIGHT));
		detailViewDTO.convfact = c.getInt(c.getColumnIndex("CONVFACT"));
		saleOrderDTO.vat = c.getDouble(c.getColumnIndex("VAT"));
		detailViewDTO.uom2 = StringUtil.getStringFromSQliteCursor(c, "UOM2");
		detailViewDTO.grossWeight = c.getDouble(c
				.getColumnIndex("GROSS_WEIGHT"));

		detailViewDTO.productCode = c.getString(c
				.getColumnIndex("PRODUCT_CODE"));
		detailViewDTO.productName = c.getString(c
				.getColumnIndex("PRODUCT_NAME"));
		detailViewDTO.stock = c.getLong(c
				.getColumnIndex(STOCK_TOTAL_TABLE.AVAILABLE_QUANTITY));
		detailViewDTO.quantity = String.valueOf(saleOrderDTO.quantity);
		saleOrderDTO.programeTypeCode = c.getString(c.getColumnIndex(PROGRAME_TYPE_CODE));

		detailViewDTO.orderDetailDTO = saleOrderDTO;
		return detailViewDTO;
	}

	/**
	 * 
	 * Khoi tao doi tuong chi tiet don hang cho viet gui server
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param c
	 * @return
	 * @return: OrderDetailViewDTO
	 * @throws:
	 */
	private OrderDetailViewDTO initSaleOrderDetailObject(Cursor c) {
		SaleOrderDetailDTO saleOrderDTO = new SaleOrderDetailDTO();
		OrderDetailViewDTO detailViewDTO = new OrderDetailViewDTO();

		saleOrderDTO.salesOrderDetailId = c.getLong(c
				.getColumnIndex("SALE_ORDER_DETAIL_ID"));
		saleOrderDTO.productId = c.getInt(c.getColumnIndex("PRODUCT_ID"));
		saleOrderDTO.quantity = c.getInt(c.getColumnIndex("QUANTITY"));
		saleOrderDTO.priceId = c.getLong(c.getColumnIndex("PRICE_ID"));
		saleOrderDTO.discountPercentage = c.getFloat(c
				.getColumnIndex("DISCOUNT_PERCENT"));
		saleOrderDTO.discountAmount = c.getLong(c
				.getColumnIndex("DISCOUNT_AMOUNT"));
		saleOrderDTO.isFreeItem = c.getInt(c.getColumnIndex("IS_FREE_ITEM"));
		saleOrderDTO.amount = c.getLong(c.getColumnIndex("AMOUNT"));
		saleOrderDTO.salesOrderId = c
				.getLong(c.getColumnIndex("SALE_ORDER_ID"));
		saleOrderDTO.price = c.getLong(c.getColumnIndex("PRICE"));
		saleOrderDTO.createUser = c.getString(c.getColumnIndex("CREATE_USER"));
		saleOrderDTO.updateUser = c.getString(c.getColumnIndex("UPDATE_USER"));
		saleOrderDTO.createDate = c.getString(c.getColumnIndex("CREATE_DATE"));
		saleOrderDTO.updateDate = c.getString(c.getColumnIndex("UPDATE_DATE"));
		saleOrderDTO.orderDate = c.getString(c.getColumnIndex("ORDER_DATE"));
		saleOrderDTO.maxQuantityFree = c.getInt(c
				.getColumnIndex("MAX_QUANTITY_FREE"));
		// saleOrderDTO.pro =
		// c.getString(c.getColumnIndex("DISPLAY_PROGRAME_CODE"));
		saleOrderDTO.programeCode = c.getString(c
				.getColumnIndex("PROGRAM_CODE"));
		saleOrderDTO.programeType = c.getInt(c.getColumnIndex("PROGRAM_TYPE"));
		saleOrderDTO.shopId = c.getInt(c.getColumnIndex("SHOP_ID"));
		saleOrderDTO.staffId = c.getInt(c.getColumnIndex("STAFF_ID"));
		saleOrderDTO.synState = c.getInt(c.getColumnIndex("SYN_STATE"));
		detailViewDTO.orderDetailDTO = saleOrderDTO;
		return detailViewDTO;

	}

	/**
	 * 
	 * Lay ds sp ban de sua don hang
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param orderId
	 * @return
	 * @throws Exception
	 * @return: List<OrderDetailViewDTO>
	 * @throws:
	 */
	public List<OrderDetailViewDTO> getListProductForEdit(String orderId,
			String orderType) throws Exception {
		Cursor c = null;
		ArrayList<String> params = new ArrayList<String>();
		String objectId = "0";
		int objectType = 1;
		if (orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_PRESALE)) {// presale
			objectType = StockTotalDTO.TYPE_SHOP;
			objectId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
		} else if (orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_VANSALE)) {// vansale
			objectType = StockTotalDTO.TYPE_VANSALE;
			objectId = String.valueOf(GlobalInfo.getInstance().getProfile()
					.getUserData().id);
		} else {
			objectType = StockTotalDTO.TYPE_CUSTOMER;
		}
		params.add(objectId);
		params.add(String.valueOf(objectType));
		params.add(orderId);
		// String[] params = new String[] { orderId };
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT pr.product_id as PRODUCT_ID , pr.product_code as PRODUCT_CODE, pr.product_name as PRODUCT_NAME, sod.price as PRICE, ");
		sql.append(" sod.quantity as ACTUAL, sod.MAX_QUANTITY_FREE as MAX_QUANTITY_FREE, sod.amount as AMOUNT, sod.is_free_item as IS_FREE_ITEM, ");
		sql.append(" sod.program_code AS PROGRAM_CODE, sod.program_type AS PROGRAM_TYPE, sod.PRICE_ID as PRICE_ID,pr.convfact as CONVFACT,  ");
		sql.append(" st.quantity as QUANTITY, st.available_quantity as AVAILABLE_QUANTITY , sod.PRICE_NOT_VAT as PRICE_NOT_VAT , ");
		sql.append(" sod.TOTAL_WEIGHT as  TOTAL_WEIGHT, sod.VAT as VAT, pr.gross_weight as GROSS_WEIGHT, sod.PROGRAME_TYPE_CODE as PROGRAME_TYPE_CODE, ap.ap_param_name  UOM2 ");
		// sql.append(" FROM   sales_order_detail sod, product pr, sales_order so ");
		sql.append(" FROM   sale_order_detail sod, product pr left outer join (SELECT   *  FROM    stock_total ");
		sql.append(" WHERE object_id = ? AND object_type = ? ) st on st.product_id = pr.product_id");
		sql.append(" left outer join ap_param ap on pr.uom2 = ap.ap_param_code AND ap.status = 1 AND ap.type = 'UOM2' ");
		sql.append(" WHERE       1 = 1 ");
		sql.append(" AND sod.product_id = pr.product_id ");
		// sql.append(" AND pr.status = 1 ");
		// sql.append(" and so.sale_order_id = sod.sale_order_id ");
		sql.append(" AND sod.sale_order_id = ? ");
		sql.append(" AND sod.is_free_item = 0 ");
//		sql.append(" AND ap.status = 1 ");
//		sql.append(" AND ap.type = 'UOM2' ");

		ArrayList<OrderDetailViewDTO> v = new ArrayList<OrderDetailViewDTO>();

		try {
			c = rawQuery(sql.toString(),
					params.toArray(new String[params.size()]));

			if (c != null) {
				if (c.moveToFirst()) {
					OrderDetailViewDTO orderJoinTableDTO;

					do {
						orderJoinTableDTO = initSaleOrderDetailObjectFromGetProductStatement(c);
						checkFocusProduct(orderJoinTableDTO);
						v.add(orderJoinTableDTO);

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

		return v;
	}

	/**
	 * 
	 * Dung cho chuc nang chuyen don hang tren ds don hang
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param orderId
	 * @return
	 * @throws Exception
	 * @return: List<OrderDetailViewDTO>
	 * @throws:
	 */
	public List<OrderDetailViewDTO> getListProductForSend(String orderId) {
		Cursor c = null;
		ArrayList<String> params = new ArrayList<String>();
		params.add(orderId);

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT sod.* ");
		sql.append(" FROM   sales_order_detail sod ");
		sql.append(" WHERE       1 = 1 ");
		sql.append(" AND sod.sale_order_id = ? ");
		// sql.append(" AND sod.is_free_item = 0 ");

		ArrayList<OrderDetailViewDTO> v = new ArrayList<OrderDetailViewDTO>();

		try {
			c = rawQuery(sql.toString(),
					params.toArray(new String[params.size()]));

			if (c != null) {
				if (c.moveToFirst()) {
					OrderDetailViewDTO orderJoinTableDTO;

					do {
						orderJoinTableDTO = initSaleOrderDetailObject(c);
						v.add(orderJoinTableDTO);

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

		return v;
	}

	/**
	 * Kiem tra sp co thuoc chuong trinh trong tam hay ko?
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param orderJoinTableDTO
	 * @return: void
	 * @throws:
	 */

	private void checkFocusProduct(OrderDetailViewDTO orderJoinTableDTO)
			throws Exception {
		StringBuilder sql = new StringBuilder();
		String dateNow= DateUtils.now();
		ArrayList<String> params = new ArrayList<String>();
		sql.append(" SELECT distinct fpd.product_id AS product_id ");
		sql.append(" FROM focus_programe fp_, focus_programe_detail fpd, focus_shop_map map , STAFF AS STF ");
		sql.append(" WHERE fp_.status = 1 ");
		sql.append(" AND IFNULL(DATE(fp_.from_date) <= DATE(?),0) ");
		params.add(dateNow);
		sql.append(" AND (IFNULL(DATE(fp_.TO_DATE) >= DATE(?),0) ");
		params.add(dateNow);
		sql.append(" OR DATE(fp_.TO_DATE) IS NULL OR fp_.TO_DATE = '') ");
		sql.append(" AND fp_.focus_program_id = MAP.focus_program_id ");
		sql.append(" AND (map.status = 1 OR map.status = 'true') ");
		sql.append(" AND MAP.FOCUS_SHOP_MAP_ID = fpd.FOCUS_SHOP_MAP_ID ");
		sql.append(" AND MAP.STAFF_TYPE = STF.STAFF_TYPE ");
		sql.append(" AND STF.STAFF_ID = ?");
		params.add(String.valueOf(GlobalInfo.getInstance().getProfile()
				.getUserData().id));
		// sql.append(GlobalInfo.getInstance().getProfile().getUserData().id);
		sql.append(" AND map.shop_id = ?");
		params.add(GlobalInfo.getInstance().getProfile().getUserData().shopId);
		// sql.append(GlobalInfo.getInstance().getProfile().getUserData().shopId);
		sql.append(" AND fpd.product_id = ?");
		params.add(String.valueOf(orderJoinTableDTO.orderDetailDTO.productId));
		// sql.append(orderJoinTableDTO.orderDetailDTO.productId);

		Cursor c = null;
		try {
			c = rawQuery(sql.toString(),
					params.toArray(new String[params.size()]));

			if (c != null) {
				if (c.moveToFirst()) {
					long product_id = c.getLong(c.getColumnIndex("product_id"));
					if (product_id > 0) {
						orderJoinTableDTO.isFocus = 1;
					}
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
	}

	/**
	 * Lay ds san pham khuyen mai cua 1 don hang co san
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param orderId
	 *            : id cua don hang
	 * @return
	 * @return: List<OrderDetailViewDTO>
	 * @throws:
	 */

	public List<OrderDetailViewDTO> getPromotionProductsForEdit(String orderId,
			String orderType) throws Exception {
		Cursor c = null;
		ArrayList<String> params = new ArrayList<String>();
		String objectId = "0";
		int objectType = 1;
		if (orderType.equals("IN")) {// presale
			objectType = StockTotalDTO.TYPE_SHOP;
			objectId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
		} else if (orderType.equals("SO")) {// vansale
			objectType = StockTotalDTO.TYPE_VANSALE;
			objectId = String.valueOf(GlobalInfo.getInstance().getProfile()
					.getUserData().id);
		} else {
			objectType = StockTotalDTO.TYPE_CUSTOMER;
		}
		params.add(objectId);
		params.add(String.valueOf(objectType));
		params.add(orderId);

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT   pr.product_code AS PRODUCT_CODE, ");
		sql.append(" pr.product_name AS PRODUCT_NAME, sod.price AS PRICE, sod.price_id AS PRICE_ID, sod.PRICE_NOT_VAT AS PRICE_NOT_VAT,  sod.TOTAL_WEIGHT AS TOTAL_WEIGHT, ");
		sql.append(" sod.MAX_QUANTITY_FREE AS MAX_QUANTITY_FREE, sod.quantity AS ACTUAL ,sod.discount_amount AS DISCOUNT_AMOUNT, sod.is_free_item as IS_FREE_ITEM, ");
		sql.append(" Case sod.MAX_QUANTITY_FREE When 0 Then 'KM tien' Else 'KM hang' End as typeName , ");
		sql.append(" sod.program_code AS PROGRAM_CODE, sod.program_type AS PROGRAM_TYPE, pr.product_id as PRODUCT_ID, ");
		sql.append(" st.quantity as QUANTITY, st.available_quantity as AVAILABLE_QUANTITY  ");
		sql.append(" ,sod.VAT as VAT, pr.gross_weight as GROSS_WEIGHT, ap.ap_param_name  UOM2, ");
		sql.append(" sod.DISCOUNT_PERCENT as  DISCOUNT_PERCENT, sod.MAX_AMOUNT_FREE as MAX_AMOUNT_FREE, sod.PROGRAME_TYPE_CODE as PROGRAME_TYPE_CODE ");
		sql.append(" FROM   sale_order_detail sod left outer join product pr  on sod.product_id = pr.product_id  ");
		sql.append(" left outer join ap_param ap on pr.uom2 = ap.ap_param_code AND ap.status = 1 AND ap.type = 'UOM2' ");
		sql.append(" left outer join (SELECT   *  FROM    stock_total ");
		sql.append(" WHERE object_id = ? AND object_type = ? ) st on st.product_id = pr.product_id");
		sql.append(" WHERE       1 = 1 ");
		sql.append(" AND sod.sale_order_id = ? ");
		sql.append(" AND sod.is_free_item = 1 ");
//		sql.append(" AND ap.status = 1 ");
//		sql.append(" AND ap.type = 'UOM2' ");

		ArrayList<OrderDetailViewDTO> v = new ArrayList<OrderDetailViewDTO>();

		try {
			c = rawQuery(sql.toString(),
					params.toArray(new String[params.size()]));

			if (c != null) {
				if (c.moveToFirst()) {
					OrderDetailViewDTO orderJoinTableDTO;

					do {
						orderJoinTableDTO = initPromotionProductFromCursor(c);
						v.add(orderJoinTableDTO);

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

		return v;
	}

	/**
	 * 
	 * get list programe for product
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: List<ProgrameForProductDTO>
	 * @throws:
	 */
	public List<ProgrameForProductDTO> getListProgrameForProduct(
			String shop_id, String staff_id, String customer_id,
			String customerTypeId) {
		List<ProgrameForProductDTO> result = new ArrayList<ProgrameForProductDTO>();

		SHOP_TABLE shopTB = new SHOP_TABLE(this.mDB);
		ArrayList<String> listShopId = shopTB.getShopRecursive(shop_id);
		String strListShop = TextUtils.join(",", listShopId);

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * ");
		sql.append("FROM   (SELECT DISTINCT DP.display_program_id   PROMOTION_PROGRAM_ID, ");
		sql.append("                        DP.display_program_code AS PROGRAM_CODE, ");
		sql.append("                        DP.display_program_name AS PROGRAM_NAME, ");
		sql.append("                        'CTTB'                  AP_PARAM_NAME, ");
		sql.append("                        'TB'                    TYPE, ");
		sql.append("                        2                       AS PROGRAME_TYPE, ");
		sql.append("                        0                       COKHAIBAO1, ");
		sql.append("                        0                       COKHAIBAO2, ");
		sql.append("                        0                       COKHAIBAO3, ");
		sql.append("                        0                       COKHAIBAO4, ");
		sql.append("                        0                       COKHAIBAO5, ");
		sql.append("                        0                       COKHAIBAO6, ");
		sql.append("                        0                       COKHAIBAO7, ");
		sql.append("                        0                       COKHAIBAO8, ");
		sql.append("                        0                       COKHAIBAO9, ");
		sql.append("                        0                       COKHAIBAO10 ");
		sql.append("        FROM   display_customer_map DCM, ");
		sql.append("               display_program_level AS DPL, ");
		sql.append("               display_staff_map DSM, ");
		sql.append("               display_program AS DP ");
		sql.append("        WHERE  DCM.display_program_level_id = DPL.display_program_level_id ");
		sql.append("               AND DP.display_program_id = DPL.display_program_id ");
		sql.append("               AND DCM.customer_id = ? ");
		sql.append("               AND Date(DCM.from_date) <= (SELECT Date('now', 'localtime')) ");
		sql.append("               AND Ifnull(Date(DCM.to_date) >= (SELECT Date('now', 'localtime')) ");
		sql.append("                   , 1) ");
		sql.append("               AND DCM.status = 1 ");
		sql.append("               AND DCM.display_staff_map_id = DSM.display_staff_map_id ");
		sql.append("               AND DSM.status = 1 ");
		sql.append("               AND DSM.display_program_id = DP.display_program_id ");
		sql.append("               AND DSM.staff_id = ? ");
		sql.append("               AND DP.status = 1 ");
		sql.append("               AND Date(DP.from_date) <= (SELECT Date('NOW', 'localtime')) ");
		sql.append("               AND Ifnull(( Date(DP.to_date) ) >= (SELECT ");
		sql.append("                          Date('NOW', 'localtime')), 1) ");
		sql.append("               AND DPL.status = 1 ");
		sql.append("        UNION ALL ");
		sql.append("        SELECT DISTINCT * ");
		sql.append("        FROM   (SELECT DISTINCT pp.promotion_program_id PROMOTION_PROGRAM_ID, ");
		sql.append("                                pp.promotion_program_code PROGRAM_CODE, ");
		sql.append("                                pp.promotion_program_name      PROGRAM_NAME, ");
		sql.append("                                ap.ap_param_name               AP_PARAM_NAME, ");
		sql.append("                                pp.type                        TYPE, ");
		sql.append("                                1                              AS PROGRAME_TYPE, ");
		sql.append("                                Ifnull(TTTT1.num_condition, 0) COKHAIBAO1, ");
		sql.append("                                Ifnull(TTTT2.num_condition, 0) COKHAIBAO2, ");
		sql.append("                                Ifnull(TTTT3.num_condition, 0) COKHAIBAO3, ");
		sql.append("                                Ifnull(TTTT4.num_condition, 0) COKHAIBAO4, ");
		sql.append("                                Ifnull(TTTT5.num_condition, 0) COKHAIBAO5, ");
		sql.append("                                Ifnull(TTTT6.num_condition, 0) COKHAIBAO6, ");
		sql.append("                                Ifnull(TTTT7.num_condition, 0) COKHAIBAO7, ");
		sql.append("                                Ifnull(TTTT8.num_condition, 0) COKHAIBAO8, ");
		sql.append("                                Ifnull(TTTT9.num_condition, 0) COKHAIBAO9, ");
		sql.append("                                Ifnull(TTTT10.num_condition, 0) COKHAIBAO10 ");
		sql.append("                FROM   promotion_program pp ");
		sql.append("                       LEFT JOIN (SELECT pca.promotion_program_id, ");
		sql.append("                                         Count (pca.promotion_cust_attr_id) ");
		sql.append("                                         NUM_CONDITION ");
		sql.append("                                  FROM   promotion_cust_attr pca ");
		sql.append("                                  WHERE  pca.object_type = 2 ");
		sql.append("                                         AND pca.status = 1 ");
		sql.append("                                  GROUP  BY pca.promotion_program_id) TTTT1 ");
		sql.append("                              ON TTTT1.promotion_program_id = ");
		sql.append("                                 pp.promotion_program_id ");
		sql.append("                       LEFT JOIN (SELECT pca.promotion_program_id ");
		sql.append("                                         promotion_program_id, ");
		sql.append("                                         Count (pca.promotion_cust_attr_id) ");
		sql.append("                                         NUM_CONDITION ");
		sql.append("                                  FROM   promotion_cust_attr pca, ");
		sql.append("                                         promotion_cust_attr_detail pcad ");
		sql.append("                                  WHERE  1 = 1 ");
		sql.append("                                         AND pca.promotion_cust_attr_id = ");
		sql.append("                                             pcad.promotion_cust_attr_id ");
		sql.append("                                         AND pca.object_type = 2 ");
		sql.append("                                         AND pcad.object_id = ? ");
		sql.append("                                         AND pca.status = 1 ");
		sql.append("                                         AND pcad.status = 1 ");
		sql.append("                                  GROUP  BY pca.promotion_program_id) TTTT2 ");
		sql.append("                              ON TTTT2.promotion_program_id = ");
		sql.append("                                 pp.promotion_program_id ");
		sql.append("                       LEFT JOIN (SELECT pca.promotion_program_id ");
		sql.append("                                         promotion_program_id, ");
		sql.append("                                         Count (promotion_cust_attr_id) ");
		sql.append("                                         NUM_CONDITION ");
		sql.append("                                  FROM   promotion_cust_attr pca ");
		sql.append("                                  WHERE  pca.object_type = 3 ");
		sql.append("                                         AND pca.status = 1 ");
		sql.append("                                  GROUP  BY promotion_program_id) TTTT3 ");
		sql.append("                              ON TTTT3.promotion_program_id = ");
		sql.append("                                 pp.promotion_program_id ");
		sql.append("                       LEFT JOIN (SELECT pca.promotion_program_id ");
		sql.append("                                         promotion_program_id, ");
		sql.append("                                         Count (pca.promotion_cust_attr_id) ");
		sql.append("                                         NUM_CONDITION ");
		sql.append("                                  FROM   promotion_cust_attr pca, ");
		sql.append("                                         promotion_cust_attr_detail pcad, ");
		sql.append("                                         sale_level_cat slc, ");
		sql.append("                                         customer_cat_level ccl ");
		sql.append("                                  WHERE  1 = 1 ");
		sql.append("                                         AND pca.promotion_cust_attr_id = ");
		sql.append("                                             pcad.promotion_cust_attr_id ");
		sql.append("                                         AND pcad.object_id = ");
		sql.append("                                             slc.sale_level_cat_id ");
		sql.append("                                         AND slc.sale_level_cat_id = ");
		sql.append("                                             ccl.sale_level_cat_id ");
		sql.append("                                         AND pca.object_type = 3 ");
		sql.append("                                         AND pca.status = 1 ");
		sql.append("                                         AND pcad.status = 1 ");
		sql.append("                                         AND slc.status = 1 ");
		sql.append("                                         AND ccl.customer_id = ? ");
		sql.append("                                  GROUP  BY pca.promotion_program_id) TTTT4 ");
		sql.append("                              ON TTTT4.promotion_program_id = ");
		sql.append("                                 pp.promotion_program_id ");
		sql.append("                       LEFT JOIN (SELECT pca.promotion_program_id ");
		sql.append("                                         promotion_program_id, ");
		sql.append("                                         Count (promotion_cust_attr_id) ");
		sql.append("                                         NUM_CONDITION ");
		sql.append("                                  FROM   promotion_cust_attr pca, ");
		sql.append("                                         attribute AT ");
		sql.append("                                  WHERE  pca.object_type = 1 ");
		sql.append("                                         AND pca.object_id = AT.attribute_id ");
		sql.append("                                         AND pca.status = 1 ");
		sql.append("                                         AND AT.status = 1 ");
		sql.append("                                         AND AT.value_type IN ( 1, 2, 3 ) ");
		sql.append("                                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                                  GROUP  BY pca.promotion_program_id) TTTT5 ");
		sql.append("                              ON TTTT5.promotion_program_id = ");
		sql.append("                                 pp.promotion_program_id ");
		sql.append("                       LEFT JOIN (SELECT pca.promotion_program_id ");
		sql.append("                                         promotion_program_id, ");
		sql.append("                                         Count (pca.promotion_cust_attr_id) ");
		sql.append("                                         NUM_CONDITION ");
		sql.append("                                  FROM   promotion_cust_attr pca, ");
		sql.append("                                         attribute AT, ");
		sql.append("                                         attribute_value atv ");
		sql.append("                                  WHERE  pca.object_type = 1 ");
		sql.append("                                         AND pca.object_id = AT.attribute_id ");
		sql.append("                                         AND AT.attribute_id = atv.attribute_id ");
		sql.append("                                         AND pca.status = 1 ");
		sql.append("                                         AND AT.status = 1 ");
		sql.append("                                         AND atv.status = 1 ");
		sql.append("                                         AND AT.value_type IN ( 1, 2, 3 ) ");
		sql.append("                                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                                         AND atv.table_name = 'CUSTOMER' ");
		sql.append("                                         AND atv.table_id = ? ");
		sql.append("                                         AND ( CASE ");
		sql.append("                                                 WHEN AT.value_type = 2 THEN ");
		sql.append("                                                 Ifnull( ");
		sql.append("                                                 cast(atv.value as integer) >= pca.from_value, ");
		sql.append("                                                                             1) ");
		sql.append("                                                                             AND ");
		sql.append("                                                 Ifnull(cast(atv.value as integer) <= ");
		sql.append("                                                        pca.to_value, 1) ");
		sql.append("                                                 WHEN AT.value_type = 1 THEN ");
		sql.append("                                                 trim(lower(atv.value)) = trim(lower(pca.from_value)) ");
		sql.append("                                                 WHEN AT.value_type = 3 THEN atv.value is not null and ");
		sql.append("                                                 Ifnull( ");
		sql.append("                                                 Date(atv.value) >= ");
		sql.append("                                                 Date(pca.from_value), ");
		sql.append("                                                          1) ");
		sql.append("                                                          AND ");
		sql.append("                                                 Ifnull(Date(atv.value) <= ");
		sql.append("                                                        Date(pca.to_value), ");
		sql.append("                                                 1) ");
		sql.append("                                               END ) ");
		sql.append("                                  GROUP  BY pca.promotion_program_id) TTTT6 ");
		sql.append("                              ON TTTT6.promotion_program_id = ");
		sql.append("                                 pp.promotion_program_id ");
		
		sql.append("                       LEFT JOIN (SELECT pca.promotion_program_id ");
		sql.append("                                         promotion_program_id, ");
		sql.append("                                         Count (pca.promotion_cust_attr_id) ");
		sql.append("                                         NUM_CONDITION ");
		sql.append("                                  FROM   promotion_cust_attr pca, ");
		sql.append("                                         attribute AT ");
		sql.append("                                  WHERE  pca.object_type = 1 ");
		sql.append("                                         AND pca.object_id = AT.attribute_id ");
		sql.append("                                         AND pca.status = 1 ");
		sql.append("                                         AND AT.status = 1 ");
		sql.append("                                         AND AT.value_type = 4 ");
		sql.append("                                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                                  GROUP  BY pca.promotion_program_id) TTTT7 ");
		sql.append("                              ON TTTT7.promotion_program_id = ");
		sql.append("                                 pp.promotion_program_id ");
		sql.append("                       LEFT JOIN (SELECT pca.promotion_program_id ");
		sql.append("                                         promotion_program_id, ");
		sql.append("                                         Count (pca.promotion_cust_attr_id) ");
		sql.append("                                         NUM_CONDITION ");
		sql.append("                                  FROM   promotion_cust_attr pca, ");
		sql.append("                                         attribute AT, ");
		sql.append("                                         attribute_value atv, ");
		sql.append("                                         attribute_detail atd, ");
		sql.append("                                         attribute_value_detail atvd, ");
		sql.append("                                         promotion_cust_attr_detail pcad ");
		sql.append("                                  WHERE  pca.object_type = 1 ");
		sql.append("                                         AND pca.object_id = AT.attribute_id ");
		sql.append("                                         AND AT.attribute_id = atv.attribute_id ");
		sql.append("                                         AND AT.attribute_id = atd.attribute_id ");
		sql.append("                                         AND atv.attribute_value_id = ");
		sql.append("                                             atvd.attribute_value_id ");
		sql.append("                                         AND atd.attribute_detail_id = ");
		sql.append("                                             atvd.attribute_detail_id ");
		sql.append("                                         AND atd.attribute_detail_id = ");
		sql.append("                                             pcad.object_id ");
		sql.append("                                         AND pca.promotion_cust_attr_id = ");
		sql.append("                                             pcad.promotion_cust_attr_id ");
		sql.append("                                         AND pca.status = 1 ");
		sql.append("                                         AND AT.status = 1 ");
		sql.append("                                         AND atv.status = 1 ");
		sql.append("                                         AND atd.status = 1 ");
		sql.append("                                         AND atvd.status = 1 ");
		sql.append("                                         AND pcad.status = 1 ");
		sql.append("                                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                                         AND AT.value_type = 4 ");
		sql.append("                                         AND atv.table_name = 'CUSTOMER' ");
		sql.append("                                         AND atv.table_id = ? ");
		sql.append("                                  GROUP  BY pca.promotion_program_id)TTTT8 ");
		sql.append("                              ON TTTT8.promotion_program_id = ");
		sql.append("                                 pp.promotion_program_id ");
		
		sql.append("                       LEFT JOIN (SELECT pca.promotion_program_id ");
		sql.append("                                         promotion_program_id, ");
		sql.append("                                         Count (pca.promotion_cust_attr_id) ");
		sql.append("                                         NUM_CONDITION ");
		sql.append("                                  FROM   promotion_cust_attr pca, ");
		sql.append("                                         attribute AT ");
		sql.append("                                  WHERE  pca.object_type = 1 ");
		sql.append("                                         AND pca.object_id = AT.attribute_id ");
		sql.append("                                         AND pca.status = 1 ");
		sql.append("                                         AND AT.status = 1 ");
		sql.append("                                         AND AT.value_type = 5 ");
		sql.append("                                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                                  GROUP  BY pca.promotion_program_id) TTTT9 ");
		sql.append("                              ON TTTT9.promotion_program_id = ");
		sql.append("                                 pp.promotion_program_id ");
		sql.append("                       LEFT JOIN (SELECT pca.promotion_program_id ");
		sql.append("                                         promotion_program_id, ");
		sql.append("                                         Count (pca.promotion_cust_attr_id) ");
		sql.append("                                         NUM_CONDITION ");
		sql.append("                                  FROM   promotion_cust_attr pca, ");
		sql.append("                                         attribute AT, ");
		sql.append("                                         attribute_value atv, ");
		sql.append("                                         attribute_detail atd, ");
		sql.append("                                         attribute_value_detail atvd, ");
		sql.append("                                         promotion_cust_attr_detail pcad ");
		sql.append("                                  WHERE  pca.object_type = 1 ");
		sql.append("                                         AND pca.object_id = AT.attribute_id ");
		sql.append("                                         AND AT.attribute_id = atv.attribute_id ");
		sql.append("                                         AND AT.attribute_id = atd.attribute_id ");
		sql.append("                                         AND atv.attribute_value_id = ");
		sql.append("                                             atvd.attribute_value_id ");
		sql.append("                                         AND atd.attribute_detail_id = ");
		sql.append("                                             atvd.attribute_detail_id ");
		sql.append("                                         AND atd.attribute_detail_id = ");
		sql.append("                                             pcad.object_id ");
		sql.append("                                         AND pca.promotion_cust_attr_id = ");
		sql.append("                                             pcad.promotion_cust_attr_id ");
		sql.append("                                         AND pca.status = 1 ");
		sql.append("                                         AND AT.status = 1 ");
		sql.append("                                         AND atv.status = 1 ");
		sql.append("                                         AND atd.status = 1 ");
		sql.append("                                         AND atvd.status = 1 ");
		sql.append("                                         AND pcad.status = 1 ");
		sql.append("                                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                                         AND AT.value_type = 5 ");
		sql.append("                                         AND atv.table_name = 'CUSTOMER' ");
		sql.append("                                         AND atv.table_id = ? ");
		sql.append("                                  GROUP  BY pca.promotion_program_id)TTTT10 ");
		sql.append("                              ON TTTT10.promotion_program_id = ");
		sql.append("                                 pp.promotion_program_id, ");
		
		sql.append("                       ap_param ap, ");
		sql.append("                       promotion_shop_map psm ");
		sql.append("                       LEFT JOIN promotion_customer_map pcm ");
		sql.append("                              ON pcm.promotion_shop_map_id = ");
		sql.append("                                 psm.promotion_shop_map_id ");
		sql.append("                WHERE  1 = 1 ");
		sql.append("                       AND psm.promotion_program_id = pp.promotion_program_id ");
		sql.append("                       AND psm.shop_id IN ( " + strListShop);
		sql.append("                       ) ");
		sql.append("                       AND pp.status = 1 ");
		sql.append("                       AND ap.status = 1 ");
		sql.append("                       AND psm.status = 1 ");
		sql.append("                       AND Date(pp.from_date) <= Date('now', 'localtime') ");
		sql.append("                       AND Ifnull (Date(pp.to_date) >= Date('now', 'localtime'), ");
		sql.append("                           1) ");
		sql.append("                       AND ( CASE ");
		sql.append("                               WHEN (SELECT Count(*) ");
		sql.append("                                     FROM   promotion_customer_map pcmm ");
		sql.append("                                     WHERE  pcmm.promotion_shop_map_id = ");
		sql.append("                                            psm.promotion_shop_map_id) = 0 ");
		sql.append("                                 THEN ");
		sql.append("                               1 ");
		sql.append("                               ELSE pcm.customer_id = ? ");
		sql.append("                                    AND pcm.status = 1 ");
		sql.append("                             END ) ");
		sql.append("                       AND ( pp.type LIKE 'ZM%' ");
		sql.append("                              OR PP.type IN ( 'ZH', 'ZD', 'ZT' ) ) ");
		sql.append("                       AND ap.ap_param_code = pp.type ");
		sql.append("                       AND ap.type = 'PROMOTION_MANUAL')) ");
		sql.append("ORDER  BY programe_type, ");
		sql.append("          program_code, ");
		sql.append("          program_name ");

		Cursor c = null;
		String[] params = new String[] { customer_id, staff_id, customerTypeId,
				customer_id, customer_id, customer_id, customer_id, customer_id };
		try {
			c = rawQuery(sql.toString(), params);

			if (c != null) {
				if (c.moveToFirst()) {
					do {
						ProgrameForProductDTO programeDTO = new ProgrameForProductDTO();
						programeDTO.initPrograme(c);
						// chi co CTTB va CTKM (phai hop le voi cach tinh KM moi) thi moi them vao d/s
						if(programeDTO.isCTKMInvalid){
							result.add(programeDTO);
						}
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
	 * 
	 * lay d/s san pham de bo xung vao noi dung danh gia NVBH
	 * 
	 * @author: HaiTC3
	 * @param data
	 * @return
	 * @return: ListFindProductSaleOrderDetailViewDTO
	 * @throws:
	 */
	public ListFindProductSaleOrderDetailViewDTO getListProductToAddReviewsStaff(
			Bundle data) {
		String ext = data.getString(IntentConstants.INTENT_PAGE);
		String productCode = data
				.getString(IntentConstants.INTENT_PRODUCT_CODE);
		String productName = data
				.getString(IntentConstants.INTENT_PRODUCT_NAME);
		// shop id cua NVBH
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		SHOP_TABLE shopTB = new SHOP_TABLE(this.mDB);
		ArrayList<String> listShopId = shopTB.getShopRecursive(shopId);
		String strListShop = TextUtils.join(",", listShopId);
		// id cua NVBH
		// String staffId = data.getString(IntentConstants.INTENT_STAFF_ID);
		// id cua khach hang
		String customerId = data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		// type id cua khach hang
		String customerTypeId = data
				.getString(IntentConstants.INTENT_CUSTOMER_TYPE_ID);
		// sale type code cua NVBH
		String saleTypeCode = data
				.getString(IntentConstants.INTENT_SALE_TYPE_CODE);
		// id cua GSNPP
		// String supperStaffId = data
		// .getString(IntentConstants.INTENT_STAFF_OWNER_ID);

		boolean isGetTotalItem = data
				.getBoolean(IntentConstants.INTENT_IS_GET_NUM_TOTAL_ITEM);

		boolean hasProductCode = !StringUtil.isNullOrEmpty(productCode);
		boolean hasProductName = !StringUtil.isNullOrEmpty(productName);
		String dateNow= DateUtils.now();

		if (!StringUtil.isNullOrEmpty(productCode)) {
			productCode = StringUtil.escapeSqlString(productCode);
			productCode = DatabaseUtils
					.sqlEscapeString("%" + productCode + "%");
			productCode = productCode.substring(1, productCode.length() - 1);
		}
		if (!StringUtil.isNullOrEmpty(productName)) {
			productName = StringUtil.getEngStringFromUnicodeString(productName);
			productName = StringUtil.escapeSqlString(productName);
			productName = DatabaseUtils
					.sqlEscapeString("%" + productName + "%");
			productName = productName.substring(1, productName.length() - 1);
		}

		/**
		 * SQL lay danh sach san pham
		 */
		StringBuffer sql = new StringBuffer();
		ArrayList<String> listParams = new ArrayList<String>();
		sql.append("SELECT DISTINCT p.product_id   AS PRODUCT_ID, ");
		sql.append("                pr_.price_id   AS PRICE_ID, ");
		sql.append("                pr_.price      AS PRICE, ");
		sql.append("                p.product_name AS PRODUCT_NAME, ");
		sql.append("                p.product_code AS PRODUCT_CODE, ");
		sql.append("                st.available_quantity AS AVAILABLE_QUANTITY, ");
		sql.append("                ( CASE ");
		sql.append("                    WHEN fp.product_id IS NULL THEN 0 ");
		sql.append("                    ELSE 1 ");
		sql.append("                  END )        AS TT ");

		sql.append("                ,NEW_PROMOTION.COKHAIBAO1       AS COKHAIBAO1 ");
		sql.append("                ,NEW_PROMOTION.COKHAIBAO2       AS COKHAIBAO2 ");
		sql.append("                ,NEW_PROMOTION.COKHAIBAO3       AS COKHAIBAO3 ");
		sql.append("                ,NEW_PROMOTION.COKHAIBAO4       AS COKHAIBAO4 ");
		sql.append("                ,NEW_PROMOTION.COKHAIBAO5       AS COKHAIBAO5 ");
		sql.append("                ,NEW_PROMOTION.COKHAIBAO6       AS COKHAIBAO6 ");
		sql.append("                ,NEW_PROMOTION.COKHAIBAO7       AS COKHAIBAO7 ");
		sql.append("                ,NEW_PROMOTION.COKHAIBAO8       AS COKHAIBAO8 ");
		sql.append("                ,NEW_PROMOTION.COKHAIBAO9       AS COKHAIBAO9 ");
		sql.append("                ,NEW_PROMOTION.COKHAIBAO10       AS COKHAIBAO10 ");
		sql.append("                ,NEW_PROMOTION.code       AS CODE ");

		// check bang product & product info
		sql.append(" FROM   (SELECT p.PRODUCT_CODE       product_code, ");
		sql.append("               p.PRODUCT_ID       product_id, ");
		sql.append("               p.PRODUCT_NAME       product_name, ");
		sql.append("               p.NAME_TEXT  product_name_text, ");
		sql.append("               p.convfact  convfact, ");
		sql.append("               p.GROSS_WEIGHT  GROSS_WEIGHT, ");
		sql.append("               p.uom2  uom2, ");
		sql.append("               p.cat_id cat_id ");
		sql.append("        FROM   PRODUCT p, ");
		sql.append("               PRODUCT_INFO pi ");
		sql.append("        WHERE  p.STATUS = 1 ");
		sql.append("               AND pi.STATUS = 1 ");
		sql.append("               AND pi.TYPE = 1 ");
		sql.append("               AND pi.PRODUCT_INFO_ID = p.CAT_ID ");
		sql.append("               AND pi.PRODUCT_INFO_CODE != 'Z') AS P ");

		// check bang stock total
		sql.append("       INNER JOIN (SELECT quantity, ");
		sql.append("                          product_id, ");
		sql.append("                          available_quantity ");
		sql.append("                   FROM   stock_total WHERE ");
		sql.append("                   object_type = 1 ");
		sql.append("                          AND object_id = ?) st ");
		listParams.add(shopId);
		sql.append("               ON st.product_id = p.product_id ");

		// check bang price
		sql.append("       INNER JOIN (SELECT * ");
		sql.append("                   FROM   price pr ");
		sql.append("                   WHERE   pr.status = 1 ");
		sql.append("                          AND Ifnull(Date(pr.from_date) <= ");
		sql.append("                                     Date (?), 0 ");
		listParams.add(dateNow);
		sql.append("                              ) ");
		sql.append("                          AND  Ifnull(Date(pr.to_date) >= ");
		sql.append("                                       Date(?), 1) ");
		listParams.add(dateNow);
		sql.append("                                 ) AS pr_ ");
		sql.append("               ON p.product_id = pr_.product_id ");

		// mat hang trong tam
		sql.append("   LEFT JOIN ( SELECT DISTINCT ");
		sql.append("       fcmd.product_id, ");
		sql.append("       fp.focus_program_id focus_program_code ");
		sql.append("FROM   focus_program fp, ");
		sql.append("       focus_shop_map fsm, ");
		sql.append("       focus_channel_map fcm, ");
		sql.append("       focus_channel_map_product fcmd ");
		sql.append("WHERE  fp.focus_program_id = fsm.focus_program_id ");
		sql.append("       AND fp.focus_program_id = fcm.focus_program_id ");
		sql.append("       AND fcm.focus_channel_map_id = fcmd.focus_channel_map_id ");
		sql.append("       AND fcm.sale_type_code = ? ");
		listParams.add(saleTypeCode);
		sql.append("       AND date(fp.from_date) <= date(?)");
		listParams.add(dateNow);
		sql.append("       AND ifnull(date(fp.to_date) >= date(?), 1)");
		listParams.add(dateNow);
		sql.append("       AND fp.status = 1 ");
		sql.append("       AND fsm.status = 1 ");
		sql.append("       AND fcm.status = 1 ");
		sql.append("       AND fsm.shop_id IN ( ");
		sql.append(strListShop + ") ");
		sql.append("        ) AS FP");
		sql.append("              ON p.product_id = FP.product_id ");

		// Chuong trinh khuyen mai
		listParams.add(customerTypeId);
		listParams.add(customerId);
		listParams.add(customerId);
		listParams.add(customerId);
		listParams.add(customerId);
		listParams.add(customerId);
		sql.append(" LEFT JOIN ( SELECT distinct pp.promotion_program_code code, pp.promotion_program_id, ");
		sql.append("                ppd.product_id product_id, ");
		sql.append("                Ifnull(TTTT1.num_condition, 0) COKHAIBAO1, ");
		sql.append("                Ifnull(TTTT2.num_condition, 0) COKHAIBAO2, ");
		sql.append("                Ifnull(TTTT3.num_condition, 0) COKHAIBAO3, ");
		sql.append("                Ifnull(TTTT4.num_condition, 0) COKHAIBAO4, ");
		sql.append("                Ifnull(TTTT5.num_condition, 0) COKHAIBAO5, ");
		sql.append("                Ifnull(TTTT6.num_condition, 0) COKHAIBAO6, ");
		sql.append("                Ifnull(TTTT7.num_condition, 0) COKHAIBAO7, ");
		sql.append("                Ifnull(TTTT8.num_condition, 0) COKHAIBAO8, ");
		sql.append("                Ifnull(TTTT9.num_condition, 0) COKHAIBAO9, ");
		sql.append("                Ifnull(TTTT10.num_condition, 0) COKHAIBAO10 ");
		sql.append("FROM   promotion_program pp ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id, ");
		sql.append("                         Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca ");
		sql.append("                  WHERE  pca.object_type = 2 ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT1 ");
		sql.append("              ON TTTT1.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id ");
		sql.append("                         , ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         promotion_cust_attr_detail pcad ");
		sql.append("                  WHERE  1 = 1 ");
		sql.append("                         AND pca.promotion_cust_attr_id = ");
		sql.append("                             pcad.promotion_cust_attr_id ");
		sql.append("                         AND pca.object_type = 2 ");
		sql.append("                         AND pcad.object_id = ? ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND pcad.status = 1 ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT2 ");
		sql.append("              ON TTTT2.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id       promotion_program_id, ");
		sql.append("                         Count (promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca ");
		sql.append("                  WHERE  pca.object_type = 3 ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                  GROUP  BY promotion_program_id) TTTT3 ");
		sql.append("              ON TTTT3.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id ");
		sql.append("                         , ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         promotion_cust_attr_detail pcad, ");
		sql.append("                         sale_level_cat slc, ");
		sql.append("                         customer_cat_level ccl ");
		sql.append("                  WHERE  1 = 1 ");
		sql.append("                         AND pca.promotion_cust_attr_id = ");
		sql.append("                             pcad.promotion_cust_attr_id ");
		sql.append("                         AND pcad.object_id = slc.sale_level_cat_id ");
		sql.append("                         AND slc.sale_level_cat_id = ccl.sale_level_cat_id ");
		sql.append("                         AND pca.object_type = 3 ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND pcad.status = 1 ");
		sql.append("                         AND slc.status = 1 ");
		sql.append("                         AND ccl.customer_id = ? ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT4 ");
		sql.append("              ON TTTT4.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id       promotion_program_id, ");
		sql.append("                         Count (promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         attribute AT ");
		sql.append("                  WHERE  pca.object_type = 1 ");
		sql.append("                         AND pca.object_id = AT.attribute_id ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND AT.status = 1 ");
		sql.append("                         AND AT.value_type IN ( 1, 2, 3 ) ");
		sql.append("                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT5 ");
		sql.append("              ON TTTT5.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id ");
		sql.append("                         , ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         attribute AT, ");
		sql.append("                         attribute_value atv ");
		sql.append("                  WHERE  pca.object_type = 1 ");
		sql.append("                         AND pca.object_id = AT.attribute_id ");
		sql.append("                         AND AT.attribute_id = atv.attribute_id ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND AT.status = 1 ");
		sql.append("                         AND atv.status = 1 ");
		sql.append("                         AND AT.value_type IN ( 1, 2, 3 ) ");
		sql.append("                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                         AND atv.table_name = 'CUSTOMER' ");
		sql.append("                         AND atv.table_id = ? ");
		sql.append("                         AND ( CASE ");
		sql.append("                                 WHEN AT.value_type = 2 THEN Ifnull( ");
		sql.append("                                 cast(atv.value as integer) >= pca.from_value, ");
		sql.append("                                                             1) ");
		sql.append("                                                             AND ");
		sql.append("                                 Ifnull(cast(atv.value as integer) <= pca.to_value, 1) ");
		sql.append("                                 WHEN AT.value_type = 1 THEN ");
		sql.append("                                 trim(lower(atv.value)) = trim(lower(pca.from_value)) ");
		sql.append("                                 WHEN AT.value_type = 3 THEN atv.value is not null and Ifnull( ");
		sql.append("                                 Date(atv.value) >= Date(pca.from_value), ");
		sql.append("                                                             1) ");
		sql.append("                                                             AND ");
		sql.append("                                 Ifnull(Date(atv.value) <= Date(pca.to_value), ");
		sql.append("                                 1) ");
		sql.append("                               END ) ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT6 ");
		sql.append("              ON TTTT6.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id, ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         attribute AT ");
		sql.append("                  WHERE  pca.object_type = 1 ");
		sql.append("                         AND pca.object_id = AT.attribute_id ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND AT.status = 1 ");
		sql.append("                         AND AT.value_type = 4 ");
		sql.append("                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT7 ");
		sql.append("              ON TTTT7.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id, ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         attribute AT, ");
		sql.append("                         attribute_value atv, ");
		sql.append("                         attribute_detail atd, ");
		sql.append("                         attribute_value_detail atvd, ");
		sql.append("                         promotion_cust_attr_detail pcad ");
		sql.append("                  WHERE  pca.object_type = 1 ");
		sql.append("                         AND pca.object_id = AT.attribute_id ");
		sql.append("                         AND AT.attribute_id = atv.attribute_id ");
		sql.append("                         AND AT.attribute_id = atd.attribute_id ");
		sql.append("                         AND atv.attribute_value_id = atvd.attribute_value_id ");
		sql.append("                         AND atd.attribute_detail_id = atvd.attribute_detail_id ");
		sql.append("                         AND atd.attribute_detail_id = pcad.object_id ");
		sql.append("                         AND pca.promotion_cust_attr_id = ");
		sql.append("                             pcad.promotion_cust_attr_id ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND AT.status = 1 ");
		sql.append("                         AND atv.status = 1 ");
		sql.append("                         AND atd.status = 1 ");
		sql.append("                         AND atvd.status = 1 ");
		sql.append("                         AND pcad.status = 1 ");
		sql.append("                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                         AND AT.value_type = 4 ");
		sql.append("                         AND atv.table_name = 'CUSTOMER' ");
		sql.append("                         AND atv.table_id = ? ");
		sql.append("                  GROUP  BY pca.promotion_program_id)TTTT8 ");
		sql.append("              ON TTTT8.promotion_program_id = pp.promotion_program_id ");

		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id, ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         attribute AT ");
		sql.append("                  WHERE  pca.object_type = 1 ");
		sql.append("                         AND pca.object_id = AT.attribute_id ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND AT.status = 1 ");
		sql.append("                         AND AT.value_type = 5 ");
		sql.append("                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT9 ");
		sql.append("              ON TTTT9.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id, ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         attribute AT, ");
		sql.append("                         attribute_value atv, ");
		sql.append("                         attribute_detail atd, ");
		sql.append("                         attribute_value_detail atvd, ");
		sql.append("                         promotion_cust_attr_detail pcad ");
		sql.append("                  WHERE  pca.object_type = 1 ");
		sql.append("                         AND pca.object_id = AT.attribute_id ");
		sql.append("                         AND AT.attribute_id = atv.attribute_id ");
		sql.append("                         AND AT.attribute_id = atd.attribute_id ");
		sql.append("                         AND atv.attribute_value_id = atvd.attribute_value_id ");
		sql.append("                         AND atd.attribute_detail_id = atvd.attribute_detail_id ");
		sql.append("                         AND atd.attribute_detail_id = pcad.object_id ");
		sql.append("                         AND pca.promotion_cust_attr_id = ");
		sql.append("                             pcad.promotion_cust_attr_id ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND AT.status = 1 ");
		sql.append("                         AND atv.status = 1 ");
		sql.append("                         AND atd.status = 1 ");
		sql.append("                         AND atvd.status = 1 ");
		sql.append("                         AND pcad.status = 1 ");
		sql.append("                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                         AND AT.value_type = 5 ");
		sql.append("                         AND atv.table_name = 'CUSTOMER' ");
		sql.append("                         AND atv.table_id = ? ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT10 ");
		sql.append("              ON TTTT10.promotion_program_id = pp.promotion_program_id, ");

		sql.append("       promotion_program_detail ppd, ");
		sql.append("       promotion_shop_map psm ");
		sql.append("       LEFT JOIN promotion_customer_map pcm ");
		sql.append("              ON pcm.promotion_shop_map_id = psm.promotion_shop_map_id ");
		sql.append("WHERE  1 = 1 ");
		sql.append("       AND psm.promotion_program_id = pp.promotion_program_id ");
		sql.append("       AND pp.promotion_program_id = ppd.promotion_program_id ");
		sql.append("       AND psm.shop_id IN ( ");
		sql.append(strListShop + " )");
		sql.append("       AND pp.status = 1 ");
		sql.append("       AND psm.status = 1 ");
		sql.append("       AND Date(pp.from_date) <= Date(?) ");
		listParams.add(dateNow);
		sql.append("       AND Ifnull (Date(pp.to_date) >= Date(?), 1) ");
		listParams.add(dateNow);
		sql.append("       AND ( CASE ");
		sql.append("               WHEN (SELECT Count(*) ");
		sql.append("                     FROM   promotion_customer_map pcmm ");
		sql.append("                     WHERE  pcmm.promotion_shop_map_id = ");
		sql.append("                            psm.promotion_shop_map_id) = 0 ");
		sql.append("                 THEN ");
		sql.append("               1 ");
		sql.append("               ELSE pcm.customer_id = ? ");
		sql.append("                    AND pcm.status = 1 ");
		sql.append("             END ) ) NEW_PROMOTION on NEW_PROMOTION.product_id = p.product_id ");

		// dk where
		sql.append(" WHERE  1 = 1 ");
		sql.append("       AND PR_.price > 0 ");

		if (hasProductCode) {
			sql.append("AND upper(p.product_code) LIKE upper(?) escape '^' ");
			listParams.add(productCode);
		}
		if (hasProductName) {
			sql.append("AND upper(p.product_name_text) LIKE upper(?) escape '^' ");
			listParams.add(productName);
		}
		sql.append("ORDER BY tt DESC, product_code ASC, product_name ASC");

		/**
		 * sql lay tong so luong dong
		 */
		String getCountProductList = " select count(*) as total_row from ("
				+ sql.toString() + ") ";

		// khoi tao tham so cho sql lay danh sach san pham
		String[] paramsGetListProduct = new String[listParams.size()];
		paramsGetListProduct = listParams
				.toArray(paramsGetListProduct);

		ListFindProductSaleOrderDetailViewDTO listResult = new ListFindProductSaleOrderDetailViewDTO();
		
		Cursor cTmp = null;
		//long startTime = System.currentTimeMillis();
		try {
			// get total row first
			int total = 0;
			if (isGetTotalItem) {
				cTmp = rawQuery(getCountProductList, paramsGetListProduct);
				if (cTmp != null) {
					cTmp.moveToFirst();
					total = cTmp.getInt(0);
					listResult.totalObject = total;
				}
			}
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			if (isGetTotalItem && cTmp != null) {
				cTmp.close();
			}
		}
		Cursor c = null;
		try{
			c = rawQuery(sql + ext,
					paramsGetListProduct);

			if (c != null) {

				if (c.moveToFirst()) {
					do {
						FindProductSaleOrderDetailViewDTO orderJoinTableDTO = new FindProductSaleOrderDetailViewDTO();
						orderJoinTableDTO
								.initSaleOrderDetailObjectFromGetProductStatement(c);
						listResult.listObject.add(orderJoinTableDTO);
					} while (c.moveToNext());
				}
			}

		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return listResult;
	}

	/**
	 * delete chi tiet don hang truoc 3 thang
	 * @author banghn
	 * @return
	 */
	public long deleteOldSaleOrderDetail() {
		long success = -1;
		try {
			String startOfMonth = DateUtils
					.getFirstDateOfNumberPreviousMonthWithFormat(
							DateUtils.DATE_FORMAT_SQL_DEFAULT, -2);
			String[] params = {startOfMonth};
			//mDB.beginTransaction();
			//xoa don hang detail giu lai trong 3 thang hien tai
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
