package com.viettel.coms.dto;

import java.util.List;

import com.viettel.service.base.model.BaseFWModelImpl;

public class AppParamDTO extends ComsBaseFWDTO<BaseFWModelImpl> {
    private String code;
    private String name;
    private String parOrder;
    private String parType;
    private String status;
    // phucvx_28/06/2018
    private Double amount;
    // chinhpxn_20180614_start
    private Long appParamId;
    // chinhpxn_20180614_end
    private String confirm;
    //	hoanm1_20180703_start
    private Long constructionTaskDailyId;
    
    //Huypq-start
    private String filePath;
    
    private List<String> lstParType;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
    //Huypq-end

    public Long getConstructionTaskDailyId() {
        return constructionTaskDailyId;
    }

    public void setConstructionTaskDailyId(Long constructionTaskDailyId) {
        this.constructionTaskDailyId = constructionTaskDailyId;
    }

    //	hoanm1_20180703_end
    // chinhpxn_20180614_start
    public Long getAppParamId() {
        return appParamId;
    }

    public void setAppParamId(Long appParamId) {
        this.appParamId = appParamId;
    }

    // chinhpxn_20180614_end
    public String getParOrder() {
        return parOrder;
    }

    public void setParOrder(String parOrder) {
        this.parOrder = parOrder;
    }

    public String getParType() {
        return parType;
    }

    public void setParType(String parType) {
        this.parType = parType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
    
    public List<String> getLstParType() {
		return lstParType;
	}

	public void setLstParType(List<String> lstParType) {
		this.lstParType = lstParType;
	}

	@Override
    public String catchName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long getFWModelId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BaseFWModelImpl toModel() {
        // TODO Auto-generated method stub
        return null;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
    
    private Long optionNumber;

	public Long getOptionNumber() {
		return optionNumber;
	}

	public void setOptionNumber(Long optionNumber) {
		this.optionNumber = optionNumber;
	}
    

}
