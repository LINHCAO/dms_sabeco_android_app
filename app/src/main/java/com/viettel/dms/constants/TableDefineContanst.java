/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.constants;

import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;



/**
 * Define size/text cho cac tableLayout
 * 
 * @author: PhucNT
 * @version: 1.0
 * @since: 1.0
 */
public class TableDefineContanst {
	public final static String COLUMN_NAME_STT = "STT";
	public final static int COLUMN_WITDH_STT = 40;
	public final static String COLUMN_NAME_DATE = "Ngày";
	public final static int COLUMN_WITDH_DATE = 120;
	public final static String COLUMN_NAME_CUSTOMER_CODE = "Mã KH";
	public final static int COLUMN_WITDH_CUSTOMER_CODE = 170;
	public final static String COLUMN_NAME_CUSTOMER_NAME = "Mô tả";
	public final static int COLUMN_WITDH_CUSTOMER_NAME = 400;
	public final static String COLUMN_NAME_ORDER_NUMBER = "Số đơn hàng";
	public final static int COLUMN_WITDH_ORDER_NUMBER = 130;
	public final static String COLUMN_NAME_MONEY = "Thành tiền";
	public final static int COLUMN_WITDH_MONEY = 130;
	public final static String COLUMN_NAME_ORDER_EMPTY = "Mô tả";
	public final static int COLUMN_WITDH_ORDER_EMPTY = 280;
	public final static String COLUMN_NAME_WAY = "  ";
	public final static int COLUMN_WITDH_WAY = 70;
	public final static String COLUMN_NAME_SELECT = " Chuyển ";
	public final static int COLUMN_WIDTH_SELECT = 90;

	public final static String COLUMN_NAME_MMH = "Mã MH";
	public final static int COLUMN_WITDH_MMH = 110;
	public final static String COLUMN_NAME_TMH = "Tên mặt hàng";
	public final static int COLUMN_WITDH_TMH = 425;
	public final static String COLUMN_NAME_CONVFACT = "Đơn vị tính";
	public final static int COLUMN_WITDH_CONVFACT = 110;
	public final static String COLUMN_NAME_CTKM = "CTKM";
	public final static int COLUMN_WITDH_CTKM = 80;
	public final static String COLUMN_NAME_REMAIN_NUMBER = "SL tồn";
	public final static int COLUMN_WITDH_REMAIN_NUMBER = 120;
	public final static String COLUMN_NAME_HINT_NUMBER = "SL gợi ý";
	public final static int COLUMN_WITDH_HINT_NUMBER = 120;
	public final static String COLUMN_NAME_CHECK = "";
	public final static int COLUMN_WITDH_CHECK = 50;
	public final static String COLUMN_NAME_REMAIN = "Tồn kho";
	public final static int COLUMN_WITDH_REMAIN = 50;
	// bang danh sach don hang
	int[] CUSTOMER_LIST_TABLE_WIDTHS = { 45, 58, 200, 70, 240, 170, 80, 70 };
	/**
	 * set with and colum title for table find product add to order list
	 */
	public final static int COLUMN_WITDH_PRODUCT_CODE = 110;
	public final static String COLUMN_NAME_PRODUCT_CODE = "Mã mặt hàng";
	public final static int COLUMN_WITDH_PRODUCT_NAME = 237;
	public final static String COLUMN_NAME_PRODUCT_NAME = "Tên mặt hàng";
	public final static int COLUMN_WITDH_PRODUCT_PRICE = 95;
	public final static String COLUMN_NAME_PRODUCT_PRICE = "Đơn giá";
	public final static int COLUMN_WITDH_PRODUCT_INVENTORY = 90;
	public final static String COLUMN_NAME_PRODUCT_INVENTORY = "Tồn kho";
	public final static int COLUMN_WITDH_PRODUCT_UOM = 95;
	public final static String COLUMN_NAME_PRODUCT_UOM = "ĐVT";
	// public final static int COLUMN_WITDH_PRODUCT_STATUS = 50;
	// public final static String COLUMN_NAME_PRODUCT_STATUS = "TT";
	public final static int COLUMN_WITDH_PRODUCT_CTKM = 60;
	public final static String COLUMN_NAME_PRODUCT_CTKM = "CTKM";
	public final static int COLUMN_WITDH_PRODUCT_NUMBER = 95;
	public final static String COLUMN_NAME_PRODUCT_NUMBER = "SL";
	public final static int COLUMN_WITDH_PRODUCT_DETAIL = 106;
	public final static int COLUMN_WITDH_STATUS = 450;
	public final static String COLUMN_NAME_PRODUCT_DETAIL = "CT";
	/**
	 * set with and colum title for table general statistics
	 */
	public final static int COLUMN_WITDH_STATISTICS_CONTENT_DAY = 404;
	public final static String COLUMN_NAME_STATISTICS_CONTENT_DAY = "Nội dung";
	public final static int COLUMN_WITDH_STATISTICS_QUOTA_DAY = 180;
	public final static String COLUMN_NAME_STATISTICS_QUOTA_DAY = "Chỉ tiêu";
	public final static int COLUMN_WITDH_STATISTICS_ATTAIN_DAY = 180;
	public final static String COLUMN_NAME_STATISTICS_ATTAIN_DAY = "Đạt";
	public final static int COLUMN_WITDH_STATISTICS_REMAIN_DAY = 180;
	public final static String COLUMN_NAME_STATISTICS_REMAIN_DAY = "Còn lại";

	/**
	 * set with and colum title for table general statistics
	 */
	public final static int COLUMN_WITDH_STATISTICS_CONTENT_MONTH = 294;
	public final static String COLUMN_NAME_STATISTICS_CONTENT_MONTH = "Nội dung";
	public final static int COLUMN_WITDH_STATISTICS_QUOTA_MONTH = 170;
	public final static String COLUMN_NAME_STATISTICS_QUOTA_MONTH = "Chỉ tiêu";
	public final static int COLUMN_WITDH_STATISTICS_ATTAIN_MONTH = 170;
	public final static String COLUMN_NAME_STATISTICS_ATTAIN_MONTH = "Đạt";
	public final static int COLUMN_WITDH_STATISTICS_REMAIN_MONTH = 170;
	public final static String COLUMN_NAME_STATISTICS_REMAIN_MONTH = "Còn lại";
	public final static int COLUMN_WITDH_STATISTICS_PROGRESS_MONTH = 140;
	public final static String COLUMN_NAME_STATISTICS_PROGRESS_MONTH = "Tiến độ";

	/**
	 * set with and colum title for table promotin
	 */
	public final static int COLUMN_WITDH_PROMOTION_STT = 50;
	public final static String COLUMN_NAME_PROMOTION_STT = "STT";
	public final static int COLUMN_WITDH_PROMOTION_MACT = 200;
	public final static String COLUMN_NAME_PROMOTION_MACT = "Mã CT";
	public final static int COLUMN_WITDH_PROMOTION_TENCT = 400;
	public final static String COLUMN_NAME_PROMOTION_TENCT = "Tên CT";
	public final static int COLUMN_WITDH_PROMOTION_NGAYAD = 295;
	public final static String COLUMN_NAME_PROMOTION_NGAYAD = "Ngày áp dụng";

