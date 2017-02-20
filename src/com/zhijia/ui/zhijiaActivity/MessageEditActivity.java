package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.data.Medol.UserModel;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 写消息
 */
public class MessageEditActivity extends Activity {

    private static String FIND_USER_URL = Global.API_WEB_URL + "/message/user";

    private static String SEND_MESSAGE_URL = Global.API_WEB_URL + "/message/send";

    //点击事件侦听
    private ClickListener clickListener = new ClickListener();

    private EditText messageReceiver, messageBody;

    private ListView selectUserResultListView;

    private boolean isSetText = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_edit);

        messageReceiver = (EditText) findViewById(R.id.message_receiver);
        messageBody = (EditText) findViewById(R.id.message_body);
        selectUserResultListView = (ListView) findViewById(R.id.select_user_result_list);

        this.findViewById(R.id.back).setOnClickListener(clickListener);
        this.findViewById(R.id.send_message).setOnClickListener(clickListener);

        messageReceiver.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (isSetText || charSequence.length() == 0) {
                    selectUserResultListView.setVisibility(View.GONE);
                } else {
                    GetUserListAsyncTask getUserListAsyncTask = new GetUserListAsyncTask();
                    getUserListAsyncTask.execute(charSequence.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private Map<String, String> sendMessage() {
        Map<String, String> resultMap = new HashMap<String, String>();
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("authstr", Global.USER_AUTH_STR);
        map.put("info", messageBody.getText().toString());
        map.put("touser", messageReceiver.getText().toString());

        Optional<Map<String, String>> optional = httpClientUtils.postSignedMap(SEND_MESSAGE_URL, map, String.class);
        if (optional.isPresent()) {
            resultMap.put("status", optional.get().get("status"));
            resultMap.put("message", optional.get().get("message"));
        }

        return resultMap;
    }

    /**
     * 获得信息
     *
     * @return
     */
    private Map<String, Object> findUserInfo(String keyword) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        if (!Global.USER_AUTH_STR.equals("")) {
            HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
            Map<String, String> map = new HashMap<String, String>();
            map.put("authstr", Global.USER_AUTH_STR);
            map.put("cityid", Global.NOW_CITY_ID);
            map.put("key", keyword);
            Optional<JsonResult<List<UserModel>>> optional = httpClientUtils.postUnsignedListByData(FIND_USER_URL, map, UserModel.class);
            if (optional.isPresent()) {
                resultMap.put("status", String.valueOf(optional.get().isStatus()));
                resultMap.put("message", optional.get().getMessage());
                if (optional.get().getData() != null) {
                    resultMap.put("userList", optional.get().getData());
                }
            }
        }

        return resultMap;
    }

    //点击事件的公共类
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back: //后退
                    finish();
                    break;
                case R.id.send_message: //发送
                    if (messageReceiver.getText().length() == 0) {
                        Toast.makeText(MessageEditActivity.this.getBaseContext(), R.string.message_receiver_hint, Toast.LENGTH_SHORT).show();
                        messageReceiver.requestFocus();
                    } else if (messageBody.getText().length() < 2) {
                        Toast.makeText(MessageEditActivity.this.getBaseContext(), R.string.less_than_message_min_length, Toast.LENGTH_SHORT).show();
                        messageBody.requestFocus();
                    } else {
                        SendMessageAsyncTask sendMessageAsyncTask = new SendMessageAsyncTask();
                        sendMessageAsyncTask.execute();
                        findViewById(R.id.send_message).setClickable(false);
                    }
                    break;
            }
        }
    }

    class SendMessageAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {
        @Override
        protected Map<String, String> doInBackground(Void... params) {
            return sendMessage();
        }

        @Override
        protected void onPostExecute(Map<String, String> resultMap) {
            super.onPostExecute(resultMap);

            if (resultMap.size() > 0 && resultMap.get("status").equalsIgnoreCase("true")) {//成功
                Toast.makeText(MessageEditActivity.this.getBaseContext(), R.string.message_sent, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                findViewById(R.id.send_message).setClickable(true);
                Toast.makeText(getApplicationContext(), resultMap.get("message"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class GetUserListAsyncTask extends AsyncTask<String, Void, Map<String, Object>> {
        @Override
        protected Map<String, Object> doInBackground(String... params) {
            return findUserInfo(params[0]);
        }

        @Override
        protected void onPostExecute(Map<String, Object> resultMap) {
            super.onPostExecute(resultMap);

            if (resultMap.size() > 0 && resultMap.get("status").equals("true")) {//成功
                final List<UserModel> resultList = (List<UserModel>) resultMap.get("userList");

                if (resultList.size() > 0) {
                    selectUserResultListView.setAdapter(new AllUserListAdapter(MessageEditActivity.this, resultList));
                    selectUserResultListView.setVisibility(View.VISIBLE);
                    selectUserResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            isSetText = true;
                            messageReceiver.setText(resultList.get(position).getUsername());
                            //主要是防止再次提示
                            isSetText = false;
                        }
                    });
                } else {
                    selectUserResultListView.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(getApplicationContext(), (String) resultMap.get("message"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 全部用户的Adapter
     */
    private class AllUserListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<UserModel> list;

        public AllUserListAdapter(Context context, List<UserModel> list) {
            this.inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.adapter_all_user_item, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.user_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(list.get(position).getUsername());

            return convertView;
        }

        private class ViewHolder {
            TextView name;
        }

    }
}
