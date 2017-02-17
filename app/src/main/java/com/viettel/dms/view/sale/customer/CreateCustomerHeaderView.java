/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.customer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.viettel.dms.dto.view.CreateCustomerInfoDTO;
import com.viettel.dms.dto.view.CreateCustomerInfoDTO.AreaItem;
import com.viettel.dms.dto.view.CreateCustomerInfoDTO.CustomerType;
import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.sqllite.db.CUSTOMER_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.sabeco.R;

/**
 * Create customer header for SaleMan CreateCustomerHeaderView.java
 * 
 * @author: duongdt3
 * @version: 1.0
 * @since: 16:20:38 4 Jan 2014
 */
public class CreateCustomerHeaderView  extends LinearLayout implements OnItemSelectedListener, View.OnTouchListener {
	// ten khach hang
	private TextView tvCustomerName;
	// input ten khach hang
	private VNMEditTextClearable edCustomerName;
	// spinner khach hang lay hang ty C2
	private Spinner spinnerC2;
	// textView Loai khach hang
	private TextView tvType;
	// spinner loai khach hang
	private Spinner spinnerType;
	// textView so dien thoai co dinh
	private TextView tvPhone;
	// input so dien thoai co dinh
	private VNMEditTextClearable edPhone;
	// textView so dien thoai di dong
	private TextView tvMobilePhone;
	// input so dien thoai di dong
	private VNMEditTextClearable edMobilePhone;
	// textview nguoi lien he
	private TextView tvContactName;
	// input nguoi lien he
	private VNMEditTextClearable edContactName;
	// textview Tinh/ thanh pho
	private TextView tvProvine;
	// spinner chon tinh/thanh pho
	private Spinner spinnerProvine;
	//TextView quan/huyen
	private TextView tvDistrict;
	//spinner chon quan/huyen
	private Spinner spinnerDistrict;
	//TextView phuong xa
	private TextView tvPrecinct;
	// spinner chon phuong/xa
	private Spinner spinnerPrecinct;
	//input so nha
	private VNMEditTextClearable edHouseNumber;
	//input duong
	private VNMEditTextClearable edStreet;
	// textview thong bao nhap sai
	private TextView tvError;
	
	//for map fragment use
	public Button btSave;
	private boolean isEditMode = false;
	public CreateCustomerHeaderView(Context context, boolean isEditMode) {
		super(context);
		this.isEditMode  = isEditMode; 
		initView(context);
	}

	public CreateCustomerHeaderView(Context context, AttributeSet attrs) {
		super(context);
		initView(context);
	}

