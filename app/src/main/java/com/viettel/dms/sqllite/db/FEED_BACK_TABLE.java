/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.dto.db.FeedBackDetailDTO;
import com.viettel.dms.dto.db.FeedBackTBHVDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.DisplayProgrameItemDTO;
import com.viettel.dms.dto.view.FollowProblemDTO;
import com.viettel.dms.dto.view.FollowProblemItemDTO;
import com.viettel.dms.dto.view.ListNoteInfoViewDTO;
import com.viettel.dms.dto.view.NoteInfoDTO;
import com.viettel.dms.dto.view.ReviewsObjectDTO;
import com.viettel.dms.dto.view.ReviewsStaffViewDTO;
import com.viettel.dms.dto.view.SuperviorTrackAndFixProblemOfGSNPPViewDTO;
import com.viettel.dms.dto.view.SupervisorProblemOfGSNPPDTO;
import com.viettel.dms.dto.view.TBHVFollowProblemItemDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.sale.customer.CustomerFeedBackDto;
import com.viettel.dms.view.sale.customer.CustomerFeedBackListView;
import com.viettel.dms.view.sale.customer.PostFeedbackView;
import com.viettel.dms.view.sale.statistic.NoteListView;
import com.viettel.dms.view.tnpg.followproblem.TTTTFollowProblemView;
import com.viettel.utils.VTLog;

