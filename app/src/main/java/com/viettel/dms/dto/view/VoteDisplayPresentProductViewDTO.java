/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Thong tin chuong trinh trung bay cua 1 khach hang
 *  - 1 DTO chua danh sach cach chuong trinh trung bay
 *  - Moi item trong danh sach chua thong tin chi tiet 1 chuong trinh trong do co danh sach san pham
 * 
 * VoteDisplayPresentProductViewDTO.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  14:02:44 09-05-2014
 */
public class VoteDisplayPresentProductViewDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	
	// Danh sach chuong trinh trung bay
	public List<DisplayPresentProductInfo> listDisplayProgrameInfo; 
		
	public VoteDisplayPresentProductViewDTO(){  
		listDisplayProgrameInfo = new ArrayList<DisplayPresentProductInfo>();
	}
}
