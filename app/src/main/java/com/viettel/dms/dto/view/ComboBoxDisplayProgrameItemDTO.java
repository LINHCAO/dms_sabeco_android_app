/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;

/**
 * 
 * Mo ta cho class
 * 
 * @author: ThanhNN8
 * @version: 1.0
 * @since: 1.0
 */
public class ComboBoxDisplayProgrameItemDTO extends AbstractTableDTO {
	private static final long serialVersionUID = 1L;
	public String id;
	public String value;
	public String name;

	public ComboBoxDisplayProgrameItemDTO(){
		super(TableType.PRO_STRUCTURE_TABLE);
	}
	public void initFromCursorLevel(Cursor c) {
		if (c.getColumnIndex("LEVEL_NAME") >= 0) {
			name = c.getString(c.getColumnIndex("LEVEL_NAME"));
		} else {
			name = "";
		}
	}

	@Override
	public ComboBoxDisplayProgrameItemDTO clone() {
		ComboBoxDisplayProgrameItemDTO dto = new ComboBoxDisplayProgrameItemDTO();
		dto.id = id;
		dto.value = value;
		dto.name = name;
		return dto;
	}
}
