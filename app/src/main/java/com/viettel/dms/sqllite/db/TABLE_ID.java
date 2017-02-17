/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.Vector;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.TableIdDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * Luu max id cua cac table
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class TABLE_ID extends ABSTRACT_TABLE {
	// id bang
	public static final String ID = "ID";
	// ten table
	public static final String TABLE_NAME = "TABLE_NAME";
	// max id
	public static final String MAX_ID = "MAX_ID";
	// factor -- bo sung khi tao id moi
	public static final String FACTOR = "FACTOR";
	// last syn
	public static final String LAST_SYN_MAX_ID = "LAST_SYN_MAX_ID";
	// id NPP
	public static final String SHOP_ID = "SHOP_ID";
	// staffId
	public static final String STAFF_ID = "STAFF_ID";

	public static final String TABLE_ID_NAME = "TABLE_ID";

	public TABLE_ID(SQLiteDatabase mDB) {
		this.tableName = TABLE_ID_NAME;
		this.columns = new String[] { ID, TABLE_NAME, MAX_ID, FACTOR, LAST_SYN_MAX_ID, SHOP_ID, STAFF_ID, SYN_STATE };
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
		ContentValues value = initDataRow((TableIdDTO) dto);
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
	public long insert(TableIdDTO dto) {
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
		TableIdDTO tableId = (TableIdDTO) dto;
		ContentValues value = initDataRow(tableId);
		String[] params = { "" + tableId.id };
		return update(value, ID + " = ?", params);
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
		return delete(ID + " = ?", params);
	}

	public long delete(AbstractTableDTO dto) {
		TableIdDTO cusDTO = (TableIdDTO) dto;
		String[] params = { String.valueOf(cusDTO.id) };
		return delete(ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: DoanDM replaced
	 * @param id
	 * @return: DisplayPrdogrameLvDTO
	 * @throws:
	 */
	public TableIdDTO getRowById(String id) {
		TableIdDTO dto = null;
		Cursor c = null;
		try {
			String[] params = { id };
			c = query(ID + " = ?", params, null, null, null);
			if (c != null) {
				if (c.moveToFirst()) {
					dto = initLogDTOFromCursor(c);
				}
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
		return dto;
	}

	/**
	 * Lay maxId cua table
	 * 
	 * @author: TruongHN
	 * @param tableName
	 * @return: long
	 * @throws:
	 */
	public long getMaxIdFromTableName(String tableName) {
		long res = -1;
		Cursor c = null;
		try {
			String[] params = { tableName };
			String sql = "select MAX_ID as MAX_ID from TABLE_ID where TABLE_NAME like ?";
			c = rawQuery(sql, params);
			if (c != null) {
				if (c.moveToFirst()) {
					res = c.getLong(c.getColumnIndex(MAX_ID));
				}
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

		return res;
	}

	/**
	 * Lay max_id trong table
	 * 
	 * @author: TruongHN
	 * @param tableName
	 * @return: long
	 * @throws:
	 */
	public long getMaxId(String tableName) {
		long staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
		long maxId = getMaxIdFromTable(tableName, staffId);
		return maxId;
	}

	/**
	 * Lay max_id trong table
	 * 
	 * @author: BangHN
	 * @param tableName
	 *            : ten bang lay max id
	 * @param objectId : Thong thuong truyen vao STAFF_ID cua nhan vien
	 *           mot so truong hop dac biet co the la CUSTOMER_ID,...
	 * @return: long
	 */
	public long getMaxId(String tableName, long objectId) {
		long maxId = getMaxIdFromTable(tableName, objectId);
		return maxId;
	}

	/**
	 * Lay max_id tu table va staff_id
	 * 
	 * @author: TruongHN
	 * @param tableName
	 * @param objectId : Thong thuong se la STAFF_ID
	 * mot so truong hop dat biet objectId co the la CUSTOEMR_ID
	 * @return: long
	 * @throws:
	 */
	private long getMaxIdFromTable(String tableName, long objectId) {
		long res = 0;
		long deviceId = GlobalInfo.getInstance().getDeviceId();
		long staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
		if (deviceId <= 0) {
			ServerLogger.sendLog("getMaxIdFromTable", "deviceId: " + deviceId + " staffId: " + staffId, TabletActionLogDTO.LOG_CLIENT);
			deviceId = staffId;
		}
		
		//time stamp by second
		long timeStamp = System.currentTimeMillis() / 10;
		VTLog.i("MAXID","timeStamp"+ timeStamp);
		res = deviceId * (long)Math.pow(10, String.valueOf(timeStamp).length()) + timeStamp;
		VTLog.i("MAXID","id"+ res);
		if(deviceId <= 0) {
			ServerLogger.sendLog("getMaxIdFromTable", "deviceId: " + deviceId + " staffId: " + staffId, TabletActionLogDTO.LOG_CLIENT);
			((GlobalBaseActivity) GlobalInfo.getInstance().getActivityContext()).runOnUiThread(new Runnable() {
				public void run() {
							((GlobalBaseActivity) GlobalInfo.getInstance()
									.getActivityContext()).showDialogMissingDeviceId(StringUtil
									.getString(R.string.TEXT_LOGIN_ONLINE_TO_GET_DEVICE_INFO));
				};
			});
			throw new IllegalArgumentException();
		}
		
//		Cursor c = null;
//		try {
//			String[] params = { "" + objectId };
//			String sql = "";
//
//			if (SALE_ORDER_TABLE.TABLE_NAME.equals(tableName)) {
//				sql = "select max(SALE_ORDER_ID) as MAX_ID from SALE_ORDER where STAFF_ID = ?";
//			} else if (SALES_ORDER_DETAIL_TABLE.TABLE_NAME.equals(tableName)) {
//				sql = "select max(SALE_ORDER_DETAIL_ID) as MAX_ID from SALE_ORDER_DETAIL where STAFF_ID = ?";
//			} else if (ACTION_LOG_TABLE.TABLE_NAME.equals(tableName)) {
//				sql = "select max(" + ACTION_LOG_TABLE.ACTION_LOG_ID + ") as MAX_ID from ACTION_LOG where STAFF_ID = ?";
//			} else if (CUSTOMER_STOCK_HISTORY_TABLE.TABLE_NAME.equals(tableName)) {
//				sql = "select max(CUSTOMER_STOCK_HISTORY_ID) as MAX_ID from CUSTOMER_STOCK_HISTORY where STAFF_ID = ?";
//			} else if (CUSTOMER_DISPLAY_PROGRAME_SCORE_TABLE.TABLE_NAME.equals(tableName)) {
//				sql = "select max(ID) as MAX_ID from CUSTOMER_DISPLAY_PROGR_SCORE where STAFF_ID = ?";
//			} else if (FEED_BACK_TABLE.FEED_BACK_TABLE.equals(tableName)) {
//				sql = "select max(FEEDBACK_ID) as MAX_ID from FEEDBACK where CREATE_USER_ID = ?";
//			} else if (FEEDBACK_DETAIL_TABLE.FEEDBACK_DETAIL_TABLE.equals(tableName)) {
//				sql = "select max(FEEDBACK_DETAIL_ID) as MAX_ID from FEEDBACK_DETAIL where CREATE_USER_ID = ?";
//			} else if (MEDIA_ITEM_TABLE.TABLE_MEDIA_ITEM.equals(tableName)) {
//				sql = "select max(MEDIA_ITEM_ID) as MAX_ID from MEDIA_ITEM where STAFF_ID = ?";
//			} else if (DEBIT_TABLE.TABLE_NAME.equals(tableName)) {
//				sql = "select max(DEBIT_ID) as MAX_ID from DEBIT";
//				params = new String[0];
//			} else if (DEBIT_DETAIL_TABLE.TABLE_NAME.equals(tableName)) {
//				sql = "select max(DEBIT_DETAIL_ID) as MAX_ID from DEBIT_DETAIL";
//				params = new String[0];
//			} else if (TRAINING_RESULT_TABLE.TABLE_NAME.equals(tableName)) {
//				StringBuffer strBuilder = new StringBuffer();
//				strBuilder.append("SELECT MAX(ID) as MAX_ID FROM TRAINING_RESULT");
//				params = new String[0];
//				sql = strBuilder.toString();
//			} else if (TRAINING_SHOP_MANAGER_RESULT_TABLE.TABLE_NAME.equals(tableName)) {
//				StringBuffer strBuilder = new StringBuffer();
//				strBuilder.append("SELECT Max(ID) AS MAX_ID FROM TRAINING_SHOP_MANAGER_RESULT");
//				params = new String[0];
//				sql = strBuilder.toString();
//			} else if (STAFF_CUSTOMER_TABLE.TABLE_NAME.equals(tableName)) {
//				sql = "select max(" + STAFF_CUSTOMER_TABLE.STAFF_CUSTOMER_ID
//						+ ") as MAX_ID from STAFF_CUSTOMER where STAFF_ID = ?";
//			} else if (PAY_RECEIVED_TABLE.TABLE_PAY_RECEIVED.equals(tableName)) {
//				sql = "select max(" + PAY_RECEIVED_TABLE.PAY_RECEIVED_ID + ") as MAX_ID from "
//						+ PAY_RECEIVED_TABLE.TABLE_PAY_RECEIVED + " where STAFF_ID = ?";
//			} else if (PAYMENT_DETAIL_TABLE.TABLE_PAYMENT_DETAIL.equals(tableName)) {
//				sql = "select max(" + PAYMENT_DETAIL_TABLE.PAYMENT_DETAIL_ID + ") as MAX_ID from "
//						+ PAYMENT_DETAIL_TABLE.TABLE_PAYMENT_DETAIL + " where STAFF_ID = ?";
//			} else if(DEBIT_TABLE.TABLE_NAME.equals(tableName)){
//				sql = "select max(" + DEBIT_TABLE.DEBIT_ID + ") as MAX_ID from "
//						+ DEBIT_TABLE.TABLE_NAME + " where OBJECT_ID = ?";
//			} else if (SUGGESTED_PRICE_TABLE.TABLE_NAME.equals(tableName)) {
//				sql = "select max(SUGGESTED_PRICE_ID) as MAX_ID from SUGGESTED_PRICE where STAFF_ID = ?";
//			} else if (TIME_KEEPER_TABLE.TABLE_NAME.equals(tableName)) {
//				sql = "select max(TIME_KEEPER_ID) as MAX_ID from TIME_KEEPER where STAFF_OWNER_ID = ?";
//			} else if(OP_SALE_VOLUME_TABLE.OP_SALE_VOLUME_TABLE.equals(tableName)){
//				sql = "select max(OP_SALE_VOLUME_ID) as MAX_ID from OP_SALE_VOLUME where STAFF_ID = ?";
//			} else if(OP_STOCK_TOTAL_TABLE.OP_STOCK_TOTAL_TABLE.equals(tableName)){
//				sql = "select max(OP_STOCK_TOTAL_ID) as MAX_ID from OP_STOCK_TOTAL where STAFF_ID = ?";
//			} else if(C2_SALE_ORDER_TABLE.TABLE_NAME.equals(tableName)){
//				sql = "select max(C2_SALE_ORDER_ID) as MAX_ID from C2_SALE_ORDER where STAFF_ID = ?";
//			} else if(C2_SALE_ORDER_DETAIL_TABLE.TABLE_NAME.equals(tableName)){
//				sql = "select max(C2_SALE_ORDER_DETAIL_ID) as MAX_ID from C2_SALE_ORDER_DETAIL where STAFF_ID = ?";
//			} else if(C2_PURCHASE_TABLE.TABLE_NAME.equals(tableName)){
//				sql = "select max(C2_PURCHASE_ID) as MAX_ID from C2_PURCHASE where STAFF_ID = ?";
//			} else if(C2_PURCHASE_DETAIL_TABLE.TABLE_NAME.equals(tableName)){
//				sql = "select max(C2_PURCHASE_DETAIL_ID) as MAX_ID from C2_PURCHASE_DETAIL where STAFF_ID = ?";
//			}else if(CUSTOMER_AVATAR_TABLE.CUSTOMER_AVATAR_TABLE.equals(tableName)){
//				sql = "select max("+CUSTOMER_AVATAR_TABLE.CUSTOMER_AVATAR_ID +") as MAX_ID from "+CUSTOMER_AVATAR_TABLE.CUSTOMER_AVATAR_TABLE+ " where STAFF_ID = ?";
//			}else if (CUSTOMER_TABLE.CUSTOMER_TABLE.equals(tableName)) {
//				sql = "select max(" + CUSTOMER_TABLE.CUSTOMER_ID + ") as MAX_ID from CUSTOMER where STAFF_ID = ?";
//			}else if (PRO_DISPLAY_PROGRAM_TABLE.PRO_DISPLAY_PROGRAM_TABLE.equals(tableName)) {
//				sql = "select max(" + PRO_DISPLAY_PROGRAM_TABLE.PRO_DISPLAY_PROGRAM_ID + ") as MAX_ID from PRO_DISPLAY_PROGRAM where STAFF_ID = ?";
//			}else if (PRO_DISPLAY_PROGRAM_DETAIL_TABLE.PRO_DISPLAY_PROGRAM_DETAIL_TABLE.equals(tableName)) {
//				sql = "select max(" + PRO_DISPLAY_PROGRAM_DETAIL_TABLE.PRO_DISPLAY_PROGRAM_DETAIL_ID + ") as MAX_ID from PRO_DISPLAY_PROGRAM_DETAIL where STAFF_ID = ?";
//			}else if (PRO_CUS_MAP_TABLE.PRO_CUS_MAP_TABLE.equals(tableName)) {
//				sql = "select max(" + PRO_CUS_MAP_TABLE.PRO_CUS_MAP_ID + ") as MAX_ID from PRO_CUS_MAP where STAFF_ID = ?";
//			}else if (PRO_CUS_MAP_DETAIL_TABLE.PRO_CUS_MAP_DETAIL_TABLE.equals(tableName)) {
//				sql = "select max(" + PRO_CUS_MAP_DETAIL_TABLE.PRO_CUS_MAP_DETAIL_ID + ") as MAX_ID from PRO_CUS_MAP_DETAIL where STAFF_ID = ?";
//			}else if (PRO_CUS_HISTORY_TABLE.PRO_CUS_HISTORY_TABLE.equals(tableName)) {
//				sql = "select max(" + PRO_CUS_HISTORY_TABLE.PRO_CUS_HISTORY_ID + ") as MAX_ID from PRO_CUS_HISTORY where STAFF_ID = ?";
//			}
//			
//			c = rawQuery(sql, params);
//			if (c != null) {
//				if (c.moveToFirst()) {
//					res = c.getLong(c.getColumnIndex("MAX_ID"));
//				}
//			}
//
//		} catch (Exception ex) {
//			VTLog.i("error", ex.toString());
//		} finally {
//			try {
//				if (c != null) {
//					c.close();
//				}
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//		}

		return res;
	}

	/**
	 * Lay so fator default
	 * 
	 * @author: TruongHN
	 * @return: String
	 * @throws:
	 */
	public long getFactorValue() {
		long res = 1;
		Vector<TableIdDTO> v = getAllRow();
		if (v.size() > 0) {
			res = v.get(0).factor;
		}

		return res;
	}

	/**
	 * Lay so fator default
	 * 
	 * @author: TruongHN
	 * @return: String
	 * @throws:
	 */
	public long getFactorValue2() {
		long res = 1;
		Vector<TableIdDTO> v = getAllRow();
		if (v.size() > 0) {
			res = v.get(0).factor;
		}

		for (TableIdDTO row : v) {
			long maxId = getMaxIdFromTable(row);

			if (maxId != -1) {
				long offValue = GlobalInfo.getInstance().getProfile().getUserData().id * row.factor;
				maxId -= offValue;

				if (maxId != row.maxId) {
					updateMaxId(row.tableName, maxId);
				}
			}
		}

		return res;
	}

	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return
	 * @return: long
	 * @throws:
	 */

	private long getMaxIdFromTable(TableIdDTO row) {
		long res = -1;
		String tableName = row.tableName;
		String staff_id = String.valueOf(row.staffId);
		String columnName = "";

		if (tableName.equals(SALE_ORDER_TABLE.TABLE_NAME)) {
			columnName = SALE_ORDER_TABLE.SALE_ORDER_ID;
		} else if (tableName.equals(SALES_ORDER_DETAIL_TABLE.TABLE_NAME)) {
			columnName = SALES_ORDER_DETAIL_TABLE.SALE_ORDER_DETAIL_ID;
		} else if (tableName.equals(ACTION_LOG_TABLE.TABLE_NAME)) {
			columnName = ACTION_LOG_TABLE.ACTION_LOG_ID;
		}

		Cursor c = null;
		if (!StringUtil.isNullOrEmpty(columnName)) {
			try {
				ArrayList<String> params = new ArrayList<String>();
				params.add(staff_id);
				StringBuilder sql = new StringBuilder();
				sql.append("select max (");
				sql.append(columnName);
				sql.append(") as MAX_ID from ");
				sql.append(tableName);
				sql.append(" where staff_id = ? ");
				c = rawQuery(sql.toString(), params.toArray(new String[params.size()]));
				if (c != null) {
					if (c.moveToFirst()) {
						res = c.getLong(c.getColumnIndex(MAX_ID));
					}
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
		}

		return res;

	}

	/**
	 * Cap nhat maxId
	 * 
	 * @author: TruongHN
	 * @param tableName
	 * @param maxId
	 * @return
	 * @return: void
	 * @throws:
	 */
	public int updateMaxId(String tableName, long maxId) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(MAX_ID, maxId);

		String[] params = { tableName };
		return update(editedValues, TABLE_NAME + " like ?", params);

		// StringBuilder sqlQuery = new StringBuilder();
		// sqlQuery.append("UPDATE TABLE_ID SET MAX_ID =? ");
		// sqlQuery.append(" WHERE TABLE_NAME like ? ");
		//
		// String[] params = { "" + maxId, tableName };
		// rawQuery(sqlQuery.toString(), params);
	}

	private TableIdDTO initLogDTOFromCursor(Cursor c) {
		TableIdDTO dto = new TableIdDTO();
		dto.id = (c.getInt(c.getColumnIndex(ID)));
		dto.maxId = (c.getLong(c.getColumnIndex(MAX_ID)));
		dto.factor = (c.getLong(c.getColumnIndex(FACTOR)));
		dto.tableName = (c.getString(c.getColumnIndex(TABLE_NAME)));
		dto.lastSynMaxId = (c.getLong(c.getColumnIndex(LAST_SYN_MAX_ID)));
		dto.shopId = (c.getInt(c.getColumnIndex(SHOP_ID)));
		dto.staffId = (c.getInt(c.getColumnIndex(STAFF_ID)));
		dto.synState = (c.getInt(c.getColumnIndex(SYN_STATE)));

		return dto;
	}

	/**
	 * 
	 * lay tat ca cac dong cua CSDL
	 * 
	 * @author: HieuNH
	 * @return: Vector<TableIdDTO>
	 * @throws:
	 */
	public Vector<TableIdDTO> getAllRow() {
		Vector<TableIdDTO> v = new Vector<TableIdDTO>();
		Cursor c = null;
		try {
			c = query(null, null, null, null, null);
			if (c != null) {
				TableIdDTO tableDto;
				if (c.moveToFirst()) {
					do {
						tableDto = initLogDTOFromCursor(c);
						v.addElement(tableDto);
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

		return v;

	}

	private ContentValues initDataRow(TableIdDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(ID, dto.id);
		editedValues.put(TABLE_NAME, dto.tableName);
		editedValues.put(MAX_ID, dto.maxId);
		editedValues.put(LAST_SYN_MAX_ID, dto.lastSynMaxId);
		editedValues.put(SHOP_ID, dto.shopId);
		editedValues.put(STAFF_ID, dto.staffId);
		editedValues.put(SYN_STATE, dto.synState);
		editedValues.put(FACTOR, dto.factor);

		return editedValues;
	}
}
