/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import net.sqlcipher.database.SQLiteDatabase;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.CustomerDisplayProgrameDTO;
import com.viettel.dms.dto.view.CustomerAttentProgrameDTO;
import com.viettel.dms.dto.view.CustomerProgrameDTO;
import com.viettel.dms.dto.view.ListCustomerAttentProgrameDTO;
import com.viettel.dms.util.DateUtils;
import com.viettel.utils.VTLog;

/**
 * Khach hang tham gia CTTB
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class CUSTOMER_DISPLAY_PROGRAME_TABLE extends ABSTRACT_TABLE {
	// id bang
	public static final String CUSTOMER_DISPLAY_PROGRAME_ID = "CUSTOMER_DISPLAY_PROGRAME_ID";
	// ma khach hang
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	// ma CTTB
	public static final String DISPLAY_PROGRAME_CODE = "DISPLAY_PROGRAME_CODE";
	// muc CTTB
	public static final String LEVEL_CODE = "LEVEL_CODE";
	// tu ngay
	public static final String FROM_DATE = "FROM_DATE";
	// den ngay
	public static final String TO_DATE = "TO_DATE";
	// trang thai
	public static final String STATUS = "STATUS";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";

	public static final String TABLE_CUSTOMER_DISPLAY_PROGRAME = "CUSTOMER_DISPLAY_PROGRAME";

	public CUSTOMER_DISPLAY_PROGRAME_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_CUSTOMER_DISPLAY_PROGRAME;
		this.columns = new String[] { CUSTOMER_DISPLAY_PROGRAME_ID,
				CUSTOMER_ID, DISPLAY_PROGRAME_CODE, LEVEL_CODE, FROM_DATE,
				TO_DATE, STATUS, CREATE_USER, UPDATE_USER, CREATE_DATE,
				UPDATE_DATE, SYN_STATE };
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
		ContentValues value = initDataRow((CustomerDisplayProgrameDTO) dto);
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
	public long insert(CustomerDisplayProgrameDTO dto) {
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
		CustomerDisplayProgrameDTO dtoDisplay = (CustomerDisplayProgrameDTO) dto;
		ContentValues value = initDataRow(dtoDisplay);
		String[] params = { "" + dtoDisplay.customerDisplayProgrameId };
		return update(value, CUSTOMER_DISPLAY_PROGRAME_ID + " = ?", params);
	}

	/**
	 * Xoa 1 dong cua CSDL
	 * 
	 * @author: TruongHN
	 * @return: int
	 * @throws:
	 */
	public int delete(String code) {
		String[] params = { code };
		return delete(CUSTOMER_DISPLAY_PROGRAME_ID + " = ?", params);
	}

	public long delete(AbstractTableDTO dto) {
		CustomerDisplayProgrameDTO dtoDisplay = (CustomerDisplayProgrameDTO) dto;
		String[] params = { "" + dtoDisplay.customerDisplayProgrameId };
		return delete(CUSTOMER_DISPLAY_PROGRAME_ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: DoanDM replaced
	 * @param id
	 * @return: DisplayPrdogrameLvDTO
	 * @throws:
	 */
	public CustomerDisplayProgrameDTO getRowById(String id) {
		CustomerDisplayProgrameDTO dto = null;
		Cursor c = null;
		try {
			String[] params = { id };
			c = query(CUSTOMER_DISPLAY_PROGRAME_ID + " = ?", params, null,
					null, null);
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

	private CustomerDisplayProgrameDTO initDTOFromCursor(Cursor c) {
		CustomerDisplayProgrameDTO dto = new CustomerDisplayProgrameDTO();
		dto.customerDisplayProgrameId = (c.getInt(c
				.getColumnIndex(CUSTOMER_DISPLAY_PROGRAME_ID)));
		dto.customerId = (c.getString(c.getColumnIndex(CUSTOMER_ID)));
		dto.status = (c.getInt(c.getColumnIndex(STATUS)));
		dto.fromDate = (c.getString(c.getColumnIndex(FROM_DATE)));
		dto.toDate = (c.getString(c.getColumnIndex(TO_DATE)));
		dto.createUser = (c.getString(c.getColumnIndex(CREATE_USER)));

		dto.updateUser = (c.getString(c.getColumnIndex(UPDATE_USER)));
		dto.createDate = (c.getString(c.getColumnIndex(CREATE_DATE)));
		dto.updateDate = (c.getString(c.getColumnIndex(UPDATE_DATE)));

		return dto;
	}

	/**
	 * 
	 * lay tat ca cac dong cua CSDL
	 * 
	 * @author: HieuNH
	 * @return
	 * @return: Vector<DisplayPrdogrameLvDTO>
	 * @throws:
	 */
	public Vector<CustomerDisplayProgrameDTO> getAllRow() {
		Vector<CustomerDisplayProgrameDTO> v = new Vector<CustomerDisplayProgrameDTO>();
		Cursor c = null;
		try {
			c = query(null, null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			CustomerDisplayProgrameDTO dto;
			if (c.moveToFirst()) {
				do {
					dto = initDTOFromCursor(c);
					v.addElement(dto);
				} while (c.moveToNext());
			}
		}
		try {
			if (c != null) {
				c.close();
			}
		} catch (Exception e2) {
			// TODO: handle exception
		}
		return v;

	}

	private ContentValues initDataRow(CustomerDisplayProgrameDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(CUSTOMER_DISPLAY_PROGRAME_ID,
				dto.customerDisplayProgrameId);
		editedValues.put(CUSTOMER_ID, dto.customerId);
		editedValues.put(DISPLAY_PROGRAME_CODE, dto.displayProgrameCode);
		editedValues.put(LEVEL_CODE, dto.levelCode);
		editedValues.put(FROM_DATE, dto.fromDate);
		editedValues.put(TO_DATE, dto.toDate);
		editedValues.put(STATUS, dto.status);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(UPDATE_USER, dto.updateUser);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(UPDATE_DATE, dto.updateDate);
		editedValues.put(STATUS, dto.status);

		return editedValues;
	}

	/**
	 * tinh doanh so da dat cua khach hang khi tham gia chuong trih
	 * 
	 * @author : BangHN since : 1.0
	 */
	public long caculateAmountAchievedOfPrograme(String customerId,
			String programId, boolean isFino, boolean hasFino) {
		long amount = 0;
		List<String> params= new ArrayList<String>();
		String dateNow= DateUtils.now();
		StringBuffer sql = new StringBuffer();
		sql.append("select ( sum ( case when  so.order_type = 'CM' then -sod.amount");
		sql.append(" when so.ORDER_TYPE = 'CO' then -sod.amount else sod.AMOUNT end)) as amount");
		sql.append(" from sales_order_detail sod, sales_order so, product p, display_program_detail dpd");
		sql.append(" where sod.sale_order_id = so.sale_order_id and  p.product_id = dpd.product_id AND p.product_id = sod.product_id");
		sql.append(" and sod.is_free_item = 0");

		if (hasFino && !isFino) {// neu co fino va dang tinh doanh so nhom hang
									// khac
			// sql.append(" and p.sub_cat is  null");
//			params = new String[] { programId, customerId, programId };
			sql.append(" and (case when p.sub_cat is null then 0 else p.sub_cat end) not in (select sub_cat from display_programe_sub_cat where status = 1 and display_program_id = ?)");
			params.add(programId);
		}
		if (isFino) {
			// sql.append(" and p.sub_cat = 'C_Fino'");
//			params = new String[] { programId, customerId, programId };
			sql.append(" and (case when p.sub_cat is null then 0 else p.sub_cat end) in (select sub_cat from display_programe_sub_cat where status = 1 and display_program_id = ?)");
			params.add(programId);
		}
		sql.append(" and so.customer_id = ?");
		params.add(customerId);
		sql.append(" and dpd.display_program_id = ?");
		params.add(programId);
		sql.append(" and ((so.state = 2 and date(so.order_date) < date(?)"); // lay trong thang hien tai
		params.add(dateNow);
																								
		sql.append(" and date(so.order_date) >= DATE(?,'start of month'))");
		params.add(dateNow);

		sql.append(" or (so.state = 1 and so.is_send = 1 and date(so.order_date) = date(?)))");
		params.add(dateNow);

		String[] param= new String[params.size()];
		param=params.toArray(param);
		
		Cursor c = null;
		try {
			c = rawQuery(sql.toString(), param);
			if (c != null) {
				if (c.moveToFirst()) {
					amount = c.getLong(c.getColumnIndex("amount"));
				}
			}

		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try{
				if (c != null) {
					c.close();
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		return amount;
	}

	/**
	 * Lay danh sach chuong trinh cua khach hang tham gia
	 * 
	 * @author : BangHN since : 1.0
	 */
	public ArrayList<CustomerProgrameDTO> getCustomerProgrames(String customerId) {
		ArrayList<CustomerProgrameDTO> result = new ArrayList<CustomerProgrameDTO>();
		String dateNow = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);

		StringBuffer  sql = new StringBuffer();
		sql.append("SELECT display_programe_id, ");
		sql.append("       display_programe_code, ");
		sql.append("       display_programe_level, ");
		sql.append("       display_programe_name, ");
		sql.append("       from_date, ");
		sql.append("       to_date, ");
		sql.append("       amount_plan, ");
		sql.append("       amount, ");
		sql.append("       AMOUNT_REMAIN AMOUNT_REMAIN, ");
		sql.append("       result, ");
		sql.append("       Group_concat(DISTINCT product_info_code) cat ");
		sql.append("FROM   (SELECT RDP.display_programe_id, ");
		sql.append("               RDP.display_programe_code, ");
		sql.append("               RDP.display_programe_name, ");
		sql.append("               RDP.display_programe_level, ");
		sql.append("               Strftime('%d/%m/%Y', RDP.from_date) AS from_date, ");
		sql.append("               Strftime('%d/%m/%Y', RDP.to_date)   AS to_date, ");
		sql.append("               RDP.amount_plan, ");
		sql.append("               RDP.amount, ");
		sql.append("               RDP.amount_remain AMOUNT_REMAIN, ");
		sql.append("               ( RDP.amount_plan - RDP.amount )    AS result, ");
		sql.append("               PI.product_info_code ");
		sql.append("        FROM   rpt_display_program RDP ");
		sql.append("               LEFT JOIN display_program_detail DPD ");
		sql.append("                      ON RDP.[display_programe_id] = DPD.display_program_id ");
		sql.append("               LEFT JOIN product PR ");
		sql.append("                      ON DPD.product_id = PR.product_id ");
		sql.append("               LEFT JOIN product_info PI ");
		sql.append("                      ON PR.cat_id = PI.product_info_id ");
		sql.append("        WHERE  RDP.customer_id = ? ");
		sql.append("               AND Ifnull(substr(RDP.to_date,0,11) >= ?, 0) ");
		sql.append("               AND ( DPD.product_id IS NULL ");
		sql.append("                      OR ( PR.status = 1 ");
		sql.append("                           AND PI.status = 1 ) ) ");
		sql.append("        ORDER  BY product_info_code) ");
		sql.append("GROUP  BY display_programe_code, ");
		sql.append("          display_programe_name ");


		
		String[] params = new String[] { customerId, dateNow };
		Cursor c = null;
		try {
			c = rawQuery(sql.toString(), params);
			if (c != null) {
				if (c.moveToFirst()) {
					CustomerProgrameDTO customerPrograme;
					do {
						customerPrograme = new CustomerProgrameDTO();
						customerPrograme.initCustomerProgrameDTO(c);
						result.add(customerPrograme);
					} while (c.moveToNext());
				}
			}

		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try{
				if (c != null) {
					c.close();
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}
	
	/**
	 * Lay danh sach khach hang tham gia chuong trinh
	 * 
	 * @author: ThanhNN8
	 * @param extPage
	 * @param displayProgramCode
	 * @param customer_code
	 * @param customer_name
	 * @return
	 * @return: ListCustomerAttentProgrameDTO
	 * @throws:
	 */
	@SuppressLint("DefaultLocale")
	public ListCustomerAttentProgrameDTO getListCustomerAttentPrograme(String extPage, String displayProgramCode, long displayProgrameId, String customer_code, String customer_name, long staffId, boolean checkPagging) {
		List<String> countparams = new ArrayList<String>();
		String dateNow= DateUtils.now();
		StringBuilder sqlCountQuery = new StringBuilder();
		sqlCountQuery.append("select c.customer_id, c.customer_code, c.customer_name, cdp.level_code, dp.display_program_id, dpl.amount, dp.percent_fino");
		sqlCountQuery.append(" from customer as c, customer_display_programe as cdp, display_program_level as dpl, display_program as dp");
		sqlCountQuery.append(" where cdp.customer_id = c.customer_id");
		sqlCountQuery.append(" and cdp.display_programe_code = dpl.display_program_code");
		sqlCountQuery.append(" and cdp.display_programe_code = dp.display_program_code");
		sqlCountQuery.append(" and cdp.level_code = dpl.level_code");
		sqlCountQuery.append(" and c.status = 1");
		sqlCountQuery.append(" and cdp.status = 1");
		sqlCountQuery.append(" and dpl.status = 1");
		sqlCountQuery.append(" and (SELECT date(?)) >= date(cdp.from_date)");
		countparams.add(dateNow);
		sqlCountQuery.append(" and ((SELECT date(?)) <= date(cdp.to_date) or cdp.to_date is null)");
		countparams.add(dateNow);
		sqlCountQuery.append(" and cdp.display_programe_code = ?");
		countparams.add(displayProgramCode);
		sqlCountQuery.append(" and cdp.staff_id = ?");
		countparams.add("" + staffId);
		List<String> params = new ArrayList<String>();
		StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append("SELECT c.customer_id, c.customer_code, c.customer_name, cdp.level_code, dp.display_program_id, dpl.amount, dp.percent_fino,");
		sqlQuery.append(" (case when dp.percent_fino is not null then");
		sqlQuery.append(" (SELECT (SUM (CASE WHEN so.order_type = 'CM' THEN -sod.amount WHEN so.ORDER_TYPE = 'CO' THEN -sod.amount ELSE sod.AMOUNT END)) AS amount");
		sqlQuery.append(" FROM sales_order_detail sod, sales_order so, product p, display_programe_detail dpd");
		sqlQuery.append(" WHERE sod.sale_order_id = so.sale_order_id AND p.product_id = dpd.product_id AND p.product_id = sod.product_id AND sod.is_free_item = 0");
		sqlQuery.append(" AND p.SUB_CAT is not null AND exists(select 1 from display_programe_sub_cat where status = 1 and display_program_id = ? and sub_cat = p.sub_cat)");
		params.add(displayProgrameId + "");
		sqlQuery.append(" AND so.customer_id = c.customer_id AND dpd.display_program_id = ?");
		params.add(displayProgrameId + "");
		sqlQuery.append(" AND ((so.state = 2 AND date(so.order_date) < date(?) AND date(so.order_date) >= DATE(?,'start of month'))");
		params.add(dateNow);
		params.add(dateNow);
		sqlQuery.append(" OR (so.state = 1 AND so.is_send = 1 AND date(so.order_date) = date(?))))");
		params.add(dateNow);
		sqlQuery.append(" else 0 end) as AMOUT_FINO,");
		sqlQuery.append(" (SELECT (SUM (CASE WHEN so.order_type = 'CM' THEN -sod.amount WHEN so.ORDER_TYPE = 'CO' THEN -sod.amount ELSE sod.AMOUNT END)) AS amount");
		sqlQuery.append(" FROM sales_order_detail sod, sales_order so, product p, display_program_detail dpd");
		sqlQuery.append(" WHERE sod.sale_order_id = so.sale_order_id AND p.product_id = dpd.product_id AND p.product_id = sod.product_id AND sod.is_free_item = 0");
		sqlQuery.append(" AND not exists(select 1 from display_programe_sub_cat where status = 1 and display_program_id = ? and sub_cat = p.sub_cat)");
		params.add(displayProgrameId + "");
		sqlQuery.append(" AND so.customer_id = c.customer_id AND dpd.display_program_id = ?");
		params.add(displayProgrameId + "");
		sqlQuery.append(" AND ((so.state = 2 AND date(so.order_date) < date(?) AND date(so.order_date) >= DATE(?))");
		params.add(dateNow);
		params.add(dateNow);
		sqlQuery.append(" OR (so.state = 1 AND so.is_send = 1 AND date(so.order_date) = date(?)))) as AMOUNT_OTHERS");
		params.add(dateNow);
		sqlQuery.append(" FROM customer c, customer_display_programe cdp, display_program_level dpl, display_program dp");
		sqlQuery.append(" WHERE cdp.customer_id = c.customer_id AND cdp.display_programe_code = dpl.display_program_code");
		sqlQuery.append(" AND cdp.display_programe_code = dp.display_program_code AND cdp.level_code = dpl.level_code AND c.status = 1 AND cdp.status = 1");
		sqlQuery.append(" AND date(?) >= date(cdp.from_date) AND (date(?) <= date(cdp.to_date) OR cdp.to_date IS NULL)");
		params.add(dateNow);
		params.add(dateNow);
		sqlQuery.append(" AND cdp.display_programe_code = ? AND cdp.staff_id = ?");
		params.add(displayProgramCode);
		params.add("" + staffId);
		//for count sql query

		if (customer_code.length() > 0) {
			sqlCountQuery.append(" and LOWER(substr(c.customer_code,1,3)) like ?");
			sqlQuery.append(" and LOWER(substr(c.customer_code,1,3)) like ?");
			params.add("%" + customer_code.toLowerCase() + "%");
			countparams.add("%" + customer_code.toLowerCase() + "%");
		}
		if (customer_name.length() > 0) {
			sqlCountQuery.append(" and LOWER(c.customer_name_address_text) like ?");
			sqlQuery.append(" and LOWER(c.customer_name_address_text) like ?");
			params.add("%" + customer_name.toLowerCase() + "%");
			countparams.add("%" + customer_name.toLowerCase() + "%");
		}
		String getCountProductList = " select count(*) as total_row from ("
				+ sqlCountQuery.toString() + ") ";

		sqlQuery.append(" order by c.customer_code, c.customer_name");
		
		String queryGetListCustomerAttentPrograme = sqlQuery.toString();
		ListCustomerAttentProgrameDTO result = new ListCustomerAttentProgrameDTO();
		Cursor cTmp = null;
		try {
			String[] arrCountParam = new String[countparams.size()];
			arrCountParam= countparams.toArray(arrCountParam);
			// get total row first
			if (!checkPagging) {
				cTmp = rawQuery(getCountProductList, arrCountParam);
				int total = 0;
				if (cTmp != null) {
					cTmp.moveToFirst();
					total = cTmp.getInt(0);
					result.setTotalSize(total);
					VTLog.v("total item: ", "= " + total);
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
		// end
		
		String[] arrParam = new String[params.size()];
		arrParam=params.toArray(arrParam);
		Cursor c = null;
		try {
			List<CustomerAttentProgrameDTO> listCustomer = new ArrayList<CustomerAttentProgrameDTO>();
			c = rawQuery(queryGetListCustomerAttentPrograme + extPage, arrParam);

			if (c != null) {

				if (c.moveToFirst()) {
					do {
						CustomerAttentProgrameDTO customerAttent = new CustomerAttentProgrameDTO();
						customerAttent.initDataFromCursor(c);
						listCustomer.add(customerAttent);
					} while (c.moveToNext());
				}
			}
			result.setListCustomer(listCustomer);

		} catch (Exception e) {
			result = null;
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

}
