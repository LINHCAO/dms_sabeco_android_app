/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

/**
 *  Luu gia ca san pham
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class PriceDTO extends AbstractTableDTO {
	// ma gia
	public int priceId ;
	// ma san pham
	public int productId ; 
	// hieu luc tu ngay
	public String fromDate ;
	// hieu luc den ngay
	public String toDate ; 
	// 1: hoat dong, 0: ngung
	public int status ; 
	// nguoi tao
	public String createUser ; 
	// nguoi cap nhat
	public String updateUser ; 
	// ngay tao
	public String createDate ; 
	// ngay cap nhat
	public String updateDate ;
	// gia
	public float price ;
	// gia chua vat
	public float priceNotVat;
	// vat
	public float vat;
	
	public PriceDTO(){
		super(TableType.PRICE_TABLE);
	}
}
