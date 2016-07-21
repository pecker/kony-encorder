/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.encoder.job;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.appcel.core.encoder.FastDFSClient;
import com.appcel.core.encoder.config.EncoderConfiguration;
import com.appcel.core.encoder.executor.EncoderExecutor;
import com.appcel.core.encoder.locator.EncoderLocator;
import com.appcel.core.encoder.manager.EncoderInfoManager;
import com.appcel.facade.encoder.DTO.MediaRecord;
import com.appcel.facade.encoder.enums.EncoderExecutorEnum;
import com.appcel.facade.encoder.enums.EncoderPlayModeEnum;
import com.appcel.facade.encoder.enums.EncoderStatusEnum;
import com.appcel.facade.encoder.mode.Configure;
import com.appcel.facade.encoder.mode.EncoderInfo;

/**
 * 截取缩略图工作调度
 * 
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-12
 */
public class CaptureImageJob {

	private static Log LOGGER = LogFactory.getLog(CaptureImageJob.class);

	@Autowired
	EncoderInfoManager encoderInfoManager;

	//boolean retained = EncoderConfiguration.getMe().getEncoderFileRetained();
	String size = EncoderConfiguration.getMe().getEncoderCaptureImageSize();

	public CaptureImageJob() {

	}

	public void doCaptureImage() {

		// 查询出全部需要待截图的转换文件
		List<EncoderInfo> encoderInfos = encoderInfoManager.getEncoderInfoByStatus(EncoderStatusEnum.STATUS_WAIT_CAPTUREIMG);

		if (encoderInfos != null && !encoderInfos.isEmpty()) {

			Map<String, Configure> configureMap = encoderInfoManager.getAllConfigureMap();
			Configure thumbnail_ss_cfg = configureMap.get("thumbnail_ss");
			Configure folder_thumbnail_cfg = configureMap.get("folder_thumbnail");
			Configure folder_video_cfg = configureMap.get("folder_video");

			//----------因为文件系统分布式部署的原因，需要有一个专门做转换的场所，从配置中获取临时转换路径
			String tempFilePath = EncoderConfiguration.getMe().getEncoderExecuteDir();

			StringBuffer realthumbnailDir = new StringBuffer(tempFilePath);
			realthumbnailDir.append("/");
			realthumbnailDir.append(folder_thumbnail_cfg.getValue());

			//---------检查目录是否存在
			File realthumbnailDirFile = new File(realthumbnailDir.toString());
			if (!realthumbnailDirFile.exists()) {
				LOGGER.info("Directory not exist. Create it.");
				LOGGER.info(realthumbnailDirFile);
				realthumbnailDirFile.mkdirs();
			}

			//----------构造转换文件的目录
			StringBuffer realfileDir = new StringBuffer(tempFilePath);
			realfileDir.append("/");
			realfileDir.append(folder_video_cfg.getValue());
			//----------检查目录文件是否存在
			File realfileDirFile = new File(realfileDir.toString());
			if (!realfileDirFile.exists()) {
				LOGGER.info("Directory not exist. Create it.");
				LOGGER.info(realfileDirFile);
				realfileDirFile.mkdirs();
			}

			for (EncoderInfo encoderInfo : encoderInfos) {

				try {
					LOGGER.info("开始处理媒体文件转码截图 =============>>> " + encoderInfo);

					StringBuffer realfileoriPath = new StringBuffer(realfileDir);

					//--------根据播放模式处理要截图的转换文件
					if (EncoderPlayModeEnum.VOD.getValue() == encoderInfo.getPlayMode()) {
						//----------点播的需要从文件系统中找出要转换的文件，再进行截图						

						//----------上传后原始媒体文件存放路径
						String mediaFilePath = encoderInfo.getSrcFilePath();
						//----------处理要转换的文件路径
						//mediaFilePath = mediaFilePath.replaceAll("\\", "/");
						//----------获取要转换的原始文件的文件名
						String srcFileName = mediaFilePath.substring(mediaFilePath.lastIndexOf("/") + 1, mediaFilePath.length());

						//---------构造要下载的文件路径和文件名，也是等待要转换的文件路径
						realfileoriPath.append("/");
						String fileNamePath = srcFileName.substring(0, srcFileName.lastIndexOf("."));
						realfileoriPath.append(fileNamePath);
						realfileoriPath.append("/");
						File encoderFileDir = new File(realfileoriPath.toString());
						if (!encoderFileDir.exists()) {
							LOGGER.info("Directory not exist. Create it.");
							LOGGER.info(encoderFileDir);
							encoderFileDir.mkdirs();
						}

						realfileoriPath.append(srcFileName);
						//---------从文件系统中下载要转换的源文件，也是等待要转换的文件
						File sourceFile = new File(realfileoriPath.toString());
						if (!sourceFile.exists()) {
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("转码源文件下载路径==========>>> " + realfileoriPath);
							}
							InputStream inputStream = FastDFSClient.downloadFile(mediaFilePath);
							FileUtils.copyInputStreamToFile(inputStream, sourceFile);
							LOGGER.info("==========>>> 转码源文件已下载");

						}

					} else if (EncoderPlayModeEnum.LIVE.getValue() == encoderInfo.getPlayMode()) {
						//---------直播模式的可以直接对视频流进行截图
						realfileoriPath.append(encoderInfo.getDestFilePath());
						String a[] = realfileoriPath.toString().split(":");
						//RTMP FIX: libRTMP URL
						if (a[0].equals("rtmp") || a[0].equals("rtmpe") || a[0].equals("rtmpte") || a[0].equals("rtmps")) {
							realfileoriPath.append(" live=1");
							LOGGER.info(realfileoriPath);
						}
					} else {
						throw new UnsupportedOperationException("不支持的播放模式的转换");
					}
					//----------截图后要生成的缩略图文件
					StringBuffer realthumbnailPath = new StringBuffer(realthumbnailDir);
					realthumbnailPath.append("/");
					realthumbnailPath.append(encoderInfo.getKey());
					realthumbnailPath.append(".jpg");

					//----------调用执行转换器执行截图转换命令
					executeJob(encoderInfo, realfileoriPath.toString(), realthumbnailPath.toString(), thumbnail_ss_cfg);

					//----------截图作为转换文件的缩略图，并上传到文件系统
					File destFile = new File(realthumbnailPath.toString());
					if (destFile.exists()) {
						String fileId = FastDFSClient.uploadFile(destFile, encoderInfo.getKey() + ".jpg");
						if (StringUtils.isNotBlank(fileId)) {
							LOGGER.info("截图文件已上传==========>>> " + fileId);
							encoderInfo.setCaptureImgPath(fileId);
						} else {
							LOGGER.error("截图文件上传失败 ==========>>> " + fileId);
							continue;
						}
					} else {
						LOGGER.error("文件不存在截图失败 ==========>>> " + destFile.getAbsolutePath());
						continue;
					}

					if (EncoderPlayModeEnum.VOD.getValue() == encoderInfo.getPlayMode()) {
						//----------是点播，更新转换状态为 3，待转码
						encoderInfo.setStatus(EncoderStatusEnum.STATUS_WAIT_ENCODER.getValue());
					} else if (EncoderPlayModeEnum.LIVE.getValue() == encoderInfo.getPlayMode()) {
						//---------是点播，更新转换状态为 4，已完成
						encoderInfo.setStatus(EncoderStatusEnum.STATUS_COMPLETE.getValue());
					}

					//----------更新转换信息数据
					encoderInfo.setEncoderTime(new Date());
					encoderInfoManager.update(encoderInfo);

					LOGGER.info("处理媒体文件转码截图完成 =============>>> " + encoderInfo);

					//Rest--------------------------
					Thread.sleep(10 * 1000);

				} catch (Exception e) {
					encoderInfoManager.updateEncoderStatus(encoderInfo.getKey(), EncoderStatusEnum.STATUS_FAIL);
					LOGGER.error("处理媒体文件转码截图失败 =============>>> Message: " + e.getMessage());
					LOGGER.error("处理媒体文件转码截图失败 =============>>> Cause: " + e.getCause());
					e.printStackTrace();

				} finally {
					//					File tempFileDir = new File(tempFilePath);
					//					if (tempFileDir.exists()) {
					//						tempFileDir.deleteOnExit();
					//					}
				}
			}
		}

	}

	private MediaRecord executeJob(EncoderInfo encoderInfo, String realfileoriPath, String realthumbnailPath, Configure thumbnail_ss_cfg) {

		EncoderExecutor ffmpegExecutor = EncoderLocator.getMe().createEncoderExecutor(EncoderExecutorEnum.FFMPEGEXECUTOR);

		while (!ffmpegExecutor.isLinuxos() && realfileoriPath.startsWith("/")) {
			realfileoriPath = realfileoriPath.substring(1, realfileoriPath.length());
		}

		while (!ffmpegExecutor.isLinuxos() && realthumbnailPath.startsWith("/")) {
			realthumbnailPath = realthumbnailPath.substring(1, realthumbnailPath.length());
		}

		MediaRecord record = new MediaRecord();
		record.setSrcFilePath(realfileoriPath);
		record.setDestFilePath(realthumbnailPath);

		try {

			ffmpegExecutor.addArgument("-y");
			ffmpegExecutor.addArgument("-i");
			ffmpegExecutor.addArgument(realfileoriPath);
			ffmpegExecutor.addArgument("-ss");
			ffmpegExecutor.addArgument(thumbnail_ss_cfg.getValue());
			ffmpegExecutor.addArgument("-s");
			size = StringUtils.isNotBlank(size) ? size : "220x110";
			ffmpegExecutor.addArgument(size);
			ffmpegExecutor.addArgument("-f");
			ffmpegExecutor.addArgument("image2");
			ffmpegExecutor.addArgument("-vframes");
			ffmpegExecutor.addArgument("1");
			ffmpegExecutor.addArgument(realthumbnailPath);

			ffmpegExecutor.execute(record);

			LOGGER.info("Encoder executor Capture image video info ==========>>> " + record);

		} catch (Exception e) {
			encoderInfoManager.updateEncoderStatus(encoderInfo.getKey(), EncoderStatusEnum.STATUS_FAIL);
			LOGGER.error("==========>>> 转码截图失败Message: " + e.getMessage());
			LOGGER.error("==========>>> 转码截图失败Cause: " + e.getCause());
			e.printStackTrace();
		} finally {
			if (null != ffmpegExecutor) {
				ffmpegExecutor.destroy();
				ffmpegExecutor = null;
			}
		}
		return record;
	}

	public static void main(String[] args) {
		String srcFileName = "wKg4Q1c1iMaAT1uXAEmTktBBsV4183.wmv";
		String fileNamePath = srcFileName.substring(0, srcFileName.lastIndexOf("."));
		System.out.println(fileNamePath);
		;
	}
}
