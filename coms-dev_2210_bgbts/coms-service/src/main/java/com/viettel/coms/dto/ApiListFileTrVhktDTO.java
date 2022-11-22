package com.viettel.coms.dto;

public class ApiListFileTrVhktDTO {

	private String systemCode;
	private String fileName;
	private String fileData;
	private String systemOriginalCode;
	
	public String getSystemOriginalCode() {
		return systemOriginalCode;
	}
	public void setSystemOriginalCode(String systemOriginalCode) {
		this.systemOriginalCode = systemOriginalCode;
	}
	public String getSystemCode() {
		return systemCode;
	}
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileData() {
		return fileData;
	}
	public void setFileData(String fileData) {
		this.fileData = fileData;
	}
	
	
}
