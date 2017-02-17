package com.viettel.dms.sqllite.db;

import android.content.ContentValues;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.OpProductDTO;
import com.viettel.dms.dto.db.ProductCompetitorDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by LuBin on 23/03/2016.
 */
public class OP_PRICE_TABLE extends ABSTRACT_TABLE {
    // id
    public static final String OP_PRICE_ID = "OP_PRICE_ID";
    // id doi thu
    public static final String OPPONENT_ID = "OPPONENT_ID";
    // id san pham doi thu
    public static final String OP_PRODUCT_ID = "OP_PRODUCT_ID";
    // gia
    public static final String PRICE = "PRICE";
    // ma khach hang kiem kho
    public static final String CUSTOMER_ID = "CUSTOMER_ID";
    // ma nhan vien kiem kho
    public static final String STAFF_ID = "STAFF_ID";
    //trang thai
    public static final String STATUS = "STATUS";
    // ngay kiem kho
    public static final String SALE_DATE = "SALE_DATE";
    // ngay tao don
    public static final String CREATE_DATE = "CREATE_DATE";
    public static final String OP_PRICE_TABLE = "OP_PRICE";
    // Type: 1 - BSG, 0- doi thu
    public static final String TYPE = "TYPE";
    // Type: 0 - Thu thap du lieu thi truong, duoc edit, 1- lay tu HTBH , khong duoc edit
    public static final String ACTION_TYPE = "ACTION_TYPE";
    // nguoi tao
    public static final String CREATE_USER = "CREATE_USER";
    // nguoi cap nhat
    public static final String UPDATE_USER = "UPDATE_USER";
    // ngay cap nhat
    public static final String UPDATE_DATE = "UPDATE_DATE";

    public OP_PRICE_TABLE(SQLiteDatabase mDB){
        this.tableName = OP_PRICE_TABLE;

        this.columns = new String[] {OP_PRICE_ID, OPPONENT_ID, OP_PRODUCT_ID, PRICE, CUSTOMER_ID, STAFF_ID, ACTION_TYPE, STATUS,
                SALE_DATE, CREATE_DATE};
        this.sqlGetCountQuerry += this.tableName + ";";
        this.sqlDelete += this.tableName + ";";
        sqlInsert+=tableName+" ( ";
        this.mDB = mDB;
    }

    /* (non-Javadoc)
     * @see com.viettel.dms.sqllite.db.ABSTRACT_TABLE#insert(com.viettel.dms.dto.db.AbstractTableDTO)
     */
    @Override
    protected long insert(AbstractTableDTO dto) {
        // TODO Auto-generated method stub
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
     * insert gia cac san pham sau khi kiem ban
     * @author: trungnt56
     * @param maxID
     * @param: Tham số của hàm
     * @return: Kết qủa trả về
     * @throws: Ngoại lệ do hàm đưa ra (nếu có)
     */
    public long savePrice(ProductCompetitorDTO product, long staffID, String staffCode,
                               long cusID, String date, int type, long maxID) {
        long success=-2;
        try{
            for(OpProductDTO opProduct: product.getArrProduct()){
                opProduct.setMaxID(maxID);

                // Lay loai request db
                int typeRequest = opProduct.getTypeRequestPriceDB();

                // Insert
                if(typeRequest == 1){
                    success = initDataRow(opProduct, staffID, cusID, type, date);
                    if (success > 0) {
                        maxID +=1;
                        success = maxID;
                    }
                    VTLog.i("MAXID", "maxID" + maxID);
                }
                // Update
                else if(typeRequest == 2){
                    opProduct.updateDate = DateUtils.now();
                    opProduct.updateUser = staffCode;
                    success = updateData(opProduct);
                }
                // Delele
                else if(typeRequest == 3){
                    success = deleteData(opProduct);
                }

                if(success == -1){
                    break;
                }
            }
        }catch(Exception ex){
            success=-1;
            VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
        }
        return success;
    }

    /**
     * Delete 1 dong trong bang op_sale_volume
     * @author: quangvt1
     * @since: 15:27:01 07-05-2014
     * @return: long
     * @throws:
     * @param opProduct
     * @return
     */
    private long deleteData(OpProductDTO opProduct) {
        String where = OP_PRICE_ID + " = ?";
        String[] args = {opProduct.getOp_price_id() + ""};
        return delete(where, args);
    }

    /**
     * Update du lieu bang
     * @author: quangvt1
     * @since: 15:27:35 07-05-2014
     * @return: long
     * @throws:
     * @param opProduct
     * @return
     */
    private long updateData(OpProductDTO opProduct) {
        // Du lieu can update
        ContentValues values = new ContentValues();
        values.put(PRICE, opProduct.getNewPrice());
        values.put(UPDATE_DATE, opProduct.updateDate);
        values.put(UPDATE_USER, opProduct.updateUser);

        // where
        String where = OP_PRICE_ID + " = ?";
        String[] args = {opProduct.getOp_price_id() + ""};
        return update(values, where, args);
    }

    /**
     * Luu  du lieu thu thap du lieu thi truong role NVBH/ luu kiem ton, ban cua bia sai gon , bia doi thu
     * @author: hoanpd1
     * @since: 11:55:57 09-09-2014
     * @return: ContentValues
     * @throws:
     * @param opProduct
     * @param staffID
     * @param cusID
     * @return
     */
    private long initDataRow(OpProductDTO opProduct, long staffID, long cusID, int type, String date) {
        ContentValues editedValues = new ContentValues();
        editedValues.put(OP_PRICE_ID, String.valueOf(opProduct.getMaxID()));
        editedValues.put(OPPONENT_ID, String.valueOf(opProduct.getOpID()));
        editedValues.put(OP_PRODUCT_ID, String.valueOf(opProduct.getOpProductID()));

        editedValues.put(CUSTOMER_ID, String.valueOf(cusID));
        editedValues.put(STATUS, 1);
        editedValues.put(STAFF_ID, String.valueOf(staffID));
        editedValues.put(TYPE, type + "");

        editedValues.put(CREATE_DATE, DateUtils.now());
        editedValues.put(CREATE_USER, GlobalInfo.getInstance().getProfile().getUserData().userCode);
        if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_TNPG){
            editedValues.put(SALE_DATE, DateUtils.now());
            editedValues.put(PRICE, String.valueOf(opProduct.getPrice()));
        }else{
            editedValues.put(SALE_DATE, date);
            editedValues.put(PRICE, String.valueOf(opProduct.getNewPrice()));
            editedValues.put(ACTION_TYPE, 0);
        }

        return insert(null, editedValues);
    }
}
