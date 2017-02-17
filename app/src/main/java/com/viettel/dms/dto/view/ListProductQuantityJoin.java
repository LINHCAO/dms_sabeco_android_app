package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO.TableAction;
import com.viettel.dms.sqllite.db.PRO_CUS_MAP_DETAIL_TABLE;
import com.viettel.dms.sqllite.db.PRO_CUS_MAP_TABLE;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * DTO danh sach san pham de dang ki san luong tham gia cho chuong trinh
 * ListProductQuantityJoin.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  16:54:48 20-05-2014
 */
public class ListProductQuantityJoin implements Serializable{ 
	private static final long serialVersionUID = -4814046512130031501L;
	
	// Danh sach san pham de dang ki san luong tham gia
	public List<ProductQuantityJoin> listProduct;
	// Ma pro_cus_map
	public String pro_cus_map_id;
	
	public ListProductQuantityJoin(){
		listProduct = new ArrayList<ListProductQuantityJoin.ProductQuantityJoin>();
	}
	
	public class ProductQuantityJoin{
		// id san pham
		public String product_id;
		// Ma san pham
		public String product_code;
		// San luong da dang ki
		public long oldQuantity;
		// San luong dang ki moi
		public long newQuantity;
		// Ten san pham
		public String product_name; 
		// pro_cus_map_detail_id
		public String pro_cus_map_detail_id;
		// maxid
		public String maxId;
	}

	public void initFromCursor(Cursor c) {
		 if(c!= null && c.moveToFirst()){
			 boolean isFirst = true;
			 do{
				 ProductQuantityJoin p = new ProductQuantityJoin();
				 
				 if(c.getColumnIndex("PRODUCT_ID") > -1){
					 p.product_id = c.getString(c.getColumnIndex("PRODUCT_ID"));
				 }
				 
				 if(c.getColumnIndex("PRODUCT_CODE") > -1){
					 p.product_code = c.getString(c.getColumnIndex("PRODUCT_CODE"));
				 }
				 
				 if(c.getColumnIndex("PRODUCT_NAME") > -1){
					 p.product_name = c.getString(c.getColumnIndex("PRODUCT_NAME"));
				 }
				 
				 if(c.getColumnIndex("PRO_CUS_MAP_DETAIL_ID") > -1){
					 p.pro_cus_map_detail_id = c.getString(c.getColumnIndex("PRO_CUS_MAP_DETAIL_ID")); 
				 }

				 if(c.getColumnIndex("PRO_CUS_MAP_ID") > -1 && isFirst){
					this.pro_cus_map_id = c.getString(c.getColumnIndex("PRO_CUS_MAP_ID"));
					isFirst = false;
				 }
				 
				 if(c.getColumnIndex("QUANTITY") > -1){
					 p.oldQuantity = c.getLong(c.getColumnIndex("QUANTITY")); 
					 p.newQuantity = p.oldQuantity; 
				 }
				 
				 this.listProduct.add(p);
			 }while(c.moveToNext());
		 }
	}

	public JSONArray getJsonUpdate(boolean isChangeLevel, int newLevel,
			String staffId, String staffCode) {
		JSONArray jarr = new JSONArray();
		if(isChangeLevel){
			// Gen cau update level
			JSONObject jsonUpdateLevel = getJonUpdateLevel(newLevel, staffId, staffCode);
			jarr.put(jsonUpdateLevel);
			
			// Gen cau delete cac detail
			JSONObject jsonDeteleDetail = getJonDeleteDetail(newLevel, staffId, staffCode);
			jarr.put(jsonDeteleDetail); 
		}
		
		for (ProductQuantityJoin p : listProduct) {
			if(p.newQuantity > 0){
				if(p.newQuantity != 0 && p.oldQuantity == 0 && p.newQuantity != p.oldQuantity){
					JSONObject jsonInsertDetail = getJonInsertDetail(p, staffId, staffCode);
					jarr.put(jsonInsertDetail);
					
				}else if(p.newQuantity != 0 && p.oldQuantity != 0 && p.newQuantity != p.oldQuantity){
					JSONObject jsonUpdatetDetail = getJonUpdateDetail(p, staffId, staffCode);
					jarr.put(jsonUpdatetDetail);
					
				}
			}
		}
		return jarr;
	}

