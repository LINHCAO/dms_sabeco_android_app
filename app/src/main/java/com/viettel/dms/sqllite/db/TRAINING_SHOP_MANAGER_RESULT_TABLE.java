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

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.TrainingShopManagerResultDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.DisplayProgrameItemDTO;
import com.viettel.dms.dto.view.TBHVFollowProblemDTO;
import com.viettel.dms.dto.view.TBHVFollowProblemItemDTO;
import com.viettel.dms.util.StringUtil;

/**
 * luu tru cac danh gia ve NVBH, ghi nhan gop y cua khach hang, ghi nhan cac SKU
 * can ban lan sau. Cac thong tin duoc ghi nhan trong qua trinh huan luyen NVBH
 * tren mot khach hang cu the
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class TRAINING_SHOP_MANAGER_RESULT_TABLE extends ABSTRACT_TABLE {

	// id bang
	public static final String ID = "ID";
	// ma training plan detail
	public static final String TRAINING_PLAN_DETAIL_ID = "TRAINING_PLAN_DETAIL_ID";
	// xac dinh loai du lieu cua dong tuong ung
	public static final String OBJECT_TYPE = "OBJECT_TYPE";
	// chi co y nghia voi object type = 3
	public static final String STATUS = "STATUS";
	//
	public static final String REMIND_DATE = "REMIND_DATE";
	// ngay thuc hien
	public static final String DONE_DATE = "DONE_DATE";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// noi dung
	public static final String CONTENT = "CONTENT";

	// ten table
	public static final String TABLE_NAME = "TRAINING_SHOP_MANAGER_RESULT";

	public TRAINING_SHOP_MANAGER_RESULT_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_NAME;
		this.columns = new String[] { ID, TRAINING_PLAN_DETAIL_ID, OBJECT_TYPE,
				STATUS, REMIND_DATE, DONE_DATE, CREATE_DATE, UPDATE_DATE,
				CONTENT, SYN_STATE };
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
		ContentValues value = initDataRow((TrainingShopManagerResultDTO) dto);
		return insert(null, value);
	}

	/**
	 * 
	 * insert with TrainingResultDTO
	 * 
	 * @author: HaiTC3
	 * @param dto
	 * @return
	 * @return: long
	 * @throws:
	 */
	public long insert(TrainingShopManagerResultDTO dto) {
		ContentValues value = initDataRow(dto);
		return insert(null, value);
	}


	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		TrainingShopManagerResultDTO dtoDisplay = (TrainingShopManagerResultDTO) dto;
		ContentValues value = initDataRow(dtoDisplay);
		String[] params = { String.valueOf(dtoDisplay.ID) };
		return update(value, ID + " = ?", params);
	}


	@Override
	protected long delete(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		TrainingShopManagerResultDTO dtoDisplay = (TrainingShopManagerResultDTO) dto;
		String[] params = { "" + dtoDisplay.ID };
		return delete(ID + " = ?", params);
	}

	/**
	 * 
	 * init data for row in DB
	 * 
	 * @author: HaiTC3
	 * @param dto
	 * @return
	 * @return: ContentValues
	 * @throws:
	 */
	private ContentValues initDataRow(TrainingShopManagerResultDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(ID, dto.ID);
		editedValues.put(TRAINING_PLAN_DETAIL_ID, dto.trainingPlanDetailID);
		editedValues.put(OBJECT_TYPE, dto.objectType);
		editedValues.put(STATUS, dto.status);
		editedValues.put(REMIND_DATE, dto.remindDate);
		editedValues.put(DONE_DATE, dto.doneDate);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(UPDATE_DATE, dto.updateDate);
		editedValues.put(CONTENT, dto.content);
		editedValues.put(SYN_STATE, dto.synState);
		return editedValues;
	}


	/**
	 * 
	 * Lay ds van de theo GSNPP, TTTT cua GST
	 * 
	 * @author: YenNTH
	 * @param data
	 * @return
	 * @return: FollowProblemDTO
	 * @throws:
	 */
	public TBHVFollowProblemDTO getListProblemOfTBHV(Bundle data) {
		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
		String parentShopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		ArrayList<String> listShopId = shopTable.getShopRecursiveReverse(parentShopId);
		String strListShop = TextUtils.join(",", listShopId);
		String extPage = data.getString(IntentConstants.INTENT_PAGE);		
		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID_PARA);
		String status = data.getString(IntentConstants.INTENT_STATE);
		String typeProblem = data.getString(IntentConstants.INTENT_TYPE_PROBLEM);
		String parentStaffId = data.getString(IntentConstants.INTENT_STAFF_ID);
		String fromDate = data.getString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE);
		String toDate = data.getString(IntentConstants.INTENT_FIND_ORDER_TO_DATE);
		boolean checkPagging = data.getBoolean(IntentConstants.INTENT_CHECK_PAGGING, false);

		TBHVFollowProblemDTO result = new TBHVFollowProblemDTO();
		List<String> stringParams = new ArrayList<String>();
		StringBuilder sql = new StringBuilder();		
		sql.append("SELECT FB.feedback_id                       as  ID, ");
		sql.append("       FB.staff_id                          as  STAFF_ID, ");	
		sql.append("       S.staff_code                         as  STAFF_CODE, ");
		sql.append("       S.staff_name                         as  STAFF_NAME, ");
		sql.append("       FB.type                              as  TYPE, ");
		sql.append("       C.customer_code                      as  CUSTOMER_CODE, ");
		sql.append("       C.customer_name                      as  CUSTOMER_NAME, ");
		sql.append("       C.housenumber                        as  HOUSENUMBER, ");
		sql.append("       C.street                             as  STREET, ");
		sql.append("       C.address                            as  ADDRESS, ");
		sql.append("       FB.content                           as  CONTENT, ");
		sql.append("       Strftime('%d/%m/%Y', FB.create_date) as  CREATE_DATE, ");
		sql.append("       Strftime('%d/%m/%Y', FB.remind_date) as  REMIND_DATE, ");
		sql.append("       Strftime('%d/%m/%Y', FB.done_date)   as  DONE_DATE, ");
		sql.append("       FB.num_return                        as  NUM_RETURN, ");
		sql.append("       FB.status                            as  STATUS, ");
		sql.append("       ct.object_type                       as  OBJECT_TYPE, ");
		sql.append("       (select ap_param_name from ap_param where ap_param_code = fb.type and status= 1 and type in ('FEEDBACK_TYPE_TTTT','FEEDBACK_TYPE_GSNPP')) as DESCRIPTION ");
		sql.append("FROM   feedback FB, staff s ");
		sql.append("       LEFT JOIN customer C ");
		sql.append("              ON FB.customer_id = C.customer_id, channel_type ct ");
		sql.append("WHERE  FB.staff_id = S.staff_id ");
		sql.append("       and fb.create_user_id = ? ");
		stringParams.add(parentStaffId);
		sql.append("       and s.staff_id in (select distinct a.staff_id ");
		sql.append("       from staff a, staff a1 ");
		sql.append("       where 1=1 ");
		sql.append("       and a.status = 1 ");
		sql.append("       and a1.status = 1 ");
		sql.append("       and ((a1.staff_owner_id in (?) ");
		stringParams.add(parentStaffId);
		sql.append("       and a.staff_type_id in (select channel_type_id from channel_type where status = 1 and type = 2 and object_type =11 )) ");
		sql.append("       or ( ");
		sql.append("		 a1.STAFF_ID in(	");
		sql.append("	    	SELECT	");
		sql.append("	        	sgd.STAFF_ID	");
		sql.append("	    	FROM	");
		sql.append("	        	staff_group_detail sgd	");
		sql.append("	    	WHERE	");
		sql.append("	        	sgd.STAFF_GROUP_ID IN       (	");
		sql.append("	            	SELECT	");
		sql.append("	                	sg1.staff_group_id	");
		sql.append("	            	FROM	");
		sql.append("	                	staff_group sg1	");
		sql.append("	            	WHERE	");
		sql.append("	                	sg1.STAFF_ID = ?	");
		stringParams.add(parentStaffId);
		sql.append("	                	AND sg1.status = 1	");
		sql.append("	                	AND sg1.GROUP_LEVEL = 3	");
		sql.append("	                	AND sg1.GROUP_TYPE = 4	");
		sql.append("	        ))	");
		sql.append("       and a.staff_type_id in (select channel_type_id from channel_type where status = 1 and type = 2 and object_type =5 ))) ");
		sql.append("       and a1.shop_id in( ");
		sql.append(strListShop);
		sql.append("       )) ");
		sql.append("       AND s.staff_type_id = ct.channel_type_id ");
		sql.append("       AND DESCRIPTION is not null "); 
		if (!StringUtil.isNullOrEmpty(status)) {
			sql.append(" AND FB.STATUS = ? "); // loc theo status
			stringParams.add(status);
		} else {
			sql.append(" AND FB.STATUS IN (1,2,3) ");
		}
		if (!StringUtil.isNullOrEmpty(fromDate)) {
			sql.append(" AND substr(FB.CREATE_DATE,1,10) >= ? ");
			stringParams.add(fromDate);
		}
		if (!StringUtil.isNullOrEmpty(toDate)) {
			sql.append(" AND substr(FB.CREATE_DATE,1,10) <= ? ");
			stringParams.add(toDate);
		}
		if (!StringUtil.isNullOrEmpty(staffId)) {
			sql.append(" AND FB.STAFF_ID = ? ");// loc theo GSNPP nao
			stringParams.add(staffId);
		}
		if (!StringUtil.isNullOrEmpty(typeProblem)) {
			sql.append("       AND FB.TYPE = ? ");
			stringParams.add(typeProblem);
		}
		sql.append(" ORDER BY FB.STATUS, ");
		sql.append("          datetime(FB.REMIND_DATE), ");
		sql.append("          datetime(FB.DONE_DATE) desc, ");
		sql.append("          datetime(FB.CREATE_DATE) desc, ");
		sql.append("          S.STAFF_NAME ");
		String requestGetFollowProblemList = sql.toString();
		
		String[] params = new String[stringParams.size()];
		for (int i = 0, length = stringParams.size(); i < length; i++) {
			params[i] = stringParams.get(i);
		}

		Cursor cTmp = null;
		String getCountFollowProblemList = " SELECT COUNT(*) AS TOTAL_ROW FROM ("
				+ requestGetFollowProblemList + ") ";

		try {
			if (!checkPagging) {
				cTmp = rawQuery(getCountFollowProblemList, params);
				if (cTmp != null) {
					cTmp.moveToFirst();
					result.total = cTmp.getInt(0);
				}
			}
		} catch (Exception e) {
		} finally {
			try {
				if (cTmp != null) {
					cTmp.close();
				}
			} catch (Exception e2) {
			}
		}
		Cursor c = null;
		try {
			c = rawQuery(requestGetFollowProblemList + extPage, params);
			if (c != null) {
				if (c.moveToFirst()) {
					List<TBHVFollowProblemItemDTO> listFollow = new ArrayList<TBHVFollowProblemItemDTO>();
					do {
						TBHVFollowProblemItemDTO note = new TBHVFollowProblemItemDTO();
						note.initDateWithCursor(c);
						listFollow.add(note);
					} while (c.moveToNext());
					result.list = listFollow;
				} else {
					result.list = new ArrayList<TBHVFollowProblemItemDTO>();
				}
			}
		} catch (Exception e) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e2) {
			}
		}
		return result;
	}

	/**
	 * 
	 * Update status cua van de
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param dto
	 * @return
	 * @return: long
	 * @throws:
	 */
	public long updateFollowProblemDone(TBHVFollowProblemItemDTO dto) {
		long returnCode = -1;
		try {
			ContentValues editedValues = new ContentValues();
			editedValues.put(STATUS, dto.status);
			String[] params = { String.valueOf(dto.id) };
			returnCode = update(editedValues, ID + " = ?", params);
		} catch (Exception e) {
		}
		return returnCode;
	}

	/**
	 * 
	 * lay combobox trang thai cho man hinh theo doi khac phuc cua TBHV
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return
	 * @return: List<ComboBoxDisplayProgrameItemDTO>
	 * @throws:
	 */
	public List<DisplayProgrameItemDTO> getComboboxProblemStatusOfTBHV() {
		List<DisplayProgrameItemDTO> result = new ArrayList<DisplayProgrameItemDTO>();
		return result;
	}

}
