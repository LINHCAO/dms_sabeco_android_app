/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.ShopParamDTO;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * cac thong tin dac biet ve shop (khai bao thoi gian cham cong, di tuyen)
 * 
 * @author: HaiTC3
 * @version: 1.1
 * @since: 1.0
 */
public class SHOP_PARAM_TABLE extends ABSTRACT_TABLE {

	// id cua table
	public static final String SHOP_PARAM_ID = "SHOP_PARAM_ID";
	// id cua shop khai bao
	public static final String SHOP_ID = "SHOP_ID";
	// type param
	public static final String TYPE = "TYPE";
	// code param
	public static final String CODE = "CODE";
	// name param
	public static final String NAME = "NAME";
	// description
	public static final String DESCRIPTION = "DESCRIPTION";
	// status
	public static final String STATUS = "STATUS";
	// create_date
	public static final String CREATE_DATE = "CREATE_DATE";
	// create_user
	public static final String CREATE_USER = "CREATE_USER";
	// update date
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// update user
	public static final String UPDATE_USER = "UPDATE_USER";

	public static final String TABLE_NAME = "SHOP_PARAM";

	public SHOP_PARAM_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_NAME;
		this.columns = new String[] { SHOP_PARAM_ID, SHOP_ID, TYPE, CODE, NAME,
				DESCRIPTION, STATUS, CREATE_DATE, CREATE_USER, UPDATE_DATE,
				UPDATE_USER };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.lib.sqllite.db.ABSTRACT_TABLE#insert(com.viettel
	 * .vinamilk.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long insert(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.lib.sqllite.db.ABSTRACT_TABLE#update(com.viettel
	 * .vinamilk.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.lib.sqllite.db.ABSTRACT_TABLE#delete(com.viettel
	 * .vinamilk.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long delete(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	} 
	
	/**
	 * Get list time define header for table
	 * 
	 * @author: QuangVT
	 * @since: 4:22:26 PM Dec 4, 2013
	 * @return: ArrayList<ShopParamDTO>
	 * @throws:  
	 * @param data
	 * @return
	 */
	public ArrayList<ShopParamDTO> getListTimeHeader(Bundle data) {
		ArrayList<ShopParamDTO> listTimeDefine = new ArrayList<ShopParamDTO>();
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		//SHOP_TABLE shopTB = new SHOP_TABLE(this.mDB);
		ArrayList<String> listShopId = new ArrayList<String>();
		listShopId.add(shopId);
		boolean isHaveTimeHeader = false;
		for(String shop:listShopId){ 
			StringBuilder sqlRequest = new StringBuilder();
			ArrayList<String> params = new ArrayList<String>();
			sqlRequest.append("SELECT * ");
			sqlRequest.append("FROM   shop_param sp ");
			sqlRequest.append("WHERE  sp.status = 1 ");
			sqlRequest.append("       AND sp.type IN ('DT_START', 'DT_MIDDLE', 'DT_END' )");
			sqlRequest.append("       AND sp.shop_id = ?");
			params.add(shop);
			sqlRequest.append(" limit 3"); 
		 	
			Cursor c = null;
			try {
				c = rawQueries(sqlRequest.toString(), params);
				if (c != null) {
					if (c.moveToFirst()) {
						isHaveTimeHeader = true;
						do {
							ShopParamDTO object = new ShopParamDTO();
							object.initObjectWithCursor(c);
							listTimeDefine.add(object);
						} while (c.moveToNext());
					}
				}
			} catch (Exception e) {
				VTLog.i("[Quang]", e.getMessage());
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			} finally {
				if (c != null) {
					try {
						c.close();
					} catch (Exception e2) {
						VTLog.i("[Quang]", e2.getMessage());
					}
				}
			}
			
			if(isHaveTimeHeader){
				break;
			}
		} 
		
		return listTimeDefine;
	}

	/**
	 * Lay params
	 *
	 * @author: Nguyen Thanh Dung
	 * @param shopId
	 * @return
	 * @return: List<ApParamDTO>
	 * @throws:
	 */

	public ShopParamDTO getDistanceParamForTakeAttendance(String shopId) {
		ArrayList<String> listShopId = new ArrayList<String>();
		listShopId.add(shopId);
		ArrayList<String> params = new ArrayList<String>();
		params.add(shopId);
		String sql = "select sp.* from shop_param sp join shop s on sp.shop_id = s.parent_shop_id where s.shop_id = ? and type = 'CC_DISTANCE'";

		Cursor c = null;
		ShopParamDTO param_CC_DISTANCE = null;
		try {
			c = rawQueries(sql, params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						ShopParamDTO object = new ShopParamDTO();
						object.initObjectWithCursor(c);
						param_CC_DISTANCE = object;
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

		return param_CC_DISTANCE;
	}

	/**
	 * Lay ds params
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param ext
	 * @return
	 * @return: List<ApParamDTO>
	 * @throws:
	 */

	public List<ShopParamDTO> getListParamForTakeAttendance(String shopId) {
//		String shopId = ext.getString(IntentConstants.INTENT_PARENT_SHOP_ID);

		SHOP_TABLE shopTB = new SHOP_TABLE(this.mDB);
		ArrayList<String> listShopId = shopTB.getShopRecursive(shopId);
//		ArrayList<String> listShopId = new ArrayList<String>();
//		listShopId.add(shopId);
		//String strListShop = TextUtils.join(",", listShopId);

		List<ShopParamDTO> listParam = new ArrayList<ShopParamDTO>();
		
		boolean isHaveParam = false;
		for (String shop : listShopId) {
			ArrayList<String> params = new ArrayList<String>();
			params.add(shop);
			String sql=SQLiteQueryBuilder.buildQueryString(false, "shop_param", null, " status = 1 AND type IN ('CC_START','CC_END', 'CC_DISTANCE' ) AND shop_id = ? ", null, null, null, "3");
 
			Cursor c = null;
			ShopParamDTO param_CC_START = null;
			ShopParamDTO param_CC_END = null;
			ShopParamDTO param_CC_DISTANCE = null;
			try {
				c = rawQueries(sql, params);
				if (c != null) {
					if (c.moveToFirst()) {
						isHaveParam = true;
						do {
							ShopParamDTO object = new ShopParamDTO();
							object.initObjectWithCursor(c);
							if (object.type.equals("CC_START")) {
								param_CC_START = object;
							} else if (object.type.equals("CC_END")) {
								param_CC_END = object;
							} else if (object.type.equals("CC_DISTANCE")) {
								param_CC_DISTANCE = object;
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
				
				//duongdt3 update 
				//Them 3 loai vao đúng thứ tự
				if (param_CC_START != null && param_CC_END != null && param_CC_DISTANCE!= null) {
					listParam.add(param_CC_START);
					listParam.add(param_CC_END);
					listParam.add(param_CC_DISTANCE);
				}
				
				if(!isHaveParam || listParam.size() < 3){
					listParam.clear();
					continue;
				}
			}

			if (isHaveParam) {
				break;
			}
		}

		return listParam;
	}
}
