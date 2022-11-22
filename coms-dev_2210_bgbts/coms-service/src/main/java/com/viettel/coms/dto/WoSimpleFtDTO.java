package com.viettel.coms.dto;

public class WoSimpleFtDTO {
    private Long ftId;
    private String fullName;
    private Long sysGroupId;
    private String employeeCode;
    private String email;
    private String keySearch;

    public Long getFtId() {
        return ftId;
    }

    public void setFtId(Long ftId) {
        this.ftId = ftId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKeySearch() {
        return keySearch;
    }

    public void setKeySearch(String keySearch) {
        this.keySearch = keySearch;
    }
}
