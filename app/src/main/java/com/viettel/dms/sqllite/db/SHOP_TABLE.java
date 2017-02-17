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

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.ShopDTO;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.map.dto.LatLng;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * Luu thong tin NPP
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class SHOP_TABLE extends ABSTRACT_TABLE {
	// id npp
	public static final String SHOP_ID = "SHOP_ID";
	// ma NPP
	public static final String SHOP_CODE = "SHOP_CODE";
	// ten NPP
	public static final String SHOP_NAME = "SHOP_NAME";
	// id NPP cha
	public static final String PARENT_SHOP_ID = "PARENT_SHOP_ID";
	// duong
	public static final String STREET = "STREET";
	// phuong
	public static final String WARD = "WARD";
	// quan/huyen
	public static final String DISTRICT = "DISTRICT";
	// tinh
	public static final String PROVINCE = "PROVINCE";
	// ma vung
	public static final String AREA_CODE = "AREA_CODE";
	// nuoc
	public static final String COUNTRY = "COUNTRY";
	// sdt ban
	public static final String PHONE = "PHONE";
	// di dong
	public static final String TELEPHONE = "TELEPHONE";
	// loai NPP, 1: NPP (default), 2: VNM
	public static final String SHOP_TYPE = "SHOP_TYPE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// loai kenh
	public static final String CHANNEL_TYPE_ID = "CHANNEL_TYPE_ID";
	// email giam sat NPP
	public static final String EMAIL = "EMAIL";
	// duong dan den NPP
	public static final String SHOP_PATH = "SHOP_PATH";
	public static final String LAT = "LAT";
	public static final String LNG = "LNG";

	private static final String ROLE_TABLE = "ROLE";

	public SHOP_TABLE(SQLiteDatabase mDB) {
		this.tableName = ROLE_TABLE;
		this.columns = new String[] { SHOP_ID, SHOP_CODE, SHOP_NAME,
				PARENT_SHOP_ID, STREET, WARD, DISTRICT, PROVINCE, AREA_CODE,
				COUNTRY, PHONE, TELEPHONE, SHOP_TYPE, CREATE_USER, UPDATE_USER,
				CREATE_DATE, UPDATE_DATE, CHANNEL_TYPE_ID, EMAIL, SHOP_PATH,
				SYN_STATE };
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
		ContentValues value = initDataRow((ShopDTO) dto);
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
	public long insert(ShopDTO dto) {
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
		ShopDTO dtoShop = (ShopDTO) dto;
		ContentValues value = initDataRow(dtoShop);
		String[] params = { "" + dtoShop.shopId };
		return update(value, SHOP_ID + " = ?", params);
	}

	/**
	 * Xoa 1 dong cua CSDL
	 * @author: TruongHN
	 * @return: int
	 * @throws:
	 */
	public int delete(String code) {
		String[] params = { code };
		return delete(SHOP_ID + " = ?", params);
	}

	public long delete(AbstractTableDTO dto) {
		ShopDTO dtoRole = (ShopDTO) dto;
		String[] params = { "" + dtoRole.shopId };
		return delete(SHOP_ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: TruongHN
	 * @param id
	 * @return: RoleDTO
	 * @throws:
	 */
	public ShopDTO getRowById(String id) {
		ShopDTO dto = null;
		Cursor c = null;
		try {
			String[] params = { id };
			c = query(SHOP_ID + " = ?", params, null, null, null);
		} catch (Exception ex) {
			c = null;
		}
		if (c != null) {
			if (c.moveToFirst()) {
				dto = initDTOFromCursor(c);
			}
		}
		if (c != null) {
			c.close();
		}
		return dto;
	}

	private ShopDTO initDTOFromCursor(Cursor c) {
		ShopDTO dto = new ShopDTO();
		dto.shopId = (c.getLong(c.getColumnIndex(SHOP_ID)));
		dto.shopCode = (c.getString(c.getColumnIndex(SHOP_CODE)));
		dto.shopName = (c.getString(c.getColumnIndex(SHOP_NAME)));
		dto.parentShopId = (c.getInt(c.getColumnIndex(PARENT_SHOP_ID)));
		dto.street = (c.getString(c.getColumnIndex(STREET)));
		dto.ward = (c.getString(c.getColumnIndex(WARD)));
		dto.district = (c.getString(c.getColumnIndex(DISTRICT)));
		dto.province = (c.getString(c.getColumnIndex(PROVINCE)));
		dto.areaCode = (c.getString(c.getColumnIndex(AREA_CODE)));
		dto.country = (c.getString(c.getColumnIndex(COUNTRY)));
		dto.phone = (c.getString(c.getColumnIndex(PHONE)));
		dto.telephone = (c.getString(c.getColumnIndex(TELEPHONE)));
		dto.shopType = (c.getInt(c.getColumnIndex(SHOP_TYPE)));
		dto.createUser = (c.getString(c.getColumnIndex(CREATE_USER)));
		dto.updateUser = (c.getString(c.getColumnIndex(UPDATE_USER)));
		dto.createDate = (c.getString(c.getColumnIndex(CREATE_DATE)));
		dto.updateDate = (c.getString(c.getColumnIndex(UPDATE_DATE)));
		dto.channelTypeId = (c.getInt(c.getColumnIndex(CHANNEL_TYPE_ID)));
		dto.email = (c.getString(c.getColumnIndex(EMAIL)));
		dto.shopPath = (c.getString(c.getColumnIndex(SHOP_PATH)));

		return dto;
	}

	/**
	 * lay tat ca cac dong cua CSDL
	 * 
	 * @author: TruongHN
	 * @return: Vector<ShopDTO>
	 * @throws:
	 */
	public Vector<ShopDTO> getAllRow() {
		Vector<ShopDTO> v = new Vector<ShopDTO>();
		Cursor c = null;
		try {
			c = query(null, null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			ShopDTO dto;
			if (c.moveToFirst()) {
				do {
					dto = initDTOFromCursor(c);
					v.addElement(dto);
				} while (c.moveToNext());
			}
			c.close();
		}
		return v;

	}

	private ContentValues initDataRow(ShopDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(SHOP_ID, dto.shopId);
		editedValues.put(SHOP_CODE, dto.shopCode);
		editedValues.put(SHOP_NAME, dto.shopName);
		editedValues.put(PARENT_SHOP_ID, dto.parentShopId);
		editedValues.put(STREET, dto.street);
		editedValues.put(WARD, dto.ward);
		editedValues.put(DISTRICT, dto.district);
		editedValues.put(PROVINCE, dto.province);
		editedValues.put(AREA_CODE, dto.areaCode);
		editedValues.put(COUNTRY, dto.country);
		editedValues.put(PHONE, dto.phone);
		editedValues.put(TELEPHONE, dto.telephone);
		editedValues.put(SHOP_TYPE, dto.shopType);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(UPDATE_USER, dto.updateUser);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(UPDATE_DATE, dto.updateDate);
		editedValues.put(CHANNEL_TYPE_ID, dto.channelTypeId);
		editedValues.put(EMAIL, dto.email);
		editedValues.put(SHOP_PATH, dto.shopPath);

		return editedValues;
	}

	/**
	 * Mo ta chuc nang cua ham
	 * @author: Nguyen Thanh Dung
	 * @return
	 * @return: LatLng
	 * @throws:
	 */

	public LatLng getPositionOfShop(String shopId) {
		String sql = "SELECT LAT, LNG FROM SHOP WHERE SHOP_ID = ? AND STATUS = 1";

		Cursor c = null;
		String[] params = { shopId };
		LatLng position = new LatLng();
		try {
			c = rawQuery(sql, params);
			if (c != null) {
				if (c.moveToFirst()) {
					position.lat = c.getDouble(c.getColumnIndex(LAT));
					position.lng = c.getDouble(c.getColumnIndex(LNG));
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

		return position;
	}

	/**
	 * Lay ds shop cha cua 1 shop va de quy toi khi ko con shop cha thi thoi
	 * @author: Nguyen Thanh Dung
	 * @return
	 * @return: LatLng
	 * @throws:
	 */

	public ArrayList<String> getShopRecursive(String shopId) {
		String sql = "SELECT SHOP_ID, PARENT_SHOP_ID FROM SHOP WHERE STATUS = 1 AND SHOP_ID = ?";
		Cursor c = null;
		String[] params = { shopId };
		ArrayList<String> shopIdArray = new ArrayList<String>();
		if (!StringUtil.isNullOrEmpty(shopId) && !"null".equals(shopId)) {
			shopIdArray.add(shopId);
		}
		try {
			c = rawQuery(sql, params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
					String parentId = c.getString(c.getColumnIndex(PARENT_SHOP_ID));
					ArrayList<String> tempArray = getShopRecursive(parentId);
					shopIdArray.addAll(tempArray);
					} while (c.moveToNext());
				}
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}

		return shopIdArray;
	}
	
	/**
	 * Lay ds shop con cua 1 shop va de quy toi khi ko con shop con thi thoi
	 * @author: Nguyen Thanh Dung
	 * @return
	 * @return: LatLng
	 * @throws:
	 */
	
	public ArrayList<String> getShopRecursiveReverse(String parentShopId) {
		String sql = "SELECT SHOP_ID, PARENT_SHOP_ID FROM SHOP WHERE STATUS = 1 AND PARENT_SHOP_ID = ?";
		
		Cursor c = null;
		String[] params = { parentShopId };
		ArrayList<String> shopIdArray = new ArrayList<String>();
		if (!StringUtil.isNullOrEmpty(parentShopId) && !"null".equals(parentShopId)) {
			shopIdArray.add(parentShopId);
		}
		try {
			c = rawQuery(sql, params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						String shopId = c.getString(c.getColumnIndex(SHOP_ID));
						ArrayList<String> tempArray = getShopRecursiveReverse(shopId);
						shopIdArray.addAll(tempArray);
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
		
		return shopIdArray;
	}	

	/**
	 * 
	 * Lay danh sach shop_id role GST
	 * @author: YenNTH
	 * @return
	 * @return: ArrayList<String>
	 * @throws:
	 */
	public ArrayList<String> getShopRecursiveReverseTBHV(String staffId) {
		ArrayList<String> params = new ArrayList<String>();
		StringBuffer  var1 = new StringBuffer();
		var1.append("SELECT DISTINCT s.shop_id, ");
		var1.append("                sh.parent_shop_id ");
		var1.append("FROM   staff s, ");
		var1.append("       shop sh ");
		var1.append("WHERE  s.staff_owner_id = ? ");
		params.add(Constants.STR_BLANK + staffId);
		var1.append("       AND sh.status = 1 ");
		var1.append("       AND s.shop_id = sh.shop_id ");
		var1.append("       AND s.status = 1");
		Cursor c = null;
		ArrayList<String> shopIdArray = new ArrayList<String>();
		if (!StringUtil.isNullOrEmpty(staffId) && !"null".equals(staffId)) {
			shopIdArray.add(staffId);
		}
		try {
			c = rawQueries(var1.toString(), params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						String shopId = c.getString(c.getColumnIndex(SHOP_ID));
						ArrayList<String> tempArray = getShopRecursiveReverse(shopId);
						shopIdArray.addAll(tempArray);
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
		
		return shopIdArray;
	}
	
	/**
	 * Lay danh sach NPP cua GST
	 * 
	 * @author: QuangVT
	 * @since: 6:19:02 PM Dec 17, 2013
	 * @return: List<ShopDTO>
	 * @throws:  
	 * @param staffId
	 * @return
	 */
	public List<ShopDTO> getListShopOfTBHV(String staffId) { 
		List<ShopDTO> list = new ArrayList<ShopDTO>();
		ArrayList<String> params = new ArrayList<String>(); 
		StringBuffer  sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT DISTINCT SH.SHOP_ID   AS SHOP_ID, ");
		sqlQuery.append("                SH.SHOP_CODE AS SHOP_CODE, ");
		sqlQuery.append("                SH.SHOP_NAME AS SHOP_NAME ");
		sqlQuery.append("FROM   SHOP SH, ");
		sqlQuery.append("       (SELECT SHOP_ID AS SHOP_ID ");
		sqlQuery.append("        FROM   STAFF ");
		sqlQuery.append("        WHERE  STAFF_OWNER_ID IN (SELECT STAFF_ID AS ID_GSBH ");
		sqlQuery.append("                                  FROM   STAFF ");
		sqlQuery.append("                                  WHERE  1 = 1 ");
//		sqlQuery.append("                                         AND STAFF_OWNER_ID = ? ");
//		params.add(staffId);
		sqlQuery.append("							AND STAFF_ID in(	");
		sqlQuery.append("	    						SELECT	");
		sqlQuery.append("	        						sgd.STAFF_ID	");
		sqlQuery.append("	    						FROM	");
		sqlQuery.append("	        						staff_group_detail sgd	");
		sqlQuery.append("	    						WHERE	");
		sqlQuery.append("	        						sgd.STAFF_GROUP_ID IN       (	");
		sqlQuery.append("	            					SELECT	");
		sqlQuery.append("	                					sg1.staff_group_id	");
		sqlQuery.append("	            					FROM	");
		sqlQuery.append("	                					staff_group sg1	");
		sqlQuery.append("	            					WHERE	");
		sqlQuery.append("	                					sg1.STAFF_ID = ?	");
		params.add(staffId);
		sqlQuery.append("	                					AND sg1.status = 1	");
		sqlQuery.append("	                					AND sg1.GROUP_LEVEL = 3	");
		sqlQuery.append("	                					AND sg1.GROUP_TYPE = 4	");
		sqlQuery.append("	        ))	");
		sqlQuery.append("                                         AND STAFF_TYPE_ID IN ");
		sqlQuery.append("                                             (SELECT ");
		sqlQuery.append("                                             CHANNEL_TYPE_ID ");
		sqlQuery.append("                                                               FROM ");
		sqlQuery.append("                                             CHANNEL_TYPE ");
		sqlQuery.append("                                                               WHERE ");
		sqlQuery.append("                                             TYPE = 2 ");
		sqlQuery.append("                                             AND OBJECT_TYPE ");
		sqlQuery.append("                                                 IN ( 5 )) ");
		sqlQuery.append("                                         ) ");
		sqlQuery.append("               AND STAFF_TYPE_ID IN (SELECT CHANNEL_TYPE_ID ");
		sqlQuery.append("                                     FROM   CHANNEL_TYPE ");
		sqlQuery.append("                                     WHERE  TYPE = 2 ");
		sqlQuery.append("                                            AND OBJECT_TYPE IN ( 1 ))) AS ST ");
		sqlQuery.append("WHERE  1 = 1 ");
		sqlQuery.append("       AND SH.STATUS = 1 ");
		sqlQuery.append("       AND SH.SHOP_ID = ST.SHOP_ID "); 
		sqlQuery.append("ORDER BY SH.SHOP_CODE "); 
		
		Cursor cursor = null;
		try {
			cursor = rawQueries(sqlQuery.toString(), params);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					if(cursor.getCount() > 1){
						ShopDTO shopAll = new ShopDTO();
						shopAll.shopId = -1;
						shopAll.shopCode = StringUtil.getString(R.string.ALL);
						shopAll.shopName = Constants.STR_BLANK;
						list.add(shopAll);
					}
					do{
						ShopDTO shop = new ShopDTO();
						shop.parseDataFromCusor(cursor);
						list.add(shop);
					} while (cursor.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("[Quang]", e.getMessage());
		} finally {
			if (cursor != null) {
				try{
					cursor.close();
				}catch(Exception ex){
					VTLog.e("[Quang]", ex.getMessage());
				} 
			}
		}  
		return list;
	}

	/**
	 * @author: dungnt19
	 * @since: 14:30:51 10-03-2014
	 * @return: int
	 * @throws:  
	 * @return
	 */
	public int getIsShowPriceDefince(String shopId) {
		int result = 1;
		String sql = "SELECT IS_SHOW_PRICE FROM SHOP WHERE SHOP_ID = ? AND STATUS = 1";

		Cursor c = null;
		String[] params = { shopId };
		try {
			c = rawQuery(sql, params);
			if (c != null) {
				if (c.moveToFirst()) {
					String value = c.getString(c.getColumnIndex("IS_SHOW_PRICE"));
					if(!StringUtil.isNullOrEmpty(value)) {
						result = c.getInt(c.getColumnIndex("IS_SHOW_PRICE"));
					}
				}
			}
		} catch (Exception ex) {
			VTLog.e("getIsShowPriceDefince", ex.getMessage());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}

		return result;
	}

	/**
	 * Lấy danh sách shop được quản lý bởi GSBH
	 * @param staffId
	 * @return
     */
	public ArrayList<String> getListShopStaffGroup(String staffId, String ownerId) {
		StringBuffer sqlObject = new StringBuffer();
		ArrayList<String> paramsObject = new ArrayList<String>();
		sqlObject.append("	SELECT	");
		sqlObject.append("	    distinct dbtg.SHOP_ID	as	SHOP_ID ");
		sqlObject.append("	FROM	");
		sqlObject.append("	    staff_group dbtg	");
		sqlObject.append("	WHERE	");
		sqlObject.append("	    dbtg.status        = 1	");
		sqlObject.append("	    AND dbtg.STAFF_GROUP_ID IN   (	");
		sqlObject.append("	        SELECT	");
		sqlObject.append("	            sgd.STAFF_GROUP_ID	");
		sqlObject.append("	        FROM	");
		sqlObject.append("	            staff_group_detail sgd,	");
		sqlObject.append("	            staff_group sg	");
		sqlObject.append("	        WHERE	");
		sqlObject.append("	            sgd.STAFF_ID IN     (	");
		sqlObject.append("	                SELECT	");
		sqlObject.append("	                    sgd.STAFF_ID	");
		sqlObject.append("	                FROM	");
		sqlObject.append("	                    staff_group_detail sgd	");
		sqlObject.append("	                WHERE	");
		sqlObject.append("	                    sgd.STAFF_GROUP_ID IN       (	");
		sqlObject.append("	                        SELECT	");
		sqlObject.append("	                            sg1.staff_group_id	");
		sqlObject.append("	                        FROM	");
		sqlObject.append("	                            staff_group sg1	");
		sqlObject.append("	                        WHERE	");
		sqlObject.append("	                            sg1.STAFF_ID = ?	");
		paramsObject.add(staffId);
		sqlObject.append("	                            AND sg1.status = 1	");
		sqlObject.append("	                            AND sg1.GROUP_LEVEL = 3	");
		sqlObject.append("	                            AND sg1.GROUP_TYPE = 4	");
		sqlObject.append("	                    )	");
		sqlObject.append("	                    and sgd.STAFF_ID in ( " + ownerId  + " )" );
		sqlObject.append("	                    AND sgd.status = 1	");
		sqlObject.append("	                )	");
		sqlObject.append("	                AND sgd.STATUS        = 1	");
		sqlObject.append("	                AND sg.STAFF_GROUP_ID = sgd.STAFF_GROUP_ID	");
		sqlObject.append("	        )	");
		sqlObject.append("	        OR (	");
		sqlObject.append("	            dbtg.STAFF_ID = ?	");
		paramsObject.add(staffId);
		sqlObject.append("	            AND dbtg.status   = 1	");
		sqlObject.append("	        )	");
		sqlObject.append("	        AND dbtg.GROUP_LEVEL = 4	");
		sqlObject.append("	        AND dbtg.GROUP_TYPE = 4	");
		Cursor c = null;
		ArrayList<String> shopIdArray = new ArrayList<String>();
		try {
			c = rawQueries(sqlObject.toString(), paramsObject);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						String shopId = c.getString(c.getColumnIndex(SHOP_ID));
						shopIdArray.add(shopId);
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

		return shopIdArray;
	}
}
