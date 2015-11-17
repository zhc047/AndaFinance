package com.icitymobile.anda.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.hualong.framework.LibConfig;
import com.hualong.framework.log.Logger;
import com.hualong.framework.net.HttpKit;
import com.hualong.framework.net.ResponseHelper;
/**
 * 服务中心
 * @author robot
 */
public class ServiceCenter {

	
	//服务器地址，HTTPS协议，不需要以“/”结尾
	private static final String SERVER = "http://anda.icitymobile.com";
	public static final String URL_FOR_API = SERVER + "/api/v1";
	
	/**
	 * 发送GET请求
	 * @param url
	 * @return
	 */
	public static HttpResponse get(String uri) {
		String url = URL_FOR_API + uri;
		Logger.d("", url);
		return HttpKit.get(url);
	}
	
	public static String getStringResp(String uri) {
		return ResponseHelper.parseString(get(uri));
	}
	
	/**
	 * 发送POST请求
	 * @param uri
	 * @param params
	 * @return
	 */
	public static HttpResponse post(String uri, Header[] headers, List<NameValuePair> params) {
		String url = URL_FOR_API + uri;
		if (params == null) {
			params = new ArrayList<NameValuePair>();
		}
		// 基本参数
		params.add(new BasicNameValuePair("sys", "Android"));
		//TODO version,model needed?
		params.add(new BasicNameValuePair("sys_version", android.os.Build.VERSION.RELEASE));
		params.add(new BasicNameValuePair("sys_model", android.os.Build.MODEL));
		HttpResponse response = HttpKit.postJson(url, headers, params);
		if(LibConfig.getDebug() && response != null) {
			Logger.d("", "----------------------------------------");
			Logger.d("", "Http Request:");
			Logger.d("", "----------------------------------------");
			Logger.d("", "Uri:" + uri);
			for (int i = 0; i < params.size(); i++) {
				Logger.d("", String.format("%s : %s", params.get(i).getName(), params.get(i).getValue()));
			}
			Logger.d("", "----------------------------------------");
			Logger.d("", "Http Response:");
			Logger.d("", "----------------------------------------");
			Logger.d("", "Code:" + response.getStatusLine().getStatusCode());
		}
		return response;
	}

}
