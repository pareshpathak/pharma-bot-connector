
package com.ibm.hello.model;

public class InputDTO {
	private String inputText = "";
	private String formData;
	private String additionalData;

	
	public InputDTO(String inputText, String formData){
		this.inputText = inputText;
		this.formData = formData;
	}
	
	public String getInputText() {
		return inputText;
	}
	public void setInputText(String inputText) {
		this.inputText = inputText;
	}
	public String getFormData() {
		return formData;
	}
	public void setFormData(String formData) {
		this.formData = formData;
	}

	public InputDTO() {

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
		StringBuilder inpDtoStr = new StringBuilder();
		inpDtoStr.append("InputDTO [inputText=");
		inpDtoStr.append(inputText);
		inpDtoStr.append(", formData=");
		inpDtoStr.append(formData);
		inpDtoStr.append(", additionalData=");
		inpDtoStr.append(additionalData);
		inpDtoStr.append("]");
		return inpDtoStr.toString();
	}
	
	
}
