/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.constants;


/**
 *  Define hang so trong chuong trinh
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public interface Constants {
	public static final String OAUTH_SECRET = "7cb7bff14f7f976e045624ccdafb00c32a2531cb";
	public static final String OAUTH_KEY = "afcdmsone";
//	public static final String OAUTH_SECRET = "3bf47226c60e20ce280eadc22c9c7f5de0cbc6c2";
//	public static final String OAUTH_KEY = "kunkun";
	
	public static final String CLIENT_ID = "dms";
	public static final String CLIENT_SECRET = "1192d23f3d8470e0deab0a51d5946f9c";
	public static final String GRANT_TYPE = "client_credentials";
	public static final String RESPONSE_TYPE = "token";
	
	public static final String EXPIRED_TIMESTAMP = "Expired timestamp";
	public static final String APACHE_TOMCAT = "Apache Tomcat";
	public static final String DMS_VIETTEL_MAP_KEY_RELEASE = "0766ff0fa5c27c0cd957ec413ac92a10";
//	public static final String DMS_VIETTEL_MAP_KEY_TEST = "TTUDCNTT_KEY_TEST";
	
	/* Dinh nghia cac phuong thuc request truyen len server*/
	public static final String METHOD_QUERY_EXECUTE_SQL = "queryController/executeSql";
	public static final String METHOD_AUTH_LOGIN = "authController/login";
	public static final String METHOD_AUTH_CHANGE_PASSWORD = "authController/changePassword";
	public static final String METHOD_SYN_CREATE_SQLITE_FILE = "synDataController/createSQLiteFile";
	public static final String METHOD_SYN_GET_DATA_FROM_SERVER = "synDataController/getDataFromServer";
	public static final String METHOD_SYN_GET_SERVER_INFO = "synDataController/getServerInfo";
	public static final String METHOD_SYN_UPDATE_STAFF_SYN_DATA_STATUS = "synDataController/updateStaffSynDataStatus";
	public static final String METHOD_MEDIA_ADD_IMAGE = "mediaController/addImage";
	public static final String METHOD_MEDIA_UPLOAD_LOG_FILE = "mediaController/uploadLogFile";
	/**/
	
	//kich thuoc cua hinh anh dinh kem
	public static final int MAX_THUMB_NAIL_WIDTH = 70;
	public static final int MAX_THUMB_NAIL_HEIGHT = 50;
	//kich thuoc anh toi da upload
	public static final int MAX_FULL_IMAGE_WIDTH = 600;
	public static final int MAX_FULL_IMAGE_HEIGHT = 600;
	//kich thuoc anh toi da upload
	public static final int MAX_THUMB_IMAGE_WIDTH = 250;
	public static final int MAX_THUMB_IMAGE_HEIGHT = 200;
	
	public static final int MAX_UPLOAD_IMAGE_WIDTH = 740;
	public static final int MAX_UPLOAD_IMAGE_HEIGHT = 740;
	public static final int MAX_LENGHT_QUANTITY = 5;
	public static final int MAX_LENGTH_TRANSACTION_ID = 14;
	public static final int MAX_LENGTH_RANDOM_ID = 18;
	
	public static final String HTTPCONNECTION_POST = "POST";
	public static final String STR_BLANK = "";
	public static final String STR_SPACE = " ";
	public static final String LOG_LBS = "LOG_LBS";
	public static final String ACTION_BROADCAST = "dms.action";
	public static final String HASHCODE_BROADCAST = "dms.hashCode";
	public static final String STR_SPLIT_SQL = "@";
	public static final String REPLACED_STRING = "xxx";
	
	public static final String CIPHER_KEY = "dms@mobile@viettel";
	
	/**
	 * define type map when show
	 */
	public static final int TYPE_MAP_MY_LOCATION = 0;
	public static final int TYPE_MAP_PLACE_LOCATION_FIND_WAY = 1;
	public static final int TYPE_MAP_PLACE_LOCATION_ON_CONTENT = 2;
	public static final int NUM_ITEM_PER_PAGE = 10;
	public static final String DAY_LINE[]={"T2","T3","T4","T5","T6","T7","CN"};
	public static final String TODAY[]={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
	public static final String DATABASE_NAME = "DMSDatabase";
	public static final String TYPE_INTEGER = "INTEGER";
	public static final int MAX_LENGTH_CUSTOMER_CODE = 5;
	public static final int MAX_LENGTH_CUSTOMER_NAME= 250;
	public static final String ARRAY_LINE_CHOOSE[] = { "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5",
			"Thứ 6", "Thứ 7", "Chủ nhật", "Tất cả" };
	public static final String TEMP_IMG = "temp_image";
	public static final String TEMP_SYNDATA_FILE = "temp_syndata";
	public static final String STR_AND = " và ";
	public static final String STR_OR = " hoặc ";
	public static final String HAS_ERROR_HAPPEN = "Có lỗi xảy ra, vui lòng thử lại!";
	
	
	public static final int TAKEN_PHOTO_WITH = 200;
	public static final int TAKEN_PHOTO_HEIGHT = 200;
	
	// toa do lat, lng de show ban do toan viet nam
	public static final double LAT_SHOW_VN = 16.852914;
	public static final double LNG_SHOW_VN = 106.849365;
	public static final double LAT_DMS_TOWNER = 10.729793;
	public static final double LNG_DMS_TOWNER = 106.724388;
	public static final double CHECK_SALE_COMPETITOR = 1;
	public static final double CHECK_REMAIN_COMPETITOR = 0;
	
	// trang thai van de
	public static final String ALL = "Tất cả";// tat ca
	public static final String NOT_DONE = "Tạo mới";// tao moi
	public static final String DONE = "Đã thực hiện";// da thuc hien
	public static final String APPROVED = "Đã duyệt";// da duyet
	public static final String[] ARRCHOOSE = new String[] { ALL, NOT_DONE, DONE, APPROVED };
	// loai GST
	public static final String FEEDBACK_TYPE_TBHV = "FEEDBACK_TYPE_TBHV";
	// loai GSNPP
	public static final String FEEDBACK_TYPE_GSNPP = "FEEDBACK_TYPE_GSNPP";
	// loai TTTT
	public static final String FEEDBACK_TYPE_TTTT = "FEEDBACK_TYPE_TTTT";
	// loai GS
	public static final String TYPE_GS = "GS";
	// loai TT
	public static final String TYPE_TT = "TT";
	// loai NVBH
	public static final String TYPE_NVBH = "NVBH";
	// loai GST
	public static final String TYPE_GST = "GST";  

	public static final int CONNECTION_ONLINE  = 1;
	public static final int CONNECTION_OFFLINE  = -1;
	// loai album (HADB- Hinh Anh Diem Ban)
	public static final int TYPE_HADB = 2;
	public static final String MESSAGE_ERROR_COMMON = "Lỗi trong quá trình xử lý, vui lòng thử lại.";
	// kich thuoc left menu
	public static final int LEFT_MARGIN_TABLE_DIP = 40;
	public static final int RIGHT_MARGIN_TABLE_DIP = 40;
	public static final int ICON_SIZE = 32;
	public static final int DEFAULT_DISTANCE_ORDER = 300;
	public static String MAP_PROTOCOL = "http";
	public static String MAP_PATH = "viettelmaps.vn";
	public static int MAP_PORT = 80;
	public static final boolean IS_TABLET = false;
	public static final String TYPE_FORMAT_INTEGER = "d";
	public static final String TYPE_FORMAT_FLOAT = "f";
	public static int MAX_LENGHT_DISCOUNT_MONEY = 12;
	public static int MAX_LENGHT_DISCOUNT_PERCENT = 5;
	public static int MAX_LENGHT_PRICE = 20;
	public static int MAX_LENGHT_PERCENT = 2;
	public static final String MESSAGE_ERROR_SESSION_RESET = "not login";
	public static final String ARRAY_C2_TYPE_CHOOSE[] = { "C2 Mua", "C2 Bán" };
}
