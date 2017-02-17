/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor.training;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.constants.TableDefineContanst;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.dto.db.FeedBackDetailDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.FindProductSaleOrderDetailViewDTO;
import com.viettel.dms.dto.view.ListFindProductSaleOrderDetailViewDTO;
import com.viettel.dms.dto.view.ReviewsObjectDTO;
import com.viettel.dms.dto.view.ReviewsStaffViewDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.SupervisorActivity;
import com.viettel.sabeco.R;

/**
 * reviews staff view
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class ReviewsStaffView extends BaseFragment implements
		VinamilkTableListener, OnTouchListener {
	// tag for fragment
	public static final String TAG = ReviewsStaffView.class.getName();
	private static final int MENU_MAP = 0;
	private static final int MENU_IMAGE = 1;
	private static final int MENU_REVIEW = 2;
	private static final int MENU_INFO_DETAIL = 3;

	private static final int DONE_DATE_CONTROL = 1;
	// get main activity
	private SupervisorActivity parent;
	// customer code
	TextView tvCustomerCode;
	// customer name
	TextView tvCustomerName;
	// input add SKU
	EditText etListSKUNeedSale;
	// button add sku
	ImageButton ibAddSKU;
	// input improves
	EditText etInputImproves;
	// input feedback
	EditText etInputFeedback;
	// input reviews skill
	EditText etInputReviewsSkill;
	// button save reviews
	Button btSave;
	// dialog product detail view
	AlertDialog alertProductDetail;
	// product list
	VinamilkTableView tbProductList;
	// input product code
	VNMEditTextClearable etInputProductCode;
	// input product name
	VNMEditTextClearable etInputProductName;
	// // input programe code
	// VNMEditTextClearable etInputProgrameCode;
	// button close popup
	Button btClose;
	// button choose sku
	Button btChoose;
	// button search sku
	Button btSearch;
	// // button clear all data input
	// Button btClearAllInput;
	// list product
	ListFindProductSaleOrderDetailViewDTO listSourceProduct = new ListFindProductSaleOrderDetailViewDTO();
	// list product choosed
	ListFindProductSaleOrderDetailViewDTO listSelectedProduct = new ListFindProductSaleOrderDetailViewDTO();

	// flag when searching product
	boolean isSearchingProduct = false;

	// customer info
	CustomerDTO customerObject;
	// staff id cua NVBH
	public long staffId;
	// shop id cua NVBH
	public String shopId;
	// sale type code of staff id
	public String saleTypeCode;
	// training id
	public long trainId;
	// staff name
	public String staffName;
	// thong tin danh gia cua man hinh
	public ReviewsStaffViewDTO viewData = new ReviewsStaffViewDTO();
	// flag the first load
	public boolean isDoneLoadFirst = false;
	// limit item on page
	private int MAX_ITEM_ON_PAGE = 10;
	// product code
	private String productCodeKW = Constants.STR_BLANK;
	// product name
	private String productNameKW = Constants.STR_BLANK;
	// programe code
	private String progameCodeKW = Constants.STR_BLANK;
	// input done date
	VNMEditTextClearable etInputDoneDate;
	// current calendar
	int currentCalendar;
	private Bundle trainingReviewStaffBundle;

	public static ReviewsStaffView getInstance() {
		ReviewsStaffView instance = new ReviewsStaffView();
		return instance;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (SupervisorActivity) activity;
		trainingReviewStaffBundle = parent.getTrainingReviewStaffBundle();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_reviews_staff_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		// lay doi tuong training review staff Bundle neu null truoc khi lay du
		// lieu ben trong
		if (trainingReviewStaffBundle == null && parent != null) {
			trainingReviewStaffBundle = parent.getTrainingReviewStaffBundle();
		}
		if (trainingReviewStaffBundle != null) {
			if (!StringUtil.isNullOrEmpty(trainingReviewStaffBundle
					.getString(IntentConstants.INTENT_STAFF_ID))) {
				this.staffId = Long.parseLong(trainingReviewStaffBundle
						.getString(IntentConstants.INTENT_STAFF_ID));
			}
			if (!StringUtil.isNullOrEmpty(trainingReviewStaffBundle
					.getString(IntentConstants.INTENT_SALE_TYPE_CODE))) {
				this.saleTypeCode = trainingReviewStaffBundle
						.getString(IntentConstants.INTENT_SALE_TYPE_CODE);
			}
			this.staffName = trainingReviewStaffBundle
					.getString(IntentConstants.INTENT_STAFF_NAME);
			if (!StringUtil.isNullOrEmpty(trainingReviewStaffBundle
					.getString(IntentConstants.INTENT_SHOP_ID))) {
				this.shopId = trainingReviewStaffBundle
						.getString(IntentConstants.INTENT_SHOP_ID);
			}
			if (!StringUtil.isNullOrEmpty(trainingReviewStaffBundle
					.getString(IntentConstants.INTENT_TRAINING_DETAIL_ID))) {
				this.trainId = Long.parseLong(trainingReviewStaffBundle
						.getString(IntentConstants.INTENT_TRAINING_DETAIL_ID));
			}
			if (trainingReviewStaffBundle
					.getSerializable(IntentConstants.INTENT_CUSTOMER_OBJECT_TRAINING) != null) {
				this.customerObject = (CustomerDTO) trainingReviewStaffBundle
						.getSerializable(IntentConstants.INTENT_CUSTOMER_OBJECT);
			}
			enableMenuBar(this);
			initMenuActionBar();
			initView(v);
			this.setTitleForScreen();
			if (!this.isDoneLoadFirst) {
				this.loadReviewsDoneInDay();
			} else {
				renderLayout();
			}
		} else {
			this.parent.showDialog(Constants.HAS_ERROR_HAPPEN);
		}
		return v;
	}

	@Override
	public void onResume() {
		if (trainingReviewStaffBundle == null && parent != null) {
			trainingReviewStaffBundle = parent.getTrainingReviewStaffBundle();
		}
		super.onResume();
	}

	/**
	 * Mo ta muc dich cua ham
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void initMenuActionBar() {
		addMenuItem(StringUtil.getString(R.string.TEXT_POSITION),
				R.drawable.icon_map, MENU_MAP);
		addMenuItem(StringUtil.getString(R.string.TEXT_PICTURE),
				R.drawable.menu_picture_icon, MENU_IMAGE);
		addMenuItem(StringUtil.getString(R.string.TEXT_LABLE_REVIEWS),
				R.drawable.icon_note, MENU_REVIEW, View.INVISIBLE);
		addMenuItem(StringUtil.getString(R.string.TEXT_INFO),
				R.drawable.icon_detail, MENU_INFO_DETAIL);
		setMenuItemFocus(3);
	}

	/**
	 * set title header for screen
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void setTitleForScreen() {
		SpannableObject obj = new SpannableObject();
		if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES
				|| GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_VALSALES) {
			obj.addSpan(getString(R.string.TITLE_HEADER_TITLE_REVIEWS_NVBH_DAY)
					+ Constants.STR_SPACE, ImageUtil.getColor(R.color.BLACK),
					android.graphics.Typeface.NORMAL);
		} else {
			obj.addSpan(
					getString(R.string.TITLE_HEADER_TITLE_GSNPP_REVIEWS_NVBH_DAY)
							+ Constants.STR_SPACE,
					ImageUtil.getColor(R.color.BLACK),
					android.graphics.Typeface.NORMAL);
		}

		obj.addSpan(this.staffName, ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		setTitleHeaderView(obj);
	}

	/**
	 * kiem tra ngay hien tai co hop le hay khong
	 * @author: HaiTC3
	 * @return
	 * @return: boolean
	 * @throws:
	 * @since: Feb 5, 2013
	 */
	public boolean checkDoneDateCorrect() {
		boolean result = true;
		String doneDate = etInputDoneDate.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(doneDate)) {
			parent.showDialog(StringUtil
					.getString(R.string.TEXT_NOTIFY_DONE_DATE_NOT_NULL));
			result = false;
		} else {
			if (DateUtils.isCompareWithToDate(doneDate) < 0) {
				parent.showDialog(StringUtil
						.getString(R.string.TEXT_LABLE_TBHV_INFO_REMIND_DATE_ERROR));
				result = false;
			}
		}
		return result;
	}

	/**
	 * Kiem tra nhap lieu it nhat mot truong
	 * @author: BANGHN
	 * @return
	 */
	public boolean checkInputData(){
		boolean result = true;
		String contentApprove = etInputImproves.getText().toString().trim();
		String contentFeedback = etInputFeedback.getText().toString().trim();
		String contentSkill = etInputReviewsSkill.getText().toString().trim();
		String contentSKU = etListSKUNeedSale.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(contentApprove)
				&& StringUtil.isNullOrEmpty(contentFeedback)
				&& StringUtil.isNullOrEmpty(contentSkill)
				&& StringUtil.isNullOrEmpty(contentSKU)) {
			result = false;
			parent.showDialog(StringUtil
					.getString(R.string.TEXT_NOTIFY_INPURT_REVIEW));
		}
		return result;
	}
	
	
	/**
	 * lay thong tin cac danh gia NVBH tren khach hang da thuc hien trong ngay
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void loadReviewsDoneInDay() {
		this.parent.showProgressDialog(getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_STAFF_ID,
				String.valueOf(this.staffId));
		data.putString(IntentConstants.INTENT_CUSTOMER_ID,
				String.valueOf(this.customerObject.customerId));
		data.putString(IntentConstants.INTENT_TRAINING_DETAIL_ID,
				String.valueOf(this.trainId));
		data.putString(
				IntentConstants.INTENT_STAFF_OWNER_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile()
						.getUserData().id));
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance()
				.getProfile().getUserData().shopId);
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_REVIEWS_INFO_DONE;
		e.isNeedCheckTimeServer = false;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	/**
	 * cap nhat thong tin danh gia xuong DB local va toi server
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void requestUpdateReviewsToDBAndServer() {
		this.parent.showProgressDialog(getString(R.string.loading), false);
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_STAFF_ID,
				String.valueOf(this.staffId));
		data.putString(IntentConstants.INTENT_CUSTOMER_ID,
				this.customerObject.customerCode);
		data.putString(
				IntentConstants.INTENT_STAFF_OWNER_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile()
						.getUserData().id));
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance()
				.getProfile().getUserData().shopId);
		data.putString(IntentConstants.INTENT_TRAINING_DETAIL_ID,
				String.valueOf(this.trainId));
		data.putSerializable(IntentConstants.INTENT_REVIEWS_INFO, this.viewData);
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.UPDATE_OR_INSERT_REVIEWS_TO_DB;
		// e.isNeedCheckTimeServer = true;
		this.btSave.setEnabled(false);
		SuperviorController.getInstance().handleViewEvent(e);
	}

	/**
	 * init control view
	 * @author: HaiTC3
	 * @param v
	 * @return: void
	 * @throws:
	 */
	public void initView(View v) {
		tvCustomerCode = (TextView) v.findViewById(R.id.tvCustomerCode);
		tvCustomerName = (TextView) v.findViewById(R.id.tvCustomerName);
		// update UI customer info
		tvCustomerCode
				.setText(this.customerObject.customerCode != null
						&& this.customerObject.customerCode.length() > 3 ? this.customerObject.customerCode
						.substring(0, 3) : this.customerObject.customerCode);
		tvCustomerName.setText(this.customerObject.customerName);

		etListSKUNeedSale = (EditText) v.findViewById(R.id.etListSKUNeedSale);
		ibAddSKU = (ImageButton) v.findViewById(R.id.ibAddSKU);
		ibAddSKU.setOnClickListener(this);
		etInputImproves = (EditText) v.findViewById(R.id.etInputImproves);
		etInputFeedback = (EditText) v.findViewById(R.id.etInputFeedback);
		etInputReviewsSkill = (EditText) v
				.findViewById(R.id.etInputReviewsSkill);
		btSave = (Button) v.findViewById(R.id.btSave);
		btSave.setOnClickListener(this);
		// ngay nhac nho
		etInputDoneDate = (VNMEditTextClearable) v
				.findViewById(R.id.etInputDoneDate);
		this.etInputDoneDate.setIsHandleDefault(false);
		etInputDoneDate.setOnTouchListener(this);
		etInputDoneDate.setText(DateUtils.getCurrentDate());
	}

	/**
	 * render layout for screen
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void renderLayout() {
		if (viewData != null) {
			// hien thi danh sach SKU da chon
			renderSKUInfo();
			String remainDate = Constants.STR_BLANK;
			// hien thi thong tin danh gia
			if (viewData.listReviewsObject != null
					&& viewData.listReviewsObject.size() > 0) {
				for (int i = 0, size = viewData.listReviewsObject.size(); i < size; i++) {
					ReviewsObjectDTO reviewsInfo = viewData.listReviewsObject
							.get(i);
					if (reviewsInfo.feedBack.type == FeedBackDTO.FEEDBACK_TYPE_ABOUT_DISPLAY) {
						etInputImproves.setText(reviewsInfo.feedBack.content);
					} else if (reviewsInfo.feedBack.type == FeedBackDTO.FEEDBACK_TYPE_SUPPORT_NVBH) {
						etInputReviewsSkill
								.setText(reviewsInfo.feedBack.content);
					} else if (reviewsInfo.feedBack.type == FeedBackDTO.FEEDBACK_TYPE_AMOUNT) {
						etInputFeedback.setText(reviewsInfo.feedBack.content);
					}
					remainDate = reviewsInfo.feedBack.remindDate;
				}
			}
			if (viewData.feedBackSKU != null
					&& !StringUtil
							.isNullOrEmpty(viewData.feedBackSKU.feedBack.remindDate)) {
				etInputDoneDate.setText(DateUtils.convertFormatDate(
						viewData.feedBackSKU.feedBack.remindDate, "yyyy-MM-dd",
						"dd/MM/yyyy"));
			} else if (!StringUtil.isNullOrEmpty(remainDate)) {

				etInputDoneDate.setText(DateUtils.convertFormatDate(remainDate,
						"yyyy-MM-dd", "dd/MM/yyyy"));
			}
		}
	}

	/**
	 * hien thi thong tin sku tu d/s sku
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void renderSKUInfo() {
		if (viewData != null) {
			// hien thi danh sach SKU da chon
			if (viewData.listSKU != null && viewData.listSKU.size() > 0) {
				String doneSKU = Constants.STR_BLANK;
				for (int i = 0, size = viewData.listSKU.size(); i < size; i++) {
					if (viewData.listSKU.get(i).currentState != FeedBackDetailDTO.STATE_DELETED) {
						doneSKU += String
								.valueOf(viewData.listSKU.get(i).productCode);
						doneSKU += ",";
					}
				}
				if (doneSKU.length() > 1) {
					doneSKU = doneSKU.substring(0, doneSKU.length() - 1);
				}
				etListSKUNeedSale.setText(doneSKU);
			} else {
				etListSKUNeedSale.setText(Constants.STR_BLANK);
			}
		}
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.GET_REVIEWS_INFO_DONE:
			this.isDoneLoadFirst = false;
			if (modelEvent.getModelData() != null) {
				this.viewData = (ReviewsStaffViewDTO) modelEvent.getModelData();
				this.renderLayout();
			}
			this.parent.closeProgressDialog();
			break;
		case ActionEventConstant.GET_LIST_PRODUCT_ADD_REVIEWS_STAFF: {
			ListFindProductSaleOrderDetailViewDTO list = (ListFindProductSaleOrderDetailViewDTO) modelEvent
					.getModelData();

			if (list != null) {
				this.listSourceProduct = list;
			}

			this.showPopupSelectSKU();
			if (isSearchingProduct) {
				isSearchingProduct = false;
			}
			this.parent.closeProgressDialog();
			break;
		}
		case ActionEventConstant.UPDATE_OR_INSERT_REVIEWS_TO_DB: {
			this.parent.closeProgressDialog();
			this.btSave.setEnabled(true);
			GlobalUtil.popBackStack(this.getActivity());
			break;
		}
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.GET_REVIEWS_INFO_DONE:
		case ActionEventConstant.GET_LIST_PRODUCT_ADD_REVIEWS_STAFF:
			this.parent.closeProgressDialog();
			break;
		case ActionEventConstant.UPDATE_OR_INSERT_REVIEWS_TO_DB:
			this.btSave.setEnabled(true);

			// remove all danh gia vua duoc tao moi nhung bi loi khi luu, sku
			// thi khong remove vi da tao truoc khi action save
			for (int i = 0, size = this.viewData.listReviewsObject.size(); i < size; i++) {
				if (this.viewData.listReviewsObject.get(i).currentStateOfFeedback == FeedBackDetailDTO.STATE_NEW_INSERT) {
					this.viewData.listReviewsObject.remove(i);
					size = this.viewData.listReviewsObject.size();
				}
			}
			this.parent.closeProgressDialog();
			break;

		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == this.btSave) {
			GlobalUtil.forceHideKeyboard(parent);
			if (this.checkDoneDateCorrect() && checkInputData()) {
				// cap nhat thong tin de save xuong db
				this.generalDataToUpdateDB(); 
				// local va db server
				this.requestUpdateReviewsToDBAndServer();
			}
		} else if (v == this.ibAddSKU) {
			getListProductAddToReviewsStaff(0, this.productCodeKW,
					this.productNameKW, this.progameCodeKW, true);
		} else if (v == this.btSearch) {
			this.isSearchingProduct = true;
			this.saveTMPProductChecked();
			String productCodeKeyWord = Constants.STR_BLANK;
			String productNameKeyWord = Constants.STR_BLANK;
			String progameCodeKeyWord = Constants.STR_BLANK;

			if (this.alertProductDetail != null) {
				productCodeKeyWord = etInputProductCode.getText().toString()
						.trim();
				productNameKeyWord = etInputProductName.getText().toString()
						.trim();
				// progameCodeKeyWord = etInputProgrameCode.getText().toString()
				// .trim();
			}
			GlobalUtil.forceHideKeyboard(parent);
			this.getListProductAddToReviewsStaff(0, productCodeKeyWord,
					productNameKeyWord, progameCodeKeyWord, true);
		} else if (v == this.btClose) {
			this.resetKeyWord();
			GlobalUtil.forceHideKeyboard(parent);
			this.resetListObjectAndListSelectedObject();
			if (this.alertProductDetail != null) {
				this.alertProductDetail.dismiss();
			}
		} else if (v == this.btChoose) {
			this.saveTMPProductChecked();
			this.createListSKUFromListProductChecked();
			this.renderSKUInfo();
			this.resetKeyWord();
			GlobalUtil.forceHideKeyboard(parent);
			this.resetListObjectAndListSelectedObject();
			if (this.alertProductDetail != null) {
				this.alertProductDetail.dismiss();
			}
		}
		// else if (v == btClearAllInput) {
		// this.etInputProductCode.setText(Constants.STR_BLANK);
		// this.etInputProductName.setText(Constants.STR_BLANK);
		// this.etInputProgrameCode.setText(Constants.STR_BLANK);
		// }
		else {
			super.onClick(v);
		}
	}

	/**
	 * reset all keyword when close popup
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	private void resetKeyWord() {
		this.productCodeKW = Constants.STR_BLANK;
		this.productNameKW = Constants.STR_BLANK;
		this.progameCodeKW = Constants.STR_BLANK;
	}

	/**
	 * clear all product add to list selected product and list source product
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	private void resetListObjectAndListSelectedObject() {
		this.listSelectedProduct.listObject.clear();
		this.listSourceProduct.listObject.clear();
	}

	/**
	 * tao d/s SKU tu d/s san pham da checked trong popup
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void createListSKUFromListProductChecked() {
		for (int i = 0, size = this.listSelectedProduct.listObject.size(); i < size; i++) {
			boolean isExist = false;
			for (int j = 0, sizeSKU = this.viewData.listSKU.size(); j < sizeSKU; j++) {
				if (this.viewData.listSKU.get(j).productId == this.listSelectedProduct.listObject
						.get(i).saleOrderDetail.productId) {
					// if (this.viewData.listSKU.get(j).currentState ==
					// TrainingResultDTO.STATE_DELETED) {
					// this.viewData.listSKU.get(j).currentState =
					// TrainingResultDTO.STATE_NO_UPDATE;
					// }
					isExist = true;
				}
			}
			if (!isExist) {
				// training result info
				FeedBackDetailDTO feedbackDetailInfo = new FeedBackDetailDTO();
				feedbackDetailInfo
						.initData(
								-1,
								-1,
								Long.valueOf(this.listSelectedProduct.listObject
										.get(i).saleOrderDetail.productId),
								this.listSelectedProduct.listObject.get(i).productCode,
								GlobalInfo.getInstance().getProfile()
										.getUserData().id);
				this.viewData.listSKU.add(feedbackDetailInfo);
			}
		}
	}

	/**
	 * luu tam thoi cac san pham da check vao d/s tmp
	 * @author: HaiTC3
	 * @return
	 * @return: boolean
	 * @throws:
	 */
	public boolean saveTMPProductChecked() {
		boolean check = false;
		if (this.listSourceProduct != null
				&& this.listSourceProduct.listObject != null
				&& this.listSourceProduct.listObject.size() > 0) {
			for (int i = 0, size = tbProductList.getListRowView().size(); i < size; i++) {
				ProductNeedAddToReviewStaffRow row = (ProductNeedAddToReviewStaffRow) tbProductList
						.getListRowView().get(i);

				int index = this
						.isExitsProductInListSelected(this.listSourceProduct.listObject
								.get(i));
				if (index != -1) {
					boolean isChecked = row.cbSelected.isChecked();
					if (!isChecked) {
						this.listSelectedProduct.listObject.remove(index);

						// set state sku in list sku = state_deleted voi cac sku
						// da co trong db (id != -1) . con cac sku chua ton tai
						// trong DB thi remove khoi d/s luon
						for (int j = 0, length = viewData.listSKU.size(); j < length; j++) {
							if (viewData.listSKU.get(j).productId == this.listSourceProduct.listObject
									.get(i).saleOrderDetail.productId) {
								// da ton tai trong db nen chuyen trang thai cua
								// state = state_deleted
								if (viewData.listSKU.get(j).feedbackDetailId != -1
										&& viewData.listSKU.get(j).feedbackDetailId != 0) {
									viewData.listSKU.get(j).currentState = FeedBackDetailDTO.STATE_DELETED;
								}
								// chua co trong db nen se remove truc tiep
								else {
									viewData.listSKU.remove(j);
								}
								break;
							}
						}
						check = true;
					}
				} else {
					boolean isChecked = row.cbSelected.isChecked();
					// truong hop co chon 1 san pham thi bo xung san pham vao
					// d/s san pham da duoc chon
					if (isChecked) {
						this.listSourceProduct.listObject.get(i).isSelected = isChecked;
						this.listSelectedProduct.listObject
								.add(this.listSourceProduct.listObject.get(i));
						check = true;
					}
				}
			}
		}
		return check;
	}

	/**
	 * kiem tra product da ton tai trong d/s da duoc chon
	 * @author: HaiTC3
	 * @param currentProduct
	 * @return
	 * @return: boolean
	 * @throws:
	 */
	public int isExitsProductInListSelected(
			FindProductSaleOrderDetailViewDTO currentProduct) {
		int kq = -1;
		if (this.listSelectedProduct != null) {
			for (int i = 0, size = this.listSelectedProduct.listObject.size(); i < size; i++) {
				FindProductSaleOrderDetailViewDTO selectedProduct = this.listSelectedProduct.listObject
						.get(i);
				if (currentProduct.saleOrderDetail.productId == selectedProduct.saleOrderDetail.productId) {
					kq = i;
					break;
				}
			}
		}
		return kq;
	}

	/**
	 * lay danh sach san pham can them vao danh gia
	 * @author: HaiTC3
	 * @param numPage
	 * @return: void
	 * @throws:
	 */
	private void getListProductAddToReviewsStaff(int numPage,
			String productCodeKeyWord, String productNameKeyWord,
			String progameCodeKeyWord, boolean isGetCount) {
		this.parent.showProgressDialog(getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		String page = " limit " + (numPage * MAX_ITEM_ON_PAGE) + ","
				+ MAX_ITEM_ON_PAGE;
		this.productCodeKW = productCodeKeyWord.trim();
		this.productNameKW = productNameKeyWord.trim();
		this.progameCodeKW = progameCodeKeyWord.trim();
		// id NVBH
		data.putString(IntentConstants.INTENT_STAFF_ID,
				String.valueOf(this.staffId));
		// sale type code NVBH
		data.putString(IntentConstants.INTENT_SALE_TYPE_CODE,
				String.valueOf(this.saleTypeCode));
		// id cua GSNPP
		data.putString(
				IntentConstants.INTENT_STAFF_OWNER_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile()
						.getUserData().id));
		// shop id cua NVBH
		data.putString(IntentConstants.INTENT_SHOP_ID, this.shopId);
		// customer id
		data.putString(IntentConstants.INTENT_CUSTOMER_ID,
				String.valueOf(this.customerObject.customerCode));
		// customer type id
		data.putString(IntentConstants.INTENT_CUSTOMER_TYPE_ID,
				String.valueOf(this.customerObject.channelTypeId));
		// page
		data.putString(IntentConstants.INTENT_PAGE, page);

		// tham so khi tim kiem
		if (!StringUtil.isNullOrEmpty(productCodeKW)) {
			data.putString(IntentConstants.INTENT_PRODUCT_CODE, productCodeKW);
		}
		if (!StringUtil.isNullOrEmpty(productNameKW)) {
			data.putString(IntentConstants.INTENT_PRODUCT_NAME, productNameKW);
		}
		if (!StringUtil.isNullOrEmpty(progameCodeKW)) {
			data.putString(IntentConstants.INTENT_CTKM_CODE, progameCodeKW);
		}
		data.putBoolean(IntentConstants.INTENT_IS_GET_NUM_TOTAL_ITEM,
				isGetCount);

		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_LIST_PRODUCT_ADD_REVIEWS_STAFF;
		e.isNeedCheckTimeServer = false;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	/**
	 * hien thi pop up chon sku
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	private void showPopupSelectSKU() {
		// TODO Auto-generated method stub
		if (alertProductDetail == null) {
			Builder build = new AlertDialog.Builder(parent,
					R.style.CustomDialogTheme);
			LayoutInflater inflater = this.parent.getLayoutInflater();
			View view = inflater.inflate(
					R.layout.layout_select_product_need_add_sale, null);

			tbProductList = (VinamilkTableView) view
					.findViewById(R.id.tbProductList);
			tbProductList.setListener(this);
			btChoose = (Button) view.findViewById(R.id.btChoose);
			btChoose.setOnClickListener(this);
			btClose = (Button) view.findViewById(R.id.btClose);
			btClose.setOnClickListener(this);
			// btClearAllInput = (Button)
			// view.findViewById(R.id.btClearAllInput);
			// btClearAllInput.setOnClickListener(this);
			btSearch = (Button) view.findViewById(R.id.btSearch);
			btSearch.setOnClickListener(this);

			etInputProductCode = (VNMEditTextClearable) view
					.findViewById(R.id.etInputProductCode);
			etInputProductCode.setFocusableInTouchMode(true);
			etInputProductName = (VNMEditTextClearable) view
					.findViewById(R.id.etInputProductName);
			// etInputProgrameCode = (VNMEditTextClearable) view
			// .findViewById(R.id.etInputProgrameCode);

			tbProductList
					.getHeaderView()
					.addColumns(
							TableDefineContanst.SELECT_PRODUCT_ADD_REVIEWS_STAFF_TABLE_WIDTHS,
							TableDefineContanst.SELECT_PRODUCT_ADD_REVIEWS_STAFF_TABLE_TITLES,
							ImageUtil.getColor(R.color.BLACK),
							ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			tbProductList
					.getHeaderView()
					.addColumnIsCheckBoxControl(
							TableDefineContanst.COLUMN_WIDTH_CHECK_BOX_ADD_REVIEWS_STAFF,
							ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			tbProductList.setNumItemsPage(MAX_ITEM_ON_PAGE);

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
		if (tbProductList != null) {
			tbProductList.getHeaderView().resetCheckBoxCheckAll();
			// paging
			if (this.listSourceProduct.listObject.size() > 0) {
				tbProductList.getPagingControl().setVisibility(View.VISIBLE);
				if (tbProductList.getPagingControl().totalPage < 0
						|| isSearchingProduct
						|| !alertProductDetail.isShowing()) {
					tbProductList
							.setTotalSize(this.listSourceProduct.totalObject);
					tbProductList.getPagingControl().setCurrentPage(1);
				}
			} else {
				tbProductList.getPagingControl().setVisibility(View.GONE);
			}
		}

		int pos = 1;
		List<TableRow> listRows = new ArrayList<TableRow>();
		if (listSourceProduct.listObject.size() > 0) {
			for (int i = 0, size = listSourceProduct.listObject.size(); i < size; i++) {
				FindProductSaleOrderDetailViewDTO dto = listSourceProduct.listObject
						.get(i);
				ProductNeedAddToReviewStaffRow row = new ProductNeedAddToReviewStaffRow(
						parent, tbProductList);
				row.setClickable(true);
				row.setTag(Integer.valueOf(pos));
				// update number order for product on view
				FindProductSaleOrderDetailViewDTO productSelected = this
						.getNumberOrderForProduct(dto);
				if (productSelected != null) {
					dto = productSelected;
					listSourceProduct.listObject.set(i, productSelected);
				}
				row.renderLayout(
						pos
								+ (tbProductList.getPagingControl()
										.getCurrentPage() - 1)
								* MAX_ITEM_ON_PAGE, dto);

				row.setListener(this);
				listRows.add(row);
				pos++;
			}
		} else {
			ProductNeedAddToReviewStaffRow row = new ProductNeedAddToReviewStaffRow(
					parent, tbProductList);
			row.setClickable(true);
			row.setListener(this);
			row.renderLayoutNotify(getString(R.string.TEXT_NOTIFY_PRODUCT_NULL));
			listRows.add(row);
		}
		tbProductList.addContent(listRows);
		alertProductDetail.setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				// TODO Auto-generated method stub
				GlobalUtil.forceHideKeyboard(parent);
			}
		});
		if (!alertProductDetail.isShowing()) {
			alertProductDetail.show();
		}
	}

	/**
	 * get product if it have selected
	 * @author: HaiTC3
	 * @param product
	 * @return
	 * @return: FindProductSaleOrderDetailViewDTO
	 * @throws:
	 */
	public FindProductSaleOrderDetailViewDTO getNumberOrderForProduct(
			FindProductSaleOrderDetailViewDTO product) {
		FindProductSaleOrderDetailViewDTO kq = product;
		for (int i = 0, size = listSelectedProduct.listObject.size(); i < size; i++) {
			FindProductSaleOrderDetailViewDTO productSelected = listSelectedProduct.listObject
					.get(i);
			if (product.saleOrderDetail.productId == productSelected.saleOrderDetail.productId) {
				kq = productSelected;
				return kq;
			}
		}
		if (viewData != null && viewData.listSKU != null) {
			for (int i = 0, size = viewData.listSKU.size(); i < size; i++) {
				if (product.saleOrderDetail.productId == viewData.listSKU
						.get(i).productId
						&& viewData.listSKU.get(i).currentState != FeedBackDetailDTO.STATE_DELETED) {
					product.isSelected = true;
					listSelectedProduct.listObject.add(product);
					kq = product;
					break;
				}
			}
		}
		return kq;
	}

	/**
	 * 
	 * cap nhat thong tin danh gia truoc khi tien hanh luu xuong db local va db
	 * server. cac thong tin d/s sku da duoc tao truoc do roi nen o day khong
	 * khoi tao lai nua
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void generalDataToUpdateDB() {
		// cap nhat thong tin review
		String contentApproveDisplay = etInputImproves.getText().toString()
				.trim();
		String contentFeedback = etInputFeedback.getText().toString().trim();
		String contentReviewsSkill = etInputReviewsSkill.getText().toString()
				.trim();
		String remainDate = this.getRemindDate() + " 00:00:00";
		boolean isHasApproveDisplay = false;
		boolean isHasFeedBack = false;
		boolean isHasReviewsSkill = false;

		// cap nhat thong tin feedback SKU
		if (viewData.feedBackSKU.feedBack != null
				&& viewData.feedBackSKU.feedBack.feedBackId <= 0) {
			if (viewData.listSKU != null
					&& this.viewData.listSKU.size() > 0) {
				viewData
						.initFeedbackSKUFromListSKU(this.staffId,
								FeedBackDTO.FEEDBACK_TYPE_SKU, GlobalInfo
										.getInstance().getProfile()
										.getUserData().id, remainDate,
								String.valueOf(this.trainId),
								String.valueOf(this.customerObject.customerId),
								DateUtils.now(), DateUtils.now(), GlobalInfo
										.getInstance().getProfile()
										.getUserData().shopId);
			}else{
				//khong co sku
				viewData.feedBackSKU.feedBack = null;
			}

		} else {
			this.viewData.updateContentAndRemainDate(
					remainDate,
					String.valueOf(GlobalInfo.getInstance().getProfile()
							.getUserData().id));
		}

		// kiem tra noi dung da danh gia truoc day (neu co)
		for (int i = 0, size = this.viewData.listReviewsObject.size(); i < size; i++) {
			String content = Constants.STR_BLANK;
			ReviewsObjectDTO reviewsInfo = viewData.listReviewsObject.get(i);
			if (reviewsInfo.feedBack.type == FeedBackDTO.FEEDBACK_TYPE_ABOUT_DISPLAY) {
				isHasApproveDisplay = true;
				content = contentApproveDisplay;
			} else if (reviewsInfo.feedBack.type == FeedBackDTO.FEEDBACK_TYPE_SUPPORT_NVBH) {
				isHasReviewsSkill = true;
				content = contentReviewsSkill;
			} else if (reviewsInfo.feedBack.type == FeedBackDTO.FEEDBACK_TYPE_AMOUNT) {
				isHasFeedBack = true;
				content = contentFeedback;
			}
			// truong hop noi dung co thay doi thi moi chuyen state thanh
			// "new_update" con khong thi mac dinh thiet lap la "no_update"
			if (!content
					.equals(viewData.listReviewsObject.get(i).feedBack.content)
					|| !viewData.listReviewsObject.get(i).feedBack.remindDate
							.equals(remainDate)) {
				viewData.listReviewsObject.get(i).feedBack.content = content;
				viewData.listReviewsObject.get(i).feedBack.remindDate = remainDate;
				viewData.listReviewsObject.get(i).feedBack.updateUser = String
						.valueOf(GlobalInfo.getInstance().getProfile()
								.getUserData().id);
				viewData.listReviewsObject.get(i).feedBack.updateDate = DateUtils
						.now();
				if (StringUtil.isNullOrEmpty(content)) {
					viewData.listReviewsObject.get(i).currentStateOfFeedback = FeedBackDetailDTO.STATE_DELETED;
				} else {
					viewData.listReviewsObject.get(i).currentStateOfFeedback = FeedBackDetailDTO.STATE_NEW_UPDATE;
				}
			} else {
				viewData.listReviewsObject.get(i).currentStateOfFeedback = FeedBackDetailDTO.STATE_NO_UPDATE;
			}
		}

		// truong hop cac noi dung chua tao trong list thi bat dau tao cac danh
		// gia de bo xung vao list voi state = new_insert
		if (!isHasApproveDisplay
				&& !StringUtil.isNullOrEmpty(contentApproveDisplay)) {
			ReviewsObjectDTO reviewsInfo1 = new ReviewsObjectDTO();
			FeedBackDTO feedback1 = new FeedBackDTO();
			feedback1
					.initDateForTrainingResult(
							-1,
							this.staffId,
							String.valueOf(this.customerObject.customerId),
							FeedBackDTO.FEEDBACK_STATUS_CREATE,
							contentApproveDisplay,
							String.valueOf(GlobalInfo.getInstance()
									.getProfile().getUserData().id),
							DateUtils.now(),
							DateUtils.now(),
							FeedBackDTO.FEEDBACK_TYPE_ABOUT_DISPLAY,
							GlobalInfo.getInstance().getProfile().getUserData().id,
							remainDate, GlobalInfo.getInstance().getProfile()
									.getUserData().shopId,
							String.valueOf(this.trainId));
			reviewsInfo1.feedBack = feedback1;

			viewData.listReviewsObject.add(reviewsInfo1);
		}
		if (!isHasFeedBack && !StringUtil.isNullOrEmpty(contentFeedback)) {
			ReviewsObjectDTO reviewsInfo2 = new ReviewsObjectDTO();
			FeedBackDTO feedback2 = new FeedBackDTO();
			feedback2
					.initDateForTrainingResult(
							-1,
							this.staffId,
							String.valueOf(this.customerObject.customerId),
							FeedBackDTO.FEEDBACK_STATUS_CREATE,
							contentFeedback,
							String.valueOf(GlobalInfo.getInstance()
									.getProfile().getUserData().id),
							DateUtils.now(),
							DateUtils.now(),
							FeedBackDTO.FEEDBACK_TYPE_AMOUNT,
							GlobalInfo.getInstance().getProfile().getUserData().id,
							remainDate, GlobalInfo.getInstance().getProfile()
									.getUserData().shopId,
							String.valueOf(this.trainId));
			reviewsInfo2.feedBack = feedback2;

			viewData.listReviewsObject.add(reviewsInfo2);
		}
		if (!isHasReviewsSkill
				&& !StringUtil.isNullOrEmpty(contentReviewsSkill)) {
			ReviewsObjectDTO reviewsInfo3 = new ReviewsObjectDTO();
			FeedBackDTO feedback3 = new FeedBackDTO();
			feedback3
					.initDateForTrainingResult(
							-1,
							this.staffId,
							String.valueOf(this.customerObject.customerId),
							FeedBackDTO.FEEDBACK_STATUS_CREATE,
							contentReviewsSkill,
							String.valueOf(GlobalInfo.getInstance()
									.getProfile().getUserData().id),
							DateUtils.now(),
							DateUtils.now(),
							FeedBackDTO.FEEDBACK_TYPE_SUPPORT_NVBH,
							GlobalInfo.getInstance().getProfile().getUserData().id,
							remainDate, GlobalInfo.getInstance().getProfile()
									.getUserData().shopId,
							String.valueOf(this.trainId));
			reviewsInfo3.feedBack = feedback3;

			viewData.listReviewsObject.add(reviewsInfo3);
		}
	}

	/**
	 * get remain date when save
	 * @author: HaiTC3
	 * @return
	 * @return: String
	 * @throws:
	 * @since: Feb 5, 2013
	 */
	public String getRemindDate() {
		String result = Constants.STR_BLANK;
		try {
			String strRemindDate = etInputDoneDate.getText().toString().trim();
			if (!StringUtil.isNullOrEmpty(strRemindDate)) {
				Date tn = StringUtil.stringToDate(strRemindDate,
						Constants.STR_BLANK);
				result = StringUtil.dateToString(tn, "yyyy-MM-dd");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		case MENU_INFO_DETAIL:
			GlobalUtil.popBackStack(this.getActivity());
			gotoCustomerInfo(customerObject.customerId);
			break;
		case MENU_MAP:
			GlobalUtil.popBackStack(this.getActivity());
			gotoCustomerLocation();
			break;
		case MENU_IMAGE:
			GlobalUtil.popBackStack(this.getActivity());
			gotoListAlbumUser();
			break;
		default:
			break;
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub
		if (control == tbProductList) {
			this.saveTMPProductChecked();
			this.getListProductAddToReviewsStaff((tbProductList
					.getPagingControl().getCurrentPage() - 1),
					this.productCodeKW, this.productNameKW, this.progameCodeKW,
					false);
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		// TODO Auto-generated method stub
		if (action == ActionEventConstant.HEADER_CLICK
				&& control == tbProductList) {
			int check = Integer.parseInt(String.valueOf(data));
			if (this.listSourceProduct != null
					&& this.listSourceProduct.listObject != null
					&& this.listSourceProduct.listObject.size() > 0) {
				for (int i = 0, size = tbProductList.getListRowView().size(); i < size; i++) {
					ProductNeedAddToReviewStaffRow row = (ProductNeedAddToReviewStaffRow) tbProductList
							.getListRowView().get(i);
					if (check == 1) {
						row.cbSelected.setChecked(true);
					} else {
						row.cbSelected.setChecked(false);
					}
				}
			}
		} else if (action == ActionEventConstant.GO_TO_PROMOTION_PROGRAME_DETAIL) {
			FindProductSaleOrderDetailViewDTO dto = (FindProductSaleOrderDetailViewDTO) data;
			this.requestGetPromotionDetail(dto.promotionProgrameCode);
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				if (this.alertProductDetail != null
						&& this.alertProductDetail.isShowing()) {
					GlobalUtil.forceHideKeyboard(parent);
					this.alertProductDetail.dismiss(); // dong popup
				}
				this.loadReviewsDoneInDay(); // load lai du lieu
			}
			break;

		default:
			super.receiveBroadcast(action, extras);
			break;
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (v == etInputDoneDate) {
			if (!etInputDoneDate.onTouchEvent(event)) {
				currentCalendar = DONE_DATE_CONTROL;
				parent.fragmentTag = ReviewsStaffView.TAG;
				parent.showDatePickerWithDate(
						GlobalBaseActivity.DATE_DIALOG_ID, etInputDoneDate
								.getText().toString(), true);
			}
		}
		return true;
	}

	/**
	 * update edittext done date
	 * @author: HaiTC3
	 * @param dayOfMonth
	 * @param monthOfYear
	 * @param year
	 * @return: void
	 * @throws:
	 * @since: Feb 5, 2013
	 */
	public void updateFromDateAndEndDate(int dayOfMonth, int monthOfYear,
			int year) {
		String sDay = String.valueOf(dayOfMonth);
		String sMonth = String.valueOf(monthOfYear + 1);
		if (dayOfMonth < 10) {
			sDay = "0" + sDay;
		}
		if (monthOfYear + 1 < 10) {
			sMonth = "0" + sMonth;
		}
		String dates = Constants.STR_BLANK;
		if (currentCalendar == DONE_DATE_CONTROL) {
			dates = new StringBuilder().append(sDay).append("/").append(sMonth)
					.append("/").append(year).append(Constants.STR_SPACE)
					.toString();
			int a = DateUtils.isCompareWithToDate(dates);
			if (a < 0) {
				parent.showDialog(StringUtil
						.getString(R.string.TEXT_LABLE_TBHV_INFO_REMIND_DATE_ERROR));
			} else {
				etInputDoneDate.setTextColor(ImageUtil.getColor(R.color.BLACK));
				etInputDoneDate.setText(new StringBuilder().append(sDay)
						.append("/").append(sMonth).append("/").append(year)
						.append(Constants.STR_SPACE));
			}
		}
	}

	/**
	 * Mo ta muc dich cua ham
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void gotoListAlbumUser() {
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentPrefixTitle()); 
		bundle.putSerializable(IntentConstants.INTENT_CUSTOMER, customerObject);
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_LIST_ALBUM_USER;
		SaleController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Mo ta muc dich cua ham
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void gotoCustomerLocation() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentPrefixTitle());
		bundle.putSerializable(IntentConstants.INTENT_CUSTOMER, customerObject);
		e.viewData = bundle;
		e.action = ActionEventConstant.GOTO_CUSTOMER_LOCATION;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Mo ta muc dich cua ham
	 * @author: TamPQ
	 * @param customerId
	 * @return: voidvoid
	 * @throws:
	 */
	private void gotoCustomerInfo(long customerId) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bunde = new Bundle();
		bunde.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentPrefixTitle());
		bunde.putString(IntentConstants.INTENT_CUSTOMER_ID, "" + customerId);
		e.viewData = bunde;
		e.action = ActionEventConstant.GO_TO_CUSTOMER_INFO;
		UserController.getInstance().handleSwitchFragment(e);
	}

}
