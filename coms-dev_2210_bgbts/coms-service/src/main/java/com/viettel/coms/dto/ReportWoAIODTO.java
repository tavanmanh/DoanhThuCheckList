package com.viettel.coms.dto;

import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;


public class ReportWoAIODTO extends YearPlanDetailDTO {
    private String cdLevel2;
    private String cdLevel4;
    private String trCode;
    private String contractCode;
    private String woCode;
    private String insdustryName;
    private Long moneyValue;
    private String state;
    private String userCreated;
    private String userFt;
    private String reason;
    private String acceptTime;
    private String endTime;
    private String dateFrom;
    private String dateTo;

    public String getCdLevel2() {
        return cdLevel2;
    }

    public void setCdLevel2(String cdLevel2) {
        this.cdLevel2 = cdLevel2;
    }

    public String getCdLevel4() {
        return cdLevel4;
    }

    public void setCdLevel4(String cdLevel4) {
        this.cdLevel4 = cdLevel4;
    }

    public String getTrCode() {
        return trCode;
    }

    public void setTrCode(String trCode) {
        this.trCode = trCode;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getWoCode() {
        return woCode;
    }

    public void setWoCode(String woCode) {
        this.woCode = woCode;
    }

    public String getInsdustryName() {
        return insdustryName;
    }

    public void setInsdustryName(String insdustryName) {
        this.insdustryName = insdustryName;
    }

    public Long getMoneyValue() {
        return moneyValue;
    }

    public void setMoneyValue(Long moneyValue) {
        this.moneyValue = moneyValue;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public String getUserFt() {
        return userFt;
    }

    public void setUserFt(String userFt) {
        this.userFt = userFt;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }
}
