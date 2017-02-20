package com.zhijia.ui.zhijiaActivity;

import android.os.Bundle;
import android.widget.TextView;
import com.zhijia.Global;
import com.zhijia.ui.R;
import com.zhijia.ui.calculator.HouseBankRate;

/**
 * 贷款计算器结果页
 */
public class CalculatorLoanResultActivity extends CommonActivity {

    private TextView house_total_price, loan_total_priceTextView, pay_total_price,
            pay_interest, first_payment, loan_total_month, pay_back_per_month;

    private int loanType, loanByType, years, payLoanType, percent, lilv;

    private double house_single_price, area, loan_total_price, business_money, fund_money;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_loan_result);

        house_total_price = (TextView) findViewById(R.id.house_total_price);
        loan_total_priceTextView = (TextView) findViewById(R.id.loan_total_price);
        pay_total_price = (TextView) findViewById(R.id.pay_total_price);
        pay_interest = (TextView) findViewById(R.id.pay_interest);
        first_payment = (TextView) findViewById(R.id.first_payment);
        loan_total_month = (TextView) findViewById(R.id.loan_total_month);
        pay_back_per_month = (TextView) findViewById(R.id.pay_back_per_month);

        this.setTopTitle(getString(R.string.compute_result), R.id.common_two_view_text);
        this.setOnClickListener(this);

        house_single_price = getIntent().getDoubleExtra("house_single_price", 0d);
        area = getIntent().getDoubleExtra("area", 0d);
        loan_total_price = getIntent().getDoubleExtra("loan_total_price", 0d);
        business_money = getIntent().getDoubleExtra("business_money", 0d);
        fund_money = getIntent().getDoubleExtra("fund_money", 0d);
        loanType = getIntent().getIntExtra("loanType", 0);
        loanByType = getIntent().getIntExtra("loanByType", 0);
        years = getIntent().getIntExtra("years", 0);
        payLoanType = getIntent().getIntExtra("payLoanType", 0);
        percent = getIntent().getIntExtra("percent", 0);
        lilv = getIntent().getIntExtra("lilv", 0);

        compute();
    }

    private void compute() {
        int month = years * 12;
        loan_total_month.setText(month + " 个月");

        if (loanType == 2) {
            double total_sy = business_money * 10000;
            double total_gjj = fund_money * 10000;
            house_total_price.setText("略");//房款总额
            first_payment.setText("0 元");//首期付款

            //贷款总额
            double daikuan_total = total_sy + total_gjj;
            loan_total_priceTextView.setText(Global.DEFAULT_NF.format(daikuan_total) + " 元");

            //月还款
            double lilv_sd = HouseBankRate.getBankRate(lilv, 1, years);//得到商贷利率
            double lilv_gjj = HouseBankRate.getBankRate(lilv, 2, years);//得到公积金利率

            //1.本金还款
            //月还款
            if (payLoanType == 1) {
                double all_total2 = 0;
                String month_money2 = "";
                for (int j = 0; j < month; j++) {
                    //调用函数计算: 本金月还款额
                    double huankuan = getMonthMoney2(lilv_sd, total_sy, month, j) + getMonthMoney2(lilv_gjj, total_gjj, month, j);
                    all_total2 += huankuan;
                    //fmobj.month_money2.options[j] = new Option( (j+1) +"月," + huankuan + "(元)", huankuan);
                    month_money2 += (j + 1) + "月," + Global.DEFAULT_NF.format(huankuan) + " (元)\n";
                }
                pay_back_per_month.setText(month_money2);
                //还款总额
                pay_total_price.setText(Global.DEFAULT_NF.format(all_total2) + " 元");
                //支付利息款
                pay_interest.setText(Global.DEFAULT_NF.format(all_total2 - daikuan_total) + " 元");
            } else {
                //2.本息还款
                //月均还款
                double month_money1 = getMonthMoney1(lilv_sd, total_sy, month) + getMonthMoney1(lilv_gjj, total_gjj, month);//调用函数计算
                pay_back_per_month.setText(Global.DEFAULT_NF.format(month_money1) + " 元");
                //还款总额
                double all_total1 = month_money1 * month;
                pay_total_price.setText(Global.DEFAULT_NF.format(all_total1) + " 元");
                //支付利息款
                pay_interest.setText(Global.DEFAULT_NF.format(all_total1 - daikuan_total) + " 元");
            }
        } else {
            //--  商业贷款、公积金贷款
            double bankLilv = HouseBankRate.getBankRate(lilv, (loanType + 1), years);//得到利率
            double daikuan_total = 0;
            if (loanByType == 0) {
                //------------ 根据单价面积计算
                //房款总额
                double fangkuan_total = house_single_price * area;
                house_total_price.setText(Global.DEFAULT_NF.format(fangkuan_total) + " 元");
                //贷款总额
                daikuan_total = (house_single_price * area) * (percent / 10.0);
                loan_total_priceTextView.setText(Global.DEFAULT_NF.format(daikuan_total) + " 元");
                //首期付款
                double money_first = fangkuan_total - daikuan_total;
                first_payment.setText(Global.DEFAULT_NF.format(money_first) + " 元");
            } else {
                //------------ 根据贷款总额计算
                //房款总额
                house_total_price.setText("略");
                //贷款总额
                daikuan_total = loan_total_price * 10000;
                loan_total_priceTextView.setText(Global.DEFAULT_NF.format(daikuan_total) + " 元");
                //首期付款
                first_payment.setText("0 元");
            }
            //1.本金还款
            //月还款
            if (payLoanType == 1) {
                double all_total2 = 0;
                String month_money2 = "";
                for (int j = 0; j < month; j++) {
                    //调用函数计算: 本金月还款额
                    double huankuan = getMonthMoney2(bankLilv, daikuan_total, month, j);
                    all_total2 += huankuan;
                    //fmobj.month_money2.options[j] = new Option( (j+1) +"月," + huankuan + "(元)", huankuan);
                    month_money2 += (j + 1) + "月," + Global.DEFAULT_NF.format(huankuan) + " (元)\n";
                }
                pay_back_per_month.setText(month_money2);
                //还款总额
                pay_total_price.setText(Global.DEFAULT_NF.format(all_total2) + " 元");
                //支付利息款
                pay_interest.setText(Global.DEFAULT_NF.format(all_total2 - daikuan_total) + " 元");
            } else {
                //2.本息还款
                //月均还款
                double month_money1 = getMonthMoney1(bankLilv, daikuan_total, month);//调用函数计算
                pay_back_per_month.setText(Global.DEFAULT_NF.format(month_money1) + " 元");
                //还款总额
                double all_total1 = month_money1 * month;
                pay_total_price.setText(Global.DEFAULT_NF.format(all_total1) + " 元");
                //支付利息款
                pay_interest.setText(Global.DEFAULT_NF.format(all_total1 - daikuan_total) + " 元");
            }
        }
    }

    //本金还款的月还款额(参数: 年利率 / 贷款总额 / 贷款总月份 / 贷款当前月0～length-1)
    private double getMonthMoney2(double lilv, double total, int month, int cur_month) {
        double lilv_month = lilv / 12;//月利率
        //return total * lilv_month * Math.pow(1 + lilv_month, month) / ( Math.pow(1 + lilv_month, month) -1 );
        double benjin_money = total / month;
        return (total - benjin_money * cur_month) * lilv_month + benjin_money;

    }

    //本息还款的月还款额(参数: 年利率/贷款总额/贷款总月份)
    private double getMonthMoney1(double lilv, double total, int month) {
        double lilv_month = lilv / 12;//月利率
        return total * lilv_month * Math.pow(1 + lilv_month, month) / (Math.pow(1 + lilv_month, month) - 1);
    }
}