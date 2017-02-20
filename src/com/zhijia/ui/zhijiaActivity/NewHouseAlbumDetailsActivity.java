package com.zhijia.ui.zhijiaActivity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.data.Medol.PictureJsonModel;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.util.DefaultDataUtils;
import com.zhijia.util.DownloadImageTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新房相册详细类
 */
public class NewHouseAlbumDetailsActivity extends BaseDetailsActivity {

	private final String PICTURE_URL = Global.API_WEB_URL + "/xinfang/picture";
	private final int header_title = R.id.details_title;
	private List<View> contentViews;
	private ViewPager contentViewPager;
	private String pid;
	private String hid;
	private String pic;
	private int position;
	private String catname;
	// 图片title的名字
	private String name;
	private String total;
	private int curentPostion;
	private List<PictureJsonModel> pictureJsonModelList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_house_album_details);
		pid = (String) getIntent().getSerializableExtra("pid");
		// 改:注掉了position增加了pic
		hid = (String) getIntent().getSerializableExtra("hid");
		pic = (String) getIntent().getSerializableExtra("pic");
		// position=(Integer) getIntent().getSerializableExtra("position");
		// Log.d(">>>>>>pid", pid);
		// Log.d(">>>>>>hid", hid);
		// Log.d(">>>>>>position", position+"");
		pictureJsonModelList = new ArrayList<PictureJsonModel>();
		contentViews = new ArrayList<View>();
		AlbumDetailsAsyncTask albumDetailsAsyncTask = new AlbumDetailsAsyncTask();
		albumDetailsAsyncTask.execute(PICTURE_URL);
	}

	private void iniContentViewPager() {
		this.contentViewPager = (ViewPager) findViewById(R.id.new_house_album_details_view_page);
	}

	private void initContentListener() {
		this.contentViewPager
				.setOnPageChangeListener(new ContentViewListener());
	}

	public JsonResult<Map> getAlbumDetails(String url) {
		System.out.println("url:+" + url);
		HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
		Map<String, String> map = new HashMap<String, String>();
		Log.d(">>>>>>>>cityid", Global.NOW_CITY_ID);
		map.put("cityid", Global.NOW_CITY_ID);

		map.put("hid", hid);
		map.put("pid", pid);
		// map.put("catName", picture);
		Optional<JsonResult<Map>> optional = httpClientUtils.getUnsignedByData(
				url, map, Map.class);
		System.out.println(optional.toString());
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public void setHeader(String position) {
		Map<Integer, Object> map = new HashMap<Integer, Object>();
		map.put(header_title, position + "/" + total);

		setHeader(map);
	}

	public void setGoneHeader() {
		Map<Integer, Object> map1 = new HashMap<Integer, Object>();

		setHeader(map1);
	}

	public void setAlbumDetails(int position) {
		LinearLayout linearLayout = (LinearLayout) contentViews.get(position);
		linearLayout.findViewById(
				R.id.new_house_album_details_item_list_wait_load_relative)
				.setVisibility(View.GONE);
		linearLayout.findViewById(
				R.id.new_house_album_details_item_linear_layout).setVisibility(
				View.VISIBLE);
		PictureJsonModel pictureJsonModel = pictureJsonModelList.get(position);
		final ImageView imageView = (ImageView) linearLayout
				.findViewById(R.id.new_house_album_details_item_image);
		TextView unitsTextView = (TextView) linearLayout
				.findViewById(R.id.new_house_album_details_item_units);
		TextView contentTextView = (TextView) linearLayout
				.findViewById(R.id.new_house_album_details_item_content);
		TextView textView = (TextView) linearLayout
				.findViewById(R.id.new_house_album_details_item_name_text_view);
		textView.setText(name);
		String pic = pictureJsonModel.getPic();
		String info = pictureJsonModel.getInfo();
		String catName = pictureJsonModel.getCatname();
//		Log.d(">>>>>pic ", pic);
//		Log.d(">>>>>info ", info);
//		Log.d(">>>>>catname ", catName);
		if (pic != null) {
			String picUrl = "http://" + pic;
			Log.d("setAlbumDetails->>", picUrl);
			// 得到可用的图片
			// Bitmap bitmap = getHttpBitmap(picUrl);
			// imageView.setImageBitmap(bitmap);
			new DownloadImageTask().doInBackground(picUrl, imageView,
					R.drawable.house);
		} else {
			imageView.setImageResource(R.drawable.house);
		}
		DefaultDataUtils.setValue(info, unitsTextView);
		DefaultDataUtils.setValue(catName, contentTextView);
		contentViewPager.setCurrentItem(position);
	}

	class AlbumDetailsAsyncTask extends
			AsyncTask<String, Void, JsonResult<Map>> {

		@Override
		protected JsonResult<Map> doInBackground(String... params) {
			return getAlbumDetails(params[0]);
		}

		@Override
		protected void onPostExecute(JsonResult<Map> mapJsonResult) {
			findViewById(R.id.new_house_album_details_list_wait_load_relative)
					.setVisibility(View.GONE);
			findViewById(R.id.details_back).setOnClickListener(
					new DetailsOnClickListener());
			if (mapJsonResult != null && mapJsonResult.isStatus()) {
				findViewById(R.id.new_house_album_details_linear_layout)
						.setVisibility(View.VISIBLE);
				iniContentViewPager();
				initContentListener();
				Map<String, Object> map = mapJsonResult.getData();
				for (Map.Entry<String, Object> temp : map.entrySet()) {
					String keyStr = temp.getKey();
					String value = temp.getValue().toString();

					if (keyStr.equalsIgnoreCase("name")) {
						setName(value);
						System.out.println("name:" + value);
					} else if (keyStr.equalsIgnoreCase("total")) {
						setTotal(value);
						System.out.println("title:" + value);
					} else {
						PictureJsonModel pictureJsonModel = DefaultDataUtils
								.convertObjectByString(value,
										PictureJsonModel.class);
						if (pictureJsonModel.getInfo() == null) {
							Log.d("AlbumDetailsAsyncTask",
									pictureJsonModel.getInfo() + "");
						}
						pictureJsonModelList.add(pictureJsonModel);
					}
					LinearLayout linearLayout = (LinearLayout) getLayoutInflater()
							.inflate(R.layout.new_house_album_details_item,
									null);
					contentViews.add(linearLayout);
				}
				// 改
				curentPostion= reSortList(pictureJsonModelList);
				contentViewPager.setAdapter(new ContentViewAdapter());
//				setHeader("1");
				setHeader(String.valueOf(curentPostion));
				setGoneHeader();
//				setAlbumDetails(position);
				setAlbumDetails(curentPostion);
			} else {

			}
		}
	}

	class ContentViewListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {
			Log.d("NewHouseAlbumDetailsActivity->onPageSelected", "position==="
					+ position);
			setAlbumDetails(position);
			setHeader(String.valueOf(position + 1));
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	}

	class ContentViewAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return contentViews.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = contentViews.get(position);
			Log.d("NewHouseAlbumDetailsActivity->instantiateItem",
					"position===" + position);
			((ViewPager) container).addView(view);
			return contentViews.get(position);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			Log.d("NewHouseAlbumDetailsActivity->destroyItem", "position==="
					+ position);
			((ViewPager) container).removeView((View) object);
		}
	}

	/**
	 * 重新排序改
	 * */
	private int reSortList(List<PictureJsonModel> pictureJsonModelList) {
		if (pictureJsonModelList.size() != 0) {		
			for (int i = 0; i < pictureJsonModelList.size(); i++) {
				if (pictureJsonModelList.get(i).getPid().equals(pid)) {
//					Log.d("list测试","if (pictureJsonModelList.get(i).getPid().equals(pic)) {");
//					pictureJsonModelList.add(0, pictureJsonModelList.get(i));
//					pictureJsonModelList.remove(i + 1);
					return i;
				}
			}
			Log.d("list测试",pictureJsonModelList.toString());
		}
		return 1;
	}
}