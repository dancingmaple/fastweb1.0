/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.maple.fastweb.base.shiro;

import com.maple.fastweb.base.pojo.Log;
import com.maple.fastweb.po.Principal;
import com.maple.fastweb.service.AdminService;
import com.maple.fastweb.service.CaptchaService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 权限认证
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
public class SimpleAuthenticationRealm extends AuthorizingRealm {

	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	/**
	 * 获取认证信息
	 * 
	 * @param token
	 *            令牌
	 * @return 认证信息
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
		SimpleAuthenticationToken authenticationToken = (SimpleAuthenticationToken) token;
		String username = authenticationToken.getUsername();
		String password = new String(authenticationToken.getPassword());
		String captchaId = authenticationToken.getCaptchaId();
		String captcha = authenticationToken.getCaptcha();
		String ip = authenticationToken.getHost();
		boolean remeberme = authenticationToken.isRememberMe();
		if (!captchaService.isValid(CaptchaService.CaptchaType.adminLogin, captchaId, captcha)) {
			Log.e("验证码验证失败");
			throw new IncorrectCaptchaException();
		}
		Log.e("验证码验证成功");
		if (username != null && password != null) {
			return new SimpleAuthenticationInfo(new Principal(1L, username), password, getName());
		}
		throw new UnknownAccountException();
	}

	/**
	 * 获取授权信息
	 * 
	 * @param principalCollection
	 *            PrincipalCollection
	 * @return 授权信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		Principal principal = (Principal) principalCollection.fromRealm(getName()).iterator().next();
		if (principal != null) {
			List<String> authorities = new ArrayList<>();
			authorities.add("admin:"+principal.getUsername());
			if (authorities != null) {
				SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
				authorizationInfo.addStringPermissions(authorities);
				return authorizationInfo;
			}
		}
		return null;
	}

}