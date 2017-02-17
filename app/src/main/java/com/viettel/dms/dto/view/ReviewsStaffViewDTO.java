/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.dto.db.FeedBackDetailDTO;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.viettellib.json.me.JSONArray;

/**
 * thong tin chi tiet man hinh danh gia NVBH tren mot khach hang cu the
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class ReviewsStaffViewDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// danh sach SKU can ban lan sau
	public ArrayList<FeedBackDetailDTO> listSKU = new ArrayList<FeedBackDetailDTO>();
	// danh sach danh gia
	public ArrayList<ReviewsObjectDTO> listReviewsObject = new ArrayList<ReviewsObjectDTO>();

	// feedback dto for sku
	public ReviewsObjectDTO feedBackSKU = new ReviewsObjectDTO();

	public ReviewsStaffViewDTO() {
		listSKU = new ArrayList<FeedBackDetailDTO>();
		listReviewsObject = new ArrayList<ReviewsObjectDTO>();
	}

	/**
	 * 
	 * convert list sku to object feedback dto to save to DB
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: FeedBackDTO
	 * @throws:
	 * @since: Jan 29, 2013
	 */
	public void initFeedbackSKUFromListSKU(long staffId, int type,
			long createUserId, String remainDate, String traningPlandDetailId,
			String customerId, String createDate, String updateDate,
			String shopId) {
		String contentSKU = Constants.STR_BLANK;
		for (int i = 0, size = listSKU.size(); i < size; i++) {
			if (listSKU.get(i).currentState != FeedBackDetailDTO.STATE_DELETED) {
				contentSKU += String.valueOf(listSKU.get(i).productCode);
				contentSKU += ",";
			}
		}
		if (!StringUtil.isNullOrEmpty(contentSKU)) {
			contentSKU = contentSKU.substring(0, contentSKU.length() - 1);
		}

		if (StringUtil.isNullOrEmpty(contentSKU)) {
			feedBackSKU.feedBack.content = contentSKU;
			feedBackSKU.currentStateOfFeedback = FeedBackDetailDTO.STATE_NO_UPDATE;
		} else {
			// init feedback for sku
			feedBackSKU.feedBack.type = type;
			feedBackSKU.feedBack.status = FeedBackDTO.FEEDBACK_STATUS_CREATE;
			feedBackSKU.feedBack.content = contentSKU;
			feedBackSKU.feedBack.staffId = staffId;
			feedBackSKU.feedBack.createUserId = createUserId;
			feedBackSKU.feedBack.remindDate = remainDate;
			feedBackSKU.feedBack.trainingPlanDetailId = traningPlandDetailId;
			feedBackSKU.feedBack.customerId = customerId;
			feedBackSKU.feedBack.createDate = createDate;
			feedBackSKU.feedBack.shopId = shopId;
		}
	}

	/**
	 * 
	 * da co feedback sku truoc do, gio chi cap nhat them remain date va noi
	 * dung
	 * 
	 * @author: HaiTC3
	 * @param remainDate
	 * @return: void
	 * @throws:
	 * @since: Feb 5, 2013
	 */
	public void updateContentAndRemainDate(String remainDate,
			String userIdUpdate) {
		String contentSKU = Constants.STR_BLANK;
		for (int i = 0, size = listSKU.size(); i < size; i++) {
			if (listSKU.get(i).currentState != FeedBackDetailDTO.STATE_DELETED) {
				contentSKU += String.valueOf(listSKU.get(i).productCode);
				contentSKU += ",";
			}
		}
		if (!StringUtil.isNullOrEmpty(contentSKU)) {
			contentSKU = contentSKU.substring(0, contentSKU.length() - 1);
		}

		if (StringUtil.isNullOrEmpty(contentSKU)) {
			feedBackSKU.feedBack.content = contentSKU;
			feedBackSKU.feedBack.remindDate = remainDate;
			feedBackSKU.feedBack.updateUser = userIdUpdate;
			feedBackSKU.currentStateOfFeedback = FeedBackDetailDTO.STATE_DELETED;
		} else {
			if (!contentSKU.equals(feedBackSKU.feedBack.content)
					|| !remainDate.equals(feedBackSKU.feedBack.remindDate)) {
				feedBackSKU.feedBack.content = contentSKU;
				feedBackSKU.feedBack.remindDate = remainDate;
				feedBackSKU.feedBack.updateUser = userIdUpdate;
				feedBackSKU.feedBack.updateDate = DateUtils.now();
				feedBackSKU.currentStateOfFeedback = FeedBackDetailDTO.STATE_NEW_UPDATE;
			} else {
				feedBackSKU.currentStateOfFeedback = FeedBackDetailDTO.STATE_NO_UPDATE;
			}
		}
	}

	/**
	 * 
	 * general sql insert and update reviews to server
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: JSONArray
	 * @throws:
	 */
	public JSONArray generalSQLInsertAndUpdateToServer() {
		JSONArray kq = new JSONArray();

		// general SQL for create feedback sku
		if (this.feedBackSKU != null) {
			if (this.feedBackSKU.currentStateOfFeedback == FeedBackDetailDTO.STATE_NEW_INSERT) {
				kq.put(this.feedBackSKU.feedBack.generateFeedbackSql());
			} else if (this.feedBackSKU.currentStateOfFeedback == FeedBackDetailDTO.STATE_NEW_UPDATE) {
				kq.put(this.feedBackSKU.feedBack
						.generateUpdateContentFeedbackSql());
			} else if (this.feedBackSKU.currentStateOfFeedback == FeedBackDetailDTO.STATE_DELETED) {
				kq.put(this.feedBackSKU.feedBack
						.generalSqlDeleteFeedBackOutOfDB());
			}
		}

		// general SQL for list SKU. just insert new sku or delete sku
		for (int i = 0, size = listSKU.size(); i < size; i++) {
			// chi cap nhat nhung SKU tao moi
			if (listSKU.get(i).currentState == FeedBackDetailDTO.STATE_NEW_INSERT) {
				kq.put(listSKU.get(i).generateCreateSqlInsertFeedbackDetail());
			} else if (listSKU.get(i).currentState == FeedBackDetailDTO.STATE_DELETED) {
				kq.put(listSKU.get(i).generateDeleteSql());
			}
		}

		// general SQL for reviews info. insert new reviews and update content
		// for old reviews
		for (int i = 0, size = listReviewsObject.size(); i < size; i++) {
			ReviewsObjectDTO objectData = listReviewsObject.get(i);
			// new reviews
			if (objectData.currentStateOfFeedback == FeedBackDetailDTO.STATE_NEW_INSERT) {
				kq.put(objectData.feedBack.generateFeedbackSql());
			}
			// update reviews
			else if (objectData.currentStateOfFeedback == FeedBackDetailDTO.STATE_NEW_UPDATE) {
				kq.put(objectData.feedBack.generateUpdateContentFeedbackSql());
			} else if (objectData.currentStateOfFeedback == FeedBackDetailDTO.STATE_DELETED) {
				kq.put(objectData.feedBack.generalSqlDeleteFeedBackOutOfDB());
			}
		}
		return kq;
	}
}
