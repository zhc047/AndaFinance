package com.icitymobile.anda.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.hualong.framework.net.ResponseHelper;
import com.icitymobile.anda.bean.AndaResult;
import com.icitymobile.anda.bean.User;
/**
 * 短信接口
 * @author Sherry
 */
public class AmsServiceCenter {
	/**
	 * 发送短信验证码服务
	 * @param phone
	 */
	public static AndaResult<Void> getMessageCode(String phone) {
		AndaResult<Void> result = null;
		 String uri = String.format("/users/get_valid_code?phone=%s", phone);
		 HttpResponse response = ServiceCenter.get(uri);
		 if(response != null && response.getStatusLine().getStatusCode() == 200) {
			 String resp = ResponseHelper.parseString(response);
			 result = AndaResult.fromJson(resp);
		 }
		 return result;
	}
}
