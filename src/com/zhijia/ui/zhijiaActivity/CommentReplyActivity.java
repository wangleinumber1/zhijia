package com.zhijia.ui.zhijiaActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.CommentReplyJsonModel;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.data.Medol.ReplyListJsonModel;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;
import com.zhijia.ui.list.adapter.ItemAdapter;
import com.zhijia.util.DefaultDataUtils;
import com.zhijia.util.DownloadImageTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class CommentReplyActivity extends BaseDetailsActivity {

    private final int header_title = R.id.details_title;
    private final String REPLY_LIST_URL = Global.API_WEB_URL + "/xinfang/replylist";
    private final String COMMENT_URL = Global.API_WEB_URL + "/xinfang/reply";
    private String hid;
    private String cid;
    private OnClickListener onClickListener = new OnClickListener();

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_reply);
        hid = getIntent().getStringExtra("hid");
        cid = getIntent().getStringExtra("cid");
        findViewById(R.id.details_back).setOnClickListener(onClickListener);
        listView = (ListView) findViewById(R.id.comment_reply_content_list_view);

        findViewById(R.id.comment_reply_button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.comment_reply_edit_text);
                String infoContent = editText.getText().toString();
                if (infoContent != null && !infoContent.isEmpty()) {
                    Map<String, String> commentMap = new HashMap<String, String>();
                    commentMap.put("info", editText.getText().toString());
                    CommentAsyncTask commentAsyncTask = new CommentAsyncTask();
                    commentAsyncTask.execute(commentMap);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "评论数据不能为空", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        findViewById(R.id.comment_reply_button_ok_gone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.comment_reply_edit_text_gone);
                String infoContent = editText.getText().toString();
                if (infoContent != null && !infoContent.isEmpty()) {
                    Map<String, String> commentMap = new HashMap<String, String>();
                    commentMap.put("info", editText.getText().toString());
                    CommentAsyncTask commentAsyncTask = new CommentAsyncTask();
                    commentAsyncTask.execute(commentMap);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "评论数据不能为空", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        setHeader();
        startTask();
    }

    public void setHeader() {
        Map<Integer, Object> map = new HashMap<Integer, Object>();
        map.put(header_title, "楼盘点评回复");
        setHeader(map);
        Map<Integer, Object> map1 = new HashMap<Integer, Object>();
        setHeader(map1);
    }


    public void setCommentReply(CommentReplyJsonModel jsonModel) {
        final ImageView imageView = (ImageView) findViewById(R.id.comment_reply_item_image_view);
        TextView userNameTextView = (TextView) findViewById(R.id.comment_reply_item_user_type);
        RatingBar ratingBarComment = (RatingBar) findViewById(R.id.comment_reply_item_rating_bar);
        TextView replyTimeTextView = (TextView) findViewById(R.id.comment_reply_item_time);
        TextView contentTextView = (TextView) findViewById(R.id.comment_reply_item_content_text_view);
        TextView trampleTextView = (TextView) findViewById(R.id.comment_reply_item_reply_trample);
        TextView topTextView = (TextView) findViewById(R.id.comment_reply_item_reply_top);
        TextView replyTextView = (TextView) findViewById(R.id.comment_reply_item_reply);
        if (jsonModel.getAvatar() != null && !jsonModel.getAvatar().isEmpty()) {
            new DownloadImageTask().doInBackground(jsonModel.getAvatar(), imageView, R.drawable.user_head_default);
        } else {
            imageView.setImageResource(R.drawable.user_head_default);
        }
        if (jsonModel.getGrade() != null && !jsonModel.getGrade().isEmpty()) {
            ratingBarComment.setRating(Float.parseFloat(jsonModel.getGrade()));
        } else {
            ratingBarComment.setRating(0);
        }
        DefaultDataUtils.setValue(jsonModel.getUserName(), userNameTextView);
        DefaultDataUtils.setValue(jsonModel.getPostTime(), replyTimeTextView);
        DefaultDataUtils.setValue(jsonModel.getInfo(), contentTextView);
        String down = jsonModel.getDown();
        String top = jsonModel.getDigg();
        String replyNum = jsonModel.getReplyNum();

        if (down != null && down.isEmpty()) {
            down = "踩" + down;
        } else {
            down = "踩0";
        }
        trampleTextView.setText(down);

        if (top != null && !top.isEmpty()) {
            top = "顶" + top;
        } else {
            top = "顶0";
        }
        topTextView.setText(top);
        if (replyNum != null && !replyNum.isEmpty()) {
            replyNum = "回复" + replyNum;
        } else {
            replyNum = "回复0";
        }
        replyTextView.setText(replyNum);

        List<ReplyListJsonModel> replyListJsonModels = jsonModel.getReply();
        List<Map<Integer, Object>> listItems = new ArrayList<Map<Integer, Object>>();
        if (replyListJsonModels != null) {

            TextView tempTextView = (TextView) findViewById(R.id.comment_reply_content_name_text_view_two);
            if (replyListJsonModels.size() > 0) {
                tempTextView.setText("现有" + replyListJsonModels.size() + "个回复");
            } else {
                tempTextView.setText("快来回复");
            }

            for (ReplyListJsonModel temp : replyListJsonModels) {
                Map<Integer, Object> map = new HashMap<Integer, Object>();
                String picURL = temp.getAvatar();
                String userName = temp.getUserName();
                String time = temp.getPostTime();
                String info = temp.getInfo();
                if (picURL != null && !picURL.isEmpty()) {
                    map.put(R.id.comment_reply_item_item_image_view, temp.getAvatar());
                } else {
                    map.put(R.id.comment_reply_item_item_image_view, R.drawable.user_head_default);
                }
                if (userName != null && !userName.isEmpty()) {
                    map.put(R.id.comment_reply_item_item_user_type, temp.getUserName());
                } else {
                    map.put(R.id.comment_reply_item_item_user_type, "暂无");
                }
                if (time != null && !time.isEmpty()) {
                    map.put(R.id.comment_reply_item_item_time, time + "(回复)");
                } else {
                    map.put(R.id.comment_reply_item_item_time, "暂无");
                }
                if (info != null && !info.isEmpty()) {
                    map.put(R.id.comment_reply_item_item_content_text_view, info);
                } else {
                    map.put(R.id.comment_reply_item_item_content_text_view, "暂无");
                }
            }
            ItemAdapter adapter = new ItemAdapter(this, R.layout.comment_reply_item, listItems);
            listView.setAdapter(adapter);
        } else {
            findViewById(R.id.comment_reply_content_list_relative_layout).setVisibility(View.GONE);
            findViewById(R.id.comment_reply_content_linear_layout_gone).setVisibility(View.VISIBLE);
            findViewById(R.id.comment_reply_content_linear_layout).setVisibility(View.GONE);
        }

    }


    public JsonResult<CommentReplyJsonModel> getReply() {
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("cityid", Global.NOW_CITY_ID);
        map.put("cid", cid);
        map.put("hid", hid);

        Optional<JsonResult<CommentReplyJsonModel>> optional = httpClientUtils.getUnsignedByData(REPLY_LIST_URL, map, CommentReplyJsonModel.class);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public void startTask() {
        ReplyAsyncTask replyAsyncTask = new ReplyAsyncTask();
        replyAsyncTask.execute();
    }

    public Map<String, String> sendComment(Map<String, String> param) {
        String info = param.get("info");
        Map<String, String> resultMap = new HashMap<String, String>();
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("hid", hid);
        map.put("info", info);
        map.put("cid", cid);
        map.put("cityid", Global.NOW_CITY_ID);
        if (!Global.USER_AUTH_STR.equals("")) {
            map.put("authstr", Global.USER_AUTH_STR);
        }
        Optional<Map<String, String>> optional = httpClientUtils.postSignedMap(COMMENT_URL, map, String.class);
        if (optional.isPresent()) {
            resultMap.put("status", optional.get().get("status"));
            resultMap.put("message", optional.get().get("message"));
            return resultMap;
        }
        return null;
    }

    public class ReplyAsyncTask extends AsyncTask<String, Void, JsonResult<CommentReplyJsonModel>> {

        @Override
        protected JsonResult<CommentReplyJsonModel> doInBackground(String... params) {
            return getReply();
        }

        @Override
        protected void onPostExecute(JsonResult<CommentReplyJsonModel> jsonResult) {
            if (jsonResult != null && jsonResult.isStatus()) {
                findViewById(R.id.comment_reply_wait_load_relative).setVisibility(View.GONE);
                findViewById(R.id.comment_reply_relative_layout).setVisibility(View.VISIBLE);
                setCommentReply(jsonResult.getData());
            } else if (jsonResult == null) {

            }
        }
    }

    public class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.details_back:
                    finish();
                    break;
            }
        }
    }

    public class CommentAsyncTask extends AsyncTask<Map<String, String>, Void, Map<String, String>> {

        @Override
        protected Map<String, String> doInBackground(Map<String, String>... params) {
            return sendComment(params[0]);
        }

        @Override
        protected void onPostExecute(Map<String, String> resultMap) {
            if (resultMap != null) {
                Toast toast = Toast.makeText(getApplicationContext(), resultMap.get("message"), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                EditText editText = (EditText) findViewById(R.id.comment_reply_edit_text);
                editText.setText("");
                editText.setHint(getString(R.string.message_body_hint));

            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "网络数据获取失败", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }
}
