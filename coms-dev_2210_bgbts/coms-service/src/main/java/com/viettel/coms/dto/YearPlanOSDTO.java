package com.viettel.coms.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.viettel.coms.bo.YearPlanOSBO;

/**
 * @author HoangNH38
 */
@XmlRootElement(name = "YEAR_PLAN_OSBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class YearPlanOSDTO extends ComsBaseFWDTO<YearPlanOSBO>{


    private java.lang.Long year;
    private java.lang.String code;
    private java.lang.String name;
    private java.lang.String description;
    private java.lang.String signState;
    private java.lang.String status;
    private java.util.Date createdDate;
    private java.lang.Long createdUserId;
    private java.lang.Long createdGroupId;
    private java.util.Date updatedDate;
    private java.lang.Long updatedUserId;
    private java.lang.Long updatedGroupId;
    private java.lang.Long yearPlanId;

    @Override
    public YearPlanOSBO toModel() {
    	YearPlanOSBO yearPlanOSBO = new YearPlanOSBO();
        yearPlanOSBO.setYear(this.year);
        yearPlanOSBO.setCode(this.code);
        yearPlanOSBO.setName(this.name);
        yearPlanOSBO.setDescription(this.description);
        yearPlanOSBO.setSignState(this.signState);
        yearPlanOSBO.setStatus(this.status);
        yearPlanOSBO.setCreatedDate(this.createdDate);
        yearPlanOSBO.setCreatedUserId(this.createdUserId);
        yearPlanOSBO.setCreatedGroupId(this.createdGroupId);
        yearPlanOSBO.setUpdatedDate(this.updatedDate);
        yearPlanOSBO.setUpdatedUserId(this.updatedUserId);
        yearPlanOSBO.setUpdatedGroupId(this.updatedGroupId);
        yearPlanOSBO.setYearPlanId(yearPlanId);
        return yearPlanOSBO;
    }

    public java.lang.Long getYearPlanId() {
        return yearPlanId;
    }

    public void setYearPlanId(java.lang.Long yearPlanId) {
        this.yearPlanId = yearPlanId;
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


}