	/**
	 * set with and colum title for table business support
	 */
	public final static int COLUMN_WITDH_BUSINESS_SUPPORT_STT = 50;
	public final static String COLUMN_NAME_BUSINESS_SUPPORT_STT = "STT";
	public final static int COLUMN_WITDH_BUSINESS_SUPPORT_LOAICT = 200;
	public final static String COLUMN_NAME_BUSINESS_SUPPORT_LOAICT = "Loại CT";
	public final static int COLUMN_WITDH_BUSINESS_SUPPORT_SOLUONG = 695;
	public final static String COLUMN_NAME_BUSINESS_SUPPORT_SOLUONG = "Số lượng";

	/**
	 * set with and colum title for table display promotin
	 */
	public final static int COLUMN_WITDH_DIS_PROMOTION_STT = 50;
	public final static String COLUMN_NAME_DIS_PROMOTION_STT = "STT";
	public final static int COLUMN_WITDH_DIS_PROMOTION_MACT = 150;
	public final static String COLUMN_NAME_DIS_PROMOTION_MACT = "Mã CT";
	public final static int COLUMN_WITDH_DIS_PROMOTION_TENCT = 365;
	public final static String COLUMN_NAME_DIS_PROMOTION_TENCT = "Tên Chương trình";
	public final static int COLUMN_WITDH_DIS_PROMOTION_NGAYSTART = 125;
	public final static String COLUMN_NAME_DIS_PROMOTION_NGAYSTART = "Từ ngày";
	public final static int COLUMN_WITDH_DIS_PROMOTION_NGAYEND = 125;
	public final static String COLUMN_NAME_DIS_PROMOTION_NGAYEND = "Đến ngày";
	public final static int COLUMN_WITDH_DIS_PROMOTION_TYPE = 125;
	public final static String COLUMN_NAME_DIS_PROMOTION_TYPE = "Ngành hàng";

	/**
	 * set with and colum title for table display promotin
	 */
	public final static int COLUMN_WITDH_SUPER_DIS_PROMOTION_STT = 50;
	public final static String COLUMN_NAME_SUPER_DIS_PROMOTION_STT = "STT";
	public final static int COLUMN_WITDH_SUPER_DIS_PROMOTION_MACT = 150;
	public final static String COLUMN_NAME_SUPER_DIS_PROMOTION_MACT = "Mã CT";
	public final static int COLUMN_WITDH_SUPER_DIS_PROMOTION_TENCT = 365;
	public final static String COLUMN_NAME_SUPER_DIS_PROMOTION_TENCT = "Tên Chương trình";
	public final static int COLUMN_WITDH_SUPER_DIS_PROMOTION_NGAYSTART = 125;
	public final static String COLUMN_NAME_SUPER_DIS_PROMOTION_NGAYSTART = "Từ ngày";
	public final static int COLUMN_WITDH_SUPER_DIS_PROMOTION_NGAYEND = 125;
	public final static String COLUMN_NAME_SUPER_DIS_PROMOTION_NGAYEND = "Đến ngày";
	public final static int COLUMN_WITDH_SUPER_DIS_PROMOTION_TYPE = 125;
	public final static String COLUMN_NAME_SUPER_DIS_PROMOTION_TYPE = "Ngành hàng";

	/**
	 * set with and colum title for table display promotin (TBHV)
	 */
	public final static int COLUMN_WITDH_TBHV_DIS_PROMOTION_STT = 50;
	public final static String COLUMN_NAME_TBHV_DIS_PROMOTION_STT = "STT";
	public final static int COLUMN_WITDH_TBHV_DIS_PROMOTION_MACT = 150;
	public final static String COLUMN_NAME_TBHV_DIS_PROMOTION_MACT = "Mã CT";
	public final static int COLUMN_WITDH_TBHV_DIS_PROMOTION_TENCT = 365;
	public final static String COLUMN_NAME_TBHV_DIS_PROMOTION_TENCT = "Tên chương trình";
	public final static int COLUMN_WITDH_TBHV_DIS_PROMOTION_NGAYSTART = 125;
	public final static String COLUMN_NAME_TBHV_DIS_PROMOTION_NGAYSTART = "Từ ngày";
	public final static int COLUMN_WITDH_TBHV_DIS_PROMOTION_NGAYEND = 125;
	public final static String COLUMN_NAME_TBHV_DIS_PROMOTION_NGAYEND = "Đến ngày";
	public final static int COLUMN_WITDH_TBHV_DIS_PROMOTION_TYPE = 125;
	public final static String COLUMN_NAME_TBHV_DIS_PROMOTION_TYPE = "Ngành hàng";



	/**
	 * set with and colum title for table customer promotin
	 */
	public final static int COLUMN_WITDH_ITEM_PROMOTION_STT = 50;
	public final static String COLUMN_NAME_ITEM_PROMOTION_STT = "STT";
	public final static int COLUMN_WITDH_ITEM_PROMOTION_MACT = 100;
	public final static String COLUMN_NAME_ITEM_PROMOTION_MACT = "Mã SP";
	public final static int COLUMN_WITDH_ITEM_PROMOTION_TENCT = 890;
	public final static String COLUMN_NAME_ITEM_PROMOTION_TENCT = "Tên sản phẩm";

	/**
	 * set with and colum title for table list product
	 */
	public final static String COLUMN_NAME_PRODUCTS_STT = "STT";
	public final static int COLUMN_WITDH_PRODUCTS_STT = 45;
	public final static int COLUMN_WITDH_PRODUCTS_CODE = 150;
	public final static String COLUMN_NAME_PRODUCTS_CODE = "Mã SP";
	public final static int COLUMN_WITDH_PRODUCTS_NAME = 255;
	public final static String COLUMN_NAME_PRODUCTS_NAME = "Tên sản phẩm";
	public final static int COLUMN_WITDH_PRODUCTS_PRICE = 150;
	public final static String COLUMN_NAME_PRODUCTS_PRICE = "Đơn giá";
	public final static int COLUMN_WITDH_PRODUCTS_SPECIFICATION = 150;
	public final static String COLUMN_NAME_PRODUCTS_SPECIFICATION = "Quy cách";
	public final static int COLUMN_WITDH_PRODUCTS_STOCK = 150;
	public final static String COLUMN_NAME_PRODUCTS_STOCK = "Tồn kho";
	public final static int COLUMN_WITDH_PRODUCTS_TOTAL = 150;
	public final static String COLUMN_NAME_PRODUCTS_TOTAL = "Tổng hàng";

