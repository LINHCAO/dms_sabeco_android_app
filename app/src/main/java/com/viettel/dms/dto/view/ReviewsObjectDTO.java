/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.dto.db.FeedBackDetailDTO;

/**
 * thong tin chi tiet mot danh gia cua GSNPP
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class ReviewsObjectDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// thong tin bang feedback
	public FeedBackDTO feedBack;
	// current state
	public int currentStateOfFeedback;

	public ReviewsObjectDTO() {
		currentStateOfFeedback = FeedBackDetailDTO.STATE_NEW_INSERT;
		feedBack = new FeedBackDTO();
	}

	/**
	 * 
	 * init data with cursor
	 * 
	 * @author: HaiTC3
	 * @param c
	 * @return: void
	 * @throws:
	 */
	public void parserDataFromCursor(Cursor c) {
		feedBack.initDataWithCursor(c);
	}
}
