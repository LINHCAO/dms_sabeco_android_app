package com.viettel.dms.sqllite.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.ShopParamDTO;
import com.viettel.dms.dto.db.StaffPositionLogDTO;
import com.viettel.dms.dto.db.WorkLogDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.map.dto.LatLng;
import com.viettel.utils.VTLog;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yennth16 on 8/10/2016.
 */
public class WORK_LOG_TABLE extends ABSTRACT_TABLE {
    // id
    public static final String WORK_LOG_ID = "WORK_LOG_ID";
    // staff_id
    public static final String STAFF_ID = "STAFF_ID";
    // SHOP_ID
    public static final String SHOP_ID = "SHOP_ID";
    // ngay tao
    public static final String CREATE_DATE = "CREATE_DATE";
    // toa do lat STAFF
    public static final String STAFF_LAT = "STAFF_LAT";
    // toa do long STAFF
    public static final String STAFF_LNG = "STAFF_LNG";
    // toa do lat SHOP
    public static final String SHOP_LAT = "SHOP_LAT";
    // toa do long SHOP
    public static final String SHOP_LNG = "SHOP_LNG";
    // do chinh xac
    public static final String DISTANCE = "DISTANCE";
    // ngay tao
    public static final String WORK_DATE = "WORK_DATE";

    public static final String WORK_LOG_TABLE = "WORK_LOG";