	/**
	 * set width and colum title for table statistics products
	 */
	public final static String COLUMN_NAME_STATISTICS_PRODUCT_CODE = "Mã MH";
	public final static int COLUMN_WIDTH_STATISTICS_PRODUCT_CODE = 150;
	public final static String COLUMN_NAME_STATISTICS_PRODUCT_NAME = "Tên MH";
	public final static int COLUMN_WIDTH_STATISTICS_PRODUCT_NAME = 255;
	public final static String COLUMN_NAME_STATISTICS_PRODUCT_PROMOTION = "Khuyến mãi";
	public final static int COLUMN_WIDTH_STATISTICS_PRODUCT_PROMOTION = 150;
	public final static String COLUMN_NAME_STATISTICS_PRODUCT_NUMBER_PRODUCT = "Số lượng";
	public final static int COLUMN_WIDTH_STATISTICS_PRODUCT_NUMBER_PRODUCT = 150;
	public final static String COLUMN_NAME_STATISTICS_PRODUCT_SOLD = "Đã bán";
	public final static int COLUMN_WIDTH_STATISTICS_PRODUCT_SOLD = 150;
	public final static String COLUMN_NAME_STATISTICS_PRODUCT_STOCK = "Còn lại";
	public final static int COLUMN_WIDTH_STATISTICS_PRODUCT_STOCK = 150;

	/**
	 * set width and column title for table vote display present product
	 * promotion
	 */
	public final static String COLUMN_NAME_VOTE_PRODUCT_CODE = "Mã mặt hàng";
	public final static int COLUMN_WIDTH_VOTE_PRODUCT_CODE = 150;
	public final static String COLUMN_NAME_VOTE_PRODUCT_NAME = "Tên mặt hàng";
	public final static int COLUMN_WIDTH_VOTE_PRODUCT_NAME = 485;
	public final static String COLUMN_NAME_VOTE_NUMBER_PRODUCT = "Số lượng";
	public final static int COLUMN_WIDTH_VOTE_NUMBER_PRODUCT = 132;
	public final static String COLUMN_NAME_VOTE_NUMBER = "Chấm";
	public final static int COLUMN_WIDTH_VOTE_NUMBER = 132;

	/**
	 * set width and colum title for table indebtedness list
	 */
	public final static String COLUMN_NAME_INDEBTEDNESS_CUSTOMMER_CODE = "Mã MH";
	public final static int COLUMN_WIDTH_INDEBTEDNESS_CUSTOMMER_CODE = 150;
	public final static String COLUMN_NAME_INDEBTEDNESS_CUSTOMMER_NAME = "Tên MH";
	public final static int COLUMN_WIDTH_INDEBTEDNESS_CUSTOMMER_NAME = 300;
	public final static String COLUMN_NAME_INDEBTEDNESS_BALANCE = "Dư nợ";
	public final static int COLUMN_WIDTH_INDEBTEDNESS_BALANCE = 200;
	public final static String COLUMN_NAME_INDEBTEDNESS_STATUS = "Trạng thái";
	public final static int COLUMN_WIDTH_INDEBTEDNESS_STATUS = 150;
	public final static String COLUMN_NAME_INDEBTEDNESS_TIME = "Thời gian";
	public final static int COLUMN_WIDTH_INDEBTEDNESS_TIME = 150;
	public final static String COLUMN_NAME_INDEBTEDNESS_DETAIL = "  ";
	public final static int COLUMN_WIDTH_INDEBTEDNESS_DETAIL = 50;

	/**
	 * set chieu rong va title cho bang doanh so cua khach hang
	 */
	public final static String COLUMN_NAME_CUS_SALE_STT = "STT";
	public final static int COLUMN_WIDTH_CUS_SALE_STT = 40;
	public final static String COLUMN_NAME_CUS_SALE_GROUP = "Nhóm doanh số";
	public final static int COLUMN_WIDTH_CUS_SALE_GROUP = 250;
	public final static String COLUMN_NAME_CUS_SALE_DG = "Diễn giải";
	public final static int COLUMN_WIDTH_CUS_SALE_DG = 400;
	public final static String COLUMN_NAME_CUS_SALE_MDS = "Mức doanh số";
	public final static int COLUMN_WIDTH_CUS_SALE_MDS = 250;

	/**
	 * set chieu rong va title cho bang nhung don hang gan day cua khach hang
	 */
	public final static String COLUMN_NAME_CUS_LAST_ORDER_STT = "STT";
	public final static int COLUMN_WIDTH_CUS_LAST_ORDER_STT = 50;
	public final static String COLUMN_NAME_CUS_LAST_ORDER_CODE = "Số đơn hàng";
	public final static int COLUMN_WIDTH_CUS_LAST_ORDER_CODE = 250;
	public final static String COLUMN_NAME_CUS_LAST_ORDER_DATE = "Ngày";
	public final static int COLUMN_WIDTH_CUS_LAST_ORDER_DATE = 250;
	public final static String COLUMN_NAME_CUS_LAST_ORDER_SKU = "SKU";
	public final static int COLUMN_WIDTH_CUS_LAST_ORDER_SKU = 195;
	public final static String COLUMN_NAME_CUS_LAST_ORDER_MONEY = "Thành tiền";
	public final static int COLUMN_WIDTH_CUS_LAST_ORDER_MONEY = 195;

	/**
	 * set chieu rong va title cho bang nhung don hang gan day cua khach hang
	 */
	public final static String COLUMN_NAME_CUS_TOOL_STT = "STT";
	public final static int COLUMN_WIDTH_CUS_TOOL_STT = 40;
	public final static String COLUMN_NAME_CUS_TOOL_CODE = "Mã công cụ";
	public final static int COLUMN_WIDTH_CUS_TOOL_CODE = 210;
	public final static String COLUMN_NAME_CUS_TOOL_NAME = "Tên công cụ";
	public final static int COLUMN_WIDTH_CUS_TOOL_NAME = 500;
	public final static String COLUMN_NAME_CUS_TOOL_TARGET = "Chỉ tiêu";
	public final static int COLUMN_WIDTH_CUS_TOOL_TARGET = 150;
	public final static String COLUMN_NAME_CUS_TOOL_FACT = "Đã đặt";
	public final static int COLUMN_WIDTH_CUS_TOOL_FACT = 150;

	/**
	 * set with and colum title for table select product has promotion
	 */
	public final static int COLUMN_WITDH_PRODUCT_PROMOTION_CODE = 160;
	public final static String COLUMN_NAME_PRODUCT_PROMOTION_CODE = "Mã mặt hàng";
	public final static int COLUMN_WITDH_PRODUCT_PROMOTION_NAME = 270;
	public final static String COLUMN_NAME_PRODUCT_PROMOTION_NAME = "Tên mặt hàng";
	public final static int COLUMN_WITDH_PRODUCT_PROMOTION_INVENTORY = 150;
	public final static String COLUMN_NAME_PRODUCT_PROMOTION_INVENTORY = "Tồn kho";
	// public final static int COLUMN_WITDH_PRODUCT_PROMOTION_TOTAL_PRODUCT =
	// 150;
	// public final static String COLUMN_NAME_PRODUCT_PROMOTION_TOTAL_PRODUCT =
	// "Đơn giá";
	public final static int COLUMN_WITDH_PRODUCT_PROMOTION_NUM_PROMOTION = 150;
	public final static String COLUMN_NAME_PRODUCT_PROMOTION_NUM_PROMOTION = "Số lượng";

