/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import android.util.SparseIntArray;

import com.viettel.dms.dto.db.ApParamDTO;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.DebitDTO;
import com.viettel.dms.dto.db.DebitDetailDTO;
import com.viettel.dms.dto.db.SaleOrderDTO;
import com.viettel.dms.dto.db.SaleOrderDetailDTO;
import com.viettel.dms.dto.db.StaffCustomerDTO;
import com.viettel.dms.dto.db.StockTotalDTO;
import com.viettel.dms.dto.db.SuggestedPriceDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.sqllite.db.SALE_ORDER_TABLE;
import com.viettel.viettellib.json.me.JSONArray;

/**
 *  Thong tin don hang tren view
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class OrderViewDTO implements Serializable {
	// thong tin don hang
	public SaleOrderDTO orderInfo = new SaleOrderDTO();
	// thong tin khach hang
	public CustomerDTO customer = new CustomerDTO();
	// ds mat hang ban
	public ArrayList<OrderDetailViewDTO> listBuyOrders = new ArrayList<OrderDetailViewDTO>();
	// ds mat hang khuyen mai
	public ArrayList<OrderDetailViewDTO> listPromotionOrders = new ArrayList<OrderDetailViewDTO>();
	//ds mat hang khuyen mai dung de doi giua cac mat hang khuyen mai
	public SortedMap<Long, ArrayList<OrderDetailViewDTO>> listPromotionChange = new TreeMap<Long, ArrayList<OrderDetailViewDTO>>();
	
	// ds muc do khan
	public ArrayList<ApParamDTO> listPriority = new ArrayList<ApParamDTO>();
	// so SKU mat hang ban
//	public long numSKU;
	// ma nhan vien
	public long staffId;
	//So SKU khuyen mai
	public long numSKUPromotion;
	// bien dung de kiem tra thong tin da khoi tao hay chua
	public boolean isFirstInit = true;
	
	// bien dung de kiem tra tao moi hay chinh sua don hang
	public boolean isEditOrder =  false;
	// bien dung de kiem tra co quay ve man hinh kiem hang ton hay khong?
	public boolean isBackToRemainOrder = false; 
	
	// thoi gian bat dau ghe tham
	public String beginTimeVisit = "";
	// thoi gian chuyen mong muon
	public String deliveryTime = "";
	// ngay chuyen mong muon
	public String deliveryDate = "";
	
	// kiem tra co enable button luu hay khong
	public boolean isEnableButton = true;
	// kiem tra du lieu co thay doi hay khong
	public boolean isChangeData = false;
	
	// thong tin staffCustomer
	public StaffCustomerDTO staffCusDto = new StaffCustomerDTO();
	
	//Ds san pham ko con ton kho hoac ton kho bi thieu
	public SortedMap<Long, OrderDetailViewDTO> listLackRemainProduct = new TreeMap<Long, OrderDetailViewDTO>();
	
	//Chi tiet cong no
	public DebitDetailDTO debitDetailDto = new DebitDetailDTO();
	
	//Thong tin cong no
	public DebitDTO debitDto = new DebitDTO();
	
	//id cua debit da ton tai
	public long debitIdExist;
	
	//khoi luong cua sp KM
	public double promotionTotalWeight;
	
	//Danh sach gia nhan vien nhap khi tao don hang -> gui len server
	public ArrayList<SuggestedPriceDTO> listPrice = new ArrayList<SuggestedPriceDTO>();
	
	// ds CTKM don hang thay the
	public ArrayList<OrderDetailViewDTO> listPromotionForOrderChange = new ArrayList<OrderDetailViewDTO>();
	
	// danh sach id san pham khong co stock_total
	public SparseIntArray listProductIdNotHaveStock;
	/**
	*  Phat sinh ra cau lenh sql chuyen len server
	*  @author: TruongHN
	*  @return: JSONArray
	*  @throws:
	 */
	public JSONArray generateNewOrderSql() {
//		ArrayList<JSONArray> result = new ArrayList<JSONArray>();

//		JSONArray listDeclare = new JSONArray();
		JSONArray listSql = new JSONArray();
		long staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
		int shopId = Integer.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId);
		customer.setLastOrder(orderInfo.createDate);
		
		if (orderInfo.totalDetail != (listBuyOrders.size() + listPromotionOrders.size())){
			orderInfo.totalDetail = (listBuyOrders.size() + listPromotionOrders.size());
		}
		// insert into SalesOrder
		listSql.put(orderInfo.generateCreateSql());
		// insert vao ban database_log tuong ung
