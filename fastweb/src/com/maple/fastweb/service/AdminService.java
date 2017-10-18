/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.maple.fastweb.service;


import java.util.List;

/**
 * Service - 管理员
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
public interface AdminService  {


	/**
	 * 判断管理员是否登录
	 * 
	 * @return 管理员是否登录
	 */
	boolean isAuthenticated();



	/**
	 * 获取当前登录用户名
	 * 
	 * @return 当前登录用户名，若不存在则返回null
	 */
	String getCurrentUsername();

	/**
	 * 获取登录令牌
	 * 
	 * @return 登录令牌
	 */
	String getLoginToken();

}