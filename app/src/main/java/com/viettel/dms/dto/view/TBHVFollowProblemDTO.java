/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * dto theo doi khac phuc GST
 * 
 * @author: ThanhNN8
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVFollowProblemDTO {
	public List<TBHVFollowProblemItemDTO> list;
	public ComboboxFollowProblemDTO comboboxDTO;
	public int total = 0;
	public TBHVFollowProblemDTO() {
		list = new ArrayList<TBHVFollowProblemItemDTO>();
		list.add(new TBHVFollowProblemItemDTO());
		list.add(new TBHVFollowProblemItemDTO());
		list.add(new TBHVFollowProblemItemDTO());
		comboboxDTO = new ComboboxFollowProblemDTO();
		total = 0;
	}
}
