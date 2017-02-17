/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

/**
 * Log DTO class
 * @version: 1.0 
 * @since:  10:15:13 8 Jan 2014
 */
@SuppressWarnings("serial")
public class LogDTO extends AbstractTableDTO{
	// mac dinh chua co
	public static final String STATE_NONE = "-1";
	// tao moi
	public static final String STATE_NEW = "0"; 
	// thuc hien that bai chung
	public static final String STATE_FAIL = "1";
	// thanh cong
	public static final String STATE_SUCCESS = "2";
	// close --> NHUNG REQUEST LOI DO THOI GIAN KHONG HOP LE THI CANCEL CAC REQUEST CO CUNG NGAY
	public static final String STATE_INVALID_TIME = "3";
	// nhung request trung khoa thi khong thuc hien goi len lai
	public static final String STATE_UNIQUE_CONTRAINTS = "4";
	// nhung request cua don hang da bi delete
	public static final String STATE_ORDER_DELETED = "5";
	// nhung request anh gui len server ko ton tai hinh anh
	public static final String STATE_IMAGE_DELETED = "6";
		
	// khong can kiem tra thoi gian khi request
	public static final int NO_NEED_CHECK_TIME = 0;
	// can kiem tra khi request
	public static final int NEED_CHECK_TIME = 1;
	
	// log cua cac request binh thuong goi len server
	public static final int TYPE_NORMAL = 0;
	// request ghi log loi tu client len server 
	public static final int TYPE_LOG = 1;
	// request anh 
	public static final int TYPE_IMAGE = 2;
	// request don hang
	public static final int TYPE_ORDER = 3;
	// request cap nhat vi tri
	public static final int TYPE_POSITION = 4;
	
	// id
	public String logId;
	// gia tri goi len server
	public String value;
	// userId
	public String userId;
	// trang thai
	public String state = "-1";
	// table name - tam thoi chua dung
	public String tableName;
	// ngay tao
	public String createDate;
	// ngay cap nhat
	public String updateDate;
	// nguoi tao
	public String createUser;
	// nguoi cap nhat
	public String updateUser;
	// co can kiem tra thoi gian truoc khi thuc hien request hay khong? : 0: khong can, 1: can kiem tra
	public int needCheckDate = NO_NEED_CHECK_TIME;
	// dung de phan biet: 0: cac TH request binh thuong, 1: request ghi log, request anh
	public int tableType = TYPE_NORMAL;
	// id table - tam thoi chua dung
	public String tableId;
	// kiem tra request can check thoi gian hay ko? 1: mac dinh kiem tra , 
	//0: ko can kiem tra nhung request da duoc loc theo dieu kien cau hinh ap_param
	public int isCheckTime = 1;
	
	public LogDTO(){}
	
	public LogDTO(String logId, String value, String state, String userId, String createDate, String createUser,
			int needCheckDate, String tableId, int tableType, String tableName){
		this.setType(AbstractTableDTO.TableType.LOG);
		this.logId = logId;
		this.value = value;
		this.state = state;
		this.userId = userId;
		this.createDate = createDate;
		this.createUser = createUser;
		this.needCheckDate = needCheckDate;
		this.tableId = tableId;
		this.tableType = tableType;
		this.tableName = tableName;
	}

}
