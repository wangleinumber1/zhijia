package com.zhijia.ui.zhijiaActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import com.zhijia.Global;
import com.zhijia.ui.R;
import com.zhijia.util.NativeWebViewClient;
import com.zhijia.util.NewsWebChromeClient;
import com.zhijia.util.UserAgentUtil;

/**
 *
 */
@SuppressLint("SetJavaScriptEnabled")
public class NewHouseDynamicDetailActivity extends Activity{


    //点击事件的侦听
    private ClickListener clickListener = new ClickListener();

    //相关新闻列表
    private WebView newHouseDynamicDetailWebView;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_house_dynamic_detail);
        newHouseDynamicDetailWebView = (WebView)findViewById(R.id.new_house_dynamic_detail_news_detail_webview) ;
      //修改userAgent 
      	UserAgentUtil.userAgentCH(newHouseDynamicDetailWebView);
        findViewById(R.id.back).setOnClickListener(clickListener);
        Intent intent = getIntent();
        url = intent.getStringExtra("url") ;
        ((TextView) findViewById(R.id.new_house_dynamic_detail_title)).setText(String.format(getString(R.string.news_detail_title), "楼盘动态", Global.NOW_CITY));
        loadNewHouseDynamicDetail(url);
    }

    private void loadNewHouseDynamicDetail(String url) {
    	 newHouseDynamicDetailWebView.getSettings().setJavaScriptEnabled(true);
    	 newHouseDynamicDetailWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
         newHouseDynamicDetailWebView.getSettings().setUseWideViewPort(true);
         //newHouseDynamicDetailWebView.getSettings().setPluginsEnabled(true);
         newHouseDynamicDetailWebView.getSettings().setAllowFileAccess(true);
         newHouseDynamicDetailWebView.getSettings().setLoadWithOverviewMode(true);  
         newHouseDynamicDetailWebView.getSettings().setSupportZoom(true); 
         newHouseDynamicDetailWebView.getSettings().setDomStorageEnabled(true); 
         newHouseDynamicDetailWebView.getSettings().setLoadsImagesAutomatically(true);
         newHouseDynamicDetailWebView.getSettings().setBuiltInZoomControls(true); 
         newHouseDynamicDetailWebView.setWebChromeClient(new NewsWebChromeClient());
         newHouseDynamicDetailWebView.setWebViewClient(new NativeWebViewClient(this));
        //加载需要显示的网页
        newHouseDynamicDetailWebView.loadUrl(url);
    }
    //点击事件的公共类
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back: //后退
                    finish();
                    break;
            }
        }
    }
}
