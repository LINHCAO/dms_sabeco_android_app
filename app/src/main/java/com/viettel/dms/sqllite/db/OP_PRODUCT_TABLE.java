/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import com.viettel.dms.dto.db.AbstractTableDTO;

/**
 * @author dungdq3
 *
 */
public class OP_PRODUCT_TABLE extends ABSTRACT_TABLE {

	// id doi thu
	public static final String OPPONENT_ID = "OPPONENT_ID";
	// id doi thu
	public static final String OP_PRODUCT_ID = "OP_PRODUCT_ID";
	// ma san pham doi thu
	public static final String PRODUCT_CODE = "PRODUCT_CODE";
	// ten san pham doi thu
	public static final String PRODUCT_NAME = "PRODUCT_NAME";
	//trang thai
	public static final String STATUS = "STATUS";
	// đơn vị lẽ
	public static final String UOM1 = "UOM1";
	// đơn vị package
	public static final String UOM2 = "UOM2";
	// quy cach
	public static final String CONVFACT = "CONVFACT";
	
	public static final String OP_PRODUCT_TABLE = "OP_PRODUCT";
	
	/* (non-Javadoc)
	 * @see com.viettel.dms.sqllite.db.ABSTRACT_TABLE#insert(com.viettel.dms.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long insert(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.viettel.dms.sqllite.db.ABSTRACT_TABLE#update(com.viettel.dms.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.viettel.dms.sqllite.db.ABSTRACT_TABLE#delete(com.viettel.dms.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long delete(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}
}
