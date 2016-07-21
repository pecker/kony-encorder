/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.encoder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.appcel.common.DTO.EncoderObject;
import com.appcel.common.enums.EncoderFormatEnum;
import com.appcel.common.enums.EncoderTypeEnum;
import com.appcel.core.encoder.config.EncoderConfiguration;
import com.appcel.core.encoder.executor.EncoderExecutor;
import com.appcel.core.encoder.locator.EncoderLocator;
import com.appcel.core.encoder.utils.DurationUtil;
import com.appcel.facade.encoder.DTO.MediaRecord;
import com.appcel.facade.encoder.enums.EncoderExecutorEnum;
import com.appcel.facade.encoder.exception.EncoderException;
import com.appcel.facade.encoder.exception.EncoderRuntimeException;
import com.appcel.facade.encoder.exception.InputFormatException;
import com.pointdew.common.io.FileUtil;
import com.pointdew.common.utils.ContentTypeUtil;
import com.pointdew.common.utils.UUIDUtil;

/**
 * 
 * Defiend class file the MediaEndocder.java
 *  
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-12-4
 */
//@Component("mediaEncoder")
public class MediaEncoder {
	protected static Log LOGGER = LogFactory.getLog(MediaEncoder.class);

	/**
	 * The locator of the EncoderExecutor executable used by this encoder.
	 */
	private EncoderExecutor encoderExecutor;

	/**
	 * The locator of the ffmpeg executable used by this encoder.
	 */
	//private QTFaststartExecutor qtFaststartExecutor;

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	/**
	 * It builds an encoder using a {@link EncoderLocator} instance to
	 * locate the encoderExecutor executable to use.
	 */
	public MediaEncoder() {
		this.encoderExecutor = EncoderLocator.getMe().createEncoderExecutor(EncoderExecutorEnum.FFMPEGEXECUTOR);
	}

