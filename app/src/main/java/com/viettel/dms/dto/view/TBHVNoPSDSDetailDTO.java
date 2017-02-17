/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.viettel.dms.dto.db.ShopDTO;

/**
 * Danh sach Shop, GSBH, NVBH cua GST
 * 
 * TBHVNoPSDSDetailDTO.java
 * @author: QuangVT
 * @version: 1.0 
 * @since:  6:01:53 PM Dec 17, 2013
 */
public class TBHVNoPSDSDetailDTO implements Serializable{  
	private static final long serialVersionUID = 1L;
	
	public List<ShopDTO> listNPP;	// danh sach NPP cua giam sat tinh
	public ListStaffDTO listGSBH;	// danh sach GS cua giam sat tinh
	public ListStaffDTO listNVBH;	// danh sach nhan vien ban hang cua GST
	
	/**
	 * Ham khoi tao
	 */
	public TBHVNoPSDSDetailDTO() {
		listNPP  = new ArrayList<ShopDTO>();
		listGSBH = new ListStaffDTO();
		listNVBH = new ListStaffDTO();
	} 
}
