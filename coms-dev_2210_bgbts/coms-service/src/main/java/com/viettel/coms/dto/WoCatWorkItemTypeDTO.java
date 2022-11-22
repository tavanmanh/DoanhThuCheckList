package com.viettel.coms.dto;

public class WoCatWorkItemTypeDTO {
    private long catWorkItemTypeId;
    private String name;
    private String catWorkItemTypeCode;

    public long getCatWorkItemTypeId() {
        return catWorkItemTypeId;
    }

    public void setCatWorkItemTypeId(long catWorkItemTypeId) {
        this.catWorkItemTypeId = catWorkItemTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatWorkItemTypeCode() {
        return catWorkItemTypeCode;
    }

    public void setCatWorkItemTypeCode(String catWorkItemTypeCode) {
        this.catWorkItemTypeCode = catWorkItemTypeCode;
    }
}
