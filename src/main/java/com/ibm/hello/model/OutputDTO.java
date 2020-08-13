
package com.ibm.hello.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OutputDTO {
	private String outputText = "";
	private String formJson;
	private String additionalData;
	private String quickReply;
	
	public String getOutputText() {
		return outputText;
	}
	
	public OutputDTO(String outputText, String formJson){
		this.outputText = outputText;
		this.formJson = formJson;
	}
	
	public void setOutputText(String outputText) {
		this.outputText = outputText;
	}
	public String getFormJson() {
		return formJson;
	}
	public void setFormJson(String formJson) {
		this.formJson = formJson;
	}

	public OutputDTO() {

	}

	/**
	 * @return the additionalData
	 */
	public String getAdditionalData() {
		return additionalData;
	}

	/**
	 * @param additionalData the additionalData to set
	 */
	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder outDtoStr = new StringBuilder();
		outDtoStr.append("OutputDTO [outputText=");
		outDtoStr.append(outputText);
		outDtoStr.append(", formJson=");
		outDtoStr.append(formJson);
		outDtoStr.append(", additionalData=");
		outDtoStr.append(additionalData);
		outDtoStr.append("]");
		return outDtoStr.toString();
	}

	public String getQuickReply() {
		return quickReply;
	}

	public void setQuickReply(String quickReply) {
		this.quickReply = quickReply;
	}
		
}
