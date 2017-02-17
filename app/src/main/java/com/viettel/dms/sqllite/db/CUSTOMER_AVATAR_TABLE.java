/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.CustomerAvatarDTO;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * table CustomerAvatar
 * 
 * @author: dungdq3
 * @version: 1.0
 * @since: 1.0
 */
public class CUSTOMER_AVATAR_TABLE extends ABSTRACT_TABLE {

	// id npp
	public static final String SHOP_ID = "SHOP_ID";
	// ma NPP
	public static final String CUSTOMER_AVATAR_ID = "CUSTOMER_AVATAR_ID";
	// ten NPP
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	// id NPP cha
	public static final String MEDIA_ITEM_ID = "MEDIA_ITEM_ID";
	// duong
	public static final String URL = "URL";
	// phuong
	public static final String STATUS = "STATUS";
	// quan/huyen
	public static final String CREATE_DATE = "CREATE_DATE";
	// tinh
	public static final String CREATE_USER = "CREATE_USER";
	// ma vung
	public static final String UPDATE_USER = "UPDATE_USER";
	// nuoc
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// ten bang
	public static final String STAFFID = "STAFF_ID";
	// ten bang
	public static final String CUSTOMER_AVATAR_TABLE = "CUSTOMER_AVATAR";

	@Override
	protected long insert(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected long delete(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	public CUSTOMER_AVATAR_TABLE(SQLiteDatabase mDB) {
		this.tableName = CUSTOMER_AVATAR_TABLE;
		this.columns = new String[] { SHOP_ID, CREATE_USER, UPDATE_USER,
				CUSTOMER_ID, URL, CREATE_DATE, UPDATE_DATE, CUSTOMER_AVATAR_ID,
				MEDIA_ITEM_ID, STATUS, SYN_STATE };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;
	}

	/**
	 * Cap nhat status=0 khi delete avatar
	 * 
	 * @author: dungdq3
	 * @param: Tham số của hàm
	 * @return: long
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 * @date: Jan 4, 2014
	 */
	public long update(CustomerAvatarDTO customerAvatarDTO) {
		// TODO Auto-generated method stub
		ContentValues value = initParam(customerAvatarDTO, 2);
		String[] params = { "" + customerAvatarDTO.getCustomerID() };
		return update(value, CUSTOMER_ID + " = ?", params);
	}

	/**
	 * Cập nhật link hinh ảnh mới cho khách hang
	 * 
	 * @author: dungdq3
	 * @param: CustomerAvatarDTO customerAvatarDTO
	 * @return: long
	 * @date: Jan 6, 2014
	 */
	public long updateURL(CustomerAvatarDTO customerAvatarDTO) {
		// TODO Auto-generated method stub
		ContentValues value = initParam(customerAvatarDTO, 2);
		String[] params = { "" + customerAvatarDTO.getCustomerAvatarID() };
		return update(value, CUSTOMER_AVATAR_ID + " = ?", params);
	}

	/**
	 * Them mới avatar
	 * 
	 * @author: dungdq3
	 * @param: CustomerAvatarDTO customerAvatarDTO
	 * @return: long
	 * @date: Jan 6, 2014
	 */
	public long insertAvatar(CustomerAvatarDTO customerAvatarDTO) {
		// TODO Auto-generated method stub
		long success = -1;
		try {
			if (customerAvatarDTO != null) {
				TABLE_ID tableId = new TABLE_ID(mDB);
				long maxID = tableId.getMaxId(CUSTOMER_AVATAR_TABLE);
				customerAvatarDTO.setMaxID(maxID);
				ContentValues cv = initParam(customerAvatarDTO, 1);
				success = insert(null, cv);
			}
		} catch (Exception ex) {
			success = -1;
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
		return success;
	}

	/**
	 * tạo các param để thực thi query 
	 * 
	 * @author: dungdq3
	 * @param: CustomerAvatarDTO customerAvatarDTO
	 * @param: updateOrInsert
	 * @return: long
	 * @date: Jan 6, 2014
	 */
	private ContentValues initParam(CustomerAvatarDTO customerAvatarDTO,
			int updateOrInsert) {
		// TODO Auto-generated method stub
		// updateOrInsert==1 la insert, ==2 la update
		ContentValues editedValues = new ContentValues();
		String dateNow= DateUtils.now();
		if (updateOrInsert == 1) {
			editedValues.put(CREATE_USER, customerAvatarDTO.getCreateUser());
			editedValues.put(CUSTOMER_AVATAR_ID, customerAvatarDTO.getMaxID());
			editedValues.put(CREATE_DATE, dateNow);
		} else {
			editedValues.put(UPDATE_USER, customerAvatarDTO.getCreateUser());
			editedValues.put(UPDATE_DATE, dateNow);
		}
		if (!StringUtil.isNullOrEmpty(customerAvatarDTO.getMediaItemID())) {
			editedValues.put(MEDIA_ITEM_ID, customerAvatarDTO.getMediaItemID());
		}
		if (!StringUtil.isNullOrEmpty(customerAvatarDTO.getURL())) {
			editedValues.put(URL, customerAvatarDTO.getURL());
		}

		if (!StringUtil.isNullOrEmpty(customerAvatarDTO.getShopID())) {
			editedValues.put(SHOP_ID, customerAvatarDTO.getShopID());
		}
		if (!StringUtil.isNullOrEmpty(customerAvatarDTO.getCustomerID())) {
			editedValues.put(CUSTOMER_ID, customerAvatarDTO.getCustomerID());
		}
		if (!StringUtil.isNullOrEmpty(customerAvatarDTO.getStatus())) {
			editedValues.put(STATUS, customerAvatarDTO.getStatus());
		}
		if (!StringUtil.isNullOrEmpty(customerAvatarDTO.getStaffID())) {
			editedValues.put(STAFFID, customerAvatarDTO.getStaffID());
		}
		return editedValues;
	}

	/**
	* Lay giá trị max id avatar cua khach hang
	* @author: dungdq3
	* @param: Tham số của hàm
	* @return: long
	* @throws: Ngoại lệ do hàm đưa ra (nếu có)
	* @date: Jan 6, 2014
	*/
	public long getMaxCustomerAvatarID() {
		// TODO Auto-generated method stub
		TABLE_ID tableId = new TABLE_ID(mDB);
		long maxID = tableId.getMaxId(CUSTOMER_AVATAR_TABLE);
		return maxID;
	}
}
