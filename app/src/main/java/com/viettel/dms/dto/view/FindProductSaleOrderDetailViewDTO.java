/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.dto.db.SaleOrderDetailDTO;
import com.viettel.dms.sqllite.db.PRICE_TABLE;
import com.viettel.dms.sqllite.db.PRODUCT_TABLE;
import com.viettel.dms.sqllite.db.STOCK_TOTAL_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;

/**
 * data cho man hinh tim kiem san pham them vao don hang
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class FindProductSaleOrderDetailViewDTO implements Serializable {
	/**
	 * 
	 */
	public static int TYPE_OBJECT_SAVE_FILTER = 1;
	public static int TYPE_OBJECT_SAVE_NOT_FILTER = 2;
	private static final long serialVersionUID = 1L;
	// sale order detail info
	public SaleOrderDetailDTO saleOrderDetail = new SaleOrderDetailDTO();
	// product code
	public String productCode;
	// product name
	public String productName;
	// history
	public String history;
	// recomment
	public int recommend;
	// convfact
	public int convfact;
	// uom2
	public String uom2;
	// stock
	public String stock;
	// QUANTITY : so luong ton kho
	public int quantity;
	// available_quantity : so luong co the dat hang
	public long available_quantity = 0;
	// available_quantity format : so luong co the dat hang da format
	public String available_quantity_format;
	// mhTT
	public int mhTT;
	// number product when choose
	public String numProduct;
	// su dung cho man hinh chon SKU them vao danh gia NVBH cua GSNPP
	public boolean isSelected = false;

	// promotion programe code
	public String promotionProgrameCode;

	// flag check has promotion programe code
	public boolean hasPromotionProgrameCode = false;

	// flag check display programe code
	public boolean hasDisplayProgrameCode = false;

	// has choose programe
	public boolean hasSelectPrograme;

	// check type object
	public int typeObject;

	// GSNPP REQUEST SALE
	public int gsnppRequest = 0;

	// gross weight of product
	public double grossWeight = 0;
	public String suggestedPrice = "";

	public FindProductSaleOrderDetailViewDTO() {
		numProduct = "0";
		uom2 = "";
		available_quantity_format = "";
		isSelected = false;
		hasSelectPrograme = false;
		typeObject = TYPE_OBJECT_SAVE_FILTER;
	}

	/**
	 * 
	 * init object with cursor
	 * 
	 * @author: HaiTC3
	 * @param c
	 * @return: void
	 * @throws:
	 */
	public void initSaleOrderDetailObjectFromGetProductStatement(Cursor c) {

		if (c.getColumnIndex("AMOUNT") >= 0) {
			saleOrderDetail.amount = c.getInt(c.getColumnIndex("AMOUNT"));
		} else {
			saleOrderDetail.amount = 0;
		}

		if (c.getColumnIndex("QUANTITY") >= 0) {
			saleOrderDetail.quantity = c.getInt(c.getColumnIndex("QUANTITY"));
		} else {
			saleOrderDetail.quantity = 0;
		}

		if (c.getColumnIndex("CONVFACT") >= 0) {
			convfact = c.getInt(c.getColumnIndex("CONVFACT"));
		} else {
			convfact = 0;
		}

		if (c.getColumnIndex("PRICE_ID") >= 0) {
			saleOrderDetail.priceId = c.getLong(c.getColumnIndex("PRICE_ID"));
		} else {
			saleOrderDetail.priceId = 0;
		}

		if (c.getColumnIndex("PRODUCT_ID") >= 0) {
			saleOrderDetail.productId = c
					.getInt(c.getColumnIndex("PRODUCT_ID"));
		} else {
			saleOrderDetail.productId = 0;
		}

		if (c.getColumnIndex("PRODUCT_CODE") >= 0) {
			productCode = c.getString(c.getColumnIndex("PRODUCT_CODE"));
		} else {
			productCode = "";
		}

		if (c.getColumnIndex("PRODUCT_NAME") >= 0) {
			productName = c.getString(c.getColumnIndex("PRODUCT_NAME"));
		} else {
			productName = "";
		}

		// uom2
		if (c.getColumnIndex(PRODUCT_TABLE.UOM2) >= 0) {
			uom2 = c.getString(c.getColumnIndex(PRODUCT_TABLE.UOM2));
		} else {
			uom2 = "";
		}

		if (c.getColumnIndex("PRICE") >= 0) {
			saleOrderDetail.price = c.getLong(c.getColumnIndex("PRICE"));
			//duongdt fix loi get String 1 gia tri Long -> 1e+07
			//suggestedPrice = StringUtil.getStringFromSQliteCursor(c, "PRICE");
		} else {
			saleOrderDetail.price = 0;
		}
		
		//neu gia > 0, se hien gia
		if (saleOrderDetail.price > 0) {
			suggestedPrice = String.valueOf(saleOrderDetail.price);
		}else{
			//neu <= 0, hien chuoi NULL
			suggestedPrice = "";
		}
		
		
		if (c.getColumnIndex(PRICE_TABLE.PRICE_NOT_VAT) >= 0) {
			saleOrderDetail.priceNotVat = c.getLong(c
					.getColumnIndex(PRICE_TABLE.PRICE_NOT_VAT));
		} else {
			saleOrderDetail.priceNotVat = 0;
		}
		if (c.getColumnIndex(PRICE_TABLE.VAT) >= 0) {
			saleOrderDetail.vat = c
					.getDouble(c.getColumnIndex(PRICE_TABLE.VAT));
		} else {
			saleOrderDetail.vat = 0;
		}
		if (c.getColumnIndex("GROSS_WEIGHT") >= 0) {
			grossWeight = c.getDouble(c.getColumnIndex("GROSS_WEIGHT"));
		} else {
			grossWeight = 0;
		}

		if (c.getColumnIndex("HISTORY") >= 0) {
			history = c.getString(c.getColumnIndex("HISTORY"));
		} else {
			history = "0";
		}

		if (c.getColumnIndex("RECOMMEND") >= 0) {
			recommend = c.getInt(c.getColumnIndex("RECOMMEND"));
		} else {
			recommend = 0;
		}

		// so luong ton kho
		if (c.getColumnIndex(STOCK_TOTAL_TABLE.QUANTITY) >= 0) {
			quantity = c.getInt(c.getColumnIndex(STOCK_TOTAL_TABLE.QUANTITY));
		} else {
			quantity = 0;
		}

		// so luong toi da co the dat hang
		if (c.getColumnIndex(STOCK_TOTAL_TABLE.AVAILABLE_QUANTITY) >= 0) {
			available_quantity = c.getLong(c.getColumnIndex(STOCK_TOTAL_TABLE.AVAILABLE_QUANTITY));
			available_quantity_format = GlobalUtil.formatNumberProductFlowConvfact(available_quantity, convfact);
		} else {
			available_quantity = 0;
			available_quantity_format = "0/0";
		}

		// stock
		if (quantity > 0 && convfact > 0) {

			stock = StringUtil.parseAmountMoney(quantity / convfact) + "/"
					+ StringUtil.parseAmountMoney(quantity % convfact);
		} else {
			stock = "0/0";
		}

		if (c.getColumnIndex("CODE") >= 0) {
			promotionProgrameCode = c.getString(c.getColumnIndex("CODE"));
			if (!StringUtil.isNullOrEmpty(promotionProgrameCode)) {
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
					hasPromotionProgrameCode = false;
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
					hasPromotionProgrameCode = false;
				}
				hasPromotionProgrameCode = result;
			} else {
				hasPromotionProgrameCode = false;
			}
			if(!hasPromotionProgrameCode){
				promotionProgrameCode = "";
			}
		} else {
			hasPromotionProgrameCode = false;
			promotionProgrameCode = "";
		}
		if (c.getColumnIndex("DISPLAY_PROGRAME_CODE") >= 0) {
			int check = c.getInt(c.getColumnIndex("DISPLAY_PROGRAME_CODE"));
			if (check == 1) {
				hasDisplayProgrameCode = true;
			} else {
				hasDisplayProgrameCode = false;
			}
		} else {
			hasDisplayProgrameCode = false;
		}

		if (c.getColumnIndex("TT") >= 0) {
			mhTT = c.getInt(c.getColumnIndex("TT"));
		} else {
			mhTT = 0;
		}
		if (c.getColumnIndex("GSNPP_REQUEST") >= 0) {
			gsnppRequest = c.getInt(c.getColumnIndex("GSNPP_REQUEST"));
		} else {
			gsnppRequest = 0;
		}
	}
}
