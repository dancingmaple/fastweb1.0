/*
 * Copyright 2005-2015 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package com.maple.fastweb.base.setting;

import com.maple.fastweb.base.pojo.Log;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Utils - 系统
 * 
 * @author SHOP++ Team
 * @version 4.0
 */
public final class SystemUtils {

	/** 日期格式配比 */
	public static final String[] DATE_PATTERNS = new String[] { "yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss" };

	/** config.xml文件路径 */
	public static final String SHOPXX_XML_PATH = "/config.xml";

	/** config.properties文件路径 */
	public static final String SHOPXX_PROPERTIES_PATH = "/config.properties";

	/** CacheManager */
	private static final CacheManager CACHE_MANAGER = CacheManager.create();

	/** BeanUtilsBean */
	private static final BeanUtilsBean BEAN_UTILS;

	static {
		ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean() {
			@Override
			public String convert(Object value) {
				if (value != null) {
					Class<?> type = value.getClass();
					if (type.isEnum() && super.lookup(type) == null) {
						super.register(new EnumConverter(type), type);
					} else if (type.isArray() && type.getComponentType().isEnum()) {
						if (super.lookup(type) == null) {
							ArrayConverter arrayConverter = new ArrayConverter(type, new EnumConverter(type.getComponentType()), 0);
							arrayConverter.setOnlyFirstToString(false);
							super.register(arrayConverter, type);
						}
						Converter converter = super.lookup(type);
						return ((String) converter.convert(String.class, value));
					}
				}
				return super.convert(value);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(String value, Class clazz) {
				if (clazz.isEnum() && super.lookup(clazz) == null) {
					super.register(new EnumConverter(clazz), clazz);
				}
				return super.convert(value, clazz);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(String[] values, Class clazz) {
				if (clazz.isArray() && clazz.getComponentType().isEnum() && super.lookup(clazz.getComponentType()) == null) {
					super.register(new EnumConverter(clazz.getComponentType()), clazz.getComponentType());
				}
				return super.convert(values, clazz);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(Object value, Class targetType) {
				if (super.lookup(targetType) == null) {
					if (targetType.isEnum()) {
						super.register(new EnumConverter(targetType), targetType);
					} else if (targetType.isArray() && targetType.getComponentType().isEnum()) {
						ArrayConverter arrayConverter = new ArrayConverter(targetType, new EnumConverter(targetType.getComponentType()), 0);
						arrayConverter.setOnlyFirstToString(false);
						super.register(arrayConverter, targetType);
					}
				}
				return super.convert(value, targetType);
			}
		};

		DateConverter dateConverter = new DateConverter();
		dateConverter.setPatterns(DATE_PATTERNS);
		convertUtilsBean.register(dateConverter, Date.class);
		BEAN_UTILS = new BeanUtilsBean(convertUtilsBean);
	}

	/**
	 * 不可实例化
	 */
	private SystemUtils() {
	}

	/**
	 * 获取系统设置(首先从缓存中读取，没有命中则去config.xml读取)
	 * 
	 * @return 系统设置
	 */
	@SuppressWarnings("unchecked")
	public static Setting getSetting() {
		Ehcache cache = CACHE_MANAGER.getEhcache(Setting.CACHE_NAME);
		String cacheKey = "setting";
		Element cacheElement = cache.get(cacheKey);
		Log.e("cache  " + cacheElement == null ? " not exist" : " exist");
		if (cacheElement == null) {
			Log.e("cache is null ,read from files");
			Setting setting = new Setting();
			try {
				File shopxxXmlFile = new ClassPathResource(SHOPXX_XML_PATH).getFile();
				Document document = new SAXReader().read(shopxxXmlFile);
				List<org.dom4j.Element> elements = document.selectNodes("/shopxx/setting");
				for (org.dom4j.Element element : elements) {
					try {
						String name = element.attributeValue("name");
						String value = element.attributeValue("value");
						BEAN_UTILS.setProperty(setting, name, value);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e.getMessage(), e);
					} catch (InvocationTargetException e) {
						throw new RuntimeException(e.getMessage(), e);
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (DocumentException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			cache.put(new Element(cacheKey, setting));
			cacheElement = cache.get(cacheKey);
		}
		return (Setting) cacheElement.getObjectValue();
	}

	/**
	 * 设置系统设置
	 * 
	 * @param setting
	 *            系统设置
	 */
	@SuppressWarnings("unchecked")
	public static void setSetting(Setting setting) {
		Assert.notNull(setting);
		Setting currentSetting = getSetting();
		try {
			File shopxxXmlFile = new ClassPathResource(SHOPXX_XML_PATH).getFile();
			Document document = new SAXReader().read(shopxxXmlFile);
			List<org.dom4j.Element> elements = document.selectNodes("/shopxx/setting");
			for (org.dom4j.Element element : elements) {
				try {
					String name = element.attributeValue("name");
					String value = BEAN_UTILS.getProperty(setting, name);
					if (null == value || "".equals(value)){
						// value = BEAN_UTILS.getProperty(currentSetting, name);
						continue;
					}
					Attribute attribute = element.attribute("value");
					attribute.setValue(value);
					BEAN_UTILS.setProperty(currentSetting, name,value);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}

			XMLWriter xmlWriter = null;
			try {
				OutputFormat outputFormat = OutputFormat.createPrettyPrint();
				outputFormat.setEncoding("UTF-8");
				outputFormat.setIndent(true);
				outputFormat.setIndent("	");
				outputFormat.setNewlines(true);
				xmlWriter = new XMLWriter(new FileOutputStream(shopxxXmlFile), outputFormat);
				xmlWriter.write(document);
				xmlWriter.flush();
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				try {
					if (xmlWriter != null) {
						xmlWriter.close();
					}
				} catch (IOException e) {
				}
			}
			Ehcache cache = CACHE_MANAGER.getEhcache(Setting.CACHE_NAME);
			String cacheKey = "setting";
			cache.put(new Element(cacheKey, currentSetting));
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (DocumentException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}


	/**
	 * 获取所有日志配置
	 * 
	 * @return 所有日志配置
	 */
	@SuppressWarnings("unchecked")
	public static List<LogConfig> getLogConfigs() {
		Ehcache cache = CACHE_MANAGER.getEhcache(LogConfig.CACHE_NAME);
		String cacheKey = "logConfigs";
		Element cacheElement = cache.get(cacheKey);
		if (cacheElement == null) {
			List<LogConfig> logConfigs = new ArrayList<LogConfig>();
			try {
				File shopxxXmlFile = new ClassPathResource(SHOPXX_XML_PATH).getFile();
				Document document = new SAXReader().read(shopxxXmlFile);
				List<org.dom4j.Element> elements = document.selectNodes("/shopxx/logConfig");
				for (org.dom4j.Element element : elements) {
					String operation = element.attributeValue("operation");
					String urlPattern = element.attributeValue("urlPattern");
					LogConfig logConfig = new LogConfig();
					logConfig.setOperation(operation);
					logConfig.setUrlPattern(urlPattern);
					logConfigs.add(logConfig);
				}
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (DocumentException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			cache.put(new Element(cacheKey, logConfigs));
			cacheElement = cache.get(cacheKey);
		}
		return (List<LogConfig>) cacheElement.getObjectValue();
	}



}