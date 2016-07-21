/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.encoder.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.appcel.core.encoder.dao.ConfigureDao;
import com.appcel.facade.encoder.mode.Configure;
import com.appcel.kernel.mybatis.dao.BaseDaoImpl;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-11
 */
@Repository("configureDao")
public class ConfigureDaoImpl extends BaseDaoImpl<Configure> implements ConfigureDao {

	public Configure createConfigure(String name, String value, String remark) {
		Configure configure = new Configure(name, value, remark);
		String key = insert(configure);
		configure.setKey(key);
		return configure;
	}

	public Configure findConfigureByKey(String key) {
		Configure encoderInfo = findByKey(key);
		return encoderInfo;
	}

	public List<Configure> listAllConfigure() {
		return super.listAll();
	}

	public boolean deleteConfigureByKey(String key) {
		Configure configure = findByKey(key);
		if (configure != null) {
			deleteByKey(configure.getKey());
			return true;
		}
		return false;
	}
}
