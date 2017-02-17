package com.viettel.dms.dto.view;

import android.database.Cursor;

import com.viettel.dms.constants.Constants;

import java.io.Serializable;

/**
 * Created by yennth16 on 8/18/2016.
 */
public class PGCustomerViewDTO implements Serializable {
    // customerId
    public long customerId;
    // customerCode
    public String customerCode;
    // customerName
    public String customerName;
    // pgSaleOrderId
    public long pgSaleOrderId;
    public PGCustomerViewDTO() {
        customerId = 0;
        customerCode = Constants.STR_BLANK;
        customerName = Constants.STR_BLANK;
        pgSaleOrderId = 0;
    }
    /**
     * initView
     * @param c
     */
    public void initView(Cursor c) {
        if (c.getColumnIndex("CUSTOMER_ID") >= 0) {
            customerId = c.getLong(c.getColumnIndex("CUSTOMER_ID"));
        } else {
            customerId = 0;
        }
        if (c.getColumnIndex("CUSTOMER_CODE") >= 0) {
            customerCode = c.getString(c.getColumnIndex("CUSTOMER_CODE"));
        } else {
            customerCode = "";
        }
        if (c.getColumnIndex("CUSTOMER_NAME") >= 0) {
            customerName = c.getString(c.getColumnIndex("CUSTOMER_NAME"));
        } else {
            customerName = "";
        }
        if (c.getColumnIndex("PG_SALE_ORDER_ID") >= 0) {
            pgSaleOrderId = c.getLong(c.getColumnIndex("PG_SALE_ORDER_ID"));
        } else {
            pgSaleOrderId = 0;
        }
    }
}
