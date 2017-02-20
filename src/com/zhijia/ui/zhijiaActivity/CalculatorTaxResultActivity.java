package com.zhijia.ui.zhijiaActivity;

import android.os.Bundle;
import android.widget.TextView;
import com.zhijia.Global;
import com.zhijia.ui.R;

/**
 * 税费计算器结果页
 */
public class CalculatorTaxResultActivity extends CommonActivity {

    private Double price, area;

    private TextView houseTotalPriceTextView, stampTaxTextView,
            notarizationChargeTextView, contractTaxTextView,
            quitclaimDeedCommissionChargeTextView, houseDealCommissionChargeTextView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_tax_result);

        this.setTopTitle(getString(R.string.compute_result), R.id.common_two_view_text);
        this.setOnClickListener(this);

        houseTotalPriceTextView = (TextView) findViewById(R.id.house_total_price);
        stampTaxTextView = (TextView) findViewById(R.id.stamp_tax);
        notarizationChargeTextView = (TextView) findViewById(R.id.notarization_charge);
        contractTaxTextView = (TextView) findViewById(R.id.contract_tax);
        quitclaimDeedCommissionChargeTextView = (TextView) findViewById(R.id.quitclaim_deed_commission_charge);
        houseDealCommissionChargeTextView = (TextView) findViewById(R.id.house_deal_commission_charge);

        price = getIntent().getDoubleExtra("price", 0d);
        area = getIntent().getDoubleExtra("area", 0d);

        compute();
    }

    private void compute() {
        double fkz3 = price * area;
        double fw = 0, q = 0;

        double yh = fkz3 * 0.0005;
        if (price <= 9432) {
            q = fkz3 * 0.015;
        } else if (price > 9432) {
            q = fkz3 * 0.03;
        }
        if (area <= 120) {
            fw = 500;
        } else if (120 < area && area <= 5000) {
            fw = 1500;
        }
        if (area > 5000) {
            fw = 5000;
        }
        double gzh = fkz3 * 0.003;
        stampTaxTextView.setText(Global.DEFAULT_NF.format(yh) + " 元");
        houseTotalPriceTextView.setText(Global.DEFAULT_NF.format(fkz3) + " 元");
        notarizationChargeTextView.setText(Global.DEFAULT_NF.format(gzh) + " 元");
        contractTaxTextView.setText("0 元");
        quitclaimDeedCommissionChargeTextView.setText(Global.DEFAULT_NF.format(gzh) + " 元");
        houseDealCommissionChargeTextView.setText(Global.DEFAULT_NF.format(fw) + " 元");
    }
}