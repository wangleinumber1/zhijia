package com.zhijia.ui.zhijiaActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.zhijia.service.data.Medol.CalculatorAdapterModel;
import com.zhijia.ui.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计算器 --主页面
 */
public class CalculatorActivity extends CommonActivity {

    private final String LOG_TAG = "CalculatorActivity" ;

    private final String ADAPTER_CALCULATOR_IMAGE = "adapter_calculator_image";

    private final String ADAPTER_CALCULATOR_TEXT_ONE = "adapter_calculator_text_one";

    private final String ADAPTER_CALCULATOR_TEXT_TWO = "adapter_calculator_text_two";

    private final String[] ADAPTER_FROM = {ADAPTER_CALCULATOR_IMAGE, ADAPTER_CALCULATOR_TEXT_ONE, ADAPTER_CALCULATOR_TEXT_TWO};

    private final int[] ADAPTER_TO = {R.id.calculator_image, R.id.calculator_text_one, R.id.calculator_text_two};

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);
        this.setOnClickListener(this);

        List<CalculatorAdapterModel> calculatorList = new ArrayList<CalculatorAdapterModel>();

        CalculatorAdapterModel modelOne = new CalculatorAdapterModel();
        modelOne.setImageId(R.drawable.calculator_icons_1);
        modelOne.setTextOne("购房能力评估");
        modelOne.setTextTwo("(根据收入、支持等计算购房能力)");

        CalculatorAdapterModel modelTwo = new CalculatorAdapterModel();
        modelTwo.setImageId(R.drawable.calculator_icons_2);
        modelTwo.setTextOne("贷款计算器");
        modelTwo.setTextTwo("(商业贷款、公积金、组合计算器)");

        CalculatorAdapterModel modelThree = new CalculatorAdapterModel();
        modelThree.setImageId(R.drawable.calculator_icons_3);
        modelThree.setTextOne("公积金贷款计算器");
        modelThree.setTextTwo("(最新的个人公积金贷款计算公式)");

        CalculatorAdapterModel modelFour = new CalculatorAdapterModel();
        modelFour.setImageId(R.drawable.calculator_icons_4);
        modelFour.setTextOne("提前还款计算器");
        modelFour.setTextTwo("(提前还款是否划算，来算一算)");

        CalculatorAdapterModel modelFive = new CalculatorAdapterModel();
        modelFive.setImageId(R.drawable.calculator_icons_5);
        modelFive.setTextOne("税费计算器");
        modelFive.setTextTwo("(根据面积、单价计算税费   )");

        calculatorList.add(modelOne);
        calculatorList.add(modelTwo);
        calculatorList.add(modelThree);
        calculatorList.add(modelFour);
        calculatorList.add(modelFive);
        this.setAdapterLiseView(calculatorList);

    }

    private void setAdapterLiseView(List<CalculatorAdapterModel> calculatorList) {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

        for (CalculatorAdapterModel temp : calculatorList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(ADAPTER_CALCULATOR_IMAGE, temp.getImageId());
            map.put(ADAPTER_CALCULATOR_TEXT_ONE, temp.getTextOne());
            map.put(ADAPTER_CALCULATOR_TEXT_TWO, temp.getTextTwo());
            listItems.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.adapter_calculator_item, ADAPTER_FROM, ADAPTER_TO);
        ListView listView = (ListView) findViewById(R.id.calculator_listView);
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG,"position==="+position) ;
                switch (position){
                    case 0:
                        Intent intentCapacity = new Intent(getApplicationContext(),CalculatorCapacityActivity.class) ;
                        startActivity(intentCapacity);
                        break;
                    case 1:
                        Intent intentLoan = new Intent(getApplicationContext(),CalculatorLoanActivity.class) ;
                        startActivity(intentLoan);
                        break;
                    case 2:
                        Intent intentFounds = new Intent(getApplicationContext(),CalculatorFundsActivity.class) ;
                        startActivity(intentFounds);
                        break;
                    case 3:
                        Intent intentBeforeLoan = new Intent(getApplicationContext(),CalculatorBeforeLoanActivity.class);
                        startActivity(intentBeforeLoan);
                        break;
                    case 4:
                        Intent intentTax = new Intent(getApplicationContext(),CalculatorTaxActivity.class) ;
                        startActivity(intentTax);
                        break;
                }
            }
        });
    }
}
