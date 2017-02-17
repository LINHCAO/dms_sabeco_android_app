/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

/**
 *  DTO chua du lieu cua view ListAlbumUser
 *  @author: SoaN
 *  @version: 1.0
 *  @since: Aug 2, 2012
 */

public class PhotoThumbnailListDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private AlbumDTO albumInfo;
	private String customerId;
	private String customerName;
	public boolean isFirstInit;
	
	/**
	 * 
	 */
	public PhotoThumbnailListDto() {
		setAlbumInfo(new AlbumDTO());
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @return the albumInfo
	 */
	public AlbumDTO getAlbumInfo() {
		return albumInfo;
	}

	/**
	 * @param albumInfo the albumInfo to set
	 */
	public void setAlbumInfo(AlbumDTO albumInfo) {
		this.albumInfo = albumInfo;
	}

}
