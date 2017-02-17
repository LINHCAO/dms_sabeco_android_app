package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.CUSTOMER_TABLE;
import com.viettel.dms.sqllite.db.STAFF_CUSTOMER_DETAIL_TABLE;
import com.viettel.dms.sqllite.db.STAFF_CUSTOMER_TABLE;
import com.viettel.dms.sqllite.db.STAFF_POSITION_LOG_TABLE;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * Created by yennth16 on 02/06/2016.
 */
public class StaffCustomerDetailDTO extends AbstractTableDTO {

    public long staffCustomerDetailId;
    public long staffCustomerId;
    public String startDate;
    public String endDate;
    public String createDate;
    public String createUser;
    public String updateDate;
    public String updateUser;

    public StaffCustomerDetailDTO() {
        super(TableType.STAFF_CUSTOMER_DETAIL_TABLE);
    }

    /**
     * generate Update ExceptionOrderDate
     *
     * @author: YENNTH16
     * @return
     * @return: JSONObject
     * @throws:
     */
    public JSONObject generateCreateSql(){
        JSONObject orderJson = new JSONObject();
        try{
            orderJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
            orderJson.put(IntentConstants.INTENT_TABLE_NAME, STAFF_CUSTOMER_DETAIL_TABLE.TABLE_NAME);

            // ds params
            JSONArray params = new JSONArray();
            params.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_DETAIL_TABLE.STAFF_CUSTOMER_DETAIL_ID,this.staffCustomerDetailId, null));
            params.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_DETAIL_TABLE.STAFF_CUSTOMER_ID, this.staffCustomerId, null));
            if (!StringUtil.isNullOrEmpty(startDate)) {
                params.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_DETAIL_TABLE.START_DATE, this.startDate, null));
            }
            if (!StringUtil.isNullOrEmpty(createDate)) {
                params.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_DETAIL_TABLE.CREATE_DATE, this.createDate, null));
            }
            if (!StringUtil.isNullOrEmpty(createUser)) {
                params.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_DETAIL_TABLE.CREATE_USER, this.createUser, null));
            }
            orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);

        }catch (JSONException e) {
            // TODO: handle exception
            VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
        }
        return orderJson;
    }

    /**
     * Generate cau lenh update
     * @author: yennth16
     * @return: JSONObject
     * @throws:
     */
    public JSONObject generateUpdateSql() {
        JSONObject json = new JSONObject();
        try {
            json.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
            json.put(IntentConstants.INTENT_TABLE_NAME, STAFF_CUSTOMER_DETAIL_TABLE.TABLE_NAME);

            // ds params
            JSONArray params = new JSONArray();
            if (!StringUtil.isNullOrEmpty(endDate)) {
                params.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_DETAIL_TABLE.END_DATE, endDate, null));
            }
            if (!StringUtil.isNullOrEmpty(updateDate)) {
                params.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_DETAIL_TABLE.UPDATE_DATE, updateDate, null));
            }
            if (!StringUtil.isNullOrEmpty(updateUser)) {
                params.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_DETAIL_TABLE.UPDATE_USER, updateUser, null));
            }
            json.put(IntentConstants.INTENT_LIST_PARAM, params);

            // ds where params --> insert khong co menh de where
            JSONArray wheres = new JSONArray();
            wheres.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_DETAIL_TABLE.STAFF_CUSTOMER_DETAIL_ID, staffCustomerDetailId, null));
            json.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

        } catch (JSONException e) {
        }
        return json;
    }
}