	/**
	 * set width and title name for table in popup list programe select for
	 * product
	 */
	public final static String COLUMN_NAME_PROGRAME_STT = "STT";
	public final static String COLUMN_NAME_CUSTOMER = "Tên KH";
	public final static String COLUMN_ADDRESS = "Địa chỉ";
	public final static String COLUMN_NVBH = "NVBH";
	public final static String COLUMN_MA_NVBH = "Mã NVBH";
	public final static String COLUMN_SO_THIET_BI = "Số thiết bị";
	public final static String COLUMN_KHONG_DAT = "Không đạt";
	public final static String COLUMN_TUYEN = "Tuyến";
	public final static String COLUMN_SLGT = "SLGT";
	public final static String COLUMN_DS_THANG_TRUOC = "DS tháng trước";
	public final static String COLUMN_DATE_DH_LAST = "Ngày DH cuối";
	public final static int COLUMN_WIDTH_PROGRAME_STT = 45;
	public final static String COLUMN_NAME_PROGRAME_CODE = "Mã CT";
	public final static int COLUMN_WIDTH_PROGRAME_CODE = 200;
	public final static String COLUMN_NAME_PROGRAME_NAME = "Tên CT";
	public final static int COLUMN_WIDTH_PROGRAME_NAME = 300;
	public final static String COLUMN_NAME_PROGRAME_TYPE = "Loại CT";
	public final static int COLUMN_WIDTH_PROGRAME_TYPE = 122;

	/**
	 * set with and colum title for table display promotin
	 */
	public final static int COLUMN_WITDH_REPORT_DISPLAY_PROGRAME_DETAIL_CUSTOMER_CODE = 100;
	public final static String COLUMN_NAME_REPORT_DISPLAY_PROGRAME_DETAIL_CUSTOMER_CODE = "Mã KH";
	public final static int COLUMN_WITDH_REPORT_DISPLAY_PROGRAME_DETAIL_CUSTOMER_NAME = 280;
	public final static String COLUMN_NAME_REPORT_DISPLAY_PROGRAME_DETAIL_CUSTOMER_NAME = "Tên khách hàng";
	public final static int COLUMN_WITDH_REPORT_DISPLAY_PROGRAME_DETAIL_CUSTOMER_ADDRESS = 220;
	public final static String COLUMN_NAME_REPORT_DISPLAY_PROGRAME_DETAIL_CUSTOMER_ADDRESS = "Địa chỉ";
	public final static int COLUMN_WITDH_REPORT_DISPLAY_PROGRAME_DETAIL_CUSTOMER_LEVEL = 53;
	public final static String COLUMN_NAME_REPORT_DISPLAY_PROGRAME_DETAIL_CUSTOMER_LEVEL = "Mức";
	public final static int COLUMN_WITDH_REPORT_DISPLAY_PROGRAME_DETAIL_AMOUNT_REMAIN = 145;
	public final static String COLUMN_NAME_REPORT_DISPLAY_PROGRAME_DETAIL_AMOUNT_REMAIN = "DS còn lại (ĐVT:1000Đ)";
	public final static int COLUMN_WITDH_REPORT_DISPLAY_PROGRAME_DETAIL_AMOUNT_PLAN = 145;
	public final static String COLUMN_NAME_REPORT_DISPLAY_PROGRAME_DETAIL_AMOUNT_PLAN = "DS định mức (ĐVT:1000Đ)";

	/**
	 * set width and colum title for table selected product add to reviews staff
	 */
	public final static String COLUMN_NAME_PRODUCT_CODE_ADD_REVIEWS_STAFF = "Mã mặt hàng";
	public final static int COLUMN_WIDTH_PRODUCT_CODE_ADD_REVIEWS_STAFF = 130;
	public final static String COLUMN_NAME_PRODUCT_NAME_ADD_REVIEWS_STAFF = "Tên mặt hàng";
	public final static int COLUMN_WIDTH_PRODUCT_NAME_ADD_REVIEWS_STAFF = 340;
	public final static String COLUMN_NAME_PRODUCT_PRICE_ADD_REVIEWS_STAFF = "Đơn giá";
	public final static int COLUMN_WIDTH_PRODUCT_PRICE_ADD_REVIEWS_STAFF = 120;
	public final static String COLUMN_NAME_PROGRAME_CODE_ADD_REVIEWS_STAFF = "CTKM";
	public final static int COLUMN_WIDTH_PROGRAME_CODE_ADD_REVIEWS_STAFF = 60;
	public final static int COLUMN_WIDTH_CHECK_BOX_ADD_REVIEWS_STAFF = 60;

	/**
	 * set with and colum title for table list product sale satatistics
	 */
	public final static int COLUMN_WITDH_PRODUCTS_CODE_SALE_STATISTICS = 100;
	// chu y dung chung cho nhieu man hinh
	public final static String COLUMN_NAME_PRODUCTS_CODE_SALE_STATISTICS = "Mã hàng"; 
	public final static int COLUMN_WITDH_PRODUCTS_NAME_SALE_STATISTICS = 210;
	public final static String COLUMN_NAME_PRODUCTS_NAME_SALE_STATISTICS = "Tên mặt hàng";
	public final static int COLUMN_WITDH_INDUSTRY_PRODUCT_SALE_STATISTICS = 45;
	public final static String COLUMN_NAME_INDUSTRY_PRODUCT_SALE_STATISTICS = "NH";
	public final static int COLUMN_WITDH_CONVFACT_SALE_STATISTICS = 123;
	public final static String COLUMN_NAME_CONVFACT_SALE_STATISTICS = "Quy cách";
	public final static int COLUMN_WITDH_PRODUCTS_PRICE_SALE_STATISTICS = 140;
	public final static String COLUMN_NAME_PRODUCTS_PRICE_SALE_STATISTICS = "Đơn giá";
	public final static int COLUMN_WITDH_PRODUCTS_NUMBER_SALE_STATISTICS = 135;
	public final static String COLUMN_NAME_PRODUCTS_NUMBER_SALE_STATISTICS = "Số lượng";
	public final static int COLUMN_WITDH_PRODUCTS_TOTAL_MONNEY_SALE_STATISTICS = 140;
	public final static String COLUMN_NAME_PRODUCTS_TOTAL_MONNEY_SALE_STATISTICS = "Tổng tiền";

	public final static String COLUMN_PRODUCT_NAME = "Tên hàng";


	// thong tin header bang doanh so don hang gan nhat : doanh so khach hang
	public static int[] LAST_ORDER_CUSTOMER_TABLE_WIDTHS = {
			COLUMN_WIDTH_CUS_LAST_ORDER_STT, COLUMN_WIDTH_CUS_LAST_ORDER_CODE,
			COLUMN_WIDTH_CUS_LAST_ORDER_DATE, COLUMN_WIDTH_CUS_LAST_ORDER_SKU,
			COLUMN_WIDTH_CUS_LAST_ORDER_MONEY };
	public static String[] LAST_ORDER_CUSTOMER_TABLE_TITLES = {
			COLUMN_NAME_CUS_SALE_STT, COLUMN_NAME_CUS_LAST_ORDER_CODE,
			COLUMN_NAME_CUS_LAST_ORDER_DATE, COLUMN_NAME_CUS_LAST_ORDER_SKU,
			COLUMN_NAME_CUS_LAST_ORDER_MONEY };


