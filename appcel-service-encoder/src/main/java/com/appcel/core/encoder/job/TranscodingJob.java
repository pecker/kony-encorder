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
import com.appcel.core.encoder.manager.MultimediaManager;
import com.appcel.facade.encoder.DTO.MediaRecord;
import com.appcel.facade.encoder.enums.EncoderExecutorEnum;
import com.appcel.facade.encoder.enums.EncoderStatusEnum;
import com.appcel.facade.encoder.exception.EncoderException;
import com.appcel.facade.encoder.exception.InputFormatException;
import com.appcel.facade.encoder.mode.AudioInfo;
import com.appcel.facade.encoder.mode.Configure;
import com.appcel.facade.encoder.mode.EncoderInfo;
import com.appcel.facade.encoder.mode.Multimedia;
import com.appcel.facade.encoder.mode.VideoInfo;
import com.pointdew.common.utils.UUIDUtil;

/**
 * 转码工作调度
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-11
 */
public class TranscodingJob {
	private static Log LOGGER = LogFactory.getLog(TranscodingJob.class);

	@Autowired
	EncoderInfoManager encoderInfoManager;
	@Autowired
	MultimediaManager multimediaManager;

	Configure transcoder_vcodec_cfg;
	Configure transcoder_bv_cfg;
	Configure transcoder_framerate_cfg;
	Configure transcoder_acodec_cfg;
	Configure transcoder_ar_cfg;
	Configure transcoder_ba_cfg;
	Configure transcoder_scale_w_cfg;
	Configure transcoder_scale_h_cfg;
	Configure transcoder_watermarkuse_cfg;
	Configure transcoder_watermark_url_cfg;
	Configure transcoder_watermark_x_cfg;
	Configure transcoder_watermark_y_cfg;
	Configure transcoder_keepaspectratio_cfg;
	Configure transcoder_outfmt_cfg;
	Configure folder_video_cfg;

	String watermarkFile;
	File realwatermarkDirFile;

	boolean retained = EncoderConfiguration.getMe().getEncoderFileRetained();

	//构造函数
	public TranscodingJob() {
	}

