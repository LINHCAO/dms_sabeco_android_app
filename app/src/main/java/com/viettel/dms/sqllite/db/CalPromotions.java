/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.PromotionProDetailDTO;
import com.viettel.dms.dto.db.PromotionProgrameDTO;
import com.viettel.dms.dto.db.SaleOrderDetailDTO;
import com.viettel.dms.dto.view.OrderDetailViewDTO;
import com.viettel.dms.util.StringUtil;

/**
 * Class dung de tinh toan khuyen mai
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class CalPromotions {
	private static final String ZV01 = "ZV01";
	private static final String ZV02 = "ZV02";
	private static final String ZV03 = "ZV03";
	private static final String ZV04 = "ZV04";
	private static final String ZV05 = "ZV05";
	private static final String ZV06 = "ZV06";
	private static final String ZV07 = "ZV07";
	private static final String ZV08 = "ZV08";
	private static final String ZV09 = "ZV09";
	private static final String ZV10 = "ZV10";
	private static final String ZV11 = "ZV11";
	private static final String ZV12 = "ZV12";
	private static final String ZV13 = "ZV13";
	private static final String ZV14 = "ZV14";
	private static final String ZV15 = "ZV15";
	private static final String ZV16 = "ZV16";
	private static final String ZV17 = "ZV17";
	private static final String ZV18 = "ZV18";
	public static final String ZV19 = "ZV19";
	public static final String ZV20 = "ZV20";
	public static final String ZV21 = "ZV21";

	/**
	 * 
	*  Tinh KM theo san pham mua
	*  @author: Nguyen Thanh Dung
	*  @param promotionProEntity: thong tin chuong trinh KM
	*  @param sortListPromotionProDetail: ds cac sp KM da duoc sort (gom nhieu sp, nhieu level)
	*  @param sortListProductSale: ds cac sp ban da duoc sort
	*  @param listProductPromotionsale: ds cac sp KM hien thi len man hinh sau khi tinh KM
	*  @param keyList: luu key moi nhat cua ds trong ds sach sortListOutPut de phuc vu muc dinh doi hang
	*  @param sortListOutPut: ds chua ds cac sp KM co the doi (can change promotion product)
	*  @return: void
	*  @throws:
	 */
	public static void calcPromotionForProduct(
			PromotionProgrameDTO promotionProEntity,
			SortedMap<Long, List<PromotionProDetailDTO>> sortListPromotionProDetail,
			SortedMap<Long, OrderDetailViewDTO> sortListProductSale,
			List<OrderDetailViewDTO> listProductPromotionsale, Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		if (ZV01.equals(promotionProEntity.TYPE)) {
			// Line-Qtty- Percent
			calZV01(promotionProEntity, sortListPromotionProDetail,
					sortListProductSale, listProductPromotionsale,
					keyList, sortListOutPut);
		} else if (ZV02.equals(promotionProEntity.TYPE)) {// Group-Qtty-Amount
			calZV02(promotionProEntity, sortListPromotionProDetail,
					sortListProductSale, listProductPromotionsale,
					 keyList, sortListOutPut);
		} else if (ZV03.equals(promotionProEntity.TYPE)) {// Group-Qtty-Amount
			calZV03(promotionProEntity, sortListPromotionProDetail,
					sortListProductSale, listProductPromotionsale,
					 keyList, sortListOutPut);
		} else if (ZV04.equals(promotionProEntity.TYPE)) {// Group-Qtty-Amount
			calZV04(promotionProEntity, sortListPromotionProDetail,
					sortListProductSale, listProductPromotionsale,
					 keyList, sortListOutPut);
		} else if (ZV05.equals(promotionProEntity.TYPE)) {// Group-Qtty-Amount
			calZV05(promotionProEntity, sortListPromotionProDetail,
					sortListProductSale, listProductPromotionsale,
					keyList, sortListOutPut);
		} else if (ZV06.equals(promotionProEntity.TYPE)) {// Group-Qtty-Amount
			calZV06(promotionProEntity, sortListPromotionProDetail,
					sortListProductSale, listProductPromotionsale,
					keyList, sortListOutPut);
		} else if (ZV07.equals(promotionProEntity.TYPE)) {// Group-Qtty-Amount
			calZV07(promotionProEntity, sortListPromotionProDetail,
					sortListProductSale, listProductPromotionsale,
					keyList, sortListOutPut);
		} else if (ZV08.equals(promotionProEntity.TYPE)) {// Group-Qtty-Amount
			calZV08(promotionProEntity, sortListPromotionProDetail,
					sortListProductSale, listProductPromotionsale,
					keyList, sortListOutPut);
		} else if (ZV09.equals(promotionProEntity.TYPE)) {// Group-Qtty-Amount
			calZV09(promotionProEntity, sortListPromotionProDetail,
					sortListProductSale, listProductPromotionsale,
					keyList, sortListOutPut);
		} else if (ZV10.equals(promotionProEntity.TYPE)) {// Group-Amt-Percent
			calZV10(promotionProEntity, sortListPromotionProDetail,
					sortListProductSale, listProductPromotionsale,
					keyList, sortListOutPut);
		} else if (ZV11.equals(promotionProEntity.TYPE)) {// Group-Amt-Percent
			calZV11(promotionProEntity, sortListPromotionProDetail,
					sortListProductSale, listProductPromotionsale,
					keyList, sortListOutPut);
		} else if (ZV12.equals(promotionProEntity.TYPE)) {// Group-Amt-FreeItem
			calZV12(promotionProEntity, sortListPromotionProDetail,
					sortListProductSale, listProductPromotionsale,
					keyList, sortListOutPut);
		} else if (ZV13.equals(promotionProEntity.TYPE)) {// Group-Amt-FreeItem
			calZV13(promotionProEntity, sortListPromotionProDetail,
					sortListProductSale, listProductPromotionsale,
					keyList, sortListOutPut);
		} else if (ZV14.equals(promotionProEntity.TYPE)) {// Group-Amt-FreeItem
			calZV14(promotionProEntity, sortListPromotionProDetail,
					sortListProductSale, listProductPromotionsale,
					keyList, sortListOutPut);
		} else if (ZV15.equals(promotionProEntity.TYPE)) {// Group-Amt-FreeItem
			calZV15(promotionProEntity, sortListPromotionProDetail,
					sortListProductSale, listProductPromotionsale,
					keyList, sortListOutPut);
		} else if (ZV16.equals(promotionProEntity.TYPE)) {// Group-Amt-FreeItem
			calZV16(promotionProEntity, sortListPromotionProDetail,
					sortListProductSale, listProductPromotionsale,
					keyList, sortListOutPut);
		} else if (ZV17.equals(promotionProEntity.TYPE)) {// Group-Amt-FreeItem
			calZV17(promotionProEntity, sortListPromotionProDetail,
					sortListProductSale, listProductPromotionsale,
					keyList, sortListOutPut);
		} else if (ZV18.equals(promotionProEntity.TYPE)) {// Group-Amt-FreeItem
			calZV18(promotionProEntity, sortListPromotionProDetail,
					sortListProductSale, listProductPromotionsale,
					keyList, sortListOutPut);
		}
	}

	/**
	*  Tinh khuyen mai theo don hang
	*  @author: TruongHN3
	*  @param promotionProEntity: thong tin chuong trinh KM
	*  @param sortListPromotionProDetail: ds cac sp KM da duoc sort (gom nhieu sp, nhieu level)
	*  @param sortListProductSale: ds cac sp ban da duoc sort
	*  @param listProductPromotionsale: ds cac sp KM hien thi len man hinh sau khi tinh KM
	*  @param keyList: luu key moi nhat cua ds trong ds sach sortListOutPut de phuc vu muc dinh doi hang
	*  @param sortListOutPut: ds chua ds cac sp KM co the doi (can change promotion product)
	*  @return: OrderDetailViewDTO, null neu ko ap dung KM
	*  @throws:
	 */
	public static OrderDetailViewDTO calcPromotionForOrder(long amtOrder,
			PromotionProgrameDTO promotionProEntity,
			List<PromotionProDetailDTO> listPromotionProgramDetail,
			List<OrderDetailViewDTO> listProductPromotionsale, Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		OrderDetailViewDTO detailView = null;
		if (ZV19.equals(promotionProEntity.TYPE)) {// Document-Amt-Percent
			detailView = calZV19(amtOrder, promotionProEntity,listPromotionProgramDetail, listProductPromotionsale,
					keyList, sortListOutPut);
		} else if (ZV20.equals(promotionProEntity.TYPE)) {// Document-Amt-Amount
			detailView = calZV20(amtOrder, promotionProEntity, listPromotionProgramDetail, listProductPromotionsale,
					keyList, sortListOutPut);
		} else if (ZV21.equals(promotionProEntity.TYPE)) {// Document-Amt-FreeItem
			detailView = calZV21(amtOrder, promotionProEntity,listPromotionProgramDetail, listProductPromotionsale,
					keyList, sortListOutPut);
		}
		return detailView;
	}
	
	/**
	 * Tinh khuyen mai ZV01
	 * 
	 * @author: TruongHN
	 * @param listPromoProducts
	 * @param saleOrder
	 * @param orderDetail
	 * @param promotionProgram
	 * @param listPromotionDetail
	 * @return: void
	 * @throws:
	 */
	private static void calZV01(
			PromotionProgrameDTO promotionProEntity,
			SortedMap<Long, List<PromotionProDetailDTO>> sortListPromotionProDetail,
			SortedMap<Long, OrderDetailViewDTO> sortListProductSale,
			List<OrderDetailViewDTO> listProductPromotionsale,
			 Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		// Line-Qtty- Percent
		// Tinh truong hop ZV01
		// Mua 1 sản phẩm, với số lượng xác định, giảm % tổng
		// tiền). Vd: Mua 5 hộp A, giảm 5% tổng tiền.
		// kiem tra, lay chi tiet khuyen mai phu hop nhat

		for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
				.values()) {
			Long key = Long
					.valueOf(listPromotionProgramDetail.get(0).productId);
			OrderDetailViewDTO productSalePro = sortListProductSale.get(key);
			if (productSalePro != null) {
				// remove de khong tinh lai san pham nua
				sortListProductSale.remove(key);
				PromotionProDetailDTO selectedPromoDetail;
				for (int i = listPromotionProgramDetail.size() - 1; i >= 0; i--) {
					selectedPromoDetail = listPromotionProgramDetail.get(i);
					if (selectedPromoDetail.saleQTY <= productSalePro.orderDetailDTO.quantity) {
						// tim duoc khuyen mai phu hop
						// tìm khuyến mãi tương ứng với số lượng bán của sản
						// phẩm
						// bán rồi trả về
						OrderDetailViewDTO detailView = new OrderDetailViewDTO();
						SaleOrderDetailDTO selectedOrderDetail = new SaleOrderDetailDTO();
						selectedOrderDetail.productId = productSalePro.orderDetailDTO.productId;
						selectedOrderDetail.isFreeItem = 1; // mat hang khuyen
															// mai
						// so tien khuyen mai
						long totalAmount = productSalePro.orderDetailDTO.quantity
								* productSalePro.orderDetailDTO.price;
						
						// so tien khuyen mai
						if (totalAmount > Integer.MAX_VALUE) {
							selectedOrderDetail.discountAmount = (long)(totalAmount / 1000 * (int)(selectedPromoDetail.discPer * 10));
						} else {
							selectedOrderDetail.discountAmount = (long)(totalAmount * selectedPromoDetail.discPer / 100);
						}
						selectedOrderDetail.maxAmountFree = selectedOrderDetail.discountAmount;
						selectedOrderDetail.discountPercentage = selectedPromoDetail.discPer;
						
//						selectedOrderDetail.discountAmount = (long)(productSalePro.orderDetailDTO.quantity
//								* productSalePro.orderDetailDTO.price
//								* selectedPromoDetail.discPer / 100);
						selectedOrderDetail.programeCode = productSalePro.orderDetailDTO.programeCode;
						selectedOrderDetail.programeType = 0;
						detailView.productCode = "";
						detailView.productName = "";
						detailView.orderDetailDTO = selectedOrderDetail;
						detailView.type = OrderDetailViewDTO.FREE_PERCENT;
						detailView.typeName = "KM %";

						listProductPromotionsale.add(detailView);
						break;
					}
				}
			}
		}
	}

	/**
	 * Tinh khuyen mai ZV02
	 * 
	 * @author: TruongHN
	 * @param promotionProEntity
	 * @param sortListPromotionProDetail
	 * @param sortListProductSale
	 * @param listProductPromotionsale
	 * @param listProductSalePromotion
	 * @param keyList
	 * @param sortListOutPut
	 * @return: void
	 * @throws:
	 */
	private static void calZV02(
			PromotionProgrameDTO promotionProEntity,
			SortedMap<Long, List<PromotionProDetailDTO>> sortListPromotionProDetail,
			SortedMap<Long, OrderDetailViewDTO> sortListProductSale,
			List<OrderDetailViewDTO> listProductPromotionsale,
			 Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		// Mua 1 sp, voi so luong xac dinh, giam so tien
		// kiem tra, lay chi tiet khuyen mai phu hop nhat

		for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
				.values()) {
			Long key = Long
					.valueOf(listPromotionProgramDetail.get(0).productId);
			OrderDetailViewDTO productSalePro = sortListProductSale.get(key);
			if (productSalePro != null) {
				// remove de khong tinh lai san pham nua
				sortListProductSale.remove(key);
				PromotionProDetailDTO selectedPromoDetail = null;
				int multiple = 1; // cap so nhan
				int currentNumProduct = productSalePro.orderDetailDTO.quantity;

				OrderDetailViewDTO detailView = null;
				for (int i = listPromotionProgramDetail.size() - 1; i >= 0; i--) {
					selectedPromoDetail = listPromotionProgramDetail.get(i);

					if (selectedPromoDetail.saleQTY <= currentNumProduct) {
						// tim duoc khuyen mai phu hop nhat
						if (promotionProEntity.MULTIPLE == 1) {
							// CTKM co cap so nhan
							multiple = (int) (currentNumProduct / selectedPromoDetail.saleQTY);
							currentNumProduct = currentNumProduct
									% selectedPromoDetail.saleQTY;
						} else {
							multiple = 1;
							currentNumProduct = currentNumProduct
									- selectedPromoDetail.saleQTY;
						}
					} else {
						continue;
					}
					// them san pham vao ds san pham khuyen mai
					if (detailView == null) {
						detailView = new OrderDetailViewDTO();
						SaleOrderDetailDTO selectedOrderDetail = new SaleOrderDetailDTO();
						selectedOrderDetail.productId = productSalePro.orderDetailDTO.productId;
						selectedOrderDetail.isFreeItem = 1; // mat hang khuyen
															// mai
						// so tien khuyen mai
						selectedOrderDetail.discountAmount = (long)(multiple* selectedPromoDetail.discAMT);
						
						selectedOrderDetail.programeCode = productSalePro.orderDetailDTO.programeCode;
						selectedOrderDetail.programeType = 0;
						detailView.productCode = "";
						detailView.productName = "";
						detailView.orderDetailDTO = selectedOrderDetail;
						detailView.type = OrderDetailViewDTO.FREE_PRICE;
						detailView.typeName = "KM tiền";
					} else {
						detailView.orderDetailDTO.discountAmount += multiple * selectedPromoDetail.discAMT;
					}
					detailView.orderDetailDTO.maxAmountFree = detailView.orderDetailDTO.discountAmount;
					// kiem tra neu khong cho phep de quy toi uu, thi chi tinh
					// muc cao nhat
					if (promotionProEntity.recursive == 0) {
						break;
					}
				}
				if (detailView != null) {
					listProductPromotionsale.add(detailView);
				}
			}

		}
	}


	/**
	 * Tinh khuyen mai ZV03
	 * 
	 * @author: TruongHN
	 * @param listPromoProducts
	 * @param saleOrder
	 * @param orderDetail
	 * @param promotionProgram
	 * @param listPromotionDetail
	 * @return: void
	 * @throws:
	 */
	private static void calZV03(
			PromotionProgrameDTO promotionProEntity,
			SortedMap<Long, List<PromotionProDetailDTO>> sortListPromotionProDetail,
			SortedMap<Long, OrderDetailViewDTO> sortListProductSale,
			List<OrderDetailViewDTO> listProductPromotionsale,
			Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		// Line-Qtty-FreeItem
		// Mua 1 san pham, voi 1 so luong xac dinh, duoc tang 1 hoac nhieu san
		// pham nao do
		// Mua 1 san pham, voi so luong xac dinh, tang 1 or nhieu san pham nao
		// do
		if (promotionProEntity.RELATION == PromotionProgrameDTO.RELATION_OR) {
			// hoac
			for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
					.values()) {
				Long key = Long
						.valueOf(listPromotionProgramDetail.get(0).productId);
				OrderDetailViewDTO productSalePro = sortListProductSale
						.get(key);

				if (productSalePro != null) {
					// remove de khong tinh lai san pham nua
					sortListProductSale.remove(key);

					PromotionProDetailDTO selectedPromoDetail = null;
					int multiple = 1; // cap so nhan
					int currentNumProduct = productSalePro.orderDetailDTO.quantity;
					int oldQuantity = 0;

					for (int i = listPromotionProgramDetail.size() - 1; i >= 0; i--) {
						selectedPromoDetail = listPromotionProgramDetail.get(i);
						keyList++;
						if (selectedPromoDetail.saleQTY <= currentNumProduct
								&& selectedPromoDetail.saleQTY != oldQuantity) {
							oldQuantity = selectedPromoDetail.saleQTY;
							// tim duoc khuyen mai phu hop nhat
							if (promotionProEntity.MULTIPLE == 1) {
								// CTKM co cap so nhan
								multiple = (int) (currentNumProduct / selectedPromoDetail.saleQTY);
								currentNumProduct = currentNumProduct
										% selectedPromoDetail.saleQTY;
							} else {
								multiple = 1;
								currentNumProduct = currentNumProduct
										- selectedPromoDetail.saleQTY;
							}
						} else {
							continue;
						}
						// them ds mat hang lua chon
						OrderDetailViewDTO productSaleAdd;
						SaleOrderDetailDTO productSaleDTO;
						List<OrderDetailViewDTO> listProductChange = new ArrayList<OrderDetailViewDTO>();
						for (int j = 0, size = listPromotionProgramDetail
								.size(); j < size; j++) {
							if (selectedPromoDetail.saleQTY == listPromotionProgramDetail
									.get(j).saleQTY) {
								productSaleAdd = new OrderDetailViewDTO();
								productSaleDTO = new SaleOrderDetailDTO();
								productSaleAdd.orderDetailDTO = productSaleDTO;

								productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
								productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
								productSaleDTO.programeType = 0;
								productSaleDTO.productId = listPromotionProgramDetail.get(j).productId;
								productSaleDTO.price = 0;
								productSaleDTO.discountAmount = 0;// amountPromo = 0;
								productSaleDTO.isFreeItem = 1; // mat hang khuyen mai
								productSaleDTO.quantity = listPromotionProgramDetail.get(j).freeQTY * multiple;// quantityPromo = (float)listPromotionProgramDetail[j].FREE_QTY * n;
								productSaleDTO.maxQuantityFree = productSaleDTO.quantity;

								productSaleAdd.productPromoId = listPromotionProgramDetail.get(j).freeProductId;
								productSaleAdd.changeProduct = 1;
								productSaleAdd.keyList = keyList;
								productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
								productSaleAdd.typeName = "KM hàng";

								listProductChange.add(productSaleAdd);
							}
						}

						sortListOutPut.put(keyList, listProductChange);

						// tao mat hang ban dinh kem sp khuyen mai
						productSaleAdd = new OrderDetailViewDTO();
						productSaleDTO = new SaleOrderDetailDTO();
						productSaleAdd.orderDetailDTO = productSaleDTO;

						productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
						productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
						productSaleDTO.programeType = 0;
						productSaleDTO.productId = selectedPromoDetail.productId;
						productSaleAdd.productPromoId = selectedPromoDetail.freeProductId;
						productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
						productSaleAdd.typeName = "KM hàng";
						productSaleDTO.isFreeItem = 1; // mat hang khuyen mai
						productSaleDTO.price = 0;
						productSaleDTO.discountAmount = 0;// amountPromo = 0;
						if (listProductChange.size() > 1)// nếu có nhiêu hơn 2 mặt hàng khuyến mãi thì gán
															// KeyList để làm Key đối soát như ở trên
						{
							productSaleAdd.changeProduct = 1;
							productSaleAdd.keyList = keyList;
						}
						if (selectedPromoDetail.saleQTY <= productSalePro.orderDetailDTO.quantity) {
							productSaleAdd.orderDetailDTO.quantity = multiple * selectedPromoDetail.freeQTY;
							productSaleAdd.orderDetailDTO.maxQuantityFree = productSaleAdd.orderDetailDTO.quantity;
							productSaleAdd.orderDetailDTO.discountAmount = 0;
							listProductPromotionsale.add(productSaleAdd);
						}

						// kiem tra neu khong cho phep de quy toi uu, thi chi
						// tinh
						// muc cao nhat
						if (promotionProEntity.recursive == 0) {
							break;
						}
					}
				}
			}
		} else {
			// va
			for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
					.values()) {
				Long key = Long
						.valueOf(listPromotionProgramDetail.get(0).productId);
				OrderDetailViewDTO productSalePro = sortListProductSale
						.get(key);

				if (productSalePro != null) {
					// remove de khong tinh lai san pham nua
					sortListProductSale.remove(key);

					PromotionProDetailDTO selectedPromoDetail = null;
					int multiple = 1; // cap so nhan
					int currentNumProduct = productSalePro.orderDetailDTO.quantity;
					int oldQuantity = 0;

					for (int i = listPromotionProgramDetail.size() - 1; i >= 0; i--) {
						selectedPromoDetail = listPromotionProgramDetail.get(i);
						multiple = 1;
						if (selectedPromoDetail.saleQTY <= currentNumProduct
								&& selectedPromoDetail.saleQTY != oldQuantity) {
							oldQuantity = selectedPromoDetail.saleQTY;
							// tim duoc khuyen mai phu hop nhat
							if (promotionProEntity.MULTIPLE == 1) {
								// CTKM co cap so nhan
								multiple = (int) (currentNumProduct / selectedPromoDetail.saleQTY);
								currentNumProduct = currentNumProduct
										% selectedPromoDetail.saleQTY;
							} else {
								multiple = 1;
								currentNumProduct = currentNumProduct
										- selectedPromoDetail.saleQTY;
							}
						} else {
							continue;
						}
						// them ds mat hang lua chon
						OrderDetailViewDTO productSaleAdd;
						SaleOrderDetailDTO productSaleDTO;
						for (int j = 0, size = listPromotionProgramDetail
								.size(); j < size; j++) {
							if (selectedPromoDetail.saleQTY == listPromotionProgramDetail
									.get(j).saleQTY) {
								productSaleAdd = new OrderDetailViewDTO();
								productSaleDTO = new SaleOrderDetailDTO();
								productSaleAdd.orderDetailDTO = productSaleDTO;

								productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
								productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
								productSaleDTO.programeType = 0;
								productSaleDTO.isFreeItem = 1; // mat hang khuyen mai
								productSaleDTO.productId = listPromotionProgramDetail.get(j).productId;
								productSaleDTO.price = 0;
								productSaleDTO.discountAmount = 0;// amountPromo = 0;
								productSaleDTO.quantity = listPromotionProgramDetail.get(j).freeQTY * multiple;// quantityPromo = (float)listPromotionProgramDetail[j].FREE_QTY * n;
								productSaleDTO.maxQuantityFree = productSaleDTO.quantity;
								
								productSaleAdd.productPromoId = listPromotionProgramDetail.get(j).freeProductId;
//								productSaleAdd.changeProduct = 1;
//								productSaleAdd.keyList = keyList;
								productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
								productSaleAdd.typeName = "KM hàng";

								listProductPromotionsale.add(productSaleAdd);
							}
						}

						// kiem tra neu khong cho phep de quy toi uu, thi chi
						// tinh
						// muc cao nhat
						if (promotionProEntity.recursive == 0) {
							break;
						}
					}
				}

			}
		}

	}

	/**
	 * Tinh khuyen mai ZV04
	 * 
	 * @author: TruongHN
	 * @param listPromoProducts
	 * @param saleOrder
	 * @param orderDetail
	 * @param promotionProgram
	 * @param listPromotionDetail
	 * @return: void
	 * @throws:
	 */
	private static void calZV04(
			PromotionProgrameDTO promotionProEntity,
			SortedMap<Long, List<PromotionProDetailDTO>> sortListPromotionProDetail,
			SortedMap<Long, OrderDetailViewDTO> sortListProductSale,
			List<OrderDetailViewDTO> listProductPromotionsale,
			Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		// Line-Qtty- Percent
		// Tinh truong hop ZV01
		// Mua 1 sản phẩm, với số tien dat muc nao do, giảm % tổng

		for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
				.values()) {
			Long key = Long
					.valueOf(listPromotionProgramDetail.get(0).productId);
			OrderDetailViewDTO productSalePro = sortListProductSale.get(key);
			if (productSalePro != null) {
				// remove de khong tinh lai san pham nua
				sortListProductSale.remove(key);
				PromotionProDetailDTO selectedPromoDetail;
				for (int i = listPromotionProgramDetail.size() - 1; i >= 0; i--) {
					selectedPromoDetail = listPromotionProgramDetail.get(i);
					if (selectedPromoDetail.saleAMT <= productSalePro.orderDetailDTO.quantity
							* productSalePro.orderDetailDTO.price) {
						// tim duoc khuyen mai phu hop
						// tìm khuyến mãi tương ứng với số lượng bán của sản
						// phẩm
						// bán rồi trả về
						OrderDetailViewDTO detailView = new OrderDetailViewDTO();
						SaleOrderDetailDTO selectedOrderDetail = new SaleOrderDetailDTO();
						selectedOrderDetail.productId = productSalePro.orderDetailDTO.productId;
						selectedOrderDetail.isFreeItem = 1; // mat hang khuyen
															// mai
						long totalAmount = productSalePro.orderDetailDTO.quantity
								* productSalePro.orderDetailDTO.price;
						
						// so tien khuyen mai
						if (totalAmount > Integer.MAX_VALUE) {
							selectedOrderDetail.discountAmount = (long)(totalAmount / 1000 * (int)(selectedPromoDetail.discPer * 10));
						} else {
							selectedOrderDetail.discountAmount = (long)(totalAmount * selectedPromoDetail.discPer / 100);
						}
						
						selectedOrderDetail.maxAmountFree = selectedOrderDetail.discountAmount;
						selectedOrderDetail.discountPercentage = selectedPromoDetail.discPer;
						// so tien khuyen mai
//						selectedOrderDetail.discountAmount = (long)(productSalePro.orderDetailDTO.quantity
//								* productSalePro.orderDetailDTO.price
//								* selectedPromoDetail.discPer / 100);
						selectedOrderDetail.programeCode = productSalePro.orderDetailDTO.programeCode;
						selectedOrderDetail.programeType = 0;
						detailView.productCode = "";
						detailView.productName = "";
						detailView.orderDetailDTO = selectedOrderDetail;
						detailView.type = OrderDetailViewDTO.FREE_PERCENT;
						detailView.typeName = "KM %";

						listProductPromotionsale.add(detailView);
						break;
					}
				}
			}
		}
	}

	/**
	 * Tinh khuyen mai ZV05
	 * 
	 * @author: TruongHN
	 * @param listPromoProducts
	 * @param saleOrder
	 * @param orderDetail
	 * @param promotionProgram
	 * @param listPromotionDetail
	 * @return: void
	 * @throws:
	 */
	private static void calZV05(
			PromotionProgrameDTO promotionProEntity,
			SortedMap<Long, List<PromotionProDetailDTO>> sortListPromotionProDetail,
			SortedMap<Long, OrderDetailViewDTO> sortListProductSale,
			List<OrderDetailViewDTO> listProductPromotionsale,
			Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		// Mua 1 sp, voi so tien dat muc nao do thi duoc giam tru tien
		// kiem tra, lay chi tiet khuyen mai phu hop nhat

		for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
				.values()) {
			Long key = Long
					.valueOf(listPromotionProgramDetail.get(0).productId);
			OrderDetailViewDTO productSalePro = sortListProductSale.get(key);
			if (productSalePro != null) {
				// remove de khong tinh lai san pham nua
				sortListProductSale.remove(key);
				PromotionProDetailDTO selectedPromoDetail = null;
				int multiple = 1; // cap so nhan
				long salePrice = productSalePro.orderDetailDTO.quantity
						* productSalePro.orderDetailDTO.price;
				long oldSale = 0;
				OrderDetailViewDTO detailView = null;
				for (int i = listPromotionProgramDetail.size() - 1; i >= 0; i--) {
					selectedPromoDetail = listPromotionProgramDetail.get(i);

					if (selectedPromoDetail.saleAMT <= salePrice
							&& oldSale != selectedPromoDetail.saleAMT) {
						oldSale = (long) selectedPromoDetail.saleAMT;
						// tim duoc khuyen mai phu hop nhat
						if (promotionProEntity.MULTIPLE == 1) {
							// CTKM co cap so nhan
							multiple = (int) (salePrice / selectedPromoDetail.saleAMT);
							salePrice = (long) (salePrice % selectedPromoDetail.saleAMT);
						} else {
							multiple = 1;
							salePrice = salePrice
									- (long) selectedPromoDetail.saleAMT;
						}
					} else {
						continue;
					}
					// them san pham vao ds san pham khuyen mai
					if (detailView == null) {
						detailView = new OrderDetailViewDTO();
						SaleOrderDetailDTO selectedOrderDetail = new SaleOrderDetailDTO();
						selectedOrderDetail.productId = selectedPromoDetail.productId;
						selectedOrderDetail.isFreeItem = 1; // mat hang khuyen mai
						// so tien khuyen mai
						selectedOrderDetail.discountAmount = (long)(multiple * selectedPromoDetail.discAMT);
						selectedOrderDetail.programeCode = productSalePro.orderDetailDTO.programeCode;
						selectedOrderDetail.programeType = 0;
						detailView.productCode = "";
						detailView.productName = "";
						detailView.orderDetailDTO = selectedOrderDetail;
						detailView.type = OrderDetailViewDTO.FREE_PRICE;
						detailView.typeName = "KM tiền";
					} else {
						detailView.orderDetailDTO.discountAmount += multiple * selectedPromoDetail.discAMT;
					}
					
					detailView.orderDetailDTO.maxAmountFree = detailView.orderDetailDTO.discountAmount;

					// kiem tra neu khong cho phep de quy toi uu, thi chi tinh
					// muc cao nhat
					if (promotionProEntity.recursive == 0) {
						break;
					}
				}
				if (detailView != null) {
					listProductPromotionsale.add(detailView);
				}
			}

		}
	}

	/**
	 * Tinh khuyen mai ZV06
	 * 
	 * @author: TruongHN
	 * @param promotionProEntity
	 * @param sortListPromotionProDetail
	 * @param sortListProductSale
	 * @param listProductPromotionsale
	 * @param listProductSalePromotion
	 * @param keyList
	 * @param sortListOutPut
	 * @return: void
	 * @throws:
	 */
	private static void calZV06(
			PromotionProgrameDTO promotionProEntity,
			SortedMap<Long, List<PromotionProDetailDTO>> sortListPromotionProDetail,
			SortedMap<Long, OrderDetailViewDTO> sortListProductSale,
			List<OrderDetailViewDTO> listProductPromotionsale,
			Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		// Line-Qtty-FreeItem
		// Mua 1 san pham, voi 1 so tien dat muc nao do, thi duoc tang 1 hoac 1
		// nhom san pham nao do
		if (promotionProEntity.RELATION == PromotionProgrameDTO.RELATION_OR) {
			// hoac
			for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
					.values()) {
				Long key = Long
						.valueOf(listPromotionProgramDetail.get(0).productId);
				OrderDetailViewDTO productSalePro = sortListProductSale
						.get(key);

				if (productSalePro != null) {
					// remove de khong tinh lai san pham nua
					sortListProductSale.remove(key);

					PromotionProDetailDTO selectedPromoDetail = null;
					int multiple = 1; // cap so nhan
					float salePrice = productSalePro.orderDetailDTO.quantity
							* productSalePro.orderDetailDTO.price;
					float oldPrice = 0;

					for (int i = listPromotionProgramDetail.size() - 1; i >= 0; i--) {
						multiple = 1;
						selectedPromoDetail = listPromotionProgramDetail.get(i);
						keyList++;
						if (selectedPromoDetail.saleAMT <= salePrice
								&& selectedPromoDetail.saleAMT != oldPrice) {
							oldPrice = selectedPromoDetail.saleAMT;
							// tim duoc khuyen mai phu hop nhat
							if (promotionProEntity.MULTIPLE == 1) {
								// CTKM co cap so nhan
								multiple = (int) (salePrice / selectedPromoDetail.saleAMT);
								salePrice = salePrice
										% selectedPromoDetail.saleAMT;
							} else {
								multiple = 1;
								salePrice = salePrice
										- selectedPromoDetail.saleAMT;
							}
						} else {
							continue;
						}
						// them ds mat hang lua chon
						OrderDetailViewDTO productSaleAdd;
						SaleOrderDetailDTO productSaleDTO;
						List<OrderDetailViewDTO> listProductChange = new ArrayList<OrderDetailViewDTO>();
						for (int j = 0, size = listPromotionProgramDetail
								.size(); j < size; j++) {
							if (selectedPromoDetail.saleAMT == listPromotionProgramDetail
									.get(j).saleAMT) {
								productSaleAdd = new OrderDetailViewDTO();
								productSaleDTO = new SaleOrderDetailDTO();
								productSaleAdd.orderDetailDTO = productSaleDTO;

								productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
								productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
								productSaleDTO.programeType = 0;
								productSaleDTO.productId = listPromotionProgramDetail.get(j).productId;
								productSaleDTO.price = 0;
								productSaleDTO.discountAmount = 0;// amountPromo = 0;
								productSaleDTO.isFreeItem = 1; // mat hang khuyen mai
								productSaleDTO.quantity = listPromotionProgramDetail.get(j).freeQTY * multiple;// quantityPromo = (float)listPromotionProgramDetail[j].FREE_QTY * n;
								productSaleDTO.maxQuantityFree = productSaleDTO.quantity;

								productSaleAdd.productPromoId = listPromotionProgramDetail
										.get(j).freeProductId;
								productSaleAdd.changeProduct = 1;
								productSaleAdd.keyList = keyList;
								productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
								productSaleAdd.typeName = "KM hàng";

								listProductChange.add(productSaleAdd);
							}
						}

						sortListOutPut.put(keyList, listProductChange);

						// tao mat hang ban dinh kem sp khuyen mai
						productSaleAdd = new OrderDetailViewDTO();
						productSaleDTO = new SaleOrderDetailDTO();
						productSaleAdd.orderDetailDTO = productSaleDTO;

						productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
						productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
						productSaleDTO.programeType = 0;
						productSaleDTO.productId = selectedPromoDetail.productId;
						productSaleAdd.productPromoId = selectedPromoDetail.freeProductId;
						productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
						productSaleAdd.typeName = "KM hàng";
						productSaleDTO.isFreeItem = 1; // mat hang khuyen mai
						productSaleDTO.price = 0;
						productSaleDTO.discountAmount = 0;// amountPromo = 0;

						if (listProductChange.size() > 1){// nếu có nhiêu hơn mặt hàng khuyến mãi thì gán KeyList để làm Key đối soát như ở trên 2
							productSaleAdd.changeProduct = 1;
							productSaleAdd.keyList = keyList;
						}
						if (selectedPromoDetail.saleQTY <= productSalePro.orderDetailDTO.quantity) {
							productSaleAdd.orderDetailDTO.quantity = multiple * selectedPromoDetail.freeQTY;
							productSaleAdd.orderDetailDTO.maxQuantityFree = productSaleAdd.orderDetailDTO.quantity;
							
							productSaleAdd.orderDetailDTO.discountAmount = 0;
							listProductPromotionsale.add(productSaleAdd);
						}

						// kiem tra neu khong cho phep de quy toi uu, thi chi
						// tinh
						// muc cao nhat
						if (promotionProEntity.recursive == 0) {
							break;
						}
					}
				}
			}
		} else {
			// va
			for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
					.values()) {
				Long key = Long
						.valueOf(listPromotionProgramDetail.get(0).productId);
				OrderDetailViewDTO productSalePro = sortListProductSale
						.get(key);

				if (productSalePro != null) {
					// remove de khong tinh lai san pham nua
					sortListProductSale.remove(key);

					PromotionProDetailDTO selectedPromoDetail = null;
					int multiple = 1; // cap so nhan
					float salePrice = productSalePro.orderDetailDTO.quantity
							* productSalePro.orderDetailDTO.price;
					float oldPrice = 0;

					for (int i = listPromotionProgramDetail.size() - 1; i >= 0; i--) {
						multiple = 1;
						selectedPromoDetail = listPromotionProgramDetail.get(i);
						if (selectedPromoDetail.saleAMT <= salePrice
								&& selectedPromoDetail.saleAMT != oldPrice) {
							oldPrice = selectedPromoDetail.saleAMT;
							// tim duoc khuyen mai phu hop nhat
							if (promotionProEntity.MULTIPLE == 1) {
								// CTKM co cap so nhan
								multiple = (int) (salePrice / selectedPromoDetail.saleAMT);
								salePrice = salePrice
										% selectedPromoDetail.saleAMT;
							} else {
								multiple = 1;
								salePrice = salePrice
										- selectedPromoDetail.saleAMT;
							}
						} else {
							continue;
						}
						// them ds mat hang lua chon
						OrderDetailViewDTO productSaleAdd;
						SaleOrderDetailDTO productSaleDTO;
						for (int j = 0, size = listPromotionProgramDetail
								.size(); j < size; j++) {
							if (selectedPromoDetail.saleAMT == listPromotionProgramDetail
									.get(j).saleAMT) {
								productSaleAdd = new OrderDetailViewDTO();
								productSaleDTO = new SaleOrderDetailDTO();
								productSaleAdd.orderDetailDTO = productSaleDTO;

								productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
								productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
								productSaleDTO.programeType = 0;
								productSaleDTO.isFreeItem = 1; // mat hang khuyen mai
								productSaleDTO.productId = listPromotionProgramDetail
										.get(j).productId;
								productSaleDTO.price = 0;
								productSaleDTO.discountAmount = 0;// amountPromo = 0;
								productSaleDTO.quantity = listPromotionProgramDetail.get(j).freeQTY * multiple;// quantityPromo = (float)listPromotionProgramDetail[j].FREE_QTY * n;
								productSaleDTO.maxQuantityFree = productSaleDTO.quantity;
								
								productSaleAdd.productPromoId = listPromotionProgramDetail.get(j).freeProductId;
								productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
								productSaleAdd.typeName = "KM hàng";

								listProductPromotionsale.add(productSaleAdd);
							}
						}

						// kiem tra neu khong cho phep de quy toi uu, thi chi
						// tinh
						// muc cao nhat
						if (promotionProEntity.recursive == 0) {
							break;
						}
					}
				}

			}
		}

	}

	/**
	 * Tinh khuyen mai ZV07
	 * 
	 * @author: TruongHN
	 * @param promotionProEntity
	 * @param sortListPromotionProDetail
	 * @param sortListProductSale
	 * @param listProductPromotionsale
	 * @param listProductSalePromotion
	 * @param keyList
	 * @param sortListOutPut
	 * @return: void
	 * @throws:
	 */
	private static void calZV07(
			PromotionProgrameDTO promotionProEntity,
			SortedMap<Long, List<PromotionProDetailDTO>> sortListPromotionProDetail,
			SortedMap<Long, OrderDetailViewDTO> sortListProductSale,
			List<OrderDetailViewDTO> listProductPromotionsale,
			Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		long total = 0;
		long totalAmount = 0;
		PromotionProDetailDTO promotionProgramDetailGet = null;// = new
																// PromotionProDetailDTO();

		// duyệt các điều kiện khuyến mãi và count số lượng sản phẩm bán
		for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
				.values()) {
			promotionProgramDetailGet = listPromotionProgramDetail.get(0);

			Long key = Long.valueOf(promotionProgramDetailGet.productId);
			OrderDetailViewDTO productSalePro = sortListProductSale.get(key);

			if (promotionProgramDetailGet.required == 1
					&& productSalePro == null) {
				return;
			}

			if (productSalePro != null) {
				if (promotionProgramDetailGet.required == 1
						&& productSalePro.orderDetailDTO.quantity == 0) {
					return;
				}

				sortListProductSale.remove(Long
						.valueOf(promotionProgramDetailGet.productId));
				total += productSalePro.orderDetailDTO.quantity;
				totalAmount += productSalePro.orderDetailDTO.quantity
						* productSalePro.orderDetailDTO.price;
			}
		}

		List<PromotionProDetailDTO> listPromotionProgramDetail = sortListPromotionProDetail
				.get(sortListPromotionProDetail.firstKey());

		// lay muc phu hop nhat
		for (int j = listPromotionProgramDetail.size() - 1; j >= 0; j--) {
			promotionProgramDetailGet = listPromotionProgramDetail.get(j);
			if (promotionProgramDetailGet.saleQTY <= total) {
				break;
			}
		}

		if (promotionProgramDetailGet != null
				&& promotionProgramDetailGet.saleQTY <= total) {
			{
				OrderDetailViewDTO productSaleAdd = new OrderDetailViewDTO();
				SaleOrderDetailDTO productSaleDTO = new SaleOrderDetailDTO();

				productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
				productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
				productSaleDTO.programeType = 0;
				productSaleDTO.productId = 0;
				productSaleDTO.price = 0;
				
				if (totalAmount > Integer.MAX_VALUE) {
					productSaleDTO.discountAmount = (long)(totalAmount / 1000 * (int)(promotionProgramDetailGet.discPer * 10));
				} else {
					productSaleDTO.discountAmount = (long)(totalAmount * promotionProgramDetailGet.discPer / 100);
				}
				
				productSaleDTO.maxAmountFree = productSaleDTO.discountAmount;
				productSaleDTO.discountPercentage = promotionProgramDetailGet.discPer;

				productSaleDTO.quantity = 0;
				productSaleDTO.isFreeItem = 1;

				productSaleAdd.type = OrderDetailViewDTO.FREE_PERCENT;
				productSaleAdd.typeName = "KM tiền";
				productSaleAdd.productPromoId = 0;

				productSaleAdd.orderDetailDTO = productSaleDTO;
				listProductPromotionsale.add(productSaleAdd);
			}
		}
	}

	/**
	 * 
	 * Tinh khuyen mai ZV08 (Group-Qtty-Amount) Mua 1 nhóm sản phẩm nào đó
	 * – với số lượng xác định (tổng), thì được giảm trừ tiền. Vd:
	 * Mua nhóm sản phẩm (A, B, C) với số lượng 50 hộp, được giảm trừ
	 * 5000 đ.
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param listPromoProducts
	 * @param saleOrder
	 * @param orderDetail
	 * @param promotionProgram
	 * @param listPromotionDetail
	 * @return: void
	 * @throws:
	 */
	@SuppressWarnings("unused")
	private static void calZV08(
			PromotionProgrameDTO promotionProEntity,
			SortedMap<Long, List<PromotionProDetailDTO>> sortListPromotionProDetail,
			SortedMap<Long, OrderDetailViewDTO> sortListProductSale,
			List<OrderDetailViewDTO> listProductPromotionsale,
			Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		long total = 0;
		long totalAmount = 0;

		PromotionProDetailDTO promotionProgramDetailGet;// = new
														// PromotionProDetailDTO();

		// duyệt các điều kiện khuyến mãi và count số lượng sản phẩm bán
		for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
				.values()) {
			promotionProgramDetailGet = listPromotionProgramDetail.get(0);

			Long key = Long
					.valueOf(listPromotionProgramDetail.get(0).productId);
			OrderDetailViewDTO productSalePro = sortListProductSale.get(key);

			if (promotionProgramDetailGet.required == 1
					&& productSalePro == null) {
				return;
			}

			if (productSalePro != null) {
				if (promotionProgramDetailGet.required == 1
						&& productSalePro.orderDetailDTO.quantity == 0) {
					return;
				}

				sortListProductSale.remove(Long
						.valueOf(promotionProgramDetailGet.productId));
				total += productSalePro.orderDetailDTO.quantity;
				totalAmount += productSalePro.orderDetailDTO.quantity
						* productSalePro.orderDetailDTO.price;
			}
		}

		List<PromotionProDetailDTO> listPromotionProgramDetail = sortListPromotionProDetail
				.get(sortListPromotionProDetail.firstKey());

		int n = 1;
		long totalDiscount = 0;
		long old_Sale_Qty = 0;
		// vì một khuyến mãi Group có thể có nhiều mức, nên để tối ưu phải duyệt
		// tất cả khuyến mãi, kiểm tra khuyến mãi nhỏ hơn tổng hàng hiện tại thì
		// bổ sung số tiền khuyến mãi vào KQ tra về
		for (int j = listPromotionProgramDetail.size() - 1; j >= 0; j--) {
			n = 1;
			promotionProgramDetailGet = listPromotionProgramDetail.get(j);
			if (promotionProgramDetailGet.saleQTY <= total
					&& promotionProgramDetailGet.saleQTY != old_Sale_Qty) {
				old_Sale_Qty = promotionProgramDetailGet.saleQTY;

				if (promotionProEntity.MULTIPLE == 1) {
					n = (int) total / (int) promotionProgramDetailGet.saleQTY;
					total = total % promotionProgramDetailGet.saleQTY;
				} else {
					n = 1;
					total -= promotionProgramDetailGet.saleQTY;
				}

				totalDiscount += (long) promotionProgramDetailGet.discAMT * n;

				// Nếu khuyến mãi không cho phép đệ quy tối ưu thì chỉ tính mức
				// lớn nhất
				// if (promotionProEntity.IsNullRECURSIVE ||
				// promotionProEntity.recursive == 0)
				if (promotionProEntity.recursive == 0)
					break;
			}
		}

		if (totalDiscount > 0) {
			OrderDetailViewDTO productSaleAdd = new OrderDetailViewDTO();
			SaleOrderDetailDTO productSaleDTO = new SaleOrderDetailDTO();
		

			productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
			productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
			productSaleDTO.programeType = 0;
			productSaleDTO.productId = 0;
			productSaleDTO.price = 0;
			productSaleDTO.discountAmount = totalDiscount;
			productSaleDTO.maxAmountFree = productSaleDTO.discountAmount;
			productSaleDTO.quantity = 0;
			productSaleDTO.isFreeItem = 1;

			productSaleAdd.type = OrderDetailViewDTO.FREE_PRICE;
			productSaleAdd.typeName = "KM tiền";
			productSaleAdd.productPromoId = 0;

			productSaleAdd.orderDetailDTO = productSaleDTO;
			listProductPromotionsale.add(productSaleAdd);
		}
	}

	/** 
	 * 
	 * Tinh khuyen mai ZV09 (Group-Qtty-FreeItem) Mua 1 nhóm sản phẩm nào
	 * đó – với số lượng xác định (tổng), thì được tặng 1 hoặc 1
	 * nhóm sản phẩm nào đó. Vd: - Mua nhóm sản phẩm (A, B, C) với số
	 * lượng 50 hộp được tặng 5 sản phẩm A và 10 sản phẩm B và 8 sản
	 * phẩm C (1). - Mua nhóm sản phẩm (A,B,C) với số lượng 50 hộp,
	 * được lựa chọn các sản phẩm tặng (D hoặc E hoặc F) với số
	 * lượng tổng là 10. (2)
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param listPromoProducts
	 * @param saleOrder
	 * @param orderDetail
	 * @param promotionProgram
	 * @param listPromotionDetail
	 * @return: void
	 * @throws:
	 */
	@SuppressWarnings("unused")
	private static void calZV09(
			PromotionProgrameDTO promotionProEntity,
			SortedMap<Long, List<PromotionProDetailDTO>> sortListPromotionProDetail,
			SortedMap<Long, OrderDetailViewDTO> sortListProductSale,
			List<OrderDetailViewDTO> listProductPromotionsale,
			Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		if (promotionProEntity.RELATION == PromotionProgrameDTO.RELATION_OR) {
			long total = 0;
			long totalAmount = 0;

			PromotionProDetailDTO promotionProgramDetailGet;
			// kiểm tra điều kiện khuyến mãi và tính tổng hàng bán
			for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
					.values()) {
				promotionProgramDetailGet = listPromotionProgramDetail.get(0);

				Long key = Long
						.valueOf(listPromotionProgramDetail.get(0).productId);
				OrderDetailViewDTO productSalePro = sortListProductSale
						.get(key);

				if (promotionProgramDetailGet.required == 1
						&& productSalePro == null) {
					return;
				}

				if (productSalePro != null) {
					if (promotionProgramDetailGet.required == 1
							&& productSalePro.orderDetailDTO.quantity == 0) {
						return;
					}

					sortListProductSale.remove(Long
							.valueOf(promotionProgramDetailGet.productId));
					total += productSalePro.orderDetailDTO.quantity;
					totalAmount += productSalePro.orderDetailDTO.quantity
							* productSalePro.orderDetailDTO.price;
				}
			}

			// kiểm tra tổng số lượng mặt hàng bán được ở mức khuyến mãi nào
			List<PromotionProDetailDTO> listPromotionProgramDetail = sortListPromotionProDetail
					.get(sortListPromotionProDetail.firstKey());
			int n = 1;
			long old_Sale_Qty = 0;

			for (int k = listPromotionProgramDetail.size() - 1; k >= 0; k--) {
				n = 1;
				keyList++;
				promotionProgramDetailGet = listPromotionProgramDetail.get(k);
				if (promotionProgramDetailGet.saleQTY <= total
						&& promotionProgramDetailGet.saleQTY != old_Sale_Qty) {
					old_Sale_Qty = promotionProgramDetailGet.saleQTY;

					if (promotionProEntity.MULTIPLE == 1) {
						n = (int) total
								/ (int) promotionProgramDetailGet.saleQTY;
						total = total % promotionProgramDetailGet.saleQTY;
					} else {
						n = 1;
						total -= promotionProgramDetailGet.saleQTY;
					}

				} else
					continue;

				// list danh sach cac mat hang can chon
				List<OrderDetailViewDTO> listProductChange = new ArrayList<OrderDetailViewDTO>();
				for (int j = 0, size = listPromotionProgramDetail.size(); j < size; j++) {
					if (promotionProgramDetailGet.saleQTY == listPromotionProgramDetail
							.get(j).saleQTY) {
						OrderDetailViewDTO productSaleAdd = new OrderDetailViewDTO();
						SaleOrderDetailDTO productSaleDTO = new SaleOrderDetailDTO();
						productSaleAdd.orderDetailDTO = productSaleDTO;

						productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
						productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
						productSaleDTO.programeType = 0;
						productSaleDTO.productId = listPromotionProgramDetail.get(j).productId;
						productSaleDTO.price = 0;
						productSaleDTO.discountAmount = 0;// amountPromo = 0;
						
						productSaleDTO.quantity = listPromotionProgramDetail.get(j).freeQTY * n;// quantityPromo = (float)listPromotionProgramDetail[j].FREE_QTY * n;
						productSaleDTO.maxQuantityFree = productSaleDTO.quantity;
						
						productSaleDTO.isFreeItem = 1;

						productSaleAdd.productPromoId = listPromotionProgramDetail
								.get(j).freeProductId;
						productSaleAdd.changeProduct = 1;
						productSaleAdd.keyList = keyList;
						productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
						productSaleAdd.typeName = "KM hàng";

						listProductChange.add(productSaleAdd);
					}
				}

				sortListOutPut.put(keyList, listProductChange);

				OrderDetailViewDTO productSaleAdd = new OrderDetailViewDTO();
				SaleOrderDetailDTO productSaleDTO = new SaleOrderDetailDTO();
				productSaleAdd.orderDetailDTO = productSaleDTO;

				productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
				productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
				productSaleDTO.programeType = 0;
				productSaleDTO.productId = promotionProgramDetailGet.productId;
				productSaleDTO.price = 0;
				productSaleDTO.discountAmount = 0;// amountPromo = 0;
				
				productSaleDTO.quantity = promotionProgramDetailGet.freeQTY * n;// quantityPromo = (float)listPromotionProgramDetail[j].FREE_QTY * n;
				productSaleDTO.maxQuantityFree = productSaleDTO.quantity;
				
				productSaleDTO.isFreeItem = 1;
				productSaleAdd.productPromoId = promotionProgramDetailGet.freeProductId;
				if (listProductChange.size() > 1) {
					productSaleAdd.changeProduct = 1;
					productSaleAdd.keyList = keyList;
				}
				
				productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
				productSaleAdd.typeName = "KM hàng";

				listProductPromotionsale.add(productSaleAdd);

				// Nếu khuyến mãi không cho phép đệ quy tối ưu thì chỉ tính mức
				// lớn nhất
				if (promotionProEntity.recursive == 0)
					break;
			}

		} else {// km nhieu mat hang
			long total = 0;
			long totalAmount = 0;

			PromotionProDetailDTO promotionProgramDetailGet;
			for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
					.values()) {
				promotionProgramDetailGet = listPromotionProgramDetail.get(0);

				Long key = Long
						.valueOf(listPromotionProgramDetail.get(0).productId);
				OrderDetailViewDTO productSalePro = sortListProductSale
						.get(key);

				if (promotionProgramDetailGet.required == 1
						&& productSalePro == null) {
					return;
				}

				if (productSalePro != null) {
					if (promotionProgramDetailGet.required == 1
							&& productSalePro.orderDetailDTO.quantity == 0) {
						return;
					}

					sortListProductSale.remove(Long
							.valueOf(promotionProgramDetailGet.productId));
					total += productSalePro.orderDetailDTO.quantity;
					totalAmount += productSalePro.orderDetailDTO.quantity
							* productSalePro.orderDetailDTO.price;
				}
			}

			List<PromotionProDetailDTO> listPromotionProgramDetail = sortListPromotionProDetail
					.get(sortListPromotionProDetail.firstKey());
			// Add vào danh sách sort để count total cho một sản phẩm theo từng
			// loại khuyến mãi
			int n = 1;
			long old_Sale_Qty = 0;
			for (int k = listPromotionProgramDetail.size() - 1; k >= 0; k--) {
				n = 1;
				promotionProgramDetailGet = listPromotionProgramDetail.get(k);
				if (promotionProgramDetailGet.saleQTY <= total
						&& promotionProgramDetailGet.saleQTY != old_Sale_Qty) {
					old_Sale_Qty = promotionProgramDetailGet.saleQTY;

					if (promotionProEntity.MULTIPLE == 1) {
						n = (int) total
								/ (int) promotionProgramDetailGet.saleQTY;
						total = total % promotionProgramDetailGet.saleQTY;
					} else {
						n = 1;
						total -= promotionProgramDetailGet.saleQTY;
					}
				} else
					continue;

				// list danh sach cac mat hang can chon
				for (int j = 0, size = listPromotionProgramDetail.size(); j < size; j++) {
					if (promotionProgramDetailGet.saleQTY == listPromotionProgramDetail
							.get(j).saleQTY) {
						OrderDetailViewDTO productSaleAdd = new OrderDetailViewDTO();
						SaleOrderDetailDTO productSaleDTO = new SaleOrderDetailDTO();
						productSaleAdd.orderDetailDTO = productSaleDTO;

						productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
						productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
						productSaleDTO.programeType = 0;
						productSaleDTO.productId = listPromotionProgramDetail
								.get(j).productId;
						productSaleDTO.price = 0;
						productSaleDTO.discountAmount = 0;// amountPromo = 0;
						
						productSaleDTO.quantity = listPromotionProgramDetail.get(j).freeQTY * n;// quantityPromo = (float)listPromotionProgramDetail[j].FREE_QTY * n;
						productSaleDTO.maxQuantityFree = productSaleDTO.quantity;
						
						productSaleDTO.isFreeItem = 1;
						productSaleAdd.productPromoId = listPromotionProgramDetail
								.get(j).freeProductId;
						productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
						productSaleAdd.typeName = "KM hàng";

						listProductPromotionsale.add(productSaleAdd);
					}
				}

				// Nếu khuyến mãi không cho phép đệ quy tối ưu thì chỉ tính mức
				// lớn nhất
				if (promotionProEntity.recursive == 0)
					break;
			}
		}
	}

	/**
	 * 
	 * Tinh khuyen mai ZV10 (Group-Amt-Percent): Mua 1 nhóm sản phẩm nào đó
	 * – với số tiền xác định (tổng), thì được giảm % tổng tiền của
	 * nhóm này. Vd: Mua nhóm sản phẩm (A, B, C) với tổng tiền là
	 * 500.000 đ, thì được giảm 10%.
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param listPromoProducts
	 * @param saleOrder
	 * @param orderDetail
	 * @param promotionProgram
	 * @param listPromotionDetail
	 * @return: void
	 * @throws:
	 */
	@SuppressWarnings("unused")
	private static void calZV10(
			PromotionProgrameDTO promotionProEntity,
			SortedMap<Long, List<PromotionProDetailDTO>> sortListPromotionProDetail,
			SortedMap<Long, OrderDetailViewDTO> sortListProductSale,
			List<OrderDetailViewDTO> listProductPromotionsale,
			Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		long total = 0;
		long totalAmount = 0;

		PromotionProDetailDTO promotionProgramDetailGet = new PromotionProDetailDTO();

		// duyệt các điều kiện khuyến mãi và Tinh tong san pham ban
		for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
				.values()) {
			promotionProgramDetailGet = listPromotionProgramDetail.get(0);

			Long key = Long
					.valueOf(listPromotionProgramDetail.get(0).productId);
			OrderDetailViewDTO productSalePro = sortListProductSale.get(key);

			if (promotionProgramDetailGet.required == 1
					&& productSalePro == null) {
				return;
			}

			if (productSalePro != null) {
				if (promotionProgramDetailGet.required == 1
						&& productSalePro.orderDetailDTO.quantity == 0) {
					return;
				}

				sortListProductSale.remove(Long
						.valueOf(promotionProgramDetailGet.productId));
				total += productSalePro.orderDetailDTO.quantity;
				totalAmount += productSalePro.orderDetailDTO.quantity
						* productSalePro.orderDetailDTO.price;
			}
		}

		List<PromotionProDetailDTO> listPromotionProgramDetail = sortListPromotionProDetail
				.get(sortListPromotionProDetail.firstKey());

		for (int j = listPromotionProgramDetail.size() - 1; j >= 0; j--) {
			promotionProgramDetailGet = listPromotionProgramDetail.get(j);
			if (promotionProgramDetailGet.saleAMT <= totalAmount) {
				break;
			}
		}

		if (totalAmount >= promotionProgramDetailGet.saleAMT) {
			OrderDetailViewDTO productSaleAdd = new OrderDetailViewDTO();
			SaleOrderDetailDTO productSaleDTO = new SaleOrderDetailDTO();

			productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
			productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
			productSaleDTO.programeType = 0;
			productSaleDTO.productId = 0;
			productSaleDTO.price = 0;
			
			if (totalAmount > Integer.MAX_VALUE) {
				productSaleDTO.discountAmount = (long)(totalAmount / 1000 * (int)(promotionProgramDetailGet.discPer * 10));
			} else {
				productSaleDTO.discountAmount = (long)(totalAmount * promotionProgramDetailGet.discPer / 100);
			}
			
			productSaleDTO.maxAmountFree = productSaleDTO.discountAmount;
			productSaleDTO.discountPercentage = promotionProgramDetailGet.discPer;
			productSaleDTO.quantity = 0;
			productSaleDTO.isFreeItem = 1;

			productSaleAdd.type = OrderDetailViewDTO.FREE_PERCENT;
			productSaleAdd.typeName = "KM %";
			productSaleAdd.productPromoId = 0;

			productSaleAdd.orderDetailDTO = productSaleDTO;
			listProductPromotionsale.add(productSaleAdd);
		}
	}

	/**
	 * 
	 * Tinh khuyen mai ZV11 (Group-Amt-Amount): Mua 1 nhóm sản phẩm nào đó
	 * – với số tiền xác định (tổng), thì được giảm trừ 1 khoản
	 * tiền. Vd: Mua nhóm sản phẩm (A, B, C) với tổng tiền là 500.000 đ,
	 * thì được trừ 20.000 đ.
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param listPromoProducts
	 * @param saleOrder
	 * @param orderDetail
	 * @param promotionProgram
	 * @param listPromotionDetail
	 * @return: void
	 * @throws:
	 */
	@SuppressWarnings("unused")
	private static void calZV11(
			PromotionProgrameDTO promotionProEntity,
			SortedMap<Long, List<PromotionProDetailDTO>> sortListPromotionProDetail,
			SortedMap<Long, OrderDetailViewDTO> sortListProductSale,
			List<OrderDetailViewDTO> listProductPromotionsale,
			Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		long total = 0;
		long totalAmount = 0;

		PromotionProDetailDTO promotionProgramDetailGet = new PromotionProDetailDTO();

		// duyệt các điều kiện khuyến mãi và tinh tong amount
		for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
				.values()) {
			promotionProgramDetailGet = listPromotionProgramDetail.get(0);

			Long key = Long
					.valueOf(listPromotionProgramDetail.get(0).productId);
			OrderDetailViewDTO productSalePro = sortListProductSale.get(key);

			if (promotionProgramDetailGet.required == 1
					&& productSalePro == null) {
				return;
			}

			if (productSalePro != null) {
				if (promotionProgramDetailGet.required == 1
						&& productSalePro.orderDetailDTO.quantity == 0) {
					return;
				}

				sortListProductSale.remove(Long
						.valueOf(promotionProgramDetailGet.productId));
				total += productSalePro.orderDetailDTO.quantity;
				totalAmount += productSalePro.orderDetailDTO.quantity
						* productSalePro.orderDetailDTO.price;
			}
		}

		List<PromotionProDetailDTO> listPromotionProgramDetail = sortListPromotionProDetail
				.get(sortListPromotionProDetail.firstKey());

		int n = 1;
		long totalDiscount = 0;
		float old_Sale_Amt = 0;

		// vì một khuyến mãi Group có thể có nhiều mức, nên để tối ưu phải duyệt
		// tất cả khuyến mãi, kiểm tra khuyến mãi nhỏ hơn tổng hàng hiện tại thì
		// bổ sung số tiền khuyến mãi vào KQ tra về
		for (int j = listPromotionProgramDetail.size() - 1; j >= 0; j--) {
			n = 1;
			promotionProgramDetailGet = listPromotionProgramDetail.get(j);
			if (promotionProgramDetailGet.saleAMT <= totalAmount
					&& promotionProgramDetailGet.saleAMT != old_Sale_Amt) {
				old_Sale_Amt = promotionProgramDetailGet.saleAMT;
				if (promotionProEntity.MULTIPLE == 1) {
					n = (int) totalAmount
							/ (int) promotionProgramDetailGet.saleAMT;
					totalAmount = totalAmount
							% (int) promotionProgramDetailGet.saleAMT;
				} else {
					n = 1;
					totalAmount -= promotionProgramDetailGet.saleAMT;
				}

				totalDiscount += (long) promotionProgramDetailGet.discAMT * n;

				// Nếu khuyến mãi không cho phép đệ quy tối ưu thì chỉ tính mức
				// lớn nhất
				if (promotionProEntity.recursive == 0)
					break;
			}
		}

		if (totalDiscount > 0) {
			OrderDetailViewDTO productSaleAdd = new OrderDetailViewDTO();
			SaleOrderDetailDTO productSaleDTO = new SaleOrderDetailDTO();

			productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
			productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
			productSaleDTO.programeType = 0;
			productSaleDTO.productId = 0;
			productSaleDTO.price = 0;
			productSaleDTO.discountAmount = totalDiscount;
			productSaleDTO.maxAmountFree = productSaleDTO.discountAmount;
			productSaleDTO.quantity = 0;
			productSaleDTO.isFreeItem = 1;

			productSaleAdd.type = OrderDetailViewDTO.FREE_PRICE;
			productSaleAdd.typeName = "KM tiền";
			productSaleAdd.productPromoId = 0;

			productSaleAdd.orderDetailDTO = productSaleDTO;
			listProductPromotionsale.add(productSaleAdd);
		}
	}

	/**
	 * 
	 * Tinh khuyen mai ZV12 (Group-Amt-FreeItem) Mua 1 nhóm sản phẩm nào đó
	 * – với số tiền xác định (tổng), thì được tặng 1 hoặc nhóm sản
	 * phẩm nào đó. Vd: Mua nhóm sản phẩm (A, B, C) với tổng tiền
	 * 500.000 đ, thì được tặng, có quyền lựa chọn trong nhóm sản phẩm
	 * (A hoặc B hoặc C hoặc D hoặc E) với tổng sô là 20 hộp.
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param listPromoProducts
	 * @param saleOrder
	 * @param orderDetail
	 * @param promotionProgram
	 * @param listPromotionDetail
	 * @return: void
	 * @throws:
	 */
	@SuppressWarnings("unused")
	private static void calZV12(
			PromotionProgrameDTO promotionProEntity,
			SortedMap<Long, List<PromotionProDetailDTO>> sortListPromotionProDetail,
			SortedMap<Long, OrderDetailViewDTO> sortListProductSale,
			List<OrderDetailViewDTO> listProductPromotionsale,
			Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		if (promotionProEntity.RELATION == PromotionProgrameDTO.RELATION_OR) {
			long total = 0;
			long totalAmount = 0;

			PromotionProDetailDTO promotionProgramDetailGet;
			// kiểm tra điều kiện khuyến mãi và tính tổng hàng bán
			for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
					.values()) {
				promotionProgramDetailGet = listPromotionProgramDetail.get(0);

				Long key = Long
						.valueOf(listPromotionProgramDetail.get(0).productId);
				OrderDetailViewDTO productSalePro = sortListProductSale
						.get(key);

				if (promotionProgramDetailGet.required == 1
						&& productSalePro == null) {
					return;
				}

				if (productSalePro != null) {
					if (promotionProgramDetailGet.required == 1
							&& productSalePro.orderDetailDTO.quantity == 0) {
						return;
					}

					sortListProductSale.remove(Long
							.valueOf(promotionProgramDetailGet.productId));
					total += productSalePro.orderDetailDTO.quantity;
					totalAmount += productSalePro.orderDetailDTO.quantity
							* productSalePro.orderDetailDTO.price;
				}
			}

			// kiểm tra tổng số lượng mặt hàng bán được ở mức khuyến mãi nào
			List<PromotionProDetailDTO> listPromotionProgramDetail = sortListPromotionProDetail
					.get(sortListPromotionProDetail.firstKey());
			int n = 1;
			long old_Sale_Amt = 0;

			for (int k = listPromotionProgramDetail.size() - 1; k >= 0; k--) {
				n = 1;
				keyList++;
				promotionProgramDetailGet = listPromotionProgramDetail.get(k);
				if (promotionProgramDetailGet.saleAMT <= totalAmount
						&& promotionProgramDetailGet.saleAMT != old_Sale_Amt) {
					old_Sale_Amt = (long) promotionProgramDetailGet.saleAMT;

					if (promotionProEntity.MULTIPLE == 1) {
						n = (int) totalAmount
								/ (int) promotionProgramDetailGet.saleAMT;
						totalAmount = totalAmount
								% (int) promotionProgramDetailGet.saleAMT;
					} else {
						n = 1;
						totalAmount -= promotionProgramDetailGet.saleAMT;
					}

				} else
					continue;

				if (totalAmount >= 0) {

					// list danh sach cac mat hang can chon
					List<OrderDetailViewDTO> listProductChange = new ArrayList<OrderDetailViewDTO>();
					for (int j = 0, size = listPromotionProgramDetail.size(); j < size; j++) {
						if (promotionProgramDetailGet.saleAMT == listPromotionProgramDetail
								.get(j).saleAMT) {
							OrderDetailViewDTO productSaleAdd = new OrderDetailViewDTO();
							SaleOrderDetailDTO productSaleDTO = new SaleOrderDetailDTO();
							productSaleAdd.orderDetailDTO = productSaleDTO;

							productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
							productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
							productSaleDTO.programeType = 0;
							productSaleDTO.productId = listPromotionProgramDetail
									.get(j).productId;
							productSaleDTO.price = 0;
							productSaleDTO.discountAmount = 0;// amountPromo = 0;
							productSaleDTO.quantity = listPromotionProgramDetail.get(j).freeQTY * n;// quantityPromo = (float)listPromotionProgramDetail[j].FREE_QTY * n;
							productSaleDTO.maxQuantityFree = productSaleDTO.quantity;
							
							productSaleDTO.isFreeItem = 1;

							productSaleAdd.productPromoId = listPromotionProgramDetail
									.get(j).freeProductId;
							productSaleAdd.changeProduct = 1;
							productSaleAdd.keyList = keyList;
							productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
							productSaleAdd.typeName = "KM hàng";

							listProductChange.add(productSaleAdd);
						}
					}

					// Ds cac ds san pham tuy chon
					sortListOutPut.put(keyList, listProductChange);

					OrderDetailViewDTO productSaleAdd = new OrderDetailViewDTO();
					SaleOrderDetailDTO productSaleDTO = new SaleOrderDetailDTO();
					productSaleAdd.orderDetailDTO = productSaleDTO;

					productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
					productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
					productSaleDTO.programeType = 0;
					productSaleDTO.productId = promotionProgramDetailGet.productId;
					productSaleDTO.price = 0;
					productSaleDTO.discountAmount = 0;// amountPromo = 0;
					productSaleDTO.quantity = promotionProgramDetailGet.freeQTY * n;// quantityPromo = (float)listPromotionProgramDetail[j].FREE_QTY * n;
					productSaleDTO.maxQuantityFree = productSaleDTO.quantity;
					
					productSaleDTO.isFreeItem = 1;

					productSaleAdd.productPromoId = promotionProgramDetailGet.freeProductId;
					if (listProductChange.size() > 1) {
						productSaleAdd.changeProduct = 1;
						productSaleAdd.keyList = keyList;
					}
					productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
					productSaleAdd.typeName = "KM hàng";

					listProductPromotionsale.add(productSaleAdd);
				}
				// Nếu khuyến mãi không cho phép đệ quy tối ưu thì chỉ tính mức
				// lớn nhất
				if (promotionProEntity.recursive == 0)
					break;
			}

		} else {// km nhieu mat hang
			long total = 0;
			long totalAmount = 0;

			PromotionProDetailDTO promotionProgramDetailGet;
			for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
					.values()) {
				promotionProgramDetailGet = listPromotionProgramDetail.get(0);

				Long key = Long
						.valueOf(listPromotionProgramDetail.get(0).productId);
				OrderDetailViewDTO productSalePro = sortListProductSale
						.get(key);

				if (promotionProgramDetailGet.required == 1
						&& productSalePro == null) {
					return;
				}

				if (productSalePro != null) {
					if (promotionProgramDetailGet.required == 1
							&& productSalePro.orderDetailDTO.quantity == 0) {
						return;
					}

					sortListProductSale.remove(Long
							.valueOf(promotionProgramDetailGet.productId));
					total += productSalePro.orderDetailDTO.quantity;
					totalAmount += productSalePro.orderDetailDTO.quantity
							* productSalePro.orderDetailDTO.price;
				}
			}

			List<PromotionProDetailDTO> listPromotionProgramDetail = sortListPromotionProDetail
					.get(sortListPromotionProDetail.firstKey());
			// Add vào danh sách sort để count total cho một sản phẩm theo từng
			// loại khuyến mãi
			int n = 1;
			long old_Sale_Amt = 0;
			for (int k = listPromotionProgramDetail.size() - 1; k >= 0; k--) {
				n = 1;
				promotionProgramDetailGet = listPromotionProgramDetail.get(k);
				if (promotionProgramDetailGet.saleAMT <= totalAmount
						&& promotionProgramDetailGet.saleAMT != old_Sale_Amt) {
					old_Sale_Amt = (long) promotionProgramDetailGet.saleAMT;

					if (promotionProEntity.MULTIPLE == 1) {
						n = (int) totalAmount
								/ (int) promotionProgramDetailGet.saleAMT;
						totalAmount = totalAmount
								% (int) promotionProgramDetailGet.saleAMT;
					} else {
						n = 1;
						totalAmount -= promotionProgramDetailGet.saleAMT;
					}
				} else
					continue;

				// list danh sach cac mat hang can chon
				for (int j = 0, size = listPromotionProgramDetail.size(); j < size; j++) {
					if (promotionProgramDetailGet.saleAMT == listPromotionProgramDetail
							.get(j).saleAMT) {
						OrderDetailViewDTO productSaleAdd = new OrderDetailViewDTO();
						SaleOrderDetailDTO productSaleDTO = new SaleOrderDetailDTO();
						productSaleAdd.orderDetailDTO = productSaleDTO;

						productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
						productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
						productSaleDTO.programeType = 0;
						productSaleDTO.productId = listPromotionProgramDetail.get(j).productId;
						productSaleDTO.price = 0;
						productSaleDTO.discountAmount = 0;// amountPromo = 0;
						productSaleDTO.quantity = listPromotionProgramDetail.get(j).freeQTY * n;// quantityPromo = (float)listPromotionProgramDetail[j].FREE_QTY * n;
						productSaleDTO.maxQuantityFree = productSaleDTO.quantity;
						
						productSaleDTO.isFreeItem = 1;
						productSaleAdd.productPromoId = listPromotionProgramDetail
								.get(j).freeProductId;
						productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
						productSaleAdd.typeName = "KM hàng";

						listProductPromotionsale.add(productSaleAdd);
					}
				}

				// Nếu khuyến mãi không cho phép đệ quy tối ưu thì chỉ tính mức
				// lớn nhất
				if (promotionProEntity.recursive == 0)
					break;
			}
		}
	}

	/**
	 * 
	 * Tinh khuyen mai ZV13 (Bundle-Qtty-Percent): Mua theo Bộ sản phẩm
	 * (nghĩa là phải đầy đủ sản phẩm, bắt buộc)- với số lượng xác
	 * định, thì sẽ được giảm % tổng tiền của nhóm này. Vd: Mua 10
	 * hộp A và 20 hộp B, thì sẽ được giảm 5%. (Nghĩa là KH phải mua
	 * đủ sản phẩm A,B với số lượng = hoặc lớn hơn số mình đã định
	 * sẵn, thì mới được hưởng KM. Nếu mua 30 hộp A, mà chỉ mua 19 hộp
	 * B, thì vẫn không được KM. Nếu mua 10 hộp A và 21 hộp B, thì được
	 * KM. Nếu mua 20 hộp A và 40 hộp B , thì sẽ được KM gấp đôi – nhân
	 * lên theo số bộ)
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param listPromoProducts
	 * @param saleOrder
	 * @param orderDetail
	 * @param promotionProgram
	 * @param listPromotionDetail
	 * @return: void
	 * @throws:
	 */
	private static void calZV13(
			PromotionProgrameDTO promotionProEntity,
			SortedMap<Long, List<PromotionProDetailDTO>> sortListPromotionProDetail,
			SortedMap<Long, OrderDetailViewDTO> sortListProductSale,
			List<OrderDetailViewDTO> listProductPromotionsale,
			Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		int n = -1;
		boolean checkpro = true;
		int countDetail = sortListPromotionProDetail.size();
		int countSale = 0;
		long totalPro = 0;

		PromotionProDetailDTO promotionProgramDetailGet = new PromotionProDetailDTO();

		// check theo muc xem KM o muc nao
		int indexTotal = 10000000;
		for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
				.values()) {
			promotionProgramDetailGet = listPromotionProgramDetail.get(0);

			Long key = Long
					.valueOf(listPromotionProgramDetail.get(0).productId);
			OrderDetailViewDTO productSalePro = sortListProductSale.get(key);

			if (productSalePro != null) {
				// if (promotionProgramDetailGet.required == 1 &&
				// productSalePro.orderDetailDTO.quantity == 0) {
				// return;
				// }

				int index = -1;
				for (int j = 0, size = listPromotionProgramDetail.size(); j < size; j++) {
					if (productSalePro.orderDetailDTO.quantity >= listPromotionProgramDetail
							.get(j).saleQTY) {
						index = j;
					}
				}
				if (index == -1) {
					checkpro = false;
				}
				if (indexTotal != -1 && indexTotal > index) {
					indexTotal = index;
				}
			} else {
				checkpro = false;
			}

		}

		// Tinh luy ke
		if (checkpro) {
			for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
					.values()) {
				promotionProgramDetailGet = listPromotionProgramDetail
						.get(indexTotal);

				Long key = Long.valueOf(listPromotionProgramDetail
						.get(indexTotal).productId);
				OrderDetailViewDTO productSalePro = sortListProductSale
						.get(key);

				if (productSalePro != null) {
					sortListProductSale.remove(Long
							.valueOf(promotionProgramDetailGet.productId));
					if (productSalePro.orderDetailDTO.quantity < promotionProgramDetailGet.saleQTY) {
						checkpro = false;
						// break;
					}
					if (n > (int) productSalePro.orderDetailDTO.quantity
							/ (int) promotionProgramDetailGet.saleQTY
							|| n < 0) {
						n = (int) productSalePro.orderDetailDTO.quantity
								/ (int) promotionProgramDetailGet.saleQTY;
					}
					promotionProgramDetailGet.price = productSalePro.orderDetailDTO.price;
					countSale++;
				}
			}

			// Khi ko luy ke - VietHQ noi thieu doan nay
			if (promotionProEntity.MULTIPLE == 0) {
				n = 1;
			}
		} else {
			for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
					.values()) {
				promotionProgramDetailGet = listPromotionProgramDetail.get(0);

				Long key = Long
						.valueOf(listPromotionProgramDetail.get(0).productId);
				OrderDetailViewDTO productSalePro = sortListProductSale
						.get(key);

				if (productSalePro != null) {
					sortListProductSale.remove(Long
							.valueOf(promotionProgramDetailGet.productId));
				}
			}
		}

		if (checkpro && countSale == countDetail) {
			for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
					.values()) {
				promotionProgramDetailGet = listPromotionProgramDetail
						.get(indexTotal);

				long totalAmount = (long) (promotionProgramDetailGet.saleQTY * promotionProgramDetailGet.price * n);

				if (totalAmount > Integer.MAX_VALUE) {
					totalPro += (long) (totalAmount / 1000 * (int) (promotionProgramDetailGet.discPer * 10));
				} else {
					totalPro += (long) (totalAmount * promotionProgramDetailGet.discPer / 100);
				}
			}

			OrderDetailViewDTO productSaleAdd = new OrderDetailViewDTO();
			SaleOrderDetailDTO productSaleDTO = new SaleOrderDetailDTO();

			productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
			productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
			productSaleDTO.programeType = 0;
			productSaleDTO.productId = 0;
			productSaleDTO.price = 0;
			productSaleDTO.discountAmount = totalPro; // Hoi VietHQ
			productSaleDTO.maxAmountFree = productSaleDTO.discountAmount;
			productSaleDTO.discountPercentage = promotionProgramDetailGet.discPer;
			productSaleDTO.quantity = 0;
			productSaleDTO.isFreeItem = 1;

			productSaleAdd.type = OrderDetailViewDTO.FREE_PERCENT;
			productSaleAdd.typeName = "KM %";
			productSaleAdd.productPromoId = 0;

			productSaleAdd.orderDetailDTO = productSaleDTO;
			listProductPromotionsale.add(productSaleAdd);
		}
	}

	/**
	 * 
	 * Tinh khuyen mai ZV14 (Bundle-Qtty-Amount): Mua theo Bộ sản phẩm
	 * (nghĩa là phải mua đầy đủ sản phẩm, bắt buộc) - với số lượng
	 * xác định, thì sẽ được giảm trừ 1 số tiền. Vd: Mua 10 hộp A và
	 * 20 hộp B, thì sẽ được giảm 5.000 đ
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param listPromoProducts
	 * @param saleOrder
	 * @param orderDetail
	 * @param promotionProgram
	 * @param listPromotionDetail
	 * @return: void
	 * @throws:
	 */
	@SuppressWarnings("unused")
	private static void calZV14(
			PromotionProgrameDTO promotionProEntity,
			SortedMap<Long, List<PromotionProDetailDTO>> sortListPromotionProDetail,
			SortedMap<Long, OrderDetailViewDTO> sortListProductSale,
			List<OrderDetailViewDTO> listProductPromotionsale,
			Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		int n = -1;
		boolean checkpro = true;
		// int countDetail = sortListPromotionProDetail.size();
		int countSale = 0;
		long totalPro = 0;

		PromotionProDetailDTO promotionProgramDetailGet = new PromotionProDetailDTO();

		// check theo muc xem KM o muc nao
		int indexTotal = 10000000;

		/*
		 * VietHQ Khuyến mãi lưu trong sortListPromotionProDetail có nhiều mức
		 * khác nhau, Công việc vòng lặp For đầu tiền trong Vòng White để tìm
		 * mức phù hợp nhất với sản phẩm bán. sau đó dùng Old_IndexTotal để lưu
		 * trạng thái đó lại, tránh lần sau chọn trùng lặp lại
		 */

		int old_IndexTotal = 10000000;
		while (checkpro) {
			checkpro = true;
			n = -1;

			for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
					.values()) {
				promotionProgramDetailGet = listPromotionProgramDetail.get(0);

				Long key = Long
						.valueOf(listPromotionProgramDetail.get(0).productId);
				OrderDetailViewDTO productSalePro = sortListProductSale
						.get(key);

				if (productSalePro != null) {
					// Hoi VietHQ
					// if (promotionProgramDetailGet.required == 1 &&
					// productSalePro.orderDetailDTO.quantity == 0) {
					// return;
					// }

					int index = -1;
					for (int j = 0, size = listPromotionProgramDetail.size(); j < size; j++) {
						if (productSalePro.orderDetailDTO.quantity >= listPromotionProgramDetail
								.get(j).saleQTY
								&& old_IndexTotal > j
								&& (old_IndexTotal == 10000000 || (old_IndexTotal < listPromotionProgramDetail
										.size() && listPromotionProgramDetail
										.get(j).saleQTY != listPromotionProgramDetail
										.get(old_IndexTotal).saleQTY))) {
							index = j;
						}
					}
					if (index == -1) {
						checkpro = false;
					}
					if (indexTotal != -1 && indexTotal > index) {
						indexTotal = index;
					}
				} else {
					checkpro = false;
				}

			}

			old_IndexTotal = indexTotal;

			// Tinh luy ke
			if (checkpro) {
				for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
						.values()) {
					promotionProgramDetailGet = listPromotionProgramDetail
							.get(indexTotal);

					Long key = Long.valueOf(listPromotionProgramDetail
							.get(indexTotal).productId);
					OrderDetailViewDTO productSalePro = sortListProductSale
							.get(key);

					if (productSalePro != null) {
						// sortListProductSale.remove(Long.valueOf(promotionProgramDetailGet.productId));
						if (productSalePro.orderDetailDTO.quantity < promotionProgramDetailGet.saleQTY) {
							checkpro = false;
							// break;
						}
						if (n > (int) productSalePro.orderDetailDTO.quantity
								/ (int) promotionProgramDetailGet.saleQTY
								|| n < 0) {
							n = (int) productSalePro.orderDetailDTO.quantity
									/ (int) promotionProgramDetailGet.saleQTY;
						}
						// promotionProgramDetailGet.price =
						// productSalePro.orderDetailDTO.price;
						countSale++;
					}
				}

				// Khi ko luy ke - VietHQ noi thieu doan nay
				if (promotionProEntity.MULTIPLE == 0) {
					n = 1;
				}

				for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
						.values()) {
					promotionProgramDetailGet = listPromotionProgramDetail.get(indexTotal);
					Long key = Long.valueOf(listPromotionProgramDetail
							.get(indexTotal).productId);
					OrderDetailViewDTO productSalePro = sortListProductSale
							.get(key);

					if (productSalePro != null) {
						productSalePro.orderDetailDTO.quantity -= n
								* promotionProgramDetailGet.saleQTY;
					}
				}

				List<PromotionProDetailDTO> listPromotionProgramDetail = sortListPromotionProDetail
						.get(sortListPromotionProDetail.firstKey());
				promotionProgramDetailGet = listPromotionProgramDetail
						.get(indexTotal);
				totalPro += (float) promotionProgramDetailGet.discAMT * n;
			} else {
				for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
						.values()) {
					promotionProgramDetailGet = listPromotionProgramDetail
							.get(0);

					Long key = Long
							.valueOf(listPromotionProgramDetail.get(0).productId);
					OrderDetailViewDTO productSalePro = sortListProductSale
							.get(key);

					if (productSalePro != null) {
						sortListProductSale.remove(Long
								.valueOf(promotionProgramDetailGet.productId));
					}
				}
			}

			// Nếu khuyến mãi không cho phép đệ quy tối ưu thì chỉ tính mức lớn
			// nhất
			if (promotionProEntity.recursive == 0)
				break;
		}

		// add kết quả trả về
		if (totalPro > 0) {
			OrderDetailViewDTO productSaleAdd = new OrderDetailViewDTO();
			SaleOrderDetailDTO productSaleDTO = new SaleOrderDetailDTO();

			productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
			productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
			productSaleDTO.programeType = 0;
			productSaleDTO.productId = 0;
			productSaleDTO.price = 0;
			productSaleDTO.discountAmount = totalPro;
			productSaleDTO.maxAmountFree = productSaleDTO.discountAmount;
			productSaleDTO.quantity = 0;
			productSaleDTO.isFreeItem = 1;

			productSaleAdd.type = OrderDetailViewDTO.FREE_PRICE;
			productSaleAdd.typeName = "KM tiền";
			productSaleAdd.productPromoId = 0;

			productSaleAdd.orderDetailDTO = productSaleDTO;
			listProductPromotionsale.add(productSaleAdd);
		}
	}

	/**
	 * 
	 * Tinh khuyen mai ZV15 (Bundle-Qtty-FreeItem): Mua theo Bộ sản phẩm
	 * (nghĩa là phải mua đầy đủ sản phẩm, bắt buộc) - với số lượng
	 * xác định, thì sẽ được tặng 1 hoặc nhóm sản phẩm nào đó với
	 * số lượng xác định. Vd: Mua 10 hộp A và 20 hộp B, thì sẽ được
	 * tặng 5 sản phẩm (A hoặc B). Mua 10 hộp A và 20 hộp B và 30 hộp
	 * sẽ được tặng 5 sản phẩm E và 10 sản phẩm B
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param listPromoProducts
	 * @param saleOrder
	 * @param orderDetail
	 * @param promotionProgram
	 * @param listPromotionDetail
	 * @return: void
	 * @throws:
	 */
	@SuppressWarnings("unused")
	private static void calZV15(
			PromotionProgrameDTO promotionProEntity,
			SortedMap<Long, List<PromotionProDetailDTO>> sortListPromotionProDetail,
			SortedMap<Long, OrderDetailViewDTO> sortListProductSale,
			List<OrderDetailViewDTO> listProductPromotionsale,
			Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		if (promotionProEntity.RELATION == PromotionProgrameDTO.RELATION_OR) {
			int n = -1;
			boolean checkpro = true;
			// int countDetail = sortListPromotionProDetail.size();
			int countSale = 0;
			// int total = 0;
//			long totalPro = 0;

			PromotionProDetailDTO promotionProgramDetailGet = new PromotionProDetailDTO();

			// check theo muc xem KM o muc nao
			int indexTotal = 10000000;

			/*
			 * VietHQ Khuyến mãi lưu trong sortListPromotionProDetail có nhiều
			 * mức khác nhau, Công việc vòng lặp For đầu tiền trong Vòng White
			 * để tìm mức phù hợp nhất với sản phẩm bán. sau đó dùng
			 * Old_IndexTotal để lưu trạng thái đó lại, tránh lần sau chọn trùng
			 * lặp lại
			 */

			int old_IndexTotal = 10000000;
			while (checkpro) {
				checkpro = true;
				n = -1;

				for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
						.values()) {
					keyList++;
					promotionProgramDetailGet = listPromotionProgramDetail
							.get(0);

					Long key = Long
							.valueOf(listPromotionProgramDetail.get(0).productId);
					OrderDetailViewDTO productSalePro = sortListProductSale
							.get(key);

					if (productSalePro != null) {
						// Hoi VietHQ
						// if (promotionProgramDetailGet.required == 1 &&
						// productSalePro.orderDetailDTO.quantity == 0) {
						// return;
						// }

						int index = -1;
						for (int j = 0, size = listPromotionProgramDetail
								.size(); j < size; j++) {
							if (productSalePro.orderDetailDTO.quantity >= listPromotionProgramDetail
									.get(j).saleQTY
									&& old_IndexTotal > j
									&& (old_IndexTotal == 10000000 || (old_IndexTotal < listPromotionProgramDetail
											.size() && listPromotionProgramDetail
											.get(j).saleQTY != listPromotionProgramDetail
											.get(old_IndexTotal).saleQTY))) {
								index = j;
							}
						}
						if (index == -1) {
							checkpro = false;
						}
						if (indexTotal != -1 && indexTotal > index) {
							indexTotal = index;
						}
					} else {
						checkpro = false;
					}

				}

				old_IndexTotal = indexTotal;

				// Tinh luy ke
				if (checkpro) {
					for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
							.values()) {
						promotionProgramDetailGet = listPromotionProgramDetail
								.get(indexTotal);

						Long key = Long.valueOf(listPromotionProgramDetail
								.get(indexTotal).productId);
						OrderDetailViewDTO productSalePro = sortListProductSale
								.get(key);

						if (productSalePro != null) {
							// sortListProductSale
							// .remove(Long
							// .valueOf(promotionProgramDetailGet.productId));
							if (productSalePro.orderDetailDTO.quantity < promotionProgramDetailGet.saleQTY) {
								checkpro = false;
								// break;
							}
							if (n > (int) productSalePro.orderDetailDTO.quantity
									/ (int) promotionProgramDetailGet.saleQTY
									|| n < 0) {
								n = (int) productSalePro.orderDetailDTO.quantity
										/ (int) promotionProgramDetailGet.saleQTY;
							}
							countSale++;
						}
					}

					// Khi ko luy ke
					if (promotionProEntity.MULTIPLE == 0) {
						n = 1;
					}

					for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
							.values()) {
						promotionProgramDetailGet = listPromotionProgramDetail
								.get(indexTotal);
						Long key = Long.valueOf(listPromotionProgramDetail
								.get(indexTotal).productId);
						OrderDetailViewDTO productSalePro = sortListProductSale
								.get(key);

						if (productSalePro != null) {
							productSalePro.orderDetailDTO.quantity -= n
									* promotionProgramDetailGet.saleQTY;
						}
					}
				} else {
					for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
							.values()) {
						promotionProgramDetailGet = listPromotionProgramDetail
								.get(0);

						Long key = Long.valueOf(listPromotionProgramDetail
								.get(0).productId);
						OrderDetailViewDTO productSalePro = sortListProductSale
								.get(key);

						if (productSalePro != null) {
							sortListProductSale
									.remove(Long
											.valueOf(promotionProgramDetailGet.productId));
						}
					}
				}

				if (checkpro && indexTotal != 10000000 && indexTotal != -1) {
					List<PromotionProDetailDTO> listPromotionProgramDetail = sortListPromotionProDetail
							.get(sortListPromotionProDetail.firstKey());
					promotionProgramDetailGet = listPromotionProgramDetail
							.get(indexTotal);

					// list danh sach cac mat hang can chon
					List<OrderDetailViewDTO> listProductChange = new ArrayList<OrderDetailViewDTO>();
					for (int j = 0, size = listPromotionProgramDetail.size(); j < size; j++) {
						if (listPromotionProgramDetail.get(j).saleQTY == listPromotionProgramDetail
								.get(indexTotal).saleQTY) {
							OrderDetailViewDTO productSaleAdd = new OrderDetailViewDTO();
							SaleOrderDetailDTO productSaleDTO = new SaleOrderDetailDTO();
							productSaleAdd.orderDetailDTO = productSaleDTO;

							productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
							productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
							productSaleDTO.programeType = 0;
							productSaleDTO.productId = listPromotionProgramDetail
									.get(j).productId;
							productSaleDTO.price = 0;
							productSaleDTO.discountAmount = 0;// amountPromo = 0;
							productSaleDTO.quantity = listPromotionProgramDetail.get(j).freeQTY * n;// quantityPromo = (float)listPromotionProgramDetail[j].FREE_QTY* n;
							productSaleDTO.maxQuantityFree = productSaleDTO.quantity;
							productSaleDTO.isFreeItem = 1;

							productSaleAdd.productPromoId = listPromotionProgramDetail
									.get(j).freeProductId;
							productSaleAdd.changeProduct = 1;
							productSaleAdd.keyList = keyList;
							productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
							productSaleAdd.typeName = "KM hàng";

							listProductChange.add(productSaleAdd);
						}
					}

					sortListOutPut.put(keyList, listProductChange);

					OrderDetailViewDTO productSaleAdd = new OrderDetailViewDTO();
					SaleOrderDetailDTO productSaleDTO = new SaleOrderDetailDTO();
					productSaleAdd.orderDetailDTO = productSaleDTO;

					productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
					productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
					productSaleDTO.programeType = 0;
					productSaleDTO.productId = promotionProgramDetailGet.productId;
					productSaleDTO.price = 0;
					productSaleDTO.discountAmount = 0;// amountPromo = 0;
					productSaleDTO.quantity = promotionProgramDetailGet.freeQTY
							* n;// quantityPromo =
								// (float)listPromotionProgramDetail[j].FREE_QTY
								// * n;
					productSaleDTO.maxQuantityFree = productSaleDTO.quantity;
					productSaleDTO.isFreeItem = 1;

					productSaleAdd.productPromoId = promotionProgramDetailGet.freeProductId;
					if (listProductChange.size() > 1) {
						productSaleAdd.changeProduct = 1;
						productSaleAdd.keyList = keyList;
					}
					productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
					productSaleAdd.typeName = "KM hàng";

					listProductPromotionsale.add(productSaleAdd);
				}

				// Nếu khuyến mãi không cho phép đệ quy tối ưu thì chỉ tính mức
				// lớn nhất
				if (promotionProEntity.recursive == 0)
					break;
			}

		} else {
			int n = -1;
			boolean checkpro = true;
			// int countDetail = sortListPromotionProDetail.size();
			int countSale = 0;
			// int total = 0;
//			long totalPro = 0;

			PromotionProDetailDTO promotionProgramDetailGet = new PromotionProDetailDTO();
			List<PromotionProDetailDTO> listPromotionProgramDetail = new ArrayList<PromotionProDetailDTO>();

			// check theo muc xem KM o muc nao
			int indexTotal = 10000000;

			/*
			 * VietHQ Khuyến mãi lưu trong sortListPromotionProDetail có nhiều
			 * mức khác nhau, Công việc vòng lặp For đầu tiền trong Vòng White
			 * để tìm mức phù hợp nhất với sản phẩm bán. sau đó dùng
			 * Old_IndexTotal để lưu trạng thái đó lại, tránh lần sau chọn trùng
			 * lặp lại
			 */

			int old_IndexTotal = 10000000;
			while (checkpro) {
				checkpro = true;
				n = -1;

				for (Iterator<Long> it = sortListPromotionProDetail.keySet()
						.iterator(); it.hasNext();) {
					// for (int i = 0, size = sortListPromotionProDetail.size();
					// i < size; i++) {
					// for (List<PromotionProDetailDTO>
					// listPromotionProgramDetail : sortListPromotionProDetail
					// .values()) {
					Long md = it.next();
					listPromotionProgramDetail = sortListPromotionProDetail
							.get(md);
					promotionProgramDetailGet = listPromotionProgramDetail
							.get(0);

					Long key = Long
							.valueOf(listPromotionProgramDetail.get(0).productId);
					OrderDetailViewDTO productSalePro = sortListProductSale
							.get(key);

					if (productSalePro != null) {
						int index = -1;
						for (int j = 0, size = listPromotionProgramDetail
								.size(); j < size; j++) {
							if (productSalePro.orderDetailDTO.quantity >= listPromotionProgramDetail
									.get(j).saleQTY
									&& old_IndexTotal > j
									&& (old_IndexTotal == 10000000 || (old_IndexTotal < listPromotionProgramDetail
											.size() && listPromotionProgramDetail
											.get(j).saleQTY != listPromotionProgramDetail
											.get(old_IndexTotal).saleQTY))) {
								index = j;
							}
						}
						if (index == -1) {
							checkpro = false;
						}
						if (indexTotal != -1 && indexTotal > index) {
							indexTotal = index;
						}
					} else {
						checkpro = false;
					}
				}

				old_IndexTotal = indexTotal;

				// Tinh luy ke
				if (checkpro) {
					for (Iterator<Long> it = sortListPromotionProDetail
							.keySet().iterator(); it.hasNext();) {
						Long md = it.next();
						listPromotionProgramDetail = sortListPromotionProDetail
								.get(md);
						promotionProgramDetailGet = listPromotionProgramDetail
								.get(indexTotal);

						Long key = Long.valueOf(listPromotionProgramDetail
								.get(indexTotal).productId);
						OrderDetailViewDTO productSalePro = sortListProductSale
								.get(key);

						if (productSalePro != null) {
							// sortListProductSale
							// .remove(Long
							// .valueOf(promotionProgramDetailGet.productId));
							if (productSalePro.orderDetailDTO.quantity < promotionProgramDetailGet.saleQTY) {
								checkpro = false;
								// break;
							}
							if (n > (int) productSalePro.orderDetailDTO.quantity
									/ (int) promotionProgramDetailGet.saleQTY
									|| n < 0) {
								n = (int) productSalePro.orderDetailDTO.quantity
										/ (int) promotionProgramDetailGet.saleQTY;
							}
							countSale++;
						}
					}

					// Khi ko luy ke
					if (promotionProEntity.MULTIPLE == 0) {
						n = 1;
					}

					for (Iterator<Long> it = sortListPromotionProDetail
							.keySet().iterator(); it.hasNext();) {
						Long md = it.next();
						listPromotionProgramDetail = sortListPromotionProDetail
								.get(md);
						promotionProgramDetailGet = listPromotionProgramDetail
								.get(indexTotal);
						Long key = Long.valueOf(listPromotionProgramDetail
								.get(indexTotal).productId);
						OrderDetailViewDTO productSalePro = sortListProductSale
								.get(key);

						if (productSalePro != null) {
							productSalePro.orderDetailDTO.quantity -= n
									* promotionProgramDetailGet.saleQTY;
						}
					}
				} else {
					for (Iterator<Long> it = sortListPromotionProDetail
							.keySet().iterator(); it.hasNext();) {
						Long md = it.next();
						listPromotionProgramDetail = sortListPromotionProDetail
								.get(md);
						promotionProgramDetailGet = listPromotionProgramDetail
								.get(0);

						Long key = Long.valueOf(listPromotionProgramDetail
								.get(0).productId);
						OrderDetailViewDTO productSalePro = sortListProductSale
								.get(key);

						if (productSalePro != null) {
							sortListProductSale
									.remove(Long
											.valueOf(promotionProgramDetailGet.productId));
						}
					}
				}

				if (checkpro && indexTotal != 10000000 && indexTotal != -1) {
					if (promotionProEntity.MULTIPLE == 0) {
						n = 1;
					}

					for (int j = 0, size = listPromotionProgramDetail.size(); j < size; j++) {
						if (listPromotionProgramDetail.get(j).saleQTY == listPromotionProgramDetail
								.get(indexTotal).saleQTY) {
							OrderDetailViewDTO productSaleAdd = new OrderDetailViewDTO();
							SaleOrderDetailDTO productSaleDTO = new SaleOrderDetailDTO();

							productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
							productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
							productSaleDTO.programeType = 0;
							productSaleDTO.productId = listPromotionProgramDetail
									.get(j).productId;
							productSaleDTO.price = 0;
							productSaleDTO.discountAmount = 0;
							productSaleDTO.quantity = listPromotionProgramDetail.get(j).freeQTY * n;
							productSaleDTO.maxQuantityFree = productSaleDTO.quantity;
							productSaleDTO.isFreeItem = 1;

							productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
							productSaleAdd.typeName = "KM hàng";
							productSaleAdd.productPromoId = listPromotionProgramDetail
									.get(j).freeProductId;

							productSaleAdd.orderDetailDTO = productSaleDTO;
							listProductPromotionsale.add(productSaleAdd);
						}
					}
				}
				// Nếu khuyến mãi không cho phép đệ quy tối ưu thì chỉ tính mức
				// lớn nhất
				if (promotionProEntity.recursive == 0)
					break;
			}
		}
	}

	/**
	 * 
	 * Tinh khuyen mai ZV16 (Bundle-Amt-Percent): Mua theo Bộ sản phẩm
	 * (nghĩa là phải đầy đủ sản phẩm, bắt buộc)- với số tiền xác
	 * định, thì sẽ được giảm %. Vd: Mua 500.000 sản phẩm A và 300.000
	 * sản phẩm B, sẽ được giảm 5% trên tổng tiền mua các sản phẩm A
	 * và B).
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param listPromoProducts
	 * @param saleOrder
	 * @param orderDetail
	 * @param promotionProgram
	 * @param listPromotionDetail
	 * @return: void
	 * @throws:
	 */
	private static void calZV16(
			PromotionProgrameDTO promotionProEntity,
			SortedMap<Long, List<PromotionProDetailDTO>> sortListPromotionProDetail,
			SortedMap<Long, OrderDetailViewDTO> sortListProductSale,
			List<OrderDetailViewDTO> listProductPromotionsale,
			Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		long n = -1;
		boolean checkpro = true;
		int countDetail = sortListPromotionProDetail.size();
		int countSale = 0;
		long totalPro = 0;

		PromotionProDetailDTO promotionProgramDetailGet = new PromotionProDetailDTO();

		// check theo muc xem KM o muc nao
		int indexTotal = 10000000;

		/*
		 * VietHQ Khuyến mãi lưu trong sortListPromotionProDetail có nhiều mức
		 * khác nhau, Công việc vòng lặp For đầu tiền trong Vòng White để tìm
		 * mức phù hợp nhất với sản phẩm bán. sau đó dùng Old_IndexTotal để lưu
		 * trạng thái đó lại, tránh lần sau chọn trùng lặp lại
		 */

		for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
				.values()) {
			promotionProgramDetailGet = listPromotionProgramDetail.get(0);

			Long key = Long
					.valueOf(listPromotionProgramDetail.get(0).productId);
			OrderDetailViewDTO productSalePro = sortListProductSale.get(key);

			if (productSalePro != null) {
				// Hoi VietHQ
				// if (promotionProgramDetailGet.required == 1 &&
				// productSalePro.orderDetailDTO.quantity == 0) {
				// return;
				// }

				int index = -1;
				for (int j = 0, size = listPromotionProgramDetail.size(); j < size; j++) {
					if (productSalePro.orderDetailDTO.quantity
							* productSalePro.orderDetailDTO.price >= listPromotionProgramDetail
								.get(j).saleAMT) {
						index = j;
					}
				}
				if (index == -1) {
					checkpro = false;
				}
				if (indexTotal != -1 && indexTotal > index) {
					indexTotal = index;
				}
			} else {
				checkpro = false;
			}
		}

		// Tinh luy ke
		if (checkpro) {
			for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
					.values()) {
				promotionProgramDetailGet = listPromotionProgramDetail
						.get(indexTotal);

				Long key = Long.valueOf(listPromotionProgramDetail
						.get(indexTotal).productId);
				OrderDetailViewDTO productSalePro = sortListProductSale
						.get(key);

				if (productSalePro != null) {
					sortListProductSale.remove(Long
							.valueOf(promotionProgramDetailGet.productId));

					if (productSalePro.orderDetailDTO.quantity
							* productSalePro.orderDetailDTO.price < promotionProgramDetailGet.saleAMT) {
						checkpro = false;
						// break;
					}
					if (n > (long) productSalePro.orderDetailDTO.quantity
							* productSalePro.orderDetailDTO.price
							/ (int) promotionProgramDetailGet.saleAMT
							|| n < 0) {
						n = (long) productSalePro.orderDetailDTO.quantity
								* productSalePro.orderDetailDTO.price
								/ (int) promotionProgramDetailGet.saleAMT;
					}
					// promotionProgramDetailGet.price =
					// productSalePro.orderDetailDTO.price;
					countSale++;
				}
			}
		} else {
			for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
					.values()) {
				promotionProgramDetailGet = listPromotionProgramDetail.get(0);

				Long key = Long
						.valueOf(listPromotionProgramDetail.get(0).productId);
				OrderDetailViewDTO productSalePro = sortListProductSale
						.get(key);

				if (productSalePro != null) {
					sortListProductSale.remove(Long
							.valueOf(promotionProgramDetailGet.productId));
				}
			}
		}

		if (checkpro && countSale == countDetail) {
			if (promotionProEntity.MULTIPLE == 0) {
				n = 1;
			}

			for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
					.values()) {
				promotionProgramDetailGet = listPromotionProgramDetail.get(indexTotal);
				
				long totalAmount = (long) (promotionProgramDetailGet.saleAMT * n);

				if (totalAmount > Integer.MAX_VALUE) {
					totalPro += (long) (totalAmount / 1000 * (int) (promotionProgramDetailGet.discPer * 10));
				} else {
					totalPro += (long) (totalAmount * promotionProgramDetailGet.discPer / 100);
				}
			}

			OrderDetailViewDTO productSaleAdd = new OrderDetailViewDTO();
			SaleOrderDetailDTO productSaleDTO = new SaleOrderDetailDTO();

			productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
			productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
			productSaleDTO.programeType = 0;
			productSaleDTO.productId = 0;
			productSaleDTO.price = 0;
			productSaleDTO.discountAmount = totalPro;
			productSaleDTO.maxAmountFree = productSaleDTO.discountAmount;
			productSaleDTO.discountPercentage = promotionProgramDetailGet.discPer;
			productSaleDTO.quantity = 0;
			productSaleDTO.isFreeItem = 1;

			productSaleAdd.type = OrderDetailViewDTO.FREE_PERCENT;
			productSaleAdd.typeName = "KM %";
			productSaleAdd.productPromoId = 0;

			productSaleAdd.orderDetailDTO = productSaleDTO;
			listProductPromotionsale.add(productSaleAdd);
		}
	}

	/**
	 * 
	 * Tinh khuyen mai ZV17 (Bundle-Amt-Amt): Mua theo Bộ sản phẩm (nghĩa
	 * là phải đầy đủ sản phẩm, bắt buộc)- với số tiền xác định,
	 * thì sẽ được trừ tiền. Vd: Mua 500.000 sản phẩm A và 300.000 sản
	 * phẩm B, sẽ được giảm trừ 50.000 đ
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param listPromoProducts
	 * @param saleOrder
	 * @param orderDetail
	 * @param promotionProgram
	 * @param listPromotionDetail
	 * @return: void
	 * @throws:
	 */
	@SuppressWarnings("unused")
	private static void calZV17(
			PromotionProgrameDTO promotionProEntity,
			SortedMap<Long, List<PromotionProDetailDTO>> sortListPromotionProDetail,
			SortedMap<Long, OrderDetailViewDTO> sortListProductSale,
			List<OrderDetailViewDTO> listProductPromotionsale,
			Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		int n = -1;
		boolean checkpro = true;
		int countDetail = sortListPromotionProDetail.size();
		int countSale = 0;
		long totalPro = 0;

		PromotionProDetailDTO promotionProgramDetailGet = new PromotionProDetailDTO();

		// check theo muc xem KM o muc nao
		int indexTotal = 10000000;

		/*
		 * VietHQ Khuyến mãi lưu trong sortListPromotionProDetail có nhiều mức
		 * khác nhau, Công việc vòng lặp For đầu tiền trong Vòng White để tìm
		 * mức phù hợp nhất với sản phẩm bán. sau đó dùng Old_IndexTotal để lưu
		 * trạng thái đó lại, tránh lần sau chọn trùng lặp lại
		 */

		int old_IndexTotal = 10000000;

		for (OrderDetailViewDTO productSalePro : sortListProductSale.values()) {
			productSalePro.orderDetailDTO.price = productSalePro.orderDetailDTO.price
					* productSalePro.orderDetailDTO.quantity;
		}

		while (checkpro) {
			checkpro = true;
			n = -1;

			for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
					.values()) {
				promotionProgramDetailGet = listPromotionProgramDetail.get(0);

				Long key = Long
						.valueOf(listPromotionProgramDetail.get(0).productId);
				OrderDetailViewDTO productSalePro = sortListProductSale
						.get(key);

				if (productSalePro != null) {
					// Hoi VietHQ
					// if (promotionProgramDetailGet.required == 1 &&
					// productSalePro.orderDetailDTO.quantity == 0) {
					// return;
					// }

					int index = -1;
					for (int j = 0, size = listPromotionProgramDetail.size(); j < size; j++) {
						if (productSalePro.orderDetailDTO.price >= listPromotionProgramDetail
								.get(j).saleAMT
								&& old_IndexTotal > j
								&& (old_IndexTotal == 10000000 || (old_IndexTotal < listPromotionProgramDetail
										.size() && listPromotionProgramDetail
										.get(j).saleAMT != listPromotionProgramDetail
										.get(old_IndexTotal).saleAMT))) {
							index = j;
						}
					}
					if (index == -1) {
						checkpro = false;
					}
					if (indexTotal != -1 && indexTotal > index) {
						indexTotal = index;
					}
				} else {
					checkpro = false;
				}

			}

			old_IndexTotal = indexTotal;

			// Tinh luy ke
			if (checkpro) {
				for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
						.values()) {
					promotionProgramDetailGet = listPromotionProgramDetail
							.get(indexTotal);

					Long key = Long.valueOf(listPromotionProgramDetail
							.get(indexTotal).productId);
					OrderDetailViewDTO productSalePro = sortListProductSale
							.get(key);

					if (productSalePro != null) {
						// sortListProductSale.remove(Long.valueOf(promotionProgramDetailGet.productId));
						if (productSalePro.orderDetailDTO.price < promotionProgramDetailGet.saleAMT) {
							checkpro = false;
							// break;
						}
						if (n > (int) productSalePro.orderDetailDTO.price
								/ (int) promotionProgramDetailGet.saleAMT
								|| n < 0) {
							n = (int) productSalePro.orderDetailDTO.price
									/ (int) promotionProgramDetailGet.saleAMT;
						}
						// promotionProgramDetailGet.price =
						// productSalePro.orderDetailDTO.price;
						countSale++;
					}
				}

				// Khi ko luy ke - VietHQ noi thieu doan nay
				if (promotionProEntity.MULTIPLE == 0) {
					n = 1;
				}

				for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
						.values()) {
					promotionProgramDetailGet = listPromotionProgramDetail.get(indexTotal);
					Long key = Long.valueOf(listPromotionProgramDetail
							.get(indexTotal).productId);
					OrderDetailViewDTO productSalePro = sortListProductSale
							.get(key);

					if (productSalePro != null) {
						productSalePro.orderDetailDTO.price -= n
								* promotionProgramDetailGet.saleAMT;
					}
				}

				// List<PromotionProDetailDTO> listPromotionProgramDetail =
				// sortListPromotionProDetail.get(0);
				// promotionProgramDetailGet =
				// listPromotionProgramDetail.get(indexTotal);
				totalPro += (float) promotionProgramDetailGet.discAMT * n;
			} else {
				for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
						.values()) {
					promotionProgramDetailGet = listPromotionProgramDetail
							.get(0);

					Long key = Long
							.valueOf(listPromotionProgramDetail.get(0).productId);
					OrderDetailViewDTO productSalePro = sortListProductSale
							.get(key);

					if (productSalePro != null) {
						sortListProductSale.remove(Long
								.valueOf(promotionProgramDetailGet.productId));
					}
				}
			}

			// Nếu khuyến mãi không cho phép đệ quy tối ưu thì chỉ tính mức lớn
			// nhất
			if (promotionProEntity.recursive == 0)
				break;
		}

		// add kết quả trả về
		if (totalPro > 0) {
			OrderDetailViewDTO productSaleAdd = new OrderDetailViewDTO();
			SaleOrderDetailDTO productSaleDTO = new SaleOrderDetailDTO();

			productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
			productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
			productSaleDTO.programeType = 0;
			productSaleDTO.productId = 0;
			productSaleDTO.price = 0;
			productSaleDTO.discountAmount = totalPro;
			productSaleDTO.maxAmountFree = productSaleDTO.discountAmount;
			productSaleDTO.quantity = 0;
			productSaleDTO.isFreeItem = 1;

			productSaleAdd.type = OrderDetailViewDTO.FREE_PRICE;
			productSaleAdd.typeName = "KM tiền";
			productSaleAdd.productPromoId = 0;

			productSaleAdd.orderDetailDTO = productSaleDTO;
			listProductPromotionsale.add(productSaleAdd);
		}
	}

	/**
	 * 
	 * Tinh khuyen mai ZV18 (Bundle-Amt-FreeItem): Mua theo Bộ sản phẩm
	 * (nghĩa là phải đầy đủ sản phẩm, bắt buộc)- với số tiền xác
	 * định, thì sẽ được tặng 1 hoặc nhóm sản phẩm nào đó. Vd: Mua
	 * 500.000 sản phẩm A và 300.000 sản phẩm B, sẽ được tặng 10 sản
	 * phẩm (C hoặc D). Mua 500.000 sản phẩm A và 300.000 sản phẩm B, sẽ
	 * được tặng 10 sản phẩm C và 5 sản phẩm A.
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param listPromoProducts
	 * @param saleOrder
	 * @param orderDetail
	 * @param promotionProgram
	 * @param listPromotionDetail
	 * @return: void
	 * @throws:
	 */
	@SuppressWarnings("unused")
	private static void calZV18(
			PromotionProgrameDTO promotionProEntity,
			SortedMap<Long, List<PromotionProDetailDTO>> sortListPromotionProDetail,
			SortedMap<Long, OrderDetailViewDTO> sortListProductSale,
			List<OrderDetailViewDTO> listProductPromotionsale,
			Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		if (promotionProEntity.RELATION == PromotionProgrameDTO.RELATION_OR) {
			int n = -1;
			boolean checkpro = true;
//			int countDetail = sortListPromotionProDetail.size();
			int countSale = 0;
//			long totalPro = 0;

			PromotionProDetailDTO promotionProgramDetailGet = new PromotionProDetailDTO();

			// check theo muc xem KM o muc nao
			int indexTotal = 10000000;

			/*
			 * VietHQ Khuyến mãi lưu trong sortListPromotionProDetail có nhiều
			 * mức khác nhau, Công việc vòng lặp For đầu tiền trong Vòng White
			 * để tìm mức phù hợp nhất với sản phẩm bán. sau đó dùng
			 * Old_IndexTotal để lưu trạng thái đó lại, tránh lần sau chọn trùng
			 * lặp lại
			 */

			int old_IndexTotal = 10000000;

			for (OrderDetailViewDTO productSalePro : sortListProductSale
					.values()) {
				productSalePro.orderDetailDTO.price = productSalePro.orderDetailDTO.price
						* productSalePro.orderDetailDTO.quantity;
			}

			while (checkpro) {
				checkpro = true;
				keyList++;
				n = -1;

				for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
						.values()) {
					promotionProgramDetailGet = listPromotionProgramDetail
							.get(0);

					Long key = Long
							.valueOf(listPromotionProgramDetail.get(0).productId);
					OrderDetailViewDTO productSalePro = sortListProductSale
							.get(key);

					if (productSalePro != null) {
						// Hoi VietHQ
						// if (promotionProgramDetailGet.required == 1 &&
						// productSalePro.orderDetailDTO.quantity == 0) {
						// return;
						// }

						int index = -1;
						for (int j = 0, size = listPromotionProgramDetail
								.size(); j < size; j++) {
							if (productSalePro.orderDetailDTO.price >= listPromotionProgramDetail
									.get(j).saleAMT
									&& old_IndexTotal > j
									&& (old_IndexTotal == 10000000 || (old_IndexTotal < listPromotionProgramDetail
											.size() && listPromotionProgramDetail
											.get(j).saleAMT != listPromotionProgramDetail
											.get(old_IndexTotal).saleAMT))) {
								index = j;
							}
						}
						if (index == -1) {
							checkpro = false;
						}
						if (indexTotal != -1 && indexTotal > index) {
							indexTotal = index;
						}
					} else {
						checkpro = false;
					}

				}

				old_IndexTotal = indexTotal;

				// Tinh luy ke
				if (checkpro) {
					for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
							.values()) {
						promotionProgramDetailGet = listPromotionProgramDetail
								.get(indexTotal);

						Long key = Long.valueOf(listPromotionProgramDetail
								.get(indexTotal).productId);
						OrderDetailViewDTO productSalePro = sortListProductSale
								.get(key);

						if (productSalePro != null) {
							// sortListProductSale.remove(Long.valueOf(promotionProgramDetailGet.productId));
							if (productSalePro.orderDetailDTO.price < promotionProgramDetailGet.saleAMT) {
								checkpro = false;
								break;
							}
							if (n > (int) productSalePro.orderDetailDTO.price
									/ (int) promotionProgramDetailGet.saleAMT
									|| n < 0) {
								n = (int) productSalePro.orderDetailDTO.price
										/ (int) promotionProgramDetailGet.saleAMT;
							}
							// promotionProgramDetailGet.price =
							// productSalePro.orderDetailDTO.price;
							countSale++;
						}
					}

					// Khi ko luy ke - VietHQ noi thieu doan nay
					if (promotionProEntity.MULTIPLE == 0) {
						n = 1;
					}

					for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
							.values()) {
						promotionProgramDetailGet = listPromotionProgramDetail.get(indexTotal);
						Long key = Long.valueOf(listPromotionProgramDetail
								.get(indexTotal).productId);
						OrderDetailViewDTO productSalePro = sortListProductSale
								.get(key);

						if (productSalePro != null) {
							productSalePro.orderDetailDTO.price -= n
									* promotionProgramDetailGet.saleAMT;
						}
					}

					// List<PromotionProDetailDTO> listPromotionProgramDetail =
					// sortListPromotionProDetail.get(0);
					// promotionProgramDetailGet =
					// listPromotionProgramDetail.get(indexTotal);
					// totalPro += (float)promotionProgramDetailGet.discAMT * n;
				} else {
					for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
							.values()) {
						promotionProgramDetailGet = listPromotionProgramDetail
								.get(0);

						Long key = Long.valueOf(listPromotionProgramDetail
								.get(0).productId);
						OrderDetailViewDTO productSalePro = sortListProductSale
								.get(key);

						if (productSalePro != null) {
							sortListProductSale
									.remove(Long
											.valueOf(promotionProgramDetailGet.productId));
						}
					}
				}

				// Nếu khuyến mãi không cho phép đệ quy tối ưu thì chỉ tính mức
				// lớn nhất
				// if (promotionProEntity.recursive == 0)
				// break;

				if (checkpro) {
					List<PromotionProDetailDTO> listPromotionProgramDetail = sortListPromotionProDetail
							.get(sortListPromotionProDetail.firstKey());
					promotionProgramDetailGet = listPromotionProgramDetail
							.get(indexTotal);

					if (promotionProEntity.MULTIPLE == 0) {
						n = 1;
					}
					

					// list danh sach cac mat hang can chon
					List<OrderDetailViewDTO> listProductChange = new ArrayList<OrderDetailViewDTO>();
					for (int j = 0, size = listPromotionProgramDetail.size(); j < size; j++) {
						if (listPromotionProgramDetail.get(j).saleAMT == listPromotionProgramDetail
								.get(indexTotal).saleAMT) {
							OrderDetailViewDTO productSaleAdd = new OrderDetailViewDTO();
							SaleOrderDetailDTO productSaleDTO = new SaleOrderDetailDTO();
							productSaleAdd.orderDetailDTO = productSaleDTO;

							productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
							productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
							productSaleDTO.programeType = 0;
							productSaleDTO.productId = listPromotionProgramDetail
									.get(j).productId;
							productSaleDTO.price = 0;
							productSaleDTO.discountAmount = 0;// amountPromo = 0;
							productSaleDTO.quantity = listPromotionProgramDetail.get(j).freeQTY * n;// quantityPromo = (float)listPromotionProgramDetail[j].FREE_QTY * n;
							productSaleDTO.maxQuantityFree = productSaleDTO.quantity;
							productSaleDTO.isFreeItem = 1;

							productSaleAdd.productPromoId = listPromotionProgramDetail
									.get(j).freeProductId;
							productSaleAdd.changeProduct = 1;
							productSaleAdd.keyList = keyList;
							productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
							productSaleAdd.typeName = "KM hàng";

							listProductChange.add(productSaleAdd);
						}
					}

					sortListOutPut.put(keyList, listProductChange);
					
					//ds pham hien thi
					OrderDetailViewDTO productSaleAdd = new OrderDetailViewDTO();
					SaleOrderDetailDTO productSaleDTO = new SaleOrderDetailDTO();

					productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
					productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
					productSaleDTO.programeType = 0;
					productSaleDTO.productId = promotionProgramDetailGet.productId;
					productSaleDTO.price = 0;
					productSaleDTO.discountAmount = 0;
					productSaleDTO.quantity = promotionProgramDetailGet.freeQTY* n;
					productSaleDTO.maxQuantityFree = productSaleDTO.quantity;
					productSaleDTO.isFreeItem = 1;

					productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
					productSaleAdd.typeName = "KM hàng";
					productSaleAdd.productPromoId = promotionProgramDetailGet.freeProductId;
					if (listProductChange.size() > 1) {// nếu có nhiêu hơn 2
						productSaleAdd.keyList = keyList;
						productSaleAdd.changeProduct = 1;
					}

					productSaleAdd.orderDetailDTO = productSaleDTO;
					listProductPromotionsale.add(productSaleAdd);
				}
				
				// Nếu khuyến mãi không cho phép đệ quy tối ưu thì chỉ tính mức lớn nhất
                if (promotionProEntity.recursive == 0) break;
			}

		} else {
			int n = -1;
			boolean checkpro = true;
			// int countDetail = sortListPromotionProDetail.size();
			int countSale = 0;
//			float totalPro = 0;

			PromotionProDetailDTO promotionProgramDetailGet = new PromotionProDetailDTO();

			// check theo muc xem KM o muc nao
			int indexTotal = 10000000;

			/*
			 * VietHQ Khuyến mãi lưu trong sortListPromotionProDetail có nhiều
			 * mức khác nhau, Công việc vòng lặp For đầu tiền trong Vòng White
			 * để tìm mức phù hợp nhất với sản phẩm bán. sau đó dùng
			 * Old_IndexTotal để lưu trạng thái đó lại, tránh lần sau chọn trùng
			 * lặp lại
			 */

			int old_IndexTotal = 10000000;

			for (OrderDetailViewDTO productSalePro : sortListProductSale
					.values()) {
				productSalePro.orderDetailDTO.price = productSalePro.orderDetailDTO.price
						* productSalePro.orderDetailDTO.quantity;
			}

			while (checkpro) {
				checkpro = true;
				n = -1;

				for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
						.values()) {
					promotionProgramDetailGet = listPromotionProgramDetail
							.get(0);

					Long key = Long
							.valueOf(listPromotionProgramDetail.get(0).productId);
					OrderDetailViewDTO productSalePro = sortListProductSale
							.get(key);

					if (productSalePro != null) {
						// Hoi VietHQ
						// if (promotionProgramDetailGet.required == 1 &&
						// productSalePro.orderDetailDTO.quantity == 0) {
						// return;
						// }

						int index = -1;
						for (int j = 0, size = listPromotionProgramDetail
								.size(); j < size; j++) {
							if (productSalePro.orderDetailDTO.price >= listPromotionProgramDetail
									.get(j).saleAMT
									&& old_IndexTotal > j
									&& (old_IndexTotal == 10000000 || (old_IndexTotal < listPromotionProgramDetail
											.size() && listPromotionProgramDetail
											.get(j).saleAMT != listPromotionProgramDetail
											.get(old_IndexTotal).saleAMT))) {
								index = j;
							}
						}
						if (index == -1) {
							checkpro = false;
						}
						if (indexTotal != -1 && indexTotal > index) {
							indexTotal = index;
						}
					} else {
						checkpro = false;
					}
				}

				old_IndexTotal = indexTotal;

				// Tinh luy ke
				if (checkpro) {
					for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
							.values()) {
						promotionProgramDetailGet = listPromotionProgramDetail
								.get(indexTotal);

						Long key = Long.valueOf(listPromotionProgramDetail
								.get(indexTotal).productId);
						OrderDetailViewDTO productSalePro = sortListProductSale
								.get(key);

						if (productSalePro != null) {
							// sortListProductSale.remove(Long.valueOf(promotionProgramDetailGet.productId));
							if (productSalePro.orderDetailDTO.price < promotionProgramDetailGet.saleAMT) {
								checkpro = false;
								break;
							}
							if (n > (int) productSalePro.orderDetailDTO.price
									/ (int) promotionProgramDetailGet.saleAMT
									|| n < 0) {
								n = (int) productSalePro.orderDetailDTO.price
										/ (int) promotionProgramDetailGet.saleAMT;
							}
							// promotionProgramDetailGet.price =
							// productSalePro.orderDetailDTO.price;
							countSale++;
						}
					}

					// Khi ko luy ke - VietHQ noi thieu doan nay
					if (promotionProEntity.MULTIPLE == 0) {
						n = 1;
					}

					for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
							.values()) {
						promotionProgramDetailGet = listPromotionProgramDetail.get(indexTotal);
						Long key = Long.valueOf(listPromotionProgramDetail
								.get(indexTotal).productId);
						OrderDetailViewDTO productSalePro = sortListProductSale
								.get(key);

						if (productSalePro != null) {
							productSalePro.orderDetailDTO.price -= n
									* promotionProgramDetailGet.saleAMT;
						}
					}

					// List<PromotionProDetailDTO> listPromotionProgramDetail =
					// sortListPromotionProDetail.get(0);
					// promotionProgramDetailGet =
					// listPromotionProgramDetail.get(indexTotal);
					// totalPro += (float)promotionProgramDetailGet.discAMT * n;
				} else {
					for (List<PromotionProDetailDTO> listPromotionProgramDetail : sortListPromotionProDetail
							.values()) {
						promotionProgramDetailGet = listPromotionProgramDetail
								.get(0);

						Long key = Long.valueOf(listPromotionProgramDetail
								.get(0).productId);
						OrderDetailViewDTO productSalePro = sortListProductSale
								.get(key);

						if (productSalePro != null) {
							sortListProductSale
									.remove(Long
											.valueOf(promotionProgramDetailGet.productId));
						}
					}
				}

				// Nếu khuyến mãi không cho phép đệ quy tối ưu thì chỉ tính mức
				// lớn nhất
				// if (promotionProEntity.recursive == 0)
				// break;

				if (checkpro) {
					List<PromotionProDetailDTO> listPromotionProgramDetail = sortListPromotionProDetail
							.get(sortListPromotionProDetail.firstKey());
					promotionProgramDetailGet = listPromotionProgramDetail
							.get(0);

					if (promotionProEntity.MULTIPLE == 0) {
						n = 1;
					}

					for (int j = 0, size = listPromotionProgramDetail.size(); j < size; j++) {
						if (listPromotionProgramDetail.get(j).saleAMT == listPromotionProgramDetail
								.get(indexTotal).saleAMT) {
							OrderDetailViewDTO productSaleAdd = new OrderDetailViewDTO();
							SaleOrderDetailDTO productSaleDTO = new SaleOrderDetailDTO();

							productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
							productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
							productSaleDTO.programeType = 0;
							productSaleDTO.productId = listPromotionProgramDetail.get(j).productId;
							productSaleDTO.price = 0;
							productSaleDTO.discountAmount = 0;
							productSaleDTO.quantity = listPromotionProgramDetail.get(j).freeQTY * n;
							productSaleDTO.maxQuantityFree = productSaleDTO.quantity;
							productSaleDTO.isFreeItem = 1;

							productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
							productSaleAdd.typeName = "KM hàng";
							productSaleAdd.productPromoId = listPromotionProgramDetail.get(j).freeProductId;

							productSaleAdd.orderDetailDTO = productSaleDTO;
							listProductPromotionsale.add(productSaleAdd);
						}
					}
				}
				
				// Nếu khuyến mãi không cho phép đệ quy tối ưu thì chỉ tính
				// mức lớn nhất
				if (promotionProEntity.recursive == 0)
					break;
			}
		}
	}

	/**
	 * Tinh khuyen mai ZV19 - Docmt-Amt-Percent
	 * @author: TruongHN
	 * @param listPromoProducts
	 * @param saleOrder
	 * @param orderDetail
	 * @param promotionProgram
	 * @param listPromotionDetail
	 * @return: OrderDetailViewDTO - CTKM cua don hang
	 * @throws:
	 */
	private static OrderDetailViewDTO calZV19(long amtOrder,
			PromotionProgrameDTO promotionProEntity,
			List<PromotionProDetailDTO> listPromotionProgramDetail,
			List<OrderDetailViewDTO> listProductPromotionsale,
			Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		// Docmt-Amt-Percent
		// Tinh theo gia tri don hang, neu dat tong tien xac dinh se duoc giam % tren don hang
		// Mua don hang gia tri 500 thi se duoc giam 1% tong tien hoa don
		OrderDetailViewDTO detailView = null;
		if (amtOrder > 0) {
			PromotionProDetailDTO selectedPromoDetail;
			for (int i = listPromotionProgramDetail.size() - 1; i >= 0; i--) {
				selectedPromoDetail = listPromotionProgramDetail.get(i);
				if (selectedPromoDetail.saleAMT <= amtOrder) {
					// tim duoc khuyen mai phu hop
					detailView = new OrderDetailViewDTO();
					SaleOrderDetailDTO selectedOrderDetail = new SaleOrderDetailDTO();
					selectedOrderDetail.isFreeItem = 1; // mat hang khuyen mai
					detailView.promotionType = OrderDetailViewDTO.PROMOTION_FOR_ORDER;
					
					long totalAmount = amtOrder;
					
					// so tien khuyen mai
					if (totalAmount > Integer.MAX_VALUE) {
						selectedOrderDetail.discountAmount = (long)(totalAmount / 1000 * (int)(selectedPromoDetail.discPer * 10));
					} else {
						selectedOrderDetail.discountAmount = (long)(totalAmount * selectedPromoDetail.discPer / 100);
					}
					selectedOrderDetail.maxAmountFree = selectedOrderDetail.discountAmount;
					selectedOrderDetail.discountPercentage = selectedPromoDetail.discPer;
										
					// so tien khuyen mai
					selectedOrderDetail.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
					selectedOrderDetail.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
					selectedOrderDetail.programeType = 0;
					selectedOrderDetail.programeTypeCode = ZV19;
					detailView.productCode = "";
					detailView.productName = "";
					detailView.orderDetailDTO = selectedOrderDetail;
					detailView.type = OrderDetailViewDTO.FREE_PERCENT;
					detailView.typeName = "KM %";
					detailView.promotionDescription = selectedOrderDetail.discountPercentage + "%";

//					listProductPromotionsale.add(detailView);
					break;
				}
			}
		}
		return detailView;
	}
	
	
	/**
	 * Tinh khuyen mai ZV20 - Docmt-Amt-Amount
	 * @author: TruongHN
	 * @param listPromoProducts
	 * @param saleOrder
	 * @param orderDetail
	 * @param promotionProgram
	 * @param listPromotionDetail
	 * @return: void
	 * @throws:
	 */
	private static OrderDetailViewDTO calZV20(long amtOrder,
			PromotionProgrameDTO promotionProEntity,
			List<PromotionProDetailDTO> listPromotionProgramDetail,
			List<OrderDetailViewDTO> listProductPromotionsale,
			Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		// Docmt-Amt-Amount
		// Tinh theo gia tri don hang, neu dat tong tien xac dinh se duoc giam so tien xac dinh tren don hang
		// Mua don hang gia tri 500D thi se duoc giam 10D
		
		OrderDetailViewDTO detailView = null;
		
		if (amtOrder > 0) {
			PromotionProDetailDTO selectedPromoDetail = null;
			int multiple = 1; // cap so nhan
			long salePrice = amtOrder;
			long oldSale = 0;
			for (int i = listPromotionProgramDetail.size() - 1; i >= 0; i--) {
				selectedPromoDetail = listPromotionProgramDetail.get(i);
				if (selectedPromoDetail.saleAMT <= salePrice
						&& oldSale != selectedPromoDetail.saleAMT) {
					oldSale = (long) selectedPromoDetail.saleAMT;
					// tim duoc khuyen mai phu hop nhat
					if (promotionProEntity.MULTIPLE == 1) {
						// CTKM co cap so nhan
						multiple = (int) (salePrice / selectedPromoDetail.saleAMT);
						salePrice = (long) (salePrice % selectedPromoDetail.saleAMT);
					} else {
						multiple = 1;
						salePrice = salePrice - (long) selectedPromoDetail.saleAMT;
					}
				} else {
					continue;
				}
				// them san pham vao ds san pham khuyen mai
				if (detailView == null) {
					detailView = new OrderDetailViewDTO();
					detailView.promotionType = OrderDetailViewDTO.PROMOTION_FOR_ORDER;
					SaleOrderDetailDTO selectedOrderDetail = new SaleOrderDetailDTO();
//					selectedOrderDetail.productId = selectedPromoDetail.productId;
					selectedOrderDetail.isFreeItem = 1; // mat hang khuyen mai
					// so tien khuyen mai
					selectedOrderDetail.discountAmount = (long)(multiple * selectedPromoDetail.discAMT);
					selectedOrderDetail.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
					selectedOrderDetail.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
					selectedOrderDetail.programeType = 0;
					selectedOrderDetail.programeTypeCode = ZV20;
					detailView.productCode = "";
					detailView.productName = "";
					detailView.orderDetailDTO = selectedOrderDetail;
					detailView.type = OrderDetailViewDTO.FREE_PRICE;
					detailView.typeName = "KM tiền";
				} else {
					detailView.orderDetailDTO.discountAmount += multiple * selectedPromoDetail.discAMT;
				}
				
				detailView.orderDetailDTO.maxAmountFree = detailView.orderDetailDTO.discountAmount;

				// kiem tra neu khong cho phep de quy toi uu, thi chi tinh
				// muc cao nhat
				if (promotionProEntity.recursive == 0) {
					break;
				}
			}
			if (detailView != null) {
				// Hien thi promotion cho KM don hang
				detailView.promotionDescription = StringUtil.parseAmountMoney(String.valueOf(detailView.orderDetailDTO.discountAmount));
			}
		}
		return detailView;
	}
	
	/**
	* Tinh KM 21 - Document-Amt-freeitem
	* @author: TruongHN
	* @param promotionProEntity
	* @param sortListPromotionProDetail
	* @param sortListProductSale
	* @param listProductPromotionsale
	* @param keyList
	* @param sortListOutPut
	* @return: OrderDetailViewDTO
	* @throws:
	 */
	private static OrderDetailViewDTO calZV21(long amtOrder,
			PromotionProgrameDTO promotionProEntity,
			List<PromotionProDetailDTO> listPromotionProgramDetail,
			List<OrderDetailViewDTO> listProductPromotionsale,
			Long keyList,
			SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut) {
		// Docmt-Amt-Amount
		// Tinh theo gia tri don hang, neu dat tong tien xac dinh se duoc tang 1 hoac 1 nhom san pham nao do
		// Mua don hang gia tri 500D thi se duoc tang 1 spA hoac 1 spB....
		StringBuffer promotionDescription = new StringBuffer();
		ArrayList<OrderDetailViewDTO> listPromotionForPromo21 = new ArrayList<OrderDetailViewDTO>();
		if (promotionProEntity.RELATION == PromotionProgrameDTO.RELATION_OR) {
			// hoac
			if (amtOrder > 0) {
				// remove de khong tinh lai san pham nua
//				sortListProductSale.remove(key);
				PromotionProDetailDTO selectedPromoDetail = null;
				int multiple = 1; // cap so nhan
				long salePrice = amtOrder;
				float oldPrice = 0;

				for (int i = listPromotionProgramDetail.size() - 1; i >= 0; i--) {
					multiple = 1;
					selectedPromoDetail = listPromotionProgramDetail.get(i);
					keyList++;
					if (selectedPromoDetail.saleAMT <= salePrice
							&& selectedPromoDetail.saleAMT != oldPrice) {
						oldPrice = selectedPromoDetail.saleAMT;
						// tim duoc khuyen mai phu hop nhat
						if (promotionProEntity.MULTIPLE == 1) {
							// CTKM co cap so nhan
							
							multiple = (int) (salePrice / selectedPromoDetail.saleAMT);
							salePrice = (long) (salePrice % selectedPromoDetail.saleAMT);
							
						} else {
							multiple = 1;
							salePrice = salePrice - (long) selectedPromoDetail.saleAMT;
						}
					} else {
						continue;
					}
					// them ds mat hang lua chon
					OrderDetailViewDTO productSaleAdd;
					SaleOrderDetailDTO productSaleDTO;
					List<OrderDetailViewDTO> listProductChange = new ArrayList<OrderDetailViewDTO>();
					for (int j = 0, size = listPromotionProgramDetail.size(); j < size; j++) {
						if (selectedPromoDetail.saleAMT == listPromotionProgramDetail.get(j).saleAMT) {
							productSaleAdd = new OrderDetailViewDTO();
							productSaleAdd.promotionType = OrderDetailViewDTO.PROMOTION_FOR_ORDER_TYPE_PRODUCT;
							productSaleDTO = new SaleOrderDetailDTO();
							productSaleAdd.orderDetailDTO = productSaleDTO;

							productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
							productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
							productSaleDTO.programeType = 0;
							productSaleDTO.programeTypeCode = ZV21;
							productSaleDTO.productId = listPromotionProgramDetail.get(j).productId;
							productSaleDTO.price = 0;
							productSaleDTO.discountAmount = 0;// amountPromo = 0
							productSaleDTO.isFreeItem = 1; // mat hang khuyen mai
							productSaleDTO.quantity = listPromotionProgramDetail.get(j).freeQTY * multiple;// quantityPromo = (float)listPromotionProgramDetail[j].FREE_QTY * n;
							productSaleDTO.maxQuantityFree = productSaleDTO.quantity;

							productSaleAdd.productPromoId = listPromotionProgramDetail.get(j).freeProductId;
							productSaleAdd.changeProduct = 1;
							productSaleAdd.keyList = keyList;
							productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
							productSaleAdd.typeName = "KM hàng";

							listProductChange.add(productSaleAdd);
							
							// cap nhat thong tin hien thi KM
							if (promotionDescription.length() > 0){
								promotionDescription.append(Constants.STR_OR);
							}
							promotionDescription.append(productSaleAdd.orderDetailDTO.quantity);
							promotionDescription.append("(");
							promotionDescription.append(listPromotionProgramDetail.get(j).productCode);
							promotionDescription.append(")");
						}
					}

					sortListOutPut.put(keyList, listProductChange);

					// tao mat hang ban dinh kem sp khuyen mai
					productSaleAdd = new OrderDetailViewDTO();
					productSaleAdd.promotionType = OrderDetailViewDTO.PROMOTION_FOR_ORDER_TYPE_PRODUCT;
					productSaleDTO = new SaleOrderDetailDTO();
					productSaleAdd.orderDetailDTO = productSaleDTO;

					productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
					productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
					productSaleDTO.programeType = 0;
					productSaleDTO.programeTypeCode = ZV21;
					productSaleDTO.productId = selectedPromoDetail.productId;
					productSaleAdd.productPromoId = selectedPromoDetail.freeProductId;
					productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
					productSaleAdd.typeName = "KM hàng";
					productSaleDTO.isFreeItem = 1; // mat hang khuyen mai
					productSaleDTO.price = 0;
					productSaleDTO.discountAmount = 0;// amountPromo = 0;

					if (listProductChange.size() > 1){// nếu có nhiêu hơn 2 mặt hàng khuyến mãi thì gán KeyList để làm Key đối soát như ở trên
						productSaleAdd.changeProduct = 1;
						productSaleAdd.keyList = keyList;
					}
					
					productSaleAdd.orderDetailDTO.quantity = multiple * selectedPromoDetail.freeQTY;
					productSaleAdd.orderDetailDTO.maxQuantityFree = productSaleAdd.orderDetailDTO.quantity;
					
					listPromotionForPromo21.add(productSaleAdd);
					
					// kiem tra neu khong cho phep de quy toi uu, thi chi tinh muc cao nhat
					if (promotionProEntity.recursive == 0) {
						break;
					}
				}
			}
		} else {
			// va
			if (amtOrder > 0) {
				PromotionProDetailDTO selectedPromoDetail = null;
				int multiple = 1; // cap so nhan
				long salePrice = amtOrder;
				float oldPrice = 0;

				for (int i = listPromotionProgramDetail.size() - 1; i >= 0; i--) {
					multiple = 1;
					selectedPromoDetail = listPromotionProgramDetail.get(i);
					if (selectedPromoDetail.saleAMT <= salePrice
							&& selectedPromoDetail.saleAMT != oldPrice) {
						oldPrice = selectedPromoDetail.saleAMT;
						// tim duoc khuyen mai phu hop nhat
						if (promotionProEntity.MULTIPLE == 1) {
							// CTKM co cap so nhan
							multiple = (int) (salePrice / selectedPromoDetail.saleAMT);
							salePrice = (long) (salePrice % selectedPromoDetail.saleAMT);
						} else {
							multiple = 1;
							salePrice =  salePrice - (long)selectedPromoDetail.saleAMT;
						}
					} else {
						continue;
					}
					// them ds mat hang lua chon
					OrderDetailViewDTO productSaleAdd;
					SaleOrderDetailDTO productSaleDTO;
					for (int j = 0, size = listPromotionProgramDetail.size(); j < size; j++) {
						if (selectedPromoDetail.saleAMT == listPromotionProgramDetail.get(j).saleAMT) {
							productSaleAdd = new OrderDetailViewDTO();
							productSaleAdd.promotionType = OrderDetailViewDTO.PROMOTION_FOR_ORDER_TYPE_PRODUCT;
							productSaleDTO = new SaleOrderDetailDTO();
							productSaleAdd.orderDetailDTO = productSaleDTO;

							productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
							productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
							productSaleDTO.programeType = 0;
							productSaleDTO.programeTypeCode = ZV21;
							productSaleDTO.isFreeItem = 1; // mat hang khuyen mai
							productSaleDTO.productId = listPromotionProgramDetail
									.get(j).productId;
							productSaleDTO.price = 0;
							productSaleDTO.discountAmount = 0;// amountPromo = 0;
							productSaleDTO.quantity = listPromotionProgramDetail.get(j).freeQTY * multiple;// quantityPromo = (float)listPromotionProgramDetail[j].FREE_QTY * n;
							productSaleDTO.maxQuantityFree = productSaleDTO.quantity;
							
							productSaleAdd.productPromoId = listPromotionProgramDetail.get(j).freeProductId;
							productSaleAdd.type = OrderDetailViewDTO.FREE_PRODUCT;
							productSaleAdd.typeName = "KM hàng";

							listPromotionForPromo21.add(productSaleAdd);
							
							// cap nhat thong tin hien thi KM
							if (promotionDescription.length() > 0){
								promotionDescription.append(Constants.STR_AND);
							}
							promotionDescription.append(productSaleAdd.orderDetailDTO.quantity);
							promotionDescription.append("(");
							promotionDescription.append(listPromotionProgramDetail.get(j).productCode);
							promotionDescription.append(")");
						}
					}

						// kiem tra neu khong cho phep de quy toi uu, thi chi tinh muc cao nhat
					if (promotionProEntity.recursive == 0) {
						break;
					}
				}
			}
		}
		
		// neu co nhan KM 21
		OrderDetailViewDTO detailView  = null;
		if (listPromotionForPromo21.size() > 0){
			SaleOrderDetailDTO productSaleDTO;
			detailView = new OrderDetailViewDTO();
			detailView.promotionType = OrderDetailViewDTO.PROMOTION_FOR_ZV21;
			productSaleDTO = new SaleOrderDetailDTO();
			detailView.orderDetailDTO = productSaleDTO;

			productSaleDTO.programeCode = promotionProEntity.PROMOTION_PROGRAM_CODE;
			productSaleDTO.programeName = promotionProEntity.PROMOTION_PROGRAM_NAME;
			productSaleDTO.programeType = 0;
			productSaleDTO.programeTypeCode = ZV21;
			productSaleDTO.isFreeItem = 1; // mat hang khuyen mai
//			productSaleDTO.productId = listPromotionProgramDetail.get(j).productId;
			productSaleDTO.price = 0;
			productSaleDTO.discountAmount = 0;// amountPromo = 0;
//			productSaleDTO.quantity = 0
			productSaleDTO.maxQuantityFree = productSaleDTO.quantity;
			
//			productSaleAdd.productPromoId = listPromotionProgramDetail.get(j).freeProductId;
			detailView.type = OrderDetailViewDTO.FREE_PRODUCT;
			detailView.typeName = "KM hàng";
			detailView.listPromotionForPromo21 = listPromotionForPromo21;
			
			if (promotionDescription.length() > 0){
				detailView.promotionDescription = String.valueOf(promotionDescription.toString());
			}
//			listProductPromotionsale.add(detailView);
		}
		return detailView;
	 }
}
