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
import android.database.sqlite.SQLiteQueryBuilder;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.ApParamDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.DisplayProgrameItemDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.sale.customer.PostFeedbackView;
import com.viettel.utils.VTLog;

/**
 * Luu cac cau hinh
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class AP_PARAM_TABLE extends ABSTRACT_TABLE {
	// id
	public static final String AP_PARAM_ID = "AP_PARAM_ID";
	// ma tham so cau hinh
	public static final String AP_PARAM_CODE = "AP_PARAM_CODE";
	// gia tri tham so cau hinh
	public static final String VALUE = "VALUE";
	// trang thai: 0: het hieu luc, 1: dang hieu luc
	public static final String STATUS = "STATUS";
	// ghi chu
	public static final String DESCRIPTION = "DESCRIPTION";
	// ten
	public static final String AP_PARAM_NAME = "AP_PARAM_NAME";
	// type
	public static final String TYPE = "TYPE";

	private static final String TABLE_APP_PARAM = "AP_PARAM";

	public AP_PARAM_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_APP_PARAM;
		this.columns = new String[] { AP_PARAM_ID, AP_PARAM_CODE, VALUE, STATUS, DESCRIPTION, AP_PARAM_NAME, TYPE,
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
		ContentValues value = initDataRow((ApParamDTO) dto);
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
	public long insert(ApParamDTO dto) {
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
		ApParamDTO disDTO = (ApParamDTO) dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.apParamCode };
		return update(value, TYPE + " = ?", params);
	}

	/**
	 * Xoa 1 dong cua CSDL
	 * 
	 * @author: TruongHN
	 * @param code
	 * @return: int
	 * @throws:
	 */
	public int delete(String code) {
		String[] params = { code };
		return delete(TYPE + " = ?", params);
	}

	public long delete(AbstractTableDTO dto) {
		ApParamDTO paramDTO = (ApParamDTO) dto;
		String[] params = { paramDTO.apParamCode };
		return delete(TYPE + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: TruongHN
	 * @param id
	 * @return: ApParamDTO
	 * @throws:
	 */
	public ApParamDTO getRowById(String id) {
		ApParamDTO dto = null;
		Cursor c = null;
		try {
			String[] params = { id };
			c = query(TYPE + " = ?", params, null, null, null);
		} catch (Exception ex) {
			c = null;
		}
		if (c != null) {
			if (c.moveToFirst()) {
				dto = initApParamDTOFromCursor(c);
			}
		}
		if (c != null) {
			c.close();
		}
		return dto;
	}

	private ApParamDTO initApParamDTOFromCursor(Cursor c) {
		ApParamDTO dto = new ApParamDTO();
		dto.apParamId = (c.getLong(c.getColumnIndex(AP_PARAM_ID)));
		dto.apParamCode = (c.getString(c.getColumnIndex(AP_PARAM_CODE)));
		dto.status = (c.getString(c.getColumnIndex(STATUS)));
		dto.value = (c.getString(c.getColumnIndex(VALUE)));
		dto.description = (c.getString(c.getColumnIndex(DESCRIPTION)));
		dto.apParamName = (c.getString(c.getColumnIndex(AP_PARAM_NAME)));
		dto.type = (c.getString(c.getColumnIndex(TYPE)));
		return dto;
	}

	/**
	 * 
	 * lay tat ca cac dong cua CSDL
	 * 
	 * @author: TruongHN
	 * @return: Vector<ApParamDTO>
	 * @throws:
	 */
	public Vector<ApParamDTO> getAllRow() {
		Vector<ApParamDTO> v = new Vector<ApParamDTO>();
		Cursor c = null;
		try {
			c = query(null, null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			ApParamDTO dto;
			if (c.moveToFirst()) {
				do {
					dto = initApParamDTOFromCursor(c);
					v.addElement(dto);
				} while (c.moveToNext());
			}
			c.close();
		}
		return v;

	}

	/**
	 * * Lay ds param theo type
	 * 
	 * @author: TruongHN
	 * @param code
	 * @return: ArrayList<ApParamDTO>
	 * @throws:
	 */
	public ArrayList<ApParamDTO> getAllByType(String code) {
		ArrayList<ApParamDTO> v = new ArrayList<ApParamDTO>();
		Cursor c = null;
		try {
			String[] params = { "" + code };
			c = query(TYPE + " = ? AND STATUS = 1", params, null, null, null);
			if (c != null) {
				ApParamDTO dto;
				if (c.moveToFirst()) {
					do {
						dto = initApParamDTOFromCursor(c);
						v.add(dto);
					} while (c.moveToNext());
				}
			}
			// c.close();
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

	private ContentValues initDataRow(ApParamDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(AP_PARAM_ID, String.valueOf(dto.apParamId));
		editedValues.put(AP_PARAM_CODE, dto.apParamCode);
		editedValues.put(VALUE, dto.value);
		editedValues.put(STATUS, dto.status);
		editedValues.put(DESCRIPTION, dto.description);

		return editedValues;
	}

	public List<DisplayProgrameItemDTO> getListTypeDisplayPrograme() {
		List<DisplayProgrameItemDTO> result = new ArrayList<DisplayProgrameItemDTO>();
		String queryGetListTypeDisplayPrograme = "SELECT * FROM AP_PARAM WHERE TYPE = ? AND STATUS = 1";
		Cursor c = null;
		String params[] = { "DISPLAY_PROGR_TYPE" };
		try {
			// get total row first
			c = rawQuery(queryGetListTypeDisplayPrograme, params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						DisplayProgrameItemDTO dto = initComboBoxDisProItemFromCursor(c);
						result.add(dto);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			result = null;
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
	 * init danh sach van de
	 * 
	 * @author: YenNTH
	 * @param c
	 * @return
	 * @return: ComboBoxDisplayProgrameItemDTO
	 * @throws:
	 */
	private DisplayProgrameItemDTO initComboBoxDisProItemFromCursor(Cursor c) {
		// TODO Auto-generated method stub
		DisplayProgrameItemDTO result = new DisplayProgrameItemDTO();
		if (c.getColumnIndex("VALUE") >= 0) {
			result.value = (c.getString(c.getColumnIndex(VALUE)));
		} else {
			result.value = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("AP_PARAM_NAME") >= 0) {
			result.name = (c.getString(c.getColumnIndex(AP_PARAM_NAME)));
		} else {
			result.name = Constants.STR_BLANK;
		}
		if(c.getColumnIndex("TYPE") >= 0){
			result.type = c.getString(c.getColumnIndex("TYPE"));
		}else {
			result.type = Constants.STR_BLANK;
		}
		return result;
	}

	/**
	 * Lay ds van de cua TBHV
	 * 
	 * @author: YenNTH
	 * @return: List<DisplayProgrameItemDTO>
	 * @throws:
	 */
	public List<DisplayProgrameItemDTO> getListTBHVProblemType() {
		List<DisplayProgrameItemDTO> result = new ArrayList<DisplayProgrameItemDTO>();
		StringBuilder  var1 = new StringBuilder();
		var1.append("SELECT ap_param_code   AS VALUE, ");
		var1.append("       ap_param_name   AS AP_PARAM_NAME, ");
		var1.append("       type            AS TYPE ");
		var1.append("FROM   ap_param ");
		var1.append("WHERE  ( type LIKE 'FEEDBACK_TYPE_GSNPP' ");
		var1.append("          OR type LIKE 'FEEDBACK_TYPE_TTTT' ) ");
		var1.append("       AND status = 1 ");
		var1.append("ORDER  BY ap_param_name ");		
		Cursor c = null;
		String params[] = new String []{};
		try {
			// get total row first
			c = rawQuery(var1.toString(), params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						DisplayProgrameItemDTO dto = initComboBoxDisProItemFromCursor(c);
						result.add(dto);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			result = null;
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
	 * Lay cac gia tri default trong chuong trinh
	 * 
	 * @author: TruongHN
	 * @return: ArrayList<ApParamDTO>
	 * @throws:
	 */
	public ArrayList<ApParamDTO> getConstantsApp() {
		ArrayList<ApParamDTO> v = new ArrayList<ApParamDTO>();
		Cursor c = null;
		try {
			String query = "SELECT * FROM AP_PARAM WHERE TYPE IN (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) and STATUS = 1";
			String[] params = { "TIME_TEST_ORDER", "RADIUS_OF_POSITION", "TIME_TRIG_POSITION", "TIME_SYNC_TO_SERVER",
					"TIME_ALLOW_SYN_DATA_START", "TIME_ALLOW_SYN_DATA_END", "TIMEOUT_WHEN_IDLE", "ALLOW_EDIT_PROMOTION",
					"SERVER_IMAGE_PRO_VNM", "TIME_TRIG_POS_ATTEND","MAX_LOGKPI_ALLOW","MAX_TIME_WRONG", "INTERVAL_FUSED_POSITION",
					"FAST_INTERVAL_FUSED_POSITION","RADIUS_OF_POSITION_FUSED", "FUSED_POSTION_PRIORITY","TIME_REQUEST_TO_SERVER",
					"FLAG_VALIDATE_MOCK_LOCATION", "VT_MAP_SET_SERVER","VT_MAP_PROTOCOL","VT_MAP_PORT","VT_MAP_IP", "LOCATION_SPEED",
					"CF_NUM_FLOAT", "PG_SEND_ORDER", "EMAIL_SEND_DATA", "TYPE_HASH_PASS"};
			c = rawQuery(query, params);
			if (c != null) {
				ApParamDTO dto;
				if (c.moveToFirst()) {
					do {
						dto = initApParamDTOFromCursor(c);
						v.add(dto);
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
				VTLog.i("getConstantsApp", e.getMessage());
			}
		}
		return v;

	}

	/**
	 * 
	 * lay phan tram mat dinh cua 1 CTTB cho mot ngay lam viec
	 * 
	 * @return
	 * @return: float
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 11, 2013
	 */
	public float getPercentDefaultInDay() {
		float percent = 0;
		Cursor c = null;
		try {
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append("SELECT ap_param_code ");
			sqlQuery.append("FROM   ap_param ");
			sqlQuery.append("WHERE  status = 1 ");
			sqlQuery.append("       AND type = 'DP_ORDER_PERCENT' ");
			c = this.rawQuery(sqlQuery.toString(), new String[] {});
			// if (c.moveToFirst()) {
			if (c != null && c.moveToFirst()) {
				percent = c.getFloat(c.getColumnIndex(AP_PARAM_TABLE.AP_PARAM_CODE));
			}
		} catch (Exception e) {
			percent = 0;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return percent;
	}

	/**
	 * 
	 * lay danh sach loai van de NVBH, GSNPP
	 * 
	 * @return
	 * @return: float
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 11, 2013
	 */
	public Vector<ApParamDTO> getListTypeProblemNVBHGSNPP() {
		Cursor c = null;
		Vector<ApParamDTO> result = new Vector<ApParamDTO>();
		try {
			String sqlQuery = "SELECT AP_PARAM_NAME, AP_PARAM_CODE FROM AP_PARAM WHERE (TYPE = 'FEEDBACK_TYPE_NVBH' OR TYPE = 'FEEDBACK_TYPE_GSNPP') AND STATUS = 1 ORDER BY AP_PARAM_NAME ";
			String params[] = new String[] {};
			c = this.rawQuery(sqlQuery.toString(), params);
			if (c.moveToFirst()) {
				do {
					ApParamDTO item = new ApParamDTO();
					item.initObjectWithCursor(c);
					result.add(item);
				} while (c.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return result;
	}

	/**
	 * lay danh sach van de 
	 * MH them van de GSBH
	 * MH them ghi chu TTTT/NVBH
	 * MH them gop y NVBH/TTTT
	 * @author: TamPQ
	 * @param from
	 * @return
	 * @return: ArrayList<String>void
	 * @throws:
	 */
	public ArrayList<ApParamDTO> requestGetTypeFeedback(int from) {
		ArrayList<ApParamDTO> appList = new ArrayList<ApParamDTO>();
		Cursor c_ap_param = null;
		String sql=Constants.STR_BLANK;
		if (from == PostFeedbackView.FROM_TTTT_POST_FEEDBACK_VIEW || GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType== UserDTO.TYPE_TNPG) {
			sql=SQLiteQueryBuilder.buildQueryString(true, "ap_param", null, " type like 'FEEDBACK_TYPE_TTTT' and status = 1 ", null, null, "AP_PARAM_NAME", null);
		}else if (from == PostFeedbackView.FROM_GSNPP_POST_FEEDBACK_VIEW) {
			sql=SQLiteQueryBuilder.buildQueryString(true, "ap_param", null, " type like 'FEEDBACK_TYPE_NVBH' or type like 'FEEDBACK_TYPE_GSNPP' and status = 1 ", null, null, "AP_PARAM_NAME", null);
		} else {
			sql=SQLiteQueryBuilder.buildQueryString(true, "ap_param", null, " type like 'FEEDBACK_TYPE_NVBH' and status = 1 ", null, null, "AP_PARAM_NAME", null);
		}
		try {
			c_ap_param = rawQuery(sql, null);
			if (c_ap_param != null) {
				if (c_ap_param.moveToFirst()) {
					do {
						ApParamDTO dto = new ApParamDTO();
						dto.initObjectWithCursor(c_ap_param);
						appList.add(dto);
					} while (c_ap_param.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (c_ap_param != null) {
				c_ap_param.close();
			}
		}
		return appList;
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @param staffTypeId
	 * @return
	 * @return: Stringvoid
	 * @throws:
	 */
	public String requestStaffType(int staffTypeId) {
		String staffType = null;
		StringBuffer sqlRequest = new StringBuffer();
		sqlRequest.append("SELECT * FROM AP_PARAM WHERE AP_PARAM_ID = ? ");
		Cursor c = null;
		try {
			c = rawQuery(sqlRequest.toString(), new String[] { "" + staffTypeId });
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						ApParamDTO object = new ApParamDTO();
						object.initObjectWithCursor(c);
						staffType = object.apParamCode;
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception e2) {
				}
			}
		}
		return staffType;
	}
	 
	
	/**
	 * Lay X, Y de tinh thoi gian duoc nhap san luong ban
	 * @author: quangvt1
	 * @since: 10:19:07 13-06-2014
	 * @return: String
	 * @throws:  
	 * @return
	 */
	public ArrayList<ApParamDTO> getParamsForInputQuantityPrograme() { 
		StringBuffer  sql = new StringBuffer();
		sql.append("SELECT * ");
		sql.append("FROM   AP_PARAM ");
		sql.append("WHERE  TYPE LIKE 'REWARD' ");
		sql.append("       AND AP_PARAM_CODE IN ( 'START_DAY_REWARD', 'LAST_DAY_REWARD' ) ");
		sql.append("       AND STATUS = 1 ");
		sql.append("ORDER  BY AP_PARAM_CODE DESC ");
		ArrayList<ApParamDTO> result = null;
		Cursor c = null;
		try { 
			c = rawQuery(sql.toString(), new String[] {});
			if (c != null) {
				result = new ArrayList<ApParamDTO>();
				if (c.moveToFirst()) {
					do {
						ApParamDTO object = new ApParamDTO();
						object.initObjectWithCursor(c); 
						object.value = "+" +  object.value.trim() + " day";
						result.add(object);
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
					VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e2));
				}
			}
		}
		return result;
	}
	
	/**
	 * 
	 * lay danh sach loai van de TTTT
	 * 
	 * @return
	 * @return: float
	 * @throws:
	 * @author: YenNTH
	 * @date: Jan 11, 2013
	 */
	public Vector<ApParamDTO> getListTypeProblemTTTT() {
		Cursor c = null;
		Vector<ApParamDTO> result = new Vector<ApParamDTO>();
		try {
			String sqlQuery = "SELECT AP_PARAM_NAME, AP_PARAM_CODE FROM AP_PARAM WHERE TYPE = 'FEEDBACK_TYPE_TTTT' AND STATUS = 1 ORDER BY AP_PARAM_NAME ";
			String params[] = new String[] {};
			c = this.rawQuery(sqlQuery.toString(), params);
			if (c.moveToFirst()) {
				do {
					ApParamDTO item = new ApParamDTO();
					item.initObjectWithCursor(c);
					result.add(item);
				} while (c.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return result;
	}

	/**
	 * 
	 * Lay thoi gian cho phep ghi nhan don hang
	 * 
	 * @return
	 * @return: int
	 * @throws:
	 * @author: Nguyen Thanh Dung
	 * @date: Jan 11, 2013
	 */
	public int getC2ReportTime() {
		Cursor c = null;
		int c2ReportTime = 0;
		try {
			String sqlQuery = "SELECT AP_PARAM_NAME, AP_PARAM_CODE, VALUE FROM AP_PARAM WHERE TYPE = 'C2_REPORT_TIME' AND STATUS = 1 ";
			String params[] = new String[] {};
			c = this.rawQuery(sqlQuery.toString(), params);
			if (c.moveToFirst()) {
				c2ReportTime = (int) StringUtil.getLongFromSQliteCursor(c, "VALUE");
			}
		} catch (Exception e) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return c2ReportTime;
	}
}
