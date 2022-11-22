package com.viettel.coms.bo;

import com.viettel.coms.dto.WoXdddChecklistDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WO_XDDD_CHECKLIST")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WoXdddChecklistBO extends BaseFWModelImpl {
    public Long id;
    public Long woId;
    public String state;
    public Long status;
    public Double value;
    public String name;
    public Long confirm;
    public Date confirmDate;
    public String confirmBy;
    public Long hshc;
    public Date createDate;
    public String userCreated;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "WO_XDDD_CHECKLIST_SEQ")})
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "WO_ID")
    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    @Column(name = "STATE")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "STATUS")
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Column(name = "VALUE")
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "CONFIRM")
    public Long getConfirm() {
        return confirm;
    }

    public void setConfirm(Long confirm) {
        this.confirm = confirm;
    }

    @Column(name = "CONFIRM_DATE")
    public Date getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }

    @Column(name = "CONFIRM_BY")
    public String getConfirmBy() {
        return confirmBy;
    }

    public void setConfirmBy(String confirmBy) {
        this.confirmBy = confirmBy;
    }

    @Column(name = "HSHC")
    public Long getHshc() {
        return hshc;
    }

    public void setHshc(Long hshc) {
        this.hshc = hshc;
    }

    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "USER_CREATED")
    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    @Override
    public WoXdddChecklistDTO toDTO() {
        WoXdddChecklistDTO dto = new WoXdddChecklistDTO();

        dto.setId(this.id);
        dto.setWoId(this.woId);
        dto.setState(this.state);
        dto.setStatus(this.status);
        dto.setValue(this.value);
        dto.setName(this.name);
        dto.setConfirm(this.confirm);
        dto.setConfirmDate(this.confirmDate);
        dto.setConfirmBy(this.confirmBy);
        dto.setHshc(this.hshc);
        dto.setCreateDate(this.createDate);
        dto.setUserCreated(this.userCreated);

        return dto;
    }
}
