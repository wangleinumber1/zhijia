package com.zhijia.ui.zhijiaActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.zhijia.ui.R;

import java.util.Calendar;

/**
 * 提前还贷款计算器
 */
public class CalculatorBeforeLoanActivity extends CommonActivity {

    private final Calendar cal = Calendar.getInstance();
    private final String[] mItems = {"2年(24期)", "3年(36期)", "4年(48期)", "5年(60期)", "6年(72期)", "7年(84期)", "8年(96期)", "9年(108期)", "10年(120期)", "15年(180期)", "20年(240期)", "25年(300期)", "30年(360期)"};
    private final int[] values = {24, 36, 48, 60, 72, 84, 96, 108, 120, 180, 240, 300, 360};
    private int years = values[0];
    private final String[] lItems = {"12年7月6日利率上限(1.1倍)", " 12年7月6日利率下限(85折)", " 12年7月6日利率下限(7折)", "12年7月6日基准利率", "12年6月8日利率上限(1.1倍)", "12年6月8日利率下限(85折)", "12年6月8日利率下限(7折)", "12年6月8日基准利率", "11年7月6日利率上限(1.1倍)", "11年7月6日利率下限(85折)", "11年7月6日利率下限(7折)", " 11年7月6日基准利率", " 11年4月5日利率上限(1.1倍)", "11年4月5日利率下限(85折)"};
    private final int[] l_values = {31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18};
    private int lilv = l_values[3];
    private ClickListener clickListener = new ClickListener();
    private LinearLayout calculator_before_loan_type_layout, before_loan_method_part_time_layout, before_loan_price_layout;

    private TextView calculator_before_loan_title_one, calculator_before_loan_title_two,
            loan_total_years, loan_rate, first_payment_month, last_payment_month;

    private EditText loan_total_price, before_loan_price;

    private ImageView before_loan_method_part_time_split_line, before_loan_price_split_line;

    private RadioGroup before_loan_method, deal_method_group;

