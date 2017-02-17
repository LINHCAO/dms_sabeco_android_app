/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.depot;

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
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.db.ProductDTO;
import com.viettel.dms.dto.view.CategoryCodeDTO;
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
import com.viettel.dms.view.main.SalesPersonActivity;
import com.viettel.sabeco.R;

/**
 * 
 * MH danh sach san pham NVBH
 * 
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.0
 */
public class ProductListView extends BaseFragment implements OnClickListener,
		VinamilkTableListener, OnEventControlListener {

	public static final String TAG = ProductListView.class.getName();

	// input product code
	EditText etInputProductCode;
	// input product name
	EditText etInputProductName;
	Button ibtSearch;
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
	private SalesPersonActivity parent;
	// list product
	ListProductDTO listProductDto;
	// list category
	CategoryCodeDTO listCategory;
	private String textProductCode = Constants.STR_BLANK;// ma sp
	private String textProductName = Constants.STR_BLANK;//ten sp
	private boolean isUpdateData = false;// trang thai update data
	private int curPage = -1;// current page
	private boolean isBack = false;// back

	public static ProductListView newInstance() {
		// if (instance == null) {
		ProductListView instance = new ProductListView();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		instance.setArguments(args);
		// }

		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (SalesPersonActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_list_product_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		initView(v);
		setTitleHeaderView(getString(R.string.TEXT_PRODUCT_LIST));
		return v;
	}

	/**
	 * 
	 * init controls for view
	 * 
	 * @author: YenNTH
	 * @param v
	 * @return: void
	 * @throws:
	 */
	public void initView(View v) {
		// table list product
		tbProductList = (DMSTableView) v.findViewById(R.id.tbProductList);
		tbProductList.setListener(this);
		initHeaderTable(tbProductList, new ProductInfoRow(parent, this));
		etInputProductCode = (EditText) v.findViewById(R.id.etInputProductCode);
		etInputProductName = (EditText) v.findViewById(R.id.etInputProductName);
		// button search
		ibtSearch = (Button) v.findViewById(R.id.ibtSearch);
		ibtSearch.setOnClickListener(this);
		if (listProductDto != null && curPage > 0) {
			renderLayout();
		} else {
			getProductList(1, 1);
		}
	}

	/**
	 * 
	 * request danh sach san pham
	 * 
	 * @author: YenNTH
	 * @param curPage
	 * @param isGetTotolPage
	 * @return: void
	 * @throws:
	 */
	private void getProductList(int curPage, int isGetTotolPage) {
		parent.showLoadingDialog();
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		String page = " limit " + ((curPage - 1) * Constants.NUM_ITEM_PER_PAGE)
				+ "," + Constants.NUM_ITEM_PER_PAGE;
		data.putString(IntentConstants.INTENT_PAGE, page);
		data.putInt(IntentConstants.INTENT_GET_TOTAL_PAGE, isGetTotolPage);
		if (!StringUtil.isNullOrEmpty(textProductCode)) {
			data.putString(IntentConstants.INTENT_PRODUCT_CODE, textProductCode);
		}
		if (!StringUtil.isNullOrEmpty(textProductName)) {
			data.putString(IntentConstants.INTENT_PRODUCT_NAME, textProductName);
		}
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		data.putString(IntentConstants.INTENT_STAFF_ID,String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
		data.putString(IntentConstants.INTENT_SALE_TYPE_CODE, GlobalInfo.getInstance().getProfile().getUserData().saleTypeCode);
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_PRODUCT_LIST;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * 
	 * render layout for list product view
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		// paging
		if (curPage <= 0) {
			tbProductList.setTotalSize(listProductDto.total,1);
			curPage = tbProductList.getPagingControl().getCurrentPage();
		} else {
			if (isBack) {
				isBack = false;
				tbProductList.setTotalSize(listProductDto.total, curPage);
			}
			tbProductList.getPagingControl().setCurrentPage(curPage);
		}
		int pos = 1 + Constants.NUM_ITEM_PER_PAGE * (tbProductList.getPagingControl().getCurrentPage() - 1);
		tbProductList.clearAllData();
		if (listProductDto.producList.size() > 0) {
			for (int i = 0, s = listProductDto.producList.size(); i < s; i++) {
				ProductInfoRow row = new ProductInfoRow(parent, this);
				row.renderLayout(pos, listProductDto.producList.get(i));
				row.tvProductCode.setOnClickListener(this);
				row.tvProductCode.setTag(listProductDto.producList.get(i));
				pos++;
				tbProductList.addRow(row);
			}
		} else {
			tbProductList.showNoContentRow();
		}
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		ActionEvent actionEvent = modelEvent.getActionEvent();
		switch (actionEvent.action) {
		case ActionEventConstant.GET_PRODUCT_LIST:
			ListProductDTO tempDto = (ListProductDTO) modelEvent.getModelData();
			if (listProductDto != null) {
				listProductDto.producList = tempDto.producList;
			} else {
				listProductDto = tempDto;
			}
			if (isUpdateData) {
				isUpdateData = false;
				curPage = -1;
			}
			if (curPage == -1) {
				listProductDto.total = tempDto.total;
			}
			renderLayout();
			requestInsertLogKPI(HashMapKPI.NVBH_DANHSACHSANPHAM, modelEvent.getActionEvent().startTimeFromBoot);
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
		case ActionEventConstant.GET_PRODUCT_LIST: {
			if (listProductDto != null) {
				listProductDto.producList.clear();
			} else {
				listProductDto = new ListProductDTO();
			}
			if (isUpdateData) {
				isUpdateData = false;
				curPage = -1;
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
			// reset dto
			curPage = -1;
			listProductDto = null;
			textProductCode = etInputProductCode.getText().toString().trim();
			textProductName = etInputProductName.getText().toString().trim();
			GlobalUtil.forceHideKeyboard(parent);
			getProductList(1, 1);
		} else if (v.getId() == R.id.tvProductCode) {
			curPage = tbProductList.getPagingControl().getCurrentPage();
			isBack = true;
			ProductDTO dto = (ProductDTO) v.getTag();
			Bundle bundle = new Bundle();
			bundle.putString(IntentConstants.INTENT_PRODUCT_ID, Constants.STR_BLANK + dto.productId);
			bundle.putString(IntentConstants.INTENT_PRODUCT_CODE, Constants.STR_BLANK + dto.productCode);
			bundle.putString(IntentConstants.INTENT_PRODUCT_NAME,dto.productName);
			ActionEvent e = new ActionEvent();
			e.sender = this;
			e.viewData = bundle;
			e.action = ActionEventConstant.GOTO_PRODUCT_INTRODUCTION;
			SaleController.getInstance().handleSwitchFragment(e);
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		curPage = tbProductList.getPagingControl().getCurrentPage();
		getProductList(curPage, 0);
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		default:
			break;
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				// cau request du lieu man hinh
				isUpdateData = true;
				textProductCode = Constants.STR_BLANK;
				textProductName = Constants.STR_BLANK;
				curPage = -1;
				etInputProductCode.setText(textProductCode);
				etInputProductName.setText(textProductName);
				getProductList(1, 1);
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

}
