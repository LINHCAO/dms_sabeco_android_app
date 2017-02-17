package com.viettel.dms.sqllite.db;

import android.content.ContentValues;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.dto.view.PGSaleOrderDTO;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by yennth16 on 8/18/2016.
 */
public class PG_SALE_ORDER_TABLE extends ABSTRACT_TABLE {
    // id
    public static final String PG_SALE_ORDER_ID = "PG_SALE_ORDER_ID";
    // SHOP_ID
    public static final String SHOP_ID = "SHOP_ID";
    // STAFF_ID
    public static final String STAFF_ID = "STAFF_ID";
    // CUSTOMER_ID
    public static final String CUSTOMER_ID = "CUSTOMER_ID";
    // STATUS
    public static final String STATUS = "STATUS";
    public static final String ORDER_DATE = "ORDER_DATE";
    // ngay tao
    public static final String CREATE_DATE = "CREATE_DATE";
    public static final String CREATE_USER = "CREATE_USER";
    public static final String UPDATE_DATE = "UPDATE_DATE";
    public static final String UPDATE_USER = "UPDATE_USER";

    public static final String PG_SALE_ORDER_TABLE = "PG_SALE_ORDER";

    public PG_SALE_ORDER_TABLE(SQLiteDatabase mDB) {
        this.tableName = PG_SALE_ORDER_TABLE;
        this.columns = new String[]{PG_SALE_ORDER_ID, SHOP_ID, STAFF_ID, CUSTOMER_ID, STATUS, ORDER_DATE, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER};
        this.sqlGetCountQuerry += this.tableName + ";";
        this.sqlDelete += this.tableName + ";";
        this.mDB = mDB;
    }

    @Override
    protected long insert(AbstractTableDTO dto) {
        return 0;
    }

    @Override
    protected long update(AbstractTableDTO dto) {
        return 0;
    }

    @Override
    protected long delete(AbstractTableDTO dto) {
        return 0;
    }

    /**
     * insert
     * @param dto
     * @return
     */
    public long insert(PGSaleOrderDTO dto) {
        ContentValues value = initDataRowInsert(dto);
        return insert(null, value);
    }

    /**
     * initDataRowInsert
     * @param dto
     * @return
     */
    private ContentValues initDataRowInsert(PGSaleOrderDTO dto) {
        ContentValues editedValues = new ContentValues();
        editedValues.put(PG_SALE_ORDER_ID, dto.pgSaleOrderId);
        editedValues.put(SHOP_ID, dto.shopId);
        editedValues.put(STAFF_ID, dto.staffId);
        editedValues.put(CUSTOMER_ID, dto.customerId);
        editedValues.put(STATUS, dto.status);
        editedValues.put(ORDER_DATE, dto.orderDate);
        editedValues.put(CREATE_DATE, dto.createDate);
        editedValues.put(CREATE_USER, dto.createUser);
        return editedValues;
    }

    /**
     * update
     * @param dto
     * @return
     */
    public long update(PGSaleOrderDTO dto) {
        ContentValues value = initDataRowUpdate(dto);
        String[] params = { String.valueOf(dto.pgSaleOrderId) };
        return update(value, PG_SALE_ORDER_ID + " = ?", params);
    }
    /**
     * initDataRowInsert
     * @param dto
     * @return
     */
    private ContentValues initDataRowUpdate(PGSaleOrderDTO dto) {
        ContentValues editedValues = new ContentValues();
        editedValues.put(STATUS, 0);
        editedValues.put(UPDATE_DATE, dto.updateDate);
        editedValues.put(UPDATE_USER, dto.updateUser);
        return editedValues;
    }
}
