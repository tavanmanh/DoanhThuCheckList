package com.viettel.coms.bo;

import com.viettel.coms.dto.WoConfigContractCommitteeDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WO_CONFIG_CONTRACT_COMMITTEE")
public class WoConfigContractCommitteeBO extends BaseFWModelImpl {
    private Long id;
    private Long contractId;
    private String contractCode;
    private Long userId;
    private Long userRole;
    private String userPosition;
    private String userCreated;
    private Date userCreatedDate;
    private String lastUpdateUser;
    private Date lastUpdateDate;
    private Long status;
    private String userName;
    private String userCode;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "WO_CONFIG_CONTRACT_COMMITTEE_SEQ")})
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Column(name = "CONTRACT_ID")
    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }
    @Column(name = "CONTRACT_CODE")
    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    @Column(name = "USER_ID")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    @Column(name = "USER_ROLE")
    public Long getUserRole() {
        return userRole;
    }

    public void setUserRole(Long userRole) {
        this.userRole = userRole;
    }
    @Column(name = "USER_POSITION")
    public String getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(String userPosition) {
        this.userPosition = userPosition;
    }
    @Column(name = "CREATED_USER")
    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }
    @Column(name = "CREATED_DATE_USER")
    public Date getUserCreatedDate() {
        return userCreatedDate;
    }

    public void setUserCreatedDate(Date userCreatedDate) {
        this.userCreatedDate = userCreatedDate;
    }
    @Column(name = "LAST_UPDATED_USER")
    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }
    @Column(name = "LAST_UPDATED_DATE")
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
    @Column(name = "STATUS")
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
    @Column(name = "USER_NAME")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    @Column(name = "USER_CODE")
    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @Override
    public WoConfigContractCommitteeDTO toDTO() {
        WoConfigContractCommitteeDTO woConfigContractCommitteeDTO = new WoConfigContractCommitteeDTO();
        woConfigContractCommitteeDTO.setId(this.id);
        woConfigContractCommitteeDTO.setContractCode(this.contractCode);
        woConfigContractCommitteeDTO.setContractId(this.contractId);
        woConfigContractCommitteeDTO.setUserId(this.userId);
        woConfigContractCommitteeDTO.setUserRole(this.userRole);
        woConfigContractCommitteeDTO.setUserPosition(this.userPosition);
        woConfigContractCommitteeDTO.setUserCreated(this.userCreated);
        woConfigContractCommitteeDTO.setUserCreatedDate(this.userCreatedDate);
        woConfigContractCommitteeDTO.setLastUpdateUser(this.lastUpdateUser);
        woConfigContractCommitteeDTO.setLastUpdateDate(this.lastUpdateDate);
        woConfigContractCommitteeDTO.setStatus(this.status);
        woConfigContractCommitteeDTO.setUserName(this.userName);
        woConfigContractCommitteeDTO.setUserCode(this.userCode);
        return woConfigContractCommitteeDTO;
    }
}
