/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.control;

/**
 * Menu item class
 * MenuItemDTO.java
 * @author: BangHn
 * @version: 1.0 
 * @since:  10:11:07 8 Jan 2014
 */
public class MenuItemDTO{
	String menuText;
	int menuIcon;
	boolean isSelected = false;
	
	public MenuItemDTO(){
	}

	public MenuItemDTO(String text, int icon){
		menuText = text;
		menuIcon = icon;
	}
	
	public void setTextMenu(String text){
		menuText = text;
	}
	
	public void setIconMenu(int icon){
		menuIcon = icon;
	}
	
	public String getTextMenu(){
		return menuText;
	}
	
	public int getIconMenu(){
		return menuIcon;
	}
	
	public void setSelected(boolean selected){
		isSelected = selected;
	}
	
	public boolean isSelected(){
		return isSelected;
	}
}
