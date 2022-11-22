package com.viettel.coms.dto;

import java.util.Date;

public class AIOTrContractDetailDTO {
    private Long aioPackageDetailId;
    private String name;
    private Long isProvinceBought;
    private String assignmentName;
    private String isProvinceBoughtName;

    public Long getAioPackageDetailId() {
        return aioPackageDetailId;
    }

    public void setAioPackageDetailId(Long aioPackageDetailId) {
        this.aioPackageDetailId = aioPackageDetailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIsProvinceBought() {
        return isProvinceBought;
    }

    public void setIsProvinceBought(Long isProvinceBought) {
        this.isProvinceBought = isProvinceBought;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getIsProvinceBoughtName() {
        return isProvinceBoughtName;
    }

    public void setIsProvinceBoughtName(String isProvinceBoughtName) {
        this.isProvinceBoughtName = isProvinceBoughtName;
    }
}