	public void doTranscoding() {

		// 查询出全部需要待转换的文件
		List<EncoderInfo> encoderInfos = encoderInfoManager.getEncoderInfoByStatus(EncoderStatusEnum.STATUS_WAIT_ENCODER);

		if (encoderInfos != null && !encoderInfos.isEmpty()) {

			Map<String, Configure> configureMap = encoderInfoManager.getAllConfigureMap();
			//Load Configure
			transcoder_vcodec_cfg = configureMap.get("transcoder_vcodec");
			transcoder_bv_cfg = configureMap.get("transcoder_bv");
			transcoder_framerate_cfg = configureMap.get("transcoder_framerate");
			transcoder_acodec_cfg = configureMap.get("transcoder_acodec");
			transcoder_ar_cfg = configureMap.get("transcoder_ar");
			transcoder_ba_cfg = configureMap.get("transcoder_ba");
			transcoder_scale_w_cfg = configureMap.get("transcoder_scale_w");
			transcoder_scale_h_cfg = configureMap.get("transcoder_scale_h");
			transcoder_watermarkuse_cfg = configureMap.get("transcoder_watermarkuse");
			transcoder_watermark_url_cfg = configureMap.get("transcoder_watermark_url");
			transcoder_watermark_x_cfg = configureMap.get("transcoder_watermark_x");
			transcoder_watermark_y_cfg = configureMap.get("transcoder_watermark_y");
			transcoder_keepaspectratio_cfg = configureMap.get("transcoder_keepaspectratio");
			transcoder_outfmt_cfg = configureMap.get("transcoder_outfmt");
			folder_video_cfg = configureMap.get("folder_video");

			//----------因为文件系统分布式部署的原因，需要有一个专门做转换的场所，从配置中获取临时转换路径
			String tempFilePath = EncoderConfiguration.getMe().getEncoderExecuteDir();

			//----------Folder of Watermark 构造水印文件存放目录
			String[] watermarkstrlist = transcoder_watermark_url_cfg.getValue().split("/");
			String watermarkDir = "";
			watermarkFile = watermarkstrlist[watermarkstrlist.length - 1];
			for (int i = 0; i < watermarkstrlist.length - 1; i++) {
				watermarkDir += watermarkstrlist[i] + "/";
			}
			//----------组装
			StringBuffer realwatermarkDir = new StringBuffer(tempFilePath);
			realwatermarkDir.append("/");
			realwatermarkDir.append(watermarkDir);
			//----------定义并检查水印文件目录
			String realwatermarkPath = realwatermarkDir.toString();
			//while (!ffmpegExecutor.isLinuxos() && realwatermarkPath.startsWith("/")) {
			//realwatermarkPath = realwatermarkPath.substring(1, realwatermarkPath.length());
			//}

			realwatermarkDirFile = new File(realwatermarkPath);
			if (!realwatermarkDirFile.exists()) {
				LOGGER.info("Directory not exist. Create it.");
				LOGGER.info(realwatermarkDirFile);
				realwatermarkDirFile.mkdirs();
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

				LOGGER.info("开始处理媒体文件转码 =============>>> " + encoderInfo);

				try {

					//----------转码源（原始）文件路径
					StringBuffer realfileoriginalPath = new StringBuffer(realfileDir);
					realfileoriginalPath.append("/");

					//----------转码目标文件路径
					StringBuffer realfilePath = new StringBuffer(realfileDir);
					realfilePath.append("/");

					//----------上传后原始媒体文件存放路径
					String mediaFilePath = encoderInfo.getSrcFilePath();
					//----------处理要转换的文件路径
					//mediaFilePath = mediaFilePath.replaceAll("\\", "/");
					//----------获取要转换的原始文件的文件名
					String srcFileName = mediaFilePath.substring(mediaFilePath.lastIndexOf("/") + 1, mediaFilePath.length());

					//---------构造要下载的文件路径和文件名，也是等待要转换的文件路径
					String fileNamePath = srcFileName.substring(0, srcFileName.lastIndexOf("."));
					realfileoriginalPath.append(fileNamePath);
					realfileoriginalPath.append("/");
					File srcFileDir = new File(realfileoriginalPath.toString());
					if (!srcFileDir.exists()) {
						LOGGER.info("==========>>> Directory not exist. Create it.");
						LOGGER.info(srcFileDir);
						srcFileDir.mkdir();
					}
					realfileoriginalPath.append(srcFileName);

					//---------从文件系统中下载要转换的源文件，也是等待要转换的文件
					File sourceFile = new File(realfileoriginalPath.toString());
					if (!sourceFile.exists()) {
						InputStream inputStream = FastDFSClient.downloadFile(mediaFilePath);
						FileUtils.copyInputStreamToFile(inputStream, sourceFile);
					}

					LOGGER.info(realfileoriginalPath);

					//----------构造转换目标文件路径
					realfilePath.append(fileNamePath);
					realfilePath.append("/");
					File destFileDir = new File(realfilePath.toString());
					if (!destFileDir.exists()) {
						LOGGER.info("==========>>> Directory not exist. Create it.");
						LOGGER.info(destFileDir);
						destFileDir.mkdir();
					}
					//----------构造转码文件名
					realfilePath.append(fileNamePath);
					realfilePath.append(".");
					realfilePath.append(transcoder_outfmt_cfg.getValue());

					MediaRecord record = executeJob(encoderInfo, realfileoriginalPath.toString(), realfilePath.toString());

					//----------截图作为转换文件的缩略图，并上传到文件系统
					File destFile = new File(realfilePath.toString());
					if (destFile.exists()) {
						String fileId = FastDFSClient.uploadFile(destFile, fileNamePath + "." + transcoder_outfmt_cfg.getValue());
						if (StringUtils.isNotBlank(fileId)) {
							LOGGER.info("转码文件已上传至文件系统 ==========>>> " + fileId);
							encoderInfo.setDestFilePath(fileId);
							if (!retained) {
								//destFile.delete();
								destFile.deleteOnExit();
							}

						} else {
							LOGGER.error("转码文件上传失败 ==========>>> " + fileId);
							continue;
						}
					} else {
						LOGGER.error("文件不存在转码失败 ==========>>> " + destFile.getAbsolutePath());
						continue;
					}

					//----------更新转换信息数据
					encoderInfo.setStatus(EncoderStatusEnum.STATUS_COMPLETE.getValue());
					encoderInfo.setEncoderTime(new Date());
					encoderInfoManager.update(encoderInfo);

					LOGGER.info("处理媒体文件转码成功 =============>>> " + encoderInfo);

					if (null != record) {
						// 保存视频信息
						storageMediaInfo(record, encoderInfo);
					}

					//Rest--------------------------
					Thread.sleep(10 * 1000);

				} catch (Exception e) {
					encoderInfoManager.updateEncoderStatus(encoderInfo.getKey(), EncoderStatusEnum.STATUS_FAIL);
					LOGGER.error("==========>>> 处理媒体文件转码失败Message: " + e.getMessage());
					LOGGER.error("==========>>> 处理媒体文件转码失败Cause: " + e.getCause());
					e.printStackTrace();
				} finally {
					//					File tempFileDir = new File(tempFilePath);
					//					if (!retained&& tempFileDir.exists()) {
					//						tmpFile.delete();
					//						tempFileDir.deleteOnExit();
					//					}
				}
			}
		}
	}

