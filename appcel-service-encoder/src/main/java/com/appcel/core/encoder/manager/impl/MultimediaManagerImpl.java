/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.encoder.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appcel.core.encoder.dao.MultimediaDao;
import com.appcel.core.encoder.manager.MultimediaManager;
import com.appcel.facade.encoder.mode.AudioInfo;
import com.appcel.facade.encoder.mode.Multimedia;
import com.appcel.facade.encoder.mode.VideoInfo;
import com.appcel.kernel.mybatis.dao.BaseDao;
import com.appcel.kernel.mybatis.manager.BaseManagerImpl;

/**
 *  
 * Defiend class file the MultimediaManagerImpl.java
 * 
 *  
 * @author Rock.Lee
 * @version appcel 1.0.0 
 * @since JDK-1.7.0
 * @date 2014-12-9
 */
@Service("multimediaManager")
public class MultimediaManagerImpl extends BaseManagerImpl<Multimedia> implements MultimediaManager {

	@Autowired
	MultimediaDao multimediaDao;

	public Multimedia createMultimedia(String entityKey, String duration, Long timelen, String type, String fps, String startTime,
			AudioInfo audioInfo, VideoInfo videoInfo) {
		return multimediaDao.createMultimedia(entityKey, duration, timelen, type, fps, startTime, audioInfo, videoInfo);

	}

	public Multimedia getMultimediaByKey(String key) {
		checkParameter(key, "multimedia key");
		return multimediaDao.findMultimediaByKey(key);
	}

	public List<Multimedia> getMultimediaByEntityKey(String entityKey) {
		checkParameter(entityKey, "Multimedia relation entityKey");
		return multimediaDao.findMultimediaByEntityKey(entityKey);
	}

	public boolean deleteMultimedaByEntityKey(String entityKey) {
		checkParameter(entityKey, "Multimedia relation entityKey");
		return multimediaDao.deleteMultimedaByEntityKey(entityKey);
	}

	protected BaseDao<Multimedia> getDao() {
		return this.multimediaDao;
	}
}
