package com.viettel.coms.dto;

import com.viettel.coms.bo.WoOpinionTypeBO;

public class WoOpinionTypeDTO extends ComsBaseFWDTO<WoOpinionTypeBO>{
    private long opinionTypeId;
    private String opinionCode;
    private String opinionName;
    private int status;

    public long getOpinionTypeId() {
        return opinionTypeId;
    }

    public void setOpinionTypeId(long opinionTypeId) {
        this.opinionTypeId = opinionTypeId;
    }

    public String getOpinionCode() {
        return opinionCode;
    }

    public void setOpinionCode(String opinionCode) {
        this.opinionCode = opinionCode;
    }

    public String getOpinionName() {
        return opinionName;
    }

    public void setOpinionName(String opinionName) {
        this.opinionName = opinionName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public WoOpinionTypeBO toModel() {
        WoOpinionTypeBO bo = new WoOpinionTypeBO();

        bo.setOpinionTypeId(this.opinionTypeId);
        bo.setOpinionCode(this.opinionCode);
        bo.setOpinionName(this.opinionName);
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
