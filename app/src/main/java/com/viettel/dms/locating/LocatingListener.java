package com.viettel.dms.locating;

import android.location.LocationListener;

/**
 *  lang nghe cac su kien dinh vi co time out (interface)
 *  @author: AnhND
 *  @version: 1.0
 *  @since: 1.0
 */
public interface LocatingListener extends LocationListener{
	public void onTimeout (Locating locating);
}