package com.viettel.coms.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.viettel.coms.bo.TotalMonthPlanOSBO;

/**
 * @author HoangNh38
 */
@XmlRootElement(name = "TOTAL_MONTH_PLAN_OSBO")
public class TotalMonthPlanOSDTO extends ComsBaseFWDTO<TotalMonthPlanOSBO>{


    private java.lang.Long totalMonthPlanId;
    private java.lang.Long month;
    private java.lang.Long year;
    private java.lang.String code;
    private java.lang.String name;
    private java.lang.String description;
    private java.lang.String signState;
    private java.lang.String status;
    private java.lang.String conclution;
    private java.lang.String targetNote;
    private java.lang.String sourceNote;
    private java.lang.String financeNote;
    private java.lang.String contractNote;
    private java.util.Date createdDate;
    private java.lang.Long createdUserId;
    private java.lang.Long createdGroupId;
    private java.util.Date updatedDate;
    private java.lang.Long updatedUserId;
    private java.lang.Long updatedGroupId;
    private java.lang.String maintainNote;
    private java.lang.String btsNote;
    private java.lang.String lineNote;
    private java.lang.String materialNote;

    @Override
    public TotalMonthPlanOSBO toModel() {
    	TotalMonthPlanOSBO totalMonthPlanOSBO = new TotalMonthPlanOSBO();
    	totalMonthPlanOSBO.setTotalMonthPlanId(this.totalMonthPlanId);
    	totalMonthPlanOSBO.setMonth(this.month);
        totalMonthPlanOSBO.setYear(this.year);
        totalMonthPlanOSBO.setCode(this.code);
        totalMonthPlanOSBO.setName(this.name);
        totalMonthPlanOSBO.setDescription(this.description);
        totalMonthPlanOSBO.setSignState(this.signState);
        totalMonthPlanOSBO.setStatus(this.status);
        totalMonthPlanOSBO.setConclution(this.conclution);
        totalMonthPlanOSBO.setTargetNote(this.targetNote);
        totalMonthPlanOSBO.setMaintainNote(this.maintainNote);
        totalMonthPlanOSBO.setBtsNote(this.btsNote);
        totalMonthPlanOSBO.setLineNote(this.lineNote);
        totalMonthPlanOSBO.setMaterialNote(this.materialNote);
        totalMonthPlanOSBO.setSourceNote(this.sourceNote);
        totalMonthPlanOSBO.setFinanceNote(this.financeNote);
        totalMonthPlanOSBO.setContractNote(this.contractNote);
        totalMonthPlanOSBO.setCreatedDate(this.createdDate);
        totalMonthPlanOSBO.setCreatedUserId(this.createdUserId);
        totalMonthPlanOSBO.setCreatedGroupId(this.createdGroupId);
        totalMonthPlanOSBO.setUpdatedDate(this.updatedDate);
        totalMonthPlanOSBO.setUpdatedUserId(this.updatedUserId);
        totalMonthPlanOSBO.setUpdatedGroupId(this.updatedGroupId);
        return totalMonthPlanOSBO;
    }

    public java.lang.String getMaintainNote() {
        return maintainNote;
    }

    public void setMaintainNote(java.lang.String maintainNote) {
        this.maintainNote = maintainNote;
    }

    public java.lang.String getBtsNote() {
        return btsNote;
    }

    public void setBtsNote(java.lang.String btsNote) {
        this.btsNote = btsNote;
    }

    public java.lang.String getLineNote() {
        return lineNote;
    }

    public void setLineNote(java.lang.String lineNote) {
        this.lineNote = lineNote;
    }

    public java.lang.String getMaterialNote() {
        return materialNote;
    }

    public void setMaterialNote(java.lang.String materialNote) {
        this.materialNote = materialNote;
    }

    @Override
    public Long getFWModelId() {
        return totalMonthPlanId;
    }

    @Override
    public String catchName() {
        return getTotalMonthPlanId().toString();
    }

    public java.lang.Long getTotalMonthPlanId() {
        return totalMonthPlanId;
    }

    public void setTotalMonthPlanId(java.lang.Long totalMonthPlanId) {
        this.totalMonthPlanId = totalMonthPlanId;
    }

    public java.lang.Long getMonth() {
        return month;
    }

    public void setMonth(java.lang.Long month) {
        this.month = month;
    }

    public java.lang.Long getYear() {
        return year;
    }

    public void setYear(java.lang.Long year) {
        this.year = year;
    }

    public java.lang.String getCode() {
        return code;
    }

    public void setCode(java.lang.String code) {
        this.code = code;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.lang.String getSignState() {
        return signState;
    }

    public void setSignState(java.lang.String signState) {
        this.signState = signState;
    }

    public java.lang.String getStatus() {
        return status;
    }

    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    public java.lang.String getConclution() {
        return conclution;
    }

    public void setConclution(java.lang.String conclution) {
        this.conclution = conclution;
    }

    public java.lang.String getTargetNote() {
        return targetNote;
    }

    public void setTargetNote(java.lang.String targetNote) {
        this.targetNote = targetNote;
    }

    public java.lang.String getSourceNote() {
        return sourceNote;
    }

    public void setSourceNote(java.lang.String sourceNote) {
        this.sourceNote = sourceNote;
    }

    public java.lang.String getFinanceNote() {
        return financeNote;
    }

    public void setFinanceNote(java.lang.String financeNote) {
        this.financeNote = financeNote;
    }

    public java.lang.String getContractNote() {
        return contractNote;
    }

    public void setContractNote(java.lang.String contractNote) {
        this.contractNote = contractNote;
    }

    public java.util.Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(java.util.Date createdDate) {
        this.createdDate = createdDate;
    }

    public java.lang.Long getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(java.lang.Long createdUserId) {
        this.createdUserId = createdUserId;
    }

    public java.lang.Long getCreatedGroupId() {
        return createdGroupId;
    }

    public void setCreatedGroupId(java.lang.Long createdGroupId) {
        this.createdGroupId = createdGroupId;
    }

    public java.util.Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(java.util.Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public java.lang.Long getUpdatedUserId() {
        return updatedUserId;
    }

    public void setUpdatedUserId(java.lang.Long updatedUserId) {
        this.updatedUserId = updatedUserId;
    }

    public java.lang.Long getUpdatedGroupId() {
        return updatedGroupId;
    }

    public void setUpdatedGroupId(java.lang.Long updatedGroupId) {
        this.updatedGroupId = updatedGroupId;
    }


}
