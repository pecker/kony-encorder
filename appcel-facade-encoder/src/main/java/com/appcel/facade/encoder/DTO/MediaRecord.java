/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.encoder.DTO;

/**
 * 
 * Defiend class file the MediaRecord.java
 * 
 *  
 *  
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-7-18
 */
public class MediaRecord {

	private String srcFilePath;
	private String destFilePath;

	// 时长
	private String duration;
	private long timelen;

	// 开始时间
	private String startTime;
	// 单位 b
	private String bitrate;
	// 媒体类型
	private String type;
	// 
	private String size;
	private int height = 0;
	private int width = 0;
	//
	private String fps;
	// 音频 Hz
	private String audioBitrate;

	private String vedioformat;

	private String entityFile;

	// 生成图片数量
	//	private int imageNum = 1;
	//	private String imageFromTime = null;
	//
	//	private static final String defaultImgFrmTime1 = "3";
	//	private static final String defaultImgFrmTime2 = "20";

	//private InputStream captureImage;

	/**
	 * @return the entityFile
	 */
	public String getEntityFile() {
		return entityFile;
	}

	/**
	 * @param entityFile the entityFile to set
	 */
	public void setEntityFile(String entityFile) {
		this.entityFile = entityFile;
	}

	/**
	 * 构造函数
	 */
	public MediaRecord() {
	}

	/**
	 * 构造函数
	 */
	public MediaRecord(String bitrate, String audioBitrate, String size, String fps) {
		this.setBitrate(bitrate);
		this.setAudioBitrate(audioBitrate);
		this.setSize(size);
		this.setFps(fps);
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	/**
	 * @return the timelen
	 */
	public long getTimelen() {
		return timelen;
	}

	/**
	 * @param timelen the timelen to set
	 */
	public void setTimelen(long timelen) {
		this.timelen = timelen;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getBitrate() {
		return bitrate;
	}

	public void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
		if (this.size != null) {
			String[] sizeArray = this.size.split("\\*");
			this.setWidth(Integer.parseInt(sizeArray[0]));
			this.setHeight(Integer.parseInt(sizeArray[1]));
		}
	}

	public String getFps() {
		return fps;
	}

	public void setFps(String fps) {
		this.fps = fps;
	}

	public String getAudioBitrate() {
		return audioBitrate;
	}

	public void setAudioBitrate(String audiorate) {
		this.audioBitrate = audiorate;
	}

	public String getSrcFilePath() {
		return srcFilePath;
	}

	public void setSrcFilePath(String srcFilePath) {
		this.srcFilePath = srcFilePath;
	}

	public String getDestFilePath() {
		return destFilePath;
	}

	public String getDestMp4FilePath() {
		return destFilePath == null ? null : destFilePath.substring(0, destFilePath.lastIndexOf(".")) + ".mp4";
	}

	public void setDestFilePath(String destFilePath) {
		this.destFilePath = destFilePath;
	}

	public String getVedioformat() {
		return vedioformat;
	}

	public void setVedioformat(String vformat) {
		this.vedioformat = vformat;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	//	public String getImageFromTime() {
	//		return imageFromTime;
	//	}
	//
	//	public void setImageFromTime(String imageFromTime) {
	//		this.imageFromTime = imageFromTime;
	//	}
	//
	//	public String getDefaultImgFrmTime1() {
	//		return defaultImgFrmTime1;
	//	}
	//
	//	public String getDefaultImgFrmTime2() {
	//		return defaultImgFrmTime2;
	//	}
	//
	//	public int getImageNum() {
	//		return imageNum;
	//	}
	//
	//	public void setImageNum(int imageNum) {
	//		this.imageNum = imageNum;
	//	}

	//	/**
	//	 * @return the captureImage
	//	 */
	//	public InputStream getCaptureImage() {
	//		return captureImage;
	//	}
	//
	//	/**
	//	 * @param captureImage the captureImage to set
	//	 */
	//	public void setCaptureImage(InputStream captureImage) {
	//		this.captureImage = captureImage;
	//	}

	@Override
	public String toString() {
		return "MediaRecord [srcFilePath=" + srcFilePath + ", destFilePath=" + destFilePath + ", duration=" + duration + ", timelen="
				+ timelen + ", startTime=" + startTime + ", bitrate=" + bitrate + ", type=" + type + ", size=" + size + ", height="
				+ height + ", width=" + width + ", fps=" + fps + ", audioBitrate=" + audioBitrate + ", vedioformat=" + vedioformat + "]";
	}

}
