/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.DisplayProgrameDTO;

/**
 *  DTO chua du lieu cua view ListAlbumUser
 *  @author: QuangVT 
 */ 
public class ListAlbumUserDTO implements Serializable {  
	private static final long serialVersionUID = 1L;
	 
	private ArrayList<AlbumDTO> listAlbum; // danh sach album
	private CustomerDTO 		customer;  // thong tin khach hang
	private ArrayList<AlbumDTO> listProgrameAlbum;	// danh sach album chuog trinh
	private ArrayList<DisplayProgrameItemDTO> listDisplayPrograme;	// danh sach Item CTTB
	private ArrayList<DisplayProgrameDTO> 	  listPhotoDPrograme;	// danh sach  CTTB 
	public boolean isFirstInit; 	// khoi tao lan dau
	public double  shopDistance; 	// khoang cach so voi shop

	/**
	 * Khoi tao doi tuong
	 * 
	 * @author quangvt1
	 */
	public ListAlbumUserDTO() {
		setListAlbum(new ArrayList<AlbumDTO>());
		setListProgrameAlbum(new ArrayList<AlbumDTO>());
	}

	/**
	 * Lay danh sach album
	 * 
	 * @author quangvt1
	 * @return the listAlbum
	 */
	public ArrayList<AlbumDTO> getListAlbum() {
		return listAlbum;
	}

	/**
	 * gan danh sach album
	 * 
	 * @author quangvt1
	 * @param listAlbum
	 *            the listAlbum to set
	 */
	public void setListAlbum(ArrayList<AlbumDTO> listAlbum) {
		this.listAlbum = listAlbum;
	}

	/**
	 * Lay thong tin khach hang
	 * 
	 * @author quangvt1
	 * @return the customer
	 */
	public CustomerDTO getCustomer() {
		return customer;
	}

	/**
	 * gan thong tin khach hang
	 * 
	 * @author quangvt1
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}

	/**
	 * Lay danh sach Album chuong trinh
	 * 
	 * @author quangvt1
	 * @return
	 */
	public ArrayList<AlbumDTO> getListProgrameAlbum() {
		return listProgrameAlbum;
	}

	/**
	 * get danh sach album chuong trinh
	 * 
	 * @author quangvt1
	 * @param listProgrameAlbum
	 */
	public void setListProgrameAlbum(ArrayList<AlbumDTO> listProgrameAlbum) {
		this.listProgrameAlbum = listProgrameAlbum;
	}

	/**
	 * Lay danh sach Item CCTB
	 * 
	 * @author quangvt1
	 * @return
	 */
	public ArrayList<DisplayProgrameItemDTO> getListDisplayPrograme() {
		return listDisplayPrograme;
	}

	/**
	 * Gan danh sach chuong tring
	 * 
	 * @author quangvt1
	 * @param listDisplayPrograme
	 */
	public void setListDisplayPrograme(ArrayList<DisplayProgrameItemDTO> listDisplayPrograme) {
		this.listDisplayPrograme = listDisplayPrograme;
	}

	/**
	 * Lay danh sach hinh anh chuong trinh
	 * 
	 * @author quangvt1
	 * @return
	 */
	public ArrayList<DisplayProgrameDTO> getListPhotoDPrograme() {
		return listPhotoDPrograme;
	}

	/**
	 * Lay danh sach hinh anh chuong trinh
	 * 
	 * @author quangvt1
	 * @param listPhotoDPrograme
	 */
	public void setListPhotoDPrograme(ArrayList<DisplayProgrameDTO> listPhotoDPrograme) {
		this.listPhotoDPrograme = listPhotoDPrograme;
	}
}
