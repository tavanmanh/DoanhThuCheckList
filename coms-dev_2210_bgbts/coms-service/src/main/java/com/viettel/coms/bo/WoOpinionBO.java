package com.viettel.coms.bo;

import com.viettel.coms.dto.WoOpinionDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "WO_OPINION")
public class WoOpinionBO extends BaseFWModelImpl {
    private long opinionId;
    private long opinionWoId;
    private long opinionTypeId;
    private String opinionContent;
    private String opinionState;
    private int status;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "WO_OPINION_SEQ")})
    @Column(name = "ID")
    public long getOpinionId() {
        return opinionId;
    }
    public void setOpinionId(long opinionId) { this.opinionId = opinionId; }

    @Column(name = "WO_ID")
    public long getOpinionWoId() { return opinionWoId; }
    public void setOpinionWoId(long opinionWoId) { this.opinionWoId = opinionWoId; }

    @Column(name = "OPINION_TYPE_ID")
    public long getOpinionTypeId() { return opinionTypeId; }
    public void setOpinionTypeId(long opinionTypeId) { this.opinionTypeId = opinionTypeId;}

    @Column(name = "CONTENT")
    public String getOpinionContent() { return opinionContent; }
    public void setOpinionContent(String opinionContent) { this.opinionContent = opinionContent; }

    @Column(name = "STATE")
    public String getOpinionState() { return opinionState; }
    public void setOpinionState(String opinionState) { this.opinionState = opinionState; }

    @Column(name = "STATUS")
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    @Override
    public WoOpinionDTO toDTO() {
        WoOpinionDTO woOpinionDTO = new WoOpinionDTO();
        woOpinionDTO.setOpinionId(this.opinionId);
        woOpinionDTO.setOpinionWoId(this.opinionWoId);
        woOpinionDTO.setOpinionTypeId(this.opinionTypeId);
        woOpinionDTO.setOpinionContent(this.opinionContent);
        woOpinionDTO.setStatus(this.status);
        woOpinionDTO.setOpinionState(this.opinionState);
        return woOpinionDTO;
    }
}
