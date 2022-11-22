package com.viettel.coms.dto;

import com.viettel.coms.bo.WoClassQoutaBO;

public class WoClassQoutaDTO extends ComsBaseFWDTO<WoClassQoutaBO> {
    private Long id;
    private Long classId;
    private String checklistName;
    private Long qoutaValue;
    private Long status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getChecklistName() {
        return checklistName;
    }

    public void setChecklistName(String checklistName) {
        this.checklistName = checklistName;
    }

    public Long getQoutaValue() {
        return qoutaValue;
    }

    public void setQoutaValue(Long qoutaValue) {
        this.qoutaValue = qoutaValue;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Override
    public WoClassQoutaBO toModel() {
        WoClassQoutaBO bo = new WoClassQoutaBO();

        bo.setId(this.id);
        bo.setClassId(this.classId);
        bo.setChecklistName(this.checklistName);
        bo.setQoutaValue(this.qoutaValue);
        bo.setStatus(this.status);

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
}
