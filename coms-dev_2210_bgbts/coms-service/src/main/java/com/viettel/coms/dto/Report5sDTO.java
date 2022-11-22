package com.viettel.coms.dto;

public class Report5sDTO extends YearPlanDetailDTO {

    private String cdLevel2Name;
    private String ftName;
    private Long totalRecord5s;
    private Long countDone;
    private Long countDoneOver;
    private Long countNotDone;
    private Long countNotDoneOver;

    // Approved
    private Long department;
    private String departmentName;
    private String consTypeName;
    private Long totalWo;
    private Long executeInDeadline;
    private Long approvedInDeadline;
    private Double executeInDeadlineRatio;
    private Double approvedInDeadlineRatio;

    // Detail
    private String woCode;
    private String createdDateStr;
    private String receiveDateStr;
    private String updateFtReceiveWoStr;
    private String updateCdApproveWoStr;
    private String userCdApproveWo;
    private String woState;
    private String executeState;
    private String approvedState;
    private String endTime;
    private String finishDate;

    public String getCdLevel2Name() {
        return cdLevel2Name;
    }

    public void setCdLevel2Name(String cdLevel2Name) {
        this.cdLevel2Name = cdLevel2Name;
    }

    public String getFtName() {
        return ftName;
    }

    public void setFtName(String ftName) {
        this.ftName = ftName;
    }

    public Long getTotalRecord5s() {
        return totalRecord5s;
    }

    public void setTotalRecord5s(Long totalRecord5s) {
        this.totalRecord5s = totalRecord5s;
    }

    public Long getCountDone() {
        return countDone;
    }

    public void setCountDone(Long countDone) {
        this.countDone = countDone;
    }

    public Long getCountDoneOver() {
        return countDoneOver;
    }

    public void setCountDoneOver(Long countDoneOver) {
        this.countDoneOver = countDoneOver;
    }

    public Long getCountNotDone() {
        return countNotDone;
    }

    public void setCountNotDone(Long countNotDone) {
        this.countNotDone = countNotDone;
    }

    public Long getCountNotDoneOver() {
        return countNotDoneOver;
    }

    public void setCountNotDoneOver(Long countNotDoneOver) {
        this.countNotDoneOver = countNotDoneOver;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getConsTypeName() {
        return consTypeName;
    }

    public void setConsTypeName(String consTypeName) {
        this.consTypeName = consTypeName;
    }

    public Long getTotalWo() {
        return totalWo;
    }

    public void setTotalWo(Long totalWo) {
        this.totalWo = totalWo;
    }

    public Long getExecuteInDeadline() {
        return executeInDeadline;
    }

    public void setExecuteInDeadline(Long executeInDeadline) {
        this.executeInDeadline = executeInDeadline;
    }

    public Long getApprovedInDeadline() {
        return approvedInDeadline;
    }

    public void setApprovedInDeadline(Long approvedInDeadline) {
        this.approvedInDeadline = approvedInDeadline;
    }

    public Double getExecuteInDeadlineRatio() {
        return executeInDeadlineRatio;
    }

    public void setExecuteInDeadlineRatio(Double executeInDeadlineRatio) {
        this.executeInDeadlineRatio = executeInDeadlineRatio;
    }

    public Double getApprovedInDeadlineRatio() {
        return approvedInDeadlineRatio;
    }

    public void setApprovedInDeadlineRatio(Double approvedInDeadlineRatio) {
        this.approvedInDeadlineRatio = approvedInDeadlineRatio;
    }

    public String getWoCode() {
        return woCode;
    }

    public void setWoCode(String woCode) {
        this.woCode = woCode;
    }

    public String getCreatedDateStr() {
        return createdDateStr;
    }

    public void setCreatedDateStr(String createdDateStr) {
        this.createdDateStr = createdDateStr;
    }

    public String getReceiveDateStr() {
        return receiveDateStr;
    }

    public void setReceiveDateStr(String receiveDateStr) {
        this.receiveDateStr = receiveDateStr;
    }

    public String getUpdateFtReceiveWoStr() {
        return updateFtReceiveWoStr;
    }

    public void setUpdateFtReceiveWoStr(String updateFtReceiveWoStr) {
        this.updateFtReceiveWoStr = updateFtReceiveWoStr;
    }

    public String getUpdateCdApproveWoStr() {
        return updateCdApproveWoStr;
    }

    public void setUpdateCdApproveWoStr(String updateCdApproveWoStr) {
        this.updateCdApproveWoStr = updateCdApproveWoStr;
    }

    public String getUserCdApproveWo() {
        return userCdApproveWo;
    }

    public void setUserCdApproveWo(String userCdApproveWo) {
        this.userCdApproveWo = userCdApproveWo;
    }

    public String getWoState() {
        return woState;
    }

    public void setWoState(String woState) {
        this.woState = woState;
    }

    public String getExecuteState() {
        return executeState;
    }

    public void setExecuteState(String executeState) {
        this.executeState = executeState;
    }

    public String getApprovedState() {
        return approvedState;
    }

    public void setApprovedState(String approvedState) {
        this.approvedState = approvedState;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }
}
