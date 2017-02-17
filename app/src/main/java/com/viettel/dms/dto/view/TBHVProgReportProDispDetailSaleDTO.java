/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

/**
 *  Mo ta muc dich cua lop (interface)
 *  @author: HoanPD1
 *  @version: 1.0
 *  @since: 1.0
 */
public class TBHVProgReportProDispDetailSaleDTO {
	public int remainSaleTotal;
	public int total;

	public ArrayList<TBHVProgReportProDispDetailSaleRowDTO> listItem;

	public  TBHVProgReportProDispDetailSaleDTO(){
		listItem = new ArrayList<TBHVProgReportProDispDetailSaleRowDTO>();
		remainSaleTotal=0;
		total = 0;
	}
	public void addItem(TBHVProgReportProDispDetailSaleRowDTO c) {
		listItem.add(c);
		remainSaleTotal +=c.remainSale;
	}
//	public TBHVProgReportProDispDetailSaleRowDTO NewProgReportProDispDetailSaleRow() {
//		return new TBHVProgReportProDispDetailSaleRowDTO();
//	}
	/**
	*  Mo ta chuc nang cua ham
	*  @author: HoanPD1
	*  @return
	*  @return: int
	*  @throws: 
	*/
	public int getTotal() {
		// TODO Auto-generated method stub
		return remainSaleTotal;
	}

}
