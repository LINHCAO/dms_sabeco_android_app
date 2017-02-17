/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.text.TextUtils;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.CreateCustomerInfoDTO.AreaItem;
import com.viettel.dms.dto.view.CreateCustomerInfoDTO.CustomerType;
import com.viettel.dms.dto.view.CustomerInfoDTO;
import com.viettel.dms.dto.view.CustomerListDTO;
import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.dto.view.CustomerNotPsdsInMonthReportViewDTO;
import com.viettel.dms.dto.view.GsnppLessThan2MinsDTO;
import com.viettel.dms.dto.view.GsnppLessThan2MinsDTO.LessThan2MinsItem;
import com.viettel.dms.dto.view.ImageListDTO;
import com.viettel.dms.dto.view.ImageListItemDTO;
import com.viettel.dms.dto.view.NewCustomerListDTO;
import com.viettel.dms.dto.view.WrongPlanCustomerDTO;
import com.viettel.dms.dto.view.WrongPlanCustomerDTO.WrongPlanCustomerItem;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * Luu thong tin khach hang
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
@SuppressLint("DefaultLocale")
public class CUSTOMER_TABLE extends ABSTRACT_TABLE {
	// id khach hang
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	// ma khach hang
	public static final String CUSTOMER_CODE = "CUSTOMER_CODE";
	// code rut gon
	public static final String SHORT_CODE = "SHORT_CODE";
	public static final String FIRST_CODE = "FIRST_CODE";
	// ten khach hang
	public static final String CUSTOMER_NAME = "CUSTOMER_NAME";
	// id NPP
	public static final String SHOP_ID = "SHOP_ID";
	// ?
	public static final String GROUP_TRANSFER_ID = "GROUP_TRANSFER_ID";
	public static final String CASHIER_STAFF_ID = "CASHIER_STAFF_ID";
	// cong no cho phep toi da
	public static final String MAX_DEBIT_AMOUNT = "MAX_DEBIT_AMOUNT";
	// so ngay toi da cho phep no
	public static final String MAX_DEBIT_DATE = "MAX_DEBIT_DATE";
	// cho phep no
	public static final String APPLY_DEBIT_LIMITED = "APPLY_DEBIT_LIMITED";
	// truong nay chua dung
	// public static final String COUNTRY = "COUNTRY";
	// ma vung
	public static final String AREA_ID = "AREA_ID";
	// duong
	public static final String STREET = "STREET";
	// so nha
	public static final String HOUSENUMBER = "HOUSENUMBER";
	// dia chi full
	public static final String ADDRESS = "ADDRESS";
	// dien thoai
	public static final String PHONE = "PHONE";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// truong nay chua dung
	// public static final String POSTAL_CODE = "POSTAL_CODE";
	// nguoi lien he
	public static final String CONTACT_NAME = "CONTACT_NAME";
	// truong nay chua dung
	// public static final String TAX_CODE = "TAX_CODE";
	// so di dong
	public static final String MOBIPHONE = "MOBIPHONE";
	// khu vuc 1: thanh thi, 2: nong thon
	public static final String LOCATION = "LOCATION";
	// loai kenh
	public static final String CHANNEL_TYPE_ID = "CHANNEL_TYPE_ID";
	// do trung thanh
	public static final String LOYALTY = "LOYALTY";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// loai chuong trinh tham gia
	public static final String DISPLAY = "DISPLAY";
	// don hang cuoi cung duoc duyet
	public static final String LAST_APPROVE_ORDER = "LAST_APPROVE_ORDER";
	// don hang cuoi cung tao
	public static final String LAST_ORDER = "LAST_ORDER";
	// status
	
	//Tráº¡ng thĂ¡i: ChÆ°a gá»­i SYN_STATE = 0  => 1
	public static final int STATE_CUSTOMER_NOT_SEND = 1;
	//Chá»� duyá»‡t STATUS = 2 => 2
	public static final int STATE_CUSTOMER_WAIT_APPROVED = 2;
	//Tá»« chá»‘i STATUS = 3 => 3
	public static final int STATE_CUSTOMER_NOT_APPROVED = 3;
	//Lá»—i SYN_STATE = 1 => 4
	public static final int STATE_CUSTOMER_ERROR = 4;
	/**
	 * Chá»� duyá»‡t STATUS = 2 => STATE = 2
	 * Tá»« chá»‘i STATUS = 3 => STATE = 3
	 */
	public static final String STATUS = "STATUS";
	
	public static final String NAME_TEXT = "NAME_TEXT"; 
	// lat
	public static final String LAT = "LAT";
	// lng
	public static final String LNG = "LNG";

	//mĂ£ C2 Kh Ä‘ang láº¥y hĂ ng
	public static final String PARENT_CUSTOMER_ID = "PARENT_CUSTOMER_ID";
	//NVBH táº¡o tablet
	public static final String STAFF_ID = "STAFF_ID";
	
	
	public static final String CUSTOMER_TABLE = "CUSTOMER";

	public CUSTOMER_TABLE(SQLiteDatabase mDB) {
		this.tableName = CUSTOMER_TABLE;

		this.columns = new String[] { CUSTOMER_ID, SHORT_CODE, CUSTOMER_CODE, CUSTOMER_NAME, ADDRESS, SHOP_ID, AREA_ID,
				STREET, HOUSENUMBER, PHONE, CREATE_DATE, UPDATE_DATE, CONTACT_NAME, MOBIPHONE, LOCATION,
				CHANNEL_TYPE_ID, LOYALTY, CREATE_USER, UPDATE_USER, DISPLAY, LAST_APPROVE_ORDER, LAST_ORDER, STATUS,
				LAT, LNG, SYN_STATE, MAX_DEBIT_AMOUNT, MAX_DEBIT_DATE, PARENT_CUSTOMER_ID, STAFF_ID };
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
		ContentValues value = initDataRow((CustomerDTO) dto);
		return insert(null, value);
	}

	/**
	 * Cap nhat customer
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: int
	 * @throws:
	 */
	public int update(CustomerDTO dto) {
		ContentValues value = initDataRow(dto);
		String[] params = { "" + dto.getCustomerId() };
		return update(value, CUSTOMER_ID + " = ?", params);
	}

	public long update(AbstractTableDTO dto) {
		CustomerDTO cusDTO = (CustomerDTO) dto;
		ContentValues value = initDataRow(cusDTO);
		String[] params = { "" + cusDTO.getCustomerId() };
		return update(value, CUSTOMER_ID + " = ?", params);
	}

	/**
	 * update vi tri cua khach hang
	 * 
	 * @author : BangHN since : 1.0
	 */
	public long updateLocation(CustomerDTO dto) {
		ContentValues value = initCustomerLocationData(dto);
		String[] params = { "" + dto.getCustomerId() };
		return update(value, CUSTOMER_ID + " = ?", params);
	}

	/**
	 * Cap nhat khi tao moi order
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	public long updateLastOrder(AbstractTableDTO dto) {
		CustomerDTO cusDTO = (CustomerDTO) dto;
		ContentValues value = initDataUpdateFromOrder(cusDTO);
		String[] params = { "" + cusDTO.getCustomerId() };
		return update(value, CUSTOMER_ID + " = ?", params);
	}

	/**
	 * Xoa 1 dong cua CSDL
	 * 
	 * @author: HieuNH
	 * @param id
	 * @return: int
	 * @throws:
	 */
	public int delete(String id) {
		String[] params = { id };
		return delete(CUSTOMER_ID + " = ?", params);
	}

