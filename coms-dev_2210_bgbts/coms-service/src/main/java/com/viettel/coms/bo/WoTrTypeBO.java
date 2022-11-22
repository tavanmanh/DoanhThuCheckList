package com.viettel.coms.bo;

import com.viettel.coms.dto.WoTrTypeDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "WO_TR_TYPE")
public class WoTrTypeBO extends BaseFWModelImpl {
    private Long woTrTypeId;
    private String trTypeCode;
    private String trTypeName;
    private int status;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "WO_SEQ")})
    @Column(name = "ID")
    public Long getWoTrTypeId() {
        return woTrTypeId;
    }

    public void setWoTrTypeId(long woTrTypeId) {
        this.woTrTypeId = woTrTypeId;
    }

    @Column(name = "TR_TYPE_CODE")
    public String getTrTypeCode() {
        return trTypeCode;
    }

    public void setTrTypeCode(String trTypeCode) {
        this.trTypeCode = trTypeCode;
    }

    @Column(name = "TR_TYPE_NAME")
    public String getTrTypeName() {
        return trTypeName;
    }

    public void setTrTypeName(String trTypeName) {
        this.trTypeName = trTypeName;
    }

    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public WoTrTypeDTO toDTO() {
        WoTrTypeDTO trTypeDto = new WoTrTypeDTO();
        trTypeDto.setWoTrTypeId(this.woTrTypeId);
        trTypeDto.setTrTypeCode(this.trTypeCode);
        trTypeDto.setTrTypeName(this.trTypeName);
        trTypeDto.setStatus(this.status);
        return trTypeDto;
    }
}
