/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteStatement;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.text.TextUtils;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.ProductDTO;
import com.viettel.dms.dto.db.SaleOrderDetailDTO;
import com.viettel.dms.dto.db.StockTotalDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.C2PurchaseDetailViewDTO;
import com.viettel.dms.dto.view.C2PurchaselViewDTO;
import com.viettel.dms.dto.view.C2SaleOrderDetailViewDTO;
import com.viettel.dms.dto.view.C2SaleOrderlViewDTO;
import com.viettel.dms.dto.view.ForcusProductOfNVBHDTO;
import com.viettel.dms.dto.view.ListProductDTO;
import com.viettel.dms.dto.view.NVBHReportForcusProductInfoViewDTO;
import com.viettel.dms.dto.view.OrderDetailViewDTO;
import com.viettel.dms.dto.view.SaleProductInfoDTO;
import com.viettel.dms.dto.view.SaleStatisticsAccumulateDayDTO;
import com.viettel.dms.dto.view.SaleStatisticsProductInDayInfoViewDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * Luu thong tin san pham
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class PRODUCT_TABLE extends ABSTRACT_TABLE {
	// id san pham
	public static final String PRODUCT_ID = "PRODUCT_ID";
	// ten san pham
	public static final String PRODUCT_NAME = "PRODUCT_NAME";
	// trang thai 1: hoat dong, 0: ngung
	public static final String STATUS = "STATUS";
	// don vi tinh nho nhat (hop)
	public static final String UOM1 = "UOM1";
	// package (thung)
	public static final String UOM2 = "UOM2";
	// gia tri quy doi tu UOM2 --> UOM1
	public static final String CONVFACT = "CONVFACT";
	// ma nganh hang
	public static final String CATEGORY_CODE = "CATEGORY_CODE";
	public static final String SUBCATEGORY_ID = "SUBCATEGORY_ID";
	// thuoc tinh cua mat hang
	public static final String BRAND = "BRAND";
	// thuoc tinh cua mat hang
	public static final String FLAVOUR = "FLAVOUR";
	// ton kho an toan - chua dung
	public static final String SAFETYSTOCK = "SAFETYSTOCK";
	// hoa hong - chua dung
	public static final String COMMISSION = "COMMISSION";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// thuoc tinh cua mat hang
	public static final String VOLUMN = "VOLUMN";
	// thuoc tinh cua mat hang
	public static final String NET_WEIGHT = "NET_WEIGHT";
	// thuoc tinh cua mat hang
	public static final String GROSS_WEIGHT = "GROSS_WEIGHT";
	// thuoc tinh cua mat hang
	public static final String PACKING = "PACKING";
	public static final String PRODUCT_TYPE_ID = "PRODUCT_TYPE_ID";
	// ma mat hang
	public static final String PRODUCT_CODE = "PRODUCT_CODE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// loai san pham
	public static final String PRODUCT_TYPE = "PRODUCT_TYPE";
	// sub cat cua sang pham
	public static final String SUB_CAT = "SUB_CAT";

	public static final String PRODUCT_NAME_TABLE = "PRODUCT";

	/**
	 * tao va mo mot CSDL
	 * 
	 * @author: BangHN
	 * @return: SQLiteUtil
	 * @throws:
	 */
	public PRODUCT_TABLE() {
		this.tableName = PRODUCT_NAME_TABLE;
		this.columns = new String[] { PRODUCT_ID, PRODUCT_NAME, STATUS, UOM1, UOM2, CONVFACT, CATEGORY_CODE,
				SUBCATEGORY_ID, BRAND, FLAVOUR, SAFETYSTOCK, COMMISSION, CREATE_DATE, UPDATE_DATE, VOLUMN, NET_WEIGHT,
				GROSS_WEIGHT, PACKING, PRODUCT_TYPE_ID, PRODUCT_CODE, CREATE_USER, UPDATE_USER, PRODUCT_TYPE, SUB_CAT,
				SYN_STATE };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = SQLUtils.getInstance().getmDB();
	}

	public PRODUCT_TABLE(SQLiteDatabase mDB) {
		this.tableName = PRODUCT_NAME_TABLE;
		this.columns = new String[] { PRODUCT_ID, PRODUCT_NAME, STATUS, UOM1, UOM2, CONVFACT, CATEGORY_CODE,
				SUBCATEGORY_ID, BRAND, FLAVOUR, SAFETYSTOCK, COMMISSION, CREATE_DATE, UPDATE_DATE, VOLUMN, NET_WEIGHT,
				GROSS_WEIGHT, PACKING, PRODUCT_TYPE_ID, PRODUCT_CODE, CREATE_USER, UPDATE_USER, PRODUCT_TYPE, SUB_CAT };
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
			GlobalInfo.getInstance().getAppContext().deleteDatabase(Constants.DATABASE_NAME);
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
	 * them 1 dong xuong CSDL
	 * 
	 * @author: BangHN
	 * @param dto
	 * @return
	 * @return: long
	 * @throws:
	 */
	public long insert(ProductDTO dto) {
		ContentValues value = initDataRow(dto);
		return insert(null, value);
	}

	@Override
	protected long insert(AbstractTableDTO dto) {
		ContentValues value = initDataRow((ProductDTO) dto);
		return insert(null, value);
	}

	/**
	 * thay doi 1 dong cua CSDL
	 * 
	 * @author: BangHN
	 * @param id
	 * @param dto
	 * @return
	 * @return: int
	 * @throws:
	 */
	public int update(ProductDTO dto) {
		ContentValues value = initDataRow(dto);
		String[] params = { "" + dto.productId };
		return update(value, PRODUCT_ID + " = ?", params);
	}

	public long update(AbstractTableDTO dto) {
		ProductDTO pro = (ProductDTO) dto;
		ContentValues value = initDataRow(pro);
		String[] params = { "" + pro.productId };
		return update(value, PRODUCT_ID + " = ?", params);
	}

	/**
	 * 
	 * Xoa 1 dong cua CSDL
	 * 
	 * @author: HieuNH
	 * @param id
	 * @return
	 * @return: int
	 * @throws:
	 */
	public int delete(String id) {
		String[] params = { id };
		return delete(PRODUCT_ID + " = ?", params);
	}

	public long delete(AbstractTableDTO dto) {
		ProductDTO proDTO = (ProductDTO) dto;
		String[] params = { Integer.toString(proDTO.productId) };
		return delete(PRODUCT_ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: DoanDM replaced
	 * @param id
	 * @return: ProductDTO
	 * @throws:
	 */
	public ProductDTO getProductById(String id) {
		ProductDTO ProductDTO = null;
		Cursor c = null;
		try {
			String[] params = { id };
			c = query(PRODUCT_ID + " = ?", params, null, null, null);
		} catch (Exception ex) {
			c = null;
		}
		if (c != null) {
			if (c.moveToFirst()) {
				ProductDTO = initLogDTOFromCursor(c);
			}
		}
		if (c != null) {
			c.close();
		}
		return ProductDTO;
	}

	private ProductDTO initLogDTOFromCursor(Cursor c) {
		ProductDTO productDTO = new ProductDTO();
		productDTO.productId = (c.getInt(c.getColumnIndex(PRODUCT_ID)));
		productDTO.productName = (c.getString(c.getColumnIndex(PRODUCT_NAME)));
		productDTO.status = (c.getInt(c.getColumnIndex(STATUS)));
		productDTO.uom1 = (c.getString(c.getColumnIndex(UOM1)));
		productDTO.uom2 = (c.getString(c.getColumnIndex(UOM2)));
		productDTO.convfact = (c.getInt(c.getColumnIndex(CONVFACT)));
		productDTO.categoryCode = (c.getString(c.getColumnIndex(CATEGORY_CODE)));

		productDTO.subCategoryId = (c.getString(c.getColumnIndex(SUBCATEGORY_ID)));
		productDTO.brand = (c.getString(c.getColumnIndex(BRAND)));
		productDTO.flavour = (c.getString(c.getColumnIndex(FLAVOUR)));
		productDTO.safetyStock = (c.getFloat(c.getColumnIndex(SAFETYSTOCK)));
		productDTO.commission = (c.getString(c.getColumnIndex(COMMISSION)));
		productDTO.createDate = (c.getString(c.getColumnIndex(CREATE_DATE)));
		productDTO.udpateDate = (c.getString(c.getColumnIndex(UPDATE_DATE)));
		productDTO.volumn = (c.getFloat(c.getColumnIndex(VOLUMN)));
		productDTO.netweight = (c.getFloat(c.getColumnIndex(NET_WEIGHT)));
		productDTO.grossWeight = (c.getFloat(c.getColumnIndex(GROSS_WEIGHT)));
		productDTO.packing = (c.getString(c.getColumnIndex(PACKING)));
		productDTO.productTypeId = (c.getInt(c.getColumnIndex(PRODUCT_TYPE_ID)));
		productDTO.productCode = (c.getString(c.getColumnIndex(PRODUCT_CODE)));
		productDTO.createUser = (c.getString(c.getColumnIndex(CREATE_USER)));
		productDTO.udpateUser = (c.getString(c.getColumnIndex(UPDATE_USER)));
		productDTO.productType = (c.getString(c.getColumnIndex(PRODUCT_TYPE)));
		productDTO.subCat = (c.getString(c.getColumnIndex(SUB_CAT)));

		return productDTO;
	}

	/**
	 * lay tat ca cac dong cua CSDL
	 * 
	 * @author: BangHN
	 * @return
	 * @return: Vector<ProductDTO>
	 * @throws:
	 */
	public Vector<ProductDTO> getAllRow() {
		Vector<ProductDTO> v = new Vector<ProductDTO>();
		Cursor c = null;
		try {
			c = query(null, null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			ProductDTO ProductDTO;
			if (c.moveToFirst()) {
				do {
					ProductDTO = initLogDTOFromCursor(c);
					v.addElement(ProductDTO);
				} while (c.moveToNext());
			}
			c.close();
		}
		return v;

	}

	private ContentValues initDataRow(ProductDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(PRODUCT_ID, dto.productId);
		editedValues.put(PRODUCT_NAME, dto.productName);
		editedValues.put(STATUS, dto.status);
		editedValues.put(UOM1, dto.uom1);
		editedValues.put(UOM2, dto.uom2);
		editedValues.put(CONVFACT, dto.convfact);
		editedValues.put(CATEGORY_CODE, dto.categoryCode);
		editedValues.put(SUBCATEGORY_ID, dto.subCategoryId);
		editedValues.put(BRAND, dto.brand);
		editedValues.put(FLAVOUR, dto.flavour);
		editedValues.put(SAFETYSTOCK, dto.safetyStock);
		editedValues.put(COMMISSION, dto.commission);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(UPDATE_DATE, dto.udpateDate);
		editedValues.put(VOLUMN, dto.volumn);
		editedValues.put(NET_WEIGHT, dto.netweight);
		editedValues.put(GROSS_WEIGHT, dto.grossWeight);
		editedValues.put(PACKING, dto.packing);
		editedValues.put(PRODUCT_TYPE_ID, dto.productTypeId);
		editedValues.put(PRODUCT_CODE, dto.productCode);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(UPDATE_USER, dto.udpateUser);
		editedValues.put(PRODUCT_TYPE, dto.productType);
		editedValues.put(SUB_CAT, dto.subCat);

		return editedValues;
	}

	/**
	 * 
	 * Lay thong tin cua 1 san pham khuyen mai
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param productID
	 * @param nvbhShopId
	 * @return
	 * @return: OrderDetailViewDTO
	 * @throws:
	 */
	public OrderDetailViewDTO getProductStockByID(String productID, String orderType) {
		StringBuilder sqlObject = new StringBuilder();
		String dateNow= DateUtils.now();
		ArrayList<String> params = new ArrayList<String>();
		String objectId = "0";
		int objectType = 1;
		if (orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_PRESALE)) {// presale
			objectType = StockTotalDTO.TYPE_SHOP;
			objectId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
		} else if (orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_VANSALE)) {// vansale
			objectType = StockTotalDTO.TYPE_VANSALE;
			objectId = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);
		} else {
			objectType = StockTotalDTO.TYPE_CUSTOMER;
		}
		params.add(objectId);
		params.add(String.valueOf(objectType));
		params.add(productID);
		params.add(productID);

		sqlObject
				.append("SELECT p.product_code as PRODUCT_CODE, p.GROSS_WEIGHT as GROSS_WEIGHT, p.product_name as PRODUCT_NAME, ");
		sqlObject.append(" p.convfact AS CONVFACT , pr.price as PRICE, pr.price_id as PRICE_ID, pr.VAT as VAT, ");
		sqlObject
				.append(" st.quantity as QUANTITY, st.available_quantity as AVAILABLE_QUANTITY, pr.price_not_vat as PRICE_NOT_VAT  ");
		sqlObject.append(" FROM product p left outer join (SELECT   *  FROM    stock_total ");
		sqlObject
				.append(" WHERE   object_id = ? AND object_type = ? AND product_id = ?) st on st.product_id = p.product_id ");

		sqlObject.append(" left outer join (select * from price ");
		sqlObject
				.append(" WHERE status = 1 AND DATE(FROM_DATE) <= DATE(?) AND IFNULL(DATE(TO_DATE) >= DATE(?), 1)) pr ");
		params.add(dateNow);
		params.add(dateNow);
		sqlObject.append(" on pr.product_id = p.product_id  ");
		sqlObject.append(" WHERE p.product_id = ? ");

		OrderDetailViewDTO productPromotion = null;
		Cursor c = null;
		try {
			c = rawQuery(sqlObject.toString(), params.toArray(new String[params.size()]));

			if (c != null) {
				if (c.moveToFirst()) {
					productPromotion = initPromotionProductInfo(c);
				}
			}

		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}

		return productPromotion;

	}

	/**
	 * 
	 * Tao mot doi tuong khuyen mai voi gia tri ton kho con lai
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param cursor
	 * @return: OrderDetailViewDTO
	 * @throws:
	 */
	OrderDetailViewDTO initPromotionProductInfo(Cursor cursor) {
		OrderDetailViewDTO productPromotion = new OrderDetailViewDTO();
		SaleOrderDetailDTO productInfo = new SaleOrderDetailDTO();
		productPromotion.orderDetailDTO = productInfo;

		try {
			productPromotion.productCode = cursor.getString(cursor.getColumnIndex(PRODUCT_CODE));
			productPromotion.productName = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME));
			productPromotion.convfact = cursor.getInt(cursor.getColumnIndex(CONVFACT));
			productPromotion.stock = cursor.getLong(cursor.getColumnIndex(STOCK_TOTAL_TABLE.AVAILABLE_QUANTITY));
			productPromotion.orderDetailDTO.price = cursor.getLong(cursor.getColumnIndex(PRICE_TABLE.PRICE));
			productPromotion.orderDetailDTO.priceNotVat = cursor.getLong(cursor
					.getColumnIndex(PRICE_TABLE.PRICE_NOT_VAT));
			productPromotion.orderDetailDTO.priceId = cursor.getLong(cursor.getColumnIndex(PRICE_TABLE.PRICE_ID));
			productPromotion.grossWeight = cursor.getDouble(cursor.getColumnIndex(PRODUCT_TABLE.GROSS_WEIGHT));
			productPromotion.orderDetailDTO.vat = cursor.getDouble(cursor.getColumnIndex(PRICE_TABLE.VAT));
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}

		return productPromotion;
	}

	/**
	 * 
	 * lay thong tin chi tiet san pham
	 * 
	 * @author: HaiTC3
	 * @param productId
	 * @return
	 * @return: ProductDTO
	 * @throws:
	 * @since: Feb 4, 2013
	 */
	public ProductDTO getProductInfoDetail(String productId) {
		ProductDTO dto = new ProductDTO();
		StringBuffer sqlObject = new StringBuffer();
		sqlObject.append("SELECT DISTINCT p.product_code, ");
		sqlObject.append("                p.product_name, ");
		sqlObject.append("                p.convfact, ");
		sqlObject.append("                pi.product_info_code         CATEGORY_CODE, ");
		sqlObject.append("                (SELECT ap.ap_param_name ");
		sqlObject.append("                 FROM   ap_param ap ");
		sqlObject.append("                 WHERE  p.uom2 = ap.ap_param_code ");
		sqlObject.append("                        AND ap.status = 1 ");
		sqlObject.append("                        AND ap.type = 'UOM2') UOM2, ");
		sqlObject.append("                (SELECT ap.ap_param_name ");
		sqlObject.append("                 FROM   ap_param ap ");
		sqlObject.append("                 WHERE  p.uom1 = ap.ap_param_code ");
		sqlObject.append("                        AND ap.status = 1 ");
		sqlObject.append("                        AND ap.type = 'UOM1') UOM1 ");
		sqlObject.append("FROM   product p, ");
		sqlObject.append("       product_info pi, ");
		sqlObject.append("       ap_param ap ");
		sqlObject.append("WHERE  p.product_id = ? ");
		sqlObject.append("       AND pi.status = 1 ");
		sqlObject.append("       AND pi.type = 1 ");
		sqlObject.append("       AND AP.STATUS =1 ");
		sqlObject.append("       AND pi.product_info_id = p.cat_id ");

		String[] param = new String[] { productId };
		Cursor c = null;
		try {
			c = rawQuery(sqlObject.toString(), param);

			if (c != null) {
				if (c.moveToFirst()) {
					dto.initDataWithCursor(c);
				}
			}

		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return dto;
	}

	/**
	 * 
	 * Lay danh sach san pham NVBH, GSBH
	 * 
	 * @author: YenNTH
	 * @param data
	 * @return
	 * @return: ListProductDTO
	 * @throws:
	 */
	public ListProductDTO getProductList(Bundle data, ArrayList<String> shopIdArray) {
		ListProductDTO dto = null;
		String dateNow = DateUtils.now();
		String page = data.getString(IntentConstants.INTENT_PAGE);
		String productCode = data.getString(IntentConstants.INTENT_PRODUCT_CODE);
		String productName = data.getString(IntentConstants.INTENT_PRODUCT_NAME);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		String idListString = TextUtils.join(",", shopIdArray);
		int isGetTotalPage = data.getInt(IntentConstants.INTENT_GET_TOTAL_PAGE);
		int objectType;
		String objectId = null;
		if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES) {
			objectType = StockTotalDTO.TYPE_SHOP;
			objectId = shopId;
			// Vansale lay ton kho NPP
		} else if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_VALSALES) {
			// objectType = StockTotalDTO.TYPE_VANSALE;
			objectType = StockTotalDTO.TYPE_SHOP;
			objectId = shopId;
		} else {
			objectType = StockTotalDTO.TYPE_CUSTOMER;
		}
		StringBuffer totalPageSql = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();
		StringBuffer var1 = new StringBuffer();
		var1.append("SELECT DISTINCT PD.PRODUCT_ID         AS PRODUCT_ID, ");
		var1.append("       PD.PRODUCT_CODE                AS PRODUCT_CODE, ");
		var1.append("       PD.CONVFACT                    AS CONVFACT, ");
		var1.append("       PD.PRODUCT_NAME                AS PRODUCT_NAME, ");
		var1.append("       ( CASE ");
		var1.append("          WHEN PD.SORT_ORDER IS NULL THEN 'z' ");
		var1.append("          ELSE SORT_ORDER ");
		var1.append("          END )        AS SORT_ORDER, ");
		var1.append("                (SELECT ap.ap_param_name ");
		var1.append("                 FROM   ap_param ap ");
		var1.append("                 WHERE  PD.UOM2 = ap.ap_param_code ");
		var1.append("                        AND ap.status = 1 ");
		var1.append("                        AND ap.type = 'UOM2') UOM2, ");
		var1.append("       PD.NAME_TEXT                   AS PRODUCT_NAME_TEXT, ");
		var1.append("       PD.PRODUCT_INFO_CODE           AS CATEGORY_CODE, ");
		var1.append("       PD.CREATE_DATE                 AS CREATE_DATE, ");
		var1.append("       ST.AVAILABLE_QUANTITY          AS AVAILABLE_QUANTITY, ");
		var1.append("                ( CASE ");
		var1.append("                    WHEN pro.product_id IS NULL THEN 0 ");
		var1.append("                    ELSE 1 ");
		var1.append("                  END )        AS HAS_PROMOTION ");
		var1.append("FROM   (SELECT *  FROM PRODUCT, PRODUCT_INFO WHERE PRODUCT.CAT_ID = PRODUCT_INFO.PRODUCT_INFO_ID AND PRODUCT_INFO.OBJECT_TYPE = 0 AND PRODUCT_INFO.type = 1 AND PRODUCT.STATUS = 1 AND PRODUCT_INFO.STATUS = 1 ) PD ");
		var1.append("      LEFT JOIN (SELECT * ");
		var1.append("             FROM   STOCK_TOTAL ");
		var1.append("             WHERE  OBJECT_ID = ? AND OBJECT_TYPE = ? ) ST ");
		param.add(objectId);
		param.add(String.valueOf(objectType));
		var1.append("         ON PD.PRODUCT_ID = ST.PRODUCT_ID ");
		// Chuong trinh khuyen mai
		var1.append("      LEFT JOIN ( SELECT distinct  ");
		var1.append("       ppd.product_id            product_id ");
		var1.append("FROM   promotion_program pp, ");
		var1.append("       promotion_program_detail ppd, ");
		var1.append("       promotion_shop_map psm ");
		var1.append("WHERE  pp.promotion_program_id = ppd.promotion_program_id ");
		var1.append("       AND pp.promotion_program_id = psm.promotion_program_id ");
		var1.append("       AND Date(pp.from_date) <= Date(?) ");
		param.add(dateNow);
		var1.append("       AND Ifnull(Date(pp.to_date) >= Date(?), 1) ");
		param.add(dateNow);
		var1.append("       AND pp.status = 1 ");
		var1.append("       AND psm.status = 1 ");
		var1.append("       AND psm.shop_id in (");
		var1.append(idListString);
		var1.append(") ");
		var1.append("       ) as pro");
		var1.append("       ON pd.product_id = pro.product_id ");
		var1.append("WHERE  1 = 1 ");
		var1.append("       AND UOM2 is not null ");
		var1.append("       AND CONVFACT > 1 ");
		var1.append("       AND DATE(PD.CREATE_DATE) <= DATE(?) ");
		param.add(dateNow);
		var1.append("       AND IFNULL(DATE(PD.UPDATE_DATE) <= DATE(?), 1) ");
		param.add(dateNow);
		if (!StringUtil.isNullOrEmpty(productCode)) {
			productCode = StringUtil.escapeSqlString(productCode);
			productCode = DatabaseUtils.sqlEscapeString("%" + productCode + "%");
			productCode = productCode.substring(1, productCode.length()-1);
			var1.append("	and upper(PRODUCT_CODE) like upper(?) escape '^' ");
			param.add(productCode);
		}
		if (!StringUtil.isNullOrEmpty(productName)) {
			productName = StringUtil.getEngStringFromUnicodeString(productName);
			productName = StringUtil.escapeSqlString(productName);
			productName = DatabaseUtils.sqlEscapeString("%" + productName + "%");
			productName = productName.substring(1, productName.length()-1);
			var1.append("	and upper(PRODUCT_NAME_TEXT) like upper(?) escape '^' ");
			param.add(productName);
		}
		var1.append("ORDER BY SORT_ORDER, has_promotion DESC, product_code ASC, product_name ASC");
		totalPageSql.append("select COUNT(*) as TOTAL_ROW from (" + var1 + ")");
		var1.append(page);
		Cursor c = null;
		try {
			c = rawQueries(var1.toString(), param);
			if (c != null) {
				if (c.moveToFirst()) {
					dto = new ListProductDTO();
					do {
						ProductDTO productInfo = new ProductDTO();
						productInfo.initDataFromCursor(c);
						dto.producList.add(productInfo);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (c != null) {
				c.close();
			}
		}
		Cursor cTotal =null;		
		try {
			if (isGetTotalPage == 1) {
				cTotal = rawQueries(totalPageSql.toString(), param);
				if (cTotal != null) {
					if (cTotal.moveToFirst()) {
						dto.total = cTotal.getInt(0);
					}
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (cTotal != null) {
				cTotal.close();
			}
		}

		return dto;
	}

	/**
	 * 
	 * get list product storage GSNPP
	 * 
	 * @author: HieuNH
	 * @param data
	 * @return
	 * @return: ListProductDTO
	 * @throws:
	 */
	public ListProductDTO getSupervisorProductList(Bundle data, ArrayList<String> shopIdArray) {
		ListProductDTO dto = null;
		String page = data.getString(IntentConstants.INTENT_PAGE);
		String productCode = data.getString(IntentConstants.INTENT_PRODUCT_CODE);
		String productName = data.getString(IntentConstants.INTENT_PRODUCT_NAME);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		String idListString = TextUtils.join(",", shopIdArray);
		int isGetTotalPage = data.getInt(IntentConstants.INTENT_GET_TOTAL_PAGE);

		StringBuffer totalPageSql = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();

		StringBuffer var1 = new StringBuffer();
		var1.append("SELECT PD.PRODUCT_ID                  AS PRODUCT_ID, ");
		var1.append("       PD.PRODUCT_CODE                AS PRODUCT_CODE, ");
		var1.append("       PD.CONVFACT                    AS CONVFACT, ");
		var1.append("       PD.PRODUCT_NAME                AS PRODUCT_NAME, ");
		var1.append("       ( CASE ");
		var1.append("          WHEN PD.SORT_ORDER IS NULL THEN 'z' ");
		var1.append("          ELSE SORT_ORDER ");
		var1.append("          END )        AS SORT_ORDER, ");
		var1.append("                (SELECT ap.ap_param_name ");
		var1.append("                 FROM   ap_param ap ");
		var1.append("                 WHERE  PD.UOM2 = ap.ap_param_code ");
		var1.append("                        AND ap.status = 1 ");
		var1.append("                        AND ap.type = 'UOM2') UOM2, ");
		var1.append("       PD.NAME_TEXT           		   AS NAME_TEXT, ");
		var1.append("       PD.PRODUCT_INFO_CODE           AS CATEGORY_CODE, ");
		var1.append("       PD.CREATE_DATE                 AS CREATE_DATE, ");
		var1.append("       ST.AVAILABLE_QUANTITY AS AVAILABLE_QUANTITY, ");
		var1.append("                ( CASE ");
		var1.append("                    WHEN pro.product_id IS NULL THEN 0 ");
		var1.append("                    ELSE 1 ");
		var1.append("                  END )        AS HAS_PROMOTION ");
		var1.append("FROM   (SELECT *  FROM PRODUCT, PRODUCT_INFO WHERE PRODUCT.CAT_ID = PRODUCT_INFO.PRODUCT_INFO_ID AND PRODUCT_INFO.OBJECT_TYPE = 0 AND PRODUCT_INFO.type = 1 AND PRODUCT.STATUS = 1 AND PRODUCT_INFO.STATUS = 1) PD ");
		var1.append("      LEFT JOIN (SELECT * ");
		var1.append("             FROM   STOCK_TOTAL ");
		// npp khi moi login vao chon
		var1.append("             WHERE  OBJECT_ID = ? AND OBJECT_TYPE = 1) ST ");
		var1.append("         ON PD.PRODUCT_ID = ST.PRODUCT_ID ");
		param.add(shopId);


		// Chuong trinh khuyen mai
		var1.append("      LEFT JOIN ( SELECT distinct  ");
		var1.append("       ppd.product_id            product_id ");
		var1.append("FROM   promotion_program pp, ");
		var1.append("       promotion_program_detail ppd, ");
		var1.append("       promotion_shop_map psm ");
		var1.append("WHERE  pp.promotion_program_id = ppd.promotion_program_id ");
		var1.append("       AND pp.promotion_program_id = psm.promotion_program_id ");
		var1.append("       AND Date(pp.from_date) <= Date('now', 'localtime') ");
		var1.append("       AND Ifnull(Date(pp.to_date) >= Date('now', 'localtime'), 1) ");
		var1.append("       AND pp.status = 1 ");
		var1.append("       AND psm.status = 1 ");
		var1.append("       AND psm.shop_id in (");
		var1.append(idListString);
		var1.append(") ");
		var1.append("       ) as pro");
		var1.append("       ON pd.product_id = pro.product_id ");

		var1.append("WHERE  1 = 1 ");
		var1.append("       AND UOM2 is not null ");
		var1.append("       AND CONVFACT > 1 ");
		var1.append("       AND DATE(PD.CREATE_DATE) <= DATE('now', 'localtime') ");
		var1.append("       AND IFNULL(DATE(PD.UPDATE_DATE) <= DATE('now', 'localtime'), 1) ");

		if (!StringUtil.isNullOrEmpty(productCode)) {
			productCode = StringUtil.escapeSqlString(productCode);
			productCode = DatabaseUtils.sqlEscapeString("%" + productCode + "%");
			productCode = productCode.substring(1, productCode.length()-1);
			var1.append("	and upper(PRODUCT_CODE) like upper(?) escape '^' ");
			param.add(productCode);
		}
		if (!StringUtil.isNullOrEmpty(productName)) {
			productName = StringUtil.getEngStringFromUnicodeString(productName);
			productName = StringUtil.escapeSqlString(productName);
			productName = DatabaseUtils.sqlEscapeString("%" + productName + "%");
			productName = productName.substring(1, productName.length()-1);
			var1.append("	and upper(NAME_TEXT) like upper(?) escape '^' ");
			param.add(productName);
		}

		var1.append("ORDER BY SORT_ORDER, has_promotion DESC, product_code ASC, product_name ASC");

		totalPageSql.append("select COUNT(*) as TOTAL_ROW from (" + var1 + ")");

		var1.append(page);

		Cursor c = null;
		try {
			c = rawQueries(var1.toString(), param);
			if (c != null) {
				if (c.moveToFirst()) {
					dto = new ListProductDTO();
					do {
						ProductDTO productInfo = new ProductDTO();
						productInfo.initDataFromCursor(c);
						dto.producList.add(productInfo);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
		} finally {
			if (c != null) {
				c.close();
			}
		}
		Cursor cTotal = null;
		try {
			if (isGetTotalPage == 1) {
				cTotal = rawQueries(totalPageSql.toString(), param);
				if (cTotal != null) {
					cTotal.moveToFirst();
					dto.total = cTotal.getInt(0);
				}

			}
		} catch (Exception e) {
		} finally {
			if (cTotal != null) {
				cTotal.close();
			}
		}

		return dto;
	}

	/**
	 * 
	 * Lay ds sp cua TBHV
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param data
	 * @return
	 * @return: ListProductDTO
	 * @throws:
	 */
	public ListProductDTO getTBHVProductList(Bundle data) {
		ListProductDTO dto = null;
		String page = data.getString(IntentConstants.INTENT_PAGE);
		String productCode = data.getString(IntentConstants.INTENT_PRODUCT_CODE);
		String productName = data.getString(IntentConstants.INTENT_PRODUCT_NAME);
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);
		boolean isGetTotalPage = data.getBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE);

		StringBuffer totalPageSql = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();
		StringBuffer sqlRequest = new StringBuffer();
		sqlRequest.append("SELECT PD.product_id        AS PRODUCT_ID, ");
		sqlRequest.append("       PD.product_code      AS PRODUCT_CODE, ");
		sqlRequest.append("       PD.convfact          AS CONVFACT, ");
		sqlRequest.append("       PD.UOM1              AS UOM1, ");
		sqlRequest.append("       PD.UOM2              AS UOM2, ");
		sqlRequest.append("       PD.product_name      AS PRODUCT_NAME, ");
		sqlRequest.append("       ( CASE ");
		sqlRequest.append("         WHEN PD.SORT_ORDER IS NULL THEN 'z' ");
		sqlRequest.append("         ELSE SORT_ORDER ");
		sqlRequest.append("         END )        AS SORT_ORDER, ");
		sqlRequest.append("       AP_PARAM_NAME AS CATEGORY_CODE, ");
		sqlRequest.append("       PD.create_date       AS CREATE_DATE, ");
		sqlRequest.append("       PR.price_id          AS PRICE_ID, ");
		sqlRequest.append("       PR.price             AS PRICE, ");
		sqlRequest.append("       PR.status            AS PRICE_STATUS ");
		sqlRequest.append("FROM   product PD ");
		sqlRequest.append("       LEFT JOIN (SELECT * FROM PRICE PRR WHERE PRR.STATUS = 1 AND DATE(PRR.FROM_DATE) <= DATE(?) ");
		sqlRequest.append("AND IFNULL(DATE(PRR.TO_DATE) >= DATE(?), 1) and type=2 GROUP BY PRODUCT_ID ) PR ON PD.PRODUCT_ID = PR.PRODUCT_ID ");
		
		sqlRequest.append(" left join	");
		sqlRequest.append(" (select AP_PARAM_CODE, AP_PARAM_NAME from AP_PARAM where type = 'UOM2'  AND STATUS =1 )	");
		sqlRequest.append(" on UOM2= AP_PARAM_CODE ");
		
		param.add(date_now);
		param.add(date_now);
		sqlRequest.append("       JOIN (select PI.product_info_id as PID from product_info PI where PI.status = 1 AND PI.type = 1 AND pi.object_type = 0 ) ");
		sqlRequest.append("         ON PID = PD.cat_id "); 
		sqlRequest.append("WHERE  PD.status = 1 ");
		sqlRequest.append("       AND PD.Convfact>1 ");
		
		if (!StringUtil.isNullOrEmpty(productCode)) {
			productCode = StringUtil.escapeSqlString(productCode);
			productCode = DatabaseUtils.sqlEscapeString("%" + productCode + "%");
			productCode = productCode.substring(1, productCode.length()-1);
			sqlRequest.append("	and upper(PD.product_code) like upper(?) escape '^' ");
			param.add(productCode);
		}
		
		if (!StringUtil.isNullOrEmpty(productName)) {
			productName = StringUtil.getEngStringFromUnicodeString(productName);
			productName = StringUtil.escapeSqlString(productName);
			productName = DatabaseUtils.sqlEscapeString("%" + productName + "%");
			productName = productName.substring(1, productName.length()-1);
			sqlRequest.append("	and upper(PD.name_text) like upper(?) escape '^' ");
			param.add(productName);
		}

		sqlRequest.append("ORDER  BY SORT_ORDER, product_code ASC ");
		//sqlRequest.append("GROUP BY PRODUCT_ID ");

		totalPageSql.append("select COUNT(*) as TOTAL_ROW from (" + sqlRequest + ")");

		sqlRequest.append(page);

		String[] paramStr = param.toArray(new String[param.size()]);
		Cursor c = null;
		try {
			c = rawQuery(sqlRequest.toString(), paramStr);
			if (c != null) {
				if (c.moveToFirst()) {
					dto = new ListProductDTO();
					do {
						ProductDTO productInfo = new ProductDTO();
						productInfo.initDataFromCursor(c);
						dto.producList.add(productInfo);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
		Cursor cTotal = null;
		try {
			if (isGetTotalPage) {
				cTotal = rawQuery(totalPageSql.toString(), paramStr);
				if (cTotal != null) {
					cTotal.moveToFirst();
					dto.total = cTotal.getInt(0);
				}

			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (cTotal != null) {
				try {
					cTotal.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}

		return dto;
	}

	/**
	 * 
	 * get list industry product and list product for the first industry
	 * 
	 * @param ext
	 * @return
	 * @return: SaleStatisticsProductInDayInfoViewDTO
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 20, 2012
	 */
	public SaleStatisticsProductInDayInfoViewDTO getListIndustryProductAndListProduct(Bundle ext) {
		SaleStatisticsProductInDayInfoViewDTO result = new SaleStatisticsProductInDayInfoViewDTO();
		result.listIndustry = this.getListIndustryProduct();
		int staffType = UserDTO.TYPE_PRESALES; // default
		if (!StringUtil.isNullOrEmpty(ext.getString(IntentConstants.INTENT_STAFF_TYPE))) {
			staffType = Integer.valueOf(ext.getString(IntentConstants.INTENT_STAFF_TYPE));
		}
		if (result.listIndustry != null && result.listIndustry.size() > 0) {
			// ext.putString(IntentConstants.INTENT_INDUSTRY,
			// result.listIndustry.get(0));
			if (staffType == UserDTO.TYPE_PRESALES) {
				result.listProduct = this.getListProductPreSaleSold(ext);
			} else if (staffType == UserDTO.TYPE_VALSALES) {
				result.listProduct = this.getListProductVanSaleSold(ext);
			}
		}
		return result;
	}

	/**
	 * 
	 * get list product pre sale sold
	 * 
	 * @param ext
	 * @return
	 * @return: ArrayList<SaleProductInfoDTO>
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 20, 2012
	 */
	public ArrayList<SaleProductInfoDTO> getListProductPreSaleSold(Bundle ext) {
		ArrayList<SaleProductInfoDTO> result = new ArrayList<SaleProductInfoDTO>();
		String staffId = ext.getString(IntentConstants.INTENT_STAFF_ID);
		String shopId = ext.getString(IntentConstants.INTENT_SHOP_ID);
		String dateNow= DateUtils.now();
		boolean isHasProductCode = false;
		boolean isHasProductName = false;
		boolean isHasIndustryProduct = false;
		String productCode = ext.getString(IntentConstants.INTENT_PRODUCT_CODE);
		isHasProductCode = !StringUtil.isNullOrEmpty(productCode);
		String productName = ext.getString(IntentConstants.INTENT_PRODUCT_NAME);
		isHasProductName = !StringUtil.isNullOrEmpty(productName);
		String industryProduct = ext.getString(IntentConstants.INTENT_INDUSTRY);
		isHasIndustryProduct = !StringUtil.isNullOrEmpty(industryProduct);
		if (!StringUtil.isNullOrEmpty(productCode)) {
			productCode = StringUtil.escapeSqlString(productCode);
			productCode = DatabaseUtils.sqlEscapeString("%" + productCode + "%");
			productCode = productCode.substring(1, productCode.length() - 1);
		}
		if (!StringUtil.isNullOrEmpty(productName)) {
			productName = StringUtil.getEngStringFromUnicodeString(productName);
			productName = StringUtil.escapeSqlString(productName);
			productName = DatabaseUtils.sqlEscapeString("%" + productName + "%");
			productName = productName.substring(1, productName.length() - 1);
		}

		ArrayList<String> totalParams = new ArrayList<String>();

		StringBuffer sqlRequest = new StringBuffer();
		sqlRequest.append("SELECT sod.product_id PRODUCT_ID, ");
		sqlRequest.append("       sod.price PRICE, ");
		sqlRequest.append("       Sum(sod.quantity) PRODUCT_NUMBER, ");
		sqlRequest.append("                         Sum(SOD.AMOUNT) AMOUNT_TOTAL, ");
//		sqlRequest.append("                         Sum(CASE SO.ORDER_TYPE ");
//		sqlRequest.append("                               WHEN 'CM' THEN -SO.AMOUNT ");
//		sqlRequest.append("                               WHEN 'CO' THEN -SO.AMOUNT ");
//		sqlRequest.append("                               ELSE SOD.AMOUNT ");
//		sqlRequest.append("                             end)       AMOUNT_TOTAL, ");
		sqlRequest.append("       p.product_code PRODUCT_CODE, ");
		sqlRequest.append("       p.product_name PRODUCT_NAME, ");
		sqlRequest.append("       pi.product_info_code CATEGORY_CODE, ");
		sqlRequest.append("       p.convfact CONVFACT ");
		sqlRequest.append("FROM   sale_order_detail sod, product_info pi, sale_order so, ");
		sqlRequest.append("       product p ");
		sqlRequest.append("WHERE  date(sod.order_date) = date(?) ");
		totalParams.add(dateNow);
		sqlRequest.append("       AND pi.status = 1 ");
		sqlRequest.append("       AND pi.type = 1 ");
		sqlRequest.append("       AND pi.product_info_id = p.cat_id ");
		sqlRequest.append("       AND sod.staff_id = ? ");
		totalParams.add(staffId);
		sqlRequest.append("       AND is_free_item = 0 ");
		sqlRequest.append("       AND so.approved in (0,1) ");
		sqlRequest.append("       AND so.type = 1 ");
		sqlRequest
				.append("       AND (CASE WHEN SO.APPROVED == 0 THEN DATE(SO.ORDER_DATE) = DATE(?) WHEN SO.APPROVED == 1 THEN DATE(SO.ORDER_DATE) <= DATE(?) END) ");
		totalParams.add(dateNow);
		totalParams.add(dateNow);
		sqlRequest.append("       AND sod.product_id = p.product_id ");
		sqlRequest.append("       AND p.status = 1 ");
		sqlRequest.append("       AND sod.sale_order_id = so.sale_order_id ");
		sqlRequest.append("       AND date(so.order_date) = date(?) ");
		totalParams.add(dateNow);
		sqlRequest.append("       AND so.staff_id = ? ");
		sqlRequest.append("       AND so.shop_id = ? ");
		
		totalParams.add(staffId);
		totalParams.add(shopId);
		if (isHasIndustryProduct) {
			sqlRequest.append("       AND upper(pi.product_info_code) = upper(?) ");
			totalParams.add(industryProduct);
		} else {
			sqlRequest.append("       AND upper(pi.product_info_code) != 'Z' ");
		}

		if (isHasProductCode) {
			sqlRequest.append("       AND upper(p.product_code) LIKE upper(?) escape '^' ");
			totalParams.add(productCode);
		}
		if (isHasProductName) {
			sqlRequest.append("       AND upper(p.name_text) LIKE upper(?) escape '^' ");
			totalParams.add(productName);
		}
		sqlRequest.append("GROUP  BY sod.product_id, ");
		sqlRequest.append("          sod.price, ");
		sqlRequest.append("          p.product_code, ");
		sqlRequest.append("          p.product_name, ");
		sqlRequest.append("          pi.product_info_name, ");
		sqlRequest.append("          p.convfact ");
		sqlRequest.append(" order by p.product_code, p.product_name ");

		String[] params = new String[] {};
		params = totalParams.toArray(new String[totalParams.size()]);

		Cursor c = null;
		try {
			c = rawQuery(sqlRequest.toString(), params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						SaleProductInfoDTO item = new SaleProductInfoDTO();
						item.initDataPreSaleFromCursor(c);
						result.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}

		return result;
	}

	/**
	 * 
	 * get list product van sale sold
	 * 
	 * @param ext
	 * @return
	 * @return: ArrayList<SaleProductInfoDTO>
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 17, 2013
	 */
	public ArrayList<SaleProductInfoDTO> getListProductVanSaleSold(Bundle ext) {
		ArrayList<SaleProductInfoDTO> result = new ArrayList<SaleProductInfoDTO>();
		String staffId = ext.getString(IntentConstants.INTENT_STAFF_ID);
		String shopId = ext.getString(IntentConstants.INTENT_SHOP_ID);
		boolean isHasProductCode = false;
		boolean isHasProductName = false;
		boolean isHasIndustryProduct = false;
		String productCode = ext.getString(IntentConstants.INTENT_PRODUCT_CODE);
		isHasProductCode = !StringUtil.isNullOrEmpty(productCode);
		String productName = ext.getString(IntentConstants.INTENT_PRODUCT_NAME);
		isHasProductName = !StringUtil.isNullOrEmpty(productName);
		String industryProduct = ext.getString(IntentConstants.INTENT_INDUSTRY);
		isHasIndustryProduct = !StringUtil.isNullOrEmpty(industryProduct);
		String dateNow= DateUtils.now();

		if (!StringUtil.isNullOrEmpty(productCode)) {
			productCode = StringUtil.escapeSqlString(productCode);
			productCode = DatabaseUtils.sqlEscapeString("%" + productCode + "%");
			productCode = productCode.substring(1, productCode.length() - 1);
		}
		if (!StringUtil.isNullOrEmpty(productName)) {
			productName = StringUtil.getEngStringFromUnicodeString(productName);
			productName = StringUtil.escapeSqlString(productName);
			productName = DatabaseUtils.sqlEscapeString("%" + productName + "%");
			productName = productName.substring(1, productName.length() - 1);
		}

		ArrayList<String> totalParams = new ArrayList<String>();

		StringBuffer sqlRequest = new StringBuffer();
		sqlRequest.append("SELECT TT.product_id                   PRODUCT_ID, ");
		sqlRequest.append("       TT.price                        PRICE, ");
		sqlRequest.append("       TT.product_code                 PRODUCT_CODE, ");
		sqlRequest.append("       TT.product_name                 PRODUCT_NAME, ");
		sqlRequest.append("       TT.product_name_text            PRODUCT_NAME_TEXT, ");
		sqlRequest.append("       TT.category_code                CATEGORY_CODE, ");
		sqlRequest.append("       TT.convfact                     CONVFACT, ");
		sqlRequest.append("       TT.total_product_remain         TOTAL_PRODUCT_REMAIN, ");
		sqlRequest.append("       Sum(TT.total_product)           TOTAL_PRODUCT, ");
		sqlRequest.append("       Sum(TT.total_amount)            TOTAL_AMOUNT, ");
		sqlRequest.append("       Sum(TT.total_product_add_order) TOTAL_PRODUCT_ADD_ORDER, ");
		sqlRequest.append("       Sum(TT.total_amount_add_order)  TOTAL_TOTAL_AMOUNT_ADD_ORDER, ");
		sqlRequest.append("       Sum(TT.total_product_sold)      TOTAL_PRODUCT_SOLD, ");
		sqlRequest.append("       Sum(TT.total_amount_sold)       TOTAL_TOTAL_AMOUNT_SOLD ");
		sqlRequest.append("FROM   (SELECT sod.product_id         PRODUCT_ID, ");
		sqlRequest.append("               sod.price              PRICE, ");
		sqlRequest.append("               p.product_code         PRODUCT_CODE, ");
		sqlRequest.append("               p.product_name         PRODUCT_NAME, ");
		sqlRequest.append("               p.name_text    PRODUCT_NAME_TEXT, ");
		sqlRequest.append("               pi.product_info_code   CATEGORY_CODE, ");
		sqlRequest.append("               p.convfact             CONVFACT, ");
		sqlRequest.append("               SO.order_type          ORDER_TYPE, ");
		sqlRequest.append("               Sum(sod.quantity)      TOTAL_PRODUCT, ");
		sqlRequest.append("               Sum(sod.amount)        TOTAL_AMOUNT, ");
		sqlRequest.append("               STT.available_quantity TOTAL_PRODUCT_REMAIN, ");
		sqlRequest.append("               CASE ");
		sqlRequest.append("                 WHEN SO.order_type = 'IN' THEN Sum(SOD.quantity) ");
		sqlRequest.append("                 ELSE 0 ");
		sqlRequest.append("               END                    TOTAL_PRODUCT_ADD_ORDER, ");
		sqlRequest.append("               CASE ");
		sqlRequest.append("                 WHEN SO.order_type = 'IN' THEN Sum(SOD.amount) ");
		sqlRequest.append("                 ELSE 0 ");
		sqlRequest.append("               END                    TOTAL_AMOUNT_ADD_ORDER, ");
		sqlRequest.append("               CASE ");
		sqlRequest.append("                 WHEN SO.order_type = 'SO' THEN Sum(SOD.quantity) ");
		sqlRequest.append("                 ELSE 0 ");
		sqlRequest.append("               END                    TOTAL_PRODUCT_SOLD, ");
		sqlRequest.append("               CASE ");
		sqlRequest.append("                 WHEN SO.order_type = 'SO' THEN Sum(SOD.amount) ");
		sqlRequest.append("                 ELSE 0 ");
		sqlRequest.append("               END                    TOTAL_AMOUNT_SOLD ");
		sqlRequest.append("        FROM   (SELECT * ");
		sqlRequest.append("                FROM   sale_order_detail SOD ");
		sqlRequest.append("                WHERE  SOD.shop_id = ? ");
		totalParams.add(shopId);
		sqlRequest.append("                       AND SOD.staff_id = ? ");
		totalParams.add(staffId);
		sqlRequest.append("                       AND SOD.IS_FREE_ITEM = 0 ");
		sqlRequest.append("                       AND Date(order_date) = Date(?) ");
		totalParams.add(dateNow);
		sqlRequest.append("                       AND SOD.price > 0) SOD ");
		sqlRequest.append("               INNER JOIN (SELECT SO.sale_order_id, ");
		sqlRequest.append("                                  SO.order_type ");
		sqlRequest.append("                           FROM   sale_order SO ");
		sqlRequest.append("                           WHERE  SO.shop_id = ? ");
		totalParams.add(shopId);
		sqlRequest.append("                                  AND SO.staff_id = ? ");
		totalParams.add(staffId);
		sqlRequest.append("                                  AND Date(SO.order_date) = ");
		sqlRequest.append("                                      Date(?) ");
		totalParams.add(dateNow);
		sqlRequest.append("                                  AND ( SO.order_type = 'IN' ");
		sqlRequest.append("                                         OR SO.order_type = 'SO' ) ");
		sqlRequest.append("       AND so.approved in (0,1) ");
		sqlRequest.append("       AND so.syn_state in (0,2) ");
		sqlRequest
				.append("       AND (CASE WHEN SO.APPROVED == 0 THEN DATE(SO.ORDER_DATE) = DATE(?) WHEN SO.APPROVED == 1 THEN DATE(SO.ORDER_DATE) <= DATE(?) END) ) SO ");
		totalParams.add(dateNow);
		totalParams.add(dateNow);
		sqlRequest.append("                       ON SOD.sale_order_id = SO.sale_order_id ");
		sqlRequest.append("               LEFT JOIN (SELECT STT.product_id, ");
		sqlRequest.append("                                  STT.available_quantity ");
		sqlRequest.append("                           FROM   stock_total STT ");
		sqlRequest.append("                           WHERE  STT.object_type = 2 ");
		sqlRequest.append("                                  AND STT.object_id = ?) STT ");
		totalParams.add(staffId);
		sqlRequest.append("                       ON STT.product_id = SOD.product_id, ");
		sqlRequest.append("               product_info PI, ");
		sqlRequest.append("               product P ");
		sqlRequest.append("        WHERE  1 = 1 ");
		sqlRequest.append("               AND PI.status = 1 ");
		sqlRequest.append("               AND PI.type = 1 ");
		sqlRequest.append("               AND PI.product_info_id = P.cat_id ");
		sqlRequest.append("               AND P.product_id = SOD.product_id ");
		sqlRequest.append("        GROUP  BY sod.product_id, ");
		sqlRequest.append("                  sod.price, ");
		sqlRequest.append("                  p.product_code, ");
		sqlRequest.append("                  p.product_name, ");
		sqlRequest.append("                  p.name_text, ");
		sqlRequest.append("                  pi.product_info_code, ");
		sqlRequest.append("                  p.convfact, ");
		sqlRequest.append("                  SO.order_type, ");
		sqlRequest.append("                  STT.available_quantity) TT where ");
		if (isHasIndustryProduct) {
			sqlRequest.append("       upper(TT.category_code) = upper(?) ");
			totalParams.add(industryProduct);
		} else {
			sqlRequest.append("       upper(TT.category_code) != 'Z' ");
		}

		if (isHasProductCode) {
			sqlRequest.append("       AND upper(TT.product_code) LIKE upper(?) escape '^' ");
			totalParams.add(productCode);
		}
		if (isHasProductName) {
			sqlRequest.append("       AND upper(TT.product_name_text) LIKE upper(?) escape '^' ");
			totalParams.add(productName);
		}
		sqlRequest.append("GROUP  BY TT.product_id, ");
		sqlRequest.append("          TT.price, ");
		sqlRequest.append("          TT.product_code, ");
		sqlRequest.append("          TT.product_name, ");
		sqlRequest.append("          TT.product_name_text, ");
		sqlRequest.append("          TT.category_code, ");
		sqlRequest.append("          TT.convfact, ");
		sqlRequest.append("          TT.total_product_remain ");
		sqlRequest.append(" order by TT.product_code, TT.product_name ");

		String[] params = new String[] {};
		params = totalParams.toArray(new String[totalParams.size()]);

		Cursor c = null;
		try {
			c = rawQuery(sqlRequest.toString(), params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						SaleProductInfoDTO item = new SaleProductInfoDTO();
						item.initDataVanSaleFromCursor(c);
						result.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}

		return result;
	}

	/**
	 * 
	 * get list industry for product
	 * 
	 * @param staffIdGS
	 * @return
	 * @return: ArrayList<String>
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 20, 2012
	 */
	public ArrayList<String> getListIndustryProduct() {
		ArrayList<String> listIndustry = new ArrayList<String>();
		StringBuffer sqlRequest = new StringBuffer();
		sqlRequest.append("SELECT DISTINCT PRODUCT_INFO_CODE ");
		sqlRequest.append("FROM   PRODUCT_INFO ");
		sqlRequest.append("WHERE  PRODUCT_INFO_CODE != 'Z' ");
		sqlRequest.append("       AND STATUS = 1 ");
		sqlRequest.append("       AND TYPE = 1 ");
		sqlRequest.append("GROUP  BY PRODUCT_INFO_CODE ");
		sqlRequest.append("ORDER  BY PRODUCT_INFO_CODE ");

		String[] params = new String[] {};
		Cursor c = null;
		try {
			c = rawQuery(sqlRequest.toString(), params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						if (c.getColumnIndex(PRODUCT_INFO_TABLE.PRODUCT_INFO_CODE) >= 0) {
							listIndustry.add(c.getString(c.getColumnIndex(PRODUCT_INFO_TABLE.PRODUCT_INFO_CODE)));
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			// TODO: handle exception
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}

		return listIndustry;
	}

	/**
	 * 
	 * Mo ta ham
	 * 
	 * @author: HieuNH6
	 * @return
	 * @return: SaleStatisticsAccumulateDayDTO
	 * @throws:
	 */
	public SaleStatisticsAccumulateDayDTO getSaleStatisticsAccumulateDayListProduct(String shopId, String staffId,
			String productCode, String productName, String industry, String page) {
		SaleStatisticsAccumulateDayDTO listIndustry = new SaleStatisticsAccumulateDayDTO();
		if (!StringUtil.isNullOrEmpty(productName)) {
			productName = "%" + productName + "%";
		}
		if (!StringUtil.isNullOrEmpty(productCode)) {
			productCode = "%" + productCode + "%";
		}
		ArrayList<String> totalParams = new ArrayList<String>();
		totalParams.add(staffId);
		// totalParams.add(shopId);
		totalParams.add(shopId);
		totalParams.add(staffId);

		StringBuffer sqlRequest = new StringBuffer();
		sqlRequest.append("SELECT DISTINCT sp.product_id                               product_id, ");
		sqlRequest.append("                p.product_code                              maHang, ");
		sqlRequest.append("                p.product_name                              tenMatHang, ");
		sqlRequest.append("                pi.product_info_code                             nh, ");
		sqlRequest.append("                Sum(Ifnull (sp.amount, 0)) keHoach, ");
		sqlRequest.append("                Sum(Ifnull (TTT.amount, 0))                 thucHien ");
		sqlRequest.append("FROM   sale_plan sp, ");
		// sqlRequest.append("       price pr, ");
		sqlRequest.append("       product p JOIN product_info pi ON p.cat_id = pi.product_info_id");
		sqlRequest.append("       LEFT OUTER JOIN (SELECT DISTINCT product_id, ");
		sqlRequest.append("                                        Sum(amount) amount ");
		sqlRequest.append("                        FROM   rpt_sale_in_month ");
		sqlRequest.append("                        WHERE  1 = 1 ");
		sqlRequest.append("                               AND staff_id = ? ");
		sqlRequest.append("                               AND status = 1 ");
		sqlRequest.append("                               AND shop_id = ? ");
		sqlRequest.append("                               AND Strftime('%Y/%m', month) = ");
		sqlRequest.append("                                   Strftime('%Y/%m', 'now', 'localtime') ");
		sqlRequest.append("                        GROUP  BY product_id) TTT ");
		sqlRequest.append("                    ON TTT.product_id = sp.product_id ");
		sqlRequest.append("WHERE  1 = 1 ");
		// sqlRequest.append("       AND sp.shop_id = ? ");
		sqlRequest.append("       AND sp.object_id = ? AND sp.object_type = 2 and sp.type = 2");
		sqlRequest.append("       AND sp.month = ");
		sqlRequest.append("           Strftime('%m', 'now', 'localtime') ");
		sqlRequest.append("       AND sp.year = ");
		sqlRequest.append("           Strftime('%Y', 'now', 'localtime') ");
		sqlRequest.append("       AND p.status = 1 ");

		if (!StringUtil.isNullOrEmpty(industry)) {
			sqlRequest.append("       AND pi.product_info_code = ? ");
			totalParams.add(industry);
		} else {
			sqlRequest.append("       AND upper(pi.product_info_code) != 'Z' ");
		}
		if (!StringUtil.isNullOrEmpty(productName)) {
			sqlRequest.append("       AND upper(p.name_text) LIKE upper(?) ");
			totalParams.add(productName);
		}
		if (!StringUtil.isNullOrEmpty(productCode)) {
			sqlRequest.append("       AND upper(p.product_code) LIKE upper(?) ");
			totalParams.add(productCode);
		}

		sqlRequest.append("       AND p.product_id = sp.product_id ");
		// sqlRequest.append("       AND pr.product_id = sp.product_id ");
		//
		// sqlRequest.append("       AND pr.status = 1 ");
		// sqlRequest
		// .append("       AND Date(pr.from_date) <= (SELECT Date('now', 'localtime')) ");
		// sqlRequest
		// .append("       AND ( Date(pr.to_date) >= (SELECT Date('now', 'localtime')) ");
		// sqlRequest.append("              OR Date(pr.to_date) IS NULL ) ");
		sqlRequest.append("GROUP  BY product_id, ");
		sqlRequest.append("          product_code, ");
		sqlRequest.append("          product_name ");
		sqlRequest.append("UNION ALL ");

		totalParams.add(staffId);
		totalParams.add(shopId);

		sqlRequest.append("SELECT DISTINCT sim.product_id  product_id, ");
		sqlRequest.append("                p.product_code  maHang, ");
		sqlRequest.append("                p.product_name  tenMatHang, ");
		sqlRequest.append("                pi.product_info_code         nh, ");
		sqlRequest.append("                0               keHoach, ");
		sqlRequest.append("                Sum(sim.amount) thucHien ");
		sqlRequest.append("FROM   rpt_sale_in_month sim, ");
		sqlRequest.append("       product p JOIN product_info pi ON p.cat_id = pi.product_info_id");
		sqlRequest.append(" WHERE  1 = 1 ");
		sqlRequest.append("       AND sim.staff_id = ? ");
		sqlRequest.append("       AND sim.status = 1 ");
		sqlRequest.append("       AND sim.shop_id = ? ");
		sqlRequest.append("       AND Strftime('%Y/%m', sim.month) = Strftime('%Y/%m', 'now', 'localtime') ");
		sqlRequest.append("       AND p.status = 1 ");
		sqlRequest.append("       AND p.product_id = sim.product_id ");
		if (!StringUtil.isNullOrEmpty(industry)) {
			sqlRequest.append("       AND pi.product_info_code = ? ");
			totalParams.add(industry);
		} else {
			sqlRequest.append("       AND upper(pi.product_info_code) != 'Z' ");
		}

		totalParams.add(shopId);
		totalParams.add(staffId);
		sqlRequest.append("       AND sim.product_id NOT IN (SELECT DISTINCT product_id ");
		sqlRequest.append("                                  FROM   sale_plan ");
		sqlRequest.append("                                  WHERE  1 = 1 ");
		sqlRequest.append("                                         AND shop_id = ? ");
		sqlRequest.append("                                         AND staff_id = ? ");
		sqlRequest.append("       AND p.product_id = sim.product_id ");
		sqlRequest.append("                                         AND month = ");
		sqlRequest.append("                                             Strftime( ");
		sqlRequest.append("                                             '%m', 'now', 'localtime') ");
		sqlRequest.append("                                         AND year = ");
		sqlRequest.append("                                             Strftime( ");
		sqlRequest.append("                                             '%Y', 'now', 'localtime') ");
		sqlRequest.append("                                 ) ");
		if (!StringUtil.isNullOrEmpty(productName)) {
			sqlRequest.append("       AND upper(p.name_text) LIKE upper(?) ");
			totalParams.add(productName);
		}
		if (!StringUtil.isNullOrEmpty(productCode)) {
			sqlRequest.append("       AND upper(p.product_code) LIKE upper(?) ");
			totalParams.add(productCode);
		}
		sqlRequest.append("GROUP  BY product_id, ");
		sqlRequest.append("          product_code, ");
		sqlRequest.append("          product_name ");
		sqlRequest.append("ORDER  BY ");
		sqlRequest.append("          product_code, ");
		sqlRequest.append("          product_name ");
		sqlRequest.append(page);

		Cursor c = null;
		try {
			c = rawQuery(sqlRequest.toString(), totalParams.toArray(new String[totalParams.size()]));
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						listIndustry.addItem(c);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			// TODO: handle exception
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}

		return listIndustry;
	}

	/**
	 * 
	 * Mo ta ham
	 * 
	 * @author: HieuNH6
	 * @return
	 * @return: SaleStatisticsAccumulateDayDTO
	 * @throws:
	 */
	public int getCountSaleStatisticsAccumulateDayListProduct(String shopId, String staffId, String productCode,
			String productName, String industry) {
		int countListIndustry = 0;
		if (!StringUtil.isNullOrEmpty(productName)) {
			productName = "%" + productName + "%";
		}
		if (!StringUtil.isNullOrEmpty(productCode)) {
			productCode = "%" + productCode + "%";
		}
		ArrayList<String> totalParams = new ArrayList<String>();
		totalParams.add(staffId);
		totalParams.add(shopId);
		totalParams.add(staffId);

		StringBuffer sqlRequest = new StringBuffer();
		sqlRequest.append("SELECT DISTINCT sp.product_id                               product_id, ");
		sqlRequest.append("                p.product_code                              maHang, ");
		sqlRequest.append("                p.product_name                              tenMatHang, ");
		sqlRequest.append("                pi.product_info_code                         nh, ");
		sqlRequest.append("                Sum(Ifnull (sp.amount, 0)) keHoach, ");
		sqlRequest.append("                Sum(Ifnull (TTT.amount, 0))                 thucHien ");
		sqlRequest.append("FROM   price pr, ");
		sqlRequest.append("       sale_plan sp, ");
		sqlRequest.append("       product p join product_info pi on p.cat_id = pi.product_info_id ");
		sqlRequest.append("       LEFT OUTER JOIN (SELECT DISTINCT product_id, ");
		sqlRequest.append("                                        Sum(amount) amount ");
		sqlRequest.append("                        FROM   rpt_sale_in_month ");
		sqlRequest.append("                        WHERE  1 = 1 ");
		sqlRequest.append("                               AND staff_id = ? ");
		sqlRequest.append("                               AND status = 1 ");
		sqlRequest.append("                               AND shop_id = ? ");
		sqlRequest.append("                               AND Strftime('%Y/%m', month) = ");
		sqlRequest.append("                                   Strftime('%Y/%m', 'now', 'localtime') ");
		sqlRequest.append("                        GROUP  BY product_id) TTT ");
		sqlRequest.append("                    ON TTT.product_id = sp.product_id ");
		sqlRequest.append("WHERE  1 = 1 ");
		// sqlRequest.append("       AND sp.shop_id = ? ");
		sqlRequest.append("       AND sp.object_id = ? ");
		sqlRequest.append("       AND sp.object_type = 2 ");
		sqlRequest.append("       AND sp.month = ");
		sqlRequest.append("           Strftime('%m', 'now', 'localtime') ");
		sqlRequest.append("       AND sp.year = ");
		sqlRequest.append("           Strftime('%Y', 'now', 'localtime') ");
		sqlRequest.append("       AND p.status = 1 ");

		if (!StringUtil.isNullOrEmpty(industry)) {
			sqlRequest.append("       AND pi.product_info_code = ? ");
			totalParams.add(industry);
		} else {
			sqlRequest.append("       AND upper(pi.product_info_code) != 'Z' ");
		}
		if (!StringUtil.isNullOrEmpty(productName)) {
			sqlRequest.append("       AND upper(p.name_text) LIKE upper(?) ");
			totalParams.add(productName);
		}
		if (!StringUtil.isNullOrEmpty(productCode)) {
			sqlRequest.append("       AND upper(p.product_code) LIKE upper(?) ");
			totalParams.add(productCode);
		}

		sqlRequest.append("       AND p.product_id = sp.product_id ");
		sqlRequest.append("       AND pr.product_id = sp.product_id ");

		sqlRequest.append("       AND pr.status = 1 ");
		sqlRequest.append("       AND Date(pr.from_date) <= (SELECT Date('now', 'localtime')) ");
		sqlRequest.append("       AND ( Date(pr.to_date) >= (SELECT Date('now', 'localtime')) ");
		sqlRequest.append("              OR Date(pr.to_date) IS NULL ) ");
		sqlRequest.append("GROUP  BY product_id, ");
		sqlRequest.append("          product_code, ");
		sqlRequest.append("          product_name ");
		sqlRequest.append("UNION ALL ");

		totalParams.add(staffId);
		totalParams.add(shopId);

		sqlRequest.append("SELECT DISTINCT sim.product_id  product_id, ");
		sqlRequest.append("                p.product_code  maHang, ");
		sqlRequest.append("                p.product_name  tenMatHang, ");
		sqlRequest.append("                pi.product_info_code         nh, ");
		sqlRequest.append("                0               keHoach, ");
		sqlRequest.append("                Sum(sim.amount) thucHien ");
		sqlRequest.append("FROM   rpt_sale_in_month sim, ");
		sqlRequest.append("       product p join product_info pi on p.cat_id = pi.product_info_id ");
		sqlRequest.append("WHERE  1 = 1 ");
		sqlRequest.append("       AND sim.staff_id = ? ");
		sqlRequest.append("       AND sim.status = 1 ");
		sqlRequest.append("       AND sim.shop_id = ? ");
		sqlRequest.append("       AND Strftime('%Y/%m', sim.month) = Strftime('%Y/%m', 'now', 'localtime') ");
		sqlRequest.append("       AND p.status = 1 ");
		sqlRequest.append("       AND p.product_id = sim.product_id ");
		if (!StringUtil.isNullOrEmpty(industry)) {
			sqlRequest.append("       AND pi.product_info_code = ? ");
			totalParams.add(industry);
		} else {
			sqlRequest.append("       AND upper(pi.product_info_code) != 'Z' ");
		}

		totalParams.add(shopId);
		totalParams.add(staffId);
		sqlRequest.append("       AND sim.product_id NOT IN (SELECT DISTINCT product_id ");
		sqlRequest.append("                                  FROM   sale_plan sp ");
		sqlRequest.append("                                  WHERE  1 = 1 ");
		sqlRequest.append("                                         AND shop_id = ? ");
		sqlRequest.append("                                         AND staff_id = ? ");
		sqlRequest.append("       AND p.product_id = sim.product_id ");
		sqlRequest.append("                                         AND sp.month = ");
		sqlRequest.append("                                             Strftime( ");
		sqlRequest.append("                                             '%m', 'now', 'localtime') ");
		sqlRequest.append("                                         AND sp.year = ");
		sqlRequest.append("                                             Strftime( ");
		sqlRequest.append("                                             '%Y', 'now', 'localtime') ");
		sqlRequest.append("                                 ) ");
		if (!StringUtil.isNullOrEmpty(productName)) {
			sqlRequest.append("       AND upper(p.name_text) LIKE upper(?) ");
			totalParams.add(productName);
		}
		if (!StringUtil.isNullOrEmpty(productCode)) {
			sqlRequest.append("       AND upper(p.product_code) LIKE upper(?) ");
			totalParams.add(productCode);
		}
		sqlRequest.append("GROUP  BY product_id, ");
		sqlRequest.append("          product_code, ");
		sqlRequest.append("          product_name ");

		Cursor c = null;
		try {
			c = rawQuery(sqlRequest.toString(), totalParams.toArray(new String[totalParams.size()]));
			if (c != null) {
				if (c.moveToFirst()) {
					countListIndustry = c.getCount();
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			// TODO: handle exception
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}

		return countListIndustry;
	}

	/**
	 * 
	 * Mo ta ham
	 * 
	 * @author: HieuNH6
	 * @return
	 * @return: SaleStatisticsAccumulateDayDTO
	 * @throws:
	 */
	public ArrayList<String> getSaleStatisticsAccumulateDay(String shopId, String staffId, String productCode,
			String productName, String industry, String page) {
		return getListIndustryProduct();
	}

	/**
	 * 
	 * get forcus product info view
	 * 
	 * @param data
	 * @return
	 * @return: NVBHReportForcusProductInfoViewDTO
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 2, 2013
	 */
	public NVBHReportForcusProductInfoViewDTO getForcusProductInfoView(
			Bundle data) {
		NVBHReportForcusProductInfoViewDTO result = new NVBHReportForcusProductInfoViewDTO();
		SHOP_TABLE shopTB = new SHOP_TABLE(this.mDB);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		ArrayList<String> listShopId = shopTB.getShopRecursive(shopId);
		String strListShop = TextUtils.join(",", listShopId);
		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID);
		String saleTypeCode = data.getString(IntentConstants.INTENT_SALE_TYPE_CODE);
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT DISTINCT FCMP.PRODUCT_ID PRODUCT_ID, ");
		sqlQuery.append("       P.PRODUCT_CODE PRODUCT_CODE, ");
		sqlQuery.append("       P.PRODUCT_NAME PRODUCT_NAME, ");
		sqlQuery.append("       PI.PRODUCT_INFO_NAME PRODUCT_INFO_NAME, ");
		sqlQuery.append("       SP.AMOUNT  PLAN_AMOUNT, ");
		sqlQuery.append("       SOD.AMOUNT AMOUNT, ");
		sqlQuery.append("       FCMP.TYPE TYPE_PRODUCT_FORCUS ");
		sqlQuery.append("FROM   FOCUS_CHANNEL_MAP FCM, ");
		sqlQuery.append("       FOCUS_SHOP_MAP FSM, ");
		sqlQuery.append("       FOCUS_PROGRAM FP, ");
		sqlQuery.append("       PRODUCT P, ");
		sqlQuery.append("       PRODUCT_INFO PI, ");
		sqlQuery.append("       FOCUS_CHANNEL_MAP_PRODUCT FCMP ");
		sqlQuery.append("       LEFT JOIN (SELECT PRODUCT_ID, ");
		sqlQuery.append("                         AMOUNT ");
		sqlQuery.append("                  FROM   SALE_PLAN ");
		sqlQuery.append("                  WHERE  OBJECT_ID = ? ");
		sqlQuery.append("                         AND OBJECT_TYPE = 2 ");
		sqlQuery.append("                         AND TYPE = 2 ");
		sqlQuery.append("                         AND MONTH = Strftime('%m', 'now', 'localtime') ");
		sqlQuery.append("                         AND YEAR = Strftime('%Y', 'now', 'localtime')) SP ");
		sqlQuery.append("              ON FCMP.PRODUCT_ID = SP.PRODUCT_ID ");
		sqlQuery.append("       LEFT JOIN (SELECT DISTINCT SOD.PRODUCT_ID PRODUCT_ID, ");
		sqlQuery.append("                         Sum(CASE SO.ORDER_TYPE ");
		sqlQuery.append("                               WHEN 'CM' THEN -SO.AMOUNT ");
		sqlQuery.append("                               WHEN 'CO' THEN -SO.AMOUNT ");
		sqlQuery.append("                               ELSE SOD.AMOUNT ");
		sqlQuery.append("                             end)       AMOUNT ");
		sqlQuery.append("                  FROM   SALE_ORDER SO, ");
		sqlQuery.append("                         SALE_ORDER_DETAIL SOD ");
		sqlQuery.append("                  WHERE  SO.SHOP_ID IN ( ");
		sqlQuery.append(strListShop + ") ");
		sqlQuery.append("                         AND SO.STAFF_ID = ? ");
		sqlQuery.append("                         AND Date(SO.ORDER_DATE) >= Date('NOW', 'localtime','START OF MONTH') ");
		sqlQuery.append("                         AND SOD.SALE_ORDER_ID = SO.SALE_ORDER_ID ");
		sqlQuery.append("                         AND SOD.SHOP_ID IN ( ");
		sqlQuery.append(strListShop + ") ");
		sqlQuery.append("                         AND ((date(so.order_date) > date('now', 'localtime', 'start of month') and date(so.order_date) < Date('now') and so.type = 1 and so.approved = 1) ");
		sqlQuery.append("  								or (date(so.order_date) = Date('now') and so.type = 1 and so.approved in (1,0))) ");
//		sqlQuery.append("                         AND SO.APPROVED != 2 ");
//		sqlQuery.append("                         AND (CASE WHEN SO.APPROVED == 0 THEN DATE(SO.ORDER_DATE) = DATE('NOW','LOCALTIME') WHEN SO.APPROVED == 1 THEN DATE(SO.ORDER_DATE) <= DATE('NOW','LOCALTIME') END) ");
//		sqlQuery.append("                         AND SOD.STAFF_ID = ? ");
//		sqlQuery.append("                         AND Date(SOD.ORDER_DATE) >= ");
//		sqlQuery.append("                             Date('NOW', 'localtime','START OF MONTH')  "); 
		sqlQuery.append("					group by     SOD.PRODUCT_ID  ) SOD ");
		sqlQuery.append("              ON SOD.PRODUCT_ID = FCMP.PRODUCT_ID ");
		sqlQuery.append("WHERE  FP.STATUS = 1 ");
		sqlQuery.append("       AND Date(FP.FROM_DATE) <= Date('NOW', 'LOCALTIME') ");
		sqlQuery.append("       AND Ifnull (Date(FP.TO_DATE) >= Date('NOW', 'LOCALTIME'), 1) ");
		sqlQuery.append("       AND FP.FOCUS_PROGRAM_ID = FCM.FOCUS_PROGRAM_ID ");
		sqlQuery.append("       AND FCM.SALE_TYPE_CODE = ? ");
		sqlQuery.append("       AND FCM.STATUS = 1 ");
		// sqlQuery.append("       AND Date(FCM.FROM_DATE) <= Date('NOW', 'LOCALTIME') ");
		// sqlQuery.append("       AND Ifnull (Date(FCM.TO_DATE) >= Date('NOW', 'LOCALTIME'), 1) ");
		sqlQuery.append("       AND FP.FOCUS_PROGRAM_ID = FSM.FOCUS_PROGRAM_ID ");
		sqlQuery.append("       AND FSM.SHOP_ID IN ( ");
		sqlQuery.append(strListShop + ") ");
		sqlQuery.append("       AND FSM.STATUS = 1 ");
		sqlQuery.append("       AND FCMP.FOCUS_CHANNEL_MAP_ID = FCM.FOCUS_CHANNEL_MAP_ID ");
		sqlQuery.append("       AND P.PRODUCT_ID = FCMP.PRODUCT_ID ");
		sqlQuery.append("       AND P.STATUS = 1 ");
		sqlQuery.append("       AND P.CAT_ID = PI.PRODUCT_INFO_ID ");
		sqlQuery.append("       AND PI.STATUS = 1 ");
		sqlQuery.append("       AND PI.TYPE = 1 ");
		sqlQuery.append("       ORDER BY  FCMP.TYPE DESC, P.PRODUCT_CODE, P.PRODUCT_NAME ");

		Cursor c = null;
		ArrayList<String> totalParams = new ArrayList<String>();
		totalParams.add(staffId);
		totalParams.add(staffId);
		totalParams.add(saleTypeCode);
		String[] params = new String[] {};
		params = totalParams.toArray(new String[totalParams.size()]);
		try {
			c = this.rawQuery(sqlQuery.toString(), params);
			if (c != null && c.moveToFirst()) {
				do {
					ForcusProductOfNVBHDTO item = new ForcusProductOfNVBHDTO();
					item.initDateWithCursor(c);
					result.listForcusProduct.add(item);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		// get list type forcus product
		ArrayList<String> listForcusType = new ArrayList<String>();
		for (int i = 0, size = result.listForcusProduct.size(); i < size; i++) {
			ForcusProductOfNVBHDTO dto = result.listForcusProduct.get(i);
			String currentType = String.valueOf(dto.typeProductFocus);
			if (i == 0 || !this.isExistListType(listForcusType, currentType)) {
				listForcusType.add(currentType);
			}
		}

		for (int i = 0, size = listForcusType.size(); i < size; i++) {
			String type = listForcusType.get(i);
			ForcusProductOfNVBHDTO totalType = new ForcusProductOfNVBHDTO();
			// totalType.typeProductFocus = StringUtil
			// .getString(R.string.TEXT_HEADER_MENU_MHTT) + type;
			totalType.typeProductFocus = type;
			long planAmount = 0;
			long doneAmount = 0;
			long remainAmount = 0;
			int progress = 0;
			for (int j = 0, size2 = result.listForcusProduct.size(); j < size2; j++) {
				ForcusProductOfNVBHDTO dto = result.listForcusProduct.get(j);
				if (type.equals(dto.typeProductFocus)) {
					planAmount += dto.planAmount;
					doneAmount += dto.doneAmount;
				}
			}
			remainAmount = (planAmount - doneAmount) >= 0 ? (planAmount - doneAmount)
					: 0;
			if (doneAmount > 0) {
				progress = (int) (doneAmount * 100 / (planAmount <= 0 ? doneAmount
						: planAmount));
			} else {
				if (planAmount == 0) {
					progress = 100;
				} else {
					progress = 0;
				}
			}
			totalType.planAmount = planAmount;
			totalType.doneAmount = doneAmount;
			totalType.remainAmount = remainAmount;
			totalType.progress = progress;

			result.listTotalForcusProduct.add(totalType);
		}

		// number day plan sale in month
		try {
			result.numberDayPlan = new SALE_DAYS_TABLE(mDB)
					.getPlanWorkingDaysOfMonth(new Date());
			// number day sold in month
			result.numberDaySold = new EXCEPTION_DAY_TABLE(mDB)
					.getWorkingDaysOfMonth();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		
		// percent
		if (result.numberDaySold <= 0) {
			result.progressSold = 0;
		} else {
			result.progressSold = (int) ((float) result.numberDaySold * 100 / ((float) result.numberDayPlan <= 0 ? (float) result.numberDaySold
					: (float) result.numberDayPlan));
		}

		return result;
	}

	/**
	 * check type forcus exist in list
	 * 
	 * @param listType
	 * @param typeNeedCheck
	 * @return
	 * @return: boolean
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 2, 2013
	 */
	public boolean isExistListType(ArrayList<String> listType, String typeNeedCheck) {
		boolean kq = false;
		if (!StringUtil.isNullOrEmpty(typeNeedCheck)) {
			for (int i = 0, size = listType.size(); i < size; i++) {
				if (typeNeedCheck.equals(listType.get(i))) {
					kq = true;
					break;
				}
			}
		}
		return kq;
	}
	
	/**
	 * 
	 * get list product for C2 sale or buy
	 * 
	 * @param ext
	 * @return
	 * @return: C2SaleOrderlViewDTO
	 * @throws:
	 * @author: Nguyen Thanh Dung
	 * @date: 06/12/2013
	 */
	public C2SaleOrderlViewDTO getListProductForC2Order(Bundle ext) {
		C2SaleOrderlViewDTO order = new C2SaleOrderlViewDTO();

		ArrayList<String> totalParams = new ArrayList<String>();

		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT p.PRODUCT_CODE       PRODUCT_CODE, ");
		sql.append("               p.PRODUCT_ID       PRODUCT_ID, ");
		sql.append("               p.PRODUCT_NAME       PRODUCT_NAME, ");
		sql.append("       		   ( CASE ");
		sql.append("          		WHEN p.SORT_ORDER IS NULL THEN 'z' ");
		sql.append("          		ELSE SORT_ORDER ");
		sql.append("          		END )        AS SORT_ORDER, ");
		sql.append("               p.NAME_TEXT  PRODUCT_NAME_TEXT ");
		sql.append("        FROM   PRODUCT p, PRODUCT_INFO pi ");
		sql.append("        WHERE  p.STATUS = 1 ");
		sql.append("        	   AND p.convfact > 1 "); // thung/ket
		sql.append("               AND pi.STATUS = 1 ");
		sql.append("               AND pi.TYPE = 1 ");
		sql.append("               AND pi.OBJECT_TYPE = 0 ");
		sql.append("               AND pi.PRODUCT_INFO_ID = p.CAT_ID ");
//		sql.append("               AND pi.PRODUCT_INFO_CODE != 'Z' AND pi.PRODUCT_INFO_CODE != 'X' ");
		sql.append("		ORDER BY SORT_ORDER, p.PRODUCT_CODE, p.PRODUCT_NAME ");
		
		String[] params = new String[] {};
		params = totalParams.toArray(new String[totalParams.size()]);

		Cursor c = null;
		try {
			c = rawQuery(sql.toString(), params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						C2SaleOrderDetailViewDTO item = new C2SaleOrderDetailViewDTO();
						item.initFromCursor(c);
						
						order.listProduct.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}

		return order;
	}

	/**
	 * get list product for C2 sale or buy
	 * 
	 * @param ext
	 * @return
	 * @return: C2PurchaselViewDTO
	 * @throws:
	 * @author: Nguyen Thanh Dung
	 * @date: 06/12/2013
	 */
	public C2PurchaselViewDTO getListProductForC2Purchase(Bundle ext) {
		C2PurchaselViewDTO order = new C2PurchaselViewDTO();

		ArrayList<String> totalParams = new ArrayList<String>();

		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT p.PRODUCT_CODE       PRODUCT_CODE, ");
		sql.append("               p.PRODUCT_ID       PRODUCT_ID, ");
		sql.append("               p.PRODUCT_NAME       PRODUCT_NAME, ");
		sql.append("       		   ( CASE ");
		sql.append("          		WHEN p.SORT_ORDER IS NULL THEN 'z' ");
		sql.append("          		ELSE SORT_ORDER ");
		sql.append("          		END )        AS SORT_ORDER, ");
		sql.append("               p.NAME_TEXT  PRODUCT_NAME_TEXT ");
		sql.append("        FROM   PRODUCT p, PRODUCT_INFO pi ");
		sql.append("        WHERE  p.STATUS = 1 ");
		sql.append("        	   AND p.convfact > 1 "); // thung/ket
		sql.append("               AND pi.STATUS = 1 ");
		sql.append("               AND pi.TYPE = 1 ");
		sql.append("               AND pi.OBJECT_TYPE = 0 ");
		sql.append("               AND pi.PRODUCT_INFO_ID = p.CAT_ID ");
//		sql.append("               AND pi.PRODUCT_INFO_CODE != 'Z' AND pi.PRODUCT_INFO_CODE != 'X' ");
		sql.append("		ORDER BY SORT_ORDER, p.PRODUCT_CODE, p.PRODUCT_NAME ");

		String[] params = new String[] {};
		params = totalParams.toArray(new String[totalParams.size()]);

		Cursor c = null;
		try {
			c = rawQuery(sql.toString(), params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						C2PurchaseDetailViewDTO item = new C2PurchaseDetailViewDTO();
						item.initFromCursor(c);
						
						order.listProduct.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}

		return order;
	}
}
