package com.icitymobile.anda.bean;

import java.io.Serializable;

import org.json.JSONObject;

import com.hualong.framework.log.Logger;

/**
 * 安达请求结果
 * @author robot
 */
public class AndaResult<T> implements Serializable {
	
	private String code;
	
	private String message;
	
	private String token;
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	private T data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	/**
	 * 判断操作是否成功
	 * @return
	 */
	public boolean isSucceed() {
		return "1".equals(code);
	}
	
	/**
	 * 将JSON数据转为对象
	 * @param json
	 * @return
	 */
	public static AndaResult fromJson(String json) {
		AndaResult result = null;
		try {
			JSONObject obj = new JSONObject(json);
			result = new AndaResult();
			result.code = obj.optString("code");
			result.message = obj.optString("message");
			result.token = obj.optString("authentication_token");
		} catch (Exception e) {
			Logger.e("", "", e);
		}
		return result;
	}


}
