/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.encoder.executor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.appcel.facade.encoder.DTO.MediaRecord;
import com.appcel.facade.encoder.exception.EncoderException;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-11
 */
public interface EncoderExecutor {

	boolean isLinuxos();

	/**
	 * Adds an argument to the encoderExcutor executable call.
	 * 
	 * @param arg
	 *            The argument.
	 */
	void addArgument(String arg);

	/**
	 * Executes the encoderExcutor process with the previous given arguments.
	 * 
	 * @throws IOException
	 *             If the process call fails.
	 */
	void execute() throws EncoderException;

	void execute(File directory) throws EncoderException;

	void execute(MediaRecord record) throws EncoderException;

	void execute(MediaRecord record, File directory) throws EncoderException;

	/**
	 * Returns a stream reading from the encoderExcutor process standard output channel.
	 * 
	 * @return A stream reading from the encoderExcutor process standard output channel.
	 */
	InputStream getInputStream();

	/**
	 * Returns a stream writing in the encoderExcutor process standard input channel.
	 * 
	 * @return A stream writing in the encoderExcutor process standard input channel.
	 */
	OutputStream getOutputStream();

	/**
	 * Returns a stream reading from the encoderExcutor process standard error channel.
	 * 
	 * @return A stream reading from the encoderExcutor process standard error channel.
	 */
	InputStream getErrorStream();

	/**
	 * If there's a encoderExcutor execution in progress, it kills it.
	 */
	void destroy();

}