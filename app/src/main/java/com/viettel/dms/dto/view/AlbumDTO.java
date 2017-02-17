/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

import com.viettel.dms.dto.photo.PhotoDTO;

/**
 *  Mo ta muc dich cua lop (interface)
 *  @author: SoaN
 *  @version: 1.0
 *  @since: Aug 2, 2012
 */

public class AlbumDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String localUrl;
	private String thumbUrl;
	private int albumType;
	private String albumTitle;
	private int numImage;
	private long displayProgrameId;
	private ArrayList<PhotoDTO> listPhoto;
	
	/**
	 * 
	 */
	public AlbumDTO() {
		setThumbUrl("");
		setAlbumType(0);
		setAlbumTitle("");
		setNumImage(0);
		setListPhoto(new ArrayList<PhotoDTO>());
	}

	/**
	 * @return the thumbUrl
	 */
	public String getThumbUrl() {
		return thumbUrl;
	}

	/**
	 * @param thumbUrl the thumbUrl to set
	 */
	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

	/**
	 * @return the albumType
	 */
	public int getAlbumType() {
		return albumType;
	}

	/**
	 * @param albumType the albumType to set
	 */
	public void setAlbumType(int albumType) {
		this.albumType = albumType;
	}

	/**
	 * @return the albumTitle
	 */
	public String getAlbumTitle() {
		return albumTitle;
	}

	/**
	 * @param albumTitle the albumTitle to set
	 */
	public void setAlbumTitle(String albumTitle) {
		this.albumTitle = albumTitle;
	}

	/**
	 * @return the numImage
	 */
	public int getNumImage() {
		return numImage;
	}

	/**
	 * @param numImage the numImage to set
	 */
	public void setNumImage(int numImage) {
		this.numImage = numImage;
	}

	/**
	 * @return the listPhoto
	 */
	public ArrayList<PhotoDTO> getListPhoto() {
		return listPhoto;
	}

	/**
	 * @param listPhoto the listPhoto to set
	 */
	public void setListPhoto(ArrayList<PhotoDTO> listPhoto) {
		this.listPhoto = listPhoto;
	}
	
	public AlbumDTO clone2()  {
		AlbumDTO obj = new AlbumDTO();
		
		obj.thumbUrl = thumbUrl;
		obj.albumType = albumType;
		obj.albumTitle = albumTitle;
		obj.numImage = numImage;
		obj.displayProgrameId = displayProgrameId;
		obj.listPhoto.addAll(listPhoto);
		
		return obj;
	}

	public long getDisplayProgrameId() {
		return displayProgrameId;
	}

	public void setDisplayProgrameId(long displayProgrameId) {
		this.displayProgrameId = displayProgrameId;
	}
}
