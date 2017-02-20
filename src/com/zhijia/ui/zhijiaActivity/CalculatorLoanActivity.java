package com.zhijia.ui.zhijiaActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.zhijia.ui.R;

/**
 * 贷款计算器
 */
public class CalculatorLoanActivity extends CommonActivity {

    private final String[] mItems = {"1年（12期）", "2年（24期）", "3年（36期）", "4年（48期）", "5年（60期）", "6年（72期）", "7年（84期）", "8年（96期）", "9年（108期）", "10年（120期）", "11年（132期）", "12年（124期）", "13年（156期）", "14年（168期）", "15年（180期）", "16年（192期）", "17年（204期）", "18年（216期）", "19年（228期）", "20年（240期）", "25年（300期）", "30年（360期）"};
    private final int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
    private int years = values[0];
    private final String[] lItems = {"12年7月6日利率上限(1.1倍)", " 12年7月6日利率下限(85折)", " 12年7月6日利率下限(7折)", "12年7月6日基准利率", "12年6月8日利率上限(1.1倍)", "12年6月8日利率下限(85折)", "12年6月8日利率下限(7折)", "12年6月8日基准利率", "11年7月6日利率上限(1.1倍)", "11年7月6日利率下限(85折)", "11年7月6日利率下限(7折)", " 11年7月6日基准利率", " 11年4月5日利率上限(1.1倍)", "11年4月5日利率下限(85折)"};
    private final int[] l_values = {31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18};
    private int lilv = l_values[3];
    private int percent = l_values[2];
    private final String[] pItems = {"等额本息", "等额本金"};
    private final int[] p_values = {0, 1};
    private int payLoanType = p_values[0];
    private final String[] lpItems = {"9成", "8成", "7成", "6成", "5成", "4成", "3成", "2成",};
    private final int[] lp_values = {9, 8, 7, 6, 5, 4, 3, 2};
    private ClickListener clickListener = new ClickListener();
    private TextView type_combination, type_accumulation_fund,
            type_business, by_area, by_total_money, loan_total_years, loan_rate, pay_type, loan_percent;

    private EditText fund_money, business_money, house_single_price, area, loan_total_price;
    private LinearLayout by_total_money_layout_1, combination_layout_1, combination_layout_2,
            by_area_layout_1, by_area_layout_2, by_area_layout_3, calculator_type_layout;
    private ImageView by_total_money_split_line_1, combination_split_line_1, combination_split_line_2,
            by_area_split_line_1, by_area_split_line_2, by_area_split_line_3;

