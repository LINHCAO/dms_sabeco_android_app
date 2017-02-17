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
import com.viettel.dms.dto.db.PromotionProDetailDTO;
import com.viettel.utils.VTLog;

/**
 *  Chi tiet chuong trinh khuyen mai
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class PROMOTION_PROGRAME_DETAIL_TABLE extends ABSTRACT_TABLE {
	// id chi tiet CTKM
	public static final String PROMOTION_PROGRAM_DETAIL_ID = "PROMOTION_PROGRAM_DETAIL_ID";
	// id CTKM
	public static final String PROMOTION_PROGRAM_ID = "PROMOTION_PROGRAM_ID";
	// id san pham
	public static final String PRODUCT_ID = "PRODUCT_ID";
	// so luong ban
	public static final String SALE_QTY = "SALE_QTY";
	// don vi san pham ban
	public static final String SALE_UOM = "SALE_UOM";
	// so tien ban
	public static final String SALE_AMT = "SALE_AMT";
	// so tien giam KM neu co
	public static final String DISC_AMT = "DISC_AMT";
	// % KM neu co
	public static final String DISC_PER = "DISC_PER";
	// ma san pham KM
	public static final String FREE_PRODUCT_ID = "FREE_PRODUCT_ID";
	// so luong san pham KM
	public static final String FREE_QTY = "FREE_QTY";
	// don vi sp KM
	public static final String FREE_UOM = "FREE_UOM";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// bat buoc co sp nay hay khong? 1: co, 0: khong bat buoc
	public static final String REQUIRED = "REQUIRED";

	private static final String TABLE_PROMOTION_PROGRAME_DETAIL = "PROMOTION_PROGRAM_DETAIL";
	
	public PROMOTION_PROGRAME_DETAIL_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_PROMOTION_PROGRAME_DETAIL;
		this.columns = new String[] { PROMOTION_PROGRAM_DETAIL_ID,
				PROMOTION_PROGRAM_ID, PRODUCT_ID, SALE_QTY, SALE_UOM, SALE_AMT,
				DISC_AMT, DISC_PER, FREE_PRODUCT_ID, FREE_QTY, FREE_UOM, CREATE_USER, UPDATE_USER, CREATE_DATE,
				UPDATE_DATE, REQUIRED, SYN_STATE };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;

	}

	/**
	 * Them 1 dong xuong db
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	@Override
	public long insert(AbstractTableDTO dto) {
		ContentValues value = initDataRow((PromotionProDetailDTO) dto);
		return insert(null, value);
	}

	/**
	 * 
	 * them 1 dong xuong CSDL
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	public long insert(PromotionProDetailDTO dto) {
		ContentValues value = initDataRow(dto);
		return insert(null, value);
	}
	
	/**
	 * Update 1 dong xuong db
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	@Override
	public long update(AbstractTableDTO dto) {
		PromotionProDetailDTO disDTO = (PromotionProDetailDTO)dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.oromotionProDetailId };
		return update(value, PROMOTION_PROGRAM_DETAIL_ID + " = ?", params);
	}

	/**
	 * Xoa 1 dong cua CSDL
	 * @author: TruongHN
	 * @param id
	 * @return: int
	 * @throws:
	 */
	public int delete(String code) {
		String[] params = { code };
		return delete(PROMOTION_PROGRAM_DETAIL_ID + " = ?", params);
	}
	
	public long delete(AbstractTableDTO dto) {
		PromotionProDetailDTO proDetail = (PromotionProDetailDTO)dto;
		String[] params = { "" + proDetail.oromotionProDetailId };
		return delete(PROMOTION_PROGRAM_DETAIL_ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * @author: DoanDM replaced
	 * @param id
	 * @return: DisplayPrdogrameLvDTO
	 * @throws:
	 */
	public PromotionProDetailDTO getRowById(int id) {
		PromotionProDetailDTO dto = null;
		Cursor c = null;
		try {
			String[]params = {"" +id};
			c = query(
					PROMOTION_PROGRAM_DETAIL_ID + " = ?" , params, null, null, null);
		} catch (Exception ex) {
			c = null;
		}
		if (c != null) {
			if (c.moveToFirst()) {
				dto = initLogDTOFromCursor(c);
			}
		}
		if (c != null) {
			c.close();
		}
		return dto;
	}

	 
	
	private PromotionProDetailDTO initLogDTOFromCursor(Cursor c) {
		PromotionProDetailDTO dpDetailDTO = new PromotionProDetailDTO();
		dpDetailDTO.oromotionProDetailId = (c.getInt(c.getColumnIndex(PROMOTION_PROGRAM_DETAIL_ID)));
		dpDetailDTO.promotionProgrameId = (c.getInt(c.getColumnIndex(PROMOTION_PROGRAM_ID)));
		dpDetailDTO.productId = (c.getInt(c.getColumnIndex(PRODUCT_ID)));
		dpDetailDTO.saleQTY = (c.getInt(c.getColumnIndex(SALE_QTY)));
		dpDetailDTO.saleUOM = (c.getString(c.getColumnIndex(SALE_UOM)));
		dpDetailDTO.saleAMT = (c.getInt(c.getColumnIndex(SALE_AMT)));
		
		dpDetailDTO.discAMT = (c.getFloat(c.getColumnIndex(DISC_AMT)));
		dpDetailDTO.discPer = (c.getFloat(c.getColumnIndex(DISC_PER)));
		dpDetailDTO.freeProductId = (c.getInt(c.getColumnIndex(FREE_PRODUCT_ID)));
		dpDetailDTO.freeQTY = (c.getInt(c.getColumnIndex(FREE_QTY)));
		dpDetailDTO.freeUOM = (c.getString(c.getColumnIndex(FREE_UOM)));
		dpDetailDTO.createUser = (c.getString(c.getColumnIndex(CREATE_USER)));
		
		dpDetailDTO.updateUser = (c.getString(c.getColumnIndex(UPDATE_USER)));
		dpDetailDTO.createDate = (c.getString(c.getColumnIndex(CREATE_DATE)));
		dpDetailDTO.updateDate = (c.getString(c.getColumnIndex(UPDATE_DATE)));
		dpDetailDTO.required = (c.getInt(c.getColumnIndex(REQUIRED)));
		// lay product code
		dpDetailDTO.productCode = (c.getString(c.getColumnIndex(PRODUCT_TABLE.PRODUCT_CODE)));
		
		
		return dpDetailDTO;
	}

	/**
	 * Lay tat ca cac dong cua CSDL
	 * @author: HieuNH
	 * @return: Vector<DisplayPrdogrameLvDTO>
	 * @throws:
	 */
	public Vector<PromotionProDetailDTO> getAllRow() {
		Vector<PromotionProDetailDTO> v = new Vector<PromotionProDetailDTO>();
		Cursor c = null;
		try {
			c = query(null,
					null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			PromotionProDetailDTO DisplayPrdogrameLvDTO;
			if (c.moveToFirst()) {
				do {
					DisplayPrdogrameLvDTO = initLogDTOFromCursor(c);
					v.addElement(DisplayPrdogrameLvDTO);
				} while (c.moveToNext());
			}
			c.close();
		}
		return v;

	}
	
	/**
	*  Lay ds chi tiet khuyen mai tu chuong trinh khuyen mai id
	*  @author: TruongHN
	*  @param promoId
	*  @return: ArrayList<PromotionProDetailDTO>
	*  @throws:
	 */
	public ArrayList<PromotionProDetailDTO> getPromotionDetailByPromotionId(int promoId){
		ArrayList<PromotionProDetailDTO> result = new ArrayList<PromotionProDetailDTO>();
		Cursor c = null;
		
		StringBuffer  sqlGetProgrameDetail = new StringBuffer();
		sqlGetProgrameDetail.append("SELECT ppd.*, ");
		sqlGetProgrameDetail.append("       p.product_code ");
		sqlGetProgrameDetail.append("FROM   promotion_program_detail ppd ");
		sqlGetProgrameDetail.append("       LEFT JOIN product p ");
		sqlGetProgrameDetail.append("              ON ppd.free_product_id = p.product_id ");
		sqlGetProgrameDetail.append("WHERE  ppd.promotion_program_id = ? and case when free_product_id > 0 then free_qty > 0 else 1 end ");
		sqlGetProgrameDetail.append("ORDER  BY ppd.product_id ASC, ");
		sqlGetProgrameDetail.append("          ppd.sale_qty ASC, ");
		sqlGetProgrameDetail.append("          ppd.sale_amt ASC ");
		
		try {
			String[]params = {"" +promoId};
			c = rawQuery(sqlGetProgrameDetail.toString(), params);
			
			if (c != null) {
				PromotionProDetailDTO DisplayPrdogrameLvDTO;
				if (c.moveToFirst()) {
					do {
						DisplayPrdogrameLvDTO = initLogDTOFromCursor(c);
						result.add(DisplayPrdogrameLvDTO);
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
		
		return result;
	}
	
	private ContentValues initDataRow(PromotionProDetailDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(PROMOTION_PROGRAM_DETAIL_ID, dto.oromotionProDetailId);
		editedValues.put(PROMOTION_PROGRAM_ID, dto.promotionProgrameId);
		editedValues.put(PRODUCT_ID, dto.productId);
		editedValues.put(SALE_QTY, dto.saleQTY);
		editedValues.put(SALE_UOM, dto.saleUOM);
		editedValues.put(SALE_AMT, dto.saleAMT);
		
		editedValues.put(DISC_AMT, dto.oromotionProDetailId);
		editedValues.put(DISC_PER, dto.discPer);
		editedValues.put(FREE_PRODUCT_ID, dto.freeProductId);
		editedValues.put(FREE_QTY, dto.freeQTY);
		editedValues.put(FREE_UOM, dto.freeUOM);
		editedValues.put(CREATE_USER, dto.createUser);
		
		editedValues.put(UPDATE_USER, dto.oromotionProDetailId);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(UPDATE_DATE, dto.updateDate);
		editedValues.put(REQUIRED, dto.required);

		return editedValues;
	}
}
