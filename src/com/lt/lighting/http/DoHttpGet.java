package com.lt.lighting.http;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class DoHttpGet {

	/**
	 * 
	 * @Title: doGetWork
	 * @Description:  Get请求
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String doGetWork(String url) throws Exception {
		String mResult = "";
		HttpGet httpGet = new HttpGet(url);
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
		DefaultHttpClient client = new DefaultHttpClient(httpParams);
		httpGet.setHeader("Connection", "Keep-Alive");
		BasicCookieStore cookieStore = new BasicCookieStore();
		client.setCookieStore(cookieStore);
		// 解决Invalid cookie header问题
		HttpClientParams.setCookiePolicy(client.getParams(),
				CookiePolicy.BROWSER_COMPATIBILITY);
		HttpResponse response = client.execute(httpGet);
		int status = response.getStatusLine().getStatusCode();
		if (status == HttpStatus.SC_OK) {
			mResult = EntityUtils.toString(response.getEntity(), "UTF-8");
		}

		return mResult;
	}
}
