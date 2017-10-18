/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.maple.fastweb.service.impl;


import com.maple.fastweb.service.RSAService;
import com.maple.fastweb.util.shopxx.RSAUtils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Service - RSA安全
 * 对重要的参数进行加密，比如登录密码等，其他则不进行，影响效率
 * @author SHOP++ Team
 * @version 4.0
 */
@Service("rsaServiceImpl")
public class RSAServiceImpl implements RSAService {

	/** 密钥大小 */
	private static final int KEY_SIZE = 1024;

	/** "私钥"属性名称 */
	private static final String PRIVATE_KEY_ATTRIBUTE_NAME = "privateKey";

	@Override
    @Transactional(readOnly = true,rollbackFor = Exception.class)
	public RSAPublicKey generateKey(HttpServletRequest request) {
		Assert.notNull(request,"");

		KeyPair keyPair = RSAUtils.generateKeyPair(KEY_SIZE);
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		request.getSession().setAttribute(PRIVATE_KEY_ATTRIBUTE_NAME, privateKey);
		return publicKey;
	}

	@Override
    @Transactional(readOnly = true,rollbackFor = Exception.class)
	public void removePrivateKey(HttpServletRequest request) {
		Assert.notNull(request,"");

		request.getSession().removeAttribute(PRIVATE_KEY_ATTRIBUTE_NAME);
	}

	@Override
    @Transactional(readOnly = true,rollbackFor = Exception.class)
	public String decryptParameter(String name, HttpServletRequest request) {
		Assert.notNull(request,"");

		if (!StringUtils.isEmpty(name)) {
			return null;
		}
		RSAPrivateKey privateKey = (RSAPrivateKey) request.getSession().getAttribute(PRIVATE_KEY_ATTRIBUTE_NAME);
		String parameter = request.getParameter(name);
		if (privateKey != null && (!StringUtils.isEmpty(parameter))) {
			try {
				return new String(RSAUtils.decrypt(privateKey, Base64.decodeBase64(parameter)));
			} catch (RuntimeException e) {
				return null;
			}
		}
		return null;
	}

}