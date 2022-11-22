package com.viettel.coms.bo;

import com.viettel.coms.dto.WoClassQoutaDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "WO_CLASS_QOUTA")
public class WoClassQoutaBO extends BaseFWModelImpl {

    private Long id;
    private Long classId;
    private String checklistName;
    private Long qoutaValue;
    private Long status;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "WO_NAME_SEQ")})
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "CLASS_ID")
    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    @Column(name = "CHECKLIST_NAME")
    public String getChecklistName() {
        return checklistName;
    }

    public void setChecklistName(String checklistName) {
        this.checklistName = checklistName;
    }

    @Column(name = "QOUTA_VALUE")
    public Long getQoutaValue() {
        return qoutaValue;
    }

    public void setQoutaValue(Long qoutaValue) {
        this.qoutaValue = qoutaValue;
    }

    @Column(name = "STATUS")
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Override
    public WoClassQoutaDTO toDTO() {
        WoClassQoutaDTO dto = new WoClassQoutaDTO();

        dto.setId(this.id);
        dto.setClassId(this.classId);
        dto.setChecklistName(this.checklistName);
        dto.setQoutaValue(this.qoutaValue);
        dto.setStatus(this.status);

        return dto;
    }
}
