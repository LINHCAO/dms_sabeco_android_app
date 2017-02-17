/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.main;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.FragmentMenuContanst.MenuCommand;
import com.viettel.dms.constants.FragmentMenuContanst.MenuItemInfo;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.ProductDTO;
import com.viettel.dms.dto.db.PromotionProgrameDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.util.guard.AccessInternetService;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.control.MenuItem;
import com.viettel.dms.view.control.RightHeaderMenuBar;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.sale.order.ProductInfoDetailView;
import com.viettel.dms.view.sale.promotion.PromotionProgrameDetailView;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * Base fragment
 * Xu ly cac phan chung: header, handle du lieu tu controller...
 * @author banghn
 */
public class BaseFragment extends Fragment implements OnClickListener, OnEventControlListener{
	//activity chua view
	protected GlobalBaseActivity activity;
	//view root chua header & view fragment con
	LinearLayout viewRoot;
	//header trong fragment
	private View rlHeader;
	//header title
	private TextView tvTitle;
	//menu icon trong view fragment
	public RightHeaderMenuBar menuBar;
	//kiem tra fragment duoc finish, detroy chua
	public boolean isFinished = false;
	// dialog product detail view
	AlertDialog alertPromotionDetail;
	
	PromotionProgrameDetailView promotionDetailView;
	
	// dialog product detail view
	AlertDialog alertProductInfoDetail;
		
	ProductInfoDetailView productInfoDetailView;
	
	public String VIEW_NAME = "";

