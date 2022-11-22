package com.viettel.coms.dto;

import com.viettel.asset.dto.BaseWsRequest;

public class SysUserRequest extends BaseWsRequest {
    private long sysUserId;
    private long departmentId;
    private String sysGroupId;
    private int flag;
    private String dataImage;
    private String nameImage;
    private String name;
    private String authorities;
    //    hoanm1_20190802_start
    private String typeMenu;

    public String getTypeMenu() {
        return typeMenu;
    }

    public void setTypeMenu(String typeMenu) {
        this.typeMenu = typeMenu;
    }

    //	hoanm1_20190802_end
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameImage() {
        return nameImage;
    }

    public void setNameImage(String nameImage) {
        this.nameImage = nameImage;
    }

    public String getDataImage() {
        return dataImage;
    }

    public void setDataImage(String dataImage) {
        this.dataImage = dataImage;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(String sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    //Huypq-20191002-start
    private Long lastShipperId;

    public Long getLastShipperId() {
        return lastShipperId;
    }

    public void setLastShipperId(Long lastShipperId) {
        this.lastShipperId = lastShipperId;
    }
    //Huy-pq

    private String fromDate;
    private String toDate;

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
