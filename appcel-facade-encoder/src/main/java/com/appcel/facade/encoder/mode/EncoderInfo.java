/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.encoder.mode;

import java.util.Date;

import com.appcel.facade.encoder.enums.EncoderPlayModeEnum;
import com.appcel.facade.encoder.enums.EncoderStatusEnum;
import com.appcel.kernel.mybatis.model.BaseModel;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-11
 */
public class EncoderInfo extends BaseModel {
	private static final long serialVersionUID = 1L;

	/**
	 * 转换文件名
	 */
	private String fileName;
	/**
	 * 转换文件播放模式：直播、点播
	 */
	private Integer playMode = EncoderPlayModeEnum.VOD.getValue();

	/**
	 * 原始文件存放路径
	 */
	private String srcFilePath;

	/**
	 * 转换后文件存放路径
	 */
	private String destFilePath;

	/**
	 * 截图文件存放路径
	 */
	private String captureImgPath;

	/**
	 * 关联的资源或者datastream的key
	 */
	private String entityKey;
	/**
	 * 文件转换状态
	 */
	private Integer status = EncoderStatusEnum.STATUS_WAIT_UPLOAD.getValue();
	/**
	 * 注意事项
	 */
	private String remark;

	/**
	 * 最后转换完成时间
	 */
	protected Date encoderTime;

	/**
	 * 提取视频字幕文件信息
	 */
	private SubtitleFileInfo subtitle;
	/**
	 * 提取视频音频文件信息
	 */
	private VoiceFileInfo voice;

	public EncoderInfo() {
	}

	public EncoderInfo(String entityKey, String fileName, String srcFilePath, EncoderStatusEnum status, String remark) {
		this.entityKey = entityKey;
		this.fileName = fileName;
		this.srcFilePath = srcFilePath;
		this.status = status.getValue();
		this.remark = remark;
	}

	public EncoderInfo(String entityKey, String fileName, String srcFilePath, EncoderPlayModeEnum playMode, EncoderStatusEnum status,
			String remark) {
		this.entityKey = entityKey;
		this.fileName = fileName;
		this.srcFilePath = srcFilePath;
		this.playMode = playMode.getValue();
		this.status = status.getValue();
		this.remark = remark;
	}

	public EncoderInfo(String entityKey, String fileName, String srcFilePath, String destFilePath, String captureImgPath,
			EncoderPlayModeEnum playMode, EncoderStatusEnum status, String remark) {
		this.entityKey = entityKey;
		this.fileName = fileName;
		this.srcFilePath = srcFilePath;
		this.destFilePath = destFilePath;
		this.captureImgPath = captureImgPath;
		this.playMode = playMode.getValue();
		this.status = status.getValue();
		this.remark = remark;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the playMode
	 */
	public Integer getPlayMode() {
		return playMode;
	}

	/**
	 * @param playMode the playMode to set
	 */
	public void setPlayMode(Integer playMode) {
		this.playMode = playMode;
	}

	/**
	 * @return the srcFilePath
	 */
	public String getSrcFilePath() {
		return srcFilePath;
	}

	/**
	 * @param srcFilePath the srcFilePath to set
	 */
	public void setSrcFilePath(String srcFilePath) {
		this.srcFilePath = srcFilePath;
	}

	/**
	 * @return the destFilePath
	 */
	public String getDestFilePath() {
		return destFilePath;
	}

	/**
	 * @param destFilePath the destFilePath to set
	 */
	public void setDestFilePath(String destFilePath) {
		this.destFilePath = destFilePath;
	}

	/**
	 * @return the captureImgPath
	 */
	public String getCaptureImgPath() {
		return captureImgPath;
	}

	/**
	 * @param captureImgPath the captureImgPath to set
	 */
	public void setCaptureImgPath(String captureImgPath) {
		this.captureImgPath = captureImgPath;
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
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the encoderTime
	 */
	public Date getEncoderTime() {
		return encoderTime;
	}

	/**
	 * @param encoderTime the encoderTime to set
	 */
	public void setEncoderTime(Date encoderTime) {
		this.encoderTime = encoderTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((captureImgPath == null) ? 0 : captureImgPath.hashCode());
		result = prime * result + ((destFilePath == null) ? 0 : destFilePath.hashCode());
		result = prime * result + ((entityKey == null) ? 0 : entityKey.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((playMode == null) ? 0 : playMode.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((srcFilePath == null) ? 0 : srcFilePath.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		EncoderInfo other = (EncoderInfo) obj;
		if (captureImgPath == null) {
			if (other.captureImgPath != null)
				return false;
		} else if (!captureImgPath.equals(other.captureImgPath))
			return false;
		if (destFilePath == null) {
			if (other.destFilePath != null)
				return false;
		} else if (!destFilePath.equals(other.destFilePath))
			return false;
		if (entityKey == null) {
			if (other.entityKey != null)
				return false;
		} else if (!entityKey.equals(other.entityKey))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (playMode == null) {
			if (other.playMode != null)
				return false;
		} else if (!playMode.equals(other.playMode))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (srcFilePath == null) {
			if (other.srcFilePath != null)
				return false;
		} else if (!srcFilePath.equals(other.srcFilePath))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

	public SubtitleFileInfo getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(SubtitleFileInfo subtitle) {
		this.subtitle = subtitle;
	}

	public VoiceFileInfo getVoice() {
		return voice;
	}

	public void setVoice(VoiceFileInfo voice) {
		this.voice = voice;
	}

	@Override
	public String toString() {
		return "EncoderInfo [id=" + getId() + ",key=" + getKey() + ", version=" + getVersion() + ",fileName=" + fileName + ", playMode="
				+ playMode + ", srcFilePath=" + srcFilePath + ", destFilePath=" + destFilePath + ", captureImgPath=" + captureImgPath
				+ ", entityKey=" + entityKey + ", status=" + status + ", remark=" + remark + "]";
	}

}
