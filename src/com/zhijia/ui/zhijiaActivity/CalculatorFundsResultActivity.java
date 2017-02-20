package com.zhijia.ui.zhijiaActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zhijia.Global;
import com.zhijia.ui.R;

/**
 * 公积金贷款计算器结果页
 */
public class CalculatorFundsResultActivity extends CommonActivity {

    private RelativeLayout line1, line2, line3, line4;

    private TextView textView1, textView2, textView3, textView4, textViewContent1, textViewContent2, textViewContent3, textViewContent4;

    private Double price;

    private Integer years, type;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_funds_loan_result);

        line1 = (RelativeLayout) findViewById(R.id.line1);
        line2 = (RelativeLayout) findViewById(R.id.line2);
        line3 = (RelativeLayout) findViewById(R.id.line3);
        line4 = (RelativeLayout) findViewById(R.id.line4);
        textView1 = (TextView) findViewById(R.id.text_view1);
        textView2 = (TextView) findViewById(R.id.text_view2);
        textView3 = (TextView) findViewById(R.id.text_view3);
        textView4 = (TextView) findViewById(R.id.text_view4);
        textViewContent1 = (TextView) findViewById(R.id.text_view_content1);
        textViewContent2 = (TextView) findViewById(R.id.text_view_content2);
        textViewContent3 = (TextView) findViewById(R.id.text_view_content3);
        textViewContent4 = (TextView) findViewById(R.id.text_view_content4);

        this.setTopTitle(getString(R.string.compute_result), R.id.common_two_view_text);
        this.setOnClickListener(this);

        price = getIntent().getDoubleExtra("price", 0d);
        years = getIntent().getIntExtra("years", 0);
        type = getIntent().getIntExtra("type", 2);

        compute();
    }

    private void compute() {
        int dknx;
        double syhk, dked, hkfs, bxhj, bxhj2, gbl, r, rb, l1_5 = 0.0405, l6_30 = 0.0459, bcv = 0;
        dknx = years;
        if (dknx > 5) {
            bcv = Math.round(1000000 * l6_30 / 12) / 1000000;
        } else {
            bcv = Math.round(1000000 * l1_5 / 12) / 1000000;
        }

        dked = Math.round(price * 10) / 10;
        price = dked;

        if (type == 0) {
            double ylv_new;
            if (dknx >= 1 && dknx <= 5)
                ylv_new = l1_5 / 12;
            else
                ylv_new = l6_30 / 12;
            double ncm = ylv_new + 1;
            double dknx_new = dknx * 12;
            double total_ncm = Math.pow(ncm, dknx_new);
            String value1 = Global.DEFAULT_NF.format(dked * 10000 * ylv_new * total_ncm / (total_ncm - 1));
            bxhj = (dked * 10000 * ylv_new * total_ncm) / (total_ncm - 1) * dknx * 12;

            textView1.setText(getString(R.string.monthly_payments));
            textViewContent1.setText(value1 + " 元");
            textView2.setText(getString(R.string.principal_and_interest));
            textViewContent2.setText(Global.DEFAULT_NF.format(bxhj) + " 元");
            line3.setVisibility(View.GONE);
            line4.setVisibility(View.GONE);
        } else if (type == 1) {
            if (dknx > 5) {
                rb = l6_30 * 100;
            } else {
                rb = l1_5 * 100;
            }
            syhk = dked * 10000 / (dknx * 12) + dked * 10000 * rb / (100 * 12);

            double yhke, dkys, sydkze, yhkbj;

            dkys = dknx * 12;
            yhkbj = dked * 10000 / dkys;
            yhke = syhk;
            sydkze = dked * 10000 - yhkbj;
            bxhj = syhk;
            for (int count = 2; count <= dkys; ++count) {
                yhke = dked * 10000 / dkys + sydkze * rb / 1200;
                sydkze -= yhkbj;
                bxhj += yhke;
            }


            textView1.setText(getString(R.string.first_month_repayment));
            textViewContent1.setText(Global.DEFAULT_NF.format(syhk) + " 元");
            textView2.setText(getString(R.string.principal_and_interest));
            textViewContent2.setText(Global.DEFAULT_NF.format(bxhj) + " 元");
            line3.setVisibility(View.GONE);
            line4.setVisibility(View.GONE);
        } else if (type == 2) {
            rb = 0;
            switch (dknx) {
                case 1:
                    rb = 83.04 / 100;
                    break;
                case 2:
                    rb = 81.08 / 100;
                    break;
                case 3:
                    rb = 79.12 / 100;
                    break;
                case 4:
                    rb = 77.16 / 100;
                    break;
                case 5:
                    rb = 75.20 / 100;
                    break;
                case 6:
                    rb = 73.24 / 100;
                    break;
                case 7:
                    rb = 71.28 / 100;
                    break;
                case 8:
                    rb = 69.32 / 100;
                    break;
                case 9:
                    rb = 67.36 / 100;
                    break;
                case 10:
                    rb = 65.40 / 100;
                    break;
                case 11:
                    rb = 63.44 / 100;
                    break;
                case 12:
                    rb = 61.48 / 100;
                    break;
                case 13:
                    rb = 59.52 / 100;
                    break;
                case 14:
                    rb = 57.56 / 100;
                    break;
                case 15:
                    rb = 55.60 / 100;
                    break;
                case 16:
                    rb = 53.64 / 100;
                    break;
                case 17:
                    rb = 51.68 / 100;
                    break;
                case 18:
                    rb = 49.72 / 100;
                    break;
                case 19:
                    rb = 47.76 / 100;
                    break;
                case 20:
                    rb = 45.80 / 100;
                    break;
                case 21:
                    rb = 43.84 / 100;
                    break;
                case 22:
                    rb = 41.88 / 100;
                    break;
                case 23:
                    rb = 39.92 / 100;
                    break;
                case 24:
                    rb = 37.96 / 100;
                    break;
                case 25:
                    rb = 36.00 / 100;
                    break;
                case 26:
                    rb = 34.04 / 100;
                    break;
                case 27:
                    rb = 32.08 / 100;
                    break;
                case 28:
                    rb = 30.12 / 100;
                    break;
                case 29:
                    rb = 28.16 / 100;
                    break;
                case 30:
                    rb = 26.20 / 100;
                    break;
            }
            double yhke, ll, zhbj, zdhkll;
            zhbj = dked * 10000 * rb;
            if (dknx <= 5) {
                ll = l1_5 / 12;
                zdhkll = 0.0378 / 12;
                double total_gjj = Math.pow(zdhkll + 1, dknx * 12);
                syhk = Math.ceil(dked * 10000 * zdhkll * total_gjj / (total_gjj - 1));
            } else {
                ll = l6_30 / 12;
                zdhkll = 0.0423 / 12;
                double total_gjj = Math.pow(zdhkll + 1, dknx * 12 - 1);
                syhk = Math.ceil((dked * 10000 - zhbj) * zdhkll * total_gjj / (total_gjj - 1) + zhbj * zdhkll);
            }

            double zhyqbj = dked * 10000;
            double zchlx = 0;
            for (int i = 1; i < dknx * 12; i++) {
                zchlx += zhyqbj * ll;
                zhyqbj = zhyqbj - (syhk - zhyqbj * ll);
            }
            double sydkze = dked * 10000 - syhk;
            zchlx += zhyqbj * ll;

            textView1.setText(getString(R.string.minimum_payment));
            textViewContent1.setText(syhk + " 元");
            textView2.setText(getString(R.string.last_interest));
            textViewContent2.setText(Global.DEFAULT_NF.format(zhyqbj) + " 元");
            textView3.setText(getString(R.string.finally_principal));
            textViewContent3.setText(Global.DEFAULT_NF.format(zhyqbj * ll) + " 元");
            textView4.setText(getString(R.string.total_repayment_interest));
            textViewContent4.setText(Global.DEFAULT_NF.format(zchlx) + " 元");
        }
    }
}