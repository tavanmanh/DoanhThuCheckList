package com.viettel.coms.dto;

import java.util.Date;

public class AIOWoTrDetailDTO {
    private Long contractDetailId;
    private Long contractId;
    private Long status;
    private Date startDate;
    private Date endDate;
    private String packageName;


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getContractDetailId() {
        return contractDetailId;
    }

    public void setContractDetailId(Long contractDetailId) {
        this.contractDetailId = contractDetailId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
}
