/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.encoder.dao;

import java.util.List;

import com.appcel.facade.encoder.mode.Configure;
import com.appcel.kernel.mybatis.dao.BaseDao;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-12
 */
public interface ConfigureDao extends BaseDao<Configure> {

	Configure createConfigure(String name, String value, String remark);

	Configure findConfigureByKey(String key);

	List<Configure> listAllConfigure();

	boolean deleteConfigureByKey(String key);

}