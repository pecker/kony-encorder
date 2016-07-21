/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.encoder.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.appcel.core.encoder.dao.EncoderInfoDao;
import com.appcel.facade.encoder.enums.EncoderPlayModeEnum;
import com.appcel.facade.encoder.enums.EncoderStatusEnum;
import com.appcel.facade.encoder.mode.EncoderInfo;
import com.appcel.kernel.mybatis.dao.BaseDaoImpl;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-11
 */
@Repository("encoderInfoDao")
public class EncoderInfoDaoImpl extends BaseDaoImpl<EncoderInfo> implements EncoderInfoDao {

	public EncoderInfo createEncoderInfo(String entityKey, String fileName, String srcFilePath, EncoderPlayModeEnum playMode,
			EncoderStatusEnum status, String remark) {
		EncoderInfo encoderInfo = new EncoderInfo(entityKey, fileName, srcFilePath, playMode, status, remark);
		String key = save(encoderInfo);
		encoderInfo.setKey(key);
		return encoderInfo;
	}

	public EncoderInfo findEncoderInfoByKey(String key) {
		EncoderInfo encoderInfo = findByKey(key);
		return encoderInfo;
	}

	public List<EncoderInfo> findEncoderInfoByEntityKey(String entityKey) {
		return super.getSessionTemplate().selectList("listByEntityKey", entityKey);
	}

	public List<EncoderInfo> findEncoderInfoByStatus(EncoderStatusEnum encoderSatus) {
		Integer status = encoderSatus.getValue();
		return super.getSessionTemplate().selectList("listByStatus", status);
	}

	public boolean deleteEncoderInfoByEntityKey(String entityKey) {
		List<EncoderInfo> encoderInfos = findEncoderInfoByEntityKey(entityKey);
		if (null != encoderInfos && !encoderInfos.isEmpty()) {
			for (EncoderInfo encoderInfo : encoderInfos) {
				deleteByKey(encoderInfo.getKey());
			}
			return true;
		}
		return false;
	}

	public boolean updateEncoderTime(String key) {
		EncoderInfo encoderInfo = findByKey(key);
		if (encoderInfo != null) {
			encoderInfo.setEncoderTime(new Date());
			update(encoderInfo);
			return true;
		}
		return false;
	}

	public boolean updateEncoderStatus(String key, EncoderStatusEnum encoderSatus) {
		EncoderInfo encoderInfo = findByKey(key);
		if (encoderInfo != null) {
			encoderInfo.setStatus(encoderSatus.getValue());
			update(encoderInfo);
			return true;
		}
		return false;
	}

	public boolean deleteEncoderInfoByKey(String key) {
		EncoderInfo encoderInfo = findByKey(key);
		if (encoderInfo != null) {
			deleteByKey(encoderInfo.getKey());
			return true;
		}
		return false;
	}
}
