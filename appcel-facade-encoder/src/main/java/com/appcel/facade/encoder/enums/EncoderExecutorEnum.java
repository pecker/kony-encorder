/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.encoder.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 转换器枚举
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-11
 */
public enum EncoderExecutorEnum {
	FFMPEGEXECUTOR("ffmpeg 转换执行器", "ffmpeg"), MENCODEREXECUTOR("mencoder 转换执行器", "mencoder"), YAMDIEXECUTOR(
			"yamdi flv格式流媒体关键帧注入执行器", "yamdi"), QT_FASTSTART("qt-faststart mp4格式流媒体播放头部信息处理器", "qt-faststart");

	/** 名称，状态描述  */
	private String name;
	/** 枚举值 */
	private String value;

	/** 构造函数 */
	private EncoderExecutorEnum(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static EncoderExecutorEnum getEnum(String value) {
		EncoderExecutorEnum resultEnum = null;
		EncoderExecutorEnum[] enumAry = EncoderExecutorEnum.values();
		for (int i = 0; i < enumAry.length; i++) {
			if (enumAry[i].getValue() == value) {
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList() {
		EncoderExecutorEnum[] ary = EncoderExecutorEnum.values();
		List list = new ArrayList();
		for (int i = 0; i < ary.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("value", String.valueOf(ary[i].getValue()));
			map.put("name", ary[i].getName());
			list.add(map);
		}
		return list;
	}
}
