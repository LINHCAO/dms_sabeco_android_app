package com.viettel.dms.sqllite.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.view.ListOrderViewDTO;
import com.viettel.dms.dto.view.PGCustomerViewDTO;
import com.viettel.dms.dto.view.PGOrderViewDTO;
import com.viettel.dms.dto.view.PGSaleOrderDTO;
import com.viettel.dms.dto.view.PGSaleOrderDetailDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.utils.VTLog;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by yennth16 on 8/18/2016.
 */
public class PG_SALE_ORDER_DETAIL_TABLE extends ABSTRACT_TABLE {
    // id
    public static final String PG_SALE_ORDER_DETAIL_ID = "PG_SALE_ORDER_DETAIL_ID";
    // PG_SALE_ORDER_ID
    public static final String PG_SALE_ORDER_ID = "PG_SALE_ORDER_ID";
    // shop id
    public static final String SHOP_ID = "SHOP_ID";
    // STAFF_ID
    public static final String STAFF_ID = "STAFF_ID";
    // PRODUCT_ID
    public static final String PRODUCT_ID = "PRODUCT_ID";
    public static final String PARENT_PRODUCT_ID = "PARENT_PRODUCT_ID";
    public static final String ORDER_DATE = "ORDER_DATE";
    public static final String QUANTITY = "QUANTITY";
    public static final String CONVFACT = "CONVFACT";
    // ngay tao
    public static final String CREATE_DATE = "CREATE_DATE";
    public static final String CREATE_USER = "CREATE_USER";
    public static final String UPDATE_DATE = "UPDATE_DATE";
    public static final String UPDATE_USER = "UPDATE_USER";

    public static final String PG_SALE_ORDER_DETAIL_TABLE = "PG_SALE_ORDER_DETAIL";

