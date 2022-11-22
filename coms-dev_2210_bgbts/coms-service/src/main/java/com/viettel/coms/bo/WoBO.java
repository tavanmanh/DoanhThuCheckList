package com.viettel.coms.bo;

import com.viettel.coms.dto.WoDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WO")
public class WoBO extends BaseFWModelImpl {
    private Long woId;
    private String woCode;
    private String woName;
    private Long woTypeId;
    private Long trId;
    private String state;
    private Long constructionId;
    private Long catWorkItemTypeId;
    private String stationCode;
    private String userCreated;
    private Date createdDate;
    private Date finishDate;
    private Integer qoutaTime;
    private Date acceptTime;
    private Date startTime;
    private Date endTime;
    private String executeMethod;
    private String quantityValue;
    private String cdLevel1;
    private String cdLevel2;
    private String cdLevel3;
    private String cdLevel4;
    private String cdLevel5;
    private Long ftId;
    private String executeLat;
    private String executeLong;
    private Long status;
    private Long totalMonthPlanId;
    private Double moneyValue;
    private String moneyFlowBill;
    private Date moneyFlowDate;
    private Long moneyFlowValue;
    private Long moneyFlowRequired;
    private String moneyFlowContent;
    private Long apConstructionType;
    private Long apWorkSrc;
    private String opinionResult;
    private Long contractId;
    private Long executeChecklist;
    private Long woNameId;
    private Long quantityByDate;
    private Date closedTime;
    private String constructionCode;
    private String contractCode;
    private Long projectId;
    private String projectCode;
    private String cdLevel1Name;
    private String cdLevel2Name;
    private String cdLevel3Name;
    private String cdLevel4Name;
    private String cdLevel5Name;
    private String ftName;
    private String ftEmail;
    private String createdUserFullName;
    private String createdUserEmail;
    private String trCode;
    private Long catConstructionTypeId;
    private Long checklistStep;
    private String catProvinceCode;
    private String userCdLevel2ReceiveWo;
    private Date updateCdLevel2ReceiveWo;
    private String userCdLevel3ReceiveWo;
    private Date updateCdLevel3ReceiveWo;
    private String userCdLevel4ReceiveWo;
    private Date updateCdLevel4ReceiveWo;
    private String userCdLevel5ReceiveWo;
    private Date updateCdLevel5ReceiveWo;
    private String userFtReceiveWo;
    private Date updateFtReceiveWo;
    private String userCdApproveWo;
    private Date updateCdApproveWo;
    private String userTthtApproveWo;
    private Date updateTthtApproveWo;
    private Date approveDateReportWo;
    private Long hcqtProjectId;
    private Date hshcReceiveDate;
    private String type;
    private String hcqtContractCode;
    private String cnkv;

    private String userTcBranchApproveWo;
    private Date updateTcBranchApproveWo;
    private String userTcTctApproveWo;
    private Date updateTcTctApproveWo;

    private String overdueReason;
    private String overdueApproveState;
    private String overdueApprovePerson;

    private String description;
    private String vtnetWoCode;
    private String partner;

    private String woOrder;
    private Long voState;
    private String voRequestDept;
    private String voRequestRole;
    private String voApprovedDept;
    private String voApprovedRole;
    private String voMngtDept;
    private String voMngtRole;

    private Long woOrderId;
    private String businessType;

    private Long woOrderConfirm;
    private String emailTcTct;
    private Long catStationHouseId;
    private String autoExpire;

    private Date createdDate5s;

    
    //Huypq-22102021-start
    private Double moneyValueHtct;
    private String userDthtApprovedWo;
    private Date updateDthtApprovedWo;
    //Huy-end
    //Huypq-02112021-start
    private String opinionType;
    //Huy-end
    //Huypq-14122021-start
    private String stateHtct;
    //Huy-end

    //aeg start 20220529
    private String describeAfterMath;
    //aeg end 20220529
    private String invoicePeriod;
    private Date stationRevenueDate;
    
    private String licenceName;//ducpm23 add 290722

