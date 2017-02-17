/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;

import com.viettel.dms.util.DateUtils;

/**
 *  Luu thong tin chuong trinh khuyen mai
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class PromotionProgrameDTO extends AbstractTableDTO{
	// id chuong trinh khuyen mai
	public int PROMOTION_PROGRAM_ID ; 
	// ma chuong trinh
	public String PROMOTION_PROGRAM_CODE ; 
	// ten chuong trinh
	public String PROMOTION_PROGRAM_NAME ; 
	// trang thai 1: hieu luc, 0: het hieu luc
	public int STATUS ; 
	// ma phan loai chuong trinh khuyen mai
	public String TYPE ; 
	// dang khuyen mai
	public String PRO_FORMAT ;
	// tu ngay
	public String FROM_DATE ;
	// den ngay
	public String TO_DATE ; 
	// quan he 1: or, 0: and
	public int RELATION ; 
	// id NPP
	public int SHOP_ID ; 
	// nguoi tao
	public String CREATE_USER ;
	// nguoi cap nhat
	public String UPDATE_USER ;
	// ngay tao
	public String CREATE_DATE ;
	// ngay cap nhat
	public String UPDATE_DATE ;
	// co tinh boi so khuyen mai hay khong? 1: co tinh, 0: khong tinh
	public int MULTIPLE ;
	// co tinh toi uu hay khong? 1: co tinh, 0: khong tinh
	public int recursive;
	// mo ta
	public String DESCRIPTION;
	
	public static  int RELATION_AND = 0;
    public static  int RELATION_OR = 1;
    
    // kiem tra CTKM co hop le hay khong
    public boolean isInvalid = false; 
	
	public PromotionProgrameDTO(){
		super(TableType.PROMOTION_PROGRAME_TABLE);
	}
	
	/**
	 * 
	*  init promotioin programe dto for request get promotion programe detail
	*  @author: HaiTC3
	*  @param c
	*  @return: void
	*  @throws:
	 */
	public void initDataForRequestGetPromotionProgrameDetail(Cursor c){
		if (c.getColumnIndex("PROMOTION_PROGRAM_CODE") >= 0) {
			PROMOTION_PROGRAM_CODE = c.getString(c.getColumnIndex("PROMOTION_PROGRAM_CODE"));
		} else {
			PROMOTION_PROGRAM_CODE = "";
		}
		if (c.getColumnIndex("PROMOTION_PROGRAM_NAME") >= 0) {
			PROMOTION_PROGRAM_NAME = c.getString(c.getColumnIndex("PROMOTION_PROGRAM_NAME"));
		} else {
			PROMOTION_PROGRAM_NAME = "";
		}
		if (c.getColumnIndex("TYPE") >= 0) {
			TYPE = c.getString(c.getColumnIndex("TYPE"));
		} else {
			TYPE = "";
		}
		if (c.getColumnIndex("FROM_DATE") >= 0) {
			FROM_DATE = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex("FROM_DATE")));
		} else {
			FROM_DATE = "";
		}
		if (c.getColumnIndex("TO_DATE") >= 0) {
			TO_DATE = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex("TO_DATE")));
		} else {
			TO_DATE = "";
		}
		if (c.getColumnIndex("DESCRIPTION") >= 0) {
			DESCRIPTION = c.getString(c.getColumnIndex("DESCRIPTION"));
		} else {
			DESCRIPTION = "";
		}
	}
}
