package com.icitymobile.anda.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.hualong.framework.encode.YLBase64;
import com.hualong.framework.net.ResponseHelper;
import com.icitymobile.anda.bean.AndaResult;
import com.icitymobile.anda.bean.User;

/**
 * 会员相关服务中心
 * @author Sherry
 */
public class MemberServiceCenter {
	
	/**
	 * 用户登录
	 * @param username
	 * @param password
	 */
	public static AndaResult<User> login(String username, String password) {
		AndaResult<User> result = null;
		 String uri = String.format("/users/login?username=%s&password=%s", YLBase64.encode(username), 
				      YLBase64.encode(password));
		 HttpResponse response = ServiceCenter.get(uri);
		 if(response != null && response.getStatusLine().getStatusCode() == 200) {
			 String resp = ResponseHelper.parseString(response);
			 result = AndaResult.fromJson(resp);
		
			try {
				User user = User.fromJson(new JSONObject(resp));
				result.setData(user);
			} catch (JSONException e) {
				e.printStackTrace();
			}	
		 }
		 return result;
	}
	
	/**
	 * 查询会员信息
	 * @param user_token
	 */
	public static AndaResult<User> getUserInfo(String token) {
		AndaResult<User> result = null;
		 String uri = String.format("/users/detail_info?user_token=%s", token);
		 HttpResponse response = ServiceCenter.get(uri);
		 if(response != null && response.getStatusLine().getStatusCode() == 200) {
			 String resp = ResponseHelper.parseString(response);
			 result = AndaResult.fromJson(resp);
			 User user = User.fromJson(resp);
			 result.setData(user);
		 }
		 return result;
	}
	
	/**
	 * 检查手机号是否已被注册
	 * @param phone
	 */
	public static AndaResult<Void> checkPhone(String phone) {
		AndaResult<Void> result = null;
		 String uri = String.format("/users/check_phone?phone=%s", phone);
		 HttpResponse response = ServiceCenter.get(uri);
		 if(response != null && response.getStatusLine().getStatusCode() == 200) {
			 String resp = ResponseHelper.parseString(response);
			 result = AndaResult.fromJson(resp);
		 }
		 return result;
	}
	
	/**
	 * 验证短信验证码
	 * @param phone
	 * @param code
	 */
	public static AndaResult<Void> checkMessageCode(String phone,String code) {
		AndaResult<Void> result = null;
		 String uri = String.format("/users/check_val_code?phone=%s&code=%s", phone,code);
		 HttpResponse response = ServiceCenter.get(uri);
		 if(response != null && response.getStatusLine().getStatusCode() == 200) {
			 String resp = ResponseHelper.parseString(response);
			 result = AndaResult.fromJson(resp);
		 }
		 return result;
	}
	
	/**
	 * 检测用户名是否可用
	 * @param username
	 */
	public static AndaResult<Void> checkUsername(String username) {
		AndaResult<Void> result = null;
		 String uri = String.format("/users/check_username?username=%s", username);
		 HttpResponse response = ServiceCenter.get(uri);
		 if(response != null && response.getStatusLine().getStatusCode() == 200) {
			 String resp = ResponseHelper.parseString(response);
			 result = AndaResult.fromJson(resp);
		 }
		 return result;
	}

	
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	public static AndaResult<User> userRegister(User user) {
		 String uri = "/users/register";
		 List<Header> headList = new ArrayList<Header>();
		 Header header = new BasicHeader("Content-Type", "application/json");
		 headList.add(header);
		 //头部
		 Header[] headers = new Header[headList.size()];
		 headers = headList.toArray(headers);
		
		 List<NameValuePair> params = new ArrayList<NameValuePair>();
		 params.add(new BasicNameValuePair("phone", user.getPhone()));
		 params.add(new BasicNameValuePair("code", user.getCode()));
		 params.add(new BasicNameValuePair("real_name", user.getName()));
		 params.add(new BasicNameValuePair("gender", user.getGender()));
		 params.add(new BasicNameValuePair("username", user.getUsername()));
		 params.add(new BasicNameValuePair("password", YLBase64.encode(user.getPassword())));
		 params.add(new BasicNameValuePair("idcard", user.getIDCardNumber()));
		 params.add(new BasicNameValuePair("city_id", user.getCity()));
		 params.add(new BasicNameValuePair("debit_card_number", user.getDebitCardNumber()));
		 params.add(new BasicNameValuePair("card_bank", user.getCardBank()));
		 params.add(new BasicNameValuePair("cardholder", user.getCardHolder()));
		 params.add(new BasicNameValuePair("bank_reserve_phone_number", user.getReservedPhone()));
		 HttpResponse response = ServiceCenter.post(uri, headers, params);
		 if(response != null && response.getStatusLine().getStatusCode() == 200) {
			 return AndaResult.fromJson(ResponseHelper.parseString(response));
		 }
		 return null;
	}
	
