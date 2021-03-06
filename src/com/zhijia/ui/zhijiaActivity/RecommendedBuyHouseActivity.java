package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.common.base.Optional;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.HouseArea;
import com.zhijia.service.data.Medol.JsonResult;
import com.zhijia.service.network.HttpClientUtils;
import com.zhijia.ui.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推荐买房
 */
public class RecommendedBuyHouseActivity extends Activity {

    private static String AREA_URL = Global.API_WEB_URL + "/xinfang/recommendhousefield";
    private static String RECOMMENDED_URL = Global.API_WEB_URL + "/xinfang/recommendhouse";

    private OnClickListener onClickListener = new OnClickListener();

    private EditText friendNameEditText, friendCellphoneEditText, intentionHouseEditText, intentionDescriptionEditText;

    private TextView selectAreaTextView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommended_buy_house);

        friendNameEditText = (EditText) findViewById(R.id.input_friend_name);
        friendCellphoneEditText = (EditText) findViewById(R.id.input_friend_cellphone);
        intentionHouseEditText = (EditText) findViewById(R.id.input_intention_house);
        intentionDescriptionEditText = (EditText) findViewById(R.id.intention_description);
        selectAreaTextView = (TextView) findViewById(R.id.select_area);

        findViewById(R.id.back).setOnClickListener(onClickListener);
        findViewById(R.id.recommended_rules).setOnClickListener(onClickListener);
        findViewById(R.id.immediately_recommended).setOnClickListener(onClickListener);

        GetHouseAreaAsyncTask getHouseAreaAsyncTask = new GetHouseAreaAsyncTask();
        getHouseAreaAsyncTask.execute();
    }

    private Map<String, Object> getHouseArea() {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        if (!Global.USER_AUTH_STR.equals("")) {
            HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
            Map<String, String> map = new HashMap<String, String>();
            map.put("cityid", Global.NOW_CITY_ID);
            map.put("authstr", Global.USER_AUTH_STR);
            Optional<JsonResult<HouseArea>> optional = httpClientUtils.getUnsignedByData(AREA_URL, map, HouseArea.class);
            if (optional.isPresent()) {
                resultMap.put("status", String.valueOf(optional.get().isStatus()));
                resultMap.put("message", optional.get().getMessage());
                if (optional.get().getData() != null) {
                    resultMap.put("area", optional.get().getData().getPlace());
                }
            }
        }

        return resultMap;
    }

    private Map<String, String> postRecommended() {
        String friendName = friendNameEditText.getText().toString();
        String friendCellphone = friendCellphoneEditText.getText().toString();
        String intentionHouse = intentionHouseEditText.getText().toString();
        String intentionDescription = intentionDescriptionEditText.getText().toString();
        String selectArea = selectAreaTextView.getText().toString();

        Map<String, String> resultMap = new HashMap<String, String>();
        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("cityid", Global.NOW_CITY_ID);
        map.put("authstr", Global.USER_AUTH_STR);
        map.put("truename", friendName);
        map.put("mobile", friendCellphone);
        map.put("place", selectArea);
        map.put("house", intentionHouse);
        map.put("info", intentionDescription);
        Optional<Map<String, String>> optional = httpClientUtils.postSignedMap(RECOMMENDED_URL, map, String.class);
        if (optional.isPresent()) {
            resultMap.put("status", optional.get().get("status"));
            resultMap.put("message", optional.get().get("message"));
        }

        return resultMap;
    }

    public class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.recommended_rules:
                    Intent earnCommissionIntent = new Intent(view.getContext(), EarnCommissionActivity.class);
                    startActivity(earnCommissionIntent);
                    break;
                case R.id.immediately_recommended:
                    if (friendNameEditText.getText().toString().trim().length() == 0 || friendCellphoneEditText.getText().toString().trim().length() == 0
                            || intentionHouseEditText.getText().toString().trim().length() == 0 || intentionDescriptionEditText.getText().toString().trim().length() == 0
                            || selectAreaTextView.getText().toString().equalsIgnoreCase("请选择 >")) {
                        Toast.makeText(getApplicationContext(), "填写数据不完整", Toast.LENGTH_SHORT).show();
                    } else {
                        RecommendedAsyncTask recommendedAsyncTask = new RecommendedAsyncTask();
                        recommendedAsyncTask.execute();
                        findViewById(R.id.immediately_recommended).setClickable(false);
                    }
                    break;
            }
        }
    }

    class GetHouseAreaAsyncTask extends AsyncTask<Void, Void, Map<String, Object>> {
        @Override
        protected Map<String, Object> doInBackground(Void... params) {
            return getHouseArea();
        }

        @Override
        protected void onPostExecute(Map<String, Object> resultMap) {
            super.onPostExecute(resultMap);

            if (resultMap.size() > 0 && ((String) resultMap.get("status")).equalsIgnoreCase("true")) {//成功
                List<HouseArea.Place> placeList = (List<HouseArea.Place>) resultMap.get("area");
                final String[] areas = new String[placeList.size()];
                for (int i = 0; i < placeList.size(); i++) {
                    areas[i] = placeList.get(i).getName();
                }
                RecommendedBuyHouseActivity.this.findViewById(R.id.select_area).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RecommendedBuyHouseActivity.this);
                        builder.setTitle(getString(R.string.please_select));
                        builder.setItems(areas, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                selectAreaTextView.setText(areas[which]);
                            }
                        });

                        builder.create().show();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), (String) resultMap.get("message"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class RecommendedAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {
        @Override
        protected Map<String, String> doInBackground(Void... params) {
            return postRecommended();
        }

        @Override
        protected void onPostExecute(Map<String, String> resultMap) {
            super.onPostExecute(resultMap);

            if (resultMap.size() > 0 && resultMap.get("status").equalsIgnoreCase("true")) {//登录成功
                Toast.makeText(getApplicationContext(), resultMap.get("message"), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                findViewById(R.id.immediately_recommended).setClickable(true);
                Toast.makeText(getApplicationContext(), resultMap.get("message"), Toast.LENGTH_SHORT).show();
            }
        }
    }
}