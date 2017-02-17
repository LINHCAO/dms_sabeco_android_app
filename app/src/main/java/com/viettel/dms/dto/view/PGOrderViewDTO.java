package com.viettel.dms.dto.view;

import android.database.Cursor;

import com.viettel.dms.util.StringUtil;

import java.io.Serializable;

/**
 * Created by yennth16 on 8/12/2016.
 */
public class PGOrderViewDTO implements Serializable {
    public static int TYPE_OBJECT_SAVE_FILTER = 1;
    public static int TYPE_OBJECT_SAVE_NOT_FILTER = 2;
    private static final long serialVersionUID = 1L;
    // ma san pham
    public long productId;
    // ma san pham
    public long parentProductId;
    // product code
    public String productCode;
    // product name
    public String productName;
    // convfact
    public int convfact;
    // uom2
    public String uom2;
    // QUANTITY : so luong ton kho
    public String quantity;
    // number product when choose
    public String numProduct;
    // check type object
    public int typeObject;
    public long realQuantity = 0;
    // has choose programe
    public boolean hasSelectPrograme;
    public PGOrderViewDTO() {
        realQuantity = 0;
        numProduct = "0";
        uom2 = "";
        typeObject = TYPE_OBJECT_SAVE_FILTER;
    }
    /**
     * initView
     * @param c
     */
    public void initView(Cursor c) {
        if (c.getColumnIndex("PRODUCT_ID") >= 0) {
            productId = c.getLong(c.getColumnIndex("PRODUCT_ID"));
        } else {
            productId = 0;
        }
        if (c.getColumnIndex("PARENT_PRODUCT_ID") >= 0) {
            parentProductId = c.getLong(c.getColumnIndex("PARENT_PRODUCT_ID"));
        } else {
            parentProductId = 0;
        }
        if (c.getColumnIndex("PRODUCT_CODE") >= 0) {
            productCode = c.getString(c.getColumnIndex("PRODUCT_CODE"));
        } else {
            productCode = "";
        }
        if (c.getColumnIndex("PRODUCT_NAME") >= 0) {
            productName = c.getString(c.getColumnIndex("PRODUCT_NAME"));
        } else {
            productName = "";
        }
        if (c.getColumnIndex("CONVFACT") >= 0) {
            convfact = c.getInt(c.getColumnIndex("CONVFACT"));
        } else {
            convfact = 0;
        }

        if (c.getColumnIndex("QUANTITY") >= 0) {
            quantity = c.getString(c.getColumnIndex("QUANTITY"));
            if(!StringUtil.isNullOrEmpty(quantity) && !quantity.toString().equals("0")) {
                realQuantity = Long.parseLong(quantity);
            }
        } else {
            quantity = "0";
            realQuantity = 0;
        }
    }
}