	// thong tin header mat hang - module dat hang
	public static int[] ORDER_TABLE_WIDTHS = { COLUMN_WITDH_STT,
			COLUMN_WITDH_ORDER_NUMBER, COLUMN_WITDH_CUSTOMER_CODE,
			COLUMN_WITDH_DATE, COLUMN_WITDH_MONEY, COLUMN_WITDH_ORDER_EMPTY,
			COLUMN_WITDH_WAY };
	public static String[] ORDER_TABLE_TITLES = { COLUMN_NAME_STT,
			COLUMN_NAME_ORDER_NUMBER, "Khách hàng",
			COLUMN_NAME_DATE, COLUMN_NAME_MONEY, COLUMN_NAME_ORDER_EMPTY,
			COLUMN_NAME_WAY };

	// bang mat hang ton kho
	public static int[] REMAIN_PRODUCT_TABLE_WIDTHS = { COLUMN_WITDH_STT,
			COLUMN_WITDH_MMH, COLUMN_WITDH_TMH, COLUMN_WITDH_CONVFACT,
			COLUMN_WITDH_CTKM, COLUMN_WITDH_REMAIN_NUMBER,
			COLUMN_WITDH_CHECK };
	public static String[] REMAIN_PRODUCT_TABLE_TITLES = { COLUMN_NAME_STT,
			COLUMN_NAME_MMH, COLUMN_NAME_TMH, COLUMN_NAME_CONVFACT, COLUMN_NAME_REMAIN_NUMBER,
			COLUMN_NAME_CHECK };


	public static int[] FIND_PRODUCT_TABLE_WIDTHS = {
			COLUMN_WITDH_PRODUCTS_STT, COLUMN_WITDH_PRODUCT_CODE,
			COLUMN_WITDH_PRODUCT_NAME, COLUMN_WITDH_PRODUCT_UOM, 
			COLUMN_WITDH_PRODUCT_INVENTORY, COLUMN_WITDH_PRODUCT_CTKM, COLUMN_WITDH_PRODUCT_PRICE,
			COLUMN_WITDH_PRODUCT_NUMBER, COLUMN_WITDH_PRODUCT_DETAIL };
	public static String[] FIND_PRODUCT_TABLE_TITLES = {
			COLUMN_NAME_PRODUCTS_STT, COLUMN_NAME_PRODUCT_CODE,
			COLUMN_NAME_PRODUCT_NAME, COLUMN_NAME_PRODUCT_UOM,
			COLUMN_NAME_PRODUCT_INVENTORY, COLUMN_NAME_PRODUCT_CTKM, COLUMN_NAME_PRODUCT_PRICE,
			COLUMN_NAME_PRODUCT_NUMBER, COLUMN_NAME_PRODUCT_DETAIL };



	//set width and colum name for table promotion list
	public static int[] PROMOTION_TABLE_WIDTHS = { COLUMN_WITDH_PROMOTION_STT,
			COLUMN_WITDH_PROMOTION_MACT, COLUMN_WITDH_PROMOTION_TENCT,
			COLUMN_WITDH_PROMOTION_NGAYAD };
	public static String[] PROMOTION_TABLE_TITLES = {
			COLUMN_NAME_PROMOTION_STT, COLUMN_NAME_PROMOTION_MACT,
			COLUMN_NAME_PROMOTION_TENCT, COLUMN_NAME_PROMOTION_NGAYAD };

	//set width and colum name for table display promotion list
	public static int[] DIS_PROMOTION_TABLE_WIDTHS = {
			COLUMN_WITDH_DIS_PROMOTION_STT, COLUMN_WITDH_DIS_PROMOTION_MACT,
			COLUMN_WITDH_DIS_PROMOTION_TENCT,
			COLUMN_WITDH_DIS_PROMOTION_NGAYSTART,
			COLUMN_WITDH_DIS_PROMOTION_NGAYEND, COLUMN_WITDH_DIS_PROMOTION_TYPE };
	public static String[] DIS_PROMOTION_TABLE_TITLES = {
			COLUMN_NAME_DIS_PROMOTION_STT, COLUMN_NAME_DIS_PROMOTION_MACT,
			COLUMN_NAME_DIS_PROMOTION_TENCT,
			COLUMN_NAME_DIS_PROMOTION_NGAYSTART,
			COLUMN_NAME_DIS_PROMOTION_NGAYEND, COLUMN_NAME_DIS_PROMOTION_TYPE };

	
	
	//set width and colum name for table display promotion list
	public static int[] CUS_PROMOTION_TABLE_WIDTHS = { 50, 150, 445, 150, 150 };
	public static String[] CUS_PROMOTION_TABLE_TITLES = {
			StringUtil.getString(R.string.TEXT_STT),
			StringUtil.getString(R.string.TEXT_TB_CUSTOMER_CODE),
			StringUtil.getString(R.string.TEXT_TB_CUSTOMER_NAME),
			StringUtil.getString(R.string.TEXT_TB_CUSTOMER_LEVEL),
			StringUtil.getString(R.string.TEXT_TB_CUSTOMER_SALES_REMAIN) };


	// header thong tin mat hang ban - module dat hang
	public static int[] ORDER_PRODUCT_HEADER_WIDTH = { 40, 90, 193, 80, 80, 100,
			100, 130, 70, 60 };
	public static String[] ORDER_PRODUCT_HEADER_TITLE = { COLUMN_NAME_STT,
		COLUMN_NAME_PRODUCTS_CODE_SALE_STATISTICS, COLUMN_PRODUCT_NAME, "ĐVT", COLUMN_NAME_REMAIN,
			COLUMN_NAME_PRODUCT_PRICE, "Số lượng đặt", "Thành tiền", "CTKM", " " };
	
	// header thong tin mat hang khuyen mai - module dat hang
	public static int[] ORDER_PROMOTION_PRODUCT_HEADER_WIDTH = {
			COLUMN_WITDH_STT, 90, 245, 80, 230, 70, 120, 70 };
	public static String[] ORDER_PROMOTION_PRODUCT_HEADER_TITLE = {
			COLUMN_NAME_STT, COLUMN_NAME_PRODUCTS_CODE_SALE_STATISTICS, COLUMN_PRODUCT_NAME,
			COLUMN_NAME_REMAIN, "KM", "%CK", "HTTM", " " };


	//set width and colum title for table statistics total products
	public static int[] STATISTICS_TOTAL_PRODUCTS_TABLE_WIDTHS = {
			COLUMN_WITDH_PRODUCTS_STT, COLUMN_WIDTH_STATISTICS_PRODUCT_CODE,
			COLUMN_WIDTH_STATISTICS_PRODUCT_NAME,
			COLUMN_WIDTH_STATISTICS_PRODUCT_PROMOTION,
			COLUMN_WIDTH_STATISTICS_PRODUCT_NUMBER_PRODUCT,
			COLUMN_WIDTH_STATISTICS_PRODUCT_SOLD,
			COLUMN_WIDTH_STATISTICS_PRODUCT_STOCK };
	public static String[] STATISTICS_TOTAL_PRODUCTS_TABLE_TITLES = {
			COLUMN_NAME_PRODUCTS_STT, COLUMN_NAME_STATISTICS_PRODUCT_CODE,
			COLUMN_NAME_STATISTICS_PRODUCT_NAME,
			COLUMN_NAME_STATISTICS_PRODUCT_PROMOTION,
			COLUMN_NAME_STATISTICS_PRODUCT_NUMBER_PRODUCT,
			COLUMN_NAME_STATISTICS_PRODUCT_SOLD,
			COLUMN_NAME_STATISTICS_PRODUCT_STOCK };

