/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.encoder.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appcel.core.encoder.manager.EncoderInfoManager;
import com.appcel.facade.encoder.enums.EncoderPlayModeEnum;
import com.appcel.facade.encoder.enums.EncoderStatusEnum;
import com.appcel.facade.encoder.mode.Configure;
import com.appcel.facade.encoder.mode.EncoderInfo;
import com.appcel.facade.encoder.service.EncoderInfoService;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-12
 */
@Service("encoderInfoService")
public class EncoderInfoServiceImpl implements EncoderInfoService {

	@Autowired
	EncoderInfoManager encoderInfoManager;

	public EncoderInfo createEncoderInfo(String entityKey, String fileName, String srcFilePath, EncoderPlayModeEnum playMode,
			EncoderStatusEnum status, String remark) {
		return encoderInfoManager.createEncoderInfo(entityKey, fileName, srcFilePath, playMode, status, remark);
	}

	public EncoderInfo getEncoderInfoByKey(String key) {
		return encoderInfoManager.getEncoderInfoByKey(key);
	}

	public List<EncoderInfo> getEncoderInfoByEntityKey(String entityKey) {
		return encoderInfoManager.getEncoderInfoByEntityKey(entityKey);
	}

	public boolean deleteEncoderInfoByEntityKey(String entityKey) {
		return encoderInfoManager.deleteEncoderInfoByEntityKey(entityKey);
	}

	public boolean updateEncoderTime(String key) {
		return encoderInfoManager.updateEncoderTime(key);
	}

	public boolean deleteEncoderInfoByKey(String key) {
		return encoderInfoManager.deleteEncoderInfoByKey(key);
	}

	public Configure createConfigure(String name, String value, String remark) {
		return encoderInfoManager.createConfigure(name, value, remark);
	}

	public Configure findConfigureByKey(String key) {
		return encoderInfoManager.getConfigureByKey(key);
	}

	public List<Configure> getAllConfigure() {
		return encoderInfoManager.getAllConfigure();
	}

	public boolean deleteConfigureByKey(String key) {
		return encoderInfoManager.deleteConfigureByKey(key);
	}
}
