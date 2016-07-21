/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.encoder.queue;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.listener.SessionAwareMessageListener;

import com.alibaba.fastjson.JSONObject;
import com.appcel.common.DTO.EncoderObject;
import com.appcel.common.enums.EncoderFormatEnum;
import com.appcel.common.enums.EncoderTypeEnum;
import com.appcel.core.encoder.MediaEncoder;
import com.appcel.core.encoder.manager.MultimediaManager;
import com.appcel.facade.encoder.DTO.MediaRecord;

/**
 * 
 * @描述: 多媒体文件转换消息队列监听器 .
 * @作者: LiRongSheng
 * @创建时间: 2015-3-17,下午11:21:23 .
 * @版本号: V1.0 .
 */
//@Component("encoderSessionAwareMessageListener")
public class EncoderSessionAwareMessageListener implements SessionAwareMessageListener<Message> {

	private static final Log LOG = LogFactory.getLog(EncoderSessionAwareMessageListener.class);

	@Autowired
	private JmsTemplate encoderJmsTemplate;
	@Autowired
	private Destination sessionAwareQueue;
	@Autowired
	private MediaEncoder mediaEncoder;

	@Autowired
	MultimediaManager multimediaManager;

	//	@Autowired
	//	MediaEncoderService mediaEncoderService;

	public synchronized void onMessage(Message message, Session session) {
		try {
			ActiveMQTextMessage msg = (ActiveMQTextMessage) message;
			final String ms = msg.getText();

			LOG.info("==>receive message:" + ms);

			EncoderObject encoderObject = JSONObject.parseObject(ms, EncoderObject.class);// 转换成相应的对象
			if (encoderObject == null) {
				return;
			}

			try {
				String captureImagePath = null;
				MediaRecord mediaRecord = null;
				EncoderTypeEnum encoderType = encoderObject.getEncoderType();

				if (EncoderTypeEnum.VIDEO.equals(encoderType)) {
					// 处理视频转换截图
					EncoderObject encoderImageObject = encoderObject.clone();
					encoderImageObject.setEncoderType(EncoderTypeEnum.IMAGE);
					encoderImageObject.setEncoderFormat(EncoderFormatEnum.IMAGE_FORMAT_PNG);

					mediaRecord = mediaEncoder.encode(encoderImageObject);
					captureImagePath = mediaRecord.getEntityFile();

					LOG.info("视频转换截图文件 ==== >> " + captureImagePath);

					//					// 处理视频转换
					//					encoderObject.setEncoderType(EncoderTypeEnum.VIDEO);
					//					encoderObject.setEncoderFormat(EncoderFormatEnum.VIDEO_FORMAT_MP4);
					//					mediaRecord = mediaEncoder.encode(encoderObject);
					//
					//					Multimedia multimedia = multimediaManager.createMultimedia(mediaRecord.getEntityFile(),
					//							mediaRecord.getEntityFile(), mediaRecord.getType(), mediaRecord.getTimelen(),
					//							mediaRecord.getStartTime(), mediaRecord.getAudioBitrate(), mediaRecord.getAudioBitrate(),
					//							mediaRecord.getAudioBitrate(), mediaRecord.getFps(), mediaRecord.getFps(),
					//							mediaRecord.getVedioformat(), captureImagePath, mediaRecord.getWidth(),
					//							mediaRecord.getHeight());
					//
					//					LOG.info("视频转换多媒体信息数据 ==== >> " + multimedia);
				} else {
					throw new UnsupportedOperationException("不支持的媒体转换格式" + EncoderTypeEnum.toList());
				}

				LOG.info("active mq consumer message : " + mediaRecord);

			} catch (Exception e) {
				//发送异常，重新放回队列
				encoderJmsTemplate.send(sessionAwareQueue, new MessageCreator() {
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(ms);
					}
				});
				LOG.error("==>Encoder handle Exception:", e);
			}
		} catch (Exception e) {
			LOG.error("Activemq message handle Exception ==>", e);
		}
	}
}
