package com.viettel.dms.sqllite.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.StaffCustomerDTO;
import com.viettel.dms.dto.db.StaffCustomerDetailDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.utils.VTLog;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by yennth16 on 02/06/2016.
 */
public class STAFF_CUSTOMER_DETAIL_TABLE extends ABSTRACT_TABLE {
    //STAFF_CUSTOMER_DETAIL_ID
    public static final String STAFF_CUSTOMER_DETAIL_ID = "STAFF_CUSTOMER_DETAIL_ID";
    // STAFF_CUSTOMER_ID
    public static final String STAFF_CUSTOMER_ID = "STAFF_CUSTOMER_ID";
    // START_DATE
    public static final String START_DATE = "START_DATE";
    // END_DATE
    public static final String END_DATE = "END_DATE";
    // CREATE_DATE
    public static final String CREATE_DATE = "CREATE_DATE";
    // CREATE_USER
    public static final String CREATE_USER = "CREATE_USER";
    // UPDATE_DATE
    public static final String UPDATE_DATE = "UPDATE_DATE";
    // UPDATE_USER
    public static final String UPDATE_USER = "UPDATE_USER";

    public static final String TABLE_NAME = "STAFF_CUSTOMER_DETAIL";

    public STAFF_CUSTOMER_DETAIL_TABLE(SQLiteDatabase mDB) {
        this.tableName = TABLE_NAME;
        this.columns = new String[] { STAFF_CUSTOMER_DETAIL_ID,
                STAFF_CUSTOMER_ID,
                START_DATE,
                END_DATE,
                CREATE_DATE,
                CREATE_USER,
                UPDATE_DATE,
                UPDATE_USER, SYN_STATE };
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

    private ContentValues initRowInsertStaffCustomer(StaffCustomerDetailDTO dto) {
        ContentValues editedValues = new ContentValues();
        editedValues.put(STAFF_CUSTOMER_DETAIL_ID, dto.staffCustomerDetailId);
        editedValues.put(STAFF_CUSTOMER_ID, dto.staffCustomerId);
        if(!StringUtil.isNullOrEmpty(dto.startDate)){
            editedValues.put(START_DATE, dto.startDate);
        }
        if(!StringUtil.isNullOrEmpty(dto.createDate)){
            editedValues.put(CREATE_DATE, dto.createDate);
        }
        if(!StringUtil.isNullOrEmpty(dto.createUser)) {
            editedValues.put(CREATE_USER, dto.createUser);
        }
        return editedValues;
    }

    private ContentValues initRowForUpdateStaffCustomer(StaffCustomerDetailDTO dto) {
        ContentValues editedValues = new ContentValues();
        if(!StringUtil.isNullOrEmpty(dto.endDate)){
            editedValues.put(END_DATE, dto.endDate);
        }
        if(!StringUtil.isNullOrEmpty(dto.updateDate)){
            editedValues.put(UPDATE_DATE, dto.updateDate);
        }
        if(!StringUtil.isNullOrEmpty(dto.updateUser)) {
            editedValues.put(UPDATE_USER, dto.updateUser);
        }
        return editedValues;
    }

    /**
     *  Insert or update staff customer
     *  @author: TruongHN
     *  @param dto
     *  @return: long
     *  @throws:
     */
    public long insertOrUpdate(StaffCustomerDTO dto){
        long res = -1;
        StaffCustomerDetailDTO staffCustomerDetailDTO = new StaffCustomerDetailDTO();
        long staffCustomerDetailId = getRowStaffCustomerDetail(Constants.STR_BLANK + dto.staffCustomerId);
        if (dto.exceptionOrderDate == null && staffCustomerDetailId > 0){
            staffCustomerDetailDTO.endDate = DateUtils.now();
            staffCustomerDetailDTO.updateDate = DateUtils.now();
            staffCustomerDetailDTO.updateUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
            staffCustomerDetailDTO.staffCustomerDetailId = staffCustomerDetailId;
            res = updateLastOrder(staffCustomerDetailDTO);
            if(res > 0){
                dto.staffCustomerDetailDTO = staffCustomerDetailDTO;
            }
        }else{
            TABLE_ID tableId = new TABLE_ID(mDB);
            long staffCustomerDetailIdMax = tableId.getMaxId(STAFF_CUSTOMER_DETAIL_TABLE.TABLE_NAME, GlobalInfo.getInstance().getProfile().getUserData().id);
            staffCustomerDetailDTO.staffCustomerDetailId = staffCustomerDetailIdMax ;
            staffCustomerDetailDTO.staffCustomerId = dto.staffCustomerId;
            staffCustomerDetailDTO.startDate = DateUtils.now();
            staffCustomerDetailDTO.createDate = DateUtils.now();
            staffCustomerDetailDTO.createUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
            ContentValues value = initRowInsertStaffCustomer(staffCustomerDetailDTO);
            res = insert(null, value);
            if(res > 0){
                dto.staffCustomerDetailDTO = staffCustomerDetailDTO;
            }
        }
        return res;
    }

    /**
     * Lay thong tin mot row du lieu STAFF-CUSTOMER_DETAIL
     * @author: yennth16
     * @return
     * @throws: Ngoại lệ do hàm đưa ra (nếu có)
     */
    public long getRowStaffCustomerDetail( String staffCustomerId){
        long staffCustomerDetailId = 0;
        Cursor c = null;
        try {
            String query = "SELECT STAFF_CUSTOMER_DETAIL_ID  FROM STAFF_CUSTOMER_DETAIL WHERE STAFF_CUSTOMER_ID = ? ORDER BY START_DATE DESC LIMIT 1 ";
            String[] params = {staffCustomerId};
            c = rawQuery(query, params);
            if (c != null) {
                if (c.moveToFirst()) {
                   staffCustomerDetailId = (c.getLong(c.getColumnIndex(STAFF_CUSTOMER_DETAIL_ID)));
                }
            }
        } catch (Exception e) {
            VTLog.i("error", e.toString());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return staffCustomerDetailId;
    }

    public long updateLastOrder(AbstractTableDTO dto) {
        StaffCustomerDetailDTO detail = (StaffCustomerDetailDTO) dto;
        ContentValues value = initRowForUpdateStaffCustomer(detail);
        String[] params = { String.valueOf(detail.staffCustomerDetailId) };
        return update(value, STAFF_CUSTOMER_DETAIL_ID + " = ? ", params);
    }

}
