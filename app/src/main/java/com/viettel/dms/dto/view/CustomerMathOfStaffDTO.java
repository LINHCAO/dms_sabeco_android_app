/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import android.database.Cursor;

/**
 * thong tin diem cua khach hang ma NVBH da ban trong ngay
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class CustomerMathOfStaffDTO {
	// customer id
	public long customer_id; // doanh so da dat trong ngay cua khach hang
	public long amount;
	// doanh so chi tieu trong ngay cua khach hang
	public long planAmount;
	// tien do thuc hien
	public double percent;
	// check use in plan or out of plan (0 : ngoai tuyen, 1: trong tuyen)
	public int isVisitPlan;

	public CustomerMathOfStaffDTO() {
		customer_id = 0;
		amount = 0;
		planAmount = 0;
		percent = 0;
		isVisitPlan = 0;
	}

	/**
	 * 
	*  lay thong tin cua khach hang va diem cua khach hang trong ngay
	*  @author: HaiTC3
	*  @param c
	*  @return: void
	*  @throws:
	 */
	public void initDataFromCursor(Cursor c){
		if (c.getColumnIndex("CUSTOMER_ID") >= 0) {
			customer_id = c.getLong(c.getColumnIndex("CUSTOMER_ID"));
		}
		if (c.getColumnIndex("AMOUNT") >= 0) {
			amount = c.getLong(c
					.getColumnIndex("AMOUNT"));
		}
		if (c.getColumnIndex("PLAN_AMOUNT") >= 0) {
			planAmount = c.getLong(c
					.getColumnIndex("PLAN_AMOUNT"));
		}
		if (c.getColumnIndex("PERCENT") >= 0) {
//			percent = Math.round(c.getFloat(c.getColumnIndex("PERCENT")));
			percent = Math.round(c.getFloat(c.getColumnIndex("PERCENT"))* 100.0) / 100.0;
		}
		if (c.getColumnIndex("IS_VISIT_PLAN") >= 0) {
			isVisitPlan = c.getInt(c
					.getColumnIndex("IS_VISIT_PLAN"));
		}
	}
}
