/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.CustomerInputQuantityDTO;
import com.viettel.dms.dto.db.CustomerInputQuantityDetailDTO;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

 
/**
 * Thong tin san luong ban cua khach hang qua cac chu ki cua CT HTBH
 * PRO_CUS_PROCESS_TABLE.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  14:51:49 19-06-2014
 */
public class PRO_CUS_PROCESS_TABLE extends ABSTRACT_TABLE {
	// id 
	public static final String PRO_CUS_PROCESS_ID = "PRO_CUS_PROCESS_ID";
	// ma chuong trinh
	public static final String PRO_INFO_ID = "PRO_INFO_ID";
	// PRO_CUS_MAP_ID
	public static final String PRO_CUS_MAP_ID = "PRO_CUS_MAP_ID";
	// PRO_CUS_MAP_ID
	public static final String PRO_PERIOD_ID = "PRO_CUS_MAP_ID";
	// ma san pham
	public static final String PRODUCT_ID = "PRODUCT_ID";
	// so luong ban do nhan vien nhap
	public static final String QUANTITY = "QUANTITY";
	// OBJECT_ID
	public static final String OBJECT_ID = "OBJECT_ID";
	// OBJECT_TYPE
	public static final String OBJECT_TYPE = "OBJECT_TYPE"; 
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE"; 
	// So lieu mua tu nha phan phoi
	public static final String QUANTITY_SHOP = "QUANTITY_SHOP"; 
	// So luong ban tong top tu SMS PG
	public static final String QUANTITY_PG = "QUANTITY_PG"; 
	
	public static final String TABLE_NAME = "PRO_CUS_PROCESS";

	public PRO_CUS_PROCESS_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_NAME;
		this.columns = new String[] { PRO_CUS_PROCESS_ID, PRO_INFO_ID,
				PRO_CUS_MAP_ID, PRO_PERIOD_ID, PRODUCT_ID, QUANTITY, OBJECT_ID,
				OBJECT_TYPE, CREATE_USER, UPDATE_USER,
				CREATE_DATE, UPDATE_DATE, QUANTITY_SHOP, QUANTITY_PG, SYN_STATE };
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
	 * Cap nhat san luong thuc hien
	 * @author: quangvt1
	 * @since: 10:23:33 25-06-2014
	 * @return: long
	 * @throws:  
	 * @param detail
	 * @param staffCode
	 * @return
	 */
	public long updateQuantityDone(CustomerInputQuantityDetailDTO detail, String staffCode) { 
		ContentValues value = new ContentValues();
		value.put(QUANTITY, detail.NEWQUANTITY);
		value.put(UPDATE_USER, staffCode);
		value.put(UPDATE_DATE, DateUtils.now());
		
		String[] params = { "" + detail.PRO_CUS_PROCESS_ID };
		return update(value, PRO_CUS_PROCESS_ID + " = ?", params);
	}
 
	/**
	 * Cap nhat san luong thuc thuc cho cac san pham cu khach hang trong 1 chu ki
	 * @author: quangvt1
	 * @since: 10:23:53 25-06-2014
	 * @return: boolean
	 * @throws:  
	 * @param customer
	 * @param staffCode
	 * @return
	 */
	public boolean saveDoneProduct(CustomerInputQuantityDTO customer, String staffCode) {
		boolean isSuccess = true;
		for (CustomerInputQuantityDetailDTO detail : customer.details) {
			if(updateQuantityDone(detail, staffCode) <= 0){
				isSuccess = false;
				break;
			}
		}
		return isSuccess;
	}

	/**
	 * Lay danh sach cac san pham cung chu ki can cap nhat
	 * @author: quangvt1
	 * @since: 10:23:09 25-06-2014
	 * @return: CustomerInputQuantityDTO
	 * @throws:  
	 * @param customer
	 * @param listProductInput  
	 * @return
	 */
	public CustomerInputQuantityDTO getListProductSamePreriod(CustomerInputQuantityDTO customer, Map<Long, Long> listProductInput) {
		CustomerInputQuantityDetailDTO cusdetail = customer.details.get(0);
		List<String> listProductId = new ArrayList<String>();
		for (CustomerInputQuantityDetailDTO detail : customer.details) {
			listProductId.add(detail.PRODUCT_ID + "");
		}
		String productStr = TextUtils.join(",", listProductId);
		
		ArrayList<String> params = new ArrayList<String>();
		StringBuffer  var1 = new StringBuffer();
		var1.append("SELECT PC.PRO_CUS_PROCESS_ID, ");
		var1.append("       PC.PRODUCT_ID ");
		var1.append("FROM   PRO_CUS_PROCESS PC, ");
		var1.append("       PRO_CUS_HISTORY PH, ");
		var1.append("       PRO_PERIOD PR ");
		var1.append("WHERE  1 = 1 ");
		var1.append("       AND PC.PRO_INFO_ID <> ? ");
		params.add(cusdetail.PRO_INFO_ID + "");
		var1.append("       AND PC.OBJECT_ID = ? ");
		params.add(cusdetail.OBJECT_ID + "");
		var1.append("       AND PH.PRO_CUS_MAP_ID = PC.PRO_CUS_MAP_ID ");
		var1.append("       AND PH.TYPE = 1 ");
		var1.append("       AND PH.FROM_DATE <= PR.FROM_DATE ");
		var1.append("       AND ( PH.TO_DATE IS NULL ");
		var1.append("              OR PH.TO_DATE >= PR.TO_DATE ) ");
		var1.append("       AND PR.PRO_PERIOD_ID = PC.PRO_PERIOD_ID ");
		var1.append("       AND DATE(PR.FROM_DATE) = DATE(?) ");
		params.add(cusdetail.PERIOD_FROM_DATE);
		var1.append("       AND DATE(PR.TO_DATE) = DATE(?) ");
		params.add(cusdetail.PERIOD_TO_DATE);
		var1.append("       AND PC.PRODUCT_ID IN ( " + productStr + " ) "); 
		
		Cursor c = null; 
		CustomerInputQuantityDTO cusDTO = new CustomerInputQuantityDTO();
		try {  
			c = rawQueries(var1.toString(), params);
			if (c != null) {  
				if (c.moveToFirst()) { 
					do { 
						CustomerInputQuantityDetailDTO detail = new CustomerInputQuantityDetailDTO();
						detail.initFromCursorForListSamePreriod(c); 
						
						if(listProductInput.containsKey(detail.PRODUCT_ID)){
							detail.NEWQUANTITY = listProductInput.get(detail.PRODUCT_ID);
							cusDTO.details.add(detail); 
						}
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
			} catch (Exception e2) {
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e2));
			}
		}
		
		return cusDTO;
	}
}