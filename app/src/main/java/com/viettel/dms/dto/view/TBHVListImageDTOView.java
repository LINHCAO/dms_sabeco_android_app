/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * DTO man hinh ds hinh anh cua GS NPP
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class TBHVListImageDTOView implements Serializable {
	public int totalItems;
	public ArrayList<ImageInfoShopDTO> listData = new ArrayList<ImageInfoShopDTO>();
	
	
}
