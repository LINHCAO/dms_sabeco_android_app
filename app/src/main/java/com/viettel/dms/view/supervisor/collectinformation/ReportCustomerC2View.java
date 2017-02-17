package com.viettel.dms.view.supervisor.collectinformation;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.PGController;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.dto.view.ListOrderViewDTO;
import com.viettel.dms.dto.view.PGOrderViewDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.SupervisorActivity;
import com.viettel.dms.view.pg.PGOrderListRow;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yennth16 on 8/20/2016.
 */
public class ReportCustomerC2View extends BaseFragment implements
        OnEventControlListener, View.OnClickListener, VinamilkTableListener, View.OnTouchListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = ReportCustomerC2View.class.getName();
    // list ban
    VinamilkTableView tbC2ListSale;
    // list mua
    VinamilkTableView tbC2ListBuy;
    // button search
    Button btSearch;
    // input order code
    VNMEditTextClearable etInputFromDateSale;
    // input order code
    VNMEditTextClearable etInputToDateSale;
    private GlobalBaseActivity parent; // parent
    // flag when searching product
    boolean isSearchingProduct = false;
    // flag when load list product the first
    boolean isFirstLoadProduct = false;
    // limit row in page
    public static final int LIMIT_ROW_PER_PAGE = 10;
    // list product
    ListOrderViewDTO listDTO = new ListOrderViewDTO();
    private int mDay;// mDay
    private int mMonth;// mMonth
    private int mYear;// mYear
    private String saleFromDateTime = Constants.STR_BLANK;
    private String saleToDateTime = Constants.STR_BLANK;
    private boolean isEnable = false;
    // Cho biet fomDate/toDate duoc Touch
    private static final int DATE_FROM_CONTROL = 1; // touch from date
    private static final int DATE_TO_CONTROL = 2; // touch to date
    private static final int MENU_CUSTOMER_C2 = 3;
    private static final int MENU_REPORT = 4;

    private static final int TYPE = 0;
    private int currentCalender; // index luu dang touch from/to date
    private Spinner spinnerType;
    public static ReportCustomerC2View getInstance(Bundle data) {
        ReportCustomerC2View instance = new ReportCustomerC2View();
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
        ViewGroup view = (ViewGroup) inflater.inflate( R.layout.layout_report_customer_c2_view, container, false);
        View v = super.onCreateView(inflater, view, savedInstanceState);
        enableMenuBar(this);
        addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_GENERAL_STATISTICS), R.drawable.icon_clock, MENU_REPORT);
        addMenuItem(StringUtil.getString(R.string.TEXT_INPUT_NEW), R.drawable.icon_task, MENU_CUSTOMER_C2);
        setMenuItemFocus(1);
        initView(v);
        setTitleHeaderView(StringUtil.getString(R.string.TEXT_HEADER_MENU_GENERAL_STATISTICS));
        isFirstLoadProduct = true;
        getListProductAddOrder(0,true);
        return v;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            parent = (SupervisorActivity) activity;
        } catch (Exception e) {
            VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
        }

    }
    /**
     * initView
     * @param v
     */
    public void initView(View v) {
        tbC2ListSale = (VinamilkTableView) v.findViewById(R.id.tbC2ListSale);
        tbC2ListSale.setListener(this);
        tbC2ListSale.setNumItemsPage(LIMIT_ROW_PER_PAGE);
        tbC2ListBuy = (VinamilkTableView) v.findViewById(R.id.tbC2ListBuy);
        tbC2ListBuy.setListener(this);
        tbC2ListBuy.setNumItemsPage(LIMIT_ROW_PER_PAGE);
        btSearch = (Button) v.findViewById(R.id.btSearch);
        btSearch.setOnClickListener(this);
        TextView tvTextSale = (TextView) v.findViewById(R.id.tvTextSale);
        tvTextSale.requestFocus();
        GlobalUtil.forceHideKeyboardUseToggle(parent);
        etInputFromDateSale = (VNMEditTextClearable) v.findViewById(R.id.etInputFromDateSale);
        etInputFromDateSale.setText(DateUtils.getCurrentDate());
        etInputFromDateSale.setOnTouchListener(this);
        etInputFromDateSale.setIsHandleDefault(false);
        GlobalUtil.forceHideKeyboardInput(parent, etInputFromDateSale);
        saleFromDateTime = DateUtils.convertDateOneFromFormatToAnotherFormat(etInputFromDateSale.getText().toString(),
                DateUtils.defaultDateFormat.toPattern(), DateUtils.defaultSqlDateFormat.toPattern());
        etInputToDateSale = (VNMEditTextClearable) v.findViewById(R.id.etInputToDateSale);
        etInputToDateSale.setText(DateUtils.getCurrentDate());
        etInputToDateSale.setOnTouchListener(this);
        etInputToDateSale.setIsHandleDefault(false);
        saleToDateTime = DateUtils.convertDateOneFromFormatToAnotherFormat(etInputToDateSale.getText().toString(),
                DateUtils.defaultDateFormat.toPattern(), DateUtils.defaultSqlDateFormat.toPattern());
        spinnerType = (Spinner) v.findViewById(R.id.spinnerType);
        spinnerType.setOnItemSelectedListener(this);
        SpinnerAdapter adapterLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item,
                Constants.ARRAY_C2_TYPE_CHOOSE);
        spinnerType.setAdapter(adapterLine);
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
        data.putString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE, saleFromDateTime);
        data.putString(IntentConstants.INTENT_FIND_ORDER_TO_DATE, saleToDateTime);
        data.putBoolean(IntentConstants.INTENT_IS_GET_NUM_TOTAL_ITEM, isGetCount);
        e.viewData = data;
        e.sender = this;
        e.action = ActionEventConstant.GET_PG_REPORT_ORDER_VIEW;
        e.isNeedCheckTimeServer = false;
        PGController.getInstance().handleViewEvent(e);
    }
    /**
     * render layout for screen after get data from db
     * @return: void
     * @throws:
     */
    private void renderLayout() {
        int pos = 1;
        int numberItem = 0;
        if (listDTO.listObject.size() > 0) {
            numberItem = listDTO.totalObject;
            tbC2ListSale.clearAllData();
            tbC2ListSale.hideNoContentRow();
            for (int i = 0, size = listDTO.listObject.size(); i < size; i++) {
                PGOrderViewDTO dto = listDTO.listObject.get(i);
                PGOrderListRow row = new PGOrderListRow(parent, this);
                row.setClickable(true);
                row.setTag(Integer.valueOf(pos));
                if (size == 1) {
                    row.renderLayout(pos + (tbC2ListSale.getPagingControl().getCurrentPage() - 1) * LIMIT_ROW_PER_PAGE, dto, true, isEnable, true);
                    GlobalUtil.showKeyboardUseToggle(parent);
                } else {
                    row.renderLayout( pos+ (tbC2ListSale.getPagingControl().getCurrentPage() - 1) * LIMIT_ROW_PER_PAGE, dto, false, isEnable, true);
                }
                pos++;
                tbC2ListSale.addRow(row);
            }
        } else {
            tbC2ListSale.showNoContentRow();
        }
        if (isFirstLoadProduct) {
            initHeader();
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
        if(GlobalUtil.checkIsTablet(parent)) {
            parent.setStatusVisible(StringUtil.getString(R.string.TEXT_COLUM_NVTT_COLON)
                    + " " + GlobalInfo.getInstance().getProfile().getUserData().displayName
                    + "\n" + StringUtil.getString(R.string.TEXT_PG_CUSTOMER_VISIT) + " "
                    + listDTO.pgCustomerViewDTO.customerName, View.VISIBLE);
            FIND_PRODUCT_TABLE_WIDTHS = new int[] { 50, 730, 220};
        }else{
            parent.setStatusVisible(GlobalInfo.getInstance().getProfile().getUserData().displayName
                    + "\n" + listDTO.pgCustomerViewDTO.customerName, View.VISIBLE);
        }
        tbC2ListSale.getHeaderView().addColumns(
                FIND_PRODUCT_TABLE_WIDTHS,
                FIND_PRODUCT_TABLE_TITLES,
                ImageUtil.getColor(R.color.BLACK),
                ImageUtil.getColor(R.color.TABLE_HEADER_BG));
    }
    @Override
    public void handleModelViewEvent(ModelEvent modelEvent) {
        // TODO Auto-generated method stub
        ActionEvent actionEvent = modelEvent.getActionEvent();
        switch (actionEvent.action) {
            case ActionEventConstant.GET_PG_REPORT_ORDER_VIEW:
                ListOrderViewDTO list = (ListOrderViewDTO) modelEvent.getModelData();
                listDTO = list;
                if (listDTO.listObject.size() > 0) {
                    tbC2ListSale.getPagingControl().setVisibility(
                            View.VISIBLE);
                    if (tbC2ListSale.getPagingControl().totalPage < 0
                            || isSearchingProduct) {
                        tbC2ListSale.setTotalSize(listDTO.totalObject);
                        tbC2ListSale.getPagingControl().setCurrentPage(1);
                    }
                } else {
                    tbC2ListSale.getPagingControl().setVisibility(View.GONE);
                }
                renderLayout();
                if (isFirstLoadProduct) {
                    isFirstLoadProduct = false;
                }
                this.parent.closeProgressDialog();
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
            case ActionEventConstant.GET_PG_REPORT_ORDER_VIEW:
                listDTO = new ListOrderViewDTO();
                this.renderLayout();
                isFirstLoadProduct = false;
                this.parent.closeProgressDialog();
                break;
            default:
                super.handleErrorModelViewEvent(modelEvent);
                break;
        }
    }
    @Override
    public void onEvent(int eventType, View control, Object data) {
        switch (eventType) {
            case MENU_CUSTOMER_C2: {
                ActionEvent e = new ActionEvent();
                e.sender = this;
                e.viewData = new Bundle();
                e.action = ActionEventConstant.GO_TO_CUSTOMER_LIST_C2;
                SuperviorController.getInstance().handleSwitchFragment(e);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btSearch) {
            GlobalUtil.forceHideKeyboard(parent);
            final String currFromDate = etInputFromDateSale.getText().toString().trim();
            final String currToDate = etInputToDateSale.getText().toString().trim();
            String dateTimePattern = StringUtil
                    .getString(R.string.TEXT_DATE_TIME_PATTERN);
            Pattern pattern = Pattern.compile(dateTimePattern);
            String fromDateValid = validateDate(currFromDate, pattern);
            if (!StringUtil.isNullOrEmpty(currFromDate)
                    && fromDateValid == null) {
                parent.showDialog(StringUtil.getString(R.string.TEXT_START_DATE_SEARCH_SYNTAX_ERROR));
                return;
            } else if(StringUtil.isNullOrEmpty(currFromDate)){
                saleFromDateTime = Constants.STR_BLANK;
            }
            String toDateValid = validateDate(currToDate, pattern);
            if (!StringUtil.isNullOrEmpty(currToDate) && toDateValid == null) {
                parent.showDialog(StringUtil.getString(R.string.TEXT_END_DATE_SEARCH_SYNTAX_ERROR));
                return;
            } else if(StringUtil.isNullOrEmpty(currToDate)){
                saleToDateTime = Constants.STR_BLANK;
            }
            if (fromDateValid != null && toDateValid != null
                    && DateUtils.compareDate(fromDateValid, toDateValid) == 1) {
                GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.TEXT_DATE_TIME_INVALID_2),
                                StringUtil.getString(R.string.TEXT_BUTTON_CLOSE),0, null, false);
                return;
            }
            isSearchingProduct = true;
            this.getListProductAddOrder(0, true);
        }
    }
    @Override
    public void handleVinamilkTableloadMore(View control, Object data) {
        if (control == tbC2ListSale) {
            isSearchingProduct = false;
            getListProductAddOrder((tbC2ListSale.getPagingControl().getCurrentPage() - 1),  false);
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
                    isSearchingProduct = true;
                    this.getListProductAddOrder(0, true);
                }
                break;

            default:
                super.receiveBroadcast(action, extras);
                break;
        }

    }
    @Override
    public boolean onTouch(View v, MotionEvent arg1) {
        if (v == etInputFromDateSale) {
            if (!etInputFromDateSale.onTouchEvent(arg1)) {
                currentCalender = DATE_FROM_CONTROL;
                parent.fragmentTag = ReportCustomerC2View.TAG;
                parent.showDatePickerWithDate(GlobalBaseActivity.DATE_DIALOG_ID, etInputFromDateSale.getText().toString(), true);
            }
        }
        if (v == etInputToDateSale) {
            if (!etInputToDateSale.onTouchEvent(arg1)) {
                currentCalender = DATE_TO_CONTROL;
                parent.fragmentTag = ReportCustomerC2View.TAG;
                parent.showDatePickerWithDate(GlobalBaseActivity.DATE_DIALOG_ID, etInputToDateSale.getText().toString(), true);
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
        StringBuilder strDate = new StringBuilder();
        strDate.append(sDay).append("/").append(sMonth).append("/")
                .append(year).append(" ");
        if (currentCalender == DATE_FROM_CONTROL) {
            if (!DateUtils.checkDateInOffsetMonth(sDay, sMonth, year, -2)) {
                GlobalUtil.showDialogConfirm(this, parent,
                        StringUtil.getString(R.string.TEXT_FROM_DATE_INVALID),
                        StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), 0, null,
                        false);
                return;
            }
            etInputFromDateSale.setTextColor(ImageUtil.getColor(R.color.BLACK));
            etInputFromDateSale.setText(strDate );
            saleFromDateTime = mYear + "-" + sMonth + "-" + sDay;
        } else if (currentCalender == DATE_TO_CONTROL) {
            if (!DateUtils.checkDateInOffsetMonth(sDay, sMonth, year, -2)) {
                GlobalUtil.showDialogConfirm(this, parent,
                        StringUtil.getString(R.string.TEXT_TO_DATE_INVALID),
                        StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), 0, null,
                        false);
                return;
            }
            etInputToDateSale.setTextColor(ImageUtil.getColor(R.color.BLACK));
            etInputToDateSale.setText(strDate );
            saleToDateTime = mYear + "-" + sMonth + "-" + sDay;
        }
    }
    /***
     * Kiem tra chuoi ngay co dung voi mau khong - Tra ve chuoi ngay theo dinh
     * dang :yyyy-MM-dd neu chuoi hop le - Nguoc lai tra ve chuoi rong
     *
     * @author quangvt1
     * @param strDate
     * @param pattern
     * @return
     */
    private String validateDate(final String strDate, Pattern pattern) {
        String result = Constants.STR_BLANK;
        if (!StringUtil.isNullOrEmpty(strDate)) {
            Matcher matcher = pattern.matcher(strDate);
            if (matcher.matches()) {
                try {
                    Date date = StringUtil.stringToDate(strDate, "");
                    result = StringUtil.dateToString(date,DateUtils.DATE_FORMAT_SQL_DEFAULT);
                } catch (Exception ex) {
                }
            }
        }
        return result;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}