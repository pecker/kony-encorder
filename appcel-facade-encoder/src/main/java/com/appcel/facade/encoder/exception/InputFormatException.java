/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.encoder.exception;


/**
 * 
 * Defiend class file the InputFormatException.java
 * 
* This expection is thrown if a source file format is not recognized.
*   
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-12-2
 */
public class InputFormatException extends EncoderException {

	private static final long serialVersionUID = 1L;

	public InputFormatException() {
		super();
	}

	public InputFormatException(String message) {
		super(message);
	}

}
