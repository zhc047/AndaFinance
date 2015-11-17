package com.icitymobile.anda.cache;

import com.hualong.framework.kit.FileKit;
import com.icitymobile.anda.MyApplication;
import com.icitymobile.anda.bean.AndaResult;
import com.icitymobile.anda.bean.User;
import com.icitymobile.anda.util.Const;

/**
 * 缓存数据中心
 * @author Sherry
 */
public class CacheCenter {
	
	/**
	 * 获取当前登录的用户
	 * @return
	 */
	public static User getCurrentUser() {
		Object obj = FileKit.getObject(MyApplication.getInstance(), Const.CACHE_OBJ_USER);
		if(obj != null) {
			return (User)obj;
		}
		return null;
	}
	
	/**
	 * 缓存用户数据
	 * @param user
	 * @return
	 */
	public static boolean cacheCurrentUser(User user) {
		return FileKit.save(MyApplication.getInstance(), user, Const.CACHE_OBJ_USER);
	}
	
	/**
	 * 清除用户数据
	 * @return
	 */
	public static boolean removeCurrentUser() {
		return FileKit.remove(MyApplication.getInstance(), Const.CACHE_OBJ_USER);
	}
}
