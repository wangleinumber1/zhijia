package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.zhijia.ui.R;

/**
 * 看房须知
 */
public class CondoTourMustKnowActivity extends Activity {

    //点击事件的侦听
    private ClickListener clickListener = new ClickListener();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.condo_tour_must_know);

        findViewById(R.id.back).setOnClickListener(clickListener);
        findViewById(R.id.i_know).setOnClickListener(clickListener);
    }

    //点击事件的公共类
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.back: //后退
                case R.id.i_know://知道了
                    finish();
                    break;
            }
        }
    }
}