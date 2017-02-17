/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.viettel.dms.dto.db.CustomerStockHistoryDTO;
import com.viettel.viettellib.json.me.JSONArray;

/**
 * list customer display programe score
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class ListCustomerDisplayProgrameScoreDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<CustomerStockHistoryDTO> listCusDTO = new ArrayList<CustomerStockHistoryDTO>();

	// public long maxCustomerDisplayProgID = 0;

	public ListCustomerDisplayProgrameScoreDTO() {
		setListCusDTO(new ArrayList<CustomerStockHistoryDTO>());
	}

	/**
	 * @return the listCusDTO
	 */
	public List<CustomerStockHistoryDTO> getListCusDTO() {
		return listCusDTO;
	}

	/**
	 * @param listCusDTO
	 *            the listCusDTO to set
	 */
	public void setListCusDTO(List<CustomerStockHistoryDTO> listCusDTO) {
		this.listCusDTO = listCusDTO;
	}

	/**
	 * 
	 * general SQL vote display product
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: JSONArray
	 * @throws:
	 */
	public JSONArray generateNewVoteDisplayList() {
		JSONArray result = new JSONArray();
		for (int i = 0, size = listCusDTO.size(); i < size; i++) {
			CustomerStockHistoryDTO item = listCusDTO.get(i);
			result.put(item.generateCreateSqlInsertVoteDisplayProgram());
		}
		return result;
	}
}
