/*
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * Encoder A Java Audio/Video Encoder (based on FFMPEG)
 * 
 * Copyright (C) 2006-2014 LiRongSheng (http://www.pointdew.com)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.pointdew.com>.
 */
package com.appcel.core.encoder.executor;

/**
 * 
 * Defiend class file the FFMPEGExecutor.java
 * 
 * A ffmpeg process wrapper.
 *   
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-12-2
 */
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.appcel.facade.encoder.DTO.MediaRecord;
import com.appcel.facade.encoder.exception.EncoderException;

public class FfmpegEncoderExecutor implements EncoderExecutor {
	private static Log LOGGER = LogFactory.getLog(FfmpegEncoderExecutor.class);
	/**
	 * The path of the ffmpeg executable.
	 */
	private String executablePath;

	/**
	 * Arguments for the executable.
	 */
	private List<String> args = null;

	/**
	 * The process representing the ffmpeg execution.
	 */
	private Process ffmpeg = null;

	/**
	 * A process killer to kill the ffmpeg process with a shutdown hook, useful
	 * if the jvm execution is shutted down during an ongoing encoding process.
	 */
	private ProcessKiller ffmpegKiller = null;

	/**
	 * A stream reading from the ffmpeg process standard output channel.
	 */
	private InputStream inputStream = null;

	/**
	 * A stream writing in the ffmpeg process standard input channel.
	 */
	private OutputStream outputStream = null;

	/**
	 * A stream reading from the ffmpeg process standard error channel.
	 */
	private InputStream errorStream = null;

	private boolean linuxosed;

	public boolean isLinuxos() {
		return linuxosed;
	}

	/**
	 * It build the executor.
	 * 
	 * @param ffmpegExecutablePath
	 *            The path of the ffmpeg executable.
	 */
	public FfmpegEncoderExecutor(String executablePath, boolean linuxosed) {
		this.executablePath = executablePath;
		this.linuxosed = linuxosed;
		args = new LinkedList<String>();
		args.add(executablePath);
	}

	/**
	 * Adds an argument to the ffmpeg executable call.
	 * 
	 * @param arg
	 *            The argument.
	 */
	public void addArgument(String arg) {
		args.add(arg);
	}

	public void execute() throws EncoderException {
		File file = null;
		execute(file);
	}

	/**
	 * Executes the ffmpeg process with the previous given arguments.
	 * 
	 * @throws IOException
	 *             If the process call fails.
	 */
	public void execute(File directory) throws EncoderException {

		LOGGER.info("==========>>> 进入 ffmpeg 转码中...");
		try {
			int argsSize = args.size();
			String[] cmd = new String[argsSize + 1];
			cmd[0] = executablePath;
			for (int i = 0; i < argsSize; i++) {
				cmd[i + 1] = args.get(i);
			}

			LOGGER.info("Ffmpeg 执行命令集 ===>>> " + args);

			Runtime runtime = Runtime.getRuntime();

			ffmpeg = runtime.exec(cmd, null, directory);

			ffmpegKiller = new ProcessKiller(ffmpeg);
			runtime.addShutdownHook(ffmpegKiller);
			inputStream = ffmpeg.getInputStream();
			outputStream = ffmpeg.getOutputStream();
			errorStream = ffmpeg.getErrorStream();

			LOGGER.info("==========>>> ffmpeg 转码成功.");
		} catch (IOException e) {
			LOGGER.error("==========>>> ffmpeg 转码失败 Message: " + e.getMessage());
			LOGGER.error("==========>>> ffmpeg 转码失败 Cause: " + e.getCause());
			e.printStackTrace();
		} finally {
			destroy();
		}
	}

	public void execute(MediaRecord record) throws EncoderException {
		execute(record, null);
	}

	public void execute(MediaRecord record, File directory) throws EncoderException {

		LOGGER.info("进入 ffmpeg 转码  video 中...  Ffmpeg 执行命令集 ===>>> " + args);

		final ProcessBuilder pb = new ProcessBuilder().directory(directory);
		pb.redirectErrorStream(true);
		if (null != directory) {
			LOGGER.info("ffmpeg 执行转换水印文件目录==========>>>" + directory.toString());
		}

		pb.command(args);

		try {
			final Process process = pb.start();
			inputStream = process.getInputStream();

			MediaInputStreamParser.parseMediaRecord(inputStream, record);

			outputStream = process.getOutputStream();
			errorStream = process.getErrorStream();
			// 开启单独的线程来处理输入和输出流，避免缓冲区满导致线程阻塞.
			//			BufferedInputStream in = new BufferedInputStream(inputStream);
			//			BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
			//			String lineStr;
			//			while ((lineStr = inBr.readLine()) != null)
			//				LOGGER.info("process.getInputStream() ===>>> " + lineStr);

			int waitfor = process.waitFor();
			if (waitfor != 0) {
				//p.exitValue()==0表示正常结束，1：非正常结束
				if (process.exitValue() == 1) {
					LOGGER.info("===>>> ffmpeg 转码 Failed!");
					throw new EncoderException("ffmpeg 转码 Failed!");
				} else {
					LOGGER.info("==========>>> ffmpeg 转码成功.");
				}
			} else {
				LOGGER.info("==========>>> ffmpeg 转码成功.");
			}

		} catch (IOException e) {
			LOGGER.error("==========>>> ffmpeg 转码失败 Message: " + e.getMessage());
			LOGGER.error("==========>>> ffmpeg 转码失败 Cause: " + e.getCause());
			e.printStackTrace();
		} catch (InterruptedException e) {
			LOGGER.error("==========>>> ffmpeg 转码失败 Message: " + e.getMessage());
			LOGGER.error("==========>>> ffmpeg 转码失败 Cause: " + e.getCause());
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} finally {
			destroy();
		}
	}

	/**
	 * Returns a stream reading from the ffmpeg process standard output channel.
	 * 
	 * @return A stream reading from the ffmpeg process standard output channel.
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * Returns a stream writing in the ffmpeg process standard input channel.
	 * 
	 * @return A stream writing in the ffmpeg process standard input channel.
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * Returns a stream reading from the ffmpeg process standard error channel.
	 * 
	 * @return A stream reading from the ffmpeg process standard error channel.
	 */
	public InputStream getErrorStream() {
		return errorStream;
	}

	/**
	 * If there's a ffmpeg execution in progress, it kills it.
	 */
	public void destroy() {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (Throwable t) {
				;
			}
			inputStream = null;
		}
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (Throwable t) {
				;
			}
			outputStream = null;
		}
		if (errorStream != null) {
			try {
				errorStream.close();
			} catch (Throwable t) {
				;
			}
			errorStream = null;
		}
		if (ffmpeg != null) {
			ffmpeg.destroy();
			ffmpeg = null;
		}
		if (ffmpegKiller != null) {
			Runtime runtime = Runtime.getRuntime();
			runtime.removeShutdownHook(ffmpegKiller);
			ffmpegKiller = null;
		}
		if (null != args) {
			args = null;
		}
	}
}
