/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import android.database.Cursor;

import com.viettel.dms.dto.db.ShopParamDTO;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.map.dto.LatLng;
import com.viettel.utils.VTLog;

/**
 * TBHV thong tin bao cao cham cong ngay cua GSNPP
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
@SuppressWarnings("serial")
public class TBHVAttendanceDTO implements Serializable {
	public ArrayList<TVBHAttendanceItem> arrGsnppList = new ArrayList<TBHVAttendanceDTO.TVBHAttendanceItem>();
	public ArrayList<NvbhLog> arrNvbhList = new ArrayList<NvbhLog>();
	public Hashtable<String, ShopParamDTO> listParam = new Hashtable<String, ShopParamDTO>();

	public TVBHAttendanceItem newTVBHAttendanceItem() {
		return new TVBHAttendanceItem();
	}

	public NvbhLog newNvbhLog() {
		return new NvbhLog();
	}

	public class TVBHAttendanceItem {
		public String nvbhShopCode;
		public int gsnppStaffId;
		public String gsnppStaffCode;
		public String gsnppStaffName;
		public int numCus;
		public int lateArrivedNPP;
		public int notArrivedNPP;
		public int nvbhShopId;
		public boolean hasPosition;
		public LatLng latLng;
		
		public TVBHAttendanceItem(){
			latLng= new LatLng();
		}

		/**
		 * init data with cursor
		 * 
		 * @param c
		 * @return: void
		 * @throws:
		 * @author: HaiTC3
		 * @date: Jan 22, 2013
		 */
		public void initObjectWithCursor(Cursor c) {
			try {
				if (c == null) {
					throw new Exception("Cursor is empty");
				}
				if (c.getColumnIndex("GS_STAFF_ID") > -1) {
					gsnppStaffId = c.getInt(c.getColumnIndex("GS_STAFF_ID"));
				}
				if (c.getColumnIndex("GS_STAFF_CODE") > -1) {
					gsnppStaffCode = c.getString(c.getColumnIndex("GS_STAFF_CODE"));
				}
				if (c.getColumnIndex("GS_STAFF_NAME") > -1) {
					gsnppStaffName = c.getString(c.getColumnIndex("GS_STAFF_NAME"));
				}
				if (c.getColumnIndex("NVBH_SHOP_CODE") > -1) {
					nvbhShopCode = c.getString(c.getColumnIndex("NVBH_SHOP_CODE"));
				}
				if (c.getColumnIndex("NVBH_SHOP_ID") > -1) {
					nvbhShopId = c.getInt(c.getColumnIndex("NVBH_SHOP_ID"));
				}
				if (c.getColumnIndex("NUM_NVBH") > -1) {
					numCus = c.getInt(c.getColumnIndex("NUM_NVBH"));
				}
				if (c.getColumnIndex("LAT") > -1) {
					latLng.lat= c.getDouble(c.getColumnIndex("LAT"));
				}
				if (c.getColumnIndex("LNG") > -1) {
					latLng.lng = c.getDouble(c.getColumnIndex("LNG"));
				}
//				//+1 them chinh gsnpp do
//				if (c.getColumnIndex("NUM_NVBH") > -1) {
//					numCus = c.getInt(c.getColumnIndex("NUM_NVBH")) + 1;
//				}
				notArrivedNPP = numCus;
			} catch (Exception e) {
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			}
		}
	}

	public class NvbhLog {
		public int nvbhStaffId;
		public int gsnppStaffId;
		// public int shopDistance;
		public ArrayList<LatLng> latLngList = new ArrayList<LatLng>();
		public ArrayList<LatLng> latLngList815 = new ArrayList<LatLng>();
		public LatLng nppLatLng;
		public boolean isValidArrival;
		public boolean isLateArrival;
		public int nvbhShopId;

		public void initCursor(Cursor c, String ccDistance) {
			try {
				if (c == null) {
					throw new Exception("Cursor is empty");
				}
				if (c.getColumnIndex("STAFF_ID") > -1) {
					nvbhStaffId = c.getInt(c.getColumnIndex("STAFF_ID"));
				}
				if (c.getColumnIndex("NPP_STAFF_ID") > -1) {
					gsnppStaffId = c.getInt(c.getColumnIndex("NPP_STAFF_ID"));
				}
				if (c.getColumnIndex("SHOP_ID") > -1) {
					nvbhShopId = c.getInt(c.getColumnIndex("SHOP_ID"));
				}
				// if (c.getColumnIndex("CC_DISTANCE_VALUE") > -1) {
				// shopDistance =
				// c.getInt(c.getColumnIndex("CC_DISTANCE_VALUE"));
				// }
				double nppLat = 0;
				if (c.getColumnIndex("SHOP_LAT") > -1) {
					nppLat = c.getDouble(c.getColumnIndex("SHOP_LAT"));
				}
				double nppLng = 0;
				if (c.getColumnIndex("SHOP_LNG") > -1) {
					nppLng = c.getDouble(c.getColumnIndex("SHOP_LNG"));
				}
				nppLatLng = new LatLng(nppLat, nppLng);

				String latListStr = null;
				String lngListStr = null;
				String latList815Str = null;
				String lngList815Str = null;
				if (c.getColumnIndex("LAT_LIST") > -1) {
					latListStr = c.getString(c.getColumnIndex("LAT_LIST"));
					if (latListStr == null) {
						latListStr = "";
					}
				}
				if (c.getColumnIndex("LNG_LIST") > -1) {
					lngListStr = c.getString(c.getColumnIndex("LNG_LIST"));
					if (lngListStr == null) {
						lngListStr = "";
					}
				}
				if (c.getColumnIndex("LAT_LIST_815") > -1) {
					latList815Str = c.getString(c.getColumnIndex("LAT_LIST_815"));
					if (latList815Str == null) {
						latList815Str = "";
					}
				}
				if (c.getColumnIndex("LNG_LIST_815") > -1) {
					lngList815Str = c.getString(c.getColumnIndex("LNG_LIST_815"));
					if (lngList815Str == null) {
						lngList815Str = "";
					}
				}
				String[] latList = latListStr.split(",");
				String[] lngList = lngListStr.split(",");
				String[] latList815 = latList815Str.split(",");
				String[] lngList815 = lngList815Str.split(",");
				for (int i = 0; i < latList.length; i++) {
					LatLng latLng = new LatLng();
					if (!StringUtil.isNullOrEmpty(latList[i])) {
						latLng.lat = Double.parseDouble(latList[i]);
						latLng.lng = Double.parseDouble(lngList[i]);
					}
					latLngList.add(latLng);
				}
				if(!StringUtil.isNullOrEmpty(ccDistance)) {
					for (int i = 0; i < latLngList.size(); i++) {
						double distance = GlobalUtil.getDistanceBetween(nppLatLng, latLngList.get(i));
						if (distance <= Double.parseDouble(ccDistance)) {
							isValidArrival = true;
							break;
						}
					}
				}
				for (int i = 0; i < latList815.length; i++) {
					LatLng latLng = new LatLng();
					if (!StringUtil.isNullOrEmpty(latList815[i])) {
						latLng.lat = Double.parseDouble(latList815[i]);
						latLng.lng = Double.parseDouble(lngList815[i]);
					}
					latLngList815.add(latLng);
				}
				// neu chua toi trong khoang quy dinh thi check tiep toi muon
				// gio
				if (!isValidArrival && !StringUtil.isNullOrEmpty(ccDistance)){
					for (int i = 0; i < latLngList815.size(); i++) {
						double distance = GlobalUtil.getDistanceBetween(nppLatLng, latLngList815.get(i));
						if (distance <= Double.parseDouble(ccDistance)) {
							isLateArrival = true;
							break;
						}
					}
				}

			} catch (Exception e) {
			}
		}
	}

	/**
	 * Tính số lượng nhân viên sai quy định
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public void processCount() {
		for (TVBHAttendanceItem attendanceItem: arrGsnppList) {
			if(attendanceItem.latLng.lat()>0 && attendanceItem.latLng.lng()>0){
				attendanceItem.hasPosition=true;
			}
			for (NvbhLog nvbhLog: arrNvbhList) {
				if (attendanceItem.gsnppStaffId == nvbhLog.gsnppStaffId
						&& attendanceItem.nvbhShopId == nvbhLog.nvbhShopId) {
					if (nvbhLog.isValidArrival) {
						attendanceItem.notArrivedNPP--;
					}
					if (nvbhLog.isLateArrival) {
						attendanceItem.lateArrivedNPP++;
					}
				}
			}
		}
	}
}
