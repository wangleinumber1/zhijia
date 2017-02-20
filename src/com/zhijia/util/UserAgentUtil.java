package com.zhijia.util;

import android.webkit.WebSettings;
import android.webkit.WebView;

public class UserAgentUtil {
	/*获得useragent和设置useragent WebView webView=new WebView(this); WebSettings 
	 * websettings=webView.getSettings(); String user_agent=websettings.getUserAgentString();
	 * // websettings.setUserAgent(ua); Log.i("-----------", "user_agent--AAA-:"+user_agent);
	 *  websettings.setUserAgentString("wwwwwwwwwwwwwwwwwwww"); Log.i(
	 * "-----------", "user_agent--BBB-:"+websettings.getUserAgentString());
	 * */
	// 强行修改userAgent
	public static void userAgentCH(WebView wb) {
		// 修改
		WebSettings websettings = wb.getSettings();
		websettings.setUserAgentString(websettings.getUserAgentString()
				+ " zhijia.inc");
	}
}
