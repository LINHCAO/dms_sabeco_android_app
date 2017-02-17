/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.order;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.constants.TableDefineContanst;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.ApParamDTO;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.dto.db.SaleOrderDetailDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.FindProductSaleOrderDetailViewDTO;
import com.viettel.dms.dto.view.ListFindProductSaleOrderDetailViewDTO;
import com.viettel.dms.dto.view.OrderDetailViewDTO;
import com.viettel.dms.dto.view.OrderViewDTO;
import com.viettel.dms.dto.view.RemainProductViewDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.locating.PositionManager;
import com.viettel.dms.sqllite.db.ABSTRACT_TABLE;
import com.viettel.dms.sqllite.db.SALE_ORDER_TABLE;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VinamilkHeaderTable;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.SalesPersonActivity;
import com.viettel.dms.view.sale.customer.CustomerListView;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * Man hinh dat hang
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class OrderView extends BaseFragment implements OnClickListener, OnEventControlListener, OnItemSelectedListener,
		VinamilkTableListener {
	// bien phan biet tao don hang presale or vansale
	public static final int CREATE_ORDER_FOR_PRESALE = 0;
	public static final int CREATE_ORDER_FOR_VANSALE = 1;
	// gia tri mac dinh orderType khi tao moi
	public static final String TAG = OrderView.class.getName();
	
	// xem chi tiet san pham
	public static final int ACTION_VIEW_PRODUCT = 0;
	// xem chi tiet khuyen mai
	public static final int ACTION_VIEW_PROMOTION = 1;
	// xoa mat hang ra khoi ds
	public static final int ACTION_DELETE = 2;
	// thay doi thong tin dat hang
	public static final int ACTION_CHANGE_REAL_ORDER = 3;
	// thay doi khuyen mai
	public static final int ACTION_CHANGE_PROMOTION = 4;
	// xoa sp khuyen mai
	public static final int ACTION_DELETE_PROMOTION = 5;
	// dong y luu dong hang
	public static final int ACTION_SAVE_ORDER_OK = 6;
	// tu choi luu don hang
	public static final int ACTION_CANCEL_SAVE_ORDER = 7;
	// cancel back default
	public static final int ACTION_CANCEL_BACK_DEFAULT = 8;
	// dong y back
//	public static final int ACTION_AGRRE_BACK = 9;
//	// tu choi back
//	public static final int ACTION_CANCEL_BACK = 10;
	// dong y ket thuc vieng tham
	private static final int ACTION_END_VISIT_OK = 11;
	// tu choi ket thuc ghe tham
	private static final int ACTION_END_VISIT_CANCEL = 12;
	// thay doi so luong dat khuyen mai
	public static final int ACTION_CHANGE_REAL_PROMOTION = 13;
	// Dong y doi mode luu don hang
	public static final int ACTION_CHANGE_MODE_SAVE_ORDER_OK = 14;
	// Cancel doi mode luu don hang
	public static final int ACTION_CANCEL_CHANGE_MODE_SAVE_ORDER = 15;
	// action refresh caculator promotion for promotion order
	public static final int ACTION_CHANGE_PROMOTION_FOR_PROMOTIOIN_ORDER = 16;
	// thay doi  tien KM cua don hang
	public static final int ACTION_CHANGE_REAL_MONEY_PROMOTION = 17;
	// thay doi so luong KM cua don hang
	public static final int ACTION_CHANGE_REAL_PROMOTION_ORDER = 18;
	// thay doi so tien
	public static final int ACTION_CHANGE_REAL_PRICE = 19;

	// bien de kiem tra la don hang moi hay don hay chinh sua
	private boolean isEditOrder = false;
	// thong tin khach hang
	private HeaderOrderInfo infoOrder;
	// button them hang
	private Button btAddBill;
	// button tinh khuyen mai
	private Button btnCalPromotion;
	// header danh sach mat hang ban
	private VinamilkHeaderTable headerProducts;
	// table mat hang ban
	private TableLayout tbProducts;
	// linearlayout mat hang khuyen mai
	private LinearLayout llPromotionProducts;
	// header ds mat hang khuyen mai c
	private VinamilkHeaderTable headerPromotionProducts;
	// table mat hang khuyen mai
	private TableLayout tbPromotionProducts;
	// mat hang khuyen mai
	private TextView tvTextPromotionProduct;
	// text mat hang ban
	private TextView tvTextTitleListProduct;
	// text tong tien
	private TextView tvTotalAmount;
	// luu tren local
	// private Button btSave;
	// luu va chuyen xuong server
	private Button btSaveAndSend;
	// ds muu do uu tien
	private Spinner spinnerPriority;
	// ngay chuyen
	private TextView tvDeliveryDate;// etDate
	// thoi gian chuyen
	private TextView tvDeliveryTime;
	private OrderViewDTO orderDTO = new OrderViewDTO();
	// row tong cua ds mat hang ban
	private OrderProductRow productTotalRow;
	// row tong cua ds mat khuyen mai
	//private OrderPromotionRow promotionTotalRow;

	private AlertDialog alertRemindDialog = null;
	private TextView tvTextTime;
	// dung de hien thi import code khi sua don hang
	// private TextView tvImportCode;
	boolean firstTimeGetOrder;

	// toa do cua nvbh hien tai
	private double lat = -1;
	private double lng = -1;
	CountDownTimer timer = null;
	// bien dung de kiem tra neu bi duplicate request
	private boolean isRequesting = false;
	// Kiem tra don hang import thanh cong thi hien thi mau binh thuong
	private boolean isCheckStockTotal = true;

	// cac control dung cho vansale
	// vung thong tin dung cho vansale
	private RelativeLayout rlOrderVansale;
	// check box dat hang
	private CheckBox cbOrder;
	// text tong tien valsale
	private TextView tvTotalAmountVansales;
	// button thanh toan
	private Button btPayment;

	// thong tin dat hang cho presale
	private LinearLayout llOrderPresale;

	// dialog product detail view
	private AlertDialog alertSelectPromotionProduct;
	// table vinamilk list product change for product have promotion (just
	// display in popup)
	private VinamilkTableView tbProductPromotionList;
	// promotion programe code
	private TextView tvCTKMCode;
	// promotion programe name
	private TextView tvCTKMName;
	// list product to change
	private ArrayList<OrderDetailViewDTO> listProductChange = new ArrayList<OrderDetailViewDTO>();
	// product selected
	private OrderDetailViewDTO productSelected = new OrderDetailViewDTO();
	// button close
	private Button btClosePopupPrograme;
	// button close popup select promotion for promotion type order
	private Button btClosePopupPromotion;

	// bien dung de phan biet tao don hang presale hay vansale
	private int orderType;

	// linearlayout llNewPromotion
	private LinearLayout llPromotionForOrder;
	// TextView tvSTTNewPromotion
	private TextView tvSTTPromotionForOrder;
	// TableLayout tbListromotionForOrder
	private TableLayout tbListPromotionForOrder;
	// dialog list promotion for promotion type order
	AlertDialog alertListPromotion;
	// refresh update list promotion for promotion type order
	boolean isRefresh = true;
	// table list promotion for order
	private VinamilkTableView tbPromotionListView;
	
	//Vi tri cua KM don hang
	private int indexPromotionOrder;

	public static OrderView newInstance(Bundle viewData) {
		OrderView f = new OrderView();
		f.setArguments(viewData);
		f.VIEW_NAME = StringUtil.getString(R.string.TEXT_BOOK_ORDER);
		return f;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRetainInstance(true);
		Bundle bundle = getArguments();
		if (bundle != null) {
			orderDTO.orderInfo.saleOrderId = Long.parseLong(bundle.getString(IntentConstants.INTENT_ORDER_ID));
			orderDTO.orderInfo.approved=bundle.getInt(IntentConstants.INTENT_FIND_ORDER_BAPPROVED);
			if (orderDTO.orderInfo.saleOrderId > 0) {
				// sua don hang
//				if(orderDTO.orderInfo.approved!=2){
//					orderDTO.isEditOrder = true;
//				}else{
//					orderDTO.isEditOrder = false;
//				}
				orderDTO.isEditOrder = true;
			} else {
				// tao moi don hang
				orderDTO.isEditOrder = false;
				// lay thong tin don hang goi y (neu co)
				ArrayList<RemainProductViewDTO> listSuggest = (ArrayList<RemainProductViewDTO>) bundle
						.getSerializable(IntentConstants.INTENT_SUGGEST_ORDER_LIST);
				if (listSuggest != null) {
					initSuggestList(listSuggest);
				}
				orderDTO.orderInfo.isVisitPlan = Integer.parseInt(bundle.getString(IntentConstants.INTENT_IS_OR)) == 0 ? 1
						: 0;
			}
			orderDTO.beginTimeVisit = DateUtils.now();
			orderDTO.customer.setCustomerId(Long.parseLong(bundle.getString(IntentConstants.INTENT_CUSTOMER_ID)));
			orderDTO.customer.setCustomerName(bundle.getString(IntentConstants.INTENT_CUSTOMER_NAME));
			orderDTO.customer.setStreet(bundle.getString(IntentConstants.INTENT_CUSTOMER_ADDRESS));
			orderDTO.customer.setCustomerTypeId(bundle.getInt(IntentConstants.INTENT_CUSTOMER_TYPE_ID));
			String customerCode = bundle.getString(IntentConstants.INTENT_CUSTOMER_CODE);
			if (customerCode != null) {
				if (customerCode.length() >= 3) {
					customerCode = customerCode.substring(0, 3);
				}

				orderDTO.customer.setCustomerCode(customerCode);
			}

			orderDTO.isBackToRemainOrder = bundle.getBoolean(IntentConstants.INTENT_HAS_REMAIN_PRODUCT, false);
			orderDTO.orderInfo.customerId = orderDTO.customer.customerId;
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.layout_order_view, null);
		View view = super.onCreateView(inflater, v, savedInstanceState);
		lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
		lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();

		initView(view);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_ORDER));
		enableMenuBar(this);

		if (orderDTO.isFirstInit) {
			if (orderDTO.isEditOrder) {
				isEditOrder = true;
				this.getOrderForEdit("" + orderDTO.orderInfo.saleOrderId);
			} else {
				initData();
				// hien thi ds mat hang ban
				showInfoOnView();
			}
			// lay ds muc do uu tien
			getCommonData("ORDER_PIRITY", orderDTO.orderInfo.customerId);
		}

		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (orderDTO.isFirstInit) {
		} else {
			// layout ds sp khuyen mai
			layoutPromotionProduct(orderDTO.listPromotionOrders);

			// layout ds sp ban
			reLayoutBuyProducts();

			// cap nhat thoi gian chuyen
			tvDeliveryDate.setText(orderDTO.deliveryDate);
			tvDeliveryTime.setText(orderDTO.deliveryTime);
			orderDTO.isFirstInit = true;

			// disable button save neu nhu sua don hang
			checkEnableButton();

			// layout combobox
			displayListPriority();
		}
	}

	/**
	 * Khoi tao ds goi y
	 * 
	 * @author: TruongHN
	 * @param listSuggest
	 * @return: void
	 * @throws:
	 */
	private void initSuggestList(ArrayList<RemainProductViewDTO> listDTO) {
		if (listDTO != null) {
			for (int i = 0, size = listDTO.size(); i < size; i++) {
				RemainProductViewDTO dto = listDTO.get(i);
				SaleOrderDetailDTO saleOrderDTO = new SaleOrderDetailDTO();
				OrderDetailViewDTO detailViewDTO = new OrderDetailViewDTO();

				saleOrderDTO.productId = Integer.valueOf(dto.productId);
				saleOrderDTO.price = dto.price;
				detailViewDTO.suggestedPrice = dto.suggestedPrice;
				saleOrderDTO.priceId = dto.priceId;
				saleOrderDTO.synState = ABSTRACT_TABLE.CREATED_STATUS;
				saleOrderDTO.updateUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
				saleOrderDTO.isFreeItem = 0;
				saleOrderDTO.programeCode = dto.promotionProgramCode;
				detailViewDTO.convfact = dto.convfact;
				detailViewDTO.uom2 = dto.uom;
//				detailViewDTO.quantity = dto.HINT_NUMBER;
//				saleOrderDTO.quantity = GlobalUtil.calRealOrder(detailViewDTO.quantity, detailViewDTO.convfact);
				saleOrderDTO.priceNotVat = dto.priceNotVat;
				saleOrderDTO.totalWeight = dto.grossWeight * saleOrderDTO.quantity;
				saleOrderDTO.vat = dto.vat;
//				if (dto.sign == 4 || dto.sign == 3 || dto.sign == 6) {
//					detailViewDTO.isFocus = 1;
//				}
				// detailViewDTO.isFocus = dto.getIS_FOCUS();
				detailViewDTO.productCode = dto.productCode;
				detailViewDTO.productName = dto.productName;
				detailViewDTO.stock = dto.quantityRemain;
				detailViewDTO.grossWeight = dto.grossWeight;
				detailViewDTO.orderDetailDTO = saleOrderDTO;
				if(dto.quantityRemain >0){
					detailViewDTO.remaindStockFormat = StringUtil.parseAmountMoney(String.valueOf(dto.quantityRemain));
				}

				orderDTO.listBuyOrders.add(detailViewDTO);
			}
		}
	}

	/**
	 * Khoi tao thong tin man hinh
	 * 
	 * @author: TruongHN
	 * @param v
	 * @return: void
	 * @throws:
	 */
	private void initView(View v) {
		// thong tin khach hang
		infoOrder = (HeaderOrderInfo) v.findViewById(R.id.headerOrderInfo);
		btAddBill = (Button) v.findViewById(R.id.btAddBill);
		btAddBill.setOnClickListener(this);
		btnCalPromotion = (Button) v.findViewById(R.id.btnCalPromotion);
		btnCalPromotion.setOnClickListener(this);

		// mat hang ban
		tvTextTitleListProduct = (TextView) v.findViewById(R.id.tvTextTitleListProduct);
		headerProducts = (VinamilkHeaderTable) v.findViewById(R.id.headerProducts);
		tbProducts = (TableLayout) v.findViewById(R.id.tbProducts);

		// Mat hang khuyen mai
		tvTextPromotionProduct = (TextView) v.findViewById(R.id.tvTextPromotionProduct);
		llPromotionProducts = (LinearLayout) v.findViewById(R.id.llPromotionProducts);
		headerPromotionProducts = (VinamilkHeaderTable) v.findViewById(R.id.headerPromotionProducts);
		tbPromotionProducts = (TableLayout) v.findViewById(R.id.tbPromotionProducts);

		// tong tien
		tvTotalAmount = (TextView) v.findViewById(R.id.tvTotalAmount);
		// btSave = (Button) v.findViewById(R.id.btSave);
		// btSave.setOnClickListener(this);

		btSaveAndSend = (Button) v.findViewById(R.id.btSaveAndSend);
		btSaveAndSend.setOnClickListener(this);
		spinnerPriority = (Spinner) v.findViewById(R.id.spinnerPriority);
		spinnerPriority.setOnItemSelectedListener(this);
		tvTextTime = (TextView) v.findViewById(R.id.tvTextTime);
		tvDeliveryDate = (TextView) v.findViewById(R.id.tvDeliveryDate);
		tvDeliveryDate.setOnClickListener(this);
		tvDeliveryTime = (TextView) v.findViewById(R.id.tvDeliveryTime);
		tvDeliveryTime.setOnClickListener(this);

		// tvImportCode = (TextView) v.findViewById(R.id.tvImportCode);

		// thong tin vansale
		rlOrderVansale = (RelativeLayout) v.findViewById(R.id.rlOrderVansale);
		tvTotalAmountVansales = (TextView) v.findViewById(R.id.tvTotalAmountVansales);
		btPayment = (Button) v.findViewById(R.id.btPayment);
		btPayment.setOnClickListener(this);
		cbOrder = (CheckBox) v.findViewById(R.id.cbOrder);
		cbOrder.setOnClickListener(this);
		llOrderPresale = (LinearLayout) v.findViewById(R.id.llOrderPresale);
		// GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType
		// = UserDTO.TYPE_VALSALES;
		// kiem tra don hang tao cho presale hay vansale
		if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES ||
				GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_TNPG ||
				GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSNPP ||
				GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSTINH
				) {
			this.orderType = CREATE_ORDER_FOR_PRESALE;
			rlOrderVansale.setVisibility(View.GONE);
		} else if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_VALSALES) {
			if (orderDTO.isFirstInit) {
				this.orderType = CREATE_ORDER_FOR_VANSALE;
			}
			// Render lai layout tuy thuoc vao loai don hang dan chon
			if (this.orderType == CREATE_ORDER_FOR_PRESALE) {
				tvTotalAmountVansales.setVisibility(View.GONE);
				btPayment.setVisibility(View.GONE);
				llOrderPresale.setVisibility(View.VISIBLE);
				if (orderDTO.isEditOrder) {
					cbOrder.setChecked(true);
					cbOrder.setEnabled(false);
				}
			} else {
				tvTotalAmountVansales.setVisibility(View.VISIBLE);
				btPayment.setVisibility(View.VISIBLE);
				llOrderPresale.setVisibility(View.GONE);
			}
		}

		// thong tin control cho hien thi CTKM cho don hang
		llPromotionForOrder = (LinearLayout) v.findViewById(R.id.llPromotionForOrder);
		tvSTTPromotionForOrder = (TextView) v.findViewById(R.id.tvSTTPromotionForOrder);
		tbListPromotionForOrder = (TableLayout) v.findViewById(R.id.tbListPromotionForOrder);
	}

	/**
	 * Khoi tao data khi tao moi don hang
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void initData() {
		// mac dinh them moi
		// SaleOrderDTO orderInfo = new SaleOrderDTO();
		orderDTO.orderInfo.createUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
		if (this.orderType == CREATE_ORDER_FOR_PRESALE) {
			orderDTO.orderInfo.orderType = SALE_ORDER_TABLE.ORDER_TYPE_PRESALE;
			orderDTO.orderInfo.approved = 0;// don hang kieu presale thi chua
											// duoc approve
		} else {
			orderDTO.orderInfo.orderType = SALE_ORDER_TABLE.ORDER_TYPE_VANSALE;
			orderDTO.orderInfo.approved = 1;// don hang kieu vansale thi duoc
											// approve luon
		}
		orderDTO.orderInfo.orderSource = 2;
		// orderInfo.shopId = 1;
		orderDTO.orderInfo.shopId = Integer.parseInt(GlobalInfo.getInstance().getProfile().getUserData().shopId);
		orderDTO.orderInfo.shopCode = GlobalInfo.getInstance().getProfile().getUserData().shopCode;
		orderDTO.orderInfo.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
		// orderDTO.orderInfo.state = 1; // chua duyet
		orderDTO.staffId = orderDTO.orderInfo.staffId;

		try {
			orderDTO.orderInfo.orderDate = DateUtils.now();
			orderDTO.orderInfo.createDate = orderDTO.orderInfo.orderDate;
			orderDTO.orderInfo.deliveryDate = orderDTO.orderInfo.orderDate;

			// ngay mai
			// orderDTO.deliveryDate = DateUtils.now().split(" ")[0];
			// tvDeliveryDate.setText(orderDTO.deliveryDate);
			// orderDTO.deliveryTime = "09:00";
			// tvDeliveryTime.setText(orderDTO.deliveryTime);
			enableButtonSave(false);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			orderDTO.orderInfo.orderDate = "";
		}
		orderDTO.orderInfo.synState = ABSTRACT_TABLE.CREATED_STATUS;
	}

	/**
	 * Tinh khuyen mai
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void calalPromotion() {
		if(!isRequesting) {
			isRequesting = true;

			if (activity != null) {
				activity.showLoadingDialog();
			}

			ActionEvent e = new ActionEvent();
			e.viewData = orderDTO;
			e.sender = this;
			e.action = ActionEventConstant.GET_PROMOTION_PRODUCT_FROM_SALE_PRODUCT;
			SaleController.getInstance().handleViewEvent(e);
		}
	}

	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: TruongHN
	 * @param listPromotion
	 * @return: void
	 * @throws:
	 */
	private void showPromotionProduct(ArrayList<OrderDetailViewDTO> listPromotion) {
		if (headerPromotionProducts.getHeaderCount() == 0) {
			// thong tin mat hang khuyen mai
			headerPromotionProducts.addColumns(TableDefineContanst.ORDER_PROMOTION_PRODUCT_HEADER_WIDTH,
					TableDefineContanst.ORDER_PROMOTION_PRODUCT_HEADER_TITLE, ImageUtil.getColor(R.color.BLACK),
					ImageUtil.getColor(R.color.TABLE_HEADER_BG));
		}
		// giu lai cac mat hang khuyen mai chon tu man hinh them hang
		for (int i = 0, size = orderDTO.listPromotionOrders.size(); i < size; i++) {
			if (orderDTO.listPromotionOrders.get(i).orderDetailDTO.programeType != 0) {
				listPromotion.add(0, orderDTO.listPromotionOrders.get(i));
			}
		}
		orderDTO.listPromotionOrders.clear();
		orderDTO.listPromotionOrders = listPromotion;
		// xoa het cac table layout lai
		tbPromotionProducts.removeAllViews();
		displayPromotion(listPromotion);
	}

	/**
	 * Them ds khuyen mai vao view
	 * 
	 * @author: TruongHN
	 * @param listPromotion
	 * @return: void
	 * @throws:
	 */
	private void displayPromotion(ArrayList<OrderDetailViewDTO> listPromotion) {
		orderDTO.numSKUPromotion = 0;
		orderDTO.orderInfo.discount = 0;
		orderDTO.promotionTotalWeight = 0;
		orderDTO.checkStockTotal();

		if (headerPromotionProducts.getHeaderCount() == 0) {
			// thong tin mat hang khuyen mai
			headerPromotionProducts.addColumns(TableDefineContanst.ORDER_PROMOTION_PRODUCT_HEADER_WIDTH,
					TableDefineContanst.ORDER_PROMOTION_PRODUCT_HEADER_TITLE, ImageUtil.getColor(R.color.BLACK),
					ImageUtil.getColor(R.color.TABLE_HEADER_BG));
		}

		if (listPromotion.size() > 0) {
			// remove table KM cho don hang
			tbListPromotionForOrder.removeAllViews();

			// ds mat hang ban
			OrderDetailViewDTO detailView;
			int numberRowCurrent = 0;
			// kiem tra co CTKM theo don hang hay khong (va co san pham KM theo
			// cung hay khong)
			// 0: Ko co CTKM theo don hang
			// 1: Co CTKM theo don hang
			// 2: Co CTKM theo don hang va co san pham khuyen mai
			for (int i = 0, size = listPromotion.size(); i < size; i++) {
				if (listPromotion.get(i).promotionType == OrderDetailViewDTO.PROMOTION_FOR_PRODUCT) {
					// <HaiTC> cap nhat dinh dang so luong ton kho thung/hop
					if (StringUtil.isNullOrEmpty(listPromotion.get(i).remaindStockFormat)) {
						listPromotion.get(i).remaindStockFormat = StringUtil.parseAmountMoney(String.valueOf(listPromotion.get(i).stock));
//						listPromotion.get(i).remaindStockFormat = GlobalUtil.formatNumberProductFlowConvfact(
//								listPromotion.get(i).stock, listPromotion.get(i).convfact);
					}
					// </HaiTC>
					OrderPromotionRow row = new OrderPromotionRow(activity, null);
					row.setListner(this);
					detailView = listPromotion.get(i);

					detailView.numberOrderView = i + 1;

					// Tinh so luong khuyen mai
					orderDTO.numSKUPromotion += detailView.orderDetailDTO.quantity;
					double discountRounded = detailView.orderDetailDTO.discountAmount;
					detailView.orderDetailDTO.discountAmount = Math.round(discountRounded);
					orderDTO.promotionTotalWeight += detailView.orderDetailDTO.totalWeight;
					// tinh tong so tien khuyen mai
					orderDTO.orderInfo.discount += detailView.orderDetailDTO.discountAmount;
					numberRowCurrent++;
					row.updateData(String.valueOf(numberRowCurrent), detailView, isCheckStockTotal);
					tbPromotionProducts.addView(row);
				} else {
					// neu la KM cho don hang --> KM cuoi cung
					// render KM don hang
					indexPromotionOrder = i;
					layoutPromotionForOrder(listPromotion.get(i), numberRowCurrent);
					break;
				}
			}

			// update row tong
//			promotionTotalRow = new OrderPromotionRow(activity, null);
//			promotionTotalRow.updateTotalPromotionRow("" + orderDTO.numSKUPromotion, "" + orderDTO.orderInfo.discount);
//			tbPromotionProducts.addView(promotionTotalRow);
			llPromotionProducts.setVisibility(View.VISIBLE);
			tvTextPromotionProduct.setVisibility(View.VISIBLE);

			// hien thi thong tin CTKM va d/s san pham cho CTKM cua don hang
			if (tbListPromotionForOrder.getChildCount() > 0) {
				llPromotionForOrder.setVisibility(View.VISIBLE);
			} else {
				llPromotionForOrder.setVisibility(View.GONE);
			}
		}
		
		// tinh so tien VAT sau khi tru khuyen mai
		orderDTO.orderInfo.total = orderDTO.orderInfo.amount - orderDTO.orderInfo.discount;

		// update text tong tien
		initTotalPriveVAT(orderDTO.orderInfo.total);
	}

	/**
	 * Layout ds KM cho don hang
	 * 
	 * @author: TruongHN
	 * @param listPromotion
	 * @param numberRowCurrent
	 * @return: void
	 * @throws:
	 */
	private void layoutPromotionForOrder(OrderDetailViewDTO detailView, int numberRowCurrent) {
		numberRowCurrent++;
		 tbListPromotionForOrder.removeAllViews();
		tvSTTPromotionForOrder.setText(String.valueOf(numberRowCurrent));

		// render row ZV19, ZV20,ZV21
		if (detailView.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ORDER
				|| detailView.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ZV21) {
			// <HaiTC> cap nhat dinh dang so luong ton kho thung/hop
			if (StringUtil.isNullOrEmpty(detailView.remaindStockFormat)) {
				detailView.remaindStockFormat = StringUtil.parseAmountMoney(String.valueOf(detailView.stock));
//				detailView.remaindStockFormat = GlobalUtil.formatNumberProductFlowConvfact(detailView.stock,
//						detailView.convfact);
			}
			// </HaiTC>
			OrderPromotionRow row = new OrderPromotionRow(activity, null);
			row.setListner(this);
			if(orderDTO.listPromotionForOrderChange.size() > 1){
				row.isAlowChange = true;
			}
			detailView.numberOrderView = numberRowCurrent;

			// Tinh so luong khuyen mai
			orderDTO.numSKUPromotion += detailView.orderDetailDTO.quantity;
			double discountRounded = detailView.orderDetailDTO.discountAmount;
			detailView.orderDetailDTO.discountAmount = Math.round(discountRounded);
			orderDTO.promotionTotalWeight += detailView.orderDetailDTO.totalWeight;
			// tinh tong so tien khuyen mai
			orderDTO.orderInfo.discount += detailView.orderDetailDTO.discountAmount;
			// thong tin % KM hoac so tien KM tren don hang
			orderDTO.orderInfo.discountPercent = detailView.orderDetailDTO.discountPercentage;
			orderDTO.orderInfo.discountAmount = detailView.orderDetailDTO.discountAmount;
			orderDTO.orderInfo.maxAmountFree = detailView.orderDetailDTO.maxAmountFree;
			orderDTO.orderInfo.programeCode = detailView.orderDetailDTO.programeCode;
			row.updateData("" + numberRowCurrent, detailView, isCheckStockTotal);
			tbListPromotionForOrder.addView(row);
		}
		// KM neu la KM 21 thi hien thi them ds san pham KM
		if (detailView.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ZV21) {
			for (int j = 0, sizepro = detailView.listPromotionForPromo21.size(); j < sizepro; j++) {
				// <HaiTC> cap nhat dinh dang so luong ton kho
				// thung/hop
				OrderDetailViewDTO promotion = detailView.listPromotionForPromo21.get(j);
				if (StringUtil.isNullOrEmpty(promotion.remaindStockFormat)) {
					promotion.remaindStockFormat = StringUtil.parseAmountMoney(String.valueOf(promotion.stock));
//					promotion.remaindStockFormat = GlobalUtil.formatNumberProductFlowConvfact(promotion.stock,
//							promotion.convfact);
				}
				// </HaiTC>
				OrderPromotionRow row = new OrderPromotionRow(activity, null);
				row.setListner(this);
				promotion.numberOrderView = j + 1;

				// Tinh so luong khuyen mai
				orderDTO.numSKUPromotion += promotion.orderDetailDTO.quantity;
//				double discountRounded = promotion.orderDetailDTO.discountAmount;
//				promotion.orderDetailDTO.discountAmount = Math.round(discountRounded);
				orderDTO.promotionTotalWeight += promotion.orderDetailDTO.totalWeight;
				// tinh tong so tien khuyen mai
				orderDTO.orderInfo.discount += promotion.orderDetailDTO.discountAmount;
				row.updateData("" + numberRowCurrent, promotion, isCheckStockTotal);
				tbListPromotionForOrder.addView(row);
			}
		}
	}

	/**
	 * Layout lai mat hang khuyen mai
	 * 
	 * @author: TruongHN
	 * @param listPromotion
	 * @return: void
	 * @throws:
	 */
	private void layoutPromotionProduct(ArrayList<OrderDetailViewDTO> listPromotion) {
		orderDTO.orderInfo.discount = 0;
		orderDTO.checkStockTotal();

		if (headerPromotionProducts.getHeaderCount() == 0) {
			// thong tin mat hang khuyen mai
			headerPromotionProducts.addColumns(TableDefineContanst.ORDER_PROMOTION_PRODUCT_HEADER_WIDTH,
					TableDefineContanst.ORDER_PROMOTION_PRODUCT_HEADER_TITLE, ImageUtil.getColor(R.color.BLACK),
					ImageUtil.getColor(R.color.TABLE_HEADER_BG));
		}
		tbPromotionProducts.removeAllViews();

		if (listPromotion.size() > 0) {
			// ds mat hang ban
			OrderDetailViewDTO detailView;
			int numberRowCurrent = 0;
			// kiem tra co CTKM theo don hang hay khong (va co san pham KM theo
			// cung hay khong)
			// 0: Ko co CTKM theo don hang
			// 1: Co CTKM theo don hang
			// 2: Co CTKM theo don hang va co san pham khuyen mai
			for (int i = 0, size = listPromotion.size(); i < size; i++) {
				if (listPromotion.get(i).promotionType == OrderDetailViewDTO.PROMOTION_FOR_PRODUCT) {
					// <HaiTC> cap nhat dinh dang so luong ton kho thung/hop
					if (StringUtil.isNullOrEmpty(listPromotion.get(i).remaindStockFormat)) {
						listPromotion.get(i).remaindStockFormat = StringUtil.parseAmountMoney(String.valueOf(listPromotion.get(i).stock));
//						listPromotion.get(i).remaindStockFormat = GlobalUtil.formatNumberProductFlowConvfact(
//								listPromotion.get(i).stock, listPromotion.get(i).convfact);
					}
					// </HaiTC>
					OrderPromotionRow row = new OrderPromotionRow(activity, null);
					row.setListner(this);
					detailView = listPromotion.get(i);
					// Tinh tong khoi luong sp KM
					orderDTO.promotionTotalWeight += detailView.orderDetailDTO.totalWeight;
					// tinh tong so tien khuyen mai
					orderDTO.orderInfo.discount += detailView.orderDetailDTO.discountAmount;
					detailView.numberOrderView = i + 1;
					numberRowCurrent++;
					row.updateData(String.valueOf(numberRowCurrent), detailView, isCheckStockTotal);
					tbPromotionProducts.addView(row);
				} else {
					// neu la KM cho don hang --> KM cuoi cung
					// render KM don hang
					indexPromotionOrder = i;
					layoutPromotionForOrder(listPromotion.get(i), numberRowCurrent);
					break;
				}
			}
			// update row tong
//			promotionTotalRow = new OrderPromotionRow(activity, null);
//			promotionTotalRow.updateTotalPromotionRow("" + orderDTO.numSKUPromotion, "" + orderDTO.orderInfo.discount);
//			tbPromotionProducts.addView(promotionTotalRow);
			llPromotionProducts.setVisibility(View.VISIBLE);
			tvTextPromotionProduct.setVisibility(View.VISIBLE);

			// tinh so tien VAT sau khi tru khuyen mai
			orderDTO.orderInfo.total = orderDTO.orderInfo.amount - orderDTO.orderInfo.discount;

			// update text tong tien
			initTotalPriveVAT(orderDTO.orderInfo.total);

			// hien thi thong tin CTKM va d/s san pham cho CTKM cua don hang
			if (tbListPromotionForOrder.getChildCount() > 0) {
				llPromotionForOrder.setVisibility(View.VISIBLE);
			} else {
				llPromotionForOrder.setVisibility(View.GONE);
			}

		}
	}
	
	/**
	 * Tinh lai amount & total cua don hang truoc khi luu
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param 
	 * @return: void
	 * @throws:
	 */
	private void recalculateAmountOrder() {
		orderDTO.orderInfo.amount = 0;
//		orderDTO.orderInfo.total = 0;
//		orderDTO.orderInfo.discount = 0;

		// ds mat hang ban
		for (int i = 0, size = orderDTO.listBuyOrders.size(); i < size; i++) {
			OrderDetailViewDTO dto = orderDTO.listBuyOrders.get(i);

			dto.orderDetailDTO.amount = dto.orderDetailDTO.price * dto.orderDetailDTO.quantity;
			orderDTO.orderInfo.amount += dto.orderDetailDTO.amount;
//			dto.quantity = GlobalUtil.convertQuantityToRealOrderOnView(dto.orderDetailDTO.quantity, dto.convfact);
		}
	}

	/**
	 * Lay chi tiet don hang da luu
	 * @author: dungnt19
	 * @since: 10:43:06 16-01-2014
	 * @return: void
	 * @throws:  
	 * @param orderId
	 */
	private void getOrderForEdit(String orderId) {
		if (activity != null) {
			activity.showLoadingDialog();
		}
		ActionEvent e = new ActionEvent();
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_ORDER, orderId);

		e.viewData = bundle;
		e.sender = this;
		e.action = ActionEventConstant.GET_ORDER_FOR_EDIT;

		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * Lay cac thong tin chung tren man hinh
	 * 
	 * @author: TruongHN
	 * @param code
	 * @return: void
	 * @throws:
	 */
	private void getCommonData(String code, long customerId) {
		ActionEvent e = new ActionEvent();
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, String.valueOf(customerId));
		bundle.putString(IntentConstants.INTENT_PRIORITY_CODE, code);
		// bundle.putString(IntentConstants.INTENT_PRIORITY_CODE,code);
		// bundle.putString(IntentConstants.INTENT_PRIORITY_CODE,code);

		e.viewData = bundle;
		e.sender = this;
		e.action = ActionEventConstant.GET_COMMON_DATA_ORDER;

		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * Hien thi thong tin mat hang ban tren view
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void showInfoOnView() {
		// orderDTO.orderInfo.amount = 0;
		// orderDTO.numSKU = 0;
		// Tinh toan so luong ton kho tong cua tong mat hang
		orderDTO.checkStockTotal();

		// thong tin khach hang
		infoOrder.updateData(orderDTO);
		// thong tin ma mat hang
		if (headerProducts.getHeaderCount() == 0) {
			int[] ORDER_PRODUCT_HEADER_WIDTH = { 50, 90, 195, 70, 90, 120, 120, 150, 60 };
			String[] ORDER_PRODUCT_HEADER_TITLE = { TableDefineContanst.COLUMN_NAME_STT,
					TableDefineContanst.COLUMN_NAME_PRODUCTS_CODE_SALE_STATISTICS, TableDefineContanst.COLUMN_PRODUCT_NAME, "ĐVT", 
					TableDefineContanst.COLUMN_NAME_REMAIN, TableDefineContanst.COLUMN_NAME_PRODUCT_PRICE, "Số lượng đặt", "Thành tiền", " " };
			//Khong hien thi gia
			if(GlobalInfo.getInstance().getIsShowPrice() == 0) {
				ORDER_PRODUCT_HEADER_WIDTH = new int[]{ 50, 90, 193 + 102 + 131, 70, 90, 120, 60 };
				ORDER_PRODUCT_HEADER_TITLE = new String[]{ TableDefineContanst.COLUMN_NAME_STT,
						TableDefineContanst.COLUMN_NAME_PRODUCTS_CODE_SALE_STATISTICS, TableDefineContanst.COLUMN_PRODUCT_NAME, "ĐVT", 
						TableDefineContanst.COLUMN_NAME_REMAIN, "Số lượng đặt", " " };
			}
			// thong tin mat hang ban
			headerProducts.addColumns(ORDER_PRODUCT_HEADER_WIDTH,
					ORDER_PRODUCT_HEADER_TITLE, ImageUtil.getColor(R.color.BLACK),
					ImageUtil.getColor(R.color.TABLE_HEADER_BG));
		}

		tbProducts.removeAllViews();
		// ds mat hang ban
		for (int i = 0, size = orderDTO.listBuyOrders.size(); i < size; i++) {
			// <HaiTC> cap nhat dinh dang so luong ton kho thung/hop
			if (StringUtil.isNullOrEmpty(orderDTO.listBuyOrders.get(i).remaindStockFormat)) {
				orderDTO.listBuyOrders.get(i).remaindStockFormat = StringUtil.parseAmountMoney(String.valueOf(orderDTO.listBuyOrders.get(i).stock));
//				orderDTO.listBuyOrders.get(i).remaindStockFormat = GlobalUtil.formatNumberProductFlowConvfact(
//						orderDTO.listBuyOrders.get(i).stock, orderDTO.listBuyOrders.get(i).convfact);
			}
			// </HaiTC>
			OrderProductRow row = new OrderProductRow(activity, null);
			OrderDetailViewDTO dto = orderDTO.listBuyOrders.get(i);
			dto.numberOrderView = i + 1;
			row.setListner(this);

			orderDTO.orderInfo.totalQuantity += dto.orderDetailDTO.quantity;
			orderDTO.orderInfo.totalWeight += dto.orderDetailDTO.totalWeight;
			dto.orderDetailDTO.amount = dto.orderDetailDTO.price * dto.orderDetailDTO.quantity;
			orderDTO.orderInfo.amount += dto.orderDetailDTO.amount;
//			dto.quantity = GlobalUtil.convertQuantityToRealOrderOnView(dto.orderDetailDTO.quantity, dto.convfact);
			dto.quantity = String.valueOf(dto.orderDetailDTO.quantity);
			row.updateData(dto, isCheckStockTotal);
			tbProducts.addView(row);
		}
		// update row tong
		productTotalRow = new OrderProductRow(activity, null);
		productTotalRow.updateTotalRow(String.valueOf(orderDTO.orderInfo.amount), String.valueOf(orderDTO.orderInfo.totalQuantity));
		tbProducts.addView(productTotalRow);
		//Khong hien thi gia thi khong hien thi tong tien
		if(GlobalInfo.getInstance().getIsShowPrice() == 0) {
			productTotalRow.setVisibility(View.GONE);
		}

		orderDTO.orderInfo.total = orderDTO.orderInfo.amount - orderDTO.orderInfo.discount;
		// update text tong tien
		initTotalPriveVAT(orderDTO.orderInfo.total);

		llPromotionProducts.setVisibility(View.GONE);
		tvTextPromotionProduct.setVisibility(View.GONE);
		tvTextTitleListProduct.setVisibility(View.VISIBLE);
		setPriority(orderDTO.orderInfo.priority);
		tvDeliveryDate.setText(orderDTO.deliveryDate);
		tvDeliveryTime.setText(orderDTO.deliveryTime);
	}

	/**
	 * Layout ds mat hang ban
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void reLayoutBuyProducts() {
		tbProducts.removeAllViews();
		orderDTO.orderInfo.totalQuantity = 0;
		orderDTO.orderInfo.totalWeight = 0;
		// thong tin khach hang
		infoOrder.updateData(orderDTO);
		// thong tin ma mat hang
		if (headerProducts.getHeaderCount() == 0) {
			int[] ORDER_PRODUCT_HEADER_WIDTH = { 50, 90, 195, 70, 90, 120, 120, 150, 60 };
			String[] ORDER_PRODUCT_HEADER_TITLE = { TableDefineContanst.COLUMN_NAME_STT,
					TableDefineContanst.COLUMN_NAME_PRODUCTS_CODE_SALE_STATISTICS, TableDefineContanst.COLUMN_PRODUCT_NAME, "ĐVT", 
					TableDefineContanst.COLUMN_NAME_REMAIN, TableDefineContanst.COLUMN_NAME_PRODUCT_PRICE, "Số lượng đặt", "Thành tiền", " " };
			//Khong hien thi gia
			if(GlobalInfo.getInstance().getIsShowPrice() == 0) {
				ORDER_PRODUCT_HEADER_WIDTH = new int[]{ 50, 90, 193 + 102 + 131, 70, 90, 120, 60 };
				ORDER_PRODUCT_HEADER_TITLE = new String[]{ TableDefineContanst.COLUMN_NAME_STT,
						TableDefineContanst.COLUMN_NAME_PRODUCTS_CODE_SALE_STATISTICS, TableDefineContanst.COLUMN_PRODUCT_NAME, "ĐVT", 
						TableDefineContanst.COLUMN_NAME_REMAIN, "Số lượng đặt", " " };
			}
			// thong tin mat hang ban
			headerProducts.addColumns(ORDER_PRODUCT_HEADER_WIDTH,
					ORDER_PRODUCT_HEADER_TITLE, ImageUtil.getColor(R.color.BLACK),
					ImageUtil.getColor(R.color.TABLE_HEADER_BG));
		}

		tbProducts.removeAllViews();
		// ds mat hang ban
		int i = 0;
		while (i < orderDTO.listBuyOrders.size()) {
			OrderDetailViewDTO dto = orderDTO.listBuyOrders.get(i);
			// <HaiTC> cap nhat dinh dang so luong ton kho thung/hop
			if (StringUtil.isNullOrEmpty(orderDTO.listBuyOrders.get(i).remaindStockFormat)) {
				orderDTO.listBuyOrders.get(i).remaindStockFormat = StringUtil.parseAmountMoney(String.valueOf(orderDTO.listBuyOrders.get(i).stock));
//				orderDTO.listBuyOrders.get(i).remaindStockFormat = GlobalUtil.formatNumberProductFlowConvfact(
//						orderDTO.listBuyOrders.get(i).stock, orderDTO.listBuyOrders.get(i).convfact);
			}
			// </HaiTC>
			if (dto.orderDetailDTO.quantity == 0) {
				orderDTO.listBuyOrders.remove(i);
			} else {

				OrderProductRow row = new OrderProductRow(activity, null);
				dto.numberOrderView = i + 1;
				orderDTO.orderInfo.totalQuantity += dto.orderDetailDTO.quantity;
				orderDTO.orderInfo.totalWeight += dto.orderDetailDTO.totalWeight;
//				dto.quantity = GlobalUtil.convertQuantityToRealOrderOnView(dto.orderDetailDTO.quantity, dto.convfact);
				dto.quantity = String.valueOf(dto.orderDetailDTO.quantity);
				row.setListner(this);
				row.updateData(dto, isCheckStockTotal);
				if(!isCanEditOrder()){
					row.etPrice.setEnabled(false);
					row.etRealOrder.setEnabled(false);
					row.ivAction.setEnabled(false);
					row.ivAction.setVisibility(View.GONE);
				}else{
					row.ivAction.setVisibility(View.VISIBLE);
				}
				tbProducts.addView(row);
				i++;
			}
		}

		// update row tong
		productTotalRow = new OrderProductRow(activity, null);
		productTotalRow.updateTotalRow(String.valueOf(orderDTO.orderInfo.amount), String.valueOf(orderDTO.orderInfo.totalQuantity));
		tbProducts.addView(productTotalRow);
		//Khong hien thi gia thi khong hien thi tong tien
		if(GlobalInfo.getInstance().getIsShowPrice() == 0) {
			productTotalRow.setVisibility(View.GONE);
		}

		// update text tong tien
		initTotalPriveVAT(orderDTO.orderInfo.total);
		if (orderDTO.listPromotionOrders.size() == 0) {// Dung cho kiem tra ton
														// kho, move ham hien
														// thi KM len truoc ham
														// reLayoutProduct
			llPromotionProducts.setVisibility(View.GONE);
			tvTextPromotionProduct.setVisibility(View.GONE);
		}
		tvTextTitleListProduct.setVisibility(View.VISIBLE);

		setPriority(orderDTO.orderInfo.priority);
		tvDeliveryDate.setText(orderDTO.deliveryDate);
		tvDeliveryTime.setText(orderDTO.deliveryTime);
		// hien thi import code (neu co)
		// if (!StringUtil.isNullOrEmpty(orderDTO.orderInfo.importCode)) {
		// tvImportCode.setText(StringUtil.getString(R.string.TEXT_REASON) + " "
		// + orderDTO.orderInfo.importCode.trim());
		// tvImportCode.setVisibility(View.VISIBLE);
		// } else {
		// tvImportCode.setVisibility(View.GONE);
		// }
	}

	private void initTotalPriveVAT(long total) {
		// update text tong tien
		SpannableObject obj = new SpannableObject();
		obj.addSpan(StringUtil.getString(R.string.TEXT_TOTAL_MONEY_PLUS_VAT) + Constants.STR_SPACE,
				ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);
		obj.addSpan(StringUtil.parseAmountMoney(String.valueOf(total)), ImageUtil.getColor(R.color.RED),
				android.graphics.Typeface.BOLD);
		tvTotalAmount.setText(obj.getSpan());
		tvTotalAmountVansales.setText(obj.getSpan());
		
		//Khong hien thi gia thi khong hien thi Tong tien
		if(GlobalInfo.getInstance().getIsShowPrice() == 0) {
			tvTotalAmount.setVisibility(View.INVISIBLE);
			tvTotalAmountVansales.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btAddBill) {
			// dong ban phim
			GlobalUtil.forceHideKeyboard(activity);
			if (orderDTO != null && orderDTO.orderInfo != null) {
				if (orderDTO.orderInfo.approved == 2 || !orderDTO.isEditOrder) {
					// chuyen sang man hinh them moi don hang
					gotoFindProductAddToListOrder();
				}
			}
		} else if (v == btSaveAndSend || v == btPayment) {
			// Truong hop vansale khong du hang thi ko cho dat hang
			if (orderType == CREATE_ORDER_FOR_VANSALE && !orderDTO.checkStockTotalSuccess()) {
				Collection<OrderDetailViewDTO> temp = (Collection<OrderDetailViewDTO>) orderDTO.listLackRemainProduct
						.values();
				ArrayList<String> listProductCode = new ArrayList<String>();
				for (OrderDetailViewDTO detail : temp) {
					listProductCode.add(detail.productCode);
				}

				GlobalUtil.showDialogConfirm(
						this,
						this.activity,
						StringUtil.getStringAndReplace(R.string.TEXT_CONFIRM_CHANGE_ORDER_MODE,
								TextUtils.join(", ", listProductCode)), StringUtil.getString(R.string.TEXT_AGREE),
						ACTION_CHANGE_MODE_SAVE_ORDER_OK, StringUtil.getString(R.string.TEXT_DENY),
						ACTION_CANCEL_CHANGE_MODE_SAVE_ORDER, null);
			} else {
				// kiem tra va luu don hang
				processSaveOrder();
			}
		} else if (v == btnCalPromotion) {
			// tin toan khuyen mai tu dong
			processCalPromotion();
		} else if (v == tvDeliveryDate && isCanEditOrder()) {
			activity.fragmentTag = OrderView.TAG;
			activity.showDatePickerWithDate(SalesPersonActivity.DATE_DIALOG_ID, tvDeliveryDate.getText().toString(), true);
		} else if (v == tvDeliveryTime && isCanEditOrder()) {
			activity.fragmentTag = OrderView.TAG;
			activity.showDialog(SalesPersonActivity.TIME_DIALOG_ID);
		} else if (v.getId() == R.id.btClose) {// TamPQ dong dialog nhac nho
			this.alertRemindDialog.dismiss();
			// chuyen sang man hinh thong tin khach hang
			handleAfterCreateOrder(orderDTO.customer.getCustomerId());
		} else if (v == btClosePopupPrograme) {
			if (alertSelectPromotionProduct.isShowing()) {
				alertSelectPromotionProduct.dismiss();
			}
		} else if (v == cbOrder) {
			// dat hang cho vansale
			if (cbOrder.isChecked()) {
				tvTotalAmountVansales.setVisibility(View.GONE);
				btPayment.setVisibility(View.GONE);
				llOrderPresale.setVisibility(View.VISIBLE);
				this.orderType = CREATE_ORDER_FOR_PRESALE;
			} else {
				tvTotalAmountVansales.setVisibility(View.VISIBLE);
				btPayment.setVisibility(View.VISIBLE);
				llOrderPresale.setVisibility(View.GONE);
				this.orderType = CREATE_ORDER_FOR_VANSALE;
			}

			// order type for calculate promotion
			if (this.orderType == CREATE_ORDER_FOR_PRESALE) {
				orderDTO.orderInfo.orderType = SALE_ORDER_TABLE.ORDER_TYPE_PRESALE;
				orderDTO.orderInfo.approved = 0;// don hang kieu presale thi
												// chua duoc approve
			} else {
				orderDTO.orderInfo.orderType = SALE_ORDER_TABLE.ORDER_TYPE_VANSALE;
				orderDTO.orderInfo.approved = 1;// don hang kieu vansale thi
												// duoc approve luon
			}

			// Neu da tinh KM roi thi khi chon checkbox thi tinh KM lai
			if (orderDTO.isEnableButton) {
				processCalPromotion();
			}
		} else if (v == btClosePopupPromotion) {
			if (alertListPromotion.isShowing()) {
				alertListPromotion.dismiss();
			}
		} else {
			super.onClick(v);
		}
	}

	/**
	 * Tinh toan khuyen mai tu dong
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void processCalPromotion() {
		// dong ban phim
		GlobalUtil.forceHideKeyboard(activity);
		// kiem tra neu mat hang bi xoa het
		if (orderDTO.listBuyOrders.size() == 0) {
			showPromotionProduct(new ArrayList<OrderDetailViewDTO>());
			// neu so luong sp khuyen mai > 0 & ton tai sp CTTB --> enable
			// button luu
			boolean errorInput = false;
			if (orderDTO.listPromotionOrders.size() == 0){
				errorInput = true;
			}else{
				for (OrderDetailViewDTO promotion : orderDTO.listPromotionOrders) {
					// Ds KM co KM  tu dong thi bat buoc phai co mat
					// hang ban
					if (promotion.orderDetailDTO.programeType == 0) {
						errorInput = true;
						break;
					}
				}
			}
			

			if (errorInput ) {
				activity.showDialog(StringUtil.getString(R.string.ERROR_CHOOSE_PRODUCT));
			} else {
				enableButtonSave(true);
			}
		} else {
			// validate don hang co mat hang ban thi moi tinh KM
			if (validateOrder() == -1) {
				orderDTO.listPromotionChange.clear();
				
				// gom cac sp cung gia
				ArrayList<OrderDetailViewDTO> groupedBuyOrders = new ArrayList<OrderDetailViewDTO>();
				
				for (int i = 0, size = orderDTO.listBuyOrders.size(); i < size; i++) {
					OrderDetailViewDTO buyProduct = orderDTO.listBuyOrders.get(i);
					int res = -1; // khong ton tai
					for(int j = 0, size2 = groupedBuyOrders.size(); j < size2; j++) {
						OrderDetailViewDTO groupBuyProduct = groupedBuyOrders.get(j);
						
						if(buyProduct.orderDetailDTO.productId == groupBuyProduct.orderDetailDTO.productId &&
								buyProduct.orderDetailDTO.price == groupBuyProduct.orderDetailDTO.price) {
							res = j;
							groupBuyProduct.orderDetailDTO.quantity += buyProduct.orderDetailDTO.quantity;
							groupBuyProduct.orderDetailDTO.amount = groupBuyProduct.orderDetailDTO.quantity * groupBuyProduct.orderDetailDTO.price; 
							break;
						}
					}
					
					if(res == -1) {
						groupedBuyOrders.add(buyProduct);
					}
				}
				
				orderDTO.listBuyOrders = groupedBuyOrders;

				//recalculate amount
				recalculateAmountOrder();
				
				// tinh khuyen mai
				calalPromotion();
			}

		}
	}

	/**
	 * Kiem tra thong tin va luu don hang
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void processSaveOrder() {
		boolean allowSave = false;
		// Validate co don hang thi moi cho luu
		if (orderDTO.listBuyOrders.size() == 0) {
			// neu so luong sp khuyen mai > 0 & ton tai sp CTTB --> enable
			// button luu
			boolean errorInput = false;
			if (orderDTO.listPromotionOrders.size() == 0){
				errorInput = true;
			}else{
				for (OrderDetailViewDTO promotion : orderDTO.listPromotionOrders) {
					// Ds KM co KM  tu dong thi bat buoc phai co mat
					// hang ban
					if (promotion.orderDetailDTO.programeType == 0) {
						errorInput = true;
						break;
					}
				}
			}
			
			if (errorInput) {
				activity.showDialog(StringUtil.getString(R.string.ERROR_CHOOSE_PRODUCT));
				allowSave = false;
			} else {
				allowSave = true;
			}
		} else {
			allowSave = true;
		}

		if (allowSave) {
			// Ngay giao hang khong hop le
			int inValidAmount = -1;
			if (orderType == CREATE_ORDER_FOR_PRESALE && !validateDeliveryDate()) {
				if (activity != null) {
					activity.showDialog(StringUtil.getString(R.string.TEXT_VALIDATE_DELIVERY_DATE));
				}
			} else if ((inValidAmount = validatePrice()) >= 0) {
				activity.showDialog(StringUtil.getString(R.string.TEXT_INPUT_PRODUCT_PRICE));
				OrderProductRow row = (OrderProductRow) tbProducts.getChildAt(inValidAmount);
				row.etPrice.requestFocus();
			} else {
				String customerName = "";
				if (orderDTO.customer != null && !StringUtil.isNullOrEmpty(orderDTO.customer.getCustomerName())) {
					customerName = orderDTO.customer.getCustomerName().trim();
				}
				GlobalUtil.showDialogConfirm(this, this.activity,
						StringUtil.getStringAndReplace(R.string.TEXT_CONFIRM_SAVE_ORDER, customerName),
						StringUtil.getString(R.string.TEXT_AGREE), ACTION_SAVE_ORDER_OK,
						StringUtil.getString(R.string.TEXT_DENY), ACTION_CANCEL_SAVE_ORDER, null);
			}
		}
	}

	/**
	 * Kiem tra co dong nao gia khong hop le khong
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param 
	 * @return: void
	 * @throws:
	 */
	private int validatePrice() {
		int result = -1;
		for (int i = 0, size = orderDTO.listBuyOrders.size(); i < size; i++) {
			OrderDetailViewDTO dto = orderDTO.listBuyOrders.get(i);
			if(StringUtil.isNullOrEmpty(dto.suggestedPrice) || dto.orderDetailDTO.price == 0) {
				result = i;
				break;
			}
		}
		
		return result;
	}

	/**
	 * Enable button
	 * 
	 * @author: TruongHN
	 * @param isEnable
	 * @return: void
	 * @throws:
	 */
	private void enableButtonSave(boolean isEnable) {
		orderDTO.isEnableButton = isEnable;
		GlobalUtil.setEnableButton(btPayment, isEnable);
		GlobalUtil.setEnableButton(btSaveAndSend, isEnable);
	}

	/**
	 * Enable all button
	 * 
	 * @author: TruongHN
	 * @param isEnable
	 * @return: void
	 * @throws:
	 */
	private void enableAllButton(boolean isEnable) {
		GlobalUtil.setEnableButton(btnCalPromotion, isEnable);
		GlobalUtil.setEnableButton(btAddBill, isEnable);
		// GlobalUtil.setEnableButton(btSave, isEnable);
		enableButtonSave(isEnable);
		if (orderDTO.isEditOrder) {
			cbOrder.setEnabled(isEnable);

			if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_VALSALES) {
				// Render lai layout tuy thuoc vao loai don hang dan chon
				if (this.orderType == CREATE_ORDER_FOR_PRESALE) {
					cbOrder.setChecked(true);
					cbOrder.setEnabled(false);
				}
			}
		}
	}

	/**
	 * Kiem tra enable/disable button
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void checkEnableButton() {
		// chi cho phep sua don hang tra ve trong ngay hien tại
		if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType != UserDTO.TYPE_PRESALES || (orderDTO.isEditOrder
				&& (orderDTO.orderInfo.approved != 2 || DateUtils.compareWithNow(orderDTO.orderInfo.createDate,
						"yyyy-MM-dd") != 0))) {
			enableAllButton(false);
//			spinnerPriority.setEnabled(false);
			//tvDeliveryTime.setEnabled(false);
			if (orderDTO.orderInfo.approved == 1) {
				isCheckStockTotal = false;
			}
		} else if (!orderDTO.isEnableButton) {
			enableButtonSave(false);
		} else {
			enableAllButton(true);
		}
		
		if(!isCanEditOrder()) {
			spinnerPriority.setEnabled(false);
		} else {
			spinnerPriority.setEnabled(true);
		}
		
	}
	
	/**
	 * Kiem tra don hang co duoc phep sua hay khong
	 * @author: dungnt19
	 * @since: 14:27:58 28-12-2013
	 * @return: boolean
	 * @throws:  
	 * @return
	 */
	public boolean isCanEditOrder() {
		boolean result = false;
		if ((orderDTO.isEditOrder == false) ||(orderDTO.isEditOrder && orderDTO.orderInfo.approved == 2 && DateUtils.compareWithNow(orderDTO.orderInfo.createDate,
				"yyyy-MM-dd") == 0)) {
			result = true;
		}
		
		return result;
	}

	/**
	 * Request tao moi don hang
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void requestCreateOrder() {
		// Cap nhat ngay
		if (!isRequesting) {
			activity.showLoadingDialog();
			checkOrderBeforeSave();
			isRequesting = true;
			orderDTO.orderInfo.orderDate = DateUtils.now();
			orderDTO.orderInfo.createDate = orderDTO.orderInfo.orderDate;
			orderDTO.orderInfo.updateUser = "";
			orderDTO.orderInfo.updateDate = "";

//			if (ApParamDTO.PRIORITY_NOW.equals(orderDTO.orderInfo.priority)) {
//				orderDTO.orderInfo.deliveryDate = orderDTO.orderInfo.orderDate;
//				recalculateDeliveryDateAndTime();
//			}
			if (StringUtil.isNullOrEmpty(orderDTO.orderInfo.deliveryDate)
					|| ":00".equals(orderDTO.orderInfo.deliveryDate)) {
				hardcode();
			}

			ActionEvent e = new ActionEvent();
			e.viewData = orderDTO;
			e.sender = this;
			e.action = ActionEventConstant.CREATE_NEW_ORDER;
			e.isNeedCheckTimeServer = true;
			SaleController.getInstance().handleViewEvent(e);
		}
	}

	/**
	 * Cap nhat don hang truoc khi luu
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void checkOrderBeforeSave() {
		if (this.orderType == CREATE_ORDER_FOR_PRESALE) {
			orderDTO.orderInfo.orderType = SALE_ORDER_TABLE.ORDER_TYPE_PRESALE;
			String strDateTime = DateUtils.getDateTimeStringFromDateAndTime(orderDTO.deliveryDate, orderDTO.deliveryTime);
			orderDTO.orderInfo.deliveryDate = strDateTime;
		} else {
			orderDTO.orderInfo.orderType = SALE_ORDER_TABLE.ORDER_TYPE_VANSALE;
			orderDTO.orderInfo.deliveryDate = "";
			orderDTO.orderInfo.priority = 0;
		}
	}

	/**
	 * 
	 * Fix ta.m bug deliver code khong co nen delivery date cung ko co'
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return: void
	 * @throws:
	 */
	private void hardcode() {

		// lay code

		for (int i = 0, size = orderDTO.listPriority.size(); i < size; i++) {

			ApParamDTO dto = orderDTO.listPriority.get(i);
			if (ApParamDTO.PRIORITY_NOW.equals(dto.apParamCode)) {
				orderDTO.orderInfo.priority = dto.apParamId;
				break;
			}
		}
		orderDTO.orderInfo.deliveryDate = orderDTO.orderInfo.createDate;
	}

	/**
	 * 
	 * Kiem tra ngay giao hang co hop le hay ko?
	 * 
	 * @author: Nguyen Thanh Dung
	 * @rewturn: boolean
	 * @throws:
	 */
	@SuppressLint("SimpleDateFormat") private boolean validateDeliveryDate() {
		boolean result = true;
		// lay code
		String appCode = "";
		for (int i = 0, size = orderDTO.listPriority.size(); i < size; i++) {
			ApParamDTO dto = orderDTO.listPriority.get(i);
			if (dto.apParamId == orderDTO.orderInfo.priority) {
				appCode = dto.apParamCode;
				break;
			}
		}

//		if (ApParamDTO.PRIORITY_NOW.equals(appCode)) {
//			orderDTO.orderInfo.deliveryDate = orderDTO.orderInfo.createDate;
//			recalculateDeliveryDateAndTime();
//
//		} else {
			String strDateTime = DateUtils.getDateTimeStringFromDateAndTime(orderDTO.deliveryDate,
					orderDTO.deliveryTime);
			orderDTO.orderInfo.deliveryDate = strDateTime;
//		}

		if (ApParamDTO.PRIORITY_OUT_DAY.equals(appCode)) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date createDate = formatter.parse(orderDTO.orderInfo.createDate);
				Date deliveryDate = formatter.parse(orderDTO.orderInfo.deliveryDate);

				if (deliveryDate.compareTo(createDate) == -1) {
					result = false;
				}
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e1));
			}
		}

		return result;
	}

	/**
	 * Request chinh sua don hang
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void requestEditOrder() {
		if (!isRequesting) {
			checkOrderBeforeSave();
			isRequesting = true;
			if (StringUtil.isNullOrEmpty(orderDTO.orderInfo.deliveryDate)
					|| ":00".equals(orderDTO.orderInfo.deliveryDate)) {
				hardcode();
			}
			if (activity != null) {
				activity.showLoadingDialog();
			}
			ActionEvent e = new ActionEvent();
			e.viewData = orderDTO;
			e.sender = this;
			e.action = ActionEventConstant.EDIT_AND_SEND_ORDER;
			// if (orderDTO.orderInfo.isSend == 1) {
			// e.isNeedCheckTimeServer = true;
			// }
			SaleController.getInstance().handleViewEvent(e);
		}
	}

	/**
	 * Validate don hang truoc khi luu
	 * 
	 * @author: TruongHN
	 * @return: int
	 * @throws:
	 */
	private int validateOrder() {
		int res = -1;
		// validate ds mat hang ban
		for (int i = 0, size = orderDTO.listBuyOrders.size(); i < size; i++) {
			OrderDetailViewDTO orderProduct = orderDTO.listBuyOrders.get(i);
			if (!GlobalUtil.isValidQuantity(orderProduct.quantity)
					&& !StringUtil.isNullOrEmpty(orderProduct.quantity.trim())) {
				// chua nhap so luong --> Gia tri thuc dat phai lon hon 0
				activity.showDialog(StringUtil.getString(R.string.ERROR_INVALID_REAL_ORDER));
				res = i;
				break;
			}
		}
		//
		return res;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == spinnerPriority) {
			ApParamDTO app = getPriority(spinnerPriority.getSelectedItemPosition());
			if (app != null) {
				orderDTO.orderInfo.priority = app.apParamId;
				Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
				SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
				SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

				if (ApParamDTO.PRIORITY_NOW.equals(app.apParamCode) || ApParamDTO.PRIORITY_IN_DAY.equals(app.apParamCode)) {
					tvTextTime.setVisibility(View.VISIBLE);
					tvDeliveryDate.setVisibility(View.VISIBLE);
					tvDeliveryTime.setVisibility(View.VISIBLE);
					tvDeliveryDate.setEnabled(false);
					tvDeliveryTime.setEnabled(true);

					tvDeliveryTime.setText(timeFormatter.format(calendar.getTime()));
					tvDeliveryDate.setTextColor(Color.GRAY);
				} else if (ApParamDTO.PRIORITY_OUT_DAY.equals(app.apParamCode)) {
					tvTextTime.setVisibility(View.VISIBLE);
					tvDeliveryDate.setVisibility(View.VISIBLE);
					tvDeliveryTime.setVisibility(View.VISIBLE);
					tvDeliveryDate.setEnabled(true);
					tvDeliveryTime.setEnabled(true);

					calendar.add(Calendar.DAY_OF_YEAR, 1);
					tvDeliveryTime.setText("08:00");
					tvDeliveryDate.setTextColor(Color.BLACK);
				}

				tvDeliveryDate.setText(dateFormatter.format(calendar.getTime()));
				tvDeliveryTime.setTextColor(Color.BLACK);

				if (firstTimeGetOrder) {
					firstTimeGetOrder = false;
					tvDeliveryDate.setText(orderDTO.deliveryDate);
					tvDeliveryTime.setText(orderDTO.deliveryTime);
				} else {
					orderDTO.deliveryDate = tvDeliveryDate.getText().toString().trim();
					orderDTO.deliveryTime = tvDeliveryTime.getText().toString().trim();
				}
			}
		}
	}

	/**
	 * 
	 * display find product add to order view
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	private void gotoFindProductAddToListOrder() {
		Bundle bundle = new Bundle();
		bundle.putSerializable(IntentConstants.INTENT_LIST_PRODUCT_NOT_IN, orderDTO.listBuyOrders);
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, orderDTO.customer.getCustomerId());
		bundle.putInt(IntentConstants.INTENT_CUSTOMER_TYPE_ID, orderDTO.customer.getCustomerTypeId());
		if (this.orderType == CREATE_ORDER_FOR_PRESALE) {
			bundle.putInt(IntentConstants.INTENT_STAFF_TYPE, UserDTO.TYPE_PRESALES);
		} else {
			bundle.putInt(IntentConstants.INTENT_STAFF_TYPE, UserDTO.TYPE_VALSALES);
		}
		bundle.putString(IntentConstants.INTENT_ORDER_TYPE, orderDTO.orderInfo.orderType);
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_LIST_PRODUCTS_ADD_ORDER_LIST;
		UserController.getInstance().handleSwitchFragment(e);

		orderDTO.isFirstInit = false;
	}

	/**
	 * Chuyen sang man hinh thong tin chi tiet khach hang
	 * 
	 * @author: TruongHN
	 * @param customerId
	 * @return: void
	 * @throws:
	 */
	private void handleAfterCreateOrder(String customerId) {
		int isOr = orderDTO.orderInfo.isVisitPlan == 0 ? 1 : 0;
		// ghi log ghe tham khi dat hang
		activity.requestInsertActionLog(orderDTO.beginTimeVisit, ActionLogDTO.TYPE_ORDER,
				String.valueOf(orderDTO.orderInfo.saleOrderId), orderDTO.customer.customerId, String.valueOf(isOr));

		activity.getOrderInLogForNotify();

		// Sau khi thực hiện xong các chức năng trên. Các anh nhớ thêm vào câu
		// lệnh hide button Đóng cửa giúp em.
		activity.removeMenuCloseCustomer();

		// Dat hang cho KH ngoai tuyen
		if (isOr == 1) {
			endVisitCustomer();
		} else {// trong tuyen
			// Kiem tra xem co da ket thuc hay chua U hoi co muon ket thuc hay
			// ko?
			ActionLogDTO action = GlobalInfo.getInstance().getProfile().getActionLogVisitCustomer();
			if (action != null && action.objectType.equals("0") && StringUtil.isNullOrEmpty(action.endTime)) {
				SpannableObject textConfirmed = new SpannableObject();
				textConfirmed.addSpan(StringUtil
						.getString(R.string.TEXT_ALREADY_VISIT_CUSTOMER),
						ImageUtil.getColor(R.color.WHITE),
						android.graphics.Typeface.NORMAL);
				textConfirmed.addSpan(" " + (!StringUtil.isNullOrEmpty(action.aCustomer.customerCode) ? action.aCustomer.customerCode.substring(0, 3) : action.aCustomer.customerName),
						ImageUtil.getColor(R.color.WHITE),
						android.graphics.Typeface.BOLD);
				textConfirmed.addSpan(" - ",
						ImageUtil.getColor(R.color.WHITE),
						android.graphics.Typeface.BOLD);
				textConfirmed.addSpan(action.aCustomer.customerName,
						ImageUtil.getColor(R.color.WHITE),
						android.graphics.Typeface.BOLD);
				textConfirmed.addSpan(" trong ",
						ImageUtil.getColor(R.color.WHITE),
						android.graphics.Typeface.NORMAL);
				textConfirmed.addSpan(DateUtils.getVisitTime(action.startTime),
						ImageUtil.getColor(R.color.WHITE),
						android.graphics.Typeface.BOLD);
				textConfirmed.addSpan(StringUtil.getString(R.string.TEXT_ASK_END_VISIT_CUSTOMER),
						ImageUtil.getColor(R.color.WHITE),
						android.graphics.Typeface.NORMAL);
				
				GlobalUtil.showDialogConfirmCanBackAndTouchOutSide(this, activity, textConfirmed.getSpan(), StringUtil.getString(R.string.TEXT_AGREE),
						ACTION_END_VISIT_OK, StringUtil.getString(R.string.TEXT_CANCEL), ACTION_END_VISIT_CANCEL, null,
						false, false);
			} else {
				gotoCustomerInfo();
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent actionEvent = modelEvent.getActionEvent();
		switch (actionEvent.action) {
		case ActionEventConstant.GET_PROMOTION_PRODUCT_FROM_SALE_PRODUCT:
			if (activity != null) {
				activity.closeProgressDialog();
			}
			isRequesting = false;
			SortedMap<Long, ArrayList<OrderDetailViewDTO>> listPromotion = (SortedMap<Long, ArrayList<OrderDetailViewDTO>>) modelEvent
					.getModelData();

			// Tinh so luong khuyen mai
			orderDTO.numSKUPromotion = 0;
			// tinh tong so tien khuyen mai
			orderDTO.orderInfo.discount = 0;

			// ArrayList<OrderDetailViewDTO> listPromotion =
			// (ArrayList<OrderDetailViewDTO>) modelEvent.getModelData();
			if (listPromotion != null) {
				showPromotionProduct(listPromotion.get(Long.valueOf(1)));
				reLayoutBuyProducts();
			}

			// // cap nhat lai, khong can tinh khuyen mai khi luu don hang nua
			// (neu khong co j thay doi)
			// orderDTO.isNeedCallPromotion = false;

			// Luu ds cac ds san pham khuyen mai co the duoc doi
			if (listPromotion.size() > 3) { // 1: ds hien thi, 10: ds hien thi
											// loi, 11: ds doi hang loi
				Iterator<Long> it = listPromotion.keySet().iterator();
				it.next();
				it.next();
				it.next();

				while (it.hasNext()) {
					Long md = it.next();
					ArrayList<OrderDetailViewDTO> listProductChange = listPromotion.get(md);
					orderDTO.listPromotionChange.put(md, listProductChange);
				}
			}

			ArrayList<OrderDetailViewDTO> listProductSalePromotionMissing = listPromotion.get(Long.valueOf(10));

			if (listProductSalePromotionMissing.size() > 0) {
				StringBuilder strMissing = new StringBuilder();

				for (OrderDetailViewDTO detailMissing : listProductSalePromotionMissing) {
					strMissing.append(String.valueOf(detailMissing.productCode) + ", ");
				}

				strMissing.replace(strMissing.length() - 2, strMissing.length() - 1, "");
				activity.showDialog(StringUtil.getStringAndReplace(R.string.TEXT_PROMOTION_PRODUCT_HAS_NO_PRICE,
						strMissing.toString()));
			} else {
				if (orderDTO.listBuyOrders.size() > 0) {
					enableButtonSave(true);
				} else {
					boolean hasPromotionProduct = false;
					boolean hasDisplayProduct = false;
					for (OrderDetailViewDTO promotion : orderDTO.listPromotionOrders) {
						// Ds KM co KM = tay or tu dong
						if (promotion.orderDetailDTO.programeType == 1 || promotion.orderDetailDTO.programeType == 0) {
							hasPromotionProduct = true;
							break;
						} else {
							hasDisplayProduct = true;
						}
					}

					if (hasPromotionProduct || !hasDisplayProduct) {
						// activity.showDialog(StringUtil
						// .getString(R.string.ERROR_CHOOSE_PRODUCT));
					} else {
						enableButtonSave(true);
					}
				}
			}
			requestInsertLogKPI(HashMapKPI.NVBH_TINHKHUYENMAI, actionEvent.startTimeFromBoot);
			break;
		case ActionEventConstant.GET_ORDER_FOR_EDIT:

			OrderViewDTO model = (OrderViewDTO) modelEvent.getModelData();
			if (model.orderInfo != null) {
				orderDTO.orderInfo = model.orderInfo;
			}

			if (model.listBuyOrders != null) {
				orderDTO.listBuyOrders = model.listBuyOrders;
			}

			firstTimeGetOrder = true;
			orderDTO.isChangeData = false;

			// TruongHN: fix bug mac dinh khi sua don hang la phai disable
			// button
			orderDTO.isEnableButton = false;

			recalculateDeliveryDateAndTime();

			// kiem tra don hang la don hang presale hay vansale
			if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES) {
				rlOrderVansale.setVisibility(View.GONE);
			} else if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_VALSALES) {
				if (SALE_ORDER_TABLE.ORDER_TYPE_PRESALE.equals(orderDTO.orderInfo.orderType)) {
					this.orderType = CREATE_ORDER_FOR_PRESALE;
					tvTotalAmountVansales.setVisibility(View.GONE);
					btPayment.setVisibility(View.GONE);
					llOrderPresale.setVisibility(View.VISIBLE);
					if (orderDTO.isEditOrder) {
						cbOrder.setChecked(true);
						cbOrder.setEnabled(false);
					}
				} else if (SALE_ORDER_TABLE.ORDER_TYPE_VANSALE.equals(orderDTO.orderInfo.orderType)) {
					this.orderType = CREATE_ORDER_FOR_VANSALE;
					llOrderPresale.setVisibility(View.GONE);
					tvTotalAmountVansales.setVisibility(View.VISIBLE);
					btPayment.setVisibility(View.VISIBLE);
				}
			}
			
			// Kiem tra xem co enable cac nut de thuc hien chuc nang hay ko?
			checkEnableButton();

			// Update promotion
			if (model.listPromotionOrders != null) {
				orderDTO.listPromotionOrders.clear();
				showPromotionProduct(model.listPromotionOrders);
			}

			// showInfoOnView();
			reLayoutBuyProducts();

			if (activity != null) {
				activity.closeProgressDialog();
			}
			break;
		case ActionEventConstant.EDIT_AND_SEND_ORDER: {
			// remove id trong listId neu co
			String orderId = String.valueOf(orderDTO.orderInfo.saleOrderId);
			if (!GlobalInfo.getInstance().notifyOrderReturnInfo.listOrderProcessedId.contains(orderId)) {
				GlobalInfo.getInstance().notifyOrderReturnInfo.listOrderProcessedId.add(orderId);
			}

			// closeProgressDialog();
			isRequesting = false;

			Bundle dataObject = new Bundle();
			dataObject.putSerializable(IntentConstants.INTENT_UPDATE_EDITED_ORDER, (Serializable) orderDTO);

			ListOrderView orderFragment = (ListOrderView) getActivity().getFragmentManager().findFragmentByTag(
					ListOrderView.TAG);
			if (orderFragment != null) {
				orderFragment.receiveBroadcast(ActionEventConstant.BROADCAST_UPDATE_EDIT_ORDER, dataObject);
			}

			if (activity != null) {
				activity.closeProgressDialog();
				// kiem tra network
				if (!GlobalUtil.checkNetworkAccess()) {
					activity.showToastMessage(StringUtil.getString(R.string.TEXT_NETWORK_DISABLE));
				}

				activity.getOrderInLogForNotify();
			}
			GlobalUtil.popBackStack(this.getActivity());

		}
			requestInsertLogKPI(HashMapKPI.NVBH_SUADONHANG, actionEvent.startTimeFromBoot);
			break;
		case ActionEventConstant.CREATE_NEW_ORDER:
			isRequesting = false;
			if (activity != null) {
				activity.closeProgressDialog();
				// kiem tra network
				if (!GlobalUtil.checkNetworkAccess()) {
					activity.showToastMessage(StringUtil.getString(R.string.TEXT_NETWORK_DISABLE));
				}
			}

			// chuyen sang man hinh thong tin khach hang
			handleAfterCreateOrder(orderDTO.customer.getCustomerId());
			requestInsertLogKPI(HashMapKPI.NVBH_LUUDONHANG, actionEvent.startTimeFromBoot);
			break;
		case ActionEventConstant.GET_COMMON_DATA_ORDER:
			ArrayList<Object> res = (ArrayList<Object>) modelEvent.getModelData();
			if (res != null) {
				// cap nhat ds muc do
				orderDTO.listPriority = (ArrayList<ApParamDTO>) res.get(0);

				if (orderDTO.listPriority.size() > 1) {
					if (!orderDTO.isEditOrder) {
						orderDTO.orderInfo.priority = orderDTO.listPriority.get(1).apParamId;
					}

				}
				displayListPriority();
			}
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return: void
	 * @throws:
	 */

	private void recalculateDeliveryDateAndTime() {
		if (orderDTO.orderInfo.deliveryDate != null) {
			String[] arrDate = orderDTO.orderInfo.deliveryDate.split(" ");

			// Ngay
			if (arrDate.length >= 1) {
				String[] dates = arrDate[0].trim().split("-");

				if (dates.length >= 3) {
					StringBuilder sbDate = new StringBuilder();
					sbDate.append(dates[2]).append("/").append(dates[1]).append("/").append(dates[0]);
					orderDTO.deliveryDate = sbDate.toString();
				}
			}

			// Gio
			if (arrDate.length >= 2) {
				String[] times = arrDate[1].trim().split(":");

				if (times.length >= 3) {
					StringBuilder sbTime = new StringBuilder();
					sbTime.append(times[0]).append(":").append(times[1]);
					orderDTO.deliveryTime = sbTime.toString();
				}
			} else {
				orderDTO.deliveryTime = "00:00";
			}
		}
	}

	/**
	 * Hien thi ds muc do
	 * 
	 * @author: TruongHN
	 * @param listApp
	 * @return: void
	 * @throws:
	 */
	private void displayListPriority() {
		if (orderDTO.listPriority != null) {
			String[] arr = new String[orderDTO.listPriority.size()];
			for (int i = 0, size = orderDTO.listPriority.size(); i < size; i++) {
				arr[i] = orderDTO.listPriority.get(i).apParamName;
			}
			// orderDTO.listPriority = listApp;
			// cap nhat vao spiner
			SpinnerAdapter adapterLine = new SpinnerAdapter(activity, R.layout.simple_spinner_item, arr);
			this.spinnerPriority.setAdapter(adapterLine);

			// if (orderDTO.listPriority.size() > 1) {
			// if (!orderDTO.isEditOrder) {
			// orderDTO.orderInfo.priority = orderDTO.listPriority.get(1).value;
			// }
			// // Set priority
			// setPriority(orderDTO.orderInfo.priority);
			// }

			// Set priority
			setPriority(orderDTO.orderInfo.priority);
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		ActionEvent actionEvent = modelEvent.getActionEvent();
		switch (actionEvent.action) {
		case ActionEventConstant.EDIT_AND_SEND_ORDER:
			if (activity != null) {
				activity.closeProgressDialog();
			}
			isRequesting = false;
			enableButtonSave(true);
			break;
		case ActionEventConstant.CREATE_NEW_ORDER:
			if (activity != null) {
				activity.closeProgressDialog();
			}
			isRequesting = false;
			enableButtonSave(true);
			break;
		case ActionEventConstant.GET_PROMOTION_PRODUCT_FROM_SALE_PRODUCT:
			if (activity != null) {
				activity.closeProgressDialog();
			}
			isRequesting = false;
			break;
		case ActionEventConstant.ACTION_GET_CURRENT_DATE_TIME_SERVER:
			if (activity != null) {
				activity.closeProgressDialog();
			}
			enableButtonSave(true);
			super.handleErrorModelViewEvent(modelEvent);
			break;
		default:
			// closeProgressDialog();
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}

	}

	@Override
	public void receiveBroadcast(int action, Bundle bundle) {
		// TODO Auto-generated method stub
		switch (action) {
		case ActionEventConstant.BROADCAST_CHOOSE_PRODUCT_ADD_ORDER_LIST:
			ListFindProductSaleOrderDetailViewDTO orgListProductDTO = (ListFindProductSaleOrderDetailViewDTO) bundle
					.getSerializable(IntentConstants.INTENT_PRODUCTS_ADD_ORDER_LIST);
			if (orgListProductDTO != null) {
				ArrayList<FindProductSaleOrderDetailViewDTO> listProduct = orgListProductDTO.listObject;
				if (listProduct != null && listProduct.size() > 0) {
					// ArrayList<OrderDetailViewDTO> listPromotion = new
					// ArrayList<OrderDetailViewDTO>();
					for (int i = listProduct.size() - 1; i >= 0; i--) {
						FindProductSaleOrderDetailViewDTO findProductDTO = listProduct.get(i);
						// add vao ds san pham ban
						OrderDetailViewDTO detailViewDTO = new OrderDetailViewDTO();
						// ma chuong trinh khuyen mai???
						SaleOrderDetailDTO saleOrderDTO = findProductDTO.saleOrderDetail;
						// bien de kiem tra them moi hay update
						boolean isAddNew = true;
						// kiem tra xem da co san pham nay chua, neu co roi thi
						// cap
						// nhat lai so luong dat
						if (!findProductDTO.hasSelectPrograme) {
							int res = checkExistProducts(findProductDTO.saleOrderDetail.productId, findProductDTO.saleOrderDetail.price);
							if (res >= 0) {
								orderDTO.orderInfo.totalQuantity -= orderDTO.listBuyOrders.get(res).orderDetailDTO.quantity;
								orderDTO.orderInfo.amount -= orderDTO.listBuyOrders.get(res).orderDetailDTO.amount;

								// So luong
								orderDTO.listBuyOrders.get(res).orderDetailDTO.quantity = GlobalUtil.calRealOrder(
										findProductDTO.numProduct, findProductDTO.convfact);
								orderDTO.listBuyOrders.get(res).orderDetailDTO.price = findProductDTO.saleOrderDetail.price;
								orderDTO.orderInfo.totalQuantity += orderDTO.listBuyOrders.get(res).orderDetailDTO.quantity;

								// So tien
								orderDTO.listBuyOrders.get(res).orderDetailDTO.amount = orderDTO.listBuyOrders.get(res).orderDetailDTO.quantity
										* orderDTO.listBuyOrders.get(res).orderDetailDTO.price;
								orderDTO.orderInfo.amount += orderDTO.listBuyOrders.get(res).orderDetailDTO.amount;

								orderDTO.orderInfo.total = orderDTO.orderInfo.amount - orderDTO.orderInfo.discount;
								// orderDTO.listBuyOrders.get(res).orderDetailDTO.quantity
								// = findProductDTO.numProduct;
								orderDTO.listBuyOrders.get(res).quantity = findProductDTO.numProduct;
								orderDTO.listBuyOrders.get(res).suggestedPrice = findProductDTO.suggestedPrice;

								// trong luong
								orderDTO.listBuyOrders.get(res).orderDetailDTO.totalWeight = findProductDTO.saleOrderDetail.totalWeight;
								isAddNew = false;
							} else {
								// them moi
							}
						} else {
							int res = checkExistPromotions(findProductDTO.saleOrderDetail.productId, findProductDTO.saleOrderDetail.programeType, findProductDTO.saleOrderDetail.programeCode);
							if (res >= 0) {
								// So luong
								orderDTO.listPromotionOrders.get(res).orderDetailDTO.quantity = GlobalUtil.calRealOrder(
										findProductDTO.numProduct, findProductDTO.convfact);

								isAddNew = false;
							}
						}
						if (isAddNew) {
							// saleOrderDTO.programeCode =
							// findProductDTO.promotionProgrameCode;
							saleOrderDTO.synState = ABSTRACT_TABLE.CREATED_STATUS;
							saleOrderDTO.updateUser = orderDTO.orderInfo.updateUser;
							saleOrderDTO.createUser = orderDTO.orderInfo.createUser;

							detailViewDTO.productCode = findProductDTO.productCode;
							detailViewDTO.productName = findProductDTO.productName;
							detailViewDTO.orderDetailDTO = saleOrderDTO;
							detailViewDTO.convfact = findProductDTO.convfact;
							detailViewDTO.isFocus = findProductDTO.mhTT;
							detailViewDTO.stock = findProductDTO.available_quantity;
							detailViewDTO.grossWeight = findProductDTO.grossWeight;
							detailViewDTO.suggestedPrice = findProductDTO.suggestedPrice;
							detailViewDTO.uom2 = findProductDTO.uom2;
							// <HaiTC> cap nhat dinh dang so luong ton kho
							// thung/hop
							detailViewDTO.remaindStockFormat = StringUtil.parseAmountMoney(String.valueOf(detailViewDTO.stock));
//							detailViewDTO.remaindStockFormat = GlobalUtil.formatNumberProductFlowConvfact(
//									detailViewDTO.stock, detailViewDTO.convfact);
							// </HaiTC>

							if (findProductDTO.hasSelectPrograme) {
								// co chon CT khuyen mai --> sp khuyen mai
								saleOrderDTO.isFreeItem = 1; // mat hang khuyen
																// mai
								detailViewDTO.orderDetailDTO.quantity = GlobalUtil.calRealOrder(
										findProductDTO.numProduct, findProductDTO.convfact);
//								detailViewDTO.orderDetailDTO.maxQuantityFree = detailViewDTO.orderDetailDTO.quantity;
								// listPromotion.add(detailViewDTO);
								if(indexPromotionOrder >= 0) {
									orderDTO.listPromotionOrders.add(indexPromotionOrder, detailViewDTO);									
								}
							} else {
								// san pham ban
								saleOrderDTO.programeCode = findProductDTO.promotionProgrameCode;
								saleOrderDTO.isFreeItem = 0; // mat hang ban
								saleOrderDTO.quantity = GlobalUtil.calRealOrder(findProductDTO.numProduct,
										findProductDTO.convfact);
								detailViewDTO.quantity = findProductDTO.numProduct;

								OrderProductRow row = new OrderProductRow(activity, null);
								row.setListner(this);
								row.updateData(detailViewDTO, isCheckStockTotal);
								tbProducts.addView(row, 0);

								orderDTO.orderInfo.totalQuantity += detailViewDTO.orderDetailDTO.quantity;
								detailViewDTO.orderDetailDTO.amount = detailViewDTO.orderDetailDTO.price
										* detailViewDTO.orderDetailDTO.quantity;

								orderDTO.orderInfo.amount += detailViewDTO.orderDetailDTO.amount;

								orderDTO.listBuyOrders.add(0, detailViewDTO);
							}

						}

					}
					// cap nhat lai stt cua row
					int numberRow = 1;
					for (int i = 0, size = orderDTO.listBuyOrders.size(); i < size; i++) {
						OrderProductRow row = (OrderProductRow) tbProducts.getChildAt(i);
						orderDTO.listBuyOrders.get(i).numberOrderView = numberRow;
						row.updateNumberRow(numberRow);
						numberRow++;
					}
					// tinh lai tong tien
					orderDTO.orderInfo.total = orderDTO.orderInfo.amount - orderDTO.orderInfo.discount;
					// update text tong tien
					initTotalPriveVAT(orderDTO.orderInfo.total);

					// them vao ds khuyen mai (neu co)
					displayPromotion(orderDTO.listPromotionOrders);

					// // can tinh lai khuyen mai
					// orderDTO.isNeedCallPromotion = true;
					// disable button
					enableButtonSave(false);
					orderDTO.isChangeData = true;
				}

			}
			break;
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				if (orderDTO.isEditOrder) {
					this.getOrderForEdit("" + orderDTO.orderInfo.saleOrderId);
				}
			}
			break;
		default:
			super.receiveBroadcast(action, bundle);
			break;
		}
	}

	/**
	 * Kiem tra ton tai mat hang trong ds hay khong
	 * 
	 * @author: TruongHN
	 * @param productId
	 * @return: int
	 * @throws:
	 */
	private int checkExistProducts(int productId, long price) {
		int res = -1; // khong ton tai
		for (int i = 0, size = orderDTO.listBuyOrders.size(); i < size; i++) {
			if (orderDTO.listBuyOrders.get(i).orderDetailDTO.productId == productId && 
					orderDTO.listBuyOrders.get(i).orderDetailDTO.price == price) {
				res = i;
				break;
			}
		}
		return res;
	}

	/**
	 * Kiem tra ton tai mat hang KM trong ds hay khong
	 * @author: TruongHN
	 * @param productId
	 * @return: int
	 * @throws:
	 */
	private int checkExistPromotions(int productId, int programeType, String programeCode) {
		int res = -1; // khong ton tai
		for (int i = 0, size = orderDTO.listPromotionOrders.size(); i < size; i++) {
			SaleOrderDetailDTO detail = orderDTO.listPromotionOrders.get(i).orderDetailDTO;
			if (detail.productId == productId && detail.programeType == programeType && detail.programeCode.equals(programeCode)) {
				res = i;
				break;
			}
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		switch (eventType) {
		case ACTION_VIEW_PRODUCT:
			OrderDetailViewDTO detailDto = (OrderDetailViewDTO) data;
			if (detailDto != null) {
				requestGetProductInfoDetail(String.valueOf(detailDto.orderDetailDTO.productId));
				// gotoIntroduceProduct(
				// String.valueOf(detailDto.orderDetailDTO.productId),
				// detailDto.productName, detailDto.productCode);
			}

			break;
		case ACTION_CHANGE_REAL_ORDER: {
			ArrayList<String> param = (ArrayList<String>) data;
			int index = Integer.parseInt(param.get(0));
			String newQuantity = param.get(1);
			// tinh tong
			if (productTotalRow != null && tbProducts != null) {
				reCalTotalPrice(index, newQuantity, null);
			}
			break;
		}
		case ACTION_CHANGE_REAL_PRICE: {
			ArrayList<String> param = (ArrayList<String>) data;
			int index = Integer.parseInt(param.get(0));
			String newPrice = param.get(1);
			// tinh tong
			if (productTotalRow != null && tbProducts != null) {
				reCalTotalPrice(index, null, newPrice);
			}
			break;
		}
		case ACTION_VIEW_PROMOTION: {
			// gotoListProductHasPromotion();
			// xem chi tiet khuyen mai
			OrderDetailViewDTO orderDto = (OrderDetailViewDTO) data;
			if (orderDto != null) {
				requestGetPromotionDetail(orderDto.orderDetailDTO.programeCode);
				orderDTO.isFirstInit = false;
			}
			break;
		}
		case ACTION_DELETE: {
			// xoa mat hang ra khoi don hang: chi xoa khi don hang tao moi hoac
			// don hang tu choi duyet
			if (orderDTO.orderInfo != null && (orderDTO.orderInfo.approved == 2 || !orderDTO.isEditOrder)) {
				// (orderDTO.orderInfo.isSend == 1 &&
				// !StringUtil.isNullOrEmpty(orderDTO.orderInfo.importCode) &&
				// !"OK".equals(orderDTO.orderInfo.importCode))) {
				OrderDetailViewDTO dto = (OrderDetailViewDTO) data;
				if (dto != null) {
					int indexRow = reCalTotalPrice(dto.numberOrderView, "", "");
					if (indexRow >= 0) {
						int numberRow = dto.numberOrderView;
						// cap nhat lai STT trong ds
						for (int i = dto.numberOrderView, size = orderDTO.listBuyOrders.size(); i < size; i++) {
							OrderProductRow row = (OrderProductRow) tbProducts.getChildAt(i);
							if (row != null) {
								orderDTO.listBuyOrders.get(i).numberOrderView = numberRow;
								row.updateNumberRow(numberRow);
								numberRow++;
							}
						}
						// xoa ra khoi danh sach
						orderDTO.listBuyOrders.remove(indexRow);
						// remove row
						tbProducts.removeViewAt(dto.numberOrderView - 1);

						// kiem tra neu la row cuoi thi cap nhat row tong
						if (orderDTO.listBuyOrders.size() == 0) {
							orderDTO.orderInfo.totalQuantity = 0;
							orderDTO.orderInfo.amount = 0;
							productTotalRow.updateTotalValue(String.valueOf(orderDTO.orderInfo.amount),
									String.valueOf(orderDTO.orderInfo.totalQuantity));
						}
					}
				}
			} else {
				// thong bao loi
			}
			break;
		}
		case ACTION_CHANGE_PROMOTION: {
			OrderDetailViewDTO orderDetailDto = (OrderDetailViewDTO) data;

			Iterator<Long> it = orderDTO.listPromotionChange.keySet().iterator();

			while (it.hasNext()) {
				Long md = it.next();
				ArrayList<OrderDetailViewDTO> listProductChange = orderDTO.listPromotionChange.get(md);

				if (listProductChange.size() > 0) {
					OrderDetailViewDTO promotionProduct = listProductChange.get(0);
					if (promotionProduct.keyList.longValue() == orderDetailDto.keyList.longValue()) {
						gotoListProductHasPromotion(listProductChange, orderDetailDto);
						break;
					}
				}
			}

			break;
		}
		case ACTION_DELETE_PROMOTION: {
			OrderDetailViewDTO orderDetailDto = (OrderDetailViewDTO) data;
			if (orderDetailDto != null && (orderDTO.orderInfo.approved == 2) || !orderDTO.isEditOrder) {
				// (orderDTO.orderInfo.isSend == 1 &&
				// !StringUtil.isNullOrEmpty(orderDTO.orderInfo.importCode) &&
				// !"OK".equals(orderDTO.orderInfo.importCode))) {
				// xoa mat hang khuyen mai thuoc CTKM/CTTB tu chon
				orderDTO.numSKUPromotion -= orderDetailDto.orderDetailDTO.quantity;
				orderDTO.orderInfo.discount -= orderDetailDto.orderDetailDTO.discountAmount;
//				if (productTotalRow != null){
//					promotionTotalRow.updateTotalPromotionRow("" + orderDTO.numSKUPromotion, ""
//							+ orderDTO.orderInfo.discount);
//				}
				
				if (orderDetailDto.numberOrderView > 0
						&& orderDetailDto.numberOrderView <= orderDTO.listPromotionOrders.size()) {

					int numberRow = orderDetailDto.numberOrderView;
					// cap nhat lai STT trong ds
					for (int i = orderDetailDto.numberOrderView, size = orderDTO.listPromotionOrders.size(); i < size; i++) {
						OrderPromotionRow row = (OrderPromotionRow) tbPromotionProducts.getChildAt(i);
						if (row != null) {
							orderDTO.listPromotionOrders.get(i).numberOrderView = numberRow;
							row.updateNumberRow(numberRow);
							numberRow++;
						}
					}

					// xoa ra khoi danh sach
					orderDTO.listPromotionOrders.remove(orderDetailDto.numberOrderView - 1);

					// remove row
					tbPromotionProducts.removeViewAt(orderDetailDto.numberOrderView - 1);
				}

				// update text tong tien
				initTotalPriveVAT(orderDTO.orderInfo.total);
			}

			break;
		}
		case ACTION_CANCEL_SAVE_ORDER: {
			break;
		}
		case ACTION_SAVE_ORDER_OK: {
			// Disable -> do not allow do action again
			enableButtonSave(false);

			// don hang ban nhung chua tra
			orderDTO.orderInfo.type = 1;
			orderDTO.orderInfo.totalWeight += orderDTO.promotionTotalWeight;
			if (isEditOrder) {
				// sua -> chuyen
				orderDTO.orderInfo.approved = 0;// don hang kieu presale thi
												// chua duoc approve
				requestEditOrder();

			} else {
				requestCreateOrder();
				// this.getCurrentDateTimeServer();
			}
			break;
		}
		case ACTION_CHANGE_MODE_SAVE_ORDER_OK: {
			cbOrder.setChecked(!cbOrder.isChecked());
			onClick(cbOrder);
			break;
		}
		case ACTION_CANCEL_CHANGE_MODE_SAVE_ORDER: {
			break;
		}
//		case ACTION_AGRRE_BACK: {
//			// dong y back tro lai
//			if (orderDTO.isBackToRemainOrder) {
//				RemainProductView remainFragment = (RemainProductView) getActivity().getFragmentManager()
//						.findFragmentByTag(RemainProductView.TAG);
//				if (remainFragment != null) {
//					remainFragment.receiveBroadcast(ActionEventConstant.BROADCAST_ORDER_VIEW, null);
//				}
//				GlobalUtil.popBackStack(this.getActivity());
//			} else {
//				if (orderDTO.isChangeData) {
//					GlobalUtil.popBackStack(this.getActivity());
//				}
//			}
//
//			break;
//		}
//		case ACTION_CANCEL_BACK: {
//			// khong dong y back
//			break;
//		}
		case ACTION_END_VISIT_OK: {
			// ket thuc ghe tham
			endVisitCustomer();
			break;
		}
		case ACTION_END_VISIT_CANCEL: {
			gotoCustomerInfo();
			break;
		}
		case ACTION_CHANGE_REAL_PROMOTION: {
			ArrayList<Integer> para = (ArrayList<Integer>) data;
			int indexPromo = para.get(0);
			int newQuanPro = para.get(1);
			// tinh tong
			if (productTotalRow != null && tbPromotionProducts != null) {
				reCalQuantityPromotion(indexPromo, newQuanPro);
			}
			break;
		}
		case ACTION_CHANGE_REAL_PROMOTION_ORDER: {
			ArrayList<Integer> para = (ArrayList<Integer>) data;
			int indexPromo = para.get(0);
			int newQuanPro = para.get(1);
			// tinh tong
			if (productTotalRow != null && tbListPromotionForOrder != null) {
				reCalQuantityPromotionOrder(indexPromo, newQuanPro);
			}
			break;
		}
		case ACTION_CHANGE_PROMOTION_FOR_PROMOTIOIN_ORDER: {
			// thay doi chuong trinh khuyen mai dang khuyen mai cho don hang
			showPopupChangePromotionForPromotionTypeOrder((OrderDetailViewDTO) data);
			break;
		}
		case ACTION_CHANGE_REAL_MONEY_PROMOTION: {
			OrderDetailViewDTO dto = (OrderDetailViewDTO) data;
			//Thay doi so tien cua KM 19, 20 & KM tien, % cho sp
			reCalMoneyPromotion(dto);
			break;
		}
		
		
		default:
			// super.onEvent(eventType, control, data);
			break;
		}

	}

	/**
	 * 
	 * hien thi popup thay doi chuong trinh khuyen mai cho loai khuyen mai theo
	 * don hang
	 * 
	 * @author: HaiTC3
	 * @param currentData
	 * @return: void
	 * @throws:
	 * @since: May 10, 2013
	 */
	public void showPopupChangePromotionForPromotionTypeOrder(OrderDetailViewDTO currentData) {
		if (alertListPromotion == null) {
			Builder build = new AlertDialog.Builder(activity, R.style.CustomDialogTheme);
			LayoutInflater inflater = this.activity.getLayoutInflater();
			View view = inflater.inflate(R.layout.layout_select_promotion_for_promotion_type_order, null);

			tbPromotionListView = (VinamilkTableView) view.findViewById(R.id.tbPromotionListView);
			tbPromotionListView.setVisibility(View.VISIBLE);
			btClosePopupPromotion = (Button) view.findViewById(R.id.btClosePopupPromotion);
			btClosePopupPromotion.setOnClickListener(this);

			tbPromotionListView.getHeaderView().addColumns(TableDefineContanst.SELECT_PROMOTION_TABLE_WIDTHS,
					TableDefineContanst.SELECT_PROMOTION_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK),
					ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			 tbPromotionListView.setNumItemsPage(10);

			build.setView(view);
			alertListPromotion = build.create();
			// alertProductDetail.setCancelable(false);

			Window window = alertListPromotion.getWindow();
			window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
			window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
		}
		if (isRefresh) {
			List<TableRow> listRows = new ArrayList<TableRow>();
			int pos = 1;
			for (int i = 0, size = orderDTO.listPromotionForOrderChange.size(); i < size; i++) {
				if (orderDTO.listPromotionForOrderChange.get(i).promotionType == OrderDetailViewDTO.PROMOTION_FOR_ORDER
						|| orderDTO.listPromotionForOrderChange.get(i).promotionType == OrderDetailViewDTO.PROMOTION_FOR_ZV21) {
					OrderDetailViewDTO dto = orderDTO.listPromotionForOrderChange.get(i);
					SelectPromotionForPromotionTypeOrder row = new SelectPromotionForPromotionTypeOrder(activity,
							tbPromotionListView);
					row.setClickable(true);
					row.setTag(dto);
					row.renderLayout(pos, dto);
					if(currentData.orderDetailDTO.programeCode.equals(dto.orderDetailDTO.programeCode)) {
						row.updateLayoutSelected();
					}
					row.setListener(this);
					pos++;
					listRows.add(row);
				}
			}
			String message = Constants.STR_BLANK;
			if (orderDTO.listPromotionForOrderChange.size() == 0) {
				message = getString(R.string.TEXT_NOTIFY_NOT_HAVE_PROGRAME);
				SelectProgrameForProduct row = new SelectProgrameForProduct(activity, tbPromotionListView);
				row.setClickable(true);
				row.renderLayoutEndRowNotSelectProgrameOrNull(message);
				pos++;
				listRows.add(row);
			}
			tbPromotionListView.addContent(listRows);
		}
		if(!alertListPromotion.isShowing()) {
			alertListPromotion.show();
		}
	}

	/**
	 * Thuc hien ket thuc ghe tham KH
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return: void
	 * @throws:
	 */

	public void endVisitCustomer() {
		activity.requestUpdateActionLog("0", null, null, activity);

		// kiem tra neu truoc do con man hinh kiem hang ton thi pop ra
//		if (orderDTO.isBackToRemainOrder) {
//			GlobalUtil.popBackStack(this.getActivity());
//		}
//
//		// pop ko cho quay ve man hinh tao don hang nua
//		GlobalUtil.popBackStack(this.getActivity());
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = new Bundle();
		e.action = ActionEventConstant.GET_CUSTOMER_LIST;
		SaleController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Chuyen qua man hinh thong tin kh
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return: void
	 * @throws:
	 */

	private void gotoCustomerInfo() {
		// kiem tra neu truoc do con man hinh kiem hang ton thi pop ra
		if (orderDTO.isBackToRemainOrder) {
			GlobalUtil.popBackStack(this.getActivity());
		}

		// pop ko cho quay ve man hinh tao don hang nua
		// TamPQ: set 1 frag ko cho back ve DSKH load lai DS
		CustomerListView frag = (CustomerListView) this.getActivity().getFragmentManager()
				.findFragmentByTag(CustomerListView.TAG);
		if (frag != null) {
			frag.isBackFromPopStack = true;
		}
		GlobalUtil.popBackStack(this.getActivity());

		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bunde = new Bundle();
		bunde.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "02-01");
		bunde.putString(IntentConstants.INTENT_CUSTOMER_ID, orderDTO.customer.getCustomerId());
		// bunde.putBoolean(IntentConstants.INTENT_IS_FROM_CREATE_ORDER, true);
		e.viewData = bunde;
		e.action = ActionEventConstant.GO_TO_CUSTOMER_INFO;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * 
	 * show list product has promotion
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	private void gotoListProductHasPromotion(ArrayList<OrderDetailViewDTO> listProductChange,
			OrderDetailViewDTO orderDetailDto) {

		this.listProductChange = listProductChange;
		productSelected = orderDetailDto;
		this.showPopupSelectPromotionProduct();

		// ActionEvent e = new ActionEvent();
		// e.sender = this;
		// Bundle bundle = new Bundle();
		// bundle.putSerializable(IntentConstants.INTENT_CHANGE_PRODUCT_PROMOTION,
		// listProductChange);
		// bundle.putSerializable(IntentConstants.INTENT_PRODUCT_PROMOTION_SELECTED,
		// orderDetailDto);
		// e.viewData = bundle;
		// e.action = ActionEventConstant.GO_TO_LIST_PROMOTION_PRODUCT;
		// SaleController.getInstance().handleSwitchFragment(e);
		// orderDTO.isFirstInit = false;
	}

	/**
	 * Tinh lai so tien tong, sau khi them or delete mat hang
	 * 
	 * @author: TruongHN
	 * @param indexRow
	 * @param newQuantity
	 * @return: res
	 * @throws:
	 */
	private int reCalTotalPrice(int indexRow, String newQuantity, String newPrice) {
		int res = -1;
		// tinh tong
		if (indexRow > 0 && indexRow <= orderDTO.listBuyOrders.size()) {
			orderDTO.orderInfo.discount = 0;
			OrderDetailViewDTO selectedDetail = orderDTO.listBuyOrders.get(indexRow - 1);
			if (selectedDetail != null) {
				res = indexRow - 1;
				long oldAmount = selectedDetail.orderDetailDTO.amount;
				int oldQuantity = selectedDetail.orderDetailDTO.quantity;
				double oldWeight = selectedDetail.orderDetailDTO.totalWeight;
				// tinh lai quantity moi
				if(newQuantity != null) {
					if (StringUtil.isNullOrEmpty(newQuantity)) {
						selectedDetail.orderDetailDTO.quantity = 0;
					} else {
	//					selectedDetail.orderDetailDTO.quantity = GlobalUtil.calRealOrder(newQuantity.trim(), selectedDetail.convfact);
						selectedDetail.orderDetailDTO.quantity = Integer.parseInt(newQuantity.trim());
					}
				}
				
				//Tinh lai price moi
				if(newPrice != null) {
					selectedDetail.suggestedPrice = newPrice; 
					if(StringUtil.isNullOrEmpty(newPrice)) {
						selectedDetail.orderDetailDTO.price = 0;
					} else {
						selectedDetail.orderDetailDTO.price = Long.parseLong(newPrice.trim());
					}
				}
				// selectedDetail.orderDetailDTO.quantity = newQuantity;
				selectedDetail.orderDetailDTO.totalWeight = selectedDetail.grossWeight
						* selectedDetail.orderDetailDTO.quantity;
				selectedDetail.totalOrderQuantity = selectedDetail.totalOrderQuantity - oldQuantity
						+ selectedDetail.orderDetailDTO.quantity;
				if(newQuantity != null) {
					selectedDetail.quantity = newQuantity;
				}
				long newAmount = selectedDetail.orderDetailDTO.price * selectedDetail.orderDetailDTO.quantity;
				selectedDetail.orderDetailDTO.amount = newAmount;

				// tinh lai tong
				orderDTO.orderInfo.amount = (orderDTO.orderInfo.amount - oldAmount) + newAmount;
				orderDTO.orderInfo.totalQuantity = orderDTO.orderInfo.totalQuantity - oldQuantity + selectedDetail.orderDetailDTO.quantity;
				orderDTO.orderInfo.totalWeight = (orderDTO.orderInfo.totalWeight - oldWeight)
						+ selectedDetail.orderDetailDTO.totalWeight;
				productTotalRow.updateTotalValue(String.valueOf(orderDTO.orderInfo.amount),
						String.valueOf(orderDTO.orderInfo.totalQuantity));

				// update thanh tien cua row dc chon
				OrderProductRow row = (OrderProductRow) tbProducts.getChildAt(indexRow - 1);
				if (row != null) {
					row.updateAmount(String.valueOf(newAmount));
				}
				// tinh so tien VAT sau khi tru khuyen mai
				orderDTO.orderInfo.total = orderDTO.orderInfo.amount - orderDTO.orderInfo.discount;

				// update text tong tien
				initTotalPriveVAT(orderDTO.orderInfo.total);

				// can tinh lai khuyen mai
				enableButtonSave(false);
				orderDTO.isChangeData = true;
			}
		}
		return res;
	}

	/**
	 * Tinh lai tong khuyen mai
	 * 
	 * @author: TruongHN
	 * @param indexRow
	 * @param newQuantity
	 * @return: int
	 * @throws:
	 */
	private void reCalQuantityPromotion(int indexRow, int newQuantity) {
		// tinh tong
		if (indexRow > 0 && indexRow <= orderDTO.listPromotionOrders.size()) {
			OrderDetailViewDTO selectedDetail = orderDTO.listPromotionOrders.get(indexRow - 1);
			if (selectedDetail != null) {
				int oldQuantity = selectedDetail.orderDetailDTO.quantity;
				double oldWeight = selectedDetail.orderDetailDTO.totalWeight;
				selectedDetail.orderDetailDTO.quantity = newQuantity;

				// selectedDetail.orderDetailDTO.quantity = newQuantity;]
				selectedDetail.totalOrderQuantity = selectedDetail.totalOrderQuantity - oldQuantity
						+ selectedDetail.orderDetailDTO.quantity;
				selectedDetail.orderDetailDTO.totalWeight = selectedDetail.grossWeight
						* selectedDetail.orderDetailDTO.quantity;

				// cap nhat lai mau
				if (isCheckStockTotal) {
					// update thanh tien cua row dc chon
					OrderPromotionRow row = (OrderPromotionRow) tbPromotionProducts.getChildAt(indexRow - 1);
					if (row != null) {
						row.checkStockTotal(selectedDetail);
					}
				}

				// tinh lai tong
				orderDTO.orderInfo.totalWeight = (orderDTO.orderInfo.totalWeight - oldWeight)
						+ selectedDetail.orderDetailDTO.totalWeight;
				orderDTO.numSKUPromotion = orderDTO.numSKUPromotion - oldQuantity + newQuantity;
//				if (promotionTotalRow != null){
//					promotionTotalRow.updateTotalPromotionRow("" + orderDTO.numSKUPromotion, ""
//							+ orderDTO.orderInfo.discount);
//				}
				
			}
		}
	}
	/**
	 * Tinh lai tong khuyen mai
	 * 
	 * @author: TruongHN
	 * @param indexRow
	 * @param newQuantity
	 * @return: int
	 * @throws:
	 */
	private void reCalQuantityPromotionOrder(int indexRow, int newQuantity) {
		if(indexPromotionOrder >= 0 && orderDTO.listPromotionOrders.size() > 0 && indexPromotionOrder < orderDTO.listPromotionOrders.size()) {
			OrderDetailViewDTO promotionDetail = orderDTO.listPromotionOrders.get(indexPromotionOrder);
			// tinh tong
			if (indexRow > 0 && indexRow <= promotionDetail.listPromotionForPromo21.size()) {
				OrderDetailViewDTO selectedDetail = promotionDetail.listPromotionForPromo21.get(indexRow - 1);
				if (selectedDetail != null) {
					int oldQuantity = selectedDetail.orderDetailDTO.quantity;
					double oldWeight = selectedDetail.orderDetailDTO.totalWeight;
					selectedDetail.orderDetailDTO.quantity = newQuantity;
					
					// selectedDetail.orderDetailDTO.quantity = newQuantity;]
					selectedDetail.totalOrderQuantity = selectedDetail.totalOrderQuantity - oldQuantity
							+ selectedDetail.orderDetailDTO.quantity;
					selectedDetail.orderDetailDTO.totalWeight = selectedDetail.grossWeight
							* selectedDetail.orderDetailDTO.quantity;
					
					// cap nhat lai mau
					if (isCheckStockTotal) {
						// update thanh tien cua row dc chon
						OrderPromotionRow row = (OrderPromotionRow) tbListPromotionForOrder.getChildAt(indexRow);
						if (row != null) {
							row.checkStockTotal(selectedDetail);
						}
					}
					
					// tinh lai tong
					orderDTO.orderInfo.totalWeight = (orderDTO.orderInfo.totalWeight - oldWeight)
							+ selectedDetail.orderDetailDTO.totalWeight;
					orderDTO.numSKUPromotion = orderDTO.numSKUPromotion - oldQuantity + newQuantity;
//					promotionTotalRow.updateTotalPromotionRow("" + orderDTO.numSKUPromotion, ""
//							+ orderDTO.orderInfo.discount);
				}
			}
		}
		
	}
	
	/**
	 * 
	*  Cap nhat khi thay doi tien KM cua KM cho don hang
	*  @author: Nguyen Thanh Dung
	*  @param dto
	*  @return: void
	*  @throws:
	 */
	private void reCalMoneyPromotion(OrderDetailViewDTO dto) {
		orderDTO.orderInfo.discount = orderDTO.orderInfo.discount - dto.oldDiscountAmount + dto.orderDetailDTO.discountAmount;
		orderDTO.orderInfo.total = orderDTO.orderInfo.amount - orderDTO.orderInfo.discount;
		
		//update info for order if change money of promotion ZV19,20
		if(dto.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ORDER) {
			orderDTO.orderInfo.discountAmount = dto.orderDetailDTO.discountAmount;
		}
		
		// update text tong tien
		initTotalPriveVAT(orderDTO.orderInfo.total);
//		if (promotionTotalRow != null) {
//			promotionTotalRow.updateTotalPromotionRow("" + orderDTO.numSKUPromotion, "" + orderDTO.orderInfo.discount);
//		}
	}

	/**
	 * updateDisplay
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public void updateDate(int day, int month, int year) {
		// this.mDay = day;
		// this.mMonth = month;
		// this.mYear = year;
		//
		String sDay = String.valueOf(day);
		String sMonth = String.valueOf(month + 1);
		if (day < 10) {
			sDay = "0" + sDay;
		}
		if (month + 1 < 10) {
			sMonth = "0" + sMonth;
		}

		tvDeliveryDate.setTextColor(ImageUtil.getColor(R.color.BLACK));
		tvDeliveryDate.setText(new StringBuilder()
		// Month is 0 based so add 1
				.append(sDay).append("/").append(sMonth).append("/").append(year).append(" "));

		orderDTO.deliveryDate = tvDeliveryDate.getText().toString().trim();
		//

	}

	/**
	 * Cap nhat thoi gian
	 * 
	 * @author: TruongHN
	 * @param hourOfDay
	 * @param minute
	 * @return: void
	 * @throws:
	 */
	public void updateTime(int hourOfDay, int minute) {
		String hour = String.valueOf(hourOfDay);
		String sMinute = String.valueOf(minute);
		if (hourOfDay < 10) {
			hour = "0" + hour;
		}
		if (minute < 10) {
			sMinute = "0" + sMinute;
		}
		tvDeliveryTime.setTextColor(ImageUtil.getColor(R.color.BLACK));
		tvDeliveryTime.setText(hour + ":" + sMinute);

		orderDTO.deliveryTime = tvDeliveryTime.getText().toString().trim();
	}

	/**
	 * Lay app value
	 * 
	 * @author: TruongHN
	 * @param index
	 * @return: int
	 * @throws:
	 */
	private ApParamDTO getPriority(int index) {
		ApParamDTO app = null;
		if (index >= 0 && index < orderDTO.listPriority.size()) {
			app = orderDTO.listPriority.get(index);
		}
		return app;
	}

	/**
	 * Set muc do khan khi sua don hang
	 * 
	 * @author: TruongHN
	 * @param value
	 * @return: void
	 * @throws:
	 */
	private void setPriority(long id) {
		if (id > 0) {
			for (int i = 0, size = orderDTO.listPriority.size(); i < size; i++) {
				if (orderDTO.listPriority.get(i).apParamId == id) {
					spinnerPriority.setSelection(i);
				}
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * updateFeedbackRow
	 * 
	 * @author: TamPQ
	 * @param value
	 * @return: void
	 * @throws:
	 */
	public void updateFeedbackRow(FeedBackDTO item) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.action = ActionEventConstant.UPDATE_FEEDBACK;
		e.viewData = item;
		// e.isNeedCheckTimeServer = true;
		SaleController.getInstance().handleViewEvent(e);
	}


	/**
	 * Xu ly xu kien back he thong
	 * 
	 * @author: TruongHN
	 * @return: int
	 * @throws:
	 */
//	public int onBackPressed() {
//		int handleBack = -1; // chua xu ly
//		if (!orderDTO.isEditOrder) {
//			// neu tao moi don hang, cancel request
//			handleBack = ACTION_CANCEL_BACK_DEFAULT;
//			String mess = "";
//			if (orderDTO.isBackToRemainOrder) {
//				mess = StringUtil.getString(R.string.TEXT_CONFIRM_BACK_REMAIN_ORDER);
//				// hien thi dialog confirm
//				GlobalUtil.showDialogConfirm(this, this.activity, mess, StringUtil.getString(R.string.TEXT_AGREE),
//						ACTION_AGRRE_BACK, StringUtil.getString(R.string.TEXT_DENY), ACTION_CANCEL_BACK, null);
//			} else {
//				if (orderDTO.isChangeData) {
//					// neu thay doi
//					handleBack = ACTION_CANCEL_BACK_DEFAULT;
//					mess = StringUtil.getString(R.string.TEXT_CONFIRM_BACK_ORDER);
//					GlobalUtil.showDialogConfirm(this, this.activity, mess, StringUtil.getString(R.string.TEXT_AGREE),
//							ACTION_AGRRE_BACK, StringUtil.getString(R.string.TEXT_DENY), ACTION_CANCEL_BACK, null);
//				} else {
//					// back binh thuong
//					GlobalUtil.popBackStack(this.getActivity());
//				}
//
//			}
//
//		} else {
//			// nguoc lai, neu nhu sua don hang thi kiem tra da thay doi chua
//			// neu chua thay doi thi back binh thuong
//			String mess = "";
//			if (orderDTO.isChangeData) {
//				// neu thay doi
//				handleBack = ACTION_CANCEL_BACK_DEFAULT;
//				mess = StringUtil.getString(R.string.TEXT_CONFIRM_BACK_ORDER);
//				GlobalUtil.showDialogConfirm(this, this.activity, mess, StringUtil.getString(R.string.TEXT_AGREE),
//						ACTION_AGRRE_BACK, StringUtil.getString(R.string.TEXT_DENY), ACTION_CANCEL_BACK, null);
//			}
//
//		}
//
//		return handleBack;
//	}

	/**
	 * Kiem tra co thay doi du lieu hay khong
	 * 
	 * @author: TruongHN
	 * @return: String - message hien thi thong bao
	 * @throws:
	 */
	public String checkChangeData() {
		String strMessage = ""; // khong thay doi
		if (!orderDTO.isEditOrder) {
			// neu tao moi don hang, cancel request
			if (orderDTO.isBackToRemainOrder) {
				strMessage = StringUtil.getString(R.string.TEXT_CONFIRM_EXIT_ORDER);
				// strMessage =
				// StringUtil.getString(R.string.TEXT_CONFIRM_BACK_REMAIN_ORDER);
			} else {
				if (orderDTO.isChangeData) {
					// neu thay doi
					strMessage = StringUtil.getString(R.string.TEXT_CONFIRM_EXIT_ORDER);
					// strMessage =
					// StringUtil.getString(R.string.TEXT_CONFIRM_BACK_ORDER);
				}
			}

		} else {
			// nguoc lai, neu nhu sua don hang thi kiem tra da thay doi chua
			// neu chua thay doi thi back binh thuong
			if (orderDTO.isChangeData) {
				// neu thay doi
				strMessage = StringUtil.getString(R.string.TEXT_CONFIRM_EXIT_ORDER);
				// strMessage =
				// StringUtil.getString(R.string.TEXT_CONFIRM_BACK_ORDER);
			}

		}
		return strMessage;
	}

	/**
	 * 
	 * show popup select promotion product
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 7, 2013
	 */
	private void showPopupSelectPromotionProduct() {
		if (alertSelectPromotionProduct == null) {
			Builder build = new AlertDialog.Builder(activity, R.style.CustomDialogTheme);
			LayoutInflater inflater = this.activity.getLayoutInflater();
			View view = inflater.inflate(R.layout.layout_select_product_promotion_view, null);

			tbProductPromotionList = (VinamilkTableView) view.findViewById(R.id.tbProductPromotionList);
			tbProductPromotionList.setListener(this);

			tvCTKMCode = (TextView) view.findViewById(R.id.tvCTKMCode);
			tvCTKMName = (TextView) view.findViewById(R.id.tvCTKMName);
			btClosePopupPrograme = (Button) view.findViewById(R.id.btClosePopupPrograme);
			btClosePopupPrograme.setOnClickListener(this);

			tbProductPromotionList.getHeaderView().addColumns(
					TableDefineContanst.SELECT_PRODUCT_PROMOTION_TABLE_WIDTHS,
					TableDefineContanst.SELECT_PRODUCT_PROMOTION_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK),
					ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			tbProductPromotionList.getPagingControl().setVisibility(View.GONE);

			build.setView(view);
			alertSelectPromotionProduct = build.create();
			// alertProductDetail.setCancelable(false);

			Window window = alertSelectPromotionProduct.getWindow();
			window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
			window.setLayout(LayoutParams.WRAP_CONTENT, GlobalUtil.dip2Pixel(600));
			window.setGravity(Gravity.CENTER);
		}

		tvCTKMCode.setText(productSelected.orderDetailDTO.programeCode);
		tvCTKMName.setText(productSelected.orderDetailDTO.programeName);

		int pos = 1;
		List<TableRow> listRows = new ArrayList<TableRow>();
		if (this.listProductChange.size() > 0) {
			for (OrderDetailViewDTO dto : this.listProductChange) {
				SelectPromotionProductRow row = new SelectPromotionProductRow(activity, tbProductPromotionList);
				row.setClickable(true);
				row.setTag(Integer.valueOf(pos));
				row.renderLayout(pos, dto);
				if (this.productSelected.productCode.equals(dto.productCode)) {
					row.updateLayoutSelected();
				}
				row.setListener(this);
				listRows.add(row);
				pos++;
			}
		} else {
			SelectPromotionProductRow row = new SelectPromotionProductRow(activity, tbProductPromotionList);
			row.setClickable(true);
			row.setListener(this);
			row.renderLayoutNotify(getString(R.string.TEXT_NOTIFY_PRODUCT_NULL));
			listRows.add(row);
		}

		tbProductPromotionList.addContent(listRows);
		if (!alertSelectPromotionProduct.isShowing()) {
			alertSelectPromotionProduct.show();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.listener.VinamilkTableListener#
	 * handleVinamilkTableloadMore(android.view.View, java.lang.Object)
	 */
	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.listener.VinamilkTableListener#
	 * handleVinamilkTableRowEvent(int, android.view.View, java.lang.Object)
	 */
	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		// TODO Auto-generated method stub
		if (control == tbProductPromotionList) {
			// send borad cast ProductHasPromotionDTO to order view
			if (action == ActionEventConstant.BROADCAST_CHANGE_PROMOTION_PRODUCT) {
				this.updatePromotionProductAfterSelectInPopup((OrderDetailViewDTO) data, productSelected);
				if (alertSelectPromotionProduct.isShowing()) {
					alertSelectPromotionProduct.dismiss();
				}
			}
		} else if (control == tbPromotionListView) {
			if (action == ActionEventConstant.ACTION_SELECT_PROMOTION_TYPE_PROMOTION_ORDER) {
				OrderDetailViewDTO dto = (OrderDetailViewDTO) data;
				if (!orderDTO.listPromotionOrders.get(indexPromotionOrder).orderDetailDTO.programeCode
						.equals(dto.orderDetailDTO.programeCode)) {
					orderDTO.listPromotionOrders.set(indexPromotionOrder, dto);

					// layout ds sp khuyen mai
					layoutPromotionProduct(orderDTO.listPromotionOrders);
				}
				if (alertListPromotion.isShowing()) {
					alertListPromotion.dismiss();
				}
			}
		}
	}

	/**
	 * 
	 * update promotion for product after change product
	 * 
	 * @param productPromotion
	 * @param oldPromotion
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 7, 2013
	 */
	private void updatePromotionProductAfterSelectInPopup(OrderDetailViewDTO productPromotion,
			OrderDetailViewDTO oldPromotion) {
		if (productPromotion.orderDetailDTO.productId != oldPromotion.orderDetailDTO.productId) {
			if(productPromotion.promotionType == OrderDetailViewDTO.PROMOTION_FOR_PRODUCT) {
				for (int i = 0, size = orderDTO.listPromotionOrders.size(); i < size; i++) {
					OrderDetailViewDTO orderDetail = orderDTO.listPromotionOrders.get(i);

					if (orderDetail.keyList != null && orderDetail.keyList.longValue() == oldPromotion.keyList.longValue()) {
						orderDTO.listPromotionOrders.set(i, productPromotion);
						// enableButtonSave(false);
						break;
					}
				}
			} else if (productPromotion.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ORDER_TYPE_PRODUCT) {
				OrderDetailViewDTO promotionOrder = orderDTO.listPromotionOrders.get(indexPromotionOrder);
				
				for (int i = 0, size = promotionOrder.listPromotionForPromo21.size(); i < size; i++) {
					OrderDetailViewDTO orderDetail = promotionOrder.listPromotionForPromo21.get(i);

					if (orderDetail.keyList != null && orderDetail.keyList.longValue() == oldPromotion.keyList.longValue()) {
						promotionOrder.listPromotionForPromo21.set(i, productPromotion);
						// enableButtonSave(false);
						break;
					}
				}
			}
			

			// Kiem tra xem co enable cac nut de thuc hien chuc nang hay ko?
			checkEnableButton();

			// cap nhat thong tin san pham khuyen mai
			tbPromotionProducts.removeAllViews();
			displayPromotion(orderDTO.listPromotionOrders);

			reLayoutBuyProducts();
		}
	}

}
