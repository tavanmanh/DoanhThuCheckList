package com.viettel.coms.bo;

import com.viettel.coms.dto.WoHcqtProjectDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WO_HCQT_PROJECT")
public class WoHcqtProjectBO extends BaseFWModelImpl {

    private Long hcqtProjectId;
    private String name;
    private String code;
    private String userCreated;
    private Date createdDate;
    private Long status;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "WO_HCQT_PROJECT_SEQ")})
    @Column(name = "HCQT_PROJECT_ID")
    public Long getHcqtProjectId() {
        return hcqtProjectId;
    }

    public void setHcqtProjectId(Long hcqtProjectId) {
        this.hcqtProjectId = hcqtProjectId;
    }

    @Column(name = "HCQT_PROJECT_NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "HCQT_PROJECT_CODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "USER_CREATED")
    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    @Column(name = "CREATED_DATE")
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Column(name = "STATUS")
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Override
    public WoHcqtProjectDTO toDTO() {
        WoHcqtProjectDTO dto = new WoHcqtProjectDTO();

        dto.setCode(this.code);
        dto.setCreatedDate(this.createdDate);
        dto.setHcqtProjectId(this.hcqtProjectId);
        dto.setName(this.name);
        dto.setUserCreated(this.userCreated);
        dto.setStatus(this.status);

        return dto;
    }
}
