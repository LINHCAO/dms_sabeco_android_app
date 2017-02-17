package com.viettel.dms.dto.view;

import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.StockTotalDTO;
import com.viettel.dms.dto.db.SuggestedPriceDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.sqllite.db.SALE_ORDER_TABLE;
import com.viettel.viettellib.json.me.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yennth16 on 8/12/2016.
 */
public class ListOrderViewDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    public static int TYPE_OBJECT_SAVE_FILTER = 1;
    public static int TYPE_OBJECT_SAVE_NOT_FILTER = 2;
    // total object after research
    public int totalObject;
    // list object in page
    public ArrayList<PGOrderViewDTO> listObject;// danh sach view man hinh
    public PGSaleOrderDTO saleOrderDTO = new PGSaleOrderDTO();// dto su dung luu thong tin
    public ArrayList<PGSaleOrderDetailDTO> listSaleOrderDetailDTO = new ArrayList<>();// dto su dung luu thong tin
    public PGCustomerViewDTO pgCustomerViewDTO = new PGCustomerViewDTO();// thong tin view man hinh
    public PGSaleOrderDTO pgOrderUpdateDTO = new PGSaleOrderDTO();// dto su dung cap nhat
    // danh sach tat ca don hang su dung de luu khi chua click cac trang >1
    public ArrayList<PGOrderViewDTO> listAllOrder = new ArrayList<PGOrderViewDTO>();
    public boolean insertSuccess = false;
    public CustomerDTO customerDTO = new CustomerDTO();// khach hang cham soc
    public CustomerDTO customerDTOVisiting = new CustomerDTO();// khach hang dang cham soc
    public boolean isCheckPlanLine = false;
    public ListOrderViewDTO(){
        insertSuccess = false;
        totalObject = 0;
        isCheckPlanLine = false;
        listObject = new ArrayList<PGOrderViewDTO>();
        saleOrderDTO = new PGSaleOrderDTO();
        listSaleOrderDetailDTO = new ArrayList<>();
        pgCustomerViewDTO = new PGCustomerViewDTO();
        listAllOrder = new ArrayList<PGOrderViewDTO>();
        customerDTO = new CustomerDTO();
        customerDTOVisiting = new CustomerDTO();
    }

    /**
     * Json tao don hang pg
     * @param dto
     * @return
     */
    public JSONArray generateNewOrderSql(ListOrderViewDTO dto) {
        JSONArray listSql = new JSONArray();
        // insert pg sale order
        if (dto.saleOrderDTO != null) {
            listSql.put(dto.saleOrderDTO.generateCreateSql());
        }
        if(dto.pgOrderUpdateDTO.pgSaleOrderId > 0) {
            // update pg sale order old
            listSql.put(dto.pgOrderUpdateDTO.generateEditOrderSql());
        }
        // insert pg sale order detail
        if(dto.listSaleOrderDetailDTO.size() > 0) {
            for (int i = 0; i < dto.listSaleOrderDetailDTO.size(); i++) {
                listSql.put(dto.listSaleOrderDetailDTO.get(i).generateCreateSql());
            }
        }
        return listSql;
    }
}