package com.viettel.dms.dto.view;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.sqllite.db.PG_SALE_ORDER_DETAIL_TABLE;
import com.viettel.dms.sqllite.db.PG_SALE_ORDER_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

import java.io.Serializable;

/**
 * Created by yennth16 on 8/18/2016.
 */
public class PGSaleOrderDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    public long pgSaleOrderDetailId;
    public long pgSaleOrderId;
    public String shopId;
    public long staffId;
    public long productId;
    public long parentProductId;
    public String orderDate;
    public String description;
    public long quantity;
    public int convfact;
    public String createDate;
    public String createUser;
    public String updateDate;
    public String updateUser;
    public PGSaleOrderDetailDTO(){
        pgSaleOrderDetailId = 0;
        pgSaleOrderId = 0;
        shopId = Constants.STR_BLANK;
        staffId = 0;
        productId = 0;
        parentProductId = 0;
        orderDate = Constants.STR_BLANK;
        quantity = 0;
        convfact = 0;
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
            orderJson.put(IntentConstants.INTENT_TABLE_NAME, PG_SALE_ORDER_DETAIL_TABLE.PG_SALE_ORDER_DETAIL_TABLE);
            JSONArray params = new JSONArray();
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_DETAIL_TABLE.PG_SALE_ORDER_DETAIL_ID, pgSaleOrderDetailId, null));
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_DETAIL_TABLE.PG_SALE_ORDER_ID, pgSaleOrderId, null));
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_DETAIL_TABLE.SHOP_ID, shopId, null));
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_DETAIL_TABLE.STAFF_ID, staffId, null));
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_DETAIL_TABLE.PRODUCT_ID,productId, null));
            if(parentProductId > 0) {
                params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_DETAIL_TABLE.PARENT_PRODUCT_ID, parentProductId, null));
            }
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_DETAIL_TABLE.ORDER_DATE, orderDate, null));
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_DETAIL_TABLE.QUANTITY, quantity, null));
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_DETAIL_TABLE.CONVFACT, convfact, null));
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_DETAIL_TABLE.CREATE_USER, createUser, null));
            params.put(GlobalUtil.getJsonColumn(PG_SALE_ORDER_DETAIL_TABLE.CREATE_DATE,createDate, null));
            orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);
        }catch (JSONException e) {
            VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
        }
        return orderJson;
    }
}
