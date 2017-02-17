/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;


/**
 *  training plan dto
 *  @author: HaiTC3
 *  @version: 1.0
 *  @since: 1.0
 */
public class TrainingPlanDTO extends AbstractTableDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// training plan id
	public long ID;
	// id of supper staff
	public long staffId;
	// thoi gian huan luyen dinh nghia la thang nao
	public String month;
	// status 0: OFF --- 1: ON
	public int status;
	// Cho TBHV ghi ly do Approve or Reject
	public String note;
	// Ngay phe duyet (tam thoi chua su dung)
	public String approveDate;
	// TBHV phe duyet (tam thoi chua su dung)
	public String approceUser;
	// ngay tao
	public String createDate;
	// ngay update cuoi cung
	public String updateDate;
	// id cua NPP
	public int shopId;
	
	public TrainingPlanDTO(){
		super(TableType.TRAINING_PLAN_TABLE);
	}
}
