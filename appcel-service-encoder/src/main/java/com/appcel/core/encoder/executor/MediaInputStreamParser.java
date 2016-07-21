/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.encoder.executor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appcel.core.encoder.utils.DurationUtil;
import com.appcel.facade.encoder.DTO.AudioInfoDTO;
import com.appcel.facade.encoder.DTO.MediaRecord;
import com.appcel.facade.encoder.DTO.MultimediaInfo;
import com.appcel.facade.encoder.DTO.VideoInfoDTO;
import com.appcel.facade.encoder.DTO.VideoSizeDTO;
import com.appcel.facade.encoder.exception.EncoderException;
import com.appcel.facade.encoder.exception.InputFormatException;

/**
 * 
 * Defiend class file the MediaInputStreamParser.java
 * 
 *  
 *  
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-12-4
 */
public class MediaInputStreamParser {
	private static final int AUDI_BASE = 11025;

	private static final String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
	private static final String regexVideo = ".*Video: (.*?), (.*?), (.*?)[,\\s].*";
	private static final String regexAudio = ".*Audio: .*, (\\d*) Hz.*";
	private static final String regexDuration2 = ".*Duration: (.*?), start: (.*?),.*";
	private static final String regexDuration3 = ".* (\\d*) kb\\/s,.*";

	/**
	 * 负责从返回的媒体内容中解析出媒体信息记录
	 * 
	 * Input #0, avi, from 'E:\test.avi': 
	 * Duration: 00:00:10.68(时长), 
	 * start: 0.000000(开始时间), 
	 * bitrate: 166 kb/s(码率) 
	 * Stream #0:0: 
	 * Video: msrle
	 * ([1][0][0][0] / 0x0001)(编码格式), pal8(视频格式), 165x97(分辨率), 33.33 tbr, 33.33
	 * tbn, 33.33 tbc Metadata: 
	 * title : AVI6700.tmp.avi Video #1
	 * 
	 * @param is
	 * @return
	 */
	public static void parseMediaRecord(InputStream is, MediaRecord record) {
		String mediaInfo = read(is);
		System.out.println("pattern media record info : " + mediaInfo);

		Matcher matcher = Pattern.compile(regexDuration, Pattern.CASE_INSENSITIVE).matcher(mediaInfo);
		if (matcher.matches()) {
			MatchResult result = matcher.toMatchResult();
			if (result != null) {
				record.setTimelen(DurationUtil.calculateDuration(matcher.group(1)));
				record.setDuration(result.group(1).trim());
				record.setStartTime(result.group(2).trim());
				record.setBitrate(result.group(3) + "kb/s");
			}
		} else {
			matcher = Pattern.compile(regexDuration2, Pattern.CASE_INSENSITIVE).matcher(mediaInfo);
			if (matcher.matches()) {
				MatchResult result = matcher.toMatchResult();
				if (result != null) {
					if ("N/A".equalsIgnoreCase(result.group(1).trim())) {
						record.setDuration("");
					} else {
						record.setTimelen(DurationUtil.calculateDuration(matcher.group(1)));
						record.setDuration(result.group(1).trim());
					}
					record.setStartTime(result.group(2).trim());
				}
			}

			matcher = Pattern.compile(regexDuration3, Pattern.CASE_INSENSITIVE).matcher(mediaInfo);
			if (matcher.matches()) {
				MatchResult result = matcher.toMatchResult();
				if (result != null) {
					record.setBitrate(result.group(1) + "kb/s");
				}
			}
		}

		matcher = Pattern.compile(regexVideo, Pattern.CASE_INSENSITIVE).matcher(mediaInfo);
		if (matcher.matches()) {
			MatchResult result = matcher.toMatchResult();
			if (result != null) {
				if (result.group(1).indexOf("none") > -1) {
					record.setType("yuv420p");
				} else {
					record.setType(result.group(1).trim());
				}
				if (result.group(3).indexOf("x") != -1) {
					record.setSize(result.group(3).replace("x", "*"));
				}
				record.setVedioformat(result.group(2).trim());
			}
		}

		matcher = Pattern.compile(regexAudio, Pattern.CASE_INSENSITIVE).matcher(mediaInfo);
		if (matcher.matches()) {
			MatchResult result = matcher.toMatchResult();
			if (result != null) {
				Integer audi = Integer.valueOf(result.group(1).trim());
				if (audi != null) {
					if (audi % AUDI_BASE > 0) {
						record.setAudioBitrate(AUDI_BASE * (audi / AUDI_BASE) + "");
					} else {
						record.setAudioBitrate(result.group(1).trim());
					}
				}
			}
		}

		System.out.println("Encoder media record info : " + record);
	}

