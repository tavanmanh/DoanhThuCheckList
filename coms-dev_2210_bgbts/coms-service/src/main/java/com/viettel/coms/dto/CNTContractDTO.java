package com.viettel.coms.dto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.viettel.utils.CustomJsonDateDeserializer;
import com.viettel.utils.CustomJsonDateSerializer;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CNTContractDTO {
    private java.lang.String code;
    private java.lang.String name;
    private java.lang.String orderName;
    private java.lang.String outContract;
    private java.lang.Long status;
    private java.lang.Long type;
    private java.lang.Long constructionId;
    private java.lang.String partnerName;
    private java.lang.String sysGroupName;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private java.util.Date signDate;
    private java.lang.Double price;
    private java.lang.Long page;
    private java.lang.Integer pageSize;
    private java.lang.Integer total;
    private java.lang.Integer totalRecord;
    private Long cntContractId;
    private String deploymentDateReality;


    public String getDeploymentDateReality() {
        return deploymentDateReality;
    }

    public void setDeploymentDateReality(String deploymentDateReality) {
        this.deploymentDateReality = deploymentDateReality;
    }

    public Long getCntContractId() {
        return cntContractId;
    }

    public void setCntContractId(Long cntContractId) {
        this.cntContractId = cntContractId;
    }

    public java.lang.Integer getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(java.lang.Integer totalRecord) {
        this.totalRecord = totalRecord;
    }

    public java.lang.Long getPage() {
        return page;
    }

    public void setPage(java.lang.Long page) {
        this.page = page;
    }

    public java.lang.Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(java.lang.Integer pageSize) {
        this.pageSize = pageSize;
    }

    public java.lang.Integer getTotal() {
        return total;
    }

    public void setTotal(java.lang.Integer total) {
        this.total = total;
    }

    public java.lang.Long getType() {
        return type;
    }

    public void setType(java.lang.Long type) {
        this.type = type;
    }

    public java.lang.String getCode() {
        return code;
    }

    public void setCode(java.lang.String code) {
        this.code = code;
    }

    public java.lang.String getOutContract() {
        return outContract;
    }

    public void setOutContract(java.lang.String outContract) {
        this.outContract = outContract;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getOrderName() {
        return orderName;
    }

    public void setOrderName(java.lang.String orderName) {
        this.orderName = orderName;
    }

    public java.lang.Long getStatus() {
        return status;
    }

    public void setStatus(java.lang.Long status) {
        this.status = status;
    }

    public java.lang.String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(java.lang.String partnerName) {
        this.partnerName = partnerName;
    }

    public java.lang.String getSysGroupName() {
        return sysGroupName;
    }

    public void setSysGroupName(java.lang.String sysGroupName) {
        this.sysGroupName = sysGroupName;
    }

    public java.util.Date getSignDate() {
        return signDate;
    }

    public void setSignDate(java.util.Date signDate) {
        this.signDate = signDate;
    }

    public java.lang.Double getPrice() {
        return price;
    }

    public void setPrice(java.lang.Double price) {
        this.price = price;
    }

    public java.lang.Long getConstructionId() {
        return constructionId;
    }

    public void setConstructionId(java.lang.Long constructionId) {
        this.constructionId = constructionId;
    }
    
    private List<Long> lstContractType;

	public List<Long> getLstContractType() {
		return lstContractType;
	}

	public void setLstContractType(List<Long> lstContractType) {
		this.lstContractType = lstContractType;
	}


    private String handoverUseDateReality;


   public String getHandoverUseDateReality() {
        return handoverUseDateReality;
    }

    public void setHandoverUseDateReality(String handoverUseDateReality) {
        this.handoverUseDateReality = handoverUseDateReality;
    }
}
