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
 */
public class ProductCompetitorListDTO extends AbstractTableDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5009841528672964729L;
	
	private ArrayList<ProductCompetitorDTO> arrProductCompetitor;
	public ArrayList<ProductCompetitorDTO> getArrProductCompetitor() {
		return arrProductCompetitor;
	}

	public void setArrProductCompetitor(ArrayList<ProductCompetitorDTO> arrProductCompetitor) {
		this.arrProductCompetitor = arrProductCompetitor;
	}
	
	public ProductCompetitorListDTO(){
		arrProductCompetitor= new ArrayList<ProductCompetitorDTO>();
	}

	/**
	* Khoi tao tu con tro
	* @author: dungdq3
	* @param: Cursor cursor
	*/
	public void initFromCursor(Cursor cursor) {
		// TODO Auto-generated method stub
		if (cursor.moveToFirst()) {
			do{
				ProductCompetitorDTO productCompetitorDTO= new ProductCompetitorDTO();
				productCompetitorDTO.initFromCursor(cursor);
				boolean flag=false; // khong trung
				int i=0;
				if(arrProductCompetitor.size()>0){
					for(ProductCompetitorDTO dto: arrProductCompetitor){
						if(dto.getIdCompetitor()==productCompetitorDTO.getIdCompetitor()){
							//arrProductCompetitor.add(productCompetitorDTO);
							flag=true; //co trung
							break;
						}
						i++;
					}
				}
				if(flag){
					arrProductCompetitor.get(i).getArrProduct().add(productCompetitorDTO.getOpProductDTO());
				}else{
					arrProductCompetitor.add(productCompetitorDTO);
				}
			}while(cursor.moveToNext());
		}
	}
	
	/**
	 * Lay du lieu tu Cursor cho thong tin san pham cua BSG
	 * @author: dungdq3
	 * @param: Cursor cursor
	 */
	public void initFromCursorForInfoBSG(Cursor cursor) {
		if (cursor.moveToFirst()) {
			int index = 0;
			do{
				ProductCompetitorDTO productCompetitorDTO= new ProductCompetitorDTO();
				productCompetitorDTO.initFromCursor(cursor); 
				if(index > 0){
					arrProductCompetitor.get(0).getArrProduct().add(productCompetitorDTO.getOpProductDTO());
				}else{
					arrProductCompetitor.add(productCompetitorDTO);
				}
				index++;
			}while(cursor.moveToNext());
		}
	}
}
