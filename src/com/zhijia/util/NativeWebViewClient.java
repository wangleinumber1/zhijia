package com.zhijia.util;

import com.zhijia.ui.zhijiaActivity.MainActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * WebViewClient的实现，主要用于WebView在本地直接加载URL。
 */
public class NativeWebViewClient extends WebViewClient {

    /**
     * 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
     * @param view
     * @param url
     * @return
     */
	private static int flag=1;
	private ProgressDialog progressDialog;
	private Context context;
	public NativeWebViewClient(Context context) {
		this.context=context;
	}
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        //如果不需要其他对点击链接事件的处理返回true，否则返回false
        return true;
    }
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon){
    	progressDialog=ProgressDialog.show(context, "页面加载中,请稍候...", "loading...");
    	super.onPageStarted(view, url, favicon);
    }
    @Override
    public void onPageFinished(WebView view, String url) {
    	if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}	
    }
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    	System.out.println("onReceivedError:error");
    	Intent intent = new Intent(context,MainActivity.class);
    	
    	super.onReceivedError(view, errorCode, description, failingUrl);
    }
    
}
