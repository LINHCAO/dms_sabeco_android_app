/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.MediaItemDTO;
import com.viettel.dms.dto.photo.PhotoDTO;
import com.viettel.dms.dto.view.AlbumDTO;
import com.viettel.dms.dto.view.ListAlbumUserDTO;
import com.viettel.dms.dto.view.PhotoThumbnailListDto;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.utils.VTLog;

/**
 * 
 * Table cho media item
 * 
 * @author: ThanhNN8
 * @version: 1.0
 * @since: 1.0
 */
public class MEDIA_ITEM_TABLE extends ABSTRACT_TABLE {
	// id media
	public static final String MEDIA_ITEM_ID = "MEDIA_ITEM_ID";
	// ma khach hang / ma chuong trinh
	public static final String OBJECT_ID = "OBJECT_ID";
	// url cua hinh anh
	public static final String URL = "URL";
	// thumnail cua hinh anh
	public static final String THUMB_URL = "THUMB_URL";
	// title cua hinh anh
	public static final String TITLE = "TITLE";
	// loai hinh anh (trung bay, khach hang, cua san pham, ...)
	public static final String MEDIA_TYPE = "MEDIA_TYPE";
	// mo ta hinh anh
	public static final String DESCRIPTION = "DESCRIPTION";
	// kich thuoc hinh anh
	public static final String FILE_SIZE = "FILE_SIZE";
	// chieu rong cua hinh anh
	public static final String WIDTH = "WIDTH";
	// chieu cao cua hinh anh
	public static final String HEIGHT = "HEIGHT";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// lat
	public static final String LAT = "LAT";
	// lng
	public static final String LNG = "LNG";
	// loai thuc the chua hinh anh
	public static final String OBJECT_TYPE = "OBJECT_TYPE";
	// url tren client
	public static final String CLIENT_THUMB_URL = "CLIENT_THUMB_URL";
	// staff id
	public static final String STAFF_ID = "STAFF_ID";
	// table name
	public static final String TABLE_MEDIA_ITEM = "MEDIA_ITEM";
	// status
	public static final String STATUS = "STATUS";
	// display programe id
//	public static final String DISPLAY_PROGRAM_ID = "DISPLAY_PROGRAM_ID";  // server that hien tai chua doi cot nay
    public static final String DISPLAY_PROGRAM_ID = "PRO_INFO_ID"; // server test
	
	// type
	public static final String TYPE = "TYPE";
	// shopId
	public static final String SHOP_ID = "SHOP_ID";
	
