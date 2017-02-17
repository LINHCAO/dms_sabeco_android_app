package com.viettel.dms.view.supervisor.training;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.FragmentMenuContanst;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.StaffCustomerDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.CustomerListDTO;
import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.dto.view.ListStaffDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.sale.customer.CustomerListRow;
import com.viettel.dms.view.supervisor.CustomerSaleListRow;
import com.viettel.map.dto.LatLng;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

import java.util.ArrayList;

/**
 * Màn hình huấn luyện GSNPP
 * Created by yennth16 on 10/12/2016.
 */
public class TrainingListView extends BaseFragment implements OnEventControlListener, View.OnClickListener,
        VinamilkTableListener, AdapterView.OnItemSelectedListener {
    private static final int ACTION_OK = -1;
    private static final int ACTION_CANCEL = -2;
    public static final int ACTION_POS_OK = -3;
    private static final int ACTION_OK_2 = -4;
    public static final String TAG = TrainingListView.class.getName();
    private GlobalBaseActivity parent; // parent
    public CustomerListDTO cusDto;// cusList
    private DMSTableView tbCusList;// tbCusList
    private VNMEditTextClearable edMKH;// edMKH
    private VNMEditTextClearable edTKH;// edTKH
    private Spinner spinnerLine;// spinnerLine
    private Button btSearch; // btSearch
    private int currentPage = -1;
    // string luu ma khach hang
    private String textCusCode = "";
    // string luu ten khach hang
    private String textCusName = "";
    private int currentSpinerSelectedItem = -1;
    private boolean isUpdateData = false;
    private boolean isBack = false;
    public boolean isBackFromPopStack = false;
    CountDownTimer timer = null;
    boolean isSearchOrSelectSpiner = false;
    private boolean isRequestCusList = false;
    Spinner spNVBH;// spiner list nhan vien ban hang
    Spinner spGSNPP;// spiner list giam sat
    // list ds nhan vien o spiner
    private String[] arrNVBHChoose;
    // list ds gsnpp o spiner
    private String[] arrGSNPPChoose;
    // danh sach nhan vien ban hang
    private ListStaffDTO dtoListStaff;
    // danh sach gsnpp
    private ListStaffDTO dtoListGSNPP;
    private String textStaffId = "";
    int currentNVBH = -1;// KH dang chon
    int currentTuyen = -1;// tuyen dang chon
    int currentGSNPP = -1;// GSNPP dang chon
    private String textGSNPPId = "";
    private String textGSNPPShopId = "";
    String staffIdGSAll = "";
    String shopIdGSAll = "";
    private boolean isFirst = true;// vo man hinh lan dau tien hay kg?
    public boolean isBackFromPopStackNot = false;
    public boolean isNotResetValue = false;
    public static TrainingListView newInstance(Bundle args ) {
        TrainingListView f = new TrainingListView();
        // Supply index input as an argument.
        args = (args !=null ? args : new Bundle());
        args.putBoolean("isReload", true);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = (GlobalBaseActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_training_list_view, container, false);
        View v = super.onCreateView(inflater, view, savedInstanceState);
        enableMenuBar(this);
        setTitleHeaderView(StringUtil.getString(R.string.TITLE_TRAINING));
        if (!isBackFromPopStack) {
            initView(v);
            initspLine();
            if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSNPP) {
                initSpGSNPPSup();
                if (isFirst) {
                    getListSaleStaff();
                    isFirst = false;
                } else if(currentPage < 1){
                    isNotResetValue = true;
                    initSpNVBH();
                    currentPage = 1;
                    getCustomerSaleList(currentPage);
                } else {
                    initSpNVBH();
                    renderLayout();
                }
            } else if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSTINH) {
                if (isFirst) {
                    getListGSNPPByGSBH(GlobalInfo.getInstance().getProfile().getUserData().id);
                    isFirst = false;
                }  else if(currentPage < 1){
                    isNotResetValue = true;
                    initSpGSNPP();
                    initSpNVBH();
                    currentPage = 1;
                    getCustomerSaleList(currentPage);
                } else {
                    initSpGSNPP();
                    initSpNVBH();
                    renderLayout();
                }
            }
        }else{
            isBackFromPopStack = false;
            spNVBH.setSelection(currentNVBH);
            spinnerLine.setSelection(currentTuyen);
            spGSNPP.setSelection(currentGSNPP);
        }
        return v;
    }
    private void initView(View v) {
        spGSNPP = (Spinner) v.findViewById(R.id.spGSNPP);
        spGSNPP.setOnItemSelectedListener(this);
        spNVBH = (Spinner) v.findViewById(R.id.spNVBH);
        spNVBH.setOnItemSelectedListener(this);
        edMKH = (VNMEditTextClearable) v.findViewById(R.id.edCusCode);
        edTKH = (VNMEditTextClearable) v.findViewById(R.id.edCusName);
        spinnerLine = (Spinner) v.findViewById(R.id.spinnerPath);
        spinnerLine.setOnItemSelectedListener(this);
        btSearch = (Button) v.findViewById(R.id.btSearch);
        btSearch.setOnClickListener(this);
        tbCusList = (DMSTableView) v.findViewById(R.id.tbCusList);
        tbCusList.setListener(this);
        if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSNPP) {
            initHeaderTable(tbCusList, new TrainingListRowSup(parent, this));
        } else if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSTINH) {
            initHeaderTable(tbCusList, new TrainingListRow(parent, this));
        }
        // reset lai bien isInsertingActionLogVisit
        parent.isInsertingActionLogVisit = false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Lấy danh sách nhân viên bán hàng
     */
    private void getListSaleStaff() {
        ActionEvent e = new ActionEvent();
        Bundle b = new Bundle();
        b.putString(IntentConstants.INTENT_SHOP_ID,String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId));
        b.putString(IntentConstants.INTENT_STAFF_OWNER_ID,String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
        b.putBoolean(IntentConstants.INTENT_ORDER, true);
        b.putBoolean(IntentConstants.INTENT_IS_ALL, false);
        e.viewData = b;
        e.action = ActionEventConstant.GET_TRAINING_LIST_NVBH;
        e.sender = this;
        SuperviorController.getInstance().handleViewEvent(e);
    }
    /**
     * Lấy danh sách nhân viên bán hàng
     */
    private void getListSaleStaffGSBH() {
        ActionEvent e = new ActionEvent();
        Bundle b = new Bundle();
        b.putString(IntentConstants.INTENT_STAFF_OWNER_ID,
                String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
        b.putString(IntentConstants.INTENT_STAFF_ID, textGSNPPId);
        b.putBoolean(IntentConstants.INTENT_IS_ALL, false);
        e.viewData = b;
        e.action = ActionEventConstant.GET_LIST_NVBH_BY_GSBH;
        e.sender = this;
        SuperviorController.getInstance().handleViewEvent(e);
    }

    /**
     * Lấy danh sách giám sát NPP
     * @param staffId
     */
    private void getListGSNPPByGSBH(long staffId) {
        ActionEvent e = new ActionEvent();
        Bundle b = new Bundle();
        b.putString(IntentConstants.INTENT_STAFF_OWNER_ID,
                String.valueOf(staffId));
        b.putBoolean(IntentConstants.INTENT_IS_ALL, false);
        e.viewData = b;
        e.action = ActionEventConstant.GET_LIST_GSNPP_BY_GSBH;
        e.sender = this;
        SuperviorController.getInstance().handleViewEvent(e);
    }

    /**
     * Lấy danh sách khách hàng
     * @param page
     */
    private void getCustomerSaleList(int page) {
        parent.showLoadingDialog();
        ActionEvent e = new ActionEvent();
        Bundle bundle = new Bundle();
        bundle.putInt(IntentConstants.INTENT_PAGE, page);
        bundle.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
        bundle.putString(IntentConstants.INTENT_VISIT_PLAN, DateUtils.getVisitPlan(Constants.ARRAY_LINE_CHOOSE[currentTuyen]));
        bundle.putString(IntentConstants.INTENT_CUSTOMER_CODE, textCusCode);
        bundle.putString(IntentConstants.INTENT_CUSTOMER_NAME, StringUtil.getEngStringFromUnicodeString(textCusName));
        bundle.putString(IntentConstants.INTENT_STAFF_ID, textStaffId);
        if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSNPP) {
            bundle.putString(IntentConstants.INTENT_PARENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopIdProfile);
            bundle.putString(IntentConstants.INTENT_USER_ID, "" + GlobalInfo.getInstance().getProfile().getUserData().id);
            bundle.putString(IntentConstants.INTENT_ID, "" + GlobalInfo.getInstance().getProfile().getUserData().id);
        } else if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSTINH) {
            bundle.putString(IntentConstants.INTENT_PARENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
            if(!StringUtil.isNullOrEmpty(textGSNPPShopId) && !StringUtil.isNullOrEmpty(textGSNPPId)) {
                bundle.putString(IntentConstants.INTENT_USER_ID, "" + textGSNPPId);
                bundle.putBoolean(IntentConstants.INTENT_IS_ALL, false);
                bundle.putString(IntentConstants.INTENT_ID, "" + GlobalInfo.getInstance().getProfile().getUserData().id);
            }else if(StringUtil.isNullOrEmpty(textGSNPPShopId) && StringUtil.isNullOrEmpty(textGSNPPId)) {
                bundle.putString(IntentConstants.INTENT_USER_ID, "" + staffIdGSAll);
                bundle.putBoolean(IntentConstants.INTENT_IS_ALL, true);
                bundle.putString(IntentConstants.INTENT_ID, "" + GlobalInfo.getInstance().getProfile().getUserData().id);
            }else{
                bundle.putString(IntentConstants.INTENT_USER_ID, "" + GlobalInfo.getInstance().getProfile().getUserData().id);
                bundle.putBoolean(IntentConstants.INTENT_IS_ALL, false);
                bundle.putString(IntentConstants.INTENT_ID, "" + GlobalInfo.getInstance().getProfile().getUserData().id);
            }
        }
        e.viewData = bundle;
        e.action = ActionEventConstant.ACTION_GET_CUSTOMER_SALE_LIST_TRAINING;
        e.sender = this;
        SaleController.getInstance().handleViewEvent(e);
    }
    /**
     * khoi tao view list NVBH
     *
     * @author yennth16
     */
    private void initspLine() {
        SpinnerAdapter adapterLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item,
                Constants.ARRAY_LINE_CHOOSE);
        spinnerLine.setAdapter(adapterLine);
        if(isFirst) {
            currentTuyen = DateUtils.getCurrentDay();// them tat ca o dau
            spinnerLine.setSelection(currentTuyen);
        }
        int temp = currentTuyen;
        if(isBackFromPopStackNot || isNotResetValue){
            spinnerLine.setSelection(temp);
            if(isNotResetValue){
                currentTuyen = temp;
            }
        }
    }
    int tempCurrentNVBH = -1;
    String staffIdNVAll = "";
    /**
     * khoi tao view list NVBH
     *
     * @author yennth16
     */
    private void initSpNVBH() {
        ArrayList<String> listProductCode = new ArrayList<String>();
        arrNVBHChoose = new String[dtoListStaff.arrList.size()];
        int i = 0;
        for (ListStaffDTO.StaffItem staffItem : dtoListStaff.arrList) {
            if (!StringUtil.isNullOrEmpty(staffItem.code)) {
                arrNVBHChoose[i] = staffItem.code + "-" + staffItem.name;
            } else {
                arrNVBHChoose[i] = staffItem.name;
            }
            listProductCode.add(staffItem.id);
            i++;
        }
        staffIdNVAll = TextUtils.join(", ", listProductCode);
        SpinnerAdapter adapterNVBH = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrNVBHChoose);
        spNVBH.setAdapter(adapterNVBH);
        int temp = -1;
        temp = currentNVBH;
        currentNVBH = spNVBH.getSelectedItemPosition();
        if(isBackFromPopStackNot|| isNotResetValue){
            tempCurrentNVBH = temp;
            spNVBH.setSelection(tempCurrentNVBH);
            if(isNotResetValue){
                currentNVBH = tempCurrentNVBH;
                spinnerLine.setSelection(currentTuyen);
            }
            isNotResetValue = false;
            if(reset) {
                isBackFromPopStackNot = false;
                reset = false;
            }
        }
        if (currentNVBH < dtoListStaff.arrList.size()) {
            if(dtoListStaff.arrList.size() > 0) {
                textStaffId = dtoListStaff.arrList.get(spNVBH.getSelectedItemPosition()).id;
            }else{
                textStaffId = "";
            }
        }
    }

    /**
     * khoi tao view list GSNPP
     */
    private void initSpGSNPP() {
        ArrayList<String> liStstaffIdGSAll = new ArrayList<String>();
        ArrayList<String> listShopIdGSAll = new ArrayList<String>();
        arrGSNPPChoose = new String[dtoListGSNPP.arrList.size()];
        int i = 0;
        for (ListStaffDTO.StaffItem staffItem : dtoListGSNPP.arrList) {
            if (!StringUtil.isNullOrEmpty(staffItem.code)) {
                arrGSNPPChoose[i] = staffItem.code + "-" + staffItem.name;
            } else {
                arrGSNPPChoose[i] = staffItem.name;
            }
            if (!StringUtil.isNullOrEmpty(staffItem.shopId) && !StringUtil.isNullOrEmpty(staffItem.id)) {
                liStstaffIdGSAll.add(staffItem.id);
                listShopIdGSAll.add(staffItem.shopId);
            }
            i++;
        }
        staffIdGSAll = TextUtils.join(", ", liStstaffIdGSAll);
        shopIdGSAll = TextUtils.join(", ", listShopIdGSAll);
        SpinnerAdapter adapterGSNPP = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrGSNPPChoose);
        spGSNPP.setAdapter(adapterGSNPP);
        int temp = currentGSNPP;
        currentGSNPP = spGSNPP.getSelectedItemPosition();
        if(isBackFromPopStackNot || isNotResetValue){
            spGSNPP.setSelection(temp);
            if(isNotResetValue){
                currentGSNPP = temp;
            }
        }
        if (currentGSNPP < dtoListGSNPP.arrList.size()) {
            textGSNPPId = dtoListGSNPP.arrList.get(spGSNPP.getSelectedItemPosition()).id;
            textGSNPPShopId = dtoListGSNPP.arrList.get(spGSNPP.getSelectedItemPosition()).shopId;
        } else {
            textGSNPPId = "";
            textGSNPPShopId = "";
        }
    }
    /**
     * khoi tao view list GSNPP
     */
    private void initSpGSNPPSup() {
        arrGSNPPChoose = new String[1];
        arrGSNPPChoose[0] = GlobalInfo.getInstance().getProfile().getUserData().userCode + "-" + GlobalInfo.getInstance().getProfile().getUserData().displayName;
        SpinnerAdapter adapterGSNPP = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrGSNPPChoose);
        spGSNPP.setAdapter(adapterGSNPP);
        currentGSNPP = spGSNPP.getSelectedItemPosition();
    }

    /**
     * titleString
     * @param dayIndex
     * @return
     */
    public SpannableObject titleString(int dayIndex) {
        if (dayIndex < 0) {
            dayIndex = DateUtils.getCurrentDay();
        }
        String today = DateUtils.getVisitPlan(Constants.ARRAY_LINE_CHOOSE[dayIndex]);
        String st = "(" + StringUtil.getString(R.string.TEXT_IN_ROUTE) + " " + today + ")";

        SpannableObject obj = new SpannableObject();
        obj.addSpan(StringUtil.getString(R.string.TITLE_VIEW_CUSTOMER) + Constants.STR_SPACE,
                ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
        obj.addSpan(st, ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);
        return obj;
    }

    @Override
    public void handleModelViewEvent(ModelEvent modelEvent) {
        ActionEvent e = modelEvent.getActionEvent();
        switch (e.action) {
            case ActionEventConstant.GET_LIST_GSNPP_BY_GSBH:
                dtoListGSNPP = (ListStaffDTO) modelEvent.getModelData();
                if (dtoListGSNPP != null) {
                    initSpGSNPP();
                    getListSaleStaffGSBH();
                }
                break;
            case ActionEventConstant.GET_TRAINING_LIST_NVBH:
                dtoListStaff = (ListStaffDTO) modelEvent.getModelData();
                if (dtoListStaff != null) {
                    initSpNVBH();
                    getCustomerSaleList(1);
                }
                break;
            case ActionEventConstant.GET_LIST_NVBH_BY_GSBH:
                dtoListStaff = (ListStaffDTO) modelEvent.getModelData();
                if (dtoListStaff != null) {
                    initSpNVBH();
                    getCustomerSaleList(1);
                }
                break;
            case ActionEventConstant.ACTION_GET_CUSTOMER_SALE_LIST_TRAINING:
                CustomerListDTO tempDto = (CustomerListDTO) modelEvent.getModelData();
                if (cusDto == null) {
                    cusDto = tempDto;
                } else {
                    cusDto.cusList = tempDto.cusList;
                }

                if (cusDto != null) {
                    renderLayout();
                }
                parent.closeProgressDialog();
                requestInsertLogKPI(HashMapKPI.GSNPP_DIEMBAN, modelEvent.getActionEvent().startTimeFromBoot);
                break;
            case ActionEventConstant.GET_CUSTOMER_LIST:
                if (isSearchOrSelectSpiner) {
                    isSearchOrSelectSpiner = false;
                    cusDto = null;
                    currentPage = -1;
                }
                CustomerListDTO tempDto1 = (CustomerListDTO) modelEvent.getModelData();
                if (cusDto == null) {
                    cusDto = tempDto1;
                } else {
                    cusDto.cusList = tempDto1.cusList;
                    //cusDto.distance = tempDto.distance;
                }

                if (isUpdateData) {
                    isUpdateData = false;
                    currentPage = -1;
                    cusDto.totalCustomer = tempDto1.totalCustomer;
                }

                if (cusDto != null) {
                    renderLayout();
                }
                parent.closeProgressDialog();
                isRequestCusList = false;
                requestInsertLogKPI(HashMapKPI.NVBH_DANHSACHKHACHHANG, e.startTimeFromBoot);
                break;
            case ActionEventConstant.UPDATE_ACTION_LOG:
                GlobalInfo.getInstance().getProfile().setVisitingCustomer(false);
                GlobalInfo.getInstance().getProfile().setActionLogVisitCustomer((ActionLogDTO) e.viewData);
                processOrder((CustomerListItem) e.userData);
                break;
            default:
                super.handleModelViewEvent(modelEvent);
                break;
        }
    }

    @Override
    public void handleErrorModelViewEvent(ModelEvent modelEvent) {
        switch (modelEvent.getActionEvent().action){
            case ActionEventConstant.GET_CUSTOMER_LIST: {
                isRequestCusList = false;
            }
            break;
        }
        parent.closeProgressDialog();
        super.handleErrorModelViewEvent(modelEvent);
    }

    /**
     * renderLayout
     */
    private void renderLayout() {
        if (currentPage != -1) {
            if (isBack) {
                isBack = false;
            }
            tbCusList.setTotalSize(cusDto.totalCustomer, currentPage);
            tbCusList.getPagingControl().setCurrentPage(currentPage);
        } else {
            tbCusList.setTotalSize(cusDto.totalCustomer, 1);
            currentPage = tbCusList.getPagingControl().getCurrentPage();
        }

        int pos = 1 + Constants.NUM_ITEM_PER_PAGE * (tbCusList.getPagingControl().getCurrentPage() - 1);
        int size = cusDto.cusList.size();
        tbCusList.clearAllData();
        if (size > 0) {
            if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSNPP) {
                for (CustomerListItem item: cusDto.cusList) {
                    TrainingListRowSup row = new TrainingListRowSup(parent, this);
                    row.render(pos, item);
                    pos++;
                    tbCusList.addRow(row);
                }
            }else{
                for (CustomerListItem item: cusDto.cusList) {
                    TrainingListRow row = new TrainingListRow(parent, this);
                    row.render(pos, item);
                    pos++;
                    tbCusList.addRow(row);
                }
            }
        }else{
            tbCusList.showNoContentRow();
        }
    }

    /**
     * di toi man hinh chi tiet Khach hang
     */
    public void gotoCustomerInfo(String customerId) {
        ActionEvent e = new ActionEvent();
        e.sender = this;
        Bundle bunde = new Bundle();
        bunde.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);
        bunde.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "02-01");
        bunde.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getMenuIndexString());
        e.viewData = bunde;
        e.action = ActionEventConstant.GO_TO_CUSTOMER_INFO;
        UserController.getInstance().handleSwitchFragment(e);
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {

        switch (eventType) {
            case ACTION_OK:
                CustomerListItem item = (CustomerListItem) data;
                // ket thuc ghe tham
                parent.requestUpdateActionLog("0", null, item, this);
                break;
            case ACTION_POS_OK:
                break;
            case ACTION_OK_2:
                cusDto = null;
                currentPage = -1;
                getCustomerSaleList(1);
                break;

            default:
                super.onEvent(eventType, control, data);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSearch:
                isSearchOrSelectSpiner = true;
                textCusCode = edMKH.getText().toString().trim();
                textCusName = edTKH.getText().toString().trim();
                currentTuyen=spinnerLine.getSelectedItemPosition();
                currentNVBH = spNVBH.getSelectedItemPosition();
                currentPage = -1;
                cusDto = null;
                if(dtoListStaff.arrList.size() > 0) {
                    textStaffId = dtoListStaff.arrList.get(spNVBH.getSelectedItemPosition()).id;
                }else{
                    textStaffId = "0";
                }
                // hide ban phim
                GlobalUtil.forceHideKeyboard(parent);
                getCustomerSaleList(1);
                break;
            default:
                break;
        }

    }

    /**
     * ghe tham
     * @param item
     * @return: voidvoid
     * @throws:
     */
    private void processOrder(CustomerListItem item) {
        if (item.isOr == 0) {// KH trong tuyen
            gotoCustomerInfo(item.aCustomer.getCustomerId());
            if (item.visitStatus != CustomerListItem.VISIT_STATUS.VISITED_FINISHED) {
                if (item.visitStatus == CustomerListItem.VISIT_STATUS.VISITED_CLOSED) {
                    saveLastVisitToActionLogProfile(item);
                    parent.initCustomerNameOnMenuVisit(item.aCustomer.shortCode, item.aCustomer.customerName);
                } else if (item.visitStatus == CustomerListItem.VISIT_STATUS.VISITING) {
                    saveLastVisitToActionLogProfile(item);
                    parent.initMenuVisitCustomer(item.aCustomer.shortCode, item.aCustomer.customerName);
                    if (item.isTodayCheckedRemain || item.isTodayVoted || item.isTodayOrdered) {
                        // da ghe tham, da dat hang, da cham trung bay, da kiem
                        // hang
                        // ton trong ngay thi an menu dong cua
                        parent.removeMenuCloseCustomer();
                    }
                } else {// VISIT_STATUS.NONE_VISIT
                    parent.initMenuVisitCustomer(item.aCustomer.shortCode, item.aCustomer.customerName);
                    parent.requestStartInsertVisitActionLog(item);
                    item.visitStatus = CustomerListItem.VISIT_STATUS.VISITING;
                }
            } else {// neu da ghe tham thi chi hien thi title Ghe tham
                parent.initCustomerNameOnMenuVisit(item.aCustomer.shortCode, item.aCustomer.customerName);
                saveLastVisitToActionLogProfile(item);
            }
            // check to update EXCEPTION_ORDER_DATE: bien cho phep dat hang khi
            // khoach cach qua xa
            if (!StringUtil.isNullOrEmpty(item.exceptionOrderDate)) {
                requestUpdateExceptionOrderDate(item);
            }
            // set toa do khach hang dang ghe tham de toi uu dinh vi
            GlobalInfo.getInstance().setPositionCustomerVisiting(new LatLng(item.aCustomer.lat, item.aCustomer.lng));
        } else {// KH ngoai tuyen
            gotoCustomerInfo(item.aCustomer.getCustomerId());
            if (item.visitStatus == CustomerListItem.VISIT_STATUS.VISITED_FINISHED) {
                parent.initCustomerNameOnMenuVisit(item.aCustomer.shortCode, item.aCustomer.customerName);
            } else {
                parent.initMenuVisitCustomer(item.aCustomer.shortCode, item.aCustomer.customerName);
            }
            parent.removeMenuCloseCustomer();
            parent.removeMenuFinishCustomer();
            if (item.visitStatus == CustomerListItem.VISIT_STATUS.NONE_VISIT) {
                parent.requestStartInsertVisitActionLog(item);
                item.visitStatus = CustomerListItem.VISIT_STATUS.VISITING;
            } else {
                saveLastVisitToActionLogProfile(item);
            }
        }
        resetAllValue();
        cusDto = null;
    }

    /**
     * //check to update EXCEPTION_ORDER_DATE: bien cho phep dat hang khi khoach
     * cach qua xa
     * @return: voidvoid
     * @throws:
     */
    private void requestUpdateExceptionOrderDate(CustomerListItem item) {
        StaffCustomerDTO staffCusDto = new StaffCustomerDTO();
        staffCusDto.staffCustomerId = item.staffCustomerId;

        ActionEvent e = new ActionEvent();
        e.sender = this;
        e.action = ActionEventConstant.UPDATE_EXCEPTION_ORDER_DATE;
        e.viewData = staffCusDto;
        e.isNeedCheckTimeServer = false;
        SaleController.getInstance().handleViewEvent(e);

    }

    /**
     * saveLastVisitToActionLogProfile
     * @param item
     */
    private void saveLastVisitToActionLogProfile(CustomerListItem item) {
        ActionLogDTO action = new ActionLogDTO();
        // chu y ko co id cua action_log
        action.id = item.visitActLogId;
        action.aCustomer.customerId = item.aCustomer.customerId;
        action.aCustomer.customerName = item.aCustomer.customerName;
        action.aCustomer.customerCode = item.aCustomer.customerCode;
        action.aCustomer.shortCode = item.aCustomer.shortCode;
        action.startTime = item.visitStartTime;
        action.endTime = item.visitEndTime;
        action.isOr = item.isOr;
        action.createUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
        action.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
        action.lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLastLatitude();
        action.lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLastLongtitude();
        action.objectType = "0";
        action.distance = item.cusDistance;
        GlobalInfo.getInstance().getProfile().setActionLogVisitCustomer(action);
    }
    /**
     * resetAllValue
     */
    private void resetAllValue() {
        spinnerLine.setSelection(DateUtils.getCurrentDay());
        currentSpinerSelectedItem = spinnerLine.getSelectedItemPosition();
        textCusName = "";
        textCusCode = "";
        edMKH.setText(textCusCode);
        edTKH.setText(textCusName);
        currentPage = -1;
        cusDto = null;
    }

    @Override
    public void handleVinamilkTableloadMore(View control, Object data) {
        if (control == tbCusList) {
            currentPage = tbCusList.getPagingControl().getCurrentPage();
            getCustomerSaleList(currentPage);
        }
    }

    @Override
    public void handleVinamilkTableRowEvent(int act, View control, Object data) {
        switch (act) {
            case ActionEventConstant.GO_TO_CUSTOMER_INFO: {
                CustomerListItem item = (CustomerListItem) data;
                currentPage = tbCusList.getPagingControl().getCurrentPage();
                isBack = true;
                gotoCustomerInfo(item.aCustomer.getCustomerId());
                break;
            }
            case ActionEventConstant.VISIT_CUSTOMER: {
                CustomerListItem item = (CustomerListItem) data;
                currentPage = tbCusList.getPagingControl().getCurrentPage();
                isBack = true;
                if (isValidDistanceWhenBeginVisitation(item)) {
                    ActionLogDTO action = GlobalInfo.getInstance().getProfile().getActionLogVisitCustomer();
                    if (action != null && action.aCustomer.customerId != item.aCustomer.customerId
                            && DateUtils.compareDate(action.startTime, DateUtils.now()) == 0
                            && StringUtil.isNullOrEmpty(action.endTime)) {
                        // kiem tra neu vao lai dung khach hang do thi khong
                        // insertlog
                        SpannableObject textConfirmed = new SpannableObject();
                        textConfirmed.addSpan(StringUtil
                                        .getString(R.string.TEXT_ALREADY_VISIT_CUSTOMER),
                                ImageUtil.getColor(R.color.WHITE),
                                android.graphics.Typeface.NORMAL);
                        if(!StringUtil.isNullOrEmpty(action.aCustomer.customerCode)) {
                            textConfirmed.addSpan(" " + action.aCustomer.customerCode.substring(0, 3),
                                    ImageUtil.getColor(R.color.WHITE),
                                    android.graphics.Typeface.BOLD);
                        }
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

                        GlobalUtil.showDialogConfirmCanBackAndTouchOutSide(this, parent, textConfirmed.getSpan(),
                                StringUtil.getString(R.string.TEXT_AGREE), ACTION_OK,
                                StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), ACTION_CANCEL, item, true, true);
                    } else {
                        processOrder(item);
                    }
                } else {
                    String mess = Constants.STR_BLANK;
                    if(!StringUtil.isNullOrEmpty(item.aCustomer.customerCode)) {
                        mess = StringUtil.getString(R.string.TEXT_TOO_FAR_FROM_CUS_1) + " "
                                + item.aCustomer.customerName + " - " + item.aCustomer.customerCode.substring(0, 3)
                                + StringUtil.getString(R.string.TEXT_TOO_FAR_FROM_CUS_2);
                    }else{
                        mess = StringUtil.getString(R.string.TEXT_TOO_FAR_FROM_CUS_1) + " "
                                + item.aCustomer.customerName
                                + StringUtil.getString(R.string.TEXT_TOO_FAR_FROM_CUS_2);
                    }

                    GlobalUtil.showDialogConfirm(this, parent, mess, StringUtil.getString(R.string.TEXT_BUTTON_CLOSE),
                            ACTION_OK_2, item, false);
                }
                break;
            }
            default:
                break;
        }
    }

    /**
     * K tra lai khoang cach khi bat dau ghe tham
     * @param item
     * @return: voidvoid
     * @throws:
     */
    private boolean isValidDistanceWhenBeginVisitation(CustomerListItem item) {
        boolean isValid = true;
        item.isTooFarShop = false;
        if(cusDto != null){
            if (item.isOr == 0 && !item.isVisit() && !item.canOrderException()) {
                item.updateCustomerDistance();
                item.isTooFarShop = false;
                isValid = !item.isTooFarShop;
            }
        }else{
            isValid = false;
        }
        return isValid;
    }
    boolean reset = false;
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (arg0 == spNVBH) {
            if (currentNVBH != spNVBH.getSelectedItemPosition()) {
                textCusCode = edMKH.getText().toString().trim();
                textCusName = edTKH.getText().toString().trim();
                currentNVBH = spNVBH.getSelectedItemPosition();
                currentTuyen=spinnerLine.getSelectedItemPosition();
                currentGSNPP = spGSNPP.getSelectedItemPosition();
                currentPage = -1;
                cusDto = null;
                if(dtoListStaff.arrList.size() > 0) {
                    textStaffId = dtoListStaff.arrList.get(spNVBH.getSelectedItemPosition()).id;
                    textGSNPPId = dtoListStaff.arrList.get(spNVBH.getSelectedItemPosition()).staffOwnerId;
                    textGSNPPShopId = dtoListStaff.arrList.get(spNVBH.getSelectedItemPosition()).shopId;
                }else{
                    textStaffId = "0";
                    textGSNPPId = "0";
                    textGSNPPShopId = "0";
                }
                getCustomerSaleList(1);
            }
        } else if (arg0 == spinnerLine) {
            if(currentTuyen!=spinnerLine.getSelectedItemPosition()){
                textCusCode = edMKH.getText().toString().trim();
                textCusName = edTKH.getText().toString().trim();
                currentTuyen=spinnerLine.getSelectedItemPosition();
                currentNVBH = spNVBH.getSelectedItemPosition();
                currentGSNPP = spGSNPP.getSelectedItemPosition();
                currentPage = -1;
                cusDto = null;
                if(dtoListStaff.arrList.size() > 0) {
                    textStaffId = dtoListStaff.arrList.get(spNVBH.getSelectedItemPosition()).id;
                    textGSNPPId = dtoListStaff.arrList.get(spNVBH.getSelectedItemPosition()).staffOwnerId;
                    textGSNPPShopId = dtoListStaff.arrList.get(spNVBH.getSelectedItemPosition()).shopId;
                }else{
                    textStaffId = "0";
                    textGSNPPId = "0";
                    textGSNPPShopId = "0";
                }
                getCustomerSaleList(1);
            }
        } else if (arg0 == spGSNPP) {
            if (currentGSNPP != spGSNPP.getSelectedItemPosition()) {
                textCusCode = edMKH.getText().toString().trim();
                textCusName = edTKH.getText().toString().trim();
                currentTuyen=spinnerLine.getSelectedItemPosition();
                currentNVBH = spNVBH.getSelectedItemPosition();
                currentPage = -1;
                cusDto = null;
                currentGSNPP = spGSNPP.getSelectedItemPosition();
                textGSNPPId = dtoListGSNPP.arrList.get(spGSNPP.getSelectedItemPosition()).id;
                textGSNPPShopId = dtoListGSNPP.arrList.get(spGSNPP.getSelectedItemPosition()).shopId;
                textStaffId = "";
                getListSaleStaffGSBH();
                if(isBackFromPopStackNot) {
                    reset = true;
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @Override
    public void receiveBroadcast(int action, Bundle extras) {
        switch (action) {
            case ActionEventConstant.NOTIFY_REFRESH_VIEW:
                if (this.isVisible()) {
                    parent.showLoadingDialog();
                    isUpdateData = true;
                    resetAllValue();
                    getCustomerSaleList(1);
                }
                break;
            case ActionEventConstant.ACTION_UPDATE_POSITION:
                ArrayList<DMSTableRow> rows = tbCusList.getListChildRow();
                if (rows != null && !rows.isEmpty()) {
                    try {
                        for (DMSTableRow row : rows) {
                            CustomerListRow rowCus = (CustomerListRow) row;
                            rowCus.reRenderVisitStatus();
                        }
                    } catch (Exception ex){
                        VTLog.e("rerender status list cus", ex.getMessage());
                    }
                } else{
                    if (!isRequestCusList) {
                        getCustomerSaleList(1);
                    }
                }

                break;
            default:
                super.receiveBroadcast(action, extras);
                break;
        }
    }
}
