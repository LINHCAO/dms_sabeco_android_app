/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.FEED_BACK_TABLE;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

import java.util.List;

/**
 * Thong tin dia ban
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class FeedBackDTO extends AbstractTableDTO implements Parcelable{
	// nvbh
	public static String FEEDBACK_TYPE_AP_NVBH = "FEEDBACK_TYPE_NVBH"; 
	// gsnpp
	public static String FEEDBACK_TYPE_AP_GSNPP = "FEEDBACK_TYPE_GSNPP"; 
	// xoa
	public static int FEEDBACK_STATUS_DELETE = 0; 
	// tiep nhan
	public static int FEEDBACK_STATUS_CREATE = 1; 
	// user nhan feedback da xu ly
	public static int FEEDBACK_STATUS_STAFF_DONE = 2; 
	// nguoi quan ly da xu ly
	public static int FEEDBACK_STATUS_STAFF_OWNER_DONE = 3;
	// feed back type 1
	public static int FEEDBACK_TYPE_SERVICE = 0; 
	// feed back type 2
	public static int FEEDBACK_TYPE_PRODUCT = 1; 
	// feed back type 2
	public static int FEEDBACK_TYPE_PROGRAME_HTTM = 2;
	// feed back type 2
	public static int FEEDBACK_TYPE_DISPLAY = 3;
	// feed back type 2
	public static int FEEDBACK_TYPE_SHELF = 4;
    // feed back type 2
	public static int FEEDBACK_TYPE_CTTB = 5;
	// feed back ve chuong trinh trung bay
	public static int FEEDBACK_TYPE_ABOUT_DISPLAY = 6; 
	// feed back ho tro NVBH: gom cac danh gia ve ki nang NVBH
	public static int FEEDBACK_TYPE_SUPPORT_NVBH = 7;
	// feed back tich luy ve doanh so: gom cac danh gia ve cai
	//thien trung bay , phan anh khach hang
	public static int FEEDBACK_TYPE_AMOUNT = 8;
	// feed back type 9 (SKU)
	public static int FEEDBACK_TYPE_SKU = 9; 

	// nhan xet chung
	public static final int FEEDBACK_TYPE_GENERAL = 10;
	// phan phoi va trung bay
	public static final int FEEDBACK_TYPE_DISTRIBUTION_DISPLAY = 11;
	// dich vu khach hang
	public static final int FEEDBACK_TYPE_SERVICE_CUSTOMER = 12;
	// quan he khach hang
	public static final int FEEDBACK_TYPE_RELATIONSHIP_CUSTOMER = 13;
	// ky nang NVBH
	public static final int FEEDBACK_TYPE_SKILL_NVBH = 14;
	// ky nang GSNPP
	public static final int FEEDBACK_TYPE_SKILL_GSNPP = 15;
	// van de khac
	public static final int FEEDBACK_TYPE_OTHER_PROBLEM = 16;

	// id bang
	public long feedBackId;
	// id NVBH
	public long staffId;
	// id KH
	public String customerId;
	// id Shop
	public String shopId;
	// id training Plan Detail
	public String trainingPlanDetailId;
	// Ma KH
	public String customerCode;
	// Ten KH
	public String customerName;
	// trang thai
	public int status;
	// noi dung phan anh
	public String content;
	// nguoi tao
	public String updateUser;
	// ap param name
	public String apParamName;
	// ngay tao
	public String createDate;
	// ngay cap nhat
	public String updateDate;
	// ngay thuc hien
	public String doneDate;
	// num return
	public String numReturn;
	// ngay nhac nho
	public String remindDate;
	// loai phan anh
	public int type;
	// loai van de do nvbh tao hay gsnpp tao
	public String type_ap;
	// da xoa
	public int isDeleted;
	// supper staff id
	public long createUserId;
	// so dien thoai nha
	public String houseNumber;
	// street
	public String street;
	// address
	public String address;
	// thong tin nhan vien
	public StaffDTO staffDto = new StaffDTO();
	
	public static final String FEEDBACK_NOT_DONE = "1";// tao moi
	public static final String FEEDBACK_DONE = "2";// da thuc hien
	public static final String FEEDBACK_APPROVED = "3";// da duyet
	
	public FeedBackDTO() {
		super(TableType.FEEDBACK_TABLE);
	}

	protected FeedBackDTO(Parcel in) {
		feedBackId = in.readLong();
		staffId = in.readInt();
		customerId = in.readString();
		shopId = in.readString();
		trainingPlanDetailId = in.readString();
		customerCode = in.readString();
		customerName = in.readString();
		status = in.readInt();
		content = in.readString();
		updateUser = in.readString();
		apParamName = in.readString();
		createDate = in.readString();
		updateDate = in.readString();
		doneDate = in.readString();
		numReturn = in.readString();
		remindDate = in.readString();
		type = in.readInt();
		type_ap = in.readString();
		isDeleted = in.readInt();
		createUserId = in.readLong();
		houseNumber = in.readString();
		street = in.readString();
		address = in.readString();
	}

	public static final Creator<FeedBackDTO> CREATOR = new Creator<FeedBackDTO>() {
		@Override
		public FeedBackDTO createFromParcel(Parcel in) {
			return new FeedBackDTO(in);
		}

		@Override
		public FeedBackDTO[] newArray(int size) {
			return new FeedBackDTO[size];
		}
	};

	/**
	 * 
	 * init data feedback
	 * 
	 * @author: YenNTH
	 * @param c
	 * @return: void
	 * @throws:
	 */
	public void initDataFromCursor(Cursor c) {
		try {
			if (c == null) {
				throw new Exception("Cursor is empty");
			}
			feedBackId = Long.parseLong((StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("FEEDBACK_ID")))) ? "0": c.getString(c.getColumnIndexOrThrow("FEEDBACK_ID")));
			staffId = Long.parseLong((StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("STAFF_ID")))) ? "0" : c.getString(c.getColumnIndexOrThrow("STAFF_ID")));
			type = Integer.parseInt((StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("TYPE")))) ? "0" : c.getString(c.getColumnIndexOrThrow("TYPE")));
			createUserId = c.getLong(c.getColumnIndexOrThrow("CREATE_USER_ID"));
			status = Integer.parseInt((StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("STATUS")))) ? "1": c.getString(c.getColumnIndexOrThrow("STATUS")));
			content = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("CONTENT"))) ? Constants.STR_BLANK : c.getString(c.getColumnIndexOrThrow("CONTENT"));
			apParamName = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("AP_PARAM_NAME"))) ? Constants.STR_BLANK : c.getString(c.getColumnIndexOrThrow("AP_PARAM_NAME"));
			remindDate = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("REMIND_DATE"))) ? Constants.STR_BLANK : c.getString(c.getColumnIndexOrThrow("REMIND_DATE"));
			doneDate = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("DONE_DATE"))) ? Constants.STR_BLANK : c.getString(c.getColumnIndexOrThrow("DONE_DATE"));
			createDate = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("CREATE_DATE"))) ? Constants.STR_BLANK : c.getString(c.getColumnIndexOrThrow("CREATE_DATE"));
			customerId = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("CUSTOMER_ID"))) ? Constants.STR_BLANK : c.getString(c.getColumnIndexOrThrow("CUSTOMER_ID"));
			customerCode = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("CUSTOMER_CODE"))) ? Constants.STR_BLANK : c.getString(c.getColumnIndexOrThrow("CUSTOMER_CODE"));
			customerName = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("CUSTOMER_NAME"))) ? Constants.STR_BLANK : c.getString(c.getColumnIndexOrThrow("CUSTOMER_NAME"));
			houseNumber = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("HOUSE_NUMBER"))) ? Constants.STR_BLANK : c.getString(c.getColumnIndexOrThrow("HOUSE_NUMBER"));
			street = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("STREET"))) ? Constants.STR_BLANK : c.getString(c.getColumnIndexOrThrow("STREET"));
			address = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("ADDRESS"))) ? Constants.STR_BLANK : c.getString(c.getColumnIndexOrThrow("ADDRESS"));
			staffDto.staffCode = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndex("STAFF_CODE"))) ? Constants.STR_BLANK : c.getString(c.getColumnIndex("STAFF_CODE"));
			staffDto.name = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndex("STAFF_NAME"))) ? Constants.STR_BLANK : c.getString(c.getColumnIndex("STAFF_NAME"));
		} catch (Exception e) {
		}
	}

	/**
	 * Phat sinh ra cau lenh sql chuyen len server
	 * 
	 * @author: TamPQ
	 * @return: ArrayList<JSONArray>
	 * @throws:
	 */
	public JSONObject generateFeedbackSql(String remindDate) {
		JSONObject feedbackJson = new JSONObject();
		try {
			feedbackJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			feedbackJson.put(IntentConstants.INTENT_TABLE_NAME, FEED_BACK_TABLE.FEED_BACK_TABLE);

			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.FEEDBACK_ID, feedBackId, null));
			if (!StringUtil.isNullOrEmpty(customerId)) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.CUSTOMER_ID, customerId, null));
			}
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.CONTENT, content, null));
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.STAFF_ID, staffId, null));
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.STATUS, status, null));
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.TYPE, type, null));
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.CREATE_DATE, createDate, null));
			if (!StringUtil.isNullOrEmpty(remindDate)) {// nhac nho
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.REMIND_DATE, remindDate, null));
			}
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.CREATE_USER_ID, String.valueOf(createUserId), null));
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.SHOP_ID, shopId, null));
			feedbackJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
		} catch (JSONException e) {
		}

		return feedbackJson;
	}

	/**
	 * 
	 * generate sql insert to server
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateFeedbackSql() {
		JSONObject feedbackJson = new JSONObject();

		try {
			feedbackJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			feedbackJson.put(IntentConstants.INTENT_TABLE_NAME, FEED_BACK_TABLE.FEED_BACK_TABLE);

			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.FEEDBACK_ID, feedBackId, null));
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.STAFF_ID, staffId, null));
			if (!StringUtil.isNullOrEmpty(customerId)) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.CUSTOMER_ID, customerId, null));
			}
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.STATUS, status, null));
			if (!StringUtil.isNullOrEmpty(content)) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.CONTENT, content, null));
			}
			if (!StringUtil.isNullOrEmpty(createDate)) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.CREATE_DATE, createDate, null));
			}
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.TYPE, type, null));
			if (!StringUtil.isNullOrEmpty(remindDate)) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.REMIND_DATE, remindDate, null));
			}
			if (!StringUtil.isNullOrEmpty(doneDate)) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.DONE_DATE, doneDate, null));
			}
			if (!StringUtil.isNullOrEmpty(shopId)) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.SHOP_ID, shopId, null));
			}
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.CREATE_USER_ID, String.valueOf(createUserId), null));
			if (!StringUtil.isNullOrEmpty(trainingPlanDetailId)) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.TRAINING_PLAN_DETAIL_ID,String.valueOf(trainingPlanDetailId), null));
			}

			feedbackJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
		} catch (JSONException e) {
		}

		return feedbackJson;
	}

	/**
	 * 
	 * gen sql cap nhat feedback
	 * TTTT
	 * @author: YenNTH
	 * @return
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateUpdateFeedbackSql() {
		JSONObject feedbackJson = new JSONObject();
		try {
			feedbackJson.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			feedbackJson.put(IntentConstants.INTENT_TABLE_NAME, FEED_BACK_TABLE.FEED_BACK_TABLE);
			// ds params
			JSONArray detailPara = new JSONArray();
			if (doneDate != null) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.DONE_DATE, doneDate, null));
			}
			if (updateUser != null) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.UPDATE_USER, updateUser, null));
			}
			if (updateDate != null) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.UPDATE_DATE, updateDate, null));
			}
			if (status != 0) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.STATUS, status, null));
			}
			feedbackJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.FEEDBACK_ID, this.feedBackId, null));
			feedbackJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (Exception e) {
		}
		return feedbackJson;
	}

	/**
	 * 
	 * generate sql update feedback status
	 * 
	 * @param feedBackIdValue
	 * @param feedBackStatusValue
	 * @param doneDateValue
	 * @return
	 * @return: JSONObject
	 * @throws:
	 * @author: HaiTC3
	 * @param updateUser 
	 * @param updateDate 
	 * @date: Nov 7, 2012
	 */
	public JSONObject generateUpdateFeedBackSql(String feedBackIdValue, String feedBackStatusValue, String doneDateValue, String updateDate, String updateUser) {
		JSONObject feedbackJson = new JSONObject();
		try {
			feedbackJson.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			feedbackJson.put(IntentConstants.INTENT_TABLE_NAME, FEED_BACK_TABLE.FEED_BACK_TABLE);
			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.DONE_DATE, doneDateValue, null));
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.STATUS, feedBackStatusValue, null));
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.UPDATE_DATE, updateDate, null));
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.UPDATE_USER, updateUser, null));
			feedbackJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.FEEDBACK_ID, feedBackIdValue, null));
			feedbackJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (Exception e) {
		}
		return feedbackJson;
	}

	/**
	 * 
	 * generalte sql update content for feedback
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateUpdateContentFeedbackSql() {
		JSONObject feedbackJson = new JSONObject();
		try {
			feedbackJson.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			feedbackJson.put(IntentConstants.INTENT_TABLE_NAME, FEED_BACK_TABLE.FEED_BACK_TABLE);
			// ds params
			JSONArray detailPara = new JSONArray();
			if (!StringUtil.isNullOrEmpty(updateDate)) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.UPDATE_DATE, updateDate, null));
			}
			if (!StringUtil.isNullOrEmpty(updateUser)) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.UPDATE_USER, updateUser, null));
			}
			if (!StringUtil.isNullOrEmpty(remindDate)) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.REMIND_DATE, remindDate, null));
			}
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.CONTENT, content, null));

			feedbackJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.FEEDBACK_ID, this.feedBackId, null));
			feedbackJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (Exception e) {
		}
		return feedbackJson;
	}

	/**
	 * general sql delete feedback out of db server Mo ta chuc nang cua ham
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generalSqlDeleteFeedBackOutOfDB() {
		JSONObject feedbackJson = new JSONObject();
		try {
			feedbackJson.put(IntentConstants.INTENT_TYPE, TableAction.DELETE);
			feedbackJson.put(IntentConstants.INTENT_TABLE_NAME, FEED_BACK_TABLE.FEED_BACK_TABLE);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.FEEDBACK_ID, String.valueOf(this.feedBackId), null));
			feedbackJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (Exception e) {
		}
		return feedbackJson;
	}

	/**
	 * 
	 * gen sql xoa feedback
	 * NVBH
	 * @author: YenNTH
	 * @param dto
	 * @return
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateDeleteFeedbackSql(FeedBackDTO dto) {
		JSONObject feedbackJson = new JSONObject();
		try {
			feedbackJson.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			feedbackJson.put(IntentConstants.INTENT_TABLE_NAME, FEED_BACK_TABLE.FEED_BACK_TABLE);
			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.STATUS, status, null));
			if (updateDate != null) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.UPDATE_DATE, updateDate, null));
			}
			if (updateUser != null) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.UPDATE_USER, updateUser, null));
			}
			feedbackJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.FEEDBACK_ID, this.feedBackId, null));
			feedbackJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (Exception e) {
		}
		return feedbackJson;
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
	public void initDataWithCursor(Cursor c) {
		if (c.getColumnIndex(FEED_BACK_TABLE.FEEDBACK_ID) >= 0) {
			feedBackId = c.getLong(c.getColumnIndex(FEED_BACK_TABLE.FEEDBACK_ID));
		} else {
			feedBackId = -1;
		}
		if (c.getColumnIndex(FEED_BACK_TABLE.STAFF_ID) >= 0) {
			staffId = c.getInt(c.getColumnIndex(FEED_BACK_TABLE.STAFF_ID));
		} else {
			staffId = -1;
		}
		if (c.getColumnIndex(FEED_BACK_TABLE.CUSTOMER_ID) >= 0) {
			customerId = c.getString(c.getColumnIndex(FEED_BACK_TABLE.CUSTOMER_ID));
		} else {
			customerId = Constants.STR_BLANK;
		}
		if (c.getColumnIndex(FEED_BACK_TABLE.STATUS) >= 0) {
			status = c.getInt(c.getColumnIndex(FEED_BACK_TABLE.STATUS));
		} else {
			status = -1;
		}
		if (c.getColumnIndex(FEED_BACK_TABLE.CONTENT) >= 0) {
			content = c.getString(c.getColumnIndex(FEED_BACK_TABLE.CONTENT));
		} else {
			content = Constants.STR_BLANK;
		}
		if (c.getColumnIndex(FEED_BACK_TABLE.SHOP_ID) >= 0) {
			shopId = c.getString(c.getColumnIndex(FEED_BACK_TABLE.SHOP_ID));
		} else {
			shopId = Constants.STR_BLANK;
		}
		if (c.getColumnIndex(FEED_BACK_TABLE.UPDATE_USER) >= 0) {
			updateUser = c.getString(c.getColumnIndex(FEED_BACK_TABLE.UPDATE_USER));
		} else {
			updateUser = Constants.STR_BLANK;
		}
		if (c.getColumnIndex(FEED_BACK_TABLE.CREATE_DATE) >= 0) {

			createDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex("CREATE_DATE")));

		} else {
			createDate = Constants.STR_BLANK;
		}
		if (c.getColumnIndex(FEED_BACK_TABLE.UPDATE_DATE) >= 0) {

			updateDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex("UPDATE_DATE")));

		} else {
			updateDate = Constants.STR_BLANK;
		}
		if (c.getColumnIndex(FEED_BACK_TABLE.DONE_DATE) >= 0) {

			doneDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex("DONE_DATE")));

		} else {
			doneDate = Constants.STR_BLANK;
		}
		if (c.getColumnIndex(FEED_BACK_TABLE.REMIND_DATE) >= 0) {

			remindDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex("REMIND_DATE")));

		} else {
			remindDate = Constants.STR_BLANK;
		}
		if (c.getColumnIndex(FEED_BACK_TABLE.TYPE) >= 0) {
			type = c.getInt(c.getColumnIndex(FEED_BACK_TABLE.TYPE));
		} else {
			type = -1;
		}
		if (c.getColumnIndex(FEED_BACK_TABLE.CREATE_USER_ID) >= 0) {
			createUserId = c.getLong(c.getColumnIndex(FEED_BACK_TABLE.CREATE_USER_ID));
		} else {
			createUserId = -1;
		}
	}

	public String getTypeTitle(int type) {
		String title = "";
		if (type == FeedBackDTO.FEEDBACK_TYPE_SERVICE) {
			title = "Dịch vụ";
		} else if (type == FeedBackDTO.FEEDBACK_TYPE_PRODUCT) {
			title = "Sản phẩm";
		} else if (type == FeedBackDTO.FEEDBACK_TYPE_PROGRAME_HTTM) {
			title = "Chương trình HTTM";
		} else if (type == FeedBackDTO.FEEDBACK_TYPE_DISPLAY) {
			title = "Cải thiện về trưng bày";
		} else if (type == FeedBackDTO.FEEDBACK_TYPE_SHELF) {
			title = "Tủ";
		} else if (type == FeedBackDTO.FEEDBACK_TYPE_CTTB) {
			title = "CTTB";
		} else if (type == FeedBackDTO.FEEDBACK_TYPE_SUPPORT_NVBH) {
			title = "Hỗ trợ NVBH";
		} else if (type == FeedBackDTO.FEEDBACK_TYPE_AMOUNT) {
			title = "Tích luỹ doanh số";
		}
		return title;
	}

	/**
	 * 
	 * create object with params
	 * 
	 * @author: HaiTC3
	 * @param feedabackId
	 * @param staffId
	 * @param customerId
	 * @param status
	 * @param desc
	 * @param userUpdate
	 * @param createDate
	 * @param updateDate
	 * @param type
	 * @param parentStaffId
	 * @return: void
	 * @throws:
	 */
	public void initDateForTrainingResult(long feedabackId, long staffId, String customerId, int status, String desc,
			String userUpdate, String createDate, String updateDate, int type, long parentStaffId, String remainDate,
			String shopId, String trainpdId) {
		this.feedBackId = feedabackId;
		this.staffId = staffId;
		this.customerId = customerId;
		this.status = status;
		this.content = desc;
		this.createDate = createDate;
		this.type = type;
		this.trainingPlanDetailId = trainpdId;
		// not have is_send and remind date, done_date, is_delete
		this.createUserId = parentStaffId;
		this.remindDate = remainDate;
		this.shopId = shopId;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeLong(feedBackId);
		parcel.writeLong(staffId);
		parcel.writeString(customerId);
		parcel.writeString(shopId);
		parcel.writeString(trainingPlanDetailId);
		parcel.writeString(customerCode);
		parcel.writeString(customerName);
		parcel.writeInt(status);
		parcel.writeString(content);
		parcel.writeString(updateUser);
		parcel.writeString(apParamName);
		parcel.writeString(createDate);
		parcel.writeString(updateDate);
		parcel.writeString(doneDate);
		parcel.writeString(numReturn);
		parcel.writeString(remindDate);
		parcel.writeInt(type);
		parcel.writeString(type_ap);
		parcel.writeInt(isDeleted);
		parcel.writeLong(createUserId);
		parcel.writeString(houseNumber);
		parcel.writeString(street);
		parcel.writeString(address);
	}
}
