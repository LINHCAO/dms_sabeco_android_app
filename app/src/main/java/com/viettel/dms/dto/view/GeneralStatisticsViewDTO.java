/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.viettel.dms.dto.view.GeneralStatisticsTNPGViewDTO.CustomerInfo;
import com.viettel.dms.util.StringUtil;
/**
 * Info for GeneralStatisticsView, include info of GeneralStatisticsExView
 * @author duongdt3
 *
 */
public class GeneralStatisticsViewDTO implements Serializable {

	private static final long serialVersionUID = 8763285450370570689L;
	//info numberDayPlan, numberDaySold, progressSold
	public ProgressSoldViewDTO progressSoldInfo; 
	
	public List<GeneralStatisticsInfo> listInfo;
	public CustomerInfo customerInfo;
	
	public GeneralStatisticsViewDTO(){
		customerInfo = new CustomerInfo();
		progressSoldInfo = new ProgressSoldViewDTO();
		listInfo = new ArrayList<GeneralStatisticsViewDTO.GeneralStatisticsInfo>();
	}
	
	public static class GeneralStatisticsInfo implements Serializable{
		private static final long serialVersionUID = 3511409596076368587L;
		//info of DTO
		public String tvObjectId;
		public String tvObjectCode;
		public String tvObjectName;
		
		public String tvStaffMobile;
		public String tvDVT;
		public int tvConvfact;
		public long tvKeHoach;
		public long tvThucHien;
		public long tvConLai;
		public long tvKeHoachThang;
		public long tvThucHienThang;
		public long tvConLaiThang;
		public float tvTienDoThang;
		
		//for View
		public int tvSTT;
		public boolean isHightLight;
		//shop info
		public String tvShopId;
		public String tvShopName;
		public String tvShopCode;
		
		public GeneralStatisticsInfo() {
			tvObjectId  ="";
			tvObjectCode = "";
			tvObjectName = "";
			tvStaffMobile = "";
			tvDVT = "";
			tvConvfact = 0;
			tvKeHoach = 0;
			tvThucHien = 0;
			tvConLai = 0;
			tvKeHoachThang = 0;
			tvThucHienThang = 0;
			tvConLaiThang = 0;
			tvTienDoThang = 0f;
			
			//shop info 
			tvShopId = "";
			tvShopName = "";
			tvShopCode = "";
			
			//info View
			tvSTT = 0;
			isHightLight = false;
		}
		
		/**
		 * Init thong tin chung
		 * @author: duongdt3
		 * @since: 16:03:12 27 Nov 2013
		 * @return: void
		 * @throws:  
		 * @param c
		 */
		private void innit(Cursor c){
			tvObjectId = String.valueOf(StringUtil.getLongFromSQliteCursor(c, "OBJECT_ID")) ;
			tvObjectCode = StringUtil.getStringFromSQliteCursor(c, "OBJECT_CODE");
			tvObjectName = StringUtil.getStringFromSQliteCursor(c, "OBJECT_NAME");
			tvStaffMobile = StringUtil.getStringFromSQliteCursor(c, "STAFF_MOBILE");
			tvDVT = StringUtil.getStringFromSQliteCursor(c, "DVT");
			
			//get shop info
			tvShopId = String.valueOf(StringUtil.getLongFromSQliteCursor(c, "SHOP_ID"));
			tvShopCode = StringUtil.getStringFromSQliteCursor(c, "SHOP_CODE");
			tvShopName = StringUtil.getStringFromSQliteCursor(c, "SHOP_NAME");
		}
		
