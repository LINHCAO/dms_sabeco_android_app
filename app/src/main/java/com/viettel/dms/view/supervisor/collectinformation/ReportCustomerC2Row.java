package com.viettel.dms.view.supervisor.collectinformation;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.PGOrderViewDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

import java.util.ArrayList;

/**
 * Created by yennth16 on 8/12/2016.
 */
public class ReportCustomerC2Row extends TableRow implements
        View.OnClickListener, TextWatcher {
    public static final int MAX_LENGTH = 9;
    public static final int MAX_LENGTH_WITH_COMMA = 11;

    private final Context _context;
    private final View view;
    // STT
    TextView tvSTT;
    // code
    TextView tvMMH;
    // number
    EditText etNumber;
    TextView tvNumber;
    LinearLayout llEditText;
    // listener
    protected VinamilkTableListener listener;
    // data to render layout for row
    PGOrderViewDTO myData;
    ArrayList<PGOrderViewDTO> listObject = null;
    ArrayList<PGOrderViewDTO> listAllProduct = null;

    public ReportCustomerC2Row(Context con){
        super(con);
        _context = con;
        LayoutInflater vi = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(GlobalUtil.checkIsTablet(_context)) {
            view = vi.inflate(R.layout.layout_pg_order_large_row, this);
        }else{
            view = vi.inflate(R.layout.layout_pg_order_row, this);
        }
    }

    /**
     * constructor for class
     *
     * @param context
     */
    public ReportCustomerC2Row(Context context, VinamilkTableListener listen) {
        this(context);
        listener=listen;
        setOnClickListener(this);
        tvSTT = (TextView) view.findViewById(R.id.tvSTT);
        tvMMH = (TextView) view.findViewById(R.id.tvMMH);
        etNumber = (EditText) view.findViewById(R.id.etNumber);
        etNumber.addTextChangedListener(this);
        tvNumber = (TextView) view.findViewById(R.id.tvNumber);
        StringUtil.initInputDialog(etNumber, StringUtil.getString(R.string.TEXT_INPUT_NUMBER));
        llEditText = (LinearLayout) view.findViewById(R.id.llEditText);
    }

    /**
     *
     * init layout for row
     *
     * @author: HaiTC3
     * @param position
     * @param item
     * @return: void
     * @throws:
     */
    public void renderLayout(int position, PGOrderViewDTO item, boolean isRequestFocus, boolean isEnable, boolean isView) {
        this.myData = item;
        tvSTT.setText(String.valueOf(position));
        tvMMH.setText(item.productName);
        if(isView){
            etNumber.setVisibility(View.GONE);
            tvNumber.setVisibility(View.VISIBLE);
            if (!StringUtil.isNullOrEmpty(item.quantity) && !item.quantity.equals("0")) {
                if (!StringUtil.isStringContainValidChars(item.quantity,'/')) {
                    tvNumber.setText(GlobalUtil.formatNumberProductFlowConvfact(Long.valueOf(item.quantity), item.convfact));
                } else{
                    tvNumber.setText(item.quantity);
                }
            }
        }else{
            tvNumber.setVisibility(View.GONE);
            etNumber.setVisibility(View.VISIBLE);
            if(isEnable) {
                etNumber.setTag(StringUtil.setDataInputDialog(StringUtil.TAG_INPUT_QUANTITY, item.productName, item.convfact));
            }
            if (!StringUtil.isNullOrEmpty(item.quantity) && !item.quantity.equals("0")) {
                if (!StringUtil.isStringContainValidChars(item.quantity,'/')  && !StringUtil.isStringContainValidChars(item.quantity,',')) {
                    etNumber.setText(GlobalUtil.formatNumberProductFlowConvfact(Long.valueOf(item.quantity), item.convfact));
                } else{
                    etNumber.setText(item.quantity);
                }
            }
            GlobalUtil.setEnableEditText(etNumber, isEnable);
        }
    }
    public void upDateRow(PGOrderViewDTO item){
        etNumber.setText(StringUtil.formatNumberProductFlowConvfact(Long.valueOf(item.numProduct), item.convfact));
    }
    public View getView() {
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == this && _context != null) {
            GlobalUtil.forceHideKeyboard((GlobalBaseActivity) _context);
        }
    }

    @Override
    public void afterTextChanged(Editable arg0) {
        listObject = GlobalInfo.getInstance().getListProduct();
        listAllProduct = GlobalInfo.getInstance().getListAllProduct();
        if (!StringUtil.isNullOrEmpty(etNumber.getText().toString().trim())) {
            myData.realQuantity = GlobalUtil.realQuantity(etNumber.getText().toString().trim(), this.myData.convfact);
            myData.quantity = etNumber.getText().toString().trim();
            boolean isResetValue = false;
            if(listObject != null) {
                if(listObject.size() > 0) {
                    for (int i = 0; i < listObject.size(); i++) {
                        if (listObject.get(i).productId == myData.productId) {
                            isResetValue = true;
                            listObject.get(i).quantity = myData.quantity;
                            listObject.get(i).realQuantity = myData.realQuantity;
                            GlobalInfo.getInstance().setListProduct(listObject, false);
                            break;
                        }
                    }
                    if(!isResetValue){
                        listObject.add(myData);
                    }
                }else {
                    listObject.add(myData);
                }
            }else{
                listObject.add(myData);
            }
            GlobalInfo.getInstance().setListProduct(listObject, false);
        }else{
            if(listObject != null) {
                if(listObject.size() > 0) {
                    for (int i = 0; i < listObject.size(); i++) {
                        if (listObject.get(i).productId == myData.productId) {
                            listObject.remove(i);
                            GlobalInfo.getInstance().setListProduct(listObject, false);
                            break;
                        }
                    }
                }else{
                    GlobalInfo.getInstance().setListProduct(new ArrayList<PGOrderViewDTO>(), true);
                }
            }
        }
        for (int i = 0; i < listAllProduct.size(); i++) {
            if (listAllProduct.get(i).productId == myData.productId) {
                listAllProduct.remove(i);
                GlobalInfo.getInstance().setListAllProduct(listAllProduct);
                break;
            }
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
}