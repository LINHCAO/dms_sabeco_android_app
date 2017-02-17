/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.ApParamDTO;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.sqllite.db.CUSTOMER_TABLE;
import com.viettel.dms.sqllite.db.FEED_BACK_TABLE;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;

/**
 * problem info of gsnpp of module gsnpp
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class SupervisorProblemOfGSNPPDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final int OBJECT_TYPE_NOMAL = 0;
	public static final int OBJECT_TYPE_OUT_REMIND_NOT_DONE = 1;
	public static final int OBJECT_TYPE_DONE_TO_DAY = 2;
	public static final int OBJECT_TYPE_DONE_OUT_TO_DAY = 3;
	
	
	// problem status
	public ApParamDTO problemStatus;
	// problem id
	public long feedbackId;
	// problem type
	public String problemType;
	// problem content
	public String problemContent;
	// customer info
	public CustomerDTO customerInfo;
	// remain date for gsnpp that tbhv set
	public String remindDate;
	// create date
	public String createDate;
	// done date
	public String doneDate;
	// status
	public int status;
	// row status : check object status ( 1: red, 2: blue, 3: gray)
	public int rowStatus;

	public SupervisorProblemOfGSNPPDTO() {
		problemStatus = new ApParamDTO();
		feedbackId = 0;
		problemType = Constants.STR_BLANK;
		problemContent = Constants.STR_BLANK;
		customerInfo = new CustomerDTO();
		remindDate = Constants.STR_BLANK;
		rowStatus = 0;
	}

	/**
	 * 
	 * init datea with cursor
	 * 
	 * @param c
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 6, 2012
	 */
	public void initDataWithCursor(Cursor c) {
		// feedback id
		if (c.getColumnIndex(FEED_BACK_TABLE.FEEDBACK_ID) >= 0) {
			feedbackId = c.getLong(c
					.getColumnIndex(FEED_BACK_TABLE.FEEDBACK_ID));
		} else {
			feedbackId = 0;
		}
		// create date
		if (c.getColumnIndex(FEED_BACK_TABLE.CREATE_DATE) >= 0) {
			createDate = c.getString(c
					.getColumnIndex(FEED_BACK_TABLE.CREATE_DATE));
		} else {
			createDate = Constants.STR_BLANK;
		}
		// done date
		if (c.getColumnIndex(FEED_BACK_TABLE.DONE_DATE) >= 0) {
			doneDate = c.getString(c.getColumnIndex(FEED_BACK_TABLE.DONE_DATE));
		} else {
			doneDate = Constants.STR_BLANK;
		}
		// remind date
		if (c.getColumnIndex(FEED_BACK_TABLE.REMIND_DATE) >= 0) {
			remindDate = c.getString(c
					.getColumnIndex(FEED_BACK_TABLE.REMIND_DATE));
		} else {
			remindDate = Constants.STR_BLANK;
		}
		// problem content
		if (c.getColumnIndex(FEED_BACK_TABLE.CONTENT) >= 0) {
			problemContent = c.getString(c
					.getColumnIndex(FEED_BACK_TABLE.CONTENT));
		} else {
			problemContent = Constants.STR_BLANK;
		}
		// problem status
		if (c.getColumnIndex(FEED_BACK_TABLE.STATUS) >= 0) {
			status = c.getInt(c.getColumnIndex(FEED_BACK_TABLE.STATUS));
		} else {
			status = 0;
		}
		// customer id
		if (c.getColumnIndex(CUSTOMER_TABLE.CUSTOMER_ID) >= 0) {
			this.customerInfo.customerId = c.getLong(c
					.getColumnIndex(CUSTOMER_TABLE.CUSTOMER_ID));
		} else {
			this.customerInfo.customerId = 0;
		}
		// customer code
		if (c.getColumnIndex(CUSTOMER_TABLE.CUSTOMER_CODE) >= 0) {
			customerInfo.customerCode = c.getString(c
					.getColumnIndex(CUSTOMER_TABLE.CUSTOMER_CODE));
		} else {
			customerInfo.customerCode = Constants.STR_BLANK;
		}
		// customer name
		if (c.getColumnIndex(CUSTOMER_TABLE.CUSTOMER_NAME) >= 0) {
			customerInfo.customerName = c.getString(c
					.getColumnIndex(CUSTOMER_TABLE.CUSTOMER_NAME));
		} else {
			customerInfo.customerCode = Constants.STR_BLANK;
		}
		
		// customer street
		if (c.getColumnIndex(CUSTOMER_TABLE.STREET) >= 0) {
			customerInfo.setStreet(c.getString(c
					.getColumnIndex(CUSTOMER_TABLE.STREET)));
		} else {
			customerInfo.setStreet(Constants.STR_BLANK);
		}
		// customer housenumber
		if (c.getColumnIndex(CUSTOMER_TABLE.HOUSENUMBER) >= 0) {
			customerInfo.setHouseNumber(c.getString(c
					.getColumnIndex(CUSTOMER_TABLE.HOUSENUMBER)));
		} else {
			customerInfo.setHouseNumber(Constants.STR_BLANK);
		}
		// problem type
		if (c.getColumnIndex("TYPE") >= 0) {
			problemType = c.getString(c
					.getColumnIndex("TYPE"));
		} else {
			problemType = Constants.STR_BLANK;
		}
		
		if (!StringUtil.isNullOrEmpty(doneDate)) {			
			if (doneDate.equals(DateUtils.getCurrentDate())) {
				rowStatus = SupervisorProblemOfGSNPPDTO.OBJECT_TYPE_DONE_TO_DAY;				
			} else {
				rowStatus = SupervisorProblemOfGSNPPDTO.OBJECT_TYPE_DONE_OUT_TO_DAY;
			}
		} else if (!StringUtil.isNullOrEmpty(remindDate)
				&& DateUtils.compareWithNow(remindDate, "dd/MM/yyyy") == -1) {
			rowStatus = SupervisorProblemOfGSNPPDTO.OBJECT_TYPE_OUT_REMIND_NOT_DONE;
		}
		else {
			rowStatus = 0;
		}
	}

}
