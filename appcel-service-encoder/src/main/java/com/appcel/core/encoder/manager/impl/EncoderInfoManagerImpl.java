/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.encoder.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appcel.core.encoder.dao.ConfigureDao;
import com.appcel.core.encoder.dao.EncoderInfoDao;
import com.appcel.core.encoder.manager.EncoderInfoManager;
import com.appcel.facade.encoder.enums.EncoderPlayModeEnum;
import com.appcel.facade.encoder.enums.EncoderStatusEnum;
import com.appcel.facade.encoder.mode.Configure;
import com.appcel.facade.encoder.mode.EncoderInfo;
import com.appcel.kernel.mybatis.dao.BaseDao;
import com.appcel.kernel.mybatis.manager.BaseManagerImpl;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-12
 */
@Service("encoderInfoManager")
public class EncoderInfoManagerImpl extends BaseManagerImpl<EncoderInfo> implements EncoderInfoManager {

	@Autowired
	EncoderInfoDao encoderInfoDao;
	@Autowired
	ConfigureDao configureDao;

	public EncoderInfo createEncoderInfo(String entityKey, String fileName, String srcFilePath, EncoderPlayModeEnum playMode,
			EncoderStatusEnum status, String remark) {
		return encoderInfoDao.createEncoderInfo(entityKey, fileName, srcFilePath, playMode, status, remark);
	}

	public EncoderInfo getEncoderInfoByKey(String key) {
		return encoderInfoDao.findEncoderInfoByKey(key);
	}

	public List<EncoderInfo> getEncoderInfoByEntityKey(String entityKey) {
		return encoderInfoDao.findEncoderInfoByEntityKey(entityKey);
	}

	public List<EncoderInfo> getEncoderInfoByStatus(EncoderStatusEnum encoderSatus) {
		return encoderInfoDao.findEncoderInfoByStatus(encoderSatus);
	}

	public boolean deleteEncoderInfoByEntityKey(String entityKey) {
		return encoderInfoDao.deleteEncoderInfoByEntityKey(entityKey);
	}

	public boolean updateEncoderTime(String key) {
		return encoderInfoDao.updateEncoderTime(key);
	}

	public boolean updateEncoderStatus(String key, EncoderStatusEnum encoderSatus) {
		return encoderInfoDao.updateEncoderStatus(key, encoderSatus);
	}

	public boolean deleteEncoderInfoByKey(String key) {
		return encoderInfoDao.deleteEncoderInfoByKey(key);
	}

	public Configure createConfigure(String name, String value, String remark) {
		return configureDao.createConfigure(name, value, remark);
	}

	public Configure getConfigureByKey(String key) {
		return configureDao.findConfigureByKey(key);
	}

	public List<Configure> getAllConfigure() {
		return configureDao.listAllConfigure();
	}

	public boolean deleteConfigureByKey(String key) {
		return configureDao.deleteConfigureByKey(key);
	}

	public Map<String, Configure> getAllConfigureMap() {
		Map<String, Configure> configMap = new HashMap<String, Configure>();
		List<Configure> configures = configureDao.listAllConfigure();
		for (Configure configure : configures) {
			configMap.put(configure.getName(), configure);
		}
		return configMap;
	}

	@Override
	protected BaseDao<EncoderInfo> getDao() {

		return this.encoderInfoDao;
	}

}
