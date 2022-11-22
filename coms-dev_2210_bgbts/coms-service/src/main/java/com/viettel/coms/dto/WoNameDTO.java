package com.viettel.coms.dto;

import com.viettel.coms.bo.WoNameBO;

import java.util.List;

public class WoNameDTO extends ComsBaseFWDTO<WoNameBO> {

    private Long id;
    private String name;
    private Long woTypeId;
    private Long status;

    private String woTypeName;
    private String woTypeCode;
    private List<Long> idRange;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getWoTypeId() {
        return woTypeId;
    }

    public void setWoTypeId(Long woTypeId) {
        this.woTypeId = woTypeId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
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

    public List<Long> getIdRange() {
        return idRange;
    }

    public void setIdRange(List<Long> idRange) {
        this.idRange = idRange;
    }

    @Override
    public WoNameBO toModel() {
        WoNameBO bo = new WoNameBO();

        bo.setId(this.id);
        bo.setName(this.name);
        bo.setWoTypeId(this.woTypeId);
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
