/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;


/**
 * THONG TIN CHI TIET TRAINING PLAN
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class TrainingPlanDetailDTO extends AbstractTableDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// id cua this table
	public long ID;
	// id cua bang training plan detail
	public long trainingPlanID;
	// staff id
	public long staffId;
	// training date
	public String trainingDate;
	// status
	public int status;
	// note info
	public String note;
	// score
	public int score;
	// create date
	public String createDate;
	// update Date
	public String updateDate;
	// amount plan
	public long amountPlan;
	// amount
	public long amount;
	// number customer plan
	public int numCustomerPlan;
	// number customer order
	public int numCustomerOrder;
	// number customer ir
	public int numCustomerIr;
	// number customer or
	public int numCustomerOr;
	// number customer new
	public int numCustomerNew;
	// shop id
	public int shopId;
	// number customer on
	public int numCustomerOn;
	// state for object in DB
	public int synState;

	public TrainingPlanDetailDTO() {
		super(TableType.TRAINING_PLAN_DETAIL_TABLE);
	}

}