	//set width and colum title for table vote display present product
	// promotion
	public static int[] VOTE_DISPLAY_PRESENT_PRODUCT_TABLE_WIDTHS = {
			COLUMN_WITDH_PRODUCTS_STT, COLUMN_WIDTH_VOTE_PRODUCT_CODE,
			COLUMN_WIDTH_VOTE_PRODUCT_NAME, COLUMN_WIDTH_VOTE_NUMBER_PRODUCT,
			COLUMN_WIDTH_VOTE_NUMBER };
	public static String[] VOTE_DISPLAY_PRESENT_PRODUCT_TABLE_TITLES = {
			COLUMN_NAME_PRODUCTS_STT, COLUMN_NAME_VOTE_PRODUCT_CODE,
			COLUMN_NAME_VOTE_PRODUCT_NAME, COLUMN_NAME_VOTE_NUMBER_PRODUCT,
			COLUMN_NAME_VOTE_NUMBER };

	//set width and colum title for table indebtedness
	public static int[] INDEBTEDNESS_TABLE_WIDTHS = {
			COLUMN_WITDH_PRODUCTS_STT,
			COLUMN_WIDTH_INDEBTEDNESS_CUSTOMMER_CODE,
			COLUMN_WIDTH_INDEBTEDNESS_CUSTOMMER_NAME,
			COLUMN_WIDTH_INDEBTEDNESS_BALANCE,
			COLUMN_WIDTH_INDEBTEDNESS_STATUS, COLUMN_WIDTH_INDEBTEDNESS_TIME,
			COLUMN_WIDTH_INDEBTEDNESS_DETAIL };
	public static String[] INDEBTEDNESS_TABLE_TITLES = {
			COLUMN_NAME_PRODUCTS_STT, COLUMN_NAME_INDEBTEDNESS_CUSTOMMER_CODE,
			COLUMN_NAME_INDEBTEDNESS_CUSTOMMER_NAME,
			COLUMN_NAME_INDEBTEDNESS_BALANCE, COLUMN_NAME_INDEBTEDNESS_STATUS,
			COLUMN_NAME_INDEBTEDNESS_TIME, COLUMN_NAME_INDEBTEDNESS_DETAIL };


	public static int[] SELECT_PRODUCT_PROMOTION_TABLE_WIDTHS = {
			COLUMN_WITDH_PRODUCTS_STT, COLUMN_WITDH_PRODUCT_PROMOTION_CODE,
			COLUMN_WITDH_PRODUCT_PROMOTION_NAME,
			COLUMN_WITDH_PRODUCT_PROMOTION_INVENTORY,
			COLUMN_WITDH_PRODUCT_PROMOTION_NUM_PROMOTION };
	public static String[] SELECT_PRODUCT_PROMOTION_TABLE_TITLES = {
			COLUMN_NAME_PRODUCTS_STT, COLUMN_NAME_PRODUCT_PROMOTION_CODE,
			COLUMN_NAME_PRODUCT_PROMOTION_NAME,
			COLUMN_NAME_PRODUCT_PROMOTION_INVENTORY,
			COLUMN_NAME_PRODUCT_PROMOTION_NUM_PROMOTION };

	//set width and colum name for table in select programe list for product
	public static int[] SELECT_PROGRAME_TABLE_WIDTHS = {
			COLUMN_WIDTH_PROGRAME_STT, COLUMN_WIDTH_PROGRAME_CODE,
			COLUMN_WIDTH_PROGRAME_NAME, COLUMN_WIDTH_PROGRAME_TYPE };
	public static String[] SELECT_PROGRAME_TABLE_TITLES = {
			COLUMN_NAME_PROGRAME_STT, COLUMN_NAME_PROGRAME_CODE,
			COLUMN_NAME_PROGRAME_NAME, COLUMN_NAME_PROGRAME_TYPE };

	//set width and colum name for table in select promotion for promotion type order
	public static int[] SELECT_PROMOTION_TABLE_WIDTHS = { 45, 200, 300, 130 };
	public static String[] SELECT_PROMOTION_TABLE_TITLES = {
			COLUMN_NAME_PROGRAME_STT, COLUMN_NAME_PROGRAME_CODE,
			COLUMN_NAME_CUSTOMER_NAME, COLUMN_NAME_STATISTICS_PRODUCT_PROMOTION };



	//set width and colum name for table quan ly thiet bi
	public static int[] EQUIPMENT_TABLE_WIDTHS = { 150, 495, 150, 150 };
	public static String[] EQUIPMENT_TABLE_TITLES = { COLUMN_MA_NVBH,
			COLUMN_NVBH, COLUMN_SO_THIET_BI, COLUMN_KHONG_DAT };

	//set width and colum name for table quan ly thiet bi chi tiet tung NPP
	public static int[] EQUIPMENT_DETAIL_TABLE_WIDTHS = { 150, 495, 150, 150 };
	public static String[] EQUIPMENT_DETAIL_TABLE_TITLES = { COLUMN_MA_NVBH,
			COLUMN_NVBH, COLUMN_SO_THIET_BI, COLUMN_KHONG_DAT };

	//set width and colum name for table bao cao chi tiet chuong trinh trung
	//bay phan khach hang
	public static int[] REPORT_DISPLAY_PROGRAME_DETAIL_WIDTHS = {
			COLUMN_WITDH_REPORT_DISPLAY_PROGRAME_DETAIL_CUSTOMER_CODE,
			COLUMN_WITDH_REPORT_DISPLAY_PROGRAME_DETAIL_CUSTOMER_NAME,
			COLUMN_WITDH_REPORT_DISPLAY_PROGRAME_DETAIL_CUSTOMER_ADDRESS,
			COLUMN_WITDH_REPORT_DISPLAY_PROGRAME_DETAIL_CUSTOMER_LEVEL,
			COLUMN_WITDH_REPORT_DISPLAY_PROGRAME_DETAIL_AMOUNT_PLAN,
			COLUMN_WITDH_REPORT_DISPLAY_PROGRAME_DETAIL_AMOUNT_REMAIN };
	public static String[] REPORT_DISPLAY_PROGRAME_DETAIL_TITLES = {
			COLUMN_NAME_REPORT_DISPLAY_PROGRAME_DETAIL_CUSTOMER_CODE,
			COLUMN_NAME_REPORT_DISPLAY_PROGRAME_DETAIL_CUSTOMER_NAME,
			COLUMN_NAME_REPORT_DISPLAY_PROGRAME_DETAIL_CUSTOMER_ADDRESS,
			COLUMN_NAME_REPORT_DISPLAY_PROGRAME_DETAIL_CUSTOMER_LEVEL,
			COLUMN_NAME_REPORT_DISPLAY_PROGRAME_DETAIL_AMOUNT_PLAN,
			COLUMN_NAME_REPORT_DISPLAY_PROGRAME_DETAIL_AMOUNT_REMAIN };

