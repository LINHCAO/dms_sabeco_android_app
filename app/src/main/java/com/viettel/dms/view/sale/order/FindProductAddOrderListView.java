/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.constants.TableDefineContanst;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.AutoCompleteFindProductSaleOrderDetailViewDTO;
import com.viettel.dms.dto.view.FindProductSaleOrderDetailViewDTO;
import com.viettel.dms.dto.view.ListFindProductOrderListDTO;
import com.viettel.dms.dto.view.ListFindProductSaleOrderDetailViewDTO;
import com.viettel.dms.dto.view.OrderDetailViewDTO;
import com.viettel.dms.dto.view.ProgrameForProductDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.sqllite.db.SALE_ORDER_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.SalesPersonActivity;
import com.viettel.dms.view.sale.promotion.PromotionProgrameDetailView;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * 
 * find product to add order list
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class FindProductAddOrderListView extends BaseFragment implements
		OnEventControlListener, OnClickListener, VinamilkTableListener {

	public static final String TAG = FindProductAddOrderListView.class.getName();
	// back action
	public static final int BACK_ACTION = 0;
	// table vinamilk list product
	VinamilkTableView tbProductOrderList;
	// text view display number product result
	TextView tvNumberProduct;
	// list product result
	ListFindProductOrderListDTO listProduct;
	// button close
	Button btClose;
	// button search
	Button btSearch;
	// button choose
	Button btChoose;
	// input order code
	VNMEditTextClearable etInputOrderCode;
	// // input CTKM code
	// VNMEditTextClearable etInputCTKMCode;
	// input order Name
	VNMEditTextClearable etInputOrderName;
	// button clear all data input
	Button btClearAllInput;
	// main activity
	private SalesPersonActivity parent;
	// flag when searching product
	boolean isSearchingProduct = false;
	// flag when load list product the first
	boolean isFirstLoadProduct = false;
	// button close popup programe
	Button btClosePopupPrograme;
	// button close popup and reset choose programe
	Button btResetChoosePrograme;
	// limit row in page
	public static final int LIMIT_ROW_PER_PAGE = 10;
	// list product
	ListFindProductSaleOrderDetailViewDTO listDTO = new ListFindProductSaleOrderDetailViewDTO();
	// list product
	ListFindProductSaleOrderDetailViewDTO orgListDTO = new ListFindProductSaleOrderDetailViewDTO();
	// list number order for each product selected
	// ArrayList<Integer> arrayNumSelected = new ArrayList<Integer>();
	// list product not in
	ArrayList<OrderDetailViewDTO> listBuyOrders = new ArrayList<OrderDetailViewDTO>();

	// current customer id
	public String currentCustomerId;
	// customer type id
	public int customerTypeId;
	// check load data
	boolean isLoadData = false;

	// dialog product detail view
	AlertDialog alertProductDetail;
	boolean isFirstShowSelectPrograme = false;
	// dialog product detail view
	AlertDialog alertPromotionDetail;
	// table list programe
	VinamilkTableView tbProgrameList;
	// list ProgrameForProductDTO
	List<ProgrameForProductDTO> listPrograme = new ArrayList<ProgrameForProductDTO>();
	FindProductSaleOrderDetailViewDTO currentObjectClick = new FindProductSaleOrderDetailViewDTO();

	// product code keConstants.STR_BLANKword to search
	private String productCodeKW = Constants.STR_BLANK;
	// product name key word to search
	private String productNameKW = Constants.STR_BLANK;
	// // progame code key word to search
	// private String programeCodeKW = Constants.STR_BLANK;
	// flag check click update on header menu
	private boolean isUpdate = false;
	// type staff
	private int staffType = UserDTO.TYPE_PRESALES;
	private String orderType = SALE_ORDER_TABLE.ORDER_TYPE_PRESALE;
	/**
	 * control for popup promotion detail
	 */
	PromotionProgrameDetailView promotionDetailView;

	/**
	 * method get instance
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: FindProductAddOrderListView
	 * @throws:
	 */
	public static FindProductAddOrderListView getInstance(Bundle data) {
		FindProductAddOrderListView instance = new FindProductAddOrderListView();
		instance.setArguments(data);
		instance.VIEW_NAME = StringUtil.getString(R.string.ADD_ORDER);
		return instance;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_find_product_add_orderslist_view, container,
				false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		if (getArguments().getSerializable(
				IntentConstants.INTENT_LIST_PRODUCT_NOT_IN) != null) {
			listBuyOrders = (ArrayList<OrderDetailViewDTO>) getArguments()
					.getSerializable(IntentConstants.INTENT_LIST_PRODUCT_NOT_IN);
		}
		if (getArguments().getString(IntentConstants.INTENT_CUSTOMER_ID) != null) {
			this.currentCustomerId = getArguments().getString(
					IntentConstants.INTENT_CUSTOMER_ID);
		}
		if (getArguments().getInt(IntentConstants.INTENT_STAFF_TYPE) >= 0) {
			this.staffType = getArguments().getInt(
					IntentConstants.INTENT_STAFF_TYPE);
		}
		if (getArguments().getInt(IntentConstants.INTENT_ORDER_TYPE) >= 0) {
			this.orderType = getArguments().getString(
					IntentConstants.INTENT_ORDER_TYPE);
		}
		if (getArguments().getInt(IntentConstants.INTENT_CUSTOMER_TYPE_ID) >= 0) {
			this.customerTypeId = getArguments().getInt(
					IntentConstants.INTENT_CUSTOMER_TYPE_ID);
		}
		initView(v);
		// renderLayout();
		// enableMenuBar(this);
		// addMenuItem(R.drawable.icon_menu_payment_homephone, BACK_ACTION);
		setTitleHeaderView(getString(R.string.TEXT_HEADER_TITLE_FIND_PRODUCT_ADD_ORDERLIST));
		if (!this.isLoadData) {
			// request get list product add to order list
			isFirstLoadProduct = true;
			getInitListProductAddOrder();
			// getListProductAddOrder(0, this.productCodeKW, this.productNameKW,
			// this.programeCodeKW, true);
		} else {
			this.renderLayout();
		}

		return v;
	}

	/**
	 * 
	 * get list product add order
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	private void getListProductAddOrder(int numPage, String productCodeKeyWord,
			String productNameKeyWord, boolean isGetCount) {
		if (!this.parent.isShowProgressDialog()) {
			this.parent.showProgressDialog(getString(R.string.loading));
		}
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		String page = " limit " + (numPage * LIMIT_ROW_PER_PAGE) + ","
				+ LIMIT_ROW_PER_PAGE;
		this.productCodeKW = productCodeKeyWord.trim();
		this.productNameKW = StringUtil
				.getEngStringFromUnicodeString(productNameKeyWord.trim());

		data.putString(IntentConstants.INTENT_PAGE, page);
		data.putString(IntentConstants.INTENT_CUSTOMER_ID, currentCustomerId);

		if (!StringUtil.isNullOrEmpty(this.productCodeKW)) {
			data.putString(IntentConstants.INTENT_PRODUCT_CODE,
					this.productCodeKW);
		}
		if (!StringUtil.isNullOrEmpty(this.productNameKW)) {
			data.putString(IntentConstants.INTENT_PRODUCT_NAME,
					this.productNameKW);
		}
		data.putString(IntentConstants.INTENT_SALE_TYPE_CODE, GlobalInfo
				.getInstance().getProfile().getUserData().saleTypeCode);
		data.putString(IntentConstants.INTENT_CUSTOMER_TYPE_ID,
				String.valueOf(this.customerTypeId));
		data.putInt(IntentConstants.INTENT_STAFF_TYPE, this.staffType);
		data.putString(IntentConstants.INTENT_ORDER_TYPE, this.orderType);

		data.putString(
				IntentConstants.INTENT_SHOP_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile()
						.getUserData().shopId));

		// list product don't get
		if (orgListDTO != null && orgListDTO.listObject != null
				&& orgListDTO.listObject.size() > 0) {
			String strProductID = Constants.STR_BLANK;
			for (int i = 0, size = orgListDTO.listObject.size(); i < size; i++) {
				if (orgListDTO.listObject.get(i).typeObject == FindProductSaleOrderDetailViewDTO.TYPE_OBJECT_SAVE_FILTER) {
					FindProductSaleOrderDetailViewDTO product = orgListDTO.listObject
							.get(i);
					strProductID += "'" + product.productCode + "'";
					strProductID += ",";
				}
			}
			if (strProductID.length() > 1) {
				strProductID = strProductID.substring(0,
						strProductID.length() - 1);
			}
			data.putString(IntentConstants.INTENT_LIST_PRODUCT_NOT_IN,
					strProductID);
		}

		data.putBoolean(IntentConstants.INTENT_IS_GET_NUM_TOTAL_ITEM,
				isGetCount);
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_PRODUCTS_TO_ADD_ORDER;
		e.isNeedCheckTimeServer = false;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * 
	 * khoi tao 3 textview autocomplete
	 * 
	 * @author: HieuNH6
	 * @return: void
	 * @throws:
	 */
	private void getInitListProductAddOrder() {
		this.parent.showProgressDialog(getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		String page = Constants.STR_BLANK;
		this.productCodeKW = Constants.STR_BLANK;
		this.productNameKW = Constants.STR_BLANK;
		// this.programeCodeKW = Constants.STR_BLANK;

		data.putString(IntentConstants.INTENT_PAGE, page);
		data.putString(IntentConstants.INTENT_CUSTOMER_ID, currentCustomerId);

		if (!StringUtil.isNullOrEmpty(this.productCodeKW)) {
			data.putString(IntentConstants.INTENT_PRODUCT_CODE,
					this.productCodeKW);
		}
		if (!StringUtil.isNullOrEmpty(this.productNameKW)) {
			data.putString(IntentConstants.INTENT_PRODUCT_NAME,
					this.productNameKW);
		}
		// if (!StringUtil.isNullOrEmpty(this.programeCodeKW)) {
		// data.putString(IntentConstants.INTENT_CTKM_CODE,
		// this.programeCodeKW);
		// }

		data.putString(
				IntentConstants.INTENT_SHOP_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile()
						.getUserData().shopId));

		// list product don't get
		if (orgListDTO != null && orgListDTO.listObject != null
				&& orgListDTO.listObject.size() > 0) {
			String strProductID = Constants.STR_BLANK;
			for (int i = 0, size = orgListDTO.listObject.size(); i < size; i++) {
				if (orgListDTO.listObject.get(i).typeObject == FindProductSaleOrderDetailViewDTO.TYPE_OBJECT_SAVE_FILTER) {
					FindProductSaleOrderDetailViewDTO product = orgListDTO.listObject
							.get(i);
					strProductID += "'" + product.productCode + "'";
					strProductID += ",";
				}
			}
			if (strProductID.length() > 1) {
				strProductID = strProductID.substring(0,
						strProductID.length() - 1);
			}
			data.putString(IntentConstants.INTENT_LIST_PRODUCT_NOT_IN,
					strProductID);
		}

		data.putBoolean(IntentConstants.INTENT_IS_GET_NUM_TOTAL_ITEM, false);
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_INIT_LIST_PRODUCT_ADD_TO_ORDER_VIEW;
		e.isNeedCheckTimeServer = false;
		SaleController.getInstance().handleViewEvent(e);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			parent = (SalesPersonActivity) activity;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

	}

	/**
	 * Init view for screen
	 * 
	 * @author: HaiTC3
	 * @param v
	 * @return: void
	 * @throws:
	 */
	public void initView(View v) {
		tbProductOrderList = (VinamilkTableView) v
				.findViewById(R.id.tbProductOrderList);
		tbProductOrderList.setListener(this);
		tbProductOrderList.setNumItemsPage(LIMIT_ROW_PER_PAGE);
		tvNumberProduct = (TextView) v.findViewById(R.id.tvNumberProduct);
		btClose = (Button) v.findViewById(R.id.btClose);
		btClose.setOnClickListener(this);
		btSearch = (Button) v.findViewById(R.id.btSearch);
		btSearch.setOnClickListener(this);
		btClearAllInput = (Button) v.findViewById(R.id.btClearAllInput);
		btClearAllInput.setOnClickListener(this);
		btChoose = (Button) v.findViewById(R.id.btChoose);
		btChoose.setOnClickListener(this);
		etInputOrderCode = (VNMEditTextClearable) v
				.findViewById(R.id.etInputOrderCode);
		etInputOrderCode.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				searchProductFlowFillter();
			}
		});
		etInputOrderName = (VNMEditTextClearable) v
				.findViewById(R.id.etInputOrderName);
		etInputOrderName.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				searchProductFlowFillter();
			}
		});

		// etInputCTKMCode = (VNMEditTextClearable)
		// v.findViewById(R.id.etInputCTKMCode);
	}

	/**
	 * 
	 * handle search product after user click product code / product name from
	 * list combox textview
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 8, 2013
	 */
	private void searchProductFlowFillter() {
		GlobalUtil.forceHideKeyboardUseToggle(parent);

		// update object type for all item in list save
		for (int i = 0, size = this.orgListDTO.listObject.size(); i < size; i++) {
			this.orgListDTO.listObject.get(i).typeObject = FindProductSaleOrderDetailViewDTO.TYPE_OBJECT_SAVE_FILTER;
		}
		if (this.saveProductSelected(FindProductSaleOrderDetailViewDTO.TYPE_OBJECT_SAVE_FILTER)) {
			listDTO.listObject.clear();
			isSearchingProduct = true;
			String productCodeKeyWord = etInputOrderCode.getText().toString()
					.trim();
			String productNameKeyWord = etInputOrderName.getText().toString()
					.trim();
			// String programeCodeKeyWord =
			// etInputCTKMCode.getText().toString().trim();
			getListProductAddOrder(0, productCodeKeyWord, productNameKeyWord,
					true);
		}
	}

	/**
	 * render layout for screen after get data from db
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		int pos = 1;
		int numberItem = 0;
		if (listDTO.listObject.size() > 0) {
			numberItem = listDTO.totalObject;
			tbProductOrderList.clearAllData();
			tbProductOrderList.hideNoContentRow();
			for (int i = 0, size = listDTO.listObject.size(); i < size; i++) {
				FindProductSaleOrderDetailViewDTO dto = listDTO.listObject
						.get(i);
				FindProductAddOrderListRow row = new FindProductAddOrderListRow(parent, this);
				row.setClickable(true);
				row.setTag(Integer.valueOf(pos));
				// update number order for product on view
				FindProductSaleOrderDetailViewDTO productSelected = this
						.getNumberOrderForProduct(dto);
				if (productSelected != null) {
					dto = productSelected;
					listDTO.listObject.set(i, productSelected);

				}
				if (size == 1) {
					row.renderLayout(
							pos
									+ (tbProductOrderList.getPagingControl()
											.getCurrentPage() - 1)
									* LIMIT_ROW_PER_PAGE, dto, true);
					GlobalUtil.showKeyboardUseToggle(parent);
				} else {
					row.renderLayout(
							pos
									+ (tbProductOrderList.getPagingControl()
											.getCurrentPage() - 1)
									* LIMIT_ROW_PER_PAGE, dto, false);

				}
				pos++;
				tbProductOrderList.addRow(row);
			}
		} else {
			tbProductOrderList.showNoContentRow();
		}

		if (isFirstLoadProduct) {
			// int productNameWidth = 237;
			int[] FIND_PRODUCT_TABLE_WIDTHS = { 50, 110, 280, 95, 130, 130, 130 };
			// new int[] {45, 110, productNameWidth, 95, 90, 60, 95, 106};
			String[] FIND_PRODUCT_TABLE_TITLES = {
					StringUtil.getString(R.string.TEXT_STT),
					StringUtil.getString(R.string.TEXT_COLUM_ORDER_CODE),
					StringUtil.getString(R.string.TEXT_COLUM_ORDER_NAME),
					StringUtil.getString(R.string.TEXT_UNIT),
					StringUtil.getString(R.string.TEXT_COLUM_INVENTORY),
					StringUtil.getString(R.string.TEXT_COLUM_PRICE),
					StringUtil.getString(R.string.TEXT_NUMBER_PRODUCT) };
			// Khong hien thi gia
			if (GlobalInfo.getInstance().getIsShowPrice() == 0) {
				FIND_PRODUCT_TABLE_WIDTHS = new int[] { 50, 110, 289 + 95, 95,
						130, 130, };
				// new int[] {45, 110, productNameWidth, 95, 90, 60, 95, 106};
				FIND_PRODUCT_TABLE_TITLES = new String[] {
						StringUtil.getString(R.string.TEXT_STT),
						StringUtil.getString(R.string.TEXT_COLUM_ORDER_CODE),
						StringUtil.getString(R.string.TEXT_COLUM_ORDER_NAME),
						StringUtil.getString(R.string.TEXT_UNIT),
						StringUtil.getString(R.string.TEXT_COLUM_INVENTORY),
						StringUtil.getString(R.string.TEXT_NUMBER_PRODUCT) };
			}

			tbProductOrderList.getHeaderView().addColumns(
					FIND_PRODUCT_TABLE_WIDTHS,
					FIND_PRODUCT_TABLE_TITLES,
					ImageUtil.getColor(R.color.BLACK),
					ImageUtil.getColor(R.color.TABLE_HEADER_BG));

			// update textView number item
			SpannableObject obj = new SpannableObject();
			obj.addSpan(getString(R.string.TEXT_NOTIFY_TOTAL) + " ",
					ImageUtil.getColor(R.color.BLACK),
					android.graphics.Typeface.NORMAL);
			obj.addSpan(String.valueOf(numberItem),
					ImageUtil.getColor(R.color.RED),
					android.graphics.Typeface.BOLD);
			obj.addSpan(" " + getString(R.string.TEXT_NOTIFY_PRODUCT),
					ImageUtil.getColor(R.color.BLACK),
					android.graphics.Typeface.NORMAL);
			tvNumberProduct.setText(obj.getSpan());
		} else if (isSearchingProduct) {
			SpannableObject obj = new SpannableObject();
			obj.addSpan(getString(R.string.TEXT_NOTIFY_TOTAL) + " ",
					ImageUtil.getColor(R.color.BLACK),
					android.graphics.Typeface.NORMAL);
			obj.addSpan(String.valueOf(numberItem),
					ImageUtil.getColor(R.color.RED),
					android.graphics.Typeface.BOLD);
			obj.addSpan(" " + getString(R.string.TEXT_NOTIFY_PRODUCT),
					ImageUtil.getColor(R.color.BLACK),
					android.graphics.Typeface.NORMAL);
			tvNumberProduct.setText(obj.getSpan());
		}

		tvNumberProduct.setVisibility(View.VISIBLE);

	}

	/**
	 * 
	 * init cho textview de autoComplete
	 * 
	 * @author: HieuNH6
	 * @param list
	 * @return: void
	 * @throws:
	 */
	private void initAutoCompleteTextView(
			AutoCompleteFindProductSaleOrderDetailViewDTO list) {
		customAutoCompleteAdapter customAutoCompleteFilterProductCode = new customAutoCompleteAdapter(
				parent, R.layout.custom_dropdown_item_1line,
				list.strInputOrderCode, null,
				customAutoCompleteAdapter.FILTER_CODE);
		etInputOrderCode.setAdapter(customAutoCompleteFilterProductCode);
		// etInputOrderCode.setDropDownHeight(GlobalUtil.dip2Pixel(300));
		etInputOrderCode.setThreshold(1);

		customAutoCompleteAdapter customAutoCompleteFilterProductName = new customAutoCompleteAdapter(
				parent, R.layout.custom_dropdown_item_1line,
				list.strInputOrderName, list.strInputOrderNameTextUnSigned,
				customAutoCompleteAdapter.FILTER_NAME);
		etInputOrderName.setAdapter(customAutoCompleteFilterProductName);
		etInputOrderName.setThreshold(1);
	}

	public class customAutoCompleteAdapter extends ArrayAdapter<String> {

		public static final int FILTER_CODE = 0;
		public static final int FILTER_NAME = 1;
		public ArrayList<String> allItems;
		// su dung cho trong hop filter ten khong dau
		public ArrayList<String> allItemsNoSinged = new ArrayList<String>();
		public ArrayList<String> items;
		public ArrayList<String> arrayListFilter = new ArrayList<String>();

		// 0: input product code,
		// 1: input product name
		public int typeInput = -1;

		/**
		 * @param context
		 * @param textViewResourceId
		 * @param strInputData
		 */
		@SuppressWarnings("unchecked")
		public customAutoCompleteAdapter(Context context,
				int textViewResourceId, ArrayList<String> strInputData,
				ArrayList<String> strInputDataNoSigend, int type) {
			super(context, textViewResourceId, strInputData);
			this.items = strInputData;
			allItems = (ArrayList<String>) this.items.clone();
			typeInput = type;
			if (type == FILTER_NAME) {
				allItemsNoSinged = strInputDataNoSigend;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (typeInput == FILTER_CODE) {
				TextView view = (TextView) super.getView(position, convertView,
						parent);
				String textOfRow = view.getText().toString().trim();
				String textInput = Constants.STR_BLANK;
				if (!StringUtil.isNullOrEmpty(etInputOrderCode.getText()
						.toString())) {
					textInput = etInputOrderCode.getText().toString().trim()
							.toUpperCase();
				}
				if (!StringUtil.isNullOrEmpty(textInput)
						&& !StringUtil.isNullOrEmpty(textOfRow)) {
					int index = textOfRow.toUpperCase().indexOf(textInput);
					if (index >= 0) {
						SpannableObject obj = new SpannableObject();
						int idBegin = textOfRow.toUpperCase()
								.indexOf(textInput);
						int idEnd = idBegin + textInput.length();
						if (idBegin == 0) {
							obj.addSpan(textOfRow.substring(idBegin, idEnd),
									ImageUtil.getColor(R.color.BLACK),
									android.graphics.Typeface.BOLD);
							if (idEnd < textOfRow.length()) {
								obj.addSpan(textOfRow.substring(idEnd),
										ImageUtil.getColor(R.color.BLACK),
										android.graphics.Typeface.NORMAL);
							}
						} else {
							obj.addSpan(textOfRow.substring(0, idBegin),
									ImageUtil.getColor(R.color.BLACK),
									android.graphics.Typeface.NORMAL);
							if (idBegin < textOfRow.length()) {
								obj.addSpan(
										textOfRow.substring(idBegin, idEnd),
										ImageUtil.getColor(R.color.BLACK),
										android.graphics.Typeface.BOLD);
								if (idEnd < textOfRow.length()) {
									obj.addSpan(textOfRow.substring(idEnd),
											ImageUtil.getColor(R.color.BLACK),
											android.graphics.Typeface.NORMAL);
								}
							}
						}
						view.setText(obj.getSpan());
					} else {
						return super.getView(position, convertView, parent);
					}
				} else {
					return super.getView(position, convertView, parent);
				}
				return view;
			} else {
				return super.getView(position, convertView, parent);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.ArrayAdapter#getFilter()
		 */
		@Override
		public Filter getFilter() {
			return myFilter;
		}

		Filter myFilter = new Filter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * android.widget.Filter#convertResultToString(java.lang.Object)
			 */
			@Override
			public CharSequence convertResultToString(Object resultValue) {
				// TODO Auto-generated method stub
				return super.convertResultToString(resultValue);
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				// TODO Auto-generated method stub
				if (results != null && results.count > 0) {
					items.clear();
					// allItemsNoSingedFilter.clear();
					for (int i = 0, size = ((ArrayList<String>) results.values)
							.size(); i < size; i++) {
						items.add(((ArrayList<String>) results.values).get(i));
					}
					// for (int i = 0, size = allItemsNoSingedFilterTMP.size();
					// i < size; i++) {
					// allItemsNoSingedFilter.add(allItemsNoSingedFilterTMP.get(i));
					// }
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				// TODO Auto-generated method stub
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					arrayListFilter.clear();
					// allItemsNoSingedFilterTMP.clear();
					if (typeInput == FILTER_CODE) {
						for (int i = 0, size = allItems.size(); i < size; i++) {
							if (allItems
									.get(i)
									.toUpperCase()
									.indexOf(
											String.valueOf(constraint)
													.toUpperCase()) != -1) {
								arrayListFilter.add(allItems.get(i));
							}
						}
					} else if (typeInput == FILTER_NAME) {
						for (int i = 0, size = allItemsNoSinged.size(); i < size; i++) {
							if (!StringUtil.isNullOrEmpty(allItemsNoSinged
									.get(i))) {
								if (allItemsNoSinged
										.get(i)
										.toUpperCase()
										.indexOf(
												String.valueOf(constraint)
														.toUpperCase()) != -1) {
									arrayListFilter.add(allItems.get(i));
									// allItemsNoSingedFilterTMP.add(allItemsNoSinged.get(i));
								}
							}
						}
					}
					filterResults.values = arrayListFilter;
					filterResults.count = arrayListFilter.size();
				}
				return filterResults;
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent actionEvent = modelEvent.getActionEvent();
		switch (actionEvent.action) {
		case ActionEventConstant.GET_INIT_LIST_PRODUCT_ADD_TO_ORDER_VIEW:
			AutoCompleteFindProductSaleOrderDetailViewDTO dto = (AutoCompleteFindProductSaleOrderDetailViewDTO) modelEvent
					.getModelData();
			if (dto != null) {
				initAutoCompleteTextView(dto);
				getListProductAddOrder(0, this.productCodeKW,
						this.productNameKW, true);
			}
			break;
		case ActionEventConstant.GET_PRODUCTS_TO_ADD_ORDER:
			ListFindProductSaleOrderDetailViewDTO list = (ListFindProductSaleOrderDetailViewDTO) modelEvent
					.getModelData();

			if (list != null) {
				listDTO = list;
				this.isLoadData = true;
			}
			// paging
			if (listDTO.listObject.size() > 0) {
				tbProductOrderList.getPagingControl().setVisibility(
						View.VISIBLE);
				if (tbProductOrderList.getPagingControl().totalPage < 0
						|| isSearchingProduct) {
					tbProductOrderList.setTotalSize(listDTO.totalObject);
					tbProductOrderList.getPagingControl().setCurrentPage(1);
				}
			} else {
				tbProductOrderList.getPagingControl().setVisibility(View.GONE);
			}

			renderLayout();
			if (isSearchingProduct) {
				isSearchingProduct = false;
			}
			if (isFirstLoadProduct) {
				isFirstLoadProduct = false;
			}
			this.parent.closeProgressDialog();
			break;
		case ActionEventConstant.GET_LIST_PROGRAME_FOR_PRODUCT: {
			List<ProgrameForProductDTO> listObject = (List<ProgrameForProductDTO>) modelEvent
					.getModelData();
			if (listObject != null) {
				this.listPrograme = listObject;
				this.showSelectProgrameFlowProduct();
				if (isFirstShowSelectPrograme) {
					isFirstShowSelectPrograme = false;
				}
			}
			this.parent.closeProgressDialog();
			break;
		}
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	/**
	 * 
	 * validate data num product input
	 * 
	 * @author: HaiTC3
	 * @param productData
	 * @param numberProduct
	 * @return
	 * @return: boolean
	 * @throws:
	 */
	public boolean validateDataProductInput(
			FindProductSaleOrderDetailViewDTO productData, String numberProduct) {
		boolean kq = false;
		if (productData.hasSelectPrograme) {
			if (StringUtil.isNullOrEmpty(numberProduct)) {
				kq = false;
			} else {
				if (numberProduct.equals("0")) {
					kq = false;
				} else {
					kq = StringUtil.isValidateNumProductInput(numberProduct);
				}
			}
		} else {
			if (!StringUtil.isNullOrEmpty(numberProduct)) {
				kq = StringUtil.isValidateNumProductInput(numberProduct);
			} else {
				kq = true;
			}
		}
		return kq;
	}

	/**
	 * 
	 * save list product we selected and number order for each product
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public boolean saveProductSelected(int typeObjectSave) {
		boolean check = true;
		if (listDTO != null && listDTO.listObject != null
				&& listDTO.listObject.size() > 0) {
			for (int i = 0, size = tbProductOrderList.getListRowView().size(); i < size; i++) {
				FindProductAddOrderListRow row = (FindProductAddOrderListRow) tbProductOrderList
						.getListRowView().get(i);
				String numberProduct = row.etNumber.getText().toString();
				if (!this.validateDataProductInput(listDTO.listObject.get(i),
						numberProduct)) {
					GlobalUtil
							.showDialogConfirm(
									this.parent,
									getString(R.string.TEXT_NOTIFY_INPUT_NUM_PRODUCT_INCORRECT),
									getString(R.string.TEXT_BUTTON_CLOSE), 0,
									Constants.STR_BLANK, 0, null);

					// Builder build = new AlertDialog.Builder(parent);
					// AlertDialog alertProductDetail = build.create();
					// alertProductDetail
					// .setMessage(getString(R.string.TEXT_NOTIFY_INPUT_NUM_PRODUCT_INCORRECT));
					// alertProductDetail.setCancelable(true);
					// alertProductDetail.show();
					check = false;
					break;
				}
			}
			if (check) {
				for (int i = 0, size = tbProductOrderList.getListRowView()
						.size(); i < size; i++) {
					FindProductAddOrderListRow row = (FindProductAddOrderListRow) tbProductOrderList
							.getListRowView().get(i);
					String numberProduct = row.etNumber.getText().toString();
					String suggestedPrice = row.etPrice.getText().toString().trim().replace(",", "");
					if (this.validateDataProductInput(
							listDTO.listObject.get(i), numberProduct)) {
						if ((!StringUtil.isNullOrEmpty(numberProduct) && !numberProduct
								.equals("0"))
								|| listDTO.listObject.get(i).hasSelectPrograme) {
							if (!this
									.isExitsProductInListSelected(listDTO.listObject
											.get(i))) {
								if (StringUtil.isNullOrEmpty(numberProduct)) {
									listDTO.listObject.get(i).numProduct = "0";
								} else {
									listDTO.listObject.get(i).numProduct = numberProduct;
								}
								
								//price
								listDTO.listObject.get(i).suggestedPrice = suggestedPrice;
								if(StringUtil.isNullOrEmpty(suggestedPrice)) {
									listDTO.listObject.get(i).saleOrderDetail.price = 0;
								} else {
									listDTO.listObject.get(i).saleOrderDetail.price = Long.parseLong(suggestedPrice.trim());
								}
								
								listDTO.listObject.get(i).typeObject = typeObjectSave;
								orgListDTO.listObject.add(listDTO.listObject
										.get(i));
							} else {
								if (this.getIndexProductInProductListSelected(listDTO.listObject
										.get(i)) >= 0) {

									// update value number product for org list
									// dto
									int index = getIndexProductInProductListSelected(listDTO.listObject
											.get(i));
									if (StringUtil.isNullOrEmpty(numberProduct)) {
										listDTO.listObject.get(i).numProduct = "0";
									} else {
										listDTO.listObject.get(i).numProduct = numberProduct;
									}
									
									//price
									listDTO.listObject.get(i).suggestedPrice = suggestedPrice;
									if(StringUtil.isNullOrEmpty(suggestedPrice)) {
										listDTO.listObject.get(i).saleOrderDetail.price = 0;
									} else {
										listDTO.listObject.get(i).saleOrderDetail.price = Long.parseLong(suggestedPrice.trim());
									}
									
									listDTO.listObject.get(i).typeObject = typeObjectSave;
									orgListDTO.listObject.set(index,
											listDTO.listObject.get(i));
								}
							}
						} else {
							if (this.isExitsProductInListSelected(listDTO.listObject
									.get(i))) {
								if (this.getIndexProductInProductListSelected(listDTO.listObject
										.get(i)) >= 0) {
									int index = this
											.getIndexProductInProductListSelected(listDTO.listObject
													.get(i));
									orgListDTO.listObject.remove(index);
								}
							}
						}
					} else {

					}
				}
			}
		}
		return check;
	}

	/**
	 * check saleOrderDetailDTO exits in list product selected
	 * 
	 * @author: HaiTC3
	 * @param currentProduct
	 * @return
	 * @return: boolean
	 * @throws:
	 */
	public boolean isExitsProductInListSelected(
			FindProductSaleOrderDetailViewDTO currentProduct) {
		boolean kq = false;
		if (orgListDTO != null) {
			for (int i = 0, size = orgListDTO.listObject.size(); i < size; i++) {
				FindProductSaleOrderDetailViewDTO selectedProduct = orgListDTO.listObject
						.get(i);
				if (currentProduct.saleOrderDetail.productId == selectedProduct.saleOrderDetail.productId) {
					kq = true;
					break;
				}
			}
		}
		return kq;
	}

	/**
	 * get number order for product
	 * 
	 * @author: HaiTC3
	 * @param product
	 * @return
	 * @return: int (0: none selected or order = 0)
	 * @throws:
	 */
	public FindProductSaleOrderDetailViewDTO getNumberOrderForProduct(
			FindProductSaleOrderDetailViewDTO product) {
		for (int i = 0, size = orgListDTO.listObject.size(); i < size; i++) {
			FindProductSaleOrderDetailViewDTO productSelected = orgListDTO.listObject
					.get(i);
			if (product.saleOrderDetail.productId == productSelected.saleOrderDetail.productId) {
				return productSelected;
			}
		}
		return null;
	}

	/**
	 * get index of current product in list product selected
	 * 
	 * @author: HaiTC3
	 * @param product
	 * @return
	 * @return: int
	 * @throws:
	 */
	public int getIndexProductInProductListSelected(
			FindProductSaleOrderDetailViewDTO product) {
		int kq = -1;
		if (this.isExitsProductInListSelected(product)) {
			for (int i = 0, size = orgListDTO.listObject.size(); i < size; i++) {
				FindProductSaleOrderDetailViewDTO selectedProduct = orgListDTO.listObject
						.get(i);
				if (product.saleOrderDetail.productId == selectedProduct.saleOrderDetail.productId) {
					kq = i;
					break;
				}
			}
		}
		return kq;
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent actionEvent = modelEvent.getActionEvent();
		switch (actionEvent.action) {
		case ActionEventConstant.GET_PRODUCTS_TO_ADD_ORDER:
			listDTO = new ListFindProductSaleOrderDetailViewDTO();
			orgListDTO = new ListFindProductSaleOrderDetailViewDTO();
			this.renderLayout();
			this.parent.closeProgressDialog();
			break;
		case ActionEventConstant.GET_LIST_PROGRAME_FOR_PRODUCT:
			this.parent.closeProgressDialog();
			break;
		case ActionEventConstant.GET_INIT_LIST_PRODUCT_ADD_TO_ORDER_VIEW:
			this.parent.closeProgressDialog();
			break;
		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub

		super.onEvent(eventType, control, data);

	}

	@Override
	public void onClick(View v) {
		if (v == btClose) {
			GlobalUtil.forceHideKeyboardUseToggle(parent);
			parent.onBackPressed();
			VTLog.v("close view", "message close view");
			// this.gotoPaymentOrderBilling();
		} else if (v == btSearch) {
			this.searchProductFlowFillter();
		} else if (v == btChoose) {
			GlobalUtil.forceHideKeyboardUseToggle(parent);
			sendProductToOrderList();
		} else if (v == btClosePopupPrograme) {
			if (alertProductDetail.isShowing()) {
				alertProductDetail.dismiss();
			}
		} else if (v == btResetChoosePrograme) {
			this.resetSelectProgrameForProduct();
			if (alertProductDetail.isShowing()) {
				alertProductDetail.dismiss();
			}
		} else if (v == btClearAllInput) {
			// etInputCTKMCode.setText(Constants.STR_BLANK);
			etInputOrderCode.setText(Constants.STR_BLANK);
			etInputOrderName.setText(Constants.STR_BLANK);
			// mac dinh focus vao ma mat hang
			// etInputCTKMCode.clearFocus();
			etInputOrderName.clearFocus();
			etInputOrderCode.requestFocus();
			GlobalUtil.showKeyboardUseToggle(parent);
		} else {
			GlobalUtil.forceHideKeyboardUseToggle(parent);
			super.onClick(v);
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub
		if (control == tbProductOrderList) {
			// load more data for table product list
			if (this.saveProductSelected(FindProductSaleOrderDetailViewDTO.TYPE_OBJECT_SAVE_NOT_FILTER)) {
				getListProductAddOrder((tbProductOrderList.getPagingControl()
						.getCurrentPage() - 1), this.productCodeKW,
						this.productNameKW, false);
			} else {
				this.tbProductOrderList.getPagingControl()
						.setCurrentPage(
								this.tbProductOrderList.getPagingControl()
										.getOldPage());
			}
		}
	}

	/**
	 * 
	 * send all product slected to order list screen
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void sendProductToOrderList() {
		// send all product to order list
		if (this.saveProductSelected(FindProductSaleOrderDetailViewDTO.TYPE_OBJECT_SAVE_FILTER)) {
			// tinh toan lai trong luong cua san pham theo so luong san pham da
			// chon
			for (int i = 0, size = orgListDTO.listObject.size(); i < size; i++) {
				orgListDTO.listObject.get(i).saleOrderDetail.totalWeight = orgListDTO.listObject
						.get(i).grossWeight
						* GlobalUtil.calRealOrder(
								orgListDTO.listObject.get(i).numProduct,
								orgListDTO.listObject.get(i).convfact);
			}
			Bundle data = new Bundle();
			data.putSerializable(
					IntentConstants.INTENT_PRODUCTS_ADD_ORDER_LIST,
					(Serializable) orgListDTO);
			// sendBroadcast(ActionEventConstant.BROADCAST_CHOOSE_PRODUCT_ADD_ORDER_LIST,
			// data);

			OrderView orderFragment = (OrderView) getActivity()
					.getFragmentManager().findFragmentByTag(OrderView.TAG);
			if (orderFragment != null) {
				orderFragment
						.receiveBroadcast(
								ActionEventConstant.BROADCAST_CHOOSE_PRODUCT_ADD_ORDER_LIST,
								data);
			}
			GlobalUtil.popBackStack(this.getActivity());
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		// TODO Auto-generated method stub
		if (control == tbProductOrderList) {
			switch (action) {
			case ActionEventConstant.ROW_CLICK:
				// click row of table
				break;
			case ActionEventConstant.GO_TO_PROMOTION_PROGRAME_DETAIL: {
				FindProductSaleOrderDetailViewDTO dto = (FindProductSaleOrderDetailViewDTO) data;
				this.requestGetPromotionDetail(dto.promotionProgrameCode);
				break;
			}
			case ActionEventConstant.GO_TO_CT_DETAIL: {
				if (this.listPrograme != null && this.listPrograme.size() > 0
						&& !isUpdate) {
					currentObjectClick = (FindProductSaleOrderDetailViewDTO) data;
					this.showSelectProgrameFlowProduct();
				} else {
					if (isUpdate) {
						isUpdate = false;
					}
					currentObjectClick = (FindProductSaleOrderDetailViewDTO) data;
					getListProgrameFlowProduct((FindProductSaleOrderDetailViewDTO) data);
				}
				break;
			}
			default:
				break;
			}
		} else if (control == tbProgrameList) {
			switch (action) {
			case ActionEventConstant.ACTION_SELECT_PROGRAME:
				alertProductDetail.dismiss();
				if (data != null) {
					updateSelectProgrameForProduct((ProgrameForProductDTO) data);
				} else {
					this.resetSelectProgrameForProduct();
				}
				break;

			default:
				break;
			}
		}
	}

	/**
	 * cap nhat trang thai san pham co chon chuong trinh khuyen mai hoac chuong
	 * trinh trung bay
	 * 
	 * @author: HaiTC3
	 * @param dto
	 * @return: void
	 * @throws:
	 */
	public void updateSelectProgrameForProduct(ProgrameForProductDTO dto) {
		FindProductSaleOrderDetailViewDTO objectData = new FindProductSaleOrderDetailViewDTO();
		for (int i = 0, size = listDTO.listObject.size(); i < size; i++) {
			objectData = listDTO.listObject.get(i);
			if (objectData.saleOrderDetail.productId == currentObjectClick.saleOrderDetail.productId) {
				objectData.saleOrderDetail.programeType = dto.programeType;
				objectData.saleOrderDetail.programeCode = dto.programeCode;
				objectData.hasSelectPrograme = true;
				break;
			}
		}

		for (int i = 0, size = tbProductOrderList.getListRowView().size(); i < size; i++) {
			FindProductAddOrderListRow row = (FindProductAddOrderListRow) tbProductOrderList
					.getListRowView().get(i);
			if (row.myData.saleOrderDetail.productId == currentObjectClick.saleOrderDetail.productId) {
				row.updateLayout(objectData);
				break;
			}
		}
	}

	/**
	 * 
	 * reset select programe for product
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void resetSelectProgrameForProduct() {
		FindProductSaleOrderDetailViewDTO objectData = new FindProductSaleOrderDetailViewDTO();
		for (int i = 0, size = listDTO.listObject.size(); i < size; i++) {
			objectData = listDTO.listObject.get(i);
			if (objectData.saleOrderDetail.productId == currentObjectClick.saleOrderDetail.productId) {
				objectData.saleOrderDetail.programeType = 0;
				objectData.saleOrderDetail.programeCode = Constants.STR_BLANK;
				objectData.hasSelectPrograme = false;
				break;
			}
		}

		for (int i = 0, size = tbProductOrderList.getListRowView().size(); i < size; i++) {
			FindProductAddOrderListRow row = (FindProductAddOrderListRow) tbProductOrderList
					.getListRowView().get(i);
			if (row.myData.saleOrderDetail.productId == currentObjectClick.saleOrderDetail.productId) {
				row.updateLayout(objectData);
				break;
			}
		}
	}

	/**
	 * get list programe for product to choose
	 * 
	 * @author: HaiTC3
	 * @param data
	 * @return: void
	 * @throws:
	 */
	private void getListProgrameFlowProduct(
			FindProductSaleOrderDetailViewDTO data) {
		this.parent.showProgressDialog(getString(R.string.loading));
		isFirstShowSelectPrograme = true;
		Bundle dataSend = new Bundle();
		dataSend.putString(IntentConstants.INTENT_CUSTOMER_ID,
				this.currentCustomerId);
		dataSend.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo
				.getInstance().getProfile().getUserData().shopId);
		dataSend.putString(
				IntentConstants.INTENT_STAFF_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile()
						.getUserData().id));
		dataSend.putString(IntentConstants.INTENT_CUSTOMER_TYPE_ID,
				String.valueOf(this.customerTypeId));
		ActionEvent e = new ActionEvent();
		e.viewData = dataSend;
		e.sender = this;
		e.action = ActionEventConstant.GET_LIST_PROGRAME_FOR_PRODUCT;
		e.isNeedCheckTimeServer = false;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * show dialog select programe flow product
	 * 
	 * @author: HaiTC3
	 * @param data
	 * @return: void
	 * @throws:
	 */
	private void showSelectProgrameFlowProduct() {
		if (alertProductDetail == null) {
			Builder build = new AlertDialog.Builder(parent,
					R.style.CustomDialogTheme);
			LayoutInflater inflater = this.parent.getLayoutInflater();
			View view = inflater.inflate(
					R.layout.layout_select_programe_for_product, null);

			tbProgrameList = (VinamilkTableView) view
					.findViewById(R.id.tbProgrameListView);
			btClosePopupPrograme = (Button) view
					.findViewById(R.id.btClosePopupPrograme);
			btClosePopupPrograme.setOnClickListener(this);
			btResetChoosePrograme = (Button) view
					.findViewById(R.id.btResetChoosePrograme);
			btResetChoosePrograme.setOnClickListener(this);

			tbProgrameList.getHeaderView().addColumns(
					TableDefineContanst.SELECT_PROGRAME_TABLE_WIDTHS,
					TableDefineContanst.SELECT_PROGRAME_TABLE_TITLES,
					ImageUtil.getColor(R.color.BLACK),
					ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			tbProgrameList.setNumItemsPage(listPrograme.size() + 1);

			build.setView(view);
			alertProductDetail = build.create();
			// alertProductDetail.setCancelable(false);

			Window window = alertProductDetail.getWindow();
			window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255,
					255, 255)));
			window.setLayout(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
		}
		if (isFirstShowSelectPrograme) {
			List<TableRow> listRows = new ArrayList<TableRow>();
			int pos = 1;
			for (int i = 0, size = listPrograme.size(); i < size; i++) {
				ProgrameForProductDTO dto = listPrograme.get(i);
				SelectProgrameForProduct row = new SelectProgrameForProduct(
						parent, tbProgrameList);
				row.setClickable(true);
				row.setTag(dto);
				row.renderLayout(pos, dto);
				row.setListener(this);
				pos++;
				listRows.add(row);
			}
			String message = Constants.STR_BLANK;
			if (listPrograme.size() == 0) {
				message = getString(R.string.TEXT_NOTIFY_NOT_HAVE_PROGRAME);
				SelectProgrameForProduct row = new SelectProgrameForProduct(
						parent, tbProgrameList);
				row.setClickable(true);
				row.renderLayoutEndRowNotSelectProgrameOrNull(message);
				pos++;
				listRows.add(row);
			}
			tbProgrameList.addContent(listRows);
		}
		alertProductDetail.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.main.BaseFragment#receiveBroadcast(int,
	 * android.os.Bundle)
	 */
	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		// TODO Auto-generated method stub
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				isSearchingProduct = true;
				isUpdate = true;
				this.clearDataOfScreen();
				this.getListProductAddOrder(0, this.productCodeKW,
						this.productNameKW, true);
			}
			break;

		default:
			super.receiveBroadcast(action, extras);
			break;
		}

	}

	/**
	 * 
	 * clear data of screen when click action "update du lieu" on top screen
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void clearDataOfScreen() {
		etInputOrderCode.setText(Constants.STR_BLANK);
		etInputOrderName.setText(Constants.STR_BLANK);
		// etInputCTKMCode.setText(Constants.STR_BLANK);
		this.productCodeKW = Constants.STR_BLANK;
		this.productNameKW = Constants.STR_BLANK;
		// this.programeCodeKW = Constants.STR_BLANK;
		listDTO = new ListFindProductSaleOrderDetailViewDTO();
		orgListDTO = new ListFindProductSaleOrderDetailViewDTO();
		listPrograme.clear();
		if (alertProductDetail != null && alertProductDetail.isShowing()) {
			alertProductDetail.dismiss();
		}
	}
}
