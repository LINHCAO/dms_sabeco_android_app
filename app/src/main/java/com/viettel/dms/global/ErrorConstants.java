/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.global;

/**
 *  Define cac ma loi server tra ve
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class ErrorConstants {
	public static final String ERROR_CODE_STRING = "errorCode";
    public static final String RESPONSE_STRING = "response";
    public static final String RESPONSE_STRING_SYNDATA_SUCCESS = "SYNDATA_SUCCESS";
    public static final String RESPONSE_STRING_SYNDATA_ERROR = "SYNDATA_ERROR";
    
    //ERROR CLIENT DINH NGHIA
    public static final int ERROR_EXPIRED_TIMESTAMP 		= 0;// loi thoi gian nho hon server
    
    //ERROR SERVER DINH NGHIA
    public static final int ERROR_NO_CONNECTION 			= 404;  // loi ko ket noi
    public static final int ERROR_CODE_SUCCESS 				= 200; //thanh cong
    public static final int ERROR_COMMON 					= 201;  // loi chung
    public static final int ERROR_ACCOUNT_LOCKED 			= 205  ;//tai khoan bi khoa
    public static final int ERROR_INVALID_ACCOUNT 			= 207 ;//(errorCode=207)
    public static final int ERROR_INVALID_PASSWORD 			= 208 ;//(errorCode=208)
    
    public static final int ERROR_INVALID_IMEI 				= 211; //loi khong dung imei device
    public static final int ERROR_INVALID_SERIAL			= 212; //loi khong dung serial sim 
    public static final int ERROR_INVALID_IMEI_OR_SERIAL  	= 213; //
    
    public static final int ERROR_SESSION_RESET 			= 215;//(errorCode=215)	session time out
    
    public static final int ERROR_ACCOUNT_STATUS 			= 242 ;//tran thai tai khoan ko dung
    public static final int ERROR_ACCOUNT_MAXIMUM_LOCK		= 243  ;//dang nhap sai qua so lan quy dinh
    
    public static final int ERROR_DB_OUT_OF_DATE 			= 304;//database out of date
    public static final int ERROR_REST_DB_OUT_MAXIMUM 		= 241;//vuot qua so lan reset db
    public static final int ERROR_UNIQUE_CONTRAINTS 		= 244;// loi trung khoa
    public static final int ERROR_CONNET_SERVER_DB 			= 245;// loi khong ket noi duoc voi DB server
    public static final int ERROR_WAITING 					= 1;  // loi lock db

    

}