	public MEDIA_ITEM_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_MEDIA_ITEM;
		this.columns = new String[] {MEDIA_ITEM_ID, OBJECT_ID ,URL,THUMB_URL,TITLE,MEDIA_TYPE,DESCRIPTION,FILE_SIZE,WIDTH,HEIGHT,CREATE_DATE,UPDATE_DATE,CREATE_USER,UPDATE_USER,LAT,LNG,OBJECT_TYPE,STAFF_ID, STATUS, DISPLAY_PROGRAM_ID, TYPE, SHOP_ID};
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.lib.sqllite.db.ABSTRACT_TABLE#insert(com.viettel
	 * .vinamilk.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long insert(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		MediaItemDTO mediaDTO = (MediaItemDTO) dto;
		ContentValues value = initDataRow(mediaDTO);
		return insert(null, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.lib.sqllite.db.ABSTRACT_TABLE#update(com.viettel
	 * .vinamilk.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		MediaItemDTO mediaDTO = (MediaItemDTO)dto;
		ContentValues value = initDataRow(mediaDTO);
		String[] params = { "" + mediaDTO.id };
		return update(value, MEDIA_ITEM_ID + " = ?", params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.lib.sqllite.db.ABSTRACT_TABLE#delete(com.viettel
	 * .vinamilk.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long delete(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		MediaItemDTO mediaDTO = (MediaItemDTO)dto;
		String[] params = { "" + mediaDTO.id };
		return delete(MEDIA_ITEM_ID + " = ?", params);
	}
	
	private ContentValues initDataRow(MediaItemDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(MEDIA_ITEM_ID, dto.id);
		editedValues.put(OBJECT_ID, dto.objectId);
		editedValues.put(URL, dto.url);
		editedValues.put(THUMB_URL, dto.thumbUrl);
		if(!StringUtil.isNullOrEmpty(dto.title)) {
			editedValues.put(TITLE, dto.title);
		}
		editedValues.put(MEDIA_TYPE, dto.mediaType);
		editedValues.put(FILE_SIZE, dto.fileSize);
		editedValues.put(WIDTH, dto.width);
		editedValues.put(HEIGHT, dto.height);
		if(!StringUtil.isNullOrEmpty(dto.createDate)) {
			editedValues.put(CREATE_DATE, dto.createDate);
		}
		if(!StringUtil.isNullOrEmpty(dto.updateDate)) {
			editedValues.put(UPDATE_DATE, dto.updateDate);
		}
		if(!StringUtil.isNullOrEmpty(dto.createUser)) {
			editedValues.put(CREATE_USER, dto.createUser);
		}
		if(!StringUtil.isNullOrEmpty(dto.updateUser)) {
			editedValues.put(UPDATE_USER, dto.updateUser);
		}
		editedValues.put(LAT, dto.lat);
		editedValues.put(LNG, dto.lng);
		editedValues.put(OBJECT_TYPE, dto.objectType);
		editedValues.put(STAFF_ID, dto.staffId);
		editedValues.put(STATUS, dto.status);
		editedValues.put(DISPLAY_PROGRAM_ID, dto.displayProgrameId);
		editedValues.put(TYPE, dto.type);
		editedValues.put(SHOP_ID, dto.shopId);
		return editedValues;
	}
	
	public MediaItemDTO getRowById(String id) {
		MediaItemDTO dto = null;
		Cursor c = null;
		try {
			String[]params = {id};
			c = query(
					MEDIA_ITEM_ID + " = ?" , params, null, null, null);
		} catch (Exception ex) {
			c = null;
		}
		if (c != null) {
			if (c.moveToFirst()) {
				dto = initLogDTOFromCursor(c);
			}
		}
		if (c != null) {
			c.close();
		}
		return dto;
	}

	/**
	 * 
	*  Ham gan gia tri cho dto from ket qua cua cau lenh sql
	*  @author: ThanhNN8
	*  @param c
	*  @return
	*  @return: MediaItemDTO
	*  @throws:
	 */
	private MediaItemDTO initLogDTOFromCursor(Cursor c) {
		MediaItemDTO mediaDTO = new MediaItemDTO();
		mediaDTO.id = (c.getLong(c.getColumnIndex(MEDIA_ITEM_ID)));
		mediaDTO.objectId = (c.getLong(c.getColumnIndex(OBJECT_ID)));
		mediaDTO.url = (c.getString(c.getColumnIndex(URL)));
		mediaDTO.thumbUrl = (c.getString(c.getColumnIndex(THUMB_URL)));
		mediaDTO.title = (c.getString(c.getColumnIndex(TITLE)));
		mediaDTO.mediaType = (c.getInt(c.getColumnIndex(MEDIA_TYPE)));
		mediaDTO.fileSize = (c.getLong(c.getColumnIndex(FILE_SIZE)));
		mediaDTO.width = (c.getInt(c.getColumnIndex(WIDTH)));
		mediaDTO.height = (c.getInt(c.getColumnIndex(HEIGHT)));
		mediaDTO.createDate = (c.getString(c.getColumnIndex(CREATE_DATE)));
		mediaDTO.updateDate = (c.getString(c.getColumnIndex(UPDATE_DATE)));
		mediaDTO.createUser = (c.getString(c.getColumnIndex(CREATE_USER)));
		mediaDTO.updateUser = (c.getString(c.getColumnIndex(UPDATE_USER)));
		mediaDTO.lat = (c.getFloat(c.getColumnIndex(LAT)));
		mediaDTO.lng = (c.getFloat(c.getColumnIndex(LNG)));
		mediaDTO.objectType = (c.getInt(c.getColumnIndex(OBJECT_TYPE)));
		mediaDTO.staffId = (c.getInt(c.getColumnIndex(STAFF_ID)));
		mediaDTO.status = c.getInt(c.getColumnIndex(STATUS));
		mediaDTO.displayProgrameId = c.getLong(c.getColumnIndex(DISPLAY_PROGRAM_ID));
		mediaDTO.type = c.getInt(c.getColumnIndex(TYPE));
		mediaDTO.shopId = c.getInt(c.getColumnIndex(SHOP_ID));
		
		return mediaDTO;
	}
	
	/**
	 * 
	*  Ham lay danh sach media ho tro cho man hinh gioi thieu san pham
	*  @author: ThanhNN8
	*  @param c
	*  @return
	*  @return: MediaItemDTO
	*  @throws:
	 */
	private MediaItemDTO initForListMedia(Cursor c) {
		MediaItemDTO mediaDTO = new MediaItemDTO();
		if (c.getColumnIndex(MEDIA_ITEM_ID) >= 0) {
			mediaDTO.id = (c.getLong(c.getColumnIndex(MEDIA_ITEM_ID)));
		} else {
			mediaDTO.id = Long.valueOf(0);
		}
		if (c.getColumnIndex(OBJECT_ID) >= 0) {
			mediaDTO.objectId = (c.getLong(c.getColumnIndex(OBJECT_ID)));
		} else {
			mediaDTO.objectId = Long.valueOf(0);
		}
		if (c.getColumnIndex(URL) >= 0) {
			mediaDTO.url = c.getString(c.getColumnIndex(URL));
		} else {
			mediaDTO.url = "";
		}
		if (c.getColumnIndex(THUMB_URL) >= 0) {
			mediaDTO.thumbUrl = c.getString(c.getColumnIndex(THUMB_URL));
		} else {
			mediaDTO.thumbUrl = "";
		}
		if (c.getColumnIndex(MEDIA_TYPE) >= 0) {
			mediaDTO.mediaType = (c.getInt(c.getColumnIndex(MEDIA_TYPE)));
		} else {
			mediaDTO.mediaType = 0;
		}
		if (c.getColumnIndex(CLIENT_THUMB_URL) >= 0) {
			mediaDTO.sdCardPath = (c.getString(c.getColumnIndex(CLIENT_THUMB_URL)));
		} else {
			mediaDTO.sdCardPath = "";
		}
		return mediaDTO;
	}

	/**
	*  Lay danh sach media theo productId
	*  @author: ThanhNN8
	*  @param productId
	*  @return
	*  @return: List<MediaItemDTO>
	*  @throws: 
	*/
	public List<MediaItemDTO> getListMediaOfProduct(String productId) {
		// TODO Auto-generated method stub
		List<MediaItemDTO> result = new ArrayList<MediaItemDTO>();
		String queryGetListMediaOfProduct = "select * from media_item_ex where object_id = ? and object_type = 3 and status=1 order by media_type desc";
		String[]params = {productId};
		Cursor c = null;
		try {
			c = rawQuery(queryGetListMediaOfProduct, params);
			if (c != null) {
				MediaItemDTO mediaItemDTO;
				if (c.moveToFirst()) {
					do {
						mediaItemDTO = initForListMedia(c);
						result.add(mediaItemDTO);
					} while (c.moveToNext());
				}
			}

		} catch (Exception e) {
			VTLog.i("error", e.toString());
			result = null;
		} finally {
			try{
				if (c != null) {
					c.close();
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}
	
	public int updateClientThumbnailUrl (String clientThumbnailUrl, long mediaId) {
		ContentValues args = new ContentValues();
	    args.put(CLIENT_THUMB_URL, clientThumbnailUrl);
	    return mDB.update(TABLE_MEDIA_ITEM, args, MEDIA_ITEM_ID + "=" + mediaId, null);
	}

	/**
	*  Lay thong tin album cua user
	*  @author: Nguyen Thanh Dung
	*  @param customerId
	*  @param shopId
	*  @return: void
	*  @throws: 
	*/
	public ListAlbumUserDTO getAlbumUserInfo(String customerId, String shopId) {
		String[] params = {customerId, DateUtils.now()};
		StringBuffer  sqlObject = new StringBuffer();
		sqlObject.append("select *, count(*) NUM_IMAGE from ( ");
		sqlObject.append("SELECT url, ");
		sqlObject.append("       thumb_url THUMB_URL, ");
		sqlObject.append("       object_type OBJECT_TYPE ");
		sqlObject.append("FROM   media_item mi ");
		sqlObject.append("WHERE  object_id = ? ");
		//sqlObject.append(" and shop_id = ? ");
		sqlObject.append(" and type = 1");
		sqlObject.append(" and status = 1");
		sqlObject.append("       AND object_type IN ( 0, 1, 2, 4) ");
		sqlObject.append("       AND media_type = 0 ");
		//add kiem tra create_date > 2 thang truoc
		sqlObject.append(" and date(mi.create_date) >=  date(?,'start of month','-2 month')");
		sqlObject.append(" order by object_type, datetime(create_date))  ");
		sqlObject.append("GROUP  BY object_type ");
		
		Cursor c = null;
		ListAlbumUserDTO data = new ListAlbumUserDTO(); 
		
		CUSTOMER_TABLE cusTable = new CUSTOMER_TABLE(mDB);
		CustomerDTO cusDTO = cusTable.getCustomerById(customerId);
		if(StringUtil.isNullOrEmpty(cusDTO.shortCode)){
			cusDTO.setCustomerCode(cusDTO.shortCode);
		}
		data.setCustomer(cusDTO);
		
		int type[] = {0, 1, 2, 4};
		for (int i = 0; i < type.length; i++) {
			AlbumDTO dto = new AlbumDTO();
			dto.setAlbumType(type[i]);
			data.getListAlbum().add(dto);
		}
		try {
			c = rawQuery(sqlObject.toString(), params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						int object_type = 0;
						if(c.getColumnIndex("OBJECT_TYPE") >= 0){
							object_type = c.getInt(c.getColumnIndex("OBJECT_TYPE"));
						}
						
						if(object_type == 4)
							object_type = 3;
						
						data.getListAlbum().get(object_type).setThumbUrl(c.getString(c.getColumnIndex(THUMB_URL)));
						data.getListAlbum().get(object_type).setNumImage(c.getInt(c.getColumnIndex("NUM_IMAGE")));
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return data;
	}

	/**
	*  Mo ta chuc nang cua ham
	*  @author: Nguyen Thanh Dung
	*  @param customerId
	*  @param type
	*  @param numTop
	 * @param page 
	 * @param shopId 
	*  @return
	*  @return: ArrayList<PhotoDTO>
	*  @throws: 
	*/
	
	public PhotoThumbnailListDto getAlbumDetailUser(String customerId, String type, String numTop, String page, String shopId, boolean isGetTotalImage) {
//		StringBuilder sqlObject = new StringBuilder();
		
//		sqlObject.append(" select ID, THUMB_URL, URL, Strftime('%d/%m/%Y - %H:%M',create_date) as CREATE_DATE, LAT, LNG, CREATE_USER ");
//		sqlObject.append(" from media_item mi where ");
//		sqlObject.append(" object_id = ? ");
//		sqlObject.append(" and object_type = ? ");
		PhotoThumbnailListDto dto = new PhotoThumbnailListDto();
		
		StringBuffer  sqlObject = new StringBuffer();
		sqlObject.append("SELECT media_item_id, ");
		sqlObject.append("       thumb_url, ");
		sqlObject.append("       url, ");
		sqlObject.append("       Strftime('%d/%m/%Y - %H:%M', mi.create_date) AS CREATE_DATE, ");
		sqlObject.append("       lat, ");
		sqlObject.append("       lng, ");
		sqlObject.append("       st.staff_name                             AS STAFF_NAME, ");
		sqlObject.append("       st.staff_code                             AS STAFF_CODE, ");
		sqlObject.append("       mi.create_user, ");
		sqlObject.append("       mi.pro_info_id                            AS PRO_INFO_ID,  ");
		sqlObject.append("       ifnull(pi.pro_info_code,'')               AS PRO_INFO_CODE, ");
		sqlObject.append("       ifnull(pi.pro_info_name,'')               AS PRO_INFO_NAME ");
		sqlObject.append("FROM   media_item mi ");
		sqlObject.append("       LEFT JOIN staff st ");
		sqlObject.append("              ON mi.staff_id = st.staff_id ");
		sqlObject.append("       LEFT JOIN pro_info pi ");
		sqlObject.append("              ON mi.pro_info_id = pi.pro_info_id ");
		sqlObject.append("WHERE  object_id = ? ");
		sqlObject.append("       AND object_type = ? ");
		sqlObject.append("       AND mi.type = 1 ");
		sqlObject.append("       AND mi.status = 1 ");
		sqlObject.append("       AND mi.media_type = 0 ");
		//add kiem tra create_date > 2 thang truoc
		sqlObject.append(" and date(mi.create_date) >=  date(?,'start of month','-2 month')");
		
		ArrayList<String> params = new ArrayList<String>();
		params.add(customerId);
		params.add(type);
		params.add(DateUtils.now());

		// Lay tong so luong hinh anh
		if (isGetTotalImage) {
			StringBuffer sqlTotal = new StringBuffer();
			sqlTotal.append(" Select count(*) as NUM_IMAGE ");
			sqlTotal.append(" From  (  ");
			sqlTotal.append(sqlObject.toString());
			sqlTotal.append(" ) ");
			ArrayList<String> paramsTotal = new ArrayList<String>();
			paramsTotal.add(customerId);
			paramsTotal.add(type);
			paramsTotal.add(DateUtils.now());
			Cursor cTotal = null;
			try {
				cTotal = rawQuery(sqlTotal.toString(), paramsTotal.toArray(new String[paramsTotal.size()]));
				if (cTotal != null && cTotal.moveToFirst()) {
					int totalImage = cTotal.getInt(cTotal.getColumnIndex("NUM_IMAGE"));
					dto.getAlbumInfo().setNumImage(totalImage);
				}
			} catch (Exception e) {
			} finally {
				if (cTotal != null) {
					cTotal.close();
				}
			}
		}
		
		sqlObject.append(" ORDER BY datetime(mi.create_date) desc ");
		sqlObject.append(" limit ? offset ?");
		params.add(numTop);
		int offset = Integer.parseInt(page) * Integer.parseInt(numTop);
		params.add(String.valueOf(offset));
		
		Cursor c = null;
		ArrayList<PhotoDTO> listPhoto = new ArrayList<PhotoDTO>();
		try {
			c = rawQuery(sqlObject.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				do {
					PhotoDTO photoDTO = initPhotoInfo(c);
					listPhoto.add(photoDTO);
				} while (c.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			if (c != null) {
				c.close();
			}
		}
		dto.getAlbumInfo().getListPhoto().addAll(listPhoto);
		return dto;
	}

	/**
	*  Mo ta chuc nang cua ham
	*  @author: Nguyen Thanh Dung
	*  @param c
	*  @return
	*  @return: PhotoDTO
	*  @throws: 
	*/
	
	private PhotoDTO initPhotoInfo(Cursor c) {
//		PhotoDTO photoDTO = new PhotoDTO();
//		photoDTO.thumbUrl = "http://us.123rf.com/400wm/400/400/tr3gi/tr3gi1107/tr3gi110700001/10017592-rainbow-flower-multi-colored-petals-of-daisy-flower-isolated-on-white-background-range-of-happy-mult.jpg";
//		photoDTO.fullUrl = "http://us.123rf.com/400wm/400/400/tr3gi/tr3gi1107/tr3gi110700001/10017592-rainbow-flower-multi-colored-petals-of-daisy-flower-isolated-on-white-background-range-of-happy-mult.jpg";
//		photoDTO.createdTime = "04/07/2012 - 12:00";
//		photoDTO.mediaId = "123";
//		photoDTO.lat = 12345;
//		photoDTO.lng = 54321;
//		photoDTO.createUser = "Huỳnh Kim Chương";
//		photoDTO.userCode = "00000012345";
		
		PhotoDTO photoDTO = new PhotoDTO();
		photoDTO.thumbUrl = c.getString(c.getColumnIndex("THUMB_URL"));
		photoDTO.fullUrl = c.getString(c.getColumnIndex("URL"));
		photoDTO.createdTime = c.getString(c.getColumnIndex("CREATE_DATE"));
		photoDTO.mediaId = String.valueOf(c.getLong(c.getColumnIndex("MEDIA_ITEM_ID")));
		photoDTO.lat = c.getDouble(c.getColumnIndex("LAT"));
		photoDTO.lng = c.getDouble(c.getColumnIndex("LNG"));
		photoDTO.staffName = c.getString(c.getColumnIndex("STAFF_NAME"));
		photoDTO.staffCode = c.getString(c.getColumnIndex("CREATE_USER"));
		photoDTO.proInfoId = c.getLong(c.getColumnIndex("PRO_INFO_ID"));
		photoDTO.proInfoCode = c.getString(c.getColumnIndex("PRO_INFO_CODE"));
		photoDTO.proInfoName = c.getString(c.getColumnIndex("PRO_INFO_NAME"));
		
		return photoDTO;
	}

	/**
	 * Request upload photo
	 * @author: PhucNT
	 * @param mediaDTO
	 * @return: void
	 * @throws:
	*/
	public int updateNewLink(MediaItemDTO mediaDTO) {
		// TODO Auto-generated method stub
		ContentValues args = new ContentValues();
	    args.put(THUMB_URL,mediaDTO.thumbUrl );
	    args.put(URL,mediaDTO.url );
	    return mDB.update(TABLE_MEDIA_ITEM, args, MEDIA_ITEM_ID + "=" + mediaDTO.id, null);
	}

	/**
	*  Mo ta chuc nang cua ham
	*  @author: Nguyen Thanh Dung
	*  @param customerId
	*  @param type
	*  @param numTop
	*  @param lastMediaId
	*  @param lastCreatedTime
	*  @return
	*  @return: ArrayList<PhotoDTO>
	*  @throws: 
	*/
	
	public ArrayList<PhotoDTO> getSupervisorAlbumDetailUser(String customerId, String type, String numTop, String page, String shopId) {
		StringBuffer  sqlObject = new StringBuffer();
		sqlObject.append("SELECT mi.media_item_id                                AS MEDIA_ITEM_ID, ");
		sqlObject.append("       mi.thumb_url                                    AS THUMB_URL, ");
		sqlObject.append("       url                                          AS URL, ");
		sqlObject.append("       Strftime('%d/%m/%Y - %H:%M', MI.create_date) AS CREATE_DATE, ");
		sqlObject.append("       mi.lat                                          AS LAT, ");
		sqlObject.append("       mi.lng                                          AS LNG, ");
		sqlObject.append("       st.staff_name                                AS STAFF_NAME, ");
		sqlObject.append("       st.staff_code                                AS STAFF_CODE, ");
		sqlObject.append("       MI.create_user                               AS CREATE_USER ");
		sqlObject.append("FROM   media_item mi ");
		sqlObject.append("       LEFT JOIN staff st ");
		sqlObject.append("              ON mi.staff_id = st.staff_id ");
		sqlObject.append("WHERE  object_id = ? ");
		sqlObject.append("       AND object_type = ? ");
		sqlObject.append("       AND mi.type = 1 ");
		sqlObject.append("       AND mi.status = 1 ");
		sqlObject.append("       AND mi.media_type = 0 ");
		//add kiem tra create_date > 2 thang truoc
		sqlObject.append(" and date(mi.create_date) >=  date(?,'start of month','-2 month')");
		
		ArrayList<String> params = new ArrayList<String>();
		params.add(customerId);
		params.add(type);
		params.add(DateUtils.now());
		if (!StringUtil.isNullOrEmpty(shopId)) {
			sqlObject.append("       AND mi.shop_id = ? ");
			params.add(shopId);
		}
		
		sqlObject.append(" ORDER BY datetime(mi.create_date) desc ");
		sqlObject.append(" limit ? offset ?");
		params.add(numTop);
		int offset = Integer.parseInt(page) * Integer.parseInt(numTop);
		params.add(String.valueOf(offset));
		
		Cursor c = null;
		ArrayList<PhotoDTO> listPhoto = new ArrayList<PhotoDTO>();
		try {
			c = rawQuery(sqlObject.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				do {
					PhotoDTO photoDTO = initPhotoInfo(c);
					listPhoto.add(photoDTO);
				} while (c.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			if (c != null) {
				c.close();
			}
		}
		
		return listPhoto;
	}
	
	/**
	 * ham lay danh sach album theo chuong trinh, ngay chup
	 * @author thanhnn
	 * @param customerId
	 * @param type
	 * @param numTop
	 * @param lastMediaId
	 * @param lastCreatedTime
	 * @param programeId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public ArrayList<PhotoDTO> getAlbumDetailPrograme(String customerId, String type, String numTop,
			String page, String programeCode, String fromDate, String toDate, String shopId) {
		ArrayList<String> params = new ArrayList<String>();
		StringBuffer  sqlObject = new StringBuffer();
		sqlObject.append("SELECT mi.media_item_id                                AS MEDIA_ITEM_ID, ");
		sqlObject.append("       mi.thumb_url                                    AS THUMB_URL, ");
		sqlObject.append("       mi.url                                          AS URL, ");
		sqlObject.append("       Strftime('%d/%m/%Y - %H:%M', MI.create_date) AS CREATE_DATE, ");
		sqlObject.append("       mi.lat                                          AS LAT, ");
		sqlObject.append("       mi.lng                                          AS LNG, ");
		sqlObject.append("       st.staff_name                                AS STAFF_NAME, ");
		sqlObject.append("       st.staff_code                                AS STAFF_CODE, ");
		sqlObject.append("       MI.create_user                               AS CREATE_USER, ");
		sqlObject.append("       MI.pro_info_id                       AS DISPLAY_PROGRAM_ID ");
		sqlObject.append("FROM   media_item mi left join display_shop_map dp ON mi.pro_info_id = dp.display_program_id and dp.status = 1 ");
		sqlObject.append("       LEFT JOIN staff st ");
		sqlObject.append("              ON mi.staff_id = st.staff_id ");
		sqlObject.append("WHERE  mi.object_id = ? ");
		params.add(customerId);
		sqlObject.append("       AND mi.type = 1 ");
		sqlObject.append("       AND mi.status = 1 ");
		
		if (Integer.valueOf(type) == 4) {
			sqlObject.append("       AND mi.object_type = 4 ");
		} else {
			sqlObject.append("       AND mi.object_type in (0, 1, 2, 4) ");
		}
		sqlObject.append("       AND mi.media_type = 0 ");
		sqlObject.append(" and case when mi.object_type = 4 then dp.status = 1 else 1 end ");
		
		//add kiem tra create_date > 2 thang truoc
		sqlObject.append(" and date(mi.create_date) >=  date(?,'start of month','-2 month')");
		params.add(DateUtils.now());
		
		if (!StringUtil.isNullOrEmpty(fromDate)) {
			sqlObject.append(" and date(mi.create_date) >=  date(?)");
			params.add(fromDate);
		}
		if (!StringUtil.isNullOrEmpty(toDate)) {
			sqlObject.append(" and date(mi.create_date) <=  date(?)");
			params.add(toDate);
		}
		if (!StringUtil.isNullOrEmpty(programeCode)) {
			sqlObject.append(" and dp.display_program_code =  ?");
			params.add(programeCode);
		}
		if (!StringUtil.isNullOrEmpty(shopId)) {
			sqlObject.append(" and mi.shop_id =  ?");
			params.add(shopId);
		}
		
		
		sqlObject.append(" ORDER BY datetime(MI.create_date) desc ");
		sqlObject.append(" limit ? offset ?");
		params.add(numTop);
		int offset = Integer.parseInt(page) * Integer.parseInt(numTop);
		params.add(String.valueOf(offset));
		
		Cursor c = null;
		ArrayList<PhotoDTO> listPhoto = new ArrayList<PhotoDTO>();
		try {
			c = rawQuery(sqlObject.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				do {
					PhotoDTO photoDTO = initPhotoInfo(c);
					listPhoto.add(photoDTO);
				} while (c.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			if (c != null) {
				c.close();
			}
		}
		
		return listPhoto;
	}
	
	 /**
	  * Lay danh sach hinh anh o che do search
	  * 
	  * @author: QuangVT
	  * @since: 6:03:06 PM Dec 12, 2013
	  * @return: ArrayList<PhotoDTO>
	  * @throws:  
	  * @param customerId
	  * @param type
	  * @param numTop
	  * @param page
	  * @param programeCode
	  * @param fromDate
	  * @param toDate
	  * @param shopId
	  * @return
	  */
	public ArrayList<PhotoDTO> getAlbumDetailForSearch(String customerId, String numTop,
			String page, String fromDate, String toDate) {
		ArrayList<String> params = new ArrayList<String>();
		StringBuffer  sqlObject = new StringBuffer();
		sqlObject.append("SELECT mi.media_item_id                                AS MEDIA_ITEM_ID, ");
		sqlObject.append("       mi.thumb_url                                    AS THUMB_URL, ");
		sqlObject.append("       mi.url                                          AS URL, ");
		sqlObject.append("       Strftime('%d/%m/%Y - %H:%M', MI.create_date) AS CREATE_DATE, ");
		sqlObject.append("       mi.lat                                          AS LAT, ");
		sqlObject.append("       mi.lng                                          AS LNG, ");
		sqlObject.append("       st.staff_name                                AS STAFF_NAME, ");
		sqlObject.append("       st.staff_code                                AS STAFF_CODE, ");
		sqlObject.append("       MI.create_user                               AS CREATE_USER, ");
		sqlObject.append("       MI.pro_info_id                               AS DISPLAY_PROGRAM_ID, ");
		sqlObject.append("       mi.pro_info_id                            AS PRO_INFO_ID,  ");
		sqlObject.append("       ifnull(pi.pro_info_code,'')               AS PRO_INFO_CODE, ");
		sqlObject.append("       ifnull(pi.pro_info_name,'')               AS PRO_INFO_NAME ");
		sqlObject.append("FROM   media_item mi");
		sqlObject.append("       LEFT JOIN staff st ");
		sqlObject.append("              ON mi.staff_id = st.staff_id ");
		sqlObject.append("       LEFT JOIN pro_info pi ");
		sqlObject.append("              ON mi.pro_info_id = pi.pro_info_id ");
		sqlObject.append("WHERE  mi.object_id = ? ");
		params.add(customerId);
		sqlObject.append("       AND mi.type = 1 ");
		sqlObject.append("       AND mi.status = 1 "); 
		sqlObject.append("       AND mi.object_type in (0, 1, 2, 4) ");
		sqlObject.append("       AND mi.media_type = 0 ");
		
		//add kiem tra create_date > 2 thang truoc
		sqlObject.append(" and date(mi.create_date) >=  date(?,'start of month','-2 month')");
		params.add(DateUtils.now());
		
		if (!StringUtil.isNullOrEmpty(fromDate)) {
			sqlObject.append(" and date(mi.create_date) >=  date(?)");
			params.add(fromDate);
		}
		if (!StringUtil.isNullOrEmpty(toDate)) {
			sqlObject.append(" and date(mi.create_date) <=  date(?)");
			params.add(toDate);
		} 
		
		sqlObject.append(" ORDER BY datetime(MI.create_date) desc ");
		sqlObject.append(" limit ? offset ?");
		params.add(numTop);
		int offset = Integer.parseInt(page) * Integer.parseInt(numTop);
		params.add(String.valueOf(offset));
		
		Cursor c = null;
		ArrayList<PhotoDTO> listPhoto = new ArrayList<PhotoDTO>();
		try {
			c = rawQuery(sqlObject.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				do {
					PhotoDTO photoDTO = initPhotoInfo(c);
					listPhoto.add(photoDTO);
				} while (c.moveToNext());
			}
		} catch (Exception e) {
			VTLog.e("[Quang]", e.getMessage());
		} finally {
			if (c != null) {
				c.close();
			}
		}
		
		return listPhoto;
	}
	
	/**
	 * ham lay danh sach album theo chuong trinh, ngay chup cho chuc nang gsnpp
	 * @author thanhnn
	 * @param customerId
	 * @param type
	 * @param numTop
	 * @param lastMediaId
	 * @param lastCreatedTime
	 * @param programeId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public ArrayList<PhotoDTO> getSuperVisorAlbumDetailPrograme(String customerId, String type, String numTop,
			String page, String programeCode, String fromDate, String toDate) {
		ArrayList<String> params = new ArrayList<String>();
		StringBuffer  sqlObject = new StringBuffer();
		sqlObject.append("SELECT mi.media_item_id                                AS MEDIA_ITEM_ID, ");
		sqlObject.append("       mi.thumb_url                                    AS THUMB_URL, ");
		sqlObject.append("       mi.url                                          AS URL, ");
		sqlObject.append("       Strftime('%d/%m/%Y - %H:%M', MI.create_date) AS CREATE_DATE, ");
		sqlObject.append("       mi.lat                                          AS LAT, ");
		sqlObject.append("       mi.lng                                          AS LNG, ");
		sqlObject.append("       st.staff_name                                AS STAFF_NAME, ");
		sqlObject.append("       st.staff_code                                AS STAFF_CODE, ");
		sqlObject.append("       MI.create_user                               AS CREATE_USER ");
		sqlObject.append("FROM   media_item mi left join display_shop_map dp ON mi.pro_info_id = dp.display_program_id and dp.status = 1 ");
		sqlObject.append("       LEFT JOIN staff st ");
		sqlObject.append("              ON mi.staff_id = st.staff_id ");
		sqlObject.append("WHERE  mi.object_id = ? ");
		params.add(customerId);
		sqlObject.append("       AND mi.type = 1 ");
		sqlObject.append("       AND mi.status = 1 ");
		
		if (Integer.valueOf(type) == 4) {
			sqlObject.append("       AND mi.object_type = 4 ");
		} else {
			sqlObject.append("       AND mi.object_type in (0, 1, 2, 4) ");
		}
		sqlObject.append("       AND mi.media_type = 0 ");
		sqlObject.append(" and case when mi.object_type = 4 then dp.status = 1 else 1 end ");
		
		//add them status cho display_shop_map
		sqlObject.append(" and dp.status = 1 ");
		//add kiem tra create_date > 2 thang truoc
		sqlObject.append(" and date(mi.create_date) >=  date(?,'start of month','-2 month')");
		params.add(DateUtils.now());

		if (!StringUtil.isNullOrEmpty(fromDate)) {
			sqlObject.append(" and date(mi.create_date) >=  date(?)");
			params.add(fromDate);
		}
		if (!StringUtil.isNullOrEmpty(toDate)) {
			sqlObject.append(" and date(mi.create_date) <=  date(?)");
			params.add(toDate);
		}
		if (!StringUtil.isNullOrEmpty(programeCode)) {
			sqlObject.append(" and dp.display_program_code =  ?");
			params.add(programeCode);
		}
		
		sqlObject.append(" ORDER BY datetime(MI.create_date) desc ");
		sqlObject.append(" limit ? offset ?");
		params.add(numTop);
		int offset = Integer.parseInt(page) * Integer.parseInt(numTop);
		params.add(String.valueOf(offset));
		
		Cursor c = null;
		ArrayList<PhotoDTO> listPhoto = new ArrayList<PhotoDTO>();
		try {
			c = rawQuery(sqlObject.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				do {
					PhotoDTO photoDTO = initPhotoInfo(c);
					listPhoto.add(photoDTO);
				} while (c.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			if (c != null) {
				c.close();
			}
		}
		
		return listPhoto;
	}
	
	public ArrayList<AlbumDTO> getAlbumProgrameInfo(String programeCode, String customerId, String fromDate, String toDate, String shopId) {
		ArrayList<String> params = new ArrayList<String>();
		StringBuffer  sqlObject = new StringBuffer();
		sqlObject.append("select *, count(*) NUM_IMAGE from ( ");
		sqlObject.append("SELECT distinct mi.url, ");
		sqlObject.append("       mi.thumb_url THUMB_URL, ");
		sqlObject.append("       mi.object_type OBJECT_TYPE, ");
		sqlObject.append("       mi.pro_info_id DISPLAY_PROGRAM_ID, ");
		sqlObject.append("       dp.display_program_code DISPLAY_PROGRAM_CODE ");
		sqlObject.append("FROM   media_item mi join display_shop_map dp on mi.pro_info_id = dp.display_program_id ");
		sqlObject.append("WHERE  mi.object_id = ? ");
		params.add(customerId);
		sqlObject.append("       AND mi.object_type = 4 ");
		sqlObject.append("       AND mi.media_type = 0 ");
		sqlObject.append(" and mi.status = 1");
		sqlObject.append(" and mi.type = 1 ");
		//add them status cho display_shop_map
		sqlObject.append(" and dp.status = 1 ");
		//add kiem tra create_date > 2 thang truoc
		sqlObject.append(" and date(mi.create_date) >=  date(?,'start of month','-2 month')");
		params.add(DateUtils.now());
		
		if (!StringUtil.isNullOrEmpty(fromDate)) {
			sqlObject.append(" and date(mi.create_date) >=  date(?)");
			params.add(fromDate);
		}
		if (!StringUtil.isNullOrEmpty(toDate)) {
			sqlObject.append(" and date(mi.create_date) <=  date(?)");
			params.add(toDate);
		}
		if (!StringUtil.isNullOrEmpty(shopId)) {
			sqlObject.append(" and mi.shop_id =  ?");
			params.add(shopId);
		}
		if (!StringUtil.isNullOrEmpty(programeCode)) {
			sqlObject.append(" and dp.display_program_code =  ?");
			params.add(programeCode);
		}
		sqlObject.append(" order by dp.display_program_code, date(mi.create_date))  ");
		sqlObject.append(" GROUP  BY display_program_code ");
		
		Cursor c = null;
		ArrayList<AlbumDTO> data = new ArrayList<AlbumDTO>();
		try {
			c = rawQuery(sqlObject.toString(), params.toArray(new String[params.size()]));
			if (c != null) {
				if (c.moveToFirst()) {
					int index = 0;
					do {
						int object_type = 0;
						AlbumDTO dto = new AlbumDTO();
						data.add(dto);
						if(c.getColumnIndex("OBJECT_TYPE") >= 0){
							object_type = c.getInt(c.getColumnIndex("OBJECT_TYPE"));
						}
						dto.setAlbumType(object_type);
						data.get(index).setThumbUrl(c.getString(c.getColumnIndex(THUMB_URL)));
						data.get(index).setNumImage(c.getInt(c.getColumnIndex("NUM_IMAGE")));
						data.get(index).setDisplayProgrameId(c.getLong(c.getColumnIndex("DISPLAY_PROGRAM_ID")));
						data.get(index).setAlbumTitle(c.getString(c.getColumnIndex("DISPLAY_PROGRAM_CODE")));
						index ++;
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return data;
	}
	
	/**
	 * Xoa du lieu hinh anh luu qua 2 thang
	 * giu lai hinh anh san pham
	 * @author: banghn
	 * @return
	 */
	public long deleteOldMediaItem(){
		long result = -1;
		try {
			String startOf2MonthAgo = DateUtils
					.getFirstDateOfNumberPreviousMonthWithFormat(
							DateUtils.DATE_FORMAT_SQL_DEFAULT, -2);
			String userId = "" + GlobalInfo.getInstance().getProfile().getUserData().id;
			//mDB.beginTransaction();
			// xoa action tu thang truoc
			StringBuffer sqlDel = new StringBuffer();
			sqlDel.append(" substr(CREATE_DATE,0,11) < ?");
			sqlDel.append(" AND OBJECT_TYPE IN (0, 1, 2, 4, 5)");

			String[] params = { startOf2MonthAgo };
			
			delete(sqlDel.toString(), params);
			result = 1;
			//mDB.setTransactionSuccessful();
		} catch (Throwable e) {
			e.printStackTrace();
			result = -1;
		} finally {
//			if (mDB != null && mDB.inTransaction()) {
//				mDB.endTransaction();
//			}
			SQLUtils.getInstance().isProcessingTrans = false;
		}
		return result;
	}

}
