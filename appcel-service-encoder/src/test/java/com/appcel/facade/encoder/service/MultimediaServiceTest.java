/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.encoder.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.appcel.facade.encoder.mode.AudioInfo;
import com.appcel.facade.encoder.mode.Multimedia;
import com.appcel.facade.encoder.mode.VideoInfo;

/**
 * Unit test for simple App.
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-9
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:dubbo-consumer.xml" })
public class MultimediaServiceTest {
	@Autowired
	MultimediaService multimediaService;

	@Test
	public void testCreateDatastream() {
		AudioInfo audioInfo = new AudioInfo(0, 0, "audioBitrate");
		VideoInfo videoInfo = new VideoInfo("videoBitrate", 0.0f, "vedioformat", "640x360", 640, 360);
		Multimedia multimedia = multimediaService.createMultimedia("entityKey", "duration", 68L, "type", "fps", "startTime", audioInfo,
				videoInfo);
		Assert.assertNotNull(multimedia);
	}
}