//		listSql.put(GlobalUtil.generateSqlSync(SALE_ORDER_TABLE.TABLE_NAME, DataBaseLogDTO.ACTION_INSERT, String.valueOf(orderInfo.saleOrderId), orderInfo.shopId, staffId));

		for (OrderDetailViewDTO detail: listBuyOrders) {
			if (detail.orderDetailDTO.quantity > 0){
				detail.orderDetailDTO.createDate = orderInfo.createDate;
				detail.orderDetailDTO.createUser = orderInfo.createUser;
				detail.orderDetailDTO.shopId = shopId;
				detail.orderDetailDTO.staffId = staffId;
				// insert sales order detail
				listSql.put(detail.orderDetailDTO.generateCreateSql());
				// insert vao ban database_log tuong ung
//				listSql.put(GlobalUtil.generateSqlSync(SALES_ORDER_DETAIL_TABLE.TABLE_NAME, DataBaseLogDTO.ACTION_INSERT, String.valueOf(detail.orderDetailDTO.salesOrderDetailId), orderInfo.shopId, staffId));
				 
				// cap nhat stock total //Khong cap nhat ton kho nua -> de thuc hien chuc nang hien thi mau` ton kho
				StockTotalDTO stockTotal = StockTotalDTO.createStockTotalInfo(this, detail.orderDetailDTO);

				//if(orderInfo.isNotUpdateStockTotal == false) {
					if (orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_PRESALE)) {// presale
						// Neu san pham nay chua co stock_total thi insert
						if (listProductIdNotHaveStock.indexOfKey(detail.orderDetailDTO.productId) > -1) {
							listSql.put(stockTotal.generateInsertOrUpdateSqlPresale());
							listProductIdNotHaveStock.removeAt(listProductIdNotHaveStock.indexOfKey(detail.orderDetailDTO.productId));
						}
						// Neu da co thi update
						else {
							listSql.put(stockTotal.generateDescreaseSqlPresale());
						}
					} else if (orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_VANSALE)) {// vansale
						listSql.put(stockTotal.generateDescreaseSqlVansale());
					}
				//}
				
				// insert vao ban database_log tuong ung
