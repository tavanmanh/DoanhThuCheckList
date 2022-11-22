package com.viettel.coms.dto;

import com.viettel.coms.bo.WoMappingHshcTcBO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WoMappingHshcTcDTO extends ComsBaseFWDTO<WoMappingHshcTcBO> {
    private Long id;
    private Long woHshcId;
    private Long woTcId;
    private Long status;
    private Long catStationHouseId;
    private String contractCode;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getWoHshcId() {
        return woHshcId;
    }

    public void setWoHshcId(Long woHshcId) {
        this.woHshcId = woHshcId;
    }

    public Long getWoTcId() {
        return woTcId;
    }

    public void setWoTcId(Long woTcId) {
        this.woTcId = woTcId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getCatStationHouseId() {
        return catStationHouseId;
    }

    public void setCatStationHouseId(Long catStationHouseId) {
        this.catStationHouseId = catStationHouseId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    @Override
    public WoMappingHshcTcBO toModel() {
        WoMappingHshcTcBO woMappingHshcTcBO = new WoMappingHshcTcBO();
        woMappingHshcTcBO.setId(this.id);
        woMappingHshcTcBO.setWoHshcId(this.woHshcId);
        woMappingHshcTcBO.setWoTcId(this.woTcId);
        woMappingHshcTcBO.setStatus(this.status);
        woMappingHshcTcBO.setCatStationHouseId(this.catStationHouseId);
        woMappingHshcTcBO.setContractCode(this.contractCode);
        return woMappingHshcTcBO;
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
