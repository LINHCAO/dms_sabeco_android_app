/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import android.database.Cursor;

import com.viettel.dms.constants.Constants;

/**
 * thong tin chuong trinh cho san pham
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class ProgrameForProductDTO {
	public static int PROGRAME_DISPLAY = 2;
	public static int PROGRAME_PROMOTION = 1;
	public static int PROGRAME_PROMOTION_CANCEL = 4;
	public static int PROGRAME_PROMOTION_CHANGE = 3;
	public static int PROGRAME_PROMOTION_RETURN = 5;
	public String programeCode;
	public String programeName;
	// ten cua Chuong trinh: CTTB, CTKM (huy, doi, tra, khuyen mai tay)
	public String programeTypeName;
	public int programeType;
	// check CTKM co hop le hay khong dua vao cach tinh CTKM moi. Neu la CTTB thi luon luon true
	public boolean isCTKMInvalid = false;

	public ProgrameForProductDTO() {
		programeCode = "";
		programeName = "";
		programeTypeName = "";
		programeType = 0;
	}

	/**
	 * 
	 * khoi tao object voi cursor
	 * 
	 * @author: HaiTC3
	 * @param c
	 * @return: void
	 * @throws:
	 */
	public void initPrograme(Cursor c) {
		// ma chuong trinh
		if (c.getColumnIndex("PROGRAM_CODE") >= 0) {
			programeCode = c.getString(c.getColumnIndex("PROGRAM_CODE"));
		} else {
			programeCode = "";
		}
		// ten chuong trinh
		if (c.getColumnIndex("PROGRAM_NAME") >= 0) {
			programeName = c.getString(c.getColumnIndex("PROGRAM_NAME"));
		} else {
			programeName = "";
		}
		// loai chuong trinh
		if (c.getColumnIndex("PROGRAME_TYPE") >= 0) {
			programeType = c.getInt(c.getColumnIndex("PROGRAME_TYPE"));
		} else {
			programeType = 0;
		}
		// loai CTKM (huy, doi , tra, nomal), CTTB
		String type = Constants.STR_BLANK;
		if (c.getColumnIndex("TYPE") >= 0) {
			type = c.getString(c.getColumnIndex("TYPE"));
		} else {
			type = "TB";
		}
		if(type.equals("TB")){
			isCTKMInvalid = true;
		}
		else{
			int COKHAIBAO1 = 0;
			int COKHAIBAO2 = 0;
			int COKHAIBAO3 = 0;
			int COKHAIBAO4 = 0;
			int COKHAIBAO5 = 0;
			int COKHAIBAO6 = 0;
			int COKHAIBAO7 = 0;
			int COKHAIBAO8 = 0;
			int COKHAIBAO9 = 0;
			int COKHAIBAO10 = 0;

			if (c.getColumnIndex("COKHAIBAO1") >= 0) {
				COKHAIBAO1 = c.getInt(c.getColumnIndex("COKHAIBAO1"));
			}
			if (c.getColumnIndex("COKHAIBAO2") >= 0) {
				COKHAIBAO2 = c.getInt(c.getColumnIndex("COKHAIBAO2"));
			}
			if (c.getColumnIndex("COKHAIBAO3") >= 0) {
				COKHAIBAO3 = c.getInt(c.getColumnIndex("COKHAIBAO3"));
			}
			if (c.getColumnIndex("COKHAIBAO4") >= 0) {
				COKHAIBAO4 = c.getInt(c.getColumnIndex("COKHAIBAO4"));
			}
			if (c.getColumnIndex("COKHAIBAO5") >= 0) {
				COKHAIBAO5 = c.getInt(c.getColumnIndex("COKHAIBAO5"));
			}
			if (c.getColumnIndex("COKHAIBAO6") >= 0) {
				COKHAIBAO6 = c.getInt(c.getColumnIndex("COKHAIBAO6"));
			}
			if (c.getColumnIndex("COKHAIBAO7") >= 0) {
				COKHAIBAO7 = c.getInt(c.getColumnIndex("COKHAIBAO7"));
			}
			if (c.getColumnIndex("COKHAIBAO8") >= 0) {
				COKHAIBAO8 = c.getInt(c.getColumnIndex("COKHAIBAO8"));
			}
			if (c.getColumnIndex("COKHAIBAO9") >= 0) {
				COKHAIBAO9 = c.getInt(c.getColumnIndex("COKHAIBAO9"));
			}
			if (c.getColumnIndex("COKHAIBAO10") >= 0) {
				COKHAIBAO10 = c.getInt(c.getColumnIndex("COKHAIBAO10"));
			}
			boolean result = false;
			if (COKHAIBAO1 == 0) {
				result = true;
			} else {
				result = COKHAIBAO2 > 0 ? true : false;
			}
			if (result) {
				if (COKHAIBAO3 == 0) {
					result = true;
				} else {
					result = COKHAIBAO4 > 0 ? true : false;
				}
			} else {
				isCTKMInvalid = false;
			}
			if (result) {
				boolean result1 = false;
				if (COKHAIBAO5 == 0) {
					result1 = true;
				} else {
					result1 = COKHAIBAO6 == COKHAIBAO5 ? true : false;
				}

				boolean result2 = false;
				if (COKHAIBAO7 == 0) {
					result2 = true;
				} else {
					result2 = COKHAIBAO8 == COKHAIBAO7 ? true : false;
				}
				
				boolean result3 = false;
				if (COKHAIBAO9 == 0) {
					result3 = true;
				} else {
					result3 = COKHAIBAO10 > 0 ? true : false;
				}
				result = result1 && result2 && result3;
			} else {
				isCTKMInvalid = false;
			}
			isCTKMInvalid = result;
		}
		if (type.equals("ZH")) {
			programeType = PROGRAME_PROMOTION_CANCEL;
		} else if (type.equals("ZD")) {
			programeType = PROGRAME_PROMOTION_CHANGE;
		} else if (type.equals("ZT")) {
			programeType = PROGRAME_PROMOTION_RETURN;
		}
		if (c.getColumnIndex("AP_PARAM_NAME") >= 0) {
			programeTypeName = c.getString(c.getColumnIndex("AP_PARAM_NAME"));
		} else {
			programeTypeName = "";
		}
	}
}
