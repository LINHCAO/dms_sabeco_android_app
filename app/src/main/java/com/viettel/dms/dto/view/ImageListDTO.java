/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;
import java.util.List;

import com.viettel.dms.dto.db.DisplayProgrameDTO;

/**
 * Image List DTO class
 * ImageListDTO.java
 * @version: 1.0 
 * @since:  10:29:44 8 Jan 2014
 */
public class ImageListDTO {	
	public int totalCustomer;
	public ArrayList<ImageListItemDTO> listItem;
	public List<DisplayProgrameDTO> listPrograme;
	public ListStaffDTO staffList;
	
	public ImageListDTO(){
		listItem = new ArrayList<ImageListItemDTO>();
		totalCustomer=0;
		listPrograme = new ArrayList<DisplayProgrameDTO>();
	}
	
	public void addItem(ImageListItemDTO c) {
		listItem.add(c);
	}
	public ImageListItemDTO newImageListRowDTO() {
		return new ImageListItemDTO();
	}
}