//				listSql.put(GlobalUtil.generateSqlSync(STOCK_TOTAL_TABLE.TABLE_STOCK_TOTAL, DataBaseLogDTO.ACTION_UPDATE, orderInfo.shopId, staffId));
			}
			
		}
		// ds san pham khuyen mai
		for (OrderDetailViewDTO promotionDetail : listPromotionOrders) {
			if (promotionDetail.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ORDER || (promotionDetail.promotionType == OrderDetailViewDTO.PROMOTION_FOR_PRODUCT
					&& ((promotionDetail.type != OrderDetailViewDTO.FREE_PRODUCT) || (promotionDetail.type == OrderDetailViewDTO.FREE_PRODUCT && promotionDetail.orderDetailDTO.quantity >= 0)))) {
			
				promotionDetail.orderDetailDTO.createDate = orderInfo.createDate;
				promotionDetail.orderDetailDTO.createUser = orderInfo.createUser;
				promotionDetail.orderDetailDTO.shopId = shopId;
				promotionDetail.orderDetailDTO.staffId = staffId;
				// insert sales order detail
				listSql.put(promotionDetail.orderDetailDTO.generateCreateSql());
				
				// cap nhat stock total //Khong cap nhat ton kho nua -> de thuc hien chuc nang hien thi mau` ton kho
				StockTotalDTO stockTotal = StockTotalDTO.createStockTotalInfo(this, promotionDetail.orderDetailDTO);

				//if(orderInfo.isNotUpdateStockTotal == false) {
					if (orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_PRESALE)) {// presale
						// Neu san pham nay chua co stock_total thi insert
						if (listProductIdNotHaveStock.indexOfKey(promotionDetail.orderDetailDTO.productId) > -1) {
							listSql.put(stockTotal.generateInsertOrUpdateSqlPresale());
							listProductIdNotHaveStock.removeAt(listProductIdNotHaveStock.indexOfKey(promotionDetail.orderDetailDTO.productId));
						}
						// Neu da co thi update
						else {
							listSql.put(stockTotal.generateDescreaseSqlPresale());
						}
					} else if (orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_VANSALE)) {// vansale
						listSql.put(stockTotal.generateDescreaseSqlVansale());
					}
				//}
				
			} else if (promotionDetail.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ZV21) {
				List<OrderDetailViewDTO> listPromotionForPromo21 =  promotionDetail.listPromotionForPromo21; 
				for (OrderDetailViewDTO detailViewDTO1 : listPromotionForPromo21) {
					detailViewDTO1.orderDetailDTO.createDate = orderInfo.createDate;
					detailViewDTO1.orderDetailDTO.createUser = orderInfo.createUser;
					detailViewDTO1.orderDetailDTO.shopId = shopId;
					detailViewDTO1.orderDetailDTO.staffId = staffId;
					// insert sales order detail
					listSql.put(detailViewDTO1.orderDetailDTO.generateCreateSql());
					
					// cap nhat stock total //Khong cap nhat ton kho nua -> de thuc hien chuc nang hien thi mau` ton kho
					StockTotalDTO stockTotal = StockTotalDTO.createStockTotalInfo(this, detailViewDTO1.orderDetailDTO);

					//if(orderInfo.isNotUpdateStockTotal == false) {
						if (orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_PRESALE)) {// presale
							// Neu san pham nay chua co stock_total thi insert
							if (listProductIdNotHaveStock.indexOfKey(promotionDetail.orderDetailDTO.productId) > -1) {
								listSql.put(stockTotal.generateInsertOrUpdateSqlPresale());
							}
							// Neu da co thi update
							else {
								listSql.put(stockTotal.generateDescreaseSqlPresale());
							}
						} else if (orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_VANSALE)) {// vansale
							listSql.put(stockTotal.generateDescreaseSqlVansale());
						}
					//}
				}
				
				break;
			}
		}
		
		// cap nhat ban customer
		// update customer set LAST_ORDER = sysdate where customer_id = 87484
		listSql.put(customer.generateUpdateFromOrderSql());

		// cap nhat staff_customer
		listSql.put(staffCusDto.generateInsertOrUpdateFromOrderSql());
		
		//Cap nhat gia tham khao
		for(SuggestedPriceDTO price : listPrice) {
			listSql.put(price.generateInsertOrUpdateFromOrderSql());
		}
		
		if (orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_VANSALE)) {// vansale
			//insert or update cong no
			listSql.put(debitDto.generateInsertOrUpdateFromOrderSql());
			
			//Insert chi tiet cong no
			listSql.put(debitDetailDto.generateInsertFromOrderSql());
		}
			
		return listSql;
	}
	
	/**
	 * 
	*  Kiem tra xem sp co con ton kho hay ko, tinh tong so luong ban cua tung sp
	*  @author: Nguyen Thanh Dung
	*  @return: void
	*  @throws:
	 */
	public void checkStockTotal() {
		//ds mat hang de hien thi con ton kho hay ko?
		SortedMap<Long, OrderDetailViewDTO> listDistinctProduct = new TreeMap<Long, OrderDetailViewDTO>();
		
		//Duyet qua ds de tinh tong so luong ban cua 1 sp
		for (OrderDetailViewDTO detail: listBuyOrders) {
			Long key2 = Long.valueOf(detail.orderDetailDTO.productId);
			if (listDistinctProduct.containsKey(key2)) {
				OrderDetailViewDTO orderDetail = listDistinctProduct.get(key2);
				orderDetail.orderDetailDTO.quantity += detail.orderDetailDTO.quantity;
			} else {
				OrderDetailViewDTO orderDetail = new OrderDetailViewDTO();
				SaleOrderDetailDTO orderDTO = new SaleOrderDetailDTO();
				orderDetail.orderDetailDTO = orderDTO;

				orderDTO.quantity = detail.orderDetailDTO.quantity;
				orderDTO.productId = detail.orderDetailDTO.productId;
				orderDetail.stock = detail.stock;
				// orderDTO.price = detail.orderDetailDTO.price;
				// orderDTO.programeCode = detail.orderDetailDTO.programeCode;

				listDistinctProduct.put(Long.valueOf(detail.orderDetailDTO.productId), orderDetail);
			}
		}
		
		//Duyet qua ds de tinh tong so luong tru vao kho cua 1 san pham
		for (OrderDetailViewDTO detail : listPromotionOrders) {
			if (detail.promotionType == OrderDetailViewDTO.PROMOTION_FOR_PRODUCT
					&& detail.type == OrderDetailViewDTO.FREE_PRODUCT && detail.orderDetailDTO.quantity >= 0) {
				Long key2 = Long.valueOf(detail.orderDetailDTO.productId);
				if(listDistinctProduct.containsKey(key2)) {
					OrderDetailViewDTO orderDetail = listDistinctProduct.get(key2);
					orderDetail.orderDetailDTO.quantity += detail.orderDetailDTO.quantity;
				} else {
					OrderDetailViewDTO orderDetail = new OrderDetailViewDTO();
					SaleOrderDetailDTO orderDTO = new SaleOrderDetailDTO();
					orderDetail.orderDetailDTO = orderDTO;
		
					orderDTO.quantity = detail.orderDetailDTO.quantity;
					orderDTO.productId = detail.orderDetailDTO.productId;
					orderDetail.stock = detail.stock;
		//			orderDTO.price = detail.orderDetailDTO.price;
		//			orderDTO.programeCode = detail.orderDetailDTO.programeCode;
		
					listDistinctProduct.put(Long.valueOf(detail.orderDetailDTO.productId), orderDetail);
				}
			} else if (detail.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ZV21) {
				List<OrderDetailViewDTO> listPromotionForPromo21 = detail.listPromotionForPromo21;
				for (OrderDetailViewDTO detailViewDTO1 : listPromotionForPromo21) {
					detail = detailViewDTO1;
					if (detail.type == OrderDetailViewDTO.FREE_PRODUCT && detail.orderDetailDTO.quantity >= 0) {
						Long key2 = Long.valueOf(detail.orderDetailDTO.productId);
						if(listDistinctProduct.containsKey(key2)) {
							OrderDetailViewDTO orderDetail = listDistinctProduct.get(key2);
							orderDetail.orderDetailDTO.quantity += detail.orderDetailDTO.quantity;
						} else {
							OrderDetailViewDTO orderDetail = new OrderDetailViewDTO();
							SaleOrderDetailDTO orderDTO = new SaleOrderDetailDTO();
							orderDetail.orderDetailDTO = orderDTO;
				
							orderDTO.quantity = detail.orderDetailDTO.quantity;
							orderDTO.productId = detail.orderDetailDTO.productId;
							orderDetail.stock = detail.stock;
				
							listDistinctProduct.put(Long.valueOf(detail.orderDetailDTO.productId), orderDetail);
						}
					}
				}

				break;
			}
		}
		
		for (OrderDetailViewDTO detail: listBuyOrders) {
			Long key2 = Long.valueOf(detail.orderDetailDTO.productId);
			if (listDistinctProduct.containsKey(key2)) {
				OrderDetailViewDTO orderDetail = listDistinctProduct.get(key2);
				detail.totalOrderQuantity = orderDetail.orderDetailDTO.quantity;
			}
		}
		
		for (OrderDetailViewDTO detail : listPromotionOrders) {
			if (detail.promotionType == OrderDetailViewDTO.PROMOTION_FOR_PRODUCT
					&& detail.type == OrderDetailViewDTO.FREE_PRODUCT && detail.orderDetailDTO.quantity >= 0) {
				Long key2 = Long.valueOf(detail.orderDetailDTO.productId);
				if(listDistinctProduct.containsKey(key2)) {
					OrderDetailViewDTO orderDetail = listDistinctProduct.get(key2);
					detail.totalOrderQuantity = orderDetail.orderDetailDTO.quantity;
				}
			} else if (detail.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ZV21) {
				List<OrderDetailViewDTO> listPromotionForPromo21 = detail.listPromotionForPromo21;
				for (OrderDetailViewDTO detailViewDTO1 : listPromotionForPromo21) {
					detail = detailViewDTO1;
					if (detail.type == OrderDetailViewDTO.FREE_PRODUCT && detail.orderDetailDTO.quantity >= 0) {
						Long key2 = Long.valueOf(detail.orderDetailDTO.productId);
						if(listDistinctProduct.containsKey(key2)) {
							OrderDetailViewDTO orderDetail = listDistinctProduct.get(key2);
							detail.totalOrderQuantity = orderDetail.orderDetailDTO.quantity;
						}
					}
				}

				break;
			}
		}
	}
	
	/**
	*  Kiem tra don hang co stock total hop le hay ko -> dung cho vansale
	*  @author: Nguyen Thanh Dung
	*  @return
	*  @return: boolean
	*  @throws: 
	*/
	
	public boolean checkStockTotalSuccess() {
		boolean result = true;
		listLackRemainProduct.clear();
		for (OrderDetailViewDTO detail: listBuyOrders) {
			if(detail.stock <= 0 || detail.totalOrderQuantity > detail.stock) {
				Long key2 = Long.valueOf(detail.orderDetailDTO.productId);
				if (!listLackRemainProduct.containsKey(key2)) {
					listLackRemainProduct.put(Long.valueOf(detail.orderDetailDTO.productId), detail);
					result = false;
				}
			}
		}
		
		for (OrderDetailViewDTO detail : listPromotionOrders) {
			if (detail.type == OrderDetailViewDTO.FREE_PRODUCT && detail.orderDetailDTO.quantity >= 0) {
				if(detail.totalOrderQuantity > 0 && (detail.stock <= 0 || detail.totalOrderQuantity > detail.stock)) {
					Long key2 = Long.valueOf(detail.orderDetailDTO.productId);
					if (!listLackRemainProduct.containsKey(key2)) {
						listLackRemainProduct.put(Long.valueOf(detail.orderDetailDTO.productId), detail);
						result = false;
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Lay danh sach id san pham trong order
	 * Danh sach bao gom trong list mua va list khuyen mai
	 * @author: quangvt1
	 * @since: 16:52:25 03-06-2014
	 * @return: List<Integer>
	 * @throws:  
	 * @return
	 */
	public List<Integer> getListIdProduct(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		// Lay list id tu danh sach san pham mua
		for (OrderDetailViewDTO dto : listBuyOrders) {
			list.add(dto.orderDetailDTO.productId);
		}
		
		// Lay list id tu danh sach san pham mua
		for (OrderDetailViewDTO dto : listPromotionOrders) {
			list.add(dto.orderDetailDTO.productId);
		}

		return list;
	}
}
