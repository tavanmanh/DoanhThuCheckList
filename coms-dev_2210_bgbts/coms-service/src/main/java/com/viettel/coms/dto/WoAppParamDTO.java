package com.viettel.coms.dto;

public class WoAppParamDTO {
    private String code;
    private String name;
    private String parType;
    private Long parOrder;
    private String description;

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParType() {
        return parType;
    }

    public void setParType(String parType) {
        this.parType = parType;
    }

    public Long getParOrder() {
        return parOrder;
    }

    public void setParOrder(Long parOrder) {
        this.parOrder = parOrder;
    }

}
