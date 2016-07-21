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
package com.appcel.core.encoder.locator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.appcel.core.encoder.config.EncoderConfiguration;
import com.appcel.core.encoder.executor.EncoderExecutor;
import com.appcel.core.encoder.executor.FfmpegEncoderExecutor;
import com.appcel.core.encoder.executor.QTFaststartExecutor;
import com.appcel.facade.encoder.enums.EncoderExecutorEnum;
import com.appcel.facade.encoder.exception.EncoderRuntimeException;

/**
 * 
 * Defiend class file the DefaultFFMPEGLocator.java
 * 
 * The default ffmpeg executable locator, which exports on disk the ffmpeg
 * executable bundled with the library distributions. It should work both for
 * windows and many linux distributions. If it doesn't, try compiling your own
 * ffmpeg executable and plug it in JAVE with a custom {@link AbstractEncoderLocator}.
 *   
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-12-2
 */
public class EncoderLocator extends AbstractEncoderLocator {
	protected static Log LOGGER = LogFactory.getLog(EncoderLocator.class);
	/**
	 * Trace the version of the bundled ffmpeg executable. It's a counter: every
	 * time the bundled ffmpeg change it is incremented by 1.
	 */
	//private static final int myEXEversion = 1;

	private static EncoderLocator encoderLocator = new EncoderLocator();

	/**
	 * It builds the default FFMPEGLocator, exporting the ffmpeg executable on a
	 * temp file.
	 */
	private EncoderLocator() {

	}

	public static EncoderLocator getMe() {
		return encoderLocator;
	}

	@Override
	public EncoderExecutor createEncoderExecutor(EncoderExecutorEnum encoderExcutorEnum) {
		EncoderExecutor encoderExcutor = null;

		String os = System.getProperty("os.name").toLowerCase();
		// Windows?
		boolean isWindows = (os.indexOf("windows") != -1) ? true : false; // Need a chmod?

		//String archexe = encoderExcutorEnum.getValue();

		// Temp dir?
		//		File temp = new File(System.getProperty("java.io.tmpdir"), archexe + myEXEversion);
		//		if (!temp.exists()) {
		//			temp.mkdirs();
		//			temp.deleteOnExit();
		//		}

		// encoder executable export on disk.
		//		String suffix = isWindows ? ".exe" : "";
		//		File exeFile = new File(temp, archexe + suffix);

		//String executableCmdPath = exeFile.getAbsolutePath();

		//		if (!exeFile.exists()) {
		//
		//			copyFile(archexe + suffix, exeFile);
		//		}

		String executableCmdPath = "";
		if (EncoderExecutorEnum.FFMPEGEXECUTOR.equals(encoderExcutorEnum)) {

			executableCmdPath = isWindows ? EncoderConfiguration.getMe().getEncoderFfmpegWinHome() : EncoderConfiguration.getMe()
					.getEncoderFfmpegLinuxHome();

			encoderExcutor = new FfmpegEncoderExecutor(executableCmdPath, !isWindows);

		} else if (EncoderExecutorEnum.MENCODEREXECUTOR.equals(encoderExcutorEnum)) {

		} else if (EncoderExecutorEnum.YAMDIEXECUTOR.equals(encoderExcutorEnum)) {

		} else if (EncoderExecutorEnum.QT_FASTSTART.equals(encoderExcutorEnum)) {

			executableCmdPath = isWindows ? EncoderConfiguration.getMe().getEncoderQtFaststartWinHome() : EncoderConfiguration.getMe()
					.getEncoderQtFaststartLinuxHome();
			encoderExcutor = new QTFaststartExecutor(executableCmdPath, !isWindows);

		} else {
			throw new EncoderRuntimeException("Illegal The encoder paramter executor error : " + encoderExcutorEnum + " is not support.");
		}

		if (!isWindows) {
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec(new String[] { "/bin/chmod", "755", executableCmdPath });
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		LOGGER.info("Encoder executor Path 编码器所在位置==========>>> " + executableCmdPath);

		return encoderExcutor;
	}

	/** * Copies a file bundled in the package to the supplied destination.
	 * 
	 * @param path
	 *            The name of the bundled file.
	 * @param dest
	 *            The destination.
	 * @throws RuntimeException
	 *             If aun unexpected error occurs.
	 */
	protected void copyFile(String path, File dest) throws RuntimeException {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = getClass().getResourceAsStream(path);
			output = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int l;
			while ((l = input.read(buffer)) != -1) {
				output.write(buffer, 0, l);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot write file " + dest.getAbsolutePath());
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (Throwable t) {
					;
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (Throwable t) {
					;
				}
			}
		}
	}
}
