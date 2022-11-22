package com.viettel.coms.dto;

import com.viettel.coms.bo.WoMappingStationBO;

public class WoMappingStationDTO extends ComsBaseFWDTO<WoMappingStationBO> {
    private Long id;
    private Long catStationId;
    private Long woId;
    private String reason;
    private Long status;
    private String code;
    private String address;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getCatStationId() {
        return catStationId;
    }

    public void setCatStationId(Long catStationId) {
        this.catStationId = catStationId;
    }

    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public WoMappingStationBO toModel() {
        WoMappingStationBO woMappingStationBO = new WoMappingStationBO();
        woMappingStationBO.setId(this.id);
        woMappingStationBO.setCatStationId(this.catStationId);
        woMappingStationBO.setWoId(this.woId);
        woMappingStationBO.setReason(this.reason);
        woMappingStationBO.setStatus(this.status);
        return woMappingStationBO;
    }

    @Override
    public String catchName() {
        return getId().toString();
    }

    @Override
    public Long getFWModelId() {
        return id;
    }
}
