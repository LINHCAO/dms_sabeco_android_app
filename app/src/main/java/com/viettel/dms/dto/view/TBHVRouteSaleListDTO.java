/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

import com.viettel.dms.dto.view.TBHVRouteSupervisionDTO.STATUS;
import com.viettel.dms.dto.view.TBHVRouteSupervisionDTO.THBVRouteSupervisionItem;
import com.viettel.dms.dto.view.TBHVRouteSupervisionDTO.THBVRouteSupervisionItem.Customer;

/**
 * 
 * dto dung cho dialog Danh sach diem ban di huan luyen
 * 
 * @author: Nguyen Huu Hieu
 * @version: 1.1
 * @since: 1.0
 */
public class TBHVRouteSaleListDTO {
	public int totalList;
	public String nameGSNPP;
	public String nameNVBH;

	public ArrayList<TBHVRouteSaleListItem> arrList;

	public TBHVRouteSaleListDTO() {
		arrList = new ArrayList<TBHVRouteSaleListDTO.TBHVRouteSaleListItem>();
	}

	public TBHVRouteSaleListItem newTBHVRouteSaleLstItem() {
		return new TBHVRouteSaleListItem();
	}

	// public void addItem(Cursor c){
	// TBHVRouteSaleListItem item = new TBHVRouteSaleListItem();
	// item.khachHang = c.getString(c.getColumnIndex("khachHang"));
	// item.diaChi = c.getString(c.getColumnIndex("diaChi"));
	// item.thoiGianNVBH = c.getString(c.getColumnIndex("thoiGianNVBH"));
	// item.thuTuNVBH = c.getString(c.getColumnIndex("thuTuNVBH"));
	// item.thoiGianGSNPP = c.getString(c.getColumnIndex("thoiGianGSNPP"));
	// item.thuTuGSNPP = c.getString(c.getColumnIndex("thuTuGSNPP"));
	// arrList.add(item);
	// }

	public class TBHVRouteSaleListItem {
		public String khachHang;
		public String diaChi;
		public String thoiGianNVBH;
		public int thuTuNVBH;
		public String thoiGianGSNPP;
		public int thuTuGSNPP;
		public boolean isNVBHVisiting;// dau * NVBH
		public boolean isNPPVisiting;// dau * NPP
		public boolean isWrongPlan;// boi do

		// public String mota;

		public TBHVRouteSaleListItem() {
			khachHang = "";
			diaChi = "";
			thoiGianNVBH = "";
			thuTuNVBH = 0;
			diaChi = "";
			thoiGianGSNPP = "";
			thuTuGSNPP = 0;
		}

		public void intGsnppItem(THBVRouteSupervisionItem aRouteSupervisionItem, int i) {
			khachHang = aRouteSupervisionItem.gsnppRealSeq.get(i).codeName;
			diaChi = aRouteSupervisionItem.gsnppRealSeq.get(i).address;

			thuTuNVBH = -1;
			thoiGianNVBH = "";

			thuTuGSNPP = i + 1;
			thoiGianGSNPP = aRouteSupervisionItem.gsnppRealSeq.get(thuTuGSNPP - 1).visit_start_time;
			isNPPVisiting = aRouteSupervisionItem.gsnppRealSeq.get(i).isVisting;

			if (aRouteSupervisionItem.gsnppRealSeq.get(i).status == STATUS.GSNPP_VISITED_BUT_NVBH) {
				isWrongPlan = true;
				// mota = StringUtil.getString(R.string.TEXT_WRONG_ORDER);
			}
		}

		/**
		 * Khách hàng sai tuyến
		 * 
		 * @author: TamPQ
		 * @param nvbhCus
		 * @param gsnppCus
		 * @return: void
		 * @throws:
		 */
		public void intWrongCus(Customer nvbhCus, Customer gsnppCus) {
			if (nvbhCus != null) {
				khachHang = nvbhCus.codeName;
				diaChi = nvbhCus.address;
				if (nvbhCus.status == STATUS.NVBH_VISITED_BUT_GSNPP) {
					isWrongPlan = true;
				}
			} else if (gsnppCus != null) {
				khachHang = gsnppCus.codeName;
				diaChi = gsnppCus.address;
			}

			if (nvbhCus != null) {
				thuTuNVBH = nvbhCus.realSeq + 1;
				thoiGianNVBH = nvbhCus.visit_start_time;
				isNVBHVisiting = nvbhCus.isVisting;
			} else {
				thuTuNVBH = -1;
				thoiGianNVBH = "";
			}

			if (gsnppCus != null) {
				thuTuGSNPP = gsnppCus.realSeq + 1;
				thoiGianGSNPP = gsnppCus.visit_start_time;
				isNPPVisiting = gsnppCus.isVisting;

				if (gsnppCus.status == STATUS.WRONG_PLAN || gsnppCus.status == STATUS.GSNPP_VISITED_BUT_NVBH) {
					isWrongPlan = true;
				}
			} else {
				thuTuGSNPP = -1;
				thoiGianGSNPP = "";
			}
		}

	}

}
