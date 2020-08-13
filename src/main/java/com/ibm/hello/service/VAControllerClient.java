package com.ibm.hello.service;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VAControllerClient {

	private final Logger logger = LoggerFactory.getLogger(VAControllerClient.class);
	private RestTemplate restTemplate;
	private String serviceUrl;
	private static final int TIMEOUT = 600000;

	public VAControllerClient(final RestTemplateBuilder builder) {
		//restTemplate = builder.setConnectTimeout(TIMEOUT).setReadTimeout(TIMEOUT).build();
	}

	public JSONObject processConverse(String message, String serviceURL, String userName,String sessionID) throws Exception {

		this.serviceUrl = serviceURL;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", message);
		jsonObject.put("userName", userName);
		jsonObject.put("sessionID", sessionID);
		HttpEntity<String> request = new HttpEntity<String>(jsonObject.toString());

		String response = (String) restTemplate.postForObject(this.serviceUrl.trim(), request, String.class);
		JSONObject responseJSON = new JSONObject(response);
		logger.debug("In connector Result - body ****************" + response);
		return responseJSON;
	}

	public JSONObject processConverseWithHttpCall(String message, String serviceURL, String userName,String sessionID)
			throws JSONException {

		String response = "";

		try {
			this.serviceUrl = serviceURL;
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("message", message);
			jsonObject.put("userName", userName);
			jsonObject.put("sessionID", sessionID);

			CloseableHttpClient httpClient = HttpClients.createDefault();

			HttpPost sendEndPoint = new HttpPost(serviceURL);
			sendEndPoint.setEntity(new StringEntity(jsonObject.toString()));
			sendEndPoint.setHeader("Accept", "application/json");
			sendEndPoint.setHeader("Content-type", "application/json");
			CloseableHttpResponse result = httpClient.execute(sendEndPoint);
			response = EntityUtils.toString(result.getEntity());
			System.out.println("response of new http call is--> " + response);

		} catch (IOException ex) {
			logger.debug("Exception caugh during HTTP Call to controller {}", ex.getMessage());
		}
		JSONObject responseJSON = new JSONObject(response);
		logger.debug("In connector Result - body ****************" + response);
		return responseJSON;
	}

}