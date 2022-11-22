package com.viettel.coms.dto;

import com.viettel.coms.bo.WoMappingPlanBO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "WOPLANBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WoMappingPlanDTO extends ComsBaseFWDTO<WoMappingPlanBO> {
    private Long id;
    private Long woPlanId;
    private Long woId;
    private Long status;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getWoPlanId() {
        return woPlanId;
    }

    public void setWoPlanId(Long woPlanId) {
        this.woPlanId = woPlanId;
    }

    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Override
    public WoMappingPlanBO toModel() {
        WoMappingPlanBO woPlanBO = new WoMappingPlanBO();
        woPlanBO.setId(this.id);
        woPlanBO.setWoPlanId(this.woPlanId);
        woPlanBO.setWoId(this.woId);
        woPlanBO.setStatus(this.status);
        return woPlanBO;
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
