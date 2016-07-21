/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.encoder.DTO;

/**
 * 
 * Defiend class file the AudioInfo.java
 * 
 * Instances of this class report informations about an audio stream that can be
 * decoded.
 *  
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-12-2
 */
public class AudioInfoDTO {

	/**
	 * The audio stream decoder name.
	 */
	private String decoder;

	/**
	 * The audio stream sampling rate. If less than 0, this information is not
	 * available.
	 */
	private int samplingRate = -1;

	/**
	 * The audio stream channels number (1=mono, 2=stereo). If less than 0, this
	 * information is not available.
	 */
	private int channels = -1;

	/**
	 * The audio stream (average) bit rate. If less than 0, this information is
	 * not available.
	 */
	private int bitRate = -1;

	/**
	 * Returns the audio stream decoder name.
	 * 
	 * @return The audio stream decoder name.
	 */
	public String getDecoder() {
		return decoder;
	}

	/**
	 * Sets the audio stream decoder name.
	 * 
	 * @param decoder
	 *            The audio stream decoder name.
	 */
	public void setDecoder(String format) {
		this.decoder = format;
	}

	/**
	 * Returns the audio stream sampling rate. If less than 0, this information
	 * is not available.
	 * 
	 * @return The audio stream sampling rate.
	 */
	public int getSamplingRate() {
		return samplingRate;
	}

	/**
	 * Sets the audio stream sampling rate.
	 * 
	 * @param samplingRate
	 *            The audio stream sampling rate.
	 */
	public void setSamplingRate(int samplingRate) {
		this.samplingRate = samplingRate;
	}

	/**
	 * Returns the audio stream channels number (1=mono, 2=stereo). If less than
	 * 0, this information is not available.
	 * 
	 * @return the channels The audio stream channels number (1=mono, 2=stereo).
	 */
	public int getChannels() {
		return channels;
	}

	/**
	 * Sets the audio stream channels number (1=mono, 2=stereo).
	 * 
	 * @param channels
	 *            The audio stream channels number (1=mono, 2=stereo).
	 */
	public void setChannels(int channels) {
		this.channels = channels;
	}

	/**
	 * Returns the audio stream (average) bit rate. If less than 0, this
	 * information is not available.
	 * 
	 * @return The audio stream (average) bit rate.
	 */
	public int getBitRate() {
		return bitRate;
	}

	/**
	 * Sets the audio stream (average) bit rate.
	 * 
	 * @param bitRate
	 *            The audio stream (average) bit rate.
	 */
	public void setBitRate(int bitRate) {
		this.bitRate = bitRate;
	}

	public String toString() {
		return getClass().getName() + " (decoder=" + decoder + ", samplingRate=" + samplingRate + ", channels="
				+ channels + ", bitRate=" + bitRate + ")";
	}

}