	private JSONObject getJonUpdateDetail(ProductQuantityJoin p,
			String staffId, String staffCode) { 
		JSONObject jsonUpdate = new JSONObject(); 
		try {
			// Update
			jsonUpdate.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE); 
			jsonUpdate.put(IntentConstants.INTENT_TABLE_NAME, PRO_CUS_MAP_DETAIL_TABLE.PRO_CUS_MAP_DETAIL_TABLE);
			
			// ds params
			JSONArray detailPara = new JSONArray();
			// ...thêm thuộc tính		
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_DETAIL_TABLE.QUANTITY, p.newQuantity, null));
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_DETAIL_TABLE.UPDATE_DATE, DateUtils.now(), null));
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_DETAIL_TABLE.UPDATE_USER, staffCode, null));
			jsonUpdate.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
			
			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_DETAIL_TABLE.PRO_CUS_MAP_DETAIL_ID, p.pro_cus_map_detail_id, null));
			jsonUpdate.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (JSONException e) {
			VTLog.e("UnexceptionLog",
					VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

		return jsonUpdate; 
	}

	private JSONObject getJonInsertDetail(ProductQuantityJoin p,
			String staffId, String staffCode) { 
		JSONObject jsonInsert = new JSONObject(); 
		try {
			// Insert
			jsonInsert.put(IntentConstants.INTENT_TYPE, TableAction.INSERT); 
			jsonInsert.put(IntentConstants.INTENT_TABLE_NAME, PRO_CUS_MAP_DETAIL_TABLE.PRO_CUS_MAP_DETAIL_TABLE);
			
			// ds params
			JSONArray detailPara = new JSONArray();
			// ...thêm thuộc tính		
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_DETAIL_TABLE.PRO_CUS_MAP_DETAIL_ID, p.maxId, null)); 
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_DETAIL_TABLE.PRO_CUS_MAP_ID, pro_cus_map_id, null)); 
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_DETAIL_TABLE.PRODUCT_ID, p.product_id, null)); 
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_DETAIL_TABLE.QUANTITY, p.newQuantity, null)); 
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_DETAIL_TABLE.CREATE_DATE, DateUtils.now(), null)); 
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_DETAIL_TABLE.CREATE_USER, staffCode, null)); 
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_DETAIL_TABLE.STAFF_ID, staffId, null)); 
			jsonInsert.put(IntentConstants.INTENT_LIST_PARAM, detailPara); 
			
		} catch (JSONException e) {
			VTLog.e("UnexceptionLog",
					VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

		return jsonInsert; 
	}

	private JSONObject getJonDeleteDetail(int newLevel, String staffId,
			String staffCode) { 
		JSONObject jsonDetete = new JSONObject(); 
		try {
			// Delete
			jsonDetete.put(IntentConstants.INTENT_TYPE, TableAction.DELETE); 
			jsonDetete.put(IntentConstants.INTENT_TABLE_NAME, PRO_CUS_MAP_DETAIL_TABLE.PRO_CUS_MAP_DETAIL_TABLE); 
			
			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_DETAIL_TABLE.PRO_CUS_MAP_ID, pro_cus_map_id, null));
			jsonDetete.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (JSONException e) {
			VTLog.e("UnexceptionLog",
					VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

		return jsonDetete; 
	}

	private JSONObject getJonUpdateLevel(int newLevel, String staffId,
			String staffCode) {
		JSONObject jsonUpdate = new JSONObject(); 
		try {
			// Update
			jsonUpdate.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE); 
			jsonUpdate.put(IntentConstants.INTENT_TABLE_NAME, PRO_CUS_MAP_TABLE.PRO_CUS_MAP_TABLE);
			
			// ds params
			JSONArray detailPara = new JSONArray();
			// ...thêm thuộc tính		
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_TABLE.LEVEL_NUMBER, newLevel, null));
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_TABLE.UPDATE_DATE, DateUtils.now(), null));
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_TABLE.UPDATE_USER, staffCode, null));
			jsonUpdate.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
			
			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_TABLE.PRO_CUS_MAP_ID, pro_cus_map_id, null));
			jsonUpdate.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (JSONException e) {
			VTLog.e("UnexceptionLog",
					VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

		return jsonUpdate; 
	}
}
