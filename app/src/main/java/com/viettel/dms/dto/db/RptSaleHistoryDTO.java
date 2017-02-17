/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;

/** 
 * Rpt Sale History DTO class
 * RptSaleHistoryDTO.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  10:16:29 8 Jan 2014
 */
@SuppressWarnings("serial")
public class RptSaleHistoryDTO extends AbstractTableDTO{
	public long id;
	public long staffId;
	public int month;
	public int numCustomer;
	public int numCustNotOrder;
	public int totalValueOfOrder;
	public int numSKU;
	public String createDate;
	
	public RptSaleHistoryDTO() {
		super(TableType.RPT_SALE_HISTORY);
	}
	
	public void initDataFromCursor(Cursor c) {
		
	}
}
