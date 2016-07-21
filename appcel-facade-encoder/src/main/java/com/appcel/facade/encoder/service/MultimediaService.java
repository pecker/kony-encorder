/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.encoder.service;

import java.util.List;

import com.appcel.facade.encoder.mode.AudioInfo;
import com.appcel.facade.encoder.mode.Multimedia;
import com.appcel.facade.encoder.mode.VideoInfo;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-3
 */
public interface MultimediaService {

	Multimedia createMultimedia(String entityKey, String duration, Long timelen, String type, String fps, String startTime,
			AudioInfo audioInfo, VideoInfo videoInfo);

	Multimedia getMultimediaByKey(String key);

	List<Multimedia> getMultimediaByEntityKey(String entityKey);

	boolean deleteMultimedaByEntityKey(String entityKey);

}