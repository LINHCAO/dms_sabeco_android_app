/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

/** 
 * Paid Show Room Promotion Dto class
 * PaidShowRoomPromotionDto.java
 * @version: 1.0 
 * @since:  10:35:56 8 Jan 2014
 */
public class PaidShowRoomPromotionDto {
	public String programCode;
	public String programName;
	public String cusCode;
	public String cusName;
	public String fromDate;
	public String toDate;
	public int level;
	public PaidPromotionItem item = new PaidPromotionItem();
	
	public PaidShowRoomPromotionDto(){
		
	}
	
	
	public class PaidPromotionItem{
		public String productCode;
		public String productName;
		public String productPrice;
		public String realNumber;
		public String promotionNumber;
		
		public PaidPromotionItem() {
			productCode = "02AA10";
			productName = "SB D.Alpha Step 1 HT 400g";
			productPrice = "88,700";
			realNumber = "6";
			promotionNumber = "MN05121003";
		}
	}
}
