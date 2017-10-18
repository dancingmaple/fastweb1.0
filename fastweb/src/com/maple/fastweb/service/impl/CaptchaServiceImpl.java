/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.maple.fastweb.service.impl;


import com.maple.fastweb.service.CaptchaService;
import com.octo.captcha.service.CaptchaServiceException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;

/**
 * Service - 验证码
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
@Service("captchaServiceImpl")
public class CaptchaServiceImpl implements CaptchaService {

	@Resource(name = "imageCaptchaService")
	private com.octo.captcha.service.CaptchaService imageCaptchaService;

	@Override
    public BufferedImage buildImage(String captchaId) {
		Assert.hasText(captchaId,"");
		Object o = imageCaptchaService.getChallengeForID(captchaId);
		return (BufferedImage) o ;
	}

	@Override
    public boolean isValid(CaptchaType captchaType, String captchaId, String captcha) {
	//	Assert.notNull(captchaType);

		/*Setting setting = SystemUtils.getSetting();
		if (!ArrayUtils.contains(setting.getCaptchaTypes(), captchaType)) {
			return true;
		}*/
		if (StringUtils.isEmpty(captchaId) || StringUtils.isEmpty(captcha)) {
			return false;
		}
		try {
			boolean v = imageCaptchaService.validateResponseForID(captchaId, captcha.toUpperCase());
			return v ;
		} catch (CaptchaServiceException e) {
			return false;
		}
	}

}