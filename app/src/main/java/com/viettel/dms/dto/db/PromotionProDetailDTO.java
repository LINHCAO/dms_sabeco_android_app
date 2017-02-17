/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

/**
 *  Chi tiet chuong trinh khuyen mai
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class PromotionProDetailDTO extends AbstractTableDTO{
	// id chi tiet CTKM
	public int oromotionProDetailId ; 
	// id CTKM
	public int promotionProgrameId ; 
	// id sp
	public int productId ;
	// // so luong ban
	public int saleQTY ; 
	// don vi san pham ban
	public String saleUOM ; 
	// so tien ban
	public float saleAMT ; 
	// so tien giam KM neu co
	public float discAMT ; 
	// % KM neu co
	public float discPer ; 
	// ma san pham KM
	public int freeProductId ; 
	// so luong san pham KM
	public int freeQTY ; 
	// don vi sp KM
	public String freeUOM ; 
	// nguoi tao
	public String createUser ;
	// nguoi cap nhat
	public String updateUser ;
	// ngay tao
	public String createDate ;
	// ngay cap nhat
	public String updateDate ;
	// bat buoc co sp nay hay khong? 1: co, 0: khong bat buoc
	public int required ; 
	
	// gia khuyen mai
	public float price;
	
	// ma sp
	public String productCode;
	
	public PromotionProDetailDTO(){
		super(TableType.PROMOTION_PROGRAME_DETAIL_TABLE);
	}
}
