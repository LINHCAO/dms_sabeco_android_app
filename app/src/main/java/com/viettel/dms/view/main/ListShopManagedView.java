/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TableRow;

import com.viettel.dms.dto.db.ShopDTO;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.sabeco.R;

/**
 *  Popup danh sach nha phan phoi quan ly
 *  @author: BangHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class ListShopManagedView extends ScrollView{
	private GlobalBaseActivity parent;
	public View viewLayout;
	private VinamilkTableView tbShop;
	private int actionSelectedShop = -1;
	List<TableRow> listRows = new ArrayList<TableRow>();
	
	public ListShopManagedView(Context context, ArrayList<ShopDTO> listShop, int actionSelectedShop) {
		super(context);
		this.actionSelectedShop = actionSelectedShop;
		parent = (GlobalBaseActivity) context;
		LayoutInflater inflater = this.parent.getLayoutInflater();
		viewLayout = inflater.inflate(R.layout.layout_shop_managed_view, null);
		tbShop = (VinamilkTableView) viewLayout.findViewById(R.id.tbShop);
		renderLayout(listShop);
	}

	/**
	* Render du lieu bang npp giam sat quan ly
	* @author: BangHN
	* @param listShop: danh sach nha phan phoi (id, name, code, street)
	* @return: void
	* @throws: Ngoại lệ do hàm đưa ra (nếu có)
	*/
	private void renderLayout(final ArrayList<ShopDTO> listShop) {
		int pos = 1;
		if (listShop != null && listShop.size() > 0) {
			for (int i = 0, s = listShop.size(); i < s; i++) {
				ShopManagedRow row = new ShopManagedRow(parent, actionSelectedShop);
				row.renderLayout(pos, listShop.get(i));
				pos++;
				listRows.add(row);
			}
			// customer list table
			int[] CUSTOMER_LIST_TABLE_WIDTHS = { 320, 410 };
			String[] CUSTOMER_LIST_TABLE_TITLES = { StringUtil.getString(R.string.TEXT_HEADER_TABLE_NPP),
					StringUtil.getString(R.string.TEXT_ADDRESS) };

			tbShop.getHeaderView().addColumns(CUSTOMER_LIST_TABLE_WIDTHS, CUSTOMER_LIST_TABLE_TITLES,
					ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			tbShop.addContent(listRows);
		}

	}


}
