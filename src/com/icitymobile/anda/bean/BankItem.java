package com.icitymobile.anda.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hualong.framework.log.Logger;
/**
 * Bank Item
 * @author sherry
 *
 */
public class BankItem implements Serializable{

	private static final long serialVersionUID = 2666715929011646780L;
	private String bank;
	private String id;
	
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public static BankItem fromJson(JSONObject obj) {
		BankItem result = null;
		try {
			result = new BankItem();
			result.bank = obj.getString("bank_name");
			result.id = obj.getString("id");
		} catch (Exception e) {
			Logger.e("", "", e);
		}
		return result;
	}
	/**
	 * 字符串转JSON，并返回资讯列表
	 * @param json
	 * @return
	 */
	public static List<BankItem> fromArray(String json) {
		try {
			JSONArray array = new JSONObject(json).getJSONArray("list");
			return fromArray(array);
		} catch (Exception e) {
			Logger.e("", "", e);
		}
		return null;
	}
	
	/**
	 * JSON，并返回资讯列表
	 * @param array
	 * @return
	 */
	public static List<BankItem> fromArray(JSONArray array) {
		List<BankItem> result = null;
		try {
			result = new ArrayList<BankItem>();
			for (int i = 0; i < array.length(); i++) {
				result.add(fromJson(array.getJSONObject(i)));
			}
		} catch (Exception e) {
			Logger.e("", "", e);
		}
		return result;
	}

	
}
