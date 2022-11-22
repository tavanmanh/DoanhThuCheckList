package com.viettel.coms.dto;

import com.viettel.coms.bo.WoScheduleWorkItemBO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WoScheduleWorkItemDTO extends ComsBaseFWDTO<WoScheduleWorkItemBO> {
    private Long woWorkItemId;
    private String workItemCode;
    private String workItemName;
    private String userCreated;
    private Date createdDate;
    private int status;
    private String userCreatedFullName;

    public Long getWoWorkItemId() {
        return woWorkItemId;
    }

    public void setWoWorkItemId(Long woWorkItemId) {
        this.woWorkItemId = woWorkItemId;
    }

    public String getWorkItemCode() {
        return workItemCode;
    }

    public void setWorkItemCode(String workItemCode) {
        this.workItemCode = workItemCode;
    }

    public String getWorkItemName() {
        return workItemName;
    }

    public void setWorkItemName(String workItemName) {
        this.workItemName = workItemName;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserCreatedFullName() {
        return userCreatedFullName;
    }

    public void setUserCreatedFullName(String userCreatedFullName) {
        this.userCreatedFullName = userCreatedFullName;
    }

    @Override
    public WoScheduleWorkItemBO toModel() {
        WoScheduleWorkItemBO woScheduleWorkItemBO = new WoScheduleWorkItemBO();
        woScheduleWorkItemBO.setWoWorkItemId(this.woWorkItemId);
        woScheduleWorkItemBO.setWorkItemCode(this.workItemCode);
        woScheduleWorkItemBO.setWorkItemName(this.workItemName);
        woScheduleWorkItemBO.setUserCreated(this.userCreated);
        woScheduleWorkItemBO.setCreatedDate(this.createdDate);
        woScheduleWorkItemBO.setStatus(this.status);
        return woScheduleWorkItemBO;
    }

    @Override
    public Long getFWModelId() {
        return null;
    }

    @Override
    public String catchName() {
        return null;
    }
}
