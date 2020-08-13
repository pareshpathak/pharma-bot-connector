package com.ibm.hello.web;

import java.util.Map;
import java.util.UUID;

import javax.websocket.Session;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.hello.model.MessageDTO;
import com.ibm.hello.service.VAConnector;
import com.ibm.hello.util.SessionsMap;
import com.ibm.hello.util.UniqueIdMap;



@Component
public class WebSocketMessageReceiver extends TextWebSocketHandler {

	private static final int MESSAGE_SIZE_LIMIT = 1024 * 1024;

	private static final String OUTPUT_TEXT = "inputText";

	private static final Logger LOG = LoggerFactory.getLogger(WebSocketMessageReceiver.class);

	private static String uniqueRequestId;

	public static Map<String, Session> sessionMap;
	public static Map<String, Session> uniqueIdMap;
	
	@Autowired
	VAConnector vAConnectorService;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		LOG.debug("Connection established");
		LOG.debug("Session opened. original session Id = {}", session.getId());
		UniqueIdMap.addUniqueId(session.getId() + "");
		String uniqueId = UniqueIdMap.getUniqueId(session.getId());
			SessionsMap.addSession(uniqueId, session);
			session.setTextMessageSizeLimit(MESSAGE_SIZE_LIMIT);
			session.setBinaryMessageSizeLimit(MESSAGE_SIZE_LIMIT);
			session.sendMessage(new TextMessage(prepareResponseText(null, true)));
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		LOG.debug("handleTextMessage::payload {}", message.getPayload());
		ObjectMapper mapper = new ObjectMapper();
		mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
		@SuppressWarnings("unchecked")
		Map<String, String> value = mapper.readValue(message.getPayload(), Map.class);

		String inputMsg = value.get("message");
		String userName = value.get("user_name");
		String formData = value.get("formData");

		uniqueRequestId = value.get("uniqueReqId");
		// Channel ID via user communicating with Bot Ex: web1,web2
		String channelId = value.get("channelId");
		String additionalData = value.get("additionalData");

		LOG.debug("------------ dialogId: {}", additionalData);
		LOG.debug("handleTextMessage::inputMsg {}", inputMsg);
		LOG.debug("uniqueReqId::inputMsg {}", uniqueRequestId);
		// Check if Channel passed from WEB Client is blank then default it to "web1"
		String[] additionalArr = new String[] {};
	

		if (StringUtils.isBlank(channelId)) {
			channelId = "web";
		}
		if ("--KeepAlive--".equalsIgnoreCase(inputMsg)) {
			// keep alive message.. do nothing.. return
			return;
		}
		
		LOG.debug("newly created uid::uniqueRequestId {}", uniqueRequestId);

		MessageDTO messageDTO = new MessageDTO(channelId, UniqueIdMap.getUniqueId(session.getId()), userName, inputMsg);
		messageDTO.getInput().setFormData(formData);
		messageDTO.getInput().setAdditionalData(additionalData);
		
		try {
			LOG.debug("handleTextMessage::inputMsg:: {}", inputMsg);
			vAConnectorService.sendMessageToWebController(inputMsg,UniqueIdMap.getUniqueId(session.getId()), userName);
		} catch (Exception e) {
			LOG.debug("Error = {}", e.getMessage());
			sendErrorResponse(session);
		}

	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		LOG.debug("SESSION ONCLOSE METHOD INVOKED");
		String uniqueId = UniqueIdMap.getUniqueId(session.getId() + "");
		SessionsMap.removeSession(uniqueId, session, status);
		UniqueIdMap.removeUniqueId(session.getId());
	}

	private void sendErrorResponse(WebSocketSession webSocketSession) throws Exception {
		LOG.debug("Received message to send to client");
		JSONObject responseText = new JSONObject();
		responseText.put(OUTPUT_TEXT, "error");
		LOG.debug("responseText for web client = {}", responseText);
		LOG.debug("socket id = {}", webSocketSession.getId());
		webSocketSession.sendMessage(new TextMessage(responseText.toString()));
	}

	private String prepareResponseText(MessageDTO responseDTO, boolean isWelcomeMessage) throws Exception {
		JSONObject responseText = new JSONObject();
		
		if (isWelcomeMessage) {
			responseText.put(OUTPUT_TEXT,"");
			LOG.debug("prepareResponseText = {}", responseText);
			return responseText.toString();
		}


		responseText.put(OUTPUT_TEXT,
				responseDTO.getOutput().getOutputText() == null ? "" : responseDTO.getOutput().getOutputText());

		if (StringUtils.isNotBlank(responseDTO.getAdditionalMessageToCaller())) {
			responseText.put(OUTPUT_TEXT, responseDTO.getAdditionalMessageToCaller());
		}

		if (StringUtils.isNotBlank(responseDTO.getOutput().getFormJson())) {
			if (StringUtils.isNotBlank(responseDTO.getOutput().getOutputText())) {
				String inputData = responseDTO.getOutput().getOutputText() + "<BR><BR>"
						+ responseDTO.getOutput().getFormJson();
				responseText.put(OUTPUT_TEXT, inputData);
			} else {
				String inputData = responseDTO.getOutput().getFormJson();
				responseText.put(OUTPUT_TEXT, inputData);
			}

		}

		responseText.put("uniqueRequestId", uniqueRequestId);
		LOG.debug("prepareResponseText = {}", responseText);
		return responseText.toString();
	}

	public static synchronized String createUniqueRequestID() {
		if (uniqueRequestId == null) {
			uniqueRequestId = UUID.randomUUID().toString();
		}
		return uniqueRequestId;
	}

}