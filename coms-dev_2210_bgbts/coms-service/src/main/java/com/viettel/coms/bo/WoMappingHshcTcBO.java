package com.viettel.coms.bo;

import com.viettel.coms.dto.WoMappingHshcTcDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "WO_MAPPING_HSHC_TC")
public class WoMappingHshcTcBO extends BaseFWModelImpl {
    private Long id;
    private Long woHshcId;
    private Long woTcId;
    private Long status;
    private Long catStationHouseId;
    private String contractCode;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "WO_MAPPING_HSHC_TC_SEQ")})
    @Column(name = "ID", length = 11)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "WO_HSHC_ID", length = 11)
    public Long getWoHshcId() {
        return woHshcId;
    }

    public void setWoHshcId(Long woHshcId) {
        this.woHshcId = woHshcId;
    }

    @Column(name = "WO_TC_ID", length = 11)
    public Long getWoTcId() {
        return woTcId;
    }

    public void setWoTcId(Long woTcId) {
        this.woTcId = woTcId;
    }

    @Column(name = "STATUS", length = 1)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Column(name = "CAT_STATION_HOUSE_ID", length = 11)
    public Long getCatStationHouseId() {
        return catStationHouseId;
    }

    public void setCatStationHouseId(Long catStationHouseId) {
        this.catStationHouseId = catStationHouseId;
    }

    @Column(name = "CONTRACT_CODE")
    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    @Override
    public WoMappingHshcTcDTO toDTO() {
        WoMappingHshcTcDTO woMappingHshcTcDTO = new WoMappingHshcTcDTO();
        woMappingHshcTcDTO.setId(this.id);
        woMappingHshcTcDTO.setWoHshcId(this.woHshcId);
        woMappingHshcTcDTO.setWoTcId(this.woTcId);
        woMappingHshcTcDTO.setStatus(this.status);
        woMappingHshcTcDTO.setCatStationHouseId(this.catStationHouseId);
        woMappingHshcTcDTO.setContractCode(this.contractCode);
        return woMappingHshcTcDTO;
    }
}
