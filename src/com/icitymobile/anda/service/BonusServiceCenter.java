package com.icitymobile.anda.service;

import java.util.List;

import org.apache.http.HttpResponse;

import com.hualong.framework.net.ResponseHelper;
import com.icitymobile.anda.bean.AndaResult;
import com.icitymobile.anda.bean.BillItem;

public class BonusServiceCenter {
	
	/**
	 * 获取我的全部奖励列表
	 * @param page			页码
	 * @param user_token	用户
	 */
	public static AndaResult<List<BillItem>> getBillList(int page, String user_token) {
		AndaResult<List<BillItem>> result = null;
		String uri = String.format("/bonus/list?page=%s&user_token=%s", page, user_token);
		HttpResponse response = ServiceCenter.get(uri);
		if(response != null && response.getStatusLine().getStatusCode() == 200) {
			String resp = ResponseHelper.parseString(response);
			result = AndaResult.fromJson(resp);
			List<BillItem> BillList = BillItem.fromArray(resp);
			result.setData(BillList);
		}
		return result;
	}
	
	/**
	 * 获取总的奖励数额
	 * @param user_token	用户
	 */
	public static AndaResult<BillItem> getTotalAmount(String user_token) {
		AndaResult<BillItem> result = null;
		String uri = String.format("/bonus/total_bonus?user_token=%s", user_token);
		HttpResponse response = ServiceCenter.get(uri);
		if(response != null && response.getStatusLine().getStatusCode() == 200) {
			String resp = ResponseHelper.parseString(response);
			result = AndaResult.fromJson(resp);
			BillItem Bill = BillItem.fromJson(resp);
			result.setData(Bill);
		}
		return result;
	}

	/**
	 * 获取我的奖励详情
	 * @param order_id		订单号
	 * @param user_token	用户
	 */
	public static AndaResult<List<BillItem>> getBillDetail(String order_id, String user_token) {
		AndaResult<List<BillItem>> result = null;
		String uri = String.format("/bonus/detail?id=%s&user_token=%s", order_id, user_token);
		HttpResponse response = ServiceCenter.get(uri);
		if(response != null && response.getStatusLine().getStatusCode() == 200) {
			String resp = ResponseHelper.parseString(response);
			result = AndaResult.fromJson(resp);
			List<BillItem> BillList = BillItem.fromArray(resp);
			result.setData(BillList);
		}
		return result;
	}
}
