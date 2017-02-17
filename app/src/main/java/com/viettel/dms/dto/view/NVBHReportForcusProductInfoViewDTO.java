/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * thong tin bao cao san pham mat hang trong tam cua NVBH
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class NVBHReportForcusProductInfoViewDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// so ngay ban hang theo ke hoach
	public int numberDayPlan;
	// so ngay ban hang da qua
	public int numberDaySold;
	// tien do chuan
	public float progressSold;
	// danh sach mat hang trong tam
	public ArrayList<ForcusProductOfNVBHDTO> listForcusProduct = new ArrayList<ForcusProductOfNVBHDTO>();
	// danh sach thong ke tong tung loai mat hang trong tam
	public ArrayList<ForcusProductOfNVBHDTO> listTotalForcusProduct = new ArrayList<ForcusProductOfNVBHDTO>();

	public NVBHReportForcusProductInfoViewDTO() {
		numberDayPlan = 0;
		numberDaySold = 0;
		progressSold = 0;
		listForcusProduct = new ArrayList<ForcusProductOfNVBHDTO>();
		listTotalForcusProduct = new ArrayList<ForcusProductOfNVBHDTO>();
	}
}