	public long delete(AbstractTableDTO dto) {
		CustomerDTO cusDTO = (CustomerDTO) dto;
		String[] params = { cusDTO.getCustomerId() };
		return delete(CUSTOMER_ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: BangHN
	 * @param id
	 * @return: CustomerDTO
	 * @throws:
	 */
	public CustomerDTO getCustomerById(String id) {
		CustomerDTO CustomerDTO = null;
		Cursor c = null;
		try {
			String[] params = { id };
			c = query(CUSTOMER_ID + " = ?", params, null, null, null);
			if (c != null) {
				if (c.moveToFirst()) {
					CustomerDTO = initLogDTOFromCursor(c);
				}
			}
		} catch (Exception ex) {
			c = null;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		return CustomerDTO;
	}

	/**
	 * get customer by id for create customer
	 * @author: duongdt3
	 * @since: 11:06:32 6 Jan 2014
	 * @return: CustomerDTO
	 * @throws:  
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public CustomerDTO getCustomerByIdForCreateCustomer(String id) throws Exception {
		CustomerDTO customerDTO = null;
		Cursor c = null;
		try {
			String[] params = { id };
			c = query(CUSTOMER_ID + " = ?", params, null, null, null);
			if (c != null) {
				if (c.moveToFirst()) {
					customerDTO = new CustomerDTO();
					customerDTO.initCreateCustomerDTOFromCursor(c);
				}
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			throw ex;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}

		return customerDTO;
	}
	
	/**
	 * Lay thong tin trong CUSTOMER & CUSTOMER_TYPE & SKU THANG, NUMBER ORDER
	 * THANG
	 * @return: CustomerDTO
	 * @throws:
	 */
	public CustomerInfoDTO getCustomerInfo(String customerId, String shopId) throws Exception {
		String staffId = GlobalInfo.getInstance().getCurrentStaffId();
		CUSTOMER_TABLE cusTable = new CUSTOMER_TABLE(mDB);
		int disOrder = cusTable.getDistanceOrder(customerId, shopId, staffId);
		Cursor cursor = null;
		CustomerInfoDTO customerInfo = new CustomerInfoDTO();
		List<String> params = new ArrayList<String>();
		String dateNow = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		String dateFirstMonth = DateUtils.getFirstDateOfNumberPreviousMonthWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT, 0);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT C.customer_id 	as CUSTOMER_ID, ");
		sql.append("       C.SHORT_CODE 	AS CUSTOMER_CODE, ");
		sql.append("       C.shop_id 		AS SHOP_ID, ");
		sql.append("       C.customer_name 	AS CUSTOMER_NAME, ");
		sql.append("       C.housenumber 	AS HOUSENUMBER, ");
		sql.append("       C.street 		AS STREET, ");
		sql.append("       C.address 		AS ADDRESS, ");
		sql.append("       C.phone 			AS PHONE, ");
		sql.append("       C.MOBIPHONE 		AS MOBIPHONE, ");
		sql.append("       C.CONTACT_NAME 	AS CONTACT_NAME, ");
		sql.append("       C.lat 			AS LAT, ");
		sql.append("       C.lng 			AS LNG, ");
		sql.append("       DISTANCE , ");
		sql.append("		URL,	");
		sql.append("		CUSTOMER_AVATAR_ID,	");
		sql.append("       cti, ");
		sql.append("       ctc, ");
		sql.append("       ctn, ");
		sql.append("       NUM_ORDER_IN_MONTH.COUNT AS NUM_ORDER_IN_MONTH, ");
		sql.append("       NUM_SKU.sku AS NUM_SKU, ");
		sql.append("       AVG_IN_MONTH.quantity AS AVG_IN_MONTH ");
		//sku trong thang
		sql.append("FROM   customer c left join ");
		sql.append("       (Select CT.channel_type_id as cti, CT.channel_type_code as ctc, CT.channel_type_name as ctn from channel_type ct where type=3) ");
		sql.append(" on c.[channel_type_id] = cti ");
		sql.append("	   left join(select URL, CUSTOMER_AVATAR_ID, customer_id as ci from CUSTOMER_AVATAR where customer_id=? and status=1) on c.customer_id=ci");
		params.add(customerId);
		//add disorder
		sql.append("	   ,(Select	" + disOrder + " as DISTANCE), ");
		sql.append("       (SELECT Count(1) AS COUNT ");
		sql.append("        FROM   (SELECT Date(a.order_date), ");
		sql.append("                       Sum(CASE ");
		sql.append("                             WHEN a.order_type = 'CM' THEN -a.total ");
		sql.append("                             WHEN a.order_type = 'CO' THEN -a.total ");
		sql.append("                             ELSE a.total ");
		sql.append("                           END) AS tong ");
		sql.append("                FROM   sale_order a ");
		sql.append("                WHERE  a.customer_id = ? ");
		params.add(customerId);
		sql.append("                       AND ( ( a.approved = 1 ");
		sql.append("                               AND substr(a.order_date,0,11) >= ");
		sql.append("                                   ?) ");
		params.add(dateFirstMonth);
		sql.append("                              OR ( a.approved != 2 ");
		sql.append("                                   AND substr(a.order_date,0,11) = ");
		sql.append("                                       ?) ");
		params.add(dateNow);
		sql.append("                           ) ");
		sql.append("                GROUP  BY Date(a.order_date)) ");
		sql.append("        WHERE  tong > 0)                            AS NUM_ORDER_IN_MONTH, ");
		sql.append("       (SELECT Count (DISTINCT product_id) sku ");
		sql.append("        FROM   rpt_sale_in_month sim ");
		sql.append("        WHERE  sim.customer_id = ? and sim.shop_id = ? and sim.amount > 0");
		params.add(customerId);
		params.add(shopId);
		sql.append("               AND substr(sim.month,0,11) >= ? ");
		params.add(dateFirstMonth);
		sql.append("       ) AS ");
		sql.append("       NUM_SKU, ");
		//doanh so trung binh trong thang
		sql.append("(select sum(sim.quantity) as quantity ");
		sql.append(" from RPT_SALE_IN_MONTH sim");
		sql.append(" where sim.CUSTOMER_ID = ? ");
		params.add(customerId);
		sql.append(" and sim.status = 1 and sim.quantity > 0 ");
		sql.append(" and substr(sim.MONTH,0,11) = ?) AS AVG_IN_MONTH ");
		params.add(dateFirstMonth);
		sql.append("WHERE  c.customer_id = ? ");
		params.add(customerId);

		try {
			String[] arrParam = new String[params.size()];
			for (int i = 0; i < params.size(); i++) {
				arrParam[i] = params.get(i);
			}
			cursor = rawQuery(sql.toString(), arrParam);

			if (cursor != null) {
				if (cursor.moveToFirst()) {
					customerInfo.parseCustomerInfo(cursor);
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
		return customerInfo;
	}

	public CustomerDTO initLogDTOFromCursor(Cursor c) {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCustomerTypeId(c.getInt(c.getColumnIndex(CHANNEL_TYPE_ID)));
		customerDTO.shortCode = c.getString(c.getColumnIndex(SHORT_CODE));
		// customerDTO.setLoyalty(c.getInt(c.getColumnIndex(LOYALTY)));
		// customerDTO.setCreateUser(c.getString(c.getColumnIndex(CREATE_USER)));
		// customerDTO.setUpdateUser(c.getString(c.getColumnIndex(UPDATE_USER)));
		// customerDTO.setDislay(c.getString(c.getColumnIndex(DISLAY)));
		// customerDTO.setLastApproverder(c.getString(c.getColumnIndex(LAST_APPROVE_ORDER)));
		// customerDTO.setLastOrder(c.getString(c.getColumnIndex(LAST_ORDER)));
		customerDTO.setAreaId(c.getInt(c.getColumnIndex(AREA_ID)));
		customerDTO.address = c.getString(c.getColumnIndex("ADDRESS"));
		customerDTO.setCustomerId(c.getLong(c.getColumnIndex(CUSTOMER_ID)));
		customerDTO.setCustomerCode(c.getString(c.getColumnIndex(CUSTOMER_CODE)));
		customerDTO.setCustomerName(c.getString(c.getColumnIndex(CUSTOMER_NAME)));
		customerDTO.setShopId(c.getString(c.getColumnIndex(SHOP_ID)));
		// customerDTO.setCountry(c.getString(c.getColumnIndex(COUNTRY)));
		// customerDTO.setAreaCode(c.getString(c.getColumnIndex(AREA_CODE)));
		customerDTO.setStreet(c.getString(c.getColumnIndex(STREET)));
		customerDTO.setHouseNumber(c.getString(c.getColumnIndex(HOUSENUMBER)));
		customerDTO.setPhone(c.getString(c.getColumnIndex(PHONE)));
		// customerDTO.setCreateDate(c.getString(c.getColumnIndex(CREATE_DATE)));
		// customerDTO.setUpdateDate(c.getString(c.getColumnIndex(UPDATE_DATE)));
		// customerDTO.setPostalCode(c.getString(c.getColumnIndex(POSTAL_CODE)));
		customerDTO.setContactPerson(c.getString(c.getColumnIndex(CONTACT_NAME)));
		// customerDTO.setTaxCode(c.getString(c.getColumnIndex(TAX_CODE)));
		customerDTO.setMobilephone(c.getString(c.getColumnIndex(MOBIPHONE)));
		// customerDTO.setLocation(c.getInt(c.getColumnIndex(LOCATION)));
		customerDTO.setStatus(c.getInt(c.getColumnIndex(STATUS)));
		customerDTO.setLat(c.getFloat(c.getColumnIndex(LAT)));
		customerDTO.setLng(c.getFloat(c.getColumnIndex(LNG)));
		customerDTO.setMaxDebitAmount(c.getLong(c.getColumnIndex(MAX_DEBIT_AMOUNT)));
		customerDTO.setMaxDebitDate(c.getString(c.getColumnIndex(MAX_DEBIT_DATE)));

		String parentCustomerId = c.getString(c.getColumnIndex(PARENT_CUSTOMER_ID));
		if(StringUtil.isNullOrEmpty(parentCustomerId)) {
			customerDTO.setParentCustomerId(0);
		} else {
			customerDTO.setParentCustomerId(Long.valueOf(parentCustomerId));
		}

		if (StringUtil.isNullOrEmpty(customerDTO.address)) {
			customerDTO.address = customerDTO.getHouseNumber() + " " + customerDTO.street;
		}
		return customerDTO;
	}

	public ContentValues initCustomerLocationData(CustomerDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(LAT, dto.getLat());
		editedValues.put(LNG, dto.getLng());
		editedValues.put(UPDATE_DATE, dto.getUpdateDate());
		editedValues.put(UPDATE_USER, dto.getUpdateUser());
		return editedValues;
	}

	/**
	 * Khi táº¡o khĂ¡ch hĂ ng
	 * @author: duongdt3
	 * @since: 14:13:47 7 Jan 2014
	 * @return: ContentValues
	 * @throws:  
	 * @param dto
	 * @return
	 */
	public ContentValues initDataRow(CustomerDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(CUSTOMER_ID, dto.getCustomerId());
		editedValues.put(CUSTOMER_CODE, dto.getCustomerCode());
		editedValues.put(CUSTOMER_NAME, dto.getCustomerName());
		editedValues.put(SHOP_ID, dto.getShopId());
		editedValues.put(AREA_ID, dto.getAreaId());
		if(!StringUtil.isNullOrEmpty(dto.getHouseNumber())) {
			editedValues.put(HOUSENUMBER, dto.getHouseNumber());
		}
		if(!StringUtil.isNullOrEmpty(dto.getStreet())) {
			editedValues.put(STREET, dto.getStreet());
		}
		editedValues.put(PHONE, dto.getPhone());
		editedValues.put(CREATE_DATE, dto.getCreateDate());
		//editedValues.put(UPDATE_DATE, dto.getUpdateDate());
		editedValues.put(CONTACT_NAME, dto.getContactPerson());
		editedValues.put(MOBIPHONE, dto.getMobilephone());
		//editedValues.put(LOCATION, dto.getLocation());
		editedValues.put(CHANNEL_TYPE_ID, dto.getCustomerTypeId());
		//editedValues.put(LOYALTY, dto.getLoyalty());
		editedValues.put(CREATE_USER, dto.getCreateUser());
		//editedValues.put(UPDATE_USER, dto.getUpdateUser());
		//editedValues.put(DISPLAY, dto.getDislay());
		//editedValues.put(LAST_APPROVE_ORDER, dto.getLastApproverder());
		//editedValues.put(LAST_ORDER, dto.getLastOrder());
		editedValues.put(STATUS, dto.getStatus());
		editedValues.put(LAT, dto.getLat());
		editedValues.put(LNG, dto.getLng());
		editedValues.put(ADDRESS, dto.getAddress());
		editedValues.put(NAME_TEXT, dto.getNameText());
		//editedValues.put(MAX_DEBIT_AMOUNT, dto.getMaxDebitAmount());
		//editedValues.put(MAX_DEBIT_DATE, dto.getMaxDebitDate());
		editedValues.put(STAFF_ID, dto.getStaffId());
		editedValues.put(SYN_STATE, 0);
		//náº¿u cĂ³ parrent customer id thĂ¬ má»›i insert vĂ o
		if (dto.getParentCustomerId() > 0) {
			editedValues.put(PARENT_CUSTOMER_ID, dto.getParentCustomerId());
		}
		
		return editedValues;
	}
	
	/**
	 * Khi sá»­a khĂ¡ch hĂ ng
	 * @author: duongdt3
	 * @since: 14:13:58 7 Jan 2014
	 * @return: ContentValues
	 * @throws:  
	 * @param dto
	 * @return
	 */
	public ContentValues initUpdateDataRow(CustomerDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(CUSTOMER_ID, dto.getCustomerId());
		//editedValues.put(CUSTOMER_CODE, dto.getCustomerCode());
		editedValues.put(CUSTOMER_NAME, dto.getCustomerName());
		//editedValues.put(SHOP_ID, dto.getShopId());
		editedValues.put(AREA_ID, dto.getAreaId());
		editedValues.put(HOUSENUMBER, dto.getHouseNumber());
		editedValues.put(STREET, dto.getStreet());
		editedValues.put(PHONE, dto.getPhone());
		editedValues.put(UPDATE_DATE, dto.getUpdateDate());
		editedValues.put(CONTACT_NAME, dto.getContactPerson());
		editedValues.put(MOBIPHONE, dto.getMobilephone());
		editedValues.put(CHANNEL_TYPE_ID, dto.getCustomerTypeId());
		editedValues.put(UPDATE_USER, dto.getUpdateUser());
		editedValues.put(STATUS, dto.getStatus());
		editedValues.put(LAT, dto.getLat());
		editedValues.put(LNG, dto.getLng());
		editedValues.put(ADDRESS, dto.getAddress());
		editedValues.put(NAME_TEXT, dto.getNameText());
		editedValues.put(SYN_STATE, 0);
		
		if (dto.getParentCustomerId() > 0) {
			editedValues.put(PARENT_CUSTOMER_ID, dto.getParentCustomerId());
		}else{
			//náº¿u khĂ´ng cĂ³ parrent customer id => Ä‘áº·t rá»—ng ""
			editedValues.put(PARENT_CUSTOMER_ID, "");
		}
		
		return editedValues;
	}

	/**
	 * Khi xĂ³a khĂ¡ch hĂ ng
	 * @author: duongdt3
	 * @since: 14:13:23 7 Jan 2014
	 * @return: ContentValues
	 * @throws:  
	 * @param dto
	 * @return
	 */
	private ContentValues initDeleteDataRow(CustomerDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(STATUS, dto.getStatus());
		return editedValues;
	}
	
	/**
	 * Cap nhat customer khi tao moi order
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: ContentValues
	 * @throws:
	 */
	public ContentValues initDataUpdateFromOrder(CustomerDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(LAST_ORDER, dto.getLastOrder());
		if (!StringUtil.isNullOrEmpty(dto.getLastApproverder())) {
			editedValues.put(LAST_APPROVE_ORDER, dto.getLastApproverder());
		}
		return editedValues;
	}

	int getDistanceOrder(String customerId, String shopId, String staffId){
		return getDistanceOrder(customerId, shopId, staffId, true);
	}

	int getDistanceOrder(String customerId, String shopId, String staffId, boolean isSetDefault){
		int dis = 0;
		Cursor cDistance = null;
		StringBuffer sqlObject = new StringBuffer();
		ArrayList<String> paramsObject = new ArrayList<String>();
		sqlObject.append("	SELECT	");
		sqlObject.append("	    IFNULL(IFNULL(CUS_DIS, STAFF_DIS), SHOP_DIS) DISTANCE	");
		sqlObject.append("	FROM	");
		sqlObject.append("	    (   SELECT	");
		sqlObject.append("	        (SELECT	");
		sqlObject.append("	            DISTANCE_ORDER	");
		sqlObject.append("	        FROM	");
		sqlObject.append("	            SHOP	");
		sqlObject.append("	        WHERE	");
		sqlObject.append("	            SHOP_ID = ?) SHOP_DIS,	");
		paramsObject.add(StringUtil.isNullOrEmpty(shopId) ? "-1" : shopId);
		sqlObject.append("	        (SELECT	");
		sqlObject.append("	            DISTANCE_ORDER	");
		sqlObject.append("	        FROM	");
		sqlObject.append("	            STAFF	");
		sqlObject.append("	        WHERE	");
		sqlObject.append("	            STAFF_ID = ?) STAFF_DIS,	");
		paramsObject.add(StringUtil.isNullOrEmpty(staffId) ? "-1" : staffId);
		sqlObject.append("	        (SELECT	");
		sqlObject.append("	            DISTANCE_ORDER	");
		sqlObject.append("	        FROM	");
		sqlObject.append("	            CUSTOMER	");
		sqlObject.append("	        WHERE	");
		sqlObject.append("	            CUSTOMER_ID = ?) CUS_DIS)	");
		paramsObject.add(StringUtil.isNullOrEmpty(customerId) ? "-1" : customerId);
		try {

			cDistance = rawQueries(sqlObject.toString(), paramsObject);
			if (cDistance != null) {
				if (cDistance.moveToFirst()) {
					dis = cDistance.getInt(0);
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (cDistance != null) {
				cDistance.close();
			}
		}

		//get default distance
		if (dis <= 0 && isSetDefault){
			dis = Constants.DEFAULT_DISTANCE_ORDER;
		}
		return dis;
	}


	/**
	 * Ds khach hangf
	 * 
	 * @author: TamPQ
	 * @param staffId
	 * @param customerName
	 * @param customerCode
	 * @param visitPlan
	 * @param isGetWrongPlan
	 * @param page
	 * @param isGetTotalPage
	 * @param type
	 * @return: CustomerListDTO
	 * @throws:
	 */
	public CustomerListDTO getCustomerList(long staffId, long shopId, String customerName, String customerCode,
			String visitPlan, boolean isGetWrongPlan, int page, boolean isGetTotalPage, int type) throws Exception {
		CustomerListDTO dto = null;
		Cursor c = null;
		Cursor cTotalRow = null;
		String daySeq = "";
		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
		ArrayList<String> shopList = shopTable.getShopRecursiveReverse(String.valueOf(shopId));
		String shopStr = TextUtils.join(",", shopList);
		String dateNow=DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		String dateLastTwoMonth = DateUtils.getFirstDateOfNumberPreviousMonthWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT,-2);
		if (!StringUtil.isNullOrEmpty(visitPlan)) {
			if (visitPlan.equals(Constants.DAY_LINE[0])) {
				daySeq = "2";
			} else if (visitPlan.equals(Constants.DAY_LINE[1])) {
				daySeq = "3";
			} else if (visitPlan.equals(Constants.DAY_LINE[2])) {
				daySeq = "4";
			} else if (visitPlan.equals(Constants.DAY_LINE[3])) {
				daySeq = "5";
			} else if (visitPlan.equals(Constants.DAY_LINE[4])) {
				daySeq = "6";
			} else if (visitPlan.equals(Constants.DAY_LINE[5])) {
				daySeq = "7";
			} else if (visitPlan.equals(Constants.DAY_LINE[6])) {
				daySeq = "8";
			}
		}

		//lay cau hinh KC
		dto = new CustomerListDTO();
		double distance = 0;
		if (type == 1){
			//pg chi lay cua NV
			distance = getDistanceOrder(null, null, String.valueOf(staffId), false);
		} else {
			//nv thi lay cua shop + nv
			distance = getDistanceOrder(null, String.valueOf(shopId), String.valueOf(staffId));
		}
		//dto.distance
		StringBuffer var1 = new StringBuffer();
		StringBuffer totalPageSql = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();

		var1.append("SELECT * ");
		var1.append("FROM   (SELECT VP.VISIT_PLAN_ID        AS VISIT_PLAN_ID, ");
		var1.append("               VP.SHOP_ID              AS SHOP_ID, ");
		var1.append("               VP.STAFF_ID             AS STAFF_ID, ");
		var1.append("               VP.FROM_DATE            AS FROM_DATE, ");
		var1.append("               VP.TO_DATE              AS TO_DATE, ");
		var1.append("               S.SHOP_CODE             AS SHOP_CODE, ");
		var1.append("               RT.ROUTING_ID           AS ROUTING_ID, ");
		var1.append("               RT.ROUTING_CODE         AS ROUTING_CODE, ");
		var1.append("               RT.ROUTING_NAME         AS ROUTING_NAME, ");
		var1.append("               CT.CUSTOMER_ID          AS CUSTOMER_ID, ");
		var1.append("               CT.DISTANCE_ORDER       AS DISTANCE_ORDER, ");
		var1.append("               CT.SHORT_CODE  			AS CUSTOMER_CODE, ");
		var1.append("               CT.SHORT_CODE  			AS SHORT_CODE, ");
		var1.append("               CT.CUSTOMER_NAME        AS CUSTOMER_NAME, ");
		var1.append("               CT.SHOP_ID        		AS CUSTOMER_SHOP_ID, ");
		var1.append("               (SELECT DISTANCE_ORDER   FROM SHOP sh1 WHERE 1=1 and sh1.SHOP_ID = CT.SHOP_ID) AS     SHOP_DISTANCE, ");
		var1.append("               CT.MOBIPHONE            AS MOBIPHONE, ");
		var1.append("               CT.NAME_TEXT    		AS NAME_TEXT, ");
		var1.append("               CT.PHONE                AS PHONE, ");
		var1.append("               CT.LAT                  AS LAT, ");
		var1.append("               CT.LNG                  AS LNG, ");
		var1.append("               CT.CHANNEL_TYPE_ID      AS CHANNEL_TYPE_ID, ");
		var1.append("               EXCEPTION_ORDER_DATE    AS EXCEPTION_ORDER_DATE, ");
		var1.append("               CT.ADDRESS       		AS ADDRESS, ");
		var1.append("               ( CT.HOUSENUMBER ");
		var1.append("                 || ' ' ");
		var1.append("                 || CT.STREET )        AS STREET, ");
		var1.append("               ( CASE ");
		var1.append("                   WHEN RTC.MONDAY = 1 THEN 'T2' ");
		var1.append("                   ELSE '' ");
		var1.append("                 END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.TUESDAY = 1 THEN ',T3' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.WEDNESDAY = 1 THEN ',T4' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.THURSDAY = 1 THEN ',T5' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.FRIDAY = 1 THEN ',T6' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.SATURDAY = 1 THEN ',T7' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.SUNDAY = 1 THEN ',CN' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END )              AS CUS_PLAN, ");
		var1.append("               ( CASE ");
		var1.append("                   WHEN RTC.SEQ2 = 0 THEN '' ");
		var1.append("                   ELSE RTC.SEQ2 ");
		var1.append("                 END ) ");
		var1.append("               || ( CASE ");
		var1.append("                      WHEN RTC.SEQ3 = 0 THEN '' ");
		var1.append("                      ELSE ',' ");
		var1.append("                           || RTC.SEQ3 ");
		var1.append("                    END ) ");
		var1.append("               || ( CASE ");
		var1.append("                      WHEN RTC.SEQ4 = 0 THEN '' ");
		var1.append("                      ELSE ',' ");
		var1.append("                           || RTC.SEQ4 ");
		var1.append("                    END ) ");
		var1.append("               || ( CASE ");
		var1.append("                      WHEN RTC.SEQ5 = 0 THEN '' ");
		var1.append("                      ELSE ',' ");
		var1.append("                           || RTC.SEQ5 ");
		var1.append("                    END ) ");
		var1.append("               || ( CASE ");
		var1.append("                      WHEN RTC.SEQ6 = 0 THEN '' ");
		var1.append("                      ELSE ',' ");
		var1.append("                           || RTC.SEQ6 ");
		var1.append("                    END ) ");
		var1.append("               || ( CASE ");
		var1.append("                      WHEN RTC.SEQ7 = 0 THEN '' ");
		var1.append("                      ELSE ',' ");
		var1.append("                           || RTC.SEQ7 ");
		var1.append("                    END ) ");
		var1.append("               || ( CASE ");
		var1.append("                      WHEN RTC.SEQ8 = 0 THEN '' ");
		var1.append("                      ELSE ',' ");
		var1.append("                           || RTC.SEQ8 ");
		var1.append("                    END )              AS PLAN_SEQ, ");
		var1.append("               RTC.ROUTING_CUSTOMER_ID AS ROUTING_CUSTOMER_ID ");

		if (!StringUtil.isNullOrEmpty(daySeq)) {
			var1.append("  			, RTC.SEQ" + daySeq + " as DAY_SEQ");
		}

		var1.append("        FROM   VISIT_PLAN VP, ");
		var1.append("               ROUTING RT, ");
		var1.append("               (SELECT ROUTING_CUSTOMER_ID, ");
		var1.append("                       ROUTING_ID, ");
		var1.append("                       CUSTOMER_ID as CUID, ");
		var1.append("                       STATUS, ");
		var1.append("                       MONDAY, ");
		var1.append("                       TUESDAY, ");
		var1.append("                       WEDNESDAY, ");
		var1.append("                       THURSDAY, ");
		var1.append("                       FRIDAY, ");
		var1.append("                       SATURDAY, ");
		var1.append("                       SUNDAY, ");
		var1.append("                       SEQ2, ");
		var1.append("                       SEQ3, ");
		var1.append("                       SEQ4, ");
		var1.append("                       SEQ5, ");
		var1.append("                       SEQ6, ");
		var1.append("                       SEQ7, ");
		var1.append("                       SEQ8, ");
		var1.append("                       WEEK1, ");
		var1.append("                       WEEK2, ");
		var1.append("                       WEEK3, ");
		var1.append("                       WEEK4 ");
		var1.append("                FROM   ROUTING_CUSTOMER");
		var1.append("                WHERE (END_DATE IS NULL OR substr(END_DATE,0,11) >= ?) and ");
		param.add(dateNow);
		var1.append("                substr(START_DATE,0,11) <= ? and ");
		param.add(dateNow);
		var1.append("                ( ");
		var1.append("                (WEEK1 IS NULL AND WEEK2 IS NULL AND WEEK3 IS NULL AND WEEK4 IS NULL) OR");
		var1.append("	(((cast((julianday(?) - julianday(start_date)) / 7  as integer) +");
		param.add(dateNow);
		var1.append("		   (case when ((julianday(?) - julianday(start_date)) % 7 > 0)");
		param.add(dateNow);
		var1.append("			and (");
		var1.append("		cast((case when strftime('%w', ?) = '0' ");
		param.add(dateNow);
		var1.append("									  then 7 ");
		var1.append("									  else strftime('%w', ?)                          ");
		param.add(dateNow);
		var1.append("									  end ) as integer) < ");
		var1.append("		 cast((case when strftime('%w', start_date) = '0' ");
		var1.append("										  then 7 ");
		var1.append("										  else strftime('%w', start_date)                          ");
		var1.append("										  end ) as integer) ) ");
		var1.append("			then 1 else 0 end)) % 4 + 1)=1 and week1=1) or");

		var1.append("	(((cast((julianday(?) - julianday(start_date)) / 7  as integer) +");
		param.add(dateNow);
		var1.append("		   (case when ((julianday(?) - julianday(start_date)) % 7 > 0)");
		param.add(dateNow);
		var1.append("			and (");
		var1.append("		cast((case when strftime('%w', ?) = '0' ");
		param.add(dateNow);
		var1.append("									  then 7 ");
		var1.append("									  else strftime('%w', ?)                          ");
		param.add(dateNow);
		var1.append("									  end ) as integer) < ");
		var1.append("		 cast((case when strftime('%w', start_date) = '0' ");
		var1.append("										  then 7 ");
		var1.append("										  else strftime('%w', start_date)                          ");
		var1.append("										  end ) as integer) ) ");
		var1.append("			then 1 else 0 end)) % 4 + 1)=2 and week2=1) or");

		var1.append("	(((cast((julianday(?) - julianday(start_date)) / 7  as integer) +");
		param.add(dateNow);
		var1.append("		   (case when ((julianday(?) - julianday(start_date)) % 7 > 0)");
		param.add(dateNow);
		var1.append("			and (");
		var1.append("		cast((case when strftime('%w', ?) = '0' ");
		param.add(dateNow);
		var1.append("									  then 7 ");
		var1.append("									  else strftime('%w', ?)                          ");
		param.add(dateNow);
		var1.append("									  end ) as integer) < ");
		var1.append("		 cast((case when strftime('%w', start_date) = '0' ");
		var1.append("										  then 7 ");
		var1.append("										  else strftime('%w', start_date)                          ");
		var1.append("										  end ) as integer) ) ");
		var1.append("			then 1 else 0 end)) % 4 + 1)=3 and week3=1) or");

		var1.append("	(((cast((julianday(?) - julianday(start_date)) / 7  as integer) +");
		param.add(dateNow);
		var1.append("		   (case when ((julianday(?) - julianday(start_date)) % 7 > 0)");
		param.add(dateNow);
		var1.append("			and (");
		var1.append("		cast((case when strftime('%w', ?) = '0' ");
		param.add(dateNow);
		var1.append("									  then 7 ");
		var1.append("									  else strftime('%w', ?)                          ");
		param.add(dateNow);
		var1.append("									  end ) as integer) < ");
		var1.append("		 cast((case when strftime('%w', start_date) = '0' ");
		var1.append("										  then 7 ");
		var1.append("										  else strftime('%w', start_date)                          ");
		var1.append("										  end ) as integer) ) ");
		var1.append("			then 1 else 0 end)) % 4 + 1)=4 and week4=1) )");

		var1.append("                ) RTC  ");
		var1.append("		left join 	( SELECT SC.CUSTOMER_ID as SCCUS, strftime('%d/%m/%Y',EXCEPTION_ORDER_DATE) as EXCEPTION_ORDER_DATE FROM STAFF_CUSTOMER SC WHERE SC.STAFF_ID= ? ) ");
		param.add(String.valueOf(staffId));
		var1.append("			ON SCCUS=CUID	");
		var1.append("               ,CUSTOMER CT, SHOP S ");
		if(type==1){
			var1.append("		,(select CHANNEL_TYPE_ID as CHID from CHANNEL_TYPE CH where type=3 and OBJECT_TYPE =1 and status=1	) ");
		}
		var1.append("        WHERE  VP.ROUTING_ID = RT.ROUTING_ID ");
		var1.append("		and s.status=1 and rt.status=1 ");
		var1.append("               AND RTC.ROUTING_ID = RT.ROUTING_ID ");
		var1.append("               AND RTC.CUID = CT.CUSTOMER_ID ");
		var1.append("               AND substr(VP.FROM_DATE,0,11) <= ? ");
		param.add(dateNow);
		var1.append("				and RT.TYPE=?	");
		param.add(String.valueOf(type));
		if (type==0 && shopId > 0) {
			var1.append("               AND S.SHOP_ID = CT.SHOP_ID ");
			var1.append("               AND CT.SHOP_ID = ? ");
			param.add(String.valueOf(shopId));
		}else if (type==1){
			if(!StringUtil.isNullOrEmpty(shopStr)){
				var1.append("               AND CT.SHOP_ID in ( "+ shopStr  +" )");
			}
			var1.append(" and	CT.CHANNEL_TYPE_ID= CHID ");
		}
		
		var1.append("               AND (VP.TO_DATE IS NULL OR substr(VP.TO_DATE,0,11) >= ?) ");
		param.add(dateNow);
		var1.append("               AND VP.STAFF_ID = ? ");
		param.add(String.valueOf(staffId));
		var1.append("               AND CT.STATUS in (1,2) Group by CUID) CUS ");
		var1.append("       LEFT JOIN (SELECT STAFF_ID, ");
		var1.append("                         CUSTOMER_ID               AS CUSTOMER_ID_1, ");
		var1.append("                         GROUP_CONCAT(OBJECT_TYPE) AS ACTION ");
		var1.append("                  FROM   ACTION_LOG ");
		var1.append("                  WHERE  STAFF_ID = ? ");
		param.add(String.valueOf(staffId));
		var1.append("                         AND substr(START_TIME,0,11) = ? ");
		param.add(dateNow);
		var1.append("                         AND OBJECT_TYPE IN ( 2, 3, 4 ) ");
		var1.append("                  GROUP  BY CUSTOMER_ID) ACTION ");
		var1.append("              ON CUS.CUSTOMER_ID = ACTION.CUSTOMER_ID_1 ");
		
//		Lay so chuong trinh cua KH
//		[Quang] Lay so chuong trinh trung bay
		var1.append("  LEFT JOIN (  SELECT COUNT(*) AS NUM_DISPLAY_PROGRAM_ID, PRO_MAP.OBJECT_ID AS CUS_ID "); 
		var1.append("				FROM   PRO_INFO PRO, ");
		var1.append("       			   PRO_CUS_MAP PRO_MAP ");
		var1.append("				WHERE  1 = 1 ");
		var1.append("       			   AND PRO.PRO_INFO_ID = PRO_MAP.PRO_INFO_ID ");
		var1.append("       			   AND PRO.STATUS = 1 ");
		var1.append("       			   AND substr(PRO.FROM_DATE,0,11) <= ? ");
		param.add(dateNow); 
		var1.append("      				   AND (PRO.TO_DATE IS NULL ");
		var1.append("             				OR substr(PRO.TO_DATE,0,11) >= ? )  ");
		param.add(dateNow);
		var1.append("                  GROUP  BY PRO_MAP.OBJECT_ID) CTTB ");
		var1.append("              ON CUS.CUSTOMER_ID = CTTB.CUS_ID ");
//		[Quang] Lay so chuong trinh trung bay
		
		var1.append("       LEFT JOIN (SELECT STAFF_ID, ");
		var1.append("                         ACTION_LOG_ID AS VISIT_ACT_LOG_ID, ");
		var1.append("                         CUSTOMER_ID AS CUSTOMER_ID_2, ");
		var1.append("                         OBJECT_TYPE AS VISIT, ");
		var1.append("                         START_TIME, ");
		var1.append("                         END_TIME, ");
		var1.append("                         LAT AS VISITED_LAT, ");
		var1.append("                         LNG AS VISITED_LNG, ");
		var1.append("                         IS_OR ");
		var1.append("                  FROM   ACTION_LOG ");
		var1.append("                  WHERE  STAFF_ID = ? ");
		param.add(String.valueOf(staffId));
		var1.append("                         AND substr(START_TIME,0,11) = ? ");
		param.add(dateNow);
		var1.append("                         AND OBJECT_TYPE IN ( 0, 1 ) ");
		var1.append("                  GROUP  BY CUSTOMER_ID) VISIT ");
		var1.append("              ON VISIT.CUSTOMER_ID_2 = CUS.CUSTOMER_ID ");
		
		var1.append("       LEFT JOIN (select OP_SALE_VOLUME_ID, ");
		var1.append("                         CUSTOMER_ID AS OP_SALE_CUSID ");
		var1.append("                         from OP_SALE_VOLUME ");
		var1.append("                         where substr(SALE_DATE,0,11)=? ");
		param.add(dateNow);
		var1.append("                         and staff_id=? group by CUSTOMER_ID) OPSALE ");
		param.add(String.valueOf(staffId));
		var1.append("              ON OP_SALE_CUSID = CUS.CUSTOMER_ID ");
		var1.append("       LEFT JOIN (select OP_STOCK_TOTAL_ID, ");
		var1.append("                         CUSTOMER_ID AS OP_STOCK_CUSID ");
		var1.append("                         from OP_STOCK_TOTAL ");
		var1.append("                         where substr(INVENTORY_DATE,0,11)=? ");
		param.add(dateNow);
		var1.append("                         and staff_id=? group by CUSTOMER_ID) OPSTOCK ");
		param.add(String.valueOf(staffId));
		var1.append("              ON OP_STOCK_CUSID = CUS.CUSTOMER_ID ");
		var1.append("       LEFT JOIN (SELECT SO.SALE_ORDER_ID, SO.CUSTOMER_ID as  CUSTOMER_ID_4");
		var1.append("             FROM   SALE_ORDER SO ");
		var1.append("             WHERE  SO.STAFF_ID = ? ");
		param.add(String.valueOf(staffId));
		var1.append("             AND IFNULL(substr(SO.ORDER_DATE,0,11) >= ");
//		var1.append("                  DATE(?, ");
//		param.add(dateNow);
//		var1.append("                       'start of month', ");
		var1.append("                       ?, 0)  GROUP BY CUSTOMER_ID ) KHT ");
		var1.append("        ON KHT.CUSTOMER_ID_4 = CUS.CUSTOMER_ID ");
		param.add(dateLastTwoMonth);
		
		var1.append("       LEFT JOIN (SELECT CUSTOMER_ID CUSTOMER_ID_5, count(*) TOTAL_PG");
		var1.append("             FROM   TIME_KEEPER ");
		var1.append("             WHERE  STAFF_OWNER_ID = ? and is_absent = 0 ");
		param.add(String.valueOf(staffId));
		var1.append("             AND substr(CREATE_DATE,0,11) = ? GROUP BY CUSTOMER_ID ) TK ");
		param.add(dateNow);
		var1.append("        ON TK.CUSTOMER_ID_5 = CUS.CUSTOMER_ID ");
		var1.append("       LEFT JOIN (");
		var1.append(" SELECT count(PGVP.STAFF_ID) AS SUM_PG, PGVP.CUSTOMER_ID AS CUSTOMERID"); 
		var1.append(" FROM PG_VISIT_PLAN PGVP, STAFF ST "); 
		var1.append(" WHERE 1 = 1 ");
		var1.append(" 	AND PGVP.STAFF_ID = ST.STAFF_ID ");
		var1.append(" 	AND PGVP.PARENT_STAFF_ID = ? ");
		var1.append(" 	AND PGVP.STATUS = 1 AND ST.STATUS=1 ");
		param.add(String.valueOf(staffId));
		var1.append(" 	AND PGVP.SHOP_ID = ? ");
		param.add(GlobalInfo.getInstance().getProfile().getUserData().shopId);
		var1.append(" 	AND substr(PGVP.FROM_DATE,0,11) <= ? ");
		param.add(dateNow);
		var1.append(" 	AND IFNULL(substr(PGVP.TO_DATE,0,11) >= ?, 1) ");
		param.add(dateNow);
		var1.append("   group by PGVP.CUSTOMER_ID) PGVP ON CUS.CUSTOMER_ID=PGVP.CUSTOMERID");

		var1.append(" WHERE 1 = 1");
		if (!StringUtil.isNullOrEmpty(visitPlan)) {// ko phai chon tat ca
			if (isGetWrongPlan) {
				var1.append("	and (upper(CUS_PLAN) like upper(?) OR IS_OR IN (0,1) )");
				param.add("%" + visitPlan + "%");
			} else {
				var1.append("	and upper(CUS_PLAN) like upper(?) ");
				param.add("%" + visitPlan + "%");
			}
		}
		if (!StringUtil.isNullOrEmpty(customerCode)) {
			customerCode = StringUtil.escapeSqlString(customerCode);
			customerCode = DatabaseUtils.sqlEscapeString("%" + customerCode+ "%");
			customerCode = customerCode.substring(1, customerCode.length() - 1);
			var1.append("	and upper(SHORT_CODE) like upper(?) escape '^' ");
			param.add(customerCode);
		}
		if (!StringUtil.isNullOrEmpty(customerName)) {
			customerName = StringUtil
					.getEngStringFromUnicodeString(customerName);
			customerName = StringUtil.escapeSqlString(customerName);
			customerName = DatabaseUtils.sqlEscapeString("%" + customerName+ "%");
			customerName = customerName.substring(1, customerName.length() - 1);
			var1.append("	and upper(NAME_TEXT) like upper(?) escape '^' ");
			param.add(customerName);
		}

		if (!StringUtil.isNullOrEmpty(daySeq)) {
				var1.append("   order by VISIT asc, DAY_SEQ asc, CUSTOMER_CODE asc, CUSTOMER_NAME asc ");
		} else {
			var1.append("   order by VISIT asc, SHORT_CODE asc, CUSTOMER_NAME asc ");
		}

		if (page > 0) {
			// get count
			if (isGetTotalPage) {
				totalPageSql.append("select count(*) as TOTAL_ROW from ("
						+ var1 + ")");
				try {
					cTotalRow = rawQueries(totalPageSql.toString(), param);
					if (cTotalRow != null) {
						if (cTotalRow.moveToFirst()) {
							dto.totalCustomer = cTotalRow.getInt(0);
						}
					}
				} catch (Exception ex) {
					VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
				} finally {
					try {
						if (cTotalRow != null) {
							cTotalRow.close();
						}
					} catch (Exception ex) {
					}
				}
			}
			var1.append(" limit "
					+ Integer.toString(Constants.NUM_ITEM_PER_PAGE));
			var1.append(" offset "
					+ Integer
							.toString((page - 1) * Constants.NUM_ITEM_PER_PAGE));
		}

		try {
			c = rawQueries(var1.toString(), param);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						CustomerListItem item = new CustomerListItem();
						if (type == 1){
							double distanceTTTT = distance;
							//TTTT thi phai lay them shop distance vi 1 KH co shop khac nhau
							if (c.getColumnIndex("SHOP_DISTANCE") > -1) {
								double disShop = c.getDouble(c.getColumnIndex("SHOP_DISTANCE"));
								if (distanceTTTT <= 0){
									//neu ko co cau hinh cua nv thi lay theo shop tuong ung voi KH
									distanceTTTT = disShop;
								}

								if(distanceTTTT <= 0){
									distanceTTTT = Constants.DEFAULT_DISTANCE_ORDER;
								}
							}

							item.initDataFromCursor(c, distanceTTTT, visitPlan);
						} else {
							item.initDataFromCursor(c, distance, visitPlan);
						}

						dto.cusList.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
			}
		}

		return dto;
	}

	/**
	 * Ds khach hangf
	 * 
	 * @author: TamPQ
	 * @param staffId
	 * @param customerName
	 * @param customerCode
	 * @param customerAddress
	 * @param visitPlan
	 * @param isGetWrongPlan
	 * @param page
	 * @param isGetTotalPage
	 * @return: CustomerListDTO
	 * @throws:
	 */
	public CustomerListDTO getTBHVListCustomer(long staffId, long shopId, String customerName, String customerCode,
			String customerAddress, String visitPlan, boolean isGetWrongPlan, int page, int isGetTotalPage)
			throws Exception {
		CustomerListDTO dto = null;
		Cursor c = null;
		String daySeq = "";
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);

		if (!StringUtil.isNullOrEmpty(visitPlan)) {
			if (visitPlan.equals(Constants.DAY_LINE[0])) {
				daySeq = "2";
			} else if (visitPlan.equals(Constants.DAY_LINE[1])) {
				daySeq = "3";
			} else if (visitPlan.equals(Constants.DAY_LINE[2])) {
				daySeq = "4";
			} else if (visitPlan.equals(Constants.DAY_LINE[3])) {
				daySeq = "5";
			} else if (visitPlan.equals(Constants.DAY_LINE[4])) {
				daySeq = "6";
			} else if (visitPlan.equals(Constants.DAY_LINE[5])) {
				daySeq = "7";
			} else if (visitPlan.equals(Constants.DAY_LINE[6])) {
				daySeq = "8";
			}
		}

		StringBuffer var1 = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();
		String dateNow=DateUtils.now();

		var1.append("SELECT * ");
		var1.append("FROM   (SELECT VP.VISIT_PLAN_ID               AS VISIT_PLAN_ID, ");
		var1.append("               VP.SHOP_ID                     AS SHOP_ID, ");
		var1.append("               VP.STAFF_ID                    AS STAFF_ID, ");
		var1.append("               VP.FROM_DATE                   AS FROM_DATE, ");
		var1.append("               VP.TO_DATE                     AS TO_DATE, ");
		var1.append("               RT.ROUTING_ID                  AS ROUTING_ID, ");
		var1.append("               RT.ROUTING_CODE                AS ROUTING_CODE, ");
		var1.append("               RT.ROUTING_NAME                AS ROUTING_NAME, ");
		var1.append("               CT.CUSTOMER_ID                 AS CUSTOMER_ID, ");
		var1.append("               SUBSTR(CT.CUSTOMER_CODE, 1, 3) AS CUSTOMER_CODE, ");
		var1.append("               CT.CUSTOMER_NAME               AS CUSTOMER_NAME, ");
		var1.append("               CT.NAME_TEXT                   AS NAME_TEXT, ");
		var1.append("               CT.MOBIPHONE                   AS MOBIPHONE, ");
		var1.append("               CT.PHONE                       AS PHONE, ");
		var1.append("               CT.LAT                         AS LAT, ");
		var1.append("               CT.LNG                         AS LNG, ");
		var1.append("               CT.CHANNEL_TYPE_ID             AS CHANNEL_TYPE_ID, ");
		var1.append("               CT.ADDRESS                     AS ADDRESS, ");
		var1.append("               ( CT.HOUSENUMBER ");
		var1.append("                 || ' ' ");
		var1.append("                 || CT.STREET )               AS STREET, ");
		var1.append("               ( CASE ");
		var1.append("                   WHEN RTC.MONDAY = 1 THEN 'T2' ");
		var1.append("                   ELSE '' ");
		var1.append("                 END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.TUESDAY = 1 THEN ',T3' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.WEDNESDAY = 1 THEN ',T4' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.THURSDAY = 1 THEN ',T5' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.FRIDAY = 1 THEN ',T6' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.SATURDAY = 1 THEN ',T7' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.SUNDAY = 1 THEN ',CN' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END )                     AS CUS_PLAN, ");
		var1.append("               ( CASE ");
		var1.append("                   WHEN RTC.SEQ2 = 0 THEN '' ");
		var1.append("                   ELSE RTC.SEQ2 ");
		var1.append("                 END ) ");
		var1.append("               || ( CASE ");
		var1.append("                      WHEN RTC.SEQ3 = 0 THEN '' ");
		var1.append("                      ELSE ',' ");
		var1.append("                           || RTC.SEQ3 ");
		var1.append("                    END ) ");
		var1.append("               || ( CASE ");
		var1.append("                      WHEN RTC.SEQ4 = 0 THEN '' ");
		var1.append("                      ELSE ',' ");
		var1.append("                           || RTC.SEQ4 ");
		var1.append("                    END ) ");
		var1.append("               || ( CASE ");
		var1.append("                      WHEN RTC.SEQ5 = 0 THEN '' ");
		var1.append("                      ELSE ',' ");
		var1.append("                           || RTC.SEQ5 ");
		var1.append("                    END ) ");
		var1.append("               || ( CASE ");
		var1.append("                      WHEN RTC.SEQ6 = 0 THEN '' ");
		var1.append("                      ELSE ',' ");
		var1.append("                           || RTC.SEQ6 ");
		var1.append("                    END ) ");
		var1.append("               || ( CASE ");
		var1.append("                      WHEN RTC.SEQ7 = 0 THEN '' ");
		var1.append("                      ELSE ',' ");
		var1.append("                           || RTC.SEQ7 ");
		var1.append("                    END ) ");
		var1.append("               || ( CASE ");
		var1.append("                      WHEN RTC.SEQ8 = 0 THEN '' ");
		var1.append("                      ELSE ',' ");
		var1.append("                           || RTC.SEQ8 ");
		var1.append("                    END )                     AS PLAN_SEQ, ");
		var1.append("               RTC.ROUTING_CUSTOMER_ID        AS ROUTING_CUSTOMER_ID ");

		if (!StringUtil.isNullOrEmpty(daySeq)) {
			var1.append("  			, RTC.SEQ" + daySeq + " as DAY_SEQ");
		}

		var1.append("        FROM   VISIT_PLAN VP, ");
		var1.append("               ROUTING RT, ");
		var1.append("               (SELECT ROUTING_CUSTOMER_ID, ");
		var1.append("                       ROUTING_ID, ");
		var1.append("                       CUSTOMER_ID, ");
		var1.append("                       STATUS, ");
		var1.append("                       MONDAY, ");
		var1.append("                       TUESDAY, ");
		var1.append("                       WEDNESDAY, ");
		var1.append("                       THURSDAY, ");
		var1.append("                       FRIDAY, ");
		var1.append("                       SATURDAY, ");
		var1.append("                       SUNDAY, ");
		var1.append("                       SEQ2, ");
		var1.append("                       SEQ3, ");
		var1.append("                       SEQ4, ");
		var1.append("                       SEQ5, ");
		var1.append("                       SEQ6, ");
		var1.append("                       SEQ7, ");
		var1.append("                       SEQ8 ");
		var1.append("                FROM   ROUTING_CUSTOMER ");
		
		var1.append("                WHERE (DATE(END_DATE) >= DATE(?) OR DATE(END_DATE) IS NULL) and ");
		param.add(dateNow);
		var1.append("                DATE(START_DATE) <= DATE(?) and ");
		param.add(dateNow);
		var1.append("                ( ");
		var1.append("                (WEEK1 IS NULL AND WEEK2 IS NULL AND WEEK3 IS NULL AND WEEK4 IS NULL) OR");
		var1.append("	(((cast((julianday(Date(?)) - julianday(start_date)) / 7  as integer) +");
		param.add(dateNow);
		var1.append("		   (case when ((julianday(Date(?)) - julianday(start_date)) % 7 > 0)");
		param.add(dateNow);
		var1.append("			and (");
		var1.append("		cast((case when strftime('%w', ?) = '0' ");
		param.add(dateNow);
		var1.append("									  then 7 ");
		var1.append("									  else strftime('%w', ?)                          ");
		param.add(dateNow);
		var1.append("									  end ) as integer) < ");
		var1.append("		 cast((case when strftime('%w', start_date) = '0' ");
		var1.append("										  then 7 ");
		var1.append("										  else strftime('%w', start_date)                          ");
		var1.append("										  end ) as integer) ) ");
		var1.append("			then 1 else 0 end)) % 4 + 1)=1 and week1=1) or");

		var1.append("	(((cast((julianday(Date(?)) - julianday(start_date)) / 7  as integer) +");
		param.add(dateNow);
		var1.append("		   (case when ((julianday(Date(?)) - julianday(start_date)) % 7 > 0)");
		param.add(dateNow);
		var1.append("			and (");
		var1.append("		cast((case when strftime('%w', ?) = '0' ");
		param.add(dateNow);
		var1.append("									  then 7 ");
		var1.append("									  else strftime('%w', ?)                          ");
		param.add(dateNow);
		var1.append("									  end ) as integer) < ");
		var1.append("		 cast((case when strftime('%w', start_date) = '0' ");
		var1.append("										  then 7 ");
		var1.append("										  else strftime('%w', start_date)                          ");
		var1.append("										  end ) as integer) ) ");
		var1.append("			then 1 else 0 end)) % 4 + 1)=2 and week2=1) or");

		var1.append("	(((cast((julianday(Date(?)) - julianday(start_date)) / 7  as integer) +");
		param.add(dateNow);
		var1.append("		   (case when ((julianday(Date(?)) - julianday(start_date)) % 7 > 0)");
		param.add(dateNow);
		var1.append("			and (");
		var1.append("		cast((case when strftime('%w', ?) = '0' ");
		param.add(dateNow);
		var1.append("									  then 7 ");
		var1.append("									  else strftime('%w', ?)                          ");
		param.add(dateNow);
		var1.append("									  end ) as integer) < ");
		var1.append("		 cast((case when strftime('%w', start_date) = '0' ");
		var1.append("										  then 7 ");
		var1.append("										  else strftime('%w', start_date)                          ");
		var1.append("										  end ) as integer) ) ");
		var1.append("			then 1 else 0 end)) % 4 + 1)=3 and week3=1) or");

		var1.append("	(((cast((julianday(Date(?)) - julianday(start_date)) / 7  as integer) +");
		param.add(dateNow);
		var1.append("		   (case when ((julianday(Date(?)) - julianday(start_date)) % 7 > 0)");
		param.add(dateNow);
		var1.append("			and (");
		var1.append("		cast((case when strftime('%w', ?) = '0' ");
		param.add(dateNow);
		var1.append("									  then 7 ");
		var1.append("									  else strftime('%w', ?)                          ");
		param.add(dateNow);
		var1.append("									  end ) as integer) < ");
		var1.append("		 cast((case when strftime('%w', start_date) = '0' ");
		var1.append("										  then 7 ");
		var1.append("										  else strftime('%w', start_date)                          ");
		var1.append("										  end ) as integer) ) ");
		var1.append("			then 1 else 0 end)) % 4 + 1)=4 and week4=1) )");

		var1.append("                ) RTC,  ");
		
		var1.append("               CUSTOMER CT ");
		var1.append("        WHERE  VP.ROUTING_ID = RT.ROUTING_ID ");
		var1.append("               AND RTC.ROUTING_ID = RT.ROUTING_ID ");
		var1.append("               AND RTC.CUSTOMER_ID = CT.CUSTOMER_ID ");
		var1.append("               AND DATE(VP.FROM_DATE) <= DATE(?) ");
		param.add(date_now);
		var1.append("               AND ( DATE(VP.TO_DATE) >= DATE(?) ");
		param.add(date_now);
		var1.append("                      OR DATE(VP.TO_DATE) IS NULL ) ");
		var1.append("               AND VP.STAFF_ID = ? ");
		param.add("" + staffId);
		var1.append("               AND RT.STATUS = 1 ");
		var1.append("               AND CT.STATUS = 1 ");
		var1.append("               AND RTC.STATUS = 1) CUS ");
		var1.append("       LEFT JOIN (SELECT STAFF_ID, ");
		var1.append("                         CUSTOMER_ID               AS CUSTOMER_ID_1, ");
		var1.append("                         GROUP_CONCAT(OBJECT_TYPE) AS ACTION ");
		var1.append("                  FROM   ACTION_LOG ");
		var1.append("                  WHERE  STAFF_ID = ? ");
		param.add("" + staffId);
		var1.append("                         AND DATE(START_TIME) = DATE(?) ");
		param.add(date_now);
		var1.append("                         AND OBJECT_TYPE IN ( 2, 3, 4 ) ");
		var1.append("                  GROUP  BY CUSTOMER_ID) ACTION ");
		var1.append("              ON CUS.CUSTOMER_ID = ACTION.CUSTOMER_ID_1 ");
		var1.append("       LEFT JOIN (SELECT STAFF_ID, ");
		var1.append("                         CUSTOMER_ID AS CUSTOMER_ID_2, ");
		var1.append("                         OBJECT_TYPE AS VISIT, ");
		var1.append("                         START_TIME, ");
		var1.append("                         END_TIME, ");
		var1.append("                         LAT         AS VISITED_LAT, ");
		var1.append("                         LNG         AS VISITED_LNG, ");
		var1.append("                         IS_OR ");
		var1.append("                  FROM   ACTION_LOG ");
		var1.append("                  WHERE  STAFF_ID = ? ");
		param.add("" + staffId);
		var1.append("                         AND DATE(START_TIME) = DATE(?) ");
		param.add(date_now);
		var1.append("                         AND OBJECT_TYPE IN ( 0, 1 ) ");
		var1.append("                  GROUP  BY CUSTOMER_ID) VISIT ");
		var1.append("              ON VISIT.CUSTOMER_ID_2 = CUS.CUSTOMER_ID ");
		var1.append("WHERE  1 = 1 ");
		var1.append("       AND ( UPPER(CUS_PLAN) LIKE UPPER(?) OR IS_OR IN (0,1) )");
		param.add("%" + visitPlan + "%");
		double distance = 0;
		try {
			c = rawQueries(var1.toString(), param);

			if (c != null) {
				if (dto == null) {
					dto = new CustomerListDTO();
					distance = getDistanceOrder(null, String.valueOf(shopId), String.valueOf(staffId));
				}
				if (c.moveToFirst()) {
					do {
						CustomerListItem item = new CustomerListItem();
						item.initDataFromCursor(c, distance, visitPlan);

						dto.cusList.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
			}
		}

		return dto;
	}

	/** 
	 * Lay danh sach khach hang cung so luong hinh anh
	 * 
	 * @author: QuangVT
	 * @param data
	 * @return
	 * @return: ImageListDTO
	 * @throws Exception
	 * @throws:
	 */
	public ImageListDTO getImageList(Bundle data) throws Exception { 
		ImageListDTO dto = null; 
		
		// Lay cac params tu bundle
		int page 		 = data.getInt(IntentConstants.INTENT_PAGE);
		long staffId 	 = data.getLong(IntentConstants.INTENT_STAFF_ID);
		//String cusCode 	 = data.getString(IntentConstants.INTENT_CUSTOMER_CODE);
		String cusName 	 = data.getString(IntentConstants.INTENT_CUSTOMER_NAME); 
		String day 		 = data.getString(IntentConstants.INTENT_VISIT_PLAN);
		String fromDate  = data.getString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE);
		String toDate 	 = data.getString(IntentConstants.INTENT_FIND_ORDER_TO_DATE);
		String shopId 	 = data.getString(IntentConstants.INTENT_SHOP_ID); 
		//boolean isSearch = data.getBoolean(IntentConstants.INTENT_IS_SEARCH);
		boolean isGetTotalPage 	= data.getBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE);
		String date_now  = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);  
		
		ArrayList<String> param = new ArrayList<String>(); 
		StringBuffer  getCusList = new StringBuffer();
		
		// Danh sach khach hang
		getCusList.append("SELECT CTT.CUSTOMER_CODE              AS CUSTOMER_CODE, ");
		getCusList.append("       CTT.CUSTOMER_ID                AS CUSTOMER_ID, ");
		getCusList.append("       CTT.CUSTOMER_NAME              AS CUSTOMER_NAME, ");
		getCusList.append("       CTt.ADDRESS                    AS ADDRESS, ");
		getCusList.append("       CTT.STREET                     AS STREET, ");
		getCusList.append("       CTT.LAT                        AS LAT, ");
		getCusList.append("       CTT.LNG                        AS LNG, ");
		getCusList.append("       CTT.HOUSENUMBER                AS HOUSENUMBER, ");
		getCusList.append("       CTT.VISIT_PLAN                 AS VISIT_PLAN, ");
		getCusList.append("       CTT.CUSTOMER_NAME_ADDRESS_TEXT AS CUSTOMER_NAME_ADDRESS_TEXT, ");
		getCusList.append("       CTT.STAFF_ID                   AS STAFF_ID, ");
		getCusList.append("       COUNT(MI.OBJECT_ID)            NUM_ITEM ");
		getCusList.append("FROM   (SELECT CT.CUSTOMER_CODE AS CUSTOMER_CODE, ");
		getCusList.append("               CT.CUSTOMER_ID   AS CUSTOMER_ID, ");
		getCusList.append("               CT.LAT           AS LAT, ");
		getCusList.append("               CT.LNG           AS LNG, ");
		getCusList.append("               CT.HOUSENUMBER   AS HOUSENUMBER, ");
		getCusList.append("               CT.CUSTOMER_NAME AS CUSTOMER_NAME, ");
		getCusList.append("               CT.STREET        AS STREET, ");
		getCusList.append("               CT.ADDRESS       AS ADDRESS, ");
		getCusList.append("               ( CASE ");
		getCusList.append("                   WHEN RTC.MONDAY = 1 THEN 'T2' ");
		getCusList.append("                   ELSE '' ");
		getCusList.append("                 END ) ");
		getCusList.append("               || ( CASE ");
		getCusList.append("                      WHEN RTC.TUESDAY = 1 THEN ',T3' ");
		getCusList.append("                      ELSE '' ");
		getCusList.append("                    END ) ");
		getCusList.append("               || ( CASE ");
		getCusList.append("                      WHEN RTC.WEDNESDAY = 1 THEN ',T4' ");
		getCusList.append("                      ELSE '' ");
		getCusList.append("                    END ) ");
		getCusList.append("               || ( CASE ");
		getCusList.append("                      WHEN RTC.THURSDAY = 1 THEN ',T5' ");
		getCusList.append("                      ELSE '' ");
		getCusList.append("                    END ) ");
		getCusList.append("               || ( CASE ");
		getCusList.append("                      WHEN RTC.FRIDAY = 1 THEN ',T6' ");
		getCusList.append("                      ELSE '' ");
		getCusList.append("                    END ) ");
		getCusList.append("               || ( CASE ");
		getCusList.append("                      WHEN RTC.SATURDAY = 1 THEN ',T7' ");
		getCusList.append("                      ELSE '' ");
		getCusList.append("                    END ) ");
		getCusList.append("               || ( CASE ");
		getCusList.append("                      WHEN RTC.SUNDAY = 1 THEN ',CN' ");
		getCusList.append("                      ELSE '' ");
		getCusList.append("                    END )       AS VISIT_PLAN, ");
		getCusList.append("               CT.NAME_TEXT     AS CUSTOMER_NAME_ADDRESS_TEXT, ");
		getCusList.append("               VP.STAFF_ID ");
		getCusList.append("        FROM   VISIT_PLAN VP, ");
		getCusList.append("               ROUTING RT, ");
		getCusList.append("               ROUTING_CUSTOMER RTC, ");
		getCusList.append("               CUSTOMER CT ");
		getCusList.append("        WHERE  VP.ROUTING_ID = RT.ROUTING_ID ");
		getCusList.append("               AND RT.ROUTING_ID = RTC.ROUTING_ID ");
		getCusList.append("               AND RTC.CUSTOMER_ID = CT.CUSTOMER_ID ");
		//getCusList.append("               AND VP.STATUS = 1 ");
		getCusList.append("               AND VP.STATUS in (0, 1) ");
		getCusList.append("               AND RT.STATUS = 1 ");
		//getCusList.append("               AND RTC.STATUS = 1 ");
		getCusList.append("               AND CT.STATUS = 1 ");
		
		// Kiem tra thoi gian Visit_Plan
		getCusList.append("               AND substr(VP.FROM_DATE,1,10) <= ? ");
		param.add(date_now);
		getCusList.append("               AND ( (substr(VP.TO_DATE,1,10) >= ?) ");
		getCusList.append("                      OR (substr(VP.TO_DATE,1,10) IS NULL )) ");
		param.add(date_now);
		
		// Kiem tra thoi gian Routing_Customer
		getCusList.append("               AND substr(RTC.START_DATE,1,10) <= ? ");
		param.add(date_now);
		getCusList.append("               AND ( (substr(RTC.END_DATE,1,10) >= ?) ");
		getCusList.append("                      OR (substr(RTC.END_DATE,1,10) IS NULL )) ");
		param.add(date_now);
		
		getCusList.append("               AND VP.STAFF_ID = ? ");
		param.add("" + staffId);
		 
		// Search theo ma KH
//		if (!StringUtil.isNullOrEmpty(cusCode)) {
//			cusCode = StringUtil.getEngStringFromUnicodeString(cusCode);
//			cusCode = StringUtil.escapeSqlString(cusCode);
//			cusCode = DatabaseUtils.sqlEscapeString("%" + cusCode + "%");
//			cusCode = cusCode.substring(1, cusCode.length() - 1);
//			getCusList.append("	and upper(substr(CT.CUSTOMER_CODE, 1, 3)) like upper(?) escape '^' ");
//			param.add(cusCode);
//		}
		
		// Search theo ten KH
		if (!StringUtil.isNullOrEmpty(cusName)) {
			cusName = StringUtil.getEngStringFromUnicodeString(cusName);
			cusName = StringUtil.escapeSqlString(cusName);
			cusName = DatabaseUtils.sqlEscapeString("%" + cusName + "%");
			cusName = cusName.substring(1, cusName.length() - 1);
			getCusList.append("	and ((upper(substr(CT.CUSTOMER_CODE, 1, 3)) like upper(?) escape '^' )");
			param.add(cusName);
			getCusList.append("	or (upper(CT.NAME_TEXT) like upper(?) escape '^') ) ");
			param.add(cusName);
		}

		// Search theo tuyen
		if (!StringUtil.isNullOrEmpty(day)) {
			getCusList.append("	and upper(VISIT_PLAN) like upper(?) ");
			param.add("%" + day + "%");
		} 
		getCusList.append("  group by CT.CUSTOMER_ID   ) CTT ");
		// Ket thuc SQL lay danh sach KH
		
		// Danh sach hinh anh
		getCusList.append("     LEFT JOIN (SELECT * ");
		getCusList.append("                  FROM   MEDIA_ITEM ");
		getCusList.append("                  WHERE  OBJECT_TYPE IN ( 0, 1, 2, 4 ) ");
		getCusList.append("                         AND STATUS = 1 ");
		getCusList.append("                         AND TYPE = 1 ");
		getCusList.append("                         AND DATE(CREATE_DATE) >= DATE(?, ");
		getCusList.append("                                                  'start of month', ");
		getCusList.append("                                                  '-2 month') ");
		param.add(date_now);
		getCusList.append("                         AND MEDIA_TYPE = 0 ");
		getCusList.append("                         AND SHOP_ID = ? ");
		param.add(shopId); 
		// Search danh sach hinh anh trong 1 khoang thoi gian
//		if(isSearch){
		if (!StringUtil.isNullOrEmpty(fromDate)) {
			getCusList.append(" and substr(create_date,1,10) >= ? ");
			param.add(fromDate);
		}
		
		if (!StringUtil.isNullOrEmpty(toDate)) {
			getCusList.append(" and substr(create_date,1,10) <= ? ");
			param.add(toDate);
		} 
//		}
		getCusList.append("                          ) MI ");
		getCusList.append("              ON CTT.CUSTOMER_ID = MI.OBJECT_ID ");
		getCusList.append("WHERE  1 = 1 "); 
		
		
		getCusList.append(" GROUP BY CTT.CUSTOMER_ID"); 
		
//		if(isSearch){
//			getCusList.append(" HAVING NUM_ITEM > 0 ");
//		}

		getCusList.append(" order by CUSTOMER_CODE asc, CUSTOMER_NAME asc ");

		// Phan trang
		String getTotalCount = "";
		if (page > 0) {
			if (isGetTotalPage) {  
				getTotalCount = " select count(*) as TOTAL_ROW from (" + getCusList + ")";
			}

			getCusList.append(" limit " + Integer.toString(Constants.NUM_ITEM_PER_PAGE));
			getCusList.append(" offset " + Integer.toString((page - 1) * Constants.NUM_ITEM_PER_PAGE));
		}

		// Query database
		Cursor c = null;
		try {
			c = rawQueries(getCusList.toString(), param);

			if (c != null) {
				dto = new ImageListDTO();
				if (c.moveToFirst()) {
					do {
						ImageListItemDTO item = new ImageListItemDTO();
						item.imageListItemDTO(c);
						dto.listItem.add(item);
					} while (c.moveToNext());
				}
			}
		}catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally { 
			// Close cursor lay danh sach
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				VTLog.e("Quang", e.getMessage());
			}
		}
		
		Cursor cTotalRow = null;
		try{
			if (isGetTotalPage) {
				cTotalRow = rawQueries(getTotalCount, param);
				if (cTotalRow != null) {
					if (cTotalRow.moveToFirst()) {
						dto.totalCustomer = cTotalRow.getInt(0);
					}
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally { 	
			// Close cursor lay so luong dong
			try {
				if (cTotalRow != null) {
					cTotalRow.close();
				}
			} catch (Exception e) {
				VTLog.e("Quang", e.getMessage());
			}
		}
		
		return dto;
	}

	/**
	 *
	 * 05-01 get list image GSBH
	 *
	 * @author: trungnt56
	 * @param data
	 * @return
	 * @return: ImageListDTO
	 * @throws Exception
	 * @throws:
	 */
	public ImageListDTO getGSBHImageList(Bundle data) throws Exception {
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);
		ImageListDTO dto = null;
		int page = data.getInt(IntentConstants.INTENT_PAGE);
		boolean isGetTotalPage = data.getBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE);
		String staffOwnerId  = data.getString(IntentConstants.INTENT_STAFF_OWNER_ID);
		String cusCode = data.getString(IntentConstants.INTENT_CUSTOMER_CODE);
		String cusName = data.getString(IntentConstants.INTENT_CUSTOMER_NAME);
		long staffId    = data.getLong(IntentConstants.INTENT_STAFF_ID);
		String day     = data.getString(IntentConstants.INTENT_VISIT_PLAN);
		String fromDate  = data.getString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE);
		String toDate    = data.getString(IntentConstants.INTENT_FIND_ORDER_TO_DATE);
		//Boolean isSearch = data.getBoolean(IntentConstants.INTENT_IS_SEARCH);
		int numItemInPage = data.getInt(IntentConstants.INTENT_NUM_ITEM_IN_PAGE);

		STAFF_TABLE staff_table = new STAFF_TABLE(mDB);
		ArrayList<String> strings = new ArrayList<>();
		if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSTINH){
			strings =  staff_table.getStaffGroupTBHV(String.valueOf(staffOwnerId));
		}else{
			strings = staff_table.getStaffRecursiveReverseTBHV(String.valueOf(staffOwnerId));
		}
		StringBuffer lstStaffId = new StringBuffer();
		for (String s:
				strings) {
			if(lstStaffId.length() > 0) {
				lstStaffId.append(",");
			}
			lstStaffId.append(s);
		}

		ArrayList<String> param = new ArrayList<String>();
		StringBuffer  getCusList = new StringBuffer();
		getCusList.append("select * ");
		getCusList.append(" from (SELECT CTT.CUSTOMER_CODE              AS CUSTOMER_CODE, ");
		getCusList.append("       CTT.CUSTOMER_ID                AS CUSTOMER_ID, ");
		getCusList.append("       CTT.CUSTOMER_NAME              AS CUSTOMER_NAME, ");
		getCusList.append("       CTT.STREET                     AS STREET, ");
		getCusList.append("       CTT.ADDRESS                    AS ADDRESS, ");
		getCusList.append("       CTT.LAT                        AS LAT, ");
		getCusList.append("       CTT.LNG                        AS LNG, ");
		getCusList.append("       CTT.HOUSENUMBER                AS HOUSENUMBER, ");
		getCusList.append("       CTT.VISIT_PLAN                 AS VISIT_PLAN, ");
		getCusList.append("       CTT.CUSTOMER_NAME_ADDRESS_TEXT AS CUSTOMER_NAME_ADDRESS_TEXT, ");
		getCusList.append("       CTT.STAFF_ID                   AS STAFF_ID, ");
		getCusList.append("       CTT.STAFF_NAME                 AS NVBH_STAFF_NAME, ");
		getCusList.append("       COUNT(MI.OBJECT_ID)            NUM_ITEM ");
		getCusList.append("FROM   (SELECT CT.CUSTOMER_CODE AS CUSTOMER_CODE, ");
		getCusList.append("               CT.CUSTOMER_ID   AS CUSTOMER_ID, ");
		getCusList.append("               CT.LAT           AS LAT, ");
		getCusList.append("               CT.LNG           AS LNG, ");
		getCusList.append("               CT.HOUSENUMBER   AS HOUSENUMBER, ");
		getCusList.append("               CT.CUSTOMER_NAME AS CUSTOMER_NAME, ");
		getCusList.append("               CT.STREET        AS STREET, ");
		getCusList.append("               CT.ADDRESS        AS ADDRESS, ");
		getCusList.append("               ( CASE ");
		getCusList.append("                   WHEN RTC.MONDAY = 1 THEN 'T2' ");
		getCusList.append("                   ELSE '' ");
		getCusList.append("                 END ) ");
		getCusList.append("               || ( CASE ");
		getCusList.append("                      WHEN RTC.TUESDAY = 1 THEN ',T3' ");
		getCusList.append("                      ELSE '' ");
		getCusList.append("                    END ) ");
		getCusList.append("               || ( CASE ");
		getCusList.append("                      WHEN RTC.WEDNESDAY = 1 THEN ',T4' ");
		getCusList.append("                      ELSE '' ");
		getCusList.append("                    END ) ");
		getCusList.append("               || ( CASE ");
		getCusList.append("                      WHEN RTC.THURSDAY = 1 THEN ',T5' ");
		getCusList.append("                      ELSE '' ");
		getCusList.append("                    END ) ");
		getCusList.append("               || ( CASE ");
		getCusList.append("                      WHEN RTC.FRIDAY = 1 THEN ',T6' ");
		getCusList.append("                      ELSE '' ");
		getCusList.append("                    END ) ");
		getCusList.append("               || ( CASE ");
		getCusList.append("                      WHEN RTC.SATURDAY = 1 THEN ',T7' ");
		getCusList.append("                      ELSE '' ");
		getCusList.append("                    END ) ");
		getCusList.append("               || ( CASE ");
		getCusList.append("                      WHEN RTC.SUNDAY = 1 THEN ',CN' ");
		getCusList.append("                      ELSE '' ");
		getCusList.append("                    END )       AS VISIT_PLAN, ");
		getCusList.append("               CT.NAME_TEXT     AS CUSTOMER_NAME_ADDRESS_TEXT, ");
		getCusList.append("               VP.STAFF_ID, ");
		getCusList.append("               STAFF.STAFF_NAME ");
		getCusList.append("        FROM   VISIT_PLAN VP, ");
		getCusList.append("               ROUTING RT, ");
		getCusList.append("               ROUTING_CUSTOMER RTC, ");
		getCusList.append("               CUSTOMER CT, ");
		getCusList.append("               STAFF ");
		getCusList.append("        WHERE  VP.ROUTING_ID = RT.ROUTING_ID ");
		getCusList.append("               AND RT.ROUTING_ID = RTC.ROUTING_ID ");
		getCusList.append("               AND RTC.CUSTOMER_ID = CT.CUSTOMER_ID ");
		getCusList.append("               AND RT.STATUS = 1 ");
		getCusList.append("               AND VP.STATUS in (0, 1) ");
		//getCusList.append("               AND VP.STATUS = 1 ");
		//getCusList.append("               AND RTC.STATUS = 1 ");
		getCusList.append("               AND CT.STATUS = 1 ");

		// Kiem tra thoi gian Visit_Plan
		getCusList.append("               AND substr(VP.FROM_DATE,1,10) <= ? ");
		param.add(date_now);
		getCusList.append("               AND ( (substr(VP.TO_DATE,1,10) >= ?) ");
		getCusList.append("                      OR (substr(VP.TO_DATE,1,10) IS NULL )) ");
		param.add(date_now);

		// Kiem tra thoi gian Routing_Customer
		getCusList.append("               AND substr(RTC.START_DATE,1,10) <= ? ");
		param.add(date_now);
		getCusList.append("               AND ( (substr(RTC.END_DATE,1,10) >= ?) ");
		getCusList.append("                      OR (substr(RTC.END_DATE,1,10) IS NULL )) ");
		param.add(date_now);
		getCusList.append("               AND VP.STAFF_ID = STAFF.STAFF_ID ");
		getCusList.append("               AND STAFF.STATUS = 1 ");
		getCusList.append("               AND STAFF.STAFF_ID in (");
		getCusList.append(lstStaffId);
		getCusList.append(") ");


		// Nhan vien ban hang
		if (staffId > 0) {
			getCusList.append(" and vp.STAFF_ID = ?");
			param.add("" + staffId);
		}

		// Search theo Ma khach hang
		if (!StringUtil.isNullOrEmpty(cusCode)) {
			getCusList.append("	and upper(substr(CT.CUSTOMER_CODE, 1, 3)) like upper(?) ");
			cusCode = StringUtil.toOracleSearchText(cusCode, false).trim();
			if ("%%".equals(cusCode)) {
				param.add(cusCode);
			} else {
				param.add("%" + cusCode + "%");
			}

		}

		// Search theo Ten Khach hang
		if (!StringUtil.isNullOrEmpty(cusName)) {
			getCusList.append("	and upper(CT.NAME_TEXT) like upper(?) ");
			cusName = StringUtil.toOracleSearchText(cusName, false).trim();
			if ("%%".equals(cusName)) {
				param.add(cusName);
			} else {
				param.add("%" + cusName + "%");
			}

		}

		// Search theo tuyen
		if (!StringUtil.isNullOrEmpty(day)) {
			getCusList.append("	and upper(VISIT_PLAN) like upper(?) ");
			param.add("%" + day + "%");
		}

		getCusList.append(" ) CTT     LEFT JOIN (SELECT * ");
		getCusList.append("                  FROM   MEDIA_ITEM ");
		getCusList.append("                  WHERE  OBJECT_TYPE IN ( 0, 1, 2, 4 ) ");
		getCusList.append("                         AND STATUS = 1 ");
		getCusList.append("                         AND TYPE = 1 ");
		getCusList.append("                         AND DATE(CREATE_DATE) >= DATE(?, ");
		getCusList.append("                                                  'start of month', ");
		getCusList.append("                                                  '-2 month') ");
		param.add(date_now);
		getCusList.append("                         AND MEDIA_TYPE = 0 ");
		getCusList.append("                         AND STAFF_ID in (");
		getCusList.append(lstStaffId);
		getCusList.append(") ");

		// Ngay dau tien
		if (!StringUtil.isNullOrEmpty(fromDate)) {
			getCusList.append(" and substr(create_date,1,10) >= ? ");
			param.add(fromDate);
		}

		// Ngay cuoi cung
		if (!StringUtil.isNullOrEmpty(toDate)) {
			getCusList.append(" and substr(create_date,1,10) <= ? ");
			param.add(toDate);
		}

		getCusList.append(") MI ");
		getCusList.append(" ON CTT.CUSTOMER_ID = MI.object_id ");

		getCusList.append(" where 1 = 1 ");

		getCusList.append(" GROUP BY CTT.CUSTOMER_ID, CTT.STAFF_ID ");
		getCusList.append(" union all ");
		getCusList.append(" SELECT CTT.CUSTOMER_CODE              AS CUSTOMER_CODE, ");
		getCusList.append("       CTT.CUSTOMER_ID                AS CUSTOMER_ID, ");
		getCusList.append("       CTT.CUSTOMER_NAME              AS CUSTOMER_NAME, ");
		getCusList.append("       CTT.STREET                     AS STREET, ");
		getCusList.append("       CTT.ADDRESS                    AS ADDRESS, ");
		getCusList.append("       CTT.LAT                        AS LAT, ");
		getCusList.append("       CTT.LNG                        AS LNG, ");
		getCusList.append("       CTT.HOUSENUMBER                AS HOUSENUMBER, ");
		getCusList.append("       null                 AS VISIT_PLAN, ");
		getCusList.append("       CTT.CUSTOMER_NAME_ADDRESS_TEXT AS CUSTOMER_NAME_ADDRESS_TEXT, ");
		getCusList.append("       CTT.STAFF_ID                   AS STAFF_ID, ");
		getCusList.append("       CTT.STAFF_NAME                 AS NVBH_STAFF_NAME, ");
		getCusList.append("       COUNT(MI.OBJECT_ID)            NUM_ITEM ");
		getCusList.append("FROM   (SELECT c.CUSTOMER_CODE AS CUSTOMER_CODE, ");
		getCusList.append("               c.CUSTOMER_ID   AS CUSTOMER_ID, ");
		getCusList.append("               c.LAT           AS LAT, ");
		getCusList.append("               c.LNG           AS LNG, ");
		getCusList.append("               c.HOUSENUMBER   AS HOUSENUMBER, ");
		getCusList.append("               c.CUSTOMER_NAME AS CUSTOMER_NAME, ");
		getCusList.append("               c.STREET        AS STREET, ");
		getCusList.append("               c.ADDRESS        AS ADDRESS, ");
		getCusList.append("               c.NAME_TEXT     AS CUSTOMER_NAME_ADDRESS_TEXT, ");
		getCusList.append("               s.STAFF_ID, ");
		getCusList.append("               s.STAFF_NAME ");
		getCusList.append("        FROM   pg_visit_plan pvp, ");
		getCusList.append("               customer c, ");
		getCusList.append("               staff s ");
		getCusList.append("        WHERE  pvp.customer_id = c.customer_id ");
		getCusList.append("               and pvp.staff_id = s.staff_id ");
		getCusList.append("               and pvp.status in (0,1) ");
		getCusList.append("               and c.status = 1 ");
		getCusList.append("               and s.status = 1 ");

		getCusList.append("               and substr(pvp.from_date,1,10) <= ? ");
		param.add(date_now);
		getCusList.append("               and (pvp.to_date is null ");
		getCusList.append("                      OR substr(pvp.to_date,1,10) >= ?) ");
		param.add(date_now);

		getCusList.append("               and s.staff_owner_id = ? ");
		param.add(staffOwnerId);

		// Nhan vien ban hang
		if (staffId > 0) {
			getCusList.append(" and pvp.staff_id = ?");
			param.add("" + staffId);
		}

		// Search theo Ma khach hang
		if (!StringUtil.isNullOrEmpty(cusCode)) {
			getCusList.append("	and upper(substr(c.customer_code, 1, 3)) like upper(?) ");
			cusCode = StringUtil.toOracleSearchText(cusCode, false).trim();
			if ("%%".equals(cusCode)) {
				param.add(cusCode);
			} else {
				param.add("%" + cusCode + "%");
			}

		}

		// Search theo Ten Khach hang
		if (!StringUtil.isNullOrEmpty(cusName)) {
			getCusList.append("	and upper(c.name_text) like upper(?) ");
			cusName = StringUtil.toOracleSearchText(cusName, false).trim();
			if ("%%".equals(cusName)) {
				param.add(cusName);
			} else {
				param.add("%" + cusName + "%");
			}

		}

		getCusList.append(" ) CTT     LEFT JOIN (SELECT * ");
		getCusList.append("                  FROM   MEDIA_ITEM ");
		getCusList.append("                  WHERE  OBJECT_TYPE IN ( 0, 1, 2, 4 ) ");
		getCusList.append("                         AND STATUS = 1 ");
		getCusList.append("                         AND TYPE = 1 ");
		getCusList.append("                         AND DATE(CREATE_DATE) >= DATE(?, ");
		getCusList.append("                                                  'start of month', ");
		getCusList.append("                                                  '-2 month') ");
		param.add(date_now);
		getCusList.append("                         AND MEDIA_TYPE = 0 ");
		getCusList.append("                         AND STAFF_ID in (");
		getCusList.append(lstStaffId);
		getCusList.append(") ");

		// Ngay dau tien
		if (!StringUtil.isNullOrEmpty(fromDate)) {
			getCusList.append(" and substr(create_date,1,10) >= ? ");
			param.add(fromDate);
		}

		// Ngay cuoi cung
		if (!StringUtil.isNullOrEmpty(toDate)) {
			getCusList.append(" and substr(create_date,1,10) <= ? ");
			param.add(toDate);
		}

		getCusList.append(") MI ");
		getCusList.append(" ON CTT.CUSTOMER_ID = MI.object_id ");

		getCusList.append(" where 1 = 1 ");

		getCusList.append(" GROUP BY CTT.CUSTOMER_ID, CTT.STAFF_ID ");

		getCusList.append(" ) ");

//		if (isSearch) {
//			getCusList.append(" HAVING NUM_ITEM > 0 ");
//		}

		String getTotalCount = "";
		if (page > 0) {
			if (isGetTotalPage) {
				getTotalCount = " select count(*) as TOTAL_ROW from (" + getCusList + ")";
			}
		}

		getCusList.append(" order by CUSTOMER_CODE asc, CUSTOMER_NAME asc ");

		if (page > 0) {
			getCusList.append(" limit " + Integer.toString(numItemInPage));
			getCusList.append(" offset " + Integer.toString((page - 1) * numItemInPage));
		}
		Cursor c = null;
		try {
			c = rawQueries(getCusList.toString(), param);

			if (c != null) {
				dto = new ImageListDTO();
				if (c.moveToFirst()) {
					do {
						ImageListItemDTO item = new ImageListItemDTO();
						item.imageListItemDTO(c);
						dto.listItem.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("[Quang]", e.getMessage());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
				VTLog.e("[Quang]", ex.getMessage());
			}
		}
		Cursor cTotalRow = null;
		try{
			if (isGetTotalPage) {
				cTotalRow = rawQueries(getTotalCount, param);
				if (cTotalRow != null) {
					if (cTotalRow.moveToFirst()) {
						dto.totalCustomer = cTotalRow.getInt(0);
					}
				}
			}
		} catch (Exception e) {
			VTLog.e("[Quang]", e.getMessage());
		} finally {
			try {
				if (cTotalRow != null) {
					cTotalRow.close();
				}
			} catch (Exception ex) {
				VTLog.e("[Quang]", ex.getMessage());
			}
		}
		return dto;
	}

	/**
	 * 
	 * 07-02 get list image GSNPP
	 * 
	 * @author: HoanPD1
	 * @param data
	 * @return
	 * @return: ImageListDTO
	 * @throws Exception
	 * @throws:
	 */
	public ImageListDTO getSupervisorImageList(Bundle data) throws Exception {
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);
		ImageListDTO dto = null;
		int page = data.getInt(IntentConstants.INTENT_PAGE);
		boolean isGetTotalPage = data.getBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE);
		String shopId  = data.getString(IntentConstants.INTENT_SHOP_ID);
		String cusCode = data.getString(IntentConstants.INTENT_CUSTOMER_CODE);
		String cusName = data.getString(IntentConstants.INTENT_CUSTOMER_NAME);
		long staffId    = data.getLong(IntentConstants.INTENT_STAFF_ID);
		String day     = data.getString(IntentConstants.INTENT_VISIT_PLAN);
		String fromDate  = data.getString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE);
		String toDate    = data.getString(IntentConstants.INTENT_FIND_ORDER_TO_DATE);
		//Boolean isSearch = data.getBoolean(IntentConstants.INTENT_IS_SEARCH);
		int numItemInPage = data.getInt(IntentConstants.INTENT_NUM_ITEM_IN_PAGE);

		ArrayList<String> param = new ArrayList<String>(); 
		StringBuffer  getCusList = new StringBuffer();
		getCusList.append("SELECT CTT.CUSTOMER_CODE              AS CUSTOMER_CODE, ");
		getCusList.append("       CTT.CUSTOMER_ID                AS CUSTOMER_ID, ");
		getCusList.append("       CTT.CUSTOMER_NAME              AS CUSTOMER_NAME, ");
		getCusList.append("       CTT.STREET                     AS STREET, ");
		getCusList.append("       CTT.ADDRESS                    AS ADDRESS, ");
		getCusList.append("       CTT.LAT                        AS LAT, ");
		getCusList.append("       CTT.LNG                        AS LNG, ");
		getCusList.append("       CTT.HOUSENUMBER                AS HOUSENUMBER, ");
		getCusList.append("       CTT.VISIT_PLAN                 AS VISIT_PLAN, ");
		getCusList.append("       CTT.CUSTOMER_NAME_ADDRESS_TEXT AS CUSTOMER_NAME_ADDRESS_TEXT, ");
		getCusList.append("       CTT.STAFF_ID                   AS STAFF_ID, ");
		getCusList.append("       CTT.STAFF_NAME                 AS NVBH_STAFF_NAME, ");
		getCusList.append("       COUNT(MI.OBJECT_ID)            NUM_ITEM ");
		getCusList.append("FROM   (SELECT CT.CUSTOMER_CODE AS CUSTOMER_CODE, ");
		getCusList.append("               CT.CUSTOMER_ID   AS CUSTOMER_ID, ");
		getCusList.append("               CT.LAT           AS LAT, ");
		getCusList.append("               CT.LNG           AS LNG, ");
		getCusList.append("               CT.HOUSENUMBER   AS HOUSENUMBER, ");
		getCusList.append("               CT.CUSTOMER_NAME AS CUSTOMER_NAME, ");
		getCusList.append("               CT.STREET        AS STREET, ");
		getCusList.append("               CT.ADDRESS        AS ADDRESS, ");
		getCusList.append("               ( CASE ");
		getCusList.append("                   WHEN RTC.MONDAY = 1 THEN 'T2' ");
		getCusList.append("                   ELSE '' ");
		getCusList.append("                 END ) ");
		getCusList.append("               || ( CASE ");
		getCusList.append("                      WHEN RTC.TUESDAY = 1 THEN ',T3' ");
		getCusList.append("                      ELSE '' ");
		getCusList.append("                    END ) ");
		getCusList.append("               || ( CASE ");
		getCusList.append("                      WHEN RTC.WEDNESDAY = 1 THEN ',T4' ");
		getCusList.append("                      ELSE '' ");
		getCusList.append("                    END ) ");
		getCusList.append("               || ( CASE ");
		getCusList.append("                      WHEN RTC.THURSDAY = 1 THEN ',T5' ");
		getCusList.append("                      ELSE '' ");
		getCusList.append("                    END ) ");
		getCusList.append("               || ( CASE ");
		getCusList.append("                      WHEN RTC.FRIDAY = 1 THEN ',T6' ");
		getCusList.append("                      ELSE '' ");
		getCusList.append("                    END ) ");
		getCusList.append("               || ( CASE ");
		getCusList.append("                      WHEN RTC.SATURDAY = 1 THEN ',T7' ");
		getCusList.append("                      ELSE '' ");
		getCusList.append("                    END ) ");
		getCusList.append("               || ( CASE ");
		getCusList.append("                      WHEN RTC.SUNDAY = 1 THEN ',CN' ");
		getCusList.append("                      ELSE '' ");
		getCusList.append("                    END )       AS VISIT_PLAN, ");
		getCusList.append("               CT.NAME_TEXT     AS CUSTOMER_NAME_ADDRESS_TEXT, ");
		getCusList.append("               VP.STAFF_ID, ");
		getCusList.append("               STAFF.STAFF_NAME ");
		getCusList.append("        FROM   VISIT_PLAN VP, ");
		getCusList.append("               ROUTING RT, ");
		getCusList.append("               ROUTING_CUSTOMER RTC, ");
		getCusList.append("               CUSTOMER CT, ");
		getCusList.append("               STAFF ");
		getCusList.append("        WHERE  VP.ROUTING_ID = RT.ROUTING_ID ");
		getCusList.append("               AND RT.ROUTING_ID = RTC.ROUTING_ID ");
		getCusList.append("               AND RTC.CUSTOMER_ID = CT.CUSTOMER_ID ");
		getCusList.append("               AND RT.STATUS = 1 ");
		getCusList.append("               AND VP.STATUS in (0, 1) ");
		//getCusList.append("               AND VP.STATUS = 1 ");
		//getCusList.append("               AND RTC.STATUS = 1 ");
		getCusList.append("               AND CT.STATUS = 1 ");
		
		// Kiem tra thoi gian Visit_Plan
		getCusList.append("               AND substr(VP.FROM_DATE,1,10) <= ? ");
		param.add(date_now);
		getCusList.append("               AND ( (substr(VP.TO_DATE,1,10) >= ?) ");
		getCusList.append("                      OR (substr(VP.TO_DATE,1,10) IS NULL )) ");
		param.add(date_now);
				
		// Kiem tra thoi gian Routing_Customer
		getCusList.append("               AND substr(RTC.START_DATE,1,10) <= ? ");
		param.add(date_now);
		getCusList.append("               AND ( (substr(RTC.END_DATE,1,10) >= ?) ");
		getCusList.append("                      OR (substr(RTC.END_DATE,1,10) IS NULL )) ");
		param.add(date_now); 
		getCusList.append("               AND VP.STAFF_ID = STAFF.STAFF_ID ");
		getCusList.append("               AND STAFF.STATUS = 1 ");
		getCusList.append("               AND CT.SHOP_ID = ? ");
		param.add(shopId); 
		 

		// Nhan vien ban hang
		if (staffId > 0) {
			getCusList.append(" and vp.STAFF_ID = ?");
			param.add("" + staffId);
		}
		
		// Search theo Ma khach hang
		if (!StringUtil.isNullOrEmpty(cusCode)) {
			getCusList.append("	and upper(substr(CT.CUSTOMER_CODE, 1, 3)) like upper(?) ");
			cusCode = StringUtil.toOracleSearchText(cusCode, false).trim();
			if ("%%".equals(cusCode)) {
				param.add(cusCode);
			} else {
				param.add("%" + cusCode + "%");
			}

		}
		
		// Search theo Ten Khach hang
		if (!StringUtil.isNullOrEmpty(cusName)) {
			getCusList.append("	and upper(CT.NAME_TEXT) like upper(?) ");
			cusName = StringUtil.toOracleSearchText(cusName, false).trim();
			if ("%%".equals(cusName)) {
				param.add(cusName);
			} else {
				param.add("%" + cusName + "%");
			}

		}

		// Search theo tuyen
		if (!StringUtil.isNullOrEmpty(day)) {
			getCusList.append("	and upper(VISIT_PLAN) like upper(?) ");
			param.add("%" + day + "%");
		} 

		getCusList.append(" ) CTT     LEFT JOIN (SELECT * ");
		getCusList.append("                  FROM   MEDIA_ITEM ");
		getCusList.append("                  WHERE  OBJECT_TYPE IN ( 0, 1, 2, 4 ) ");
		getCusList.append("                         AND STATUS = 1 ");
		getCusList.append("                         AND TYPE = 1 ");
		getCusList.append("                         AND DATE(CREATE_DATE) >= DATE(?, ");
		getCusList.append("                                                  'start of month', ");
		getCusList.append("                                                  '-2 month') ");
		param.add(date_now);
		getCusList.append("                         AND MEDIA_TYPE = 0 ");
		getCusList.append("                         AND SHOP_ID = ? ");
		param.add(shopId);

		// Ngay dau tien
		if (!StringUtil.isNullOrEmpty(fromDate)) {
			getCusList.append(" and substr(create_date,1,10) >= ? ");
			param.add(fromDate);
		}

		// Ngay cuoi cung
		if (!StringUtil.isNullOrEmpty(toDate)) {
			getCusList.append(" and substr(create_date,1,10) <= ? ");
			param.add(toDate);
		}

		getCusList.append(") MI ");
		getCusList.append(" ON CTT.CUSTOMER_ID = MI.object_id ");

		getCusList.append(" where 1 = 1 "); 
		 
		getCusList.append(" GROUP BY CTT.CUSTOMER_ID, CTT.STAFF_ID ");

//		if (isSearch) {
//			getCusList.append(" HAVING NUM_ITEM > 0 ");
//		}

		String getTotalCount = "";
		if (page > 0) {
			if (isGetTotalPage) {
				getTotalCount = " select count(*) as TOTAL_ROW from (" + getCusList + ")";
			}
		}

		getCusList.append(" order by CUSTOMER_CODE asc, CUSTOMER_NAME asc ");

		if (page > 0) {
			getCusList.append(" limit " + Integer.toString(numItemInPage));
			getCusList.append(" offset " + Integer.toString((page - 1) * numItemInPage));
		}
		Cursor c = null;
		try {
			c = rawQueries(getCusList.toString(), param);

			if (c != null) {
				dto = new ImageListDTO();
				if (c.moveToFirst()) {
					do {
						ImageListItemDTO item = new ImageListItemDTO();
						item.imageListItemDTO(c);
						dto.listItem.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("[Quang]", e.getMessage());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
				VTLog.e("[Quang]", ex.getMessage());
			}
		}
		Cursor cTotalRow = null;
		try{
			if (isGetTotalPage) {
				cTotalRow = rawQueries(getTotalCount, param);
				if (cTotalRow != null) {
					if (cTotalRow.moveToFirst()) {
						dto.totalCustomer = cTotalRow.getInt(0);
					}
				}
			}
		} catch (Exception e) {
			VTLog.e("[Quang]", e.getMessage());
		} finally {
			try {
				if (cTotalRow != null) {
					cTotalRow.close();
				}
			} catch (Exception ex) {
				VTLog.e("[Quang]", ex.getMessage());
			}
		}
		return dto;
	}

	public WrongPlanCustomerDTO getWrongCustomerList(long staffId) throws Exception {
		WrongPlanCustomerDTO dto = null;
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);
		ArrayList<String> param = new ArrayList<String>();

		StringBuffer var1 = new StringBuffer();
		var1.append("SELECT * ");
		var1.append("FROM   (SELECT CT.CUSTOMER_ID                 AS CUSTOMER_ID, ");
		//var1.append("               (substr(ct.customer_code, 1, 3) || ' - ' || ct.customer_name)  as CUSTOMER_CODE_NAME, ");
		var1.append("               (ct.short_code || ' - ' || ct.customer_name)  as CUSTOMER_CODE_NAME, ");
		var1.append("               CT.CUSTOMER_NAME               AS CUSTOMER_NAME, ");
		var1.append("               CT.ADDRESS                     AS ADDRESS, ");
		var1.append("               ADDRESS               		   AS STREET, ");
		var1.append("               ( CASE ");
		var1.append("                   WHEN RTC.MONDAY = 1 THEN 'T2' ");
		var1.append("                   ELSE '' ");
		var1.append("                 END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.TUESDAY = 1 THEN ',T3' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.WEDNESDAY = 1 THEN ',T4' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.THURSDAY = 1 THEN ',T5' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.FRIDAY = 1 THEN ',T6' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.SATURDAY = 1 THEN ',T7' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.SUNDAY = 1 THEN ',CN' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END )                     AS WEEK_PLAN ");
		var1.append("        FROM   VISIT_PLAN VP, ");
		var1.append("               ROUTING RT, ");
		var1.append("               (SELECT ROUTING_CUSTOMER_ID, ");
		var1.append("                       ROUTING_ID, ");
		var1.append("                       CUSTOMER_ID, ");
		var1.append("                       MONDAY, ");
		var1.append("                       TUESDAY, ");
		var1.append("                       WEDNESDAY, ");
		var1.append("                       THURSDAY, ");
		var1.append("                       FRIDAY, ");
		var1.append("                       SATURDAY, ");
		var1.append("                       SUNDAY, ");
		var1.append("                       SEQ2, ");
		var1.append("                       SEQ3, ");
		var1.append("                       SEQ4, ");
		var1.append("                       SEQ5, ");
		var1.append("                       SEQ6, ");
		var1.append("                       SEQ7, ");
		var1.append("                       SEQ8 ");
		var1.append("                FROM   ROUTING_CUSTOMER ");
		var1.append("                WHERE 1=1  ");
		var1.append("               AND DATE(START_DATE) <= DATE(?) ");
		param.add(date_now);
		var1.append("               AND IFNULL( DATE(END_DATE) >= DATE(?), 1) ");
		param.add(date_now);
		//check ngoai tuyen trong DK phai co Tuyen trong TUAN nay
		/*var1.append("	             	AND (((cast((julianday(DATE ('now', 'localtime')) - julianday(start_date)) / 7 AS INT) + (	");
		var1.append("	            									CASE WHEN ((julianday(DATE ('now', 'localtime')) - julianday(start_date)) % 7 > 0)	");
		var1.append("	            											AND (cast((CASE WHEN strftime('%w', 'now', 'localtime') = '0' THEN 7 ELSE strftime('%w', 'now', 'localtime') END) AS INT) < cast((CASE WHEN strftime('%w', start_date) = '0' THEN 7 ELSE strftime('%w', start_date) END) AS INT)) THEN 1 ELSE 0 END	");
		var1.append("	            									)	");
		var1.append("	            								) % 4 + 1) IN ( WEEK1 * 1, WEEK2 * 2, WEEK3 * 3, WEEK4 * 4) )	");*/
		var1.append("                                   ) RTC, ");
		var1.append("               CUSTOMER CT ");
		var1.append("        WHERE  VP.ROUTING_ID = RT.ROUTING_ID ");
		var1.append("               AND RTC.ROUTING_ID = RT.ROUTING_ID ");
		var1.append("               AND RTC.CUSTOMER_ID = CT.CUSTOMER_ID ");
		var1.append("               AND DATE(VP.FROM_DATE) <= DATE(?) ");
		param.add(date_now);
		var1.append("               AND IFNULL(DATE(VP.TO_DATE) >= DATE(?), 1) ");
		param.add(date_now);
		var1.append("               AND VP.STAFF_ID = ? ");
		param.add("" + staffId);
		var1.append("               AND VP.STATUS = 1 ");
		var1.append("               AND RT.STATUS = 1 ");
		var1.append("               AND CT.STATUS = 1 ");
		var1.append("               AND CT.STATUS = 1 ) CUS ");
		var1.append("      JOIN (SELECT CUSTOMER_ID AS CUSTOMER_ID_1, Strftime('%H:%M',START_TIME) AS START_TIME ");
		var1.append("             FROM   ACTION_LOG AL ");
		var1.append("             WHERE  DATE(AL.START_TIME) = DATE(?) ");
		param.add(date_now);
		var1.append("                    AND AL.END_TIME IS NOT NULL ");
		var1.append("                    AND AL.OBJECT_TYPE IN ( 0, 1 ) ");
		var1.append("               	 AND AL.STAFF_ID = ? ");
		param.add("" + staffId);
		var1.append("                    AND AL.IS_OR = 1) ALOG ");
		var1.append("         ON CUS.CUSTOMER_ID = ALOG.CUSTOMER_ID_1 ");
		var1.append("       LEFT JOIN (SELECT CUSTOMER_ID AS CUSTOMER_ID_2, ");
		var1.append("                         SUM(TOTAL_QUANTITY)  AS QUANTITY_IN_DATE ");
		var1.append("                  FROM   SALE_ORDER");
		var1.append("                  WHERE  1 = 1 ");
		var1.append("                         AND approved in (0,1) "); //--chua duyet+duyet
		var1.append("                         AND type = 1");
		var1.append("                         AND STAFF_ID = ? ");
		param.add("" + staffId);
		var1.append("                         AND ( DATE(ORDER_DATE) = DATE(?) ) ");
		param.add(date_now);
		var1.append("                  GROUP BY CUSTOMER_ID");
		var1.append("                  ) SALES ");
		var1.append("              ON CUS.CUSTOMER_ID = SALES.CUSTOMER_ID_2 ");
		var1.append("    ORDER BY START_TIME ASC ");
		Cursor cursor = null;
		try {
			cursor = rawQueries(var1.toString(), param);
			if (cursor != null) {
				dto = new WrongPlanCustomerDTO();
				if (cursor.moveToFirst()) {
					do {
						WrongPlanCustomerItem item = dto.newWrongPlanCustomerItem();
						item.initWrongPlanCusItem(cursor);
						dto.arrWrong.add(item);
					} while (cursor.moveToNext());
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try{
    			if (cursor != null) {
    				cursor.close();
    			}
			} catch (Exception e) {}
		}
		return dto;
	}

	/**
	 * Request lay danh sach khach hang cua nhan vien trong tuyen Cho nhan vien
	 * giam sat vi tri khach hang
	 * 
	 * @author banghn
	 * @return
	 */
	public CustomerListDTO requestGetCustomerSaleList(String ownerId, String shopId, String staffId, String cusCode,
			String cusNameAdd, String visitPlan, int page) {
		CustomerListDTO dto = null;
		String dateNow=DateUtils.now();
		StringBuffer totalPageSql = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();

		StringBuffer sqlString = new StringBuffer();
		sqlString.append("SELECT TMP.*, max(SYN_STATE) ");
		sqlString.append("FROM   (SELECT VP.visit_plan_id                 AS VISIT_PLAN_ID, ");
		sqlString.append("               VP.shop_id                       AS SHOP_ID, ");
		sqlString.append("               VP.staff_id                      AS STAFF_ID, ");
		sqlString.append("               CT.customer_id                   AS CUSTOMER_ID, ");
		sqlString.append("               Substr(CT.customer_code, 1, 3)   AS CUSTOMER_CODE, ");
		sqlString.append("               CT.customer_name                 AS CUSTOMER_NAME, ");
		sqlString.append("               CT.name_text                 	  AS CUSTOMER_NAME_TEXT, ");
		sqlString.append("               CT.lat                           AS LAT, ");
		sqlString.append("               CT.lng                           AS LNG, ");
		sqlString.append("               CT.address                       AS ADDRESS, ");
		sqlString.append("               st.[staff_code]                  AS STAFF_CODE, ");
		sqlString.append("               st.[staff_name]                  AS STAFF_NAME, ");
		sqlString.append("               sc.[STAFF_CUSTOMER_ID]           AS STAFF_CUSTOMER_ID, ");
		sqlString.append("               STRFTIME('%d/%m/%Y', sc.exception_order_date)  AS EXCEPTION_ORDER_DATE, ");
		sqlString.append("               sc.SYN_STATE                     AS SYN_STATE, ");
		sqlString.append("               ( CT.housenumber ");
		sqlString.append("                 || ' ' ");
		sqlString.append("                 || CT.street )                 AS STREET, ");
		sqlString.append("               ( CASE ");
		sqlString.append("                   WHEN RTC.monday = 1 THEN 'T2' ");
		sqlString.append("                   ELSE '' ");
		sqlString.append("                 end ");
		sqlString.append("                 || CASE ");
		sqlString.append("                      WHEN RTC.tuesday = 1 THEN ',T3' ");
		sqlString.append("                      ELSE '' ");
		sqlString.append("                    end ");
		sqlString.append("                 || CASE ");
		sqlString.append("                      WHEN RTC.wednesday = 1 THEN ',T4' ");
		sqlString.append("                      ELSE '' ");
		sqlString.append("                    end ");
		sqlString.append("                 || CASE ");
		sqlString.append("                      WHEN RTC.thursday = 1 THEN ',T5' ");
		sqlString.append("                      ELSE '' ");
		sqlString.append("                    end ");
		sqlString.append("                 || CASE ");
		sqlString.append("                      WHEN RTC.friday = 1 THEN ',T6' ");
		sqlString.append("                      ELSE '' ");
		sqlString.append("                    end ");
		sqlString.append("                 || CASE ");
		sqlString.append("                      WHEN RTC.saturday = 1 THEN ',T7' ");
		sqlString.append("                      ELSE '' ");
		sqlString.append("                    end ");
		sqlString.append("                 || CASE ");
		sqlString.append("                      WHEN RTC.sunday = 1 THEN ',CN' ");
		sqlString.append("                      ELSE '' ");
		sqlString.append("                    end )                       AS TUYEN, ");

		// check co vi tri hay khong HAVE_POSITION
		sqlString.append("              ( CASE ");
		sqlString.append("                     WHEN (CT.LAT <= 0 OR CT.LNG <= 0) THEN 0 ");
		sqlString.append("                     ELSE 1 ");
		sqlString.append("                   END )       AS HAVE_POSITION, ");

		// LAY SO LAN UPDATE VI TRI, NGAY CAP NHAT VI TRI
		sqlString.append("               (SELECT Count(1) ");
		sqlString.append("                FROM   customer_position_log cpl ");
		sqlString.append("                WHERE  cpl.[customer_id] = ct.customer_id ");
		sqlString.append("                       AND cpl.[lat] > 0 ");
		sqlString.append("                       AND cpl.[lng] > 0)       AS NUM_UPDATE, ");
		sqlString.append("               Strftime('%d/%m/%Y', (SELECT cpl.create_date ");
		sqlString.append("                                     FROM   customer_position_log cpl ");
		sqlString.append("                                     WHERE  cpl.[customer_id] = ct.customer_id ");
		sqlString.append("                                            AND cpl.[lat] > 0 ");
		sqlString.append("                                            AND cpl.[lng] > 0 ");
		sqlString.append("                                     ORDER  BY create_date DESC ");
		sqlString.append("                                     LIMIT  1)) AS LAST_UPDATE ");
		sqlString.append("        FROM   visit_plan VP, ");
		sqlString.append("               routing RT, ");
		sqlString.append("               (SELECT routing_customer_id, ");
		sqlString.append("                       routing_id, ");
		sqlString.append("                       customer_id, ");
		sqlString.append("                       status, ");
		sqlString.append("                       monday, ");
		sqlString.append("                       tuesday, ");
		sqlString.append("                       wednesday, ");
		sqlString.append("                       thursday, ");
		sqlString.append("                       friday, ");
		sqlString.append("                       saturday, ");
		sqlString.append("                       sunday, ");
		sqlString.append("                       seq2, ");
		sqlString.append("                       seq3, ");
		sqlString.append("                       seq4, ");
		sqlString.append("                       seq5, ");
		sqlString.append("                       seq6, ");
		sqlString.append("                       seq7, ");
		sqlString.append("                       seq8 ");
		sqlString.append("                FROM   routing_customer ");
		sqlString.append("                WHERE (DATE(END_DATE) >= DATE(?) OR DATE(END_DATE) IS NULL) and ");
		param.add(dateNow);
		sqlString.append("                DATE(START_DATE) <= DATE(?)) RTC, ");
		param.add(dateNow);
		sqlString.append("               customer CT ");
		// LAY THONG TIN NHANVINE
		sqlString.append("               JOIN staff st ");
		sqlString.append("                 ON VP.staff_id = st.staff_id ");
		sqlString.append("                    AND st.status = 1 ");
		sqlString.append("                    AND st.staff_owner_id = ? ");
		param.add(ownerId);

		// THONG TIN KHACH HANG
		sqlString.append("               LEFT JOIN staff_customer sc ");
		sqlString.append("                      ON (sc.staff_id = VP.staff_id ");
		sqlString.append("                      	and sc.customer_id = RTC.customer_id)");
		sqlString.append("        WHERE  1 = 1 ");
		sqlString.append("               AND VP.routing_id = RT.routing_id ");
		sqlString.append("               AND RTC.routing_id = RT.routing_id ");
		sqlString.append("               AND RTC.customer_id = CT.customer_id ");
		sqlString.append("               AND Ifnull(Date(VP.from_date) <= Date(?), 0) ");
		param.add(dateNow);
		sqlString.append("               AND Ifnull(Date(VP.to_date) >= Date(?), 1) ");
		param.add(dateNow);
		sqlString.append("               AND VP.shop_id = ? ");
		param.add(shopId);
		sqlString.append("               AND CT.status = 1 ");
		sqlString.append("               AND VP.status in (0,1) ");
		sqlString.append("               AND RT.status = 1 ");
		sqlString.append("               ) AS TMP ");
		sqlString.append("WHERE  1 = 1 ");

		if (!StringUtil.isNullOrEmpty(visitPlan)) {
			sqlString.append("       AND Upper(TUYEN) LIKE Upper(?) ");
			param.add("%" + visitPlan + "%");
		}

		if (!StringUtil.isNullOrEmpty(staffId)) {
			sqlString.append("       AND STAFF_ID = ? ");
			param.add(staffId);
		}

		// customer code search
		if (!StringUtil.isNullOrEmpty(cusCode)) {
			cusCode = StringUtil.escapeSqlString(cusCode);
			cusCode = DatabaseUtils.sqlEscapeString("%" + cusCode + "%");
			cusCode = cusCode.substring(1,cusCode.length()-1);
			sqlString.append("	and upper(CUSTOMER_CODE) like upper(?) escape '^' ");
			param.add(cusCode);

		}
		// customer name search
		if (!StringUtil.isNullOrEmpty(cusNameAdd)) {
			cusNameAdd = StringUtil.escapeSqlString(cusNameAdd);
			cusNameAdd = DatabaseUtils.sqlEscapeString("%" + cusNameAdd + "%");
			cusNameAdd = cusNameAdd.substring(1, cusNameAdd.length()-1);
			sqlString.append("	and ((upper(CUSTOMER_NAME_TEXT) like upper(?) escape '^') ");
			param.add(cusNameAdd);
			sqlString.append(" or (upper(STREET) like upper(?) escape '^')");
			param.add(cusNameAdd);
			sqlString.append(" or (upper(ADDRESS) like upper(?) escape '^'))");
			param.add(cusNameAdd);
		}

		// group theo staff_id, customer_id lay syn_state lon nhat
		sqlString.append("group by staff_id, customer_id ");
		// thu tu order
		sqlString.append("order by STAFF_CODE asc, STAFF_NAME asc, CUSTOMER_CODE asc, CUSTOMER_NAME asc, HAVE_POSITION asc ");
		Cursor cTotalRow = null;
		try {
			if (page > 0) {
				totalPageSql.append("select count(1) as TOTAL_ROW from ("
						+ sqlString + ")");
				cTotalRow = rawQueries(totalPageSql.toString(), param);
				if (dto == null) {
					dto = new CustomerListDTO();
				}
				if (cTotalRow != null) {
					if (cTotalRow.moveToFirst()) {
						dto.totalCustomer = cTotalRow.getInt(0);
					}
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			try {
				if (cTotalRow != null) {
					cTotalRow.close();
				}
			} catch (Exception ex) {
			}
		}
		
		Cursor c = null;
		try {
			sqlString.append(" limit ? offset ? ");
			param.add(Integer.toString(Constants.NUM_ITEM_PER_PAGE));
			param.add(Integer.toString((page - 1) * Constants.NUM_ITEM_PER_PAGE));
			c = rawQueries(sqlString.toString(), param);
			if (c != null) {
				if (dto == null) {
					dto = new CustomerListDTO();
				}
				if (c.moveToFirst()) {
					do {
						CustomerListItem item = new CustomerListItem();
						item.initDataCustomerSaleItem(c);
						dto.cusList.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
			}
		}
		return dto;
	}

	/**
	 * HieuNH Dem so luong khach hang chua phat sinh doanh so trong thang
	 * @param shopId
	 * @param staffOwnerId
	 *            : id NVGS
	 * @param visit_plan
	 *            : tuyen nao (T2, T3,...,)
	 * @param staffId
	 *            : id NVBH
	 * @return
	 */

	public int getCountCustomerNotPsdsInMonthReport(long shopId, String staffOwnerId, String visit_plan, String staffId) {
		int count = 0;
		List<String> params = new ArrayList<String>();
		String dateNow=DateUtils.now();
		StringBuffer sqlQuery = new StringBuffer();

		sqlQuery.append("SELECT Count(*) COUNT ");
		sqlQuery.append("FROM   (SELECT s.staff_id, ");
		sqlQuery.append("               s.staff_name NVBH, ");
		sqlQuery.append("               a.customer_id ");
		sqlQuery.append("        FROM   (SELECT * ");
		sqlQuery.append("                FROM   routing_customer ");
		sqlQuery.append("                WHERE  Round(Strftime('%W', ?) + 1 - start_week ");
		params.add(dateNow);
		sqlQuery.append("                       ) >= 0 ");
		sqlQuery.append("                       AND status = 1) a, ");
		sqlQuery.append("               visit_plan b, ");
		sqlQuery.append("               routing c, ");
		sqlQuery.append("               staff s, ");
		sqlQuery.append("               shop sh, ");
		sqlQuery.append("               customer cu ");
		sqlQuery.append("        WHERE  1 = 1 ");
		sqlQuery.append("               AND b.routing_id = a.routing_id ");
		sqlQuery.append("               AND c.routing_id = a.routing_id ");
		sqlQuery.append("               AND b.staff_id = s.staff_id ");
		sqlQuery.append("               AND a.customer_id = cu.customer_id ");
		sqlQuery.append("               AND s.shop_id = sh.shop_id ");
		sqlQuery.append("               AND sh.status = 1 ");
		sqlQuery.append("               AND a.status = 1 ");
		sqlQuery.append("               AND b.status = 1 ");
		sqlQuery.append("               AND s.status = 1 ");
		sqlQuery.append("               AND c.status = 1 ");
		sqlQuery.append("               AND cu.status = 1 ");
		sqlQuery.append("               AND ( (SELECT Date(?)) >= Date(b.from_date) ");
		params.add(dateNow);
		sqlQuery.append("                     AND ( b.to_date IS NULL ");
		sqlQuery.append("                            OR (SELECT Date(?)) <= ");
		params.add(dateNow);
		sqlQuery.append("                               Date(b.to_date) ) ) ");
		sqlQuery.append("               AND exists (SELECT a.staff_id ");
		sqlQuery.append("                                  FROM   staff a, ");
		sqlQuery.append("                                         channel_type b ");
		sqlQuery.append("                                  WHERE  a.staff_type_id = b.channel_type_id ");
		sqlQuery.append("                                         AND a.status = 1 ");
		sqlQuery.append("                                         AND b.status = 1 ");
		sqlQuery.append("                                         AND a.shop_id = ? ");
		params.add("" + shopId);
		// var1.append("                                         AND staff_owner_id = 570--gs.staff_id ");
		if (!StringUtil.isNullOrEmpty(staffOwnerId)) {
			sqlQuery.append(" AND staff_owner_id in ( ");
			sqlQuery.append(staffOwnerId);
			sqlQuery.append(" )");
		}
		sqlQuery.append("       and a.staff_id=b.staff_id        )) tb1 ");
		sqlQuery.append("       LEFT JOIN staff_customer tb2 ");
		sqlQuery.append("              ON ( tb1.staff_id = tb2.staff_id ");
		sqlQuery.append("                   AND tb1.customer_id = tb2.customer_id ) ");
		sqlQuery.append("WHERE  1 = 1 ");
		sqlQuery.append("       AND ( Date(tb2.last_approve_order) IS NULL ");
		sqlQuery.append("              OR tb2.last_approve_order = '' ");
		sqlQuery.append("              OR Date(tb2.last_approve_order) < (SELECT ");
		sqlQuery.append("                 Date(?, 'start of month' ");
		params.add(dateNow);
		sqlQuery.append("                 )) ) ");
		sqlQuery.append("       AND ( tb2.last_order IS NULL ");
		sqlQuery.append("              OR tb2.last_order = '' ");
		sqlQuery.append("              OR Date(tb2.last_order) < (SELECT Date(?)) ) ");
		params.add(dateNow);
		if (!StringUtil.isNullOrEmpty(visit_plan)) {// ko phai chon tat ca
			sqlQuery.append("	and TUYEN like ? ");
			params.add("%" + visit_plan + "%");
		}
		if (!StringUtil.isNullOrEmpty(staffId)) {// ko phai chon tat ca
			sqlQuery.append("	and tb1.STAFF_ID = ?");
			params.add(staffId);
		}

		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));

			if (c != null && c.moveToFirst()) {
				count = c.getInt(c.getColumnIndex("COUNT"));
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

		return count;
	}
	
	/**
	 * Láº¥y bĂ¡o cĂ¡o khĂ¡ch hĂ ng chÆ°a PSDS trong thĂ¡ng cho role NVBH 
	 * @author: duongdt3
	 * @since: 15:06:05 23 Nov 2013
	 * @return: CustomerNotPsdsInMonthReportViewDTO
	 * @throws:  
	 * @param shopId
	 * @param dayInWeek
	 * @param staffId
	 * @param page
	 * @param numItemInPage
	 * @return
	 */
	public CustomerNotPsdsInMonthReportViewDTO getReportCustomerNotPSDSForSaleMan(long shopId, int dayInWeek, String staffId, int page, int numItemInPage) {
		CustomerNotPsdsInMonthReportViewDTO dto = new CustomerNotPsdsInMonthReportViewDTO();
		String dateNow=DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		String dateFirstMonthNow = DateUtils.getFirstDateOfNumberPreviousMonthWithFormat(DateUtils.DATE_FORMAT_SQL_MONTH_DEFAULT,0);
		String dateFirstMonthLast = DateUtils.getFirstDateOfNumberPreviousMonthWithFormat(DateUtils.DATE_FORMAT_SQL_MONTH_DEFAULT,-1);
		//lay thu cua ngay hien tai
		String strdayInWeek  = "";
		if (dayInWeek >= 2) {
			//dayInWeek 2 -> 8
			strdayInWeek = Constants.TODAY[dayInWeek-2];
		}
		
		ArrayList<String> params =new ArrayList<String>();
		StringBuilder sqlQuery = new StringBuilder();
		//############# BEGIN SQL ###################		
		sqlQuery.append("	SELECT");
		sqlQuery.append("		  rt_cus.ADDRESS ADDRESS");
		sqlQuery.append("		, rt_cus.customer_id CUSTOMER_ID");
		sqlQuery.append("		, rt_cus.CUSTOMER_SHORT_CODE CUSTOMER_CODE");
		sqlQuery.append("		, rt_cus.customer_name CUSTOMER_NAME");
		sqlQuery.append("		, IFNULL(Strftime('%d/%m/%Y', rt_cus.RT_CUS_START_DATE), '') RT_CUS_START_DATE ");
		sqlQuery.append("		, IFNULL(Strftime('%d/%m/%Y', rt_cus.RT_CUS_END_DATE), '') RT_CUS_END_DATE ");
		sqlQuery.append("		, Strftime('%d/%m/%Y', st_cus.last_approve_order) LAST_ORDER_DATE");
		sqlQuery.append("		, rt_cus.staff_id STAFF_ID");
		sqlQuery.append("		, rt_cus.staff_name STAFF_NAME");
		sqlQuery.append("		, rt_cus.staff_code STAFF_CODE");
		sqlQuery.append("		, ( SELECT Count(al.action_log_id)");
		sqlQuery.append("			FROM action_log al");
		sqlQuery.append("			WHERE al.object_type IN (0, 1)");
		sqlQuery.append("				AND al.customer_id = rt_cus.customer_id");
		sqlQuery.append("				AND al.staff_id = rt_cus.staff_id");
		sqlQuery.append("				AND substr(al.start_time,1,7) >= ? ");
		params.add(dateFirstMonthNow);
		sqlQuery.append("		) AS VISIT_NUMBER_IN_MONTH");
		sqlQuery.append("		, (");
		sqlQuery.append("			SELECT Sum(amount)");
		sqlQuery.append("			FROM rpt_sale_in_month rsim");
		sqlQuery.append("			WHERE substr(month,1,7) = ? ");
		params.add(dateFirstMonthLast);
		sqlQuery.append("				AND rsim.customer_id = rt_cus.customer_id");
		sqlQuery.append("				AND rsim.STATUS = 1");
		sqlQuery.append("				AND rsim.staff_id = rt_cus.staff_id");
		sqlQuery.append("		) AS AMOUNT_IN_LAST_MONTH, ");
		sqlQuery.append("		 (CASE WHEN rt_cus.monday = 1 THEN 'T2,' ELSE '' END )");
		sqlQuery.append("		 || ( CASE WHEN rt_cus.tuesday = 1 THEN 'T3,' ELSE '' END ) ");
		sqlQuery.append("		 || ( CASE WHEN rt_cus.wednesday = 1 THEN 'T4,' ELSE '' END ) ");
		sqlQuery.append("		 || ( CASE WHEN rt_cus.thursday = 1 THEN 'T5,' ELSE '' END ) ");
		sqlQuery.append("		 || ( CASE WHEN rt_cus.friday = 1 THEN 'T6,' ELSE '' END ) ");
		sqlQuery.append("		 || ( CASE WHEN rt_cus.saturday = 1 THEN 'T7,' ELSE '' END ) ");
		sqlQuery.append("		 || ( CASE WHEN rt_cus.sunday = 1 THEN 'CN' ELSE ''END ) AS LIST_ROUTING_DATE");
		sqlQuery.append("		, IFNULL( substr(st_cus.last_approve_order,1,7) < ? , 1) IS_LOSS_DISTRIBUTION");
		params.add(dateFirstMonthLast);
		sqlQuery.append("		, rt_cus.CURRENT_WEEK CURRENT_WEEK");
		sqlQuery.append("	  , IFNULL(rt_cus.WEEK1,0) WEEK1  ");
		sqlQuery.append("	  , IFNULL(rt_cus.WEEK2,0) WEEK2 ");
		sqlQuery.append("	  , IFNULL(rt_cus.WEEK3,0) WEEK3 ");
		sqlQuery.append("	  , IFNULL(rt_cus.WEEK4,0) WEEK4 ");
		sqlQuery.append("	FROM (");
		sqlQuery.append("		SELECT IFNULL(cu.address, '') address, cu.short_code CUSTOMER_SHORT_CODE, a.START_DATE RT_CUS_START_DATE, a.END_DATE RT_CUS_END_DATE,   *");
		sqlQuery.append("			   ");
		sqlQuery.append("		       ");
		sqlQuery.append("		FROM (");
		sqlQuery.append("			SELECT (");
		sqlQuery.append("					(");
		sqlQuery.append("						cast((julianday(?) - julianday(START_DATE)) / 7 AS INT) + (");
		params.add(dateNow);
		sqlQuery.append("							CASE ");
		sqlQuery.append("								WHEN ((julianday(?) - julianday(START_DATE)) % 7 > 0)");
		params.add(dateNow);
		sqlQuery.append("									AND (");
		sqlQuery.append("										cast((");
		sqlQuery.append("												CASE ");
		sqlQuery.append("													WHEN strftime('%w', ?) = '0'");
		params.add(dateNow);
		sqlQuery.append("														THEN 7");
		sqlQuery.append("													ELSE strftime('%w', ?)");
		params.add(dateNow);
		sqlQuery.append("													END");
		sqlQuery.append("												) AS INT) < cast((");
		sqlQuery.append("												CASE ");
		sqlQuery.append("													WHEN strftime('%w', START_DATE) = '0'");
		sqlQuery.append("														THEN 7");
		sqlQuery.append("													ELSE strftime('%w', START_DATE)");
		sqlQuery.append("													END");
		sqlQuery.append("												) AS INT)");
		sqlQuery.append("										)");
		sqlQuery.append("									THEN 1");
		sqlQuery.append("								ELSE 0");
		sqlQuery.append("								END");
		sqlQuery.append("							)");
		sqlQuery.append("						) % 4 + 1");
		sqlQuery.append("					) CURRENT_WEEK,  ");
		sqlQuery.append("			routing_id,");
		sqlQuery.append("			customer_id,  ");
		sqlQuery.append("			start_date, end_date,  ");
		sqlQuery.append("			monday, tuesday, wednesday, thursday,   ");
		sqlQuery.append("			friday, saturday, sunday, ");
		sqlQuery.append("			week1, week2,week3, week4");
		sqlQuery.append("			FROM routing_customer");
		sqlQuery.append("			WHERE 1 = 1");
		//Thá»© hiá»‡n táº¡i pháº£i cĂ³ Ä‘i tuyáº¿n
		//náº¿u cĂ³ Ä‘iá»�u kiá»‡n nĂ y thĂ¬ thĂªm vĂ o
		if (!StringUtil.isNullOrEmpty(strdayInWeek)) {
			sqlQuery.append("				AND " + strdayInWeek + " = 1");
		}
		//duongdt3 khi chuyen doi, import du lieu tuyen, ngay mai moi co hieu luc, nhung da chuyen status = 0 => ko check status
		//sqlQuery.append("				AND STATUS = 1");
//		sqlQuery.append("				AND (	monday	OR tuesday	OR wednesday	OR thursday	OR friday	OR saturday	OR sunday	) = 1");
		
		//dungdq3 fix bug 25217 start
		//duongdt3 , them dieu kien check ngay bact dau, ket thuc giua STAFF +  CUSTOMER >= NOW
		sqlQuery.append("				and substr(START_DATE,1,10) <= ? ");
		params.add(dateNow);
		sqlQuery.append("				and IFNULL(substr(END_DATE,1,10) >= ?, 1) ");
		params.add(dateNow);
		// dungdq3 fix bug 25217 end
		
		sqlQuery.append("			) a, visit_plan b, routing c, staff s, shop sh, customer cu");
		sqlQuery.append("		WHERE 1 = 1");
		sqlQuery.append("			AND b.routing_id = a.routing_id");
		sqlQuery.append("			AND c.routing_id = a.routing_id");
		sqlQuery.append("			AND b.staff_id = s.staff_id");
		sqlQuery.append("			AND a.customer_id = cu.customer_id");
		sqlQuery.append("			AND s.shop_id = sh.shop_id");
		sqlQuery.append("			AND sh.STATUS = 1");
		//duongdt3 khi chuyen doi, import du lieu tuyen, ngay mai moi co hieu luc, nhung da chuyen status = 0 => ko check status
		//sqlQuery.append("			AND b.STATUS = 1");
		sqlQuery.append("			AND s.STATUS = 1");
		sqlQuery.append("			AND c.STATUS = 1");
		sqlQuery.append("			AND cu.STATUS = 1");
		sqlQuery.append("			AND ? >= substr(b.from_date,1,10)");
		params.add(dateNow);
		sqlQuery.append("			AND IFNULL(? <= substr(b.to_date,1,10), 1)");
		params.add(dateNow);
		//##### BEGIN ADD PARAM #####
		sqlQuery.append("			AND b.staff_id IN (" + staffId + ")"); // staffId nguoi dung k nhap vao nen k bi SQL Injection
		//##### END ADD PARAM #####
		sqlQuery.append("		) rt_cus");
		sqlQuery.append("	LEFT JOIN staff_customer st_cus");
		sqlQuery.append("		ON (rt_cus.staff_id = st_cus.staff_id AND rt_cus.customer_id = st_cus.customer_id)");
		sqlQuery.append("	WHERE 1 = 1");
		sqlQuery.append("		AND IFNULL( substr(st_cus.last_approve_order,1,7) < ? , 1)");
		params.add(dateFirstMonthNow);
		sqlQuery.append("		AND IFNULL(substr(st_cus.last_order,1,10) < ?, 1)");
		params.add(dateNow);
		sqlQuery.append("	ORDER BY VISIT_NUMBER_IN_MONTH DESC, CUSTOMER_CODE ASC, CUSTOMER_NAME ASC");
		//############# END SQL ###################

		Cursor countC = null;
		try {
			EXCEPTION_DAY_TABLE ex = new EXCEPTION_DAY_TABLE(mDB);
			dto.setListExcepTionDay(ex.getListExceptionDay());

			String sqlC = sqlQuery.toString();
			String sqlCount = StringUtil.getCountSql(sqlC);
			if (page <= 0) {
				// get total page
				countC = this.rawQueries(sqlCount, params);
				if (countC != null && countC.moveToFirst()) {
					dto.totalList = countC.getInt(0);
				}
				page = 1;
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
		} finally {
			try {
				if (countC != null) {
					countC.close();
				}
			} catch (Exception e) {
			}
		}
		
		Cursor c = null;
		try {
			// phan trang
			String strPaging = StringUtil.getPagingSql(numItemInPage, page);
			sqlQuery.append(strPaging);
			String sqlWithPaging = sqlQuery.toString();

			c = this.rawQueries(sqlWithPaging, params);

			if (c != null && c.moveToFirst()) {
				do {
					dto.addItem(c);
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
			}
		}
		
		return dto;
	}


	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @param lessThan2MinList
	 * @return
	 * @return: LessThan2MinsDTOvoid
	 * @throws:
	 */
	public GsnppLessThan2MinsDTO requestGetLessThan2Mins(long staffId, String lessThan2MinList) {
		GsnppLessThan2MinsDTO dto = null;
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);

		ArrayList<String> params = new ArrayList<String>();
		StringBuffer var1 = new StringBuffer();
		var1.append("SELECT * ");
		var1.append("FROM   (SELECT CUSTOMER.CUSTOMER_ID, ");
		//var1.append("               ( SUBSTR(CUSTOMER.CUSTOMER_CODE, 1, 3) ");
		var1.append("               ( CUSTOMER.SHORT_CODE ");
		var1.append("                 || ' - ' ");
		var1.append("                 || CUSTOMER.CUSTOMER_NAME )            AS CUS_CODE_NAME, ");
		var1.append("               CUSTOMER.ADDRESS						 AS STREET, ");
		var1.append("               STRFTIME('%H:%M', ACTION_LOG.START_TIME) AS START_TIME, ");
		var1.append("               STRFTIME('%H:%M', ACTION_LOG.END_TIME)   AS END_TIME ");
		var1.append("        FROM   CUSTOMER, ");
		var1.append("               ACTION_LOG ");
		var1.append("        WHERE  CUSTOMER.CUSTOMER_ID = ACTION_LOG.CUSTOMER_ID ");
		var1.append("               AND DATE(ACTION_LOG.START_TIME) = DATE(?) ");
		params.add(date_now);
		var1.append("               AND ACTION_LOG.OBJECT_TYPE IN ( 0, 1 ) ");
		var1.append("               AND ACTION_LOG.STAFF_ID = ? ");
		params.add(String.valueOf(staffId));
		var1.append("               AND CUSTOMER.CUSTOMER_ID IN ( " + lessThan2MinList + " )");
		var1.append("       ORDER BY START_TIME ASC");
		var1.append("               ) CUS ");
		var1.append("       LEFT JOIN (SELECT CUSTOMER_ID AS CUSTOMER_ID_1, ");
		var1.append("                         SUM(TOTAL_QUANTITY)  AS QUANTITY_IN_DATE ");
		var1.append("                  FROM   SALE_ORDER");
		var1.append("                  WHERE  1 = 1 ");
		var1.append("                         AND approved in (0,1) "); //--chua duyet+duyet
		var1.append("                         AND type = 1");
		var1.append("                         AND STAFF_ID = ? ");
		params.add(String.valueOf(staffId));
		var1.append("                         AND DATE(ORDER_DATE) = DATE(?) ");
		params.add(date_now);
		var1.append("                  GROUP BY CUSTOMER_ID");
		var1.append("                  ) SALES ");
		var1.append("              ON CUS.CUSTOMER_ID = SALES.CUSTOMER_ID_1 ");
		
		Cursor cursor = null;
		try {
			cursor = rawQueries(var1.toString(), params);
			if (cursor != null) {
				dto = new GsnppLessThan2MinsDTO();
				if (cursor.moveToFirst()) {
					do {
						LessThan2MinsItem item = dto.newLessThan2MinsItem();
						item.initItem(cursor);
						dto.arrList.add(item);
					} while (cursor.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.logger.debug("requestGetLessThan2Mins", e);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return dto;
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @param isGetTotalPage
	 * @param page
	 * @return
	 * @return: CustomerListDTOvoid
	 * @throws:
	 */
	public CustomerListDTO requestGetCusNoPSDS(int rptSaleHisId, boolean isGetTotalPage, int page) {
		CustomerListDTO dto = null;
		ArrayList<String> params = new ArrayList<String>();
		ArrayList<String> totalPageParam = new ArrayList<String>();
		StringBuffer var1 = new StringBuffer();
		var1.append("SELECT CUSTOMER.CUSTOMER_ID, ");
		var1.append("       SUBSTR(CUSTOMER.CUSTOMER_CODE, 1, 3) AS CUSTOMER_CODE, ");
		var1.append("       CUSTOMER.CUSTOMER_NAME, ");
		var1.append("       CUSTOMER.HOUSENUMBER ");
		var1.append("       || ' ' ");
		var1.append("       || CUSTOMER.STREET                   AS ADDRESS ");
		var1.append("FROM   RPT_CUSTOMER_INACTIVE, ");
		var1.append("       CUSTOMER ");
		var1.append("WHERE  RPT_SALE_HISTORY_ID = ? ");
		params.add("" + rptSaleHisId);
		totalPageParam.add("" + rptSaleHisId);
		var1.append("       AND RPT_CUSTOMER_INACTIVE.CUSTOMER_ID = CUSTOMER.CUSTOMER_ID ");
		var1.append("ORDER BY CUSTOMER_NAME ");

		// get count
		StringBuffer totalCount = new StringBuffer();
		if (isGetTotalPage == true) {
			totalCount.append("select count(*) as TOTAL_ROW from (" + var1 + ")");
		}

		if (page > 0) {
			var1.append("       limit ? offset ?");
			params.add("" + 5);
			params.add("" + (page - 1) * 5);
		}

		Cursor c = null;
		try {
			c = rawQueries(var1.toString(), params);
			if (c != null) {
				dto = new CustomerListDTO();
				if (c.moveToFirst()) {
					do {
						CustomerListItem item = new CustomerListItem();
						item.initDataFromNoPSDS(c);
						dto.cusList.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
			}
		}
		
		Cursor c_totalRow = null;
		try{
			if (isGetTotalPage == true) {
				c_totalRow = rawQueries(totalCount.toString(), totalPageParam);
				if (c_totalRow != null) {
					if (c_totalRow.moveToFirst()) {
						dto.totalCustomer = c_totalRow.getInt(0);
					}
				}
			}
		} catch (Exception e) {
		} finally {
			try {
				if (c_totalRow != null) {
					c_totalRow.close();
				}
			} catch (Exception ex) {
			}
		}
		return dto;
	}

	/**
	 * Lay ds KH cua C2
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return
	 * @return: CustomerListDTO
	 * @throws:
	 */
	public CustomerListDTO getCustomerListOfC2(long staffId, long shopId, long c2CustomerId, String customerName, String customerCode,
			int page, boolean isGetTotalPage, int type) throws Exception {
		CustomerListDTO dto = new CustomerListDTO();
		String date_now = DateUtils
				.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		StringBuffer var1 = new StringBuffer();
		StringBuffer totalPageSql = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();

		var1.append("SELECT * ");
		var1.append("FROM   (SELECT S.SHOP_CODE              AS SHOP_CODE, ");
		var1.append("               CT.CUSTOMER_ID          AS CUSTOMER_ID, ");
		var1.append("               CT.SHORT_CODE  AS CUSTOMER_CODE, ");
		var1.append("               CT.SHORT_CODE  AS SHORT_CODE, ");
		var1.append("               CT.CUSTOMER_NAME        AS CUSTOMER_NAME, ");
		var1.append("               S.DISTANCE_ORDER   AS     SHOP_DISTANCE, ");
		var1.append("               CT.MOBIPHONE            AS MOBIPHONE, ");
		var1.append("               CT.NAME_TEXT    AS NAME_TEXT, ");
		var1.append("               CT.PHONE                AS PHONE, ");
		var1.append("               CT.LAT                  AS LAT, ");
		var1.append("               CT.LNG                  AS LNG, ");
		var1.append("               CT.CHANNEL_TYPE_ID      AS CHANNEL_TYPE_ID, ");
		var1.append("               CT.ADDRESS       AS ADDRESS, ");
		var1.append("               ( CT.HOUSENUMBER ");
		var1.append("                 || ' ' ");
		var1.append("                 || CT.STREET )        AS STREET, ");
		var1.append("               (CASE WHEN C2_SO.C2_SALE_ORDER_ID > 0 then 1 else 0 end) AS HAS_TODAY_ORDERED ");
		var1.append("			FROM CUSTOMER CT, SHOP S ");
		var1.append("			LEFT JOIN C2_SALE_ORDER C2_SO ON CT.CUSTOMER_ID = C2_SO.CUSTOMER_ID AND substr(C2_SO.CREATE_DATE,1,10) = ? ");
		param.add(date_now);
		var1.append("        WHERE  1 = 1 ");
		var1.append("               AND S.SHOP_ID = CT.SHOP_ID ");
		if (shopId > 0) {
			var1.append("               AND CT.SHOP_ID = ? ");
			param.add(String.valueOf(shopId));
		}
		var1.append("               AND CT.STATUS = 1 ");
		var1.append("               AND CT.PARENT_CUSTOMER_ID = ? ");
		param.add(String.valueOf(c2CustomerId));
		var1.append("   group by CT.CUSTOMER_ID) CTT ");
		
		var1.append("   Where 1 = 1 ");
		if (!StringUtil.isNullOrEmpty(customerCode)) {
			customerCode = StringUtil.escapeSqlString(customerCode);
			customerCode = DatabaseUtils.sqlEscapeString("%" + customerCode+ "%");
			customerCode= customerCode.substring(1, customerCode.length()-1);
			var1.append("	and upper(CTT.SHORT_CODE) like upper(?) escape '^' ");
			param.add(customerCode);
		}
		if (!StringUtil.isNullOrEmpty(customerName)) {
			customerName = StringUtil
					.getEngStringFromUnicodeString(customerName);
			customerName = StringUtil.escapeSqlString(customerName);
			customerName = DatabaseUtils.sqlEscapeString("%" + customerName+ "%");
			customerName = customerName.substring(1,customerName.length()-1);
			var1.append("	and upper(CTT.NAME_TEXT) like upper(?) escape '^' ");
			param.add(customerName);
		}

		var1.append(" ORDER BY CTT.HAS_TODAY_ORDERED ASC, CTT.CUSTOMER_CODE asc, CTT.CUSTOMER_NAME asc ");

		Cursor cTotalRow = null;
		if (page > 0) {
			// get count
			if (isGetTotalPage) {
				totalPageSql.append("select count(*) as TOTAL_ROW from ("
						+ var1 + ")");

				try {
					cTotalRow = rawQueries(totalPageSql.toString(), param);
					if (cTotalRow != null) {
						if (cTotalRow.moveToFirst()) {
							dto.totalCustomer = cTotalRow.getInt(0);
						}
					}
				} catch (Exception e) {
					VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
				} finally {
					try {
						if (cTotalRow != null) {
							cTotalRow.close();
						}
					} catch (Exception ex) {
					}
				}
			}
			var1.append(" limit "
					+ Integer.toString(Constants.NUM_ITEM_PER_PAGE));
			var1.append(" offset "
					+ Integer.toString((page - 1) * Constants.NUM_ITEM_PER_PAGE));
		}
		

		Cursor c = null;
		try {
			c = rawQueries(var1.toString(), param);

			if (c != null) {
				if (c.moveToFirst()) {
					do {
						CustomerListItem item = new CustomerListItem();
						item.initDataCustomerOfC2FromCursor(c);

						dto.cusList.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
			}
		}

		return dto;
	}


	/**
	 * Lay danh sach khach hang cua C2
	 * @author: quangvt1
	 * @since: 14:18:17 06-05-2014
	 * @return: CustomerListDTO
	 * @throws:  
	 * @param staffId
	 * @param shopId
	 * @param c2CustomerId: id cua customerc2
	 * @param dataSearch: ten va dia chi, ma, so dien thoai ban, dien thoai di dong 
	 * @param page
	 * @param isGetTotalPage
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public CustomerListDTO getListCustomerOfC2(long staffId, long shopId, long c2CustomerId, String dataSearch,
			int page, boolean isGetTotalPage, int type) throws Exception {
		CustomerListDTO dto = new CustomerListDTO();
		StringBuffer var1 = new StringBuffer();
		StringBuffer totalPageSql = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();

		var1.append("SELECT * ");
		var1.append("FROM   (SELECT S.SHOP_CODE              AS SHOP_CODE, ");
		var1.append("               CT.CUSTOMER_ID          AS CUSTOMER_ID, ");
		var1.append("               CT.SHORT_CODE  AS CUSTOMER_CODE, ");
		var1.append("               CT.SHORT_CODE  AS SHORT_CODE, ");
		var1.append("               CT.CUSTOMER_NAME        AS CUSTOMER_NAME, ");
		var1.append("               S.DISTANCE_ORDER   AS     SHOP_DISTANCE, ");
		var1.append("               CT.MOBIPHONE            AS MOBIPHONE, ");
		var1.append("               CT.NAME_TEXT    AS NAME_TEXT, ");
		var1.append("               CT.PHONE                AS PHONE, ");
		var1.append("               CT.LAT                  AS LAT, ");
		var1.append("               CT.LNG                  AS LNG, ");
		var1.append("               CT.CHANNEL_TYPE_ID      AS CHANNEL_TYPE_ID, ");
		var1.append("               CT.ADDRESS       AS ADDRESS, ");
		var1.append("               ( CT.HOUSENUMBER ");
		var1.append("                 || ' ' ");
		var1.append("                 || CT.STREET )        AS STREET, ");
		var1.append("               (CASE WHEN C2_SO.C2_SALE_ORDER_ID > 0 then 1 else 0 end) AS HAS_TODAY_ORDERED ");
		var1.append("			FROM CUSTOMER CT, SHOP S ");
		var1.append("			LEFT JOIN C2_SALE_ORDER C2_SO ON CT.CUSTOMER_ID = C2_SO.CUSTOMER_ID AND date(C2_SO.CREATE_DATE) = date('now', 'localtime') ");
		var1.append("        WHERE  1 = 1 ");
		var1.append("               AND S.SHOP_ID = CT.SHOP_ID ");
		if (shopId > 0) {
			var1.append("               AND CT.SHOP_ID = ? ");
			param.add(String.valueOf(shopId));
		}
		var1.append("               AND CT.STATUS = 1 ");
		var1.append("               AND CT.PARENT_CUSTOMER_ID = ? ");
		param.add(String.valueOf(c2CustomerId));
		var1.append("   group by CT.CUSTOMER_ID) CTT ");
		
		var1.append("   Where 1 = 1 ");	
		if (!StringUtil.isNullOrEmpty(dataSearch)) {
			dataSearch = StringUtil
					.getEngStringFromUnicodeString(dataSearch);
			dataSearch = StringUtil.escapeSqlString(dataSearch);
			dataSearch = DatabaseUtils.sqlEscapeString("%" + dataSearch + "%");
			dataSearch = dataSearch.substring(1, dataSearch.length()-1);
			// tim theo truong ten va dia chi
			var1.append("	and (upper(CTT.NAME_TEXT) like upper(?) escape '^' ");
			param.add(dataSearch);
			// tim theo dt co dinh
			var1.append("	or upper(CTT.PHONE) like upper(?) escape '^' ");
			param.add(dataSearch);
			// tim theo ma khach hang
			var1.append("	or upper(CTT.SHORT_CODE) like upper(?) escape '^' ");
			param.add(dataSearch);
			// tim theo so dtdd
			var1.append("	or upper(CTT.MOBIPHONE) like upper(?) escape '^' )");
			param.add(dataSearch);
		}

		var1.append(" ORDER BY CTT.SHORT_CODE asc, CTT.CUSTOMER_NAME asc ");

		Cursor cTotalRow = null;
		if (page > 0) {
			// get count
			if (isGetTotalPage) {
				totalPageSql.append("select count(*) as TOTAL_ROW from ("
						+ var1 + ")");
				try {
					cTotalRow = rawQueries(totalPageSql.toString(), param);
					if (cTotalRow != null) {
						if (cTotalRow.moveToFirst()) {
							dto.totalCustomer = cTotalRow.getInt(0);
						}
					}
				} catch (Exception e) {
					VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
				} finally {
					try {
						if (cTotalRow != null) {
							cTotalRow.close();
						}
					} catch (Exception ex) {
					}
				}
			}
			var1.append(" limit "
					+ Integer.toString(Constants.NUM_ITEM_PER_PAGE));
			var1.append(" offset "
					+ Integer.toString((page - 1) * Constants.NUM_ITEM_PER_PAGE));
		}
		
		
		Cursor c = null;
		try {
			c = rawQueries(var1.toString(), param);

			if (c != null) {
				if (c.moveToFirst()) {
					do {
						CustomerListItem item = new CustomerListItem();
						item.initDataCustomerOfC2FromCursor(c);

						dto.cusList.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
			}
		}

		return dto;
	}

	/**
	 * há»— trá»£ láº¥y danh sĂ¡ch C2 tá»« id String
	 * @author: duongdt3
	 * @since: 10:00:32 6 Jan 2014
	 * @return: CustomerListDTO
	 * @throws:  
	 * @param staffId
	 * @param shopId
	 * @param page
	 * @param isGetTotalPage
	 * @return
	 */
	public CustomerListDTO getListC2Customer(String staffId, String shopId, int page, boolean isGetTotalPage) {
		return getListC2Customer(Integer.valueOf(staffId), Integer.valueOf(shopId), page, isGetTotalPage);
	}
	
	/**
	 * Lay ds KH la C2
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return
	 * @return: CustomerListDTO
	 * @throws:
	 */
	public CustomerListDTO getListC2Customer(long staffId, long shopId, int page, boolean isGetTotalPage) {
		CustomerListDTO dto = new CustomerListDTO();
		StringBuffer var1 = new StringBuffer();
		StringBuffer totalPageSql = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();

		var1.append("SELECT CT.CUSTOMER_ID          AS CUSTOMER_ID, ");
		var1.append("               CT.SHORT_CODE  AS CUSTOMER_CODE, ");
		var1.append("               CT.SHORT_CODE  AS SHORT_CODE, ");
		var1.append("               CT.CUSTOMER_NAME        AS CUSTOMER_NAME, ");
		var1.append("               CT.MOBIPHONE            AS MOBIPHONE, ");
		var1.append("               CT.NAME_TEXT    AS NAME_TEXT, ");
		var1.append("               CT.PHONE                AS PHONE, ");
		var1.append("               CT.LAT                  AS LAT, ");
		var1.append("               CT.LNG                  AS LNG, ");
		var1.append("               CT.CHANNEL_TYPE_ID      AS CHANNEL_TYPE_ID, ");
		var1.append("               CT.ADDRESS       AS ADDRESS, ");
		var1.append("               ( CT.HOUSENUMBER ");
		var1.append("                 || ' ' ");
		var1.append("                 || CT.STREET )        AS STREET ");
		var1.append("			FROM CUSTOMER CT ");
		var1.append("        WHERE  1 = 1 ");
		if (shopId > 0) {
			var1.append("               AND CT.SHOP_ID = ? ");
			param.add(String.valueOf(shopId));
		}
		var1.append("               AND CT.STATUS = 1 ");
		var1.append("               AND EXISTS (SELECT CHANNEL_TYPE_ID FROM CHANNEL_TYPE CHT "); 
		var1.append("               WHERE CHT.CHANNEL_TYPE_ID = CT.CHANNEL_TYPE_ID AND CHT.OBJECT_TYPE = 4) ");
		var1.append(" ORDER BY UPPER(CT.CUSTOMER_NAME) asc ");

		Cursor cTotalRow = null;
		if (page > 0) {
			// get count
			if (isGetTotalPage) {
				totalPageSql.append("select count(*) as TOTAL_ROW from ("
						+ var1 + ")");
				try {
					cTotalRow = rawQueries(totalPageSql.toString(), param);
					if (cTotalRow != null) {
						if (cTotalRow.moveToFirst()) {
							dto.totalCustomer = cTotalRow.getInt(0);
						}
					}
				} catch (Exception e) {
					VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
				} finally {
					try {
						if (cTotalRow != null) {
							cTotalRow.close();
						}
					} catch (Exception ex) {
					}
				}
			}
			var1.append(" limit "
					+ Integer.toString(Constants.NUM_ITEM_PER_PAGE));
			var1.append(" offset "
					+ Integer.toString((page - 1) * Constants.NUM_ITEM_PER_PAGE));
		}
		

		Cursor c = null;
		try {
			c = rawQueries(var1.toString(), param);

			if (c != null) {
				if (c.moveToFirst()) {
					do {
						CustomerListItem item = new CustomerListItem();
						item.initDataCustomerOfC2FromCursor(c);

						dto.cusList.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
			}
		}

		return dto;
	}
  
	/**
	 * Lay danh sach khach hang la C2 trong tuyen cua NVBH
	 * @author: quangvt1
	 * @since: 17:43:51 05-05-2014
	 * @return: CustomerListDTO
	 * @throws:  
	 * @param staffId
	 * @param shopId
	 * @param page
	 * @param isGetTotalPage
	 * @return
	 */
	public CustomerListDTO getListC2CustomerOnPlan(long staffId, long shopId, int page, boolean isGetTotalPage) {
		String dateNow = DateUtils.now(); 
		CustomerListDTO dto = new CustomerListDTO();
		StringBuffer var1 = new StringBuffer();
		StringBuffer totalPageSql = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();

		var1.append("SELECT 		CT.CUSTOMER_ID          AS CUSTOMER_ID, ");
		var1.append("               CT.SHORT_CODE  AS CUSTOMER_CODE, ");
		var1.append("               CT.SHORT_CODE  AS SHORT_CODE, ");
		var1.append("               CT.CUSTOMER_NAME        AS CUSTOMER_NAME, ");
		var1.append("               CT.MOBIPHONE            AS MOBIPHONE, ");
		var1.append("               CT.NAME_TEXT    AS NAME_TEXT, ");
		var1.append("               CT.PHONE                AS PHONE, ");
		var1.append("               CT.LAT                  AS LAT, ");
		var1.append("               CT.LNG                  AS LNG, ");
		var1.append("               CT.CHANNEL_TYPE_ID      AS CHANNEL_TYPE_ID, ");
		var1.append("               CT.ADDRESS       AS ADDRESS, ");
		var1.append("               ( CT.HOUSENUMBER ");
		var1.append("                 || ' ' ");
		var1.append("                 || CT.STREET )        AS STREET ");
		var1.append("			FROM CUSTOMER CT, ");
		var1.append("           	 VISIT_PLAN VP, ");
		var1.append("                ROUTING RT, ");
		var1.append("                ROUTING_CUSTOMER RTC "); 
		var1.append("        WHERE  1 = 1 ");
		var1.append("       		AND VP.ROUTING_ID = RT.ROUTING_ID ");
		var1.append("               AND RTC.ROUTING_ID = RT.ROUTING_ID ");
		var1.append("               AND RTC.CUSTOMER_ID = CT.CUSTOMER_ID ");
		// Routing customer
		var1.append("               AND (DATE(RTC.END_DATE) >= DATE(?) OR DATE(RTC.END_DATE) IS NULL) and ");
		param.add(dateNow);
		var1.append("                DATE(RTC.START_DATE) <= DATE(?)");
		param.add(dateNow);  
		// Customer
		if (shopId > 0) {
			var1.append("               AND CT.SHOP_ID = ? ");
			param.add(String.valueOf(shopId));
		}
		var1.append("               AND CT.STATUS = 1 ");
		// Routing
		var1.append("               AND RT.STATUS = 1 ");
		// Visit plan
		var1.append("               AND VP.STAFF_ID = ? ");
		param.add(String.valueOf(staffId));;
		var1.append("               AND DATE(VP.FROM_DATE) <= DATE(?) ");
		param.add(dateNow);
		var1.append("               AND (DATE(VP.TO_DATE) >= DATE(?) OR DATE(VP.TO_DATE) IS NULL) ");
		param.add(dateNow);
		var1.append("               AND EXISTS (SELECT CHANNEL_TYPE_ID FROM CHANNEL_TYPE CHT "); 
		var1.append("               WHERE CHT.CHANNEL_TYPE_ID = CT.CHANNEL_TYPE_ID AND CHT.OBJECT_TYPE = 4 AND CHT.TYPE = 3 and CHT.STATUS = 1) "); // 4 - C2
		var1.append(" ORDER BY UPPER(CT.CUSTOMER_NAME) asc ");  
		

		Cursor cTotalRow = null;
		if (page > 0) {
			// get count
			if (isGetTotalPage) {
				totalPageSql.append("select count(*) as TOTAL_ROW from ("
						+ var1 + ")");
				try {
					cTotalRow = rawQueries(totalPageSql.toString(), param);
					if (cTotalRow != null) {
						if (cTotalRow.moveToFirst()) {
							dto.totalCustomer = cTotalRow.getInt(0);
						}
					}
				} catch (Exception e) {
					VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
				} finally {
					try {
						if (cTotalRow != null) {
							cTotalRow.close();
						}
					} catch (Exception ex) {
					}
				}
			}
			var1.append(" limit "
					+ Integer.toString(Constants.NUM_ITEM_PER_PAGE));
			var1.append(" offset "
					+ Integer.toString((page - 1) * Constants.NUM_ITEM_PER_PAGE));
		}
		

		Cursor c = null;
		try {
			c = rawQueries(var1.toString(), param);

			if (c != null) {
				if (c.moveToFirst()) {
					do {
						CustomerListItem item = new CustomerListItem();
						item.initDataCustomerOfC2FromCursor(c);

						dto.cusList.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
			}
		}

		return dto;
	}
	
	/**
	 * @author: duongdt3
	 * @since: 11:24:09 3 Jan 2014
	 * @return: NewCustomerListDTO
	 * @throws:  
	 * @param staffId
	 * @param shopId
	 * @param customerName
	 * @param numItemInPage
	 * @param currentIndexSpinnerState
	 * @param currentPage
	 * @return
	 * @throws Exception 
	 */
	public NewCustomerListDTO getListNewCustomer(String staffId, String shopId, String customerName, int numItemInPage, int currentIndexSpinnerState, int currentPage) throws Exception {
		NewCustomerListDTO dto = new NewCustomerListDTO();
		boolean isGetTotalItem = currentPage < 0;
		//neu <0 mac dinh lay trang 1
		numItemInPage = numItemInPage < 0 ? 1 : numItemInPage;
		
		StringBuffer var1 = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();
		
		var1.append("		SELECT * FROM (	");
		var1.append("		  SELECT CT.CUSTOMER_ID AS CUSTOMER_ID	");
		var1.append("			, CT.CUSTOMER_NAME AS CUSTOMER_NAME	");
		var1.append("			, CT.NAME_TEXT AS NAME_TEXT	");
		var1.append("			, CT.MOBIPHONE AS PHONE	");
		var1.append("			, CT.ADDRESS AS ADDRESS	");
		var1.append("			, (IFNULL(CT.HOUSENUMBER,'') || ' ' || IFNULL(CT.STREET,'')) AS STREET	");
		//Tráº¡ng thĂ¡i: ChÆ°a gá»­i SYN_STATE = 0  => 1
		var1.append("			, (CASE WHEN SYN_STATE = 0 THEN '1' 	");
		//Lá»—i SYN_STATE = 1 => 4
		var1.append("				ELSE (CASE WHEN SYN_STATE = 1 THEN '4' 	");
		//Chá»� duyá»‡t STATUS = 2 => 2
		var1.append("						ELSE (CASE WHEN STATUS = 2 THEN '2' 	");
		//Tá»« chá»‘i STATUS = 3 => 3
		var1.append("								ELSE (CASE WHEN STATUS = 3 THEN '3' 	");
		var1.append("									  END) 	");
		var1.append("							  END) 	");
		var1.append("					  END) 	");
		var1.append("			  END) AS STATE  	");
		var1.append("		  FROM CUSTOMER CT	");
		var1.append("		  WHERE 1 = 1	");
		var1.append("			AND ( CT.STATUS IN (2, 3) OR CT.SYN_STATE IN (0, 1) )  	");
		//chÆ°a Ä‘Æ°á»£c xĂ³a
		var1.append("			AND CT.STATUS NOT IN (-1)  	");
		var1.append("		   AND CT.STAFF_ID = ?	");
		//ADD PARAM STAFF_ID
		param.add(staffId);
		var1.append("		)	");
		var1.append("		WHERE 1 = 1	");
		if (currentIndexSpinnerState > 0) {
			var1.append("		   AND STATE = ? ");
			//ADD PARAM STATE
			param.add(String.valueOf(currentIndexSpinnerState));
		}
		
		if (!StringUtil.isNullOrEmpty(customerName)) {
			customerName = StringUtil
					.getEngStringFromUnicodeString(customerName);
			customerName = StringUtil.escapeSqlString(customerName);
			customerName = DatabaseUtils.sqlEscapeString("%" + customerName + "%");
			customerName = customerName.substring(1, customerName.length()-1);
			var1.append("	AND upper(NAME_TEXT) like upper(?) escape '^' ");
			param.add(customerName);
		}
		
		var1.append("		ORDER BY STATE ASC, upper(CUSTOMER_NAME) ASC	");
		
		String sql = var1.toString();
		String sqlTotal = StringUtil.getCountSql(sql);

		Cursor cTotalRow = null;
		try {
			if (isGetTotalItem) {
				cTotalRow = rawQueries(sqlTotal, param);
				if (cTotalRow != null) {
					if (cTotalRow.moveToFirst()) {
						do {
							dto.totalItem = cTotalRow.getInt(0);
						} while (cTotalRow.moveToNext());
					}
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			throw e;

		} finally {
			try {
				if (cTotalRow != null) {
					cTotalRow.close();
				}
			} catch (Exception ex) {
			}
		}
		Cursor c = null;
		try {
			// them phan trang cho sql
			sql += StringUtil.getPagingSql(numItemInPage, currentPage);
			c = rawQueries(sql, param);

			if (c != null) {
				if (c.moveToFirst()) {
					do {
						dto.AddCustomerItemFromCursor(c);

					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			throw e;

		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
			}
		}
		return dto;
	}

	/**
	 * láº¥y danh sĂ¡ch loáº¡i khĂ¡ch hĂ ng
	 * @author: duongdt3
	 * @since: 10:10:35 6 Jan 2014
	 * @return: ArrayList<CustomerType>
	 * @throws:  
	 * @return
	 * @throws Exception 
	 */
	public ArrayList<CustomerType> getListCustomerType() throws Exception {
		
		ArrayList<CustomerType> result = new ArrayList<CustomerType>();
		Cursor c = null;

		StringBuffer var1 = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();
		var1.append(" SELECT CHT.CHANNEL_TYPE_ID CHANNEL_TYPE_ID, CHT.CHANNEL_TYPE_NAME CHANNEL_TYPE_NAME, CHT.OBJECT_TYPE OBJECT_TYPE ");
		var1.append(" FROM CHANNEL_TYPE CHT ");
		var1.append(" WHERE 1=1 AND CHT.STATUS = 1 AND CHT.TYPE = 3 ");
		var1.append(" ORDER BY UPPER(CHT.CHANNEL_TYPE_NAME) ASC ");
		String sql = var1.toString();
		try {
			c = rawQueries(sql, param);
			
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						CustomerType type = new CustomerType();
						type.initFromCursor(c);
						result.add(type);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			throw e;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
			}
		}
		
		return result;
	}

	/**
	 * Láº¥y Ä‘á»‹a bĂ n cha (náº¿u xĂ£ láº¥y huyá»‡n, huyá»‡n láº¥y ra tá»‰nh)
	 * @author: duongdt3
	 * @since: 11:33:27 6 Jan 2014
	 * @return: int
	 * @throws:  
	 * @param idDistrict
	 * @return
	 * @throws Exception 
	 */
	public int getParrentAreaId(long idDistrict) throws Exception {
		int result = 0;
		Cursor c = null;

		StringBuffer var1 = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();
		var1.append(" SELECT a.PARENT_AREA_ID ID ");
		var1.append(" FROM AREA a ");
		var1.append(" WHERE 1=1 ");
		var1.append(" AND AREA_ID = ? ");
		param.add(String.valueOf(idDistrict));
		
		String sql = var1.toString();
		try {
			c = rawQueries(sql, param);
			
			if (c != null) {
				if (c.moveToFirst()) {
					 result = (int)StringUtil.getLongFromSQliteCursor(c, "ID");
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			throw e;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
			}
		}
		
		return result;
	}

	public static final int AREA_TYPE_PROVINE = 1;
	public static final int AREA_TYPE_DISTRICT = 2;
	public static final int AREA_TYPE_PRECINCT = 3;
	
	/**
	 * láº¥y danh sĂ¡ch Ä‘á»‹a bĂ n vá»›i loáº¡i vĂ  id Ä‘á»‹a bĂ n cha
	 * @author: duongdt3
	 * @since: 11:48:55 6 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param areaType {AREA_TYPE_PROVINE, AREA_TYPE_DISTRICT, AREA_TYPE_PRECINCT}
	 * @param idProvine
	 * @throws Exception 
	 */
	public ArrayList<AreaItem> getListArea(int areaType, long idProvine) throws Exception {
		ArrayList<AreaItem> result = new ArrayList<AreaItem>();
		Cursor c = null;

		StringBuffer var1 = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();
		var1.append(" SELECT a.PARENT_AREA_ID PARENT_AREA_ID, a.AREA_ID AREA_ID, a.AREA_NAME AREA_NAME");
		var1.append(" FROM AREA a ");
		var1.append(" WHERE 1=1 ");
		var1.append(" AND a.STATUS = 1 ");
		var1.append(" AND a.TYPE = ? ");
		param.add(String.valueOf(areaType));
		if (idProvine > 0) {
			var1.append(" AND a.PARENT_AREA_ID = ? ");
			param.add(String.valueOf(idProvine));
		}		
		var1.append(" ORDER BY UPPER(a.AREA_NAME) ASC ");
		String sql = var1.toString();
		try {
			c = rawQueries(sql, param);
			
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						AreaItem area = new AreaItem();
						area.initFromCursor(c);
						result.add(area);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			throw e;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
			}
		}
		return result;
	}

	/**
	 * láº¥y thĂ´ng tin XĂ£, Huyá»‡n, Tá»‰nh cá»§a area id
	 * @author: duongdt3
	 * @since: 13:39:36 7 Jan 2014
	 * @return: String
	 * @throws:  
	 * @param areaId
	 * @return
	 * @throws Exception 
	 */
	public String getAddressFromAreaId(int areaId) throws Exception {
		String result = "";
		Cursor c = null;

		StringBuffer var1 = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();
		var1.append(" SELECT IFNULL(a.PRECINCT_NAME || ', ', '') ||  IFNULL(a.DISTRICT_NAME || ', ', '') ||  IFNULL(a.PROVINCE_NAME, '') AS AREA_ADRRESS");
		var1.append(" FROM AREA a ");
		var1.append(" WHERE 1=1 ");
		var1.append(" AND a.AREA_ID = ? ");
		param.add(String.valueOf(areaId));
		
		String sql = var1.toString();
		try {
			c = rawQueries(sql, param);
			
			if (c != null) {
				if (c.moveToFirst()) {
					result = StringUtil.getStringFromSQliteCursor(c, "AREA_ADRRESS");
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			throw e;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
			}
		}
		return result;
	}

	/**
	 * ThĂªm cĂ¡c thĂ´ng tin thĂªm khi thĂªm, cáº­p nháº­t khĂ¡ch hĂ ng {Ä�á»‹a chá»‰, name_text}
	 * @author: duongdt3
	 * @since: 14:08:24 7 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param dto
	 * @throws Exception
	 */
	void addInfoForNewCustomer(CustomerDTO dto) throws Exception{
		//set láº¡i status = 2, cho cáº£ insert + upadate
		dto.setStatus(2);
		
		String areaAddress = getAddressFromAreaId(dto.getAreaId());
		String houseNumer = (StringUtil.isNullOrEmpty(dto.houseNumber) ? ""
				: dto.houseNumber + ", ");
		String street = (StringUtil.isNullOrEmpty(dto.street) ? ""
				: dto.street + ", ");
		// ná»‘i chuá»—i Ä‘á»‹a chá»‰
		String address = houseNumer + street + areaAddress;
		dto.setAddress(address);

		// ná»‘i chuá»—i name text = tĂªn KH + Ä‘á»‹a chá»‰, thĂ nh khĂ´ng dáº¥u, in hoa
		String nameText = dto.getCustomerName() + " - " + address;
		nameText = StringUtil.getEngStringFromUnicodeString(nameText).toUpperCase();
		dto.setNameText(nameText);
	}
	
	/**
	 * staff cáº­p nháº­t khĂ¡ch hĂ ng
	 * @author: duongdt3
	 * @since: 14:02:36 7 Jan 2014
	 * @return: long
	 * @throws:  
	 * @param dto
	 * @return
	 * @throws Exception 
	 */
	public long updateCustomer(CustomerDTO dto) throws Exception {
		//thĂªm Ä‘á»‹a chá»‰ + name text
		addInfoForNewCustomer(dto);
		
		ContentValues value = initUpdateDataRow(dto);
		String[] params = { "" + dto.getCustomerId() };
		return update(value, CUSTOMER_ID + " = ?", params);
	}

	/**
	 * staff thĂªm khĂ¡ch hĂ ng
	 * @author: duongdt3
	 * @since: 14:05:01 7 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param dto
	 * @return 
	 * @throws Exception 
	 */
	public long insertCustomer(CustomerDTO dto) throws Exception {
		TABLE_ID idTable = new TABLE_ID(mDB);
		// táº¡o id
		dto.customerId = idTable.getMaxId(CUSTOMER_TABLE);
		// táº¡o luĂ´n customer code = id, Ä‘á»ƒ trĂ¡nh null
		dto.customerCode = String.valueOf(dto.customerId);
		//thĂªm Ä‘á»‹a chá»‰ + name text
		addInfoForNewCustomer(dto);

		return insert(dto);
	}

	/**
	 * staff xĂ³a khĂ¡ch hĂ ng 
	 * @author: duongdt3
	 * @since: 14:11:30 7 Jan 2014
	 * @return: long
	 * @throws:  
	 * @param dto
	 * @return
	 */
	public long deleteCustomer(CustomerDTO dto) {
		//xĂ³a khĂ¡ch hĂ ng nĂ y, set status = -1
		dto.setStatus(-1);
		ContentValues value = initDeleteDataRow(dto);
		String[] params = { "" + dto.getCustomerId() };
		return update(value, CUSTOMER_ID + " = ?", params);
	} 

	/**
	 * @author: dungdq3
	 * @since: 10:26:51 AM Mar 7, 2014
	 * @return: CustomerListDTO
	 * @throws:  
	 * @param staffId
	 * @param shopId
	 * @param isGetWrongPlan
	 * @param page
	 * @param isGetTotalPage
	 * @param type
	 * @return
	 */
	public CustomerListDTO getListCustomerForOpponentSale(long staffId, long shopId, String dataSearch,
			String visitPlan, boolean isGetWrongPlan, int page, boolean isGetTotalPage, int type) {
		// TODO Auto-generated method stub
		CustomerListDTO dto = null;
		String daySeq = "";
		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
		ArrayList<String> shopList = shopTable.getShopRecursiveReverse(String.valueOf(shopId));
		String shopStr = TextUtils.join(",", shopList);
		String dateNow=DateUtils.now();
		if (!StringUtil.isNullOrEmpty(visitPlan)) {
			if (visitPlan.equals(Constants.DAY_LINE[0])) {
				daySeq = "2";
			} else if (visitPlan.equals(Constants.DAY_LINE[1])) {
				daySeq = "3";
			} else if (visitPlan.equals(Constants.DAY_LINE[2])) {
				daySeq = "4";
			} else if (visitPlan.equals(Constants.DAY_LINE[3])) {
				daySeq = "5";
			} else if (visitPlan.equals(Constants.DAY_LINE[4])) {
				daySeq = "6";
			} else if (visitPlan.equals(Constants.DAY_LINE[5])) {
				daySeq = "7";
			} else if (visitPlan.equals(Constants.DAY_LINE[6])) {
				daySeq = "8";
			}
		}
		dto = new CustomerListDTO();
		double distance = getDistanceOrder(null, String.valueOf(shopId), String.valueOf(staffId));
		//dto.distance

		StringBuffer var1 = new StringBuffer();
		StringBuffer totalPageSql = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();

		var1.append("SELECT * ");
		var1.append("FROM   (SELECT VP.VISIT_PLAN_ID        AS VISIT_PLAN_ID, ");
		var1.append("               VP.SHOP_ID              AS SHOP_ID, ");
		var1.append("               VP.STAFF_ID             AS STAFF_ID, ");
		var1.append("               VP.FROM_DATE            AS FROM_DATE, ");
		var1.append("               VP.TO_DATE              AS TO_DATE, ");
		var1.append("               S.SHOP_CODE              AS SHOP_CODE, ");
		var1.append("               RT.ROUTING_ID           AS ROUTING_ID, ");
		var1.append("               RT.ROUTING_CODE         AS ROUTING_CODE, ");
		var1.append("               RT.ROUTING_NAME         AS ROUTING_NAME, ");
		var1.append("               CT.CUSTOMER_ID          AS CUSTOMER_ID, ");
		var1.append("               CT.DISTANCE_ORDER          AS DISTANCE_ORDER, ");
		var1.append("               CT.SHORT_CODE  AS CUSTOMER_CODE, ");
		var1.append("               CT.SHORT_CODE  AS SHORT_CODE, ");
		var1.append("               CT.CUSTOMER_NAME        AS CUSTOMER_NAME, ");
		var1.append("               S.DISTANCE_ORDER   AS     SHOP_DISTANCE, ");
		var1.append("               CT.MOBIPHONE            AS MOBIPHONE, ");
		var1.append("               CT.NAME_TEXT    AS NAME_TEXT, ");
		var1.append("               CT.PHONE                AS PHONE, ");
		var1.append("               CT.LAT                  AS LAT, ");
		var1.append("               CT.LNG                  AS LNG, ");
		var1.append("               CT.CHANNEL_TYPE_ID      AS CHANNEL_TYPE_ID, ");
		var1.append("               EXCEPTION_ORDER_DATE      AS EXCEPTION_ORDER_DATE, ");
		var1.append("               CT.ADDRESS       AS ADDRESS, ");
		var1.append("               ( CT.HOUSENUMBER ");
		var1.append("                 || ' ' ");
		var1.append("                 || CT.STREET )        AS STREET, ");
		var1.append("               ( CASE ");
		var1.append("                   WHEN RTC.MONDAY = 1 THEN 'T2' ");
		var1.append("                   ELSE '' ");
		var1.append("                 END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.TUESDAY = 1 THEN ',T3' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.WEDNESDAY = 1 THEN ',T4' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.THURSDAY = 1 THEN ',T5' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.FRIDAY = 1 THEN ',T6' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.SATURDAY = 1 THEN ',T7' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END ");
		var1.append("                 || CASE ");
		var1.append("                      WHEN RTC.SUNDAY = 1 THEN ',CN' ");
		var1.append("                      ELSE '' ");
		var1.append("                    END )              AS CUS_PLAN, ");
		var1.append("               ( CASE ");
		var1.append("                   WHEN RTC.SEQ2 = 0 THEN '' ");
		var1.append("                   ELSE RTC.SEQ2 ");
		var1.append("                 END ) ");
		var1.append("               || ( CASE ");
		var1.append("                      WHEN RTC.SEQ3 = 0 THEN '' ");
		var1.append("                      ELSE ',' ");
		var1.append("                           || RTC.SEQ3 ");
		var1.append("                    END ) ");
		var1.append("               || ( CASE ");
		var1.append("                      WHEN RTC.SEQ4 = 0 THEN '' ");
		var1.append("                      ELSE ',' ");
		var1.append("                           || RTC.SEQ4 ");
		var1.append("                    END ) ");
		var1.append("               || ( CASE ");
		var1.append("                      WHEN RTC.SEQ5 = 0 THEN '' ");
		var1.append("                      ELSE ',' ");
		var1.append("                           || RTC.SEQ5 ");
		var1.append("                    END ) ");
		var1.append("               || ( CASE ");
		var1.append("                      WHEN RTC.SEQ6 = 0 THEN '' ");
		var1.append("                      ELSE ',' ");
		var1.append("                           || RTC.SEQ6 ");
		var1.append("                    END ) ");
		var1.append("               || ( CASE ");
		var1.append("                      WHEN RTC.SEQ7 = 0 THEN '' ");
		var1.append("                      ELSE ',' ");
		var1.append("                           || RTC.SEQ7 ");
		var1.append("                    END ) ");
		var1.append("               || ( CASE ");
		var1.append("                      WHEN RTC.SEQ8 = 0 THEN '' ");
		var1.append("                      ELSE ',' ");
		var1.append("                           || RTC.SEQ8 ");
		var1.append("                    END )              AS PLAN_SEQ, ");
		var1.append("               RTC.ROUTING_CUSTOMER_ID AS ROUTING_CUSTOMER_ID ");

		if (!StringUtil.isNullOrEmpty(daySeq)) {
			var1.append("  			, RTC.SEQ" + daySeq + " as DAY_SEQ");
		}

		var1.append("        FROM   VISIT_PLAN VP, ");
		var1.append("               ROUTING RT, ");
		var1.append("               (SELECT ROUTING_CUSTOMER_ID, ");
		var1.append("                       ROUTING_ID, ");
		var1.append("                       CUSTOMER_ID as CUID, ");
		var1.append("                       STATUS, ");
		var1.append("                       MONDAY, ");
		var1.append("                       TUESDAY, ");
		var1.append("                       WEDNESDAY, ");
		var1.append("                       THURSDAY, ");
		var1.append("                       FRIDAY, ");
		var1.append("                       SATURDAY, ");
		var1.append("                       SUNDAY, ");
		var1.append("                       SEQ2, ");
		var1.append("                       SEQ3, ");
		var1.append("                       SEQ4, ");
		var1.append("                       SEQ5, ");
		var1.append("                       SEQ6, ");
		var1.append("                       SEQ7, ");
		var1.append("                       SEQ8, ");
		var1.append("                       WEEK1, ");
		var1.append("                       WEEK2, ");
		var1.append("                       WEEK3, ");
		var1.append("                       WEEK4 ");
		var1.append("                FROM   ROUTING_CUSTOMER");
		var1.append("                WHERE (DATE(END_DATE) >= DATE(?) OR DATE(END_DATE) IS NULL) and ");
		param.add(dateNow);
		var1.append("                DATE(START_DATE) <= DATE(?) ");
		param.add(dateNow);  
		var1.append("                ) RTC  ");
		var1.append("		left join 	( SELECT SC.CUSTOMER_ID as SCCUS, strftime('%d/%m/%Y',EXCEPTION_ORDER_DATE) as EXCEPTION_ORDER_DATE FROM STAFF_CUSTOMER SC WHERE SC.STAFF_ID= ? ) ");
		param.add(String.valueOf(staffId));
		var1.append("			ON SCCUS=CUID	");
		var1.append("               ,CUSTOMER CT, SHOP S ");
		if(type==1){
			var1.append("		,(select CHANNEL_TYPE_ID as CHID from CHANNEL_TYPE CH where type=3 and OBJECT_TYPE =1 and status=1	) ");
		}
		var1.append("        WHERE  VP.ROUTING_ID = RT.ROUTING_ID ");
		var1.append("		and s.status=1 and rt.status=1 ");
		var1.append("               AND RTC.ROUTING_ID = RT.ROUTING_ID ");
		var1.append("               AND RTC.CUID = CT.CUSTOMER_ID ");
		var1.append("               AND DATE(VP.FROM_DATE) <= DATE(?) ");
		param.add(dateNow);
		var1.append("				and RT.TYPE=?	");
		param.add(String.valueOf(type));
		if (type==0 && shopId > 0) {
			var1.append("               AND S.SHOP_ID = CT.SHOP_ID ");
			var1.append("               AND CT.SHOP_ID = ? ");
			param.add(String.valueOf(shopId));
		}else if (type==1){
			if(!StringUtil.isNullOrEmpty(shopStr)){
				var1.append("               AND CT.SHOP_ID in ( "+ shopStr  +" )");
			}
			var1.append(" and	CT.CHANNEL_TYPE_ID= CHID ");
		}
		
		var1.append("               AND (DATE(VP.TO_DATE) >= DATE(?) OR DATE(VP.TO_DATE) IS NULL) ");
		param.add(dateNow);
		var1.append("               AND VP.STAFF_ID = ? ");
		param.add(String.valueOf(staffId));
		var1.append("               AND CT.STATUS = 1 Group by CUID) CUS ");
		var1.append("       LEFT JOIN (SELECT STAFF_ID, ");
		var1.append("                         CUSTOMER_ID               AS CUSTOMER_ID_1, ");
		var1.append("                         GROUP_CONCAT(OBJECT_TYPE) AS ACTION ");
		var1.append("                  FROM   ACTION_LOG ");
		var1.append("                  WHERE  STAFF_ID = ? ");
		param.add(String.valueOf(staffId));
		var1.append("                         AND DATE(START_TIME) = DATE(?) ");
		param.add(dateNow);
		var1.append("                         AND OBJECT_TYPE IN ( 2, 3, 4 ) ");
		var1.append("                  GROUP  BY CUSTOMER_ID) ACTION ");
		var1.append("              ON CUS.CUSTOMER_ID = ACTION.CUSTOMER_ID_1 ");
		var1.append("       LEFT JOIN (SELECT STAFF_ID, ");
		var1.append("                         ACTION_LOG_ID AS VISIT_ACT_LOG_ID, ");
		var1.append("                         CUSTOMER_ID AS CUSTOMER_ID_2, ");
		var1.append("                         OBJECT_TYPE AS VISIT, ");
		var1.append("                         START_TIME, ");
		var1.append("                         END_TIME, ");
		var1.append("                         LAT AS VISITED_LAT, ");
		var1.append("                         LNG AS VISITED_LNG, ");
		var1.append("                         IS_OR ");
		var1.append("                  FROM   ACTION_LOG ");
		var1.append("                  WHERE  STAFF_ID = ? ");
		param.add(String.valueOf(staffId));
		var1.append("                         AND DATE(START_TIME) = DATE(?) ");
		param.add(dateNow);
		var1.append("                         AND OBJECT_TYPE IN ( 0, 1 ) ");
		var1.append("                  GROUP  BY CUSTOMER_ID) VISIT ");
		var1.append("              ON VISIT.CUSTOMER_ID_2 = CUS.CUSTOMER_ID ");
		
		var1.append("       LEFT JOIN (select OP_SALE_VOLUME_ID, ");
		var1.append("                         CUSTOMER_ID AS OP_SALE_CUSID ");
		var1.append("                         from OP_SALE_VOLUME ");
		var1.append("                         where Date(SALE_DATE)=Date(?) ");
		param.add(dateNow);
		var1.append("                         and staff_id=? group by CUSTOMER_ID) OPSALE ");
		param.add(String.valueOf(staffId));
		var1.append("              ON OP_SALE_CUSID = CUS.CUSTOMER_ID ");
		var1.append("       LEFT JOIN (select OP_STOCK_TOTAL_ID, ");
		var1.append("                         CUSTOMER_ID AS OP_STOCK_CUSID ");
		var1.append("                         from OP_STOCK_TOTAL ");
		var1.append("                         where Date(INVENTORY_DATE)=Date(?) ");
		param.add(dateNow);
		var1.append("                         and staff_id=? group by CUSTOMER_ID) OPSTOCK ");
		param.add(String.valueOf(staffId));
		var1.append("              ON OP_STOCK_CUSID = CUS.CUSTOMER_ID ");
		var1.append("       LEFT JOIN (SELECT SO.SALE_ORDER_ID, SO.CUSTOMER_ID as  CUSTOMER_ID_4");
		var1.append("             FROM   SALE_ORDER SO ");
		var1.append("             WHERE  SO.STAFF_ID = ? ");
		param.add(String.valueOf(staffId));
		var1.append("             AND IFNULL(DATE(SO.ORDER_DATE) >= ");
		var1.append("                  DATE(?, ");
		param.add(dateNow);
		var1.append("                       'start of month', ");
		var1.append("                       '-2 month'), 0)  GROUP BY CUSTOMER_ID ) KHT ");
		var1.append("        ON KHT.CUSTOMER_ID_4 = CUS.CUSTOMER_ID ");
		
		var1.append("       LEFT JOIN (SELECT CUSTOMER_ID CUSTOMER_ID_5, count(*) TOTAL_PG");
		var1.append("             FROM   TIME_KEEPER ");
		var1.append("             WHERE  STAFF_OWNER_ID = ? ");
		param.add(String.valueOf(staffId));
		var1.append("             AND DATE(CREATE_DATE) = DATE(?) GROUP BY CUSTOMER_ID ) TK ");
		param.add(dateNow);
		var1.append("        ON TK.CUSTOMER_ID_5 = CUS.CUSTOMER_ID ");
		var1.append("       LEFT JOIN (");
		var1.append(" SELECT count(PGVP.STAFF_ID) AS SUM_PG, PGVP.CUSTOMER_ID AS CUSTOMERID"); 
		var1.append(" FROM PG_VISIT_PLAN PGVP, STAFF ST "); 
		var1.append(" WHERE 1 = 1 ");
		var1.append(" 	AND PGVP.STAFF_ID = ST.STAFF_ID ");
		var1.append(" 	AND PGVP.PARENT_STAFF_ID = ? ");
		var1.append(" 	AND PGVP.STATUS = 1 AND ST.STATUS=1 ");
		param.add(String.valueOf(staffId));
		var1.append(" 	AND PGVP.SHOP_ID = ? ");
		param.add(GlobalInfo.getInstance().getProfile().getUserData().shopId);
		var1.append(" 	AND DATE(PGVP.FROM_DATE) <= DATE(?) ");
		param.add(dateNow);
		var1.append(" 	AND IFNULL(DATE(PGVP.TO_DATE) >= DATE(?), 1) ");
		param.add(dateNow);
		var1.append("   group by PGVP.CUSTOMER_ID) PGVP ON CUS.CUSTOMER_ID=PGVP.CUSTOMERID");

		var1.append(" WHERE 1 = 1");
		if (!StringUtil.isNullOrEmpty(visitPlan)) {// ko phai chon tat ca
			if (isGetWrongPlan) {
				var1.append("	and (upper(CUS_PLAN) like upper(?) OR IS_OR IN (0,1) )");
				param.add("%" + visitPlan + "%");
			} else {
				var1.append("	and upper(CUS_PLAN) like upper(?) ");
				param.add("%" + visitPlan + "%");
			}
		}
		if (!StringUtil.isNullOrEmpty(dataSearch)) {
			dataSearch = StringUtil
					.getEngStringFromUnicodeString(dataSearch);
			dataSearch = StringUtil.escapeSqlString(dataSearch);
			dataSearch = DatabaseUtils.sqlEscapeString("%" + dataSearch + "%");
			dataSearch=dataSearch.substring(1, dataSearch.length()-1);
			// tim theo truong ten va dia chi
			var1.append("	and (upper(NAME_TEXT) like upper(?) escape '^' ");
			param.add(dataSearch);
			// tim theo dt co dinh
			var1.append("	or upper(PHONE) like upper(?) escape '^' ");
			param.add(dataSearch);
			// tim theo ma khach hang
			var1.append("	or upper(SHORT_CODE) like upper(?) escape '^' ");
			param.add(dataSearch);
			// tim theo so dtdd
			var1.append("	or upper(MOBIPHONE) like upper(?) escape '^' )");
			param.add(dataSearch);
			
		}

		if (!StringUtil.isNullOrEmpty(daySeq)) {
				var1.append("   order by VISIT asc, DAY_SEQ asc, SHORT_CODE asc, CUSTOMER_NAME asc ");
		} else {
			var1.append("   order by VISIT asc, SHORT_CODE asc, CUSTOMER_NAME asc ");
		}

		Cursor cTotalRow = null;
		if (page > 0) {
			// get count
			if (isGetTotalPage) {
				totalPageSql.append("select count(*) as TOTAL_ROW from ("
						+ var1 + ")");
				try {
					cTotalRow = rawQueries(totalPageSql.toString(), param);
					if (cTotalRow != null) {
						if (cTotalRow.moveToFirst()) {
							dto.totalCustomer = cTotalRow.getInt(0);
						}
					}
				} catch (Exception e) {
					VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
				} finally {
					try {
						if (cTotalRow != null) {
							cTotalRow.close();
						}
					} catch (Exception ex) {
					}
				}
			}
			var1.append(" limit "
					+ Integer.toString(Constants.NUM_ITEM_PER_PAGE));
			var1.append(" offset "
					+ Integer.toString((page - 1) * Constants.NUM_ITEM_PER_PAGE));
		}
		
		Cursor c = null;
		try {
			c = rawQueries(var1.toString(), param);

			if (c != null) {
				if (c.moveToFirst()) {
					do {
						CustomerListItem item = new CustomerListItem();
						item.initDataFromCursor(c, distance, visitPlan);

						dto.cusList.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
			}
		}

		return dto;
	}

	/**
	 * Lay danh sach khach hang trong tuyen va C2
	 * @author: quangvt1
	 * @since: 15:38:55 06-05-2014
	 * @return: CustomerListDTO
	 * @throws:  
	 * @param staffId
	 * @param shopId
	 * @param dataSearch
	 * @param visitPlan
	 * @param isGetWrongPlan
	 * @param page
	 * @param isGetTotalPage
	 * @param type
	 * @return
	 */
	public CustomerListDTO getListCustomerOnPlanAndOfC2(long staffId,
														long shopId, String dataSearch, String visitPlan,
			boolean isGetWrongPlan, int page, boolean isGetTotalPage, int type) {
		CustomerListDTO dto = null;
		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
		ArrayList<String> shopList = shopTable.getShopRecursiveReverse(String.valueOf(shopId));
		String shopStr = TextUtils.join(",", shopList);
		String dateNow = DateUtils.now(); 
		 
		StringBuffer var1 = new StringBuffer();
		StringBuffer totalPageSql = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();

		var1.append("SELECT * ");
		var1.append("FROM  (  "); 
		
		//  Danh sach khach hang trong tuyen
		var1.append("SELECT CUSTOMER_ID,  ");
		var1.append("		CUSTOMER_CODE, ");  
		var1.append("		SHORT_CODE, ");  
		var1.append("		CUSTOMER_NAME, ");  
		var1.append("		MOBIPHONE, ");  
		var1.append("		NAME_TEXT, ");  
		var1.append("		PHONE, ");  
		var1.append("		LAT, ");  
		var1.append("		LNG, ");  
		var1.append("		CHANNEL_TYPE_ID, ");  
		var1.append("		ADDRESS, ");  
		var1.append("		STREET ");  
		var1.append("FROM   ( "); 
		var1.append("		(SELECT CT.CUSTOMER_ID          AS CUSTOMER_ID, ");
		var1.append("               CT.SHORT_CODE  AS CUSTOMER_CODE, ");
		var1.append("               CT.SHORT_CODE  AS SHORT_CODE, ");
		var1.append("               CT.CUSTOMER_NAME        AS CUSTOMER_NAME, "); 
		var1.append("               CT.MOBIPHONE            AS MOBIPHONE, ");
		var1.append("               CT.NAME_TEXT    AS NAME_TEXT, ");
		var1.append("               CT.PHONE                AS PHONE, ");
		var1.append("               CT.LAT                  AS LAT, ");
		var1.append("               CT.LNG                  AS LNG, ");
		var1.append("               CT.CHANNEL_TYPE_ID      AS CHANNEL_TYPE_ID, ");
		var1.append("               CT.ADDRESS       AS ADDRESS, ");
		var1.append("               ( CT.HOUSENUMBER ");
		var1.append("                 || ' ' ");
		var1.append("                 || CT.STREET )        AS STREET ");
		var1.append("        FROM   VISIT_PLAN VP, ");
		var1.append("               ROUTING RT, ");
		var1.append("               (SELECT ROUTING_CUSTOMER_ID, ");
		var1.append("                       ROUTING_ID, ");
		var1.append("                       CUSTOMER_ID as CUID, ");
		var1.append("                       STATUS, ");
		var1.append("                       MONDAY, ");
		var1.append("                       TUESDAY, ");
		var1.append("                       WEDNESDAY, ");
		var1.append("                       THURSDAY, ");
		var1.append("                       FRIDAY, ");
		var1.append("                       SATURDAY, ");
		var1.append("                       SUNDAY, ");
		var1.append("                       SEQ2, ");
		var1.append("                       SEQ3, ");
		var1.append("                       SEQ4, ");
		var1.append("                       SEQ5, ");
		var1.append("                       SEQ6, ");
		var1.append("                       SEQ7, ");
		var1.append("                       SEQ8, ");
		var1.append("                       WEEK1, ");
		var1.append("                       WEEK2, ");
		var1.append("                       WEEK3, ");
		var1.append("                       WEEK4 ");
		var1.append("                FROM   ROUTING_CUSTOMER");
		var1.append("                WHERE (DATE(END_DATE) >= DATE(?) OR DATE(END_DATE) IS NULL) and ");
		param.add(dateNow);
		var1.append("                DATE(START_DATE) <= DATE(?) ");
		param.add(dateNow); 
		var1.append("                ) RTC  ");
		var1.append("		left join 	( SELECT SC.CUSTOMER_ID as SCCUS, strftime('%d/%m/%Y',EXCEPTION_ORDER_DATE) as EXCEPTION_ORDER_DATE FROM STAFF_CUSTOMER SC WHERE SC.STAFF_ID= ? ) ");
		param.add(String.valueOf(staffId));
		var1.append("			ON SCCUS=CUID	");
		var1.append("               ,CUSTOMER CT, SHOP S ");
		if(type==1){
			var1.append("		,(select CHANNEL_TYPE_ID as CHID from CHANNEL_TYPE CH where type=3 and OBJECT_TYPE =1 and status=1	) ");
		}
		var1.append("        WHERE  VP.ROUTING_ID = RT.ROUTING_ID ");
		var1.append("		and s.status=1 and rt.status=1 ");
		var1.append("               AND RTC.ROUTING_ID = RT.ROUTING_ID ");
		var1.append("               AND RTC.CUID = CT.CUSTOMER_ID ");
		var1.append("               AND DATE(VP.FROM_DATE) <= DATE(?) ");
		param.add(dateNow);
		var1.append("				and RT.TYPE=?	");
		param.add(String.valueOf(type));
		if (type==0 && shopId > 0) {
			var1.append("               AND S.SHOP_ID = CT.SHOP_ID ");
			var1.append("               AND CT.SHOP_ID = ? ");
			param.add(String.valueOf(shopId));
		}else if (type==1){
			if(!StringUtil.isNullOrEmpty(shopStr)){
				var1.append("               AND CT.SHOP_ID in ( "+ shopStr  +" )");
			}
			var1.append(" and	CT.CHANNEL_TYPE_ID= CHID ");
		}
		
		var1.append("               AND (DATE(VP.TO_DATE) >= DATE(?) OR DATE(VP.TO_DATE) IS NULL) ");
		param.add(dateNow);
		var1.append("               AND VP.STAFF_ID = ? ");
		param.add(String.valueOf(staffId));
		var1.append("               AND CT.STATUS = 1 Group by CUID) CUS ");
		var1.append("       LEFT JOIN (SELECT STAFF_ID, ");
		var1.append("                         CUSTOMER_ID               AS CUSTOMER_ID_1, ");
		var1.append("                         GROUP_CONCAT(OBJECT_TYPE) AS ACTION ");
		var1.append("                  FROM   ACTION_LOG ");
		var1.append("                  WHERE  STAFF_ID = ? ");
		param.add(String.valueOf(staffId));
		var1.append("                         AND DATE(START_TIME) = DATE(?) ");
		param.add(dateNow);
		var1.append("                         AND OBJECT_TYPE IN ( 2, 3, 4 ) ");
		var1.append("                  GROUP  BY CUSTOMER_ID) ACTION ");
		var1.append("              ON CUS.CUSTOMER_ID = ACTION.CUSTOMER_ID_1 ");
		var1.append("       LEFT JOIN (SELECT STAFF_ID, ");
		var1.append("                         ACTION_LOG_ID AS VISIT_ACT_LOG_ID, ");
		var1.append("                         CUSTOMER_ID AS CUSTOMER_ID_2, ");
		var1.append("                         OBJECT_TYPE AS VISIT, ");
		var1.append("                         START_TIME, ");
		var1.append("                         END_TIME, ");
		var1.append("                         LAT AS VISITED_LAT, ");
		var1.append("                         LNG AS VISITED_LNG, ");
		var1.append("                         IS_OR ");
		var1.append("                  FROM   ACTION_LOG ");
		var1.append("                  WHERE  STAFF_ID = ? ");
		param.add(String.valueOf(staffId));
		var1.append("                         AND DATE(START_TIME) = DATE(?) ");
		param.add(dateNow);
		var1.append("                         AND OBJECT_TYPE IN ( 0, 1 ) ");
		var1.append("                  GROUP  BY CUSTOMER_ID) VISIT ");
		var1.append("              ON VISIT.CUSTOMER_ID_2 = CUS.CUSTOMER_ID ");
		
		var1.append("       LEFT JOIN (select OP_SALE_VOLUME_ID, ");
		var1.append("                         CUSTOMER_ID AS OP_SALE_CUSID ");
		var1.append("                         from OP_SALE_VOLUME ");
		var1.append("                         where Date(SALE_DATE)=Date(?) ");
		param.add(dateNow);
		var1.append("                         and staff_id=? group by CUSTOMER_ID) OPSALE ");
		param.add(String.valueOf(staffId));
		var1.append("              ON OP_SALE_CUSID = CUS.CUSTOMER_ID ");
		var1.append("       LEFT JOIN (select OP_STOCK_TOTAL_ID, ");
		var1.append("                         CUSTOMER_ID AS OP_STOCK_CUSID ");
		var1.append("                         from OP_STOCK_TOTAL ");
		var1.append("                         where Date(INVENTORY_DATE)=Date(?) ");
		param.add(dateNow);
		var1.append("                         and staff_id=? group by CUSTOMER_ID) OPSTOCK ");
		param.add(String.valueOf(staffId));
		var1.append("              ON OP_STOCK_CUSID = CUS.CUSTOMER_ID ");
		var1.append("       LEFT JOIN (SELECT SO.SALE_ORDER_ID, SO.CUSTOMER_ID as  CUSTOMER_ID_4");
		var1.append("             FROM   SALE_ORDER SO ");
		var1.append("             WHERE  SO.STAFF_ID = ? ");
		param.add(String.valueOf(staffId));
		var1.append("             AND IFNULL(DATE(SO.ORDER_DATE) >= ");
		var1.append("                  DATE(?, ");
		param.add(dateNow);
		var1.append("                       'start of month', ");
		var1.append("                       '-2 month'), 0)  GROUP BY CUSTOMER_ID ) KHT ");
		var1.append("        ON KHT.CUSTOMER_ID_4 = CUS.CUSTOMER_ID ");
		
		var1.append("       LEFT JOIN (SELECT CUSTOMER_ID CUSTOMER_ID_5, count(*) TOTAL_PG");
		var1.append("             FROM   TIME_KEEPER ");
		var1.append("             WHERE  STAFF_OWNER_ID = ? ");
		param.add(String.valueOf(staffId));
		var1.append("             AND DATE(CREATE_DATE) = DATE(?) GROUP BY CUSTOMER_ID ) TK ");
		param.add(dateNow);
		var1.append("        ON TK.CUSTOMER_ID_5 = CUS.CUSTOMER_ID ");
		var1.append("       LEFT JOIN (");
		var1.append(" SELECT count(PGVP.STAFF_ID) AS SUM_PG, PGVP.CUSTOMER_ID AS CUSTOMERID"); 
		var1.append(" FROM PG_VISIT_PLAN PGVP, STAFF ST "); 
		var1.append(" WHERE 1 = 1 ");
		var1.append(" 	AND PGVP.STAFF_ID = ST.STAFF_ID ");
		var1.append(" 	AND PGVP.PARENT_STAFF_ID = ? ");
		var1.append(" 	AND PGVP.STATUS = 1 AND ST.STATUS=1 ");
		param.add(String.valueOf(staffId));
		var1.append(" 	AND PGVP.SHOP_ID = ? ");
		param.add(GlobalInfo.getInstance().getProfile().getUserData().shopId);
		var1.append(" 	AND DATE(PGVP.FROM_DATE) <= DATE(?) ");
		param.add(dateNow);
		var1.append(" 	AND IFNULL(DATE(PGVP.TO_DATE) >= DATE(?), 1) ");
		param.add(dateNow);
		var1.append("   group by PGVP.CUSTOMER_ID) PGVP ON CUS.CUSTOMER_ID=PGVP.CUSTOMERID )"); 
		// Ket thuc danh sach khach hang trong tuyen  
		
		// Union voi danh sach khach hang cua C2 
		var1.append("   union ");
		
		var1.append("    SELECT * ");
		var1.append("		FROM   (SELECT ");
		var1.append("               CT.CUSTOMER_ID          AS CUSTOMER_ID, ");
		var1.append("               CT.SHORT_CODE  AS CUSTOMER_CODE, ");
		var1.append("               CT.SHORT_CODE  AS SHORT_CODE, ");
		var1.append("               CT.CUSTOMER_NAME        AS CUSTOMER_NAME, "); 
		var1.append("               CT.MOBIPHONE            AS MOBIPHONE, ");
		var1.append("               CT.NAME_TEXT    AS NAME_TEXT, ");
		var1.append("               CT.PHONE                AS PHONE, ");
		var1.append("               CT.LAT                  AS LAT, ");
		var1.append("               CT.LNG                  AS LNG, ");
		var1.append("               CT.CHANNEL_TYPE_ID      AS CHANNEL_TYPE_ID, ");
		var1.append("               CT.ADDRESS       AS ADDRESS, ");
		var1.append("               ( CT.HOUSENUMBER ");
		var1.append("                 || ' ' ");
		var1.append("                 || CT.STREET )        AS STREET "); 
		var1.append("			FROM CUSTOMER CT ");
		var1.append("        WHERE  1 = 1 "); 
		if (shopId > 0) {
			var1.append("               AND CT.SHOP_ID = ? ");
			param.add(String.valueOf(shopId));
		}
		var1.append("               AND CT.STATUS = 1 ");
		var1.append("               AND CT.PARENT_CUSTOMER_ID in ");
		
		// Danh sach khach hang c2
//		var1.append(" ( SELECT CT.CUSTOMER_ID          AS CUSTOMER_ID "); 
//		var1.append("	FROM CUSTOMER CT ");
//		var1.append("   WHERE  1 = 1 ");
//		if (shopId > 0) {
//			var1.append("               AND CT.SHOP_ID = ? ");
//			param.add(String.valueOf(shopId));
//		}
//		var1.append("               AND CT.STATUS = 1 ");
//		var1.append("               AND EXISTS (SELECT CHANNEL_TYPE_ID FROM CHANNEL_TYPE CHT "); 
//		var1.append("               WHERE CHT.CHANNEL_TYPE_ID = CT.CHANNEL_TYPE_ID AND CHT.OBJECT_TYPE = 4)) ");
		
		var1.append("(SELECT 		CT.CUSTOMER_ID          AS CUSTOMER_ID "); 
		var1.append("			FROM CUSTOMER CT, ");
		var1.append("           	 VISIT_PLAN VP, ");
		var1.append("                ROUTING RT, ");
		var1.append("                ROUTING_CUSTOMER RTC "); 
		var1.append("        WHERE  1 = 1 ");
		var1.append("       		AND VP.ROUTING_ID = RT.ROUTING_ID ");
		var1.append("               AND RTC.ROUTING_ID = RT.ROUTING_ID ");
		var1.append("               AND RTC.CUSTOMER_ID = CT.CUSTOMER_ID ");
		// Routing customer
		var1.append("               AND (DATE(RTC.END_DATE) >= DATE(?) OR DATE(RTC.END_DATE) IS NULL) and ");
		param.add(dateNow);
		var1.append("                DATE(RTC.START_DATE) <= DATE(?)");
		param.add(dateNow);  
		// Customer
		if (shopId > 0) {
			var1.append("               AND CT.SHOP_ID = ? ");
			param.add(String.valueOf(shopId));
		}
		var1.append("               AND CT.STATUS = 1 ");
		// Routing
		var1.append("               AND RT.STATUS = 1 ");
		// Visit plan
		var1.append("               AND VP.STAFF_ID = ? ");
		param.add(String.valueOf(staffId));;
		var1.append("               AND DATE(VP.FROM_DATE) <= DATE(?) ");
		param.add(dateNow);
		var1.append("               AND (DATE(VP.TO_DATE) >= DATE(?) OR DATE(VP.TO_DATE) IS NULL) ");
		param.add(dateNow);
		var1.append("               AND EXISTS (SELECT CHANNEL_TYPE_ID FROM CHANNEL_TYPE CHT "); 
		var1.append("               WHERE CHT.CHANNEL_TYPE_ID = CT.CHANNEL_TYPE_ID AND CHT.OBJECT_TYPE = 4 AND CHT.TYPE = 3 and CHT.STATUS = 1)) "); // 4 - C2
		 
		var1.append("   group by CT.CUSTOMER_ID) )");
		// Ket thuc danh sach khach hang cua c2

		var1.append(" WHERE 1 = 1");
		 
		if (!StringUtil.isNullOrEmpty(dataSearch)) {
			dataSearch = StringUtil
					.getEngStringFromUnicodeString(dataSearch);
			dataSearch = StringUtil.escapeSqlString(dataSearch);
			dataSearch = DatabaseUtils.sqlEscapeString("%" + dataSearch + "%");
			dataSearch = dataSearch.substring(1, dataSearch.length()-1);
			// tim theo truong ten va dia chi
			var1.append("	and (upper(NAME_TEXT) like upper(?) escape '^' ");
			param.add(dataSearch);
			// tim theo dt co dinh
			var1.append("	or upper(PHONE) like upper(?) escape '^' ");
			param.add(dataSearch);
			// tim theo ma khach hang
			var1.append("	or upper(SHORT_CODE) like upper(?) escape '^' ");
			param.add(dataSearch);
			// tim theo so dtdd
			var1.append("	or upper(MOBIPHONE) like upper(?) escape '^' )");
			param.add(dataSearch);
			
		}
 
		var1.append("   order by SHORT_CODE asc, CUSTOMER_NAME asc "); 
		
		dto = new CustomerListDTO();

		Cursor cTotalRow = null; 
		if (page > 0) {
			// get count
			if (isGetTotalPage) {
				totalPageSql.append("select count(*) as TOTAL_ROW from ("
						+ var1 + ")");
				try {
					cTotalRow = rawQueries(totalPageSql.toString(), param);
					if (cTotalRow != null) {
						if (cTotalRow.moveToFirst()) {
							dto.totalCustomer = cTotalRow.getInt(0);
						}
					}
				} catch (Exception e) {
					VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
				} finally {
					try {
						if (cTotalRow != null) {
							cTotalRow.close();
						}
					} catch (Exception ex) {
					}
				}
			}
			var1.append(" limit "
					+ Integer.toString(Constants.NUM_ITEM_PER_PAGE));
			var1.append(" offset "
					+ Integer.toString((page - 1) * Constants.NUM_ITEM_PER_PAGE));
		}
		
		Cursor c = null;
		try {
			c = rawQueries(var1.toString(), param);

			if (c != null) {
				if (c.moveToFirst()) {
					do {
						CustomerListItem item = new CustomerListItem();
						// item.initDataFromCursor(c, dto.distance, visitPlan);
						item.initDataCustomerOfC2FromCursor(c);
						dto.cusList.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
			}
		}

		return dto;
	}

	public int getGetAreaIdFromCode(String areaCode) {
		int areaId = 0;
		ArrayList<AreaItem> result = new ArrayList<AreaItem>();
		Cursor c = null;

		StringBuffer var1 = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();
		var1.append(" SELECT a.AREA_ID AREA_ID ");
		var1.append(" FROM AREA a ");
		var1.append(" WHERE 1=1 ");
		var1.append(" AND a.STATUS = 1 ");
		var1.append(" AND a.TYPE = ? ");
		param.add(String.valueOf(AREA_TYPE_PRECINCT));
		var1.append(" AND a.AREA_CODE = ? ");
		param.add(areaCode);

		String sql = var1.toString();
		try {
			c = rawQueries(sql, param);

			if (c != null) {
				if (c.moveToFirst()) {
					do {
						areaId = c.getInt(0);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
			}
		}
		return areaId;
	}

	/**
	 * Lấy danh sách khách hàng màn hình huấn luyện GSNPP, GSBH
	 * @param ownerId
	 * @param shopId
	 * @param staffId
	 * @param cusCode
	 * @param cusNameAdd
	 * @param visitPlan
     * @param page
     * @return
     */
	public CustomerListDTO requestGetCustomerSaleListTraining(String id, String shopIdParent, String ownerId, String shopId, String staffId, String cusCode,
													  String cusNameAdd, String visitPlan, int page, boolean isAll) {
		CustomerListDTO dto = null;
		String dateNow=DateUtils.now();
		String daySeq = "";
		String dateNowActionLog=DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		if (!StringUtil.isNullOrEmpty(visitPlan)) {
			if (visitPlan.equals(Constants.DAY_LINE[0])) {
				daySeq = "2";
			} else if (visitPlan.equals(Constants.DAY_LINE[1])) {
				daySeq = "3";
			} else if (visitPlan.equals(Constants.DAY_LINE[2])) {
				daySeq = "4";
			} else if (visitPlan.equals(Constants.DAY_LINE[3])) {
				daySeq = "5";
			} else if (visitPlan.equals(Constants.DAY_LINE[4])) {
				daySeq = "6";
			} else if (visitPlan.equals(Constants.DAY_LINE[5])) {
				daySeq = "7";
			} else if (visitPlan.equals(Constants.DAY_LINE[6])) {
				daySeq = "8";
			}
		}
		double distance = 0;
		distance = getDistanceOrder(null, shopIdParent, id);
		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
		ArrayList<String> shopList = shopTable.getListShopStaffGroup(id, ownerId);
		String shopStr = TextUtils.join(",", shopList);

		StringBuffer totalPageSql = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();
		StringBuffer sqlString = new StringBuffer();
		sqlString.append("SELECT TMP.*, max(SYN_STATE) ");
		sqlString.append("FROM   (SELECT VP.visit_plan_id                 AS VISIT_PLAN_ID, ");
		sqlString.append("               VP.shop_id                       AS SHOP_ID, ");
		sqlString.append("               VP.staff_id                      AS STAFF_ID, ");
		sqlString.append("               VISIT.VISIT_ACT_LOG_ID           as VISIT_ACT_LOG_ID, ");
		sqlString.append("               VISIT.VISIT                      as VISIT, ");
		sqlString.append("               VISIT.START_TIME                 as START_TIME, ");
		sqlString.append("               VISIT.END_TIME                   as END_TIME, ");
		sqlString.append("               CT.customer_id                   AS CUSTOMER_ID, ");
		sqlString.append("               Substr(CT.customer_code, 1, 3)   AS CUSTOMER_CODE, ");
		sqlString.append("               CT.customer_name                 AS CUSTOMER_NAME, ");
		sqlString.append("               CT.short_code                 	  AS SHORT_CODE, ");
		sqlString.append("               CT.name_text                 	  AS CUSTOMER_NAME_TEXT, ");
		sqlString.append("               CT.lat                           AS LAT, ");
		sqlString.append("               CT.lng                           AS LNG, ");
		sqlString.append("               CT.address                       AS ADDRESS, ");
		sqlString.append("               SH.shop_name                     AS SHOP_NAME, ");
		sqlString.append("               st.[staff_code]                  AS STAFF_CODE, ");
		sqlString.append("               st.[staff_name]                  AS STAFF_NAME, ");
		sqlString.append("               sc.[STAFF_CUSTOMER_ID]           AS STAFF_CUSTOMER_ID, ");
		sqlString.append("               STRFTIME('%d/%m/%Y', sc.exception_order_date)  AS EXCEPTION_ORDER_DATE, ");
		sqlString.append("               sc.SYN_STATE                     AS SYN_STATE, ");
		sqlString.append("               ( CT.housenumber ");
		sqlString.append("                 || ' ' ");
		sqlString.append("                 || CT.street )                 AS STREET, ");
		sqlString.append("               ( CASE ");
		sqlString.append("                   WHEN RTC.monday = 1 THEN 'T2' ");
		sqlString.append("                   ELSE '' ");
		sqlString.append("                 end ");
		sqlString.append("                 || CASE ");
		sqlString.append("                      WHEN RTC.tuesday = 1 THEN ',T3' ");
		sqlString.append("                      ELSE '' ");
		sqlString.append("                    end ");
		sqlString.append("                 || CASE ");
		sqlString.append("                      WHEN RTC.wednesday = 1 THEN ',T4' ");
		sqlString.append("                      ELSE '' ");
		sqlString.append("                    end ");
		sqlString.append("                 || CASE ");
		sqlString.append("                      WHEN RTC.thursday = 1 THEN ',T5' ");
		sqlString.append("                      ELSE '' ");
		sqlString.append("                    end ");
		sqlString.append("                 || CASE ");
		sqlString.append("                      WHEN RTC.friday = 1 THEN ',T6' ");
		sqlString.append("                      ELSE '' ");
		sqlString.append("                    end ");
		sqlString.append("                 || CASE ");
		sqlString.append("                      WHEN RTC.saturday = 1 THEN ',T7' ");
		sqlString.append("                      ELSE '' ");
		sqlString.append("                    end ");
		sqlString.append("                 || CASE ");
		sqlString.append("                      WHEN RTC.sunday = 1 THEN ',CN' ");
		sqlString.append("                      ELSE '' ");
		sqlString.append("                    end )                       AS TUYEN, ");
		// check co vi tri hay khong HAVE_POSITION
		sqlString.append("              ( CASE ");
		sqlString.append("                     WHEN (CT.LAT <= 0 OR CT.LNG <= 0) THEN 0 ");
		sqlString.append("                     ELSE 1 ");
		sqlString.append("                   END )       AS HAVE_POSITION, ");

		if (!StringUtil.isNullOrEmpty(daySeq)) {
			sqlString.append("  			 RTC.SEQ" + daySeq + " as DAY_SEQ, ");
		}

		// LAY SO LAN UPDATE VI TRI, NGAY CAP NHAT VI TRI
		sqlString.append("               (SELECT Count(1) ");
		sqlString.append("                FROM   customer_position_log cpl ");
		sqlString.append("                WHERE  cpl.[customer_id] = ct.customer_id ");
		sqlString.append("                       AND cpl.[lat] > 0 ");
		sqlString.append("                       AND cpl.[lng] > 0)       AS NUM_UPDATE, ");
		sqlString.append("               Strftime('%d/%m/%Y', (SELECT cpl.create_date ");
		sqlString.append("                                     FROM   customer_position_log cpl ");
		sqlString.append("                                     WHERE  cpl.[customer_id] = ct.customer_id ");
		sqlString.append("                                            AND cpl.[lat] > 0 ");
		sqlString.append("                                            AND cpl.[lng] > 0 ");
		sqlString.append("                                     ORDER  BY create_date DESC ");
		sqlString.append("                                     LIMIT  1)) AS LAST_UPDATE ");
		sqlString.append("        FROM   visit_plan VP, ");
		sqlString.append("               routing RT, ");
		sqlString.append("               (SELECT routing_customer_id, ");
		sqlString.append("                       routing_id, ");
		sqlString.append("                       customer_id, ");
		sqlString.append("                       status, ");
		sqlString.append("                       monday, ");
		sqlString.append("                       tuesday, ");
		sqlString.append("                       wednesday, ");
		sqlString.append("                       thursday, ");
		sqlString.append("                       friday, ");
		sqlString.append("                       saturday, ");
		sqlString.append("                       sunday, ");
		sqlString.append("                       seq, ");
		sqlString.append("                       seq2, ");
		sqlString.append("                       seq3, ");
		sqlString.append("                       seq4, ");
		sqlString.append("                       seq5, ");
		sqlString.append("                       seq6, ");
		sqlString.append("                       seq7, ");
		sqlString.append("                       seq8 ");
		sqlString.append("                FROM   routing_customer ");
		sqlString.append("                WHERE (DATE(END_DATE) >= DATE(?) OR DATE(END_DATE) IS NULL) and ");
		param.add(dateNow);
		sqlString.append("                DATE(START_DATE) <= DATE(?)) RTC, ");
		param.add(dateNow);
		sqlString.append("               customer CT, SHOP SH  ");
		// LAY THONG TIN NHANVINE
		sqlString.append("               JOIN staff st ");
		sqlString.append("                 ON VP.staff_id = st.staff_id ");
		sqlString.append("                    AND st.status = 1 ");
		if(isAll){
			sqlString.append("                    AND st.staff_owner_id in ( "+ ownerId  +" )" );
		}else {
			sqlString.append("                    AND st.staff_owner_id = ? ");
			param.add(ownerId);
		}
		// THONG TIN KHACH HANG
		sqlString.append("               LEFT JOIN staff_customer sc ");
		sqlString.append("                      ON (sc.staff_id = VP.staff_id ");
		sqlString.append("                      	and sc.customer_id = RTC.customer_id)");
		sqlString.append("       LEFT JOIN (SELECT STAFF_ID, ");
		sqlString.append("                         ACTION_LOG_ID AS VISIT_ACT_LOG_ID, ");
		sqlString.append("                         CUSTOMER_ID AS CUSTOMER_ID_2, ");
		sqlString.append("                         OBJECT_TYPE AS VISIT, ");
		sqlString.append("                         START_TIME, ");
		sqlString.append("                         END_TIME, ");
		sqlString.append("                         LAT AS VISITED_LAT, ");
		sqlString.append("                         LNG AS VISITED_LNG, ");
		sqlString.append("                         IS_OR ");
		sqlString.append("                  FROM   ACTION_LOG ");
		sqlString.append("                  WHERE  1 = 1 ");
		sqlString.append("                         AND STAFF_ID = ? ");
		param.add(id);
		sqlString.append("                         AND substr(START_TIME,0,11) = ? ");
		param.add(dateNowActionLog);
		sqlString.append("                         AND OBJECT_TYPE IN ( 0, 1 ) ");
		sqlString.append("                  GROUP  BY CUSTOMER_ID) VISIT ");
		sqlString.append("              ON VISIT.CUSTOMER_ID_2 = CT.CUSTOMER_ID ");
		sqlString.append("        WHERE  1 = 1 ");
		sqlString.append("               AND VP.routing_id = RT.routing_id ");
		sqlString.append("               AND RTC.routing_id = RT.routing_id ");
		sqlString.append("               AND RTC.customer_id = CT.customer_id ");
		sqlString.append("               AND Ifnull(Date(VP.from_date) <= Date(?), 0) ");
		param.add(dateNow);
		sqlString.append("               AND Ifnull(Date(VP.to_date) >= Date(?), 1) ");
		param.add(dateNow);
		sqlString.append("               AND CT.status = 1 ");
		sqlString.append("               AND CT.shop_id = SH.shop_id ");
		if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSNPP) {
			sqlString.append("               AND VP.shop_id = ? ");
			param.add(shopId);
		}else{
			sqlString.append("               AND VP.shop_id in ( "+ shopStr  +" )" );
		}
		sqlString.append("               AND VP.status in (0,1) ");
		sqlString.append("               AND RT.status = 1 ");
		sqlString.append("               ) AS TMP ");
		sqlString.append("WHERE  1 = 1 ");
		if (!StringUtil.isNullOrEmpty(visitPlan)) {
			sqlString.append("       AND Upper(TUYEN) LIKE Upper(?) ");
			param.add("%" + visitPlan + "%");
		}
		if (!StringUtil.isNullOrEmpty(staffId)) {
			sqlString.append("       AND STAFF_ID = ? ");
			param.add(staffId);
		}
		// customer code search
		if (!StringUtil.isNullOrEmpty(cusCode)) {
			cusCode = StringUtil.escapeSqlString(cusCode);
			cusCode = DatabaseUtils.sqlEscapeString("%" + cusCode + "%");
			cusCode = cusCode.substring(1,cusCode.length()-1);
			sqlString.append("	and upper(CUSTOMER_CODE) like upper(?) escape '^' ");
			param.add(cusCode);
		}
		// customer name search
		if (!StringUtil.isNullOrEmpty(cusNameAdd)) {
			cusNameAdd = StringUtil.escapeSqlString(cusNameAdd);
			cusNameAdd = DatabaseUtils.sqlEscapeString("%" + cusNameAdd + "%");
			cusNameAdd = cusNameAdd.substring(1, cusNameAdd.length()-1);
			sqlString.append("	and ((upper(CUSTOMER_NAME_TEXT) like upper(?) escape '^') ");
			param.add(cusNameAdd);
			sqlString.append(" or (upper(STREET) like upper(?) escape '^')");
			param.add(cusNameAdd);
			sqlString.append(" or (upper(ADDRESS) like upper(?) escape '^'))");
			param.add(cusNameAdd);
		}
		// group theo staff_id, customer_id lay syn_state lon nhat
//		sqlString.append("group by staff_id, customer_id ");
		sqlString.append("group by customer_id ");
		// thu tu order
		sqlString.append("order by VISIT, STAFF_CODE asc, STAFF_NAME asc, CUSTOMER_CODE asc, CUSTOMER_NAME asc, HAVE_POSITION asc ");
		Cursor cTotalRow = null;
		try {
			if (page > 0) {
				totalPageSql.append("select count(1) as TOTAL_ROW from ("
						+ sqlString + ")");
				cTotalRow = rawQueries(totalPageSql.toString(), param);
				if (dto == null) {
					dto = new CustomerListDTO();
				}
				if (cTotalRow != null) {
					if (cTotalRow.moveToFirst()) {
						dto.totalCustomer = cTotalRow.getInt(0);
					}
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			try {
				if (cTotalRow != null) {
					cTotalRow.close();
				}
			} catch (Exception ex) {
			}
		}
		Cursor c = null;
		try {
			sqlString.append(" limit ? offset ? ");
			param.add(Integer.toString(Constants.NUM_ITEM_PER_PAGE));
			param.add(Integer.toString((page - 1) * Constants.NUM_ITEM_PER_PAGE));
			c = rawQueries(sqlString.toString(), param);
			if (c != null) {
				if (dto == null) {
					dto = new CustomerListDTO();
				}
				if (c.moveToFirst()) {
					do {
						CustomerListItem item = new CustomerListItem();
						item.initDataCustomerSaleItemTraining(c, distance);
						dto.cusList.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
			}
		}
		return dto;
	}

}
