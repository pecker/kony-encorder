/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.encoder.manager;

import java.util.List;

import com.appcel.facade.encoder.mode.AudioInfo;
import com.appcel.facade.encoder.mode.Multimedia;
import com.appcel.facade.encoder.mode.VideoInfo;
import com.appcel.kernel.mybatis.manager.BaseManager;

/**
 * 
 * Defiend class file the MultimediaManager.java
 * 
 *  
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-12-10
 */
public interface MultimediaManager extends BaseManager<Multimedia> {

	Multimedia createMultimedia(String entityKey, String duration, Long timelen, String type, String fps, String startTime,
			AudioInfo audioInfo, VideoInfo videoInfo);

	Multimedia getMultimediaByKey(String key);

	List<Multimedia> getMultimediaByEntityKey(String entityKey);

	boolean deleteMultimedaByEntityKey(String entityKey);
}