package com.viettel.coms.dto;

import com.viettel.coms.bo.WoFTConfig5SBO;
import com.viettel.coms.bo.WoScheduleConfigBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializer;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WoFTConfig5SDTO extends ComsBaseFWDTO<WoFTConfig5SBO>  {

    private Long ftConfigId;
    private String ftConfigCode;
    private String ftConfigName;
    private String userCreated;
    private String userNameCreated;
    private Date createdDate;
    private String createdDateString;
    private int status;
    private String cdLevel2;
    private String cdLevel2Name;
    private Long ftId;
    private String ftName;
    private String userUpdated;
    private String loggedInUser;

    public Long getFtConfigId() {
        return ftConfigId;
    }

    public void setFtConfigId(Long ftConfigId) {
        this.ftConfigId = ftConfigId;
    }

    public String getFtConfigCode() {
        return ftConfigCode;
    }

    public void setFtConfigCode(String ftConfigCode) {
        this.ftConfigCode = ftConfigCode;
    }

    public String getFtConfigName() {
        return ftConfigName;
    }

    public void setFtConfigName(String ftConfigName) {
        this.ftConfigName = ftConfigName;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getCreatedDateString() {
        return createdDateString;
    }

    public void setCreatedDateString(String createdDateString) {
        this.createdDateString = createdDateString;
    }

    public String getUserNameCreated() {
        return userNameCreated;
    }

    public void setUserNameCreated(String userNameCreated) {
        this.userNameCreated = userNameCreated;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCdLevel2() {
        return cdLevel2;
    }

    public void setCdLevel2(String cdLevel2) {
        this.cdLevel2 = cdLevel2;
    }

    public String getCdLevel2Name() {
        return cdLevel2Name;
    }

    public void setCdLevel2Name(String cdLevel2Name) {
        this.cdLevel2Name = cdLevel2Name;
    }

    public Long getFtId() {
        return ftId;
    }

    public void setFtId(Long ftId) {
        this.ftId = ftId;
    }

    public String getFtName() {
        return ftName;
    }

    public void setFtName(String ftName) {
        this.ftName = ftName;
    }

    public String getUserUpdated() {
        return userUpdated;
    }

    public void setUserUpdated(String userUpdated) {
        this.userUpdated = userUpdated;
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    @Override
    public WoFTConfig5SBO toModel() {
        WoFTConfig5SBO woFTConfig5SBO = new WoFTConfig5SBO();
        woFTConfig5SBO.setFtConfigId(this.ftConfigId);
        woFTConfig5SBO.setFtConfigCode(this.ftConfigCode);
        woFTConfig5SBO.setFtConfigName(this.ftConfigName);
        woFTConfig5SBO.setUserCreated(this.userCreated);
        woFTConfig5SBO.setUserNameCreated(this.userNameCreated);
        woFTConfig5SBO.setCreatedDate(this.createdDate);
        woFTConfig5SBO.setStatus(this.status);
        woFTConfig5SBO.setCdLevel2(this.cdLevel2);
        woFTConfig5SBO.setCdLevel2Name(this.cdLevel2Name);
        woFTConfig5SBO.setFtId(this.ftId);
        woFTConfig5SBO.setFtName(this.ftName);
        woFTConfig5SBO.setUserUpdated(this.userUpdated);
        return woFTConfig5SBO;
    }

    @Override
    public Long getFWModelId() {
        return null;
    }

    @Override
    public String catchName() {
        return null;
    }
}
