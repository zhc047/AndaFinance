package com.icitymobile.anda.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import com.hualong.framework.log.Logger;
import com.hualong.framework.net.ResponseHelper;
import com.icitymobile.anda.bean.AndaResult;
import com.icitymobile.anda.bean.BankItem;
import com.icitymobile.anda.bean.PCItem;
import com.icitymobile.anda.bean.User;


/**
 * 省市银行相关服务中心
 * @author Sherry
 */
public class PCBServiceCenter {
	/**
	 * 获取省市列表
	 * @return
	 */

	public static AndaResult<List<PCItem>> getProvinceCity(String province) {
		AndaResult<List<PCItem>> result = null;
		String uri = String.format("/users/get_cities?province_id=%s", province);
		HttpResponse response = ServiceCenter.get(uri);
		if(response != null && response.getStatusLine().getStatusCode() == 200) {
			String resp = ResponseHelper.parseString(response);
			result = AndaResult.fromJson(resp);
			List<PCItem> PClist = PCItem.fromArray(resp);
			result.setData(PClist);
		}
		return result;
	}
	
	/**
	 * 获取银行列表
	 * @return
	 */

	public static AndaResult<List<BankItem>> getBank() {
		AndaResult<List<BankItem>> result = null;
		String uri = "/banks/get_bank_list" ;
		HttpResponse response = ServiceCenter.get(uri);
		if(response != null && response.getStatusLine().getStatusCode() == 200) {
			String resp = ResponseHelper.parseString(response);
			result = AndaResult.fromJson(resp);
			List<BankItem> BankList = BankItem.fromArray(resp);
			result.setData(BankList);
		}
		return result;
	}
	
}
