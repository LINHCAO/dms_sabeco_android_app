package com.viettel.dms.view.supervisor.training;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by yennth16 on 10/13/2016.
 */
public class TrainingListRow extends DMSTableRow implements View.OnClickListener {

    //stt
    private TextView tvNum;
    //nhan vien ban hang
    private TextView tvCusCode;
    //ten khach hang
    private TextView tvCusName;
    //dia chi khach hang
    private TextView tvAddress;
    //ngay udpate toa do
    private TextView tvVisit;
    //so lan update toa do
    private TextView tvPath;
    // khoanh cach tu nv den khach hang
    TextView tvDistance;
    // image the hien thang thai ghe tham
    public ImageView ivVisitCus;
    //ngay hien tại
    Calendar calendar = Calendar.getInstance();
    String sToday = DateUtils.defaultDateFormat.format(calendar.getTime());
    private CustomerListItem item;
    TextView tvShopName;
    public TrainingListRow(Context context, VinamilkTableListener lis) {
        super(context, R.layout.layout_training_list_row);
        setListener(lis);
        this.setOnClickListener(this);
        tvNum = (TextView) this.findViewById(R.id.tvNum);
        tvCusCode = (TextView) this.findViewById(R.id.tvCusCode);
        tvCusName = (TextView) this.findViewById(R.id.tvCusName);
        tvVisit = (TextView) this.findViewById(R.id.tvVisit);
        tvAddress = (TextView) this.findViewById(R.id.tvAdd);
        tvPath = (TextView) this.findViewById(R.id.tvPath);
        tvDistance = (TextView) this.findViewById(R.id.tvDistance);
        ivVisitCus = (ImageView) this.findViewById(R.id.visitCus);
        tvCusCode.setOnClickListener(this);
        ivVisitCus.setOnClickListener(this);
        tvShopName = (TextView) this.findViewById(R.id.tvShopName);
    }

    @Override
    public void onClick(View v) {
        if (v == this && context != null){
            GlobalUtil.forceHideKeyboard((GlobalBaseActivity)context);
        }else if (v == tvCusCode) {
            listener.handleVinamilkTableRowEvent(ActionEventConstant.GO_TO_CUSTOMER_INFO, tvCusCode, item);
        } else if (v == ivVisitCus) {
            listener.handleVinamilkTableRowEvent(ActionEventConstant.VISIT_CUSTOMER, ivVisitCus, item);
        }
    }

    /**
     * render data
     * @param pos
     * @param item1
     */
    public void render(int pos, CustomerListItem item1) {
        this.item=item1;
        tvNum.setText("" + pos);
        if(!StringUtil.isNullOrEmpty(item.aCustomer.getCustomerCode())) {
            tvCusCode.setText(Constants.STR_BLANK + item.aCustomer.getCustomerCode().substring(0, 3));
        } else {
            tvCusCode.setText(Constants.STR_BLANK);
        }
        tvCusName.setText(Constants.STR_BLANK+item.aCustomer.getCustomerName());
        tvAddress.setText(Constants.STR_BLANK+item.aCustomer.getStreet());
        tvPath.setText(item.cusPlan);
        tvVisit.setText(item.seqInDayPlan);
        tvShopName.setText(item.shopName);
        renderVisitStatus();
    }

    public void reRenderVisitStatus() {
        if (this.item != null){
            this.item.updateCustomerDistance();
            renderVisitStatus();
        }
    }

    private void renderVisitStatus() {
        ivVisitCus.setEnabled(true);

        if (item.cusDistance >= 1000) {
            float tempDistance = (float) item.cusDistance / 1000;
            DecimalFormat df = new DecimalFormat("0.00");
            String formater = df.format(tempDistance);
            tvDistance.setText("" + formater + " km");
        } else if (item.cusDistance >= 0) {
            tvDistance.setText("" + item.cusDistance + " m");
        } else {
            tvDistance.setText("");
        }

        if (item.isVisit()) {
            ivVisitCus.setImageResource(R.drawable.icon_check);
        } else {
            ivVisitCus.setImageResource(R.drawable.icon_door);
        }

        if (item.visitStatus == CustomerListItem.VISIT_STATUS.VISITED_CLOSED) {
            setBackGroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
        } else if (item.isTodayOrdered) {
            setBackGroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_ORDER_SUCC));
        } else {
            tvNum.setBackgroundResource(R.drawable.style_row_default);
            tvCusCode.setBackgroundResource(R.drawable.style_row_default);
            tvCusName.setBackgroundResource(R.drawable.style_row_default);
            tvCusName.setPadding(GlobalUtil.dip2Pixel(10), 0, GlobalUtil.dip2Pixel(10), 0);
            tvVisit.setBackgroundResource(R.drawable.style_row_default);
            tvVisit.setPadding(GlobalUtil.dip2Pixel(10), 0, GlobalUtil.dip2Pixel(10), 0);
            tvAddress.setBackgroundResource(R.drawable.style_row_default);
            tvAddress.setPadding(GlobalUtil.dip2Pixel(10), 0, GlobalUtil.dip2Pixel(10), 0);
            tvPath.setBackgroundResource(R.drawable.style_row_default);
            tvPath.setPadding(GlobalUtil.dip2Pixel(5), 0, GlobalUtil.dip2Pixel(5), 0);
            tvDistance.setBackgroundResource(R.drawable.style_row_default);
            tvShopName.setBackgroundResource(R.drawable.style_row_default);
        }

        if(item.canOrderException()){
            ivVisitCus.setVisibility(View.VISIBLE);
        }else {
            if (item.isOr == 0 && item.isTooFarShop) {
                // neu da co check ghe tham lan dau thi van hien thi
                if (item.isVisit()) {
                    ivVisitCus.setVisibility(View.VISIBLE);
                } else {
                    ivVisitCus.setEnabled(false);
                    ivVisitCus.setImageResource(0);
                }
            } else if(item.isOr==1) {
                if(item.aCustomer.lat > 0 && item.aCustomer.lng > 0){
                    ivVisitCus.setVisibility(View.VISIBLE);
                }else{
                    ivVisitCus.setEnabled(false);
                    ivVisitCus.setImageResource(0);
                }
            }
        }
    }
    /**
     * set màu nền cho các view.
     * @param: int color
     * @return: void
     */
    private void setBackGroundColor(int color) {
        // TODO Auto-generated method stub
        tvNum.setBackgroundColor(color);
        tvCusCode.setBackgroundColor(color);
        tvCusName.setBackgroundColor(color);
        tvVisit.setBackgroundColor(color);
        tvAddress.setBackgroundColor(color);
        tvPath.setBackgroundColor(color);
        tvDistance.setBackgroundColor(color);
        ivVisitCus.setBackgroundColor(color);
        tvShopName.setBackgroundColor(color);
    }
}