	/**
	 * 负责从返回信息中读取媒体内容
	 * 
	 * @param is
	 * @return
	 */
	private static String read(InputStream is) {
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		try {
			br = new BufferedReader(new InputStreamReader(is), 500);

			String line = "";
			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				sb.append(line);
			}
			br.close();
		} catch (Exception e) {
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (Exception e) {
			}
		}
		return sb.toString();
	}

	/**
	 * Private utility. It parses the ffmpeg output, extracting informations
	 * about a source multimedia file.
	 * 
	 * @param source
	 *            The source multimedia file.
	 * @param reader
	 *            The ffmpeg output channel.
	 * @return A set of informations about the source multimedia file and its
	 *         contents.
	 * @throws InputFormatException
	 *             If the format of the source file cannot be recognized and
	 *             decoded.
	 * @throws EncoderException
	 *             If a problem occurs calling the underlying ffmpeg executable.
	 */
	public static MultimediaInfo parseMultimediaInfo(File source, EncoderBufferedReader reader) throws InputFormatException,
			EncoderException {
		Pattern p1 = Pattern.compile("^\\s*Input #0, (\\w+).+$\\s*", Pattern.CASE_INSENSITIVE);
		Pattern p2 = Pattern.compile("^\\s*Duration: (\\d\\d):(\\d\\d):(\\d\\d)\\.(\\d).*$", Pattern.CASE_INSENSITIVE);
		Pattern p3 = Pattern.compile("^\\s*Stream #\\S+: ((?:Audio)|(?:Video)|(?:Data)): (.*)\\s*$", Pattern.CASE_INSENSITIVE);
		MultimediaInfo info = null;
		try {
			int step = 0;
			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				if (step == 0) {
					String token = source.getAbsolutePath() + ": ";
					if (line.startsWith(token)) {
						String message = line.substring(token.length());
						throw new InputFormatException(message);
					}
					Matcher m = p1.matcher(line);
					if (m.matches()) {
						String format = m.group(1);
						info = new MultimediaInfo();
						info.setFormat(format);
						step++;
					}
				} else if (step == 1) {
					Matcher m = p2.matcher(line);
					if (m.matches()) {
						long hours = Integer.parseInt(m.group(1));
						long minutes = Integer.parseInt(m.group(2));
						long seconds = Integer.parseInt(m.group(3));
						long dec = Integer.parseInt(m.group(4));
						long duration = (dec * 100L) + (seconds * 1000L) + (minutes * 60L * 1000L) + (hours * 60L * 60L * 1000L);
						info.setDuration(duration);
						step++;
					} else {
						step = 3;
					}
				} else if (step == 2) {
					Matcher m = p3.matcher(line);
					if (m.matches()) {
						String type = m.group(1);
						String specs = m.group(2);
						if ("Video".equalsIgnoreCase(type)) {
							VideoInfoDTO video = new VideoInfoDTO();
							StringTokenizer st = new StringTokenizer(specs, ",");
							for (int i = 0; st.hasMoreTokens(); i++) {
								String token = st.nextToken().trim();
								if (i == 0) {
									video.setDecoder(token);
								} else {
									boolean parsed = false;
									// Video size.
									Matcher m2 = SIZE_PATTERN.matcher(token);
									if (!parsed && m2.find()) {
										int width = Integer.parseInt(m2.group(1));
										int height = Integer.parseInt(m2.group(2));
										video.setSize(new VideoSizeDTO(width, height));
										parsed = true;
									}
									// Frame rate.
									m2 = FRAME_RATE_PATTERN.matcher(token);
									if (!parsed && m2.find()) {
										try {
											float frameRate = Float.parseFloat(m2.group(1));
											video.setFrameRate(frameRate);
										} catch (NumberFormatException e) {
											;
										}
										parsed = true;
									}
									// Bit rate.
									m2 = BIT_RATE_PATTERN.matcher(token);
									if (!parsed && m2.find()) {
										int bitRate = Integer.parseInt(m2.group(1));
										video.setBitRate(bitRate);
										parsed = true;
									}
								}
							}
							info.setVideo(video);
						} else if ("Audio".equalsIgnoreCase(type)) {
							AudioInfoDTO audio = new AudioInfoDTO();
							StringTokenizer st = new StringTokenizer(specs, ",");
							for (int i = 0; st.hasMoreTokens(); i++) {
								String token = st.nextToken().trim();
								if (i == 0) {
									audio.setDecoder(token);
								} else {
									boolean parsed = false;
									// Sampling rate.
									Matcher m2 = SAMPLING_RATE_PATTERN.matcher(token);
									if (!parsed && m2.find()) {
										int samplingRate = Integer.parseInt(m2.group(1));
										audio.setSamplingRate(samplingRate);
										parsed = true;
									}
									// Channels.
									m2 = CHANNELS_PATTERN.matcher(token);
									if (!parsed && m2.find()) {
										String ms = m2.group(1);
										if ("mono".equalsIgnoreCase(ms)) {
											audio.setChannels(1);
										} else if ("stereo".equalsIgnoreCase(ms)) {
											audio.setChannels(2);
										}
										parsed = true;
									}
									// Bit rate.
									m2 = BIT_RATE_PATTERN.matcher(token);
									if (!parsed && m2.find()) {
										int bitRate = Integer.parseInt(m2.group(1));
										audio.setBitRate(bitRate);
										parsed = true;
									}
								}
							}
							info.setAudio(audio);
						}
					} else {
						step = 3;
					}
				}
				if (step == 3) {
					reader.reinsertLine(line);
					break;
				}
			}
		} catch (IOException e) {
			throw new EncoderException(e);
		}
		if (info == null) {
			throw new InputFormatException();
		}
		return info;
	}

	/**
	 * This regexp is used to parse the ffmpeg output about the size of a video
	 * stream.
	 */
	private static final Pattern SIZE_PATTERN = Pattern.compile("(\\d+)x(\\d+)", Pattern.CASE_INSENSITIVE);

	/**
	 * This regexp is used to parse the ffmpeg output about the frame rate value
	 * of a video stream.
	 */
	private static final Pattern FRAME_RATE_PATTERN = Pattern.compile("([\\d.]+)\\s+(?:fps|tb\\(r\\))", Pattern.CASE_INSENSITIVE);

	/**
	 * This regexp is used to parse the ffmpeg output about the bit rate value
	 * of a stream.
	 */
	private static final Pattern BIT_RATE_PATTERN = Pattern.compile("(\\d+)\\s+kb/s", Pattern.CASE_INSENSITIVE);

	/**
	 * This regexp is used to parse the ffmpeg output about the sampling rate of
	 * an audio stream.
	 */
	private static final Pattern SAMPLING_RATE_PATTERN = Pattern.compile("(\\d+)\\s+Hz", Pattern.CASE_INSENSITIVE);

	/**
	 * This regexp is used to parse the ffmpeg output about the channels number
	 * of an audio stream.
	 */
	private static final Pattern CHANNELS_PATTERN = Pattern.compile("(mono|stereo)", Pattern.CASE_INSENSITIVE);

}
