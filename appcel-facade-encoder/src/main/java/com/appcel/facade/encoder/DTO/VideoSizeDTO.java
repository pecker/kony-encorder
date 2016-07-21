/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.encoder.DTO;

import java.io.Serializable;

/**
 * 
 * Defiend class file the VideoSize.java
 * 
 * Instances of this class report informations about videos size. 
 *  
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-12-2
 */
public class VideoSizeDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The video width.
	 */
	private int width;

	/**
	 * The video height.
	 */
	private int height;

	/**
	 * It builds the bean.
	 * 
	 * @param width
	 *            The video width.
	 * @param height
	 *            The video height.
	 */
	public VideoSizeDTO(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns the video width.
	 * 
	 * @return The video width.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the video height.
	 * 
	 * @return The video height.
	 */
	public int getHeight() {
		return height;
	}

	public String toString() {
		return getClass().getName() + " (width=" + width + ", height=" + height + ")";
	}

}
