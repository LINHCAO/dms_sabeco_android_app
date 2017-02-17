/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

import android.database.Cursor;
/**
 * List them moi Khach Hang
 * NewCustomerListDTO.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:13:41 3 Jan 2014
 */
public class NewCustomerListDTO implements Serializable {
	private static final long serialVersionUID = 2646580985189907668L;
	public ArrayList<NewCustomerItem> cusList;
	public int totalItem;
	
	public NewCustomerListDTO() {
		totalItem = -1;
		cusList = new ArrayList<NewCustomerItem>();
	}
	
	public void AddCustomerItemFromCursor(Cursor c){
		NewCustomerItem item = new NewCustomerItem();
		item.initFromCursor(c);
		cusList.add(item);
	}
}
