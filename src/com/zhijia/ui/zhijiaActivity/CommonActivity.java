package com.zhijia.ui.zhijiaActivity;

import android.app.Activity;
import android.widget.TextView;
import com.zhijia.ui.R;
import com.zhijia.ui.common.CommonListener;

/**
 * 公共的Activity
 */
public abstract class CommonActivity extends Activity {

    public void setTopTitle(String text, int textViewId) {
        TextView textView = (TextView) findViewById(textViewId);
        textView.setText(text);
    }

    public void setOnClickListener(Activity activity) {
        findViewById(R.id.common_two_view_back).setOnClickListener(new CommonListener(activity));
    }
}