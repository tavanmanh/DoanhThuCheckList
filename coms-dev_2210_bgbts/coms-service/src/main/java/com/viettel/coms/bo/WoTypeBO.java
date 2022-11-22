package com.viettel.coms.bo;

import com.viettel.coms.dto.WoTypeDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "WO_TYPE")
public class WoTypeBO extends BaseFWModelImpl {
    private Long woTypeId;
    private String woTypeName;
    private String woTypeCode;
    private int status;
    private Long hasApWorkSrc;
    private Long hasConstruction;
    private Long hasWorkItem;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "WO_TYPE_SEQ")})
    @Column(name = "ID")
    public Long getWoTypeId() {
        return woTypeId;
    }

    public void setWoTypeId(Long woTypeId) {
        this.woTypeId = woTypeId;
    }

    @Column(name = "WO_TYPE_NAME")
    public String getWoTypeName() {
        return woTypeName;
    }

    public void setWoTypeName(String woTypeName) {
        this.woTypeName = woTypeName;
    }

    @Column(name = "WO_TYPE_CODE")
    public String getWoTypeCode() {
        return woTypeCode;
    }

    public void setWoTypeCode(String woTypeCode) {
        this.woTypeCode = woTypeCode;
    }

    @Column(name="STATUS")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name="HAS_AP_WORK_SRC")
    public Long getHasApWorkSrc() {
        return hasApWorkSrc;
    }

    public void setHasApWorkSrc(Long hasApWorkSrc) {
        this.hasApWorkSrc = hasApWorkSrc;
    }

    @Column(name="HAS_CONSTRUCTION")
    public Long getHasConstruction() {
        return hasConstruction;
    }

    public void setHasConstruction(Long hasConstruction) {
        this.hasConstruction = hasConstruction;
    }

    @Column(name="HAS_WORK_ITEM")
    public Long getHasWorkItem() {
        return hasWorkItem;
    }

    public void setHasWorkItem(Long hasWorkItem) {
        this.hasWorkItem = hasWorkItem;
    }

    @Override
    public WoTypeDTO toDTO() {
        WoTypeDTO woTypeDTO = new WoTypeDTO();
        woTypeDTO.setWoTypeId(this.woTypeId);
        woTypeDTO.setWoTypeName(this.woTypeName);
        woTypeDTO.setWoTypeCode(this.woTypeCode);
        woTypeDTO.setStatus(this.status);
        woTypeDTO.setHasApWorkSrc(this.hasApWorkSrc);
        woTypeDTO.setHasConstruction(this.hasConstruction);
        woTypeDTO.setHasWorkItem(this.hasWorkItem);
        return woTypeDTO;
    }
}
