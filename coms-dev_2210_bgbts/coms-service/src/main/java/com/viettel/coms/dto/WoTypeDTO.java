package com.viettel.coms.dto;

import com.viettel.coms.bo.WoTypeBO;

import java.util.List;

public class WoTypeDTO extends ComsBaseFWDTO<WoTypeBO> {
    private long woTypeId;
    private String woTypeName;
    private String woTypeCode;
    private int status;
    private Long hasApWorkSrc;
    private Long hasConstruction;
    private Long hasWorkItem;
    private List<Long> idRange;

    public long getWoTypeId() {
        return woTypeId;
    }

    public void setWoTypeId(long woTypeId) {
        this.woTypeId = woTypeId;
    }

    public String getWoTypeName() {
        return woTypeName;
    }

    public void setWoTypeName(String woTypeName) {
        this.woTypeName = woTypeName;
    }

    public String getWoTypeCode() {
        return woTypeCode;
    }

    public void setWoTypeCode(String woTypeCode) {
        this.woTypeCode = woTypeCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getHasApWorkSrc() {
        return hasApWorkSrc;
    }

    public void setHasApWorkSrc(Long hasApWorkSrc) {
        this.hasApWorkSrc = hasApWorkSrc;
    }

    public Long getHasConstruction() {
        return hasConstruction;
    }

    public void setHasConstruction(Long hasConstruction) {
        this.hasConstruction = hasConstruction;
    }

    public Long getHasWorkItem() {
        return hasWorkItem;
    }

    public void setHasWorkItem(Long hasWorkItem) {
        this.hasWorkItem = hasWorkItem;
    }

    public List<Long> getIdRange() {
        return idRange;
    }

    public void setIdRange(List<Long> idRange) {
        this.idRange = idRange;
    }

    @Override
    public WoTypeBO toModel() {
        WoTypeBO woTypeBo = new WoTypeBO();
        woTypeBo.setWoTypeId(this.woTypeId);
        woTypeBo.setWoTypeName(this.woTypeName);
        woTypeBo.setWoTypeCode(this.woTypeCode);
        woTypeBo.setStatus(this.status);
        woTypeBo.setHasApWorkSrc(this.hasApWorkSrc);
        woTypeBo.setHasConstruction(this.hasConstruction);
        woTypeBo.setHasWorkItem(this.hasWorkItem);
        return woTypeBo;
    }

    @Override
    public Long getFWModelId() { return null; }

    @Override
    public String catchName() { return null; }
}
