package com.icitymobile.anda.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hualong.framework.log.Logger;



/**
 * 会员
 * @author Sherry
 */
public class User implements Serializable {
	//TODO
	private static final long serialVersionUID = -5286096859502269561L;
	// 编号
	private String token;
	// 验证码
	private String code;
	// 姓名
	private String name;
	// 性别
	private String gender;
	// 联系电话
	private String phone;
	// 证件号码
	private String IDCardNumber;
	// 手机号码
	private String reservedPhone;
	// 邮箱
	private String email;
	// 所在城市
	private String city;
	// 借记卡卡号
	private String debitCardNumber;
	// 银行
	private String cardBank;
	// 持卡人
	private String cardHolder;
	// 登录结果
	private String loginResult;
	
	// 账号
	private String username;
	private String password;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getReservedPhone() {
		return reservedPhone;
	}
	public void setReservedPhone(String reservedPhone) {
		this.reservedPhone = reservedPhone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDebitCardNumber() {
		return debitCardNumber;
	}
	public void setDebitCardNumber(String debitCardNumber) {
		this.debitCardNumber = debitCardNumber;
	}
	public String getIDCardNumber() {
		return IDCardNumber;
	}
	public void setIDCardNumber(String IDCardNumber) {
		this.IDCardNumber = IDCardNumber;
	}	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCardBank() {
		return cardBank;
	}
	public void setCardBank(String cardBank) {
		this.cardBank = cardBank;
	}
	public String getCardHolder() {
		return cardHolder;
	}
	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLoginResult() {
		return loginResult;
	}
	public void setLoginResult(String loginResult) {
		this.loginResult = loginResult;
	}

	/**
	 * 将JSON数据转为User对象
	 * @param json
	 * @return
	 */
	
	public static User fromJson(String json) {
		User result = null;
		try {
			JSONObject obj = new JSONObject(json);
			result = fromJson(obj.getJSONObject("info"));
		} catch (Exception e) {
			Logger.e("", "", e);
		}
		return result;
	}

	public static User fromJson(JSONObject obj) {
		User result = null;
		try {
			result = new User();
			result.token = obj.optString("authentication_token");
			result.username = obj.optString("username");
			result.phone = obj.optString("phone");
			result.name = obj.optString("real_name");
			result.IDCardNumber = obj.optString("idcard");
			result.city = obj.optString("city");
			result.debitCardNumber = obj.optString("debit_card_number");
			result.reservedPhone = obj.optString("bank_reserve_phone_number");
			result.gender = obj.optString("gender");
			result.cardBank = obj.optString("card_bank");
			result.cardHolder = obj.optString("cardholder");
		} catch (Exception e) {
			Logger.e("", "", e);
		}
		return result;
	}
}