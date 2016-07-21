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
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-10
 */
public enum EncoderPlayModeEnum {
	/**
	 * 点播模式
	 */
	VOD("点播", 0),

	/**
	 * 直播模式
	 */
	LIVE("直播", 1);

	/** 名称，状态描述  */
	private String name;
	/** 枚举值 */
	private int value;

	/** 构造函数 */
	private EncoderPlayModeEnum(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static EncoderPlayModeEnum getEnum(int value) {
		EncoderPlayModeEnum resultEnum = null;
		EncoderPlayModeEnum[] enumAry = EncoderPlayModeEnum.values();
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
		EncoderPlayModeEnum[] ary = EncoderPlayModeEnum.values();
		List list = new ArrayList();
		for (int i = 0; i < ary.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("value", String.valueOf(ary[i].getValue()));
			map.put("desc", ary[i].getName());
			list.add(map);
		}
		return list;
	}
}
