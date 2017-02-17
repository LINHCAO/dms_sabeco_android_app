/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.viettel.dms.dto.view.ComboBoxDisplayProgrameItemDTO;
import com.viettel.dms.util.DateUtils;

/**
 *  Thong tin CT HTBH
 *  @author: dungnt19
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class ProInfoDTO extends AbstractTableDTO{
	// id chuong trinh khuyen mai
	public long PRO_INFO_ID ; 
	// ma chuong trinh
	public String PRO_INFO_CODE ; 
	// ten chuong trinh
	public String PRO_INFO_NAME ; 
	// Loai chuong trinh: 1: HT01, 2: HT02, 3: HT03, 4: HT04, 5: HT05
	public int TYPE ; 
	// tu ngay
	public String FROM_DATE ;
	// den ngay
	public String TO_DATE ; 
	// Trang thai: 0: du thao; 1: hoat dong; 2: tam ngung
	public int STATUS ; 
	// Phan loai chuong trinh, magetArgumentsp qua bang PRO_CATEGORY
	public long PRO_CATEGORY_ID ; 
	// Loai khach hang: 1: NPP C1, 2: C2, 3: C3, 4: Quan, 5: Tiem tap hoa, 6: Khac
	public int CUSTOMER_TYPE;
	// Id chuong trinh goc
	public long PARENT_PRO_INFO_ID;
	// Loai nguon ngan sach: 1: Tong cong ty, 2: Thuong mai
	public int BUDGET_TYPE;
	// mo ta
	public String CONTENT;
	// Loai chu ky: 1: Thang, 2: Quy, 3: Thoi gian CT, 4: Tuy chinh 
	public int PERIOD_TYPE;
	// nguoi tao
	public String CREATE_USER ;
	// nguoi cap nhat
	public String UPDATE_USER ;
	// ngay tao
	public String CREATE_DATE ;
	// ngay cap nhat
	public String UPDATE_DATE ;
	// id NPP
	public int SHOP_ID ; 
	public int totalLevel;
	// Co can nhap san luong hay khong?
	public boolean isNeedTypeQuantity;
	// Co it nhat 1 chu ki da qua? neu co thi moi co tab thuc hien
	public boolean isHaveDone;
	// Danh sach chu ki tinh thuong da qua cua CTHTBH
	public List<ProPeriodDTO> listPeriod;
	// Danh sach muc chuong trinh
	public List<ComboBoxDisplayProgrameItemDTO> listLevel;
	
	public ProInfoDTO(){
		super(TableType.PRO_INFO_TABLE);
		this.listPeriod = new ArrayList<ProPeriodDTO>();
		this.listLevel = new ArrayList<ComboBoxDisplayProgrameItemDTO>();
	}
	
	/**
	 * 
	*  init promotioin programe dto for request get promotion programe detail
	*  @author: dungnt19
	*  @param c
	*  @return: void
	*  @throws:
	 */
	public void initDataForRequestGetPromotionProgrameDetail(Cursor c){
		if (c.getColumnIndex("PRO_INFO_ID") >= 0) {
			PRO_INFO_ID = c.getLong(c.getColumnIndex("PRO_INFO_ID"));
		} else {
			PRO_INFO_ID = 0;
		}
		
		if (c.getColumnIndex("TOTAL_LEVEL") >= 0) {
			totalLevel = c.getInt(c.getColumnIndex("TOTAL_LEVEL"));
		} else {
			totalLevel = 0;
		}
		
		if (c.getColumnIndex("PRO_INFO_CODE") >= 0) {
			PRO_INFO_CODE = c.getString(c.getColumnIndex("PRO_INFO_CODE"));
		} else {
			PRO_INFO_CODE = "";
		}
		if (c.getColumnIndex("PRO_INFO_NAME") >= 0) {
			PRO_INFO_NAME = c.getString(c.getColumnIndex("PRO_INFO_NAME"));
		} else {
			PRO_INFO_NAME = "";
		}
		if (c.getColumnIndex("TYPE") >= 0) {
			TYPE = c.getInt(c.getColumnIndex("TYPE"));
		} else {
			TYPE = 0;
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
		if (c.getColumnIndex("CONTENT") >= 0) {
			CONTENT = c.getString(c.getColumnIndex("CONTENT"));
		} else {
			CONTENT = "";
		}
		
		if (c.getColumnIndex("NEED_INPUT_QUANTITY") >= 0) {
			boolean needInput = c.getInt(c.getColumnIndex("NEED_INPUT_QUANTITY")) == 1;
			isNeedTypeQuantity = needInput;
		} else {
			isNeedTypeQuantity = false;
		}
		
		if (c.getColumnIndex("IS_HAVE_DONE") >= 0) {
			isHaveDone = c.getInt(c.getColumnIndex("IS_HAVE_DONE")) == 1; 
		} else {
			isHaveDone = false;
		}
	}
	
	/**
	 * Copy cac thuoc tin tu doi tuong DTO khac
	 * @author: quangvt1
	 * @since: 14:20:21 23-05-2014
	 * @return: void
	 * @throws:  
	 * @param dto
	 */
	public void copyPropertiesForDTO(ProInfoDTO dto){
		this.BUDGET_TYPE = dto.BUDGET_TYPE;
		this.CONTENT = dto.CONTENT;
		this.CREATE_DATE = dto.CREATE_DATE;
		this.CREATE_USER = dto.CREATE_USER;
		this.CUSTOMER_TYPE = dto.CUSTOMER_TYPE;
		this.FROM_DATE = dto.FROM_DATE;
		this.PARENT_PRO_INFO_ID = dto.PARENT_PRO_INFO_ID;
		this.PERIOD_TYPE = dto.PERIOD_TYPE;
		this.PRO_CATEGORY_ID = dto.PRO_CATEGORY_ID;
		this.PRO_INFO_CODE = dto.PRO_INFO_CODE;
		this.PRO_INFO_ID = dto.PRO_INFO_ID;
		this.PRO_INFO_NAME = dto.PRO_INFO_NAME;
		this.SHOP_ID = dto.SHOP_ID;
		this.STATUS = dto.STATUS;
		this.TO_DATE = dto.TO_DATE;
		this.totalLevel = dto.totalLevel;
		this.TYPE = dto.TYPE;
		this.UPDATE_DATE = dto.UPDATE_DATE;
		this.UPDATE_USER = dto.UPDATE_USER; 
		
		this.listPeriod.clear();
		for (ProPeriodDTO pro : dto.listPeriod) {
			this.listPeriod.add(pro.clone());
		}
		this.listLevel.clear();
		for (ComboBoxDisplayProgrameItemDTO level : dto.listLevel) {
			this.listLevel.add(level.clone());
		}
	}
}
