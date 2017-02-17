/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

import com.viettel.dms.dto.db.SaleOrderDetailDTO;

/**
 *  Thong tin chi tiet don hang tren view
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class OrderDetailViewDTO implements Serializable {
	public static final int FREE_PRODUCT = 1;
    public static final int FREE_PRICE = 2;
    public static final int FREE_PERCENT = 3;
    // KM cho mat hang ban
    public static final int PROMOTION_FOR_PRODUCT = 0;
    // KM cho don hang (type promotion ZV019, ZV020)
    public static final int PROMOTION_FOR_ORDER = 1;
    // KM cho don hang (type promotion ZV021)
    public static final int PROMOTION_FOR_ZV21 = 2;
    // san pham khuyen mai cho don hang (type promotion ZV021)
    public static final int PROMOTION_FOR_ORDER_TYPE_PRODUCT = 3;
    
	// stt
	public int numberOrderView;
	// thong tin chi tiet don hang
	public SaleOrderDetailDTO orderDetailDTO;
	// thanh tien = don gia * so luong
	public double totalAmount;
	// ma mat hang
	public String productCode = "";
	// ten mat hang
	public String productName = "";
	// don vi quy doi tinh toan
	public int convfact = 1;
	// so luong dat hien thi tren view: vi du 2/4
	public String quantity;
	// mat hang trong tam hay ko?
	public int isFocus;
	
	public int type;//1: tra thuong sp, 2: tra thuong tien, 3: tra thuong % tien
	public String typeName;
	public long productPromoId;
	public int changeProduct = 0;//1: la duoc lua chon
	public Long keyList;
	public long quantityEdit;
	public long stock; //ton kho
	// ton kho theo dang format thung/hop
	public String remaindStockFormat;
	public long totalOrderQuantity;//Tong so luong cua sp trong trong don hang, gom: ban, KM tu dong, KM = tay
	public double grossWeight;//Trong luong cua san pham
	public String uom2 = "";

	// bien dung de kiem tra co phai chon tu man hinh them hang khong --> dung cho ds san pham khuyen mai
//	public boolean isSelectFromAddProduct = false;
	public int promotionType = 0;
	public String promotionDescription = "";
	
	// ds mat hang KM cua CTKM 21
	public ArrayList<OrderDetailViewDTO> listPromotionForPromo21 = new ArrayList<OrderDetailViewDTO>();
	public long oldDiscountAmount = 0;
	public String suggestedPrice = "";
}
