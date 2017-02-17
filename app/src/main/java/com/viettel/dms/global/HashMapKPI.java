/**
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.global;

/**
 * Danh sach chuc nang log KPI
 *
 * @author: hoanpd1
 * @version: 1.0 
 * @since:  1.0
 */
public enum HashMapKPI {
	GLOBAL_LOGIN("Đăng nhập"),
	GLOBAL_GET_FILE_DB("Tải file db"),
	GLOBAL_SYN_DATA("Đồng bộ dữ liệu"),	
	GLOBAL_LOGIN_SYN_DATA("Đồng bộ dữ liệu khi đăng nhập"),
	NVBH_DANHSACHKHACHHANG("NVBH - Danh sách khách hàng"),
	NVBH_CHITIETKHACHHANG("NVBH - Chi tiết khách hàng"),
	NVBH_GOPY("NVBH - Góp ý"),
	NVBH_CHAMTRUNGBAY("NVBH - Chấm trưng bày"),
	NVBH_KIEMHANGTON("NVBH - Kiểm hàng tồn"),
	NVBH_TINHKHUYENMAI("NVBH - Tính khuyến mãi"),
	NVBH_LUUDONHANG("NVBH - Lưu đơn  hàng"),
	NVBH_SUADONHANG("NVBH - Sữa đơn hàng"),
	NVBH_DANHSACHDONHANG("NVBH - Danh sách đơn hàng"),
	NVBH_DANHSACHHINHANH("NVBH - Danh sách hình ảnh"),
	NVBH_DANHSACHALBUM("NVBH - Danh sách album"),
	NVBH_DANHSACHHINHANHTHEOALBUM("NVBH - Danh sách hình ảnh theo album"),
	NVBH_CHUPHINHTAIDIEMBAN("NVBH - Chụp hình tại điểm bán"),
	NVBH_DANHSACHHINHANHTIMKIEM("NVBH - Danh sách hình ảnh tìm kiếm"),
	NVBH_DANHSACHSANPHAM("NVBH - Danh sách sản phẩm"),
	NVBH_GIOITHIEUSANPHAM("NVBH - Giới thiệu sản phẩm"),
	NVBH_TAIANH("NVBH - Tải hình ảnh sản phẩm"),
	NVBH_TAIVIDEO("NVBH - Tải video sản phẩm"),
	NVBH_DANHSACHSANPHAMCANCAPNHATBIADOITHU("NVBH - Danh sách sản phẩm cần cập nhật của bia đối thủ"),
	NVBH_DANHSACHKHACHHANGCANCAPNHATBIADOITHU("NVBH - Danh sách khác hàng cần cập nhật bia đối thủ"),
	NVBH_DANHSACHSANPHAMCANCAPNHATBIASAIGON("NVBH - Danh sách sản phẩm cần cập nhật của bia Sài Gòn"),
	NVBH_DANHSACHKHACHHANGCANCAPNHATBIASAIGON("NVBH - Danh sách khác hàng cần cập nhật bia Sài Gòn"),
	NVBH_DANHSACHCHUONGTRINHHOTROBANHANG("NVBH - Danh sách chương trình hỗ trợ bán hàng"),
	NVBH_THONGTINCHUNGCHUONGTRINHHOTROBANHANG("NVBH - Thông tin chung chương trình hỗ trợ bán hàng"),
	NVBH_DANHSACHDIEMBANTHAMGIA("NVBH - Danh sách điểm bán tham gia"),
	NVBH_THEMDIEMBANTHAMGIA("NVBH - Thêm điểm bán tham gia"),
	GSNPP_BAOCAOTIENDONGAY("GSNPP - Báo cáo tiến độ ngày"),
	GSNPP_BAOCAOKHCHUAPSDS("GSNPP - Báo cáo khách hàng chưa phát sinh doanh số"),
	GSNPP_THONGTINNHANVIEN("GSNPP - Thông tin nhân viên"),
	GSNPP_GIAMSATLOTRINHBANHANG("GSNPP - Giám sát lộ trình"),
	GSNPP_DANHSACHKHACHHANGGHETHAMSAITUYEN("GSNPP - Danh sách khách hàng ghé thăm sai tuyến"),
	GSNPP_DANHSACHKHACHHANGGHETHAMSAITHOIGIAN("GSNPP - Danh sách khách hàng ghé thăm thời gian"),
	GSNPP_XEMVITRINHANVIENTRENBANDO("GSNPP - Xem vị trí nhân viên"),
	GSNPP_CHAMCONGNHANVIEN("GSNPP - Chấm công nhân viên"),
	GSNPP_THEODOIKETQUADITUYENNHANVIEN("GSNPP - Theo dõi kết quả đi tuyến của nhân viên"),
	GSNPP_THUTHAPDULIEUTHITRUONG("GSNPP - Thu thập dữ liệu thị trường"),
	GSNPP_DANHSACHSANPHAMC2MUANGOAI("GSNPP - Danh sách sản phẩm C2 mua"),
	GSNPP_DANHSACHKHACHHANGMUAHANGCUAC2("GSNPP - Danh sách khách mua hàng của C2"),
	GSNPP_DANHSACHSANPHAM("GSNPP - Danh sách Sản phẩm"),
	GSNPP_GIOITHIEUSANPHAM("GSNPP - Giới thiệu sản phẩm"),
	GSNPP_THEODOIKHACPHUC("GSNPP - Theo dõi khắc phục"),
	GSNPP_POPUPDANHSACHKHACHHANG_THEODOIKHACPHUC("GSNPP - Danh sách khách hàng theo dõi khắc phục"),
	GSNPP_POPUPDANHSACHKHACHHANG_THEMMOIVANDE("GSNPP - Danh sách khách hàng thêm mới khắc phục"),
	GSNPP_CANTHUCHIEN("GSNPP - Cần thực hiện"),
	GSNPP_DIEMBAN("GSNPP - Danh sách bán"),
	GSNPP_CHITIETKHACHHANG("GSNPP - Chi tiết khách hàng"),
	GSNPP_GOPY("GSNPP - Gop ý"),
	GSNPP_DANHSACHHINHANH("GSNPP - Danh sách hình ảnh"),
	GSNPP_DANHSACHALBUM("GSNPP - Danh sách album"),
	GSNPP_CHUPHINHTAIDIEMBAN("GSNPP - chụp hình tại điểm bán"),
	GSNPP_DANHSACHHINHANHTHEOALBUM("GSNPP - Danh sách hình ảnh theo album"),
	GSNPP_DANHSACHHINHANHTIMKIEM("GSNPP - Danh sách hình ảnh khi tìm kiếm"),
	TTTT_BAOCAOCHITIETTIENDONVTT("TTTT - Báo cáo chi tiết tiến độ NVTT"),
	TTTT_DANHSACHKHACHHANG("TTTT - Danh sách khách hàng"),
	TTTT_CHITIETKHACHHANG("TTTT - Chi tiết khách hàng"),
	TTTT_GOPY("TTTT - Góp ý"),
	TTTT_DANHSACHALBUMKHACHHANG("TTTT - Danh sách album khách hàng"),
	TTTT_DANHSACHHINHANHCUAALBUMKHACHHANG("TTTT - Danh sách hình ảnh của album khách hàng"),
	TTTT_LOTRINH("TTTT - Lộ trình"),
	TTTT_THEODOIKHACPHUC("TTTT - Theo dõi khắc phục"),
	GSBH_BAOCAOTONGHOPTIENDONGAY("GSBH - Báo cáo tổng hợp tiến độ ngày"),
	GSBH_BAOCAOCHITIETTIENDONGAYTHEOGSNPP("GSBH - Báo cáo tiến độ ngày theo GSNPP"),
	GSBH_BAOCAOCHITIETTIENDONGAYTHEONVBH("GSBH - Báo cáo tiến độ ngày theo NVBH"),
	GSBH_BAOCAOCHITIETTIENDONGAYTHEONVTT("GSBH - Báo cáo tiến độ ngày theo NVTT"),
	GSBH_BAOCAOTONGHOPTIENDOLUYKE("GSBH - Báo cáo tổng hợp tiến độ lũy kế"),
	GSBH_BAOCAOCHITIETTIENDOLUYKETHEOGSNPP("GSBH - Báo cáo tiến độ lũy kế theo GSNPP"),
	GSBH_BAOCAOCHITIETTIENDOLUYKETHEONVBH("GSBH - Báo cáo tiến độ lũy kế theo NVBH"),
	GSBH_BAOCAOCHITIETTIENDOLUYKETHEONVTT("GSBH - Báo cáo tiến độ lũy kế theo NVTT"),
	GSBH_BAOCAOTONGHOPKHCHUAPSDS("GSBH - Báo cáo tổng hợp khách hàng chưa PSDS"),
	GSBH_BAOCAOCHITIETKHCHUAPSDS("GSBH - Báo cáo chi tiết khách hàng chưa PSDS"),
	GSBH_XEMVITRIGSNPPTRENBANDO("GSBH - Xem vị trí GSNPP"),
	GSBH_XEMVITRITTTTTRENBANDO("GSBH - Xem vị trí TTTT"),
	GSBH_XEMVITRINVBHTRENBANDO("GSBH - Xem vị trí NVBH"),
	GSBH_CHAMCONG("GSBH - Chấm công"),
	GSBH_CHITIETCHAMCONG("GSBH - Chi tiết chấm cống"),
	GSBH_CANHBAOGHETHAMTUYEN("GSBH - Cảnh báo ghé thăm tuyến"),
	GSBH_CHITIETCANHBAOGHETHAMTUYEN("GSBH - Chi tiết cảnh báo ghé thăm tuyến"),
	GSBH_DANHSACHSANPHAM("GSBH - Danh sách sản phẩm"),
	GSBH_THEODOIKHACPHUCVANDE("GSBH - Theo dõi khắc phục vấn đề"),
	GLOBAL_FULL_LOGIN("Đăng nhập full");
	
	String note;
	private HashMapKPI(String pNote){
		note = pNote;
	}
	
	public String getNote() {
		return note;
	}
}