	private void initView(Context mContext) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_new_customer_header, this, true);
		TableLayout table = (TableLayout) view.findViewById(R.id.table);
		tvCustomerName = (TextView) view.findViewById(R.id.tvCustomerName);
		edCustomerName = (VNMEditTextClearable) view.findViewById(R.id.edCustomerName);
		spinnerC2 = (Spinner) view.findViewById(R.id.spinnerC2);
		tvType = (TextView) view.findViewById(R.id.tvType);
		spinnerType = (Spinner) view.findViewById(R.id.spinnerType);
		tvPhone = (TextView) view.findViewById(R.id.tvPhone);
		edPhone = (VNMEditTextClearable) view.findViewById(R.id.edPhone);
		tvMobilePhone = (TextView) view.findViewById(R.id.tvMobilePhone);
		edMobilePhone = (VNMEditTextClearable) view.findViewById(R.id.edMobilePhone);
		tvContactName = (TextView) view.findViewById(R.id.tvContactName);
		edContactName = (VNMEditTextClearable) view.findViewById(R.id.edContactName);
		tvProvine = (TextView) view.findViewById(R.id.tvProvine);
		spinnerProvine = (Spinner) view.findViewById(R.id.spinnerProvine);
		tvDistrict = (TextView) view.findViewById(R.id.tvDistrict);
		spinnerDistrict = (Spinner) view.findViewById(R.id.spinnerDistrict);
		tvPrecinct = (TextView) view.findViewById(R.id.tvPrecinct);
		spinnerPrecinct = (Spinner) view.findViewById(R.id.spinnerPrecinct);
		//TextView tvHouseNumber = (TextView) view.findViewById(R.id.tvHouseNumber);
		edHouseNumber = (VNMEditTextClearable) view.findViewById(R.id.edHouseNumber);
		//TextView tvStreet = (TextView) view.findViewById(R.id.tvStreet);
		edStreet = (VNMEditTextClearable) view.findViewById(R.id.edStreet);
		btSave = (Button) view.findViewById(R.id.btSave);
		tvError = (TextView) view.findViewById(R.id.tvError);

		//set event for spinners
		spinnerC2.setOnItemSelectedListener(this);
		spinnerDistrict.setOnItemSelectedListener(this);
		spinnerPrecinct.setOnItemSelectedListener(this);
		spinnerProvine.setOnItemSelectedListener(this);
		spinnerType.setOnItemSelectedListener(this);
		
		// array of view
		TextView[] arrTextView = new TextView[] { tvCustomerName, tvType,
				tvPhone, tvMobilePhone, tvContactName, tvProvine, tvDistrict,
				tvPrecinct };

		setRequireSymbolTextViews(arrTextView);
		
		//hide keyboard
		table.setOnTouchListener(this);
		spinnerC2.setOnTouchListener(this);
		spinnerDistrict.setOnTouchListener(this);
		spinnerPrecinct.setOnTouchListener(this);
		spinnerProvine.setOnTouchListener(this);
		spinnerType.setOnTouchListener(this);
		btSave.setOnTouchListener(this);
		
		//không ko cho chỉnh sửa nếu là view mode
		if (!isEditMode) {
			//ẩn nút lưu
			btSave.setVisibility(View.INVISIBLE);
			//disiable toàn bộ view
			//spinner
			spinnerC2.setEnabled(false);
			spinnerType.setEnabled(false);
			spinnerProvine.setEnabled(false);
			spinnerDistrict.setEnabled(false);
			spinnerPrecinct.setEnabled(false);
			
			//edit text
			edCustomerName.setEnabled(false);
			edPhone.setEnabled(false);
			edMobilePhone.setEnabled(false);
			edContactName.setEnabled(false);
			edHouseNumber.setEnabled(false);
			edStreet.setEnabled(false);
		}
	}

	/**
	 * set dấu * cho các TextView thông tin bắt buột
	 * 
	 * @author: duongdt3
	 * @since: 14:33:32 6 Jan 2014
	 * @return: void
	 * @throws:
	 * @param arrTextView
	 */
	private void setRequireSymbolTextViews(TextView[] arrTextView) {
		for (int i = 0; i < arrTextView.length; i++) {
			String text = arrTextView[i].getText().toString();
			int color = ImageUtil.getColor(R.color.RED);
			// thêm *: vào
			text += " " + StringUtil.getColorText("*", color) + ":";

			arrTextView[i].setText(StringUtil.getHTMLText(text));
		}
	}
	
	CreateCustomerView parrent = null;
	public void setParrent(CreateCustomerView view){
		parrent = view;
	}
	
	CreateCustomerInfoDTO dto;
	
	/**
	 * Set thong tin KH
	 * 
	 * @author: duongdt3
	 * @since: 16:37:29 4 Jan 2014
	 * @return: void
	 * @throws:
	 * @param dto
	 */
	public void setCustomerInfo(CreateCustomerInfoDTO dto) {
		this.dto = dto;
		
		if(dto != null){
    		// nếu có thông tin KH
    		if (dto.cusInfo != null) {
    			// tên
    			edCustomerName.setText(dto.cusInfo.getCustomerName());
    			// điện thoại bàn
    			edPhone.setText(dto.cusInfo.getPhone());
    			// điện thoại di động
    			edMobilePhone.setText(dto.cusInfo.getMobilephone());
    			// người liên hệ
    			edContactName.setText(dto.cusInfo.getContactPerson());
    			// số nhà
    			edHouseNumber.setText(dto.cusInfo.getHouseNumber());
    			// đường
    			edStreet.setText(dto.cusInfo.getStreet());
    		}
    		
    		boolean isHaveC2Type = true;
    		
    		// danh sách C2
    		renderListC2();
    		spinnerC2.setSelection(dto.currentIndexC2);
    		//nếu có thuộc c2 thì ko hiển thị C2 
    		if (dto.curentIdC2 > 0) {
				isHaveC2Type = false;
				isHaveC2Current = false;
			}
    		
    		// Danh sách loại khách hàng
    		int indexC2Remove = renderListCusType(isHaveC2Type);
    		if (indexC2Remove >= 0) {
    			//nếu vị trí hiện tại nắm dưới C2, mà c2 đã bị remove => trừ index đi 1
				if (dto.currentIndexType > indexC2Remove) {
					dto.currentIndexType--;
				}
			}
    		spinnerType.setSelection(dto.currentIndexType);

    		// Danh sách tỉnh
    		renderListProvince();    		
    		spinnerProvine.setSelection(dto.currentIndexProvince);

    		// Danh sách huyện
    		renderListDicstrict();
    		spinnerDistrict.setSelection(dto.currentIndexDistrict);
    
    		// Danh sách xã
    		renderListPrecinct();
    		spinnerPrecinct.setSelection(dto.currentIndexPrecinct);
		}
	}

	public void updateDiaBanCustomer(CreateCustomerInfoDTO dto) {
		this.dto = dto;
		if(dto != null){
			// Danh sách tỉnh
			renderListProvince();
			spinnerProvine.setSelection(dto.currentIndexProvince);

			// Danh sách huyện
			renderListDicstrict();
			spinnerDistrict.setSelection(dto.currentIndexDistrict);

			// Danh sách xã
			renderListPrecinct();
			spinnerPrecinct.setSelection(dto.currentIndexPrecinct);
		}
	}

	/**
	 * hiện thị danh sách C2
	 * @author: duongdt3
	 * @since: 09:00:49 8 Jan 2014
	 * @return: void
	 * @throws:
	 */
	void renderListC2(){
		if(dto.listC2 != null){
			int sizeC2 = dto.listC2.cusList.size();
			String[] arrC2 = new String[sizeC2];

			for (int i = 0; i < sizeC2; i++) {
				CustomerListItem item = dto.listC2.cusList.get(i);
				arrC2[i] = item.aCustomer.customerName;
			}
			SpinnerAdapter adapterC2 = new SpinnerAdapter(getContext(), R.layout.simple_spinner_item, arrC2);
			spinnerC2.setAdapter(adapterC2);
		}
	}

	List<CustomerType> listCusTypeCurrent = new ArrayList<CustomerType>();
	boolean isHaveC2Current = true;
	/**
	 * hiện thị danh sách loại khách hàng
	 * @author: duongdt38
	 * @since: 09:00:58 8 Jan 2014
	 * @return: int
	 * @throws:  
	 * @param isHaveC2
	 * @return
	 */
	int renderListCusType(boolean isHaveC2){
		int indexC2 = -1;
		// Danh sách loại khách hàng
		if(dto.listCusType != null){
			int sizeCusType = dto.listCusType.size();
			listCusTypeCurrent.clear();
			for (int i = 0; i < sizeCusType; i++) {
				CustomerType item = dto.listCusType.get(i);
				//nếu ko render c2 => object_type = 4
				if (isHaveC2 || (!isHaveC2 && item.objectType != 4)) {
					listCusTypeCurrent.add(item);
				}else{
					indexC2 = i;
				}
			}
			
			int sizeCurrent = listCusTypeCurrent.size();
			if (sizeCurrent > 0) {
				String[] arrCusType = new String[sizeCurrent];
				for (int i = 0; i < sizeCurrent; i++) {
					arrCusType[i] = listCusTypeCurrent.get(i).typeName;
				}
				SpinnerAdapter adapterCusType = new SpinnerAdapter(getContext(), R.layout.simple_spinner_item, arrCusType);
				spinnerType.setAdapter(adapterCusType);
			}
		}
		
		isHaveC2Current = isHaveC2;
		return indexC2;
	}
	
	/**
	 * Hiển thị danh sách huyện
	 * @author: duongdt3
	 * @since: 09:01:10 8 Jan 2014
	 * @return: void
	 * @throws:
	 */
	void renderListDicstrict(){
		// Danh sách huyện
		if(dto.listDistrict != null){
			int sizeDistrict = dto.listDistrict.size();
			String[] arrDistrict = new String[sizeDistrict];

			for (int i = 0; i < sizeDistrict; i++) {
				AreaItem item = dto.listDistrict.get(i);
				arrDistrict[i] = item.areaName;
			}
			SpinnerAdapter adapterDistrict = new SpinnerAdapter(getContext(), R.layout.simple_spinner_item, arrDistrict);
			spinnerDistrict.setAdapter(adapterDistrict);
		}
	}


	void renderListProvince(){
		// Danh sách tỉnh
		if(dto.listProvine != null){
			int sizeProvine = dto.listProvine.size();
			String[] arrProvine = new String[sizeProvine];

			for (int i = 0; i < sizeProvine; i++) {
				AreaItem item = dto.listProvine.get(i);
				arrProvine[i] = item.areaName;
			}
			SpinnerAdapter adapterProvine = new SpinnerAdapter(getContext(), R.layout.simple_spinner_item, arrProvine);
			spinnerProvine.setAdapter(adapterProvine);

		}
	}
	
	/**
	 * Hiển thị danh sách xã
	 * @author: duongdt3
	 * @since: 09:01:35 8 Jan 2014
	 * @return: void
	 * @throws:
	 */
	void renderListPrecinct(){
		// Danh sách xã
		if(dto.listPrecinct != null){
			int sizePrecinct = dto.listPrecinct.size();
			String[] arrPrecinct = new String[sizePrecinct];

			for (int i = 0; i < sizePrecinct; i++) {
				AreaItem item = dto.listPrecinct.get(i);
				arrPrecinct[i] = item.areaName;
			}
			SpinnerAdapter adapterPrecinct = new SpinnerAdapter(getContext(), R.layout.simple_spinner_item, arrPrecinct);
			spinnerPrecinct.setAdapter(adapterPrecinct);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> spinner, View v, int pos, long id) {
		if (spinner == spinnerC2) {
			if (this.dto.currentIndexC2 != pos) {
				this.dto.setCurrentC2(pos);
				//tức là đã chọn 1 c2
				if (this.dto.curentIdC2 > 0) {
					//lúc trước đang có C2, giờ bỏ c2 ra 
					if (isHaveC2Current == true) {
						dto.currentIndexType = -1;
						//render lại spinner type
						renderListCusType(false);
					}
				}else{
					dto.currentIndexType = -1;
					renderListCusType(true);
				}
			}
		} else if (spinner == spinnerDistrict) {
			if (this.dto.currentIndexDistrict != pos) {
				this.dto.setCurrentDistrict(pos);
				//request lại danh sách xã
				parrent.requestDataArea(CUSTOMER_TABLE.AREA_TYPE_PRECINCT, this.dto.curentIdDistrict);
			}
		} else if (spinner == spinnerPrecinct) {
			if (this.dto.currentIndexPrecinct != pos) {
				this.dto.setCurrentPrecinct(pos);
			}
		} else if (spinner == spinnerProvine) {
			if (this.dto.currentIndexProvince != pos) {
				this.dto.setCurrentProvince(pos);
				//request lại danh sách huyện
				parrent.requestDataArea(CUSTOMER_TABLE.AREA_TYPE_DISTRICT, this.dto.curentIdProvine);
			}
		} else if (spinner == spinnerType) {
			if (this.dto.currentIndexType != pos) {
				//vì có thể bị thay đổi C2 hay không
				if (pos >= 0 && pos < listCusTypeCurrent.size()) {
					long typeId = listCusTypeCurrent.get(pos).typeId;
					this.dto.setCurrentType(pos, typeId);
				} 
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> spinner) {
		
	}

	/**
	 * set danh sách địa bàn theo loại render vào spinner
	 * @author: duongdt3
	 * @since: 17:14:16 6 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param type
	 * @param arrayArea
	 */
	public void setArrayAreaInfo(int type, ArrayList<AreaItem> arrayArea) {
		switch (type) {
    		case CUSTOMER_TABLE.AREA_TYPE_PRECINCT:
    			//list xã
    			dto.listPrecinct = arrayArea;
    			dto.setCurrentPrecinct(-1);
    			renderListPrecinct();
    			break;
    		case CUSTOMER_TABLE.AREA_TYPE_DISTRICT:
    			//list huyện
    			dto.listDistrict = arrayArea;
    			dto.setCurrentDistrict(-1);
    			renderListDicstrict();
    			break;
    		default:
    			break;
		}
	}

	/**
	 * check các input thông tin cho Khách hàng
	 * @author: duongdt3
	 * @since: 08:34:36 7 Jan 2014
	 * @return: boolean
	 * @throws:  
	 * @return
	 */
	public boolean checkInputInfo() {
		boolean isVaild = true;
		String strError = "";
		//remove khoảng trắng
		edCustomerName.setText(edCustomerName.getText().toString().trim());
		edContactName.setText(edContactName.getText().toString().trim());
		edHouseNumber.setText(edHouseNumber.getText().toString().trim());
		edStreet.setText(edStreet.getText().toString().trim());
		
		//check
		if (edCustomerName.getText().toString().isEmpty()) {
			// kiểm tra tên rỗng
			isVaild = false;
			strError = StringUtil.getString(R.string.TEXT_NOTIFY_CUSTOMER_NAME_NOT_NULL);
		} else if (!StringUtil.isCustomerNameContainValidChars(edCustomerName.getText().toString())) {
			// kiểm tra tên có chứa ký tự đặc biệt
			isVaild = false;
			strError = StringUtil.getString(R.string.TEXT_NOTIFY_CUSTOMER_NAME_NOT_VALID);
		} else if (edMobilePhone.getText().toString().isEmpty()) {
			// kiểm tra điện thoại di động rỗng
			isVaild = false;
			strError = StringUtil.getString(R.string.TEXT_NOTIFY_MOBILE_NOT_NULL);
		} else if (edPhone.getText().toString().isEmpty()) {
			// kiểm tra điện thoại bàn rỗng
			isVaild = false;
			strError = StringUtil.getString(R.string.TEXT_NOTIFY_PHONE_NOT_NULL);
		} else if (edContactName.getText().toString().isEmpty()) {
			// kiểm tra tên liên hệ rỗng
			isVaild = false;
			strError = StringUtil.getString(R.string.TEXT_NOTIFY_CONTACT_NAME_NOT_NULL);
		} else if (!StringUtil.isCustomerNameContainValidChars(edContactName.getText().toString())) {
			// kiểm tra tên liên hệ có chứa ký tự đặc biệt
			isVaild = false;
			strError = StringUtil.getString(R.string.TEXT_NOTIFY_CONTACT_NAME_NOT_VALID);
		} else if (!StringUtil.isNullOrEmpty(edHouseNumber.getText().toString()) && !StringUtil.isHouseNumberContainValidChars(edHouseNumber.getText().toString())) {
			// kiểm tra số nhà có giá trị + chứa ký tự đặc biệt
			isVaild = false;
			strError = StringUtil.getString(R.string.TEXT_NOTIFY_HOUSE_NUMBER_NOT_VALID);
		} else if (!StringUtil.isNullOrEmpty(edStreet.getText().toString()) && !StringUtil.isStreetContainValidChars(edStreet.getText().toString())) {
			// kiểm tra đường có giá trị + chứa ký tự đặc biệt 
			isVaild = false;
			strError = StringUtil.getString(R.string.TEXT_NOTIFY_HOUSE_STREET_NOT_VALID);
		} else if (dto.currentIndexType < 0) {
			// kiểm tra không có loại khách hàng
			isVaild = false;
			strError = StringUtil.getString(R.string.TEXT_NOTIFY_CUSTOMER_TYPE_NOT_NULL);
		} else if (dto.currentIndexPrecinct < 0) {
			// kiểm tra không có địa chỉ
			isVaild = false;
			strError = StringUtil.getString(R.string.TEXT_NOTIFY_PRECINCT_NOT_NULL);
		}
		
		tvError.setText(strError);
		return isVaild;
	}

	/**
	 * lấy thông tin từ view => cusInfo
	 * @author: duongdt3
	 * @since: 09:12:47 7 Jan 2014
	 * @return: void
	 * @throws:  
	 */
	public void getInfoFromView() {
		//id
		dto.cusInfo.setCustomerTypeId((int)dto.curentIdType);
		dto.cusInfo.setParentCustomerId(dto.curentIdC2);
		dto.cusInfo.setAreaId((int)dto.curentIdPrecinct);
		//text
		dto.cusInfo.setCustomerName(edCustomerName.getText().toString());
		dto.cusInfo.setMobilephone(edMobilePhone.getText().toString());
		dto.cusInfo.setPhone(edPhone.getText().toString());
		dto.cusInfo.setHouseNumber(edHouseNumber.getText().toString());
		dto.cusInfo.setStreet(edStreet.getText().toString());
		dto.cusInfo.setContactPerson(edContactName.getText().toString());

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		GlobalUtil.forceHideKeyboardInput(getContext(), this);
		return super.onTouchEvent(event);
	}
}
