/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.util.StringUtil;
/**
 * Info for GeneralStatisticsView, include info of GeneralStatisticsExView
 * @author duongdt3
 *
 */
public class GeneralStatisticsTNPGViewDTO implements Serializable {
	private static final long serialVersionUID = 66745433265851538L;

	public GSBHGeneralStatisticsViewDTO reportInfo; 
	public ListStaffDTO listStaffInfo;
	public GeneralStatisticsTNPGViewDTO(){
		reportInfo = new GSBHGeneralStatisticsViewDTO();
		listStaffInfo = new ListStaffDTO();
	}
	
	public static class CustomerInfo{
		public String cusName;
		public String cusCode;
		public String address;
		public CustomerInfo(){
			address = "";
			cusCode = "";
			cusName = "";
		}

		public void initForCursor(Cursor c) {
			address = StringUtil.getStringFromSQliteCursor(c, "ADDRESS");
			cusCode = StringUtil.getStringFromSQliteCursor(c, "CUSTOMER_CODE");
			cusName = StringUtil.getStringFromSQliteCursor(c, "CUSTOMER_NAME");
		}
	}
}
