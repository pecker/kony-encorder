/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.encoder.mode;

import java.io.Serializable;

/**
 * 
 * Defiend class file the SubtitleInfo.java
 * 
 * 视频多媒体字幕信息 
 *  
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-12-9
 */
public class SubtitleFileInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 字幕文件存放路径
	 */
	private String subtitleFilePath;
	/**
	 * 字幕文件大小
	 */
	private long subtitleFileSize;

	public SubtitleFileInfo() {
	}

	public SubtitleFileInfo(String subtitleFilePath, long subtitleFileSize) {
		this.subtitleFilePath = subtitleFilePath;
		this.subtitleFileSize = subtitleFileSize;
	}

	public String getSubtitleFilePath() {
		return subtitleFilePath;
	}

	public void setSubtitleFilePath(String subtitleFilePath) {
		this.subtitleFilePath = subtitleFilePath;
	}

	public long getSubtitleFileSize() {
		return subtitleFileSize;
	}

	public void setSubtitleFileSize(long subtitleFileSize) {
		this.subtitleFileSize = subtitleFileSize;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subtitleFilePath == null) ? 0 : subtitleFilePath.hashCode());
		result = prime * result + (int) (subtitleFileSize ^ (subtitleFileSize >>> 32));
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
		SubtitleFileInfo other = (SubtitleFileInfo) obj;
		if (subtitleFilePath == null) {
			if (other.subtitleFilePath != null)
				return false;
		} else if (!subtitleFilePath.equals(other.subtitleFilePath))
			return false;
		if (subtitleFileSize != other.subtitleFileSize)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SubtitleFileInfo [subtitleFilePath=" + subtitleFilePath + ", subtitleFileSize=" + subtitleFileSize + "]";
	}

}
