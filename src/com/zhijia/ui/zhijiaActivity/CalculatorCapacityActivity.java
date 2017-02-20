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
 * 计算购房能力
 */
public class CalculatorCapacityActivity extends CommonActivity {

    private final String[] mItems = {"2年(24期)", "3年(36期)", "4年(48期)", "5年(60期)", "6年(72期)", "7年(84期)", "8年(96期)", "9年(108期)", "10年(120期)", "15年(180期)", "20年(240期)", "25年(300期)", "30年(360期)"};
    private final int[] values = {24, 36, 48, 60, 72, 84, 96, 108, 120, 180, 240, 300, 360};
    private int years = values[0];
    private EditText buyHouseMoneyEditText, salaryEditText, payMonthEditText, buyAreaEditText;
    private TextView yearsTextView;
    private ClickListener clickListener = new ClickListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_capacity);
        this.setTopTitle(getString(R.string.calculator_capacity), R.id.title);

        buyHouseMoneyEditText = (EditText) findViewById(R.id.buy_house_money);
        salaryEditText = (EditText) findViewById(R.id.salary);
        payMonthEditText = (EditText) findViewById(R.id.pay_month);
        buyAreaEditText = (EditText) findViewById(R.id.buy_area);
        yearsTextView = (TextView) findViewById(R.id.years);
        yearsTextView.setText(mItems[0]);

        this.setOnClickListener(this);
        findViewById(R.id.years).setOnClickListener(clickListener);
        findViewById(R.id.reset).setOnClickListener(clickListener);
        findViewById(R.id.compute).setOnClickListener(clickListener);
    }

    //点击事件的公共类
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.reset: //重置
                    buyHouseMoneyEditText.setText("");
                    salaryEditText.setText("");
                    payMonthEditText.setText("");
                    buyAreaEditText.setText("");
                    yearsTextView.setText(mItems[0]);
                    years = values[0];
                    break;
                case R.id.compute://计算
                    if (buyHouseMoneyEditText.getText().toString().trim().length() == 0 || salaryEditText.getText().toString().trim().length() == 0 || payMonthEditText.getText().toString().trim().length() == 0 || buyAreaEditText.getText().toString().trim().length() == 0) {
                        Toast.makeText(getApplicationContext(), "填写数据不完整", Toast.LENGTH_SHORT).show();
                    } else if (Double.parseDouble(buyHouseMoneyEditText.getText().toString()) < 4.7) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CalculatorCapacityActivity.this);
                        builder.setMessage("您确定是" + buyHouseMoneyEditText.getText().toString() + "万元?\n那么您目前尚不具备购房能力，建议积攒积蓄或能筹集更多的资金。");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    } else if (Double.parseDouble(buyHouseMoneyEditText.getText().toString()) > 10000) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CalculatorCapacityActivity.this);
                        builder.setMessage("您确定拥有超过一亿元的购房资金？");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    } else if (Double.parseDouble(payMonthEditText.getText().toString()) > Double.parseDouble(salaryEditText.getText().toString()) * 0.7) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CalculatorCapacityActivity.this);
                        builder.setMessage("您预计家庭每月可用于购房支出已超过家庭月收入的70%，是否确定不会影响您的正常生活消费？\n建议在40%（" + Double.parseDouble(salaryEditText.getText().toString()) * 0.4 + "元）左右。");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    } else {
                        Intent calculatorTaxResultIntent = new Intent(getApplicationContext(), CalculatorCapacityResultActivity.class);
                        calculatorTaxResultIntent.putExtra("buy_house_money", Double.parseDouble(buyHouseMoneyEditText.getText().toString().trim()));
                        calculatorTaxResultIntent.putExtra("salary", Double.parseDouble(salaryEditText.getText().toString().trim()));
                        calculatorTaxResultIntent.putExtra("pay_month", Double.parseDouble(payMonthEditText.getText().toString().trim()));
                        calculatorTaxResultIntent.putExtra("buy_area", Double.parseDouble(buyAreaEditText.getText().toString().trim()));
                        calculatorTaxResultIntent.putExtra("years", years);
                        startActivity(calculatorTaxResultIntent);
                    }

                    break;
                case R.id.years://按揭年数
                    AlertDialog.Builder builder = new AlertDialog.Builder(CalculatorCapacityActivity.this);
                    builder.setTitle(getString(R.string.please_select));
                    builder.setItems(mItems, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            years = values[which];
                            yearsTextView.setText(mItems[which]);
                        }
                    });

                    builder.create().show();
                    break;
            }
        }
    }
}
