package com.viettel.coms.bo;

import com.viettel.coms.dto.WoMappingPlanDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WO_MAPPING_PLAN")
public class WoMappingPlanBO extends BaseFWModelImpl {
    private Long id;
    private Long woPlanId;
    private Long woId;
    private Long status;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "WO_MAPPING_PLAN_SEQ")})
    @Column(name = "ID", length = 11)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "WO_PLAN_ID", length = 11)
    public Long getWoPlanId() {
        return woPlanId;
    }

    public void setWoPlanId(Long woPlanId) {
        this.woPlanId = woPlanId;
    }

    @Column(name = "WO_ID", length = 11)
    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    @Column(name = "STATUS", length = 1)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }


    @Override
    public WoMappingPlanDTO toDTO() {
        WoMappingPlanDTO WOMappingPlanDTO = new WoMappingPlanDTO();
        WOMappingPlanDTO.setId(this.id);
        WOMappingPlanDTO.setWoPlanId(this.woPlanId);
        WOMappingPlanDTO.setWoId(this.woId);
        WOMappingPlanDTO.setStatus(this.status);
        return WOMappingPlanDTO;
    }


}
