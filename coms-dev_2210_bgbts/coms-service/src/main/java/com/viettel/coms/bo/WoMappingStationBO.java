package com.viettel.coms.bo;

import com.viettel.coms.dto.WoMappingStationDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "WO_MAPPING_STATION")
public class WoMappingStationBO extends BaseFWModelImpl {
    private Long id;
    private Long woId;
    private Long catStationId;
    private String reason;
    private Long status;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "WO_MAPPING_STATION_SEQ")})
    @Column(name = "ID", length = 11)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "WO_ID", length = 11)
    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    @Column(name = "CAT_STATION_ID", length = 11)
    public Long getCatStationId() {
        return catStationId;
    }

    public void setCatStationId(Long catStationId) {
        this.catStationId = catStationId;
    }

    @Column(name = "REASON")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Column(name = "STATUS", length = 1)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }


    @Override
    public WoMappingStationDTO toDTO() {
        WoMappingStationDTO woMappingStationDTO = new WoMappingStationDTO();
        woMappingStationDTO.setId(this.id);
        woMappingStationDTO.setCatStationId(this.catStationId);
        woMappingStationDTO.setWoId(this.woId);
        woMappingStationDTO.setReason(this.reason);
        woMappingStationDTO.setStatus(this.status);
        return woMappingStationDTO;
    }
}
