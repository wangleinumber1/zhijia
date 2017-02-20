package com.zhijia.ui.zhijiaActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.zhijia.ui.R;

/**
 * 税计算器
 */
public class CalculatorTaxActivity extends CommonActivity {

    private EditText areaEditText, priceEditText;

    private ClickListener clickListener = new ClickListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_tax);

        areaEditText = (EditText) findViewById(R.id.area);
        priceEditText = (EditText) findViewById(R.id.price);

        this.setTopTitle(getString(R.string.calculator_tax), R.id.title);

        this.setOnClickListener(this);
        findViewById(R.id.reset).setOnClickListener(clickListener);
        findViewById(R.id.compute).setOnClickListener(clickListener);
    }

    //点击事件的公共类
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.reset: //重置
                    priceEditText.setText("");
                    areaEditText.setText("");
                    break;
                case R.id.compute://计算
                    if (priceEditText.getText().toString().trim().length() == 0 || areaEditText.getText().toString().trim().length() == 0) {
                        Toast.makeText(getApplicationContext(), "填写数据不完整", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent calculatorTaxResultIntent = new Intent(getApplicationContext(), CalculatorTaxResultActivity.class);
                        calculatorTaxResultIntent.putExtra("price", Double.parseDouble(priceEditText.getText().toString().trim()));
                        calculatorTaxResultIntent.putExtra("area", Double.parseDouble(areaEditText.getText().toString().trim()));
                        startActivity(calculatorTaxResultIntent);
                    }

                    break;
            }
        }
    }
}
