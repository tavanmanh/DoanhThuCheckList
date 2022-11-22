package com.viettel.coms.dto;

public class CatStationRentDTO {
    private String branch;
    private String code;
    private Long sysGroupId;
    private Long rentStatus;
    private Long rentTotal;
    private Long rentOk;
    private Long rentNg;
    private Long rentNew;
    private Long rentProcessing;
    private Long rentTarget;
    private String rentDetail;

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    public Long getRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(Long rentStatus) {
        this.rentStatus = rentStatus;
    }

    public Long getRentTotal() {
        return rentTotal;
    }

    public void setRentTotal(Long rentTotal) {
        this.rentTotal = rentTotal;
    }

    public Long getRentOk() {
        return rentOk;
    }

    public void setRentOk(Long rentOk) {
        this.rentOk = rentOk;
    }

    public Long getRentNg() {
        return rentNg;
    }

    public void setRentNg(Long rentNg) {
        this.rentNg = rentNg;
    }

    public Long getRentNew() {
        return rentNew;
    }

    public void setRentNew(Long rentNew) {
        this.rentNew = rentNew;
    }

    public Long getRentProcessing() {
        return rentProcessing;
    }

    public void setRentProcessing(Long rentProcessing) {
        this.rentProcessing = rentProcessing;
    }

    public Long getRentTarget() {
        return rentTarget;
    }

    public void setRentTarget(Long rentTarget) {
        this.rentTarget = rentTarget;
    }

    public String getRentDetail() {
        return rentDetail;
    }

    public void setRentDetail(String rentDetail) {
        this.rentDetail = rentDetail;
    }
}
