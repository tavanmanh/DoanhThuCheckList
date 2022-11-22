package com.viettel.coms.dto;

import com.viettel.coms.bo.WoTrMappingStationBO;

public class WoTrMappingStationDTO extends ComsBaseFWDTO<WoTrMappingStationBO> {
    private Long id;
    private Long trId;
    private Long sysGroupId;
    private Long catStationId;
    private Long status;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getTrId() {
        return trId;
    }

    public void setTrId(Long trId) {
        this.trId = trId;
    }

    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    public Long getCatStationId() {
        return catStationId;
    }

    public void setCatStationId(Long catStationId) {
        this.catStationId = catStationId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    private String lstStations;
    private String lstStationCodes;

    public String getLstStations() {
        return lstStations;
    }

    public void setLstStations(String lstStations) {
        this.lstStations = lstStations;
    }

    public String getLstStationCodes() {
        return lstStationCodes;
    }

    public void setLstStationCodes(String lstStationCodes) {
        this.lstStationCodes = lstStationCodes;
    }

    @Override
    public WoTrMappingStationBO toModel() {
        WoTrMappingStationBO woTrMappingStationBO = new WoTrMappingStationBO();
        woTrMappingStationBO.setId(this.id);
        woTrMappingStationBO.setCatStationId(this.catStationId);
        woTrMappingStationBO.setSysGroupId(this.sysGroupId);
        woTrMappingStationBO.setStatus(this.status);
        woTrMappingStationBO.setTrId(this.trId);
        return woTrMappingStationBO;
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
