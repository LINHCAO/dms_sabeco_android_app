package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.CUSTOMER_TABLE;
import com.viettel.dms.sqllite.db.ROUTING_CUSTOMER_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * Created by LuBin on 24/02/2016.
 */
public class RoutingCustomerDTO extends AbstractTableDTO {
    // id
    public Long routingCustomerId;
    // id khach hang
    public Long customerId;
    // cac thu
    public int monday;
    public int tuesday;
    public int wednesday;
    public int thursday;
    public int friday;
    public int saturday;
    public int sunday;
    public int status;
    public String createDate;
    public String createUser;
    public Long routingId;
    public String startDate;
    public String endDate;
    public int week1;
    public int week2;
    public int week3;
    public int week4;

    public RoutingCustomerDTO () {
        super(TableType.ROUTING_CUSTOMER);
    }

    /**
     * tạo json create routing Customer
     * @author: trungnt56
     * @since: 09:50:21 7 Jan 2014
     * @return: JSONObject
     * @throws:
     * @return
     * @throws JSONException
     */
    public JSONObject generateCreateRoutingCustomerSql() throws JSONException {
        JSONObject insertRoutingCustomerJson = new JSONObject();
        insertRoutingCustomerJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
        insertRoutingCustomerJson.put(IntentConstants.INTENT_TABLE_NAME, ROUTING_CUSTOMER_TABLE.ROUTING_CUSTOMER_TABLE);

        // ds params
        JSONArray detailPara = new JSONArray();
        // ...thêm thuộc tính
        detailPara.put(GlobalUtil.getJsonColumn(ROUTING_CUSTOMER_TABLE.ROUTING_CUSTOMER_ID, routingCustomerId, null));
        detailPara.put(GlobalUtil.getJsonColumn(ROUTING_CUSTOMER_TABLE.CUSTOMER_ID, customerId, null));
        detailPara.put(GlobalUtil.getJsonColumn(ROUTING_CUSTOMER_TABLE.MONDAY, monday, null));
        detailPara.put(GlobalUtil.getJsonColumn(ROUTING_CUSTOMER_TABLE.TUESDAY, tuesday, null));
        detailPara.put(GlobalUtil.getJsonColumn(ROUTING_CUSTOMER_TABLE.WEDNESDAY, wednesday, null));
        detailPara.put(GlobalUtil.getJsonColumn(ROUTING_CUSTOMER_TABLE.THURSDAY, thursday , null));
        detailPara.put(GlobalUtil.getJsonColumn(ROUTING_CUSTOMER_TABLE.FRIDAY, friday, null));
        detailPara.put(GlobalUtil.getJsonColumn(ROUTING_CUSTOMER_TABLE.SATURDAY, saturday, null));
        detailPara.put(GlobalUtil.getJsonColumn(ROUTING_CUSTOMER_TABLE.SUNDAY, sunday, null));
        detailPara.put(GlobalUtil.getJsonColumn(ROUTING_CUSTOMER_TABLE.STATUS, status, null));
        detailPara.put(GlobalUtil.getJsonColumn(ROUTING_CUSTOMER_TABLE.CREATE_DATE, createDate, null));
        detailPara.put(GlobalUtil.getJsonColumn(ROUTING_CUSTOMER_TABLE.CREATE_USER, createUser, null));
        detailPara.put(GlobalUtil.getJsonColumn(ROUTING_CUSTOMER_TABLE.ROUTING_ID, routingId, null));
        detailPara.put(GlobalUtil.getJsonColumn(ROUTING_CUSTOMER_TABLE.START_DATE, startDate, null));
        if(!StringUtil.isNullOrEmpty(endDate)) {
            detailPara.put(GlobalUtil.getJsonColumn(ROUTING_CUSTOMER_TABLE.END_DATE, endDate, null));
        } else {
            detailPara.put(GlobalUtil.getJsonColumn(ROUTING_CUSTOMER_TABLE.END_DATE, "", DATA_TYPE.NULL.toString()));
        }
        detailPara.put(GlobalUtil.getJsonColumn(ROUTING_CUSTOMER_TABLE.WEEK1, week1, null));
        detailPara.put(GlobalUtil.getJsonColumn(ROUTING_CUSTOMER_TABLE.WEEK2, week2, null));
        detailPara.put(GlobalUtil.getJsonColumn(ROUTING_CUSTOMER_TABLE.WEEK3, week3, null));
        detailPara.put(GlobalUtil.getJsonColumn(ROUTING_CUSTOMER_TABLE.WEEK4, week4, null));
        insertRoutingCustomerJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
        return insertRoutingCustomerJson;
    }
}
