/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.encoder.DTO;


/**
 * 
 * Defiend class file the MultimediaInfo.java
 * 
 * Instances of this class report informations about a decoded multimedia file. 
 *  
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-12-2
 */
public class MultimediaInfo {

	/**
	 * The multimedia file format name.
	 */
	private String format = null;

	/**
	 * The stream duration in millis. If less than 0 this information is not
	 * available.
	 */
	private long duration = -1;

	/**
	 * A set of audio-specific informations. If null, there's no audio stream in
	 * the multimedia file.
	 */
	private AudioInfoDTO audio = null;

	/**
	 * A set of video-specific informations. If null, there's no video stream in
	 * the multimedia file.
	 */
	private VideoInfoDTO video = null;

	/**
	 * Returns the multimedia file format name.
	 * 
	 * @return The multimedia file format name.
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Sets the multimedia file format name.
	 * 
	 * @param format
	 *            The multimedia file format name.
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * Returns the stream duration in millis. If less than 0 this information is
	 * not available.
	 * 
	 * @return The stream duration in millis. If less than 0 this information is
	 *         not available.
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * Sets the stream duration in millis.
	 * 
	 * @param duration
	 *            The stream duration in millis.
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * Returns a set of audio-specific informations. If null, there's no audio
	 * stream in the multimedia file.
	 * 
	 * @return A set of audio-specific informations.
	 */
	public AudioInfoDTO getAudio() {
		return audio;
	}

	/**
	 * Sets a set of audio-specific informations.
	 * 
	 * @param audio
	 *            A set of audio-specific informations.
	 */
	public void setAudio(AudioInfoDTO audio) {
		this.audio = audio;
	}

	/**
	 * Returns a set of video-specific informations. If null, there's no video
	 * stream in the multimedia file.
	 * 
	 * @return A set of audio-specific informations.
	 */
	public VideoInfoDTO getVideo() {
		return video;
	}

	/**
	 * Sets a set of video-specific informations.
	 * 
	 * @param video
	 *            A set of video-specific informations.
	 */
	public void setVideo(VideoInfoDTO video) {
		this.video = video;
	}

	public String toString() {
		return getClass().getName() + " (format=" + format + ", duration=" + duration + ", video=" + video + ", audio="
				+ audio + ")";
	}

}
