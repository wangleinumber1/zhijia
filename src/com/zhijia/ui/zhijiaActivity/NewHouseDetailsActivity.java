package com.zhijia.ui.zhijiaActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.*;

import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.BusinessJsonModel;
import com.zhijia.service.data.Medol.DetailJsonModel;
import com.zhijia.service.data.Medol.DianpingJsonModel;
import com.zhijia.service.data.Medol.ImpressionJsonModel;
import com.zhijia.service.data.Medol.IntroductionJsonModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.data.Medol.MapJsonModel;
import com.zhijia.service.data.Medol.NewHouseDetailJsonModel;
import com.zhijia.service.data.Medol.NewsJsonModel;
import com.zhijia.service.data.Medol.SlideJsonModel;
import com.zhijia.service.data.Medol.TrendJsonModel;
import com.zhijia.service.data.Medol.TuanJsonModel;
import com.zhijia.service.data.Medol.UnitsJsonModel;
import com.zhijia.service.data.Medol.WendaJsonModel;

import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.ui.list.adapter.ItemAdapter;
import com.zhijia.ui.list.interfaces.IListDetailDataNetWork;
import com.zhijia.ui.view.ZjGridView;
import com.zhijia.util.DefaultDataUtils;
import com.zhijia.util.DownloadImageTask;
import com.zhijia.util.HouseDBUtil;
import com.zhijia.util.ScreenUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint({ "ResourceAsColor", "UseSparseArrays" })
public class NewHouseDetailsActivity extends BaseDetailsActivity implements
		IListDetailDataNetWork<NewHouseDetailJsonModel> {

	private final String DETAIL_URL = Global.API_WEB_URL + "/xinfang/get";

	private final String ATTENTION_URL = Global.API_WEB_URL
			+ "/xinfang/attention";

	private final String IMPRESSION_URL = Global.API_WEB_URL
			+ "/xinfang/impression";

	private final String QUESTION_URL = Global.API_WEB_URL
			+ "/xinfang/question";

	private final String COMMENT_URL = Global.API_WEB_URL + "/xinfang/comment";

	private final String IMPRESSION_SUPPORT_URL = Global.API_WEB_URL
			+ "/xinfang/impressionsupport";

	private final String VIEW_POINR_URL = Global.API_WEB_URL
			+ "/xinfang/viewpoint";

	private final int header_title = R.id.details_title;

	private String newHid = null;
	private ProgressDialog mProgressBar;
	private TextView youhui;

	private Map<String, String> paramMap = new HashMap<String, String>();

	private Map<String, String> impressionMap = new HashMap<String, String>();

	private Map<String, String> questionMap = new HashMap<String, String>();

	private Map<String, String> commentMap = new HashMap<String, String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_house_details);
		imageViews = new ArrayList<ImageView>();
		dotsList = new ArrayList<View>();
		viewPager = (ViewPager) findViewById(R.id.new_house_details_house_image);
		youhui = (TextView) findViewById(R.id.new_house_details_privilege_now_apply);
		newHid = (String) getIntent().getSerializableExtra("hid");
		System.out.println("hid:" + newHid);
		youhui.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						GetPrivilegeActivity.class);
				intent.putExtra("hid", newHid);
				startActivity(intent);
			}
		});
		//System.out.println("hid:" + newHid);
		paramMap.put("url", ATTENTION_URL);
		paramMap.put("hid", newHid);
		impressionMap.put("url", IMPRESSION_URL);
		impressionMap.put("hid", newHid);
		questionMap.put("houseid", newHid);
		questionMap.put("url", QUESTION_URL);
		commentMap.put("hid", newHid);
		commentMap.put("url", COMMENT_URL);
		findViewById(R.id.new_house_details_tuan_apply).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent immediatelyApplyIntent = new Intent(view
								.getContext(), CondoTourApplyActivity.class);
						immediatelyApplyIntent.putExtra("source", 1);
						immediatelyApplyIntent.putExtra("id", newHid);
						startActivity(immediatelyApplyIntent);
					}
				});
		HouseDBUtil.addBrowseHistory(newHid, HouseDBUtil.HouseType.NEW_HOUSE);
		startListTask(newHid);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			//Log.d("onActivityResult", newHid);
			AttentionAsyncTask attentionAsyncTask = new AttentionAsyncTask();
			attentionAsyncTask.execute(paramMap);
		} else {
			finish();
		}
	}

	/**
	 * 动态新闻
	 */
	private void addHouseDynamicItem(NewsJsonModel news) {
		View view = findViewById(R.id.new_house_details_house_dynamic_linear);
		if (news != null && news.getDynamic() != null) {
			TextView textView = (TextView) findViewById(R.id.new_house_details_house_dynamic_text);
			String total = news.getTotal();
			if (total != null && !total.isEmpty()) {
				textView.setText("楼盘动态(" + total + ")");
			}
			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(NewHouseDetailsActivity.this,
							NewHouseDynamicActivity.class);
					intent.putExtra("hid", newHid);
					startActivity(intent);
				}
			});
			LinearLayout dynamicLinear = (LinearLayout) findViewById(R.id.new_house_details_house_dynamic_list_linear);
			for (NewsJsonModel.DynamicJsonModel jsonModel : news.getDynamic()) {
				final String URL = jsonModel.getUrl();
				RelativeLayout itemRelative = (RelativeLayout) LayoutInflater
						.from(this).inflate(
								R.layout.new_house_details_dynamic_item, null);
				itemRelative.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent newsDetailIntent = new Intent(
								NewHouseDetailsActivity.this,
								NewHouseDynamicDetailActivity.class);
						newsDetailIntent.putExtra("url", URL);
						startActivity(newsDetailIntent);

					}
				});
				TextView news_time = (TextView) itemRelative
						.findViewById(R.id.new_house_details_dynamic_item_time);
				news_time.setText(jsonModel.getPublished());
				TextView news_desc = (TextView) itemRelative
						.findViewById(R.id.new_house_details_dynamic_item_privilege_desc);
				news_desc.setText(jsonModel.getTitle());
				dynamicLinear.addView(itemRelative);
			}
		} else {
			view.setVisibility(View.GONE);
		}
	}

	/**
	 * 主力户型
	 */
	private void addHouseUnitsItem(UnitsJsonModel unitsJsonModel) {
		View view = findViewById(R.id.new_house_details_house_main_linear);
		if (unitsJsonModel != null && unitsJsonModel.getList() != null) {
			TextView mainTextView = (TextView) findViewById(R.id.new_house_details_house_main_text);
			mainTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(NewHouseDetailsActivity.this,
							NewHouseAlbumActivity.class);
					intent.putExtra("hid", newHid);
					startActivity(intent);
				}
			});
			String total = unitsJsonModel.getTotal();
			if (total != null && !total.isEmpty()) {
				mainTextView.setText("主力户型(" + total + ")");
			}
			LinearLayout mainLinear = (LinearLayout) findViewById(R.id.new_house_details_house_main_image_list_linear);
			for (UnitsJsonModel.UnitsListJsonModel jsonModel : unitsJsonModel
					.getList()) {
				LinearLayout itemRelative = (LinearLayout) LayoutInflater.from(
						this).inflate(R.layout.new_house_details_units_item,
						null);
				itemRelative.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(
								NewHouseDetailsActivity.this,
								NewHouseAlbumActivity.class);
						intent.putExtra("hid", newHid);
						startActivity(intent);
					}
				});
				final ImageView imageView = (ImageView) itemRelative
						.findViewById(R.id.new_house_details_units_point_line);
				if (jsonModel.getPic() != null && !jsonModel.getPic().isEmpty()) {
					new DownloadImageTask().doInBackground(jsonModel.getPic(),
							imageView, R.drawable.a);
				} else {
					imageView.setImageResource(R.drawable.house);
				}

				TextView houseName = (TextView) itemRelative
						.findViewById(R.id.new_house_details_units_item_house_name);
				houseName.setText(jsonModel.getTitle());
				TextView houseRoom = (TextView) itemRelative
						.findViewById(R.id.new_house_details_units_item_desc);
				houseRoom.setText(jsonModel.getRoom());
				TextView houseArea = (TextView) itemRelative
						.findViewById(R.id.new_house_details_units_item_desc_area);
				mainLinear.addView(itemRelative);
				houseArea.setText(jsonModel.getArea());
			}
		} else {
			view.setVisibility(View.GONE);
		}
	}

	/**
	 * 问答
	 */
	@SuppressLint("ResourceAsColor")
	private void addAskAnswerItem(WendaJsonModel jsonModel) {

		findViewById(R.id.new_house_details_ask_answer_button)
				.setOnClickListener(new View.OnClickListener() {
					@SuppressWarnings("unchecked")
					@Override
					public void onClick(View v) {
						EditText editText = (EditText) findViewById(R.id.new_house_details_ask_answer_body);
						String askAnswerContent = editText.getText().toString();
						if (askAnswerContent != null
								&& !askAnswerContent.isEmpty()) {
							questionMap.put("content", editText.getText()
									.toString());
							QuestionAsyncTask questionAsyncTask = new QuestionAsyncTask();
							questionAsyncTask.execute(questionMap);
							editText.setText("");
						} else {
							Toast.makeText(getApplicationContext(),
									"提交的数据不能为空", Toast.LENGTH_SHORT).show();
						}
					}
				});

		View view = findViewById(R.id.new_house_details_ask_answer_evaluate_relative);
		if (jsonModel != null) {
			LinearLayout mainLinear = (LinearLayout) findViewById(R.id.new_house_details_ask_answer_linear_layout);
			TextView textView = (TextView) findViewById(R.id.new_house_details_ask_answer_text_view);
			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(NewHouseDetailsActivity.this,
							WendaListActivity.class);
					intent.putExtra("hid", newHid);
					startActivity(intent);
				}
			});
			String total = jsonModel.getTotal();
			if (total != null && !total.isEmpty()) {
				textView.setText("楼盘问答(" + total + ")");
			}
			for (WendaJsonModel.ListJsonModel listJsonModel : jsonModel
					.getList()) {
				RelativeLayout itemRelative = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.new_house_details_ask_answer_item,
								null);
				TextView askTextView = (TextView) itemRelative.findViewById(R.id.new_house_details_ask_text_view);
				String ask = listJsonModel.getContent();
				askTextView.setText(ask);
				askTextView.setTextColor(R.color.dark_bg);
				TextView answerTextView = (TextView) itemRelative.findViewById(R.id.new_house_details_answer_text_view);
				answerTextView.setTextColor(R.color.dark_bg);
				String answer = listJsonModel.getAnswer();
				DefaultDataUtils.setValue(answer, answerTextView);
				TextView resultTextView = (TextView) itemRelative.findViewById(R.id.new_house_details_result_text_view);
				resultTextView.setTextColor(R.color.dark_bg);
				String result = listJsonModel.getBestAnswer();
				String bestAnswer = listJsonModel.getBestAnswer();
				String updateTime = listJsonModel.getUpdatetime();
				if (result.equals("0") || bestAnswer != null|| !bestAnswer.equals("")) {
					resultTextView.setText("已解决|" + updateTime);
				} else {
					resultTextView.setText("未解决");
				}
				mainLinear.addView(itemRelative);
			}
		} else {
			view.setVisibility(View.GONE);
		}
	}

	/**
	 * 点评
	 */
	// 点评
	private void addBbsCommentItem(DianpingJsonModel jsonModel) {
		final RatingBar bbsRatingBar = (RatingBar) findViewById(R.id.new_house_details_bbs_rating_bar);
		final EditText editText = (EditText) findViewById(R.id.new_house_details_bbs_comment_edit_text);
		bbsRatingBar
				.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						ratingBar.setRating(rating);
					}
				});
		TextView commentButtonTextView = (TextView) findViewById(R.id.new_house_details_bbs_comment_button);
		commentButtonTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("RatingBar->>>>", bbsRatingBar.getRating() + "");
				int rating = (int) bbsRatingBar.getRating();
				String infoContent = editText.getText().toString();
				if (infoContent != null && !infoContent.isEmpty()) {
					commentMap.put("grade", rating + "");
					commentMap.put("info", editText.getText().toString());
					CommentAsyncTask commentAsyncTask = new CommentAsyncTask();
					commentAsyncTask.execute(commentMap);
					editText.setText("");
				} else {
					Toast.makeText(getApplicationContext(), "提交的数据不能为空",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		View view = findViewById(R.id.new_house_details_bbs_comment_relative_layout);
		if (jsonModel != null) {
			TextView textView = (TextView) findViewById(R.id.new_house_details_bbs_text_view);
			String total = jsonModel.getTotal();
			if (total != null && !total.isEmpty()) {
				textView.setText("楼盘点评(" + total + ")");
			}
			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(NewHouseDetailsActivity.this,
							CommentListActivity.class);
					intent.putExtra("hid", newHid);
					startActivity(intent);
				}
			});
			LinearLayout mainLinear = (LinearLayout) findViewById(R.id.new_house_details_bbs_comment_linear_layout);
			for (DianpingJsonModel.ListJsonModel listJsonModel : jsonModel
					.getList()) {
				RelativeLayout itemRelative = (RelativeLayout) LayoutInflater
						.from(this).inflate(
								R.layout.new_house_details_bbs_comment_item,
								null);
				final ImageView imageView = (ImageView) itemRelative
						.findViewById(R.id.new_house_details_bbs_comment_item_image_view);
				TextView userNameTextView = (TextView) itemRelative
						.findViewById(R.id.new_house_details_bbs_comment_item_user_type);
				RatingBar ratingBar = (RatingBar) itemRelative
						.findViewById(R.id.new_house_details_bbs_comment_item_rating_bar);
				TextView commentTextView = (TextView) itemRelative
						.findViewById(R.id.new_house_details_bbs_comment_item_time);
				TextView contentTextView = (TextView) itemRelative
						.findViewById(R.id.new_house_details_bbs_comment_item_content_text_view);
				final TextView trampleTextView = (TextView) itemRelative
						.findViewById(R.id.new_house_details_bbs_comment_item_reply_trample);
				final TextView topTextView = (TextView) itemRelative
						.findViewById(R.id.new_house_details_bbs_comment_item_reply_top);
				TextView replyTextView = (TextView) itemRelative
						.findViewById(R.id.new_house_details_bbs_comment_item_reply);
				String picUrl = listJsonModel.getAvatar();
				String userName = listJsonModel.getUsername();
				String rating = listJsonModel.getGrade();
				String commentTime = listJsonModel.getPosttime();
				String content = listJsonModel.getTitle();
				final String trample = listJsonModel.getDown() == null ? "0" : listJsonModel.getDown();
				final String top = listJsonModel.getDigg() == null ? "0" : listJsonModel.getDigg();
				String reply = listJsonModel.getReplynum() == null ? "0" : listJsonModel.getReplynum();
				if (picUrl != null && !picUrl.isEmpty()) {
					new DownloadImageTask().doInBackground(picUrl, imageView,
							R.drawable.a);
				}
				userNameTextView.setText(userName);
				ratingBar.setRating(Float.parseFloat(rating));
				commentTextView.setText(commentTime);
				DefaultDataUtils.setValue(content, contentTextView);
				trampleTextView.setText("踩" + trample);
				topTextView.setText("顶" + top);
				replyTextView.setText("回复" + reply);
				mainLinear.addView(itemRelative);
				final String cid = listJsonModel.getCid();
				topTextView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ViewpointAsyncTask viewpointAsyncTask = new ViewpointAsyncTask(topTextView, top, "ding");
						viewpointAsyncTask.execute(cid, "digg");
					}
				});
				trampleTextView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ViewpointAsyncTask viewpointAsyncTask = new ViewpointAsyncTask(trampleTextView, trample, "cai");
						viewpointAsyncTask.execute(cid, "down");
					}
				});
			}
		} else {
			view.setVisibility(View.GONE);
		}

	}

	/**
	 * 印象
	 */
	private void setGridItem(ImpressionJsonModel jsonModel) {
		final EditText impressEditText = (EditText) findViewById(R.id.new_house_details_impress_edit_text);
		findViewById(R.id.new_house_details_impress_button).setOnClickListener(
				new View.OnClickListener() {
					@SuppressWarnings("unchecked")
					@Override
					public void onClick(View v) {
						String impressionContent = impressEditText.getText().toString();
						if (impressionContent != null&& !impressionContent.isEmpty()) {
							impressionMap.put("name", impressEditText.getText().toString());
							ImpressionAsyncTask impressionAsyncTask = new ImpressionAsyncTask();
							impressionAsyncTask.execute(impressionMap);
							impressEditText.setText("");
						} else {
							Toast.makeText(getApplicationContext(),"提交的数据不能为空", Toast.LENGTH_SHORT).show();
						}

					}
				});
		View view = findViewById(R.id.new_house_details_impress_relative_layout);
		if (jsonModel != null) {
			TextView totalTextView = (TextView) findViewById(R.id.new_house_details_house_impress);
			totalTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(NewHouseDetailsActivity.this,ImpressionListActivity.class);
					intent.putExtra("hid", newHid);
					startActivity(intent);
				}
			});
			int total = Integer.parseInt(jsonModel.getTotal());
			if (total > 0) {
				totalTextView.setText("楼盘印象(" + total + ")");
			}
			List<ImpressionJsonModel.ListJsonModel> listJsonModel = jsonModel
					.getList();
			if (listJsonModel != null && !listJsonModel.isEmpty()) {
				final List<Map<Integer, Object>> gridList = new ArrayList<Map<Integer, Object>>();
				final Map<Integer, String> paramMap = new HashMap<Integer, String>();
				for (int i = 0; i < listJsonModel.size(); i++) {
					ImpressionJsonModel.ListJsonModel tempJson = listJsonModel.get(i);
					Map<Integer, Object> map = new HashMap<Integer, Object>();
					String color = tempJson.getBgcolor();
					final String beforeStr;
					final String countStr = tempJson.getSupport();
					if (color != null && !color.isEmpty() && color.length() > 6) {
						beforeStr = "background:" + tempJson.getBgcolor() + ":"
								+ tempJson.getName() + "(";
						map.put(R.id.new_house_details_description_grid_text,
								beforeStr + countStr + ")");
					} else {
						beforeStr = "background:#00CCCC" + ":"
								+ tempJson.getName() + "(";
						map.put(R.id.new_house_details_description_grid_text,
								beforeStr + countStr + ")");
					}
					paramMap.put(i, beforeStr);
					map.put(-1, tempJson.getId());

					gridList.add(map);
				}
				final ItemAdapter adapter = new ItemAdapter(
						NewHouseDetailsActivity.this,
						R.layout.new_house_details_grid_item, gridList);
				ZjGridView zjGridView = (ZjGridView) findViewById(R.id.new_house_details_impress_evaluate_grid);
				zjGridView.setAdapter(adapter);
				zjGridView
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								Map<Integer, Object> tempMap = gridList
										.get(position);
								String impressId = (String) tempMap.get(-1);
								ImpressionSupportAsyncTask impressionSupportAsyncTask = new ImpressionSupportAsyncTask(
										tempMap, adapter, paramMap
												.get(position));
								impressionSupportAsyncTask.execute(impressId);
							}
						});
			}
		} else {
			view.setVisibility(View.GONE);
		}
	}

	private void setHeaderValue(String name) {
		// 设置头消息
		Map<Integer, Object> map = new HashMap<Integer, Object>();
		map.put(header_title, name);
		map.put(R.id.details_button_collect, getString(R.string.attention));
		setHeader(map);
		Map<Integer, Object> map1 = new HashMap<Integer, Object>();
		map1.put(R.id.details_button_collect, "visibility:" + View.VISIBLE);
		setHeader(map1);
		findViewById(R.id.details_button_collect).setOnClickListener(
				new View.OnClickListener() {
					@SuppressWarnings("unchecked")
					@Override
					public void onClick(View v) {
						if (Global.USER_AUTH_STR.equals("")) {
							Intent loginIntent = new Intent(NewHouseDetailsActivity.this,LoginActivity.class);
							startActivityForResult(loginIntent, 101);
						} else {
							AttentionAsyncTask attentionAsyncTask = new AttentionAsyncTask();
							attentionAsyncTask.execute(paramMap);
						}
					}
				});
	}

	private void setClientService(final NewHouseDetailJsonModel jsonModel) {
		TextView detailsTel = (TextView) findViewById(R.id.new_house_details_telephone);
		detailsTel.setText("售楼电话:\n" + jsonModel.getClientServiceTell());
		TextView cellTelTextView = (TextView) findViewById(R.id.new_house_details_cell_telephone);
		cellTelTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String cellTel = jsonModel.getClientService();
				AlertDialog.Builder builder = new AlertDialog.Builder(NewHouseDetailsActivity.this);
				builder.setMessage(getResources().getString(R.string.confirm_dial)+ "\n" + cellTel);
				builder.setTitle(getResources().getString(R.string.prompt));
				builder.setPositiveButton(getResources().getString(R.string.confirm),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String[] tels = cellTel.split(",");
								Log.d("setClientService",tels[0].replaceAll("-", ""));
								Uri uri = Uri.parse("tel:"+ tels[0].replaceAll("-", "")); // 拨打电话号码的URI格式
								Intent intent = new Intent(); // 实例化Intent
								intent.setAction(Intent.ACTION_CALL); // 指定Action
								intent.setData(uri); // 设置数据
								startActivity(intent);
							}

						});
				builder.setNegativeButton(
						getResources().getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();

			}
		});

	}

	private void setHouseDetail(DetailJsonModel jsonModel) {

		View view = findViewById(R.id.new_house_details_house_desc_relative_layout);
		if (jsonModel != null) {
			// TextView openTimeTextView = (TextView)
			// findViewById(R.id.new_house_details_house_desc_right_open_time_value);
			TextView dealTimeTextView = (TextView) findViewById(R.id.new_house_details_house_desc_right_deal_time_value);
			TextView projectTypeTextView = (TextView) findViewById(R.id.new_house_details_house_desc_right_project_type_value);
			TextView projectPlanTextView = (TextView) findViewById(R.id.new_house_details_house_desc_right_project_plan_value);
			TextView conditionTextView = (TextView) findViewById(R.id.new_house_details_house_desc_right_decorate_condition_value);
			TextView projectAreaTextView = (TextView) findViewById(R.id.new_house_details_house_desc_right_project_area_value);
			TextView planAreaTextView = (TextView) findViewById(R.id.new_house_details_house_desc_right_plan_area);
			TextView planCountTextView = (TextView) findViewById(R.id.new_house_details_house_desc_right_plan_count);
			TextView volumeRatioTextView = (TextView) findViewById(R.id.new_house_details_house_desc_right_volume_ratio);
			TextView afforestRatioTextView = (TextView) findViewById(R.id.new_house_details_house_desc_right_afforest_ratio);
			TextView stopSpaceTextView = (TextView) findViewById(R.id.new_house_details_house_desc_right_stop_space);
			TextView houseAgeTextView = (TextView) findViewById(R.id.new_house_details_house_desc_right_house_age);
			TextView presellNumTextView = (TextView) findViewById(R.id.new_house_details_house_desc_right_presell__number);
			TextView permissionScopeTextView = (TextView) findViewById(R.id.new_house_details_house_desc_right_permission__scope);
			TextView qqGroupTextView = (TextView) findViewById(R.id.new_house_details_house_desc_right_sell_house_qq);
			TextView sellHouseAddressTextView = (TextView) findViewById(R.id.new_house_details_house_desc_right_sell_house_address);
			TextView houseAddressTextView = (TextView) findViewById(R.id.new_house_details_house_desc_right_house_address);
			TextView developersTextView = (TextView) findViewById(R.id.new_house_details_house_desc_right_developers);
			String openTime = jsonModel.getOpentimeYearMonth();
			String dealTime = jsonModel.getLivetime();
			String projectType = "";
			StringBuffer sb = new StringBuffer();
			for (String temp : jsonModel.getProjecttype()) {
				Log.d("NewHouseDetail->>> temp", temp);
				sb.append(temp + ",");
			}
			projectType = sb.substring(0, sb.length() - 1).toString();
			Log.d("NewHouseDetail->>> projectType", projectType + "::::sb==="
					+ sb.toString());
			String projectProgress = jsonModel.getProjectProgress();
			String decoration = jsonModel.getDecoration();
			String projectArea = jsonModel.getAreabuild();
			String planArea = jsonModel.getArealand();
			String planCount = jsonModel.getRoomnum();
			String volumeRatio = jsonModel.getCapacityRate();
			String afforestRatio = jsonModel.getGreenRate();
			String stopSpace = jsonModel.getParking();
			String houseAge = jsonModel.getPropertyage();
			DetailJsonModel.CarddataJsonModel carddataJsonModel = jsonModel.getCarddata();
			String presellNum = "";
			String permissionScope = "";
			if (carddataJsonModel != null) {
				presellNum = carddataJsonModel.getCardnumber();
				permissionScope = carddataJsonModel.getExtent();
			}
			String qqGroup = jsonModel.getQqgroup();
			String sellHouseAddress = jsonModel.getSaleaddress();
			String houseAddress = jsonModel.getAddress();
			String developers = jsonModel.getDeveloper();

			// DefaultDataUtils.setValue(openTime, openTimeTextView);

			DefaultDataUtils.setValue(dealTime, dealTimeTextView);

			DefaultDataUtils.setValue(projectType, projectTypeTextView);

			DefaultDataUtils.setValue(projectProgress, projectPlanTextView);

			DefaultDataUtils.setValue(decoration, conditionTextView);

			DefaultDataUtils.setValue(projectArea, projectAreaTextView);

			DefaultDataUtils.setValue(planArea, planAreaTextView);

			DefaultDataUtils.setValue(planCount, planCountTextView);

			DefaultDataUtils.setValue(volumeRatio, volumeRatioTextView);

			DefaultDataUtils.setValue(afforestRatio, afforestRatioTextView);

			DefaultDataUtils.setValue(stopSpace, stopSpaceTextView);

			DefaultDataUtils.setValue(houseAge, houseAgeTextView);

			DefaultDataUtils.setValue(presellNum, presellNumTextView);

			DefaultDataUtils.setValue(permissionScope, permissionScopeTextView);

			DefaultDataUtils.setValue(qqGroup, qqGroupTextView);

			DefaultDataUtils.setValue(sellHouseAddress,
					sellHouseAddressTextView);

			DefaultDataUtils.setValue(houseAddress, houseAddressTextView);

			DefaultDataUtils.setValue(developers, developersTextView);

		} else {
			view.setVisibility(View.GONE);
		}

	}

	private void setIntroduce(NewHouseDetailJsonModel detailJsonModel) {
		IntroductionJsonModel introductionJsonModel = detailJsonModel
				.getIntroduction();
		TuanJsonModel tuanJsonModel = detailJsonModel.getTuan();
		BusinessJsonModel businessJsonModel = detailJsonModel.getBusiness();
		((TextView) findViewById(R.id.new_house_details_house_introduce_content))
				.setText(introductionJsonModel.getName());
		setHeaderValue(introductionJsonModel.getName());
		TextView averagepriceTextView = (TextView) findViewById(R.id.new_house_details_house_introduce_price_value);
		TextView buildfeatureOneTextView = (TextView) findViewById(R.id.new_house_details_house_introduce_content_buildfeature_one);
		TextView buildfeatureTwoTextView = (TextView) findViewById(R.id.new_house_details_house_introduce_content_buildfeature_two);
		TextView openTimeTextView = (TextView) findViewById(R.id.new_house_details_house_introduce_open_time_value);
		TextView houseAreaTextView = (TextView) findViewById(R.id.new_house_details_introduce_house_area_value);
		TextView equityTextView = (TextView) findViewById(R.id.new_house_details_introduce_price_equity_value);
		TextView unitTextView = (TextView) findViewById(R.id.new_house_details_introduce_house_unit_value);
		View tuanView = findViewById(R.id.new_house_details_tuan_relative);
		View splitLineView = findViewById(R.id.new_house_details_house_split_line_three);
		View privilegeView = findViewById(R.id.new_house_details_privilege_relative);
		String averageprice = introductionJsonModel.getAverageprice();
		String openTime = introductionJsonModel.getOpentimeYearMonth();
		String houseArea = introductionJsonModel.getRoomarea();
		String propertyAge = introductionJsonModel.getPropertyage();
		String roomType = introductionJsonModel.getRoomtype();
		List<String> buildfeatureList = introductionJsonModel.getBuildfeature();
		if (averageprice != null && !averageprice.isEmpty()) {
			averagepriceTextView.setText(averageprice);
		} else {
			averagepriceTextView.setText("暂无");
		}
		if (buildfeatureList != null && !buildfeatureList.isEmpty()) {
			int size = buildfeatureList.size();
			String tempStr = buildfeatureList.get(0);
			if (!tempStr.equals("")) {
				buildfeatureOneTextView.setText(buildfeatureList.get(0));
			} else {
				buildfeatureOneTextView.setVisibility(View.GONE);
			}

			if (size >= 2) {
				buildfeatureTwoTextView.setText(buildfeatureList.get(1));
			} else {
				buildfeatureTwoTextView.setVisibility(View.GONE);
			}
		} else {
			buildfeatureOneTextView.setVisibility(View.GONE);
			buildfeatureTwoTextView.setVisibility(View.GONE);
		}
		if (openTime != null && !openTime.isEmpty()) {
			openTimeTextView.setText(openTime);
		} else {
			openTimeTextView.setText("暂无");
		}
		if (houseArea != null && !houseArea.isEmpty()) {
			houseAreaTextView.setText(houseArea);
		} else {
			houseAreaTextView.setText("暂无");
		}
		if (propertyAge != null && !propertyAge.isEmpty()) {
			equityTextView.setText(propertyAge);
		} else {
			equityTextView.setText("暂无");
		}
		if (roomType != null && !roomType.isEmpty()) {
			unitTextView.setText(roomType);
		} else {
			unitTextView.setText("暂无");
		}
		if (tuanJsonModel != null) {
			TextView tuanTitleTextView = (TextView) findViewById(R.id.new_house_details_tuan_title);
			TextView endTimeTextView = (TextView) findViewById(R.id.new_house_details_tuan_end_time_value);
			TextView peopleCountTextView = (TextView) findViewById(R.id.new_house_details_tuan_people_count);
			TextView peopleEndTextView = (TextView) findViewById(R.id.new_house_details_tuan_people_end_value);
			TextView applyTextView = (TextView) findViewById(R.id.new_house_details_tuan_now_apply);
			String tuanTitle = tuanJsonModel.getTitle();
			String endTime = tuanJsonModel.getEnd();
			String nums = tuanJsonModel.getNums();
			String end = tuanJsonModel.getEnd();
			if (tuanTitle != null && !tuanTitle.isEmpty()) {
				tuanTitleTextView.setText(tuanTitle);
			} else {
				tuanTitleTextView.setText("暂无");
			}
			if (endTime != null && !endTime.isEmpty()) {
				endTimeTextView.setText(endTime);
			} else {
				endTimeTextView.setText("暂无");
			}
			if (nums != null && !nums.isEmpty()) {
				peopleCountTextView.setText(nums);
			} else {
				peopleCountTextView.setText("暂无");
			}
			if (end != null && !end.isEmpty()) {
				peopleEndTextView.setText(end);
			} else {
				peopleEndTextView.setText("暂无");
			}
			applyTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent immediatelyApplyIntent = new Intent(
							NewHouseDetailsActivity.this,
							CondoTourApplyActivity.class);
					immediatelyApplyIntent.putExtra("source", 1);
					immediatelyApplyIntent.putExtra("id", newHid);
					startActivity(immediatelyApplyIntent);
				}
			});

		} else {
			tuanView.setVisibility(View.GONE);
			splitLineView.setVisibility(View.GONE);
		}

		if (businessJsonModel != null) {
			TextView privilegetime_value = (TextView) findViewById(R.id.new_house_details_privilege_time_value);
			TextView privilegeTitleTextView = (TextView) findViewById(R.id.new_house_details_privilege_title);
			TextView privilegeTimeTextView = (TextView) findViewById(R.id.new_house_details_privilege_time);
			TextView peopleCountTextView = (TextView) findViewById(R.id.new_house_details_privilege_people_count);
			String privilege = businessJsonModel.getPrivilege();
			String privilegeTime = businessJsonModel.getTime();
			String peopleCount = businessJsonModel.getNums();
			if (privilege != null && !privilege.isEmpty()) {
				privilegeTitleTextView.setText(privilege);
			} else {
				privilegeTitleTextView.setText("暂无");
			}
			if (privilegeTime != null && !privilegeTime.isEmpty()) {
				String a[] = privilegeTime.split("~");
				System.out.println(a[0]);
				System.out.println(a[1]);
				DateFormat fmt = new SimpleDateFormat("yyyy.MM.dd");
				Date date;
				Date date2;
				try {
					date2 = fmt.parse(a[1]);
					date = fmt.parse(a[0]);
					long cha = date2.getTime() / (24 * 60 * 60 * 1000)
							- date.getTime() / (24 * 60 * 60 * 1000);// 计算两个日期相差的天数
					privilegetime_value.setText("\n剩余天数:" + cha + "天");
					privilegeTimeTextView.setText("暂无");
				} catch (ParseException e) {
					e.printStackTrace();
				}

				privilegeTimeTextView.setText(privilegeTime);
			} else {

			}
			if (peopleCount != null && !peopleCount.isEmpty()) {
				peopleCountTextView.setText(peopleCount);
			} else {
				peopleCountTextView.setText("暂无");
			}
		} else {
			privilegeView.setVisibility(View.GONE);
		}

	}

	/**
	 * 趋势图
	 */
	private void setTrend(TrendJsonModel jsonModel) {
		View view = findViewById(R.id.new_house_details_house_trend_relative);
		if (jsonModel != null) {
			TextView priceTextView = (TextView) findViewById(R.id.new_house_details_house_trend_price);
			final ImageView imageView = (ImageView) findViewById(R.id.new_house_details_house_trend_image);
			String percent = jsonModel.getPercent();
			String price = jsonModel.getAverageprice();
			String trend = jsonModel.getTrend();
			StringBuffer sb = new StringBuffer();
			if (price != null && !price.isEmpty()) {
				sb.append("当前平均价：" + price);
			} else {
				sb.append("当前平均价：暂无");
			}
			if (percent != null && !percent.isEmpty()) {
				sb.append(",价格涨幅:" + percent);
			} else {
				sb.append(",价格涨幅: 暂无");
			}
			priceTextView.setText(sb.toString());
			if (trend != null && !trend.isEmpty()) {
				new DownloadImageTask().doInBackground(trend, imageView, null,
						true);
			} else {
				imageView.setVisibility(View.GONE);
			}

		} else {
			view.setVisibility(View.GONE);
		}
	}

	private void setMap(MapJsonModel jsonModel) {
		View view = findViewById(R.id.new_house_details_house_traffic_relative);
		if (jsonModel != null) {
			TextView situationTextView = (TextView) findViewById(R.id.new_house_details_house_traffic_situation_content);
			TextView gatherTextView = (TextView) findViewById(R.id.new_house_details_house_traffic_gather_content);
			TextView introduceTextView = (TextView) findViewById(R.id.new_house_details_house_traffic_project_introduce_content);
			final ImageView imageView = (ImageView) findViewById(R.id.new_house_details_house_traffic_image);
			String situation = jsonModel.getBus();
			String gather = jsonModel.getPeripheralSupport();
			String introduce = jsonModel.getInfo();
			DefaultDataUtils.setValue(situation, situationTextView);
			DefaultDataUtils.setValue(gather, gatherTextView);
			DefaultDataUtils.setValue(introduce, introduceTextView);
			String url = jsonModel.getMap();
			if (url != null && !url.isEmpty()) {
				new DownloadImageTask().doInBackground(url, imageView, null,
						true);
			} else {
				imageView.setVisibility(View.GONE);
			}
		} else {
			view.setVisibility(View.GONE);
		}
	}

	@Override
	public JsonResult<NewHouseDetailJsonModel> getDetailDataByNetWork(String id) {
		HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
		Map<String, String> map = new HashMap<String, String>();
		map.put("cityid", Global.NOW_CITY_ID);
		map.put("hid", id);
		Optional<JsonResult<NewHouseDetailJsonModel>> optional = httpClientUtils
				.getUnsignedByData(DETAIL_URL, map,
						NewHouseDetailJsonModel.class);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public void startListTask(String hid) {
		NewHouseDetailsAsyncTask task = new NewHouseDetailsAsyncTask();
		task.execute(hid);
	}

	/**
	 * 新房详情幻灯片
	 */
	private void setLoopPic(SlideJsonModel slideJsonModel, int dotId) {
		LinearLayout linearLayout = (LinearLayout) findViewById(dotId);
		if (slideJsonModel.getList() != null) {
			((TextView) findViewById(R.id.new_house_details_house_image_text_view))
					.setText("共" + slideJsonModel.getCount() + "图");
			for (final SlideJsonModel.ListJsonModel tempJson : slideJsonModel
					.getList()) {
				final ImageView imageView = new ImageView(
						NewHouseDetailsActivity.this);
				Log.d("NewHouseDetailsActivity->jsonResult->size",
						tempJson.getPic());
				new DownloadImageTask().doInBackground(tempJson.getPic(),
						imageView, R.drawable.a, false,
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(
										NewHouseDetailsActivity.this,
										//改：增加了pid,pic
										NewHouseAlbumDetailsActivity.class);
//								intent.putExtra("position", value);
								intent.putExtra("pid", tempJson.getPid());
								intent.putExtra("pic", tempJson.getPic());
								intent.putExtra("hid", newHid);
								System.out.println("hid" + newHid);
								startActivity(intent);
							}
						});
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				imageViews.add(imageView);
			}

			for (int i = 0; i < imageViews.size(); i++) {
				View dotView = new View(NewHouseDetailsActivity.this);
				if (i == 0) {
					dotView.setBackgroundResource(R.drawable.index_top_dot_selected);
				} else {
					dotView.setBackgroundResource(R.drawable.index_top_dot_unselected);
				}
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						ScreenUtil.dps2pixels(5, NewHouseDetailsActivity.this),
						ScreenUtil.dps2pixels(5, NewHouseDetailsActivity.this));
				params.setMargins(ScreenUtil.dps2pixels(1.5,
						NewHouseDetailsActivity.this), ScreenUtil.dps2pixels(
						15, NewHouseDetailsActivity.this), ScreenUtil
						.dps2pixels(1.5, NewHouseDetailsActivity.this),
						ScreenUtil.dps2pixels(15, NewHouseDetailsActivity.this));
				dotView.setLayoutParams(params);
				dotsList.add(dotView);
				linearLayout.addView(dotView);
			}
			// 设置一个监听器，当ViewPager中的页面改变时调用
			viewPager.setOnPageChangeListener(new SplashImageListener());
			viewPager.setCurrentItem(currentItem);
		} else {
			ImageView imageView = new ImageView(NewHouseDetailsActivity.this);
			imageView.setImageResource(getResources().getIdentifier("house_bg",
					"drawable", Global.PACKAGE_NAME));
			imageViews.add(imageView);
		}
		// 设置填充ViewPager页面的适配器
		viewPager.setAdapter(new SplashImageAdapter());
	}

	public Map<String, String> sendImpression(Map<String, String> param) {
		String url = param.get("url");
		String hid = param.get("hid");
		String name = param.get("name");
		Map<String, String> resultMap = new HashMap<String, String>();
		HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
		Map<String, String> map = new HashMap<String, String>();
		map.put("hid", hid);
		map.put("name", name);
		map.put("cityid", Global.NOW_CITY_ID);
		Optional<Map<String, String>> optional = httpClientUtils.postSignedMap(
				url, map, String.class);
		if (optional.isPresent()) {
			resultMap.put("status", optional.get().get("status"));
			resultMap.put("message", optional.get().get("message"));
			return resultMap;
		}
		return null;
	}

	public Map<String, String> sendQuestion(Map<String, String> param) {
		String url = param.get("url");
		String hid = param.get("houseid");
		String content = param.get("content");
		Map<String, String> resultMap = new HashMap<String, String>();
		HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
		Map<String, String> map = new HashMap<String, String>();
		map.put("houseid", hid);
		map.put("content", content);
		map.put("cityid", Global.NOW_CITY_ID);
		if (!Global.USER_AUTH_STR.equals("")) {
			map.put("authstr", Global.USER_AUTH_STR);
		}
		Optional<Map<String, String>> optional = httpClientUtils.postSignedMap(
				url, map, String.class);
		if (optional.isPresent()) {
			resultMap.put("status", optional.get().get("status"));
			resultMap.put("message", optional.get().get("message"));
			return resultMap;
		}
		return null;
	}

	public Map<String, String> sendComment(Map<String, String> param) {
		String url = param.get("url");
		String hid = param.get("hid");
		String grade = param.get("grade");
		String info = param.get("info");
		Map<String, String> resultMap = new HashMap<String, String>();
		HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
		Map<String, String> map = new HashMap<String, String>();
		map.put("hid", hid);
		map.put("grade", grade);
		map.put("info", info);
		map.put("cityid", Global.NOW_CITY_ID);
		if (!Global.USER_AUTH_STR.equals("")) {
			map.put("authstr", Global.USER_AUTH_STR);
		}
		Optional<Map<String, String>> optional = httpClientUtils.postSignedMap(
				url, map, String.class);
		if (optional.isPresent()) {
			resultMap.put("status", optional.get().get("status"));
			resultMap.put("message", optional.get().get("message"));
			return resultMap;
		}
		return null;
	}

	public Map<String, String> sendImpressionSupport(String impressionId) {
		Map<String, String> resultMap = new HashMap<String, String>();
		HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", impressionId);
		Optional<Map<String, String>> optional = httpClientUtils.postSignedMap(
				IMPRESSION_SUPPORT_URL, map, String.class);
		if (optional.isPresent()) {
			resultMap.put("status", optional.get().get("status"));
			resultMap.put("message", optional.get().get("message"));
			return resultMap;
		}
		return null;
	}

	public Map<String, String> sendViewpoint(String cid, String field) {
		Map<String, String> resultMap = new HashMap<String, String>();
		HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
		Map<String, String> map = new HashMap<String, String>();
		map.put("cid", cid);
		map.put("field", field);
		Optional<Map<String, String>> optional = httpClientUtils.postSignedMap(
				VIEW_POINR_URL, map, String.class);
		if (optional.isPresent()) {
			resultMap.put("status", optional.get().get("status"));
			resultMap.put("message", optional.get().get("message"));
			return resultMap;
		}
		return null;
	}

	public class ViewpointAsyncTask extends
			AsyncTask<String, Void, Map<String, String>> {

		TextView textView;
		String value;
		String type;

		public ViewpointAsyncTask(TextView textView, String value, String type) {
			this.textView = textView;
			this.value = value;
			this.type = type;
		}

		@Override
		protected Map<String, String> doInBackground(String... params) {
			return sendViewpoint(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(Map<String, String> stringStringMap) {
			if (stringStringMap != null) {
				if (type.equals("ding")) {
					textView.setText("顶"
							+ String.valueOf(Integer.parseInt(value) + 1));
				} else {
					textView.setText("踩"
							+ String.valueOf(Integer.parseInt(value) + 1));
				}

				Toast.makeText(getApplicationContext(),
						stringStringMap.get("message"), Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getApplicationContext(), "网络数据获取失败",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	// 印象支持
	public class ImpressionSupportAsyncTask extends
			AsyncTask<String, Void, Map<String, String>> {

		Map<Integer, Object> map;
		ItemAdapter itemAdapter;
		String beforeStr;

		public ImpressionSupportAsyncTask(Map<Integer, Object> map,
				ItemAdapter itemAdapter, String beforeStr) {
			this.map = map;
			this.itemAdapter = itemAdapter;
			this.beforeStr = beforeStr;
		}

		@Override
		protected Map<String, String> doInBackground(String... params) {
			return sendImpressionSupport(params[0]);
		}

		@Override
		protected void onPostExecute(Map<String, String> resultMap) {
			if (resultMap != null) {
				String value = (String) map
						.get(R.id.new_house_details_description_grid_text);
				String nowCount = "";
				int tempCount = value.indexOf("(");

				if (tempCount != -1) {
					nowCount = value.substring(tempCount + 1,
							value.length() - 1);

				}
				if (!nowCount.equals("")) {
					nowCount = String.valueOf(Integer.parseInt(nowCount) + 1);
				}
				map.put(R.id.new_house_details_description_grid_text, beforeStr
						+ nowCount + ")");
				itemAdapter.notifyDataSetChanged();
				Toast.makeText(getApplicationContext(),
						resultMap.get("message"), Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "网络数据获取失败",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public class CommentAsyncTask extends
			AsyncTask<Map<String, String>, Void, Map<String, String>> {

		@Override
		protected Map<String, String> doInBackground(
				Map<String, String>... params) {
			return sendComment(params[0]);
		}

		@Override
		protected void onPostExecute(Map<String, String> resultMap) {
			if (resultMap != null) {
				Toast.makeText(getApplicationContext(),
						"点评:" + resultMap.get("message"), Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getApplicationContext(), "网络数据获取失败",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public class QuestionAsyncTask extends
			AsyncTask<Map<String, String>, Void, Map<String, String>> {

		@Override
		protected Map<String, String> doInBackground(
				Map<String, String>... params) {
			return sendQuestion(params[0]);
		}

		@Override
		protected void onPostExecute(Map<String, String> resultMap) {
			if (resultMap != null) {
				Toast.makeText(getApplicationContext(),
						"楼盘问答:" + resultMap.get("message"), Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getApplicationContext(), "网络数据获取失败",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public class ImpressionAsyncTask extends
			AsyncTask<Map<String, String>, Void, Map<String, String>> {

		@Override
		protected Map<String, String> doInBackground(
				Map<String, String>... params) {
			return sendImpression(params[0]);
		}

		@Override
		protected void onPostExecute(Map<String, String> resultMap) {
			if (resultMap != null) {
				Toast.makeText(getApplicationContext(),
						"楼盘印象:" + resultMap.get("message"), Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getApplicationContext(), "网络数据获取失败",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public class NewHouseDetailsAsyncTask extends
			AsyncTask<String, Void, JsonResult<NewHouseDetailJsonModel>> {

		@Override
		protected JsonResult<NewHouseDetailJsonModel> doInBackground(
				String... params) {
			return getDetailDataByNetWork(params[0]);
		}

		@Override
		protected void onPostExecute(
				JsonResult<NewHouseDetailJsonModel> jsonResult) {
			findViewById(R.id.new_house_list_wait_load_relative).setVisibility(
					View.GONE);
			findViewById(R.id.new_house_details_relative).setVisibility(
					View.VISIBLE);
			findViewById(R.id.details_back).setOnClickListener(
					new DetailsOnClickListener());
			if (jsonResult != null && jsonResult.isStatus()) {
				setLoopPic(jsonResult.getData().getSlide(),
						R.id.new_house_details_house_dotsId);
				setIntroduce(jsonResult.getData());//
				setClientService(jsonResult.getData());//
				addHouseDynamicItem(jsonResult.getData().getNews());
				addHouseUnitsItem(jsonResult.getData().getUnits());
				setHouseDetail(jsonResult.getData().getDetail());
				addAskAnswerItem(jsonResult.getData().getWenda());
				addBbsCommentItem(jsonResult.getData().getDianping());
				setGridItem(jsonResult.getData().getImpression());
				setTrend(jsonResult.getData().getTrend());
				setMap(jsonResult.getData().getMap());
			}
		}
	}
}