    private int loanType = 0, loanByType = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_loan);

        type_combination = (TextView) findViewById(R.id.type_combination);
        type_accumulation_fund = (TextView) findViewById(R.id.type_accumulation_fund);
        type_business = (TextView) findViewById(R.id.type_business);
        by_area = (TextView) findViewById(R.id.by_area);
        by_total_money = (TextView) findViewById(R.id.by_total_money);
        loan_total_years = (TextView) findViewById(R.id.loan_total_years);
        loan_rate = (TextView) findViewById(R.id.loan_rate);
        pay_type = (TextView) findViewById(R.id.pay_type);
        loan_percent = (TextView) findViewById(R.id.loan_percent);
        fund_money = (EditText) findViewById(R.id.fund_money);
        business_money = (EditText) findViewById(R.id.business_money);
        house_single_price = (EditText) findViewById(R.id.house_single_price);
        loan_total_price = (EditText) findViewById(R.id.loan_total_price);
        area = (EditText) findViewById(R.id.area);
        calculator_type_layout = (LinearLayout) findViewById(R.id.calculator_type_layout);
        by_total_money_layout_1 = (LinearLayout) findViewById(R.id.by_total_money_layout_1);
        combination_layout_1 = (LinearLayout) findViewById(R.id.combination_layout_1);
        combination_layout_2 = (LinearLayout) findViewById(R.id.combination_layout_2);
        by_area_layout_1 = (LinearLayout) findViewById(R.id.by_area_layout_1);
        by_area_layout_2 = (LinearLayout) findViewById(R.id.by_area_layout_2);
        by_area_layout_3 = (LinearLayout) findViewById(R.id.by_area_layout_3);
        by_total_money_split_line_1 = (ImageView) findViewById(R.id.by_total_money_split_line_1);
        combination_split_line_1 = (ImageView) findViewById(R.id.combination_split_line_1);
        combination_split_line_2 = (ImageView) findViewById(R.id.combination_split_line_2);
        by_area_split_line_1 = (ImageView) findViewById(R.id.by_area_split_line_1);
        by_area_split_line_2 = (ImageView) findViewById(R.id.by_area_split_line_2);
        by_area_split_line_3 = (ImageView) findViewById(R.id.by_area_split_line_3);

        this.setTopTitle(getString(R.string.calculator_loan), R.id.title);

        initUI();

        this.setOnClickListener(this);
        findViewById(R.id.reset).setOnClickListener(clickListener);
        findViewById(R.id.compute).setOnClickListener(clickListener);
        type_business.setOnClickListener(clickListener);
        type_accumulation_fund.setOnClickListener(clickListener);
        type_combination.setOnClickListener(clickListener);
        by_area.setOnClickListener(clickListener);
        by_total_money.setOnClickListener(clickListener);
        loan_total_years.setOnClickListener(clickListener);
        loan_rate.setOnClickListener(clickListener);
        loan_percent.setOnClickListener(clickListener);
        pay_type.setOnClickListener(clickListener);
    }

    private void initUI() {
        fund_money.setText("");
        business_money.setText("");
        house_single_price.setText("");
        area.setText("");
        loan_total_price.setText("");
        loan_total_years.setText(mItems[0]);
        years = values[0];
        loan_rate.setText(lItems[3]);
        lilv = l_values[3];
        pay_type.setText(pItems[0]);
        payLoanType = p_values[0];
        loan_percent.setText(lpItems[2]);
        percent = lp_values[2];
        changeType(loanType, loanByType);
    }

    /**
     * 改变ui展现
     *
     * @param loanType   贷款方式
     * @param loanByType 计算方式
     */
    private void changeType(int loanType, int loanByType) {
        if (loanType == 0 || loanType == 1) {
            calculator_type_layout.setVisibility(View.VISIBLE);
            if (loanType == 0) {
                type_business.setTextColor(getResources().getColor(R.color.red));
                type_accumulation_fund.setTextColor(getResources().getColor(R.color.black));
                type_combination.setTextColor(getResources().getColor(R.color.black));
            } else {
                type_business.setTextColor(getResources().getColor(R.color.black));
                type_accumulation_fund.setTextColor(getResources().getColor(R.color.red));
                type_combination.setTextColor(getResources().getColor(R.color.black));
            }

            combination_layout_1.setVisibility(View.GONE);
            combination_split_line_1.setVisibility(View.GONE);
            combination_layout_2.setVisibility(View.GONE);
            combination_split_line_2.setVisibility(View.GONE);

            if (loanByType == 0) {//按面积
                calculator_type_layout.setBackgroundResource(R.drawable.calculator_before_loan_one);
                by_area.setTextColor(getResources().getColor(R.color.white));
                by_total_money.setTextColor(getResources().getColor(R.color.red));
                by_area_layout_1.setVisibility(View.VISIBLE);
                by_area_split_line_1.setVisibility(View.VISIBLE);
                by_area_layout_2.setVisibility(View.VISIBLE);
                by_area_split_line_2.setVisibility(View.VISIBLE);
                by_area_layout_3.setVisibility(View.VISIBLE);
                by_area_split_line_3.setVisibility(View.VISIBLE);
                by_total_money_layout_1.setVisibility(View.GONE);
                by_total_money_split_line_1.setVisibility(View.GONE);
            } else {
                calculator_type_layout.setBackgroundResource(R.drawable.calculator_before_loan_two);
                by_area.setTextColor(getResources().getColor(R.color.red));
                by_total_money.setTextColor(getResources().getColor(R.color.white));
                by_area_layout_1.setVisibility(View.GONE);
                by_area_split_line_1.setVisibility(View.GONE);
                by_area_layout_2.setVisibility(View.GONE);
                by_area_split_line_2.setVisibility(View.GONE);
                by_area_layout_3.setVisibility(View.GONE);
                by_area_split_line_3.setVisibility(View.GONE);
                by_total_money_layout_1.setVisibility(View.VISIBLE);
                by_total_money_split_line_1.setVisibility(View.VISIBLE);
            }
        } else {
            calculator_type_layout.setVisibility(View.GONE);
            type_business.setTextColor(getResources().getColor(R.color.black));
            type_accumulation_fund.setTextColor(getResources().getColor(R.color.black));
            type_combination.setTextColor(getResources().getColor(R.color.red));

            by_total_money_layout_1.setVisibility(View.GONE);
            by_total_money_split_line_1.setVisibility(View.GONE);
            combination_layout_1.setVisibility(View.VISIBLE);
            combination_split_line_1.setVisibility(View.VISIBLE);
            combination_layout_2.setVisibility(View.VISIBLE);
            combination_split_line_2.setVisibility(View.VISIBLE);
            by_area_layout_1.setVisibility(View.GONE);
            by_area_split_line_1.setVisibility(View.GONE);
            by_area_layout_2.setVisibility(View.GONE);
            by_area_split_line_2.setVisibility(View.GONE);
            by_area_layout_3.setVisibility(View.GONE);
            by_area_split_line_3.setVisibility(View.GONE);
        }
    }

    //点击事件的公共类
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.reset: //重置
                    initUI();
                    break;
                case R.id.type_business:
                    loanType = 0;
                    loanByType = 0;
                    initUI();
                    break;
                case R.id.type_accumulation_fund:
                    loanType = 1;
                    loanByType = 0;
                    initUI();
                    break;
                case R.id.type_combination:
                    loanType = 2;
                    loanByType = 0;
                    initUI();
                    break;
                case R.id.by_area:
                    loanByType = 0;
                    initUI();
                    break;
                case R.id.by_total_money:
                    loanByType = 1;
                    initUI();
                    break;
                case R.id.loan_total_years:
                    AlertDialog.Builder builder = new AlertDialog.Builder(CalculatorLoanActivity.this);
                    builder.setTitle(getString(R.string.please_select));
                    builder.setItems(mItems, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            years = values[which];
                            loan_total_years.setText(mItems[which]);
                        }
                    });

                    builder.create().show();
                    break;
                case R.id.loan_rate:
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(CalculatorLoanActivity.this);
                    builder2.setTitle(getString(R.string.please_select));
                    builder2.setItems(lItems, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            loan_rate.setText(lItems[which]);
                            lilv = l_values[which];
                        }
                    });

                    builder2.create().show();
                    break;
                case R.id.loan_percent:
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(CalculatorLoanActivity.this);
                    builder3.setTitle(getString(R.string.please_select));
                    builder3.setItems(lpItems, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            loan_percent.setText(lpItems[which]);
                            percent = lp_values[which];
                        }
                    });

                    builder3.create().show();
                    break;
                case R.id.pay_type:
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(CalculatorLoanActivity.this);
                    builder4.setTitle(getString(R.string.please_select));
                    builder4.setItems(pItems, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            pay_type.setText(pItems[which]);
                            payLoanType = p_values[which];
                        }
                    });

                    builder4.create().show();
                    break;
                case R.id.compute:
                    if (loanType == 2 && (business_money.getText().toString().trim().length() == 0 || fund_money.getText().toString().trim().length() == 0)) {
                        Toast.makeText(getApplicationContext(), "填写数据不完整", Toast.LENGTH_SHORT).show();
                    } else if ((loanType == 1 || loanType == 0) && loanByType == 0 && (house_single_price.getText().toString().trim().length() == 0 || area.getText().toString().trim().length() == 0)) {
                        Toast.makeText(getApplicationContext(), "填写数据不完整", Toast.LENGTH_SHORT).show();
                    } else if ((loanType == 1 || loanType == 0) && loanByType == 1 && loan_total_price.getText().toString().trim().length() == 0) {
                        Toast.makeText(getApplicationContext(), "填写数据不完整", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent calculatorLoanResultIntent = new Intent(getApplicationContext(), CalculatorLoanResultActivity.class);
                        calculatorLoanResultIntent.putExtra("loanType", loanType);
                        calculatorLoanResultIntent.putExtra("loanByType", loanByType);
                        calculatorLoanResultIntent.putExtra("years", years);
                        calculatorLoanResultIntent.putExtra("lilv", lilv);
                        calculatorLoanResultIntent.putExtra("payLoanType", payLoanType);
                        calculatorLoanResultIntent.putExtra("percent", percent);
                        double singlPrice = 0;
                        if (house_single_price.getText().toString().trim().length() != 0) {
                            singlPrice = Double.parseDouble(house_single_price.getText().toString().trim());
                        }
                        calculatorLoanResultIntent.putExtra("house_single_price", singlPrice);
                        double areaDouble = 0;
                        if (area.getText().toString().trim().length() != 0) {
                            areaDouble = Double.parseDouble(area.getText().toString().trim());
                        }
                        calculatorLoanResultIntent.putExtra("area", areaDouble);
                        double loanTotalPrice = 0;
                        if (loan_total_price.getText().toString().trim().length() != 0) {
                            loanTotalPrice = Double.parseDouble(loan_total_price.getText().toString().trim());
                        }
                        calculatorLoanResultIntent.putExtra("loan_total_price", loanTotalPrice);
                        double businessMoney = 0;
                        if (business_money.getText().toString().trim().length() != 0) {
                            businessMoney = Double.parseDouble(business_money.getText().toString().trim());
                        }
                        calculatorLoanResultIntent.putExtra("business_money", businessMoney);
                        double fundMoney = 0;
                        if (fund_money.getText().toString().trim().length() != 0) {
                            fundMoney = Double.parseDouble(fund_money.getText().toString().trim());
                        }
                        calculatorLoanResultIntent.putExtra("fund_money", fundMoney);

                        startActivity(calculatorLoanResultIntent);
                    }
                    break;
            }
        }
    }
}