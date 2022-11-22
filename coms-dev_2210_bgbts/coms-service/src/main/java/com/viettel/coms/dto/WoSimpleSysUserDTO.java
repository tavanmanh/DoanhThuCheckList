package com.viettel.coms.dto;

import java.util.List;

public class WoSimpleSysUserDTO {
    public Long sysUserId;
    public String loginName;
    public String fullName;
    public String employeeCode;
    public String email;
    public Long sysGroupId;
    public List<String> emailRange;

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<String> getEmailRange() {
        return emailRange;
    }

    public void setEmailRange(List<String> emailRange) {
        this.emailRange = emailRange;
    }
}
