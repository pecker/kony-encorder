/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.encoder.mode;

import java.io.Serializable;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-25
 */
public class VoiceFileInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 音频文件存放路径
	 */
	private String voiceFilePath;
	/**
	 * 音频文件大小
	 */
	private long voiceFileSize;

	public VoiceFileInfo() {
	}

	public VoiceFileInfo(String voiceFilePath, long voiceFileSize) {
		this.voiceFilePath = voiceFilePath;
		this.voiceFileSize = voiceFileSize;
	}

	public String getVoiceFilePath() {
		return voiceFilePath;
	}

	public void setVoiceFilePath(String voiceFilePath) {
		this.voiceFilePath = voiceFilePath;
	}

	public long getVoiceFileSize() {
		return voiceFileSize;
	}

	public void setVoiceFileSize(long voiceFileSize) {
		this.voiceFileSize = voiceFileSize;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((voiceFilePath == null) ? 0 : voiceFilePath.hashCode());
		result = prime * result + (int) (voiceFileSize ^ (voiceFileSize >>> 32));
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
		VoiceFileInfo other = (VoiceFileInfo) obj;
		if (voiceFilePath == null) {
			if (other.voiceFilePath != null)
				return false;
		} else if (!voiceFilePath.equals(other.voiceFilePath))
			return false;
		if (voiceFileSize != other.voiceFileSize)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VoiceFileInfo [voiceFilePath=" + voiceFilePath + ", voiceFileSize=" + voiceFileSize + "]";
	}

}