    private Integer loanType = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_before_loan);
   
        this.setTopTitle(getString(R.string.calculator_before_loan), R.id.title);
        calculator_before_loan_type_layout = (LinearLayout) findViewById(R.id.calculator_before_loan_type_layout);
        //before_loan_method_part_time_layout = (LinearLayout) findViewById(R.id.before_loan_method_part_time_layout);
       // before_loan_price_layout = (LinearLayout) findViewById(R.id.before_loan_price_layout);
        calculator_before_loan_title_one = (TextView) findViewById(R.id.calculator_before_loan_title_one);
        calculator_before_loan_title_two = (TextView) findViewById(R.id.calculator_before_loan_title_two);
        loan_total_years = (TextView) findViewById(R.id.loan_total_years);
        loan_rate = (TextView) findViewById(R.id.loan_rate);
        first_payment_month = (TextView) findViewById(R.id.first_payment_month);
        last_payment_month = (TextView) findViewById(R.id.last_payment_month);
        loan_total_price = (EditText) findViewById(R.id.loan_total_price);
       // before_loan_price = (EditText) findViewById(R.id.before_loan_price);
        //before_loan_method_part_time_split_line = (ImageView) findViewById(R.id.before_loan_method_part_time_split_line);
        before_loan_price_split_line = (ImageView) findViewById(R.id.before_loan_price_split_line);
        before_loan_method = (RadioGroup) findViewById(R.id.before_loan_method);
        //deal_method_group = (RadioGroup) findViewById(R.id.deal_method_group);

        initUI();

        this.setOnClickListener(this);
        calculator_before_loan_title_one.setOnClickListener(clickListener);
        calculator_before_loan_title_two.setOnClickListener(clickListener);
        loan_total_years.setOnClickListener(clickListener);
        loan_rate.setOnClickListener(clickListener);
        first_payment_month.setOnClickListener(clickListener);
        last_payment_month.setOnClickListener(clickListener);
        findViewById(R.id.reset).setOnClickListener(clickListener);
        findViewById(R.id.compute).setOnClickListener(clickListener);

        before_loan_method.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                int radioButtonId = arg0.getCheckedRadioButtonId();
                if (radioButtonId == R.id.before_loan_method_part_time) {
                	LayoutInflater inflater = LayoutInflater.from(CalculatorBeforeLoanActivity.this);
                	View layout=inflater.inflate(R.layout.dialog,null);
                	before_loan_price=(EditText)layout.findViewById(R.id.before_loan_price);
                	deal_method_group = (RadioGroup)layout.findViewById(R.id.deal_method_group);
                	deal_method_group.check(R.id.deal_method_reduce_years);
                	AlertDialog.Builder builder=new AlertDialog.Builder(CalculatorBeforeLoanActivity.this);
                	builder.setTitle("部分还清还款方式")  
                    .setView(layout)  
                    .setPositiveButton("确定", null);  
                	builder.setNegativeButton("取消", null);
                	builder.create().show();
                   /* before_loan_price_split_line.setVisibility(View.VISIBLE);
                    before_loan_price_layout.setVisibility(View.VISIBLE);
                    before_loan_method_part_time_split_line.setVisibility(View.VISIBLE);
                    before_loan_method_part_time_layout.setVisibility(View.VISIBLE);*/
                } else {
                    /*before_loan_price.setText("");
                    before_loan_price_split_line.setVisibility(View.GONE);
                    before_loan_price_layout.setVisibility(View.GONE);
                    before_loan_method_part_time_split_line.setVisibility(View.GONE);
                    before_loan_method_part_time_layout.setVisibility(View.GONE);*/
                }
            }
        });
    }

    private void initUI() {
        loan_total_price.setText("");
        //before_loan_price.setText("");
        loan_total_years.setText(mItems[0]);
        years = values[0];
        loan_rate.setText(lItems[3]);
        lilv = l_values[3];
        loanType = 0;
        changeType(loanType);
        //before_loan_method_part_time_split_line.setVisibility(View.GONE);
        //before_loan_method_part_time_layout.setVisibility(View.GONE);
        before_loan_price_split_line.setVisibility(View.GONE);
        //before_loan_price_layout.setVisibility(View.GONE);
        before_loan_method.check(R.id.before_loan_method_once_time);
        first_payment_month.setText("2014-2");
        last_payment_month.setText("2014-2");
       // deal_method_group.check(R.id.deal_method_reduce_years);
    }

    /**
     * 修改类型及样式
     *
     * @param type
     */
    private void changeType(int type) {
    	//initUI();
        if (type == 0) {
            calculator_before_loan_type_layout.setBackgroundResource(R.drawable.calculator_before_loan_one);
            calculator_before_loan_title_one.setTextColor(getResources().getColor(R.color.white));
            calculator_before_loan_title_two.setTextColor(getResources().getColor(R.color.red));
        } else {
            calculator_before_loan_type_layout.setBackgroundResource(R.drawable.calculator_before_loan_two);
            calculator_before_loan_title_one.setTextColor(getResources().getColor(R.color.red));
            calculator_before_loan_title_two.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }

    //点击事件的公共类
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.reset: //重置
                    initUI();
                    break;
                case R.id.compute://计算
                	System.out.println("部分还款："+before_loan_price.getText());
                    if (loan_total_price.getText().toString().trim().length() == 0) {
                        Toast.makeText(getApplicationContext(), "填写数据不完整", Toast.LENGTH_SHORT).show();
                        loan_total_price.setFocusable(true);
                    } else if (before_loan_method.getCheckedRadioButtonId() == R.id.before_loan_method_part_time && before_loan_price.getText().toString().trim().length() == 0) {
                        Toast.makeText(getApplicationContext(), "填写数据不完整", Toast.LENGTH_SHORT).show();
                        before_loan_price.setFocusable(true);
                    } else {
                        double temp = 0;
                        if (before_loan_price.getText().toString().trim().length() > 0) {
                            temp = Double.parseDouble(before_loan_price.getText().toString().trim());
                        }
                        Intent calculatorBeforeLoanResultIntent = new Intent(getApplicationContext(), CalculatorBeforeLoanResultActivity.class);
                        calculatorBeforeLoanResultIntent.putExtra("loan_total_price", Double.parseDouble(loan_total_price.getText().toString().trim()));
                        calculatorBeforeLoanResultIntent.putExtra("before_loan_price", temp);
                        calculatorBeforeLoanResultIntent.putExtra("loanType", loanType);
                        calculatorBeforeLoanResultIntent.putExtra("years", years);
                        calculatorBeforeLoanResultIntent.putExtra("lilv", lilv);
                        calculatorBeforeLoanResultIntent.putExtra("first_payment_month", first_payment_month.getText().toString());
                        calculatorBeforeLoanResultIntent.putExtra("last_payment_month", last_payment_month.getText().toString());
                        calculatorBeforeLoanResultIntent.putExtra("before_loan_method", before_loan_method.getCheckedRadioButtonId());
                        calculatorBeforeLoanResultIntent.putExtra("deal_method_group", deal_method_group.getCheckedRadioButtonId());
                        startActivity(calculatorBeforeLoanResultIntent);
                        loan_total_price.setText("");
                        first_payment_month.setText("2014-2");
                        last_payment_month.setText("2014-2");
                    }
                    break;
                case R.id.calculator_before_loan_title_one:
                    loanType = 0;
                    changeType(loanType);
                    break;
                case R.id.calculator_before_loan_title_two:
                    loanType = 1;
                    changeType(loanType);
                    break;
                case R.id.loan_total_years:
                    AlertDialog.Builder builder = new AlertDialog.Builder(CalculatorBeforeLoanActivity.this);
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
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(CalculatorBeforeLoanActivity.this);
                    builder2.setTitle(getString(R.string.please_select));
                    builder2.setItems(lItems, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            loan_rate.setText(lItems[which]);
                            lilv = l_values[which];
                        }
                    });

                    builder2.create().show();
                    break;
                case R.id.first_payment_month:
                    DatePickerDialog dialog = new CustomerDatePickerDialog(
                            CalculatorBeforeLoanActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                                    first_payment_month.setText(year + "-" + (month + 1));
                                }
                            },
                            cal.get(Calendar.YEAR), // 传入年份
                            cal.get(Calendar.MONTH), // 传入月份
                            cal.get(Calendar.DAY_OF_MONTH) // 传入天数
                    );
                    dialog.show();
                    DatePicker dp = findDatePicker((ViewGroup) dialog.getWindow().getDecorView());
                    if (dp != null) {
                        if (Build.VERSION.SDK_INT < 11) {
                            ((ViewGroup) dp.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                        } else if (Build.VERSION.SDK_INT > 14) {
                            ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                        }
                    }
                    break;
                case R.id.last_payment_month:
                    DatePickerDialog dialog2 = new CustomerDatePickerDialog(
                            CalculatorBeforeLoanActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                                    last_payment_month.setText(year + "-" + (month + 1));
                                }
                            },
                            cal.get(Calendar.YEAR), // 传入年份
                            cal.get(Calendar.MONTH), // 传入月份
                            cal.get(Calendar.DAY_OF_MONTH) // 传入天数
                    );
                    dialog2.show();
                    DatePicker dp2 = findDatePicker((ViewGroup) dialog2.getWindow().getDecorView());
                    if (dp2 != null) {
                        if (Build.VERSION.SDK_INT < 11) {
                            ((ViewGroup) dp2.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                        } else if (Build.VERSION.SDK_INT > 14) {
                            ((ViewGroup) ((ViewGroup) dp2.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                        }
                    }
                    break;
            }
        }
    }

    class CustomerDatePickerDialog extends DatePickerDialog {
        public CustomerDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
            this.setTitle(year + "年" + (monthOfYear + 1) + "月");
        }

        @Override
        public void onDateChanged(DatePicker view, int year, int month, int day) {
            super.onDateChanged(view, year, month, day);
            this.setTitle(year + "年" + (month + 1) + "月");
        }
    }
}
