package com.ibm.hello.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.websocket.Session;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import com.ibm.hello.util.SessionsMap;



@Component
public class WebSocketMessageSender extends TextWebSocketHandler {

	private static final String OUTPUT_TEXT = "inputText";

	private static final Logger LOG = LoggerFactory.getLogger(WebSocketMessageSender.class);

	private static String uniqueRequestId;

	public static Map<String, Session> sessionMap;
	public static Map<String, Session> uniqueIdMap;

	public void sendResponse(JSONObject responseJSON, String sessionID) {
		LOG.debug(sessionID + "Received message to send to client" + responseJSON);

		WebSocketSession responseSession = SessionsMap.getSession(sessionID);

		try {
			String response = responseJSON.toString();
			LOG.debug("responseText for web client = {}", response);
			responseSession.sendMessage(new TextMessage(prepareResponseText(response, false)));

		} catch (Exception ex) {
			LOG.error(ex.getMessage());
			sendErrorResponse(responseSession);
		}
	}

	private void sendErrorResponse(WebSocketSession webSocketSession) {
		LOG.debug("Received message to send to client");
		JSONObject responseText = new JSONObject();
		try {
			responseText.put(OUTPUT_TEXT, "error");
			LOG.debug("responseText for web client = {}", responseText);
			LOG.debug("socket id = {}", webSocketSession.getId());
			webSocketSession.sendMessage(new TextMessage(responseText.toString()));
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}

	private String prepareResponseText(String responseDTO, boolean isWelcomeMessage) throws Exception {
		
		JSONObject outpuJObject = new JSONObject(responseDTO);
		JSONObject watsonJson = null;
		JSONObject pythonJson = null;
		if (outpuJObject.has("jsonWatson")) {
			watsonJson = outpuJObject.getJSONObject("jsonWatson");
		}

		if (outpuJObject.has("jsonPython")) {
			pythonJson = outpuJObject.getJSONObject("jsonPython");
		}

		JSONObject responseText = new JSONObject();

		if(null != watsonJson && watsonJson.get("response") != null) {
			
		if (watsonJson.getString("response").startsWith("[")) {
			JSONArray responseJSON = new JSONArray(watsonJson.getString("response"));
			if (isWelcomeMessage) {
				responseText.put(OUTPUT_TEXT, "Welcome, How can i help you ?");

				LOG.debug("prepareResponseText = {}", responseText);
				return responseText.toString();
			}

			JSONObject responseOutput = new JSONObject();
			JSONObject responseQuickReply = new JSONObject();

			LOG.debug("responseJSON--> " + responseJSON);

			for (int i = 0, size = responseJSON.length(); i < size; i++) {
				JSONObject responseObject = responseJSON.getJSONObject(i);
				LOG.debug("-responseObject-> " + responseObject);
				if ("text".equals(responseObject.get("response_type"))) {

					responseOutput = responseObject;
					responseText.put(OUTPUT_TEXT, responseOutput.getString("text"));

				}
				if ("option".equals(responseObject.get("response_type"))) {
					responseQuickReply = responseObject;
					JSONObject finalQuickReply = new JSONObject();
					finalQuickReply.put("type", "option");
					finalQuickReply.put("title", responseQuickReply.getString("title"));
					finalQuickReply.put("options", responseQuickReply.getJSONArray("options"));

					JSONArray finalArray = new JSONArray();
					finalArray.put(finalQuickReply);

					LOG.debug("finalQuickReply--> " + finalQuickReply); //
					LOG.debug("finalArray--> " + finalArray);
					responseText.put("quickreplies", finalArray);
				}
				if ("suggestion".equals(responseObject.get("response_type"))) {
					
					JSONObject finalQuickReply = new JSONObject();
					finalQuickReply.put("type", "option");
					finalQuickReply.put("title", responseObject.get("title"));
					
					List<String> labelSet=new ArrayList();
					JSONArray suggestionArray = (JSONArray) responseObject.get("suggestions");
					for (int j = 0, length = suggestionArray.length(); j < length; j++) {
						JSONObject labelObject = suggestionArray.getJSONObject(j);
						String label = labelObject.getString("label");
						labelSet.add(label);
					}
					LOG.debug("labelSet--> " + labelSet);
					
					
					JSONArray options = new JSONArray();
					JSONObject inputJson = new JSONObject();
					JSONObject valueJson = new JSONObject();
					JSONObject optionJson = new JSONObject();
					for (String tcode : labelSet) {

						

						inputJson = new JSONObject();
						inputJson.put("text", tcode);

						valueJson = new JSONObject();
						valueJson.put("input", inputJson);

						optionJson = new JSONObject();
						optionJson.put("label", tcode);
						optionJson.put("value", valueJson);
						
						options.put(optionJson);
						//options.add(optionJson);

					}
					finalQuickReply.put("options", options);
					
					JSONArray finalArray = new JSONArray();
					finalArray.put(finalQuickReply);

					LOG.debug("finalQuickReply-suggestion-> " + finalQuickReply); //
					LOG.debug("finalArray-suggestion-> " + finalArray);
					responseText.put("quickreplies", finalArray);
				}
			}
		}
	}

		if (null != pythonJson && pythonJson.get("response") != null) {
			JSONObject jsonObject = new JSONObject(pythonJson.getString("response"));
			responseText.put(OUTPUT_TEXT, jsonObject);
		}
		responseText.put("uniqueRequestId", uniqueRequestId);

		LOG.debug("prepareResponseText = {}", responseDTO);
		return responseText.toString();
	}

	public static synchronized String createUniqueRequestID() {
		if (uniqueRequestId == null) {
			uniqueRequestId = UUID.randomUUID().toString();
		}
		return uniqueRequestId;
	}

}