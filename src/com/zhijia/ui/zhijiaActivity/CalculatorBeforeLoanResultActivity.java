package com.zhijia.ui.zhijiaActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.zhijia.Global;
import com.zhijia.ui.R;
import com.zhijia.ui.calculator.HouseBankRate;

/**
 * 提前还贷款计算器结果页
 */
public class CalculatorBeforeLoanResultActivity extends CommonActivity {

    private double loan_total_price, before_loan_price;

    private int loanType, years, lilv, before_loan_method, deal_method_group;

    private TextView pay_month, last_month, already_pay_total_money,
            already_pay_interest_money, this_month_payment, next_month_payment, save_money, new_last_payment_month;

    private String remark = "", first_payment_month, last_payment_month;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_before_loan_result);

        this.setTopTitle(getString(R.string.compute_result), R.id.common_two_view_text);
        this.setOnClickListener(this);

        pay_month = (TextView) findViewById(R.id.pay_month);
        last_month = (TextView) findViewById(R.id.last_month);
        already_pay_total_money = (TextView) findViewById(R.id.already_pay_total_money);
        already_pay_interest_money = (TextView) findViewById(R.id.already_pay_interest_money);
        this_month_payment = (TextView) findViewById(R.id.this_month_payment);
        next_month_payment = (TextView) findViewById(R.id.next_month_payment);
        save_money = (TextView) findViewById(R.id.save_money);
        new_last_payment_month = (TextView) findViewById(R.id.new_last_payment_month);

        years = getIntent().getIntExtra("years", 0);
        loanType = getIntent().getIntExtra("loanType", 0);
        before_loan_method = getIntent().getIntExtra("before_loan_method", R.id.before_loan_method_once_time);
        lilv = getIntent().getIntExtra("lilv", 0);
        deal_method_group = getIntent().getIntExtra("deal_method_group", R.id.deal_method_reduce_years);
        loan_total_price = getIntent().getDoubleExtra("loan_total_price", 0d);
        before_loan_price = getIntent().getDoubleExtra("before_loan_price", 0d);
        first_payment_month = getIntent().getStringExtra("first_payment_month");
        last_payment_month = getIntent().getStringExtra("last_payment_month");

        compute();
    }

    private void compute() {
        int firstYear, firstMonth, beforeYear, beforeMonth;

        String[] split = first_payment_month.split("-");
        firstYear = Integer.parseInt(split[0]);
        firstMonth = Integer.parseInt(split[1]);

        split = last_payment_month.split("-");
        beforeYear = Integer.parseInt(split[0]);
        beforeMonth = Integer.parseInt(split[1]);

        double dkzys = loan_total_price * 10000;

        int s_yhkqs = years;

        //月利率
        double dklv = 0;
        if (loanType == 1) {
            if (s_yhkqs > 60) {
                dklv = HouseBankRate.getBankRate(lilv, 2, 10) / 12; //公积金贷款利率5年以上4.23%
            } else {
                dklv = HouseBankRate.getBankRate(lilv, 2, 3) / 12;  //公积金贷款利率5年(含)以下3.78%
            }
        }
        if (loanType == 0) {
            if (s_yhkqs > 60) {
                dklv = HouseBankRate.getBankRate(lilv, 1, 10) / 12; //商业性贷款利率5年以上5.31%
            } else {
                dklv = HouseBankRate.getBankRate(lilv, 1, 3) / 12; //商业性贷款利率5年(含)以下4.95%
            }
        }

        //已还贷款期数
        int yhdkqs = (beforeYear * 12 + beforeMonth) - (firstYear * 12 + firstMonth);

        if (yhdkqs < 0 || yhdkqs > s_yhkqs) {
            Toast.makeText(getApplicationContext(), "预计提前还款时间(年)与第一次还款时间有矛盾，请查实。", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        double yhk = dkzys * (dklv * Math.pow((1 + dklv), s_yhkqs)) / (Math.pow((1 + dklv), s_yhkqs) - 1);
        String yhkjssj = Math.round(Math.floor((firstYear * 12 + firstMonth + s_yhkqs - 2) / 12)) + "年" + ((firstYear * 12 + firstMonth + s_yhkqs - 2) % 12 + 1) + "月";
        double yhdkys = yhk * yhdkqs;

        double yhlxs = 0;
        double yhbjs = 0;
        for (int i = 1; i <= yhdkqs; i++) {
            yhlxs = yhlxs + (dkzys - yhbjs) * dklv;
            yhbjs = yhbjs + yhk - (dkzys - yhbjs) * dklv;
        }

        String xdkjssj = "";
        double xyhk = 0, jslx = 0, byhk = 0;
        if (before_loan_method == R.id.before_loan_method_part_time) {
            double tqhkys = before_loan_price * 10000;
            if (tqhkys + yhk >= (dkzys - yhbjs) * (1 + dklv)) {
                remark = "您的提前还款额已足够还清所欠贷款！";
            } else {
                yhbjs = yhbjs + yhk;
                byhk = yhk + tqhkys;
                if (deal_method_group == R.id.deal_method_reduce_years) {
                    double yhbjs_temp = yhbjs + tqhkys;
                    int xdkqs = 0;
                    for (xdkqs = 0; yhbjs_temp <= dkzys; xdkqs++) {
                        yhbjs_temp = yhbjs_temp + yhk - (dkzys - yhbjs_temp) * dklv;
                    }
                    xdkqs = xdkqs - 1;
                    xyhk = (dkzys - yhbjs - tqhkys) * (dklv * Math.pow((1 + dklv), xdkqs)) / (Math.pow((1 + dklv), xdkqs) - 1);
                    jslx = yhk * s_yhkqs - yhdkys - byhk - xyhk * xdkqs;
                    xdkjssj = Math.floor((beforeYear * 12 + beforeMonth + xdkqs - 2) / 12) + "年" + ((beforeYear * 12 + beforeMonth + xdkqs - 2) % 12 + 1) + "月";
                } else {
                    xyhk = (dkzys - yhbjs - tqhkys) * (dklv * Math.pow((1 + dklv), (s_yhkqs - yhdkqs))) / (Math.pow((1 + dklv), (s_yhkqs - yhdkqs)) - 1);
                    jslx = yhk * s_yhkqs - yhdkys - byhk - xyhk * (s_yhkqs - yhdkqs);
                    xdkjssj = yhkjssj;
                }
            }
        }

        if (before_loan_method == R.id.before_loan_method_once_time || !remark.equalsIgnoreCase("")) {
            byhk = (dkzys - yhbjs) * (1 + dklv);
            xyhk = 0;
            jslx = yhk * s_yhkqs - yhdkys - byhk;
            xdkjssj = beforeYear + "年" + beforeMonth + "月";
        }

        pay_month.setText(Global.DEFAULT_NF.format(yhk) + " 元");
        already_pay_total_money.setText(Global.DEFAULT_NF.format(yhdkys) + " 元");
        already_pay_interest_money.setText(Global.DEFAULT_NF.format(yhlxs) + " 元");
        this_month_payment.setText(Global.DEFAULT_NF.format(byhk) + " 元");
        next_month_payment.setText(Global.DEFAULT_NF.format(xyhk) + " 元");
        save_money.setText(Global.DEFAULT_NF.format(jslx) + " 元");
        last_month.setText(yhkjssj);
        new_last_payment_month.setText(xdkjssj);

        if (!remark.equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), remark, Toast.LENGTH_SHORT).show();
        }
    }
}