/**
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.view.ComboBoxDisplayProgrameItemDTO;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * Mo ta class hoac interface
 *
 * @author: hoanpd1
 * @version: 1.0 
 * @since:  10:43:10 24-07-2014
 */
public class PRO_STRUCTURE_TABLE extends ABSTRACT_TABLE {
	public static final String PRO_STRUCTURE_ID ="PRO_STRUCTURE_ID";
	public static final String PRO_INFO_ID  ="PRO_INFO_ID";
	public static final String TYPE ="TYPE";
	public static final String QUOTA ="QUOTA";
	public static final String TOTAL_LEVEL ="TOTAL_LEVEL";
	public static final String QUANLIFIED_QUANTITY ="QUANLIFIED_QUANTITY";
	public static final String CREATE_DATE ="CREATE_DATE";
	public static final String CREATE_USER ="CREATE_USER";
	public static final String UPDATE_DATE ="UPDATE_DATE";
	public static final String UPDATE_USER ="UPDATE_USER";
	public static final String TYPE_LEVEL ="TYPE_LEVEL";
	public static final String QUANTITY_TYPE ="QUANTITY_TYPE";
	private static final String TABLE_NAME = "PRO_STRUCTURE_TABLE";
	
	public PRO_STRUCTURE_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_NAME;
		this.columns = new String[] { PRO_STRUCTURE_ID, PRO_INFO_ID,
				TYPE, QUOTA, TOTAL_LEVEL, QUANLIFIED_QUANTITY, CREATE_DATE,
				CREATE_USER, UPDATE_DATE, UPDATE_USER,
				TYPE_LEVEL, QUANTITY_TYPE, SYN_STATE };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;

	}

	@Override
	protected long insert(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected long delete(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public List<ComboBoxDisplayProgrameItemDTO> getListLevel(String pro_info_id) { 
		List<ComboBoxDisplayProgrameItemDTO> data  = null;
		Cursor c = null; 
		ArrayList<String> params = new ArrayList<String>();
		StringBuffer  var1 = new StringBuffer();
		var1.append("SELECT DISTINCT( psd.[level_name] ) LEVEL_NAME ");
		var1.append("FROM   pro_structure ps, ");
		var1.append("       pro_structure_detail psd ");
		var1.append("WHERE  ps.[pro_info_id] = ? ");
		params.add(pro_info_id);
		var1.append("       AND ps.[pro_structure_id] = psd.[pro_structure_id] ");
		var1.append("ORDER  BY psd.[level_number] ");
		
		try { 
			c = rawQueries(var1.toString(), params);
			if (c != null) { 
				data = new ArrayList<ComboBoxDisplayProgrameItemDTO>();
				if (c.moveToFirst()) {  
					do{
						ComboBoxDisplayProgrameItemDTO dto = new ComboBoxDisplayProgrameItemDTO();
						dto.initFromCursorLevel(c);
						data.add(dto);
					}while(c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			try {
				if (c != null) {
					c.close();
				} 
			} catch (Exception e2) { 
			}
		}
		return data;
	}
}
