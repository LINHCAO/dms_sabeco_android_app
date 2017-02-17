package com.viettel.dms.view.pg;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.PGController;
import com.viettel.dms.dto.view.FindProductSaleOrderDetailViewDTO;
import com.viettel.dms.dto.view.ListOrderViewDTO;
import com.viettel.dms.dto.view.PGOrderViewDTO;
import com.viettel.dms.dto.view.PGSaleOrderDetailDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.TransactionProcessManager;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.PGActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

import java.util.ArrayList;

/**
 * Created by yennth16 on 8/12/2016.
 */
public class PGOrderView extends BaseFragment implements
        OnEventControlListener, View.OnClickListener, VinamilkTableListener, View.OnTouchListener {

    public static final String TAG = PGOrderView.class.getName();
    // table vinamilk list product
    VinamilkTableView tbProductOrderList;
    // button search
    Button btSave;
    // input order code
    VNMEditTextClearable etInputDateSale;
    // main activity
    private PGActivity parent;
    // flag when searching product
    boolean isSearchingProduct = false;
    // flag when load list product the first
    boolean isFirstLoadProduct = false;
    // limit row in page
    public static final int LIMIT_ROW_PER_PAGE = 10;
    // list product
    ListOrderViewDTO listDTO = new ListOrderViewDTO();
    // list product
    ListOrderViewDTO orgListDTO = new ListOrderViewDTO();
    private int mDay;// mDay
    private int mMonth;// mMonth
    private int mYear;// mYear
    private String saleDateTime = Constants.STR_BLANK;
    // dong y luu dong hang
    public static final int ACTION_SAVE_ORDER_OK = 1;
    // tu choi luu don hang
    public static final int ACTION_CANCEL_SAVE_ORDER = 2;
    public static final int ACTION_CLOSE = 3;
    private static final int MENU_ORDER = 4;
    private static final int MENU_REPORT = 5;
    public static final int ACTION_EXIST = 6;
    private boolean isEnable = false;
    // flag when load list product the first
    boolean isReLoadProduct = false;
    TextView tvCustomerVisited;// quan cham soc
    public static PGOrderView getInstance(Bundle data) {
        PGOrderView instance = new PGOrderView();
        instance.setArguments(data);
        instance.VIEW_NAME = StringUtil.getString(R.string.ADD_ORDER);
        return instance;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate( R.layout.layout_pg_order_list_view, container, false);
        View v = super.onCreateView(inflater, view, savedInstanceState);
        enableMenuBar(this);
        addMenuItem(StringUtil.getString(R.string.TEXT_MENU_SALES), R.drawable.menu_customer_icon, MENU_ORDER);
        addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_GENERAL_STATISTICS), R.drawable.icon_calendar, MENU_REPORT);
        setMenuItemFocus(1);
        initView(v);
        setTitleHeaderView("");
        isFirstLoadProduct = true;
        isReLoadProduct = true;
        getListProductAddOrder(0,true);
        if(!GlobalUtil.checkIsTablet(parent)) {
            parent.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else{
            parent.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        return v;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            parent = (PGActivity) activity;
        } catch (Exception e) {
            VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
        }

    }
    /**
     * initView
     * @param v
     */
    public void initView(View v) {
        tbProductOrderList = (VinamilkTableView) v.findViewById(R.id.tbProductOrderList);
        tbProductOrderList.setListener(this);
        tbProductOrderList.setNumItemsPage(LIMIT_ROW_PER_PAGE);
        btSave = (Button) v.findViewById(R.id.btSave);
        btSave.setOnClickListener(this);
        TextView tvTextSale = (TextView) v.findViewById(R.id.tvTextSale);
        tvTextSale.requestFocus();
        etInputDateSale = (VNMEditTextClearable) v.findViewById(R.id.etInputDateSale);
        etInputDateSale.setText(DateUtils.getCurrentDate());
        etInputDateSale.setOnTouchListener(this);
        etInputDateSale.setIsHandleDefault(false);
        etInputDateSale.clearFocus();
        etInputDateSale.setFocusable(false);
        etInputDateSale.setImageClearVisibile(false);
        saleDateTime = DateUtils.convertDateOneFromFormatToAnotherFormat(etInputDateSale.getText().toString(),
                DateUtils.defaultDateFormat.toPattern(), DateUtils.defaultSqlDateFormat.toPattern());
        isEnable = true;
        tvCustomerVisited = (TextView) v.findViewById(R.id.tvCustomerVisited);
        GlobalInfo.getInstance().setListProduct(new ArrayList<PGOrderViewDTO>(), true);
        orgListDTO = new ListOrderViewDTO();
    }
    /**
     * Lay danh sach sna pham, don hang
     * @param numPage
     * @param isGetCount
     */
    private void getListProductAddOrder(int numPage, boolean isGetCount) {
        this.parent.showProgressDialog(getString(R.string.loading));
        ActionEvent e = new ActionEvent();
        Bundle data = new Bundle();
        String page = " limit " + (numPage * LIMIT_ROW_PER_PAGE) + ","
                + LIMIT_ROW_PER_PAGE;
        data.putString(IntentConstants.INTENT_PAGE, page);
        data.putString(IntentConstants.INTENT_SHOP_ID, String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId));
        data.putString(IntentConstants.INTENT_SALE_DATE, saleDateTime);
        data.putBoolean(IntentConstants.INTENT_IS_GET_NUM_TOTAL_ITEM, isGetCount);
        data.putBoolean(IntentConstants.INTENT_IS_FIRST_LOAD_PRODUCT_PG, isReLoadProduct);
        e.viewData = data;
        e.sender = this;
        e.action = ActionEventConstant.GET_PG_ORDER_VIEW;
        e.isNeedCheckTimeServer = false;
        PGController.getInstance().handleViewEvent(e);
    }
    /**
     * handle search product after user click product code / product name from
     * list combox textview
     * @return: void
     * @throws:
     * @date: Jan 8, 2013
     */
    private void searchProductFlowFillter() {
        GlobalUtil.forceHideKeyboardUseToggle(parent);
        for (int i = 0, size = this.orgListDTO.listObject.size(); i < size; i++) {
            this.orgListDTO.listObject.get(i).typeObject = FindProductSaleOrderDetailViewDTO.TYPE_OBJECT_SAVE_FILTER;
        }
    }

    /**
     * render layout for screen after get data from db
     * @return: void
     * @throws:
     */
    private void renderLayout() {
        if(!StringUtil.isNullOrEmpty(listDTO.customerDTO.customerName)){
            tvCustomerVisited.setText(listDTO.customerDTO.customerName);
        }else{
            tvCustomerVisited.setText("");
        }
        tbProductOrderList.clearAllData();
        if (isFirstLoadProduct) {
            initHeader();
        }
        int pos = 1;
        int numberItem = 0;
        ArrayList<PGOrderViewDTO> listObject = new ArrayList<>();
        GlobalInfo.getInstance().setListProduct(listObject, true);
        if (listDTO.listObject.size() > 0) {
            numberItem = listDTO.totalObject;
            tbProductOrderList.clearAllData();
            tbProductOrderList.hideNoContentRow();
            for (int i = 0, size = listDTO.listObject.size(); i < size; i++) {
                PGOrderViewDTO dto = listDTO.listObject.get(i);
                PGOrderListRow row = new PGOrderListRow(parent, this);
                row.setClickable(true);
                row.setTag(Integer.valueOf(pos));
                if(orgListDTO.listObject.size() ==0 && dto.quantity != null) {
                    if (!StringUtil.isNullOrEmpty(dto.quantity.toString()) && !dto.quantity.toString().equals("0")) {
                        for (int j = 0; j < orgListDTO.listObject.size(); j++) {
                            if (orgListDTO.listObject.get(j).productId != dto.productId) {
                                orgListDTO.listObject.add(dto);
                            }
                        }
                    }
                }
//                // update number order for product on view
                PGOrderViewDTO productSelected = this
                        .getNumberOrderForProduct(dto);
                if (productSelected != null) {
                    dto = productSelected;
                    listDTO.listObject.set(i, productSelected);
                }
                if (size == 1) {
                    row.renderLayout(pos + (tbProductOrderList.getPagingControl().getCurrentPage() - 1) * LIMIT_ROW_PER_PAGE, dto, true, isEnable, false);
                    GlobalUtil.showKeyboardUseToggle(parent);
                } else {
                    row.renderLayout( pos+ (tbProductOrderList.getPagingControl().getCurrentPage() - 1) * LIMIT_ROW_PER_PAGE, dto, false, isEnable, false);
                }
                pos++;
                tbProductOrderList.addRow(row);
            }
            GlobalInfo.getInstance().setListProduct(orgListDTO.listObject, false);
        } else {
            tbProductOrderList.showNoContentRow();
        }
    }

    /**
     * initHeader
     */
    public void initHeader(){
        int[] FIND_PRODUCT_TABLE_WIDTHS = { 50, 180, 100 };
        String[] FIND_PRODUCT_TABLE_TITLES = {
                StringUtil.getString(R.string.TEXT_STT),
                StringUtil.getString(R.string.TEXT_NAME_PRODUCT),
                StringUtil.getString(R.string.TEXT_SALES_2)};
        if(StringUtil.isNullOrEmpty(listDTO.customerDTOVisiting.customerName)){
            listDTO.customerDTOVisiting.customerName = "";
        }
        if(GlobalUtil.checkIsTablet(parent)) {
            parent.setStatusVisible(StringUtil.getString(R.string.TEXT_COLUM_NVTT_COLON)
                    + " " + GlobalInfo.getInstance().getProfile().getUserData().displayName
                    + "\n" + StringUtil.getString(R.string.TEXT_PG_CUSTOMER_VISIT) + " "
                    + listDTO.customerDTOVisiting.customerName, View.VISIBLE);
            FIND_PRODUCT_TABLE_WIDTHS = new int[] { 50, 730, 220};
        }else{
            parent.setStatusVisible(GlobalInfo.getInstance().getProfile().getUserData().displayName
                    + "\n" + listDTO.customerDTOVisiting.customerName, View.VISIBLE);
        }
        tbProductOrderList.getHeaderView().addColumns(
                FIND_PRODUCT_TABLE_WIDTHS,
                FIND_PRODUCT_TABLE_TITLES,
                ImageUtil.getColor(R.color.BLACK),
                ImageUtil.getColor(R.color.TABLE_HEADER_BG));
    }

    /**
     * Cập nhật heard thông tin nhân viên, quán chăm sóc
     */
    public void notifyHeader(){
        if(StringUtil.isNullOrEmpty(listDTO.customerDTOVisiting.customerName)){
            listDTO.customerDTOVisiting.customerName = "";
        }
        if(GlobalUtil.checkIsTablet(parent)) {
            parent.setStatusVisible(StringUtil.getString(R.string.TEXT_COLUM_NVTT_COLON)
                    + " " + GlobalInfo.getInstance().getProfile().getUserData().displayName
                    + "\n" + StringUtil.getString(R.string.TEXT_PG_CUSTOMER_VISIT) + " "
                    + listDTO.customerDTOVisiting.customerName, View.VISIBLE);
        }else{
            parent.setStatusVisible(GlobalInfo.getInstance().getProfile().getUserData().displayName
                    + "\n" + listDTO.customerDTOVisiting.customerName, View.VISIBLE);
        }
    }
    @Override
    public void handleModelViewEvent(ModelEvent modelEvent) {
        // TODO Auto-generated method stub
        ActionEvent actionEvent = modelEvent.getActionEvent();
        switch (actionEvent.action) {
            case ActionEventConstant.GET_PG_ORDER_VIEW:
                ListOrderViewDTO list = (ListOrderViewDTO) modelEvent.getModelData();
                listDTO = list;
                if(!listDTO.isCheckPlanLine){
                    this.parent.closeProgressDialog();
                    GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.TEXT_NOTIFY_NO_VISIT_PLAN),
                            StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), ACTION_EXIST, null, false);
                }else {
                    if(listDTO.customerDTO.customerId <= 0){
                        isEnable = false;
                        GlobalUtil.setEnableButton(btSave, isEnable);
                    }
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
                    if (isFirstLoadProduct) {
                        isFirstLoadProduct = false;
                    }
                    if(isReLoadProduct){
                        GlobalInfo.getInstance().setListAllProduct(listDTO.listAllOrder);
                        isReLoadProduct = false;
                    }
                    this.parent.closeProgressDialog();
                }
                break;
            case ActionEventConstant.CREATE_NEW_ORDER:
                getListProductAddOrder(tbProductOrderList.getPagingControl().getCurrentPage() - 1,false);
                GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.SAVE_ORDER_C2_SUCCESS),
                        StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), ACTION_CLOSE, null, false);
                break;
            default:
                super.handleModelViewEvent(modelEvent);
                break;
        }
    }

    @Override
    public void handleErrorModelViewEvent(ModelEvent modelEvent) {
        // TODO Auto-generated method stub
        ActionEvent actionEvent = modelEvent.getActionEvent();
        switch (actionEvent.action) {
            case ActionEventConstant.GET_PG_ORDER_VIEW:
                listDTO = new ListOrderViewDTO();
                orgListDTO = new ListOrderViewDTO();
                this.renderLayout();
                isFirstLoadProduct = false;
                isReLoadProduct = false;
                this.parent.closeProgressDialog();
                break;
            case ActionEventConstant.CREATE_NEW_ORDER:
                this.parent.closeProgressDialog();
                GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.SAVE_ORDER_C2_FAIL),
                        StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), ACTION_CLOSE, null, false);
                break;
            default:
                super.handleErrorModelViewEvent(modelEvent);
                break;
        }
    }
    @Override
    public void onEvent(int eventType, View control, Object data) {
        switch (eventType) {
            case ACTION_CANCEL_SAVE_ORDER: {
                break;
            }
            case ACTION_SAVE_ORDER_OK: {
                saveOrderList();
                break;
            }
            case  ACTION_CLOSE:{
                break;
            }
            case MENU_REPORT: {
                ActionEvent e = new ActionEvent();
                e.sender = this;
                e.viewData = new Bundle();
                e.action = ActionEventConstant.GO_TO_PG_REPORT_ORDER_VIEW;
                PGController.getInstance().handleSwitchFragment(e);
                break;
            }
            case ACTION_EXIST:
                sendBroadcast(ActionEventConstant.ACTION_FINISH_AND_SHOW_LOGIN,
                        new Bundle());
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btSave) {
            GlobalUtil.showDialogConfirm(this, this.activity,
                    StringUtil.getString(R.string.TEXT_CONFIRM_SAVE_ORDER_PG),
                    StringUtil.getString(R.string.TEXT_AGREE), ACTION_SAVE_ORDER_OK,
                    StringUtil.getString(R.string.TEXT_DENY), ACTION_CANCEL_SAVE_ORDER, null);
        }
    }
    @Override
    public void handleVinamilkTableloadMore(View control, Object data) {
        if (control == tbProductOrderList) {
            if (this.saveProductSelected(PGOrderViewDTO.TYPE_OBJECT_SAVE_NOT_FILTER)) {
                isSearchingProduct = false;
                getListProductAddOrder((tbProductOrderList.getPagingControl().getCurrentPage() - 1),  false);
            } else {
                this.tbProductOrderList.getPagingControl().setCurrentPage(this.tbProductOrderList.getPagingControl().getOldPage());
            }
        }
    }
    @Override
    public void handleVinamilkTableRowEvent(int action, View control, Object data) {
    }
    @Override
    protected void receiveBroadcast(int action, Bundle extras) {
        // TODO Auto-generated method stub
        switch (action) {
            case ActionEventConstant.NOTIFY_REFRESH_VIEW:
                if (this.isVisible()) {
                    GlobalInfo.getInstance().setListProduct(new ArrayList<PGOrderViewDTO>(), true);
                    isSearchingProduct = true;
                    validateSaleDateTime();
                    this.getListProductAddOrder(0, true);
                    notifyHeader();
                }
                break;

            default:
                super.receiveBroadcast(action, extras);
                break;
        }

    }
    @Override
    public boolean onTouch(View v, MotionEvent arg1) {
        if (v == etInputDateSale) {
            if (!etInputDateSale.onTouchEvent(arg1)) {
                parent.fragmentTag = PGOrderView.TAG;
                parent.showDatePickerWithDate(GlobalBaseActivity.DATE_DIALOG_ID, etInputDateSale.getText().toString(), true);
            }
        }
        return false;
    }
    /**
     * updateDisplay
     *
     * @author: YenNTH
     * @return: void
     * @throws:
     */
    public void updateDate(int day, int month, int year) {
        this.mDay = day;
        this.mMonth = month;
        this.mYear = year;
        String sDay = String.valueOf(mDay);
        String sMonth = String.valueOf(mMonth + 1);
        if (mDay < 10) {
            sDay = "0" + sDay;
        }
        if (mMonth + 1 < 10) {
            sMonth = "0" + sMonth;
        }
        etInputDateSale.setTextColor(ImageUtil.getColor(R.color.BLACK));
        etInputDateSale.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(sDay).append("/").append(sMonth).append("/").append(mYear).append(" "));
        saleDateTime = mYear + "-" + sMonth + "-" + sDay;
        validateSaleDateTime();
        isSearchingProduct = true;
        isReLoadProduct = true;
        GlobalInfo.getInstance().setListProduct(new ArrayList<PGOrderViewDTO>(), true);
        orgListDTO = new ListOrderViewDTO();
        this.getListProductAddOrder(0, true);
    }

    /**
     * Check co duoc luu san luong hay ko?
     */
    public void validateSaleDateTime(){
        if(DateUtils.compareWithNow(saleDateTime, DateUtils.DATE_FORMAT_SQL_DEFAULT) == 0
                || (DateUtils.daysBetween(DateUtils.convertToDate(saleDateTime, DateUtils.DATE_FORMAT_SQL_DEFAULT),
                DateUtils.now(DateUtils.DATE_FORMAT_SQL_DEFAULT)) <= GlobalInfo.getInstance().getPGNumberSendOrder()
                && DateUtils.compareWithNow(saleDateTime, DateUtils.DATE_FORMAT_SQL_DEFAULT) != 1)){
            isEnable = true;
        } else{
            isEnable = false;
        }
        GlobalUtil.setEnableButton(btSave, isEnable);
    }
    /**
     * Luu lai san pham da nhap
     * @param typeObjectSave
     * @return
     */
    public boolean saveProductSelected(int typeObjectSave) {
        boolean check = true;
        for (int i = 0, size = tbProductOrderList.getListRowView().size(); i < size; i++) {
            PGOrderListRow row = (PGOrderListRow) tbProductOrderList.getListRowView().get(i);
            String numberProduct = row.etNumber.getText().toString();
            if (this.validateDataProductInput(listDTO.listObject.get(i), numberProduct)) {
                if ((!StringUtil.isNullOrEmpty(numberProduct) && !numberProduct.equals("0"))) {
                    if (!this.isExitsProductInListSelected(listDTO.listObject.get(i))) {
                        if (StringUtil.isNullOrEmpty(numberProduct)) {
                            listDTO.listObject.get(i).quantity = "0";
                        } else {
                            listDTO.listObject.get(i).quantity = numberProduct;
                        }
                        orgListDTO.listObject.add(listDTO.listObject.get(i));
                    } else {
                        if (this.getIndexProductInProductListSelected(listDTO.listObject
                                .get(i)) >= 0) {
                            // update value number product for org list
                            // dto
                            int index = getIndexProductInProductListSelected(listDTO.listObject
                                    .get(i));
                            if (StringUtil.isNullOrEmpty(numberProduct)) {
                                listDTO.listObject.get(i).quantity = "0";
                            } else {
                                listDTO.listObject.get(i).quantity = numberProduct;
                            }
                            orgListDTO.listObject.set(index, listDTO.listObject.get(i));
                        }
                    }
                } else {
                    if (this.isExitsProductInListSelected(listDTO.listObject.get(i))) {
                        if (this.getIndexProductInProductListSelected(listDTO.listObject.get(i)) >= 0) {
                            int index = this.getIndexProductInProductListSelected(listDTO.listObject.get(i));
                            orgListDTO.listObject.remove(index);
                        }
                    }
                }
            } else {
            }
        }
        return check;
    }

    /**
     * Luu thong tin truoc khi chuyen trang
     */
    public void saveProductSelected() {
        for (int i = 0, size = tbProductOrderList.getListRowView().size(); i < size; i++) {
            PGOrderListRow row = (PGOrderListRow) tbProductOrderList.getListRowView().get(i);
            String numberProduct = row.etNumber.getText().toString();
            if (this.validateDataProductInput(listDTO.listObject.get(i), numberProduct)) {
                if ((!StringUtil.isNullOrEmpty(numberProduct) && !numberProduct.equals("0"))) {
                    if (!this.isExitsProductInListSelected(listDTO.listObject.get(i))) {
                        if (StringUtil.isNullOrEmpty(numberProduct)) {
                            listDTO.listObject.get(i).quantity = "0";
                        } else {
                            listDTO.listObject.get(i).quantity = numberProduct;
                        }
                        orgListDTO.listObject.add(listDTO.listObject.get(i));
                    } else {
                        if (this.getIndexProductInProductListSelected(listDTO.listObject
                                .get(i)) >= 0) {
                            // update value number product for org list
                            // dto
                            int index = getIndexProductInProductListSelected(listDTO.listObject
                                    .get(i));
                            if (StringUtil.isNullOrEmpty(numberProduct)) {
                                listDTO.listObject.get(i).quantity = "0";
                            } else {
                                listDTO.listObject.get(i).quantity = numberProduct;
                            }
                            orgListDTO.listObject.set(index, listDTO.listObject.get(i));
                        }
                    }
                } else {
                    if (this.isExitsProductInListSelected(listDTO.listObject.get(i))) {
                        if (this.getIndexProductInProductListSelected(listDTO.listObject.get(i)) >= 0) {
                            int index = this.getIndexProductInProductListSelected(listDTO.listObject.get(i));
                            orgListDTO.listObject.remove(index);
                        }
                    }
                }
            }
        }
    }

    /**
     * check saleOrderDetailDTO exits in list product selected
     * @param currentProduct
     * @return
     */
    public boolean isExitsProductInListSelected(PGOrderViewDTO currentProduct) {
        boolean kq = false;
        if (orgListDTO != null) {
            for (int i = 0, size = orgListDTO.listObject.size(); i < size; i++) {
                PGOrderViewDTO selectedProduct = orgListDTO.listObject.get(i);
                if (currentProduct.productId == selectedProduct.productId) {
                    kq = true;
                    break;
                }
            }
        }
        return kq;
    }

    /**
     * Validate data num product input
     * @param productData
     * @param numberProduct
     * @return
     */
    public boolean validateDataProductInput(PGOrderViewDTO productData, String numberProduct) {
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
     * get index of current product in list product selected
     * @param product
     * @return
     */
    public int getIndexProductInProductListSelected(PGOrderViewDTO product) {
        int kq = -1;
        if (this.isExitsProductInListSelected(product)) {
            for (int i = 0, size = orgListDTO.listObject.size(); i < size; i++) {
                PGOrderViewDTO selectedProduct = orgListDTO.listObject.get(i);
                if (product.productId == selectedProduct.productId) {
                    kq = i;
                    break;
                }
            }
        }
        return kq;
    }
    /**
     * Luu don hang vao db
     */
    public void saveOrderList() {
        boolean isAllowSave = false;
        if(GlobalInfo.getInstance().getListProduct() != null){
            if(GlobalInfo.getInstance().getListProduct().size() > 0) {
                orgListDTO.listObject = GlobalInfo.getInstance().getListProduct();
                isAllowSave = true;
            } else {
                orgListDTO.listObject = new ArrayList<>();
            }
        } else if(orgListDTO.listObject.size() > 0){
            for (int i = 0, size = orgListDTO.listObject.size(); i < size; i++) {
                orgListDTO.listObject.get(i).realQuantity = GlobalUtil.realQuantity(orgListDTO.listObject.get(i).quantity,
                        orgListDTO.listObject.get(i).convfact);
            }
            isAllowSave = true;
        }
        if(GlobalInfo.getInstance().getListAllProduct().size() > 0){
            if(orgListDTO.listObject.size() > 0) {
                ArrayList<PGOrderViewDTO> listAllOrder = new ArrayList<PGOrderViewDTO>();
                listAllOrder = GlobalInfo.getInstance().getListAllProduct();
                for (PGOrderViewDTO itemList : orgListDTO.listObject) {
                    int size = listAllOrder.size();
                    PGOrderViewDTO item = new PGOrderViewDTO();
                    for (int i = 0; i < size; i++) {
                        item = listAllOrder.get(i);
                        if (item.productId == itemList.productId){
                            listAllOrder.remove(i);
                            size = size - 1;
                            if(i <= 0 || i == size){
                                break;
                            }
                        }
                    }
                }
                GlobalInfo.getInstance().setListAllProduct(listAllOrder);
                if(GlobalInfo.getInstance().getListAllProduct().size() > 0){
                    orgListDTO.listObject.addAll(GlobalInfo.getInstance().getListAllProduct());
                }
            }else {
                orgListDTO.listObject.addAll(GlobalInfo.getInstance().getListAllProduct());
            }
            if(orgListDTO.listObject.size() > 0) {
                isAllowSave = true;
            }
        }
        if(listDTO.pgCustomerViewDTO.pgSaleOrderId > 0) {
            isAllowSave = true;
        }
        if(isAllowSave) {
            requestCreateOrder();
        }else{
            GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.TEXT_INPUT_PG_SALE),
                    StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), ACTION_CLOSE, null, false);
        }
    }

    /**
     * Tao don hang pg
     */
    private void requestCreateOrder() {
        parent.showLoadingDialog();
        ListOrderViewDTO dto = new ListOrderViewDTO();
        if(orgListDTO.listObject.size() > 0) {
            dto.saleOrderDTO.shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
            dto.saleOrderDTO.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
            dto.saleOrderDTO.customerId = listDTO.customerDTO.customerId;
            dto.saleOrderDTO.status = 1;
            dto.saleOrderDTO.orderDate = saleDateTime;
            dto.saleOrderDTO.createUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
            dto.saleOrderDTO.createDate = DateUtils.now();
            for (int i = 0, size = orgListDTO.listObject.size(); i < size; i++) {
                PGSaleOrderDetailDTO pgSaleOrderDetailDTO = new PGSaleOrderDetailDTO();
                pgSaleOrderDetailDTO.shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
                pgSaleOrderDetailDTO.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
                pgSaleOrderDetailDTO.productId = orgListDTO.listObject.get(i).productId;
                pgSaleOrderDetailDTO.parentProductId = orgListDTO.listObject.get(i).parentProductId;
                pgSaleOrderDetailDTO.quantity = orgListDTO.listObject.get(i).realQuantity;
                pgSaleOrderDetailDTO.convfact = orgListDTO.listObject.get(i).convfact;
                pgSaleOrderDetailDTO.orderDate = saleDateTime;
                pgSaleOrderDetailDTO.createUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
                pgSaleOrderDetailDTO.createDate = DateUtils.now();
                dto.listSaleOrderDetailDTO.add(pgSaleOrderDetailDTO);
            }
        }else{
            dto.saleOrderDTO = null;
            dto.listSaleOrderDetailDTO = new ArrayList<>();
        }
        if(listDTO.pgCustomerViewDTO.pgSaleOrderId > 0) {
            dto.pgOrderUpdateDTO.pgSaleOrderId = listDTO.pgCustomerViewDTO.pgSaleOrderId;
            dto.pgOrderUpdateDTO.status = 0;
            dto.pgOrderUpdateDTO.updateDate = DateUtils.now();
            dto.pgOrderUpdateDTO.updateUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
        }
        ActionEvent e = new ActionEvent();
        e.viewData = dto;
        e.sender = this;
        e.action = ActionEventConstant.CREATE_NEW_ORDER;
        PGController.getInstance().handleViewEvent(e);
    }
    /**
     * get number order for product
     * @param product
     * @return
     * @return: int (0: none selected or order = 0)
     * @throws:
     */
    public PGOrderViewDTO getNumberOrderForProduct(PGOrderViewDTO product) {
        for (int i = 0, size = orgListDTO.listObject.size(); i < size; i++) {
            PGOrderViewDTO productSelected = orgListDTO.listObject.get(i);
            if (product.productId == productSelected.productId) {
                return productSelected;
            }
        }
        return null;
    }
}