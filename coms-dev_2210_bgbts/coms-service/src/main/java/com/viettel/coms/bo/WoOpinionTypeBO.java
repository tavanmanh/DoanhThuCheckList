package com.viettel.coms.bo;

import com.viettel.coms.dto.WoOpinionTypeDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "WO_OPINION_TYPE")
public class WoOpinionTypeBO extends BaseFWModelImpl {
    private long opinionTypeId;
    private String opinionCode;
    private String opinionName;
    private int status;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "WO_OPINION_TYPE_SEQ")})
    @Column(name = "ID")
    public long getOpinionTypeId() {
        return opinionTypeId;
    }

    public void setOpinionTypeId(long opinionTypeId) {
        this.opinionTypeId = opinionTypeId;
    }

    @Column(name = "OPINION_CODE")
    public String getOpinionCode() {
        return opinionCode;
    }

    public void setOpinionCode(String opinionCode) {
        this.opinionCode = opinionCode;
    }

    @Column(name = "OPINION_NAME")
    public String getOpinionName() {
        return opinionName;
    }

    public void setOpinionName(String opinionName) {
        this.opinionName = opinionName;
    }

    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public WoOpinionTypeDTO toDTO() {
        WoOpinionTypeDTO dto = new WoOpinionTypeDTO();

        dto.setOpinionTypeId(this.opinionTypeId);
        dto.setOpinionCode(this.opinionCode);
        dto.setOpinionName(this.opinionName);
        dto.setStatus(this.status);

        return dto;
    }
}