    public PG_SALE_ORDER_DETAIL_TABLE(SQLiteDatabase mDB) {
        this.tableName = PG_SALE_ORDER_DETAIL_TABLE;
        this.columns = new String[]{PG_SALE_ORDER_DETAIL_ID, PG_SALE_ORDER_ID, SHOP_ID, STAFF_ID, PRODUCT_ID, PARENT_PRODUCT_ID,
                ORDER_DATE, QUANTITY, CONVFACT, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER};
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
    public long insert(PGSaleOrderDetailDTO dto) {
        ContentValues value = initDataRowInsert(dto);
        return insert(null, value);
    }

    /**
     * initDataRowInsert
     * @param dto
     * @return
     */
    private ContentValues initDataRowInsert(PGSaleOrderDetailDTO dto) {
        ContentValues editedValues = new ContentValues();
        editedValues.put(PG_SALE_ORDER_DETAIL_ID, dto.pgSaleOrderDetailId);
        editedValues.put(PG_SALE_ORDER_ID, dto.pgSaleOrderId);
        editedValues.put(SHOP_ID, dto.shopId);
        editedValues.put(STAFF_ID, dto.staffId);
        editedValues.put(PRODUCT_ID, dto.productId);
        if(dto.parentProductId > 0) {
            editedValues.put(PARENT_PRODUCT_ID, dto.parentProductId);
        }
        editedValues.put(QUANTITY, dto.quantity);
        editedValues.put(ORDER_DATE, dto.orderDate);
        editedValues.put(CREATE_DATE, dto.createDate);
        editedValues.put(CREATE_USER, dto.createUser);
        return editedValues;
    }
    /**
     * Lay danh sach don hang, san pham cho PG
     * @return
     */
    public ListOrderViewDTO requestGetOrderView(Bundle data, long customerId) {
        String ext = data.getString(IntentConstants.INTENT_PAGE);
        boolean isGetTotalItem = data.getBoolean(IntentConstants.INTENT_IS_GET_NUM_TOTAL_ITEM);
        String staffId = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);
        String saleDate = data.getString(IntentConstants.INTENT_SALE_DATE);
        String[] paramsList = new String[] {};
        ArrayList<String> listParams = new ArrayList<String>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ");
        sql.append("(SELECT        p.PRODUCT_CODE       PRODUCT_CODE, ");
        sql.append("               p.PRODUCT_ID         PRODUCT_ID, ");
        sql.append("               p.PARENT_PRODUCT_ID  PARENT_PRODUCT_ID, ");
        sql.append("               p.PRODUCT_NAME       PRODUCT_NAME, ");
        sql.append("       		   ( CASE ");
        sql.append("          		WHEN p.SORT_ORDER IS NULL THEN 'z' ");
        sql.append("          		ELSE SORT_ORDER ");
        sql.append("          		END )        AS SORT_ORDER, ");
        sql.append("               p.CONVFACT           CONVFACT ");
        sql.append("        FROM   PRODUCT p, ap_param ap, ");
        sql.append("               PRODUCT_INFO pi ");
        sql.append("        WHERE  p.STATUS = 1 ");
        sql.append("        	     AND p.convfact > 1 ");
        sql.append("               AND pi.STATUS = 1 ");
        sql.append("               AND pi.TYPE = 1 ");
        sql.append("               AND p.uom2 = ap.ap_param_code ");
        sql.append("               AND ap.status = 1 ");
        sql.append("               AND ap.type = 'UOM2' ");
        sql.append("               AND pi.PRODUCT_INFO_ID = p.CAT_ID ");
        sql.append("               AND pi.OBJECT_TYPE = 0 )   PRO ");
        sql.append("LEFT JOIN ");
        sql.append("( ");
        sql.append("     SELECT ");
        sql.append("        PGOD.PRODUCT_ID   AS  PRODUCT_ID_PG, ");
        sql.append("        PGOD.QUANTITY     AS  QUANTITY ");
        sql.append("     FROM PG_SALE_ORDER PGO, ");
        sql.append("          PG_SALE_ORDER_DETAIL PGOD, ");
        sql.append("          PG_VISIT_PLAN VP ");
        sql.append("     WHERE 1=1 ");
        sql.append("        AND PGO.STAFF_ID = ? ");
        listParams.add(staffId);
        sql.append("        AND PGO.CUSTOMER_ID = ? ");
        listParams.add("" + customerId);
        sql.append("        AND substr(PGO.ORDER_DATE,0,11) = ? ");
        listParams.add(saleDate);
        sql.append("        AND PGO.STATUS = 1 ");
        sql.append("        AND PGO.PG_SALE_ORDER_ID = PGOD.PG_SALE_ORDER_ID ");
        sql.append("        AND VP.CUSTOMER_ID = PGO.CUSTOMER_ID  ");
        sql.append("        AND VP.STATUS in (0, 1) ");
        sql.append("	    AND DATE(VP.FROM_DATE) <= DATE(?)	");
        listParams.add(saleDate);
        sql.append("	    AND (	");
        sql.append("	        DATE(VP.TO_DATE) >= DATE(?)	");
        listParams.add(saleDate);
        sql.append("	        OR DATE(VP.TO_DATE) IS NULL	");
        sql.append("	    )	");
        sql.append(")  PGO_D ON PRO.PRODUCT_ID = PGO_D.PRODUCT_ID_PG ");
        sql.append("ORDER  BY SORT_ORDER, PRODUCT_CODE ASC");
        paramsList = listParams.toArray(new String[listParams.size()]);
        ListOrderViewDTO listResult = new ListOrderViewDTO();
        String getCountProductList = " select count(*) as total_row from ("
                + sql.toString() + ") ";
        Cursor cTmp = null;
        try {
            int total = 0;
                cTmp = rawQuery(getCountProductList, paramsList);
                if (cTmp != null) {
                    cTmp.moveToFirst();
                    total = cTmp.getInt(0);
                    listResult.totalObject = total;
                }
        } catch (Exception e) {
            VTLog.i("error", e.toString());
        } finally {
            try {
                if (cTmp != null) {
                    cTmp.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        Cursor c = null;
        try {
            c = rawQuery(sql.toString() + ext, paramsList);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        PGOrderViewDTO item = new PGOrderViewDTO();
                        item.initView(c);
                        listResult.listObject.add(item);
                    } while (c.moveToNext());
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
            }
        }
        return listResult;
    }
    /**
     * Lay danh sach don hang, san pham cho PG
     * @return
     */
    public PGCustomerViewDTO requestPGSaleOrderView(Bundle data, long customerId) {
        String staffId = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);
        String saleDate = data.getString(IntentConstants.INTENT_SALE_DATE);
        String[] paramsList = new String[] {};
        ArrayList<String> listParams = new ArrayList<String>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ");
        sql.append("( SELECT C.CUSTOMER_ID         AS CUSTOMER_ID, ");
        sql.append("        C.CUSTOMER_CODE       AS CUSTOMER_CODE, ");
        sql.append("        C.CUSTOMER_NAME       AS CUSTOMER_NAME ");
        sql.append(" FROM ");
        sql.append("  PG_VISIT_PLAN VP, ");
        sql.append("  CUSTOMER C ");
        sql.append("WHERE 1                         = 1 ");
        sql.append(" AND VP.STAFF_ID                 = ? ");
        listParams.add(staffId);
        sql.append(" AND VP.CUSTOMER_ID               = C.CUSTOMER_ID ");
        sql.append("	    AND DATE(VP.FROM_DATE) <= DATE(?)	");
        listParams.add(saleDate);
        sql.append("	    AND (	");
        sql.append("	        DATE(VP.TO_DATE) >= DATE(?)	");
        listParams.add(saleDate);
        sql.append("	        OR DATE(VP.TO_DATE) IS NULL	");
        sql.append("	    )	");
        sql.append(" AND VP.STATUS                    in (0, 1) ) CUS ");
        sql.append("LEFT JOIN ");
        sql.append("(SELECT PGO.PG_SALE_ORDER_ID     AS PG_SALE_ORDER_ID, ");
        sql.append("        PGO.CUSTOMER_ID          AS CUSTOMER_ID_PG ");
        sql.append("FROM PG_SALE_ORDER PGO ");
        sql.append("WHERE 1=1 ");
        sql.append("AND PGO.STAFF_ID                 = ? ");
        listParams.add(staffId);
        sql.append("AND PGO.CUSTOMER_ID                 = ? ");
        listParams.add("" + customerId);
        sql.append("AND SUBSTR(PGO.ORDER_DATE,0,11) = ? ");
        listParams.add(saleDate);
        sql.append("AND PGO.STATUS                   = 1) PGSALE ");
        sql.append("ON CUS.CUSTOMER_ID                = PGSALE.CUSTOMER_ID_PG");
        paramsList = listParams.toArray(new String[listParams.size()]);
        PGCustomerViewDTO listResult = new PGCustomerViewDTO();
        Cursor c = null;
        try {
            c = rawQuery(sql.toString(), paramsList);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        listResult.initView(c);
                    } while (c.moveToNext());
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
            }
        }
        return listResult;
    }

    /**
     * Lay danh sach don hang, san pham cho PG
     * @return
     */
    public boolean checkPlanLine(Bundle data) {
        boolean result = false;
        String staffId = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);
        String dateFirstMonth = DateUtils.getFirstDateOfNumberPreviousMonthWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT, -2);
        StringBuffer sqlObject = new StringBuffer();
        ArrayList<String> paramsObject = new ArrayList<String>();
        sqlObject.append("	SELECT	");
        sqlObject.append("	    count(C.CUSTOMER_ID)         AS NUM	");
        sqlObject.append("	FROM	");
        sqlObject.append("	    PG_VISIT_PLAN PVP,	");
        sqlObject.append("	    CUSTOMER C	");
        sqlObject.append("	WHERE	");
        sqlObject.append("	    1=1	");
        sqlObject.append("	    AND PVP.STAFF_ID = ?	");
        paramsObject.add(staffId);
        sqlObject.append("	    AND PVP.CUSTOMER_ID = C.CUSTOMER_ID	");
        sqlObject.append("	    AND PVP.STATUS in (0,1)	");
        sqlObject.append("	    AND (	");
        sqlObject.append("	        DATE(PVP.TO_DATE) >= DATE(?)	");
        paramsObject.add(dateFirstMonth);
        sqlObject.append("	        OR DATE(PVP.TO_DATE) IS NULL	");
        sqlObject.append("	    )	");
        Cursor c = null;
        try {
            c = rawQueries(sqlObject.toString(), paramsObject);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        if (c.getColumnIndex("NUM") > -1) {
                           if(c.getInt(c.getColumnIndex("NUM")) > 0){
                               result = true;
                           }else {
                               result = false;
                           }
                        }
                    } while (c.moveToNext());
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
            }
        }
        return result;
    }

    /**
     * Danh sach don hang truoc do
     * @param data
     * @return
     */
    public ArrayList<PGOrderViewDTO> requestALLPGSaleOrderView(Bundle data, long customerId) {
        String staffId = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);
        String saleDate = data.getString(IntentConstants.INTENT_SALE_DATE);
        String[] paramsList = new String[] {};
        ArrayList<String> listParams = new ArrayList<String>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ");
        sql.append("(SELECT        p.PRODUCT_CODE       PRODUCT_CODE, ");
        sql.append("               p.PRODUCT_ID         PRODUCT_ID, ");
        sql.append("               p.PARENT_PRODUCT_ID  PARENT_PRODUCT_ID, ");
        sql.append("               p.PRODUCT_NAME       PRODUCT_NAME, ");
        sql.append("               p.CONVFACT           CONVFACT ");
        sql.append("        FROM   PRODUCT p, ap_param ap, ");
        sql.append("               PRODUCT_INFO pi ");
        sql.append("        WHERE  p.STATUS = 1 ");
        sql.append("        	     AND p.convfact > 1 ");
        sql.append("               AND pi.STATUS = 1 ");
        sql.append("               AND pi.TYPE = 1 ");
        sql.append("               AND p.uom2 = ap.ap_param_code ");
        sql.append("               AND ap.status = 1 ");
        sql.append("               AND ap.type = 'UOM2' ");
        sql.append("               AND pi.PRODUCT_INFO_ID = p.CAT_ID ");
        sql.append("               AND pi.OBJECT_TYPE = 0 )   PRO ");
        sql.append(" JOIN ");
        sql.append("( ");
        sql.append("     SELECT ");
        sql.append("        PGOD.PRODUCT_ID   AS  PRODUCT_ID_PG, ");
        sql.append("        PGOD.QUANTITY     AS  QUANTITY ");
        sql.append("     FROM PG_SALE_ORDER PGO, ");
        sql.append("          PG_SALE_ORDER_DETAIL PGOD, ");
        sql.append("          PG_VISIT_PLAN VP ");
        sql.append("     WHERE 1=1 ");
        sql.append("        AND PGO.STAFF_ID = ? ");
        listParams.add(staffId);
        sql.append("        AND PGO.CUSTOMER_ID = ? ");
        listParams.add("" + customerId);
        sql.append("        AND substr(PGO.ORDER_DATE,0,11) = ? ");
        listParams.add(saleDate);
        sql.append("        AND PGO.STATUS = 1 ");
        sql.append("        AND PGO.PG_SALE_ORDER_ID = PGOD.PG_SALE_ORDER_ID ");
        sql.append("        AND VP.CUSTOMER_ID = PGO.CUSTOMER_ID  ");
        sql.append("        AND VP.STATUS in (0, 1) ");
        sql.append("	    AND DATE(VP.FROM_DATE) <= DATE(?)	");
        listParams.add(saleDate);
        sql.append("	    AND (	");
        sql.append("	        DATE(VP.TO_DATE) >= DATE(?)	");
        listParams.add(saleDate);
        sql.append("	        OR DATE(VP.TO_DATE) IS NULL	");
        sql.append("	    )	");
        sql.append(")  PGO_D ON PRO.PRODUCT_ID = PGO_D.PRODUCT_ID_PG ");
        sql.append("ORDER  BY PRODUCT_CODE ASC");
        paramsList = listParams.toArray(new String[listParams.size()]);
        ArrayList<PGOrderViewDTO> listResult = new ArrayList<PGOrderViewDTO>();
        Cursor c = null;
        try {
            c = rawQuery(sql.toString(), paramsList);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        PGOrderViewDTO item = new PGOrderViewDTO();
                        item.initView(c);
                        listResult.add(item);
                    } while (c.moveToNext());
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
            }
        }
        return listResult;
    }
    /**
     * Lay danh sach bao cao don hang, san pham cho PG
     * @return
     */
    public ListOrderViewDTO requestGetReportOrderView(Bundle data) {
        String ext = data.getString(IntentConstants.INTENT_PAGE);
        boolean isGetTotalItem = data.getBoolean(IntentConstants.INTENT_IS_GET_NUM_TOTAL_ITEM);
        String staffId = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);
        String fromDate = data.getString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE);
        String toDate = data.getString(IntentConstants.INTENT_FIND_ORDER_TO_DATE);
        String[] paramsList = new String[] {};
        ArrayList<String> listParams = new ArrayList<String>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ");
        sql.append("(SELECT        p.PRODUCT_CODE       PRODUCT_CODE, ");
        sql.append("               p.PRODUCT_ID         PRODUCT_ID, ");
        sql.append("               p.PARENT_PRODUCT_ID  PARENT_PRODUCT_ID, ");
        sql.append("               p.PRODUCT_NAME       PRODUCT_NAME, ");
        sql.append("       		   ( CASE ");
        sql.append("          		WHEN p.SORT_ORDER IS NULL THEN 'z' ");
        sql.append("          		ELSE SORT_ORDER ");
        sql.append("          		END )        AS SORT_ORDER, ");
        sql.append("               p.CONVFACT           CONVFACT ");
        sql.append("        FROM   PRODUCT p, ap_param ap, ");
        sql.append("               PRODUCT_INFO pi ");
        sql.append("        WHERE  p.STATUS = 1 ");
        sql.append("        	     AND p.convfact > 1 ");
        sql.append("               AND pi.STATUS = 1 ");
        sql.append("               AND pi.TYPE = 1 ");
        sql.append("               AND p.uom2 = ap.ap_param_code ");
        sql.append("               AND ap.status = 1 ");
        sql.append("               AND ap.type = 'UOM2' ");
        sql.append("               AND pi.PRODUCT_INFO_ID = p.CAT_ID ");
        sql.append("               AND pi.OBJECT_TYPE = 0 )   PRO ");
        sql.append("LEFT JOIN ");
        sql.append("( ");
        sql.append("     SELECT ");
        sql.append("        PGOD.PRODUCT_ID         AS  PRODUCT_ID_PG, ");
        sql.append("        SUM(PGOD.QUANTITY)      AS  QUANTITY  ");
        sql.append("     FROM PG_SALE_ORDER PGO, ");
        sql.append("          PG_SALE_ORDER_DETAIL PGOD ");
