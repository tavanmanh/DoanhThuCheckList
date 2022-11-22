package com.viettel.coms.dto;

import com.viettel.coms.bo.WoOverdueReasonBO;

import java.util.Date;

public class WoOverdueReasonDTO extends ComsBaseFWDTO<WoOverdueReasonBO> {

    private Long overdueReasonId;
    private Long woId;

    private String reasonLevel2;
    private Date reasonDateLevel2;
    private String approveStateLevel2;
    private Long approveUserIdLevel2;
    private Date approveDateLevel2;

    private String reasonLevel3;
    private Date reasonDateLevel3;
    private String approveStateLevel3;
    private Long approveUserIdLevel3;
    private Date approveDateLevel3;

    private String reasonLevel4;
    private Date reasonDateLevel4;
    private String approveStateLevel4;
    private Long approveUserIdLevel4;
    private Date approveDateLevel4;

    private String reasonLevel5;
    private Date reasonDateLevel5;
    private String approveStateLevel5;
    private Long approveUserIdLevel5;
    private Date approveDateLevel5;

    private String reasonLevelFt;
    private Date reasonDateLevelFt;
    private String approveStateLevelFt;
    private Long approveUserIdLevelFt;
    private Date approveDateLevelFt;

    private Date createdDate;
    private Long status;

    private Long reasonUserIdLevel2;
    private Long reasonUserIdLevel3;
    private Long reasonUserIdLevel4;

    public Long getOverdueReasonId() {
        return overdueReasonId;
    }

    public void setOverdueReasonId(Long overdueReasonId) {
        this.overdueReasonId = overdueReasonId;
    }

    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    public String getReasonLevel2() {
        return reasonLevel2;
    }

    public void setReasonLevel2(String reasonLevel2) {
        this.reasonLevel2 = reasonLevel2;
    }

    public Date getReasonDateLevel2() {
        return reasonDateLevel2;
    }

    public void setReasonDateLevel2(Date reasonDateLevel2) {
        this.reasonDateLevel2 = reasonDateLevel2;
    }

    public String getApproveStateLevel2() {
        return approveStateLevel2;
    }

    public void setApproveStateLevel2(String approveStateLevel2) {
        this.approveStateLevel2 = approveStateLevel2;
    }

    public Long getApproveUserIdLevel2() {
        return approveUserIdLevel2;
    }

    public void setApproveUserIdLevel2(Long approveUserIdLevel2) {
        this.approveUserIdLevel2 = approveUserIdLevel2;
    }

    public Date getApproveDateLevel2() {
        return approveDateLevel2;
    }

    public void setApproveDateLevel2(Date approveDateLevel2) {
        this.approveDateLevel2 = approveDateLevel2;
    }

    public String getReasonLevel3() {
        return reasonLevel3;
    }

    public void setReasonLevel3(String reasonLevel3) {
        this.reasonLevel3 = reasonLevel3;
    }

    public Date getReasonDateLevel3() {
        return reasonDateLevel3;
    }

    public void setReasonDateLevel3(Date reasonDateLevel3) {
        this.reasonDateLevel3 = reasonDateLevel3;
    }

    public String getApproveStateLevel3() {
        return approveStateLevel3;
    }

    public void setApproveStateLevel3(String approveStateLevel3) {
        this.approveStateLevel3 = approveStateLevel3;
    }

    public Long getApproveUserIdLevel3() {
        return approveUserIdLevel3;
    }

    public void setApproveUserIdLevel3(Long approveUserIdLevel3) {
        this.approveUserIdLevel3 = approveUserIdLevel3;
    }

    public Date getApproveDateLevel3() {
        return approveDateLevel3;
    }

    public void setApproveDateLevel3(Date approveDateLevel3) {
        this.approveDateLevel3 = approveDateLevel3;
    }

    public String getReasonLevel4() {
        return reasonLevel4;
    }

    public void setReasonLevel4(String reasonLevel4) {
        this.reasonLevel4 = reasonLevel4;
    }

    public Date getReasonDateLevel4() {
        return reasonDateLevel4;
    }

    public void setReasonDateLevel4(Date reasonDateLevel4) {
        this.reasonDateLevel4 = reasonDateLevel4;
    }

    public String getApproveStateLevel4() {
        return approveStateLevel4;
    }

    public void setApproveStateLevel4(String approveStateLevel4) {
        this.approveStateLevel4 = approveStateLevel4;
    }

    public Long getApproveUserIdLevel4() {
        return approveUserIdLevel4;
    }

    public void setApproveUserIdLevel4(Long approveUserIdLevel4) {
        this.approveUserIdLevel4 = approveUserIdLevel4;
    }

    public Date getApproveDateLevel4() {
        return approveDateLevel4;
    }

    public void setApproveDateLevel4(Date approveDateLevel4) {
        this.approveDateLevel4 = approveDateLevel4;
    }

    public String getReasonLevel5() {
        return reasonLevel5;
    }

    public void setReasonLevel5(String reasonLevel5) {
        this.reasonLevel5 = reasonLevel5;
    }

    public Date getReasonDateLevel5() {
        return reasonDateLevel5;
    }

    public void setReasonDateLevel5(Date reasonDateLevel5) {
        this.reasonDateLevel5 = reasonDateLevel5;
    }

    public String getApproveStateLevel5() {
        return approveStateLevel5;
    }

