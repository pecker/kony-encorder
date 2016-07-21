/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.encoder.service;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.appcel.core.encoder.FastDFSClient;
import com.appcel.facade.encoder.enums.EncoderPlayModeEnum;
import com.appcel.facade.encoder.enums.EncoderStatusEnum;
import com.appcel.facade.encoder.mode.EncoderInfo;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:dubbo-consumer.xml" })
public class EncoderInfoServiceTest {
	private static final Log log = LogFactory.getLog(EncoderInfoServiceTest.class);

	@Autowired
	EncoderInfoService encoderInfoService;

	/**
	 * 上传测试.
	 * @throws Exception
	 */
	public String upload(File file, String filePath) throws Exception {

		String fileId = FastDFSClient.uploadFile(file, filePath);
		System.out.println("Upload local file " + filePath + " ok, fileid=" + fileId);
		// fileId:	group1/M00/00/00/wKg4Q1b6IEuAO-zIAAGoSC2lYbU129.png
		// url:	http://192.168.56.67:8888/group1/M00/00/00/wKg4Q1b6IEuAO-zIAAGoSC2lYbU129.png
		return fileId;
	}

	@Test
	public void testTranscoding() {
		try {

			String filePath = "D:\\FFmpeg\\test\\zs.swf";
			File file = new File(filePath);
			String fileId = upload(file, filePath);

			//String filePath = "group1/M00/00/00/wKg4Q1c1iMaAT1uXAEmTktBBsV4183.wmv";//"group1/M00/00/00/wKg4Q1cx4ACAaNihAGGnSIoqNtM031.avi";
			//String fileId = filePath;

			log.info(fileId);

			// 文件转换
			String entityKey = "wKg4Q1cx4ACAaNihAGGnSIoqNtM031";
			String fileName = "wKg4Q1cx4ACAaNihAGGnSIoqNtM031";
			String srcFilePath = fileId;
			//			Integer playMode = EncoderPlayModeEnum.VOD.getValue();
			//			Integer status = EncoderStatusEnum.STATUS_WAIT_CAPTUREIMG.getValue();
			String remark = "测试文件转码截图试试看";

			EncoderInfo encoderInfo = encoderInfoService.createEncoderInfo(entityKey, fileName, srcFilePath, EncoderPlayModeEnum.VOD,
					EncoderStatusEnum.STATUS_WAIT_CAPTUREIMG, remark);
			Assert.assertNotNull(encoderInfo);

		} catch (Exception e) {
			log.error("==>Encoder tanscoding start error:", e);
			e.printStackTrace();
			System.exit(0);
		} finally {
			log.info("===>System.exit");
			System.exit(0);
		}
	}
}
