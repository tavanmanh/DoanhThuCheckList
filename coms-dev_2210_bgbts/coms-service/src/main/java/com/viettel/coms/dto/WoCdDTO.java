package com.viettel.coms.dto;

public class WoCdDTO {
    private Long sysGroupId;
    private String groupName;
    private String groupNameLv1;
    private String groupNameLv2;
    private String groupNameLv3;
    private Long parentId;

    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupNameLv1() {
        return groupNameLv1;
    }

    public void setGroupNameLv1(String groupNameLv1) {
        this.groupNameLv1 = groupNameLv1;
    }

    public String getGroupNameLv2() {
        return groupNameLv2;
    }

    public void setGroupNameLv2(String groupNameLv2) {
        this.groupNameLv2 = groupNameLv2;
    }

    public String getGroupNameLv3() {
        return groupNameLv3;
    }

    public void setGroupNameLv3(String groupNameLv3) {
        this.groupNameLv3 = groupNameLv3;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
