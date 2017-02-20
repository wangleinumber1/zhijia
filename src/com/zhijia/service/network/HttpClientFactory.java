package com.zhijia.service.network;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

/**
 * 客户端工厂
 */
public class HttpClientFactory {
	
	public static final HttpClientFactory INSTANCE = new HttpClientFactory();
	private HttpParams params;
	private SchemeRegistry registry;
	private static final String Tag = "HttpClientFactory";
	private HttpClientFactory() {
		params = new BasicHttpParams();
		// 每个路由的最大连接数 ，默认最大是20
		ConnManagerParams.setMaxTotalConnections(params, 100);
		// 每个路由初始化的连接数 默认最大是2个
		ConnPerRouteBean connPerRoute = new ConnPerRouteBean(10);
		ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		HttpConnectionParams.setSoTimeout(params, 50000);
		registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
	}
	public HttpClient createHttpClient() {
		ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
				registry);
		return new DefaultHttpClient(ccm, params);
	}
}
