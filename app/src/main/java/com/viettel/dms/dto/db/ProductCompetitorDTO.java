/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import java.util.ArrayList;

import android.database.Cursor;

/**
 * @author dungdq3
 * @version: 1.0
 * @since: 1.0
 *
 */
public class ProductCompetitorDTO extends AbstractTableDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8140598043014860687L;
	
	// ten doi thu
	private String nameCompetitor;
	public String getNameCompetitor() {
		return nameCompetitor;
	}
	public void setNameCompetitor(String nameCompetitor) {
		this.nameCompetitor = nameCompetitor;
	}
	
	// ma doi thu
	private String codeCompetitor;
	public String getCodeCompetitor() {
		return codeCompetitor;
	}
	public void setCodeCompetitor(String codeCompetitor) {
		this.codeCompetitor = codeCompetitor;
	}
	
	// id doi thu
	private long idCompetitor;
	public long getIdCompetitor() {
		return idCompetitor;
	}
	public void setIdCompetitor(long idCompetitor) {
		this.idCompetitor = idCompetitor;
	}
	
	// danh sach cac san pham cua doi thu tuong ung
	private ArrayList<OpProductDTO> arrProduct;
	public ArrayList<OpProductDTO> getArrProduct() {
		return arrProduct;
	}
	public void setArrProduct(ArrayList<OpProductDTO> arrProduct) {
		this.arrProduct = arrProduct;
	}
	
	// san pham tam cuar doi thu.
	private OpProductDTO opProductDTO;
	public OpProductDTO getOpProductDTO() {
		return opProductDTO;
	}
	public void setOpProductDTO(OpProductDTO OpProductDTO) {
		this.opProductDTO = OpProductDTO;
	}
	
	public ProductCompetitorDTO(){
		arrProduct= new ArrayList<OpProductDTO>();
		opProductDTO= new OpProductDTO();
	}
	
	/**
	* khoi tao tu con tro
	* @author: dungdq3
	* @param: Cursor cursor
	*/
	public void initFromCursor(Cursor cursor) { 
		if(cursor.getColumnIndex("OPID")>=0){
			idCompetitor=cursor.getLong(cursor.getColumnIndex("OPID"));
		}
		if(cursor.getColumnIndex("OPPONENT_NAME")>=0){
			nameCompetitor=cursor.getString(cursor.getColumnIndex("OPPONENT_NAME"));
		}
		if(cursor.getColumnIndex("OPPONENT_CODE")>=0){
			codeCompetitor=cursor.getString(cursor.getColumnIndex("OPPONENT_CODE"));
		}
		opProductDTO.initFromCursor(cursor);
		arrProduct.add(opProductDTO);
	}
}
