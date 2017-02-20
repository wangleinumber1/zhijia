package com.zhijia.ui.zhijiaActivity;

import android.os.Bundle;
import android.widget.TextView;
import com.zhijia.Global;
import com.zhijia.ui.R;

/**
 * 计算购房能力计算结果
 */
public class CalculatorCapacityResultActivity extends CommonActivity {

    private double[] rhb = {440.104, 301.103, 231.7, 190.136, 163.753, 144.08, 129.379, 117.991, 108.923, 101.542, 95.425, 90.282, 85.902, 82.133, 78.861, 75.997, 73.473, 71.236, 69.241, 67.455, 65.848, 64.397, 63.082, 61.887, 60.798, 59.802, 58.890, 58.052, 57.282};

    private double[] yhz = {1.978, 2.9344, 3.8699, 4.7847, 5.6794, 6.5544, 7.4102, 8.2472, 9.0657, 9.8662, 10.6491, 11.4148, 12.1636, 12.8959, 13.6121, 14.3126, 14.9977, 15.6677, 16.3229, 16.9637, 17.5904, 18.2034, 18.8028, 19.389, 19.9624, 20.5231, 21.0715, 21.6078, 22.1323};

    private TextView house_total_price, house_single_price, contract_tax,
            repair_fund, first_payment, insurance_premium, lawyer_fee, mortgage_registration_fees;

    private double buy_house_money, salary, pay_month, buy_area;

    private int years;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_capacity_result);

        this.setTopTitle(getString(R.string.compute_result), R.id.common_two_view_text);

        house_total_price = (TextView) findViewById(R.id.house_total_price);
        house_single_price = (TextView) findViewById(R.id.house_single_price);
        contract_tax = (TextView) findViewById(R.id.contract_tax);
        repair_fund = (TextView) findViewById(R.id.repair_fund);
        first_payment = (TextView) findViewById(R.id.first_payment);
        insurance_premium = (TextView) findViewById(R.id.insurance_premium);
        lawyer_fee = (TextView) findViewById(R.id.lawyer_fee);
        mortgage_registration_fees = (TextView) findViewById(R.id.mortgage_registration_fees);

        this.setOnClickListener(this);

        buy_house_money = getIntent().getDoubleExtra("buy_house_money", 0d);
        salary = getIntent().getDoubleExtra("salary", 0d);
        pay_month = getIntent().getDoubleExtra("pay_month", 0d);
        buy_area = getIntent().getDoubleExtra("buy_area", 0d);
        years = getIntent().getIntExtra("years", 0);

        compute();
    }

    private void compute() {
        double js00 = buy_house_money * 10000;
        double js01 = pay_month;
        double js02 = Math.round(js01 / rhb[years / 12 - 2]) * 10000;
        double js03 = buy_area;

        if (js02 > (js00 * 3.2)) {
            js02 = js00 * 3.2;
        }

        double totalPrice = js02 + 0.8 * js00;
        double singlePrice = totalPrice / js03;
        double contractTax = 0;
        if (js03 < 120) {
            contractTax = Math.round(totalPrice * 2) / 100;
        } else {
            contractTax = ((totalPrice - singlePrice * 120) * 4 + singlePrice * 120 * 2) / 100;
        }

        house_total_price.setText(Global.DEFAULT_NF.format(totalPrice) + " 元");
        house_single_price.setText(Global.DEFAULT_NF.format(singlePrice) + " 元/m²");
        contract_tax.setText(Global.DEFAULT_NF.format(contractTax) + " 元");
        repair_fund.setText(Global.DEFAULT_NF.format(Math.round(totalPrice * 2) / 100) + " 元");
        first_payment.setText(Global.DEFAULT_NF.format(Math.round(totalPrice * 20) / 100) + " 元");
        insurance_premium.setText(Global.DEFAULT_NF.format(Math.round(totalPrice * 0.05) / 100 * yhz[years / 12 - 2]) + " 元");
        lawyer_fee.setText(Global.DEFAULT_NF.format(Math.round(totalPrice * 0.3) / 100) + " 元");
        mortgage_registration_fees.setText("200~500 元");
    }
}