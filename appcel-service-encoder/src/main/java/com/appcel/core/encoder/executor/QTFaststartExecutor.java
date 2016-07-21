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
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.appcel.facade.encoder.DTO.MediaRecord;
import com.appcel.facade.encoder.exception.EncoderException;

/**
 * 
 * Defiend class file the QTFaststartExecutor.java
 * 
 *  
 *  
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2015-12-2
 */
public class QTFaststartExecutor implements EncoderExecutor {
	private static Log LOGGER = LogFactory.getLog(QTFaststartExecutor.class);
	/**
	 * The path of the qtfaststartExecutablePath executable.
	 */
	private String executablePath;

	/**
	 * @return the executablePath
	 */
	public String getExecutablePath() {
		return executablePath;
	}

	/**
	 * Arguments for the executable.
	 */
	private List<String> args = null;

	/**
	 * A stream reading from the qtfaststart process standard output channel.
	 */
	protected InputStream inputStream = null;

	/**
	 * A stream writing in the qtfaststart process standard input channel.
	 */
	protected OutputStream outputStream = null;

	/**
	 * A stream reading from the qtfaststart process standard error channel.
	 */
	protected InputStream errorStream = null;

	/**
	 * The process representing the qtfaststart execution.
	 */
	private Process qtfaststartProcess = null;

	/**
	 * A process killer to kill the qtfaststart process with a shutdown hook, useful
	 * if the jvm execution is shutted down during an ongoing encoding process.
	 */
	private ProcessKiller qtfaststartKiller = null;

	private boolean linuxosed;

	public boolean isLinuxos() {
		return linuxosed;
	}

	/**
	 * It build the executor.
	 * 
	 * @param qtfaststartExecutablePath
	 *            The path of the qtfaststart executable.
	 */
	public QTFaststartExecutor(String executablePath, boolean linuxosed) {
		this.executablePath = executablePath;
		this.linuxosed = linuxosed;
		args = new LinkedList<String>();
		args.add(executablePath);
	}

	/**
	 * Adds an argument to the qtfaststart executable call.
	 * 
	 * @param arg
	 *            The argument.
	 */
	public void addArgument(String arg) {
		args.add(arg);
	}

	public void execute(MediaRecord record) throws EncoderException {
		execute(record, null);
	}

	/**
	 * Executes the qtfaststart process with the previous given arguments.
	 * 
	 * @throws IOException
	 *             If the process call fails.
	 */
	public void execute() throws EncoderException {
		File directory = null;
		execute(directory);
	}

	@Override
	public void execute(File directory) throws EncoderException {

		LOGGER.info("==========>>> 进入 qt-faststart 提取  vedio meta info 中...");
		try {
			int argsSize = args.size();
			String[] cmd = new String[argsSize];
			for (int i = 0; i < argsSize; i++) {
				cmd[i] = args.get(i);
			}

			LOGGER.info("QT-faststart 执行命令集 ===>>> " + args);

			Runtime runtime = Runtime.getRuntime();

			qtfaststartProcess = runtime.exec(cmd, null, directory);

			qtfaststartKiller = new ProcessKiller(qtfaststartProcess);
			runtime.addShutdownHook(qtfaststartKiller);
			inputStream = qtfaststartProcess.getInputStream();
			outputStream = qtfaststartProcess.getOutputStream();
			errorStream = qtfaststartProcess.getErrorStream();

			LOGGER.info("==========>>> qt-faststart 提取视频播放信息至头部  success...");
		} catch (IOException e) {
			LOGGER.info("==========>>> qt-faststart 提取视频播放信息至头部  fail...");
			e.printStackTrace();
		} finally {
			destroy();
		}
	}

	@Override
	public void execute(MediaRecord record, File directory) throws EncoderException {

		LOGGER.info("==========>>> 进入 qt-faststart 提取  vedio meta info 中...");

		final ProcessBuilder pb = new ProcessBuilder().directory(directory);
		pb.redirectErrorStream(true);
		if (null != directory) {
			LOGGER.info("qt-faststart 执行转换文件目录==========>>>" + directory.toString());
		}
		LOGGER.info("QT-faststart 执行命令集 ===>>> " + args);
		pb.command(args);

		try {
			final Process process = pb.start();
			inputStream = process.getInputStream();

			MediaInputStreamParser.parseMediaRecord(inputStream, record);

			outputStream = process.getOutputStream();

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
					LOGGER.info("==========>>> qt-faststart 提取播放信息 Failed!");
					throw new EncoderException("qt-faststart 提取播放信息 Failed!");
				} else {
					LOGGER.info("==========>>> qt-faststart 提取视频播放信息至头部  success.");
				}
			} else {
				LOGGER.info("==========>>> qt-faststart 提取视频播放信息至头部  success.");
			}

			errorStream = process.getErrorStream();
			MediaInputStreamParser.parseMediaRecord(errorStream, record);

		} catch (IOException e) {
			LOGGER.error("==========>>> qt-faststart 提取播放信息 Failed Message: " + e.getMessage());
			LOGGER.error("==========>>> qt-faststart 提取播放信息 Failed Cause: " + e.getCause());
			e.printStackTrace();
		} catch (InterruptedException e) {
			LOGGER.error("==========>>> qt-faststart 提取播放信息 Failed Message: " + e.getMessage());
			LOGGER.error("==========>>> qt-faststart 提取播放信息 Failed Cause: " + e.getCause());
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} finally {
			destroy();
		}
	}

	/**
	 * Returns a stream reading from the qtfaststart process standard output channel.
	 * 
	 * @return A stream reading from the qtfaststart process standard output channel.
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * Returns a stream writing in the qtfaststart process standard input channel.
	 * 
	 * @return A stream writing in the qtfaststart process standard input channel.
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * Returns a stream reading from the qtfaststart process standard error channel.
	 * 
	 * @return A stream reading from the qtfaststart process standard error channel.
	 */
	public InputStream getErrorStream() {
		return errorStream;
	}

	/**
	 * If there's a qtfaststart execution in progress, it kills it.
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
		if (null != args) {
			args = null;
		}
	}
}
