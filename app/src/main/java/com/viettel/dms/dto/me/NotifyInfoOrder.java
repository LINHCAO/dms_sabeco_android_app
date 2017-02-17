/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.me;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *  Luu tru thong tin don hang tra ve realtime
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class NotifyInfoOrder implements Serializable {
	// bien dung de kiem tra co can syn du lieu tu server ve truoc khi xem ds don hang bi loi hay khong? 
	// bien nay = true co nghia la co don hang loi tra ve tu nha phan phoi ma chua update xuong tablet
	public boolean isSyncDataFromServer = false;
	// bien dung de kiem tra co don hang chua chuyen thanh cong hay khong ? - khong can luu khi bi reset
	public boolean hasOrderFail = false;
	// luu ds don hang tra ve - dung de thong bao khi chuong trinh dang inactive -- khong can luu khi bi reset
	public ArrayList<String> listOrderId = new ArrayList<String>();
	// luu ds don hang tra ve da duoc cap nhat ve client
//	public ArrayList<String> listOrderUpdateId = new ArrayList<String>();
	// luu ds don hang tra ve da duoc xu ly: da xoa, hoac cap nhat
	public ArrayList<String> listOrderProcessedId = new ArrayList<String>();
}
