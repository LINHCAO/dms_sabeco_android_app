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
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.text.TextUtils;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.ApParamDTO;
import com.viettel.dms.dto.db.CustomerInputQuantityDTO;
import com.viettel.dms.dto.db.CustomerInputQuantityDetailDTO;
import com.viettel.dms.dto.db.CustomerListDoneProgrameDTO;
import com.viettel.dms.dto.db.ProInfoDTO;
import com.viettel.dms.dto.db.ProPeriodDTO;
import com.viettel.dms.dto.db.PromotionProgrameDTO;
import com.viettel.dms.dto.view.PromotionProgrameModel;
import com.viettel.dms.dto.view.SaleSupportProgramModel;
import com.viettel.dms.dto.view.TBHVPromotionProgrameDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * Luu thong tin chuong trinh khuyen mai
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class PROMOTION_PROGRAME_TABLE extends ABSTRACT_TABLE {
	// id chuong trinh khuyen mai
	public static final String PROMOTION_PROGRAM_ID = "PROMOTION_PROGRAM_ID";
	// ma chuong trinh
	public static final String PROMOTION_PROGRAM_CODE = "PROMOTION_PROGRAM_CODE";
	// ten chuong trinh
	public static final String PROMOTION_PROGRAM_NAME = "PROMOTION_PROGRAM_NAME";
	// trang thai 1: hieu luc, 0: het hieu luc
	public static final String STATUS = "STATUS";
	// ma phan loai chuong trinh khuyen mai
	public static final String TYPE = "TYPE";
	// dang khuyen mai
	public static final String PRO_FORMAT = "PRO_FORMAT";
	// tu ngay
	public static final String FROM_DATE = "FROM_DATE";
	// den ngay
	public static final String TO_DATE = "TO_DATE";
	// quan he 1: or, 0: and
	public static final String RELATION = "RELATION";
	// id NPP
	public static final String SHOP_ID = "SHOP_ID";
	// khao sat lai?
	public static final String CUSTOMER_TYPE = "CUSTOMER_TYPE";
	// khao sat lai?
	public static final String AREA_CODE = "AREA_CODE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// co tinh boi so khuyen mai hay khong? 1: co tinh, 0: khong tinh
	public static final String MULTIPLE = "MULTIPLE";
	// cho phep tinh toi uu hay khong? 1: co, 0: khong co
	public static final String RECURSIVE = "RECURSIVE";

	private static final String TABLE_PROMOTION_PROGRAME = "PROMOTION_PROGRAM";
	// mo ta chuong trinh khuyen mai
	private static final String DESCRIPTION = "DESCRIPTION";

	public PROMOTION_PROGRAME_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_PROMOTION_PROGRAME;
		this.columns = new String[] { PROMOTION_PROGRAM_ID,
				PROMOTION_PROGRAM_CODE, PROMOTION_PROGRAM_NAME, STATUS, TYPE,
				PRO_FORMAT, FROM_DATE, TO_DATE, RELATION, SHOP_ID,
				CUSTOMER_TYPE, AREA_CODE, CREATE_USER, UPDATE_USER,
				CREATE_DATE, UPDATE_DATE, MULTIPLE, RECURSIVE, SYN_STATE };
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
		ContentValues value = initDataRow((PromotionProgrameDTO) dto);
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
	public long insert(PromotionProgrameDTO dto) {
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
		PromotionProgrameDTO disDTO = (PromotionProgrameDTO) dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.PROMOTION_PROGRAM_ID };
		return update(value, PROMOTION_PROGRAM_ID + " = ?", params);
	}

	/**
	 * Xoa 1 dong cua CSDL
	 * 
	 * @author: TruongHN
	 * @param id
	 * @return: int
	 * @throws:
	 */
	public int delete(String code) {
		String[] params = { code };
		return delete(PROMOTION_PROGRAM_ID + " = ?", params);
	}

	public long delete(AbstractTableDTO dto) {
		PromotionProgrameDTO pro = (PromotionProgrameDTO) dto;
		String[] params = { "" + pro.PROMOTION_PROGRAM_ID };
		return delete(PROMOTION_PROGRAM_ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: DoanDM replaced
	 * @param id
	 * @return: DisplayPrdogrameLvDTO
	 * @throws:
	 */
	public PromotionProgrameDTO getRowById(String id) {
		PromotionProgrameDTO dto = null;
		Cursor c = null;
		try {
			String[] params = { id };
			c = query(PROMOTION_PROGRAM_ID + " = ?", params, null, null, null);
			if (c != null) {
				if (c.moveToFirst()) {
					dto = initDTOFromCursor(c);
				}
			}
		} catch (Exception ex) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return dto;
	}

	private PromotionProgrameDTO initDTO(Cursor c) {
		PromotionProgrameDTO dpDetailDTO = new PromotionProgrameDTO();

		dpDetailDTO.PROMOTION_PROGRAM_ID = (c.getInt(c
				.getColumnIndex(PROMOTION_PROGRAM_ID)));
		dpDetailDTO.PROMOTION_PROGRAM_CODE = (c.getString(c
				.getColumnIndex(PROMOTION_PROGRAM_CODE)));
		dpDetailDTO.PROMOTION_PROGRAM_NAME = (c.getString(c
				.getColumnIndex(PROMOTION_PROGRAM_NAME)));
		dpDetailDTO.STATUS = (c.getInt(c.getColumnIndex(STATUS)));
		dpDetailDTO.TYPE = (c.getString(c.getColumnIndex(TYPE)));
		dpDetailDTO.PRO_FORMAT = (c.getString(c.getColumnIndex(PRO_FORMAT)));

		dpDetailDTO.FROM_DATE = (c.getString(c.getColumnIndex(FROM_DATE)));
		dpDetailDTO.TO_DATE = (c.getString(c.getColumnIndex(TO_DATE)));
		dpDetailDTO.RELATION = (c.getInt(c.getColumnIndex(RELATION)));
		dpDetailDTO.MULTIPLE = (c.getInt(c.getColumnIndex(MULTIPLE)));
		dpDetailDTO.recursive = (c.getInt(c.getColumnIndex(RECURSIVE)));

		return dpDetailDTO;
	}

	/**
	 * 
	 * khoi tao doi tuong CTKM co kiem tra CTKM co hop le theo policy moi hay
	 * khong
	 * 
	 * @author: HaiTC3
	 * @param c
	 * @return
	 * @return: PromotionProgrameDTO
	 * @throws:
	 * @since: May 20, 2013
	 */
	private PromotionProgrameDTO initDTOHasCheckValidCTKM(Cursor c) {
		PromotionProgrameDTO resultObject = initDTO(c);
		if (resultObject != null) {
			// lay cac bien de kiem tra san pham co thuoc CTKM hay khong
			int COKHAIBAO1 = 0;
			int COKHAIBAO2 = 0;
			int COKHAIBAO3 = 0;
			int COKHAIBAO4 = 0;
			int COKHAIBAO5 = 0;
			int COKHAIBAO6 = 0;
			int COKHAIBAO7 = 0;
			int COKHAIBAO8 = 0;
			int COKHAIBAO9 = 0;
			int COKHAIBAO10 = 0;

			if (c.getColumnIndex("COKHAIBAO1") >= 0) {
				COKHAIBAO1 = c.getInt(c.getColumnIndex("COKHAIBAO1"));
			}
			if (c.getColumnIndex("COKHAIBAO2") >= 0) {
				COKHAIBAO2 = c.getInt(c.getColumnIndex("COKHAIBAO2"));
			}
			if (c.getColumnIndex("COKHAIBAO3") >= 0) {
				COKHAIBAO3 = c.getInt(c.getColumnIndex("COKHAIBAO3"));
			}
			if (c.getColumnIndex("COKHAIBAO4") >= 0) {
				COKHAIBAO4 = c.getInt(c.getColumnIndex("COKHAIBAO4"));
			}
			if (c.getColumnIndex("COKHAIBAO5") >= 0) {
				COKHAIBAO5 = c.getInt(c.getColumnIndex("COKHAIBAO5"));
			}
			if (c.getColumnIndex("COKHAIBAO6") >= 0) {
				COKHAIBAO6 = c.getInt(c.getColumnIndex("COKHAIBAO6"));
			}
			if (c.getColumnIndex("COKHAIBAO7") >= 0) {
				COKHAIBAO7 = c.getInt(c.getColumnIndex("COKHAIBAO7"));
			}
			if (c.getColumnIndex("COKHAIBAO8") >= 0) {
				COKHAIBAO8 = c.getInt(c.getColumnIndex("COKHAIBAO8"));
			}
			if (c.getColumnIndex("COKHAIBAO9") >= 0) {
				COKHAIBAO9 = c.getInt(c.getColumnIndex("COKHAIBAO9"));
			}
			if (c.getColumnIndex("COKHAIBAO10") >= 0) {
				COKHAIBAO10 = c.getInt(c.getColumnIndex("COKHAIBAO10"));
			}
			boolean result = false;
			if (COKHAIBAO1 == 0) {
				result = true;
			} else {
				result = COKHAIBAO2 > 0 ? true : false;
			}
			if (result) {
				if (COKHAIBAO3 == 0) {
					result = true;
				} else {
					result = COKHAIBAO4 > 0 ? true : false;
				}
			} else {
				resultObject.isInvalid = false;
			}
			if (result) {
				boolean result1 = false;
				if (COKHAIBAO5 == 0) {
					result1 = true;
				} else {
					result1 = COKHAIBAO6 == COKHAIBAO5 ? true : false;
				}

				boolean result2 = false;
				if (COKHAIBAO7 == 0) {
					result2 = true;
				} else {
					result2 = COKHAIBAO8 == COKHAIBAO7 ? true : false;
				}
				
				
				boolean result3 = false;
				if (COKHAIBAO9 == 0) {
					result3 = true;
				} else {
					result3 = COKHAIBAO10 > 0 ? true : false;
				}
				result = result1 && result2 && result3;
			} else {
				resultObject.isInvalid = false;
			}
			resultObject.isInvalid = result;
		}
		return resultObject;
	}

	/**
	 * 
	 * parse data to check CTKM invalid ?
	 * 
	 * @author: HaiTC3
	 * @param c
	 * @return
	 * @return: boolean
	 * @throws:
	 * @since: May 24, 2013
	 */
	public boolean checkPromotionProgramInvalid(Cursor c) {
		boolean kq = false;
		// lay cac bien de kiem tra san pham co thuoc CTKM hay khong
		int COKHAIBAO1 = 0;
		int COKHAIBAO2 = 0;
		int COKHAIBAO3 = 0;
		int COKHAIBAO4 = 0;
		int COKHAIBAO5 = 0;
		int COKHAIBAO6 = 0;
		int COKHAIBAO7 = 0;
		int COKHAIBAO8 = 0;

		if (c.getColumnIndex("COKHAIBAO1") >= 0) {
			COKHAIBAO1 = c.getInt(c.getColumnIndex("COKHAIBAO1"));
		}
		if (c.getColumnIndex("COKHAIBAO2") >= 0) {
			COKHAIBAO2 = c.getInt(c.getColumnIndex("COKHAIBAO2"));
		}
		if (c.getColumnIndex("COKHAIBAO3") >= 0) {
			COKHAIBAO3 = c.getInt(c.getColumnIndex("COKHAIBAO3"));
		}
		if (c.getColumnIndex("COKHAIBAO4") >= 0) {
			COKHAIBAO4 = c.getInt(c.getColumnIndex("COKHAIBAO4"));
		}
		if (c.getColumnIndex("COKHAIBAO5") >= 0) {
			COKHAIBAO5 = c.getInt(c.getColumnIndex("COKHAIBAO5"));
		}
		if (c.getColumnIndex("COKHAIBAO6") >= 0) {
			COKHAIBAO6 = c.getInt(c.getColumnIndex("COKHAIBAO6"));
		}
		if (c.getColumnIndex("COKHAIBAO7") >= 0) {
			COKHAIBAO7 = c.getInt(c.getColumnIndex("COKHAIBAO7"));
		}
		if (c.getColumnIndex("COKHAIBAO8") >= 0) {
			COKHAIBAO8 = c.getInt(c.getColumnIndex("COKHAIBAO8"));
		}
		boolean resultTMP = false;
		if (COKHAIBAO1 == 0) {
			resultTMP = true;
		} else {
			resultTMP = COKHAIBAO2 > 0 ? true : false;
		}
		if (resultTMP) {
			if (COKHAIBAO3 == 0) {
				resultTMP = true;
			} else {
				resultTMP = COKHAIBAO4 > 0 ? true : false;
			}
		} else {
			kq = false;
		}
		if (resultTMP) {
			boolean result1 = false;
			if (COKHAIBAO5 == 0) {
				result1 = true;
			} else {
				result1 = COKHAIBAO6 == COKHAIBAO5 ? true : false;
			}

			boolean result2 = false;
			if (COKHAIBAO7 == 0) {
				result2 = true;
			} else {
				result2 = COKHAIBAO8 == COKHAIBAO7 ? true : false;
			}
			resultTMP = result1 & result2;
		} else {
			kq = false;
		}
		kq = resultTMP;
		return kq;
	}

	private PromotionProgrameDTO initDTOFromCursor(Cursor c) {
		PromotionProgrameDTO dpDetailDTO = new PromotionProgrameDTO();

		dpDetailDTO.PROMOTION_PROGRAM_ID = (c.getInt(c
				.getColumnIndex(PROMOTION_PROGRAM_ID)));
		dpDetailDTO.PROMOTION_PROGRAM_CODE = (c.getString(c
				.getColumnIndex(PROMOTION_PROGRAM_CODE)));
		dpDetailDTO.PROMOTION_PROGRAM_NAME = (c.getString(c
				.getColumnIndex(PROMOTION_PROGRAM_NAME)));
		dpDetailDTO.STATUS = (c.getInt(c.getColumnIndex(STATUS)));
		dpDetailDTO.TYPE = (c.getString(c.getColumnIndex(TYPE)));
		dpDetailDTO.PRO_FORMAT = (c.getString(c.getColumnIndex(PRO_FORMAT)));

		dpDetailDTO.FROM_DATE = (c.getString(c.getColumnIndex(FROM_DATE)));
		dpDetailDTO.TO_DATE = (c.getString(c.getColumnIndex(TO_DATE)));
		dpDetailDTO.RELATION = (c.getInt(c.getColumnIndex(RELATION)));
		dpDetailDTO.SHOP_ID = (c.getInt(c.getColumnIndex(SHOP_ID)));

		dpDetailDTO.CREATE_USER = (c.getString(c.getColumnIndex(CREATE_USER)));
		dpDetailDTO.UPDATE_USER = (c.getString(c.getColumnIndex(UPDATE_USER)));
		dpDetailDTO.CREATE_DATE = (c.getString(c.getColumnIndex(CREATE_DATE)));
		dpDetailDTO.UPDATE_DATE = (c.getString(c.getColumnIndex(UPDATE_DATE)));
		dpDetailDTO.MULTIPLE = (c.getInt(c.getColumnIndex(MULTIPLE)));

		return dpDetailDTO;
	}

	private ContentValues initDataRow(PromotionProgrameDTO dto) {
		ContentValues editedValues = new ContentValues();

		editedValues.put(PROMOTION_PROGRAM_ID, dto.PROMOTION_PROGRAM_ID);
		editedValues.put(PROMOTION_PROGRAM_CODE, dto.PROMOTION_PROGRAM_CODE);
		editedValues.put(PROMOTION_PROGRAM_NAME, dto.PROMOTION_PROGRAM_NAME);
		editedValues.put(STATUS, dto.STATUS);
		editedValues.put(TYPE, dto.TYPE);
		editedValues.put(PRO_FORMAT, dto.PRO_FORMAT);

		editedValues.put(FROM_DATE, dto.FROM_DATE);
		editedValues.put(TO_DATE, dto.TO_DATE);
		editedValues.put(RELATION, dto.RELATION);
		editedValues.put(SHOP_ID, dto.SHOP_ID);
		editedValues.put(CREATE_USER, dto.CREATE_USER);
		editedValues.put(UPDATE_USER, dto.UPDATE_USER);
		editedValues.put(CREATE_DATE, dto.CREATE_DATE);
		editedValues.put(UPDATE_DATE, dto.UPDATE_DATE);
		editedValues.put(MULTIPLE, dto.MULTIPLE);

		return editedValues;
	}

	/**
	 * Get list product
	 * 
	 * @author: ThuatTQ
	 * @param ext
	 * @return: List<SaleOrderDetailDTO>
	 * @throws:
	 */
	public SaleSupportProgramModel getListSaleSupportPrograme(
			String shopId, String ext, boolean checkLoadMore) {
		AP_PARAM_TABLE table = new AP_PARAM_TABLE(mDB);
		ArrayList<ApParamDTO> listParams = table.getParamsForInputQuantityPrograme();
		if(listParams.size() < 2){
			return null;
		} 
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		ArrayList<String> params = new ArrayList<String>();
		StringBuffer  sql = new StringBuffer();
		sql.append("SELECT PP.PRO_INFO_ID, ");
		sql.append("       PP.PRO_INFO_CODE, ");
		sql.append("       PP.TYPE, ");
		sql.append("       PP.PRO_INFO_NAME, ");
		sql.append("       STRFTIME('%d/%m/%Y', PP.FROM_DATE) AS FROM_DATE, ");
		sql.append("       STRFTIME('%d/%m/%Y', PP.TO_DATE)   AS TO_DATE, ");
		sql.append("       CASE ");
		sql.append("         WHEN P1.NUMNEED > 0 THEN 1 ");
		sql.append("         ELSE 0 ");
		sql.append("       END                                AS NEED_INPUT_QUANTITY, ");
		sql.append("       CASE ");
		sql.append("         WHEN P2.NUMDONE > 0 THEN 1 ");
		sql.append("         ELSE 0 ");
		sql.append("       END                                AS IS_HAVE_DONE ");
		sql.append("FROM   PRO_INFO PP, ");
		sql.append("       PRO_SHOP_MAP PSM, ");
		sql.append("       PRO_INFO_CUS PIC, ");
		sql.append("       CHANNEL_TYPE CT ");
		sql.append("       LEFT JOIN (SELECT COUNT(*) NUMNEED, ");
		sql.append("                         P1.PRO_INFO_ID ");
		sql.append("                  FROM   PRO_PERIOD AS P1 ");
		sql.append("                  WHERE  DATE(?) >= DATE(P1.TO_DATE, ?) ");
		params.add(date_now);
		params.add(listParams.get(0).value);
		sql.append("                         AND DATE(?) <= DATE(P1.TO_DATE, ?) ");
		params.add(date_now);
		params.add(listParams.get(1).value);
		sql.append("                  GROUP  BY P1.PRO_INFO_ID) AS P1 ");
		sql.append("              ON P1.PRO_INFO_ID = PP.PRO_INFO_ID ");
		sql.append("       LEFT JOIN (SELECT COUNT(*) NUMDONE, ");
		sql.append("                         P2.PRO_INFO_ID ");
		sql.append("                  FROM   PRO_PERIOD AS P2 ");
		sql.append("                  WHERE  DATE(?) >= DATE(P2.TO_DATE, ?) ");
		params.add(date_now);
		params.add(listParams.get(0).value);
		sql.append("                  GROUP  BY P2.PRO_INFO_ID) AS P2 ");
		sql.append("              ON P2.PRO_INFO_ID = PP.PRO_INFO_ID ");
		sql.append("WHERE  1 = 1 ");
		sql.append("       AND (IFNULL (DATE(PP.TO_DATE) >= DATE(?), 1) ");
		params.add(date_now);
		sql.append("       OR date(PP.TO_DATE) >= date('now', 'localtime', ? )) ");
		params.add(listParams.get(1).value.replace("+", "-"));
		sql.append("       AND PP.STATUS = 1 ");
		sql.append("       AND PSM.SHOP_ID IN (SELECT PARENT_SHOP_ID ");
		sql.append("                           FROM   SHOP ");
		sql.append("                           WHERE  SHOP_ID = ?) ");
		params.add(shopId);
		sql.append("       AND PSM.PRO_INFO_ID = PP.PRO_INFO_ID ");
		sql.append("       AND PP.PRO_INFO_ID = PIC.PRO_INFO_ID ");
		sql.append("       AND PIC.TYPE = CT.OBJECT_TYPE ");
		sql.append("       AND CT.TYPE = 3 ");
		sql.append("       AND CT.STATUS = 1 ");
		sql.append("GROUP  BY PP.PRO_INFO_ID ");
		sql.append("ORDER  BY DATETIME(PP.FROM_DATE) DESC, ");
		sql.append("          CASE ");
		sql.append("            WHEN PP.TO_DATE IS NULL THEN 1 ");
		sql.append("            ELSE 0 ");
		sql.append("          END, ");
		sql.append("          DATETIME(PP.TO_DATE), ");
		sql.append("          PP.PRO_INFO_CODE, ");
		sql.append("          PP.PRO_INFO_NAME ");
		
		String queryGetListProductForOrder = sql.toString();
		String getCountProductList = " SELECT COUNT(*) AS TOTAL_ROW FROM ("
				+ queryGetListProductForOrder + ") ";

		SaleSupportProgramModel modelData = new SaleSupportProgramModel();
		
		Cursor cTmp = null;
		try {
			// get total row first
			if (checkLoadMore == false) {
				cTmp = rawQuery(getCountProductList,
						params.toArray(new String[params.size()]));
				int total = 0;
				if (cTmp != null) {
					cTmp.moveToFirst();
					total = cTmp.getInt(0);
				}
				modelData.total = total;
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			try {
				if (cTmp != null) {
					cTmp.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		Cursor c = null;
		try{
			c = rawQuery(queryGetListProductForOrder + ext,
					params.toArray(new String[params.size()]));

			if (c != null) {

				if (c.moveToFirst()) {
					do {
						ProInfoDTO orderJoinTableDTO = new ProInfoDTO();
						orderJoinTableDTO.initDataForRequestGetPromotionProgrameDetail(c);
						modelData.listPrograme.add(orderJoinTableDTO);
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
				// TODO: handle exception
			}
		}

		return modelData;
	}
	
	/**
	 * Lay thong tin chi tiet 1 chuong trinh
	 * 
	 * @author: quangvt
	 * @param pro_info_id
	 * @return: ProInfoDTO
	 * @throws:
	 */
	public ProInfoDTO getDetailPrograme(String pro_info_id) { 
		ProInfoDTO data  = null;
		ArrayList<String> params = new ArrayList<String>();
		
		StringBuilder sqlQuery = new StringBuilder(); 
		sqlQuery.append("select pp.pro_info_id, ");
		sqlQuery.append(" pp.pro_info_code, ");
		sqlQuery.append(" pp.type, ");
		sqlQuery.append(" pp.pro_info_name, ");
		sqlQuery.append(" strftime('%d/%m/%Y', PP.from_date) AS FROM_DATE, ");
		sqlQuery.append(" strftime('%d/%m/%Y', pp.to_date)   as TO_DATE, ");
		sqlQuery.append(" PS.TOTAL_LEVEL 					 AS TOTAL_LEVEL "); 
		sqlQuery.append(" from pro_info pp,  PRO_STRUCTURE PS  "); 
		sqlQuery.append(" where  1 = 1  "); 
		sqlQuery.append("       AND pp.pro_info_id = ? ");
		params.add(pro_info_id); 
		sqlQuery.append("       AND pp.STATUS = 1 ");
		sqlQuery.append("       AND pp.PRO_INFO_ID = PS.PRO_INFO_ID ");
		  
		Cursor c = null; 
		try {
			 
			c = rawQueries(sqlQuery.toString(), params);
			
			if (c != null) { 
				if (c.moveToFirst()) { 
						data = new ProInfoDTO();
						data.initDataForRequestGetPromotionProgrameDetail(c); 
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
			}
		}
		
		return data;
	}
	
	/**
	 * Lay danh sach chu ki tinh thuong da qua cua CT HTBH
	 * 
	 * @author: quangvt
	 * @param pro_info_id
	 * @return: ProInfoDTO
	 * @throws:
	 */
	public List<ProPeriodDTO> getListProPeriodDone(String pro_info_id) { 
		AP_PARAM_TABLE table = new AP_PARAM_TABLE(mDB);
		ArrayList<ApParamDTO> listParams = table.getParamsForInputQuantityPrograme();
		if(listParams.size() < 2){
			return new ArrayList<ProPeriodDTO>();
		}
		
		List<ProPeriodDTO> data  = null;
		ArrayList<String> params = new ArrayList<String>();
		
		StringBuffer  sql = new StringBuffer();
		sql.append("SELECT P.PRO_PERIOD_ID   AS PRO_PERIOD_ID, ");
		sql.append("       P.PRO_PERIOD_NAME AS PRO_PERIOD_NAME, ");
		sql.append("       P.PRO_INFO_ID     AS PRO_INFO_ID, ");
		sql.append("       P.FROM_DATE       AS FROM_DATE, ");
		sql.append("       P.TO_DATE         AS TO_DATE ");
		sql.append("FROM   PRO_PERIOD P ");
		sql.append("WHERE  P.PRO_INFO_ID = ? ");
		params.add(pro_info_id);
		sql.append("       AND DATE('now', 'localtime') >= DATE(P.TO_DATE, ?) ");
		params.add(listParams.get(0).value);
		sql.append("ORDER BY  P.TO_DATE DESC ");
		  
		
		Cursor c = null; 
		try { 
			c = rawQueries(sql.toString(), params);
			
			if (c != null) { 
				data = new ArrayList<ProPeriodDTO>();
				if (c.moveToFirst()) {  
					do{
						ProPeriodDTO dto = new ProPeriodDTO();
						dto.initFromCursor(c);
						data.add(dto);
					}while(c.moveToNext());
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
			}
		}
		
		return data;
	}
	
	/**
	 * Lay danh sach chuong trinh can nhap san luong
	 * @author: quangvt1
	 * @since: 08:18:56 12-06-2014
	 * @return: ProInfoDTO
	 * @throws:  
	 * @param pro_info_id
	 * @return
	 */
	public SaleSupportProgramModel getAllProgrameNeedTypeQuantity(String shopid) {
		AP_PARAM_TABLE table = new AP_PARAM_TABLE(mDB);
		ArrayList<ApParamDTO> listParams = table.getParamsForInputQuantityPrograme();
		if(listParams.size() < 2){
			return null;
		}
		
		SaleSupportProgramModel data  = null;
		ArrayList<String> params = new ArrayList<String>();
		
		StringBuffer  sql = new StringBuffer();
		sql.append("SELECT PP.PRO_INFO_ID, ");
		sql.append("       PP.PRO_INFO_CODE, ");
		sql.append("       PP.TYPE, ");
		sql.append("       PP.PRO_INFO_NAME, ");
		sql.append("       STRFTIME('%d/%m/%Y', PP.FROM_DATE) AS FROM_DATE, ");
		sql.append("       STRFTIME('%d/%m/%Y', PP.TO_DATE)   AS TO_DATE ");
		sql.append("FROM   PRO_INFO PP, ");
		sql.append("       PRO_SHOP_MAP PSM, ");
		sql.append("       PRO_INFO_CUS PIC, ");
		sql.append("       PRO_PERIOD PR, ");
		sql.append("       CHANNEL_TYPE CT ");
		sql.append("WHERE  1 = 1 ");
		sql.append("       AND IFNULL (DATE(PP.TO_DATE) >= DATE('now', 'localtime'), 1) ");
		sql.append("       AND PP.STATUS = 1 ");
		sql.append("       AND PSM.SHOP_ID IN (SELECT PARENT_SHOP_ID ");
		sql.append("                           FROM   SHOP ");
		sql.append("                           WHERE  SHOP_ID = ?) ");
		params.add(shopid);
		sql.append("       AND PSM.PRO_INFO_ID = PP.PRO_INFO_ID ");
		sql.append("       AND PP.PRO_INFO_ID = PIC.PRO_INFO_ID ");
		sql.append("       AND PIC.TYPE = CT.OBJECT_TYPE ");
		sql.append("       AND CT.TYPE = 3 ");
		sql.append("       AND CT.STATUS = 1 ");
		sql.append("       AND PR.PRO_INFO_ID = PP.PRO_INFO_ID ");
		sql.append("       AND DATE('now', 'localtime') >= DATE(PR.TO_DATE, ?) ");
		params.add(listParams.get(0).value);
		sql.append("       AND DATE('now', 'localtime') <= DATE(PR.TO_DATE, ?) ");
		params.add(listParams.get(1).value);
		sql.append("GROUP  BY PP.PRO_INFO_ID ");
		sql.append("ORDER  BY DATETIME(PP.FROM_DATE) ");
		
		Cursor c = null; 
		try {
			 
			c = rawQueries(sql.toString(), params);
			
			if (c != null) {
				data = new SaleSupportProgramModel();
				if (c.moveToFirst()) {
					do {
						ProInfoDTO dto = new ProInfoDTO();
						dto.initDataForRequestGetPromotionProgrameDetail(c);
						dto.isHaveDone = true;
						data.listPrograme.add(dto);
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
		
		return data;
	}

	/**
	 * 
	 * Lay ds chuong trinh KM cua GSNPP
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param staffId
	 * @param ext
	 * @param checkLoadMore
	 * @return
	 * @return: PromotionProgrameModel
	 * @throws:
	 */
	public PromotionProgrameModel getListSupervisorPromotionPrograme(
			ArrayList<String> shopIdArray, String ext, boolean checkLoadMore) {
		String idListString = TextUtils.join(",", shopIdArray);
		ArrayList<String> params = new ArrayList<String>();

		StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append("SELECT DISTINCT PP.promotion_program_code, ");
		sqlQuery.append("       PP.promotion_program_name, ");
		sqlQuery.append("       Strftime('%d/%m/%Y', PP.from_date) AS FROM_DATE, ");
		sqlQuery.append("       Strftime('%d/%m/%Y', PP.to_date)   AS TO_DATE, ");
		sqlQuery.append("       PP.description ");
		sqlQuery.append("FROM   promotion_program PP, ");
		sqlQuery.append("       promotion_shop_map PSM ");
		sqlQuery.append("WHERE  Ifnull (Date(PP.to_date) >= Date('now', 'localtime'), 1) ");
		sqlQuery.append("       AND PP.status = 1 AND PSM.status = 1 ");
		sqlQuery.append("	AND PSM.SHOP_ID IN ( ");
		sqlQuery.append(idListString);
		sqlQuery.append(") ");
		sqlQuery.append("       AND PSM.promotion_program_id = PP.promotion_program_id ");
		sqlQuery.append("ORDER  BY PP.from_date DESC, CASE WHEN PP.to_date IS NULL THEN 1 ELSE 0 END,");
		sqlQuery.append("          PP.to_date, ");
		sqlQuery.append("          PP.promotion_program_code, ");
		sqlQuery.append("          PP.promotion_program_name ");

		String queryGetListProductForOrder = sqlQuery.toString();
		String getCountProductList = " SELECT COUNT(*) AS TOTAL_ROW FROM ("
				+ queryGetListProductForOrder + ") ";

		PromotionProgrameModel modelData = new PromotionProgrameModel();
		List<PromotionProgrameDTO> list = new ArrayList<PromotionProgrameDTO>();
		
		Cursor cTmp = null;
		try {
			// get total row first
			if (checkLoadMore == false) {
				cTmp = rawQuery(getCountProductList,
						params.toArray(new String[params.size()]));
				int total = 0;
				if (cTmp != null) {
					cTmp.moveToFirst();
					total = cTmp.getInt(0);
				}
				modelData.setTotal(total);
			}
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (cTmp != null) {
					cTmp.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}

		Cursor c = null;
		try{
			c = rawQuery(queryGetListProductForOrder + ext,
					params.toArray(new String[params.size()]));

			if (c != null) {

				if (c.moveToFirst()) {
					PromotionProgrameDTO orderJoinTableDTO = null;
					do {
						orderJoinTableDTO = this
								.initPromotionProgrameObjectFromGetStatement(c);
						list.add(orderJoinTableDTO);
					} while (c.moveToNext());
				}
			}
			modelData.setModelData(list);
		} catch (Exception e) {
			return null;
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
	 * Lay ds chuong trinh KM
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param shopId
	 * @param ext
	 * @param checkLoadMore
	 * @return
	 * @return: PromotionProgrameModel
	 * @throws:
	 */
	public TBHVPromotionProgrameDTO getListTBHVPromotionPrograme(String shopId,
			int page, boolean checkLoadMore) {
		SHOP_TABLE shopTB = new SHOP_TABLE(this.mDB);
		ArrayList<String> listParentShopId = shopTB.getShopRecursive(shopId);
		ArrayList<String> listShopId = shopTB.getShopRecursiveReverse(shopId);
		String strListParentShop = TextUtils.join(",", listParentShopId);
		String strListShop = TextUtils.join(",", listShopId);
		String dateNow= DateUtils.now();
		List<String> param= new ArrayList<String>();
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT * ");
		sqlQuery.append("FROM  ( select DISTINCT Strftime('%d/%m/%Y', from_date) AS FROM_DATE,    ");
		sqlQuery.append("               Strftime('%d/%m/%Y', to_date)   AS TO_DATE, ");
		sqlQuery.append("               Trim(description)               AS DESCRIPTION,  ");
		sqlQuery.append("				promotion_program_code as PROMOTION_PROGRAM_CODE, ");
		sqlQuery.append("				promotion_program_name as PROMOTION_PROGRAM_NAME, ");
		sqlQuery.append("				promotion_program_id as PROMOTION_PROGRAM_ID ");
		sqlQuery.append("               from promotion_program ");
		sqlQuery.append("				where status = 1 ");
		sqlQuery.append("				and Ifnull(Date(to_date) >= Date(?), 1)) inner join ");
		param.add(dateNow);
		sqlQuery.append("       (select promotion_program_id as PSMID  ");
		sqlQuery.append("		from promotion_shop_map	");
		sqlQuery.append("		where status = 1	");
		sqlQuery.append("		and shop_id IN ("+ strListParentShop+","+strListShop+") )	");
		sqlQuery.append("		on PSMID= PROMOTION_PROGRAM_ID ");

		sqlQuery.append("GROUP  BY PROMOTION_PROGRAM_CODE, ");
		sqlQuery.append("          PROMOTION_PROGRAM_NAME, ");
		sqlQuery.append("          FROM_DATE, ");
		sqlQuery.append("          TO_DATE ");
		
		String getCountProductList = " SELECT COUNT(*) AS TOTAL_ROW FROM ("
				+ sqlQuery.toString() + ") ";
		
		sqlQuery.append("ORDER  BY FROM_DATE DESC, CASE WHEN TO_DATE IS NULL THEN 1 ELSE 0 END,");
		sqlQuery.append("          TO_DATE, ");
		sqlQuery.append("          PROMOTION_PROGRAM_CODE, ");
		sqlQuery.append("          PROMOTION_PROGRAM_NAME ");

		TBHVPromotionProgrameDTO modelData = new TBHVPromotionProgrameDTO();
		List<PromotionProgrameDTO> list = new ArrayList<PromotionProgrameDTO>();
		modelData.setModelData(list);


		Cursor cTmp = null;
		try {
			// get total row first
			if (!checkLoadMore) {
				String[] params= new String[param.size()];
				params=param.toArray(params);
				cTmp = this.rawQuery(getCountProductList.toString(), params);
				int total = 0;
				if (cTmp != null) {
					cTmp.moveToFirst();
					total = cTmp.getInt(0);
				}
				modelData.setTotal(total);
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
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
		sqlQuery.append("limit ?, ? ");
		param.add(String.valueOf(page * Constants.NUM_ITEM_PER_PAGE));
		param.add(String.valueOf(Constants.NUM_ITEM_PER_PAGE));
		String[] params= new String[param.size()];
		params=param.toArray(params);
		Cursor c = null;
		try{
			c = this.rawQuery(sqlQuery.toString(), params);

			if (c != null) {
				if (c.moveToFirst()) {
					PromotionProgrameDTO orderJoinTableDTO = null;
					do {
						orderJoinTableDTO = this
								.initPromotionProgrameObjectFromGetStatement(c);
						list.add(orderJoinTableDTO);
					} while (c.moveToNext());
				}
			}
			modelData.setModelData(list);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
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
	 * Khoi tao doi tuong chuong trinh KM
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param c
	 * @return
	 * @return: PromotionProgrameDTO
	 * @throws:
	 */
	private PromotionProgrameDTO initPromotionProgrameObjectFromGetStatement(
			Cursor c) {
		PromotionProgrameDTO dto = new PromotionProgrameDTO();

		if (c.getColumnIndex(PROMOTION_PROGRAM_ID) >= 0) {
			dto.PROMOTION_PROGRAM_ID = c.getInt(c
					.getColumnIndex(PROMOTION_PROGRAM_ID));
		} else {
			dto.PROMOTION_PROGRAM_ID = 0;
		}

		if (c.getColumnIndex(PROMOTION_PROGRAM_CODE) >= 0) {
			dto.PROMOTION_PROGRAM_CODE = c.getString(c
					.getColumnIndex(PROMOTION_PROGRAM_CODE));
		} else {
			dto.PROMOTION_PROGRAM_CODE = "";
		}

		if (c.getColumnIndex(PROMOTION_PROGRAM_NAME) >= 0) {
			dto.PROMOTION_PROGRAM_NAME = c.getString(c
					.getColumnIndex(PROMOTION_PROGRAM_NAME));
		} else {
			dto.PROMOTION_PROGRAM_NAME = "";
		}

		if (c.getColumnIndex(FROM_DATE) >= 0) {
			dto.FROM_DATE = DateUtils.parseDateFromSqlLite(c.getString(c
					.getColumnIndex(FROM_DATE)));
		} else {
			dto.FROM_DATE = "";
		}

		if (c.getColumnIndex(TO_DATE) >= 0) {
			dto.TO_DATE = DateUtils.parseDateFromSqlLite(c.getString(c
					.getColumnIndex(TO_DATE)));
		} else {
			dto.TO_DATE = "";
		}
		if (dto.TO_DATE == null) {
			dto.TO_DATE = "";
		}
		// DESCRIPTION
		if (c.getColumnIndex(DESCRIPTION) >= 0) {
			dto.DESCRIPTION = c.getString(c.getColumnIndex(DESCRIPTION));
		} else {
			dto.DESCRIPTION = "";
		}

		return dto;
	}

	/**
	 * 
	 * get promotion programe detail
	 * 
	 * @author: HaiTC3
	 * @param data
	 * @return
	 * @return: PromotionProgrameDTO
	 * @throws:
	 */
	public PromotionProgrameDTO getPromotionProgrameDetail(Bundle data) {
		String promotionCode = data
				.getString(IntentConstants.INTENT_PROMOTION_CODE);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		SHOP_TABLE shopTB = new SHOP_TABLE(this.mDB);
		ArrayList<String> listShopId = shopTB.getShopRecursive(shopId);
		String strListShop = TextUtils.join(",", listShopId);

		StringBuffer queryGetPromotionDetail = new StringBuffer();
		queryGetPromotionDetail
				.append("SELECT pp.promotion_program_code                AS PROMOTION_PROGRAM_CODE, ");
		queryGetPromotionDetail
				.append("       pp.promotion_program_name                AS PROMOTION_PROGRAM_NAME, ");
		queryGetPromotionDetail
				.append("       type                                     AS TYPE, ");
		queryGetPromotionDetail
				.append("       Strftime('%d/%m/%Y', Date(pp.from_date)) AS FROM_DATE, ");
		queryGetPromotionDetail
				.append("       Strftime('%d/%m/%Y', Date(pp.to_date))   AS TO_DATE, ");
		queryGetPromotionDetail
				.append("       pp.description                           AS DESCRIPTION ");
		queryGetPromotionDetail.append("FROM   promotion_program pp, ");
		queryGetPromotionDetail.append("       promotion_shop_map psm ");
		queryGetPromotionDetail.append("WHERE  pp.promotion_program_code = ? ");
		queryGetPromotionDetail.append("       AND pp.status = 1 ");
		queryGetPromotionDetail
				.append("       AND pp.promotion_program_id = psm.promotion_program_id ");
		queryGetPromotionDetail.append("       AND psm.shop_id IN ( ");
		queryGetPromotionDetail.append(strListShop + ") ");
		queryGetPromotionDetail.append("       AND psm.status = 1 ");

		String[] params = new String[] { promotionCode };

		PromotionProgrameDTO promotionDetail = new PromotionProgrameDTO();
		Cursor c = null;
		try {
			c = rawQuery(queryGetPromotionDetail.toString(), params);

			if (c != null) {

				if (c.moveToFirst()) {
					promotionDetail
							.initDataForRequestGetPromotionProgrameDetail(c);
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

		return promotionDetail;
	}


	/**
	 * 
	 * lay doi tuong CTKM voi co them cac thuoc tinh dung de check CTKM co hop
	 * le hay khong
	 * 
	 * @author: HaiTC3
	 * @param productId
	 * @param idListShop
	 * @param customerId
	 * @param customerTypeId
	 * @return
	 * @return: PromotionProgrameDTO
	 * @throws:
	 * @since: May 20, 2013
	 */
	public PromotionProgrameDTO getPromotionObjectWithCheckInvalid(
			String productId, String idListShop, String customerId,
			String customerTypeId) {
		PromotionProgrameDTO promotionObject = new PromotionProgrameDTO();
		StringBuffer sql = new StringBuffer();
		ArrayList<String> params = new ArrayList<String>();
		sql.append("SELECT DISTINCT pp.promotion_program_code      PROMOTION_PROGRAM_CODE, ");
		sql.append("                pp.promotion_program_id        PROMOTION_PROGRAM_ID, ");
		sql.append("                pp.promotion_program_name      PROMOTION_PROGRAM_NAME, ");
		sql.append("                pp.status                      STATUS, ");
		sql.append("                pp.type                        TYPE, ");
		sql.append("                pp.pro_format                  PRO_FORMAT, ");
		sql.append("                pp.from_date                   FROM_DATE, ");
		sql.append("                pp.to_date                     TO_DATE, ");
		sql.append("                pp.relation                    RELATION, ");
		sql.append("                pp.multiple                    MULTIPLE, ");
		sql.append("                pp.recursive                   RECURSIVE, ");
		sql.append("                Ifnull(TTTT1.num_condition, 0) COKHAIBAO1, ");
		sql.append("                Ifnull(TTTT2.num_condition, 0) COKHAIBAO2, ");
		sql.append("                Ifnull(TTTT3.num_condition, 0) COKHAIBAO3, ");
		sql.append("                Ifnull(TTTT4.num_condition, 0) COKHAIBAO4, ");
		sql.append("                Ifnull(TTTT5.num_condition, 0) COKHAIBAO5, ");
		sql.append("                Ifnull(TTTT6.num_condition, 0) COKHAIBAO6, ");
		sql.append("                Ifnull(TTTT7.num_condition, 0) COKHAIBAO7, ");
		sql.append("                Ifnull(TTTT8.num_condition, 0) COKHAIBAO8 ");
		sql.append("FROM   promotion_program pp ");
		
		//Thuoc tinh dong loai khach hang
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id, ");
		sql.append("                         Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca ");
		sql.append("                  WHERE  pca.object_type = 2 ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT1 ");
		sql.append("              ON TTTT1.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id, ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         promotion_cust_attr_detail pcad ");
		sql.append("                  WHERE  1 = 1 ");
		sql.append("                         AND pca.promotion_cust_attr_id = ");
		sql.append("                             pcad.promotion_cust_attr_id ");
		sql.append("                         AND pca.object_type = 2 ");
		sql.append("                         AND pcad.object_id = ? ");
		params.add(customerTypeId);
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND pcad.status = 1 ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT2 ");
		sql.append("              ON TTTT2.promotion_program_id = pp.promotion_program_id ");
		
		//Thuoc tinh dong muc doanh so
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id       promotion_program_id, ");
		sql.append("                         Count (promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca ");
		sql.append("                  WHERE  pca.object_type = 3 ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                  GROUP  BY promotion_program_id) TTTT3 ");
		sql.append("              ON TTTT3.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id, ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         promotion_cust_attr_detail pcad, ");
		sql.append("                         sale_level_cat slc, ");
		sql.append("                         customer_cat_level ccl ");
		sql.append("                  WHERE  1 = 1 ");
		sql.append("                         AND pca.promotion_cust_attr_id = ");
		sql.append("                             pcad.promotion_cust_attr_id ");
		sql.append("                         AND pcad.object_id = slc.sale_level_cat_id ");
		sql.append("                         AND slc.sale_level_cat_id = ccl.sale_level_cat_id ");
		sql.append("                         AND pca.object_type = 3 ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND pcad.status = 1 ");
		sql.append("                         AND slc.status = 1 ");
		sql.append("                         AND ccl.customer_id = ? ");
		params.add(customerId);
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT4 ");
		sql.append("              ON TTTT4.promotion_program_id = pp.promotion_program_id ");
		
		//Thuoc tinh dong cac gia tri don le: 1: chuoi, 2: so, 3: ngay
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id       promotion_program_id, ");
		sql.append("                         Count (promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         attribute AT ");
		sql.append("                  WHERE  pca.object_type = 1 ");
		sql.append("                         AND pca.object_id = AT.attribute_id ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND AT.status = 1 ");
		sql.append("                         AND AT.value_type IN ( 1, 2, 3 ) ");
		sql.append("                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT5 ");
		
		sql.append("              ON TTTT5.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id, ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         attribute AT, ");
		sql.append("                         attribute_value atv ");
		sql.append("                  WHERE  pca.object_type = 1 ");
		sql.append("                         AND pca.object_id = AT.attribute_id ");
		sql.append("                         AND AT.attribute_id = atv.attribute_id ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND AT.status = 1 ");
		sql.append("                         AND atv.status = 1 ");
		sql.append("                         AND AT.value_type IN ( 1, 2, 3 ) ");
		sql.append("                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                         AND atv.table_name = 'CUSTOMER' ");
		sql.append("                         AND atv.table_id = ? ");
		params.add(customerId);
		sql.append("                         AND ( CASE ");
		sql.append("                                 WHEN AT.value_type = 2 THEN Ifnull( ");
		sql.append("                                 cast(atv.value as integer) >= pca.from_value, 1) AND");
		sql.append("                                 Ifnull(cast(atv.value as integer) <= pca.to_value, 1) ");
		sql.append("                                 WHEN AT.value_type = 1 THEN ");
		sql.append("                                 trim(lower(atv.value)) = trim(lower(pca.from_value)) ");
		sql.append("                                 WHEN AT.value_type = 3 THEN atv.value is not null and Ifnull( ");
		sql.append("                                 Date(atv.value) >= Date(pca.from_value), 1) AND");
		sql.append("                                 Ifnull(Date(atv.value) <= Date(pca.to_value), 1) ");
		sql.append("                               END ) ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT6 ");
		sql.append("              ON TTTT6.promotion_program_id = pp.promotion_program_id ");
		
		//Thuoc tinh dong dang select
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id, ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         attribute AT ");
		sql.append("                  WHERE  pca.object_type = 1 ");
		sql.append("                         AND pca.object_id = AT.attribute_id ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND AT.status = 1 ");
		sql.append("                         AND AT.value_type = 4 ");
		sql.append("                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT7 ");
		sql.append("              ON TTTT7.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id, ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         attribute AT, ");
		sql.append("                         attribute_value atv, ");
		sql.append("                         attribute_detail atd, ");
		sql.append("                         attribute_value_detail atvd, ");
		sql.append("                         promotion_cust_attr_detail pcad ");
		sql.append("                  WHERE  pca.object_type = 1 ");
		sql.append("                         AND pca.object_id = AT.attribute_id ");
		sql.append("                         AND AT.attribute_id = atv.attribute_id ");
		sql.append("                         AND AT.attribute_id = atd.attribute_id ");
		sql.append("                         AND atv.attribute_value_id = atvd.attribute_value_id ");
		sql.append("                         AND atd.attribute_detail_id = atvd.attribute_detail_id ");
		sql.append("                         AND atd.attribute_detail_id = pcad.object_id ");
		sql.append("                         AND pca.promotion_cust_attr_id = ");
		sql.append("                             pcad.promotion_cust_attr_id ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND AT.status = 1 ");
		sql.append("                         AND atv.status = 1 ");
		sql.append("                         AND atd.status = 1 ");
		sql.append("                         AND atvd.status = 1 ");
		sql.append("                         AND pcad.status = 1 ");
		sql.append("                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                         AND AT.value_type = 4 ");
		sql.append("                         AND atv.table_name = 'CUSTOMER' ");
		sql.append("                         AND atv.table_id = ? ");
		params.add(customerId);
		sql.append("                  GROUP  BY pca.promotion_program_id)TTTT8 ");
		sql.append("              ON TTTT8.promotion_program_id = pp.promotion_program_id ");

		//Thuoc tinh dong dang multi select
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id, ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         attribute AT ");
		sql.append("                  WHERE  pca.object_type = 1 ");
		sql.append("                         AND pca.object_id = AT.attribute_id ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND AT.status = 1 ");
		sql.append("                         AND AT.value_type = 5 ");
		sql.append("                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT9 ");
		sql.append("              ON TTTT9.promotion_program_id = pp.promotion_program_id ");
		sql.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id, ");
		sql.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sql.append("                  FROM   promotion_cust_attr pca, ");
		sql.append("                         attribute AT, ");
		sql.append("                         attribute_value atv, ");
		sql.append("                         attribute_detail atd, ");
		sql.append("                         attribute_value_detail atvd, ");
		sql.append("                         promotion_cust_attr_detail pcad ");
		sql.append("                  WHERE  pca.object_type = 1 ");
		sql.append("                         AND pca.object_id = AT.attribute_id ");
		sql.append("                         AND AT.attribute_id = atv.attribute_id ");
		sql.append("                         AND AT.attribute_id = atd.attribute_id ");
		sql.append("                         AND atv.attribute_value_id = atvd.attribute_value_id ");
		sql.append("                         AND atd.attribute_detail_id = atvd.attribute_detail_id ");
		sql.append("                         AND atd.attribute_detail_id = pcad.object_id ");
		sql.append("                         AND pca.promotion_cust_attr_id = ");
		sql.append("                             pcad.promotion_cust_attr_id ");
		sql.append("                         AND pca.status = 1 ");
		sql.append("                         AND AT.status = 1 ");
		sql.append("                         AND atv.status = 1 ");
		sql.append("                         AND atd.status = 1 ");
		sql.append("                         AND atvd.status = 1 ");
		sql.append("                         AND pcad.status = 1 ");
		sql.append("                         AND AT.table_name = 'CUSTOMER' ");
		sql.append("                         AND AT.value_type = 5 ");
		sql.append("                         AND atv.table_name = 'CUSTOMER' ");
		sql.append("                         AND atv.table_id = ? ");
		params.add(customerId);
		sql.append("                  GROUP  BY pca.promotion_program_id) TTTT10 ");
		sql.append("              ON TTTT10.promotion_program_id = pp.promotion_program_id, ");

		sql.append("       promotion_program_detail ppd, ");
		sql.append("       promotion_shop_map psm ");
		sql.append("       LEFT JOIN promotion_customer_map pcm ");
		sql.append("              ON pcm.promotion_shop_map_id = psm.promotion_shop_map_id ");
		sql.append("WHERE  1 = 1 ");
		sql.append("       AND psm.promotion_program_id = pp.promotion_program_id ");
		sql.append("       AND pp.promotion_program_id = ppd.promotion_program_id ");
		sql.append("       AND psm.shop_id IN ( " + idListShop);
		sql.append("       ) ");
		sql.append("       AND pp.status = 1 ");
		sql.append("       AND psm.status = 1 ");
		sql.append("       AND Date(pp.from_date) <= Date('now', 'localtime') ");
		sql.append("       AND Ifnull (Date(pp.to_date) >= Date('now', 'localtime'), 1) ");
		sql.append("       AND ( CASE ");
		sql.append("               WHEN (SELECT Count(*) ");
		sql.append("                     FROM   promotion_customer_map pcmm ");
		sql.append("                     WHERE  pcmm.promotion_shop_map_id = ");
		sql.append("                            psm.promotion_shop_map_id) = 0 ");
		sql.append("                 THEN ");
		sql.append("               1 ");
		sql.append("               ELSE pcm.customer_id = ? ");
		params.add(customerId);
		sql.append("                    AND pcm.status = 1 ");
		sql.append("             END ) ");
		sql.append("       AND ppd.product_id = ? ");
		params.add(productId);
		
		Cursor c = null;
		try {
			c = rawQuery(sql.toString(), params.toArray(new String[params.size()]));
			if (c != null) {
				if (c.moveToFirst()) {
					promotionObject = initDTOHasCheckValidCTKM(c);
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
				// TODO: handle exception
			}
		}
		return promotionObject;
	}

	/**
	 * 
	 * kiem tra CTKM co hop le voi Khach hang nay hay khong
	 * 
	 * @author: HaiTC3
	 * @param listShopId
	 * @param customerId
	 * @param customerTypeId
	 * @return
	 * @return: boolean
	 * @throws:
	 * @since: May 24, 2013
	 */
	public boolean checkPromotionInValid(String promotionProgramId,
			String listShopId, String customerId, String customerTypeId) {
		boolean result = false;
		StringBuffer sqlString = new StringBuffer();
		sqlString
				.append("SELECT DISTINCT pp.promotion_program_code      PROMOTION_PROGRAM_CODE, ");
		sqlString
				.append("                pp.promotion_program_id        PROMOTION_PROGRAM_ID, ");
		sqlString
				.append("                pp.promotion_program_name      PROMOTION_PROGRAM_NAME, ");
		sqlString
				.append("                Ifnull(TTTT1.num_condition, 0) COKHAIBAO1, ");
		sqlString
				.append("                Ifnull(TTTT2.num_condition, 0) COKHAIBAO2, ");
		sqlString
				.append("                Ifnull(TTTT3.num_condition, 0) COKHAIBAO3, ");
		sqlString
				.append("                Ifnull(TTTT4.num_condition, 0) COKHAIBAO4, ");
		sqlString
				.append("                Ifnull(TTTT5.num_condition, 0) COKHAIBAO5, ");
		sqlString
				.append("                Ifnull(TTTT6.num_condition, 0) COKHAIBAO6, ");
		sqlString
				.append("                Ifnull(TTTT7.num_condition, 0) COKHAIBAO7, ");
		sqlString
				.append("                Ifnull(TTTT8.num_condition, 0) COKHAIBAO8 ");
		sqlString.append("FROM   promotion_program pp ");
		sqlString.append("       LEFT JOIN (SELECT pca.promotion_program_id, ");
		sqlString
				.append("                         Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sqlString.append("                  FROM   promotion_cust_attr pca ");
		sqlString.append("                  WHERE  pca.object_type = 2 ");
		sqlString.append("                         AND pca.status = 1 ");
		sqlString
				.append("                  GROUP  BY pca.promotion_program_id) TTTT1 ");
		sqlString
				.append("              ON TTTT1.promotion_program_id = pp.promotion_program_id ");
		sqlString
				.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id ");
		sqlString.append("                         , ");
		sqlString
				.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sqlString.append("                  FROM   promotion_cust_attr pca, ");
		sqlString
				.append("                         promotion_cust_attr_detail pcad ");
		sqlString.append("                  WHERE  1 = 1 ");
		sqlString
				.append("                         AND pca.promotion_cust_attr_id = ");
		sqlString
				.append("                             pcad.promotion_cust_attr_id ");
		sqlString.append("                         AND pca.object_type = 2 ");
		sqlString.append("                         AND pcad.object_id = ? ");
		sqlString.append("                         AND pca.status = 1 ");
		sqlString.append("                         AND pcad.status = 1 ");
		sqlString
				.append("                  GROUP  BY pca.promotion_program_id) TTTT2 ");
		sqlString
				.append("              ON TTTT2.promotion_program_id = pp.promotion_program_id ");
		sqlString
				.append("       LEFT JOIN (SELECT pca.promotion_program_id       promotion_program_id, ");
		sqlString
				.append("                         Count (promotion_cust_attr_id) NUM_CONDITION ");
		sqlString.append("                  FROM   promotion_cust_attr pca ");
		sqlString.append("                  WHERE  pca.object_type = 3 ");
		sqlString.append("                         AND pca.status = 1 ");
		sqlString
				.append("                  GROUP  BY promotion_program_id) TTTT3 ");
		sqlString
				.append("              ON TTTT3.promotion_program_id = pp.promotion_program_id ");
		sqlString
				.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id ");
		sqlString.append("                         , ");
		sqlString
				.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sqlString.append("                  FROM   promotion_cust_attr pca, ");
		sqlString
				.append("                         promotion_cust_attr_detail pcad, ");
		sqlString.append("                         sale_level_cat slc, ");
		sqlString.append("                         customer_cat_level ccl ");
		sqlString.append("                  WHERE  1 = 1 ");
		sqlString
				.append("                         AND pca.promotion_cust_attr_id = ");
		sqlString
				.append("                             pcad.promotion_cust_attr_id ");
		sqlString
				.append("                         AND pcad.object_id = slc.sale_level_cat_id ");
		sqlString
				.append("                         AND slc.sale_level_cat_id = ccl.sale_level_cat_id ");
		sqlString.append("                         AND pca.object_type = 3 ");
		sqlString.append("                         AND pca.status = 1 ");
		sqlString.append("                         AND pcad.status = 1 ");
		sqlString.append("                         AND slc.status = 1 ");
		sqlString.append("                         AND ccl.customer_id = ? ");
		sqlString
				.append("                  GROUP  BY pca.promotion_program_id) TTTT4 ");
		sqlString
				.append("              ON TTTT4.promotion_program_id = pp.promotion_program_id ");
		sqlString
				.append("       LEFT JOIN (SELECT pca.promotion_program_id       promotion_program_id, ");
		sqlString
				.append("                         Count (promotion_cust_attr_id) NUM_CONDITION ");
		sqlString.append("                  FROM   promotion_cust_attr pca, ");
		sqlString.append("                         attribute AT ");
		sqlString.append("                  WHERE  pca.object_type = 1 ");
		sqlString
				.append("                         AND pca.object_id = AT.attribute_id ");
		sqlString.append("                         AND pca.status = 1 ");
		sqlString.append("                         AND AT.status = 1 ");
		sqlString
				.append("                         AND AT.value_type IN ( 1, 2, 3 ) ");
		sqlString
				.append("                         AND AT.table_name = 'CUSTOMER' ");
		sqlString
				.append("                  GROUP  BY pca.promotion_program_id) TTTT5 ");
		sqlString
				.append("              ON TTTT5.promotion_program_id = pp.promotion_program_id ");
		sqlString
				.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id ");
		sqlString.append("                         , ");
		sqlString
				.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sqlString.append("                  FROM   promotion_cust_attr pca, ");
		sqlString.append("                         attribute AT, ");
		sqlString.append("                         attribute_value atv ");
		sqlString.append("                  WHERE  pca.object_type = 1 ");
		sqlString
				.append("                         AND pca.object_id = AT.attribute_id ");
		sqlString
				.append("                         AND AT.attribute_id = atv.attribute_id ");
		sqlString.append("                         AND pca.status = 1 ");
		sqlString.append("                         AND AT.status = 1 ");
		sqlString.append("                         AND atv.status = 1 ");
		sqlString
				.append("                         AND AT.value_type IN ( 1, 2, 3 ) ");
		sqlString
				.append("                         AND AT.table_name = 'CUSTOMER' ");
		sqlString
				.append("                         AND atv.table_name = 'CUSTOMER' ");
		sqlString.append("                         AND atv.table_id = ? ");
		sqlString.append("                         AND ( CASE ");
		sqlString
				.append("                                 WHEN AT.value_type = 2 THEN Ifnull( ");
		sqlString
				.append("                                 cast(atv.value as integer) >= pca.from_value, ");
		sqlString
				.append("                                                             1) ");
		sqlString
				.append("                                                             AND ");
		sqlString
				.append("                                 Ifnull(cast(atv.value as integer) <= pca.to_value, 1) ");
		sqlString
				.append("                                 WHEN AT.value_type = 1 THEN ");
		sqlString
				.append("                                 trim(lower(atv.value)) = trim(lower(pca.from_value)) ");
		sqlString
				.append("                                 WHEN AT.value_type = 3 THEN atv.value is not null and Ifnull( ");
		sqlString
				.append("                                 Date(atv.value) >= Date(pca.from_value), ");
		sqlString
				.append("                                                             1) ");
		sqlString
				.append("                                                             AND ");
		sqlString
				.append("                                 Ifnull(Date(atv.value) <= Date(pca.to_value), ");
		sqlString.append("                                 1) ");
		sqlString.append("                               END ) ");
		sqlString
				.append("                  GROUP  BY pca.promotion_program_id) TTTT6 ");
		sqlString
				.append("              ON TTTT6.promotion_program_id = pp.promotion_program_id ");
		sqlString
				.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id ");
		sqlString.append("                         , ");
		sqlString
				.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sqlString.append("                  FROM   promotion_cust_attr pca, ");
		sqlString.append("                         attribute AT ");
		sqlString.append("                  WHERE  pca.object_type = 1 ");
		sqlString
				.append("                         AND pca.object_id = AT.attribute_id ");
		sqlString.append("                         AND pca.status = 1 ");
		sqlString.append("                         AND AT.status = 1 ");
		sqlString
				.append("                         AND AT.value_type IN ( 4, 5 ) ");
		sqlString
				.append("                         AND AT.table_name = 'CUSTOMER' ");
		sqlString
				.append("                  GROUP  BY pca.promotion_program_id) TTTT7 ");
		sqlString
				.append("              ON TTTT7.promotion_program_id = pp.promotion_program_id ");
		sqlString
				.append("       LEFT JOIN (SELECT pca.promotion_program_id           promotion_program_id ");
		sqlString.append("                         , ");
		sqlString
				.append("       Count (pca.promotion_cust_attr_id) NUM_CONDITION ");
		sqlString.append("                  FROM   promotion_cust_attr pca, ");
		sqlString.append("                         attribute AT, ");
		sqlString.append("                         attribute_value atv, ");
		sqlString.append("                         attribute_detail atd, ");
		sqlString
				.append("                         attribute_value_detail atvd, ");
		sqlString
				.append("                         promotion_cust_attr_detail pcad ");
		sqlString.append("                  WHERE  pca.object_type = 1 ");
		sqlString
				.append("                         AND pca.object_id = AT.attribute_id ");
		sqlString
				.append("                         AND AT.attribute_id = atv.attribute_id ");
		sqlString
				.append("                         AND AT.attribute_id = atd.attribute_id ");
		sqlString
				.append("                         AND atv.attribute_value_id = atvd.attribute_value_id ");
		sqlString
				.append("                         AND atd.attribute_detail_id = atvd.attribute_detail_id ");
		sqlString
				.append("                         AND atd.attribute_detail_id = pcad.object_id ");
		sqlString
				.append("                         AND pca.promotion_cust_attr_id = ");
		sqlString
				.append("                             pcad.promotion_cust_attr_id ");
		sqlString.append("                         AND pca.status = 1 ");
		sqlString.append("                         AND AT.status = 1 ");
		sqlString.append("                         AND atv.status = 1 ");
		sqlString.append("                         AND atd.status = 1 ");
		sqlString.append("                         AND atvd.status = 1 ");
		sqlString.append("                         AND pcad.status = 1 ");
		sqlString
				.append("                         AND AT.table_name = 'CUSTOMER' ");
		sqlString
				.append("                         AND AT.value_type IN ( 4, 5 ) ");
		sqlString
				.append("                         AND atv.table_name = 'CUSTOMER' ");
		sqlString.append("                         AND atv.table_id = ? ");
		sqlString
				.append("                  GROUP  BY pca.promotion_program_id)TTTT8 ");
		sqlString
				.append("              ON TTTT8.promotion_program_id = pp.promotion_program_id, ");
		sqlString.append("       promotion_program_detail ppd, ");
		sqlString.append("       promotion_shop_map psm ");
		sqlString.append("       LEFT JOIN promotion_customer_map pcm ");
		sqlString
				.append("              ON pcm.promotion_shop_map_id = psm.promotion_shop_map_id ");
		sqlString.append("WHERE  1 = 1 ");
		sqlString
				.append("       AND psm.promotion_program_id = pp.promotion_program_id ");
		sqlString
				.append("       AND pp.promotion_program_id = ppd.promotion_program_id ");
		sqlString.append("       AND psm.shop_id IN ( " + listShopId);
		sqlString.append("       ) ");
		sqlString.append("       AND pp.status = 1 ");
		sqlString.append("       AND psm.status = 1 ");
		sqlString
				.append("       AND Date(pp.from_date) <= Date('now', 'localtime') ");
		sqlString
				.append("       AND Ifnull (Date(pp.to_date) >= Date('now', 'localtime'), 1) ");
		sqlString.append("       AND ( CASE ");
		sqlString.append("               WHEN (SELECT Count(*) ");
		sqlString
				.append("                     FROM   promotion_customer_map pcmm ");
		sqlString
				.append("                     WHERE  pcmm.promotion_shop_map_id = ");
		sqlString
				.append("                            psm.promotion_shop_map_id) = 0 ");
		sqlString.append("                 THEN ");
		sqlString.append("               1 ");
		sqlString.append("               ELSE pcm.customer_id = ? ");
		sqlString.append("                    AND pcm.status = 1 ");
		sqlString.append("             END ) ");
		sqlString.append("       AND pp.promotion_program_id = ? ");

		String[] paramApplyNPP = new String[] { customerTypeId, customerId,
				customerId, customerId, customerId, promotionProgramId };
		Cursor cApplyNPP = null;
		try {
			cApplyNPP = rawQuery(sqlString.toString(), paramApplyNPP);
			if (cApplyNPP != null) {
				if (cApplyNPP.moveToFirst()) {
					result = checkPromotionProgramInvalid(cApplyNPP);
				}
			}
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try {
				if (cApplyNPP != null) {
					cApplyNPP.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return result;
	}

	/**
	 * Lay ds CTKM dang chay
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return
	 * @return: int
	 * @throws:
	 */

	public int getNumPromotionProgrameRunning(String shopId) {
		StringBuilder sqlQuery = new StringBuilder();
		// sqlQuery.append("SELECT DISTINCT PP.PROMOTION_PROGRAM_CODE, PP.PROMOTION_PROGRAM_NAME, StrfTime('%d/%m/%Y', PP.FROM_DATE) as FROM_DATE, StrfTime('%d/%m/%Y', PP.TO_DATE) as TO_DATE, TRIM(PP.DESCRIPTION) AS DESCRIPTION  FROM PROMOTION_PROGRAM PP ");
		// sqlQuery.append(" WHERE (date(PP.TO_DATE) >= (SELECT date('now','localtime')) OR PP.TO_DATE is null)");
		// sqlQuery.append(" AND (PP.STATUS=1) AND (PP.SHOP_ID IN (SELECT SHOP_ID FROM SHOP SH WHERE SH.PARENT_SHOP_ID = ?))");
		// sqlQuery.append(" GROUP BY PP.PROMOTION_PROGRAM_CODE, PP.PROMOTION_PROGRAM_NAME, FROM_DATE, TO_DATE");

		sqlQuery.append("SELECT DISTINCT PP.promotion_program_code, ");
		sqlQuery.append("                PP.promotion_program_name, ");
		sqlQuery.append("                Strftime('%d/%m/%Y', PP.from_date) AS FROM_DATE, ");
		sqlQuery.append("                Strftime('%d/%m/%Y', PP.to_date)   AS TO_DATE, ");
		sqlQuery.append("                Trim(PP.description)               AS DESCRIPTION ");
		sqlQuery.append("FROM   promotion_program PP, ");
		sqlQuery.append("       promotion_shop_map PSM ");
		sqlQuery.append("WHERE  Ifnull (Date(PP.to_date) >= Date('now', 'localtime'), 1) ");
		sqlQuery.append("       AND ( PP.status = 1 ) ");
		sqlQuery.append("       AND PSM.promotion_program_id = PP.promotion_program_id ");
		sqlQuery.append("       AND PSM.status = 1 ");
		sqlQuery.append("       AND ( PSM.shop_id IN (SELECT shop_id ");
		sqlQuery.append("                             FROM   shop SH ");
		sqlQuery.append("                             WHERE  SH.parent_shop_id = ?) ) ");
		sqlQuery.append("GROUP  BY PP.promotion_program_code, ");
		sqlQuery.append("          PP.promotion_program_name, ");
		sqlQuery.append("          from_date, ");
		sqlQuery.append("          to_date ");

		String queryGetListProductForOrder = sqlQuery.toString();
		String getCountProductList = " SELECT COUNT(*) AS TOTAL_ROW FROM ("
				+ queryGetListProductForOrder + ") ";
		ArrayList<String> params = new ArrayList<String>();
		params.add(shopId);

		Cursor cTmp = null;
		int total = 0;
		try {
			cTmp = rawQuery(getCountProductList,
					params.toArray(new String[params.size()]));
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

	/**
	 * Lay ds CTKM cho don hang
	 * 
	 * @author: TruongHN
	 * @param shopId
	 * @return: ArrayList<PromotionProgrameDTO>
	 * @throws:
	 */
	public ArrayList<PromotionProgrameDTO> getListPromotionForOrder(
			String shopIdList, String customerId) {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer
				.append("SELECT DISTINCT ( pp.promotion_program_id ) AS PROMOTION_PROGRAM_ID, ");
		strBuffer
				.append("                pp.promotion_program_code   AS PROMOTION_PROGRAM_CODE, ");
		strBuffer
				.append("                pp.promotion_program_name   AS PROMOTION_PROGRAM_NAME, ");
		strBuffer
				.append("                pp.status                   AS STATUS, ");
		strBuffer
				.append("                pp.type                     AS TYPE, ");
		strBuffer
				.append("                pp.relation                 AS RELATION, ");
		strBuffer
				.append("                pp.multiple                 AS MULTIPLE, ");
		strBuffer
				.append("                pp.description              AS DESCRIPTION, ");
		strBuffer
				.append("                pp.recursive                AS RECURSIVE ");
		strBuffer.append("FROM   promotion_program pp,  ");
		strBuffer.append("       promotion_shop_map psm ");
		strBuffer
				.append("       left join promotion_customer_map pcm on pcm.promotion_shop_map_id = psm.promotion_shop_map_id ");// Kiem
																																	// tra
																																	// xem
																																	// co
																																	// ap
																																	// dung
																																	// toi
																																	// muc
																																	// KH
																																	// ko?
		strBuffer.append("WHERE  1 = 1 ");
		strBuffer.append("       AND psm.shop_id in ( ");
		strBuffer.append(shopIdList);
		strBuffer.append(") ");
		strBuffer
				.append("       AND psm.promotion_program_id = pp.promotion_program_id ");
		strBuffer.append("       AND pp.status = 1 ");
		strBuffer.append("       AND psm.status = 1 ");
		strBuffer
				.append("       AND Date(pp.from_date) <= Date('now', 'localtime') ");
		strBuffer
				.append("       AND Ifnull (Date(pp.to_date) >= Date('now', 'localtime'), 1) ");
		strBuffer
				.append("       AND (case when (select count(1) from promotion_customer_map pcmm where pcmm.promotion_shop_map_id = psm.promotion_shop_map_id) = 0 then 1 ");
		strBuffer
				.append("        else pcm.customer_id = ? and pcm.status = 1 end) ");
		strBuffer.append("       AND pp.type IN ( 'ZV19', 'ZV20', 'ZV21' ) ");

		// String[] params = new String[]{shopId};
		// String[] params = new String[]{};
		String[] params = new String[] { customerId };

		ArrayList<PromotionProgrameDTO> listPromotion = new ArrayList<PromotionProgrameDTO>();
		Cursor c = null;
		try {
			c = rawQuery(strBuffer.toString(), params);
			if (c != null) {
				PromotionProgrameDTO dto;
				if (c.moveToFirst()) {
					do {
						dto = initPromotionPrograme(c);
						listPromotion.add(dto);
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
				VTLog.i("getListPromotionForOrder", e.getMessage());
			}
		}

		return listPromotion;
	}

	/**
	 * 
	 * Khoi tao doi tuong chuong trinh KM
	 * 
	 * @author: TruongHN
	 * @param c
	 * @return: PromotionProgrameDTO
	 * @throws:
	 */
	private PromotionProgrameDTO initPromotionPrograme(Cursor c) {
		PromotionProgrameDTO dto = new PromotionProgrameDTO();
		dto.PROMOTION_PROGRAM_CODE = c.getString(c
				.getColumnIndex(PROMOTION_PROGRAM_CODE));
		dto.PROMOTION_PROGRAM_ID = c.getInt(c
				.getColumnIndex(PROMOTION_PROGRAM_ID));
		dto.PROMOTION_PROGRAM_NAME = c.getString(c
				.getColumnIndex(PROMOTION_PROGRAM_NAME));
		dto.STATUS = c.getInt(c.getColumnIndex(STATUS));
		dto.TYPE = c.getString(c.getColumnIndex(TYPE));
		dto.RELATION = c.getInt(c.getColumnIndex(RELATION));
		dto.recursive = c.getInt(c.getColumnIndex(RECURSIVE));
		dto.MULTIPLE = c.getInt(c.getColumnIndex(MULTIPLE));
		dto.DESCRIPTION = c.getString(c.getColumnIndex(DESCRIPTION));
		return dto;
	}

	/**
	 * Lay danh sach khach hang xong 1 chu ki tinh thuong
	 * Lay ve de tien hanh nhap san luong ban cho chu ki do.
	 * @author: quangvt1
	 * @since: 15:12:58 13-06-2014
	 * @return: CustomerListDoneProgrameDTO
	 * @throws:  HoanPD1
	 * @param data
	 * @return
	 */
	public CustomerListDoneProgrameDTO getListCustomerDonePrograme(Bundle data) { 
		CustomerListDoneProgrameDTO dto = new CustomerListDoneProgrameDTO();
		long pro_info_id 	= data.getLong(IntentConstants.INTENT_PROGRAM_ID);
		long pro_period_id 	= data.getLong(IntentConstants.INTENT_PERIOD_ID);
		boolean isGetTotal 	= data.getBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE);
		String searchData 	= data.getString(IntentConstants.INTENT_NAME);
		int page = data.getInt(IntentConstants.INTENT_PAGE);
		int typePro = data.getInt(IntentConstants.INTENT_TYPE);
		String level 		= data.getString(IntentConstants.INTENT_LEVEL);
		String date_now 	= DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		String staffId 		= GlobalInfo.getInstance().getProfile().getUserData().id + "";
		String shopId 		= GlobalInfo.getInstance().getProfile().getUserData().shopId;
		
		AP_PARAM_TABLE table = new AP_PARAM_TABLE(mDB);
		ArrayList<ApParamDTO> listParams = table.getParamsForInputQuantityPrograme();
		if(listParams.size() < 2){
			return null;
		} 
		StringBuilder totalPageSql = new StringBuilder();
		Cursor c = null;
		ArrayList<String> params = new ArrayList<String>();
        StringBuffer  var1 = new StringBuffer();
        var1.append("SELECT * FROM ( ");
        var1.append(" SELECT PCP.pro_cus_process_id AS PRO_CUS_PROCESS_ID, ");
        var1.append("       PCP.pro_info_id        AS PRO_INFO_ID, ");
        var1.append("       PCP.pro_cus_map_id     AS PRO_CUS_MAP_ID, ");
        var1.append("       PCP.pro_period_id      AS PRO_PERIOD_ID, ");
        var1.append("       PCP.object_id          AS OBJECT_ID, ");
        var1.append("       ct.short_code          AS CUSTOMER_CODE, ");
        var1.append("       ct.customer_name       AS CUSTOMER_NAME, ");
        var1.append("       ct.housenumber         AS HOUSENUMBER, ");
        var1.append("       ct.street              AS STREET, ");
        var1.append("       ct.area_name           AS AREA_NAME, ");
        var1.append("       PP.from_date           AS PERIOD_FROM_DATE, ");
        var1.append("       PP.to_date             AS PERIOD_TO_DATE, ");
        var1.append("       PCM.level_number       AS JOIN_LEVEL, ");
        var1.append("       PCP.quantity_shop      AS QUANTITY_SHOP,  ");   
        var1.append("       PCP.quantity           AS QUANTITY, ");
        var1.append("       PCP.quantity_pg        AS QUANTITY_PG,  ");
        var1.append("       PCP.update_user        AS UPDATE_USER,   ");
        if(typePro==3 || typePro==4 ){
        	var1.append("   lv.level_name          AS LEVEL_NAME, ");
        }
        var1.append("       CASE ");
        var1.append("         WHEN ( ( Date(?) >= Date(PP.to_date, ?) ) ");
        params.add(date_now);
        params.add(listParams.get(0).value);
        var1.append("                AND ( Date(?) <= Date(PP.to_date, ?) ) ) THEN 1 ELSE 0 ");
        params.add(date_now);
        params.add(listParams.get(1).value);
        var1.append("       end                    ALLOW_EDIT ");
        var1.append(" FROM   pro_cus_process PCP, ");
        var1.append("       pro_cus_history PCH, ");
        var1.append("       pro_period PP, ");
        var1.append("       pro_cus_map PCM, ");
		if (typePro==3 || typePro==4 ) {
			var1.append("       (SELECT DISTINCT( psd.[level_name] ) LEVEL_NAME, ");
			var1.append("               psd.level_number     LEVEL_NUMBER ");
			var1.append("        FROM   pro_structure ps, ");
			var1.append("               pro_structure_detail psd ");
			var1.append("        WHERE  ps.[pro_info_id] = ? ");
			params.add(pro_info_id + "");
			var1.append("               AND ps.[pro_structure_id] = psd.[pro_structure_id] ");
			var1.append("        ORDER  BY psd.[level_number] ) LV ON PCM.[level_number] = LV.level_number,  ");
		}
        var1.append("        (SELECT c.customer_id, ");
        var1.append("                c.customer_code, ");
        var1.append("                c.short_code, ");
        var1.append("                c.customer_name, ");
        var1.append("                A.area_name, ");
        var1.append("                C.housenumber, ");
        var1.append("                C.street, ");
        var1.append("                C.address, ");
        var1.append("                C.name_text, ");
        var1.append("                C.phone, ");
        var1.append("                C.mobiphone ");
        var1.append("         FROM   visit_plan vp, ");
        var1.append("                customer c, ");
        var1.append("                routing rt, ");
        var1.append("                shop sh, ");
        var1.append("                area A, ");
        var1.append("                routing_customer rc ");
        var1.append("         WHERE  1 = 1 ");
        var1.append("                AND vp.status = 1 ");
        var1.append("                AND c.status = 1 ");
        var1.append("                AND rt.status = 1 ");
        var1.append("                AND vp.staff_id = ? ");
        params.add(""+staffId);
        var1.append("                AND sh.shop_id = ? ");
        params.add(shopId);
        var1.append("                AND RT.type = 0 ");
        var1.append("                AND (substr(rc.end_date,1,10) >= ? ");
        params.add(date_now);
        var1.append("                       OR substr(rc.end_date,1,10) IS NULL) ");
        var1.append("                 AND substr(rc.start_date,1,10) <= ? ");
        params.add(date_now);
        var1.append("                AND sh.shop_id = c.shop_id ");
        var1.append("                AND sh.shop_id = rt.shop_id ");
        var1.append("                AND sh.shop_id = vp.shop_id ");
        var1.append("                AND vp.routing_id = rt.routing_id ");
        var1.append("                AND A.area_id = C.area_id ");
        
        var1.append("                AND rt.routing_id = rc.routing_id ");
        var1.append("                AND rc.customer_id = c.customer_id )CT ");
        var1.append(" WHERE  1 = 1 ");
        var1.append("       AND pcp.pro_info_id = ? ");
        params.add(pro_info_id + "");
        var1.append("       AND pcp.pro_period_id = ? ");
        params.add(pro_period_id + "");
        var1.append("       AND pcp.object_type = 1 ");
        var1.append("       AND pch.type = 1 ");
        var1.append("       AND pcp.pro_cus_map_id = pcm.pro_cus_map_id ");
        var1.append("       AND pcp.pro_cus_map_id = pch.pro_cus_map_id ");
        var1.append("       AND pch.pro_cus_map_id = pcm.pro_cus_map_id ");
        var1.append("       AND pcp.pro_period_id = pp.pro_period_id ");
        var1.append("       AND pcp.object_id = pcm.object_id ");
        var1.append("       AND pcp.object_id = ct.customer_id ");
        var1.append("       AND pcm.object_id = ct.customer_id ");
        if(!"0".equals(level)){
        	var1.append("   AND pcm.level_number = ? ");
        	params.add(level);
        }
        var1.append("       AND Substr(pch.from_date, 1, 10) <= Substr(pp.from_date, 1, 10) ");
        var1.append("       AND ( pch.to_date IS NULL ");
        var1.append("              OR Substr(pch.to_date, 1, 10) >= Substr(pp.to_date, 1, 10) ) ");
        if (!StringUtil.isNullOrEmpty(searchData)) {
			searchData = StringUtil.getEngStringFromUnicodeString(searchData);
			searchData = StringUtil.escapeSqlString(searchData);
			searchData = DatabaseUtils.sqlEscapeString("%" + searchData + "%"); 
			searchData = searchData.substring(1, searchData.length()-1);
			// tim theo truong ten va dia chi
			var1.append("	and (upper(CT.NAME_TEXT) like upper(?) escape '^' ");
			params.add(searchData);
			// tim theo dt co dinh
			var1.append("	or upper(CT.PHONE) like upper(?) escape '^' ");
			params.add(searchData);
			// tim theo ma khach hang
			var1.append("	or upper(CT.SHORT_CODE) like upper(?) escape '^' ");
			params.add(searchData);
			// tim theo so dtdd
			var1.append("	or upper(CT.MOBIPHONE) like upper(?) escape '^' )");
			params.add(searchData);
		} 

		Cursor cTotalRow = null;
		if (isGetTotal) {
			totalPageSql.append("select count(*) as TOTAL_ROW from ("
					+ var1.toString() + " GROUP BY  PCP.OBJECT_ID )) ");
			try {
				cTotalRow = rawQueries(totalPageSql.toString(), params);
				if (cTotalRow != null) {
					if (cTotalRow.moveToFirst()) {
						dto.totalRow = cTotalRow.getInt(0);
					}
				}
			} catch (Exception ex) {
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			} finally {
				try {
					if (cTotalRow != null) {
						cTotalRow.close();
					}
				} catch (Exception e) {
				}
			}
		}
		var1.append("	ORDER BY  PCP.quantity ASC  )");
		var1.append("	GROUP BY  OBJECT_ID ");
		var1.append("	ORDER BY  CUSTOMER_CODE ");
		var1.append(" limit "
				+ Integer.toString(Constants.NUM_ITEM_PER_PAGE));
		var1.append(" offset "
				+ Integer.toString((page - 1) * Constants.NUM_ITEM_PER_PAGE));

		try {
			c = rawQueries(var1.toString(), params);
			if (c != null) {
				
				if (c.moveToFirst()) {
					do {
						CustomerInputQuantityDTO cusDTO = new CustomerInputQuantityDTO();
						cusDTO.initFromCursor(c);
						dto.listCustomer.add(cusDTO);
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

		return dto;
	}

	/**
	 * Lay danh sach san pham can nhap thuc hien cua khach hang
	 * 
	 * @author: hoanpd1
	 * @since: 19:52:20 07-08-2014
	 * @return: CustomerInputQuantityDTO
	 * @throws:  
	 * @param data
	 * @return
	 */
	public CustomerInputQuantityDTO getListProductDonePrograme(Bundle data) { 
		CustomerInputQuantityDTO dto = new CustomerInputQuantityDTO();
		int page = data.getInt(IntentConstants.INTENT_PAGE);
		boolean isGetTotal 	= data.getBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE);
		long pro_info_id 	= data.getLong(IntentConstants.INTENT_PROGRAM_ID);
		long customerId     = data.getLong(IntentConstants.INTENT_CUSTOMER_ID);
		long pro_period_id 	= data.getLong(IntentConstants.INTENT_PERIOD_ID);
		long proCusMapId 	= data.getLong(IntentConstants.INTENT_PRO_CUS_MAP_ID);
		String date_now 	= DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		Cursor c = null;
		Cursor cTotalRow = null;
		AP_PARAM_TABLE table = new AP_PARAM_TABLE(mDB);
		ArrayList<ApParamDTO> listParams = table.getParamsForInputQuantityPrograme();
		if(listParams.size() < 2){
			return null;
		} 
		ArrayList<String> params = new ArrayList<String>();
		
		StringBuffer  var1 = new StringBuffer();
		var1.append("SELECT PCP.pro_cus_process_id AS PRO_CUS_PROCESS_ID, ");
		var1.append("       PCP.pro_info_id        AS PRO_INFO_ID, ");
		var1.append("       PCP.pro_cus_map_id     AS PRO_CUS_MAP_ID, ");
		var1.append("       PCP.pro_period_id      AS PRO_PERIOD_ID, ");
		var1.append("       PCP.object_id          AS OBJECT_ID, ");
		var1.append("       PCP.quantity           AS QUANTITY, ");
		var1.append("       PCP.quantity_pg        AS QUANTITY_PG, ");
		var1.append("       PCP.quantity_shop      AS QUANTITY_SHOP, ");
		var1.append("       PP.from_date           AS PERIOD_FROM_DATE, ");
		var1.append("       PP.to_date             AS PERIOD_TO_DATE, ");
		var1.append("       P.product_id           AS PRODUCT_ID, ");
		var1.append("       P.product_code         AS PRODUCT_CODE, ");
		var1.append("       P.product_name         AS PRODUCT_NAME, ");
		var1.append("       PCP.update_user        AS UPDATE_USER, ");
		var1.append("       CASE ");
		var1.append("         WHEN ( ( Date(?) >= Date(PP.to_date, ?) ) ");
		params.add(date_now);
		params.add(listParams.get(0).value);
		var1.append("                AND ( Date(?) <= Date(PP.to_date, ?) ) ) THEN 1 ");
		params.add(date_now);
		params.add(listParams.get(1).value);
		var1.append("         ELSE 0 end  ALLOW_EDIT ");
		var1.append("FROM   pro_cus_process PCP, ");
		var1.append("       pro_period PP, ");
		var1.append("       product P ");
		var1.append("WHERE  1 = 1 ");
		var1.append("       AND pcp.pro_info_id = ? ");
		params.add("" + pro_info_id);
		var1.append("       AND pcp.pro_period_id = ? ");
		params.add("" + pro_period_id);
		var1.append("       AND pcp.object_id = ? ");
		params.add("" + customerId);
		var1.append("       AND pcp.object_type = 1 ");
		// if(!StringUtil.isNullOrEmpty(level)){
		var1.append("       AND pcp.pro_cus_map_id = ?   ");
		params.add("" + proCusMapId);
		// }
		var1.append("       AND pcp.pro_period_id = pp.pro_period_id ");
		var1.append("       AND pcp.product_id = p.product_id ");
		StringBuilder totalPageSql = new StringBuilder();

		if (isGetTotal) {
			totalPageSql.append("select count(*) as TOTAL_ROW from ("
					+ var1.toString()+")" );
			try {
				cTotalRow = rawQueries(totalPageSql.toString(), params);
				if (cTotalRow != null) {
					if (cTotalRow.moveToFirst()) {
						dto.totalProduct = cTotalRow.getInt(0);
					}
				}
			} catch (Exception ex) {
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			} finally {
				try {
					if (cTotalRow != null) {
						cTotalRow.close();
					}
				} catch (Exception e) {
				}
			}
		}
		var1.append("	ORDER BY P.product_code, P.product_name   ");
		var1.append(" limit "
				+ Integer.toString(Constants.NUM_ITEM_PER_PAGE));
		var1.append(" offset "
				+ Integer.toString((page - 1) * Constants.NUM_ITEM_PER_PAGE));

		try {
			c = rawQueries(var1.toString(), params);
			if (c != null) {
				
				if (c.moveToFirst()) {
					do {
						CustomerInputQuantityDetailDTO product = new CustomerInputQuantityDetailDTO();
						product.initFromCursor(c);
						dto.details.add(product);
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

		return dto;
	}
}