	public MediaRecord encode(final EncoderObject encoderObject) {
		final MediaRecord mediaRecord = new MediaRecord();
		threadPoolTaskExecutor.execute(new Runnable() {
			public void run() {

				try {
					// 视频转换截图
					String mediaFilePath = encoderObject.getFileId();

					String tempFilePath = EncoderConfiguration.getMe().getEncoderExecuteDir();

					// 处理要转换的文件，从文件系统中下载要转换的文件
					mediaFilePath = mediaFilePath.replaceAll("\\\\", "/").replaceAll("\\", "/");
					String srcFileName = mediaFilePath.substring(mediaFilePath.lastIndexOf("/") + 1, mediaFilePath.length());

					//构造要下载的文件路径和文件名,即要转换的源文件
					StringBuffer srcFilePath = new StringBuffer(tempFilePath);
					srcFilePath.append("/");
					srcFilePath.append(srcFileName);
					File sourceFile = new File(srcFilePath.toString());
					if (!sourceFile.exists()) {
						InputStream inputStream = FastDFSClient.downloadFile(mediaFilePath);
						FileUtils.copyInputStreamToFile(inputStream, sourceFile);
					}

					// 构造要转换到的目标文件路径和文件名
					StringBuffer destFilePath = new StringBuffer(tempFilePath);
					destFilePath.append("/");
					destFilePath.append(encoderObject.getFileName());

					EncoderFormatEnum encoderFormat = encoderObject.getEncoderFormat();
					String format = null;

					EncoderTypeEnum encoderType = encoderObject.getEncoderType();

					if (EncoderTypeEnum.VIDEO.equals(encoderType)) {
						// 转换视频

						if (EncoderFormatEnum.VIDEO_FORMAT_FLV.equals(encoderFormat)) {
							format = "flv";
						} else if (EncoderFormatEnum.VIDEO_FORMAT_MP4.equals(encoderFormat)) {
							format = "mp4";
						} else {
							throw new UnsupportedOperationException("不支持的转换格式");
						}
						destFilePath.append("." + format);

						LOGGER.info("Encoder dest file path ===>>> " + destFilePath);

						String targetFilePath = destFilePath.toString();
						targetFilePath = targetFilePath.replaceAll("\\\\", "/").replaceAll("\"", "/");
						encodeVideo(sourceFile, targetFilePath, format, mediaRecord);

					} else if (EncoderTypeEnum.AUDIO.equals(encoderType)) {
						// 转换音频

					} else if (EncoderTypeEnum.IMAGE.equals(encoderType)) {
						// 视频截图						
						if (EncoderFormatEnum.IMAGE_FORMAT_PNG.equals(encoderFormat)) {
							format = "png";
						} else if (EncoderFormatEnum.IMAGE_FORMATE_JPG.equals(encoderFormat)) {
							format = "jpg";
						} else {
							throw new UnsupportedOperationException("不支持的转换格式");
						}
						destFilePath.append("." + format);

						LOGGER.info("Encoder dest file path ===>>> " + destFilePath);

						String targetFilePath = destFilePath.toString();
						targetFilePath = targetFilePath.replaceAll("\\\\", "/").replaceAll("\"", "/");
						encoderCaptureImage(sourceFile, targetFilePath, format, mediaRecord);

					} else {
						throw new UnsupportedOperationException("不支持的转换格式");
					}

					LOGGER.info("Media Record ===>>> " + mediaRecord);

					File destFile = new File(mediaRecord.getDestFilePath());
					LOGGER.info(destFile);

					//将转换过的文件上传至文件系统
					String fileId = FastDFSClient.uploadFile(destFile, encoderObject.getFileName());
					LOGGER.info("Encoder upload file id ===>>> " + fileId);
					mediaRecord.setEntityFile(fileId);

					LOGGER.info("Encoder Object ===>>> " + encoderObject);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		return mediaRecord;
	}

	/**
	 *
	 * @param sourceFile
	 *            将要被转换的目标文件
	 * @param destFilePath
	 *            转换之后文件的存放路径 ffmpeg commandLine： ffmpeg -y -i
	 *            /usr/local/bin/test.mp4 -ab 56 -ar 22050 -b 500 -s 320x240
	 *            /usr/local/bin/test.mp4
	 * @throws IOException
	 */
	public MediaRecord encodeVideo(File sourceFile, String targetFilePath, String format, MediaRecord record)
			throws IllegalArgumentException, InputFormatException, EncoderException {

		String fileName = sourceFile.getName();
		String extNametype = fileName.substring(fileName.lastIndexOf(".") + 1);
		if (!ContentTypeUtil.isSupportedVedioExt(extNametype)) {
			throw new EncoderException("unsurpported encode file type " + extNametype);
		}

		//MediaRecord record = new MediaRecord();
		record.setSrcFilePath(sourceFile.getAbsolutePath());
		record.setDestFilePath(targetFilePath);
		record.setType(extNametype);

		//MultimediaInfo info = new MultimediaInfo();

		LOGGER.info("===>>> encodeVideo 视频转换中...");

		try {

			encoderExecutor.addArgument("-i");//filename 输入文件
			encoderExecutor.addArgument(sourceFile.getAbsolutePath());

			// 在PSP中显示的影片的标题
			//ffmpegEncoderExecutor.addArgument("-title");
			//ffmpegEncoderExecutor.addArgument("Title Kony Lee");

			// 设置作者
			//ffmpegEncoderExecutor.addArgument("-author");
			//ffmpegEncoderExecutor.addArgument("Author Kony Lee");

			// 设置版权
			//ffmpegEncoderExecutor.addArgument("-copyright");
			//ffmpegEncoderExecutor.addArgument("Copyright Kony Lee");

			// 设置评论
			//ffmpegEncoderExecutor.addArgument("-comment");
			//ffmpegEncoderExecutor.addArgument("Comment Kony Lee");

			// 使用XVID编码压缩视频，不能改的
			//ffmpegEncoderExecutor.addArgument("-vcodec");
			//ffmpegEncoderExecutor.addArgument("xvid");

			/*
			 * 视频元数据 (编码等级、分辨率、色域、码率、帧率、位深、时长等等)
			 * -acodec aac 设定声音编码 			
			* -ac <数值> 设定声道数，1就是单声道，2就是立体声，转换单声道的TVrip可以用1（节省一半容量），高品质 
			* 的DVDrip就可以用2 
			* -ar <采样率> 设定声音采样率，PSP只认24000 
			* -ab <比特率> 设定声音比特率，前面-ac设为立体声时要以一半比特率来设置，比如192kbps的就设成96，转换 
			* 君默认比特率都较小，要听到较高品质声音的话建议设到160kbps（80）以上 
			* -vol <百分比> 设定音量，某些DVDrip的AC3轨音量极小，转换时可以用这个提高音量，比如200就是原来的2倍 
			* 这样，要得到一个高画质音质低容量的MP4的话，首先画面最好不要用固定比特率，而用VBR参数让程序自己去 
			*判断，而音质参数可以在原来的基础上提升一点，听起来要舒服很多，也不会太大，看情况调整。
			 */

			//bitrate 设置音频码率
			//ffmpegEncoderExecutor.addArgument("-ab");
			//ffmpegEncoderExecutor.addArgument("56");

			//freq 设置音频采样率
			//ffmpegEncoderExecutor.addArgument("-ar");
			//ffmpegEncoderExecutor.addArgument("22050");

			//bitrate 设置比特率，缺省200kb/s
			String bitrate = EncoderConfiguration.getMe().getEncoderVideoBitrate();
			if (StringUtils.isNotBlank(bitrate)) {
				//ffmpegEncoderExecutor.addArgument("-b");
				//ffmpegEncoderExecutor.addArgument(bitrate);
			}

			//size 设置帧大小 格式为WXH 缺省160*128
			//String resolution = EncoderConfiguration.getMe().getMediaEncoderResolution();
			//if (StringUtils.isNotBlank(resolution)) {
			//ffmpegEncoderExecutor.addArgument("-s");
			//ffmpegEncoderExecutor.addArgument(resolution);
			//}

			// 激活高质量设置
			//ffmpegEncoderExecutor.addArgument("-hq");

			// 打印特定调试信息
			//ffmpegEncoderExecutor.addArgument("-debug");

			// 强迫采用格式
			encoderExecutor.addArgument("-f");
			encoderExecutor.addArgument(format);

			//覆盖输出文件（覆盖输出文件，即如果1.***文件已经存在的话，不经提示就覆盖掉了） 
			encoderExecutor.addArgument("-y");

			if ("mp4".equalsIgnoreCase(format)) {
				File tempTargetFile = new File(targetFilePath);

				StringBuilder tempTargetFilePath = new StringBuilder(tempTargetFile.getParent());
				tempTargetFilePath.append(File.separator);
				tempTargetFilePath.append("qt-faststart-");
				tempTargetFilePath.append(UUIDUtil.generateUniqueID());
				tempTargetFilePath.append(".mp4");

				encoderExecutor.addArgument(tempTargetFilePath.toString());
				encoderExecutor.execute(record);

				qtFaststartEncodeForMp4(tempTargetFilePath.toString(), targetFilePath, record);

			} else if ("flv".equalsIgnoreCase(format)) {

				encoderExecutor.addArgument(targetFilePath);
				encoderExecutor.execute(record);
			}

			LOGGER.info("Encode vedio to " + format + " success...");

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Vedio encode error ===>> ", e.fillInStackTrace());
		} finally {
			if (null != encoderExecutor) {
				encoderExecutor.destroy();
			}
		}

		return record;
	}

	private void qtFaststartEncodeForMp4(String tempTargetFilePath, String targetFilePath, MediaRecord record)
			throws IllegalArgumentException, InputFormatException, EncoderException {

		String extName = FileUtil.getFileExtension(tempTargetFilePath);

		if ("mp4".equalsIgnoreCase(extName)) {

			encoderExecutor = EncoderLocator.getMe().createEncoderExecutor(EncoderExecutorEnum.QT_FASTSTART);

			LOGGER.info("===>>> qt-faststart 转换  vedio meta info 中...");

			try {

				encoderExecutor.addArgument(tempTargetFilePath);
				encoderExecutor.addArgument(targetFilePath);
				encoderExecutor.execute(record);

				File tmpFile = new File(tempTargetFilePath);
				if (tmpFile.exists()) {
					tmpFile.delete();
					tmpFile.deleteOnExit();
				}
				LOGGER.info("===>>> qt-faststart Encode vedio meta info success...");

			} catch (Exception e) {
				throw new EncoderException(e);
			} finally {
				if (null != encoderExecutor) {
					encoderExecutor.destroy();
				}

			}
		}
	}

	/**
	 *
	 * @param sourceFile
	 *            将要被转换的目标文件
	 * @param destFilePath
	 *            转换之后文件的存放路径 ffmpeg commandLine： ffmpeg -y -i
	 *            /usr/local/bin/lh.mp4 -ab 56 -ar 22050 -b 500 -s 320x240
	 *            /usr/local/bin/lh.flv
	 * @throws IOException
	 */
	public MediaRecord encodeAudio(File sourceFile, String targetFilePath, String format) throws IllegalArgumentException,
			InputFormatException, EncoderException {

		String fileName = sourceFile.getName();
		String type = fileName.substring(fileName.lastIndexOf(".") + 1);
		if (!ContentTypeUtil.isSupportedAudioExt(type)) {
			throw new EncoderRuntimeException("unsurpported file type " + type);
		}

		MediaRecord record = new MediaRecord();
		record.setSrcFilePath(sourceFile.getAbsolutePath());
		record.setDestFilePath(targetFilePath);
		record.setType(type);

		try {

			encoderExecutor.addArgument("-i");//filename 输入文件
			encoderExecutor.addArgument(sourceFile.getAbsolutePath());

			//			ffmpegEncoderExecutor.addArgument("-ab");//bitrate 设置音频码率
			//			ffmpegEncoderExecutor.addArgument("56");
			//			ffmpegEncoderExecutor.addArgument("-ar");//freq 设置音频采样率
			//			ffmpegEncoderExecutor.addArgument("22050");
			//			ffmpegEncoderExecutor.addArgument("-b");//bitrate 设置比特率，缺省200kb/s
			//			int bitrate = EncoderConfiguration.getMe().getMediaEncoderBitrate();
			//			ffmpegEncoderExecutor.addArgument(Integer.toString(bitrate));

			//ffmpeg.addArgument("-s"); //size 设置帧大小 格式为WXH 缺省160X128
			//ffmpeg.addArgument("320*240");
			//+ File.separator + fileName.substring(0, fileName.lastIndexOf(".mp3"))

			encoderExecutor.addArgument("-f");
			encoderExecutor.addArgument(format);//"mp3"
			//覆盖输出文件（覆盖输出文件，即如果1.***文件已经存在的话，不经提示就覆盖掉了） 
			encoderExecutor.addArgument("-y");

			encoderExecutor.addArgument(targetFilePath);

			encoderExecutor.execute(record);

			LOGGER.info("===>>> Encode audio to mp4 success...");

		} catch (EncoderException e) {
			throw new EncoderException(e);
		} finally {
			if (null != encoderExecutor) {
				encoderExecutor.destroy();
			}

		}
		return record;
	}

	/**
	 *
	 * 获取图片的第一帧 ffmpeg commandLine： ffmpeg -y -i /usr/local/bin/lh.3gp -vframes
	 * 1 -r 1 -ac 1 -ab 2 -s 320x240 -f image2 /usr/local/bin/lh.jpg
	 *
	 * @param srcFile 源文件
	 * @param destFilePath 目标文件
	 * @throws IOException
	 * @throws IOException
	 */
	public MediaRecord encoderCaptureImage(File srcFile, String destFilePath, String format, MediaRecord record)
			throws IllegalArgumentException, InputFormatException, EncoderException {
		String size = EncoderConfiguration.getMe().getEncoderCaptureImageSize();
		String time = EncoderConfiguration.getMe().getEncoderCaptureImageTime();
		return encoderCaptureImage(srcFile, destFilePath, format, time, size, record);
	}

	/**
	 * 捕获第一帧视频为图片
	 *  Capture Frame To Image
	 * @param srcFile 视频源文件
	 * @param destination 截图目标存放位置
	 * @param format 要保存的图片格式：jpg,jpeg,gif
	 * @param position
	 * @param size
	 * @return
	 */
	public MediaRecord encoderCaptureImage(File srcFile, String destFilePath, String format, String position, String size,
			MediaRecord record) throws IllegalArgumentException, InputFormatException, EncoderException {
		String fileName = srcFile.getName();
		String surffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		if (StringUtils.isNotBlank(surffix) && !ContentTypeUtil.isSupportedVedioExt(surffix.toLowerCase())) {
			throw new EncoderRuntimeException("unsurpported file type " + surffix);
		}

		//MediaRecord record = new MediaRecord();
		record.setSrcFilePath(srcFile.getAbsolutePath());
		record.setDestFilePath(destFilePath);
		record.setType(format);
		record.setSize(size);
		try {

			encoderExecutor.addArgument("-y");//覆盖输出文件
			encoderExecutor.addArgument("-i");//filename 输入文件
			encoderExecutor.addArgument(srcFile.getAbsolutePath());

			encoderExecutor.addArgument("-ss");//position 搜索到指定的时间 [-]hh:mm:ss[.xxx]的格式也支持
			encoderExecutor.addArgument(position);

			encoderExecutor.addArgument("-vframes");
			encoderExecutor.addArgument("1");
			encoderExecutor.addArgument("-r");//fps 设置帧频 缺省25
			encoderExecutor.addArgument("1");
			encoderExecutor.addArgument("-ac");//channels 设置通道 缺省为1
			encoderExecutor.addArgument("1");
			encoderExecutor.addArgument("-ab");//bitrate 设置音频码率
			encoderExecutor.addArgument("2");
			encoderExecutor.addArgument("-s");//size 设置帧大小 格式为WXH 缺省160X128
			encoderExecutor.addArgument(size);
			encoderExecutor.addArgument("-f");//fmt 强迫采用格式fmt
			encoderExecutor.addArgument("image2");

			encoderExecutor.addArgument(destFilePath);

			encoderExecutor.execute(record);

			//			File imageFile = new File(destCaptureImage.toString());
			//			InputStream captureImage = new FileInputStream(imageFile);
			//			record.setCaptureImage(captureImage);

			LOGGER.info("Encode vedio frame to capture image success...");

		} catch (EncoderException e) {
			throw new EncoderException(e);
		} finally {
			if (null != encoderExecutor) {
				encoderExecutor.destroy();
			}

		}
		return record;
	}

	/**
	 * 为转换的视频添加水印
	 * 
	 * @param srcFile
	 * @param destFilePath
	 * @param watermarkPath
	 */
	public void appendWatermark(File srcFile, String destFilePath, String watermarkPath) throws IllegalArgumentException,
			InputFormatException, EncoderException {
		String fileName = srcFile.getName();
		String type = fileName.substring(fileName.lastIndexOf(".") + 1);
		if (!ContentTypeUtil.isSupportedVedioExt(type)) {
			throw new EncoderRuntimeException("unsurpported file type " + type);
		}

		try {

			encoderExecutor.addArgument("-i");//filename 输入文件
			encoderExecutor.addArgument(srcFile.getAbsolutePath());
			encoderExecutor.addArgument("-vf");
			//String param = "movie=logo.png [logo]; [in][logo] overlay=10:10 [out]";
			//ffmpegEncoderExecutor.addArgument("movie=logo.png [logo]; [in][logo] overlay=10:10 [out]");	
			StringBuffer params = new StringBuffer("movie=");
			params.append(watermarkPath + " ");
			params.append("[logo]; [in][logo] overlay=10:10 [out]");

			encoderExecutor.addArgument("\"" + params.toString() + "\"");
			encoderExecutor.addArgument(destFilePath);

			MediaRecord record = new MediaRecord();
			encoderExecutor.execute(record);

			LOGGER.info("Encode vedio to append watermark success...");

		} catch (EncoderException e) {
			throw new EncoderException(e);
		} finally {
			if (null != encoderExecutor) {
				encoderExecutor.destroy();
			}
		}
	}

	public static void main(String[] args) {

		//长发公主番外篇（麻烦不断）.rmvb");//长发公主番外篇（麻烦不断）.rmvb");//Wildlife.wmv");//01.avi");
		//"D:/MyEclipse for Spring/workspace/appcel/appcel-service-encoder/target/test-classes/tempath/video/wKg4Q1c1iMaAT1uXAEmTktBBsV4183/wKg4Q1c1iMaAT1uXAEmTktBBsV4183.wmv"
		File sourceFile = new File("D:\\FFmpeg\\test");//EOS的应用.mp4");//长发公主番外篇（麻烦不断）.rmvb
		String destFilePath = "D:/FFmpeg/test/destination/";
		//String destFilePath = "D:/FFmpeg/test/destination/testbeizi.png";
		//String watermarkPath = "D:/FFmpeg/test/watermark.png";

		long startTime = System.currentTimeMillis();
		try {

			MediaEncoder encoder = new MediaEncoder();
			MediaRecord mediaRecord = new MediaRecord();

			//			encoder.encoderCaptureImage(sourceFile, destFilePath, "jpg", mediaRecord);
			//			System.out.println("Capture frame to image Time consuming : "
			//					+ DurationUtil.formatMS2DTMSMS(System.currentTimeMillis() - startTime));

			//						startTime = System.currentTimeMillis();
			String fileName = sourceFile.getName();
			fileName = fileName.substring(0, fileName.lastIndexOf("."));
			encoder.encodeVideo(sourceFile, destFilePath + "/" + fileName + ".mp4", "mp4", mediaRecord);

			System.out.println("conver to mp4 Time consuming : " + DurationUtil.formatMS2DTMSMS(System.currentTimeMillis() - startTime));

			//			startTime = System.currentTimeMillis();
			//			encoder.appendWatermark(sourceFile, destFilePath, watermarkPath);
			//			System.out.println("Append watermark Time consuming : "
			//					+ DurationUtil.formatMS2DTMSMS(System.currentTimeMillis() - sentartTime));

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InputFormatException e) {
			e.printStackTrace();
		} catch (EncoderException e) {
			e.printStackTrace();
		}

		System.exit(0);
	}
}
