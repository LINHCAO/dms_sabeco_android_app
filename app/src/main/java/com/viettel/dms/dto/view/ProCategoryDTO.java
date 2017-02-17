/**
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.dto.view;

import android.database.Cursor;

/**
 * Mo ta class hoac interface
 *
 * @author: hoanpd1
 * @version: 1.0 
 * @since:  18:59:26 24-07-2014
 */
public class ProCategoryDTO {
	public long id;
	public long parentId;
	public String name;
	
	public void initFromCursorProCate(Cursor c) {
		if (c.getColumnIndex("PRO_CATEGORY_ID") >= 0) {
			id = c.getLong(c.getColumnIndex("PRO_CATEGORY_ID"));
		} else {
			id = 0;
		}
		if (c.getColumnIndex("PARENT_PRO_CATEGORY_ID") >= 0) {
			parentId = c.getLong(c.getColumnIndex("PARENT_PRO_CATEGORY_ID"));
		} else {
			parentId = 0;
		}
		if (c.getColumnIndex("PRO_CATEGORY_NAME") >= 0) {
			name = c.getString(c.getColumnIndex("PRO_CATEGORY_NAME"));
		} else {
			name = "";
		}
	}
}
