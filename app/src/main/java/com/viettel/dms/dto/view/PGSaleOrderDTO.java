package com.viettel.dms.dto.view;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.sqllite.db.PG_SALE_ORDER_TABLE;
import com.viettel.dms.sqllite.db.SALE_ORDER_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

import java.io.Serializable;

/**
 * Created by yennth16 on 8/18/2016.
 */
public class PGSaleOrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    public long pgSaleOrderId;
    public String shopId;
    public long staffId;
    public long customerId;
    public int status;
    public String orderDate;
    public String description;
    public String createDate;
    public String createUser;
    public String updateDate;
    public String updateUser;
    public PGSaleOrderDTO(){
        pgSaleOrderId = 0;
        shopId = Constants.STR_BLANK;
        staffId = 0;
        customerId = 0;
        status = 0;
        orderDate = Constants.STR_BLANK;
        description = Constants.STR_BLANK;
        createUser = Constants.STR_BLANK;
        createDate = Constants.STR_BLANK;
        updateUser = Constants.STR_BLANK;
        updateDate = Constants.STR_BLANK;
    }

    /**
     * Json tao don hang pg
     * @return
     */
    public JSONObject generateCreateSql(){
        JSONObject orderJson = new JSONObject();
        try{
            orderJson.put(IntentConstants.INTENT_TYPE, AbstractTableDTO.TableAction.INSERT);
            orderJson.put(IntentConstants.INTENT_TABLE_NAME, PG_SALE_ORDER_TABLE.PG_SALE_ORDER_TABLE);
            JSONArray params = new JSONArray();
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_TABLE.PG_SALE_ORDER_ID, pgSaleOrderId, null));
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_TABLE.SHOP_ID, shopId, null));
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_TABLE.STAFF_ID, staffId, null));
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_TABLE.CUSTOMER_ID,customerId, null));
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_TABLE.STATUS, status , null));
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_TABLE.ORDER_DATE, orderDate, null));
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_TABLE.CREATE_USER, createUser, null));
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_TABLE.CREATE_DATE,createDate, null));
            orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);
        }catch (JSONException e) {
            VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
        }
        return orderJson;
    }

    /**
     * Json cap nhat trang thai don hang pg
     * @return
     */
    public JSONObject generateEditOrderSql() {
        JSONObject orderJson = new JSONObject();
        try{
            orderJson.put(IntentConstants.INTENT_TYPE, AbstractTableDTO.TableAction.UPDATE);
            orderJson.put(IntentConstants.INTENT_TABLE_NAME, PG_SALE_ORDER_TABLE.PG_SALE_ORDER_TABLE);
            JSONArray params = new JSONArray();
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_TABLE.STATUS, status, null));
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_TABLE.UPDATE_DATE, updateDate, null));
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_TABLE.UPDATE_USER, updateUser, null));//Tam
            orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);
            JSONArray whereParams = new JSONArray();
            whereParams.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_TABLE.PG_SALE_ORDER_ID, String.valueOf(pgSaleOrderId), null));
            orderJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, whereParams);

        }catch (JSONException e) {
            VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
        }
        return orderJson;
    }
}
