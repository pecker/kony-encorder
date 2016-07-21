/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.encoder.mode;

import com.appcel.kernel.mybatis.model.BaseModel;

/**
 * 
 * Defiend class file the Multimedia.java
 * 
 * 多媒体文件信息，通过转换服务抽取出多媒体的属性信息 
 *  
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-12-9
 */
public class Multimedia extends BaseModel {
	private static final long serialVersionUID = 1L;
	// 时长格式00:01:08.17
	private String duration;
	// 媒体时长以秒(ss)为单位，格式为：timelen=68
	private long timelen;
	//编码库类型，比如：h264 (libx264) ([33][0][0][0] / 0x0021)
	private String type;
	// 开始时间
	private String startTime;

	private String fps;
	/**
	 * A set of audio-specific informations. If null, there's no audio stream in
	 * the multimedia file.
	 */
	private AudioInfo audio = null;
	/**
	 * A set of video-specific informations. If null, there's no video stream in
	 * the multimedia file.
	 */
	private VideoInfo video = null;

	/**
	 * 关联的资源或者datastream的key
	 */
	private String entityKey;

	public Multimedia() {
	}

	/**
	 * 构造函数
	 */
	public Multimedia(AudioInfo audio, VideoInfo video, String entityKey, String startTime) {
		this.entityKey = entityKey;
		this.startTime = startTime;
		this.audio = audio;
		this.video = video;
	}

	/**
	 * @return the audio
	 */
	public AudioInfo getAudio() {
		return audio;
	}

	/**
	 * @param audio the audio to set
	 */
	public void setAudio(AudioInfo audio) {
		this.audio = audio;
	}

	/**
	 * @return the video
	 */
	public VideoInfo getVideo() {
		return video;
	}

	/**
	 * @param video the video to set
	 */
	public void setVideo(VideoInfo video) {
		this.video = video;
	}

	/**
	 * @return the entityKey
	 */
	public String getEntityKey() {
		return entityKey;
	}

	/**
	 * @param entityKey the entityKey to set
	 */
	public void setEntityKey(String entityKey) {
		this.entityKey = entityKey;
	}

	/**
	 * Returns the stream duration in millis. If less than 0 this information is
	 * not available.
	 * 
	 * @return The stream duration in millis. If less than 0 this information is
	 *         not available.
	 */
	public String getDuration() {
		return duration;
	}

	/**
	 * Sets the stream duration in millis.
	 * 
	 * @param duration
	 *            The stream duration in millis.
	 */
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

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	public String getFps() {
		return fps;
	}

	public void setFps(String fps) {
		this.fps = fps;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((audio == null) ? 0 : audio.hashCode());
		result = prime * result + ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((entityKey == null) ? 0 : entityKey.hashCode());
		result = prime * result + ((fps == null) ? 0 : fps.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + (int) (timelen ^ (timelen >>> 32));
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((video == null) ? 0 : video.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Multimedia other = (Multimedia) obj;
		if (audio == null) {
			if (other.audio != null)
				return false;
		} else if (!audio.equals(other.audio))
			return false;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (entityKey == null) {
			if (other.entityKey != null)
				return false;
		} else if (!entityKey.equals(other.entityKey))
			return false;
		if (fps == null) {
			if (other.fps != null)
				return false;
		} else if (!fps.equals(other.fps))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (timelen != other.timelen)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (video == null) {
			if (other.video != null)
				return false;
		} else if (!video.equals(other.video))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Multimedia [duration=" + duration + ", timelen=" + timelen + ", type=" + type + ", startTime=" + startTime + ", fps=" + fps
				+ ", audio=" + audio + ", video=" + video + ", entityKey=" + entityKey + "]";
	}

}
