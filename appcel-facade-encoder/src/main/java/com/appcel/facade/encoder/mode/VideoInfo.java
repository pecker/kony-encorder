/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.encoder.mode;

import java.io.Serializable;

/**
 * 
 * Defiend class file the VideoInfo.java
 * 
 * Instances of this class report informations about a video stream that can be
 * decoded. 
 *  
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-12-2
 */
public class VideoInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The video size. If null this information is not available.
	 */
	private VideoSize size = null;

	/**
	 * The video stream (average) bit rate. If less than 0, this information is
	 * not available.
	 * 音频 Hz，单位为：kb/s
	 */
	// 单位 b
	private String videoBitrate;

	/**
	 * The video frame rate. If less than 0 this information is not available.
	 */
	private float frameRate = -1;

	/**
	 * 视频格式，如：yuv420p
	 */
	private String vedioFormat;

	/**
	 * @return the vedioformat
	 */
	public String getVedioFormat() {
		return vedioFormat;
	}

	/**
	 * @param vedioformat the vedioformat to set
	 */
	public void setVedioFormat(String vedioFormat) {
		this.vedioFormat = vedioFormat;
	}

	public VideoInfo() {
	}

	public VideoInfo(String videoBitrate, float frameRate, String vedioFormat, String sizex, int width, int height) {
		this.videoBitrate = videoBitrate;
		this.frameRate = frameRate;
		this.vedioFormat = vedioFormat;
		size = new VideoSize(sizex, width, height);
	}

	/**
	 * Returns the video size. If null this information is not available.
	 * 
	 * @return the size The video size.
	 */
	public VideoSize getSize() {
		return size;
	}

	/**
	 * Sets the video size.
	 * 
	 * @param size
	 *            The video size.
	 */
	public void setSize(VideoSize size) {
		this.size = size;
		//		this.size = size;
		//		if (this.size != null) {
		//			String[] sizeArray = this.size.split("\\*");
		//			this.setWidth(Integer.parseInt(sizeArray[0]));
		//			this.setHeight(Integer.parseInt(sizeArray[1]));
		//		}
	}

	/**
	 * Returns the video frame rate. If less than 0 this information is not
	 * available.
	 * 
	 * @return The video frame rate.
	 */
	public float getFrameRate() {
		return frameRate;
	}

	/**
	 * Sets the video frame rate.
	 * 
	 * @param frameRate
	 *            The video frame rate.
	 */
	public void setFrameRate(float frameRate) {
		this.frameRate = frameRate;
	}

	/**
	 * Returns the video stream (average) bit rate. If less than 0, this
	 * information is not available.
	 * 
	 * @return The video stream (average) bit rate.
	 */
	public String getVideoBitrate() {
		return videoBitrate;
	}

	/**
	 * Sets the video stream (average) bit rate.
	 * 
	 * @param bitRate
	 *            The video stream (average) bit rate.
	 */
	public void setVideoBitrate(String bitrate) {
		this.videoBitrate = bitrate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(frameRate);
		result = prime * result + ((size == null) ? 0 : size.hashCode());
		result = prime * result + ((vedioFormat == null) ? 0 : vedioFormat.hashCode());
		result = prime * result + ((videoBitrate == null) ? 0 : videoBitrate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VideoInfo other = (VideoInfo) obj;
		if (Float.floatToIntBits(frameRate) != Float.floatToIntBits(other.frameRate))
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		if (vedioFormat == null) {
			if (other.vedioFormat != null)
				return false;
		} else if (!vedioFormat.equals(other.vedioFormat))
			return false;
		if (videoBitrate == null) {
			if (other.videoBitrate != null)
				return false;
		} else if (!videoBitrate.equals(other.videoBitrate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VideoInfo [size=" + size + ", videoBitrate=" + videoBitrate + ", frameRate=" + frameRate + ", vedioFormat=" + vedioFormat
				+ "]";
	}

}
