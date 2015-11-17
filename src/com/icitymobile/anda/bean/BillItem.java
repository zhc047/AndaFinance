package com.icitymobile.anda.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hualong.framework.log.Logger;

public class BillItem implements Serializable{	

	private static final long serialVersionUID = 5137964096863583917L;
	private String id;
	private String remark;
	private String payTime;
	private String amount;
	private String orderId;
	private String totalAmount;
	private String currentYearAmount;

	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getCurrentYearAmount() {
		return currentYearAmount;
	}
	public void setCurrentYearAmount(String currentYearAmount) {
		this.currentYearAmount = currentYearAmount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public static BillItem fromJson(JSONObject obj) {
		BillItem result = null;
		try {
			result = new BillItem();
			result.id = obj.optString("id");
			result.remark = obj.optString("remark");
			result.payTime = obj.optString("pay_time");
			result.amount = obj.optString("amount");
			result.orderId = obj.optString("order_id");
			result.currentYearAmount = obj.optString("current_year_amount");
			result.totalAmount = obj.optString("total_amount");
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
	public static List<BillItem> fromArray(String json) {
		try {
			JSONArray array = new JSONObject(json).getJSONArray("list");
			return fromArray(array);
		} catch (Exception e) {
			Logger.e("", "", e);
		}
		return null;
	}
	
	/**
	 * 将JSON数据转为BillItem对象
	 * @param json
	 * @return
	 */
	
	public static BillItem fromJson(String json) {
		BillItem result = null;
		try {
			JSONObject obj = new JSONObject(json);
			result = fromJson(obj.getJSONObject("info"));
		} catch (Exception e) {
			Logger.e("", "", e);
		}
		return result;
	}
	
	/**
	 * JSON，并返回资讯列表
	 * @param array
	 * @return
	 */
	public static List<BillItem> fromArray(JSONArray array) {
		List<BillItem> result = null;
		try {
			result = new ArrayList<BillItem>();
			for (int i = 0; i < array.length(); i++) {
				result.add(fromJson(array.getJSONObject(i)));
			}
		} catch (Exception e) {
			Logger.e("", "", e);
		}
		return result;
	}

}
