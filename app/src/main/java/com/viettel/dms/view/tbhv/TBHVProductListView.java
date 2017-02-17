/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.db.ProductDTO;
import com.viettel.dms.dto.view.ListProductDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.TBHVActivity;
import com.viettel.sabeco.R;

/**
 * 
 *  Ds sp cua TBHV
 *  @author: Nguyen Thanh Dung
 *  @version: 1.1
 *  @since: 1.0
 */
public class TBHVProductListView extends BaseFragment implements OnClickListener,
		VinamilkTableListener, OnEventControlListener {

	public static final String TAG = TBHVProductListView.class.getName();
	public static final int ACTION_PROMOTION = 0;
	public static final int ACTION_DISPLAY = 1;
	public static final int ACTION_PRODUCT_LIST = 2;
	
	// input product code
	EditText etInputProductCode;
	// input product name
	EditText etInputProductName;
	Button ibtSearch;
	// text view display number product above table list product
	TextView tvNumberProduct;
	// vinamilk table product list
	DMSTableView tbProductList;
	// button close in dialog
	Button btClose;
	// product code infor
	TextView tvProductCode;
	// product name info
	TextView tvProductName;
	// product promotion info
	TextView tvProductPromotion;
	// product price info
	TextView tvProductPrice;
	// product specification info
	TextView tvProductSpecification;
	// product stock info
	TextView tvProductStock;
	// product total info
	TextView tvProductTotal;
	private TBHVActivity parent;
	// list product
	ListProductDTO listProductDto;
	//luu product code
	private String textProductCode = "";
	//luu product name
	private String textProductName = "";
	//Check kiem tra khi nhan nut cap nhat thi reset phan trag
	private boolean isUpdateData = false;
	//luu trang hien tai
	private int curPage = -1;
	//Kiem tra neu back tu MH chi tiet sp thi layout lai phan trang
	private boolean isBack = false; 

	/**
	 * 
	*  init instance
	*  @author: Nguyen Thanh Dung
	*  @return
	*  @return: TBHVProductListView
	*  @throws:
	 */
	public static TBHVProductListView getInstance() {
		TBHVProductListView instance = new TBHVProductListView();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		instance.setArguments(args);
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (TBHVActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_tbhv_list_product_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);

		// menu bar
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_PRODUCT),R.drawable.icon_product_list, ACTION_PRODUCT_LIST);
//		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_CTHTBH),R.drawable.menu_manage_icon, ACTION_DISPLAY);
//		addMenuItem(StringUtil.getString(R.string.TEXT_CTKM),R.drawable.menu_promotion_icon, ACTION_PROMOTION);
		setMenuItemFocus(1);
		
		initView(v);

		setTitleHeaderView(getString(R.string.TITLE_VIEW_TBHV_PRODUCT_LIST));
		return v;
	}

	/**
	 * 
	*  init controls for view
	*  @author: Nguyen Thanh Dung
	*  @param v
	*  @return: void
	*  @throws:
	 */
	public void initView(View v) {
		// table list product
		tbProductList = (DMSTableView) v.findViewById(R.id.tbProductList);
		tbProductList.setListener(this);
		initHeaderTable(tbProductList, new TBHVProductInfoRow(parent, this));
		etInputProductCode = (EditText) v.findViewById(R.id.etInputProductCode);
		etInputProductName = (EditText) v.findViewById(R.id.etInputProductName);
		
		// text view number product info
		tvNumberProduct = (TextView) v.findViewById(R.id.tvNumberProduct);
		// button search
		ibtSearch = (Button) v.findViewById(R.id.ibtSearch);
		ibtSearch.setOnClickListener(this);
		
		if(listProductDto != null && curPage > 0){
			renderLayout();
		}else{
			getListProduct(1, true);
		}
	}

	/**
	 * 
	*  Gui reqest lay ds san pham
	*  @author: Nguyen Thanh Dung
	*  @param curPage
	*  @param isGetTotolPage
	*  @return: void
	*  @throws:
	 */
	private void getListProduct(int curPage, boolean isGetTotolPage) {
		parent.showLoadingDialog();
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();

		String page = " limit "
				+ ((curPage - 1) * Constants.NUM_ITEM_PER_PAGE)
				+ "," + Constants.NUM_ITEM_PER_PAGE;

		data.putString(IntentConstants.INTENT_PAGE, page);
		data.putBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE, isGetTotolPage);

		if (!StringUtil.isNullOrEmpty(textProductCode)) {
			data.putString(IntentConstants.INTENT_PRODUCT_CODE, textProductCode);
		}
		if (!StringUtil.isNullOrEmpty(textProductName)) {
			data.putString(IntentConstants.INTENT_PRODUCT_NAME, textProductName);
		}
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_TBHV_LIST_PRODUCT_STORAGE;
		TBHVController.getInstance().handleViewEvent(e);
	}

	/**
	 * 
	*  render layout for list product view
	*  @author: Nguyen Thanh Dung
	*  @return: void
	*  @throws:
	 */
	private void renderLayout() {
		// paging
		if(curPage <= 0 ){
			tbProductList.setTotalSize(listProductDto.total,1);
			curPage = tbProductList.getPagingControl().getCurrentPage();
		}else{
			if(isBack ){
				isBack = false;
				tbProductList.setTotalSize(listProductDto.total, curPage);
			}
			tbProductList.getPagingControl().setCurrentPage(curPage);
		}
		int pos = 1 + Constants.NUM_ITEM_PER_PAGE
				* (tbProductList.getPagingControl().getCurrentPage() - 1);
		tbProductList.clearAllData();
		if (listProductDto.producList.size() > 0) {
			for (ProductDTO productDTO: listProductDto.producList) {
				TBHVProductInfoRow row = new TBHVProductInfoRow(parent, this);
				row.renderLayout(pos, productDTO);
				pos++;
				tbProductList.addRow(row);
			}
		}else {
			tbProductList.showNoContentRow();
		}
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		ActionEvent actionEvent = modelEvent.getActionEvent();
		switch (actionEvent.action) {
		case ActionEventConstant.GET_TBHV_LIST_PRODUCT_STORAGE:
			ListProductDTO tempDto = (ListProductDTO) modelEvent.getModelData();
			if(listProductDto != null){
				listProductDto.producList = tempDto.producList;
			}else{
				listProductDto =  tempDto;
			}
			
			if(isUpdateData){
				isUpdateData = false;
				curPage  = -1;
			}
			if(curPage == -1){
				listProductDto.total = tempDto.total;
			}
			renderLayout();
			requestInsertLogKPI(HashMapKPI.GSBH_DANHSACHSANPHAM, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}

	}
	
	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		ActionEvent actionEvent = modelEvent.getActionEvent();
		switch (actionEvent.action) {
			case ActionEventConstant.GET_TBHV_LIST_PRODUCT_STORAGE:{
				if(listProductDto != null){
					listProductDto.producList.clear();
				} else {
					listProductDto = new ListProductDTO();
				}
				
				if(isUpdateData){
					isUpdateData = false;
					curPage  = -1;
				}
				
				renderLayout();
				
				break;
			}
			default: {
				super.handleErrorModelViewEvent(modelEvent);
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v == ibtSearch) {
			//reset dto 
			curPage = -1;
			listProductDto = null;
			
			textProductCode = etInputProductCode.getText().toString().trim();
			textProductName = etInputProductName.getText().toString().trim();
			
			GlobalUtil.forceHideKeyboard(parent);
			getListProduct(1, true);
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		curPage = tbProductList.getPagingControl().getCurrentPage();
		getListProduct(curPage, false);
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		switch (action) {
		case ActionEventConstant.GOTO_PRODUCT_INTRODUCTION:{
			curPage = tbProductList.getPagingControl().getCurrentPage();
			isBack = true;
			ProductDTO productDTO=(ProductDTO) data;
			Bundle bundle = new Bundle();
			bundle.putString(IntentConstants.INTENT_PRODUCT_ID, "" + productDTO.productId);
			bundle.putString(IntentConstants.INTENT_PRODUCT_CODE, "" + productDTO.productCode);
			bundle.putString(IntentConstants.INTENT_PRODUCT_NAME, productDTO.productName);
			ActionEvent e = new ActionEvent();
			e.sender = this;
			e.viewData = bundle;
			e.action = ActionEventConstant.GOTO_PRODUCT_INTRODUCTION;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		}
		default:
			break;
		}
		
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		if(eventType == ACTION_PROMOTION){
			ActionEvent e = new ActionEvent();
			e.sender = this;
			e.viewData = new Bundle();
			e.action = ActionEventConstant.GO_TO_TBHV_PROMOTION_PROGRAM;
			TBHVController.getInstance().handleSwitchFragment(e);
		}else if (eventType == ACTION_DISPLAY) {
			ActionEvent e = new ActionEvent();
			e.sender = this;
			e.viewData = new Bundle();
			e.action = ActionEventConstant.GO_TO_TBHV_SUPPORT_SALE;
			TBHVController.getInstance().handleSwitchFragment(e);
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
	      switch (action) {
	      case ActionEventConstant.NOTIFY_REFRESH_VIEW:
	            if(this.isVisible()){
	                  //cau request du lieu man hinh
	            	isUpdateData = true;
	            	textProductCode = "";
	            	textProductName = "";
	            	curPage = -1;
	            	etInputProductCode.setText(textProductCode);
	            	etInputProductName.setText(textProductName);
	            	getListProduct(1, true);
	            }
	            break;
	      default:
	            super.receiveBroadcast(action, extras);
	            break;
	      }
	}
}