    public void setApproveStateLevel5(String approveStateLevel5) {
        this.approveStateLevel5 = approveStateLevel5;
    }

    public Long getApproveUserIdLevel5() {
        return approveUserIdLevel5;
    }

    public void setApproveUserIdLevel5(Long approveUserIdLevel5) {
        this.approveUserIdLevel5 = approveUserIdLevel5;
    }

    public Date getApproveDateLevel5() {
        return approveDateLevel5;
    }

    public void setApproveDateLevel5(Date approveDateLevel5) {
        this.approveDateLevel5 = approveDateLevel5;
    }

    public String getReasonLevelFt() {
        return reasonLevelFt;
    }

    public void setReasonLevelFt(String reasonLevelFt) {
        this.reasonLevelFt = reasonLevelFt;
    }

    public Date getReasonDateLevelFt() {
        return reasonDateLevelFt;
    }

    public void setReasonDateLevelFt(Date reasonDateLevelFt) {
        this.reasonDateLevelFt = reasonDateLevelFt;
    }

    public String getApproveStateLevelFt() {
        return approveStateLevelFt;
    }

    public void setApproveStateLevelFt(String approveStateLevelFt) {
        this.approveStateLevelFt = approveStateLevelFt;
    }

    public Long getApproveUserIdLevelFt() {
        return approveUserIdLevelFt;
    }

    public void setApproveUserIdLevelFt(Long approveUserIdLevelFt) {
        this.approveUserIdLevelFt = approveUserIdLevelFt;
    }

    public Date getApproveDateLevelFt() {
        return approveDateLevelFt;
    }

    public void setApproveDateLevelFt(Date approveDateLevelFt) {
        this.approveDateLevelFt = approveDateLevelFt;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getReasonUserIdLevel2() {
        return reasonUserIdLevel2;
    }

    public void setReasonUserIdLevel2(Long reasonUserIdLevel2) {
        this.reasonUserIdLevel2 = reasonUserIdLevel2;
    }

    public Long getReasonUserIdLevel3() {
        return reasonUserIdLevel3;
    }

    public void setReasonUserIdLevel3(Long reasonUserIdLevel3) {
        this.reasonUserIdLevel3 = reasonUserIdLevel3;
    }

    public Long getReasonUserIdLevel4() {
        return reasonUserIdLevel4;
    }

    public void setReasonUserIdLevel4(Long reasonUserIdLevel4) {
        this.reasonUserIdLevel4 = reasonUserIdLevel4;
    }

    @Override
    public WoOverdueReasonBO toModel() {
        WoOverdueReasonBO bo = new WoOverdueReasonBO();

        bo.setOverdueReasonId(this.overdueReasonId);
        bo.setWoId(this.woId);

        bo.setReasonLevel2(this.reasonLevel2);
        bo.setReasonLevel3(this.reasonLevel3);
        bo.setReasonLevel4(this.reasonLevel4);
        bo.setReasonLevel5(this.reasonLevel5);
        bo.setReasonLevelFt(this.reasonLevelFt);

        bo.setReasonDateLevel2(this.reasonDateLevel2);
        bo.setReasonDateLevel3(this.reasonDateLevel3);
        bo.setReasonDateLevel4(this.reasonDateLevel4);
        bo.setReasonDateLevel5(this.reasonDateLevel5);
        bo.setReasonDateLevelFt(this.reasonDateLevelFt);

        bo.setApproveStateLevel2(this.approveStateLevel2);
        bo.setApproveStateLevel3(this.approveStateLevel3);
        bo.setApproveStateLevel4(this.approveStateLevel4);
        bo.setApproveStateLevel5(this.approveStateLevel5);
        bo.setApproveStateLevelFt(this.approveStateLevelFt);

        bo.setApproveUserIdLevel2(this.approveUserIdLevel2);
        bo.setApproveUserIdLevel3(this.approveUserIdLevel3);
        bo.setApproveUserIdLevel4(this.approveUserIdLevel4);
        bo.setApproveUserIdLevel5(this.approveUserIdLevel5);
        bo.setApproveUserIdLevelFt(this.approveUserIdLevelFt);

        bo.setApproveDateLevel2(this.approveDateLevel2);
        bo.setApproveDateLevel3(this.approveDateLevel3);
        bo.setApproveDateLevel4(this.approveDateLevel4);
        bo.setApproveDateLevel5(this.approveDateLevel5);
        bo.setApproveDateLevelFt(this.approveDateLevelFt);

        bo.setCreatedDate(this.createdDate);
        bo.setStatus(this.status);

        bo.setReasonUserIdLevel2(this.reasonUserIdLevel2);
        bo.setReasonUserIdLevel3(this.reasonUserIdLevel3);
        bo.setReasonUserIdLevel4(this.reasonUserIdLevel4);

        return bo;
    }

    @Override
    public Long getFWModelId() {
        return null;
    }

    @Override
    public String catchName() {
        return null;
    }

    private Long sysUserId;
    private String overdueReason;
    private String overdueReasonLevel;
    private String state;

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getOverdueReason() {
        return overdueReason;
    }

    public void setOverdueReason(String overdueReason) {
        this.overdueReason = overdueReason;
    }

    public String getOverdueReasonLevel() {
        return overdueReasonLevel;
    }

    public void setOverdueReasonLevel(String overdueReasonLevel) {
        this.overdueReasonLevel = overdueReasonLevel;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