//        sql.append("          ,PG_VISIT_PLAN VP ");
        sql.append("     WHERE 1=1 ");
        sql.append("        AND PGO.STAFF_ID = ? ");
        listParams.add(staffId);
        if (!StringUtil.isNullOrEmpty(fromDate)) {
            sql.append("        AND substr(PGO.ORDER_DATE,0,11) >= ? ");
            listParams.add(fromDate);
        }
        if (!StringUtil.isNullOrEmpty(toDate)) {
            sql.append("        AND substr(PGO.ORDER_DATE,0,11) <= ? ");
            listParams.add(toDate);
        }
        sql.append("        AND PGO.STATUS = 1 ");
        sql.append("        AND PGO.PG_SALE_ORDER_ID = PGOD.PG_SALE_ORDER_ID ");
//        sql.append("        AND VP.CUSTOMER_ID = PGO.CUSTOMER_ID  ");
//        sql.append("        AND VP.STATUS in (0, 1) ");
        sql.append("        GROUP BY PGOD.PRODUCT_ID ");
        sql.append(")  PGO_D ON PRO.PRODUCT_ID = PGO_D.PRODUCT_ID_PG ");
        sql.append("ORDER  BY SORT_ORDER, PRODUCT_CODE ASC");
        paramsList = listParams.toArray(new String[listParams.size()]);
        ListOrderViewDTO listResult = new ListOrderViewDTO();
        String getCountProductList = " select count(*) as total_row from ("
                + sql.toString() + ") ";
        Cursor cTmp = null;
        try {
            int total = 0;
//            if(isGetTotalItem) {
            cTmp = rawQuery(getCountProductList, paramsList);
            if (cTmp != null) {
                cTmp.moveToFirst();
                total = cTmp.getInt(0);
                listResult.totalObject = total;
            }
//            }
        } catch (Exception e) {
            VTLog.i("error", e.toString());
        } finally {
            try {
                if (cTmp != null) {
                    cTmp.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        Cursor c = null;
        try {
            c = rawQuery(sql.toString() + ext, paramsList);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        PGOrderViewDTO item = new PGOrderViewDTO();
                        item.initView(c);
                        listResult.listObject.add(item);
                    } while (c.moveToNext());
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
            }
        }
        return listResult;
    }
    /**
     * Lay danh sach bao cao don hang, san pham cho PG
     * @return
     */
    public PGCustomerViewDTO requestPGReportSaleOrderView(Bundle data) {
        String staffId = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);
        String[] paramsList = new String[] {};
        ArrayList<String> listParams = new ArrayList<String>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT C.CUSTOMER_ID         AS CUSTOMER_ID, ");
        sql.append("        C.CUSTOMER_CODE       AS CUSTOMER_CODE, ");
        sql.append("        C.CUSTOMER_NAME       AS CUSTOMER_NAME ");
        sql.append("FROM ");
        sql.append("  PG_VISIT_PLAN VP, ");
        sql.append("  CUSTOMER C ");
        sql.append("WHERE 1                         = 1 ");
        sql.append("AND VP.STAFF_ID                 = ? ");
        listParams.add(staffId);
        sql.append("AND VP.CUSTOMER_ID               = C.CUSTOMER_ID ");
        sql.append("AND VP.STATUS                    = 1 ");
        paramsList = listParams.toArray(new String[listParams.size()]);
        PGCustomerViewDTO listResult = new PGCustomerViewDTO();
        Cursor c = null;
        try {
            c = rawQuery(sql.toString(), paramsList);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        listResult.initView(c);
                    } while (c.moveToNext());
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
            }
        }
        return listResult;
    }
    /**
     * Lay danh sach khach hang cham soc
     * @return
     */
    public CustomerDTO requestCustomerVisited(Bundle data) {
        String staffId = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);
        String saleDate = data.getString(IntentConstants.INTENT_SALE_DATE);
        StringBuffer sqlObject = new StringBuffer();
        ArrayList<String> paramsObject = new ArrayList<String>();
        sqlObject.append("	SELECT	");
        sqlObject.append("	    C.CUSTOMER_ID         AS CUSTOMER_ID,	");
        sqlObject.append("	    C.CUSTOMER_CODE       AS CUSTOMER_CODE,	");
        sqlObject.append("	    C.CUSTOMER_NAME       AS CUSTOMER_NAME	");
        sqlObject.append("	FROM	");
        sqlObject.append("	    PG_VISIT_PLAN PVP,	");
        sqlObject.append("	    CUSTOMER C	");
        sqlObject.append("	WHERE	");
        sqlObject.append("	    1=1	");
        sqlObject.append("	    AND PVP.STAFF_ID = ?	");
        paramsObject.add(staffId);
        sqlObject.append("	    AND PVP.CUSTOMER_ID = C.CUSTOMER_ID	");
        sqlObject.append("	    AND PVP.STATUS IN (	");
        sqlObject.append("	        0,1	");
        sqlObject.append("	    )	");
        sqlObject.append("	    AND DATE(PVP.FROM_DATE) <= DATE(?)	");
        paramsObject.add(saleDate);
        sqlObject.append("	    AND (	");
        sqlObject.append("	        DATE(PVP.TO_DATE) >= DATE(?)	");
        paramsObject.add(saleDate);
        sqlObject.append("	        OR DATE(PVP.TO_DATE) IS NULL	");
        sqlObject.append("	    )	");
        CustomerDTO result = new CustomerDTO();
        Cursor c = null;
        try {
            c = rawQueries(sqlObject.toString(), paramsObject);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        result.initView(c);
                    } while (c.moveToNext());
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
            }
        }
        return result;
    }
    /**
     * Lay danh sach khach hang dang cham soc
     * @return
     */
    public CustomerDTO requestCustomerVisiting(Bundle data) {
        String staffId = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);
        String saleDate = data.getString(IntentConstants.INTENT_SALE_DATE);
        StringBuffer sqlObject = new StringBuffer();
        ArrayList<String> paramsObject = new ArrayList<String>();
        sqlObject.append("	SELECT	");
        sqlObject.append("	    C.CUSTOMER_ID         AS CUSTOMER_ID,	");
        sqlObject.append("	    C.CUSTOMER_CODE       AS CUSTOMER_CODE,	");
        sqlObject.append("	    C.CUSTOMER_NAME       AS CUSTOMER_NAME	");
        sqlObject.append("	FROM	");
        sqlObject.append("	    PG_VISIT_PLAN PVP,	");
        sqlObject.append("	    CUSTOMER C	");
        sqlObject.append("	WHERE	");
        sqlObject.append("	    1=1	");
        sqlObject.append("	    AND PVP.STAFF_ID = ?	");
        paramsObject.add(staffId);
        sqlObject.append("	    AND PVP.CUSTOMER_ID = C.CUSTOMER_ID	");
        sqlObject.append("	    AND PVP.STATUS = 1	");
        sqlObject.append("	    AND DATE(PVP.FROM_DATE) <= DATE(?)	");
        paramsObject.add(saleDate);
        sqlObject.append("	    AND (	");
        sqlObject.append("	        DATE(PVP.TO_DATE) >= DATE(?)	");
        paramsObject.add(saleDate);
        sqlObject.append("	        OR DATE(PVP.TO_DATE) IS NULL	");
        sqlObject.append("	    )	");
        CustomerDTO result = new CustomerDTO();
        Cursor c = null;
        try {
            c = rawQueries(sqlObject.toString(), paramsObject);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        result.initView(c);
                    } while (c.moveToNext());
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
            }
        }
        return result;
    }

    /**
     * Lay danh sach khach hang dang cham soc
     * @return
     */
    public CustomerDTO requestCustomerReportVisiting(Bundle data) {
        String staffId = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);
        String saleDate = DateUtils.now();
        StringBuffer sqlObject = new StringBuffer();
        ArrayList<String> paramsObject = new ArrayList<String>();
        sqlObject.append("	SELECT	");
        sqlObject.append("	    C.CUSTOMER_ID         AS CUSTOMER_ID,	");
        sqlObject.append("	    C.CUSTOMER_CODE       AS CUSTOMER_CODE,	");
        sqlObject.append("	    C.CUSTOMER_NAME       AS CUSTOMER_NAME	");
        sqlObject.append("	FROM	");
        sqlObject.append("	    PG_VISIT_PLAN PVP,	");
        sqlObject.append("	    CUSTOMER C	");
        sqlObject.append("	WHERE	");
        sqlObject.append("	    1=1	");
        sqlObject.append("	    AND PVP.STAFF_ID = ?	");
        paramsObject.add(staffId);
        sqlObject.append("	    AND PVP.CUSTOMER_ID = C.CUSTOMER_ID	");
        sqlObject.append("	    AND PVP.STATUS = 1	");
        sqlObject.append("	    AND DATE(PVP.FROM_DATE) <= DATE(?)	");
        paramsObject.add(saleDate);
        sqlObject.append("	    AND (	");
        sqlObject.append("	        DATE(PVP.TO_DATE) >= DATE(?)	");
        paramsObject.add(saleDate);
        sqlObject.append("	        OR DATE(PVP.TO_DATE) IS NULL	");
        sqlObject.append("	    )	");
        CustomerDTO result = new CustomerDTO();
        Cursor c = null;
        try {
            c = rawQueries(sqlObject.toString(), paramsObject);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        result.initView(c);
                    } while (c.moveToNext());
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
            }
        }
        return result;
    }
}
