/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.encoder.DTO;

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
public class VideoInfoDTO {

	/**
	 * The video stream decoder name.
	 */
	private String decoder;

	/**
	 * The video size. If null this information is not available.
	 */
	private VideoSizeDTO size = null;

	/**
	 * The video stream (average) bit rate. If less than 0, this information is
	 * not available.
	 */
	private int bitRate = -1;

	/**
	 * The video frame rate. If less than 0 this information is not available.
	 */
	private float frameRate = -1;

	/**
	 * Returns the video stream decoder name.
	 * 
	 * @return The video stream decoder name.
	 */
	public String getDecoder() {
		return decoder;
	}

	/**
	 * Sets the video stream decoder name.
	 * 
	 * @param decoder
	 *            The video stream decoder name.
	 */
	public void setDecoder(String codec) {
		this.decoder = codec;
	}

	/**
	 * Returns the video size. If null this information is not available.
	 * 
	 * @return the size The video size.
	 */
	public VideoSizeDTO getSize() {
		return size;
	}

	/**
	 * Sets the video size.
	 * 
	 * @param size
	 *            The video size.
	 */
	public void setSize(VideoSizeDTO size) {
		this.size = size;
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
	public int getBitRate() {
		return bitRate;
	}

	/**
	 * Sets the video stream (average) bit rate.
	 * 
	 * @param bitRate
	 *            The video stream (average) bit rate.
	 */
	public void setBitRate(int bitRate) {
		this.bitRate = bitRate;
	}

	public String toString() {
		return getClass().getName() + " (decoder=" + decoder + ", size=" + size + ", bitRate=" + bitRate
				+ ", frameRate=" + frameRate + ")";
	}
}
