package com.viettel.coms.dto;

import com.viettel.coms.bo.WoOpinionBO;

public class WoOpinionDTO extends ComsBaseFWDTO<WoOpinionBO>{
    private long opinionId;
    private long opinionWoId;
    private long opinionTypeId;
    private String opinionContent;
    private String opinionState;
    private int status;

    private String opinionTypeName;

    public long getOpinionId() { return opinionId; }
    public void setOpinionId(long opinionId) { this.opinionId = opinionId; }

    public long getOpinionWoId() { return opinionWoId; }
    public void setOpinionWoId(long opinionWoId) { this.opinionWoId = opinionWoId; }

    public long getOpinionTypeId() { return opinionTypeId; }
    public void setOpinionTypeId(long opinionTypeId) { this.opinionTypeId = opinionTypeId; }

    public String getOpinionContent() { return opinionContent; }
    public void setOpinionContent(String opinionContent) { this.opinionContent = opinionContent; }

    public String getOpinionState() { return opinionState; }
    public void setOpinionState(String opinionState) { this.opinionState = opinionState; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getOpinionTypeName() {
        return opinionTypeName;
    }

    public void setOpinionTypeName(String opinionTypeName) {
        this.opinionTypeName = opinionTypeName;
    }

    @Override
    public WoOpinionBO toModel() {
        WoOpinionBO bo = new WoOpinionBO();
        bo.setOpinionId(this.opinionId);
        bo.setOpinionWoId(this.opinionWoId);
        bo.setOpinionTypeId(this.opinionTypeId);
        bo.setOpinionContent(this.opinionContent);
        bo.setOpinionState(this.opinionState);
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
