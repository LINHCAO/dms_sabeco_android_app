/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import android.database.Cursor;

import com.viettel.dms.util.StringUtil;

/**
 * 
 * dto thong tin cua khach hang tham gia chuong trinh truong bay
 * 
 * @author: ThanhNN8
 * @version: 1.0
 * @since: 1.0
 */
public class CustomerAttentProgrameDTO {
	private String customerCode; //ma khach hang
	private String customerName; //ten khach hang
	private String customerLevel; //muc tham gia
	private Long customerSalesRemain; //doanh so con lai
	private String customerId; //id cua khach hang
	private String displayProgrameId; //id cua chuong trinh
	
	public long amountLevel = 0;//doanh so dinh muc cua muc khach hang tham gia
	public int percentFino = 0;//% nhom hang fino
	public int percentOther = 0;//% nhom hang con lai
	public long finoTarget = 0;//chi tieu nhom fino
	public long otherTarget = 0;//chi tieu nhom con lai
	public long finoRest = 0;//ds fino con lai
	public long otherRest = 0;//ds nhom khac con lai 
	public long finoGot = 0;//fino da dat
	public long otherGot = 0;//nhom khac da dat
	public long targetTotal = 0;//tong chi tieu
	public long gotTotal = 0;//tong da dat
	public long restTotal=0;//tong con lai
	
	
	/**
	 * @return the customerCode
	 */
	public String getCustomerCode() {
		return customerCode;
	}
	/**
	 * @param customerCode the customerCode to set
	 */
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return the customerLevel
	 */
	public String getCustomerLevel() {
		return customerLevel;
	}
	/**
	 * @param customerLevel the customerLevel to set
	 */
	public void setCustomerLevel(String customerLevel) {
		this.customerLevel = customerLevel;
	}
	/**
	 * @return the customerSalesRemain
	 */
	public Long getCustomerSalesRemain() {
		return customerSalesRemain;
	}
	/**
	 * @param customerSalesRemain the customerSalesRemain to set
	 */
	public void setCustomerSalesRemain(Long customerSalesRemain) {
		this.customerSalesRemain = customerSalesRemain;
	}
	
	/**
	 * 
	*  Ham tao du lieu tu cursor
	*  @author: ThanhNN8
	*  @param c
	*  @throws Exception
	*  @return: void
	*  @throws:
	 */
	public void initDataFromCursor(Cursor c) throws Exception {
		try {
			if (c == null) {
				throw new Exception("Cursor is empty");
			}
			customerCode = (StringUtil.isNullOrEmpty(c
					.getString(c.getColumnIndexOrThrow("CUSTOMER_CODE"))) ? ""
					: c.getString(c.getColumnIndexOrThrow("CUSTOMER_CODE")));
			customerName = (StringUtil.isNullOrEmpty(c
					.getString(c.getColumnIndexOrThrow("CUSTOMER_NAME"))) ? ""
					: c.getString(c.getColumnIndexOrThrow("CUSTOMER_NAME")));
			customerLevel = (StringUtil.isNullOrEmpty(c
					.getString(c.getColumnIndexOrThrow("LEVEL_CODE"))) ? ""
					: c.getString(c.getColumnIndexOrThrow("LEVEL_CODE")));
			customerId = (StringUtil.isNullOrEmpty(c
					.getString(c.getColumnIndexOrThrow("CUSTOMER_ID"))) ? ""
					: c.getString(c.getColumnIndexOrThrow("CUSTOMER_ID")));
			displayProgrameId = (StringUtil.isNullOrEmpty(c
					.getString(c.getColumnIndexOrThrow("DISPLAY_PROGRAM_ID"))) ? ""
					: c.getString(c.getColumnIndexOrThrow("DISPLAY_PROGRAM_ID")));
			if (StringUtil.isNullOrEmpty(c
					.getString(c.getColumnIndexOrThrow("AMOUNT"))) == false) {
				amountLevel = c.getLong(c.getColumnIndexOrThrow("AMOUNT"));
			} 
			if (StringUtil.isNullOrEmpty(c
					.getString(c.getColumnIndexOrThrow("PERCENT_FINO"))) == false) {
				percentFino = c.getInt(c.getColumnIndexOrThrow("PERCENT_FINO"));
				
			} 
			if(percentFino > 0){
				percentOther = 100 - percentFino;
				finoTarget = (percentFino*amountLevel)/100;
				otherTarget = amountLevel - finoTarget;
			}else{
				percentOther = 100;
				otherTarget = amountLevel;
			}
			if (c.getColumnIndex("AMOUNT_FINO") >= 0) {
				finoGot = c.getLong(c.getColumnIndex("AMOUNT_FINO"));
			} else {
				finoGot = 0;
			}
			
			if (c.getColumnIndex("AMOUNT_OTHERS") >= 0) {
				otherGot = c.getLong(c.getColumnIndex("AMOUNT_OTHERS"));
			} else {
				otherGot = 0;
			}
			
			long totalRemain = 0;
			// tong kiem tra dat
			long ktd = Math.min(this.finoTarget,
					this.finoGot) + this.otherGot;
			if (ktd < this.amountLevel) {
				totalRemain = this.amountLevel - ktd;
			} else {
				totalRemain = this.amountLevel
						- (this.finoGot + this.otherGot);
			}
			customerSalesRemain = totalRemain;
			
		} catch (Exception ex) {
			throw ex;
		}
	}
	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the displayProgrameId
	 */
	public String getDisplayProgrameId() {
		return displayProgrameId;
	}
	/**
	 * @param displayProgrameId the displayProgrameId to set
	 */
	public void setDisplayProgrameId(String displayProgrameId) {
		this.displayProgrameId = displayProgrameId;
	}
	/**
	 * @return the amoutLevel
	 */
	public Long getAmoutLevel() {
		return amountLevel;
	}
	/**
	 * @param amoutLevel the amoutLevel to set
	 */
	public void setAmoutLevel(Long amoutLevel) {
		this.amountLevel = amoutLevel;
	}
	/**
	 * @return the fino
	 */
	public int getFino() {
		return percentFino;
	}
	/**
	 * @param fino the fino to set
	 */
	public void setFino(int fino) {
		this.percentFino = fino;
	}
}
