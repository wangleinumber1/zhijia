package com.zhijia.service.network;

import android.util.Log;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.zhijia.Global;
import com.zhijia.service.data.Medol.JsonResult;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 连接网络
 */
public class HttpClientUtils {

    private static HttpClientUtils instance;

    private HttpClientUtils() {

    }

    public static HttpClientUtils getInstance() {
        if (null == instance) {
            instance = new HttpClientUtils();
        }
        return instance;
    }
    /**
     *
     * @param response
     * @param httpClient
     * @return null或sessionId串
     */
    private static String getAndWriteSessionId(HttpResponse response, HttpClient httpClient) {
        String returnStr = null;
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            CookieStore mCookieStore = ((DefaultHttpClient) httpClient).getCookieStore();
            List<Cookie> cookies = mCookieStore.getCookies();
            for (int i = 0; i < cookies.size(); i++) {
                if (Global.SESSION_ID_KEY.equals(cookies.get(i).getName())) {
                    Global.SESSION_ID = cookies.get(i).getValue();
                    returnStr = Global.SESSION_ID;
                    break;
                }
            }
        }
        Log.d(Global.LOG_TAG, "SESSION_ID returnStr=" + returnStr);
        Log.d(Global.LOG_TAG, "SESSION_ID=" + Global.SESSION_ID);
        return returnStr;
    }

    public <T> Optional<Map<String, T>> postFileSignedMap(String url, Map<String, Object> queryString, final Class<T> cls) {
        Function<ObjectMapper, JavaType> function = new Function<ObjectMapper, JavaType>() {
            @Override
            public JavaType apply(ObjectMapper mapper) {
                return mapper.getTypeFactory().constructMapType(Map.class, String.class, cls);
            }
        };

        HttpClient client = HttpClientFactory.INSTANCE.createHttpClient();
        Map<String, Object> paramMap = queryString == null ? Maps.<String, Object>newHashMap() : Maps.newHashMap(queryString);

        MultipartEntity entity = new MultipartEntity();

        try {
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                if (entry.getValue() instanceof String) {
                    StringBody stringBody = new StringBody((String) entry.getValue());
                    entity.addPart(entry.getKey(), stringBody);
                } else if (entry.getValue() instanceof File) {
                    FileBody fileBody = new FileBody((File) entry.getValue());
                    entity.addPart(entry.getKey(), fileBody);
                }
            }

            HttpPost post = new HttpPost(url);
            if (Global.SESSION_ID != null) {
                post.setHeader("Cookie", Global.SESSION_ID_KEY + "=" + Global.SESSION_ID);
            }
            post.setEntity(entity);       
            HttpResponse response = client.execute(post);
            getAndWriteSessionId(response, client);
            int statusCode = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity());
            if (statusCode == HttpStatus.SC_OK) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, T> jsonResult = mapper.readValue(body, function.apply(mapper));
                Log.d("postSigned", jsonResult.toString());
                return Optional.fromNullable(jsonResult);
            } else {
                return Optional.absent();
            }
        } catch (Exception e) {
            //如果异常直接返回空对象
            return Optional.absent();
        } finally {
            client.getConnectionManager().shutdown();
        }
    }

    public <T> Optional<Map<String, T>> postSignedMap(String url, Map<String, String> queryString, final Class<T> cls) {
        Function<ObjectMapper, JavaType> function = new Function<ObjectMapper, JavaType>() {
            @Override
            public JavaType apply(ObjectMapper mapper) {
                return mapper.getTypeFactory().constructMapType(Map.class, String.class, cls);
            }
        };

        HttpClient client = HttpClientFactory.INSTANCE.createHttpClient();
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        Map<String, String> paramMap = queryString == null ? Maps.<String, String>newHashMap() : Maps.newHashMap(queryString);
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        Log.d("HttpClientUtils postSignedMap->queryString",paramMap.toString()) ;

        HttpPost post = new HttpPost(url);
        if (Global.SESSION_ID != null) {
            post.setHeader("Cookie", Global.SESSION_ID_KEY + "=" + Global.SESSION_ID);
        }

        Log.d("HttpClientUtils",url) ;
        try {
            post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse response = client.execute(post);
            getAndWriteSessionId(response, client);

            int statusCode = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity());
            if (statusCode == HttpStatus.SC_OK) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, T> jsonResult = mapper.readValue(body, function.apply(mapper));
                Log.d("postSigned", jsonResult.toString());
                return Optional.fromNullable(jsonResult);
            } else {
                return Optional.absent();
            }
        } catch (Exception e) {
            //如果异常直接返回空对象
            return Optional.absent();
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
    //选择城市
    public <T> Optional<Map<String, T>> getUnsignedMap(String url,
    		Map<String,String> queryString, final Class<T> cls) {
        Function<ObjectMapper, JavaType> function = new Function<ObjectMapper, JavaType>() {
        @Override
        public JavaType apply(ObjectMapper mapper){
               JavaType mapType = mapper.getTypeFactory().constructMapType(Map.class, String.class, cls);
               return mapType;
           }
        };   
        HttpClient client = HttpClientFactory.INSTANCE.createHttpClient();
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        Map<String, String> paramMap = queryString == null ? Maps.<String, String>newHashMap():Maps.newHashMap(queryString);
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        String fullUrl = url + "?" + URLEncodedUtils.format(params,HTTP.UTF_8);
        Log.d("HttpClientUtils-->getUnsignedMap", fullUrl);
        HttpGet get = new HttpGet(fullUrl); 
        //get.getParams().setIntParameter(CoreProtocolPNames.WAIT_FOR_CONTINUE,100);
        if (Global.SESSION_ID != null) {
            get.setHeader("Cookie", Global.SESSION_ID_KEY + "=" + Global.SESSION_ID);
        }else {
			System.out.println("Cookie:session_id=null");
		}
        try {        	                
            HttpResponse response = client.execute(get);
            getAndWriteSessionId(response, client);
            int statusCode = response.getStatusLine().getStatusCode();          
            if (statusCode == HttpStatus.SC_OK) {
            	String body = EntityUtils.toString(response.getEntity());
                System.out.println(body);
                ObjectMapper mapper = new ObjectMapper();
                Map<String, T> jsonResult = mapper.readValue(body, function.apply(mapper));
                Log.d("return getUnsignedMap", jsonResult.toString());
                return Optional.fromNullable(jsonResult);
            }else {
				System.out.println("MapCity absent null");
                return Optional.absent();
            }            
        } catch (Exception e) {
            Log.e("httpclient-------->>>", e.getMessage());
            return Optional.absent();
        } finally {
            client.getConnectionManager().shutdown();
        }

    }


    public <T> Optional<T> getUnsigned(String url,
    		Map<String, String> queryString, final Class<T> cls) {
        HttpClient client = HttpClientFactory.INSTANCE.createHttpClient();
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        Map<String, String> paramMap = queryString == null ? Maps.<String, String>newHashMap() : Maps.newHashMap(queryString);
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        String fullUrl = url + "?" + URLEncodedUtils.format(params, HTTP.UTF_8);
        Log.d("HttpClientUtils-->getUnsigned", fullUrl);
        HttpGet get = new HttpGet(fullUrl);
        if (Global.SESSION_ID != null) {
            get.setHeader("Cookie", Global.SESSION_ID_KEY + "=" + Global.SESSION_ID);
            }
        try {
            HttpResponse response = client.execute(get);
            getAndWriteSessionId(response, client);

            int statusCode = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity());
            if (statusCode == HttpStatus.SC_OK) {
                ObjectMapper mapper = new ObjectMapper();
                T jsonResult = mapper.readValue(body, cls);
                return Optional.fromNullable(jsonResult);
            } else {
                return Optional.absent();
            }
        } catch (Exception e) {
            //如果异常直接返回空对象
            return Optional.absent();
        } finally {
            client.getConnectionManager().shutdown();
        }
    }

    public <T> Optional<JsonResult<List<T>>> getUnsignedListByData(String url,
    		Map<String, String> queryString, final Class<T> cls) {
        return getByData(url, queryString, new Function<ObjectMapper, JavaType>() {
            @Override
            public JavaType apply(ObjectMapper mapper) {
                JavaType listType = mapper.getTypeFactory().constructCollectionType(List.class, cls);
                return mapper.getTypeFactory().constructParametricType(JsonResult.class, listType);
            }
        });
    }

    public <T> Optional<JsonResult<List<T>>> postUnsignedListByData(String url,
    		Map<String, String> queryString, final Class<T> cls) {
        return postByData(url, queryString, new Function<ObjectMapper, JavaType>() {
            @Override
            public JavaType apply(ObjectMapper mapper) {
                JavaType listType = mapper.getTypeFactory().constructCollectionType(List.class, cls);
                return mapper.getTypeFactory().constructParametricType(JsonResult.class, listType);
            }
        });
    }

    public <T> Optional<JsonResult<T>> getUnsignedByData(String url, 
    		Map<String, String> queryString, final Class<T> cls) {

        return getByData(url, queryString, new Function<ObjectMapper, JavaType>() {
            @Override
            public JavaType apply(ObjectMapper mapper) {
            	//http://www.cnblogs.com/quanyongan/archive/2013/04/16/3024993.html
                return mapper.getTypeFactory().constructParametricType(JsonResult.class, cls);
            }
        });
    }

    private <T> Optional<JsonResult<T>> getByData(String url,
    		Map<String, String> queryString, Function<ObjectMapper, JavaType> javaTypeFactory) {
        HttpClient client = HttpClientFactory.INSTANCE.createHttpClient();
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        Map<String, String> paramMap = queryString == null ? Maps.<String, String>newHashMap() : Maps.newHashMap(queryString);
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        String fullUrl = url + "?" + URLEncodedUtils.format(params, HTTP.UTF_8);
        Log.d("HttpClientUtils-->getByData--->fullUrl：", fullUrl);
        HttpGet get = new HttpGet(fullUrl);
        System.out.println("请求信息:"+get.getRequestLine());
        if (Global.SESSION_ID != null) {
            get.setHeader("Cookie", Global.SESSION_ID_KEY + "=" + Global.SESSION_ID);
           }
       try {	   
            HttpResponse response = client.execute(get);
            System.out.println("相应信息"+response.getStatusLine());
            getAndWriteSessionId(response, client); 
            int statusCode = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity());
            if (statusCode == HttpStatus.SC_OK) {
            	Log.d("getByData", "没有出异常 有返回值");
                ObjectMapper mapper = new ObjectMapper();
                //由于对方接口出现了tagtext这个属于是list，但是server在没有数据的情况下返回的是一个字符串，有些崩溃，所以需要用这个属性表示对应
                mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,true);
                JsonResult<T> jsonResult = mapper.readValue(body, javaTypeFactory.apply(mapper));
                return Optional.fromNullable(jsonResult);
            }else if(statusCode==HttpStatus.SC_MOVED_PERMANENTLY) {
				System.out.println("重定向");
				return Optional.absent();
			} else {
            	get.abort();
                Log.d("getByData", "没有出异常 返回是空");
                return Optional.absent();
            }
        } catch (Exception e) {
            //Log.d("HttpClientUtils->exception","exception:"+e.getMessage());  
            return Optional.absent();
        } finally {
            client.getConnectionManager().shutdown();
        }
    }

    private <T> Optional<JsonResult<T>> postByData(String url, Map<String, String> queryString,
    		Function<ObjectMapper, JavaType> javaTypeFactory) {
        HttpClient client = HttpClientFactory.INSTANCE.createHttpClient();
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        Map<String, String> paramMap = queryString == null ? Maps.<String, String>newHashMap() : Maps.newHashMap(queryString);
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        HttpPost post = new HttpPost(url);
        if (Global.SESSION_ID != null) {
            post.setHeader("Cookie", Global.SESSION_ID_KEY + "=" + Global.SESSION_ID);
            }
        try {
            post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse response = client.execute(post);
            getAndWriteSessionId(response, client);
            int statusCode = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity());
            if (statusCode == HttpStatus.SC_OK) {
            	Log.d("getByData", "没有异常,有返回值");
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,true);
                JsonResult<T> jsonResult = mapper.readValue(body, javaTypeFactory.apply(mapper));        
                return Optional.fromNullable(jsonResult);
            } else {
                Log.d("getByData", "没有出异常 返回是空");
                return Optional.absent();
            }
        } catch (Exception e) {
            //Log.d("HttpClientUtils->exception", e.getMessage());     
            return Optional.absent();
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
}
