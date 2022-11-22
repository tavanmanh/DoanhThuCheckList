package com.viettel.coms.bo;

import com.viettel.coms.dto.WoTrDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WO_TR")
public class WoTrBO extends BaseFWModelImpl {
    private Long trId;
    private String trCode;
    private String trName;
    private long trTypeId;
    private String contractCode;
    private String projectCode;
    private String userCreated;
    private Date createdDate;
    private Date startDate;
    private Date finishDate;
    private String state;
    private Integer qoutaTime;
    private String executeLat;
    private String executeLong;
    private String cdLevel1;
    private int status;
    private String constructionCode;
    private String stationCode;
    private Long contractId;

    private Double quantityValue;
    private Long customerType;
    private Long contractType;
    private Long projectId;
    private String cdLevel1Name;
    private String userApproveTr;
    private Date updateApproveTr;
    private String cdLevel2;
    private String cdLevel2Name;
    private String userReceiveTr;
    private Date updateReceiveTr;
    private String groupCreated;
    private String groupCreatedName;
    private Long constructionId;

    private Long tmbtTarget;
    private String tmbtTargetDetail;

    private Date dbTkdaDate;
    private Date dbTtkdtDate;
    private Date dbVtDate;
    private String autoExpire;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "WO_TR_SEQ")})
    @Column(name = "ID")
    public Long getTrId() {
        return trId;
    }

    public void setTrId(Long trId) {
        this.trId = trId;
    }

    @Column(name = "TR_CODE")
    public String getTrCode() {
        return trCode;
    }

    public void setTrCode(String trCode) {
        this.trCode = trCode;
    }

    @Column(name = "TR_NAME")
    public String getTrName() {
        return trName;
    }

    public void setTrName(String trName) {
        this.trName = trName;
    }

    @Column(name = "TR_TYPE_ID")
    public long getTrTypeId() {
        return trTypeId;
    }

    public void setTrTypeId(long trTypeId) {
        this.trTypeId = trTypeId;
    }

    @Column(name = "CONTRACT_CODE")
    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    @Column(name = "PROJECT_CODE")
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
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

    @Column(name = "START_DATE")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Column(name = "FINISH_DATE")
    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    @Column(name = "STATE")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "QOUTA_TIME")

    public Integer getQoutaTime() {
        return qoutaTime;
    }

    public void setQoutaTime(Integer qoutaTime) {
        this.qoutaTime = qoutaTime;
    }

    @Column(name = "EXECUTE_LAT")
    public String getExecuteLat() {
        return executeLat;
    }

    public void setExecuteLat(String executeLat) {
        this.executeLat = executeLat;
    }

    @Column(name = "EXECUTE_LONG")
    public String getExecuteLong() {
        return executeLong;
    }

    public void setExecuteLong(String executeLong) {
        this.executeLong = executeLong;
    }

    @Column(name = "CD_LEVEL_1")
    public String getCdLevel1() {
        return cdLevel1;
    }

    public void setCdLevel1(String cdLevel1) {
        this.cdLevel1 = cdLevel1;
    }

    @Column(name = "STATUS")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "CONSTRUCTION_CODE")
    public String getConstructionCode() {
        return constructionCode;
    }

    public void setConstructionCode(String constructionCode) {
        this.constructionCode = constructionCode;
    }

    @Column(name = "STATION_CODE")
    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    @Column(name = "CONTRACT_ID")
    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    @Column(name = "QUANTITY_VALUE")
    public Double getQuantityValue() {
        return quantityValue;
    }

    public void setQuantityValue(Double quantityValue) {
        this.quantityValue = quantityValue;
    }

    @Column(name = "CUSTOMER_TYPE")
    public Long getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Long customerType) {
        this.customerType = customerType;
    }

    @Column(name = "CONTRACT_TYPE")
    public Long getContractType() {
        return contractType;
    }

    public void setContractType(Long contractType) {
        this.contractType = contractType;
    }

    @Column(name = "PROJECT_ID")
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Column(name = "CD_LEVEL_1_NAME")
    public String getCdLevel1Name() {
        return cdLevel1Name;
    }

    public void setCdLevel1Name(String cdLevel1Name) {
        this.cdLevel1Name = cdLevel1Name;
    }

    @Column(name = "USER_APPROVE_TR")
    public String getUserApproveTr() {
        return userApproveTr;
    }

    public void setUserApproveTr(String userApproveTr) {
        this.userApproveTr = userApproveTr;
    }

    @Column(name = "UPDATE_APPROVE_TR")
    public Date getUpdateApproveTr() {
        return updateApproveTr;
    }

    public void setUpdateApproveTr(Date updateApproveTr) {
        this.updateApproveTr = updateApproveTr;
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

    @Column(name = "USER_RECEIVE_TR")
    public String getUserReceiveTr() {
        return userReceiveTr;
    }

    public void setUserReceiveTr(String userReceiveTr) {
        this.userReceiveTr = userReceiveTr;
    }

    @Column(name = "UPDATE_RECEIVE_TR")
    public Date getUpdateReceiveTr() {
        return updateReceiveTr;
    }

    public void setUpdateReceiveTr(Date updateReceiveTr) {
        this.updateReceiveTr = updateReceiveTr;
    }

    @Column(name = "GROUP_CREATED")
    public String getGroupCreated() {
        return groupCreated;
    }

    public void setGroupCreated(String groupCreated) {
        this.groupCreated = groupCreated;
    }

    @Column(name = "GROUP_CREATED_NAME")
    public String getGroupCreatedName() {
        return groupCreatedName;
    }

    public void setGroupCreatedName(String groupCreatedName) {
        this.groupCreatedName = groupCreatedName;
    }

    @Column(name = "CONSTRUCTION_ID")
    public Long getConstructionId() {
        return constructionId;
    }

    public void setConstructionId(Long constructionId) {
        this.constructionId = constructionId;
    }

    @Column(name = "TMBT_TARGET")
    public Long getTmbtTarget() {
        return tmbtTarget;
    }

    public void setTmbtTarget(Long tmbtTarget) {
        this.tmbtTarget = tmbtTarget;
    }

    @Column(name = "TMBT_TARGET_DETAIL")
    public String getTmbtTargetDetail() {
        return tmbtTargetDetail;
    }

    public void setTmbtTargetDetail(String tmbtTargetDetail) {
        this.tmbtTargetDetail = tmbtTargetDetail;
    }

    @Column(name = "DB_TKDA")
    public Date getDbTkdaDate() {
        return dbTkdaDate;
    }

    public void setDbTkdaDate(Date dbTkdaDate) {
        this.dbTkdaDate = dbTkdaDate;
    }

    @Column(name = "DB_TTKDT")
    public Date getDbTtkdtDate() {
        return dbTtkdtDate;
    }

    public void setDbTtkdtDate(Date dbTtkdtDate) {
        this.dbTtkdtDate = dbTtkdtDate;
    }

    @Column(name = "DB_VT")
    public Date getDbVtDate() {
        return dbVtDate;
    }

    public void setDbVtDate(Date dbVtDate) {
        this.dbVtDate = dbVtDate;
    }
    
    @Column(name = "AUTO_EXPIRE")
    public String getAutoExpire() {
		return autoExpire;
	}

	public void setAutoExpire(String autoExpire) {
		this.autoExpire = autoExpire;
	}

    @Override
    public WoTrDTO toDTO() {
        WoTrDTO trDto = new WoTrDTO();

        trDto.setTrId(this.trId);
        trDto.setTrCode(this.trCode);
        trDto.setTrName(this.trName);
        trDto.setTrTypeId(this.trTypeId);
        trDto.setContractCode(this.contractCode);
        trDto.setProjectCode(this.projectCode);
        trDto.setUserCreated(this.userCreated);
        trDto.setCreatedDate(this.createdDate);
        trDto.setFinishDate(this.createdDate);
        trDto.setState(this.state);
        trDto.setQoutaTime(this.qoutaTime);
        trDto.setExecuteLat(this.executeLat);
        trDto.setExecuteLong(this.executeLong);
        trDto.setCdLevel1(this.cdLevel1);
        trDto.setStatus(this.status);
        trDto.setConstructionCode(this.constructionCode);
        trDto.setStationCode(this.stationCode);
        trDto.setStartDate(this.startDate);
        trDto.setContractId(this.contractId);
        trDto.setQuantityValue(this.quantityValue);
        trDto.setCustomerType(this.customerType);
        trDto.setContractType(this.contractType);
        trDto.setProjectId(this.projectId);
        trDto.setCdLevel1Name(this.cdLevel1Name);
        trDto.setUserApproveTr(this.userApproveTr);
        trDto.setUpdateApproveTr(this.updateApproveTr);
        trDto.setCdLevel2(this.cdLevel2);
        trDto.setCdLevel2Name(this.cdLevel2Name);
        trDto.setUserReceiveTr(this.userReceiveTr);
        trDto.setUpdateReceiveTr(this.updateReceiveTr);
        trDto.setGroupCreated(this.groupCreated);
        trDto.setGroupCreatedName(this.groupCreatedName);
        trDto.setConstructionId(this.constructionId);
        trDto.setTmbtTarget(this.tmbtTarget);
        trDto.setTmbtTargetDetail(this.tmbtTargetDetail);
        trDto.setDbTkdaDate(this.dbTkdaDate);
        trDto.setDbTtkdtDate(this.dbTtkdtDate);
        trDto.setDbVtDate(this.dbVtDate);
        trDto.setAutoExpire(this.autoExpire);

        return trDto;

    }
}
