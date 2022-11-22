package com.viettel.asset.dto;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ResultInfo {

    public static final String RESULT_OK = "OK";
    public static final String RESULT_NOK = "NOK";

    public static final Long STATUS_OK = 200L;
    public static final Long STATUS_NOK = 400L;
    
    private String status;
    private String message;
    
    private String type;
    private Long statusReponse;
    private Object response;

    private List data ;

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getStatusReponse() {
		return statusReponse;
	}

	public void setStatusReponse(Long statusReponse) {
		this.statusReponse = statusReponse;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}
}
