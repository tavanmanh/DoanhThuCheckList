package com.viettel.coms.dto;

import com.viettel.coms.bo.WoScheduleCheckListBO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WoScheduleCheckListDTO extends ComsBaseFWDTO<WoScheduleCheckListBO>{
    private Long scheduleCheckListId;
    private String scheduleCheckListCode;
    private String scheduleCheckListName;
    private String userCreated;
    private Date createdDate;
    private int status;
    private Long scheduleWorkItemId;

    public Long getScheduleCheckListId() {
        return scheduleCheckListId;
    }

    public void setScheduleCheckListId(Long scheduleCheckListId) {
        this.scheduleCheckListId = scheduleCheckListId;
    }

    public String getScheduleCheckListCode() {
        return scheduleCheckListCode;
    }

    public void setScheduleCheckListCode(String scheduleCheckListCode) {
        this.scheduleCheckListCode = scheduleCheckListCode;
    }

    public String getScheduleCheckListName() {
        return scheduleCheckListName;
    }

    public void setScheduleCheckListName(String scheduleCheckListName) {
        this.scheduleCheckListName = scheduleCheckListName;
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

    public Long getScheduleWorkItemId() {
        return scheduleWorkItemId;
    }

    public void setScheduleWorkItemId(Long scheduleWorkItemId) {
        this.scheduleWorkItemId = scheduleWorkItemId;
    }

    @Override
    public WoScheduleCheckListBO toModel() {
        WoScheduleCheckListBO woScheduleCheckListBO = new WoScheduleCheckListBO();
        woScheduleCheckListBO.setScheduleCheckListId(this.scheduleCheckListId);
        woScheduleCheckListBO.setScheduleCheckListCode(this.scheduleCheckListCode);
        woScheduleCheckListBO.setScheduleCheckListName(this.scheduleCheckListName);
        woScheduleCheckListBO.setUserCreated(this.userCreated);
        woScheduleCheckListBO.setCreatedDate(this.createdDate);
        woScheduleCheckListBO.setStatus(this.status);
        woScheduleCheckListBO.setScheduleWorkItemId(this.scheduleWorkItemId);
        return woScheduleCheckListBO;
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
