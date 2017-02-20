package com.zhijia.ui.zhijiaActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.zhijia.ui.R;

/**
 * 公积金贷款计算器
 */
public class CalculatorFundsActivity extends CommonActivity {

    final String[] mItems = {"等额均还", "等额本金", "自由还款"};
    private ClickListener clickListener = new ClickListener();
    private EditText calculatorFundsLoanPriceEditText, calculatorFundsLoanYearsEditText;
    private TextView calculatorFundsLoanTypeTextView;
    private Integer type = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_funds_loan);

        this.setTopTitle(getString(R.string.calculator_funds_loan), R.id.title);

        calculatorFundsLoanPriceEditText = (EditText) findViewById(R.id.calculator_funds_loan_price);
        calculatorFundsLoanYearsEditText = (EditText) findViewById(R.id.calculator_funds_loan_years);
        calculatorFundsLoanTypeTextView = (TextView) findViewById(R.id.calculator_funds_loan_type);

        this.setOnClickListener(this);
        findViewById(R.id.reset).setOnClickListener(clickListener);
        findViewById(R.id.calculator_funds_loan_type).setOnClickListener(clickListener);
        findViewById(R.id.compute).setOnClickListener(clickListener);
    }

    //点击事件的公共类
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.reset: //重置
                    calculatorFundsLoanPriceEditText.setText("");
                    calculatorFundsLoanYearsEditText.setText("");
                    break;
                case R.id.compute://计算
                    if (calculatorFundsLoanPriceEditText.getText().toString().trim().length() == 0 || calculatorFundsLoanYearsEditText.getText().toString().trim().length() == 0) {
                        Toast.makeText(getApplicationContext(), "填写数据不完整", Toast.LENGTH_SHORT).show();
                    } else if (Integer.parseInt(calculatorFundsLoanYearsEditText.getText().toString()) <= 0 || Integer.parseInt(calculatorFundsLoanYearsEditText.getText().toString()) > 30) {
                        Toast.makeText(getApplicationContext(), "年限只能介于1～30之间", Toast.LENGTH_SHORT).show();
                        calculatorFundsLoanYearsEditText.setFocusable(true);
                    } else {
                        Intent calculatorFundsResultIntent = new Intent(getApplicationContext(), CalculatorFundsResultActivity.class);
                        calculatorFundsResultIntent.putExtra("price", Double.parseDouble(calculatorFundsLoanPriceEditText.getText().toString().trim()));
                        calculatorFundsResultIntent.putExtra("years", Integer.parseInt(calculatorFundsLoanYearsEditText.getText().toString().trim()));
                        calculatorFundsResultIntent.putExtra("type", type);
                        startActivity(calculatorFundsResultIntent);
                    }

                    break;
                case R.id.calculator_funds_loan_type://类型
                    AlertDialog.Builder builder = new AlertDialog.Builder(CalculatorFundsActivity.this);
                    builder.setTitle(getString(R.string.please_select));
                    builder.setItems(mItems, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            type = which;
                            calculatorFundsLoanTypeTextView.setText(mItems[which]);
                        }
                    });

                    builder.create().show();
                    break;
            }
        }
    }
}
