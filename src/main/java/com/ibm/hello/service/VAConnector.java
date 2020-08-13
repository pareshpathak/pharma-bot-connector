package com.ibm.hello.service;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.ibm.hello.config.Constants;
import com.ibm.hello.web.WebSocketMessageSender;


@Component
public class VAConnector {

	@Autowired
	WebSocketMessageSender webSocketMessageSender;

	@Autowired
	VAControllerClient vaControllerClient;

	private final Logger log = LoggerFactory.getLogger(VAConnector.class);

	@Async
	public void sendMessageToWebController(String message, String sessionID, String userName) throws Exception {

		String endPointURL = Constants.END_POINT_URL;

		log.info("Bot EndpointURL sendMessageToVAController {} ", endPointURL);

		// JSONObject jsonResponse = vaControllerClient.processConverse(message,
		// endPointURL, userName);
		JSONObject jsonResponse = vaControllerClient.processConverseWithHttpCall(message, endPointURL, userName,sessionID);

		log.info("VA Controller responseDTO sendMessageToVAController {} ", jsonResponse);
		webSocketMessageSender.sendResponse(jsonResponse, sessionID);

	}

}
