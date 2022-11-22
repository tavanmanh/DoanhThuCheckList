package com.viettel.coms.dto;

public class WoSimpleSysGroupDTO {
    private Long sysGroupId;
    private String code;
    private Long parentId;
    private String groupName;
    private String groupNameLevel1;
    private String groupNameLevel2;
    private String groupNameLevel3;
    private int groupLevel;
    private boolean trCreator;
    private boolean cdLevel1;
    private boolean cdLevel2;
    private boolean cdLevel3;
    private boolean cdLevel4;
    private Long provinceId;
    private String provinceCode;
    private String groupNameLv1;
    private String groupNameLv2;
    private String groupNameLv3;
    private Long parentGroupId;

    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentGroupId) {
        this.parentId = parentGroupId;
    }

    public String getGroupNameLevel1() {
        return groupNameLevel1;
    }

    public void setGroupNameLevel1(String groupNameLevel1) {
        this.groupNameLevel1 = groupNameLevel1;
    }

    public String getGroupNameLevel2() {
        return groupNameLevel2;
    }

    public void setGroupNameLevel2(String groupNameLevel2) {
        this.groupNameLevel2 = groupNameLevel2;
    }

    public String getGroupNameLevel3() {
        return groupNameLevel3;
    }

    public void setGroupNameLevel3(String groupNameLevel3) {
        this.groupNameLevel3 = groupNameLevel3;
    }

    public int getGroupLevel() {
        return groupLevel;
    }

    public void setGroupLevel(int groupLevel) {
        this.groupLevel = groupLevel;
    }

    public boolean isTrCreator() {
        return trCreator;
    }

    public void setTrCreator(boolean trCreator) {
        this.trCreator = trCreator;
    }

    public boolean isCdLevel1() {
        return cdLevel1;
    }

    public void setCdLevel1(boolean cdLevel1) {
        this.cdLevel1 = cdLevel1;
    }

    public boolean isCdLevel2() {
        return cdLevel2;
    }

    public void setCdLevel2(boolean cdLevel2) {
        this.cdLevel2 = cdLevel2;
    }

    public boolean isCdLevel3() {
        return cdLevel3;
    }

    public void setCdLevel3(boolean cdLevel3) {
        this.cdLevel3 = cdLevel3;
    }

    public boolean isCdLevel4() {
        return cdLevel4;
    }

    public void setCdLevel4(boolean cdLevel4) {
        this.cdLevel4 = cdLevel4;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
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

    public Long getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(Long parentGroupId) {
        this.parentGroupId = parentGroupId;
    }
}
