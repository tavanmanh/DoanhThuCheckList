package com.viettel.coms.dto;

public class WoSimpleContractDTO {
    private Long contractId;
    private String contractCode;
    private String contractName;
    private Long projectId;
    private String constructionCode;
    private Long contractTypeO;

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getConstructionCode() {
        return constructionCode;
    }

    public void setConstructionCode(String constructionCode) {
        this.constructionCode = constructionCode;
    }

	public Long getContractTypeO() {
		return contractTypeO;
	}

	public void setContractTypeO(Long contractTypeO) {
		this.contractTypeO = contractTypeO;
	}
    
}
