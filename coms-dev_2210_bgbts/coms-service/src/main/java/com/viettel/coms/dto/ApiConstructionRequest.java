package com.viettel.coms.dto;

import java.util.Date;

public class ApiConstructionRequest {

	private String systemCode;
	private String year;
	private Date reportDate;

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	
}
