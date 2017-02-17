/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *  DTO MH Cham cong cua TT tiep thi
 *  @author: Nguyen Thanh Dung
 *  @version: 1.0
 *  @since: 
 */

public class PGTakeAttendanceViewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	public ArrayList<PGTakeAttendanceDTO> listPG = new ArrayList<PGTakeAttendanceDTO>();
}
