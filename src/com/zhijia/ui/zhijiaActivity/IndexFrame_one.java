package com.zhijia.ui.zhijiaActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import static com.zhijia.Global.DeviceID;

import com.zhijia.Global;
import com.zhijia.ui.R;
import com.zhijia.util.NativeWebChromeClient;
import com.zhijia.util.NativeWebViewClient;
import com.zhijia.util.UserAgentUtil;

/**
 * Created by Administrator on 2014/7/3.
 */
@SuppressLint("SetJavaScriptEnabled")
public class IndexFrame_one extends Activity {
	// 点击事件的侦听
	private ClickListener clickListener = new ClickListener();
	// 相关
	private WebView newsDetailWebView;
	//
	private String newsTypeName;
	private static ProgressDialog progressDialog;
	// URL
	private String url;
//	private String ua="Mozilla/5.0(Linux;U;Android 2.2.1;en-us;zhijia.inc Build.FRG83) "
//			 +
//			 "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1";
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inedx_one);
		newsDetailWebView = (WebView) findViewById(R.id.news_detail_webview);		
		//修改userAgent 
		UserAgentUtil.userAgentCH(newsDetailWebView);		
		findViewById(R.id.back).setOnClickListener(clickListener);
		Intent intent = getIntent();
		System.out.println("Index>>>>>>>>>>" + DeviceID);
		newsTypeName = "置家网";
		url = intent.getStringExtra("url");
		((TextView) findViewById(R.id.title)).setText(String.format(getString(R.string.news_detail_title), newsTypeName,Global.NOW_CITY));
		loadNewsDetail(url + "?deviceid=" + DeviceID);
	}

	// 加载当前信息
	private void loadNewsDetail(String url) {
		// 加载需要显示的网页
		newsDetailWebView.loadUrl(url);
		// 设置WebView属性，能够执行Javascript脚本
		newsDetailWebView.getSettings().setJavaScriptEnabled(true);
		// newsDetailWebView.getSettings().setBlockNetworkImage(true);
		newsDetailWebView.getSettings()
				.setJavaScriptCanOpenWindowsAutomatically(true);
		newsDetailWebView.getSettings().setUseWideViewPort(true);
		//newsDetailWebView.getSettings().setPluginsEnabled(true);
		newsDetailWebView.getSettings().setAllowFileAccess(true);
		newsDetailWebView.getSettings().setLoadWithOverviewMode(true);
		newsDetailWebView.getSettings().setSupportZoom(true);
		newsDetailWebView.getSettings().setAllowFileAccess(true);
		newsDetailWebView.getSettings().setDomStorageEnabled(true);
		newsDetailWebView.getSettings().setLoadsImagesAutomatically(true);
		newsDetailWebView.getSettings().setBuiltInZoomControls(true);
		newsDetailWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);//隐藏滚动条
		//newsDetailWebView.setWebChromeClient(new NativeWebChromeClient());
		newsDetailWebView.setWebChromeClient(new WebChromeClient(){
			@Override  
		    public void onCloseWindow(WebView window) {  
		        super.onCloseWindow(window);  
		    }  
		  
		    @Override  
		    public boolean onCreateWindow(WebView view, boolean dialog,boolean userGesture, Message resultMsg) {  
		        return super.onCreateWindow(view, dialog, userGesture, resultMsg);  
		    }  
		  
		    /**  
		     * 覆盖默认的window.alert展示界面，避免title里显示为“：来自file:////”  
		     */  
		    public boolean onJsAlert(WebView view, String url, String message,JsResult result) {
		    	
		        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext()); 
		        System.out.println(message);                 
		        builder.setTitle("砸蛋赢大奖").setMessage(message).setPositiveButton("确定", null);               
		        // 不需要绑定按键事件  
		        // 屏蔽keycode等于84之类的按键  
		        builder.setOnKeyListener(new OnKeyListener() {  
		            public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {  
		                Log.v("onJsAlert", "keyCode==" + keyCode + "event="+ event);  
		                return true;  
		            }
		        });  
		        // 禁止响应按back键的事件  
		        builder.setCancelable(false);  
		        AlertDialog dialog = builder.create();  
		        dialog.show();  
		        result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。  
		        return true;  
		        // return super.onJsAlert(view, url, message, result);  
		    }  
		  
		    public boolean onJsBeforeUnload(WebView view, String url,  
		            String message, JsResult result) {  
		        return super.onJsBeforeUnload(view, url, message, result);  
		    }  
		  
		    /** 
		     * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////” 
		     */  
		    public boolean onJsConfirm(WebView view, String url, String message,  
		            final JsResult result) {  
		        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());  
		        builder.setTitle("砸蛋赢大奖")  
		                .setMessage(message)  
		                .setPositiveButton("确定",new OnClickListener() {  
		                            public void onClick(DialogInterface dialog,int which) {  
		                                result.confirm();  
		                            }  
		                        })  
		                .setNeutralButton("取消", new OnClickListener() {  
		                    public void onClick(DialogInterface dialog, int which) {  
		                        result.cancel();  
		                    }  
		                });  
		        builder.setOnCancelListener(new OnCancelListener() {  
		            @Override  
		            public void onCancel(DialogInterface dialog) {  
		                result.cancel();  
		            }  
		        });  
		  
		        // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题  
		        builder.setOnKeyListener(new OnKeyListener() {
		            @Override  
		            public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {  
		                Log.v("onJsConfirm", "keyCode==" + keyCode + "event="+ event);  
		                return true;  
		            }  
		        });  
		        // 禁止响应按back键的事件  
		        // builder.setCancelable(false);  
		        AlertDialog dialog = builder.create();  
		        dialog.show();  
		        return true;  
		        // return super.onJsConfirm(view, url, message, result);  
		    }  
		  
		    /** 
		     * 覆盖默认的window.prompt展示界面，避免title里显示为“：来自file:////” 
		     * window.prompt('请输入您的域名地址', '618119.com'); 
		     */  
		    public boolean onJsPrompt(WebView view, String url, String message,  
		            String defaultValue, final JsPromptResult result) {  
		        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());  
		                  
		        builder.setTitle("砸蛋赢大奖").setMessage(message);  
		                  
		        final EditText et = new EditText(view.getContext());  
		        et.setSingleLine();  
		        et.setText(defaultValue);  
		        builder.setView(et)  
		                .setPositiveButton("确定", new OnClickListener() {  
		                    public void onClick(DialogInterface dialog, int which) {  
		                        result.confirm(et.getText().toString());  
		                    }  
		          
		                })  
		                .setNeutralButton("取消", new OnClickListener() {  
		                    public void onClick(DialogInterface dialog, int which) {  
		                        result.cancel();  
		                    }  
		                });  
		  
		        // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题  
		        builder.setOnKeyListener(new OnKeyListener() {  
		            public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {  
		                Log.v("onJsPrompt", "keyCode==" + keyCode + "event="+ event);  
		                return true;  
		            }  
		        });  
		  
		        // 禁止响应按back键的事件  
		        // builder.setCancelable(false);  
		        AlertDialog dialog = builder.create();  
		        dialog.show();  
		        return true;  
		        // return super.onJsPrompt(view, url, message, defaultValue,  
		        // result);  
		    }  
		    @Override  
		    public void onProgressChanged(WebView view, int newProgress) {     	
		        super.onProgressChanged(view, newProgress);  
		    }  
		  
		    @Override  
		    public void onReceivedIcon(WebView view, Bitmap icon) {  
		        super.onReceivedIcon(view, icon);  
		    }  
		  
		    @Override  
		    public void onReceivedTitle(WebView view, String title) {  
		        super.onReceivedTitle(view, title);  
		    }  
		  
		    @Override  
		    public void onRequestFocus(WebView view) {  
		        super.onRequestFocus(view);  
		    }  
		});
		//newsDetailWebView.setWebViewClient(new NativeWebViewClient(this));
		//
		newsDetailWebView.setWebViewClient(new WebViewClient(){
			@Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        view.loadUrl(url);
		        //如果不需要其他对点击链接事件的处理返回true，否则返回false
		        return true;
		    }
		    @Override
		    public void onPageStarted(WebView view, String url, Bitmap favicon){
		    	progressDialog=ProgressDialog.show(IndexFrame_one.this, "页面加载中,请稍候...", "loading...");
		    	super.onPageStarted(view, url, favicon);
		    }
		    @Override
		    public void onPageFinished(WebView view, String url) {		    	
					progressDialog.dismiss();	
		    }
		    @Override
		    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		    	System.out.println("onReceivedError:error");
		    	Intent intent = new Intent(IndexFrame_one.this,MainActivity.class);
		    	IndexFrame_one.this.startActivity(intent);
		    	finish();
		    	super.onReceivedError(view, errorCode, description, failingUrl);
		    }
		});
		
	}

	@Override
	protected void onDestroy() {
		System.out.println("onDestroy");
		super.onDestroy();
		newsDetailWebView.clearCache(true);
		//newsDetailWebView.clearHistory();
		newsDetailWebView.stopLoading();
	}

	@Override
	protected void onPause() {
		System.out.println("onPause");
		super.onPause();
		newsDetailWebView.pauseTimers();
	}

	@Override
	protected void onResume() {
		System.out.println("onResume");
		super.onResume();
		newsDetailWebView.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;

		default:
			break;
		}
		return true;

	}

	// 点击事件的公共类
	class ClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back: // 后退
				finish();
				break;
			}
		}
	}

}