	/**
	 * 编辑会员信息
	 * @param user
	 * @return
	 */
	public static AndaResult<Void> userEdit(User user) {
		 String uri = "/users/edit";
		 List<Header> headList = new ArrayList<Header>();
		 Header header = new BasicHeader("Content-Type", "application/json");
		 headList.add(header);
		 String anthorizationToken = String.format("Token token=\"%s\"", user.getToken());
		 Header header2 = new BasicHeader("Authorization", anthorizationToken);
		 headList.add(header2);
		 //头部
		 Header[] headers = new Header[headList.size()];
		 headers = headList.toArray(headers);
		
		 List<NameValuePair> params = new ArrayList<NameValuePair>();
		 params.add(new BasicNameValuePair("real_name", user.getName()));
		 params.add(new BasicNameValuePair("gender", user.getGender()));
		 params.add(new BasicNameValuePair("idcard", user.getIDCardNumber()));
		 params.add(new BasicNameValuePair("city_id", user.getCity()));
		 params.add(new BasicNameValuePair("debit_card_number", user.getDebitCardNumber()));
		 params.add(new BasicNameValuePair("card_bank", user.getCardBank()));
		 params.add(new BasicNameValuePair("cardholder", user.getCardHolder()));
		 params.add(new BasicNameValuePair("bank_reserve_phone_number", user.getReservedPhone()));
		 HttpResponse response = ServiceCenter.post(uri, headers, params);
		 if(response != null && response.getStatusLine().getStatusCode() == 200) {
			 return AndaResult.fromJson(ResponseHelper.parseString(response));
		 }
		 return null;
	}

	
	/**
	 * 修改密码
	 * @param user_token	用户名
	 * @param old_password	原密码
	 * @param new_password	新密码
	 * @return
	 */
	public static AndaResult<Void> changePassword(String user_token, String old_password, String new_password) {
		AndaResult<Void> result = null;
		String uri = String.format("/users/change_password?user_token=%s&original_password=%s&new_password=%s", 
				      user_token,YLBase64.encode(old_password),YLBase64.encode(new_password));
		 HttpResponse response = ServiceCenter.get(uri);
		 if(response != null && response.getStatusLine().getStatusCode() == 200) {
			 String resp = ResponseHelper.parseString(response);
			 result = AndaResult.fromJson(resp);
		 }
		 return result;
	}
	
	/**
	 * 修改手机号
	 * @param user_token	用户名
	 * @param old_phone		原手机
	 * @param new_phone		新密码
	 * @param old_code		原验证码
	 * @param new_code		新验证码
	 * @return
	 */
	public static AndaResult<Void> changePhone(String user_token, String old_phone, String new_phone, 
			                                   String old_code, String new_code) {
		AndaResult<Void> result = null;
		String uri = String.format("/users/update_user_phone?user_token=%s&old_phone=%s&old_code=%s"
				+ "&new_phone=%s&new_code=%s", 
				      user_token,old_phone,new_phone,old_code,new_code);
		 HttpResponse response = ServiceCenter.get(uri);
		 if(response != null && response.getStatusLine().getStatusCode() == 200) {
			 String resp = ResponseHelper.parseString(response);
			 result = AndaResult.fromJson(resp);
		 }
		 return result;
	}

	/**
	 * 重置密码
	 * @param phone	手机号
	 * @param code	验证码
	 * @param password	新密码
	 * @return
	 */
	public static AndaResult<Void> resetPassword(String phone, String code, String password) {
		AndaResult<Void> result = null;
		String uri = String.format("/users/change_password_by_phone?phone=%s&valid_code=%s"
				+ "&new_password=%s", phone,code,YLBase64.encode(password));
		 HttpResponse response = ServiceCenter.get(uri);
		 if(response != null && response.getStatusLine().getStatusCode() == 200) {
			 String resp = ResponseHelper.parseString(response);
			 result = AndaResult.fromJson(resp);
		 }
		 return result;
	}

}