		/**
		 * Init du lieu cho Saleman
		 * @author: duongdt3
		 * @since: 16:03:19 27 Nov 2013
		 * @return: void
		 * @throws:  
		 * @param c
		 */
		public void initCursorForSaleman(Cursor c){
			innit(c);
			//o	Kế hoạch = Chỉ tiêu tháng
			tvKeHoachThang = StringUtil.getLongFromSQliteCursor(c, "MONTH_PLAN");
			tvThucHienThang = StringUtil.getLongFromSQliteCursor(c, "SALE_IN_MONTH");
			
			//cal remain month
			tvConLaiThang = StringUtil.calRemain(tvKeHoachThang, tvThucHienThang);
			//cal progress
			tvTienDoThang = StringUtil.calProgress(tvKeHoachThang, tvThucHienThang, false);
			
			tvKeHoach = StringUtil.getLongFromSQliteCursor(c, "DATE_PLAN");
			tvThucHien = StringUtil.getLongFromSQliteCursor(c, "SALE_IN_DATE");
			
			//cal remain day
			tvConLai = StringUtil.calRemain(tvKeHoach, tvThucHien);
		}
		
		/**
		 * Init du lieu cho PG
		 * @author: duongdt3
		 * @since: 16:03:31 27 Nov 2013
		 * @return: void
		 * @throws:  
		 * @param c
		 * @param date 
		 */
		public void initCursorForPG(Cursor c){
			innit(c);
			
			String monthPlanColName = "MONTH_PLAN";
			String datePlanColName = "DATE_PLAN";
			//ngay request trong thang hien tai
			int isRequestInMonth = (int) StringUtil.getLongFromSQliteCursor(c, "IS_DATE_REQUEST_IN_MONTH");
			
			//doi lai cot PLAN neu trong thang hien tai (Truong hop thay doi PLAN trong thang)
			if (isRequestInMonth == 1) {
				monthPlanColName = "MONTH_PLAN_LAST";
				datePlanColName = "DATE_PLAN_LAST";
			}
						
			tvConvfact = (int) StringUtil.getLongFromSQliteCursor(c, "CONVFACT");
			//o	Kế hoạch = Chỉ tiêu tháng
			//doi voi PG thì KH -> Thùng => KH * Convfact = so le
			tvKeHoachThang = StringUtil.getLongFromSQliteCursor(c, monthPlanColName) * tvConvfact;
			//thuc hien la so le
			tvThucHienThang = StringUtil.getLongFromSQliteCursor(c, "PG_SALE_IN_MONTH");
			
			//cal remain month
			tvConLaiThang = StringUtil.calRemain(tvKeHoachThang, tvThucHienThang);
			//cal progress
			tvTienDoThang = StringUtil.calProgress(tvKeHoachThang, tvThucHienThang, false);
			
			//doi voi PG thì KH -> Thùng => KH * Convfact = so le
			tvKeHoach = StringUtil.getLongFromSQliteCursor(c, datePlanColName)  * tvConvfact ;
			tvThucHien = StringUtil.getLongFromSQliteCursor(c, "PG_SALE_IN_DATE");
			tvConLai = StringUtil.calRemain(tvKeHoach, tvThucHien);
		}
		
		public void calTotal(GeneralStatisticsInfo dtoAdd){
			tvKeHoach += dtoAdd.tvKeHoach;
			tvThucHien += dtoAdd.tvThucHien;
			//tinh lai ton ngay moi khi them
			tvConLai = StringUtil.calRemain(tvKeHoach, tvThucHien);
			
			tvKeHoachThang += dtoAdd.tvKeHoachThang;
			tvThucHienThang += dtoAdd.tvThucHienThang;
			//tinh lai ton thang moi khi them
			tvConLaiThang = StringUtil.calRemain(tvKeHoachThang, tvThucHienThang);
			
			//tinh lai tien do thang moi khi them
			tvTienDoThang = StringUtil.calProgress(tvKeHoachThang, tvThucHienThang, false);
		}

		/**
		 * Set progress check hightLight
		 * @author: duongdt3
		 * @since: 16:04:31 27 Nov 2013
		 * @return: void
		 * @throws:  
		 * @param progressSold
		 */
		public void checkHightLightByProgress(float progressSold) {
			if (this.tvTienDoThang < progressSold ) {
				isHightLight = true;
			}else{
				isHightLight = false;
			}
		}

	}
	
}
