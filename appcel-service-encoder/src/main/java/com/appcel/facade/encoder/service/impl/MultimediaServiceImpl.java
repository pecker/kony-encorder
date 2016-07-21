/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.encoder.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appcel.core.encoder.manager.MultimediaManager;
import com.appcel.facade.encoder.mode.AudioInfo;
import com.appcel.facade.encoder.mode.Multimedia;
import com.appcel.facade.encoder.mode.VideoInfo;
import com.appcel.facade.encoder.service.MultimediaService;

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
@Service("multimediaService")
public class MultimediaServiceImpl implements MultimediaService {

	@Autowired
	MultimediaManager multimediaManager;

	public Multimedia createMultimedia(String entityKey, String duration, Long timelen, String type, String fps, String startTime,
			AudioInfo audioInfo, VideoInfo videoInfo) {
		return multimediaManager.createMultimedia(entityKey, duration, timelen, type, fps, startTime, audioInfo, videoInfo);
	}

	public Multimedia getMultimediaByKey(String key) {
		return multimediaManager.getMultimediaByKey(key);
	}

	public List<Multimedia> getMultimediaByEntityKey(String entityKey) {
		return multimediaManager.getMultimediaByEntityKey(entityKey);
	}

	public boolean deleteMultimedaByEntityKey(String entityKey) {
		return multimediaManager.deleteMultimedaByEntityKey(entityKey);
	}

}
