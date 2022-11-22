package com.viettel.coms.dto;

import java.util.Date;

public class CntHandoverUseDateRealityDTO {
    private Long contractId;
    private Date handoverUseDateReality;



    public CntHandoverUseDateRealityDTO() {
    }

    public CntHandoverUseDateRealityDTO(Long contractId, Date handoverUseDateReality) {
        this.contractId = contractId;
        this.handoverUseDateReality = handoverUseDateReality;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Date getHandoverUseDateReality() {
        return handoverUseDateReality;
    }

    public void setHandoverUseDateReality(Date handoverUseDateReality) {
        this.handoverUseDateReality = handoverUseDateReality;
    }
}
