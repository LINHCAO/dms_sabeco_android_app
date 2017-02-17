/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.photo;

import java.io.Serializable;



/**
 *  Thong tin cua hinh anh
 *  @author: HaiTC
 *  @version: 1.0
 *  @since: 1.0
 */
public class PhotoDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String mediaId;
	public String thumbUrl;
	public String fullUrl;
	public String createdTime;
	public double lat;
	public double lng;
	public String createUser;
	public String staffName;
	public String staffCode;
	public String localUrl;
	public long proInfoId;
	public String proInfoCode;
	public String proInfoName;
	public PhotoDTO(){
		mediaId = "";
		thumbUrl = "";
		fullUrl="";
		createdTime="";
		lat = 0;
		lng = 0;
		staffName = "";
		proInfoId = 0;
		proInfoCode = "";
		proInfoName = "";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return mediaId==((PhotoDTO)o).mediaId;
	}
}
