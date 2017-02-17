/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.DisplayProgrameDTO;
import com.viettel.dms.dto.view.DisplayProgrameModel;
import com.viettel.dms.dto.view.TBHVDisplayProgrameModel;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.utils.VTLog;

/**
 * DISPLAY_PROGRAME - Chuong trinh trung bay
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class DISPLAY_PROGRAME_TABLE extends ABSTRACT_TABLE {
	// id cua bang
	public static final String DISPLAY_PROGRAM_ID = "DISPLAY_PROGRAM_ID";
	// ma CTTB
	public static final String DISPLAY_PROGRAM_CODE = "DISPLAY_PROGRAM_CODE";
	// ten CTTB
	public static final String DISPLAY_PROGRAM_NAME = "DISPLAY_PROGRAM_NAME";
	// ma ngan CTTB
	public static final String DP_SHORT_CODE = "DP_SHORT_CODE";
	// ten ngan CTTB
	public static final String DP_SHORT_NAME = "DP_SHORT_NAME";
	// trang thai
	public static final String STATUS = "STATUS";
	// tu ngay
	public static final String FROM_DATE = "FROM_DATE";
	// den ngay
	public static final String TO_DATE = "TO_DATE";
	// truong nay chua dung
	public static final String OBJTARGET = "OBJTARGET";
	// quan he: 1: OR, 0 : AND
	public static final String RELATION = "RELATION";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay update
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi update
	public static final String UPDATE_USER = "UPDATE_USER";
	// ma nganh hang
	public static final String CAT = "CAT";
	// % ti le fino
	public static final String PERCENT_FINO = "PERCENT_FINO";
	// loai CT
	public static final String TYPE = "TYPE";
	
	public static final String DESCRIPTION = "DESCRIPTION";

	private static final String TABLE_DISPLAY_PROGRAME = "DISPLAY_PROGRAME";

	public DISPLAY_PROGRAME_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_DISPLAY_PROGRAME;
		this.columns = new String[] { DISPLAY_PROGRAM_ID,
				DISPLAY_PROGRAM_CODE, DISPLAY_PROGRAM_NAME, STATUS,
				FROM_DATE, TO_DATE, OBJTARGET, RELATION, CREATE_DATE,
				UPDATE_DATE, CREATE_USER, UPDATE_USER, CAT, PERCENT_FINO, SYN_STATE};
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
		ContentValues value = initDataRow((DisplayProgrameDTO) dto);
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
	public long insert(DisplayProgrameDTO dto) {
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
		DisplayProgrameDTO disDTO = (DisplayProgrameDTO) dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.displayProgrameCode };
		return update(value, DISPLAY_PROGRAM_CODE + " = ?", params);
	}

	/**
	 * Xoa 1 dong cua CSDL
	 * @author: TruongHN
	 * @return: int
	 * @throws:
	 */
	public int delete(String code) {
		String[] params = { code };
		return delete(DISPLAY_PROGRAM_CODE + " = ?", params);
	}

	public long delete(AbstractTableDTO dto) {
		DisplayProgrameDTO disDTO = (DisplayProgrameDTO) dto;
		String[] params = { "" + disDTO.displayProgrameCode };
		return delete(DISPLAY_PROGRAM_CODE + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: DoanDM replaced
	 * @param id
	 * @return: DisplayProgrameDTO
	 * @throws:
	 */
	public DisplayProgrameDTO getRowById(String id) {
		DisplayProgrameDTO dto = null;
		Cursor c = null;
		try {
			String[] params = { id };
			String queryStr = "SELECT * FROM DISPLAY_PROGRAME WHERE DISPLAY_PROGRAME_CODE like ?";
			c = rawQuery(queryStr, params);
			if (c != null) {
				if (c.moveToFirst()) {
					dto = initLogDTOFromCursor(c);
				}
			}
		} catch (Exception ex) {
			c = null;
		} finally{
			try{
				if (c != null) {
					c.close();
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		return dto;
	}

	private DisplayProgrameDTO initLogDTOFromCursor(Cursor c) {
		DisplayProgrameDTO dpProDTO = new DisplayProgrameDTO();
		dpProDTO.displayProgrameId = (c.getInt(c.getColumnIndex(DISPLAY_PROGRAM_ID)));
		dpProDTO.displayProgrameCode = (c.getString(c.getColumnIndex(DISPLAY_PROGRAM_CODE)));
		dpProDTO.displayProgrameName = (c.getString(c.getColumnIndex(DISPLAY_PROGRAM_NAME)));
		dpProDTO.status = (c.getInt(c.getColumnIndex(STATUS)));
		dpProDTO.fromDate = (c.getString(c.getColumnIndex(FROM_DATE)));
		dpProDTO.toDate = (c.getString(c.getColumnIndex(TO_DATE)));
		dpProDTO.objTarget = (c.getFloat(c.getColumnIndex(OBJTARGET)));

		dpProDTO.relation = (c.getInt(c.getColumnIndex(RELATION)));
		dpProDTO.createDate = (c.getString(c.getColumnIndex(CREATE_DATE)));
		dpProDTO.updateDate = (c.getString(c.getColumnIndex(UPDATE_DATE)));
		dpProDTO.createUser = (c.getString(c.getColumnIndex(CREATE_USER)));
		dpProDTO.updateUser = (c.getString(c.getColumnIndex(UPDATE_USER)));
		dpProDTO.cat = (c.getString(c.getColumnIndex(CAT)));
		dpProDTO.percentFino = (c.getFloat(c.getColumnIndex(PERCENT_FINO)));

		return dpProDTO;
	}

	private ContentValues initDataRow(DisplayProgrameDTO dto) {
		ContentValues editedValues = new ContentValues();

		editedValues.put(DISPLAY_PROGRAM_ID, dto.displayProgrameId);
		editedValues.put(DISPLAY_PROGRAM_CODE, dto.displayProgrameCode);
		editedValues.put(DISPLAY_PROGRAM_NAME, dto.displayProgrameName);
		editedValues.put(STATUS, dto.status);
		editedValues.put(FROM_DATE, dto.fromDate);
		editedValues.put(TO_DATE, dto.toDate);
		editedValues.put(RELATION, dto.relation);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(UPDATE_DATE, dto.updateDate);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(UPDATE_USER, dto.updateUser);
		editedValues.put(CAT, dto.cat);
		editedValues.put(PERCENT_FINO, dto.percentFino);

		return editedValues;
	}

	/**
	 * Lay ds chuong trinh trung bay cua NVBH
	 * @author: ThuatTQ
	 * @return: List<SaleOrderDetailDTO>
	 * @throws:
	 */
	public DisplayProgrameModel getListDisplayPrograme(Bundle data, ArrayList<String> shopIdArray) {
		String ext = data.getString(IntentConstants.INTENT_PAGE);
		String displayDepart = data.getString(IntentConstants.INTENT_DISPLAY_DEPART);
		boolean isHasDepart = !StringUtil.isNullOrEmpty(displayDepart);
		boolean checkPagging = data.getBoolean(IntentConstants.INTENT_CHECK_PAGGING, false);
		ArrayList<String> params = new ArrayList<String>();
		String idListString = TextUtils.join(",", shopIdArray); 
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISPLAY_PROGRAM_CODE,DISPLAY_PROGRAM_NAME, FROM_DATE, FROM_DATE_SORT, TO_DATE, TO_DATE_SORT, GROUP_CONCAT(DISTINCT PRODUCT_INFO_CODE) CAT FROM ");
		sql.append(" ( ");
		sql.append("SELECT DP.DISPLAY_PROGRAM_CODE, DP.DISPLAY_PROGRAM_NAME, StrfTime('%d/%m/%Y', DP.FROM_DATE) as FROM_DATE, DP.FROM_DATE FROM_DATE_SORT, StrfTime('%d/%m/%Y', DP.TO_DATE) as TO_DATE, DP.TO_DATE TO_DATE_SORT, PI.PRODUCT_INFO_CODE ");
		sql.append(" FROM DISPLAY_PROGRAM DP, DISPLAY_SHOP_MAP DSM ");
		sql.append(" left join DISPLAY_PROGRAM_DETAIL DPD on DP.DISPLAY_PROGRAM_ID = DPD.DISPLAY_PROGRAM_ID ");
		sql.append(" left join PRODUCT PR on DPD.PRODUCT_ID = PR.PRODUCT_ID ");
		sql.append(" LEFT JOIN PRODUCT_INFO PI ON PR.CAT_ID = PI.PRODUCT_INFO_ID ");
		sql.append("	WHERE DP.STATUS=1 AND DSM.STATUS = 1 AND DSM.SHOP_ID IN (");
		sql.append(idListString);
		sql.append(") ");
		sql.append(" AND DP.DISPLAY_PROGRAM_ID = DSM.DISPLAY_PROGRAM_ID ");
		sql.append(" AND ( DPD.PRODUCT_ID IS NULL OR (PR.STATUS = 1 AND PI.STATUS = 1)) ");
		sql.append(" AND ifnull(date(DP.TO_DATE) >= date(?),1)  ");// con hieu luc or chua bat dau
		params.add(DateUtils.now());
		sql.append(" ORDER BY PRODUCT_INFO_CODE ");
		sql.append(" ) ");
		sql.append(" GROUP BY DISPLAY_PROGRAM_CODE, DISPLAY_PROGRAM_NAME ");
		
		if (isHasDepart) {
			sql.append(" HAVING CAT LIKE ?");
			params.add("%" + displayDepart + "%");
		}
		String getCountProductList = " SELECT COUNT(*) AS TOTAL_ROW FROM (" +sql.toString() + ") ";
		sql.append(" ORDER BY FROM_DATE_SORT DESC, CASE WHEN TO_DATE_SORT IS NULL THEN 1 ELSE 0 END, TO_DATE_SORT, DISPLAY_PROGRAM_CODE, DISPLAY_PROGRAM_NAME");
//		stringbuilder.append(" ORDER BY FROM_DATE DESC, TO_DATE, DISPLAY_PROGRAM_CODE, DISPLAY_PROGRAM_NAME");
		String queryGetListProductForOrder = sql.toString() + " " + ext;
		

		DisplayProgrameModel modelData = new DisplayProgrameModel();
		List<DisplayProgrameDTO> list = new ArrayList<DisplayProgrameDTO>();
		modelData.setModelData(list);

		Cursor cTmp = null;
		try {
			// get total row first
			if (!checkPagging) {
				cTmp = rawQuery(getCountProductList, params.toArray(new String[params.size()]));
				int total = 0;
				if (cTmp != null) {
					cTmp.moveToFirst();
					total = cTmp.getInt(0);
				}
				modelData.setTotal(total);
			}
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try {
				if (cTmp != null) {
					cTmp.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		// end
		Cursor c = null;
		try {
			c = rawQuery(queryGetListProductForOrder, params.toArray(new String[params.size()]));

			if (c != null) {

				if (c.moveToFirst()) {
					DisplayProgrameDTO orderJoinTableDTO = null;
					do {
						orderJoinTableDTO = this.initPromotionProgrameObjectFromGetStatement(c);
						list.add(orderJoinTableDTO);
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
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}

		return modelData;
	}
	
	/**
	 * 
	*  Lay ds chuong trinh bay cua GSNPP
	*  @author: Nguyen Thanh Dung
	*  @param data
	*  @return
	*  @return: DisplayProgrameModel
	*  @throws:
	 */
	public DisplayProgrameModel getListSuperDisplayPrograme(Bundle data, ArrayList<String> shopIdArray) {
		String ext = data.getString(IntentConstants.INTENT_PAGE);
		String displayDepart = data.getString(IntentConstants.INTENT_DISPLAY_DEPART);
		boolean isHasDepart = !StringUtil.isNullOrEmpty(displayDepart);
		boolean checkPagging = data.getBoolean(IntentConstants.INTENT_CHECK_PAGGING, false);
		String idListString = TextUtils.join(",", shopIdArray); 
		ArrayList<String> params = new ArrayList<String>();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISPLAY_PROGRAM_CODE,DISPLAY_PROGRAM_NAME, FROM_DATE, FROM_DATE_SORT, TO_DATE, TO_DATE_SORT, GROUP_CONCAT(DISTINCT PRODUCT_INFO_CODE) CAT FROM ");
		sql.append(" ( ");
		sql.append("SELECT DP.DISPLAY_PROGRAM_CODE, DP.DISPLAY_PROGRAM_NAME, StrfTime('%d/%m/%Y', DP.FROM_DATE) as FROM_DATE, DP.FROM_DATE FROM_DATE_SORT, StrfTime('%d/%m/%Y', DP.TO_DATE) as TO_DATE, DP.TO_DATE TO_DATE_SORT, PI.PRODUCT_INFO_CODE ");
		sql.append(" FROM DISPLAY_PROGRAM DP, DISPLAY_SHOP_MAP DSM ");
		sql.append(" left join DISPLAY_PROGRAM_DETAIL DPD on DP.DISPLAY_PROGRAM_ID = DPD.DISPLAY_PROGRAM_ID ");
		sql.append(" left join PRODUCT PR on DPD.PRODUCT_ID = PR.PRODUCT_ID ");
		sql.append(" LEFT JOIN PRODUCT_INFO PI ON PR.CAT_ID = PI.PRODUCT_INFO_ID ");
		sql.append("	WHERE DP.STATUS=1 AND DSM.STATUS = 1 AND DSM.SHOP_ID IN (");
		sql.append(idListString);
		sql.append(") ");
		sql.append(" AND DP.DISPLAY_PROGRAM_ID = DSM.DISPLAY_PROGRAM_ID ");
		sql.append(" AND ( DPD.PRODUCT_ID IS NULL OR (PR.STATUS = 1 AND PI.STATUS = 1)) ");
		sql.append("	AND ifnull(date(DP.TO_DATE) >= date(?),1)  ");// con hieu luc or chua bat dau
		params.add(DateUtils.now());
		sql.append(" ORDER BY PRODUCT_INFO_CODE ");
		sql.append(" ) ");
		sql.append(" GROUP BY DISPLAY_PROGRAM_CODE, DISPLAY_PROGRAM_NAME ");
		
//		params.add("" + staffId);
		if (isHasDepart) {
			sql.append(" HAVING CAT LIKE ?");
			params.add("%" + displayDepart + "%");
		}
		String getCountProductList = " SELECT COUNT(*) AS TOTAL_ROW FROM (" +sql.toString() + ") ";
		sql.append(" ORDER BY FROM_DATE_SORT DESC, CASE WHEN TO_DATE_SORT IS NULL THEN 1 ELSE 0 END, TO_DATE_SORT, DISPLAY_PROGRAM_CODE, DISPLAY_PROGRAM_NAME");
		String queryGetListProductForOrder = sql.toString() + " " + ext;
				
		DisplayProgrameModel modelData = new DisplayProgrameModel();
		List<DisplayProgrameDTO> list = new ArrayList<DisplayProgrameDTO>();
		modelData.setModelData(list);

		Cursor cTmp = null;
		try {
			// get total row first
			if (!checkPagging) {
				cTmp = rawQuery(getCountProductList, params.toArray(new String[params.size()]));
				int total = 0;
				if (cTmp != null) {
					cTmp.moveToFirst();
					total = cTmp.getInt(0);
				}
				modelData.setTotal(total);
			}
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try {
				if (cTmp != null) {
					cTmp.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
			// end
		Cursor c = null;
		try{
			c = rawQuery(queryGetListProductForOrder, params.toArray(new String[params.size()]));
			
			if (c != null) {
				
				if (c.moveToFirst()) {
					DisplayProgrameDTO orderJoinTableDTO = null;
					do {
						orderJoinTableDTO = this
								.initPromotionProgrameObjectForSuperFromGetStatement(c);
						list.add(orderJoinTableDTO);
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
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		
		return modelData;
	}
	
	/**
	 * 
	*  Lay ds chuong trinh trung bay cua TBHV
	*  @author: Nguyen Thanh Dung
	*  @param data
	*  @return
	*  @return: TBHVDisplayProgrameModel
	*  @throws:
	 */
	public TBHVDisplayProgrameModel getListTBHVDisplayPrograme(Bundle data) {
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		String ext = data.getString(IntentConstants.INTENT_PAGE);
//		String displayType = data.getString(IntentConstants.INTENT_DISPLAY_TYPE);
		String displayDepart = data.getString(IntentConstants.INTENT_DISPLAY_DEPART);
//		boolean isHasType = !StringUtil.isNullOrEmpty(displayType);
		boolean isHasDepart = !StringUtil.isNullOrEmpty(displayDepart);
		boolean checkPagging = data.getBoolean(IntentConstants.INTENT_CHECK_PAGGING, false);
		
		ArrayList<String> params = new ArrayList<String>();
//		String[] params = new String[] {shopId};
		
		SHOP_TABLE shopTB = new SHOP_TABLE(this.mDB);
		ArrayList<String> listParentShopId = shopTB.getShopRecursive(shopId);
		ArrayList<String> listShopId = shopTB.getShopRecursiveReverse(shopId);
		String strListParentShop = TextUtils.join(",", listParentShopId);
		String strListShop = TextUtils.join(",", listShopId);
		StringBuffer  stringbuilder = new StringBuffer();
		stringbuilder.append("select TMP.display_program_id DISPLAY_PROGRAM_ID, TMP.display_program_code DISPLAY_PROGRAM_CODE,TMP.display_program_name DISPLAY_PROGRAM_NAME, ");
		stringbuilder.append(" TMP.FROM_DATE FROM_DATE, TMP.TO_DATE TO_DATE,TMP.FROM_DATE_SORT FROM_DATE_SORT, TMP.TO_DATE_SORT TO_DATE_SORT, group_concat(TMP.CAT) CAT   from ( ");
		stringbuilder.append("SELECT DISTINCT dp.display_program_id display_program_id, ");
		stringbuilder.append("       dp.display_program_code display_program_code, ");
		stringbuilder.append("       dp.display_program_name display_program_name, ");
		stringbuilder.append("       Strftime('%d/%m/%Y', dp.from_date) AS FROM_DATE, ");
		stringbuilder.append("       DP.FROM_DATE FROM_DATE_SORT, ");
		stringbuilder.append("       Strftime('%d/%m/%Y', dp.to_date) AS TO_DATE, ");
		stringbuilder.append("       DP.TO_DATE TO_DATE_SORT, ");
		stringbuilder.append("       PI.product_info_code CAT ");
		stringbuilder.append("FROM   display_program dp, ");
		stringbuilder.append("       display_shop_map dsm ");
		stringbuilder.append("       JOIN display_program_detail DPD ");
		stringbuilder.append("         ON DP.display_program_id = DPD.display_program_id ");
		stringbuilder.append("       JOIN product PR ");
		stringbuilder.append("         ON DPD.product_id = PR.product_id ");
		stringbuilder.append("       JOIN product_info PI ");
		stringbuilder.append("         ON PR.cat_id = PI.product_info_id ");
		stringbuilder.append("WHERE  1 = 1 ");
		stringbuilder.append("       AND dp.display_program_id = dsm.display_program_id ");
		stringbuilder.append("       AND dp.status = 1 ");
		stringbuilder.append("       AND dsm.status = 1 ");
		stringbuilder.append("       AND dsm.shop_id IN ( ");
		stringbuilder.append(strListParentShop +", " + strListShop +") ");
		stringbuilder.append("       AND Ifnull(Date(dp.to_date) >= Date(?), 1) ");
		params.add(DateUtils.now());
		if (isHasDepart) {
			stringbuilder.append(" AND PI.product_info_code LIKE (?)");
			params.add("%" + displayDepart + "%");
		}
		
		stringbuilder.append(" ) TMP ");
		stringbuilder.append(" group by TMP.display_program_id,TMP.display_program_code, TMP.display_program_name  ");

		String getCountProductList = " SELECT COUNT(*) AS TOTAL_ROW FROM (" +stringbuilder.toString() + ") ";

		stringbuilder.append(" ORDER BY TMP.FROM_DATE_SORT DESC, CASE WHEN TMP.TO_DATE_SORT IS NULL THEN 1 ELSE 0 END, TMP.TO_DATE_SORT, ");
		stringbuilder.append("          TMP.display_program_code, ");
		stringbuilder.append("          TMP.display_program_name ");
		String queryGetListProductForOrder = stringbuilder.toString() + " " + ext;
		
		TBHVDisplayProgrameModel modelData = new TBHVDisplayProgrameModel();
		List<DisplayProgrameDTO> list = new ArrayList<DisplayProgrameDTO>();
		modelData.setModelData(list);
		Cursor cTmp = null;
		try {
			// get total row first
			if (!checkPagging) {
				cTmp = rawQuery(getCountProductList, params.toArray(new String[params.size()]));
				int total = 0;
				if (cTmp != null) {
					cTmp.moveToFirst();
					total = cTmp.getInt(0);
				}
				modelData.setTotal(total);
			}
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try {
				if (cTmp != null) {
					cTmp.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
			// end
		Cursor c = null;
		try{
			c = rawQuery(queryGetListProductForOrder, params.toArray(new String[params.size()]));
			
			if (c != null) {
				
				if (c.moveToFirst()) {
					DisplayProgrameDTO orderJoinTableDTO = null;
					do {
						orderJoinTableDTO = this
								.initPromotionProgrameObjectForSuperFromGetStatement(c);
						list.add(orderJoinTableDTO);
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
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		
		return modelData;
	}
	
	/**
	*  set gia tri cho displayProgramDTO danh cho GSNPP
	*  @author: ThanhNN8
	*  @param c
	*  @return
	*  @return: DisplayProgrameDTO
	*  @throws: 
	*/
	private DisplayProgrameDTO initPromotionProgrameObjectForSuperFromGetStatement(
			Cursor c) {
		// TODO Auto-generated method stub
		DisplayProgrameDTO dto = new DisplayProgrameDTO();

		if (c.getColumnIndex(DISPLAY_PROGRAM_ID) >= 0) {
			dto.displayProgrameId = c.getLong(c
					.getColumnIndex(DISPLAY_PROGRAM_ID));
		} else {
			dto.displayProgrameId = 0;
		}

		if (c.getColumnIndex(DISPLAY_PROGRAM_CODE) >= 0) {
			dto.displayProgrameCode = c.getString(c
					.getColumnIndex(DISPLAY_PROGRAM_CODE));
		} else {
			dto.displayProgrameCode = "";
		}

		if (c.getColumnIndex(DISPLAY_PROGRAM_NAME) >= 0) {
			dto.displayProgrameName = c.getString(c
					.getColumnIndex(DISPLAY_PROGRAM_NAME));
		} else {
			dto.displayProgrameName = "";
		}

		if (c.getColumnIndex(FROM_DATE) >= 0) {
			dto.fromDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex(FROM_DATE)));
		} else {
			dto.fromDate = "";
		}

		if (c.getColumnIndex(TO_DATE) >= 0) {
			dto.toDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex(TO_DATE)));
		} else {
			dto.toDate = "";
		}
//		if (c.getColumnIndex("TG") >= 0) {
//			dto.quantity = c.getInt(c.getColumnIndex("TG"));
//		} else {
//			dto.quantity = 0;
//		}
//		if (c.getColumnIndex("KD") >= 0) {
//			dto.countCustomerNotComplete = c.getInt(c.getColumnIndex("KD"));
//		} else {
//			dto.countCustomerNotComplete = 0;
//		}
//		if (c.getColumnIndex(DESCRIPTION) >= 0) {
//			dto.displayProgrameType = c.getString(c.getColumnIndex(DESCRIPTION));
//		} else {
//			dto.displayProgrameType = "";
//		}
		
		if(c.getColumnIndex(CAT) >= 0) {
			dto.cat = c.getString(c.getColumnIndex(CAT));
		} else {
			dto.cat = "";
		}

		return dto;
	}

	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: SoaN
	 * @param c
	 * @return
	 * @return: DisplayProgrameDTO
	 * @throws:
	 */

	private DisplayProgrameDTO initPromotionProgrameObjectFromGetStatement(
			Cursor c) {
		DisplayProgrameDTO dto = new DisplayProgrameDTO();

		if (c.getColumnIndex(DISPLAY_PROGRAM_ID) >= 0) {
			dto.displayProgrameId = c.getLong(c
					.getColumnIndex(DISPLAY_PROGRAM_ID));
		} else {
			dto.displayProgrameId = 0;
		}

		if (c.getColumnIndex(DISPLAY_PROGRAM_CODE) >= 0) {
			dto.displayProgrameCode = c.getString(c
					.getColumnIndex(DISPLAY_PROGRAM_CODE));
		} else {
			dto.displayProgrameCode = "";
		}

		if (c.getColumnIndex(DISPLAY_PROGRAM_NAME) >= 0) {
			dto.displayProgrameName = c.getString(c
					.getColumnIndex(DISPLAY_PROGRAM_NAME));
		} else {
			dto.displayProgrameName = "";
		}

		if (c.getColumnIndex(FROM_DATE) >= 0) {
			dto.fromDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex(FROM_DATE)));
		} else {
			dto.fromDate = "";
		}

		if (c.getColumnIndex(TO_DATE) >= 0) {
			dto.toDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex(TO_DATE)));
		} else {
			dto.toDate = "";
		}
//		if (c.getColumnIndex("SOSUAT") >= 0) {
//			dto.quantity = c.getInt(c.getColumnIndex("SOSUAT"));
//		} else {
//			dto.quantity = 0;
//		}
//		if (c.getColumnIndex(DESCRIPTION) >= 0) {
//			dto.displayProgrameType = c.getString(c.getColumnIndex(DESCRIPTION));
//		} else {
//			dto.displayProgrameType = "";
//		}
		if(c.getColumnIndex("CAT") >= 0) {
			dto.cat = c.getString(c.getColumnIndex("CAT"));
		} else {
			dto.cat = "";
		}
		
		return dto;
	}

	/**
	*  Lay so chuong trinh trung bay dang chay
	*  @author: Nguyen Thanh Dung
	*  @return
	*  @return: int
	*  @throws: 
	*/
	
	public int getNumDisplayProgrameRunning() {
		String dateNow=DateUtils.now();
		List<String> param= new ArrayList<String>();
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("SELECT DP.DISPLAY_PROGRAME_ID, DP.DISPLAY_PROGRAME_CODE, DP.DISPLAY_PROGRAME_NAME, StrfTime('%d/%m/%Y', DP.FROM_DATE) as FROM_DATE, StrfTime('%d/%m/%Y', DP.TO_DATE) as TO_DATE, GROUP_CONCAT(distinct DP.CAT) as CAT FROM DISPLAY_PROGRAME DP");
		stringbuilder.append(" WHERE DP.STATUS=1 ");
		stringbuilder.append(" AND date(DP.FROM_DATE) <= (SELECT date(?)) AND ifnull(date(DP.TO_DATE) >= (SELECT date(?)),1)");
		param.add(dateNow);
		param.add(dateNow);
		stringbuilder.append(" GROUP BY DP.DISPLAY_PROGRAME_CODE, DP.DISPLAY_PROGRAME_NAME");
		
		String getCountProductList = " SELECT COUNT(*) AS TOTAL_ROW FROM (" +stringbuilder.toString() + ") ";
		
		Cursor cTmp = null;
		int total = 0;
		try {
			// get total row first
			cTmp = rawQuery(getCountProductList, null);
			
			if (cTmp != null) {
				cTmp.moveToFirst();
				total = cTmp.getInt(0);
			}
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try {
				if (cTmp != null) {
					cTmp.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		
		return total;
	}
	
}
