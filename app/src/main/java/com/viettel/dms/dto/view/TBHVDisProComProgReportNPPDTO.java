/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

/**
 * DTO man hinh :01-07. Tien do CTTB theo NPP (TBHV)
 * 
 * @author: HoanPD1
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVDisProComProgReportNPPDTO {

	// danh sach item
	public ArrayList<TBHVDisProComProgReportItem> arrList;
	// danh sach level code
	public ArrayList<String> arrLevelCode;
	// tong ket qua
	public ArrayList<TBHVDisProComProgReportItemResult> arrResultTotal;
	// tong cua tung level
	public TBHVDisProComProgReportItemResult dtoResultTotal;
	// list display programe for use
	public ArrayList<DisplayPresentProductInfo> listDisplayProgrameInfo = new ArrayList<DisplayPresentProductInfo>();
	// list gsnpp
	public ArrayList<GSNPPInfoDTO> listGSNPPInfo = new ArrayList<GSNPPInfoDTO>();

	public TBHVDisProComProgReportNPPDTO() {
		arrList = new ArrayList<TBHVDisProComProgReportItem>();
		arrLevelCode = new ArrayList<String>();
		arrResultTotal = new ArrayList<TBHVDisProComProgReportItemResult>();
		dtoResultTotal = new TBHVDisProComProgReportItemResult();
		listDisplayProgrameInfo = new ArrayList<DisplayPresentProductInfo>();
		listGSNPPInfo = new ArrayList<GSNPPInfoDTO>();
	}
}
