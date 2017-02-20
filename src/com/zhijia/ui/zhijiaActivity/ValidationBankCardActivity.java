package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证银行卡
 */
public class ValidationBankCardActivity extends Activity {

    private static String GET_BANK_URL = Global.API_WEB_URL + "/member/bankfield";

    private static String VERIFY_BANK_URL = Global.API_WEB_URL + "/member/verifybank";

    //点击事件的侦听
    private ClickListener clickListener = new ClickListener();

    private EditText bankAccountNumberEditText;

    private TextView bankTextView;

    private ListView selectBankResultListView;

    private String bank = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.validation_bank_card);

        bankTextView = (TextView) findViewById(R.id.bank);
        bankAccountNumberEditText = (EditText) findViewById(R.id.bank_account_number);
        selectBankResultListView = (ListView) findViewById(R.id.select_bank_result_list);

        findViewById(R.id.back).setOnClickListener(clickListener);
        findViewById(R.id.submit).setOnClickListener(clickListener);

        bankTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetBankListAsyncTask getBankListAsyncTask = new GetBankListAsyncTask();
                getBankListAsyncTask.execute();
                bankTextView.setClickable(false);
            }
        });
    }

    /**
     * 获得信息
     *
     * @return
     */
    private Map<String, Object> getBankInfo() {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        if (!Global.USER_AUTH_STR.equals("")) {
            HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
            Map<String, String> map = new HashMap<String, String>();
            map.put("authstr", Global.USER_AUTH_STR);
            map.put("cityid", Global.NOW_CITY_ID);
            Optional<JsonResult<List<String>>> optional = httpClientUtils.getUnsignedListByData(GET_BANK_URL, map, String.class);
            if (optional.isPresent()) {
                resultMap.put("status", String.valueOf(optional.get().isStatus()));
                resultMap.put("message", optional.get().getMessage());
                if (optional.get().getData() != null) {
                    resultMap.put("bankList", optional.get().getData());
                }
            }
        }

        return resultMap;
    }

    private Map<String, String> sendMessage() {
        Map<String, String> resultMap = new HashMap<String, String>();
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("authstr", Global.USER_AUTH_STR);
        map.put("cityid", Global.NOW_CITY_ID);
        map.put("bankname", bank);
        map.put("bankcard", bankAccountNumberEditText.getText().toString());

        Optional<Map<String, String>> optional = httpClientUtils.postSignedMap(VERIFY_BANK_URL, map, String.class);
        if (optional.isPresent()) {
            resultMap.put("status", optional.get().get("status"));
            resultMap.put("message", optional.get().get("message"));
        }

        return resultMap;
    }

    //点击事件的公共类
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.back: //后退
                    finish();
                    break;
                case R.id.submit: //提交
                    if (bank == null) {
                        Toast.makeText(getApplicationContext(), R.string.select_bank_hint, Toast.LENGTH_SHORT).show();
                    } else if (bankAccountNumberEditText.getText().toString().trim().length() == 0) {
                        Toast.makeText(getApplicationContext(), R.string.input_bank_account_number_hint, Toast.LENGTH_SHORT).show();
                        bankAccountNumberEditText.requestFocus();
                    } else {
                        SendMessageAsyncTask sendMessageAsyncTask = new SendMessageAsyncTask();
                        sendMessageAsyncTask.execute();
                        findViewById(R.id.submit).setClickable(false);
                    }
                    break;
            }
        }
    }

    class GetBankListAsyncTask extends AsyncTask<Void, Void, Map<String, Object>> {
        @Override
        protected Map<String, Object> doInBackground(Void... params) {
            return getBankInfo();
        }

        @Override
        protected void onPostExecute(Map<String, Object> resultMap) {
            super.onPostExecute(resultMap);

            if (resultMap.size() > 0 && resultMap.get("status").equals("true")) {//成功
                final List<String> resultList = (List<String>) resultMap.get("bankList");

                if (resultList.size() > 0) {
                    selectBankResultListView.setAdapter(new AllBankListAdapter(ValidationBankCardActivity.this, resultList));
                    selectBankResultListView.setVisibility(View.VISIBLE);
                    selectBankResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            bankTextView.setText(resultList.get(position));
                            bank = resultList.get(position);
                            selectBankResultListView.setVisibility(View.GONE);
                        }
                    });
                } else {
                    selectBankResultListView.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(getApplicationContext(), (String) resultMap.get("message"), Toast.LENGTH_SHORT).show();
            }

            bankTextView.setClickable(true);
        }
    }

    /**
     * 全部银行的Adapter
     */
    private class AllBankListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<String> list;

        public AllBankListAdapter(Context context, List<String> list) {
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

            holder.name.setText(list.get(position));

            return convertView;
        }

        private class ViewHolder {
            TextView name;
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
                Toast.makeText(getApplicationContext(), resultMap.get("message"), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                findViewById(R.id.submit).setClickable(true);
                Toast.makeText(getApplicationContext(), resultMap.get("message"), Toast.LENGTH_SHORT).show();
            }
        }
    }
}