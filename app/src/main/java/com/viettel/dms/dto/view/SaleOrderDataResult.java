/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

/**
 *  Ket qua khi tao moi don hang
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class SaleOrderDataResult {
	public boolean isCreateSqlLiteSuccess = false;
	public long orderId;
	public ArrayList<Long> listOrderDetailId = new ArrayList<Long>();
}