	//set width and colum name for table display promotion list (GSNPP)
	public static int[] SUPER_DIS_PROMOTION_TABLE_WIDTHS = {
			COLUMN_WITDH_SUPER_DIS_PROMOTION_STT,
			COLUMN_WITDH_SUPER_DIS_PROMOTION_MACT,
			COLUMN_WITDH_SUPER_DIS_PROMOTION_TENCT,
			COLUMN_WITDH_SUPER_DIS_PROMOTION_NGAYSTART,
			COLUMN_WITDH_SUPER_DIS_PROMOTION_NGAYEND,
			COLUMN_WITDH_SUPER_DIS_PROMOTION_TYPE };
	public static String[] SUPER_DIS_PROMOTION_TABLE_TITLES = {
			COLUMN_NAME_SUPER_DIS_PROMOTION_STT,
			COLUMN_NAME_SUPER_DIS_PROMOTION_MACT,
			COLUMN_NAME_SUPER_DIS_PROMOTION_TENCT,
			COLUMN_NAME_SUPER_DIS_PROMOTION_NGAYSTART,
			COLUMN_NAME_SUPER_DIS_PROMOTION_NGAYEND,
			COLUMN_NAME_SUPER_DIS_PROMOTION_TYPE };

	//set width and colum name for table display promotion list (TBHV)
	public static int[] TBHV_DIS_PROMOTION_TABLE_WIDTHS = {
			COLUMN_WITDH_TBHV_DIS_PROMOTION_STT,
			COLUMN_WITDH_TBHV_DIS_PROMOTION_MACT,
			COLUMN_WITDH_TBHV_DIS_PROMOTION_TENCT,
			COLUMN_WITDH_TBHV_DIS_PROMOTION_NGAYSTART,
			COLUMN_WITDH_TBHV_DIS_PROMOTION_NGAYEND,
			COLUMN_WITDH_TBHV_DIS_PROMOTION_TYPE };
	public static String[] TBHV_DIS_PROMOTION_TABLE_TITLES = {
			COLUMN_NAME_TBHV_DIS_PROMOTION_STT,
			COLUMN_NAME_TBHV_DIS_PROMOTION_MACT,
			COLUMN_NAME_TBHV_DIS_PROMOTION_TENCT,
			COLUMN_NAME_TBHV_DIS_PROMOTION_NGAYSTART,
			COLUMN_NAME_TBHV_DIS_PROMOTION_NGAYEND,
			COLUMN_NAME_TBHV_DIS_PROMOTION_TYPE };

	//set width and colum name for table promotion list part super visor
	public static int[] SUPER_PROMOTION_TABLE_WIDTHS = {
			COLUMN_WITDH_PROMOTION_STT, COLUMN_WITDH_PROMOTION_MACT,
			COLUMN_WITDH_PROMOTION_TENCT, COLUMN_WITDH_PROMOTION_NGAYAD };
	public static String[] SUPER_PROMOTION_TABLE_TITLES = {
			COLUMN_NAME_PROMOTION_STT, COLUMN_NAME_PROMOTION_MACT,
			COLUMN_NAME_PROMOTION_TENCT, COLUMN_NAME_PROMOTION_NGAYAD };

	//set width and colum name for table promotion list part TBHV
	public static int[] TBHV_PROMOTION_TABLE_WIDTHS = {
			COLUMN_WITDH_PROMOTION_STT, COLUMN_WITDH_PROMOTION_MACT,
			COLUMN_WITDH_PROMOTION_TENCT, COLUMN_WITDH_PROMOTION_NGAYAD };
	public static String[] TBHV_PROMOTION_TABLE_TITLES = {
			COLUMN_NAME_PROMOTION_STT, COLUMN_NAME_PROMOTION_MACT,
			COLUMN_NAME_PROMOTION_TENCT, COLUMN_NAME_PROMOTION_NGAYAD };

	//set width and colum name for table promotion list part TBHV
	public static int[] TBHV_BUSINESS_SUPPORT_TABLE_WIDTHS = {
			COLUMN_WITDH_BUSINESS_SUPPORT_STT,
			COLUMN_WITDH_BUSINESS_SUPPORT_LOAICT,
			COLUMN_WITDH_BUSINESS_SUPPORT_SOLUONG };
	public static String[] TBHV_BUSINESS_SUPPORT_TABLE_TITLES = {
			COLUMN_NAME_BUSINESS_SUPPORT_STT,
			COLUMN_NAME_BUSINESS_SUPPORT_LOAICT,
			COLUMN_NAME_BUSINESS_SUPPORT_SOLUONG };

	//set width and colum name for table in select programe list for product
	public static int[] SELECT_PRODUCT_ADD_REVIEWS_STAFF_TABLE_WIDTHS = {
			45,
			130,
			340,
			120,
			60 };
	public static String[] SELECT_PRODUCT_ADD_REVIEWS_STAFF_TABLE_TITLES = {
			COLUMN_NAME_PROGRAME_STT,
			COLUMN_NAME_PRODUCT_CODE_ADD_REVIEWS_STAFF,
			COLUMN_NAME_PRODUCT_NAME_ADD_REVIEWS_STAFF,

			COLUMN_NAME_PRODUCT_PRICE_ADD_REVIEWS_STAFF,
			COLUMN_NAME_PROGRAME_CODE_ADD_REVIEWS_STAFF };

	
	// set width and colum name for table promotion list part super visor
	public static int[] SUPER_FOLLOW_PROBLEM_TABLE_WIDTH = { 45, 150, 140, 180,
			243, 110, 110, 60 };
	public static String[] SUPER_FOLLOW_PROBLEM_TABLE_TITLES = {
			StringUtil.getString(R.string.TEXT_STT),
			StringUtil.getString(R.string.TEXT_COLUM_NVBH_CODE),
			StringUtil.getString(R.string.TEXT_CUSTOMER),
			StringUtil.getString(R.string.TEXT_TYPE_PROBLEM),
			StringUtil.getString(R.string.CONTENT),
			StringUtil.getString(R.string.STATE),
			StringUtil.getString(R.string.TEXT_CREATE_DATE),
			StringUtil.getString(R.string.TEXT_FOLLOW_PROBLEM_OK) };
	