	private MediaRecord executeJob(EncoderInfo encoderInfo, String realfileoriginalPath, String realfilePath) {

		EncoderExecutor ffmpegExecutor = EncoderLocator.getMe().createEncoderExecutor(EncoderExecutorEnum.FFMPEGEXECUTOR);

		while (!ffmpegExecutor.isLinuxos() && realfileoriginalPath.startsWith("/")) {
			realfileoriginalPath = realfileoriginalPath.substring(1, realfileoriginalPath.length());
		}

		while (!ffmpegExecutor.isLinuxos() && realfilePath.startsWith("/")) {
			realfilePath = realfilePath.substring(1, realfilePath.length());
		}

		MediaRecord record = new MediaRecord();
		record.setSrcFilePath(realfileoriginalPath);
		record.setDestFilePath(realfilePath);

		try {
			//转码命令如下所示
			//ffmpeg -i xxx.mkv -ar 22050 -b 600k -vcodec libx264 
			//-vf scale=w=640:h=360:force_original_aspect_ratio=decrease,pad=w=640:h=360:x=(ow-iw)/2:y=(oh-ih)/2[aa];
			//movie=watermark.png[bb];[aa][bb]overlay=5:5 yyy.flv
			//AVFilter参数作用如下所示
			//scale:视频拉伸滤镜。force_original_aspect_ratio用于强制保持宽高比
			//pad:用于加黑边，四个参数含义分别为：处理后宽，处理后高，输入图像左上角x坐标，输入视频左上角Y坐标。
			//其中ow,oh为输出（填充后）视频的宽高；iw,ih为输入（填充前）视频的宽高。
			//movie：用于指定需要叠加的水印Logo（PNG文件）。
			//overlay:用于叠加水印Logo和视频文件
			ffmpegExecutor.addArgument("-y");
			ffmpegExecutor.addArgument("-i");
			ffmpegExecutor.addArgument(realfileoriginalPath);
			ffmpegExecutor.addArgument("-vcodec");
			ffmpegExecutor.addArgument(transcoder_vcodec_cfg.getValue());
			ffmpegExecutor.addArgument("-b:v");
			ffmpegExecutor.addArgument(transcoder_bv_cfg.getValue());
			ffmpegExecutor.addArgument("-r");
			ffmpegExecutor.addArgument(transcoder_framerate_cfg.getValue());
			ffmpegExecutor.addArgument("-acodec");
			ffmpegExecutor.addArgument(transcoder_acodec_cfg.getValue());
			ffmpegExecutor.addArgument("-b:a");
			ffmpegExecutor.addArgument(transcoder_ba_cfg.getValue());
			ffmpegExecutor.addArgument("-ar");
			ffmpegExecutor.addArgument(transcoder_ar_cfg.getValue());
			ffmpegExecutor.addArgument("-vf");

			StringBuffer transcmd = new StringBuffer("scale=w=");
			transcmd.append(transcoder_scale_w_cfg.getValue());
			transcmd.append(":h=");
			transcmd.append(transcoder_scale_h_cfg.getValue());

			if (transcoder_keepaspectratio_cfg.getValue().equals("true")) {
				transcmd.append(":force_original_aspect_ratio=decrease,");
				transcmd.append("pad=w=");
				transcmd.append(transcoder_scale_w_cfg.getValue());
				transcmd.append(":h=");
				transcmd.append(transcoder_scale_h_cfg.getValue());
				transcmd.append(":x=(ow-iw)/2:y=(oh-ih)/2");
			}

			transcmd.append("[aa]");

			if (transcoder_watermarkuse_cfg.getValue().equals("true")) {
				transcmd.append(";movie=");
				transcmd.append(watermarkFile);
				transcmd.append("[bb];");
				transcmd.append("[aa][bb]");
				transcmd.append("overlay=x=");
				transcmd.append(transcoder_watermark_x_cfg.getValue());
				transcmd.append(":y=");
				transcmd.append(transcoder_watermark_y_cfg.getValue());

			}
			ffmpegExecutor.addArgument(transcmd.toString());

			if ("mp4".equalsIgnoreCase(transcoder_outfmt_cfg.getValue())) {
				File tempTargetFile = new File(realfilePath);
				StringBuilder tempTargetFilePath = new StringBuilder(tempTargetFile.getParent());
				tempTargetFilePath.append(File.separator);
				tempTargetFilePath.append("qt-faststart-");
				tempTargetFilePath.append(UUIDUtil.generateUniqueID());
				tempTargetFilePath.append(".mp4");

				ffmpegExecutor.addArgument(tempTargetFilePath.toString());
				//----------开始转码
				ffmpegExecutor.execute(record, realwatermarkDirFile);

				//----------提取mp4播放信息至视频头部
				qt_faststartExecute(encoderInfo, tempTargetFilePath.toString(), realfilePath, record);

			} else if ("flv".equalsIgnoreCase(transcoder_outfmt_cfg.getValue())) {

				ffmpegExecutor.addArgument(realfilePath);
				ffmpegExecutor.execute(record, realwatermarkDirFile);
			}

			File realfileoriginalPathFile = new File(realfileoriginalPath);
			if (!retained && realfileoriginalPathFile.exists()) {
				//realfileoriginalPathFile.delete();
				realfileoriginalPathFile.deleteOnExit();
			}
			LOGGER.info("Ffmpeg encoder media record info ==========>>> " + record);

		} catch (Exception e) {
			encoderInfoManager.updateEncoderStatus(encoderInfo.getKey(), EncoderStatusEnum.STATUS_FAIL);
			LOGGER.error("==========>>> 转码失败Message: " + e.getMessage());
			LOGGER.error("==========>>> 转码失败Cause: " + e.getCause());
			e.printStackTrace();
		} finally {
			if (null != ffmpegExecutor) {
				ffmpegExecutor.destroy();
				ffmpegExecutor = null;
			}
		}
		return record;
	}

