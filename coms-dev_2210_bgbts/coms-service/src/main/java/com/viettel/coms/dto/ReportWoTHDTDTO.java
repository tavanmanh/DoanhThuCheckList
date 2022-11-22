package com.viettel.coms.dto;

import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;


public class ReportWoTHDTDTO extends YearPlanDetailDTO {

    private String moneyFlowBill;
    private Long moneyFlowValue;
    private Long moneyFlowRequired;
    private String proCode;
    private String contractCode;
    private String signerPartner;
    private String moneyFlowDate;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private java.util.Date dateFrom;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date dateTo;

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getMoneyFlowBill() {
        return moneyFlowBill;
    }

    public void setMoneyFlowBill(String moneyFlowBill) {
        this.moneyFlowBill = moneyFlowBill;
    }

    public Long getMoneyFlowValue() {
        return moneyFlowValue;
    }

    public void setMoneyFlowValue(Long moneyFlowValue) {
        this.moneyFlowValue = moneyFlowValue;
    }

    public Long getMoneyFlowRequired() {
        return moneyFlowRequired;
    }

    public void setMoneyFlowRequired(Long moneyFlowRequired) {
        this.moneyFlowRequired = moneyFlowRequired;
    }

    public String getProCode() {
        return proCode;
    }

    public void setProCode(String proCode) {
        this.proCode = proCode;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getSignerPartner() {
        return signerPartner;
    }

    public void setSignerPartner(String signerPartner) {
        this.signerPartner = signerPartner;
    }

    public String getMoneyFlowDate() {
        return moneyFlowDate;
    }

    public void setMoneyFlowDate(String moneyFlowDate) {
        this.moneyFlowDate = moneyFlowDate;
    }
}
