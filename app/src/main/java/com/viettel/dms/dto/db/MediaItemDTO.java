/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.MEDIA_ITEM_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;



/**
 * 
 * Mo ta cho class
 * 
 * @author: ThanhNN8
 * @version: 1.0
 * @since: 1.0
 */
public class MediaItemDTO extends AbstractTableDTO {
	public static final int MEDIA_IMAGE  = 0;
	public static final int MEDIA_VIDEO  = 1;
	// hinh anh dong cua
	public static final int TYPE_LOCATION_CLOSED = 0;
	// hinh anh trung bay
	public static final int TYPE_DISPLAY_PROGAME_IMAGE = 1;
	// hinh anh diem ban
	public static final int TYPE_LOCATION_IMAGE = 2;
	// hinh anh san pham
	public static final int TYPE_PRODUCT_IMAGE = 3;
	// hinh anh doi thu canh tranh
	public static final int TYPE_IMAGE_RIVAL = 4; 

	private static final long serialVersionUID = 1L;
	// ma media
	public long id ;
	// ma thuc the chua media nay
	public long objectId ; 
	// url cua media
	public String url ;
	// url thumnail cua media
	public String thumbUrl ; 
	// title cua media
	public String title ; 
	// loai media
	public int mediaType ; 
	// mo ta cho media
	public String description ; 
	// kich thuoc cua media
	public long fileSize ; 
	// chieu rong
	public int width ;
	// chieu cao
	public int height ;
	// ngay tao
	public String createDate;
	// ngay cap nhat
	public String updateDate;
	// nguoi tao
	public String createUser;
	// nguoi cap nhat
	public String updateUser;
	// vi tri lat
	public double lat;
	// vi tri lng
	public double lng;
	// loai object
	public int objectType;
	// staff id
	public long staffId;
	// duong dan luu duoi sd card
	public String sdCardPath = "";
	
	public boolean isSelected = false;

	// status
	public int status;
	// display programe id
	public long displayProgrameId;
	// type
	public int type;
	// shop id
	public int shopId;
	
	public MediaItemDTO(){
		super(TableType.MEDIA_ITEM_TABLE);
	}
	
	@Override
	public MediaItemDTO clone() {
		MediaItemDTO category = new MediaItemDTO();
		category.id = id;
		category.objectId = objectId;
		category.url = url;
		category.thumbUrl = thumbUrl;
		category.title = title;
		category.mediaType = mediaType;
		category.description = description;
		category.fileSize = fileSize;
		category.width = width;
		category.height = height;
		category.createDate = createDate;
		category.updateDate = updateDate;
		category.createUser = createUser;
		category.updateUser = updateUser;
		category.lat = lat;
		category.lng = lng;
		category.objectType = objectType;
		category.sdCardPath = sdCardPath;
		category.staffId = staffId;
		category.status = status;
		category.displayProgrameId = displayProgrameId;
		category.staffId = staffId;
		category.shopId = shopId;
		
		return category;
	}

	/**
	 * Tao cau sql de insert media item 
	 * @author: PhucNT
	 * @return
	 * @return: JSONArray
	 * @throws:
	*/
	public JSONArray generateInsertMediaItem() {
		// TODO Auto-generated method stub
		JSONArray result = new JSONArray();
		JSONObject orderJson = new JSONObject();
		try{
//			UPDATE media item 
			
			orderJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			orderJson.put(IntentConstants.INTENT_TABLE_NAME, MEDIA_ITEM_TABLE.TABLE_MEDIA_ITEM);
			
			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(MEDIA_ITEM_TABLE.MEDIA_ITEM_ID,id, null));
		
			params.put(GlobalUtil.getJsonColumn(MEDIA_ITEM_TABLE.OBJECT_ID,objectId, null));
			params.put(GlobalUtil.getJsonColumn(MEDIA_ITEM_TABLE.OBJECT_TYPE, objectType, null));
			params.put(GlobalUtil.getJsonColumn(MEDIA_ITEM_TABLE.URL,url, null));
			params.put(GlobalUtil.getJsonColumn(MEDIA_ITEM_TABLE.MEDIA_TYPE,mediaType, null));
			params.put(GlobalUtil.getJsonColumn(MEDIA_ITEM_TABLE.LAT, lat, null));
			params.put(GlobalUtil.getJsonColumn(MEDIA_ITEM_TABLE.LNG,lng, null));
			params.put(GlobalUtil.getJsonColumn(MEDIA_ITEM_TABLE.FILE_SIZE,fileSize, null));
			params.put(GlobalUtil.getJsonColumn(MEDIA_ITEM_TABLE.CREATE_USER, createUser, null));
			params.put(GlobalUtil.getJsonColumn(MEDIA_ITEM_TABLE.CREATE_DATE,createDate, null));		
			params.put(GlobalUtil.getJsonColumn(MEDIA_ITEM_TABLE.STAFF_ID,staffId, null));	
			params.put(GlobalUtil.getJsonColumn(MEDIA_ITEM_TABLE.SHOP_ID,shopId, null));	
			if(!StringUtil.isNullOrEmpty(updateDate)) {
				params.put(GlobalUtil.getJsonColumn(MEDIA_ITEM_TABLE.UPDATE_DATE,updateDate, null));
			}
			
			if(!StringUtil.isNullOrEmpty(updateUser)) {
				params.put(GlobalUtil.getJsonColumn(MEDIA_ITEM_TABLE.UPDATE_USER,updateUser, null));
			}
			// syn status
			
			orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);
			result.put(orderJson);
			// ds where params --> insert khong co menh de where
			// sqlInsertOrder.put(IntentConstants.INTENT_LIST_WHERE_PARAM, value);
			
		}catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return result;
	}

	/**
	 * parse json media sau khi da lay link tu server
	 * @author: PhucNT
	 * @param jsonObject
	 * @return: void
	 * @throws:
	*/
	public void parseJsonMedia(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		try {
			this.id = jsonObject.getInt("id");
			url = jsonObject.getString("url");
			thumbUrl = jsonObject.getString("thumbUrl");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		
	}
}