	// bien do log KPI
	//public Calendar startTimeKPI = null;
	//public Calendar endTimeKPI;
	
	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int action = intent.getExtras().getInt(Constants.ACTION_BROADCAST);
			int hasCode = intent.getExtras().getInt(
					Constants.HASHCODE_BROADCAST);
			if (hasCode != BaseFragment.this.hashCode()) {
				receiveBroadcast(action, intent.getExtras());
			}
		}
	};
	public boolean isZooming;
	private boolean isFirstRequestKPI = false;

	public static  BaseFragment getInstance(){
		return new BaseFragment();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		try {
			IntentFilter filter = new IntentFilter(SalesPersonActivity.DMS_ACTION);
			activity.registerReceiver(receiver, filter);
		} catch (Exception e) {
		}
	}

	@Override
	public void onAttach(Activity activity) {
		this.activity = (GlobalBaseActivity) activity;
		super.onAttach(activity);
	}
	
	@Override
	public void onResume() {
		isFinished = false;
		super.onResume();
	}

	@Override
	public void onDestroyView() {
		//GlobalUtil.nullViewDrawablesRecursive(viewRoot);
		//isFinished = true;
		try {
			getActivity().unregisterReceiver(receiver);
		} catch (Exception e) {
		}
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		isFinished = true;
		try {
			getActivity().unregisterReceiver(receiver);
		} catch (Exception e) {
		}
		super.onDestroy();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		isFirstRequestKPI = true;
		Bundle bundle = getArguments();
		if(bundle != null){ 
			this.strParentPrefixTitle = bundle.getString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, strParentPrefixTitle);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_fragment_base, null,
				false);
		viewRoot = (LinearLayout) view.findViewById(R.id.llMain);
		viewRoot.addView(container);
		isFinished = false;
		if (container != null){
			container.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					GlobalUtil.forceHideKeyboard(activity);
					return false;
				}
			});
		}
		initHeaderview();
		return view;
	}

	/**
	 * Khoi tao add header view
	 * @author : BangHN
	 * since : 4:30:53 PM
	 */
	private void initHeaderview() {
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rlHeader = inflater.inflate(R.layout.layout_fragment_header, null);
		viewRoot.addView(rlHeader, 0);
		tvTitle = (TextView)rlHeader.findViewById(R.id.tvTitle);
	}
	
	
	/**
	 * set title header view (fragment)
	 * @author : BangHN
	 * since : 4:33:48 PM
	 * @modify duongdt change String -> CharSequence (chấp nhận nhiều loại dữ liệu hơn)
	 */
	protected void setTitleHeaderView(CharSequence title) {
		if(tvTitle != null){
			tvTitle.setText(title);
		}
	}
	
	protected void setTitleHeaderView(SpannableObject title) {
		if(tvTitle != null){
			tvTitle.setText(title.getSpan());
		}
	}
	protected void setTitleHeaderView(SpannableString title) {
		if(tvTitle != null){
			tvTitle.setText(title);
		}
	}
	
	/**
	 * Send broadcast toi cac fragment khac
	 * @author : BangHN
	 * since : 4:29:24 PM
	 */
	public void sendBroadcast(int action, Bundle bundle) {
		Intent intent = new Intent(GlobalBaseActivity.DMS_ACTION);
		bundle.putInt(Constants.ACTION_BROADCAST, action);
		bundle.putInt(Constants.HASHCODE_BROADCAST, intent.getClass()
				.hashCode());
		intent.putExtras(bundle);
		activity.sendBroadcast(intent, "com.viettel.sabeco.permission.BROADCAST");
	}

	
	/**
	 * Nhan broadcast tu cac view fragment khac
	 * @author : BangHN
	 * since : 4:29:39 PM
	 */
	protected void receiveBroadcast(int action, Bundle extras) {
		// TODO Auto-generated method stub

	}

	
	/**
	 * Nhan handle modle tu controller.
	 * @author : BangHN
	 * since : 4:29:55 PM
	 */
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent actionEvent = modelEvent.getActionEvent();
		switch (actionEvent.action) {
		case ActionEventConstant.GO_TO_PROMOTION_PROGRAME_DETAIL: {//hien thi thong tin chi tiet khuyen mai
			PromotionProgrameDTO promotionInfo = (PromotionProgrameDTO) modelEvent
					.getModelData();
			showPromotionDetailView(promotionInfo);
			activity.closeProgressDialog();
			break;
		}
		case ActionEventConstant.GO_TO_PRODUCT_INFO_DETAIL: {//hien thi thong tin chi tiet san pham
			ProductDTO promotionInfo = (ProductDTO) modelEvent
					.getModelData();
			showProductInfoDetailView(promotionInfo);
			break;
		}
		case ActionEventConstant.ACTION_GET_CURRENT_DATE_TIME_SERVER: {
			// tam thoi chua lam j ca
			break;
		}
		default:
			break;
		}
	}

	
	/**
	 * Xu ly luong loi khi nhan tu controller
	 * @author : BangHN
	 * since : 4:30:11 PM
	 */
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent actionEvent = modelEvent.getActionEvent();
		switch (actionEvent.action) {
		case ActionEventConstant.ACTION_GET_CURRENT_DATE_TIME_SERVER: {
			// neu dong bo online ma thoi gian bi loi, thi thong bao loi va hien thi setting
			if(modelEvent.getModelData() == null){
				return;
			}
			int res = (Integer)modelEvent.getModelData();
			if (res == ModelEvent.MODEL_RESULT_FAIL_TIME_ONLINE){
				AlertDialog alertDialog = null;
				try {
					alertDialog = new AlertDialog.Builder(activity).create();
					alertDialog.setMessage(StringUtil.getString(R.string.ERROR_TIME_OFFLINE_INVALID));
					alertDialog.setButton(StringUtil
							.getString(R.string.TEXT_BUTTON_CLOSE),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									// hien thi man hinh setting 
									startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
									//khong cam setting neu bi chan
									AccessInternetService.unlockAppPrevent(true);
									return;

								}
							});
					alertDialog.show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
				}
			}
			break;
		}
		case ActionEventConstant.GO_TO_PROMOTION_PROGRAME_DETAIL:{
			activity.closeProgressDialog();
			break;
		}
		default:
			activity.handleErrorModelViewEvent(modelEvent);
			break;
		}
		
	}

	
	/**
	 * Enable menu bar icon
	 * @author : TamPQ
	 * since : 4:30:24 PM
	 */
	public void enableMenuBar(OnEventControlListener listener) {
		menuBar = new RightHeaderMenuBar(getActivity());
		menuBar.setOnEventControlListener(listener);
		LinearLayout llMenuBar = (LinearLayout) rlHeader
				.findViewById(R.id.ll_menubar);
		llMenuBar.addView(menuBar);
	}

	/**
	 * Enable menu bar icon, fill with menu info
	 * @author : duongdt
	 */
	public void enableMenuBar(OnEventControlListener listener, MenuCommand menuCommand, int indexMenuFocus) {
	    	enableMenuBar(listener);
		this.menuCommand = menuCommand;
		
		//add menu info
		for (int i = 0; i < menuCommand.getListMenuInfo().length; i++) {
		    MenuItemInfo menuInfo = menuCommand.getListMenuInfo()[i];
		    addMenuItem(menuInfo.text, menuInfo.iconId, menuInfo.action);
		}
		
		//set focus
		setMenuItemFocus(indexMenuFocus);
	}
	
	
	
	/**
	 * Add icon menu
	 * @author : TamPQ
	 * since : 4:30:39 PM
	 */
	public void addMenuItem(int drawableResId, int action) {
		menuBar.addMenuItem(drawableResId, action, View.VISIBLE);
	}
	
	public void addMenuItem(String text, int drawableResId, int action) {
		menuBar.addMenuItem(text, drawableResId, action, View.VISIBLE);
	}
	
	/**
	 * Add menu icon kem theo co hien thi dau ngan cach hay khong (GONE || VISIBLE)
	 * @author : BangHN
	 * since : 9:20:11 AM
	 */
	public void addMenuItem(int drawableResId, int action, int separateVisible) {
		menuBar.addMenuItem(drawableResId, action, separateVisible);
	}
	
	public void addMenuItem(String text, int drawableResId, int action, int separateVisible) {
		menuBar.addMenuItem(text, drawableResId, action, separateVisible);
	}
	
	public void setMenuItemFocus(int index){
		menuBar.setMenuItemFocus(index);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(promotionDetailView != null && v == promotionDetailView.btClosePopupPromotionDetail){
			alertPromotionDetail.dismiss();
		}
		if(productInfoDetailView != null && v == productInfoDetailView.btCloseProductInfoDetail){
			alertProductInfoDetail.dismiss();
		}
	}

	
	/**
	 * 
	*  show thong tin chi tiet chuong trinh khuyen mai
	*  @author: HaiTC3
	*  @param program
	*  @return: void
	*  @throws:
	 */
	public void showPromotionDetailView(PromotionProgrameDTO program) {
		if (alertPromotionDetail == null) {
			Builder build = new AlertDialog.Builder(activity,
					R.style.CustomDialogTheme);
			promotionDetailView = new PromotionProgrameDetailView(activity,
					this);
			build.setView(promotionDetailView.viewLayout);
			alertPromotionDetail = build.create();
			Window window = alertPromotionDetail.getWindow();
			window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255,
					255, 255)));
			window.setLayout(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
		}
		promotionDetailView.updateLayout(program);
		alertPromotionDetail.show();
	}
	
	/**
	 * 
	*  Hien thi popup chi tiet san pham
	*  @author: ThanhNN8
	*  @param product
	*  @return: void
	*  @throws:
	 */
	public void showProductInfoDetailView(ProductDTO product) {
		if (alertProductInfoDetail == null) {
			Builder build = new AlertDialog.Builder(activity,
					R.style.CustomDialogTheme);
			productInfoDetailView = new ProductInfoDetailView(activity,
					this);
			build.setView(productInfoDetailView.viewLayout);
			alertProductInfoDetail = build.create();
			Window window = alertProductInfoDetail.getWindow();
			window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255,
					255, 255)));
			window.setLayout(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
		}
		productInfoDetailView.updateLayout(product);
		alertProductInfoDetail.show();
	}
	
	/**
	 * 
	*  Request get thong tin chi tiet chuong trinh
	*  @author: HaiTC3
	*  @param promotionCode
	*  @return: void
	*  @throws:
	 */
	public void requestGetPromotionDetail(String promotionCode) {
		// go to promotion detail
		activity.showProgressDialog(getString(R.string.loading));
		Bundle dto = new Bundle();
		dto.putString(IntentConstants.INTENT_PROMOTION_CODE, promotionCode);
		dto.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = dto;
		e.action = ActionEventConstant.GO_TO_PROMOTION_PROGRAME_DETAIL;
		e.isNeedCheckTimeServer = false;
		SaleController.getInstance().handleViewEvent(e);
	}
	
	/**
	 * 
	*  Request lay thong tin chi tiet cua san pham
	*  @author: ThanhNN8
	*  @param productId
	*  @return: void
	*  @throws:
	 */
	public void requestGetProductInfoDetail(String productId) {
		// go to promotion detail
		Bundle dto = new Bundle();
		dto.putString(IntentConstants.INTENT_PRODUCT_ID, productId);
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = dto;
		e.action = ActionEventConstant.GO_TO_PRODUCT_INFO_DETAIL;
		SaleController.getInstance().handleViewEvent(e);
	}
	
	/**
	 * 
	*  Remove fragment ra khoi stack 
	*  @author: PhucNT
	*  @param promotionCode
	*  @return: void
	*  @throws:
	 */
	public void removeFragmentFromBackStack() {
		// go to promotion detail
		FragmentManager fm = this.getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		fm.popBackStack(this.getClass().getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
		ft.commit();
	}

	protected MenuCommand menuCommand = null;
	@Override
	public void onEvent(int eventType, View control, Object data) {
		//menu item
	    //duongdt add menuComand
	    if (control instanceof MenuItem) {
	    	if (menuCommand != null) {
	    		int indexMenuFocus = menuBar.getMenuItemFocus();
	    		menuCommand.onActionMenuEvent(this, eventType, indexMenuFocus);
	    	}
	    }else{
        	if (eventType == PromotionProgrameDetailView.CLOSE_POPUP_DETAIL_PROMOTION){
       			alertPromotionDetail.dismiss();
       		}
    	}
	}
		
	/**
	*  Lay thoi gian hien tai tren server
	*  @author: TruongHN
	*  @return: void
	*  @throws:
	 */
	public void getCurrentDateTimeServer(ActionEvent actionEventBeforeGetTime) {
		ActionEvent e = new ActionEvent();
		Bundle bundle = new Bundle();
		bundle.putBoolean(IntentConstants.INTENT_IS_SYNC, false);
		e.viewData = bundle;
		e.sender = this;
		e.userData = actionEventBeforeGetTime;
		e.action = ActionEventConstant.ACTION_GET_CURRENT_DATE_TIME_SERVER;
		UserController.getInstance().handleViewEvent(e);
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
	}


	public void closePopup() {

	}
	
	/**
	 * Tra ve tab index, mac dinh tra ve 1
	 * 
	 * @author: QuangVT1
	 * @since: 6:20:06 PM Jan 13, 2014
	 * @return: int
	 * @throws:  
	 * @return
	 */
	protected int getTabIndex(){
		return 1;
	}
	
	//onCreate lay trong bundle
	protected String strParentPrefixTitle = "";
	
	/**
	 * Tra ve prefix title cua man hinh cha
	 * 
	 * @author: QuangVT1
	 * @since: 6:20:12 PM Jan 13, 2014
	 * @return: String
	 * @throws:  
	 * @return
	 */
	protected String getParentPrefixTitle(){
		return strParentPrefixTitle;
	}
	
	/**
	 * Lay Prefix title cua chinh minh
	 * VD: 02-01. Danh sach khach hang -->  return : 02-01
	 * 
	 * @author: QuangVT1
	 * @since: 4:41:02 PM Jan 13, 2014
	 * @return: String
	 * @throws:  
	 * @return
	 */
	public String getMenuIndexString(){
		NumberFormat formatter = new DecimalFormat("00");
		String s = formatter.format(getTabIndex()); 
		return getParentPrefixTitle() + "-" + s; 
	}
	
	/**
	 * Tra ve title chua co so. Vd : Danh sach hinh anh
	 * 
	 * @author: QuangVT1
	 * @since: 6:19:54 PM Jan 13, 2014
	 * @return: String
	 * @throws:  
	 * @return
	 */
	protected String getTitleString(){
		return Constants.STR_BLANK;
	}
	
	/**
	 * Tra ve title hoan hinh
	 * 
	 * @author: QuangVT1
	 * @since: 6:19:57 PM Jan 13, 2014
	 * @return: String
	 * @throws:  
	 * @return
	 */
	final protected String getFullTitleString(){
		return getMenuIndexString() + ". " + getTitleString();
	}

	/**
	 * Request ghi log KPI
	 * 
	 * @param type
	 * @param startTimeKPIFromBoot
	 */
	public void requestInsertLogKPI(HashMapKPI type, long startTimeKPIFromBoot) {
		// nếu fragment còn sống + fragment đang hiển thị, thì mới được gởi KPI.
		if (this != null && (isFirstRequestKPI || this.isVisible())) {
			isFirstRequestKPI = false;
			GlobalUtil.requestInsertLogKPI(type, startTimeKPIFromBoot);
		} else {
			// nếu fragment bị null, hoặc fragment đang bị ẩn thì ko cho gởi KPI
			// LOG.
			long endFromBoot = SystemClock.elapsedRealtime();
			long totalExecutationTime = endFromBoot - startTimeKPIFromBoot;
			ServerLogger.sendLog("KPI FRAGMENT HIDE: " + type.getNote()
					+ " TOTAL: " + totalExecutationTime + " TIME: "
					+ DateUtils.getRightTimeNow(), TabletActionLogDTO.LOG_CLIENT);
		}
	}

	 /**
	 * Khoi tao header table
	 * @author: Tuanlt11
	 * @param tb
	 * @param header
	 * @return: void
	 * @throws:
	*/
	protected void initHeaderTable(DMSTableView tb, DMSTableRow header){
		tb.addHeader(header);
	}

	protected void restartLocatingUpdate() {
		boolean isNeedWaiting = !GlobalInfo.getInstance().getProfile().getMyGPSInfo().isVaildLocation();
		if (activity != null && !activity.isFinishing()){
			activity.restartLocating(isNeedWaiting);
		}
	}
}