/**
 * Luu thong tin phan anh
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class FEED_BACK_TABLE extends ABSTRACT_TABLE {
	// id bang
	public static final String FEEDBACK_ID = "FEEDBACK_ID";
	// id NVBH
	public static final String STAFF_ID = "STAFF_ID";
	// id KH
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	// id SHOP
	public static final String SHOP_ID = "SHOP_ID";
	// training plan detail id
	public static final String TRAINING_PLAN_DETAIL_ID = "TRAINING_PLAN_DETAIL_ID";
	// trang thai: 1: tiep nhan, 2: da xu ly
	public static final String STATUS = "STATUS";
	// noi dung
	public static final String CONTENT = "CONTENT";
	// loai phan anh
	public static final String TYPE = "TYPE";
	// nguoi nhac nho
	public static final String REMIND_DATE = "REMIND_DATE";
	// ngay thuc hien
	public static final String DONE_DATE = "DONE_DATE";
	// num return
	public static final String NUM_RETURN = "NUM_RETURN";
	// parent staff id
	public static final String CREATE_USER_ID = "CREATE_USER_ID";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// nguoi update
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";

	public static final String FEED_BACK_TABLE = "FEEDBACK";

	public FEED_BACK_TABLE(SQLiteDatabase mDB) {
		this.tableName = FEED_BACK_TABLE;
		this.columns = new String[] { FEEDBACK_ID, STAFF_ID, CUSTOMER_ID,
				STATUS, DONE_DATE, REMIND_DATE, UPDATE_USER, CREATE_DATE,
				UPDATE_DATE, TYPE, CREATE_USER_ID, SYN_STATE };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		sqlInsert+=tableName+" ( ";
		sbInsert= new StringBuffer(sqlInsert);
		sbParams= new StringBuffer(" ( ");
		arrParams= new ArrayList<String>();
		this.mDB = mDB;

	}

	/**
	 * Them 1 dong xuong db
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	@Override
	public long insert(AbstractTableDTO dto) {
		ContentValues value = initDataRow((FeedBackDTO) dto);
		return insert(null, value);
	}

	/**
	 * 
	 * them 1 dong xuong CSDL
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	public long insert(FeedBackDTO dto) {
		ContentValues value = initDataRow(dto);
		return insert(null, value);
	}

	/**
	 * Update 1 dong xuong db
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	@Override
	public long update(AbstractTableDTO dto) {
		FeedBackDTO dtoFeedBack = (FeedBackDTO) dto;
		ContentValues value = initDataRow(dtoFeedBack);
		String[] params = { "" + dtoFeedBack.feedBackId };
		return update(value, FEEDBACK_ID + " = ?", params);
	}

	/**
	 * 
	 * cap nhat noi dung cho feedback
	 * 
	 * @author: HaiTC3
	 * @param dto
	 * @return
	 * @return: long
	 * @throws:
	 */
	public long updateContentFeedBack(FeedBackDTO dto) {
		ContentValues value = initDateRowUpdateFeedBackContent(dto);
		String[] params = { String.valueOf(dto.feedBackId) };
		return update(value, FEEDBACK_ID + " = ?", params);
	}

	/**
	 * 
	 * update status feedback dto in DB at table FEEDBACK
	 * 
	 * @author: HaiTC3
	 * @param dto
	 * @return
	 * @return: long
	 * @throws:
	 */
	public long updateFeedBackStatus(FeedBackDTO dto) {
		try {
			ContentValues value = initDateRowUpdateFeedBackStatus(dto);
			String[] params = { String.valueOf(dto.feedBackId) };
			return update(value, FEEDBACK_ID + " = ?", params);
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * 
	 * update feedback status from feedbackId, status, doneDate
	 * 
	 * @param status
	 * @param doneDate
	 * @param feedbackId
	 * @return
	 * @return: long
	 * @throws:
	 * @author: HaiTC3
	 * @param updateUser 
	 * @param updateDate 
	 * @date: Nov 7, 2012
	 */
	public long updateFeedBackStatus(String status, String doneDate,
			String feedbackId, String updateDate, String updateUser) {
		try {
			ContentValues value = initdateUpdateFeedBackStatus(status, doneDate, updateDate,updateUser);
			String[] params = { feedbackId };
			return update(value, FEEDBACK_ID + " = ?", params);
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Xoa 1 dong cua CSDL
	 * @author: TruongHN
	 * @return: int
	 * @throws:
	 */
	public int delete(String code) {
		String[] params = { code };
		return delete(FEEDBACK_ID + " = ?", params);
	}

	public long delete(AbstractTableDTO dto) {
		FeedBackDTO dtoFeedBack = (FeedBackDTO) dto;
		String[] params = { "" + dtoFeedBack.feedBackId };
		return delete(FEEDBACK_ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: DoanDM replaced
	 * @param id
	 * @return: FeedBackDTO
	 * @throws:
	 */
	public FeedBackDTO getRowById(String id) {
		FeedBackDTO dto = null;
		Cursor c = null;
		try {
			String[] params = { id };
			c = query(FEEDBACK_ID + " = ?", params, null, null, null);
			if (c != null) {
				if (c.moveToFirst()) {
					dto = initDTOFromCursor(c);
				}
			}
		} catch (Exception ex) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return dto;
	}

	private FeedBackDTO initDTOFromCursor(Cursor c) {
		FeedBackDTO dto = new FeedBackDTO();
		dto.feedBackId = (c.getLong(c.getColumnIndex(FEEDBACK_ID)));
		dto.staffId = (c.getInt(c.getColumnIndex(STAFF_ID)));
		dto.customerId = (c.getString(c.getColumnIndex(CUSTOMER_ID)));
		dto.status = (c.getInt(c.getColumnIndex(STATUS)));
		dto.content = (c.getString(c.getColumnIndex("DESCR")));
		dto.updateUser = (c.getString(c.getColumnIndex(UPDATE_USER)));
		dto.remindDate = (c.getString(c.getColumnIndex(REMIND_DATE)));
		dto.doneDate = (c.getString(c.getColumnIndex(DONE_DATE)));
		dto.createDate = (c.getString(c.getColumnIndex(CREATE_DATE)));
		dto.updateDate = (c.getString(c.getColumnIndex(UPDATE_DATE)));
		dto.type = (c.getInt(c.getColumnIndex(TYPE)));
		dto.createUserId = (c.getLong(c.getColumnIndex(CREATE_USER_ID)));
		return dto;
	}

	/**
	 * lay tat ca cac dong cua CSDL
	 * 
	 * @author: TruongHN
	 * @return: Vector<FeedBackDTO>
	 * @throws:
	 */
	public Vector<FeedBackDTO> getAllRow() {
		Vector<FeedBackDTO> v = new Vector<FeedBackDTO>();
		Cursor c = null;
		try {
			c = query(null, null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			FeedBackDTO dto;
			if (c.moveToFirst()) {
				do {
					dto = initDTOFromCursor(c);
					v.addElement(dto);
				} while (c.moveToNext());
			}
			c.close();
		}
		return v;

	}

	/**
	 * 
	 * init row insert feedback
	 * 
	 * @author: YenNTH
	 * @param dto
	 * @return
	 * @return: ContentValues
	 * @throws:
	 */
	private ContentValues initDataRow(FeedBackDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(FEEDBACK_ID, dto.feedBackId);
		editedValues.put(STAFF_ID, dto.staffId);
		editedValues.put(CUSTOMER_ID, dto.customerId);
		editedValues.put(SHOP_ID, dto.shopId);
		editedValues.put(TRAINING_PLAN_DETAIL_ID, dto.trainingPlanDetailId);
		editedValues.put(STATUS, dto.status);
		editedValues.put(CONTENT, dto.content);
		editedValues.put(TYPE, dto.type);
		editedValues.put(REMIND_DATE, dto.remindDate);
		editedValues.put(DONE_DATE, dto.doneDate);
		editedValues.put(NUM_RETURN, dto.numReturn);
		editedValues.put(CREATE_USER_ID, dto.createUserId);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(UPDATE_USER, dto.updateUser);
		editedValues.put(UPDATE_DATE, dto.updateDate);

		// editedValues.put(DESCR, dto.descr);
		// editedValues.put(IS_SEND, dto.isSend);
		// editedValues.put(IS_DELETED, dto.isDeleted);
		return editedValues;
	}

	/**
	 * 
	 * update feedback date row at column "STATUS"
	 * 
	 * @author: HaiTC3
	 * @param dto
	 * @return
	 * @return: ContentValues
	 * @throws:
	 */
	private ContentValues initDateRowUpdateFeedBackStatus(FeedBackDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(STATUS, dto.status);
		editedValues.put(DONE_DATE, dto.doneDate);
		return editedValues;
	}

	/**
	 * 
	 * update feedback status from status and doneDate
	 * 
	 * @param status
	 * @param doneDate
	 * @return
	 * @return: ContentValues
	 * @throws:
	 * @author: HaiTC3
	 * @param updateUser 
	 * @param updateDate 
	 * @date: Nov 7, 2012
	 */
	private ContentValues initdateUpdateFeedBackStatus(String status,
			String doneDate, String updateDate, String updateUser) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(STATUS, status);
		editedValues.put(DONE_DATE, doneDate);
		editedValues.put(UPDATE_DATE, updateDate);
		editedValues.put(UPDATE_USER, updateUser);
		return editedValues;
	}

	/**
	 * 
	 * tao du lieu update content cho feedaback
	 * 
	 * @author: HaiTC3
	 * @param dto
	 * @return
	 * @return: ContentValues
	 * @throws:
	 */
	private ContentValues initDateRowUpdateFeedBackContent(FeedBackDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(CONTENT, dto.content);
		editedValues.put(UPDATE_USER, dto.updateUser);
		editedValues.put(UPDATE_DATE, dto.updateDate);
		editedValues.put(REMIND_DATE, dto.remindDate);
		return editedValues;
	}

	/**
	 * 
	 * ds theo doi khac phuc NVBH, TTTT
	 * @author: YenNTH
	 * @param staffId
	 * @param customerId
	 * @param type
	 * @param status
	 * @param doneDate
	 * @param page
	 * @param sender
	 * @return
	 * @return: CustomerFeedBackDto
	 * @throws:
	 */
	public CustomerFeedBackDto getFeedBackList(String staffId,
			String customerId, String type, String status, String doneDate,
			int page, boolean getTotalPage, BaseFragment sender, int from) {
		CustomerFeedBackDto dto = null;
		dto = new CustomerFeedBackDto();
		ArrayList<String> staffList = null;
		String staffStr = Constants.STR_BLANK;
		STAFF_TABLE st= new STAFF_TABLE();
			if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSNPP) {
				staffList = st.getStaffRecursiveReverseTBHV(staffId);
				staffStr = TextUtils.join(",", staffList);
			}
			
		StringBuffer var1 = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();
		var1.append("SELECT FB.FEEDBACK_ID                       as FEEDBACK_ID, ");
		var1.append("       FB.STAFF_ID                          as STAFF_ID, ");
		var1.append("       FB.CUSTOMER_ID                       as CUSTOMER_ID, ");
		var1.append("       CT.CUSTOMER_CODE                     as CUSTOMER_CODE, ");
		var1.append("       CT.CUSTOMER_NAME                     as CUSTOMER_NAME, ");
		var1.append("       CT.STREET                     		 as STREET, ");
		var1.append("       CT.HOUSENUMBER                       as HOUSE_NUMBER, ");
		var1.append("       CT.ADDRESS                           as ADDRESS, ");
		var1.append("       FB.STATUS                            as STATUS, ");
		var1.append("       FB.CONTENT                           as CONTENT, ");
		var1.append("       FB.TYPE                              as TYPE, ");
		var1.append("       FB.create_user_id                    as CREATE_USER_ID, ");
		var1.append("       AP.AP_PARAM_NAME                     as AP_PARAM_NAME, ");
		var1.append("       s.STAFF_CODE                         as STAFF_CODE, ");
		var1.append("       s.STAFF_NAME                         as STAFF_NAME, ");
		var1.append("       Strftime('%d/%m/%Y', FB.CREATE_DATE) as CREATE_DATE, ");
		var1.append("       Strftime('%d/%m/%Y', FB.REMIND_DATE) as REMIND_DATE, ");
		var1.append("       Strftime('%d/%m/%Y', FB.DONE_DATE)   as DONE_DATE, ");
		var1.append("       Strftime('%d/%m/%Y', FB.UPDATE_DATE) as UPDATE_DATE, ");
		var1.append("       Strftime('%d/%m/%Y', FB.UPDATE_USER) as USER_UPDATE ");
		var1.append("FROM   AP_PARAM AP, FEEDBACK FB left join CUSTOMER CT on FB.CUSTOMER_ID = CT.CUSTOMER_ID, STAFF s  ");
		var1.append("WHERE  1 = 1 ");
		var1.append("       AND FB.STAFF_ID=  s.STAFF_ID ");
		if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSNPP && sender instanceof CustomerFeedBackListView){
			var1.append("       AND FB.staff_id in ("+ staffStr + ") ");
		}else {
			var1.append("       AND FB.STAFF_ID = ? ");
			param.add(staffId);
		}
		if(from == PostFeedbackView.FROM_TTTT_POST_FEEDBACK_VIEW){
			var1.append("       and (AP.TYPE like ('FEEDBACK_TYPE_TTTT') )");
		}else{
			var1.append("       and (AP.TYPE like ('FEEDBACK_TYPE_NVBH') or AP.TYPE like ('FEEDBACK_TYPE_GSNPP') )");
		}
		var1.append("       and FB.TYPE = AP.AP_PARAM_CODE");
		if (!StringUtil.isNullOrEmpty(type)) {
			var1.append(" AND AP.AP_PARAM_CODE = ?");
			param.add(type);
		}
		var1.append("       and FB.STATUS <> 0 ");
		var1.append("       and AP.STATUS = 1 ");
		var1.append("       and Date(FB.CREATE_DATE) <= Date(?) ");
		param.add(DateUtils.now());
		if (sender instanceof NoteListView) {
			if (!StringUtil.isNullOrEmpty(status)) {
				var1.append("       and FB.STATUS = ? ");
				param.add(status);
			}
			if (!StringUtil.isNullOrEmpty(doneDate) && doneDate.equals("0")) {
				var1.append("       and FB.DONE_DATE is null ");
			}
		} else if (sender instanceof CustomerFeedBackListView) {
			var1.append("       and FB.CUSTOMER_ID = ? ");
			param.add(customerId);
		}else if (sender instanceof TTTTFollowProblemView) {
			if (!StringUtil.isNullOrEmpty(status)) {
				var1.append("       and FB.STATUS = ? ");
				param.add(status);
			}
			if (!StringUtil.isNullOrEmpty(doneDate) && doneDate.equals("0")) {
				var1.append("       and FB.DONE_DATE is null ");
			}
		}
		var1.append("		ORDER BY   FB.STATUS asc, ");
		var1.append("          			datetime(FB.REMIND_DATE), ");
		var1.append("          			datetime(FB.DONE_DATE) desc, ");
		var1.append("          			datetime(FB.CREATE_DATE) desc, ");
		var1.append("          			CT.CUSTOMER_CODE, ");
		var1.append("          			CT.CUSTOMER_NAME ");
		// get count
		StringBuffer totalCount = new StringBuffer();

		Cursor c_totalRow = null;
		try {
			if (getTotalPage) {
				totalCount.append("select count(*) as TOTAL_ROW from (" + var1
						+ ")");
				c_totalRow = rawQueries(totalCount.toString(), param);
				if (c_totalRow != null) {
					if (c_totalRow.moveToFirst()) {
						dto.totalFeedBack = c_totalRow.getInt(0);
					}
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (c_totalRow != null) {
				c_totalRow.close();
			}
		}
		Cursor c = null;
		if (page > 0) {
			var1.append("       limit ? offset ?");
			param.add(Constants.STR_BLANK + Constants.NUM_ITEM_PER_PAGE);
			param.add(Constants.STR_BLANK + (page - 1)
					* Constants.NUM_ITEM_PER_PAGE);
		}
		try {
			c = rawQueries(var1.toString(), param);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						FeedBackDTO item = new FeedBackDTO();
						item.initDataFromCursor(c);
						dto.arrItem.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return dto;
	}

	/**
	 * 
	 * update note info
	 * 
	 * @author: HaiTC3
	 * @param staffId
	 * @param shopId
	 * @param noteUpdate
	 * @return
	 * @return: ListNoteInfoViewDTO
	 * @throws:
	 */
	public ListNoteInfoViewDTO updateNoteInfo(String staffId, String shopId,
			NoteInfoDTO noteUpdate) {
		ListNoteInfoViewDTO listNote = new ListNoteInfoViewDTO();
		this.updateFeedBackStatus(noteUpdate.feedBack);
		listNote = this.getListNote(staffId, shopId, " limit 0,2");
		return listNote;
	}

	/**
	 * 
	 * lay danh sach ghi chu cua NVBH doi voi cac user
	 * 
	 * @author: HaiTC3
	 * @param staff_id
	 * @param shop_id
	 * @return
	 * @return: ListNoteInfoViewDTO
	 * @throws:
	 */
	public ListNoteInfoViewDTO getListNote(String staff_id, String shop_id,
			String ext) {
		ListNoteInfoViewDTO DTO = new ListNoteInfoViewDTO();
		StringBuffer requestGetNoteList = new StringBuffer();
		requestGetNoteList.append("SELECT FB.feedback_id, ");
		requestGetNoteList.append("       FB.descr, ");
		requestGetNoteList.append("       FB.remind_date, ");
		requestGetNoteList.append("       FB.done_date, ");
		requestGetNoteList.append("       FB.status, ");
		requestGetNoteList
				.append("       Strftime('%d/%m/%Y', FB.create_date) AS CREATE_DATE, ");
		requestGetNoteList
				.append("       FB.type                              AS TYPE, ");
		requestGetNoteList
				.append("       FB.create_user_id                    AS CREATE_USER_ID, ");
		requestGetNoteList.append("       CT.customer_id, ");
		requestGetNoteList.append("       CT.customer_code, ");
		requestGetNoteList.append("       CT.customer_name ");
		requestGetNoteList.append("FROM   feedback AS FB, ");
		requestGetNoteList.append("       customer AS CT ");
		requestGetNoteList.append("WHERE  FB.customer_id = CT.customer_id ");
		requestGetNoteList.append("       AND FB.staff_id = ? ");
		requestGetNoteList.append("       AND CT.shop_id = ? ");
		requestGetNoteList.append("       AND FB.is_deleted = 0 ");
		requestGetNoteList.append("       AND CT.STATUS = 1 ");
		requestGetNoteList.append("       AND FB.status = 1 ");
		requestGetNoteList.append("ORDER  BY FB.create_date DESC ");

		Cursor cTmp = null;
		String getCountNoteList = " SELECT COUNT(*) AS TOTAL_ROW FROM ("
				+ requestGetNoteList.toString() + ") ";
		String[] params = new String[] { staff_id, shop_id };

		try {
			cTmp = rawQuery(getCountNoteList, params);
			int total = 0;
			if (cTmp != null) {
				cTmp.moveToFirst();
				total = cTmp.getInt(0);
				DTO.setNoteNumber(total);
			}
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			if (cTmp != null) {
				cTmp.close();
			}
		}
		Cursor c = null;
		try {
			c = rawQuery(requestGetNoteList.toString() + ext, params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						NoteInfoDTO note = new NoteInfoDTO();
						note.initDateWithCursor(c);
						DTO.getListNote().add(note);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return DTO;
	}

	/**
	 * 
	 * cap nhat thuc hien van de
	 * TTTT, NVBH
	 * @author: YenNTH
	 * @param dto
	 * @return
	 * @return: long
	 * @throws:
	 */
	public long updateDoneDateFeedBack(FeedBackDTO dto) {
		long returnCode = -1;
		if (AbstractTableDTO.TableType.FEEDBACK_TABLE.equals(dto.getType())) {
			try {
				ContentValues editedValues = new ContentValues();
				editedValues.put(DONE_DATE, dto.doneDate);
				editedValues.put(STATUS, dto.status);
				editedValues.put(UPDATE_DATE, dto.updateDate);
				editedValues.put(UPDATE_USER, dto.updateUser);
				String[] params = { "" + dto.feedBackId };
				returnCode = update(editedValues, FEEDBACK_ID + " = ?", params);
			} catch (Exception e) {
			}
		}
		return returnCode;
	}

	/**
	 * 
	 * cap nhat feedback
	 * TTTT, NVBH
	 * @author: YenNTH
	 * @param dto
	 * @return
	 * @return: long
	 * @throws:
	 */
	public long updateDeleteFeedBack(FeedBackDTO dto) {
		long returnCode = -1;
		if (AbstractTableDTO.TableType.FEEDBACK_TABLE.equals(dto.getType())) {
			try {
				ContentValues editedValues = new ContentValues();
				editedValues.put(STATUS, dto.status);
				if (dto.updateDate != null) {
					editedValues.put(UPDATE_DATE, dto.updateDate);
				}
				if (dto.updateUser != null) {
					editedValues.put(UPDATE_USER, dto.updateUser);
				}
				String[] params = { Constants.STR_BLANK + dto.feedBackId };
				returnCode = update(editedValues, FEEDBACK_ID + " = ?", params);
			} catch (Exception e) {
			}
		}
		return returnCode;
	}

	/**
	 * 
	 * Thuc hien cap nhat van de cua nvbh (thuc hien boi GSNPP)
	 * 
	 * @author: ThanhNN8
	 * @param dto
	 * @return
	 * @return: long
	 * @throws:
	 */
	public long updateGSNPPFollowProblemDone(FollowProblemItemDTO dto) {
		long returnCode = -1;
		try {
			ContentValues editedValues = new ContentValues();
			editedValues.put(STATUS, dto.status);
			// neu la yeu cau lam lai thi update lai so lan yeu cau
			if (dto.status == FeedBackDTO.FEEDBACK_STATUS_CREATE) {
				editedValues.put(NUM_RETURN, dto.numReturn);
				editedValues.put(DONE_DATE, dto.doneDate);
			}
			editedValues.put(UPDATE_USER, dto.updateUser);
			editedValues.put(UPDATE_DATE, dto.updateDate);
			String[] params = { "" + dto.feedBackId };
			returnCode = update(editedValues, FEEDBACK_ID + " = ?", params);
		} catch (Exception e) {
		}
		return returnCode;
	}

	/**
	 * update van de cua TBHV
	 * 
	 * @author YenNTH
	 * @param dto
	 * @return
	 */
	public long updateTBHVFollowProblemDone(TBHVFollowProblemItemDTO dto) {
		long returnCode = -1;
		try {
			ContentValues editedValues = new ContentValues();
			editedValues.put(STATUS, dto.status);
			editedValues.put(UPDATE_USER, dto.updateUser);
			editedValues.put(UPDATE_DATE, dto.updateDate);
			if(!StringUtil.isNullOrEmpty(dto.doneDate)){
				editedValues.put(DONE_DATE, dto.doneDate);
			}
			editedValues.put(NUM_RETURN, dto.numReturn);
			String[] params = { Constants.STR_BLANK + dto.id };
			returnCode = update(editedValues, FEEDBACK_ID + " = ?", params);
		} catch (Exception e) {
		}
		return returnCode;
	}

	/**
	 * delete van de cua TBHV
	 * 
	 * @author YenNTH
	 * @param dto
	 * @return
	 */
	public long deleteTBHVFollowProblemDone(TBHVFollowProblemItemDTO dto) {
		long returnCode = -1;
		try {
			ContentValues editedValues = new ContentValues();
			editedValues.put(STATUS, dto.status);
			if (dto.updateDate != null) {
				editedValues.put(UPDATE_DATE, dto.updateDate);
			}
			if (dto.updateUser != null) {
				editedValues.put(UPDATE_USER, dto.updateUser);
			}
			String[] params = { "" + dto.id };
			returnCode = delete(FEEDBACK_ID + " = ?", params);
		} catch (Exception e) {
		}
		return returnCode;
	}

	/**
	 * 
	 * danh sach theo doi khac phuc cua GSNPP
	 * 
	 * @author: YenNTH
	 * @param data
	 * @return
	 * @return: FollowProblemDTO
	 * @throws:
	 */
	public FollowProblemDTO getListProblemOfSuperVisor(Bundle data) {
		String extPage = data.getString(IntentConstants.INTENT_PAGE);
		// chua su dung id gsnpp
		long superStaffId = data.getLong(IntentConstants.INTENT_STAFF_ID);
		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID_PARA);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		String customerCode = data.getString(IntentConstants.INTENT_CUSTOMER_CODE);
		String status = data.getString(IntentConstants.INTENT_STATE);
		String typeProblem = data.getString(IntentConstants.INTENT_TYPE_PROBLEM);
		String fromDate = data.getString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE);
		String toDate = data.getString(IntentConstants.INTENT_FIND_ORDER_TO_DATE);
		boolean checkPagging = data.getBoolean(IntentConstants.INTENT_CHECK_PAGGING, false);

		FollowProblemDTO result = new FollowProblemDTO();
		List<String> stringParams = new ArrayList<String>();
		stringParams.add(shopId);
		stringParams.add(Constants.STR_BLANK + superStaffId);

		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("SELECT s.staff_id                           as STAFF_ID, ");
		stringbuilder.append("       s.staff_code                         as STAFF_CODE, ");
		stringbuilder.append("       s.staff_name                         as STAFF_NAME, ");
		stringbuilder.append("       c.customer_id                        as CUSTOMER_ID, ");
		stringbuilder.append("       SUBSTR(c.customer_code, 1, 3)        as CUSTOMER_CODE, ");
		stringbuilder.append("       c.customer_name                      as CUSTOMER_NAME, ");
		stringbuilder.append("       c.address                            as ADDRESS, ");
		stringbuilder.append("       c.housenumber                        as HOUSENUMBER, ");
		stringbuilder.append("       c.street                             as STREET, ");
		stringbuilder.append("       ap.ap_param_name                     as AP_PARAM_NAME, ");
		stringbuilder.append("       fb.feedback_id                       as FEEDBACK_ID, ");
		stringbuilder.append("       fb.content                           as CONTENT, ");
		stringbuilder.append("       fb.num_return                        as NUM_RETURN, ");
		stringbuilder.append("       fb.status                            as STATUS, ");
		stringbuilder.append("       Strftime('%d/%m/%Y', fb.create_date) as CREATE_DATE, ");
		stringbuilder.append("       Strftime('%d/%m/%Y', fb.done_date)   as DONE_DATE, ");
		stringbuilder.append("       Strftime('%d/%m/%Y', fb.remind_date) as REMIND_DATE ");
		stringbuilder.append("FROM   feedback fb LEFT JOIN staff s ON (fb.staff_id = s.staff_id AND s.shop_id = ? ) ");
		stringbuilder.append("       LEFT JOIN customer c ON (fb.customer_id = c.customer_id), ");
		stringbuilder.append("       ap_param ap ");
		stringbuilder.append("WHERE  fb.type = ap.ap_param_code ");
		stringbuilder.append("       AND ( ap.type LIKE 'FEEDBACK_TYPE_NVBH' ");
		stringbuilder.append("              OR ap.type LIKE 'FEEDBACK_TYPE_GSNPP' ) ");
		stringbuilder.append("       AND s.staff_owner_id = ? ");
		stringbuilder.append("       AND s.status = 1 ");
		stringbuilder.append("       AND ap.status = 1 ");
		if (!StringUtil.isNullOrEmpty(status)) {
			stringbuilder.append("   AND fb.status = ? ");
			stringParams.add(status);
		} else {
			stringbuilder.append("   AND fb.status <> 0 ");
		}
		if (!StringUtil.isNullOrEmpty(fromDate)) {
			stringbuilder.append("   AND substr(fb.create_date,1,10) >= ? ");
			stringParams.add(fromDate);
		}
		if (!StringUtil.isNullOrEmpty(toDate)) {
			stringbuilder.append("   AND substr(fb.create_date,1,10) <= ? ");
			stringParams.add(toDate);
		}
		if (!StringUtil.isNullOrEmpty(staffId)) {
			stringbuilder.append("   AND fb.staff_id = ? ");
			stringParams.add(staffId);
		}
		if (!StringUtil.isNullOrEmpty(customerCode)) {
			stringbuilder.append("   AND Lower(c.customer_code) LIKE ? ");
			stringParams.add("%" + customerCode.toLowerCase() + "%");
		}
		if (!StringUtil.isNullOrEmpty(typeProblem)) {
			stringbuilder.append("   AND fb.type = ? ");
			stringParams.add(typeProblem);
		}
		stringbuilder.append("ORDER  BY status, ");
		stringbuilder.append("          datetime(fb.done_date), ");
		stringbuilder.append("          datetime(fb.remind_date), ");
		stringbuilder.append("          datetime(fb.create_date) DESC, ");
		stringbuilder.append("          staff_name, ");
		stringbuilder.append("          customer_code, ");
		stringbuilder.append("          customer_name ");

		String requestGetFollowProblemList = stringbuilder.toString();
		String[] params = new String[stringParams.size()];
		for (int i = 0, length = stringParams.size(); i < length; i++) {
			params[i] = stringParams.get(i);
		}
		Cursor cTmp = null;
		String getCountFollowProblemList = " SELECT COUNT(*) AS TOTAL_ROW FROM ("+ requestGetFollowProblemList + ") ";
		try {
			if (!checkPagging) {
				cTmp = rawQuery(getCountFollowProblemList, params);
				if (cTmp != null) {
					cTmp.moveToFirst();
					result.total = cTmp.getInt(0);
				}
			}
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (cTmp != null) {
					cTmp.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		Cursor c = null;
		try {
			c = rawQuery(requestGetFollowProblemList + extPage, params);
			if (c != null) {
				if (c.moveToFirst()) {
					List<FollowProblemItemDTO> listFollow = new ArrayList<FollowProblemItemDTO>();
					do {
						FollowProblemItemDTO note = new FollowProblemItemDTO();
						note.initDateWithCursor(c);
						listFollow.add(note);
					} while (c.moveToNext());
					result.list = listFollow;
				} else {
					result.list = new ArrayList<FollowProblemItemDTO>();
				}
			}
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return result;
	}

	/**
	 * 
	 * lay combobox trang thai cho man hinh theo doi khac phuc
	 * 
	 * @author: ThanhNN8
	 * @param
	 * @return
	 * @return: List<ComboBoxDisplayProgrameItemDTO>
	 * @throws:
	 */
	public List<DisplayProgrameItemDTO> getComboboxProblemStatusOfSuperVisor() {
		List<DisplayProgrameItemDTO> result = new ArrayList<DisplayProgrameItemDTO>();
		return result;
	}

	/**
	 * 
	 * get list track and fix problem of gsnpp
	 * 
	 * @param ext
	 * @return
	 * @return: ArrayList<SupervisorProblemOfGSNPPDTO>
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 6, 2012
	 */
	public SuperviorTrackAndFixProblemOfGSNPPViewDTO getListTrackAndFixProblemOfGSNPP(
			Bundle ext) {
			SuperviorTrackAndFixProblemOfGSNPPViewDTO result = new SuperviorTrackAndFixProblemOfGSNPPViewDTO();
			String typeProblem = ext.getString(IntentConstants.INTENT_TYPE_PROBLEM_GSNPP);
			String staffId = ext.getString(IntentConstants.INTENT_STAFF_ID);
			boolean isGetTotalItem = ext.getBoolean(IntentConstants.INTENT_IS_ALL);
			String page = ext.getString(IntentConstants.INTENT_PAGE);
			String fromDate = ext.getString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE);
			String toDate = ext.getString(IntentConstants.INTENT_FIND_ORDER_TO_DATE);
			ArrayList<String> listparam = new ArrayList<String>();
			listparam.add(staffId);

			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append("SELECT FB.feedback_id                       AS FEEDBACK_ID, ");
			sqlQuery.append("       FB.staff_id                          AS STAFF_ID, ");
			sqlQuery.append("       FB.customer_id                       AS CUSTOMER_ID, ");
			sqlQuery.append("       CT.customer_code                     AS CUSTOMER_CODE, ");
			sqlQuery.append("       CT.customer_name                     AS CUSTOMER_NAME, ");
			sqlQuery.append("       CT.street                            AS STREET, ");
			sqlQuery.append("       CT.housenumber                       AS HOUSE_NUMBER, ");
			sqlQuery.append("       FB.status                            AS STATUS, ");
			sqlQuery.append("       FB.content                           AS CONTENT, ");
			sqlQuery.append("       AP.ap_param_name                     AS TYPE, ");
			sqlQuery.append("       FB.status                            AS IS_DELETED, ");
			sqlQuery.append("       Strftime('%d/%m/%Y', FB.create_date) AS CREATE_DATE, ");
			sqlQuery.append("       Strftime('%d/%m/%Y', FB.remind_date) AS REMIND_DATE, ");
			sqlQuery.append("       Strftime('%d/%m/%Y', FB.done_date)   AS DONE_DATE, ");
			sqlQuery.append("       Strftime('%d/%m/%Y', FB.update_date) AS UPDATE_DATE, ");
			sqlQuery.append("       Strftime('%d/%m/%Y', FB.update_user) AS USER_UPDATE ");
			sqlQuery.append("FROM   ap_param AP, ");
			sqlQuery.append("       feedback FB ");
			sqlQuery.append("       LEFT JOIN customer CT ");
			sqlQuery.append("              ON FB.customer_id = CT.customer_id ");
			sqlQuery.append("WHERE       FB.status <> 0 ");
			sqlQuery.append("       AND  FB.staff_id = ?");
			sqlQuery.append("       AND  AP.type LIKE 'FEEDBACK_TYPE_GSNPP' ");
			sqlQuery.append("       AND  substr(FB.create_date,1,10) <= ? ");
			listparam.add(DateUtils.now());
			sqlQuery.append("       AND  FB.type = AP.ap_param_code ");
			sqlQuery.append("       AND  AP.status = 1 ");
			if (!StringUtil.isNullOrEmpty(fromDate)) {
				sqlQuery.append("       AND ifnull(substr(fb.remind_date,1,10) >= ?,1) ");
				listparam.add(fromDate);
			}
			if (!StringUtil.isNullOrEmpty(toDate)) {
				sqlQuery.append("       AND ifnull(substr(fb.remind_date,1,10) <= ?,1) ");
				listparam.add(toDate);
			}

			if (!StringUtil.isNullOrEmpty(typeProblem)) {
				sqlQuery.append("       AND fb.status = ? ");
				listparam.add(typeProblem);
			}

			sqlQuery.append("ORDER  BY FB.status ASC, ");
			sqlQuery.append("          datetime(FB.remind_date), ");
			sqlQuery.append("          datetime(FB.done_date) DESC, ");
			sqlQuery.append("          datetime(FB.create_date) DESC, ");
			sqlQuery.append("          CT.customer_code, ");
			sqlQuery.append("          CT.customer_name ");

			String getCountProblemList = " select count(*) as total_row from ("
					+ sqlQuery.toString() + ") ";

			String params[] = listparam.toArray(new String[listparam.size()]);

			int total = 0;
			
		Cursor cTmp = null;
		try {
			if (isGetTotalItem) {
				cTmp = rawQuery(getCountProblemList, params);
				if (cTmp != null) {
					cTmp.moveToFirst();
					total = cTmp.getInt(0);
					result.totalItem = total;
				}
			}
		} catch (Exception e) {
		} finally {
			try {
				if (cTmp != null) {
					cTmp.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString() + page, params);
			if (c.moveToFirst()) {
				do {
					SupervisorProblemOfGSNPPDTO item = new SupervisorProblemOfGSNPPDTO();
					item.initDataWithCursor(c);
					result.listProblemsOfGSNPP.add(item);
				} while (c.moveToNext());
			}
		} catch (Exception e) {
			result = null;
			// TODO: handle exception
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return result;
	}

	/**
	 * 
	 * get list feedback reviews of tbhv
	 * 
	 * @param data
	 * @return
	 * @return: List<FeedBackTBHVDTO>
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 8, 2012
	 */
	public List<FeedBackTBHVDTO> getListReviewsOfTBHV(Bundle data) {
		List<FeedBackTBHVDTO> listReviews = new ArrayList<FeedBackTBHVDTO>();
		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID);
		String createUserStaffId = data
				.getString(IntentConstants.INTENT_CREATE_USER_STAFF_ID);

		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT FEEDBACK_ID, STAFF_ID, CUSTOMER_ID, STATUS, CONTENT, UPDATE_USER, CREATE_DATE, UPDATE_DATE, TYPE, Strftime('%d/%m/%Y',REMIND_DATE) REMIND_DATE, DONE_DATE, CREATE_USER_ID ");
		sqlQuery.append("FROM   feedback ");
		sqlQuery.append("WHERE  1 = 1 ");
		sqlQuery.append("       AND staff_id = ? ");
		sqlQuery.append("       AND create_user_id = ? ");
		sqlQuery.append("       AND Date(create_date) = Date(?) ");

		Cursor c = null;
		String[] paramsList = new String[] { staffId, createUserStaffId, DateUtils.now() };

		try {
			c = this.rawQuery(sqlQuery.toString(), paramsList);
			if (c != null && c.moveToFirst()) {
				do {
					FeedBackTBHVDTO item = new FeedBackTBHVDTO();
					item.feedBackBasic.initDataWithCursor(c);
					item.currentState = FeedBackTBHVDTO.STATE_NO_UPDATE;
					listReviews.add(item);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return listReviews;
	}

	/**
	 * 
	 * get reviews of staff
	 * 
	 * @author: HaiTC3
	 * @param data
	 * @return
	 * @return: ReviewsStaffViewDTO
	 * @throws:
	 * @since: Jan 29, 2013
	 */
	public ReviewsStaffViewDTO getReviewStaffView(Bundle data) {
		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID);
		String customerId = data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		String createUserId = data.getString(IntentConstants.INTENT_STAFF_OWNER_ID);
		String tpdId = data.getString(IntentConstants.INTENT_TRAINING_DETAIL_ID);
		ReviewsStaffViewDTO reviewsInfo = new ReviewsStaffViewDTO();
		reviewsInfo.feedBackSKU.feedBack = this.getFeedBackSKU(staffId,customerId, shopId, tpdId, createUserId);
		if (reviewsInfo.feedBackSKU.feedBack != null
				&& reviewsInfo.feedBackSKU.feedBack.feedBackId > 0) {
			String feedBackSKUId = String
					.valueOf(reviewsInfo.feedBackSKU.feedBack.feedBackId);
			reviewsInfo.listSKU = this.getListSKUDone(feedBackSKUId);
		}
		reviewsInfo.listReviewsObject = this.getListReviewsOfStaff(shopId,
				createUserId, staffId, customerId, tpdId);
		return reviewsInfo;
	}

	/**
	 * 
	 * get list revies of staff
	 * @author: HaiTC3
	 * @return
	 * @return: ArrayList<ReviewsObjectDTO>
	 * @throws:
	 * @since: Jan 29, 2013
	 */
	public ArrayList<ReviewsObjectDTO> getListReviewsOfStaff(String shopId,
			String createUserId, String staffId, String customerId, String tpdId) {
		ArrayList<ReviewsObjectDTO> listReviews = new ArrayList<ReviewsObjectDTO>();
		StringBuffer requestGetListReviews = new StringBuffer();
		requestGetListReviews.append("SELECT * ");
		requestGetListReviews.append("FROM   feedback ");
		requestGetListReviews.append("WHERE  staff_id = ? ");
		requestGetListReviews.append("       AND customer_id = ? ");
		requestGetListReviews.append("       AND shop_id = ? ");
		requestGetListReviews.append("       AND training_plan_detail_id = ? ");
		requestGetListReviews.append("       AND status = 1 ");
		requestGetListReviews.append("       AND type in (6, 7, 8) ");
		requestGetListReviews.append("       AND create_user_id = ? ");
		requestGetListReviews
				.append("       AND Date(create_date) = Date('now', 'localtime') ");
		String[] params = { staffId, customerId, shopId, tpdId, createUserId };
		Cursor c = null;
		try {
			// get total row first
			c = rawQuery(requestGetListReviews.toString(), params);

			if (c != null) {
				if (c.moveToFirst()) {
					do {
						ReviewsObjectDTO trainingResult = new ReviewsObjectDTO();
						trainingResult.parserDataFromCursor(c);
						listReviews.add(trainingResult);
					} while (c.moveToNext());
				}
			}

		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return listReviews;
	}

	/**
	 * 
	 * get list SKU done
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: ArrayList<FeedBackDetailDTO>
	 * @throws:
	 * @since: Jan 29, 2013
	 */
	public ArrayList<FeedBackDetailDTO> getListSKUDone(String feedbackId) {
		ArrayList<FeedBackDetailDTO> result = new ArrayList<FeedBackDetailDTO>();
		String requestGetListSKU = "select fbd.*, p.product_code PRODUCT_CODE from feedback_detail fbd, product p where fbd.product_id = p.product_id and p.status = 1 and feedback_id = ?";
		String[] params = { feedbackId };
		Cursor c = null;
		try {
			// get total row first
			c = rawQuery(requestGetListSKU.toString(), params);

			if (c != null) {
				if (c.moveToFirst()) {
					do {
						FeedBackDetailDTO feedBackSKU = new FeedBackDetailDTO();
						feedBackSKU.initWithCursor(c);
						result.add(feedBackSKU);
					} while (c.moveToNext());
				}
			}

		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}

	/**
	 * 
	 * get feedback for list SKU
	 * 
	 * @author: HaiTC3
	 * @param staffId
	 * @param customerId
	 * @param shopId
	 * @param tpdId
	 * @param createUserId
	 * @return
	 * @return: FeedBackDTO
	 * @throws:
	 * @since: Jan 29, 2013
	 */
	public FeedBackDTO getFeedBackSKU(String staffId, String customerId,
			String shopId, String tpdId, String createUserId) {
		FeedBackDTO feedBackSKU = new FeedBackDTO();
		StringBuffer requestGetListSKU = new StringBuffer();
		requestGetListSKU.append("SELECT * ");
		requestGetListSKU.append("FROM   feedback ");
		requestGetListSKU.append("WHERE  staff_id = ? ");
		requestGetListSKU.append("       AND customer_id = ? ");
		requestGetListSKU.append("       AND shop_id = ? ");
		requestGetListSKU.append("       AND training_plan_detail_id = ? ");
		requestGetListSKU.append("       AND status = 1 ");
		requestGetListSKU.append("       AND type = 9 ");
		requestGetListSKU.append("       AND create_user_id = ? ");
		requestGetListSKU
				.append("       AND Date(create_date) = Date('now', 'localtime') ");

		String[] params = { staffId, customerId, shopId, tpdId, createUserId };
		Cursor c = null;
		try {
			// get total row first
			c = rawQuery(requestGetListSKU.toString(), params);

			if (c != null) {
				if (c.moveToFirst()) {
					feedBackSKU.initDataWithCursor(c);
				}
			}

		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return feedBackSKU;
	}
}
