/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.encoder.mode;

import java.io.Serializable;

/**
 * 
 * Defiend class file the AudioInfo.java
 * 
 * 音频信息
 * 
 * Instances of this class report informations about an audio stream that can be
 * decoded.
 *  
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-12-2
 */
public class AudioInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	 * 
	 *  音频 Hz 字符串 单位 b
	 */
	private String audioBitrate = "0";

	public AudioInfo() {
	}

	public AudioInfo(int samplingRate, int channels, String audioBitrate) {
		this.samplingRate = samplingRate;
		this.channels = channels;
		this.audioBitrate = audioBitrate;
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
	public String getAudioBitrate() {
		return audioBitrate;
	}

	/**
	 * Sets the audio stream (average) bit rate.
	 * 
	 * @param bitRate
	 *            The audio stream (average) bit rate.
	 */
	public void setAudioBitrate(String audioBitrate) {
		this.audioBitrate = audioBitrate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((audioBitrate == null) ? 0 : audioBitrate.hashCode());
		result = prime * result + channels;
		result = prime * result + samplingRate;
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
		AudioInfo other = (AudioInfo) obj;
		if (audioBitrate == null) {
			if (other.audioBitrate != null)
				return false;
		} else if (!audioBitrate.equals(other.audioBitrate))
			return false;
		if (channels != other.channels)
			return false;
		if (samplingRate != other.samplingRate)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AudioInfo [samplingRate=" + samplingRate + ", channels=" + channels + ", audioBitrate=" + audioBitrate + "]";
	}
}
