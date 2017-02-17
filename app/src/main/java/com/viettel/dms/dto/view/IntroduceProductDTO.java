/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.List;

import com.viettel.dms.dto.db.MediaItemDTO;

/**
 * 
 * dto cho man hinh gioi thieu san pham
 * 
 * @author: ThanhNN8
 * @version: 1.0
 * @since: 1.0
 */
public class IntroduceProductDTO {
	private String htmlContextIntroduce;
	private List<MediaItemDTO> listMedia;
	/**
	 * @return the htmlContextIntroduce
	 */
	public String getHtmlContextIntroduce() {
		return htmlContextIntroduce;
	}
	/**
	 * @param htmlContextIntroduce the htmlContextIntroduce to set
	 */
	public void setHtmlContextIntroduce(String htmlContextIntroduce) {
		this.htmlContextIntroduce = htmlContextIntroduce;
	}
	/**
	 * @return the listMedia
	 */
	public List<MediaItemDTO> getListMedia() {
		return listMedia;
	}
	/**
	 * @param listMedia the listMedia to set
	 */
	public void setListMedia(List<MediaItemDTO> listMedia) {
		this.listMedia = listMedia;
	}
	
}
