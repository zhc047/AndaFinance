package com.icitymobile.anda.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hualong.framework.log.Logger;
/**
 * province and city item
 * @author sherry
 *
 */
public class PCItem implements Serializable{

	private static final long serialVersionUID = -6737886673373928806L;
	private String city;
	private String cityId;
	private String hasChild;
	private String id;

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getHasChild() {
		return hasChild;
	}
	public void setHasChild(String hasChild) {
		this.hasChild = hasChild;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	
	public static PCItem fromJson(JSONObject obj) {
		PCItem result = null;
		try {
			result = new PCItem();
			result.city = obj.getString("city");
			result.cityId = obj.getString("city_id");
			result.hasChild = obj.getString("has_child");
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
	public static List<PCItem> fromArray(String json) {
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
	public static List<PCItem> fromArray(JSONArray array) {
		List<PCItem> result = null;
		try {
			result = new ArrayList<PCItem>();
			for (int i = 0; i < array.length(); i++) {
				result.add(fromJson(array.getJSONObject(i)));
			}
		} catch (Exception e) {
			Logger.e("", "", e);
		}
		return result;
	}

	
}
