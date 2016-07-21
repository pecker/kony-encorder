/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.encoder.executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Defiend class file the RBufferedReader.java
 * 
 * A package-private utility extending java.io.BufferedReader. If a line read
 * with {@link EncoderBufferedReader#readLine()} is not useful for the calling code,
 * it can be re-inserted in the stream. The same line will be returned again at
 * the next readLine() call.
 *  
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-12-2
 */
public class EncoderBufferedReader extends BufferedReader {

	/**
	 * Re-inserted lines buffer.
	 */
	private List<String> lines = new ArrayList<String>();

	/**
	 * It builds the reader.
	 * 
	 * @param in
	 *            The underlying reader.
	 */
	public EncoderBufferedReader(Reader in) {
		super(in);
	}

	/**
	 * It returns the next line in the stream.
	 */
	public String readLine() throws IOException {
		if (lines.size() > 0) {
			return lines.remove(0);
		} else {
			return super.readLine();
		}
	}

	/**
	 * Reinserts a line in the stream. The line will be returned at the next
	 * {@link EncoderBufferedReader#readLine()} call.
	 * 
	 * @param line
	 *            The line.
	 */
	public void reinsertLine(String line) {
		lines.add(0, line);
	}

}
