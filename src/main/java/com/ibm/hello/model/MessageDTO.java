
package com.ibm.hello.model;


public class MessageDTO {
	private String requestChannel;
	private String clientThreadID;
	private String userID;
	private InputDTO input;	
	private OutputDTO output;
	private String agentID;
	private String responseChannel;
	private String responseThreadID;
	private String receiverID;
	private String additionalMessageToCaller;
	private String piMaskerFlag;
	private boolean endOfConversation;
	private String communicationChannel;
	private boolean authEnabled;
	
	public boolean isAuthEnabled() {
		return authEnabled;
	}
	public void setAuthEnabled(boolean authEnabled) {
		this.authEnabled = authEnabled;
	}

	public String getCommunicationChannel() {
		return communicationChannel;
	}
	public void setCommunicationChannel(String communicationChannel) {
		this.communicationChannel = communicationChannel;
	}

	
	public String getRequestChannel() {
		return requestChannel;
	}
	public void setRequestChannel(String requestChannel) {
		this.requestChannel = requestChannel;
	}
	public String getClientThreadID() {
		return clientThreadID;
	}
	public void setClientThreadID(String clientThreadID) {
		this.clientThreadID = clientThreadID;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public InputDTO getInput() {
		return input;
	}
	public void setInput(InputDTO input) {
		this.input = input;
	}
	public OutputDTO getOutput() {
		return output;
	}
	public void setOutput(OutputDTO output) {
		this.output = output;
	}
	public String getAgentID() {
		return agentID;
	}
	public void setAgentID(String agentID) {
		this.agentID = agentID;
	}
	public String getResponseChannel() {
		return responseChannel;
	}
	public void setResponseChannel(String responseChannel) {
		this.responseChannel = responseChannel;
	}
	public String getResponseThreadID() {
		return responseThreadID;
	}
	public void setResponseThreadID(String responseThreadID) {
		this.responseThreadID = responseThreadID;
	}
	public String getReceiverID() {
		return receiverID;
	}
	public void setReceiverID(String receiverID) {
		this.receiverID = receiverID;
	}
	
	public MessageDTO(String channelId, String sessionId, String userId, String inputText){
		this.requestChannel = channelId;
		this.clientThreadID = sessionId;
		this.userID = userId;
		if( this.input == null ){
			this.input = new InputDTO("","");
		}
		this.input.setInputText(inputText);
	}
	
	public MessageDTO(){
		
	}
	
	
	/**
	 * @return the endOfConversation
	 */
	public boolean getEndOfConversation() {
		return endOfConversation;
	}
	/**
	 * @param endOfConversation the endOfConversation to set
	 */
	public void setEndOfConversation(boolean endOfConversation) {
		this.endOfConversation = endOfConversation;
	}
	
		
	/**
	 * @return the additionalMessageToCaller
	 */
	public String getAdditionalMessageToCaller() {
		return additionalMessageToCaller;
	}
	/**
	 * @param additionalMessageToCaller the additionalMessageToCaller to set
	 */
	public void setAdditionalMessageToCaller(String additionalMessageToCaller) {
		this.additionalMessageToCaller = additionalMessageToCaller;
	}
	/**
	 * @return the piMaskerFlag
	 */
	public String getPiMaskerFlag() {
		return piMaskerFlag;
	}
	/**
	 * @param piMaskerFlag the piMaskerFlag to set
	 */
	public void setPiMaskerFlag(String piMaskerFlag) {
		this.piMaskerFlag = piMaskerFlag;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder msgDtoStr = new StringBuilder();
		msgDtoStr.append("MessageDTO [requestChannel=");
		msgDtoStr.append(requestChannel);
		msgDtoStr.append(", clientThreadID=");
		msgDtoStr.append(clientThreadID);
		msgDtoStr.append(", userID=");
		msgDtoStr.append(userID);
		msgDtoStr.append(", input=");
		msgDtoStr.append(input);
		msgDtoStr.append(", output=");
		msgDtoStr.append(output);
		msgDtoStr.append(", agentID=");
		msgDtoStr.append(agentID);
		msgDtoStr.append(", responseChannel=");
		msgDtoStr.append(responseChannel);
		msgDtoStr.append(", responseThreadID=");
		msgDtoStr.append(responseThreadID);
		msgDtoStr.append(", receiverID=");
		msgDtoStr.append(receiverID);
		msgDtoStr.append(", additionalMessageToCaller=");
		msgDtoStr.append(additionalMessageToCaller);
		msgDtoStr.append(", piMaskerFlag=");
		msgDtoStr.append(piMaskerFlag);
		msgDtoStr.append(", endOfConversation=");
		msgDtoStr.append(endOfConversation);
		msgDtoStr.append("]");
		return msgDtoStr.toString();
	}
	
	
	
	
}
