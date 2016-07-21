/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.encoder.dao;

import java.util.List;

import com.appcel.facade.encoder.enums.EncoderPlayModeEnum;
import com.appcel.facade.encoder.enums.EncoderStatusEnum;
import com.appcel.facade.encoder.mode.EncoderInfo;
import com.appcel.kernel.mybatis.dao.BaseDao;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-11
 */
public interface EncoderInfoDao extends BaseDao<EncoderInfo> {

	EncoderInfo createEncoderInfo(String entityKey, String fileName, String srcFilePath, EncoderPlayModeEnum playMode,
			EncoderStatusEnum status, String remark);

	EncoderInfo findEncoderInfoByKey(String key);

	List<EncoderInfo> findEncoderInfoByEntityKey(String entityKey);

	boolean deleteEncoderInfoByEntityKey(String entityKey);

	boolean deleteEncoderInfoByKey(String key);

	boolean updateEncoderTime(String key);

	List<EncoderInfo> findEncoderInfoByStatus(EncoderStatusEnum encoderSatus);

	boolean updateEncoderStatus(String key, EncoderStatusEnum encoderSatus);

}