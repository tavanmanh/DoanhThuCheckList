package com.viettel.coms.bo;

import com.viettel.coms.dto.WoFTConfig5SDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WO_FT_CONFIG_5S")
public class WoFTConfig5SBO extends BaseFWModelImpl {

    private Long ftConfigId;
    private String ftConfigCode;
    private String ftConfigName;
    private String userCreated;
    private String userNameCreated;
    private Date createdDate;
    private int status;
    private String cdLevel2;
    private String cdLevel2Name;
    private Long ftId;
    private String ftName;
    private String userUpdated;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "WO_SCHEDULE_CONFIG_SEQ")})
    @Column(name = "ID")
    public Long getFtConfigId() {
        return ftConfigId;
    }

    public void setFtConfigId(Long ftConfigId) {
        this.ftConfigId = ftConfigId;
    }

    @Column(name = "CODE")
    public String getFtConfigCode() {
        return ftConfigCode;
    }

    public void setFtConfigCode(String ftConfigCode) {
        this.ftConfigCode = ftConfigCode;
    }

    @Column(name = "NAME")
    public String getFtConfigName() {
        return ftConfigName;
    }

    public void setFtConfigName(String ftConfigName) {
        this.ftConfigName = ftConfigName;
    }

    @Column(name = "USER_CREATED")
    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    @Column(name = "USER_CREATED_NAME")
    public String getUserNameCreated() {
        return userNameCreated;
    }

    public void setUserNameCreated(String userNameCreated) {
        this.userNameCreated = userNameCreated;
    }

    @Column(name = "CREATED_DATE")
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "CD_LEVEL_2")
    public String getCdLevel2() {
        return cdLevel2;
    }

    public void setCdLevel2(String cdLevel2) {
        this.cdLevel2 = cdLevel2;
    }

    @Column(name = "CD_LEVEL_2_NAME")
    public String getCdLevel2Name() {
        return cdLevel2Name;
    }

    public void setCdLevel2Name(String cdLevel2Name) {
        this.cdLevel2Name = cdLevel2Name;
    }

    @Column(name = "FT_ID")
    public Long getFtId() {
        return ftId;
    }

    public void setFtId(Long ftId) {
        this.ftId = ftId;
    }

    @Column(name = "FT_NAME")
    public String getFtName() {
        return ftName;
    }

    public void setFtName(String ftName) {
        this.ftName = ftName;
    }

    @Column(name = "USER_UPDATED")
    public String getUserUpdated() {
        return userUpdated;
    }

    public void setUserUpdated(String userUpdated) {
        this.userUpdated = userUpdated;
    }

    @Override
    public WoFTConfig5SDTO toDTO() {
        WoFTConfig5SDTO woFTConfig5SDTO  = new WoFTConfig5SDTO();
        woFTConfig5SDTO.setFtConfigId(this.ftConfigId);
        woFTConfig5SDTO.setFtConfigCode(this.ftConfigCode);
        woFTConfig5SDTO.setFtConfigName(this.ftConfigName);
        woFTConfig5SDTO.setUserCreated(this.userCreated);
        woFTConfig5SDTO.setUserNameCreated(this.userNameCreated);
        woFTConfig5SDTO.setCreatedDate(this.createdDate);
        woFTConfig5SDTO.setStatus(this.status);
        woFTConfig5SDTO.setCdLevel2(this.cdLevel2);
        woFTConfig5SDTO.setCdLevel2Name(this.cdLevel2Name);
        woFTConfig5SDTO.setFtId(this.ftId);
        woFTConfig5SDTO.setFtName(this.ftName);
        woFTConfig5SDTO.setUserUpdated(this.userUpdated);
        return woFTConfig5SDTO;
    }
}