    public WORK_LOG_TABLE(SQLiteDatabase mDB) {
        this.tableName = WORK_LOG_TABLE;
        this.columns = new String[] { WORK_LOG_ID, STAFF_ID, SHOP_ID, STAFF_LAT, STAFF_LNG,
                SHOP_LAT, SHOP_LNG, CREATE_DATE, DISTANCE, WORK_DATE, SYN_STATE };
        this.sqlGetCountQuerry += this.tableName + ";";
        this.sqlDelete += this.tableName + ";";
        this.mDB = mDB;
    }
    @Override
    protected long insert(AbstractTableDTO dto) {
        ContentValues value = initDataRow((WorkLogDTO) dto);
        return insert(null, value);
    }
    @Override
    protected long update(AbstractTableDTO dto) {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    protected long delete(AbstractTableDTO dto) {
        // TODO Auto-generated method stub
        return 0;
    }
    /**
     * Lay thong tin cham cong
     * @return
     */
    public WorkLogDTO getWorkLog(){
        WorkLogDTO result = null;
        SHOP_PARAM_TABLE apParamTable = new SHOP_PARAM_TABLE(mDB);
        SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
        LatLng shopPosition = shopTable.getPositionOfShop(GlobalInfo.getInstance().getProfile().getUserData().shopId);
        String dateNow= DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
        List<ShopParamDTO> listShopParamDTO = apParamTable.getListParamForTakeAttendance(GlobalInfo.getInstance().getProfile().getUserData().shopId);
        String ccStart = "";
        String ccEnd = "";
        String ccDistance = "";
        if(listShopParamDTO.size() >= 3) {
            ccStart = dateNow + ' ' + listShopParamDTO.get(0).code;
            ccEnd = dateNow + ' ' + listShopParamDTO.get(1).code;
            ccDistance = listShopParamDTO.get(2).code;
        }
        String[] params = {dateNow, GlobalInfo.getInstance().getProfile().getUserData().id + "", ccStart, ccEnd};
        StringBuffer  sql = new StringBuffer();
        sql.append("SELECT WORK_LOG_ID 			            AS WORK_LOG_ID, ");
        sql.append("       STAFF_ID 						AS STAFF_ID, ");
        sql.append("       SHOP_ID 						    AS SHOP_ID, ");
        sql.append("       STRFTIME('%H:%M', WORK_DATE) 	AS WORK_DATE, ");
        sql.append("       CREATE_DATE 	                    AS CREATE_DATE, ");
        sql.append("       STAFF_LAT 						AS STAFF_LAT, ");
        sql.append("       STAFF_LNG 						AS STAFF_LNG, ");
        sql.append("       SHOP_LAT 						AS SHOP_LAT, ");
        sql.append("       SHOP_LNG 						AS SHOP_LNG, ");
        sql.append("       DISTANCE 						AS DISTANCE ");
        sql.append(" FROM   WORK_LOG ");
        sql.append(" WHERE 1 = 1 and substr(WORK_DATE,0,11) >= ? ");
        sql.append(" AND STAFF_ID = ? ");
        sql.append(" AND WORK_DATE >= ? ");
        sql.append(" AND WORK_DATE <= ? ");
        Cursor c = null;
        WorkLogDTO lastLogPosition = null;
        if (shopPosition.lat > 0 && shopPosition.lng > 0 && !StringUtil.isNullOrEmpty(ccDistance)) {
            try {
                c = rawQuery(sql.toString(), params);
                if (c != null) {
                    if (c.moveToFirst()) {
                        lastLogPosition = (new WorkLogDTO()).initFromCursor(c);
                        double cusDistance = -1;
                        if (lastLogPosition.staffLat > 0 && lastLogPosition.staffLng > 0) {
                            cusDistance = GlobalUtil.getDistanceBetween(new LatLng(lastLogPosition.staffLat, lastLogPosition.staffLng), shopPosition);
                            if (Double.valueOf(ccDistance).compareTo(cusDistance) >= 0) {
                                result = lastLogPosition;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                VTLog.printStackTrace(e);
            } finally {
                if (c != null) {
                    try {
                        c.close();
                    } catch (Exception e2) {
                        VTLog.printStackTrace(e2);
                    }
                }
            }
        }
        return result;
    }

    /**
     * initDataRow
     * @param dto
     * @return
     */
    private ContentValues initDataRow(WorkLogDTO dto) {
        ContentValues editedValues = new ContentValues();
        editedValues.put(WORK_LOG_ID, dto.workLogId);
        editedValues.put(STAFF_ID, dto.staffId);
        editedValues.put(SHOP_ID, dto.shopId);
        editedValues.put(STAFF_LAT, dto.staffLat);
        editedValues.put(STAFF_LNG, dto.staffLng);
        editedValues.put(SHOP_LAT, dto.shopLat);
        editedValues.put(SHOP_LNG, dto.shopLng);
        editedValues.put(CREATE_DATE, dto.createDate);
        editedValues.put(DISTANCE, dto.distance);
        editedValues.put(WORK_DATE, dto.workDate);
        return editedValues;
    }
    /**
     * Lay danh sach khong thoa dieu kien cham cong xoa khoi bang work_log
     * @return
     */
    public ArrayList<WorkLogDTO> getWorkLogDelete(long workLogIdNotDelete){
        ArrayList<WorkLogDTO> result = new ArrayList<>();
        String dateNow= DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
        StringBuffer  sql = new StringBuffer();
        ArrayList<String> param = new ArrayList<String>();
        sql.append("SELECT WORK_LOG_ID 			            AS WORK_LOG_ID, ");
        sql.append("       STAFF_ID 						AS STAFF_ID, ");
        sql.append("       SHOP_ID 						    AS SHOP_ID, ");
        sql.append("       STRFTIME('%H:%M', WORK_DATE) 	AS WORK_DATE, ");
        sql.append("       CREATE_DATE 	                    AS CREATE_DATE, ");
        sql.append("       STAFF_LAT 						AS STAFF_LAT, ");
        sql.append("       STAFF_LNG 						AS STAFF_LNG, ");
        sql.append("       SHOP_LAT 						AS SHOP_LAT, ");
        sql.append("       SHOP_LNG 						AS SHOP_LNG, ");
        sql.append("       DISTANCE 						AS DISTANCE ");
        sql.append(" FROM   WORK_LOG ");
        sql.append(" WHERE 1 = 1 and substr(WORK_DATE,0,11) >= ? ");
        param.add(dateNow);
        sql.append(" AND STAFF_ID = ? ");
        param.add(GlobalInfo.getInstance().getProfile().getUserData().id + "");
        sql.append(" AND WORK_LOG_ID != ? ");
        param.add("" + workLogIdNotDelete);
        Cursor c = null;
            try {
                c = rawQueries(sql.toString(), param);
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            WorkLogDTO lastLogPosition = new WorkLogDTO();
                            lastLogPosition.init(c);
                            result.add(lastLogPosition);
                        } while (c.moveToNext());
                    }
                }
            } catch (Exception e) {
                VTLog.printStackTrace(e);
            } finally {
                if (c != null) {
                    try {
                        c.close();
                    } catch (Exception e2) {
                        VTLog.printStackTrace(e2);
                    }
                }
            }
        return result;
    }

    /**
     * Xoa thong tin cham cong khong thoa dieu kien
     * @param workLogDTO
     * @return
     */
    public long deleteWorkLog(WorkLogDTO workLogDTO) {
        ArrayList<String> params = new ArrayList<String>();
        params.add(String.valueOf(workLogDTO.workLogId));
        return delete(" WORK_LOG_ID = ? ", params.toArray(new String[params.size()]));
    }
}

