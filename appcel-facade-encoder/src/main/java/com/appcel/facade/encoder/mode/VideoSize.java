/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.encoder.mode;

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
public class VideoSize implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 分辨率
	 */
	private String sizex;
	/**
	 * The video width.
	 */
	private int width;

	/**
	 * The video height.
	 */
	private int height;

	public VideoSize() {
	}

	/**
	 * It builds the bean.
	 * 
	 * @param width
	 *            The video width.
	 * @param height
	 *            The video height.
	 */
	public VideoSize(String sizex, int width, int height) {
		this.sizex = sizex;
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

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the sizex
	 */
	public String getSizex() {
		return sizex;
	}

	/**
	 * @param sizex the sizex to set
	 */
	public void setSizex(String sizex) {
		this.sizex = sizex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + ((sizex == null) ? 0 : sizex.hashCode());
		result = prime * result + width;
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
		VideoSize other = (VideoSize) obj;
		if (height != other.height)
			return false;
		if (sizex == null) {
			if (other.sizex != null)
				return false;
		} else if (!sizex.equals(other.sizex))
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VideoSize [sizex=" + sizex + ", width=" + width + ", height=" + height + "]";
	}

}
