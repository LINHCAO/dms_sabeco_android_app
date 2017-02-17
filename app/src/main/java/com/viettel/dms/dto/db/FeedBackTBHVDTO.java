/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import java.io.Serializable;

/**
 * reviews of tbhv for gsnpp
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class FeedBackTBHVDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// feedback info basic
	public FeedBackDTO feedBackBasic;
	// Da xoa
	public static final int STATUS_DELETE = 0;
	// tao moi
	public static final int STATUS_NEW_CREATE = 1;
	// gsnpp da thuc hien
	public static final int STATUS_GSNPP_DONE = 2;
	// da duyet
	public static final int STATUS_DONE_CONFIRM = 3;
	// state new insert
	public static final int STATE_NEW_INSERT = 0;
	// state new update
	public static final int STATE_NEW_UPDATE = 1;
	// state deleted
	public static final int STATE_DELETED = 2;
	// state no update
	public static final int STATE_NO_UPDATE = 3;
	// state insert or update
	public int currentState;

	public FeedBackTBHVDTO() {
		this.feedBackBasic = new FeedBackDTO();
		this.currentState = STATE_NO_UPDATE;
	}

	public FeedBackTBHVDTO(int type, int newCurrentState) {
		this.currentState = newCurrentState;
		this.feedBackBasic = new FeedBackDTO();
		this.feedBackBasic.type = type;
	}

	/**
	 * 
	 * update feedback info
	 * 
	 * @param desc
	 * @param staffId
	 * @param status
	 * @param userUpdate
	 * @param createDate
	 * @param remindDate
	 * @param isDelete
	 * @param createUserId
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 8, 2012
	 */
	public void updateFeedbackInf(String desc, String staffId, int status,
			String userUpdate, String createDate, String remindDate,
			int isDelete, String createUserId) {
		feedBackBasic.content = desc;
		feedBackBasic.staffId = Long.parseLong(staffId);
		feedBackBasic.status = status;
		feedBackBasic.updateUser = userUpdate;
		feedBackBasic.createDate = createDate;
		feedBackBasic.remindDate = remindDate;
		feedBackBasic.isDeleted = isDelete;
		feedBackBasic.createUserId = Long.parseLong(createUserId);

	}
}
