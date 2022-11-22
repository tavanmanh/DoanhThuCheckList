package com.viettel.coms.bo;

import com.viettel.coms.dto.WoTrMappingStationDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "WO_TR_MAPPING_STATION")
public class WoTrMappingStationBO extends BaseFWModelImpl {
    private Long id;
    private Long trId;
    private Long sysGroupId;
    private Long catStationId;
    private Long status;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "WO_TR_MAPPING_STATION_SEQ")})
    @Column(name = "ID", length = 11)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "TR_ID", length = 11)
    public Long getTrId() {
        return trId;
    }

    public void setTrId(Long trId) {
        this.trId = trId;
    }

    @Column(name = "SYS_GROUP_ID", length = 11)
    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    @Column(name = "CAT_STATION_ID", length = 11)
    public Long getCatStationId() {
        return catStationId;
    }

    public void setCatStationId(Long catStationId) {
        this.catStationId = catStationId;
    }

    @Column(name = "STATUS", length = 1)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Override
    public WoTrMappingStationDTO toDTO() {
        WoTrMappingStationDTO woTrMappingStationDTO = new WoTrMappingStationDTO();
        woTrMappingStationDTO.setId(this.id);
        woTrMappingStationDTO.setCatStationId(this.catStationId);
        woTrMappingStationDTO.setSysGroupId(this.sysGroupId);
        woTrMappingStationDTO.setStatus(this.status);
        woTrMappingStationDTO.setTrId(this.trId);
        return woTrMappingStationDTO;
    }
}