    private String bgbtsResult;



    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "WO_SEQ")})
    @Column(name = "ID")
    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }
    @Column(name = "LICENCE_NAME")
    public String getLicenceName() {
		return licenceName;
	}

	public void setLicenceName(String licenceName) {
		this.licenceName = licenceName;
	}

	@Column(name = "WO_CODE")
    public String getWoCode() {
        return woCode;
    }

    public void setWoCode(String woCode) {
        this.woCode = woCode;
    }

    @Column(name = "WO_NAME")
    public String getWoName() {
        return woName;
    }

    public void setWoName(String woName) {
        this.woName = woName;
    }

    @Column(name = "WO_TYPE_ID")
    public Long getWoTypeId() {
        return woTypeId;
    }

    public void setWoTypeId(Long woTypeId) {
        this.woTypeId = woTypeId;
    }

    @Column(name = "TR_ID")
    public Long getTrId() {
        return trId;
    }

    public void setTrId(Long trId) {
        this.trId = trId;
    }

    @Column(name = "STATE")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "CONSTRUCTION_ID")
    public Long getConstructionId() {
        return constructionId;
    }

    public void setConstructionId(Long constructionId) {
        this.constructionId = constructionId;
    }

    @Column(name = "CAT_WORK_ITEM_TYPE_ID")
    public Long getCatWorkItemTypeId() {
        return catWorkItemTypeId;
    }

    public void setCatWorkItemTypeId(Long catWorkItemTypeId) {
        this.catWorkItemTypeId = catWorkItemTypeId;
    }

    @Column(name = "STATION_CODE")
    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
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

    @Column(name = "FINISH_DATE")
    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    @Column(name = "QOUTA_TIME")
    public Integer getQoutaTime() {
        return qoutaTime;
    }

    public void setQoutaTime(Integer qoutaTime) {
        this.qoutaTime = qoutaTime;
    }

    @Column(name = "EXECUTE_METHOD")
    public String getExecuteMethod() {
        return executeMethod;
    }

    public void setExecuteMethod(String executeMethod) {
        this.executeMethod = executeMethod;
    }

    @Column(name = "QUANTITY_VALUE")
    public String getQuantityValue() {
        return quantityValue;
    }

    public void setQuantityValue(String quantityValue) {
        this.quantityValue = quantityValue;
    }

    @Column(name = "CD_LEVEL_1")
    public String getCdLevel1() {
        return cdLevel1;
    }

    public void setCdLevel1(String cdLevel1) {
        this.cdLevel1 = cdLevel1;
    }

    @Column(name = "CD_LEVEL_2")
    public String getCdLevel2() {
        return cdLevel2;
    }

    public void setCdLevel2(String cdLevel2) {
        this.cdLevel2 = cdLevel2;
    }

    @Column(name = "CD_LEVEL_3")
    public String getCdLevel3() {
        return cdLevel3;
    }

    public void setCdLevel3(String cdLevel3) {
        this.cdLevel3 = cdLevel3;
    }

    @Column(name = "CD_LEVEL_4")
    public String getCdLevel4() {
        return cdLevel4;
    }

    public void setCdLevel4(String cdLevel4) {
        this.cdLevel4 = cdLevel4;
    }

    @Column(name = "FT_ID")
    public Long getFtId() {
        return ftId;
    }

    public void setFtId(Long ftId) {
        this.ftId = ftId;
    }

    @Column(name = "ACCEPT_TIME")
    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    @Column(name = "END_TIME")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    @Column(name = "STATUS")
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Column(name = "TOTAL_MONTH_PLAN_ID")
    public Long getTotalMonthPlanId() {
        return totalMonthPlanId;
    }

    public void setTotalMonthPlanId(Long totalMonthPlanId) {
        this.totalMonthPlanId = totalMonthPlanId;
    }

    @Column(name = "MONEY_VALUE")
    public Double getMoneyValue() {
        return moneyValue;
    }

    public void setMoneyValue(Double moneyValue) {
        this.moneyValue = moneyValue;
    }

    @Column(name = "MONEY_FLOW_BILL")
    public String getMoneyFlowBill() {
        return moneyFlowBill;
    }

    public void setMoneyFlowBill(String moneyFlowBill) {
        this.moneyFlowBill = moneyFlowBill;
    }

    @Column(name = "MONEY_FLOW_DATE")
    public Date getMoneyFlowDate() {
        return moneyFlowDate;
    }

    public void setMoneyFlowDate(Date moneyFlowDate) {
        this.moneyFlowDate = moneyFlowDate;
    }

    @Column(name = "MONEY_FLOW_VALUE")
    public Long getMoneyFlowValue() {
        return moneyFlowValue;
    }

    public void setMoneyFlowValue(Long moneyFlowValue) {
        this.moneyFlowValue = moneyFlowValue;
    }

    @Column(name = "MONEY_FLOW_REQUIRED")
    public Long getMoneyFlowRequired() {
        return moneyFlowRequired;
    }

    public void setMoneyFlowRequired(Long moneyFlowRequired) {
        this.moneyFlowRequired = moneyFlowRequired;
    }

    @Column(name = "MONEY_FLOW_CONTENT")
    public String getMoneyFlowContent() {
        return moneyFlowContent;
    }

    public void setMoneyFlowContent(String moneyFlowContent) {
        this.moneyFlowContent = moneyFlowContent;
    }

    @Column(name = "AP_CONSTRUCTION_TYPE")
    public Long getApConstructionType() {
        return apConstructionType;
    }

    public void setApConstructionType(Long apConstructionType) {
        this.apConstructionType = apConstructionType;
    }

    @Column(name = "AP_WORK_SRC")
    public Long getApWorkSrc() {
        return apWorkSrc;
    }

    public void setApWorkSrc(Long apWorkSrc) {
        this.apWorkSrc = apWorkSrc;
    }

    @Column(name = "START_TIME")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Column(name = "OPINION_RESULT")
    public String getOpinionResult() {
        return opinionResult;
    }

    public void setOpinionResult(String opinionResult) {
        this.opinionResult = opinionResult;
    }

    @Column(name = "EXECUTE_CHECKLIST")
    public Long getExecuteChecklist() {
        return executeChecklist;
    }

    public void setExecuteChecklist(Long executeChecklist) {
        this.executeChecklist = executeChecklist;
    }

    @Column(name = "CONTRACT_ID")
    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    @Column(name = "WO_NAME_ID")
    public Long getWoNameId() {
        return woNameId;
    }

    public void setWoNameId(Long woNameId) {
        this.woNameId = woNameId;
    }

    @Column(name = "QUANTITY_BY_DATE")
    public Long getQuantityByDate() {
        return quantityByDate;
    }

    public void setQuantityByDate(Long quantityByDate) {
        this.quantityByDate = quantityByDate;
    }

    @Column(name = "CLOSED_TIME")
    public Date getClosedTime() {
        return closedTime;
    }

    public void setClosedTime(Date closedTime) {
        this.closedTime = closedTime;
    }

    @Column(name = "CONSTRUCTION_CODE")
    public String getConstructionCode() {
        return constructionCode;
    }

    public void setConstructionCode(String constructionCode) {
        this.constructionCode = constructionCode;
    }

    @Column(name = "CONTRACT_CODE")
    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    @Column(name = "PROJECT_ID")
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Column(name = "PROJECT_CODE")
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @Column(name = "CD_LEVEL_1_NAME")
    public String getCdLevel1Name() {
        return cdLevel1Name;
    }

    public void setCdLevel1Name(String cdLevel1Name) {
        this.cdLevel1Name = cdLevel1Name;
    }

    @Column(name = "CD_LEVEL_2_NAME")
    public String getCdLevel2Name() {
        return cdLevel2Name;
    }

    public void setCdLevel2Name(String cdLevel2Name) {
        this.cdLevel2Name = cdLevel2Name;
    }

    @Column(name = "CD_LEVEL_3_NAME")
    public String getCdLevel3Name() {
        return cdLevel3Name;
    }

    public void setCdLevel3Name(String cdLevel3Name) {
        this.cdLevel3Name = cdLevel3Name;
    }

    @Column(name = "CD_LEVEL_4_NAME")
    public String getCdLevel4Name() {
        return cdLevel4Name;
    }

    public void setCdLevel4Name(String cdLevel4Name) {
        this.cdLevel4Name = cdLevel4Name;
    }

    @Column(name = "FT_NAME")
    public String getFtName() {
        return ftName;
    }

    public void setFtName(String ftName) {
        this.ftName = ftName;
    }

    @Column(name = "CREATED_USER_FULL_NAME")
    public String getCreatedUserFullName() {
        return createdUserFullName;
    }

    public void setCreatedUserFullName(String createdUserFullName) {
        this.createdUserFullName = createdUserFullName;
    }

    @Column(name = "FT_EMAIL")
    public String getFtEmail() {
        return ftEmail;
    }

    public void setFtEmail(String ftEmail) {
        this.ftEmail = ftEmail;
    }

    @Column(name = "CREATED_USER_EMAIL")
    public String getCreatedUserEmail() {
        return createdUserEmail;
    }

    public void setCreatedUserEmail(String createdUserEmail) {
        this.createdUserEmail = createdUserEmail;
    }

    @Column(name = "TR_CODE")
    public String getTrCode() {
        return trCode;
    }

    public void setTrCode(String trCode) {
        this.trCode = trCode;
    }

    @Column(name = "CAT_CONSTRUCTION_TYPE_ID")
    public Long getCatConstructionTypeId() {
        return catConstructionTypeId;
    }

    public void setCatConstructionTypeId(Long catConstructionTypeId) {
        this.catConstructionTypeId = catConstructionTypeId;
    }

    @Column(name = "CHECKLIST_STEP")
    public Long getChecklistStep() {
        return checklistStep;
    }

    public void setChecklistStep(Long checklistStep) {
        this.checklistStep = checklistStep;
    }

    @Column(name = "CAT_PROVINCE_CODE")
    public String getCatProvinceCode() {
        return catProvinceCode;
    }

    public void setCatProvinceCode(String catProvinceCode) {
        this.catProvinceCode = catProvinceCode;
    }

    @Column(name = "USER_CD_LEVEL2_RECEIVE_WO")
    public String getUserCdLevel2ReceiveWo() {
        return userCdLevel2ReceiveWo;
    }

    public void setUserCdLevel2ReceiveWo(String userCdLevel2ReceiveWo) {
        this.userCdLevel2ReceiveWo = userCdLevel2ReceiveWo;
    }

    @Column(name = "UPDATE_CD_LEVEL2_RECEIVE_WO")
    public Date getUpdateCdLevel2ReceiveWo() {
        return updateCdLevel2ReceiveWo;
    }

    public void setUpdateCdLevel2ReceiveWo(Date updateCdLevel2ReceiveWo) {
        this.updateCdLevel2ReceiveWo = updateCdLevel2ReceiveWo;
    }

    @Column(name = "USER_CD_LEVEL3_RECEIVE_WO")
    public String getUserCdLevel3ReceiveWo() {
        return userCdLevel3ReceiveWo;
    }

    public void setUserCdLevel3ReceiveWo(String userCdLevel3ReceiveWo) {
        this.userCdLevel3ReceiveWo = userCdLevel3ReceiveWo;
    }

    @Column(name = "UPDATE_CD_LEVEL3_RECEIVE_WO")
    public Date getUpdateCdLevel3ReceiveWo() {
        return updateCdLevel3ReceiveWo;
    }

    public void setUpdateCdLevel3ReceiveWo(Date updateCdLevel3ReceiveWo) {
        this.updateCdLevel3ReceiveWo = updateCdLevel3ReceiveWo;
    }

    @Column(name = "USER_CD_LEVEL4_RECEIVE_WO")
    public String getUserCdLevel4ReceiveWo() {
        return userCdLevel4ReceiveWo;
    }

    public void setUserCdLevel4ReceiveWo(String userCdLevel4ReceiveWo) {
        this.userCdLevel4ReceiveWo = userCdLevel4ReceiveWo;
    }

    @Column(name = "UPDATE_CD_LEVEL4_RECEIVE_WO")
    public Date getUpdateCdLevel4ReceiveWo() {
        return updateCdLevel4ReceiveWo;
    }

    public void setUpdateCdLevel4ReceiveWo(Date updateCdLevel4ReceiveWo) {
        this.updateCdLevel4ReceiveWo = updateCdLevel4ReceiveWo;
    }

    @Column(name = "USER_CD_APPROVE_WO")
    public String getUserCdApproveWo() {
        return userCdApproveWo;
    }

    public void setUserCdApproveWo(String userCdApproveWo) {
        this.userCdApproveWo = userCdApproveWo;
    }

    @Column(name = "UPDATE_CD_APPROVE_WO")
    public Date getUpdateCdApproveWo() {
        return updateCdApproveWo;
    }

    public void setUpdateCdApproveWo(Date updateCdApproveWo) {
        this.updateCdApproveWo = updateCdApproveWo;
    }

    @Column(name = "USER_TTHT_APPROVE_WO")
    public String getUserTthtApproveWo() {
        return userTthtApproveWo;
    }

    public void setUserTthtApproveWo(String userTthtApproveWo) {
        this.userTthtApproveWo = userTthtApproveWo;
    }

    @Column(name = "UPDATE_TTHT_APPROVE_WO")
    public Date getUpdateTthtApproveWo() {
        return updateTthtApproveWo;
    }

    public void setUpdateTthtApproveWo(Date updateTthtApproveWo) {
        this.updateTthtApproveWo = updateTthtApproveWo;
    }

    @Column(name = "APPROVE_DATE_REPORT_WO")
    public Date getApproveDateReportWo() {
        return approveDateReportWo;
    }

    public void setApproveDateReportWo(Date approveDateReportWo) {
        this.approveDateReportWo = approveDateReportWo;
    }

    @Column(name = "HCQT_PROJECT_ID")
    public Long getHcqtProjectId() {
        return hcqtProjectId;
    }

    public void setHcqtProjectId(Long hcqtProjectId) {
        this.hcqtProjectId = hcqtProjectId;
    }

    @Column(name = "HSHC_RECEIVE_DATE")
    public Date getHshcReceiveDate() {
        return hshcReceiveDate;
    }

    public void setHshcReceiveDate(Date hshcReceiveDate) {
        this.hshcReceiveDate = hshcReceiveDate;
    }

    @Column(name = "TYPE")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "HCQT_CONTRACT_CODE")
    public String getHcqtContractCode() {
        return hcqtContractCode;
    }

    public void setHcqtContractCode(String hcqtContractCode) {
        this.hcqtContractCode = hcqtContractCode;
    }

    @Column(name = "CNKV")
    public String getCnkv() {
        return cnkv;
    }

    public void setCnkv(String cnkv) {
        this.cnkv = cnkv;
    }

    @Column(name = "USER_FT_RECEIVE_WO")
    public String getUserFtReceiveWo() {
        return userFtReceiveWo;
    }

    public void setUserFtReceiveWo(String userFtReceiveWo) {
        this.userFtReceiveWo = userFtReceiveWo;
    }

    @Column(name = "UPDATE_FT_RECEIVE_WO")
    public Date getUpdateFtReceiveWo() {
        return updateFtReceiveWo;
    }

    public void setUpdateFtReceiveWo(Date updateFtReceiveWo) {
        this.updateFtReceiveWo = updateFtReceiveWo;
    }

    @Column(name = "CD_LEVEL_5")
    public String getCdLevel5() {
        return cdLevel5;
    }

    public void setCdLevel5(String cdLevel5) {
        this.cdLevel5 = cdLevel5;
    }

    @Column(name = "CD_LEVEL_5_NAME")
    public String getCdLevel5Name() {
        return cdLevel5Name;
    }

    public void setCdLevel5Name(String cdLevel5Name) {
        this.cdLevel5Name = cdLevel5Name;
    }

    @Column(name = "USER_CD_LEVEL5_RECEIVE_WO")
    public String getUserCdLevel5ReceiveWo() {
        return userCdLevel5ReceiveWo;
    }

    public void setUserCdLevel5ReceiveWo(String userCdLevel5ReceiveWo) {
        this.userCdLevel5ReceiveWo = userCdLevel5ReceiveWo;
    }

    @Column(name = "UPDATE_CD_LEVEL5_RECEIVE_WO")
    public Date getUpdateCdLevel5ReceiveWo() {
        return updateCdLevel5ReceiveWo;
    }

    public void setUpdateCdLevel5ReceiveWo(Date updateCdLevel5ReceiveWo) {
        this.updateCdLevel5ReceiveWo = updateCdLevel5ReceiveWo;
    }

    //Huypq-05012021-start
    private Long alarmId;

    @Column(name = "ALARM_ID")
    public Long getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Long alarmId) {
        this.alarmId = alarmId;
    }
    //Huy-end

    @Column(name = "USER_TC_BRANCH_APPROVE_WO")
    public String getUserTcBranchApproveWo() {
        return userTcBranchApproveWo;
    }

    public void setUserTcBranchApproveWo(String userTcBranchApproveWo) {
        this.userTcBranchApproveWo = userTcBranchApproveWo;
    }

    @Column(name = "UPDATE_TC_BRANCH_APPROVE_WO")
    public Date getUpdateTcBranchApproveWo() {
        return updateTcBranchApproveWo;
    }

    public void setUpdateTcBranchApproveWo(Date updateTcBranchApproveWo) {
        this.updateTcBranchApproveWo = updateTcBranchApproveWo;
    }

    @Column(name = "USER_TC_TCT_APPROVE_WO")
    public String getUserTcTctApproveWo() {
        return userTcTctApproveWo;
    }

    public void setUserTcTctApproveWo(String userTcTctApproveWo) {
        this.userTcTctApproveWo = userTcTctApproveWo;
    }

    @Column(name = "UPDATE_TC_TCT_APPROVE_WO")
    public Date getUpdateTcTctApproveWo() {
        return updateTcTctApproveWo;
    }

    public void setUpdateTcTctApproveWo(Date updateTcTctApproveWo) {
        this.updateTcTctApproveWo = updateTcTctApproveWo;
    }

    @Column(name = "OVERDUE_REASON")
    public String getOverdueReason() {
        return overdueReason;
    }

    public void setOverdueReason(String overdueReason) {
        this.overdueReason = overdueReason;
    }

    @Column(name = "OVERDUE_APPROVE_STATE")
    public String getOverdueApproveState() {
        return overdueApproveState;
    }

    public void setOverdueApproveState(String overdueApproveState) {
        this.overdueApproveState = overdueApproveState;
    }

    @Column(name = "OVERDUE_APPROVE_PERSON")
    public String getOverdueApprovePerson() {
        return overdueApprovePerson;
    }

    public void setOverdueApprovePerson(String overdueApprovePerson) {
        this.overdueApprovePerson = overdueApprovePerson;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "VTNET_WO_CODE")
    public String getVtnetWoCode() {
        return vtnetWoCode;
    }

    public void setVtnetWoCode(String vtnetWoCode) {
        this.vtnetWoCode = vtnetWoCode;
    }

    @Column(name = "PARTNER")
    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    @Column(name = "WO_ORDER")
    public String getWoOrder() {
        return woOrder;
    }

    public void setWoOrder(String woOrder) {
        this.woOrder = woOrder;
    }

    @Column(name = "VO_STATE")
    public Long getVoState() {
        return voState;
    }

    public void setVoState(Long voState) {
        this.voState = voState;
    }

    @Column(name = "VO_REQUEST_DEPT")
    public String getVoRequestDept() {
        return voRequestDept;
    }

    public void setVoRequestDept(String voRequestDept) {
        this.voRequestDept = voRequestDept;
    }

    @Column(name = "VO_REQUEST_ROLE")
    public String getVoRequestRole() {
        return voRequestRole;
    }

    public void setVoRequestRole(String voRequestRole) {
        this.voRequestRole = voRequestRole;
    }

    @Column(name = "VO_APPROVED_DEPT")
    public String getVoApprovedDept() {
        return voApprovedDept;
    }

    public void setVoApprovedDept(String voApprovedDept) {
        this.voApprovedDept = voApprovedDept;
    }

    @Column(name = "VO_APPROVED_ROLE")
    public String getVoApprovedRole() {
        return voApprovedRole;
    }

    public void setVoApprovedRole(String voApprovedRole) {
        this.voApprovedRole = voApprovedRole;
    }

    @Column(name = "VO_MNGT_DEPT")
    public String getVoMngtDept() {
        return voMngtDept;
    }

    public void setVoMngtDept(String voMngtDept) {
        this.voMngtDept = voMngtDept;
    }

    @Column(name = "VO_MNGT_ROLE")
    public String getVoMngtRole() {
        return voMngtRole;
    }

    public void setVoMngtRole(String voMngtRole) {
        this.voMngtRole = voMngtRole;
    }

    @Column(name = "WO_ORDER_ID")
    public Long getWoOrderId() {
        return woOrderId;
    }

    public void setWoOrderId(Long woOrderId) {
        this.woOrderId = woOrderId;
    }

    @Column(name = "BUSINESS_TYPE")
    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    @Column(name = "WO_ORDER_CONFIRM")
    public Long getWoOrderConfirm() {
        return woOrderConfirm;
    }

    public void setWoOrderConfirm(Long woOrderConfirm) {
        this.woOrderConfirm = woOrderConfirm;
    }

    @Column(name = "EMAIL_TC_TCT")
    public String getEmailTcTct() {
        return emailTcTct;
    }

    public void setEmailTcTct(String emailTcTct) {
        this.emailTcTct = emailTcTct;
    }

    @Column(name = "CAT_STATION_HOUSE_ID")
    public Long getCatStationHouseId() {
        return catStationHouseId;
    }

    public void setCatStationHouseId(Long catStationHouseId) {
        this.catStationHouseId = catStationHouseId;
    }
    
    @Column(name = "AUTO_EXPIRE")
    public String getAutoExpire() {
		return autoExpire;
	}

	public void setAutoExpire(String autoExpire) {
		this.autoExpire = autoExpire;
	}

	@Column(name = "CREATED_DATE_5S")
	public Date getCreatedDate5s() {
		return createdDate5s;
	}

	public void setCreatedDate5s(Date createdDate5s) {
		this.createdDate5s = createdDate5s;
	}

	@Column(name = "MONEY_VALUE_HTCT")
	public Double getMoneyValueHtct() {
		return moneyValueHtct;
	}

	public void setMoneyValueHtct(Double moneyValueHtct) {
		this.moneyValueHtct = moneyValueHtct;
	}

	@Column(name = "USER_DTHT_APPROVED_WO")
	public String getUserDthtApprovedWo() {
		return userDthtApprovedWo;
	}

	public void setUserDthtApprovedWo(String userDthtApprovedWo) {
		this.userDthtApprovedWo = userDthtApprovedWo;
	}

	@Column(name = "UPDATE_DTHT_APPROVED_WO")
	public Date getUpdateDthtApprovedWo() {
		return updateDthtApprovedWo;
	}

	public void setUpdateDthtApprovedWo(Date updateDthtApprovedWo) {
		this.updateDthtApprovedWo = updateDthtApprovedWo;
	}

	@Column(name = "OPINION_TYPE")
	public String getOpinionType() {
		return opinionType;
	}

	public void setOpinionType(String opinionType) {
		this.opinionType = opinionType;
	}

	@Column(name = "STATE_HTCT")
	public String getStateHtct() {
		return stateHtct;
	}

	public void setStateHtct(String stateHtct) {
		this.stateHtct = stateHtct;
	}

    @Column(name = "DESCRIBE_AFTERMATH")
    public String getDescribeAfterMath() {

        return describeAfterMath;
    }

    public void setDescribeAfterMath(String describeAfterMath) {

        this.describeAfterMath = describeAfterMath;
    }

    @Column(name = "INVOICE_PERIOD")
    public String getInvoicePeriod() {
		return invoicePeriod;
	}

	public void setInvoicePeriod(String invoicePeriod) {
		this.invoicePeriod = invoicePeriod;
	}

	@Column(name = "STATION_REVENUE_DATE")
	public Date getStationRevenueDate() {
		return stationRevenueDate;
	}

	public void setStationRevenueDate(Date stationRevenueDate) {
		this.stationRevenueDate = stationRevenueDate;
	}

    @Column(name = "BGBTS_RESULT")
    public String getBgbtsResult() {
        return bgbtsResult;
    }

    public void setBgbtsResult(String bgbtsResult) {
        this.bgbtsResult = bgbtsResult;
    }

	@Override
    public WoDTO toDTO() {
        WoDTO woDto = new WoDTO();
        woDto.setWoId(this.woId);
        woDto.setWoCode(this.woCode);
        woDto.setWoName(this.woName);
        woDto.setWoTypeId(this.woTypeId);
        woDto.setState(this.state);
        woDto.setConstructionId(this.constructionId);
        woDto.setCatWorkItemTypeId(this.catWorkItemTypeId);
        woDto.setStationCode(this.stationCode);
        woDto.setUserCreated(this.userCreated);
        woDto.setCreatedDate(this.createdDate);
        woDto.setFinishDate(this.finishDate);
        woDto.setQoutaTime(this.qoutaTime);
        woDto.setExecuteMethod(this.executeMethod);
        woDto.setQuantityValue(this.quantityValue);
        woDto.setCdLevel1(this.cdLevel1);
        woDto.setCdLevel2(this.cdLevel2);
        woDto.setCdLevel3(this.cdLevel3);
        woDto.setCdLevel4(this.cdLevel4);
        woDto.setCdLevel5(this.cdLevel5);
        woDto.setFtId(this.ftId);
        woDto.setAcceptTime(this.acceptTime);
        woDto.setExecuteLat(this.executeLat);
        woDto.setExecuteLong(this.executeLong);
        woDto.setStatus(this.status);
        woDto.setTotalMonthPlanId(this.totalMonthPlanId);
        woDto.setMoneyValue(this.moneyValue);
        woDto.setMoneyFlowBill(this.moneyFlowBill);
        woDto.setMoneyFlowDate(this.moneyFlowDate);
        woDto.setMoneyFlowValue(this.moneyFlowValue);
        woDto.setMoneyFlowRequired(this.moneyFlowRequired);
        woDto.setMoneyFlowContent(this.moneyFlowContent);
        woDto.setApConstructionType(this.apConstructionType);
        woDto.setApWorkSrc(this.apWorkSrc);
        woDto.setEndTime(this.endTime);
        woDto.setStartTime(this.startTime);
        woDto.setOpinionResult(this.opinionResult);
        woDto.setContractId(this.contractId);
        woDto.setExecuteChecklist(this.executeChecklist);
        woDto.setWoNameId(this.woNameId);
        woDto.setQuantityByDate(this.quantityByDate);
        woDto.setClosedTime(this.closedTime);
        woDto.setConstructionCode(this.constructionCode);
        woDto.setContractCode(this.contractCode);
        woDto.setProjectId(this.projectId);
        woDto.setProjectCode(this.projectCode);
        woDto.setCdLevel1Name(this.cdLevel1Name);
        woDto.setCdLevel2Name(this.cdLevel2Name);
        woDto.setCdLevel3Name(this.cdLevel3Name);
        woDto.setCdLevel4Name(this.cdLevel4Name);
        woDto.setCdLevel5Name(this.cdLevel5Name);
        woDto.setFtName(this.ftName);
        woDto.setFtEmail(this.ftEmail);
        woDto.setCreatedUserFullName(this.createdUserFullName);
        woDto.setCreatedUserEmail(this.createdUserEmail);
        woDto.setTrCode(this.trCode);
        woDto.setCatConstructionTypeId(this.catConstructionTypeId);
        woDto.setChecklistStep(this.checklistStep);
        woDto.setCatProvinceCode(this.catProvinceCode);
        woDto.setUserCdLevel2ReceiveWo(this.userCdLevel2ReceiveWo);
        woDto.setUserCdLevel3ReceiveWo(this.userCdLevel3ReceiveWo);
        woDto.setUserCdLevel4ReceiveWo(this.userCdLevel4ReceiveWo);
        woDto.setUserCdLevel5ReceiveWo(this.userCdLevel5ReceiveWo);
        woDto.setUserFtReceiveWo(this.userFtReceiveWo);
        woDto.setUpdateCdLevel2ReceiveWo(this.updateCdLevel2ReceiveWo);
        woDto.setUpdateCdLevel3ReceiveWo(this.updateCdLevel3ReceiveWo);
        woDto.setUpdateCdLevel4ReceiveWo(this.updateCdLevel4ReceiveWo);
        woDto.setUpdateCdLevel5ReceiveWo(this.updateCdLevel5ReceiveWo);
        woDto.setUpdateFtReceiveWo(this.updateFtReceiveWo);
        woDto.setUserCdApproveWo(this.userCdApproveWo);
        woDto.setUpdateCdApproveWo(this.updateCdApproveWo);
        woDto.setUserTthtApproveWo(this.userTthtApproveWo);
        woDto.setUpdateTthtApproveWo(this.updateTthtApproveWo);
        woDto.setApproveDateReportWo(this.approveDateReportWo);
        woDto.setHcqtProjectId(this.hcqtProjectId);
        woDto.setHshcReceiveDate(this.hshcReceiveDate);
        woDto.setHcqtContractCode(this.hcqtContractCode);
        woDto.setCnkv(this.cnkv);
        woDto.setAlarmId(this.alarmId);

        woDto.setUserTcBranchApproveWo(this.userTcBranchApproveWo);
        woDto.setUpdateTcBranchApproveWo(this.updateTcBranchApproveWo);
        woDto.setUserTcTctApproveWo(this.userTcTctApproveWo);
        woDto.setUpdateTcTctApproveWo(this.updateTcTctApproveWo);

        woDto.setOverdueReason(this.overdueReason);
        woDto.setOverdueApproveState(this.overdueApproveState);
        woDto.setOverdueApprovePerson(this.overdueApprovePerson);

        woDto.setDescription(this.description);
        woDto.setVtnetWoCode(this.vtnetWoCode);
        woDto.setPartner(this.partner);

        woDto.setWoOrder(this.woOrder);
        woDto.setVoState(this.voState);
        woDto.setVoRequestDept(this.voRequestDept);
        woDto.setVoRequestRole(this.voRequestRole);
        woDto.setVoApprovedDept(this.voApprovedDept);
        woDto.setVoApprovedRole(this.voApprovedRole);
        woDto.setVoMngtDept(this.voMngtDept);
        woDto.setVoMngtRole(this.voMngtRole);
        woDto.setWoOrderId(this.woOrderId);
        woDto.setBusinessType(this.businessType);
        woDto.setWoOrderConfirm(this.woOrderConfirm);
        woDto.setEmailTcTct(this.emailTcTct);
        woDto.setCatStationHouseId(this.catStationHouseId);
        woDto.setTrId(this.trId);
        woDto.setAutoExpire(this.autoExpire);
        woDto.setCreatedDate5s(this.createdDate5s);
        woDto.setMoneyValueHtct(this.moneyValueHtct);
        woDto.setUserDthtApprovedWo(this.userDthtApprovedWo);
        woDto.setUpdateDthtApprovedWo(this.updateDthtApprovedWo);
        woDto.setOpinionType(this.opinionType);
        woDto.setStateHtct(this.stateHtct);
        woDto.setLicenceName(this.licenceName);

        woDto.setDescribeAfterMath(this.describeAfterMath);

        woDto.setPmtStatus(this.pmtStatus);
        woDto.setInvoicePeriod(this.invoicePeriod);
        woDto.setStationRevenueDate(this.stationRevenueDate);
        woDto.setBgbtsResult(this.bgbtsResult);
        return woDto;
    }

    //trungtv-start

    private Integer pmtStatus;

    @Column(name = "PMT_STATUS")
    public Integer getPmtStatus() {
        return pmtStatus;
    }

    public void setPmtStatus(Integer pmtStatus) {
        this.pmtStatus = pmtStatus;
    }

    //trungtv-end

}
