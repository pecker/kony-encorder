/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.encoder.config;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * Defiend class file the EncoderConfiguration.java
 *  
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-7-24
 */
public class EncoderConfiguration {

	private final static Log LOGGER = LogFactory.getLog(EncoderConfiguration.class);

	//## ************视频编码转换配置**********
	private static final String ENCODER_FFMPEG_WIN_HOME = "encoder.ffmpeg.win.home";
	private static final String ENCODER_FFMPEG_LINUX_HOME = "encoder.ffmpeg.linux.home";

	//## 用于flv文件加入metadata
	private static final String ENCODER_YAMDI_WIN_HOME = "encoder.yamdi.win.home";
	//用于mp4文件加入metadata
	private static final String ENCODER_QT_FASTSTART_WIN_HOME = "encoder.qt_faststart.win.home";
	private static final String ENCODER_QT_FASTSTART_LINUX_HOME = "encoder.qt_faststart.linux.home";

	//## 用于转换 Mpeg、avi、rm、rmvb、mkv、DAT、DVD 视频格式文件到 flv
	private static final String ENCODER_MENCODER_WIN_HOME = "encoder.mencoder.win.home";
	private static final String ENCODER_MENCODER_LINUX_HOME = "encoder.mencoder.linux.home";

	//## ********* 转码视频比特率 ********* 
	private static final String ENCODER_VIDEO_BITRATE = "encoder.video.bitrate";
	//## ********* 转码视频宽高 ********* 
	private static final String ENCODER_VIDEO_WIDTH = "encoder.video.width";
	private static final String ENCODER_VIDEO_HEIGHT = "encoder.video.height";

	//## *********视频截图的张数*********
	private static final String ENCODER_CAPTURE_IMAGE_COUNT = "encoder.capture.image.count";
	//## *********视频截图大小160*128 *********
	private static final String ENCODER_CAPTURE_IMAGE_SIZE = "encoder.capture.image.size";
	//## *********视频截图大小160*128 *********
	private static final String ENCODER_CAPTURE_IMAGE_TIME = "encoder.capture.image.time";

	//## ********* 编码器转码目录配置 *********
	private static final String ENCODER_EXECUTE_DIR = "encoder.execute.dir";
	//## ********* 转码后的文件是否保留，默认false，不保留，转码完成后即会删除转码后的文件，以节省磁盘  *********
	private static final String ENCODER_FILE_RETAINED = "encoder.file.retained";

	private static final String ENCODER_CONF_FILE = "encoder.properties";

	private static Map<String, String> encoderConfMap = new HashMap<String, String>();
	private static EncoderConfiguration enconf = new EncoderConfiguration();

	public static EncoderConfiguration getMe() {
		return enconf;
	}

	private EncoderConfiguration() {
		loadEncoderConfigInside();
	}

	public void loadEncoderConfigInside() {
		loadConfig(ENCODER_CONF_FILE);
	}

	private void loadConfig(String name) {
		Properties props = new Properties();
		InputStreamReader is = null;
		try {
			is = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(name), "utf-8");
			props.load(is);
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		}
		try {
			Set<Object> set = props.keySet();
			Iterator<Object> it = set.iterator();
			LOGGER.info(name);
			while (it.hasNext()) {
				String key = it.next().toString();
				Object value = props.get(key);
				encoderConfMap.put((String) key, (String) value);
				LOGGER.info(key + "=" + value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the encoderFfmpegWinHome
	 */
	public String getEncoderFfmpegWinHome() {
		return getValueByKey(ENCODER_FFMPEG_WIN_HOME);
	}

	/**
	 * @return the encoderFfmpegLinuxHome
	 */
	public String getEncoderFfmpegLinuxHome() {
		return getValueByKey(ENCODER_FFMPEG_LINUX_HOME);
	}

	/**
	 * @return the encoderYamdiWinHome
	 */
	public String getEncoderYamdiWinHome() {
		return getValueByKey(ENCODER_YAMDI_WIN_HOME);
	}

	/**
	 * @return the encoderQtFaststartWinHome
	 */
	public String getEncoderQtFaststartWinHome() {
		return getValueByKey(ENCODER_QT_FASTSTART_WIN_HOME);
	}

	/**
	 * @return the encoderQtFaststartLinuxHome
	 */
	public String getEncoderQtFaststartLinuxHome() {
		return getValueByKey(ENCODER_QT_FASTSTART_LINUX_HOME);
	}

	/**
	 * @return the encoderMencoderWinHome
	 */
	public String getEncoderMencoderWinHome() {
		return getValueByKey(ENCODER_MENCODER_WIN_HOME);
	}

	/**
	 * @return the encoderMencoderLinuxHome
	 */
	public String getEncoderMencoderLinuxHome() {
		return getValueByKey(ENCODER_MENCODER_LINUX_HOME);
	}

	/**
	 * @return the encoderVideoBitrate
	 */
	public String getEncoderVideoBitrate() {
		return getValueByKey(ENCODER_VIDEO_BITRATE);
	}

	/**
	 * @return the encoderVideoWidth
	 */
	public String getEncoderVideoWidth() {
		return getValueByKey(ENCODER_VIDEO_WIDTH);
	}

	/**
	 * @return the encoderVideoHeight
	 */
	public String getEncoderVideoHeight() {
		return getValueByKey(ENCODER_VIDEO_HEIGHT);
	}

	/**
	 * @return the encoderCaptureImageCount
	 */
	public String getEncoderCaptureImageCount() {
		return getValueByKey(ENCODER_CAPTURE_IMAGE_COUNT);
	}

	/**
	 * @return the encoderCaptureImageSize
	 */
	public String getEncoderCaptureImageSize() {
		return getValueByKey(ENCODER_CAPTURE_IMAGE_SIZE);
	}

	/**
	 * @return the encoderCaptureImageTime
	 */
	public String getEncoderCaptureImageTime() {
		return getValueByKey(ENCODER_CAPTURE_IMAGE_TIME);
	}

	/**
	 * 获取转码目录
	 * @return the encoderExecuteDir
	 */
	public String getEncoderExecuteDir() {

		String encoderdir = this.getValue(ENCODER_EXECUTE_DIR, "/encoderdir");
		try {
			encoderdir = URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource("").getPath() + encoderdir, "utf-8");
			LOGGER.info(encoderdir);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encoderdir;
	}

	/**
	 * @return the encoderFileRetained
	 */
	public boolean getEncoderFileRetained() {
		return "true".equals(getValueByKey(ENCODER_FILE_RETAINED));
	}

	/**
	 * 根据 Key 获取编码器转码 config 信息
	 * 
	 * @param key
	 * @param defaultValue 默认值
	 * @return
	 */
	public String getValue(String key, String defaultValue) {
		return encoderConfMap.get(key) == null ? defaultValue : encoderConfMap.get(key);
	}

	public String getValueByKey(String key) {
		return getValue(key, "");
	}

	public static void main(String[] args) {
		String tempdir = getMe().getEncoderExecuteDir();
		LOGGER.info(tempdir);
	}
}
