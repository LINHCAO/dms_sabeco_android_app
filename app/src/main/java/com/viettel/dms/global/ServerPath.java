/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.global;


/**
 *  Thong tin cau hinh build 
 *  @author: BangHN
 *  @version: 1.0 
 *  @since: 1.0 
 */ 
public class ServerPath { 
	
	//1 SERVER TEST SABECO
//	 Apply https // nhớ start 2 con chưng thuc : 11080 va 13080
//	public static final String SERVER_PATH = "http://10.30.174.211:14080/";
//	public static final String SERVER_PATH_LOGIN = "https://10.30.174.211:14443/";
//	public static final String IMAGE_PRODUCT_VNM = "http://10.30.174.211:9005/sabeco_demo/image/ProductImage/";
//	public static final String SERVER_PATH_NATIVE_LIB = "http://10.30.174.211:9005/sabeco_demo/nativelibs/";
//	public static final String IMAGE_PATH = "http://10.30.174.211:9005/sabeco_demo";
//	public static final String SERVER_PATH_OAUTH = SERVER_PATH;
//	public static final String SERVER_PATH_SYN_DATA = SERVER_PATH_OAUTH + "synchronizer";

	// SERVER ra soat ATTT
//	public static final String SERVER_PATH_LOGIN = "http://10.60.110.2:9190/";
//	public static final String SERVER_PATH = "https://10.60.110.2:9193/";
//	public static final String IMAGE_PRODUCT_VNM = "http://10.60.110.2:8000/SBC/upload/ProductImage/";
//	public static final String SERVER_PATH_NATIVE_LIB = "http://10.60.110.2:8000/SBC/nativelibs/";
//	public static final String SERVER_PATH_OAUTH = SERVER_PATH;
//	public static final String IMAGE_PATH = "http://10.60.110.2:8000/SBC";
//	public static final String SERVER_PATH_SYN_DATA = SERVER_PATH_OAUTH + "synchronizer";

	// SERVER LOCAL SABECO
	public static final String SERVER_PATH = "http://10.30.174.82:8080/";
    public static final String SERVER_PATH_LOGIN = "http://10.30.174.82:8080/";
    public static final String IMAGE_PRODUCT_VNM = "http://10.30.174.82:9005/sabeco_demo/image/ProductImage/";
    public static final String SERVER_PATH_NATIVE_LIB = "http://10.30.174.82:9005/sabeco_demo/nativelibs/";
    public static final String IMAGE_PATH = "http://10.30.174.82:9005/sabeco_demo";
	public static final String SERVER_PATH_OAUTH = SERVER_PATH;
    public static final String SERVER_PATH_SYN_DATA = SERVER_PATH_OAUTH + "synchronizer";
	
//============================RELEASE======================================================================	
	//SERVER RELEASE https, cấu hình HAProxy, port mobile server: 9380, 2 port chứng thực: 9360, 9370
//	public static final String SERVER_PATH_LOGIN = "https://biasaigon.dmsone.vn:8580/";
// 	public static final String SERVER_PATH = "http://biasaigon.dmsone.vn:8583/";
// 	public static final String IMAGE_PRODUCT_VNM = "http://biasaigon.dmsone.vn:8000/dmsoneSBC/upload/ProductImage/";
// 	public static final String SERVER_PATH_NATIVE_LIB = "http://biasaigon.dmsone.vn:8000/SBC/nativelibs/";
// 	public static final String SERVER_PATH_OAUTH = SERVER_PATH;
// 	public static final String IMAGE_PATH = "http://biasaigon.dmsone.vn:8000/SBC";
// 	public static final String SERVER_PATH_SYN_DATA = SERVER_PATH_OAUTH + "synchronizer";

	//theo dia chi ip
//	public static final String SERVER_PATH_LOGIN = "https://203.190.173.195:8580/";
// 	public static final String SERVER_PATH = "http://203.190.173.195:8583/";
// 	public static final String IMAGE_PRODUCT_VNM = "http://203.190.173.195:8000/dmsoneSBC/upload/ProductImage/";
// 	public static final String SERVER_PATH_NATIVE_LIB = "http://203.190.173.195:8000/SBC/nativelibs/";
// 	public static final String SERVER_PATH_OAUTH = SERVER_PATH;
// 	public static final String IMAGE_PATH = "http://203.190.173.195:8000/SBC";
// 	public static final String SERVER_PATH_SYN_DATA = SERVER_PATH_OAUTH + "synchronizer";



}
