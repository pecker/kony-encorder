/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.encoder.DTO;

import java.io.Serializable;

/**
 * 
 * Defiend class file the AudioAttributes.java
 * 
 * Attributes controlling the audio encoding process. 
 *  
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-12-2
 */
public class AudioAttributes implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * This value can be setted in the codec field to perform a direct stream
	 * copy, without re-encoding of the audio stream.
	 */
	public static final String DIRECT_STREAM_COPY = "copy";

	/**
	 * The codec name for the encoding process. If null or not specified the
	 * encoder will perform a direct stream copy.
	 */
	private String codec = null;

	/**
	 * The bitrate value for the encoding process. If null or not specified a
	 * default value will be picked.
	 */
	private Integer bitRate = null;

	/**
	 * The samplingRate value for the encoding process. If null or not specified
	 * a default value will be picked.
	 */
	private Integer samplingRate = null;

	/**
	 * The channels value (1=mono, 2=stereo) for the encoding process. If null
	 * or not specified a default value will be picked.
	 */
	private Integer channels = null;

	/**
	 * The volume value for the encoding process. If null or not specified a
	 * default value will be picked. If 256 no volume change will be performed.
	 */
	private Integer volume = null;

	/**
	 * Returns the codec name for the encoding process.
	 * 
	 * @return The codec name for the encoding process.
	 */
	public String getCodec() {
		return codec;
	}

	/**
	 * Sets the codec name for the encoding process. If null or not specified
	 * the encoder will perform a direct stream copy.
	 * 
	 * Be sure the supplied codec name is in the list returned by
	 * {@link Encoder#getAudioEncoders()}.
	 * 
	 * A special value can be picked from
	 * {@link AudioAttributes#DIRECT_STREAM_COPY}.
	 * 
	 * @param codec
	 *            The codec name for the encoding process.
	 */
	public void setCodec(String codec) {
		this.codec = codec;
	}

	/**
	 * Returns the bitrate value for the encoding process.
	 * 
	 * @return The bitrate value for the encoding process.
	 */
	public Integer getBitRate() {
		return bitRate;
	}

	/**
	 * Sets the bitrate value for the encoding process. If null or not specified
	 * a default value will be picked.
	 * 
	 * @param bitRate
	 *            The bitrate value for the encoding process.
	 */
	public void setBitRate(Integer bitRate) {
		this.bitRate = bitRate;
	}

	/**
	 * Returns the samplingRate value for the encoding process.
	 * 
	 * @return the samplingRate The samplingRate value for the encoding process.
	 */
	public Integer getSamplingRate() {
		return samplingRate;
	}

	/**
	 * Sets the samplingRate value for the encoding process. If null or not
	 * specified a default value will be picked.
	 * 
	 * @param samplingRate
	 *            The samplingRate value for the encoding process.
	 */
	public void setSamplingRate(Integer samplingRate) {
		this.samplingRate = samplingRate;
	}

	/**
	 * Returns the channels value (1=mono, 2=stereo) for the encoding process.
	 * 
	 * @return The channels value (1=mono, 2=stereo) for the encoding process.
	 */
	public Integer getChannels() {
		return channels;
	}

	/**
	 * Sets the channels value (1=mono, 2=stereo) for the encoding process. If
	 * null or not specified a default value will be picked.
	 * 
	 * @param channels
	 *            The channels value (1=mono, 2=stereo) for the encoding
	 *            process.
	 */
	public void setChannels(Integer channels) {
		this.channels = channels;
	}

	/**
	 * Returns the volume value for the encoding process.
	 * 
	 * @return The volume value for the encoding process.
	 */
	public Integer getVolume() {
		return volume;
	}

	/**
	 * Sets the volume value for the encoding process. If null or not specified
	 * a default value will be picked. If 256 no volume change will be
	 * performed.
	 * 
	 * @param volume
	 *            The volume value for the encoding process.
	 */
	public void setVolume(Integer volume) {
		this.volume = volume;
	}

	public String toString() {
		return getClass().getName() + "(codec=" + codec + ", bitRate=" + bitRate + ", samplingRate=" + samplingRate
				+ ", channels=" + channels + ", volume=" + volume + ")";
	}

}
