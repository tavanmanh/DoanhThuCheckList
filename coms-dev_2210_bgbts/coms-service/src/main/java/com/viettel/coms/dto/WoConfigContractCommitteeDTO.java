package com.viettel.coms.dto;
import com.viettel.coms.bo.WoConfigContractCommitteeBO;
import com.viettel.coms.bo.WoTrTypeBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializer;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
@JsonIgnoreProperties(ignoreUnknown = true)
public class WoConfigContractCommitteeDTO extends ComsBaseFWDTO<WoConfigContractCommitteeBO>{
    private Long id;
    private Long contractId;
    private String contractCode;
    private Long userId;
    private Long userRole;
    private String userPosition;
    private String userCreated;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date userCreatedDate;
    private String lastUpdateUser;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date lastUpdateDate;
    private Long status;
    private String userName;
    private String userCode;
    private String userCodeEx;
    private String userCreatedDateStr;
    private String userRoleText;
    private String customField;


    @Override
    public WoConfigContractCommitteeBO toModel() {
        WoConfigContractCommitteeBO woConfigContractCommitteeBO = new WoConfigContractCommitteeBO();
        woConfigContractCommitteeBO.setId(this.id);
        woConfigContractCommitteeBO.setContractCode(this.contractCode);
        woConfigContractCommitteeBO.setContractId(this.contractId);
        woConfigContractCommitteeBO.setUserId(this.userId);
        woConfigContractCommitteeBO.setUserRole(this.userRole);
        woConfigContractCommitteeBO.setUserPosition(this.userPosition);
        woConfigContractCommitteeBO.setUserCreated(this.userCreated);
        woConfigContractCommitteeBO.setUserCreatedDate(this.userCreatedDate);
        woConfigContractCommitteeBO.setLastUpdateUser(this.lastUpdateUser);
        woConfigContractCommitteeBO.setLastUpdateDate(this.lastUpdateDate);
        woConfigContractCommitteeBO.setStatus(this.status);
        woConfigContractCommitteeBO.setUserName(this.userName);
        woConfigContractCommitteeBO.setUserCode(this.userCode);
        return woConfigContractCommitteeBO;
    }

    @Override
    public Long getFWModelId() {
        return null;
    }

    @Override
    public String catchName() {
        return null;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserRole() {
        return userRole;
    }

    public void setUserRole(Long userRole) {
        this.userRole = userRole;
    }

    public String getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(String userPosition) {
        this.userPosition = userPosition;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public Date getUserCreatedDate() {
        return userCreatedDate;
    }

    public void setUserCreatedDate(Date userCreatedDate) {
        this.userCreatedDate = userCreatedDate;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserCreatedDateStr() {
        return userCreatedDateStr;
    }

    public void setUserCreatedDateStr(String userCreatedDateStr) {
        this.userCreatedDateStr = userCreatedDateStr;
    }

    public String getUserRoleText() {
        return userRoleText;
    }

    public void setUserRoleText(String userRoleText) {
        this.userRoleText = userRoleText;
    }

    @Override
    public String getCustomField() {
        return customField;
    }

    @Override
    public void setCustomField(String customField) {
        this.customField = customField;
    }

    public String getUserCodeEx() {
        return userCodeEx;
    }

    public void setUserCodeEx(String userCodeEx) {
        this.userCodeEx = userCodeEx;
    }
}
