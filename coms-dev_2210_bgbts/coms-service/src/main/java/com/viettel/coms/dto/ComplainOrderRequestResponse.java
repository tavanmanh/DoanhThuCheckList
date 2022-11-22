package com.viettel.coms.dto;

import com.viettel.asset.dto.ResultInfo;

public class ComplainOrderRequestResponse {
	private ResultInfo resultInfo;
	private String performerName;
	private Long performerId;

	public ResultInfo getResultInfo() {
		return resultInfo;
	}

	public void setResultInfo(ResultInfo resultInfo) {
		this.resultInfo = resultInfo;
	}

	public String getPerformerName() {
		return performerName;
	}

	public void setPerformerName(String performerName) {
		this.performerName = performerName;
	}

	public Long getPerformerId() {
		return performerId;
	}

	public void setPerformerId(Long performerId) {
		this.performerId = performerId;
	}

}
