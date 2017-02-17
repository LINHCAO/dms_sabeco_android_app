/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

import java.util.ArrayList;

/**
 * Mo ta muc dich cua class
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
public class ROUTING_TABLE extends ABSTRACT_TABLE {
	// ID bảng
	public static final String ROUTING_ID = "ROUTING_ID";
	// Mã tuyến
	public static final String ROUTING_CODE = "ROUTING_CODE";
	// Tên tuyến
	public static final String ROUTING_NAME = "ROUTING_NAME";
	// ID NPP
	public static final String SHOP_ID = "SHOP_ID";
	// 0: ngung hoat dong, 1:hoat dong
	public static final String STATUS = "STATUS";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// ngay update
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";

	public static final String ROUTING_TABLE = "ROUTING_TABLE";

	public ROUTING_TABLE() {
		this.tableName = ROUTING_TABLE;
		this.columns = new String[] { ROUTING_ID, ROUTING_CODE, ROUTING_NAME, SHOP_ID, STATUS, CREATE_DATE,
				CREATE_USER, UPDATE_DATE, UPDATE_USER, SYN_STATE };

		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = SQLUtils.getInstance().getmDB();
	}

	public ROUTING_TABLE(SQLiteDatabase mDB) {
		this.tableName = ROUTING_TABLE;
		this.columns = new String[] { ROUTING_ID, ROUTING_CODE, ROUTING_NAME, SHOP_ID, STATUS, CREATE_DATE,
				CREATE_USER, UPDATE_DATE, UPDATE_USER, SYN_STATE };

		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;
	}

	// lay routingId cua staff thuoc ve
	public long getRoutingIdByStaffId(long staffId) {
		long result = 0;
		String dateNow = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		StringBuffer sqlObject = new StringBuffer();
		ArrayList<String> paramsObject = new ArrayList<String>();
		sqlObject.append("	SELECT	");
		sqlObject.append("	    r.routing_id ROUTING_ID	");
		sqlObject.append("	FROM	");
		sqlObject.append("	    routing r,	");
		sqlObject.append("	    visit_plan vp	");
		sqlObject.append("	WHERE	");
		sqlObject.append("	    r.routing_id = vp.routing_id	");
		sqlObject.append("	    AND r.status = 1	");
		sqlObject.append("	    AND vp.status = 1	");
		sqlObject.append("	    AND vp.from_date <= substr(?,0,11)	");
		paramsObject.add(dateNow);
		sqlObject.append("	    AND (	");
		sqlObject.append("	        vp.to_date is null	");
		sqlObject.append("	        OR date(vp.to_date) >= substr(?,0,11)	");
		paramsObject.add(dateNow);
		sqlObject.append("	    )	");
		sqlObject.append("	    AND vp.staff_id = ? limit 1	");
		paramsObject.add(String.valueOf(staffId));
		Cursor c = null;
		try {
			c = rawQueries(sqlObject.toString(), paramsObject);
			if (c != null) {
				if (c.moveToFirst()) {
					result = c.getLong(c.getColumnIndex("ROUTING_ID"));
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	protected long insert(AbstractTableDTO dto) {
		return 0;
	}

	@Override
	protected long update(AbstractTableDTO dto) {
		return 0;
	}

	@Override
	protected long delete(AbstractTableDTO dto) {
		return 0;
	}

}