	// set width and colum name for table problem of TBHV
	public static int[] TBHV_FOLLOW_PROBLEM_TABLE_WIDTH = { 45, 197, 150, 200,
			95 ,100, 100, 50 };
	public static String[] TBHV_FOLLOW_PROBLEM_TABLE_TITLES = { "STT", "GSNPP/TTTT",
			"Loại vấn đề", "Nội dung", "Trạng thái" ,"Ngày tạo", "Ngày nhắc nhở", "OK" };

	
	// set width and colum title for table in sale statistics in day
	public static int[] SALE_STATISTICS_IN_DAY_TABLE_WIDTHS = { 45, 100, 210,
			45, 123, 140, 135, 140 };
	public static String[] SALE_STATISTICS_IN_DAY_TABLE_TITLES = {
			StringUtil.getString(R.string.TEXT_STT),
			StringUtil.getString(R.string.TEXT_HEADER_PRODUCT_CODE),
			StringUtil.getString(R.string.TEXT_COLUM_ORDER_NAME),
			StringUtil.getString(R.string.TEXT_HEADER_PRODUCT_INDUSTRY),
			StringUtil.getString(R.string.TEXT_PRODUCT_SPECIFICATION),
			StringUtil.getString(R.string.TEXT_COLUM_PRICE),
			StringUtil.getString(R.string.TEXT_NUMBER_PRODUCT),
			StringUtil.getString(R.string.TEXT_TOTAL_MONEY)};

	
	//set width and colum title for table in list problem of gsnpp
	public static int[] TRACK_FIX_PROBLEM_GSNPP_TABLE_WIDTHS = { 45, 200, 300,
			195, 150, 50 };
	public static String[] TRACK_FIX_PROBLEM_GSNPP_TABLE_TITLES = {
			StringUtil.getString(R.string.TEXT_STT), 
			StringUtil.getString(R.string.TEXT_TYPE_PROBLEM), 
			StringUtil.getString(R.string.CONTENT), 
			StringUtil.getString(R.string.TEXT_CUSTOMER), 
			StringUtil.getString(R.string.TEXT_LABLE_DATE_NOTIFY), 
			StringUtil.getString(R.string.TEXT_FOLLOW_PROBLEM_OK)};
	
	/*------------------Quang------------------*/
	// 04.01-Danh sach hinh anh (GSBH)
	// Dinh nghia titles, widths table
	public static int[] IMAGE_LIST_GSNPP_TABLE_WIDTHS = { 70, 70, 180, 227,
			170, 150, 72 };
	public static String[] IMAGE_LIST_GSNPP_TABLE_TITLES = {
			StringUtil.getString(R.string.TEXT_STT),
			StringUtil.getString(R.string.TEXT_TB_CUSTOMER_CODE),
			StringUtil.getString(R.string.TEXT_TB_CUSTOMER_NAME),
			StringUtil.getString(R.string.TEXT_ADDRESS),
			StringUtil.getString(R.string.TEXT_NVBH),
			StringUtil.getString(R.string.LINE),
			StringUtil.getString(R.string.TEXT_NUMBER_PHOTO) };
	// MH can thuc hien NVBH
	// Dinh nghia titles, widths table 
	public static int[] CUSTOMER_LIST_NVBH_TABLE_WIDTHS = { 45, 313, 150, 150, 110, 110, 60 };
	public static String[] CUSTOMER_LIST_NVBH_TABLE_TITLES = {
			StringUtil.getString(R.string.TEXT_STT),
			StringUtil.getString(R.string.CONTENT),
			StringUtil.getString(R.string.TEXT_TB_CUSTOMER_CODE),
			StringUtil.getString(R.string.TEXT_TYPE_PROBLEM),
			StringUtil.getString(R.string.TEXT_REMIND_DATE),
			StringUtil.getString(R.string.TEXT_DONE_DATE_2), Constants.STR_SPACE };
	// MH danh sach san pham NVBH
	// Dinh nghia titles, widths table 
	public static String[] LIST_PRODUCT_TABLE_WIDTHS = {
			StringUtil.getString(R.string.TEXT_STT),
			StringUtil.getString(R.string.TEXT_MA_SP),
			StringUtil.getString(R.string.TEXT_NAME_PRODUCT),
			StringUtil.getString(R.string.TEXT_LABLE_PRODUCT_UOM),
			StringUtil.getString(R.string.TEXT_COLUM_INVENTORY) };
	public static int[] LIST_PRODUCT_TABLE_TITLES = { 70, 180, 295, 200, 200 };
	// MH theo doi khac phuc van de TTTT
	// Dinh nghia titles, widths table 
	public static int[] CUSTOMER_LIST_TTTT_TABLE_WIDTHS = { 45, 313, 150, 150, 110, 110, 60 };
	public static String[] CUSTOMER_LIST_TTTT_TABLE_TITLES = {
			StringUtil.getString(R.string.TEXT_STT),
			StringUtil.getString(R.string.CONTENT),
			StringUtil.getString(R.string.TEXT_TB_CUSTOMER_CODE),
			StringUtil.getString(R.string.TEXT_TYPE_PROBLEM),
			StringUtil.getString(R.string.TEXT_REMIND_DATE),
			StringUtil.getString(R.string.TEXT_DONE_DATE_2), Constants.STR_SPACE };

	
	//
	//Header table man hinh 4.01 - Hinh anh
	public static String[] TABLECUSIMAGETITLES = { 
      "STT",
      "Mã KH",
      "Tên khách hàng",
      "Địa chỉ",
      "Tuyến",
      "Số hình ảnh"};
	
    // Chieu rong cac cot table man hinh 4.01 - Hinh anh
	public static int[] TABLECUSIMAGEWIDTHS = {70,
        70,
        250,
        307,
        150,
        95}; 
	// header row danh sach san pham role GSNPP
	public static String[] SUPERVISOR_LIST_PRODUCT_TABLE_WIDTHS = {
			StringUtil.getString(R.string.TEXT_STT),
			StringUtil.getString(R.string.TEXT_MA_SP),
			StringUtil.getString(R.string.TEXT_NAME_PRODUCT),
			StringUtil.getString(R.string.TEXT_LABLE_PRODUCT_UOM),
			StringUtil.getString(R.string.TEXT_COLUM_INVENTORY) };
	public static int[] SUPERVISOR_LIST_PRODUCT_TABLE_TITLES = { 70, 180, 295, 200, 200 };
	
	// customer list table
	public static int[] FEEDBACK_CUSTOMER_LIST_TABLE_WIDTHS = { 45, 413, 120, 120, 120, 60, 60 };
	public static String[] FEEDBACK_CUSTOMER_LIST_TABLE_TITLES = { StringUtil.getString(R.string.TEXT_STT),
			StringUtil.getString(R.string.CONTENT), StringUtil.getString(R.string.TEXT_FEEDBACK_TYPE),
			StringUtil.getString(R.string.TEXT_REMIND_DATE), StringUtil.getString(R.string.TEXT_DONE_DATE), " ", " " };
	public static int[] GSNPP_CUSTOMER_LIST_TABLE_WIDTHS = { 45, 413, 160, 160, 160 };
	public static String[] GSNPP_CUSTOMER_LIST_TABLE_TITLES = { StringUtil.getString(R.string.TEXT_STT),
			StringUtil.getString(R.string.CONTENT), StringUtil.getString(R.string.TEXT_FEEDBACK_TYPE),
			StringUtil.getString(R.string.TEXT_REMIND_DATE), StringUtil.getString(R.string.TEXT_DONE_DATE)};
}
