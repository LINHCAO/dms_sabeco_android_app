/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commonsware.cwac.cache.SimpleWebImageCache;
import com.commonsware.cwac.thumbnail.ThumbnailAdapter;
import com.commonsware.cwac.thumbnail.ThumbnailBus;
import com.commonsware.cwac.thumbnail.ThumbnailMessage;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.download.ExternalStorage;
import com.viettel.dms.dto.db.MediaItemDTO;
import com.viettel.dms.dto.view.IntroduceProductDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.HorizontalListView;
import com.viettel.dms.view.control.MediaGalleryAdapter;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * 
 * Mo ta cho class
 * 
 * @author: ThanhNN8
 * @version: 1.0
 * @since: 1.0
 */
public class SupervisorIntroduceProductView extends BaseFragment implements
OnEventControlListener, OnClickListener, VinamilkTableListener{
	public static final String TAG = SupervisorIntroduceProductView.class.getName();
	//frame image view full
	private FrameLayout flImageViewFull;
	//man hinh video
	private ImageView imgViewFull;
	private TextView tvProductName;
	private int selectIndex;
	private LinearLayout llGallery;
	private WebView wvIntroduce;
	// category for list location search
	// category adapter
	MediaGalleryAdapter mediaAdapter;
	HorizontalListView gallery;
	private ThumbnailAdapter categoryThumbs = null;
	// main activity
	private GlobalBaseActivity parent;
	// define id of avatar on row
	private static final int[] IMAGE_IDS = { R.id.imgViewMedia, R.id.imgViewBoder };
	//productId
	private String productId;
	// image hien tai
	MediaItemDTO curentImage;
	// category list
	public Vector<MediaItemDTO> mediaList = new Vector<MediaItemDTO>();
	
	public static SupervisorIntroduceProductView newInstance(Bundle data) {
		SupervisorIntroduceProductView instance = new SupervisorIntroduceProductView();
		instance.setArguments(data);
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(
				R.layout.layout_introduce_product, null);
		View view = super.onCreateView(inflater, v, savedInstanceState);
		//get Date
		Bundle data = (Bundle) getArguments();
		productId = data.getString(IntentConstants.INTENT_PRODUCT_ID);
		String productCode = data.getString(IntentConstants.INTENT_PRODUCT_CODE);
		String productName = data.getString(IntentConstants.INTENT_PRODUCT_NAME);
		flImageViewFull = (FrameLayout) view.findViewById(R.id.flImageViewFull);
		imgViewFull = (ImageView) view.findViewById(R.id.imgViewFull);
		imgViewFull.setOnClickListener(this);
		tvProductName = (TextView) view.findViewById(R.id.tvProductName);
		tvProductName.setText(productCode + " - " +productName);
		llGallery = (LinearLayout) view.findViewById(R.id.llGallery);
		llGallery.setVisibility(View.VISIBLE);
		gallery = (HorizontalListView) view.findViewById(R.id.glCategoryView);
		wvIntroduce = (WebView) view.findViewById(R.id.wvIntroduce);
		wvIntroduce.setBackgroundColor(Color.TRANSPARENT);

		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_GSNPP_INTRODUCE_PRODUCT));
		//Request information for view
		RequestIntroduceProduct (productId);//change productId
		return view;
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			parent = (GlobalBaseActivity) activity;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

	}
	/**
	*  Goi du lieu len server lay thong tin man hinh gioi thieu san pham
	*  @author: ThanhNN8
	*  @param i
	*  @return: void
	*  @throws: 
	*/
	private void RequestIntroduceProduct(String productId) {
		// TODO Auto-generated method stub
		parent.showProgressDialog(getString(R.string.loading));
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_PRODUCT_ID, productId);
		ActionEvent e = new ActionEvent();
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_INTRODUCE_PRODUCT;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * bo xung comment cho ThanhNN: play video from path
	 * @author: BangHN
	 * @param path
	 * @return: void
	 * @throws:
	 */
	private void openMeida(String path, int mediaType) {
		// call service
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		if(mediaType == MediaItemDTO.MEDIA_IMAGE){
			intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
		}else{
			intent.setDataAndType(Uri.parse(path), "video/mp4");
		}
		startActivity(intent);
	}

	@SuppressWarnings("resource")
	private String getDataSource(String path) throws Exception {
		MediaItemDTO mdiDTO = (MediaItemDTO) mediaList.get(selectIndex);
		URL url = new URL(path);
		URLConnection cn = url.openConnection();
		cn.connect();
		InputStream stream = cn.getInputStream();
		if (stream == null) {
			throw new RuntimeException("stream is null");
		}
		File dir = new File(ExternalStorage.DMS_SABECO_FOLDER + "PRODUCTS/");
		dir.mkdirs();
		File temp ;
		if(mdiDTO.mediaType == MediaItemDTO.MEDIA_IMAGE){
			//temp = File.createTempFile("mediaplayertmp", ".jpg", dir);
			temp = new File(dir, StringUtil.md5(mdiDTO.url) + ".jpg");
		}else{
			//temp = File.createTempFile("mediaplayertmp", ".mp4", dir);
			temp = new File(dir, StringUtil.md5(mdiDTO.url) + ".mp4");
		}
		temp.deleteOnExit();
		String tempPath = temp.getAbsolutePath();
		FileOutputStream out = new FileOutputStream(temp);
		byte buf[] = new byte[128];
		//trungnt fix download error
		do {
			try {
				int numread = stream.read(buf);
				if (numread <= 0)
					break;
				out.write(buf, 0, numread);
			} catch (Exception exception) {
				stream.close();
				File file = new File(tempPath);
				if (file.exists()) {
					file.delete();
				}
				throw exception;	
			}
		} while (true);
		try {
			stream.close();
		} catch (IOException ex) {
			VTLog.e(TAG, "error: " + ex.getMessage(), ex);
		}
		return tempPath;
	}
	/**
	 * Background task to download and unpack .zip file in background.
	 */
	private class DownloadTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			parent.showProgressDialog(getString(R.string.downloading_product_info));
		}

		@Override
		protected String doInBackground(String... params) {
			String url = (String) params[0];
			String path=StringUtil.EMPTY_STRING;
			try {
				path = getDataSource(url);
				publishProgress(path);
			} catch (Exception e) {
				return null;
			}

			return path;
		}
		
		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			if (values != null) {
				MediaItemDTO mdiDTO = (MediaItemDTO) mediaList.get(selectIndex);
				mdiDTO.sdCardPath = values[0];
				updateClientThumnailUrl(mdiDTO);
				parent.closeProgressDialog();
				if(SupervisorIntroduceProductView.this != null && SupervisorIntroduceProductView.this.isVisible()){
					openMeida(mdiDTO.sdCardPath, mdiDTO.mediaType);
				}
			} else {
				
			}
			
		}
		
		/**
		*  Update url client for database
		*  @author: ThanhNN8
		*  @param mdiDTO
		*  @return: void
		*  @throws: 
		*/
		private void updateClientThumnailUrl(MediaItemDTO mdiDTO) {
			// TODO Auto-generated method stub
			Bundle data = new Bundle();
			data.putSerializable(IntentConstants.INTENT_DATA, mdiDTO);
			ActionEvent e = new ActionEvent();
			e.viewData = data;
			e.sender = this;
			e.action = ActionEventConstant.UPDATE_CLIENT_THUMNAIL_URL;
			SaleController.getInstance().handleViewEvent(e);
		}

		//trungnt fix download error
		@Override
		protected void onPostExecute(String result) {
			if(result==null){
				parent.closeProgressDialog();
				parent.showDialog(StringUtil.getString(R.string.TEXT_WARNING_DOWNLOAD_FILE));
			}
		}
	}


	/* (non-Javadoc)
	 * @see com.viettel.vinamilk.view.listener.VinamilkTableListener#handleVinamilkTableloadMore(android.view.View, java.lang.Object)
	 */
	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == imgViewFull){
			String mediaName;
			if(curentImage.mediaType == MediaItemDTO.MEDIA_IMAGE){
				mediaName = StringUtil.md5(curentImage.url)+ ".jpg";
			}else{
				mediaName = StringUtil.md5(curentImage.url)+ ".mp4";
			}
			//neu ton tai cache truoc do roi
			if(GlobalUtil.isFileExistsInDirectory(new File(ExternalStorage.DMS_SABECO_FOLDER + "PRODUCTS/"), mediaName)){
				//xem offline, file ton tai duoi may tinh bang
				openMeida(ExternalStorage.DMS_SABECO_FOLDER + "PRODUCTS/" 
						+ mediaName, curentImage.mediaType);
			}else if (!GlobalUtil.checkNetworkAccess()){
				//truong hop khong co mang
				parent.showDialog(StringUtil.getString(R.string.TEXT_NETWORK_DISABLE_PRODUCT_INFO));
			}else{
				//download moi ve
				new DownloadTask().execute(GlobalInfo.getInstance().getServerImageProductVNM() + curentImage.url);
			}
		}
	}


	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		super.handleModelViewEvent(modelEvent);
		IntroduceProductDTO inDTO = (IntroduceProductDTO) modelEvent.getModelData();
		initMediaGallery(inDTO.getListMedia());
		renderLayout(inDTO);
	}

	/**
	*  render giao dien cho man hinh
	*  @author: ThanhNN8
	*  @param modelData
	*  @return: void
	*  @throws: 
	*/
	private void renderLayout(IntroduceProductDTO modelData) {
		// TODO Auto-generated method stub
		String htmlContent = modelData.getHtmlContextIntroduce();
		wvIntroduce.getSettings().setSupportZoom(false);
		if (htmlContent != null) {
			StringBuilder regul = new StringBuilder();
			regul.append("<html><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><body>");
			regul.append(htmlContent);
			regul.append("</body></html>");
			wvIntroduce.loadDataWithBaseURL(null, regul.toString(), "text/html", "UTF-8", null);
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		super.handleErrorModelViewEvent(modelEvent);
	}
	
	/**
	 * 
	*  bo xung comment cho THanhNN : khoi tao media cho gallery
	*  @author: HaiTC3
	*  @param listDTO
	*  @return: void
	*  @throws:
	 */
	private void initMediaGallery(List<MediaItemDTO> listDTO) {
		int size = listDTO.size();
		if (size == 0) {
			imgViewFull.setVisibility(View.GONE);
			flImageViewFull.setVisibility(View.GONE);
			return;
		} else {
			imgViewFull.setVisibility(View.VISIBLE);
			flImageViewFull.setVisibility(View.VISIBLE);
		}
		mediaList.clear();
		for (int i = 0; i < size; i++) {
			MediaItemDTO media = listDTO.get(i);
			mediaList.add(media.clone());
		}
		mediaAdapter = new MediaGalleryAdapter(parent,
				R.layout.layout_media_item, mediaList, true);
		try {
			ThumbnailBus thumbBus = new ThumbnailBus();
			categoryThumbs = new ThumbnailAdapter(parent, mediaAdapter,
					new SimpleWebImageCache<ThumbnailBus, ThumbnailMessage>(
							null, null, 101, thumbBus), IMAGE_IDS);
		} catch (Exception e1) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e1));
		}
		gallery.setAdapter(categoryThumbs);
		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				//categoryList.elementAt(categoryId).linkAvatar=categoryList.elementAt(0).linkAvatar;
				selectIndex = position;
				MediaItemDTO mdiDTO = (MediaItemDTO) mediaList.get(position);
				//to do handler event
				showImage(mdiDTO);
				changeImageClick();
			}
		});
		//set gia tri 0 mac dinh ban dau
		if (mediaList.size() > 0) {
			selectIndex = 0;
			MediaItemDTO mdiDTO = (MediaItemDTO) mediaList.get(0);
			changeImageClick();
			// to do handler event
			showImage(mdiDTO);
		}
	}
	
	/**
	*  Thay doi lai danh sach hinh anh khi chon 1 item
	*  @author: ThanhNN8
	*  @return: void
	*  @throws: 
	*/
	protected void changeImageClick() {
		// TODO Auto-generated method stub
		for (MediaItemDTO mdiDTO : mediaList) {
			mdiDTO.isSelected = false;
		}
		// Send message update
		mediaList.elementAt(selectIndex).isSelected = true;
		categoryThumbs.notifyDataSetChanged();
	}

	/**
	 * 
	*  Hien thi full hinh anh hoac mo trinh xem video cua he thong
	*  @author: ThanhNN8
	*  @param mdiDTO
	*  @return: void
	*  @throws:
	 */
	private void showImage(MediaItemDTO mdiDTO) {
		// TODO Auto-generated method stub
		curentImage = mdiDTO;
		if (mdiDTO.mediaType == 1) { // neu la video
			imgViewFull.setImageResource(R.drawable.videofull);
		} else { // neu la hinh anh
			// showFullImage(ServerPath.IMAGE_PRODUCT_VNM + mdiDTO.url);
			showFullImage(GlobalInfo.getInstance().getServerImageProductVNM() + mdiDTO.url);
		}
	}
	

	/**
	*  Ham hien thi full hinh anh
	*  @author: ThanhNN8
	*  @param url
	*  @return: void
	*  @throws: 
	*/
	private void showFullImage(String url) {
		// TODO Auto-generated method stub
		ImageUtil.getImageFromURL(url, parent, imgViewFull);
	}
	
	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if(this.isVisible()){
				RequestIntroduceProduct (productId);
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
}
