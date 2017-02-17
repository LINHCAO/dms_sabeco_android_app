package com.viettel.dms.dto.db;

import android.database.Cursor;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.ACTION_LOG_TABLE;
import com.viettel.dms.sqllite.db.STAFF_POSITION_LOG_TABLE;
import com.viettel.dms.sqllite.db.WORK_LOG_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * Created by yennth16 on 8/10/2016.
 */
public class WorkLogDTO extends AbstractTableDTO{
    // id
    public long workLogId;
    // staff id
    public long staffId;
    // staff id
    public long shopId;
    // lat
    public double staffLat;
    // lng
    public double staffLng;
    // lat
    public double shopLat;
    // lng
    public double shopLng;
    //createDate
    public String createDate;
    // khoang cach
    public double distance = 0;
    // ngay cham cong
    public String workDate;

    public WorkLogDTO() {
        super(TableType.WORK_LOG_TABLE);
    }
    /**
     * Generate cau lenh insert
     * @return
     */
    public JSONObject generateCreateSql(){
        JSONObject orderJson = new JSONObject();
        try{
            orderJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
            orderJson.put(IntentConstants.INTENT_TABLE_NAME, WORK_LOG_TABLE.WORK_LOG_TABLE);
            // ds params
            JSONArray params = new JSONArray();
            params.put(GlobalUtil.getJsonColumn(WORK_LOG_TABLE.WORK_LOG_ID, this.workLogId, null));
            params.put(GlobalUtil.getJsonColumn(WORK_LOG_TABLE.STAFF_ID, this.staffId, null));
            params.put(GlobalUtil.getJsonColumn(WORK_LOG_TABLE.SHOP_ID, this.shopId, null));
            params.put(GlobalUtil.getJsonColumn(WORK_LOG_TABLE.STAFF_LAT, this.staffLat , null));
            params.put(GlobalUtil.getJsonColumn(WORK_LOG_TABLE.STAFF_LNG,this.staffLng, null));
            params.put(GlobalUtil.getJsonColumn(WORK_LOG_TABLE.SHOP_LAT, this.shopLat , null));
            params.put(GlobalUtil.getJsonColumn(WORK_LOG_TABLE.SHOP_LNG,this.shopLng, null));
            params.put(GlobalUtil.getJsonColumn(WORK_LOG_TABLE.CREATE_DATE, this.createDate, null));
            params.put(GlobalUtil.getJsonColumn(WORK_LOG_TABLE.WORK_DATE, this.workDate, null));
            if (this.distance >= 0){
                params.put(GlobalUtil.getJsonColumn(WORK_LOG_TABLE.DISTANCE,this.distance, null));
            }
            orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);
        }catch (JSONException e) {
            // TODO: handle exception
            VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
        }
        return orderJson;
    }
    /**
     * initFromCursor
     * @param c
     * @return
     */
    public WorkLogDTO initFromCursor(Cursor c) {
        WorkLogDTO temp = new WorkLogDTO();
        temp.workLogId = c.getLong(c.getColumnIndex("WORK_LOG_ID"));
        temp.staffId = c.getLong(c.getColumnIndex("STAFF_ID"));
        temp.shopId = c.getLong(c.getColumnIndex("SHOP_ID"));
        temp.staffLat = c.getDouble(c.getColumnIndex("STAFF_LAT"));
        temp.staffLng = c.getDouble(c.getColumnIndex("STAFF_LNG"));
        temp.shopLat = c.getDouble(c.getColumnIndex("SHOP_LAT"));
        temp.shopLng = c.getDouble(c.getColumnIndex("SHOP_LNG"));
        temp.distance = c.getDouble(c.getColumnIndex("DISTANCE"));
        temp.createDate = c.getString(c.getColumnIndex("CREATE_DATE"));
        temp.workDate = c.getString(c.getColumnIndex("WORK_DATE"));
        return temp;
    }

    /**
     * json xao thong tin cham cong
     * @return
     */
    public JSONObject generateDeletWorkLog() {
        JSONObject deleteJson = new JSONObject();
        try {
            deleteJson.put(IntentConstants.INTENT_TYPE, TableAction.DELETE);
            deleteJson.put(IntentConstants.INTENT_TABLE_NAME, WORK_LOG_TABLE.WORK_LOG_TABLE);
            // ds where params --> insert khong co menh de where
            JSONArray whereParams = new JSONArray();
            whereParams.put(GlobalUtil.getJsonColumn(WORK_LOG_TABLE.WORK_LOG_ID, workLogId, null));
            deleteJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, whereParams);
        } catch (JSONException e) {
            VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
        }
        return deleteJson;
    }

    public void init(Cursor c) {
        workLogId = c.getLong(c.getColumnIndex("WORK_LOG_ID"));
        staffId = c.getLong(c.getColumnIndex("STAFF_ID"));
        shopId = c.getLong(c.getColumnIndex("SHOP_ID"));
        staffLat = c.getDouble(c.getColumnIndex("STAFF_LAT"));
        staffLng = c.getDouble(c.getColumnIndex("STAFF_LNG"));
        shopLat = c.getDouble(c.getColumnIndex("SHOP_LAT"));
        shopLng = c.getDouble(c.getColumnIndex("SHOP_LNG"));
        distance = c.getDouble(c.getColumnIndex("DISTANCE"));
        createDate = c.getString(c.getColumnIndex("CREATE_DATE"));
        workDate = c.getString(c.getColumnIndex("WORK_DATE"));
    }
}