	private void qt_faststartExecute(EncoderInfo encoderInfo, String tempTargetFilePath, String targetFilePath, MediaRecord record)
			throws IllegalArgumentException, InputFormatException, EncoderException {

		EncoderExecutor qtfaststartExecutor = EncoderLocator.getMe().createEncoderExecutor(EncoderExecutorEnum.QT_FASTSTART);

		try {

			qtfaststartExecutor.addArgument(tempTargetFilePath);
			qtfaststartExecutor.addArgument(targetFilePath);

			//executor.setDirectory(realwatermarkDirFile);
			qtfaststartExecutor.execute(record);

			File tmpFile = new File(tempTargetFilePath);
			if (!retained && tmpFile.exists()) {
				//tmpFile.delete();
				tmpFile.deleteOnExit();
			}

		} catch (Exception e) {
			encoderInfoManager.updateEncoderStatus(encoderInfo.getKey(), EncoderStatusEnum.STATUS_FAIL);
			LOGGER.error("==========>>> 转码失败Message: " + e.getMessage());
			LOGGER.error("==========>>> 转码失败Cause: " + e.getCause());
			throw new EncoderException(e);
		} finally {
			if (null != qtfaststartExecutor) {
				qtfaststartExecutor.destroy();
				qtfaststartExecutor = null;
			}
		}
	}

	private void storageMediaInfo(MediaRecord record, EncoderInfo encoderInfo) {

		if (null != record) {
			AudioInfo audioInfo = new AudioInfo(0, 0, record.getAudioBitrate());
			VideoInfo videoInfo = new VideoInfo(record.getBitrate(), 0.0f, record.getVedioformat(), record.getSize(), record.getWidth(),
					record.getHeight());

			Multimedia multimedia = multimediaManager.createMultimedia(encoderInfo.getEntityKey(), record.getDuration(),
					record.getTimelen(), record.getType(), record.getFps(), record.getStartTime(), audioInfo, videoInfo);
			LOGGER.info("视频转换多媒体信息数据 ==== >> " + multimedia);
		}
	}

	public static void main(String[] args) {
		String val = "watermark/watermark.png";
		String[] watermarkstrlist = val.split("/");
		String watermarkDir = "";
		String watermarkFile = watermarkstrlist[watermarkstrlist.length - 1];
		for (int i = 0; i < watermarkstrlist.length - 1; i++) {
			watermarkDir += watermarkstrlist[i] + "/";
		}
		System.out.println("watermarkDir=====>>>" + watermarkDir);
		System.out.println("watermarkFile=====>>>" + watermarkFile);
	}
}
